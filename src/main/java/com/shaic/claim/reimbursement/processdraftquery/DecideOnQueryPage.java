package com.shaic.claim.reimbursement.processdraftquery;

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

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.ViewDraftRemarksTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
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
import com.vaadin.ui.themes.ValoTheme;
/**
 * 
 * @author Lakshminarayana
 *
 */

public class DecideOnQueryPage extends ViewComponent{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;

	private ReimbursementQueryDto bean;
	
	private BeanFieldGroup<ReimbursementQueryDto> binder;
	
	private TextField queryRemarks;
	
	//private TextArea reDraftRemarks;
	
	private TextArea queryLetterRemarks;
	
	private Button reDraftQueryBtn;
	
	private Button rejectQueryBtn;
	
	private Button approveQueryBtn;
	
//	private TextField queryRemarksTxt;
	
	private TextArea queryRemarksTxt;
	
	private String autoQueryRemarks;
	
	private String autoQueryLetterRemarks;
	
	//private String autoQueryRedraftRemarks;
	
	private TextArea queryLetterRemarksTxta;
	
//	private RichTextArea queryLetterRemarksTxta;
	
	//private TextArea reDraftRemarksTxta;
	
	private TextArea rejectionRemarksTxta;
	
	private HorizontalLayout buttonsLayout;
	
	private VerticalLayout dynamicLayout; 
	
	private List<ViewQueryDTO> queryDetailsList;
	
	@Inject
	private Instance<ViewDraftRemarksTable> viewDraftRemarksTableInstance;
	
	private ViewDraftRemarksTable viewDraftRemarksTableObj;
	
	@Inject
	private Instance<ViewRedraftRemarksTable> viewRedraftRemarksTableInstance;
	
	private ViewRedraftRemarksTable viewRedraftRemarksTableObj;
	
	private GWizard wizard;
		
	private boolean clickAction = false;
	
	//protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private VerticalLayout decideOnQueryPageLayout;
	
	private List<Component> mandatoryFields = new ArrayList<Component>(); 
	
	@Inject
	private Instance<QueryDraftLetterDetailsTable> draftQueryTableInstance;
	
	private QueryDraftLetterDetailsTable draftQueryTableInstanceObj;
	
	@Inject
	private Instance<QueryRedraftLetterDetailsTable> redraftQueryTableInstance;
	
	private QueryRedraftLetterDetailsTable redraftQueryTableInstanceObj;
	
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
	
