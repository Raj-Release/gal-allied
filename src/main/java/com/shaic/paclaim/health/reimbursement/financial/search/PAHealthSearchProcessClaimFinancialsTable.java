package com.shaic.paclaim.health.reimbursement.financial.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.PAMenuPresenter;
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
public class PAHealthSearchProcessClaimFinancialsTable extends GBaseTable<PAHealthSearchProcessClaimFinancialsTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimedAmountAsPerBill", "strCpuCode","cpuName","claimType","documentReceivedFrom","productName",
		"insuredPatientName", "lOB",  "hospitalName",  "hospitalAddress", "reasonForAdmission", "type","originatorID","originatorName"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PAHealthSearchProcessClaimFinancialsTableDTO>(PAHealthSearchProcessClaimFinancialsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("hospitalAddress", 350);
		table.setHeight("250px");

	}
	
	public void alertMessage(final PAHealthSearchProcessClaimFinancialsTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setData(t);
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
							fireViewEvent(PAMenuPresenter.SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);

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
			PAHealthSearchProcessClaimFinancialsTableDTO t) {
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				alertMessage(t, errorMessage);
			} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
				errorMessage = "Cheque Status is Pending";
				alertMessage(t, errorMessage);
			} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Due";
				alertMessage(t, errorMessage);
			}else {
				
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
					fireViewEvent(PAMenuPresenter.SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
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
				fireViewEvent(PAMenuPresenter.SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
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
	
	//PAMenuPresenter.SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN, t

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
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

}
