def replace_string_in_file_directly(file_path, old_string, new_string):
    try:
        # 打开文件进行读取
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()

        # 替换字符串
        content = content.replace(old_string, new_string)

        # 打开同一个文件进行写入，覆盖原有内容
        with open(file_path, 'w', encoding='utf-8') as file:
            file.write(content)

        print(f"字符串替换完成，原始文件 {file_path} 已被更新。")
    except FileNotFoundError:
        print(f"错误：文件 {file_path} 未找到。")
    except Exception as e:
        print(f"发生错误：{e}")


# 使用示例
file_path = './blockchain.html'  # 替换为你的.go文件路径
old_string = '127.0.0.1:1234'  # 替换为你要查找的旧字符串
new_string = '61.48.133.1:65002'  # 替换为你要替换的新字符串

replace_string_in_file_directly(file_path, old_string, new_string)