/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.listenertable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDetailsPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.DetailsPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillEntryDetailsPresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class PAHealthUploadedDocumentsListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<UploadDocumentDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UploadDocumentDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UploadDocumentDTO> container = new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> classificationContainer ;
	
	private BeanItemContainer<SelectValue> category;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private PAHealthBillEntryTableUI billEntryTableObj;
	
	private Boolean billEntryFinalStatus = false;
	
	@Inject
	private Instance<PAHealthBillEntryTableUI> billEntryTableInstance;
	
	//private String billEntryStatus = "NO";
	public Boolean billEntryStatus = false;
	
	public Double totalClaimedValue ;
	
	private String presenterString = "";
	
	public TextField claimedAmtField;
	
	//public TextField netAmtField;
	
	
	public Double totalNetAmountValue;
	
	public Boolean isFinancial = false;
	
	public  Button button;
	
	public Map buttonMap = new HashMap();
	
	private TextField txtStatusOfBills;
	
	private int iTotalNoOfBills;
	
	private List<UploadDocumentDTO> uploadDocsDTO;

	private TextField addmissionDateFld;
	
	private TextField dischargeDateFld;
	
	private int iBillEntryTableSize;

	
	public void initPresenter(String presenterString, TextField addmissionDate , TextField dischargeDate)
	{
		this.presenterString = presenterString;
		addmissionDateFld = addmissionDate;
		dischargeDateFld = dischargeDate;
	}

	
