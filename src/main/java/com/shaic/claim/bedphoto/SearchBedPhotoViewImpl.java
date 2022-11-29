package com.shaic.claim.bedphoto;

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
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchBedPhotoViewImpl extends AbstractMVPView implements SearchBedPhoto{
	
	@Inject
	private SearchBedPhotoSearchForm searchBedPhtoFormUI;
	
	@Inject
	private SearchBedPhotoTable searchBedPhotoTable;
	
	private VerticalSplitPanel mainPanel;
	
//	private VerticalLayout mainPanel;
	
	@PostConstruct
	protected void initView(){
		addStyleName("view");
		setSizeFull();
		searchBedPhtoFormUI.init();
		searchBedPhotoTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchBedPhtoFormUI);
		mainPanel.setSecondComponent(searchBedPhotoTable);
		mainPanel.setSplitPosition(34);
		mainPanel.setSizeFull();
//		mainPanel = new VerticalLayout(searchBedPhtoFormUI,searchBedPhotoTable);
		setHeight("510px");
		setCompositionRoot(mainPanel);
		searchBedPhotoTable.addSearchListener(this);
		searchBedPhtoFormUI.addSearchListener(this);
		resetView();
	}

	@Override
	public void init() {
		searchBedPhtoFormUI.init();
	}
	
	@Override
	public void doSearch() {
			BedPhotoDTO searchDTO = searchBedPhtoFormUI.getSearchDTO();
			Pageable pageable = searchBedPhotoTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchBedPhotoPreseneter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {

		searchBedPhotoTable.getPageable().setPageNumber(1);
		searchBedPhotoTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchBedPhotoTable)
			{
				((SearchBedPhotoTable) comp).removeRow();
			}
		}
	
		
	
	}

	@Override
	public void resetView() {
		searchBedPhtoFormUI.refresh();
	}
	
	@Override
	public void list(Page<SearchBedPhotoTableDTO> tableDto) {
		if(null != tableDto && null != tableDto.getPageItems() && 0!= tableDto.getPageItems().size())
		{	
			searchBedPhotoTable.setTableList(tableDto);
			searchBedPhotoTable.tablesize();
			searchBedPhotoTable.setHasNextPage(tableDto.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: red;'>No Records Found</b>", ContentMode.HTML);			
			Button homeButton = new Button("Bed Photo Home");
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
					fireViewEvent(MenuItemBean.UPLOAD_BED_PHOTO, null);
					
				}
			});
		}
		searchBedPhtoFormUI.enableButtons();
	}

}
