package org.fisco.bcos.upload.contract;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import it.unisa.dia.gas.jpbc.Element;

class ReceiverData {
    // Add revoke.

    public int attribute_numbers;

    public String user_id;

    public boolean isRevoked = false;

    // Decrypt

    public int[] sigma;
    public Element[] secret_key_sigma;

    // Match

    public Element alpha;
    public Element t_0;
    public Element[] t_1_i;

    // Match the attributes.
    public Element[] C_2_i_match;
    public Element[] C_5_i_match;
    public Element[] C_1_i_match;
    public Element[] C_4_i_match;

    public void enterAttributes() {
        System.out.println("Please enter your attributes, they should by separated by comma");
        Scanner sc = new Scanner(System.in);
        String inputContents = sc.nextLine();
        String[] contents = inputContents.split(",");

        for (String content : contents) {
            this.sigma[Integer.parseInt(content) - 1] = Integer.parseInt(content);
        }
    }

    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public String parseBigInt2Str(BigInteger message_big_int) {
        String hexString = message_big_int.toString(16);
        return hexStringToString(hexString);
    }

    public ReceiverData() {
        this.attribute_numbers = 20;

        this.sigma = new int[attribute_numbers];
        this.secret_key_sigma = new Element[attribute_numbers];

        this.t_1_i = new Element[attribute_numbers];

        this.C_2_i_match = new Element[attribute_numbers];
        this.C_5_i_match = new Element[attribute_numbers];
        this.C_1_i_match = new Element[attribute_numbers];
        this.C_4_i_match = new Element[attribute_numbers];

    }

    public ReceiverData(int attribute_numbers) {
        this.attribute_numbers = attribute_numbers;

        this.attribute_numbers = 20;

        this.sigma = new int[attribute_numbers];
        this.secret_key_sigma = new Element[attribute_numbers];

        this.t_1_i = new Element[attribute_numbers];

        this.C_2_i_match = new Element[attribute_numbers];
        this.C_5_i_match = new Element[attribute_numbers];
        this.C_1_i_match = new Element[attribute_numbers];
        this.C_4_i_match = new Element[attribute_numbers];

    }

}