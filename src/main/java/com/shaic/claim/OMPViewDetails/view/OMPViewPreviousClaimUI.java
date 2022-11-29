package com.shaic.claim.OMPViewDetails.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails.ClaimAndDocumentDetailsPageUI;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.InsuredService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPViewPreviousClaimUI extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OptionGroup viewType;
	
	private VerticalLayout mainLayout;
	
	private final String POLICY_WISE="Policy Wise";
	
	private final String INSURED_WISE="Insured Wise";

	private ComboBox cbox;
	
	private String intimationNo;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private OMPClaimService claimService;
	
	@Inject
	private OMPViewPreviousClaimDetailTable viewOmpPreviousClaimDataTable;
	
	@EJB
	private InsuredService insuredService;
	
	
	public void showValuesForOP(Long policyKey,String intimationNo){
		
		
		mainLayout = getPrivousClaimsLayoutForOP(policyKey);
		this.intimationNo = intimationNo;
		
		this.setContent(mainLayout);
		
		setCompositionRoot(mainLayout);
		
//		getClaimByInsuredWiseForOP(intimationNo);	
		getClaimByPolicyWiseForOP(policyKey, intimationNo);
		
		
	}
	
	private void setContent(VerticalLayout mainLayout2) {
	// TODO Auto-generated method stub
	
}
	public VerticalLayout getPrivousClaimsLayoutForOP(Long policyKey) {
		HorizontalLayout optionLayout = buildViewTypeForOp(policyKey);
		
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		
		cbox =new ComboBox();
		cbox.addItem("All");
		cbox.addItem("2012");
		cbox.addItem("2013");
		cbox.addItem("2014");
		cbox.addItem("2015");
		cbox.setCaption("Policy Year");
		
		mainLayout.addComponent(optionLayout);
		mainLayout.addComponent(cbox);
		
		return mainLayout;
		
	}
	
	public HorizontalLayout buildViewTypeForOp(Long policyKey) {
		
		final Long policyId = policyKey;
       
		
		Policy policy = policyService.getPolicyByKey(policyKey);
		
		OMPIntimation intimation=intimationService.getIntimationByNo(intimationNo);
		
		
		final OptionGroup viewType = getOptionGroup(policy,intimation);
		
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						
						viewType.setValue(POLICY_WISE);
						
					} else if (viewType.getValue().equals(INSURED_WISE)) {
						
						viewType.setValue(INSURED_WISE);
						
					} 
				}
			
			}

			
		});
		NativeButton viewBtn = getViewButtonForOP(policyKey, viewType);
		viewBtn.setCaption("View");
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
		optionLayout.addComponent(viewBtn);
	
		return optionLayout;
	}
	
	
	public OptionGroup getOptionGroup(Policy policy,OMPIntimation intimation) {
		 viewType = new OptionGroup();
			//Vaadin8-setImmediate() viewType.setImmediate(false);
		
	
		 	if(policy.getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY))
		 	{
		 		viewType.addItem(POLICY_WISE);
		 		viewType.addItem(INSURED_WISE);
		 		viewType.setItemEnabled(INSURED_WISE, false);
//		 		if(cbox!=null && cbox.getValue()!=null){
//		 			viewType.setValue(cbox.getValue());
//		 		}
		 	
		 		
		 	}
			else
		 	{
		 		viewType.addItem(POLICY_WISE);
		 		viewType.addItem(INSURED_WISE);
		 	}
			viewType.setStyleName("inlineStyle");
			viewType.addItem(POLICY_WISE);
			viewType.addItem(INSURED_WISE);
			viewType.setValue(POLICY_WISE);
			//viewType.setValue(INSURED_WISE);
			return viewType;
		}
	
	private NativeButton getViewButtonForOP(final Long policyKey,
			final OptionGroup viewType) {
		 final NativeButton viewBtn = new NativeButton();
		viewBtn.setCaption("View");
		viewBtn.setData(viewType);
	
		viewBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						mainLayout.removeAllComponents();
						
						mainLayout.addComponent(viewType, 0);
						
						mainLayout.setSpacing(true);
//						
						mainLayout.setComponentAlignment(viewType, Alignment.MIDDLE_LEFT);
						mainLayout.addComponent(viewBtn, 1);
						mainLayout.addComponent(cbox, 2);
						mainLayout.setComponentAlignment(viewBtn, Alignment.MIDDLE_CENTER);
						//Vaadin8-setImmediate() mainLayout.setImmediate(true);
						cbox.setEnabled(true);
						getClaimByPolicyWiseForOP(policyKey,intimationNo);
						
						
					} else if (viewType.getValue().equals(INSURED_WISE)) {
						
						mainLayout.removeAllComponents();
						mainLayout.addComponent(viewType, 0);
						mainLayout.setSpacing(true);
						
						mainLayout.addComponent(viewBtn, 1);
						mainLayout.setComponentAlignment(viewBtn, Alignment.MIDDLE_CENTER);
						//Vaadin8-setImmediate() mainLayout.setImmediate(true);
						cbox.setValue(null);
						cbox.setEnabled(false);
						
						if(intimationNo!=null){
							getClaimByInsuredWiseForOP(intimationNo);
						} else {
//							 getClaimByRegisterInsuredWiseForOP(claimAndDocumentPage);
						}
						// getClaimByInsuredWiseForOP(intimationNo);
					} 
				}

			}

		});
		return viewBtn;
	}
	
	
	@SuppressWarnings({ "deprecation", "serial" })
	public void getClaimByPolicyWiseForOP(Long policyKey, String intimationNo){
		
		//Intimation intimation =intimationService.getIntimationByNo(intimationNo);
		Policy policy = policyService.getPolicyByKey(policyKey);
		List<OMPClaim> claimsByPolicyNumber = claimService
				.getClaimsByPolicyNumber(policy.getPolicyNumber());
		
		OMPClaim currentClaim = null;
		if(intimationNo != null) {
			currentClaim = claimService.getClaimsByIntimationNumber(intimationNo);
		}
		mainLayout.setSpacing(true);
		
		List<PreviousClaimsTableDTO> claimList = new ArrayList<PreviousClaimsTableDTO>();
		
		//List<Claim> claimLisByOP= new ArrayList<Claim>();
		if(claimsByPolicyNumber!=null){
			
		for(OMPClaim claim :claimsByPolicyNumber){
			
				if(claim.getClaimType()!=null && (claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT) || claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
					PreviousClaimsTableDTO previousClaimsTableDTO=new PreviousClaimsTableDTO();
					previousClaimsTableDTO.setPolicyNumber(claim.getIntimation().getPolicy().getPolicyNumber());
					previousClaimsTableDTO.setOmpIntimation(claim.getIntimation());
					previousClaimsTableDTO.setClaimNumber(claim.getClaimId());
					previousClaimsTableDTO.setClaimStatus(claim.getStatus().getProcessValue());
					
//					OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claim.getKey());
//					previousClaimsTableDTO.setPolicyYear(claim.getIntimation().getPolicy().getPolicyYear() != null ?claim.getIntimation().getPolicy().getPolicyYear().toString(): "");
//					if(null != opHealthCheckup && opHealthCheckup.getAmountPayable()!=null){
//						Double amountPayable=0d;
//						previousClaimsTableDTO.setAmountPayable(opHealthCheckup.getAmountPayable().doubleValue());
//					}
//					if(null != opHealthCheckup){
//						List<OPDocumentBillEntry> opDocumentBillEntry = processOPRequestService.getOpBillEntryByOPHealthKey(opHealthCheckup.getKey());
//						Long claimAmountInteger = 0l;
//						for(OPDocumentBillEntry opDocumentBill:opDocumentBillEntry){
//							if(opDocumentBill.getClaimedAmount() != null){
//								claimAmountInteger += opDocumentBill.getClaimedAmount();	
//							}	
//					}
//						previousClaimsTableDTO.setClaimedAmount(claimAmountInteger);	
//					}
					
					if(cbox !=null && cbox.getValue() !=null && String.valueOf(claim.getIntimation().getPolicy().getPolicyYear()).equals(cbox.getValue().toString())) {
						if(currentClaim == null) {
							claimList.add(previousClaimsTableDTO);
						} else if(!currentClaim.getClaimId().equalsIgnoreCase(claim.getClaimId())) {
							claimList.add(previousClaimsTableDTO);
						}
						
					} else if(cbox == null | cbox.getValue() == null | (cbox.getValue() != null && cbox.getValue().toString().toLowerCase().equalsIgnoreCase("all")))  {
						if(currentClaim == null) {
							claimList.add(previousClaimsTableDTO);
						} 
						else if(!currentClaim.getClaimId().equalsIgnoreCase(claim.getClaimId())) {
							claimList.add(previousClaimsTableDTO);
						}
					}	
		
		mainLayout.setSpacing(true);
		viewOmpPreviousClaimDataTable.init(POLICY_WISE, false, false);
		viewOmpPreviousClaimDataTable.setTableList(claimList);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.addComponent(viewOmpPreviousClaimDataTable);
		}
		
		}
		
		}
		if(claimList == null || claimList.isEmpty()){
			Notification.show("", "No Records Found", Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	
	private void getClaimByInsuredWiseForOP(String intimationNo){
		if(intimationNo!=null){
			
		//Policy policy=policyService.getPolicyByKey(policyKey);
		OMPIntimation intimation=intimationService.getIntimationByNo(intimationNo);
		List<OMPClaim> claimsByIntimationKey =claimService.getClaimsByPolicyNumber(intimation.getPolicyNumber());
		
		//List<Claim> claimsByPolicyKey= claimService.getClaimsByPolicyNumber(policy.getPolicyNumber());
		OMPClaim claimsByIntimationNo = claimService.getClaimsByIntimationNumber(intimationNo);
			
			
		List<PreviousClaimsTableDTO> claimList = new ArrayList<PreviousClaimsTableDTO>();
		
		for(OMPClaim claim : claimsByIntimationKey){
			if(!claim.getClaimId().equals(claimsByIntimationNo.getClaimId()) && claim.getIntimation().getInsured().equals(claimsByIntimationNo.getIntimation().getInsured())) {
				if(claim.getClaimType()!=null && (claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT) || claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
//					OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claim.getKey());
					PreviousClaimsTableDTO previousClaimsTableDTO = new PreviousClaimsTableDTO();
					previousClaimsTableDTO.setPolicyNumber(claim.getIntimation().getPolicy().getPolicyNumber());
					previousClaimsTableDTO.setOmpIntimation(claim.getIntimation());
					previousClaimsTableDTO.setClaimNumber(claim.getClaimId());
					previousClaimsTableDTO.setInsuredName(intimation.getInsured().getInsuredName());
//					previousClaimsTableDTO.setInsuredPatientName(claim.getIntimation().getInsuredPatientName());
					previousClaimsTableDTO.setClaimStatus(claim.getStatus().getProcessValue());
					previousClaimsTableDTO.setPolicyYear(claim.getIntimation().getPolicy().getPolicyYear() != null ?claim.getIntimation().getPolicy().getPolicyYear().toString(): "");
//					if(null != opHealthCheckup && opHealthCheckup.getAmountPayable()!=null){
//						previousClaimsTableDTO.setAmountPayable(opHealthCheckup.getAmountPayable().doubleValue());
//					}
//					if(null != opHealthCheckup){
//					List<OPDocumentBillEntry> opDocumentBillEntry = processOPRequestService.getOpBillEntryByOPHealthKey(opHealthCheckup.getKey());
//					Long claimAmountInteger = 0l;
//					for(OPDocumentBillEntry opDocumentBill:opDocumentBillEntry){
//						if(opDocumentBill.getClaimedAmount() != null){
//							claimAmountInteger += opDocumentBill.getClaimedAmount();	
//						}	
//				
//					}
//					previousClaimsTableDTO.setClaimedAmount(claimAmountInteger);
//					
//					}
					claimList.add(previousClaimsTableDTO);
					
				}
			}
		}
		if(claimList!=null || !claimList.isEmpty()){
		mainLayout.setSpacing(true);
		viewOmpPreviousClaimDataTable.init(INSURED_WISE, false, false);
		viewOmpPreviousClaimDataTable.setTableList(claimList);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.addComponent(viewOmpPreviousClaimDataTable);
	}
		
		if(claimList.isEmpty() || claimList == null){
			Notification.show("", "No Records Found", Notification.TYPE_ERROR_MESSAGE);
		}
		}
		
	}
	
	private void getClaimByRegisterInsuredWiseForOP(ClaimAndDocumentDetailsPageUI claimAndDocumentPage){
		

//		
////		
////		if(documentDetailsPageInstance.getInsuredKey()!=null){
////			Long key = documentDetailsPageInstance.getInsuredKey();
//		
//		
//		Insured insured = insuredService.getCLSInsured(key.toString());
//		
//		if(insured.getPolicy().getKey()!=null){
//		
//		Policy policy = policyService.getPolicyByKey(insured.getPolicy().getKey());
//		
//		List<OMPIntimation> intimationlist = policyService
//				.getIntimationListByInsured(String.valueOf(insured.getInsuredId()));
//	
//		List<PreviousClaimsTableDTO> claimList = new ArrayList<PreviousClaimsTableDTO>();
//		
//		
//		for(OMPIntimation intimationList:intimationlist){
//			
//			
//			List<OMPClaim> claimOfIntimation = claimService.getClaimByIntimation(intimationList.getKey());
//			
//			OMPClaim claim = null;
//			
//			if(! claimOfIntimation.isEmpty()){
//			claim = claimOfIntimation.get(0);
//			}
//			
//			if(intimationList.getClaimType().getKey()!=null && (intimationList.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT) || intimationList.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
//				if(claim!=null){
////				OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claim.getKey());
//				PreviousClaimsTableDTO previousClaimsTableDTO = new PreviousClaimsTableDTO();
//				previousClaimsTableDTO.setPolicyNumber(intimationList.getPolicyNumber());
//				previousClaimsTableDTO.setOmpIntimation(claim.getIntimation());
//				previousClaimsTableDTO.setClaimNumber(claim.getClaimId());
//
//				previousClaimsTableDTO.setInsuredPatientName(claim.getIntimation().getInsured().getInsuredName());
//				previousClaimsTableDTO.setClaimStatus(claim.getStatus().getProcessValue());
//				previousClaimsTableDTO.setClaimStatus("OP Registered");
//				if(intimationList.getClaimType() != null && intimationList.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)) {
//					previousClaimsTableDTO.setClaimStatus("HC Registered");
//				}
//				if(intimationList.getStatus().getKey().equals(ReferenceTable.OP_APPROVE)) {
//					previousClaimsTableDTO.setClaimStatus("Approved");
//				} else if(intimationList.getStatus().getKey().equals(ReferenceTable.OP_REJECT)) {
//					previousClaimsTableDTO.setClaimStatus("Rejected");
//				}
//				previousClaimsTableDTO.setPolicyYear(claim.getIntimation().getPolicy().getPolicyYear() != null ?claim.getIntimation().getPolicy().getPolicyYear().toString(): "");
//				if(null != opHealthCheckup && opHealthCheckup.getAmountPayable()!=null){
//					previousClaimsTableDTO.setAmountPayable(opHealthCheckup.getAmountPayable().doubleValue());
//				}
//				if(null != opHealthCheckup){
//				List<OPDocumentBillEntry> opDocumentBillEntry = processOPRequestService.getOpBillEntryByOPHealthKey(opHealthCheckup.getKey());
//				Long claimAmountInteger = 0l;
//				for(OPDocumentBillEntry opDocumentBill:opDocumentBillEntry){
//					if(opDocumentBill.getClaimedAmount() != null){
//						claimAmountInteger += opDocumentBill.getClaimedAmount();	
//					}	
//			
//			}
//				previousClaimsTableDTO.setClaimedAmount(claimAmountInteger);
//				
//				}
//				claimList.add(previousClaimsTableDTO);
//				}
//			}
//			
//			
//		}
//		
//	
//		mainLayout.setSpacing(true);
//		viewOmpPreviousClaimDataTable.init(INSURED_WISE, false, false);
//		viewOmpPreviousClaimDataTable.setTableList(claimList);
//		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
//		mainLayout.addComponent(viewOmpPreviousClaimDataTable);
//		
//		}	
//		
//	}else
//		{
//			getErrorMessage("Select Insured Name");
//		}
//		
//	}
	}
	
	public void getErrorMessage(String eMsg) {

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
