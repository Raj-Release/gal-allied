package com.shaic.claim.pedrequest.teamlead.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchPEDRequestTeamLeadTable extends
		GBaseTable<SearchPEDRequestApproveTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalAddress", "hospitalCity",
			"strPedInitiated","pedSuggestion", "pedStatus","renewalDue" };
*/
	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		//setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchPEDRequestApproveTableDTO>(
				SearchPEDRequestApproveTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalAddress", "hospitalCity",
			"strPedInitiated","pedSuggestion", "pedStatus","renewalDue" };

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("290px");
		

	}

	@Override
	public void tableSelectHandler(SearchPEDRequestApproveTableDTO t) {
		
		
	/*	VaadinSession session = getSession();
		
		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);*/
		VaadinSession session = getSession();
//		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		
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
				fireViewEvent(MenuPresenter.PED_REQUEST_PAGE_TL_APPROVED, t);
				
			}else{
				getErrorMessage("This record is already opened by another user");
			}
		}catch(Exception e){
			
			/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);*/
			Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
			SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
			dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
			e.printStackTrace();
		}
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-pedrequestapprove-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
//		if(length>=5){
//			table.setPageLength(4);
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
