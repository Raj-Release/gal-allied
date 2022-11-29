package com.shaic.paclaim.reimbursement.processdraftrejection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ProcessRejectionDetailsTable;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.ClaimRejectionDto;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 * @author Lakshminarayana
 *
 */

@UIScoped
public class DecideOnPARejectionPage extends ViewComponent {

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
	
	private ComboBox cmbPatientStatus;
	private DateField deathDate;
	private TextField txtReasonForDeath;
		
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;

	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;

	private PreauthDTO preauthDto;
	
	private CheckBox chkNomineeDeceased;
	
	@PostConstruct
	public void init() {

	}

	public void init(ClaimRejectionDto bean, GWizard wizard) {
		this.bean = bean.getReimbursementRejectionDto();
		rejectionDetailsList = bean.getRejectionDetailsList();
		this.preauthDto = bean.getPreAuthDto();
		this.wizard = wizard;
		this.wizard.getNextButton().setEnabled(false);
		
		if(this.bean.getRejectionRemarks2() !=null){
			autoRejectionRemarks = this.bean.getRejectionRemarks()+this.bean.getRejectionRemarks2();
		}else{
			autoRejectionRemarks = this.bean.getRejectionRemarks();
		}

		//autoRejectionRemarks = this.bean.getRejectionRemarks();
		if(this.bean.getRejectionLetterRemarks2() !=null){
			autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks()+this.bean.getRejectionLetterRemarks2();
		}else{
			autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks();
		}
		
		clickAction = false;

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<ReimbursementRejectionDto>(
				ReimbursementRejectionDto.class);
		this.binder.setItemDataSource(this.bean);
	}

