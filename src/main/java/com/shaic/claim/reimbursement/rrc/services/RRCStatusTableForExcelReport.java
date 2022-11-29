/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class RRCStatusTableForExcelReport extends
		GBaseTable<SearchRRCStatusTableDTO> {

	private final static Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "rrcRequestNo", "dateOfRequestForTable",
			"requestorName", "cpuCode", "intimationNo", "initialSumInsured",
			"presentSumInsured", "productCode", "productName", "pedDisclosed",
			"claimType", "diag", "management", "patientName", "hospitalName",
			"hospitalType", "initialPEDRecommendation", "pedSuggestions",
			"pedName", "amountClaimed", "settledAmount", "amountSaved",
			"categoryValue", "status", "statusDate", "initiateRRCRemarks",
			"processRRCRemarks", "creditsEmployeeName", "creditsEmployeeName2",
			"creditsEmployeeName3", "creditsEmployeeName4",
			"creditsEmployeeName5", "creditsEmployeeName6",
			"creditsEmployeeName7", "lapseEmployeeName", "lapseEmployeeName2",
			"lapseEmployeeName3", "lapseEmployeeName4", "lapseEmployeeName5",
			"lapseEmployeeName6", "lapseEmployeeNmae7" };

	private List<Component> compList = new ArrayList<Component>();

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchRRCStatusTableDTO>(
				SearchRRCStatusTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	public void invokeView(SearchRRCStatusTableDTO t) {
		fireViewEvent(MenuPresenter.SHOW_RRC_STATUS_SCREEN_VIEW, t, this);
	}

	@Override
	public void tableSelectHandler(SearchRRCStatusTableDTO t) {

	}

	@Override
	public String textBundlePrefixString() {

		return "search-rrc-status-excel-";
	}

	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7) {
			table.setPageLength(7);
		}

	}

	public void showSearchRRCStatusView(RRCDTO rrcDTO) {
		fireViewEvent(SearchRRCStatusPresenter.SHOW_RRC_STATUS_VIEW, rrcDTO);
	}

	public void setValueForCheckBox(Boolean value) {
		List<SearchRRCStatusTableDTO> searchTableDTOList = (List<SearchRRCStatusTableDTO>) table
				.getItemIds();
		for (SearchRRCStatusTableDTO searchTableDTO : searchTableDTOList) {

			if (value)
				searchTableDTO.setCheckBoxStatus("true");
			else
				searchTableDTO.setCheckBoxStatus("false");

		}
		if (null != compList && !compList.isEmpty()) {
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}

	}

	public Table getTableEX() {
		return this.table;

	}

	public List<SearchRRCStatusTableDTO> getTableAllItems() {
		return (List<SearchRRCStatusTableDTO>) table.getItemIds();
	}

	public void addBeanToList(List<SearchRRCStatusTableDTO> dtoList) {
		int rowCount = 1;
		List<SearchRRCStatusTableDTO> finalList = new ArrayList<SearchRRCStatusTableDTO>();
		for (SearchRRCStatusTableDTO searchRRCTableDTO : dtoList) {

			if (("true")
					.equalsIgnoreCase(searchRRCTableDTO.getCheckBoxStatus())) {
				searchRRCTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalList.add(searchRRCTableDTO);
			}
		}
		table.addItems(finalList);
	}

	public void setTableValues(List<SearchRRCStatusTableDTO> tableRows) {
		setTableList(tableRows);
		for (SearchRRCStatusTableDTO searchRRCRequestTableDTO : tableRows) {

			String[] creaditListArray = searchRRCRequestTableDTO
					.getCreditEmpList();
			String[] lapseListArray = searchRRCRequestTableDTO
					.getLapseEmpList();

			for (int i = 0; i < creaditListArray.length; i++) {
				String columnName = "Credit" + (i + 1);
				dynamicGenerateCreditAndLapse(columnName, creaditListArray[i]);

			}
			for (int i = 0; i < lapseListArray.length; i++) {
				String columnName = "Lapse" + (i + 1);
				dynamicGenerateCreditAndLapse(columnName, lapseListArray[i]);
			}
		}

	}

	public void dynamicGenerateCreditAndLapse(String columnName,
			final String value) {
		table.removeGeneratedColumn(columnName);
		table.addGeneratedColumn(columnName, new Table.ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {

				TextField txtCredit = new TextField();
				SearchRRCStatusTableDTO tableDTO = (SearchRRCStatusTableDTO) itemId;
				txtCredit.setData(tableDTO);
				txtCredit.setValue(value);

				return txtCredit;
			}
		});
	}

}
