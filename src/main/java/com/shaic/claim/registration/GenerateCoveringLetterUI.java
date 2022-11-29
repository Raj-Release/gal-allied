package com.shaic.claim.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings("serial")
public class GenerateCoveringLetterUI extends ViewComponent {
	
	@EJB
	MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	private Panel claimSearchPanel;
	private VerticalLayout searchClaimVerticalLayout;
	private HorizontalLayout buttonsLayout;
	private FormLayout formLayout1;
	private FormLayout formLayout2;
	private TextField intimationNumberText;
	private TextField policyNumberText;
//	private TextField claimNumberText;
	private PopupDateField registeredDate;
	private ComboBox cpuCodeCombo;
	private Button searchButton;
	private Button resetButton;
	
	private ComboBox cmbPriority;
	
	private ComboBox cmbPriorityNew;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbType;

//	@Inject
//	private Instance<GenerateCoveringLetterSearchTable> genCovLetterSearchTableInstance;
	
	@Inject
	private GenerateCoveringLetterSearchTable genCovLetterSearchTable;
	
	private Map<String,Object> filters;
	private String letterType;
	private List<SearchClaimCoveringLetterTableDto> searchList = new ArrayList<SearchClaimCoveringLetterTableDto>();
	
	private AbsoluteLayout absoluteLayout;
	
	HorizontalLayout searchFormHLayout;
	
	private VerticalLayout wholeLayout;
		

	@PostConstruct	
	public void init()
	{
		setSizeFull();
		wholeLayout = new VerticalLayout();
		
		genCovLetterSearchTable.init("", false, false);

//				genCovLetterSearchTableInstance.get();
	
		buildClaimSearchPanel();
		
		wholeLayout.addComponent(claimSearchPanel);
		wholeLayout.addComponent(genCovLetterSearchTable);
				
		setCompositionRoot(wholeLayout);
	}
	
	public void init(String letterType)
	{
		setSizeFull();
		wholeLayout = new VerticalLayout();
		buildClaimSearchPanel();
		wholeLayout.addComponent(claimSearchPanel);
		
//		genCovLetterSearchTable = genCovLetterSearchTableInstance.get();
		
//		wholeLayout.addComponent(genCovLetterSearchTable);
		
		setCompositionRoot(wholeLayout);
		
	}

	private void buildClaimSearchPanel(){ 
		
		claimSearchPanel= new Panel("Generate Covering Letter");
		claimSearchPanel.setStyleName("panelHeader");
		claimSearchPanel.setWidth("100%");
		claimSearchPanel.setHeight("50%");
		claimSearchPanel.addStyleName("g-search-panel");
		
		buildSearchClaimVerticalLayout();
		
			
	}
	
	private void buildSearchClaimVerticalLayout(){
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("235px");
		
		
//		  buildAbsoluteLayout();
		
		formLayout1 = buildFormLayout1();
		formLayout2 = buildFormLayout2();
		
		setDropDownValues();
		
		searchFormHLayout = new HorizontalLayout(formLayout1,formLayout2);
		searchFormHLayout.setSpacing(true);
//		searchFormHLayout.setMargin(true);

		searchButton = new Button("Search");
		searchButton.setHeight("-1px");
		searchButton.setWidth("-1px");
		searchButton.setTabIndex(5);
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		searchButton.setData(letterType);
		searchButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchButton.setDisableOnClick(true);
		searchButton.addClickListener(new Button.ClickListener(){
			
			public void buttonClick(ClickEvent event) {
				searchButton.setEnabled(true);
				filters = new HashMap<String,Object>();
				
//				if(!ValidatorUtils.isNull(claimNumberText.getValue()) && claimNumberText.getValue() != "")
//				{
//					filters.put("claimNumber", claimNumberText.getValue());
//				}
				if(!ValidatorUtils.isNull(policyNumberText.getValue()) && policyNumberText.getValue() != "")
				{
						filters.put("policyNumber", policyNumberText.getValue());
				}
				if(!ValidatorUtils.isNull(registeredDate.getValue()))
				{
					filters.put("registeredDate", registeredDate.getValue());
				}
				if(!ValidatorUtils.isNull(intimationNumberText.getValue()) && intimationNumberText.getValue() != "")
				{
					filters.put("intimationNumber", intimationNumberText.getValue());
				}
				if(!ValidatorUtils.isNull(cpuCodeCombo.getValue()) && cpuCodeCombo.getValue() != "")
				{	
					SelectValue selected = (SelectValue) cpuCodeCombo.getValue();
					if(selected != null && selected.getId() != null){
						filters.put("cpuCode", selected.getId().toString());
					}else{
						filters.put("cpuCode",cpuCodeCombo.getValue());
					}
				}	
				
				if(!ValidatorUtils.isNull(cmbPriority.getValue()) && cmbPriority.getValue() != "")
				{	
					filters.put("priority", cmbPriority.getValue());
				}	
				
				if(!ValidatorUtils.isNull(cmbType.getValue()) && cmbType.getValue() != "")
				{	
					filters.put("type", cmbType.getValue());
				}	
				
				if(!ValidatorUtils.isNull(cmbSource.getValue()) && cmbSource.getValue() != "")
				{	
					filters.put("source", cmbSource.getValue());
				}	
				
				if(!ValidatorUtils.isNull(cmbPriorityNew.getValue()) && cmbPriorityNew.getValue() != "")
				{	
					filters.put("priorityNew", cmbPriorityNew.getValue());
				}
				
				fireViewEvent(GenerateCoveringLetterPresenter.INITIATE_SEARCH, null);
			}
		});		
		//Vaadin8-setImmediate() searchButton.setImmediate(true);		
		
		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(searchButton);
		absoluteLayout_3
		.addComponent(searchButton, "top:180.0px;left:230.0px;");
		
		resetButton = new Button("Reset");
		resetButton.setHeight("-1px");
		resetButton.setWidth("-1px");
		resetButton.setTabIndex(6);
		//Vaadin8-setImmediate() resetButton.setImmediate(true);
		resetButton.addStyleName(ValoTheme.BUTTON_DANGER);
		
		resetButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				resetValues();
				
				BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));
			}
		});
		absoluteLayout_3.addComponent(resetButton, "top:180.0px;left:339.0px;");
		
	
		absoluteLayout_3.addComponent(searchFormHLayout);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("750px");
		verticalLayout.setHeight("250px");
		claimSearchPanel.setContent(verticalLayout);
		
	}
	
	public void resetValues(){
		
		intimationNumberText.setValue("");
		policyNumberText.setValue("");
//		claimNumberText.setValue("");
		registeredDate.setValue(null);
		cpuCodeCombo.setValue(null);
		cmbSource.setValue(null);
		cmbPriority.setValue(null);
		cmbType.setValue(null);
	
		if(genCovLetterSearchTable != null && wholeLayout.getComponentCount()>1){
			
//			searchClaimVerticalLayout.removeComponent(genCovLetterSearchTable);
			genCovLetterSearchTable.resetTable();
			genCovLetterSearchTable.getPageable().setPageNumber(1);
			genCovLetterSearchTable.removeRow(); 
		}
	}
	
	
