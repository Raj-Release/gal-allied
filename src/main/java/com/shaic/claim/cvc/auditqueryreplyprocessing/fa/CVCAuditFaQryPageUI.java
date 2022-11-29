package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.gargoylesoftware.htmlunit.javascript.host.Set;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryapproval.AuditBillingFaQueryTable;
//import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.CVCAuditClsQryPagePresenter;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;


public class CVCAuditFaQryPageUI extends ViewComponent {
	
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	
	private TextArea claimAuditQueryRemarks; 
	private TextArea claimsReplyRemarks; 
	private PopupDateField queryRaisedDt ;
	
	private TextField claimsReplyBy;
	private TextField claimsReplyRole;
	private PopupDateField claimsReplyDt;
	
	private TextArea adlQueryRemarks; 
	private TextArea adlQryReplyRemarks; 
	private PopupDateField adQueryRaisedDt ;

	
//	private TextArea txtAuditQryRemarks; claimAuditQueryRemarks
	
//	private ComboBox cmbAuditStatus;
	
//	private ComboBoxMultiselect cmbCategoryOfError;
	
//	private ComboBoxMultiselect cmbTeam;
	
//	private ComboBoxMultiselect cmbProcessor;
	
//	private ComboBox cmbMonetaryResult;
	
//	private TextField amountInvolved;
	
//	private ComboBox cmbAuditRemediationStatus;
	
//	private TextArea txtRemediationRemarks;

	private Button submitBtn;
	
    private Button cancelBtn;
    
//    private ComboBox cmbAuditFinalStatus;
    
    private HorizontalLayout buttonHorLayout;
    
    private Searchable searchable;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private ViewDetails viewDetails;
		
	private SearchCVCAuditActionTableDTO bean;
	
	PreauthDTO preauthDTO = null;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	FormLayout remarksFLayout;
	FormLayout cmbLeftFLayout;
	FormLayout cmbRightFLayout;
	FormLayout cmbRightFLayout1;
	FormLayout cmbRightFLayout2;
	FormLayout cmbLastRightFLayout;
	HorizontalLayout horizontalLayout;
	VerticalLayout verticalLayout;
	
	
	@Inject
	private Instance<AuditBillingFaQueryTable> billingFaQryTableInstance;
	private AuditBillingFaQueryTable billingFaQryTable;
	
//	private TextArea txtOtherRemarks;

//	private TextArea txtClsQryRemarks;
//	private TextArea txtReimbQryRemarks;
//	private TextArea txtBillingFAQryRemarks;
	
//	private Button btnQryAccpt;
	
//	private Button btnQryNotAccpt;
	
//	private String clsQryRemarks;
//	private String reimbQryRemarks;
//	private String billingFAQryRemarks;
	
	public void init(SearchCVCAuditActionTableDTO bean) {
		this.bean = bean;
		setCompleteLayout();
	}
	
	public void init(SearchCVCAuditActionTableDTO bean,PreauthDTO preauthDTO) {
		this.bean = bean;
		this.preauthDTO = preauthDTO;
	}

