package com.ssp.api.util;

import org.apache.commons.lang.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;


public class WithholdUtil {

  public static Map<String, Object> objectToMap(Object obj) throws Exception {
    if (obj == null)
      return null;

    Map<String, Object> map = new HashMap<>();

    BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
    PropertyDescriptor[] propertyDescriptors = beanInfo
        .getPropertyDescriptors();
    for (PropertyDescriptor property : propertyDescriptors) {
      String key = property.getName();
      if (key.compareToIgnoreCase("class") == 0) {
        continue;
      }
      Method getter = property.getReadMethod();
      Object value = getter != null ? getter.invoke(obj) : null;
      if (!"sign".equalsIgnoreCase(key)) {
        if (value != null) {
          map.put(key, value);
        }
      }
    }

    return map;
  }

  public static String formatUrlMap(Map<String, Object> tmpMap, String spiltStr) {
    String buff = "";
    try {
//			Map<String, Object> tmpMap = objectToMap(obj);
      List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(
          tmpMap.entrySet());
      // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
      Collections.sort(infoIds,
          new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
              return (o1.getKey()).compareTo(o2.getKey());
            }
          });
      // 构造URL 键值对的格式
      StringBuilder buf = new StringBuilder();
      for (Map.Entry<String, Object> item : infoIds) {
        if (StringUtils.isNotBlank(item.getKey())) {
          String key = item.getKey();
          if ("sign".equalsIgnoreCase(key)) {
            continue;
          }
          Object valObj = item.getValue();
          if (valObj != null) {
            String val = valObj.toString();
            val = URLEncoder.encode(val, "utf-8");
//						val = URLDecoder.decode(val, "utf-8");
            buf.append(key).append("=").append(val);
            buf.append(spiltStr);
          }
        }

      }
      buff = buf.toString();
      if (!buff.isEmpty()) {
        buff = buff.substring(0, buff.length() - 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return buff;
  }

  public static boolean checkSign(String plain, String key, String sign) {
    String signCheck = AssistorCrypt.mac(key, plain);
    return signCheck.equals(sign);
  }

  public static String getSign(String key, String plain) {
    return AssistorCrypt.mac(key, plain);
  }
}
