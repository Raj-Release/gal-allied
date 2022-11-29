/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsForBillEntry;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.srikanthp
 *
 */
@SuppressWarnings("serial")
public class UploadDocumentsPage extends ViewComponent {
	
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
	//private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private UploadedDocumentsForBillEntry uploadedDocsTable;
	
	@Inject
	private UploadDocumentGridForm uploadDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
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
		
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "Add Additional Documents");
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.initPresenter(SHAConstants.ADD_ADDITIONAL_DOCUMENTS);
		uploadedDocsTable.setReference(this.referenceData);
		addUploadedDocsTableValues();

		
		/*VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable,btnUpload);
		uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);*/
		
		VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);
		//uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		
		HorizontalLayout uploadedDocLayout = new HorizontalLayout(uploadedDocsTable);
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);

		uploadDocsPanel.setContent(uploadDocLayout);
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		setTableValues();
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
			this.bean.setUploadDocsList(uploadedTblList);
		}
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			this.uploadDocsTable.init(new UploadDocumentDTO(), "Add Additional Documents");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			
		}
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				 this.bean.setUploadDocsList(uploadedTblList);
				
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
			//Temporary removed for 6081
			/*if(uploadedDocsTable != null && uploadedDocsTable.getValues() != null && ! uploadedDocsTable.getValues().isEmpty()){
				uploadedTblList.clear();
				uploadedTblList.addAll(uploadedDocsTable.getValues());
			}*/
			
			//uploadedTblList.add(uploadDocsDTO);
			
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
		try
		{
			this.binder.commit();
		}
		catch (CommitException ce)
		{
			ce.printStackTrace();
		}
		return true;
	}

	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,"Add Additional Documents");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
		}
	}

}

