/**
 * 
 */
package com.shaic.claim.legal.processconsumerforum.page.advocatenotice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import com.shaic.domain.Claim;
import com.shaic.domain.LegalAdvocate;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
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
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


public class SearchProcessAdvocateNoticeForm  extends ViewComponent{
	
	
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
	
	private AdvocateNoticeDTO advocateNoticeDTO = new AdvocateNoticeDTO();
	protected Button btnSave;
	protected Button btnSaveExit;
	protected Button btnCancel;
	
	private BeanFieldGroup<AdvocateNoticeDTO> advocateBinder;
	
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
	
	private TextField txtStatus;
	private TextField txtadvocatename;
	private DateField txtLimitationTime;
	private DateField txtDateReplySent;
	private TextArea txtAdvocateReason;
	private TextField txtAdvocateStatus;
	private DateField txtSettleDate;
	private TextField txtSettleAmt;
	private TextArea txtSettleReason;
	private ComboBox cmbMovedTo;
	private ComboBox cmbPendingLevel;
	private DateField dateAdvocateNoticeDate;
	private DateField dateNoticeRecievedDate;
	//private DateField dateComplainceDate;
	private OptionGroup chkLegalLock;
	//private CheckBox chkLegalCompleted;
	private OptionGroup chkToStandRejection;
	private CheckBox chkToSettle;

	private VerticalLayout finalLayout ;

	private TextField txtDiagnosis;

	private ComboBox cbxRecievedFrom;
	
	
	/*public SearchProcessAdvocateNoticeForm() {
		
		btnSave = new Button("Save");
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel = new Button("Exit");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
	}*/

