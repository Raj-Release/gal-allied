package com.shaic.claim.cvc.auditqueryreplyprocessing.reimb;

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
import com.shaic.claim.cvc.auditqueryapproval.AuditMedicalQueryTable;
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


public class CVCAuditReimbQryPageUI extends ViewComponent {
	
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
	
//	private TextArea txtOtherRemarks;

//	private TextArea txtClsQryRemarks;
//	private TextArea txtReimbQryRemarks;
//	private TextArea txtBillingFAQryRemarks;
	
//	private Button btnQryAccpt;
	
//	private Button btnQryNotAccpt;
	
//	private String clsQryRemarks;
//	private String reimbQryRemarks;
//	private String billingFAQryRemarks;
	
	
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
		
		preauthIntimationDetailsCarousel.init(bean.getPreauthDto().getNewIntimationDTO(), bean.getPreauthDto().getClaimDTO(), "Claim Audit Query (Reimbursement)");
		

		medicalQryTable = medicalQryTableInstance.get();
		medicalQryTable.init(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN, bean);
		if(bean.getMedicalQryList() != null && !bean.getMedicalQryList().isEmpty()){
			List<SearchCVCAuditClsQryTableDTO> medicalQryList = bean.getMedicalQryList();
			for (SearchCVCAuditClsQryTableDTO auditQryDTO : medicalQryList) {
				this.medicalQryTable.addBeanToList(auditQryDTO);
			}
		}
		// viewDetails
		viewDetails.initView(bean.getPreauthDto().getNewIntimationDTO().getIntimationId(), bean.getTransactionKey(), ViewLevels.INTIMATION, false,"Claim Audit Query (Reimbursement)");

		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setSizeFull();		
		
		verticalLayout = new VerticalLayout(medicalQryTable);
		



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
				                	fireViewEvent(MenuItemBean.CVC_AUDIT_RMB_QRY_RLY, true);
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
					
					bean.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);
			
					fireViewEvent(CVCAuditReimbQryPagePresenter.SUBMIT_EVENT_AUDIT_QUERY_REIMB, bean);
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
	
		

		if(medicalQryTable != null && medicalQryTable.getValues() != null  && !medicalQryTable.getValues().isEmpty()) {
			hasError = !medicalQryTable.isValid();
			
			if(hasError && !medicalQryTable.getErrors().isEmpty()) {
				eMsg.append(medicalQryTable.getErrors());
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
			
			if(medicalQryTable != null && medicalQryTable.getValues() != null) {
				bean.setMedicalQryList(medicalQryTable.getValues());
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
