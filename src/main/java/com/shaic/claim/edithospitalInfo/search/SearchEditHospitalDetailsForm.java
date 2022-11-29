package com.shaic.claim.edithospitalInfo.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@ViewScoped
public class SearchEditHospitalDetailsForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private DateField dateField;
	
	private Button editHospitalInfoSearchBtn;
	private Button resetBtn;
	
	private BeanFieldGroup<SearchEditHospitalDetailsFormDTO> binder;
	
	private VerticalLayout buildSearchEditHospitalInfoLayout;
	
	//private BeanItemContainer<TmpCPUCode> cpuCodeValues;
	
	private Searchable searchable;
	
	@Inject
	private SearchEditHospitalDetailsTable searchEditHospitalDetailsTable;

	
	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}
	
	public SearchEditHospitalDetailsFormDTO getSearchDTO() {
		try {
			
			this.binder.commit();
			SearchEditHospitalDetailsFormDTO bean = this.binder.getItemDataSource()
					.getBean();
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
		buildSearchEditHospitalInfoLayout  = new VerticalLayout();
		Panel editHospitalInfoPanel	= new Panel();
		//Vaadin8-setImmediate() editHospitalInfoPanel.setImmediate(false);
		editHospitalInfoPanel.setWidth("100%");
		editHospitalInfoPanel.setHeight("50%");
		editHospitalInfoPanel.setCaption("Edit Hospital Details");
		editHospitalInfoPanel.addStyleName("panelHeader");
		editHospitalInfoPanel.addStyleName("g-search-panel");
		editHospitalInfoPanel.setContent(buildhospitalInfoSearchLayout());
		buildSearchEditHospitalInfoLayout.addComponent(editHospitalInfoPanel);
		buildSearchEditHospitalInfoLayout.setComponentAlignment(editHospitalInfoPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildSearchEditHospitalInfoLayout);
		addListener();
	}
	
	
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchEditHospitalDetailsFormDTO>(
				SearchEditHospitalDetailsFormDTO.class);
		this.binder
				.setItemDataSource(new SearchEditHospitalDetailsFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildhospitalInfoSearchLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("184px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		dateField = binder.buildAndBind("Intimation Date", "intimationDate",
				DateField.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,txtPolicyNo);
		FormLayout formLayout2 = new FormLayout( dateField);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		//searchFormLayout.setComponentAlignment(formLayout1, Alignment.MIDDLE_RIGHT);
		//searchFormLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
		
		
		//HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo,cpuCodeCmb),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("100%");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		editHospitalInfoSearchBtn = new Button();
		editHospitalInfoSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() editHospitalInfoSearchBtn.setImmediate(true);
		editHospitalInfoSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		editHospitalInfoSearchBtn.setWidth("-1px");
		editHospitalInfoSearchBtn.setHeight("-10px");
		editHospitalInfoSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(editHospitalInfoSearchBtn, "top:120.0px;left:230.0px;");
		//Vaadin8-setImmediate() editHospitalInfoSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:120.0px;left:339.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("680px");
		return verticalLayout; 
		


	}

	public void addListener() {

		editHospitalInfoSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				
				searchable.doSearch();
				editHospitalInfoSearchBtn.setEnabled(true);
				searchEditHospitalDetailsTable.tablesize();
				//fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, tableDto);      //to testing purpose by yosuva
				
				
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
		
		Iterator<Component> componentIterator = buildSearchEditHospitalInfoLayout.iterator();
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
												else if(indivdualComp instanceof DateField)
												{
													DateField field = (DateField) indivdualComp;
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
	if(null != searchable )
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

