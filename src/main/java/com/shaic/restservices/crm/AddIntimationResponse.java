package com.shaic.restservices.crm;

import java.util.List;

public class AddIntimationResponse {
	
	private String resultMsg;
	private String intimationNo;
	private String errorYN;
	private List<Error> errors;
	
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getErrorYN() {
		return errorYN;
	}
	public void setErrorYN(String errorYN) {
		this.errorYN = errorYN;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString() {
		return "AddIntimationResponse [resultMsg=" + resultMsg + ", errorYN="
				+ errorYN + ", errors.size()=" + errors.size() + "]";
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
}