	@PostConstruct
	public void init() {
		advocateNoticeDTO = new AdvocateNoticeDTO();
		initBinder(advocateNoticeDTO);
		
		Panel mainPanel = new Panel();
		/*mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");*/
		mainPanel.setCaption("Lawyer/Advocate Notice");
		VerticalLayout intimationVerticalLayout = intimationVerticalLayout();
		VerticalLayout caseDetailVerticalLayout = caseDetailVerticalLayout();
		ConfirmDialog dialog = new ConfirmDialog();
		HorizontalLayout buttonsLayout = new HorizontalLayout(getSaveButtonWithListener(dialog),getSaveNexitButtonWithListener(dialog), getCancelButton(dialog));
		buttonsLayout.setWidth("50%");
		buttonsLayout.setSpacing(true);
		//VerticalLayout awardDetailVerticalLayout = awardDetailVerticalLayout();
		finalLayout = new VerticalLayout(viewDetails,intimationVerticalLayout,caseDetailVerticalLayout,buttonsLayout);
		finalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		finalLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);
		mainPanel.setContent(finalLayout);
		//mainPanel.setHeight("100%");
		setCompositionRoot(mainPanel);
	}
	/*public VerticalLayout finalVerticalLayout(Integer index,VerticalLayout intimationVerticalLayout){
		
		VerticalLayout finalLayout = new VerticalLayout(intimationVerticalLayout);
		finalLayout.addComponent(intimationVerticalLayout, 0);
		return finalLayout;
		
	}*/
	
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
		txtDiagnosis =intimationBinder.buildAndBind("Diagnosis","diagnosis",TextField.class);
		cbxRecievedFrom =intimationBinder.buildAndBind("Received From","receivedFrom",ComboBox.class);
		cbxRecievedFrom.setReadOnly(true);
		
		txtDiagnosis.setReadOnly(true);
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
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtfinancialYear,cbxRepudiation,txtprovisionAmt,txtDiagnosis);
		formLayoutLeft.setCaption("Intimation Details");
		
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo, txtProductNameCode,txtInsuredName,cbxRecievedFrom);	
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
			//mandatoryFields.add(txtDiagnosis);
		showOrHideValidation(false);
		 
		return mainVerticalLayout;
	}
	
	public VerticalLayout caseDetailVerticalLayout(){

			VerticalLayout mainVerticalLayout = new VerticalLayout();
			mainVerticalLayout.setCaption("");
			
			txtadvocatename = caseDetailBinder.buildAndBind("Advocate Name","adVocateName",TextField.class);
			
			dateAdvocateNoticeDate = caseDetailBinder.buildAndBind("Advocate Notice Date","advocateNoticeDate",DateField.class);
			txtLimitationTime = caseDetailBinder.buildAndBind("Limitation Date","limitationTime",DateField.class);
			txtStatus = caseDetailBinder.buildAndBind("Status","status",TextField.class);
			
			cmbMovedTo = caseDetailBinder.buildAndBind("Moved To","movedTo",ComboBox.class);
			dateNoticeRecievedDate = caseDetailBinder.buildAndBind("Notice Received Date","noticeRecievedDate",DateField.class);
			//dateComplainceDate = caseDetailBinder.buildAndBind("Complaince Date","complainceDate",DateField.class);
			cmbPendingLevel = caseDetailBinder.buildAndBind("Pending Level","pendingValue",ComboBox.class);
			
			chkLegalLock = (OptionGroup) caseDetailBinder.buildAndBind(" ","legalLock",OptionGroup.class);
			chkLegalLock.addItems(getReadioButtonOptions());
			chkLegalLock.setItemCaption(true, "Legal Lock");
			chkLegalLock.setItemCaption(false, "Legal Completed");
			chkLegalLock.setStyleName("horizontal");
			
			
			chkToStandRejection = (OptionGroup) caseDetailBinder.buildAndBind(" ","isStandRejection",OptionGroup.class);
			chkToStandRejection.addItems(getReadioButtonOptions());
			chkToStandRejection.setItemCaption(true, "To Stand Rejection");
			chkToStandRejection.setItemCaption(false, "To Settle");
			chkToStandRejection.setStyleName("horizontal");
			
			txtStatus.removeAllValidators();
			cmbMovedTo.removeAllValidators();
			dateNoticeRecievedDate.removeAllValidators();
			//dateComplainceDate.removeAllValidators();
			cmbPendingLevel.removeAllValidators();
			dateAdvocateNoticeDate.removeAllValidators();
			//txtLimitationTime.removeAllValidators();

			txtStatus.setComponentError(null);
			cmbMovedTo.setComponentError(null);
			dateNoticeRecievedDate.setComponentError(null);
			//dateComplainceDate.setComponentError(null);
			cmbPendingLevel.setComponentError(null);
			dateAdvocateNoticeDate.setComponentError(null);
			//txtLimitationTime.setComponentError(null);
			//txtSettleReason.setComponentError(null);
			
			txtStatus.setRequired(false);
			cmbMovedTo.setRequired(false);
			dateNoticeRecievedDate.setRequired(false);
			//dateComplainceDate.setRequired(false);
			cmbPendingLevel.setRequired(false);
			dateAdvocateNoticeDate.setRequired(false);
			//txtLimitationTime.setRequired(false);
			
			cmbMovedTo.setValidationVisible(false);
			dateNoticeRecievedDate.setValidationVisible(false);
			//dateComplainceDate.setValidationVisible(false);
			cmbPendingLevel.setValidationVisible(false);
			dateAdvocateNoticeDate.setValidationVisible(false);
			//txtLimitationTime.setValidationVisible(false);
			
			
			mandatoryFields.add(chkLegalLock);
			mandatoryFields.add(txtadvocatename);
			mandatoryFields.add(txtLimitationTime);
			showOrHideValidation(false);
			
			FormLayout formLayoutLeft = new FormLayout();
			formLayoutLeft.setCaption("Advocate Details");
			HorizontalLayout horizontalLayout = new HorizontalLayout(chkLegalLock);
			horizontalLayout.setCaption(" ");
			horizontalLayout.setSpacing(Boolean.TRUE);
			
			HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,horizontalLayout);
			fieldLayout.setMargin(true);
			fieldLayout.setWidth("100%");	
			
			
			FormLayout formLayoutLeft1 = new FormLayout(txtadvocatename,dateAdvocateNoticeDate,txtLimitationTime,txtStatus,chkToStandRejection);
			FormLayout formLayoutReight = new FormLayout(cmbMovedTo, dateNoticeRecievedDate,cmbPendingLevel);	
			
			HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
			
			noticeRecievedDateListener(dateNoticeRecievedDate, txtLimitationTime);
			
			isStandRejectionListener(chkToStandRejection, mainVerticalLayout);
			fieldLayout.setMargin(true);
			fieldLayout.setWidth("100%");	
			fieldLayout1.setMargin(true);
			fieldLayout1.setWidth("100%");	
			fieldLayout1.setSpacing(Boolean.TRUE);
			
			 mainVerticalLayout.addComponent(fieldLayout);
			 mainVerticalLayout.addComponent(fieldLayout1);
			 ////Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
			 //mainVerticalLayout.setMargin(true);
			 mainVerticalLayout.addStyleName("layout-with-border");
			
			
			return mainVerticalLayout;
		}
	

	private void unbindField(Field<?> field , BeanFieldGroup<?> binder) {
		if (field != null ) {
			Object propertyId = binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				binder.unbind(field);
			}
		}
	}
	
	public AbsoluteLayout buttonVerticalLayout(){
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(btnSave, "top:190.0px;left:140.0px;");
		absoluteLayout_3.addComponent(btnSaveExit, "top:190.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnCancel, "top:190.0px;left:329.0px;");
		return absoluteLayout_3;
	}

		
	
	
	
	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	
	private void initBinder(AdvocateNoticeDTO advocateNoticeDTO)
	{
		this.advocateBinder = new BeanFieldGroup<AdvocateNoticeDTO>(AdvocateNoticeDTO.class);
		this.advocateBinder.setItemDataSource(advocateNoticeDTO);
		this.advocateBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.intimationBinder = new BeanFieldGroup<IntimationDetailsDTO>(
				IntimationDetailsDTO.class);
		IntimationDetailsDTO intimationDetailsDTO = advocateNoticeDTO.getIntimationDetailsDTO();
		this.intimationBinder.setItemDataSource(intimationDetailsDTO);
		this.intimationBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.caseDetailBinder = new BeanFieldGroup<CaseDetailsDTO>(
				CaseDetailsDTO.class);
		CaseDetailsDTO caseDetailsDTO = advocateNoticeDTO.getCaseDetailsDTO();
		this.caseDetailBinder.setItemDataSource(caseDetailsDTO);
		this.caseDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setCBXValue(BeanItemContainer<SelectValue> pendingLevelMasterValueByCode, 
			BeanItemContainer<SelectValue> repudiationMasterValueByCode, 
			BeanItemContainer<SelectValue> moveToMasterValueByCode, BeanItemContainer<SelectValue> recievedFrom){
		
		cbxRepudiation.setContainerDataSource(repudiationMasterValueByCode);
		cbxRepudiation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRepudiation.setItemCaptionPropertyId("value");
		
		cmbMovedTo.setContainerDataSource(moveToMasterValueByCode);
		cmbMovedTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMovedTo.setItemCaptionPropertyId("value");
		
		cmbPendingLevel.setContainerDataSource(pendingLevelMasterValueByCode);
		cmbPendingLevel.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPendingLevel.setItemCaptionPropertyId("value");
		
		cbxRecievedFrom.setReadOnly(false);
		cbxRecievedFrom.setContainerDataSource(recievedFrom);
		cbxRecievedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRecievedFrom.setItemCaptionPropertyId("value");
		cbxRecievedFrom.setReadOnly(true);
	}
	
	public void addCLickListener() {
		btnIntimationSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();

				popup.setWidth("75%");
				popup.setHeight("90%");
				consumerFormSearchUI.setPopup(popup, SHAConstants.LEGAL_ADVOCATE_NOTICE);
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
					fireViewEvent(SearchProcessAdvocateNoticePresenter.LEGAL_SAVE_ADVOCATE_NOTICE,advocateNoticeDTO);
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
					fireViewEvent(SearchProcessAdvocateNoticePresenter.LEGAL_SAVE_ADVOCATE_NOTICE, advocateNoticeDTO);
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
			advocateNoticeDTO.setUserName(userName);
			
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
		
		/*
		// TODO Auto-generated method stub
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			advocateNoticeDTO.setUserName(userName);
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
	
	public void populateFieldValues(Claim claim, LegalAdvocate legalAdvocate,String diagnosisName) {
		
		if(legalAdvocate==null){
			txtIntimationNo.setValue(claim.getIntimation().getIntimationId());
			txtPolicyNo.setValue(claim.getIntimation().getPolicy().getPolicyNumber());
			txtProductNameCode.setValue(claim.getIntimation().getPolicy().getProductName());
			txtfinancialYear.setValue(claim.getIntimation().getPolicy().getPolicyYear().toString());
			if(claim.getCurrentProvisionAmount()!=null){
				txtprovisionAmt.setValue(claim.getCurrentProvisionAmount().toString());
			}
			txtInsuredName.setValue(claim.getIntimation().getInsured().getInsuredName());
			txtDiagnosis.setReadOnly(Boolean.FALSE);
			txtDiagnosis.setValue(diagnosisName);
			txtDiagnosis.setReadOnly(Boolean.TRUE);
			viewDetails.initView(claim.getIntimation().getIntimationId(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_ADVOCATE_NOTICE);
			IntimationDetailsDTO intimationDetailsDTO = advocateNoticeDTO.getIntimationDetailsDTO();
			intimationDetailsDTO.setClaimKey(claim);
		}
		
		if(legalAdvocate!=null){
			viewDetails.initView(claim.getIntimation().getIntimationId(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_ADVOCATE_NOTICE);
			txtIntimationNo.setValue(legalAdvocate.getIntimationNumber());
			txtPolicyNo.setValue(legalAdvocate.getPolicyNumber());
			txtProductNameCode.setValue(legalAdvocate.getProductName());
			if(legalAdvocate.getFinancialYear()!=null){
				txtfinancialYear.setValue(legalAdvocate.getFinancialYear().toString());
			}
			if(legalAdvocate.getProvisionAmount()!=null){
				txtprovisionAmt.setValue(legalAdvocate.getProvisionAmount().toString());
			}
			txtDiagnosis.setReadOnly(Boolean.FALSE);
			txtDiagnosis.setValue(diagnosisName);
			txtDiagnosis.setReadOnly(Boolean.TRUE);
			txtInsuredName.setValue(legalAdvocate.getInsuredName());
			MastersValue repudiationId = legalAdvocate.getRepudiationId();
			if(repudiationId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(repudiationId.getKey());
				selectValue.setValue(repudiationId.getValue());
				cbxRepudiation.setValue(selectValue);
			}
			MastersValue recievedFrom = legalAdvocate.getReceievedFrom();
			if(recievedFrom!=null){
				cbxRecievedFrom.setReadOnly(false);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(recievedFrom.getKey());
				selectValue.setValue(recievedFrom.getValue());
				cbxRecievedFrom.setValue(selectValue);
				cbxRecievedFrom.setReadOnly(true);
			}
			IntimationDetailsDTO intimationDetailsDTO = advocateNoticeDTO.getIntimationDetailsDTO();
			intimationDetailsDTO.setClaimKey(claim);
			txtStatus.setValue(legalAdvocate.getAdvstatus());
			txtadvocatename.setValue(legalAdvocate.getAdvocateName());
			txtLimitationTime.setValue(legalAdvocate.getLimitDate());
			chkToStandRejection.setValue(legalAdvocate.getRejectFlag());
			if(legalAdvocate.getRejectFlag()!=null){
			if(legalAdvocate.getRejectFlag()){
				txtDateReplySent.setValue(legalAdvocate.getRepliedDate());
				txtAdvocateReason.setValue(legalAdvocate.getRejectReason());
			}else{
				txtSettleDate.setValue(legalAdvocate.getSettleDate());
				if(legalAdvocate.getSettleAmt()!=null){
					txtSettleAmt.setValue(legalAdvocate.getSettleAmt().toString());
				}
				txtSettleReason.setValue(legalAdvocate.getSettleReason());
			}}
		
			if(legalAdvocate.getMovedId()!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(legalAdvocate.getMovedId().getKey());
				selectValue.setValue(legalAdvocate.getMovedId().getValue());
				cmbMovedTo.setValue(selectValue);
				
			}
			if(legalAdvocate.getPendingLevelId()!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(legalAdvocate.getPendingLevelId().getKey());
				selectValue.setValue(legalAdvocate.getPendingLevelId().getValue());
				cmbPendingLevel.setValue(selectValue);
			}
			dateAdvocateNoticeDate.setValue(legalAdvocate.getAdvnoticeDate());
			dateNoticeRecievedDate.setValue(legalAdvocate.getNoticerecDate());
			//dateComplainceDate.setValue(legalAdvocate.getComplainceDate());
			//chkLegalLock.setValue(legal.get);
			//chkLegalCompleted.setValue();
			
			//chkToSettle.setValue(legalAdvocate.getSettleFlag());
			chkLegalLock.setValue(legalAdvocate.getLegalLockFlag());
		}
		
	}
	
	private void doNumberValidation(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	
	
	private void noticeRecievedDateListener(final DateField noticeRecievedDate , final DateField limitationDate) {
		if(noticeRecievedDate!=null){
			
			
			noticeRecievedDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
					Date noticeDate = noticeRecievedDate.getValue();
					doAddDaysCalculation(noticeDate, SHAConstants.LEGAL_ADD_DAYS_LIMITATION, limitationDate);
				}
			}

			
		});}
	}
	
	private Date doAddDaysCalculation(Date noticeRecievedDate, String addDays,DateField limitationDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(noticeRecievedDate); // Now use today date.
		c.add(Calendar.DATE, Integer.parseInt(addDays)); // Adding 5 days
		String output = sdf.format(c.getTime());
		limitationDate.setReadOnly(false);
		limitationDate.setValue(c.getTime());
		limitationDate.setReadOnly(true);
		System.out.println(output);
		return c.getTime();
	}
	
	private void isStandRejectionListener(OptionGroup standRejection, final VerticalLayout mainVerticalLayout) {
		if(standRejection!=null){
			
			
		standRejection.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
							int componentCount = mainVerticalLayout.getComponentCount();
					     	if(mainVerticalLayout.getComponentCount() > 2){
					     		mainVerticalLayout.removeComponent(mainVerticalLayout.getComponent(componentCount - 1));
					     		unbindField(txtDateReplySent, caseDetailBinder);
							 	unbindField(txtAdvocateReason, caseDetailBinder);
						     	unbindField(txtSettleDate, caseDetailBinder);
							 	unbindField(txtSettleAmt, caseDetailBinder);
							 	unbindField(txtSettleReason, caseDetailBinder);
							 	mandatoryFields.remove(txtDateReplySent);
							 	mandatoryFields.remove(txtSettleDate);
								mandatoryFields.remove(txtSettleAmt);
					     	}
					    	
							showOrHideValidation(false);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 
							 	txtDateReplySent = caseDetailBinder.buildAndBind("Reply Sent Date","replySentDate",DateField.class);
							 	txtAdvocateReason = caseDetailBinder.buildAndBind("Reason","rejectionReason",TextArea.class);
							 	txtAdvocateReason.setMaxLength(1000);
							 	txtDateReplySent.setValidationVisible(false);
							 	txtAdvocateReason.setValidationVisible(false);
							 	txtAdvocateReason.removeAllValidators();
							 	txtDateReplySent.setRequired(true);
								mandatoryFields.add(txtDateReplySent);
							 	VerticalLayout verticalLayout = new VerticalLayout(txtDateReplySent,txtAdvocateReason);
							 	verticalLayout.setMargin(true);
							 	verticalLayout.setWidth("100%");	
							 	verticalLayout.setSpacing(Boolean.TRUE);
							 	mainVerticalLayout.addComponent(verticalLayout);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 	
								txtSettleDate = caseDetailBinder.buildAndBind("Settle Date","settleDate",DateField.class);
								txtSettleAmt = caseDetailBinder.buildAndBind("Settle Amount","settleAmt",TextField.class);
								txtSettleReason = caseDetailBinder.buildAndBind("Reason","settleReason",TextArea.class);
								txtSettleReason.setMaxLength(1000);
								txtSettleAmt.setNullRepresentation("");
								doNumberValidation(txtSettleAmt);
								txtSettleDate.setValidationVisible(false);
								txtSettleAmt.setValidationVisible(false);
								txtSettleReason.setValidationVisible(false);
								txtSettleAmt.setRequired(true);
								txtSettleDate.setRequired(true);
								mandatoryFields.add(txtSettleDate);
								mandatoryFields.add(txtSettleAmt);
								VerticalLayout verticalLayout2 = new VerticalLayout(txtSettleDate,txtSettleAmt,txtSettleReason);
								verticalLayout2.setMargin(true);
								verticalLayout2.setWidth("100%");	
								verticalLayout2.setSpacing(Boolean.TRUE);
								mainVerticalLayout.addComponent(verticalLayout2);
						 }
						
						
				}
			}

			
		});}
	}
}