	/**
	 * 
	 */
	public void setCompleteLayout() {
		
		preauthIntimationDetailsCarousel.init(bean.getPreauthDto().getNewIntimationDTO(), bean.getPreauthDto().getClaimDTO(), "Claim Audit Query (FA)");
		
		billingFaQryTable = billingFaQryTableInstance.get();
		billingFaQryTable.init(SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN, bean);
		if(bean.getBillingFaQryList() != null && !bean.getBillingFaQryList().isEmpty()){
			List<SearchCVCAuditClsQryTableDTO> billingFaQryList = bean.getBillingFaQryList();
			for (SearchCVCAuditClsQryTableDTO auditQryDTO : billingFaQryList) {
				this.billingFaQryTable.addBeanToList(auditQryDTO);
			}
		}
		/*
		claimAuditQueryRemarks = new TextArea("Claim Audit Query Remarks");
		claimAuditQueryRemarks.setWidth("700px");
//		claimAuditQueryRemarks.setHeight("190px");
		claimAuditQueryRemarks.setRows(5);
		claimAuditQueryRemarks.setId("clmAudQry");
		claimAuditQueryRemarks.setValue(bean.getBillinFaAuditQryRemarks());
		claimAuditQueryRemarks.setReadOnly(true);
		claimAuditQueryRemarks.setMaxLength(4000);

		claimAuditQueryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		claimAuditQueryRemarks.setEnabled(false);
		handleTextAreaPopup(claimAuditQueryRemarks,null);
		
		claimsReplyRemarks = new TextArea("Claims Reply Remarks");
		claimsReplyRemarks.setRows(5);
		claimsReplyRemarks.setWidth("700px");
//		claimsReplyRemarks.setHeight("190px");
		claimsReplyRemarks.setId("clmAudQryRpl");
		claimsReplyRemarks.setMaxLength(4000);

		claimsReplyRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
//		claimsReplyRemarks.setEnabled(false);
		handleTextAreaPopup(claimsReplyRemarks,null);
		
		queryRaisedDt = new PopupDateField("Query Raised Dt & Time ");
		queryRaisedDt.setEnabled(false);
		queryRaisedDt.setDateFormat("dd/MM/yyyy HH:mm");
		queryRaisedDt.setResolution(Resolution.MINUTE);
		queryRaisedDt.setLocale(new Locale("en", "EN"));
//		queryRaisedDt.setValue(new Date());   TODO
		
		Panel mainPanel = new Panel();
	
		
		adlQueryRemarks = new TextArea("Claim Audit Query Remarks");
		adlQueryRemarks.setWidth("100%");
//		claimAuditQueryRemarks.setHeight("190px");
		adlQueryRemarks.setRows(5);
		adlQueryRemarks.setValue(bean.getClsAuditAdlQryRemarks());
		adlQueryRemarks.setReadOnly(true);
		adlQueryRemarks.setId("clmAudAdQry");
		adlQueryRemarks.setMaxLength(4000);
		adlQueryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(adlQueryRemarks,null);
		
		adlQryReplyRemarks = new TextArea("Claims Reply for Additional Query");
		adlQryReplyRemarks.setRows(5);
		adlQryReplyRemarks.setWidth("100%");
//		claimsReplyRemarks.setHeight("190px");
		adlQryReplyRemarks.setId("clmAudAdQryRpl");
		adlQryReplyRemarks.setMaxLength(4000);

		adlQryReplyRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
//		claimsReplyRemarks.setEnabled(false);
		handleTextAreaPopup(adlQryReplyRemarks,null);
		
		adQueryRaisedDt = new PopupDateField("Query Raised Dt & Time ");
		adQueryRaisedDt.setEnabled(false);
		adQueryRaisedDt.setDateFormat("dd/MM/yyyy HH:mm");
		adQueryRaisedDt.setResolution(Resolution.MINUTE);
		adQueryRaisedDt.setLocale(new Locale("en", "EN"));
//		.setValue(); TODO
		
		HorizontalLayout fieldHLayout = new HorizontalLayout(adlQueryRemarks,adQueryRaisedDt,adlQryReplyRemarks);
		fieldHLayout.setSpacing(true);
		fieldHLayout.setWidth("100%");
		VerticalLayout form = new VerticalLayout(fieldHLayout);
		form.setComponentAlignment(fieldHLayout, Alignment.MIDDLE_CENTER);
		form.setSpacing(true);
		form.setMargin(true);
		mainPanel.setCaption("Addtional Query");
		mainPanel.setContent(form);
		mainPanel.setHeight("100%");
		mainPanel.setWidth("100%");
		
		*/
		
		/*cmbAuditStatus = new ComboBox("Audit Status");
		cmbAuditStatus.setNullSelectionAllowed(Boolean.FALSE);
		cmbAuditStatus.setEnabled(false);
		
		cmbCategoryOfError = new ComboBoxMultiselect("Category of Error");
		cmbCategoryOfError.setShowSelectedOnTop(true);
//		cmbCategoryOfError.setNullSelectionAllowed(Boolean.FALSE);
		cmbCategoryOfError.setEnabled(true);
		
		cmbTeam = new ComboBoxMultiselect("Team");
		cmbTeam.setShowSelectedOnTop(true);
//		cmbTeam.setNullSelectionAllowed(Boolean.FALSE);
		cmbTeam.setEnabled(true);
		
		cmbProcessor = new ComboBoxMultiselect("Processor");
		cmbProcessor.setShowSelectedOnTop(true);
//		cmbProcessor.setNullSelectionAllowed(Boolean.FALSE);
		cmbProcessor.setEnabled(true);
		
		cmbMonetaryResult = new ComboBox("Audit Result");
		cmbMonetaryResult.setNullSelectionAllowed(Boolean.TRUE);
		cmbMonetaryResult.setEnabled(true);
		
		amountInvolved = new TextField("Amount Involved");
		CSValidator validator = new CSValidator();
		validator.extend(amountInvolved);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(false);
		amountInvolved.setNullRepresentation("0");
		amountInvolved.setEnabled(true);
		
		cmbAuditRemediationStatus = new ComboBox("Audit Remediation Status");
		cmbAuditRemediationStatus.setNullSelectionAllowed(Boolean.TRUE);
		cmbAuditRemediationStatus.setEnabled(true);
		
		txtRemediationRemarks = new TextArea("Remediation Remarks");
		txtRemediationRemarks.setMaxLength(4000);
		txtRemediationRemarks.setEnabled(true);
		txtRemediationRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtRemediationRemarks,null);
		
		cmbAuditFinalStatus = new ComboBox("Audit Final Status");
		cmbAuditFinalStatus.setNullSelectionAllowed(Boolean.TRUE);
		cmbAuditFinalStatus.setEnabled(true);
		
		if(null != txtRemediationRemarks && txtRemediationRemarks.getValue() != null){
//			bean.setRemediationRemarks(txtRemediationRemarks.getValue());
		}*/
	
//		fireViewEvent(CVCAuditClsQryPagePresenter.LOAD_CVC_AUDIT_ACTION_STATUS_VALUES, bean);
		
		/*txtOtherRemarks = new TextArea("Other Remarks");
		txtOtherRemarks.setMaxLength(4000);
		txtOtherRemarks.setEnabled(true);
		txtOtherRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtOtherRemarks,null);*/
	/*	if(!StringUtils.isBlank(bean.getOtherRemarks())){
			txtOtherRemarks.setValue(bean.getOtherRemarks());
			txtOtherRemarks.setVisible(true);
			setRequiredAndValidation(txtOtherRemarks);
		}else{
			txtOtherRemarks.setVisible(false);
		}*/
		
		/*txtClsQryRemarks = new TextArea("Cashless Medical Query Remarks");
		txtClsQryRemarks.setMaxLength(4000);
		txtClsQryRemarks.setEnabled(true);
		txtClsQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtClsQryRemarks,null);*/
		/*if(!StringUtils.isBlank(bean.getClsQryRemarks())){
			txtClsQryRemarks.setValue(bean.getClsQryRemarks());
			txtClsQryRemarks.setVisible(true);
//			setRequiredAndValidation(txtClsQryRemarks);
		}else{
			txtClsQryRemarks.setVisible(false);
		}*/
		
		/*txtReimbQryRemarks = new TextArea("Reimbursement Medical Query Remarks");
		txtReimbQryRemarks.setMaxLength(4000);
		txtReimbQryRemarks.setEnabled(true);
		txtReimbQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtReimbQryRemarks,null);*/
	/*	if(!StringUtils.isBlank(bean.getReimbQryRemarks())){
			txtReimbQryRemarks.setValue(bean.getReimbQryRemarks());
			txtReimbQryRemarks.setVisible(true);
//			setRequiredAndValidation(txtReimbQryRemarks);
		}else{
			txtReimbQryRemarks.setVisible(false);
		}*/

		/*txtBillingFAQryRemarks = new TextArea("Billing / FA Audit Query Remarks");
		txtBillingFAQryRemarks.setMaxLength(4000);
		txtBillingFAQryRemarks.setEnabled(true);
		txtBillingFAQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtBillingFAQryRemarks,null);*/
	/*	if(!StringUtils.isBlank(bean.getBillingFAQryRemarks())){
			txtBillingFAQryRemarks.setValue(bean.getBillingFAQryRemarks());
			txtBillingFAQryRemarks.setVisible(true);
//			setRequiredAndValidation(txtBillingFAQryRemarks);
		}else{
			txtBillingFAQryRemarks.setVisible(false);
		}*/
		
		// viewDetails
		/*if (null != bean && null != bean.getClaimType() && bean.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_STRING)) {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),0l, ViewLevels.INTIMATION, false,"Audit Action Processing");
		}else {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),bean.getTransactionKey(), ViewLevels.INTIMATION, false,"Audit Action Processing");
		}*/

		viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(), bean.getTransactionKey(), ViewLevels.INTIMATION, false,"Claim Audit Query (FA)");

		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setSizeFull();		
						
