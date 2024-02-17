package com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("Entryby")
	private String entryby;

	@SerializedName("PKID")
	private int pKID;

	@SerializedName("EntryDate")
	private String entryDate;

	@SerializedName("CategoryName")
	private String categoryName;

	@SerializedName("CatImg")
	private String catImg;

	public String getEntryby(){
		return entryby;
	}

	public int getPKID(){
		return pKID;
	}

	public String getEntryDate(){
		return entryDate;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public String getCatImg(){
		return catImg;
	}
}