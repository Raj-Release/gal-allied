package com.shaic.newcode.wizard.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Insured;
import com.shaic.domain.MastersEvents;
//import com.shaic.domain.Locality;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCoverDetails;
import com.shaic.domain.Product;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.domain.preauth.Stage;

public class NewIntimationDto extends AbstractTableDTO implements Serializable {

	private Long key;

	private String intimationId;

	private String claimNumber;
	
	private String dateOfIntimation;

	private String productName;

	private String insuredPatientName;

	private String patientName;

	private String hospitalName;

	private String hospitalCity;
	
	private String intimationMode;
	
	private String patientNotCoveredName;
	
	private String healthCardNo;
	
	private String hospitalIrdaCode;
	
	private String hospitalInternalCode;
	
	private SelectValue claimType;
	
	private String policyYear;

	private Double balanceSI;
	
	private String copayFlag;

	

	@NotNull(message = "Please Enter Reason For Admission.")
	@Size(min = 1, message = "Please Enter Reason For Admission.")
	private String reasonForAdmission;

	@NotNull(message = "Please choose Admission Date.")
	private Date admissionDate;
	
	private Long policyKey;
	
	private Long insuredKey;

	private Product product;
	
	private Double orginalSI;

	private Policy policy;
	
	private String policyNumber;

	private SelectValue policyType;

	private SelectValue productType;

	private SelectValue genderId;

	private String insuredAge;
	
	private String policyAgeing;

	private SelectValue relationshipWithInsuredId;

	@NotNull(message = "Please Choose Mode Of Intimation.")
	private SelectValue modeOfIntimation;

	@NotNull(message = "Please Choose Intimated By.")
	private SelectValue intimatedBy;

	// @Pattern
	// (regexp="[\\s|^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$]",
	// message="Please Enter a valid Email")
	private String email;

	@NotNull(message = "Please Enter Caller Contact Number.")
	@Size(min = 8, max = 12, message = "Please Enter a valid Caller Contact Number.")
	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Caller Contact Number")
	private String callerContactNum;

	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Caller Landline Number")
	private String callerLandlineNum;

	@NotNull(message = "Please Select Relapse of Illness.")
	private SelectValue relapseofIllness;

	private String relapseofIllnessValue;

	@NotNull(message = "Please Enter Attender's Contact Number.")
	@Size(min = 8, max = 12, message = "Please Enter Attender's Contact Number.")
	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Attender Contact Number")
	private String attenderContactNum;

	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Attender Landline Number")
	private String attenderLandlineNum;
	
	@Size(min = 1, message = "Please Enter Student Patient Name.")
	@NotNull(message = "Please Enter Student Patient Name.")
	private String studentPatientName;
	
	private Integer studentAge;
	
	//@NotNull(message = "Please Enter Student Date of Birth")
	private Date studentDOB;
	
	// @NotNull(message = "Please Select Insured Patient Name")
	// private TmpInsured insuredPatientId;

//	@NotNull(message = "Please Select Insured Patient Name")
	private Insured insuredPatient;

	private List<PolicyCoverDetails> policyCoverDetails;
	
	 private String gmcMainMemberName;
	 
	 private List<GmcMainMemberList> gmcInsuredList;
	 
	 private GmcMainMemberList selectedGmcInsuredList;
	 private Long gmcMainMemberId;
	 
	 private Boolean isClaimManuallyProcessed = Boolean.FALSE;
	 
	 private String riskId;
	 
	 private PremPolicy policySummary;
	 
	 private String policyServiceType;
	 
	 private Boolean isDeletedRisk = false;
	 
	 private Date paPatientDOB;
	 
	 private Integer paPatientAge;
	 
	 private String diagnosis;

		public Boolean getIsClaimManuallyProcessed() {
			return isClaimManuallyProcessed;
		}

		public void setIsClaimManuallyProcessed(Boolean isClaimManuallyProcessed) {
			this.isClaimManuallyProcessed = isClaimManuallyProcessed;
		}


	public Insured getInsuredPatient() {
		return insuredPatient;
	}

	public void setInsuredPatient(Insured insuredPatient) {
		this.insuredPatient = insuredPatient;
	}

	private Boolean newBornFlag;
	
	private String dummy;
	
	private Boolean dummyFlag;

	@NotNull(message = "Please Enter Intimater Name.")
	@Size(min = 1, message = "Please Enter Intimater Name.")
	@Pattern(regexp = "^[a-zA-Z./' ]*$", message = "Please Enter a valid Intimater Name")
	private String intimaterName;

	// private TmpPolicy tmpPolicy;

	private List<NewBornBabyDetailsDTO> babiesDetails;
	
	private Status status;
	
	private Long statusKey;

	private Stage stage;

	private Date createdDate;

	private Long cpuId;
	
	private String sumInsured;
	
	private String cpuAddress;
	
	private String reimbCpuAddress;

	// @Pattern(regexp="^[a-zA-Z0-9./' ]*$",
	// message="Please Enter a valid Name")
	// private String name;

	private SelectValue hospitalType;

