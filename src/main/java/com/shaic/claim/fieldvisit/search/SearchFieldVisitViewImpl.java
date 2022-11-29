package com.shaic.claim.fieldvisit.search;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.processfieldvisit.search.FieldVisitTableForExcel;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class SearchFieldVisitViewImpl extends AbstractMVPView implements SearchFieldVisitView, Searchable  {
	
	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SearchFieldVisitForm searchForm;
	
	@Inject
	private SearchFieldVisitTable searchResultTable;	
	
	@Inject 
	private FieldVisitTableForExcel excelTable;
	
	private Button exportToExcel;
	
	private ExcelExport excelExport;
	
	private VerticalLayout verticalLayout;
	
//	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	private VerticalSplitPanel mainPanel;
	@PostConstruct
	protected void initView() {
		/*addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false);
		searchResultTable.setHeight("100.0%");
		searchResultTable.setWidth("100.0%");
		
		searchForm.setHeight(200, Unit.PIXELS);
		
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		
		mainPanel.setSplitPosition(40);
		mainPanel.setHeight("100.0%");
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);
		
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);*/
		
		addStyleName("view");
		//setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		//searchResultTable.setHeight("100.0%");
		//searchResultTable.setWidth("100.0%");
//		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));
		mainPanel = new VerticalSplitPanel();
		//searchForm.setHeight(600, Unit.PIXELS);

		mainPanel.setFirstComponent(searchForm);

		excelTable.init("", false, false);
		
		exportToExcel = new Button("Export to Excel");

		//Vaadin8-setImmediate() exportToExcel.setImmediate(true);
		exportToExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		exportToExcel.setWidth("-1px");
		exportToExcel.setHeight("-10px");
		
		verticalLayout = new VerticalLayout(exportToExcel,searchResultTable);
		verticalLayout.setComponentAlignment(exportToExcel, Alignment.TOP_LEFT);
		verticalLayout.setSpacing(true);
		
		mainPanel.setSecondComponent(verticalLayout);
		mainPanel.setSplitPosition(40);

		//mainPanel.setWidth("100.0%");
		//setHeight("100.0%");
		setHeight("570px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
		
		addExportListener();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		
		searchForm.refresh(); 
		/*if(searchForm.get() != null) {
			searchForm.get().init();
		}*/
	}

	@Override
	public void list(Page<SearchFieldVisitTableDTO> tableRows) {
		//searchResultTable.setTableList(tableRows.getPageItems());
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			//searchResultTable.setPage(tableRows);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Assign Feild Visit Representative Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.VISIT_FIELD, null);
					
				}
			});
		}
	
	}

	/*@Override
	public void setView(Class<? extends MVPView> viewClass,
			boolean selectInNavigationTree, ParameterDTO parameter) {
		MVPView view = views.select(viewClass).get();
		mainPanel.removeAllComponents();
		mainPanel.addComponent((Component) view);
	}*/
	
	@Override
	public void doSearch() {
		SearchFieldVisitFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		fireViewEvent(SearchFieldVisitPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
	}
	
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchFieldVisitTable)
			{
				((SearchFieldVisitTable) comp).removeRow();
			}
		}
	}
	
	private void getTableDataForReport()
	{
		if(null != excelTable)
		{
			excelTable.removeRow();
			
			List<SearchFieldVisitTableDTO> requestTableList = searchResultTable.getValues();
			
			
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				//for (SearchRRCRequestTableDTO searchRRCRequestTableDTO : requestTableList) {
				//	if(("true").equalsIgnoreCase(searchRRCRequestTableDTO.getCheckBoxStatus()))
				//	{
				
				for (SearchFieldVisitTableDTO searchFieldVisitTableDTO : requestTableList) {
					excelTable.addBeanToList(searchFieldVisitTableDTO);
				}

					//}
				}
			}	
		}
	
	public void addExportListener(){
		
		exportToExcel.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					getTableDataForReport();
					verticalLayout.addComponent(excelTable);
					excelTable.setVisible(false);
					excelExport = new  ExcelExport(excelTable.getTable());
					excelExport.excludeCollapsedColumns();
					excelExport.setDisplayTotals(false);
					excelExport.setReportTitle("FVR Assignment Report");
					excelExport.export();
					
				}
		});
		
	}
}
