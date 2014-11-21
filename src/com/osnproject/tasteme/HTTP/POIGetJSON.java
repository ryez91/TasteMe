package com.osnproject.tasteme.HTTP;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.Group;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.Model.User;

import android.util.Log;

public class POIGetJSON {
	private JSONArray jsonArray;
	private int type;
	
	public POIGetJSON(){
		
	}
	
	public POIGetJSON(JSONArray jsonArray, int type){
		this.jsonArray = jsonArray;
		this.type = type;
	}
	
	public List<ActionLike> parceLike(){
		List<ActionLike> likes = new ArrayList<ActionLike>();
		for(int i=0; i < jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				poiItem = jsonArray.getJSONObject(i);
				ActionLike like = new ActionLike();
				
				like.setActionId(poiItem.getInt(ActionLike.JSON_ACTIONID));
				like.setUserId(poiItem.getInt(ActionLike.JSON_USERID));
				like.setProductId(poiItem.getInt(ActionLike.JSON_PRODUCTID));
				String str_groupid = poiItem.getString(ActionLike.JSON_GROUPID);
				if(str_groupid.contains("null"))
					like.setGroupId(-1);
				else
					like.setGroupId(Integer.valueOf(str_groupid));
				like.setProductImage(poiItem.getString(ActionLike.JSON_PRODUCTIMAGE));
				like.setProductDescription(poiItem.getString(ActionLike.JSON_PRODUCTDESCRIB));
				like.setProductName(poiItem.getString(ActionLike.JSON_PRODUCTNAME));
					
				likes.add(like);
			} catch (JSONException e){
				Log.e("EmergencyResponderHttpGetParseError", e.toString());
				return null;
			}
		}
		
