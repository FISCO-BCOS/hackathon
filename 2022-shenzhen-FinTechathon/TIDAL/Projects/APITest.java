package org.fisco.bcos.upload.contract;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.jetbrains.annotations.NotNull;

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
        System.out.println("Receipt: " + receipt);
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
        System.out.println("Receipt: " + receipt);
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