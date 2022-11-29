package com.shaic.claim.cvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.RODBillDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.shaic.reimbursement.draftinvesigation.InvestigatorTriggerPointsTable;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class CVCPageUI extends ViewComponent {
	
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

	private Button submitBtn;
	
    private Button cancelBtn;
    
    private HorizontalLayout buttonHorLayout;
    
    private FormLayout qryFrmLayout;
    
    private TextArea txtClsQryRemarks;
    
    private TextArea txtMedicalQryRemarks;
    
    private TextArea txtBillingFAQryRemarks;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private ViewDetails viewDetails;
		
	private SearchCVCTableDTO bean;
	
	PreauthDTO preauthDTO = null;
	
	SearchCVCForm searchForm;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	FormLayout remarksFLayout;
	FormLayout cmbLeftFLayout;
	FormLayout cmbRightFLayout;
	FormLayout cmbLastRightFLayout;
	HorizontalLayout horizontalLayout;
	VerticalLayout verticalLayout;
	
	private TextArea txtOtherRemarks;

	public void init(SearchCVCTableDTO bean) {
		this.bean = bean;
		setCompleteLayout();
	}
	
	public void init(SearchCVCTableDTO bean,PreauthDTO preauthDTO) {
		this.bean = bean;
		this.preauthDTO = preauthDTO;
	}

	public void setCompleteLayout() {
		
		preauthIntimationDetailsCarousel.init(bean.getPreauthDto().getNewIntimationDTO(), bean.getPreauthDto().getClaimDTO(), "Claims Audit");
		
		txtAuditRemarks = new TextArea("Audit Remarks");
		txtAuditRemarks.setWidth("700px");
		txtAuditRemarks.setHeight("190px");
		txtAuditRemarks.setMaxLength(4000);
		
		cmbAuditStatus = new ComboBox("Audit Status");
		cmbAuditStatus.setNullSelectionAllowed(Boolean.FALSE);
		
		cmbCategoryOfError = new ComboBoxMultiselect("Category of Error");
		cmbCategoryOfError.setShowSelectedOnTop(true);
//		cmbCategoryOfError.setNullSelectionAllowed(Boolean.FALSE);
		
		cmbTeam = new ComboBoxMultiselect("Claim Processing Team");
		cmbTeam.setShowSelectedOnTop(true);
//		cmbTeam.setNullSelectionAllowed(Boolean.FALSE);
		
		cmbProcessor = new ComboBoxMultiselect("Processor");
		cmbProcessor.setShowSelectedOnTop(true);
//		cmbProcessor.setNullSelectionAllowed(Boolean.FALSE);
		
		cmbMonetaryResult = new ComboBox("Monetary Result");
		cmbMonetaryResult.setNullSelectionAllowed(Boolean.FALSE);
		
		amountInvolved = new TextField("Amount Involved");
		CSValidator validator = new CSValidator();
		validator.extend(amountInvolved);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		cmbAuditRemediationStatus = new ComboBox("Audit Remediation Status");
		cmbAuditRemediationStatus.setNullSelectionAllowed(Boolean.FALSE);
		cmbAuditRemediationStatus.setEnabled(false);
		
		txtOtherRemarks = new TextArea("Other Remarks");
		txtOtherRemarks.setMaxLength(4000);
		bean.setOtherRemarks(txtOtherRemarks.getValue());
		txtOtherRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		txtOtherRemarks.setData(txtOtherRemarks.getValue());
		if(!StringUtils.isBlank(bean.getOtherRemarks())){
			txtOtherRemarks.setValue(bean.getOtherRemarks());
			txtOtherRemarks.setVisible(true);
			setRequiredAndValidation(txtOtherRemarks);
		}else{
			txtOtherRemarks.setVisible(false);
		}
		handleTextAreaPopup(txtOtherRemarks,null);
		
		fireViewEvent(CVCPagePresenter.LOAD_CVC_STATUS_VALUES, bean);
	
		/*SelectValue cvcAuditPass = new SelectValue();
		cvcAuditPass.setId(0l);
		cvcAuditPass.setValue("Error");

		SelectValue cvcAuditFail = new SelectValue();

		cvcAuditFail.setId(1l);
		cvcAuditFail.setValue("NoError");		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(cvcAuditPass);
		selectVallueList.add(cvcAuditFail);		
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbAuditStatus.setContainerDataSource(selectValueContainer);
		cmbAuditStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditStatus.setItemCaptionPropertyId("value");*/
		
		
		if (null != bean && null != bean.getClaimType() && bean.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_CHAR)) {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),0l, ViewLevels.INTIMATION, false,"Claims Audit");
		}else {
			viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(),bean.getTransactionKey(), ViewLevels.INTIMATION, false,"Claims Audit");
		}
		
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setSizeFull();		
						
		remarksFLayout = new FormLayout(txtAuditRemarks,cmbAuditStatus);
		
		if (null != cmbAuditStatus && null != cmbAuditStatus.getValue()
				&& cmbAuditStatus.getValue().toString().equalsIgnoreCase(SHAConstants.ERROR_STRING)){
			cmbLeftFLayout = new FormLayout(cmbTeam,cmbMonetaryResult,cmbAuditRemediationStatus);
			cmbRightFLayout = new FormLayout(cmbCategoryOfError,cmbProcessor,amountInvolved);
			cmbLastRightFLayout = new FormLayout(txtOtherRemarks);
			if (null != bean && bean.getErrorCategory() != null && bean.getErrorCategory().equals(SHAConstants.OTHERS)) {
				horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout,cmbLastRightFLayout);
			}else {
				horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout);
			}
			horizontalLayout.setSpacing(true);
			horizontalLayout.setMargin(true);
			
			verticalLayout = new VerticalLayout(remarksFLayout,horizontalLayout);
			verticalLayout.setSpacing(false);
			verticalLayout.setMargin(true);
		}else {
			verticalLayout = new VerticalLayout(remarksFLayout);
			verticalLayout.setSpacing(false);
			verticalLayout.setMargin(true);
		}
		
		qryFrmLayout = new FormLayout();
		
		verticalLayout.addComponent(qryFrmLayout);
		
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
		
		setRequiredAndValidation(txtAuditRemarks);
		setRequiredAndValidation(cmbAuditStatus);
		
		mandatoryFields.remove(txtAuditRemarks);
		mandatoryFields.remove(cmbAuditStatus);
		mandatoryFields.remove(cmbCategoryOfError);
		mandatoryFields.remove(cmbTeam);
		mandatoryFields.remove(cmbProcessor);
		mandatoryFields.remove(cmbMonetaryResult);
		mandatoryFields.remove(amountInvolved);
		mandatoryFields.remove(cmbAuditRemediationStatus);
		mandatoryFields.remove(txtOtherRemarks);
		mandatoryFields.add(txtAuditRemarks);
		mandatoryFields.add(cmbAuditStatus);
		
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
				                	fireViewEvent(CVCPagePresenter.CANCEL_EVENT, bean);
				                	releaseHumanTask();
				                	fireViewEvent(MenuItemBean.CVC_AUDIT, true);
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
					fireViewEvent(CVCPagePresenter.SUBMIT_EVENT, bean);
				}

			}
		});
		
		txtAuditRemarks.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != txtAuditRemarks){
				bean.setAuditRemarks(txtAuditRemarks.getValue());
				}
			}
		});
		
		cmbAuditStatus.addValueChangeListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				SelectValue auditStatus = (SelectValue) valueChangeEvent
						.getProperty().getValue();
				
				if (auditStatus != null) {
					
					if (null != auditStatus && null != cmbAuditStatus && null != cmbAuditStatus.getValue()
							&& cmbAuditStatus.getValue().toString().equalsIgnoreCase(SHAConstants.ERROR_STRING)){
						verticalLayout.removeAllComponents();
						cmbLeftFLayout = new FormLayout(cmbTeam,cmbMonetaryResult,cmbAuditRemediationStatus);
						cmbRightFLayout = new FormLayout(cmbCategoryOfError,cmbProcessor,amountInvolved);
						cmbLastRightFLayout = new FormLayout(txtOtherRemarks);
						
//						if (null != bean && bean.getErrorCategory() != null && bean.getErrorCategory().equals(SHAConstants.OTHERS)) {
							horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout,cmbLastRightFLayout);
//						}else {
//							horizontalLayout = new HorizontalLayout(cmbLeftFLayout,cmbRightFLayout);
//						}
//						horizontalLayout.removeAllComponents();
//						horizontalLayout.addComponent(cmbLeftFLayout);
//						horizontalLayout.addComponent(cmbRightFLayout);
						horizontalLayout.setSpacing(true);
						horizontalLayout.setMargin(true);
						
//						verticalLayout = new VerticalLayout(remarksFLayout,horizontalLayout);
						verticalLayout.addComponent(remarksFLayout);
						verticalLayout.addComponent(horizontalLayout);
						verticalLayout.addComponent(qryFrmLayout);
						verticalLayout.setSpacing(false);
						verticalLayout.setMargin(true);
						
						setRequiredAndValidation(txtAuditRemarks);
						setRequiredAndValidation(cmbAuditStatus);
						setRequiredAndValidation(cmbCategoryOfError);
						setRequiredAndValidation(cmbTeam);
						setRequiredAndValidation(cmbProcessor);
						setRequiredAndValidation(cmbMonetaryResult);
						setRequiredAndValidation(amountInvolved);
						setRequiredAndValidation(cmbAuditRemediationStatus);
						setRequiredAndValidation(txtOtherRemarks);
						
						mandatoryFields.remove(txtAuditRemarks);
						mandatoryFields.remove(cmbAuditStatus);
						mandatoryFields.remove(cmbCategoryOfError);
						mandatoryFields.remove(cmbTeam);
						mandatoryFields.remove(cmbProcessor);
						mandatoryFields.remove(cmbMonetaryResult);
						mandatoryFields.remove(amountInvolved);
						mandatoryFields.remove(cmbAuditRemediationStatus);
						mandatoryFields.remove(txtOtherRemarks);
						mandatoryFields.add(txtAuditRemarks);
						mandatoryFields.add(cmbAuditStatus);
						mandatoryFields.add(cmbCategoryOfError);
						mandatoryFields.add(cmbTeam);
						mandatoryFields.add(cmbProcessor);
						mandatoryFields.add(cmbMonetaryResult);
						mandatoryFields.add(amountInvolved);
						mandatoryFields.add(cmbAuditRemediationStatus);
						mandatoryFields.add(txtOtherRemarks);
						bean.setQryStatus(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING);
						
					}else {
						verticalLayout.removeAllComponents();
//						verticalLayout = new VerticalLayout(remarksFLayout);
						verticalLayout.addComponent(remarksFLayout);
						verticalLayout.addComponent(qryFrmLayout);
						verticalLayout.setSpacing(false);
						verticalLayout.setMargin(true);
						
						setRequiredAndValidation(txtAuditRemarks);
						setRequiredAndValidation(cmbAuditStatus);
						
						mandatoryFields.remove(txtAuditRemarks);
						mandatoryFields.remove(cmbAuditStatus);
						mandatoryFields.remove(cmbCategoryOfError);
						mandatoryFields.remove(cmbTeam);
						mandatoryFields.remove(cmbProcessor);
						mandatoryFields.remove(cmbMonetaryResult);
						mandatoryFields.remove(amountInvolved);
						mandatoryFields.remove(cmbAuditRemediationStatus);
						mandatoryFields.remove(txtOtherRemarks);
						mandatoryFields.add(txtAuditRemarks);
						mandatoryFields.add(cmbAuditStatus);
					}
					
				} else {
					validatePage();
				}
				
				if (auditStatus != null) {
					bean.setAuditStatus(auditStatus.getValue());
				}
				mainLayout.removeComponent(verticalLayout);
				mainLayout.removeComponent(buttonHorLayout);
				mainLayout.addComponents(verticalLayout,buttonHorLayout);
				mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
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
						bean.setErrorCategory(event.getProperty().getValue());
						}
						
					}else {

						txtOtherRemarks.setValue("");
						txtOtherRemarks.setVisible(false);

						if(null != cmbCategoryOfError){
						bean.setErrorCategory(event.getProperty().getValue());
						}
						
					}
			}
		});
		
		cmbTeam.addValueChangeListener(new ValueChangeListener() {

			@SuppressWarnings("unused")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbTeam){
				bean.setTeam(event.getProperty().getValue());
				
				Collection multiSelect = (Collection)event.getProperty().getValue(); 
				
				// TODO  Audit QUERY  Remarks
				if(multiSelect != null && !multiSelect.isEmpty()) {
					
					qryFrmLayout.removeAllComponents();
					for (Object obj : multiSelect) {
						
						SelectValue selectedTeam = (SelectValue)obj;
						if(selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)) {
							txtClsQryRemarks = new TextArea("Query Remarks (Cashless)");
							txtClsQryRemarks.setWidth("700px");
							txtClsQryRemarks.setRows(5);
							txtClsQryRemarks.setId("cashless");
							txtClsQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
							handleTextAreaPopup(txtClsQryRemarks,null);
							if(!StringUtils.isBlank(bean.getClsQryRemarks())){
								txtClsQryRemarks.setValue(bean.getClsQryRemarks());
							}
							
							txtClsQryRemarks.addValueChangeListener(new ValueChangeListener() {

								@Override
								public void valueChange(ValueChangeEvent event) {
									if(event.getProperty().getValue() != null){
									bean.setClsQryRemarks(String.valueOf(event.getProperty().getValue()));
									}
								}
							});
								
							mandatoryFields.add(txtClsQryRemarks);
							qryFrmLayout.addComponent(txtClsQryRemarks);
						}
						else if(selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)) {
							txtMedicalQryRemarks = new TextArea("Query Remarks (Medical)");
							txtMedicalQryRemarks.setWidth("700px");
							txtMedicalQryRemarks.setRows(5);
							txtMedicalQryRemarks.setId("medical");
							txtMedicalQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
							handleTextAreaPopup(txtMedicalQryRemarks,null);
							
							if(!StringUtils.isBlank(bean.getMedicaQrylRemarks())){
								txtMedicalQryRemarks.setValue(bean.getMedicaQrylRemarks());
							}
							
							txtMedicalQryRemarks.addValueChangeListener(new ValueChangeListener() {

								@Override
								public void valueChange(ValueChangeEvent event) {
									if(event.getProperty().getValue() != null){
									bean.setMedicaQrylRemarks(String.valueOf(event.getProperty().getValue()));
									}
								}
							});
							mandatoryFields.add(txtMedicalQryRemarks);
							qryFrmLayout.addComponent(txtMedicalQryRemarks);
						}
						
					}
					String teamValue = multiSelect.toString();
					
					if(teamValue.contains(SHAConstants.AUDIT_TEAM_BILLING) || teamValue.contains(SHAConstants.AUDIT_TEAM_FINANCIAL)){
						
						if(txtBillingFAQryRemarks != null) {
							qryFrmLayout.removeComponent(txtBillingFAQryRemarks);
						}
						txtBillingFAQryRemarks = new TextArea("Query Remarks (Billing/Financial)");
						txtBillingFAQryRemarks.setWidth("700px");
						txtBillingFAQryRemarks.setRows(5);
						txtBillingFAQryRemarks.setId("billingFinance");
						txtBillingFAQryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
						handleTextAreaPopup(txtBillingFAQryRemarks,null);
						
						if(!StringUtils.isBlank(bean.getBillingFaQryRemarks())){
							txtBillingFAQryRemarks.setValue(bean.getBillingFaQryRemarks());
						}
						
						txtBillingFAQryRemarks.addValueChangeListener(new ValueChangeListener() {

							@Override
							public void valueChange(ValueChangeEvent event) {
								if(event.getProperty().getValue() != null){
								bean.setBillingFaQryRemarks(String.valueOf(event.getProperty().getValue()));
								}
							}
						});
						mandatoryFields.add(txtBillingFAQryRemarks);
						qryFrmLayout.addComponent(txtBillingFAQryRemarks);
					}
				}
				}
			}
		});
		
		cmbProcessor.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != cmbProcessor){
				bean.setProcessor(event.getProperty().getValue());
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
				if(null != cmbAuditRemediationStatus){
				bean.setRemediationStatus(cmbAuditRemediationStatus.getValue().toString());
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
		
	}
	
	private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
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
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

}
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
	
		if (null != cmbAuditStatus && null != cmbAuditStatus.getValue()
				&& cmbAuditStatus.getValue().toString().equalsIgnoreCase(SHAConstants.ERROR_STRING)){
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
			if ((txtOtherRemarks != null
					&& txtOtherRemarks.getValue() == null
					|| (txtOtherRemarks.getValue() != null && txtOtherRemarks.getValue().length() == 0)) 
					&& cmbCategoryOfError.getValue().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
				hasError = true;
				eMsg.append("Please Enter Other Remarks </br>");
			}
			
			Collection multiSelect = (Collection)bean.getTeam(); 
			
			// TODO  Audit QUERY  Remarks
			if(multiSelect != null && !multiSelect.isEmpty()) {
				
				for (Object obj : multiSelect) {
					
					SelectValue selectedTeam = (SelectValue)obj;
					if(selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)) {
						
						if (txtClsQryRemarks != null
								&& (txtClsQryRemarks.getValue() == null
								|| (txtClsQryRemarks.getValue() != null && txtClsQryRemarks.isEmpty()))) {
							hasError = true;
							eMsg.append("Please Enter Cashless Query Remarks </br>");
						}
					}
					if(selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)) {
						
						if (txtMedicalQryRemarks != null
								&& (txtMedicalQryRemarks.getValue() == null
								|| (txtMedicalQryRemarks.getValue() != null && txtMedicalQryRemarks.isEmpty()))) {
							hasError = true;
							eMsg.append("Please Enter Medical Query Remarks </br>");
						}
					}
					if(selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_BILLING) || selectedTeam.getValue().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_FINANCIAL)){
						
						if (txtBillingFAQryRemarks != null
								&& (txtBillingFAQryRemarks.getValue() == null
								|| (txtBillingFAQryRemarks.getValue() != null && txtBillingFAQryRemarks.isEmpty()))) {
							hasError = true;
							eMsg.append("Please Enter Billing & FA Query Remarks </br>");
						}
					}
			
				}
			}
			
		} else {
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
		return true;
				
	}
	
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRemarks(searchField, getShortCutListenerForRemarks(searchField));

	}

	@SuppressWarnings("deprecation")
	public  void handleShortcutForRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
				txtArea.setMaxLength(200);
