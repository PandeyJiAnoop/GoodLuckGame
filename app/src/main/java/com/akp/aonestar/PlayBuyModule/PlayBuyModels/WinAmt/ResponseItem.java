package com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("BitAmount")
	private int bitAmount;

	@SerializedName("Amount")
	private int amount;

	public int getBitAmount(){
		return bitAmount;
	}

	public int getAmount(){
		return amount;
	}
}