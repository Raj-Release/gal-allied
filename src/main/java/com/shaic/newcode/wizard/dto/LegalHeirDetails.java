package com.shaic.newcode.wizard.dto;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;



public class LegalHeirDetails extends ViewComponent {

	private Table table;
	
	private BeanItemContainer<SelectValue> relationship;
	
	private TextField heirName;
	
	private TextField sharePercentage;

	private TextField address;
	
	private TextField accountType;
	
	private TextField beneficieryname;
	
	private TextField accountNumber;
	
//	private TextField IFSCcode;
	
	private TextField documentType;
	
	private LegalHeirDTO legalBean;
	
	private Validator validator;
	
	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	BeanItemContainer<LegalHeirDTO> data;
	
	private List<LegalHeirDTO> deletedList;
	
	BeanItemContainer<SelectValue> relationshipvalues = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private Map<LegalHeirDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<LegalHeirDTO, HashMap<String, AbstractField<?>>>();
	 
	private File file;
	
	private PreauthDTO preauthBean;
	
	private String screenName;
	
	private String presenterString;
	 
	int i=0;
	int j=1;
	
	private final static Object VIEW_COLUM_HEADER[] = new Object[] {"slNo","heirName","relationship","sharePercentage","address","pincode"};
	
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	public void init(PreauthDTO bean){
		this.legalBean = bean.getLegalHeirDto();
		data = new BeanItemContainer<LegalHeirDTO>(LegalHeirDTO.class);
		this.preauthBean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				LegalHeirDTO legalHeirdto = new LegalHeirDTO();
				legalHeirdto.setDeleteLegalHeir(SHAConstants.N_FLAG);
				legalHeirdto.setPaymentMode(null);
				i = getValues().size();
				legalHeirdto.setSlNo(i+1);
				
				if(i==0) {
				BeanItem<LegalHeirDTO> addItem = data
						.addItem(legalHeirdto);
				i++;
				}else if(i>0 /*&& preauthBean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && 
						preauthBean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_FOR_LEGAL_HEIR)*/){
//					legalHeirdto.setSlNo(getValues().size()+1);
					
					BeanItem<LegalHeirDTO> addItem = data
							.addItem(legalHeirdto);
					
				}
			}
		});
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		btnAdd.setEnabled(true);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
        initTable();
		table.setWidth("100%");
		table.setPageLength(4);
