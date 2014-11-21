package com.osnproject.tasteme.Model;

import android.graphics.Bitmap;

public class Group {
	public static final String JSON_GROUPID = "GroupId";
	public static final String JSON_GROUPNAME = "GroupName";
	public static final String JSON_DESCRIB = "Description";
	public static final String JSON_GROUPICON = "GroupIcon";
	public static final String JSON_SOUR = "Sour";
	public static final String JSON_SWEET = "Sweet";
	public static final String JSON_BITTER = "Bitter";
	public static final String JSON_SPICY = "Spicy";
	public static final String JSON_SALTY = "Salty";
	public static final String JSON_CREATORID = "CreatorId";
	public static final String JSON_MEMBER = "Member";
	public static final String JSON_CREATORNAME = "CreatorName";
	
	private int groupId;
	private String groupName;
	private String description;
	private String groupIcon;
	private int sour;
	private int sweet;
	private int bitter;
	private int spicy;
	private int salty;
	private int creatorId;
	private int member;
	private String creatorName;
	private Bitmap image;
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGroupIcon() {
		return groupIcon;
	}
	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}
	public int getSour() {
		return sour;
	}
	public void setSour(int sour) {
		this.sour = sour;
	}
	public int getSweet() {
		return sweet;
	}
	public void setSweet(int sweet) {
		this.sweet = sweet;
	}
	public int getBitter() {
		return bitter;
	}
	public void setBitter(int bitter) {
		this.bitter = bitter;
	}
	public int getSpicy() {
		return spicy;
	}
	public void setSpicy(int spicy) {
		this.spicy = spicy;
	}
	public int getSalty() {
		return salty;
	}
	public void setSalty(int salty) {
		this.salty = salty;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public int getMember() {
		return member;
	}
	public void setMember(int member) {
		this.member = member;
	}
}
