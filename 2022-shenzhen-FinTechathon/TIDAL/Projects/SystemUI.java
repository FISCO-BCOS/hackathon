package org.fisco.bcos.upload.contract;

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