	private String hospitalTypeValue;

	// @Pattern(regexp="^[a-zA-Z0-9./']*$",
	// message="Please Enter a valid State Name")
	private State state;
	
	private String icrEarnedPremium;
	
	private Boolean isPaayasPolicy = Boolean.FALSE;
	
	private Boolean isJioPolicy = Boolean.FALSE;
	
	private Boolean isTataPolicy = Boolean.FALSE;

	public Double getOrginalSI() {
		return orginalSI;
	}

	public void setOrginalSI(Double orginalSI) {
		this.orginalSI = orginalSI;
	}

	private CityTownVillage city;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	

//	private Locality area;

	private HospitalDto hospitalDto;

	@Size(max = 300)
	@Pattern(regexp = "^[a-zA-Z0-9./' ]*$", message = "Please Enter a valid Doctor Name")
	private String doctorName;

//	@NotNull(message = "Please Select Management Type.")
	private SelectValue managementType;

	//@NotNull(message = "Please Select Room Category.")
	private SelectValue roomCategory;

	@Size(max = 4000)
	private String hospitalComments;
	
	@Size(max = 4000)
	private String comments;

	@NotNull(message = "Please Select Admission Type")
	private SelectValue admissionType;

	/*@NotNull(message = "Please Enter Inpatient Number")
	@Size(min = 1, message = "Please Enter Inpatient Number.")*/
	//@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Inpatient Number")
	private String inpatientNumber;

//	@NotNull(message = "Please Enter Reason for Late Intimation")
	private String lateIntimationReason;

	private List<NewBabyIntimationDto> newBabyIntimationListDto;

	private List<NewBabyIntimationDto> deletedBabyList;

	private String createdBy;
	
	private String hospitalAddress; 

	private MastersValue intimationSource;

	private Long cpuCode;

	private String smCode;

	private String smName;

	private String agentBrokerCode;

	private String agentBrokerName;

	private String registrationStatus;

	private String lineofBusiness;
	
	private String incidenceFlag;
	
	private String hospitalReqFlag;
	
	private Boolean hospitalReq;
	
	private Date accountDeactivatedDate;
	
	private String passportNumber;
	
	private Date lossDateTime;
		
	private String ailmentLoss;
	
	private String tpaIntimationNumber;
	
	private Long  eventCodeId;
	
	private String  hospitalizationFlag;
	
	private String  nonHospitalizationFlag;
	
	private String  placeVisit;
	
	private String  placeLossDelay;
	
	private String  sponsorName;
	
	private Long  countryId;
	
	private String  remarks;
	
	private Double  dollarInitProvisionAmt;
	
	private Double  inrConversionRate;
	
	private Double  inrTotalAmount;
	
	private Date  passportExpiryDate;
	
	private MastersEvents event;
	
	private String eventName;
	
	private String eventCode;
	
	private String plan;
	
	private SelectValue eventCodeValue;
	
	private Date intimationDate;
	
	private Date dischargeDate;

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	private SelectValue lobId;
	
	private String processClaimType;
	
	private Date accidentDeathDate;
	
	private String nomineeName;
	
	private String nomineeAddr;
	
	private String isPortablity;
	
	private Date policyInceptionDate ;

	/**
	 * following attributes Need to be removed from this Dto
	 */

	
	/*
	 * private String provisionAmt;
	 * 
	 * private String cashLessOrReimbersement;
	 * 
	 * private String claimCpuCode;
	 * 
	 * private String closeRemarks;
	 * 
	 * private String statusOfCashLess;
	 * 
	 * private String totalAuthAmt;
	 * 
	 * private String hospitalCode;//
	 * 
	 * private String idCardNo;
	 * 
	 * private String claimNo;
	 * 
	 * private String hospitalNetwork;//
	 * 
	 * private String fieldVisitDoctorName;
	 * 
	  private String policyNumber;
	 * 
	 * private String policyIssueOffice;
	 *//**
	 * following attributes Need to be removed from this Dto
	 */
	
	/*
	 * //redcolor reports 
	 * private String intimationMode; 
	 * private String intimatdBy;
	 * private String intimatorName; 
	 * private String hospitalPhnNo;
	 * private String hospitalAddress; 
	 * private String hospitalFaxNo; 
	 * private String roomCatgory;
	 */

	
	//private String roomCatgory;
	
	
	private String admissionDateForCarousel;

	private OrganaizationUnit organizationUnit;

	private OrganaizationUnit parentOrgUnit;
	
	
	@Size(min = 1, message = "Please Enter Parent Patient name.")
	@NotNull(message = "Please Enter Parent Patient name.")
	private String paParentName;
	
	private String paPatientName;
	
	//@NotNull(message = "Please Select Parent Date of Birth")
	private Date parentDOB;
	
	private Double parentAge;	
	
	private Double paSumInsured;

	private String employeeCode;
	
	private String relationShipWithEmployee;
	
	private String companyName;

	
	/*@Size(min = 1, message = "Please Enter Caller Address.")
	@NotNull(message = "Please Enter Caller Address.")*/
	private String callerAddress;
	
