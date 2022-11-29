package com.shaic.newcode.wizard.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Insured;
@Deprecated
public class IntimationDetailsDto  implements Serializable{

	
	private static final long serialVersionUID = -1473039396798808208L;

	private SelectValue genderId;
	
	private SelectValue relationshipWithInsuredId;
	
	@NotNull(message = "Please Choose Mode Of Intimation.")
	private SelectValue modeOfIntimation;
	
	@NotNull(message = "Please Choose Intimated By.")
	private SelectValue intimatedBy;
	
//	@Pattern (regexp="[\\s|^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$]", message="Please Enter a valid Email")
	private String email;

	@NotNull(message = "Please Enter Caller Address.")
	@Size(min = 1, message = "Please Enter Caller Address.")
	private String callerAddress;
	
	
	@NotNull(message = "Please Enter Caller Contact Number.")
	@Size(min = 1, message = "Please Enter Caller Contact Number.")
	@Pattern (regexp="(^[0-9]*)$", message="Please Enter a valid Caller Contact Number")
	private String callerContactNum;
	
	
	@NotNull(message = "Please Enter Attender's Contact Number.")
	@Size(min = 1, message = "Please Enter Attender's Contact Number.")
	@Pattern (regexp="(^[0-9]*)$", message="Please Enter a valid Attender Contact Number")
	private String attenderContactNum;
	
	@NotNull(message = "Please Select Insured Patient Name")
	private Insured insuredPatientId;
	
	private String insuredPatientName;
	
	private Boolean newBornFlag;
	
	@NotNull(message = "Please Enter Reason For Admission.")
	@Size(min = 1, message = "Please Enter Reason For Admission.")
	private String reasonForAdmission;
	
	private FullNameDTO intimatorName;

	private List<NewBornBabyDetailsDTO> babiesDetails;
	
	public IntimationDetailsDto()
	{
		this.genderId = new SelectValue();
		
		this.relationshipWithInsuredId = new SelectValue();
		this.modeOfIntimation = new SelectValue();
		this.intimatedBy = new SelectValue();
		
		this.babiesDetails = new ArrayList<NewBornBabyDetailsDTO>();
		this.intimatorName = new FullNameDTO();
		this.insuredPatientId = new Insured();
		this.modeOfIntimation = new SelectValue();
	}
	
	public FullNameDTO getIntimatorName() {
		return intimatorName;
	}

	public void setIntimatorName(FullNameDTO intimatorName) {
		this.intimatorName = intimatorName;
	}
	

	public SelectValue getModeOfIntimation() {
		return modeOfIntimation;
	}

	public void setModeOfIntimation(SelectValue modeOfIntimation) {
		this.modeOfIntimation = modeOfIntimation;
	}

	public SelectValue getIntimatedBy() {
		return intimatedBy;
	}

	public String getEmail() {
		return email;
	}

	public String getCallerContactNum() {
		return callerContactNum;
	}

	public String getAttenderContactNum() {
		return attenderContactNum;
	}

	public Insured getInsuredPatientId() {
		return insuredPatientId;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public Boolean getNewBornFlag() {
		return newBornFlag;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public List<NewBornBabyDetailsDTO> getBabiesDetails() {
		return babiesDetails;
	}

	public void setIntimatedBy(SelectValue intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCallerContactNum(String callerContactNum) {
		this.callerContactNum = callerContactNum;
	}

	public void setAttenderContactNum(String attenderContactNum) {
		this.attenderContactNum = attenderContactNum;
	}

	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
	}
	
	public void setInsuredPatientId(Insured insuredPatientId) {
		this.insuredPatientId = insuredPatientId;
	}

	public void setNewBornFlag(Boolean newBornFlag) {
		this.newBornFlag = newBornFlag;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public void setBabiesDetails(List<NewBornBabyDetailsDTO> babiesDetails) {
		this.babiesDetails = babiesDetails;
	}

	public SelectValue getGenderId() {
		return genderId;
	}

	public void setGenderId(SelectValue genderId) {
		this.genderId = genderId;
	}

	public SelectValue getRelationshipWithInsuredId() {
		return relationshipWithInsuredId;
	}

	public void setRelationshipWithInsuredId(SelectValue relationshipWithInsuredId) {
		this.relationshipWithInsuredId = relationshipWithInsuredId;
	}
	
	public void clearValues()
	{
		this.genderId = null;
		this.relationshipWithInsuredId = new SelectValue();
		this.modeOfIntimation = new SelectValue();
		this.intimatedBy = new SelectValue();
		this.insuredPatientId = new Insured();
		this.email = "";
		this.callerAddress  = "";
		this.callerContactNum  = "";
		this.attenderContactNum = "";
		this.insuredPatientName = "";
		this.newBornFlag = false;
		this.reasonForAdmission = "";
		this.intimatorName.clearValues();
		this.babiesDetails.clear();
	}
}
