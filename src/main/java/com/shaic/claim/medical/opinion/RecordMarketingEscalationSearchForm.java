package com.shaic.claim.medical.opinion;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class RecordMarketingEscalationSearchForm extends ViewComponent {

	/**
	 * 
	 */
	@Inject
	private SearchRecordMarkEscTable searchRecordMarkEscTable;
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<SearchRecordMarkEscFormDTO> binder;

	TextField txtIntimationNo;

	//TextField txtPolicyNo;
	
	

	Button SearchBtn;
	
	private Searchable searchable;
	
	Button resetBtn;	
	
	private VerticalLayout buildRecMarkEscSearchLayout ;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SearchRecordMarkEscFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchRecordMarkEscFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostConstruct
	public void init() 
	{
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildRecMarkEscSearchLayout  = new VerticalLayout();
		Panel processPedProcessRequestPanel	= new Panel();
		//Vaadin8-setImmediate() processPedProcessRequestPanel.setImmediate(false);
		//processPedProcessRequestPanel.setWidth("850px");
		processPedProcessRequestPanel.setWidth("100%");
		processPedProcessRequestPanel.setHeight("50%");
		processPedProcessRequestPanel.setCaption("Record Marketing Escalations");
		processPedProcessRequestPanel.addStyleName("panelHeader");
		processPedProcessRequestPanel.addStyleName("g-search-panel");
		processPedProcessRequestPanel.setContent(buildPedProcessRequestLayout());
		buildRecMarkEscSearchLayout.addComponent(processPedProcessRequestPanel);
		buildRecMarkEscSearchLayout.setComponentAlignment(processPedProcessRequestPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildRecMarkEscSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchRecordMarkEscFormDTO>(
				SearchRecordMarkEscFormDTO.class);
		this.binder
				.setItemDataSource(new SearchRecordMarkEscFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildPedProcessRequestLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 //absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("118px");
//		 absoluteLayout_3.setHeight("150px");
		 
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		/*txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);*/
		
		
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo);
		//FormLayout formLayout2 = new FormLayout(txtPolicyNo);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1);
		searchFormLayout.setMargin(true);
		
		searchFormLayout.setWidth("100%");
//		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		absoluteLayout_3.addComponent(searchFormLayout);
		
		
	/*	HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);*/
		
		SearchBtn = new Button();
		SearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() pedrpSearchBtn.setImmediate(true);
		SearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		SearchBtn.setWidth("-1px");
		SearchBtn.setHeight("-10px");
		SearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(SearchBtn, "top:80.0px;left:220.0px;");
		//Vaadin8-setImmediate() pedrpSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:80.0px;left:329.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("650px");
		//verticalLayout.setHeight("500px");
		
		return verticalLayout; 

	}

	public void addListener() {
		SearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SearchBtn.setEnabled(true);
				searchable.doSearch();
				searchRecordMarkEscTable.tablesize();
				//fireViewEvent(MenuPresenter.PED_REQUEST_PAGE_PROCESS, null);
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
		
		Iterator<Component> componentIterator = buildRecMarkEscSearchLayout.iterator();
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

	public void refresh() {
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
	
	public Boolean validate(){
		
		if(txtIntimationNo.getValue() == null || txtIntimationNo.getValue().equals("")){
			return false;
		}
		
		return true;
	}

	}
