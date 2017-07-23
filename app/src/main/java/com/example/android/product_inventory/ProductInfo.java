package com.example.android.product_inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.product_inventory.Utils.DbHandler;
import com.example.android.product_inventory.Utils.Product;
import com.example.android.product_inventory.Utils.Validation;

import java.io.FileDescriptor;
import java.io.IOException;

public class ProductInfo extends AppCompatActivity {
    Validation validator = new Validation();
    DbHandler db = new DbHandler(this);
    Product product;
    private static final String TAG = "ProductInfo";

    @Override
    public void setActionBar(Toolbar toolbar) {
        super.setActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(R.string.product);
        actionBar.show();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        TextView productNameView = (TextView) findViewById(R.id.product_name);
        TextView productPriceView = (TextView) findViewById(R.id.product_price);
        TextView productSoldView = (TextView) findViewById(R.id.products_sold);
        ImageView detailImageView = (ImageView) findViewById(R.id.product_image);
        final TextView productAvailableView = (TextView) findViewById(R.id.available_products);
        
        final TextView changeAvailableView = (TextView) findViewById(R.id.change_available);
        final LinearLayout modifyAvailable = (LinearLayout) findViewById(R.id.modify_available);
        final LinearLayout editConfirm = (LinearLayout) findViewById(R.id.edit_confirm);
        final Button doneButton = (Button) findViewById(R.id.button_done);
        final Button cancelButton = (Button) findViewById(R.id.button_cancel);
        final Button moreButton = (Button) findViewById(R.id.up);
        final Button lessButton = (Button) findViewById(R.id.down);
        final EditText editAvailableText = (EditText) findViewById(R.id.edit_available);
        final TextView editStatusMessage = (TextView) findViewById(R.id.edit_status_message);
        final Button orderButton = (Button) findViewById(R.id.button_order);
        Button deleteButton = (Button) findViewById(R.id.button_delete);
        Button editButton = (Button) findViewById(R.id.button_edit);

        product = (Product) getIntent().getSerializableExtra("class");
        productNameView.setText(product.getName());
        productPriceView.setText("$" + Float.toString(product.getPrice()));
        productAvailableView.setText(Integer.toString(product.getAvailable()));
        productSoldView.setText(Integer.toString(product.getSold()));
        detailImageView.setImageBitmap(getBitmapFromUri(Uri.parse(product.getImageString())));


        moreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int available = Integer.parseInt(editAvailableText.getText().toString());
                available++;
                editAvailableText.setText(Integer.toString(available));
            }
        });

        lessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int available = Integer.parseInt(editAvailableText.getText().toString());
                if (available > 0) {
                    available--;
                    editAvailableText.setText(Integer.toString(available));
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!validator.isInteger(editAvailableText)) {
                    editStatusMessage.setVisibility(View.VISIBLE);
                } else {
                    if(validator.isPositive(Integer.parseInt(editAvailableText.getText().toString()))) {
                        setVisToHide(modifyAvailable, editConfirm, changeAvailableView, editStatusMessage);
                        int available = Integer.parseInt(editAvailableText.getText().toString());
                        product.setAvailable(available);
                        productAvailableView.setText(Integer.toString(available));
                        db.updateProduct(product);
                        editAvailableText.setText("");
                    }else{
                        editStatusMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editAvailableText.setText("");
                setVisToHide(modifyAvailable, editConfirm, changeAvailableView, editStatusMessage);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                changeAvailableView.setVisibility(View.VISIBLE);
                modifyAvailable.setVisibility(View.VISIBLE);
                editConfirm.setVisibility(View.VISIBLE);
                editAvailableText.setText(Integer.toString(product.getAvailable()));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductInfo.this);
                alertDialog.setMessage("Are you sure?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteProduct(product);
                                Intent intent = new Intent(ProductInfo.this, MainActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                alertDialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create();
                alert11.show();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{product.getSupplier()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "ORDER PRODUCT");
                intent.putExtra(Intent.EXTRA_TEXT, " ID: " + product.getId() + "\n" + " name: " + product.getName());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void setVisToHide(LinearLayout lv1, LinearLayout lv2, TextView tv, TextView editQtyError) {
        lv1.setVisibility(View.GONE);
        lv2.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        editQtyError.setVisibility(View.GONE);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error closing ParcelFile Descriptor");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
