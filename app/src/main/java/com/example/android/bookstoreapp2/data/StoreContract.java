package com.example.android.bookstoreapp2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class StoreContract {

    private StoreContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreapp2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_BOOKSTORE = "bookstore";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */

    public static final class ItemEntry implements BaseColumns {

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKSTORE);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKSTORE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKSTORE;


        /** Name of database table for items */
        public final static String TABLE_NAME = "bookstore";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the item.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="item";

        /**
         * price of the item.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Number of item available
         *
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Supplier name.
         *
         * Type: STRING
         */

        public final static String COLUMN_SUPPLIER_NAME = "supplier";

        /**
         * Supplier phone number.
         *
         * Type: INTEGER
         */

        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "contact";

    }

}

