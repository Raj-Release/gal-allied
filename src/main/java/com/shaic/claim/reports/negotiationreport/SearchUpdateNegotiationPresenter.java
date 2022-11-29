package com.shaic.claim.reports.negotiationreport;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import weblogic.descriptor.LateDescriptorUpdateListener;

import com.shaic.arch.table.Page;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Preauth;

@ViewInterface(SearchUpdateNegotiationView.class)
public class SearchUpdateNegotiationPresenter extends AbstractMVPPresenter<SearchUpdateNegotiationView>{
	
	public static final String SEARCH_BUTTON = "searchnegotiationdetails";
	public static final String UPDATE_NEGOTIAION = "updatenegotiaion";
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackService;
	
	public void searchNegotiaion(
			@Observes @CDIEvent(SEARCH_BUTTON) final ParameterDTO parameters) {
		ViewNegotiationDetailsDTO negDetails = (ViewNegotiationDetailsDTO) parameters.getPrimaryParameter();
		Boolean isClmProcessed = false;
		Intimation intimationDtls = createRodService.getIntimationByNo(negDetails.getIntimationNo());
		negDetails.setIntimationNo(intimationDtls.getIntimationId());
		Claim claim = preauthService.getClaimsByIntimationNumber(intimationDtls.getIntimationId());
		List<NegotiationAmountDetails> negDtls = preauthService.getNegotiationDetails(intimationDtls.getKey());
		List<ViewNegotiationDetailsDTO> listDtls = new ArrayList<ViewNegotiationDetailsDTO>();
		Reimbursement reimb = createRodService.getPreviousRODDetails(claim.getKey());
		List<Preauth> preauthDtls = preauthService.getPreauthListByClaimKeyInDesc(claim.getKey());
		Preauth latestPreauth = null;
		if(preauthDtls != null && !preauthDtls.isEmpty()){
			latestPreauth = preauthDtls.get(0);
		}
		if(reimb != null || (preauthDtls !=null && !preauthDtls.isEmpty())){
			isClmProcessed = true;
		}
		/*if(negDtls == null || negDtls.isEmpty()) {
			ViewNegotiationDetailsDTO neg = new ViewNegotiationDetailsDTO();
			Claim claimDtls = preauthService.getClaimsByIntimationNumber(negDetails.getIntimationNo());
			TmpCPUCode cpuCode = claimService.getTmpCPUCode(intimationDtls.getCpuCode().getKey());
			neg.setIntimationNo(intimationDtls.getIntimationId());
			neg.setCpucodeName(cpuCode.getCpuCode() +"-"+ cpuCode.getDescription());
			listDtls.add(neg);
		}
		Page<ViewNegotiationDetailsDTO> page = new Page<ViewNegotiationDetailsDTO>();
		page.setPageItems(listDtls);
		view.list(page);*/
		if(negDtls == null || negDtls.isEmpty()) {
			
			negDetails.setIntimationKey(intimationDtls.getKey());
			negDetails.setCpuKey(intimationDtls.getCpuCode().getKey());
			
			if(reimb != null) {
				List<RODBillDetails> billEntryDetails = ackService.getBillEntryDetails(reimb.getKey());
				Double billAmount = 0d;
				if(billEntryDetails != null){
					for (RODBillDetails rodBillDetails : billEntryDetails) {
						billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
					}
				}
				negDetails.setClaimApprovedAmt(billAmount);
				negDetails.setLatestTransKey(reimb.getKey());
				if(reimb.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
					negDetails.setTransactionFlag("R");
				} else {
					negDetails.setTransactionFlag("C");
				}
			} else {
				negDetails.setClaimApprovedAmt(latestPreauth != null ? Double.valueOf(latestPreauth.getClaimedAmt().toString()): null);
				negDetails.setLatestTransKey(latestPreauth != null ? latestPreauth.getKey() : null);
				negDetails.setTransactionFlag("C");
			}
			view.setNegotiationDetails(negDetails,isClmProcessed,false);
		} else {
			if(latestPreauth != null && !(latestPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)
					|| latestPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)
					|| latestPreauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)
					|| latestPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)
					|| latestPreauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS))){
				negDetails.setClaimApprovedAmt(latestPreauth != null ? Double.valueOf(latestPreauth.getClaimedAmt().toString()): null);
				negDetails.setLatestTransKey(latestPreauth != null ? latestPreauth.getKey() : null);
				negDetails.setTransactionFlag("C");
				negDetails.setIntimationKey(intimationDtls.getKey());
				negDetails.setCpuKey(intimationDtls.getCpuCode().getKey());
				view.setNegotiationDetails(negDetails,isClmProcessed,false);
			} else {
				view.setNegotiationDetails(negDetails,isClmProcessed,true);
			}
		}
	}
	
	public void updateNegotiaion(
			@Observes @CDIEvent(UPDATE_NEGOTIAION) final ParameterDTO parameters) {
		ViewNegotiationDetailsDTO negUpdateDtls= (ViewNegotiationDetailsDTO) parameters.getPrimaryParameter();
		String userName = (String) parameters.getSecondaryParameter(0, String.class);
		if(negUpdateDtls != null){
			preauthService.saveStandaloneNegotionAmt(negUpdateDtls,userName);
//			preauthService.updateNegotiationDetailsinStageInformation(negUpdateDtls,userName);
		}
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
