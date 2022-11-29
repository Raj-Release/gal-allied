package com.shaic.paclaim.cashless.processdownsize.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.paclaim.cashless.listenertables.PARevisedMedicalDecisionTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
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
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PADownsizePreauthRequestDataExtractionPage extends ViewComponent implements
WizardStep<PreauthDTO> {
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> preauthPreviousDetailsPage;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private PreauthDTO preauthDto;
	
	@Inject
	private PreviousPreAuthService previousPreAuthService;
	
	private GWizard wizard;
	
	private PreviousPreAuthDetailsTable objPreviousPreAuthDetailsTable;
	
	@Inject
	private Instance<PAAmountConsideredTable> amountConsideredTableInstance;

	private PAAmountConsideredTable amountConsideredTable;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	@Inject
	private Instance<PARevisedMedicalDecisionTable> newMedicalDecisionTableInstance;
	
	private PARevisedMedicalDecisionTable newMedicalDecisionTableObj;
	
	private List<MedicalDecisionTableDTO> medicalDecisionTableList;
	
	private List<PreviousPreAuthTableDTO> previousPreauthTableList;
	
	Map<String, Object> sublimitCalculatedValues;
	
	private String diagnosisName;
	
	private TextField downSizedAmt;
	
	private TextField medicalRemarks;
	
	private TextArea downsizeRemarks;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ComboBox escalateTo;
	
	private Upload fileUpload;
	
	private TextArea escalateRemarks;
	
	private ComboBox reasonForDownSize;
	
	private ComboBox cmbSpecialistType;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private BeanItemContainer<SelectValue> escalateContainer;
	
	private TextField doctorNote;
	
	private Button intiatePEDEndorsementButton;
	
	private VerticalLayout wholeVLayout;
	
	private Button downsizePreauthBtn;
	
	private FormLayout downSizeFormLayout;
	
	private Button showclaimAmtDetailsBtn;

	private Button showclaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private Button esclateClaimBtn;
	
	private Boolean isEsclateClaim=false;
	
	Map<String, Object> referenceData;
	
	private TextField approvedAmtField;
	
	private TextField totalApprovedAmt;
	
	private TextField preauthApprovedAmtTxt;
	
	private TextField consideredAmtTxt;
	
	private TextField nonAllopathicTxt;
	
	private VerticalLayout optionCLayout;
	
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private Double amount=0.0;

	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	private Button tmpViewBtn;
	
	private FormLayout escalateForm;
	
	@EJB
	private MasterService masterService;
	
	private File file;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PreauthService preauthService;
	
	
	@PostConstruct
	public void init() {

	}
//	public void init(PreauthDTO bean, GWizard wizard) {
//		this.bean = bean;
//		this.wizard = wizard;
//	}
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		this.selectValueContainer = this.bean.getDownSizePreauthDataExtractionDetails().getDownsizeReason();
		this.escalateContainer = this.bean.getDownSizePreauthDataExtractionDetails().getEscalateTo();
	}
	
	public void init(PreauthDTO bean,GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		this.selectValueContainer = this.bean.getDownSizePreauthDataExtractionDetails().getDownsizeReason();
		this.escalateContainer = this.bean.getDownSizePreauthDataExtractionDetails().getEscalateTo();
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@Override
	public String getCaption() {
		return "Downsize Pre-auth";
	}
	
	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		intiatePEDEndorsementButton = new Button("Initiate PED Endorsement");
		HorizontalLayout buttonHLayout = new HorizontalLayout(intiatePEDEndorsementButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(intiatePEDEndorsementButton, Alignment.MIDDLE_RIGHT);
		
		consideredAmtTxt = new TextField("Amount Considered");
		consideredAmtTxt.setReadOnly(false);
		consideredAmtTxt.setValue(this.bean.getAmountConsidered());
		consideredAmtTxt.setReadOnly(true);
		
		FormLayout consideredAmtForm = new FormLayout(consideredAmtTxt);
		consideredAmtForm.setMargin(false);
		
		//For intiate ped endorsement button.
		
		showclaimAmtDetailsBtn = new Button("View");
		showclaimAmtDetailsBtnDuplicate = new Button("View");
		showclaimAmtDetailsBtn.setStyleName("link");
		showclaimAmtDetailsBtnDuplicate.setStyleName("link");
		amountConsideredViewButton = new Button("View");
		amountConsideredViewButton.setStyleName("link");
		
		objPreviousPreAuthDetailsTable = preauthPreviousDetailsPage.get();
		objPreviousPreAuthDetailsTable.init("Pre-auth Summary", false, false);
		objPreviousPreAuthDetailsTable.setTableList(this.bean.getPreviousPreauthTableDTO());	
		
		this.newMedicalDecisionTableObj=this.newMedicalDecisionTableInstance.get();
		this.newMedicalDecisionTableObj.init(this.bean);
		
		approvedAmtField = new TextField("");
		
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction Amount",
				ContentMode.HTML), approvedAmtField);
		
		optionCLayout = new VerticalLayout(approvedFormLayout);

		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showclaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showclaimAmtDetailsBtn);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);
		
		setValueToMedicalDecisionValues();
		
		MedicalDecisionTableDTO dto = new MedicalDecisionTableDTO();
		dto.setReferenceNo("Residual Treatment / Procedure Amount");
		dto.setApprovedAmount(this.preauthDto.getResidualAmountDTO().getApprovedAmount() != null ? this.preauthDto.getResidualAmountDTO().getApprovedAmount().toString() : "");
		downSizeFormLayout=new FormLayout();
		
		wholeVLayout = new VerticalLayout(consideredAmtForm,buttonHLayout, this.newMedicalDecisionTableObj,this.amountConsideredTable,buildDownSizePreAuthButtonLayout(),downSizeFormLayout);
		wholeVLayout.setComponentAlignment(buttonHLayout, Alignment.TOP_RIGHT);
		wholeVLayout.setComponentAlignment(consideredAmtForm, Alignment.TOP_LEFT);
		wholeVLayout.setSpacing(true);
		
		if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			downSizeFormLayout.removeAllComponents();
			downSizeFormLayout.addComponent(downSizeFormLayout());
		}
