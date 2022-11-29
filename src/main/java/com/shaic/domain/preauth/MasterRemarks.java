
package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
* The Master for MAS_DOC_REJECT.
* 
*/
@Entity
@Table(name = "MAS_DOC_REJECT")
@NamedQueries({
	@NamedQuery(name = "MasterRemarks.findAll", query = "SELECT i FROM MasterRemarks i where i.activeStatus = 1"),
	@NamedQuery(name = "MasterRemarks.findByRefKey", query = "SELECT i FROM MasterRemarks i where i.masRefKey=:masRefKey and i.activeStatus = 1"),
	@NamedQuery(name = "MasterRemarks.findByKey", query = "SELECT i FROM MasterRemarks i where i.key=:masterKey and i.activeStatus = 1"),
	@NamedQuery(name = "MasterRemarks.findByRejSubCategKey", query = "SELECT i FROM MasterRemarks i where i.rejSubCategKey = :rejSubCategKey and i.activeStatus = 1")
})

public class MasterRemarks implements Serializable {
	private static final long serialVersionUID = 3760145401215411454L;

	@Id
	@Column(name = "MASTER_KEY")
	private Long key;

	@Column(name = "REF_MASTER_ID")
	private Long masRefKey;
	
	@Column(name = "REJ_SUB_CATEGORY_ID")
	private Long rejSubCategKey;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "INSURED_REMARKS")
	private String insuredRemarks;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getMasRefKey() {
		return masRefKey;
	}

	public void setMasRefKey(Long masRefKey) {
		this.masRefKey = masRefKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInsuredRemarks() {		
		return insuredRemarks;
	}

	public void setInsuredRemarks(String insuredRemarks) {
		this.insuredRemarks = insuredRemarks;
	}

	public Long getRejSubCategKey() {
		return rejSubCategKey;
	}

	public void setRejSubCategKey(Long rejSubCategKey) {
		this.rejSubCategKey = rejSubCategKey;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}