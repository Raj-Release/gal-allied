package com.shaic.paclaim.convertClaimToReimb;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchConvertPAClaimTable extends
		GBaseTable<SearchConvertClaimTableDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] { "serialNumber", "intimationNo", "policyNo",
			"cpuCode", "accDeath", "insuredPatientName", "hospitalName",
			"hospitalType", "dateOfAdmission", "claimStatus" };

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
		
	}

	@Override
	public void tableSelectHandler(SearchConvertClaimTableDto t) {
		
		
		 VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			try{
				if(! isActiveHumanTask){
				
					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					fireViewEvent(PAMenuPresenter.SHOW_CONVERT_PA_CLAIM, t);
					
				}else{
					getErrorMessage("This record is already opened by another user");
				}
			}catch(Exception e){
				
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
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
