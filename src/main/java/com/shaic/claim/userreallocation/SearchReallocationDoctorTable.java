package com.shaic.claim.userreallocation;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SearchReallocationDoctorTable extends GBaseTable<SearchReallocationDoctorDetailsTableDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Object[] NATURAL_COL_ORDER_SEARCH = new Object[]{"serialNumber","empId","doctorName"}; 
	
	private Window popup;
	
	private String  presenterString;
	
	/*@Inject
	private CreateRODDocumentDetailsPage createRODDocumentDetailsPage;*/
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchReallocationDoctorDetailsTableDTO>(SearchReallocationDoctorDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER_SEARCH);
//		table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);
		table.setWidth("100%");
		table.setColumnWidth("empId", 140);
		table.setColumnWidth("doctorName",280);
		
		setSizeFull();
		table.removeGeneratedColumn("selectDetails");
		
		table.addGeneratedColumn("selectDetails", 
				new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnSelect = new Button();
				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
				btnSelect.setCaption("Select");
				final SearchReallocationDoctorDetailsTableDTO viewSearchCriteriaTableDTO = (SearchReallocationDoctorDetailsTableDTO)itemId;
				btnSelect.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
							String doctoeName = viewSearchCriteriaTableDTO.getDoctorName();
							SearchReallocationDoctorNameForm.popup.close();
							fireViewEvent(SearchReallocationDoctorDetailsPresenter.DOCTOR_SEARCH_CRITERIA, viewSearchCriteriaTableDTO);
							
							
					}
				});
				return btnSelect;
			}
		});
		
		table.setColumnHeader("selectDetails", "Select");
	}
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	@Override
	public void tableSelectHandler(SearchReallocationDoctorDetailsTableDTO t) {
		/*fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, t);
		
		popup.close();*/
		
	}
//	private void generaterColumn(){
//		
//		table.removeGeneratedColumn("Select");
//		
//		table.addGeneratedColumn("select", 
//				new Table.ColumnGenerator() {
//			
//			@Override
//			public Object generateCell(Table source, Object itemId, Object columnId) {
//				Button btnSelect = new Button();
//				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
//				btnSelect.setCaption("Select");
//				final SearchDoctorDetailsTableDTO viewSearchCriteriaTableDTO = (SearchDoctorDetailsTableDTO)itemId;
//				btnSelect.addClickListener(new ClickListener() {
//					
//					@Override
//					public void buttonClick(ClickEvent event) {
//							String doctoeName = viewSearchCriteriaTableDTO.getDoctorName();
//							SearchDoctorNameForm.popup.close();
//							fireViewEvent(SearchDoctorDetailsPresenter.DOCTOR_SEARCH_CRITERIA, viewSearchCriteriaTableDTO);
//							
//							
//					}
//				});
//				return btnSelect;
//			}
//		});
//		
//	}

	public void setTableHeader() {
		
	}
	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-emp-";
	}
	
	public void setWindowObject(Window popup) {
		this.popup = popup;
	}

}
