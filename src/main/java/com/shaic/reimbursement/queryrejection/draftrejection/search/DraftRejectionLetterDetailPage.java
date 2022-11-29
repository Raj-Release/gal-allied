package com.shaic.reimbursement.queryrejection.draftrejection.search;

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

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.RejectionDetailsTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.DecideOnRejectionPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
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

public class DraftRejectionLetterDetailPage extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RejectionDetailsTable rejectionDetailsTable;
	
	@Inject
	private SearchDraftRejectionLetterTableDTO bean;
	
//	private BeanFieldGroup<SearchDraftRejectionLetterTableDTO> binder;
	
	private FieldGroup binder;
	
	BeanItemContainer<SelectValue> reconsiderCaseMaster;
	
	BeanItemContainer<SelectValue> reconsiderReasonMaster;
	
	private TextArea rejectionRemarksTxt;
	
	private TextArea rejectionLetterRemarksTxta;
	
	private ComboBox cmbrejectCategory;
	
	private ComboBox cmbRejSubCategory;
	
	private ComboBox cmbReconsideration;
	
	private ComboBox cmbReconsidReason;
	
//	private RichTextArea rejectionLetterRemarksTxta;
	
	private TextArea reDraftRejectionRemarksTxta;
	
	private HorizontalLayout buttonsLayout;
	
	private VerticalLayout draftRejectionLetterDetailLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private BeanItemContainer<SelectValue> rejectionCategContainer;
	
	private BeanItemContainer<SelectValue> rejSubcategContainer;
	
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

	@PostConstruct
	public void init() {

	}
	
	public void initView(SearchDraftRejectionLetterTableDTO bean) {
		this.bean = bean;
		rejectionCategContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(bean.getRejectCategList() != null && !bean.getRejectCategList().isEmpty()){
			rejectionCategContainer.addAll(bean.getRejectCategList());
		}
		
		rejSubcategContainer  = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		draftRejectionLetterDetailLayout = getContent();
		
		if(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}	
	}
	
	public void initBinder() {
//		this.binder = new BeanFieldGroup<SearchDraftRejectionLetterTableDTO>(
//				SearchDraftRejectionLetterTableDTO.class);
		
		
		this.binder = new FieldGroup();
		BeanItem<SearchDraftRejectionLetterTableDTO> item = new BeanItem<SearchDraftRejectionLetterTableDTO>(bean);
		
		item.addNestedProperty("reimbursementRejectionDto.rejectionRemarks");
		item.addNestedProperty("reimbursementRejectionDto.rejectionLetterRemarks");
		item.addNestedProperty("reimbursementRejectionDto.redraftRemarks");
		item.addNestedProperty("reimbursementRejectionDto.rejCategSelectValue");
		item.addNestedProperty("reimbursementRejectionDto.rejSubCategSelectValue");
		binder.setItemDataSource(item);
		
		reconsiderCaseMaster = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderCaseMaster.addBean(bean.getReconsiderCase());
		
		reconsiderReasonMaster = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderReasonMaster.addBean(bean.getReconsiderReason());
			
	}
	
	public VerticalLayout getContent() {
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		return buildDraftRejectionDetailPageLayout();
	}
	
	private VerticalLayout buildDraftRejectionDetailPageLayout(){
		draftRejectionLetterDetailLayout = new VerticalLayout();
		
		List<ReimbursementRejectionDetailsDto> rejectionDetailsList = this.bean.getRejectionList();
		rejectionDetailsTable.init("Rejection Details", false, false);
		if(rejectionDetailsList != null && !rejectionDetailsList.isEmpty())
		{
			rejectionDetailsTable.setTableList(rejectionDetailsList);
			rejectionDetailsTable.tableSelectHandler(rejectionDetailsList.get(0));
		}

		
		draftRejectionLetterDetailLayout.addComponent(rejectionDetailsTable);
		draftRejectionLetterDetailLayout.setSpacing(true);

		
		cmbrejectCategory = binder.buildAndBind("Rejection Category", "reimbursementRejectionDto.rejCategSelectValue", ComboBox.class);
		cmbrejectCategory.setWidth("300px");
		
		cmbrejectCategory.setContainerDataSource(rejectionCategContainer);
		cmbrejectCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbrejectCategory.setItemCaptionPropertyId("value");
		
//		if(bean.getReimbursementRejectionDto().getRejCategSelectValue() != null){
//			cmbrejectCategory.setValue(bean.getReimbursementRejectionDto().getRejCategSelectValue());	
//		}

		cmbRejSubCategory = binder.buildAndBind("Rejection sub category", "reimbursementRejectionDto.rejSubCategSelectValue", ComboBox.class);
		cmbRejSubCategory.setWidth("300px");
		
		
		if(bean.getReimbursementRejectionDto().getRejCategSelectValue() != null){

			fireViewEvent(DraftRejectionLetterDetailPresenter.REJECT_SUB_CATEG_LAYOUT_DRAFT_REJ, bean.getReimbursementRejectionDto().getRejCategSelectValue().getId());
			
			if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(bean.getReimbursementRejectionDto().getRejCategSelectValue().getId())) {
					
				cmbRejSubCategory.setVisible(true);
			}
			else {
				cmbRejSubCategory.setVisible(false);
			}
			for(int i = 0; i<rejectionCategContainer.size();i++){
				if(rejectionCategContainer.getIdByIndex(i).getValue().equalsIgnoreCase(bean.getReimbursementRejectionDto().getRejCategSelectValue().getValue()))
				{	
					cmbrejectCategory.setValue(rejectionCategContainer.getIdByIndex(i));
					break;
				}
			}
		}
		else {
			cmbRejSubCategory.setVisible(false);
		}
		
		if(bean.getReimbursementRejectionDto().getRejSubCategSelectValue() != null){
			
			for(int i = 0; i < rejSubcategContainer.size();i++){
				if(rejSubcategContainer.getIdByIndex(i).getId().equals(bean.getReimbursementRejectionDto().getRejSubCategSelectValue().getId()))
				{	
					cmbRejSubCategory.setValue(rejSubcategContainer.getIdByIndex(i));
					break;
				}
			}				
		}
		
				
		draftRejectionLetterDetailLayout.addComponents(cmbrejectCategory, cmbRejSubCategory);
		
		FormLayout admissionLayout = new FormLayout();
		admissionDate = new  PopupDateField("Date of Admission");
		admissionDate.setValue(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getAdmissionDate());
		admissionDate.setDateFormat("dd/MM/yyyy");
		admissionLayout.addComponent(admissionDate);
		
		FormLayout dischargeLayout = new FormLayout();
		dischargeDate = new  PopupDateField("Date of Discharge");
		dischargeDate.setValue(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getDischargeDate());
		dischargeDate.setDateFormat("dd/MM/yyyy");
		dischargeLayout.addComponent(dischargeDate);
		HorizontalLayout dateLayout = new HorizontalLayout();
		
		dateLayout.addComponent(admissionLayout);
		dateLayout.addComponent(dischargeLayout);
		
		FormLayout diseasedLayout = new FormLayout();
		if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
				||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
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
			deathDate.setValue(bean.getReimbursementRejectionDto().getReimbursementDto().getDateOfDeath());
			deathDate.setEnabled(false);
			
			diseasedLayout.addComponent(deathDate);
			
			txtReasonForDeath = new TextField("Reason For Death");
			txtReasonForDeath.setValue(bean.getReimbursementRejectionDto().getReimbursementDto().getDeathReason() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getDeathReason() : "");
			txtReasonForDeath.setEnabled(false);
			
			diseasedLayout.addComponent(txtReasonForDeath);
			
			dateLayout.addComponent(diseasedLayout);
		}
				
		dateLayout.setMargin(false);
		dateLayout.setWidth("100%");
		dateLayout.setHeight("40px");
		
		
		draftRejectionLetterDetailLayout.addComponent(dateLayout);
		
		
		cmbReconsideration = binder.buildAndBind("Reconsideration Case", "reconsiderCase", ComboBox.class);
				
		cmbReconsideration.setContainerDataSource(reconsiderCaseMaster);
		cmbReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsideration.setItemCaptionPropertyId("value");
		cmbReconsideration.setEnabled(false);
		cmbReconsideration.setValue(bean.getReconsiderCase());
		
		cmbReconsidReason = binder.buildAndBind("Reason for Reconsideration", "reconsiderReason", ComboBox.class);
		cmbReconsidReason.setWidth("295px");
		cmbReconsidReason.setContainerDataSource(reconsiderReasonMaster);
		cmbReconsidReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsidReason.setItemCaptionPropertyId("value");
		cmbReconsidReason.setEnabled(false);		
		cmbReconsidReason.setValue(bean.getReconsiderReason());
	
		HorizontalLayout reconsiderLayout = new HorizontalLayout(new FormLayout(cmbReconsideration), new FormLayout(cmbReconsidReason));
		
		draftRejectionLetterDetailLayout.addComponent(reconsiderLayout);
		
		reDraftRejectionRemarksTxta = binder.buildAndBind("Redraft Rejection Remarks", "reimbursementRejectionDto.redraftRemarks", TextArea.class);
		reDraftRejectionRemarksTxta.setWidth("500px");
		reDraftRejectionRemarksTxta.setHeight("100px");
		//reDraftRejectionRemarksTxta.setEnabled(false);
		reDraftRejectionRemarksTxta.setReadOnly(true);
		reDraftRejectionRemarksTxta.setId("autoRedraftRem");
		reDraftRejectionRemarksTxta.setData(bean);
		reDraftRejectionRemarksTxta.setDescription("Click the Text Box and Press F8 For Detail Popup");
		handleRejLetterRemarksPopup(reDraftRejectionRemarksTxta,null);
		
		rejectionRemarksTxt = binder.buildAndBind("Rejection Remarks", "reimbursementRejectionDto.rejectionRemarks", TextArea.class);
		rejectionRemarksTxt.setWidth("500px");
		rejectionRemarksTxt.setMaxLength(8000);
		//rejectionRemarksTxt.setEnabled(false);
		//IMSSUPPOR-9896
		rejectionRemarksTxt.setReadOnly(true);
		rejectionRemarksTxt.setId("autoRejRem");
		rejectionRemarksTxt.setData(bean);
		rejectionRemarksTxt.setDescription("Click the Text Box and Press F8 For Detail Popup");
		handleRejLetterRemarksPopup(rejectionRemarksTxt,null);
		rejectionLetterRemarksTxta = binder.buildAndBind("Rejection Letter Remarks", "reimbursementRejectionDto.rejectionLetterRemarks", TextArea.class);
		rejectionLetterRemarksTxta.setWidth("500px");
		rejectionLetterRemarksTxta.setHeight("200px");
		rejectionLetterRemarksTxta.setMaxLength(4000);
		rejectionLetterRemarksTxta.setNullRepresentation("");
		rejectionLetterRemarksTxta.setRequired(true);
		rejectionLetterRemarksTxta.setId("rejLetRem");
		rejectionLetterRemarksTxta.setData(bean);
		rejectionLetterRemarksTxta.setDescription("Click the Text Box and Press F8 For Detail Popup");
		handleRejLetterRemarksPopup(rejectionLetterRemarksTxta,null);
				
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
	
	if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
			||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
			&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
			&& bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
			&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		
		if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null) {
			nomineeDetailsTable.setTableList(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
		}
		
		draftRejectionLetterDetailLayout.addComponent(nomineeDetailsTable);
	
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
		preauthDto.getPreauthDataExtractionDetails().setPatientStatus(new SelectValue(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId(),""));
		preauthDto.setNewIntimationDTO(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto());
		
		legalHeirDetails.init(preauthDto);
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		draftRejectionLetterDetailLayout.addComponent(legalHeirLayout);

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
		
		draftRejectionLetterDetailLayout.addComponent(legalHeirLayout);	
	
	}
	
	submitBtn = new Button("Submit");
	submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
	
	cancelBtn = new Button("Cancel");
	
	cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
	
	
	addListener();
	addDateListener();

	buttonsLayout = new HorizontalLayout(submitBtn,cancelBtn);
	
	buttonsLayout.setSpacing(true);
	
	draftRejectionLetterDetailLayout.addComponent(buttonsLayout);
	draftRejectionLetterDetailLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_CENTER);
	
	
	return draftRejectionLetterDetailLayout;
	}
	
