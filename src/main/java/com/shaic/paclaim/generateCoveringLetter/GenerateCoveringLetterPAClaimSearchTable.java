package com.shaic.paclaim.generateCoveringLetter;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class GenerateCoveringLetterPAClaimSearchTable extends GBaseTable<GenerateCoveringLetterSearchTableDto>{

	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber",
		"intimationNo",
		"policyNo",
		"cpuCode",
		"insuredPatientName",
		"hospitalName",
		"hospitalType",		
		"admissionDate",
		"accedentDeath",
		"claimStatus"		
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
		table.setHeight("320px");

	}

	@Override
	public void tableSelectHandler(GenerateCoveringLetterSearchTableDto t) {
		
		VaadinSession session = getSession();
		//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		Boolean isActiveHumanTask = false;
		if(null != session){
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
				fireViewEvent(MenuItemBean.PA_COVERING_LETTER_DETAIL,t);
				
			}
			else{
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

	@Override
	public String textBundlePrefixString() {
		return "generate-pa-covering-letter-";
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
