package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class FVRGradingDTO extends AbstractTableDTO implements Serializable{
	private static final long serialVersionUID = -5445925456818406055L;

	private Long key;
	
	private String category;
	
	private String representativeCode;
	
	private String representativeName;
	
	private String applicability;
	
	private String statusFlag;
	
	
	private Integer slNo;
		
	private BeanItemContainer<SelectValue> commonValues = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	@NotNull(message = "Please Select Status in FVR Grading")
	private SelectValue status;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
		this.status = new SelectValue();
		this.status.setId((this.statusFlag != null && this.statusFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public SelectValue getStatus() {
		return status;
	}

	public void setStatus(SelectValue status) {
		this.status = status;
		this.statusFlag = this.status != null && this.status.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getRepresentativeCode() {
		return representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getApplicability() {
		return applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public BeanItemContainer<SelectValue> getCommonValues() {
		return commonValues;
	}

	public void setCommonValues(BeanItemContainer<SelectValue> commonValues) {
		this.commonValues = commonValues;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}



	

	
}
