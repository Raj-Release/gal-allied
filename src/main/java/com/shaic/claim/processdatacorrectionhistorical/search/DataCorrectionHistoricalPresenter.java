package com.shaic.claim.processdatacorrectionhistorical.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

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

@ViewInterface(DataCorrectionHistoricalView.class)
public class DataCorrectionHistoricalPresenter extends AbstractMVPPresenter<DataCorrectionHistoricalView>{

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
	private SearchProcessDataCorrectionHistoricalService correctionService;
	
	@EJB
	private PPCodingService codingService;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	public static final String SET_TABLE_DATA_HISTORICAL = "set_table_data_historical";
	
	public static final String SUBMIT_BUTTON_DATA_CODING_HISTORICAL = "submit_button_data_coding_historical";
	
	public static final String SUBMIT_HOSPITAL_SCORING_HISTORICAL= "SUBMIT_HOSPITAL_SCORING_HISTORICAL";
	
	public static final String GET_CORRECTION_PROCEDURE_VALUES_HIST = "get_correction_procedure_values_HIST";

	public static final String EDIT_CORRECTION_SPECIALITY_VALUES_HIST = "edit_correction_speciality_values_HIST";
	
	public static final String DELETE_CORRECTION_SPECIALITY_VALUES_HIST = "delete_correction_speciality_values_HIST";
	
	public static final String ADD_SPECIALITY_PROCEDURE_VALUES_HIST = "add_speciality_procedure_values_HIST";

	public static final String EDIT_DIGANOSIS_CORRECTION_VALUES_HIST = "edit_diganosis_correction_values_HIST";

	public static final String DELETE_DIGANOSIS_CORRECTION_VALUES_HIST = "delete_diganosis_correction_values_HIST";
	
	public static final String EDIT_PROCEDURE_CORRECTION_VALUES_HIST = "edit_procedure_correction_values_HIST";

	public static final String DELETE_PROCEDURE_CORRECTION_VALUES_HIST = "delete_procedure_correction_values_HIST";
	
	public static final String EDIT_TREATING_CORRECTION_VALUES_HIST = "edit_treating_correction_values_HIST";

	public static final String DELETE_TREATING_CORRECTION_VALUES_HIST = "delete_treating_correction_values_HIST";
	
	public static final String EDIT_IMPLANT_CORRECTION_VALUES_HIST = "edit_implant_correction_values_HIST";

	public static final String DELETE_IMPLANT_CORRECTION_VALUES_HIST = "delete_implant_correction_values";
	
	public static final String ACTUAL_IMPLANT_APPLICABLE_CHANGED_HIST = "actual_implant_applicable_changed";
	
	public static final String DC_PP_GENERATE_EVENT_HIST = "dc_pp_generate_event_hist";
	
	public static final String DATA_VALIDATION_HISTORICAL_GENERATE_COVID19VARIANT_FIELDS = "data_validation_historical_generate_covid19vaiantfields";
	
	public void setReferenceData(@Observes @CDIEvent(SET_TABLE_DATA_HISTORICAL) final ParameterDTO parameters) {
		
		referenceData.put("treatmentType", masterService.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("diagnosisName", masterService.getDiagnosisList());
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		BeanItemContainer<SelectValue> procedureCodeListNames = preauthService.getProcedureCodeList();
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureCodeListNames);
		referenceData.put("specialityType", preauthService.getAllSpecialityType());
		referenceData.put("covid19Variant", masterService.getSelectValueContainer(ReferenceTable.COVID19VARIANT));
		referenceData.put("cocktailDrugs", masterService.getSelectValueContainer(ReferenceTable.COCKTAILDRUG));
		
		view.setReferenceData(referenceData);
	}
	
	public void saveCorrectionDatas(@Observes @CDIEvent(SUBMIT_BUTTON_DATA_CODING_HISTORICAL) final ParameterDTO parameters) {
				
		ProcessDataCorrectionDTO dataCorrectionDTO = (ProcessDataCorrectionDTO) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		correctionService.saveDataCorrection(dataCorrectionDTO,userName);
		view.buildSuccessLayout();
		
	}
	
