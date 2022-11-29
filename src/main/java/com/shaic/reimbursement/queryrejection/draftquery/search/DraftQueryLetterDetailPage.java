package com.shaic.reimbursement.queryrejection.draftquery.search;

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

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.cdi.UIScoped;
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
@UIScoped
public class DraftQueryLetterDetailPage extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
		
	private SearchDraftQueryLetterTableDTO bean;
	
	private BeanFieldGroup<SearchDraftQueryLetterTableDTO> binder;
	
//	private TextField queryRemarksTxt;
	
	private TextArea queryRemarksTxt;
	
	private TextArea queryLetterRemarksTxta;
	
	@Inject
	private Instance<QueryDraftLetterDetailsTable> tableInstance;
	
	private QueryDraftLetterDetailsTable draftLetterRemarkstableInstanceObj;
	
	@Inject
	private Instance<ViewDraftRemarksTable> viewDraftRemarksTableInstance;
	
	private ViewDraftRemarksTable viewDraftRemarksTableObj;
	
	private TextArea reDraftRemarksTxta;
	
	private HorizontalLayout buttonsLayout;
	
	private VerticalLayout draftQueryLetterDetailLayout;
	
	private VerticalLayout draftLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private GWizard wizard;
	
	private List<ViewQueryDTO> queryDetailsList;
	
	private PopupDateField admissionDate;
	
	private PopupDateField dischargeDate;
	
	private ComboBox cmbPatientStatus;
	private DateField deathDate;
	private TextField txtReasonForDeath;
	
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
//	private RichTextArea rQueryLetterRemarksTxta;

	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	
	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	@PostConstruct
	public void init() {

	}
	public void init(SearchDraftQueryLetterTableDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		queryDetailsList = bean.getQueryDetailsList();
		initView(this.bean);
	}	
