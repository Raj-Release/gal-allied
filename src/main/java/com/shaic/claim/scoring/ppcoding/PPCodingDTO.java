package com.shaic.claim.scoring.ppcoding;

import javax.persistence.Column;

public class PPCodingDTO {

	private String hospitalType;
	
	private String ppCode;
	
	private String ppCodingDesc;
	
	private Long ppVersion;

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getPpCode() {
		return ppCode;
	}

	public void setPpCode(String ppCode) {
		this.ppCode = ppCode;
	}

	public String getPpCodingDesc() {
		return ppCodingDesc;
	}

	public void setPpCodingDesc(String ppCodingDesc) {
		this.ppCodingDesc = ppCodingDesc;
	}

	public Long getPpVersion() {
		return ppVersion;
	}

	public void setPpVersion(Long ppVersion) {
		this.ppVersion = ppVersion;
	}
	
}
