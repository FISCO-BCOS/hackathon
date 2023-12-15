#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pymysql


def init_database():
    # Connect to the database.
    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='test',
                         password='123456',
                         database='bom_data',
                         charset='utf8')

    return db


def end_database(db):
    db.close()


def selectData(aimKey, database_index):
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    pre_fix = 'bom_data_part_' + str(database_index)
    # First, we create the SQL statement.
    sql = "SELECT data FROM %s WHERE bom_key='%s'"
    # Then, we execute the SQL statement.
    cursor.execute(sql)
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    # Close the database.
    end_database(db, (pre_fix, aimKey))

    return results


def insertData(aimKey, database_index, content):
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    pre_fix = 'bom_data_part_' + str(database_index)
    # First, we create the SQL statement.
    sql = "INSERT INTO %s(bom_key, data) VALUES('%s', '%s')"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (pre_fix, aimKey, content))
    try:
        db.commit()
    except Exception as e:
        db.rollback()
    # Close the database.
    end_database(db)


def updateData(aimKey, database_index, new_content):
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    pre_fix = 'bom_data_part_' + str(database_index)
    # First, we create the SQL statement.
    sql = "UPDATE %s SET data='%s' WHERE bom_key='%s'"
        
    # Then, we execute the SQL statement.
    cursor.execute(sql, (pre_fix, new_content, aimKey))
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
    # Close the database.
    end_database(db)


def removeData(aimKey, database_index):
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    pre_fix = 'bom_data_part_' + str(database_index)
    # First, we create the SQL statement.
    sql = "DELETE FROM %s WHERE bom_key='%s'"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (pre_fix, aimKey))
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
    # Close the database.
    end_database(db)

def getAllKeyData():
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    # First, we create the SQL statement.
    sql_tables = "SHOW TABLES"
    # Then, we execute the SQL statement.
    cursor.execute(sql_tables)
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    
    # Key collection.
    all_keys = []
    
    # Iterate over all tables.
    for key_table in results:
        sql = "SELECT bom_key FROM %s"
        cursor.execute(sql, (key_table[0]))
        # Finally, we fetch all the data returned by the SQL statement.
        key_results = cursor.fetchall()
        for keys in key_results:
            all_keys.append(keys[0])
    # Close the database.
    end_database(db)
    
    # Return all keys.
    return list(set(all_keys))

def main():
    # print(selectData('A', 1))
    # print(getAllKeyData())
    # insertData('B', 1, 'BIT')
    # updateData('A', 1, 'BUPT')
    # removeData('8月碳排放量定额', 3)
    # removeData('8月碳排放量定额', 5)
    removeData('A', 1)


if __name__ == '__main__':
    main()
