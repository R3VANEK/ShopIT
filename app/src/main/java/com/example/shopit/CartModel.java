package com.example.shopit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartModel extends Model{

    public static final String TABLE = "carts";
    public static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<String, String>() {{
        put("id", "INTEGER PRIMARY KEY NOT NULL,");
        put("customer_id", "INTEGER NOT NULL,");
//        put("FOREIGN KEY(customer_id) REFERENCES customers(id)",",");
        put("is_sent", "INTEGER NOT NULL DEFAULT 0,");
        put("sumTotal", "NUMERIC NOT NULL,");
    }};

    private int customerId;
    private boolean isSent = false;
    private double sumTotal = -1;



    private Context context;
    ArrayList<ProductModel> cartElements;


    public CartModel(Context context){
        this.context = context;
        cartElements = new ArrayList<>();

    }

    private int getUserId(String login) throws SQLException {
        SQLiteDatabase db = new MyDatabaseHelper(this.context).getReadableDatabase();
        int id = -1;
        Cursor c1 = db.rawQuery("SELECT id FROM customers WHERE login = ?", new String[] {login});
        if (c1.moveToFirst()){
            do {
                id = c1.getInt(0);
            } while(c1.moveToNext());
        }
        c1.close();
        db.close();

        if(id == -1)
            throw new SQLException("Nie udało się zrobić selecta");

        return id;

    }

    public int getCartIdDB(String userLogin) throws SQLException {
        int user_id = this.getUserId(userLogin);
        int cartId = -1;
        SQLiteDatabase db1 = new MyDatabaseHelper(this.context).getReadableDatabase();
        Cursor c = db1.rawQuery("SELECT id FROM carts WHERE customer_id = ? AND is_sent = 0", new String[] {String.valueOf(user_id)});
        if (c.moveToFirst()){
            do {
                cartId = c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        db1.close();

        return cartId;
    }

    public ArrayList<ProductModel> getCart(String userLogin) throws SQLException {

        int id = this.getUserId(userLogin);
        int numberOfEmptyCarts = 0;

        SQLiteDatabase db1 = new MyDatabaseHelper(this.context).getReadableDatabase();
        ArrayList<ProductModel> cartElements = new ArrayList<>();

        Cursor c = db1.rawQuery("SELECT id FROM carts WHERE customer_id = ? AND is_sent = 0", new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            do {
                // zabezpieczenie dodatkowe
                if(numberOfEmptyCarts > 1)
                    break;

                CartRelationModel cartRelationModel = new CartRelationModel(this.context);
                cartElements = cartRelationModel.getCartItemsDB(c.getInt(0));
                numberOfEmptyCarts+=1;
            } while(c.moveToNext());
        }
        c.close();
        db1.close();

        if(cartElements.size() == 0 && numberOfEmptyCarts == 0){
            createEmptyCart(id);
        }

        this.cartElements = cartElements;
        return cartElements;
    }


    public void createEmptyCart(int user_id){
        SQLiteDatabase db1 = new MyDatabaseHelper(this.context).getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("customer_id", user_id);
        cv1.put("is_sent", 0);
        cv1.put("sumTotal", 0);

        db1.insert(CartModel.TABLE, null, cv1);
        db1.close();

    }


    public void deleteProductFromCartDB(ProductModel productModel, String login) throws SQLException {
        int product_id = productModel.getIdDB();
        int cart_id = this.getCartIdDB(login);
        SQLiteDatabase db1 = new MyDatabaseHelper(context).getWritableDatabase();

        db1.delete(CartRelationModel.TABLE, "cart_id=? and product_id=?", new String[]{String.valueOf(cart_id), String.valueOf(product_id)});
    }


    public void addProductToCartDB(Context context, ProductModel productModel, String login) throws SQLException {

        int product_id = productModel.getIdDB();
        int cart_id = this.getCartIdDB(login);

        SQLiteDatabase db1 = new MyDatabaseHelper(context).getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("product_id", product_id);
        cv1.put("quantity", productModel.getQuantity());
        cv1.put("cart_id", cart_id);

        db1.insert(CartRelationModel.TABLE, null, cv1);
        db1.close();

    }

    public void closeCart(String login) throws SQLException {

        int id = this.getUserId(login);
        SQLiteDatabase db1 = new MyDatabaseHelper(context).getWritableDatabase();
        String strSQL = "UPDATE "+TABLE+" SET is_sent = 1 WHERE customer_id = "+ id;
        db1.execSQL(strSQL);
        db1.close();
    }


    //wywoływana przy zamknięciu aplikacji w celu zapisania zmian
    public void insertToDB() {

    }
}
