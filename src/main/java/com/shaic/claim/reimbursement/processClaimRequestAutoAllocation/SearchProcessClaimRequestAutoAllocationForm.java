package com.shaic.claim.reimbursement.processClaimRequestAutoAllocation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.PreauthService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;
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

public class SearchProcessClaimRequestAutoAllocationForm extends ViewComponent{

	private static final long serialVersionUID = -3616823920991467671L;

	private BeanFieldGroup<SearchProcessClaimRequestTableDTO> binder;

	@EJB
	private PreauthService preauthService;

	Button claimRequestSearchBtn;

	Button resetBtn;

	VerticalLayout buildProcessClaimRequestSearchLayuout;

	private Searchable searchable;	
	

	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}

	public SearchProcessClaimRequestTableDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimRequestTableDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}


	@PostConstruct
	public void init() {
		initBinder();
		buildProcessClaimRequestSearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Process Claim Request - Auto Allocation");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildProcessClaimRequestSearchLayuout.addComponent(preauthPremedicalPanel);
		buildProcessClaimRequestSearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildProcessClaimRequestSearchLayuout.setMargin(false);
		setCompositionRoot(buildProcessClaimRequestSearchLayuout);
		addListener();
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimRequestTableDTO>(SearchProcessClaimRequestTableDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimRequestTableDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}


	private VerticalLayout buildPreauthSearchLayout() 
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(true);		 	

		claimRequestSearchBtn = new Button();
		claimRequestSearchBtn.setCaption("Get Next");
		claimRequestSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		claimRequestSearchBtn.setWidth("-1px");
		claimRequestSearchBtn.setHeight("-10px");
		claimRequestSearchBtn.setDisableOnClick(true);

		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		resetBtn.setVisible(false);

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(claimRequestSearchBtn);
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

		claimRequestSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				claimRequestSearchBtn.setEnabled(true);
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
