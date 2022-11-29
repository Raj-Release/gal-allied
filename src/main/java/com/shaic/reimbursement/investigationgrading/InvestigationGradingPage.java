package com.shaic.reimbursement.investigationgrading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeTableDTO;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.InvestigatorTriggerPointsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InvestigationGradingPage extends ViewComponent{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtRequestingRole;
	
	private TextField investigationInitiatedDate;
	
	private TextField txtType;

	private TextField txtRequestorIdOrName;

	private ComboBox cmbAllocationTo;
	
	private ComboBox cmbReasonforIniInv;

	private ComboBox cmbAllocationToForState;

	private TextArea txtInvestigatingApprovedRemarks;

	private TextArea txtReasonForReferring;

	private TextArea txtTriggerPointsForFocus;

	private FormLayout requestingRoleFormLayout;

	private FormLayout reasonFormLayout;
	
	private FormLayout gradingRemarksLayout;

	private HorizontalLayout requestingHorizontalLayout;

	private ComboBox cmbState;

	private ComboBox cmbCity;

	// private ComboBox cmbAllocationTo;

	private FormLayout stateLayout;

	private ComboBox cmbInvestigatorName;
	
	private TextField txtInvestigatorName;

	private TextField txtInvestigatorTelNo;

	private TextField txtInvestigatorMobileNo;

	private TextArea txtClaimBackgroundDetails;
	
	private TextArea txtFactsOfTheCase;

	private TextArea txtInvestigationTriggerPoints;

	private FormLayout investigationFormLayout;

	private VerticalLayout mainLayout;

	//private GWizard wizard;

	private BeanFieldGroup<AssignInvestigatorDto> binder;

	private AssignInvestigatorDto bean;

	/*private BeanItemContainer<SelectValue> stateContainer;

	private BeanItemContainer<SelectValue> cityContainer;

	private BeanItemContainer<SelectValue> investigatorContainer;	
	
	
	private Boolean flag;*/
	
	private ComboBox cmbGradingCategory;
	
	private TextArea txtInvestigationGradingRemarks;	
	
	
	@Inject
	private InvestigatorTriggerPointsTable searchResultTable;
	
//	@Inject
//	private RevisedDraftInvTriggerPointsTable searchResultTable;
	
	Map<String, Object> draftrefData = null;


	private void initBinder() {
		this.binder = new BeanFieldGroup<AssignInvestigatorDto>(
				AssignInvestigatorDto.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void init(AssignInvestigatorDto bean, GWizard wizard) {
		this.bean = bean;
		//this.wizard = wizard;
	}

	public Component getContent() {
		//this.wizard = wizard;
		
		// R1152
		if (!this.bean.getIsGeoSame()) {
			getGeoBasedOnCPU();
		}
		else if(this.bean.getClaimCount() >3){
			alertMessageForClaimCount(this.bean.getClaimCount());
		}
		else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		}
		else if(!bean.getSuspiciousPopupMap().isEmpty()){
			suspiousPopupMessage();
		}
		else if(!bean.getNonPreferredPopupMap().isEmpty()){
			nonPreferredPopupMessage();
		}
		else if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
		}
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		initBinder();
		draftrefData  = new HashMap<String, Object>();
		txtRequestingRole = new TextField("Requesting Role");
		txtRequestingRole.setValue(bean.getRequestingRole());
		txtRequestingRole.setEnabled(false);
		txtRequestorIdOrName = new TextField("Requestor ID/Name");
		txtRequestorIdOrName.setValue(bean.getRequestroIdOrName());
		txtRequestorIdOrName.setEnabled(false);
		
		txtType = new TextField("Type");
		txtType.setValue(bean.getInvestigationType());
		txtType.setEnabled(false);
		
		txtRequestingRole.setEnabled(false);
		txtRequestorIdOrName.setEnabled(false);

		cmbAllocationTo = new ComboBox("Allocation to");
	//	cmbAllocationTo.setRequired(true);
		cmbAllocationTo.setEnabled(false);
		
		
		cmbReasonforIniInv = new ComboBox("Reason for Initiating Investigation");
		//	cmbAllocationTo.setRequired(true);
		cmbReasonforIniInv.setEnabled(false);
		
		
		cmbAllocationToForState = new ComboBox("Allocation to");
	//	cmbAllocationToForState.setRequired(true);
		cmbAllocationToForState.setEnabled(true);
		
		investigationInitiatedDate = binder.buildAndBind("Investigation Initiated Date","dateOfInvestigationValue",TextField.class);
		investigationInitiatedDate.setEnabled(false);

		txtInvestigatingApprovedRemarks = new TextArea(
				"Investigation Approved\nRemarks");
		txtInvestigatingApprovedRemarks.setId("invApproveRemarks");
		txtInvestigatingApprovedRemarks.setData(bean);
		handleRemarksPopup(txtInvestigatingApprovedRemarks, null);
		txtInvestigatingApprovedRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		
	//	txtInvestigatingApprovedRemarks.setRequired(true);
		

		txtReasonForReferring = new TextArea("Reason for Refering");
//		txtReasonForReferring.setMaxLength(100);
	//	txtReasonForReferring.setRequired(true);
		txtReasonForReferring.setId("invRefReason");
		txtReasonForReferring.setData(bean);
		handleRemarksPopup(txtReasonForReferring, null);
		txtReasonForReferring.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		txtTriggerPointsForFocus = new TextArea("Trigger points to focus");
//		txtTriggerPointsForFocus.setMaxLength(100);
		//txtTriggerPointsForFocus.setRequired(true);
		txtTriggerPointsForFocus.setId("TrigPointsFocus");
		txtTriggerPointsForFocus.setData(bean);
		handleRemarksPopup(txtTriggerPointsForFocus, null);
		txtTriggerPointsForFocus.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		
		requestingRoleFormLayout = new FormLayout(txtType,txtRequestingRole,
				txtRequestorIdOrName, cmbAllocationTo,
				txtInvestigatingApprovedRemarks);
		
		requestingRoleFormLayout.setMargin(true);
		reasonFormLayout = new FormLayout(investigationInitiatedDate,cmbReasonforIniInv,txtReasonForReferring,
				txtTriggerPointsForFocus);
		//reasonFormLayout.setMargin(true);
		requestingHorizontalLayout = new HorizontalLayout(
				requestingRoleFormLayout, reasonFormLayout);
		requestingHorizontalLayout.setSpacing(true);
		//requestingHorizontalLayout.setHeight("150px");
		//requestingHorizontalLayout.setMargin(true);

		cmbState = binder.buildAndBind("State", "stateSelectValue",
				ComboBox.class);
		cmbState.setRequired(true);

		cmbCity = binder
				.buildAndBind("City", "citySelectValue", ComboBox.class);
//		cmbCity.setRequired(true);

		stateLayout = new FormLayout(cmbState, cmbCity, cmbAllocationToForState);
		stateLayout.setSpacing(true);
		stateLayout.setMargin(true);

		//VerticalLayout stateVerticalLayout = new VerticalLayout(stateLayout);
	//	stateVerticalLayout.setSpacing(false);
		//stateVerticalLayout.setMargin(true);

		cmbInvestigatorName = binder.buildAndBind("Investigator Name",
				"investigatorNameListSelectValue", ComboBox.class);
	//	cmbInvestigatorName.setRequired(true);
		
		txtInvestigatorName = binder.buildAndBind("Investigator Name","investigatorName",TextField.class);
	//	txtInvestigatorName.setRequired(true);
		txtInvestigatorName.setEnabled(false);
		

		txtInvestigatorTelNo = binder.buildAndBind("Investigator Tel No",
				"investigatorTelNo", TextField.class);
//		txtInvestigatorTelNo.setRequired(true);
		txtInvestigatorTelNo.setEnabled(false);

		txtInvestigatorMobileNo = binder.buildAndBind("Investigator Mobile No",
				"investigatorMobileNo", TextField.class);
//		txtInvestigatorMobileNo.setRequired(true);
		txtInvestigatorMobileNo.setEnabled(false);

		txtClaimBackgroundDetails = binder.buildAndBind(
				"Claim Background Details", "claimBackgroundDetails",
				TextArea.class);
		txtClaimBackgroundDetails.setMaxLength(4000);
		txtClaimBackgroundDetails.setWidth("500px");
		txtClaimBackgroundDetails.setId("clmBackgroundDetails");
		txtClaimBackgroundDetails.setData(bean);
		handleRemarksPopup(txtClaimBackgroundDetails, null);
		txtClaimBackgroundDetails.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		txtClaimBackgroundDetails.setReadOnly(true);
	//	txtClaimBackgroundDetails.setRequired(true);
		
		txtFactsOfTheCase = binder.buildAndBind("Facts of the Case","factsOfTheCase",TextArea.class);
		txtFactsOfTheCase.setMaxLength(4000);
		txtFactsOfTheCase.setWidth("500px");
	//	txtFactsOfTheCase.setRequired(true);
		txtFactsOfTheCase.setId("invFactsCase");
		txtFactsOfTheCase.setData(bean);
		handleRemarksPopup(txtFactsOfTheCase, null);
		txtFactsOfTheCase.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		txtFactsOfTheCase.setReadOnly(true);
		
		txtInvestigationTriggerPoints = binder.buildAndBind(
				"Investigation Trigger Points", "investigationTriggerPoints",
				TextArea.class);
		txtInvestigationTriggerPoints.setMaxLength(4000);
//		txtInvestigationTriggerPoints.setRequired(true);
		txtInvestigationTriggerPoints.setWidth("500px");
		txtInvestigationTriggerPoints.setId("invtrigPoints");
		txtInvestigationTriggerPoints.setData(bean);
		handleRemarksPopup(txtInvestigationTriggerPoints, null);
		txtInvestigationTriggerPoints.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		investigationFormLayout = new FormLayout(txtInvestigatorName,
				txtInvestigatorTelNo, txtInvestigatorMobileNo,
				txtClaimBackgroundDetails, txtFactsOfTheCase);
		investigationFormLayout.setSpacing(false);
		investigationFormLayout.setMargin(true);
		//investigationFormLayout.setHeight("240px");

		VerticalLayout investigationVerticalLayout = new VerticalLayout(
				investigationFormLayout);
		investigationVerticalLayout.setSpacing(false);
		investigationVerticalLayout.setMargin(false);
			
		searchResultTable.init("Investigator Trigger Points",false,false);
		
//		searchResultTable.init(false);
		
		cmbGradingCategory = new ComboBox("Grading Category");
		cmbGradingCategory.setRequired(true);
		cmbGradingCategory.setEnabled(true);
		
		txtInvestigationGradingRemarks =binder.buildAndBind(
				"Investigation Grading Remarks", "gradingRemarks",TextArea.class); 
		txtInvestigationGradingRemarks.setMaxLength(4000);
	//	txtInvestigationGradingRemarks.setRequired(true);
		txtInvestigationGradingRemarks.setEnabled(true);
		txtInvestigationGradingRemarks.setId("invGradingRemarks");
		txtInvestigationGradingRemarks.setData(bean);
		handleRemarksPopup(txtInvestigationGradingRemarks, null);
		txtInvestigationGradingRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		gradingRemarksLayout = new FormLayout(cmbGradingCategory,txtInvestigationGradingRemarks);
		VerticalLayout investigationgradingRemarksLayout = new VerticalLayout(
				gradingRemarksLayout);
		investigationgradingRemarksLayout.setSpacing(false);
		investigationgradingRemarksLayout.setMargin(false);

		mainLayout = new VerticalLayout(requestingHorizontalLayout,investigationVerticalLayout,new Label("Investigator Trigger Points"),searchResultTable,gradingRemarksLayout);
		addListener();
		setFormData(bean);
		// setCompositionRoot(mainLayout);
		setAssignInvestigatorData(bean);
		return mainLayout;
	}
	

	private void setFormData(AssignInvestigatorDto assignInvestigatorDto) {
		if (assignInvestigatorDto != null) {
			this.bean = assignInvestigatorDto;
			BeanItemContainer<SelectValue> allocationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			allocationContainer.addAll(this.bean.getAllocationToSelectValueList());
			cmbAllocationTo.setContainerDataSource(allocationContainer);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			
			if(this.bean.getAllocationToSelectValue() != null){
				cmbAllocationTo.setValue(this.bean.getAllocationToSelectValue());
			}
			
			BeanItemContainer<SelectValue> reasonForInitiateInvestContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			reasonForInitiateInvestContainer.addAll(this.bean.getReasonForInitiatingInvestSelectValueList());
			cmbReasonforIniInv.setContainerDataSource(reasonForInitiateInvestContainer);
			cmbReasonforIniInv.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReasonforIniInv.setItemCaptionPropertyId("value");
			
			if(this.bean.getReasonForInitiatingInvestSelectValue() != null){
				cmbReasonforIniInv.setValue(this.bean.getReasonForInitiatingInvestSelectValue());
			}

			BeanItemContainer<SelectValue> gradingContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			gradingContainer.addAll(this.bean.getGradingCategoryList());
			cmbGradingCategory.setContainerDataSource(gradingContainer);
			cmbGradingCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbGradingCategory.setItemCaptionPropertyId("value");
			
			cmbAllocationToForState.setContainerDataSource(allocationContainer);
			cmbAllocationToForState.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationToForState.setItemCaptionPropertyId("value");
			
			if(this.bean.getAllocationToSelectValue() != null){
				cmbAllocationToForState.setValue(this.bean.getAllocationToSelectValue());
			}
			
			txtInvestigatingApprovedRemarks.setReadOnly(false);
			txtInvestigatingApprovedRemarks
					.setValue(assignInvestigatorDto
							.getInvestiationApprovedRemarks() != null ? assignInvestigatorDto
							.getInvestiationApprovedRemarks() : "");
			txtInvestigatingApprovedRemarks.setReadOnly(true);
			
			txtReasonForReferring.setReadOnly(false);
			txtReasonForReferring.setValue(assignInvestigatorDto
					.getReasonForRefering() != null ? assignInvestigatorDto
					.getReasonForRefering() : "");
			txtReasonForReferring.setReadOnly(true);
			
			txtTriggerPointsForFocus.setReadOnly(false);
			txtTriggerPointsForFocus.setValue(assignInvestigatorDto
					.getTriggerPointsForFocus() != null ? assignInvestigatorDto.getTriggerPointsForFocus() : "");
			txtTriggerPointsForFocus.setReadOnly(true);
			
//			txtTriggerPointsForFocus.setDescription(assignInvestigatorDto
//					.getTriggerPointsForFocus() != null ? assignInvestigatorDto
//					.getTriggerPointsForFocus() : "");
			
			
			BeanItemContainer<SelectValue> state = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			state.addAll(assignInvestigatorDto.getStateList());
			//this.stateContainer = state;
			cmbState.setContainerDataSource(state);
//			BeanItemContainer<SelectValue> nameContainer = assignInvestigatorDto.getInvestigatorNameContainer();
			BeanItemContainer<SelectValue> nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			nameContainer.addAll(assignInvestigatorDto.getInvestigatorNameContainerList());
			cmbInvestigatorName.setContainerDataSource(nameContainer);
			cmbInvestigatorName.setItemCaptionPropertyId("value");
		}
	}

	public void setAssignInvestigatorData(
			AssignInvestigatorDto assignInvestigatorDto) {
		if (assignInvestigatorDto != null) {
			if (assignInvestigatorDto.getStateList() != null
					&& assignInvestigatorDto
							.getInvestigatorNameList() != null) {
				this.cmbState.setValue(assignInvestigatorDto
						.getStateSelectValue());
				this.cmbCity.setValue(assignInvestigatorDto
						.getCitySelectValue());
				this.cmbInvestigatorName.setValue(assignInvestigatorDto
						.getInvestigatorNameListSelectValue());
				this.txtInvestigatorName.setValue(assignInvestigatorDto.getInvestigatorName());
				this.investigationInitiatedDate.setValue(this.bean.getDateOfInvestigationValue());
				this.txtFactsOfTheCase.setReadOnly(false);
				this.txtFactsOfTheCase.setValue(this.bean.getFactsOfCase());
				this.txtFactsOfTheCase.setReadOnly(true);
			}
		}
		
		if(null != searchResultTable)
		{
			List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList = this.bean.getInvestigatorTriggerPointsList();
			for(int i =0 ; i<investigatorTriggerPointsList.size() ; i++)
			{
				DraftTriggerPointsToFocusDetailsTableDto draftTriggerPointsToFocusDetailsTableDto = investigatorTriggerPointsList.get(i);
				draftTriggerPointsToFocusDetailsTableDto.setSerialNumber(i);
//				searchResultTable.addBeanToList(draftTriggerPointsToFocusDetailsTableDto);
			}
			searchResultTable.setTableList(investigatorTriggerPointsList);
			//investigationTriggerPoints.setTableList(this.bean.getInvestigatorTriggerPointsList());
		}
	}

	private void addListener() {
		cmbState.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				/*try {
					//SelectValue state = (SelectValue) cmbState.getValue();
					//Long state_Id = state.getId();
//					fireViewEvent(AssignInvestigatorPresenter.SEARCH_CITY,
//							state_Id);
				} catch (Exception e) {
					// cmbCity.setContainerDataSource(null);
				}*/
			}
		});

		cmbCity.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
