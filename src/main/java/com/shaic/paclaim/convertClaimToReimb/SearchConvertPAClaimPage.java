package com.shaic.paclaim.convertClaimToReimb;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimFormDto;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchConvertPAClaimPage extends ViewComponent {

	/**
	 * 
	 */
	
	@Inject
	private SearchConvertPAClaimTable searchResultTable;	
	
	@EJB
	private PreauthService preauthService;
	
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private ComboBox cmbHospType;
	
	private ComboBox cmbAccDeath;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbCpuCode;
	
	private Button convertClaimSearchBtn;
	private Button resetBtn;
	
	private BeanFieldGroup<SearchConvertClaimFormDto> binder;
	
	private VerticalLayout buildConvertClaimSearchLayout  = new VerticalLayout();

	private Searchable searchable;
	
	private BeanItemContainer<SelectValue> hospitalTypeContainer;
	private BeanItemContainer<SelectValue> cpuCodeContainer;
	private BeanItemContainer<SelectValue> sourceContainer;
	private BeanItemContainer<SelectValue> accDeathContainer;
	
	public void addSearchListener(Searchable searable) {
		this.searchable = searable;
	}
	
	public SearchConvertClaimFormDto getSearchDTO() {
		try {
			
			this.binder.commit();
			SearchConvertClaimFormDto bean = this.binder.getItemDataSource()
					.getBean();
			bean.setLobKey(ReferenceTable.PA_LOB_KEY);
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setDefaultValues(Map<String,Object> refereceData){
		
		if(refereceData != null && !refereceData.isEmpty()){
			
			if(refereceData.containsKey("hospitalTypeContainer") && refereceData.get("hospitalTypeContainer") != null){
			
				hospitalTypeContainer = (BeanItemContainer<SelectValue>)refereceData.get("hospitalTypeContainer");
			}
			
			if(refereceData.containsKey("cpuCodeContainer") && refereceData.get("cpuCodeContainer") != null){
				cpuCodeContainer = (BeanItemContainer<SelectValue>)refereceData.get("cpuCodeContainer");	
			}
			if(refereceData.containsKey("sourceContainer") && refereceData.get("sourceContainer") != null){
				sourceContainer = (BeanItemContainer<SelectValue>)refereceData.get("sourceContainer");
			}
			if(refereceData.containsKey("accDeathContainer") && refereceData.get("accDeathContainer") != null){
				accDeathContainer = (BeanItemContainer<SelectValue>)refereceData.get("accDeathContainer");
			}			
			setDropDownValues();			
		}
		
	}
	
	@PostConstruct
	public void init()
	{
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildConvertClaimSearchLayout  = new VerticalLayout();
		Panel convertClaimPanel	= new Panel();
		//Vaadin8-setImmediate() convertClaimPanel.setImmediate(false);
		convertClaimPanel.setWidth("100%");
//		convertClaimPanel.setHeight("50%");
		convertClaimPanel.setCaption("Convert Claim type to Reimbursement(In Process)");
		convertClaimPanel.addStyleName("panelHeader");
		convertClaimPanel.addStyleName("g-search-panel");
		convertClaimPanel.setContent(buildConvertClaimLayout());
		buildConvertClaimSearchLayout.addComponent(convertClaimPanel);
		buildConvertClaimSearchLayout.setSizeFull();
		buildConvertClaimSearchLayout.setComponentAlignment(convertClaimPanel, Alignment.TOP_LEFT);
		setSizeFull();
		setCompositionRoot(buildConvertClaimSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchConvertClaimFormDto>(
				SearchConvertClaimFormDto.class);
		this.binder
				.setItemDataSource(new SearchConvertClaimFormDto());
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
		 absoluteLayout_3.setHeight("135px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
				TextField.class);
		cmbCpuCode = binder.buildAndBind("CPU Code", "cpuCode",
				ComboBox.class);
		
		cmbHospType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbAccDeath = binder.buildAndBind("Accident / Death","accDeath",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbHospType);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo,cmbSource);
		FormLayout formLayout3 = new FormLayout(cmbCpuCode,cmbAccDeath);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1, formLayout2, formLayout3);

		searchFormLayout.setMargin(true);
		searchFormLayout.setSpacing(true);
		searchFormLayout.setWidth("890");
		searchFormLayout.setHeight("500px");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		convertClaimSearchBtn = new Button();
		convertClaimSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		convertClaimSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertClaimSearchBtn.setWidth("-1px");
		convertClaimSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(convertClaimSearchBtn, "top:100.0px;left:230.0px;");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		absoluteLayout_3.addComponent(resetBtn, "top:100.0px;left:339.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("900px");
		
		return verticalLayout;

	}

	public void addListener() {

		convertClaimSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {							
				convertClaimSearchBtn.setEnabled(true);
				searchable.doSearch();
				searchResultTable.tablesize();
//				fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, tableDto);      //to testing purpose by yosuva
				
				
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();
			}
		});
	}
	
	public void setCpuCode(BeanItemContainer<SelectValue> parameter){		
		cmbCpuCode.setContainerDataSource(parameter);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		
		parameter.sort(new Object[] {"value"}, new boolean[] {true});
		
	}
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildConvertClaimSearchLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  Panel )
				{	
					Panel panel = (Panel)searchScrnComponent;
					Iterator<Component> searchScrnCompIter = panel.iterator();
					while (searchScrnCompIter.hasNext())
					{
						Component verticalLayoutComp = searchScrnCompIter.next();
						VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
						Iterator<Component> vLayoutIter = vLayout.iterator();
						while(vLayoutIter.hasNext())
						{
							Component absoluteComponent = vLayoutIter.next();
							AbsoluteLayout absLayout = (AbsoluteLayout)absoluteComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component horizontalComp = absLayoutIter.next();
								if(horizontalComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component formComp = formLayComp.next();
										FormLayout fLayout = (FormLayout)formComp;
										Iterator<Component> formComIter = fLayout.iterator();
									
										while(formComIter.hasNext())
										{
											Component indivdualComp = formComIter.next();
											if(indivdualComp != null) 
											{
												if(indivdualComp instanceof Label) 
												{
													continue;
												}	
												if(indivdualComp instanceof TextField) 
												{
													TextField field = (TextField) indivdualComp;
													field.setValue("");
												} 
												else if(indivdualComp instanceof ComboBox)
												{
													ComboBox field = (ComboBox) indivdualComp;
													field.setValue(null);
												}	 
									// Remove the table if exists..	
									//removeTableFromLayout();
											}
										}
									}
								}
							}
						}
					}
				}
				//Method to reset search table.
				removeTableFromLayout();
			}
		}

public void setDropDownValues(){		
		
		cmbCpuCode.setContainerDataSource(cpuCodeContainer);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
				
		cmbHospType.setContainerDataSource(hospitalTypeContainer);
		cmbHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospType.setItemCaptionPropertyId("value");
		
		cmbAccDeath.setContainerDataSource(accDeathContainer);
		cmbAccDeath.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAccDeath.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(sourceContainer);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}
	

	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
		}
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
}
