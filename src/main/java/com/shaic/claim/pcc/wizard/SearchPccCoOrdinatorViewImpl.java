package com.shaic.claim.pcc.wizard;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SearchPccCoOrdinatorViewImpl extends AbstractMVPView implements SearchPccView, Searchable{
	
	@Inject
	private SearchProcessPCCRequestForm searchForm;
	
	@Inject
	private SearchProcessPCCRequestTable searchTable;
	
	private VerticalSplitPanel mainPanel;
	
	private String menuString;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		
		searchForm.init(menuString);
		searchTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchTable);
		mainPanel.setSplitPosition(41);
		setHeight("620px");
		setCompositionRoot(mainPanel);
		searchTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	

	@Override
	public void doSearch() {
		SearchProcessPCCRequestFormDTO searchDto = searchForm.getSearchDTO();
		Pageable pageable = searchTable.getPageable();
		searchDto.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
	
		fireViewEvent(SearchProcessPCCRequestPresenter.SEARCH_PCC_RQUEST, searchDto, userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchTable.getPageable().setPageNumber(1);
		searchTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessPCCRequestTable)
			{
				((SearchProcessPCCRequestTable) comp).removeRow();
			}
		}
	}

	@Override
	public void resetView() {
		searchForm.refresh();
	}
	
	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}

	@Override
	public void list(Page<SearchProcessPCCRequestTableDTO> list) {
		if(null != list && null != list.getPageItems() && 0!= list.getPageItems().size())
		{	
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			searchTable.setUserName(userName);
			searchTable.setTableList(list);
			searchTable.tablesize();
			searchTable.setHasNextPage(list.isHasNext());
		}
		else
		{
		
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		searchForm.enableButtons();
	}

}
