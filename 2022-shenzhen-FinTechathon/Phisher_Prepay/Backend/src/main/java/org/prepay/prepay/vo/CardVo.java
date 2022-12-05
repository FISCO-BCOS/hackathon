package org.prepay.prepay.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardVo {
    private String cardId;
    private String userName;
    private String shopName;
    private BigInteger balance;
    private String text;
}
