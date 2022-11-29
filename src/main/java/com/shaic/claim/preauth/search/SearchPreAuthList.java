
package com.shaic.claim.preauth.search;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.TataPolicy;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.sun.jersey.api.client.WebResource.Builder;
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

@SuppressWarnings("serial")
public class SearchPreAuthList extends GBaseTable<SearchPreauthTableDTO> {
	
	@EJB
	private IntimationService intimationService;
	
	private final Logger log = LoggerFactory.getLogger(SearchPreAuthList.class);
	
	@EJB
	private PreauthService preauthService;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
		"intimationNo",
		"intimationSource",
		"cpuName",
		"productName",
		"insuredPatientName",
		"hospitalName",
		"networkHospType",
		"preAuthReqAmt",
		"claimedAmountAsPerBill",
		"treatmentType",
		"speciality",
		"balanceSI",
		"strDocReceivedTimeForReg",
		"strDocReceivedTimeForMatch",
		"type"
	};*/
	
	public static final Object[] NATURAL_COL_ORDER_CORP = new Object[] {"serialNumber","crmFlagged",
		"aboveCPULimitCorp",
		"intimationNo",
		"intimationSource",
		"cpuName",
		"productName",
		"insuredPatientName",
		"hospitalName",
		"networkHospType",
		"preAuthReqAmt",
		"claimedAmountAsPerBill",
		"treatmentType",
		"speciality",
		"balanceSI",
		"strDocReceivedTimeForReg",
		"strDocReceivedTimeForMatch",
		"type"
	};
	
	
	public static final Object[] NATURAL_COL_ORDER_CPU = new Object[] {"serialNumber","crmFlagged",
		"intimationNo",
		"intimationSource",
		"cpuName",
		"productName",
		"insuredPatientName",
		"hospitalName",
		"networkHospType",
		"preAuthReqAmt",
		"claimedAmountAsPerBill",
		"treatmentType",
		"speciality",
		"balanceSI",
		"strDocReceivedTimeForReg",
		"strDocReceivedTimeForMatch",
		"adviseStatus",
		"type"
	};
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchPreauthTableDTO>(SearchPreauthTableDTO.class));
		//table.setPageLength(table.size());
	Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","crmFlagged",
			"intimationNo",
			"intimationSource",
			"cpuName",
			"productName",
			"insuredPatientName",
			"hospitalName",
			"networkHospType",
			"preAuthReqAmt",
			"claimedAmountAsPerBill",
			"treatmentType",
			"speciality",
			"balanceSI",
			"strDocReceivedTimeForReg",
			"strDocReceivedTimeForMatch",
			"type"
		};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("295px");
	}

	public void setCorpView(){
		table.setVisibleColumns(NATURAL_COL_ORDER_CORP);
		table.setColumnHeader("aboveCPULimitCorp", "Above CPU limit - Corp Processing/Advise");
	}
	public void setCpuView(){
		table.setVisibleColumns(NATURAL_COL_ORDER_CPU);
		table.setColumnHeader("adviseStatus", "Advise Status");
	}

	public void alertMessage(final SearchPreauthTableDTO t, String message) {/*

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

		Button homeButton = new Button("Ok");
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
				 
					
				 VaadinSession session = getSession();
				 
				 DBCalculationService dbCalculationService = new DBCalculationService();
				 
				 	Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				 	log.info("Current Work flow &&&&&&&&&&&&&&&&&&&&&" + lockedWorkFlowKey1);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);

				    Boolean isActiveHumanTask = false;
				    
					Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
					Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
					
					String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//					DBCalculationService dbCalculationService = new DBCalculationService();
					String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
					String[] lockSplit = callLockProcedure.split(":");
					String sucessMsg = lockSplit[0];
					String userName = lockSplit[1];
					
					if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
						isActiveHumanTask= true;
					}
					
				    Date date4 = new Date();
					Date date5 = new Date();
					log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS ACTIVE HUMAN TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date4, date5));
					try{
						if(isActiveHumanTask){
							Date date6 = new Date();
							log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS SET ACTIVE OR DEACTIVE CLAIM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date6, new Date()));
							
							ImsUser imsUser = t.getImsUser();
							
							if(imsUser != null){
							
								String[] userRoleList = imsUser.getUserRoleList();
								
								
								WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
								
								if((Boolean)escalateValidation.get(SHAConstants.CMA6)){
									t.setCMA6(true);
								}else if((Boolean)escalateValidation.get(SHAConstants.CMA5)){
									t.setCMA5(true);
								}
								else if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
										t.setCMA4(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
										t.setCMA3(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
										t.setCMA2(true);
		
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
										t.setCMA1(true);
								}   
							}
							
//							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
						}else{
							getErrorMessage(callLockProcedure);
						}
					} catch(Exception e){
						
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						
						e.printStackTrace();
					}

//				 fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
			}
		});
	*/
		final MessageBox showAlert = showAlertMessageBox(message);
		Button homeButton = showAlert.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showAlert.close();
				 VaadinSession session = getSession();
				 DBCalculationService dbCalculationService = new DBCalculationService();
				 
				 	Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				 	log.info("Current Work flow &&&&&&&&&&&&&&&&&&&&&" + lockedWorkFlowKey1);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);

				    Boolean isActiveHumanTask = false;
				    
					Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
					Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
					Long cashlessKey = (Long) wrkFlowMap.get(SHAConstants.CASHLESS_KEY);
					
					String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//					DBCalculationService dbCalculationService = new DBCalculationService();
					String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
					String[] lockSplit = callLockProcedure.split(":");
					String sucessMsg = lockSplit[0];
					String userName = lockSplit[1];
					
					if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
						isActiveHumanTask= true;
					}
					
				    Date date4 = new Date();
					Date date5 = new Date();
					log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS ACTIVE HUMAN TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date4, date5));
					try{
						if(isActiveHumanTask){
							Date date6 = new Date();
							log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS SET ACTIVE OR DEACTIVE CLAIM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date6, new Date()));
							
							ImsUser imsUser = t.getImsUser();
							
							if(imsUser != null){
							
								String[] userRoleList = imsUser.getUserRoleList();
								
								
								WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
								
								if((Boolean)escalateValidation.get(SHAConstants.CMA6)){
									t.setCMA6(true);
								}else if((Boolean)escalateValidation.get(SHAConstants.CMA5)){
									t.setCMA5(true);
								}
								else if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
										t.setCMA4(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
										t.setCMA3(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
										t.setCMA2(true);
		
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
										t.setCMA1(true);
								}   
							}
							if(null != cashlessKey && !cashlessKey.equals(0l)){
								fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
							}
							else
							{
								fireViewEvent(MenuPresenter.SHOW_TATA_POLICY_PREAUTH_WIZARD, t);
							}
							
						}else{
							getErrorMessage(callLockProcedure);
						}
					} catch(Exception e){
						
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
	public void tableSelectHandler(SearchPreauthTableDTO t) {
		Date date = new Date();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		VaadinSession session = getSession();
		PremBonusDetails getBonusDetails = null;
	    Boolean isActiveHumanTask = false;
	    String chequeStatus = null;
		Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
		Long cashlessKey = (Long) wrkFlowMap.get(SHAConstants.CASHLESS_KEY);
		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
		String get64vbStatus = null;
		//Uncommented for FLP BY pass cases - 18-03-2020 as per Satish sir
		if(t.getProductKey() != null && ! ReferenceTable.getGMCProductList().containsKey(t.getProductKey())){
			getBonusDetails=PremiaService.getInstance().getBonusDetails(t.getPolicyNo());
			if(null != getBonusDetails) {
				intimationService.updateCumulativeBonusFromWebService(getBonusDetails);
				chequeStatus=getBonusDetails.getChequeStatus();
			}
		}
		
		String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		DBCalculationService dbCalculationService = new DBCalculationService();
		log.info("Current work flow key &&&&&&&&&&"+wrkFlowKey);
		String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
		String[] lockSplit = callLockProcedure.split(":");
		String sucessMsg = lockSplit[0];
		//String userName = lockSplit[1];
		if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
			isActiveHumanTask= true;
		}
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			Date date1 = new Date();
			if(t != null && t.getPolicyNo() != null){			
				Policy policyCheckQueue = intimationService.getPolicyByPolicyNubember(t.getPolicyNo());
				if(policyCheckQueue != null &&  policyCheckQueue.getChequeStatus() != null){
					get64vbStatus =  policyCheckQueue.getChequeStatus();
				}
				//Uncommented for FLP BY pass cases - 18-03-2020 as per Satish sir
				if(get64vbStatus == null || (!SHAConstants.REALISED.equalsIgnoreCase(get64vbStatus) && !SHAConstants.UNIQUE_64VB_COLLECTED.equalsIgnoreCase(get64vbStatus))){
					if(t.getProductKey() != null && ReferenceTable.getGMCProductList().containsKey(t.getProductKey())){
						get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
					}
					else{
						get64vbStatus = chequeStatus;
					}
				}
			}
			 
			Date date2 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED BY PREMIA SERVICE FOR GET 64VBSTATUS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date1, date2));
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
				alertMessage(t, errorMessage);
			} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
				errorMessage = "Cheque Status is Pending";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
				alertMessage(t, errorMessage);
			} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Due";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
				alertMessage(t, errorMessage);
			}else {
				
				    Date date4 = new Date();
					Date date5 = new Date();
					log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS ACTIVE HUMAN TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date4, date5));
					try{
						if(isActiveHumanTask){
							Date date6 = new Date();
							log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS SET ACTIVE OR DEACTIVE CLAIM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date6, new Date()));
							
							ImsUser imsUser = t.getImsUser();
							
							if(imsUser != null){
							
								String[] userRoleList = imsUser.getUserRoleList();
								
								
								WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
								
								if((Boolean)escalateValidation.get(SHAConstants.CMA6)){
									t.setCMA6(true);
								}else if((Boolean)escalateValidation.get(SHAConstants.CMA5)){
									t.setCMA5(true);
								}
								else if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
										t.setCMA4(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
										t.setCMA3(true);
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
										t.setCMA2(true);
		
								}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
										t.setCMA1(true);
								}   
							} 
							
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;							
							TataPolicy tataPolicy = preauthService.getTataPolicy(t.getPolicyNo());
							if(null != cashlessKey && !cashlessKey.equals(0l)){
								fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
							}
							else
							{
								fireViewEvent(MenuPresenter.SHOW_TATA_POLICY_PREAUTH_WIZARD, t);
							}
							
						//	fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
							log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED BY TABLE SELECT HANDLER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date, new Date()));
						}else{
							getErrorMessage(callLockProcedure);
						}
					} catch(Exception e){
						
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						
						e.printStackTrace();
					}
 
			}
		} else {
			
		    Date date4 = new Date();
			Date date5 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS ACTIVE HUMAN TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date4, date5));
			
			if(isActiveHumanTask){
					Date date6 = new Date();
					log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED IS SET ACTIVE OR DEACTIVE CLAIM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date6, new Date()));
					
					ImsUser imsUser = t.getImsUser();
					
					if(imsUser != null){
					
						String[] userRoleList = imsUser.getUserRoleList();
						
						
						WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
						
						if((Boolean)escalateValidation.get(SHAConstants.CMA6)){
							t.setCMA6(true);
						}else if((Boolean)escalateValidation.get(SHAConstants.CMA5)){
							t.setCMA5(true);
						}
						else if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
								t.setCMA4(true);
						}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
								t.setCMA3(true);
						}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
								t.setCMA2(true);

						}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
								t.setCMA1(true);
						}   
					}
					
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
					fireViewEvent(MenuPresenter.SHOW_PREATUH_WIZARD, t);
					//SHAUtils.setClearReferenceData(wrkFlowMap);
					log.info("%%%%%%%%%%%%%%%%%%%  TOTAL TIME CONSUMED BY TABLE SELECT HANDLER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ SHAUtils.getDurationFromTwoDate(date, new Date()));
				}else{
					getErrorMessage(callLockProcedure);
				}
		}
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-preauth-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
//		if(length>=7){
//			table.setPageLength(7);
//		}
		
	}
	
public void getErrorMessage(String eMsg){/*
		
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
	*/
	
	MessageBox.createError()
	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
    .withOkButton(ButtonOption.caption("OK")).open();
	}

public MessageBox showAlertMessageBox(String message){
	
	
	final MessageBox msgBox = MessageBox
		    .createWarning()
		    .withCaptionCust("Warning")
		    .withMessage(message)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
	return msgBox;
	
	
}


}
