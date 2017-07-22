package com.example.android.product_inventory.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.product_inventory.Utils.ProductContract.DATABASE_NAME;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, ProductContract.DATABASE_VERSION);
        mContext = context;
    }
    private Context mContext;

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String STRING_INT = " INTEGER";
        final String STRING_FLOAT = " REAL";
        final String STRING_TEXT = " TEXT";
        final String STRING_COMMA = ",";
        String CREATE_TABLE = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + "("
                + ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY,"
                + ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + STRING_TEXT + STRING_COMMA
                + ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE + STRING_TEXT + STRING_COMMA
                + ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE + STRING_FLOAT + STRING_COMMA
                + ProductContract.ProductEntry.COLUMN_PRODUCT_AVAILABLE + STRING_INT + STRING_COMMA
                + ProductContract.ProductEntry.COLUMN_PRODUCT_SOLD + STRING_INT + STRING_COMMA
                + ProductContract.ProductEntry.COLUMN_SUPPLIER + STRING_TEXT + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME);
        onCreate(db);
    }

    // Add New Product
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, product.getImageString());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_AVAILABLE, product.getAvailable());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SOLD, product.getSold());
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, product.getSupplier());

        db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get Product
    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME,
                new String[]{
                        ProductContract.ProductEntry.COLUMN_PRODUCT_ID,
                        ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                        ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                        ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                        ProductContract.ProductEntry.COLUMN_PRODUCT_AVAILABLE,
                        ProductContract.ProductEntry.COLUMN_PRODUCT_SOLD,
                        ProductContract.ProductEntry.COLUMN_SUPPLIER
                },
                ProductContract.ProductEntry.COLUMN_PRODUCT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    // Get All Products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        String sqlSelect = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlSelect, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setImageString(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setAvailable(Integer.parseInt(cursor.getString(4)));
                product.setSold(Integer.parseInt(cursor.getString(5)));
                product.setSupplier(cursor.getString(6));
                products.add(product);
            } while (cursor.moveToNext());
        }
        return products;
    }

    // Getting products Count
    public int getProductsCount() {
        String sqlCount = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlCount, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // UPDATE product
    public int updateProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_AVAILABLE, product.getAvailable());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SOLD, product.getSold());
        return db.update(ProductContract.ProductEntry.TABLE_NAME, values, ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
    }

    //DELETE all
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductContract.ProductEntry.TABLE_NAME, null, null);
        db.execSQL("DELETE  FROM " + ProductContract.ProductEntry.TABLE_NAME);
        db.close();
    }
    // DELETE product
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductContract.ProductEntry.TABLE_NAME, ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    //DELETE dbHandler
    public void deleteDatabase() {

        mContext.deleteDatabase(DATABASE_NAME);
    }
}
