/**
 * Based on Udacity ABND course Pets App.
 */


package com.example.android.bookstoreapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public final static String TABLE_NAME = "book";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_BOOK_TITLE = "book_title";

        public final static String COLUMN_AUTHOR_NAME = "author_name";

        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        public final static String COLUMN_BOOK_PRICE = "book_price";

        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";

        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