//		else if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
//			downSizeFormLayout.removeAllComponents();
//			downSizeFormLayout.addComponent(escalateFormLayout());
//		}
		
		addMedicalDecisionTableFooterListener();
		return wholeVLayout;
		
	}
	
	public void downSizedAmount(Double amount){
		this.amount=this.amount+amount;
//		this.bean.setDownSizedAmount(this.amount);
		downSizedAmt.setValue("" + this.amount);
	}

	
	
	public HorizontalLayout buildDownSizePreAuthButtonLayout()
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		//addListener();
		
		downsizePreauthBtn = new Button("DownSize Preauth");
		
		esclateClaimBtn = new Button("Escalate Claim");
		
		hLayout.addComponents(downsizePreauthBtn,esclateClaimBtn);
		hLayout.setSpacing(true);
		addListener();
		return hLayout;
	}
	
	public FormLayout downSizeFormLayout()
	{
		bean.setStatusKey(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
		unbindField(escalateTo);
		unbindField(escalateRemarks);
		unbindField(reasonForDownSize);
		unbindField(approvedAmtField);
		unbindField(medicalRemarks);
		unbindField(totalApprovedAmt);
		unbindField(downsizeRemarks);
		unbindField(doctorNote);
		unbindField(cmbSpecialistType);
		
		mandatoryFields.remove(reasonForDownSize);
		mandatoryFields.remove(downSizedAmt);
		mandatoryFields.remove(medicalRemarks);
		mandatoryFields.remove(escalateRemarks);
		mandatoryFields.remove(downsizeRemarks);
		
		reasonForDownSize = (ComboBox) binder.buildAndBind("Reason for DownSize",
				"downSizeReason", ComboBox.class);
		
		reasonForDownSize.setContainerDataSource(this.selectValueContainer);
		reasonForDownSize.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForDownSize.setItemCaptionPropertyId("value");
		reasonForDownSize.setNullSelectionAllowed(false);
		
		if(this.bean.getPreauthMedicalDecisionDetails().getDownSizeReason() != null){
			reasonForDownSize.setValue(this.bean.getPreauthMedicalDecisionDetails().getDownSizeReason());
		}
		
//		preauthApprovedAmtTxt = (TextField) binder.buildAndBind("Total Pre-auth Approved Amt", "initialApprovedAmt", TextField.class);
//		preauthApprovedAmtTxt.setNullRepresentation("");
//		preauthApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
//		preauthApprovedAmtTxt.setEnabled(false);
		
		Integer min = Math.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
								.getValue()));
		
		totalApprovedAmt = (TextField) binder.buildAndBind("Pre-auth Downsized Amount", "initialTotalApprovedAmt", TextField.class);
		totalApprovedAmt.setNullRepresentation("");
		totalApprovedAmt.setEnabled(false);
		totalApprovedAmt.setValue(min.toString());
		
		downsizeRemarks = (TextArea)binder.buildAndBind("Downsize Remarks","downsizeRemarks",TextArea.class);
		downsizeRemarks.setMaxLength(50);
		downsizeRemarks.setWidth("400px");
		
		doctorNote = (TextField)binder.buildAndBind("Doctor Note(Internal purpose)","doctorNote",TextField.class);
		doctorNote.setWidth("400px");
		medicalRemarks = (TextField)binder.buildAndBind("Medical Remarks","medicalRemarks",TextField.class);
		medicalRemarks.setMaxLength(100);
		medicalRemarks.setWidth("400px");
		
		mandatoryFields.add(reasonForDownSize);
		mandatoryFields.add(totalApprovedAmt);
		mandatoryFields.add(medicalRemarks);
		mandatoryFields.add(downsizeRemarks);
		showOrHideValidation(false);
		
		FormLayout downSizeForm=new FormLayout(totalApprovedAmt, reasonForDownSize, downsizeRemarks, doctorNote, medicalRemarks);
		
		return downSizeForm;
	}
	
	public FormLayout escalateFormLayout(){
		
		bean.setStatusKey(ReferenceTable.DOWNSIZE_ESCALATION_STATUS);
		final BeanItemContainer<SelectValue> selectValueContainer2 = masterService.getSelectValueContainer(ReferenceTable.SPECIALIST_TYPE);
		unbindField(escalateTo);
		unbindField(escalateRemarks);
		unbindField(reasonForDownSize);
		unbindField(downSizedAmt);
		unbindField(medicalRemarks);
		unbindField(downsizeRemarks);
		
		mandatoryFields.remove(reasonForDownSize);
		mandatoryFields.remove(downSizedAmt);
		mandatoryFields.remove(medicalRemarks);
		mandatoryFields.remove(escalateRemarks);
		mandatoryFields.remove(downsizeRemarks);
		
		escalateTo=(ComboBox) binder.buildAndBind("Escalate To",
				"escalateTo", ComboBox.class);
		
		List<SelectValue> escalateValues = this.escalateContainer.getItemIds();
		
        BeanItemContainer<SelectValue> escalateContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(bean.getDownSizePreauthDataExtrationDetails().getCMA4()){
			
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA3()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA2()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3) || selectValue.getId().equals(ReferenceTable.CMA2)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA1()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3) || selectValue.getId().equals(ReferenceTable.CMA2)
						|| selectValue.getId().equals(ReferenceTable.CMA1)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}
		
		for (SelectValue selectValue : escalateValues) {
			if(selectValue.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
			escalateContainer2.addBean(selectValue);
			}	
		}
		
		escalateTo.setContainerDataSource(escalateContainer2);
		escalateTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateTo.setItemCaptionPropertyId("value");
		escalateTo.setNullSelectionAllowed(false);
		
		final FormLayout escalateForm1=new FormLayout();
		
		escalateTo.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null && value.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					
					unbindField(cmbSpecialistType);
					cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
					cmbSpecialistType.setContainerDataSource(selectValueContainer2);
					cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbSpecialistType.setItemCaptionPropertyId("value");
					cmbSpecialistType.setVisible(true);
					
					escalateForm1.addComponent(cmbSpecialistType,1);
					mandatoryFields.add(cmbSpecialistType);
					
					showOrHideValidation(false);
					
				}else{
					unbindField(cmbSpecialistType);
					if(cmbSpecialistType != null){
						
						escalateForm.removeComponent(cmbSpecialistType);
						mandatoryFields.remove(cmbSpecialistType);
						cmbSpecialistType.setVisible(false);
						
					}
					
				}
			}
		});
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
			escalateTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo());
		}
		
		fileUpload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	if(filename != null && ! filename.equalsIgnoreCase("")){
				            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
				            fos = new FileOutputStream(file);
		        	}
		        	else{
		        		  
		        
		        	}
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(com.vaadin.server.Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		fileUpload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					if(null != fileAsbyteArray )
					{
						
						Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								String fileName = event.getFilename();
								
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
			                    tmpViewBtn.setEnabled(true);
							    buildSuccessLayout();
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		fileUpload.setCaption("File Upload");
		fileUpload.setButtonCaption(null);
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
					popup.setContent(fileViewUI);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
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
			}
		});
        
     
		Button uploadBtn = new Button("Upload");
