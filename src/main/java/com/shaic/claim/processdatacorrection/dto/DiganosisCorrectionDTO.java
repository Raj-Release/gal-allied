package com.shaic.claim.processdatacorrection.dto;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class DiganosisCorrectionDTO extends AbstractTableDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4046153966984914077L;

	private Long key;
	
	private SelectValue diagnosisName;
	
	private SelectValue icdCode;
	
	private SelectValue proposeddiagnosisName;
	
	private SelectValue proposedicdCode;
	
	private Boolean hasChanges = false;
	
	private int serialNo;
	
	private Boolean primaryDiagnosis;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public SelectValue getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(SelectValue icdCode) {
		this.icdCode = icdCode;
	}

	public SelectValue getProposeddiagnosisName() {
		return proposeddiagnosisName;
	}

	public void setProposeddiagnosisName(SelectValue proposeddiagnosisName) {
		this.proposeddiagnosisName = proposeddiagnosisName;
	}

	public SelectValue getProposedicdCode() {
		return proposedicdCode;
	}

	public void setProposedicdCode(SelectValue proposedicdCode) {
		this.proposedicdCode = proposedicdCode;
	}

	public Boolean getHasChanges() {
		return hasChanges;
	}

	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public Boolean getPrimaryDiagnosis() {
		return primaryDiagnosis;
	}

	public void setPrimaryDiagnosis(Boolean primaryDiagnosis) {
		this.primaryDiagnosis = primaryDiagnosis;
	}
	
	public SelectValue getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(SelectValue diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	

}