//	private AbsoluteLayout buildAbsoluteLayout() {
	
//	private GridLayout buildGridLayout(){	
//		buttonsLayout = new GridLayout(2,2);
//		buttonsLayout.setHeight("-1px");
//		buttonsLayout.setWidth("50%");
//		buttonsLayout.setSpacing(true);
//		formLayout1 = buildFormLayout1();
//		buttonsLayout.addComponent(formLayout1, 0, 0);
//		formLayout2 = buildFormLayout2();
//		buttonsLayout.addComponent(formLayout2, 1, 0);
//		return buttonsLayout;
//	}
	
    
	
//	private void buildAbsoluteLayout()
//	{
	private FormLayout buildFormLayout1(){	
    	formLayout1 = new FormLayout();
    	formLayout1.setSpacing(true);
//    	formLayout1.setSizeFull();
		
//    	absoluteLayout = new AbsoluteLayout();
//		//Vaadin8-setImmediate() absoluteLayout.setImmediate(false);
//		absoluteLayout.setWidth("100.0%");
//		absoluteLayout.setHeight("200px");
		
    	intimationNumberText = new TextField("Intimation No");
    	intimationNumberText.setHeight("-1px");
    	intimationNumberText.setWidth("180px");
    	intimationNumberText.setTabIndex(1);
    	//Vaadin8-setImmediate() intimationNumberText.setImmediate(true);
    	
    	CSValidator intimationNumValidator = new CSValidator();
    	intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
    	intimationNumValidator.extend(intimationNumberText);
		intimationNumValidator.setPreventInvalidTyping(true);
    	formLayout1.addComponent(intimationNumberText);
    	
    	registeredDate = new PopupDateField("Registered Date");
    	registeredDate.setWidth("180px");
    	registeredDate.setHeight("-1px");
    	registeredDate.setTabIndex(2);
    	registeredDate.setDateFormat("dd/MM/yyyy");
    	//Vaadin8-setImmediate() registeredDate.setImmediate(true);
    	formLayout1.addComponent(registeredDate);
    	
    	cmbPriority = new ComboBox("Priority(IRDA)");
		cmbPriority.setHeight("-1px");
		cmbPriority.setWidth("180px");
		formLayout1.addComponent(cmbPriority);
		
		
		cmbType = new ComboBox("Type");
		cmbType.setHeight("-1px");
		cmbType.setWidth("180px");
		cmbType.setEnabled(false);
		formLayout1.addComponent(cmbType);
    	
//    	claimNumberText = new TextField("Claim Number");
//    	claimNumberText.setHeight("-1px");
//    	claimNumberText.setWidth("180px");
//    	claimNumberText.setTabIndex(5);
//    	
//    	CSValidator claimNumValidator = new CSValidator();
//    	claimNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
//    	claimNumValidator.setPreventInvalidTyping(true);
//    	claimNumValidator.extend(claimNumberText);
//    	formLayout1.addComponent(claimNumberText);
    		
//		FormLayout formLayout1 = new FormLayout(intimationNumberText, registeredDate,claimNumberText);
//		FormLayout formLayout2 = new FormLayout(policyNumberText, cpuCodeCombo);
		
//		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2);
//		searchFormLayout.setMargin(true);
//		searchFormLayout.setWidth("100%");
//		absoluteLayout.addComponent(searchFormLayout);
		formLayout1.setMargin(true);
    	return formLayout1;
    	}	
	
	public FormLayout buildFormLayout2(){ 
		
		formLayout2 = new FormLayout();
//		formLayout2.setSizeFull();
		formLayout2.setSpacing(true);
		
		policyNumberText = new TextField("Policy No");
		policyNumberText.setHeight("-1px");
		policyNumberText.setWidth("180px");
		policyNumberText.setTabIndex(3);
		//Vaadin8-setImmediate() policyNumberText.setImmediate(true);
		CSValidator policyNumValidator = new CSValidator();
		policyNumValidator.extend(policyNumberText);
		policyNumValidator.setPreventInvalidTyping(true);
		policyNumValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		formLayout2.addComponent(policyNumberText);
		
		
		
		cmbSource = new ComboBox("Source");
		cmbSource.setHeight("-1px");
		cmbSource.setWidth("180px");
		
		cpuCodeCombo = new ComboBox("CPU Code");
		cpuCodeCombo.setHeight("-1px");
		cpuCodeCombo.setWidth("180px");
		cpuCodeCombo.setTabIndex(4);
		
		cmbPriorityNew = new ComboBox("Priority");
		cmbPriorityNew.setHeight("-1px");
		cmbPriorityNew.setWidth("180px");
		
		formLayout2.addComponent(cpuCodeCombo);
		formLayout2.addComponent(cmbSource);
		formLayout2.addComponent(cmbPriorityNew);
		formLayout2.setMargin(true);
		return formLayout2;
	
	}
	
	public void showSearchResultTable(com.shaic.arch.table.Page<GenerateCoveringLetterSearchTableDto> resultTablePageList){
		
		if(genCovLetterSearchTable != null && wholeLayout.getComponentCount()>1){
			
//			wholeLayout.removeComponent(genCovLetterSearchTable); 
		}
		
		List<GenerateCoveringLetterSearchTableDto> resultTableList = resultTablePageList.getPageItems();
		
		if(null != resultTableList && !resultTableList.isEmpty()){
		genCovLetterSearchTable.setTableList(resultTablePageList);
		
		for (GenerateCoveringLetterSearchTableDto tableDto : resultTableList) {
			if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
				genCovLetterSearchTable.setRowColor(tableDto);
			}
		}
//		VerticalLayout verticalLayout = new VerticalLayout(genCovLetterSearchTable);
//		verticalLayout.setHeight("100px");
//		Panel panel = new Panel(verticalLayout);
//		panel.setHeight("450px");
//		wholeLayout.addComponent(panel);
		}
		else{
			
			showInformation("No Result Found for Search");
//			showErrorPopup("No Result Found for Search");
//			Notification.show("No Result Found for Search");
		}
		
		
	}
	
	public void showErrorPopup(String errorMessage){
		Label label = new Label(errorMessage, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		return;
	}
	
	public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> cpuCodeContainer = masterService.getTmpCpuCodes(); 
		cpuCodeCombo.setContainerDataSource(cpuCodeContainer);
		cpuCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY); 
		cpuCodeCombo.setItemCaptionPropertyId("value");
		//Vaadin8-setImmediate() cpuCodeCombo.setImmediate(true);
		
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		selectValueForPriority.removeItem(selectValueForPriority.getIdByIndex(0));
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");*/
		
		BeanItemContainer<SelectValue> selectValueForPriorityIRDA = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriorityIRDA);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");

		BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);

//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.INTIMATION_STAGE_KEY);
		
//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(2l);
		selectValue2.setValue(SHAConstants.CONVERT_CLAIM);

//		SelectValue selectValue3 = new SelectValue();
//		selectValue3.setId(stageByKey3.getKey());
//		selectValue3.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
//		statusByStage.addBean(selectValue3);
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}
	
	public void showInformation(String info){
		
		Label information = new Label("<b style = 'color: black;'>" + info +".</b>", ContentMode.HTML);			
		Button homeButton = new Button("Generate Covering Letter Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(information, homeButton);
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
				fireViewEvent(MenuItemBean.GENERATE_COVERINGLETTER, null);
				
			}
		});
	}
	
	public Map<String,Object> getSearchFilter(){
		return this.filters;
	}

	public GenerateCoveringLetterSearchTable getGenCovLetterSearchTable(){
		return this.genCovLetterSearchTable;
	}
	
	public void resetComponents(){
		SHAUtils.resetComponent(searchClaimVerticalLayout);
	}
	
	
	
}
