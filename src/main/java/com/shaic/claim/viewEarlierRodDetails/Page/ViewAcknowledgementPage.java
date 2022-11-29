package com.shaic.claim.viewEarlierRodDetails.Page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.tables.RODQueryTableForAcknowlegementDetails;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDocumentCheckListTable;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewAcknowledgementPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private DocumentDetailsDTO bean;
	
	@Inject
	private RODQueryTableForAcknowlegementDetails rodQueryDetails;
	
	@Inject
	private ReconsiderationTableForView reconsiderRequestDetails;
	
	@Inject
	private ViewDocumentCheckListTable documentCheckList;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private TextField txtDocumentsReceivedFrom;
	
	private TextField txtAcknowledgementNo;
	
	private TextField txtAcknowledgementDate;
	
	private TextField txtAckCreatedById;
	
	private TextField txtAckCreatedByName;
	
	private TextField txtAcknowledgementContactNo;
	
	private TextField txtHospitalizationAmt;
	
	private TextField txtPreHospitalizationAmt;
	
	private TextField txtPostHospitalizationAmt;
	
	private TextField txtdocumentsReceivedDate;
	
	private TextField txtEmailId;
	
	private TextField txtModeOfReceipt;
	
	private TextField txtReconsiderationRequest;
	
	private TextField txtAdditionalRemarks;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkRepeatHospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private TextArea txtAdditonRemarks;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentDetailsService;
	
