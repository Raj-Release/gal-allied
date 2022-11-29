package com.shaic.paclaim.registration;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PAClaimRegistrationSearchTable extends GBaseTable<SearchClaimRegistrationTableDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean isCheckStaus = false;
	
	public static final Object[] PA_CLAIM_COLUMN_HEADER = new Object[] {"serialNumber",
		"intimationNumber","policyNo","cpuCode","insuredPatientName","hospitalName","hospitalType",
		"dateOfAdmissionValue","accidentOrDeath"
	};
	
	@Override
	public void removeRow() {
				
	}

	@Override
	public void initTable() {
		
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchClaimRegistrationTableDto>(SearchClaimRegistrationTableDto.class));
		table.setVisibleColumns(PA_CLAIM_COLUMN_HEADER);
	}
	
/*	public void alertMessage(final SearchClaimRegistrationTableDto t, String message) {

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
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			this.isCheckStaus = true;
			homeButton.setData(dialog);
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					
//					fireViewEvent(MenuItemBean.PA_CLAIM_REGISTER,null);
					fireViewEvent(SearchPAClaimRegistrationPresenter.CREATE_PA_CLAIM_REGISTRATION, t);
				}
			});
		}*/
	
	@Override
	public void tableSelectHandler(SearchClaimRegistrationTableDto t) {
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		String lob = "";
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
/*			String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo());
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				alertMessage(t, errorMessage);
			} else if(get64vbStatus != null && SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Pending";
				alertMessage(t, errorMessage);
			} else {*/
				
				VaadinSession session = getSession();
//				Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
				if(null != session){
				Boolean isActiveHumanTask = false;
				
				Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				lob = (String) wrkFlowMap.get(SHAConstants.LOB);
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
						if((SHAConstants.PA_LOB).equalsIgnoreCase(lob)){
						fireViewEvent(SearchPAClaimRegistrationPresenter.CREATE_PA_CLAIM_REGISTRATION, t);
						}
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
			//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			Boolean isActiveHumanTask = false;
			if(null != session){
			Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
			lob = (String) wrkFlowMap.get(SHAConstants.LOB);
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
					if((SHAConstants.PA_LOB).equalsIgnoreCase(lob)){
					fireViewEvent(SearchPAClaimRegistrationPresenter.CREATE_PA_CLAIM_REGISTRATION, t);
					}
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
	public String textBundlePrefixString() {
		return "searchPAClaimRegistration-";
	}
	
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
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
