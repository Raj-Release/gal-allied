package com.shaic.claim.reports.shadowProvision;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ClaimService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchShowdowDownloadTable extends GBaseTable<SearchShadowProvisionDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","successCount","errorCount"}; 
	
	@EJB
	private ClaimService claimService;
	
	/*@Inject
	private ErrorLogTable errorLogTable;*/
	
	//private ExcelExport excelExport;
	
	//private VerticalLayout mainVertical;
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchShadowProvisionDTO>(SearchShadowProvisionDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
//		table.setColumnWidth("hospitalAddress", 350);
		table.setHeight("260px");
		
		
		table.removeGeneratedColumn("errorLog");
		table.addGeneratedColumn("errorLog", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("Download Error Log");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						
						SearchShadowProvisionDTO tableDTO = (SearchShadowProvisionDTO) itemId;
						//List<SearchShadowProvisionDTO> errorLogDetailsForShadow = claimService.getErrorLogDetailsForShadow(tableDTO.getBatchNumber());
						
						fireViewEvent(SearchShadowProvisionPresenter.EXPORT_EXCEL, tableDTO);
						
//						errorLogTable.init("", false, false);
//						
//						mainVertical = new VerticalLayout(errorLogTable);
//						errorLogTable.setVisible(false);
//						
//						errorLogTable.setTableList(errorLogDetailsForShadow);
//						
//						excelExport = new  ExcelExport(errorLogTable.getTable());
//						excelExport.excludeCollapsedColumns();
//						excelExport.setDisplayTotals(false);
////						excelExport.setReportTitle("Revision of Provision");
//						excelExport.export();
//						
//						setCompositionRoot(mainVertical);
						
					} 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		
		table.setColumnHeader("errorLog", " ");
		
	}
	
	public void setMainVerticalLayout(VerticalLayout vertical){
		//mainVertical = vertical;
	}


	
	@Override
	public void tableSelectHandler(
			SearchShadowProvisionDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-shadow-processing-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
  public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

}
