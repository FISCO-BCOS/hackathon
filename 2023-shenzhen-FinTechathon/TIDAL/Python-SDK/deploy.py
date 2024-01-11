from bomTest import *
from bomUtils import *
from enum import Enum
from functools import wraps
from chameleon import *
import json
from flask_sqlalchemy import SQLAlchemy

SK = 9583268362935874027000375325971484198933129732104461332343913
PK = 17642134390506942541899470387315131178079445707001368818596514012034
p = 18604511303632357477261733749289932684042548414204891841229696446591
q = 2238810024504495484628367478855587567273471529988554974877219789
g = 12340
emerge_rand = 0

dic = {}
data_old = {}

def selectAllTablesContents():
    # Initialize the database.

    tables = selectAllTables()

    db = init_database()
    cursor = db.cursor()

    all_table_contents = {}

    for table in tables:
        pre_fix = table[0]
        # First, we create the SQL statement.
        sql = "SELECT * FROM %s"
        # Then, we execute the SQL statement.
        cursor.execute(sql, (pre_fix))
        # Finally, we fetch all the data returned by the SQL statement.
        results = cursor.fetchall()
        all_table_contents[pre_fix] = results

    # Close the database.
    end_database(db)

    return all_table_contents


def queryAllContents():
    # Query all the contents by the key collection.
    contents = []
    keyCollection = getAllKeyData()
    for getKey in keyCollection:
        aimKey = getKey.replace(' ', '')
        # Query the blockchain.
        dataContent = queryHashIndices(aimKey)
        contents.append(dataContent)

    return contents

def read_dict_from_file(file_path):
    with open(file_path, 'r') as file:
        data = json.load(file)
    return data

def write_dict_to_file(data, file_path):
    with open(file_path, 'w') as file:
        json.dump(data, file)


file_path = 'randoness'
nos_file_path = 'nostalgia'

def queryHashIndices(getKey: str):
    # Query the blockchain to get the corresponding hash and indices.
    aimKey = getKey.replace(' ', '')
    information = selectByKey(aimKey)

    if information is None:
        # Provide hints.
        information = {'Key': 'Empty',
                       'databaseIndices': 'Empty', 'hashValue': 'Empty'}
    else:
        information['databaseIndices'] = ','.join(
            information['databaseIndices'])

    return information

# 定义角色
class Role(Enum):
    NO_ACCESS = 0  # 无权限
    READ_ONLY = 1  # 只读权限
    READ_WRITE = 2  # 读写权限

# 模拟用户对象
class User:
    def __init__(self, username, role):
        self.username = username
        self.role = role
        
# 获取当前用户的函数
def get_current_user():
    default_name = 'Alice'
    default_role = Role(getUser(default_name)[0][0])
    return User(default_name, default_role)


def insertModify(getKey: str, indices: str, content: str) -> (bool, str):
    try:
        aimKey = getKey.replace(' ', '')
        databaseIndices = indices.split(',')
        for data_index in databaseIndices:
            insertData(aimKey, data_index, content)

        dataHash = sha256(content.encode('utf-8')).hexdigest()
        # insertValues(aimKey, databaseIndices, dataHash)
        
        dic = read_dict_from_file(file_path)
        data_old = read_dict_from_file(nos_file_path)

        chameleon_hash = str2int(dataHash)
        rand1 = random.randint(1, q)

        dic[aimKey] = rand1
        data_old[aimKey] = dataHash
        
        write_dict_to_file(dic, file_path)
        write_dict_to_file(data_old, nos_file_path)

        transaction_hash = chameleonHash(PK=PK, g=g, m=chameleon_hash, r=rand1)

        insertValues(aimKey, databaseIndices, hex(transaction_hash))

        return True, hex(transaction_hash)
    except Exception as e:
        print(f"Error in insertModify: {str(e)}")
        return False, None


def processModify(getKey: str, new_content: str) -> str:
    # Proceed the modification on both database and blockchain.
    aimKey = getKey.replace(' ', '')
    databaseIndices = queryHashIndices(aimKey)['databaseIndices'].split(',')

    for data_index in databaseIndices:

        # First, we process the database.
        updateData(aimKey, data_index, new_content)

    # Then, we process the blockchain.
    dataHash = sha256(new_content.encode('utf-8')).hexdigest()
    
    dic = read_dict_from_file(file_path)
    data_old = read_dict_from_file(nos_file_path)

    oldMsg = data_old[aimKey]

    # The process of chameleon hash.
    chameleon_hash = str2int(dataHash)

    old_chameleon_hash = str2int(oldMsg)
    emerge_rand = dic[aimKey]
    randNew = forge(SK, old_chameleon_hash, emerge_rand, chameleon_hash)
    dic[aimKey] = randNew
    data_old[aimKey] = dataHash
    
    write_dict_to_file(dic, file_path)
    write_dict_to_file(data_old, nos_file_path)

    transaction_hash = chameleonHash(PK=PK, g=g, m=chameleon_hash, r=randNew)

    # updateHashOnKey(aimKey, hex(transaction_hash))

    return hex(transaction_hash)


def verifyHash(getKey: str) -> str:
    aimKey = getKey.replace(' ', '')
    # First, we get the indices.
    databaseIndices = queryHashIndices(aimKey)['databaseIndices'].split(',')

    blockchainHash = queryHashIndices(aimKey)['hashValue']

    isVerified = True
    
    dic = read_dict_from_file(file_path)
    data_old = read_dict_from_file(nos_file_path)

    for data_index in databaseIndices:

        # Then, we get the value from the database, and the hash from the blockchain.
        databaseContent = selectData(aimKey, data_index)[0][0]
        databaseHash = sha256(databaseContent.encode('utf-8')).hexdigest()
        rand = dic[aimKey]
        old_msg = data_old[aimKey]

        # The process of chameleon hash.
        chameleon_hash = str2int(databaseHash)
        old_chameleon_hash = str2int(old_msg)
        randNew = forge(SK, old_chameleon_hash, rand, chameleon_hash)
        transaction_hash = chameleonHash(
            PK=PK, g=g, m=chameleon_hash, r=randNew)

        if hex(transaction_hash) != blockchainHash:
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
    print(selectAllTablesContents())

if __name__ == '__main__':
    main()