	/*	btnQryAccpt = new Button ("Query Accepted");
		btnQryAccpt.setIcon(new ThemeResource("images/yesstatus.png"));
		btnQryAccpt.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		btnQryNotAccpt = new Button ("Query Not Accepted");
		btnQryNotAccpt.setIcon(new ThemeResource("images/cross_mark_black.jpg"));
		btnQryNotAccpt.setStyleName(ValoTheme.BUTTON_BORDERLESS);*/

//		remarksFLayout = new FormLayout(claimAuditQueryRemarks,queryRaisedDt,claimsReplyRemarks);
		
	/*	cmbLeftFLayout = new FormLayout(cmbTeam,cmbMonetaryResult,cmbAuditRemediationStatus,cmbAuditFinalStatus,txtRemediationRemarks);
		cmbRightFLayout = new FormLayout(cmbCategoryOfError,cmbProcessor,amountInvolved, btnQryAccpt, btnQryNotAccpt);
		
		cmbRightFLayout = new FormLayout(btnQryAccpt, btnQryNotAccpt);

		cmbLastRightFLayout = new FormLayout(txtOtherRemarks);
		cmbRightFLayout1 = new FormLayout(txtClsQryRemarks, txtReimbQryRemarks, txtBillingFAQryRemarks);*/
		
	
//		horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout,cmbLastRightFLayout);
//		horizontalLayout.setSpacing(true);
//		horizontalLayout.setMargin(true);
		
//		verticalLayout = new VerticalLayout(remarksFLayout,horizontalLayout);//		
		verticalLayout = new VerticalLayout(billingFaQryTable);
		



		verticalLayout.setSpacing(false);
		verticalLayout.setMargin(true);
		
		
		
	    submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
	
		buttonHorLayout.setSpacing(true);
		
		
		mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel, viewDetailsLayout,verticalLayout,buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		
		addListener();
		
/*		setRequiredAndValidation(claimAuditQueryRemarks);
		setRequiredAndValidation(cmbAuditStatus);
		setRequiredAndValidation(cmbCategoryOfError);
		setRequiredAndValidation(cmbTeam);
		setRequiredAndValidation(cmbProcessor);
		setRequiredAndValidation(cmbMonetaryResult);
		setRequiredAndValidation(amountInvolved);
		setRequiredAndValidation(cmbAuditRemediationStatus);
		setRequiredAndValidation(txtRemediationRemarks);
		setRequiredAndValidation(cmbAuditFinalStatus);
		setRequiredAndValidation(txtOtherRemarks);*/
		
