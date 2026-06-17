// zk-worker.js
import { zkp_proof } from './ZKP.mjs'; // 确保路径正确

onmessage = async function (event) {
  const [m, r] = event.data;
  try {
    const [proof, commitment] = await zkp_proof(m, r);
    postMessage({ zkp_proof: proof, zkp_cmt: commitment });
  } catch (error) {
    postMessage({ error: error.message });
  }
};
