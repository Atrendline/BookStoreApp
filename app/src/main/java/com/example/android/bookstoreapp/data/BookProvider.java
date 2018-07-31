/**
 * Based on Udacity ABND course Pets App.
 */
package com.example.android.bookstoreapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    private static final int BOOKS = 100;

    private static final int BOOK_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher( UriMatcher.NO_MATCH );

    static {

        sUriMatcher.addURI( BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS );

        sUriMatcher.addURI( BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID );
    }

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper( getContext() );
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match( uri );
        switch (match) {
            case BOOKS:
                cursor = database.query( BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );

                break;
            case BOOK_ID:

                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};

                cursor = database.query( BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder );
                break;
            default:
                throw new IllegalArgumentException( "Cannot query unknown URI " + uri );
        }
        cursor.setNotificationUri( getContext().getContentResolver(), uri );
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match( uri );
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException( "Unknown URI " + uri + " with match " + match );
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match( uri );
        switch (match) {
            case BOOKS:
                return insertBook( uri, contentValues );
            default:
                throw new IllegalArgumentException( "Insertion is not supported for " + uri );
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        //Check the names are not null.
        String bookName = values.getAsString( BookContract.BookEntry.COLUMN_BOOK_TITLE );
        if (bookName == null) {
            throw new IllegalArgumentException( "product_name_required" );
        }

        String authorName = values.getAsString( BookContract.BookEntry.COLUMN_AUTHOR_NAME );
        if (authorName == null) {
            throw new IllegalArgumentException( "author_name_required" );
        }

        Integer productPrice = values.getAsInteger( BookContract.BookEntry.COLUMN_BOOK_PRICE );
        {
            if (productPrice == null || productPrice <= 0) {
                throw new IllegalArgumentException( "price_required" );
            }
        }

        Integer productQuantity = values.getAsInteger( BookContract.BookEntry.COLUMN_BOOK_QUANTITY );
        {
            if (productQuantity == null || productQuantity < 0) {
                throw new IllegalArgumentException( "quantity_required" );
            }
        }

        String supplierName = values.getAsString( BookContract.BookEntry.COLUMN_SUPPLIER_NAME );
        if (supplierName == null) {
            throw new IllegalArgumentException( "supplier_name_required" );
        }

        Integer supplierPhoneNumber = values.getAsInteger( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER );
        {
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException( "phone_number_required" );
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert( BookContract.BookEntry.TABLE_NAME, null, values );

        Log.e( LOG_TAG, "The book is saved with id: " + id );

        if (id == -1) {
            Log.e( LOG_TAG, "Failed to insert row for " + uri );
            return null;
        }
        getContext().getContentResolver().notifyChange( uri, null );

        return ContentUris.withAppendedId( uri, id );
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match( uri );
        switch (match) {
            case BOOKS:
                return updateBook( uri, contentValues, selection, selectionArgs );
            case BOOK_ID:

                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};
                return updateBook( uri, contentValues, selection, selectionArgs );
            default:
                throw new IllegalArgumentException( "Update is not supported for " + uri );
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey( BookContract.BookEntry.COLUMN_BOOK_TITLE )) {
            String bookName = values.getAsString( BookContract.BookEntry.COLUMN_BOOK_TITLE );
            if (bookName == null) {
                throw new IllegalArgumentException( "product_name_required" );
            }
        }
        if (values.containsKey( BookContract.BookEntry.COLUMN_AUTHOR_NAME )) {
            String authorName = values.getAsString( BookContract.BookEntry.COLUMN_AUTHOR_NAME );
            if (authorName == null) {
                throw new IllegalArgumentException( "author_name_required" );
            }
        }

        if (values.containsKey( BookContract.BookEntry.COLUMN_BOOK_PRICE )) {
            String productPrice = values.getAsString( BookContract.BookEntry.COLUMN_BOOK_PRICE );
            if (productPrice == null) {
                throw new IllegalArgumentException( "price_required" );
            }
        }

        if (values.containsKey( BookContract.BookEntry.COLUMN_BOOK_QUANTITY )) {
            String productQuantity = values.getAsString( BookContract.BookEntry.COLUMN_BOOK_QUANTITY );
            if (productQuantity == null) {
                throw new IllegalArgumentException( "quantity_required" );
            }
        }

        if (values.containsKey( BookContract.BookEntry.COLUMN_SUPPLIER_NAME )) {
            String supplierName = values.getAsString( BookContract.BookEntry.COLUMN_SUPPLIER_NAME );
            if (supplierName == null) {
                throw new IllegalArgumentException( "supplier_name_required" );
            }
        }

        if (values.containsKey( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER )) {
            String supplierPhoneNumber = values.getAsString( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER );
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException( "phone_number_required" );
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update( BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs );

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange( uri, null );
        }

        return rowsUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match( uri );
        switch (match) {
            case BOOKS:

                rowsDeleted = database.delete( BookContract.BookEntry.TABLE_NAME, selection, selectionArgs );
                break;
            case BOOK_ID:

                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};
                rowsDeleted = database.delete( BookContract.BookEntry.TABLE_NAME, selection, selectionArgs );
                break;
            default:
                throw new IllegalArgumentException( "Deletion is not supported for " + uri );
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange( uri, null );
        }

        return rowsDeleted;
    }

}