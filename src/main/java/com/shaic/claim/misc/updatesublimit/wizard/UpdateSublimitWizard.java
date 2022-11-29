package com.shaic.claim.misc.updatesublimit.wizard;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.claim.misc.updatesublimit.SearchUpdateSublimitTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface UpdateSublimitWizard extends GMVPView{

	public void init(PreauthDTO searchDTO);
	
	public void setupReferences(Map<String, Object> referenceData);
	
	public void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
	
	public void editSublimitValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
	
	public void buildSuccessLayout();
}
