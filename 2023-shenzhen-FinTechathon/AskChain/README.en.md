1. # Blockchain ESG

   #### Introduction

   The { **Blockchain ESG Scoring System** is designed to provide companies with a transparent and traceable solution for evaluating ESG (Environmental, Social and Governance) projects. Allows companies to submit ESG project information, mapped to a unique NFT, and receive a score from an ESG assessment organization.
   This NFT will contain detailed information about the project as well as the score submitted by the ESG assessment organization, ensuring data immutability and transparency.
   The system will automatically calculate and maintain the total score of the project, which can be used as an objective standard for the quality of ESG projects for the reference of government, banks and other organizations.}

   #### Blockchain Deployment Tutorial

   1. According to the official FISCO BCOS document, download build_chain.sh

   URL: https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html

   2. Regulator builds node: bash build_chain.sh -l {government server ip}:1
   3. deploy smart contracts: using the console, deploy ESGPlatform smart contracts
   4. regulator publishes ca.crt and ca.key
   5. the enterprise or assessment organization obtains ca.crt and ca.key and places them in the same directory
   6. the enterprise or assessment organization or the project side builds a node: bash build_chain.sh -l {own server ip}:1 -k {directory where ca.crt/ca.key is located}


   #### Java Backend Deployment

   1. Clone the project locally from Gitee: git clone https://gitee.com/Leivon1Z/blockchain-esg.git
   2. Modify the project directory. /java-backend/src/main/java/resources/application-prod.yml:

   ```yml
   server: ``yml
     port: 2333
   
   bcos.
     key-store-path: {path to generate user keys, e.g. /Users/admin/account/key}
     config-file: {project directory/java-backend/config.toml}
     contract-name: ESGPlatform
     contract-address: {deployed contract address}
     contract-abi: {path to contract abi, e.g. /Users/admin/abi/}
   ```

   3. Go to the java-backend and execute the command: mvn clean package (maven needs to be installed beforehand)
   4. Execute the command: nohup java -jar target/java-backend-0.0.1-SNAPSHOT.jar &


   #### WeIdentity deployment steps

   1. Install the deployment tool according to the official WeIdentity documentation.

   https://weidentity.readthedocs.io/zh-cn/latest/docs/weidentity-installation-by-web.html

   2. Deploy WeIdentity using the visual deployment method.

   a) Select "WeID Raw Mode".

   b) Select "Affiliate Chain Committee Administrator" for blockchain administrators and "Non-Affiliate Chain Committee Administrator" for non-administrators.

   c) Configure the blockchain node with the following parameters:

   i. Organization name: AskChain

   ii. Communication ID: 1

   iii. Non-State Secret/State Secret SSL: Non-State Secret

   iv. Chain version: 2.0

   v. Blockchain Node IP and Channel Port: 127.0.0.1:202