//		
//		this.autoQueryRemarks = this.bean != null && this.bean.getQueryRemarks() != null ? this.bean.getQueryRemarks():"";
//		this.autoQueryLetterRemarks = this.bean != null && this.bean.getQueryLetterRemarks() != null? this.bean.getQueryLetterRemarks() : "";
//		clickAction = false;
//		wizard.getNextButton().setEnabled(false);
//		
//	}
	public void initView(SearchDraftQueryLetterTableDTO bean) {
		this.bean = bean;
		
		if(bean.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		draftQueryLetterDetailLayout = getContent();
		
		if(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<SearchDraftQueryLetterTableDTO>(
				SearchDraftQueryLetterTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout getContent() {
		
		initBinder();	
		
		return buildDecideOnQueryPageLayout();
	}
	
	private VerticalLayout buildDecideOnQueryPageLayout(){
		draftQueryLetterDetailLayout = new VerticalLayout();
		draftLayout = new VerticalLayout();
		draftLayout.setSpacing(false);
		List<ViewQueryDTO> queryDetailsList = this.bean.getQueryDetailsList();
		queryDetailsTableObj.init("Query Details", false, false);
		queryDetailsTableObj.setQueryDetaqilsVisibleColumns();
		if(queryDetailsList != null && !queryDetailsList.isEmpty()){
		
			for (ViewQueryDTO viewQueryDTO : queryDetailsList) {
				if(viewQueryDTO.getKey() == this.bean.getQueryKey()){
					queryDetailsTableObj.tableSelectHandler(viewQueryDTO);
				}
			}
			queryDetailsTableObj.setTableList(queryDetailsList);
		}
		
		draftQueryLetterDetailLayout.addComponent(queryDetailsTableObj);
		draftQueryLetterDetailLayout.setSpacing(true);
		
		FormLayout admissionLayout = new FormLayout();
		admissionDate = new  PopupDateField("Date of Admission");
		admissionDate.setValue(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getAdmissionDate());
		admissionDate.setDateFormat("dd/MM/yyyy");
		admissionLayout.addComponent(admissionDate);
		
		FormLayout dischargeLayout = new FormLayout();
		dischargeDate = new  PopupDateField("Date of Discharge");
		dischargeDate.setValue(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getDischargeDate());
		dischargeDate.setDateFormat("dd/MM/yyyy");
		dischargeLayout.addComponent(dischargeDate);
		HorizontalLayout dateLayout = new HorizontalLayout();
		
		dateLayout.addComponent(admissionLayout);
		dateLayout.addComponent(dischargeLayout);
		
		FormLayout diseasedLayout = new FormLayout();
		if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
				||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
				&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			cmbPatientStatus = new ComboBox("Patient Status");
			cmbPatientStatus.setEnabled(false);
			
			BeanItemContainer<SelectValue> patientStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			patientStatusContainer.addBean(new SelectValue(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId(),"Deceased"));
			cmbPatientStatus.setContainerDataSource(patientStatusContainer);
			cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPatientStatus.setItemCaptionPropertyId("value");
			cmbPatientStatus.setValue(patientStatusContainer.getItemIds().get(0));
			
			diseasedLayout.addComponent(cmbPatientStatus);
			
			deathDate = new PopupDateField("Date Of Death");
			deathDate.setDateFormat("dd/MM/yyyy");
			deathDate.setValue(bean.getReimbursementQueryDto().getReimbursementDto().getDateOfDeath());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementQueryDto().getReimbursementDto().getDeathReason() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
		}
		dateLayout.addComponent(diseasedLayout);
		
		
		dateLayout.setMargin(false);
		dateLayout.setWidth("100%");
//		dateLayout.setHeight("40px");	
		draftQueryLetterDetailLayout.addComponent(dateLayout);
		
		
		
////		reDraftRemarksTxta = binder.buildAndBind("Redraft Remarks", "redraftRemarks", TextArea.class);
//		reDraftRemarksTxta = new TextArea("Redraft Remarks");
//		reDraftRemarksTxta.setWidth("50%");
//		reDraftRemarksTxta.setHeight("100px");
//		reDraftRemarksTxta.setValue(this.bean.getReimbursementQueryDto() != null && this.bean.getReimbursementQueryDto().getRedraftRemarks() != null ? this.bean.getReimbursementQueryDto().getRedraftRemarks() : "");
//		reDraftRemarksTxta.setEnabled(false);
		
//		queryRemarksTxt = binder.buildAndBind("Query Remarks", "queryRemarks", TextField.class);
//		queryRemarksTxt = new TextField("Query Remarks");
		queryRemarksTxt = new TextArea("Query Remarks");
		queryRemarksTxt.setWidth("50%");
		queryRemarksTxt.setValue(this.bean.getReimbursementQueryDto() != null && this.bean.getReimbursementQueryDto().getQueryRemarks() != null ? this.bean.getReimbursementQueryDto().getQueryRemarks() : "");
		queryRemarksTxt.setEnabled(false);
				
		queryLetterRemarksTxta = binder.buildAndBind("", "queryLetterRemarks", TextArea.class);
		queryLetterRemarksTxta.setWidth("50%");
		queryLetterRemarksTxta.setHeight("70%");
		queryLetterRemarksTxta.setMaxLength(4000);
		queryLetterRemarksTxta.setNullRepresentation("");
		queryLetterRemarksTxta.setValue(this.bean.getReimbursementQueryDto().getQueryLetterRemarks() != null ? this.bean.getReimbursementQueryDto().getQueryLetterRemarks() : "");
//		queryLetterRemarksTxta.setRequired(true);	
		
		viewDraftRemarksTableObj = viewDraftRemarksTableInstance.get();
		viewDraftRemarksTableObj.init("", false, false);
		HorizontalLayout redrafLayout = new HorizontalLayout();
		
		if((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS).equals(this.bean.getReimbursementQueryDto().getStatusKey()) || (ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS).equals(this.bean.getReimbursementQueryDto().getStatusKey())){
			if(this.bean.getReimbursementQueryDto().getRedraftRemarks() != null && !this.bean.getReimbursementQueryDto().getRedraftRemarks().isEmpty()) 
			{
				viewDraftRemarksTableObj.invitView(this.bean.getReimbursementQueryDto().getKey(), SHAConstants.QUERY_REDRAFT_OUT_COME);
				redrafLayout.setCaption("Redraft Remarks");
				redrafLayout.setSizeFull();
				redrafLayout.addComponent(viewDraftRemarksTableObj);
			}				
		}
		
		draftLetterRemarkstableInstanceObj = tableInstance.get();
		draftLetterRemarkstableInstanceObj.init("", true,true);
		Map<String,Object> refData = new HashMap<String, Object>();
		draftLetterRemarkstableInstanceObj.setReference(refData);
		
		if(bean.getReimbursementQueryDto().getQueryDarftList() != null && !bean.getReimbursementQueryDto().getQueryDarftList().isEmpty()){
			draftLetterRemarkstableInstanceObj.setTableList(bean.getReimbursementQueryDto().getQueryDarftList());   
		}
		else
		{
			List<DraftQueryLetterDetailTableDto> draftList = new ArrayList<DraftQueryLetterDetailTableDto>();
			
			for(int k = 0; k<5; k++){			
				DraftQueryLetterDetailTableDto draftDetailDto = new DraftQueryLetterDetailTableDto();		
				draftDetailDto.setSno(k+1);
				draftDetailDto.setDraftOrRedraftRemarks("");
				draftDetailDto.setProcessType("D");
				draftList.add(draftDetailDto);				
			}	
			draftLetterRemarkstableInstanceObj.setTableList(draftList);
		}
		
		draftLayout.setCaption("Query Letter Remarks");
		draftLetterRemarkstableInstanceObj.setWidth("70%");
		draftLayout.setSpacing(true);
		draftLayout.addComponents(queryLetterRemarksTxta,draftLetterRemarkstableInstanceObj);
//		rQueryLetterRemarksTxta= binder.buildAndBind("Query Letter Remarks", "queryLetterRemarks", RichTextArea.class);
//		rQueryLetterRemarksTxta.setWidth("750px");
//		rQueryLetterRemarksTxta.setHeight("200px");
//		rQueryLetterRemarksTxta.setNullRepresentation("");
//		rQueryLetterRemarksTxta.setRequired(true);
		
		
////		FormLayout draftlayout = new FormLayout(reDraftRemarksTxta,queryRemarksTxt,queryLetterRemarksTxta);
//		FormLayout draftlayout = new FormLayout(reDraftRemarksTxtaviewDraftRemarksTableObj,queryRemarksTxt,draftLayout);
		
		
	FormLayout draftFrmlayout = new FormLayout(redrafLayout,queryRemarksTxt,draftLayout);
//	draftFrmlayout.setSizeFull();
//	draftFrmlayout.setSpacing(true);

	draftQueryLetterDetailLayout.addComponent(draftFrmlayout);
	draftQueryLetterDetailLayout.setSpacing(true);
	
	
	if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
			||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))
			&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
			&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
			&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		
		if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null) {
			nomineeDetailsTable.setTableList(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
		}
		
		draftQueryLetterDetailLayout.addComponent(nomineeDetailsTable);
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
				
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		relationshipContainer.addAll(bean.getPreAuthDto().getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		
		PreauthDTO preauthDto = bean.getPreAuthDto();
		preauthDto.setClaimDTO(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto());
		preauthDto.setNewIntimationDTO(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto());
		preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId(),""));
		preauthDto.setNewIntimationDTO(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto());
		
		legalHeirDetails.init(preauthDto);
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		draftQueryLetterDetailLayout.addComponent(legalHeirLayout);

		if(enableLegalHeir) {
			
			legalHeirDetails.addBeanToList(bean.getReimbursementQueryDto().getReimbursementDto().getLegalHeirDTOList());
//			legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
			legalHeirDetails.getBtnAdd().setEnabled(true);
		}
		else {
			legalHeirDetails.deleteRows();
			legalHeirDetails.getBtnAdd().setEnabled(false);
		}		
	
	}		
	
	submitBtn = new Button("Save");
	submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
