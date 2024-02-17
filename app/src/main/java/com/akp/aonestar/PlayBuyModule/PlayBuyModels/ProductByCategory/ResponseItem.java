package com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory;

import com.google.gson.annotations.SerializedName;

public class ResponseItem{
	private boolean isClicked;

/*	@SerializedName("ProductName")
	private String ProductName;
	@SerializedName("ProductSaleRate")
	private String ProductSaleRate;
	@SerializedName("ProductCode")
	private String ProductCode;
	@SerializedName("ProductImg")
	private String ProductImg;*/




	@SerializedName("CategoryId")
	private int categoryId;

	@SerializedName("ProductSaleRate")
	private double productSaleRate;

	@SerializedName("Description")
	private String description;

	@SerializedName("ProductName")
	private String productName;

	@SerializedName("ProductCode")
	private int productCode;

	@SerializedName("DisPer")
	private double disPer;

	@SerializedName("ProductId")
	private String productId;

	@SerializedName("ProductMRP")
	private double productMRP;

	@SerializedName("EntryBy")
	private String entryBy;

	@SerializedName("PKID")
	private int pKID;

	@SerializedName("EntryDate")
	private String entryDate;

	@SerializedName("CategoryName")
	private String categoryName;

	@SerializedName("ProductImg")
	private String productImg;

	@SerializedName("DisAmt")
	private double disAmt;

	@SerializedName("Points")
	private String Points;
	public String getPoints(){
		return Points;
	}


	@SerializedName("Number")
	private String Number;
	public String getNumber(){
		return Number;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public double getProductSaleRate(){
		return productSaleRate;
	}

	public String getDescription(){
		return description;
	}

	public String getProductName(){
		return productName;
	}

	public int getProductCode(){
		return productCode;
	}

	public double getDisPer(){
		return disPer;
	}

	public String getProductId(){
		return productId;
	}

	public double getProductMRP(){
		return productMRP;
	}

	public String getEntryBy(){
		return entryBy;
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

	public String getProductImg(){
		return productImg;
	}

	public double getDisAmt(){
		return disAmt;
	}
	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean clicked) {
		isClicked = clicked;
	}


//ye comment kiye hai 15-12-2023

//	public String getPrimeProductName(){
//		return ProductName;
//	}
//	public String getPrimeProductSaleRate(){
//		return ProductSaleRate;
//	}
//	public String getPrimeProductCode(){
//		return ProductCode;
//	}
//
//	public String getPrimeProductImg(){
//		return ProductImg;
//	}
}