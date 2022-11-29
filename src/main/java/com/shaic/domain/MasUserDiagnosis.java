package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_GET_NEXT_DIAGNOSIS")
@NamedQueries({
	@NamedQuery(name="MasUserDiagnosis.findAll", query="SELECT m FROM MasUserDiagnosis m where m.activeStatus = 1 order by m.value asc ")
})
public class MasUserDiagnosis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@Column(name = "GET_NEXT_DIAGNOSIS_KEY")
	private Long key;
	
	@Column(name ="VALUE")
	private String value;
	
	
	@Column(name="ACTIVE_STATUS")
	private Boolean activeStatus;


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public Boolean getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

}