//		HorizontalLayout uploadHor = new HorizontalLayout(fileUpload,uploadBtn,tmpViewBtn);
//		uploadHor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
////		uploadHor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
//		uploadHor.setSpacing(true);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fileUpload.submitUpload();
			}
		});
		
		escalateRemarks=(TextArea)binder.buildAndBind("Escalation Remarks","escalateRemarks",TextArea.class);
		escalateRemarks.setMaxLength(100);
		escalateRemarks.setWidth("400px");
		
		mandatoryFields.add(escalateRemarks);
		mandatoryFields.add(escalateTo);
		
		showOrHideValidation(false);
		
		escalateForm1.addComponent(escalateTo);
		escalateForm1.addComponent(fileUpload);
		escalateForm1.addComponent(escalateRemarks);
		
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout escalateForm2 = new FormLayout(dummyField,uploadBtn);
		escalateForm2.setWidth("80%");
		
		TextField dummyField1 = new TextField();
		dummyField1.setEnabled(false);
		dummyField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout escalateForm3 = new FormLayout(dummyField1,tmpViewBtn);
		escalateForm3.setWidth("40%");
		escalateForm3.setComponentAlignment(tmpViewBtn, Alignment.MIDDLE_LEFT);
		HorizontalLayout mainHor = new HorizontalLayout(escalateForm1,escalateForm2,escalateForm3);
		mainHor.setWidth("1000px");
		mainHor.setSpacing(true);
		escalateForm = new FormLayout(mainHor);
		escalateForm.setWidth("100%");
		return escalateForm;
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		 this.referenceData = referenceData;
		 @SuppressWarnings("unchecked")
		 BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
			.get("fvrNotRequiredRemarks");
		 
		 BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData.get("investigatorName");
		 
	}
	
	public void setValueToMedicalDecisionValues(){
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
									if (null != pedDetailsTableDTO
											.getExclusionDetails()
											&& exclusionDetails
													.getKey()
													.equals(pedDetailsTableDTO
															.getExclusionDetails()
															.getId())) {
										paymentFlag = exclusionDetails
												.getPaymentFlag();
									}
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				} else {
					dto.setIsPaymentAvailable(false);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
				if(pedValidationTableDTO.getPedList().size() == 1) {
					dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
				}
				
//				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
//				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
//				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
//				
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}else{
					dto.setIsAmbulanceEnable(false);
				}
				
				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				Boolean isPaymentAvailable = true;
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					 isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
				} else {
					isPaymentAvailable = false;
					dto.setIsPaymentAvailable(false);
				}
					if(isPaymentAvailable) {
						if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
							isPaymentAvailable = false;
						}
					}
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				dto.setRestrictionSI("NA");
				
				//GLX2020047 - UAT changes
				//dto.setPackageAmt("NA");
				dto.setPackageAmt("0");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
					dto.setPackageAmt(procedureDTO.getPackageRate().toString());
					//GLX2020047
					dto.setAgreedPackageAmt(procedureDTO.getPackageRate().toString());
				}
				

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				dto.setCoPayPercentage(procedureDTO.getCopay());
				