//	@EJB
//	private ViewDetails viewDetails;
	
	/*@EJB
	private MasterService masterService;*/
	
	
	public void init(Intimation intimationNo,Long acknowledgementKey){
		
                                          //need to implements
		List<DocumentCheckListDTO> rodDocumentList = documentDetailsService.getDocumentList(acknowledgementKey);
		
		DocumentDetailsDTO values = documentDetailsService.getAcknowledgementDetails(acknowledgementKey);
		
		DocAcknowledgement docAcknowledgement = documentDetailsService.getDocAcknowledgementBasedOnKey(acknowledgementKey);
		
//		List<ReconsiderRODRequestTableDTO> reconsiderRODList = documentDetailsService.getReconsiderRequestTableValues(docAcknowledgement.getClaim());
		
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = documentDetailsService.getReconsiderationDetailsList(docAcknowledgement);
		
		List<RODQueryDetailsDTO> rodQueryList = documentDetailsService.getQueryDetailsList(acknowledgementKey);
		
		this.bean = values;
		String format="";
		if(docAcknowledgement.getDocumentReceivedDate() != null){
			
			Date date = docAcknowledgement.getDocumentReceivedDate();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			format = sdf.format(date);
			
		}
		
    	
    	this.binder = new BeanFieldGroup<DocumentDetailsDTO>(DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		
		if(docAcknowledgement.getCreatedDate() !=null){
			String formatDate = SHAUtils.formatDate(docAcknowledgement.getCreatedDate());
			this.bean.setAcknowledgmentCreateOn(formatDate);
		}
		
		
		txtAcknowledgementNo = (TextField)binder.buildAndBind("Acknowledgement No", "acknowledgementNumber", TextField.class);
		
		txtAcknowledgementDate = (TextField)binder.buildAndBind("Acknowledgement Created on	", "acknowledgmentCreateOn", TextField.class);
		
//		FormLayout firstForm = new FormLayout(txtAcknowledgementNo,txtAcknowledgementDate);
//		firstForm.setSpacing(true);
		
		txtAckCreatedById = (TextField)binder.buildAndBind("Acknowledgement Created By - ID", "acknowledgmentCreatedId", TextField.class);
		txtAckCreatedByName = (TextField)binder.buildAndBind("Acknowledgement Created By - Name", "acknowledgmentCreatedName", TextField.class);
		txtAckCreatedById.setValue(docAcknowledgement.getCreatedBy());
		txtAckCreatedByName.setValue(docAcknowledgement.getCreatedBy());
//		FormLayout secondForm = new FormLayout(txtAckCreatedById,txtAckCreatedByName);
//		secondForm.setSpacing(true);
//		
//		HorizontalLayout firstHor = new HorizontalLayout(firstForm,secondForm);
//		firstHor.setSpacing(true);
//		
//		Panel firstPanel = new Panel(firstHor);
//		firstPanel.setCaption("View Acknowledgement Details");
//		firstPanel.addStyleName("gridBorder");
		
		
		txtDocumentsReceivedFrom = (TextField)binder.buildAndBind("Documents  Recieved  From", "documentReceivedFromValue", TextField.class);
//		txtdocumentsReceivedDate = (TextField)binder.buildAndBind("Documents  Recieved  Date", "documentsReceivedDate", TextField.class);
		txtdocumentsReceivedDate = new TextField("Documents  Recieved  Date");
		txtdocumentsReceivedDate.setValue(format);
		txtModeOfReceipt = (TextField)binder.buildAndBind("Mode  of  Receipt", "modeOfReceiptValue", TextField.class);
		txtReconsiderationRequest = (TextField)binder.buildAndBind("Reconsideration  Request", "reconsiderationRequestValue", TextField.class);
		txtAcknowledgementContactNo = (TextField)binder.buildAndBind("Acknowledgement  Contact  Number ", "acknowledgmentContactNumber", TextField.class);
		txtHospitalizationAmt = (TextField) binder.buildAndBind("Amount Claimed(Hospitalization)", "hospitalizationClaimedAmount", TextField.class);
		txtPreHospitalizationAmt = (TextField) binder.buildAndBind("Amount Claimed(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPostHospitalizationAmt = (TextField) binder.buildAndBind("Amount Claimed(Post-Hosp)", "postHospitalizationClaimedAmount", TextField.class);
		txtEmailId = (TextField)binder.buildAndBind("Email  ID", "emailId", TextField.class);
	
		
		FormLayout thirdForm = new FormLayout(txtAcknowledgementNo,txtAcknowledgementDate,txtDocumentsReceivedFrom,txtdocumentsReceivedDate,txtModeOfReceipt,txtReconsiderationRequest);
		thirdForm.setSpacing(true);
		setReadOnly(thirdForm, true);
		
		FormLayout fourthForm = new FormLayout(txtAckCreatedById,txtAckCreatedByName,txtEmailId,txtAcknowledgementContactNo,txtHospitalizationAmt,txtPreHospitalizationAmt
				,txtPostHospitalizationAmt);
		
		txtHospitalizationAmt.setVisible(false);
		txtPreHospitalizationAmt.setVisible(false);
		txtPostHospitalizationAmt.setVisible(false);
		
		fourthForm.setSpacing(true);
		setReadOnly(fourthForm, true);
		
		HorizontalLayout secondHor = new HorizontalLayout(thirdForm,fourthForm);
		secondHor.setWidth("110%");
		secondHor.setSpacing(true);
		
		Panel panel = new Panel(secondHor);
		panel.setSizeFull();
		
		chkhospitalization = (CheckBox)binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		chkhospitalization.setEnabled(false);
		chkPreHospitalization = (CheckBox)binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		chkPreHospitalization.setEnabled(false);
		chkPostHospitalization = (CheckBox)binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		chkPostHospitalization.setEnabled(false);
		chkPartialHospitalization = (CheckBox)binder.buildAndBind("Partial Hospitalisation", "partialHospitalization", CheckBox.class);
		chkPartialHospitalization.setEnabled(false);
		chkLumpSumAmount = (CheckBox)binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		chkLumpSumAmount.setEnabled(false);
		chkAddOnBenefitsHospitalCash = (CheckBox)binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		chkAddOnBenefitsHospitalCash.setEnabled(false);
		chkAddOnBenefitsPatientCare = (CheckBox)binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		chkAddOnBenefitsPatientCare.setEnabled(false);
		chkRepeatHospitalization = (CheckBox)binder.buildAndBind("Hospitalisation(Repeat)", "hospitalizationRepeat", CheckBox.class);
		chkRepeatHospitalization.setEnabled(false);
		
		chkOtherBenefits = (CheckBox)binder.buildAndBind("Hospitalisation(Repeat)", "otherBenefits", CheckBox.class);
		chkOtherBenefits.setEnabled(false);
		
		chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evacuation", "emergencyMedicalEvaluation", CheckBox.class);
		chkEmergencyMedicalEvaluation.setEnabled(false);
		
		chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
		chkCompassionateTravel.setEnabled(false);
		
		chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
		chkRepatriationOfMortalRemains.setEnabled(false);
		
		chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
		if(null != intimationNo && intimationNo.getPolicy() != null&& intimationNo.getPolicy().getProduct() != null &&  (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationNo.getPolicy().getProduct().getKey())
				|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(intimationNo.getPolicy().getProduct().getKey()))){
			chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
		}
		chkPreferredNetworkHospital.setEnabled(false);
		
		chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
		chkSharedAccomodation.setEnabled(false);
		
		FormLayout firstHospitalForm = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout secondHospitalForm = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout thirdHospitalForm = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout fourthHospitalForm = new FormLayout(chkPartialHospitalization,chkRepeatHospitalization);
		FormLayout fifthHospitalForm = new FormLayout(chkOtherBenefits);
		firstHospitalForm.setSpacing(true);
		secondHospitalForm.setSpacing(true);
		thirdHospitalForm.setSpacing(true);
		fourthHospitalForm.setSpacing(true);
		
		FormLayout otherBenefitLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
		FormLayout otherBenefitLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
		FormLayout otherBenefitLayout3 = new FormLayout(chkRepatriationOfMortalRemains);
		otherBenefitLayout1.setSpacing(true);
		otherBenefitLayout2.setSpacing(true);
		
		HorizontalLayout benefitsHor = new HorizontalLayout(firstHospitalForm,secondHospitalForm,thirdHospitalForm,fourthHospitalForm,fifthHospitalForm);
		HorizontalLayout otherBenefithor = new HorizontalLayout(otherBenefitLayout1,otherBenefitLayout2,otherBenefitLayout3);
		
		VerticalLayout benefitLayout = new VerticalLayout(benefitsHor,otherBenefithor);
		
		benefitLayout.setCaption("Bill Classification");
		benefitLayout.setSpacing(false);
		
		
		reconsiderRequestDetails.init("", false, false);
		reconsiderRequestDetails.setHeight("200px");
		reconsiderRequestDetails.setRemoveAllItems();
		
//		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		
		if (reconsiderRODList != null) {
			for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderRODList) {
				reconsiderRequestDetails
						.addBeanToList(reconsiderRODRequestTableDTO);
			}
		}
		
		rodQueryDetails.init("View Query Details", false, false);
		rodQueryDetails.initpresenterString(SHAConstants.VIEW_ACKNOWLEDGEMENT_DETAILs);
		if (rodQueryList != null) {
			int i=1;
			for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryList) {
				rodQueryDetailsDTO.setQueryNo(i);
                  rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
                  i++;
			}
		}
		
		documentCheckList.init("Document Checklist",false, false);
		if (rodDocumentList != null) {
			documentCheckList.setTableList(rodDocumentList);
			/*for (DocumentCheckListDTO documentCheckListDTO : rodDocumentList) {

				documentCheckList.addBeanToList(documentCheckListDTO);

			}*/
		}
		VerticalLayout mainVertical= null;
	    if(txtReconsiderationRequest.getValue() != null){
	    	if(txtReconsiderationRequest.getValue().equalsIgnoreCase("Yes")){
	    	 mainVertical = new VerticalLayout(panel,reconsiderRequestDetails,benefitLayout,rodQueryDetails,documentCheckList);
	    	}
	    	else{
	    		
	    		 mainVertical = new VerticalLayout(panel,benefitLayout,rodQueryDetails,documentCheckList);
	    	}
	    }
	    if(mainVertical == null){
	    	 mainVertical = new VerticalLayout(panel,benefitLayout,rodQueryDetails,documentCheckList);
	    }
		
		mainVertical.setSpacing(true);
		
		
		setCompositionRoot(mainVertical);
	}
	
	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout.iterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	public void clearObjects(){
//	 	SHAUtils.setClearTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		this.binder = null;
		this.bean = null;
 }
}
