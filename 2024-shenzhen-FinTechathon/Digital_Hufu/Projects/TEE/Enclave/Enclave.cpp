/*
 * Copyright (C) 2011-2021 Intel Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of Intel Corporation nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

#include "Enclave.h"
#include "Enclave_t.h" /* print_string */
#include <stdarg.h>
#include <stdio.h> /* vsnprintf */
#include <string.h>
#include <string>
#include <vector>
#include <iomanip>
#include <sstream>

#include <sgx_tcrypto.h>
#include <sgx_trts.h>
#include <sgx_tseal.h>
#include "../common/shared_macros.h"

/* 
 * printf: 
 *   Invokes OCALL to display the enclave buffer to the terminal.
 */
int printf(const char* fmt, ...)
{
    char buf[BUFSIZ] = { '\0' };
    va_list ap;
    va_start(ap, fmt);
    vsnprintf(buf, BUFSIZ, fmt, ap);
    va_end(ap);
    ocall_print_string(buf);
    return (int)strnlen(buf, BUFSIZ - 1) + 1;
}

//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
// 辅助函数


// // 将二进制字节数组逆序（大小端转换）
// void reverseByByte(unsigned char* param, size_t size) {
//     for(size_t i = 0; i < size / 2; ++i) {
//         unsigned char temp = param[i];
//         param[i] = param[size-i-1];
//         param[size-i-1] = temp;
//     }
// }

// 打印二进制字节数组的十六进制字符串
void print_hex(const uint8_t* buffer, size_t size) {
    for (size_t i = 0; i < size; i++) {
        printf("%02x", buffer[i]);
        // if ((i + 1) % 16 == 0) printf("\n");
    }
    printf("\n");
}

// 将字节数组转换为十六进制字符串
void biToHexString(const unsigned char* biArray, size_t biArraySize, char* outputHex) {
    const char hex_digits[] = "0123456789abcdef";  // 十六进制字符映射表
    
    for (size_t i = 0; i < biArraySize; ++i) {
        unsigned char byte = biArray[i];
        // 通过位运算获取高4位和低4位的十六进制字符
        outputHex[i * 2] = hex_digits[byte >> 4];       // 高4位
        outputHex[i * 2 + 1] = hex_digits[byte & 0x0F];  // 低4位
    }
    outputHex[biArraySize * 2] = '\0';  // 结束符
}

// 将十六进制字符串转换回字节数组
bool hexStringToBi(const std::string& hex_str, unsigned char* array, size_t size) {
    if (hex_str.length() != size * 2) {
        printf("Error: imcompatible length: %d vs %d.\n", hex_str.length(), size * 2);
        return false;  // 十六进制字符串的长度必须是数组大小的两倍
    }

    for (size_t i = 0; i < size; ++i) {
        unsigned int byte = 0;
        // 解析每个字符对
        for (int j = 0; j < 2; ++j) {
            char ch = hex_str[i * 2 + j];
            byte <<= 4;
            if (ch >= '0' && ch <= '9') {
                byte |= (ch - '0');
            } else if (ch >= 'a' && ch <= 'f') {
                byte |= (ch - 'a' + 10);
            } else if (ch >= 'A' && ch <= 'F') {
                byte |= (ch - 'A' + 10);
            } else {
                return false; // 如果遇到无效字符
            }
        }
        array[i] = static_cast<unsigned char>(byte);
    }
    return true;
}

// 将数据seal之后写入外部文件
int seal_data_and_write_to_outer(const char* filename, unsigned char* buffer, size_t write_size, const int write_mode) {
    // 1. Seal 当前数据（数据进行加密处理）
    unsigned char sealed_data[MAX_SEAL_DATA_SIZE] = {0};
    uint32_t sealed_size = sgx_calc_sealed_data_size(0, write_size);  // 获取密封后的数据大小
    // printf("Writing line: ");
    // print_hex(buffer, write_size);
    // printf("Sealed size: %d\n", sealed_size);
    
    sgx_status_t status = sgx_seal_data(0, nullptr, write_size, buffer, sealed_size, (sgx_sealed_data_t*)sealed_data);
    if (status != SGX_SUCCESS) {
        printf("Error: fail to seal data.\n");
        return -1;
    }

    // 2. 将密封数据转换为十六进制字符串
    char hex_buffer[2 * MAX_SEAL_DATA_SIZE + 1] = {0};  // 存储十六进制字符串
    biToHexString(sealed_data, sealed_size, hex_buffer);  // 转换密封后的数据为十六进制字符串

    // 3. 添加换行符，确保写入的每次数据后都有换行符
    size_t len = strlen(hex_buffer);
    hex_buffer[len] = '\n';
    hex_buffer[len + 1] = '\0';  // 添加换行符并确保字符串结尾

    // 4. 调用外部函数（ocall）将密封后的十六进制字符串写入文件
    int ocall_write_ret = -1;
    ocall_write_file(filename, hex_buffer, strlen(hex_buffer), write_mode, &ocall_write_ret);
    if (ocall_write_ret != 0) {
        printf("Error: fail to write sealed hex string to file.\n");
        return -1;
    }

    return 0;
}

