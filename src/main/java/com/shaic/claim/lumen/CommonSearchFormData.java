package com.shaic.claim.lumen;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class CommonSearchFormData {
	private String intimationNumber;
	private String policyNumber;
	private Long cpuCode;
	private String empName;
	private String source;
	private String status;
	private String currentQ;
	private Map<Long, List<Object>> resultKeys;
	private String cpuCodeMulti;
	
	private Date fromDate;
	private Date toDate;
	private String lisOfDoctorsId;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentQ() {
		return currentQ;
	}
	public void setCurrentQ(String currentQ) {
		this.currentQ = currentQ;
	}
	public Map<Long, List<Object>> getResultKeys() {
		return resultKeys;
	}
	public void setResultKeys(Map<Long, List<Object>> resultKeys) {
		this.resultKeys = resultKeys;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getLisOfDoctorsId() {
		return lisOfDoctorsId;
	}
	public void setLisOfDoctorsId(String lisOfDoctorsId) {
		this.lisOfDoctorsId = lisOfDoctorsId;
	}
	public String getCpuCodeMulti() {
		return cpuCodeMulti;
	}
	public void setCpuCodeMulti(String cpuCodeMulti) {
		this.cpuCodeMulti = cpuCodeMulti;
	}

}