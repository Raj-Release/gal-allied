package com.shaic.restservices.bonus;

public class BonusResponse {

	private String status;
	private String currentYearBonus;
	private String requestId;
	private String message;
	
	public String getStatus() {
		return status;
	}
	public String getCurrentYearBonus() {
		return currentYearBonus;
	}
	public String getRequestId() {
		return requestId;
	}
	public String getMessage() {
		return message;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCurrentYearBonus(String currentYearBonus) {
		this.currentYearBonus = currentYearBonus;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "BonusResponse [status=" + status + ", currentYearBonus="
				+ currentYearBonus + ", requestId=" + requestId + ", message="
				+ message + "]";
	}
	
}
