package com.shaic.claim.reimbursement.billclassification;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class BillClassificationUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308951356475854324L;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;
	
	private BeanFieldGroup<PreauthDTO> binder;
	
	private VerticalLayout otherBenefitsLayout;
	
	private PreauthDTO bean;
	
	private CheckBox chkHospitalCash;
	
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDTO>(
				PreauthDTO.class);
		this.binder.setItemDataSource(bean);
	}
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		initBinder();
		/*otherBenefitsLayout = new VerticalLayout();	
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayout();
		setCompositionRoot(buildBillClassificationLayout);*/
		otherBenefitsLayout = new VerticalLayout();	
		VerticalLayout verticalBillClassificationLayout = new VerticalLayout();
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayout();
		verticalBillClassificationLayout.addComponents(buildBillClassificationLayout,otherBenefitsLayout);
	
		addListener();
		if(null != this.bean.getOtherBenefitsFlag() && (this.bean.getOtherBenefitsFlag() == true))
		{
			chkOtherBenefits.setValue(true);
		}
		
		chkEmergencyMedicalEvaluation.setEnabled(false);
		chkCompassionateTravel.setEnabled(false);
		chkRepatriationOfMortalRemains.setEnabled(false);
		chkPreferredNetworkHospital.setEnabled(false);
		chkSharedAccomodation.setEnabled(false);
		
		setCompositionRoot(verticalBillClassificationLayout);
	}
	
	public void initForEdit(PreauthDTO bean) {
		this.bean = bean;
		initBinder();
		/*otherBenefitsLayout = new VerticalLayout();	
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayoutForEdit();
		setCompositionRoot(buildBillClassificationLayout);*/
		
		otherBenefitsLayout = new VerticalLayout();	
		VerticalLayout verticalBillClassificationLayout = new VerticalLayout();
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayoutForEdit();
		verticalBillClassificationLayout.addComponents(buildBillClassificationLayout,otherBenefitsLayout);
		setCompositionRoot(verticalBillClassificationLayout);
	}
	
	private HorizontalLayout buildBillClassificationLayout() {
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		chkhospitalization.setEnabled(false);
		
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		chkPartialHospitalization.setEnabled(false);
		
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "isHospitalizationRepeat", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
		chkHospitalizationRepeat.setEnabled(false);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalizaionFlag", CheckBox.class);
		
		if(!this.bean.getIsPreHospApplicable()) {
			chkPreHospitalization.setEnabled(false);
		}
		chkPreHospitalization.setEnabled(false);
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalizaionFlag", CheckBox.class);
		
		if(!this.bean.getIsPostHospApplicable()){
			chkPostHospitalization.setEnabled(false);
		}
		chkPostHospitalization.setEnabled(false);
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmountFlag", CheckBox.class);
		if(!bean.getIsLumpsumApplicable()) {
			chkLumpSumAmount.setEnabled(false);
		}
		chkLumpSumAmount.setEnabled(false);
		
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		
		if(!this.bean.getIsHospitalCashApplicable()) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		chkAddOnBenefitsHospitalCash.setEnabled(false);
		
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		
		chkOtherBenefits = binder.buildAndBind(
				"Other Benefits", "otherBenefitsFlag",
				CheckBox.class);
		
		chkOtherBenefits.setValue(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
		chkOtherBenefits.setEnabled(false);
		

		chkHospitalCash = binder.buildAndBind("Hospital Cash", "hospitalCash", CheckBox.class);
		chkHospitalCash.setEnabled(false);
		chkHospitalCash.setValue(false);
		
		if(this.bean != null && this.bean.getReceiptOfDocumentsDTO() != null 
				&& null != this.bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCashFlag() 
				&& (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCashFlag()))
		{
			if(null != chkHospitalCash){
				chkHospitalCash.setValue(true);
			}
		}
		
		addListener();
		
		/*if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag()))
		{
			chkOtherBenefits.setValue(true);
		}*/
		
		if(null != this.bean.getOtherBenefitsFlag() && (this.bean.getOtherBenefitsFlag() == true))
		{
			chkOtherBenefits.setValue(true);
		}
		
		if(null != this.bean.getOtherBenefitsFlag() && (this.bean.getOtherBenefitsFlag() == false))
		{
			chkOtherBenefits.setValue(true);
			chkOtherBenefits.setValue(false);
		}
		
		
		if(!this.bean.getIsPatientCareApplicable()) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		chkAddOnBenefitsPatientCare.setEnabled(false);

		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
	//	classificationLayout1.setMargin(true);
	//	classificationLayout1.setWidth("20%");
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
	//	classificationLayout2.setMargin(true);
	//	classificationLayout2.setWidth("20%");
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
	//	classificationLayout3.setMargin(true);
	//	classificationLayout3.setWidth("40%");
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
	//	classificationLayout4.setMargin(true);
	//	classificationLayout4.setWidth("40%");
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits,chkHospitalCash);
		
	/*	FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization);*/
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4,classificationLayout5);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
	//	billClassificationLayout.setMargin(true);
	//	billClassificationLayout.setWidth("110%");
