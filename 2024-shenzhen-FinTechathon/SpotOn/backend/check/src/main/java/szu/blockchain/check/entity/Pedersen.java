package szu.blockchain.check.entity; // 定义包名

import java.math.BigInteger; // 导入BigInteger类，用于大整数运算
import java.util.ArrayList; // 导入ArrayList类，用于动态数组
import java.util.List; // 导入List接口

public class Pedersen { // 定义Pedersen类
    private BigInteger g; // 定义私有成员变量g，表示生成元g
    private BigInteger h; // 定义私有成员变量h，表示生成元h
    private final BigInteger p = new BigInteger("67108859"); // 定义常量p，表示大素数
    private List<BigInteger> gTable = new ArrayList<>(); // 定义gTable列表，用于存储g的幂次
    private List<BigInteger> hTable = new ArrayList<>(); // 定义hTable列表，用于存储h的幂次

    public Pedersen(BigInteger g, BigInteger h) { // 构造函数，初始化g和h
        this.g = g.mod(p); // 将g取模p，确保g在有限域内
        this.h = h.mod(p); // 将h取模p，确保h在有限域内
        gTable.add(this.g); // 将g添加到gTable中
        hTable.add(this.h); // 将h添加到hTable中
        for (int i = 1; i < 64; i++) { // 循环计算g和h的幂次
            gTable.add(gTable.get(i - 1).multiply(gTable.get(i - 1)).mod(p)); // 计算g的平方并取模p，添加到gTable
            hTable.add(hTable.get(i - 1).multiply(hTable.get(i - 1)).mod(p)); // 计算h的平方并取模p，添加到hTable
        }
    }

    public BigInteger commitment(BigInteger v, BigInteger r) { // 定义commitment方法，计算承诺值
        BigInteger vQuot = v; // 初始化vQuot为v，用于计算v的二进制位
        BigInteger rQuot = r; // 初始化rQuot为r，用于计算r的二进制位
        BigInteger vRem; // 定义vRem，用于存储v的当前二进制位
        BigInteger rRem; // 定义rRem，用于存储r的当前二进制位
        BigInteger G = BigInteger.ONE; // 初始化G为1，用于存储g的幂次乘积
        BigInteger H = BigInteger.ONE; // 初始化H为1，用于存储h的幂次乘积
        for (int i = 0; i < 64; i++) { // 循环64次，处理v和r的每一位
            vRem = vQuot.mod(BigInteger.TWO); // 计算v的当前位（0或1）
            vQuot = vQuot.divide(BigInteger.TWO); // 将v右移一位
            rRem = rQuot.mod(BigInteger.TWO); // 计算r的当前位（0或1）
            rQuot = rQuot.divide(BigInteger.TWO); // 将r右移一位
            if (vRem.equals(BigInteger.ONE)) { // 如果v的当前位为1
                G = G.multiply(gTable.get(i)).mod(p); // 将当前g的幂次乘到G上并取模p
            }
            if (rRem.equals(BigInteger.ONE)) { // 如果r的当前位为1
                H = H.multiply(hTable.get(i)).mod(p); // 将当前h的幂次乘到H上并取模p
            }
        }
        return G.multiply(H).mod(p); // 返回G和H的乘积并取模p，作为承诺值
    }
}