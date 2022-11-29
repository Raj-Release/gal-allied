package com.shaic.claim.corpbuffer.allocation.search;

import java.util.Iterator;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class AllocateCorpBufferViewImpl extends AbstractMVPView implements AllocateCorpBufferView{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AllocateCorpBufferSearchForm  corpBufferSearchForm;
	
	@Inject
	private AllocateCorpBufferTable searchResultTable;
	
	private VerticalSplitPanel mainPanel;

	private AllocateCorpBufferFormDTO dto =  new AllocateCorpBufferFormDTO();
	
	private String screenName;
	
	@Override
	public void init() {
		addStyleName("view");
		setSizeFull();
		corpBufferSearchForm.initView(dto);
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(corpBufferSearchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(30);
		setHeight("550px");
		setCompositionRoot(mainPanel);
		corpBufferSearchForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void doSearch() {
		AllocateCorpBufferFormDTO searchDTO = corpBufferSearchForm.getSearchDTO();
		if((searchDTO.getIntimationNo() != null && !("").equalsIgnoreCase(searchDTO.getIntimationNo())) || (searchDTO.getPolicyNo() != null && !("").equalsIgnoreCase(searchDTO.getPolicyNo()))){
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			fireViewEvent(AllocateCorpBufferPresenter.SEARCH_BUTTON_CLICK, searchDTO);
		} else {
			Notification.show("", "Please Enter Intimation No or Policy No to Search", Notification.TYPE_ERROR_MESSAGE);
		}
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
	    searchResultTable.resetTable();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext()) {
			Component comp = (Component)componentIter.next();
			if (comp instanceof AllocateCorpBufferTable) {
				((AllocateCorpBufferTable) comp).removeRow();
			}
		}
	}

	@Override
	public void resetView() {
		if (corpBufferSearchForm != null) {
			corpBufferSearchForm.refresh();
		}
	}
	
	@Override
	public void list(Page<AllocateCorpBufferTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (AllocateCorpBufferTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
					
				}
				searchResultTable.setData(tableDto);
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);
			Button homeButton = new Button("Allocate Corporate Buffer Home");
			if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
				homeButton.setCaption("Waiting For Input Home");
			}
			
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();

					if (null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))) {
						fireViewEvent(MenuItemBean.ALLOCATE_CORP_BUFFER, null);
					}
					else {
						fireViewEvent(MenuItemBean.ALLOCATE_CORP_BUFFER, null);
					}
					resetSearchResultTableValues();
				}
			});
		}
	}

}
