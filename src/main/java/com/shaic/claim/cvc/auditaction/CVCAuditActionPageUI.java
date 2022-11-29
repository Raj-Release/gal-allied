package com.shaic.claim.cvc.auditaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

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
import com.shaic.claim.cvc.auditqueryapproval.AuditBillingFaQueryTable;
import com.shaic.claim.cvc.auditqueryapproval.AuditClsQueryTable;
import com.shaic.claim.cvc.auditqueryapproval.AuditMedicalQueryTable;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

/**
 * @author GokulPrasath.A
 *
 */
public class CVCAuditActionPageUI extends ViewComponent {
	
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	
	private TextArea txtAuditRemarks;
	
	private ComboBox cmbAuditStatus;
	
	private ComboBoxMultiselect cmbCategoryOfError;
	
	private ComboBoxMultiselect cmbTeam;
	
	private ComboBoxMultiselect cmbProcessor;
	
	private ComboBox cmbMonetaryResult;
	
	private TextField amountInvolved;
	
	private ComboBox cmbAuditRemediationStatus;
	
	private TextArea txtRemediationRemarks;

	private Button submitBtn;
	
    private Button cancelBtn;
    
    private Button saveBtn;
    
    private ComboBox cmbAuditFinalStatus;
    
    private HorizontalLayout buttonHorLayout;
    
    private Searchable searchable;
    
    private ComboBox cmbQryOutcome;
	
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
	FormLayout cmbLastRightFLayout;
	HorizontalLayout horizontalLayout;
	VerticalLayout verticalLayout;
	
	private TextArea txtOtherRemarks;

	private BeanItemContainer<SelectValue> qryOutcomeContainer;
	
	private ComboBox cmbCompletedReason;
	private TextArea txtOutcomeRemarks;
	private TextArea txtCompletedRemarks;
	private BeanItemContainer<SelectValue> complReasonContainer;
	
	private VerticalLayout queryVLayout;
	private VerticalLayout adQueryVLayout;
	private VerticalLayout queryOutComeVLayout;
	
	private HorizontalLayout completQryHLayout;

	@Inject
	private Instance<AuditClsQueryTable> clsQryTableInstance;
	private AuditClsQueryTable clsQryTable;
	
	@Inject
	private Instance<AuditBillingFaQueryTable> billingFaQryTableInstance;
	private AuditBillingFaQueryTable billingFaQryTable;
	
	
	@Inject
	private Instance<AuditMedicalQueryTable> medicalQryTableInstance;
	private AuditMedicalQueryTable  medicalQryTable;
	
	
	
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
		
		preauthIntimationDetailsCarousel.init(bean.getPreauthDto().getNewIntimationDTO(), bean.getPreauthDto().getClaimDTO(), "Audit Action Processing");
		
		clsQryTable = clsQryTableInstance.get();
		clsQryTable.init(SHAConstants.AUDIT_ACTION_SCREEN, bean);
		
		billingFaQryTable = billingFaQryTableInstance.get();
		billingFaQryTable.init(SHAConstants.AUDIT_ACTION_SCREEN, bean);
		
		medicalQryTable = medicalQryTableInstance.get();
		medicalQryTable.init(SHAConstants.AUDIT_ACTION_SCREEN, bean);
		
		queryVLayout = new VerticalLayout();
		adQueryVLayout = new VerticalLayout();
		queryOutComeVLayout = new VerticalLayout();
		queryOutComeVLayout.setSpacing(true);
		queryOutComeVLayout.setMargin(true);
		
		txtAuditRemarks = new TextArea("Audit Remarks");
		txtAuditRemarks.setWidth("700px");
		txtAuditRemarks.setHeight("190px");
		txtAuditRemarks.setMaxLength(4000);
		txtAuditRemarks.setEnabled(false);
		
		cmbAuditStatus = new ComboBox("Audit Status");
		cmbAuditStatus.setNullSelectionAllowed(Boolean.FALSE);
		cmbAuditStatus.setEnabled(false);
		
