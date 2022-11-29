package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentReprocessSearchViewImpl extends AbstractMVPView implements PaymentReprocessSearchView{

	private static final long serialVersionUID = 9157158848005050481L;
	
	@Inject
	private PaymentReprocessSearchForm  searchForm;
	
	@Inject
	PaymentReprocessSearchResultTable PaymentReprocessTable;
	
	private VerticalSplitPanel mainPanel;
	
	
	final Window searchPopup = new com.vaadin.ui.Window();
	private VerticalLayout SecondVL;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchForm.setViewImp(this);
		PaymentReprocessTable.init("", false,searchPopup);
		PaymentReprocessTable.addSearchListener(this);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		//mainPanel.setSecondComponent(PaymentReprocessTable);
		mainPanel.setSplitPosition(30);
		setWidth("100%");
		setHeight("910px");
		setCompositionRoot(mainPanel);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		System.out.println("Calling do search Method.....");
		PaymentReprocessSearchFormDTO searchDTO = searchForm.getSearchDTO();
		String err = searchForm.validate(searchDTO);
		if(null == err){
			//Pageable pageable = PaymentReprocessTable.getPageable();
			//searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(PaymentReprocessSearchPresenter.PAYMENT_REPROCESS_SEARCH, searchDTO,userName,passWord);
		}else{
			showErrorMessage(err);
		}

	}

	private void showErrorMessage(String err) {
		// TODO Auto-generated method stub
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(err, buttonsNamewithType);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetView() {
//		searchForm.refresh();
	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	@Override
	public void renderTable(Page<PaymentReprocessSearchResultDTO> tableRows) {
		int iSlNo = 1;
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
			
			Boolean isRecordExceed = false;
			
			List<PaymentReprocessSearchResultDTO> pageItems = tableRows.getPageItems();
			for (PaymentReprocessSearchResultDTO paymentReprocessSearchResultDTO : pageItems) {
				if(paymentReprocessSearchResultDTO.getIsRecordExceed()){
					isRecordExceed = true;
					break;
				}else{
					break;
				}
			}
			
			if(! isRecordExceed){
				tableRows.getPageItems();		
				List<PaymentReprocessSearchResultDTO> listOfRecords = tableRows.getPageItems();
				if(null != listOfRecords && !listOfRecords.isEmpty())
				{				
					for (PaymentReprocessSearchResultDTO paymentReprocessSearchResultDTO : listOfRecords) {
						paymentReprocessSearchResultDTO.setSerialNo(iSlNo);
						iSlNo++;
					}
					
				}
				loadDataInWindow(tableRows);

			}


		} else {

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					
				}
			});
		}
	
		
	}

	private void loadDataInWindow(Page<PaymentReprocessSearchResultDTO> tableRows) {

		searchPopup.setWidth("97%");
		searchPopup.setHeight("97%");	
		searchPopup.setCaption("Search Result");
		if(SecondVL != null){
			SecondVL.removeAllComponents();
		}
		SecondVL = new VerticalLayout();

		PaymentReprocessTable.setTableList(tableRows.getPageItems());		
		PaymentReprocessTable.tablesize();

		List<PaymentReprocessSearchResultDTO> tableList = PaymentReprocessTable.getTableItems();	

		for (PaymentReprocessSearchResultDTO PaymentReprocessSearchResultDTO : tableList) {
			PaymentReprocessTable.setRowColor(PaymentReprocessSearchResultDTO);
		}
		//PaymentReprocessTable.setHasNextPage(false);
		PaymentReprocessTable.setPage(tableRows);
		SecondVL.addComponent(PaymentReprocessTable);


		//SecondVL.setSpacing(true);
		searchPopup.setContent(SecondVL);
		searchPopup.setClosable(true);
		searchPopup.center();
		searchPopup.setResizable(false);
		searchPopup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				PaymentReprocessTable.setIsSearchBtnClicked(false);

			}
		});
		searchPopup.setModal(true);
		UI.getCurrent().addWindow(searchPopup);
	
		

		
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
		secondLayout.addComponent(PaymentReprocessTable);

		return secondLayout;		
	}

	@Override
	public void buildSuccessLayout() {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Remarks Submitted Successfully", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(searchPopup);
				searchForm.resetAlltheValues();

			}
		});
		
	}

	@Override
	public void buildCancelLayout() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(searchPopup);
			}
			});
	}

	@Override
	public void showClaimsDMS(String url) {
		getUI().getPage().open(url, "_blank",1550,800,BorderStyle.NONE);
	
	}
		
	

}
