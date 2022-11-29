/**
 * @author ntv.vijayar
 *
 */

package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="MAS_IRDA_LEVEL2")
@NamedQueries( {
@NamedQuery(name="MasIrdaLevel2.findAll", query="SELECT m FROM MasIrdaLevel2 m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name ="MasIrdaLevel2.findByKey",query="SELECT m FROM MasIrdaLevel2 m where m.key = :primaryKey"),
@NamedQuery(name ="MasIrdaLevel2.findByIrdaLevel1Key",query="SELECT m FROM MasIrdaLevel2 m where m.irdaLevel1.key = :irdaLevel1Key and m.activeStatus is not null and m.activeStatus = 1")
})


public class MasIrdaLevel2 {
	
	@Id
	@Column(name = "IRDA_LEVEL2_KEY")
	private Long key;
	
	@Column(name = "IRDA_CODE")
	//private  Long irdaCode;
	private String irdaCode;
	
	@Column(name = "IRDA_VALUE")
	private String irdaValue;
	
	@OneToOne
	@JoinColumn(name="IRDA_LEVEL1_KEY", nullable=false)
	private MasIrdaLevel1 irdaLevel1;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
/*	@Column(name = "REMARKS")
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

/*	public Long getIrdaCode() {
		return irdaCode;
	}

	public void setIrdaCode(Long irdaCode) {
		this.irdaCode = irdaCode;
	}
*/
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

	public MasIrdaLevel1 getIrdaLevel1() {
		return irdaLevel1;
	}

	public void setIrdaLevel1(MasIrdaLevel1 irdaLevel1) {
		this.irdaLevel1 = irdaLevel1;
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
