package com.osnproject.tasteme.Model;

import android.graphics.Bitmap;

public class Comment {
	private int commentId;
	private int userId;
	private int productId;
	private String text;
	private String date;
	private String userName;
	private String productName;
	private String userImage;
	private String productImage;
	private int score;
	private Bitmap bitmap;
	
	public static final String JSON_COMMENTID = "CommentId";
	public static final String JSON_USERID = "UserId";
	public static final String JSON_PRODUCTID = "ProductId";
	public static final String JSON_TEXT = "Text";
	public static final String JSON_DATE = "Date";
	public static final String JSON_USERNAME = "UserName";
	public static final String JSON_PRODUCTNAME = "ProductName";
	public static final String JSON_USERIMAGE = "UserImage";
	public static final String JSON_PRODUCTIMAGE = "ProductImage";
	public static final String JSON_SOCRE = "Score";
	
	public static final int IMAGEFILL_USER = 1;
	public static final int IMAGEFILL_PRODUCT = 2;
	
	public void packageComment(int userid, int productid, String text, String date, int score){
		this.userId = userid;
		this.productId = productid;
		this.text = text;
		this.date = date;
		this.score = score;
	}
	
	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
}
