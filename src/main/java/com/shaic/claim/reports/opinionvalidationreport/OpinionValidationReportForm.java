package com.shaic.claim.reports.opinionvalidationreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchFormDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportForm extends SearchComponent<OpinionValidationReportFormDTO> {
	
	private ComboBoxMultiselect cmbRole;
	
	private ComboBox cmbEmployeeName;
	
	private ComboBox cmbOpnionStatus;
	
	private ComboBox cmbValidatedStatus;
	
	private PopupDateField dateField;
	
	private PopupDateField toDateField;
	
	private Button xmlReport;
	
	String userName = null;
	
	private OpinionValidationReportFormDTO bean;

	@Inject
	private OpinionValidationReportService opinionValidationReportService;

	private static final String USER_ROLE_OPINION_RPT_ADMIN = "CLM_OPINION_RPT_ADMIN";
	
	@PostConstruct
	public void init() {
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Opinion Validation Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		bean = new OpinionValidationReportFormDTO();
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("149px");
		 
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		ImsUser imsUser = null;
		String[] userRoles = null;
		if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
			imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
			userRoles = imsUser.getUserRoleList();
		}
		
		//Role
		cmbRole = new ComboBoxMultiselect("Role");
		cmbRole.setShowSelectedOnTop(true);
		cmbRole.setTabIndex(1);
		if(Arrays.asList(userRoles).contains(USER_ROLE_OPINION_RPT_ADMIN)) {
			cmbRole.setEnabled(true);	
		}else {
			cmbRole.setEnabled(false);
		}
		cmbRole.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				bean.setRole(null);				
				bean.setRole(event.getProperty().getValue());
			}
		});
		
		//Employee Name
		cmbEmployeeName = new ComboBox("Employee Id/Name");
		//Vaadin8-setImmediate() cmbEmployeeName.setImmediate(false);
		cmbEmployeeName.setHeight("-1px");
		cmbEmployeeName.setTabIndex(2);
		
		
		cmbOpnionStatus = new ComboBox("Opinion Status");
		//Vaadin8-setImmediate() cmbOpnionStatus.setImmediate(true);
		cmbOpnionStatus.setHeight("-1px");
		cmbOpnionStatus.setTabIndex(3);
		
		dateField = new PopupDateField("From Date");
		dateField.setDateFormat("dd/MM/yyyy");
		dateField.setTabIndex(4);
		dateField.setTextFieldEnabled(false);
		
		toDateField = new PopupDateField("To Date");
		toDateField.setDateFormat("dd/MM/yyyy");
		toDateField.setTabIndex(5);
		toDateField.setTextFieldEnabled(false);
		
		cmbValidatedStatus = new ComboBox("Validated Status");
		//Vaadin8-setImmediate() cmbValidatedStatus.setImmediate(true);
		cmbValidatedStatus.setHeight("-1px");
		cmbValidatedStatus.setTabIndex(6);
		
				
		dateField.setValidationVisible(false);
		toDateField.setValidationVisible(false);
		
		FormLayout formLayoutLeft = new FormLayout(cmbRole,dateField);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(true);
		FormLayout formLayoutRight = new FormLayout(cmbEmployeeName,toDateField);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(true);
		
		FormLayout formLayoutThree = new FormLayout(cmbOpnionStatus,cmbValidatedStatus);
		formLayoutThree.setSpacing(true);
		formLayoutThree.setMargin(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight,formLayoutThree);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(btnGenerate, "top:100.0px;left:200.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:315.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:100.0px;left:415.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		setStatusDropDownValues();
		addCmbListener();
		cmbValidatedStatus.setEnabled(false);
		addListener();
		addReportListener();
		return mainVerticalLayout;
	}
	
	private void addCmbListener(){
		cmbOpnionStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -82186073708404325L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				SelectValue selectedVal = (SelectValue) event.getProperty().getValue();
				if(selectedVal != null){
					cmbValidatedStatus.clear();
					if(selectedVal.getValue() != null){
						if(selectedVal.getValue().equals("Pending")){
							cmbValidatedStatus.setEnabled(false);
						}else{
							cmbValidatedStatus.setEnabled(true);
						}
					}else{
						cmbValidatedStatus.setEnabled(false);
					}
				}else{
					cmbValidatedStatus.clear();
					cmbValidatedStatus.setEnabled(false);
				}
			}
		});
	}
	
	private void addReportListener()
	{
		xmlReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(OpinionValidationReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	public OpinionValidationReportFormDTO  validate()
	{
		String err = "";
		
		
		
		
		if(dateField.getValue()!=null && toDateField.getValue()!=null)
		{
			if(!SHAUtils.validateDate(dateField.getValue()) && !SHAUtils.validateDate(dateField.getValue()))
			{				
			
		 if(toDateField.getValue().before(dateField.getValue()))
		 {
			err= "Enter Valid To Date";
			showErrorMessage(err);			
			 btnSearch.setEnabled(true);
			 btnSearch.setDisableOnClick(true);
			 return null;
			
		}	
		 else
		 {
			 bean.setFromDate(dateField.getValue());
			 bean.setToDate(toDateField.getValue());
			
			
		 }		 
			}
						
		}
		if(dateField.getValue() == null || toDateField.getValue() == null)
		{
			err= "Both Date Fields are Mandatory";
			showErrorMessage(err);	
			return null;
		}
		else
		{
			 bean.setFromDate(dateField.getValue());
			 bean.setToDate(toDateField.getValue());
		}
		
		if(dateField.getValue()!=null && toDateField.getValue()== null)
		{
			 bean.setFromDate(dateField.getValue());
		}
		
		if(toDateField.getValue()!=null && dateField.getValue()==null)
		{
			 bean.setToDate(toDateField.getValue());
		}
		
		if(cmbRole.getValue().toString() != null && !cmbRole.getValue().toString().isEmpty()){
			bean.setRole(cmbRole.getValue());
		}
		
		if(cmbEmployeeName.getValue() != null){
			bean.setEmployeeName(cmbEmployeeName.getValue().toString());
		}else{
			bean.setEmployeeName(null);
		}
		
		if(cmbOpnionStatus.getValue() != null){
			bean.setOpinionStatus((SelectValue)cmbOpnionStatus.getValue());
		}else{
			bean.setOpinionStatus(null);
		}
		
		if(cmbValidatedStatus.getValue() != null){
			bean.setValidatedStatus((SelectValue)cmbValidatedStatus.getValue());
		}else{
			bean.setValidatedStatus(null);
		}
		
		return bean;
	}
	private void showErrorMessage(String eMsg) {
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
		btnSearch.setEnabled(true);
	}
	
	public void setDropDownValues(final String name, final BeanItemContainer<SelectValue> roleContainer, final BeanItemContainer<SelectValue> empNameContainer) {
		
		
		cmbRole.setContainerDataSource(roleContainer);
		cmbRole.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRole.setItemCaptionPropertyId("value");	
		
		userName = name.toUpperCase() +" - " + opinionValidationReportService.getEmployeeByName(name);
				
		cmbRole.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				
				String[] selectedRoles = null;
				String roles = null;
				if (event.getProperty().getValue() != null) {
					roles = event.getProperty().getValue().toString().replaceAll("\\[", "").replaceAll("\\]", "");
					selectedRoles = roles.split(",");
				}
				
				List<String> roleCodeList = null;
				final List<SelectValue> userRole = (List<SelectValue>) roleContainer.getItemIds();
				if (selectedRoles != null && selectedRoles.length > 0) { 
					roleCodeList = new ArrayList<String>();
					for (String role : selectedRoles) {
						for (SelectValue value : userRole) {
							if (value.getValue().equalsIgnoreCase(role.trim())) {
								roleCodeList.add(value.getCommonValue());
							}
						}
					}
				}
						
				if (roleCodeList != null && !roleCodeList.isEmpty()) {
					BeanItemContainer<SelectValue> employeeData = new BeanItemContainer<SelectValue>(SelectValue.class);
					for (String roleCode : roleCodeList) {
						
						cmbEmployeeName.setContainerDataSource(empNameContainer);
						cmbEmployeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbEmployeeName.setItemCaptionPropertyId("value");	
						
						final List<SelectValue> employeeDetails = (List<SelectValue>) empNameContainer.getItemIds();
						if (employeeDetails != null && !employeeDetails.isEmpty()) {
							for (SelectValue selectValue : employeeDetails) {
								if(selectValue.getCommonValue().equalsIgnoreCase(roleCode)){
									employeeData.addBean(selectValue);
								 }
							}
						}
					}
					cmbEmployeeName.setContainerDataSource(employeeData);
					cmbEmployeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbEmployeeName.setItemCaptionPropertyId("value");
					Collection<SelectValue> itemIds = (Collection<SelectValue>) cmbEmployeeName.getContainerDataSource().getItemIds();
					if(itemIds != null && !itemIds.isEmpty()) {
//						cmbEmployeeName.setValue(itemIds.toArray()[0]);
						cmbEmployeeName.setNullSelectionAllowed(Boolean.FALSE);
					}
				}
				
				if (roles.isEmpty()) {
//					setLoginEmpName();
					cmbEmployeeName.setContainerDataSource(null);
					cmbEmployeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbEmployeeName.setItemCaptionPropertyId("value");	
					cmbEmployeeName.setNullSelectionAllowed(Boolean.TRUE);
				}
			}
	
		});
		
	}
	
	private void setStatusDropDownValues(){
		BeanItemContainer<SelectValue> opinionStatusTypes = getOpinionStatus();
		cmbOpnionStatus.setContainerDataSource(opinionStatusTypes);
		cmbOpnionStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOpnionStatus.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> validatedStatusTypes = getValidatedStatus();
		cmbValidatedStatus.setContainerDataSource(validatedStatusTypes);
		cmbValidatedStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbValidatedStatus.setItemCaptionPropertyId("value");
	}
	
	public BeanItemContainer<SelectValue> getOpinionStatus(){		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		Map<Long, String> stsVal = new  HashMap<Long, String>();
		stsVal.put(1L, "Completed");			
		stsVal.put(2L, "Pending");
		for (Map.Entry<Long, String> entry : stsVal.entrySet()) {
			SelectValue selected = new SelectValue();
			selected.setId(entry.getKey());
			selected.setValue(entry.getValue());
			selectValuesList.add(selected);
		}
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		mastersValueContainer.addAll(selectValuesList);
		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getValidatedStatus(){		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		Map<Long, String> stsVal = new  HashMap<Long, String>();
		stsVal.put(3205L, "Agree");			
		stsVal.put(3206L, "DisAgree");
//		stsVal.put(3207L, "Pending");
		stsVal.put(3208L, "Elapsed");
		for (Map.Entry<Long, String> entry : stsVal.entrySet()) {
			SelectValue selected = new SelectValue();
			selected.setId(entry.getKey());
			selected.setValue(entry.getValue());
			selectValuesList.add(selected);
		}
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		mastersValueContainer.addAll(selectValuesList);
		return mastersValueContainer;
	}

	
	@Override
	public void refresh()
	{
		System.out.println("---inside the refresh----");
		if(mainVerticalLayout != null) {
			SHAUtils.resetComponent(mainVerticalLayout);
			searchable.resetSearchResultTableValues();
		}
		if (quickVerticallayout != null){
			
			SHAUtils.resetComponent(quickVerticallayout);
			searchable.resetSearchResultTableValues();
		}
		setLoginEmpName();
	}
	
	public void setDefaultValues() {
		cmbRole.setValue(null);
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton() == btnReset)
		{
			SHAUtils.resetComponent(mainVerticalLayout);
			searchable.resetSearchResultTableValues();
			
			setLoginEmpName();
			
		}
		else if (event.getButton() == btnGenerate)
        {
			btnGenerate.setEnabled(false);
			searchable.doSearch();
			btnGenerate.setEnabled(true);
        }
	}

	private void setLoginEmpName() {
		
		ImsUser imsUser = null;
		String[] userRoles = null;
		if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
			imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
			userRoles = imsUser.getUserRoleList();
		}
		
		if(!Arrays.asList(userRoles).contains(USER_ROLE_OPINION_RPT_ADMIN)) {
			final BeanItemContainer<SelectValue> sourceData = new BeanItemContainer<SelectValue>(SelectValue.class);
			final SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(01L);
			selectValue1.setValue(userName != null ? userName : "");
			sourceData.addBean(selectValue1);
			cmbEmployeeName.setContainerDataSource(sourceData);
			cmbEmployeeName.setValue(userName != null ? userName : "");
			Collection<SelectValue> itemIds = (Collection<SelectValue>) cmbEmployeeName.getContainerDataSource().getItemIds();
			if(itemIds != null && !itemIds.isEmpty()) {
				cmbEmployeeName.setValue(itemIds.toArray()[0]);
				cmbEmployeeName.setNullSelectionAllowed(Boolean.FALSE);
			}
		}
	}

}
