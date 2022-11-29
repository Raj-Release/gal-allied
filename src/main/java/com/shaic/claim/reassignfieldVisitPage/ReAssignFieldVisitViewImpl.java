package com.shaic.claim.reassignfieldVisitPage;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fieldVisitPage.FieldVisitDTO;
import com.shaic.claim.fieldVisitPage.FieldVisitPageRepresentativeNameSearchUI;
import com.shaic.claim.fieldVisitPage.TmpFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reassignfieldvisit.search.SearchReAssignFieldVisitTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

public class ReAssignFieldVisitViewImpl extends AbstractMVPView implements
ReAssignFieldVisitView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FieldVisitDTO bean;
	
	@Inject
	private TmpFVRDTO tmpFVRDTO;

	@Inject
	private RevisedCashlessCarousel preauthIntimationDetailsCarousel;

	//private IntimationService intimationService;

	@Inject
	private Intimation intimation;

	@Inject
	private NewIntimationDto newIntimationDto;

	/*@Inject
	private PreauthService preAuthService;*/

	/*@Inject
	private ClaimService claimService;*/

	
	
	@EJB
	private MasterService masterService;

	@Inject
	private ClaimDto claimDTO;

	@Inject
	private ViewDetails viewDetails;

	@EJB
	private FieldVisitRequestService fieldVisitSerivice;
	
	//private SearchReAssignFieldVisitTableDTO searchReAssignFieldVisitTableDTO;
	
	private AutocompleteField<TmpFVRDTO> representName;
	
	@Inject
	private FieldVisitPageRepresentativeNameSearchUI fieldVisitPageRepresentativeNameSearchUI;
	
	public static Window popup;
	
	private TextField txtTelNo;

	private TextField txtMobileNo;
	
	private Button btnRepresentativeSearch;

	private HorizontalLayout dynamicLayout;
	
	private RRCDTO rrcDTO;
	
	private PreauthDTO preauthDTO;
	
	private ComboBox allocTo;
	private ComboBox priority;
	
	private Button reAssignFvr ;
	
	private TextArea fvrexecomments;
	private TextArea fvrTriggerPoints;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	
	@SuppressWarnings("unchecked")
	public void initView(SearchReAssignFieldVisitTableDTO searchDTO) {
		
		

		//this.searchReAssignFieldVisitTableDTO = searchDTO;
		this.rrcDTO = searchDTO.getRrcDTO();
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		//preauthDTO.setRodHumanTask(searchDTO.getHumanTask());
		
		fireViewEvent(ReAssignFieldVisitPresenter.FIELD_VISIT, searchDTO);
		
		if (newIntimationDto != null && claimDTO != null) {
//			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDTO,
//					"Process Field Visit");
			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDTO,
					"Process Field Visit",searchDTO.getDiagnosis());
		}
        
		if(searchDTO.getTransactionKey() != null && searchDTO.getTransactionFlag() != null && searchDTO.getTransactionFlag().equals("R")){
			viewDetails.initView(this.intimation.getIntimationId(), searchDTO.getTransactionKey(), ViewLevels.INTIMATION,"Process Field Visit");
		}
		else{
			viewDetails.initView(this.intimation.getIntimationId(),ViewLevels.INTIMATION);
			viewDetails.setPreAuthKey(searchDTO.getTransactionKey());
		}
        
		/*VerticalLayout verticalLayout = new VerticalLayout(
				viewDetails,pedRequestDetailsProcessList, buildMainLayout(),dynamicLayout,buttonHorLayout);*/
		
		 btnCancel = new Button("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure You want to Cancel ?",
						"Yes", "No", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if(dialog.isCanceled()){
									dialog.close();
								}else if (dialog.isConfirmed()) {
									 fireViewEvent(MenuItemBean.REASSIGN_FIELD_VISIT_REPESENTATION, null);   
								} else {
									dialog.close();
								}
							}
						});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		
		reAssignFvr = new Button("Re-Assign FVR");
		
		dynamicLayout =new HorizontalLayout(btnCancel,reAssignFvr);
		dynamicLayout.setSpacing(true);
		
		addListener();
		
		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		
		mainsplitPanel.setFirstComponent(preauthIntimationDetailsCarousel);
		
		HorizontalLayout hLyout =new HorizontalLayout(viewDetails);
		hLyout.setWidth("100%");
		hLyout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		Panel mainpanel = new Panel("Field Visit Assignment Details");
		mainpanel.setContent(buildLayout());
		
		VerticalLayout verticalLayout = new VerticalLayout(hLyout,mainpanel);
		
		
		mainsplitPanel.setSecondComponent(verticalLayout);

		mainsplitPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainsplitPanel.setSizeFull();
		mainsplitPanel.setHeight("650px");
		setCompositionRoot(mainsplitPanel);

	}
	
	
	@Override
	public void setReferenceData(FieldVisitDTO bean,
			SearchReAssignFieldVisitTableDTO searchTableDTO,
			NewIntimationDto newIntimationDto, ClaimDto claimDto,
			List<ViewFVRDTO> fvrDTOList, Intimation intimation) {
		//this.searchReAssignFieldVisitTableDTO = searchTableDTO;
		this.bean = bean;
		this.newIntimationDto = newIntimationDto;
		this.claimDTO = claimDto;
		this.intimation = intimation;
	}
	
	public VerticalLayout buildLayout(){
		
		
		
		TextArea hospitalAddress = new TextArea("Hospital Name & Address");
		
		TextField state = new TextField("State");
		
		TextField city = new TextField("City");
		
		TextField priority = new TextField("Priority");
		
		TextField allcTo = new TextField("Allocation To");
		
		TextArea fvrTriggerPoints = new TextArea("FVR Trigger Points");

		TextField repName = new TextField("Representative Name");
		
		TextField reptelno = new TextField("Representative Tel no");
		
		TextField repmobno = new TextField("Representative Mob no");
	
		TextArea fvrexecomments = new TextArea("FVR Executive Comments");
		
		TextField diagnosis = new TextField("Diagnosis");
		
		TextArea medicalFvrComment = new TextArea("Medical Approver -FVR Comments");
		
		
		
		if (bean.getExcecutiveComments() != null){
			fvrexecomments.setValue(bean.getExcecutiveComments());
		}
		
		if (bean.getFvrTriggerPoints() != null){
			fvrTriggerPoints.setValue(bean.getFvrTriggerPoints());
		}
		
		if (bean.getDiagnosis() != null){
			diagnosis.setValue(bean.getDiagnosis());
		}
		
		if (bean.getComments() != null){
			fvrexecomments.setValue(bean.getComments());
		}
		
		if (bean.getMobileNo() != null){
			repmobno.setValue(bean.getMobileNo().toString());
		}
		
		if(newIntimationDto != null){
			hospitalAddress.setValue(newIntimationDto.getHospitalDto().getName() +"\n" +newIntimationDto.getHospitalDto().getAddress());
			state.setValue(newIntimationDto.getHospitalDto().getState());
			city.setValue(newIntimationDto.getHospitalDto().getCity());
		}
		
		
		if (bean.getPriority() != null){
			priority.setValue(bean.getPriority());
		}
		
		if (bean.getAllocateTo() != null){
			allcTo.setValue(bean.getAllocateTo().toString());
		}
		
		if (bean.getName() != null){
			repName.setValue(bean.getRepresentativeName());
		}
		
		if (bean.getTelNo() != null){
			reptelno.setValue(bean.getTelNo().toString());
		}
	
		
		hospitalAddress.setReadOnly(true);
		medicalFvrComment.setReadOnly(true);
		diagnosis.setReadOnly(true);
		fvrexecomments.setReadOnly(true);
		repmobno.setReadOnly(true);
		reptelno.setReadOnly(true);
		repName.setReadOnly(true);
		fvrTriggerPoints.setReadOnly(true);
		priority.setReadOnly(true);
		city.setReadOnly(true);
		state.setReadOnly(true);
		
		FormLayout leftForm = new  FormLayout(hospitalAddress,state,city,allcTo,priority,fvrTriggerPoints);
		
		FormLayout rightForm = new  FormLayout(repName,reptelno,repmobno,fvrexecomments,fvrexecomments,diagnosis,medicalFvrComment);
		
		HorizontalLayout mainHori =new HorizontalLayout(leftForm,rightForm);
		mainHori.setSpacing(true);
		
		VerticalLayout mainVerticalLayout = new VerticalLayout(mainHori,dynamicLayout);
//		mainVerticalLayout.setComponentAlignment(dynamicLayout, Alignment.BOTTOM_RIGHT);
		
		return mainVerticalLayout;
		
		
		
	}
	
	@Override
	public void generateFieldBasedReAssignFVRClick(Boolean isChecked) {
		
		dynamicLayout.removeComponent(btnCancel);
		dynamicLayout.setMargin(false);
		
		btnSubmit = new Button("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout buttonHorLayout = new HorizontalLayout(btnSubmit, btnCancel);
		buttonHorLayout.setSpacing(true);
		FormLayout assignFVRLayout = assignFVRLayout();
		
		VerticalLayout mainVertical = new VerticalLayout(assignFVRLayout,buttonHorLayout);
	    
		
		dynamicLayout.addComponent(mainVertical);
		
	
	}
	
	
	FormLayout assignFVRLayout(){
		
		allocTo = new ComboBox("Allocation To");
		priority = new ComboBox("Priority");
		
		representName = new AutocompleteField<TmpFVRDTO>();
		//Vaadin8-setImmediate() representName.setImmediate(true);
		representName.setRequired(true);
		representName.setValidationVisible(false);
		representName.setWidth("180px");
		representName
				.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<TmpFVRDTO>() {

					@Override
					public void onSuggestionPicked(TmpFVRDTO suggestion) {
						handleRepresentativeSelection(suggestion);
					}
				});
		txtTelNo = new TextField("Representative Tel no");
		txtMobileNo = new TextField("Representative Mob no");
		
		btnRepresentativeSearch = new Button();
		FormLayout representativeNameFormLayout = new FormLayout(representName);
		FormLayout btnRepresentativeSearchform = new FormLayout(btnRepresentativeSearch);
		HorizontalLayout representativeNameLayout = new HorizontalLayout(
				representativeNameFormLayout, btnRepresentativeSearchform);
		representativeNameLayout.setWidth("420px");
		btnRepresentativeSearch.setStyleName("link");
		btnRepresentativeSearch.setIcon(new ThemeResource("images/search.png"));
//		representativeNameLayout.setComponentAlignment(btnRepresentativeSearch,
//				Alignment.MIDDLE_LEFT);
//		representativeNameLayout.setComponentAlignment(
//				representativeNameFormLayout, Alignment.MIDDLE_LEFT);
		representativeNameLayout.setCaption("Representative Name");
		
		
		
		
		fvrexecomments = new TextArea("FVR Executive Comments");
		fvrexecomments.setNullRepresentation("");
		fvrTriggerPoints = new TextArea("FVR Trigger Points");
		fvrTriggerPoints.setNullRepresentation("");
		
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO);
		
		BeanItemContainer<SelectValue> fvrPriority = masterService
				.getSelectValueContainer(ReferenceTable.FVR_PRIORITY);
		
		allocTo.setContainerDataSource(selectValueContainer);
		allocTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocTo.setItemCaptionPropertyId("value");
		
		allocTo.setNullSelectionAllowed(false);
		
		
		priority.setContainerDataSource(fvrPriority);
		priority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		priority.setItemCaptionPropertyId("value");
		
		priority.setNullSelectionAllowed(false);
		
		
		if(bean.getPrioritySelect() != null){
			priority.setValue(bean.getPrioritySelect());
			
		}
		
		if(bean.getAllocateTo() != null){
			allocTo.setValue(bean.getAllocateTo());
		}
		

		
		FormLayout form = new FormLayout(allocTo,priority,representativeNameLayout,txtTelNo,txtMobileNo,fvrexecomments,fvrTriggerPoints);
		
		btnRepresentativeSearch.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						ReAssignFieldVisitPresenter.SEARCH_REPRESENTATVIE_NAME,
						null);
			}
		});
		setUpAutoRepresentativeName(representName);
		
		addListenerForReassign();
		 
		return form;
		
	}
	
	@SuppressWarnings("serial")
	public void addListener() {

		reAssignFvr.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * btnAssignFvr.setEnabled(false); btnSkipFvr.setEnabled(true);
				 */
				fireViewEvent(ReAssignFieldVisitPresenter.RE_ASSIGN_FVR, true);
			}
		});
	}
	
	public void addListenerForReassign(){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * btnAssignFvr.setEnabled(false); btnSkipFvr.setEnabled(true);
				 */
	
				if(isValid()){
					
					bean.setExcecutiveComments(fvrexecomments
							.getValue());
					bean.setReasons(fvrexecomments.getValue());
					
					bean.setFvrTriggerPoints(fvrTriggerPoints.getValue());
					
					SelectValue allocationTo = (SelectValue)allocTo.getValue();
					bean.setAllocateTo(allocationTo);
					
					SelectValue selectPriority = (SelectValue)priority.getValue();
					bean.setPrioritySelect(selectPriority);
					
					if (tmpFVRDTO != null) {
						if (null != tmpFVRDTO
								.getRepresentativeName()) {
							bean.setName(tmpFVRDTO
									.getRepresentativeName());
							bean.setRepresentativeName(tmpFVRDTO.getRepresentativeName());
							bean.setRepresentativeCode(tmpFVRDTO
									.getRepresentativeCode());
						}
					}
					
					fireViewEvent(ReAssignFieldVisitPresenter.SUBMIT_BUTTON, bean);
				}
			}
		});
		
