package com.shaic.claim.OMPprocessrejection.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchOMPProcessRejectionDetailTable  extends GBaseTable<SearchProcessRejectionTableDTO>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4482345570354167308L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","policyNo","patientName","ailmentLoss",
		"admissionDate","eventCodeDescription" 
		}; 

	@Override
	public void removeRow() {
	
		table.removeAllItems();
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchProcessRejectionTableDTO>(
				SearchProcessRejectionTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void tableSelectHandler(SearchProcessRejectionTableDTO t) {

		   VaadinSession session = getSession();
		    Boolean isActiveHumanTask = false;
			Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);  
			
			String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
//			String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
			
			String callLockProcedure = dbCalculationService.callOMPLockProcedure(wrkFlowKey, currentqueue, userID);
			
			String[] lockSplit = callLockProcedure.split(":");
			String sucessMsg = lockSplit[0];
			String userName = lockSplit[1]; 
			
			if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
				isActiveHumanTask= true;
			}
			
			try{
				if(isActiveHumanTask){				
			
					fireViewEvent(OMPMenuPresenter.OMP_PROCESS_REJECTION_DETAIL_PAGE , t);
					
				}else{
					getErrorMessage(callLockProcedure);  
				}
			}catch(Exception e){
				
//				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.setActiveOrDeactiveOMPClaim(t.getWorkFlowKey(), session);
				
				e.printStackTrace();
			}
		
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ompprocessrejection-";
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
	
}