//				txtArea.setReadOnly(true);
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

				String strCaption = "Other Reason";
				
				if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("cashless")) {
					strCaption = "Query Remarks (Cashless)";
				}
				else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("medical")) {
					strCaption = "Query Remarks (Medical)";
				}
				else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("billingFinance")) {
					strCaption = "Query Remarks (Billing/Financial)";
				}
				

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(false);

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
	
	public void loadCVCStatusDropDownValues(BeanItemContainer<SelectValue> statusValueContainer,
			BeanItemContainer<SelectValue> ErrorValueContainer,BeanItemContainer<SelectValue> TeamValueContainer,
			BeanItemContainer<SelectValue> monetaryValueContainer,BeanItemContainer<SelectValue> remediationStatusValueContainer,
			BeanItemContainer<SelectValue> processorValueContainer) {
	
		//reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		cmbAuditStatus.setContainerDataSource(statusValueContainer);
		cmbAuditStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditStatus.setItemCaptionPropertyId("value");
		cmbAuditStatus.setValue(statusValueContainer);
		
		cmbCategoryOfError.setContainerDataSource(ErrorValueContainer);
		cmbCategoryOfError.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCategoryOfError.setItemCaptionPropertyId("commonValue");
		cmbCategoryOfError.setValue(ErrorValueContainer);
		
		cmbTeam.setContainerDataSource(TeamValueContainer);
		cmbTeam.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTeam.setItemCaptionPropertyId("value");
		cmbTeam.setValue(TeamValueContainer);
		
		cmbMonetaryResult.setContainerDataSource(monetaryValueContainer);
		cmbMonetaryResult.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMonetaryResult.setItemCaptionPropertyId("value");
		cmbMonetaryResult.setValue(monetaryValueContainer);
		
		cmbAuditRemediationStatus.setContainerDataSource(remediationStatusValueContainer);
		cmbAuditRemediationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAuditRemediationStatus.setItemCaptionPropertyId("value");
		cmbAuditRemediationStatus.setValue(remediationStatusValueContainer.getItemIds().get(1));
		bean.setRemediationStatus(cmbAuditRemediationStatus.getValue().toString());
		
		cmbProcessor.setContainerDataSource(processorValueContainer);
		cmbProcessor.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProcessor.setItemCaptionPropertyId("value");
		cmbProcessor.setValue(processorValueContainer);
	}

}