	/*	mandatoryFields.remove(claimAuditQueryRemarks);
		mandatoryFields.remove(cmbAuditStatus);
		mandatoryFields.remove(cmbCategoryOfError);
		mandatoryFields.remove(cmbTeam);
		mandatoryFields.remove(cmbProcessor);
		mandatoryFields.remove(cmbMonetaryResult);
		mandatoryFields.remove(amountInvolved);
		mandatoryFields.remove(cmbAuditRemediationStatus);
		mandatoryFields.remove(txtRemediationRemarks);
		mandatoryFields.remove(cmbAuditFinalStatus);
		mandatoryFields.remove(txtOtherRemarks);
		mandatoryFields.add(claimAuditQueryRemarks);
		mandatoryFields.add(cmbAuditStatus);
		mandatoryFields.add(cmbCategoryOfError);
		mandatoryFields.add(cmbTeam);
		mandatoryFields.add(cmbProcessor);
		mandatoryFields.add(cmbMonetaryResult);
		mandatoryFields.add(amountInvolved);
		mandatoryFields.add(cmbAuditRemediationStatus);
		mandatoryFields.add(txtRemediationRemarks);
		mandatoryFields.add(cmbAuditFinalStatus);
		mandatoryFields.add(txtOtherRemarks);*/
		
