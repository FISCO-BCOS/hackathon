from bomTest import *
from bomUtils import *
from chameleon import *

SK = 9583268362935874027000375325971484198933129732104461332343913
PK = 17642134390506942541899470387315131178079445707001368818596514012034
p = 18604511303632357477261733749289932684042548414204891841229696446591
q = 2238810024504495484628367478855587567273471529988554974877219789
g = 12340
emerge_rand = 581629177965547595704598172813834735046725681233233305045158032

def queryAllContents():
    # Query all the contents by the key collection.
    contents = []
    keyCollection = getAllKeyData()
    
    for getKey in keyCollection:
        aimKey = getKey.replace(' ','')
        # Query the blockchain.
        dataContent = queryHashIndices(aimKey)
        contents.append(dataContent)
    
    return contents

def queryHashIndices(getKey):
    # Query the blockchain to get the corresponding hash and indices.
    aimKey = getKey.replace(' ','')
    information = selectByKey(aimKey)
    
    if information is None:
        # Provide hints.
        information = {'Key': 'Empty', 'databaseIndices': 'Empty', 'hashValue': 'Empty'}
    else:
        information['databaseIndices'] = ','.join(information['databaseIndices'])
    
    return information

""" def insertModify(getKey, indices, content):
    aimKey = getKey.replace(' ','')
    # Insert the modification into the database.
    databaseIndices = indices.split(',')
    
    for data_index in databaseIndices:
        
        # First, we insert the data into the database.
        insertData(aimKey, data_index, content)
        
    # Then, we insert the hash into the blockchain.
    dataHash = sha256(content.encode('utf-8')).hexdigest()
    
    insertValues(aimKey, databaseIndices, dataHash)
    
    # The process of chameleon hash.
    
    chameleon_hash = str2int(dataHash)

    # For this emerging situation, it should be a random number.
    rand1 = emerge_rand
    
    transaction_hash = chameleonHash(PK=PK, g=g, m=chameleon_hash, r=rand1)
    
    return transaction_hash  """

def insertModify(getKey, indices, content):
    try:
        aimKey = getKey.replace(' ','')
        databaseIndices = indices.split(',')
        
        for data_index in databaseIndices:
            insertData(aimKey, data_index, content)
        
        dataHash = sha256(content.encode('utf-8')).hexdigest()
        insertValues(aimKey, databaseIndices, dataHash)
        
        chameleon_hash = str2int(dataHash)
        rand1 = emerge_rand
        transaction_hash = chameleonHash(PK=PK, g=g, m=chameleon_hash, r=rand1)
        
        return True, str(transaction_hash)
    except Exception as e:
        print(f"Error in insertModify: {str(e)}")
        return False, None

def processModify(getKey, new_content):
    # Proceed the modification on both database and blockchain.
    aimKey = getKey.replace(' ','')
    databaseIndices = queryHashIndices(aimKey)['databaseIndices'].split(',')
    
    for data_index in databaseIndices:
    
        # First, we process the database.
        updateData(aimKey, data_index, new_content)
    
    # Then, we process the blockchain.
    dataHash = sha256(new_content.encode('utf-8')).hexdigest()
    
    oldHash = queryHashIndices(aimKey)['hashValue']
    
    # The process of chameleon hash.
    chameleon_hash = str2int(dataHash)

    old_chameleon_hash = str2int(oldHash)
    
    rand2 = forge(SK, old_chameleon_hash, emerge_rand, chameleon_hash)
    
    transaction_hash = chameleonHash(PK=PK, g=g, m=chameleon_hash, r=rand2)
    
    updateHashOnKey(aimKey, dataHash)
    
    return str(transaction_hash)
    
def verifyHash(getKey):
    aimKey = getKey.replace(' ','')
    # First, we get the indices.
    databaseIndices = queryHashIndices(aimKey)['databaseIndices'].split(',')
    
    blockchainHash = queryHashIndices(aimKey)['hashValue']
    
    isVerified = True
    
    for data_index in databaseIndices:
        
    
        # Then, we get the value from the database, and the hash from the blockchain.
        databaseContent = selectData(aimKey, data_index)[0][0]
        databaseHash = sha256(databaseContent.encode('utf-8')).hexdigest()
    
        if databaseHash != blockchainHash:
            isVerified = False
    
    if isVerified:
        return "Data Verification Completed!"
    else:
        return "Data Verification Error!"
    
def chameleonHash(PK, g, m, r):
    if isinstance(m, str):
        m = str2int(m)
    return quickPower(g, m, p) * quickPower(PK, r, p) % p

def forge(SK, m1, r1, m2):
    if isinstance(m1, str):
        m1 = str2int(m1)
    if isinstance(m2, str):
        m2 = str2int(m2)
    x, _, _ = exgcd(SK, q)
    return x * (m1 - m2 + SK * r1) % q
    
def main():
    testKey = 'Great_Teacher'
    testContent = 'Chuan_Zhang'
    newContent = 'Liehuang_Zhu'
    # testIndices = "1,3,5"
    # old_tx_hash = insertModify(testKey, testIndices, testContent)
    old_tx_hash = processModify(testKey, testContent)
    print(old_tx_hash)
    print(queryHashIndices(testKey))
    new_tx_hash = processModify(testKey, newContent)
    print(new_tx_hash)
    queryHashIndices(testKey)
    print(verifyHash(testKey))
    print(queryAllContents())
    
if __name__ == '__main__':
    main()