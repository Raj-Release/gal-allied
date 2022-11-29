package com.shaic.claim.claimhistory.view.ompView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.OMPDocumentRelatedService.OMPAckDocService;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewOMPClaimHistoryRequest extends ViewComponent{
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewOMPClaimHistoryTable viewOMPClaimHistoryTable;
	
	@EJB
	private ViewOMPClaimHistoryService viewOMPClaimHistoryService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
//	@EJB
//	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPAckDocService ompAckService;
	
	private VerticalLayout mainLayout;
	
	public Boolean init(OMPIntimation ompIntimation) {		
//		Label labelHeader = new Label("History of Intimation No: " + intimation.getIntimationId());
		
		List<OMPClaim> claimByIntimation = ompClaimService.getClaimByIntimation(ompIntimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		
			OMPClaim claim = claimByIntimation.get(0);
		
		viewOMPClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistory = viewOMPClaimHistoryService.getClaimHistory(claim.getKey(),null,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		viewOMPClaimHistoryTable.setTableList(claimHistory);
		mainLayout = new VerticalLayout(viewOMPClaimHistoryTable);
		setCompositionRoot(mainLayout);
		
			return true;
		}else{
			return false;
		}
	}
	
	public VerticalLayout showOMPReimbursementClaimHistory(Long intimationKey) {		
		
		List<OMPClaim> claimByIntimation = ompClaimService.getClaimByIntimation(intimationKey);
		
		if(claimByIntimation != null  && ! claimByIntimation.isEmpty()){
		
			OMPClaim claim = claimByIntimation.get(0);
			List<ViewClaimHistoryDTO> claimHistory = new ArrayList<ViewClaimHistoryDTO>(); 
			if(claim != null){
				List<OMPReimbursement> ompReimbList = ompAckService.getReimbursementByClaimKey(claim.getKey());
				
				if(ompReimbList != null && !ompReimbList.isEmpty()){
					for (OMPReimbursement ompReimbursement : ompReimbList) {
						
						List<ViewClaimHistoryDTO> 	claimHistory1 = viewOMPClaimHistoryService.getClaimHistory(claim.getIntimation().getKey(), ompReimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
						claimHistory.addAll(claimHistory1);
						break;
					}
				}else{
					List<ViewClaimHistoryDTO> 	claimHistory1 = viewOMPClaimHistoryService.getClaimHistory(claim.getIntimation().getKey(), null,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
					claimHistory.addAll(claimHistory1);
				}
			}
			//List<ViewClaimHistoryDTO> filterDTO = new ArrayList<ViewClaimHistoryDTO>();
			//Set<String> duplicateKeys = new HashSet<String>();
			
			/*for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistory) {
//				if(viewClaimHistoryDTO.getReferenceNo()!= null){
					if(! duplicateKeys.contains(viewClaimHistoryDTO.getReferenceNo())){
						filterDTO.add(viewClaimHistoryDTO);
//					}
				}
				
				duplicateKeys.add(viewClaimHistoryDTO.getReferenceNo());
			}*/
			viewOMPClaimHistoryTable.init("", false, false);
			viewOMPClaimHistoryTable.setTableList(claimHistory);
			mainLayout = new VerticalLayout(viewOMPClaimHistoryTable);
			
			return mainLayout;
		}
		
		return null;
		
	}
	
	public Boolean showCashlessAndReimbursementHistory(OMPIntimation intimation){
		
		List<OMPClaim> claimByIntimation = ompClaimService.getClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		OMPClaim claim = claimByIntimation.get(0);
		
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		
		viewOMPClaimHistoryTable.init("", false, false);	
		
		List<OMPReimbursement> reimbursementDetails = ompAckService.getReimbursementDetails(claim.getKey());

		try{
			for (OMPReimbursement reimbursement : reimbursementDetails) {
				
				List<ViewClaimHistoryDTO> claimHistory1 = viewOMPClaimHistoryService.getClaimHistory(claim.getKey(), reimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

				tableList.addAll(claimHistory1);
				
			}
		}catch(Exception e){
			
		}
		
		List<ViewClaimHistoryDTO> filterDTO = new ArrayList<ViewClaimHistoryDTO>();
		List<Long> duplicateKeys = new ArrayList<Long>();
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : tableList) {
			
			if(! duplicateKeys.contains(viewClaimHistoryDTO.getHistoryKey())){
				filterDTO.add(viewClaimHistoryDTO);
			}
			
			duplicateKeys.add(viewClaimHistoryDTO.getHistoryKey());
		}
		
		viewOMPClaimHistoryTable.setTableList(filterDTO);
		
		mainLayout = new VerticalLayout(viewOMPClaimHistoryTable);

		setCompositionRoot(mainLayout);
		
		if(tableList != null && ! tableList.isEmpty()){
			return true;
		}

	 }
		return false;
		
	}
	
//	public void showOPHealthCheckUpClaimHistory(OMPIntimation intimation){
//		
//      List<OMPClaim> claimByIntimation = ompClaimService.getClaimByIntimation(intimation.getKey());
//		
//		OMPClaim claim = claimByIntimation.get(0);
//		
//		viewOMPClaimHistoryTable.init("", false, false);	
//		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewOMPClaimHistoryService.getClaimHistoryForOPHealthCheckUp(claim.getKey());
//
//		viewOMPClaimHistoryTable.setTableList(claimHistoryForCashless);
//		mainLayout = new VerticalLayout(viewOMPClaimHistoryTable);
//		setCompositionRoot(mainLayout);
//		
//	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
}