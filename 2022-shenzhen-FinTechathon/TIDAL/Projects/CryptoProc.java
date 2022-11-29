package org.fisco.bcos.upload.contract;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.crypto.hash.Keccak256;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.*;

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