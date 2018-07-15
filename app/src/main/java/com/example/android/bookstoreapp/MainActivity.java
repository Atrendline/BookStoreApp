package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bookstoreapp.data.BookContract;
import com.example.android.bookstoreapp.data.BookContract.BookEntry;
import com.example.android.bookstoreapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    private Button addButton;
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        addButton = (Button) findViewById( R.id.add_button );

        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, EditDataActivity.class );
                startActivity( intent );
            }
        } );

        mDbHelper = new BookDbHelper( this );
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_AUTHOR_NAME,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
        };

        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null );


        TextView displayView = (TextView) findViewById( R.id.text_view_book );

        try {

            displayView.setText( "The book table contains " + cursor.getCount() + " book.\n\n" );
            displayView.append( BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_TITLE + " - " +
                    BookEntry.COLUMN_AUTHOR_NAME + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + "\n" );

            int idColumnIndex = cursor.getColumnIndex( BookEntry._ID );
            int bookTitleColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_BOOK_TITLE );
            int authorNameColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_AUTHOR_NAME );
            int bookQuantityColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_BOOK_QUANTITY );
            int bookPriceColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_BOOK_PRICE );
            int supplierNameColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_SUPPLIER_NAME );
            int supplierPhoneColumnIndex = cursor.getColumnIndex( BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER );

            while (cursor.moveToNext()) {

                int currentID = cursor.getInt( idColumnIndex );
                String currentBookTitle = cursor.getString( bookTitleColumnIndex );
                String currentAuthorName = cursor.getString( authorNameColumnIndex );
                int currentBookQuantity = cursor.getInt( bookQuantityColumnIndex );
                int currentBookPrice = cursor.getInt( bookPriceColumnIndex );
                String currentSupplierName = cursor.getString( supplierNameColumnIndex );
                String currentSupplierPhone = cursor.getString( supplierPhoneColumnIndex );

                displayView.append( ("\n" + currentID + " - " +
                        currentBookTitle + " - " +
                        currentAuthorName + " - " +
                        currentBookQuantity + " - " +
                        currentBookPrice + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone) );
            }
        } finally

        {
            cursor.close();
        }
    }

    private void insertBook() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( BookContract.BookEntry.COLUMN_BOOK_TITLE, "bookTitleString" );
        values.put( BookContract.BookEntry.COLUMN_AUTHOR_NAME, "authorNameString" );
        values.put( BookContract.BookEntry.COLUMN_BOOK_QUANTITY, "quantityString" );
        values.put( BookContract.BookEntry.COLUMN_BOOK_PRICE, "bookPriceString" );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "supplierNameString" );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "supplierPhoneNumber" );

        long newRowId = db.insert( BookEntry.TABLE_NAME, null, values );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.menu_catalog, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.insert_data:
                insertBook();
                displayDatabaseInfo();
                return true;

            case R.id.delete_all_data:

                return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
