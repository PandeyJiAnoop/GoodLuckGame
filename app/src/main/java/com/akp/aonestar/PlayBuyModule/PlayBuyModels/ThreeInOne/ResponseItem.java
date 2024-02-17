package com.akp.aonestar.PlayBuyModule.PlayBuyModels.ThreeInOne;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("PKID")
	private int pKID;

	@SerializedName("CategoryName")
	private String categoryName;

	@SerializedName("CatImg")
	private String catImg;

	@SerializedName("SR")
	private int sR;

	public int getPKID(){
		return pKID;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public String getCatImg(){
		return catImg;
	}

	public int getSR(){
		return sR;
	}
}