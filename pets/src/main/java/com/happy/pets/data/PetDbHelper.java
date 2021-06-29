package com.happy.pets.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DATABASE_NAME = "shelter.db";
    // 数据库版本
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PetDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + "("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,"
                + PetContract.PetEntry.COLUMN_PET_GENDER +" INTEGER NOT NULL,"
                + PetContract.PetEntry.COLUMN_PET_BREED +" TEXT NOT NULL,"
                + PetContract.PetEntry.COLUMN_PET_WEIGHT +" INTEGER NOT NULL DEFAULT 0)";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
