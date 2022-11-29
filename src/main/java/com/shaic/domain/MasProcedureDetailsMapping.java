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
@Table(name = "MAS_PRODUCT_BENEFIT_PROCEDURE")
@NamedQueries({
@NamedQuery(name = "MasProcedureDetailsMapping.findAll", query = "SELECT o FROM MasProcedureDetailsMapping o where o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name="MasProcedureDetailsMapping.findByProduct", query="SELECT o FROM MasProcedureDetailsMapping o where o.product.key = :productKey"),
@NamedQuery(name="MasProcedureDetailsMapping.findByVersion", query="SELECT o FROM MasProcedureDetailsMapping o where o.product.key = :productKey and o.versionNumber = :productversionNumber"),
})
public class MasProcedureDetailsMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "\"PRC_KEY\"")
	private long key;
	
	@Column(name = "PROCEDURE_CODE")
	private Long procedureCode;
	
	@Column(name = "PROCEDURE_NAME")
	private String procedureName;


	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "VERSION_NUMBER")
	private Long versionNumber;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public Long getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(Long procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}
	
}
