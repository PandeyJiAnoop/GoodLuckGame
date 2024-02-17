package com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("EntryBy")
	private String entryBy;

	@SerializedName("PKID")
	private int pKID;

	@SerializedName("IsActive")
	private boolean isActive;

	@SerializedName("EntryDate")
	private String entryDate;

	@SerializedName("CategoryName")
	private String categoryName;

	@SerializedName("BannerImg")
	private String bannerImg;

	@SerializedName("CatId")
	private int catId;

	public String getEntryBy(){
		return entryBy;
	}

	public int getPKID(){
		return pKID;
	}

	public boolean isIsActive(){
		return isActive;
	}

	public String getEntryDate(){
		return entryDate;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public String getBannerImg(){
		return bannerImg;
	}

	public int getCatId(){
		return catId;
	}
}