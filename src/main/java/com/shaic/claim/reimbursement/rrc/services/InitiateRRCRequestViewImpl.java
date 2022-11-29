package com.shaic.claim.reimbursement.rrc.services;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
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

public class InitiateRRCRequestViewImpl extends AbstractMVPView implements InitiateRRCRequestView{

	@Inject
	private InitiateRRCRequestForm initiateRRCForm;

	@Inject
	private InitiateRRCRequestTable initiateRRCTable;

	private VerticalSplitPanel mainPanel;
	
	//@Inject
	//private ReceiptOfDocumentsDTO bean;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		initiateRRCForm.init(); 
		initiateRRCTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(initiateRRCForm);
		mainPanel.setSecondComponent(initiateRRCTable);
		mainPanel.setSplitPosition(38);
		setHeight("650px");
		mainPanel.setHeight("675px");
		setCompositionRoot(mainPanel);
		initiateRRCTable.addSearchListener(this);
		initiateRRCForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		initiateRRCForm.refresh();

	}
	

	@Override
	public void doSearch() {
		String err=initiateRRCForm.validate();
		if(err == null)
		{
		InitiateRRCRequestFormDTO initiateRRCDTO = initiateRRCForm.getSearchDTO();
		//	Pageable pageable = initiateRRCForm.getPageable();
		//	initiateRRCDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					InitiateRRCRequestPresenter.SEARCH_BUTTON_CLICK_INITIATE_RRC_REQUEST,
					initiateRRCDTO, userName, passWord);
		}
		else
		{
			showErrorMessage(err);
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
	public void resetSearchResultTableValues() {
		initiateRRCTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof InitiateRRCRequestTable) {
				((InitiateRRCRequestTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<InitiateRRCRequestTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			initiateRRCTable.setTableList(tableRows);
//			fvrTable.tablesize();
			initiateRRCTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Initiate RRC Request Home");
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
					fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST, null);
				}
			});
		}
		initiateRRCForm.enableButtons();
	}


	
	
}
