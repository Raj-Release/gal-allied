/**
 * 
 */
package com.shaic.domain.outpatient;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.OPClaim;

/**
 * @author ntv.narasimhaj
 *
 */

@Entity
@Table(name="IMS_CLS_OP_SPECIALITY")
@NamedQueries({
@NamedQuery(name="OPSpeciality.findAll", query="SELECT s FROM OPSpeciality s"),
@NamedQuery(name ="OPSpeciality.findByKey",query="SELECT s FROM OPSpeciality s WHERE s.key = :key"),
@NamedQuery(name ="OPSpeciality.findByHealthCheckUpKey",query="SELECT s FROM OPSpeciality s WHERE s.opHealthCheckup.key = :healthCheckKey")
})
public class OPSpeciality extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_SPECIALITY_GENERATOR", sequenceName = "SEQ_OP_SPECIALITY_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_SPECIALITY_GENERATOR")
	@Column(name="OP_SPECIALITY_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private OPClaim claim;
	
	@Column(name="SPECIALITY_TYPE_ID")
	private Long specialityTypeId;
	
	@Column(name="PED_NAME")
	private String pedName;
	
	@Column(name="PED_RELATED_ID")
	private Long pedRelatedId;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public OPClaim getClaim() {
		return claim;
	}

	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}

	public Long getSpecialityTypeId() {
		return specialityTypeId;
	}

	public void setSpecialityTypeId(Long specialityTypeId) {
		this.specialityTypeId = specialityTypeId;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public Long getPedRelatedId() {
		return pedRelatedId;
	}

	public void setPedRelatedId(Long pedRelatedId) {
		this.pedRelatedId = pedRelatedId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}