		cmbCategoryOfError = new ComboBoxMultiselect("Category of Error");
		cmbCategoryOfError.setShowSelectedOnTop(true);
//		cmbCategoryOfError.setNullSelectionAllowed(Boolean.FALSE);
		cmbCategoryOfError.setEnabled(true);
		
		cmbTeam = new ComboBoxMultiselect("Claim Processing Team");
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
			bean.setRemediationRemarks(txtRemediationRemarks.getValue());
		}
		
	
		fireViewEvent(CVCAuditActionPagePresenter.LOAD_CVC_AUDIT_ACTION_STATUS_VALUES, bean);
		
		txtOtherRemarks = new TextArea("Other Remarks");
		txtOtherRemarks.setMaxLength(4000);
		txtOtherRemarks.setEnabled(true);
		txtOtherRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(txtOtherRemarks,null);
		if(!StringUtils.isBlank(bean.getOtherRemarks())){
			txtOtherRemarks.setValue(bean.getOtherRemarks());
			txtOtherRemarks.setVisible(true);
			setRequiredAndValidation(txtOtherRemarks);
		}else{
			txtOtherRemarks.setVisible(false);
		}
		
		if (null != bean && null != bean.getClaimType() && bean.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_STRING)) {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),0l, ViewLevels.INTIMATION, false,"Audit Action Processing");
		}else {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),bean.getTransactionKey(), ViewLevels.INTIMATION, false,"Audit Action Processing");
		}
		
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setSizeFull();		
						
		remarksFLayout = new FormLayout(txtAuditRemarks,cmbAuditStatus);
		
		cmbLeftFLayout = new FormLayout(cmbTeam,cmbMonetaryResult,cmbAuditRemediationStatus,cmbAuditFinalStatus,txtRemediationRemarks);
		cmbRightFLayout = new FormLayout(cmbCategoryOfError,cmbProcessor,amountInvolved);
		cmbLastRightFLayout = new FormLayout(txtOtherRemarks);
		
		horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout,cmbLastRightFLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		
		verticalLayout = new VerticalLayout(remarksFLayout,horizontalLayout);
		verticalLayout.setSpacing(false);
		verticalLayout.setMargin(true);
		
		saveBtn = new Button("Save");
		saveBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setWidth("-1px");
		saveBtn.setHeight("-1px");
		saveBtn.setVisible(true);
		
		submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		if(null != bean && !bean.isClmAuditHeadUser()){
			submitBtn.setVisible(false);
		}else{
			submitBtn.setVisible(true);
		}
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(saveBtn,submitBtn,cancelBtn);
	
		buttonHorLayout.setSpacing(true);
		
		mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel, viewDetailsLayout,verticalLayout,queryVLayout,adQueryVLayout,queryOutComeVLayout,buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		
		addListener();
		if(bean.getTabStatus() != null && bean.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED) || (bean.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED) && bean.getAuditFinalStatus().equalsIgnoreCase(SHAConstants.NO_ERROR_STRING))){
				
				generateQryOutcomeLayout();
				
				setRequiredAndValidation(txtCompletedRemarks);
				setRequiredAndValidation(txtOutcomeRemarks);
				mandatoryFields.remove(txtCompletedRemarks);
				mandatoryFields.remove(txtOutcomeRemarks);
				mandatoryFields.add(txtCompletedRemarks);
				mandatoryFields.add(txtOutcomeRemarks);
		}
		
		setRequiredAndValidation(txtAuditRemarks);
		setRequiredAndValidation(cmbAuditStatus);
		setRequiredAndValidation(cmbCategoryOfError);
		setRequiredAndValidation(cmbTeam);
		setRequiredAndValidation(cmbProcessor);
		setRequiredAndValidation(cmbMonetaryResult);
		setRequiredAndValidation(amountInvolved);
		setRequiredAndValidation(cmbAuditRemediationStatus);
		setRequiredAndValidation(txtRemediationRemarks);
