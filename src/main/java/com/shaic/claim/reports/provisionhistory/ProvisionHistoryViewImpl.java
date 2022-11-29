package com.shaic.claim.reports.provisionhistory;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ProvisionHistoryViewImpl extends AbstractMVPView implements ProvisionHistoryView {

	@Inject
	ProvisionHistoryForm searchForm;
	private VerticalSplitPanel mainPanel;
	
	@Inject
	ProvisionHistoryTable provisionHistoryTableObj;
	
	//@Inject
	//DBCalculationService dbCalculationService;
	@PostConstruct
	protected void initView()
	{
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		//searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		//mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(32);
	    setHeight("570px");
		//setHeight("200px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		//searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	

	}
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		if(searchForm.validatePage())
		{
			ProvisionHistoryFormDTO searchDTO=searchForm.getSearchDTO();
			if(searchDTO.getIntimationNo() != null && (searchDTO.getIntimationNo().contains("CLI") ||
					searchDTO.getIntimationNo().contains("CIR") || searchDTO.getIntimationNo().contains("CIG"))){
			
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			fireViewEvent(ProvisionHistoryPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);			
			}else {
				showErrorMessage("Please Enter the Complete Intimation Number");
				resetView();
				//resetSearchResultTableValues();
			}}
		else
		{
			showErrorMessage("Please Enter Intimation Number");
			resetView();
			
		}
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchForm.refresh();
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
	public void list(List<ProvisionHistoryDTO> search) {
		// TODO Auto-generated method stub
		if(null !=search && !search.isEmpty())
		{
			ProvisionHistoryFormDTO searchDTO=searchForm.getSearchDTO();
			//if(searchDTO.getIntimationNo()!= null)
			//{
				Window popup = new com.vaadin.ui.Window();
				Button closeButton=new Button("Close");
				closeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				
				popup.setCaption("Provision History");
				popup.setWidth("75%");
				popup.setHeight("75%");
				provisionHistoryTableObj.init("", false, false);
				provisionHistoryTableObj.setTableList(search);
				HorizontalLayout hLayout = new HorizontalLayout(closeButton);
				hLayout.setMargin(true);
				
				VerticalLayout layout = new VerticalLayout(provisionHistoryTableObj,hLayout);
				layout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				//popup.setContent(provisionHistoryTableObj);
				popup.setContent(layout);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}

					
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				closeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				
			});

			
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Provision Report Home");
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
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.PROVISION_HISTORY, null);
					
				}
			});
		}
		
		searchForm.enableButtons();

		
	}

}
