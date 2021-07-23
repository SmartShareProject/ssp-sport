package com.ssp.api.util;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

/*****
 * 雁阵签名工具类
 * @author Chenweiwei
 *
 */
public class SignUtil {

  private static final Logger logger = Logger.getLogger(SignUtil.class);


  /**
   * 获取RSA
   *
   * @param method：消息方法名
   * @param ver：                                        版本
   * @param channelId：                                  渠道ID
   * @param privateKey：                                 用于RSA签名的私钥
   * @param param：消息体对象(可以为一个对象或者HashMap容器，但不能为JSON字符串)
   * @return String
   */
  public static String generateRSASign(String method, String ver, String channelId, String privateKey, Object param) {
    HashMap<String, Object> msg = new HashMap<String, Object>();
    try {
      msg.put("method", method);
      msg.put("ver", ver);
      msg.put("channelId", channelId);
      msg.put("signType", "RSA");
      msg.put("params", param);
      JSONObject jsonObject = GsonUtil.getJsonObject(GsonUtil.getGson().toJson(msg));
      String genSignData = genSignData(jsonObject);
      logger.info("RSA sign Data:" + genSignData);
      return RSAUtil.sign(privateKey, genSignData);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取MD5签名
   *
   * @param method：消息方法名
   * @param ver：                                        版本
   * @param channelId：                                  渠道ID
   * @param md5Key：                                     MD5签名秘钥
   * @param param：消息体对象(可以为一个对象或者HashMap容器，但不能为JSON字符串)
   * @return String
   */
  public static String generateMD5Sign(String method, String ver, String channelId, String md5Key, Object param) {
    HashMap<String, Object> msg = new HashMap<String, Object>();
    msg.put("params", param);
    // JSONObject jsonObject = GsonUtil.getJsonObject(GsonUtil.getGson().toJson(msg));
    JSONObject jsonObject = JSONObject.fromObject(msg);
    String genSignData = genSignData(jsonObject);
    genSignData += "&key=" + md5Key;
    System.out.println("request:" + genSignData);
    return Md5Util.getInstance().md5Digest(genSignData.getBytes(StandardCharsets.UTF_8));
  }

  public static String generateMD5Sign(String md5Key, Object param) {
    HashMap<String, Object> msg = new HashMap<String, Object>();
    msg.put("params", param);
    // JSONObject jsonObject = GsonUtil.getJsonObject(GsonUtil.getGson().toJson(msg));
    JSONObject jsonObject = JSONObject.fromObject(msg);
    String genSignData = genSignData(jsonObject);
    genSignData += "&key=" + md5Key;
    // System.out.println("request:"+genSignData);
    return Md5Util.getInstance().md5Digest(genSignData.getBytes(StandardCharsets.UTF_8));
  }


  /****
   *  sign签名校验
   * @param request：完整请求消息的String字符串
   * @param key：参与sign校验的key (RSA为public key)
   * @return boolean
   */
  public static boolean verifySign(String request, String key) {
    boolean checkResult = false;
    try {
      //	JSONObject jsonObject = GsonUtil.getJsonObject(GsonUtil.getGson().toJson(request));
      JSONObject jsonObject = new JSONObject().fromObject(request);
      String genSignData = genSignData(jsonObject);
      String sign = jsonObject.getString("sign");
      sign = sign.replace(" ", "+");
      // String signType =  jsonObject.getString("signType");
      String signType = "MD5";
      genSignData += "&key=" + key;
      String md5Digest = Md5Util.getInstance().md5Digest(genSignData.getBytes(StandardCharsets.UTF_8));
      // logger.info("generated MD5:"+md5Digest);
      if (sign.equals(md5Digest)) {
        checkResult = true;
      }
    } catch (Exception e) {
      logger.error("verify sign failed:" + e.getMessage());
      checkResult = false;
    }

    return checkResult;
  }

  /****
   * 生成排序好的待签名字符串
   * @param jsonObject：完整请求消息的JSON对象
   * @return 参与sign签名的排序好的待签名字符串
   */
  public static String genSignData(JSONObject jsonObject) {
    StringBuilder content = new StringBuilder();

    //按照key做首字母升序排列
    List<String> keys = new ArrayList<String>(jsonObject.keySet());
    Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);

    //按照顺序拼接待签名字符串
    for (int i = 0; i < keys.size(); i++) {
      String key = (String) keys.get(i);

      if ("sign".equals(key) ||
          "statusCode".equals(key) ||
          "errMsg".equals(key)) {//去除不需参与签名的内容
        continue;
      }

      String value = (String) jsonObject.getString(key);
      if (null == value) {// 空串不参与签名
        continue;
      }

      content.append((i == 0 ? "" : "&") + key + "=" + value);
    }

    String signSrc = content.toString();
    if (signSrc.startsWith("&")) {
      signSrc = signSrc.replaceFirst("&", "");
    }

    return signSrc;
  }


  /****
   * test例子
   * @param args
   */
  public static void main(String[] args) {

//		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJiZpfSH7DU0CBNdbn0uZohSGFmK6NGMyNPd4pn6oGtZd8PEvcetZdqDWIBUf8iyQC776n2jsl0TOIgqFL9U56RQxXu880EhODPSC8K2NGNfG8czFaJzpIIacStQA/vJ3BVOFXnM14IzgRnTuDcIJvw0UdMvlP10PtC+p50/VKQdAgMBAAECgYBZhuMfKFNDD2ihe9IjrQJIfel9NwrKTc9QxT95WNmewVmLSDSTHj7ASQi2GVzysOkI55C170DobCthb1bGvZg8HZiWgy3v7hP0RFp+ceqpOBaY09U+giJZ0IG/rXtxIk5Tf7nlC7+QO7CO2bCFYBcOdA/kAIRSeva3zR9Wyns3IQJBAMjck+qn3yQFgdWZkZ2xRFFFYoHgtvZihSlacmpTvJ/km61tJUJrKbDUlLY6a5bs9ZCOcDAgKYgSxFsGRAZphtUCQQDCfYdMrOCsiPnn1zKT6f04CcZl45aKtU0e0epdqvuQsUrp2tcz+eRUkUVqMpw2OcoOZDv/jdez1gGk2k9L09wpAkEAxFps9t14UzxW+boQEXmy8UfEznYgJaeVySEz7CFDqYLPdK/X1p/vt394iNN/TaEDRXcY0NMABpdiACGV6jbKqQJAcep4fw6bIjOwvHytYTmiWVpQXIlrOZ9rpmupbGejpWJS0JqfhhAODwJvt/4gxRogIUHQaqS3/NuSZu5/l5hl0QJAYLquPJZffJyGfRZAvbgfnKRSSWA03gwNeZrxuZ60YCwjW09MBGoZUTk31k8A9G7Bpi0JaZXnx3suLJ+hoNkLsA==";
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYmaX0h+w1NAgTXW59LmaIUhhZiujRjMjT3eKZ+qBrWXfDxL3HrWXag1iAVH/IskAu++p9o7JdEziIKhS/VOekUMV7vPNBITgz0gvCtjRjXxvHMxWic6SCGnErUAP7ydwVThV5zNeCM4EZ07g3CCb8NFHTL5T9dD7QvqedP1SkHQIDAQAB";

    //注：如果数值存在类似60.0的则需要变成60再进行加密，verifySign时不做要求
    //建议：param类型使用Map或者是po,不使用json,因为对象生成json的时候可能吧60变成60.0导致签名校验失败
		/*String param = "{\"balance\":2080.53,\"commissions\":60,\"loanAmount\":2000,\"refunds\":[{\"periodNumber\":1,\"dueDate\":\"20170108\",\"dueAmount\":693.51,\"status\":3},{\"periodNumber\":2,\"dueDate\":\"20170207\",\"dueAmount\":693.51,\"status\":3},{\"periodNumber\":3,\"dueDate\":\"20170309\",\"dueAmount\":693.51,\"status\":3}],\"loanDate\":\"20161209\"}";
		String ver = "2.0";
		String method = "getLoanDetailInfo";
		String channelId = "2";

		String md5Sign = generateMD5Sign(method, ver, channelId, "1234", param);
		logger.info("MD5 sign:"+md5Sign);

		String rsaSign = generateRSASign(method, ver, channelId, privateKey, param);
		logger.info("RSA sign:"+rsaSign);

		String request = "{\"method\":\"getLoanDetailInfo\",\"ver\":\"2.0\",\"channelId\":\"2\",\"params\":{\"balance\":2080.53,\"commissions\":60,\"loanAmount\":2000,\"refunds\":[{\"periodNumber\":1,\"dueDate\":\"20170108\",\"dueAmount\":693.51,\"status\":3},{\"periodNumber\":2,\"dueDate\":\"20170207\",\"dueAmount\":693.51,\"status\":3},{\"periodNumber\":3,\"dueDate\":\"20170309\",\"dueAmount\":693.51,\"status\":3}],\"loanDate\":\"20161209\"},\"signType\":\"RSA\",\"sign\":\""+rsaSign+"\",\"statusCode\":200}";

		boolean checksign = verifySign(request, publicKey);//RSA
		//boolean checksign = verifySign(request, "1234");//MD5

		logger.info("sign verify result:"+checksign);*/
//		String plain="loanId=88899&userId=123456d";
    /**
     outerId
     channelId
     id_card_front
     id_card_back
     image
     head_image
     company_name
     work_time
     positions
     top_education
     top_degree
     profession
     title
     address
     */
    String signkey = "201770167574654569A76BA0712A0967";
		/*
		outerId = 10000201
		amout = 1
		period = 1
		outerorderid = 1
		channelId = ffs0002

		 */
    Map<String, Object> requestMap = new HashMap<>();

    requestMap.put("outerId", "10000201");
    requestMap.put("channelId", "cc0001");
//		requestMap.put("amount","1");
//		requestMap.put("period","6");
//		requestMap.put("outerorderid","1");
//		requestMap.put("idCardNo","赵雪松");
//		requestMap.put("id_card_front","http://resourceyj.liangshua.com/group2/M00/07/0B/dNVK2VsWCROEd5K6AAAAAFRtSPU422.jpg");
//		requestMap.put("id_card_back","http://resourceyj.liangshua.com/group2/M00/07/22/dNVK2VsWQP6EKN9mAAAAAKxXm7I215.jpg");
//		requestMap.put("image","http://resourceyj.liangshua.com/group2/M00/07/22/dNVK2VsWQRGEFBNJAAAAAB-81K0137.jpg");
//		requestMap.put("head_image","http://resourceyj.liangshua.com/group2/M00/09/68/dNVK2VsguMeEWIzsAAAAANeFq20384.jpg");
    requestMap.put("creditType", "0");
    requestMap.put("returnUrl", "http://www.baidu.com");
//		requestMap.put("company_name","阿里巴巴");
//		requestMap.put("work_time","5");
//		requestMap.put("positions","java开发工程师");
//		requestMap.put("top_education","20");
//		requestMap.put("top_degree","4");
//		requestMap.put("profession","1");
//		requestMap.put("title","1");
//		requestMap.put("address","北京朝阳望京");
//		requestMap.put("emContacts","emContacts");
//		requestMap.put("userId","36020170928180210585");
    String plain = WithholdUtil.formatUrlMap(requestMap, "&");
    String localSign = SignUtil.generateMD5Sign("index", "2.0", "1", signkey, plain);
    requestMap.put("sign", localSign);
    System.out.println(localSign);

    int[] arrayDemo = new int[]{};
  }
}
