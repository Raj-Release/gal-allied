package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

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

public class SearchClaimWiseAllowApprovalViewImpl extends AbstractMVPView implements SearchClaimWiseAllowApprovalView {
	
	@Inject
	private SearchClaimWiseAllowApprovalForm searchClaimWiseAllowApprovalForm;
	
	@Inject
	private SearchClaimWiseAllowApprovalTable searchClaimWiseAllowApprovalTable;
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView(){

		addStyleName("view");
		setSizeFull();
		searchClaimWiseAllowApprovalForm.init();
		searchClaimWiseAllowApprovalTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchClaimWiseAllowApprovalForm);
		mainPanel.setSecondComponent(searchClaimWiseAllowApprovalTable);
		mainPanel.setSplitPosition(34);
		mainPanel.setSizeFull();
		setHeight("570px");
		setCompositionRoot(mainPanel);
		searchClaimWiseAllowApprovalTable.addSearchListener(this);
		searchClaimWiseAllowApprovalForm.addSearchListener(this);
		resetView();
	
	}

	@Override
	public void doSearch() {
		SearchClaimWiseAllowApprovalFormDto searchDTO = searchClaimWiseAllowApprovalForm.getSearchDTO();
		if((searchDTO.getIntimationNo() != null && !("").equalsIgnoreCase(searchDTO.getIntimationNo())) || 
				(searchDTO.getPolicyNo() != null && !("").equalsIgnoreCase(searchDTO.getPolicyNo())) ||
				searchDTO.getClaimNo() !=null && !("").equalsIgnoreCase(searchDTO.getClaimNo())){
		Pageable pageable = searchClaimWiseAllowApprovalTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchClaimWiseAllowApprovalPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		} else {
			getErrorMessage("Any One Field is mandatory for search");
		}
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchClaimWiseAllowApprovalTable.getPageable().setPageNumber(1);
		searchClaimWiseAllowApprovalTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchClaimWiseAllowApprovalTable)
			{
				((SearchClaimWiseAllowApprovalTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void resetView() {
		searchClaimWiseAllowApprovalForm.refresh();
	}
	
	
	@Override
	public void list(Page<SearchClaimWiseAllowApprovalDto> tableRows){
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchClaimWiseAllowApprovalTable.setTableList(tableRows);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: red;'>Policy is not Cancelled</b>", ContentMode.HTML);			
			Button homeButton = new Button("OK");
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
					fireViewEvent(MenuItemBean.CLAIM_WISE_ALLOW_APPROVAL, null);
					
				}
			});
		}
		searchClaimWiseAllowApprovalForm.enableButtons();
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

}
