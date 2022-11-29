package com.shaic.claim.reimbursement.commonBillingFA;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
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

public class SearchProcessClaimCommonBillingAndFinancialsAutoAllocationViewImpl  extends AbstractMVPView implements SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView,Searchable, View{


	@Inject
	private SearchProcessClaimCommonBillingAndFinancialsAutoAllocationForm  searchForm;

	@Inject
	private SearchProcessClaimCommonBillingAndFinancialsHoldMonitoringTable billingHoldMonitoringTable;

	private final Logger log = LoggerFactory.getLogger(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationViewImpl.class);

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	@EJB
	private IntimationService intimationService;

	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		billingHoldMonitoringTable.init("CLAIMS ON HOLD", false, false);
		billingHoldMonitoringTable.addStyleName((ValoTheme.TABLE_COMPACT));

		mainPanel.setFirstComponent(searchForm);
		VerticalLayout vLayout = new VerticalLayout(billingHoldMonitoringTable);
		mainPanel.setSecondComponent(vLayout);
		mainPanel.setSplitPosition(23);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("550px");
		setCompositionRoot(mainPanel);

		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 

	}

	@Override
	public void doSearch() {
		SearchProcessClaimFinancialsTableDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = new Pageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationPresenter.COMMON_BILLING_FA_AUTO_SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
	
	}

	@Override
	public void manuallyDoSearchForCompletedCases(){
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		fireViewEvent(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationPresenter.SEARCH_FOR_HOLD_TABLE_LIST, userName,passWord);
		//doSearch();
	}
	
//	@Override
//	public void setValuesForCompletedCase(SearchPreauthTableDTO resultDto) {
//		searchForm.setCompletedCase(resultDto);
//	}
	
	@Override
	public void setHoldTableList(List<SearchHoldMonitorScreenTableDTO> resultDto) {
		billingHoldMonitoringTable.setTableList(resultDto);
		billingHoldMonitoringTable.getScreenName(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION);
	}

	public void getErrorMessage(String eMsg){

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	@Override
	public void manuallyDoSearch(){
		doSearch();
	}

//	@Override
//	public void resetSearchResultTableValues() {
//		searchResultTable.getPageable().setPageNumber(1);
//		searchResultTable.resetTable();
//		Iterator<Component> componentIter = mainPanel.getComponentIterator();
//		while(componentIter.hasNext())
//		{
//			Component comp = (Component)componentIter.next();
//			if(comp instanceof SearchProcessClaimCommonBillingAndFinancialsTable)
//			{
//				((SearchProcessClaimCommonBillingAndFinancialsTable) comp).removeRow();
//			}
//		}
//	}

	@Override
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows) {


		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()){	

			tableSelectHandler(tableRows.getPageItems().get(0));
		}else
		{	
			Label successLabel = new Label("<b style = 'color: black;'>No Claim Found in Common Billing & FA Queue for Allocation.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Common for Billing & FA- Auto Allocation Home");
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
					searchForm.refresh();							
				}
			});		
		}				

	}


	public void tableSelectHandler(SearchProcessClaimFinancialsTableDTO t) {
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";		
		PremBonusDetails getBonusDetails = null;
		String chequeStatus = null;
		String get64vbStatus = null;
		if(t.getProductKey() != null && ! ReferenceTable.getGMCProductList().containsKey(t.getProductKey())){
			getBonusDetails=PremiaService.getInstance().getBonusDetails(t.getPolicyNo());
			intimationService.updateCumulativeBonusFromWebService(getBonusDetails);
			if(getBonusDetails != null && getBonusDetails.getChequeStatus() != null  && ! getBonusDetails.getChequeStatus().isEmpty()){
				chequeStatus=getBonusDetails.getChequeStatus();
			}
		}
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			if(t.getProductKey() != null && ReferenceTable.getGMCProductList().containsKey(t.getProductKey())){
				get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
			}
			else{
				get64vbStatus = chequeStatus;
			}
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				alertMessage(t, errorMessage);
			} /*else if(get64vbStatus != null && SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus)) {
					errorMessage = "Cheque Status is Pending";
					alertMessage(t, errorMessage);
				}*/ else {

					VaadinSession session = getSession();

					Boolean isActiveHumanTask = false;

					Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
					Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
					String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					DBCalculationService dbCalculationService = new DBCalculationService();
					String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
					String[] lockSplit = callLockProcedure.split(":");
					String sucessMsg = lockSplit[0];
					String userName = lockSplit[1];

					if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
						isActiveHumanTask= true;
					}

					try{

						if(isActiveHumanTask){

							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							t.setScreenName(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION);
							fireViewEvent(MenuPresenter.SHOW_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
						}else{
							getErrorMessage("This record is already opened by another user");
						}

					}catch(Exception e){
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						e.printStackTrace();
					}
				}
		} else {

			VaadinSession session = getSession();

			Boolean isActiveHumanTask = false;

			Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
			String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
			String[] lockSplit = callLockProcedure.split(":");
			String sucessMsg = lockSplit[0];
			String userName = lockSplit[1];

			if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
				isActiveHumanTask= true;
			}
			try{
				if(isActiveHumanTask){

					//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
					fireViewEvent(MenuPresenter.SHOW_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
				}else{
					getErrorMessage("This record is already opened by another user");
				}
			}catch(Exception e){
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		}

	}

	@Override
	public void init() {

	}
	public void alertMessage(SearchProcessClaimFinancialsTableDTO t, String message) {

		final MessageBox msgBox = MessageBox
				.createWarning()
				.withCaptionCust("Warning")
				.withMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		Button homeButton=msgBox.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				VaadinSession session = getSession();

				DBCalculationService dbCalculationService = new DBCalculationService();

				Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);

				Boolean isActiveHumanTask = false;
				Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);

				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];

				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}

				try{
					if(isActiveHumanTask){
						fireViewEvent(MenuPresenter.SHOW_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
					}else{
						getErrorMessage(callLockProcedure);
					}
				}catch(Exception e){

					Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
					SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
					e.printStackTrace();
				}
			}
		});		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	
}


