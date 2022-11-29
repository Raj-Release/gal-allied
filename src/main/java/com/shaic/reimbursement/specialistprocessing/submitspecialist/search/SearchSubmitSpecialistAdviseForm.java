/**
 * 
 */
package com.shaic.reimbursement.specialistprocessing.submitspecialist.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchSubmitSpecialistAdviseForm extends SearchComponent<SearchSubmitSpecialistAdviseFormDTO> {
	
	@Inject
	private SearchSubmitSpecialistAdviseTable searchTable;
	
	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private ComboBox cbxCPUCode;
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Submit Specialist Advise");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtClaimNo = binder.buildAndBind("Claim Number","claimNo",TextField.class);
		
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode);
		FormLayout formLayoutReight = new FormLayout(txtClaimNo);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("90%");
		fieldLayout.setSpacing(true);
		fieldLayout.setHeight("140px");
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setWidth("100%");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		buttonlayout.setMargin(false);		
		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
//		mainVerticalLayout.setWidth("50%");
		mainVerticalLayout.setHeight("190px");
		//mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchSubmitSpecialistAdviseFormDTO>(SearchSubmitSpecialistAdviseFormDTO.class);
		this.binder.setItemDataSource(new SearchSubmitSpecialistAdviseFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCPUCode(BeanItemContainer<SelectValue> parameter) {
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
	}	
}