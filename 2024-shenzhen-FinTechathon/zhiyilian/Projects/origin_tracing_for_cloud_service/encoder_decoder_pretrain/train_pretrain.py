import torch
import torch.nn.functional as F
from torch.utils.data import Dataset,DataLoader
from watermark_model import Watermark
import argparse
import numpy as np

@torch.no_grad()
def reconstruct(device,model,args):
        #secret key generation
        X=[]
        for j in range(args.batchsize):
                binary=torch.Tensor(np.random.choice([0, 1], size=(args.secret_length))).to(device)
                binary = binary.unsqueeze(-1).unsqueeze(-1).unsqueeze(0)
                binary = binary.expand(-1,-1,64,64)
                X.append(binary)
        x=torch.cat(X,dim=0)
        
        #encoder/decoder
        model_output = model(x)
        
        #input/output
        input = x.detach().cpu()
        output = model_output[0].detach().cpu()
        
        #secret/resotre secret
        original_secret = torch.mean(input, dim=(-2, -1))
        restore_secret = torch.round(torch.mean(output, dim=(-2, -1)))
        
        #print biterror
        print(f'bit error={torch.sum(abs(original_secret-restore_secret))/args.batchsize}')
        
def train(device, model, optimizer,args):
    # train
    for i in range(args.steps):
        
            #secret key generation
            X=[]
            for j in range(args.batchsize):
                secret=torch.Tensor(np.random.choice([0, 1], size=(args.secret_length))).to(device)
                secret = secret.unsqueeze(-1).unsqueeze(-1).unsqueeze(0)
                secret = secret.expand(-1,-1,64,64)
                X.append(secret)
            x=torch.cat(X,dim=0)
            
            #encoder/decoder
            restore, mean, logvar = model(x)
            
            #calculate loss and backward
            recloss=F.mse_loss(restore,x,reduction='sum')
            kl_loss = torch.mean(
            -0.5 * torch.sum(1 + logvar - mean**2 - torch.exp(logvar), 1), 0)
            loss=args.rec_weight*recloss+args.kl_weight*kl_loss
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()
            
            #test and save model
            if i%500==0:
                print(f'step {i}: recloss:{recloss} klloss:{kl_loss} loss={loss}')
                reconstruct(device, model, args)
                torch.save(model.state_dict(),'model48bit.pth') 
                     
    return model
        

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='encoder-decoder pretraining')
    parser.add_argument('--secret_length', default=48, type=int)
    parser.add_argument('--steps', default=10000000, type=int)
    parser.add_argument('--kl_weight', default=1, type=float)
    parser.add_argument('--rec_weight', default=1, type=float)
    parser.add_argument('--lr', default=0.0005, type=float)
    parser.add_argument('--batchsize', default=32, type=int)
    parser.add_argument('--load_path', default='./model48bit.pth', type=str)
    parser.add_argument('--save_path', default='./model48bit.pth', type=str)
    args =parser.parse_known_args()[0]
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    
    #DiffuseTrace Model
    model=Watermark(secret_length=args.secret_length).to(device)
    
    #optimizer
    optimizer = torch.optim.Adam(model.parameters(), args.lr)
    
    #load DiffuseTrace Model
    if args.load_path != None:
        model.load_state_dict(torch.load(args.load_path))
    
    #Train
    model=train(device, model, optimizer,args)
    
    #Save Model
    torch.save(model.state_dict(),args.save_path)