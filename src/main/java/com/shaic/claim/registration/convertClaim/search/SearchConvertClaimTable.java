package com.shaic.claim.registration.convertClaim.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchConvertClaimTable extends
		GBaseTable<SearchConvertClaimTableDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] { "serialNumber", "crmFlagged",
			"lob", "claimType", "cpuCode", "claimNumber", "insuredPatientName",
			"hospitalName", "dateOfAdmission", "claimStatus" };

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchConvertClaimTableDto>(
				SearchConvertClaimTableDto.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("295px");
	}

	@Override
	public void tableSelectHandler(SearchConvertClaimTableDto t) {
		
		
		 VaadinSession session = getSession();
		 
		 Boolean isActiveHumanTask = false;
		    
			Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
			
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
			
			try{
				if(isActiveHumanTask){
				
					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, t);
					
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
		
		
		// fireViewEvent(MenuPresenter.SHOW_HOSPITAL, t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "convertClaimType-";
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
