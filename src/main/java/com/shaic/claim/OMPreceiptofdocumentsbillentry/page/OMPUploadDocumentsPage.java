/**
 * 
 */
package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class OMPUploadDocumentsPage extends ViewComponent {
	
	
	@Inject
	public OMPClaimCalculationViewTableDTO bean;
	
	//@Inject
	//private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	public UploadedDocumentsForOMP uploadedDocsTable;
	
	@Inject
	private OMPUploadDocumentGridForm uploadDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
	@EJB
	private MasterService masterService;
	
	@PostConstruct
	public void init() {

	}
	
	public void init(OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		this.bean = calculationViewTableDTO;
		getContent();
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean.getReceiptOfDocumentsDTO()
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
		uploadedTblList = new ArrayList<UploadDocumentDTO>();
		fireViewEvent(OMPUploadDocumentsPresenter.OMP_UPLOAD_DOC_SETUP_DROPDOWN_VALUES, bean);
		UploadDocumentDTO uploadDocumentsDTO = bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO();
		uploadDocumentsDTO.setRodKey(bean.getReceiptOfDocumentsDTO().getRodKeyFromPayload());
		uploadDocumentsDTO.setIntimationNo(bean.getReceiptOfDocumentsDTO().getIntimationNo());
		uploadDocsTable.init(bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO(), SHAConstants.OMP_ROD);
		//uploadDocsTable.setFileTypeValues(fileTypeValues);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.initPresenter(SHAConstants.OMP_ROD);
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
		setCompositionRoot(uploadDocMainLayout);
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
		//uploadDocsTable = new OMPUploadDocumentGridForm();
		//uploadDocsTable.init(new UploadDocumentDTO(), SHAConstants.OMP_ROD);
		
	}
	
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getReceiptOfDocumentsDTO().getUploadDocsList())
		{
			uploadedDocsTable.removeRow();
			//Clearing the old list and re intializing the same for new values
			reset();
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getReceiptOfDocumentsDTO().getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				uploadDocumentDTO.setsNo(String.valueOf(uploadDocsDTO.indexOf(uploadDocumentDTO)+1));
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileTypeValue());
				uploadDocumentDTO.setFileName(uploadDocumentDTO.getFileName());
				uploadDocumentDTO.setDocumentType(uploadDocumentDTO.getDocumentType());
				uploadDocumentDTO.setDocReceivedDate(uploadDocumentDTO.getDocReceivedDate());
				uploadDocumentDTO.setReceivStatusValue(uploadDocumentDTO.getReceivStatusValue());
				uploadDocumentDTO.setNoOfItems(uploadDocumentDTO.getNoOfItems());
				uploadDocumentDTO.setRemarks(uploadDocumentDTO.getRemarks());
				uploadDocumentDTO.setIntimationNo(uploadDocumentDTO.getIntimationNo());
				uploadDocumentDTO.setRodKey(uploadDocumentDTO.getRodKey());
				uploadDocumentDTO.setRodNo(uploadDocumentDTO.getRodNo());
				if(uploadDocumentDTO.getUploadedBy() != null){
					uploadDocumentDTO.setUploadedBy(uploadDocumentDTO.getUploadedBy());
				}
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
				uploadedTblList.add(uploadDocumentDTO);
			}
		}
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			
//			this.uploadDocsTable.init(new UploadDocumentDTO(), SHAConstants.OMP_ROD);
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
			//List<UploadDocumentDTO> values = this.uploadedDocsTable.getValues();
			List<UploadDocumentDTO> uploadDocsList = this.bean.getReceiptOfDocumentsDTO().getUploadDocsList();
			this.bean.getReceiptOfDocumentsDTO().setUploadDocsList(uploadDocsList);
		}
		if(null != uploadedDocsTable)
		{
			this.bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
	
	}
	
	
	@SuppressWarnings("unchecked")
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		fileTypeValues = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 //uploadDocsTable.setFileTypeValues(fileTypeValues);

	}
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			if(this.bean!=null && null != this.bean.getReceiptOfDocumentsDTO().getRodNumberForUploadTbl())
			{
				uploadDocsDTO.setRodNo(this.bean.getReceiptOfDocumentsDTO().getRodNumberForUploadTbl());
			}
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			uploadDocsDTO.setRemarks(uploadDocsDTO.getRemarks());
			uploadDocsDTO.setEmptyRowStatus(false);
			uploadDocsDTO.setClaimType(this.bean.getReceiptOfDocumentsDTO().getClaimDTO().getClaimType());
			uploadDocsDTO.setUpdatedOn(new Timestamp(System.currentTimeMillis()).toString());
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);

			String modifyEmployeeName = "";
			String employeeName = "";
			if(userId != null){
				TmpEmployee employee =  masterService.getUserLoginDetail(userId);
				if(employee != null){
					if(employee.getEmpFirstName() != null){
						employeeName = employeeName+employee.getEmpFirstName();
					}
					if(employee.getEmpMiddleName() != null){
						employeeName = employeeName+employee.getEmpMiddleName();
					}
					if(employee.getEmpLastName() != null){
						employeeName = employeeName+employee.getEmpLastName();
					}
				}
				modifyEmployeeName = userId +" - "+employeeName;
				uploadDocsDTO.setUploadedBy(modifyEmployeeName);
			}

			List<UploadDocumentDTO> values = uploadedDocsTable.getValues();
			Integer sno =0;
			if(uploadedTblList!=null && values!=null){
				uploadedTblList.addAll(values);
				 sno =values.size();
			}else{
				sno =values.size();
			}
			uploadDocsDTO.setsNo(String.valueOf(sno+1));
			if(uploadDocsDTO.getReceivStatus()!=null){
				uploadDocsDTO.setReceivStatusValue(uploadDocsDTO.getReceivStatus().getValue());
			}
			if(uploadDocsDTO.getDocumentType()!=null){
				uploadDocsDTO.setDocumentTypeValue(uploadDocsDTO.getDocumentType().getValue());
			}
			uploadDocsDTO.setIntimationNo(uploadDocsDTO.getIntimationNo());
			uploadDocsDTO.setRodKey(uploadDocsDTO.getRodKey());
			uploadDocsDTO.setRodNo(uploadDocsDTO.getRodNo());
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
			fireViewEvent(OMPUploadDocumentsPresenter.OMP_SAVE_UPLOADED_DOCUMENTS, uploadDocsDTO);
//			List<UploadDocumentDTO> uploadDocsList = receiptOfDocumentsDTO.getUploadDocsList();//DD
			this.uploadedDocsTable.setValue(uploadDocsDTO);
			bean.getReceiptOfDocumentsDTO().setUploadDocsList(uploadedDocsTable.getValues());
	}
	
	
	
	
	
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable)
		{
			dto.setDeleted("Y");
			this.uploadedDocsTable.removeRow(dto);
			fireViewEvent(OMPUploadDocumentsPresenter.OMP_SAVE_UPLOADED_DOCUMENTS, dto);

			this.uploadedTblList.clear();
				
			List<UploadDocumentDTO> values = this.uploadedDocsTable.getValues();
			Integer sno = 1;
			for (UploadDocumentDTO uploadDocumentDTO : values) {
					uploadDocumentDTO.setsNo(sno.toString());
					sno = sno +1;
					
			}	
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
//			uploadDocsTable.init(dto,SHAConstants.OMP_ROD);
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UploadDocumentDTO> getValues() {
		if(uploadedDocsTable!=null){
			List<UploadDocumentDTO> values = uploadedDocsTable.getValues();
			return values;
		}
		return null;
 	}	

}

