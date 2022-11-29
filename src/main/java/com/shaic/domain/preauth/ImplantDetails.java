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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_IMPLANT_DETAILS")
@NamedQueries({
	@NamedQuery(name="ImplantDetails.findByClaimKey", query="SELECT i FROM ImplantDetails i where i.claimKey = :claimKey and i.activeStatus = 1"),
	@NamedQuery(name="ImplantDetails.findByTransactionKey", query="SELECT i FROM ImplantDetails i where i.transactionKey = :transactionKey and i.activeStatus = 1"),
	@NamedQuery(name="ImplantDetails.findByKey", query="SELECT i FROM ImplantDetails i where i.key = :primarykey")
})

public class ImplantDetails extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_IMPLANT_DTLS_KEY_GENERATOR", sequenceName = "SEQ_IMPLANT_DTLS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_IMPLANT_DTLS_KEY_GENERATOR")
	@Column(name = "IMPLANT_DTLS_KEY")
	private Long key;

	@Column(name = "CLAIM_KEY")
	private Long claimKey;

	@Column(name = "TRANSACTION_KEY")
	private Long transactionKey;

	@Column(name = "IMPLANT_NAME")
	private String implantName;

	@Column(name = "IMPLANT_COST")
	private Double implantCost; 
	
	@Column(name = "IMPLANT_TYPE")
	private String implantType; 

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "OLD_IMPLANT_NAME")
	private String oldImplantName;

	@Column(name = "OLD_IMPLANT_COST")
	private Double oldImplantCost; 
	
	@Column(name = "OLD_IMPLANT_TYPE")
	private String oldImplantType; 

	@Column(name = "OLD_IMSPLANT_FLAG")
	private String dcImplantFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getImplantName() {
		return implantName;
	}

	public void setImplantName(String implantName) {
		this.implantName = implantName;
	}

	public Double getImplantCost() {
		return implantCost;
	}

	public void setImplantCost(Double implantCost) {
		this.implantCost = implantCost;
	}

	public String getImplantType() {
		return implantType;
	}

	public void setImplantType(String implantType) {
		this.implantType = implantType;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOldImplantName() {
		return oldImplantName;
	}

	public void setOldImplantName(String oldImplantName) {
		this.oldImplantName = oldImplantName;
	}

	public Double getOldImplantCost() {
		return oldImplantCost;
	}

	public void setOldImplantCost(Double oldImplantCost) {
		this.oldImplantCost = oldImplantCost;
	}

	public String getOldImplantType() {
		return oldImplantType;
	}

	public void setOldImplantType(String oldImplantType) {
		this.oldImplantType = oldImplantType;
	}

	public String getDcImplantFlag() {
		return dcImplantFlag;
	}

	public void setDcImplantFlag(String dcImplantFlag) {
		this.dcImplantFlag = dcImplantFlag;
	}
	
}
