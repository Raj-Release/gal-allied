package com.shaic.claim.fieldvisit.search;

import java.util.List;
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

public class SearchFieldVisitTable extends GBaseTable<SearchFieldVisitTableDTO> {

	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "policyNo", "insuredPatiendName", "intimaterName", "hospitalName", "intimationMode", "intimatedBy", 
			"strDateOfAdmission", "admissionType", "strDateOfIntimation", "callerMobileNumber", "reasonForAdmission", "strCpuCode","strFvrCpuCode"};

	@Override
	public void removeRow() {
		
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchFieldVisitTableDTO>(
				SearchFieldVisitTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("320px");
	}

	@Override
	public void tableSelectHandler(SearchFieldVisitTableDTO t) {
		//TODO
		//fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM, t.getKey());
		
		/* VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);*/
		 VaadinSession session = getSession();
//			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			
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
					fireViewEvent(MenuPresenter.PROCESS_FIELD_VISIT , t);
				}else{
					getErrorMessage("This record is already opened by another user");
				}
			}catch(Exception e){
				
				/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				e.printStackTrace();*/
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		
		
		
	}

	@Override
	public String textBundlePrefixString() {
//		return "search-fieldvisit-";
		
		return "search-process-field-visit-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		table.setPageLength(4);
		int length =table.getPageLength();
		if(length>=5){
//			
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
	 
	    public List<SearchFieldVisitTableDTO> getValues(){
	    	return (List<SearchFieldVisitTableDTO>) table.getItemIds();
	    }
	
	 
}
