package org.fisco.bcos.upload.contract;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.crypto.hash.Keccak256;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.*;

class APITest {

    // 获取配置文件路径
    public final String configFile = APITest.class.getClassLoader().getResource("config-example.toml").getPath();

    /**
     * Deploy the contract on the blockchain.
     * 
     * @return The contract itself.
     * @throws ConfigException
     */
    public HashUpload contractDeploy() throws ConfigException {
        BcosSDK sdk = BcosSDK.build(configFile);
        Client client = sdk.getClient(Integer.valueOf(1));

        CryptoKeyPair cryptoPair = client.getCryptoSuite().getCryptoKeyPair();
        HashUpload hashUpload = null;
        try {
            hashUpload = HashUpload.deploy(client, cryptoPair);
        } catch (ContractException e) {
            e.printStackTrace();
        }

        return hashUpload;
    }

    /**
     * Get the hash value by hash id, from the blockchain.
     * 
     * @param contract The contract on the blockchain.
     * @param hash_id  The hash_id.
     * @return The corresponding hash value.
     */
    public String getHashValue(@NotNull HashUpload contract, String hash_id) {
        String hash_value = null;
        try {
            hash_value = contract.selectHash(hash_id).getValue2();
        } catch (ContractException e) {
            e.printStackTrace();
        }
        return hash_value;
    }

    /**
     * Upload the hash value on the block with the corresponding id.
     * 
     * @param contract   The contract on the blockchain.
     * @param hash_id    The id for the uploaded hash.
     * @param hash_value The value for the uploaded hash.
     */

    public void UploadHash(@NotNull HashUpload contract, String hash_id, String hash_value) {
        TransactionReceipt receipt;
        receipt = contract.uploadHash(hash_id, hash_value);
        out.println("Receipt: " + receipt);
    }

    /**
     * Modify the value of the hash according to the id.
     * 
     * @param contract   The contract on the blockchain.
     * @param hash_id    The id for the hash waited for modification.
     * @param hash_value The value for the hash waited for modification.
     */

    public void ModifyHash(@NotNull HashUpload contract, String hash_id, String hash_value) {
        TransactionReceipt receipt;
        receipt = contract.modifyHash(hash_id, hash_value);
        out.println("Receipt: " + receipt);
    }

    // public static void main(String args[]) throws ConfigException {
    // APITest newTest = new APITest();
    //
    // HashUpload newContract = newTest.contractDeploy();
    // String hash_id_test_1 = "hash_102";
    // String hash_value_test_1 =
    // "0F622AC0D0500AE8634505003AA8122C01F74C99352ECDBD0C988D2FEE7093D8";
    // String hash_value_test_2 =
    // "532EAABD9574880DBF76B9B8CC00832C20A6EC113D682299550D7A6E0F345E25";
    //
    // out.println("Uploading hash.");
    // newTest.UploadHash(newContract, hash_id_test_1, hash_value_test_1);
    // out.println("Querying hash.");
    // out.println("The queried hash is: " + newTest.getHashValue(newContract,
    // hash_id_test_1));
    // out.println("Modifying hash.");
    // newTest.ModifyHash(newContract, hash_id_test_1, hash_value_test_2);
    // out.println("Querying new hash.");
    // out.println("The new queried hash is: " + newTest.getHashValue(newContract,
    // hash_id_test_1));
    // }

}

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

class SystemData {
    // Size of some data within array format.
    public int number_of_policy_read;
    public int number_of_policy_edit;
    public int number_of_attributes;

    // System parameters.
    public Element g, p;
    public Field G;
    public Field GT;
    public Field ZP;
    public Pairing e;

    // Attributes.
    public List<String> attributesList;

    // The secret key and the corresponding part in public key parameters.
    public Element[] r_i;
    public Element[] R_i;

    // Other parameters.
    public Element gamma;

    public void initAttributes() {
        attributesList = new ArrayList<>();
        attributesList.add("China");
        attributesList.add("America");
        attributesList.add("BIT");
        attributesList.add("MIT");
        attributesList.add("CST");
        attributesList.add("CS");
        attributesList.add("Student");
        attributesList.add("Professor");
        attributesList.add("Man");
        attributesList.add("Woman");
    }

