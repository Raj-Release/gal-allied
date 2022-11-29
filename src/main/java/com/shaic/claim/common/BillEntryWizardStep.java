package com.shaic.claim.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/
import org.vaadin.teemu.wizards.WizardStep;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class BillEntryWizardStep extends ViewComponent implements WizardStep<PreauthDTO> {
	private static final long serialVersionUID = 8285268650169999229L;
	private PreauthDTO bean;
	
	private VerticalLayout layout;
	private String presenterString = "";
	
	private UploadedDocumentsListenerTable billEntryObj;
	
	private Button btnBillingWorksheetBtn;
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	private RevisedMedicalDecisionTable revisedMedicalDecisionTableObj;
	
	private  AmountConsideredTable amountConsideredTable;
	
	private ClaimRequestMedicalDecisionButtons uiPage;
	
	private FormLayout userRoleFrmLayout;
	private HorizontalLayout horUserLayout;
	
	private Map<String, Object> referenceData;
	
	private Window popup;
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}
	
	public void setMedicalDecisiontable(RevisedMedicalDecisionTable revisedMedicalDecisionTableObj) {
		this.revisedMedicalDecisionTableObj = revisedMedicalDecisionTableObj;
		
	}
	
	public void setAmountConsideredTable( AmountConsideredTable amountConsideredTable){
		this.amountConsideredTable = amountConsideredTable;
	}
	
	public void setReferenceData(Map<String, Object> referenceData){
		this.referenceData = referenceData;
	}
	
	public void setUserRoleLayout(HorizontalLayout hLayout,FormLayout userLayout,ClaimRequestMedicalDecisionButtons uIpage){
		horUserLayout = hLayout;
		userRoleFrmLayout = userLayout;
		this.uiPage = uIpage;
	}
	
	
	
 	@Override
	public String getCaption() {
		return "Bill View";
	}

	@Override
	public Component getContent() {
		
		return layout;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		if(validatePage()){
		if(this.presenterString.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
			
			fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SAVE_BILL_ENTRY, this.bean);
	
		} else if (this.presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
			fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SAVE_BILL_ENTRY, this.bean);
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			String subCover = "";
			if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
				subCover = this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
			}
			Double balanceSI = dbCalculationService.getBalanceSIForReimbursementStarCancerGold(this.bean.getNewIntimationDTO().getPolicy().getKey() ,this.bean.getNewIntimationDTO().getInsuredPatient().getKey(),this.bean.getClaimKey(), this.bean.getKey(),subCover).get(SHAConstants.TOTAL_BALANCE_SI);
			
			this.bean.setBalanceSI(balanceSI);
			this.amountConsideredTable.setBalanceSumInsuredAfterRecharge(balanceSI);
		}
		
		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
		
			if(amountConsideredTable != null){
				amountConsideredTable.setPostHospitalAmountValue(bean);
			}
		}
		if(revisedMedicalDecisionTableObj != null) {
			List<DiagnosisProcedureTableDTO> values = this.revisedMedicalDecisionTableObj.getValues();
			
			int medicalDecisionSize = 0;
			
			for (DiagnosisProcedureTableDTO medicalDecisionDto : values) {
				if(! medicalDecisionDto.getIsDeletedOne()){
					medicalDecisionSize++;
				}
			}
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : values) {
				if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
					Integer subLimitAvaliableAmt = 0;
					Boolean isResidual = false;
					if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName() != null && (diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
						subLimitAvaliableAmt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
					} else if (diagnosisProcedureTableDTO.getProcedureDTO() != null && diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName() != null && (diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
						subLimitAvaliableAmt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
					} else {
						isResidual = true;
					}
					
					if(!isResidual) {
						Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
						Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
						int minValue = Math.min(SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getSubLimitAmount()) , availAmt);
						diagnosisProcedureTableDTO.setSubLimitAvaliableAmt(minValue);
						diagnosisProcedureTableDTO.setSubLimitUtilAmount(0);
						
						
					}
				}
				
				if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())) {
					
					if(diagnosisProcedureTableDTO.getDiagOrProcedure() != null && ! diagnosisProcedureTableDTO.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount") && ! diagnosisProcedureTableDTO.getIsDeletedOne()){
						
						if(this.bean.getIsAmbulanceApplicable()){
							diagnosisProcedureTableDTO.setIsAmbulanceEnable(true);
							if(medicalDecisionSize == 2){
								diagnosisProcedureTableDTO.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(true);
								
								Integer netAmount = diagnosisProcedureTableDTO.getNetAmount();
								
								if(netAmount != null){
									netAmount += this.bean.getAmbulanceLimitAmount().intValue();
								}
								
								Integer submitAvailableAmt = -1;
								Integer selectedSIAvaliableAmt = -1;
								
							    Boolean sublimitApplicable = SHAUtils.isSublimitApplicable(diagnosisProcedureTableDTO);
							    if(sublimitApplicable){
							    	submitAvailableAmt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
							    }
							    
							    Boolean sIavailable = SHAUtils.isSIavailable(diagnosisProcedureTableDTO);
							    if(sIavailable){
							    	selectedSIAvaliableAmt = diagnosisProcedureTableDTO.getAvailableAmout();
							    }

								diagnosisProcedureTableDTO.setAmtWithAmbulanceCharge(netAmount);

								Integer findMinimumValuesForCommon = SHAUtils.findMinimumValuesForCommon(netAmount, submitAvailableAmt, selectedSIAvaliableAmt);
								diagnosisProcedureTableDTO.setMinimumAmount(findMinimumValuesForCommon);
								diagnosisProcedureTableDTO.setReverseAllocatedAmt(findMinimumValuesForCommon);
								
								revisedMedicalDecisionTableObj.calculateTotal(false);
                               
							}
						}else{
							diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
							diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
						}

					}else{
						diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
						diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
						if(diagnosisProcedureTableDTO.getIsDeletedOne()){
							diagnosisProcedureTableDTO.setAmountConsidered(0);
						}
					}
					
				}
			}
			
			if(this.uiPage != null){
				//R1295
				bean.setQueryType(null);
				bean.setQueryCount(0);
				FormLayout buildUserRoleLayout = this.uiPage.buildUserRoleLayout();
				userRoleFrmLayout.removeAllComponents();
				userRoleFrmLayout.addComponent(buildUserRoleLayout);
				horUserLayout.addComponent(userRoleFrmLayout);
			}
			
			
