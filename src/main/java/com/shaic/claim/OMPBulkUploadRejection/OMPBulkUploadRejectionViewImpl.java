package com.shaic.claim.OMPBulkUploadRejection;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUWisePreauthResultDto;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionTable;
import com.shaic.claim.reports.shadowProvision.SearchShowdowView;
import com.shaic.claim.reports.shadowProvision.ShadowProvisionForm;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPBulkUploadRejectionViewImpl  extends AbstractMVPView implements OMPBulkUploadRejectionView,Searchable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private BulkUploadRejectionForm bulkUploadRejectionForm;

	@Inject
	private BulkUploadRejectionTable bulkUploadRejectionTableObj;
	
	private ExcelExport excelExport;
	
	private Button btnExportToExcel;
	
	
	@PostConstruct
	@Override
	public void initView(BeanItemContainer<SelectValue> statusContainer) {
		
		bulkUploadRejectionForm.init(statusContainer);
		
		VerticalLayout mainVertical = new VerticalLayout(bulkUploadRejectionForm);
		
//		addListner();
		
		setCompositionRoot(mainVertical);
		
	}
	
	@Override
	public void init() {
		
//		bulkUploadRejectionForm.init();
//		Panel mainPanel = new Panel(bulkUploadRejectionForm);
//		
//		btnExportToExcel = bulkUploadRejectionForm.getExportButtonToExcel();
//		
//		addListner();
//		
//		setCompositionRoot(mainPanel);


	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
// Check and remove
/*	public void addListner(){
		btnExportToExcel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				fireViewEvent(StarFaxSimulatePresenter.SUBMIT_CONVERT_CLAIM, txtCLoseCLaim.getValue().toString());
				DBCalculationService dbcalculationService = new DBCalculationService();
				List<SearchShadowProvisionDTO> downloadForProvisonExcel = dbcalculationService.getDownloadForProvisonExcel(new Date());
				
				bulkUploadRejectionTableObj.init("", false, false);
				for (SearchShadowProvisionDTO searchShadowProvisionDTO : downloadForProvisonExcel) {
					bulkUploadRejectionTableObj.addBeanToList(searchShadowProvisionDTO);
				}			
				excelExport = new  ExcelExport(bulkUploadRejectionTableObj.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setDisplayTotals(false);
				excelExport.setReportTitle("Revision of Provision");
				excelExport.export();
			
//				fireViewEvent(StarFaxSimulatePresenter.AUTO_CLOSE_CLAIM_PROCESS, txtCLoseCLaim.getValue().toString());

			}
		});
	}*/

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportToExcelList(
			List<SearchShadowProvisionDTO> errorLogDetailsForShadow) {
		bulkUploadRejectionForm.exportToErrorLog(errorLogDetailsForShadow);
		
	}

	@Override
	public void showBulkUploadRejectionDetails(
			List<OMPBulkUploadRejectionResultDto> bulkUploadRejectionList) {
		bulkUploadRejectionForm.showTableResult(bulkUploadRejectionList);
		
	}

}
