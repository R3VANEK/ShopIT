package com.example.shopit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductModel extends Model{

    private Context context;
    public static final String TABLE = "products";
    public static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<String, String>() {{
        put("id", "INTEGER PRIMARY KEY NOT NULL,");
        put("name", "VARCHAR(150) NOT NULL UNIQUE,");
        put("price", "NUMERIC NOT NULL,");
        put("description", "TEXT,");
        put("image_id", "INTEGER,");
    }};

    private String name;
    private int imageId;
    private double price;
    private String description;
    private int quantity;

    public ProductModel(Context context, String name, String description, double price, int imageId){
        this.context = context;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
    }

    public ProductModel(String name, String description, double price, int imageId){
        super();
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
    }

    public ProductModel(Context context, int productIdDB, int quantity){
        this.context = context;

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c1 = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[] {String.valueOf(productIdDB)});
        if (c1.moveToFirst()){
            do {
                this.name = c1.getString(1);
                this.description = c1.getString(3);
                this.imageId = c1.getInt(4);
                this.price = c1.getDouble(2);
                this.quantity = quantity;
            } while(c1.moveToNext());
        }
    }

    public ProductModel(Context applicationContext) {
        this.context = applicationContext;
    }


    @Override
    public void insertToDB() {
        ContentValues cv = new ContentValues();
        cv.put("name", this.name);
        cv.put("description", this.description);
        cv.put("price", this.price);
        cv.put("image_id", this.imageId);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(TABLE, null, cv);
    }

    public int getIdDB(){
        int product_id = -1;
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c1 = db.rawQuery("SELECT id FROM products WHERE name = ?", new String[] {this.name});
        if (c1.moveToFirst()){
            do {
               product_id = c1.getInt(0);
            } while(c1.moveToNext());
        }

        return product_id;
    }


    public ContentValues constructCV(){
        ContentValues cv = new ContentValues();
        cv.put("name", this.name);
        cv.put("price", this.price);
        cv.put("description", this.description);
        cv.put("image_id", this.imageId);

        return cv;
    }

    public static ArrayList<ProductModel> getAllProductsDB(Context context){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<ProductModel> output = new ArrayList<>();
        @SuppressLint("Recycle") Cursor c1 = db.rawQuery("SELECT * FROM products ",null);
        if (c1.moveToFirst()){
            do {
                output.add(new ProductModel(c1.getString(1), c1.getString(3),c1.getDouble(2), c1.getInt(4)));
            } while(c1.moveToNext());
        }
        return output;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }

    public String getName() { return name; }

    public double getPrice() {
        return price;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescription() {
        return description;
    }
}
