package com.pri.estimate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Estimate.db";
    private static final String DB_TABLE = "Hotel_Table";

    private static final String PRICE = "PRICE";
    private static final String COUNT = "COUNT";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" +
            PRICE + " INTEGER, " + COUNT + " INTEGER " + ")";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }

    public boolean insertData (int price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRICE, price);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1;     //if result = -1 doesn't insert
    }

    public boolean insertData2 (int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNT, count);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1;     //if result = -1 doesn't insert
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
