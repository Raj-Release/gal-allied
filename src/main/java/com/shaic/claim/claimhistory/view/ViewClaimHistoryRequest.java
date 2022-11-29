package com.shaic.claim.claimhistory.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.bouncycastle.jcajce.provider.symmetric.Shacal2;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewClaimHistoryRequest extends ViewComponent{
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewClaimHistoryTable viewClaimHistoryTable;
	
	@Inject
	private ViewOPClaimHistoryTable viewOPClaimHistoryTable;
	
	@EJB
	private ViewClaimHistoryService viewClaimHistoryService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	private VerticalLayout mainLayout;
	
	@Inject
	private ViewClaimAuditTable viewClaimAuditTable;
	
	public Boolean init(Intimation intimation) {		
//		Label labelHeader = new Label("History of Intimation No: " + intimation.getIntimationId());
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		
		Claim claim = claimByIntimation.get(0);
		
//		List<Preauth> preauthByClaimKey = preauthService
//				.getPreauthListByDescendingOrder(claim.getKey());
		
		viewClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForCashless(claim.getKey());
		//viewClaimHistoryTable.setTableList(claimHistoryForCashless);
		tableList.addAll(claimHistoryForCashless);
		List<ViewClaimHistoryDTO> hrmclaimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForHRMCashless(claim.getKey());
		if(hrmclaimHistoryForCashless != null && !hrmclaimHistoryForCashless.isEmpty()){
			tableList.addAll(hrmclaimHistoryForCashless);
		}
		viewClaimHistoryTable.setTableList(tableList);
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewClaimHistoryTable);
//		mainLayout.setComponentAlignment(labelHeader, Alignment.MIDDLE_CENTER);
		setCompositionRoot(mainLayout);
		
			return true;
		}else{
			return false;
		}
	}
	
	public Boolean showReimbursementClaimHistory(Long intimationKey,Long rodKey) {		
//		Label labelHeader = new Label("History of Intimation No: " + intimation.getIntimationId());
		
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimationKey);
		
		if(claimByIntimation != null  && ! claimByIntimation.isEmpty()){
		
			Claim claim = claimByIntimation.get(0);
			
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), rodKey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			
	//		Reimbursement latestReimbursementDetails = reimbursementService.getLatestReimbursementDetails(claim.getKey());
			
			viewClaimHistoryTable.init("", false, false);
			viewClaimHistoryTable.setTableList(claimHistoryForReimbursement);
	//		viewClaimHistoryTable.setTableList(viewClaimHistoryService.search(intimation));		
			mainLayout = new VerticalLayout(/*labelHeader,*/ viewClaimHistoryTable);
	//		mainLayout.setComponentAlignment(labelHeader, Alignment.MIDDLE_CENTER);
			setCompositionRoot(mainLayout);
			return true;
		}else{
			return false;
		}
		
	}
	
	public Boolean showCashlessAndReimbursementHistory(Intimation intimation){
		
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		Claim claim = claimByIntimation.get(0);
		
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		
		viewClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForCashless(claim.getKey());
		tableList.addAll(claimHistoryForCashless);
		List<ViewClaimHistoryDTO> claimHistoryForHRMCashless = viewClaimHistoryService.getClaimHistoryForHRMCashless(claim.getKey());
		if(claimHistoryForHRMCashless != null && !claimHistoryForHRMCashless.isEmpty()){
			tableList.addAll(claimHistoryForHRMCashless);
		}
		
		List<Reimbursement> reimbursementDetails = reimbursementService.getReimbursementDetails(claim.getKey());

		for (Reimbursement reimbursement : reimbursementDetails) {
			
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), reimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

			tableList.addAll(claimHistoryForReimbursement);
			
		}
		try{
			if(reimbursementDetails == null || reimbursementDetails.isEmpty()){
				List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), 0l,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
				tableList.addAll(claimHistoryForReimbursement);
			}
		}catch(Exception e){
			
		}
		List<ViewClaimHistoryDTO> claimHistoryForClaimAlter = viewClaimHistoryService.getClaimHistoryForClaimsAlert(intimation);
		tableList.addAll(claimHistoryForClaimAlter);
		
		List<ViewClaimHistoryDTO> filterDTO = new ArrayList<ViewClaimHistoryDTO>();
		List<Long> duplicateKeys = new ArrayList<Long>();
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : tableList) {
			
			if(! duplicateKeys.contains(viewClaimHistoryDTO.getHistoryKey())){
				filterDTO.add(viewClaimHistoryDTO);
			}
			
			duplicateKeys.add(viewClaimHistoryDTO.getHistoryKey());
		}
		

		viewClaimHistoryTable.setTableList(filterDTO);
		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewClaimHistoryTable);

		setCompositionRoot(mainLayout);
		
		if(tableList != null && ! tableList.isEmpty()){
			return true;
		}

	 }
		return false;
		
	}
	
	public Boolean showOPCashlessAndReimbursementHistory(OPIntimation intimation){
		
		List<OPClaim> claimByIntimation = claimService.getOPClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		OPClaim claim = claimByIntimation.get(0);
		
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		
		viewOPClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getOPClaimHistoryForCashless(intimation.getKey());
		tableList.addAll(claimHistoryForCashless);
		
		List<Reimbursement> reimbursementDetails = reimbursementService.getReimbursementDetails(claim.getKey());

		for (Reimbursement reimbursement : reimbursementDetails) {
			
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), reimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

			tableList.addAll(claimHistoryForReimbursement);
			
		}
		try{
			if(reimbursementDetails == null || reimbursementDetails.isEmpty()){
				List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), 0l,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
				tableList.addAll(claimHistoryForReimbursement);
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
		//For OP Allow Intimation
		
		/*String policyNumber = claim.getIntimation().getPolicy().getPolicyNumber();
		if(policyNumber != null && !policyNumber.isEmpty()){
			Policy policy = policyService.getByPolicyNumber(policyNumber);
			System.out.println(String.format("policyNumber : [%s]", policyNumber));

			if(policy != null && policy.getOpAllowIntimation()!=null && policy.getOpAllowIntimation().equalsIgnoreCase(SHAConstants.YES_FLAG)){

				System.out.println(String.format("OP ALLOW Intimation : [%s]", policy.getOpAllowIntimation()));
				for (ViewClaimHistoryDTO viewClaimHistoryDTOList : filterDTO) {

					viewClaimHistoryDTOList.setClaimStage(SHAConstants.OP_CLAIM_HISTORY_STAGE);
					viewClaimHistoryDTOList.setStatus(SHAConstants.OP_CLAIM_HISTORY_STATUS);

				}

			}
		}*/

		viewOPClaimHistoryTable.setTableList(filterDTO);
		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewOPClaimHistoryTable);

		setCompositionRoot(mainLayout);
		
		if(tableList != null && ! tableList.isEmpty()){
			return true;
		}

	 }
		return false;
		
	}
	
	
	public void showOPHealthCheckUpClaimHistory(Intimation intimation){
		
      List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		
		Claim claim = claimByIntimation.get(0);
		
//		List<Preauth> preauthByClaimKey = preauthService
//				.getPreauthListByDescendingOrder(claim.getKey());
		
		viewOPClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForOPHealthCheckUp(claim.getKey());

		viewOPClaimHistoryTable.setTableList(claimHistoryForCashless);
//		viewClaimHistoryTable.setTableList(viewClaimHistoryService.search(intimation));		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewOPClaimHistoryTable);
//		mainLayout.setComponentAlignment(labelHeader, Alignment.MIDDLE_CENTER);
		setCompositionRoot(mainLayout);
		
	}
	
	public void showOPHealthCheckUpOPClaimHistory(OPIntimation intimation){
		
	      List<OPClaim> claimByIntimation = claimService.getOPClaimByIntimation(intimation.getKey());
			
	      OPClaim claim = claimByIntimation.get(0);
			
//			List<Preauth> preauthByClaimKey = preauthService
//					.getPreauthListByDescendingOrder(claim.getKey());
			
	      viewOPClaimHistoryTable.init("", false, false);	
			List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForOPHealthCheckUp(claim.getKey());
			
			/*//For Allow OP Intimation
			String policyNumber = claim.getIntimation().getPolicy().getPolicyNumber();
			if(policyNumber != null && !policyNumber.isEmpty()){
				Policy policy = policyService.getByPolicyNumber(policyNumber);
				System.out.println(String.format("policyNumber : [%s]", policyNumber));

				if(policy != null && policy.getOpAllowIntimation()!=null && policy.getOpAllowIntimation().equalsIgnoreCase(SHAConstants.YES_FLAG)){

					System.out.println(String.format("OP ALLOW Intimation : [%s]", policy.getOpAllowIntimation()));
					for (ViewClaimHistoryDTO viewClaimHistoryDTOList : claimHistoryForCashless) {

						System.out.println(String.format("TYpe of Claim : [%s]", viewClaimHistoryDTOList.getTypeofClaim()));
						viewClaimHistoryDTOList.setClaimStage(SHAConstants.OP_CLAIM_HISTORY_STAGE);
						viewClaimHistoryDTOList.setStatus(SHAConstants.OP_CLAIM_HISTORY_STATUS);

					}

				}
			}*/
			viewOPClaimHistoryTable.setTableList(claimHistoryForCashless);
//			viewClaimHistoryTable.setTableList(viewClaimHistoryService.search(intimation));		
			mainLayout = new VerticalLayout(/*labelHeader,*/ viewOPClaimHistoryTable);
//			mainLayout.setComponentAlignment(labelHeader, Alignment.MIDDLE_CENTER);
			setCompositionRoot(mainLayout);
			
		}
	
	public void getErrorMessage(String eMsg){
		
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	
	public Boolean showCashlessAndReimbursementHistory(OMPIntimation intimation){
		
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		Claim claim = claimByIntimation.get(0);
		
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		
		viewClaimHistoryTable.init("", false, false);	
		List<ViewClaimHistoryDTO> claimHistoryForCashless = viewClaimHistoryService.getClaimHistoryForCashless(claim.getKey());
		tableList.addAll(claimHistoryForCashless);
		
		List<Reimbursement> reimbursementDetails = reimbursementService.getReimbursementDetails(claim.getKey());

		for (Reimbursement reimbursement : reimbursementDetails) {
			
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), reimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

			tableList.addAll(claimHistoryForReimbursement);
			
		}
		try{
			if(reimbursementDetails == null || reimbursementDetails.isEmpty()){
				List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claim.getKey(), 0l,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
				tableList.addAll(claimHistoryForReimbursement);
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
		

		viewClaimHistoryTable.setTableList(filterDTO);
		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewClaimHistoryTable);

		setCompositionRoot(mainLayout);
		
		if(tableList != null && ! tableList.isEmpty()){
			return true;
		}

	 }
		return false;
		
	}
	
	public Boolean showClaimAuditHistory(Intimation intimation, boolean clmAuditUser){
		
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
		List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
		
		viewClaimAuditTable.init("", false, false);
		
		if(clmAuditUser) {
			viewClaimAuditTable.setAuditUserVisibleColumns();
		}
		else {
			viewClaimAuditTable.setClaimUserVisibleColumns();
		}
		
		List<ViewClaimHistoryDTO> claimCVCAuditTrailsList = viewClaimHistoryService.getClaimHistoryForClaimAudit(intimation.getKey());
		if(claimCVCAuditTrailsList != null && !claimCVCAuditTrailsList.isEmpty()){
			for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimCVCAuditTrailsList) {
				viewClaimHistoryDTO.setSerialNumber(claimCVCAuditTrailsList.indexOf(viewClaimHistoryDTO)+1);
			}
			tableList.addAll(claimCVCAuditTrailsList);
		}
		viewClaimAuditTable.setTableList(tableList);
		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewClaimAuditTable);

		setCompositionRoot(mainLayout);
		
		if(tableList != null && ! tableList.isEmpty()){
			return true;
		}

	 }
		return false;
		
	}
	
	public void clearClaimAuditHistoryPopup(){
    	if(mainLayout != null){
    		mainLayout.removeAllComponents();
    	}
    	if(this.viewClaimAuditTable != null){
    		this.viewClaimAuditTable.getTable().clear();
    	}
    }

	public void setClearReferenceData(){
//		SHAUtils.setClearReferenceData(referenceData);
		if(mainLayout != null){
			mainLayout.removeAllComponents();
		}
//		this.diagnosisDetailsTableObj.clearObject();
//		this.balanceSumInsuredTable = null;
		if(viewClaimHistoryTable!=null){
			this.viewClaimHistoryTable.removeRow();
		}
		if(this.viewOPClaimHistoryTable!=null){
			this.viewOPClaimHistoryTable.removeRow();
		}
	}

}
