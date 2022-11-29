package com.shaic.claim.processdatacorrection.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.domain.ReferenceTable;

public class ProcessDataCorrectionDTO extends AbstractTableDTO implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4115196985338366211L;

	private Long intimationKey;
	
	private Long transactionKey;
	
	private Long claimKey;
	
	private Long coadingKey;
	
	private List<DiganosisCorrectionDTO> diganosisCorrectionDTOs;
	
	private List<ProcedureCorrectionDTO> procedureCorrectionDTOs;
	
	private List<SpecialityCorrectionDTO> specialityCorrectionDTOs;
	
	private SelectValue covid19Variant;
	
	private SelectValue actualCovid19Variant;
	
	private SelectValue cocktailDrug;
	
	private SelectValue actualCocktailDrug;
	
	private String actualCocktailDrugFlag;
	
	private SelectValue roomCategory;
	
	private SelectValue proposedroomCategory;
	
	private Boolean ventilatorSupport;
	
	private Boolean proposedVentilatorSupport;
	
	private HospitalScoringDTO hospitalScoringDTO;
	
	private HospitalScoringDTO proposedScoringDTO;
	
	private SelectValue treatmentType;
	
	private ClaimDto claimDto;
	
	private Boolean isdiganosisChanged = false;
	
	private Boolean isprocedureChanged = false;
	
	private Boolean isspecialityChanged = false;
	
	private Boolean isroomCatChanged = false;
	
	private String remarks;
	
	private Boolean isCovid19VariantChanged = false;
	
	private Boolean isCocktailDrugChanged = false;
	
	private Map<String, Object> referenceData;
	
	private List<HospitalScoringDTO> scoringDTOs;
	
	private Boolean isScoringChanged = false;
	
	private String intimationNo;
	
	private List<TreatingCorrectionDTO> treatingCorrectionDTOs;
	
	private Boolean istreatingChanged = false;

	private String roomCat;

	private String proposedroomCat;

	private String treatment;
	
	private String icdExclusionReason;

	private List<ImplantCorrectionDTO> implantCorrectionDTOs;

	private Boolean isimplantChanged = false;
	
	private Boolean implantApplicable =false;
	
	private List<ImplantCorrectionDTO> deletedimplantDTOs;
	
	private Boolean implantcorrectionApplicable =false;
	
	private Boolean isPPChangesmade =false;
	
	private Boolean ppCodingSelected =false;
	
	private Map<String, Boolean> selectedPPCoding;
	private SelectValue admissionType;
	
	private SelectValue actualadmissionType;
	
	private String typeofAdmission;

	private String actualtypeofAdmission;
	
	private String claimType;
	
	private Boolean isCashlessChanges = false;
	
	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public List<DiganosisCorrectionDTO> getDiganosisCorrectionDTOs() {
		return diganosisCorrectionDTOs;
	}

	public void setDiganosisCorrectionDTOs(
			List<DiganosisCorrectionDTO> diganosisCorrectionDTOs) {
		this.diganosisCorrectionDTOs = diganosisCorrectionDTOs;
	}

	public List<ProcedureCorrectionDTO> getProcedureCorrectionDTOs() {
		return procedureCorrectionDTOs;
	}

	public void setProcedureCorrectionDTOs(
			List<ProcedureCorrectionDTO> procedureCorrectionDTOs) {
		this.procedureCorrectionDTOs = procedureCorrectionDTOs;
	}

	public List<SpecialityCorrectionDTO> getSpecialityCorrectionDTOs() {
		return specialityCorrectionDTOs;
	}

	public void setSpecialityCorrectionDTOs(
			List<SpecialityCorrectionDTO> specialityCorrectionDTOs) {
		this.specialityCorrectionDTOs = specialityCorrectionDTOs;
	}

	public HospitalScoringDTO getHospitalScoringDTO() {
		return hospitalScoringDTO;
	}

	public void setHospitalScoringDTO(HospitalScoringDTO hospitalScoringDTO) {
		this.hospitalScoringDTO = hospitalScoringDTO;
	}

	public HospitalScoringDTO getProposedScoringDTO() {
		return proposedScoringDTO;
	}

	public void setProposedScoringDTO(HospitalScoringDTO proposedScoringDTO) {
		this.proposedScoringDTO = proposedScoringDTO;
	}

	public SelectValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public SelectValue getProposedroomCategory() {
		return proposedroomCategory;
	}

	public void setProposedroomCategory(SelectValue proposedroomCategory) {
		this.proposedroomCategory = proposedroomCategory;
	}

	public SelectValue getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(SelectValue treatmentType) {
		this.treatmentType = treatmentType;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public Boolean getIsdiganosisChanged() {
		return isdiganosisChanged;
	}

	public void setIsdiganosisChanged(Boolean isdiganosisChanged) {
		this.isdiganosisChanged = isdiganosisChanged;
	}

	public Boolean getIsprocedureChanged() {
		return isprocedureChanged;
	}

	public void setIsprocedureChanged(Boolean isprocedureChanged) {
		this.isprocedureChanged = isprocedureChanged;
	}

	public Boolean getIsspecialityChanged() {
		return isspecialityChanged;
	}

	public void setIsspecialityChanged(Boolean isspecialityChanged) {
		this.isspecialityChanged = isspecialityChanged;
	}

	public Boolean getIsroomCatChanged() {
		return isroomCatChanged;
	}

	public void setIsroomCatChanged(Boolean isroomCatChanged) {
		this.isroomCatChanged = isroomCatChanged;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getCoadingKey() {
		return coadingKey;
	}

	public void setCoadingKey(Long coadingKey) {
		this.coadingKey = coadingKey;
	}

	public Map<String, Object> getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public List<HospitalScoringDTO> getScoringDTOs() {
		return scoringDTOs;
	}

	public void setScoringDTOs(List<HospitalScoringDTO> scoringDTOs) {
		this.scoringDTOs = scoringDTOs;
	}

	public Boolean getIsScoringChanged() {
		return isScoringChanged;
	}

	public void setIsScoringChanged(Boolean isScoringChanged) {
		this.isScoringChanged = isScoringChanged;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public List<TreatingCorrectionDTO> getTreatingCorrectionDTOs() {
		return treatingCorrectionDTOs;
	}

	public void setTreatingCorrectionDTOs(
			List<TreatingCorrectionDTO> treatingCorrectionDTOs) {
		this.treatingCorrectionDTOs = treatingCorrectionDTOs;
	}

	public Boolean getIstreatingChanged() {
		return istreatingChanged;
	}

	public void setIstreatingChanged(Boolean istreatingChanged) {
		this.istreatingChanged = istreatingChanged;
	}

	public String getRoomCat() {
		return roomCat;
	}

	public void setRoomCat(String roomCat) {
		this.roomCat = roomCat;
	}

	public String getProposedroomCat() {
		return proposedroomCat;
	}

	public void setProposedroomCat(String proposedroomCat) {
		this.proposedroomCat = proposedroomCat;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public List<ImplantCorrectionDTO> getImplantCorrectionDTOs() {
		return implantCorrectionDTOs;
	}

	public void setImplantCorrectionDTOs(
			List<ImplantCorrectionDTO> implantCorrectionDTOs) {
		this.implantCorrectionDTOs = implantCorrectionDTOs;
	}

	public Boolean getIsimplantChanged() {
		return isimplantChanged;
	}

	public void setIsimplantChanged(Boolean isimplantChanged) {
		this.isimplantChanged = isimplantChanged;
	}

	public Boolean getImplantApplicable() {
		return implantApplicable;
	}

	public void setImplantApplicable(Boolean implantApplicable) {
		this.implantApplicable = implantApplicable;
	}
	
	

	public List<ImplantCorrectionDTO> getDeletedimplantDTOs() {
		return deletedimplantDTOs;
	}

	public void setDeletedimplantDTOs(List<ImplantCorrectionDTO> deletedimplantDTOs) {
		this.deletedimplantDTOs = deletedimplantDTOs;
	}

	public Boolean getImplantcorrectionApplicable() {
		return implantcorrectionApplicable;
	}

	public void setImplantcorrectionApplicable(Boolean implantcorrectionApplicable) {
		this.implantcorrectionApplicable = implantcorrectionApplicable;
	}
	
	public String getIcdExclusionReason() {
		return icdExclusionReason;
	}

	public void setIcdExclusionReason(String icdExclusionReason) {
		this.icdExclusionReason = icdExclusionReason;
	}

	public Boolean getIsPPChangesmade() {
		return isPPChangesmade;
	}

	public void setIsPPChangesmade(Boolean isPPChangesmade) {
		this.isPPChangesmade = isPPChangesmade;
	}

	public Boolean getPpCodingSelected() {
		return ppCodingSelected;
	}

	public void setPpCodingSelected(Boolean ppCodingSelected) {
		this.ppCodingSelected = ppCodingSelected;
	}

	public Map<String, Boolean> getSelectedPPCoding() {
		return selectedPPCoding;
	}

	public void setSelectedPPCoding(Map<String, Boolean> selectedPPCoding) {
		this.selectedPPCoding = selectedPPCoding;
	}

	public SelectValue getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(SelectValue admissionType) {
		this.admissionType = admissionType;
	}

	public SelectValue getActualadmissionType() {
		return actualadmissionType;
	}

	public void setActualadmissionType(SelectValue actualadmissionType) {
		this.actualadmissionType = actualadmissionType;
	}

	public String getTypeofAdmission() {
		return typeofAdmission;
	}

	public void setTypeofAdmission(String typeofAdmission) {
		this.typeofAdmission = typeofAdmission;
	}

	public String getActualtypeofAdmission() {
		return actualtypeofAdmission;
	}

	public void setActualtypeofAdmission(String actualtypeofAdmission) {
		this.actualtypeofAdmission = actualtypeofAdmission;
	}

	public Boolean getVentilatorSupport() {
		return ventilatorSupport;
	}

	public void setVentilatorSupport(Boolean ventilatorSupport) {
		this.ventilatorSupport = ventilatorSupport;
	}

	public Boolean getProposedVentilatorSupport() {
		return proposedVentilatorSupport;
	}

	public void setProposedVentilatorSupport(Boolean proposedVentilatorSupport) {
		this.proposedVentilatorSupport = proposedVentilatorSupport;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public Boolean getIsCashlessChanges() {
		return isCashlessChanges;
	}

	public void setIsCashlessChanges(Boolean isCashlessChanges) {
		this.isCashlessChanges = isCashlessChanges;
	}


	public SelectValue getCovid19Variant() {
		return covid19Variant;
	}

	public void setCovid19Variant(SelectValue covid19Variant) {
		this.covid19Variant = covid19Variant;
	}

	public SelectValue getActualCovid19Variant() {
		return actualCovid19Variant;
	}

	public void setActualCovid19Variant(SelectValue actualCovid19Variant) {
		this.actualCovid19Variant = actualCovid19Variant;
	}

	public SelectValue getCocktailDrug() {
		return cocktailDrug;
	}

	public void setCocktailDrug(SelectValue cocktailDrug) {
		this.cocktailDrug = cocktailDrug;
	}

	public SelectValue getActualCocktailDrug() {
		return actualCocktailDrug;
	}

	public void setActualCocktailDrug(SelectValue actualCocktailDrug) {
		this.actualCocktailDrug = actualCocktailDrug;
		//this.actualCocktailDrugFlag = this.actualCocktailDrug != null && null != this.actualCocktailDrug.getValue() && this.actualCocktailDrug.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}
	
	public String getActualCocktailDrugFlag() {
		return actualCocktailDrugFlag;
	}

	public void setActualCocktailDrugFlag(String actualCocktailDrugFlag) {
		this.actualCocktailDrugFlag = actualCocktailDrugFlag;
		/*this.cocktailDrug = new SelectValue();
		this.cocktailDrug.setId((this.actualCocktailDrugFlag != null && this.actualCocktailDrugFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : (this.actualCocktailDrugFlag != null && this.actualCocktailDrugFlag.equalsIgnoreCase("N")) ? ReferenceTable.COMMONMASTER_NO : 0l);*/
	}
	

	public Boolean getIsCovid19VariantChanged() {
		return isCovid19VariantChanged;
	}

	public void setIsCovid19VariantChanged(Boolean isCovid19VariantChanged) {
		this.isCovid19VariantChanged = isCovid19VariantChanged;
	}

	public Boolean getIsCocktailDrugChanged() {
		return isCocktailDrugChanged;
	}

	public void setIsCocktailDrugChanged(Boolean isCocktailDrugChanged) {
		this.isCocktailDrugChanged = isCocktailDrugChanged;
	}
	
}
