import { initialize } from "zokrates-js";
import * as msgpack from '@msgpack/msgpack';
import fetch from 'node-fetch';
// 从 MessagePack 文件中加载数据
const loadDataFromMessagePack = async () => {
  try {
    const response = await fetch('/data.msgpack');
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const arrayBuffer = await response.arrayBuffer();
    const data = msgpack.decode(new Uint8Array(arrayBuffer));
    return data;
  } catch (error) {
    console.error('Error loading data from MessagePack:', error);
    return null;
  }
};

const zkp_proof = async (m, r) => {
  const g = "56544564";
  const h = "237684576";
  const d = "800000";
  const p = "67108859";
  m = m.toString();
  r = r.toString();

  // 初始化 ZoKrates
  const zokratesProvider = await initialize();
  
  console.log("zokratesProvider:", zokratesProvider);
  console.log("WASM Enabled:", zokratesProvider !== undefined);

  // 从 MessagePack 文件加载数据
  const loadedData = await loadDataFromMessagePack();
  console.log("finished loading data");
  if (!loadedData) {
    console.error('Failed to load data from MessagePack.');
    return;
  }

  const start = new Date();
  console.log("start time:", start);
  const { artifacts, keypair } = loadedData;

  // 计算 output
  const { witness, output } = zokratesProvider.computeWitness(artifacts, [g, h, m, r, d, p]);
  const end = new Date();
  console.log("end time:", end);
  console.log("time used_witness:", (end - start) / 1000, "s");

  //console.log('cmt_zk:', output);

  return [output];
};

export { zkp_proof };

// 示例调用
// zkp_proof("800", "57852638");
