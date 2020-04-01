package com.ssp.api.exception;

public class AppException extends RuntimeException {
	 private static final long serialVersionUID = 1L;
     private Object attach;
     private String errCode;
     private String message;
     private String[] format;
     
     public AppException(String errCode)
     {
       this.errCode = errCode;
     }

     public AppException(String errCode, String message, String[] format)
     {
       this.errCode = errCode;
       this.message = message;
       this.format = format;
     }

     public AppException(String errCode, String message, Object attach, String[] format)
     {
       this.errCode = errCode;
       this.message = message;
       this.format = format;
       this.attach = attach;
     }
	public Object getAttach() {
		return attach;
	}
	public void setAttach(Object attach) {
		this.attach = attach;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String[] getFormat() {
		return format;
	}
	public void setFormat(String[] format) {
		this.format = format;
	}
     
}