    public SystemData() {
        this.number_of_policy_read = 10;
        this.number_of_policy_edit = 10;
        this.number_of_attributes = 20;

        this.r_i = new Element[number_of_attributes];
        this.R_i = new Element[number_of_attributes];
    }

    public SystemData(int number_of_policy_read, int number_of_policy_edit, int number_of_attributes) {
        this.number_of_policy_read = number_of_policy_read;
        this.number_of_policy_edit = number_of_policy_edit;
        this.number_of_attributes = number_of_attributes;

        this.r_i = new Element[number_of_attributes];
        this.R_i = new Element[number_of_attributes];
    }
}

class SystemUI {

    public void startMenu() {
        // Display choices.
        System.out.println("====================Main  Menu==================");
        System.out.println("===============1.Sender=========================");
        System.out.println("===============2.Receiver=======================");
        System.out.println("===============3.Exit===========================");
    }

    public void senderMenu() {
        // Display choices for users.
        System.out.println("====================User  Menu==================");
        System.out.println("===============1.Add new sender=================");
        System.out.println("===============2.Encrypt message================");
        System.out.println("===============3.Back to main menu==============");
    }

    public void receiverMenu() {
        // Display choices for receivers.
        System.out.println("====================Receiver  Menu==============");
        System.out.println("===============1.Add new receiver===============");
        System.out.println("===============2.Revoke receiver================");
        System.out.println("===============3.Read and write message=========");
        System.out.println("===============4.Back to main menu==============");
    }

}

/**
 * @ClassName : CryptoProc
 * @Description : The class for crypto process in the project.
 * @Author : Gadget
 * @Date: 2022/11/8 16:23
 */

public class CryptoProc {
    // Number of participants.

    public SystemData system = new SystemData();
    public List<SenderData> senders = new ArrayList<>();
    public List<ReceiverData> receivers = new ArrayList<>();

    /**
     * @return void
     * @throws
     * @Param : []
     * @Description : The initialization for the crypto process.
     * @Author : Gadget
     * @Date : 2022/11/8 16:27
     */

    public void setup() {
        system.initAttributes();
        // If you want to change the mode of pairing, modify this code.
        system.e = PairingFactory.getPairing("a.properties");
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        // Initialize the bilinear group.
        system.G = system.e.getG1();
        system.GT = system.e.getGT();
        system.ZP = system.e.getZr();
        // Note: getImmutable restrict the change of variable can only be made by
        // assigning values.
        system.g = system.G.newElement().setToRandom().getImmutable();
        system.p = system.ZP.getNqr().getImmutable();
        // Initialize the secret random values and the corresponding public value.
        for (int i = 0; i < system.number_of_attributes; i++) {
            system.r_i[i] = system.ZP.newElement().setToRandom().getImmutable();
            system.R_i[i] = system.g.powZn(system.r_i[i]).getImmutable();
        }
    }

    public void printAttr() {
        System.out.println("Attributes:");
        for (String attr : system.attributesList) {
            System.out.print("|" + attr + "| ");
        }
        System.out.println();
        System.out.println("Please select the attributes according to the index of the corresponding attribute.");
    }

    /**
     * @return void
     * @throws
     * @Param : [receiver]
     * @Description : The method that generates the secret key.
     * @Author : Gadget
     * @Date : 2022/11/8 16:31
     */

    public void keyGen(ReceiverData receiver) {
        // Initialization for gamma.
        system.gamma = system.ZP.newElement().setToRandom().getImmutable();
        // The attribute set sigma and the corresponding secret key.
        for (int i = 0; i < system.number_of_attributes; i++) {
            // The elements in sigma represent the attributes of receiver.
            // receiver.sigma[i] = i;
            receiver.secret_key_sigma[i] = system.g
                    .powZn(system.gamma.getImmutable().div(system.r_i[i].getImmutable()));
        }
    }

