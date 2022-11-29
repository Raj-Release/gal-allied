package com.shaic.claim.lumen.searchlevelone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class LumenSearchLevelOneReqForm extends SearchComponent<LumenSearchReqFormDTO>{
	
	private TextField intimationNumber;
	private TextField policyNumber;
//	private ComboBox cmbCPUOffice;
	private ComboBoxMultiselect cmbCPUOffice;
	private ComboBoxMultiselect cmbEmpName;
	private ComboBox cmbSource;
	private ComboBox cmbStatus;
	
	//CR-R1089 -- Start
	private PopupDateField fromDate;
	private PopupDateField toDate;	
	//CR-R1089 -- End	

	private String userName;

	private Button resetBtn;
	
	private BeanItemContainer<SelectValue> cpuCode;

	@Inject
	LumenDbService lumenService;
	
	@Inject
	MasterService masterService;
	
	@Inject
	SearchLevelOneResultTable lumenTable;
	
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Lumen Request - Level I");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<LumenSearchReqFormDTO>(LumenSearchReqFormDTO.class);
		this.binder.setItemDataSource(new LumenSearchReqFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();

		intimationNumber = binder.buildAndBind("Intimation Number", "intimationNumber", TextField.class);
		policyNumber = binder.buildAndBind("Policy Number","policyNumber",TextField.class);
//		cmbCPUOffice = binder.buildAndBind("CPU Office","cmbCPUOffice",ComboBox.class);
		cmbCPUOffice = new ComboBoxMultiselect("CPU Code");
		cmbCPUOffice.setShowSelectedOnTop(true);
		cmbCPUOffice.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<LumenSearchReqFormDTO> dtoBeanObject = binder.getItemDataSource();
				LumenSearchReqFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCpuCodeMulti(null);
				dtoObject.setCpuCodeMulti(event.getProperty().getValue());
			}
		});
		cmbEmpName = new ComboBoxMultiselect("Employee ID/Name"); //binder.buildAndBind("Employee ID/Name","cmbEmpName",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","cmbSource",ComboBox.class);
		cmbStatus = binder.buildAndBind("Status","cmbStatus",ComboBox.class);
		
		//CR-R1089 Search

		fromDate = binder.buildAndBind("From Date", "frmDate", PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setRangeEnd(new Date());
		
		toDate = binder.buildAndBind("To Date", "toDate", PopupDateField.class);
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setRangeEnd(new Date());
		

		FormLayout formLayoutLeft = new FormLayout(intimationNumber,policyNumber,cmbEmpName,fromDate);
		FormLayout formLayoutRight = new FormLayout(cmbSource,cmbStatus,cmbCPUOffice,toDate);
		
		//CR-R1089 Search END
	    
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		AbsoluteLayout searchIntimation_layout =  new AbsoluteLayout();
		searchIntimation_layout.addComponent(fieldLayout);
		searchIntimation_layout.addComponent(btnSearch, "top:180.0px;left:220.0px;");
		searchIntimation_layout.addComponent(resetBtn, "top:180.0px;left:329.0px;");
				
		mainVerticalLayout.addComponent(searchIntimation_layout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("750px");
		mainVerticalLayout.setMargin(false);
		
		//Vaadin8-setImmediate() searchIntimation_layout.setImmediate(false);
		searchIntimation_layout.setWidth("100.0%");
		searchIntimation_layout.setHeight("230px");
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		btnSearch.setWidth("-1px");
		btnSearch.setTabIndex(12);
		btnSearch.setHeight("-1px");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		addListener();
		setDropDownValues();
		return mainVerticalLayout;		
	}

	
	public void setDropDownValues(){
//		BeanItemContainer<SelectValue> cpuCodes = masterService.getCommonTmpCpuCodeList();
//		cmbCPUOffice.setContainerDataSource(cpuCodes);
//		cmbCPUOffice.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbCPUOffice.setItemCaptionPropertyId("value");
		
		cmbCPUOffice.setContainerDataSource(cpuCode);
		cmbCPUOffice.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCPUOffice.setItemCaptionPropertyId("value");
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		cpuCode = masterService.getTmpCpuCodeList();
		List<SelectValue> listCpuList = (List<SelectValue>) cpuCode.getItemIds();
		if(listCpuList != null && !listCpuList.isEmpty()){
			for (SelectValue selectValue : listCpuList) {
				if(!ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					filterList.add(selectValue);
				}
			}
		}
		cmbCPUOffice.removeAllItems();
		cpuCode.addAll(filterList);
		
		cmbCPUOffice.setContainerDataSource(cpuCode);
		cmbCPUOffice.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCPUOffice.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> lumenSourceTypes = masterService.getLumenSource();
		cmbSource.setContainerDataSource(lumenSourceTypes);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> lumenStatusTypes = lumenService.getLevelOneStatus();//masterService.getLumenStatus();
		cmbStatus.setContainerDataSource(lumenStatusTypes);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");
	}
	
	public void setEmpNameDropDownValue(){
		BeanItemContainer<SelectValue> empNames = masterService.getLumenEmployeeName();
		//setting empName drop down
		cmbEmpName.setContainerDataSource(empNames);
		cmbEmpName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEmpName.setFilteringMode(FilteringMode.CONTAINS);
		cmbEmpName.setTextInputAllowed(true);
		cmbEmpName.setItemCaptionPropertyId("value");
	}

	public void addListener() {

		btnSearch.removeClickListener(this);
		btnSearch.addClickListener(this);

		resetBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();	
			}
		});		
		
		cmbEmpName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<LumenSearchReqFormDTO> dtoBeanObject = binder.getItemDataSource();
				LumenSearchReqFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCmbEmpName(null);
				dtoObject.setCmbEmpName(event.getProperty().getValue());
			}
		});
	}

	public void resetAlltheValues(){
		intimationNumber.setValue("");
		policyNumber.setValue("");
		cmbCPUOffice.clear();
		cmbEmpName.clear();
		cmbSource.clear();
		cmbStatus.clear();
		toDate.clear();
		fromDate.clear();
		if(lumenTable != null){
			if(lumenTable.getPageable()==null)
				lumenTable.init("", false, false);
			lumenTable.getPageable().setPageNumber(1);
			lumenTable.resetTable();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
