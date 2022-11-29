package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_IRDA_LEVEL1")
@NamedQueries( {
@NamedQuery(name="MasIrdaLevel1.findAll", query="SELECT m FROM MasIrdaLevel1 m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name ="MasIrdaLevel1.findByKey",query="SELECT m FROM MasIrdaLevel1 m where m.key = :primaryKey")
})

public class MasIrdaLevel1 {
	
	@Id
	@Column(name = "IRDA_LEVEL1_KEY")
	private Long key;
	
	@Column(name = "IRDA_CODE")
	//private  Long irdaCode;
	private  String irdaCode;
	
	@Column(name = "IRDA_VALUE")
	private String irdaValue;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	/*@Column(name = "REMARKS")
	private String remarks;*/
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	/*public Long getIrdaCode() {
		return irdaCode;
	}

	public void setIrdaCode(Long irdaCode) {
		this.irdaCode = irdaCode;
	}*/

	public String getIrdaValue() {
		return irdaValue;
	}

	public void setIrdaValue(String irdaValue) {
		this.irdaValue = irdaValue;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/*public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
*/
	public void setIrdaCode(String irdaCode) {
		this.irdaCode = irdaCode;
	}

	public String getIrdaCode() {
		return irdaCode;
	}

}
