package com.ssp.api.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ssp.api.context.NotBoundObject;
import com.ssp.api.context.WebApiResponse;
import com.ssp.api.context.WebApiResponseFactory;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class WebapiResponseHttpConverter extends FastJsonHttpMessageConverter
{
	private WebApiResponseFactory webApiResponseFactory;

	  public WebApiResponseFactory getWebApiResponseFactory()
	  {
	    return this.webApiResponseFactory;
	  }

	  public void setWebApiResponseFactory(WebApiResponseFactory webApiResponseFactory) {
	    this.webApiResponseFactory = webApiResponseFactory;
	  }

	/**
	 *
	 * @param obj
	 * @param outputMessage
	 * @throws IOException
	 * @throws HttpMessageNotWritableException
	 * Object obj 为service返回值
     */
	  protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
	    throws IOException, HttpMessageNotWritableException
	  {
	    OutputStream out = outputMessage.getBody();
	    String text = null;
	    if (((obj instanceof WebApiResponse)) || ((obj instanceof NotBoundObject))) {
	      text = JSON.toJSONString(obj, getFeatures());
	    }
	    else {
			// 本地环境 中文或英文
	      Locale locale = LocaleContextHolder.getLocale();
			// 通过0000取对应的返回值
	      WebApiResponse webApiResponse = this.webApiResponseFactory.get("0000", null, obj, locale);

	      text = JSON.toJSONString(webApiResponse, getFeatures());
	    }
		  System.out.println("--------------api-response--------------" + text);
		  byte[] bytes = text.getBytes(getCharset());
	    out.write(bytes);
	  }
}