//		addListener();
		layout.addComponent(table);
		setCompositionRoot(layout);
	}
	
	public void initTable() {

		// Create a data source and bind it to a table
		table = new Table("Legal Heir Details", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(4);

		deletedList = new ArrayList<LegalHeirDTO>();
		
		// Added for table height..
		table.setHeight("160px");
		table.setVisibleColumns(new Object[] {"slNo","heirName","relationship","sharePercentage","address","pincode","docType","paymentMode","payableAt","accountType","accountPreference","beneficiaryName",
				"accountNo","ifscCode"});
		
		table.removeGeneratedColumn("paymentMode");
		table.addGeneratedColumn("paymentMode", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				OptionGroup paymentOption = new OptionGroup();
				LegalHeirDTO dto = (LegalHeirDTO)itemId;
				paymentOption.setData(dto);
				addPayModeValues(paymentOption);
				paymentOption.setValue(dto.getPaymentMode() != null && dto.getPaymentMode() ? dto.getPaymentMode() : false);
				paymentOption.addValueChangeListener(paymentModeListener());
				return paymentOption;
				
			}
		});
		
		table.removeGeneratedColumn("docType");
		table.addGeneratedColumn("docType", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				GComboBox docTypeCmb = new GComboBox();
				LegalHeirDTO dto = (LegalHeirDTO)itemId;
				docTypeCmb.setData(dto);
				addDocTypeValues(docTypeCmb);
				return docTypeCmb;
				
			}
		});
		
		
		generateDynamicColumns();
		
		/*table.removeGeneratedColumn("ifscCode1");
		table.addGeneratedColumn("ifscCode1", new Table.ColumnGenerator() {
					
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnIFCSSearch = new Button();
				LegalHeirDTO curRow = (LegalHeirDTO)itemId;
				btnIFCSSearch.setData(curRow);
				btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
				btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
				
				if(curRow.getPaymentMode() != null && curRow.getPaymentMode()) {
					btnIFCSSearch.setEnabled(false);
				}
				else{
					btnIFCSSearch.setEnabled(true);
				}
				
				if(curRow.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(curRow.getPaymentModeId())) {
					btnIFCSSearch.setEnabled(false);
				}
				else{
					btnIFCSSearch.setEnabled(true);
				}	

				btnIFCSSearch.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						Window popup = new com.vaadin.ui.Window();
						viewSearchCriteriaWindow.setWindowObject(popup);
						viewSearchCriteriaWindow.setPresenterString(presenterString);
						viewSearchCriteriaWindow.initView();
						viewSearchCriteriaWindow.setPreauthDto(preauthBean);
						viewSearchCriteriaWindow.setLegalHeirDto(curRow);
						popup.setWidth("75%");
						popup.setHeight("90%");
						popup.setContent(viewSearchCriteriaWindow);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
						
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
						btnIFCSSearch.setEnabled(true);
					}
				});
				
				return btnIFCSSearch;		
			}
		});
			
		table.removeGeneratedColumn("Upload Document");
		table.addGeneratedColumn("Upload Document", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				upload = new Upload("Choose File",(Receiver) table.getParent());
				upload.addSucceededListener((SucceededListener) table.getParent());
				return upload;
				com.vaadin.v7.ui.Upload upload  = new Upload();
				  upload.setReceiver(new Receiver1());
				  upload.setData((LegalHeirDTO)itemId);
					upload.addSucceededListener(new SucceededListener() {
						
						@Override
						public void uploadSucceeded(SucceededEvent event) {
						
							System.out.println("File uploaded" + event.getFilename());
							
							try{
								
								
								byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

								String fileName = event.getFilename();
								LegalHeirDTO legalHeirFileName = (LegalHeirDTO)upload.getData();
								legalHeirFileName.setFileName(fileName);
								if (null != fileAsbyteArray) {
									//file gets uploaded in data directory when code comes here.
									if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
											event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
									{
										File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());

										fileName = convertedFile.getName();
										fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
									}
						
										HashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
										Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
										//TO read file after load
										if (flagUploadSuccess.booleanValue())
										{
											String token = "" + uploadStatus.get("fileKey");
											Long docKey = Long.parseLong(token);
											legalBean.setDocumentToken(docKey);
											LegalHeirDTO docTokenIncludedRow = (LegalHeirDTO)upload.getData();
											docTokenIncludedRow.setDocumentToken(docKey);
											SelectValue docType = new SelectValue();
											if(SHAConstants.PA_LOB.equals(screenName)) {
												docType.setId(ReferenceTable.PA_LEGAL_HEIR_DOC_KEY);
											}
											else {
												docType.setId(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY);
											}
											docType.setValue(SHAConstants.LEGAL_HEIR_CERT);
											docTokenIncludedRow.setDocType(docType);
											
											//String fileName = event.getFilename();
//										    uploadStatus = null;
//										    submitHandler.submit(bean);
//										    thisObj.close();
										}
								}
								
							}catch(Exception e){
								e.printStackTrace();
							}
						
							
						}
					});
					upload.setButtonCaption(null);
					upload.setCaption("Choose File");
					
					Button btnUpload = new Button("Upload Document");
					btnUpload.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							upload.submitUpload();
						}
					});
					
					HorizontalLayout uploadLayout = new HorizontalLayout(upload, btnUpload);
					uploadLayout.setSpacing(true);
					
//					return upload;
					
					return uploadLayout;
			}
		});
		
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						LegalHeirDTO currentItemId = (LegalHeirDTO) event.getButton().getData();
						currentItemId.setDeleteLegalHeir(SHAConstants.YES_FLAG);
						table.removeItem(currentItemId);
						if(currentItemId.getLegalHeirKey() != null) {
							deletedList.add(currentItemId);
						}	
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		});*/
		
		table.setColumnHeader("slNo", "Sl No");
		table.setColumnHeader("heirName", "Heir Name");
		table.setColumnHeader("relationship", "Relationship");
		table.setColumnHeader("sharePercentage", "Share %");
		table.setColumnHeader("address", "Address");
		table.setColumnHeader("pincode", "Pincode");
		table.setColumnHeader("paymentMode", "Payment Mode");
		table.setColumnHeader("payableAt", "Payable At");
		table.setColumnHeader("accountType", "Account Type");
		table.setColumnHeader("accountPreference", "Account Preference");
		table.setColumnHeader("beneficiaryName", "Account Holder Name");
		table.setColumnHeader("accountNo", "Account Number");
		table.setColumnHeader("ifscCode", "IFSC Code");
		table.setColumnHeader("ifscCode1", "");
		table.setColumnHeader("docType", "Document Type");
		table.setColumnHeader("documentToken", "");
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			LegalHeirDTO dto = (LegalHeirDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(dto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(dto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(dto);
			}
			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("40px");
				field.setEnabled(true);
				field.setData(dto);
				int val = j++;
				String serialNumber = String.valueOf(val);
				field.setValue(serialNumber);
						/*CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);*/
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("slNo", field);
				return field;
			}else if ("heirName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				tableRow.put("heirName", field);
				/*final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);*/
				return field;
			}else if ("relationship".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setData(dto);
				addRelationshipValues(field);
				tableRow.put("relationship", field);
				return field;
			}else if ("sharePercentage".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setData(dto);
				tableRow.put("sharePercentage", field);
				return field;
			}else if ("address".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setRows(3);
				field.setNullRepresentation("");
				field.setData(dto);
				tableRow.put("address", field);
				return field;
			}else if ("pincode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				setPincodeToDto(field);
				tableRow.put("pincode", field);
				return field;
			}else if ("accountType".equals(propertyId)) {
				GComboBox field = new GComboBox();
				addAccTypeValues(field);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						||(dto.getPaymentMode() == null || (dto.getPaymentMode() != null && dto.getPaymentMode()))){
					field.setEnabled(false);
					field.setValue(null);
				}
				else {
					field.setEnabled(true);
					field.setValue(dto.getAccountType());
				}
				
				tableRow.put("accountType", field);
				return field;
			}else if ("accountPreference".equals(propertyId)) {
				GComboBox field = new GComboBox();
				addAccPrefValues(field);
				field.setData(dto);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						|| (dto.getPaymentMode() != null && dto.getPaymentMode())){
					field.setEnabled(false);
					field.setValue(null);
				}else {
					field.setValue(dto.getAccountPreference());
				}
				
				tableRow.put("accountPreference", field);
				addAccPrefListener(field);
				return field;
			}else if ("beneficiaryName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						|| (dto.getPaymentMode() != null && dto.getPaymentMode())){
					field.setEnabled(true);
					field.setValue(dto.getBeneficiaryName());
				}
				else {
					field.setEnabled(false);
					field.setValue("");
				}
					
				
				tableRow.put("beneficiaryName", field);
				return field;
			}else if ("payableAt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						|| (dto.getPaymentMode() != null && dto.getPaymentMode())){
					field.setEnabled(true);
					field.setValue(dto.getPayableAt());
				}
				else {
					field.setEnabled(false);
					field.setValue("");
				}
				
				tableRow.put("payableAt", field);
				return field;
			}else if ("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				field.setEnabled(false);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						|| (dto.getPaymentMode() != null && dto.getPaymentMode())){
					field.setEnabled(false);
					field.setValue("");
				}
				
				tableRow.put("ifscCode", field);
				return field;
			}else if ("accountNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(dto);
				
				if((dto.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(dto.getPaymentModeId()))
						|| (dto.getPaymentMode() != null && dto.getPaymentMode())){
					field.setEnabled(false);
					field.setValue("");
				}
				
				tableRow.put("accountNo", field);
				return field;
			}else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField) {
					field.setWidth("100%");
					((TextField)field).setNullRepresentation("");
					((TextField)field).setData(dto);
				}	
				return field;
			}
			
			
		}
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public List<LegalHeirDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<LegalHeirDTO> itemIds = (List<LegalHeirDTO>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<LegalHeirDTO>();
		}
		
		return itemIds;
	}
	
	public boolean isValid() {
		boolean valid = true;
	
		if(this.table.getItemIds() != null && !this.table.getItemIds().isEmpty()) {
			List<LegalHeirDTO> itemIds = (List<LegalHeirDTO>) this.table.getItemIds();
			Double totalshare = 0d;
			for (LegalHeirDTO legalHeirDTO : itemIds) {
				if(legalHeirDTO.getDeleteLegalHeir() == null || (legalHeirDTO.getDeleteLegalHeir() != null && SHAConstants.N_FLAG.equalsIgnoreCase(legalHeirDTO.getDeleteLegalHeir()))){
					if(legalHeirDTO.getHeirName() == null || legalHeirDTO.getHeirName().isEmpty()
							||legalHeirDTO.getAddress() == null || legalHeirDTO.getAddress().isEmpty()
							||legalHeirDTO.getPincode() == null || legalHeirDTO.getPincode().isEmpty()
							||legalHeirDTO.getSharePercentage() == null || legalHeirDTO.getSharePercentage().intValue() == 0){
						valid = false;
					}
					totalshare += legalHeirDTO.getSharePercentage();
				}
			}
			if(totalshare != 100) {
				valid = false;
			}
		}
		else {
			valid = false;
		}
		
		return valid;
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<LegalHeirDTO> itemIds = (Collection<LegalHeirDTO>) table.getItemIds();
		
		int i = 0;
		 for (LegalHeirDTO docCheckListDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(docCheckListDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("slNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i));
					 itemNoFld.setReadOnly(true);
					 //itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}
	
	public void addBeanToList(List<LegalHeirDTO> legalHeirDTOList) {
		if(legalHeirDTOList != null) {
			data.removeAllItems();
			data.addAll(legalHeirDTOList);
		}
	}
	
	public void addRelationshipValues(GComboBox comboBox){
		
		BeanItemContainer<SelectValue> relationshipValues = (BeanItemContainer<SelectValue>) referenceData
				.get("relationship");
		comboBox.setContainerDataSource(relationshipValues);
	}
	
	public void addDocTypeValues(GComboBox comboBox){
		
		BeanItemContainer<SelectValue> relationshipValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		relationshipValues.addBean(new SelectValue(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY, "Legal Heir Certificate"));
		comboBox.setContainerDataSource(relationshipValues);
		comboBox.setValue(relationshipValues.getIdByIndex(0));
	}
	
	public void addAccTypeValues(GComboBox comboBox){
		
		BeanItemContainer<SelectValue> relationshipValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		relationshipValues.addBean(new SelectValue(1l, "SAVINGS"));
		relationshipValues.addBean(new SelectValue(2l, "CURRENT"));
		
		comboBox.setContainerDataSource(relationshipValues);
	}
	
	public void addAccPrefValues(GComboBox comboBox){
		
		BeanItemContainer<SelectValue> relationshipValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		relationshipValues.addBean(new SelectValue(1l, "PRIMARY"));
		relationshipValues.addBean(new SelectValue(2l, "SECONDARY"));
		
		comboBox.setContainerDataSource(relationshipValues);
	}

	public void addAccPrefListener(GComboBox cmbAccPref){
		
		cmbAccPref.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				LegalHeirDTO rowDto = (LegalHeirDTO)cmbAccPref.getData();
				GComboBox prefCmb = (GComboBox)event.getProperty();
				SelectValue accPrefSelect = prefCmb.getValue() != null ? (SelectValue) prefCmb.getValue() : null;
				if(accPrefSelect != null) {
					rowDto.setAccountPreference(accPrefSelect);
				}
				
			}
		});
	}
	public void setPincodeToDto(TextField field){
		
		field.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				LegalHeirDTO rowDto = (LegalHeirDTO)field.getData();
				TextField txtPincode = (TextField)event.getProperty();

				if(txtPincode.getValue() != null) {
					rowDto.setPincode(txtPincode.getValue());
				}
				
			}
		});
	}
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public List<LegalHeirDTO> getDeletedList() {
		return deletedList;
	}

	public Button getBtnAdd() {
		return btnAdd;
	}
	
	public void deleteRows() {
		
		List<LegalHeirDTO> itemIds = (List<LegalHeirDTO>) this.table.getItemIds();
		if(!itemIds.isEmpty()) {
			
			table.removeAllItems();
			/*for (LegalHeirDTO currentItemId : itemIds) {
				currentItemId.setDeleteLegalHeir(SHAConstants.YES_FLAG);
				table.removeItem(currentItemId);
				deletedList.add(currentItemId);
			}*/
		}
	}
	
	public void setBankDetails(ViewSearchCriteriaTableDTO dto, LegalHeirDTO currentRowDto){
		
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(currentRowDto);
		
		if(null != hashMap && !hashMap.isEmpty())
		 {
			 TextField itemIfscFld = (TextField)hashMap.get("ifscCode");
			 itemIfscFld.setValue(dto.getIfscCode());	
			 currentRowDto.setAccoutDetailsDto(dto);
			 List<LegalHeirDTO> itemIds = new ArrayList<LegalHeirDTO>();
			 itemIds.addAll((List<LegalHeirDTO>)this.table.getItemIds());
			 table.removeAllItems();
			 addBeanToList(itemIds);
		 }		
	}
	
	public void setIFSCView(ViewSearchCriteriaViewImpl viewSearchCriteriaWindow) {
		this.viewSearchCriteriaWindow = viewSearchCriteriaWindow; 
		
		
	}
	
	public void addPayModeValues(OptionGroup paymentOption){
		Collection<Boolean> values = new ArrayList<Boolean>(2);
		values.add(true);
		values.add(false);
		paymentOption.addItems(values);
		paymentOption.setItemCaption(true, "Cheque/DD");
		paymentOption.setItemCaption(false, "Bank Transfer");
		paymentOption.setStyleName("vertical");		
	}
	
	public ValueChangeListener paymentModeListener() {
		ValueChangeListener valChangeListener = new Property.ValueChangeListener() {
		
		private static final long serialVersionUID = -1774887765294036092L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			//TODO
			Boolean value = (Boolean) event.getProperty().getValue();
			OptionGroup mode = (OptionGroup)event.getProperty();
			
			LegalHeirDTO legalHeirDto = (LegalHeirDTO)mode.getData();
			
			if(value)
			{
				legalHeirDto.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
				legalHeirDto.setPaymentMode(true);
				enableChequePaymentFields(legalHeirDto);
			}
			else{
				legalHeirDto.setPaymentModeId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				legalHeirDto.setPaymentMode(false);
				enableBankTransferPaymentFields(legalHeirDto);
			}	
					
		}
	};
		return valChangeListener;
	}

	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void setViewColumnDetails() {
		table.setVisibleColumns(VIEW_COLUM_HEADER);
	}
	
	public void enableChequePaymentFields(LegalHeirDTO legalHeirDto) {
		
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(legalHeirDto);
		
		if(null != hashMap && !hashMap.isEmpty())
		 {
			TextField payAtTextField = (TextField)hashMap.get("payableAt");
			payAtTextField.setEnabled(true);
			
			TextField itemIfscFld = (TextField)hashMap.get("ifscCode");
			itemIfscFld.setValue("");
			itemIfscFld.setData(legalHeirDto);
			
			geneateIFSCColumn(legalHeirDto);
			
			GComboBox prefTextField = (GComboBox)hashMap.get("accountPreference");
			prefTextField.setEnabled(false);
			prefTextField.setValue(null);

			GComboBox accTypeCmbField = (GComboBox)hashMap.get("accountType");
			accTypeCmbField.setEnabled(false);
			accTypeCmbField.setValue(null);
			
			TextField accNumTextField = (TextField)hashMap.get("accountNo");
			accNumTextField.setEnabled(false);
			accNumTextField.setValue("");
		 }	 
	}
	
	public void enableBankTransferPaymentFields(LegalHeirDTO legalHeirDto) {
		
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(legalHeirDto);
		
		if(null != hashMap && !hashMap.isEmpty())
		 {
			TextField payAtTextField = (TextField)hashMap.get("payableAt");
			payAtTextField.setValue("");
			payAtTextField.setEnabled(false);
			
			TextField itemIfscFld = (TextField)hashMap.get("ifscCode");
			itemIfscFld.setData(legalHeirDto);
			itemIfscFld.setValue(legalHeirDto.getIfscCode());
			
			geneateIFSCColumn(legalHeirDto);
			
			GComboBox prefTextField = (GComboBox)hashMap.get("accountPreference");
			prefTextField.setValue(legalHeirDto.getAccountPreference());
			prefTextField.setEnabled(true);

			GComboBox accTypeCmbField = (GComboBox)hashMap.get("accountType");
			accTypeCmbField.setValue(legalHeirDto.getAccountType());
			accTypeCmbField.setEnabled(true);
			
			TextField accNumTextField = (TextField)hashMap.get("accountNo");
			accNumTextField.setValue(legalHeirDto.getAccountNo());
			accNumTextField.setEnabled(true);
		 }	 
	}
	
	public void geneateIFSCColumn(LegalHeirDTO legalHeirDto) {
		
		String ifscCode = "";
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(legalHeirDto);
		
		if(null != hashMap && !hashMap.isEmpty())
		 {
			
			TextField itemIfscFld = (TextField)hashMap.get("ifscCode");
			ifscCode = itemIfscFld.getValue();
		 }	
		
		generateDynamicColumns();
		/*table.removeGeneratedColumn("ifscCode1");
		table.addGeneratedColumn("ifscCode1", new Table.ColumnGenerator() {
					
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnIFCSSearch = new Button();
				LegalHeirDTO curRowDto = (LegalHeirDTO)itemId;
				btnIFCSSearch.setData(curRowDto);
				btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
				btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
				
				if(!curRowDto.getPaymentMode()) {
					btnIFCSSearch.setEnabled(true);
				}
				else {
					btnIFCSSearch.setEnabled(false);
				}

				btnIFCSSearch.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						Window popup = new com.vaadin.ui.Window();
						viewSearchCriteriaWindow.setWindowObject(popup);
						viewSearchCriteriaWindow.setPresenterString(presenterString);
						viewSearchCriteriaWindow.initView();
						viewSearchCriteriaWindow.setPreauthDto(preauthBean);
						viewSearchCriteriaWindow.setLegalHeirDto(curRowDto);
						popup.setWidth("75%");
						popup.setHeight("90%");
						popup.setContent(viewSearchCriteriaWindow);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
						
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
						
//						if(ifscCode.equalsIgnoreCase(curRowDto.getIfscCode())) {
							
							if(!curRowDto.getPaymentMode()) {
								btnIFCSSearch.setEnabled(true);
							}
							else {
								btnIFCSSearch.setEnabled(false);
							}
//						}		
					}
				});
				
				return btnIFCSSearch;		
			}
		});*/
	}
	
	
	public void generateDynamicColumns() {

		table.removeGeneratedColumn("ifscCode1");
		table.addGeneratedColumn("ifscCode1", new Table.ColumnGenerator() {
					
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnIFCSSearch = new Button();
				LegalHeirDTO curRow = (LegalHeirDTO)itemId;
				btnIFCSSearch.setData(curRow);
				btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
				btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
				
				if(curRow.getPaymentMode() != null && curRow.getPaymentMode()) {
					btnIFCSSearch.setEnabled(false);
				}
				else{
					btnIFCSSearch.setEnabled(true);
				}
				
				if(curRow.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(curRow.getPaymentModeId())) {
					btnIFCSSearch.setEnabled(false);
				}
				else{
					btnIFCSSearch.setEnabled(true);
				}	

				btnIFCSSearch.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						Window popup = new com.vaadin.ui.Window();
						viewSearchCriteriaWindow.setWindowObject(popup);
						viewSearchCriteriaWindow.setPresenterString(presenterString);
						viewSearchCriteriaWindow.initView();
						viewSearchCriteriaWindow.setPreauthDto(preauthBean);
						viewSearchCriteriaWindow.setLegalHeirDto(curRow);
						popup.setWidth("75%");
						popup.setHeight("90%");
						popup.setContent(viewSearchCriteriaWindow);
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
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						
						UI.getCurrent().addWindow(popup);
						btnIFCSSearch.setEnabled(true);
					}
				});
				
				return btnIFCSSearch;		
			}
		});

		table.removeGeneratedColumn("Upload Document");
		table.addGeneratedColumn("Upload Document", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				/*upload = new Upload("Choose File",(Receiver) table.getParent());
				upload.addSucceededListener((SucceededListener) table.getParent());
				return upload;*/
				com.vaadin.v7.ui.Upload upload  = new Upload();
				  upload.setReceiver(new Receiver1());
				  upload.setData((LegalHeirDTO)itemId);
					upload.addSucceededListener(new SucceededListener() {
						
						@Override
						public void uploadSucceeded(SucceededEvent event) {
						
							System.out.println("File uploaded" + event.getFilename());
							
							try{
								
								
								byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

								String fileName = event.getFilename();
								LegalHeirDTO legalHeirFileName = (LegalHeirDTO)upload.getData();
								legalHeirFileName.setFileName(fileName);
								if (null != fileAsbyteArray) {
									//file gets uploaded in data directory when code comes here.
									if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
											event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
									{
										File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());

										fileName = convertedFile.getName();
										fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
									}
						
									WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
										Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
										//TO read file after load
										if (flagUploadSuccess.booleanValue())
										{
											String token = "" + uploadStatus.get("fileKey");
											Long docKey = Long.parseLong(token);
											legalBean.setDocumentToken(docKey);
											LegalHeirDTO docTokenIncludedRow = (LegalHeirDTO)upload.getData();
											docTokenIncludedRow.setDocumentToken(docKey);
											SelectValue docType = new SelectValue();
											if(SHAConstants.PA_LOB.equals(screenName)) {
												docType.setId(ReferenceTable.PA_LEGAL_HEIR_DOC_KEY);
											}
											else {
												docType.setId(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY);
											}
											docType.setValue(SHAConstants.LEGAL_HEIR_CERT);
											docTokenIncludedRow.setDocType(docType);
											
											//String fileName = event.getFilename();
//										    uploadStatus = null;
//										    submitHandler.submit(bean);
//										    thisObj.close();
										}
								}
								
							}catch(Exception e){
								e.printStackTrace();
							}
						
							
						}
					});
					upload.setButtonCaption(null);
					upload.setCaption("Choose File");
					
					Button btnUpload = new Button("Upload Document");
					btnUpload.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							upload.submitUpload();
						}
					});
					
					HorizontalLayout uploadLayout = new HorizontalLayout(upload, btnUpload);
					uploadLayout.setSpacing(true);
					
//					return upload;
					
					return uploadLayout;
			}
		});


	table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						LegalHeirDTO currentItemId = (LegalHeirDTO) event.getButton().getData();
						currentItemId.setDeleteLegalHeir(SHAConstants.YES_FLAG);
						table.removeItem(currentItemId);
						if(currentItemId.getLegalHeirKey() != null) {
							deletedList.add(currentItemId);
						}	
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		});
		
	}
	
	
	class Receiver1 implements Receiver{
		
		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// Create upload stream
	        FileOutputStream fos = null; // Stream to write to
	        try {
	            // Open the file for writing.
	        	
	            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
	            fos = new FileOutputStream(file);
	            
	            new Notification("File Uploaded Successfuly.", Notification.Type.WARNING_MESSAGE)
                .show(Page.getCurrent());
	            
	        } catch (final java.io.FileNotFoundException e) {
	            new Notification("Could not open file", e.getMessage(), Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	        }
	        return fos; // Return the output stream to write to
		}
	}
}
