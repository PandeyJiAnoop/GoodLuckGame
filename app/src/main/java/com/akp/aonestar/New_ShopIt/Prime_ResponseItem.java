package com.akp.aonestar.New_ShopIt;

import com.google.gson.annotations.SerializedName;

public class Prime_ResponseItem {
    private boolean isClicked;

    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("ProductSaleRate")
    private String ProductSaleRate;
    @SerializedName("ProductCode")
    private String ProductCode;
    @SerializedName("ProductImg")
    private String ProductImg;

    @SerializedName("IsPlan")
    private String IsPlan;

    public String getPrimeProductName(){
        return ProductName;
    }
    public String getPrimeProductSaleRate(){
        return ProductSaleRate;
    }
    public String getPrimeProductCode(){
        return ProductCode;
    }

    public String getPrimeProductImg(){
        return ProductImg;
    }

    public String getIsPlan(){
        return IsPlan;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}