package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.SearchStopPaymentRequestFormView;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.SearchStopPaymentRequestPresenter;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDetailTable;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestFormDTO;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SearchStopPaymentValidationViewImpl extends AbstractMVPView implements StopPaymentValidationView {
	

	
	@Inject
	private SearchStopPaymentValidationFormView searchIntimation;

	@Inject
	private StopPaymentValidationDetailTable searchResultTable;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchIntimation.init();
		searchResultTable.init("", false, false);
		mainPanel.setFirstComponent(searchIntimation);
		Panel panel = new Panel(searchResultTable);
		mainPanel.setSecondComponent(panel);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(33);
//		setHeight("330px");
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchIntimation.addSearchListener(this);
		resetView();
	}


	@Override
	public void resetView() {
		if(searchIntimation != null) {
			searchIntimation.refresh();
			searchResultTable.getPageable().setPageNumber(1);
		    searchResultTable.resetTable();
			searchResultTable.removeRow();
		}
	}

	@Override
	public void doSearch() {
		
		StopPaymentRequestFormDTO searchDTO = searchIntimation.getSearchDTO();
		String err = searchIntimation.validate(searchDTO);
		if(err== null)
     {
		
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchStopPaymentValidationPresenter.GET_SEARCH_RESULT, searchDTO,userName,passWord);
     }
		else
		{
			showErrorMessage(err);
		}
	}

	private void showErrorMessage(String err) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Intimation or Cheque/DD Number is mandatory for search", buttonsNamewithType);
		
	}


	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
	    searchResultTable.resetTable();
		searchResultTable.removeRow();		
	}


	@Override
	public void showSearchViewDetailIntimationTable(
			SearchIntimationFormDto searchDto) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resetSearchIntimationView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showSearchResultViewDetail(
			Page<StopPaymentRequestDto> tableRows) {

		
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
        HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("No Records found.", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					
				}
			});
		}
		
		searchIntimation.enableButtons();
	
		
	}
	
	
	


}
