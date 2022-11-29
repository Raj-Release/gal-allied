package com.shaic.paclaim.cashless.fle.search;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
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

public class PASearchPreMedicalProcessingEnhancementTable extends
		GBaseTable<PASearchPreMedicalProcessingEnhancementTableDTO> {
	
	
	private static final long serialVersionUID = 2020639264115682670L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "intimationSource", "cpuNAME", "productName",
			"insuredPatientName", "hospitalName", "networkHospitalType",
			"enhancementReqAmt", "balanceSI", "docsRecievedTime","type" };*/

	@Override
	public void removeRow() {
		table.removeAllItems();
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<PASearchPreMedicalProcessingEnhancementTableDTO>(
				PASearchPreMedicalProcessingEnhancementTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "intimationSource", "cpuNAME", "productName",
			"insuredPatientName", "hospitalName", "networkHospitalType",
			"enhancementReqAmt", "balanceSI", "docsRecievedTime","type" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("290px");
		

	}
	
	public void alertMessage(final PASearchPreMedicalProcessingEnhancementTableDTO t, String message) {

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
				
				VaadinSession session = getSession();
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				 
				Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);
				
				Boolean isActiveHumanTask = false;
				/*Getting WrkFlowTable obj from db*/
				Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				
				/*Validating for locked User*/
//				DBCalculationService dbCalculationService = new DBCalculationService();
				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];
				String userName = lockSplit[1];
				
				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}
				 dialog.close();
				 try{
						if(isActiveHumanTask){
//							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							fireViewEvent(PAMenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
						}else{
							getErrorMessage(callLockProcedure);
						}
			} catch(Exception e){
				e.printStackTrace();
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
			}
			}
		});
	}

	@Override
	public void tableSelectHandler(
			PASearchPreMedicalProcessingEnhancementTableDTO t) {
		
		Boolean isRodNotAvailable = true;
		
		List<Reimbursement> reimbursementDetails = masterService.getReimbursementDetails(t.getKey());
		
		for (Reimbursement reimbursement : reimbursementDetails) {
			
			if(! ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) 
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)){
				isRodNotAvailable = false;
			}else{
//				if(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)){
//					isRodNotAvailable = false;
//				}
			}
		}
		
		Preauth preauth = preauthService.getLatestPreauthDetails(t.getKey());
		
		if(preauth != null && preauth.getClaim() != null && preauth.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			getErrorMessage("Enhancement is not applicable. Since claim type is Reimbursement.");
		}else if(preauth != null){
			
			if(isRodNotAvailable){
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
				String userName = lockSplit[1];
				
				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}
				
				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
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

						try{
							if(isActiveHumanTask){
								SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
								fireViewEvent(PAMenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
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
				} else {
		
					try{
						if(isActiveHumanTask){
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							fireViewEvent(PAMenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
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
		    }else{
		    	getErrorMessage("Enhancement is not applicable, since ROD is available for this Claim");
		    }
		}else{
			getErrorMessage("Preauth is Witdrawn. Hence First Level Enhancement is not possible");
		}

	}

	@Override
	public String textBundlePrefixString() {
		return "search-premedicalprocessingenhancement-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
//		int length =table.getPageLength();
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