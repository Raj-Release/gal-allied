package com.shaic.claim.medical.opinion;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchRecordMarkEscViewImpl extends AbstractMVPView implements SearchRecordMarkEscView, Searchable   {
	

	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private RecordMarketingEscalationSearchForm searchForm;
	
	@Inject
	private SearchRecordMarkEscTable searchResultTable;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();	
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		
	//	searchResultTable.setHeight("100.0%");
		//searchResultTable.setWidth("100.0%");
		/*searchResultTable.setWidth("1378px");
		searchResultTable.setHeight("");
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));
	*/
		
		//searchResultTable.setWidth("1395px");
		//searchResultTable.setWidth("1850px");

		
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));
		
		//searchResultTable.setHeight("380px");
		
		//searchForm.setHeight(600, Unit.PIXELS);

		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);

		mainPanel.setWidth("100.0%");
		mainPanel.setSplitPosition(25);
		//mainPanel.m
		//setHeight("100.0%");
		mainPanel.setSizeFull();
		setHeight("600px");
		setCompositionRoot(mainPanel);

		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		
		searchForm.refresh(); 
		/*if(searchForm.get() != null) {
			searchForm.get().init();
		}*/
	}

	@Override
	public void list(Page<SearchRecordMarkEscTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SearchRecordMarkEscTableDTO tableDto : tableRows.getPageItems()) {
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
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Record Marketing Escalations Home");
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
					fireViewEvent(MenuItemBean.RECORD_MARKETING_ESCALATION, null);
					
				}
			});
			
		}
		
		//searchResultTable.setTableList(tableRows.getPageItems());
		
	}

	@Override
	public void doSearch() {
		
		Boolean validate = searchForm.validate();
		if(validate){
			
			SearchRecordMarkEscFormDTO searchDTO = searchForm.getSearchDTO();
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
		
			fireViewEvent(SearchRecordMarkEscPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		} else {
			 
			showErrorMessage("Please Enter Intimation Number");
		}
	}
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchRecordMarkEscTable)
			{
				((SearchRecordMarkEscTable) comp).removeRow();
			}
		}
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
