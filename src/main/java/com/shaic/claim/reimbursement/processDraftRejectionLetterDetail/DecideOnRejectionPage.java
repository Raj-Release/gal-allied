package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ProcessRejectionDetailsTable;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Lakshminarayana
 *
 */

@UIScoped
public class DecideOnRejectionPage extends ViewComponent {

	// @Inject
	// private RejectionDetailsTable rejectionDetailsTable;

	@Inject
	private ProcessRejectionDetailsTable processRejectionDetailsTable;
	
	private ReimbursementRejectionDto bean;

	private BeanFieldGroup<ReimbursementRejectionDto> binder;

	private boolean clickAction = false;

	private TextArea rejectionRemarks;

	private String autoRejectionRemarks;

	private TextArea rejectionLetterRemarks;
	
	private String autoRejectionLetterRemarks;

	private Button reDraftRejectionBtn;

	private Button disapproveRejectionBtn;

	private Button approveRejectionBtn;

	private TextArea rejectionLetterRemarksTxta;

	// private RichTextArea rejectionLetterRemarksTxta;

	private TextArea rejectionRemarksTxta;

	private TextArea disapproveRejectionRemarksTxta;

	private TextArea reDraftRejectionRemarksTxta;

	private HorizontalLayout buttonsLayout;

	private VerticalLayout dynamicLayout;

	private GWizard wizard;

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private VerticalLayout decideOnRejectionPageLayout;

	private List<ReimbursementRejectionDetailsDto> rejectionDetailsList;

	private List<Component> mandatoryFields = new ArrayList<Component>();
	
	private BeanItemContainer<SelectValue> rejectCategContainer;
	
	private BeanItemContainer<SelectValue> rejSubCategContainer;

	private BeanItemContainer<SelectValue> reconsiderCaseMaster;
	
	private BeanItemContainer<SelectValue> reconsiderReasonMaster;
	
	private SelectValue reconsiderCase;
	
	private SelectValue reconsiderReason;
	
	private ComboBox cmbRejCategory;
	
	private ComboBox cmbRejSubCategory;
	
	private ComboBox cmbReconsideration;
	
	private ComboBox cmbReconsidReason;
	
	private PopupDateField admissionDate;
	
	private PopupDateField dischargeDate;
	
	private ComboBox cmbPatientStatus;
	private DateField deathDate;
	private TextField txtReasonForDeath;
		
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;	
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;

	private PreauthDTO preauthDto;
	
	@PostConstruct
	public void init() {

	}

	public void init(ClaimRejectionDto bean, GWizard wizard) {
		this.bean = bean.getReimbursementRejectionDto();
		
		this.preauthDto = bean.getPreAuthDto(); 
		
		rejectionDetailsList = bean.getRejectionDetailsList();
		rejectCategContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		rejSubCategContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(bean.getRejectCategList() != null && !bean.getRejectCategList().isEmpty()){
			
			rejectCategContainer.addAll(bean.getRejectCategList());
		}
		this.wizard = wizard;
		this.wizard.getNextButton().setEnabled(false);

		autoRejectionRemarks = this.bean.getRejectionRemarks();
		autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks();
		
		reconsiderCase = bean.getReconsiderCase();
		reconsiderReason = bean.getReconsiderReason();
		
		clickAction = false;
		
		if(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<ReimbursementRejectionDto>(
				ReimbursementRejectionDto.class);
		this.binder.setItemDataSource(this.bean);		
		
		reconsiderCaseMaster = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderCaseMaster.addBean(reconsiderCase);
		
		reconsiderReasonMaster = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderReasonMaster.addBean(reconsiderReason);
			
	}

	public Component getContent() {

		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
			
		
		return buildDecideOnRejectionPageLayout();
	}

	private VerticalLayout buildDecideOnRejectionPageLayout() {
		decideOnRejectionPageLayout = new VerticalLayout();

		// rejectionDetailsTable.init("Rejection Details", false, false);
		// if(rejectionDetailsList != null && !rejectionDetailsList.isEmpty())
		// {
		// rejectionDetailsTable.setTableList(rejectionDetailsList);
		// rejectionDetailsTable.tableSelectHandler(rejectionDetailsList.get(0));
		// }

		processRejectionDetailsTable.init("Rejection Details", false, false);
		if (rejectionDetailsList != null && !rejectionDetailsList.isEmpty()) {
			processRejectionDetailsTable.setTableList(rejectionDetailsList);
			processRejectionDetailsTable
					.tableSelectHandler(rejectionDetailsList.get(0));
		}

		// decideOnRejectionPageLayout.addComponent(rejectionDetailsTable);
		decideOnRejectionPageLayout.addComponent(processRejectionDetailsTable);
		decideOnRejectionPageLayout.setSpacing(true);
		
		cmbRejCategory = binder.buildAndBind("Rejection Category", "rejCategSelectValue", ComboBox.class);
		cmbRejCategory.setWidth("300px");
		cmbRejCategory.setContainerDataSource(rejectCategContainer);
		cmbRejCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRejCategory.setItemCaptionPropertyId("value");
		
		cmbRejSubCategory = binder.buildAndBind("Rejection sub category", "rejSubCategSelectValue", ComboBox.class);
		cmbRejSubCategory.setWidth("300px");
		
		if(bean.getRejCategSelectValue() != null){
			
			fireViewEvent(DecideOnRejectionPresenter.REJECT_SUB_CATEG_LAYOUT_PROCESS_DRAFT_REJ, bean.getRejCategSelectValue().getId());
			
			if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(bean.getRejCategSelectValue().getId())) {
					
				cmbRejSubCategory.setVisible(true);
				mandatoryFields.add(cmbRejSubCategory);
				showOrHideValidation(false);
			}
			else {
				cmbRejSubCategory.setVisible(false);
				mandatoryFields.remove(cmbRejSubCategory);
			}
			for(int i = 0; i<rejectCategContainer.size();i++){
				if(rejectCategContainer.getIdByIndex(i).getValue().equalsIgnoreCase(bean.getRejCategSelectValue().getValue()))
				{	
					cmbRejCategory.setValue(rejectCategContainer.getIdByIndex(i));
					break;
				}
			}
		}
		else {
			cmbRejSubCategory.setVisible(false);
			mandatoryFields.remove(cmbRejSubCategory);
		}

		if(bean.getRejCategSelectValue() != null
				&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(bean.getRejCategSelectValue().getId())) {
			
			for(int i = 0; i < rejSubCategContainer.size();i++){
				if(rejSubCategContainer.getIdByIndex(i).getId().equals(bean.getRejSubCategSelectValue().getId()))
				{	
					cmbRejSubCategory.setValue(rejSubCategContainer.getIdByIndex(i));
					break;
				}
			}

		}
			
//		if(bean.getRejCategSelectValue() != null){
//			cmbRejCategory.setValue(bean.getRejCategSelectValue());	
//		}		
		
		decideOnRejectionPageLayout.addComponents(cmbRejCategory, cmbRejSubCategory);
		
		FormLayout admissionLayout = new FormLayout();
		admissionDate = new  PopupDateField("Date of Admission");
		admissionDate.setValue(this.bean.getReimbursementDto().getClaimDto().getAdmissionDate());
		admissionDate.setDateFormat("dd/MM/yyyy");
		admissionLayout.addComponent(admissionDate);
		
		FormLayout dischargeLayout = new FormLayout();
		dischargeDate = new  PopupDateField("Date of Discharge");
		dischargeDate.setValue(this.bean.getReimbursementDto().getClaimDto().getDischargeDate());
		dischargeDate.setDateFormat("dd/MM/yyyy");
		dischargeLayout.addComponent(dischargeDate);
		HorizontalLayout dateLayout = new HorizontalLayout();
		
		dateLayout.addComponent(admissionLayout);
		dateLayout.addComponent(dischargeLayout);
		
		FormLayout diseasedLayout = new FormLayout();
		if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
				||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			cmbPatientStatus = new ComboBox("Patient Status");
			cmbPatientStatus.setEnabled(false);
			
			BeanItemContainer<SelectValue> patientStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			patientStatusContainer.addBean(new SelectValue(bean.getReimbursementDto().getPatientStatusId(),"Deceased"));
			cmbPatientStatus.setContainerDataSource(patientStatusContainer);
			cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPatientStatus.setItemCaptionPropertyId("value");
			cmbPatientStatus.setValue(patientStatusContainer.getItemIds().get(0));
			
			diseasedLayout.addComponent(cmbPatientStatus);
			
			deathDate = new PopupDateField("Date Of Death");
			deathDate.setDateFormat("dd/MM/yyyy");
			deathDate.setValue(bean.getReimbursementDto().getDateOfDeath());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementDto().getDeathReason() != null ? bean.getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
		}
		dateLayout.addComponent(diseasedLayout);
		