//	private ReceiptOfDocumentsDTO bean;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	/*public void init(int tableSize)
	{
		iBillEntryTableSize = tableSize;
		init();
	}*/
	
	
	public void init() {
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		claimedAmtField = new TextField();
	//	netAmtField = new TextField();
		this.errorMessages = new ArrayList<String>();
		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		txtStatusOfBills = new TextField();
		
		txtStatusOfBills.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		HorizontalLayout btnLayout = new HorizontalLayout(txtStatusOfBills);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(txtStatusOfBills, Alignment.MIDDLE_RIGHT);
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		if(presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
			isFinancial = true;
		}
//		/layout.addComponent(btnLayout);
		initTable();
		table.setWidth("100%");
		if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString))
		{
			table.setHeight("500px");
		}
		else
		{
			table.setHeight("180px");
		}
		table.setPageLength(table.getItemIds().size());
		addListener();
		layout.addComponent(btnLayout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<UploadDocumentDTO> list) {
		table.removeAllItems();
		for (final UploadDocumentDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public void setCaption(String caption) {
		table.setCaption(caption);
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		generateColumn();
		
		table.removeGeneratedColumn("Status");
		
		table.addGeneratedColumn("Status", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				
				btnAdd = new Button();
				UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
				
				if(uploadDTO.getStatus())
				{
					btnAdd.setIcon(new ThemeResource("images/yesstatus.png"));
				}
				else
				{
					btnAdd.setIcon(new ThemeResource("images/cross_mark_black.jpg"));
				}
				btnAdd.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				return btnAdd;
			}
		});

		
		if(
			null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) ||
			(SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
		  )
		{
			table.setVisibleColumns(new Object[] { "Status","Select For<br>Bill Entry</br>","rodNo","fileType", "fileName", "billNo", "billDate", "noOfItems", "billValue","netAmount"});
			table.setColumnHeader("rodNo","ROD No");
			table.setColumnHeader("fileType", "File <br>Type</br>");
			table.setColumnHeader("fileName", "File <br>Name</br>");
			table.setColumnHeader("billNo", "Bill <br>No</br>");
			table.setColumnHeader("billDate", "Bill <br>Date</br>");
			table.setColumnHeader("noOfItems", "No of <br>Items</br>");
			table.setColumnHeader("billValue", "Bill Value<br>(Amt Claimed)</br>");
			table.setColumnHeader("netAmount", "Net <br>Amount</br>");
			table.setColumnWidth("netAmount",90);
		}
		else
		{
			table.setVisibleColumns(new Object[] { "Status","Select For<br>Bill Entry</br>","rodNo","fileType", "fileName", "billNo", "billDate", "noOfItems", "billValue"});
			table.setColumnHeader("rodNo","ROD No");
			table.setColumnHeader("fileType", "File <br>Type</br>");
			table.setColumnHeader("fileName", "File <br>Name</br>");
			table.setColumnHeader("billNo", "Bill <br>No</br>");
			table.setColumnHeader("billDate", "Bill <br>Date</br>");
			table.setColumnHeader("noOfItems", "No of <br>Items</br>");
			table.setColumnHeader("billValue", "Bill </br>Value</br>");
			
		}
		/*table.setColumnHeader("rodNo","ROD No");
		table.setColumnHeader("fileType", "File Type");
		table.setColumnHeader("fileName", "File Name");
		table.setColumnHeader("billNo", "Bill No");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("noOfItems", "No of Items");*/
		
		/*table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		table.setColumnHeader("status", "Status");*/
		table.setEditable(true);
		table.setColumnWidth("rodNo", 210);
		table.setColumnWidth("fileType",140);
		table.setColumnWidth("fileName",120 );
		table.setColumnWidth("billNo",70 );
		table.setColumnWidth("billDate",120 );
		table.setColumnWidth("noOfItems",60);
		table.setColumnWidth("billValue",80 );
		table.setColumnWidth("Select For<br>Bill Entry</br>",80);
		table.setColumnWidth("Status",80);
		
		/*generateColumn();
		
		table.removeGeneratedColumn("Status");
		
		table.addGeneratedColumn("status", new Table.ColumnGenerator() {
		      *//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				
				btnAdd = new Button();
				UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
				
				if(uploadDTO.getStatus())
				{
					btnAdd.setIcon(new ThemeResource("images/yesstatus.png"));
				}
				else
				{
					btnAdd.setIcon(new ThemeResource("images/cross_mark_black.jpg"));
				}
				btnAdd.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				return btnAdd;
			}
		});*/
		
	
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//	table.setEditable(false);
		table.setFooterVisible(true);

	}
	
	public Boolean getBillEntryTableInstance(){
		if(billEntryTableObj != null){
		return billEntryTableObj.getZonalRemarks();
		}
		else
		{
			return false;
		}
	}
	
	public void setTableInfo(List<UploadDocumentDTO> uploadDocsDTOList)
	{
		
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTOList) {
			if(null != uploadDocumentDTO.getFileType() && uploadDocumentDTO.getFileType().getId() != null && ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(uploadDocumentDTO.getFileType().getId())) {
				for (UploadDocumentDTO uploadDocumentDTOForBill : uploadDocsDTOList) {
					if(null != uploadDocumentDTOForBill.getFileType() && uploadDocumentDTOForBill.getFileType().getId() != null && ReferenceTable.getFinalBillKeys().containsKey(uploadDocumentDTOForBill.getFileType().getId())) {
						uploadDocumentDTO.setDmsDocToken(uploadDocumentDTOForBill.getDmsDocToken());
						uploadDocumentDTO.setFileName(uploadDocumentDTOForBill.getFileName());
						break;
					}
				}
				break;
			}
		}
		
		uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
		if(null != uploadDocsDTOList && !uploadDocsDTOList.isEmpty())
		{
			for (UploadDocumentDTO uploadDocumentsDTO : uploadDocsDTOList) {
				if(null != uploadDocumentsDTO.getFileType() && (uploadDocumentsDTO.getFileType().getValue().contains("Bills") || uploadDocumentsDTO.getFileType().getValue().contains("Bill") ))
				 {
					uploadDocsDTO.add(uploadDocumentsDTO);
				 }
			}
		}
		
	}
	
	public Boolean getBillEntryZonalRemarks(){
		
		if(billEntryTableObj != null){
			return billEntryTableObj.getRemarks();
			}
			else
			{
				return false;
			}
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<UploadDocumentDTO> addItem = container.addItem(new UploadDocumentDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	
	public void manageListeners() {

		for (UploadDocumentDTO billEntryDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(billEntryDetailsDTO);

		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*		private static final long serialVersionUID = -2192723245525925990L;
*/
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			UploadDocumentDTO entryDTO = (UploadDocumentDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(entryDTO);
			}
			
			if("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setMaxLength(20);
				field.setData(entryDTO);
				field.setValue(entryDTO.getRodNo());
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("rodNo", field);
				return field;
			} 
			else if ("fileType".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("130px");
				tableRow.put("fileType", box);
				box.setData(entryDTO);
				addFileTypeValues(box);
				if(isFinancial){
					box.setEnabled(false);
				}
				valueChangeLisenerForFileType(box);
				//addCommonValues(box, "source");
				return box;
			}
			else if ("fileName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setMaxLength(20);
				field.setData(entryDTO);
				field.setValue(entryDTO.getFileName());
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("fileName", field);
				return field;
			}
			
			else if("billNo".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("200px");
				field.setWidth("60px");
				field.setNullRepresentation("");
				field.setMaxLength(20);
				field.setData(entryDTO);
				field.setValue(entryDTO.getBillNo());
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billNo", field);
				return field;
			} 
			else if ("billDate".equals(propertyId)) {
				DateField date = new DateField();
				date.setDateFormat("dd/MM/yyyy");
			//	date.setWidth("200px");
				date.setWidth("110px");
				date.setEnabled(false);
				date.setData(entryDTO);
				date.setValue(entryDTO.getBillDate());
				date.setEnabled(false);
				tableRow.put("billDate", date);
				return date;
			}
			else if ("noOfItems".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("200px");
				field.setWidth("50px");
				field.setNullRepresentation("");
				
				field.setMaxLength(6);
				field.setData(entryDTO);
				field.setValue(String.valueOf(entryDTO.getNoOfItems()));
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfItems", field);
				return field;
			}
			else if ("billValue".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("200px");
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);
				valueChangeLisenerForText(field);
				field.setValue(String.valueOf(entryDTO.getBillValue()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billValue", field);
				calculateTotal();
			//	entryDTO.setTotalClaimedAmount(totalClaimedValue);
				return field;
			}
			
			else if ("netAmount".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("200px");
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);
				valueChangeLisenerForNetAmount(field);
				field.setValue(String.valueOf(entryDTO.getBillValue()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("netAmount", field);
				calculateNetAmountTotal();
			//	entryDTO.setTotalClaimedAmount(totalClaimedValue);
				return field;
			}
			
			else
			{
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	
	public void valueChangeLisenerForText(TextField total){
		
		if(null != total)
		{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateTotal();
					
				}
			});
		}
	}
	
	public void valueChangeLisenerForNetAmount(TextField total){
		
		if(null != total)
		{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateNetAmountTotal();
					
				}
			});
		}
	}
	
	public void valueChangeLisenerForFileType(final ComboBox cmbBox){
		
		if(null != cmbBox)
		{
			cmbBox
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					//calculateNetAmountTotal();
					enableFields(cmbBox);
					
				}
			});
		}
	}
	
	public void enableFields(Component cmbBox)
	{		ComboBox cmb = (ComboBox)cmbBox;
		UploadDocumentDTO uploadDocumentDTO = (UploadDocumentDTO)cmb.getData();
		SelectValue selValue = (SelectValue)cmb.getValue();
		Boolean isBill = false;
		Boolean isXray = false;
		if(null != selValue)
		{
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(uploadDocumentDTO);
			//Button button = (Button)table.getItem(uploadDocumentDTO);
			String fileType = selValue.getValue();
			if(null != fileType && !("").equalsIgnoreCase(fileType) && fileType.contains("Bill"))
			{
				isBill = true;
				/**
				 * If its a reconsideration case, then this enable disable will 
				 * be false. but the file type might be bill. So if file type bill
				 * and enable disable is true only, this below value will be set.
				 * */
				if(null != uploadDocumentDTO.getEnableOrDisableBtn() && !uploadDocumentDTO.getEnableOrDisableBtn())
				{
					uploadDocumentDTO.setEnableOrDisableBtn(false);
				}
				else
				{
					uploadDocumentDTO.setEnableOrDisableBtn(true);
				}
				
				
				//fireViewEvent(BillEntryDetailsPresenter.SELECT_BILL_ENTRY_BTN_ENABLE_DISABLE, uploadDocumentDTO);
			}
			else
			{ 
				isBill = false;
				uploadDocumentDTO.setEnableOrDisableBtn(false);

				
				//fireViewEvent(BillEntryDetailsPresenter.SELECT_BILL_ENTRY_BTN_ENABLE_DISABLE, uploadDocumentDTO);

			}
			
			if((SHAConstants.X_RAY_REPORT).equalsIgnoreCase(fileType))
			{
				isXray = true;
			}
			
				if(null != hashMap && !hashMap.isEmpty())
				 {
					 TextField fileName = (TextField)hashMap.get("fileName");
					 
					 enableOrDisable(fileName, isBill,isXray);
	
					 TextField billNo = (TextField)hashMap.get("billNo");
					 enableOrDisable(billNo, isBill,isXray);
					 
					 DateField billDate = (DateField)hashMap.get("billDate");
					 enableOrDisable(billDate, isBill,isXray);
					 
					 TextField noOfItems = (TextField)hashMap.get("noOfItems");
					 enableOrDisable(noOfItems, isBill,isXray);
					 
					 TextField billValue = (TextField)hashMap.get("billValue");
					 enableOrDisable(billValue,isBill,isXray);
					 
					 TextField netAmount = (TextField)hashMap.get("netAmount");
					 enableOrDisable(netAmount , isBill,isXray);
					 
				 }
				enableOrDisableBtn(uploadDocumentDTO);

			}
		
		/*Collection<UploadDocumentDTO> itemIds = (Collection<UploadDocumentDTO>) table.getItemIds();
		 for (UploadDocumentDTO uploadDocumentDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(uploadDocumentDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 
				 TextField fileName = (TextField)hashMap.get("fileName");
				 if(null != fileName)
				 {
					 fileName.setEnabled(true);
				 }
			 }
		 }*/
		 /*List<UploadDocumentDTO> itemIconPropertyId = (List<UploadDocumentDTO>) table.getItemIds();*/
	}
	
	
	private void enableOrDisable(Component comp , Boolean isBill,Boolean isXray)
	{
		if(comp instanceof TextField)
		{
			TextField txtFld = (TextField)comp;
			if(null != txtFld)
			{
				if(isBill){
				txtFld.setEnabled(true);
				}
				else{
					txtFld.setEnabled(false);
				}
				if(isXray)
				{
					txtFld.setValue(null);
				}
			}
		}
		else if(comp instanceof DateField)
		{
			DateField dateFld = (DateField)comp;
			if(null != dateFld)
			{
				if(isBill){
					dateFld.setEnabled(true);
				}
				else{
					dateFld.setEnabled(false);
				}
				if(isXray)
				{
					dateFld.setValue(null);
				}
				    
			}
		}else if(comp instanceof Button){
			Button billbtn = (Button)comp;
			if(billbtn != null){
				if(isBill){
					billbtn.setEnabled(true);
				}else{
					billbtn.setEnabled(false);
				}
			}
		}
		
	}
	
	 public void calculateTotal()
	 {
		 List<UploadDocumentDTO> itemIconPropertyId = (List<UploadDocumentDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (UploadDocumentDTO uploadDocDTO : itemIconPropertyId) {
				 if(null != uploadDocDTO.getBillValue())
				 {
					 total += uploadDocDTO.getBillValue();
				 }
		}
		 table.setColumnFooter("billValue", String.valueOf(total));
		 totalClaimedValue = total;
		 if(null == totalClaimedValue)
		 {
			 totalClaimedValue = 0d;
		 }

		 if(null == presenterString && ("").equalsIgnoreCase(presenterString))
		 claimedAmtField.setValue(String.valueOf(totalClaimedValue));
		// bean.setTotalClaimedAmount(totalClaimedValue);
	// this.bean.setTotalClaimedAmount(totalClaimedValue);
/*=======
		 if(!(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) ||
					(SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
				  ))
		 claimedAmtField.setValue(String.valueOf(totalClaimedValue));*/
		// this.bean.setTotalClaimedAmount(totalClaimedValue);

		 
		 }
	 }
	 
	 
	

	 public void calculateNetAmountTotal()
	 {
		 List<UploadDocumentDTO> itemIconPropertyId = (List<UploadDocumentDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (UploadDocumentDTO uploadDocDTO : itemIconPropertyId) {
				 if(null != uploadDocDTO.getNetAmount())
				 {
					 total += uploadDocDTO.getNetAmount();
				 }
		}
		 table.setColumnFooter("netAmount", String.valueOf(total));
		 totalNetAmountValue = total;
		 if(null == totalNetAmountValue)
		 {
			 totalNetAmountValue = 0d;
		 }
		 claimedAmtField.setValue(String.valueOf(totalNetAmountValue));
		 // this.bean.setTotalClaimedAmount(totalClaimedValue);
		 
		 }
	 }
	 
	 public Double getTotalClaimedAmout()
	 {
		 return totalClaimedValue;
	 }

	 public Double getTotalClaimedAmount()
	 {
		 return totalClaimedValue;
	 }
	
	public void addFileTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("fileType");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	 public void addBeanToList(UploadDocumentDTO uploadDocumentsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 
		 if(null != uploadDocumentsDTO.getFileType() && (uploadDocumentsDTO.getFileType().getValue().contains("Bills") || uploadDocumentsDTO.getFileType().getValue().contains("Bill") 
				 || uploadDocumentsDTO.getFileType().getValue().contains("FA")))
		 {
			 container.addItem(uploadDocumentsDTO);
		 }
		 
		 updateStatusOfBills(uploadDocumentsDTO);
		 
		 
//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	 
	 private void updateStatusOfBills(UploadDocumentDTO uploadDTO)
	 {
		 int i = 0;
		 txtStatusOfBills.setReadOnly(false);
		 if(null != uploadDocsDTO && !uploadDocsDTO.isEmpty())
		 {
			 int iSize = uploadDocsDTO.size();
			 for (UploadDocumentDTO uploadDto : uploadDocsDTO) {
				if(uploadDto.getStatus())
				{
					i++;
				}
			}
			 String txtMsg = i +" of" + " "+ iSize +"  records completed";
				txtStatusOfBills.setValue(txtMsg);
				txtStatusOfBills.setReadOnly(true);
		 }
	 }
	
	
	 public List<UploadDocumentDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadDocumentDTO> itemIds = (List<UploadDocumentDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	 public void setupCategoryValues(
				BeanItemContainer<SelectValue> categoryValues) {
			
			billEntryTableObj.setUpCategoryValues(categoryValues);
		 
			
		}

	 
	 
	 public CheckBox updateStatus(CheckBox chkBox)
	 {
		/* while(true)
		 {
			 
			 if(this.billEntryFinalStatus)
			 {
				 chkBox.setValue(true);
				 break; 
			 }
		 }*/
		 return chkBox;
	 }
	 
	/*public void setBillEntryFinalStatus(Boolean status) {
		this.billEntryFinalStatus = status;
	}
*/
	 public void updateTable(List<UploadDocumentDTO> dto)
	 {
		 
		 //container.removeItem(dto);
		 //addBeanToList(dto);
		 container.removeAllItems();
		 
		
		 for (UploadDocumentDTO uploadDocumentDTO : dto) {
			/* if(null != uploadDocumentDTO.getStatus() & uploadDocumentDTO.getStatus())
			 {
				 billEntryStatus = true;
			 }
			 else
			 {
				 billEntryStatus = false;
			 }*/
			 addBeanToList(uploadDocumentDTO);
		}
		
		 
	 }
		
	 public String getHospitalizationAmount() {
//		 if(billEntryTableObj != null) {
//			 return billEntryTableObj.getHospitalizationValues();
//		 }
		 Integer hospitalizationAmount = 0;
		 if(table != null) {
			 List<UploadDocumentDTO> billEntryDetails = (List<UploadDocumentDTO>) table.getItemIds();
			 if(billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO : billEntryDetails) {
						List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
							if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(8l)) {
								//hospitalizationAmount += billEntryDetailsDTO.getItemValue() != null ? billEntryDetailsDTO.getItemValue().intValue() : 0;
								hospitalizationAmount += billEntryDetailsDTO.getNetPayableAmount() != null ? billEntryDetailsDTO.getNetPayableAmount().intValue() : 0;
							}
						}
					}
			 }
		
			 
		}
		 return  String.valueOf(hospitalizationAmount) ;
	 }

	public void setIrdaLevel2Values(
			BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue value) {
		billEntryTableObj.setIrdaLevel2Values(selectValueContainer,cmb,value);
	}

	public void setIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		billEntryTableObj.setIrdaLevel3Values(selectValueContainer,cmb,value);
		
	}
	
	public void removeAllItems()
	{
		table.removeAllItems();
		//table.removeItem(\);
	}
	
	public void addItem(UploadDocumentDTO uploadDTO)
	{
		table.addItem(uploadDTO);
		
	}

	public void generateColumn()
	{

		table.removeGeneratedColumn("Select For Bill Entry");
		table.addGeneratedColumn("Select For<br>Bill Entry</br>", new Table.ColumnGenerator() {
		     /**
			 * 
			 */
			//private static final long serialVersionUID = 1L;
			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	//final Button button = new Button("Bill Entry");
				final Button button = new Button("Bill Entry");
				
				//button = new Button("Bill Entry");
				UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
				if(uploadDocDTO.getEnableOrDisableBtn())
				{
					button.setEnabled(true);
				}
				else
				{
					button.setEnabled(false);
				}
				//final Button button = new Button();
				 button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					//private static final long serialVersionUID = 1L;
					public void buttonClick(ClickEvent event) {					
						UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
//						button.setIcon(new ThemeResource("images/BillEntry.png"));
						//if(!(uploadDocDTO.getFileTypeValue().contains("Bill")))
						if(!(null != uploadDocDTO.getFileType() && null != uploadDocDTO.getFileType().getValue() && uploadDocDTO.getFileType().getValue().contains("Bill")))
						{
							//button.setEnabled(false);
						}
						else
						{/*
								popup = new com.vaadin.ui.Window();
								popup.setCaption("Bill Entry");
								popup.setWidth("75%");
								//popup.setWidth("100%");
								popup.setHeight("85%");
								
								popup.setSizeFull();
								billEntryTableObj = billEntryTableInstance.get();
								
								if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
								{
									billEntryTableObj.initPresenter(presenterString, addmissionDateFld, dischargeDateFld);
								}
								else if((SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString))
								{
									billEntryTableObj.initPresenter(presenterString, uploadDocDTO.getiNoOfEmptyRows());
								}
								else
								{
									billEntryTableObj.initPresenter(presenterString);
								}
								 
								Boolean isRemarksRequired = false;
								Boolean isZonalReview = false;
								Boolean isBilling = false;
								Boolean isFinancial = false;
									if (presenterString != null
											&& !presenterString
													.equalsIgnoreCase("")) {
										isRemarksRequired = true;
										if (presenterString
												.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
											isZonalReview = true;
											popup.setCaption("Bill Review");
										} else if (presenterString
												.equalsIgnoreCase(SHAConstants.BILLING)) {
											isBilling = true;
											popup.setCaption("Bill Review");
										} else if (presenterString
												.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
											isFinancial = true;
											popup.setCaption("Bill Review");
										} else if (presenterString
												.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
											popup.setCaption("Bill Review");
										} else if (presenterString
												.equalsIgnoreCase(SHAConstants.ADD_ADDITIONAL_DOCS)) {
											isRemarksRequired = false;
										} else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
											isRemarksRequired = false;
										}
									}
								
						
							billEntryTableObj.init((UploadDocumentDTO)itemId, referenceData, popup, isRemarksRequired, isZonalReview, isBilling, isFinancial);
							//billStatus = ((UploadDocumentDTO)itemId).getStatus();
							popup.setContent(billEntryTableObj);
							popup.setClosable(true);
							
							popup.center();
							popup.setResizable(true);
							popup.setWidth("90%");
							popup.addCloseListener(new Window.CloseListener() {
								*//**
								 * 
								 *//*
								private static final long serialVersionUID = 1L;
	
								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});
	
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						*/
							if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.MA_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(PAHealthFinancialReviewPagePresenter.FA_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if(SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)){
								fireViewEvent(DetailsPresenter.ADD_ADDITIONAL_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(BillEntryDetailsPresenter.LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.PA_BILL_ENTRY).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(PABillEntryDetailsPresenter.LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							else if((SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
							//loadBillEntryValues(uploadDocDTO);
						
						}
					}
			    		//UI.getCurrent().addWindow(viewDetailUI); 
			    });
		    	//button.setHeight("50px");
				//button.setIcon(new ThemeResource("images/BillEntry.png"));
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	button.setWidth("70px");
		    	//Map buttonMap = new HashMap();
		    	buttonMap.put(uploadDocDTO.getSeqNo(), button);
		    	//uploadDocDTO.setButtonMap(buttonMap);
		    	//button.setEnabled(true);
		    	return button;
		      }
		});
	}
	
	
	public void loadBillEntryValues(UploadDocumentDTO uploadDocDTO)
	{

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Bill Entry");
		popup.setWidth("75%");
		//popup.setWidth("100%");
		popup.setHeight("85%");
		
		popup.setSizeFull();
		billEntryTableObj = billEntryTableInstance.get();
		
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
		{
			billEntryTableObj.initPresenter(presenterString, addmissionDateFld, dischargeDateFld);
		}
		else if((SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString))
		{
			billEntryTableObj.initPresenter(presenterString, uploadDocDTO.getiNoOfEmptyRows());
		}
		else if((SHAConstants.PA_BILL_ENTRY).equalsIgnoreCase(presenterString))
		{
			billEntryTableObj.initPresenter(presenterString, uploadDocDTO.getiNoOfEmptyRows());
		}
		else
		{
			billEntryTableObj.initPresenter(presenterString);
		}
		 
		Boolean isRemarksRequired = false;
		Boolean isZonalReview = false;
		Boolean isBilling = false;
		Boolean isFinancial = false;
			if (presenterString != null
					&& !presenterString
							.equalsIgnoreCase("")) {
				isRemarksRequired = true;
				if (presenterString
						.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
					isZonalReview = true;
					popup.setCaption("Bill Review");
				} else if (presenterString
						.equalsIgnoreCase(SHAConstants.BILLING)) {
					isBilling = true;
					popup.setCaption("Bill Review");
				} else if (presenterString
						.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
					isFinancial = true;
					popup.setCaption("Bill Review");
				} else if (presenterString
						.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
					popup.setCaption("Bill Review");
				} else if (presenterString
						.equalsIgnoreCase(SHAConstants.ADD_ADDITIONAL_DOCS)) {
					isRemarksRequired = false;
				} else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
					isRemarksRequired = false;
				}
				else if(presenterString.equalsIgnoreCase(SHAConstants.PA_BILL_ENTRY)){
					isRemarksRequired = false;}
			}
		

			billEntryTableObj.init((UploadDocumentDTO)uploadDocDTO, referenceData, popup, isRemarksRequired, isZonalReview, isBilling, isFinancial);
	//billStatus = ((UploadDocumentDTO)itemId).getStatus();
			popup.setContent(billEntryTableObj);
			popup.setClosable(true);
	
			popup.center();
			popup.setResizable(true);
		popup.setWidth("90%");
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
	
	
	public void enableOrDisableBtn(UploadDocumentDTO itemId)
	{
		//button = new Button("Bill Entry");
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
//		Button button = (Button)uploadDocDTO.getButtonMap().get(uploadDocDTO.getSeqNo());
		Button button = (Button) buttonMap.get(uploadDocDTO.getSeqNo());
		if(uploadDocDTO.getEnableOrDisableBtn())
		{
			if(null != button)
			button.setEnabled(true);
		}
		else
		{
			if(null != button)
			button.setEnabled(false);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setBillClassificationsDynamically(Boolean isNeededPre, Boolean isNeededPost, Boolean isNeededHosp) {
		BeanItemContainer<SelectValue> object = (BeanItemContainer<SelectValue>) referenceData.get(SHAConstants.ALL_BILL_CLASSIFICATIONS);
		List<SelectValue> itemIds = object.getItemIds();
		BeanItemContainer<SelectValue> newValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue> selectedValues = new ArrayList<SelectValue>();
		for (SelectValue selectValue : itemIds) {
			if(isNeededPre & selectValue.getId().equals(ReferenceTable.PRE_HOSPITALIZATION) ) {
				selectedValues.add(selectValue);
			}
			if(isNeededPost & selectValue.getId().equals(ReferenceTable.POST_HOSPITALIZATION) ) {
				selectedValues.add(selectValue);
			}
			if(isNeededHosp & selectValue.getId().equals(ReferenceTable.HOSPITALIZATION) ) {
				selectedValues.add(selectValue);
			}
		}
			
		newValues.addAll(selectedValues);
		referenceData.put("billClassification", newValues);
	}
}

