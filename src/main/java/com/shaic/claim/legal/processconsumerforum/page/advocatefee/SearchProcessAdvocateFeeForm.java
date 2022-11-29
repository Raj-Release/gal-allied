/**
 * 
 */
package com.shaic.claim.legal.processconsumerforum.page.advocatefee;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.CaseDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.ConsumerFormSearchUI;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.domain.LegalAdvocateFee;
import com.shaic.domain.LegalConsumer;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


public class SearchProcessAdvocateFeeForm  extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6589043418245772350L;

	@EJB
	private MasterService masterService;
	
	@Inject
	private ConsumerFormSearchUI consumerFormSearchUI;
	
	@Inject
	private ViewDetails viewDetails;
	
	private Button btnIntimationSearch;
	
	private AdvocateFeeDTO advocateFeeDTO = new AdvocateFeeDTO();
	
	protected Button btnSave;
	protected Button btnSaveExit;
	protected Button btnCancel;
	
	private BeanFieldGroup<AdvocateFeeDTO> advocateBinder;
	
	private BeanFieldGroup<IntimationDetailsDTO> intimationBinder;
	
	private BeanFieldGroup<CaseDetailsDTO> caseDetailBinder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	Boolean isSatisfy = false ;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxRepudiation;
	private TextField txtprovisionAmt;
	private TextField txtProductNameCode;
	private TextField txtInsuredName;
	private TextField txtfinancialYear;
	
	private TextField txtdcdrf;
	private TextField txtadvocatename;
	private TextField txtOtherAdvocateName;
	private TextField txtccNo;
	private ComboBox cmbzone;
	private TextField txtAdvocateFee;
	private TextField txtAmtPaid;
	private TextField txtDdName;
	private CheckBox chkPartPayment;
	private CheckBox chkFullPayment;
	
	private VerticalLayout finalLayout ;
	
	
	/*public SearchProcessAdvocateFeeForm() {
		
		btnSave = new Button("Save");
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel = new Button("Exit");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
	}*/

	@PostConstruct
	public void init() {
		advocateFeeDTO = new AdvocateFeeDTO();
		initBinder(advocateFeeDTO);
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption("Advocate Fee");
		VerticalLayout intimationVerticalLayout = intimationVerticalLayout();
		VerticalLayout caseDetailVerticalLayout = caseDetailVerticalLayout();
		VerticalLayout advocateFeeVerticalLayout = advocateFeeVerticalLayout();
		finalLayout = new VerticalLayout(viewDetails,intimationVerticalLayout,caseDetailVerticalLayout,advocateFeeVerticalLayout);
		finalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		mainPanel.setContent(finalLayout);
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout intimationVerticalLayout(){
	/*	btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);*/
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		
		txtIntimationNo = intimationBinder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxRepudiation = intimationBinder.buildAndBind("Repudiation","repudiation",ComboBox.class);
		txtPolicyNo = intimationBinder.buildAndBind("Policy Number","policyNo",TextField.class);
		txtprovisionAmt = intimationBinder.buildAndBind("Provision Amount","provisionAmt",TextField.class);
		txtProductNameCode = intimationBinder.buildAndBind("Product Name","productNo",TextField.class);
		txtInsuredName = intimationBinder.buildAndBind("Insured Name","insuredName",TextField.class);
		txtfinancialYear= intimationBinder.buildAndBind("Financial Year","financialYear",TextField.class);
		
		doNumberValidation(txtprovisionAmt);
		doNumberValidation(txtfinancialYear);
		
		btnIntimationSearch = new Button();
		FormLayout searchButton = new FormLayout();
		searchButton.addComponent(btnIntimationSearch);
		searchButton.setMargin(true);
		searchButton.setCaption("");
		searchButton.setWidth("10px");
		btnIntimationSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIntimationSearch.setIcon(new ThemeResource("images/search.png"));
		//lyutIFCS.setSpacing(true);
		
		addCLickListener();
		
		//cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtfinancialYear,cbxRepudiation,txtprovisionAmt);
		formLayoutLeft.setCaption("Intimation Details");
		
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo, txtProductNameCode,txtInsuredName);	
		formLayoutReight.setCaption("Policy Details");
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,searchButton,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(false);
		fieldLayout.setWidth("1000px");
		
		 mainVerticalLayout.addComponent(fieldLayout);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
		 	mandatoryFields.add(cbxRepudiation);
			mandatoryFields.add(txtPolicyNo);
			mandatoryFields.add(txtProductNameCode);
			mandatoryFields.add(txtInsuredName);
			mandatoryFields.add(txtprovisionAmt);
			mandatoryFields.add(txtfinancialYear);
		 
		showOrHideValidation(false);
		 
		return mainVerticalLayout;
	}
	
	public VerticalLayout caseDetailVerticalLayout(){

			VerticalLayout mainVerticalLayout = new VerticalLayout();
			//mainVerticalLayout.setCaption("Case Details");
			
			cmbzone = caseDetailBinder.buildAndBind("Zone","zone",ComboBox.class);
			txtadvocatename = caseDetailBinder.buildAndBind("Advocate Name","adVocateName",TextField.class);
			txtdcdrf = caseDetailBinder.buildAndBind("DCDRF", "dcdrf", TextField.class);
			txtccNo = caseDetailBinder.buildAndBind("CC No","ccNo",TextField.class);
			FormLayout formLayoutLeft1 = new FormLayout(txtdcdrf,cmbzone);
			FormLayout formLayoutReight = new FormLayout(txtccNo,txtadvocatename);	
			HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
			formLayoutLeft1.setCaption("Case Details");
			fieldLayout1.setMargin(true);
			fieldLayout1.setWidth("100%");
			mainVerticalLayout.setCaption("");
			mainVerticalLayout.addComponent(fieldLayout1);
			mainVerticalLayout.addStyleName("layout-with-border");
			
			mandatoryFields.add(txtdcdrf);
			mandatoryFields.add(txtccNo);
			mandatoryFields.add(txtadvocatename);
			mandatoryFields.add(cmbzone);
			
			showOrHideValidation(false);
			
			return mainVerticalLayout;
		}
	
	public VerticalLayout advocateFeeVerticalLayout(){

		VerticalLayout mainVerticalLayout = new VerticalLayout();
		
		txtAdvocateFee = advocateBinder.buildAndBind("Advocate Fee","advocateFee",TextField.class);
		txtOtherAdvocateName = advocateBinder.buildAndBind("Advocate Name","advocateName",TextField.class);
		txtAmtPaid = advocateBinder.buildAndBind("Amount Paid", "amtPaid", TextField.class);
		txtDdName = advocateBinder.buildAndBind("DD Name","ddName",TextField.class);
		chkFullPayment = advocateBinder.buildAndBind("Full Payment","isFullPayment",CheckBox.class);
		chkPartPayment = advocateBinder.buildAndBind("Part Payment","isPartPayment",CheckBox.class);
		
		txtAdvocateFee.setNullRepresentation("");
		txtAmtPaid.setNullRepresentation("");
		
		doNumberValidation(txtAdvocateFee);
		doNumberValidation(txtAmtPaid);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(new HorizontalLayout(), chkPartPayment,chkFullPayment);
		fieldLayout.setSpacing(Boolean.TRUE);
		FormLayout formLayoutLeft1 = new FormLayout(txtAdvocateFee,txtOtherAdvocateName);
		formLayoutLeft1.setCaption("Advocate Fee");
		FormLayout formLayoutReight = new FormLayout(fieldLayout,txtAmtPaid,txtDdName);	
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
		fieldLayout1.setMargin(true);
		fieldLayout1.setWidth("100%");
		
		ConfirmDialog dialog = new ConfirmDialog();
		Button saveNexitButtonWithListener = getSaveNexitButtonWithListener(dialog);
		Button saveButtonWithListener = getSaveButtonWithListener(dialog);
		Button cancelButton = getCancelButton(dialog);
		HorizontalLayout absoluteLayout_3 =  new HorizontalLayout(saveButtonWithListener,saveNexitButtonWithListener,cancelButton);
		absoluteLayout_3.setWidth("50%");
		
		mainVerticalLayout.setCaption("");
		mainVerticalLayout.addComponent(fieldLayout1);
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setComponentAlignment(absoluteLayout_3, Alignment.TOP_CENTER);
		
		mainVerticalLayout.addStyleName("layout-with-border");
		mandatoryFields.add(txtAmtPaid);
		mandatoryFields.add(txtDdName);
		mandatoryFields.add(txtOtherAdvocateName);
		showOrHideValidation(false);
		
		return mainVerticalLayout;
	}
	
	
	private void initBinder(AdvocateFeeDTO advocateFeeDTO)
	{
		this.advocateBinder = new BeanFieldGroup<AdvocateFeeDTO>(AdvocateFeeDTO.class);
		this.advocateBinder.setItemDataSource(advocateFeeDTO);
		this.advocateBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		IntimationDetailsDTO intimationDetailsDTO = advocateFeeDTO.getIntimationDetailsDTO();
		this.intimationBinder = new BeanFieldGroup<IntimationDetailsDTO>(
				IntimationDetailsDTO.class);
		this.intimationBinder.setItemDataSource(intimationDetailsDTO);
		this.intimationBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		CaseDetailsDTO caseDetailsDTO = advocateFeeDTO.getCaseDetailsDTO();
		this.caseDetailBinder = new BeanFieldGroup<CaseDetailsDTO>(
				CaseDetailsDTO.class);
		this.caseDetailBinder.setItemDataSource(caseDetailsDTO);
		this.caseDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setCBXValue(BeanItemContainer<SelectValue> repudiationMasterValueByCode, BeanItemContainer<SelectValue> tmpZoneList){
		
		cbxRepudiation.setContainerDataSource(repudiationMasterValueByCode);
		cbxRepudiation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRepudiation.setItemCaptionPropertyId("value");
		
		cmbzone.setContainerDataSource(tmpZoneList);
		cmbzone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbzone.setItemCaptionPropertyId("value");
		
	}
	public void addCLickListener() {
		btnIntimationSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();

				popup.setWidth("75%");
				popup.setHeight("90%");
				consumerFormSearchUI.setPopup(popup, SHAConstants.LEGAL_ADVOCATE_FEE);
				consumerFormSearchUI.init();
				popup.setContent(consumerFormSearchUI);
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
			}
		});
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Exit");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.LEGAL_HOME, null);
			}
		});
		return btnCancel;
	}
	
	private Button getSaveButtonWithListener(final ConfirmDialog dialog) {
		btnSave = new Button("Save");
		btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if(!isValid()) {
					dialog.close();
					fireViewEvent(SearchProcessAdvocateFeePresenter.LEGAL_SAVE_ADVOCATE_FEE,advocateFeeDTO);
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			
		});
		return btnSave;
	}
	
	private Button getSaveNexitButtonWithListener(final ConfirmDialog dialog) {
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSaveExit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if(!isValid()) {
					dialog.close();
					fireViewEvent(SearchProcessAdvocateFeePresenter.LEGAL_SAVE_ADVOCATE_FEE, advocateFeeDTO);
					fireViewEvent(MenuItemBean.LEGAL_HOME, null);
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			
		});
		return btnSaveExit;
	}
	
	private boolean isValid() {
		

		// TODO Auto-generated method stub
		Boolean hasError = false;
		showOrHideValidation(true);
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			advocateFeeDTO.setUserName(userName);
			
			String eMsg = "";		
			if (!this.advocateBinder.isValid()) {

				for (Field<?> field : this.advocateBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if (!this.intimationBinder.isValid()) {

				for (Field<?> field : this.intimationBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if (!this.caseDetailBinder.isValid()) {

				for (Field<?> field : this.caseDetailBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if(!hasError){
				advocateBinder.commit();
				intimationBinder.commit();
				caseDetailBinder.commit();
			}else{

				setRequired(true);
				Label label = new Label(eMsg, ContentMode.HTML);
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

				//hasError = true;
				return hasError;
			
			}
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	
		
		
		/*
	
	
	
	
		// TODO Auto-generated method stub
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			advocateFeeDTO.setUserName(userName);
			advocateBinder.commit();
			intimationBinder.commit();
			caseDetailBinder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	*/}

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
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void populateFieldValues(LegalConsumer legalConsumer, LegalAdvocateFee legalAdvocateFee) {
		
		if(legalConsumer!=null){
			txtIntimationNo.setValue(legalConsumer.getIntimationNumber());
			txtPolicyNo.setValue(legalConsumer.getPolicyNumber());
			txtProductNameCode.setValue(legalConsumer.getProductName());
			txtfinancialYear.setValue(legalConsumer.getFinancialYear().toString());
			txtprovisionAmt.setValue(legalConsumer.getProvisionAmount().toString());
			txtInsuredName.setValue(legalConsumer.getInsuredName());
			MastersValue repudiationId = legalConsumer.getRepudiationId();
			if(repudiationId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(repudiationId.getKey());
				selectValue.setValue(repudiationId.getValue());
				cbxRepudiation.setValue(selectValue);
			}
			viewDetails.initView(legalConsumer.getIntimationNumber(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_ADVOCATE_FEE);
			}
		
		
		/*if(legalAdvocateFee!=null){
			txtIntimationNo.setValue(legalAdvocateFee.getIntimationNumber());
			txtPolicyNo.setValue(legalAdvocateFee.getPolicyNumber());
			txtProductNameCode.setValue(legalAdvocateFee.getProductName());
			if(legalAdvocateFee.getFinancialYear()!=null){
				txtfinancialYear.setValue(legalAdvocateFee.getFinancialYear().toString());
			}
			if(legalAdvocateFee.getProvisionAmount()!=null){
				txtprovisionAmt.setValue(legalAdvocateFee.getProvisionAmount().toString());
			}
			txtInsuredName.setValue(legalAdvocateFee.getInsuredName());
			MastersValue repudiationId = legalAdvocateFee.getRepudiationId();
			if(repudiationId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(repudiationId.getKey());
				selectValue.setValue(repudiationId.getValue());
				cbxRepudiation.setValue(selectValue);
			}
			
			txtdcdrf.setValue(legalAdvocateFee.getDcdrfRemarks());
			txtadvocatename.setValue(legalAdvocateFee.getAdvocateName());
			txtOtherAdvocateName.setValue(legalAdvocateFee.getCaseAdvocateName());
			txtccNo.setValue(legalAdvocateFee.getCcNo());
			
			TmpCPUCode zoneId = legalAdvocateFee.getZoneId();
			if(zoneId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(zoneId.getKey());
				selectValue.setValue(zoneId.getCpuCode() +"-"+ zoneId.getDescription());
				cmbzone.setValue(selectValue);
			}
			
			if(legalAdvocateFee.getAdvocateFee()!=null){
				txtAdvocateFee.setValue(legalAdvocateFee.getAdvocateFee().toString());
			}
			if(legalAdvocateFee.getPaidAmount()!=null){
				txtAmtPaid.setValue(legalAdvocateFee.getPaidAmount().toString());
			}
			txtDdName.setValue(legalAdvocateFee.getDdName());
			chkPartPayment.setValue(legalAdvocateFee.getPartPaymentFlag());
			chkFullPayment.setValue(legalAdvocateFee.getFullPaymentFlag());

		}*/
		
	}
	
	private void doNumberValidation(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
}