//		addBillClassificationLister();
		
		return billClassificationLayout;
	}
	
	private HorizontalLayout buildBillClassificationLayoutForEdit() {
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "isHospitalizationRepeat", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalizaionFlag", CheckBox.class);
		
		if(!this.bean.getIsPreHospApplicable()) {
			chkPreHospitalization.setEnabled(false);
		}
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalizaionFlag", CheckBox.class);
		
		if(!this.bean.getIsPostHospApplicable()){
			chkPostHospitalization.setEnabled(false);
		}
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmountFlag", CheckBox.class);
		if(!bean.getIsLumpsumApplicable()) {
			chkLumpSumAmount.setEnabled(false);
		}
		
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		
		if(!this.bean.getIsHospitalCashApplicable()) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		
		if(!this.bean.getIsPatientCareApplicable()) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}

		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
	//	classificationLayout1.setMargin(true);
	//	classificationLayout1.setWidth("20%");
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
	//	classificationLayout2.setMargin(true);
	//	classificationLayout2.setWidth("20%");
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
	//	classificationLayout3.setMargin(true);
	//	classificationLayout3.setWidth("40%");
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
	//	classificationLayout4.setMargin(true);
	//	classificationLayout4.setWidth("40%");
		
	/*	FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization);*/
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
	//	billClassificationLayout.setMargin(true);
	//	billClassificationLayout.setWidth("110%");
//		addBillClassificationLister();
		
		return billClassificationLayout;
	}
	
	private void addListener()
	{
		
		chkOtherBenefits .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				
										 
				 buildOtherBenefitsLayout(value);				
				 
				 }							
			}
			
		});
	}

	
	private void buildOtherBenefitsLayout(Boolean value)
	{
		if(value)
		{
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			
			chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evaluation", "emergencyMedicalEvaluation", CheckBox.class);
			
			chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
			
			chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
			
			chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
			}
			
			chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
			
			FormLayout otherBenefitsLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
			FormLayout otherBenefitsLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
			FormLayout otherBenefitsLayout3 = new FormLayout(chkRepatriationOfMortalRemains);			
			
			HorizontalLayout otherBenefitsLayput = new HorizontalLayout();
			otherBenefitsLayput.addComponents(otherBenefitsLayout1,otherBenefitsLayout2,otherBenefitsLayout3);
			
			if(null != this.bean.getClaimDTO().getClaimType() && (SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{								
				if( bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && 
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null &&
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
				{
					
				otherBenefitsLayput.removeAllComponents();
				otherBenefitsLayput.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
				
				}
			}
			otherBenefitsLayput.setSpacing(false);
			otherBenefitsLayput.setMargin(false);
			
			
			otherBenefitsLayout.addComponent(otherBenefitsLayput);
		}else {
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			otherBenefitsLayout.removeAllComponents();
		}
		
		//return otherBenefitsLayput;
		
	}
	
	
	
	private List<Field<?>> getListOfOtherBenefitsChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkEmergencyMedicalEvaluation);
		fieldList.add(chkCompassionateTravel);
		fieldList.add(chkRepatriationOfMortalRemains);
		fieldList.add(chkPreferredNetworkHospital);
		fieldList.add(chkSharedAccomodation);
		return fieldList;
	}
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
}
