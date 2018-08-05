/**
 * Based on Udacity ABND course Pets App.
 */
package com.example.android.bookstoreapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract;

import static android.view.View.INVISIBLE;

public class EditDataActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri currentBookUri;

    private EditText mAuthorNameEditText;
    private EditText mBookTitleEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneNumberEditText;
    private EditText mBookPriceEditText;
    private TextView mQuantityTextView;
    private Button mDeleteButton;
    private Button mOrderButton;
    private Button incrementButton;
    private Button decrementButton;

    private boolean mBookHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_data_layout );
        Intent intent = getIntent();
        currentBookUri = intent.getData();

        mDeleteButton = findViewById( R.id.delete_button );
        mOrderButton = findViewById( R.id.order_button );

        if (currentBookUri == null) {

            setTitle( getString( R.string.editor_activity_title_new_product ) );

            mDeleteButton.setVisibility( INVISIBLE );
            mOrderButton.setVisibility( INVISIBLE );

            invalidateOptionsMenu();
        } else {

            setTitle( getString( R.string.editor_activity_title_edit_product ) );

            getLoaderManager().initLoader( EXISTING_BOOK_LOADER, null, this );

            mDeleteButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            } );
            mOrderButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderBook();
                }
            } );
        }

        mBookTitleEditText = findViewById( R.id.edit_book_title );
        mAuthorNameEditText = findViewById( R.id.edit_author_name );
        mQuantityTextView = findViewById( R.id.quantity_text_view );
        mBookPriceEditText = findViewById( R.id.edit_product_price );
        mSupplierNameEditText = findViewById( R.id.supplier_name );
        mSupplierPhoneNumberEditText = findViewById( R.id.supplier_phone_number );
        incrementButton = findViewById( R.id.increment );
        decrementButton = findViewById( R.id.decrement );

        mBookTitleEditText.setOnTouchListener( mTouchListener );
        mAuthorNameEditText.setOnTouchListener( mTouchListener );
        mQuantityTextView.setOnTouchListener( mTouchListener );
        mBookPriceEditText.setOnTouchListener( mTouchListener );
        mSupplierNameEditText.setOnTouchListener( mTouchListener );
        mSupplierPhoneNumberEditText.setOnTouchListener( mTouchListener );

        incrementButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt( mQuantityTextView.getText().toString() );
                quantity += 1;
                mQuantityTextView.setText( String.valueOf( quantity ) );
            }
        } );

        decrementButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt( mQuantityTextView.getText().toString() );
                if (quantity > 0) {
                    quantity -= 1;
                    mQuantityTextView.setText( String.valueOf( quantity ) );
                } else {
                    Toast.makeText( EditDataActivity.this, getString( R.string.no_product ), Toast.LENGTH_LONG ).show();
                }

            }
        } );
    }

    /**
     * Get user input and save that into database.
     */

    private void saveBook() {
        String bookTitleString = mBookTitleEditText.getText().toString().trim();
        String authorNameString = mAuthorNameEditText.getText().toString().trim();
        String productQuantityString = mQuantityTextView.getText().toString().trim();
        String bookPriceString = mBookPriceEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();

        if ((currentBookUri == null) &&
                TextUtils.isEmpty( bookTitleString ) && TextUtils.isEmpty( authorNameString ) &&
                TextUtils.isEmpty( productQuantityString ) && TextUtils.isEmpty( bookPriceString ) && TextUtils.isEmpty( supplierNameString ) &&
                TextUtils.isEmpty( supplierPhoneNumberString )) {

            return;
        }

        int quantity = Integer.parseInt( productQuantityString );

        if (TextUtils.isEmpty( bookTitleString )) {
            Toast.makeText( this, getString( R.string.product_name_required ), Toast.LENGTH_LONG ).show();
            return;
        }

        if (TextUtils.isEmpty( authorNameString )) {
            Toast.makeText( this, getString( R.string.author_name_required ), Toast.LENGTH_LONG ).show();
            return;
        }


        if (TextUtils.isEmpty( bookPriceString )) {
            Toast.makeText( this, getString( R.string.price_required ), Toast.LENGTH_LONG ).show();
            return;
        }

        if (TextUtils.isEmpty( productQuantityString )) {
            Toast.makeText( this, getString( R.string.quantity_required ), Toast.LENGTH_LONG ).show();
            return;
        }

        if (TextUtils.isEmpty( supplierNameString )) {
            Toast.makeText( this, getString( R.string.supplier_name_required ), Toast.LENGTH_LONG ).show();
            return;
        }

        if (TextUtils.isEmpty( supplierPhoneNumberString )) {
            Toast.makeText( this, getString( R.string.phone_number_required ), Toast.LENGTH_LONG ).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put( BookContract.BookEntry.COLUMN_BOOK_TITLE, bookTitleString );
        values.put( BookContract.BookEntry.COLUMN_AUTHOR_NAME, authorNameString );
        values.put( BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity );
        values.put( BookContract.BookEntry.COLUMN_BOOK_PRICE, bookPriceString );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_NAME, supplierNameString );
        values.put( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberString );

        if (currentBookUri == null) {

            Uri newUri = getContentResolver().insert( BookContract.BookEntry.CONTENT_URI, values );

            if (newUri == null) {

                Toast.makeText( this, getString( R.string.editor_insert_product_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {

                Toast.makeText( this, getString( R.string.editor_insert_product_successful ),
                        Toast.LENGTH_SHORT ).show();
            }

        } else {
            int rowsAffected = getContentResolver().update( currentBookUri, values, null, null );
            if (rowsAffected == 0) {

                Toast.makeText( this, getString( R.string.editor_update_product_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {

                Toast.makeText( this, getString( R.string.editor_update_product_successful ),
                        Toast.LENGTH_SHORT ).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.menu_edit, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu( menu );

        if (currentBookUri == null) {
            MenuItem menuItem = menu.findItem( R.id.action_delete );
            menuItem.setVisible( false );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                saveBook();
                return true;

            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask( EditDataActivity.this );
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask( EditDataActivity.this );
                            }
                        };
                showUnsavedChangesDialog( discardButtonClickListener );
                return true;
        }


        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressed() {

        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                };

        showUnsavedChangesDialog( discardButtonClickListener );
    }

    private void deleteBook() {

        if (currentBookUri != null) {

            int rowsDeleted = getContentResolver().delete( currentBookUri, null, null );

            if (rowsDeleted == 0) {

                Toast.makeText( this, getString( R.string.editor_delete_product_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {

                Toast.makeText( this, getString( R.string.editor_delete_product_successful ),
                        Toast.LENGTH_SHORT ).show();

                finish();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_TITLE,
                BookContract.BookEntry.COLUMN_AUTHOR_NAME,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_BOOK_PRICE,
                BookContract.BookEntry.COLUMN_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader( this,
                currentBookUri,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int bookTitleColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_TITLE );
            int authorNameColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_AUTHOR_NAME );
            int quantityColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_QUANTITY );
            int bookPriceColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_PRICE );
            int supplierNameColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_SUPPLIER_NAME );
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER );

            String bookName = cursor.getString( bookTitleColumnIndex );
            String authorName = cursor.getString( authorNameColumnIndex );
            int productQuantity = cursor.getInt( quantityColumnIndex );
            String bookPrice = cursor.getString( bookPriceColumnIndex );
            String supplierName = cursor.getString( supplierNameColumnIndex );
            String supplierPhoneNumber = cursor.getString( supplierPhoneNumberColumnIndex );

            mBookTitleEditText.setText( bookName );
            mAuthorNameEditText.setText( authorName );
            mQuantityTextView.setText( Integer.toString( productQuantity ) );
            mBookPriceEditText.setText( bookPrice );
            mSupplierNameEditText.setText( supplierName );
            mSupplierPhoneNumberEditText.setText( supplierPhoneNumber );
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( R.string.unsaved_changes_dialog_msg );
        builder.setPositiveButton( R.string.discard, discardButtonClickListener );
        builder.setNegativeButton( R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( R.string.delete_dialog_msg );
        builder.setPositiveButton( R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteBook();
            }
        } );
        builder.setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        } );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void orderBook() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.CALL_PHONE ) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new
                    String[]{android.Manifest.permission.CALL_PHONE}, 0 );
        } else {
            startActivity( new Intent( Intent.ACTION_CALL, Uri.parse( "tel:" + mSupplierPhoneNumberEditText ) ) );
        }
    }
}