//			if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())) {
//				this.revisedMedicalDecisionTableObj.init(bean);
//				
//				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : values) {
//					this.revisedMedicalDecisionTableObj.addBeanToList(diagnosisProcedureTableDTO);
//				}
//				
//			}

		}

		
		return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Boolean validatePage(){
		
		Boolean hasError = false;
		String eMsg = "";	
       List<UploadDocumentDTO> values = billEntryObj.getValues();
		
		for (UploadDocumentDTO uploadDocumentDTO : values) {
			if(uploadDocumentDTO.getFileType() != null && uploadDocumentDTO.getFileType().getValue().contains("Bill")){
				if (!uploadDocumentDTO.getStatus()) {
					hasError = true;
					eMsg = "Please Enter Bill Entry Details";
				}
			}
		}
		
		if (hasError) {
//			setRequired(true);
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			hasError = true;
			return !hasError;
		}else{
			return true;
		}
	}
	
	public void setComponent(UploadedDocumentsListenerTable object, String presenterString) {
		this.presenterString = presenterString;
		layout = new VerticalLayout();
		layout.addComponent(object);
		intializeButton();
		HorizontalLayout hLayout = new HorizontalLayout(btnBillingWorksheetBtn);
		layout.addComponent(hLayout);
		layout.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);
		billEntryObj = object;
	}
	
	private void intializeButton()
	{
		btnBillingWorksheetBtn = new Button("Billing WorkSheet");
		btnBillingWorksheetBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				

				/**
				 * On click, the rrc histiory needs to displayed.
				 * */
				popup = new com.vaadin.ui.Window();
				
				
				
				//uploadDocumentViewImpl.init(bean,popup);
				/*if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString))
				{
					uploadDocumentViewImpl.initPresenter(SHAConstants.ZONAL_REVIEW_BILLING_WORKSHEET);
				}
				else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
				{
					uploadDocumentViewImpl.initPresenter(SHAConstants.CLAIM_REQUEST_BILLING_WORKSHEET);
				}*/
				
				uploadDocumentViewImpl.initPresenter(SHAConstants.BILLING_WORKSHEET);
				uploadDocumentViewImpl.init(bean,popup);
				
				popup.setCaption("Billing Worksheet");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(uploadDocumentViewImpl);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			
				
			}
		});	
	}

}
