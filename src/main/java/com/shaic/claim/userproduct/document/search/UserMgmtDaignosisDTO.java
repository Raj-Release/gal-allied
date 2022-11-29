package com.shaic.claim.userproduct.document.search;

public class UserMgmtDaignosisDTO {
	
	
	private int sno;
	
	private Long key;
	
	private String value;
	
	private Boolean diagnosisEnable;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getDiagnosisEnable() {
		return diagnosisEnable;
	}

	public void setDiagnosisEnable(Boolean diagnosisEnable) {
		this.diagnosisEnable = diagnosisEnable;
	}

}
