/**
 * 
 */
package com.shaic.claim.reimbursement.dto;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

/**
 * @author ntv.vijayar
 *
 */
public class ExtraEmployeeEffortDTO {
	
	
	private Long slNo;
	
	private String employeeId;
	
	private String employeeName;
	
	private SelectValue selEmployeeName;
	
	private SelectValue selEmployeeId;
	
	private String selEmployeeNameValue;
	
	private SelectValue creditType;
	
	private Long creditTypeValue;
	
	private SelectValue typeOfContributor;
	
	private String typeOfContributorValue;
	
	private Long score;
	
	private String remarks;
	
	private EmployeeMasterDTO employeeNameDTO;
	
	private SelectValue category;
	
	private Long rrcRequestKey;
	
	private String employeeZone;
	
	private String employeeDept;
	
	private Long rrcCategoryKey;
	
	private Long rrcDetailsKey;
	
	private Long empScore;
	
	private SelectValue subCategory;
	
	private SelectValue sourceOfIdentification;
	
	private String employee;
	
	private Long categoryKey;
	
	private String talkSpokento;
	
	private Date talkSpokenDate;
	
	private String talkMobto;

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		//StringBuffer strEmpBuffer = 
		//return employeeFirstName + employeeMiddleName + employeeLastName;
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public SelectValue getCreditType() {
		return creditType;
	}

	public void setCreditType(SelectValue creditType) {
		this.creditType = creditType;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public EmployeeMasterDTO getEmployeeNameDTO() {
		return employeeNameDTO;
	}

	public void setEmployeeNameDTO(EmployeeMasterDTO employeeNameDTO) {
		this.employeeNameDTO = employeeNameDTO;
	}

	public SelectValue getCategory() {
		return category;
	}

	public void setCategory(SelectValue category) {
		this.category = category;
	}

	public Long getRrcRequestKey() {
		return rrcRequestKey;
	}

	public void setRrcRequestKey(Long rrcRequestKey) {
		this.rrcRequestKey = rrcRequestKey;
	}

	public String getEmployeeZone() {
		return employeeZone;
	}

	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}

	public String getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(String employeeDept) {
		this.employeeDept = employeeDept;
	}

	public Long getRrcCategoryKey() {
		return rrcCategoryKey;
	}

	public void setRrcCategoryKey(Long rrcCategoryKey) {
		this.rrcCategoryKey = rrcCategoryKey;
	}

	public Long getRrcDetailsKey() {
		return rrcDetailsKey;
	}

	public void setRrcDetailsKey(Long rrcDetailsKey) {
		this.rrcDetailsKey = rrcDetailsKey;
	}

	public SelectValue getSelEmployeeName() {
		return selEmployeeName;
	}

	public void setSelEmployeeName(SelectValue selEmployeeName) {
		this.selEmployeeName = selEmployeeName;
	}

	public Long getEmpScore() {
		return empScore;
	}

	public void setEmpScore(Long empScore) {
		if(null != empScore)
		{
			String strScore = String.valueOf(empScore);
		//	setScore(strScore);
		}
		this.empScore = empScore;
	}

	public Long getCreditTypeValue() {
		return creditTypeValue;
	}

	public void setCreditTypeValue(Long creditTypeValue) {
		if(null != creditTypeValue)
		{
			if(null != creditType)
				creditType.setId(creditTypeValue);
		}
		this.creditTypeValue = creditTypeValue;
	}

	public String getSelEmployeeNameValue() {
		return selEmployeeNameValue;
	}

	public void setSelEmployeeNameValue(String selEmployeeNameValue) {
		
		if(null != selEmployeeNameValue)
		{
			if(null != selEmployeeName)
				selEmployeeName.setValue(selEmployeeNameValue);
		}
		
		this.selEmployeeNameValue = selEmployeeNameValue;
	}

	public SelectValue getTypeOfContributor() {
		return typeOfContributor;
	}

	public void setTypeOfContributor(SelectValue typeOfContributor) {
		this.typeOfContributor = typeOfContributor;
	}

	public String getTypeOfContributorValue() {
		return typeOfContributorValue;
	}

	public void setTypeOfContributorValue(String typeOfContributorValue) {
		this.typeOfContributorValue = typeOfContributorValue;
	}

	public SelectValue getSelEmployeeId() {
		return selEmployeeId;
	}

	public void setSelEmployeeId(SelectValue selEmployeeId) {
		this.selEmployeeId = selEmployeeId;
	}

	public SelectValue getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SelectValue subCategory) {
		this.subCategory = subCategory;
	}

	public SelectValue getSourceOfIdentification() {
		return sourceOfIdentification;
	}

	public void setSourceOfIdentification(SelectValue sourceOfIdentification) {
		this.sourceOfIdentification = sourceOfIdentification;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public Long getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(Long categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getTalkSpokento() {
		return talkSpokento;
	}

	public void setTalkSpokento(String talkSpokento) {
		this.talkSpokento = talkSpokento;
	}

	public Date getTalkSpokenDate() {
		return talkSpokenDate;
	}

	public void setTalkSpokenDate(Date talkSpokenDate) {
		this.talkSpokenDate = talkSpokenDate;
	}

	public String getTalkMobto() {
		return talkMobto;
	}

	public void setTalkMobto(String talkMobto) {
		this.talkMobto = talkMobto;
	}

}
