package com.example.shopit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<ProductModel> {

    ArrayList<ProductModel> products;
    Context context;
    int mResource;



    public CartAdapter(@NonNull Context context, int resource, ArrayList<ProductModel> products) {
        super(context, resource, products);
        this.products = products;
        this.mResource = resource;
        this.context = context;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       LayoutInflater layoutInflater = LayoutInflater.from(this.context);
       convertView = layoutInflater.inflate(R.layout.list_view_items,parent,false);
       TextView NameView = convertView.findViewById(R.id.productName);
       TextView QuantityView = convertView.findViewById(R.id.productQuantity);
       TextView PriceView = convertView.findViewById(R.id.productPrice);
       ImageView ImgView = convertView.findViewById(R.id.productImage);
       Button deleteCartItemButton = convertView.findViewById(R.id.deleteCartItemButton);

       NameView.setText(products.get(position).getName());
       PriceView.setText(products.get(position).getPrice() + "zł/szt");
       QuantityView.setText(products.get(position).getQuantity() + " sztuk");
       ImgView.setImageResource(products.get(position).getImageId());

       setClickListener(deleteCartItemButton, position, parent);
       setClickListener(ImgView, position, parent);
       setClickListener(PriceView, position, parent);
       setClickListener(NameView, position, parent);
        setClickListener(QuantityView, position, parent);

       return convertView;


    }

    private void setClickListener(View view, final int position, final ViewGroup parent){
        view.setOnClickListener(v -> {
            ((ListView) parent).performItemClick(v, position, 0);
        });
    }
}
