package com.example.android.bookstoreapp2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bookstoreapp2.data.StoreContract;

public class StoreItemActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

        private static final int BOOKSTORE_LOADER = 0;

        StoreCursorAdapter mCursorAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_store_item);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StoreItemActivity.this, AddItemActivity.class);
                    startActivity(intent);
                }
            });

            ListView storeListView = (ListView) findViewById(R.id.list);

            View emptyView = findViewById(R.id.empty_view);
            storeListView.setEmptyView(emptyView);

            mCursorAdapter = new StoreCursorAdapter(this, null);
            storeListView.setAdapter(mCursorAdapter);

            storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(StoreItemActivity.this, AddItemActivity.class);
                    Uri currentItemUri = ContentUris.withAppendedId(StoreContract.ItemEntry.CONTENT_URI, id);
                    intent.setData(currentItemUri);
                    startActivity(intent);
                }
            });

            getLoaderManager().initLoader(BOOKSTORE_LOADER, null, this);
        }

        @Override
        protected void onStart() {
            super.onStart();
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(mCursorAdapter);
        }

    private void insertItem() {
        ContentValues values = new ContentValues();
        values.put(StoreContract.ItemEntry.COLUMN_PRODUCT_NAME, getString(R.string.dummy_data_product_name));
        values.put(StoreContract.ItemEntry.COLUMN_PRICE, getString(R.string.dummy_data_price));
        values.put(StoreContract.ItemEntry.COLUMN_QUANTITY, getString(R.string.dummy_data_quantity));
        values.put(StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME, getString(R.string.dummy_data_supplier));
        values.put(StoreContract.ItemEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "203-500-6079");

        Uri newUri = getContentResolver().insert(StoreContract.ItemEntry.CONTENT_URI, values);
    }

        private void deleteAllItems() {
            int rowsDeleted = getContentResolver().delete(StoreContract.ItemEntry.CONTENT_URI, null, null);
            Log.v("StoreItemActivity", rowsDeleted + " rows deleted from bookstore database");
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_store_item, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_insert_dummy_data:
                    insertItem();
                    return true;
                case R.id.action_delete_all_entries:
                    deleteAllItems();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                StoreContract.ItemEntry._ID,
                StoreContract.ItemEntry.COLUMN_PRODUCT_NAME,
                StoreContract.ItemEntry.COLUMN_PRICE,
                StoreContract.ItemEntry.COLUMN_QUANTITY,
                StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME,
                StoreContract.ItemEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,
                StoreContract.ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
