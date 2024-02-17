package com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchHidenum;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("Number")
	private int number;

	public int getNumber(){
		return number;
	}
}