from watermark_model import Watermark
import torch
import numpy as np
import argparse
device = 'cuda' if torch.cuda.is_available() else 'cpu'
torch.set_printoptions(sci_mode=False,profile='full')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='decoder-encoder pretrain')
    parser.add_argument('--secret_length', default=48, type=int)
    args =parser.parse_known_args()[0]
    
#Load DiffuseTrace_Pretrain Model
model=Watermark(secret_length=args.secret_length).to(device)
model.load_state_dict(torch.load('./model48bit.pth'))
model.eval()

Metric,Loss=[],[]
for i in range(100):
    #secret key
    secret=torch.Tensor(np.random.choice([0, 1], size=(args.secret_length))).to(device)
    secret = secret.unsqueeze(-1).unsqueeze(-1).unsqueeze(0)
    secret = secret.expand(-1,-1,64,64)
    
    #encoder/decoder
    restore, mean, logvar = model(secret)

    #wm initial latents generation
    mean=mean.reshape(-1,64,64)
    logvar=logvar.reshape(-1,64,64)
    eps = torch.randn_like(logvar)
    std = torch.exp(logvar / 2)
    matrix = eps * std + mean
    
    #original secret
    original_tensor = torch.mean(secret, dim=(-2, -1)).flatten()
    
    #predicted secret
    restore_pred = torch.round(torch.mean(restore[0], dim=(-2, -1)))
    restore_tensor = torch.mean(restore[0], dim=(-2, -1))

    #bit error
    biterror=torch.sum(abs(original_tensor-restore_pred))
    
    #loss
    loss = torch.nn.MSELoss()(restore_tensor, original_tensor)
    
    Metric.append(biterror)
    Loss.append(loss)

#bit error
biterror=torch.mean(torch.stack(Metric))

#bit-error percentage
biterrper=biterror/args.secret_length

#average loss
metric=torch.mean(torch.stack(Loss))

#print the output
print(f'loss=',f'{metric.cpu().detach():.4f}')
print(f'loss bits=',f'{biterror.cpu().detach().numpy():.1f}')
print(f'bitacc={(1-biterrper)*100:.2f}%')
