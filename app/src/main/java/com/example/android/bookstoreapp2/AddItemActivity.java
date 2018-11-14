package com.example.android.bookstoreapp2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.bookstoreapp2.data.StoreContract;


public class AddItemActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOKSTORE_LOADER = 0;

    private Uri mCurrentItemUri;

    private EditText mProductNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;

    private boolean mItemHasChanged = false;

    int quantity = 0;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(EXISTING_BOOKSTORE_LOADER, null, this);
        }

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_phone_number);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);


        Button mContactButton = findViewById(R.id.contact);
        mContactButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mSupplierPhoneNumberEditText.getText().toString().trim()));
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                }
            }
        } );

        Button mIncreaseButton = findViewById(R.id.increase);
        mIncreaseButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String quantityString = null;
                if (TextUtils.isEmpty(quantityString)) {
                    quantityString = mQuantityEditText.getText().toString().trim();
                    quantity = Integer.parseInt(quantityString);
                }
                quantity += 1;
                mQuantityEditText.setText(String.valueOf(quantity));
                mItemHasChanged = true;
            }
        } );


        Button mDecreaseButton = findViewById(R.id.decrease);
        mDecreaseButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String quantityString = null;
                if (TextUtils.isEmpty(quantityString)) {
                    quantityString = mQuantityEditText.getText().toString().trim();
                    quantity = Integer.parseInt(quantityString);
                }
                if (quantity == 0) {
                    return;
                }
                quantity -= 1;
                mQuantityEditText.setText(String.valueOf(quantity));
                mItemHasChanged = true;

            }

            });

        }

    private boolean saveItem() {

        String nameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String contactString = mSupplierPhoneNumberEditText.getText().toString().trim();

        if (!mItemHasChanged && mCurrentItemUri != null) {
            NavUtils.navigateUpFromSameTask(AddItemActivity.this);
            return true;
        }

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierNameString) ||
                TextUtils.isEmpty(contactString)) {
            Toast.makeText(this, "You must enter all information.", Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(StoreContract.ItemEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(StoreContract.ItemEntry.COLUMN_PRICE, priceString);
        values.put(StoreContract.ItemEntry.COLUMN_QUANTITY, quantityString);
        values.put(StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(StoreContract.ItemEntry.COLUMN_SUPPLIER_PHONE_NUMBER, contactString);

        if (!TextUtils.isEmpty(priceString)) {
            int itemPrice = Integer.parseInt(priceString);
            values.put(StoreContract.ItemEntry.COLUMN_PRICE, itemPrice);
        }

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(StoreContract.ItemEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.add_item_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.add_item_insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.add_item_update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.add_item_update_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddItemActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddItemActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
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

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                StoreContract.ItemEntry._ID,
                StoreContract.ItemEntry.COLUMN_PRODUCT_NAME,
                StoreContract.ItemEntry.COLUMN_PRICE,
                StoreContract.ItemEntry.COLUMN_QUANTITY,
                StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME,
                StoreContract.ItemEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int productColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME);
            int contactColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(productColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int contact = cursor.getInt(contactColumnIndex);

            mProductNameEditText.setText(name);
            mPriceEditText.setText(String.format(String.valueOf(price)));
            mQuantityEditText.setText(String.format(String.valueOf(quantity)));
            mSupplierNameEditText.setText(supplier);
            mSupplierPhoneNumberEditText.setText(String.format(String.valueOf(contact)));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {

        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.add_item_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.add_item_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}

