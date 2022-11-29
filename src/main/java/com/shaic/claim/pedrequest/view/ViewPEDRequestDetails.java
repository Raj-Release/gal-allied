package com.shaic.claim.pedrequest.view;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresmentDetailsTable;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndorsementDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ViewPEDRequestDetails extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PEDQueryService pedService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable;
	
	@Inject
	private ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService;

	private Object[] columns = new Object[] { "PEDSuggestion", "NameofPED",
			"RepudiationLetterDate", "Remarks", "RequestorID", "RequestedDate", "RequestStatus","action"};
	
	private VerticalLayout mainLayout;
	
	public ViewPEDRequestDetails() {
		setCaption("PED Request Details");
		buildMainLayout();
		setContent(mainLayout);

	}
	
	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setCaption("Query Remarks(Approver)");
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		setWidth("850");
		setHeight("700");
		IndexedContainer iContainer = createContainer();
		Table createViewTable = createTable(iContainer, "");
		mainLayout.addComponent(createViewTable);

		return mainLayout;
	}
	
	
	private void columnMapper(Table table, IndexedContainer container) {

		table.setContainerDataSource(container);
		table.setVisibleColumns(columns);
		table.setPageLength(table.size() + 1);
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();
		
		container.addContainerProperty("key", Long.class, null);
		container.addContainerProperty("PEDSuggestion", String.class, null);
		container.addContainerProperty("NameofPED", String.class, null);
		container.addContainerProperty("RepudiationLetterDate", String.class, null);
		container.addContainerProperty("Remarks", String.class, null);
		container.addContainerProperty("RequestorID", String.class, null);
		container.addContainerProperty("RequestedDate", String.class, null);
		container.addContainerProperty("RequestStatus", String.class, null);
		container.addContainerProperty("action", Button.class, null);

		return container;
	}

	public Table createTable(IndexedContainer container, String tableHeader) {

		final Table table = new Table(tableHeader);
		columnMapper(table, container);
		table.setWidth("100%");
		table.setHeight("100%");
		table.setEditable(false);
		return table;
	}

	public void addItem(final Table table, final IndexedContainer container) {
		Object itemId = container.addItem();
		container.getItem(itemId).getItemProperty("key");
		container.getItem(itemId).getItemProperty("PEDSuggestion");
		container.getItem(itemId).getItemProperty("NameofPED");
		container.getItem(itemId).getItemProperty("RepudiationLetterDate");
		container.getItem(itemId).getItemProperty("Remarks");
		container.getItem(itemId).getItemProperty("RequestorID");
		container.getItem(itemId).getItemProperty("RequestedDate");
		container.getItem(itemId).getItemProperty("RequestStatus");
		container.getItem(itemId).getItemProperty("action");
		Button viewClaimBtn = getViewClaimStatusButton(container, itemId);
		viewClaimBtn.addStyleName("link");
		
	}
	
	private Button getViewClaimStatusButton(final IndexedContainer container,
			final Object itemId) {
		Button viewClaimBtn = new Button("View Claim Status");
		viewClaimBtn.setData(itemId);
		viewClaimBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				final Object itemId = event.getButton().getData();
				Item item = container.getItem(itemId);
				
				Long key = (Long)container.getItem(itemId).getItemProperty("key").getValue();

				ViewPEDEndorsementDetails viewDetailUI = new ViewPEDEndorsementDetails(
	    				pedService, masterService, preauthService,
	    			    viewPEDEndoresmentDetailsTable,
	     				viewPEDEndoresementDetailsService, key);
				String strStatus = String.valueOf(container.getItem(itemId).getItemProperty("RequestStatus"));
	    		viewDetailUI.initView(key,strStatus);
	    		UI.getCurrent().addWindow(viewDetailUI);				
				
//				ViewPEDEndorsementDetails viewPEDEndorsementDetails = new ViewPEDEndorsementDetails();
//				UI.getCurrent().addWindow(viewPEDEndorsementDetails);
			}
		});
		return viewClaimBtn;
	}
	
}
