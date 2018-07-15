package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract;
import com.example.android.bookstoreapp.data.BookContract.BookEntry;
import com.example.android.bookstoreapp.data.BookDbHelper;

public class EditDataActivity extends AppCompatActivity {

    private EditText mAuthorNameEditText;
    private EditText mBookTitleEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneNumberEditText;
    private EditText mBookPriceEditText;
    private TextView mQuantityTextView;

    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_data_layout );

        mBookTitleEditText = (EditText) findViewById( R.id.edit_book_title );
        mAuthorNameEditText = (EditText) findViewById( R.id.edit_author_name );
        mQuantityTextView = (TextView) findViewById( R.id.quantity_text_view );
        mBookPriceEditText = (EditText) findViewById( R.id.edit_product_price );
        mSupplierNameEditText = (EditText) findViewById( R.id.supplier_name );
        mSupplierPhoneNumberEditText = (EditText) findViewById( R.id.supplier_phone_number );
    }

    public void increment(View view) {
        quantity = quantity + 1;
        TextView quantityTextView = (TextView) findViewById( R.id.quantity_text_view );
        quantityTextView.setText( "" + quantity );
    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText( this, "You cannot have less than 1 book", Toast.LENGTH_SHORT ).show();
            return;
        }
        quantity = quantity - 1;
        TextView quantityTextView = (TextView) findViewById( R.id.quantity_text_view );
        quantityTextView.setText( "" + quantity );
    }

    private void insertBook() {
        String bookTitleString = mBookTitleEditText.getText().toString().trim();
        String authorNameString = mAuthorNameEditText.getText().toString().trim();
        String quantityString = mQuantityTextView.getText().toString().trim();
        String bookPriceString = mBookPriceEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();

        int quantity = Integer.parseInt( quantityString );

        BookDbHelper mDbHelper = new BookDbHelper( this );

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( BookContract.BookEntry.COLUMN_BOOK_TITLE, authorNameString );
        values.put( BookContract.BookEntry.COLUMN_AUTHOR_NAME, bookTitleString );
        values.put( BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantityString );
        values.put( BookContract.BookEntry.COLUMN_BOOK_PRICE, bookPriceString );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_NAME, supplierNameString );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberString );

        long newRowId = db.insert( BookEntry.TABLE_NAME, null, values );

        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText( this, "Error with saving product", Toast.LENGTH_SHORT ).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText( this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.menu_edit, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertBook();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:

                NavUtils.navigateUpFromSameTask( this );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }
}

