package com.shaic.claim.intimation.unlockintimationaudit;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
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

public class SearchUnlockIntimationAuditDBViewImpl extends AbstractMVPView implements SearchUnlockIntimationAuditDBView {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchUnlockIntimationAuditDBForm searchForm;
	
	@Inject
	private SearchUnlockIntimationAuditDBTable searchResultTable;
	
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
			SearchUnlockIntimationAuditDBFormDTO searchDTO = searchForm.getSearchDTO();
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchUnlockIntimationAuditDBPresenter.SEARCH_BUTTON_CLICK_AUDIT_INTIMATION, searchDTO,userName,passWord);
		} else {
			
			showErrorMessage("Please enter the Intimation Number");
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
			if(comp instanceof SearchUnlockIntimationAuditDBTable)
			{
				((SearchUnlockIntimationAuditDBTable) comp).removeRow();
			}
		}
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void list(Page<SearchUnlockIntimationAuditDBTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			searchResultTable.setUserName(userName);
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Record Found.</b>", ContentMode.HTML);			
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
					fireViewEvent(MenuItemBean.AUDIT_INTIMATION_UNLOCK, null);
					
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