package com.shaic.reimbursement.processi_investigationi_initiated.search;

import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.InvestigationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessInvestigationInitiatedTable extends GBaseTable<SearchProcessInvestigationInitiatedTableDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(SearchProcessInvestigationInitiatedTable.class);
	
	@EJB
	public InvestigationService investigationService; 

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "crmFlagged", "intimationNo", "policyNo", "insuredPatientName",
		"hospitalName",  "hospitalCity", "hospitalType", "networkHospType", "lOB", "strCpuCode", "productName",  "reasonForAdmission","investigationRequestedRole","dtofInvInitiated"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchProcessInvestigationInitiatedTableDTO>(SearchProcessInvestigationInitiatedTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("288px");
	}

	@Override
	public void tableSelectHandler(SearchProcessInvestigationInitiatedTableDTO t) {
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
		String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
		if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Dishonoured";
			getAlertMessageForInvestigation(t,errorMessage,true);
		} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
			errorMessage = "Cheque Status is Pending";
			getAlertMessageForInvestigation(t,errorMessage,true);
		} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Due";
			getAlertMessageForInvestigation(t,errorMessage,true);
		}else  {
		
		  VaadinSession session = getSession();
		  
		Boolean isActiveHumanTask = false;
		    
		 	Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);	
			Long claimKey = (Long) wrkFlowMap.get(SHAConstants.DB_CLAIM_KEY);
			Long currerntInvestigationKey = (Long) wrkFlowMap.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
			
			
			String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
			String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
			String[] lockSplit = callLockProcedure.split(":");
			String sucessMsg = lockSplit[0];
			String userName = lockSplit[1];
			
			if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
				isActiveHumanTask= true;
			}
			
//			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			try{
				if(isActiveHumanTask){
				
					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					if(null != currerntInvestigationKey)
					{
					Boolean investigationAvailable = investigationService.getInvestigationByExcludingCurrentInvestigation(claimKey,currerntInvestigationKey);
					
					t.setIsInvestigation(investigationAvailable);
					}	
					else
					{
						t.setIsInvestigation(false);
					}
					
					if(! t.getIsInvestigation())
					{			
					fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, t);
				}else{	
				    String msg = "Investigation Request has already been initiated for this Claim once.";
				    getAlertMessageForMultipleInvestigation(t,msg);
				}
				}
				else{
						getErrorMessage(callLockProcedure);
					}
				
			}catch(Exception e){
				
//				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
//				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				
				e.printStackTrace();
			}
		}
		
	}
	}
	@Override
	public String textBundlePrefixString() {
		
		return "search-process-investigation-initiated-";
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

	  public void getAlertMessageForInvestigation(final SearchProcessInvestigationInitiatedTableDTO t,String message,final Boolean is64VbAlert) {
		  final SearchProcessInvestigationInitiatedTableDTO processInvestigationDto = t;
	   		/*Label successLabel = new Label(
					"<b style = 'color: red;'>Investigation Request has already been initiated for this Claim once.</b>",
					ContentMode.HTML);*/
		  Label successLabel = new Label(
					"<b style = 'color: red;'>"+message+"</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setData(t);
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

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
					//fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, processInvestigationDto);
					//bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(! is64VbAlert){
						 Date date3 = new Date();
							VaadinSession session = getSession();
							Date date4 = new Date();
						  
						  Boolean isActiveHumanTask = false;
						    
							Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
							Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
							String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);	
							Long claimKey = (Long) wrkFlowMap.get(SHAConstants.DB_CLAIM_KEY);
							Long currerntInvestigationKey = (Long) wrkFlowMap.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
							
							
							String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							DBCalculationService dbCalculationService = new DBCalculationService();
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
							String[] lockSplit = callLockProcedure.split(":");
							String sucessMsg = lockSplit[0];
							String userName = lockSplit[1];
							
							if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
								isActiveHumanTask= true;
							}
							
						  
							Date date5 = new Date();
							log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS ACTIVE HUMAN TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date4, date5));
//							Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
							try{
								if(isActiveHumanTask){
									Date date6 = new Date();
									//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
									if(null != currerntInvestigationKey)
									{
									Boolean investigationAvailable = investigationService.getInvestigationByExcludingCurrentInvestigation(claimKey,currerntInvestigationKey);
									
									t.setIsInvestigation(investigationAvailable);
									}	
									else
									{
										t.setIsInvestigation(false);
									}
									
									if(! t.getIsInvestigation())
									{			
									fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, t);
								}else{	
								
									String msg = "Investigation Request has already been initiated for this Claim once.";
									getAlertMessageForMultipleInvestigation(t,msg);
								}
								}
								else{
										getErrorMessage(callLockProcedure);
									}
								
							}catch(Exception e){
								
//								Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
//								SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
								
								Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
								Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
								SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
								dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
								
								e.printStackTrace();
				}
				}else{
					fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, t);
				}
				}
			});
			
	  }	
	  
	  
	  public void getAlertMessageForMultipleInvestigation(final SearchProcessInvestigationInitiatedTableDTO t,String message) {
		  final SearchProcessInvestigationInitiatedTableDTO processInvestigationDto = t;
	   		/*Label successLabel = new Label(
					"<b style = 'color: red;'>Investigation Request has already been initiated for this Claim once.</b>",
					ContentMode.HTML);*/
		  Label successLabel = new Label(
					"<b style = 'color: red;'>"+message+"</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setData(t);
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

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
					//fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, processInvestigationDto);
					//bean.setIsPopupMessageOpened(true);
					dialog.close();
					fireViewEvent(MenuPresenter.PROCESS_INVESTIGATION_INITIATED, t);
				}
			});
			
	  }	
	
}