//				dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
//				dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
//				dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
				
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}else{
					dto.setIsAmbulanceEnable(false);
				}
				
				medicalDecisionDTOList.add(dto);
			}

			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = 0d;
			if(null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			 insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				 insuredSumInsured = dbCalculationService
							.getInsuredSumInsured(this.bean.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId().toString(),
									this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
									.getInsuredPatient().getLopFlag());
			}
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));
			
			int medicalDecisionSize = medicalDecisionDTOList.size();

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSumInsuredRestriction() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSumInsuredRestriction().getId()
												: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
						caluculationInputValues.put("referenceFlag", "D");
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
//					caluculationInputValues.put("preauthKey", 0l);
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY,bean.getClaimDTO().getKey());
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					
					fireViewEvent(
							PADownsizePreauthRequestWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,this.bean);
					Map<String, Object> values = this.sublimitCalculatedValues;
					
					if(bean.getIsNonAllopathic()) {
						bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
						bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
					}
					
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					medicalDecisionDto.setAvailableAmout(((Double) values
							.get("restrictedAvailAmt")).intValue());
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
					}

					// need to implement in new medical listener table
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
						Integer subLimitAvaliableAmt = 0;
						Boolean isResidual = false;
						if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else {
							isResidual = true;
						}
						
						if(!isResidual && bean.getEntitlmentNoOfDays() != null) {
							Float floatAvailAmt = bean.getEntitlmentNoOfDays() * subLimitAvaliableAmt;
							Integer availAmt = Math.round(floatAvailAmt);
							int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
							medicalDecisionDto.setSubLimitAvaliableAmt(min);
							medicalDecisionDto.setSubLimitUtilAmount(0);
						}
					}
					
					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}
						}
						
					}
					
					this.newMedicalDecisionTableObj
							.addBeanToList(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				this.newMedicalDecisionTableObj.addBeanToList(dto);
				
				if(bean.getIsNonAllopathic()) {
					createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
				}
			}
			
		} else {
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
				}
			}
			this.newMedicalDecisionTableObj.addList(filledDTO);
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
	}
	
	private void createNonAllopathicFields(Double originalAmt, Double utilizedAmt) {
		nonAllopathicTxt = new TextField("Non-Allopathic Avail Amt");
		nonAllopathicTxt.setWidth("80px");
		Double availAmt = originalAmt - utilizedAmt;
		nonAllopathicTxt.setValue(String.valueOf(availAmt.intValue()) );
		nonAllopathicTxt.setEnabled(false);
		bean.setNonAllopathicAvailAmt(availAmt.intValue());
		bean.setNonAllopathicOriginalAmt(originalAmt);
		bean.setNonAllopathicUtilizedAmt(utilizedAmt);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, originalAmt);
		values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilizedAmt);
		Button viewBtn = new Button("View");
		viewBtn.setData(values);
		viewBtn.setStyleName("link");
		viewBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 9127517383717464157L;

			@Override
			public void buttonClick(ClickEvent event) {
				Map<String, Object> values = (Map<String, Object>) event.getButton().getData();
				
				TextField originalAmt = new TextField("Total Original Amt");
				originalAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT)).intValue()));
				originalAmt.setReadOnly(true);
				originalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField utilizedAmt = new TextField("Utilized Amt");
				utilizedAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT)).intValue()));
				utilizedAmt.setReadOnly(true);
				utilizedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField availableAmt = new TextField("Available Amt");
				Double availAmt = (Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) - (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
				availableAmt.setValue(String.valueOf(availAmt.intValue()) );
				availableAmt.setReadOnly(true);
				availableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Non-Allopathic Details");
				dialog.setClosable(true);
				dialog.setWidth("400px");
				dialog.setResizable(false);
				dialog.setContent(new FormLayout(originalAmt, utilizedAmt, availableAmt));
				dialog.show(getUI().getCurrent(), null, true);
			}
		});
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(nonAllopathicTxt), viewBtn);
		horizontalLayout.setSpacing(true);
		optionCLayout.addComponent(horizontalLayout);
		optionCLayout.setSpacing(true);
	}
	
	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.newMedicalDecisionTableObj.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if(medicalDecisionTableDTO.getDiagOrProcedure() != null && medicalDecisionTableDTO.getDiagOrProcedure().contains("Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean.getResidualAmountDTO();
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO.getNetAmount().doubleValue());
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO.getMinimumAmount().doubleValue());
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d);
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO.getCoPayAmount().doubleValue());
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO.getNetApprovedAmt().doubleValue());
				residualAmountDTO.setRemarks(medicalDecisionTableDTO.getRemarks());
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}
	
	
	
	public void addListener(){
		downsizePreauthBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				   NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				    if(negotiationPending != null){
				    	manageNegotation(intimationDtls,negotiationPending,SHAConstants.DOWNSIZE_PRE_AUTH);
				    }
				    else{
						downSizeFormLayout.removeAllComponents();
						downSizeFormLayout.addComponent(downSizeFormLayout());
		//				bean.setIsDownsizeOrEscalate(true);
						wizard.getFinishButton().setEnabled(false);
						wizard.getNextButton().setEnabled(true);
				    }
			}
		});
		
		esclateClaimBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isEsclateClaim=true;
				downSizeFormLayout.removeAllComponents();
				downSizeFormLayout.addComponent(escalateFormLayout());
				
				wizard.getFinishButton().setEnabled(true);
				wizard.getNextButton().setEnabled(false);
				
			}
		});

		intiatePEDEndorsementButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -8159939563947706329L;

				@Override
				public void buttonClick(ClickEvent event) {
					Long preauthKey = bean.getKey();          //preauth key from table dto
					Long intimationKey = bean.getIntimationKey();
					Long policyKey = bean.getPolicyKey();
					Long claimKey = bean.getClaimKey();
					if(bean.getIsPEDInitiatedForBtn()) {
						alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					} else {
						createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
					}
				}
			});
	}

	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);						
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	

	
	
