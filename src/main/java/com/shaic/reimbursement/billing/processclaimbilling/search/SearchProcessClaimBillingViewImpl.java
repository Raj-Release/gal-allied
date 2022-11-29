/**
 * 
 */
package com.shaic.reimbursement.billing.processclaimbilling.search;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
/*import org.vaadin.dialogs.ConfirmDialog;*/




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
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
public class SearchProcessClaimBillingViewImpl extends AbstractMVPView implements SearchProcessClaimBillingView{

	
	@Inject
	private SearchProcessClaimBillingForm  searchForm;
	
	@Inject
	private SearchProcessClaimBillingTable searchResultTable;
	
	
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
		mainPanel.setSplitPosition(47);
		setHeight("590px");
	//	mainPanel.setHeight("625px");
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
		SearchProcessClaimBillingFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchProcessClaimBillingPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessClaimBillingTable)
			{
				((SearchProcessClaimBillingTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void list(Page<SearchProcessClaimBillingTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SearchProcessClaimBillingTableDTO tableDto : tableRows.getPageItems()) {
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
			SearchProcessClaimBillingFormDTO searchDTO = searchForm.getSearchDTO();
			/**
			 * If every screen has intimation no within, then the 
			 * below search id setter is not required. Post analysis, shall
			 * think of removing the same.
			 * */
			if(searchDTO.getIntimationNo() != null && !searchDTO.getIntimationNo().isEmpty() && searchDTO.getIntimationNo().length() > 0  && 
					(null == searchDTO.getPolicyNo() ||  searchDTO.getPolicyNo().isEmpty()) && (null == searchDTO.getCpuCode())  
					&& (null == searchDTO.getSource()) && (null == searchDTO.getType()) && (null == searchDTO.getProductNameCode()) && (null == searchDTO.getPriority())
					) {
				searchDTO.setSearchId(searchDTO.getIntimationNo());
				fireViewEvent(MenuPresenter.SHOW_SEARCH_SCREEN_VALIDATION_MESSAGE, searchDTO,null);
			} 
			else
			{
				
				/*Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
				Button homeButton = new Button("Process Claim Billing Home");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
//				HorizontalLayout hLayout = new HorizontalLayout(layout);
//				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Process Claim Billing Home");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox("No Records found.", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
						
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
						
					}
				});
			}
			}
		
		searchForm.enableButtons();
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> selectValueForPriority,BeanItemContainer<SelectValue>  claimType, BeanItemContainer<SelectValue> statusByStage) {
		searchForm.setCBXValue(cpucode, productNameCode,typeContainer,claimType,statusByStage);
		
	}

}
