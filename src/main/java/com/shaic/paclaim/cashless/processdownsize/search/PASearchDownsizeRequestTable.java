package com.shaic.paclaim.cashless.processdownsize.search;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PASearchDownsizeRequestTable extends GBaseTable<PASearchWithdrawCashLessProcessTableDTO> {
	
	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo", "claimNo", "lob", "insuredPatientName",
		"diagnosis", "hospitalName", "hospitalAddress", "claimStatus" };
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PASearchWithdrawCashLessProcessTableDTO>(
				PASearchWithdrawCashLessProcessTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(PASearchWithdrawCashLessProcessTableDTO t) {
		//TODO
		Long key = t.getKey();
		Preauth preauthById = preauthService.getPreauthById(key);
//		List<Reimbursement> reimbursementByClaimKey = reimbursementService.getReimbursementByClaimKey(preauthById.getClaim().getKey());
		
		Boolean reimbursementStatusForDownsizeWithdraw = reimbursementService.getReimbursementStatusForDownsizeWithdraw(preauthById.getClaim().getKey());
		
		if(reimbursementStatusForDownsizeWithdraw){
			if(! preauthById.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)){
				
				    VaadinSession session = getSession();
					
					Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
					try{
						if(! isActiveHumanTask){
						
							SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					
							fireViewEvent(PAMenuPresenter.PA_DOWNSIZE_PREAUTH_REQUEST_PAGE_VIEW, t);
						}else{
							getErrorMessage("This record is already opened by another user");
						}
					}catch(Exception e){
						
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						e.printStackTrace();
					}

				
			}else{
				getErrorMessage("Withdraw is approved for this intimation");
			}
		}else{
			getErrorMessage("Downsize request cannot be processed, since an rod is already existing for this intimation");
		}
	}

	@Override
	public String textBundlePrefixString() {
		return "search-withdraw-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
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