public Boolean alertMessageForPEDInitiate(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}

	@Override
	public boolean onAdvance() {
		
		setResidualAmtToDTO();
		
		return validatePage();
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}
	
	public void setReferenceData(List<MedicalDecisionTableDTO> medicalDecisionTableList) {
		
		this.medicalDecisionTableList=medicalDecisionTableList;
		
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		
		//GLX2020047
				String errMsg = newMedicalDecisionTableObj.isValidForPkgChange();
				
				if(errMsg != null && !errMsg.isEmpty()){
					hasError = true;
					eMsg+=errMsg+"<br>";
				}
		
		if(this.reasonForDownSize.getValue() == null && this.escalateTo.getValue() == null){
			hasError = true;
			eMsg += "Please Select Downsize prauth or Escalate Claim</br>";
		}
		
		if(this.reasonForDownSize.getValue() != null) {
			if(downsizeRemarks.getValue() == null || downsizeRemarks.getValue().isEmpty()) {
				hasError = true;
				eMsg += "Please Enter Downsize Remarks.</br>";
			}	
		}
		
		 if(!this.newMedicalDecisionTableObj.getTotalAmountConsidered().equals(SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
			 hasError = true;
			 eMsg += "Total Amount Considered Should be equal to Claimed Amount Page Payable Amount. </br>";
		 }
		 
		 if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
				if(this.bean.getIsAmbulanceApplicable() && this.newMedicalDecisionTableObj.getAmbulanceLimitAmount() != null
						&& ! this.bean.getAmbulanceLimitAmount().equals(this.newMedicalDecisionTableObj.getAmbulanceLimitAmount())){
					
					hasError = true;
					eMsg += "Amount Entered against Ambulance charges should be equal";
					
				}
			}
		 
		 if(! hasError){
			 try {
				this.binder.commit();
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
		 }
		 
		  if(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
	        		&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null
	        		&& this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
	        	Double TotalApprovedAmount = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
	        	Double downsizeAmt = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
	        	
	        	if(TotalApprovedAmount <= downsizeAmt){
	        		hasError = true;
	        		eMsg += "Downsize amount should be less than Preauth approved Amount. </br>";
	        	}
	        }
		 
		if (hasError) {
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

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
			    this.bean.getPreauthMedicalDecisionDetails().setMedicalDecisionTableDTO(this.newMedicalDecisionTableObj.getValues());
			    this.bean.getPreauthMedicalDecisionDetails().setDownsizedAmt(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()
			    		!= null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() : 0d);
			    
			    if(this.bean.getStatusKey() != null && this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					Integer min = Math.min(amountConsideredTable.getMinimumValue(),
							SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
									.getValue()));
					this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(min != null ? Double.valueOf(min) : 0d);
					this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min != null ? Double.valueOf(min) : 0d);
				}
			    
			    if(approvedAmtField != null && approvedAmtField.getValue() != null){
					Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
					this.bean.setSublimitAndSIAmt(approvedAmount);
				}

			    return true;
		   
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
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
	
	public void setBalanceSI(Double balanceSI, List<Double> productCopay) {
		if(balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(productCopay);
	}
	
	public void setSumInsuredCaculationsForSublimit(
			Map<String, Object> diagnosisSumInsuredLimit,String diagnosisName) {
		this.sublimitCalculatedValues = diagnosisSumInsuredLimit;
		this.diagnosisName = diagnosisName;
	}
	
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
	
		this.exlusionContainer=exclusionContainer;
	}
		
	private void addMedicalDecisionTableFooterListener() {

		this.newMedicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmtValue = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmtValue));
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmtValue);
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						if ((bean.getStatusKey()
								.equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))) {
							if(totalApprovedAmt != null){
								totalApprovedAmt.setValue(min.toString());
							}
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									if(totalApprovedAmt != null){
										totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
									}
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
								}
							}
						}
						
