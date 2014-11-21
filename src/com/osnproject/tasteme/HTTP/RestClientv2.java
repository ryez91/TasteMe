package com.osnproject.tasteme.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.Model.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Log;

public class RestClientv2 {
	
	//private final String URL_PRE = "http://s320880.vicp.net:4000/api/";
	private final String URL_PRE = "http://24.91.28.7:4000/api/";
	private final String URL_IMAGE = "http://24.91.28.7:4001/";
	
	private final String URL_PRODUCT = "products/";
	private final String URL_USER = "user/";
	private final String URL_GROUP = "group/";
	private final String URL_COMMENT = "comment/";
	private final String URL_ACTION = "action/";
	
	public final int HTTP_RESPONSE_CODE_CREATED = 201;
	public final int HTTP_RESPONSE_CODE_OK = 200;
	
	
	/************  get  ****************/
	
	public List<ProductBasicInf> getAllProduct(){
		String url = URL_PRE + URL_PRODUCT;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	}
	
	public List<ProductBasicInf> getProductByBarcode(String barcode){
		String url = URL_PRE + URL_PRODUCT + "?barcode=" + barcode;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	}
	
	public List<ProductBasicInf> getProductById(int Id){
		String url = URL_PRE + URL_PRODUCT + "?id=" + Integer.toString(Id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	}
	
	public List<ProductBasicInf> getProductByName(String name){
		String url = URL_PRE + URL_PRODUCT + "?name=" + name;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	}
	
	public List<ProductBasicInf> getProductByCategory(int category, int index, int size){
		String url = URL_PRE + URL_PRODUCT + "?category=" + Integer.toString(category);
		url += "&index=" + Integer.toString(index) + "&size=" + Integer.toString(size);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	}
	
	public List<ProductBasicInf> getProductBySubCategory(int category, int subcategory, int index, int size){
		String url = URL_PRE + URL_PRODUCT + "?category=" + Integer.toString(category) + "&subcategory=" + Integer.toString(subcategory);
		url += "&index=" + Integer.toString(index) + "&size=" + Integer.toString(size);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ProductBasicInf> products = getJson.parseProduct();
		return products;
	} 
	
	public List<User> getUserByNamePWD(String name, String password){
		String url = URL_PRE + URL_USER + "?name=" + name + "&password=" + password;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<User> users = getJson.parceUser();
		return users;
	}
	
	public List<User> getUserById(int id){
		String url = URL_PRE + URL_USER + "?id=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<User> users = getJson.parceUser();
		return users;
	}
	
	public List<com.osnproject.tasteme.Model.Group> getAllGroup(){
		String url = URL_PRE + URL_GROUP;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<com.osnproject.tasteme.Model.Group> groups = getJson.parceGroup();
		return groups;
	}
	
	public List<com.osnproject.tasteme.Model.Group> getGroupById(int id){
		String url = URL_PRE + URL_GROUP + "?id=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<com.osnproject.tasteme.Model.Group> groups = getJson.parceGroup();
		return groups;
	}
	
	public List<com.osnproject.tasteme.Model.Group> getGroupByName(String name){
		String url = URL_PRE + URL_GROUP + "?name=" + name;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<com.osnproject.tasteme.Model.Group> groups = getJson.parceGroup();
		return groups;
	}
	
	public List<com.osnproject.tasteme.Model.Group> getGroupByCreator(int id){
		String url = URL_PRE + URL_GROUP + "?creatorid=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<com.osnproject.tasteme.Model.Group> groups = getJson.parceGroup();
		return groups;
	}
	
	public List<Comment> getCommentById(int id){
		String url = URL_PRE + URL_COMMENT + "?id=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<Comment> getCommentByUser(int id){
		String url = URL_PRE + URL_COMMENT + "?userid=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<Comment> getCommentByProduct(int id){
		String url = URL_PRE + URL_COMMENT + "?productid=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<Comment> getCommentByGroup(int id){
		String url = URL_PRE + URL_COMMENT + "?groupid=" + Integer.toString(id);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<Comment> getCommentByGroup(int id, int size){
		String url = URL_PRE + URL_COMMENT + "?groupid=" + Integer.toString(id) + "&size=" + Integer.toString(size);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<Comment> getCommentByUserProduct(int userid, int productid){
		String url = URL_PRE + URL_COMMENT + "?userid=" + Integer.toString(userid)+ "&productid=" + Integer.toString(productid);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<Comment> comments = getJson.parceComment();
		return comments;
	}
	
	public List<ActionLike> getAllLike(){
		String url = URL_PRE + URL_ACTION;
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ActionLike> likes = getJson.parceLike();
		return likes;
	}
	
	public List<ActionLike> getLikeByUser(int userid){
		String url = URL_PRE + URL_ACTION + "?userid=" + Integer.toString(userid);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ActionLike> likes = getJson.parceLike();
		return likes;
	}
	
	public List<ActionLike> getLikeByGroup(int groupid){
		String url = URL_PRE + URL_ACTION + "?groupid=" + Integer.toString(groupid);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ActionLike> likes = getJson.parceLike();
		return likes;
	}
	
	public List<ActionLike> getLikeByProduct(int productid){
		String url = URL_PRE + URL_ACTION + "?productid=" + Integer.toString(productid);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ActionLike> likes = getJson.parceLike();
		return likes;
	}
	
	public List<ActionLike> getLikeByUserProduct(int userid, int productid){
		String url = URL_PRE + URL_ACTION + "?productid=" + Integer.toString(productid) + "&userid=" + Integer.toString(userid);
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		POIGetJSON getJson = new POIGetJSON(jsonArray, 0);
		List<ActionLike> likes = getJson.parceLike();
		return likes;
	}
	
	public void fillBitmapLikes(List<ActionLike> likes){
		for(int i=0;i<likes.size();i++){
			ActionLike like = likes.get(i);
			if(like.getProductImage()!=null)
				like.setBitmap(getImageforLike(like));
		}
	}
	
	public void fillBitmapComments(List<Comment> comments, int imagefill){
		
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			if((imagefill == Comment.IMAGEFILL_USER && comment.getUserImage()!=null)||(imagefill == Comment.IMAGEFILL_PRODUCT && comment.getProductImage()!=null))
				comment.setBitmap(getImageforComment(comment, imagefill));
		}
	}

	public void fillBitmapProducts(List<ProductBasicInf> products){
		for(int i=0;i<products.size();i++){
			ProductBasicInf product = products.get(i);
			if(product.getImageURL()!=null)
				product.setBitmap(getImageforProduct(product));
		}
	}
	
	public void fillBitmapUsers(List<User> users){
		for(int i=0;i<users.size();i++){
			User user = users.get(i);
			if(user.getImage()!=null)
				user.setUserImage(getImageforUser(user));
		}
	}
	
	public void fillBitmapGroups(List<com.osnproject.tasteme.Model.Group> groups){
		for(int i=0;i<groups.size();i++){
			com.osnproject.tasteme.Model.Group group = groups.get(i);
			if(group.getGroupIcon()!=null)
				group.setImage(getImageforGroup(group));
		}
	}
	
	/**********  send  ****************/
	public void createNewProduct(ProductBasicInf product){
		String url = URL_PRE + URL_PRODUCT;
		PoiPutJSON poiJson = new PoiPutJSON(product);
		submitHttpPost(url, poiJson);
	}
	
	public void createNewUser(User user){
		String url = URL_PRE + URL_USER;
		PoiPutJSON poiJson = new PoiPutJSON(user);
		submitHttpPost(url, poiJson);
	}
	
	public void createNewGroup(com.osnproject.tasteme.Model.Group group){
		String url = URL_PRE + URL_GROUP;
		PoiPutJSON poiJson = new PoiPutJSON(group);
		submitHttpPost(url, poiJson);
	}
	
	public void createNewComment(Comment comment){
		String url = URL_PRE + URL_COMMENT;
		PoiPutJSON poiJson = new PoiPutJSON(comment);
		submitHttpPost(url, poiJson);
	}
	
	public void createNewLike(ActionLike like){
		String url = URL_PRE + URL_ACTION;
		PoiPutJSON poiJson = new PoiPutJSON(like);
		submitHttpPost(url, poiJson);
	}
	
	
	/**********  update  *******************/
	public void updateUser(User user){
		String url = URL_PRE + URL_USER;
		PoiPutJSON poiJson = new PoiPutJSON(user);
		submitHttpPut(url, poiJson);
	}
	
	public void updateProduct(ProductBasicInf product){
		String url = URL_PRE + URL_PRODUCT;
		PoiPutJSON poiJson = new PoiPutJSON(product);
		submitHttpPut(url, poiJson);
	}
	
	
	/************ implement  **************/
	private Bitmap getImageforComment(Comment comment, int imagefill){
		try{  
			URL url;
			if(imagefill == Comment.IMAGEFILL_USER)
				url=new URL(userURL(comment.getUserImage()));  
			else
				url=new URL(productURL(comment.getProductImage()));  
             
            InputStream is=url.openStream();  
            
            Bitmap bitmap=BitmapFactory.decodeStream(is);  
          
            is.close();  
            
            return bitmap;
        }  
       catch(Exception ex)  
       {  
           ex.printStackTrace();  
           return null;
       }  
	}
	
	private Bitmap getImageforLike(ActionLike like){
		try{  
            URL url=new URL(productURL(like.getProductImage()));  
             
            InputStream is=url.openStream();  
            
            Bitmap bitmap=BitmapFactory.decodeStream(is);  
          
            is.close();  
            
            return bitmap;
        }  
       catch(Exception ex)  
       {  
           ex.printStackTrace();  
           return null;
       }  
	}
	
	private Bitmap getImageforProduct(ProductBasicInf product){
		try{  
            URL url=new URL(productURL(product.getImageURL()));  
             
            InputStream is=url.openStream();  
            
            Bitmap bitmap=BitmapFactory.decodeStream(is);  
          
            is.close();  
            
            return bitmap;
        }  
       catch(Exception ex)  
       {  
           ex.printStackTrace();  
           return null;
       }  
	}
	
	private Bitmap getImageforUser(User user){
		try{  
            URL url=new URL(userURL(user.getImage()));  
             
            InputStream is=url.openStream();  
            
            Bitmap bitmap=BitmapFactory.decodeStream(is);  
          
            is.close();  
            
            return bitmap;
        }  
       catch(Exception ex)  
       {  
           ex.printStackTrace();  
           return null;
       }  
	}
	
	private Bitmap getImageforGroup(com.osnproject.tasteme.Model.Group group){
		try{  
            URL url=new URL(groupURL(group.getGroupIcon()));  
             
            InputStream is=url.openStream();  
            
            Bitmap bitmap=BitmapFactory.decodeStream(is);  
          
            is.close();  
            
            return bitmap;
        }  
       catch(Exception ex)  
       {  
           ex.printStackTrace();  
           return null;
       }  
	}
	
	
	private String productURL(String imageURL){
		return URL_IMAGE + "productimages/" + imageURL;
	}
	
	private String userURL(String imageURL){
		return URL_IMAGE + "userimages/" + imageURL;
	}
	
	private String groupURL(String imageURL){
		return URL_IMAGE + "groupimages/" + imageURL;
	}
	
	private void submitHttpPost(String url, PoiPutJSON poiJson){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		
		JSONObject json;
		try{
			json = poiJson.getJsonObj();
				
			HttpPost post = new HttpPost(url);
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			response = client.execute(post);
			/*check response to see if success*/
			
			if(response!=null){
				if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}
		 
				InputStream in = response.getEntity().getContent();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("httpPost", e.toString());
		}
	}
	
	private void submitHttpPut(String url, PoiPutJSON poiJson){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		
		JSONObject json;
		try{
			json = poiJson.getJsonObj();
				
			HttpPut put = new HttpPut(url);
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			put.setEntity(se);
			response = client.execute(put);
			/*check response to see if success*/
			
			if(response!=null){
				if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}
				InputStream in = response.getEntity().getContent();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("httpPut", e.toString());
		}
		
	}

	private String submitHttpGet(String url){
		HttpResponse response;
		//JSONArray jArray = null;
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url); 
		try {
			response = httpclient.execute(httpget);
			Log.i("GETcommand",response.getStatusLine().toString());
			
			if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				Log.i("GETresult",result);
				instream.close();
			}
		} 
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("httpGet", e.toString());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("httpGet", e.toString());
			e.printStackTrace();
			return null;
		} 
		return result;
	}
	
	private JSONArray stringToJsonArray(String httpResponse){
		JSONArray jArray = null;
		try {
			if(httpResponse == null || httpResponse.equals("")){
				return null;
			}
			jArray = new JSONArray(httpResponse);
			return jArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("JSONarry", e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
