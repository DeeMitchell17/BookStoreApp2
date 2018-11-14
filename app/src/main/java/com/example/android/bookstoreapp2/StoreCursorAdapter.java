package com.example.android.bookstoreapp2;

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

import com.example.android.bookstoreapp2.data.StoreContract;


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

        final Button sale = view.findViewById ( R.id.sale);
        sale.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(quantityTextView.getText().toString());

                if (quantity > 0) {
                    quantity --;
                    quantityTextView.setText(String.valueOf(quantity));
                    ContentValues storeValues= new ContentValues ();
                    storeValues.put (StoreContract.ItemEntry.COLUMN_QUANTITY, quantity );

                }else {
                    Toast.makeText ( context, "Yikes, out of stock!", Toast.LENGTH_SHORT ).show ();
                }
            }
        } );
    }
}