	private Map<String, Object> draftrefData;
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ClaimQueryDto bean, GWizard wizard) {
		this.bean = bean.getReimbursementQueryDto();
		this.queryDetailsList = bean.getQueryDetails();
		this.wizard = wizard;
		this.preauthDto = bean.getPreAuthDto(); 
		
		this.autoQueryRemarks = this.bean != null && this.bean.getQueryRemarks() != null ? this.bean.getQueryRemarks():"";
		this.autoQueryLetterRemarks = this.bean != null && this.bean.getQueryLetterRemarks() != null? this.bean.getQueryLetterRemarks() : "";
		clickAction = false;
		
		draftQueryTableInstanceObj = draftQueryTableInstance.get();
		
		draftQueryTableInstanceObj.init("", true, true);
		draftrefData = new HashMap<String, Object>();
		draftQueryTableInstanceObj.setReference(draftrefData);
		draftQueryTableInstanceObj.setTableList(this.bean.getQueryDarftList());
		
		redraftQueryTableInstanceObj = redraftQueryTableInstance.get();
		redraftQueryTableInstanceObj.init("", true, true);
		Map<String, Object> refData = new HashMap<String, Object>();
		redraftQueryTableInstanceObj.setReference(refData);
		
		if(this.bean.getRedraftList() != null && ! this.bean.getRedraftList().isEmpty()){
			redraftQueryTableInstanceObj.setTableList(this.bean.getRedraftList());	
		}
		
		wizard.getNextButton().setEnabled(false);
		
		if(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
		
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<ReimbursementQueryDto>(
				ReimbursementQueryDto.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	public Component getContent() {
		
		initBinder();	
		
		if(bean.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
				
		return buildDecideOnQueryPageLayout();
	}
	
	private VerticalLayout buildDecideOnQueryPageLayout(){
		clickAction = false;
		decideOnQueryPageLayout = new VerticalLayout();
		decideOnQueryPageLayout.setSizeFull();
		
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
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
				&& bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
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
		
		queryDetailsTableObj.init("Query Details", false, false);
		
		queryDetailsTableObj.setQueryDetaqilsVisibleColumns();
		
		if(queryDetailsList != null && !queryDetailsList.isEmpty())
		{	
			for (ViewQueryDTO viewQueryDTO : queryDetailsList) {
				if(viewQueryDTO.getKey() == this.bean.getKey()){
					queryDetailsTableObj.tableSelectHandler(viewQueryDTO);
				}
			}
			queryDetailsTableObj.setTableList(queryDetailsList);
		}
		decideOnQueryPageLayout.addComponent(queryDetailsTableObj);
		decideOnQueryPageLayout.setSpacing(true);
				
		queryRemarks = new TextField("Query Remarks");
		queryRemarks.setWidth("50%");               
		queryRemarks.setValue(this.autoQueryRemarks);
		queryRemarks.setEnabled(false);
		
		queryLetterRemarks = new TextArea();
		queryLetterRemarks.setWidth("50%");
		queryLetterRemarks.setHeight("100px");
		queryLetterRemarks.setValue(this.autoQueryLetterRemarks);
		queryLetterRemarks.setEnabled(false);
				

		FormLayout drafLayout = new FormLayout();
		viewDraftRemarksTableObj = viewDraftRemarksTableInstance.get();
		viewDraftRemarksTableObj.init("", false, false);
		viewDraftRemarksTableObj.invitView(this.bean.getKey(), SHAConstants.QUERY_DRAFT_OUT_COME);
		
		drafLayout.setCaption("Query Letter Remarks");
		drafLayout.setSizeFull();
		drafLayout.addComponents(queryLetterRemarks,viewDraftRemarksTableObj);
		
		HorizontalLayout redrafLayout = new HorizontalLayout();
		
		if(this.bean.getRedraftRemarks() != null && !this.bean.getRedraftRemarks().isEmpty()){
			viewRedraftRemarksTableObj = viewRedraftRemarksTableInstance.get();
			viewRedraftRemarksTableObj.init("", false, false);
			viewRedraftRemarksTableObj.invitView(this.bean);
			redrafLayout.setCaption("Redraft Remarks");
			redrafLayout.setSizeFull();
			redrafLayout.addComponent(viewRedraftRemarksTableObj);	
		}
		
		
//		FormLayout queryFrmLayout = new FormLayout(queryRemarks,queryLetterRemarks);

		FormLayout queryFrmLayout = new FormLayout(queryRemarks,drafLayout,redrafLayout);
		
		queryFrmLayout.setSizeFull();
		
		decideOnQueryPageLayout.addComponent(dateLayout);
		
		decideOnQueryPageLayout.addComponent(queryFrmLayout);
				
//		reDraftRemarksTxta = binder.buildAndBind("Redraft Remarks", "redraftRemarks", TextArea.class);
//		reDraftRemarksTxta.setWidth("50%");
//		reDraftRemarksTxta.setHeight("100px");
//		reDraftRemarksTxta.setRequired(true);
//		reDraftRemarksTxta.setRequiredError("Please provide value for Redraft Remarks");  
//		reDraftRemarksTxta.setMaxLength(4000);
		
		
		rejectionRemarksTxta = binder.buildAndBind("Rejection Remarks", "rejectionRemarks", TextArea.class);
		rejectionRemarksTxta.setWidth("50%");
		rejectionRemarksTxta.setHeight("100px");
		rejectionRemarksTxta.setRequired(true);
		rejectionRemarksTxta.setRequiredError("Please provide value for Rejection Remarks");
		rejectionRemarksTxta.setMaxLength(4000);
		
		
//		queryRemarksTxt = binder.buildAndBind("Query Remarks", "queryRemarks", TextField.class);
		queryRemarksTxt = binder.buildAndBind("Query Remarks", "queryRemarks", TextArea.class);
		queryRemarksTxt.setWidth("50%");
				
		queryLetterRemarksTxta = binder.buildAndBind("", "queryLetterRemarks", TextArea.class);
		queryLetterRemarksTxta.setWidth("50%");
		queryLetterRemarksTxta.setHeight("100px");
		queryLetterRemarksTxta.setMaxLength(4000);
		//Vaadin8-setImmediate() queryLetterRemarksTxta.setImmediate(true);
//		queryLetterRemarksTxta.setRequired(true);
//		queryLetterRemarksTxta.setRequiredError("Please provide value for Query Letter Remarks");
				
				
//		 mandatoryFields.add(reDraftRemarksTxta);
		 mandatoryFields.add(rejectionRemarksTxta);
//		 mandatoryFields.add(queryLetterRemarksTxta);
		 showOrHideValidation(false);
		
		reDraftQueryBtn = new Button("Redraft Query");
		rejectQueryBtn = new Button("Reject Query");
		approveQueryBtn = new Button("Approve Query");
		
		
		addListener();
		addDateListener();
		
		buttonsLayout = new HorizontalLayout(reDraftQueryBtn,rejectQueryBtn, approveQueryBtn);
		
		buttonsLayout.setSpacing(true);
		
		decideOnQueryPageLayout.addComponent(buttonsLayout);
		decideOnQueryPageLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_RIGHT);
		
		dynamicLayout = new VerticalLayout();

		
//		FormLayout approveFormLayout = new FormLayout(queryRemarksTxt,queryLetterRemarksTxta);
		
		VerticalLayout approveFormLayout = new VerticalLayout();
		
		FormLayout queryRemarksFrmLayout = new FormLayout(queryRemarksTxt);
		
		
		VerticalLayout drafttableLayout = new VerticalLayout();
		drafttableLayout.setCaption("Query Letter Remarks");
		draftQueryTableInstanceObj.setWidth("70%");
		drafttableLayout.addComponents(queryLetterRemarksTxta,draftQueryTableInstanceObj);
		drafttableLayout.setSizeFull();
		queryRemarksFrmLayout.addComponent(drafttableLayout);
		
		approveFormLayout.addComponents(queryRemarksFrmLayout);
		approveFormLayout.setSizeFull();

		dynamicLayout.addComponent(approveFormLayout);
		
		decideOnQueryPageLayout.addComponent(dynamicLayout);
		
		decideOnQueryPageLayout.setSpacing(true);
		
		return decideOnQueryPageLayout;		
		
	}
	
	public void showOrHideValidation(boolean isVisible)
	{
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
	private void addListener(){
		
		reDraftQueryBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(DecideOnQueryPresenter.BUILD_RE_DRAFT_QUERY_LAYOUT,null);  		
				
			}
			
		});
		
		rejectQueryBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(DecideOnQueryPresenter.BUILD_REJECT_QUERY_LAYOUT, null); 					
			}			
		});
		
		approveQueryBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
	
				fireViewEvent(DecideOnQueryPresenter.BUILD_APPROVE_QUERY_LAYOUT,null);				
			}
			
		});		
		
	}
	
	public void buildRedraftLayout() {
		clickAction = true;
		if(dynamicLayout != null && dynamicLayout.getComponentCount() > 0 ){
			decideOnQueryPageLayout.removeComponent(dynamicLayout);
			dynamicLayout.removeAllComponents();
		}
		else{
			dynamicLayout = new VerticalLayout();
		}				
		
//		dynamicLayout.addComponent(new FormLayout(reDraftRemarksTxta));
				
		redraftQueryTableInstanceObj = redraftQueryTableInstance.get();
		redraftQueryTableInstanceObj.init("", true, true);
		redraftQueryTableInstanceObj.setReference(new HashMap<String,Object>());
		
		if(this.bean.getRedraftList() != null && !this.bean.getRedraftList().isEmpty()){
			redraftQueryTableInstanceObj.setTableList(this.bean.getRedraftList());	
		}
		else{
			for(int i =1;i<6;i++){
				DraftQueryLetterDetailTableDto redraftBean = new DraftQueryLetterDetailTableDto();
				redraftBean.setSno(i);
				redraftBean.setProcessType("R");
				redraftQueryTableInstanceObj.addBeanToList(redraftBean);
			}			
		}
		redraftQueryTableInstanceObj.setWidth("70%");
		HorizontalLayout redraftLayout = new HorizontalLayout(redraftQueryTableInstanceObj);
		redraftLayout.setSizeFull();
		redraftLayout.setCaption("Redraft Remarks");
		FormLayout redraftfrmLayout = new FormLayout();
		redraftfrmLayout.addComponent(redraftLayout);
		dynamicLayout.addComponent(redraftfrmLayout);
		
		decideOnQueryPageLayout.addComponent(dynamicLayout);
	
//		this.binder.bind(reDraftRemarksTxta, "redraftRemarks");
		
//		if(!mandatoryFields.contains(reDraftRemarksTxta)){
//			mandatoryFields.add(reDraftRemarksTxta);
//		}
		
//		unbindField(queryLetterRemarksTxta);
//		mandatoryFields.remove(queryLetterRemarksTxta);
		queryLetterRemarksTxta.setValue("");
		unbindField(queryRemarksTxt);
		mandatoryFields.remove(queryRemarksTxt);
//		queryRemarksTxt.setValue(null);
		unbindField(rejectionRemarksTxta);
		mandatoryFields.remove(rejectionRemarksTxta);
//		rejectionRemarksTxta.setValue(null);
		
		
//		this.bean.setQueryRemarks(null);
//		this.bean.setQueryLetterRemarks(null);
//		this.bean.setRejectionRemarks(null);
		
		Long statusKey = getStatusKey(this.bean.getReimbursementDto().getStageSelectValue().getId(),SHAConstants.QUERY_REDRAFT_OUT_COME);
		this.bean.setStatusKey(statusKey);

		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
	}
	
	public void buildRejectQueryLayout() {
		clickAction = true;
		if(dynamicLayout != null && dynamicLayout.getComponentCount() > 0 ){
			decideOnQueryPageLayout.removeComponent(decideOnQueryPageLayout);
			dynamicLayout.removeAllComponents();
		}
		else{
			dynamicLayout = new VerticalLayout();
		}				
		
		dynamicLayout.addComponent(new FormLayout(rejectionRemarksTxta));
		decideOnQueryPageLayout.addComponent(dynamicLayout);
		
		if(!mandatoryFields.contains(rejectionRemarksTxta)){
			mandatoryFields.add(rejectionRemarksTxta);
		}
		
		this.binder.bind(rejectionRemarksTxta, "rejectionRemarks");
		
//		unbindField(reDraftRemarksTxta);
//		mandatoryFields.remove(reDraftRemarksTxta);
//		reDraftRemarksTxta.setValue(null);
		unbindField(queryRemarksTxt);
		mandatoryFields.remove(queryRemarksTxt);
//		queryRemarksTxt.setValue(null);
//		unbindField(queryLetterRemarksTxta);
//		mandatoryFields.remove(queryLetterRemarksTxta);
		queryLetterRemarksTxta.setValue("");
		
//		this.bean.setQueryRemarks(null);
//		this.bean.setQueryLetterRemarks("");
//		this.bean.setRedraftRemarks(null);
		
		Long statusKey = getStatusKey(this.bean.getReimbursementDto().getStageSelectValue().getId(),SHAConstants.QUERY_REJECTION_OUT_COME);
		this.bean.setStatusKey(statusKey);
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
	}
	
	public void buildApproveLayout() {
		
		clickAction = true;
		this.bean.setQueryRemarks(autoQueryRemarks);
		this.bean.setQueryLetterRemarks(autoQueryLetterRemarks);
		if(dynamicLayout != null && dynamicLayout.getComponentCount() > 0 ){
			decideOnQueryPageLayout.removeComponent(dynamicLayout);
			dynamicLayout.removeAllComponents();
		}
		else{
			dynamicLayout = new VerticalLayout();
			dynamicLayout.setSizeFull();
		}	
		
		queryRemarksTxt.setValue(queryRemarks.getValue());
		queryLetterRemarksTxta.setValue(autoQueryLetterRemarks);
//		FormLayout approveFormLayout = new FormLayout(queryRemarksTxt,queryLetterRemarksTxta);
		
		FormLayout queryremarksfrmLayout = new FormLayout();
		
		queryremarksfrmLayout.addComponent(queryRemarksTxt);
		
		
		
		draftQueryTableInstanceObj = draftQueryTableInstance.get();
		draftQueryTableInstanceObj.init("", true, true);
		draftQueryTableInstanceObj.setReference(new HashMap<String,Object>());
		draftQueryTableInstanceObj.setTableList(this.bean.getQueryDarftList());
		
		FormLayout draftLetterReamrksLayout = new FormLayout();
		draftLetterReamrksLayout.setCaption("Query Letter Remarks");
//		draftLetterReamrksLayout.setSizeFull();
		draftQueryTableInstanceObj.setWidth("70%");
		draftLetterReamrksLayout.addComponents(queryLetterRemarksTxta,draftQueryTableInstanceObj);
		queryremarksfrmLayout.addComponent(draftLetterReamrksLayout);
//		dynamicLayout.addComponents(queryremarksfrmLayout,draftLetterReamrksLayout);
		dynamicLayout.addComponents(queryremarksfrmLayout);
		
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
			relationshipContainer.addAll(this.preauthDto.getLegalHeirDto().getRelationshipContainer());
			draftrefData.put("relationship", relationshipContainer);
			legalHeirDetails.setReferenceData(draftrefData);
			
			this.preauthDto.setClaimDTO(bean.getReimbursementDto().getClaimDto());
			this.preauthDto.setNewIntimationDTO(bean.getReimbursementDto().getClaimDto().getNewIntimationDto());
			this.preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementDto().getPatientStatusId(),""));
			this.preauthDto.setNewIntimationDTO(bean.getReimbursementDto().getClaimDto().getNewIntimationDto());
			
			legalHeirDetails.init(this.preauthDto);
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
		
		decideOnQueryPageLayout.addComponent(dynamicLayout);
		
				
