package com.example.android.product_inventory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.product_inventory.Utils.DbHandler;
import com.example.android.product_inventory.Utils.Product;
import com.example.android.product_inventory.Utils.Validation;

import java.io.IOException;

public class AddProduct extends AppCompatActivity {
    DbHandler dbHandler = new DbHandler(this);
    Validation validator = new Validation();
    TextView statusText;
    Uri uri;
    private int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addImageButton = (Button) findViewById(R.id.button_add_image);
        Button saveProductButton = (Button) findViewById(R.id.button_save_product);
        statusText = (TextView) findViewById(R.id.status_message);

        final EditText editProductNameText = (EditText) findViewById(R.id.name_edit);
        final EditText editProductPriceText = (EditText) findViewById(R.id.price_edit);
        final EditText editProductNumberText = (EditText) findViewById(R.id.edit_available);
        final EditText editContactText = (EditText) findViewById(R.id.edit_contact);

        saveProductButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (validateField(editProductNameText, editProductPriceText, editProductNumberText, editContactText)) {
                    if (uri == null) {
                        statusText.setText("Please add image");
                    } else {

                        statusText.setText("");
                        String productName = editProductNameText.getText().toString();
                        String productPrice = editProductPriceText.getText().toString();
                        String productNumber = editProductNumberText.getText().toString();
                        String contactInfo = editContactText.getText().toString();

                        Product product = new Product();
                        product.setName(productName);
                        product.setPrice(Float.parseFloat(productPrice));
                        product.setAvailable(Integer.parseInt(productNumber));
                        product.setImageString(uri.toString());
                        product.setSold(0);
                        product.setSupplier(contactInfo);
                        dbHandler.addProduct(product);

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

        });

        addImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                checkWriteToExternalPerms();
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
            }

        });
    }

    private void checkWriteToExternalPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    private boolean validateField(EditText productName, EditText productPrice, EditText productNumber, EditText contactInfo) {
        if (!validator.isBlank(productName) && !validator.isBlank(productPrice) && !validator.isBlank(productNumber) && !validator.isBlank(contactInfo)) {
            if (!validator.isFloat(productPrice)) {
                statusText.setText("Wrong Price format");
                return false;
            } else {
                if (!validator.isInteger(productNumber)) {
                    statusText.setText("Wrong number format");
                    return false;
                } else {
                    return true;
                }
            }
        }else{
            statusText.setText("Empty field");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            cursor.close();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageBitmap(bitmap);
            } catch (IOException ex) {
                ex.printStackTrace();
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
