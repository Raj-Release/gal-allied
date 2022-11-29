
/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

/**
 * @author ntv.vijayar
 *
 */
public class SearchDataEntryViewImpl extends AbstractMVPView implements SearchDataEntryView {
	
	@Inject
	private SearchDataEntryForm  searchForm;
	
	@Inject
	private SearchDataEntryTable searchResultTable;
//	private RRCRequestTableForExcelReport searchResultTable;
	
	private VerticalSplitPanel mainPanel;
	
	Page<SearchDataEntryTableDTO> tableRowsPage = new Page<SearchDataEntryTableDTO>();
	
	private List<SearchDataEntryTableDTO> finalDataList = null;
	
	VerticalLayout secondLayout = null;//new VerticalLayout();
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
	//	mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSecondComponent(buildSecondComponent());
		
		mainPanel.setSplitPosition(31);
		setHeight("560px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
		finalDataList = new ArrayList<SearchDataEntryTableDTO>();
	}
	
	private VerticalLayout buildSecondComponent() {
		secondLayout = new VerticalLayout();
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(searchResultTable);
		// secondLayout.addComponent(tableForExcel);

		return secondLayout;
	}
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		String err=searchForm.validate();
		if(err == null)
		{
		SearchDataEntryFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchDataEntryPresenter.SEARCH_BUTTON_CLICK_DATA_ENTRY_FORM, searchDTO,userName,passWord);
		}
		else
		{
			showErrorMessage(err);
		}
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			
			if(comp instanceof VerticalLayout)
			{
				Iterator<Component> subCompIter = ((VerticalLayout) comp).getComponentIterator();
				while(subCompIter.hasNext())
				{
					Component tableComp = (Component)subCompIter.next();
					if(tableComp instanceof SearchDataEntryTable)
					{
						((SearchDataEntryTable) tableComp).removeRow();
					}
				}
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
	
	@Override
	public void list(Page<SearchDataEntryTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows,"");		
			//searchResultTable.setTableValues(tableRows.getPageItems());
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());		
			
			finalDataList = tableRows.getTotalList();			
			}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Data Entry Home");
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
					fireViewEvent(MenuItemBean.WAR_HOUSE, null);
				}
			});
		}
		searchForm.enableButtons();
	}
	
}