//		if(!mandatoryFields.contains(queryLetterRemarksTxta)){
//			mandatoryFields.add(queryLetterRemarksTxta);
//		}
		
		this.binder.bind(queryRemarksTxt, "queryRemarks");
		this.binder.bind(queryLetterRemarksTxta, "queryLetterRemarks");
		
//		unbindField(reDraftRemarksTxta);
//		this.mandatoryFields.remove(reDraftRemarksTxta);
//		this.reDraftRemarksTxta.setValue(null);
		unbindField(rejectionRemarksTxta);
//		this.mandatoryFields.remove(rejectionRemarksTxta);
//		rejectionRemarksTxta.setValue(null);
		
//		this.bean.setRedraftRemarks(null);
//		this.bean.setRejectionRemarks(null);

		Long statusKey = getStatusKey(this.bean.getReimbursementDto().getStageSelectValue().getId(),SHAConstants.QUERY_APPROVE_OUT_COME);
		this.bean.setStatusKey(statusKey);		
		
		wizard.getNextButton().setEnabled(true);
		
	}
	
//	public boolean checkqueryApproved(){
//		
//		if(this.bean.getQueryLetterRemarks()!= null  ){
//		
//			return true;
//		}
//		return false;
//	}
	
	public void updateBean(){
		try{		
//		if( this.reDraftRemarksTxta != null && this.reDraftRemarksTxta.getValue() != null){
//			unbindField(queryLetterRemarksTxta);
//			unbindField(rejectionRemarksTxta);
//		}
//		else if(this.queryLetterRemarksTxta != null && this.queryLetterRemarksTxta.getValue() != null){
//			unbindField(rejectionRemarksTxta);
//			unbindField(reDraftRemarksTxta);
//		}
//		else
//		{
//			unbindField(queryLetterRemarks);
//			unbindField(reDraftRemarksTxta);
//		}
			if(binder.isValid()){
				this.binder.commit();
			}
		
		this.bean.setQueryDarftList(draftQueryTableInstanceObj.getValues());
		this.bean.setRedraftList(redraftQueryTableInstanceObj.getValues());
		//CR 1044
		this.bean.getReimbursementDto().getClaimDto().setAdmissionDate(admissionDate.getValue());
		this.bean.getReimbursementDto().getClaimDto().setDischargeDate(dischargeDate.getValue());
		
		this.bean.setDeletedList(draftQueryTableInstanceObj.getDeltedQueryDraftRemarksList());
		if(this.bean.getDeletedList() != null){
			this.bean.getDeletedList().addAll(redraftQueryTableInstanceObj.getDeltedQueryRedraftList());
		} else {
			this.bean.setDeletedList(redraftQueryTableInstanceObj.getDeltedQueryRedraftList());
		}
		
		
		
		
	} catch (CommitException e) {
		
		
		
		e.printStackTrace();
	}
		
	}
	
	
	private Long getStatusKey(Long stageKey, String status){
		
		Long statusKey = 0l;
		switch(status){
		case SHAConstants.QUERY_APPROVE_OUT_COME :
													if (stageKey.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)) {
										
														statusKey = ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS;
													} else if (stageKey.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)) {
														statusKey = ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS;
														}
													break;
		
		
		case SHAConstants.QUERY_REDRAFT_OUT_COME :
													if(stageKey.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
													{
														statusKey = ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS;
													}
													else if(stageKey.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
													{
														statusKey = ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS;
													}	
													break;
		case SHAConstants.QUERY_REJECTION_OUT_COME :
													if(stageKey.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
													{
														statusKey = ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS;
													}
													else if(stageKey.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
													{
														statusKey = ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS;
													}
													break;
		}
		
		return statusKey;
		
	}	
	
	public boolean validatePage(){
		
		if(clickAction){
				
			if(queryLetterRemarksTxta.getValue() != null && !("").equals(queryLetterRemarksTxta.getValue()) && queryLetterRemarksTxta.getValue().length() > 4000){
					showErrorMessage("Maximum of 4000 characters only allowed for Query Letter Remarks");
					return !clickAction;
			}
			
			if(admissionDate.getValue() == null || dischargeDate.getValue() == null){
				showErrorMessage("Date of Admission / Date of Discharge can't be empty. Please fill the details.");
				return !clickAction;
			}
			
//			if(("").equals(reDraftRemarksTxta.getValue())){
//					showErrorMessage("Please Enter a value for the Mandatory Field Redraft Remarks");
//					return !clickAction;
//			}
			
			if(this.bean.getStatusKey() != null && (this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){
				
				StringBuffer errMessg = new StringBuffer("");
				
				if(!draftQueryTableInstanceObj.isValid()){
//				showErrorMessage("Please Enter atleast one value for the Query Letter Remarks (and/or)  Maximum of 1000 Characters only allowed");
//				return !clickAction;
					errMessg.append("Please Enter atleast one value for the Query Letter Remarks (and/or)  Maximum of 1000 Characters only allowed<br>");
				}
				
				if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
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
//									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
								    selectCnt++;	
								}
							}
							bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeSelectCount(selectCnt);
							if(selectCnt>0){
								bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
								bean.getReimbursementDto().setNomineeName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
								bean.getReimbursementDto().setNomineeAddr(null);
//								bean.setHasError(false);
							}
							else{
								bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
								
								/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("MNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("LNAME").toString());
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());*/
								
								//TODO alert for selecting Nominee to be done
								errMessg.append("Please Select Nominee<br>");								
							}							
						}
					}
					else{
						bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(null);
						bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
						
						if(this.legalHeirDetails.isValid()) {
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
							errMessg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
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
							errMessg.append("Please Enter Claimant / Legal Heir Details");							
						}
						else{
							bean.getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
						}*/	
					}
					
					if(!errMessg.toString().isEmpty()){
						showErrorMessage(errMessg.toString());
						return !clickAction;
					}	
			    }	
			
				if(this.bean.getStatusKey() != null && (this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS) || this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS))){
					if(!redraftQueryTableInstanceObj.isValid()){
						showErrorMessage("Maximum of 1000 Characters only allowed for Redraft Remarks");
						return !clickAction;
					}	
				}
			}
			
