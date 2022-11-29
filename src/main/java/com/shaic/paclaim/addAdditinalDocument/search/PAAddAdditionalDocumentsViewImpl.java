package com.shaic.paclaim.addAdditinalDocument.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentFormDTO;
import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentTableDTO;
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

public class PAAddAdditionalDocumentsViewImpl extends AbstractMVPView implements PAAddAdditionalDocumentsView{

	
	@Inject
	private PAAddAdditionalDocumentsForm searchForm;
	
	@Inject
	private PAAddAdditionalDocumentsTable searchResultTable;
	
private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(36);
		setHeight("553px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	
	
	@Override
	public void doSearch() {
		SearchAddAdditionalDocumentFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		//if((searchDTO.getIntimationNo() == null && searchDTO.getPolicyNo() == null) ||(searchDTO.getIntimationNo().isEmpty() && searchDTO.getPolicyNo().isEmpty())){
		/*if(( (null == searchDTO.getIntimationNo() || searchDTO.getIntimationNo().isEmpty()) && (null == searchDTO.getPolicyNo() ||  searchDTO.getPolicyNo().isEmpty()))){
			fireViewEvent(PAAddAdditionalDocumentsPresenter.SEARCH_ERROR, searchDTO,userName,passWord);
			} else{*/
				fireViewEvent(PAAddAdditionalDocumentsPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
			//}
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof PAAddAdditionalDocumentsTable)
			{
				((PAAddAdditionalDocumentsTable) comp).removeRow();
			}
		}
	
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void list(Page<SearchAddAdditionalDocumentTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Add Additional Documents Home");
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
					fireViewEvent(MenuItemBean.PA_ADD_ADDITIONAL_DOCUMENTS, null);
					
				}
			});
			searchForm.enableButtons();
		}
		searchForm.enableButtons();
	}



	@Override
	public void validation() {
		Label Label = new Label("<p style = 'color: red;'>Please Enter Intimation number or Policy number </p>", ContentMode.HTML);
		VerticalLayout layout = new VerticalLayout(Label);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		searchForm.enableButtons();
	}
	
	
			
		

}
