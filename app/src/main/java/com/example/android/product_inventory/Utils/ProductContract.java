package com.example.android.product_inventory.Utils;

import android.provider.BaseColumns;

public class ProductContract {
    // DB Name
    public static final String DATABASE_NAME = "ProductInventory";
    // Version
    public static final int DATABASE_VERSION = 1;

    public class ProductEntry implements BaseColumns {
        // Table
        public static final String TABLE_NAME = "products";
        // Table Columns
        public static final String COLUMN_PRODUCT_ID = "id";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_SOLD = "sold";
        public static final String COLUMN_PRODUCT_AVAILABLE = "available";
        public static final String COLUMN_PRODUCT_IMAGE = "productImage";
        public static final String COLUMN_SUPPLIER = "supplierContact";
    }
}
