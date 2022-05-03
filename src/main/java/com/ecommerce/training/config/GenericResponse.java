package com.ecommerce.training.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GenericResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/** e@Datarror code. 0 if success else unique error code value */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int statusCode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "GenericResponse [status=" + status + ", errorDescription=" + description + ", errorCode="
				+ statusCode + ", userDisplayMesg=" + message + ", Data=" + data + "]";
	}
	
}