//		setRequiredAndValidation(cmbAuditFinalStatus);
		setRequiredAndValidation(txtOtherRemarks);
		
		mandatoryFields.remove(txtAuditRemarks);
		mandatoryFields.remove(cmbAuditStatus);
		mandatoryFields.remove(cmbCategoryOfError);
		mandatoryFields.remove(cmbTeam);
		mandatoryFields.remove(cmbProcessor);
		mandatoryFields.remove(cmbMonetaryResult);
		mandatoryFields.remove(amountInvolved);
		mandatoryFields.remove(cmbAuditRemediationStatus);
		mandatoryFields.remove(txtRemediationRemarks);
//		mandatoryFields.remove(cmbAuditFinalStatus);
		mandatoryFields.remove(txtOtherRemarks);
		
		mandatoryFields.add(txtAuditRemarks);
		mandatoryFields.add(cmbAuditStatus);
		mandatoryFields.add(cmbCategoryOfError);
		mandatoryFields.add(cmbTeam);
		mandatoryFields.add(cmbProcessor);
		mandatoryFields.add(cmbMonetaryResult);
		mandatoryFields.add(amountInvolved);
		mandatoryFields.add(cmbAuditRemediationStatus);
		mandatoryFields.add(txtRemediationRemarks);
//		mandatoryFields.add(cmbAuditFinalStatus);
		
		
		setEnableDisableFields(bean);

		
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
				                	fireViewEvent(CVCAuditActionPagePresenter.CANCEL_EVENT_AUDIT_ACTION, bean);
				                	releaseHumanTask();
				                	fireViewEvent(MenuItemBean.CVC_AUDIT_ACTION_PROCESSING, true);
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
					if(null != txtRemediationRemarks && txtRemediationRemarks.getValue() != null){
						bean.setRemediationRemarks(txtRemediationRemarks.getValue());
						
						if(bean.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED) 
								&& !bean.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)
								&& bean.getAuditFinalStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)){
							bean.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
						}	
					}

					fireViewEvent(CVCAuditActionPagePresenter.SUBMIT_EVENT_AUDIT_ACTION, bean);
				}

			}
		});
		
		cmbAuditStatus.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue auditStatus = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				
				if (auditStatus != null) {
					bean.setAuditStatus(auditStatus.getValue());
				}
			}
		});
		
		cmbCategoryOfError.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String auditCategoryError = event.getProperty().getValue().toString();
				
				if (auditCategoryError != null && auditCategoryError.contains(SHAConstants.CVC_OTHERS_MAS_KEY) 
						&& null != cmbAuditStatus && null != cmbAuditStatus.getValue()
						/*&& cmbAuditStatus.getValue().toString().equalsIgnoreCase(SHAConstants.ERROR_STRING)*/){

					txtOtherRemarks.setVisible(true);
					
					if(null != cmbCategoryOfError){
					bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
					}
					
				}else {

					txtOtherRemarks.setValue("");
					txtOtherRemarks.setVisible(false);

					if(null != cmbCategoryOfError){
					bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
					}
					
				}
				/*if(null != cmbCategoryOfError){
				bean.setErrorCategory(cmbCategoryOfError.getValue().toString());
				}*/
			}
		});
		
		cmbTeam.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbTeam){
				bean.setTeam(cmbTeam.getValue().toString());
				generateTeamLayout(cmbTeam.getValue());
				}
			}
		});
		
		cmbProcessor.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbProcessor){
				bean.setProcessor(cmbProcessor.getValue().toString());
				}
			}
		});
		
		cmbMonetaryResult.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbMonetaryResult){
				bean.setMonetaryResult(cmbMonetaryResult.getValue().toString());
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
				String statusValue = event.getProperty().getValue() != null ? ((SelectValue)event.getProperty().getValue()).getValue() : null;
				if (statusValue != null 
						&& statusValue.equalsIgnoreCase(SHAConstants.COMPLETED_STRING)) {
					setRequiredAndValidation(cmbAuditFinalStatus);
					mandatoryFields.remove(cmbAuditFinalStatus);
					mandatoryFields.add(cmbAuditFinalStatus);
					
					generateQryOutcomeLayout();
					
					bean.setRemediationStatus(statusValue);
				}else {
					setNotRequiredAndValidation(cmbAuditFinalStatus);
					mandatoryFields.remove(cmbAuditFinalStatus);
					
					queryOutComeVLayout.removeAllComponents();
					bean.setQryOutcome(null);
					bean.setCompletedReason(null);
					bean.setCompletedRemarks(null);
					bean.setSatisUnSatisRemarks(null);
					
				}
				
			}
		});
		
		cmbAuditFinalStatus.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue auditFinalStatus = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				
				if (auditFinalStatus != null) {
					bean.setAuditFinalStatus(auditFinalStatus.getValue());
				}
			}
		});
		
		txtOtherRemarks.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != txtOtherRemarks){
				bean.setOtherRemarks(txtOtherRemarks.getValue());
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
		
		saveBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					if(null != txtRemediationRemarks && txtRemediationRemarks.getValue() != null){
						bean.setRemediationRemarks(txtRemediationRemarks.getValue());
						if(bean.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED)){
							bean.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED);
						}
					}
					fireViewEvent(CVCAuditActionPagePresenter.SUBMIT_EVENT_AUDIT_ACTION, bean);
				}

			}
		});
		
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
	
		if (txtAuditRemarks != null
				&& txtAuditRemarks.getValue() == null
				|| (txtAuditRemarks.getValue() != null && txtAuditRemarks
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Audit Remarks </br>");
		}
		if (cmbAuditStatus != null
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
		}

		if(clsQryTable != null && clsQryTable.getValues() != null && !clsQryTable.getValues().isEmpty()) {
			if(!clsQryTable.isValid() && !clsQryTable.getErrors().isEmpty()) {
				eMsg.append(clsQryTable.getErrors());
			}
			hasError = hasError || !clsQryTable.isValid();
		}
		
		if(medicalQryTable != null && medicalQryTable.getValues() != null  && !medicalQryTable.getValues().isEmpty()) {
			
			if(!medicalQryTable.isValid() && !medicalQryTable.getErrors().isEmpty()) {
				eMsg.append(medicalQryTable.getErrors());
			}
			hasError = hasError || !medicalQryTable.isValid();
		}
		
		if(billingFaQryTable != null && billingFaQryTable.getValues() != null  && !billingFaQryTable.getValues().isEmpty()) {
			
			if(!billingFaQryTable.isValid()  && !billingFaQryTable.getErrors().isEmpty()) {
				eMsg.append(billingFaQryTable.getErrors());
			}
			hasError = hasError || !billingFaQryTable.isValid();
		}
		
		if(cmbAuditRemediationStatus.getValue() != null
				&&  (cmbAuditRemediationStatus.getValue().toString().equalsIgnoreCase(SHAConstants.COMPLETED_STRING)
						|| SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED.equalsIgnoreCase(bean.getRemediationStatus()))) {
		
			if(cmbQryOutcome != null && cmbQryOutcome.getValue() == null) {
				hasError = true;
				eMsg.append("Please Select Query Outcome </br>");
			}
	
			if(txtOutcomeRemarks != null && (txtOutcomeRemarks.getValue() == null || txtOutcomeRemarks.getValue().isEmpty())) {
			
				hasError = true;
				eMsg.append("Please Enter Outcome Remarks</br>");
			}	
			
			if(cmbCompletedReason != null && cmbCompletedReason.getValue() == null) {
				hasError = true;
				eMsg.append("Please Select Query Completed Reason</br>");
			}
	
			if(txtCompletedRemarks != null && (txtCompletedRemarks.getValue() == null || txtCompletedRemarks.getValue().isEmpty())) {
			
				hasError = true;
				eMsg.append("Please Enter Completed Remarks</br>");
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
			
			if(clsQryTable != null && clsQryTable.getValues() != null) {
				bean.setClsQryList(clsQryTable.getValues());
			}
			
			if(medicalQryTable != null && medicalQryTable.getValues() != null) {
				bean.setMedicalQryList(medicalQryTable.getValues());
			}
			
			if(billingFaQryTable != null && billingFaQryTable.getValues() != null) {
				bean.setBillingFaQryList(billingFaQryTable.getValues());
			}
			bean.setTeam(cmbTeam.getValue().toString());
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

public void loadCVCStatusDropDownValues(AuditDetails auditDetails, 
		BeanItemContainer<SelectValue> remediationStatusValueContainer, BeanItemContainer<SelectValue> statusValueContainer, 
		List<AuditTeam> auditTeamList, List<AuditCategory> auditCategoryList, List<AuditProcessor> auditProcessorList, 
		BeanItemContainer<SelectValue> cmbCategoryContainer, BeanItemContainer<SelectValue> TeamValueContainer, 
		BeanItemContainer<SelectValue> ErrorValueContainer, BeanItemContainer<SelectValue> processorValueContainer, 
		BeanItemContainer<SelectValue> monetaryValueContainer, BeanItemContainer<SelectValue> qryOutcomeContainer,
		BeanItemContainer<SelectValue> complReasonContainer) {
	
	this.qryOutcomeContainer = qryOutcomeContainer;
	this.complReasonContainer = complReasonContainer;
	
	if(auditDetails!=null && auditDetails.getAuditRemarks()!=null){
		txtAuditRemarks.setValue(auditDetails.getAuditRemarks());
		bean.setAuditRemarks(auditDetails.getAuditRemarks().toString());
	}
	
	BeanItemContainer<SelectValue> cmbAuditStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	SelectValue cmbAuditStatustemp = new SelectValue();
	cmbAuditStatustemp.setValue(auditDetails.getAuditStatus());
	cmbAuditStatusContainer.addBean(cmbAuditStatustemp);
	cmbAuditStatus.setContainerDataSource(cmbAuditStatusContainer);
	cmbAuditStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbAuditStatus.setItemCaptionPropertyId("value");
	cmbAuditStatus.setValue(cmbAuditStatusContainer.getItemIds().get(0));
	bean.setAuditStatus(auditDetails.getAuditStatus().toString());
	
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
		bean.setAuditFinalStatus(auditDetails.getFinalAuditStatus().toString());
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
				bean.setOtherRemarks(AuditCategory.getAuditCategoryOthrRmks());
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
		generateTeamLayout(setTeamValues);
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
				bean.setMonetaryResult(cmbMonetaryResult.getValue().toString());
				break;
			}
		}
		bean.setMonetaryResult(auditDetails.getMonetaryResult() != null ? auditDetails.getMonetaryResult().toString() : "");
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
				bean.setRemediationStatus(cmbAuditRemediationStatus.getValue().toString());
				break;
			}
			else if((SHAConstants.CVC_PENDING).equalsIgnoreCase(remediationStatusValueContainer.getIdByIndex(i).getValue())){
				cmbAuditRemediationStatus.setValue(remediationStatusValueContainer.getIdByIndex(i));
				break;
			}
		}
		bean.setRemediationStatus(auditDetails.getRemediationStatus().toString());
	}else {
		cmbAuditRemediationStatus.setContainerDataSource(remediationStatusValueContainer);
		cmbAuditRemediationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditRemediationStatus.setItemCaptionPropertyId("value");
	}

	amountInvolved.setValue(auditDetails.getAmountInvolved() != null ? auditDetails.getAmountInvolved().toString() : "0");
	bean.setAmountInvolved(auditDetails.getAmountInvolved() != null ? auditDetails.getAmountInvolved().toString() : "0");
	
	if (null != auditDetails && null != auditDetails.getRemediationRemarks()) {
		txtRemediationRemarks.setValue(auditDetails.getRemediationRemarks());
		bean.setRemediationRemarks(auditDetails.getRemediationRemarks().toString());
	}
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
			
			if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("cashlessQry")) {
				strCaption = "Query Remarks (Cashless)";
			}
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("medicalQry")) {
				strCaption = "Query Remarks (Medical)";
			}
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("billingFaQry")) {
				strCaption = "Query Remarks (Billing/Financial)";
			}			
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("qryRplRemarks")) {
				strCaption = "Claims Reply Remarks";
			}					
			else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("adlQryRpl")) {
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

	public void generateQryOutcomeLayout() {
		
		queryOutComeVLayout.removeAllComponents();
		
		completQryHLayout = new HorizontalLayout();
		
		cmbQryOutcome = new ComboBox("Query Outcome");
		cmbQryOutcome.setContainerDataSource(qryOutcomeContainer);
		cmbQryOutcome.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbQryOutcome.setItemCaptionPropertyId("value");
		cmbQryOutcome.setNullSelectionAllowed(Boolean.TRUE);
		cmbQryOutcome.setEnabled(true);
		cmbQryOutcome.setData(bean);
		for(int i=0;i<qryOutcomeContainer.size();i++){
			if (bean.getQryOutcome() != null &&(bean.getQryOutcome()).equalsIgnoreCase(qryOutcomeContainer.getIdByIndex(i).getValue())){
				cmbQryOutcome.setValue(qryOutcomeContainer.getIdByIndex(i));
				break;
			}
		}
		mandatoryFields.remove(cmbQryOutcome);
		setRequiredAndValidation(cmbQryOutcome);
		mandatoryFields.add(cmbQryOutcome);
		completQryHLayout.addComponent(cmbQryOutcome);
		
		
		cmbQryOutcome.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SearchCVCAuditActionTableDTO bean = (SearchCVCAuditActionTableDTO) cmbQryOutcome.getData();
				String qryOutcomeValue = event.getProperty().getValue() != null ? ((SelectValue)event.getProperty().getValue()).getValue() : null;
				if (qryOutcomeValue != null 
						&& qryOutcomeValue.equalsIgnoreCase(SHAConstants.SATISFACTORY_STRING)) {
					
					mandatoryFields.remove(cmbQryOutcome);
					setRequiredAndValidation(cmbQryOutcome);
					mandatoryFields.add(cmbQryOutcome);					
					bean.setQryOutcome(qryOutcomeValue);
				}

			}
		});

		txtOutcomeRemarks = new TextArea("Outcome Remarks"); 
		txtOutcomeRemarks.setData(bean);
		txtOutcomeRemarks.setValue(bean.getSatisUnSatisRemarks());
		txtOutcomeRemarks.setNullRepresentation("");
		txtOutcomeRemarks.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				setRequiredAndValidation(txtOutcomeRemarks);
				mandatoryFields.add(txtOutcomeRemarks);
				SearchCVCAuditActionTableDTO bean = (SearchCVCAuditActionTableDTO) txtOutcomeRemarks.getData();
				bean.setSatisUnSatisRemarks(((TextArea)event.getProperty()).getValue());
			}
		});
		setRequiredAndValidation(txtOutcomeRemarks);
		mandatoryFields.add(txtOutcomeRemarks);
		completQryHLayout.addComponent(txtOutcomeRemarks);
		
		cmbCompletedReason = new ComboBox("Completed Reason");
		cmbCompletedReason.setContainerDataSource(complReasonContainer);
		cmbCompletedReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCompletedReason.setItemCaptionPropertyId("value");
		cmbCompletedReason.setNullSelectionAllowed(Boolean.TRUE);
		cmbCompletedReason.setEnabled(true);
		cmbCompletedReason.setData(bean);
		mandatoryFields.remove(cmbCompletedReason);
		setRequiredAndValidation(cmbCompletedReason);
		mandatoryFields.add(cmbCompletedReason);
		for(int i=0;i<complReasonContainer.size();i++){
			if (bean.getCompletedReason() != null &&(bean.getCompletedReason()).equalsIgnoreCase(complReasonContainer.getIdByIndex(i).getValue())){
				cmbCompletedReason.setValue(complReasonContainer.getIdByIndex(i));
				break;
			}
		}
		cmbCompletedReason.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SearchCVCAuditActionTableDTO bean = (SearchCVCAuditActionTableDTO) cmbCompletedReason.getData();
				bean.setCompletedReason((event.getProperty()).getValue().toString());
				
			}
		});
		
		completQryHLayout.addComponent(cmbCompletedReason);
		completQryHLayout.setSpacing(true);
		
		
		txtCompletedRemarks = new TextArea("Completed Remarks");
		txtCompletedRemarks.setData(bean);
		txtCompletedRemarks.setValue(bean.getCompletedRemarks());
		txtCompletedRemarks.setNullRepresentation("");
		setRequiredAndValidation(txtCompletedRemarks);
		mandatoryFields.add(txtCompletedRemarks);
		txtCompletedRemarks.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {	
				setRequiredAndValidation(txtCompletedRemarks);
				mandatoryFields.add(txtCompletedRemarks);
				SearchCVCAuditActionTableDTO bean = (SearchCVCAuditActionTableDTO) txtCompletedRemarks.getData();
				bean.setCompletedRemarks(((TextArea)event.getProperty()).getValue());
			}
		});
		
		mandatoryFields.remove(cmbCompletedReason);
		mandatoryFields.add(cmbCompletedReason);
		mandatoryFields.add(txtOutcomeRemarks);
		setRequiredAndValidation(txtOutcomeRemarks);
		mandatoryFields.add(txtCompletedRemarks);
		setRequiredAndValidation(txtCompletedRemarks);
		
		completQryHLayout.addComponent(txtCompletedRemarks);
				
		
		
		if(bean.getTabStatus() != null && bean.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED) ){
			cmbQryOutcome.setEnabled(false);
			txtOutcomeRemarks.setReadOnly(true);
			cmbCompletedReason.setEnabled(false); 
			txtCompletedRemarks.setReadOnly(true);
		}
		queryOutComeVLayout.addComponent(completQryHLayout);
		
	
	}
	
	
	
	public void generateTeamLayout(Object selectedTeam){
		Collection multiSelect = (Collection)selectedTeam; 
		
		// TODO  Audit QUERY  Remarks
		if(multiSelect != null && !multiSelect.isEmpty()) {
			
			queryVLayout.removeAllComponents();
//			adQueryVLayout.removeAllComponents();
			for (Object obj : multiSelect) {				
				SelectValue teamSelect = (SelectValue)obj;
				
				if(teamSelect.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)) {
					if(bean.getClsQryList() != null && !bean.getClsQryList().isEmpty()){
						List<SearchCVCAuditClsQryTableDTO> clsQryList = bean.getClsQryList();
						for (SearchCVCAuditClsQryTableDTO auditQryDTO : clsQryList) {
							auditQryDTO.setsNo(clsQryList.indexOf(auditQryDTO)+1);
							this.clsQryTable.addBeanToList(auditQryDTO);
						}
					}
					queryVLayout.addComponent(clsQryTable);
				}
				if(teamSelect.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)) {
					if(bean.getMedicalQryList() != null && !bean.getMedicalQryList().isEmpty()){
						List<SearchCVCAuditClsQryTableDTO> medicalQryList = bean.getMedicalQryList();
						for (SearchCVCAuditClsQryTableDTO auditQryDTO : medicalQryList) {
							auditQryDTO.setsNo(medicalQryList.indexOf(auditQryDTO)+1);
							this.medicalQryTable.addBeanToList(auditQryDTO);
						}
					}
					queryVLayout.addComponent(medicalQryTable);
				}
				if(teamSelect.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_BILLING) || teamSelect.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_FINANCIAL)){
					if(bean.getBillingFaQryList() != null && !bean.getBillingFaQryList().isEmpty()){
						List<SearchCVCAuditClsQryTableDTO> billingFaQryList = bean.getBillingFaQryList();
						for (SearchCVCAuditClsQryTableDTO auditQryDTO : billingFaQryList) {
							auditQryDTO.setsNo(billingFaQryList.indexOf(auditQryDTO)+1);
							this.billingFaQryTable.addBeanToList(auditQryDTO);
						}
					}
					queryVLayout.addComponent(billingFaQryTable);
				}
			}
		}
	}
	
	public void setEnableDisableFields(SearchCVCAuditActionTableDTO dto){
		if(null != dto && !dto.isClmAuditHeadUser() && dto.getTabStatus() != null && (!dto.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED ) || dto.getTabStatus().equalsIgnoreCase(SHAConstants.CVC_PENDING ) || dto.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED))){
			txtAuditRemarks.setEnabled(false);
			txtAuditRemarks.setReadOnly(true);
			cmbAuditStatus.setEnabled(false);
			cmbCategoryOfError.setEnabled(false);
			cmbTeam.setEnabled(false);
			cmbProcessor.setEnabled(false);
			cmbMonetaryResult.setEnabled(false);
			amountInvolved.setEnabled(false);
			cmbAuditRemediationStatus.setEnabled(false);
			txtRemediationRemarks.setEnabled(false);
			txtOtherRemarks.setEnabled(false);
			txtAuditRemarks.setEnabled(false);
			cmbAuditStatus.setEnabled(false);
			cmbCategoryOfError.setEnabled(false);
			cmbTeam.setEnabled(false);
			cmbProcessor.setEnabled(false);
			cmbMonetaryResult.setEnabled(false);
			amountInvolved.setEnabled(false);
			cmbAuditRemediationStatus.setEnabled(false);
			txtRemediationRemarks.setEnabled(false);
			txtRemediationRemarks.setReadOnly(true);
			txtOtherRemarks.setEnabled(false);
			cmbAuditFinalStatus.setEnabled(false);
			saveBtn.setEnabled(false);
			
		}else if (null != dto && dto.isClmAuditHeadUser() && dto.getTabStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED) || (dto.getTabStatus().equalsIgnoreCase(SHAConstants.CVC_PENDING ) && !dto.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING) )){
			
			txtAuditRemarks.setEnabled(false);
			txtAuditRemarks.setReadOnly(true);
			cmbAuditStatus.setEnabled(false);
			cmbCategoryOfError.setEnabled(false);
			cmbTeam.setEnabled(false);
			cmbProcessor.setEnabled(false);
			cmbMonetaryResult.setEnabled(false);
			amountInvolved.setEnabled(false);
			cmbAuditRemediationStatus.setEnabled(false);
			txtRemediationRemarks.setEnabled(false);
			txtOtherRemarks.setEnabled(false);
			txtAuditRemarks.setEnabled(false);
			cmbAuditStatus.setEnabled(false);
			cmbCategoryOfError.setEnabled(false);
			cmbTeam.setEnabled(false);
			cmbProcessor.setEnabled(false);
			cmbMonetaryResult.setEnabled(false);
			amountInvolved.setEnabled(false);
			cmbAuditRemediationStatus.setEnabled(false);
			txtRemediationRemarks.setEnabled(false);
			txtRemediationRemarks.setReadOnly(true);
			txtOtherRemarks.setEnabled(false);
			cmbAuditFinalStatus.setEnabled(false);
			saveBtn.setEnabled(false);
			submitBtn.setEnabled(false);
			
			if(cmbQryOutcome != null){				
			  cmbQryOutcome.setEnabled(false);
			}
			if(txtOutcomeRemarks != null){
				
				txtOutcomeRemarks.setReadOnly(true);
			}
			if(cmbCompletedReason != null){
				
				cmbCompletedReason.setEnabled(false); 
			}
			if(txtCompletedRemarks != null){
				
				txtCompletedRemarks.setReadOnly(true);
			}
			
		}
		
	}
}
