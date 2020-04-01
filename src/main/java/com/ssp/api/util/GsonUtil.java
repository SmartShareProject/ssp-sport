package com.ssp.api.util;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	/**
	 * Gson 鍙浆鎹㈡坊鍔犳敞瑙ｇ殑瀛楁
	 */
	public static Gson getExcludeGson(){
		 return GsonFactory.getExcludeGson();
	}
	
	/**
	 * 鑾峰彇鏅�Gson瀵硅薄
	 */
	public static Gson getGson(){
		return GsonFactory.getGson();
	}
	
	/**
	 * 鏍规嵁key鑾峰彇json
	 */
	public static Object getJsonByKey(String json, String key){
		try {
			return JSONObject.fromObject(json).get(key);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * @param jsonStr json瀛楃涓�
	 * @return 浣跨敤json搴撹В鏋恓son
	 */
	public static JSONObject getJsonObject(String jsonStr){
		 return JSONObject.fromObject(jsonStr);
	}
	
	private static class GsonFactory{
		//public static Gson gson = new GsonBuilder().setDateFormat(DateUtil.DATE_FORMAT1).create(); 
		public static Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create(); 
		public static GsonBuilder builder = new GsonBuilder();
		
		private static Gson getGson(){
			return gson;
		}
		
		private static Gson getExcludeGson(){
			 builder.excludeFieldsWithoutExposeAnnotation();
			 return builder.create();
		}
		
	}
	
}
