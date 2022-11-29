package com.shaic.claim.fieldvisit.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchFieldVisitTableDTO extends AbstractTableDTO implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long key;
	private String policyNo;
	private String insuredPatiendName;
	private String intimaterName;
	private Long cpuCode;
	private String hospitalName;
	private String intimationMode;
	private String intimatedBy;
	private String admissionType;
	private Date intimationDate;
	private String callerMobileNumber;
	private String hospitalAddress;
	private String hospitalCity;
	private String reasonForAdmission;
	private String hospitalType;
	private Long cpuId;
    private String strDateOfAdmission;
    private String strDateOfIntimation;
    private Date dateOfAdmission;
	private Long hospitalNameID;
	private Long fvrKey;
	private Long rodKey;
	
	private Long fvrCpuCode;
	
	private String strFvrCpuCode;
	
	private String strCpuCode;

	private String intimationNo;
	
	private String claimNo;
	
	private String lob;
	
	private String insuredPatientName;
	
	private String cpucode;
	
	private String productname;
	
	private String receivedate;
	
	private String receiveddateandtime;
	
	private String reasonforadmission;
	
	private RRCDTO rrcDTO;
	
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

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	
	public String getIntimaterName() {
		return intimaterName;
	}

	public void setIntimaterName(String intimaterName) {
		this.intimaterName = intimaterName;
	}

	public String getCpucode() {
		return cpucode;
	}

	public void setCpucode(String cpucode) {
		this.cpucode = cpucode;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(String receivedate) {
		this.receivedate = receivedate;
	}

	public String getReceiveddateandtime() {
		return receiveddateandtime;
	}

	public void setReceiveddateandtime(String receiveddateandtime) {
		this.receiveddateandtime = receiveddateandtime;
	}

	public String getReasonforadmission() {
		return reasonforadmission;
	}

	public void setReasonforadmission(String reasonforadmission) {
		this.reasonforadmission = reasonforadmission;
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

	public String getInsuredPatiendName() {
		return insuredPatiendName;
	}

	public void setInsuredPatiendName(String insuredPatiendName) {
		this.insuredPatiendName = insuredPatiendName;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	public String getIntimationMode() {
		return intimationMode;
	}

	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}

	public String getIntimatedBy() {
		return intimatedBy;
	}

	public void setIntimatedBy(String intimatedBy) {
		this.intimatedBy = intimatedBy;
	}
	
	public String getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}
	
	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getCallerMobileNumber() {
		return callerMobileNumber;
	}

	public void setCallerMobileNumber(String callerMobileNumber) {
		this.callerMobileNumber = callerMobileNumber;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
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

	public Long getHospitalNameID() {
		return hospitalNameID;
	}

	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getStrCpuCode() {
		return strCpuCode;
	}

	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
	}

	public String getStrDateOfAdmission() {
		return strDateOfAdmission;
	}

	public void setStrDateOfAdmission(String strDateOfAdmission) {
		this.strDateOfAdmission = strDateOfAdmission;
	}
	
	public String getStrDateOfIntimation() {
		return strDateOfIntimation;
	}

	public void setStrDateOfIntimation(String strDateOfIntimation) {
		this.strDateOfIntimation = strDateOfIntimation;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getStrFvrCpuCode() {
		return strFvrCpuCode;
	}

	public void setStrFvrCpuCode(String strFvrCpuCode) {
		this.strFvrCpuCode = strFvrCpuCode;
	}

	public void setFvrCpuCode(Long fvrCpuCode) {
		this.fvrCpuCode = fvrCpuCode;
	}

	public Long getFvrCpuCode() {
		return fvrCpuCode;
	}

	
	
}
