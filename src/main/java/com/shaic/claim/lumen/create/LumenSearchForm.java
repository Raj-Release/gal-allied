package com.shaic.claim.lumen.create;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class LumenSearchForm extends SearchComponent<LumenSearchFormDTO>{

	private ComboBox searchType;
	private TextField intimationNumber;
	
//	private ComboBoxMultiselect cmbCPUOffice;
	//private TextField claimNumber;
	private TextField policyNumber;

	private Button resetBtn;

	@Inject
	LumenDbService lumenService;
	
	@Inject
	private LumenSearchResultTable lumenTable;
	
	@Inject
	private LumenPolicySearchResultTable lumenPolicyTable;

	@PostConstruct
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Initiate Lumen Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<LumenSearchFormDTO>(LumenSearchFormDTO.class);
		this.binder.setItemDataSource(new LumenSearchFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();
		searchType = binder.buildAndBind("Search Type","type",ComboBox.class);
		intimationNumber = binder.buildAndBind("Intimation Number", "intimationNumber", TextField.class);
		//claimNumber = binder.buildAndBind("Claim Number","claimNumber",TextField.class);
		policyNumber = binder.buildAndBind("Policy Number","policyNumber",TextField.class);
/*		cmbCPUOffice = new ComboBoxMultiselect("CPU Code");
		cmbCPUOffice.setShowSelectedOnTop(true);
		cmbCPUOffice.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<LumenSearchFormDTO> dtoBeanObject = binder.getItemDataSource();
				LumenSearchFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCpuCodeMulti(null);
				dtoObject.setCpuCodeMulti(event.getProperty().getValue());
			}
		});*/

		intimationNumber.setVisible(false);
		//claimNumber.setVisible(false);
		policyNumber.setVisible(false);

		FormLayout formLayoutType = new FormLayout(searchType);
		FormLayout formLayoutOne = new FormLayout(intimationNumber);
		//FormLayout formLayoutTwo = new FormLayout(claimNumber);
		
		FormLayout formLayoutTwo = new FormLayout(policyNumber);
		
/*		FormLayout formLayoutThree = new FormLayout(cmbCPUOffice);*/

		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);


		AbsoluteLayout searchlumen_layout =  new AbsoluteLayout();
		searchlumen_layout.addComponent(formLayoutType, "left: 42px; top: 3px;");
		searchlumen_layout.addComponent(formLayoutOne, "left: 10px; top: 45px;");
		searchlumen_layout.addComponent(formLayoutTwo, "left: 30px; top: 45px;");
/*		searchlumen_layout.addComponent(formLayoutThree, "left: 70px; top: 45px;");*/

		searchlumen_layout.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		searchlumen_layout.addComponent(resetBtn, "top:160.0px;left:329.0px;");

		mainVerticalLayout.addComponent(searchlumen_layout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("720px");
		mainVerticalLayout.setMargin(false);	

		//Vaadin8-setImmediate() searchlumen_layout.setImmediate(false);
		searchlumen_layout.setWidth("100.0%");
		searchlumen_layout.setHeight("220px");		

		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		btnSearch.setWidth("-1px");
		btnSearch.setTabIndex(4);
		btnSearch.setHeight("-1px");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		addListener();
		setDropDownValues();
		return mainVerticalLayout;		
	}

	public void setDropDownValues(){
		BeanItemContainer<SelectValue> lumenSearchType = lumenService.getLumenSearchType();
		searchType.setContainerDataSource(lumenSearchType);
		searchType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		searchType.setItemCaptionPropertyId("value");
		List<SelectValue> search_Type = (List<SelectValue>)searchType.getContainerDataSource().getItemIds();
		searchType.setValue(search_Type.get(0));
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

		searchType.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (searchType.getValue() != null && searchType.getValue().toString().equals("Intimation")) {
					intimationNumber.setVisible(true);
					intimationNumber.setValue("");
/*					cmbCPUOffice.setVisible(true);
					cmbCPUOffice.setValue("");*/
					policyNumber.setVisible(false);
					//claimNumber.setVisible(true);
					
					lumenTable.init("", false, false);
					lumenTable.setSubmitTableHeader();
					
				}else if(searchType.getValue() != null && searchType.getValue().toString().equals("Policy")){
					policyNumber.setVisible(true);
					policyNumber.setValue("");
					intimationNumber.setVisible(false);
/*					cmbCPUOffice.setVisible(false);*/
					
					lumenPolicyTable.init("", false, false);
					lumenPolicyTable.setSubmitTableHeader();
				}else{
					intimationNumber.setVisible(false);
					policyNumber.setVisible(false);
					//claimNumber.setVisible(false);
				}
			}

		});
	}

	public void resetAlltheValues(){
		if (searchType.getValue() != null && searchType.getValue().toString().equals("Intimation")) {
			intimationNumber.setValue("");
/*			cmbCPUOffice.setValue("");*/
		}else if(searchType.getValue() != null && searchType.getValue().toString().equals("Policy")){
			policyNumber.setValue("");
		}
		//claimNumber.setValue("");
		setDropDownValues();
		if(lumenTable != null){
			lumenTable.removeRow();
		}

		if(lumenPolicyTable != null){
			lumenPolicyTable.removeRow();
		}
	}

	public String doTypeValidation(){
		String returnVal = "";
		if(searchType.getValue() == null){
			returnVal = "Please select the Search Type.";
		}
		return returnVal;
	}

	public String doSearchValidation(){
		String returnVal = "";
		if(searchType.getValue().toString().equals("Intimation") && StringUtils.isBlank(intimationNumber.getValue())){
			returnVal = "Please enter Intimation Number.";
		}else if(searchType.getValue().toString().equals("Policy") && StringUtils.isBlank(policyNumber.getValue())){
			returnVal = "Please enter Policy Number.";
		}
		return returnVal;
	}
	
	public String getSearchType(){
		return (searchType.getValue() == null)?"":searchType.getValue().toString();
	}
}
