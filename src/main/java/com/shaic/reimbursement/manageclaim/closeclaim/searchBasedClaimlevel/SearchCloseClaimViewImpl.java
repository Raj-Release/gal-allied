/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimPresenterRODLevel;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 *
 */
public class SearchCloseClaimViewImpl extends AbstractMVPView implements SearchCloseClaimView{

	
	@Inject
	private SearchCloseClaimForm  searchForm;
	
	@Inject
	private SearchCloseClaimTable searchResultTable;
	
	
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
		mainPanel.setSplitPosition(41);
		mainPanel.setSizeFull();
		setHeight("570px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		SearchCloseClaimFormDTO searchDTO = searchForm.getSearchDTO();
		
		if(searchDTO != null && ((searchDTO.getIntimationNo() != null && !("").equalsIgnoreCase(searchDTO.getIntimationNo())) || (searchDTO.getPolicyNo() != null && !("").equalsIgnoreCase(searchDTO.getPolicyNo()) )) ){
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			
//			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			/*For Slowness Issue commented below line add new condition
			fireViewEvent(SearchCloseClaimPresenter.SEARCH_BUTTON_CLICK, searchDTO);*/
			if(searchDTO.getIntimationNo() != null && !searchDTO.getIntimationNo().isEmpty()){
				if(!searchDTO.getIntimationNo().isEmpty() && (searchDTO.getIntimationNo().contains("CLI") ||
						searchDTO.getIntimationNo().contains("CIR") || searchDTO.getIntimationNo().contains("CIG"))){
					fireViewEvent(SearchCloseClaimPresenter.SEARCH_BUTTON_CLICK, searchDTO);
				} else {
					showErrorMessage("Please Enter Complete Intimation Number");
					resetView();
					resetSearchResultTableValues();
				}
			} else {
				fireViewEvent(SearchCloseClaimPresenter.SEARCH_BUTTON_CLICK, searchDTO);
			}
		}
		else{
			Notification.show("", "Please Provide Atleast One Input Intimation No. / Poicy No. For Search ", Notification.TYPE_ERROR_MESSAGE);
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
			if(comp instanceof SearchCloseClaimTable)
			{
				((SearchCloseClaimTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void getResultList(Page<SearchCloseClaimTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Close Claim Home");
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
					fireViewEvent(MenuItemBean.CLOSE_CLAIM_CLAIM_LEVEL, null);
					
				}
			});
		}
		searchForm.enableButtons();
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
