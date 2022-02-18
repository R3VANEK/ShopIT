package com.example.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDescription extends AppCompatActivity {

    String productName = "";
    String productDescription = "";
    double productPrice = 0.0;
    int productImageId = -1;

    ImageView ImageView;
    TextView NameView;
    TextView PriceView;
    TextView DescriptionView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            productName = extras.getString("productName");
            productDescription = extras.getString("productDescription");
            productPrice = extras.getDouble("productPrice");
            productImageId = extras.getInt("productImage");
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
        }

        NameView = findViewById(R.id.title);
        PriceView = findViewById(R.id.price);
        DescriptionView = findViewById(R.id.description);
        ImageView = findViewById(R.id.imageView);

        NameView.setText(productName);
        PriceView.setText(productPrice + "zł/szt");
        ImageView.setImageResource(productImageId);
        DescriptionView.setText(productDescription);


    }




    public void backToCart(android.view.View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}