	private String callerEmail;
	
	private Date dateOfDischarge;
	
	private SelectValue insuredType;	
	
	private List<NomineeDetailsDto> nomineeList;
	
	private int nomineeSelectCount;

	private String insuredDeceasedFlag;
	
	private PaymentProcessCpuPageDTO paymentDto;
	
	private Integer activeStatus;
	
	private Double roomRentMaxAmount;
	
	private Double icuMaxAmount;
	
	private Integer copayMaxPer;
	
	private Integer sublimitMaxAmt;
	
	private Insured gmcMainMember;
	
	private String hrmContactNo;
	
	private String hrmUserName;
	
	private String hrmHeadName;
	
	private String hrmHeadContactNo;
	
	private String cscMemberNameContactNo;
	private Integer icdLetterStatus;
	
	private String tollNumber;

	private String whatsupNumber;

	private String pageFooter;
	
	
	public NewIntimationDto() {

		// this.tmpPolicy = new TmpPolicy();
		this.policy = new Policy();
		this.babiesDetails = new ArrayList<NewBornBabyDetailsDTO>();
		this.insuredPatient = new Insured();
		this.hospitalDto = new HospitalDto();
		this.organizationUnit = new OrganaizationUnit();
		this.parentOrgUnit = new OrganaizationUnit();
		this.newBornFlag = false;
		this.lobId = new SelectValue();
		this.nomineeList = new ArrayList<NomineeDetailsDto>();
		this.paymentDto = new PaymentProcessCpuPageDTO();
	}

	public OrganaizationUnit getOrganizationUnit() {
		return organizationUnit;
	}