		setCompositionRoot(mainLayout);
	}
	
	@SuppressWarnings("deprecation")
	private void addListener(){
				
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
//				                	fireViewEvent(CVCAuditClsQryPagePresenter.CANCEL_EVENT_AUDIT_ACTION, bean);
				                	releaseHumanTask();
				                	fireViewEvent(MenuItemBean.CVC_AUDIT_FA_QRY_RLY, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					/*if(null != txtRemediationRemarks && txtRemediationRemarks.getValue() != null){
						bean.setRemediationRemarks(txtRemediationRemarks.getValue());
					}*/
					
					bean.setAuditStatus(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);
					
					fireViewEvent(CVCAuditFaQryPagePresenter.SUBMIT_EVENT_AUDIT_QUERY_FA, bean);
				}

		}
		});
		
	/*
		
		cmbAuditStatus.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue auditStatus = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				
				if (auditStatus != null) {
//					bean.setAuditStatus(auditStatus.getValue());
				}
			}
		});
		
		cmbCategoryOfError.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String auditCategoryError = event.getProperty().getValue().toString();
				
				if (auditCategoryError != null && auditCategoryError.contains(SHAConstants.CVC_OTHERS_MAS_KEY) 
						&& null != cmbAuditStatus && null != cmbAuditStatus.getValue()
						&& cmbAuditStatus.getValue().toString().equalsIgnoreCase(SHAConstants.ERROR_STRING)){

					txtOtherRemarks.setVisible(true);
					
					if(null != cmbCategoryOfError){
//					bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
					}
					
				}else {

					txtOtherRemarks.setValue("");
					txtOtherRemarks.setVisible(false);

					if(null != cmbCategoryOfError){
//					bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
					}
					
				}
				if(null != cmbCategoryOfError){
				bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
				}
			}
		});
		
		cmbTeam.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbTeam){
//				bean.setTeam(cmbTeam.getValue().toString());
				//TODO CR2019181 AUDIT QRY REMARKS TO BE IMPLEMENTED
				}
			}
		});
		
		cmbProcessor.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbProcessor){
//				bean.setProcessor(cmbProcessor.getValue().toString());
				}
			}
		});
		
		cmbMonetaryResult.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbMonetaryResult){
//				bean.setMonetaryResult(cmbMonetaryResult.getValue().toString());
				}
			}
		});
		
		amountInvolved.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != amountInvolved && amountInvolved.getValue() != null && !amountInvolved.isEmpty()
						&& bean.getClaimedAmount() != null){
					double involvedAmt = Double.parseDouble(amountInvolved.getValue());
						if (involvedAmt > Double.parseDouble(bean.getClaimedAmount())) {
							showErrorPopUp("Please Enter Amount Less than the Amount Claimed </br>");
							amountInvolved.setValue(null);
							amountInvolved.setNullRepresentation("");
						}else{
							bean.setAmountInvolved(amountInvolved.getValue());
						}
				}
			}
		});
		
		cmbAuditRemediationStatus.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cmbAuditRemediationStatus.getValue() != null 
						&& cmbAuditRemediationStatus.getValue().toString().equalsIgnoreCase(SHAConstants.COMPLETED_STRING)) {
					setRequiredAndValidation(cmbAuditFinalStatus);
					mandatoryFields.remove(cmbAuditFinalStatus);
					mandatoryFields.add(cmbAuditFinalStatus);
				}else {
					setNotRequiredAndValidation(cmbAuditFinalStatus);
					mandatoryFields.remove(cmbAuditFinalStatus);
				}
				if(null != cmbAuditRemediationStatus){
//				bean.setRemediationStatus(cmbAuditRemediationStatus.getValue().toString());
				}
			}
		});
		
		cmbAuditFinalStatus.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue auditFinalStatus = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				
				if (auditFinalStatus != null) {
//					bean.setAuditFinalStatus(auditFinalStatus.getValue());
				}
			}
		});
		
		txtOtherRemarks.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != txtOtherRemarks){
//				bean.setOtherRemarks(txtOtherRemarks.getValue());
				}
			}
		});
		
		amountInvolved.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != amountInvolved && amountInvolved.getValue() != null && !amountInvolved.isEmpty()
						&& bean.getClaimedAmount() != null){
					double involvedAmt = Double.parseDouble(amountInvolved.getValue());
						if (involvedAmt > Double.parseDouble(bean.getClaimedAmount())) {
							showErrorPopUp("Please Enter Amount Less than the Amount Claimed </br>");
							amountInvolved.setValue(null);
							amountInvolved.setNullRepresentation("");
						}else{
							bean.setAmountInvolved(amountInvolved.getValue());
						}
				}
			}
		});
		*/
	}
	
	private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
 		}
 		
 		if(wrkFlowKey != null){
 			DBCalculationService dbService = new DBCalculationService();
 			dbService.callUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
	public void showErrorPopUp(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(UploadInvestigationReportPresenter.VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}
	
public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
	
		/*if (claimAuditQueryRemarks != null
				&& claimAuditQueryRemarks.getValue() == null
				|| (claimAuditQueryRemarks.getValue() != null && claimAuditQueryRemarks
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Audit Remarks </br>");
		}*/

		/*if (cmbAuditStatus != null
				&& cmbAuditStatus.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Audit Status </br>");
		}
		if ((cmbTeam != null && cmbTeam.getValue() == null)
				|| cmbTeam.isEmpty()) {
			hasError = true;
			eMsg.append("Please Select Team </br>");
		}
		if ((cmbCategoryOfError != null && cmbCategoryOfError.getValue() == null)
				|| cmbCategoryOfError.isEmpty()) {
			hasError = true;
			eMsg.append("Please Select Category of Error </br>");
		}
		if (cmbMonetaryResult != null
				&& cmbMonetaryResult.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Monetary Result </br>");
		}
		if ((cmbProcessor != null && cmbProcessor.getValue() == null)
				|| cmbProcessor.isEmpty()) {
			hasError = true;
			eMsg.append("Please Select Processor </br>");
		}
		if (cmbAuditRemediationStatus != null
				&& cmbAuditRemediationStatus.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Audit Remediation Status </br>");
		}
		if (amountInvolved != null
				&& amountInvolved.getValue() == null
				|| (amountInvolved.getValue() != null && amountInvolved
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Amount Involved </br>");
		}
		if (txtRemediationRemarks != null
				&& txtRemediationRemarks.getValue() == null
				|| (txtRemediationRemarks.getValue() != null && txtRemediationRemarks
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Remediation Remarks </br>");
		}
		if (cmbAuditFinalStatus != null
				&& cmbAuditFinalStatus.getValue() == null && cmbAuditRemediationStatus.getValue() != null 
				&& cmbAuditRemediationStatus.getValue().toString().equalsIgnoreCase(SHAConstants.COMPLETED_STRING)) {
			hasError = true;
			eMsg.append("Please Select Audit Final Status </br>");
		}
		if ((txtOtherRemarks != null
				&& txtOtherRemarks.getValue() == null
				|| (txtOtherRemarks.getValue() != null && txtOtherRemarks.getValue().length() == 0)) 
				&& cmbCategoryOfError.getValue().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
			hasError = true;
			eMsg.append("Please Enter Other Remarks </br>");
		}*/
		
		if(billingFaQryTable != null && billingFaQryTable.getValues() != null && !billingFaQryTable.getValues().isEmpty()) {
			hasError = !billingFaQryTable.isValid();
			if(hasError && !billingFaQryTable.getErrors().isEmpty()) {
				eMsg.append(billingFaQryTable.getErrors());
			}
		}

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		}
		else {
			if(billingFaQryTable != null && billingFaQryTable.getValues() != null) {
				bean.setBillingFaQryList(billingFaQryTable.getValues());
			}
		}
		
		return true;
				
	}

@SuppressWarnings("unused")
private void setRequiredAndValidation(Component component) {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	AbstractField<Field> field = (AbstractField<Field>) component;
	field.setRequired(true);
	field.setValidationVisible(false);
}

/*@SuppressWarnings("unused")
private void setRequired(Boolean isRequired) {

	if (!mandatoryFields.isEmpty()) {
		for (int i = 0; i < mandatoryFields.size(); i++) {
			AbstractField<?> field = (AbstractField<?>) mandatoryFields
					.get(i);
			field.setRequired(isRequired);
		}
	}
}*/

