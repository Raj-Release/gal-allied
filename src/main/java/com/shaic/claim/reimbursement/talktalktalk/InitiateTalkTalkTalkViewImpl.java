package com.shaic.claim.reimbursement.talktalktalk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestForm;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestFormDTO;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestPresenter;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestTable;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestTableDTO;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestView;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class InitiateTalkTalkTalkViewImpl extends AbstractMVPView implements InitiateTalkTalkTalkView{

	@Inject
	private InitiateTalkTalkTalkForm initiateTALKTALKTALKForm;

	@Inject
	private InitiateTalkTalkTalkTable initiateTALKTALKTALKTable;

	private VerticalSplitPanel mainPanel;
	
	//@Inject
	//private ReceiptOfDocumentsDTO bean;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		initiateTALKTALKTALKForm.init(); 
		initiateTALKTALKTALKTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(initiateTALKTALKTALKForm);
		mainPanel.setSecondComponent(initiateTALKTALKTALKTable);
		mainPanel.setSplitPosition(20);
		setHeight("650px");
		setCompositionRoot(mainPanel);
		initiateTALKTALKTALKTable.addSearchListener(this);
		initiateTALKTALKTALKForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		initiateTALKTALKTALKForm.refresh();

	}
	
	@Override
	public void init(){
		initiateTALKTALKTALKForm.setDropDownValues();
	}
	
	

	@Override
	public void doSearch() {
		Boolean isValid=initiateTALKTALKTALKForm.validate();

		

		if(isValid)
		{
			InitiateTalkTalkTalkFormDTO initiateRRCDTO = initiateTALKTALKTALKForm.getSearchDTO();
			String userName = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(InitiateTalkTalkTalkPresenter.SEARCH_BUTTON_CLICK_INITIATE_TALK_TALK,initiateRRCDTO, userName, passWord);
		}
		else{
			//User not confirm
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
		initiateTALKTALKTALKTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof InitiateTalkTalkTalkTable) {
				((InitiateTalkTalkTalkTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<InitiateTalkTalkTalkTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			initiateTALKTALKTALKTable.setTableList(tableRows);
//			fvrTable.tablesize();
			initiateTALKTALKTALKTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Initiate TALK TALK TALK Home");
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
					fireViewEvent(MenuItemBean.INITIATE_TALK_TALK_TALK, null);
				}
			});
		}
	}


	
	
}
