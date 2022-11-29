package com.shaic.domain.preauth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;



@Entity
@Table(name="IMS_CLS_PORTABILITY")
@NamedQueries({
@NamedQuery(name="PortablityPolicy.findAll", query="SELECT p FROM PortablityPolicy p"),
@NamedQuery(name="PortablityPolicy.findByPolicyNumber", query="SELECT p FROM PortablityPolicy p where p.currentPolicyNumber = :policyNumber and (p.activeStatus is not null and p.activeStatus = 1)"),
@NamedQuery(name="PortablityPolicy.findByPolicyNumberNInsuredName", query="SELECT p FROM PortablityPolicy p where Lower(p.insuredName) = :insuredName and p.currentPolicyNumber = :policyNumber and (p.activeStatus is not null and p.activeStatus = 1)")
})

public class PortablityPolicy extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="IMS_CLS_PORTABILITY_KEY_GENERATOR", sequenceName = "SEQ_PORTABILITY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PORTABILITY_KEY_GENERATOR") 
	@Column(name="PORTABILITY_KEY")
	private Long key;

	
	/**
	 * Below attribute was Added as part of CR R1080
	 */
	@Column(name = "INSURED_NAME")
//	@Transient
	private String insuredName;
	
	@Column(name = "POLICY_NO")
	private String policyNumber;
	
