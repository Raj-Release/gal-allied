
package com.shaic.claim.process64VB.search;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
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
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchProcessVBList extends GBaseTable<SearchPreauthTableDTO> {
	
	private final Logger log = LoggerFactory.getLogger(SearchProcessVBList.class);

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
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchPreauthTableDTO>(SearchPreauthTableDTO.class));
		//table.setPageLength(table.size());
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
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

	
	
	public void alertMessage(final SearchPreauthTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

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
				 
					
				 VaadinSession session = getSession();
				 
				 DBCalculationService dbCalculationService = new DBCalculationService();
				 
				 	Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
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
							fireViewEvent(MenuPresenter.SHOW_PROCESS_64_WIZARD, t);
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
	}
	
	
	@Override
	public void tableSelectHandler(SearchPreauthTableDTO t) {
		Date date = new Date();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
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
		//String userName = lockSplit[1];
		
		//if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
			isActiveHumanTask= true;
		//}
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			Date date1 = new Date();
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
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
							fireViewEvent(MenuPresenter.SHOW_PROCESS_64_WIZARD, t);
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
					fireViewEvent(MenuPresenter.SHOW_PROCESS_64_WIZARD, t);
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
