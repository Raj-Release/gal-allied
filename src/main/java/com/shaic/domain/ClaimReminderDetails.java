package com.shaic.domain;

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

@Entity
@Table(name="IMS_CLS_CLAIM_REMINDER")
@NamedQueries({
@NamedQuery(name="ClaimReminderDetails.findAll" ,query="SELECT c FROM ClaimReminderDetails c"),
@NamedQuery(name="ClaimReminderDetails.findSubBatchIdByBatchId" ,query="SELECT c.subBatchNo , count(c.subBatchNo) FROM ClaimReminderDetails c where Lower(c.batchNo) like :batchId group by c.subBatchNo"),
@NamedQuery(name="ClaimReminderDetails.findBySubBatchId" ,query="SELECT c FROM ClaimReminderDetails c where c.subBatchNo like :subBatchId"),
@NamedQuery(name="ClaimReminderDetails.findByBatchId" ,query="SELECT c FROM ClaimReminderDetails c where Lower(c.batchNo) like :batchId"),
@NamedQuery(name="ClaimReminderDetails.findByBatchIdExportList" ,query="SELECT c FROM ClaimReminderDetails c where Lower(c.batchNo) like :batchId and (c.batchFlag is null or (c.batchFlag is not null and c.batchFlag <> 'Y')) "),
@NamedQuery(name="ClaimReminderDetails.findByBatchIdGroup" ,query="SELECT c.batchNo, count(c.key) FROM ClaimReminderDetails c where Lower(c.batchNo) like :batchId group by c.batchNo"),
@NamedQuery(name="ClaimReminderDetails.findByBatchIntimationNoGroup" ,query="SELECT c.batchNo, count(c.batchNo) FROM ClaimReminderDetails c where Lower(c.intimatonNo) like :intimationNo group by c.batchNo"),
@NamedQuery(name="ClaimReminderDetails.findBySubBatchIntimationNoGroup" ,query="SELECT c.subBatchNo, count(c.subBatchNo) FROM ClaimReminderDetails c where Lower(c.intimatonNo) like :intimationNo group by c.subBatchNo"),
@NamedQuery(name="ClaimReminderDetails.findByBatchIntimationNo" ,query="SELECT c FROM ClaimReminderDetails c where Lower(c.intimatonNo) like :intimationNo"),
@NamedQuery(name="ClaimReminderDetails.findByIntimationNoAndCategory" ,query="SELECT c FROM ClaimReminderDetails c where Lower(c.intimatonNo) like :intimationNo and c.reminderCount = :reminderCount and c.category = :remCategory"),
@NamedQuery(name="ClaimReminderDetails.findBySubBatchIdGroup" ,query="SELECT c.subBatchNo FROM ClaimReminderDetails c where c.subBatchNo like :subBatchId group by c.subBatchNo"),
@NamedQuery(name="ClaimReminderDetails.findPrevBatchByBatchId" ,query="SELECT c.batchNo , count(c.batchNo) FROM ClaimReminderDetails c where c.createdDate >= :createdDate and c.createdDate <= :endDate  group by c.batchNo"),
@NamedQuery(name="ClaimReminderDetails.findPrevBatchBysubBatchId" ,query="SELECT c.subBatchNo , count(c.subBatchNo) FROM ClaimReminderDetails c where c.createdDate >= :createdDate and c.createdDate <= :endDate group by c.subBatchNo")
})
public class ClaimReminderDetails {
	
	@Id
	@SequenceGenerator(name="IMS_CLS_REMINDER_KEY_GENERATOR", sequenceName = "SEQ_REMINDER_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_REMINDER_KEY_GENERATOR" )
	@Column(name="REMINDER_KEY")
	private Long key;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimatonNo;
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="CLAIM_KEY")
	private Long claimKey;
	
	@Column(name="CLAIM_NUMBER")
	private String claimNo;
	
	@Column(name="TRANSACTION_KEY")
	private Long transacKey;
	
	@Column(name="QUERY_KEY")
	private Long queryKey;
	
	@Column(name="BATCH_NO")
	private String batchNo;
	
	@Column(name="SUB_BATCH_NO")
	private String subBatchNo;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;
	
	@Column(name="PRINT_FLAG")
	private String printFlag;
	
	@Column(name="PRINT_COUNT")
	private Integer printCount;
	
	@Column(name="REMINDER_COUNT")
	private Integer reminderCount;
	
	@Column(name="BATCH_FLAG")
	private String batchFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GENERATED_DATE")
	private Date generatedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;	
	
	@Column(name="REMINDER_TYPE")
	private String category;
		
	@Column(name="DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="CLAIM_TYPE")
	private String claimType;
	
	@Column(name="REMINDER_CATEGORY")
	private String reimainderCategory;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;	
	
	@Column(name="LOB_TYPE")
	private String lobTypeFlag;
	
	@Transient
	private Integer totalNoofRecords;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimatonNo() {
		return intimatonNo;
	}

	public void setIntimatonNo(String intimatonNo) {
		this.intimatonNo = intimatonNo;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public Long getTransacKey() {
		return transacKey;
	}

	public void setTransacKey(Long transacKey) {
		this.transacKey = transacKey;
	}

	public Long getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSubBatchNo() {
		return subBatchNo;
	}

	public void setSubBatchNo(String subBatchNo) {
		this.subBatchNo = subBatchNo;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public Integer getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getTotalNoofRecords() {
		return totalNoofRecords;
	}

	public void setTotalNoofRecords(Integer totalNoofRecords) {
		this.totalNoofRecords = totalNoofRecords;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getBatchFlag() {
		return batchFlag;
	}

	public void setBatchFlag(String batchFlag) {
		this.batchFlag = batchFlag;
	}

	public String getReimainderCategory() {
		return reimainderCategory;
	}

	public void setReimainderCategory(String reimainderCategory) {
		this.reimainderCategory = reimainderCategory;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getLobTypeFlag() {
		return lobTypeFlag;
	}

	public void setLobTypeFlag(String lobTypeFlag) {
		this.lobTypeFlag = lobTypeFlag;
	}	
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ClaimReminderDetails) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }
	
}