package szu.blockchain.check.utils;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 同态加密运算类
 * 实现了基于模运算的加法同态、幂运算和验证功能
 */
public class Homomorphic {
    // 模数 p，用于所有模运算
    private final BigInteger p = new BigInteger("67108859"); // 定义常量p，表示大素数

    /**
     * 同态加法运算
     * 通过将两个承诺值相乘后取模实现加法同态
     *
     * @param ComA 第一个承诺值
     * @param ComB 第二个承诺值
     * @return 两个承诺的同态加法结果 (ComA * ComB) mod p
     */
    public BigInteger Add(BigInteger ComA, BigInteger ComB) {
        return ComA.multiply(ComB).mod(p);
    }

    /**
     * 同态幂运算
     * 使用查找表优化的快速幂算法，计算承诺值的n次幂
     *
     * @param Com 基础承诺值
     * @param n   指数
     * @return Com^n mod p
     */
    public BigInteger Pow(BigInteger Com, BigInteger n) {
        // 初始化结果为1
        BigInteger res = BigInteger.ONE;
        // 用于存储商的临时变量
        BigInteger quot = n;
        // 用于存储余数的临时变量
        BigInteger rem;

        // 创建幂次方查找表，用于优化计算
        ArrayList<BigInteger> table = new ArrayList<>();
        // 将基础值添加到表中
        table.add(Com);

        // 预计算并填充查找表
        // table[i] 存储 Com^(2^i) mod p
        for (int i = 1; i < 64; i++) {
            // 计算下一个幂次方：将前一个值平方并取模
            table.add(table.get(i - 1).multiply(table.get(i - 1)).mod(p));
        }

        // 使用二进制分解进行幂运算
        for (int j = 0; j < 64; j++) {
            // 获取当前位的值（0或1）
            rem = quot.mod(BigInteger.TWO);
            // 商除以2，准备处理下一位
            quot = quot.divide(BigInteger.TWO);

            // 如果当前位为1，则将对应的幂次方乘入结果
            if (rem.equals(BigInteger.ONE)) {
                res = res.multiply(table.get(j)).mod(p);
            }
        }

        return res;
    }

    /**
     * 同态验证运算
     * 验证三个承诺值是否满足特定的关系：ComA = ComB * ComC mod p
     *
     * @param ComA 待验证的承诺值
     * @param ComB 第一个验证因子
     * @param ComC 第二个验证因子
     * @return 如果验证通过返回1，否则返回0
     */
    public int Sub(BigInteger ComA, BigInteger ComB, BigInteger ComC) {
        // 计算 ComB * ComC mod p 并与 ComA 比较
        BigInteger result = ComB.multiply(ComC).mod(p);
        return result.equals(ComA) ? 1 : 0;
    }
}