package com.shaic.claim.pcc.hrmp;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author Panneer Selvam M
 *
 */
public class SearchHRMPCompletedTable extends GBaseTable<SearchHRMPCompletedTableDTO>{

	private final static Object[] VISIBLE_COL_ORDER = new Object[]{
		"intimationNo","referenceNo", "dateOfAdmission","status","hrmUserId",
		"hrmDate", "hrmRemarks", "divissionHeadUserId", "divissionHeadDate", "divissionHeadRemarks"}; 

	@EJB
	private SearchHRMPService hrmService;

	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;

	private VerticalLayout mainLayout;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

		table.removeAllItems();

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchHRMPCompletedTableDTO>(
				SearchHRMPCompletedTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("335px");

	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}

	}

	@Override
	public void tableSelectHandler(SearchHRMPCompletedTableDTO t) {
		// TODO Auto-generated method stub
        if(t.getTabStatus() != null)
		fireViewEvent(MenuPresenter.HRMP_WIZARD, t);
		//setCompleteLayout(t);

	}


	@Override
	public String textBundlePrefixString() {
		return "search-divisionhead-completed-processing-";
	}
}
