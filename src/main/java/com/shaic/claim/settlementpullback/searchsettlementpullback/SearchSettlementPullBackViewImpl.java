package com.shaic.claim.settlementpullback.searchsettlementpullback;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.SearchLotPullBackPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class SearchSettlementPullBackViewImpl extends AbstractMVPView implements SearchSettlementPullBackView {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchSettlementPullBackForm searchForm;
	
	@Inject
	private SearchSettlementPullBackTable searchResultTable;
	
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
		mainPanel.setSplitPosition(32);
		setHeight("570px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		
		
		if(searchForm.validatePage()){
			SearchSettlementPullBackFormDTO searchDTO = searchForm.getSearchDTO();
			// Below condition added for IMSSUPPOR-29947
			if(searchDTO.getIntimationNo() != null && (searchDTO.getIntimationNo().contains("CLI") ||
					searchDTO.getIntimationNo().contains("CIR") || searchDTO.getIntimationNo().contains("CIG"))){
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchSettlementPullBackPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
			} else {
				showErrorMessage("Please Enter Complete Intimation Number");
				resetView();
				resetSearchResultTableValues();
			}
		} else {
			
			showErrorMessage("Please Enter Intimation Number");
			resetView();
			resetSearchResultTableValues();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void resetSearchResultTableValues() {
		
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchSettlementPullBackTable)
			{
				((SearchSettlementPullBackTable) comp).removeRow();
			}
		}
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void list(Page<SearchSettlementPullBackDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("UNDO FA");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.SETTLEMENT_PULL_BACK, null);
					
				}
			});
		}
		
		searchForm.enableButtons();
	}
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}


}
