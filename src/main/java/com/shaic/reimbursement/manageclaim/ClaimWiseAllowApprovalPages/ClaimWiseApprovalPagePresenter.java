package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;

@ViewInterface(ClaimWiseAllowApprovalView.class)
public class ClaimWiseApprovalPagePresenter extends AbstractMVPPresenter<ClaimWiseAllowApprovalView> {
	
public static final String SET_TABLE_DATA = "set preauth and rod data";
	
	public static final String SUBMIT_DATA = "submit data";
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocService;
	
	@EJB
	private IntimationService intimationService;
	
	protected void submitData(
			@Observes @CDIEvent(SUBMIT_DATA) final ParameterDTO parameters){
		ClaimWiseApprovalDto approvalDto = (ClaimWiseApprovalDto) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		intimationService.updateCancelledPolicyApproval(approvalDto,userName);
		view.buildSuccessLayout();
	}
	
	protected void setTableValues(
			@Observes @CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters){
		SearchClaimWiseAllowApprovalDto searchDto = (SearchClaimWiseAllowApprovalDto) parameters.getPrimaryParameter();
		List<Preauth> cashlessList = preauthService.getPreauthByClaimKey(searchDto.getClaimDto().getKey());
		List<Reimbursement> reimbursementList = reimbursementService.getRembursementDetails(searchDto.getClaimDto().getKey());
		List<ClaimWiseApprovalDto> listValues = new ArrayList<ClaimWiseApprovalDto>();
		
		for (Preauth preauth : cashlessList) {
			ClaimWiseApprovalDto preauthDto = new ClaimWiseApprovalDto();
			preauthDto.setPreauthOrRodNo(preauth.getPreauthId());
			preauthDto.setAmountClaimed(Double.valueOf(preauth.getClaimedAmt() != null ? preauth.getClaimedAmt() : 0d));
			preauthDto.setStatus(preauth.getStatus().getProcessValue());
			listValues.add(preauthDto);
		}
		
		for (Reimbursement reimbursement : reimbursementList) {
			ClaimWiseApprovalDto reimbursementDto = new ClaimWiseApprovalDto();
			reimbursementDto.setPreauthOrRodNo(reimbursement.getRodNumber());
			reimbursementDto.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
			if(reimbursement.getDocAcknowLedgement() !=null){
				String billClassificationValue = getBillClassificationValue(reimbursement.getDocAcknowLedgement());
				reimbursementDto.setBillClassification(billClassificationValue);
			}
			List<RODBillDetails> billEntryDetails = ackDocService.getBillEntryDetails(reimbursement.getKey());
			if(billEntryDetails != null){
				Double billAmount = 0d;
				for (RODBillDetails rodBillDetails : billEntryDetails) {
					billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
				}
				
				if(reimbursement.getProcessClaimType() !=null && reimbursement.getProcessClaimType().equals("P")){
					reimbursementDto.setAmountClaimed(Double.valueOf(reimbursement.getBenApprovedAmt() != null ? reimbursement.getBenApprovedAmt() : 0d ));
				} else {
					reimbursementDto.setAmountClaimed(billAmount);
				}
				
			}
			reimbursementDto.setStatus(reimbursement.getStatus().getProcessValue());
			listValues.add(reimbursementDto);
		}
		
		view.setTableList(listValues);
	}
	
	private String getBillClassificationValue(DocAcknowledgement docAck) {
		StringBuilder strBuilder = new StringBuilder();
		// StringBuilder amtBuilder = new StringBuilder();
		// Double total = 0d;
		try {
			if (("Y").equals(docAck.getHospitalisationFlag())) {
				strBuilder.append("Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPreHospitalisationFlag())) {
				strBuilder.append("Pre-Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPostHospitalisationFlag())) {
				strBuilder.append("Post-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
				strBuilder.append("Partial-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getLumpsumAmountFlag())) {
				strBuilder.append("Lumpsum Amount");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getHospitalCashFlag())) {
				strBuilder.append("Add on Benefits (Hospital cash)");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getPatientCareFlag())) {
				strBuilder.append("Add on Benefits (Patient Care)");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
				strBuilder.append("Hospitalization Repeat");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getCompassionateTravel())) {
				strBuilder.append("Compassionate Travel");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
				strBuilder.append("Repatriation Of Mortal Remains");
				strBuilder.append(",");
			}
			
			if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
					(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
							|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
				if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					strBuilder.append("Valuable Service Provider (Hospital)");
					strBuilder.append(",");
				}
			}
			else{ 
				if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					strBuilder.append("Preferred Network Hospital");
					strBuilder.append(",");
				}
			}
			
			if (("Y").equals(docAck.getSharedAccomodation())) {
				strBuilder.append("Shared Accomodation");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
				strBuilder.append("Emergency Medical Evacuation");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getStarWomenCare())) {
				strBuilder.append("Star Mother Cover");
				strBuilder.append(",");
			}
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}
	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
