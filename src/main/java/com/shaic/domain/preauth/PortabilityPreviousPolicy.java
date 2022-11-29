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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
/**
 * 
 *  @author Lakshminarayana
 *  Below class was Added as part of CR R1080
 *  
 */
@Entity
@Table(name="IMS_CLS_PORT_POLICY_DTL")
@NamedQueries({
@NamedQuery(name="PortabilityPreviousPolicy.findAll", query="SELECT p FROM PortabilityPreviousPolicy p"),
@NamedQuery(name="PortabilityPreviousPolicy.findByPolicyNumber", query="SELECT p FROM PortabilityPreviousPolicy p where p.currentPolicyNumber = :policyNumber and (p.activeStatus is not null and p.activeStatus = 1)"),
@NamedQuery(name="PortabilityPreviousPolicy.findByPolicyNumberNInsuredName", query="SELECT p FROM PortabilityPreviousPolicy p where p.policyNumber = :policyNumber and Lower(p.insuredName) = :insuredName and (p.activeStatus is not null and p.activeStatus = 1)"),
@NamedQuery(name="PortabilityPreviousPolicy.findByCurPolicyNumberNInsuredName", query="SELECT p FROM PortabilityPreviousPolicy p where p.currentPolicyNumber = :currentPolicyNumber and Lower(p.insuredName) = :insuredName and (p.activeStatus is not null and p.activeStatus = 1)")
})
public class PortabilityPreviousPolicy extends AbstractEntity {
	
	/**
	 * 
	 */
	        
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_PORT_KEY_GENERATOR", sequenceName = "SEQ_PORT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_PORT_KEY_GENERATOR") 
	@Column(name="PORT_KEY")
	private Long key;
	
	@Column(name = "CUR_POLICY_NUMBER")
	private String currentPolicyNumber;
	
	@Column(name = "INSURED_NAME")
	private String insuredName;
	
	@Column(name = "AMOUNT") 
	private Long amount;
	
	@Column(name = "CUMULATIVE_BONUS") 
	private Long cummulativeBonus;
	
	@Column(name = "CUSTOMER_ID") 
	private String customerId;
							
	@Column(name = "EXCLUSION_1STYEAR") 
	private Integer exclusion_1stYr;
									
	@Column(name = "EXCLUSION_2NDYEAR") 
	private Integer exclusion_2ndYr;
											
	@Column(name = "INSURER_NAME") 
	private String insurerName;
													
	@Column(name = "NATURE_OF_ILLNESS") 
	private String natureofIllness;
															
	@Column(name = "NO_OF_CLAIMS") 
	private Integer noofClaims;
																	
	@Column(name = "PED_DETAILS") 
	private String pedDetails;
																			
	@Column(name = "PED_WAIVER") 
	private String pedWaiver;
									
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_FM_DT") 
	private Date policyFmDt;
							
	@Transient
	private String policyStrFmDt;
	
	@Column(name = "POLICY_NUMBER") 
	private String policyNumber;
				
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_TO_DT") 
	private Date policyToDt;

	@Transient
	private String policyStrToDt;
	
	@Column(name = "POLICY_TYPE") 
	private String policyType;
																													
	@Column(name = "PRODUCT_NAME") 
	private String productName;
																															
	@Column(name = "SUM_INSURED") 
	private Long sumInsured; 
																																	
	@Column(name = "UW_YEAR") 
	private Long uwYear; 
																																			
	@Column(name = "WAIVER_30DAYS") 
	private Integer waiver30Days;
																																						
	@Column(name = "YEAR") 
	private Integer year;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(Long cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getExclusion_1stYr() {
		return exclusion_1stYr;
	}

	public void setExclusion_1stYr(Integer exclusion_1stYr) {
		this.exclusion_1stYr = exclusion_1stYr;
	}

	public Integer getExclusion_2ndYr() {
		return exclusion_2ndYr;
	}

	public void setExclusion_2ndYr(Integer exclusion_2ndYr) {
		this.exclusion_2ndYr = exclusion_2ndYr;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getNatureofIllness() {
		return natureofIllness;
	}

	public void setNatureofIllness(String natureofIllness) {
		this.natureofIllness = natureofIllness;
	}

	public Integer getNoofClaims() {
		return noofClaims;
	}

	public void setNoofClaims(Integer noofClaims) {
		this.noofClaims = noofClaims;
	}

	public String getPedDetails() {
		return pedDetails;
	}

	public void setPedDetails(String pedDetails) {
		this.pedDetails = pedDetails;
	}

	public String getPedWaiver() {
		return pedWaiver;
	}

	public void setPedWaiver(String pedWaiver) {
		this.pedWaiver = pedWaiver;
	}

	public Date getPolicyFmDt() {
		return policyFmDt;
	}

	public void setPolicyFmDt(Date policyFmDt) {
		this.policyFmDt = policyFmDt;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Date getPolicyToDt() {
		return policyToDt;
	}

	public void setPolicyToDt(Date policyToDt) {
		this.policyToDt = policyToDt;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getUwYear() {
		return uwYear;
	}

	public void setUwYear(Long uwYear) {
		this.uwYear = uwYear;
	}

	public Integer getWaiver30Days() {
		return waiver30Days;
	}

	public void setWaiver30Days(Integer waiver30Days) {
		this.waiver30Days = waiver30Days;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}	

	@Override
	public Long getKey() {

		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getPolicyStrFmDt() {
		return policyStrFmDt;
	}

	public void setPolicyStrFmDt(String policyStrFmDt) {
		this.policyStrFmDt = policyStrFmDt;
	}

	public String getPolicyStrToDt() {
		return policyStrToDt;
	}

	public void setPolicyStrToDt(String policyStrToDt) {
		this.policyStrToDt = policyStrToDt;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
