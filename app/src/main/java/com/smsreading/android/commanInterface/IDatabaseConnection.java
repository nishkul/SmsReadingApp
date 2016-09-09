package com.smsreading.android.commanInterface;

import android.database.sqlite.SQLiteDatabase;

import com.smsreading.android.database.DatabaseHelper;

/**
 * Created by root on 7/9/16.
 */

public interface IDatabaseConnection {


     int DATABASE_VERSION = 1;

     String DATABASE_NAME = "static_db";
    SQLiteDatabase getMyWritableDatabase();
}
