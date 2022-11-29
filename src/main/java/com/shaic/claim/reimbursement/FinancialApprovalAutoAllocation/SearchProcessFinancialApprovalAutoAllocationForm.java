package com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation;

import javax.annotation.PostConstruct;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchProcessFinancialApprovalAutoAllocationForm  extends ViewComponent{



	private static final long serialVersionUID = -3616823920991467671L;


	private BeanFieldGroup<SearchProcessClaimFinancialsTableDTO> binder;

	Button FAAutoSearchBtn;

	Button resetBtn;

	VerticalLayout buildFAAutoSearchLayuout;

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
		buildFAAutoSearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Process Claim Financials (Auto Allocation)");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildFAAutoSearchLayuout.addComponent(preauthPremedicalPanel);
		buildFAAutoSearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildFAAutoSearchLayuout.setMargin(false);
		setCompositionRoot(buildFAAutoSearchLayuout);
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

		FAAutoSearchBtn = new Button();
		FAAutoSearchBtn.setCaption("Get Next");
		FAAutoSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		FAAutoSearchBtn.setWidth("-1px");
		FAAutoSearchBtn.setHeight("-10px");
		FAAutoSearchBtn.setDisableOnClick(true);

		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		resetBtn.setVisible(false);

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(FAAutoSearchBtn);
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

		FAAutoSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				FAAutoSearchBtn.setEnabled(true);
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
