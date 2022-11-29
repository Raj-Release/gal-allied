
package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchRRCStatusViewImpl extends AbstractMVPView implements SearchRRCStatusView {
	
	@Inject
	private SearchRRCStatusForm  searchForm;
	
	@Inject
	private SearchRRCStatusTable searchResultTable;
//	private RRCRequestTableForExcelReport searchResultTable;
	
	@Inject
	private RRCStatusTableForExcelReport tableForExcel;
	
	@Inject
	private DBCalculationService dbCalculationService;
	
	private VerticalSplitPanel mainPanel;
	
	private Button btnGenerateExcel;
	
	private CheckBox chkBox;
	
	Page<SearchRRCRequestTableDTO> tableRowsPage = new Page<SearchRRCRequestTableDTO>();
	
	private List<SearchRRCRequestTableDTO> finalDataList = null;
	
	
	//private static final long serialVersionUID = -1495833013472828014L;
	private ExcelExport excelExport;
	VerticalLayout secondLayout = null;//new VerticalLayout();
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, true);
		tableForExcel.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
	//	mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSecondComponent(buildSecondComponent());
		
		mainPanel.setSplitPosition(46);
		setHeight("620px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		addListener();
		resetView();
		finalDataList = new ArrayList<SearchRRCRequestTableDTO>();
	}
	
	private VerticalLayout buildSecondComponent()
	{
		
		btnGenerateExcel = new Button();
		btnGenerateExcel.setCaption("Generate excel report");
		//Vaadin8-setImmediate() btnGenerateExcel.setImmediate(true);
		btnGenerateExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnGenerateExcel.setWidth("-1px");
		btnGenerateExcel.setHeight("-10px");
		//btnGenerateExcel.setDisableOnClick(true);
		//btnGenerateExcel.setEnabled(false);
		chkBox = new CheckBox();
		
		FormLayout formLayout = new FormLayout(chkBox);
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		
		
		formLayout.setSpacing(false);
		formLayout.setMargin(false);
		formLayout1.setSpacing(false);
		formLayout1.setMargin(false);
		
		
		secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(formLayout,formLayout1);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(searchResultTable);
		//secondLayout.addComponent(tableForExcel);
		
		return secondLayout;
		
	}
	
	private void addListener()
	{
		
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					if(value)
					{

						chkBox.setValue(value);
					}
					chkBox.setValue(value);

			/*			SearchRRCRequestFormDTO searchFormDTO = searchForm.getSearchDTO();
						String intimationNo = null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() :null;
						String rrcRequestNo = null != searchFormDTO.getRrcRequestNo() ? searchFormDTO.getRrcRequestNo() : null;
						Long cpcode = null != searchFormDTO && null != searchFormDTO.getCpu() ? searchFormDTO.getCpu().getId() : null;
						Long rrcRequestTypeId = null != searchFormDTO && null != searchFormDTO.getRrcRequestType() ? searchFormDTO.getRrcRequestType().getId() : null;
						Long rrcEligbilityTypeId = null != searchFormDTO &&  null != searchFormDTO.getEligibilityType() ? searchFormDTO.getEligibilityType().getId() : null;
						Date fromDate = null != searchFormDTO && null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
						Date toDate = null != searchFormDTO && null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;						
								
					    java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime()); 
					    java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
					    
						List<SearchRRCRequestTableDTO> totalList = dbCalculationService.getRRCCompleteList(intimationNo, rrcRequestNo,
								cpcode, rrcRequestTypeId, rrcEligbilityTypeId, sqlFromDate, sqlToDate);
					6
						searchResultTable.setValueForCheckBox(value,totalList);*/							

					//searchResultTable.setValueForCheckBox(value,finalDataList);
					searchResultTable.setValueForCheckBox(value,searchResultTable.getTableAllItems());
				}
			}
		});
		
		
		btnGenerateExcel.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		
				
		
				
				@Override
				public void buttonClick(ClickEvent event) {
					Boolean chkBoxValue = false;
					List<SearchRRCStatusTableDTO> requestTableList = new ArrayList<SearchRRCStatusTableDTO>();
					if(chkBoxValue || (null != chkBox && chkBox.getValue()))
					{						
						
						SearchRRCStatusFormDTO searchFormDTO = searchForm.getSearchDTO();
						String intimationNo = null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() :null;
						String rrcRequestNo = null != searchFormDTO.getRrcRequestNo() ? searchFormDTO.getRrcRequestNo() : null;
						String cpcode = null != searchFormDTO && null != searchFormDTO.getCpu() ? searchFormDTO.getCpu().getValue() : null;
						String rrcRequestTypeId = null != searchFormDTO && null != searchFormDTO.getRrcRequestType() ? searchFormDTO.getRrcRequestType().getId().toString() : null;
						String rrcEligbilityTypeId = null != searchFormDTO &&  null != searchFormDTO.getEligibilityType() ? searchFormDTO.getEligibilityType().getId().toString() : null;
						Date fromDate = null != searchFormDTO && null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
						Date toDate = null != searchFormDTO && null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;						
								
						java.sql.Date sqlFromDate = null;
						if(fromDate != null){
					    sqlFromDate = new java.sql.Date(fromDate.getTime()); 
						}
						
						java.sql.Date sqlToDate = null;
						
						if(toDate != null){
						sqlToDate = new java.sql.Date(toDate.getTime());
						}
					   
						if(null != cpcode)
						{
							String[] cpuCode = cpcode.split("-");
							cpcode = cpuCode[0];
							
						}
					    
						String userName=((String)getUI().getSession().getAttribute(BPMClientContext.USERID)).toUpperCase();
						
					    requestTableList = dbCalculationService.getRRCStatusList(intimationNo, rrcRequestNo,
								cpcode, rrcRequestTypeId, rrcEligbilityTypeId, sqlFromDate, sqlToDate, userName);
					    searchResultTable.setValueForCheckBox(chkBox.getValue(),requestTableList);
						//requestTableList = searchResultTable.getTableAllItems();
					}
					else
					{
					 requestTableList = searchResultTable.getTableAllItems();
					}
					if(null != requestTableList && !requestTableList.isEmpty())
					{
					for (SearchRRCStatusTableDTO searchRRCStatusTableDTO2 : requestTableList) {
						
						if(("true").equalsIgnoreCase(searchRRCStatusTableDTO2.getCheckBoxStatus()))
							chkBoxValue = true;
					}
					
					//if(null != chkBox && chkBox.getValue())
					if(chkBoxValue || (null != chkBox && chkBox.getValue()))
					{
						getTableDataForReport(requestTableList);
						//tableForExcel.setTableValues(requestTableList);
						secondLayout.addComponent(tableForExcel);
						tableForExcel.setVisible(false);
						excelExport = new  ExcelExport(tableForExcel.getTable());
						excelExport.excludeCollapsedColumns();
						excelExport.setDisplayTotals(false);
						excelExport.setReportTitle("RRC Status History");
						excelExport.export();
					}
					else
					{
						Label label = new Label("Please select a record for report generation", ContentMode.HTML);
						label.setStyleName("errMessage");
						VerticalLayout layout = new VerticalLayout();
						layout.setMargin(true);
						layout.addComponent(label);
						ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(true);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
					}
					else
					{
						Label label = new Label("Please select a record for report generation", ContentMode.HTML);
						label.setStyleName("errMessage");
						VerticalLayout layout = new VerticalLayout();
						layout.setMargin(true);
						layout.addComponent(label);
						ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(true);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
					}
			
		});
	}
	
	private void getTableDataForReport(List<SearchRRCStatusTableDTO> finalList) {
		if (null != tableForExcel) {
			tableForExcel.removeRow();

			List<SearchRRCStatusTableDTO> requestTableList = finalList;
			if (null != requestTableList && !requestTableList.isEmpty()) {
				/*
				 * List<SearchRRCRequestTableDTO> finalList = new
				 * ArrayList<SearchRRCRequestTableDTO>(); for
				 * (SearchRRCRequestTableDTO searchRRCRequestTableDTO :
				 * requestTableList) {
				 * if(("true").equalsIgnoreCase(searchRRCRequestTableDTO
				 * .getCheckBoxStatus())) {
				 * //tableForExcel.addBeanToList(requestTableList);
				 * 
				 * finalList.add(searchRRCRequestTableDTO);
				 * 
				 * }
				 */
				// tableForExcel.setTableValues(requestTableList);
				
				
				tableForExcel.addBeanToList(requestTableList);
			}

		}
	}
	
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		SearchRRCStatusFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchRRCStatusPresenter.SEARCH_BUTTON_CLICK_RRC_STATUS, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		chkBox.setValue(false);
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			
			if(comp instanceof VerticalLayout)
			{
				Iterator<Component> subCompIter = ((VerticalLayout) comp).getComponentIterator();
				while(subCompIter.hasNext())
				{
					Component tableComp = (Component)subCompIter.next();
					if(tableComp instanceof SearchRRCRequestTable)
					{
						((SearchRRCRequestTable) tableComp).removeRow();
					}
				}
			}

		}
	
		
	}

	@Override
	public void list(Page<SearchRRCStatusTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows,"");		
			//searchResultTable.setTableValues(tableRows.getPageItems());
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());		
			
			finalDataList = tableRows.getTotalList();			
			if(null != chkBox && chkBox.getValue())
			{
		//	if(null != finalDataList && !finalDataList.isEmpty())
			//{				
				searchResultTable.setValueForSelectAllCheckBox(chkBox.getValue());
					
			//}
			}
			
			
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("RRC Status Screen Home");
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
					fireViewEvent(MenuItemBean.RRC_STATUS_SCREEN, null);
				}
			});
		}
		searchForm.enableButtons();
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType,BeanItemContainer<SelectValue> rrcEligiblity) {
		//searchForm.setCBXValue(cpu,rrcRequestType);
		searchForm.setDropDownValues(cpu, rrcRequestType,rrcEligiblity);
		
	}

	@Override
	public void showSearchRRCStatusView(RRCDTO rrcDTO) {
		// TODO Auto-generated method stub
		
	}
	

	

}

