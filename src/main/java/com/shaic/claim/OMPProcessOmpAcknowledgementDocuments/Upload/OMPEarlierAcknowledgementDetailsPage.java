package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.OMPDocumentRelatedService.OMPAckDocService;
import com.shaic.claim.claimhistory.view.ompView.ViewOMPClaimHistoryService;
import com.shaic.claim.claimhistory.view.ompView.ViewOMPClaimHistoryTable;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class OMPEarlierAcknowledgementDetailsPage extends ViewComponent {

@Inject
private OMPEarlierAcknowledgementDetailsPageTable searchHRMPCompletedTable;

@Inject
private ViewOMPClaimHistoryTable viewOMPClaimHistoryTable;

@EJB
private ViewOMPClaimHistoryService viewOMPClaimHistoryService;

@EJB
private OMPIntimationService ompIntimationService;

@EJB
private OMPClaimService ompClaimService;

//@EJB
//private PreauthService preauthService;

@EJB
private MasterService masterService;

@EJB
private OMPAckDocService ompAckService;

/*@Inject
private SearchHRMPService searchHRMPService;*/

@SuppressWarnings("deprecation")
private VerticalLayout mainLayout;

@SuppressWarnings("deprecation")
public void loadData(OMPIntimation intimation) {		
	searchHRMPCompletedTable.init("", false, false);
	List<OMPEarlierAcknowledgementDetailsPageTableDTO> resultList = getList(intimation);
	searchHRMPCompletedTable.setTableList(resultList);
	
	mainLayout = new VerticalLayout(searchHRMPCompletedTable);
	mainLayout.setSizeFull();
	setCompositionRoot(mainLayout);
}

@SuppressWarnings("deprecation")
public VerticalLayout showViewTrailsPopup(OMPIntimation intimation) {		
	loadData(intimation);
	return mainLayout;
}

private List<OMPEarlierAcknowledgementDetailsPageTableDTO> getList(
		OMPIntimation intimation) {
	List<OMPEarlierAcknowledgementDetailsPageTableDTO> acknowledgementDetailsPageTableDTOList = new ArrayList<OMPEarlierAcknowledgementDetailsPageTableDTO>();
	
	List<OMPClaim> claimByIntimationList = ompClaimService.getClaimByIntimation(intimation.getKey());
	for(OMPClaim oMPClaim : claimByIntimationList){
		
		String claimId = oMPClaim.getClaimId();
		
		//acknowledgementDetailsPageTableDTO.setFinalApprovedAmount(oMPClaim.getClaimedAmount().toString());
		List<OMPReimbursement>  ompReimbursementList  = ompIntimationService.getRembursementDetailsByClaimKey(oMPClaim.getKey());
		OMPReimbursement  ompReimbursement  = null;
		if(ompReimbursementList != null && !ompReimbursementList.isEmpty()){
			ompReimbursement  = ompReimbursementList.get(0);
		}
		List<OMPDocAcknowledgement> docAcknowledgementList = ompAckService.findAcknowledgmentListByClaimKey(oMPClaim.getKey());

        for(OMPDocAcknowledgement ompDocAcknowledgement : docAcknowledgementList){
        	OMPEarlierAcknowledgementDetailsPageTableDTO acknowledgementDetailsPageTableDTO = new OMPEarlierAcknowledgementDetailsPageTableDTO();
        	//acknowledgementDetailsPageTableDTO.setAcknowledgementNumber(claimId);
        	acknowledgementDetailsPageTableDTO.setAcknowledgementNumber(ompDocAcknowledgement.getAcknowledgeNumber());
        	acknowledgementDetailsPageTableDTO.setIntimationNumber(ompDocAcknowledgement.getClaim().getIntimation().getIntimationId());
        	if(ompReimbursement != null)
        	acknowledgementDetailsPageTableDTO.setrODNumber(ompReimbursement.getRodNumber());
        	if(ompDocAcknowledgement.getDocumentReceivedFromId() != null)
        	acknowledgementDetailsPageTableDTO.setDocumentReceivedFrom(ompDocAcknowledgement.getDocumentReceivedFromId().getValue());
        	if(ompDocAcknowledgement.getDocumentReceivedDate() != null)
        	acknowledgementDetailsPageTableDTO.setDocumentReceivedDate(SHAUtils.parseDate(ompDocAcknowledgement.getDocumentReceivedDate()));
        	if(ompDocAcknowledgement.getModeOfReceiptId() != null)
        	acknowledgementDetailsPageTableDTO.setModeOfReceipt(ompDocAcknowledgement.getModeOfReceiptId().getValue());
        	if(ompDocAcknowledgement.getClassificationId() != null)
        	acknowledgementDetailsPageTableDTO.setClassification(ompDocAcknowledgement.getClassificationId().getValue());
        	if(ompDocAcknowledgement.getSubClassificationId() != null)
        	acknowledgementDetailsPageTableDTO.setSubClassification(ompDocAcknowledgement.getSubClassificationId().getValue());
        	if(ompDocAcknowledgement.getCategoryId() != null)
        	acknowledgementDetailsPageTableDTO.setCategory(ompDocAcknowledgement.getCategoryId().getValue());
        	if(ompReimbursement != null && ompReimbursement.getFinalApprovedAmtUsd() != null)
        	acknowledgementDetailsPageTableDTO.setFinalApprovedAmount(ompReimbursement.getFinalApprovedAmtUsd().toString());
        	if(ompReimbursement != null && ompReimbursement.getFinalApprovedAmtInr() != null )
        	acknowledgementDetailsPageTableDTO.setFinalApprovedAmountINR(ompReimbursement.getFinalApprovedAmtInr().toString());
        	if(ompDocAcknowledgement.getStatus() != null)
        	acknowledgementDetailsPageTableDTO.setStatus(ompDocAcknowledgement.getStatus().getUserStatus());
        	    	
        	acknowledgementDetailsPageTableDTOList.add(acknowledgementDetailsPageTableDTO);
        }
		
	}
	
	return acknowledgementDetailsPageTableDTOList;
}
}

