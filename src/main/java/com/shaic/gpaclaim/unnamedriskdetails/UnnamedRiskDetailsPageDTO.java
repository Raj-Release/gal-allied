package com.shaic.gpaclaim.unnamedriskdetails;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class UnnamedRiskDetailsPageDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	
	private String policyNo;
	
	private String organisationName;
	
	private Double sumInsured;
	
	private String gpaSection;
	
	private String gpaParentName;
	
	private Date gpaParentDOB;	
	
	private String gpaRiskName;	
	
	private String gpaCategory;
	
	private String gpaCategoryDescription;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getGpaSection() {
		return gpaSection;
	}

	public void setGpaSection(String gpaSection) {
		this.gpaSection = gpaSection;
	}

	public String getGpaParentName() {
		return gpaParentName;
	}

	public void setGpaParentName(String gpaParentName) {
		this.gpaParentName = gpaParentName;
	}

	public Date getGpaParentDOB() {
		return gpaParentDOB;
	}

	public void setGpaParentDOB(Date gpaParentDOB) {
		this.gpaParentDOB = gpaParentDOB;
	}

	public String getGpaRiskName() {
		return gpaRiskName;
	}

	public void setGpaRiskName(String gpaRiskName) {
		this.gpaRiskName = gpaRiskName;
	}

	public String getGpaCategory() {
		return gpaCategory;
	}

	public void setGpaCategory(String gpaCategory) {
		this.gpaCategory = gpaCategory;
	}

	public String getGpaCategoryDescription() {
		return gpaCategoryDescription;
	}

	public void setGpaCategoryDescription(String gpaCategoryDescription) {
		this.gpaCategoryDescription = gpaCategoryDescription;
	}
	
	
	
}
