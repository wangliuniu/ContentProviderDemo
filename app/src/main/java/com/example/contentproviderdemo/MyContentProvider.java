package com.example.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    private static final String CONTENT = "content://";
    private static final String AUTHORIY = "edu.niit.android.content.provider";
    private static final String URI = CONTENT + AUTHORIY + "/" + DBHelper.TBL_NAME_STUDENT;

    public static final int STUDENTS_DIR = 1;//表示访问student表得所有数据
    public static final int STUDENTS_ITEM = 2;//表示访问student表得单条数据

    private static UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORIY, DBHelper.TBL_NAME_STUDENT, STUDENTS_DIR);
        matcher.addURI(AUTHORIY, DBHelper.TBL_NAME_STUDENT + "/#", STUDENTS_ITEM);
    }

    private DBHelper helper;
    private Uri uri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        helper = new DBHelper(getContext());
        return true;
    }

    public MyContentProvider() {
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        switch (matcher.match(uri)) {
            case STUDENTS_DIR:
                cursor = db.query(DBHelper.TBL_NAME_STUDENT, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STUDENTS_ITEM:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(DBHelper.TBL_NAME_STUDENT, projection,
                        "_id=?", new String[]{id}, null, null, sortOrder);
                break;
        }
        return cursor;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return 0;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Uri newUri = null;
        switch (matcher.match(uri)) {
            case STUDENTS_DIR:
            case STUDENTS_ITEM:
                long newId = db.insert(DBHelper.TBL_NAME_STUDENT, null, values);
                newUri = Uri.parse(URI + "/" + newId);
                break;
            default:
                break;
        }
        return newUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        return 0;
    }

    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case STUDENTS_DIR:
                return "vnd.android.cursor.div/vnd." + AUTHORIY;
            case STUDENTS_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORIY;
            default:
                break;
        }
        return null;
    }
}
