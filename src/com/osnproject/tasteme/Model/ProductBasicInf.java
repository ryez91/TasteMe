package com.osnproject.tasteme.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

public class ProductBasicInf {
	public static final String JSON_PRODUCTID = "ProductId";
	public static final String JSON_PRODUCTNAME = "ProductName";
	public static final String JSON_PRICE = "Price";
	public static final String JSON_DESCRIPTION = "Description";
	public static final String JSON_DETAILS = "Details";
	public static final String JSON_SELLER = "Seller";
	public static final String JSON_CATEGORY = "Category";
	public static final String JSON_SUBCATEGORY = "SubCategory";
	public static final String JSON_IMAGE = "Image1";
	public static final String JSON_BARCODE = "Barcode";
	public static final String JSON_SOUR = "Sour";
	public static final String JSON_SWEET = "Sweet";
	public static final String JSON_BITTER = "Bitter";
	public static final String JSON_SPICY = "Spicy";
	public static final String JSON_SALTY = "Salty";
	public static final String JSON_SCORE = "Score";
	
	private int productId;
	private String productName;
	private double price = -1;
	private String description;
	private String details;
	private String seller;
	private int category = 0;
	private int subCategory = 0;
	private String image;
	private String barcode;
	private Bitmap bitmap = null;
	private double Sour = 0;
	private double Sweet = 0;
	private double Bitter = 0;
	private double Spicy = 0;
	private double Salty = 0;
	private int Score = 0;
	private double RankingScore = 0;
	
	public void setAll(String productName, double price, String description, String details,
			String seller, int category, int subCategory, String barcode){
		this.productName = productName;
		this.price = price;
		this.description = description;
		this.details = details;
		this.seller = seller;
		this.category = category;
		this.subCategory = subCategory;
		this.barcode = barcode;
	}
	

	public double getRankingScore() {
		return RankingScore;
	}


	public void setRankingScore(double rankingScore) {
		RankingScore = rankingScore;
	}


	public double getSour() {
		return Sour;
	}


	public void setSour(double sour) {
		Sour = sour;
	}


	public double getSweet() {
		return Sweet;
	}


	public void setSweet(double sweet) {
		Sweet = sweet;
	}


	public double getBitter() {
		return Bitter;
	}


	public void setBitter(double bitter) {
		Bitter = bitter;
	}


	public double getSpicy() {
		return Spicy;
	}


	public void setSpicy(double spicy) {
		Spicy = spicy;
	}


	public double getSalty() {
		return Salty;
	}


	public void setSalty(double salty) {
		Salty = salty;
	}


	public String getImage() {
		return image;
	}


	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public int getProductId(){
		return productId;
	}
	
	public void setProductId(int productId){
		this.productId = productId;
	}
	
	public String getProductName(){
		return productName;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public double getPrice(){
		return price;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDetails(){
		return details;
	}
	
	public void setDetails(String details){
		this.details = details;
	}
	
	public String getSeller(){
		return seller;
	}
	
	public void setSeller(String seller){
		this.seller = seller;
	}
	
	public int getCategory(){
		return category;
	}
	
	public void setCategory(int category){
		this.category = category;
	}
	
	public int getSubcategory(){
		return subCategory;
	}
	
	public void setSubcategory(int subCategory){
		this.subCategory = subCategory;
	}
	
	public void setImage(String img){
		image = img;
	}
	
	public String getImageURL(){
		return image;
	}
	
	public String getBarcode(){
		return barcode;
	}
	
	public void setBarcode(String barcode){
		this.barcode = barcode;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
}
