package com.shaic.claim.pedrequest.initiateped;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
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

public class SearchPEDInitiateTable extends
		GBaseTable<SearchPEDInitiateTableDTO> {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static final Object[] VISIBLE_COL_ORDER = new Object[] { 
			"serialNumber","policyNo","proposerCode", "proposerName","noOfInsured", "productName",
			"policyIssuingOfficeName", "periodOfInsurance", "premium" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		//setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchPEDInitiateTableDTO>(
				SearchPEDInitiateTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("290px");
		
		//table.setSizeUndefined();
	}

	@Override
	public void tableSelectHandler(SearchPEDInitiateTableDTO t) {
		//TODO
		
		VaadinSession session = getSession();
		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		
//		Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
//		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
//		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
//		String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//		DBCalculationService dbCalculationService = new DBCalculationService();
//		String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
//		String[] lockSplit = callLockProcedure.split(":");
//		String sucessMsg = lockSplit[0];
//		String userName = lockSplit[1];
//		
//		if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
//			isActiveHumanTask= true;
//		}
		try{			
			
		if(!isActiveHumanTask){
		
			SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
			//SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
			fireViewEvent(MenuPresenter.PED_INITIATE_OUTSIDE_PROCESS, t);
		}else{
			getErrorMessage("This record is already opened by another user");
	}
		}
		catch(Exception e){
			/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
		if(existingTaskNumber != null){
				BPMClientContext.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(), existingTaskNumber, SHAConstants.SYS_RELEASE);
			}
			System.out.println("Error is occured. Task is to be released");
			e.printStackTrace();*/
			
			/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
			SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
			dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);*/
			e.printStackTrace();
		}
		
                
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-pedrequestprocess-";
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


public void alertMessage(final SearchPEDInitiateTableDTO t, String message) {

		Label successLabel = new Label(
			"<b style = 'color: red;'>"+ message + "</b>",
			ContentMode.HTML);

	// Label noteLabel = new
	// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
	// ContentMode.HTML);

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
//	dialog.setCaption("Alert");
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

				//VaadinSession session = getSession();
				//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
				VaadinSession session = getSession();
				
				//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
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
				
				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}
				try{
					if( isActiveHumanTask){
						//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
						fireViewEvent(MenuItemBean.DRAFT_QUERY_LETTER_DETAIL, t);

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
	});
}
}