public  void handleRejLetterRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRejectionLetterRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRejectionLetter(searchField, getShortCutListenerForRejectionLetterRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForRejectionLetter(final TextArea textField, final ShortcutListener shortcutListener) {
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
	private ShortcutListener getShortCutListenerForRejectionLetterRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("RejectionLetter Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				SearchDraftRejectionLetterTableDTO  searchTableDto = (SearchDraftRejectionLetterTableDTO) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				
				if(("rejLetRem").equalsIgnoreCase(txtFld.getId())){
					
					txtArea.setRows(25);
					txtArea.setReadOnly(false);
				}
				else{
					if(("autoRejRem").equalsIgnoreCase(txtFld.getId()) && searchTableDto.getReimbursementRejectionDto().getRejectionRemarks() != null){
						txtArea.setRows(searchTableDto.getReimbursementRejectionDto().getRejectionRemarks().length()/80 >= 25 ? 25 : ((searchTableDto.getReimbursementRejectionDto().getRejectionRemarks().length()/80)%25)+1);
					}
					if(("autoRedraftRem").equalsIgnoreCase(txtFld.getId()) && searchTableDto.getReimbursementRejectionDto().getRedraftRemarks() != null){
						txtArea.setRows(searchTableDto.getReimbursementRejectionDto().getRedraftRemarks().length()/80 >= 25 ? 25 : ((searchTableDto.getReimbursementRejectionDto().getRedraftRemarks().length()/80)%25)+1);
					}
					txtArea.setReadOnly(true);
				}
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("rejLetRem").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							SearchDraftRejectionLetterTableDTO mainDto = (SearchDraftRejectionLetterTableDTO)txtFld.getData();
							mainDto.getReimbursementRejectionDto().setRejectionLetterRemarks(txtFld.getValue());
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
				
				if(("autoRejRem").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Rejection Remarks";
				}
			    else if(("autoRedraftRem").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Redraft Remarks";
			    }
			    else if(("rejLetRem").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "RejectionLetter Remarks";
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
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	public void addListener(){
		
		cmbrejectCategory.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;

				if(value != null) {
					
					fireViewEvent(DraftRejectionLetterDetailPresenter.REJECT_SUB_CATEG_LAYOUT_DRAFT_REJ, value.getId());

					if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
						cmbRejSubCategory.setVisible(true);
						cmbRejSubCategory.setRequired(true);
					}
					else {
						cmbRejSubCategory.setVisible(false);
						cmbRejSubCategory.setRequired(false);
					}
				}
			}
		});

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
				
				if(cmbrejectCategory.getValue() == null || (cmbrejectCategory.getValue() != null && ((SelectValue)cmbrejectCategory.getValue()).getValue().isEmpty())){
					errMsg.append("Please Select a value for Rejection Category Field.<br>");
				}
				
				SelectValue value = cmbrejectCategory.getValue() != null ? (SelectValue) cmbrejectCategory.getValue() : null;

				if(value != null) {
					
					if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
					
						if(cmbRejSubCategory != null && cmbRejSubCategory.getValue() == null ){

                            errMsg.append("Please Select a value for Rejection Sub category Field.<br>");
						}
						else {
							bean.getReimbursementRejectionDto().setRejSubCategSelectValue((SelectValue)cmbRejSubCategory.getValue());
						}
					}
					bean.getReimbursementRejectionDto().setRejCategSelectValue(value);	
				}
				
				if(("").equalsIgnoreCase(rejectionLetterRemarksTxta.getValue())){
					errMsg.append("Please Enter a value for the Rejection Letter Remarks Field<br>");	
				}
				
				if((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
						||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())
						&& bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(tableList);
							StringBuffer nomineeNames = new StringBuffer("");
							int selectCnt = 0;
							for (NomineeDetailsDto nomineeDetailsDto : tableList) {
								nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
								if(nomineeDetailsDto.isSelectedNominee()) {
									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
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
				}
				
				
				if(bean.getReimbursementRejectionDto().getRedraftRemarks() != null){
					bean.getReimbursementRejectionDto().setRedraftRemarks(bean.getReimbursementRejectionDto().getRedraftRemarks());
				}	
				
				if(admissionDate.getValue() == null || dischargeDate.getValue() == null){
					errMsg.append("Date of Admission / Date of Discharge can't be empty. Please fill the details.<br>");
				}
				
				if(!errMsg.toString().isEmpty()){
					showErrorMessage(errMsg.toString());
					return;
				}
				
				if(rejectionLetterRemarksTxta != null && rejectionLetterRemarksTxta.getValue() != null && rejectionLetterRemarksTxta.getValue().length() <= 4000){
					bean.getReimbursementRejectionDto().setRejectionLetterRemarks(rejectionLetterRemarksTxta.getValue());
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					bean.getReimbursementRejectionDto().setRedraftRemarks(null);
					bean.getReimbursementRejectionDto().setRejectionRemarks(null);
					bean.getReimbursementRejectionDto().setRejCategSelectValue((SelectValue)cmbrejectCategory.getValue());
					bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().setAdmissionDate(admissionDate.getValue());
					bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().setDischargeDate(dischargeDate.getValue());
					
					fireViewEvent(DraftRejectionLetterDetailPresenter.SUBMIT_REJECTION_LETTER,bean);  
				}
				else{
					showErrorMessage("Maximum of 4000 Charactes only allowed for remarks");
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
			
			
			fireViewEvent(DraftRejectionLetterDetailPresenter.CANCEL_DRAFT_REJECTION_LETTER, null);
			
		}
	});
	
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
	
	public void addDateListener(){
		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();

				if(enteredDate != null) {
					Date policyFromDate = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate();
					Date policyToDate = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate();
					
					if((bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
							|| bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
							|| bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
							|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode()))){

						policyFromDate = (bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() : bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
						policyToDate = (bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() : bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());
						if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode() != null && bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember() != null){
							policyFromDate = (bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() : bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate());
							policyToDate = (bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() != null ? bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() : bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate());		
						}
					}
					Long claimKey = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getKey();
					//Long rodKey = bean.getReimbursementRejectionDto().getReimbursementDto().getKey();
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
					}else if (!isRODDateCheck && !(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0)) {
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
		
		this.rejSubcategContainer = rejSubcategContainer;
		cmbRejSubCategory.setVisible(true);
		cmbRejSubCategory.setContainerDataSource(this.rejSubcategContainer);
		cmbRejSubCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRejSubCategory.setItemCaptionPropertyId("value");
		
		
		if(bean.getReimbursementRejectionDto().getRejSubCategSelectValue() != null){
			cmbRejSubCategory.setValue(bean.getReimbursementRejectionDto().getRejSubCategSelectValue());
		}

	}
	
	public void clearObject(){
		if(draftRejectionLetterDetailLayout != null){
			draftRejectionLetterDetailLayout.removeAllComponents();
		}
		rejSubcategContainer = null;
		rejectionCategContainer = null;
		reimbursementQueryService = null;
		reconsiderCaseMaster = null;
		reconsiderReasonMaster = null;
		bean = null;
	}
}
