package com.ssp.api.hessian;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class HessianServiceExporter extends org.springframework.remoting.caucho.HessianServiceExporter {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                       IOException {

        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" },
                "HessianServiceExporter only supports POST requests");
        }

        handleHessianHeader(request);

        response.setContentType(CONTENT_TYPE_HESSIAN);
        try {
            invoke(request.getInputStream(), response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Hessian skeleton invocation failed", ex);
        } finally {
            HessianHeaderContext.close();
        }
    }

    protected void handleHessianHeader(HttpServletRequest request) {
        HessianHeaderContext context = HessianHeaderContext.getContext();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            String value = request.getHeader(name);
            context.addHeader(name, value);
        }
    }

}