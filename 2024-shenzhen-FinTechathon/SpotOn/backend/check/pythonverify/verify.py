import json
import sys

from web3 import Web3
import time
def hex_to_uint256(hex_str):
	return int(hex_str, 16)
def verify(data):
    # 连接到Infura节点（替换为你的Infura项目ID和节点URL）
    web3 = Web3(Web3.HTTPProvider('http://localhost:7545'))
    # # 设置账户和私钥
    # private_key = '0xca4d44e3fa4942e41555095e11d65585567b2af7c5ec32d6f29324d0f034a5e7'  # 请确保私钥的安全
    # account=Account.from_key(private_key)
    contract_address = '0x2335Efa031Ffa51bd618E5097C8e11B1090f1Cb9'
    contract_abi = [
        {
            "inputs": [
                {
                    "components": [
                        {
                            "components": [
                                {
                                    "internalType": "uint256",
                                    "name": "X",
                                    "type": "uint256"
                                },
                                {
                                    "internalType": "uint256",
                                    "name": "Y",
                                    "type": "uint256"
                                }
                            ],
                            "internalType": "struct Pairing.G1Point",
                            "name": "a",
                            "type": "tuple"
                        },
                        {
                            "components": [
                                {
                                    "internalType": "uint256[2]",
                                    "name": "X",
                                    "type": "uint256[2]"
                                },
                                {
                                    "internalType": "uint256[2]",
                                    "name": "Y",
                                    "type": "uint256[2]"
                                }
                            ],
                            "internalType": "struct Pairing.G2Point",
                            "name": "b",
                            "type": "tuple"
                        },
                        {
                            "components": [
                                {
                                    "internalType": "uint256",
                                    "name": "X",
                                    "type": "uint256"
                                },
                                {
                                    "internalType": "uint256",
                                    "name": "Y",
                                    "type": "uint256"
                                }
                            ],
                            "internalType": "struct Pairing.G1Point",
                            "name": "c",
                            "type": "tuple"
                        }
                    ],
                    "internalType": "struct Verifier.Proof",
                    "name": "proof",
                    "type": "tuple"
                },
                {
                    "internalType": "uint256[5]",
                    "name": "input",
                    "type": "uint256[5]"
                }
            ],
            "name": "verifyTx",
            "outputs": [
                {
                    "internalType": "bool",
                    "name": "r",
                    "type": "bool"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        }
    ]
    contract = web3.eth.contract(address=contract_address, abi=contract_abi)
    print("start")
    t = time.time() * 1000

    if data:
        # 访问字典中的值
        scheme = data.get("scheme", "")
        curve = data.get("curve", "")
        proof = data.get("proof", {})
        cmt_zk = data.get("cmt_zk", "")
        inputs = data.get("inputs", [])

        # 打印访问到的值
        print(f"Scheme: {scheme}")
        print(f"Curve: {curve}")
        print(f"Proof: {proof}")
        print(f"Cmt_zk: {cmt_zk}")
        print(f"Inputs: {inputs}")

        # 访问嵌套字典中的值
        if proof:
            a= proof.get("a", [])
            b = proof.get("b", [])
            c = proof.get("c", [])

            try:
                # 将十六进制字符串转换为整数
                a = (int(a[0], 16), int(a[1], 16))
                b = ((int(b[0][0], 16), int(b[0][1], 16)), (int(b[1][0], 16), int(b[1][1], 16)))
                c = (int(c[0], 16), int(c[1], 16))
                inputs = [int(i, 16) for i in inputs]
            except (ValueError, IndexError) as e:
                print(f"Error during conversion: {e}")
                # 处理错误，例如返回错误信息或默认值
                a = (0, 0)
                b = ((0, 0), (0, 0))
                c = (0, 0)
                inputs = [0, 0, 0, 0, 0]

            print(f"Proof A (after conversion): {a}")
            print(f"Proof B (after conversion): {b}")
            print(f"Proof C (after conversion): {c}")
            print(f"Inputs (after conversion): {inputs}")

    try:
        # tx = contract.functions.verifyTx([[0x1428d5fdc489be4b8692cd407a867100278f3d5c1579f458c3d03f89dd640512,
        #                                    0x1e368f825fb1d61ac66aaf025326cb2cdfcada82bd00f11a2ec631ba12be25b5], [
        #                                       [0x0d9be9a5b6e2b946bd18c81024f516c86c71c55021f801ca3770832a13483b4f,
        #                                        0x155bc3458a57232501798ad6b61b10fc9ac0e4d5dc3d626c5563580262c3e8d2],
        #                                       [0x144486387954a932966fc0719e845dce8a58ed426d7434fa4faa816bded27fc6,
        #                                        0x136e84b9edc202108695522de08ec7daa9038bc76c05a86c3e72b18a1eae1d50]],
        #                                   [0x0ebf676e85b86bab3a9978a35b6e224638fb0134fdf2039532e1b344c3ad75d3,
        #                                    0x2303807a23a73d6ed97b93acda1473d131c02c6454d4a803117d8a302565dec8]],
        #                                  [0x00000000000000000000000000000000000000000000000000000000035ecd34,
        #                                   0x000000000000000000000000000000000000000000000000000000000e2ac760,
        #                                   0x0000000000000000000000000000000000000000000000000000000000001f40,
        #                                   0x0000000000000000000000000000000000000000000000000000000003fffffb,
        #                                   0x0000000000000000000000000000000000000000000000000000000000d71bc5]).call()
        # 调用 verifyTx 函数
        tx = contract.functions.verifyTx((a, b, c), inputs).call()
        print("true")
    # 处理交易结果...
    except Exception as e:
        print(f"调用合约函数时发生错误: {e}")
        print("false")

    end = time.time() * 1000
    execution_time = end - t
    print(f"执行时间: {execution_time} 毫秒")


if __name__ == '__main__':
    filepath = "C:\check\zkproof.json"
    try:
        # 打开并读取 JSON 文件
        with open(filepath, 'r', encoding='utf-8') as file:
            data = json.load(file)
            verify(data)
            print(data)

        # 返回解析后的字典

    except FileNotFoundError:
        print(f"File not found: {filepath}")
    except json.JSONDecodeError as e:
        print(f"Error decoding JSON from file: {filepath} - {e}")