		dateLayout.setMargin(false);
		dateLayout.setWidth("100%");
//		dateLayout.setHeight("40px");
		
		
		decideOnRejectionPageLayout.addComponent(dateLayout);
		
		cmbReconsideration = new ComboBox("Reconsideration Case");
		
		cmbReconsideration.setContainerDataSource(reconsiderCaseMaster);
		cmbReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsideration.setItemCaptionPropertyId("value");
		cmbReconsideration.setValue(reconsiderCaseMaster.getItemIds().toArray()[0]);
		cmbReconsideration.setEnabled(false);
		
		cmbReconsidReason = new ComboBox("Reconsider Reason");
		cmbReconsidReason.setWidth("295px");
		
		cmbReconsidReason.setContainerDataSource(reconsiderReasonMaster);
		cmbReconsidReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsidReason.setItemCaptionPropertyId("value");
		cmbReconsidReason.setValue(reconsiderReasonMaster.getItemIds().toArray()[0]);
		cmbReconsidReason.setEnabled(false);		
	
		admissionLayout.addComponents(cmbReconsideration);
		dischargeLayout.addComponent(cmbReconsidReason);
		
//		HorizontalLayout reconsiderLayout = new HorizontalLayout(new FormLayout(cmbReconsideration), new FormLayout(cmbReconsidReason));
//		reconsiderLayout.setWidth("100%");
//		decideOnRejectionPageLayout.addComponent(reconsiderLayout);
		
		rejectionRemarks = new TextArea("Rejection Remarks");
		rejectionRemarks.setWidth("500px");
		rejectionRemarks.setValue(this.bean != null
				&& this.bean.getRejectionRemarks() != null ? this.bean
				.getRejectionRemarks() : "");
		//rejectionRemarks.setEnabled(false);
		rejectionRemarks.setReadOnly(true);
		rejectionRemarks.setId("autoRejRem");
		rejectionRemarks.setData(bean);
		rejectionRemarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleRedraftRemarksPopup(rejectionRemarks,null);
		
		rejectionLetterRemarks = new TextArea("Rejection Letter Remarks");
		rejectionLetterRemarks.setWidth("500px");
		rejectionLetterRemarks.setHeight("100px");
		rejectionLetterRemarks.setValue(this.bean != null
				&& this.bean.getRejectionLetterRemarks() != null ? this.bean
				.getRejectionLetterRemarks() : "");
		//rejectionLetterRemarks.setEnabled(false);
		rejectionLetterRemarks.setReadOnly(true);
		rejectionLetterRemarks.setId("autoRejLetRem");
		rejectionLetterRemarks.setData(bean);
		rejectionLetterRemarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleRedraftRemarksPopup(rejectionLetterRemarks,null);
		FormLayout rejectionFrmLayout = new FormLayout(rejectionRemarks,
				rejectionLetterRemarks);

		decideOnRejectionPageLayout.addComponent(rejectionFrmLayout);

