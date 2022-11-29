package com.shaic.claim.processdatacorrectionhistorical.bean;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_CODHIS_NW_PP_CODING")
@NamedQueries({
	@NamedQuery(name="PPCodingHist.findAll", query="SELECT m FROM PPCodingHist m"),
	@NamedQuery(name="PPCodingHist.findByIntimationKey", query="SELECT m FROM PPCodingHist m WHERE m.intimationKey = :intimationKey AND m.ppVersion= :ppVersion AND ( m.deleteFlag = 0 OR m.deleteFlag IS NULL) ORDER BY m.ppCode ASC")

})
public class PPCodingHist extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1214017219477154929L;

	@Id
	@SequenceGenerator(name="IMS_CLS_NW_PP_CODING_GENERATOR", sequenceName = "SEQ_NW_PP_CODING", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_NW_PP_CODING_GENERATOR" ) 
	@Column(name = "PP_CODING_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "PP_CODE")
	private String ppCode;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;
	
	@Column(name = "HOSPITAL_KEY")
	private Long hospitalKey;
	
	@Column(name = "HOSPITAL_TYPE")
	private String hospitalType;
	
	@Column(name = "PP_SCORE")
	private String ppScore;
	
	@Column(name = "PP_STAGE")
	private String ppStage;
	
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
	
	@Column(name = "PP_VERSION")
	private Long ppVersion;
	
	@Column(name = "DELETED_FLAG")
	private Long deleteFlag;
	
	@Column(name = "OLD_PP_SCORE")
	private String oldppScore;
	
	@Column(name = "OLD_PP_CODING_KEY")
	private Long oldppCodingKey;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public String getPpCode() {
		return ppCode;
	}

	public void setPpCode(String ppCode) {
		this.ppCode = ppCode;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getPpScore() {
		return ppScore;
	}

	public void setPpScore(String ppScore) {
		this.ppScore = ppScore;
	}

	public String getPpStage() {
		return ppStage;
	}

	public void setPpStage(String ppStage) {
		this.ppStage = ppStage;
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

	public Long getPpVersion() {
		return ppVersion;
	}

	public void setPpVersion(Long ppVersion) {
		this.ppVersion = ppVersion;
	}

	public Long getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Long deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getOldppScore() {
		return oldppScore;
	}

	public void setOldppScore(String oldppScore) {
		this.oldppScore = oldppScore;
	}

	public Long getOldppCodingKey() {
		return oldppCodingKey;
	}

	public void setOldppCodingKey(Long oldppCodingKey) {
		this.oldppCodingKey = oldppCodingKey;
	}
	
}
