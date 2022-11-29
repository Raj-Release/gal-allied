package com.shaic.claim.fieldvisit.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchFieldVisitForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<SearchFieldVisitFormDTO> binder;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;

	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	
	private ComboBox cmbClaimType;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbcpuCode;
	
	private ComboBox cmbhospitalType;
	
	private ComboBox cmbproductCode;
	
	private ComboBox cmbfvrcpuCode;

	private Button fieldVisitSearchBtn;
	
	private DateField intimationDateField;
	
	private Searchable searchable;
	
	Button resetBtn;
	
	VerticalLayout buildFieldVisitSearchLayout;

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SearchFieldVisitFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchFieldVisitFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	@PostConstruct
	public void init() {
		initBinder();
		buildFieldVisitSearchLayout  = new VerticalLayout();
		Panel processEnhancementPanel	= new Panel();
		//Vaadin8-setImmediate() processEnhancementPanel.setImmediate(false);
		//processEnhancementPanel.setWidth("850px");
		processEnhancementPanel.setWidth("100%"); // Valid width
		processEnhancementPanel.setHeight("90%");
		processEnhancementPanel.setCaption("Process Field Visit Assignment");
		processEnhancementPanel.addStyleName("panelHeader");
		processEnhancementPanel.addStyleName("g-search-panel");
		processEnhancementPanel.setContent(buildPreauthSearchLayout());
		//processEnhancementPanel.
		buildFieldVisitSearchLayout.addComponent(processEnhancementPanel);
		buildFieldVisitSearchLayout.setComponentAlignment(processEnhancementPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildFieldVisitSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchFieldVisitFormDTO>(
				SearchFieldVisitFormDTO.class);
		this.binder
				.setItemDataSource(new SearchFieldVisitFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildPreauthSearchLayout() 
	{		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("300px"); 
//		 absoluteLayout_3.setHeight("150px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		txtIntimationNo.setValue(null);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbcpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		cmbhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		
		cmbproductCode = binder.buildAndBind("Product Code","productCode",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		intimationDateField = (DateField) binder.buildAndBind("Date of Admission", "intimatedDate",
				DateField.class);

		cmbfvrcpuCode = binder.buildAndBind("FVR CPU Code","fvrCpuCode",ComboBox.class);
		
		FormLayout firstForm = new FormLayout(txtIntimationNo,cmbfvrcpuCode,intimationDateField);
		firstForm.setMargin(false);
		
		FormLayout firstForm1 = new FormLayout(cmbClaimType,cmbcpuCode,cmbSource);
		firstForm1.setMargin(false);
		
		FormLayout firstForm2 = new FormLayout(txtPolicyNo,cmbhospitalType,cmbproductCode);
	    firstForm2.setMargin(false);
		
		FormLayout firstForm3 = new FormLayout(cmbPriority,cmbType);
		firstForm3.setMargin(false);
		
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(firstForm,firstForm1,firstForm2,firstForm3);
		searchFormLayout.setMargin(true);

		searchFormLayout.setWidth("100%");
//		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_CENTER);
		
		absoluteLayout_3.addComponent(searchFormLayout);
		
		fieldVisitSearchBtn = new Button();
		fieldVisitSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() fieldVisitSearchBtn.setImmediate(true);
		fieldVisitSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		fieldVisitSearchBtn.setWidth("-1px");
		fieldVisitSearchBtn.setHeight("-10px");
		fieldVisitSearchBtn.setDisableOnClick(true);
		//absoluteLayout_3
		//.addComponent(fieldVisitSearchBtn , "top:200.0px;left:354.0px;");	
		//Vaadin8-setImmediate() fieldVisitSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
	/*	GridLayout buttonLayout = new GridLayout(2,1);
		buttonLayout.addComponent(fieldVisitSearchBtn, 0,0);
		buttonLayout.addComponent(resetBtn , 1,0);

		
		HorizontalLayout cmdButtonPanel = new HorizontalLayout(buttonLayout);
		cmdButtonPanel.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		cmdButtonPanel.setWidth("100%");
		absoluteLayout_3.addComponent(cmdButtonPanel, "top:100.0px;left:200.0px;");*/	
		absoluteLayout_3.addComponent(fieldVisitSearchBtn, "top:120.0px;left:260.0px;");
		absoluteLayout_3.addComponent(resetBtn, "top:120.0px;left:369.0px;");	
//		verticalLayout.setHeight("500px");
		verticalLayout.addComponent(absoluteLayout_3);		
		//Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		verticalLayout.setWidth("1500px");
		// mainVerticalLayout.setHeight("500px");
		verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("175px");	
		 
		 
		
		setDropDownValues();
		
		return verticalLayout; 
	}

	public void addListener() {
		fieldVisitSearchBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fieldVisitSearchBtn.setEnabled(true);
				searchable.doSearch();
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
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildFieldVisitSearchLayout.iterator();
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
				}//Method to reset search table.
				removeTableFromLayout();
			}
		}
	
    public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		Stage stageByKey1 = preauthService.getStageByKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.PROCESS_PREAUTH);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE);	
		Stage stageByKey4 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		Stage stageByKey5 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);



		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey1.getKey());
		selectValue1.setValue(stageByKey1.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey2.getKey());
		selectValue2.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(stageByKey3.getKey());
		selectValue3.setValue(stageByKey3.getStageName());
		
		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(stageByKey4.getKey());
		selectValue4.setValue(stageByKey4.getStageName());
		
		SelectValue selectValue5 = new SelectValue();
		selectValue5.setId(stageByKey5.getKey());
		selectValue5.setValue(stageByKey5.getStageName());
		
		/*SelectValue selectValue6 = new SelectValue();
		selectValue5.setId(stageByKey1.getKey());
		selectValue5.setValue(stageByKey1.getStageName());*/
		
		
//		SelectValue selectValue2 = new SelectValue();
//		selectValue2.setId(stageByKey3.getKey());
//		selectValue2.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue3);
		statusByStage.addBean(selectValue4);
		statusByStage.addBean(selectValue5);
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});
		
		BeanItemContainer<SelectValue> hospitalTypeValue = masterService
				.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
		
		cmbhospitalType.setContainerDataSource(hospitalTypeValue);
		cmbhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbhospitalType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		
		cmbcpuCode.setContainerDataSource(selectValueContainerForCPUCode);
		cmbcpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbcpuCode.setItemCaptionPropertyId("value"); 
		
		BeanItemContainer<SelectValue> clmTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		SelectValue cashlessClmTypeSelect = new SelectValue(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY,SHAConstants.CASHLESS_CLAIM_TYPE);
		SelectValue reimbClmTypeSelect = new SelectValue(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY,SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		clmTypeContainer.addBean(cashlessClmTypeSelect);
		clmTypeContainer.addBean(reimbClmTypeSelect);
		
		cmbClaimType.setContainerDataSource(clmTypeContainer);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value"); 
		
		BeanItemContainer<SpecialSelectValue> productNameValue = masterService.getContainerForProduct();
		cmbproductCode.setContainerDataSource(productNameValue);
		cmbproductCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbproductCode.setItemCaptionPropertyId("specialValue");
		
		cmbfvrcpuCode.setContainerDataSource(selectValueContainerForCPUCode);
		cmbfvrcpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbfvrcpuCode.setItemCaptionPropertyId("value");
		
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