    /**
     * @Param : [sender]
     * @Description : The hash generation for the sender.
     * @Author : Gadget
     * @Date : 2022/11/23 21:08
     * @return void
     * @throws
     */

    public void hashGen(SenderData sender) {
        // // Initialize the different policies.
        // for (int i = 0, j = 0; i < system.number_of_policy_read && j <
        // system.number_of_policy_edit; i++, j++) {
        // // This part is decided by the sender, and contains the determined policies.
        // sender.policies_read[i] = i;
        // sender.policies_edit[j] = j;
        // }
        // Randoms for message under the read policies.
        sender.o_1 = system.ZP.newElement().setToRandom().getImmutable();
        sender.o_2 = system.ZP.newElement().setToRandom().getImmutable();
        // Secret for the message
        sender.secret_const_read = system.ZP.newElement().setToOne().getImmutable();
        // Newton parameters
        for (int i = 0; i < system.number_of_policy_read; i++) {
            sender.K_read[i] = system.ZP.newElement().setToZero().getImmutable();
            sender.N_read[i] = system.ZP.newElement().setToOne().getImmutable();
        }
        // The secret.
        sender.K_read[0] = sender.secret_const_read;
        // The calculation for ciphertext.
        sender.C_m = system.e.pairing(system.g.getImmutable().powZn(sender.o_1.getImmutable()),
                system.g.getImmutable().powZn(sender.secret_const_read.getImmutable())).mul(sender.message);
        for (int i = 0; i < system.number_of_policy_read; i++) {
            sender.C_1_i[i] = system.R_i[i].duplicate().getImmutable().powZn(sender.K_read[i].getImmutable()
                    .mul(sender.N_read[i].getImmutable()).mul(sender.o_1.duplicate().getImmutable()));
            sender.C_2_i[i] = system.R_i[i].duplicate().getImmutable().powZn(sender.K_read[i].getImmutable()
                    .mul(sender.N_read[i].getImmutable()).mul(sender.o_2.duplicate().getImmutable()));
        }
        sender.C_3 = system.g
                .powZn(sender.o_2.duplicate().getImmutable().mul(sender.secret_const_read.duplicate().getImmutable()));
        // Randoms for message under the edit policies.
        sender.o_3 = system.ZP.newElement().setToRandom().getImmutable();
        sender.o_4 = system.ZP.newElement().setToRandom().getImmutable();
        // Secret for the message.
        sender.secret_const_edit = system.ZP.newElement().setToOne().getImmutable();
        // Newton parameters.
        for (int i = 0; i < system.number_of_policy_edit; i++) {
            sender.K_edit[i] = system.ZP.newElement().setToZero().getImmutable();
            sender.N_edit[i] = system.ZP.newElement().setToOne().getImmutable();
        }
        sender.K_edit[0] = sender.secret_const_edit;
        // The calculation for ciphertext.
        sender.C_x = system.e.pairing(system.g.getImmutable().powZn(sender.o_3.getImmutable()),
                system.g.getImmutable().powZn(sender.secret_const_edit.getImmutable())).mul(sender.chameleon_key);
        for (int i = 0; i < system.number_of_policy_edit; i++) {
            sender.C_4_i[i] = system.R_i[i].duplicate().getImmutable().powZn(sender.K_edit[i].getImmutable()
                    .mul(sender.N_edit[i].getImmutable()).mul(sender.o_3.duplicate().getImmutable()));
            sender.C_5_i[i] = system.R_i[i].duplicate().getImmutable().powZn(sender.K_edit[i].getImmutable()
                    .mul(sender.N_edit[i].getImmutable()).mul(sender.o_4.duplicate().getImmutable()));
        }
        sender.C_6 = system.g
                .powZn(sender.o_4.duplicate().getImmutable().mul(sender.secret_const_edit.duplicate().getImmutable()));
        sender.chameleon_big_int = new BigInteger(String.valueOf(sender.chameleon_key));
        sender.C_7 = system.g.duplicate().getImmutable().pow(sender.chameleon_big_int);
        // Hash value.
        sender.message_big_int = new BigInteger(String.valueOf(sender.message));
        sender.o_h = system.ZP.newElement().setToRandom().getImmutable();
        sender.hash_result = system.g.pow(sender.message_big_int)
                .mul(system.g.powZn(sender.o_h.duplicate().mul(sender.chameleon_big_int)));
    }

