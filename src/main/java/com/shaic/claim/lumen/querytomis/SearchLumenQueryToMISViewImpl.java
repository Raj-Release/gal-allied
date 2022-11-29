package com.shaic.claim.lumen.querytomis;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.server.VaadinSession;
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
public class SearchLumenQueryToMISViewImpl extends AbstractMVPView implements SearchLumenQueryToMISView{

	@Inject
	private LumenSearchQueryToMISForm lumenForm;
	
	@Inject
	private SearchQueryToMISResultTable lumenTable;

	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		lumenForm.init();
		
		lumenForm.setUserName((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID));
		lumenForm.setEmpNameDropDownValue();

		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(lumenForm);
		mainPanel.setSplitPosition(46);
		setHeight("560px");
		setCompositionRoot(mainPanel);

		lumenForm.addSearchListener(this);
//		lumenTable.init("", false, false);
		lumenTable = lumenForm.lumenTable;
		
		resetView();
	}

	@Override
	public void doSearch() {
		LumenSearchReqFormDTO searchDTO = lumenForm.getSearchDTO();
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		Pageable pageable = null;
		lumenTable.addSearchListener(this);
		pageable = lumenTable.getPageable();
		searchDTO.setPageable(pageable);		
		fireViewEvent(SearchLumenQueryToMISPresenter.DO_LUMEN_QRY_TO_MIS_SEARCH, searchDTO,userName,passWord);
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

	@SuppressWarnings({ "static-access" })
	@Override
	public void renderTable(Page<LumenRequestDTO> tableRows) {

		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
				mainPanel.setSecondComponent(buildSearchResultTableLayout());
				lumenTable.setTableList(tableRows);
				lumenTable.setSubmitTableHeader();
				lumenTable.tablesize();
				lumenTable.setHasNextPage(tableRows.isHasNext());

		} else {

			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Home Page");
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
					fireViewEvent(MenuItemBean.LUMEN_REQUEST_MIS, null);
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

}