//	@Transient
	@Column(name = "CUR_POLICY_NUMBER")
	private String currentPolicyNumber;
	
	@Column(name = "INSURER_CODE")
	private String insurerCode;
	
	@Column(name = "INSURER_NAME")
	private String insurerName;
	
	@Column(name = "PRODUCT_ID")
	private String productId;
	
	@Column(name = "PRODUCT_DESC")
	private String productDescription;
	
	@Column(name = "POLICY_TYPE")
	private String policyType;
	
	@Column(name = "TBA_CODE")
	private String tbaCode;
	
	@Column(name = "POL_START_DT")
	private Date policyStartDate;
	
	@Column(name = "PERIOD_ELAPSED")
	private Long periodElapsed;
	
	@Column(name = "POLICY_TERM")
	private String policyTerm;
	
	@Column(name = "DOB")
	private Date dateOfBirth;
	
	@Column(name = "PED_DECLARED")
	private String pedDeclared;
	
	@Column(name = "PED_ICD_CODE")
	private String pedIcdCode;
	
	@Column(name = "PED_DESC")
	private String pedDescription;
	
	
	@Column(name = "FAMILY_SIZE")
	private Long familySize;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "REQUEST_ID")
	private String requestId;
	
	@Column(name = "MEM_ENTRY_DT")
	private Date memberEntryDate;
	
	@Column(name = "SI_1ST")
	private Long siFist;
	
	@Column(name = "SI_2ND")
	private Long siSecond;
	
	@Column(name = "SI_3RD")
	private Long siThird;
	
	@Column(name = "SI_4TH")
	private Long siFourth;
	
	@Column(name = "SI_1STFLOAT")
	private Long siFirstFloat;
	
	@Column(name = "SI_2NDFLOAT")
	private Long siSecondFloat;
	
	@Column(name = "SI_3RDFLOAT")
	private Long siThirdFloat;
	
	@Column(name = "SI_4THFLOAT")
	private Long siFourthFloat;
	
	@Column(name = "SI_1STCHANGE")
	private Long siFirstChange;
	
	@Column(name = "SI_2NDCHANGE")
	private Long siSecondChange;
	
	@Column(name = "SI_3RDCHANGE")
	private Long siThirdChange;
	
	@Column(name = "SI_4RDCHANGE")
	private Long siFourthChange;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Transient
	private String policyStrStartDate;
	
	@Transient
	private String strDateOfBirth;
	
	@Transient
	private String strMemberEntryDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getTbaCode() {
		return tbaCode;
	}

	public void setTbaCode(String tbaCode) {
		this.tbaCode = tbaCode;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Long getPeriodElapsed() {
		return periodElapsed;
	}

	public void setPeriodElapsed(Long periodElapsed) {
		this.periodElapsed = periodElapsed;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPedDeclared() {
		return pedDeclared;
	}

	public void setPedDeclared(String pedDeclared) {
		this.pedDeclared = pedDeclared;
	}

	public String getPedIcdCode() {
		return pedIcdCode;
	}

	public void setPedIcdCode(String pedIcdCode) {
		this.pedIcdCode = pedIcdCode;
	}

	public String getPedDescription() {
		return pedDescription;
	}

	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}

	public Long getFamilySize() {
		return familySize;
	}

	public void setFamilySize(Long familySize) {
		this.familySize = familySize;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getMemberEntryDate() {
		return memberEntryDate;
	}

	public void setMemberEntryDate(Date memberEntryDate) {
		this.memberEntryDate = memberEntryDate;
	}

	public Long getSiFist() {
		return siFist;
	}

	public void setSiFist(Long siFist) {
		this.siFist = siFist;
	}

	public Long getSiSecond() {
		return siSecond;
	}

	public void setSiSecond(Long siSecond) {
		this.siSecond = siSecond;
	}

	public Long getSiThird() {
		return siThird;
	}

	public void setSiThird(Long siThird) {
		this.siThird = siThird;
	}

	public Long getSiFourth() {
		return siFourth;
	}

	public void setSiFourth(Long siFourth) {
		this.siFourth = siFourth;
	}

	public Long getSiFirstFloat() {
		return siFirstFloat;
	}

	public void setSiFirstFloat(Long siFirstFloat) {
		this.siFirstFloat = siFirstFloat;
	}

	public Long getSiSecondFloat() {
		return siSecondFloat;
	}

	public void setSiSecondFloat(Long siSecondFloat) {
		this.siSecondFloat = siSecondFloat;
	}

	public Long getSiThirdFloat() {
		return siThirdFloat;
	}

	public void setSiThirdFloat(Long siThirdFloat) {
		this.siThirdFloat = siThirdFloat;
	}

	public Long getSiFourthFloat() {
		return siFourthFloat;
	}

	public void setSiFourthFloat(Long siFourthFloat) {
		this.siFourthFloat = siFourthFloat;
	}

	public Long getSiFirstChange() {
		return siFirstChange;
	}

	public void setSiFirstChange(Long siFirstChange) {
		this.siFirstChange = siFirstChange;
	}

	public Long getSiSecondChange() {
		return siSecondChange;
	}

	public void setSiSecondChange(Long siSecondChange) {
		this.siSecondChange = siSecondChange;
	}

	public Long getSiThirdChange() {
		return siThirdChange;
	}

	public void setSiThirdChange(Long siThirdChange) {
		this.siThirdChange = siThirdChange;
	}

	public String getPolicyStrStartDate() {
		return policyStrStartDate;
	}

	public void setPolicyStrStartDate(String policyStrStartDate) {
		this.policyStrStartDate = policyStrStartDate;
	}

	public String getStrDateOfBirth() {
		return strDateOfBirth;
	}

	public void setStrDateOfBirth(String strDateOfBirth) {
		this.strDateOfBirth = strDateOfBirth;
	}

	public String getStrMemberEntryDate() {
		return strMemberEntryDate;
	}

	public void setStrMemberEntryDate(String strMemberEntryDate) {
		this.strMemberEntryDate = strMemberEntryDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getSiFourthChange() {
		return siFourthChange;
	}

	public void setSiFourthChange(Long siFourthChange) {
		this.siFourthChange = siFourthChange;
	}

	public String getCurrentPolicyNumber() {
		return currentPolicyNumber;
	}

	public void setCurrentPolicyNumber(String currentPolicyNumber) {
		this.currentPolicyNumber = currentPolicyNumber;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
}
