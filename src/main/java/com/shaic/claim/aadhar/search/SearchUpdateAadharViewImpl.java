package com.shaic.claim.aadhar.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTable;
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

public class SearchUpdateAadharViewImpl extends AbstractMVPView implements SearchUpdateAadharView {
	
	@Inject
	private SearchUpdateAadharDetailsForm searchUpdateAadharForm;
	
	@Inject
	private SearchUpdateAadharDetailsTable searchUpdateAadharTable;
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView(){
		addStyleName("view");
		setSizeFull();
		searchUpdateAadharForm.init();
		searchUpdateAadharTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchUpdateAadharForm);
		mainPanel.setSecondComponent(searchUpdateAadharTable);
		mainPanel.setSplitPosition(45);
		setHeight("570px");
		setCompositionRoot(mainPanel);
		searchUpdateAadharTable.addSearchListener(this);
		searchUpdateAadharForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void init() {
		searchUpdateAadharForm.init();
	}
	
	@Override
	public void doSearch() {
		SearchUpdateAadharDTO searchDTO = searchUpdateAadharForm.getSearchDTO();
		if((searchDTO.getIntimationNo()!=null && searchDTO.getIntimationNo().length()>0)){
			Pageable pageable = searchUpdateAadharTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchUpdateAadharPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		}
		else{
			String emsg ="Please Enter Intimation Number";
			showErrorPopUp(emsg);
		}
	}

	@Override
	public void resetSearchResultTableValues() {
		searchUpdateAadharTable.getPageable().setPageNumber(1);
		searchUpdateAadharTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchUploadPanCardTable)
			{
				((SearchUploadPanCardTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void resetView() {
		searchUpdateAadharForm.refresh();
	}
	
	@Override
	public void showErrorPopUp(String emsg) {
			Label label = new Label(emsg, ContentMode.HTML);
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
	public void list(Page<SearchUpdateAadharTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchUpdateAadharTable.setTableList(tableRows);
//			searchUpdateAadharTable.tablesize();
			searchUpdateAadharTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: red;'>Please Enter GMC Claim.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Update Aadhar Details");
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
					fireViewEvent(MenuItemBean.UPDATE_AADHAR_DETAILS, null);
					
				}
			});
		}
		searchUpdateAadharForm.enableButtons();
	}

}
