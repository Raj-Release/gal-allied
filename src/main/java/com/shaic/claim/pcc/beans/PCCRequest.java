package com.shaic.claim.pcc.beans;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_PCC_REQUEST")
@NamedQueries({
	@NamedQuery(name = "PCCRequest.findByKey", query = "SELECT i FROM PCCRequest i where i.key = :key"),
	@NamedQuery(name ="PCCRequest.findByintimationNo",query="SELECT r FROM PCCRequest r WHERE r.intimationNo = :intimationNo order by r.key desc"),

})
public class PCCRequest extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_PCC_KEY_GENERATOR", sequenceName = "SEQ_PCC_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_PCC_KEY_GENERATOR")
	@Column(name = "PCC_KEY")
	private Long key;

	@Column(name = "INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNo;

	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "CLAIM_STAGE")
	private String claimStage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_CATEGORY", nullable = false)
	private PCCCategory pccCategory;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_SUB_CATEGORY", nullable = false)
	private PCCSubCategory subCategory;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_SUB2_CATEGORY", nullable = false)
	private PCCSubCategoryTwo subCategoryTwo;
	
	@Column(name = "PCC_DOCTOR_REMARKS")
	private String pccDoctorRemarks;

	@Column(name = "INITIATE_BY")
	private String initiateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INITIATED_DATE")
	private Date initiateDate;
	
	@Column(name = "NEGOTIATED_AMOUNT")
	private Long negotiatedAmount;
	
	@Column(name = "SAVED_AMOUNT")
	private Long savedAmount;

	@Column(name = "REMARKS")
	private String pccCoordinatorRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_KEY", nullable = false)
	private Status status;

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
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_SOURCE", nullable = false)
	private MastersValue pccSource;
	
	@Column(name = "LOCK_FLAG")
	private String lockFlag;
	
	@Column(name = "LOCKED_BY")
	private String lockedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOCKED_DATE")
	private Date lockedDate;
	
	@Column(name = "PROCESSOR_REMARK")
	private String processorRemark;
	
	@Column(name = "ZONAL_REMARK")
	private String zonalRemark;
	
	@Column(name = "UPLOAD_FILE_REMARKS")
	private String uploadFileRemarks;

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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public PCCCategory getPccCategory() {
		return pccCategory;
	}

	public void setPccCategory(PCCCategory pccCategory) {
		this.pccCategory = pccCategory;
	}

	public PCCSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(PCCSubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public PCCSubCategoryTwo getSubCategoryTwo() {
		return subCategoryTwo;
	}

	public void setSubCategoryTwo(PCCSubCategoryTwo subCategoryTwo) {
		this.subCategoryTwo = subCategoryTwo;
	}

	public String getInitiateBy() {
		return initiateBy;
	}

	public void setInitiateBy(String initiateBy) {
		this.initiateBy = initiateBy;
	}

	public Date getInitiateDate() {
		return initiateDate;
	}

	public void setInitiateDate(Date initiateDate) {
		this.initiateDate = initiateDate;
	}

	public Long getNegotiatedAmount() {
		return negotiatedAmount;
	}

	public void setNegotiatedAmount(Long negotiatedAmount) {
		this.negotiatedAmount = negotiatedAmount;
	}

	public Long getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getPccDoctorRemarks() {
		return pccDoctorRemarks;
	}

	public void setPccDoctorRemarks(String pccDoctorRemarks) {
		this.pccDoctorRemarks = pccDoctorRemarks;
	}

	public String getPccCoordinatorRemarks() {
		return pccCoordinatorRemarks;
	}

	public void setPccCoordinatorRemarks(String pccCoordinatorRemarks) {
		this.pccCoordinatorRemarks = pccCoordinatorRemarks;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public MastersValue getPccSource() {
		return pccSource;
	}

	public void setPccSource(MastersValue pccSource) {
		this.pccSource = pccSource;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public String getProcessorRemark() {
		return processorRemark;
	}

	public void setProcessorRemark(String processorRemark) {
		this.processorRemark = processorRemark;
	}

	public String getZonalRemark() {
		return zonalRemark;
	}

	public void setZonalRemark(String zonalRemark) {
		this.zonalRemark = zonalRemark;
	}

	public String getUploadFileRemarks() {
		return uploadFileRemarks;
	}

	public void setUploadFileRemarks(String uploadFileRemarks) {
		this.uploadFileRemarks = uploadFileRemarks;
	}
	
	
}