		reDraftRejectionRemarksTxta = binder.buildAndBind(
				"Redraft Rejection Remarks", "redraftRemarks", TextArea.class);
		reDraftRejectionRemarksTxta.setWidth("500px");
		reDraftRejectionRemarksTxta.setHeight("200px");

		// reDraftRejectionRemarksTxta.setRequired(true);
		// reDraftRejectionRemarksTxta.setRequiredError("Please Proved value for Redraft Rejection Remarks");
		reDraftRejectionRemarksTxta.setMaxLength(4000);
		reDraftRejectionRemarksTxta.setId("redraftRem");
		reDraftRejectionRemarksTxta.setData(bean);
		reDraftRejectionRemarksTxta.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleRedraftRemarksPopup(reDraftRejectionRemarksTxta,null);

		rejectionRemarksTxta = binder.buildAndBind("Rejection Remarks",
				"rejectionRemarks", TextArea.class);
		rejectionRemarksTxta.setWidth("500px");
		rejectionRemarksTxta.setHeight("200px");
		rejectionRemarksTxta.setMaxLength(4000);
		rejectionRemarksTxta.setId("rejRem");
		rejectionRemarksTxta.setData(bean);
		rejectionRemarksTxta.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleRedraftRemarksPopup(rejectionRemarksTxta,null);
		// rejectionRemarksTxta.setRequired(true);
		// rejectionRemarksTxta.setRequiredError("Please Proved value for Rejection Remarks");

		disapproveRejectionRemarksTxta = binder.buildAndBind(
				"Disapprove Rejection Remarks", "disapprovedRemarks",
				TextArea.class);
		disapproveRejectionRemarksTxta.setWidth("500px");
		disapproveRejectionRemarksTxta.setHeight("200px");
		// disapproveRejectionRemarksTxta.setRequired(true);
		// disapproveRejectionRemarksTxta.setRequiredError("Please Proved value for Disapprove Rejection Remarks");
		disapproveRejectionRemarksTxta.setMaxLength(4000);
		disapproveRejectionRemarksTxta.setId("disapproveRem");
		disapproveRejectionRemarksTxta.setData(bean);
		disapproveRejectionRemarksTxta.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleRedraftRemarksPopup(disapproveRejectionRemarksTxta,null);

		rejectionLetterRemarksTxta = binder.buildAndBind(
				"Rejection Letter Remarks", "rejectionLetterRemarks",
				TextArea.class);
		rejectionLetterRemarksTxta.setWidth("500px");
		rejectionLetterRemarksTxta.setHeight("200px");
		rejectionLetterRemarksTxta.setMaxLength(4000);
		rejectionLetterRemarksTxta.setRequired(true);
		rejectionLetterRemarksTxta.setId("rejLetRem");
		rejectionLetterRemarksTxta.setData(bean);
		rejectionLetterRemarksTxta.setDescription("Click the Text Box and Press F8 for Detail Popup");
		rejectionLetterRemarksTxta
				.setRequiredError("Please Proved value for Rejection Letter Remarks");
		handleRedraftRemarksPopup(rejectionLetterRemarksTxta,null);

		mandatoryFields.add(cmbRejCategory);
		mandatoryFields.add(reDraftRejectionRemarksTxta);
		mandatoryFields.add(disapproveRejectionRemarksTxta);
		mandatoryFields.add(rejectionLetterRemarksTxta);
		mandatoryFields.add(rejectionRemarksTxta);
		showOrHideValidation(false);

		reDraftRejectionBtn = new Button("Redraft Rejection Letter Remarks");
		disapproveRejectionBtn = new Button("Disapprove Rejection");
		approveRejectionBtn = new Button("Approve Rejection");

		addListener();
		addDateListener();

		buttonsLayout = new HorizontalLayout(reDraftRejectionBtn,
				disapproveRejectionBtn, approveRejectionBtn);

		buttonsLayout.setSpacing(true);

		decideOnRejectionPageLayout.addComponent(buttonsLayout);
		decideOnRejectionPageLayout.setComponentAlignment(buttonsLayout,
				Alignment.MIDDLE_RIGHT);

		dynamicLayout = new VerticalLayout();

		decideOnRejectionPageLayout.addComponent(dynamicLayout);

		decideOnRejectionPageLayout.setSpacing(true);

		return decideOnRejectionPageLayout;

	}

