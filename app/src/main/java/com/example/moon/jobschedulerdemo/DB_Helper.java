package com.example.moon.jobschedulerdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DB_Helper extends SQLiteOpenHelper {
    public static final String DB_NAME = "TEST";
    public static final String TABLE_NAME = "demotable";
    public static final String TABLE_CONTACT_NAME = "contact_table";
    public static final String COL_1 = "path";
    public static final String COl_2 = "isUploaded";

    public static final String COL_1_1 = "name";
    public static final String COL_1_2 = "phone_number";
    public static final String COL_1_3 = "isUploaded";
    private static final String TAG = "DB_Helper";


    public DB_Helper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " ( " + COL_1 + " Text PRIMARY KEY, "
                + COl_2  + " integer );";
        db.execSQL(sql);

        String sql_contact = "create table if not exists " + TABLE_CONTACT_NAME + " ( " + COL_1_1 + " Text , "
                + COL_1_2  + " Text PRIMARY KEY , "  + COL_1_3  + " integer );";
        db.execSQL(sql_contact);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertIntoContactTable(String name,String phone,int isUploaded){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1,name);
        contentValues.put(COL_1_2,phone);
        contentValues.put(COL_1_3,isUploaded);
        long insert = sqLiteDatabase.insert(TABLE_CONTACT_NAME, null, contentValues);
        if(insert>0)
            return true;
        return false;
    }


    public boolean checkExistanceOFNumber(String number){
        String sql = "select * from " + TABLE_CONTACT_NAME + " where " + COL_1_2 + " = ? ";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{number});
        if(cursor.getCount()>0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    public Cursor getAllContacts(){
        String sql = "select * from " + TABLE_CONTACT_NAME;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }


    public boolean insertIntoDB(String path, int isUploaded){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,path);
        contentValues.put(COl_2,isUploaded);
        long insert = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(insert>0)
            return true;

        return false;
    }


    public boolean checkExistance(String path){
        String sql = "select * from " +TABLE_NAME + " where " + COL_1 + " = ? ";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{path});
        if(cursor.getCount()>0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public Cursor getAllImages(){
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }
}
