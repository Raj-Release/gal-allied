package com.shaic.claim.search.specialist.search;

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

public class SubmitSpecialistTable extends
		GBaseTable<SubmitSpecialistTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
		"specialityType","dateOfRefer","doctorId","doctorName","doctorComments","intimationNo", "insuredPatientName","cpuCode","hospitalName","reasonForAdmission","claimStatus","referredBy", "policyNo"
		 };*/
	
	/*public static final Object[] VISIBLE_COLUMN = new Object[] {"serialNumber",
		"specialityType","dateOfRefer","doctorId","doctorName","doctorComments","intimationNo", "insuredPatientName","cpuCode","hospitalName","reasonForAdmission","claimStatus","referredBy", "policyNo"
		 };*/

	@Override
	public void removeRow() {
		table.removeAllItems();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SubmitSpecialistTableDTO>(
				SubmitSpecialistTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", "crmFlagged",
			"specialityType","dateOfRefer","doctorId","doctorName","doctorComments","intimationNo", "insuredPatientName","cpuCode","hospitalName","reasonForAdmission","claimStatus","referredBy", "policyNo"
			 };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("250px");
	

	}
	@Override
	public void tableSelectHandler(SubmitSpecialistTableDTO t) {

		//TODO
//		if(! t.getIsReimburementFlag()){
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
			   
				if(!t.getIsReimburementFlag()){
				    fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST, t);
				}else{
					fireViewEvent(MenuPresenter.SHOW_SUBMIT_SPECIALIST_ADVISE, t);
				}
				
			}else{
				getErrorMessage(callLockProcedure);
			}
	      }catch(Exception e){
	    	  
	    	  Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
	    	  SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
	    	  Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
	    	  dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
	    	  e.printStackTrace();
	      }
//		}
	/*else{
	      
	      VaadinSession session = getSession();
			
		  Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
	      try{
		   if(! isActiveHumanTask){
			   
			   if(t.getTaskNumber() != null){
			
				SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
			   }
				System.out.println("Submit Specialist Advise page");
				if(!t.getIsReimburementFlag()){
				    fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST, t);
				}else{
					fireViewEvent(MenuPresenter.SHOW_SUBMIT_SPECIALIST_ADVISE, t);
				}
				
			}else{
				getErrorMessage("This record is already opened by another user");
			}
	      }catch(Exception e){
	    	  
	    	  Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
	    	  SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
	    	  e.printStackTrace();
	      }
		}*/


	}
	
	public void setVisibleColumns(){
		
	 Object[] VISIBLE_COLUMN = new Object[] {"serialNumber", "crmFlagged",
			"specialityType","dateOfRefer","doctorId","doctorName","doctorComments","intimationNo", "insuredPatientName","cpuCode","hospitalName","reasonForAdmission","claimStatus","referredBy", "policyNo"
			 };
		table.setVisibleColumns(VISIBLE_COLUMN);
		table.setColumnHeader("policyNo", "Policy Number");
		table.setColumnHeader("cpuCode", "CPU Code");
		table.setColumnHeader("hospitalName", "Hospital Name");
		table.setColumnHeader("reasonForAdmission", "Reason For Admission");
		table.setColumnHeader("specialityType", "Speciality Type");
		table.setColumnHeader("claimStatus", "Claim Status");	
		table.setColumnHeader("hospitalAddress", "Hospital Address");
		table.setColumnHeader("hospitalCity", "Hospital City");
		table.setColumnHeader("referredBy", "Employee ID");
		table.setColumnHeader("doctorName", "Doctor Name");
		table.setColumnHeader("doctorComments", "Doctor Comments");
		table.setColumnHeader("dateOfRefer", "Date of Refer");
	}
	
	public void setNaturalColumns(){
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", "crmFlagged",
			"specialityType","dateOfRefer","doctorId","doctorName","doctorComments","intimationNo", "insuredPatientName","cpuCode","hospitalName","reasonForAdmission","claimStatus","referredBy", "policyNo"
			 };
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public String textBundlePrefixString() {
		return "Submit-Specialist-Advise-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=4){
			table.setPageLength(4);
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
