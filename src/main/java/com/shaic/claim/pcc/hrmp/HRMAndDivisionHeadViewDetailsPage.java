/*

	@Inject
	private SearchHRMPCompletedTable searchHRMPCompletedTable;

	@Inject
	private HRMPPageUI HrmpPageUI;

	@SuppressWarnings("deprecation")
	private VerticalLayout mainLayout;

	@SuppressWarnings("deprecation")
	public void loadData(Intimation intimation) {		
		//faInternalRemarksTable.init("", false, false);
		SearchHRMPCompletedTableDTO dto = HrmpPageUI.getHRMDivisionHeadViewDetails(intimation);
		if(dto != null){
			List<SearchHRMPCompletedTableDTO> resultList = new ArrayList<SearchHRMPCompletedTableDTO>();
			resultList.add(dto);
			searchHRMPCompletedTable.setTableList(resultList);
			mainLayout = new VerticalLayout(searchHRMPCompletedTable);
			mainLayout.setSizeFull();
			setCompositionRoot(mainLayout);
		}
	}
*/



package com.shaic.claim.pcc.hrmp;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class HRMAndDivisionHeadViewDetailsPage extends ViewComponent {

	@Inject
	private SearchHRMPCompletedTable searchHRMPCompletedTable;

	@Inject
	private SearchHRMPService searchHRMPService;

	@SuppressWarnings("deprecation")
	private VerticalLayout mainLayout;

	@SuppressWarnings("deprecation")
	public void loadData(Intimation intimation) {		
		searchHRMPCompletedTable.init("", false, false);
		List<SearchHRMPCompletedTableDTO> resultList = searchHRMPService.getHRMDivisionHeadViewDetails(intimation);
		searchHRMPCompletedTable.setTableList(resultList);
		
		mainLayout = new VerticalLayout(searchHRMPCompletedTable);
		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);
	}

	@SuppressWarnings("deprecation")
	public VerticalLayout showViewTrailsPopup(Intimation intimation) {		
		loadData(intimation);
		return mainLayout;
	}
}
