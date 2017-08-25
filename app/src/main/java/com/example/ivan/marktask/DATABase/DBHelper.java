package com.example.ivan.marktask.DATABase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ivan on 04.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "task_db";

    public static final String DATABASE_TABLE_NAME = "all_tasks";

    public static final String TASK_ID = "task_id";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_DESCRIPTION = "task_description";
    public static final String TASK_DATE_DAY = "task_date_day";
    public static final String TASK_DATE_MONTH = "task_date_month";
    public static final String TASK_DATE_YEAR = "task_date_year";
    public static final String TASK_GROUP = "task_group";
    public static final String TASK_DONE = "task_done";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        DATABASE_TABLE_NAME + "(" +
                        TASK_ID + " integer primary_key, " +
                        TASK_NAME + " text, " +
                        TASK_DESCRIPTION + " text, " +
                        TASK_DATE_DAY + " integer, " +
                        TASK_DATE_MONTH + " integer, " +
                        TASK_DATE_YEAR + " integer, " +
                        TASK_GROUP + " text, " +
                        TASK_DONE + " numeric " +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
}