//						Integer totalApprovedAmtValue = SHAUtils.getIntegerFromString((String)event.getProperty().getValue());
//						approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
//						Integer min = Math.min(amountConsideredTable.getMinimumValue(),totalApprovedAmtValue);
//						 if((bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))) {
//							if(totalApprovedAmt != null) {
//								totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//							}
//							if(bean.getNewIntimationDTO() != null) {
//								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
//								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
//									min = Math.min(amountConsideredTable.getConsideredAmountValue(),totalApprovedAmtValue);
//									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//									if(totalApprovedAmt != null) {
//										totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//									}
//								}
//							}
//						} else {
//							if(bean.getNewIntimationDTO() != null) {
//								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
//								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
//									min = Math.min(amountConsideredTable.getConsideredAmountValue(),totalApprovedAmtValue);
//									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//									if(totalApprovedAmt != null) {
//										totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//									}
//								}
//							}
//						}
						
					}
				});
		
		this.amountConsideredTable.dummyField
		.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Integer totalApprovedAmtValue = SHAUtils
						.getIntegerFromString(newMedicalDecisionTableObj.dummyField
								.getValue());
				approvedAmtField.setValue(String
						.valueOf(totalApprovedAmtValue));
				if ((bean.getStatusKey()
						.equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))) {
					Integer min = Math.min(
							amountConsideredTable.getMinimumValue(),
							totalApprovedAmtValue);
					if(bean.getIsNonAllopathic()) {
						min = Math.min(min, bean.getNonAllopathicAvailAmt());
					}
					if(totalApprovedAmt != null){
						totalApprovedAmt.setValue(min.toString());
					}
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
							min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
							if(totalApprovedAmt != null){
								totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
							}
						}
					}
				} else {
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
						     Integer min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//							processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
						}
					}
				}