//public  void handleAutoRejRemarksPopup(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutForRejectionRemarks", ShortcutAction.KeyCode.F6, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForAutoRejectionRem(searchField, getShortCutListenerForAutoRejectionRemarks(searchField));
//	    
//	  }	
//	public  void handleShortcutForAutoRejectionRem(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//		textField.addBlurListener(new BlurListener() {
//
//			@Override
//			public void blur(BlurEvent event) {
//
//				textField.removeShortcutListener(shortcutListener);
//
//			}
//		});
//	}	
//	private ShortcutListener getShortCutListenerForAutoRejectionRemarks(final TextArea txtFld){
//		ShortcutListener listener =  new ShortcutListener("Rejection Remarks",KeyCodes.KEY_F6,null) {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				ReimbursementRejectionDto  searchRejDto = (ReimbursementRejectionDto) txtFld.getData();
//				VerticalLayout vLayout =  new VerticalLayout();
//				
//				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setMargin(true);
//				vLayout.setSpacing(true);
//				TextArea txtArea = new TextArea();
//				txtArea.setNullRepresentation("");
//				txtArea.setValue(searchRejDto.getRejectionRemarks());
//				txtArea.setSizeFull();
//				txtArea.setWidth("100%");
//				txtArea.setRows(searchRejDto.getRejectionRemarks() != null ? (searchRejDto.getRejectionRemarks().length()/80 >= 25 ? 25 : ((searchRejDto.getRejectionRemarks().length()/80)%25)+1) : 25);
//				txtArea.setReadOnly(true);
//				
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//				
//				final Window dialog = new Window();
//				dialog.setCaption("Rejection Remarks");
//				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//				dialog.setWidth("45%");
//				dialog.setClosable(true);
//				
//				dialog.setContent(vLayout);
//				dialog.setResizable(false);
//				dialog.setModal(true);
//				dialog.setDraggable(true);
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				getUI().getCurrent().addWindow(dialog);
//				
//				okBtn.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						dialog.close();
//					}
//				});	
//			}
//		};
//		
//		return listener;
//	}	
//public  void handleAutoRejLetterRemarksPopup(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutForAutoRejectionLetterRemarks", ShortcutAction.KeyCode.F7, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForAutoRejectionLetterRem(searchField, getShortCutListenerForAutoRejectionLetterRemarks(searchField));
//	    
//	  }
//	
//	public  void handleShortcutForAutoRejectionLetterRem(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//		textField.addBlurListener(new BlurListener() {
//
//			@Override
//			public void blur(BlurEvent event) {
//
//				textField.removeShortcutListener(shortcutListener);
//
//			}
//		});
//	}	
//	private ShortcutListener getShortCutListenerForAutoRejectionLetterRemarks(final TextArea txtFld)
//	{
//		ShortcutListener listener =  new ShortcutListener("RejectionLetter Remarks",KeyCodes.KEY_F7,null) {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				ReimbursementRejectionDto  searchRejDto = (ReimbursementRejectionDto) txtFld.getData();
//				VerticalLayout vLayout =  new VerticalLayout();
//				
//				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setMargin(true);
//				vLayout.setSpacing(true);
//				TextArea txtArea = new TextArea();
//				txtArea.setNullRepresentation("");
//				txtArea.setValue(searchRejDto.getRejectionLetterRemarks());
//				txtArea.setSizeFull();
//				txtArea.setWidth("100%");
//				txtArea.setRows(searchRejDto.getRejectionLetterRemarks() != null ? (searchRejDto.getRejectionLetterRemarks().length()/80 >= 25 ? 25 : ((searchRejDto.getRejectionLetterRemarks().length()/80)%25)+1) : 25);
//				txtArea.setReadOnly(true);
//				
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//				
//				final Window dialog = new Window();
//				dialog.setCaption("RejectionLetter Remarks");
//				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//				dialog.setWidth("45%");
//				dialog.setClosable(true);
//				
//				dialog.setContent(vLayout);
//				dialog.setResizable(false);
//				dialog.setModal(true);
//				dialog.setDraggable(true);
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				getUI().getCurrent().addWindow(dialog);
//				
//				okBtn.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						dialog.close();
//					}
//				});	
//			}
//		};
//		
//		return listener;
//	}

