package com.example.shopit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddtoCart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    ProductModel selectedProduct;
    CartModel cartModel;
    ArrayList<ProductModel> allproductArray = new ArrayList<>();
    String loggedUserLogin = "";

    TextView ProductPriceView;
    EditText QuantityView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        cartModel = new CartModel(getApplicationContext());


        Bundle extras = getIntent().getExtras();
        if (!(extras.getString("login", "").equals("")))
            loggedUserLogin = extras.getString("login","");

        if(loggedUserLogin.equals(""))
            Toast.makeText(getApplicationContext(), "nie otrzymano loginu użytkownika", Toast.LENGTH_LONG).show();


        ProductPriceView = findViewById(R.id.spinnerProductPrice);
        QuantityView = findViewById(R.id.editText);

        selectedProduct = new ProductModel(getApplicationContext());
        allproductArray = ProductModel.getAllProductsDB(getApplicationContext());
        ArrayList<String> productNamesArray = new ArrayList<String>();

        for(ProductModel product : allproductArray)
            productNamesArray.add(product.getName());



        Spinner spinner = findViewById(R.id.product_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productNamesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ProductPriceView.setText(allproductArray.get(position).getPrice() +"zł/szt");
        selectedProduct = allproductArray.get(position);
        selectedProduct.setContext(getApplicationContext());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addToCartClick(View view){
        int selectedQuantity = Integer.parseInt(QuantityView.getText().toString());

        if(selectedQuantity <= 0){
            Toast.makeText(getApplicationContext(), "Podano nieprawidłową ilość produktu", Toast.LENGTH_LONG).show();
            return;
        }

        selectedProduct.setQuantity(selectedQuantity);
        //tutaj wstawienie do tabeli zamówenia
        try{

            cartModel.addProductToCartDB(getApplicationContext(), selectedProduct, loggedUserLogin);


            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }
        catch (SQLException e){
            Toast.makeText(getApplicationContext(), "coś nie zadziałało przy dodawaniu do koszyka", Toast.LENGTH_LONG).show();
        }





    }
}