    /**
     * @return boolean
     * @throws
     * @Param : [message_big_int, o_h_1, sender]
     * @Description : The verification process of the hash value,
     * @Author : Gadget
     * @Date : 2022/11/8 17:02
     */

    public boolean verify(BigInteger message_big_int, Element o_h_1, SenderData sender) {
        Element hash_1;
        hash_1 = system.g.pow(message_big_int).mul(sender.C_7.powZn(o_h_1.duplicate().getImmutable()));
        return hash_1.isEqual(sender.hash_result);
    }

    public Element hashResult(BigInteger message_big_int, Element o_h_1, SenderData sender) {
        Element hash_1;
        hash_1 = system.g.pow(message_big_int).mul(sender.C_7.powZn(o_h_1.duplicate().getImmutable()));
        return hash_1;
    }

    /**
     * @return void
     * @throws
     * @Param : [receiver]
     * @Description : The trapdoor generation for each user.
     * @Author : Gadget
     * @Date : 2022/11/8 17:05
     */

    public void matchUser(ReceiverData receiver) {
        receiver.alpha = system.ZP.newElement().setToRandom().getImmutable();
        receiver.t_0 = system.g.powZn(receiver.alpha.duplicate().getImmutable());
        for (int i = 0; i < system.number_of_attributes; i++) {
            receiver.t_1_i[i] = receiver.secret_key_sigma[i].duplicate().getImmutable()
                    .powZn(receiver.alpha.duplicate().getImmutable());
        }
    }

    /**
     * @Param : [receiver, sender]
     * @Description : Verify whether the user can read.
     * @Author : Gadget
     * @Date : 2022/11/8 17:42
     * @return boolean
     * @throws
     */

    public boolean matchNodeRead(ReceiverData receiver, SenderData sender) {
        Element one = system.ZP.newElement().setToOne().getImmutable();

        for (int i = 0; i < system.number_of_policy_read; i++) {
            receiver.C_1_i_match[i] = sender.C_1_i[i].duplicate().getImmutable()
                    .powZn(one.duplicate().div(system.gamma.duplicate().getImmutable()));
            receiver.C_2_i_match[i] = sender.C_2_i[i].duplicate().getImmutable()
                    .powZn(one.duplicate().div(system.gamma.duplicate().getImmutable()));
        }

        Element read_comp = system.GT.newElement().setToOne().getImmutable();
        Element read_original = system.e.pairing(sender.C_3.duplicate().getImmutable(),
                receiver.t_0.duplicate().getImmutable());

        // Authorize the reader.
        for (int j = 1; j <= system.number_of_policy_read * system.number_of_attributes; j++) {
            for (int i = 0; i < system.number_of_policy_read; i++) {
                read_comp = read_comp.duplicate().getImmutable()
                        .mul(system.e.pairing(receiver.C_2_i_match[i].duplicate().getImmutable(),
                                receiver.t_1_i[i].duplicate().getImmutable()));
            }
        }
        boolean isReadable = read_comp.isEqual(read_original);
        isReadable = containsArray(sender.policies_read, receiver.sigma);
        return isReadable;

    }

    /**
     * @Param : [receiver, sender]
     * @Description : Verify whether the user can edit.
     * @Author : Gadget
     * @Date : 2022/11/8 17:42
     * @return boolean
     * @throws
     */

