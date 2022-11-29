package com.shaic.claim.userproduct.document;



public class ApplicableCpuDTO  {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	private int sno;
	
	private String cpuCodewithName;
	
	private String cpuCode;
	
	private Boolean accessability;
	
	private Boolean checkBoxValue = false;
	
	private Boolean isEnabled = false;
	
	private Long activeStatus;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno2) {
		this.sno = sno2;
	}

	public String getCpuCodewithName() {
		return cpuCodewithName;
	}

	public void setCpuCodewithName(String cpuCodewithName) {
		this.cpuCodewithName = cpuCodewithName;
	}

	public Boolean getAccessability() {
		return accessability;
	}

	public void setAccessability(Boolean accessability) {
		this.accessability = accessability;
	}

	public Boolean getCheckBoxValue() {
		return checkBoxValue;
	}

	public void setCheckBoxValue(Boolean checkBoxValue) {
		this.checkBoxValue = checkBoxValue;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}
