package com.shaic.claim.reimbursement.talktalktalk;

public class DialerPlaceCallResponse {
	
	private String STATUS;

	private String MESSAGE;

	private String PHONE_NUMBER;

	private String PROCESS;

	private String LEAD_ID;

	private String refno;
	
	private String CALL_HIT_REFERENCE_NUMBER;

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

	public String getPHONE_NUMBER() {
		return PHONE_NUMBER;
	}

	public void setPHONE_NUMBER(String pHONE_NUMBER) {
		PHONE_NUMBER = pHONE_NUMBER;
	}

	public String getPROCESS() {
		return PROCESS;
	}

	public void setPROCESS(String pROCESS) {
		PROCESS = pROCESS;
	}

	public String getLEAD_ID() {
		return LEAD_ID;
	}

	public void setLEAD_ID(String lEAD_ID) {
		LEAD_ID = lEAD_ID;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getCALL_HIT_REFERENCE_NUMBER() {
		return CALL_HIT_REFERENCE_NUMBER;
	}

	public void setCALL_HIT_REFERENCE_NUMBER(String cALL_HIT_REFERENCE_NUMBER) {
		CALL_HIT_REFERENCE_NUMBER = cALL_HIT_REFERENCE_NUMBER;
	}
	
	

}