    public boolean matchNodeEdit(ReceiverData receiver, SenderData sender) {
        Element one = system.ZP.newElement().setToOne().getImmutable();
        for (int i = 0; i < system.number_of_policy_edit; i++) {
            receiver.C_4_i_match[i] = sender.C_4_i[i].duplicate().getImmutable()
                    .powZn(one.duplicate().div(system.gamma.duplicate().getImmutable()));
            receiver.C_5_i_match[i] = sender.C_5_i[i].duplicate().getImmutable()
                    .powZn(one.duplicate().div(system.gamma.duplicate().getImmutable()));
        }

        Element edit_comp = system.GT.newElement().setToOne().getImmutable();
        Element edit_original = system.e.pairing(sender.C_3.duplicate().getImmutable(),
                receiver.t_0.duplicate().getImmutable());

        // Authorize the editor.
        for (int j = 1; j <= system.number_of_policy_edit * system.number_of_attributes; j++) {
            for (int i = 0; i < system.number_of_policy_edit; i++) {
                edit_comp = edit_comp.duplicate().getImmutable()
                        .mul(system.e.pairing(receiver.C_5_i_match[i].duplicate().getImmutable(),
                                receiver.t_1_i[i].duplicate().getImmutable()));
            }
        }

        boolean isWriteable = edit_comp.isEqual(edit_original);

        isWriteable = containsArray(sender.policies_edit, receiver.sigma);
        return isWriteable;

    }

    /**
     * @Param : [receiver, sender]
     * @Description : Return the decrypted message for the authorized user.
     * @Author : Gadget
     * @Date : 2022/11/8 17:49
     * @return it.unisa.dia.gas.jpbc.Element
     * @throws
     */

    public String readMessage(ReceiverData receiver, SenderData sender) {
        if (verify(sender.message_big_int, sender.o_h, sender)) {
            Element readComponents = system.GT.newElement().setToOne().getImmutable();
            for (int i = 0; i < system.number_of_policy_read; i++) {
                readComponents = readComponents.duplicate().getImmutable()
                        .mul(system.e.pairing(receiver.C_1_i_match[i].duplicate().getImmutable(),
                                receiver.t_1_i[i].duplicate().getImmutable()));

            }

            String finalResult = sender.C_m.duplicate().getImmutable().div(readComponents.duplicate().getImmutable())
                    .toString();
            finalResult = receiver.parseBigInt2Str(sender.message);
            return finalResult;
        } else {
            return system.GT.newElement().setToZero().getImmutable().toString();
        }
    }

    /**
     * @Param : []
     * @Description : Modify the contents when the user is authorized to edit.
     * @Author : Gadget
     * @Date : 2022/11/8 17:56
     * @return it.unisa.dia.gas.jpbc.Element
     * @throws
     */

    public void adapt(ReceiverData receiver, @NotNull SenderData sender) {
        if (verify(sender.message_big_int, sender.o_h, sender)) {
            Element editComponents = system.GT.newElement().setToOne().getImmutable();
            for (int i = 0; i < system.number_of_policy_edit; i++) {
                editComponents = editComponents.duplicate().getImmutable()
                        .mul(system.e.pairing(sender.C_4_i[i].duplicate().getImmutable(),
                                receiver.t_1_i[i].duplicate().getImmutable()));
            }
            hashGen(sender);
        }
    }

    /**
     * @Param : [array1, array2]
     * @Description : Check whether the second array contains the first array.
     * @Author : Gadget
     * @Date : 2022/11/27 21:55
     * @return boolean
     * @throws
     */

    public static boolean containsArray(int[] subList, int[] mainList) {
        boolean isContainFull = true;
        List<Integer> list1 = Arrays.stream(mainList).boxed().collect(Collectors.toList());
        for (int policy : subList) {
            if (policy > 0) {
                isContainFull = list1.contains(policy);
            }
        }
        return isContainFull;
    }

    public void addNewUser() {
        SenderData newSender = new SenderData();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the sender name: ");
        newSender.user_id = sc.nextLine();
        this.senders.add(newSender);
        System.out.println("New sender " + newSender.user_id + " has been registered.");
    }

