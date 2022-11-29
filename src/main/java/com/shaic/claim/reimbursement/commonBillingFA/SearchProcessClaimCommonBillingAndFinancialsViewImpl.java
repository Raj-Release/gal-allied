/**
 * 
 */
package com.shaic.claim.reimbursement.commonBillingFA;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsFormDTO;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimCommonBillingAndFinancialsViewImpl extends AbstractMVPView implements SearchProcessClaimCommonBillingAndFinancialsView{

	
	@Inject
	private SearchProcessClaimCommonBillingAndFinancialsForm  searchForm;
	
	@Inject
	private SearchProcessClaimCommonBillingAndFinancialsTable searchResultTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(25);
		mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		SearchProcessClaimFinancialsFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		
		Double claimedAmountFrom = searchDTO.getClaimedAmountFrom();
		Double claimedAmountTo = searchDTO.getClaimedAmountTo();
		
		if(claimedAmountFrom != null && claimedAmountTo != null)  {
			if((claimedAmountFrom > claimedAmountTo || claimedAmountTo < claimedAmountFrom)) {
				 getErrorMessage("Claimed Amount From should not less than Claim Amount To");
			}else{
				fireViewEvent(SearchProcessClaimCommonBillingAndFinancialsPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
			}
		     
		}else{
			fireViewEvent(SearchProcessClaimCommonBillingAndFinancialsPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		}
		
	}

	
	public void getErrorMessage(String eMsg){
	
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessClaimCommonBillingAndFinancialsTable)
			{
				((SearchProcessClaimCommonBillingAndFinancialsTable) comp).removeRow();
			}
		}
	
		
	}
	
//	public void setUpDropDownValues(BeanItemContainer<SelectValue> claimType,
//		BeanItemContainer<SelectValue> productName,
//		BeanItemContainer<SelectValue> cpuCode){
//		searchForm.setUpDropDownValues(claimType,productName,cpuCode);
//	}

	@Override
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SearchProcessClaimFinancialsTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
				
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
		}
		
		else
		{
			SearchProcessClaimFinancialsFormDTO searchDTO = searchForm.getSearchDTO();
			/**
			 * If every screen has intimation no within, then the 
			 * below search id setter is not required. Post analysis, shall
			 * think of removing the same.
			 * */
			if(searchDTO.getIntimationNo() != null && !searchDTO.getIntimationNo().isEmpty() && searchDTO.getIntimationNo().length() > 0  && 
					(null == searchDTO.getPolicyNo() ||  searchDTO.getPolicyNo().isEmpty()) && (null == searchDTO.getCpuCode())  
					 && (null == searchDTO.getType()) && (null == searchDTO.getProductName()) && (null == searchDTO.getClaimType()) && (null == searchDTO.getSource()) && (null == searchDTO.getPriority()) && (null == searchDTO.getClaimedAmountFrom()) && (null == searchDTO.getClaimedAmountTo())
					) {
				searchDTO.setSearchId(searchDTO.getIntimationNo());
				fireViewEvent(MenuPresenter.SHOW_SEARCH_SCREEN_VALIDATION_MESSAGE, searchDTO,null);
			} 
			else
			{
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Common for Billing & FA Home");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox("No Records found.", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS, null);
						
					}
				});
			}
			}
		
		searchForm.enableButtons();
	}

	/*public void setUpDropDownValues(BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		searchForm.setUpDropDownValues(claimType,productName,cpuCode,type,statusByStage);
		
	}*/

}
