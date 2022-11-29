package com.shaic.claim.userproduct.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.userproduct.document.search.SearchDoctorDetailsTableDTO;
import com.shaic.claim.userproduct.document.search.ViewDoctorSearchCriteriaViewImpl;
import com.shaic.domain.MasOrgCpuMapping;
import com.shaic.domain.MasUser;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UserToOrgMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;


public class ProductAndDocumentPageUI extends ViewComponent{
	
	@Inject
	private Instance<ApplicableCpuTable> applicableCpuTableInstance;
	
	private ApplicableCpuTable applicableCpuTableObj;
	
	@Inject
	private ProductDocTypeTable productDocTypeTableObj;
	
	@Inject
	private ViewDoctorSearchCriteriaViewImpl viewSearchCriteria;
	
	@EJB
	private UserProductMappingService cpuCodeService;
	
	private ProductAndDocumentTypeDTO bean;
	
	private BeanFieldGroup<ProductAndDocumentTypeDTO> binder;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private TextField doctorName;
	
	private TextField doctorId;
	
	private TextField role;
	
	private ComboBox status;
	
	private TextField queueCount;
	
	private TextField minAmount;
	
	private TextField maxAmount;
	
	private TextField startTime;
	
	private TextField endTime;
	
	//private Button editBtn;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private Long cpuKey;
	
	//private String dName;
	
	private Button searchDoctor;
	
	//public static Window popup;
	private ComboBox userType;
	
	public static Window popup;
	
	private CheckBox highValueClaim;
	
	private TextField shiftStartTime;
	
	private TextField shiftEndTime;
	
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@PostConstruct
	public void initView() {
		
	}
	
	public void initBinder() {
		
	}
	