public void loadCVCStatusDropDownValues(AuditDetails auditDetails, 
		BeanItemContainer<SelectValue> remediationStatusValueContainer, BeanItemContainer<SelectValue> statusValueContainer, 
		List<AuditTeam> auditTeamList, List<AuditCategory> auditCategoryList, List<AuditProcessor> auditProcessorList, 
		BeanItemContainer<SelectValue> cmbCategoryContainer, BeanItemContainer<SelectValue> TeamValueContainer, 
		BeanItemContainer<SelectValue> ErrorValueContainer, BeanItemContainer<SelectValue> processorValueContainer, 
		BeanItemContainer<SelectValue> monetaryValueContainer) {
	
	if(auditDetails!=null && auditDetails.getAuditRemarks()!=null){
		claimAuditQueryRemarks.setValue(auditDetails.getAuditRemarks());
//		bean.setAuditRemarks(auditDetails.getAuditRemarks().toString());
	}
	
	/*BeanItemContainer<SelectValue> cmbAuditStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	SelectValue cmbAuditStatustemp = new SelectValue();
	cmbAuditStatustemp.setValue(auditDetails.getAuditStatus());
	cmbAuditStatusContainer.addBean(cmbAuditStatustemp);
	cmbAuditStatus.setContainerDataSource(cmbAuditStatusContainer);
	cmbAuditStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbAuditStatus.setItemCaptionPropertyId("value");
	cmbAuditStatus.setValue(cmbAuditStatusContainer.getItemIds().get(0));
//	bean.setAuditStatus(auditDetails.getAuditStatus().toString());
	
	BeanItemContainer<SelectValue> cmbAuditFinalStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if (null != auditDetails && auditDetails.getFinalAuditStatus() != null) {
		SelectValue cmbAuditFinalStatustemp = new SelectValue();
		cmbAuditFinalStatustemp.setValue(auditDetails.getFinalAuditStatus());
		cmbAuditFinalStatusContainer.addBean(cmbAuditFinalStatustemp);
		cmbAuditFinalStatus.setContainerDataSource(statusValueContainer);
		cmbAuditFinalStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditFinalStatus.setItemCaptionPropertyId("value");
//		cmbMonetaryResult.setValue(cmbMonetaryResulttemp);
		for(int i=0;i<statusValueContainer.size();i++){
			if ((auditDetails.getFinalAuditStatus()).equalsIgnoreCase(statusValueContainer.getIdByIndex(i).getValue())){
				cmbAuditFinalStatus.setValue(statusValueContainer.getIdByIndex(i));
				bean.setAuditFinalStatus(cmbAuditFinalStatus.getValue().toString());
				break;
			}
		}
//		bean.setAuditFinalStatus(auditDetails.getFinalAuditStatus().toString());
	}else {
		cmbAuditFinalStatus.setContainerDataSource(statusValueContainer);
		cmbAuditFinalStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditFinalStatus.setItemCaptionPropertyId("value");
		cmbAuditFinalStatus.setValue(statusValueContainer.getItemIds());
	}
//	bean.setAuditFinalStatus(auditDetails.getAuditStatus().toString());
	
//	BeanItemContainer<SelectValue> cmbCategoryOfErrorContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if(auditCategoryList!=null && !auditCategoryList.isEmpty()){
		for (AuditCategory AuditCategory : auditCategoryList) {
//		SelectValue cmbCategoryOfErrortemp = new SelectValue();
//		cmbCategoryOfErrortemp.setValue(AuditCategory.getAuditCategory().toString());
//		cmbCategoryOfErrorContainer.addBean(cmbCategoryOfErrortemp);
			if (null != AuditCategory && AuditCategory.getAuditCategoryOthrRmks() != null && AuditCategory.getAuditCategory() != null  
					&& AuditCategory.getAuditCategory().toString().equalsIgnoreCase(SHAConstants.CVC_OTHERS_MAS_KEY)) {
//				bean.setOtherRemarks(AuditCategory.getAuditCategoryOthrRmks());
//			txtOtherRemarks.setValue(AuditCategory.getAuditCategoryOthrRmks());
			}
		}
	}
	
	HashSet<SelectValue> setCategoryValues =new HashSet<SelectValue>();
	if(auditCategoryList!=null && !auditCategoryList.isEmpty()){
		for(int i=0;i<ErrorValueContainer.size();i++){
			for (AuditCategory AuditCategory : auditCategoryList) {
				
				if ((AuditCategory.getAuditCategory().toString()).equalsIgnoreCase(ErrorValueContainer.getIdByIndex(i).getValue())){
					setCategoryValues.add((SelectValue)ErrorValueContainer.getIdByIndex(i));
					break;
				}
			}
		}
	}
	cmbCategoryOfError.setContainerDataSource(ErrorValueContainer);
	cmbCategoryOfError.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbCategoryOfError.setItemCaptionPropertyId("commonValue");
	cmbCategoryOfError.setValue(setCategoryValues);
//	bean.setErrorCategory(auditDetails.getErrorCategory().toString());

	BeanItemContainer<SelectValue> cmbTeamContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if(auditTeamList!=null && !auditTeamList.isEmpty()){
		for (AuditTeam AuditTeam : auditTeamList) {
			SelectValue cmbTeamtemp = new SelectValue();
			cmbTeamtemp.setValue(AuditTeam.getAuditTeam().toString());
			cmbTeamContainer.addBean(cmbTeamtemp);
		}
	}
	
	HashSet<SelectValue> setTeamValues =new HashSet<SelectValue>();
	if(auditTeamList!=null && !auditTeamList.isEmpty()){
		for(int i=0;i<TeamValueContainer.size();i++){
			for (AuditTeam AuditTeam : auditTeamList) {
				
				if ((AuditTeam.getAuditTeam().toString()).equalsIgnoreCase(TeamValueContainer.getIdByIndex(i).getValue())){
					setTeamValues.add((SelectValue)TeamValueContainer.getIdByIndex(i));
					break;
				}
			}
		}
	}
	
	cmbTeam.setContainerDataSource(TeamValueContainer);
	cmbTeam.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbTeam.setItemCaptionPropertyId("value");
	cmbTeam.setValue(setTeamValues);
	//cmbTeam.setValue(cmbTeamContainer.getItemIds());
//	bean.setTeam(auditDetails.getErrorTeam().toString());

	BeanItemContainer<SelectValue> cmbMonetaryResultContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if (null != auditDetails && auditDetails.getMonetaryResult() != null) {
		SelectValue cmbMonetaryResulttemp = new SelectValue();
		cmbMonetaryResulttemp.setValue(auditDetails.getMonetaryResult());
		cmbMonetaryResultContainer.addBean(cmbMonetaryResulttemp);
		cmbMonetaryResult.setContainerDataSource(monetaryValueContainer);
		cmbMonetaryResult.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMonetaryResult.setItemCaptionPropertyId("value");
//		cmbMonetaryResult.setValue(cmbMonetaryResulttemp);
		for(int i=0;i<monetaryValueContainer.size();i++){
			if ((auditDetails.getMonetaryResult()).equalsIgnoreCase(monetaryValueContainer.getIdByIndex(i).getValue())){
				cmbMonetaryResult.setValue(monetaryValueContainer.getIdByIndex(i));
//				bean.setMonetaryResult(cmbMonetaryResult.getValue().toString());
				break;
			}
		}
//		bean.setMonetaryResult(auditDetails.getMonetaryResult() != null ? auditDetails.getMonetaryResult().toString() : "");
	}else {
		cmbMonetaryResult.setContainerDataSource(monetaryValueContainer);
		cmbMonetaryResult.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMonetaryResult.setItemCaptionPropertyId("value");
		cmbMonetaryResult.setValue(monetaryValueContainer.getItemIds());
	}

	BeanItemContainer<SelectValue> cmbProcessorContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if(auditProcessorList!=null && !auditProcessorList.isEmpty()){
		for (AuditProcessor AuditProcessor : auditProcessorList) {
			SelectValue cmbProcessortemp = new SelectValue();
			cmbProcessortemp.setValue(AuditProcessor.getAuditProcessor().toString());
			cmbProcessorContainer.addBean(cmbProcessortemp);
		}
	}
	
	HashSet<SelectValue> setProcessorValues =new HashSet<SelectValue>();
	if(auditProcessorList!=null && !auditProcessorList.isEmpty()){
		for(int i=0;i<processorValueContainer.size();i++){
			for (AuditProcessor AuditProcessor : auditProcessorList) {
				
				if ((AuditProcessor.getAuditProcessor().toString()).equalsIgnoreCase(processorValueContainer.getIdByIndex(i).getValue())){
					setProcessorValues.add((SelectValue)processorValueContainer.getIdByIndex(i));
					break;
				}
			}
		}
	}
	cmbProcessor.setContainerDataSource(processorValueContainer);
	cmbProcessor.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbProcessor.setItemCaptionPropertyId("value");
	cmbProcessor.setValue(setProcessorValues);
//	bean.setProcessor(auditDetails.getErrorProcessor().toString());
	
	BeanItemContainer<SelectValue> cmbAuditRemediationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	if (null != auditDetails && auditDetails.getRemediationStatus() != null) {
		SelectValue cmbMonetaryResulttemp = new SelectValue();
		cmbMonetaryResulttemp.setValue(auditDetails.getRemediationStatus());
		cmbAuditRemediationContainer.addBean(cmbMonetaryResulttemp);
		cmbAuditRemediationStatus.setContainerDataSource(remediationStatusValueContainer);
		cmbAuditRemediationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditRemediationStatus.setItemCaptionPropertyId("value");
//		cmbAuditRemediationStatus.setValue(cmbMonetaryResulttemp);
		for(int i=0;i<remediationStatusValueContainer.size();i++){
			if ((auditDetails.getRemediationStatus()).equalsIgnoreCase(remediationStatusValueContainer.getIdByIndex(i).getValue())){
				cmbAuditRemediationStatus.setValue(remediationStatusValueContainer.getIdByIndex(i));
//				bean.setRemediationStatus(cmbAuditRemediationStatus.getValue().toString());
				break;
			}
		}
//		bean.setRemediationStatus(auditDetails.getRemediationStatus().toString());
	}else {
		cmbAuditRemediationStatus.setContainerDataSource(remediationStatusValueContainer);
		cmbAuditRemediationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditRemediationStatus.setItemCaptionPropertyId("value");
		cmbAuditRemediationStatus.setValue(remediationStatusValueContainer.getItemIds());
	}

	amountInvolved.setValue(auditDetails.getAmountInvolved() != null ? auditDetails.getAmountInvolved().toString() : "0");
//	bean.setAmountInvolved(auditDetails.getAmountInvolved() != null ? auditDetails.getAmountInvolved().toString() : "0");
	
	if (null != auditDetails && null != auditDetails.getRemediationRemarks()) {
		txtRemediationRemarks.setValue(auditDetails.getRemediationRemarks());
//		bean.setRemediationRemarks(auditDetails.getRemediationRemarks().toString());
	}*/
}

