package com.shaic.claim.fss.filedetail;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.PDIntimationPremia;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 *
 */
public class ProcessDataEntryPage extends ViewComponent {
	
	private Panel fileDetailsPanel;
	
	private Panel rrcViewPanel;
	
	private TextField txtClientName;
	
	private	ComboBox storageLocation;
	
	private ComboBox rackNo;
	
	private ComboBox shelfNo;
	
	private TextField txtClaimNo;
	
	private TextField txtYear;
	
	private TextField txtPatientName;
	
	private TextField txtAlmirahNo;
	
	//private TextField txtAdditionalShelfNo;
	
	private OptionGroup checkStatus;
	
	private CheckBox rejection;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private SearchDataEntryTableDTO bean;
	
	private BeanFieldGroup<SearchDataEntryTableDTO> binder;
	
	@Inject
	private Instance<ChequeDetailsListenerForDataEntry> chequeDetailsListenerTable;
	
	private ChequeDetailsListenerForDataEntry chequeDetailsListenerTableObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String,Object> containerMap;
	
	private BeanItemContainer<SelectValue> locationList;
	
	private BeanItemContainer<SelectValue> rackList;
	
	private BeanItemContainer<SelectValue> shelfList;
	
	private TextField txtClaimSeqNo;
	
	private TextField txtBundleNo;
	 
