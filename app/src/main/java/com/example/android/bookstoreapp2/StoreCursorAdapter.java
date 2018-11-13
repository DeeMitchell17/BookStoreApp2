package com.example.android.bookstoreapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp2.data.StoreContract;

/**
 * {@link StoreCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class StoreCursorAdapter extends CursorAdapter {

    int quantity = 0;

    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.store_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView nameTextView = view.findViewById (R.id.name);
        TextView priceTextView = view.findViewById (R.id.price);
        final TextView quantityTextView = view.findViewById (R.id.quantity);
        TextView supplierNameTextView = view.findViewById (R.id.supplier);

        final int nameColumnIndex = cursor.getColumnIndex ( StoreContract.ItemEntry.COLUMN_PRODUCT_NAME );
        int priceColumnIndex = cursor.getColumnIndex ( StoreContract.ItemEntry.COLUMN_PRICE );
        final int quantityColumnIndex = cursor.getColumnIndex ( StoreContract.ItemEntry.COLUMN_QUANTITY );
        int supplierColumnIndex = cursor.getColumnIndex(StoreContract.ItemEntry.COLUMN_SUPPLIER_NAME);

        String productName = cursor.getString (nameColumnIndex);
        final String productPrice = cursor.getString (priceColumnIndex);
        final String productQuantity = cursor.getString (quantityColumnIndex);
        String productSupplier = cursor.getString(supplierColumnIndex);

        nameTextView.setText (productName);
        priceTextView.setText (productPrice);
        quantityTextView.setText (String.valueOf (productQuantity) );
        supplierNameTextView.setText(productSupplier);

        final int cursorPosition = cursor.getPosition ();

        final Button sale = view.findViewById ( R.id.sale);
        sale.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ContentValues storeValues= new ContentValues ();
                cursor.moveToPosition ( cursorPosition );
                int quantity = 0;
                int itemId = cursor.getInt ( cursor.getColumnIndex ( StoreContract.ItemEntry._ID ) );
                quantity = Integer.parseInt (String.valueOf (quantity));
                quantity -= 1;
                if (quantity < 0) {
                    Toast.makeText ( context, "The quantity has decreased", Toast.LENGTH_SHORT ).show ();
                }
                storeValues.put (StoreContract.ItemEntry.COLUMN_QUANTITY, quantity );
                Uri productItemUri = ContentUris.withAppendedId (StoreContract.ItemEntry.CONTENT_URI, itemId );
                int rowsAffected = context.getContentResolver ().update (productItemUri, storeValues, null, null );
                if (rowsAffected > 0){
                    quantityTextView.setText ( String.format (String.valueOf(quantity)) );
                }
            }
        } );

    }
}
