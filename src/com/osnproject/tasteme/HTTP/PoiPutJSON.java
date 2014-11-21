package com.osnproject.tasteme.HTTP;

import org.json.JSONObject;

import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.Group;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.Model.User;

import android.util.Log;

public class PoiPutJSON {
	
	private JSONObject json;
	
	public JSONObject getJsonObj(){
		return json;
	}
	
	private void initialize(){
		json = new JSONObject();
	}
	
	public PoiPutJSON(ProductBasicInf product){
		initialize();
		packageProductBasicInf(product);
	}
	
	public PoiPutJSON(User user){
		initialize();
		packageUser(user);
	}
	
	public PoiPutJSON(Group group){
		initialize();
		packageGroup(group);
	}
	
	public PoiPutJSON(Comment comment){
		initialize();
		packageComment(comment);
	}
	
	public PoiPutJSON(ActionLike like){
		initialize();
		packageLike(like);
	}
	
	private void packageLike(ActionLike like){
		try{
			this.json.put(ActionLike.JSON_ACTIONID, like.getActionId());
			this.json.put(ActionLike.JSON_USERID, like.getUserId());
			this.json.put(ActionLike.JSON_PRODUCTID, like.getProductId());
			if(like.getGroupId() == -1)
				this.json.put(ActionLike.JSON_GROUPID, null);
			else
				this.json.put(ActionLike.JSON_GROUPID, like.getGroupId());
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
	}
	
	private void packageComment(Comment comment){
		try{
			this.json.put(Comment.JSON_COMMENTID, comment.getCommentId());
			this.json.put(Comment.JSON_USERID, comment.getUserId());
			this.json.put(Comment.JSON_PRODUCTID, comment.getProductId());
			this.json.put(Comment.JSON_TEXT, comment.getText());
			this.json.put(Comment.JSON_DATE, comment.getDate());
			this.json.put(Comment.JSON_SOCRE, comment.getScore());
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
	}
	
	private void packageGroup(Group group){
		try{
			
			this.json.put(Group.JSON_GROUPID, group.getGroupId());
			this.json.put(Group.JSON_GROUPNAME, group.getGroupName());
			this.json.put(Group.JSON_DESCRIB, group.getDescription());
			this.json.put(Group.JSON_GROUPICON, group.getGroupIcon());
			this.json.put(Group.JSON_CREATORID, group.getCreatorId());
			/*this.json.put(Group.JSON_SOUR, group.getSour());		
			this.json.put(Group.JSON_SWEET, group.getSweet());
			this.json.put(Group.JSON_BITTER, group.getBitter());
			this.json.put(Group.JSON_SPICY, group.getSpicy());
			this.json.put(Group.JSON_SALTY, group.getSalty());
			this.json.put(Group.JSON_MEMBER, group.getMember());*/
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
	}

	private void packageProductBasicInf(ProductBasicInf product){
		try{
			this.json.put(ProductBasicInf.JSON_PRODUCTID, product.getProductId());
			this.json.put(ProductBasicInf.JSON_PRODUCTNAME, product.getProductName());
			this.json.put(ProductBasicInf.JSON_PRICE, product.getPrice());
			this.json.put(ProductBasicInf.JSON_DESCRIPTION, product.getDescription());
			this.json.put(ProductBasicInf.JSON_DETAILS, product.getDetails());
			this.json.put(ProductBasicInf.JSON_SELLER, product.getSeller());
			this.json.put(ProductBasicInf.JSON_CATEGORY, product.getCategory());
			this.json.put(ProductBasicInf.JSON_SUBCATEGORY, product.getSubcategory());
			this.json.put(ProductBasicInf.JSON_BARCODE, product.getBarcode());
			this.json.put(ProductBasicInf.JSON_IMAGE, product.getImageURL());
			this.json.put(ProductBasicInf.JSON_SOUR, product.getSour());
			this.json.put(ProductBasicInf.JSON_SWEET, product.getSweet());
			this.json.put(ProductBasicInf.JSON_BITTER, product.getBitter());
			this.json.put(ProductBasicInf.JSON_SPICY, product.getSpicy());
			this.json.put(ProductBasicInf.JSON_SALTY, product.getSalty());
			this.json.put(ProductBasicInf.JSON_SCORE, product.getScore());
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
	}
	
	private void packageUser(User user){
		try{
			this.json.put(User.JSON_USERID, user.getUserId());
			this.json.put(User.JSON_USERNAME, user.getUserName());
			this.json.put(User.JSON_PASSWORD, user.getPassword());
			this.json.put(User.JSON_SEX, user.getSex());
			if(user.getAge() != -1)
				this.json.put(User.JSON_AGE, user.getAge());
			else
				this.json.put(User.JSON_AGE, null);
			this.json.put(User.JSON_PREFERENCE, user.getPreference());
			this.json.put(User.JSON_DESCRIPTION, user.getDescription());
			this.json.put(User.JSON_IMAGE, user.getImage());
			this.json.put(User.JSON_SOUR, user.getSour());
			this.json.put(User.JSON_SWEET, user.getSweet());
			this.json.put(User.JSON_BITTER, user.getBitter());
			this.json.put(User.JSON_SPICY, user.getSpicy());
			this.json.put(User.JSON_SALTY, user.getSalty());
			this.json.put(User.JSON_PRIORITY, user.getPriority());
			if(user.getGroupId() != -1)
				this.json.put(User.JSON_GROUPID, user.getGroupId());
			else 
				this.json.put(User.JSON_GROUPID, null);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
	}
}
