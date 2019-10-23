package com.example.contentproviderdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 1;
    public static final String TBL_NAME_STUDENT = "student";

    private Context context;
    private static final String CREATE_STUDENT = "create table " + TBL_NAME_STUDENT +"(" +
            "_id integer primary key autoincrement, " +
            "name varchar(20), " +
            "classmate varchar(30), " +
            "age integer);";
    private static final String DROP_STUDENT = "drop table if exists " + TBL_NAME_STUDENT;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // 数据库创建方法：当获取数据库对象时，如果数据库不存在时会自动调用，若已经存在，则忽略
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT);
    }

    // 数据库升级方法，当新旧版本不一致时，会调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STUDENT);
        onCreate(db);
    }
}