    public void encryptMessage(List<SenderData> senders, HashUpload newContract, Keccak256 hashCalc,
            APITest newContractInstance) {
        boolean userValid = false;
        SenderData sender = new SenderData();
        System.out.println("Enter the sender name: ");
        Scanner sc = new Scanner(System.in);
        String tempUserId = sc.nextLine();
        for (SenderData senderData : senders) {
            if (senderData.user_id.equals(tempUserId)) {
                userValid = true;
                sender = senderData;
                break;
            }
        }
        if (userValid) {
            System.out.println("Enter the message: ");
            String tempMessage = sc.nextLine();
            // Parse the message into the biginteger format.
            sender.parseStr2BigInt(tempMessage);
            printAttr();
            sender.enterReadAttributes();
            sender.enterEditAttributes();
            hashGen(sender);
            System.out.println("The encrypted message:");
            System.out.println("C_m: " + sender.C_m.toString());
            System.out.println("C_1: " + sender.C_1_i.toString());
            System.out.println("C_2: " + sender.C_2_i.toString());
            System.out.println("C_3: " + sender.C_3.toString());
            System.out.println("C_4: " + sender.C_4_i.toString());
            System.out.println("C_5: " + sender.C_5_i.toString());
            System.out.println("C_6: " + sender.C_6.toString());
            System.out.println("C_7: " + sender.C_7.toString());
            System.out.println("hash_value: " + sender.hash_result.toString());
            System.out.println("o_h: " + sender.o_h.toString());
            System.out.println("Uploading the hash value...");
            uploadChameleonHash(sender, newContract, hashCalc, newContractInstance);
        } else {
            System.out.println("The sender does not exist.");
        }
    }

    public void uploadChameleonHash(SenderData sender, HashUpload newContract, Keccak256 hashCalc,
            APITest newContractInstance) {
        sender.back_C_7 = sender.C_7;
        sender.back_hash_result = sender.hash_result;
        // Upload the hash value.
        newContractInstance.UploadHash(newContract, sender.user_id, hashCalc.hash(sender.hash_result.toString()));
    }

    public void addNewReceiver() {
        ReceiverData newReceiver = new ReceiverData();
        keyGen(newReceiver);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the receiver name: ");
        newReceiver.user_id = sc.nextLine();
        this.receivers.add(newReceiver);
        System.out.println("New receiver " + newReceiver.user_id + " has been registered.");
    }

    public void verifyHash(APITest newContractInstance, HashUpload newContract, Keccak256 hashCalc,
            List<SenderData> senders) {
        // Verify the hash value.
        boolean senderValid = true;
        SenderData sender = new SenderData();
        System.out.println("Enter the name of the sender you want to decrypt:");
        Scanner sc = new Scanner(System.in);
        String tempSenderId = sc.nextLine();
        for (SenderData senderData : senders) {
            if (senderData.user_id.equals(tempSenderId)) {
                senderValid = true;
                sender = senderData;
                break;
            }
        }

        if (senderValid) {
            String wait_for_verify = hashCalc.hash(sender.back_hash_result.toString());
            String hash_on_chain = newContractInstance.getHashValue(newContract, sender.user_id);
            if (wait_for_verify.equals(hash_on_chain)) {
                System.out.println("The hash value is verified.");
            } else {
                System.out.println("The hash value is not verified.");
            }
        } else {
            System.out.println("The sender does not exist.");
        }

    }

    public void revokeUser() {
        System.out.println("Enter the receiver name: ");
        Scanner sc = new Scanner(System.in);
        String tempUserId = sc.nextLine();
        for (ReceiverData receiverData : receivers) {
            if (receiverData.user_id.equals(tempUserId)) {
                receivers.remove(receiverData);
                System.out.println("The receiver " + tempUserId + " has been revoked.");
                return;
            }
        }
        System.out.println("The receiver does not exist.");
    }

