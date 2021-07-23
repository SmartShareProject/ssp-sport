package com.ssp.api.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoxuesong on 2018/6/12.
 */
public class SendMsgUtil {
  /**
   * 发送消息 text/html;charset=utf-8
   *
   * @param response HttpServletResponse
   * @param str      String
   */
  public static void sendMessage(HttpServletResponse response, String str) throws Exception {
    response.setContentType("text/html; charset=utf-8");
    PrintWriter writer = response.getWriter();
    writer.print(str);
    writer.close();
    response.flushBuffer();
  }

  /**
   * 将某个对象转换成json格式并发送到客户端
   *
   * @param response HttpServletResponse
   * @param obj      Object
   */
  public static void sendErrorJsonMessage(HttpServletResponse response, Object obj) throws Exception {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("retCode", "1001");
    requestMap.put("retMsg", obj);
    requestMap.put("data", obj);
    response.setContentType("application/json; charset=utf-8");
    PrintWriter writer = response.getWriter();
    writer.print(JSONObject.toJSONString(requestMap, SerializerFeature.WriteMapNullValue,
        SerializerFeature.WriteDateUseDateFormat));
    writer.close();
    response.flushBuffer();
  }
}
