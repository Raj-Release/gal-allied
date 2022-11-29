package com.shaic.claim.reimbursement.commonBillingFA;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.PreauthService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchProcessClaimCommonBillingAndFinancialsAutoAllocationForm extends ViewComponent{



	private static final long serialVersionUID = -3616823920991467671L;


	private BeanFieldGroup<SearchProcessClaimFinancialsTableDTO> binder;

	@EJB
	private PreauthService preauthService;

	Button BillingFASearchBtn;

	Button resetBtn;

	VerticalLayout buildcommonBillingFASearchLayuout;

	private Searchable searchable;	
	

	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}

	public SearchProcessClaimFinancialsTableDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimFinancialsTableDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}


	@PostConstruct
	public void init() {
		initBinder();
		buildcommonBillingFASearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Common for Billing & FA - Auto Allocation");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildcommonBillingFASearchLayuout.addComponent(preauthPremedicalPanel);
		buildcommonBillingFASearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildcommonBillingFASearchLayuout.setMargin(false);
		setCompositionRoot(buildcommonBillingFASearchLayuout);
		addListener();
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimFinancialsTableDTO>(SearchProcessClaimFinancialsTableDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimFinancialsTableDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}


	private VerticalLayout buildPreauthSearchLayout() 
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(true);		 	

		BillingFASearchBtn = new Button();
		BillingFASearchBtn.setCaption("Get Next");
		BillingFASearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		BillingFASearchBtn.setWidth("-1px");
		BillingFASearchBtn.setHeight("-10px");
		BillingFASearchBtn.setDisableOnClick(true);

		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		resetBtn.setVisible(false);

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(BillingFASearchBtn);
		hLayout.setSpacing(true);
		FormLayout fLayout = new FormLayout();
		fLayout.setMargin(false);
		fLayout.addComponents(hLayout);
		verticalLayout.addComponent(fLayout);
		verticalLayout.setHeight("50px");
		verticalLayout.addStyleName("g-search-panel");
		verticalLayout.setComponentAlignment(fLayout, Alignment.TOP_LEFT);
		return verticalLayout; 

	}

	public void addListener() {

		BillingFASearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				BillingFASearchBtn.setEnabled(true);
				searchable.doSearch();
			}
		});	
	}

	/**
	 * Method to reset all form values 
	 *
	 * */

//	public void resetAlltheValues() 
//	{
//
//		Iterator<Component> componentIterator = buildPreauthSearchLayuout.iterator();
//		while(componentIterator.hasNext()) 
//		{
//			Component searchScrnComponent = componentIterator.next() ;
//			if(searchScrnComponent instanceof  Panel )
//			{	
//				System.out.println("---inside the if block---");
//				Panel panel = (Panel)searchScrnComponent;
//				Iterator<Component> searchScrnCompIter = panel.iterator();
//				while (searchScrnCompIter.hasNext())
//				{
//					Component verticalLayoutComp = searchScrnCompIter.next();
//					VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
//					Iterator<Component> vLayoutIter = vLayout.iterator();
//					while(vLayoutIter.hasNext())
//					{
//						Component absoluteComponent = vLayoutIter.next();
//						HorizontalLayout absLayout = (HorizontalLayout)absoluteComponent;
//						Iterator<Component> absLayoutIter = absLayout.iterator();
//						while(absLayoutIter.hasNext())
//						{
//							Component horizontalComp = absLayoutIter.next();
//							if(horizontalComp instanceof HorizontalLayout)
//							{
//								HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
//								Iterator<Component> formLayComp = hLayout.iterator();
//								while(formLayComp.hasNext())
//								{
//									Component formComp = formLayComp.next();
//									HorizontalLayout fLayout = (HorizontalLayout)formComp;
//									Iterator<Component> formComIter = fLayout.iterator();
//
//									while(formComIter.hasNext())
//									{
//										Component indivdualComp = formComIter.next();
//										if(indivdualComp != null) 
//										{
//											if(indivdualComp instanceof Label) 
//											{
//												continue;
//											}	
//											if(indivdualComp instanceof TextField) 
//											{
//												TextField field = (TextField) indivdualComp;
//												field.setValue("");
//											} 
//											else if(indivdualComp instanceof ComboBox)
//											{
//												ComboBox field = (ComboBox) indivdualComp;
//												field.setValue(null);
//											}	 
//										}
//									}
//								}
//							}
//						}
//					}
//				}//Method to reset search table.
//				removeTableFromLayout();
//			}
//		}
//	}

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
		removeTableFromLayout();
	}



}
