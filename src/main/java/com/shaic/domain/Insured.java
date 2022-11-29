package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.shaic.domain.preauth.PortabilityPreviousPolicy;
import com.shaic.domain.preauth.PortablityPolicy;


/**
 * The persistent class for the IMS_CLS_POLICY_INSURED database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_POLICY_INSURED")
@NamedQueries( {
@NamedQuery(name="Insured.findAll", query="SELECT i FROM Insured i"),
@NamedQuery(name = "Insured.findByPolicyNo", query = "select o from Insured o where o.policy.policyNumber = :policyNumber"),
@NamedQuery(name = "Insured.findByPolicykey1", query = "select o from Insured o where o.policy.key = :policykey"),
@NamedQuery(name = "Insured.findByPolicyKey", query = "select o from Insured o where o.policy.key = :key and o.key = :insuredKey"),
@NamedQuery(name = "Insured.findByHealthcard", query="SELECT i from Insured i where i.healthCardNumber Like  :healthCard "),
@NamedQuery(name = "Insured.findByInsuredName", query="SELECT i from Insured i where i.insuredName Like  :insuredName "),
@NamedQuery(name = "Insured.findByInsuredNameAndDOB", query="SELECT i from Insured i where  Lower(i.insuredName) Like :insuredName and i.insuredDateOfBirth = :dob "),
@NamedQuery(name = "Insured.findByDOB", query="SELECT i from Insured i where  i.insuredDateOfBirth = :dob "),
@NamedQuery(name = "Insured.findByPolicyAndInsuredName", query = "select o from Insured o where o.policy.policyNumber = :policeNo and o.insuredName =:insuredName and o.insuredDateOfBirth = :dob "),
@NamedQuery(name = "Insured.findByInsured", query = "select o from Insured o where o.key = :key"),
@NamedQuery(name = "Insured.findByInsuredIdAndPolicyNo", query = "select o from Insured o where o.policy.policyNumber = :policyNo and o.insuredId =:insuredId and o.lopFlag =:lobFlag"),
@NamedQuery(name = "Insured.findByInsuredIdAndPolicyNoForDefault", query = "select o from Insured o where o.policy.policyNumber = :policyNo and o.insuredId =:insuredId"),
@NamedQuery(name = "Insured.findByInsuredNameAndPolicyNo", query="SELECT i from Insured i where i.insuredName Like  :insuredName and i.policy.policyNumber = :policyNo "),
@NamedQuery(name = "Insured.findByHealthcardNo", query = "SELECT o FROM Insured o where o.healthCardNumber = :healthCardNo and o.insuredAge = (select min(o.insuredAge) from Insured o where o.healthCardNumber = :healthCardNo)"),
@NamedQuery(name = "Insured.findInsuredByPolicyKeyAndInsuredId", query = "select o from Insured o where o.policy.key = :policyKey and o.insuredId = :insuredId"),
@NamedQuery(name = "Insured.findByInsuredNameAndPolicyNoAndLobFlag", query="SELECT i from Insured i where i.insuredName Like  :insuredName and i.policy.policyNumber = :policyNo and i.lopFlag = :lobFlag"),
@NamedQuery(name = "Insured.findByInsuredKeyAndPolicyNoAndLobFlag", query="SELECT i from Insured i where i.key Like  :insuredKey and i.policy.policyNumber = :policyNo and i.lopFlag = :lobFlag"),
@NamedQuery(name = "Insured.findByInsuredDependentRiskID", query = "select o from Insured o where o.dependentRiskId = :dependentRiskId or o.insuredId = :dependentRiskId order by o.key asc"),
@NamedQuery(name = "Insured.findByInsuredId", query = "select o from Insured o where o.policy.policyNumber = :policyNo and o.insuredId =:insuredId"),
@NamedQuery(name = "Insured.findByPolicykeyAndInsuredName", query = "select o from Insured o where o.policy.key = :policykey and o.insuredName = :insuredName"),
@NamedQuery(name = "Insured.findBaNCSInsuredIdAndPolicyNo", query = "select o from Insured o where o.policy.policyNumber = :policyNo and o.sourceRiskId =:sourceRiskId and o.lopFlag =:lobFlag"),
})
public class Insured implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -6984819129697419052L;
	
	@Id
//	@SequenceGenerator(name="IMS_CLS_POLICY_INSURED_KEY_GENERATOR", sequenceName = "SEQ_CLS_POLICY_INSURED_KEY"  ,allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_INSURED_KEY_GENERATOR" ) 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_POLICY_INSURED_KEY_GENERATOR")
	@GenericGenerator(name = "IMS_CLS_POLICY_INSURED_KEY_GENERATOR",
    strategy = "com.shaic.domain.keygenerator.InsuredCustomKeyGenerator")
	@Column(name="INSURED_KEY")	
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY")
	private Policy policy;

	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="INSURED_NUMBER")
	private Long insuredId;
	
	@Column(name="EMPLOYEE_ID")
	private String insuredEmployeeId;
	
	@JoinColumn(name="GENDER_ID")
	@OneToOne(fetch = FetchType.LAZY)
	private MastersValue insuredGender;
	
	@Column(name="INSURED_AGE")
	private Double insuredAge;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INSURED_DOB")
	private Date insuredDateOfBirth;
	
	@JoinColumn(name="RELATIONSHIP_WITH_PROPOSER_ID")
	@OneToOne(fetch = FetchType.LAZY)
	private MastersValue relationshipwithInsuredId;
	
	@Column(name = "RELATIONSHIP_CODE")
	private String relationshipCode;
	
	@Column(name="HEALTH_CARD_NUMBER")
	private String healthCardNumber;

	@Column(name="REGISTERED_MOBILE_NUMBER")
	private String registerdMobileNumber;
	
	@Column(name="SUM_INSURED")
	private Double insuredSumInsured;
	
	@Column(name="RECHARGED_SI")
	private Double insuredRechargedSI;
	
	@Column(name="RESTORED_SI")
	private Double insuredRestoredSI;
	
	@Column(name="CUMULATIVE_BONUS")
	private Double cummulativeBonus;
	
	@Column(name="SI_DEDUCTIBLE")
	private Double deductibleAmount;
	
	@Column(name = "HOSPITAL_CASH_BENEFIT_FLAG")
	private String hospitalCashBenefits;
	
	@Column(name = "PATIENT_CARE_BENEFIT_FLAG")
	private String patientCareBenefits;
	
	@Column(name="SI_SEC_II")
	private Double section2SI;
	
	@Column(name="INNER_LIMIT")
	private Double innerLimit;
	
	@Column(name="PED_COPAY")
	private Double pedCoPay;
	
	@Column(name="VOLUNTARY_COPAY")
	private Double voluntaryCopay;
	
	@Column(name="COMPCOPAY")
	private Double compCopay;
	
	@Column(name="DEPENDENT_RISK_ID")
	private Long dependentRiskId;
	
	@Column(name="GMC_FLOATER_SI")
	private Double gmcFloaterSI;

	//COMPCOPAY
	@Column(name = "MAJOR_DISABILITIES")
	private String majorDisablities;
	
	@Column(name = "RISK_GROUP")
	private String riskGroup;
	
	@Column(name = "LOB_FLAG")
	private String lopFlag;

	@Column(name = "VISIT_PURPOSE")
	private String purposeOfvisit;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "RETURN_DATE")
	private Date returnDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DEPARTURE_DATE")
	private Date departureDate;
	
	@Column(name = "NO_OF_DAYS")
	private Long noOfDays;
	
	@Column(name = "PLAN")
	private String plan;
	
	@Column(name = "POLICY_PLAN")
	private String PolicyPlan;
	
	@Column(name = "PLACE_VISIT")
	private String placeOfvisit;
	
	@Column(name = "VISA_TYPE")
	private String visaType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PASSPORT_DATE_EXPIRY")
	private Date passPortExpiryDate;
	
	@Column(name = "PASSPORT_NO")
	private String passportNo;
	
	@Column(name = "ASSIGNEE_NAME")
	private String assigneeName;
	
	@Column(name = "COPAY")
	private Long copay;
	
	@Column(name = "COMPULSORY_EXCLUSIONS")
	private String compulsoryExclusions;
			
	@Column(name = "TRIP_BEYOND_180")
	private String tripBeyond180;
	
	@Column(name = "TRIP_BAND")
	private String tripBand;
	
	@Column(name = "PLC_TRAVEL5")
	private String plcTravel5;
	
	@Column(name = "PLC_TRAVEL4")
	private String plcTravel4;
	
	@Column(name = "PLC_TRAVEL3")
	private String plcTravel3;
	
	@Column(name = "PLC_TRAVEL2")
	private String plcTravel2;
	
	
	@Column(name = "TRIP_INCLUDE_USA_CANADA")
	private String tripIncludeUsaCanada;
	
	@Column(name = "MEDICAL_RPT_ATTACHED")
	private String medicalRptAttached;
	
	@Column(name = "I_20_NO")
	private String i20no;
	
	@Column(name = "SEX")
	private String sex;
	
	@Column(name = "PLC_OF_ISSUE")
	private String placeOfIssue;
	
	@Column(name = "SPONSOR_NAME")
	private String sponsorName;
	
	@Column(name = "INSTITUTE_NAME")
	private String instituteName;
	
	@Column(name = "SPONSOR_ADDR")
	private String sponsorAddress;
	
	@Column(name = "STUDY_COUNTRY_ADDR")
	private String studyCountryAddress;
	
	/**
	 * Added for GPA
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="EFFFMDT")
	private Date effectiveFromDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFTODT")
	private Date effectiveToDate;
	
	@Column(name = "WORKPLACEACCIDENTYN")
	private String workPlaceAccident;
	
	@Column(name="MEMID")
	private Integer memId;
	
	@Column(name="MONTHLYINCOME")
	private Double monthlyIncome;

	@Column(name="NOOFPERSONS")
	private Integer numberOfPerson;
	
	@Column(name = "OCCUPATION")
	private String occupation;
	
	@Column(name = "RECTYPE")
	private String recType;
	
	@Column(name = "CATEGORY")
	private String category;
	
	@Column(name = "CATEGORYDESCRIPTION")
	private String categoryDescription;
	
	@Column(name="EARNINGPARENTSI")
	private Double earningParentSI;
	
	@Column(name="NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name="NOMINEE_RELATION")
	private String nomineeRelation;
	
	@Column(name="NOMINEE_SHARE_PERCENT")
	private Double nomineeSharePercent;
	
	@Column(name="OUT_PATIENT")
	private String outPatient;
	
	@Column(name="MAIN_MEMBER")
	private String mainMember;
	
	@Column(name="INSURED_EMAIL_ID")
	private String insuredEmailId;
	
	@Column(name="INSURED_MOBILE_NUMBER")
	private String insuredContactNumber;
	
	@Column(name="LINKEMAILID")
	private String linkMailId;
	
	@Column(name="LINKEMPNAME")
	private String linkEmpName;
	
	@Column(name="LINKEMPNO")
	//@Transient
	private String linkEmpNumber;
	
	@Column(name="LINKMOBILENO")
	private String linkEmpMobileNo;
	
	@Column(name="SUM_INSURED_1")
	private Double sumInsured1;
	
	@Column(name="SUM_INSURED_2")
	private Double sumInsured2;
	
	@Column(name="SUM_INSURED_3")
	private Double sumInsured3;
	
	@Column(name="SUM_INSURED_1_FLAG")
	private String sumInsured1Flag;
	
	@Column(name="SUM_INSURED_2_FLAG")
	private String sumInsured2Flag;
	
	@Column(name="SUM_INSURED_3_FLAG")
	private String sumInsured3Flag;
	
	@Column(name="INSURED_ENTRY_AGE")
	private Integer entryAge;
	
	@Column(name = "POLICY_YEAR")
	private String policyYear;
	
	@Column(name="ADDRESS1")
	private String address1;
	
	@Column(name="ADDRESS2")
	private String address2;
	
	@Column(name="ADDRESS3")
	private String address3;
	
	@Column(name="INSURED_CITY")
	private String city;
	
	@Column(name="INSURED_PINCODE")
	private String insuredPinCode;
	
	@Column(name="INSURED_STATE")
	private String insuredState;
	
	@Column(name="AADHAR_NO")
	private Long aadharNo;
	
	@Column(name="AADHAR_REMARKS")
	private String aadharRemarks;
	
	@Column(name=" Account_No")
	private String accountNumber;
	
	@Column(name="Bank_Name")
	private String bankName;
	
	@Column(name="Branch_Name")
	private String branchName;
	
	@Column(name="IFSC_Code")
	private String ifscCode;
	
	@Column(name="Name_of_Account_Holder")
	private String nameOfAccountHolder;
	
	@Column(name="DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name="GHC_POLICY_TYPE")
	private String ghcPolicyType;
	
	@Column(name="GHC_SCHEME")
	private String ghcScheme;
	
	@Column(name="GHC_DAYS")
	private Integer ghcDays;

	@Column(name="SOURCE_RISK_ID")
	private String sourceRiskId;

	@Transient
	private List<InsuredPedDetails> insuredPedList;
	
	@Transient
	private List<PortablityPolicy> portablityPolicy;
	
	/**
	 * Below code was Added as part of CR R1080
	 */
	@Transient
	private List<PortabilityPreviousPolicy> portablityPrevPolicy;
	
	@Transient
	private List<NomineeDetails> nomineeDetails;
	
	@Transient
	private List<InsuredCover> coverDetailsForPA;
	
	@Transient
	private String insuredPedNames;
	
	@Transient
	private String strGender;
	
	@Transient
	private String strEffectivedFromDate;
	
	@Transient
	private String strEffectiveToDate;
	
	@Transient
	private String table1;
	
	@Transient
	private String table2;
	
	@Transient
	private String table3;
	
	@Transient
	private String table4;
	
	@Transient
	private String hospitalExpensive;
	
	@Transient
	private String inPatient;
	
	@Transient
	private String medicalExtension;
	
	@Transient
	private String medicalExtensionOtherPaClaim;
	
	@Transient
	private String transMortRem;
	
	@Transient
	private String tutionFees;
	
	@Transient
	private String strDateOfBirth;

	@Transient
	private String strInsuredAge;
	
	@Transient
	private List<PolicyNominee> proposerInsuredNomineeDetails;
	
	@Transient
	private List<GmcContinuityBenefit> gmcContBenefitDtls;
	
	@Column(name="DEPENDENT_SI_FLAG")
	private String dependentSIFlag;
	
	@Column(name="CERTIFICATE_NO")
	private String certificateNo;
	
	@Column(name="TOPUP_POLICY_NO")
	private String topUppolicyNo;
	
	@Column(name="TOPUP_POL_INSURED_NO")
	private String topUpInsuredId;

	@Column(name="IS_VIP_CUSTOMER")
	private String vipFlag;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column (name="HC_NO_OF_DAYS")
    private Integer hcpDays;
	
	@Column(name="BUY_BACK_PED")
	private String buyBackPed;
	
	@Column(name = "LUMP_SUM_FLAG")
	private String lumpSumFlg;

	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Long getInsuredId() {
		return insuredId;
	}

	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}

	public String getInsuredEmployeeId() {
		return insuredEmployeeId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public void setInsuredEmployeeId(String insuredEmployeeId) {
		this.insuredEmployeeId = insuredEmployeeId;
	}

	public MastersValue getInsuredGender() {
		return insuredGender;
	}

	public void setInsuredGender(MastersValue insuredGender) {
		this.insuredGender = insuredGender;
	}

	public Double getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(Double insuredAge) {
		this.insuredAge = insuredAge;
	}

	public Date getInsuredDateOfBirth() {
		return insuredDateOfBirth;
	}

	public void setInsuredDateOfBirth(Date insuredDateOfBirth) {
		this.insuredDateOfBirth = insuredDateOfBirth;
	}

	public MastersValue getRelationshipwithInsuredId() {
		return relationshipwithInsuredId;
	}

	public void setRelationshipwithInsuredId(MastersValue relationshipwithInsuredId) {
		this.relationshipwithInsuredId = relationshipwithInsuredId;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public String getRegisterdMobileNumber() {
		return registerdMobileNumber;
	}

	public void setRegisterdMobileNumber(String registerdMobileNumber) {
		this.registerdMobileNumber = registerdMobileNumber;
	}

	public Double getInsuredSumInsured() {
		return insuredSumInsured;
	}

	public void setInsuredSumInsured(Double insuredSumInsured) {
		this.insuredSumInsured = insuredSumInsured;
	}

	public Double getInsuredRechargedSI() {
		return insuredRechargedSI;
	}

	public Double getInnerLimit() {
		return innerLimit;
	}

	public void setInnerLimit(Double innerLimit) {
		this.innerLimit = innerLimit;
	}

	public Double getPedCoPay() {
		return pedCoPay;
	}

	public void setPedCoPay(Double pedCoPay) {
		this.pedCoPay = pedCoPay;
	}

	public Double getVoluntaryCopay() {
		return voluntaryCopay;
	}

	public void setVoluntaryCopay(Double voluntaryCopay) {
		this.voluntaryCopay = voluntaryCopay;
	}

	public Double getCompCopay() {
		return compCopay;
	}

	public void setCompCopay(Double compCopay) {
		this.compCopay = compCopay;
	}

	public void setInsuredRechargedSI(Double insuredRechargedSI) {
		this.insuredRechargedSI = insuredRechargedSI;
	}

	public Double getInsuredRestoredSI() {
		return insuredRestoredSI;
	}

	public Double getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public void setInsuredRestoredSI(Double insuredRestoredSI) {
		this.insuredRestoredSI = insuredRestoredSI;
	}

	public Double getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(Double cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}
	

	public List<InsuredPedDetails> getInsuredPedList() {
		return insuredPedList;
	}
	
	

	public void setInsuredPedList(List<InsuredPedDetails> insuredPedList) {
		this.insuredPedList = insuredPedList;
		
		if(insuredPedList != null && !insuredPedList.isEmpty()){
			insuredPedNames = "";
			for(InsuredPedDetails ped : insuredPedList){
				if(ped.getPedDescription() != null){
					if(insuredPedNames.equalsIgnoreCase("")){
						insuredPedNames = ped.getPedDescription();
					}else{
						insuredPedNames += ", "+ped.getPedDescription() ;
					}
				}
			}
		}
	}	

	public String getInsuredPedNames() {
		return insuredPedNames;
	}

	public void setInsuredPedNames(String insuredPedNames) {
		this.insuredPedNames = insuredPedNames;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if(this.getKey() == null || ((Insured) obj).getKey() == null ) {
			return false;
		}
		return this.getKey().equals(((Insured) obj).getKey());
	}

	@Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	@Override
		public String toString() {
			return this.insuredName;
		}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public String getHospitalCashBenefits() {
		return hospitalCashBenefits;
	}

	public void setHospitalCashBenefits(String hospitalCashBenefits) {
		if(hospitalCashBenefits != null && hospitalCashBenefits.equalsIgnoreCase("1")){
	    	this.hospitalCashBenefits = "Y";
	    }else{
	    	this.hospitalCashBenefits = "N";
	    }
	}

	public String getPatientCareBenefits() {
		return patientCareBenefits;
	}

	public void setPatientCareBenefits(String patientCareBenefits) {
	    if(patientCareBenefits != null && patientCareBenefits.equalsIgnoreCase("1")){
	    	this.patientCareBenefits = "Y";
	    }else{
	    	this.patientCareBenefits = "N";
	    }
		
	}

	public List<NomineeDetails> getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(List<NomineeDetails> nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}

	public List<PortablityPolicy> getPortablityPolicy() {
		return portablityPolicy;
	}

	public void setPortablityPolicy(List<PortablityPolicy> portablityPolicy) {
		this.portablityPolicy = portablityPolicy;
	}

	public Double getSection2SI() {
		return section2SI;
	}

	public void setSection2SI(Double section2si) {
		section2SI = section2si;
	}

	public Long getDependentRiskId() {
		return dependentRiskId;
	}

	public void setDependentRiskId(Long dependentRiskId) {
		this.dependentRiskId = dependentRiskId;
	}

	public Double getGmcFloaterSI() {
		return gmcFloaterSI;
	}

	public void setGmcFloaterSI(Double gmcFloaterSI) {
		this.gmcFloaterSI = gmcFloaterSI;
	}
	public String getStrGender() {
		return strGender;
	}

	public void setStrGender(String strGender) {
		this.strGender = strGender;
	}

	public String getStrDateOfBirth() {
		return strDateOfBirth;
	}

	public void setStrDateOfBirth(String strDateOfBirth) {
		this.strDateOfBirth = strDateOfBirth;
	}

	public String getStrInsuredAge() {
		return strInsuredAge;
	}

	public void setStrInsuredAge(String strInsuredAge) {
		this.strInsuredAge = strInsuredAge;
	}

	public String getMajorDisablities() {
		return majorDisablities;
	}

	public void setMajorDisablities(String majorDisablities) {
		this.majorDisablities = majorDisablities;
	}

	public String getRiskGroup() {
		return riskGroup;
	}

	public void setRiskGroup(String riskGroup) {
		this.riskGroup = riskGroup;
	}

	public String getLopFlag() {
		return lopFlag;
	}

	public void setLopFlag(String lopFlag) {
		this.lopFlag = lopFlag;
	}

	public List<InsuredCover> getCoverDetailsForPA() {
		return coverDetailsForPA;
	}

	public void setCoverDetailsForPA(List<InsuredCover> coverDetailsForPA) {
		this.coverDetailsForPA = coverDetailsForPA;
	}

	public String getPurposeOfvisit() {
		return purposeOfvisit;
	}

	public void setPurposeOfvisit(String purposeOfvisit) {
		this.purposeOfvisit = purposeOfvisit;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getPlaceOfvisit() {
		return placeOfvisit;
	}

	public void setPlaceOfvisit(String placeOfvisit) {
		this.placeOfvisit = placeOfvisit;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public Date getPassPortExpiryDate() {
		return passPortExpiryDate;
	}

	public void setPassPortExpiryDate(Date passPortExpiryDate) {
		this.passPortExpiryDate = passPortExpiryDate;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public Long getCopay() {
		return copay;
	}

	public void setCopay(Long copay) {
		this.copay = copay;
	}

	public String getCompulsoryExclusions() {
		return compulsoryExclusions;
	}

	public void setCompulsoryExclusions(String compulsoryExclusions) {
		this.compulsoryExclusions = compulsoryExclusions;
	}

	public String getTripBeyond180() {
		return tripBeyond180;
	}

	public void setTripBeyond180(String tripBeyond180) {
		this.tripBeyond180 = tripBeyond180;
	}

	public String getTripBand() {
		return tripBand;
	}

	public void setTripBand(String tripBand) {
		this.tripBand = tripBand;
	}

	public String getPlcTravel5() {
		return plcTravel5;
	}

	public void setPlcTravel5(String plcTravel5) {
		this.plcTravel5 = plcTravel5;
	}

	public String getPlcTravel4() {
		return plcTravel4;
	}

	public void setPlcTravel4(String plcTravel4) {
		this.plcTravel4 = plcTravel4;
	}

	public String getPlcTravel3() {
		return plcTravel3;
	}

	public void setPlcTravel3(String plcTravel3) {
		this.plcTravel3 = plcTravel3;
	}

	public String getPlcTravel2() {
		return plcTravel2;
	}

	public void setPlcTravel2(String plcTravel2) {
		this.plcTravel2 = plcTravel2;
	}

	public String getTripIncludeUsaCanada() {
		return tripIncludeUsaCanada;
	}

	public void setTripIncludeUsaCanada(String tripIncludeUsaCanada) {
		this.tripIncludeUsaCanada = tripIncludeUsaCanada;
	}

	public String getMedicalRptAttached() {
		return medicalRptAttached;
	}

	public void setMedicalRptAttached(String medicalRptAttached) {
		this.medicalRptAttached = medicalRptAttached;
	}

	public String getI20no() {
		return i20no;
	}

	public void setI20no(String i20no) {
		this.i20no = i20no;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getSponsorAddress() {
		return sponsorAddress;
	}

	public void setSponsorAddress(String sponsorAddress) {
		this.sponsorAddress = sponsorAddress;
	}

	public String getStudyCountryAddress() {
		return studyCountryAddress;
	}

	public void setStudyCountryAddress(String studyCountryAddress) {
		this.studyCountryAddress = studyCountryAddress;
	}
	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public String getWorkPlaceAccident() {
		return workPlaceAccident;
	}

	public void setWorkPlaceAccident(String workPlaceAccident) {
		this.workPlaceAccident = workPlaceAccident;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(Double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public Integer getNumberOfPerson() {
		return numberOfPerson;
	}

	public void setNumberOfPerson(Integer numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getStrEffectivedFromDate() {
		return strEffectivedFromDate;
	}

	public void setStrEffectivedFromDate(String strEffectivedFromDate) {
		this.strEffectivedFromDate = strEffectivedFromDate;
	}

	public String getStrEffectiveToDate() {
		return strEffectiveToDate;
	}

	public void setStrEffectiveToDate(String strEffectiveToDate) {
		this.strEffectiveToDate = strEffectiveToDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getEarningParentSI() {
		return earningParentSI;
	}

	public void setEarningParentSI(Double earningParentSI) {
		this.earningParentSI = earningParentSI;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeRelation() {
		return nomineeRelation;
	}

	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}

	public Double getNomineeSharePercent() {
		return nomineeSharePercent;
	}

	public void setNomineeSharePercent(Double nomineeSharePercent) {
		this.nomineeSharePercent = nomineeSharePercent;
	}

	public String getOutPatient() {
		return outPatient;
	}

	public void setOutPatient(String outPatient) {
		this.outPatient = outPatient;
	}

	public String getTable4() {
		return table4;
	}

	public void setTable4(String table4) {
		this.table4 = table4;
	}

	public String getTable1() {
		return table1;
	}

	public void setTable1(String table1) {
		this.table1 = table1;
	}

	public String getTable2() {
		return table2;
	}

	public void setTable2(String table2) {
		this.table2 = table2;
	}

	public String getTable3() {
		return table3;
	}

	public void setTable3(String table3) {
		this.table3 = table3;
	}

	public String getHospitalExpensive() {
		return hospitalExpensive;
	}

	public void setHospitalExpensive(String hospitalExpensive) {
		this.hospitalExpensive = hospitalExpensive;
	}

	public String getInPatient() {
		return inPatient;
	}

	public void setInPatient(String inPatient) {
		this.inPatient = inPatient;
	}

	public String getMedicalExtension() {
		return medicalExtension;
	}

	public void setMedicalExtension(String medicalExtension) {
		this.medicalExtension = medicalExtension;
	}

	public String getTransMortRem() {
		return transMortRem;
	}

	public void setTransMortRem(String transMortRem) {
		this.transMortRem = transMortRem;
	}

	public String getTutionFees() {
		return tutionFees;
	}

	public void setTutionFees(String tutionFees) {
		this.tutionFees = tutionFees;
	}

	public String getMainMember() {
		return mainMember;
	}

	public void setMainMember(String mainMember) {
		this.mainMember = mainMember;
	}

	public String getInsuredEmailId() {
		return insuredEmailId;
	}

	public void setInsuredEmailId(String insuredEmailId) {
		this.insuredEmailId = insuredEmailId;
	}

	public String getInsuredContactNumber() {
		return insuredContactNumber;
	}

	public void setInsuredContactNumber(String insuredContactNumber) {
		this.insuredContactNumber = insuredContactNumber;
	}

	public String getLinkMailId() {
		return linkMailId;
	}

	public void setLinkMailId(String linkMailId) {
		this.linkMailId = linkMailId;
	}

	public String getLinkEmpName() {
		return linkEmpName;
	}

	public void setLinkEmpName(String linkEmpName) {
		this.linkEmpName = linkEmpName;
	}

	public String getLinkEmpNumber() {
		return linkEmpNumber;
	}

	public void setLinkEmpNumber(String linkEmpNumber) {
		this.linkEmpNumber = linkEmpNumber;
	}

	public String getLinkEmpMobileNo() {
		return linkEmpMobileNo;
	}

	public void setLinkEmpMobileNo(String linkEmpMobileNo) {
		this.linkEmpMobileNo = linkEmpMobileNo;
	}

	public Double getSumInsured1() {
		return sumInsured1;
	}

	public void setSumInsured1(Double sumInsured1) {
		this.sumInsured1 = sumInsured1;
	}

	public Double getSumInsured2() {
		return sumInsured2;
	}

	public void setSumInsured2(Double sumInsured2) {
		this.sumInsured2 = sumInsured2;
	}

	public Double getSumInsured3() {
		return sumInsured3;
	}

	public void setSumInsured3(Double sumInsured3) {
		this.sumInsured3 = sumInsured3;
	}

	public String getSumInsured1Flag() {
		return sumInsured1Flag;
	}

	public void setSumInsured1Flag(String sumInsured1Flag) {
		this.sumInsured1Flag = sumInsured1Flag;
	}

	public String getSumInsured2Flag() {
		return sumInsured2Flag;
	}

	public void setSumInsured2Flag(String sumInsured2Flag) {
		this.sumInsured2Flag = sumInsured2Flag;
	}

	public String getSumInsured3Flag() {
		return sumInsured3Flag;
	}

	public void setSumInsured3Flag(String sumInsured3Flag) {
		this.sumInsured3Flag = sumInsured3Flag;
	}

	/**
	 * Below code was Added as part of CR R1080
	 * 
	 */
	public List<PortabilityPreviousPolicy> getPortablityPrevPolicy() {
		return portablityPrevPolicy;
	}

	public void setPortablityPrevPolicy(
			List<PortabilityPreviousPolicy> portablityPrevPolicy) {
		this.portablityPrevPolicy = portablityPrevPolicy;
	}

	public Integer getEntryAge() {
		return entryAge;
	}

	public void setEntryAge(Integer entryAge) {
		this.entryAge = entryAge;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
		public String getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}

	public String getInsuredPinCode() {
		return insuredPinCode;
	}

	public void setInsuredPinCode(String insuredPinCode) {
		this.insuredPinCode = insuredPinCode;
	}

	public String getInsuredState() {
		return insuredState;
	}

	public void setInsuredState(String insuredState) {
		this.insuredState = insuredState;
	}
	public Long getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(Long aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getAadharRemarks() {
		return aadharRemarks;
	}

	public void setAadharRemarks(String aadharRemarks) {
		this.aadharRemarks = aadharRemarks;
	}

	public String getMedicalExtensionOtherPaClaim() {
		return medicalExtensionOtherPaClaim;
	}

	public void setMedicalExtensionOtherPaClaim(String medicalExtensionOtherPaClaim) {
		this.medicalExtensionOtherPaClaim = medicalExtensionOtherPaClaim;
	}
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getNameOfAccountHolder() {
		return nameOfAccountHolder;
	}

	public void setNameOfAccountHolder(String nameOfAccountHolder) {
		this.nameOfAccountHolder = nameOfAccountHolder;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public List<PolicyNominee> getProposerInsuredNomineeDetails() {
		return proposerInsuredNomineeDetails;
	}

	public void setProposerInsuredNomineeDetails(
			List<PolicyNominee> proposerInsuredNomineeDetails) {
		this.proposerInsuredNomineeDetails = proposerInsuredNomineeDetails;
	}

	public List<GmcContinuityBenefit> getGmcContBenefitDtls() {
		return gmcContBenefitDtls;
	}

	public void setGmcContBenefitDtls(List<GmcContinuityBenefit> gmcContBenefitDtls) {
		this.gmcContBenefitDtls = gmcContBenefitDtls;
	}

	public String getDependentSIFlag() {
		return dependentSIFlag;
	}

	public void setDependentSIFlag(String dependentSIFlag) {
		this.dependentSIFlag = dependentSIFlag;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getTopUppolicyNo() {
		return topUppolicyNo;
	}

	public void setTopUppolicyNo(String topUppolicyNo) {
		this.topUppolicyNo = topUppolicyNo;
	}

	public String getTopUpInsuredId() {
		return topUpInsuredId;
	}

	public void setTopUpInsuredId(String topUpInsuredId) {
		this.topUpInsuredId = topUpInsuredId;
	}

	public String getVipFlag() {
		return vipFlag;
	}

	public void setVipFlag(String vipFlag) {
		this.vipFlag = vipFlag;
	}

	public String getSourceRiskId() {
		return sourceRiskId;
	}

	public void setSourceRiskId(String sourceRiskId) {
		this.sourceRiskId = sourceRiskId;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getPolicyPlan() {
		return PolicyPlan;
	}

	public void setPolicyPlan(String policyPlan) {
		PolicyPlan = policyPlan;
	}

	public String getGhcPolicyType() {
		return ghcPolicyType;
	}

	public void setGhcPolicyType(String ghcPolicyType) {
		this.ghcPolicyType = ghcPolicyType;
	}

	public String getGhcScheme() {
		return ghcScheme;
	}

	public void setGhcScheme(String ghcScheme) {
		this.ghcScheme = ghcScheme;
	}

	public Integer getGhcDays() {
		return ghcDays;
	}

	public void setGhcDays(Integer ghcDays) {
		this.ghcDays = ghcDays;
	}

	public Integer getHcpDays() {
		return hcpDays;
	}

	public void setHcpDays(Integer hcpDays) {
		this.hcpDays = hcpDays;
	}
	public String getBuyBackPed() {
		return buyBackPed;
	}

	public void setBuyBackPed(String buyBackPed) {
		this.buyBackPed = buyBackPed;
	}

	public String getLumpSumFlg() {
		return lumpSumFlg;
	}

	public void setLumpSumFlg(String lumpSumFlg) {
		this.lumpSumFlg = lumpSumFlg;
	}
	
	

}