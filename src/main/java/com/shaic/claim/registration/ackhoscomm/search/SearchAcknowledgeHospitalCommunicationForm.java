package com.shaic.claim.registration.ackhoscomm.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchAcknowledgeHospitalCommunicationForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchAcknowledgeHospitalCommunicationTable searchAcknowledgeHospitalCommunicationTable;
	
	@EJB
	private PreauthService preauthService;

	private BeanFieldGroup<SearchAcknowledgeHospitalCommunicationFormDTO> binder;

	TextField txtIntimationNo;

	ComboBox cmbcpuCode;

	TextField txtPolicyNo;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	

	Button ahcSearchBtn;
	
	Button resetBtn;

	private Searchable searchable;
	
	VerticalLayout buildAckHospitalSearchLayuout ;

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SearchAcknowledgeHospitalCommunicationFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchAcknowledgeHospitalCommunicationFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostConstruct
	public void init() {
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildAckHospitalSearchLayuout  = new VerticalLayout();
		
		
		Panel hospSearchPanel	= new Panel();
		//Vaadin8-setImmediate() hospSearchPanel.setImmediate(false);
		hospSearchPanel.setWidth("100%");
		hospSearchPanel.setHeight("50%");
		hospSearchPanel.setCaption("Acknowledge Hospital Communication");
		hospSearchPanel.addStyleName("panelHeader");
		hospSearchPanel.addStyleName("g-search-panel");
		hospSearchPanel.setContent(buildAckHospSearchLayuout());
		buildAckHospitalSearchLayuout.addComponent(hospSearchPanel);
		buildAckHospitalSearchLayuout.setComponentAlignment(hospSearchPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildAckHospitalSearchLayuout);
		addListener();
		/*initBinder();
		VerticalLayout buildPreauthSearchLayuout = buildPreauthSearchLayuout();
		buildPreauthSearchLayuout.setWidth("100%");
		buildPreauthSearchLayuout.setHeight("100%");
		buildPreauthSearchLayuout.setSpacing(true);
		buildPreauthSearchLayuout.setMargin(true);
		Panel preauthPanel = new Panel("Acknowledge Hospital Communication");
		preauthPanel.setContent(buildPreauthSearchLayuout);
		setCompositionRoot(preauthPanel);

		addListener();*/
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchAcknowledgeHospitalCommunicationFormDTO>(
				SearchAcknowledgeHospitalCommunicationFormDTO.class);
		this.binder
				.setItemDataSource(new SearchAcknowledgeHospitalCommunicationFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildAckHospSearchLayuout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("210px");
//		 absoluteLayout_3.setHeight("170px");



		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		cmbcpuCode = binder.buildAndBind("CPU Code", "cpuCode", ComboBox.class);
		
//		fireViewEvent(SearchAcknowledgeHospitalCommunicationPresenter.GET_CPU_CODE, true);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbcpuCode,cmbSource);
		formLayout1.setMargin(true);
		FormLayout formLayout2 = new FormLayout( txtPolicyNo,cmbPriority,cmbType);
		formLayout2.setMargin(true);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
//		searchFormLayout.setMargin(true);
		absoluteLayout_3.addComponent(searchFormLayout);
		/*HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo,cmbcpuCode),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");*/
		
		
		ahcSearchBtn = new Button();
		ahcSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() ahcSearchBtn.setImmediate(true);
		ahcSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		ahcSearchBtn.setWidth("-1px");
		ahcSearchBtn.setHeight("-10px");
		ahcSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(ahcSearchBtn, "top:140.0px;left:230.0px;");
		//Vaadin8-setImmediate() ahcSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:140.0px;left:339.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.addStyleName("650px");
		//verticalLayout.addStyleName("500px");
		
		setDropDownValues();
		
		return verticalLayout; 


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
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.PREAUTH_STAGE);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE); 
		
//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getStageName());
		
//		SelectValue selectValue2 = new SelectValue();
//		selectValue2.setId(stageByKey3.getKey());
//		selectValue2.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue1);
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}
	
	public void addListener() {

		ahcSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ahcSearchBtn.setEnabled(true);
				searchable.doSearch();
				searchAcknowledgeHospitalCommunicationTable.tablesize();
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
		
		Iterator<Component> componentIterator = buildAckHospitalSearchLayuout.iterator();
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

	public void setCpuCode(BeanItemContainer<SelectValue> tmpCpuCodes) {
		
		cmbcpuCode.setContainerDataSource(tmpCpuCodes);
		cmbcpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbcpuCode.setItemCaptionPropertyId("id");
	}

	}