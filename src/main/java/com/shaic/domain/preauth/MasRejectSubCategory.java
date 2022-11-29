package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAS_REJECT_SUB_CATEGORY")
@NamedQueries({
	@NamedQuery(name = "MasRejectSubCategory.findAll", query = "SELECT i FROM MasRejectSubCategory i where i.activeStatus = 1"),
	@NamedQuery(name = "MasRejectSubCategory.findByKey", query = "SELECT i FROM MasRejectSubCategory i where i.key = :primaryKey and i.activeStatus = 1"),
	@NamedQuery(name = "MasRejectSubCategory.findByRejCategKey", query = "SELECT i FROM MasRejectSubCategory i where i.masRejCategKey = :masRejCategKey and i.activeStatus = 1")
})

public class MasRejectSubCategory  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REJ_SUB_CATEGORY_ID")
	private Long key;
	
	@Column(name = "REJ_CATEGORY_ID")
	private Long masRejCategKey;
	
	@Column(name = "DESCRIPTION")
	private String value;

	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getMasRejCategKey() {
		return masRejCategKey;
	}

	public void setMasRejCategKey(Long masRejCategKey) {
		this.masRejCategKey = masRejCategKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}	
	
}