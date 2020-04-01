package com.ssp.api.hessian;

import com.caucho.hessian.client.HessianConnection;

import java.net.URL;
import java.util.Map;


public class HessianProxy extends com.caucho.hessian.client.HessianProxy  {

   /**  */
   private static final long serialVersionUID = 1L;

    protected HessianProxy(URL url, HessianProxyFactory factory) {
       super(url, factory);
    }

    protected HessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
        super(url, factory, type);
   }

    @Override
    protected void addRequestHeaders(HessianConnection conn) {
        super.addRequestHeaders(conn);
       Map<String, String> headerMap = HessianHeaderContext.getContext().getHeaders();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            conn.addHeader(entry.getKey(), entry.getValue());
        }
    }
}