//	
//	cancelBtn = new Button("Cancel");
//	
//	cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//	
	addListener();
	addDateListener();
//
//	buttonsLayout = new HorizontalLayout(submitBtn,cancelBtn);
	
	buttonsLayout = new HorizontalLayout(submitBtn);
//	
//	buttonsLayout.setSpacing(true);
//	
	draftQueryLetterDetailLayout.addComponent(buttonsLayout);
	draftQueryLetterDetailLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_CENTER);
	
	
	return draftQueryLetterDetailLayout;
	}
	
	/*private void enableLegalHeir() {
		List<NomineeDetailsDto> nomineeList = bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList();
		
		int selectCnt = 0;
		if(nomineeList != null && !nomineeList.isEmpty()){
			nomineeDetailsTable.enableLegalHeir(false);

			for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
				if(nomineeDetailsDto.isSelectedNominee()) 
					selectCnt++;
			}
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
	
	
	public void submitDraftQueryLetter(SearchDraftQueryLetterTableDTO bean){
		
		if(queryLetterRemarksTxta.getValue() != null && !("").equalsIgnoreCase(queryLetterRemarksTxta.getValue()) && queryLetterRemarksTxta.getValue().length() > 4000){
			showErrorMessage("Maximum of 4000 Charactes only allowed for remarks");
		}
//										
//		if(("").equalsIgnoreCase(queryLetterRemarksTxta.getValue())){
//			showErrorMessage("Please Enter a value for the Mandatory Field");	
//		}
		else{
		if(bean.getReimbursementQueryDto().getRedraftRemarks() != null){
			bean.getReimbursementQueryDto().setRedraftRemarks(bean.getReimbursementQueryDto().getRedraftRemarks());
		}	
		
		
//		if(queryLetterRemarksTxta != null && queryLetterRemarksTxta.getValue() != null && queryLetterRemarksTxta.getValue().length() <= 4000){
		if(queryLetterRemarksTxta != null && queryLetterRemarksTxta.getValue() != null && draftLetterRemarkstableInstanceObj.getValues() != null &&   !draftLetterRemarkstableInstanceObj.getValues().isEmpty()){	
			
			List<DraftQueryLetterDetailTableDto> tableList = draftLetterRemarkstableInstanceObj.getValues();
			
			for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : tableList) {
				draftQueryLetterDetailTableDto.setSno(tableList.indexOf(draftQueryLetterDetailTableDto));
//				bean.getReimbursementQueryDto().setQueryLetterRemarks(tableList.indexOf(draftQueryLetterDetailTableDto) + ". " +draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks());
			}
			
//			bean.getReimbursementQueryDto().setQueryLetterRemarks(queryLetterRemarksTxta.getValue());
			bean.getReimbursementQueryDto().setQueryRemarks(null);
			bean.getReimbursementQueryDto().setRedraftRemarks(null);
			bean.getReimbursementQueryDto().setRejectionRemarks(null);
			
			String userId = UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString();
			bean.setUsername(userId);
			
			String password = UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString();
			bean.setPassword(password);					
			
			fireViewEvent(DraftQueryLetterDetailPresenter.SUBMIT_CLICKED,bean);
		
		}
	 }
			
	}
	
	public void addListener(){
		submitBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				bean = getupdatedBean();
				
				if(!bean.getHasError()){
				
					fireViewEvent(DraftQueryLetterDetailPresenter.SAVE_DRAFT_QUERY,bean);
				}	
				
////				if(("").equalsIgnoreCase(queryLetterRemarksTxta.getValue())){
////					showErrorMessage("Please Enter a value for the Mandatory Field");	
////				}
////				
////				if(bean.getReimbursementQueryDto().getRedraftRemarks() != null){
////					bean.getReimbursementQueryDto().setRedraftRemarks(bean.getReimbursementQueryDto().getRedraftRemarks());
////				}		
////				
////				if(queryLetterRemarksTxta != null && queryLetterRemarksTxta.getValue() != null){
////					bean.getReimbursementQueryDto().setQueryLetterRemarks(queryLetterRemarksTxta.getValue());
////					bean.getReimbursementQueryDto().setQueryRemarks(null);
////					bean.getReimbursementQueryDto().setRedraftRemarks(null);
////					bean.getReimbursementQueryDto().setRejectionRemarks(null);
////					
////					String userId = (String) UI.getCurrent().getSession()
////					.getAttribute(BPMClientContext.USERID);
////					bean.setUsername(userId);
////					
////					String password = (String) UI.getCurrent().getSession()
////					.getAttribute(BPMClientContext.PASSWORD);
////					bean.setPassword(password);					
////					
////					fireViewEvent(DraftQueryLetterDetailPresenter.SUBMIT_CLICKED,bean);
////				
////				}
//												
//				if(("").equalsIgnoreCase(queryLetterRemarksTxta.getValue())){
//					showErrorMessage("Please Enter a value for the Mandatory Field");	
//				}
//				if(bean.getReimbursementQueryDto().getRedraftRemarks() != null){
//					bean.getReimbursementQueryDto().setRedraftRemarks(bean.getReimbursementQueryDto().getRedraftRemarks());
//				}	
//				
////				if(rQueryLetterRemarksTxta.getValue() != null && rQueryLetterRemarksTxta.getValue().length() > 200){
////					showErrorMessage("Maximum of 200 Charactes only allowed for remarks");
////				}
				
//				if(queryLetterRemarksTxta != null && queryLetterRemarksTxta.getValue() != null && queryLetterRemarksTxta.getValue().length() <= 4000){
//					bean.getReimbursementQueryDto().setQueryLetterRemarks(queryLetterRemarksTxta.getValue());
//					bean.getReimbursementQueryDto().setQueryRemarks(null);
//					bean.getReimbursementQueryDto().setRedraftRemarks(null);
//					bean.getReimbursementQueryDto().setRejectionRemarks(null);
//					
//					String userId = UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString();
//					bean.setUsername(userId);
//					
//					String password = UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString();
//					bean.setPassword(password);					
//					
//					fireViewEvent(DraftQueryLetterDetailPresenter.SUBMIT_CLICKED,bean);
//				
//				}
//				else{
//					showErrorMessage("Maximum of 4000 Charactes only allowed for remarks");
//				}
				

			}
		});
		
//	cancelBtn.addClickListener(new Button.ClickListener() {
//		
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void buttonClick(ClickEvent event) {
//			
//			
//			fireViewEvent(DraftQueryLetterDetailPresenter.CANCEL_DRAFT_QUERY_LETTER, null);
//			
//		}
//	});
	
	}
	
	public SearchDraftQueryLetterTableDTO getupdatedBean(){
		
		StringBuffer eMsg = new StringBuffer();
		
		if(!binder.isValid()){
			
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
				eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				
			}
			showErrorMessage(eMsg.toString());
			bean.setHasError(true);
		}
		else{
			try {
				binder.commit();
				List<DraftQueryLetterDetailTableDto> draftRemarksList = draftLetterRemarkstableInstanceObj
						.getValues();
				
				if(queryLetterRemarksTxta.getValue() != null ){
					bean.getReimbursementQueryDto().setQueryLetterRemarks(queryLetterRemarksTxta.getValue());
				}
				
				if(admissionDate.getValue() == null || dischargeDate.getValue() == null){
					eMsg.append("Date of Admission / Date of Discharge can't be empty. Please fill the details.");
					showErrorMessage(eMsg.toString());
					bean.setHasError(true);
					return bean;
				}
				if (draftLetterRemarkstableInstanceObj.isValid()) {

					if (draftRemarksList != null && !draftRemarksList.isEmpty()) {
						bean.getReimbursementQueryDto().setQueryDarftList(
								draftRemarksList);
//						String queryLetterRemarks = "";
//						for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : draftRemarksList) {
//							queryLetterRemarks = queryLetterRemarks+
//									draftQueryLetterDetailTableDto.getSno()
//									+ ") " + draftQueryLetterDetailTableDto
//											.getDraftOrRedraftRemarks() + ". ";
////							draftQueryLetterDetailTableDto.setSno(draftQueryLetterDetailTableDto.getSerialNumber());
//						}
//						bean.getReimbursementQueryDto().setQueryLetterRemarks(
//								queryLetterRemarks);
					}
					bean.getReimbursementQueryDto().setDeletedList(draftLetterRemarkstableInstanceObj.getDeltedQueryDraftRemarksList());
					
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().setAdmissionDate(admissionDate.getValue());
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().setDischargeDate(dischargeDate.getValue());
					
					bean.setHasError(false);
				} else {
					eMsg.append("Please Provide Atleast One Draft Letter Remarks to Submit with a Maximum Of 1000 Characters");
					showErrorMessage(eMsg.toString());
					bean.setHasError(true);
					return bean;			
				}
				
				if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
						&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
							StringBuffer nomineeNames = new StringBuffer("");
							int selectCnt = 0;
							for (NomineeDetailsDto nomineeDetailsDto : tableList) {
								nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
								if(nomineeDetailsDto.isSelectedNominee()) {
									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
								    selectCnt++;	
								}
							}
							bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeSelectCount(selectCnt);
							if(selectCnt>0){
								bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(null);
								bean.setHasError(false);
							}
							else{
								bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
								
								/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("MNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("LNAME").toString());
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());*/
								
								//TODO alert for selecting Nominee to be done
								SHAUtils.showAlertMessageBox("Please Select Nominee");
								bean.setHasError(true);
								return bean;
							}							
						}
					}
					else{
						bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(null);
						bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
						
						if(this.legalHeirDetails.isValid()) {

							//added for support fix IMSSUPPOR-31323
							List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
							legalHeirList.addAll(this.legalHeirDetails.getValues());
							if(legalHeirList != null && !legalHeirList.isEmpty()) {
								
								List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
								
								for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
									legalHeirList.add(legalHeirDTO);
								}
								
								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirDTOList(legalHeirList);
							}
						}
						else{
							bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirDTOList(null);
							bean.setHasError(true);
							SHAUtils.showAlertMessageBox("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
							return bean;
						}
						
						/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
						if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
								&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
						{
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
							
						}
						else{
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(null);
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(null);
							bean.setHasError(true);
						}

						if( (bean.getReimbursementQueryDto().getReimbursementDto().getNomineeName() == null && bean.getReimbursementQueryDto().getReimbursementDto().getNomineeAddr() == null))
						{
							SHAUtils.showAlertMessageBox("Please Enter Claimant / Legal Heir Details");
							bean.setHasError(true);
							return bean;
						}
						else{
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
							bean.setHasError(false);
						}*/	
					}					
				}
				
			} catch (CommitException e) {
				bean.setHasError(true);
				e.printStackTrace();
				
			}
			
			return bean;	
		}
		
		return bean;
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
					Date policyFromDate = bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate();
					Date policyToDate = bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate();
					if((bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
							|| bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
							|| bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
							|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode()))){

						policyFromDate = (bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() : bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
						policyToDate = (bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() : bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());
						
						if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode() != null && bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember() != null){
							policyFromDate = (bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() : bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
							policyToDate = (bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() : bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());		
						}
					}
					Long claimKey = bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getKey();
					//Long rodKey = bean.getReimbursementQueryDto().getReimbursementDto().getKey();
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
					}else if (!isRODDateCheck && (!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0))) {
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
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date.</b>", ContentMode.HTML));
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
	
	public void repaintNomineeTableValues(List<NomineeDetailsDto> savedNomineeDetailsList) {
		
		if(nomineeDetailsTable != null && savedNomineeDetailsList != null && !savedNomineeDetailsList.isEmpty()) {
			bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(savedNomineeDetailsList);
			nomineeDetailsTable.setTableList(savedNomineeDetailsList);
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
		}
	}
	
	public void clearObject(){
		if(draftQueryLetterDetailLayout != null){
			draftQueryLetterDetailLayout.removeAllComponents();
		}
		
		queryDetailsList = null;
		relationshipContainer = null;
		reimbursementQueryService = null;
		bean = null;
		
	}
}
