/**
 * 
 */
package com.shaic.reimbursement.rod.enterbilldetails.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchEnterBillDetailTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String insuredPatientName;
	private Long cpuCode;
	private String claimType;
	private String hospitalName;
	private String hospitalCity;
	private Date dateOfAdmission1;
	private String dateOfAdmission;
	private Long hospitalNameID;
	private String hospitalType;
	private String reasonForAdmission;
	private Long cpuId;
	private Long claimkey;
	private String strCpuCode;
	
	private String paPatientName;
	private Long productKey;
	
	private Long rodKey;
	private Long ackKey;
	private String source;
	private String accidentOrDeath;

	private String crmFlagged;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
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
	
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public Long getClaimkey() {
		return claimkey;
	}
	public void setClaimkey(Long claimkey) {
		this.claimkey = claimkey;
	}
	
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	public Long getAckKey() {
		return ackKey;
	}
	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	
	
}
