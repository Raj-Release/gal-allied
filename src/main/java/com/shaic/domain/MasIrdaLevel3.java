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
@Table(name="MAS_IRDA_LEVEL3")
@NamedQueries( {
@NamedQuery(name="MasIrdaLevel3.findAll", query="SELECT m FROM MasIrdaLevel3 m where m.activeStatus is not null and m.activeStatus = 1"),
@NamedQuery(name ="MasIrdaLevel3.findByKey",query="SELECT m FROM MasIrdaLevel3 m where m.key = :primaryKey"),
@NamedQuery(name ="MasIrdaLevel3.findByIrdaLevel2Key",query="SELECT m FROM MasIrdaLevel3 m where m.irdaLevel2.key = :irdaLevel2Key and m.activeStatus is not null and m.activeStatus = 1")
})

public class MasIrdaLevel3 {
	
	@Id
	@Column(name = "IRDA_LEVEL3_KEY")
	private Long key;
	
	@Column(name = "IRDA_CODE")
	//private  Long irdaCode;
	private String irdaCode;
	
	@Column(name = "IRDA_VALUE")
	private String irdaValue;
	
	@OneToOne
	@JoinColumn(name="IRDA_LEVEL1_KEY", nullable=false)
	private MasIrdaLevel1 irdaLevel1;
	
	@OneToOne
	@JoinColumn(name="IRDA_LEVEL2_KEY", nullable=false)
	private MasIrdaLevel2 irdaLevel2;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	
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

	public MasIrdaLevel1 getIrdaLevel1() {
		return irdaLevel1;
	}

	public void setIrdaLevel1(MasIrdaLevel1 irdaLevel1) {
		this.irdaLevel1 = irdaLevel1;
	}

	public MasIrdaLevel2 getIrdaLevel2() {
		return irdaLevel2;
	}

	public void setIrdaLevel2(MasIrdaLevel2 irdaLevel2) {
		this.irdaLevel2 = irdaLevel2;
	}

	public String getIrdaCode() {
		return irdaCode;
	}

	public void setIrdaCode(String irdaCode) {
		this.irdaCode = irdaCode;
	}


}