public  void handleRedraftRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
	    
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
	private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Redraft Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				ReimbursementRejectionDto  searchTableDto = (ReimbursementRejectionDto) txtFld.getData();
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

				
				if(("autoRejRem").equalsIgnoreCase((txtFld.getId())) || ("autoRejLetRem").equalsIgnoreCase((txtFld.getId()))){
					if(("autoRejRem").equalsIgnoreCase((txtFld.getId()))){
						txtArea.setRows(searchTableDto.getRejectionRemarks() != null ? (searchTableDto.getRejectionRemarks().length()/80 >= 25 ? 25 : ((searchTableDto.getRejectionRemarks().length()/80)%25)+1) : 25);	
					}
					if(("autoRejLetRem").equalsIgnoreCase((txtFld.getId()))){
						txtArea.setRows(searchTableDto.getRejectionLetterRemarks() != null ? (searchTableDto.getRejectionLetterRemarks().length()/80 >= 25 ? 25 : ((searchTableDto.getRejectionLetterRemarks().length()/80)%25)+1) : 25);
					}
					txtArea.setReadOnly(true);
				}
				else{
					txtArea.setReadOnly(false);
					txtArea.setRows(25);
				}
				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if(!("autoRejRem").equalsIgnoreCase((txtFld.getId())) && !("autoRejLetRem").equalsIgnoreCase((txtFld.getId()))){
							
							txtFld.setValue(((TextArea)event.getProperty()).getValue());		
							
						}	
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				String strCaption = "";
				
				if(("autoRejRem").equalsIgnoreCase((txtFld.getId())) || ("rejRem").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Rejection Remarks";
				}
				if(("autoRejLetRem").equalsIgnoreCase((txtFld.getId())) || ("rejLetRem").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Rejection Letter Remarks";
				}
				if(("redraftRem").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Redraft Remarks";
				}
				if(("disapproveRem").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Disapprove Remarks";
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
//public  void handleDisapproveRemarksPopup(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutDisapproveRemarks", ShortcutAction.KeyCode.F9, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForDisapprove(searchField, getShortCutListenerForDisapproveRemarks(searchField));
//	    
//	  }
//	
//	public  void handleShortcutForDisapprove(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//		textField.addBlurListener(new BlurListener() {
//
//			@Override
//			public void blur(BlurEvent event) {
//
//				textField.removeShortcutListener(shortcutListener);
//
//			}
//		});
//	}	
//	private ShortcutListener getShortCutListenerForDisapproveRemarks(final TextArea txtFld){
//		ShortcutListener listener =  new ShortcutListener("Disapprove Remarks",KeyCodes.KEY_F9,null) {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				ReimbursementRejectionDto  searchTableDto = (ReimbursementRejectionDto) txtFld.getData();
//				VerticalLayout vLayout =  new VerticalLayout();
//				
//				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setMargin(true);
//				vLayout.setSpacing(true);
//				final TextArea txtArea = new TextArea();
//				txtArea.setData(bean);
//				txtArea.setValue(txtFld.getValue());
//				txtArea.setNullRepresentation("");
//				txtArea.setSizeFull();
//				txtArea.setWidth("100%");
//				txtArea.setMaxLength(4000);
//				txtArea.setRows(25);
//				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//							
//						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
//						ReimbursementRejectionDto mainDto = (ReimbursementRejectionDto)txtFld.getData();
//						mainDto.setDisapprovedRemarks(txtFld.getValue());
//					}
//				});
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//				
//				final Window dialog = new Window();
//				dialog.setCaption("Disapprove Remarks");
//				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//				dialog.setWidth("45%");
//				dialog.setClosable(true);
//				
//				dialog.setContent(vLayout);
//				dialog.setResizable(true);
//				dialog.setModal(true);
//				dialog.setDraggable(true);
//				dialog.setData(txtFld);
//				
//				dialog.addCloseListener(new Window.CloseListener() {
//					
//					@Override
//					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getDisapprovedRemarks());
//						dialog.close();
//					}
//				});
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				getUI().getCurrent().addWindow(dialog);
//				okBtn.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getDisapprovedRemarks());
//						dialog.close();
//					}
//				});	
//			}
//		};
//		
//		return listener;
//	}
//	
//public  void handleRejLetterRemarksPopup(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutForRejectionLetterRemarks", ShortcutAction.KeyCode.F10, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForRejectionLetter(searchField, getShortCutListenerForRejectionLetterRemarks(searchField));
//	    
//	  }
//	
//	public  void handleShortcutForRejectionLetter(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//		textField.addBlurListener(new BlurListener() {
//
//			@Override
//			public void blur(BlurEvent event) {
//
//				textField.removeShortcutListener(shortcutListener);
//
//			}
//		});
//	}	
//	private ShortcutListener getShortCutListenerForRejectionLetterRemarks(final TextArea txtFld){
//		ShortcutListener listener =  new ShortcutListener("RejectionLetter Remarks",KeyCodes.KEY_F10,null) {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				ReimbursementRejectionDto  searchTableDto = (ReimbursementRejectionDto) txtFld.getData();
//				VerticalLayout vLayout =  new VerticalLayout();
//				
//				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setMargin(true);
//				vLayout.setSpacing(true);
//				final TextArea txtArea = new TextArea();
//				txtArea.setData(searchTableDto);
//				txtArea.setValue(txtFld.getValue());
//				txtArea.setNullRepresentation("");
//				txtArea.setSizeFull();
//				txtArea.setWidth("100%");
//				txtArea.setRows(25);
//				txtArea.setMaxLength(4000);
//				txtArea.addValueChangeListener(new ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//							
//						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
//						ReimbursementRejectionDto mainDto = (ReimbursementRejectionDto)txtFld.getData();
//						mainDto.setRejectionLetterRemarks(((TextArea)event.getProperty()).getValue());
//					}
//				});
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//				
//				final Window dialog = new Window();
//				dialog.setCaption("RejectionLetter Remarks");
//				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//				dialog.setWidth("45%");
//				dialog.setClosable(true);
//				
//				dialog.setContent(vLayout);
//				dialog.setResizable(true);
//				dialog.setModal(true);
//				dialog.setDraggable(true);
//				dialog.setData(txtArea);
//				
//				dialog.addCloseListener(new Window.CloseListener() {
//					
//					@Override
//					public void windowClose(CloseEvent e) {
////						TextArea txtArea = (TextArea)dialog.getData();
////						ReimbursementRejectionDto rejDto =  (ReimbursementRejectionDto)txtArea.getData();
////						txtArea.setValue(rejDto.getRejectionLetterRemarks());
//						dialog.close();
//					}
//				});
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				getUI().getCurrent().addWindow(dialog);
//				okBtn.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
////						TextArea txtArea = (TextArea)dialog.getData();
////						ReimbursementRejectionDto rejDto =  (ReimbursementRejectionDto)txtArea.getData();
////						txtArea.setValue(rejDto.getRejectionLetterRemarks());
//						dialog.close();
//					}
//				});	
//			}
//		};
//		
//		return listener;
//	}
//		
//public  void handleRejRemarksPopup(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutForRejectionRemarks", ShortcutAction.KeyCode.F11, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForRejRemarks(searchField, getShortCutListenerForRejectionRemarks(searchField));
//	    
//	  }
//	
//	public  void handleShortcutForRejRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//		textField.addBlurListener(new BlurListener() {
//
//			@Override
//			public void blur(BlurEvent event) {
//
//				textField.removeShortcutListener(shortcutListener);
//
//			}
//		});
//	}	
//	private ShortcutListener getShortCutListenerForRejectionRemarks(final TextArea txtFld){
//		ShortcutListener listener =  new ShortcutListener("Rejection Remarks",KeyCodes.KEY_F11,null) {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				ReimbursementRejectionDto  searchTableDto = (ReimbursementRejectionDto) txtFld.getData();
//				VerticalLayout vLayout =  new VerticalLayout();
//				
//				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//				vLayout.setMargin(true);
//				vLayout.setSpacing(true);
//				final TextArea txtArea = new TextArea();
//				txtArea.setData(searchTableDto);
//				txtArea.setValue(txtFld.getValue());
//				txtArea.setNullRepresentation("");
//				txtArea.setSizeFull();
//				txtArea.setWidth("100%");
//				txtArea.setRows(25);
//				txtArea.setMaxLength(4000);
//				txtArea.addValueChangeListener(new ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//							
//						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
//						ReimbursementRejectionDto mainDto = (ReimbursementRejectionDto)txtFld.getData();
//						mainDto.setRejectionRemarks(((TextArea)event.getProperty()).getValue());
//					}
//				});
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//				
//				final Window dialog = new Window();
//				dialog.setCaption("Rejection Remarks");
//				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//				dialog.setWidth("45%");
//				dialog.setClosable(true);
//				
//				dialog.setContent(vLayout);
//				dialog.setResizable(true);
//				dialog.setModal(true);
//				dialog.setDraggable(true);
//				dialog.setData(txtArea);
//				
//				dialog.addCloseListener(new Window.CloseListener() {
//					
//					@Override
//					public void windowClose(CloseEvent e) {
////						TextArea txtArea = (TextArea)dialog.getData();
////						ReimbursementRejectionDto rejDto =  (ReimbursementRejectionDto)txtArea.getData();
////						txtArea.setValue(rejDto.getRejectionLetterRemarks());
//						dialog.close();
//					}
//				});
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				getUI().getCurrent().addWindow(dialog);
//				okBtn.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
////						TextArea txtArea = (TextArea)dialog.getData();
////						ReimbursementRejectionDto rejDto =  (ReimbursementRejectionDto)txtArea.getData();
////						txtArea.setValue(rejDto.getRejectionLetterRemarks());
//						dialog.close();
//					}
//				});	
//			}
//		};
//		
//		return listener;
//	}
	private void addListener() {

		cmbRejCategory.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;

				if(value != null){
				
					fireViewEvent(DecideOnRejectionPresenter.REJECT_SUB_CATEG_LAYOUT_PROCESS_DRAFT_REJ, value.getId());

					if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
					
						cmbRejSubCategory.setVisible(true);
						mandatoryFields.add(cmbRejSubCategory);
					}
					else {
						cmbRejSubCategory.setVisible(false);
						mandatoryFields.remove(cmbRejSubCategory);
					}

					showOrHideValidation(false);
					
				}	

			}
		});
				
		reDraftRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(
						DecideOnRejectionPresenter.BUILD_RE_DRAFT_REJECTION_LAYOUT,
						null);

			}

		});

		disapproveRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						DecideOnRejectionPresenter.BUILD_DISAPPROVE_REJECTION_LAYOUT,
						null);
			}
		});

		approveRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(
						DecideOnRejectionPresenter.BUILD_APPROVE_REJECTION_LAYOUT,
						null);
			}

		});

	}

	public void buildRedraftRejectionLayout() {

		clickAction = true;
		this.bean.setStatusValue(SHAConstants.REJECTION_REDRAFT_OUT_COME);
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			decideOnRejectionPageLayout
					.removeComponent(decideOnRejectionPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}

		dynamicLayout.addComponent(new FormLayout(reDraftRejectionRemarksTxta));
		decideOnRejectionPageLayout.addComponent(dynamicLayout);

		this.binder.bind(reDraftRejectionRemarksTxta, "redraftRemarks");

		if (!mandatoryFields.contains(reDraftRejectionRemarksTxta)) {
			mandatoryFields.add(reDraftRejectionRemarksTxta);
		}

		unbindField(rejectionRemarksTxta);
		// rejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(rejectionRemarksTxta);
		unbindField(rejectionLetterRemarksTxta);
		// rejectionLetterRemarksTxta.setValue(null);
		mandatoryFields.remove(rejectionLetterRemarksTxta);
		unbindField(disapproveRejectionRemarksTxta);
		// disapproveRejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(disapproveRejectionRemarksTxta);
		
//		this.bean.setRejectionRemarks(null);
//		this.bean.setRejectionLetterRemarks(null);
//		this.bean.setDisapprovedRemarks(null);		

		this.wizard.getNextButton().setEnabled(false);
		this.wizard.getFinishButton().setEnabled(true);

	}

	public void buildDisapproveRejectionLayout() {
		clickAction = true;
		this.bean.setStatusValue(SHAConstants.REJECTION_DISAPPROVE_OUT_COME);
		
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			decideOnRejectionPageLayout
					.removeComponent(decideOnRejectionPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}

		dynamicLayout.addComponent(new FormLayout(
				disapproveRejectionRemarksTxta));
		decideOnRejectionPageLayout.addComponent(dynamicLayout);

		this.binder.bind(disapproveRejectionRemarksTxta, "disapprovedRemarks");

		if (!mandatoryFields.contains(disapproveRejectionRemarksTxta)) {
			mandatoryFields.add(disapproveRejectionRemarksTxta);
		}

		unbindField(reDraftRejectionRemarksTxta);
		// reDraftRejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(reDraftRejectionRemarksTxta);
		unbindField(rejectionRemarksTxta);
		// rejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(rejectionRemarksTxta);
		unbindField(rejectionLetterRemarksTxta);
		// rejectionLetterRemarksTxta.setValue(null);
		mandatoryFields.remove(rejectionLetterRemarksTxta);
		
		
//		this.bean.setRejectionRemarks(null);
//		this.bean.setRejectionLetterRemarks(null);
//		this.bean.setRedraftRemarks(null);
		

		this.wizard.getNextButton().setEnabled(false);
		this.wizard.getFinishButton().setEnabled(true);

	}

	public void buildApproveRejectionLayout() {
		clickAction = true;
		this.bean.setStatusValue(SHAConstants.REJECTION_APPROVE_OUT_COME);
		
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			decideOnRejectionPageLayout
					.removeComponent(decideOnRejectionPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}

		rejectionRemarksTxta.setValue(autoRejectionRemarks);
		rejectionLetterRemarksTxta.setValue(autoRejectionLetterRemarks);
		this.binder.bind(rejectionLetterRemarksTxta, "rejectionLetterRemarks");
		this.binder.bind(rejectionRemarksTxta, "rejectionRemarks");
		FormLayout approveFormLayout = new FormLayout(rejectionRemarksTxta,
				rejectionLetterRemarksTxta);
		dynamicLayout.addComponent(approveFormLayout);
		
		if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
				||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
				&& bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

			nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			
			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null) {
				nomineeDetailsTable.setTableList(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
				nomineeDetailsTable.setViewColumnDetails();
				nomineeDetailsTable.generateSelectColumn();
			}	
			
			dynamicLayout.addComponent(nomineeDetailsTable);
		
			boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
					
			legalHeirLayout = new VerticalLayout();
			
			legalHeirDetails = legalHeirObj.get();
			
			relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			relationshipContainer.addAll(preauthDto.getLegalHeirDto().getRelationshipContainer());
			Map<String,Object> refData = new HashMap<String, Object>();
			refData.put("relationship", relationshipContainer);
			legalHeirDetails.setReferenceData(refData);
			
			preauthDto.setClaimDTO(bean.getReimbursementDto().getClaimDto());
			preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementDto().getPatientStatusId(),""));
			preauthDto.setNewIntimationDTO(bean.getReimbursementDto().getClaimDto().getNewIntimationDto());
			
			legalHeirDetails.init(preauthDto);
			legalHeirDetails.setViewColumnDetails();
			legalHeirLayout.addComponent(legalHeirDetails);
			dynamicLayout.addComponent(legalHeirLayout);

			if(enableLegalHeir) {
				
				legalHeirDetails.addBeanToList(bean.getReimbursementDto().getLegalHeirDTOList());
//				legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}	
		
		}
		
		
		decideOnRejectionPageLayout.addComponent(dynamicLayout);

		if (!mandatoryFields.contains(rejectionRemarksTxta)) {
			mandatoryFields.add(rejectionRemarksTxta);
		}

		if (!mandatoryFields.contains(rejectionLetterRemarksTxta)) {
			mandatoryFields.add(rejectionLetterRemarksTxta);
		}

		unbindField(reDraftRejectionRemarksTxta);
		// reDraftRejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(reDraftRejectionRemarksTxta);
		unbindField(disapproveRejectionRemarksTxta);
		// disapproveRejectionRemarksTxta.setValue(null);
		mandatoryFields.remove(disapproveRejectionRemarksTxta);
				
		
		
