package com.shaic.claim.intimation.create;

import java.io.Serializable;
import java.util.List;

public class CallRecordingResponse implements Serializable{

	private static final long serialVersionUID = 7440681781473904028L;
	
	private String resCode;
	private String errorCode;
	private String message;
	private List<TVCAudioDetails> tvcaudiodtl;
	
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<TVCAudioDetails> getTvcaudiodtl() {
		return tvcaudiodtl;
	}
	public void setTvcaudiodtl(List<TVCAudioDetails> tvcaudiodtl) {
		this.tvcaudiodtl = tvcaudiodtl;
	}
	
}
