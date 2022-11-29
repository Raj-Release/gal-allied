package com.shaic.claim.cashlessprocess.downsizeRequest.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.cashlessprocess.downsize.wizard.PreviousPreauthSummaryTable;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ClaimedAmountPageForDownsizeRequest extends ViewComponent implements
WizardStep<PreauthDTO> {
	
	@Inject
	private Instance<PreviousPreauthSummaryTable> preauthPreviousDetailsPage;
	
	private PreviousPreauthSummaryTable objPreviousPreAuthDetailsTable;
	
	@Inject
	private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;
	
	private NewClaimedAmountTable claimedDetailsTableObj;
	
	private BeanFieldGroup<PreauthDTO> binder;
	
	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private Button initiatePEDButton;
	
	private VerticalLayout wholeVLayout;
	
	private TextField txtAmtClaimed;
	
	private TextField txtDiscntHospBill;
	
	private TextField txtNetAmt;
	
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	@Override
	public String getCaption() {
		return "Claimed Amount";
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDTO>(PreauthDTO.class);
		
		//R1006
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("preauthDataExtractionDetails.amtClaimed");
		item.addNestedProperty("preauthDataExtractionDetails.disCntHospBill");
		item.addNestedProperty("preauthDataExtractionDetails.netAmt");
		
		this.binder.setItemDataSource(item);
	}

	@Override
	public Component getContent() {
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		if(bean.isInsuredDeleted()){
			alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
		}else{
			alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
		}
		
		 if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
	    }
		//added for installment payment status check at policy level
		 if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 SHAUtils.showPolicyInstalmentAlert();
		 }
        initiatePEDButton = new Button("Initiate PED Endorsement");
        if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()){
			initiatePEDButton.setEnabled(false);
		}
		else{
			initiatePEDButton.setEnabled(true);
		}
        
		VerticalLayout pedVertical = new VerticalLayout(initiatePEDButton);
		pedVertical.setComponentAlignment(initiatePEDButton, Alignment.TOP_RIGHT);
		pedVertical.setWidth("100%");
		
		objPreviousPreAuthDetailsTable = preauthPreviousDetailsPage.get();
		objPreviousPreAuthDetailsTable.init("Pre-auth Summary", false, false);
		objPreviousPreAuthDetailsTable.setTableList(this.bean.getPreviousPreauthTableDTO());
		
		claimedDetailsTableObj =   claimedAmountDetailsTable.get();
		claimedDetailsTableObj.initView(this.bean, SHAConstants.DOWNSIZE_PREAUTH_REQUEST_SCREEN);
		Label amtClaimedDetailsLbl = new Label("Amount Claimed Details");
		
		List<NoOfDaysCell> claimDetails = new ArrayList<NoOfDaysCell>();
		
		for (NoOfDaysCell noOfDaysCell : this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList()) {
			claimDetails.add(noOfDaysCell);
		}
		
		List<NoOfDaysCell> claimedDetailsList = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
		claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(claimDetails);
		
		//R1006
		txtAmtClaimed = binder.buildAndBind("Amount Claimed" , "preauthDataExtractionDetails.amtClaimed",TextField.class);
		txtAmtClaimed.setNullRepresentation("");
		txtAmtClaimed.setMaxLength(15);
		
		CSValidator ClaimedAmtValidator = new CSValidator();
		ClaimedAmtValidator.extend(txtAmtClaimed);
		ClaimedAmtValidator.setRegExp("^[0-9]*$");
		ClaimedAmtValidator.setPreventInvalidTyping(true);
		
		txtAmtClaimed.addBlurListener(getclaimedAmtListener());
		
		txtDiscntHospBill = binder.buildAndBind("Discount in Hospital Bill" , "preauthDataExtractionDetails.disCntHospBill",TextField.class);
		txtDiscntHospBill.setNullRepresentation("");
		txtDiscntHospBill.setMaxLength(15);
		
		
		CSValidator disCntHospBillAmtValidator = new CSValidator();
		disCntHospBillAmtValidator.extend(txtDiscntHospBill);
		disCntHospBillAmtValidator.setRegExp("^[0-9]*$");
		disCntHospBillAmtValidator.setPreventInvalidTyping(true);
		
		txtNetAmt = binder.buildAndBind("Claimed amount after Discount" , "preauthDataExtractionDetails.netAmt",TextField.class);
		txtNetAmt.setNullRepresentation("");
		txtNetAmt.setMaxLength(15);
		txtNetAmt.setEnabled(false);
		
		CSValidator netAmtValidator = new CSValidator();
		netAmtValidator.extend(txtNetAmt);
		netAmtValidator.setRegExp("^[0-9]*$");
		netAmtValidator.setPreventInvalidTyping(true);
		
		txtDiscntHospBill.addBlurListener(getHospDiscountBillListener());
		
		FormLayout claimedAmtFLayout = new FormLayout(txtAmtClaimed);
		claimedAmtFLayout.setMargin(false);
		FormLayout disCntFLayout = new FormLayout(txtDiscntHospBill);
		disCntFLayout.setMargin(false);
		FormLayout netAmtFLayout = new FormLayout(txtNetAmt);
		netAmtFLayout.setMargin(false);
		HorizontalLayout hospDiscountHLayout = new HorizontalLayout(claimedAmtFLayout,
				disCntFLayout, netAmtFLayout);
		hospDiscountHLayout.setWidth("100%");
		
		wholeVLayout = new VerticalLayout(pedVertical,objPreviousPreAuthDetailsTable,amtClaimedDetailsLbl,hospDiscountHLayout,claimedDetailsTableObj);
		wholeVLayout.setComponentAlignment(amtClaimedDetailsLbl, Alignment.TOP_LEFT);
		
		wholeVLayout.setComponentAlignment(objPreviousPreAuthDetailsTable, Alignment.TOP_RIGHT);
		
		wholeVLayout.setSpacing(true);
		
		addListener();
	   
		return wholeVLayout;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
		if(this.claimedDetailsTableObj != null) {
			this.claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		}
		
		if(this.bean.getDeletedClaimedAmountIds() != null && !this.bean.getDeletedClaimedAmountIds().isEmpty()) {
			this.claimedDetailsTableObj.setDeletedItems(this.bean.getDeletedClaimedAmountIds());
		}
		
		//Set Amount Requested and Amount Considered to PreauthDTO.
		/**
		 * Amount requested should be the pre auth request amount as per
		 * sathish Sir. Hence the data population from the amount
		 * claims table is commented . The amount requested is directly
		 * populated in the presenter. The same is set medical processing page. 
		 * */
