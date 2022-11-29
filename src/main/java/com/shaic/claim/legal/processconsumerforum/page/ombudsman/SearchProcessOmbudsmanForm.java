/**
 * 
 */
package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.ConsumerFormSearchUI;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalOmbudsman;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
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


public class SearchProcessOmbudsmanForm  extends ViewComponent{
	
	
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
	
	protected Button btnSave;	
	protected Button btnSaveExit;
	protected Button btnCancel;
	
	private BeanFieldGroup<IntimationDetailsDTO> intimationBinder;
	
	private BeanFieldGroup<OmbudsmanDetailsDTO> ombudsmanDetailBinder;
	
	private BeanFieldGroup<DecisionDetailsDto> decisionDetailBinder;
	
	private OmbudsmanDetailsDTO ombudsmanDetailsDTO = new OmbudsmanDetailsDTO();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	//private BeanItemContainer<SelectValue> decision = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private BeanItemContainer<SelectValue> hearingStatus = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private BeanItemContainer<SelectValue> awardStatus = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private BeanItemContainer<SelectValue> compromiseStatus = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private BeanItemContainer<SelectValue> grievanceOutcomeSelectValue = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	
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
	
	private ComboBox cmbOmbudsman;
	private TextField txtComplaintno;
	private DateField dateComplaintRecieptDate;
	private ComboBox cmbaddDays;
	private DateField dateStipulatedDate;
	private TextField txtReferToMedical;
	private DateField dateReceiptOfMedicalOpinion;
	private TextArea  txtOmbudsmanContactDetail;
	private TextArea  txtOmbudsmanContactAddress;
	private CheckBox chkOmbudsmanClaimLock;
	
	private ComboBox cmbDecision;
	private DateField dateOfHearing;
	private ComboBox cmbHearingStatus;
	private ComboBox cmbAwardStatus;
	private DateField nextDateOfHearing;
	private CheckBox chkSelfContainedNote;
	private DateField selfContainedNotePreparationDate;
	private DateField selfContainedNoteSubmissionDate;
	private ComboBox cmbReasonforCompromiseSettlement;
	private TextField txtCompromiseAmount;
	private TextArea  txtRemarksforCompromiseSettlement;
	
	private TextField txtCaseWon;
	private DateField closureDate;
	private TextArea  txtCaseWonRemarks;
	private TextField txtInterest;
	private DateField awardReceiptDate;
	private DateField lastDtForSatisfactionAward;
	private ComboBox cmbCaseaddDays;
	
	private TextArea txtCaseLost;
	private ComboBox cmbReasonforAward;
	private TextArea  txtReasonForCaseLost;
	private CheckBox chkExgratiaAward;
	private DateField exgratiaAwardDate;
	private TextArea  txtReasonsForExgratia;
	private TextField txtExgratiaAwardAmount;
	
	
	VerticalLayout decisionMakingLayout ;
	
	VerticalLayout dynamicDecisionMakingLayout ;
	
	private ComboBox cmbStatusOfCase;
	
	private ComboBox cmbPendingStatus;
	
	FormLayout rightContestLayout;
	
	private VerticalLayout finalLayout ;

	private FormLayout leftContestLayout;

	private FormLayout hearingStatusForm;

	private HorizontalLayout caseWonHori;

	private VerticalLayout caseWonVertical;

	private Date doAddDaysCalculation = null;

	private TextField txtDiagnosis;

	private ComboBox cbxRecievedFrom;

	private ComboBox cmbmovedTo;

	private DateField issueRejectionDate;

	private OptionGroup claimReconsideration;

	private OptionGroup cancellationOfPolicy;

	private OptionGroup reconsiderationCancellationOfPolicy;

	private OptionGroup grievanceRequestInitiated;

	protected DateField reconsiderationReceiptDate;

	protected OptionGroup reconsideration;

	protected DateField reconsiderationRejectDate;

	protected DateField reconsiderationAcceptDate;

	protected TextField acceptanceAmount;

	protected TextField refundofProrataPremium;

	protected DateField prorataPremiumRefundDate;

	protected DateField reconsiderationCancelPolicyAcceptDate;

	protected TextField amountrefundbytheInsured;


	protected DateField grievanceInitiatedDate;

	protected ComboBox grievanceOutcome;

	protected TextArea grievanceRemarks;

	private DateField postponmentDateofHearing;

	protected OptionGroup anyAwardofOmbudsmanContested;

	private TextField txtRemarks;

	protected TextArea awardReservedDetails;

	
	
	/*public SearchProcessOmbudsmanForm() {
		
		btnSave = new Button("Save");
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel = new Button("Exit");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
	}*/

	@PostConstruct
	public void init() {
		ombudsmanDetailsDTO = new OmbudsmanDetailsDTO();
		initBinder(ombudsmanDetailsDTO);
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption("Ombudsman");
		VerticalLayout intimationVerticalLayout = intimationVerticalLayout();
		VerticalLayout ombudsmanVerticalLayout = ombudsmanVerticalLayout();
		VerticalLayout descisionVerticalLayout = decisionVerticalLayout();
		HorizontalLayout statusLayout = statusVerticalLayout();
		VerticalLayout reconsiderVerticalLayout = reconsiderVerticalLayout();
		ConfirmDialog dialog = new ConfirmDialog();
		HorizontalLayout buttonsLayout = new HorizontalLayout(getSaveButtonWithListener(dialog),getSaveNexitButtonWithListener(dialog), getCancelButton(dialog));
		buttonsLayout.setWidth("50%");
		buttonsLayout.setSpacing(true);
		finalLayout = new VerticalLayout(viewDetails,intimationVerticalLayout,reconsiderVerticalLayout,ombudsmanVerticalLayout,descisionVerticalLayout,statusLayout,buttonsLayout);
		finalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		finalLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);
		//finalLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);
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
		
		issueRejectionDate= intimationBinder.buildAndBind("Issue & Rejection Date","issueRejectionDate",DateField.class);
		txtDiagnosis =intimationBinder.buildAndBind("Diagnosis","diagnosis",TextField.class);
		cbxRecievedFrom =intimationBinder.buildAndBind("Received From","receivedFrom",ComboBox.class);
		txtRemarks =intimationBinder.buildAndBind("Remarks","remarks",TextField.class);
		