		return likes;
	}
	
	public List<Comment> parceComment(){
		List<Comment> comments = new ArrayList<Comment>();
		for(int i=0; i < jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				poiItem = jsonArray.getJSONObject(i);
				Comment comment = new Comment();
				
				comment.setCommentId(poiItem.getInt(Comment.JSON_COMMENTID));
				comment.setUserId(poiItem.getInt(Comment.JSON_USERID));
				comment.setProductId(poiItem.getInt(Comment.JSON_PRODUCTID));
				comment.setText(poiItem.getString(Comment.JSON_TEXT));
				comment.setDate(poiItem.getString(Comment.JSON_DATE));
				comment.setUserName(poiItem.getString(Comment.JSON_USERNAME));
				comment.setProductName(poiItem.getString(Comment.JSON_PRODUCTNAME));
				comment.setUserImage(poiItem.getString(Comment.JSON_USERIMAGE));
				comment.setScore(poiItem.getInt(Comment.JSON_SOCRE));
				comment.setProductImage(poiItem.getString(Comment.JSON_PRODUCTIMAGE));
					
				comments.add(comment);
			} catch (JSONException e){
				Log.e("EmergencyResponderHttpGetParseError", e.toString());
				return null;
			}
		}
		
		return comments;
	}
	
	public List<Group> parceGroup(){
		List<Group> groups = new ArrayList<Group>();
		for(int i=0; i < jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				
				poiItem = jsonArray.getJSONObject(i);
				Group group = new Group();
				group.setGroupId(poiItem.getInt(Group.JSON_GROUPID));
				group.setGroupName(poiItem.getString(Group.JSON_GROUPNAME));
				group.setDescription(poiItem.getString(Group.JSON_DESCRIB));
				group.setGroupIcon(poiItem.getString(Group.JSON_GROUPICON));
				group.setSour(poiItem.getInt(Group.JSON_SOUR));
				group.setSweet(poiItem.getInt(Group.JSON_SWEET));
				group.setBitter(poiItem.getInt(Group.JSON_BITTER));
				group.setSpicy(poiItem.getInt(Group.JSON_SPICY));
				group.setSalty(poiItem.getInt(Group.JSON_SALTY));
				group.setMember(poiItem.getInt(Group.JSON_MEMBER));
				group.setCreatorId(poiItem.getInt(Group.JSON_CREATORID));
				group.setCreatorName(poiItem.getString(Group.JSON_CREATORNAME));
				
				groups.add(group);
			} catch (JSONException e){
				Log.e("EmergencyResponderHttpGetParseError", e.toString());
				return null;
			}
		}
		
		return groups;
	}
	
	public List<ProductBasicInf> parseProduct(){
		List<ProductBasicInf> products = new ArrayList<ProductBasicInf>();
		for(int i=0; i < jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				
				poiItem = jsonArray.getJSONObject(i);
				ProductBasicInf product = new ProductBasicInf();
				product.setProductId(poiItem.getInt(ProductBasicInf.JSON_PRODUCTID));
				product.setProductName(poiItem.getString(ProductBasicInf.JSON_PRODUCTNAME));
				product.setDescription(poiItem.getString(ProductBasicInf.JSON_DESCRIPTION));
				
				String str_Price = poiItem.getString(ProductBasicInf.JSON_PRICE);
				if(str_Price.contains("null"))
					product.setPrice(-1);
				else
					product.setPrice(Double.valueOf(str_Price));
				
				product.setDetails(poiItem.getString(ProductBasicInf.JSON_DETAILS));
				product.setSeller(poiItem.getString(ProductBasicInf.JSON_SELLER));
				product.setCategory(poiItem.getInt(ProductBasicInf.JSON_CATEGORY));
				product.setSubcategory(poiItem.getInt(ProductBasicInf.JSON_SUBCATEGORY));
				product.setBarcode(poiItem.getString(ProductBasicInf.JSON_BARCODE));
				product.setImage(poiItem.getString(ProductBasicInf.JSON_IMAGE));
				product.setSour(poiItem.getDouble(ProductBasicInf.JSON_SOUR));
				product.setSweet(poiItem.getDouble(ProductBasicInf.JSON_SWEET));
				product.setBitter(poiItem.getDouble(ProductBasicInf.JSON_BITTER));
				product.setSpicy(poiItem.getDouble(ProductBasicInf.JSON_SPICY));
				product.setSalty(poiItem.getDouble(ProductBasicInf.JSON_SALTY));
				product.setScore(poiItem.getInt(ProductBasicInf.JSON_SCORE));
				products.add(product);
				
			} catch (JSONException e){
				Log.e("EmergencyResponderHttpGetParseError", e.toString());
				return null;
			}
		}
		
		return products;
	}
	
	public List<User> parceUser(){
		List<User> users = new ArrayList<User>();
		for(int i=0; i< jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				
				poiItem = jsonArray.getJSONObject(i);
				User user = new User();
				user.setUserId(poiItem.getInt(User.JSON_USERID));
				user.setUserName(poiItem.getString(User.JSON_USERNAME));
				user.setSex(poiItem.getString(User.JSON_SEX));
				
				String age = poiItem.getString(User.JSON_AGE);
				if(!age.contains("null"))
					user.setAge(Integer.valueOf(age));
				else
					user.setAge(-1);
				
				user.setPreference(poiItem.getString(User.JSON_PREFERENCE));
				user.setDescription(poiItem.getString(User.JSON_DESCRIPTION));
				user.setImage(poiItem.getString(User.JSON_IMAGE));
				user.setSour(poiItem.getDouble(User.JSON_SOUR));
				user.setSweet(poiItem.getDouble(User.JSON_SWEET));
				user.setBitter(poiItem.getDouble(User.JSON_BITTER));
				user.setSpicy(poiItem.getDouble(User.JSON_SPICY));
				user.setSalty(poiItem.getDouble(User.JSON_SALTY));
				user.setPriority(poiItem.getInt(User.JSON_PRIORITY));
				
				String groupid = poiItem.getString(User.JSON_GROUPID);
				if(!groupid.contains("null"))
					user.setGroupId(Integer.valueOf(groupid));
				else
					user.setGroupId(-1);
				
				user.setGroupName(poiItem.getString(User.JSON_GROUPNAME));
				user.setGroupIcon(poiItem.getString(User.JSON_GROUPIMAGE));
				user.setGroupDescrib(poiItem.getString(User.JSON_GROUPDESCRIB));
				
				users.add(user);
			} catch (JSONException e){
				Log.e("EmergencyResponderHttpGetParseError", e.toString());
				return null;
			}
		}
		
		return users;
	}
	

}
