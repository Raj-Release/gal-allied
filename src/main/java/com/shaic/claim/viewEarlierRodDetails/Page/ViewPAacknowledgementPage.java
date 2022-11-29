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
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.RODQueryTableForAcknowlegementDetails;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDocumentCheckListTable;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
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

public class ViewPAacknowledgementPage extends ViewComponent {

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
	
	private TextField txtdocumentsReceivedDate;
	
	private TextField txtEmailId;
	
	private TextField txtModeOfReceipt;
	
	private TextField txtReconsiderationRequest;
	
	private CheckBox chkDeath;
	
	private CheckBox chkPermanentPartialDisability;
	
	private CheckBox chkPermanentTotalDisability;
	
	private CheckBox chkTemporaryTotalDisability;
	
	private CheckBox chkPAHospitalisation;
	
	private CheckBox chkPAPartialHospitalisation;
	
	private TextArea txtAdditonRemarks;
	
	private TextField txtBenefitAmt;
	
	@Inject
	private PAOptionalCoverTableForView optionalCover;
	
	@Inject
	private PAAddOnCoverTableForView addOnCover;
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentDetailsService;
	
	@EJB
	private CreateRODService createRODService;
	
//	@EJB
//	private ViewDetails viewDetails;
	
	/*@EJB
	private MasterService masterService;*/
	@Inject
	private MasterService masterService;
	
	public void init(Intimation intimationNo,Long acknowledgementKey){
		
                                          //need to implements
		List<DocumentCheckListDTO> rodDocumentList = documentDetailsService.getDocumentList(acknowledgementKey);
		
		DocumentDetailsDTO values = documentDetailsService.getAcknowledgementDetails(acknowledgementKey);
		
		DocAcknowledgement docAcknowledgement = documentDetailsService.getDocAcknowledgementBasedOnKey(acknowledgementKey);
		
//		List<ReconsiderRODRequestTableDTO> reconsiderRODList = documentDetailsService.getReconsiderRequestTableValues(docAcknowledgement.getClaim());
		
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = documentDetailsService.getPAReconsiderationDetailsList(docAcknowledgement);
		
		List<RODQueryDetailsDTO> rodQueryList = documentDetailsService.getPAQueryDetailsList(acknowledgementKey);
		
		List<PAcoverTableViewDTO> optionalCoversList = createRODService.getOptionalCoversValueForView(acknowledgementKey);
		
		List<PAcoverTableViewDTO> addOnCoversList = createRODService.getAdditionalCoversValueForView(acknowledgementKey);
		
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
		txtEmailId = (TextField)binder.buildAndBind("Email  ID", "emailId", TextField.class);
		txtBenefitAmt = (TextField) binder.buildAndBind("Amount Claimed(Benefit)", "benefitClaimedAmt", TextField.class);
	
		
		FormLayout thirdForm = new FormLayout(txtAcknowledgementNo,txtAcknowledgementDate,txtDocumentsReceivedFrom,txtdocumentsReceivedDate,txtModeOfReceipt,txtReconsiderationRequest);
		thirdForm.setSpacing(true);
		setReadOnly(thirdForm, true);
		
		FormLayout fourthForm = new FormLayout(txtAckCreatedById,txtAckCreatedByName,txtEmailId,txtBenefitAmt,txtAcknowledgementContactNo);
		fourthForm.setSpacing(true);
		setReadOnly(fourthForm, true);
		
		HorizontalLayout secondHor = new HorizontalLayout(thirdForm,fourthForm);
		secondHor.setWidth("110%");
		secondHor.setSpacing(true);
		
		Panel panel = new Panel(secondHor);
		panel.setSizeFull();
		
		chkDeath = (CheckBox)binder.buildAndBind("Death", "death", CheckBox.class);
		chkDeath.setEnabled(false);
		
		chkPermanentPartialDisability = (CheckBox)binder.buildAndBind("Permanent Partial Disability", "permanentPartialDisability", CheckBox.class);
		chkPermanentPartialDisability.setEnabled(false);
		
		chkPermanentTotalDisability = (CheckBox)binder.buildAndBind("Permanent Total Disability", "permanentTotalDisability", CheckBox.class);
		chkPermanentTotalDisability.setEnabled(false);
		
		chkTemporaryTotalDisability = (CheckBox)binder.buildAndBind("Temporary Total Disability", "temporaryTotalDisability", CheckBox.class);
		chkTemporaryTotalDisability.setEnabled(false);
		
		chkPAHospitalisation = (CheckBox)binder.buildAndBind("Hospitalisation", "paHospitalisation", CheckBox.class);
		chkPAHospitalisation.setEnabled(false);
		
		chkPAPartialHospitalisation = (CheckBox)binder.buildAndBind("Partial Hospitalisation", "paPartialHospitalisation", CheckBox.class);
		chkPAPartialHospitalisation.setEnabled(false);
		
		HorizontalLayout partIbenifits = new HorizontalLayout(chkDeath,chkPermanentPartialDisability,chkPermanentTotalDisability,chkTemporaryTotalDisability,chkPAHospitalisation,chkPAPartialHospitalisation);
		partIbenifits.setCaption("Part I-Benifits");
		partIbenifits.setSpacing(true);
		partIbenifits.setMargin(true);
		
		optionalCover.init("Part II-Optional Covers", false, false);
		
		if(optionalCoversList != null)
		{
			int i=1;
			for(PAcoverTableViewDTO paCoverTableViewDTO :optionalCoversList)
			{
				paCoverTableViewDTO.setsNo(i);
				optionalCover.addBeanToList(paCoverTableViewDTO);
				i++;
			}
		}
		
		
		
		addOnCover.init("Part III-Add on Covers",false, false);
		
		if(addOnCoversList != null)
		{
			int i=1;
			for(PAcoverTableViewDTO paCoverTableViewDTO :addOnCoversList)
			{
				paCoverTableViewDTO.setsNo(i);
				addOnCover.addBeanToList(paCoverTableViewDTO);
				i++;
			}
		}
		
		
		reconsiderRequestDetails.init("", false, false);
		reconsiderRequestDetails.setPAColumns();
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
		
		rodQueryDetails.setPAColumnsForRODQuery();
		
		
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
		
		txtAdditonRemarks = (TextArea)binder.buildAndBind("Additional Remarks", "additionalRemarks", TextArea.class);
		txtAdditonRemarks.setEnabled(false);
		
		FormLayout fifthForm = new FormLayout(txtAdditonRemarks);
		fifthForm.setSpacing(true);
		setReadOnly(fifthForm, true);
		
		
		VerticalLayout mainVertical= null;
	    if(txtReconsiderationRequest.getValue() != null){
	    	if(txtReconsiderationRequest.getValue().equalsIgnoreCase("Yes")){
	    	 mainVertical = new VerticalLayout(panel,reconsiderRequestDetails,partIbenifits,optionalCover,addOnCover,rodQueryDetails,documentCheckList,fifthForm);
	    	}
	    	else{
	    		
	    		 mainVertical = new VerticalLayout(panel,partIbenifits,optionalCover,addOnCover,rodQueryDetails,documentCheckList,fifthForm);
	    	}
	    }
	    if(mainVertical == null){
	    	 mainVertical = new VerticalLayout(panel,partIbenifits,optionalCover,addOnCover,rodQueryDetails,documentCheckList,fifthForm);
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
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	 public void clearObjects(){
//		 	SHAUtils.setClearTableItem(tableItem);
			SHAUtils.setClearReferenceData(referenceData);
			this.binder = null;
			this.bean = null;
			this.masterService = null;
	 }
}
