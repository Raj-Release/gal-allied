package com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation;

import java.util.HashMap;
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
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
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
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchProcessFinancialApprovalAutoAllocationViewImpl  extends AbstractMVPView implements SearchProcessFinancialApprovalAutoAllocationView,Searchable, View{


	@Inject
	private SearchProcessFinancialApprovalAutoAllocationForm  searchForm;

	@Inject
	private SearchProcessFinancialApprovalAutoHoldMonitoringTable FAautoHoldMonitoringTable;

	private final Logger log = LoggerFactory.getLogger(SearchProcessFinancialApprovalAutoAllocationViewImpl.class);

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;

	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		FAautoHoldMonitoringTable.init("CLAIMS ON HOLD", false, false);
		FAautoHoldMonitoringTable.addStyleName((ValoTheme.TABLE_COMPACT));

		mainPanel.setFirstComponent(searchForm);
		VerticalLayout vLayout = new VerticalLayout(FAautoHoldMonitoringTable);
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
		searchDTO.setScreenName(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchProcessFinancialApprovalAutoAllocationPresenter.FINANCIALAPPROVAL_AUTO_SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
	
	}

	@Override
	public void manuallyDoSearchForCompletedCases(){
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		fireViewEvent(SearchProcessFinancialApprovalAutoAllocationPresenter.SEARCH_FOR_FA_HOLD_TABLE_LIST, userName,passWord);
	}
	
	
	@Override
	public void setHoldTableList(List<SearchHoldMonitorScreenTableDTO> resultDto) {
		FAautoHoldMonitoringTable.setTableList(resultDto);
		FAautoHoldMonitoringTable.getScreenName(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION);
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


	@Override
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows) {


		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()){	

			tableSelectHandler(tableRows.getPageItems().get(0));
		}else
		{	
			Label successLabel = new Label("<b style = 'color: black;'>No Claim Found in Process Claim Financials (Auto Allocation).</b>", ContentMode.HTML);			
			Button homeButton = new Button("Process Claim Financials (Auto Allocation) Home");
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
		if(t.getIntimationNo() !=null && 
				checkPCCRequestavalible(t.getIntimationNo())){
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
			}  else {

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
							t.setScreenName(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION);
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
						t.setScreenName(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION);
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
	
	private Boolean checkPCCRequestavalible(String intimationNO){
		   PCCRequest pccRequest=  pccRequestService.getPCCDetailsByInitmationNo(intimationNO);
		   if(pccRequest != null && pccRequest.getStatus() !=null 
				   && pccRequest.getStatus().getKey() !=null){
			   if(!pccRequest.getStatus().getKey().equals(SHAConstants.PCC_COORDINATOR_APPROVED_STATUS)){
				   getErrorMessage("PCC response yet to be received for this intimation  " +intimationNO );
				   return false;
			   }   
		   }
		   return true;
		   
	   }

	
}