public void refresh()
{
	System.out.println("---inside the refresh----");
	resetAlltheValues();
}

/**
 * Method to reset all form values 
 *
 * */

public void resetAlltheValues() 
{
	
	Iterator<Component> componentIterator = mainLayout.iterator();
		while(componentIterator.hasNext()) 
		{
			Component searchScrnComponent = componentIterator.next() ;
			if(searchScrnComponent instanceof  Panel )
			{	
				Panel panel = (Panel)searchScrnComponent;
				Iterator<Component> searchScrnCompIter = panel.iterator();
				while (searchScrnCompIter.hasNext())
				{
					Component verticalLayoutComp = searchScrnCompIter.next();
					VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
					Iterator<Component> vLayoutIter = vLayout.iterator();
					while(vLayoutIter.hasNext())
					{
						Component absoluteComponent = vLayoutIter.next();
						AbsoluteLayout absLayout = (AbsoluteLayout)absoluteComponent;
						Iterator<Component> absLayoutIter = absLayout.iterator();
						while(absLayoutIter.hasNext())
						{
							Component horizontalComp = absLayoutIter.next();
							if(horizontalComp instanceof HorizontalLayout)
							{
								HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
								Iterator<Component> formLayComp = hLayout.iterator();
								while(formLayComp.hasNext())
								{
									Component formComp = formLayComp.next();
									FormLayout fLayout = (FormLayout)formComp;
									Iterator<Component> formComIter = fLayout.iterator();
								
									while(formComIter.hasNext())
									{
										Component indivdualComp = formComIter.next();
										if(indivdualComp != null) 
										{
											if(indivdualComp instanceof Label) 
											{
												continue;
											}	
											if(indivdualComp instanceof TextField) 
											{
												TextField field = (TextField) indivdualComp;
												field.setValue("");
											} 
											else if(indivdualComp instanceof ComboBox)
											{
												ComboBox field = (ComboBox) indivdualComp;
												field.setValue(null);
											}	 
								// Remove the table if exists..	
								//removeTableFromLayout();
										}
									}
								}
							}
						}
					}
				}
				//Method to reset search table.
				removeTableFromLayout();
			}
		}
}

