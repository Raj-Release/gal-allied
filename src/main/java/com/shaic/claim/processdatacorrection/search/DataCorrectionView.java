package com.shaic.claim.processdatacorrection.search;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

public interface DataCorrectionView extends GMVPView{

	public void setReferenceData(Map<String, Object> referenceData);

	void buildSuccessLayout();
	
	void setScoringchanges(Boolean scoringchanges,List<HospitalScoringDTO> scoringDTOs,Boolean isPPChangesmade,Boolean ppCodingSelected,Map<String, Boolean> selectedPPCoding);
	
	void getcorrectionProcedureValues(BeanItemContainer<SelectValue> procedures);
	
	void addSpecialityEdited(SpecialityCorrectionDTO specialityCorrectionDTO);
	
	void removeSpecialityEdited(Long key);
	
	void addSpecialityProcedure(Long specId,ComboBox prodcomboBox,SelectValue procValue);

	void addDiganosisEdited(DiganosisCorrectionDTO diganosisCorrectionDTO);

	void deleteactualDiganosis(Long key);
	
	void addProcedureEdited(ProcedureCorrectionDTO procedureCorrectionDTO);

	void deleteactualProcedure(Long key);
	
	void addTreatingEdited(TreatingCorrectionDTO treatingCorrectionDTO);

	void deleteactualTreating(Long key);
	
	void addImplantEdited(ImplantCorrectionDTO implantCorrectionDTO);

	void deleteactualImplant(Long key);
	
	void generateFieldsBasedOnImplantApplicable(Boolean isCked);
	
	void generatePPCoadingField(Boolean ischecked,List<PPCodingDTO> codingDTOs,Map<String,Boolean> selectedPPCoding);
	void generateFieldsBasedOnTOA(Boolean isCked);
	void resetScoringchanges();
	
	void generateCovid19VariantFields(Boolean covid19Variantenable);
	
}
