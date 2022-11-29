
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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Intimation;

/**
 * The persistent class for the IMS_CLS_STAGE_INFORMATION_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_PED_HISTORY")
@NamedQueries({
	
	@NamedQuery(name = "PEDEndormentTrails.findAll", query = "SELECT i FROM StageInformation i"),
	@NamedQuery(name = "PEDEndormentTrails.findByPedKey", query = "SELECT i FROM PEDEndormentTrails i where i.ped_initiate_key = :pedKey order by i.key asc"),

})
public class PEDEndormentTrails extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PED_TRAIL_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;


	@Column(name = "STATUS", nullable = false)
	private String status;

	@Column(name = "CREATED_DATE",updatable = false,insertable = false)
	private Timestamp createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;

	@Column(name = "PED_INITIATE_KEY", nullable = false)
	private Long ped_initiate_key;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public Long getPed_initiate_key() {
		return ped_initiate_key;
	}

	public void setPed_initiate_key(Long ped_initiate_key) {
		this.ped_initiate_key = ped_initiate_key;
	}

	
	
   
}
