package com.shaic.domain.skipZMR;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
@Entity
@Table(name="MAS_CPU_STAGE_MAPPING")
@NamedQueries({
	@NamedQuery(name="CPUStageMapping.findAll", query="SELECT m FROM CPUStageMapping m where m.activeStatus is not null and m.activeStatus = 1 "),
	@NamedQuery(name="CPUStageMapping.findByCPUCode", query="SELECT m FROM CPUStageMapping m where m.cpuCode = :cpuCode"),
})
public class CPUStageMapping  extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4946293034390117078L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_CPU_STAGE_KEY_GENERATOR", sequenceName = "SEQ_CPU_STAGE_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_CPU_STAGE_KEY_GENERATOR")
	@Column(name="CPUSTG_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;
	
	@Column(name="CASHLESS_FLAG")
	private String cashlessFlag;
	
	@Column(name="REIMBURSEMENT_FLAG")
	private String reimbursementFlag;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;


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

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCashlessFlag() {
		return cashlessFlag;
	}

	public void setCashlessFlag(String cashlessFlag) {
		this.cashlessFlag = cashlessFlag;
	}

	public String getReimbursementFlag() {
		return reimbursementFlag;
	}

	public void setReimbursementFlag(String reimbursementFlag) {
		this.reimbursementFlag = reimbursementFlag;
	}
}
