package com.shaic.reimbursement.investigationmaster;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reports.automationdashboard.AutomationDashboardPresenter;
import com.shaic.domain.InvestigationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class InvestigationMasterForm extends SearchComponent<InvestigationMasterFormDTO> {
	
	@Inject
	private InvestigationMasterTable searchTable;
	
	@Inject
	private InvestigationMasterDTO investigationMasterDTO;
	
	@Inject
	private InvestigationMasterTableDTO investigationMasterTableDTO;
	
	@EJB
	private InvestigationService investigationService;
	
	
	private ComboBox cmbInvestigatorType;
	
	private ComboBox cmbInvestigatorName;
	
	private Button createBtn;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Investigator Master");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		btnReset.setCaption("Clear");

		createBtn = new Button();
		createBtn.setCaption("Create New Investigator");
		createBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		createBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(MenuPresenter.INVESTIGATION_MASTER,investigationMasterTableDTO);
				
			}
		});
		mainVerticalLayout = new VerticalLayout();


		cmbInvestigatorType = binder.buildAndBind("Investigator Type","investigatorType",ComboBox.class);
		
		cmbInvestigatorName = binder.buildAndBind("Investigator Name","investigatorName",ComboBox.class);

		cmbInvestigatorType.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				BeanItemContainer<SelectValue> selectValueForInvestigatorName = investigationService.getInvestigatorNameList();
				BeanItemContainer<SelectValue> selectValueForPrivateInvestigatorName = investigationService.getPrivateInvestigatorNameList();
				BeanItemContainer<SelectValue> selectValueForOutSourceInvestigatorName = investigationService.getInvestigatorOutSourceNameList();
				BeanItemContainer<SelectValue> selectValueForRVOInvestigatorName = investigationService.getInvestigatorRVONameList();
				
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private")) {
					
					cmbInvestigatorName.setContainerDataSource(selectValueForPrivateInvestigatorName);
					cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbInvestigatorName.setItemCaptionPropertyId("value");
					
				} 
				else if(value != null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Outsource")) {
					
					cmbInvestigatorName.setContainerDataSource(selectValueForOutSourceInvestigatorName);
					cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbInvestigatorName.setItemCaptionPropertyId("value");
					
				} 
				else if(value != null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("RVO")) {
					
					cmbInvestigatorName.setContainerDataSource(selectValueForRVOInvestigatorName);
					cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbInvestigatorName.setItemCaptionPropertyId("value");
					
				} else {
					
					cmbInvestigatorName.setContainerDataSource(selectValueForInvestigatorName);
					cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbInvestigatorName.setItemCaptionPropertyId("value");
				}
				
			}
		});
		
		FormLayout formLayoutLeft = new FormLayout(cmbInvestigatorType);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(cmbInvestigatorName);
		formLayoutRight.setSpacing(true);


		HorizontalLayout investigatorHorLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		investigatorHorLayout.setMargin(true);
		investigatorHorLayout.setWidth("100%");		

		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(investigatorHorLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:185.0px;left:370.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:185.0px;left:470.0px;");
		absoluteLayout_3.addComponent(createBtn, "top:185.0px;left:570.0px;");



		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setWidth("1000px");
		mainVerticalLayout.setMargin(false);		 
		absoluteLayout_3.setWidth("100.0%");

		absoluteLayout_3.setHeight("224px");
		addListener();
		resetListener();

		return mainVerticalLayout;
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<InvestigationMasterFormDTO>(InvestigationMasterFormDTO.class);
		this.binder.setItemDataSource(new InvestigationMasterFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setReferences( BeanItemContainer<SelectValue> investigatorTypeContainer, BeanItemContainer<SelectValue> investigatorNameContainer,
			BeanItemContainer<SelectValue> privateInvestigatorNameContainer) {
		
		cmbInvestigatorType.setContainerDataSource(investigatorTypeContainer);
		cmbInvestigatorType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInvestigatorType.setItemCaptionPropertyId("value");
		
	/*	cmbInvestigatorName.setContainerDataSource(investigatorNameContainer);
		cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInvestigatorName.setItemCaptionPropertyId("value");*/
		
		
		/*if(cmbInvestigatorType  !=null && cmbInvestigatorType.getValue() !=null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private")){
			
			cmbInvestigatorName.setContainerDataSource(privateInvestigatorNameContainer);
			cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbInvestigatorName.setItemCaptionPropertyId("value");
			
		}else{
			cmbInvestigatorName.setContainerDataSource(investigatorNameContainer);
			cmbInvestigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbInvestigatorName.setItemCaptionPropertyId("value");
			
		}*/
		
	}
	
	private void resetListener() {
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				cmbInvestigatorType.setValue(null);	
				cmbInvestigatorName.setValue(null);
				
			}
		});
		
		
		
		
	}
	
public void showErrorPopup(String errorMessage) {
    Label label = new Label(errorMessage, ContentMode.HTML);
    label.setStyleName("errMessage");
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.addComponent(label);

    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setCaption("Error");
    dialog.setClosable(true);
    dialog.setContent(layout);
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.show(getUI().getCurrent(), null, true);

    return;
}
	
}