	public void init(SearchDataEntryTableDTO bean) {
		this.bean = bean;
	
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<SearchDataEntryTableDTO>(SearchDataEntryTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public void initPresenter(String presenterString) {
		
	}
	
	public Component getContent() {
		
		initBinder();
		this.containerMap = bean.getDataSourcesMap();
		
		VerticalLayout verticalLayout = new VerticalLayout(buildFileDetailsLayout(), buildChequeDetailsPanel());
	
		verticalLayout.setSpacing(false);
		verticalLayout.setMargin(false);
		
		rrcViewPanel = new Panel();
		//rrcViewPanel.setSizeFull();
		rrcViewPanel.setHeight("450px");
		rrcViewPanel.setContent(verticalLayout);
		addListener();
		showOrHideValidation(false);
		//setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}
	
	private Panel buildChequeDetailsPanel() {

		ChequeDetailsListenerForDataEntry chequeDetailsTableInstance = chequeDetailsListenerTable
				.get();
		chequeDetailsTableInstance.init(this.bean,
				SHAConstants.ADD_DATA_ENTRY);
		this.chequeDetailsListenerTableObj = chequeDetailsTableInstance;
		Panel chequeDetailsPanel = new Panel();
		chequeDetailsPanel.setContent(chequeDetailsTableInstance);
		chequeDetailsPanel.setSizeFull();
		chequeDetailsPanel.setHeight("150px");
		return chequeDetailsPanel;

	}
	
	private Panel  buildFileDetailsLayout()
	{
		txtClientName = (TextField)binder.buildAndBind("Client","client",TextField.class);
		txtClientName.setEnabled(false);;
		txtClientName.setRequired(true);
		storageLocation = (ComboBox) binder.buildAndBind("Storage Location",
				"selectLocation", ComboBox.class);
		storageLocation.setRequired(true);
		locationList = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.DATA_ENTRY_LOCATION_CONTAINER);
		storageLocation.setContainerDataSource(locationList);
		storageLocation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		storageLocation.setItemCaptionPropertyId("value");
		
		if (this.bean.getSelectLocation() != null) {
			this.storageLocation.setValue(this.bean.getSelectLocation());
		}
		
		rackNo = (ComboBox) binder.buildAndBind("Rack No",
				"selectRack", ComboBox.class);
		rackNo.setRequired(true);
		if(containerMap.get(SHAConstants.DATA_ENTRY_RACK_CONTAINER) != null){
			rackList = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.DATA_ENTRY_RACK_CONTAINER);
			rackNo.setContainerDataSource(rackList);
		}
		rackNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rackNo.setItemCaptionPropertyId("value");
		
		if (this.bean.getSelectRack() != null) {
			this.rackNo.setValue(this.bean.getSelectRack());
		}
		
		txtClaimNo = (TextField)binder.buildAndBind("Claim No","claimNo",TextField.class);
		txtClaimNo.setEnabled(false);
		
		txtClaimSeqNo = (TextField)binder.buildAndBind("Claim (Seq) No","claimSeqNo",TextField.class);
		txtClaimSeqNo.setRequired(true);
		/*CSValidator validator2 = new CSValidator();
		validator2.extend(txtClaimSeqNo);
		validator2.setRegExp("^[0-9]*$");
		validator2.setPreventInvalidTyping(true);
		*/
		txtPatientName = (TextField)binder.buildAndBind("Patient Name","patientName",TextField.class);
		txtPatientName.setRequired(true);
		txtAlmirahNo = (TextField)binder.buildAndBind("X Ray / Scan Film Storage Almirah No","almirahNo",TextField.class);
		
		checkStatus = (OptionGroup) binder.buildAndBind(
				"File Status", "isCheckInOutStatus", OptionGroup.class);
		checkStatus.setRequired(true);
		checkStatus.addItems(getRadioButtonOptions());
		checkStatus.setItemCaption(true, "Check In");
		checkStatus.setItemCaption(false, "Check Out");
		checkStatus.setStyleName("horizontal");
		
		if(this.bean.getIsCheckInOutStatus() != null){
			checkStatus.setValue(this.bean.getIsCheckInOutStatus());
		}
		
		TextField dummyText = new TextField();
		dummyText.setEnabled(false);
		dummyText.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText.setHeight("100%");
		
		/*TextField dummyText1 = new TextField();
		dummyText1.setEnabled(false);
		dummyText1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText1.setHeight("100%");*/
		
		TextField dummyText2 = new TextField();
		dummyText2.setEnabled(false);
		dummyText2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText2.setHeight("100%");
		
		txtYear = (TextField)binder.buildAndBind("Year","year",TextField.class);
		txtYear.setRequired(true);
		txtYear.setMaxLength(4);
		CSValidator validator = new CSValidator();
		validator.extend(txtYear);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		txtYear.setNullRepresentation("");
		/*txtAdditionalShelfNo = (TextField)binder.buildAndBind("Shelf No","addlShelfNo",TextField.class);
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtAdditionalShelfNo);
		validator1.setRegExp("^[0-9]*$");
		validator1.setPreventInvalidTyping(true);
		txtAdditionalShelfNo.setNullRepresentation("");*/
		
		txtBundleNo = (TextField)binder.buildAndBind("Bundle No","bundleNo",TextField.class);
		txtBundleNo.setMaxLength(15);
		txtBundleNo.setNullRepresentation("");
		
		shelfNo = (ComboBox) binder.buildAndBind("Shelf No",
				"selectShelf", ComboBox.class);
		shelfNo.setRequired(true);
		if(containerMap.get(SHAConstants.DATA_ENTRY_SHELF_CONTAINER) != null){
			shelfList = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.DATA_ENTRY_SHELF_CONTAINER);
			shelfNo.setContainerDataSource(shelfList);
		}
		
		shelfNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		shelfNo.setItemCaptionPropertyId("value");
		
		if (this.bean.getSelectShelf() != null) {
			this.shelfNo.setValue(this.bean.getSelectShelf());
		}
		
		rejection = (CheckBox) binder.buildAndBind("Rejected", "isRejectStatus",
				CheckBox.class);
		
		fileDetailsPanel = new Panel("File Details");
		HorizontalLayout fileDetail = new HorizontalLayout();
		FormLayout fileDetails1 = new FormLayout(txtClientName,txtYear,txtClaimSeqNo,txtClaimNo,txtPatientName,txtAlmirahNo,checkStatus);
		
		FormLayout fileDetails2 = new FormLayout(dummyText,storageLocation,rackNo,shelfNo,dummyText2,/*txtAdditionalShelfNo*/txtBundleNo,rejection);
		
		fileDetails1.setSpacing(true);
		fileDetails1.setMargin(false);
		
		fileDetails2.setSpacing(true);
		fileDetails2.setMargin(false);
		
		fileDetail.addComponent(fileDetails1);
		fileDetail.addComponent(fileDetails2);
		fileDetail.setSpacing(true);
		fileDetailsPanel.setContent(fileDetail);
		
		//employeeDetailsPanel.setStyleName("girdBorder");
		
		return fileDetailsPanel;
	}
	
	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("Submit");
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		/*AbsoluteLayout absoluteLayout_3
		.addComponent(prSearchBtn, "top:100.0px;left:220.0px;");*/
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		
		btnCancel = new Button();
		btnCancel.setCaption("Reset");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
/*		absoluteLayout_3.addComponent(resetBtn, "top:100.0px;left:329.0px;");
*/		//Vaadin8-setImmediate() btnCancel.setImmediate(true);

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
		hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}
	
	
	public void addListener() {

		storageLocation.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if (value != null && value.getId() != null) {
					rackNo.setValue(null);
					shelfNo.setValue(null);
					fireViewEvent(SetDataEntryPresenter.GET_SELECT_LIST,
							value.getId(),
							SHAConstants.DATA_ENTRY_RACK_CONTAINER);
				}
			}
		});

		rackNo.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if (value != null && value.getId() != null) {
					shelfNo.setValue(null);
					fireViewEvent(SetDataEntryPresenter.GET_SELECT_LIST,
							value.getId(),
							SHAConstants.DATA_ENTRY_SHELF_CONTAINER);
				}
			}
		});
		
		txtClaimSeqNo.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				if(component != null && component.getValue() != null && !("").equals(component.getValue())
						&& (component.getValue().length() >= 6)){
					
					if (null != txtYear
							&& null != txtYear.getValue()
							&& !("").equals(txtYear.getValue())
							&& (txtYear.getValue().length() == 4)) {
						String claimNo = component.getValue();
						claimNo = claimNo.replaceAll("\\s", "");
						String subStr = null;
							if(claimNo.length() == 6){
								subStr = claimNo;
							}else if(claimNo.length() > 6){
								
								String listOfSeq[] = claimNo.split("/");
								if(listOfSeq != null){
									String lastOne = listOfSeq[listOfSeq.length-1];
									if(lastOne != null && !lastOne.isEmpty() && lastOne.length() >= 6){
										subStr = lastOne;
									}
								}else{
									subStr = claimNo.substring(claimNo.length() - 6);	
								}
							}
							
							String year = txtYear.getValue();
						if(subStr != null && subStr.matches("[0-9]+")){
							fireViewEvent(SetDataEntryPresenter.VALIDATE_CLAIM, subStr, year);	
						}else{
							bean.setIsPremiaIntimation(false);
							txtClaimNo.setValue(null);
						}
						
						}else{
							txtClaimNo.setValue(null);
						}
				}else{
					txtClaimNo.setValue(null);
				}
				
			}



		});
		
		
		txtYear.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				if (component != null && component.getValue() != null
						&& !("").equals(component.getValue())
						&& (component.getValue().length() == 4)) {

					if (null != txtClaimSeqNo && null != txtClaimSeqNo.getValue()
							&& !("").equals(txtClaimSeqNo.getValue())
							&& (txtClaimSeqNo.getValue().length() >= 6)) {
						String year = component.getValue();
						String claimNo = txtClaimSeqNo.getValue();
						
						claimNo = claimNo.replaceAll("\\s", "");
						String subStr = null;
							if(claimNo.length() == 6){
								subStr = claimNo;
							}else if(claimNo.length() > 6){
								
								String listOfSeq[] = claimNo.split("/");
								if(listOfSeq != null){
									String lastOne = listOfSeq[listOfSeq.length-1];
									if(lastOne != null && !lastOne.isEmpty() && lastOne.length() >= 6){
										subStr = lastOne;
									}
								}else{
									subStr = claimNo.substring(claimNo.length() - 6);	
								}
							}
							
							if(subStr != null && subStr.matches("[0-9]+")){
								fireViewEvent(SetDataEntryPresenter.VALIDATE_CLAIM,
										subStr, year);
							}else{
								bean.setIsPremiaIntimation(false);
								txtClaimNo.setValue(null);
							}
						
					}else {
						txtClaimNo.setValue(null);
					}
				} else {
					txtClaimNo.setValue(null);
				}

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
	
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		String eMsg = "";
		showOrHideValidation(true);
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
		
		if(null != storageLocation )
		{
			SelectValue selValue = (SelectValue)storageLocation.getValue();
			if(!(null != selValue && null != selValue.getValue()))
			{
				hasError = true;
				eMsg += "Please select Storage Location" + "</br>";
			}
		}
		
		if(null != rackNo )
		{
			SelectValue selValue = (SelectValue)rackNo.getValue();
			if(!(null != selValue && null != selValue.getValue()))
			{
				hasError = true;
				eMsg += "Please select Rack" + "</br>";
			}
		}
		
		if(null != shelfNo )
		{
			SelectValue selValue = (SelectValue)shelfNo.getValue();
			if(!(null != selValue && null != selValue.getValue()))
			{
				hasError = true;
				eMsg += "Please select Shelf" + "</br>";
			}
		}
		
		/*if(!(null != txtClaimNo && null != txtClaimNo.getValue() && ! ("").equals(txtClaimNo.getValue())))
		{
			
				hasError = true;
				eMsg += "Please enter Claim No" + "</br>";
			
		}*/
		
		if(!(null != txtClaimSeqNo && null != txtClaimSeqNo.getValue() && ! ("").equals(txtClaimSeqNo.getValue()) && (txtClaimSeqNo.getValue().length() >= 6) ))
		{
			
				hasError = true;
				eMsg += "Please enter minimum 6 digit Claim (Seq) No" + "</br>";
			
		}
		
		if(!(null != txtYear && null != txtYear.getValue() && ! ("").equals(txtYear.getValue()) && (txtYear.getValue().length() == 4)))
		{
			
				hasError = true;
				eMsg += "Please enter Year" + "</br>";
			
		}
		
		/*if (null != txtClaimSeqNo && null != txtClaimSeqNo.getValue()
				&& !("").equals(txtClaimSeqNo.getValue())
				&& (txtClaimSeqNo.getValue().length() >= 6) && null != txtYear
				&& null != txtYear.getValue()
				&& !("").equals(txtYear.getValue())
				&& (txtYear.getValue().length() == 4)) {
			String claimNo = txtClaimSeqNo.getValue();
			claimNo = claimNo.replaceAll("\\s", "");
			
			String year = txtYear.getValue();
			
			fireViewEvent(SetDataEntryPresenter.VALIDATE_CLAIM, claimNo, year);
			if (!this.bean.getIsPremiaIntimation()) {
				hasError = true;
				eMsg += "Please Enter Valid Claim No. or Year" + "</br>";
			}
		}*/
		
		if (!this.bean.getIsPremiaIntimation() && (txtClaimNo == null || txtClaimNo.getValue() == null) && null != txtYear && null != txtYear.getValue() && null != txtClaimSeqNo && null != txtClaimSeqNo.getValue()) {
			hasError = true;
			eMsg += "Invalid Claim (Seq) No. or Year" + "</br>";
		}
		
		if(!(null != txtPatientName && null != txtPatientName.getValue() && ! ("").equals(txtPatientName.getValue())))
		{
			
				hasError = true;
				eMsg += "Please enter Patient Name" + "</br>";
			
		}
		
		if(!(null != checkStatus && null != checkStatus.getValue()))
		{
			
				hasError = true;
				eMsg += "Please Select File status" + "</br>";
			
		}
		
		/*if(this.chequeDetailsListenerTableObj != null && this.chequeDetailsListenerTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg += "Please Enter Cheque Details to Proceed Further. </br>"; 
		}*/
		
		if (this.chequeDetailsListenerTableObj != null){
			boolean isValid = this.chequeDetailsListenerTableObj.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.chequeDetailsListenerTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		if (hasError) {
			//setRequired(true);
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
		} 
		else 
		{
			try {
				this.binder.commit();
				
				if(this.bean.getIsPremiaIntimation() && txtClaimNo.getValue() != null){
					this.bean.setClaimNo(txtClaimNo.getValue());
				}
				
				if(this.bean.getDeletedChequeList() == null || this.bean.getDeletedChequeList().isEmpty()) {
					this.bean.setDeletedChequeList(this.chequeDetailsListenerTableObj.deletedDTO);
				} else {
					List<ChequeDetailsTableDTO> deletedDTO = this.chequeDetailsListenerTableObj.deletedDTO;
					for (ChequeDetailsTableDTO chequeDetailsTableDTO : deletedDTO) {
						if(!this.bean.getDeletedChequeList().contains(chequeDetailsTableDTO)) {
							this.bean.getDeletedChequeList().add(chequeDetailsTableDTO);
						}
						
					}
				}
				
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void setTableValuesToDTO() {
		List<ChequeDetailsTableDTO> chequeList = this.chequeDetailsListenerTableObj.getValues();
		this.bean.setChequeList(chequeList);
	}


	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	private void setTableValues() {

		if (this.chequeDetailsListenerTableObj != null) {
			List<ChequeDetailsTableDTO> chequeList = this.bean.getChequeList();
			if(chequeList != null){
				for (ChequeDetailsTableDTO chequeDTO : chequeList) {
					this.chequeDetailsListenerTableObj.addBeanToList(chequeDTO);
				}	
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	public void setSelectValueListener(Map<String, Object> containerMap1){
		if(containerMap1 != null){
			if(containerMap1.get(SHAConstants.DATA_ENTRY_RACK_CONTAINER) != null){
				containerMap.put(SHAConstants.DATA_ENTRY_RACK_CONTAINER, containerMap1.get(SHAConstants.DATA_ENTRY_RACK_CONTAINER));
				rackList = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.DATA_ENTRY_RACK_CONTAINER);
				rackNo.setContainerDataSource(rackList);
			}if(containerMap1.get(SHAConstants.DATA_ENTRY_SHELF_CONTAINER) != null){
				containerMap.put(SHAConstants.DATA_ENTRY_SHELF_CONTAINER, containerMap1.get(SHAConstants.DATA_ENTRY_SHELF_CONTAINER));
				shelfList = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.DATA_ENTRY_SHELF_CONTAINER);
				shelfNo.setContainerDataSource(shelfList);
			}
		}
	}
	
	public void setPremiaIntimation(PDIntimationPremia intimation){
		if(intimation != null){
			this.bean.setIsPremiaIntimation(true);
			if(intimation.getHasRecords() != null && !intimation.getHasRecords()){
				if(intimation.getIntimationNumber() != null){
					txtClaimNo.setValue(intimation.getIntimationNumber());	
				}
			}else{
				if(txtClaimSeqNo.getValue() != null){
					txtClaimNo.setValue(txtClaimSeqNo.getValue());	
				}
			}
			
		}else{
			this.bean.setIsPremiaIntimation(false);
			txtClaimNo.setValue(null);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences() {
		setTableValues();
	}
	
	

}