//					if (cmbState.getValue() != null							
//							&& cmbAllocationToForState != null
//							&& cmbAllocationToForState.getValue() != null) {
//						SelectValue state = (SelectValue) cmbState.getValue();
//						Long state_Id = state.getId();
//						SelectValue city = (SelectValue) cmbCity.getValue();
//						Long city_Id = city.getId();
//						Long category_Id = bean.getAllocationToId();
//						Map<String, Object> searchRepresentative = new HashMap<String, Object>();
//						searchRepresentative.put("stateId", state_Id);
//						searchRepresentative.put("cityId", city_Id);
//						searchRepresentative.put("catgoryId", category_Id);
//						fireViewEvent(
//								AssignInvestigatorPresenter.SEARCH_REPRESENTATIVE,
//								searchRepresentative);
//					} else {
//						Notification
//								.show("Please select State, City, Allocation To fields");
//					}
				} catch (Exception e) {

				}
			}
		});	

		cmbInvestigatorName
				.addValueChangeListener(new Property.ValueChangeListener() {

					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						try {
							SelectValue investigator = (SelectValue) cmbInvestigatorName
									.getValue();
							fireViewEvent(
									InvestigationGradingPagePresenter.SET_REPRESENTATIVE,
									investigator);
						} catch (Exception e) {
							// cmbCity.setContainerDataSource(null);
							// cmbAllocationTo.setValue(null);
							cmbInvestigatorName.setValue(null);
						}
					}
				});
	}

	public void setInvestigatorData(
			List<SearchRepresentativeTableDTO> tmpfvrDTOList) {
		BeanItemContainer<SelectValue> investigatorName = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (SearchRepresentativeTableDTO searchRepresentativeTableDTO : tmpfvrDTOList) {
			SelectValue selectValue = new SelectValue();
			selectValue.setId(searchRepresentativeTableDTO.getKey());
			selectValue.setValue(searchRepresentativeTableDTO
					.getRepresentativeName());
			investigatorName.addBean(selectValue);
		}
		cmbInvestigatorName.setContainerDataSource(investigatorName);
		//this.investigatorContainer = investigatorName;
	}

	public void setInvestigatorDetails(SearchRepresentativeTableDTO investigator) {
		txtInvestigatorMobileNo
				.setValue(investigator.getRepsentativeMobileNo());
		txtInvestigatorTelNo
				.setValue(investigator.getRepresentativeContactNo());
	}

	/*public boolean validatePage() {
		try {
			
			this.binder.commit();
			if(cmbGradingCategory != null && cmbGradingCategory.getValue() != null){
				SelectValue selected = (SelectValue) cmbGradingCategory.getValue();
				this.bean.setGradingCategorySelectValue(selected);
			}
			return true;
		} catch (CommitException e) {
			Notification.show("Please Select Grading Category");
			return false;
			
		}		
	}*/

	public boolean validatePage() {		
			
			if(cmbGradingCategory != null && cmbGradingCategory.getValue() != null){
				SelectValue selected = (SelectValue) cmbGradingCategory.getValue();
				this.bean.setGradingCategorySelectValue(selected);
				if(txtInvestigationGradingRemarks != null){
					this.bean.setGradingRemarks(txtInvestigationGradingRemarks.getValue());
				}
				if(txtFactsOfTheCase != null){
					this.bean.setFactsOfCase(txtFactsOfTheCase.getValue());
					this.bean.setFactsOfTheCase(txtFactsOfTheCase.getValue());
					
				}
				if(txtClaimBackgroundDetails != null){
					this.bean.setClaimBackgroundDetails(txtClaimBackgroundDetails.getValue());
				}
				return true;
			}
			
			else
			{
				Label label = new Label("Please Select Grading Category", ContentMode.HTML);
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
				return false;
			}			
		
	}
	
	public void setCityContainer(
			BeanItemContainer<SelectValue> citySelectValueContainer) {
		if (!citySelectValueContainer.getItemIds().isEmpty()) {
			cmbCity.setContainerDataSource(citySelectValueContainer);
			cmbCity.setEnabled(true);
			//this.cityContainer = citySelectValueContainer;
		}
	}
	
	public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
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
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				AssignInvestigatorDto assignInvestigatorDto = (AssignInvestigatorDto) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
//				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);

				if(("invGradingRemarks").equalsIgnoreCase((txtFld.getId()))){
					txtArea.setRows(25);
					txtArea.setReadOnly(false);
				}
				else{

					String remarksValue = "";	
						
					if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = assignInvestigatorDto.getInvestiationApprovedRemarks() != null ? assignInvestigatorDto.getInvestiationApprovedRemarks() : "";
//						txtArea.setRows(assignInvestigatorDto.getInvestiationApprovedRemarks() != null ? (assignInvestigatorDto.getInvestiationApprovedRemarks().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getInvestiationApprovedRemarks().length()/80)%25)+1) : 25);
					}
					if(("invRefReason").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = assignInvestigatorDto.getReasonForRefering() != null ? assignInvestigatorDto.getReasonForRefering() : "";
//						txtArea.setRows(assignInvestigatorDto.getReasonForRefering() != null ? (assignInvestigatorDto.getReasonForRefering().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getReasonForRefering().length()/80)%25)+1) : 25);
					}	
					if(("TrigPointsFocus").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = assignInvestigatorDto.getTriggerPointsForFocus() != null ? assignInvestigatorDto.getTriggerPointsForFocus() : "";
//						txtArea.setRows(assignInvestigatorDto.getTriggerPointsForFocus() != null ? (assignInvestigatorDto.getTriggerPointsForFocus().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getTriggerPointsForFocus().length()/80)%25)+1) : 25);
					}	
					if(("invTrigPoints").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = assignInvestigatorDto.getInvestigationTriggerPoints() != null ? assignInvestigatorDto.getInvestigationTriggerPoints() : "";
//						txtArea.setRows(assignInvestigatorDto.getInvestigationTriggerPoints() != null ? (assignInvestigatorDto.getInvestigationTriggerPoints().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getInvestigationTriggerPoints().length()/80)%25)+1) : 25);
					}
					if(("invFactsCase").equalsIgnoreCase(txtFld.getId())){
						remarksValue = assignInvestigatorDto.getFactsOfCase() != null ? assignInvestigatorDto.getFactsOfCase() : "";
//						txtArea.setRows(assignInvestigatorDto.getFactsOfCase() != null ? (assignInvestigatorDto.getFactsOfCase().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getFactsOfCase().length()/80)%25)+1) : 25);
					}
					if(("clmBackgroundDetails").equalsIgnoreCase(txtFld.getId())){
						remarksValue = assignInvestigatorDto.getClaimBackgroundDetails() != null ? assignInvestigatorDto.getClaimBackgroundDetails() : "";
//						txtArea.setRows(assignInvestigatorDto.getClaimBackgroundDetails() != null ? (assignInvestigatorDto.getClaimBackgroundDetails().length()/80 >= 25 ? 25 : ((assignInvestigatorDto.getClaimBackgroundDetails().length()/80)%25)+1) : 25);
					}				
					
					txtArea.setReadOnly(true);