//			if(("").equals(rejectionRemarksTxta.getValue())){
//				showErrorMessage("Please Enter a value for the Mandatory Field Rejection Remarks");
//				return !clickAction;
//			}

			if(this.bean.getStatusKey() != null && (this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS) || this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))){
				if(("").equals(rejectionRemarksTxta.getValue())){
					showErrorMessage("Please Enter a value for the Mandatory Field Rejection Remarks");
					return !clickAction;
				}
			}
						
//			showOrHideValidation(true);
			
			showOrHideValidation(true);
			
			if(!binder.isValid()){
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
			}
			else{
			
//				if(queryLetterRemarksTxta != null && queryLetterRemarksTxta.getValue() != null && queryLetterRemarksTxta.getValue().length() > 4000){
//					showErrorMessage("Maximum of 4000 Charactes only allowed for remarks");
//				return !clickAction;
//				}
				
				updateBean();
				showOrHideValidation(false);
				this.bean.setUsername(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				this.bean.setPassword(UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());
			}	
		}else{
			showErrorMessage("Please Click ReDraft / Reject / Approve Button to process the Query");
		}
			
		return clickAction;
				
//				if(this.bean.getRedraftRemarks() != null){
//					this.bean.setQueryRemarks(null);
//					this.bean.setQueryLetterRemarks(null);
//					this.bean.setRejectionRemarks(null);
//				}
//				else if(this.rejectionRemarksTxta.isModified()){
//					this.bean.setRedraftRemarks(null);
//					this.bean.setQueryLetterRemarks(null);
//					this.bean.setQueryRemarks(null);
//				}
//				else if(this.queryLetterRemarksTxta.isModified() ){
//					this.bean.setRejectionRemarks(null);
//					this.bean.setRedraftRemarks(null);
//				}
				
