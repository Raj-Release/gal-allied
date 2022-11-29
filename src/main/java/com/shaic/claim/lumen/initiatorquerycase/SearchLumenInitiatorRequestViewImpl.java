package com.shaic.claim.lumen.initiatorquerycase;

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
public class SearchLumenInitiatorRequestViewImpl extends AbstractMVPView implements SearchLumenInitiatorRequestView{

	@Inject
	private LumenSearchInitiatorQueryCaseForm lumenForm;
	
	@Inject
	private SearchInitiatorCaseResultTable lumenTable;

	private VerticalSplitPanel mainPanel;
	
	private StringBuilder errMsg = new StringBuilder();
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		lumenForm.init();
		
		lumenForm.setUserName((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID));

		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(lumenForm);
		mainPanel.setSplitPosition(46);
		setHeight("550px");
		setCompositionRoot(mainPanel);

		lumenForm.addSearchListener(this);
//		lumenTable.init("", false, false);
		lumenTable = lumenForm.lumenTable;
		
		resetView();
	}

	@Override
	public void doSearch() {
		if(!validatePage()){
			LumenSearchReqFormDTO searchDTO = lumenForm.getSearchDTO();
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			Pageable pageable = null;
			lumenTable.addSearchListener(this);
			pageable = lumenTable.getPageable();
			searchDTO.setPageable(pageable);		
			fireViewEvent(SearchLumenInitiatorRequestPresenter.DO_LUMEN_REQ_INITIATOR_SEARCH, searchDTO,userName,passWord);
		}else{
			showErrorMessage(errMsg.toString());
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		if(!(lumenForm.getSearchDTO().getFrmDate() == null  && lumenForm.getSearchDTO().getToDate() == null) && 
				(lumenForm.getSearchDTO().getFrmDate() == null  || lumenForm.getSearchDTO().getToDate() == null)){
			hasError = true;
			errMsg.append("Please fill From and To date.");
		}
		if(lumenForm.getSearchDTO().getFrmDate() != null && lumenForm.getSearchDTO().getToDate() != null){
			long fromDate = lumenForm.getSearchDTO().getFrmDate().getTime();
			long toDate = lumenForm.getSearchDTO().getToDate().getTime();
			long diff = toDate - fromDate;
			float dayCount = (diff / (1000*60*60*24));
			if(dayCount > 120){
				hasError = true;
				errMsg.append("From and To date Range should be less than or equal to 120 days.");
			}
		}		
		return hasError;
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
					fireViewEvent(MenuItemBean.LUMEN_REQUEST_INITIATOR, null);
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
	
	@SuppressWarnings("static-access")
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