	public void setScoringchanges(@Observes @CDIEvent(SUBMIT_HOSPITAL_SCORING_HISTORICAL) final ParameterDTO parameters) {
		List<HospitalScoringDTO> scoringDTOs = (List<HospitalScoringDTO>) parameters.getPrimaryParameter();
		Boolean ppCodingSelected = (Boolean)parameters.getSecondaryParameter(0, Boolean.class);
		String intimationNumber = (String)parameters.getSecondaryParameter(2, String.class);
		Map<String, Boolean> selectedPPCoding = null;
		if(!ppCodingSelected){
			selectedPPCoding = (Map<String, Boolean>)parameters.getSecondaryParameter(1, Map.class);
		}		
		Boolean isPPChangesmade = correctionService.isPPChangesmade(ppCodingSelected,selectedPPCoding,intimationNumber);
		view.setScoringchanges(correctionService.iscScoringChangesmade(scoringDTOs),scoringDTOs,isPPChangesmade,ppCodingSelected,selectedPPCoding);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void getcorrectionProcedureValues(@Observes @CDIEvent(GET_CORRECTION_PROCEDURE_VALUES_HIST) final ParameterDTO parameters)
	{
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.getcorrectionProcedureValues(procedures);
	}
	
	public void addSpecialityEdited(@Observes @CDIEvent(EDIT_CORRECTION_SPECIALITY_VALUES_HIST) final ParameterDTO parameters)
	{
		SpecialityCorrectionDTO specialistdto = (SpecialityCorrectionDTO) parameters.getPrimaryParameter();
		specialistdto.setHasChanges(true);
		view.addSpecialityEdited(specialistdto);
	}
	
	public void deleteactualSpeciality(@Observes @CDIEvent(DELETE_CORRECTION_SPECIALITY_VALUES_HIST) final ParameterDTO parameters)
	{
		Long specialistKey = (Long) parameters.getPrimaryParameter();
		view.removeSpecialityEdited(specialistKey);
	}
	
	public void addSpecialityProcedure(@Observes @CDIEvent(ADD_SPECIALITY_PROCEDURE_VALUES_HIST) final ParameterDTO parameters)
	{
		Long specId = (Long) parameters.getPrimaryParameter();
		ComboBox prodcomboBox = (ComboBox)parameters.getSecondaryParameter(0, ComboBox.class);
		SelectValue procValue = (SelectValue)parameters.getSecondaryParameter(1, SelectValue.class);
		view.addSpecialityProcedure(specId,prodcomboBox,procValue);
	}
	
	public void addDiganosisEdited(@Observes @CDIEvent(EDIT_DIGANOSIS_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		DiganosisCorrectionDTO diganosisCorrectionDTO = (DiganosisCorrectionDTO) parameters.getPrimaryParameter();
		diganosisCorrectionDTO.setHasChanges(true);
		view.addDiganosisEdited(diganosisCorrectionDTO);
	}
	
	public void deleteactualDiganosis(@Observes @CDIEvent(DELETE_DIGANOSIS_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		Long specialistKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualDiganosis(specialistKey);
	}
	
	public void addProcedureEdited(@Observes @CDIEvent(EDIT_PROCEDURE_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		ProcedureCorrectionDTO procedureCorrectionDTO = (ProcedureCorrectionDTO) parameters.getPrimaryParameter();
		procedureCorrectionDTO.setHasChanges(true);
		view.addProcedureEdited(procedureCorrectionDTO);
	}
	
	public void deleteactualProcedure(@Observes @CDIEvent(DELETE_PROCEDURE_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		Long procedureKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualProcedure(procedureKey);
	}
	public void addTreatingEdited(@Observes @CDIEvent(EDIT_TREATING_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		TreatingCorrectionDTO treatingCorrectionDTO = (TreatingCorrectionDTO) parameters.getPrimaryParameter();
		treatingCorrectionDTO.setHasChanges(true);
		view.addTreatingEdited(treatingCorrectionDTO);
	}
	
	public void deleteactualTreating(@Observes @CDIEvent(DELETE_TREATING_CORRECTION_VALUES_HIST) final ParameterDTO parameters)
	{
		Long treatingKey = (Long) parameters.getPrimaryParameter();
		view.deleteactualTreating(treatingKey);
	}
	
	public void generatePPCoadingField(@Observes @CDIEvent(DC_PP_GENERATE_EVENT_HIST) final ParameterDTO parameters)
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
	
	public void generateCovid19VariantFields(@Observes @CDIEvent(DATA_VALIDATION_HISTORICAL_GENERATE_COVID19VARIANT_FIELDS) final ParameterDTO parameters)
	{
		Boolean covid19VariantEnable = (Boolean) parameters.getPrimaryParameter();
		view.generateCovid19VariantFields(covid19VariantEnable);
	}
	
}
