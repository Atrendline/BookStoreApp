/**
 * Based on Udacity ABND course Pets App.
 */
package com.example.android.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super( context, c, 0 /* flags */ );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from( context ).inflate( R.layout.list_item, parent, false );
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final long id = cursor.getLong( cursor.getColumnIndexOrThrow( BookContract.BookEntry._ID ) );

        TextView bookNameTextView = view.findViewById( R.id.book_name );
        TextView authorNameTextView = view.findViewById( R.id.author_name );
        TextView productPriceTextView = view.findViewById( R.id.product_price );
        TextView productQuantityTextView = view.findViewById( R.id.product_quantity );
        Button saleButton = view.findViewById( R.id.sale_button );

        int bookNameColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_TITLE );
        int authorNameColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_AUTHOR_NAME );
        int productPriceColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_PRICE );
        int productQuantityColumnIndex = cursor.getColumnIndex( BookContract.BookEntry.COLUMN_BOOK_QUANTITY );

        final String bookName = cursor.getString( bookNameColumnIndex );
        final String authorName = cursor.getString( authorNameColumnIndex );
        final String productPrice = cursor.getString( productPriceColumnIndex );
        final String productQuantity = cursor.getString( productQuantityColumnIndex );

        bookNameTextView.setText( bookName );
        authorNameTextView.setText( authorName );
        productPriceTextView.setText( productPrice );
        productQuantityTextView.setText( productQuantity );

        saleButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt( productQuantity );
                if (quantity > 0) {
                    quantity = quantity - 1;
                } else {
                    Toast.makeText( context, "Run out", Toast.LENGTH_LONG ).show();
                }
                String newQuantity = String.valueOf( quantity );
                ContentValues values = new ContentValues();

                values.put( BookContract.BookEntry.COLUMN_BOOK_TITLE, bookName );
                values.put( BookContract.BookEntry.COLUMN_AUTHOR_NAME, authorName );
                values.put( BookContract.BookEntry.COLUMN_BOOK_PRICE, productPrice );
                values.put( BookContract.BookEntry.COLUMN_BOOK_QUANTITY, newQuantity );

                context.getContentResolver().update( ContentUris.withAppendedId( BookContract.BookEntry.CONTENT_URI, id ), values, null, null );
            }
        } );
    }

}
