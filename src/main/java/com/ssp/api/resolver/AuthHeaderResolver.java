package com.ssp.api.resolver;


import com.ssp.api.context.AuthContext;
import com.ssp.api.context.AuthForHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthHeaderResolver implements HandlerMethodArgumentResolver {
	private static final Logger log = LoggerFactory.getLogger(AuthHeaderResolver.class);
	private static final String AUTH_HEADER = "authorization";
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthForHeader.class);
	}

	public Object resolveArgument(MethodParameter parameter,
								  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
								  WebDataBinderFactory binderFactory) throws Exception {
		  	String header = webRequest.getHeader("authorization");
		    if (!parameter.getParameterType().isAssignableFrom(AuthContext.class)) {
		      return null;
		    }

		    return AuthContext.build(header, webRequest);
	}

}
