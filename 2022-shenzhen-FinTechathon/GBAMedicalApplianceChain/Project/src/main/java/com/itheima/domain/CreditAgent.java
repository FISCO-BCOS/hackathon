package com.itheima.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CreditAgent {
    private String addressTo;
    private BigInteger value;
    private byte[] data;

}