//		this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalAmountByPropertyName("Amount Requested"));
//		this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalAmountByPropertyName("Amount Considered"));
		
		this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalClaimedAmt().toString());
		this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
		
	}

	@Override
	public boolean onAdvance() {
//		if(validatePage()){
//		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(this.claimedDetailsTableObj.getValues());
//		this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalClaimedAmt().toString());
//		this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
//		return true;
//		}
//		return false;
		
		return validatePage();
	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();	
		if(this.claimedDetailsTableObj != null && this.claimedDetailsTableObj.getTotalPayableAmt() <= 0){
			hasError = true;
			eMsg.append("Claimed Payable amount should not be Zero. </br>");
		}
		
		//R1006
		if(this.txtAmtClaimed != null && (this.txtAmtClaimed.getValue() == null || this.txtAmtClaimed.getValue().equalsIgnoreCase("")) ){
			txtAmtClaimed.setValidationVisible(true);
			hasError = true;
			eMsg.append("Enter Claimed Amount. </br>");
		}

		if(this.txtDiscntHospBill != null && (this.txtDiscntHospBill.getValue() == null || this.txtDiscntHospBill.getValue().equalsIgnoreCase("")) ){
			txtDiscntHospBill.setValidationVisible(true);
			hasError = true;
			eMsg.append("Enter Discount in Hospital Bill. </br>");
		}

		if(this.txtNetAmt != null && this.txtNetAmt.getValue() != null && !this.txtNetAmt.getValue().equalsIgnoreCase("") && this.claimedDetailsTableObj != null){
			Integer totalNetAmtforAmtconsd = claimedDetailsTableObj.getTotalClaimedAmt();
			Integer netAmt = Integer.valueOf(txtNetAmt.getValue());
			if(totalNetAmtforAmtconsd != null && !(totalNetAmtforAmtconsd.equals(netAmt))){
				hasError = true;
				eMsg.append("Claimed amount after discount to be equal to Claimed amount. </br>");
			}}
		
		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isValid(false)) {
			hasError = true;
			List<String> errors = this.claimedDetailsTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		} else {
//			Integer enteredDays = SHAUtils.getIntegerFromString(noOfDaysTxt.getValue());
//			if(this.claimedDetailsTableObj != null  && this.claimedDetailsTableObj.getTotalNoOfDays().compareTo(enteredDays) != 0) {
//				hasError = true;
//				eMsg += "The total of number of days entered against Room Rent and ICU should be equal to no. of days. </br> Claim Table Value is " + this.claimedDetailsTableObj.getTotalNoOfDays() + " and Data Extraction Value is " + enteredDays + "</br>";
//			}
		}
		
	
		if (hasError) {
			
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			
				try {
					//R1006
					this.binder.commit();
					
					this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
					this.bean.setAmountRequested(this.claimedDetailsTableObj.getTotalClaimedAmt().toString());
					this.bean.setEntitlmentNoOfDays(this.claimedDetailsTableObj.getTotalEntitlementNoOfDays());
					this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(this.claimedDetailsTableObj.getValues());
					
					List<NoOfDaysCell> values = this.claimedDetailsTableObj.getValues();
					
					this.bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
					
					if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
						Integer ambulanceAmount = SHAUtils.isAmbulanceAvailable(values);
						if(ambulanceAmount != null){
							this.bean.setIsAmbulanceApplicable(true);
							this.bean.setAmbulanceLimitAmount(Double.valueOf(ambulanceAmount));
							 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
						      totalAmountConsidered -= ambulanceAmount;
						      this.bean.setAmbulanceAmountConsidered(totalAmountConsidered.toString());
							
						}else{
							this.bean.setAmbulanceLimitAmount(0d);
							this.bean.setIsAmbulanceApplicable(false);
						}
		        		
		        	}
					
				} catch (CommitException e) {
					e.printStackTrace();
				}
			
				
				
			return true;
		}
	}
	
     public void addListener() {
		
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}
				
			}
		});
	}
     
     private void createPEDScreen(Long preauthKey, Long intimationKey,
 			Long policyKey, Long claimKey) {
 		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
 		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);		
 		viewPEDRequest.setPresenterString(SHAConstants.CASHLESS_STRING);
 		Window popup = new com.vaadin.ui.Window();
 		popup.setCaption("View PED Request Details");
 		popup.setWidth("85%");
 		popup.setHeight("100%");
 		popup.setContent(viewPEDRequest);
 		popup.setClosable(true);
 		popup.center();
 		popup.setResizable(true);
 		popup.addCloseListener(new Window.CloseListener() {
 			/**
 			 * 
 			 */
 			private static final long serialVersionUID = 1L;

 			@Override
 			public void windowClose(CloseEvent e) {
 				
 			}
 		});

 		popup.setModal(true);
 		UI.getCurrent().addWindow(popup);
 	}
 	
 	

 	
 	
 public Boolean alertMessageForPEDInitiate(final String message) {
    		Label successLabel = new Label(
 				"<b style = 'color: red;'>" + message + "</b>",
 				ContentMode.HTML);
    		final Boolean isClicked = false;
 		Button homeButton = new Button("OK");
 		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
// 		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
 		
 		VerticalLayout layout = new VerticalLayout(successLabel);
 		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		layout.addComponent(homeButton);
 		
 		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
 		layout.setSpacing(true);
 		layout.setMargin(true);
 		HorizontalLayout hLayout = new HorizontalLayout(layout);
 		hLayout.setMargin(true);
 		hLayout.setStyleName("borderLayout");

 		final ConfirmDialog dialog = new ConfirmDialog();	
// 		dialog.setCaption("Alert");
 		dialog.setClosable(false);
 		dialog.setContent(hLayout);
 		dialog.setResizable(false);
 		dialog.setModal(true);
 		dialog.show(getUI().getCurrent(), null, true);

 		homeButton.addClickListener(new ClickListener() {
 			private static final long serialVersionUID = 7396240433865727954L;

 			@Override
 			public void buttonClick(ClickEvent event) {
 				dialog.close();
 				
 				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					bean.setMultiplePEDAvailableNotDeleted(false);
				}
 				
 				Long preauthKey=bean.getKey();
 				Long intimationKey=bean.getIntimationKey();
 				Long policyKey=bean.getPolicyKey();
 				Long claimKey=bean.getClaimKey();
 				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
 			}
 		});
 		return true;
 	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		this.claimedDetailsTableObj.setDBCalculationValues(hospitalizationDetails);
		if(hospitalizationDetails != null && hospitalizationDetails.get(15) != null){
			this.bean.setAmbulanceLimitAmount((Double)hospitalizationDetails.get(15));
		}
		
	}
	
	private BlurListener getHospDiscountBillListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					
					if(txtAmtClaimed != null && txtAmtClaimed.getValue() != null && !txtAmtClaimed.getValue().isEmpty()){
						Integer discount = Integer.valueOf(value.getValue());
						Integer claimedAmt = Integer.valueOf(txtAmtClaimed.getValue());
						Integer netAmt = claimedAmt - discount;
						
						if(txtNetAmt != null){
							txtNetAmt.setValue(netAmt.toString());
						}
						
					}
										
					Integer discount = Integer.valueOf(value.getValue());
					discount = discount*= -1;
					
					if(claimedDetailsTableObj != null){
     					
     					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
     					
     					claimedAmountValues.addAll(claimedDetailsTableObj.getValues());
     					
     					NoOfDaysCell noOfDaysCell1 = null;
     					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
							if(noOfDaysCell.getBenefitId() != null && noOfDaysCell.getBenefitId().equals(21L)) {
								noOfDaysCell.setClaimedBillAmount(discount);
								noOfDaysCell.setNetAmount(discount);
								noOfDaysCell.setPaybleAmount(discount);
								noOfDaysCell1 = noOfDaysCell;
								break;
							}
						}
     					claimedDetailsTableObj.setValuesForHospDiscount(noOfDaysCell1);
     					
     				}
					
					
				}
					
			}
		};
		return listener;
	}
	
	private BlurListener getclaimedAmtListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtDiscntHospBill != null && txtDiscntHospBill.getValue() != null && !txtDiscntHospBill.getValue().isEmpty()){
						Integer claimedAmt = Integer.valueOf(value.getValue());
						Integer discount = Integer.valueOf(txtDiscntHospBill.getValue());
						Integer netAmt = claimedAmt - discount;
						
						if(txtNetAmt != null){
							txtNetAmt.setValue(netAmt.toString());
						}
						
					}
					
				}
					
			}
		};
		return listener;
	}


}