	@SuppressWarnings("static-access")
	public void init(ProductAndDocumentTypeDTO bean, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> userTypeContainer){
		
		this.bean = bean;
		
		this.binder = new BeanFieldGroup<ProductAndDocumentTypeDTO>(ProductAndDocumentTypeDTO.class);
		
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		doctorName = (TextField) binder.buildAndBind("Doctor Name","doctorName", TextField.class);
		doctorName.setNullSettingAllowed(false);
		doctorName.setReadOnly(true);
		
		doctorId = (TextField) binder.buildAndBind("Doctor ID","doctorId", TextField.class);
		doctorId.setNullSettingAllowed(false);
		doctorId.setReadOnly(true);
		
		role = (TextField) binder.buildAndBind("Role","role", TextField.class);
		role.setNullSettingAllowed(false);
		role.setReadOnly(true);
		
		/*status = (ComboBox) binder.buildAndBind("Status",
				"status", ComboBox.class);
		
		status.setContainerDataSource(selectValueContainer);
		status.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		status.setItemCaptionPropertyId("value");
		
		if (this.bean.getStatus() != null) {
			this.status.setValue(this.bean.getStatus());
		}
		*/
		userType = (ComboBox) binder.buildAndBind("User Type",
				"type", ComboBox.class);
		
		userType.setContainerDataSource(userTypeContainer);
		userType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		userType.setItemCaptionPropertyId("value");
		
		if (this.bean.getType() != null) {
			this.userType.setValue(this.bean.getType());
		}
		
		queueCount = (TextField) binder.buildAndBind("Queue Count","queueCount", TextField.class);
		queueCount.setNullSettingAllowed(false);
		CSValidator validator3 = new CSValidator();
		validator3.extend(queueCount);
		validator3.setRegExp("^[0-9]*$");
		validator3.setPreventInvalidTyping(true);
		queueCount.setNullRepresentation("");
		
		minAmount = (TextField) binder.buildAndBind("Min Amount","minAmount",TextField.class);
		minAmount.setEnabled(false);
		CSValidator validator = new CSValidator();
		validator.extend(minAmount);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		
		maxAmount = (TextField) binder.buildAndBind("Max Amount","maxAmount",TextField.class);
		maxAmount.setEnabled(false);
		CSValidator validators = new CSValidator();
		validators.extend(maxAmount);
		validators.setRegExp("^[0-9]*$");
		validators.setPreventInvalidTyping(true);
		
		startTime = (TextField) binder.buildAndBind("Start Time","startTime",TextField.class);
		startTime.setInputPrompt("HH:MM");
		startTime.setNullSettingAllowed(false);
		CSValidator validator1 = new CSValidator();
		validator1.extend(startTime);
		validator1.setRegExp("^(|[0-9:]+)$");
		validator1.setPreventInvalidTyping(true);
		startTime.setNullRepresentation("");
		
		endTime = (TextField) binder.buildAndBind("End Time","endTime",TextField.class);
		endTime.setInputPrompt("HH:MM");
		endTime.setNullSettingAllowed(false);
		CSValidator validator2 = new CSValidator();
		validator2.extend(endTime);
		validator2.setRegExp("^(|[0-9:]+)$");
		validator2.setPreventInvalidTyping(true);
		endTime.setNullRepresentation("");
		
		//String amountLong = minAmount.getValue();
		
		TextField dummyTextField = new TextField();
		dummyTextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		//editBtn = new Button("Edit");
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		/*editBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		editBtn.setWidth("-1px");
		editBtn.setHeight("-10px");*/
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(/*editBtn,*/submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		Panel mainPanel = new Panel();
		//Vaadin8-setImmediate() mainPanel.setImmediate(true);
		mainPanel.setHeight("100%");
		mainPanel.setWidth("100%");
		
		searchDoctor = new Button();
		searchDoctor.setStyleName(ValoTheme.BUTTON_LINK);
		searchDoctor.setIcon(new ThemeResource("images/search.png"));
		
		
		addDoctorListner();
		HorizontalLayout highValue = new HorizontalLayout();
		highValue.setCaption("High Value Claim");
		
		
		
		highValueClaim = (CheckBox) binder.buildAndBind("", "highValueClaim",CheckBox.class);
		if(bean.getHighValueClaim()!= null && bean.getHighValueClaim().equals(true)){
			highValueClaim.setValue(true);
			/*if(highValueClaim.isEnabled()){
			
			}*/
		}
		
		
		highValue.addComponent(highValueClaim);
		highValueClaim.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					 if(highValueClaim.isEmpty()){
						 shiftStartTime.setValue("");
						 shiftEndTime.setValue("");
							shiftStartTime.setEnabled(false);
							shiftEndTime.setEnabled(false);
						}
					 if(!highValueClaim.isEmpty()){
						 shiftStartTime.setEnabled(true);
						 shiftEndTime.setEnabled(true);
					 }
					 
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		FormLayout firstFormLayout = new FormLayout(doctorName,role,highValue);
		firstFormLayout.setSpacing(true);
		
		/*TextField dummField = new TextField();
		dummField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummField.setEnabled(false);
		
		TextField dummField1 = new TextField();
		dummField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummField1.setEnabled(false);
		
		TextField dummField2 = new TextField();
		dummField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummField2.setEnabled(false);*/
	
	/*if(highValueClaim.getValue().TRUE) {
		shiftStartTime.setEnabled(true);
		shiftEndTime.setEnabled(true);
	}else if(highValueClaim.getValue().FALSE){
		shiftStartTime.setEnabled(false);
		shiftEndTime.setEnabled(false);
	}*/
		shiftStartTime = (TextField) binder.buildAndBind("Shift Start Time","shiftStartTime",TextField.class);
		if(highValueClaim.getValue().equals(true) && highValueClaim.isEnabled() && bean.getShiftStartTime()!=null) {
			shiftStartTime.setEnabled(true);
		}else{
			shiftStartTime.setEnabled(false);
		}
		shiftStartTime.setInputPrompt("HH:MM");
		shiftStartTime.setNullSettingAllowed(false);
		CSValidator shiftStartTimeValidator = new CSValidator();
		shiftStartTimeValidator.extend(shiftStartTime);
		shiftStartTimeValidator.setRegExp("^(|[0-9:]+)$");
		shiftStartTimeValidator.setPreventInvalidTyping(true);
		shiftStartTime.setNullRepresentation("");
		
		
		
		shiftEndTime = (TextField) binder.buildAndBind("Shift End Time","shiftEndTime",TextField.class);
		if(highValueClaim.getValue().equals(true) && highValueClaim.isEnabled() && bean.getShiftEndTime()!=null) {
			shiftEndTime.setEnabled(true);
		}else{
			shiftEndTime.setEnabled(false);
		}
		shiftEndTime.setInputPrompt("HH:MM");
		shiftEndTime.setNullSettingAllowed(false);
		CSValidator shiftEndTimeValidator = new CSValidator();
		shiftEndTimeValidator.extend(shiftEndTime);
		shiftEndTimeValidator.setRegExp("^(|[0-9:]+)$");
		shiftEndTimeValidator.setPreventInvalidTyping(true);
		shiftEndTime.setNullRepresentation("");
		
		
		
		
		FormLayout secondFormLayout = new FormLayout(doctorId,minAmount,shiftStartTime);
		secondFormLayout.setSpacing(true);
		
		FormLayout thirdFormLayout = new FormLayout(userType,maxAmount,shiftEndTime);
		thirdFormLayout.setSpacing(true);
		
		/*FormLayout fourthFormLayout = new FormLayout(userType);
		fourthFormLayout.setSpacing(true);*/
		
		HorizontalLayout horzLayout = new HorizontalLayout(firstFormLayout,secondFormLayout,thirdFormLayout);
		horzLayout.setSpacing(true);
		
		HorizontalLayout dummyLayout = new HorizontalLayout();
		
		VerticalLayout verticalMain = new VerticalLayout(horzLayout,dummyLayout,productDocumentTabs(),dummyLayout,buttonHor);
		//mainPanel.setContent(verticalMain);
		verticalMain.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		setCompositionRoot(verticalMain);
		
		addListener();
		
		/*mandatoryFields.add(queueCount);
		mandatoryFields.add(status);
		mandatoryFields.add(startTime);
		mandatoryFields.add(endTime);*/
		mandatoryFields.add(userType);
		
		showOrHideValidation(false);
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	
	private TabSheet productDocumentTabs(){
		TabSheet productDocumentTab = new TabSheet();
		//Vaadin8-setImmediate() productDocumentTab.setImmediate(true);
		
		
		productDocumentTab.setSizeFull();
		
		productDocumentTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		
		TabSheet applicableCpuTab =  getApplicableCpuTab();
		productDocumentTab.setHeight("100.0%");
		productDocumentTab.addTab(applicableCpuTab, "Applicable CPU", null);

		// tabSheet_2
		/*TabSheet productDocTypeTab = getProductDocTypeTab();
		productDocumentTab.addTab(productDocTypeTab, "Product/Doc Type", null);*/

		

		return productDocumentTab;
	}
	
	private TabSheet getApplicableCpuTab() {
		
		TabSheet applCpuTab = new TabSheet();
		applCpuTab.hideTabs(true);
		//Vaadin8-setImmediate() applCpuTab.setImmediate(true);
		applCpuTab.setWidth("100%");
		applCpuTab.setHeight("100%");
		applCpuTab.setSizeFull();
		//Vaadin8-setImmediate() applCpuTab.setImmediate(true);
		
		applicableCpuTableObj = applicableCpuTableInstance.get();
		applicableCpuTableObj.init();
		applicableCpuTableObj.setReferenceData(this.referenceData);
		//applicableCpuTableObj.setEnabled(false);
		
		
		setApplicableCpuTableValue(cpuKey); // hard code cpu key for testing remove after db dev
		
		applCpuTab.addComponent(applicableCpuTableObj);
		
		return applCpuTab;
	}
	
	private TabSheet getProductDocTypeTab() {
		TabSheet productDocType = new TabSheet();
		productDocType.hideTabs(true);
		//Vaadin8-setImmediate() productDocType.setImmediate(true);
		productDocType.setWidth("100%");
		productDocType.setHeight("100%");
		productDocType.setSizeFull();
		//Vaadin8-setImmediate() productDocType.setImmediate(true);
		
		productDocTypeTableObj.init();
		productDocTypeTableObj.setReferenceData(this.referenceData);
		//productDocTypeTableObj.setEnabled(false);
		
		Long key = 18l;
		setProductDocTypeValue(key);
		
		productDocType.addComponent(productDocTypeTableObj);
		
		return productDocType;
		
	}
	
	private void setApplicableCpuTableValue(Long cpuKey) {
		
				
		List<TmpCPUCode> tmpCpucode = cpuCodeService.getCpuCode();
		//List<ApplicableCpuDTO> cpuDetails = new ArrayList<ApplicableCpuDTO>();
		int sno=1;
		
		MasUser doctorDetails  = cpuCodeService.getEmployeeByName(bean.getDoctorName());
		
		//TmpEmployee doctorDetails = cpuCodeService.getEmployeeByName(bean.getDoctorName());
		
		//List<UserCpuMapping> cpuList2 = cpuCodeService.getList(doctorDetails.getEmpId());
		
		//List<MasOrgUserMapping> cpuList1 = cpuCodeService.getList(doctorDetails.getUserId());
		
		List<MasOrgCpuMapping> cpuList = new ArrayList<MasOrgCpuMapping>();
		/*for(MasOrgUserMapping orgUser : cpuList1){
			List<MasOrgCpuMapping> cpuList2 = cpuCodeService.getCPUList(orgUser.getOrgId());
			cpuList.addAll(cpuList2);
			
		}*/
		
		
		/*List<String> availableCpu = new ArrayList<String>();
		for (MasOrgCpuMapping orgCpuMapping : cpuList) {
			if(orgCpuMapping.getCpuCode() != null){
				availableCpu.add(orgCpuMapping.getCpuCode());
			}
		}*/
		
		List<UserToOrgMapping> userMap = cpuCodeService.getOrgMappingByUser(doctorDetails.getUserId());
		List<String> orgList = new ArrayList<String>();
		
		for (UserToOrgMapping userToOrgMapping : userMap) {
			orgList.add(userToOrgMapping.getOrgId());
		}
		List<Long> cpuListByOrg = new ArrayList<Long>();
		if(orgList!=null && !orgList.isEmpty()) {
		List<TmpCPUCode> listOfCPU = cpuCodeService.getCPUByOrgList(orgList);
		
		for (TmpCPUCode tmpCPUCode2 : listOfCPU) {
			cpuListByOrg.add(tmpCPUCode2.getCpuCode());
		}
		}
		if(tmpCpucode != null) {
			for (TmpCPUCode applicableCpuDTO : tmpCpucode) {
			ApplicableCpuDTO cpuDto = new ApplicableCpuDTO();
			cpuDto.setSno(sno);
			cpuDto.setCpuCodewithName(applicableCpuDTO.getCpuCode()+ "-" +applicableCpuDTO.getDescription());
			cpuDto.setCpuCode(Long.toString(applicableCpuDTO.getCpuCode()));
			
			
			if(cpuListByOrg!=null && !cpuListByOrg.isEmpty()) {
			if(cpuListByOrg.contains(applicableCpuDTO.getCpuCode())){
				cpuDto.setCheckBoxValue(true);
				cpuDto.setAccessability(Boolean.TRUE);
			}
			}
			//cpuDetails.add(cpuList);
			this.applicableCpuTableObj.addBeanToList(cpuDto);
			sno++;
			
		}
		}
		
		
	}
	
	public void setTableValuestoDto() {
		
		List<ApplicableCpuDTO> applicableList = this.applicableCpuTableObj.getValues();
		this.bean.setApplicableCpuList(applicableList);
	}
	
	
	
	private void setProductDocTypeValue(Long dockey) {
		List<Product> productDetails = cpuCodeService.getHealthProductDetails();
		
		int sno=1;
		
		MasUser doctorDetails = cpuCodeService.getEmployeeByName(bean.getDoctorName());
		
		List<UserProductMapping> cpuList2 = cpuCodeService.getProductList(doctorDetails.getEmpId());
		//List<String> availableProd = new ArrayList<String>();
		
		
		
		if(productDetails != null) {
			for(Product products:productDetails){
			ProductDocTypeDTO productList = new ProductDocTypeDTO();
			productList.setSno(sno);
			productList.setProdCode(products.getCode());
			productList.setProductDocType(products.getCode() + "-"+products.getValue());
			productList.setProductTypeKey(products.getKey());
			for (UserProductMapping prodMap : cpuList2) {
				if(prodMap.getProductKey() != null && prodMap.getProductKey().equals(products.getKey())) {
					if(prodMap.getPreauthEligibility() != null && prodMap.getPreauthEligibility().equalsIgnoreCase("Y") || (productList != null && productList.getPreauthcheckBoxValue())) {
						productList.setPreauthcheckBoxValue(true);
					}
					
					if(prodMap.getEnhancementEligibility() != null && prodMap.getEnhancementEligibility().equalsIgnoreCase("Y") || (productList != null && productList.getEnhancheckBoxValue())) {
						productList.setEnhancheckBoxValue(true);
					}
					
				}
				
			}
			
			this.productDocTypeTableObj.addBeanToList(productList);
			sno++;
			}
		}
	}
	
	public void setTableValuetoProduct() {
		List<ProductDocTypeDTO> productTableList = this.productDocTypeTableObj.getValues();
		/*for (ProductDocTypeDTO productDocTypeDTO : productTableList) {
			
		}*/
		this.bean.setProductsList(productTableList);
	}
	
	
	
	public void setUpReference(ProductAndDocumentTypeDTO bean){
		this.bean = bean;
	}
	
	public void addListener(){
		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.USER_ACCESS_ALLOCATION, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
					
					
			    String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			    
			    setTableValuetoProduct();
			    setTableValuestoDto();
			    
//			    if(productDocTypeTableObj.getValues() != null){
			    
			    Integer maxAmt = SHAUtils.getIntegerFromStringWithComma(maxAmount.getValue());
			    Integer minAmt = SHAUtils.getIntegerFromStringWithComma(minAmount.getValue());
			    
			    
			    
			   bean.setMinAmount(minAmt.longValue());
			   bean.setMaxAmount(maxAmt.longValue());
			   
				//fireViewEvent(ProductDocTypePresenter.PRODUCT_DOCTYPE_SUBMIT, bean,userName);
				fireViewEvent(ProductDocTypePresenter.USER_CPU_SUBMIT,bean,userName);	
				}
				
				
			}
		});
		
	/*	editBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
			
				List<ApplicableCpuDTO> listCpu = applicableCpuTableObj.getValues();
				List<ApplicableCpuDTO> changeValues = new ArrayList<ApplicableCpuDTO>();
				if(listCpu != null) {
				for (ApplicableCpuDTO applicableCpuDTO : listCpu) {
//					applicableCpuDTO.setCheckBoxValue(true);
					applicableCpuDTO.setIsEnabled(false);
					changeValues.add(applicableCpuDTO);
				}
				applicableCpuTableObj.setTableList(changeValues);
				}
				
				List<ProductDocTypeDTO> listofProducts =  productDocTypeTableObj.getValues();
				List<ProductDocTypeDTO> listchanges = new ArrayList<ProductDocTypeDTO>();
				if(listofProducts != null) {
					for (ProductDocTypeDTO productsTable : listofProducts) {
						productsTable.setIsEnabled(true);
						listchanges.add(productsTable);
					}
					productDocTypeTableObj.setTableList(listchanges);
				}
			}
		});
		editBtn.setDisableOnClick(true);*/
	}
	
	/*private void unbindField(Field<?> field) {
		if (field != null) {
			if (binder.getPropertyId(field) != null) {
				this.binder.unbind(field);
			}

		}
	}*/
	public void addDoctorListner() {
		
		searchDoctor.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				final Window popup = new com.vaadin.ui.Window();
				
				viewSearchCriteria.initView(popup);
//				viewSearchCriteria.setWindowObject(popup);
				popup.setWidth("60%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteria);
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
						popup.close();
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		});
		
	}

	public void setDoctorNameValue(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		// TODO Auto-generated method stub
//		doctorName.setValue(viewSearchCriteriaDTO.getDoctorName());
		if(viewSearchCriteriaDTO.getMinAmt() != null) {
		minAmount.setValue(viewSearchCriteriaDTO.getMinAmt().toString());
		} 
		if(viewSearchCriteriaDTO.getMaxAmt() != null) {
		maxAmount.setValue(viewSearchCriteriaDTO.getMaxAmt().toString());
		}
		
		//dName = viewSearchCriteriaDTO.getDoctorName();
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
 
		public boolean validatePage() {
			Boolean hasError = false;
			showOrHideValidation(true);
			StringBuffer eMsg = new StringBuffer();
			
			if (!this.binder.isValid()) {

				for (Field<?> field : this.binder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg.append(errMsg.getFormattedHtmlMessage());
					}
					hasError = true;
				}
			}
			
		if ((this.startTime != null && this.startTime.getValue() != null && !this.startTime
				.getValue().isEmpty())) {

			String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

			if (!(this.startTime.getValue().matches(regExp))) {
				hasError = true;
				eMsg.append("Please enter valid 24-hour time format for Start Time.</br>");
			}
		}
			
		if ((this.endTime != null && this.endTime.getValue() != null && !this.endTime
						.getValue().isEmpty())) {

			String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

			if (!(this.endTime.getValue().matches(regExp))) {
				hasError = true;
				eMsg.append("Please enter valid 24-hour time format for End Time.</br>");
			}
		}
		
		if(this.applicableCpuTableObj.getValues() != null){
			List<ApplicableCpuDTO> listValues = this.applicableCpuTableObj.getValues();
			Boolean hasSelected = Boolean.FALSE;
			for (ApplicableCpuDTO applicableCpuDTO : listValues) {
				if(applicableCpuDTO.getAccessability()!=null && applicableCpuDTO.getAccessability()) {
					hasSelected = Boolean.TRUE;
					break;
				}
			}
			
			if ((this.shiftStartTime != null && this.shiftStartTime.getValue() != null && !this.shiftStartTime
					.getValue().isEmpty())) {

				String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

				if (!(this.shiftStartTime.getValue().matches(regExp))) {
					hasError = true;
					eMsg.append("Please enter valid 24-hour time format for Shift Start Time.</br>");
				}
			}
				if ((this.shiftEndTime != null && this.shiftEndTime.getValue() != null && !this.shiftEndTime
						.getValue().isEmpty())) {

					String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

					if (!(this.shiftEndTime.getValue().matches(regExp))) {
						hasError = true;
						eMsg.append("Please enter valid 24-hour time format for Shift End Time.</br>");
					}
				}
				
				if(this.highValueClaim.getValue().equals(false)) {
					shiftStartTime.setEnabled(false);
					shiftEndTime.setEnabled(false);
				}
				if(this.highValueClaim.getValue().equals(true)) {
					if(shiftStartTime.isEmpty() || shiftEndTime.isEmpty()) {
						hasError = true;
						eMsg.append("Please Enter the Shift Timings");
					}
				}
				
				if(this.shiftStartTime.getValue() != null && this.shiftEndTime.getValue() != null && this.highValueClaim.getValue().equals(true) && 
						!this.shiftStartTime.isEmpty() && !this.shiftEndTime.isEmpty()) {
			String startTim = this.shiftStartTime.getValue();
			String endTim = this.shiftEndTime.getValue();
			if(!startTim.contains(":") && !endTim.contains(":")){
				hasError = true;
				eMsg.append("Please Enter the Time in HH:MM Format");
			}else {
			String diffTin[] = startTim.split(":");
			String diffEndTim[] = endTim.split(":");
			int std = 0,std1 = 0,etd = 0,etd1 = 0;
			for(int i=0;i<diffTin.length;i++) {
				std = Integer.parseInt(diffTin[i]);
				std1 = Integer.parseInt(diffTin[1]);
				break;
			}
			for(int i=0;i<diffEndTim.length;i++) {
				etd = Integer.parseInt(diffEndTim[i]);
				etd1 = Integer.parseInt(diffEndTim[1]);
				break;
			}
			
			if(std>etd) {
				hasError=true;
				eMsg.append("Shift End Time cannot be Lesser than Shift Start Time");
			}
			if(etd==std && std1>etd1){
				hasError=true;
				eMsg.append("Shift End Time cannot be Lesser than Shift Start Time");
			}
			if(std==etd && std1==etd1){
				hasError=true;
				eMsg.append("Shift Start Time and Shift End Time Cannot Be Same");
			}
				}
				}
				
			if(!hasSelected){
				hasError = true;
				eMsg.append("Please select atleast one accessability");
			}
		}
			
			if (hasError) {
				setRequired(true);
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
					this.binder.commit();
				} catch (CommitException e) {
					e.printStackTrace();
				}
				showOrHideValidation(false);
				return true;
			}
			
		}
	}