private void removeTableFromLayout()
{
	if(null != searchable)
	{
		searchable.resetSearchResultTableValues();
	}
}

@SuppressWarnings("unused")
public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForRemediationRemarks", ShortcutAction.KeyCode.F8, null) {

		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

}

public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}

private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
{
	ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings({ "static-access", "deprecation" })
		@Override
		public void handleAction(Object sender, Object target) {
			VerticalLayout vLayout =  new VerticalLayout();

			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			final TextArea txtArea = new TextArea();
			txtArea.setStyleName("Boldstyle"); 
			txtArea.setValue(txtFld.getValue());
			txtArea.setNullRepresentation("");
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setMaxLength(4000);
			txtArea.setReadOnly(false);
			txtArea.setRows(25);

			txtArea.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					txtFld.setValue(((TextArea)event.getProperty()).getValue());
				}
			});
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

			final Window dialog = new Window();

			String strCaption = "Remarks";

			if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("clmAudQry")) {
				strCaption = "Claim Audit Query Remarks";
			}
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("clmAudQryRpl")) {
				strCaption = "Claims Reply Remarks";
			}
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("clmAudAdQry")) {
				strCaption = "Claim Audit Query Remarks";
			}
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("clmAudAdQryRpl")) {
				strCaption = "Claims Reply for Additional Query";
			}
			
			dialog.setCaption(strCaption);

			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(true);

			dialog.setContent(vLayout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setDraggable(true);
			dialog.setData(txtFld);

			dialog.addCloseListener(new Window.CloseListener() {
				@Override
				public void windowClose(CloseEvent e) {
					dialog.close();
				}
			});

			if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(450);
				dialog.setPositionY(500);
			}
			getUI().getCurrent().addWindow(dialog);
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};

	return listener;
}

@SuppressWarnings("unused")
private void setNotRequiredAndValidation(Component component) {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	AbstractField<Field> field = (AbstractField<Field>) component;
	field.setRequired(false);
	field.setValidationVisible(false);
}

}