    public void decrypt(APITest newContractInstance, HashUpload newContract, Keccak256 hashCalc,
            List<SenderData> senders,
            List<ReceiverData> receivers) {
        boolean senderValid = false;
        boolean receiverValid = false;
        ReceiverData receiver = new ReceiverData();
        System.out.println("Enter the receiver name: ");

        SenderData sender = new SenderData();
        Scanner sc = new Scanner(System.in);
        String tempUserId = sc.nextLine();
        for (ReceiverData receiverData : receivers) {
            if (receiverData.user_id.equals(tempUserId)) {
                receiverValid = true;
                receiver = receiverData;
                break;
            }
        }
        System.out.println("Enter the name of the sender you want to decrypt:");
        String tempSenderId = sc.nextLine();
        for (SenderData senderData : senders) {
            if (senderData.user_id.equals(tempSenderId)) {
                senderValid = true;
                sender = senderData;
                break;
            }
        }
        if (senderValid & receiverValid & !receiver.isRevoked) {
            printAttr();
            receiver.enterAttributes();
            matchUser(receiver);
            boolean canRead = matchNodeRead(receiver, sender);
            boolean canEdit = matchNodeEdit(receiver, sender);
            if (!canRead & !canEdit) {
                System.out.println("The receiver does not have the right to read or edit the message.");
            } else if (canRead & !canEdit) {
                System.out.println("The receiver only has the right to read the message.");
                System.out.println("Decrypting the message...");
                System.out.println("The decrypted message:");
                System.out.println(receiver.parseBigInt2Str(sender.message));
            } else {
                System.out.println("The receiver does have the right to edit the message.");
                System.out.println("Decrypting the message...");
                System.out.println("The decrypted message:");
                System.out.println(receiver.parseBigInt2Str(sender.message));
                System.out.println("You also have the right to modify the message!");
                System.out.println("Do you want to do it? (Y/N)");
                Scanner sc2 = new Scanner(System.in);
                String read = sc2.nextLine();
                if (read.toUpperCase().equals("Y")) {
                    // Want to modify the message.
                    System.out.println("Enter the new message: ");
                    String newMessage = sc2.nextLine();
                    sender.parseStr2BigInt(newMessage);
                    hashGen(sender);
                    System.out.println("The encrypted message:");
                    System.out.println("C_m: " + sender.C_m.toString());
                    System.out.println("C_1: " + sender.C_1_i.toString());
                    System.out.println("C_2: " + sender.C_2_i.toString());
                    System.out.println("C_3: " + sender.C_3.toString());
                    System.out.println("C_4: " + sender.C_4_i.toString());
                    System.out.println("C_5: " + sender.C_5_i.toString());
                    System.out.println("C_6: " + sender.C_6.toString());
                    System.out.println("C_7: " + sender.back_C_7.toString());
                    System.out.println("hash_value: " + sender.back_hash_result.toString());
                    System.out.println("o_h: " + sender.o_h.toString());
                }
            }
        } else {
            System.out.println("The receiver does not exist or being revoked.");
        }
    }

    public static void main(String[] args) throws ContractException, ConfigException {
        CryptoProc testInstance = new CryptoProc();
        APITest newContractInstance = new APITest();

        HashUpload newContract = newContractInstance.contractDeploy();
        Keccak256 hashCalc = new Keccak256();

        SystemUI uiSample = new SystemUI();
        testInstance.setup();

        while (true) {
            uiSample.startMenu();
            System.out.println("Enter your choice:");
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            if (choice.equals("1")) {
                uiSample.senderMenu();
                System.out.println("Enter your choice:");
                String user_choice = sc.nextLine();
                if (user_choice.equals("1")) {
                    testInstance.addNewUser();
                } else if (user_choice.equals("2")) {
                    testInstance.encryptMessage(testInstance.senders, newContract, hashCalc, newContractInstance);
                } else {
                    continue;
                }
            } else if (choice.equals("2")) {
                uiSample.receiverMenu();
                System.out.println("Enter your choice:");
                String user_choice = sc.nextLine();
                if (user_choice.equals("1")) {
                    testInstance.addNewReceiver();
                } else if (user_choice.equals("2")) {
                    testInstance.revokeUser();
                } else if (user_choice.equals("3")) {
                    testInstance.verifyHash(newContractInstance, newContract, hashCalc, testInstance.senders);
                    testInstance.decrypt(newContractInstance, newContract, hashCalc, testInstance.senders,
                            testInstance.receivers);
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
        System.out.println("Good bye!");
        exit(0);
    }
}