//		btnCancel.addClickListener(new Button.ClickListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
//						"Confirmation", "Are you sure You want to Cancel ?",
//						"Yes", "No", new ConfirmDialog.Listener() {
//
//							public void onClose(ConfirmDialog dialog) {
//								if(dialog.isCanceled()){
//									dialog.close();
//								}else if (dialog.isConfirmed()) {
//									 fireViewEvent(MenuItemBean.REASSIGN_FIELD_VISIT_REPESENTATION, null);   
//								} else {
//									dialog.close();
//								}
//							}
//						});
//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
//			}
//		});
		
	}
	
	private void handleRepresentativeSelection(TmpFVRDTO suggestion) {
		if (suggestion.getRepresentativeName() != null) {
			this.tmpFVRDTO = suggestion;
			enableTelNoAndMobileNo();
			representName.setText(suggestion.getRepresentativeName());
			if(suggestion.getPhoneNumber() != null){
				txtTelNo.setValue(suggestion.getPhoneNumber().toString());
			}
			if(suggestion.getMobileNumber() != null){
			txtMobileNo.setValue(suggestion.getMobileNumber().toString());
			}
			disableTelNoAndMobileNo();
		} else {
			Notification.show("Representative Name not available");
		}
	}
	
	
	public void showFVRNotFoundMessage() {
		Notification.show("Field Visit Request, Not Found");
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	public Boolean isValid(){
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		if(representName != null && 
				(representName.getValue() != null && representName.getValue().equalsIgnoreCase(""))){
			eMsg.append("Please Select Representive Name </br>");
			hasError = true;
		}
		
		if(fvrexecomments != null && fvrexecomments.getValue().equalsIgnoreCase("")){
			eMsg.append("Please Enter Fvr Executive Comments </br>");
			hasError = true;
		}
		
		if(fvrTriggerPoints != null && fvrTriggerPoints.getValue().equalsIgnoreCase("")){
			eMsg.append("Please Enter FVR Trigger Points </br>");
			hasError = true;
		}
		
		if(allocTo != null && (allocTo.getValue() == null)){
			eMsg.append("Please Select Allocation To </br>");
			hasError = true;
		}
		if(priority != null && (priority.getValue() == null)){
			eMsg.append("Please Select Priority </br>");
			hasError = true;
		}


		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
		}
		return true;
		
	}
	
	private void enableTelNoAndMobileNo() {
		txtTelNo.setEnabled(true);
		txtTelNo.setEnabled(true);
		txtMobileNo.setReadOnly(false);
		txtMobileNo.setReadOnly(false);
	}

	private void disableTelNoAndMobileNo() {
		txtTelNo.setEnabled(false);
		txtTelNo.setEnabled(false);
		txtMobileNo.setReadOnly(true);
		txtMobileNo.setReadOnly(true);
	}
	
	private void setUpAutoRepresentativeName(AutocompleteField<TmpFVRDTO> search) {
		search.setQueryListener(new AutocompleteQueryListener<TmpFVRDTO>() {
			@Override
			public void handleUserQuery(AutocompleteField<TmpFVRDTO> field,
					String query) {
				List<TmpFVRDTO> tmpFVRDTOList = fieldVisitSerivice
						.getTmpFVRDetails(field, query);
				if (tmpFVRDTOList != null && !tmpFVRDTOList.isEmpty()) {
					for (TmpFVRDTO tmpFVRDTO : tmpFVRDTOList) {
						field.addSuggestion(tmpFVRDTO,
								tmpFVRDTO.getRepresentativeName());
					}
				} else {
					txtMobileNo.setValue("");
					txtTelNo.setValue("");
				}
			}
		});
	}
	
	@Override
	public void initRepresentativeSearch(
			BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer,BeanItemContainer<SelectValue> assignToConainer,BeanItemContainer<SelectValue> fvrPriorityContainer) {
		fieldVisitPageRepresentativeNameSearchUI.initRepresentativeNameSearch(this.intimation,SHAConstants.REASSIGN_FVR);
		fieldVisitPageRepresentativeNameSearchUI
				.setReferenceDataForStateAndAllocationTo(stateContainer,
						allocationToContainer);
		popup = new com.vaadin.ui.Window();
		popup.setCaption("Search FVR Representative");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(fieldVisitPageRepresentativeNameSearchUI);
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
	
	@SuppressWarnings("serial")
	@Override
	public void result() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),
				"Claim record saved successfully !!!",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
						      fireViewEvent(MenuItemBean.REASSIGN_FIELD_VISIT_REPESENTATION, null);
							
						} else {
							dialog.close();
						}
					}
				});
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		dialog.getCancelButton().setVisible(false);
	}
	
	
	
	@Override
	public void setRepresentativeDetails(TmpFVRDTO tmpFVRDTOList) {
		if (tmpFVRDTOList != null) {
			tmpFVRDTO.setRepresentativeCode(tmpFVRDTOList
					.getRepresentativeCode());
			tmpFVRDTO.setRepresentativeName(tmpFVRDTOList
					.getRepresentativeName());
			tmpFVRDTO.setMobileNumber(tmpFVRDTOList.getMobileNumber());
			tmpFVRDTO.setPhoneNumber(tmpFVRDTOList.getPhoneNumber());
			tmpFVRDTO.setKey(tmpFVRDTOList.getKey());
		}
		representName.setText(tmpFVRDTOList.getRepresentativeName());
		enableTelNoAndMobileNo();
		
		if(tmpFVRDTOList.getPhoneNumber() != null){
			txtTelNo.setValue(tmpFVRDTOList.getPhoneNumber());
		}
		if(tmpFVRDTOList.getMobileNumber() != null){
			txtMobileNo.setValue(tmpFVRDTOList.getMobileNumber());
		}
		
		disableTelNoAndMobileNo();
		ReAssignFieldVisitViewImpl.popup.close();
	}
	

}
