package com.shaic.domain;

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

import com.shaic.domain.preauth.ProcedureMaster;

/**
 * The persistent class for the MAS_PROCEDURE_DAYCARE_MAPPING database table.
 * 
 */
@Entity
@Table(name = "MAS_PROCEDURE_DAYCARE_MAPPING")
@NamedQueries({
@NamedQuery(name = "MasProcedureDaycareMapping.findAll", query = "SELECT o FROM MasProcedureDaycareMapping o where o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name="MasProcedureDaycareMapping.findByProduct", query="SELECT o FROM MasProcedureDaycareMapping o where o.product.key = :productKey"),
})
public class MasProcedureDaycareMapping implements Serializable {


	private static final long serialVersionUID = 5389927757850005461L;

	@Id
	@Column(name = "\"PROC_DAYCARE_KEY\"")
	private long key;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name = "ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;	

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "DAY_CARE_FLAG")
	private String dayCareFlag;

	
	@OneToOne
	@JoinColumn(name = "PROCEDURE_KEY", nullable = false)
	private ProcedureMaster procedure;

	
	@OneToOne
	@JoinColumn(name = "PRODUCT_KEY", nullable = false)
	private Product product;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

//	@Column(name = "\"VERSION\"")
//	private Long version;
	
	@Column(name = "LIMIT")
	private Long limit;

	public MasProcedureDaycareMapping() {
	}

	public long getKey() {
		return this.key;
	}

	public void setKey(long key) {
		this.key = key;
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

	public String getDayCareFlag() {
		return this.dayCareFlag;
	}

	public void setDayCareFlag(String dayCareFlag) {
		this.dayCareFlag = dayCareFlag;
	}



	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}
	
	public ProcedureMaster getProcedure() {
		return procedure;
	}

	public void setProcedure(ProcedureMaster procedure) {
		this.procedure = procedure;
	}

	
}