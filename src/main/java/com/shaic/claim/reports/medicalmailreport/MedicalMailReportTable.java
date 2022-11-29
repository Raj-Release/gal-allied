package com.shaic.claim.reports.medicalmailreport;

import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.domain.ClaimService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalMailReportTable extends GBaseTable<MedicalMailReportTableDTO>{
	
	@Inject
	protected ViewDetails viewDetails;
	
	@Inject
	private ClaimService claimservice;
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","cpuCode","officeCode","reasonForQuery","queryDateValue","emailStatus","mailDateValue",
		"status"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<MedicalMailReportTableDTO>(MedicalMailReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("print",100);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("490px");
		generateColumn();
	}
	@Override
	public void tableSelectHandler(MedicalMailReportTableDTO t) {
		fireViewEvent(MenuPresenter.MEDICAL_MAIL_REPORT, t);
	}
	
	
	private void generateColumn()
	{
	table.removeGeneratedColumn("Print");
	table.addGeneratedColumn("Print", new Table.ColumnGenerator() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
				
				
			
			Button button = new Button("Print");
			button.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					String intimationNo =  ((MedicalMailReportTableDTO)itemId).getIntimationNo();
					
					if(null != intimationNo )
					{
					viewDetails.viewUploadedQueryDocumentDetails(intimationNo,SHAConstants.QUERY);
					}
				}

			});
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("150px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
			return button;
		}
	});
}

	
	
	@Override
	public String textBundlePrefixString() {
		return "medicalmailreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	

	

}
