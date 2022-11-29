package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class NewFVRGradingDTO extends AbstractTableDTO implements Serializable{
	
	private Long key;
	
	private String category;
	
	private String representativeCode;
	
	private String representativeName;
	
	private Boolean selectFlag;
	
	private BeanItemContainer<SelectValue> commonValues = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private String statusFlag;
	
	private Boolean checkFlag;
	
	private String statusFlagSegmentC;
	
	private Boolean checkFlagA;
	
	private String statusFlagSegmentA;
	
	private String segment;
	
	private Boolean isEditAB;
	
	private Long fvrSeqNo;
	
	private Boolean IsEditABC = Boolean.TRUE;
	
	private Boolean isAssignFVRNotReceived;
	
	private Boolean isFVRReceived;
	
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

	public BeanItemContainer<SelectValue> getCommonValues() {
		return commonValues;
	}

	public void setCommonValues(BeanItemContainer<SelectValue> commonValues) {
		this.commonValues = commonValues;
	}

	public Boolean getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(Boolean selectFlag) {
		this.selectFlag = selectFlag;
		this.statusFlag = this.selectFlag != null && this.selectFlag ? "Y" : "N";
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
		this.selectFlag = ((this.statusFlag != null && this.statusFlag
				.equalsIgnoreCase("Y")) ? true : false);
	}

	public Boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
		this.statusFlagSegmentC = this.checkFlag != null && this.checkFlag ? "Y" : "N";
	}

	public String getStatusFlagSegmentC() {
		return statusFlagSegmentC;
	}

	public void setStatusFlagSegmentC(String statusFlagSegmentC) {
		this.statusFlagSegmentC = statusFlagSegmentC;
		this.checkFlag = ((this.statusFlagSegmentC != null && this.statusFlagSegmentC
				.equalsIgnoreCase("Y")) ? true : false);
	}

	public Boolean getCheckFlagA() {
		return checkFlagA;
	}

	public void setCheckFlagA(Boolean checkFlagA) {
		this.checkFlagA = checkFlagA;
		this.statusFlagSegmentA = this.checkFlagA != null && this.checkFlagA ? "Y" : "N";
	}

	public String getStatusFlagSegmentA() {
		return statusFlagSegmentA;
	}

	public void setStatusFlagSegmentA(String statusFlagSegmentA) {
		this.statusFlagSegmentA = statusFlagSegmentA;
		this.checkFlagA = ((this.statusFlagSegmentA != null && this.statusFlagSegmentA
				.equalsIgnoreCase("Y")) ? true : false);
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public Boolean getIsEditAB() {
		return isEditAB;
	}

	public void setIsEditAB(Boolean isEditAB) {
		this.isEditAB = isEditAB;
	}

	public Long getFvrSeqNo() {
		return fvrSeqNo;
	}

	public void setFvrSeqNo(Long fvrSeqNo) {
		this.fvrSeqNo = fvrSeqNo;
	}

	public Boolean getIsEditABC() {
		return IsEditABC;
	}

	public void setIsEditABC(Boolean isEditABC) {
		IsEditABC = isEditABC;
	}

	public Boolean getIsAssignFVRNotReceived() {
		return isAssignFVRNotReceived;
	}

	public void setIsAssignFVRNotReceived(Boolean isAssignFVRNotReceived) {
		this.isAssignFVRNotReceived = isAssignFVRNotReceived;
	}

	public Boolean getIsFVRReceived() {
		return isFVRReceived;
	}

	public void setIsFVRReceived(Boolean isFVRReceived) {
		this.isFVRReceived = isFVRReceived;
	}	
}
