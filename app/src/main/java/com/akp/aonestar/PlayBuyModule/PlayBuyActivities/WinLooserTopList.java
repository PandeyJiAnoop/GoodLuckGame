package com.akp.aonestar.PlayBuyModule.PlayBuyActivities;

import com.akp.aonestar.PlayBuyModule.PlayBuyModels.TossBossMatchAmt.ResponseItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinLooserTopList{

    @SerializedName("Response")
    private List<WinLooserTopList> response;

    @SerializedName("Status")
    private boolean status;

    @SerializedName("Message")
    private String message;

    @SerializedName("StatusCode")
    private String statusCode;


    @SerializedName("UserId")
    private String userid;
    @SerializedName("Img")
    private String img;


    public List<WinLooserTopList> getResponse(){
        return response;
    }

    public boolean isStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getUserid(){
        return userid;
    }
    public String getImg(){
        return img;
    }


    public String getStatusCode(){
        return statusCode;
    }
}