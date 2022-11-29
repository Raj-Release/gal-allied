package com.shaic.paclaim.cashless.flp.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PAProcessPreMedicalTableDTO  extends AbstractTableDTO  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String intimationNo;

private String intimationSource;

private String cpuName;

private String productName;

private Long productKey;

private String insuredPatientName;

private String paPatientName;

private String hospitalName;

private String hospitalTypeName;

private String networkHospType;

private String preAuthReqAmt;

private Double balanceSI;

//private Date docReceivedTimeForReg;
private String docReceivedTimeForReg;

private Date docReceivedTimeForRegDate;

//private Date docReceivedTimeForMatch;
private String docReceivedTimeForMatch;

private Date docReceivedTimeForMatchDate;

private String type;

private String transactionType;

private Long hospitalTypeId;

private String policyNo;

//private Double sumInsured;
private Integer sumInsured;

private Long policyKey;

private Long insuredId;

private Long insuredKey;

private Long intimationKey;

private Long nhpUpdDocumentKey;


//private HumanTask humanTask;

public Long getNhpUpdDocumentKey() {
	return nhpUpdDocumentKey;
}

public void setNhpUpdDocumentKey(Long nhpUpdDocumentKey) {
	this.nhpUpdDocumentKey = nhpUpdDocumentKey;
}

public Long getIntimationKey() {
	return intimationKey;
}

public void setIntimationKey(Long intimationKey) {
	this.intimationKey = intimationKey;
}

public String getTransactionType() {
	return transactionType;
}

public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
}

public String getIntimationNo() {
	return intimationNo;
}

public void setIntimationNo(String intimationNo) {
	this.intimationNo = intimationNo;
}

public String getIntimationSource() {
	return intimationSource;
}

public void setIntimationSource(String intimationSource) {
	this.intimationSource = intimationSource;
}

public String getCpuName() {
	return cpuName;
}

public void setCpuName(String cpuName) {
	this.cpuName = cpuName;
}

public String getProductName() {
	return productName;
}

public void setProductName(String productName) {
	this.productName = productName;
}

public String getInsuredPatientName() {
	return insuredPatientName;
}

public void setInsuredPatientName(String insuredPatientName) {
	this.insuredPatientName = insuredPatientName;
}

public String getHospitalName() {
	return hospitalName;
}

public void setHospitalName(String hospitalName) {
	this.hospitalName = hospitalName;
}

public String getHospitalTypeName() {
	return hospitalTypeName;
}

public void setHospitalTypeName(String hospitalTypeName) {
	this.hospitalTypeName = hospitalTypeName;
}

public String getNetworkHospType() {
	return networkHospType;
}

public void setNetworkHospType(String networkHospType) {
	this.networkHospType = networkHospType;
}

public String getPreAuthReqAmt() {
	return preAuthReqAmt;
}

public void setPreAuthReqAmt(String preAuthReqAmt) {
	this.preAuthReqAmt = preAuthReqAmt;
}

public Double getBalanceSI() {
	return balanceSI;
}

public void setBalanceSI(Double balanceSI) {
	this.balanceSI = balanceSI;
}



public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public Long getHospitalTypeId() {
	return hospitalTypeId;
}

public void setHospitalTypeId(Long hospitalTypeId) {
	this.hospitalTypeId = hospitalTypeId;
}

public String getPolicyNo() {
	return policyNo;
}

public void setPolicyNo(String policyNo) {
	this.policyNo = policyNo;
}

public Integer getSumInsured() {
	return sumInsured;
}

public void setSumInsured(Integer sumInsured) {
	this.sumInsured = sumInsured;
}

public static long getSerialversionuid() {
	return serialVersionUID;
}

/*public void setDocReceivedTimeForReg(Date docReceivedTimeForReg) {
	this.docReceivedTimeForReg = docReceivedTimeForReg;
}

public void setDocReceivedTimeForMatch(Date docReceivedTimeForMatch) {
	this.docReceivedTimeForMatch = docReceivedTimeForMatch;
}

public Date getDocReceivedTimeForReg() {
	return docReceivedTimeForReg;
}

public Date getDocReceivedTimeForMatch() {
	return docReceivedTimeForMatch;
}*/

public Long getPolicyKey() {
	return policyKey;
}

public void setPolicyKey(Long policyKey) {
	this.policyKey = policyKey;
}

public Long getInsuredId() {
	return insuredId;
}

public void setInsuredId(Long insuredId) {
	this.insuredId = insuredId;
}

public Long getInsuredKey() {
	return insuredKey;
}

public void setInsuredKey(Long insuredKey) {
	this.insuredKey = insuredKey;
}

public String getDocReceivedTimeForReg() {
	return docReceivedTimeForReg;
}

public void setDocReceivedTimeForReg(String docReceivedTimeForReg) {
	this.docReceivedTimeForReg = docReceivedTimeForReg;
}

public String getDocReceivedTimeForMatch() {
	return docReceivedTimeForMatch;
}

public void setDocReceivedTimeForMatch(String docReceivedTimeForMatch) {
	this.docReceivedTimeForMatch = docReceivedTimeForMatch;
}

public Date getDocReceivedTimeForRegDate() {
	return docReceivedTimeForRegDate;
}

public void setDocReceivedTimeForRegDate(Date docReceivedTimeForRegDate) {
	
	
	if(docReceivedTimeForRegDate !=  null){
	//	String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForRegDate);
		String dateformate =new SimpleDateFormat("dd/MM/yyyy HH:MM:SS").format(docReceivedTimeForRegDate);
		
		//setDateOfAdmission(dateformate);
		setDocReceivedTimeForReg(dateformate);
		this.docReceivedTimeForRegDate = docReceivedTimeForRegDate;
		}
	
	
}



public void setDocReceivedTimeForMatchDate(Date docReceivedTimeForMatchDate) {
	
	if(docReceivedTimeForMatchDate !=  null){
		//String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForMatchDate);
		String dateformate =new SimpleDateFormat("dd/MM/yyyy HH:MM:SS").format(docReceivedTimeForMatchDate);
		//setDateOfAdmission(dateformate);
		setDocReceivedTimeForMatch(dateformate);
		this.docReceivedTimeForMatchDate = docReceivedTimeForMatchDate;
		}
	
	
}

public Date getDocReceivedTimeForMatchDate() {
	return docReceivedTimeForMatchDate;
}

public Long getProductKey() {
	return productKey;
}

public void setProductKey(Long productKey) {
	this.productKey = productKey;
}

public String getPaPatientName() {
	return paPatientName;
}

public void setPaPatientName(String paPatientName) {
	this.paPatientName = paPatientName;
}







	
}