// 读入外部文件数据并unseal
int read_from_outer_and_unseal(const char* filename, unsigned char* decrypted_buffer, size_t max_decrypted_buffer_size) {
    // 1. 调用 ocall 读取文件（读取的是十六进制字符串）
    char hex_buffer[MAX_FILE_SIZE] = {0};  // 临时存储从文件读取的十六进制字符串
    int ocall_read_ret = -1;
    ocall_read_file(filename, hex_buffer, MAX_FILE_SIZE, &ocall_read_ret);
    if (ocall_read_ret != 0) {
        printf("Error: failed to read data from outer.\n");
        return -1;  // 如果读取失败，返回
    }

    // 2. 将十六进制字符串逐行转换并解密
    std::istringstream hex_stream(hex_buffer);  // 创建输入字符串流，按行读取
    std::string hex_line;  // 存储每一行的十六进制数据
    unsigned char temp_decrypted_buffer[MAX_UNSEAL_DATA_SIZE] = {0};  // 临时解密后的数据
    size_t total_decrypted_size = 0;  // 记录总的解密数据大小

    while (std::getline(hex_stream, hex_line)) {
        // printf("Sealed data(hex): %s\n", hex_line.c_str());
        // 每行的十六进制字符串转换为字节数组
        unsigned char sealed_data[MAX_SEAL_DATA_SIZE] = {0};  // 用于存放转换后的字节数组
        size_t sealed_data_size = hex_line.length() / 2;  // 每个字节由两个字符表示
        
        if (!hexStringToBi(hex_line, sealed_data, sealed_data_size)) {
            printf("Error: failed to convert hex string to byte array for line: %s\n", hex_line.c_str());
            return -1;
        }

        // 3. 解密（Unseal）操作
        size_t unsealed_data_size = MAX_UNSEAL_DATA_SIZE;
        sgx_status_t status = sgx_unseal_data((sgx_sealed_data_t*)sealed_data, nullptr, nullptr, (unsigned char*)temp_decrypted_buffer, (uint32_t*)&unsealed_data_size);
        if (status != SGX_SUCCESS) {
            printf("Error: failed to unseal data\n");
            return -1;
        }

        // printf("Unsealed size:%d, Unsealed line:", unsealed_data_size);
        // print_hex(temp_decrypted_buffer, unsealed_data_size);
        // 4. 检查解密后的数据大小是否超出最大缓冲区
        if (total_decrypted_size + unsealed_data_size > max_decrypted_buffer_size) {
            printf("Error: not enough buffer space for unsealed data.\n");
            return -1;
        }

        // 5. 将解密后的数据追加到输出缓冲区
        memcpy(decrypted_buffer + total_decrypted_size, temp_decrypted_buffer, unsealed_data_size);
        total_decrypted_size += unsealed_data_size;

        memset(temp_decrypted_buffer, 0, sizeof(temp_decrypted_buffer));
    }

    // 7. 确保解密数据以null结尾
    if (total_decrypted_size < max_decrypted_buffer_size) {
        decrypted_buffer[total_decrypted_size] = 0;
    } else {
        decrypted_buffer[max_decrypted_buffer_size - 1] = 0;
    }

    return 0;
}

// 验证两个读写函数的可行性
// void ecall_test_enclave_function(int* ret) {
//     char* msg = "Hello world\nHello SGX\nHello Dubley";
//     seal_data_and_write_to_outer("test.txt", (unsigned char*)("Hello world"), strlen("Hello world"), APPEND_MODE);
//     seal_data_and_write_to_outer("test.txt", (unsigned char*)("Hello SGX"), strlen("Hello SGX"), APPEND_MODE);
//     seal_data_and_write_to_outer("test.txt", (unsigned char*)("Hello Dubley"), strlen("Hello Dubley"), APPEND_MODE);
//     char unseal_msg[MAX_UNSEAL_DATA_SIZE] = {0};
//     read_from_outer_and_unseal("test.txt", (unsigned char*)unseal_msg, sizeof(unseal_msg));
//     *ret = strcmp(msg, unseal_msg);
// }

