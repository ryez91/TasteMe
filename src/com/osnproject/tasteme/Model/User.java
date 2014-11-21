package com.osnproject.tasteme.Model;

import android.graphics.Bitmap;

public class User {
	public static final int PRI_ADMIN = 10;
	public static final int PRI_USER = 1;
	
	public static final String JSON_USERID = "UserId";
	public static final String JSON_USERNAME = "UserName";
	public static final String JSON_SEX = "Sex";
	public static final String JSON_AGE = "Age";
	public static final String JSON_PREFERENCE = "Preference";
	public static final String JSON_DESCRIPTION = "Description";
	public static final String JSON_IMAGE = "Image";
	public static final String JSON_SOUR = "Sour";
	public static final String JSON_SWEET = "Sweet";
	public static final String JSON_BITTER = "Bitter";
	public static final String JSON_SPICY = "Spicy";
	public static final String JSON_SALTY = "Salty";
	public static final String JSON_COLLECTION = "Collection";
	public static final String JSON_PRIORITY = "Priority";
	public static final String JSON_GROUPID = "GroupId";
	public static final String JSON_GROUPNAME = "GroupName";
	public static final String JSON_GROUPIMAGE = "GroupImage";
	public static final String JSON_PASSWORD = "Password";
	public static final String JSON_GROUPDESCRIB = "GroupDescribtion";
	
	private int userId;
	private String userName;
	private String password;
	private String sex;
	private int age = -1;
	private String preference;
	private String description;
	private String image;
	private double Sour;
	private double Sweet;
	private double Bitter;
	private double Spicy;
	private double Salty;
	private int priority;
	private int groupId = -1;
	private String groupName;
	private String groupIcon;
	private String groupDescrib;
	
	private Bitmap userImage;
	private Bitmap groupImage;
	
	public void initUser(String username, String password, int sour, int sweet, int bitter, int spicy, int salty){
		userName = username;
		this.password = password;
		Sour = sour;
		Sweet = sweet;
		Bitter = bitter;
		Spicy = spicy;
		Salty = salty;
	}
	
	public double getSour() {
		return Sour;
	}

	public double getSweet() {
		return Sweet;
	}

	public double getBitter() {
		return Bitter;
	}

	public double getSpicy() {
		return Spicy;
	}

	public double getSalty() {
		return Salty;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setSour(double sour) {
		Sour = sour;
	}

	public void setSweet(double sweet) {
		Sweet = sweet;
	}

	public void setBitter(double bitter) {
		Bitter = bitter;
	}

	public void setSpicy(double spicy) {
		Spicy = spicy;
	}

	public void setSalty(double salty) {
		Salty = salty;
	}

	public Bitmap getUserImage() {
		return userImage;
	}

	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}

	public String getGroupDescrib() {
		return groupDescrib;
	}

	public void setGroupDescrib(String groupDescrib) {
		this.groupDescrib = groupDescrib;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	public String getGroupIcon() {
		return groupIcon;
	}
	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}
}
