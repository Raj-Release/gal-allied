package com.shaic.restservices.pms;

import java.util.List;
import com.shaic.restservices.crm.Error;

public class PmsHospitalResponse {
	
	private String resultMsg;
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
	
}
