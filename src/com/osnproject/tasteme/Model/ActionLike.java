package com.osnproject.tasteme.Model;

import android.graphics.Bitmap;

public class ActionLike {
	public static final String JSON_ACTIONID = "ActionId";
	public static final String JSON_USERID = "UserId";
	public static final String JSON_GROUPID = "GroupId";
	public static final String JSON_PRODUCTID = "ProductId";
	public static final String JSON_PRODUCTIMAGE = "ProductImage";
	public static final String JSON_PRODUCTDESCRIB = "ProductDescription";
	public static final String JSON_PRODUCTNAME = "ProductName";
	
	private int actionId;
	private int userId;
	private int groupId;
	private int productId;
	private String productImage;
	private String productDescription;
	private String productName;
	private Bitmap bitmap;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
