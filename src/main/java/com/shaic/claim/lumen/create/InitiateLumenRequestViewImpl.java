package com.shaic.claim.lumen.create;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class InitiateLumenRequestViewImpl extends AbstractMVPView implements InitiateLumenRequestView{

	@Inject
	private LumenSearchForm lumenForm;

	@Inject
	private LumenSearchResultTable lumenTable;
	
	@Inject
	private LumenPolicySearchResultTable lumenPolicyTable;

	private VerticalSplitPanel mainPanel;


	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		lumenForm.init();

		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(lumenForm);
		mainPanel.setSplitPosition(46);
		setHeight("550px");
		setCompositionRoot(mainPanel);

		lumenForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		String temp = lumenForm.doTypeValidation();
		String searchType = lumenForm.getSearchType();
		if(StringUtils.isBlank(temp)){
			temp = lumenForm.doSearchValidation();
			if(StringUtils.isBlank(temp)){
				LumenSearchFormDTO searchDTO = lumenForm.getSearchDTO();
				String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
				Pageable pageable = null;
				if(searchType.equals("Intimation")){
					lumenTable.addSearchListener(this);
					pageable = lumenTable.getPageable();
					searchDTO.setPageable(pageable);		
					fireViewEvent(InitiateLumenRequestPresenter.DO_LUMEN_SEARCH, searchDTO,userName,passWord);
				}else if(searchType.equals("Policy")){
					lumenPolicyTable.addSearchListener(this);
					pageable = lumenPolicyTable.getPageable();
					searchDTO.setPageable(pageable);
					fireViewEvent(InitiateLumenRequestPresenter.DO_LUMEN_POLICY_SEARCH, searchDTO,userName,passWord);
				}
			}else{
				showErrorPopup(temp);
			}
		}else{
			showErrorPopup(temp);
		}
	}

	@Override
	public void resetSearchResultTableValues() {
		lumenTable.removeRow();
	}

	@Override
	public void resetView() {
		lumenForm.resetAlltheValues();
		lumenTable.removeRow();
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void renderTable(Page<LumenSearchResultTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
			mainPanel.setSecondComponent(buildSearchResultTableLayout());
			lumenTable.init("", false, false);
			lumenTable.setTableList(tableRows.getTotalList());
			lumenTable.setSubmitTableHeader();
			lumenTable.tablesize();
			lumenTable.setHasNextPage(tableRows.isHasNext());
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Initiate Lumen Request Home");
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
					fireViewEvent(MenuItemBean.CREATE_LUMEN, null);
				}
			});
		}
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void renderPolicyTable(Page<LumenPolicySearchResultTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
			mainPanel.setSecondComponent(buildPolicySearchResultTableLayout());
			lumenPolicyTable.init("", false, false);
			lumenPolicyTable.setTableList(tableRows.getTotalList());
			lumenPolicyTable.setSubmitTableHeader();
			lumenPolicyTable.tablesize();
			lumenPolicyTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Initiate Lumen Request Home");
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
					fireViewEvent(MenuItemBean.CREATE_LUMEN, null);
				}
			});
		}
	}

	private VerticalLayout buildSearchResultTableLayout() {

		FormLayout searchTableLayout = new FormLayout();
		searchTableLayout.setSpacing(false);
		searchTableLayout.setMargin(false);		

		VerticalLayout	secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(searchTableLayout);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(lumenTable);

		return secondLayout;		
	}
	
	private VerticalLayout buildPolicySearchResultTableLayout(){
		FormLayout searchTableLayout = new FormLayout();
		searchTableLayout.setSpacing(false);
		searchTableLayout.setMargin(false);		

		VerticalLayout	secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(searchTableLayout);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(lumenPolicyTable);

		return secondLayout;	
	}

	@SuppressWarnings("static-access")
	private void showErrorPopup(String eMsg) {
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
