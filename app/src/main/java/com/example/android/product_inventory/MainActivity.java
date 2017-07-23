/*
 While i was creating this project i mostly looked at those projects:
 https://github.com/laramartin/android_inventory
 https://github.com/sudhirkhanger/InventoryApp
 https://github.com/hendercine/ProductInventory

 Since it's my last project in Udacity scholarship i want to thank you
 for this opportunity.
 */

package com.example.android.product_inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.product_inventory.Utils.DbHandler;
import com.example.android.product_inventory.Utils.Product;
import com.example.android.product_inventory.Utils.ProductAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Product> productInventory = new ArrayList<>();
    private ListView productList;
    private TextView textView;
    private ProductAdapter adapter;
    private DbHandler db = new DbHandler(this);

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_database:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_label)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productList = (ListView) findViewById(R.id.list_products);
                                db.close();
                                db.deleteDatabase();
                                adapter.notifyDataSetChanged();
                                productList.setVisibility(View.GONE);

                            }
                        })
                        .setNegativeButton(R.string.cancel_button, null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addItemButton = (Button) findViewById(R.id.button_add_item);
        productList = (ListView) findViewById(R.id.list_products);
        textView = (TextView) findViewById(R.id.no_product);
        adapter = new ProductAdapter(MainActivity.this, productInventory);
        productList.setAdapter(adapter);
        productList.setEmptyView(textView);
        if (db.getProductsCount() == 0) {
        } else {
            productList.setVisibility(View.VISIBLE);
            productInventory.addAll(db.getAllProducts());
            adapter.notifyDataSetChanged();
        }
        // On list item Click open screen with detail info about product
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductInfo.class);
                intent.putExtra("class", productInventory.get(position));
                startActivityForResult(intent, 0);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddProduct.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(productList.getVisibility() == View.GONE)
        {
            productList.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
        adapter.clear();
        productInventory.clear();
        productInventory.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();

        if(productInventory != null)
        {
            if(productInventory.size() == 0)
            {
                productList.setVisibility(View.GONE);
            }
            else
            {
                productList.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            productList.setVisibility(View.GONE);
        }
    }
}

