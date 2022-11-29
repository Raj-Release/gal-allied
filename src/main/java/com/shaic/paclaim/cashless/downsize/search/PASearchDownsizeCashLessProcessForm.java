package com.shaic.paclaim.cashless.downsize.search;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PASearchDownsizeCashLessProcessForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Inject
    private PASearchDownsizeCashLessProcessTable searchDownsizeCashLessProcessTable;
	private BeanFieldGroup<PASearchDownsizeCashLessProcessFormDTO> binder;

	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	
//	private ComboBox cmbType;
//	private ComboBox cmbPriority;

	private Button downsizeSearchButton;
	
	private Button resetBtn;
	
	private VerticalLayout buildDownSizePreauthLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Searchable searchable;
	
	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public PASearchDownsizeCashLessProcessFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			PASearchDownsizeCashLessProcessFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (Exception e) {
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
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildDownSizePreauthLayout  = new VerticalLayout();
		Panel downsizePreauthPanel	= new Panel();
		//Vaadin8-setImmediate() downsizePreauthPanel.setImmediate(false);
		//downsizePreauthPanel.setWidth("850px");
		downsizePreauthPanel.setWidth("100%");
		downsizePreauthPanel.setHeight("50%");
		downsizePreauthPanel.setCaption("Downsize Pre-auth");
		downsizePreauthPanel.addStyleName("panelHeader");
		downsizePreauthPanel.addStyleName("g-search-panel");
		downsizePreauthPanel.setContent(buildDownsizeLayout());
		buildDownSizePreauthLayout.addComponent(downsizePreauthPanel);
		buildDownSizePreauthLayout.setComponentAlignment(downsizePreauthPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildDownSizePreauthLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<PASearchDownsizeCashLessProcessFormDTO>(
				PASearchDownsizeCashLessProcessFormDTO.class);
		this.binder
				.setItemDataSource(new PASearchDownsizeCashLessProcessFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildDownsizeLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		// absoluteLayout_3.setHeight("250px");
		 absoluteLayout_3.setHeight("148px");
		

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
//		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
//		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
//		mandatoryFields.add(txtIntimationNo);
//		mandatoryFields.add(txtPolicyNo);
//		
//		showOrHideValidation(false);
		
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2);
		searchFormLayout.setMargin(true);
		
		searchFormLayout.setWidth("100%");
		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		absoluteLayout_3.addComponent(searchFormLayout);
		
/*		HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);*/
		
		downsizeSearchButton = new Button();
		downsizeSearchButton.setCaption("Search");
		//Vaadin8-setImmediate() downsizeSearchButton.setImmediate(true);
		downsizeSearchButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		downsizeSearchButton.setWidth("-1px");
		downsizeSearchButton.setHeight("-10px");
		downsizeSearchButton.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(downsizeSearchButton, "top:80.0px;left:220.0px;");
		//Vaadin8-setImmediate() downsizeSearchButton.setImmediate(true);
		
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
		return verticalLayout; 
	}

	public void addListener() {
		downsizeSearchButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				downsizeSearchButton.setEnabled(true);
				if(null != txtIntimationNo.getValue() && !("").equals(txtIntimationNo.getValue())|| null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())){
						searchable.doSearch();
						searchDownsizeCashLessProcessTable.tablesize();
				}
				else
				{
					Notification.show("Please Enter Intimation Number or Policy Number",Notification.TYPE_ERROR_MESSAGE);
//					searchable.doSearch();
//					searchDownsizeCashLessProcessTable.tablesize();
				}
					
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
		
		Iterator<Component> componentIterator = buildDownSizePreauthLayout.iterator();
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

	}