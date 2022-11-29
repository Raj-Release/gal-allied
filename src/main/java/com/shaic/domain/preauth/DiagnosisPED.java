package com.shaic.domain.preauth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.shaic.domain.MastersValue;

@Entity
@Table(name = "IMS_CLS_PED_DIAGNOSIS")
@NamedQueries({
		@NamedQuery(name = "DiagnosisPED.findAll", query = "SELECT i FROM DiagnosisPED i"),
		@NamedQuery(name = "DiagnosisPED.findByPEDValidationKey", query = "SELECT i FROM DiagnosisPED i where i.pedValidation is not null and i.pedValidation.key = :pedValidationKey"),
})
public class DiagnosisPED extends AbstractEntity {
	private static final long serialVersionUID = 3355913995845606787L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PED_DIAGNOSIS_KEY_GENERATOR", sequenceName = "SEQ_PED_DIAGNOSIS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_DIAGNOSIS_KEY_GENERATOR" ) 
	@Column(name="PED_DIAGNOSIS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIAGNOSIS_KEY", nullable = true)
	private PedValidation pedValidation;
	
	@Column(name = "PED_CODE")
	private String pedCode;

	@Column(name = "PED_NAME")
	private String pedName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIAGONSIS_IMPACT_ID", nullable = true)
	private MastersValue diagonsisImpact;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXCLUSION_DETAILS_ID", nullable = true)
	private ExclusionDetails exclusionDetails;
	
	@Column(name = "DIAGNOSIS_REMARKS")
	private String diagnosisRemarks;
	
	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public PedValidation getPedValidation() {
		return pedValidation;
	}

	public void setPedValidation(PedValidation pedValidation) {
		this.pedValidation = pedValidation;
	}

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public String getDiagnosisRemarks() {
		return diagnosisRemarks;
	}

	public void setDiagnosisRemarks(String diagnosisRemarks) {
		this.diagnosisRemarks = diagnosisRemarks;
	}

	public MastersValue getDiagonsisImpact() {
		return diagonsisImpact;
	}

	public void setDiagonsisImpact(MastersValue diagonsisImpact) {
		this.diagonsisImpact = diagonsisImpact;
	}

	public ExclusionDetails getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(ExclusionDetails exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}
}
