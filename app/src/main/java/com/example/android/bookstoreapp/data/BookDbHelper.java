/**
 * Based on Udacity ABND course Pets App.
 */
package com.example.android.bookstoreapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "bookStore.db";
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + "("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_BOOK_TITLE + " TEXT, "
                + BookContract.BookEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL);";

        db.execSQL( SQL_CREATE_BOOK_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