//				if(this.bean.getQueryLetterRemarks() == null){
//					wizard.getFinishButton().setEnabled(true);
//					wizard.getNextButton().setEnabled(false);
//				}
//				else{
//					wizard.getFinishButton().setEnabled(false);
//					wizard.getNextButton().setEnabled(false);
//				}
			
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
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
	
	public void setpreviousView(ClaimQueryDto queryDto) {
		
			fireViewEvent(DecideOnQueryPresenter.BUILD_APPROVE_QUERY_LAYOUT,queryDto);		
	}
	
	public void updateBean(ClaimQueryDto updatedDto){
		this.bean = updatedDto.getReimbursementQueryDto();
		this.autoQueryLetterRemarks = this.bean.getQueryLetterRemarks();
		this.autoQueryRemarks = this.bean.getQueryRemarks();
		
	}
	
//	public void setDeltedDraftRemarksToBean(DraftQueryLetterDetailTableDto deltedObj){
//		List<DraftQueryLetterDetailTableDto> draftedLetterRemarksList = this.bean.getQueryDarftList();
//		if(draftedLetterRemarksList != null && !draftedLetterRemarksList.isEmpty()){
//			int delindex = -1;
//			for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : draftedLetterRemarksList) {
//				
//				if(draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks() != null && !draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().isEmpty() && draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().equalsIgnoreCase(deltedObj.getDraftOrRedraftRemarks())){
//					delindex = 	draftedLetterRemarksList.indexOf(draftQueryLetterDetailTableDto);
//				}
//			}
//			if(delindex >=0){
//				this.bean.getQueryDarftList().get(delindex).setDeltedFlag("Y");
//			}
//		}
//	}
	
	public ReimbursementQueryDto getUpdatedBean(){
		return this.bean;
	}
	
	public void policyValidationPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {					
					dialog.close();				
				}
			});
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
	
	/*private void enableLegalHeir() {
		List<NomineeDetailsDto> nomineeList = bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList();
		
		int selectCnt = 0;
		if(nomineeList != null && !nomineeList.isEmpty()){
			for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
				if(nomineeDetailsDto.isSelectedNominee()) 
					selectCnt++;
			}
			nomineeDetailsTable.enableLegalHeir(false);
		}
		else{
			nomineeDetailsTable.enableLegalHeir(true);
		}		
		
		if(selectCnt>0){				
			nomineeDetailsTable.enableLegalHeir(false);
		}
		else{
			nomineeDetailsTable.enableLegalHeir(true);
		}
		
	}*/
	public void clearObject(){

		if(bean != null){
			bean.setQueryDarftList(null);
			bean.setRedraftList(null);
			bean.getReimbursementDto().getClaimDto().setNewIntimationDto(null);
			bean.getReimbursementDto().setClaimDto(null);
			bean.getReimbursementDto().setDocAcknowledgementDto(null);
			bean.setReimbursementDto(null);
			bean = null;
		}
		
		if(preauthDto != null){
			SHAUtils.setClearPreauthDTO(preauthDto);
		}
		queryDetailsList = null;		
		if(decideOnQueryPageLayout != null){
			decideOnQueryPageLayout.removeAllComponents();
		}
		SHAUtils.setClearReferenceData(draftrefData);
		reimbursementQueryService = null;
		
		
	}
}
