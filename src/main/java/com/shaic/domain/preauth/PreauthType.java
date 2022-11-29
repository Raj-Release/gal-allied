package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_PRE_AUTH_TYPE database table.
 */
 
@Entity
@Table(name="MAS_PRE_AUTH_TYPE")
@NamedQueries({
@NamedQuery(name="PreauthType.findAll", query="SELECT m FROM PreauthType m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name = "PreauthType.findByKey", query = "select o from PreauthType o where o.stage.key = :parentKey"),

})

public class PreauthType implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PRE_AUTH_TYPE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;


	/*@Column(name="PRE_AUTH_TYPE")
	private String preauthType;*/

/*	@Column(name="CODE")
	private String code;*/
	
	@OneToOne
	@JoinColumn(name="STAGE_KEY", nullable=false)
	private Stage stage;



	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="PRE_AUTH_STATUS")
	private String preauthStatus;

//	@Column(name="VERSION")
//	private Long version;

	public PreauthType() {
	}

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


	public String getPreauthStatus() {
		return preauthStatus;
	}

	public void setPreauthStatus(String preauthStatus) {
		this.preauthStatus = preauthStatus;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}