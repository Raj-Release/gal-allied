package com.shaic.arch.utils;

import java.util.HashMap;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class StarCommonUtils {
	
	
	@SuppressWarnings("static-access")
	public static Boolean alertMessage(UI ui, String message) {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(ui.getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		return true;
	*/

   		final MessageBox showInfo= showInfoMessageBox(message);
   		Button homeButton = showInfo.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfo.close();
			}
		});
		return true;
		
	}
	
	
	public static SelectValue getCorrectSelectValue(List<SelectValue> values, String code) {
		SelectValue value = null;
		for (SelectValue selectValue : values) {
			if(selectValue.getCommonValue() != null && code != null && code.equalsIgnoreCase(selectValue.getCommonValue())) {
				value = selectValue;
				break;
			}
		}
		return value;
	}
	
	public static Boolean shouldDisableDomicillary(PreauthDTO bean) {
		Boolean enableOrDisable = true;
		if((bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) || (ReferenceTable.getComprehensiveProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getPreHospitalizaionFlag() ||  bean.getPostHospitalizaionFlag() || (bean.getIsHospitalizationRepeat() != null && bean.getIsHospitalizationRepeat()) || (bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) || (bean.getAddOnBenefitsHospitalCash() != null && bean.getAddOnBenefitsHospitalCash()) || (bean.getAddOnBenefitsPatientCare() != null && bean.getAddOnBenefitsPatientCare())))) {
			enableOrDisable = false;
		}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_27_PRODUCT)){
			enableOrDisable = false;
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GROUP_TOPUP_PROD_KEY)){
			enableOrDisable = false;
		}
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getPreHospitalizaionFlag() ||  
				bean.getPostHospitalizaionFlag() || (bean.getIsHospitalizationRepeat() != null && bean.getIsHospitalizationRepeat()) || 
				(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) || 
				(bean.getAddOnBenefitsHospitalCash() != null && bean.getAddOnBenefitsHospitalCash()) || 
				(bean.getAddOnBenefitsPatientCare() != null && bean.getAddOnBenefitsPatientCare()))){
			enableOrDisable = false;
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_KEY_IND) 
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_KEY_FLT)){
			enableOrDisable = false;
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct() != null && (SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
				&& ("B").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))){
			enableOrDisable = false;
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) 
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
			enableOrDisable = false;
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND)){
			enableOrDisable = false;
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM)){	
			enableOrDisable = false;	
		}	
		
		return enableOrDisable;
	}
	
	
	
	public static Boolean shouldShowDomicillaryAlert(PreauthDTO bean) {
		Boolean showAlert = false;
		PreauthDataExtaractionDTO preauthDataExtractionDetails = bean.getPreauthDataExtractionDetails();
		if((ReferenceTable.getComprehensiveProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDataExtractionDetails.getDomicillaryHospitalisation() != null && preauthDataExtractionDetails.getDomicillaryHospitalisation())
				|| (ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && preauthDataExtractionDetails.getDomicillaryHospitalisation() != null && preauthDataExtractionDetails.getDomicillaryHospitalisation())) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(preauthDataExtractionDetails.getTreatmentStartDate(), preauthDataExtractionDetails.getTreatmentEndDate());
			if(noOfDays <= 3) {
				showAlert = true;
			}
		}
		return showAlert;
	}
	
	public static void setAmountconsideredForLumpsum(PreauthDTO bean) {
		Double consideredAmount = 0d;
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			List<UploadDocumentDTO> uploadList = bean.getUploadDocumentDTO();
			for (UploadDocumentDTO uploadDocumentDTO : uploadList) {
				consideredAmount += uploadDocumentDTO.getNetAmount() != null ? uploadDocumentDTO.getNetAmount() : 0d;
			}
			bean.setInitialAmountConsidered(String.valueOf(consideredAmount.longValue()));
			bean.setAmountConsidered(String.valueOf(consideredAmount.longValue()));
			bean.setAmbulanceAmountConsidered(String.valueOf(consideredAmount.longValue()));
		}
	}
	
	public static void getIncidentFlagAndDate(Claim claim, PreauthDTO preauthDTO) {
		if(claim != null) {
			if(claim.getIncidenceFlag() != null ) {
				preauthDTO.getPreauthDataExtractionDetails().setAccidentOrDeath(false);
				if(claim.getIncidenceFlag().equals(SHAConstants.ACCIDENT_FLAG)){
					preauthDTO.getPreauthDataExtractionDetails().setAccidentOrDeath(true);
				}
			}
			preauthDTO.getPreauthDataExtractionDetails().setDateOfDeathAcc(claim.getIncidenceDate());			
			
			if(null != claim.getAccidentDate())
			{
				preauthDTO.getPreauthDataExtractionDetails().setDateOfAccident(claim.getAccidentDate());
			}
			
			if(null != claim.getDeathDate())
			{
				preauthDTO.getPreauthDataExtractionDetails().setDateOfDeath(claim.getDeathDate());
			}
			
			if(null != claim.getDisablementDate())
			{
				preauthDTO.getPreauthDataExtractionDetails().setDateOfDisablement(claim.getDisablementDate());
			}
		}
	}
	
	public static ProcedureDTO getDefaultProcedureDTO(ProcedureDTO procedureDTO) {
		SelectValue procedureStatus = new SelectValue();
		procedureStatus.setId(ReferenceTable.PROCEDURE_STATUS_EXCLUDED_MASTER_ID);
		procedureStatus.setValue("Excluded");
		
		SelectValue exclusionDetails = new SelectValue();
		exclusionDetails.setId(ReferenceTable.EXCLUSION_NOT_APPLICABLE_MASTER_ID);
		exclusionDetails.setValue("Not Applicable");
		
		procedureDTO.setProcedureStatus(procedureStatus);
		procedureDTO.setExclusionDetails(exclusionDetails);
		
		return procedureDTO;
	}
	
	
	public static void getAccidentAndDeathDate(Claim claim, ReceiptOfDocumentsDTO rodDTO) {
		
		if(claim != null) {
			if(claim.getIncidenceFlag() != null ) {
				rodDTO.getDocumentDetails().setAccidentOrDeath(false);
				if(claim.getIncidenceFlag().equals(SHAConstants.ACCIDENT_FLAG)){
					rodDTO.getDocumentDetails().setAccidentOrDeath(true);
				}
			}

		rodDTO.getDocumentDetails().setAccidentOrDeathDate(
				claim.getIncidenceDate());	
			
		
		if(null != claim.getAccidentDate())
		{
			rodDTO.getDocumentDetails().setDateOfAccident(claim.getAccidentDate());
		}
		
		if(null != claim.getDeathDate())
		{
			rodDTO.getDocumentDetails().setDateOfDeath(claim.getDeathDate());
		}
		
		if(null != claim.getDisablementDate())
		{
			rodDTO.getDocumentDetails().setDateOfDisablement(claim.getDisablementDate());
		}
		}	
	}
	
	public static void showPopup(final UI ui, final String message, final String clmPrcsInstruction) {/*

		 String message1 = message.replaceAll("(.{200})", "$1<br /> &nbsp&nbsp&nbsp&nbsp");
		 message1 = message1.replaceAll("(\r\n|\n)", "<br />");

		Label successLabel = new Label(
				"<b style = 'color: red;'>" +   message1 + "</br>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		//layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		layout.setHeightUndefined();
		if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
			layout.setHeight("100%");
			layout.setWidth("100%");
			}
			
		}			
		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		if(message != null && message.contains("Sublimit selected is ")){
			dialog.setId("sublimitAlert");
		}
	
		
		if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
				dialog.setWidth("55%");
			}
		}
		dialog.show(ui.getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	dialog.close();
					 	if(clmPrcsInstruction!=null && !clmPrcsInstruction.equalsIgnoreCase(message)){
					 		showPopup(ui,clmPrcsInstruction,clmPrcsInstruction);
					 	}
				}
			});
  
	
	 */
		String message1 = message.replaceAll("(.{200})", "$1<br /> &nbsp&nbsp&nbsp&nbsp");
		message1 = message1.replaceAll("(\r\n|\n)", "<br />");
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message1+" <br> "+clmPrcsInstruction,	buttonsNamewithType); //CR2019112
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		
		homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					 	/*if(clmPrcsInstruction!=null && !clmPrcsInstruction.equalsIgnoreCase(message)){
					 		showPopup(ui,clmPrcsInstruction,clmPrcsInstruction);
					 	}*/
				}
			});	
	
	}
	
public static MessageBox showInfoMessageBox(String message){
		
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
		
		
	}
}
