package com.smsreading.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.smsreading.android.commanInterface.IDatabaseConnection;
import com.smsreading.android.commanInterface.IStaticDatabase;

import java.util.ArrayList;

import dto.ExcelDatadto;

/**
 * Created by root on 7/9/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseConnection, IStaticDatabase {

    private static DatabaseHelper databaseHelper = null;
    private static String createExceltable;
    SQLiteDatabase sqLiteDatabase = null;

    public DatabaseHelper(Context context) {
        super(context, IDatabaseConnection.DATABASE_NAME, null, IDatabaseConnection.DATABASE_VERSION);
    }

    public static DatabaseHelper getinstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public static void onCreateCustom(Context context, ArrayList<ExcelDatadto> contentValues, int columnNO) {
        if (contentValues.size() > 0) ;
        String colun[] = new String[contentValues.size()];
        for (int i = 0; i < contentValues.size(); i++) {
            ExcelDatadto name = contentValues.get(i);
            colun[i] = name.getColumnName();
        }
        createExceltable = "create table  if not exists excel_table (" + colun[0] + " INTEGER PRIMARY KEY , " + colun[1] + " TEXT , " + colun[2] + " TEXT , "
                + colun[3] + " TEXT  )";
        DatabaseHelper db = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = db.getMyWritableDatabase();
        sqLiteDatabase.execSQL(createExceltable);
       // Log.v("value name =", createExceltable + "");
        //Log.v("value name =", IStaticDatabase.SQL_CREATE_ENTRIES + "");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(IStaticDatabase.SQL_CREATE_ENTRIES);

    }

    @Override
    public SQLiteDatabase getMyWritableDatabase() {
        if ((sqLiteDatabase == null) || (!sqLiteDatabase.isOpen())) {
            sqLiteDatabase = this.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(IStaticDatabase.SQL_CREATE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public String getData(@NonNull String select_qury) {
        String name = null;
        try {
            sqLiteDatabase = this.getMyWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(select_qury, new String[]{});
            if (cursor.moveToFirst()) {
                name = cursor.getString(1);
                Log.v("value name =", name + "");
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            sqLiteDatabase.close();
            e.printStackTrace();
        }
        return name;
    }


    public long insertData(@NonNull String tbl_name, @Nullable String null_value, @NonNull ContentValues contentValues) {
        try {
            sqLiteDatabase = this.getMyWritableDatabase();

            Log.i("qqqqqqqqq", "value insert " + contentValues.getAsString("Name"));
            Log.i("qqqqqqqqq", "value insert " + contentValues.getAsString("Marks"));
            Log.i("qqqqqqqqq", "value insert " + contentValues.getAsString("Grade"));
            Log.i("qqqqqqqqq", "value insert " + contentValues.getAsString("Sr"));
            long rowid = sqLiteDatabase.insert(tbl_name, null_value, contentValues);
            sqLiteDatabase.close();
            return rowid;
        } catch (Exception e) {
            sqLiteDatabase.close();
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void close() {
        super.close();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }
}
