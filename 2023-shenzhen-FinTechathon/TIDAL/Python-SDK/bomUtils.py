#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pymysql


def init_database():
    # Connect to the database.
    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='Ilove',
                         password='FiscoBcos',
                         database='WeBank',
                         charset='utf8')

    return db


def end_database(db):
    db.close()
    
def init_user_database():
    # Connect to the database.
    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='Ilove',
                         password='FiscoBcos',
                         database='WeBank',
                         charset='utf8')

    return db

def end_user_database(db):
    db.close()
    
def selectAllUsers():
    db = init_user_database()
    cursor = db.cursor()

    sql = "SELECT username, role FROM user;"

    cursor.execute(sql)
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    # Close the database.
    end_user_database(db)

    return results

def getUser(username):
    db = init_user_database()
    cursor = db.cursor()

    sql = "SELECT role FROM user WHERE username='%s';"

    cursor.execute(sql, (username))
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    # Close the database.
    end_user_database(db)

    return results

def insertUser(username, role):
    # Initialize the database.
    db = init_user_database()
    cursor = db.cursor()
    # First, we create the SQL statement.
    sql = "INSERT INTO user(username, role) VALUES('%s', '%s')"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (username, role))
    try:
        db.commit()
    except Exception as e:
        db.rollback()
    # Close the database.
    end_user_database(db)
    
def modifyUser(username, role):
    # Initialize the database.
    db = init_user_database()
    cursor = db.cursor()
    # First, we create the SQL statement.
    sql = "UPDATE user SET role='%s' WHERE username='%s'"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (role, username))
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
    # Close the database.
    end_user_database(db)
    
def removeUser(username):
    # Initialize the database.
    db = init_user_database()
    cursor = db.cursor()
    # First, we create the SQL statement.
    sql = "DELETE FROM user WHERE username='%s'"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (username))
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
    # Close the database.
    end_user_database(db)
    
def selectAllTables():
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()

    sql = "show tables;"

    cursor.execute(sql)
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    # Close the database.
    end_database(db)

    return results


def selectData(aimKey, database_index):
    # Initialize the database.
    db = init_database()
    cursor = db.cursor()
    pre_fix = 'bom_data_part_' + str(database_index)
    # First, we create the SQL statement.
    sql = "SELECT data FROM %s WHERE bom_key='%s'"
    # Then, we execute the SQL statement.
    cursor.execute(sql, (pre_fix, aimKey))
    # Finally, we fetch all the data returned by the SQL statement.
    results = cursor.fetchall()
    # Close the database.
    end_database(db)

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
    print(getUser('Bob'))


if __name__ == '__main__':
    main()