	public void setOrganizationUnit(OrganaizationUnit organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public SelectValue getPolicyType() {
		return policyType;
	}

	public void setPolicyType(SelectValue policyType) {
		this.policyType = policyType;
	}

	public SelectValue getProductType() {
		return productType;
	}

	public void setProductType(SelectValue productType) {
		this.productType = productType;
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

	public void setRelationshipWithInsuredId(
			SelectValue relationshipWithInsuredId) {
		this.relationshipWithInsuredId = relationshipWithInsuredId;
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

	public String getLateIntimationReason() {
		return lateIntimationReason;
	}

	public void setLateIntimationReason(String lateIntimationReason) {
		this.lateIntimationReason = lateIntimationReason;
	}

	public void setIntimatedBy(SelectValue intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCallerContactNum() {
		return callerContactNum;
	}

	public void setCallerContactNum(String callerContactNum) {
		this.callerContactNum = callerContactNum;
	}

	public String getCallerLandlineNum() {
		return callerLandlineNum;
	}

	public void setCallerLandlineNum(String callerLandlineNum) {
		this.callerLandlineNum = callerLandlineNum;
	}

	public SelectValue getRelapseofIllness() {
		return relapseofIllness;
	}

	public void setRelapseofIllness(SelectValue relapseofIllness) {
		this.relapseofIllness = relapseofIllness;
	}

	public String getAttenderContactNum() {
		return attenderContactNum;
	}

	public void setAttenderContactNum(String attenderContactNum) {
		this.attenderContactNum = attenderContactNum;
	}

	public String getAttenderLandlineNum() {
		return attenderLandlineNum;
	}

	public void setAttenderLandlineNum(String attenderLandlineNum) {
		this.attenderLandlineNum = attenderLandlineNum;
	}

	/*
	 * public TmpInsured getInsuredPatient() { return insuredPatient; }
	 * 
	 * public void setInsuredPatientId(Insured insuredPatient) {
	 * this.insuredPatient = insuredPatient; }
	 */

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public Boolean getNewBornFlag() {
		return newBornFlag;
	}

	public void setNewBornFlag(Boolean newBornFlag) {
		this.newBornFlag = newBornFlag;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getIntimaterName() {
		return intimaterName;
	}

	public void setIntimaterName(String intimaterName) {
		this.intimaterName = intimaterName;
	}

	public List<NewBornBabyDetailsDTO> getBabiesDetails() {
		return babiesDetails;
	}

	public void setBabiesDetails(List<NewBornBabyDetailsDTO> babiesDetails) {
		this.babiesDetails = babiesDetails;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCpuCode() {
		return cpuCode != null ? cpuCode.toString() : "";
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public SelectValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public CityTownVillage getCity() {
		return city;
	}

	public void setCity(CityTownVillage city) {
		this.city = city;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
	
	public SelectValue getManagementType() {
		return managementType;
	}

	public void setManagementType(SelectValue managementType) {
		this.managementType = managementType;
	}

	public SelectValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getHospitalComments() {
		return this.hospitalComments;
	}

	public void setHospitalComments(String hospitalComments) {
		this.hospitalComments = hospitalComments;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public SelectValue getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(SelectValue admissionType) {
		this.admissionType = admissionType;
	}

	public String getInpatientNumber() {
		return inpatientNumber;
	}

	public void setInpatientNumber(String inpatientNumber) {
		this.inpatientNumber = inpatientNumber;
	}

	public void setNewBabyIntimationListDto(
			List<NewBabyIntimationDto> newBornBabyIntimationDtoList) {
		this.newBabyIntimationListDto = newBornBabyIntimationDtoList;
	}

	public void setDeletedBabyList(List<NewBabyIntimationDto> deletedList) {
		this.deletedBabyList = deletedList;
	}

	public List<NewBabyIntimationDto> getDeletedBabyList() {
		return deletedBabyList;
	}

	public List<NewBabyIntimationDto> getNewBabyIntimationListDto() {
		return newBabyIntimationListDto;
	}

	public String getRelapseofIllnessValue() {
		return relapseofIllnessValue;
	}

	public void setRelapseofIllnessValue(String relapseofIllnessValue) {
		this.relapseofIllnessValue = relapseofIllnessValue;
	}

//	public Locality getArea() {
//		return area;
//	}
//
//	public void setArea(Locality area) {
//		this.area = area;
//	}
	
	public String getInsuredAge() {
		return insuredAge;
	}

	public String getInsuredCalculatedAge() {

		if (policy != null) {
			Date insuredDob = insuredPatient.getInsuredDateOfBirth();
			Calendar dob = Calendar.getInstance();
			this.insuredAge = "";
			if (insuredDob != null) {
				dob.setTime(insuredDob);
				Calendar today = Calendar.getInstance();
				int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				this.insuredAge = String.valueOf(age);
			}
		}	
		return insuredAge;
	}

/*	public String getInsuredFullAge() {
		this.insuredAge = "";
		if (policy != null) {
			Date insuredDob = insuredPatient.getInsuredDateOfBirth();
			Calendar dob = Calendar.getInstance();
			
			if (insuredDob != null) {
				dob.setTime(insuredDob);
				Calendar today = Calendar.getInstance();
				int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				
				int months = today.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ? 12 + (today.get(Calendar.MONTH) - dob.get(Calendar.MONTH)) : (today.get(Calendar.MONTH) - dob.get(Calendar.MONTH));
				
				this.insuredAge = months > 0 ? (new StringBuffer( String.valueOf(age)).append(" years ").append(String.valueOf(months)).append(" months" )).toString() : (new StringBuffer( String.valueOf(age)).append(" years ")).toString();
			}
		}
		return this.insuredAge;
	}*/
	
	public String getInsuredFullAge() {
		this.insuredAge = "";
		long days = 365l;
		int age = 0;
		if (policy != null) {
			Date insuredDob = insuredPatient.getInsuredDateOfBirth();
			Calendar dob = Calendar.getInstance();
			
			Date todayDate = new Date(System.currentTimeMillis());
			
			if (insuredDob != null) {
				dob.setTime(insuredDob);
				Calendar today = Calendar.getInstance();
				
				long noofDays = SHAUtils.getDaysBetweenDate(insuredDob,todayDate);
				
				if(noofDays > days) {
					 age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				}
				
				if(today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)){
					age--;   //Exclude Current Year For month calculation
				}
				
				int months = today.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ? 12 + (today.get(Calendar.MONTH) - dob.get(Calendar.MONTH)) : (today.get(Calendar.MONTH) - dob.get(Calendar.MONTH));
				
				this.insuredAge = months > 0 ? (new StringBuffer( String.valueOf(age)).append(" years ").append(String.valueOf(months)).append(" months" )).toString() : (new StringBuffer( String.valueOf(age)).append(" years ")).toString();
			}
		}
		return this.insuredAge;
	}
	
	
	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	
	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getHospitalTypeValue() {
		return hospitalTypeValue;
	}

	public void setHospitalTypeValue(String hospitalTypeValue) {
		this.hospitalTypeValue = hospitalTypeValue;
	}

	public HospitalDto getHospitalDto() {
		return hospitalDto;
	}

	public void setHospitalDto(HospitalDto hospitalDto) {
		this.hospitalDto = hospitalDto;
	}

	public void setInsuredAge(String insuredAge) {
		this.insuredAge = insuredAge;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDateOfIntimation() {
		return dateOfIntimation;
	}

	public void setDateOfIntimation(String dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
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

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getSmCode() {
		return smCode;
	}

	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getAgentBrokerCode() {
		return agentBrokerCode;
	}

	public void setAgentBrokerCode(String agentBrokerCode) {
		this.agentBrokerCode = agentBrokerCode;
	}

	public String getAgentBrokerName() {
		return agentBrokerName;
	}

	public void setAgentBrokerName(String agentBrokerName) {
		this.agentBrokerName = agentBrokerName;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getLineofBusiness() {
		return lineofBusiness;
	}

	public void setLineofBusiness(String lineofBusiness) {
		this.lineofBusiness = lineofBusiness;
	}

	public String getAdmissionDateForCarousel() {
		return admissionDateForCarousel;
	}

	public void setAdmissionDateForCarousel(String admissionDateForCarousel) {
		this.admissionDateForCarousel = admissionDateForCarousel;
	}

	public MastersValue getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(MastersValue intimationSource) {
		this.intimationSource = intimationSource;
	}

	public String getIntimationMode() {
		return intimationMode;
	}

	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}

	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
	}

	public String getHospitalInternalCode() {
		return hospitalInternalCode;
	}

	public void setHospitalInternalCode(String hospitalInternalCode) {
		this.hospitalInternalCode = hospitalInternalCode;
	}

	public String getPatientNotCoveredName() {
		return patientNotCoveredName;
	}

	public void setPatientNotCoveredName(String patientNotCoveredName) {
		this.patientNotCoveredName = patientNotCoveredName;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getCpuAddress() {
		return cpuAddress;
	}

	public void setCpuAddress(String cpuAddress) {
		this.cpuAddress = cpuAddress;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}

	public OrganaizationUnit getParentOrgUnit() {
		return parentOrgUnit;
	}

	public void setParentOrgUnit(OrganaizationUnit parentOrgUnit) {
		this.parentOrgUnit = parentOrgUnit;
	}

	public SelectValue getLobId() {
		return lobId;
	}

	public void setLobId(SelectValue lobId) {
		this.lobId = lobId;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public String getIncidenceFlag() {
		return incidenceFlag;
	}

	public void setIncidenceFlag(String incidenceFlag) {
		this.incidenceFlag = incidenceFlag;
	}

	public String getHospitalReqFlag() {
		return hospitalReqFlag;
	}

	public void setHospitalReqFlag(String hospitalReqFlag) {
		this.hospitalReqFlag = hospitalReqFlag;
		this.hospitalReq = hospitalReqFlag != null ? hospitalReqFlag.equalsIgnoreCase("Y") ? true: false:false;
	}

	public Date getAccidentDeathDate() {
		return accidentDeathDate;
	}

	public void setAccidentDeathDate(Date accidentDeathDate) {
		this.accidentDeathDate = accidentDeathDate;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeAddr() {
		return nomineeAddr;
	}

	public void setNomineeAddr(String nomineeAddr) {
		this.nomineeAddr = nomineeAddr;
	}

	public String getPaParentName() {
		return paParentName;
	}

	public void setPaParentName(String paParentName) {
		this.paParentName = paParentName;
	}

	public String getPaPatientName() {
		return paPatientName;
	}

	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}

	public Date getParentDOB() {
		return parentDOB;
	}

	public void setParentDOB(Date parentDOB) {
		this.parentDOB = parentDOB;
	}

	public Double getParentAge() {
		return parentAge;
	}

	public void setParentAge(Double parentAge) {
		this.parentAge = parentAge;
	}

	public Double getPaSumInsured() {
		return paSumInsured;
	}

	public void setPaSumInsured(Double paSumInsured) {
		this.paSumInsured = paSumInsured;
	}
	

	public Date getAccountDeactivatedDate() {
		return accountDeactivatedDate;
	}

	public void setAccountDeactivatedDate(Date accountDeactivatedDate) {
		this.accountDeactivatedDate = accountDeactivatedDate;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public Date getLossDateTime() {
		return lossDateTime;
	}

	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getTpaIntimationNumber() {
		return tpaIntimationNumber;
	}

	public void setTpaIntimationNumber(String tpaIntimationNumber) {
		this.tpaIntimationNumber = tpaIntimationNumber;
	}

	public Long getEventCodeId() {
		return eventCodeId;
	}

	public void setEventCodeId(Long eventCodeId) {
		this.eventCodeId = eventCodeId;
	}

	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}

	public String getNonHospitalizationFlag() {
		return nonHospitalizationFlag;
	}

	public void setNonHospitalizationFlag(String nonHospitalizationFlag) {
		this.nonHospitalizationFlag = nonHospitalizationFlag;
	}

	public String getPlaceVisit() {
		return placeVisit;
	}

	public void setPlaceVisit(String placeVisit) {
		this.placeVisit = placeVisit;
	}

	public String getPlaceLossDelay() {
		return placeLossDelay;
	}

	public void setPlaceLossDelay(String placeLossDelay) {
		this.placeLossDelay = placeLossDelay;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getDollarInitProvisionAmt() {
		return dollarInitProvisionAmt;
	}

	public void setDollarInitProvisionAmt(Double dollarInitProvisionAmt) {
		this.dollarInitProvisionAmt = dollarInitProvisionAmt;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Double getInrTotalAmount() {
		return inrTotalAmount;
	}

	public void setInrTotalAmount(Double inrTotalAmount) {
		this.inrTotalAmount = inrTotalAmount;
	}

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public MastersEvents getEvent() {
		return event;
	}

	public void setEvent(MastersEvents event) {
		this.event = event;
	}

	public SelectValue getEventCodeValue() {
		return eventCodeValue;
	}

	public void setEventCodeValue(SelectValue eventCodeValue) {
		this.eventCodeValue = eventCodeValue;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	
		

	public String getIsPortablity() {
		return isPortablity;
	}

	public void setIsPortablity(String isPortablity) {
		this.isPortablity = isPortablity;
	}

	public Date getPolicyInceptionDate() {
		return policyInceptionDate;
	}

	public void setPolicyInceptionDate(Date policyInceptionDate) {
		this.policyInceptionDate = policyInceptionDate;
	}

	public String getGmcMainMemberName() {
		return gmcMainMemberName;
	}

	public void setGmcMainMemberName(String gmcMainMemberName) {
		this.gmcMainMemberName = gmcMainMemberName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getRelationShipWithEmployee() {
		return relationShipWithEmployee;
	}

	public void setRelationShipWithEmployee(String relationShipWithEmployee) {
		this.relationShipWithEmployee = relationShipWithEmployee;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIcrEarnedPremium() {
		return icrEarnedPremium;
	}

	public void setIcrEarnedPremium(String icrEarnedPremium) {
		this.icrEarnedPremium = icrEarnedPremium;
	}

	public Boolean getIsPaayasPolicy() {
		return isPaayasPolicy;
	}

	public void setIsPaayasPolicy(Boolean isPaayasPolicy) {
		this.isPaayasPolicy = isPaayasPolicy;
	}

	public Boolean getIsJioPolicy() {
		return isJioPolicy;
	}

	public void setIsJioPolicy(Boolean isJioPolicy) {
		this.isJioPolicy = isJioPolicy;
	}

	public String getReimbCpuAddress() {
		return reimbCpuAddress;
	}

	public void setReimbCpuAddress(String reimbCpuAddress) {
		this.reimbCpuAddress = reimbCpuAddress;
	}
	
	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
		this.dummyFlag = dummy != null ? dummy.equalsIgnoreCase("Y") ? true : false : false;
	}
	
	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
	}

	public String getCallerEmail() {
		return callerEmail;
	}

	public void setCallerEmail(String callerEmail) {
		this.callerEmail = callerEmail;
	}
	
	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	
	public SelectValue getInsuredType() {
		return insuredType;
	}

	public void setInsuredType(SelectValue insuredType) {
		this.insuredType = insuredType;
	}
	
	@Override
	public String toString() {
		return "NewIntimationDto [key=" + key + ", intimationId="
				+ intimationId + ", claimNumber=" + claimNumber
				+ ", dateOfIntimation=" + dateOfIntimation + ", productName="
				+ productName + ", insuredPatientName=" + insuredPatientName
				+ ", patientName=" + patientName + ", hospitalName="
				+ hospitalName + ", hospitalCity=" + hospitalCity
				+ ", intimationMode=" + intimationMode
				+ ", patientNotCoveredName=" + patientNotCoveredName
				+ ", healthCardNo=" + healthCardNo + ", hospitalIrdaCode="
				+ hospitalIrdaCode + ", hospitalInternalCode="
				+ hospitalInternalCode + ", claimType=" + claimType
				+ ", policyYear=" + policyYear + ", balanceSI=" + balanceSI
				+ ", reasonForAdmission=" + reasonForAdmission
				+ ", admissionDate=" + admissionDate + ", policyKey="
				+ policyKey + ", insuredKey=" + insuredKey + ", product="
				+ product + ", orginalSI=" + orginalSI + ", policy=" + policy
				+ ", policyNumber=" + policyNumber + ", policyType="
				+ policyType + ", productType=" + productType + ", genderId="
				+ genderId + ", insuredAge=" + insuredAge + ", policyAgeing="
				+ policyAgeing + ", relationshipWithInsuredId="
				+ relationshipWithInsuredId + ", modeOfIntimation="
				+ modeOfIntimation + ", intimatedBy=" + intimatedBy
				+ ", email=" + email + ", callerContactNum=" + callerContactNum
				+ ", callerLandlineNum=" + callerLandlineNum
				+ ", relapseofIllness=" + relapseofIllness
				+ ", relapseofIllnessValue=" + relapseofIllnessValue
				+ ", attenderContactNum=" + attenderContactNum
				+ ", attenderLandlineNum=" + attenderLandlineNum
				+ ", insuredPatient=" + insuredPatient + ", gmcMainMemberName="
				+ gmcMainMemberName + ", isClaimManuallyProcessed="
				+ isClaimManuallyProcessed + ", newBornFlag=" + newBornFlag
				+ ", intimaterName=" + intimaterName + ", babiesDetails="
				+ babiesDetails + ", status=" + status + ", stage=" + stage
				+ ", createdDate=" + createdDate + ", cpuId=" + cpuId
				+ ", sumInsured=" + sumInsured
				+ ", cpuAddress=" + cpuAddress + ", hospitalType="
				+ hospitalType + ", hospitalTypeValue=" + hospitalTypeValue
				+ ", state=" + state + ", icrEarnedPremium=" + icrEarnedPremium
				+ ", isPaayasPolicy=" + isPaayasPolicy + ", isJioPolicy="
				+ isJioPolicy + ", city=" + city + ", hospitalDto="
				+ hospitalDto + ", doctorName=" + doctorName
				+ ", managementType=" + managementType + ", roomCategory="
				+ roomCategory + ", comments=" + comments
				+ ", admissionType=" + admissionType + ", inpatientNumber="
				+ inpatientNumber + ", lateIntimationReason="
				+ lateIntimationReason + ", newBabyIntimationListDto="
				+ newBabyIntimationListDto + ", deletedBabyList="
				+ deletedBabyList + ", createdBy=" + createdBy
				+ ", hospitalAddress=" + hospitalAddress
				+ ", intimationSource=" + intimationSource + ", cpuCode="
				+ cpuCode + ", smCode=" + smCode + ", smName=" + smName
				+ ", agentBrokerCode=" + agentBrokerCode + ", agentBrokerName="
				+ agentBrokerName + ", registrationStatus="
				+ registrationStatus + ", lineofBusiness=" + lineofBusiness
				+ ", incidenceFlag=" + incidenceFlag + ", hospitalReqFlag="
				+ hospitalReqFlag + ", accountDeactivatedDate="
				+ accountDeactivatedDate + ", passportNumber=" + passportNumber
				+ ", lossDateTime=" + lossDateTime + ", ailmentLoss="
				+ ailmentLoss + ", tpaIntimationNumber=" + tpaIntimationNumber
				+ ", eventCodeId=" + eventCodeId + ", hospitalizationFlag="
				+ hospitalizationFlag + ", nonHospitalizationFlag="
				+ nonHospitalizationFlag + ", placeVisit=" + placeVisit
				+ ", placeLossDelay=" + placeLossDelay + ", sponsorName="
				+ sponsorName + ", countryId=" + countryId + ", remarks="
				+ remarks + ", dollarInitProvisionAmt="
				+ dollarInitProvisionAmt + ", inrConversionRate="
				+ inrConversionRate + ", inrTotalAmount=" + inrTotalAmount
				+ ", passportExpiryDate=" + passportExpiryDate + ", event="
				+ event + ", eventName=" + eventName + ", eventCode="
				+ eventCode + ", plan=" + plan + ", eventCodeValue="
				+ eventCodeValue + ", intimationDate=" + intimationDate
				+ ", dischargeDate=" + dischargeDate + ", lobId=" + lobId
				+ ", processClaimType=" + processClaimType
				+ ", accidentDeathDate=" + accidentDeathDate + ", nomineeName="
				+ nomineeName + ", nomineeAddr=" + nomineeAddr
				+ ", isPortablity=" + isPortablity + ", policyInceptionDate="
				+ policyInceptionDate + ", admissionDateForCarousel="
				+ admissionDateForCarousel + ", organizationUnit="
				+ organizationUnit + ", parentOrgUnit=" + parentOrgUnit
				+ ", paParentName=" + paParentName + ", paPatientName="
				+ paPatientName + ", parentDOB=" + parentDOB + ", parentAge="
				+ parentAge + ", paSumInsured=" + paSumInsured
				+ ", employeeCode=" + employeeCode
				+ ", relationShipWithEmployee=" + relationShipWithEmployee
				+ ", companyName=" + companyName + "]";
	}

	public List<PolicyCoverDetails> getPolicyCoverDetails() {
		return policyCoverDetails;
	}

	public void setPolicyCoverDetails(List<PolicyCoverDetails> policyCoverDetails) {
		this.policyCoverDetails = policyCoverDetails;
	}

	public String getStudentPatientName() {
		return studentPatientName;
	}

	public void setStudentPatientName(String studentPatientName) {
		this.studentPatientName = studentPatientName;
	}

	public Integer getStudentAge() {
		return studentAge;
	}

	public void setStudentAge(Integer studentAge) {
		this.studentAge = studentAge;
	}

	public Date getStudentDOB() {
		return studentDOB;
	}

	public void setStudentDOB(Date studentDOB) {
		this.studentDOB = studentDOB;
	}

	public List<GmcMainMemberList> getGmcInsuredList() {
		return gmcInsuredList;
	}

	public void setGmcInsuredList(List<GmcMainMemberList> gmcInsuredList) {
		this.gmcInsuredList = gmcInsuredList;
	}

	public GmcMainMemberList getSelectedGmcInsuredList() {
		return selectedGmcInsuredList;
	}

	public void setSelectedGmcInsuredList(GmcMainMemberList selectedGmcInsuredList) {
		this.selectedGmcInsuredList = selectedGmcInsuredList;
	}

	public PremPolicy getPolicySummary() {
		return policySummary;
	}

	public void setPolicySummary(PremPolicy policySummary) {
		this.policySummary = policySummary;
	}

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getPolicyServiceType() {
		return policyServiceType;
	}

	public void setPolicyServiceType(String policyServiceType) {
		this.policyServiceType = policyServiceType;
	}

	public Boolean getHospitalReq() {
		return hospitalReq;
	}

	public void setHospitalReq(Boolean hospitalReq) {
		this.hospitalReq = hospitalReq;
		this.hospitalReqFlag = this.hospitalReq != null ? this.hospitalReq ? "Y" : "N": null;
		
	}

	public Boolean getDummyFlag() {
		return dummyFlag;
	}

	public void setDummyFlag(Boolean dummyFlag) {
		this.dummyFlag = dummyFlag;
		this.dummy = this.dummyFlag != null ? this.dummyFlag ? "Y" : "N" : null;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
	public Long getGmcMainMemberId() {
		return gmcMainMemberId;
	}

	public void setGmcMainMemberId(Long gmcMainMemberId) {
		this.gmcMainMemberId = gmcMainMemberId;
	}

	public String getCopayFlag() {
		return copayFlag;
	}

	public void setCopayFlag(String copayFlag) {
		this.copayFlag = copayFlag;
	}

	public Boolean getIsTataPolicy() {
		return isTataPolicy;
	}
	public Boolean getIsDeletedRisk() {
		return isDeletedRisk;
		//return true;
	}

	public void setIsDeletedRisk(Boolean isDeletedRisk) {
		this.isDeletedRisk = isDeletedRisk;
	}

	public void setIsTataPolicy(Boolean isTataPolicy) {
		this.isTataPolicy = isTataPolicy;
	}

	public List<NomineeDetailsDto> getNomineeList() {
		return nomineeList;
	}

	public void setNomineeList(List<NomineeDetailsDto> nomineeList) {
		this.nomineeList = nomineeList;
	}

	public int getNomineeSelectCount() {
		return nomineeSelectCount;
	}

	public void setNomineeSelectCount(int nomineeSelectCount) {
		this.nomineeSelectCount = nomineeSelectCount;
	}

	public Date getPaPatientDOB() {
		return paPatientDOB;
	}

	public void setPaPatientDOB(Date paPatientDOB) {
		this.paPatientDOB = paPatientDOB;
	}

	public Integer getPaPatientAge() {
		return paPatientAge;
	}

	public void setPaPatientAge(Integer paPatientAge) {
		this.paPatientAge = paPatientAge;
	}	

	public String getInsuredDeceasedFlag() {
		return insuredDeceasedFlag;
	}

	public void setInsuredDeceasedFlag(String insuredDeceasedFlag) {
		this.insuredDeceasedFlag = insuredDeceasedFlag;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public PaymentProcessCpuPageDTO getPaymentDto() {
		return paymentDto;
	}

	public void setPaymentDto(PaymentProcessCpuPageDTO paymentDto) {
		this.paymentDto = paymentDto;
	}

	public Double getRoomRentMaxAmount() {
		return roomRentMaxAmount;
	}

	public void setRoomRentMaxAmount(Double roomRentMaxAmount) {
		this.roomRentMaxAmount = roomRentMaxAmount;
	}

	public Double getIcuMaxAmount() {
		return icuMaxAmount;
	}

	public void setIcuMaxAmount(Double icuMaxAmount) {
		this.icuMaxAmount = icuMaxAmount;
	}

	public Integer getCopayMaxPer() {
		return copayMaxPer;
	}

	public void setCopayMaxPer(Integer copayMaxPer) {
		this.copayMaxPer = copayMaxPer;
	}

	public Integer getSublimitMaxAmt() {
		return sublimitMaxAmt;
	}

	public void setSublimitMaxAmt(Integer sublimitMaxAmt) {
		this.sublimitMaxAmt = sublimitMaxAmt;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Insured getGmcMainMember() {
		return gmcMainMember;
	}

	public void setGmcMainMember(Insured gmcMainMember) {
		this.gmcMainMember = gmcMainMember;
	}

	public String getHrmContactNo() {
		return hrmContactNo;
	}

	public void setHrmContactNo(String hrmContactNo) {
		this.hrmContactNo = hrmContactNo;
	}

	public String getHrmUserName() {
		return hrmUserName;
	}

	public void setHrmUserName(String hrmUserName) {
		this.hrmUserName = hrmUserName;
	}

	public String getHrmHeadName() {
		return hrmHeadName;
	}

	public void setHrmHeadName(String hrmHeadName) {
		this.hrmHeadName = hrmHeadName;
	}

	public String getHrmHeadContactNo() {
		return hrmHeadContactNo;
	}

	public void setHrmHeadContactNo(String hrmHeadContactNo) {
		this.hrmHeadContactNo = hrmHeadContactNo;
	}

	public String getCscMemberNameContactNo() {
		return cscMemberNameContactNo;
	}

	public void setCscMemberNameContactNo(String cscMemberNameContactNo) {
		this.cscMemberNameContactNo = cscMemberNameContactNo;
	}

	public Integer getIcdLetterStatus() {
		return icdLetterStatus;
	}

	public void setIcdLetterStatus(Integer icdLetterStatus) {
		this.icdLetterStatus = icdLetterStatus;
	}

	public String getTollNumber() {
		return tollNumber;
	}

	public void setTollNumber(String tollNumber) {
		this.tollNumber = tollNumber;
	}

	public String getWhatsupNumber() {
		return whatsupNumber;
	}

	public void setWhatsupNumber(String whatsupNumber) {
		this.whatsupNumber = whatsupNumber;
	}

	public String getPageFooter() {
		return pageFooter;
	}

	public void setPageFooter(String pageFooter) {
		this.pageFooter = pageFooter;
	}
	
	
}