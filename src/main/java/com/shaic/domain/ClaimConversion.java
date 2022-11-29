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
@Table(name="IMS_CLS_CLAIM_CONVERSION")

@NamedQueries({
@NamedQuery(name="ClaimConversion.findAll" ,query="SELECT c FROM ClaimConversion c"),
@NamedQuery(name="ClaimConversion.findByKey" ,query="SELECT c FROM ClaimConversion c where c.key = :key"),
@NamedQuery(name="ClaimConversion.findSubBatchIdByBatchId" ,query="SELECT c.subBatchNo , count(c.subBatchNo) FROM ClaimConversion c where Lower(c.batchNo) like :batchId group by c.subBatchNo"),
@NamedQuery(name="ClaimConversion.findBySubBatchId" ,query="SELECT c FROM ClaimConversion c where c.subBatchNo like :subBatchId order by c.key desc"),
@NamedQuery(name="ClaimConversion.findByBatchId" ,query="SELECT c FROM ClaimConversion c where Lower(c.batchNo) like :batchId order by c.key desc"),
@NamedQuery(name="ClaimConversion.findByBatchIdGroup" ,query="SELECT c.batchNo, count(c.key) FROM ClaimConversion c where Lower(c.batchNo) like :batchId group by c.batchNo"),
@NamedQuery(name="ClaimConversion.findByBatchIntimationNoGroup" ,query="SELECT c.batchNo, count(c.batchNo) FROM ClaimConversion c where Lower(c.intimationNo) like :intimationNo group by c.batchNo"),
@NamedQuery(name="ClaimConversion.findBySubBatchIntimationNoGroup" ,query="SELECT c.subBatchNo, count(c.subBatchNo) FROM ClaimConversion c where Lower(c.intimationNo) like :intimationNo group by c.subBatchNo"),
@NamedQuery(name="ClaimConversion.findByBatchIntimationNo" ,query="SELECT c FROM ClaimConversion c where Lower(c.intimationNo) like :intimationNo"),
@NamedQuery(name="ClaimConversion.findBySubBatchIdGroup" ,query="SELECT c.subBatchNo FROM ClaimConversion c where c.subBatchNo like :subBatchId group by c.subBatchNo"),
@NamedQuery(name="ClaimConversion.findPrevBatchByBatchId" ,query="SELECT c.batchNo , count(c.batchNo) FROM ClaimConversion c where c.createdDate >= :createdDate and c.createdDate <= :endDate  group by c.batchNo"),
@NamedQuery(name="ClaimConversion.findPrevBatchBysubBatchId" ,query="SELECT c.subBatchNo , count(c.subBatchNo) FROM ClaimConversion c where c.createdDate >= :createdDate and c.createdDate <= :endDate group by c.subBatchNo")
})
public class ClaimConversion {

	@Id
	@SequenceGenerator(name="IMS_CLS_CLM_CONVERT_KEY_GENERATOR", sequenceName = "SEQ_REMINDER_KEY" , allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="IMS_CLS_CLM_CONVERT_KEY_GENERATOR" )
	@Column(name = "CONVERSION_KEY")
	Long key;	
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNo;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;

	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "CASHLESS_KEY")
	private Long preauthKey;
	
	@Column(name = "BATCH_NO")
	private String batchNo;
	
	@Column(name = "SUB_BATCH_NO")
	private String subBatchNo;
	
	@Column(name = "CPU_CODE")
	private Long cpuCode;
	
	@Column(name = "CONVERT_TO")
	private String convertToType;
	
	@Column(name = "PRINT_FLAG")
	private String printFlag;
	
	@Column(name = "PRINT_COUNT")
	private Long printCount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GENERATED_DATE")
	private Date letterDate; 
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long docToken;
	
	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate; 
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
		
	@Transient
	private Integer totalNoofRecords;
	
	@Column(name = "CONVERSION_CATEGORY")
	private String category;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
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

	public String getConvertToType() {
		return convertToType;
	}

	public void setConvertToType(String convertToType) {
		this.convertToType = convertToType;
	}

	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public Long getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Long printCount) {
		this.printCount = printCount;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public Long getDocToken() {
		return docToken;
	}

	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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

	public Integer getTotalNoofRecords() {
		return totalNoofRecords;
	}

	public void setTotalNoofRecords(Integer totalNoofRecords) {
		this.totalNoofRecords = totalNoofRecords;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}	
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ClaimConversion) obj).getKey());
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
