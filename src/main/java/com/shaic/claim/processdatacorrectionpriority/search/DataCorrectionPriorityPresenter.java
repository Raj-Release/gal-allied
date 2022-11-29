package com.shaic.claim.processdatacorrectionpriority.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizard;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.shaic.claim.scoring.ppcoding.PPCodingService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

@ViewInterface(DataCorrectionPriorityView.class)
public class DataCorrectionPriorityPresenter extends AbstractMVPPresenter<DataCorrectionPriorityView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -256384569425809859L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private SearchProcessDataCorrectionPriorityService correctionService;
	
	@EJB
	private PPCodingService codingService;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	public static final String SET_TABLE_DATA_PRIORITY = "set_table_data_priority";
	
	public static final String SUBMIT_BUTTON_DATA_CODING_PRIORITY = "submit_button_data_coding_priority";
	
	public static final String SUBMIT_HOSPITAL_SCORING_PRIORITY = "submit_hospital_scoring_priority";
	
	public static final String GET_CORRECTION_PROCEDURE_VALUES_PRIORITY = "get_correction_procedure_values_priority";

	public static final String EDIT_CORRECTION_SPECIALITY_VALUES_PRIORITY = "edit_correction_speciality_values_priority";
	
	public static final String DELETE_CORRECTION_SPECIALITY_VALUES_PRIORITY = "delete_correction_speciality_values_priority";
	
	public static final String ADD_SPECIALITY_PROCEDURE_VALUES_PRIORITY = "add_speciality_procedure_values_priority";

	public static final String EDIT_DIGANOSIS_CORRECTION_VALUES_PRIORITY = "edit_diganosis_correction_values_priority";

	public static final String DELETE_DIGANOSIS_CORRECTION_VALUES_PRIORITY = "delete_diganosis_correction_values_priority";
	
	public static final String EDIT_PROCEDURE_CORRECTION_VALUES_PRIORITY = "edit_procedure_correction_values_priority";

	public static final String DELETE_PROCEDURE_CORRECTION_VALUES_PRIORITY = "delete_procedure_correction_values_priority";
	
	public static final String EDIT_TREATING_CORRECTION_VALUES_PRIORITY = "edit_treating_correction_values_priority";

	public static final String DELETE_TREATING_CORRECTION_VALUES_PRIORITY = "delete_treating_correction_values_priority";
	
	public static final String EDIT_IMPLANT_CORRECTION_VALUES_PRIORITY = "edit_implant_correction_values_priority";

	public static final String DELETE_IMPLANT_CORRECTION_VALUES_PRIORITY = "delete_implant_correction_values_priority";
	
	public static final String ACTUAL_IMPLANT_APPLICABLE_CHANGED_PRIORITY = "actual_implant_applicable_changed_priority";
	
	public static final String DC_PP_GENERATE_EVENT_PRIORITY = "dc_pp_generate_event_priority";
	
	public static final String ACTUAL_TOA_CHANGED_PRIORITY = "actual_toa_changed_priority";
	
	public static final String RESET_HOSPITAL_SCORING_PRIORITY = "reset_hospital_scoring_priority";
	
	public static final String REFERENCE_DATA_CLEAR_PRIORITY = "reference_clear_data_coding_priority";
	
	public static final String DATA_VALIDATION_PRIORITY_GENERATE_COVID19VARIANT_FIELDS = "data_validation_priority_generate_covid19vaiantfields";
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setReferenceData(@Observes @CDIEvent(SET_TABLE_DATA_PRIORITY) final ParameterDTO parameters) {
		
		referenceData.put("treatmentType", masterService.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("diagnosisName", masterService.getDiagnosisList());
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		BeanItemContainer<SelectValue> procedureCodeListNames = preauthService.getProcedureCodeList();
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureCodeListNames);
		referenceData.put("specialityType", preauthService.getAllSpecialityType());
		referenceData.put("typeofAdmission", masterService.getTypeOfAdmissionTypes());
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));	
		referenceData.put("covid19Variant", masterService.getSelectValueContainer(ReferenceTable.COVID19VARIANT));
		referenceData.put("cocktailDrugs", masterService.getSelectValueContainer(ReferenceTable.COCKTAILDRUG));
		
		view.setReferenceData(referenceData);
	}
	
	public void saveCorrectionDatas(@Observes @CDIEvent(SUBMIT_BUTTON_DATA_CODING_PRIORITY) final ParameterDTO parameters) {
				
		ProcessDataCorrectionDTO dataCorrectionDTO = (ProcessDataCorrectionDTO) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		correctionService.saveDataCorrection(dataCorrectionDTO,userName);
		view.buildSuccessLayout();
		
	}
	
	public void setScoringchanges(@Observes @CDIEvent(SUBMIT_HOSPITAL_SCORING_PRIORITY) final ParameterDTO parameters) {
		List<HospitalScoringDTO> scoringDTOs = (List<HospitalScoringDTO>) parameters.getPrimaryParameter();
		Boolean ppCodingSelected = (Boolean)parameters.getSecondaryParameter(0, Boolean.class);
		String intimationNumber = (String)parameters.getSecondaryParameter(2, String.class);
		Map<String, Boolean> selectedPPCoding = null;
		if(ppCodingSelected !=null && !ppCodingSelected){
			selectedPPCoding = (Map<String, Boolean>)parameters.getSecondaryParameter(1, Map.class);
		}		
		Boolean isPPChangesmade = correctionService.isPPChangesmade(ppCodingSelected,selectedPPCoding,intimationNumber);
		view.setScoringchanges(correctionService.iscScoringChangesmade(scoringDTOs),scoringDTOs,isPPChangesmade,ppCodingSelected,selectedPPCoding);
	}
	
	public void getcorrectionProcedureValues(@Observes @CDIEvent(GET_CORRECTION_PROCEDURE_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.getcorrectionProcedureValues(procedures);
	}
	
	public void addSpecialityEdited(@Observes @CDIEvent(EDIT_CORRECTION_SPECIALITY_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		SpecialityCorrectionDTO specialistdto = (SpecialityCorrectionDTO) parameters.getPrimaryParameter();
		specialistdto.setHasChanges(true);
		view.addSpecialityEdited(specialistdto);
	}
	
	public void deleteactualSpeciality(@Observes @CDIEvent(DELETE_CORRECTION_SPECIALITY_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long specialistKey = (Long) parameters.getPrimaryParameter();
		view.removeSpecialityEdited(specialistKey);
	}
	
	public void addSpecialityProcedure(@Observes @CDIEvent(ADD_SPECIALITY_PROCEDURE_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long specId = (Long) parameters.getPrimaryParameter();
		ComboBox prodcomboBox = (ComboBox)parameters.getSecondaryParameter(0, ComboBox.class);
		SelectValue procValue = (SelectValue)parameters.getSecondaryParameter(1, SelectValue.class);
		view.addSpecialityProcedure(specId,prodcomboBox,procValue);
	}
	
	public void addDiganosisEdited(@Observes @CDIEvent(EDIT_DIGANOSIS_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		DiganosisCorrectionDTO diganosisCorrectionDTO = (DiganosisCorrectionDTO) parameters.getPrimaryParameter();
		diganosisCorrectionDTO.setHasChanges(true);
		view.addDiganosisEdited(diganosisCorrectionDTO);
	}
	
	public void deleteactualDiganosis(@Observes @CDIEvent(DELETE_DIGANOSIS_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long specialistKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualDiganosis(specialistKey);
	}
	
	public void addProcedureEdited(@Observes @CDIEvent(EDIT_PROCEDURE_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		ProcedureCorrectionDTO procedureCorrectionDTO = (ProcedureCorrectionDTO) parameters.getPrimaryParameter();
		procedureCorrectionDTO.setHasChanges(true);
		view.addProcedureEdited(procedureCorrectionDTO);
	}
	
	public void deleteactualProcedure(@Observes @CDIEvent(DELETE_PROCEDURE_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long procedureKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualProcedure(procedureKey);
	}
	public void addTreatingEdited(@Observes @CDIEvent(EDIT_TREATING_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		TreatingCorrectionDTO treatingCorrectionDTO = (TreatingCorrectionDTO) parameters.getPrimaryParameter();
		treatingCorrectionDTO.setHasChanges(true);
		view.addTreatingEdited(treatingCorrectionDTO);
	}
	
	public void deleteactualTreating(@Observes @CDIEvent(DELETE_TREATING_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long treatingKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualTreating(treatingKey);
	}
	
	public void addImplantEdited(@Observes @CDIEvent(EDIT_IMPLANT_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		ImplantCorrectionDTO implantCorrectionDTO = (ImplantCorrectionDTO) parameters.getPrimaryParameter();
		implantCorrectionDTO.setHasChanges(true);
		view.addImplantEdited(implantCorrectionDTO);
	}
	
	public void deleteactualImplant(@Observes @CDIEvent(DELETE_IMPLANT_CORRECTION_VALUES_PRIORITY) final ParameterDTO parameters)
	{
		Long implantKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualImplant(implantKey);
	}
	
	public void generateFieldsBasedOnImplantApplicable(@Observes @CDIEvent(ACTUAL_IMPLANT_APPLICABLE_CHANGED_PRIORITY) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnImplantApplicable(isCked);
	}
	
	public void generatePPCoadingField(@Observes @CDIEvent(DC_PP_GENERATE_EVENT_PRIORITY) final ParameterDTO parameters)
	{
		Boolean ischecked = (Boolean) parameters.getPrimaryParameter();
		Long intimationKey = (Long) parameters.getSecondaryParameter(0, Long.class);
		String hospitalType = (String) parameters.getSecondaryParameter(1, String.class);
		List<PPCodingDTO> codingDTOs =null;
		Map<String,Boolean> selectedPPCoding = codingService.getPPCodingValues(intimationKey, hospitalType);
		if(!ischecked){
			codingDTOs = codingService.populatePPCoding(hospitalType);
		}
		view.generatePPCoadingField(ischecked,codingDTOs,selectedPPCoding);
	}
	public void generateFieldsBasedOnTOA(@Observes @CDIEvent(ACTUAL_TOA_CHANGED_PRIORITY) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnTOA(isCked);
	}
	
	public void resetScoringchanges(@Observes @CDIEvent(RESET_HOSPITAL_SCORING_PRIORITY) final ParameterDTO parameters) {
		view.resetScoringchanges();
	}
	
	public void setClearReferenceData(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR_PRIORITY) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
	}
	
	public void generateCovid19VariantFields(@Observes @CDIEvent(DATA_VALIDATION_PRIORITY_GENERATE_COVID19VARIANT_FIELDS) final ParameterDTO parameters)
	{
		Boolean covid19VariantEnable = (Boolean) parameters.getPrimaryParameter();
		view.generateCovid19VariantFields(covid19VariantEnable);
	}
	
}