//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
// 交易异常监测部分


// 交易预警逻辑函数
int transaction_warning(int from_id, int to_id, double amount) {
    if(amount > MAX_SINGLE_AMOUNT) {
        return EXCESSIVE_SINGLE_AMOUNT;
    }
    return TRANSACTION_NO_EXCEPTION;
}


//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
// 密钥的生成、验证、存取和使用部分


// 向外部写入密钥参数（十六进制字符串）
int write_rsa_key_params_to_file(const std::string filename, unsigned char* e, unsigned char* p_n, unsigned char* p_d, 
                                  unsigned char* p_p, unsigned char* p_q, unsigned char* p_dmp1, unsigned char* p_dmq1, unsigned char* p_iqmp) {

    int ret = seal_data_and_write_to_outer(filename.c_str(), e, 4, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_n, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_d, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_p, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_q, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_dmp1, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_dmq1, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    ret = seal_data_and_write_to_outer(filename.c_str(), p_iqmp, RSA_KEY_SIZE, APPEND_MODE);
    if (ret != 0) {
        return -1;
    }

    return 0;
}

// 从外部读入密钥参数并转换为二进制数组
int read_rsa_key_params(const std::string filename, unsigned char* e, unsigned char* p_n, unsigned char* p_d, 
                        unsigned char* p_p, unsigned char* p_q, unsigned char* p_dmp1, unsigned char* p_dmq1, unsigned char* p_iqmp) {

    unsigned char buffer[MAX_FILE_SIZE]; // 用于存储读取的文件内容
    
    // 通过 OCALL 读取文件内容
    int read_rsa_params_ret = read_from_outer_and_unseal(filename.c_str(), buffer, MAX_FILE_SIZE);
    if (read_rsa_params_ret != 0) {
        printf("Failed to read wallet file: %s.\n", filename.c_str());
        return -1;
    }

    // 每个参数的大小
    unsigned char* params[] = {e, p_n, p_d, p_p, p_q, p_dmp1, p_dmq1, p_iqmp};
    size_t param_sizes[] = {4, RSA_KEY_SIZE, RSA_KEY_SIZE, RSA_KEY_SIZE, RSA_KEY_SIZE, RSA_KEY_SIZE, RSA_KEY_SIZE, RSA_KEY_SIZE};

    size_t offset = 0;
    int param_index = 0;

    // 按照参数的字节数读取数据
    while (param_index < 8 && offset < MAX_FILE_SIZE) {
        size_t line_length = param_sizes[param_index];  // 当前参数的字节大小

        // 检查剩余的数据是否足够读取
        if (offset + line_length > MAX_FILE_SIZE) {
            printf("Error: Not enough data in file to read parameter %d.\n", param_index);
            return -1;
        }

        // 将 buffer 中的当前参数数据复制到对应的参数
        memcpy(params[param_index], &buffer[offset], line_length);

        // 更新偏移量，跳过当前参数的字节数
        offset += line_length;

        // 处理下一个参数
        param_index++;
    }

    // 检查是否读取了足够的参数
    if (param_index != 8) {
        printf("Error: Incomplete data in file %s. Expected 8 parameters, but found %d.\n", filename.c_str(), param_index);
        return -1;
    }

    printf("RSA key params loaded successfully from file %s.\n", filename.c_str());
    return 0;
}

// 从文件中重塑密钥对
int restore_key_pair_from_file(const std::string filename, void** pub_key, void** priv_key) {
    void *new_pub_key = NULL;
    void *new_priv_key = NULL;
    unsigned char e[4] = {0x01, 0x00, 0x01, 0x00};
    unsigned char p_n[RSA_KEY_SIZE] = {0};
    unsigned char p_d[RSA_KEY_SIZE] = {0};
    unsigned char p_p[RSA_KEY_SIZE] = {0};
    unsigned char p_q[RSA_KEY_SIZE] = {0};
    unsigned char p_dmp1[RSA_KEY_SIZE] = {0};
    unsigned char p_dmq1[RSA_KEY_SIZE] = {0};
    unsigned char p_iqmp[RSA_KEY_SIZE] = {0};

    if(read_rsa_key_params(filename, e, p_n, p_d, p_p, p_q, p_dmp1, p_dmq1, p_iqmp) != 0){
        printf("Error: failed to read RSA key params from file.\n");
        return -1;
    }

    sgx_status_t ret_create_private_key = sgx_create_rsa_priv1_key(RSA_KEY_SIZE, sizeof(e), RSA_KEY_SIZE, p_n, e, p_d, &new_priv_key);
    if ( ret_create_private_key != SGX_SUCCESS) {
        printf("Error: private key generation failed (%d)\n", ret_create_private_key);
        return -1;
    }
    printf("Private key restored!\n");

    sgx_status_t ret_create_public_key = sgx_create_rsa_pub1_key(RSA_KEY_SIZE, sizeof(e), p_n, e, &new_pub_key);
    if ( ret_create_public_key != SGX_SUCCESS) {
        printf("Error: public key generation failed (%d)\n", ret_create_public_key);
        return -1;
    }
    printf("Public key restored!\n");

    *pub_key = new_pub_key;
    *priv_key = new_priv_key;
    return 0;
}

// 测试公私钥的配对性
int test_key_pair_validation(void *public_key, void *private_key) {
    // 原始明文
    const char* plaintext = "Hello, this is a secret message!";
    size_t plaintext_len = strlen(plaintext) + 1; // 包括结尾的 '\0'

    unsigned char encrypted_data[MAX_CIPHERTEXT_SIZE];
    size_t encrypted_size = MAX_CIPHERTEXT_SIZE;

    // 加密：使用公钥加密数据
    sgx_status_t status = sgx_rsa_pub_encrypt_sha256(
        public_key,               // 公钥
        encrypted_data,              // 加密后的密文
        &encrypted_size,            // 密文的大小
        (unsigned char*)plaintext,  // 明文数据
        plaintext_len               // 明文长度
    );

    if (status != SGX_SUCCESS) {
        printf("RSA encryption failed!\n");
        return -1;
    }

    unsigned char decrypted_data[MAX_PLAINTEXT_SIZE];
    size_t decrypted_size = MAX_PLAINTEXT_SIZE;

    // 解密：使用私钥解密数据
    status = sgx_rsa_priv_decrypt_sha256(
        private_key,                // 私钥
        decrypted_data,             // 解密后的明文数据
        &decrypted_size,            // 解密后的数据长度
        encrypted_data,             // 加密后的密文
        encrypted_size              // 密文长度
    );

    if (status != SGX_SUCCESS) {
        printf("RSA decryption failed!\n");
        return -1;
    }

    return strcmp((char*)decrypted_data, plaintext);
}

// 测试指定文件的密钥参数是否可重塑
int test_stored_rsa_params(const std::string filename, void *origin_pub_key, void* origin_priv_key) {
    void *restored_pub_key = NULL;
    void *restored_priv_key = NULL;
    restore_key_pair_from_file(filename, &restored_pub_key, &restored_priv_key);
    
    int pass_cnt = 0;
    pass_cnt += (test_key_pair_validation(origin_pub_key, origin_priv_key) == 0);
    pass_cnt += (test_key_pair_validation(restored_pub_key, restored_priv_key) == 0);
    pass_cnt += (test_key_pair_validation(restored_pub_key, origin_priv_key) == 0);
    pass_cnt += (test_key_pair_validation(origin_pub_key, restored_priv_key) == 0);

    printf("Restored key pair from file passed %d/4 test.\n", pass_cnt);
    if(pass_cnt != 4) {
        return -1;
    }
    return 0;
}

// 为指定用户生成密钥对
int generate_new_rsa_key_pair(int wallet_id, unsigned char* ret_pub_key, unsigned char* ret_priv_key) {
    void *g_public_key = NULL;
    void *g_private_key = NULL;
    unsigned char e[4] = {0x01, 0x00, 0x01, 0x00};
    unsigned char p_n[RSA_KEY_SIZE] = {0};
    unsigned char p_d[RSA_KEY_SIZE] = {0};
    unsigned char p_p[RSA_KEY_SIZE] = {0};
    unsigned char p_q[RSA_KEY_SIZE] = {0};
    unsigned char p_dmp1[RSA_KEY_SIZE] = {0};
    unsigned char p_dmq1[RSA_KEY_SIZE] = {0};
    unsigned char p_iqmp[RSA_KEY_SIZE] = {0};

    sgx_status_t ret_create_key_params = sgx_create_rsa_key_pair(RSA_KEY_SIZE, sizeof(e), p_n, p_d, e, p_p, p_q, p_dmp1, p_dmq1, p_iqmp);
    if (ret_create_key_params != SGX_SUCCESS) {
        printf("Error: key params generation failed (%d)\n", ret_create_key_params);
        return -1;
    }
    printf("Key params created!\n");

    sgx_status_t ret_create_private_key = sgx_create_rsa_priv1_key(RSA_KEY_SIZE, sizeof(e), RSA_KEY_SIZE, p_n, e, p_d, &g_private_key);
    if ( ret_create_private_key != SGX_SUCCESS) {
        printf("Error: private key generation failed (%d)\n", ret_create_private_key);
        return -1;
    }
    printf("Private key created!\n");

    sgx_status_t ret_create_public_key = sgx_create_rsa_pub1_key(RSA_KEY_SIZE, sizeof(e), p_n, e, &g_public_key);
    if ( ret_create_public_key != SGX_SUCCESS) {
        printf("Error: public key generation failed (%d)\n", ret_create_public_key);
        return -1;
    }
    printf("Public key created!\n");

    // 将密钥参数写入外部文件
    const std::string filename = "Wallets/wallet" + std::to_string(wallet_id) + ".txt";
    if(write_rsa_key_params_to_file(filename, e, p_n, p_d, p_p, p_q, p_dmp1, p_dmq1, p_iqmp) != 0) {
        printf("Error: failed to write RSA key params to file.\n");
        return -1;
    }
    printf("Successfully write rsa key params into file.\n");

    if (test_stored_rsa_params(filename, g_public_key, g_private_key) != 0) {
        printf("New rsa key pair failed the restoration test.\n");
        return -1;
    }

    memcpy(ret_pub_key, p_n, RSA_KEY_SIZE);
    memcpy(ret_priv_key, p_d, RSA_KEY_SIZE);

    return 0;
}

// 使用用户公钥加密交易
int encrypt_transaction_with_user_keys(int from_id, int to_id, double amount, char* encrypted_output, size_t encrypted_output_size) {
    // 获取 from_id 和 to_id 的公私钥
    void* pub_key = NULL;
    void* priv_key = NULL;

    const std::string filename = "Wallets/wallet" + std::to_string(from_id) + ".txt";
    if (restore_key_pair_from_file(filename, &pub_key, &priv_key) != 0) {
        printf("Error: Failed to get RSA keys for wallet_id %d\n", from_id);
        return -1;
    }

    printf("Data to encrypt: <from: %d, to: %d, amount: %.2lf>\n", from_id, to_id, amount);
    // 定义加密后的数据
    unsigned char encrypted_from_id[MAX_CIPHERTEXT_SIZE];
    unsigned char encrypted_to_id[MAX_CIPHERTEXT_SIZE];
    unsigned char encrypted_amount[MAX_CIPHERTEXT_SIZE];

    size_t encrypted_from_id_size = MAX_CIPHERTEXT_SIZE;
    size_t encrypted_to_id_size = MAX_CIPHERTEXT_SIZE;
    size_t encrypted_amount_size = MAX_CIPHERTEXT_SIZE;

    // 加密 from_id
    sgx_status_t status = sgx_rsa_pub_encrypt_sha256(
        pub_key,                // 公钥
        encrypted_from_id,      // 加密后的 from_id
        &encrypted_from_id_size,// 加密后的 from_id 的大小
        (unsigned char*)&from_id, // from_id 明文数据
        sizeof(from_id)         // from_id 明文长度
    );
    if (status != SGX_SUCCESS) {
        printf("Error: RSA encryption failed.\n");
        return -1;
    }

    // 加密 to_id
    status = sgx_rsa_pub_encrypt_sha256(
        pub_key,                // 公钥
        encrypted_to_id,        // 加密后的 to_id
        &encrypted_to_id_size,  // 加密后的 to_id 的大小
        (unsigned char*)&to_id, // to_id 明文数据
        sizeof(to_id)           // to_id 明文长度
    );
    if (status != SGX_SUCCESS) {
        printf("Error: RSA encryption failed for to_id\n");
        return -1;
    }

    // 加密 amount
    status = sgx_rsa_pub_encrypt_sha256(
        pub_key,                // 公钥
        encrypted_amount,       // 加密后的 amount
        &encrypted_amount_size, // 加密后的 amount 的大小
        (unsigned char*)&amount, // amount 明文数据
        sizeof(amount)           // amount 明文长度
    );
    if (status != SGX_SUCCESS) {
        printf("Error: RSA encryption failed for amount\n");
        return -1;
    }

    // 将加密后的数据转换为 16 进制字符串
    char encrypted_from_id_hex[MAX_CIPHERTEXT_SIZE * 2 + 1];
    char encrypted_to_id_hex[MAX_CIPHERTEXT_SIZE * 2 + 1];
    char encrypted_amount_hex[MAX_CIPHERTEXT_SIZE * 2 + 1];

    biToHexString(encrypted_from_id, encrypted_from_id_size, encrypted_from_id_hex);
    biToHexString(encrypted_to_id, encrypted_to_id_size, encrypted_to_id_hex);
    biToHexString(encrypted_amount, encrypted_amount_size, encrypted_amount_hex);

    // 将格式化后的字符串存储到 encrypted_output
    snprintf(encrypted_output, encrypted_output_size,
             "from_id:%s,to_id:%s,amount:%s\n",
             encrypted_from_id_hex,
             encrypted_to_id_hex,
             encrypted_amount_hex);
    // printf("Length of encrypted string: %d\n", strlen(encrypted_output));
    return 0;
}

// 使用Enclave私钥解密交易
int decrypt_transaction_with_enclave_keys(const char* encrypted_from, const char* encrypted_to, const char* encrypted_amount, 
                        size_t encrypted_size, int* from_id, int* to_id, double* amount) {
    unsigned char from_bytes[RSA_KEY_SIZE] = {0};
    unsigned char to_bytes[RSA_KEY_SIZE] = {0};
    unsigned char amount_bytes[RSA_KEY_SIZE] = {0};
    
    if (!hexStringToBi(encrypted_from, from_bytes, RSA_KEY_SIZE) || 
        !hexStringToBi(encrypted_to, to_bytes, RSA_KEY_SIZE) || 
        !hexStringToBi(encrypted_amount, amount_bytes, RSA_KEY_SIZE)) {
        printf("Error: fail to convert the byte arrays into hex string.\n");
        return -1;
    }

    // 恢复公钥和私钥
    void* pub_key = nullptr;
    void* priv_key = nullptr;
    std::string filename = "Enclave/enclave_keys.txt";
    if (restore_key_pair_from_file(filename, &pub_key, &priv_key) != 0) {
        printf("Error: fail to restore key pairs of Enclave.\n");
        return -1;
    }

    // 解密 "from"
    unsigned char decrypted_from[MAX_PLAINTEXT_SIZE] = {0};
    size_t decrypted_from_size = MAX_PLAINTEXT_SIZE;
    if (sgx_rsa_priv_decrypt_sha256(priv_key, decrypted_from, &decrypted_from_size, from_bytes, RSA_KEY_SIZE) != SGX_SUCCESS) {
        printf("Error: fail to decrypt \"from\".\n");
        return -1;
    }

    // 解密 "to"
    unsigned char decrypted_to[MAX_PLAINTEXT_SIZE] = {0};
    size_t decrypted_to_size = MAX_PLAINTEXT_SIZE;
    if (sgx_rsa_priv_decrypt_sha256(priv_key, decrypted_to, &decrypted_to_size, to_bytes, RSA_KEY_SIZE) != SGX_SUCCESS) {
        printf("Error: fail to decrypt \"to\".\n");
        return -1;
    }

    // 解密 "amount"
    unsigned char decrypted_amount[MAX_PLAINTEXT_SIZE] = {0};
    size_t decrypted_amount_size = MAX_PLAINTEXT_SIZE;
    if (sgx_rsa_priv_decrypt_sha256(priv_key, decrypted_amount, &decrypted_amount_size, amount_bytes, RSA_KEY_SIZE) != SGX_SUCCESS) {
        printf("Error: fail to decrypt \"amount\".\n");
        return -1;
    }

    *from_id = *(int*)decrypted_from;
    *to_id = *(int*)decrypted_to;
    *amount = *(double*)decrypted_amount;

    return 0;; // 解密成功
}


//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
// 交易混洗部分


// 用于生成随机浮点数的函数 [0.0, 1.0)
double generate_random_number() {
    uint64_t random_int;  // 64位整数，用于生成随机浮点数

    // 调用 sgx_read_rand 生成 8 字节的随机数
    sgx_status_t status = sgx_read_rand((unsigned char*)&random_int, sizeof(random_int));

    if (status == SGX_SUCCESS) {
        // 归一化处理，将随机整数转换为 [0.0, 1.0) 范围的 double 值
        double random_value = (double)random_int / (double)UINT64_MAX;
        return random_value;
    } else {
        printf("Fail to generate random number. Error code: %d\n", status);
        return 0.0;  // 返回 0.0 表示生成失败
    }
}

// 随机选择代理钱包
void get_random_wallets(int num_wallets, std::vector<int>& selected_wallets) {
    selected_wallets.clear();  // 清空已选择的钱包列表
    bool selected[POOL_SIZE + 1] = { false };  // 标记钱包是否已被选择
    selected[0] = true;

    while (selected_wallets.size() < num_wallets) {
        int idx = (int)(generate_random_number() * POOL_SIZE); // 生成随机钱包索引

        // 如果该钱包未被选择，标记为已选择并加入到 selected_wallets
        if (!selected[idx]) {
            selected_wallets.push_back(idx);
            selected[idx] = true;  // 标记为已选择
        }
    }
}

// 随机拆分交易金额
void split_transaction_amount(double total_amount, int num_splits, std::vector<double>& splits) {
    double remaining_amount = total_amount;

    // 随机生成每个子交易的金额，并保留两位小数
    for (int i = 0; i < num_splits - 1; ++i) {
        // 随机金额在剩余金额的 0 到 remaining_amount / 2 之间
        double max_amount = remaining_amount / 2;
        double split_amount = generate_random_number() * max_amount;

        // 保留两位小数
        split_amount = (int)(split_amount * 100.0) / 100.0;
        splits.push_back(split_amount);
        remaining_amount -= split_amount;
    }

    // 最后一个金额是剩余金额，保留两位小数
    splits.push_back((int)(remaining_amount * 100.0) / 100.0);
}

// 实现交易混洗逻辑的函数
void shuffle_transaction(int from_id, int to_id, double amount, char* shuffled_output, size_t shuffled_output_size) {
    // 随机选择 3 到 6 个代理钱包
    int num_wallets = (int)(generate_random_number() * 4.0) + 3; // 随机选择 3 到 6 个钱包
    std::vector<int> selected_wallets;
    get_random_wallets(num_wallets, selected_wallets);

    // 将交易金额按比例拆分
    std::vector<double> split_amounts;
    split_transaction_amount(amount, num_wallets, split_amounts);

    // 输出拆分后的交易信息
    printf("Shuffling transaction: <");
    printf("From: %d To: %d Amount: %.2f>\n", from_id, to_id, amount);
    for (int i = 0; i < num_wallets; ++i) {
        printf("Address: %d Amount: %.2f\n", selected_wallets[i], split_amounts[i]);
    }
    
    size_t offset = 0;
    size_t vec_size = selected_wallets.size();
    for (size_t i = 0; i < vec_size; ++i) {
        // 获取当前钱包地址
        int proxy_id = selected_wallets[i];
        char proxy_id_str[10];
        int len = snprintf(proxy_id_str, sizeof(proxy_id_str), "%d", proxy_id); // 获取钱包地址字符串长度

        // 如果还有空间，将钱包地址复制到临时缓冲区
        if (offset + len < shuffled_output_size) {
            memcpy(shuffled_output + offset, proxy_id_str, len);
            offset += len;
        }

        // 添加冒号分隔符
        if (offset + 1 < shuffled_output_size) {
            shuffled_output[offset] = ':';
            offset++;
        }

        // 获取拆分的金额
        double temp_amount = split_amounts[i];
        char amount_str[20] = {0};
        len = snprintf(amount_str, sizeof(amount_str), "%.2f", temp_amount);

        // 将金额字符串拷贝到缓冲区
        if (offset + len < shuffled_output_size) {
            memcpy(shuffled_output + offset, amount_str, len);
            offset += len;
        }

        // 如果不是最后一个钱包地址，添加逗号
        if (i != vec_size - 1) {
            if (offset + 1 < shuffled_output_size) {
                shuffled_output[offset] = ',';
                offset++;
            }
        }
    }
    // 确保字符数组以 null 结尾
    shuffled_output[offset] = '\0';
}


//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
// ecall接口

// ecall测试函数
void ecall_add(int a, int b, int *result) {
    *result = a + b;
}

// 创建一个新的钱包，并返回公钥和私钥
void ecall_create_wallet(unsigned char* ret_pub_key, unsigned char* ret_priv_key, size_t pub_key_len, int wallet_id, int* ret){
    // 构造文件名
    std::string filename = "Wallets/wallet" + std::to_string(wallet_id) + ".txt";

    // 判断文件是否存在
    int file_exists = 0;
    ocall_file_exists(filename.c_str(), &file_exists);

    if (file_exists == 1) {
        *ret = -1;  // 钱包文件已经存在
        return;
    }
    
	printf("Generating key pair for wallet_id %d\n", wallet_id);
    if(generate_new_rsa_key_pair(wallet_id, ret_pub_key, ret_priv_key) != 0){
		*ret = -2;  //生成密钥对失败
        return;
	}

    *ret = 0;
}

// 混洗交易（并将交易原文存至本地）
void ecall_deal_transaction(int from_id, int to_id, double amount, char* shuffled_output, size_t shuffled_output_size, 
                            char* encrypted_output, size_t encrypted_output_size, int* warning_sign, int* ret) {
    *warning_sign = transaction_warning(from_id, to_id, amount);
    
    shuffle_transaction(from_id, to_id, amount, shuffled_output, shuffled_output_size);
    printf("Transaction shuffled successfully.\n");
    
    // if(encrypt_transaction_with_user_keys(from_id, to_id, amount, encrypted_output, encrypted_output_size) != 0) {
    //     printf("Failed to encrypt transaction.\n");
    //     *ret = -1;
    //     return;
    // }
    // printf("Transaction data encrypted successfully.\n");
    ret = 0;
}

// 用Enclave公钥解密交易数据，并用用户公钥加密后存至本地
void ecall_decrypt_transaction_data(const char* encrypted_from, const char* encrypted_to, const char* encrypted_amount, 
                                    size_t encrypted_size, int* from_id, int* to_id, double* amount, int* ret) {
    if(decrypt_transaction_with_enclave_keys(encrypted_from, encrypted_to, encrypted_amount, encrypted_size, from_id, to_id, amount) != 0) {
        printf("Fail to decrypt trasaction data.\n");
        *ret = -1;
        return;
    }
    printf("from: %d\nto: %d\namount%lf\nSuccessfully decrypt transaction.\n", *from_id, *to_id, *amount);

    char encrypted_output[MAX_TRANSACTION_ENCRYPTED_SIZE];
    size_t encrypted_output_size = MAX_TRANSACTION_ENCRYPTED_SIZE;
    encrypt_transaction_with_user_keys(*from_id, *to_id, *amount, encrypted_output, encrypted_output_size);
    const std::string filename = "Transactions/wallet" + std::to_string(*from_id) + ".txt";
    int write_data_out_ret = -1;
    ocall_write_file(filename.c_str(), encrypted_output, encrypted_output_size, APPEND_MODE, &write_data_out_ret);
    if(write_data_out_ret != 0) {
        printf("Error: fail to write encrypted transaction into file.\n");
        *ret = -1;
        return;
    }
    printf("Successfully write encrypted transaction into file.\n");

    *ret = 0;
}

// 用用户公钥加密交易数据，并将交易密文存至本地
void ecall_encrypt_transaction(int from_id, int to_id, double amount, int* ret) {
    char encrypted_output[MAX_TRANSACTION_ENCRYPTED_SIZE];
    size_t encrypted_output_size = MAX_TRANSACTION_ENCRYPTED_SIZE;
    
    if(encrypt_transaction_with_user_keys(from_id, to_id, amount, encrypted_output, encrypted_output_size) != 0) {
        printf("Error: fail to encrypt trasaction.\n");
        *ret = -1;
        return;
    }

    printf("Succesfully encrypt transaction.\n");

    const std::string filename = "Transactions/wallet" + std::to_string(from_id) + ".txt";
    int write_data_out_ret = -1;
    ocall_write_file(filename.c_str(), encrypted_output, encrypted_output_size, APPEND_MODE, &write_data_out_ret);
    if(write_data_out_ret != 0) {
        printf("Error: fail to write encrypted transaction into file.\n");
        *ret = -1;
        return;
    }
    printf("Successfully write encrypted transaction into file.\n");

    *ret = 0;
}

// 交易预警
void ecall_transaction_warning(int from_id, int to_id, double amount, int* warning_sign, int* ret) {
    *warning_sign = transaction_warning(from_id, to_id, amount);
    *ret = 0;
}


//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
