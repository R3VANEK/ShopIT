package com.example.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<ProductModel> cartProducts = new ArrayList<>();
    CartModel cartModel;
    CustomerModel customerModel;

    TextView totalSumView;
    TextView grettingsView;
    String loggedUserLogin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        customerModel = new CustomerModel(getApplicationContext());
        cartModel = new CartModel(getApplicationContext());




        if(customerModel.validatePreferences()) {
            loggedUserLogin = customerModel.getPreferedLogin();

            //-------------------------------------------
            //tworzenie danych do ListView
            //-------------------------------------------
            cartModel = new CartModel(getApplicationContext());
            try {
                cartProducts = cartModel.getCart(loggedUserLogin);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            grettingsView = findViewById(R.id.textView);
            grettingsView.setText("Koszyk użytkownika " + loggedUserLogin);
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }


        ListView listView = findViewById(R.id.listView);

        CartAdapter cartAdapter = new CartAdapter(getApplicationContext(), R.layout.list_view_items, cartProducts);
        listView.setAdapter(cartAdapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            long viewId = view.getId();

            if(viewId == R.id.deleteCartItemButton){
                try {
                    cartModel.deleteProductFromCartDB(cartProducts.get(position), loggedUserLogin);
                    finish();
                    startActivity(getIntent());

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else{
                Intent i = new Intent(this, ProductDescription.class);
                i.putExtra("productName", cartProducts.get(position).getName());
                i.putExtra("productPrice", cartProducts.get(position).getPrice());
                i.putExtra("productImage", cartProducts.get(position).getImageId());
                i.putExtra("productDescription", cartProducts.get(position).getDescription());
                startActivity(i);
            }

        });


        totalSumView = findViewById(R.id.totalSum);
        float productsTotalPrice = 0;

        for(ProductModel product : cartProducts){
            productsTotalPrice += product.getQuantity() * product.getPrice();
        }

        totalSumView.setText(new DecimalFormat("###.##").format(productsTotalPrice) + " zł");

    }

    public void addToCartLink(android.view.View view){
        Intent i = new Intent(this, AddtoCart.class);
        i.putExtra("login", loggedUserLogin);
        startActivity(i);
    }


    public void transactionSubmitClick(View view) throws SQLException {
        cartModel.closeCart(loggedUserLogin);
        finish();
        startActivity(getIntent());
    }

    public void logOutClick(View view){
        customerModel.deletePreferences();
        finish();
        startActivity(getIntent());
    }



}