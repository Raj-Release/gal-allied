package com.shaic.paclaim.reimbursement.draftquery;

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
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
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
import com.vaadin.v7.ui.CheckBox;
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
public class DraftPAQueryLetterDetailPage extends ViewComponent{
	
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
	private Instance<DraftPAQueryLetterDetailsTable> tableInstance;
	
	private DraftPAQueryLetterDetailsTable draftLetterRemarkstableInstanceObj;
	
	@Inject
	private Instance<ViewDraftPAQueryRemarksTable> viewDraftRemarksTableInstance;
	
	private ViewDraftPAQueryRemarksTable viewDraftRemarksTableObj;
	
	private TextArea reDraftRemarksTxta;
	
	private HorizontalLayout buttonsLayout;
	
	private VerticalLayout draftQueryLetterDetailLayout;
	
	private VerticalLayout draftLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private GWizard wizard;
	
	private List<ViewQueryDTO> queryDetailsList;
	
	private CheckBox generateDisVoucherChk;
	
	/*private TextField nomineeNameTxt;
	
	private TextArea nomineeAddrTxta;*/
	
//	private RichTextArea rQueryLetterRemarksTxta;

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
	
	private CheckBox chkNomineeDeceased;
	
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
	}
	
	public VerticalLayout getContent() {
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		return buildDecideOnQueryPageLayout();
	}
	
	private VerticalLayout buildDecideOnQueryPageLayout(){
		draftQueryLetterDetailLayout = new VerticalLayout();
		
		FormLayout diseasedLayout = new FormLayout();
		/*if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
				||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))*/
		
		if((((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
				||  ("Y").equalsIgnoreCase(this.bean.getReimbursementQueryDto().getQueryType()))
				||  (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId())))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
				&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			cmbPatientStatus = new ComboBox("Patient Status");
			cmbPatientStatus.setEnabled(false);
			
			BeanItemContainer<SelectValue> patientStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			if(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId() != null) {
				patientStatusContainer.addBean(new SelectValue(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId(),"Deceased"));
				cmbPatientStatus.setContainerDataSource(patientStatusContainer);
				cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPatientStatus.setItemCaptionPropertyId("value");
				cmbPatientStatus.setValue(patientStatusContainer.getItemIds().get(0));
			}	
			
			diseasedLayout.addComponent(cmbPatientStatus);
			
			deathDate = new PopupDateField("Date Of Death");
			deathDate.setDateFormat("dd/MM/yyyy");
			deathDate.setValue(bean.getReimbursementQueryDto().getReimbursementDto().getDateOfDeath() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getDateOfDeath() : bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getDeathDate());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementQueryDto().getReimbursementDto().getDeathReason() != null ? bean.getReimbursementQueryDto().getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
		}
		draftQueryLetterDetailLayout.addComponent(diseasedLayout);
		
		
		draftLayout = new VerticalLayout();
		draftLayout.setSpacing(false);
		
		List<ViewQueryDTO> queryDetailsList = this.bean.getQueryDetailsList();
		queryDetailsTableObj.init("Query Details", false, false);
//		queryDetailsTableObj.setQueryDetaqilsVisibleColumns();
		queryDetailsTableObj.setViewPAQueryDetialsColumn();
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
			
			DraftQueryLetterDetailTableDto draftDetailDto = new DraftQueryLetterDetailTableDto();
			draftDetailDto.setSno(1);
			draftDetailDto.setDraftOrRedraftRemarks("Please return the discharge voucher duly stamped, signed by you and duly notarized.");
			draftDetailDto.setProcessType("D");
			draftList.add(draftDetailDto);
			
			for(int k = 1; k<5; k++){			
				draftDetailDto = new DraftQueryLetterDetailTableDto();
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
		
//		if((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue()) ||  ("Y").equalsIgnoreCase(this.bean.getReimbursementQueryDto().getQueryType())){
		if(("Y").equalsIgnoreCase(this.bean.getReimbursementQueryDto().getQueryType())){
		generateDisVoucherChk = binder.buildAndBind("Generate Discharge Voucher","generateDisVoucher",CheckBox.class);
		generateDisVoucherChk.setData(this.bean);
		
		generateDisVoucherChk.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SearchDraftQueryLetterTableDTO searchTableDto = (SearchDraftQueryLetterTableDTO)generateDisVoucherChk.getData();
				searchTableDto.getReimbursementQueryDto().setGenerateDisVoucher(generateDisVoucherChk.getValue());
			}
		});
		draftLayout.addComponent(generateDisVoucherChk);
		
		/*if(("Y").equalsIgnoreCase(this.bean.getReimbursementQueryDto().getQueryType())){
		
			nomineeNameTxt = new TextField("Nominee Name");
			String nomineeName = this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName() != null ? (this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName()) : "";
			nomineeNameTxt.setValue(nomineeName);
			nomineeAddrTxta = new TextArea("Address");		
			nomineeAddrTxta.setValue(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeAddr() != null ? (this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeAddr()) : "");
			FormLayout nomineeFrmLayout = new FormLayout(nomineeNameTxt,nomineeAddrTxta);
			draftLayout.addComponent(nomineeFrmLayout);
		}*/
		}
		
	FormLayout draftFrmlayout = new FormLayout(redrafLayout,queryRemarksTxt,draftLayout);
	
	draftQueryLetterDetailLayout.addComponent(draftFrmlayout);
	draftQueryLetterDetailLayout.setSpacing(true);
	
	if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
			&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
			&& (((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
			||  (this.bean.getReimbursementQueryDto().getReimbursementDto().getBenefitSelected() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(this.bean.getReimbursementQueryDto().getReimbursementDto().getBenefitSelected())))
			||  (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
					||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId())))
			&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
			&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
			&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
