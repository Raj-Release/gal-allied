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
@Table(name="IMS_CLS_CODHIS_NW_HOSP_SCORE")
@NamedQueries({
	@NamedQuery(name="HospitalScoringHist.findAll", query="SELECT m FROM HospitalScoring m"),
	@NamedQuery(name="HospitalScoringHist.findByIntimationKey", query="SELECT m FROM HospitalScoring m WHERE m.intimationKey = :intimationKey ORDER BY m.subCategoryKey ASC"),
	@NamedQuery(name="HospitalScoringHist.findScoringByKey", query="SELECT m FROM HospitalScoring m WHERE m.key = :primaryKey")

})
public class HospitalScoringHist extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4409529549425551904L;

	@Id
	@Column(name = "NW_HOSP_SCORE_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;
	
	@Column(name = "HOSPITAL_KEY")
	private Long hospitalKey;
	
	@Column(name = "SUB_CATEGORY_KEY")
	private Long subCategoryKey;
	
	@Column(name = "GRADE_SCORE")
	private String gradeScore;
	
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
	
	@Column(name = "HS_VERSION")
	private Integer scoringVersion;
	
	@Column(name = "DELETED_FLAG")
	private Integer deleteFlag;
	
	@Column(name = "OLD_GRADE_SCORE")
	private String oldGradeScore;
	
	@Override
	public Long getKey() {
		return key;
	}

	@Override
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

	public Long getSubCategoryKey() {
		return subCategoryKey;
	}

	public void setSubCategoryKey(Long subCategoryKey) {
		this.subCategoryKey = subCategoryKey;
	}

	public String getGradeScore() {
		return gradeScore;
	}

	public void setGradeScore(String gradeScore) {
		this.gradeScore = gradeScore;
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

	public Integer getScoringVersion() {
		return scoringVersion;
	}

	public void setScoringVersion(Integer scoringVersion) {
		this.scoringVersion = scoringVersion;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getOldGradeScore() {
		return oldGradeScore;
	}

	public void setOldGradeScore(String oldGradeScore) {
		this.oldGradeScore = oldGradeScore;
	}

}
