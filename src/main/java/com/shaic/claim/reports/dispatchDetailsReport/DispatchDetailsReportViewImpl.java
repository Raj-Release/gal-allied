package com.shaic.claim.reports.dispatchDetailsReport;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class DispatchDetailsReportViewImpl extends AbstractMVPView implements DispatchDetailsReportView {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8773748642488511377L;

	@Inject
	private DispatchDetailsReportForm dispatchDetailsReportForm;
	
	@Inject
	private DispatchDetailsReportTable dispatchDetailsReportTable;
	
	private ExcelExport excelExport;
	
	private HorizontalLayout buttonHorLayout;
	
	private VerticalLayout mainPanel;
	
	private Button xmlReport;
	
	private Button cancelReport;
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		dispatchDetailsReportForm.init();
		
		xmlReport = new Button("Export To Excel");
		xmlReport.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		cancelReport = new Button("Cancel");
		cancelReport.addStyleName(ValoTheme.BUTTON_DANGER);
		buttonHorLayout=new HorizontalLayout(xmlReport,cancelReport);
		buttonHorLayout.setSpacing(true);
		
		dispatchDetailsReportTable.setUpdateType(ReferenceTable.D_BULK_UPLOAD_TYPE_AWB);
		dispatchDetailsReportTable.init("", false, false);
		
		mainPanel = new VerticalLayout(dispatchDetailsReportForm);
		setCompositionRoot(mainPanel);
		mainPanel.setHeight("100%");
		dispatchDetailsReportTable.addSearchListener(this);
		dispatchDetailsReportForm.addSearchListener(this);
		resetView();
		addReportListener();
		
	}

	private void addReportListener() {
		xmlReport.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(DispatchDetailsReportPresenter.GENERATE_DISPATCH_DETAILS_REPORT, null,null);			
			}
		});	
		
		cancelReport.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.SEARCH_DISPATCH_DETAILS_REPORT, null);		
			}
		});	
	}

	@Override
	public void doSearch() {

		DispatchDetailsReportFormDTO searchDTO = dispatchDetailsReportForm.getSearchDTO();	
		if(searchDTO != null && isvalid(searchDTO))
		{		
			Pageable pageable = dispatchDetailsReportTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(DispatchDetailsReportPresenter.DISPATCH_DETAILS_REPORT_SEARCH,searchDTO, userName, passWord);
		}
	

	}

	@Override
	public void resetSearchResultTableValues() {
		if(dispatchDetailsReportTable !=null){
			dispatchDetailsReportTable.getPageable().setPageNumber(1);
			dispatchDetailsReportTable.resetTable();
			dispatchDetailsReportTable.removeRow();
		}		
	}

	@Override
	public void resetView() {
		if(dispatchDetailsReportForm != null){
			dispatchDetailsReportForm.refresh();
		}
	}

	public void init() {	
		resetView();
	}

	@Override
	public void list(Page<DispatchDetailsReportTableDTO> tableRows) {

		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			dispatchDetailsReportTable.setTableList(tableRows);
			dispatchDetailsReportTable.tablesize();
			dispatchDetailsReportTable.setHasNextPage(tableRows.isHasNext());
			mainPanel.addComponent(buttonHorLayout);	
			mainPanel.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
			mainPanel.setHeight("665px");
		} else {
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.SEARCH_DISPATCH_DETAILS_REPORT, null);
				}
			});
		}
		dispatchDetailsReportForm.enableButtons();	
	}

	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(dispatchDetailsReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Marketing Escalation Report");
		excelExport.export();
		
	}
	
	@Override
	public void buildUpdateTypeLayout(Long updateType){
		if(mainPanel !=null && dispatchDetailsReportTable != null){
			mainPanel.removeComponent(dispatchDetailsReportTable);
			mainPanel.removeComponent(buttonHorLayout);
			dispatchDetailsReportTable.setUpdateType(updateType);
			dispatchDetailsReportTable.init("", false, false);
			mainPanel.addComponent(dispatchDetailsReportTable);
			mainPanel.setHeight("500px");
		}
	}
		
	@Override
	public void resetDispatchSearchFields(){
		dispatchDetailsReportForm.resetDispatchSearchFields();
		if(dispatchDetailsReportTable !=null){
			dispatchDetailsReportTable.removeRow();		
		}
		if(mainPanel !=null && dispatchDetailsReportTable != null){
			mainPanel.removeComponent(dispatchDetailsReportTable);
			mainPanel.removeComponent(buttonHorLayout);
		}
	}
	
	public boolean isvalid(DispatchDetailsReportFormDTO searchDTO) {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();	

		

		if((searchDTO.getIntimationNo() == null || ("").equalsIgnoreCase(searchDTO.getIntimationNo()))
				&& (searchDTO.getRodNumber() == null || ("").equalsIgnoreCase(searchDTO.getRodNumber()))
				&& (searchDTO.getAwsNumber() == null || ("").equalsIgnoreCase(searchDTO.getAwsNumber()))
				&& (searchDTO.getBatchNumber() == null || ("").equalsIgnoreCase(searchDTO.getBatchNumber()))
				&& searchDTO.getToDate() == null &&  searchDTO.getFromDate() == null){
			if(searchDTO.getUpdateType() != null){
				hasError = true;
				eMsg.append("Intimation no/ROD no/Batch no/ AWB no/From Date and To date any one of the field is Mandatory.</br>");
			}else{
				hasError = true;
				eMsg.append("Intimation no/ROD no/Batch no/ AWB no/From Date and To date any one of the field is Mandatory along with Type field.</br>");	
			}		
		}else if(searchDTO.getUpdateType() == null || ("").equalsIgnoreCase(searchDTO.getUpdateType().getValue())){
			hasError = true;
			eMsg.append("Please select Type field to generate the report.</br>");
		}
		
		if(searchDTO.getToDate() !=null && searchDTO.getFromDate() ==null){	
			hasError = true;
			eMsg.append("Please select both From date and To date for search.<br>");
		}else if(searchDTO.getToDate() ==null && searchDTO.getFromDate() !=null){
			hasError = true;
			eMsg.append("Please select both From date and To date for search.<br>");
		}
		
		if(searchDTO.getToDate() !=null && searchDTO.getFromDate() !=null){	
			Long days = SHAUtils.getNoOfDaysBetweenDate(searchDTO.getFromDate(), searchDTO.getToDate());
			if(days < 0){
				hasError = true;
				eMsg.append("Invalid Date Range, Please select To date greater than From date.<br>");
			}else {
				days +=1;
			}
			if(!(days < 0) && days > 7){
				hasError = true;
				eMsg.append("From & To date difference should be 7 days.<br>");
			}	
		}

		if (hasError) {
			MessageBox.createError()
			.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
			.withOkButton(ButtonOption.caption("OK")).open();
			hasError = true;
			return !hasError;
		} 
		return !hasError;
	}

}
