package com.shaic.claim.reports.notAdheringToANHReport;

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
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Insured;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.newcode.wizard.dto.NewBabyIntimationDto;
import com.shaic.newcode.wizard.dto.NewBornBabyDetailsDTO;

public class NewIntimationNotAdheringToANHDto extends AbstractTableDTO
		implements Serializable {
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
	@Size(min = 10, max = 12, message = "Please Enter a valid Caller Contact Number.")
	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Caller Contact Number")
	private String callerContactNum;

	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Caller Landline Number")
	private String callerLandlineNum;

	@NotNull(message = "Please Select Relapse of Illness.")
	private SelectValue relapseofIllness;

	private String relapseofIllnessValue;

	@NotNull(message = "Please Enter Attender's Contact Number.")
	@Size(min = 10, max = 12, message = "Please Enter Attender's Contact Number.")
	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Attender Contact Number")
	private String attenderContactNum;

	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Attender Landline Number")
	private String attenderLandlineNum;

	// @NotNull(message = "Please Select Insured Patient Name")
	// private TmpInsured insuredPatientId;

	@NotNull(message = "Please Select Insured Patient Name")
	private Insured insuredPatient;
	
	 private String gmcMainMemberName;
	 
	 private Boolean isClaimManuallyProcessed = Boolean.FALSE;

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

	@NotNull(message = "Please Enter Intimater Name.")
	@Pattern(regexp = "^[a-zA-Z./' ]*$", message = "Please Enter a valid Intimater Name")
	private String intimaterName;

	// private TmpPolicy tmpPolicy;

	private List<NewBornBabyDetailsDTO> babiesDetails;

	private Status status;

	private Stage stage;

	private Date createdDate;

	private Long cpuId;
	
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

	@Size(max = 50)
	@Pattern(regexp = "^[a-zA-Z0-9./' ]*$", message = "Please Enter a valid Doctor Name")
	private String doctorName;

	@NotNull(message = "Please Select Management Type.")
	private SelectValue managementType;

	private SelectValue roomCategory;

	@Size(max = 200)
	private String comments;

	@NotNull(message = "Please Select Admission Type")
	private SelectValue admissionType;

	@NotNull(message = "Please Enter Inpatient Number")
	@Size(min = 1, message = "Please Enter Inpatient Number.")
	@Pattern(regexp = "(^[0-9]*)$", message = "Please Enter a valid Inpatient Number")
	private String inpatientNumber;

	@NotNull(message = "Please Enter Reason for Late Intimation")
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
	
	private String paParentName;
	
	private String paPatientName;
	
	private Date parentDOB;
	
	private Double parentAge;	
	
	private Double paSumInsured;

	private String employeeCode;
	
	private String relationShipWithEmployee;
	
	private String companyName;

	
	public NewIntimationNotAdheringToANHDto() {

		// this.tmpPolicy = new TmpPolicy();
		this.policy = new Policy();
		this.babiesDetails = new ArrayList<NewBornBabyDetailsDTO>();
		this.insuredPatient = new Insured();
		this.hospitalDto = new HospitalDto();
		this.organizationUnit = new OrganaizationUnit();
		this.parentOrgUnit = new OrganaizationUnit();
		this.newBornFlag = false;
		this.lobId = new SelectValue();
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
	


}
