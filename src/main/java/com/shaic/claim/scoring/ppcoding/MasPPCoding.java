package com.shaic.claim.scoring.ppcoding;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="MAS_PP")
@NamedQueries({
	@NamedQuery(name="MasPPCoding.findAll", query="SELECT m FROM MasPPCoding m"),
	@NamedQuery(name="MasPPCoding.findDistinctCategory", query="SELECT m FROM MasPPCoding m WHERE m.activeStatus = 1 AND m.hospitalType = :hospitalType AND m.ppVersion= :ppVersion ORDER BY m.slno ASC")
})
public class MasPPCoding extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PP_KEY")
	private Long key;
	
	@Column(name = "SLNO")
	private Long slno;
	
	@Column(name = "HOSPITAL_TYPE")
	private String hospitalType;
	
	@Column(name = "PP_CODE")
	private String ppCode;
	
	@Column(name = "PP_CODING_DESC")
	private String ppCodingDesc;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "PP_VERSION")
	private Long ppVersion;
	
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getSlno() {
		return slno;
	}

	public void setSlno(Long slno) {
		this.slno = slno;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getPpCode() {
		return ppCode;
	}

	public void setPpCode(String ppCode) {
		this.ppCode = ppCode;
	}

	public String getPpCodingDesc() {
		return ppCodingDesc;
	}

	public void setPpCodingDesc(String ppCodingDesc) {
		this.ppCodingDesc = ppCodingDesc;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getPpVersion() {
		return ppVersion;
	}

	public void setPpVersion(Long ppVersion) {
		this.ppVersion = ppVersion;
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
	
}
