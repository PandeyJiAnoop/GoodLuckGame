package com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponseGetMatchNum{

	@SerializedName("Response")
	private List<ResponseItem> response;

	@SerializedName("Status")
	private boolean status;

	@SerializedName("Message")
	private String message;

	@SerializedName("StatusCode")
	private String statusCode;

	public List<ResponseItem> getResponse(){
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
}