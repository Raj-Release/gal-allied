package com.shaic.claim.userproduct.document;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;

public class ProductAndDocumentTypeDTO {
	
	private String doctorName;
	
	private String doctorId;
	
	private String role;
	
	//@NotNull(message = "Please select Status.")
	private SelectValue status;
	
	@NotNull(message = "Please select User Type.")
	private SelectValue type;
	
	//@NotNull(message = "Please Enter Queue Count.")
	private Integer queueCount; 
	
	/*@NotNull(message = "Please Enter Window Star Time.")
	@Size(min = 1,message = "Please Enter Window Star Time.")*/
	private String startTime;
	
	/*@NotNull(message = "Please Enter Window End Time.")
	@Size(min = 1,message = "Please Enter Window End Time.")*/
	private String endTime;
	
	private Long minAmount;
	
	private Long maxAmount;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Boolean aaplicanleCheckBoxValue;
	
	private List<ApplicableCpuDTO> applicableCpuList;
	
	private List<ProductDocTypeDTO> productsList;
	
	private List<ApplicableCpuDTO> userCpuList;

	private Boolean highValueClaim;
	
	private String shiftStartTime;
	
	private String shiftEndTime;
	
	
	
	public String getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public String getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	

	public Boolean getHighValueClaim() {
		return highValueClaim;
	}

	public void setHighValueClaim(Boolean highValueClaim) {
		this.highValueClaim = highValueClaim;
	}

	public SelectValue getStatus() {
		return status;
	}

	public void setStatus(SelectValue status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Long getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Long minAmount) {
		this.minAmount = minAmount;
	}

	public Long getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
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

	public List<ApplicableCpuDTO> getApplicableCpuList() {
		return applicableCpuList;
	}

	public void setApplicableCpuList(List<ApplicableCpuDTO> applicableCpuList) {
		this.applicableCpuList = applicableCpuList;
	}

	public List<ProductDocTypeDTO> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<ProductDocTypeDTO> productsList) {
		this.productsList = productsList;
	}

	public Boolean getAaplicanleCheckBoxValue() {
		return aaplicanleCheckBoxValue;
	}

	public void setAaplicanleCheckBoxValue(Boolean aaplicanleCheckBoxValue) {
		this.aaplicanleCheckBoxValue = aaplicanleCheckBoxValue;
	}

	public Integer getQueueCount() {
		return queueCount;
	}

	public void setQueueCount(Integer queueCount) {
		this.queueCount = queueCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public SelectValue getType() {
		return type;
	}

	public void setType(SelectValue type) {
		this.type = type;
	}
	
	

}
