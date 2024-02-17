package com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("PKID")
	private int pKID;

	@SerializedName("Number")
	private int number;

	@SerializedName("Entrydate")
	private String entrydate;

	@SerializedName("IsActive")
	private boolean isActive;

	public int getPKID(){
		return pKID;
	}

	public int getNumber(){
		return number;
	}

	public String getEntrydate(){
		return entrydate;
	}

	public boolean isIsActive(){
		return isActive;
	}
}