//				Integer totalApprovedAmtValue = SHAUtils.getIntegerFromString((String)event.getProperty().getValue());
//				approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
//				Integer min = Math.min(amountConsideredTable.getMinimumValue(),totalApprovedAmtValue);
//				 if((bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))) {
//					if(totalApprovedAmt != null) {
//						totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//					}
//					if(bean.getNewIntimationDTO() != null) {
//						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
//						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
//							min = Math.min(amountConsideredTable.getConsideredAmountValue(),totalApprovedAmtValue);
//							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//							if(totalApprovedAmt != null) {
//								totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//							}
//						}
//					}
//				} else {
//					if(bean.getNewIntimationDTO() != null) {
//						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
//						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
//							min = Math.min(amountConsideredTable.getConsideredAmountValue(),totalApprovedAmtValue);
//							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//							if(totalApprovedAmt != null) {
//								totalApprovedAmt.setValue(String.valueOf(amountConsideredTable.doSuperSurplusCalculation()));
//							}
//						}
//					}
//				}
				
			}
		});

	}
	
	public void showErrorMessage(){
		
		Label label = new Label("Downsize amount should be less than Preauth approved Amount", ContentMode.HTML);
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
	
public void manageNegotation(final Intimation intimationDtls,final NegotiationDetails negotiation,final String decision){
		
		ConfirmDialog dialog = ConfirmDialog.show(UI.getCurrent(),"Confirmation", "Negotation is under progress. Do You Want to Cancel or Update?",
		        "Update", "Cancel", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	 dialog.close();
		                	 showNagotiationRemarks(true, intimationDtls,negotiation,decision);
		                } else {
		                    dialog.close();
		                    showNagotiationRemarks(false, intimationDtls,negotiation,decision);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
public void showNagotiationRemarks(final Boolean isCancel,final Intimation intimationDtls
		,final NegotiationDetails negotiation,final String decision){

	VerticalLayout vLayout =  new VerticalLayout();
	final Window dialog = new Window();
	vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
	vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
	vLayout.setMargin(true);
	vLayout.setSpacing(true);
	final TextArea txtArea = new TextArea();
	txtArea.setMaxLength(4000);
	txtArea.setData(bean);
	//txtArea.setStyleName("Boldstyle");
	txtArea.setNullRepresentation("");
	if(!isCancel){
		txtArea.setValue(negotiation.getNegotiationRemarks());
	}
	//txtArea.setSizeFull();
	
	
//	if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
		txtArea.setRows(25);
		txtArea.setHeight("30%");
		txtArea.setWidth("100%");
		dialog.setHeight("65%");
    	dialog.setWidth("65%");
		txtArea.setReadOnly(false);
//	}
	final TextField amtToNegotiatedFild = new TextField("Amount to be Negotiated");
	amtToNegotiatedFild.setValue(negotiation.getNegotiationAmt() != null ? String.valueOf(negotiation.getNegotiationAmt()) : "0");
	FormLayout negAmt = new FormLayout(amtToNegotiatedFild);
	negAmt.setVisible(false);
	
	
	Button okBtn = new Button("OK");
	okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	vLayout.addComponent(negAmt);
	vLayout.addComponent(txtArea);
	vLayout.addComponent(okBtn);
	vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
	
	
	String strCaption = "";
	
	if(isCancel){
		strCaption = "Cancel Remarks";
		negAmt.setVisible(false);
	}else{
		strCaption = "Update Remarks";
		negAmt.setVisible(true);
	}
	dialog.setCaption(strCaption);
			
	
	dialog.setClosable(true);
	
	dialog.setContent(vLayout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.setDraggable(true);
	
	dialog.addCloseListener(new Window.CloseListener() {
		
		@Override
		public void windowClose(CloseEvent e) {
			if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){downSizeFormLayout.removeAllComponents();
			downSizeFormLayout.addComponent(downSizeFormLayout());
//				bean.setIsDownsizeOrEscalate(true);
			wizard.getFinishButton().setEnabled(false);
			wizard.getNextButton().setEnabled(true);
		}
			dialog.close();
		}
	});
	
	if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
		dialog.setPositionX(250);
		dialog.setPositionY(100);
	}
	getUI().getCurrent().addWindow(dialog);
	okBtn.addClickListener(new Button.ClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			if(txtArea.getValue() != null && ! txtArea.getValue().isEmpty() && !StringUtils.isBlank(txtArea.getValue())){
				if(isCancel){
					Status status = new Status();
					status.setKey(ReferenceTable.NEGOTIATION_CANCELLED);
					negotiation.setStatusId(status);
					negotiation.setNegotiationCancelRemarks(txtArea.getValue());
				}else{
					negotiation.setCashlessKey(bean.getKey());
					negotiation.setNegotiationRemarks(txtArea.getValue());
					negotiation.setNegotiationAmt(Double.valueOf(amtToNegotiatedFild.getValue()));
				}
				
				fireViewEvent(PADownsizePreauthRequestWizardPresenter.NEGOTIATION_CANCEL_OR_UPDATE, negotiation);
				SHAUtils.buildNegotiationSuccessLayout(getUI(),isCancel);
				
				if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){downSizeFormLayout.removeAllComponents();
					downSizeFormLayout.addComponent(downSizeFormLayout());
	//				bean.setIsDownsizeOrEscalate(true);
					wizard.getFinishButton().setEnabled(false);
					wizard.getNextButton().setEnabled(true);
				}

				dialog.close();
			}else{
				showErrorMessage("Please Enter Remarks");
			}
		}
	});	
}

public void showErrorMessage(String eMsg) {
	Label label = new Label(eMsg, ContentMode.HTML);
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
}

}
