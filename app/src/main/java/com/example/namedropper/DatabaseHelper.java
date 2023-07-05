package com.example.namedropper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FRIENDS = "friends";
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_3 = "phone";
    public static final String COL_4 = "frequency";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_FRIENDS + " (" +
            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2 + " TEXT, " +
            COL_3 + " TEXT, " +
            COL_4 + " INTEGER);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }
    public boolean updateFriend(int id, String name, String phone, int frequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, phone);
        contentValues.put(COL_4, frequency);
        db.update(TABLE_FRIENDS, contentValues, COL_1 + " = ?", new String[]{Integer.toString(id)});
        return true;
    }
    public List<Friend> getAllFriends(SQLiteDatabase db) {
        List<Friend> friendList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FRIENDS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_1));
                String name = cursor.getString(cursor.getColumnIndex(COL_2));
                String phone = cursor.getString(cursor.getColumnIndex(COL_3));
                int frequency = cursor.getInt(cursor.getColumnIndex(COL_4));

                Friend friend = new Friend(id, name, phone, frequency);
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return friendList;
    }

}
