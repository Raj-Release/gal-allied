package com.shaic.claim.OMPprocessrejection.detailPage;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(OMPProcessRejectionView.class)
public class OMPProcessRejectionPresenter extends AbstractMVPPresenter<OMPProcessRejectionView> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String OMP_CONFIRM_BUTTON="Confirm OMP Rejection Button";
	public static final String OMP_WAIVE_BUTTON="Cancel OMP Rejection Button";
	public static final String OMP_SET_DATA="Bindind OMP data to field";
	public static final String OMP_SUBMIT_DATA="submit data in process OMP Rejection";
	
	@EJB
	private OMPClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hosptialService;
	
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_OMP_PROCESS_REJECTION_USER_RRC_REQUEST = "omp_process_rejection_user_rrc_request";
	
	public static final String PROCESS_OMP_REJECTION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "omp_process_rejection_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_OMP_REJECTION_SAVE_RRC_REQUEST_VALUES = "omp_process_rejection_save_rrc_request_values";

	public void generateFieldsBasedOnEnhancementChange(@Observes @CDIEvent(OMP_CONFIRM_BUTTON) final ParameterDTO parameters)
	{
		BeanItemContainer<SelectValue> rejectionCategory=masterService.getRejectionCategoryByValue();
		
		view.generateFieldBasedOnConfirmClick(rejectionCategory);
	}
	
	public void setReferenceData(@Observes @CDIEvent(OMPProcessRejectionPresenter.OMP_SET_DATA)final ParameterDTO parameters){
		
		SearchProcessRejectionTableDTO searchDTO=(SearchProcessRejectionTableDTO)parameters.getPrimaryParameter();

		view.setReferenceData(searchDTO);
	}
	
	public void generateFieldsBasedOnCancel(@Observes @CDIEvent(OMP_WAIVE_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnWaiveClick(isChecked);
	}
	
	public void saveData(@Observes @CDIEvent(OMPProcessRejectionPresenter.OMP_SUBMIT_DATA)final ParameterDTO parameters){

		ProcessRejectionDTO rejectionDto=(ProcessRejectionDTO)parameters.getPrimaryParameter();
		Boolean submitDescion=(Boolean)parameters.getSecondaryParameter(0, Boolean.class);
		String outCome=(String)parameters.getSecondaryParameter(1, String.class);
		Status status = null;
		
		if(submitDescion){
			status = masterService.getStatusByKey(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
		}
		else{
			status = masterService.getStatusByKey(ReferenceTable.PROCESS_REJECTED);
		}
		
		Boolean result=claimService.saveProcessRejection(rejectionDto,submitDescion,outCome,status);
		
	    view.savedResult();
	}

	private void setDiagnosis(PreauthDTO preauthDTO) {
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			List<DiagnosisDetailsTableDTO> diagnosisList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
			String diagnosis = "";
			if(!diagnosisList.isEmpty()){
			for(DiagnosisDetailsTableDTO diagnosisDto : diagnosisList){
				
				if(diagnosis.equals("")){
					diagnosis = diagnosisDto.getDiagnosisName().getValue();
				}
				else{
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? diagnosisDto.getDiagnosisName().getValue() : " / " ) ;
				}
			}
			}
			if(!diagnosis.equals("")){
				diagnosis = StringUtils.removeEnd(diagnosis, "/");
				preauthDTO.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
			if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
				for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
					if(!generatedList.contains(viewTmpClaim)) {
						generatedList.add(viewTmpClaim);
					}
				}
			}
			if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
				getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
			} else {
				return generatedList;
			}
			
			
		} catch(Exception e) {
			
		}
		return generatedList;
	}	

}
