package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_COUNTRY_T database table.
 * 
 */
@Entity
@Table(name="MAS_CPU_CODE")
@NamedQueries({
@NamedQuery(name="TmpCPUCode.findAll", query="SELECT m FROM TmpCPUCode m where m.activeStatus is not null and m.activeStatus = '1'"),
@NamedQuery(name="TmpCPUCode.findAllAsc", query="SELECT m FROM TmpCPUCode m where m.activeStatus is not null and m.activeStatus = '1' order by m.cpuCode asc"),
@NamedQuery(name="TmpCPUCode.findByKey",query = "SELECT o FROM TmpCPUCode o where o.key = :cpuId"),
@NamedQuery(name="TmpCPUCode.findByCode",query = "SELECT o FROM TmpCPUCode o where o.cpuCode = :cpuCode"),
@NamedQuery(name="TmpCPUCode.findByDescription",query = "SELECT o FROM TmpCPUCode o where o.description = :description"),
@NamedQuery(name="TmpCPUCode.findByCpuCode",query = "SELECT m FROM TmpCPUCode m where m.cpuCode like '95%' order by m.cpuCode"),
@NamedQuery(name="TmpCPUCode.findByOrg",query = "SELECT m FROM TmpCPUCode m where m.orgId in (:orgList) and m.activeStatus is not null and m.activeStatus = '1' order by m.cpuCode")
})
public class TmpCPUCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7217543402237269703L;

	@Id
	@Column(name="\"CPU_KEY\"")
	private Long key;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="ADDRESS")
	private String address;

	@Column(name="PROVISION_AMOUNT")
	private Double provisionAmount;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Date CreatedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="REIMBURSEMENT_ADDRESS")
	private String reimbAddress;
	
	@Column(name="ORG_ID")
	private String orgId;
	
//	@Column(name="ACTIVE_STATUS_DATE")
//	private Date activeStatusDate;
	
	@Column(name="CASHLESS_NEGOTIATION_FLAG")
	private String negotiationFlag;
	
	@Column(name="GMC_CLM_PRCS_CPU")
	private Long gmcRoutingCpuCode;
	
	@Column(name="TOLL_NUMBER")
	private String tollNumber;

	@Column(name="WHATSAPP_NUMBER")
	private String whatsupNumber;

	@Column(name="PAGEFOOTER")
	private String pageFooter;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

//	public Date getActiveStatusDate() {
//		return activeStatusDate;
//	}
//
//	public void setActiveStatusDate(Date activeStatusDate) {
//		this.activeStatusDate = activeStatusDate;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReimbAddress() {
		return reimbAddress;
	}

	public void setReimbAddress(String reimbAddress) {
		this.reimbAddress = reimbAddress;
	}
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getNegotiationFlag() {
		return negotiationFlag;
	}

	public void setNegotiationFlag(String negotiationFlag) {
		this.negotiationFlag = negotiationFlag;
	}
	
	public Long getGmcRoutingCpuCode() {
		return gmcRoutingCpuCode;
	}

	public void setGmcRoutingCpuCode(Long gmcRoutingCpuCode) {
		this.gmcRoutingCpuCode = gmcRoutingCpuCode;
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