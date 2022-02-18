package com.example.shopit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CartRelationModel extends Model{

    public static final String TABLE = "cartRelation";
    public static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<String, String>() {{

        put("id", "INTEGER PRIMARY KEY NOT NULL,");
        put("cart_id", "INTEGER NOT NULL,");
        put("product_id", "INTEGER NOT NULL,");
        put("quantity", "NUMERIC NOT NULL,");
//        put("FOREIGN KEY(customer_id) REFERENCES customers(id)",",");
//        put("FOREIGN KEY(product_id) REFERENCES products(id)",",");

    }};

    private Context context;

    public CartRelationModel(Context context){
        this.context = context;
    }

    @Override
    void insertToDB() throws SQLException {

    }

    public ArrayList<ProductModel> getCartItemsDB(int cartId){
        SQLiteDatabase db = new MyDatabaseHelper(context).getReadableDatabase();
        ArrayList<ProductModel> cartElements = new ArrayList<>();

        Cursor c1 = db.rawQuery("SELECT product_id, quantity FROM cartRelation WHERE cart_id = ?", new String[] {String.valueOf(cartId)});
        if (c1.moveToFirst()){
            do {
                cartElements.add(new ProductModel(this.context, c1.getInt(0), c1.getInt(1)));
            } while(c1.moveToNext());
        }

        db.close();
        return cartElements;
    }


}
