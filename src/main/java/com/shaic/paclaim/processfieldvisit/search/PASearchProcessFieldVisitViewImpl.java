/**
 * 
 */
package com.shaic.paclaim.processfieldvisit.search;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.processfieldvisit.search.FieldVisitTableForExcel;
import com.shaic.reimbursement.processfieldvisit.search.SearchProcessFieldVisitFormDTO;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.util.BeanItemContainer;
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

/**
 * @author ntv.narenj
 *
 */
public class PASearchProcessFieldVisitViewImpl extends AbstractMVPView implements PASearchProcessFieldVisitView{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3790868203047397682L;

	@Inject
	private PASearchProcessFieldVisitForm  searchForm;
	
	@Inject
	private PASearchProcessFieldVisitTable searchResultTable;
	
	@Inject 
	private FieldVisitTableForExcel excelTable;
	
	private Button exportToExcel;
	
	private ExcelExport excelExport;
	
	private VerticalLayout verticalLayout;
	
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
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
		mainPanel.setSplitPosition(45);
		setHeight("570px");
	//	mainPanel.setHeight("560px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		addExportListener();
		resetView();
	}
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		SearchProcessFieldVisitFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(PASearchProcessFieldVisitPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof PASearchProcessFieldVisitTable)
			{
				((PASearchProcessFieldVisitTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void list(Page<SearchFieldVisitTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Process Field Visit");
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
					fireViewEvent(MenuItemBean.PA_SHOW_PROCESS_FIELD_VISIT, null);
					
				}
			});
		}
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpuCode) {
		searchForm.setCPUCode(cpuCode);
		
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