		txtDiagnosis.setReadOnly(true);
		cbxRecievedFrom.setReadOnly(true);
		doNumberValidation(txtfinancialYear);
		doNumberValidation(txtprovisionAmt);
		btnIntimationSearch = new Button();
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
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtfinancialYear,cbxRepudiation,txtDiagnosis,txtprovisionAmt,issueRejectionDate);
		formLayoutLeft.setCaption("Intimation Details");
		
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo, txtProductNameCode,txtInsuredName,txtRemarks,cbxRecievedFrom);	
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
	
	public VerticalLayout reconsiderVerticalLayout(){
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		claimReconsideration = ombudsmanDetailBinder.buildAndBind("Claim Reconsideration","claimReconsideration",OptionGroup.class);
		cancellationOfPolicy = ombudsmanDetailBinder.buildAndBind("Cancellation Of Policy","cancellationOfPolicy",OptionGroup.class);
		reconsiderationCancellationOfPolicy = ombudsmanDetailBinder.buildAndBind("Reconsideration of Cancellation Policy","reconsiderationCancellationOfPolicy",OptionGroup.class);
		grievanceRequestInitiated = ombudsmanDetailBinder.buildAndBind("Grievance Request Initiated","grievanceRequestInitiated",OptionGroup.class);
		VerticalLayout cancellationOfPolicylayout = new VerticalLayout(cancellationOfPolicy,reconsiderationCancellationOfPolicy);
		cancellationOfPolicylayout.setSpacing(true);
		FormLayout v1 = new FormLayout(claimReconsideration);
		v1.setCaption("Reconsideration Details");
		v1.setMargin(true);
		v1.setWidth("100%");
		v1.setSpacing(true);
		isClaimReconsiderationListener(claimReconsideration, v1);
		FormLayout v2 = new FormLayout(cancellationOfPolicy);
		v2.setCaption("Cancellation Of Policy");
		v2.setMargin(true);
		v2.setWidth("100%");
		v2.setSpacing(true);
		isCancellationOfPolicyListener(cancellationOfPolicy, v2);
		FormLayout v3 = new FormLayout(reconsiderationCancellationOfPolicy);
		v3.setMargin(true);
		v3.setWidth("100%");
		v3.setSpacing(true);
		isReconsiderationCancellationOfPolicyListener(reconsiderationCancellationOfPolicy, v3);
		VerticalLayout v3v2 = new VerticalLayout(v2,v3);
		HorizontalLayout v1v2 = new HorizontalLayout(v1,v3v2);
		v1v2.setCaption("");
		v1v2.setMargin(true);
		v1v2.setWidth("100%");
		v1v2.setSpacing(true);
		VerticalLayout v4 = new VerticalLayout(grievanceRequestInitiated);
		isgrevanceRequestIntiated(grievanceRequestInitiated,v4);
		v4.setCaption("");
		v4.setMargin(true);
		v4.setWidth("100%");
		v4.setSpacing(true);
		// mainVerticalLayout.setCaption("");
		mainVerticalLayout.setSpacing(true);
		 mainVerticalLayout.addComponent(v1v2);
		 mainVerticalLayout.addComponent(v4);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
		return mainVerticalLayout;
	}	
	

	private void isgrevanceRequestIntiated(
			OptionGroup grievanceRequestInitiated, final VerticalLayout v4) {

		if(grievanceRequestInitiated!=null){
			
			
			grievanceRequestInitiated.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					 unbindField(grievanceInitiatedDate, ombudsmanDetailBinder);
					 unbindField(grievanceOutcome, ombudsmanDetailBinder);
					 unbindField(grievanceRemarks, ombudsmanDetailBinder);
					 mandatoryFields.remove(grievanceInitiatedDate);
					 mandatoryFields.remove(grievanceOutcome);
					 mandatoryFields.remove(grievanceRemarks);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 grievanceInitiatedDate = ombudsmanDetailBinder.buildAndBind("Grievance Initiated Date ","grievanceInitiatedDate",DateField.class);
							 grievanceOutcome = ombudsmanDetailBinder.buildAndBind("Grievance Outcome","grievanceOutcome",ComboBox.class);
							 
							 grievanceOutcome.setContainerDataSource(grievanceOutcomeSelectValue);
							 grievanceOutcome.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							 grievanceOutcome.setItemCaptionPropertyId("value");
								
							 grievanceRemarks = ombudsmanDetailBinder.buildAndBind("Grievance Remarks","grievanceRemarks",TextArea.class);
							 v4.addComponent(grievanceInitiatedDate);
							 v4.addComponent(grievanceOutcome);
							 v4.addComponent(grievanceRemarks);
							 mandatoryFields.add(grievanceInitiatedDate);
							 mandatoryFields.add(grievanceOutcome);
							 mandatoryFields.add(grievanceRemarks);
							 showOrHideValidation(false);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 int layoutSize = v4.getComponentCount();
								int i = layoutSize;

							 	while( i > 1 ) {
							 		v4.removeComponent(v4.getComponent((v4.getComponentCount()) - 1));
							    	i--;
							      }
						 }
						
						
				}
			}
		});}
	
		
	}

	private void isClaimReconsiderationListener(OptionGroup claimReconsideration, final FormLayout v1) {
		if(claimReconsideration!=null){
			
			
			claimReconsideration.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					 unbindField(reconsiderationReceiptDate, ombudsmanDetailBinder);
					 unbindField(reconsideration, ombudsmanDetailBinder);
					 unbindField(reconsiderationRejectDate, ombudsmanDetailBinder);
					 unbindField(reconsiderationAcceptDate, ombudsmanDetailBinder);
					 unbindField(acceptanceAmount, ombudsmanDetailBinder);
					 mandatoryFields.remove(reconsiderationReceiptDate);
					 mandatoryFields.remove(reconsideration);
					 mandatoryFields.remove(reconsiderationRejectDate);
					 mandatoryFields.remove(reconsiderationAcceptDate);
					 mandatoryFields.remove(acceptanceAmount);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 
							
							 System.out.println(v1.getComponentCount());
							 reconsiderationReceiptDate = ombudsmanDetailBinder.buildAndBind("Reconsideration Receipt Date","reconsiderationReceiptDate",DateField.class);
							 reconsideration = (OptionGroup) ombudsmanDetailBinder.buildAndBind("Reconsideration","reconsideration",OptionGroup.class);
							 reconsideration.addItems(getReadioButtonOptions());
							 reconsideration.setItemCaption(true, "Accept");
							 reconsideration.setItemCaption(false, "Reject");
							 reconsideration.setStyleName("horizontal");
							 v1.addComponent(reconsiderationReceiptDate);
							 v1.addComponent(reconsideration);
							 isreconsideration(reconsideration,v1);
							 mandatoryFields.add(reconsiderationReceiptDate);
							 mandatoryFields.add(reconsideration);
							 showOrHideValidation(false);	
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 int layoutSize = v1.getComponentCount();
								int i = layoutSize;

							 	while( i > 1 ) {
							 		v1.removeComponent(v1.getComponent((v1.getComponentCount()) - 1));
							    	i--;
							      }
						 }
						
						
				}
			}
			
		});}
	}
	
	private void isreconsideration(OptionGroup reconsideration,
			final FormLayout v1) {
		if(reconsideration!=null){
			
			
			reconsideration.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					unbindField(reconsiderationRejectDate, ombudsmanDetailBinder);
					 unbindField(reconsiderationAcceptDate, ombudsmanDetailBinder);
					 unbindField(acceptanceAmount, ombudsmanDetailBinder);
					 mandatoryFields.remove(reconsiderationAcceptDate);
					 mandatoryFields.remove(acceptanceAmount);
					 mandatoryFields.remove(reconsiderationRejectDate);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){

							 int layoutSize = v1.getComponentCount();
								int i = layoutSize;

							 	while( i > 3 ) {
							 		v1.removeComponent(v1.getComponent((v1.getComponentCount()) - 1));
							    	i--;
							      }
						 
							 
							 reconsiderationAcceptDate = ombudsmanDetailBinder.buildAndBind("Reconsideration Accept Date","reconsiderationAcceptDate",DateField.class);
							 acceptanceAmount = ombudsmanDetailBinder.buildAndBind("Acceptance Amount","acceptanceAmount",TextField.class);
							 acceptanceAmount.setNullRepresentation("");
							 doNumberValidation(acceptanceAmount);
							 v1.addComponent(reconsiderationAcceptDate);
							 v1.addComponent(acceptanceAmount);
							 mandatoryFields.add(reconsiderationAcceptDate);
							 mandatoryFields.add(acceptanceAmount);
							 showOrHideValidation(false);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 int layoutSize = v1.getComponentCount();
								int i = layoutSize;

							 	while( i > 3 ) {
							 		v1.removeComponent(v1.getComponent((v1.getComponentCount()) - 1));
							    	i--;
							      }
							 reconsiderationRejectDate = ombudsmanDetailBinder.buildAndBind("Reconsideration Reject Date","reconsiderationRejectDate",DateField.class);
							 v1.addComponent(reconsiderationRejectDate);
							 mandatoryFields.add(reconsiderationRejectDate);
							 showOrHideValidation(false);
						 }
						
						
				}
			}
		});}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	private void isCancellationOfPolicyListener(OptionGroup cancellationOfpolicy, final FormLayout v1) {
		if(cancellationOfpolicy!=null){
			
			
			cancellationOfpolicy.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					 unbindField(refundofProrataPremium, ombudsmanDetailBinder);
					 unbindField(prorataPremiumRefundDate, ombudsmanDetailBinder);
					 mandatoryFields.remove(refundofProrataPremium);
					 mandatoryFields.remove(prorataPremiumRefundDate);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 refundofProrataPremium = ombudsmanDetailBinder.buildAndBind("Refund of Prorata Premium","refundofProrataPremium",TextField.class);
							 refundofProrataPremium.setNullRepresentation("");
							 doNumberValidation(refundofProrataPremium);
							 prorataPremiumRefundDate = ombudsmanDetailBinder.buildAndBind("Prorata Premium Refund Date","prorataPremiumRefundDate",DateField.class);
							 v1.addComponent(refundofProrataPremium);
							 v1.addComponent(prorataPremiumRefundDate);
							 mandatoryFields.add(refundofProrataPremium);
							 mandatoryFields.add(prorataPremiumRefundDate);
							 showOrHideValidation(false);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 int layoutSize = v1.getComponentCount();
								int i = layoutSize;

							 	while( i > 1 ) {
							 		v1.removeComponent(v1.getComponent((v1.getComponentCount()) - 1));
							    	i--;
							      }
						 }
						
						
				}
			}
		});}
	}
	
	private void isReconsiderationCancellationOfPolicyListener(OptionGroup reconsiderationCancellationOfpolicy, final FormLayout v1) {
		if(reconsiderationCancellationOfpolicy!=null){
			
			
			reconsiderationCancellationOfpolicy.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					 unbindField(reconsiderationCancelPolicyAcceptDate, ombudsmanDetailBinder);
					 unbindField(amountrefundbytheInsured, ombudsmanDetailBinder);

					 mandatoryFields.remove(reconsiderationCancelPolicyAcceptDate);
					 mandatoryFields.remove(amountrefundbytheInsured);
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 reconsiderationCancelPolicyAcceptDate = ombudsmanDetailBinder.buildAndBind("Reconsideration of Cancellation of Policy Accept Date","reconsiderationCancelPolicyAcceptDate",DateField.class);
							 amountrefundbytheInsured = ombudsmanDetailBinder.buildAndBind("Amount Refund by the Insured","amountrefundbytheInsured",TextField.class);
							 amountrefundbytheInsured.setNullRepresentation("");
							 doNumberValidation(amountrefundbytheInsured);
							 v1.addComponent(reconsiderationCancelPolicyAcceptDate);
							 v1.addComponent(amountrefundbytheInsured);
							 mandatoryFields.add(reconsiderationCancelPolicyAcceptDate);
							 mandatoryFields.add(amountrefundbytheInsured);
							 showOrHideValidation(false);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 int layoutSize = v1.getComponentCount();
								int i = layoutSize;

							 	while( i > 1 ) {
							 		v1.removeComponent(v1.getComponent((v1.getComponentCount()) - 1));
							    	i--;
							      }
						 }
						
						
				}
			}
		});}
	}
	
	public VerticalLayout ombudsmanVerticalLayout(){
		
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		
		txtComplaintno = ombudsmanDetailBinder.buildAndBind("Complaint No", "complaintNo", TextField.class);
		cmbOmbudsman = ombudsmanDetailBinder.buildAndBind("Ombudsman Office","ombudsmanOffice",ComboBox.class);
		txtReferToMedical = ombudsmanDetailBinder.buildAndBind("Refer to Medical","referToMedical",TextField.class);
		dateComplaintRecieptDate = ombudsmanDetailBinder.buildAndBind("Complaint Receipt Date","complaintRecieptDate",DateField.class);
		dateStipulatedDate = ombudsmanDetailBinder.buildAndBind("Stipulated Date","stipulateDate",DateField.class);
		cmbaddDays = ombudsmanDetailBinder.buildAndBind("Add Days","addDays",ComboBox.class);
		dateReceiptOfMedicalOpinion= ombudsmanDetailBinder.buildAndBind("Receipt of Medical opinion","receiptOfMedicalOpinion",DateField.class);
		chkOmbudsmanClaimLock=ombudsmanDetailBinder.buildAndBind("Ombudsman Claim Lock","isOmbudsmanClaimLock",CheckBox.class);
		//txtOmbudsmanContactDetail =ombudsmanDetailBinder.buildAndBind(null,"ombudsmanContact",TextArea.class);
		txtOmbudsmanContactAddress =ombudsmanDetailBinder.buildAndBind("Ombudsman Office Contact Details","ombudsmanContactAddress",TextArea.class);
		addAddDateValueChangeListener(dateComplaintRecieptDate, cmbaddDays,dateStipulatedDate);
		addAddDaysValueChangeListener(dateComplaintRecieptDate,cmbaddDays,dateStipulatedDate);
		cmbmovedTo = ombudsmanDetailBinder.buildAndBind("Moved To","movedTo",ComboBox.class);
		ombudsmanContactListener();

		//txtOmbudsmanContactDetail.setVisible(Boolean.FALSE);
		FormLayout formLayoutLeft = new FormLayout(cmbOmbudsman,txtComplaintno,dateComplaintRecieptDate,cmbaddDays,dateStipulatedDate);
		formLayoutLeft.setCaption("Ombudsman Details");
		FormLayout formLayoutReight = new FormLayout(chkOmbudsmanClaimLock,txtOmbudsmanContactAddress,txtReferToMedical, dateReceiptOfMedicalOpinion,cmbmovedTo);	
		
		txtOmbudsmanContactAddress.setVisible(Boolean.FALSE);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		
		 mainVerticalLayout.setCaption("");
		 mainVerticalLayout.addComponent(fieldLayout);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
			mandatoryFields.add(txtComplaintno);
			mandatoryFields.add(cmbOmbudsman);
			mandatoryFields.add(dateStipulatedDate);
			mandatoryFields.add(cmbaddDays);
			mandatoryFields.add(chkOmbudsmanClaimLock);
			mandatoryFields.add(txtOmbudsmanContactAddress);
			
			showOrHideValidation(false);
		 
		 
		return mainVerticalLayout;
	}
	
	public void ombudsmanContactListener(){
			
		cmbOmbudsman.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (null != event && null != event.getProperty()
							&& null != event.getProperty().getValue()) {
						SelectValue selectString = (SelectValue) event.getProperty().getValue();
						fireViewEvent(SearchProcessOmbudsmanPresenter.LEGAL_CONTACT_OMBUDSMAN,selectString);
						/*if(ombudsmanDetailsDTO.getOmbudsmanContactAddress()!=null){
							//txtOmbudsmanContactAddress =ombudsmanDetailBinder.buildAndBind("Ombudsman Office Contact Details","ombudsmanContactAddress",TextArea.class);
							txtOmbudsmanContactAddress.setReadOnly(true);
							txtOmbudsmanContactAddress.setVisible(Boolean.TRUE);
						}*/
					}else{
						txtOmbudsmanContactAddress.setVisible(Boolean.FALSE);
					}
					
				}
			});
		}
	
	public VerticalLayout decisionVerticalLayout(){
		
		cmbDecision = decisionDetailBinder.buildAndBind("Decision","decision",ComboBox.class);
		FormLayout decitionForm = new FormLayout(cmbDecision);
		decisionMakingLayout = new VerticalLayout();
		decisionMakingLayout.setCaption("");
		dynamicDecisionMakingLayout= new VerticalLayout();
		decisionMakingLayout.removeAllComponents();
		decisionMakingLayout.addComponent(decitionForm);
		decisionMakingLayout.addComponent(dynamicDecisionMakingLayout);
		
		decisionMakingLayout.setWidth("100%");
		decisionMakingLayout.addStyleName("layout-with-border");
		addDecisionLsitener(decisionMakingLayout);
		
		mandatoryFields.add(cmbDecision);
		showOrHideValidation(false);
		
		return decisionMakingLayout;
		
	}
	
	private void initBinder(OmbudsmanDetailsDTO ombudsmanDetailsDTO)
	{
		this.ombudsmanDetailBinder = new BeanFieldGroup<OmbudsmanDetailsDTO>(
				OmbudsmanDetailsDTO.class);
		this.ombudsmanDetailBinder.setItemDataSource(ombudsmanDetailsDTO);
		this.ombudsmanDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.intimationBinder = new BeanFieldGroup<IntimationDetailsDTO>(
				IntimationDetailsDTO.class);
		this.intimationBinder.setItemDataSource(ombudsmanDetailsDTO.getIntimationDetailsDTO());
		this.intimationBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		this.decisionDetailBinder = new BeanFieldGroup<DecisionDetailsDto>(
				DecisionDetailsDto.class);
		this.decisionDetailBinder.setItemDataSource(ombudsmanDetailsDTO.getDecisionDetailsDto());
		this.decisionDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setCBXValue(BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> statusoftheCase,
			BeanItemContainer<SelectValue> ombudsmanDetailsByDesc,
			BeanItemContainer<SelectValue> addDays,
			BeanItemContainer<SelectValue> pendingLevel,
			BeanItemContainer<SelectValue> hearingStatus,
			BeanItemContainer<SelectValue> awardStatus,
			BeanItemContainer<SelectValue> compromiseStatus,
			BeanItemContainer<SelectValue> decision,BeanItemContainer<SelectValue> recievedFrom,BeanItemContainer<SelectValue> movedTO, 
			BeanItemContainer<SelectValue> grievanceOutcome){
	
		//this.decision = decision;
		this.hearingStatus = hearingStatus;
		this.awardStatus = awardStatus;
		this.compromiseStatus = compromiseStatus;
		this.grievanceOutcomeSelectValue = grievanceOutcome;
		
		cmbDecision.setContainerDataSource(decision);
		cmbDecision.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDecision.setItemCaptionPropertyId("value");
		
		cbxRepudiation.setContainerDataSource(repudiationMasterValueByCode);
		cbxRepudiation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRepudiation.setItemCaptionPropertyId("value");
		
		//cmbOmbudsman
		cmbOmbudsman.setContainerDataSource(ombudsmanDetailsByDesc);
		cmbOmbudsman.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOmbudsman.setItemCaptionPropertyId("value");
		
		//cmbaddDays
		cmbaddDays.setContainerDataSource(addDays);
		cmbaddDays.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbaddDays.setItemCaptionPropertyId("value");
		
		/*cmbCaseaddDays.setContainerDataSource(addDays);
		cmbCaseaddDays.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCaseaddDays.setItemCaptionPropertyId("value");*/
		//cmbPendingStatus
		
		cmbPendingStatus.setContainerDataSource(pendingLevel);
		cmbPendingStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPendingStatus.setItemCaptionPropertyId("value");
		
		//cmbStatusOfCase;
		cmbStatusOfCase.setContainerDataSource(statusoftheCase);
		cmbStatusOfCase.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatusOfCase.setItemCaptionPropertyId("value");
		
		cmbmovedTo.setContainerDataSource(movedTO);
		cmbmovedTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbmovedTo.setItemCaptionPropertyId("value");
		
		cbxRecievedFrom.setReadOnly(false);
		cbxRecievedFrom.setContainerDataSource(recievedFrom);
		cbxRecievedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRecievedFrom.setItemCaptionPropertyId("value");
		cbxRecievedFrom.setReadOnly(true);
		
	}
	
	public HorizontalLayout buildContestLayout(){
		
		cmbHearingStatus = decisionDetailBinder.buildAndBind("Hearing Status","hearingStatus",ComboBox.class);
		dateOfHearing= decisionDetailBinder.buildAndBind("Date of Hearing","dateOfHearing",DateField.class);
		postponmentDateofHearing= decisionDetailBinder.buildAndBind("Postponment Date of Hearing","postponmentDateofHearing",DateField.class);
		chkSelfContainedNote=decisionDetailBinder.buildAndBind("Self Contained Note","selfContainedNote",CheckBox.class);
		
		cmbHearingStatus.setContainerDataSource(hearingStatus);
		cmbHearingStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHearingStatus.setItemCaptionPropertyId("value");
		
		hearingStatusForm = new FormLayout(cmbHearingStatus);
		hearingStatusForm.setMargin(false);
		
	    leftContestLayout = new FormLayout(dateOfHearing,postponmentDateofHearing,hearingStatusForm);
		rightContestLayout = new FormLayout(chkSelfContainedNote);
		
		mandatoryFields.add(cmbHearingStatus);
		cmbHearingStatus.setValidationVisible(Boolean.FALSE);
		cmbHearingStatus.setRequired(Boolean.TRUE);
		HorizontalLayout contestLayout = new HorizontalLayout(leftContestLayout,rightContestLayout);
		contestLayout.setSpacing(true);
		contestLayout.setWidth("100%");
		addHearingStatusListener(hearingStatusForm);
		addSelfContainedListener();
		return contestLayout;
		
		
	}
	
	public HorizontalLayout buildCompromiseLayout() {

		//cmbReasonforCompromiseSettlement.setContainerDataSource(compromiseStatus);
		//cmbReasonforCompromiseSettlement.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cmbReasonforCompromiseSettlement.setItemCaptionPropertyId("value");
		FormLayout leftCompromiseLayout = new FormLayout(txtRemarksforCompromiseSettlement);
		FormLayout rightCompromiseLayout  = new FormLayout(txtCompromiseAmount);
		HorizontalLayout compromiseLayout = new HorizontalLayout(leftCompromiseLayout,rightCompromiseLayout);
		compromiseLayout.setWidth("100%");
		compromiseLayout.setSpacing(true);

		return compromiseLayout;

	}
	
	public void addSelfContainedListener(){
		
		chkSelfContainedNote.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					boolean value = (Boolean) event.getProperty()
							.getValue();
					unbindField(selfContainedNotePreparationDate, decisionDetailBinder);
					unbindField(selfContainedNoteSubmissionDate, decisionDetailBinder);
					mandatoryFields.remove(selfContainedNotePreparationDate);
					selfContainedNotePreparationDate=decisionDetailBinder.buildAndBind("Self Contained Note Preparation Date","selfConNotePreDate",DateField.class);
					selfContainedNoteSubmissionDate=decisionDetailBinder.buildAndBind("Self Contained Note Submission Date","selfConNoteSubDate",DateField.class);
					
					selfContainedNotePreparationDate.setValidationVisible(Boolean.FALSE);
					selfContainedNotePreparationDate.setRequired(Boolean.TRUE);
					rightContestLayout.addComponent(selfContainedNotePreparationDate);
					rightContestLayout.addComponent(selfContainedNoteSubmissionDate);
					mandatoryFields.add(selfContainedNotePreparationDate);
					if(! value){
						rightContestLayout.removeComponent(selfContainedNotePreparationDate);
						rightContestLayout.removeComponent(selfContainedNoteSubmissionDate);
					}
				}
				
			}
		});
	}
	
	public void addDecisionLsitener(VerticalLayout decisionMakingLayout){
		cmbDecision.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null){
					mandatoryFields.remove(cmbHearingStatus);
					mandatoryFields.remove(cmbAwardStatus);
					mandatoryFields.remove(selfContainedNotePreparationDate);
					mandatoryFields.remove(txtReasonForCaseLost);
					mandatoryFields.remove(cmbReasonforAward);
					mandatoryFields.remove(exgratiaAwardDate);
					mandatoryFields.remove(txtExgratiaAwardAmount);
					mandatoryFields.remove(cmbReasonforCompromiseSettlement);
					mandatoryFields.remove(txtCompromiseAmount);
					
					unbindDecisionLayout();
					if (value.getValue().equalsIgnoreCase("Contest")){
						showOrHideValidation(false);
						dynamicDecisionMakingLayout.removeAllComponents();
						HorizontalLayout hl = new  HorizontalLayout(buildContestLayout());
						dynamicDecisionMakingLayout.addComponent(hl);
					} else {
						//cmbReasonforCompromiseSettlement= decisionDetailBinder.buildAndBind("Reason for Compromise Settlement","reasonForCompromiseSettlement",ComboBox.class);
						txtCompromiseAmount = decisionDetailBinder.buildAndBind("Compromise Amount", "compromiseAmount", TextField.class);
						txtRemarksforCompromiseSettlement = decisionDetailBinder.buildAndBind("Compromise Settlement Details", "remarksCompromiseSettlement", TextArea.class);
						txtCompromiseAmount.setNullRepresentation("");
						doNumberValidation(txtCompromiseAmount);
						//mandatoryFields.add(cmbReasonforCompromiseSettlement);
						mandatoryFields.add(txtCompromiseAmount);
						showOrHideValidation(false);
						dynamicDecisionMakingLayout.removeAllComponents();
						HorizontalLayout hl = new  HorizontalLayout(buildCompromiseLayout());
						dynamicDecisionMakingLayout.addComponent(hl);
					}
				}
				
			}
		});
		
	}
	
	
	
	public void addAwardStatusListener(){
		cmbAwardStatus.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null){
					unbindCaseWonLayout();
					unbindCaseLostLayout();
					if (value.getValue().equalsIgnoreCase("Case Won")){
						if(dynamicDecisionMakingLayout.getComponentCount() > 1){
							int componentCount = dynamicDecisionMakingLayout.getComponentCount();
							dynamicDecisionMakingLayout.removeComponent(dynamicDecisionMakingLayout.getComponent(componentCount - 1));
						}
						dynamicDecisionMakingLayout.addComponent(buildCaseWonLayout());
					} else if (value.getValue().equalsIgnoreCase("Case Lost")) {
						if(dynamicDecisionMakingLayout.getComponentCount() > 1){
							int componentCount = dynamicDecisionMakingLayout.getComponentCount();
							dynamicDecisionMakingLayout.removeComponent(dynamicDecisionMakingLayout.getComponent(componentCount - 1));
						}
						unbindCaseLostLayout();
						dynamicDecisionMakingLayout.addComponent(buildCaseLostLayout());
					} 
				}
				else {
					if(dynamicDecisionMakingLayout.getComponentCount() > 1){
						int componentCount = dynamicDecisionMakingLayout.getComponentCount();
						dynamicDecisionMakingLayout.removeComponent(dynamicDecisionMakingLayout.getComponent(componentCount - 1));
					}
					unbindCaseLostLayout();
					unbindCaseWonLayout();
				}
			}

		
		});
		
	}
	
	private void unbindDecisionLayout() {
		unbindField(cmbHearingStatus, decisionDetailBinder);
		unbindField(postponmentDateofHearing, decisionDetailBinder);
		unbindField(cmbAwardStatus, decisionDetailBinder);
		unbindField(selfContainedNotePreparationDate, decisionDetailBinder);
		unbindField(cmbReasonforAward, decisionDetailBinder);
		unbindField(txtReasonForCaseLost, decisionDetailBinder);
		unbindField(exgratiaAwardDate, decisionDetailBinder);
		unbindField(txtExgratiaAwardAmount, decisionDetailBinder);
		unbindField(cmbReasonforCompromiseSettlement, decisionDetailBinder);
		unbindField(txtCompromiseAmount, decisionDetailBinder);
		unbindField(dateOfHearing, decisionDetailBinder);
		unbindField(nextDateOfHearing , decisionDetailBinder);
		unbindField(chkSelfContainedNote, decisionDetailBinder);
		unbindField(selfContainedNoteSubmissionDate, decisionDetailBinder);
		unbindField(txtReasonsForExgratia, decisionDetailBinder);
		unbindField(txtRemarksforCompromiseSettlement, decisionDetailBinder);
		
	}
	
	private void unbindCaseLostLayout() {
		unbindField(anyAwardofOmbudsmanContested, decisionDetailBinder);
		unbindField(txtCaseLost, decisionDetailBinder);
		unbindField(closureDate, decisionDetailBinder);
		unbindField(cmbReasonforAward, decisionDetailBinder);
		unbindField(txtReasonForCaseLost, decisionDetailBinder);
		unbindField(chkExgratiaAward, decisionDetailBinder);
		unbindField(txtInterest, decisionDetailBinder);
		unbindField(awardReceiptDate, decisionDetailBinder);
		unbindField(lastDtForSatisfactionAward, decisionDetailBinder);
		unbindField(cmbCaseaddDays, decisionDetailBinder);
		
		mandatoryFields.remove(txtCaseLost);
		mandatoryFields.remove(closureDate);
		mandatoryFields.remove(cmbReasonforAward);
		mandatoryFields.remove(txtReasonForCaseLost);
		mandatoryFields.remove(chkExgratiaAward);
		mandatoryFields.remove(txtInterest);
		mandatoryFields.remove(awardReceiptDate);
		mandatoryFields.remove(lastDtForSatisfactionAward);
		mandatoryFields.remove(cmbCaseaddDays); 
		//mandatoryFields.remove(anyAwardofOmbudsmanContested);
	}
	
	private void unbindCaseWonLayout() {
		unbindField(txtCaseWon, decisionDetailBinder);
		unbindField(closureDate, decisionDetailBinder);
		unbindField(txtCaseWonRemarks, decisionDetailBinder);
		unbindField(txtInterest, decisionDetailBinder);
		unbindField(awardReceiptDate, decisionDetailBinder);
		unbindField(lastDtForSatisfactionAward, decisionDetailBinder);
		unbindField(cmbCaseaddDays, decisionDetailBinder);
		
		mandatoryFields.remove(txtCaseWon);
		mandatoryFields.remove(closureDate);
		mandatoryFields.remove(awardReceiptDate);
		mandatoryFields.remove(lastDtForSatisfactionAward);
		mandatoryFields.remove(cmbCaseaddDays);
	}
	
	/*public BeanItemContainer<SelectValue> getSelectValueContainer() {

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(1l);
					selectValue1.setValue("Contest");
					SelectValue selectValue2 = new SelectValue();
					selectValue2.setId(2l);
					selectValue2.setValue("Compromise");
					
					selectValueList.add(selectValue2);
					selectValueList.add(selectValue1);
			selectValueContainer.addAll(selectValueList);

		return selectValueContainer;
	}*/
	
	public BeanItemContainer<SelectValue> getAwardSelectValueContainer() {

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(1l);
					selectValue1.setValue("Case Won");
					SelectValue selectValue2 = new SelectValue();
					selectValue2.setId(2l);
					selectValue2.setValue("Case Lost");
					
					selectValueList.add(selectValue2);
					selectValueList.add(selectValue1);
			selectValueContainer.addAll(selectValueList);

		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getHearingStatusSelectValueContainer() {

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(1l);
					selectValue1.setValue("Award Passed");
					SelectValue selectValue2 = new SelectValue();
					selectValue2.setId(2l);
					selectValue2.setValue("Adjournment");
					
					selectValueList.add(selectValue2);
					selectValueList.add(selectValue1);
			selectValueContainer.addAll(selectValueList);

		return selectValueContainer;
	}
	
	public HorizontalLayout statusVerticalLayout() {

		HorizontalLayout mainVerticalLayout = new HorizontalLayout();
		
		cmbStatusOfCase = decisionDetailBinder.buildAndBind("Status of Case","statusOfCase",ComboBox.class);
		cmbPendingStatus = decisionDetailBinder.buildAndBind("Pending Status","pendingStatus",ComboBox.class);


		FormLayout formLayoutLeft = new FormLayout(cmbStatusOfCase);
		
		FormLayout formLayoutReight = new FormLayout(cmbPendingStatus);

		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,
				formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");

		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setCaption("Status");
		mainVerticalLayout.addStyleName("layout-with-border");

		mandatoryFields.add(cmbStatusOfCase);
		mandatoryFields.add(cmbPendingStatus);
		
		showOrHideValidation(false);
		
		return mainVerticalLayout;
	}
	
	public void addHearingStatusListener(final FormLayout hearingStatusForm){
		cmbHearingStatus.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null){
					unbindField(nextDateOfHearing, decisionDetailBinder);
					unbindField(cmbAwardStatus, decisionDetailBinder);
					unbindField(awardReservedDetails, decisionDetailBinder);
					if (value.getValue().equalsIgnoreCase("Award Passed")){
						cmbAwardStatus = decisionDetailBinder.buildAndBind("Award Status","awardStatus",ComboBox.class);
						
						
						cmbAwardStatus.setContainerDataSource(awardStatus);
						cmbAwardStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbAwardStatus.setItemCaptionPropertyId("value");
						cmbAwardStatus.setValidationVisible(Boolean.FALSE);
						cmbAwardStatus.setRequired(Boolean.TRUE);
						
						mandatoryFields.add(cmbAwardStatus);
						int i = hearingStatusForm.getComponentCount();
						while( i > 1 ) {
							hearingStatusForm.removeComponent(hearingStatusForm.getComponent((hearingStatusForm.getComponentCount()) - 1));
					    	i--;
					      }
						
						hearingStatusForm.addComponent(cmbAwardStatus);
						addAwardStatusListener();
					} else if (value.getValue().equalsIgnoreCase("Adjournment")) {
						nextDateOfHearing= decisionDetailBinder.buildAndBind("Next Date of Hearing","nxtDateOfHearing",DateField.class);
						if(cmbAwardStatus!=null){
							cmbAwardStatus.setValue(null);
							addAwardStatusListener();
						}
						int i = hearingStatusForm.getComponentCount();
						while( i > 1 ) {
							hearingStatusForm.removeComponent(hearingStatusForm.getComponent((hearingStatusForm.getComponentCount()) - 1));
					    	i--;
					      }
						hearingStatusForm.addComponent(nextDateOfHearing);
					} else {
						if(cmbAwardStatus!=null){
							cmbAwardStatus.setValue(null);
							addAwardStatusListener();
						}
						int i = hearingStatusForm.getComponentCount();
						while( i > 1 ) {
							hearingStatusForm.removeComponent(hearingStatusForm.getComponent((hearingStatusForm.getComponentCount()) - 1));
					    	i--;
					      }
						awardReservedDetails= decisionDetailBinder.buildAndBind("Award Reserved Details","awardReservedDetails",TextArea.class);
						hearingStatusForm.addComponent(awardReservedDetails);
					}
				}
				
			}
		});
		
	}
	
	public HorizontalLayout buildCaseWonLayout() {
		
		//txtCaseWon = decisionDetailBinder.buildAndBind("Case Won", "caseWon", TextField.class);
		//closureDate=decisionDetailBinder.buildAndBind("Closure Date","closurDate",DateField.class);
		txtCaseWonRemarks = decisionDetailBinder.buildAndBind("Case Won Details", "caseWonRemarks", TextArea.class);
		//txtInterest = decisionDetailBinder.buildAndBind("Interest", "interest", TextField.class);
		awardReceiptDate=decisionDetailBinder.buildAndBind("Award Receipt Date","awardRecepitDate",DateField.class);
		//lastDtForSatisfactionAward=decisionDetailBinder.buildAndBind("Last Dt. for Satisfaction Award","lastDateForSatisfactionAward",DateField.class);
		//cmbCaseaddDays = decisionDetailBinder.buildAndBind("Add Days","wonAddDays",ComboBox.class);
		//cmbCaseaddDays.setContainerDataSource(cmbaddDays.getContainerDataSource());
		//closureDate.setValidationVisible(Boolean.FALSE);
		awardReceiptDate.setValidationVisible(Boolean.FALSE);
		//lastDtForSatisfactionAward.setValidationVisible(Boolean.FALSE);
		//closureDate.setComponentError(null);
		//awardReceiptDate.setComponentError(null);
		//awardReceiptDate.setComponentError(null);
		//lastDtForSatisfactionAward.setReadOnly(false);
		//addAddDateValueChangeListener(awardReceiptDate, cmbCaseaddDays,lastDtForSatisfactionAward);
		//addAddDaysValueChangeListener(awardReceiptDate,cmbCaseaddDays,lastDtForSatisfactionAward);
		
		FormLayout leftCaseWon = new FormLayout(txtCaseWonRemarks);
		
		FormLayout reightCaseWon = new FormLayout(awardReceiptDate);
		
		HorizontalLayout caseWonHori = new HorizontalLayout(leftCaseWon,reightCaseWon);
		caseWonHori.setWidth("100%");
		caseWonHori.setSpacing(true);
		
		//mandatoryFields.add(txtCaseWon);
		//mandatoryFields.add(closureDate);
		mandatoryFields.add(awardReceiptDate);
		//mandatoryFields.add(lastDtForSatisfactionAward);
		//txtCaseWon.setRequired(Boolean.TRUE);
		//closureDate.setRequired(true);
		awardReceiptDate.setRequired(true);
		//cmbCaseaddDays.setRequired(true);
		//lastDtForSatisfactionAward.setRequired(true);
		return caseWonHori;
		
	}
	
	public VerticalLayout buildCaseLostLayout() {

		txtCaseLost = decisionDetailBinder.buildAndBind("Case Lost Details", "caseLost", TextArea.class);
		anyAwardofOmbudsmanContested = decisionDetailBinder.buildAndBind("Any Award of Ombudsman Contested","anyAwardofOmbudsmanContested",OptionGroup.class);
		//cmbReasonforAward= decisionDetailBinder.buildAndBind("Reason for  Award","reasonForAwards",ComboBox.class);
		txtReasonForCaseLost = decisionDetailBinder.buildAndBind("Details of the Contest", "reasonForCaseLost", TextArea.class);
		chkExgratiaAward=decisionDetailBinder.buildAndBind("Exgratia Award","exgratiaAward",CheckBox.class);
		cmbCaseaddDays = decisionDetailBinder.buildAndBind("Add Days","lostAddDays",ComboBox.class);
		cmbCaseaddDays.setContainerDataSource(cmbaddDays.getContainerDataSource());
		txtInterest = decisionDetailBinder.buildAndBind("Amount", "interest", TextField.class);
		awardReceiptDate=decisionDetailBinder.buildAndBind("Award Receipt Date","lostAwardRecepitDate",DateField.class);
		lastDtForSatisfactionAward=decisionDetailBinder.buildAndBind("Last Dt. for Satisfaction Award","lostLastDateForSatisfactionAward",DateField.class);
		lastDtForSatisfactionAward.setReadOnly(false);
		addAddDateValueChangeListener(awardReceiptDate, cmbCaseaddDays,lastDtForSatisfactionAward);
		addAddDaysValueChangeListener(awardReceiptDate,cmbCaseaddDays,lastDtForSatisfactionAward);
		
		//closureDate.setValidationVisible(Boolean.FALSE);
		awardReceiptDate.setValidationVisible(Boolean.FALSE);
		lastDtForSatisfactionAward.setValidationVisible(Boolean.FALSE);
		//cmbReasonforAward.setValidationVisible(Boolean.FALSE);
		txtReasonForCaseLost.setValidationVisible(Boolean.FALSE);
		cmbCaseaddDays.setValidationVisible(Boolean.FALSE);
		
		//closureDate.setComponentError(null);
		awardReceiptDate.setComponentError(null);
		lastDtForSatisfactionAward.setComponentError(null);
		//cmbReasonforAward.setComponentError(null);
		txtReasonForCaseLost.setComponentError(null);
		cmbCaseaddDays.setComponentError(null);
		
		cmbAwardStatus.setContainerDataSource(awardStatus);
		cmbAwardStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAwardStatus.setItemCaptionPropertyId("value");
		
		//cmbReasonforAward.setContainerDataSource(compromiseStatus);
		//cmbReasonforAward.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cmbReasonforAward.setItemCaptionPropertyId("value");
		
		//cmbReasonforAward.removeAllValidators();
		txtReasonForCaseLost.removeAllValidators();
		cmbCaseaddDays.removeAllValidators();
		FormLayout leftCaseLost = new FormLayout(txtCaseLost, anyAwardofOmbudsmanContested,txtReasonForCaseLost,chkExgratiaAward);

		FormLayout reightCaseLost = new FormLayout(txtInterest,
				awardReceiptDate, cmbCaseaddDays, lastDtForSatisfactionAward);

		HorizontalLayout caseLostHori = new HorizontalLayout(leftCaseLost,
				reightCaseLost);
		caseLostHori.setWidth("100%");
		caseLostHori.setSpacing(true);
		
		addChekExgratiaListener();
		
		mandatoryFields.add(cmbDecision);
		mandatoryFields.add(txtCaseLost);
		//mandatoryFields.add(closureDate);
		mandatoryFields.add(awardReceiptDate);
		mandatoryFields.add(lastDtForSatisfactionAward);
		mandatoryFields.add(cmbCaseaddDays);
		
		txtCaseLost.setRequired(true);
		//closureDate.setRequired(true);
		awardReceiptDate.setRequired(true);
		lastDtForSatisfactionAward.setRequired(true);
		cmbCaseaddDays.setRequired(true);
		 caseWonVertical = new VerticalLayout(caseLostHori);

		return caseWonVertical;

	}

	private void addChekExgratiaListener() {
		chkExgratiaAward.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					boolean value = (Boolean) event.getProperty()
							.getValue();
					unbindExdratiaLayout();
					HorizontalLayout exgar = new HorizontalLayout(buildExdratiaLayout());
					if(! value){
						int layoutSize = caseWonVertical.getComponentCount();
						caseWonVertical.removeComponent(caseWonVertical.getComponent(layoutSize - 1));
					} else {
						caseWonVertical.addComponent(exgar);
					}
				}
				
			}
		});
		
	}
	
	
	private void unbindExdratiaLayout() {
		unbindField(exgratiaAwardDate, decisionDetailBinder);
		unbindField(txtReasonsForExgratia, decisionDetailBinder);
		unbindField(txtExgratiaAwardAmount, decisionDetailBinder);
		
		mandatoryFields.remove(exgratiaAwardDate);
		mandatoryFields.remove(txtExgratiaAwardAmount);
	}
	
	public HorizontalLayout buildExdratiaLayout() {
		
		exgratiaAwardDate=decisionDetailBinder.buildAndBind("Exgratia Award Date","exgratiaAwardDate",DateField.class);
		txtReasonsForExgratia = decisionDetailBinder.buildAndBind("Reasons for Exgratia", "reasonForExgratia", TextArea.class);
		txtExgratiaAwardAmount = decisionDetailBinder.buildAndBind("Exgratia Award Amount", "exgratiaAwardAmount", TextField.class);
		
		txtExgratiaAwardAmount.setNullRepresentation("");
		txtExgratiaAwardAmount.setValidationVisible(Boolean.FALSE);
		txtExgratiaAwardAmount.setComponentError(null);
		
		doNumberValidation(txtExgratiaAwardAmount);
		
		exgratiaAwardDate.setValidationVisible(Boolean.FALSE);
		exgratiaAwardDate.setComponentError(null);
		
		FormLayout exgratialeftform = new FormLayout(exgratiaAwardDate,txtReasonsForExgratia);
		FormLayout exgratiaRightform = new FormLayout(txtExgratiaAwardAmount);
		HorizontalLayout exgratiaLayout = new HorizontalLayout(exgratialeftform,exgratiaRightform);
		exgratiaLayout.setSpacing(true);
		
		mandatoryFields.add(exgratiaAwardDate);
		mandatoryFields.add(txtExgratiaAwardAmount);
		exgratiaAwardDate.setRequired(true);
		txtExgratiaAwardAmount.setRequired(true);
		return exgratiaLayout;
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
					fireViewEvent(SearchProcessOmbudsmanPresenter.LEGAL_SAVE_OMBUDSMAN,ombudsmanDetailsDTO);
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
					fireViewEvent(SearchProcessOmbudsmanPresenter.LEGAL_SAVE_OMBUDSMAN,ombudsmanDetailsDTO);
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
	
private boolean isValid() {
		

		// TODO Auto-generated method stub
		Boolean hasError = false;
		showOrHideValidation(true);
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			ombudsmanDetailsDTO.setUsername(userName);
			
			String eMsg = "";		
			if (!this.ombudsmanDetailBinder.isValid()) {

				for (Field<?> field : this.ombudsmanDetailBinder.getFields()) {
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
			
			if (!this.decisionDetailBinder.isValid()) {

				for (Field<?> field : this.decisionDetailBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if(!hasError){
				ombudsmanDetailBinder.commit();
				intimationBinder.commit();
				decisionDetailBinder.commit();
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
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	public void populateFieldValues(Claim claim,LegalOmbudsman legalOmbudsman,String diagnosis) {
		
		if(legalOmbudsman==null){
		txtIntimationNo.setValue(claim.getIntimation().getIntimationId());
		txtPolicyNo.setValue(claim.getIntimation().getPolicy().getPolicyNumber());
		txtProductNameCode.setValue(claim.getIntimation().getPolicy().getProductName());
		txtfinancialYear.setValue(claim.getIntimation().getPolicy().getPolicyYear().toString());
		if(claim.getCurrentProvisionAmount()!=null){
			txtprovisionAmt.setValue(claim.getCurrentProvisionAmount().toString());
		}
		txtInsuredName.setValue(claim.getIntimation().getInsured().getInsuredName());
		chkOmbudsmanClaimLock.setValue(Boolean.TRUE);

		txtDiagnosis.setReadOnly(Boolean.FALSE);
		txtDiagnosis.setValue(diagnosis);
		txtDiagnosis.setReadOnly(Boolean.TRUE);
	
		viewDetails.initView(claim.getIntimation().getIntimationId(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_OMBUDSMAN);
		
		IntimationDetailsDTO intimationDetailsDTO = ombudsmanDetailsDTO.getIntimationDetailsDTO();
		intimationDetailsDTO.setClaimKey(claim);
		}
		
		if(legalOmbudsman!=null){
			IntimationDetailsDTO intimationDetailsDTO = ombudsmanDetailsDTO.getIntimationDetailsDTO();
			intimationDetailsDTO.setClaimKey(claim);
			viewDetails.initView(claim.getIntimation().getIntimationId(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_OMBUDSMAN);
			txtIntimationNo.setValue(legalOmbudsman.getIntimationNumber());
			txtPolicyNo.setValue(legalOmbudsman.getPolicyNumber());
			txtProductNameCode.setValue(legalOmbudsman.getProductName());
			if(legalOmbudsman.getFinancialYear()!=null){
				txtfinancialYear.setValue(legalOmbudsman.getFinancialYear().toString());
			}
				txtDiagnosis.setReadOnly(Boolean.FALSE);
				txtDiagnosis.setValue(diagnosis);
				txtDiagnosis.setReadOnly(Boolean.TRUE);
			txtRemarks.setValue(legalOmbudsman.getRemarks());
			if(legalOmbudsman.getProvisionAmount()!=null){
				txtprovisionAmt.setValue(legalOmbudsman.getProvisionAmount().toString());
			}
			txtInsuredName.setValue(legalOmbudsman.getInsuredName());
			MastersValue recievedFrom = legalOmbudsman.getReceievedFrom();
			if(recievedFrom!=null){
				cbxRecievedFrom.setReadOnly(false);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(recievedFrom.getKey());
				selectValue.setValue(recievedFrom.getValue());
				cbxRecievedFrom.setValue(selectValue);
				cbxRecievedFrom.setReadOnly(true);
			}
			issueRejectionDate.setValue(legalOmbudsman.getIssueRejectionDate());
			MastersValue repudiationId = legalOmbudsman.getRepudiationId();
			if(repudiationId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(repudiationId.getKey());
				selectValue.setValue(repudiationId.getValue());
				cbxRepudiation.setValue(selectValue);
			}
			chkOmbudsmanClaimLock.setValue(legalOmbudsman.getOmbudLockFlag());
			txtComplaintno.setValue(legalOmbudsman.getComplaintNumber());
			dateComplaintRecieptDate.setValue(legalOmbudsman.getComplaintReceiptDate());
			dateStipulatedDate.setValue(legalOmbudsman.getOmbudStipulatedDate());
			txtReferToMedical.setValue(legalOmbudsman.getReferToMedical());
			dateReceiptOfMedicalOpinion.setValue(legalOmbudsman.getReceiptMedicalOpnDate());
			if(legalOmbudsman.getOmbudCpu()!=null){
				MasOmbudsman cpuCode = legalOmbudsman.getOmbudCpu();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(cpuCode.getKey());
				selectValue.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getCpuCodeDescription());
				cmbOmbudsman.setValue(selectValue);/*
				txtOmbudsmanContactAddress.setValue(cpuCode.getOmbName() + ", "  + cpuCode.getOmbAddress1() + 
					(cpuCode.getOmbAddress2()!=null?", " + cpuCode.getOmbAddress2() : "") + 
					(cpuCode.getOmbAddress3()!=null?", " + cpuCode.getOmbAddress3() : "") + 
					(cpuCode.getOmbAddress4()!=null?", " + cpuCode.getOmbAddress4() : ""));
				txtOmbudsmanContactAddress.setVisible(Boolean.TRUE);*/
				ombudsmanContactListener();
			}
			if(legalOmbudsman.getOmbudDecisionId()!=null){
				MastersValue ombudDecisionId = legalOmbudsman.getOmbudDecisionId();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(ombudDecisionId.getKey());
				selectValue.setValue(ombudDecisionId.getValue());
				cmbDecision.setValue(selectValue);
				if("COMPROMISE".equalsIgnoreCase(ombudDecisionId.getValue())){
					if(legalOmbudsman.getCompromiseSettleRsnId()!=null){
						MastersValue compSettleReason = legalOmbudsman.getCompromiseSettleRsnId();
						SelectValue selectValue1 = new SelectValue();
						selectValue1.setId(compSettleReason.getKey());
						selectValue1.setValue(compSettleReason.getValue());
						cmbReasonforCompromiseSettlement.setValue(selectValue1);
					}
						if(legalOmbudsman.getCompromiseAmount()!=null){
							txtCompromiseAmount.setValue(legalOmbudsman.getCompromiseAmount().toString());
						}
					 txtRemarksforCompromiseSettlement.setValue(legalOmbudsman.getCompromiseSettleRemark());
					
				}else{
					dateOfHearing.setValue(legalOmbudsman.getContestHrgdate());
					postponmentDateofHearing.setValue(legalOmbudsman.getPpDateHearing());
					
					if(legalOmbudsman.getHearingStatusId()!=null){
						MastersValue hearingStatusId = legalOmbudsman.getHearingStatusId();
						SelectValue selectValue1 = new SelectValue();
						selectValue1.setId(hearingStatusId.getKey());
						selectValue1.setValue(hearingStatusId.getValue());
						cmbHearingStatus.setValue(selectValue1);
						if("ADJOURNMENT".equalsIgnoreCase(hearingStatusId.getValue())){
							nextDateOfHearing.setValue(legalOmbudsman.getAdjournmenNxtHearingDate());
						}else if("AWARD PASSED".equalsIgnoreCase(hearingStatusId.getValue())){
							if(legalOmbudsman.getAwardStatusId()!=null){
								MastersValue awardStatusId = legalOmbudsman.getAwardStatusId();
								SelectValue selectValue2 = new SelectValue();
								selectValue2.setId(awardStatusId.getKey());
								selectValue2.setValue(awardStatusId.getValue());
								cmbAwardStatus.setValue(selectValue2);
								if("CASE WON".equalsIgnoreCase(awardStatusId.getValue())){
								//txtCaseWon.setValue(legalOmbudsman.getCaseWon());
								txtCaseWonRemarks.setValue(legalOmbudsman.getCaseWonRemarks());
								//closureDate.setValue(legalOmbudsman.getClosureDate());
								//txtInterest.setValue(legalOmbudsman.getWonInterest());
								awardReceiptDate.setValue(legalOmbudsman.getAwardReceiptDate());
								//lastDtForSatisfactionAward.setValue(legalOmbudsman.getLastDtStatisfactionAwd());
								if(legalOmbudsman.getLostAddDaysId()!=null){
									MastersValue addDaysId = legalOmbudsman.getLostAddDaysId();
									SelectValue selectValue5 = new SelectValue();
									selectValue5.setId(addDaysId.getKey());
									selectValue5.setValue(addDaysId.getValue());
									cmbCaseaddDays.setValue(selectValue5);
								}
								}else{
									txtCaseLost.setValue(legalOmbudsman.getCaseLost());
									if(legalOmbudsman.getAwardOmbContested()!=null){
										if("Y".equalsIgnoreCase(legalOmbudsman.getAwardOmbContested())){
											anyAwardofOmbudsmanContested.setValue(Boolean.TRUE);
										}else{
											anyAwardofOmbudsmanContested.setValue(Boolean.FALSE);
										}
									}
									if(legalOmbudsman.getLostRsnAwdId()!=null){
										MastersValue reason = legalOmbudsman.getLostRsnAwdId();
										SelectValue selectValue3= new SelectValue();
										selectValue3.setId(reason.getKey());
										selectValue3.setValue(reason.getValue());
										cmbReasonforAward.setValue(selectValue3);
									}
									if(legalOmbudsman.getLostAddDaysId()!=null){
										MastersValue addDaysId = legalOmbudsman.getLostAddDaysId();
										SelectValue selectValue4 = new SelectValue();
										selectValue4.setId(addDaysId.getKey());
										selectValue4.setValue(addDaysId.getValue());
										cmbCaseaddDays.setValue(selectValue4);
									}
									txtReasonForCaseLost.setValue(legalOmbudsman.getLostCaseReason());
									//closureDate.setValue(legalOmbudsman.getLostClosureDate());
									txtInterest.setValue(legalOmbudsman.getLostInterest());
									awardReceiptDate.setValue(legalOmbudsman.getLostAwdRecDate());
									lastDtForSatisfactionAward.setReadOnly(Boolean.FALSE);
									lastDtForSatisfactionAward.setValue(legalOmbudsman.getLostLastDateStafisfyAwd());
									lastDtForSatisfactionAward.setReadOnly(Boolean.TRUE);
									if(legalOmbudsman.getExgratiaAward()!=null && legalOmbudsman.getExgratiaAward()){
										chkExgratiaAward.setValue(legalOmbudsman.getExgratiaAward());
										exgratiaAwardDate.setValue(legalOmbudsman.getExgratiaAwardDt());
										if(legalOmbudsman.getExgratiaAwardAmt()!=null){
											txtExgratiaAwardAmount.setValue(legalOmbudsman.getExgratiaAwardAmt().toString());
										}
										txtReasonsForExgratia.setValue(legalOmbudsman.getExgratiaReason());
									}
									
								}
							}
						}else if("AWARD RESERVED".equalsIgnoreCase(hearingStatusId.getValue())){
							awardReservedDetails.setValue(legalOmbudsman.getAwardReservedDetail());
						}
					}
					
					if(legalOmbudsman.getSelfContainedFlag()!=null  && legalOmbudsman.getSelfContainedFlag()){
						chkSelfContainedNote.setValue(legalOmbudsman.getSelfContainedFlag());
						selfContainedNotePreparationDate.setValue(legalOmbudsman.getSelfContPreparationDt());
						selfContainedNoteSubmissionDate.setValue(legalOmbudsman.getSelfContSubmissionDt());
					}
					
					
					
					
				}
			}
			if(legalOmbudsman.getAddDaysId()!=null){
				MastersValue addDaysId = legalOmbudsman.getAddDaysId();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(addDaysId.getKey());
				selectValue.setValue(addDaysId.getValue());
				cmbaddDays.setValue(selectValue);
			}
			if(legalOmbudsman.getStatusCaseId()!=null){
				MastersValue statusCase = legalOmbudsman.getStatusCaseId();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(statusCase.getKey());
				selectValue.setValue(statusCase.getValue());
				cmbStatusOfCase.setValue(selectValue);
			}
			
			if(legalOmbudsman.getPendingStatus()!=null){
				MastersValue statusPending = legalOmbudsman.getPendingStatus();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(statusPending.getKey());
				selectValue.setValue(statusPending.getValue());
				cmbPendingStatus.setValue(selectValue);
			}
			if(legalOmbudsman.getClaimReconsideration()!=null){
				if("Y".equalsIgnoreCase(legalOmbudsman.getClaimReconsideration())){
					claimReconsideration.setValue(Boolean.TRUE);
					reconsiderationReceiptDate.setValue(legalOmbudsman.getReconsiderationReceipt());
				
			if(legalOmbudsman.getReconsideration()!=null){
				if("A".equalsIgnoreCase(legalOmbudsman.getReconsideration())){
					reconsideration.setValue(Boolean.TRUE);
					reconsiderationAcceptDate.setValue(legalOmbudsman.getReconAcceptDate());
					if(legalOmbudsman.getReconAcceptanceAmount()!=null){
						acceptanceAmount.setValue(legalOmbudsman.getReconAcceptanceAmount().toString());
					}
				}else{
					reconsideration.setValue(Boolean.FALSE);
					reconsiderationRejectDate.setValue(legalOmbudsman.getReconRejectDate());
				}
				
			}}}
			if(legalOmbudsman.getCancellationPolicy()!=null){
				if("Y".equalsIgnoreCase(legalOmbudsman.getCancellationPolicy())){
					cancellationOfPolicy.setValue(Boolean.TRUE);
					prorataPremiumRefundDate.setValue(legalOmbudsman.getProratePremiumRejDate());
					if(legalOmbudsman.getRefundProRataPermium()!=null){
						refundofProrataPremium.setValue(legalOmbudsman.getRefundProRataPermium().toString());
					}
				}
				
				
			}
			if(legalOmbudsman.getGrievanceReqIntiated()!=null){
				if("Y".equalsIgnoreCase(legalOmbudsman.getGrievanceReqIntiated())){
					grievanceRequestInitiated.setValue(Boolean.TRUE);
					grievanceInitiatedDate.setValue(legalOmbudsman.getGrievanceInitiatedDate());
					if(legalOmbudsman.getGrievanceOutcome()!=null){
						MastersValue grievanceOutcomeValue = legalOmbudsman.getGrievanceOutcome();
						SelectValue selectValue = new SelectValue();
						selectValue.setId(grievanceOutcomeValue.getKey());
						selectValue.setValue(grievanceOutcomeValue.getValue());
						grievanceOutcome.setValue(selectValue);
					}
					grievanceRemarks.setValue(legalOmbudsman.getGrievanceRemarks());
				}
			}
			
			if(legalOmbudsman.getReconCancelPolicy()!=null){
				if("Y".equalsIgnoreCase(legalOmbudsman.getReconCancelPolicy())){
					reconsiderationCancellationOfPolicy.setValue(Boolean.TRUE);
					reconsiderationCancelPolicyAcceptDate.setValue(legalOmbudsman.getReconCancelPolicyDate());
					if(legalOmbudsman.getAmtRefundByInsured()!=null){
						amountrefundbytheInsured.setValue(legalOmbudsman.getAmtRefundByInsured().toString());
					}
				}
			}
			
		}
		
			
	}
	
	public void addCLickListener() {
		btnIntimationSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();

				popup.setWidth("75%");
				popup.setHeight("90%");
				consumerFormSearchUI.setPopup(popup, SHAConstants.LEGAL_OMBUDSMAN);
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

	public void setOmbudsmanContact(String contactOmbudsman) {
		
		txtOmbudsmanContactAddress.setValue(contactOmbudsman);
		txtOmbudsmanContactAddress.setVisible(Boolean.TRUE);
		
	}
	
	private void unbindField(Field<?> field , BeanFieldGroup<?> binder) {
		if (field != null ) {
			Object propertyId = binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setReadOnly(false);
				field.setValue(null);
				binder.unbind(field);
			}
		}
	}
	
	private void doNumberValidation(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	private void addAddDateValueChangeListener(DateField dateComplaintRecieptDate, final ComboBox cmbaddDays2, final DateField lastDtForSatisfactionAward)
	{
		dateComplaintRecieptDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				doAddDaysCalculation = new Date();
				if(null != enteredDate && cmbaddDays2!=null && cmbaddDays2.getValue()!= null){
					SelectValue addDays = (SelectValue) cmbaddDays2.getValue();
					doAddDaysCalculation =doAddDaysCalculation(enteredDate,addDays,lastDtForSatisfactionAward);
				}
					
			}

			
		});
	}
		private Date addAddDaysValueChangeListener(final DateField dateComplaintRecieptDate, ComboBox cmbaddDays2,final DateField lastDtForSatisfactionAward)
		{
			
			
			cmbaddDays2.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = -8435623803385270083L;
				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue addDays = (SelectValue) event.getProperty().getValue();
					doAddDaysCalculation = new Date();
					if(null != addDays && dateComplaintRecieptDate!=null && dateComplaintRecieptDate.getValue()!=null){
						Date enteredDate = dateComplaintRecieptDate.getValue();
						doAddDaysCalculation = doAddDaysCalculation(enteredDate,addDays,lastDtForSatisfactionAward);
						 
					}
						
				}
			});
			return doAddDaysCalculation;
	}
		
		private Date doAddDaysCalculation(Date enteredDate, SelectValue addDays,DateField dateStipulatedDate) {
			String value = addDays.getValue();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(enteredDate); // Now use today date.
			c.add(Calendar.DATE, Integer.parseInt(value)); // Adding 5 days
			String output = sdf.format(c.getTime());
			dateStipulatedDate.setReadOnly(false);
			dateStipulatedDate.setValue(c.getTime());
			dateStipulatedDate.setReadOnly(true);
			System.out.println(output);
			return c.getTime();
		}
}