	public Component getContent() {

		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		if(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
		
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}	
		
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

		FormLayout diseasedLayout = new FormLayout();
		if(((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
				|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
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
			deathDate.setValue(bean.getReimbursementDto().getDateOfDeath() != null ? bean.getReimbursementDto().getDateOfDeath() : bean.getReimbursementDto().getClaimDto().getDeathDate());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementDto().getDeathReason() != null ? bean.getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
		}
		decideOnRejectionPageLayout.addComponent(diseasedLayout);
		
		
		processRejectionDetailsTable.init("Rejection Details", false, false);
		if (rejectionDetailsList != null && !rejectionDetailsList.isEmpty()) {
			processRejectionDetailsTable.setTableList(rejectionDetailsList);
			processRejectionDetailsTable
					.tableSelectHandler(rejectionDetailsList.get(0));
		}

		// decideOnRejectionPageLayout.addComponent(rejectionDetailsTable);
		decideOnRejectionPageLayout.addComponent(processRejectionDetailsTable);
		decideOnRejectionPageLayout.setSpacing(true);

		rejectionRemarks = new TextArea("Rejection Remarks");
		rejectionRemarks.setWidth("500px");
		rejectionRemarks.setMaxLength(8000);
		if(	this.bean.getRejectionRemarks2()!=null){

			rejectionRemarks.setValue(this.bean != null
					&& this.bean.getRejectionRemarks() != null ? this.bean.getRejectionRemarks()+ this.bean.getRejectionRemarks2(): "");
		}else{
			rejectionRemarks.setValue(this.bean != null
					&& this.bean.getRejectionRemarks() != null ? this.bean	
							.getRejectionRemarks() : "");
		}
		rejectionRemarks.setEnabled(false);

		rejectionLetterRemarks = new TextArea("Rejection Letter Remarks");
		rejectionLetterRemarks.setWidth("500px");
		rejectionLetterRemarks.setHeight("100px");
		rejectionLetterRemarks.setMaxLength(8000);
		if(this.bean.getRejectionLetterRemarks2()!=null){
			
			rejectionLetterRemarks.setValue(this.bean != null
					&& this.bean.getRejectionLetterRemarks() != null ? this.bean.getRejectionLetterRemarks()+ this.bean.getRejectionLetterRemarks2(): "");
		}else{
			rejectionLetterRemarks.setValue(this.bean != null
					&& this.bean.getRejectionLetterRemarks() != null ? this.bean	
					.getRejectionLetterRemarks() : "");
		}
		
		rejectionLetterRemarks.setEnabled(false);
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

		rejectionRemarksTxta = binder.buildAndBind("Rejection Remarks",
				"rejectionRemarks", TextArea.class);
		rejectionRemarksTxta.setWidth("500px");
		rejectionRemarksTxta.setHeight("200px");
		rejectionRemarksTxta.setMaxLength(8000);
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

		rejectionLetterRemarksTxta = binder.buildAndBind(
				"Rejection Letter Remarks", "rejectionLetterRemarks",
				TextArea.class);
		rejectionLetterRemarksTxta.setWidth("500px");
		rejectionLetterRemarksTxta.setHeight("200px");
		rejectionLetterRemarksTxta.setMaxLength(8000);
		rejectionLetterRemarksTxta.setRequired(true);
		rejectionLetterRemarksTxta
				.setRequiredError("Please Proved value for Rejection Letter Remarks");
		

		mandatoryFields.add(reDraftRejectionRemarksTxta);
		mandatoryFields.add(disapproveRejectionRemarksTxta);
		mandatoryFields.add(rejectionLetterRemarksTxta);
		mandatoryFields.add(rejectionRemarksTxta);
		showOrHideValidation(false);

		reDraftRejectionBtn = new Button("Redraft Rejection Letter Remarks");
		disapproveRejectionBtn = new Button("Disapprove Rejection");
		approveRejectionBtn = new Button("Approve Rejection");

		addListener();

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

	private void addListener() {

		reDraftRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(
						DecideOnPARejectionPresenter.BUILD_PA_RE_DRAFT_REJECTION_LAYOUT,
						null);

			}

		});

		disapproveRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						DecideOnPARejectionPresenter.BUILD_PA_DISAPPROVE_REJECTION_LAYOUT,
						null);
			}
		});

		approveRejectionBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(
						DecideOnPARejectionPresenter.BUILD_PA_APPROVE_REJECTION_LAYOUT,
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
		
		this.bean.setRejectionRemarks(null);
		this.bean.setRejectionLetterRemarks(null);
		this.bean.setDisapprovedRemarks(null);		

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
		
		
		this.bean.setRejectionRemarks(null);
		this.bean.setRejectionLetterRemarks(null);
		this.bean.setRedraftRemarks(null);
		

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
		rejectionRemarksTxta.setMaxLength(8000);
		rejectionLetterRemarksTxta.setValue(autoRejectionLetterRemarks);
		rejectionLetterRemarksTxta.setMaxLength(8000);
		//this.binder.bind(rejectionLetterRemarksTxta, "rejectionLetterRemarks");
		//this.binder.bind(rejectionRemarksTxta, "rejectionRemarks");
		FormLayout approveFormLayout = new FormLayout(rejectionRemarksTxta,
				rejectionLetterRemarksTxta);
		dynamicLayout.addComponent(approveFormLayout);
		
		if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
				|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {

			nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			chkNomineeDeceased = null;
			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null && 
					!bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()) {
				nomineeDetailsTable.setTableList(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
				nomineeDetailsTable.setViewColumnDetails();
				nomineeDetailsTable.generateSelectColumn();
				
				chkNomineeDeceased = new CheckBox("Nominee Deceased");
				if(bean.getReimbursementDto().getNomineeDeceasedFlag() != null && bean.getReimbursementDto().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					chkNomineeDeceased.setValue(Boolean.TRUE);
				}
			}	
			
			dynamicLayout.addComponent(nomineeDetailsTable);
			
			if(chkNomineeDeceased != null){
				dynamicLayout.addComponent(chkNomineeDeceased);
				addNomineeDeceasedListener();
			}
		
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
			
			if(isNomineeDeceased()){
				enableLegalHeir = Boolean.TRUE;
				nomineeDetailsTable.setEnabled(false);
			}

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
				
		this.bean.setRedraftRemarks(null);
		this.bean.setDisapprovedRemarks(null);		

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

				showOrHideValidation(true);

				if (!binder.isValid()) {
					String eMsg = "";
					for (Field<?> field : this.binder.getFields()) {
						ErrorMessage errMsg = ((AbstractField<?>) field)
								.getErrorMessage();
						if (errMsg != null) {
							eMsg += errMsg.getFormattedHtmlMessage();
						}

					}
					setRequired(true);
					showErrorMessage(eMsg);
					return !clickAction;
				} else {

					 if(rejectionLetterRemarksTxta != null &&
					 rejectionLetterRemarksTxta.getValue() != null &&
					 rejectionLetterRemarksTxta.getValue().length() > 8000){
					 showErrorMessage("Maximum of 8000 Charactes only allowed for remarks");
					 return !clickAction;
					 }

					this.binder.commit();
					showOrHideValidation(false);

					
					if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
							&& (SHAConstants.REJECTION_APPROVE_OUT_COME).equalsIgnoreCase(this.bean.getStatusValue())
							&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
									|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
						
						if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()
								&& !isNomineeDeceased()){
							List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
						
							if(tableList != null && !tableList.isEmpty()){
								bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
								StringBuffer nomineeNames = new StringBuffer("");
								int selectCnt = 0;
								for (NomineeDetailsDto nomineeDetailsDto : tableList) {
									nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
									if(nomineeDetailsDto.isSelectedNominee()) {
//										nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
										nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
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
		
		/*if(this.bean.getRejectionLetterRemarks2() !=null){
			this.autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks()+this.bean.getRejectionLetterRemarks2();
		}else{
			this.autoRejectionLetterRemarks = this.bean.getRejectionLetterRemarks();
		}*/
		
		fireViewEvent(DecideOnPARejectionPresenter.BUILD_PA_APPROVE_REJECTION_LAYOUT,null);		
	}
	
	private void addNomineeDeceasedListener(){
		 chkNomineeDeceased
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					boolean value = (Boolean) event.getProperty().getValue();
					
					if(value){
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(false);
						}
						if(legalHeirDetails != null){
							if(bean.getReimbursementDto().getLegalHeirDTOList() != null 
									&& !bean.getReimbursementDto().getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getReimbursementDto().getLegalHeirDTOList());
							}
							legalHeirDetails.getBtnAdd().setEnabled(true);
						
						}
					}else{
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(true);
						}
						if(legalHeirDetails != null){
							legalHeirDetails.deleteRows();
							legalHeirDetails.getBtnAdd().setEnabled(false);
						}
					}
					 
					}
							
					}
					
			});
	 }
	 
	 private Boolean isNomineeDeceased(){
		 if(chkNomineeDeceased != null && chkNomineeDeceased.getValue() != null && chkNomineeDeceased.getValue()){
			 return true;
		 }
		 return false;
	 }
}