package com.akp.aonestar.PlayBuyModule.PlayBuyModels;

public class ModelProduct {
    int imageName;
    String productName, productPrice;

    public ModelProduct(int imageName, String productName, String productPrice) {
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