//		this.bean.setRedraftRemarks(null);
//		this.bean.setDisapprovedRemarks(null);		

		this.wizard.getNextButton().setEnabled(true);
		this.wizard.getFinishButton().setEnabled(false);
	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			if (binder.getPropertyId(field) != null) {
				this.binder.unbind(field);
			}

		}
	}

	public void showOrHideValidation(boolean isVisible) {
		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}
		}
	}

	public boolean validatePage() {
		try {

			if (clickAction) {

				if (("").equals(disapproveRejectionRemarksTxta.getValue())) {

					showErrorMessage("Please Enter a value for the Mandatory Field Disapprove Rejection Remarks");
					return !clickAction;
				}
				if (("").equals(reDraftRejectionRemarksTxta.getValue())) {
					showErrorMessage("Please Enter a value for the Mandatory Field Redraft Remarks");
					return !clickAction;
				}
				if (("").equals(rejectionLetterRemarksTxta.getValue())) {
					showErrorMessage("Please Enter a value for the Mandatory Field Rejection Letter Remarks");
					return !clickAction;
				}
				
				if(admissionDate.getValue() == null || dischargeDate.getValue() == null){
					showErrorMessage("Date of Admission / Date of Discharge can't be empty. Please fill the details.");
					return !clickAction;
				}

//				if(cmbRejCategory.getValue() == null || (cmbRejCategory.getValue() != null && ((SelectValue)cmbRejCategory.getValue()).getValue().isEmpty())){
//					showErrorMessage("Please Select a value for Rejection Category Field.");
//				}

				if(cmbRejCategory.getValue() != null
						&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(((SelectValue)cmbRejCategory.getValue()).getId())) {
					
					if(cmbRejSubCategory.getValue() == null){
						showErrorMessage("Please Select a value for Rejection Subcategory Field.");
						return !clickAction;
					}	
				}
				else {
					mandatoryFields.remove(cmbRejSubCategory);
					unbindField(cmbRejSubCategory);					
				}
				
				if(cmbRejCategory.getValue() != null
						&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(((SelectValue)cmbRejCategory.getValue()).getId())) {
					
					if(cmbRejSubCategory.getValue() == null){
						showErrorMessage("Please Select a value for Rejection Subcategory Field.");
						return !clickAction;
					}	
				}else {
					mandatoryFields.remove(cmbRejSubCategory);
					unbindField(cmbRejSubCategory);					
				}
				
				
				showOrHideValidation(true);
				
				this.bean.getReimbursementDto().getClaimDto().setAdmissionDate(admissionDate.getValue());
				this.bean.getReimbursementDto().getClaimDto().setDischargeDate(dischargeDate.getValue());

				if (!binder.isValid()) {
					StringBuffer eMsg = new StringBuffer();
					for (Field<?> field : this.binder.getFields()) {
						ErrorMessage errMsg = ((AbstractField<?>) field)
								.getErrorMessage();
						if (errMsg != null) {
							eMsg.append(errMsg.getFormattedHtmlMessage());
						}

					}
					setRequired(true);
					showErrorMessage(eMsg.toString());
					return !clickAction;
				} else {

					 if(rejectionLetterRemarksTxta != null &&
					 rejectionLetterRemarksTxta.getValue() != null &&
					 rejectionLetterRemarksTxta.getValue().length() > 4000){
					 showErrorMessage("Maximum of 4000 Charactes only allowed for remarks");
					 return !clickAction;
					 }

					this.binder.commit();
					showOrHideValidation(false);
					
					if(SHAConstants.REJECTION_APPROVE_OUT_COME.equalsIgnoreCase(this.bean.getStatusValue())
							&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
									|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
							&& bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
						if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
							List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
						
							if(tableList != null && !tableList.isEmpty()){
								bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
								StringBuffer nomineeNames = new StringBuffer("");
								int selectCnt = 0;
								for (NomineeDetailsDto nomineeDetailsDto : tableList) {
									nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
									if(nomineeDetailsDto.isSelectedNominee()) {
										nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
									    selectCnt++;
									}
								}
								bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeSelectCount(selectCnt);
								if(selectCnt>0){
									bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
									bean.getReimbursementDto().setNomineeName(null);
//									bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
//									bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
									bean.getReimbursementDto().setNomineeAddr(null);
								}
								else{
									bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
									
									/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
									bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
//									bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("MNAME").toString());
//									bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("LNAME").toString());
									bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());*/
									
									//TODO alert for selecting Nominee to be done
									showErrorMessage("Please Select Nominee");
									return !clickAction;									
								}							
							}
						}
						else{
							bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(null);
							bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
							
							if(legalHeirDetails.isValid()) {
								//added for support fix IMSSUPPOR-31323
								List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
								legalHeirList.addAll(this.legalHeirDetails.getValues());
								if(legalHeirList != null && !legalHeirList.isEmpty()) {
									
									List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
									
									for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
										legalHeirList.add(legalHeirDTO);
									}
									
									bean.getReimbursementDto().setLegalHeirDTOList(legalHeirList);
								}
							}
							else{
								bean.getReimbursementDto().setLegalHeirDTOList(null);
								showErrorMessage("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
								return !clickAction;
							}
							
							/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
							if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
									&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
							{
								bean.getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
								bean.getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
								
							}
							else{
								bean.getReimbursementDto().setNomineeName(null);
								bean.getReimbursementDto().setNomineeAddr(null);
							}
							
							
							if( (bean.getReimbursementDto().getNomineeName() == null && bean.getReimbursementDto().getNomineeAddr() == null))
							{
								showErrorMessage("Please Enter Claimant / Legal Heir Details");
								return !clickAction;
							}
							else{
								bean.getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
								bean.getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
							}*/	
						}					
					}
					
					
					this.bean.setUsername(UI.getCurrent().getSession()
							.getAttribute(BPMClientContext.USERID).toString());
					this.bean
							.setPassword(UI.getCurrent().getSession()
									.getAttribute(BPMClientContext.PASSWORD)
									.toString());

				}
			} else {
				showErrorMessage("Please Click ReDraft / Reject / Approve Button to process the Rejection");
			}

			// if(this.reDraftRejectionRemarksTxta != null &&
			// this.reDraftRejectionRemarksTxta.getValue() != null){
			// this.bean.setRejectionRemarks(null);
			// this.bean.setRejectionLetterRemarks(null);
			// this.bean.setDisapprovedRemarks(null);
			// }
			// else if(this.disapproveRejectionRemarksTxta != null &&
			// this.disapproveRejectionRemarksTxta.getValue() != null){
			// this.bean.setRedraftRemarks(null);
			// this.bean.setRejectionLetterRemarks(null);
			// this.bean.setRejectionRemarks(null);
			// }
			// else{
			// this.bean.setDisapprovedRemarks(null);
			// this.bean.setRedraftRemarks(null);
			// }

		} catch (CommitException e) {

			e.printStackTrace();
		}
		return clickAction;
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	private void showErrorMessage(String eMsg) {
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

	public void setpreviousView(ClaimRejectionDto bean) {
		this.bean = bean.getReimbursementRejectionDto();
		this.autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks();
		this.autoRejectionRemarks = this.bean.getRejectionRemarks();
		fireViewEvent(DecideOnRejectionPresenter.BUILD_APPROVE_REJECTION_LAYOUT,null);		
	}
	
	public void addDateListener(){
		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();

				if(enteredDate != null) {
					Date policyFromDate = bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate();
					Date policyToDate = bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate();
					if((bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
							|| bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
							|| bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
							|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode()))){

						policyFromDate = (bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() != null ? bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() : bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
						policyToDate = (bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() != null ? bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() : bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());
						
						if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode() != null && bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember() != null){
							policyFromDate = (bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() != null ? bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() : bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
							policyToDate = (bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() != null ? bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() : bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());		
						}
					}
					Long claimKey = bean.getReimbursementDto().getClaimDto().getKey();
					//Long rodKey = bean.getReimbursementDto().getKey();
					boolean isRODDateCheck = reimbursementQueryService.rodAdmissionDateCompare(claimKey, admissionDate.getValue());

					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);

					if(isRODDateCheck){
						Button okButton = new Button("OK");
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date should not be before the First ROD Date.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						event.getProperty().setValue(null);
					}else if (!isRODDateCheck && !SHAUtils.isDateOfAdmissionWithPolicyRange(policyFromDate,policyToDate,enteredDate)
							/*!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0)*/) {
						Button okButton = new Button("OK");
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						event.getProperty().setValue(null);
					}
				}

			}
		});		
		
		dischargeDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null) {
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(true);
					dialog.setResizable(false);
					DateField property = (DateField) event.getProperty();
					if(property.getValue() != null && admissionDate.getValue() != null && !SHAUtils.validateDischargeDate(property.getValue(), admissionDate.getValue())) {
						Button okButton = new Button("OK");
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid Discharge Date. Date of Discharge should not be before Date of Admisssion.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						event.getProperty().setValue(null);					
					}
				}

			}
		});
	}
	
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {

			this.rejSubCategContainer = rejSubcategContainer;
			cmbRejSubCategory.setVisible(true);
			cmbRejSubCategory.setContainerDataSource(rejSubCategContainer);
			cmbRejSubCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbRejSubCategory.setItemCaptionPropertyId("value");
			
			mandatoryFields.add(cmbRejSubCategory);
			
			if(bean.getRejSubCategSelectValue() != null){
				cmbRejSubCategory.setValue(bean.getRejSubCategSelectValue());
			}

			showOrHideValidation(false);
	}
	
	public void clearObject(){
		if(decideOnRejectionPageLayout != null){
			decideOnRejectionPageLayout.removeAllComponents();
		}
		rejectionDetailsList = null;
		
		rejectCategContainer = null;
		rejSubCategContainer = null;
		reconsiderCaseMaster = null;
		reconsiderReasonMaster = null;
		reconsiderCase = null;
		reconsiderReason = null;
		reimbursementQueryService = null;
		relationshipContainer = null;
		bean = null;
		SHAUtils.setClearPreauthDTO(preauthDto);
	}
}