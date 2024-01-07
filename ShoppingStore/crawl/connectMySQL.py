import mysql.connector


def connect():

    mydb = mysql.connector.connect(
        host = "localhost",
        user = "root",
        password = "123456"
    )

    return mydb.cursor()