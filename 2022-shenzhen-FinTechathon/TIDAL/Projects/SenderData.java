package org.fisco.bcos.upload.contract;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;

import it.unisa.dia.gas.jpbc.Element;

class SenderData {

    private int element_size;
    // Create User
    public String user_id;

    // Ciphertext.

    // This part is for read_only.
    public Element C_m;
    public Element[] C_1_i;
    public Element[] C_2_i;
    public Element C_3;
    // This part is for edit.
    public Element C_x;
    public Element[] C_4_i;
    public Element[] C_5_i;
    public Element C_6;
    public Element C_7;

    public Element back_C_7;

    // Message
    public BigInteger message;

    // Parameters.
    public int chameleon_key;
    public Element o_1, o_2, o_3, o_4, o_h;

    // Newton parameters.

    public Element[] K_read;
    public Element[] N_read;
    public Element[] K_edit;
    public Element[] N_edit;

    public Element secret_const_read, secret_const_edit;

    // Policies.
    public int[] policies_read;
    public int[] policies_edit;

    // Hash result.
    public Element hash_result;
    public Element back_hash_result;

    // Hash verification parameters.
    public BigInteger message_big_int, chameleon_big_int;

    public SenderData() {
        this.element_size = 20;

        this.C_1_i = new Element[this.element_size];
        this.C_2_i = new Element[this.element_size];

        this.C_4_i = new Element[this.element_size];
        this.C_5_i = new Element[this.element_size];

        this.K_read = new Element[element_size];
        this.N_read = new Element[element_size];
        this.K_edit = new Element[element_size];
        this.N_edit = new Element[element_size];

        this.policies_read = new int[element_size];
        this.policies_edit = new int[element_size];
    }

    public SenderData(int element_size) {
        this.element_size = element_size;

        this.C_1_i = new Element[this.element_size];
        this.C_2_i = new Element[this.element_size];

        this.C_4_i = new Element[this.element_size];
        this.C_5_i = new Element[this.element_size];

        this.K_read = new Element[element_size];
        this.N_read = new Element[element_size];
        this.K_edit = new Element[element_size];
        this.N_edit = new Element[element_size];

        this.policies_read = new int[element_size];
        this.policies_edit = new int[element_size];
    }

    public void enterReadAttributes() {
        System.out.println("Please enter your read attributes, use comma to separate them.");
        Scanner sc = new Scanner(System.in);
        String inputContent = sc.nextLine();
        String[] contents = inputContent.split(",");

        for (String content : contents) {
            this.policies_read[Integer.parseInt(content) - 1] = Integer.parseInt(content);
        }
    }

    public void enterEditAttributes() {
        System.out.println("Please enter your edit attributes, " +
                "note it must contains all attributes in read policies, " +
                "these attributes are also should be separated by comma.");
        Scanner sc = new Scanner(System.in);
        String inputContent = sc.nextLine();
        String[] contents = inputContent.split(",");

        for (String content : contents) {
            this.policies_edit[Integer.parseInt(content) - 1] = Integer.parseInt(content);
        }
    }

    public static String convertString2HexString(@NotNull String message) {
        byte[] getBytesFromString = message.getBytes(StandardCharsets.UTF_8);
        BigInteger temp = new BigInteger(1, getBytesFromString);

        String convertedResult;
        convertedResult = String.format("%x", temp);
        return convertedResult;
    }

    public void parseStr2BigInt(String message) {
        this.message = new BigInteger(convertString2HexString(message), 16);
    }
}