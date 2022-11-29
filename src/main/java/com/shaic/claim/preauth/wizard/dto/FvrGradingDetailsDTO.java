package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;

public class FvrGradingDetailsDTO implements Serializable {
	private static final long serialVersionUID = 5963619633455617631L;

	private Long key;
	
	private Integer number;
	
	private String representiveCode;
	
	private String representativeName;
	
	private List<FVRGradingDTO> fvrGradingDTO;
	
	private List<NewFVRGradingDTO> newFvrGradingDTO;
	
	private Boolean isSegmentANotEdit;
	
	private Boolean isSegmentBNotEdit;
	
	private Boolean isSegmentCNotEdit;
	
	private Boolean isClearAllEnabled;
	
	private Boolean isFVRReceived;
	
	private String gradingRemarks;
	
	private Boolean isFVRReplied;

	public Long getKey() {
		return key;
	}

	public FvrGradingDetailsDTO() {
		fvrGradingDTO = new ArrayList<FVRGradingDTO>();
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRepresentiveCode() {
		return representiveCode;
	}

	public void setRepresentiveCode(String representiveCode) {
		this.representiveCode = representiveCode;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public List<FVRGradingDTO> getFvrGradingDTO() {
		return fvrGradingDTO;
	}

	public void setFvrGradingDTO(List<FVRGradingDTO> fvrGradingDTO) {
		this.fvrGradingDTO = fvrGradingDTO;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<NewFVRGradingDTO> getNewFvrGradingDTO() {
		return newFvrGradingDTO;
	}

	public void setNewFvrGradingDTO(List<NewFVRGradingDTO> newFvrGradingDTO) {
		this.newFvrGradingDTO = newFvrGradingDTO;
	}

	public Boolean getIsSegmentBNotEdit() {
		return isSegmentBNotEdit;
	}

	public void setIsSegmentBNotEdit(Boolean isSegmentBNotEdit) {
		this.isSegmentBNotEdit = isSegmentBNotEdit;
	}

	public Boolean getIsSegmentCNotEdit() {
		return isSegmentCNotEdit;
	}

	public void setIsSegmentCNotEdit(Boolean isSegmentCNotEdit) {
		this.isSegmentCNotEdit = isSegmentCNotEdit;
	}

	public Boolean getIsSegmentANotEdit() {
		return isSegmentANotEdit;
	}

	public void setIsSegmentANotEdit(Boolean isSegmentANotEdit) {
		this.isSegmentANotEdit = isSegmentANotEdit;
	}

	public Boolean getIsClearAllEnabled() {
		return isClearAllEnabled;
	}

	public void setIsClearAllEnabled(Boolean isClearAllEnabled) {
		this.isClearAllEnabled = isClearAllEnabled;
	}

	public Boolean getIsFVRReceived() {
		return isFVRReceived;
	}

	public void setIsFVRReceived(Boolean isFVRReceived) {
		this.isFVRReceived = isFVRReceived;
	}

	public String getGradingRemarks() {
		return gradingRemarks;
	}

	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}

	public Boolean getIsFVRReplied() {
		return isFVRReplied;
	}

	public void setIsFVRReplied(Boolean isFVRReplied) {
		this.isFVRReplied = isFVRReplied;
	}

	

	
	
}
