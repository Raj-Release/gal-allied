package com.shaic.claim.registration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Pageable;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
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

public class RevisedSerachClaimRegistration extends ViewComponent{

	@Inject
	private Instance<ClaimRegistrationSearchTable> claimRegistrationTableinstance;
	
	private ClaimRegistrationSearchTable claimRegistrationSearchTable;
	
	private List<SearchClaimRegistrationTableDto>  searchList;
	
	private NewIntimationDto newIntimationDto;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private PopupDateField intimationDate;
	
	private ComboBox hospitalTypeCmb;
	
	private Button claimRegistrationSearchBtn;
	
	private Button resetBtn;
	
	private BeanFieldGroup<SearchClaimRegisterationFormDto> binder;
	
	private VerticalLayout buildConvertClaimSearchLayout;
	
	private SearchClaimRegisterationFormDto bean;
	
	//private BeanItemContainer<TmpCPUCode> cpuCodeValues;
		
	
	public void getSearchDTO() {
		try {
			
			this.binder.commit();
			Pageable pageable = claimRegistrationSearchTable.getPageable();
			bean.setPageable(pageable);
			bean = this.binder.getItemDataSource()
					.getBean();
		} catch (CommitException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@PostConstruct
	public void init()
	{
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildConvertClaimSearchLayout  = new VerticalLayout();
		Panel claimRegistrationSearchPanel	= new Panel();
		//Vaadin8-setImmediate() claimRegistrationSearchPanel.setImmediate(false);
		claimRegistrationSearchPanel.setWidth("850px");
		claimRegistrationSearchPanel.setHeight("50%");
		claimRegistrationSearchPanel.setCaption("Convert Claim type to Reimbursement");
		claimRegistrationSearchPanel.addStyleName("panelHeader");
		claimRegistrationSearchPanel.setContent(buildConvertClaimLayout());
		buildConvertClaimSearchLayout.addComponent(claimRegistrationSearchPanel);
		buildConvertClaimSearchLayout.setComponentAlignment(claimRegistrationSearchPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildConvertClaimSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchClaimRegisterationFormDto>(
				SearchClaimRegisterationFormDto.class);
		this.binder
				.setItemDataSource(new SearchClaimRegisterationFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildConvertClaimLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("250px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
				TextField.class);
		
		intimationDate = binder.buildAndBind("Intimation Date", "intimationDate",
				PopupDateField.class);
		
		hospitalTypeCmb = binder.buildAndBind("Hospital Type", "hospitalType",
				ComboBox.class);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(hospitalTypeCmb,intimationDate),new FormLayout(txtPolicyNo,txtIntimationNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		claimRegistrationSearchBtn = new Button();
		claimRegistrationSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() claimRegistrationSearchBtn.setImmediate(true);
		claimRegistrationSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		claimRegistrationSearchBtn.setWidth("-1px");
		claimRegistrationSearchBtn.setHeight("-10px");
		absoluteLayout_3
		.addComponent(claimRegistrationSearchBtn, "top:200.0px;left:354.0px;");
		//Vaadin8-setImmediate() claimRegistrationSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:200.0px;left:459.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		return verticalLayout;

	}

	public void addListener() {

		claimRegistrationSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {			
				getSearchDTO();
				fireViewEvent(SearchClaimRegistrationPresenter.SEARCH_CLAIMREGISTER_TABLE_SUBMIT,bean);
				
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//searable.doSearch();
				resetAlltheValues();
			}
		});
	}
	
	public void setCpuCode(BeanItemContainer<SelectValue> parameter){		
		//cpuCodeCmb.setContainerDataSource(parameter);
		hospitalTypeCmb.setContainerDataSource(parameter);
		hospitalTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cpuCodeCmb.setItemCaptionPropertyId("description");
		hospitalTypeCmb.setItemCaptionPropertyId("value");
		
	}
	
	public void resetAlltheValues(){
		
		
	}
	
	protected void setSearchResultList(List<SearchClaimRegistrationTableDto>  searchResult)
	{
		searchList = new ArrayList<SearchClaimRegistrationTableDto>();
		if(!searchResult.isEmpty()){		
		searchList.addAll(searchResult);
		}
	}

	public void setIntimationDto(NewIntimationDto intimationDto)
	{
		newIntimationDto = intimationDto;
		newIntimationDto.setPolicy(intimationDto.getPolicy());
	}
	
	
}