/*	if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
			||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))
			&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {*/

		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		chkNomineeDeceased = null;
		if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null
				&& !bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()) {
			nomineeDetailsTable.setTableList(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
			chkNomineeDeceased = new CheckBox("Nominee Deceased");
			if(bean.getReimbursementQueryDto().getReimbursementDto().getNomineeDeceasedFlag() != null && bean.getReimbursementQueryDto().getReimbursementDto().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				chkNomineeDeceased.setValue(Boolean.TRUE);
			}
		}
		
		draftQueryLetterDetailLayout.addComponent(nomineeDetailsTable);
		
		if(chkNomineeDeceased != null){
			draftQueryLetterDetailLayout.addComponent(chkNomineeDeceased);
			addNomineeDeceasedListener();
		}
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
				
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		relationshipContainer.addAll(bean.getPreAuthDto().getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		
		PreauthDTO preauthDto = bean.getPreAuthDto();
		preauthDto.setClaimDTO(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto());
		preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId(),""));
		preauthDto.setNewIntimationDTO(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto());
		
		legalHeirDetails.init(preauthDto);
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		draftQueryLetterDetailLayout.addComponent(legalHeirLayout);
		
		if(isNomineeDeceased()){
			enableLegalHeir = Boolean.TRUE;
			nomineeDetailsTable.setEnabled(false);
		}

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

