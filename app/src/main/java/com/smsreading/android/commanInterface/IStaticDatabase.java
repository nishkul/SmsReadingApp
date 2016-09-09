package com.smsreading.android.commanInterface;

/**
 * Created by root on 7/9/16.
 */

public interface IStaticDatabase {


    String TABLE_NAME = "static_table";
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_NAME = "name";


    String SELECT_ALL = " * ";
    String SELECT = " SELECT ";
    String FROM = " FROM ";
    String TEXT_TYPE = " TEXT";
    String COMMA_SEP = ",";
    String WHERE = " WHERE ";
    String AND = " AND ";
    String EQUAL_TO = " = ";

    String SQL_SELECT_QUERY = SELECT + SELECT_ALL + FROM + TABLE_NAME;

    String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + TEXT_TYPE +
                    " )";

    String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
