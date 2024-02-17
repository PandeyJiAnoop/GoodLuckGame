package com.akp.aonestar.PriceDropModule.Models;

public class BigDealModel {
    int imageName;
    String productName, productPrice;

    public BigDealModel(int imageName, String productName, String productPrice) {
        this.imageName = imageName;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public int getImageName() {
        return imageName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
