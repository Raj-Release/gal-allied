package com.shaic.claim.omp.newregistration;

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
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewRegistrationViewImpl extends AbstractMVPView implements OMPNewRegistrationView{

	private static final long serialVersionUID = 2320380698659756970L;

	@Inject
	private OMPNewRegistationSearchForm searchForm;
	
	@Inject
    private OMPNewRegistrationSearchTable searchResultTable;
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		searchResultTable.addSearchListener(this);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSplitPosition(46);
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchForm.addSearchListener(this);
	}
	
	private VerticalLayout buildSearchIntimationTableLayout() {
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
		secondLayout.addComponent(searchResultTable);
		return secondLayout;		
	}
	
	
	@Override
	public void resetSearchResultTableValues() {
//		searchForm.setPassportFieldEnable(true);
//		searchForm.resetAlltheValues();		
//		searchForm.setPassportFieldEnable(false);
		searchResultTable.getPageable().setPageNumber(1);
	    searchResultTable.resetTable();
	}

	@SuppressWarnings({"static-access" })
	@Override
	public void list(Page<OMPNewRegistrationSearchDTO> tableRows) {

		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
			mainPanel.setSecondComponent(buildSearchIntimationTableLayout());
			searchResultTable.setTableList(tableRows);
//			searchResultTable.setSubmitTableHeader();
			/*for(OMPNewRegistrationSearchDTO rec : tableRows.getPageItems()){
				PagerUI pagerUI = (PagerUI)searchResultTable.getLayout().getComponent(0);
				rec.setTable(pagerUI);
			}*/
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search & Create Intimation Home");
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
					fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
				}
			});
		}
		searchForm.enableButtons();
	}

	@Override
	public void resetView() {
		//searchForm.refresh(); 
		searchForm.resetAlltheValues();
		if(mainPanel.getSecondComponent() != null){
			mainPanel.removeComponent(mainPanel.getSecondComponent());
		}
	}



	@Override
	public void doSearch() {
		OMPNewRegistrationSearchDTO searchDTO = searchForm.getSearchDTO();
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		searchDTO.setPageable(searchResultTable.getPageable());
		fireViewEvent(OMPNewRegistrationPresenter.OMP_REG_SEARCH, searchDTO,userName,passWord, searchResultTable);
		
		/*if (displayType.equals(SHAConstants.SEARCH_INTIMATION.toString()) &&!searchForm.doValidation().equals("No value")) {
			searchResultTable.addSearchListener(this);
			pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			fireViewEvent(OMPCreateIntimationPresenter.OMP_SUBMIT_SEARCH, searchDTO,userName,passWord);
		} 
		
		if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString()) &&searchForm.doValidation().equals("No value")){
			String eMsg ="Please Enter altlest one input Parameters for Search";
			showErrorPopup(eMsg);
		}*/
		
		}

	/*private void showErrorPopup(String eMsg) {
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
	}*/
}
