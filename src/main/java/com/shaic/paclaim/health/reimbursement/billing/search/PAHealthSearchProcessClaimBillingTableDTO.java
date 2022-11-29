/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.billing.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimBillingTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String policyNo;
	private String insuredPatientName;
	private String paPatientName;
	private Long productKey;
	private String hospitalCity;
	private Long hospitalNameID;
	private String hospitalType;
	private String reasonForAdmission;
	private Long cpuId;
	private String intimationNo;
	private Long cpuCode;
	private String cpuName;
	
	private String productName;
	private String hospitalName;
	private String hospitalAddress;
	private Date dateOfAdmission1;
	private String dateOfAdmission;
	private Double claimAmt;
	private String type;
	private String documentReceivedFrom;
	private String claimType;
	private Long rodKey;	
	private Long claimKey;
	private String originatorID;
	private String originatorName;
	
	private String strCpuCode;
	
	private String coverBenefits;
	private String accidentOrDeath;
	
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
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
	
	
	public Date getDateOfAdmission1() {
		return dateOfAdmission1;
	}
	public void setDateOfAdmission1(Date dateOfAdmission1) {
		String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(dateOfAdmission1);
		setDateOfAdmission(dateformate);
		this.dateOfAdmission1 = dateOfAdmission1;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	public Double getClaimAmt() {
		return claimAmt;
	}
	public void setClaimAmt(Double claimAmt) {
		this.claimAmt = claimAmt;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getOriginatorID() {
		return originatorID;
	}
	public String getOriginatorName() {
		return originatorName;
	}
	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}
	public void setOriginatorID(String originatorID) {
		this.originatorID = originatorID;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getStrCpuCode() {
		return strCpuCode;
	}
	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getCoverBenefits() {
		return coverBenefits;
	}
	public void setCoverBenefits(String coverBenefits) {
		this.coverBenefits = coverBenefits;
	}
	public String getAccidentOrDeath() {
		return accidentOrDeath;
	}
	public void setAccidentOrDeath(String accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
	}
	public String getPaPatientName() {
		return paPatientName;
	}
	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}
	public Long getProductKey() {
		return productKey;
	}
	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	
}
