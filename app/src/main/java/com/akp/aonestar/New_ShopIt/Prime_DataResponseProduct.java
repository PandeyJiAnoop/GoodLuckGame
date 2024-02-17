package com.akp.aonestar.New_ShopIt;

import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prime_DataResponseProduct {

    @SerializedName("Response")
    private List<Prime_ResponseItem> response;

    @SerializedName("Status")
    private boolean status;

    @SerializedName("Message")
    private String message;

    @SerializedName("StatusCode")
    private String statusCode;

    @SerializedName("IsPlan")
    private String IsPlan;


    public List<Prime_ResponseItem> getResponse(){
        return response;
    }

    public boolean isStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getStatusCode(){
        return statusCode;
    }

    public String getIsPlan(){
        return IsPlan;
    }
}