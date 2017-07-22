package com.example.android.product_inventory.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.product_inventory.R;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> products = new ArrayList<>();

    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    DbHandler db = new DbHandler(getContext());

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listItemView = view;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Product currentProduct = getItem(position);

        // Initialize variables with views and button
        Button sellButton = (Button) listItemView.findViewById(R.id.button_sell);
        TextView productNameView = (TextView) listItemView.findViewById(R.id.product_name);
        TextView productPriceView = (TextView) listItemView.findViewById(R.id.list_product_price);
        final TextView availableView = (TextView) listItemView.findViewById(R.id.products_available);
        final TextView productSoldView = (TextView) listItemView.findViewById(R.id.product_sold);
        // Set text on text views
        productNameView.setText(currentProduct.getName());
        availableView.setText(Integer.toString(currentProduct.getAvailable()));
        productPriceView.setText("$" + Float.toString(currentProduct.getPrice()));
        productSoldView.setText(Integer.toString(currentProduct.getSold()));
        // Set on Click Listner on sell Button and update number of available products
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int available = Integer.parseInt(availableView.getText().toString());
                int sold = Integer.parseInt(productSoldView.getText().toString());

                if (available > 0) {
                    sold++;
                    available--;
                    currentProduct.setAvailable(available);
                    currentProduct.setSold(sold);
                    availableView.setText(Integer.toString(available));
                    productSoldView.setText(Integer.toString(sold));
                    db.updateProduct(currentProduct);
                }

            }
        });
        return listItemView;
    }
}
