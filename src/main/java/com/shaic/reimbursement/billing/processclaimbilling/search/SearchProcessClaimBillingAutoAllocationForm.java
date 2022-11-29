package com.shaic.reimbursement.billing.processclaimbilling.search;

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

public class SearchProcessClaimBillingAutoAllocationForm extends ViewComponent{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2831604867786057575L;

	private BeanFieldGroup<SearchProcessClaimBillingFormDTO> binder;

	private Button billingGetNextBtn;

	private Button resetBtn;

	private VerticalLayout buildcommonBillingSearchLayuout;

	private Searchable searchable;	
	
	@EJB
	private PreauthService preauthService;
	
	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}

	public SearchProcessClaimBillingFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimBillingFormDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}


	@PostConstruct
	public void init() {
		initBinder();
		buildcommonBillingSearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Process Claim Billing - Auto Allocation");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildcommonBillingSearchLayuout.addComponent(preauthPremedicalPanel);
		buildcommonBillingSearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildcommonBillingSearchLayuout.setMargin(false);
		setCompositionRoot(buildcommonBillingSearchLayuout);
		addListener();
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimBillingFormDTO>(SearchProcessClaimBillingFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimBillingFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}


	private VerticalLayout buildPreauthSearchLayout() 
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(true);		 	

		billingGetNextBtn = new Button();
		billingGetNextBtn.setCaption("Get Next");
		billingGetNextBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		billingGetNextBtn.setWidth("-1px");
		billingGetNextBtn.setHeight("-10px");
		billingGetNextBtn.setDisableOnClick(true);

		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		resetBtn.setVisible(false);

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(billingGetNextBtn);
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

		billingGetNextBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				billingGetNextBtn.setEnabled(true);
				searchable.doSearch();
			}
		});	
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
		removeTableFromLayout();
	}



}
