import { initialize } from "zokrates-js";
import fs from 'fs';
import path from 'path';
import JSONStream from 'JSONStream';
import * as msgpack from '@msgpack/msgpack';
  
initialize().then((zokratesProvider) => {
  const source = `
    
    // D(num) to B(num)
def D2B(u64 mut num) -> bool[64] {
    // rem = num % 2
    u64 mut rem = 0;
    // quot = num / 2
    u64 mut quot = 0;
    // Binary array
    bool[64] mut res = [false; 64];
    // loop
    for u32 i in 0..64 {
        rem = num % 2;
        quot = num / 2;
        num = quot;
        res[i] = if rem == 1 {true} else {false};
    }
    return res;
}

// num mod p
def mod(u64 num, u64 p) -> u64 {
    return num % p;
}

// mult table
def mult(u64 num, u64 p) -> u64[64] {
    u64[64] mut res = [0; 64];
    res[0] = mod(num, p);
    for u32 i in 1..64 {
        res[i] = res[i - 1] * res[i - 1];
        res[i] = mod(res[i], p);
    }
    return res;
}

// fast pow
def pow(u64 num, u64 n, u64 p) -> u64 {
    assert(n >= 1);
    bool[64] binary = D2B(n);
    u64[64] table = mult(num, p);
    u64 mut res = 1;
    for u32 i in 0..64 {
        res = if binary[i] == true {res * table[i]} else {res};
        res = mod(res, p);
    }
    return res;
}

// Pedersen
def cmt(u64 g, u64 h, u64 m, u64 r, u64 p) -> u64 {
    u64 g_m = pow(g, m, p);
    u64 h_r = pow(h, r, p);
    u64 res = g_m * h_r;
    return mod(res, p);
}

def main(u64 g, u64 h, private u64 m, private u64 r, u64 d, u64 p) -> u64 {
    assert(m < d);
    return cmt(g, h, m, r, p);
}



    `;

  // 文件系统解析器函数
  const fileSystemResolver = (from, to) => {
    const location = path.resolve(path.dirname(path.resolve(from)), to);
    const source = fs.readFileSync(location).toString();
    return { source, location };
  };
  // compilation
  const artifacts = zokratesProvider.compile(source);

  // computation
  const { witness, output } = zokratesProvider.computeWitness(artifacts, ["56544564", "237684576", "597", "53837939", "8000", "67108859"]);

  // run setup
  const keypair = zokratesProvider.setup(artifacts.program);

  // generate proof
  const proof = zokratesProvider.generateProof(
    artifacts.program,
    witness,
    keypair.pk
  );
  console.log('Proof:', proof);
  // export solidity verifier
  const verifier = zokratesProvider.exportSolidityVerifier(keypair.vk);

  // or verify off-chain
  const isVerified = zokratesProvider.verify(keypair.vk, proof);
  console.log('Is verified:', isVerified);

  // 使用 MessagePack 序列化数据并保存到文件
  const saveDataAsMessagePack = () => {
    try {
      const data = { artifacts, keypair, verifier };
      const encoded = msgpack.encode(data);
      fs.writeFileSync('data.msgpack', encoded);
      console.log('Data saved as MessagePack successfully.');
    } catch (error) {
      console.error('Error saving data as MessagePack:', error);
    }
  };

  // 调用函数保存数据
  saveDataAsMessagePack();
}); 

