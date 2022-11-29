package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name = "MASB_LEGAL_RELATIONSHIP")
@NamedQueries({})
public class MasLegalRelationship extends AbstractEntity{

	@Id
	@Column(name="RELSHP_KEY")
	private Long key;
	
	@Column(name="RELATIONSHIP_ID")
	private Long relationshipID;
	
	@Column(name="RELATIONSHIP_CODE")
	private String relationshipCode;
	
	@Column(name="RELATIONSHIP_DESC")
	private String realtionshipDesc;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date cretaedDate;
	
	public Long getRelationshipID() {
		return relationshipID;
	}

	public void setRelationshipID(Long relationshipID) {
		this.relationshipID = relationshipID;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public String getRealtionshipDesc() {
		return realtionshipDesc;
	}

	public void setRealtionshipDesc(String realtionshipDesc) {
		this.realtionshipDesc = realtionshipDesc;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCretaedDate() {
		return cretaedDate;
	}

	public void setCretaedDate(Date cretaedDate) {
		this.cretaedDate = cretaedDate;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	

}