//	generateDisVoucherChk = new CheckBox("Generate Discharge Voucher");
	

	addListener();
	
	buttonsLayout = new HorizontalLayout(submitBtn);

	draftQueryLetterDetailLayout.addComponent(buttonsLayout);
	draftQueryLetterDetailLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_CENTER);
	
	
	return draftQueryLetterDetailLayout;
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
			
			fireViewEvent(DraftPAQueryLetterDetailPresenter.SUBMIT_PA_BUTTON_CLICKED,bean);
		
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
				
					fireViewEvent(DraftPAQueryLetterDetailPresenter.SAVE_DRAFT_PA_QUERY,bean);
				}	
			}
		});
		
		/*if(nomineeAddrTxta != null){
			nomineeAddrTxta.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeAddr(nomineeAddrTxta.getValue());
					
				}
			});
		}*/
	}
	
	public SearchDraftQueryLetterTableDTO getupdatedBean(){
		
		String	eMsg = "";
		
		if(!binder.isValid()){
			
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
				eMsg += errMsg.getFormattedHtmlMessage();
				}
				
			}
			showErrorMessage(eMsg);
			bean.setHasError(true);
		}
		else{
			try {
				binder.commit();
				List<DraftQueryLetterDetailTableDto> draftRemarksList = draftLetterRemarkstableInstanceObj
						.getValues();
				/*if(nomineeNameTxt != null && nomineeNameTxt.getValue() != null){
//					bean.getClaimDto().getNewIntimationDto().setNomineeName(nomineeNameTxt.getValue());
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(nomineeNameTxt.getValue());
				}
				
				if(nomineeAddrTxta != null && nomineeAddrTxta.getValue() != null){
//					bean.getClaimDto().getNewIntimationDto().setNomineeAddr(nomineeAddrTxta.getValue());
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeAddr(nomineeAddrTxta.getValue());
				}*/				
				
				if(queryLetterRemarksTxta.getValue() != null ){
					bean.getReimbursementQueryDto().setQueryLetterRemarks(queryLetterRemarksTxta.getValue());
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
					
					bean.setHasError(false);
				} else {
					eMsg = "Please Provide Atleast One Draft Letter Remarks to Submit with a Maximum Of 500 Characters";
					showErrorMessage(eMsg);
					bean.setHasError(true);
					return bean;			
				}
				
				if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
						&& (((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
						||  (this.bean.getReimbursementQueryDto().getReimbursementDto().getBenefitSelected() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(this.bean.getReimbursementQueryDto().getReimbursementDto().getBenefitSelected())))
						||  (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
								||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId())))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
					
				/*if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {*/
					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()
							&& !isNomineeDeceased()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
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
						
						if(legalHeirDetails.isValid()) {

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
							SHAUtils.showAlertMessageBox("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
							bean.setHasError(true);
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
					if(chkNomineeDeceased != null){
						bean.getReimbursementQueryDto().getReimbursementDto().setNomineeDeceasedFlag(chkNomineeDeceased.getValue() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
					}
				}
				else {
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
					bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(null);
					bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(null);
					bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(null);
				}

			} catch (CommitException e) {
				bean.setHasError(true);
				e.printStackTrace();
				
			}
			
			return bean;	
		}
		
		return bean;
	}	
	
	public void repaintNomineeTableValues(ReimbursementQueryDto queryDto/*List<NomineeDetailsDto> savedNomineeDetailsList*/) {
		
		List<NomineeDetailsDto> savedNomineeDetailsList = queryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList();
		List<LegalHeirDTO> legalHeirSaveList = queryDto.getReimbursementDto().getLegalHeirDTOList();
		
		if(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& (((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
				||  ("Y").equalsIgnoreCase(this.bean.getReimbursementQueryDto().getQueryType()))
				||  (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getPatientStatusId())))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
				&& bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					
				if(savedNomineeDetailsList != null && !savedNomineeDetailsList.isEmpty()) {
					if(nomineeDetailsTable != null && savedNomineeDetailsList != null && !savedNomineeDetailsList.isEmpty()) {
						bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(savedNomineeDetailsList);
						nomineeDetailsTable.setTableList(savedNomineeDetailsList);
						nomineeDetailsTable.setViewColumnDetails();
						nomineeDetailsTable.generateSelectColumn();
					}
				}	
				else if((savedNomineeDetailsList == null || savedNomineeDetailsList.isEmpty()) && legalHeirSaveList != null && !legalHeirSaveList.isEmpty()) {
					
					boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true;
					if(enableLegalHeir && legalHeirDetails != null) {
						legalHeirDetails.deleteRows();
						legalHeirDetails.addBeanToList(bean.getReimbursementQueryDto().getReimbursementDto().getLegalHeirDTOList());
//						legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
						legalHeirDetails.getBtnAdd().setEnabled(true);
					}
				}
				if(queryDto.getReimbursementDto().getNomineeDeceasedFlag() != null && queryDto.getReimbursementDto().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					if(chkNomineeDeceased != null){
						chkNomineeDeceased.setValue(Boolean.TRUE);
					}
				}
		}	
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
							if(bean.getReimbursementQueryDto().getReimbursementDto().getLegalHeirDTOList() != null 
									&& !bean.getReimbursementQueryDto().getReimbursementDto().getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getReimbursementQueryDto().getReimbursementDto().getLegalHeirDTOList());
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
