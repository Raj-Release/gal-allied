package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MAS_STAGE_T database table.
 * 
 */
@Entity
@Table(name="MAS_STAGE")
@NamedQueries({
@NamedQuery(name="Stage.findAll", query="SELECT m FROM Stage m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name="Stage.findByName", query="SELECT m FROM Stage m where Lower(m.stageName) like :stageName"),
@NamedQuery(name="Stage.findByKey",query="SELECT o from Stage o WHERE o.key=:stageKey")


})
public class Stage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5281486837892765340L;

	@Id
	@Column(name="STAGE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	/*@Column(name="SEQUENCE_NUMBER")
	private Long sequenceNumber;*/


	/*@Column(name="STAGE_CODE")

	private String stageCode;*/

	@Column(name="STAGE_NAME")
	private String stageName;

//	@Column(name="VERSION")
//	private Long version;

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/*public Long getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;

	}*/




	/*public String getStageCode() {
		return this.stageCode;
	}

	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
<<<<<<< HEAD
	}
*/




	public String getStageName() {
		return this.stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

}