/**
 * 
 */
package com.shaic.reimbursement.rod.createrod.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
//import com.shaic.ims.bpm.claim.modelv2.HumanTask;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateRODTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String insuredPatientName;
	private String paPatientName;
	private Long productKey;
	private Long cpuCode;
	
	private String claimType;
	private String hospitalName;
	private String hospitalCity;
	private Date dateOfAdmission1;
	private String dateOfAdmission;
	private Long hospitalNameID;
	private String hospitalType;
	private Long cpuId;
	private String reasonForAdmission;
//	private HumanTask humanTaskListDTO;
	private Long claimkey;
	private Long ackKey;
	
	private String acknowledgementNumber;
	private String acknowledgementType;
	private String healthCardIDNumber;
	private String status;
	private String strCpuCode;
	
	private String isDocumentUploaded;
	
	private Long ackDocKey;
	
	private Long docAcknowledgementKey;
	private String accidentOrDeath;
	
	private String crmFlagged;
	private Long rodKey;
	
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
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
	/*public HumanTask getHumanTaskListDTO() {
		return humanTaskListDTO;
	}
	public void setHumanTaskListDTO(HumanTask humanTaskListDTO) {
		this.humanTaskListDTO = humanTaskListDTO;
	}*/
	public Long getClaimkey() {
		return claimkey;
	}
	public void setClaimkey(Long claimkey) {
		this.claimkey = claimkey;
	}
	public Long getAckNo() {
		return ackKey;
	}
	public void setAckNo(Long ackKey) {
		this.ackKey = ackKey;
	}
	public Long getAckKey() {
		return ackKey;
	}
	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
	}
	public String getAcknowledgementNumber() {
		return acknowledgementNumber;
	}
	public void setAcknowledgementNumber(String acknowledgementNumber) {
		this.acknowledgementNumber = acknowledgementNumber;
	}
	public String getHealthCardIDNumber() {
		return healthCardIDNumber;
	}
	public void setHealthCardIDNumber(String healthCardIDNumber) {
		this.healthCardIDNumber = healthCardIDNumber;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAcknowledgementType() {
		return acknowledgementType;
	}
	public void setAcknowledgementType(String acknowledgementType) {
		this.acknowledgementType = acknowledgementType;
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
	public String getIsDocumentUploaded() {
		return isDocumentUploaded;
	}
	public void setIsDocumentUploaded(String isDocumentUploaded) {
		this.isDocumentUploaded = isDocumentUploaded;
	}
	public Long getAckDocKey() {
		return ackDocKey;
	}
	public void setAckDocKey(Long ackDocKey) {
		this.ackDocKey = ackDocKey;
	}
	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}
	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
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
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
}
