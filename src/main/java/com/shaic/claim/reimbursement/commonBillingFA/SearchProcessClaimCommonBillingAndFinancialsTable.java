package com.shaic.claim.reimbursement.commonBillingFA;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;







import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimCommonBillingAndFinancialsTable extends GBaseTable<SearchProcessClaimFinancialsTableDTO>{

	@EJB
	private IntimationService intimationService;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","crmFlagged","intimationNo", "claimedAmountAsPerBill", "strCpuCode","cpuName","claimType","documentReceivedFrom","productName",
		"insuredPatientName", "lOB",  "hospitalName",  "hospitalAddress", "reasonForAdmission", "type","originatorID","originatorName"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchProcessClaimFinancialsTableDTO>(SearchProcessClaimFinancialsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("hospitalAddress", 350);
		table.setHeight("250px");

	}
	
	public void alertMessage(final SearchProcessClaimFinancialsTableDTO t, String message) {

   		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
					//dialog.close();

					//VaadinSession session = getSession();
					//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
					VaadinSession session = getSession();
					
					//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
					Boolean isActiveHumanTask = false;
					
					Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
					Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
					String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					DBCalculationService dbCalculationService = new DBCalculationService();
					String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
					String[] lockSplit = callLockProcedure.split(":");
					String sucessMsg = lockSplit[0];
					//String userName = lockSplit[1];
					
					if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
						isActiveHumanTask= true;
					}
					try{
						if( isActiveHumanTask){
							//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							t.setScreenName(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA);
							fireViewEvent(MenuPresenter.SHOW_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);

						}else{
							getErrorMessage("This record is already opened by another user");
						}
					}catch(Exception e){
						
						/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						e.printStackTrace();*/
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
	public void tableSelectHandler(
			SearchProcessClaimFinancialsTableDTO t) {
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
				
				//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
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
					t.setScreenName(SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA);
					fireViewEvent(MenuPresenter.SHOW_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
					}else{
						getErrorMessage("This record is already opened by another user");
					}
				
				}catch(Exception e){
					
					/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
					e.printStackTrace();*/
					Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
					SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
					e.printStackTrace();
				}
			}
		} else {
			
			VaadinSession session = getSession();
			
			//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
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
				
				/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				e.printStackTrace();*/
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-process-claim-financials-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
   public void getErrorMessage(String eMsg){

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

}
