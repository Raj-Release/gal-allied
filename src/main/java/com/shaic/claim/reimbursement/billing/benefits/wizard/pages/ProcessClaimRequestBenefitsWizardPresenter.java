/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

/**
 * @author ntv.vijayar
 *
 */

@ViewInterface(ProcessClaimRequestBenefitsWizard.class)
public class ProcessClaimRequestBenefitsWizardPresenter extends
AbstractMVPPresenter<ProcessClaimRequestBenefitsWizard>
{
	public static final String SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS = "submit_process_claim_request_benefits";
	
	@EJB
	private ProcessClaimRequestBenefitsService claimRequest;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS) final ParameterDTO parameters) throws Exception {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		if(null == rodDTO.getDocumentDetails().getStatusId() && ! rodDTO.getIsMedicalScreen())
		{
			view.buildSuccessLayout("Please select any one action before submit",true);
		}
		else
		{
			Boolean isAlreadyPayment = false;
			Reimbursement exisitingRODNumber = claimRequest.getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
			
			if(exisitingRODNumber.getReconsiderationRequest() != null && !exisitingRODNumber.getReconsiderationRequest().equals("Y")) {
				isAlreadyPayment = reimbursementService.isPaymentAvailable(exisitingRODNumber.getRodNumber());
				DocAcknowledgement acknowledgementByKey = reimbursementService.getAcknowledgementByKey(exisitingRODNumber.getDocAcknowLedgement().getKey());
				if(acknowledgementByKey != null && acknowledgementByKey.getPaymentCancellationFlag() != null && acknowledgementByKey.getPaymentCancellationFlag().equalsIgnoreCase("Y")) {
					isAlreadyPayment = false;
				}
			}
			
			if(! isAlreadyPayment){
			
				Reimbursement reimbursement = claimRequest.submitProcessClaimRequestBenefits(rodDTO);
				if(reimbursement != null){
					dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				}
				view.buildSuccessLayout("Claim record saved successfully !!!",false);
			}else{
				view.buildSuccessLayout("Payment initiated/made for this ROD Already. Please contact IMS Support.",true);
			}
		}
	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}

