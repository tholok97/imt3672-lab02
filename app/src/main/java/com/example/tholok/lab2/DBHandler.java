package com.example.tholok.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tholok on 18.02.18.
 */

public class DBHandler extends SQLiteOpenHelper {


    private static final String LOG_NAME = "dbhandlerlog";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Topics.db";

    public static final String TABLE_TOPICS = "topics";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOPIC = "_topic";
    public static final String COLUMN_LINK = "_link";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_TOPICS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOPIC + " TEXT, " +
                COLUMN_LINK + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(sqLiteDatabase);

    }

    public void clear() {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TOPICS, "", null);

    }


    // add a new row to the database
    public void addTopic(Topic topic) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOPIC, topic.get_topic());
        values.put(COLUMN_LINK, topic.get_link());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TOPICS, null, values);


    }

    // delete product from the database
    public void deleteTopic(String topic) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_TOPICS +
                " WHERE " + COLUMN_TOPIC + " =\"" + topic + "\"");

    }


    public ArrayList<Topic> getAllTopics() {

        ArrayList<Topic> topics = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TOPICS;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String topic = cursor.getString(cursor.getColumnIndex(COLUMN_TOPIC));
                String link = cursor.getString(cursor.getColumnIndex(COLUMN_LINK));

                topics.add(new Topic(topic, link));

                cursor.moveToNext();
            }
        }

        return topics;

    }



}
