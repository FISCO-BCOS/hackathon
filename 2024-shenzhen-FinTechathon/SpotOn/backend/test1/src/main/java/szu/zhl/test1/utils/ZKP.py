import subprocess
import json

def run_zokrates_command(command):
    try:
        subprocess.run(command, check=True)
        return True
    except subprocess.CalledProcessError as e:
        print(f"Command '{e.cmd}' returned non-zero exit status {e.returncode}.")
        print(f"Output: {e.output}")
        print(f"Error: {e.stderr}")
        return False

def read_inputs_from_file(file_path):
    with open(file_path, 'r') as file:
        data = json.load(file)
    return data['embDist'], data['randDist']

def ZkpProof(m, r):
    m = str(m)
    r = str(r)
    g = "56544564"
    h = "237684576"
    d = "800000"
    p = "67108859"
    inputs = [g, h, m, r, d, p]

    # 计算 witness 和生成 proof
    if not run_zokrates_command(["zokrates", "compute-witness", "-a"] + inputs):
        return False
    if not run_zokrates_command(["zokrates", "generate-proof"]):
        return False

    # 验证 (如果需要)
    # if not run_zokrates_command(["zokrates", "verify"]):
    #     return False

    return True

# 示例
file_path = "C:\\example\\temp-zk.json"  # 确保路径正确，没有多余的空格
m, r = read_inputs_from_file(file_path)
if ZkpProof(m, r):
    print("true")
else:
    print("false")