package com.shaic.claim.medical.opinion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OpinionValidationSearchForm extends SearchComponent<OpinionValidationFormDTO> {
	
	private static final long serialVersionUID = 1L;
	
	private TextField txtEmployeeName; 
	
	private TextField txtIntimationNo;
	
	private PopupDateField activityFromDate;
	
	private PopupDateField activityToDate;
	
	private ComboBox cmbSource;	
	
	private ComboBoxMultiselect cmbCpuCodeMulti;
	
	private ComboBoxMultiselect cmbMultiDoctorName;
	
	private Button btnCompletedCases;
	
	VerticalLayout opinionVerticalLayout;
	
	private PopupDateField fromDateField;
	
	private PopupDateField toDateField;
	
	private ComboBox cmbOpinionStatus;	
	
	private Button btnSearchCompletedCase;
	
	Panel mainPanel;
	
	String userName = null;
	
	private BeanFieldGroup<OpinionValidationFormDTO> binder;
	
	private BeanItemContainer<SelectValue> cpuCode ;
	
	private BeanItemContainer<SelectValue> doctorNames ;
	
	private OpinionValidationFormDTO dto =  new OpinionValidationFormDTO();
	
	@EJB
	private MasterService masterService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private Button btnCompleteSubmit;
	
	private Button btnCompleteCancel;
	
	private Button btnCompleteClose;
	
	private String screenName;
	
	private VerticalSplitPanel verticalSplitPanel;
	
	Window popup;
	
	@Inject
	private OpinionValidationCompletedCasesTable opinionValidationCompletedCasesTable;
	
	@PostConstruct
	public void init() {
	}
	
	public void initView(OpinionValidationFormDTO dto) {
		initBinder(dto);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		setSizeFull();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Opinion Validation");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		//Employee Name
		txtEmployeeName = binder.buildAndBind("Employee Name", "employeeName", TextField.class);
		//Vaadin8-setImmediate() txtEmployeeName.setImmediate(false);
		txtEmployeeName.setWidth("160px");
		txtEmployeeName.setHeight("-1px");
		txtEmployeeName.setTabIndex(3);
		txtEmployeeName.setMaxLength(25);
		txtEmployeeName.setEnabled(false);
				
				
		// Activity From Date
		Date currentDate = new Date();
		activityFromDate = binder.buildAndBind("Activity From Date", "activityFromDate", PopupDateField.class);
		//Vaadin8-setImmediate() activityFromDate.setImmediate(false);
		activityFromDate.setTabIndex(11);
		activityFromDate.setWidth("160px");
		activityFromDate.setHeight("-1px");
		activityFromDate.setDateFormat("dd/MM/yyyy");
		activityFromDate.setData(true);
		activityFromDate.setValue(currentDate);
		activityFromDate.setTextFieldEnabled(false);
		
		//Activity To Date
		activityToDate = binder.buildAndBind("Activity To Date", "activityToDate", PopupDateField.class); 
		//Vaadin8-setImmediate() activityToDate.setImmediate(false);
		activityToDate.setTabIndex(11);
		activityToDate.setWidth("160px");
		activityToDate.setHeight("-1px");
		activityToDate.setDateFormat("dd/MM/yyyy");
		activityToDate.setData(true);
		activityToDate.setValue(currentDate);
		activityToDate.setTextFieldEnabled(false);
		
		//Restrict Date 
		Calendar cal = GregorianCalendar.getInstance();
		cal.add( Calendar.DAY_OF_YEAR, -180);
		Date startDate = cal.getTime();
		activityFromDate.setRangeStart(startDate);
		activityToDate.setRangeStart(startDate);
		activityFromDate.setRangeEnd(currentDate);
		activityToDate.setRangeEnd(currentDate);
	
		//Intimation Number
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		//Vaadin8-setImmediate() txtIntimationNo.setImmediate(false);
		txtIntimationNo.setWidth("160px");
		txtIntimationNo.setTabIndex(8);
		txtIntimationNo.setHeight("-1px");
		txtIntimationNo.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(txtIntimationNo);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
				
		//Source
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		cmbSource.setWidth("160px");
		cmbSource.setTabIndex(1);
		cmbSource.setHeight("-1px");
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
				
		// List of sources
		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(01L);
		selectValue1.setValue(SHAConstants.SOURCE_PREAUTH_PROCESS);
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(2l);
		selectValue2.setValue(SHAConstants.SOURCE_ENHANCEMENT_PROCESS);
			
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(3l);
		selectValue3.setValue(SHAConstants.MA_STAGE_SOURCE);
				
		BeanItemContainer<SelectValue> sourceData = new BeanItemContainer<SelectValue>(SelectValue.class);
		sourceData.addBean(selectValue1);
		sourceData.addBean(selectValue2);
		sourceData.addBean(selectValue3);		
			
		cmbSource.setContainerDataSource(sourceData);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
				
		//Cpu Code
		cmbCpuCodeMulti = new ComboBoxMultiselect("CPU Name");
		cmbCpuCodeMulti.setShowSelectedOnTop(true);
		cmbCpuCodeMulti.setWidth("160px");
		cmbCpuCodeMulti.addValueChangeListener(new ValueChangeListener() {
		
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<OpinionValidationFormDTO> dtoBeanObject = binder.getItemDataSource();
				OpinionValidationFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCpuCodeMulti(null);
				dtoObject.setCpuCodeMulti(event.getProperty().getValue());
			}
		});	
				
		//Doctor Name
		cmbMultiDoctorName = new ComboBoxMultiselect("Doctor Name");
		cmbMultiDoctorName.setShowSelectedOnTop(true);
		cmbMultiDoctorName.setWidth("160px");
		cmbMultiDoctorName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<OpinionValidationFormDTO> dtoBeanObject = binder.getItemDataSource();
				OpinionValidationFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setDoctorNameMulti(null);
				dtoObject.setDoctorNameMulti(event.getProperty().getValue());
			}
		});	
		
		//Completed Cases
		btnCompletedCases = new Button();
		btnCompletedCases.setCaption("Completed Cases");
		//Vaadin8-setImmediate() btnCompletedCases.setImmediate(true); 
		btnCompletedCases.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnCompletedCases.setWidth("-1px");
		btnCompletedCases.setHeight("-10px");
		//Vaadin8-setImmediate() btnCompletedCases.setImmediate(true);
		
		FormLayout formLayoutLeft = new FormLayout(txtEmployeeName, txtIntimationNo,cmbCpuCodeMulti);
		FormLayout formLayoutMiddle = new FormLayout(activityFromDate, cmbSource,cmbMultiDoctorName);
		FormLayout formLayoutRight = new FormLayout(activityToDate);
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle, formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout =  new AbsoluteLayout();
		absoluteLayout.addComponent(fieldLayout);		
		absoluteLayout.addComponent(btnSearch, "top:150.0px;left:220.0px;");
		absoluteLayout.addComponent(btnReset, "top:150.0px;left:329.0px;");
		absoluteLayout.addComponent(btnCompletedCases, "top:150.0px;left:580.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("950px");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout.setImmediate(false);
		absoluteLayout.setWidth("100.0%");
		absoluteLayout.setHeight("243px");
		addListener();
		completedCasesListener();
		return mainVerticalLayout;
	}

	private void initBinder(OpinionValidationFormDTO dto) {		
		if (dto != null) {
			this.dto = dto;
		} else {
			this.dto = new OpinionValidationFormDTO();
		}
		
		this.binder = new BeanFieldGroup<OpinionValidationFormDTO>(OpinionValidationFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode, String name, BeanItemContainer<SelectValue> doctorNames) {
		this.cpuCode = cpuCode;
		this.doctorNames = doctorNames;
		userName = name;
		userName = name +" - " + masterService.getEmployeeByName(name);
		txtEmployeeName.setValue(userName != null ? userName.toUpperCase() : "");
		setUpDropDownValues();
	}
	
	private void setUpDropDownValues() {
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	
		
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		List<SelectValue> cpuCode1 = (List<SelectValue>) cpuCode.getItemIds();
		if (cpuCode1 != null && !cpuCode1.isEmpty()) {
			 for (SelectValue selectValue : cpuCode1) {
				 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					 filterList.add(selectValue);
				 }
			}
		}
		cmbCpuCodeMulti.removeAllItems();
		cpuCode.addAll(filterList);
			
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	
		
		cmbMultiDoctorName.setContainerDataSource(doctorNames);
		cmbMultiDoctorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMultiDoctorName.setItemCaptionPropertyId("value");	
		
	}
	
	public OpinionValidationFormDTO getSearchDTO(){
		try {
			this.binder.commit();
			OpinionValidationFormDTO bean = binder.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setDefaultValues() {
		SHAUtils.resetComponent(mainVerticalLayout);
		txtEmployeeName.setValue(userName != null ? userName.toUpperCase() : "");
		activityFromDate.setValue(new Date());
		activityToDate.setValue(new Date());
		cmbCpuCodeMulti.setValue(null);
		cmbMultiDoctorName.setValue(null);
		setUpDropDownValues();		
	}
	
	private void completedCasesListener() {
		btnCompletedCases.addClickListener(new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			popup = new com.vaadin.ui.Window();
			opinionValidationCompletedCasesTable.init("", false, false);		
			verticalSplitPanel = new VerticalSplitPanel();
			verticalSplitPanel.setFirstComponent(buildCompletedCasesButtonLayout());
			VerticalLayout tableLayout = new VerticalLayout();
			HorizontalLayout buttonLayout = buildCompletedCaseTableButtonLayout();
			tableLayout.addComponent(opinionValidationCompletedCasesTable);
			tableLayout.addComponent(buttonLayout);
			tableLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
			verticalSplitPanel.setSecondComponent(tableLayout);
			verticalSplitPanel.setSplitPosition(30);
			setHeight("600px");
			popup.setCaption("Opinion Validation - Completed Cases");
			popup.setWidth("95%");
			popup.setHeight("95%");
			popup.setContent(verticalSplitPanel);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					setDefaultValues();
				}
			});
			
			OpinionValidationFormDTO completedCasesList =  new OpinionValidationFormDTO();
			completedCasesList.setEmployeeName(userName != null ? userName.toUpperCase() : "");
			fireViewEvent(OpinionValidationPresenter.SEARCH_COMPLETED_CASES, completedCasesList);	

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}

		});
	}
	
	private VerticalLayout buildCompletedCasesButtonLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();
		
		fromDateField = new PopupDateField();
		fromDateField.setCaption("From Date");
		//Vaadin8-setImmediate() fromDateField.setImmediate(false);
		fromDateField.setTabIndex(11);
		fromDateField.setWidth("160px");
		fromDateField.setHeight("-1px");
		fromDateField.setDateFormat("dd/MM/yyyy");
		fromDateField.setTextFieldEnabled(false);
		
		toDateField = new PopupDateField();
		toDateField.setCaption("To Date");
		//Vaadin8-setImmediate() toDateField.setImmediate(false);
		toDateField.setTabIndex(11);
		toDateField.setWidth("160px");
		toDateField.setHeight("-1px");
		toDateField.setDateFormat("dd/MM/yyyy");
		toDateField.setTextFieldEnabled(false);
		
		
		cmbOpinionStatus = new ComboBox();
		cmbOpinionStatus.setCaption("Status");
		cmbOpinionStatus.setWidth("160px");
		cmbOpinionStatus.setTabIndex(1);
		cmbOpinionStatus.setHeight("-1px");
		cmbOpinionStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOpinionStatus.setItemCaptionPropertyId("value");
				
		// List of sources
		SelectValue approveStatus = new SelectValue();
		approveStatus.setId(SHAConstants.OPINION_STATUS_APPROVE);
		approveStatus.setValue("Approve");
		
		SelectValue rejectStatus = new SelectValue();
		rejectStatus.setId(SHAConstants.OPINION_STATUS_REJECT);
		rejectStatus.setValue("Reject");
		
		SelectValue elapsedStatus = new SelectValue();
		elapsedStatus.setId(SHAConstants.OPINION_STATUS_ELAPSED);
		elapsedStatus.setValue("Elapsed");
				
		BeanItemContainer<SelectValue> statusData = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusData.addBean(approveStatus);
		statusData.addBean(rejectStatus);
		statusData.addBean(elapsedStatus);
			
		cmbOpinionStatus.setContainerDataSource(statusData);
		cmbOpinionStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOpinionStatus.setItemCaptionPropertyId("value");
		
		btnSearchCompletedCase = new Button();
		btnSearchCompletedCase.setCaption("Search"); 
		//Vaadin8-setImmediate() btnSearchCompletedCase.setImmediate(true); 
		btnSearchCompletedCase.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSearchCompletedCase.setWidth("-1px");
		btnSearchCompletedCase.setHeight("-10px");
		//Vaadin8-setImmediate() btnSearchCompletedCase.setImmediate(true);
		
		FormLayout formLayoutLeft = new FormLayout(fromDateField, cmbOpinionStatus);
		FormLayout formLayoutRight = new FormLayout(toDateField);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout =  new AbsoluteLayout();
		absoluteLayout.addComponent(fieldLayout);		
		absoluteLayout.addComponent(btnSearchCompletedCase, "top:100.0px;left:220.0px;");
		
		verticalLayout.addComponent(absoluteLayout);
		//Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		verticalLayout.setWidth("700px");
		verticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout.setImmediate(false);
		absoluteLayout.setWidth("100.0%");
		absoluteLayout.setHeight("243px");
		
		addCompletedCaseSearchListener();
		return verticalLayout;
	}
	
	public void addCompletedCaseSearchListener() {

		btnSearchCompletedCase.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Date startDate = fromDateField.getValue();
				Date endDate = toDateField.getValue();
				
				OpinionValidationFormDTO completedCasesList =  new OpinionValidationFormDTO();
				completedCasesList.setFromDate(startDate);
				completedCasesList.setToDate(endDate);
				if (cmbOpinionStatus.getValue() != null) {
					if (cmbOpinionStatus.getValue().toString().equalsIgnoreCase("Approve")) {
						completedCasesList.setStatus(SHAConstants.OPINION_STATUS_APPROVE);
					} else if (cmbOpinionStatus.getValue().toString().equalsIgnoreCase("Reject")) {
						completedCasesList.setStatus(SHAConstants.OPINION_STATUS_REJECT);
					} else if (cmbOpinionStatus.getValue().toString().equalsIgnoreCase("Elapsed")) {
						completedCasesList.setStatus(SHAConstants.OPINION_STATUS_ELAPSED);
					}
				}
				completedCasesList.setEmployeeName(userName != null ? userName.toUpperCase() : "");
				fireViewEvent(OpinionValidationPresenter.SEARCH_COMPLETED_CASES, completedCasesList);	
			}
		});
		
	}
	
	public void resetCompletedCasesSearchRTables(VerticalSplitPanel verticalSplitPanel) {
		opinionValidationCompletedCasesTable.getPageable().setPageNumber(1);
		opinionValidationCompletedCasesTable.resetTable();
		Iterator<Component> componentIter = verticalSplitPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				Iterator<Component> vLayoutIter = ((VerticalLayout) comp).getComponentIterator();
				while(vLayoutIter.hasNext()) {
					Component comp1 = (Component)vLayoutIter.next();
					if(comp1 instanceof OpinionValidationCompletedCasesTable)
					{
						((OpinionValidationCompletedCasesTable) comp1).removeRow();
					}
				}
					
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private HorizontalLayout buildCompletedCaseTableButtonLayout(){
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() horizontalLayout.setImmediate(true);
		
		btnCompleteSubmit = new Button();
		btnCompleteSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnCompleteSubmit.setImmediate(true); 
		btnCompleteSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnCompleteSubmit.setWidth("-1px");
		btnCompleteSubmit.setHeight("-10px");
		btnCompleteSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnCompleteSubmit.setImmediate(true);
		
		btnCompleteCancel = new Button();
		btnCompleteCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCompleteCancel.setImmediate(true);
		btnCompleteCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCompleteCancel.setWidth("-1px");
		btnCompleteCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCompleteCancel.setImmediate(true);
		
		btnCompleteClose = new Button();
		btnCompleteClose.setCaption("Close");
		//Vaadin8-setImmediate() btnCompleteClose.setImmediate(true);
		btnCompleteClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCompleteClose.setWidth("-1px");
		btnCompleteClose.setHeight("-10px");
		//Vaadin8-setImmediate() btnCompleteClose.setImmediate(true);
		
		horizontalLayout.addComponents(btnCompleteSubmit, btnCompleteCancel, btnCompleteClose);
		horizontalLayout.setSpacing(true);
		addCompletedCaseBtnListener();
		return horizontalLayout;
	
	}
	
	public void addCompletedCaseBtnListener() {

		btnCompleteSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				btnCompleteSubmit.setEnabled(true);
				HashMap<Long, Boolean> opinionStatusMap = opinionValidationCompletedCasesTable.getOpinionStatusValue();
				HashMap<Long, String> opinionStatusRemarks = opinionValidationCompletedCasesTable.getOpinionStatusRemarks();
				Boolean result = isValidData(opinionStatusMap, opinionStatusRemarks);
				Label responseLabel = null;
				if (result) {
					fireViewEvent(OpinionValidationPresenter.SUBMIT_BUTTON_CLICK, opinionStatusMap, opinionStatusRemarks, userName);				
					responseLabel = new Label("<b style = 'color: black;'>Records updated successfully</b>", ContentMode.HTML);
					Button successBtn = new Button("Opinion Validation Home");
					if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						successBtn.setCaption("Waiting For Input Home");
					}				
					successBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(responseLabel, successBtn);
					layout.setComponentAlignment(successBtn, Alignment.BOTTOM_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					successBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();

							if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))){
								
								fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
							}
							else {
								fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
							}
							resetSearchCompletedCaseTableValues();						
						}
					});
				} else {
					if (opinionStatusMap.size() > 0) {
						responseLabel = new Label("<b style = 'color: black;'>Remarks field is mandatory for disagreed opinion </b>", ContentMode.HTML);
					} else {
						responseLabel = new Label("<b style = 'color: black;'>Please select agree/disagree before submit </b>", ContentMode.HTML);
					}
					Button successBtn = new Button("Ok");
					if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						successBtn.setCaption("Waiting For Input Home");
					}				
					successBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(responseLabel, successBtn);
					layout.setComponentAlignment(successBtn, Alignment.BOTTOM_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					successBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});
				}
			}

		});
		
		btnCompleteCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				opinionValidationCompletedCasesTable.cancelOpinionStatusValue();
				Date startDate = fromDateField.getValue();
				Date endDate = toDateField.getValue();
				OpinionValidationFormDTO completedCasesList =  new OpinionValidationFormDTO();
				completedCasesList.setFromDate(startDate);
				completedCasesList.setToDate(endDate);
				completedCasesList.setEmployeeName(userName != null ? userName.toUpperCase() : "");
				fireViewEvent(OpinionValidationPresenter.SEARCH_COMPLETED_CASES, completedCasesList);	
			}
		});
		
		btnCompleteClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				setDefaultValues();
			}
		});
	}
	
	private boolean isValidData(HashMap<Long, Boolean> opinionStatusMap, HashMap<Long, String> opinionStatusRemarks) {
		if (opinionStatusMap != null && opinionStatusMap.size() > 0) {
			Iterator<?> it = opinionStatusMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry opinionStatus = (Map.Entry)it.next();
	            if (opinionStatus.getValue() != null && opinionStatus.getValue().equals(Boolean.FALSE)) {
	            	if (opinionStatusRemarks != null && opinionStatusRemarks.size() > 0) {
	            		String remarks = opinionStatusRemarks.get(opinionStatus.getKey());
	            		if(remarks == null && StringUtils.isBlank(remarks)) {
	            			return false;
	            		} 
	            	} else {
	            		return false;
	            	}
	            }
	        }
		} else {
			return false;
		}
		return true;
	}
	
	public void resetSearchCompletedCaseTableValues() {
		opinionValidationCompletedCasesTable.getPageable().setPageNumber(1);
		opinionValidationCompletedCasesTable.resetTable();
		Iterator<Component> componentIter = verticalSplitPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				Iterator<Component> vLayoutIter = ((VerticalLayout) comp).getComponentIterator();
				while(vLayoutIter.hasNext()) {
					Component comp1 = (Component)vLayoutIter.next();
					if(comp1 instanceof OpinionValidationTable)
					{
						((OpinionValidationTable) comp1).removeRow();
					}
				}
					
				
			}
		}
		opinionValidationCompletedCasesTable.cancelOpinionStatusValue();
		Date startDate = fromDateField.getValue();
		Date endDate = toDateField.getValue();
		OpinionValidationFormDTO completedCasesList =  new OpinionValidationFormDTO();
		completedCasesList.setFromDate(startDate);
		completedCasesList.setToDate(endDate);
		completedCasesList.setEmployeeName(userName != null ? userName.toUpperCase() : "");
		fireViewEvent(OpinionValidationPresenter.SEARCH_COMPLETED_CASES, completedCasesList);
		if (opinionValidationCompletedCasesTable.getTable().size() < 0) {
			btnCompleteSubmit.setVisible(false);
			btnCompleteCancel.setVisible(false);
		}
	}
}
