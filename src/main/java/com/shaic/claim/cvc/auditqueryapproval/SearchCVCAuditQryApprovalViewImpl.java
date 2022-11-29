package com.shaic.claim.cvc.auditqueryapproval;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;


public class SearchCVCAuditQryApprovalViewImpl extends AbstractMVPView implements SearchCVCAuditQryApprovalView{
	
	@Inject
	private SearchCVCAuditQryApprovalForm searchForm;
	
	@Inject
	private SearchCVCAuditQryApprovalTable searchResultTable;
	
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
		mainPanel.setSplitPosition(33);
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		searchResultTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchCVCAuditQryApprovalTable)
			{
				((SearchCVCAuditQryApprovalTable) comp).removeRow();
			}
		}
	
		
	}	
	
	@Override
	public void list(Page<SearchCVCAuditActionTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}else
		{
			

			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Claims Audit Query Approval Home");
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
			
			homeButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.CVC_AUDIT_QRY_APPRVOAL, null);
					
				}
			});
			
		}
		
		searchForm.enableButtons();
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}
	
	@Override
	public void doSearch() {
		SearchCVCAuditActionFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		if(searchDTO.getIntimationNumber() == null || searchDTO.getIntimationNumber().isEmpty() || searchDTO.getYear() == null || (searchDTO.getYear() != null && searchDTO.getYear().getValue() == null) || searchDTO.getYear().getValue().isEmpty()){
			fireViewEvent(SearchCVCAuditQryApprovalPresenter.SUBMIT_ALERT, searchDTO,userName,passWord);
		}else {
			fireViewEvent(SearchCVCAuditQryApprovalPresenter.SUBMIT_SEARCH, searchDTO,userName,passWord);
		}
		
	}
	
	@Override
	public void init(){
	searchForm.setDropDownValues();
		

	}


	@Override
	public void validation(SearchCVCAuditActionFormDTO formDTO) {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
	
		if (formDTO.getIntimationNumber() == null
				|| (formDTO.getIntimationNumber() != null && formDTO.getIntimationNumber().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Intimation Number </br>");
		}
		
		if (formDTO.getYear() == null || (formDTO.getYear() != null
				&& formDTO.getYear().getValue() == null) || (formDTO.getYear() != null && formDTO.getYear().getValue().isEmpty())) {
			hasError = true;
			eMsg.append("Please Select Year </br>");
		}
		

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			
			Button btn = new Button("Ok");
			btn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btn.setWidth("-1px");
			btn.setHeight("-10px");
			layout.addComponent(btn);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			btn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
		} 
	}

}
	
	
