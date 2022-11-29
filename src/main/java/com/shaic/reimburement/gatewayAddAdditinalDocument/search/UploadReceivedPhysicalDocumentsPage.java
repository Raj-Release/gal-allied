package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UploadReceivedPhysicalDocumentsPage extends ViewComponent{


	
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
	//private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private UploadedReceivedPhysicalDocumentsTable uploadedDocsTable;
	
	@Inject
	private UploadReceivedDocumentGridForm uploadDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
	private Button saveButton;
	
	private List<String> errorMessages;
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		this.errorMessages = new ArrayList<String>();
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean
				.getUploadDocumentsDTO());
		uploadDocMainLayout = new VerticalLayout();
		uploadDocsPanel = new Panel();
		uploadedDocsPanel = new Panel();

	}
	
	public Component getContent() 
	{	
		initBinder();
		//uploadDocsTable.init("Upload Documents", false);
		//uploadDocsTable.init("Upload Documents");
		
		//uploadedTblList = new ArrayList<UploadDocumentDTO>();
		String modereceiptValue = bean.getDocumentDetails().getModeOfReceiptValue() !=null ? bean.getDocumentDetails().getModeOfReceiptValue():"";
		
		uploadDocsTable.init(bean.getUploadDocumentsDTO(),modereceiptValue,this.bean.getScreenName());
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.initPresenter(this.bean.getScreenName());
		uploadedDocsTable.setReference(this.referenceData);
		addUploadedDocsTableValues();	
		
		VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);		
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		
		saveButton = new Button("Save");		
		
		HorizontalLayout uploadedDocLayout = new HorizontalLayout(uploadedDocsTable);
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
	

		uploadDocsPanel.setContent(uploadDocLayout);
		uploadedDocsPanel.setContent(uploadedDocLayout);		
		
		uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		uploadDocMainLayout.addComponent(saveButton);
		setTableValues();
		addListener();
		return uploadDocMainLayout;
	}
	
	
	public void reset()
	{
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			this.uploadedTblList.clear();
		}
		
		if(null != this.uploadedDocsTable)
		{
			List<UploadDocumentDTO> uploadedDeletedList = this.uploadedDocsTable.getDeletedDocumentList();
			if(null != uploadedDeletedList && !uploadedDeletedList.isEmpty())
			{
				uploadedDeletedList.clear();
			}
		}
		
	}
	
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getUploadDocsList())
		{
			uploadedDocsTable.removeRow();
			//Clearing the old list and re intializing the same for new values
			reset();
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
				//uploadDocumentDTO.setFileName("chestctscan.jpg");
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
				uploadedTblList.add(uploadDocumentDTO);
			}
		}
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			String modereceiptValue = bean.getDocumentDetails().getModeOfReceiptValue() !=null ? bean.getDocumentDetails().getModeOfReceiptValue():"";
			this.uploadDocsTable.init(new UploadDocumentDTO(),modereceiptValue,this.bean.getScreenName());
			uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				
			}			
		}
		
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.uploadedDocsTable)
		{
			this.bean.setUploadDocsList(this.uploadedDocsTable.getValues());
		}
		if(null != uploadedDocsTable)
		{
			this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
	
	}
	
	
	@SuppressWarnings("unchecked")
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 uploadDocsTable.setFileTypeValues(fileTypeValues);

	}
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			if(null != this.bean.getRodNumberForUploadTbl())
			{
				uploadDocsDTO.setRodNo(this.bean.getRodNumberForUploadTbl());
			}
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			uploadDocsDTO.setEmptyRowStatus(false);
			uploadDocsDTO.setClaimType(this.bean.getClaimDTO().getClaimType());			
			
			if(null != uploadDocsDTO.getIsEdit() && !uploadDocsDTO.getIsEdit())
			{
				uploadedTblList.add(uploadDocsDTO);
			}
			setTableValues();
		}
		
		
	}
	
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable)
		{
			
			this.uploadedDocsTable.removeRow(dto);

			this.uploadedTblList.clear();
				
			List<UploadDocumentDTO> values = this.uploadedDocsTable.getValues();
				
//			this.uploadedTblList.remove(dto);
			this.uploadedTblList.addAll(values);

		}
		
		
	}
	
	public Boolean isValid()
	{
		boolean hasError = false;
		
		errorMessages.removeAll(getErrors());
		try
		{
			this.binder.commit();
			
			int i = 0;
			if(null != uploadedTblList && !uploadedTblList.isEmpty()){
				
				for (UploadDocumentDTO uploadedDocumentsList : uploadedTblList) {
					
					if(uploadedDocumentsList.getIsReceived() || uploadedDocumentsList.getIsIgnored()){
						
						i++;
					}
					if(uploadedDocumentsList.getDocReceivedDate() == null){
						hasError = true;
						errorMessages
						.add("Please Enter the Received Date");
					}
					if(uploadedDocumentsList.getDocumentType()== null || uploadedDocumentsList.getDocumentType().getValue() == null){
						hasError = true;
						errorMessages
						.add("Please Select the Doc Type");
					}
					if(!(uploadedDocumentsList.getIsReceived() || uploadedDocumentsList.getIsIgnored())){
						hasError = true;
						errorMessages
						.add("Please Select either Received or Ignore");
					}
				}	
				
				if(uploadedTblList.size() == i){
					
					SelectRODtoAddAdditionalDocumentsDTO selectRodDocuments = new SelectRODtoAddAdditionalDocumentsDTO();
					
					selectRodDocuments.setRodNo(bean.getRodNumberForUploadTbl());
					selectRodDocuments.setBillClassification(getBillClassificationValue(bean));
					selectRodDocuments.setDocUplodedDate(bean.getDocumentDetails().getDocumentsReceivedDate());
					selectRodDocuments.setRodKey(bean.getDocumentDetails().getRodKey());
					bean.setSelectedPhysicalDocumentsDTO(selectRodDocuments);
				}
			
			}
		}
		catch (CommitException ce)
		{
			ce.printStackTrace();
		}
		return !hasError;
	}

	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			String iseditClicked = "isEditClicked";
			String modereceiptValue = bean.getDocumentDetails().getModeOfReceiptValue() !=null ? bean.getDocumentDetails().getModeOfReceiptValue():"";
			uploadDocsTable.init(dto,modereceiptValue,iseditClicked);
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
		}
	}

	
	public void addListener(){
		saveButton.addClickListener(new Button.ClickListener() {
		
		private static final long serialVersionUID = 7255298985095729669L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			if(null != bean && null != uploadedTblList && !uploadedTblList.isEmpty()){
			bean.setUploadDocsList(uploadedTblList);
			}
			fireViewEvent(UploadReceivedPhysicalDocumentsPresenter.SAVE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS, bean);
		}
	});
	}

	public Boolean validatePage()
	{
		
		if(null != uploadedDocsTable){
			List<Long> rodKeyList = new ArrayList<Long>();
			List<UploadDocumentDTO> values = uploadedDocsTable.getValues();
			for (UploadDocumentDTO uploadDocumentDTO : values) {
				if(uploadDocumentDTO.getIsReceived() || uploadDocumentDTO.getIsIgnored()){
					if(! rodKeyList.contains(uploadDocumentDTO.getRodKey())){
						rodKeyList.add(uploadDocumentDTO.getRodKey());
					}
				}
			}
		}
		return null;
		
	}
	
	
	private String getBillClassificationValue(ReceiptOfDocumentsDTO bean) {
		StringBuilder strBuilder = new StringBuilder();
		// StringBuilder amtBuilder = new StringBuilder();
		// Double total = 0d;
		try {
			if (("Y").equals(bean.getDocumentDetails().getHospitalizationFlag())) {
				strBuilder.append("Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(bean.getDocumentDetails().getPreHospitalizationFlag())) {
				strBuilder.append("Pre-Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(bean.getDocumentDetails().getPostHospitalizationFlag())) {
				strBuilder.append("Post-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(bean.getDocumentDetails().getPartialHospitalizationFlag())) {
				strBuilder.append("Partial-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(bean.getDocumentDetails().getLumpSumAmountFlag())) {
				strBuilder.append("Lumpsum Amount");
				strBuilder.append(",");

			}
			/*if (("Y").equals(bean.getDocumentDetails().getHospitalCashFlag())) {
				strBuilder.append("Add on Benefits (Hospital cash)");
				strBuilder.append(",");

			}
			if (("Y").equals(bean.getDocumentDetails().getPatientCareFlag())) {
				strBuilder.append("Add on Benefits (Patient Care)");
				strBuilder.append(",");
			}*/
			if (("Y").equals(bean.getDocumentDetails().getHospitalizationRepeatFlag())) {
				strBuilder.append("Hospitalization Repeat");
				strBuilder.append(",");
			}
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}
	
	public void buildSuccessLayout(){		

		Label successLabel = new Label(
				"<b style = 'color: black;'>Physical Documents saved successfully !!! </b>",
				ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
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

			}
		});
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
}
