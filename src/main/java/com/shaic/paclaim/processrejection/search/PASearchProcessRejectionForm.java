package com.shaic.paclaim.processrejection.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.processrejection.search.SearchProcessRejectionFormDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
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

public class PASearchProcessRejectionForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<SearchProcessRejectionFormDTO> binder;
	
	@Inject
	private SearchProcessRejectionTableDTO bean;          
	
	@EJB
	private PreauthService preauthService;
	

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	private ComboBox cmbType;
	
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbAccidentOrDeath;

	Button prSearchBtn;
	private Searchable searchable;
	//Added for reset button.
	Button resetBtn;
	
	
	private VerticalLayout buildProcessRejectionLayout ;
	
	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}
	
	
	public SearchProcessRejectionFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessRejectionFormDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@PostConstruct
	public void init() {
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildProcessRejectionLayout  = new VerticalLayout();
		Panel processRejectionPanel	= new Panel();
		//Vaadin8-setImmediate() processRejectionPanel.setImmediate(false);
		//processRejectionPanel.setWidth("850px");
		processRejectionPanel.setWidth("100%");
		processRejectionPanel.setHeight("50%");
		processRejectionPanel.setCaption("Process Rejection");
		processRejectionPanel.addStyleName("panelHeader");
		processRejectionPanel.addStyleName("g-search-panel");
		processRejectionPanel.setContent(buildProcessRejectionSearchLayuout());
		buildProcessRejectionLayout.addComponent(processRejectionPanel);
		buildProcessRejectionLayout.setComponentAlignment(processRejectionPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildProcessRejectionLayout);
		addListener();
	}
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessRejectionFormDTO>(SearchProcessRejectionFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessRejectionFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildProcessRejectionSearchLayuout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 //absoluteLayout_3.setHeight("250px");
		 absoluteLayout_3.setHeight("220px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		cmbType =  binder.buildAndBind("Type", "type", ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbAccidentOrDeath = binder.buildAndBind("Accident/Death","accidentDeath",ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbSource);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo,cmbType);
		FormLayout formLayout3 = new FormLayout(cmbPriority,cmbAccidentOrDeath);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3);
		searchFormLayout.setMargin(true);

		searchFormLayout.setWidth("100%");
		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		searchFormLayout.setSpacing(Boolean.TRUE);
		absoluteLayout_3.addComponent(searchFormLayout);
/*		HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);*/

		prSearchBtn = new Button();
		prSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() prSearchBtn.setImmediate(true);
		prSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		prSearchBtn.setWidth("-1px");
		prSearchBtn.setHeight("-10px");
		prSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(prSearchBtn, "top:140.0px;left:220.0px;");
		//absoluteLayout_3.setHeight("500px");
		//Vaadin8-setImmediate() prSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:140.0px;left:329.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("950px");
		verticalLayout.setSpacing(Boolean.TRUE);
		//verticalLayout.setHeight("500px");
		
		setDropDownValues();
		
		return verticalLayout; 

	}
	
	public void addListener() {

		prSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				prSearchBtn.setEnabled(true);
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
		
		Iterator<Component> componentIterator = buildProcessRejectionLayout.iterator();
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
					//Method to reset search table.
					removeTableFromLayout();
				}
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


  public void setDropDownValues(){
	
	BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
	
	cmbType.setContainerDataSource(selectValueForType);
	cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbType.setItemCaptionPropertyId("value");
	
	BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
	
	cmbPriority.setContainerDataSource(selectValueForPriority);
	cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbPriority.setItemCaptionPropertyId("value");
	
	Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
//	Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

	SelectValue selectValue1 = new SelectValue();
	selectValue1.setId(stageByKey2.getKey());
	selectValue1.setValue(stageByKey2.getStageName());
	
	BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
	statusByStage.addBean(selectValue1);
	
	cmbSource.setContainerDataSource(statusByStage);
	cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbSource.setItemCaptionPropertyId("value");
	
	SelectValue selectAccident = new SelectValue();
	selectAccident.setId(null);
	selectAccident.setValue(SHAConstants.ACCIDENT);
	
	SelectValue selectDeath = new SelectValue();
	selectDeath.setId(null);
	selectDeath.setValue(SHAConstants.DEATH);
	

	List<SelectValue> selectVallueList = new ArrayList<SelectValue>();		
	selectVallueList.add(selectAccident);
	selectVallueList.add(selectDeath);
	
	BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	selectValueContainer.addAll(selectVallueList);
	cmbAccidentOrDeath.setContainerDataSource(selectValueContainer);
	cmbAccidentOrDeath.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbAccidentOrDeath.setItemCaptionPropertyId("value");
	
  }
}

	/*public void addListener() {

		prSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SearchProcessRejectionFormDTO searchProcessRejectionFormDTO = new SearchProcessRejectionFormDTO();
				fireViewEvent(PASearchProcessRejectionPresenter.SEARCH_BUTTON_CLICK,
						searchProcessRejectionFormDTO);
			}
		});
	}*/


