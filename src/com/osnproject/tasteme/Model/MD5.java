package com.osnproject.tasteme.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public String str;

	public String md5encode(String plainText){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			
			for(int offset = 0; offset < b.length; offset++){
				i = b[offset];
				if(i < 0)
					i += 256;
				if( i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			
			str = buf.toString();
			
			return str;
			
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}
	
}
