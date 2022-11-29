package com.shaic.claim.reports.fvrassignmentreport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class FVRAssignmentReportTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String intimationNo;
	private Date dateofAdmission;
	private String dateofAdmissionValue;
	private String admissionType;
	private String patientName;
	private String hospitalName;
	private String location;
	private String claimType;	
	private String representativeType;
	private String pointToFocus;
	private String representativeId;
	private String representativeName;
	private String fvrAssigneeName;
	private Date fvrDate;
	private String fvrDateValue;
	private String fvrTime;	
	private Date fvrReceivedDate;
	private String fvrReceivedDateValue;
	private String fvrReceivedTime;
	private String fvrAssignedTime;
	private String cpuCode;
	private String tat;
	private String fvrExecutiveComments;
	private String fvrNotRequiredComments;
	private String hospitalCode;
	private Long hospitalNameId;
	private Long transactionKey;
	private Long preauthKey; 
	private Long intimationKey;
	private Date fvrAssignedDate;
	private String fvrAssignedDateValue;
	private String fvrCpuCode;
	private String hospitalType;
	private String initiatorName;
	private String initiatorId;
	private String productName;	
	
	
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getInitiatorName() {
		return initiatorName;
	}
	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}
	public String getInitiatorId() {
		return initiatorId;
	}
	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}
	public String getFvrCpuCode() {
		return fvrCpuCode;
	}
	public void setFvrCpuCode(String fvrCpuCode) {
		this.fvrCpuCode = fvrCpuCode;
	}
	public String getFvrAssignedTime() {
		return fvrAssignedTime;
	}
	public void setFvrAssignedTime(String fvrAssignedTime) {
		this.fvrAssignedTime = fvrAssignedTime;
	}
	
	
	public Long getPreauthKey() {
		return preauthKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Date getDateofAdmission() {
		return dateofAdmission;
	}
	public void setDateofAdmission(Date dateofAdmission) {
		if(dateofAdmission !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateofAdmission);
			setDateofAdmissionValue(dateformat);
		    this.dateofAdmission = dateofAdmission;
		}
	}
	public String getDateofAdmissionValue() {
		return dateofAdmissionValue;
	}
	public void setDateofAdmissionValue(String dateofAdmissionValue) {
		this.dateofAdmissionValue = dateofAdmissionValue;
	}
	public Date getFvrAssignedDate() {
		return fvrAssignedDate;
	}
	public void setFvrAssignedDate(Date fvrAssignedDate) {
		if(fvrAssignedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(fvrAssignedDate);
			setFvrAssignedDateValue(dateformat);
		    this.fvrAssignedDate = fvrAssignedDate;
		}
	}
	public String getFvrAssignedDateValue() {
		return fvrAssignedDateValue;
	}
	public void setFvrAssignedDateValue(String fvrAssignedDateValue) {
		this.fvrAssignedDateValue = fvrAssignedDateValue;
	}
	public String getAdmissionType() {
		return admissionType;
	}
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getRepresentativeType() {
		return representativeType;
	}
	public void setRepresentativeType(String representativeType) {
		this.representativeType = representativeType;
	}
	public String getPointToFocus() {
		return pointToFocus;
	}
	public void setPointToFocus(String pointToFocus) {
		this.pointToFocus = pointToFocus;
	}
	public String getRepresentativeId() {
		return representativeId;
	}
	public void setRepresentativeId(String representativeId) {
		this.representativeId = representativeId;
	}
	public String getRepresentativeName() {
		return representativeName;
	}
	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}
	public String getFvrAssigneeName() {
		return fvrAssigneeName;
	}
	public void setFvrAssigneeName(String fvrAssigneeName) {
		this.fvrAssigneeName = fvrAssigneeName;
	}
	public Date getFvrDate() {
		return fvrDate;
	}
	public void setFvrDate(Date fvrDate) {
		if(fvrDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(fvrDate);
			setFvrDateValue(dateformat);			
		     this.fvrDate = fvrDate;
		}
	}
	public String getFvrDateValue() {
		return fvrDateValue;
	}
	public void setFvrDateValue(String fvrDateValue) {
		this.fvrDateValue = fvrDateValue;
	}
	public String getFvrTime() {
		
		return fvrTime;
	}
	public void setFvrTime(String fvrTime) {		
		
		this.fvrTime = fvrTime;
	}
	
	public Date getFvrReceivedDate() {
		return fvrReceivedDate;
	}
	public void setFvrReceivedDate(Date fvrReceivedDate) {
		if(fvrReceivedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(fvrReceivedDate);
			setFvrReceivedDateValue(dateformat);
		    this.fvrReceivedDate = fvrReceivedDate;
		}
	}
	public String getFvrReceivedDateValue() {
		return fvrReceivedDateValue;
	}
	public void setFvrReceivedDateValue(String fvrReceivedDateValue) {
		this.fvrReceivedDateValue = fvrReceivedDateValue;
	}
	public String getFvrReceivedTime() {
				return fvrReceivedTime;
	}
	public void setFvrReceivedTime(String fvrReceivedTime) {
		
		this.fvrReceivedTime = fvrReceivedTime;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getTat() {
		return tat;
	}
	public void setTat(String tat) {
		this.tat = tat;
	}
	public String getFvrExecutiveComments() {
		return fvrExecutiveComments;
	}
	public void setFvrExecutiveComments(String fvrExecutiveComments) {
		this.fvrExecutiveComments = fvrExecutiveComments;
	}
	public String getFvrNotRequiredComments() {
		return fvrNotRequiredComments;
	}
	public void setFvrNotRequiredComments(String fvrNotRequiredComments) {
		this.fvrNotRequiredComments = fvrNotRequiredComments;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public Long getHospitalNameId() {
		return hospitalNameId;
	}
	public void setHospitalNameId(Long hospitalNameId) {
		this.hospitalNameId = hospitalNameId;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
