package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.RevisedMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;



public class SearchProcessICACTable extends GBaseTable<SearchProcessICACTableDTO> {

@EJB
private ProcessICACService processICACService; 
	
	
	private static final long serialVersionUID = 1L;
	
	public static final Object[] VISIBLE_COL_ORDER = new Object[] {"serialNumber","crmFlagged",
		"intimationNo",
		"intimationSource",
		"cpuName",
		"productName",
		"insuredPatientName",
		"hospitalName",
		"networkHospType",
		"preAuthReqAmt",
		"claimedAmountAsPerBill",
		"treatmentType",
		"speciality",
		"balanceSI",
		"strDocReceivedTimeForReg",
		"strDocReceivedTimeForMatch",
		/*"adviseStatus",*/
		"type"
	};
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchProcessICACTableDTO>(SearchProcessICACTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);

		table.setHeight("295px");
		
	}
	
	@Override
	public void tableSelectHandler(SearchProcessICACTableDTO t) {
		// need to be done
		if(t != null){
			if(t.getDirectIcacReq().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				List<String> sourceList = new ArrayList<String>();
				sourceList.add(SHAConstants.PRE_AUTH);
				sourceList.add(SHAConstants.PRE_AUTH_ENHANCEMENT);
				sourceList.add(SHAConstants.MA);
				Boolean allowed  = processICACService.getTocheckIcacrDirestcase(t.getIntimationNo(),sourceList);

				if(allowed){
					fireViewEvent(RevisedMenuPresenter.ICAC_REQUEST_WIZARD_VIEW,t);
				}
				else{
					SHAUtils.showMessageBoxWithCaption(SHAConstants.ICAC_PROCESS_ALRDY_INIT,"Information");
				}
			}else{
				fireViewEvent(RevisedMenuPresenter.ICAC_REQUEST_WIZARD_VIEW,t);

			}
		}
	}
		

	@Override
	public String textBundlePrefixString() {
		return "search-icac-";
	}
	@SuppressWarnings("deprecation")
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		/*int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}*/
		
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
