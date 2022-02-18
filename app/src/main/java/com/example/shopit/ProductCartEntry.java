package com.example.shopit;

public class ProductCartEntry {

    private String productName;
    private int productImage;
    private int productQuantity;
    private double productPrice;
    private String productDescription;


    public ProductCartEntry(String productName, int productImage, int productQuantity, double productPrice, String productDescription){
        this.productImage = productImage;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public int getProductImage() {
        return productImage;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductQuantity(boolean isAdd) {
        this.productQuantity = (isAdd) ? this.productQuantity+1 : this.productQuantity-1;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }
}
