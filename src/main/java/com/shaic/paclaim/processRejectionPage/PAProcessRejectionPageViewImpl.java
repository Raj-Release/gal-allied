package com.shaic.paclaim.processRejectionPage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ReportDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class PAProcessRejectionPageViewImpl extends AbstractMVPView implements PAProcessRejectionView {

	private static final long serialVersionUID = 1L;

	
	@Inject
	private ViewDetails viewDetails;	
	
	@Inject
	private ProcessRejectionDTO bean;
	
	@Inject
	private Instance<PAProcessRejectionPage> processRejectionPageInstance;
	
	private PAProcessRejectionPage processRejectionPageObj;
	
	@Inject
	private Instance<PAProcessRejectionPreauthPage> processRejectionPreauthPageInstance;
	
	private PAProcessRejectionPreauthPage processRejectionPreauthPageObj;
	
	private BeanFieldGroup<ProcessRejectionDTO> binder;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private static BeanItemContainer<SelectValue> selectedValues = null;
	
	private VerticalLayout mainVertical;
	
	private Button preauthDocumentBtn;
	
	private TextArea waiveRemarks;
	
	private VerticalLayout verticalMain;
	
	private HorizontalLayout rejectionButtonsLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Button confirmRejectionBtn;
	
	private Button waiveRejectionBtn;
	
	private ComboBox rejectionCategory;             //need to be change comboBox
	
	private TextArea confirmRemarks;
	
	private TextArea doctorNote;
	
	private Button viewPreauthBtn;
	
	private VerticalLayout vertical;
	
	private HorizontalLayout buttonHorLayout;
	
	private NewIntimationDto intimationDto;
	
	private FormLayout dynamicLayout;
	
	public static Boolean DESICION=false;
	
	private Boolean isButtonClicked = null;
	
	private SearchProcessRejectionTableDTO searchDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;
	
	private PreauthDTO preauthDTO;
	
	private IWizardPartialComplete wizard;
	
	@Override
	public String getCaption() {
		return "Process Rejection Page";
	}

	
	@PostConstruct
	public void initView()
	{
		
	}
	
	@Override
	public void setReferenceData(SearchProcessRejectionTableDTO bean,NewIntimationDto intimationDto) {
		
		
		this.intimationDto=intimationDto;
		this.searchDTO = bean;
		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
		this.binder.setItemDataSource(this.searchDTO.getProcessRejectionDTO());
		
		if ( this.searchDTO.getIsPremedical() == null || (this.searchDTO.getIsPremedical() != null && !this.searchDTO.getIsPremedical())) {
			this.processRejectionPageObj.setReferenceData(this.searchDTO.getProcessRejectionDTO(), intimationDto);
		}
		else {
			this.processRejectionPreauthPageObj.setReferenceData(this.searchDTO.getProcessRejectionDTO(),intimationDto);
		}
		
	}

	public void initView(SearchProcessRejectionTableDTO searchDTO)
	{
		this.searchDTO=searchDTO;
		
		this.rrcDTO = searchDTO.getRrcDTO();
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		//preauthDTO.setRodHumanTask(searchDTO.getHumanTask());

		mainVertical = new VerticalLayout();
		
		VerticalLayout verticalMain=buildLayout();
		
		mainVertical.addComponent(verticalMain);
		mainVertical.setHeight("670px");
		setCompositionRoot(mainVertical);
	}
	
	public void initView(SearchProcessRejectionTableDTO searchDTO,ProcessRejectionDTO bean,IWizardPartialComplete wizard){
		
		  //this.bean=bean;
				this.searchDTO=searchDTO;
				this.bean = bean;
				this.wizard = wizard;
				//
				
				this.rrcDTO = searchDTO.getRrcDTO();
				preauthDTO = new PreauthDTO();
				preauthDTO.setRrcDTO(this.rrcDTO);
			//	preauthDTO.setRodHumanTask(searchDTO.getHumanTask());		
	}
	
	public VerticalLayout buildLayout() {
		
		if (this.searchDTO.getIsPremedical()) {
			processRejectionPreauthPageObj = processRejectionPreauthPageInstance
					.get();
			processRejectionPreauthPageObj.initView(this.searchDTO);
		} else {
			processRejectionPageObj = processRejectionPageInstance.get();
			processRejectionPageObj.initView(this.searchDTO);
		}

		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		confirmRejectionBtn = new Button("Confirm Rejection");
		confirmRejectionBtn.addStyleName("querybtn");
		waiveRejectionBtn = new Button("Waive Rejection");
		waiveRejectionBtn.addStyleName("querybtn");
		
		unbindField(doctorNote);
//		unbindField(waiveProvisionAmt);
		

		doctorNote = (TextArea) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextArea.class);
		doctorNote.setWidth("400px");
//		waiveProvisionAmt = (TextField) binder.buildAndBind("Provision Amount",
//				"waiveProvisionAmt", TextField.class);
		doctorNote.setNullRepresentation("");
		

		rejectionButtonsLayout = new HorizontalLayout(confirmRejectionBtn,
				waiveRejectionBtn);
		rejectionButtonsLayout.setSpacing(true);

		dynamicLayout = new FormLayout();

		if(processRejectionPageObj != null) {
			vertical = new VerticalLayout(processRejectionPageObj, rejectionButtonsLayout);
			vertical.setComponentAlignment(processRejectionPageObj,
					Alignment.TOP_LEFT);
		}
		else
		{
			vertical = new VerticalLayout(processRejectionPreauthPageObj, rejectionButtonsLayout);
			vertical.setComponentAlignment(processRejectionPreauthPageObj,
					Alignment.TOP_LEFT);
		}
		
		addListener();
		vertical.setSpacing(true);
		vertical.setComponentAlignment(rejectionButtonsLayout, Alignment.BOTTOM_RIGHT);
		verticalMain = new VerticalLayout(vertical, dynamicLayout);
		
		if(this.searchDTO.getStatusKey() != null && this.searchDTO.getStatusKey().equals(ReferenceTable.PROCESS_REJECTED)){
			
			generateFieldBasedOnConfirmClick(selectedValues);
			
		}else if(bean.getStatusKey() != null && this.searchDTO.getStatusKey().equals(ReferenceTable.PREMEDICAL_WAIVED_REJECTION)){
			generateFieldBasedOnWaiveClick();
		}

		return verticalMain;
	}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		HorizontalLayout alignmentHLayout;
		if (this.searchDTO.getIsPremedical()) {
			viewPreauthBtn = new Button("View pre-auth");
			 alignmentHLayout = new HorizontalLayout(btnRRC,viewPreauthBtn);
			 
		}
		else
		{
			preauthDocumentBtn = new Button("Pre-auth Document");
		 alignmentHLayout = new HorizontalLayout(btnRRC,preauthDocumentBtn);
		}
		alignmentHLayout.setSpacing(true);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PAProcessRejectionPresenter.VALIDATE_PROCESS_REJECTION_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
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
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_PROCESS_REJECTION);
			
		/*	PreauthDTO preauthDTO = new PreauthDTO();
			preauthDTO.setRrcDTO(this.rrcDTO);*/
			
			rewardRecognitionRequestViewObj.init(preauthDTO, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void addListener() {

		confirmRejectionBtn.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				PAProcessRejectionPageViewImpl.DESICION = false;
				isButtonClicked = true;
				fireViewEvent(PAProcessRejectionPresenter.CONFIRM_PA_BUTTON, null);
			}
		});
		waiveRejectionBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				PAProcessRejectionPageViewImpl.DESICION = true;
				isButtonClicked = true;
				fireViewEvent(PAProcessRejectionPresenter.WAIVE_PA_BUTTON, null);
			}
		});


		if (viewPreauthBtn != null) {
			viewPreauthBtn.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					if (intimationDto.getIntimationId() != null) {
						viewDetails.getPreAuthDetail(intimationDto
								.getIntimationId());
					}
				}
			});
		}

	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void generateFieldBasedOnConfirmClick(BeanItemContainer<SelectValue> selectedValues) {
                       
                       //confirmLayout=new FormLayout(rejectionCategory,confirmRemarks,medicalRemarks,doctorNote);
		               
		               this.searchDTO.setStatusKey(ReferenceTable.PROCESS_REJECTED);
		               
		               this.selectedValues = selectedValues;
		               unbindField(rejectionCategory);
		               unbindField(confirmRemarks);
//		               unbindField(medicalRemarks);
		               unbindField(waiveRemarks);
		               
		                
		               rejectionCategory=(ComboBox)binder.buildAndBind("Rejection Category","rejectionCategory" ,ComboBox.class);

		               confirmRemarks=(TextArea)binder.buildAndBind("Suggested Rejection Remarks","confirmRemarks" ,TextArea.class);
		               confirmRemarks.setWidth("400px");
		               if(this.searchDTO.getProcessRejectionDTO().getRejectionRemarks() != null){
		            	   confirmRemarks.setValue(this.searchDTO.getProcessRejectionDTO().getRejectionRemarks());
		               }
//		               medicalRemarks=(TextArea)binder.buildAndBind("Medical Remarks","medicalRemarks" ,TextArea.class);
//		               medicalRemarks.setWidth("400px");
		               rejectionCategory.setContainerDataSource(selectedValues);
		               rejectionCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		               rejectionCategory.setItemCaptionPropertyId("value");
		               
		               if(this.searchDTO.getProcessRejectionDTO().getRejectionCategory() != null){
		            	   rejectionCategory.setValue(this.searchDTO.getProcessRejectionDTO().getRejectionCategory());
		               }
		               
		               mandatoryFields.add(rejectionCategory);
                       mandatoryFields.add(confirmRemarks);
//                       mandatoryFields.add(medicalRemarks);
                       
                       showOrHideValidation(false);
                       
//                       confirmLayout.removeAllComponents();
		           
//                       confirmLayout.addComponent(rejectionCategory);
//                       confirmLayout.addComponent(confirmRemarks);
////                     confirmLayout.addComponent(medicalRemarks);
//                       confirmLayout.addComponent(doctorNote);
//                     dynamicLayout.addComponent(confirmLayout);
//                     dynamicLayout.setComponentAlignment(confirmLayout, Alignment.BOTTOM_LEFT);
//                     dynamicLayout.removeComponent(waiveLayout);
//                     waiveLayout.removeAllComponents();
                       
                       dynamicLayout.removeAllComponents();
                       dynamicLayout.addComponent(rejectionCategory);
                       dynamicLayout.addComponent(confirmRemarks);
                       dynamicLayout.addComponent(doctorNote);
                                              
                       wizard.getNextButton().setEnabled(true);
               		   wizard.getFinishButton().setEnabled(false);
                      
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}
	@Override
	public void generateFieldBasedOnWaiveClick() {
		
		this.searchDTO.setStatusKey(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);

		unbindField(rejectionCategory);
        unbindField(confirmRemarks);
//        unbindField(medicalRemarks);
        unbindField(waiveRemarks);
        
		
        waiveRemarks =(TextArea) binder.buildAndBind("Waive Rejection Remarks","waiveRemarks", TextArea.class);
	    waiveRemarks.setNullRepresentation("");
	    waiveRemarks.setWidth("400px");
	    
	    mandatoryFields.add(waiveRemarks);
	    
	    showOrHideValidation(false);
	    
//		confirmLayout.removeAllComponents();
//		dynamicLayout.removeComponent(confirmLayout);
//		waiveLayout.removeAllComponents();
//		waiveLayout.addComponent(waiveRemarks);
//	    dynamicLayout.addComponent(waiveLayout);
//	    dynamicLayout.setComponentAlignment(waiveLayout, Alignment.BOTTOM_LEFT);
	    
	    dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(waiveRemarks);
		
		wizard.getNextButton().setEnabled(false);
		wizard.getFinishButton().setEnabled(true);
	}

	@Override
	public void savedResult() {

Label successLabel = new Label("<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Process Rejection Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
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
				fireViewEvent(MenuItemBean.PA_PROCESS_PREAUTH_REJECTION, true);
				
			}
		});
	}
	
	@Override
	public void openPdfFileInWindow(Claim claim,PreauthDTO preauthDTO) {
		
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		
		preauthDTOList.add(preauthDTO);
		if(claim != null){
			reportDto.setClaimId(claim.getClaimId());
		}

		reportDto.setBeanList(preauthDTOList);	
		
		
		String filePath = docGen.generatePdfDocument("RegistrationSuggestRejectionLetter", reportDto);
		
		final String finalFilePath = filePath;
		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("");
		window.setWidth("800");
		window.setHeight("100%");
		window.setClosable(false);
		window.setModal(true);
		window.center();

		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		e.setHeight("700px");
		
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				window.close();
				savedResult();
				
			}
		});
		
		HorizontalLayout hor = new HorizontalLayout(homeButton);
		hor.setWidth("100%");
		hor.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		
		VerticalLayout mainVertical = new VerticalLayout(e,hor);
		
		window.setContent(mainVertical);
		UI.getCurrent().addWindow(window);
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
		
		if(isButtonClicked == null){
			hasError = true;
			eMsg += "Please Click Confirm or Waive Button";
		}
		
		
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
		
		if(isButtonClicked != null &&  !PAProcessRejectionPageViewImpl.DESICION){
			if(confirmRemarks != null && confirmRemarks.getValue() != null && confirmRemarks.getValue().isEmpty()){
				eMsg += "Please Enter Suggested Rejection Remarks";
				hasError = true;
			}
			else{
				if(waiveRemarks != null && waiveRemarks.getValue() != null && waiveRemarks.getValue().isEmpty()){
					eMsg += "Please Enter Waive Rejection Remarks";
					hasError = true;
				}
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
		}else{
			try {
				this.binder.commit();
				if(null != confirmRemarks && null != confirmRemarks.getValue()){
					
					bean.setConfirmRemarks(confirmRemarks.getValue());
				}
					
				
				
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			showOrHideValidation(false);
			return true;
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

	@Override
	public void init(ProcessRejectionDTO bean) {
		this.bean = bean;
	}

	@Override
	public Component getContent() {
		
		mainVertical = new VerticalLayout();

		
		VerticalLayout verticalMain=buildLayout();
		
		mainVertical.addComponent(verticalMain);
		mainVertical.setSizeFull();
		mainVertical.setHeight("670px");
		return mainVertical;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		
		if(validatePage()){
			
			if (!PAProcessRejectionPageViewImpl.DESICION) {
					SelectValue selected = new SelectValue();
					selected = (SelectValue) rejectionCategory.getValue();
					this.searchDTO.getProcessRejectionDTO().setRejectionCategory(selected);
					this.searchDTO.getProcessRejectionDTO().setConfirmRemarks(confirmRemarks.getValue());
					this.searchDTO.getProcessRejectionDTO().setRejectionRemarks(confirmRemarks.getValue());
//					this.searchDTO.getProcessRejectionDTO().setMedicalRemarks(medicalRemarks.getValue());
					this.searchDTO.getProcessRejectionDTO().setDoctorNote(doctorNote.getValue());
					this.searchDTO.getProcessRejectionDTO().setUsername(searchDTO.getUsername());		
					return true;
//					fireViewEvent(PAProcessRejectionPresenter.SUBMIT_DATA,
//							bean, false, "REJECT", searchDTO,intimationDto);

			} else {

//				this.searchDTO.getProcessRejectionDTO().setProvisionAmt(new Double(waiveProvisionAmt
//							.getValue().replaceAll("(\\d+),.*", "$1")));
//
//					System.out.println(waiveProvisionAmt.getValue()
//							.replaceAll("(\\d+),.*", "$1"));

					this.searchDTO.getProcessRejectionDTO().setWaiveRemarks(waiveRemarks.getValue());
					return true;
//					fireViewEvent(PAProcessRejectionPresenter.SUBMIT_DATA,
//							bean, true, "WAIVE", searchDTO,intimationDto);

			   }
			
		}

		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ProcessRejectionDTO getUpdatedBean(){
		return this.bean;
	}

	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
	 
	 public void clearObject(){
		 selectedValues = null;
	 }
}
