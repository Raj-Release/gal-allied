package com.shaic.claim.registration;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class GenerateCoveringLetterSearchTable extends GBaseTable<GenerateCoveringLetterSearchTableDto>{

	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber", 
		"crmFlagged",
		"lob",
		"claimType",
		"cpuCode",
		"claimNumber",
		"insuredPatientName",
		"hospitalName",
		"admissionDate"
	};
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<GenerateCoveringLetterSearchTableDto>(GenerateCoveringLetterSearchTableDto.class));
		table.setVisibleColumns(COLUMN_HEADER);

	}

	@Override
	public void tableSelectHandler(GenerateCoveringLetterSearchTableDto t) {
		
		 VaadinSession session = getSession();
			Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
	
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
			
			if (getUI() != null && getUI().getSession() != null) {
			
			String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
			String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
			String[] lockSplit = callLockProcedure.split(":");
			String sucessMsg = lockSplit[0];
			String userName = lockSplit[1];
			Boolean isActiveHumanTask = false;
			if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
				isActiveHumanTask= true;
			}						
			
//			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			
			try{
			if(isActiveHumanTask){
			
				SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
				fireViewEvent(GenerateCoveringLetterPresenter.VIEW_CLAIM_COVERINGLETTER,t);
				
			}else{
				getErrorMessage(callLockProcedure);
//				getErrorMessage("This record is already opened by another user");
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

	@Override
	public String textBundlePrefixString() {
		return "generate-covering-letter-";
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
