package com.jonathan.coordinapp.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Jonathan.db";
    private static final int DB_VER = 1;

    public AppDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BackupContract.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL(BackupContract.SQL_DELETE);
        onCreate(db);
    }
}

