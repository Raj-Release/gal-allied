package com.shaic.reimbursement.investigation.draftinvestigation.search;

import java.util.Date;
import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchDraftInvestigationTable extends GBaseTable<SearchDraftInvestigationTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "crmFlagged", "intimationNo", "policyNo", 
		"insuredPatientName", "investigationApprovedDate", "hospitalName",  "hospitalCity", "lOB", "strCpuCode", "productName",  "reasonForAdmission"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchDraftInvestigationTableDTO>(SearchDraftInvestigationTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("");
	}

	@Override
	public void tableSelectHandler(
			SearchDraftInvestigationTableDTO t) {
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
		String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
		if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Dishonoured";
			getAlertMessageForDraftInvestigation(t,errorMessage);
		} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
			errorMessage = "Cheque Status is Pending";
			getAlertMessageForDraftInvestigation(t,errorMessage);
		} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Due";
			getAlertMessageForDraftInvestigation(t,errorMessage);
		}else  {
		
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
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
					fireViewEvent(MenuPresenter.DRAFT_INVESTIGATION, t);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-draft-investigation-";
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
	 public void getAlertMessageForDraftInvestigation(final SearchDraftInvestigationTableDTO t,String message) {
		  final SearchDraftInvestigationTableDTO assignInvestigationDto = t;
		  Label successLabel = new Label(
					"<b style = 'color: red;'>"+message+"</b>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setData(t);
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
						dialog.close();
						 Date date3 = new Date();
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
									SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
									fireViewEvent(MenuPresenter.DRAFT_INVESTIGATION, t);
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
			});
			
	  }	
}
