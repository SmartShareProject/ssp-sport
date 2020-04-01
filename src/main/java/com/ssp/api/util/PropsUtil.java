package com.ssp.api.util;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropsUtil.class);

	private static final String[] files = { 
			"META-INF/properties/dynprops.properties",
			"META-INF/properties/api.properties",
			"META-INF/properties/kuaike.properties",
			"META-INF/properties/app.properties",
			"META-INF/properties/rain.properties"};
	private static Properties properties = new Properties();
	private static long refreshInterval = 1000*30;// 30秒刷新
	private static Map<String, Long> modMap = new HashMap<String, Long>();
	public static long lastTime = System.currentTimeMillis();

	static { 
		load();
	}

	/**
	* desc: 加载配置文件
	*/
	private static void load() {
		File configFile = null;
		InputStream in = null;
		for (String file : files) {
			URL url = PropsUtil.class.getClassLoader().getResource(file);
			configFile = new File(URLDecoder.decode(url.getPath()));
			long lastModifiedTime = configFile.lastModified();
			Long oldModifiedTime = modMap.get(file);
			if (oldModifiedTime != null && lastModifiedTime == oldModifiedTime.longValue()){
				continue;
			}
			modMap.put(file, lastModifiedTime);
			try {
				in = new FileInputStream(configFile);
				properties.load(new InputStreamReader(in,"utf-8"));
//				properties.load(in);
				in.close();
			} catch (IOException e) {
				logger.error("加载配置文件异常", e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error("加载配置文件时，关闭输入流异常", e);
					}
					in = null;
				}
			}
		}
	}

	/**
	* desc: 
	*/
	private static void reflesh() {
		long now = System.currentTimeMillis();
		if (now - lastTime > refreshInterval) {
			load();
			lastTime = now;
		}
	}

	
	/**
	* desc: 
	* @param key
	* @param defaultValue
	* @return
	*/
	public static String getMessage(String key, String defaultValue){
		reflesh();
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	* desc: 
	* @param key
	* @return
	*/
	public static String getMessage(String key){
		reflesh();
		return properties.getProperty(key);
	}
	
	public static String getMessage(String key, Object[] args){
		reflesh();
		if(properties.containsKey(key)){
			return String.format(properties.getProperty(key), args);
		}else{
			return null;
		}
	}
}
