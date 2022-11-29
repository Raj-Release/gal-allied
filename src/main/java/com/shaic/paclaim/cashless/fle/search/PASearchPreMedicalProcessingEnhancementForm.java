package com.shaic.paclaim.cashless.fle.search;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
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

public class PASearchPreMedicalProcessingEnhancementForm extends ViewComponent {
	
	private static final long serialVersionUID = 7891553085503753364L;

	@Inject
	private PASearchPreMedicalProcessingEnhancementTable searchPreMedicalProcessingEnhancementTable;
	
	@EJB
	private PreauthService preauthService;

	private BeanFieldGroup<PASearchPreMedicalProcessingEnhancementFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	ComboBox cmbType;
	
	ComboBox cmbIntimationSource;
	
	ComboBox cmbNetworkHospType;
	
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	private ComboBox cbxCPUCode;
	
	private ComboBox cmbSortOrder;
	
	Button pmpeSearchBtn;
	Button resetBtn;
	
	private Searchable searchable;
	
	VerticalLayout buildPreauthEnhSearchLayuout;
	
	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}
	

	public PASearchPreMedicalProcessingEnhancementFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			PASearchPreMedicalProcessingEnhancementFormDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@PostConstruct
	public void init() {
		initBinder();
		
		buildPreauthEnhSearchLayuout  = new VerticalLayout();
		Panel preauthPanel	= new Panel();
		//Vaadin8-setImmediate() preauthPanel.setImmediate(false);
		preauthPanel.setWidth("100%");
		preauthPanel.setHeight("60%");
		preauthPanel.setCaption("First Level Processing (Enhancement)");
		preauthPanel.addStyleName("panelHeader");
		preauthPanel.addStyleName("g-search-panel");
		preauthPanel.setContent(buildPreauthSearchLayuout());
		buildPreauthEnhSearchLayuout.addComponent(preauthPanel);
		buildPreauthEnhSearchLayuout.setComponentAlignment(preauthPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildPreauthEnhSearchLayuout);
		addListener();
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PASearchPreMedicalProcessingEnhancementFormDTO>(PASearchPreMedicalProcessingEnhancementFormDTO.class);
		
		PASearchPreMedicalProcessingEnhancementFormDTO searchPreMedicalProcessingEnhancementFormDTO = new PASearchPreMedicalProcessingEnhancementFormDTO();
		SelectValue selectValue = new SelectValue();
		selectValue.setId(1l);
		selectValue.setValue(SHAConstants.FIFO);
		searchPreMedicalProcessingEnhancementFormDTO.setSortOrder(selectValue);
		this.binder.setItemDataSource(searchPreMedicalProcessingEnhancementFormDTO);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	

	private VerticalLayout buildPreauthSearchLayuout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("240px");


		txtIntimationNo =  binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cmbType =  binder.buildAndBind("Type", "type", ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo", TextField.class);
		cmbIntimationSource =  binder.buildAndBind("Intimation Source", "intimationSource", ComboBox.class);
		cmbNetworkHospType =  binder.buildAndBind("Network Hosp Type", "networkHospitalType", ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbSortOrder = binder.buildAndBind("Sort Order","sortOrder",ComboBox.class);
		
		BeanItemContainer<SelectValue> sortOrder = SHAUtils.getSortOrder();
		cmbSortOrder.setContainerDataSource(sortOrder);
		cmbSortOrder.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSortOrder.setItemCaptionPropertyId("value");
		
		// Set Default Value to First Option.
				Collection<?> itemIds = cmbSortOrder.getContainerDataSource().getItemIds();
				cmbSortOrder.setValue(itemIds.toArray()[0]);
				cmbSortOrder.select(itemIds.toArray()[0]);
				cmbSortOrder.setNullSelectionAllowed(false);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo, txtPolicyNo,cmbPriority,cmbSource,cmbSortOrder);
		FormLayout formLayout2 = new FormLayout(cmbType, cmbIntimationSource,cbxCPUCode, cmbNetworkHospType);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		
	//	HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo, txtPolicyNo), new FormLayout(cmbType, cmbIntimationSource, cmbNetworkHospType));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("100%");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		pmpeSearchBtn = new Button();
		pmpeSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() pmpeSearchBtn.setImmediate(true);
		pmpeSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pmpeSearchBtn.setWidth("-1px");
		pmpeSearchBtn.setHeight("-10px");
		pmpeSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(pmpeSearchBtn, "top:190.0px;left:250.0px;");
		//Vaadin8-setImmediate() pmpeSearchBtn.setImmediate(true);
		
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:190.0px;left:359.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("750px");
		//verticalLayout.setHeight("600px");
		return verticalLayout; 

	}
	
	public void addListener() {

		pmpeSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				pmpeSearchBtn.setEnabled(true);
				searchable.doSearch();
				searchPreMedicalProcessingEnhancementTable.tablesize();
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

	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildPreauthEnhSearchLayuout.iterator();
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

	
	
	public void setIntimationSrc(BeanItemContainer<SelectValue> parameter){		
		cmbIntimationSource.setContainerDataSource(parameter);
		cmbIntimationSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimationSource.setItemCaptionPropertyId("value");
		
	}
	
	public void setNetworkHospType(BeanItemContainer<SelectValue> parameter){		
		cmbNetworkHospType.setContainerDataSource(parameter);
		cmbNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNetworkHospType.setItemCaptionPropertyId("value");
	}

	
	public void setPreAuthType(BeanItemContainer<SelectValue> parameter){	
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		Status stageByKey2 = preauthService.getStatusByKey(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY);
		Status stageByKey3 = preauthService.getStatusByKey(ReferenceTable.REFER_TO_FLE);
//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getProcessValue());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getProcessValue());
		
//		SelectValue selectValue2 = new SelectValue();
//		selectValue2.setId(stageByKey3.getKey());
//		selectValue2.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});
		
		
	}


	public void setCpuCode(BeanItemContainer<SelectValue> cpuCodeContainer) {
		cbxCPUCode.setContainerDataSource(cpuCodeContainer);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
	}
}
