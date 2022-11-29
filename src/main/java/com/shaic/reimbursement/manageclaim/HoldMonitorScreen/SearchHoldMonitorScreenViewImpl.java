package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
//import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalPresenter;
//import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchHoldMonitorScreenViewImpl extends AbstractMVPView implements SearchHoldMonitorScreenView{
	
	@Inject
	private SearchHoldMonitorScreenFormPage searchPage;
	
	@Inject
	private SearchHoldMonitorScreenTable searchTable;
	
	private VerticalSplitPanel splitPanel;
	
	private String screenName;
	
	
	protected void initView(String screenName){

		addStyleName("view");
		setSizeFull();
		searchPage.init(screenName);
		searchTable.init("", false, false);
		splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(searchPage);
		splitPanel.setSecondComponent(searchTable);
		splitPanel.setSplitPosition(27);
		splitPanel.setSizeFull();
		setHeight("720px");
		setCompositionRoot(splitPanel);
		searchTable.addSearchListener(this);
		searchPage.addSearchListener(this);
		resetView();
	
	}
 
	@Override
	public void doSearch() {
			SearchHoldMonitorScreenFormDTO formDTO = searchPage.getSearchDTO();
			Pageable pageable = searchTable.getPageable();
			formDTO.setPageable(pageable);
			formDTO.setScreenName(this.screenName);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchMonitorScreenPreseneter.SEARCH_BUTTON, formDTO,userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchTable.resetTable();
		Iterator<Component> componentIter = splitPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchHoldMonitorScreenTable)
			{
				((SearchHoldMonitorScreenTable) comp).removeRow();
			}
		}
		
	}

	@Override
	public void resetView() {
		searchPage.refresh();
	}

	@Override
	public void list(Page<SearchHoldMonitorScreenTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchTable.setTableList(tableRows);
			searchTable.setHasNextPage(tableRows.isHasNext());
			searchTable.getScreenName(this.screenName);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Home");
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
					resetSearchResultTableValues();
					if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION)){
						fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_MA, null);
					}
					else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_FOR_FINANCIAL_AUTO)){
						fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_FA_AUTO, null);
					}else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)){
						fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_BILLING, null);
					}else {
						fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN, null);
					}
				}
			});
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
	
	@Override
	public void init(BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName){
		initView(screenName);
		searchPage.setComboBoxValue(type);
		searchPage.setUserComboValues(userList);
		searchPage.setCpuCodeComboBoxValue(cpuCode);
		searchPage.setScreenName(screenName);
		this.screenName = screenName;
	}

	public void buildSuccessLayout(String message) {
		Label successLabel = null;
		if(message != null && !message.isEmpty() && message.equalsIgnoreCase("Successfully")){
			 successLabel = new Label(
					"<b style = 'color: green;'>Released Successfully!!!</b>",
					ContentMode.HTML);
		} else {
			successLabel = new Label(
					"<b style = 'color: green;'>Not Released!!!</b>",
					ContentMode.HTML);
		}
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

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
				if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION)){
					fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_MA, null);
				}
				else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_FOR_FINANCIAL_AUTO)){
					fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_FA_AUTO, null);
				}
				else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)){
					fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_BILLING, null);
				}else {
					fireViewEvent(MenuItemBean.HOLD_MONITOR_SCREEN, null);
				}

			}
		});
	}

//	@Override
//	public void resetSearchResultTableValues() {
//		// TODO Auto-generated method stub
//		
//	}
	

}