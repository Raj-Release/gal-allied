package com.shaic.claim.doctorinternalnotes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.ClaimDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.processfieldvisit.search.FieldVisitTableForExcel;
import com.vaadin.addon.tableexport.ExcelExport;
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

public class SearchInternalNotesViewImpl extends AbstractMVPView implements SearchInternalNotesView, Searchable  {
	
	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SearchIniternalNotesForm searchForm;
	
	@Inject
	private SearchInternalNotesTable searchResultTable;	
	
	private VerticalLayout verticalLayout;
	
	private VerticalSplitPanel mainPanel;
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		searchForm.init();
		searchResultTable.init("", false, false);

		mainPanel = new VerticalSplitPanel();

		mainPanel.setFirstComponent(searchForm);
		
		verticalLayout = new VerticalLayout(searchResultTable);
		verticalLayout.setSpacing(true);
		
		mainPanel.setSecondComponent(verticalLayout);
		mainPanel.setSplitPosition(40);
		setHeight("570px");
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
	public void list(Page<NewIntimationDto> tableRows) {
		//searchResultTable.setTableList(tableRows.getPageItems());
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			//searchResultTable.setPage(tableRows);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Internal Note Home");
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
					fireViewEvent(MenuItemBean.SEARCH_INTERNAL_NOTES, null);
					
				}
			});
		}
	
	}
	
	@Override
	public void doSearch() {
		NewIntimationDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
//		searchDTO.setPageable(pageable);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		fireViewEvent(SearchInternalNotesPresenter.SEARCH_INTERNAL_NOTES_BUTTON_CLICK, searchDTO,userName);
	}
	
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchInternalNotesTable)
			{
				((SearchInternalNotesTable) comp).removeRow();
			}
		}
	}
	
}