//					String splitArray[] = remarksValue.split("\n");
					String splitArray[] = remarksValue.split("[\n*|.*]");
					
					if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
						txtArea.setRows(25);
					}
					else{
						txtArea.setRows(splitArray.length);
					}
				}
				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if(("invGradingRemarks").equalsIgnoreCase(txtFld.getId())){
							
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
				
				if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Approved Remarks";
				}
				
				if(("invRefReason").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Reason for Refering";
				}	
				
				if(("TrigPointsFocus").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Trigger points to focus";					
				}	
				
				if(("invTrigPoints").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Trigger Points";
				}	
				
				if(("invFactsCase").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Facts of the Case";
				}
				
				if(("invGradingRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Grading Remarks";
				}
				
				if(("clmBackgroundDetails").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Claim Background Details";
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
	
private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		
//		Button alertIcon = new Button("");
//		alertIcon.setIcon(new ThemeResource("images/alert.png"));
//		alertIcon.setEnabled(false);
//        alertIcon.setStyleName("ling");
		
//		alertIcon.addStyleName(ValoTheme.BUTTON_BORDERLESS);

   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
		
   		if(this.bean.getClaimCount() >2){
	   		successLabel = new Label(
					"<b style = 'color: black;'>"+msg+"<br>"
							+ additionalMessage+"</b>",
					ContentMode.HTML);
   		}
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor,homeButton);
//   		firstForm.setComponentAlignment(mainHor, Alignment.MIDDLE_CENTER);
   		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//   		firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);
		
		if(this.bean.getClaimCount() > 3){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		else if(!bean.getSuspiciousPopupMap().isEmpty()){
			suspiousPopupMessage();
		}
		
//		panel.setHeight("143px");
		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
//		popup.setCaption("Alert");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();
				
				if(bean.getIsPEDInitiated()) {
					alertMessageForPED();
				}
				else if(!bean.getSuspiciousPopupMap().isEmpty()){
					suspiousPopupMessage();
				}
				else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				} 
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
public Boolean alertMessageForPED() {
		Label successLabel = new Label(
			"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
			ContentMode.HTML);
		//final Boolean isClicked = false;
	Button homeButton = new Button("OK");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
	layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	/*HorizontalLayout hLayout = new HorizontalLayout(layout);
	hLayout.setMargin(true);
	hLayout.setStyleName("borderLayout");*/

	final ConfirmDialog dialog = new ConfirmDialog();
//	dialog.setCaption("Alert");
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
			bean.setIsPEDInitiated(false);
			if(!bean.getSuspiciousPopupMap().isEmpty()){
				suspiousPopupMessage();
			}
			else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
			} 
			
		}
	});
	
	return true;
}

public void suspiousPopupMessage() {
	  final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				}
				
			}
		});
		
		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
		   layout.addComponent(label);
		   layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
public void nonPreferredPopupMessage() {
	  final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		okButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				} 
			}
		});
		
		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getNonPreferredPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
		   layout.addComponent(label);
		   layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
public void get64VbChequeStatusAlert() {
		Label successLabel = new Label(
			"<b style = 'color: red;'>" + SHAConstants.VB64STATUSALERT + "</b>",
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
//	dialog.setCaption("Alert");
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);

	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
//			 bean.setIsPopupMessageOpened(true);

//				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				} 
		}
	});
	
}
public void poupMessageForProduct() {
	  final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				}
			}
		});
		
		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			if(entry.getKey().equals(SHAConstants.BREAK_INSURANCE_MESSAGE)){
				layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
		}
		layout.addComponent(okButton);
		layout.setMargin(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void getGeoBasedOnCPU() {
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
	
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
	
			homeButton.addClickListener(new ClickListener() {
	
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					
					if(bean.getClaimCount() >3){
						alertMessageForClaimCount(bean.getClaimCount());
					}
					else if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					}
					else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
					}
					 
				}
			});
		}
}