package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.OMPProcessNegotiation.search.OMPProcessOmpAcknowledgementDocumentsUI;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
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


public class OMPProcessOmpAcknowledgementDocumentsViewImpl extends AbstractMVPView implements OMPProcessOmpAcknowledgementDocumentsView{
	
	
	@Inject
	private OMPProcessOmpAcknowledgementDocumentsUI searchForm;
	
	OMPProcessOmpAcknowledgementDocumentsFormDto searchDto;
	
	@Inject
	private OMPProcessOmpAcknowledgementDocumentsTable searchResultTable;
	
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
		mainPanel.setSplitPosition(46);
		setHeight("550px");
	//	mainPanel.setHeight("630px");
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
			if(comp instanceof OMPProcessOmpAcknowledgementDocumentsTable)
			{
				((OMPProcessOmpAcknowledgementDocumentsTable) comp).removeRow();
			}
		}
	
		
	}
	@Override
	public void list(Page<OMPProcessOmpAcknowledgementDocumentsTableDTO> page){
		if(null != page && null != page.getPageItems() && 0!= page.getPageItems().size())
		{	
			searchResultTable.setTableList(page);
			searchResultTable.tablesize();
			//searchResultTable.setPage(tableRows);
		}else
		{
			

			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Claim Process  Home");
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
					fireViewEvent(MenuItemBean.OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED, null);
					
				}
			});
			
		}
		
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}
	
	@Override
	public void doSearch() {
		OMPProcessOmpAcknowledgementDocumentsFormDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(OMPProcessOmpAcknowledgementDocumentsPresenter.SUBMIT_SEARCH, searchDTO,userName,passWord);
		
	}
	/*@Override
	public void init(BeanItemContainer<SelectValue> classification){
	searchForm.setDropDownValues(classification);
		

	}*/
	
	

}
