package com.shaic.paclaim.reimbursement.draftrejection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
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

public class DraftPARejectionLetterDetailPage extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PARejectionDetailsTable rejectionDetailsTable;
	
	@Inject
	private SearchDraftRejectionLetterTableDTO bean;
	
//	private BeanFieldGroup<SearchDraftRejectionLetterTableDTO> binder;
	
	private FieldGroup binder;
	
	private TextArea rejectionRemarksTxt;
	
	private TextArea rejectionLetterRemarksTxta;
	
//	private RichTextArea rejectionLetterRemarksTxta;
	
	private TextArea reDraftRejectionRemarksTxta;
	
	private HorizontalLayout buttonsLayout;
	
	private VerticalLayout draftRejectionLetterDetailLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
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
	
	public void initView(SearchDraftRejectionLetterTableDTO bean) {
		this.bean = bean;
		draftRejectionLetterDetailLayout = getContent();
		
		if(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())) {

			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
	}
	
	public void initBinder() {
//		this.binder = new BeanFieldGroup<SearchDraftRejectionLetterTableDTO>(
//				SearchDraftRejectionLetterTableDTO.class);
		
		
		this.binder = new FieldGroup();
		BeanItem<SearchDraftRejectionLetterTableDTO> item = new BeanItem<SearchDraftRejectionLetterTableDTO>(bean);
		
		item.addNestedProperty("reimbursementRejectionDto.rejectionRemarks");
		item.addNestedProperty("reimbursementRejectionDto.rejectionRemarks2");
		item.addNestedProperty("reimbursementRejectionDto.rejectionLetterRemarks");
		item.addNestedProperty("reimbursementRejectionDto.rejectionLetterRemarks2");
		item.addNestedProperty("reimbursementRejectionDto.redraftRemarks");
				
		binder.setItemDataSource(item);
			
	}
	
	public VerticalLayout getContent() {
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		return buildDraftRejectionDetailPageLayout();
	}
	
	private VerticalLayout buildDraftRejectionDetailPageLayout(){
		draftRejectionLetterDetailLayout = new VerticalLayout();
		
		
		FormLayout diseasedLayout = new FormLayout();
		if(((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
				|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			cmbPatientStatus = new ComboBox("Patient Status");
			cmbPatientStatus.setEnabled(false);
			
			BeanItemContainer<SelectValue> patientStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			patientStatusContainer.addBean(new SelectValue(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId(),"Deceased"));
			cmbPatientStatus.setContainerDataSource(patientStatusContainer);
			cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPatientStatus.setItemCaptionPropertyId("value");
			cmbPatientStatus.setValue(patientStatusContainer.getItemIds().get(0));
			
			diseasedLayout.addComponent(cmbPatientStatus);
			
			deathDate = new PopupDateField("Date Of Death");
			deathDate.setDateFormat("dd/MM/yyyy");
			deathDate.setValue(bean.getReimbursementRejectionDto().getReimbursementDto().getDateOfDeath() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getDateOfDeath() : bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getDeathDate());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementRejectionDto().getReimbursementDto().getDeathReason() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
		}
		draftRejectionLetterDetailLayout.addComponent(diseasedLayout);
		
		List<ReimbursementRejectionDetailsDto> rejectionDetailsList = this.bean.getRejectionList();
		rejectionDetailsTable.init("Rejection Details", false, false);
		if(rejectionDetailsList != null && !rejectionDetailsList.isEmpty())
		{
			rejectionDetailsTable.setTableList(rejectionDetailsList);
			rejectionDetailsTable.tableSelectHandler(rejectionDetailsList.get(0));
		}

		
		draftRejectionLetterDetailLayout.addComponent(rejectionDetailsTable);
		draftRejectionLetterDetailLayout.setSpacing(true);
				
		reDraftRejectionRemarksTxta = binder.buildAndBind("Redraft Rejection Remarks", "reimbursementRejectionDto.redraftRemarks", TextArea.class);
		reDraftRejectionRemarksTxta.setWidth("500px");
		reDraftRejectionRemarksTxta.setHeight("100px");
		reDraftRejectionRemarksTxta.setEnabled(false);
		
		rejectionRemarksTxt = binder.buildAndBind("Rejection Remarks", "reimbursementRejectionDto.rejectionRemarks", TextArea.class);
		rejectionRemarksTxt.setWidth("500px");
		rejectionRemarksTxt.setMaxLength(8000);
		rejectionRemarksTxt.setEnabled(false);
	
		rejectionLetterRemarksTxta = binder.buildAndBind("Rejection Letter Remarks", "reimbursementRejectionDto.rejectionLetterRemarks", TextArea.class);
		rejectionLetterRemarksTxta.setWidth("500px");
		rejectionLetterRemarksTxta.setHeight("200px");
		rejectionLetterRemarksTxta.setMaxLength(8000);
		rejectionLetterRemarksTxta.setNullRepresentation("");
		rejectionLetterRemarksTxta.setRequired(true);
		SHAUtils.handleTextAreaPopupDetails(rejectionLetterRemarksTxta,null,getUI(),SHAConstants.PA_REJECTION_REMARKS);

				
	
	FormLayout draftlayout = new FormLayout();
	
	if(this.bean.getReimbursementRejectionDto().getRedraftRemarks() != null){
		draftlayout.addComponents(rejectionRemarksTxt,reDraftRejectionRemarksTxta,rejectionLetterRemarksTxta);
	}
	else{
		draftlayout.addComponents(rejectionRemarksTxt,rejectionLetterRemarksTxta);
	}
	draftlayout.setSpacing(true);
	
	draftRejectionLetterDetailLayout.addComponent(draftlayout);
	draftRejectionLetterDetailLayout.setSpacing(true);
	

	if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
			&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
			&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
			|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
			|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
			&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {

		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		chkNomineeDeceased = null;
		if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null
				&& !bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()) { 
			nomineeDetailsTable.setTableList(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
			
			chkNomineeDeceased = new CheckBox("Nominee Deceased");
			if(bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeDeceasedFlag() != null && bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				chkNomineeDeceased.setValue(Boolean.TRUE);
			}
		}	
		
		draftRejectionLetterDetailLayout.addComponent(nomineeDetailsTable);
		
		if(chkNomineeDeceased != null){
			draftRejectionLetterDetailLayout.addComponent(chkNomineeDeceased);
			addNomineeDeceasedListener();
		}
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
		
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		relationshipContainer.addAll(bean.getPreAuthDto().getLegalHeirDto().getRelationshipContainer());
		Map<String,Object> refData = new HashMap<String, Object>();
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		
		PreauthDTO preauthDto = bean.getPreAuthDto();
		preauthDto.setClaimDTO(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto());
		preauthDto.setNewIntimationDTO(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto());
		preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId(),""));
		
		legalHeirDetails.init(preauthDto);
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		draftRejectionLetterDetailLayout.addComponent(legalHeirLayout);
		
		if(isNomineeDeceased()){
			enableLegalHeir = Boolean.TRUE;
			nomineeDetailsTable.setEnabled(false);
		}

		if(enableLegalHeir) {
			
			legalHeirDetails.addBeanToList(bean.getReimbursementRejectionDto().getReimbursementDto().getLegalHeirDTOList());
//			legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
			legalHeirDetails.getBtnAdd().setEnabled(true);
		}
		else {
			legalHeirDetails.deleteRows();
			legalHeirDetails.getBtnAdd().setEnabled(false);
		}
		
		/*FormLayout legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
	
		if(enableLegalHeir) {
			nomineeDetailsTable.setLegalHeirDetails(
			bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeName(),
			bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeAddr());
		}*/	
	
	}
	
	submitBtn = new Button("Submit");
	submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
	
	cancelBtn = new Button("Cancel");
	
	cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
	
	
	addListener();

	buttonsLayout = new HorizontalLayout(submitBtn,cancelBtn);
	
	buttonsLayout.setSpacing(true);
	
	draftRejectionLetterDetailLayout.addComponent(buttonsLayout);
	draftRejectionLetterDetailLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_CENTER);
	
	
	return draftRejectionLetterDetailLayout;
	}
	
	public void addListener(){
		submitBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				bean.setUsername(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				bean.setPassword(UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());				
				
				StringBuffer errMsg = new StringBuffer("");
				
				if(("").equalsIgnoreCase(rejectionLetterRemarksTxta.getValue())){
					errMsg.append("Please Enter a value for the Mandatory Field");
					showErrorMessage(errMsg.toString());	
				}
				
				if(bean.getReimbursementRejectionDto().getRedraftRemarks() != null){
					bean.getReimbursementRejectionDto().setRedraftRemarks(bean.getReimbursementRejectionDto().getRedraftRemarks());
				}	
				
//				if(rejectionLetterRemarksTxta.getValue() != null && rejectionLetterRemarksTxta.getValue().length() > 200){
//					showErrorMessage("Maximum of 200 Charactes only allowed for remarks");
//				}
				
				if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
						&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
						|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {

					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() && !isNomineeDeceased()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
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
							bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeSelectCount(selectCnt);
							if(selectCnt>0){
								bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
								bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(null);
								bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeAddr(null);
							}
							else{
								bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
								
								/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("MNAME").toString());
//								bean.getReimbursementQueryDto().getReimbursementDto().setLegalHeirMiddleName(legalHeirMap.get("LNAME").toString());
								bean.getReimbursementQueryDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());*/
								
								//TODO alert for selecting Nominee to be done
								errMsg.append("Please Select Nominee<br>");								
							}							
						}
					}
					else{
						bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(null);
						bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(null);
						
						if(legalHeirDetails.isValid()) {

							//added for support fix IMSSUPPOR-31323
							List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
							legalHeirList.addAll(legalHeirDetails.getValues());
							if(legalHeirList != null && !legalHeirList.isEmpty()) {
								
								List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
								
								for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
									legalHeirList.add(legalHeirDTO);
								}
								
								bean.getReimbursementRejectionDto().getReimbursementDto().setLegalHeirDTOList(legalHeirList);
							}
						}
						else{
							bean.getReimbursementRejectionDto().getReimbursementDto().setLegalHeirDTOList(null);
							errMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
						}
						
						/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
						if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
								&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
						{
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
							
						}
						else{
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeName(null);
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeAddr(null);
						}
						
						
						if( (bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeName() == null && bean.getReimbursementRejectionDto().getReimbursementDto().getNomineeAddr() == null))
						{
							errMsg += "Please Enter Claimant / Legal Heir Details<br>";							
						}
						else{
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
						}*/	
					}
					if(chkNomineeDeceased != null){
						bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeDeceasedFlag(chkNomineeDeceased.getValue() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
					}
				}
				
				if(!errMsg.toString().isEmpty()){
					showErrorMessage(errMsg.toString());
					return;
				}
				
				if(rejectionLetterRemarksTxta != null && rejectionLetterRemarksTxta.getValue() != null && rejectionLetterRemarksTxta.getValue().length() <= 8000){
					bean.getReimbursementRejectionDto().setRejectionLetterRemarks(rejectionLetterRemarksTxta.getValue());
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					bean.getReimbursementRejectionDto().setRedraftRemarks(null);
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					
					fireViewEvent(DraftPARejectionLetterDetailPresenter.SUBMIT_PA_REJECTION_LETTER,bean);  
				}
				else{
				/*	if(rejectionLetterRemarksTxta.getValue() != null && !rejectionLetterRemarksTxta.getValue().isEmpty()){
						
						
						String rejectRemarks1 = rejectionLetterRemarksTxta.getValue();
						String subString1 = "";
						String subString2 = "";
						System.out.println(String.format("Reject Remarks Length [%s]", rejectRemarks1.length()));
						if(rejectRemarks1.length() > 4000){
							 subString1 = rejectRemarks1.substring(0,4000);
							 subString2 = rejectRemarks1.substring(4000,8000);
							 
							 bean.getReimbursementRejectionDto().setRejectionLetterRemarks(subString1);
							 bean.getReimbursementRejectionDto().setRejectionLetterRemarks2(subString2);
							 String rejectionLetterRemarks = bean.getReimbursementRejectionDto().getRejectionLetterRemarks();
							 String rejectionLetterRemarks2 = bean.getReimbursementRejectionDto().getRejectionLetterRemarks2();
							 System.out.println(String.format("Reject Letter Remarks 1 [%s]", rejectionLetterRemarks.length()));
							 System.out.println(String.format("Reject Letter Remarks 2 [%s]", rejectionLetterRemarks2.length()));
						}
					}
					bean.getReimbursementRejectionDto().setRejectionLetterRemarks(rejectionLetterRemarksTxta.getValue());
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					bean.getReimbursementRejectionDto().setRedraftRemarks(null);
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					fireViewEvent(DraftPARejectionLetterDetailPresenter.SUBMIT_PA_REJECTION_LETTER,bean); */
					showErrorMessage("Maximum of 8000 Charactes only allowed for remarks");
				}
			}
		});
		
	cancelBtn.addClickListener(new Button.ClickListener() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			
			fireViewEvent(DraftPARejectionLetterDetailPresenter.CANCEL_DRAFT_PA_REJECTION_LETTER, null);
			
		}
	});
	
//	btnLetterBtn.addClickListener(new Button.ClickListener() {
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
//			}
//		});
	
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
							if(bean.getReimbursementRejectionDto().getReimbursementDto().getLegalHeirDTOList() != null 
									&& !bean.getReimbursementRejectionDto().getReimbursementDto().getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getReimbursementRejectionDto().getReimbursementDto().getLegalHeirDTOList());
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