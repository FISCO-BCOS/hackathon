#ifndef SHARED_MACROS_H
#define SHARED_MACROS_H


// 内存分配宏
#define RSA_KEY_SIZE 384            // RSA密钥的长度（固定384字节）
#define MAX_FILE_SIZE 100000        // 单次读取文件的最大容量

#define MAX_TRANSACTION_SIZE 500                                        // 一条普通交易信息的最大长度（原字符串）
#define MAX_TRANSACTION_ENCRYPTED_SIZE (RSA_KEY_SIZE * 6 + 100)         // 一条加密后的交易信息的最大长度（十六进制密文的字符串）
#define MAX_PLAINTEXT_SIZE RSA_KEY_SIZE         // 最长的单块明文长度（二进制）
#define MAX_CIPHERTEXT_SIZE RSA_KEY_SIZE        // 最长的密文长度（二进制）

#define MAX_SEAL_DATA_SIZE 1200     // 密封后数据的最大长度
#define MAX_UNSEAL_DATA_SIZE 600    // 解封后数据的最大长度（可以用来密封的最长字节）

// 读写模式相关宏
#define OVER_WRITE_MODE 1   // 覆写模式
#define APPEND_MODE 0       // 追加模式

// 交易混洗相关宏
#define POOL_SIZE 10        // 钱包池个数

// 交易预警宏
#define TRANSACTION_NO_EXCEPTION 0      // 无异常
#define EXCESSIVE_SINGLE_AMOUNT 1       // 单笔交易金额过大
#define EXCESSIVE_DAILY_AMOUNT 2        // 日交易金额过大
#define EXCESSIVE_FREQUENCY 3           // 交易过于频繁
    
#define MAX_SINGLE_AMOUNT 100000        // 单笔交易最大金额
#define MAX_DAILY_AMOUNT 500000         // 日交易最大金额

#endif // SHARED_MACROS_H
