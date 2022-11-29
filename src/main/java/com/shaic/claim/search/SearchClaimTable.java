package com.shaic.claim.search;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SearchClaimTable extends
		GBaseTable<SearchClaimTableDTO> {
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber","viewClaimStatus",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalCity", "reasonforAdmission", "claimStatus" };*/
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@Inject
	private ViewDetails viewDetailsService;// = new ViewDetails(); 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchClaimTableDTO>(
				SearchClaimTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber","viewClaimStatus",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalCity", "reasonforAdmission", "claimStatus" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("290px");
//		table.setHeight("1000px");
		
		table.setColumnWidth("reasonforAdmission", 200);
		
		table.removeGeneratedColumn("viewClaimStatus");
		table.addGeneratedColumn("viewClaimStatus",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Claim Status");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								SearchClaimTableDTO searchClaimTableDTO = (SearchClaimTableDTO)itemId;
								
								if(searchClaimTableDTO.getClaimTypeId() != null && searchClaimTableDTO.getClaimTypeId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)
										|| searchClaimTableDTO.getClaimTypeId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
								
									Reimbursement reimbursement = reimbursementService.getLatestReimbursementDetails(searchClaimTableDTO.getKey());
									
									if(reimbursement != null){
									viewDetailsService.viewSearchClaimStatus(searchClaimTableDTO.getIntimationNo(),reimbursement.getKey());
									}
									else{
										viewDetailsService.viewSearchClaimStatus(searchClaimTableDTO.getIntimationNo(), null);
									}
									
								}else{
									viewDetailsService.getClaimStatus(searchClaimTableDTO.getIntimationNo());
								}
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	
	}

	@Override
	public void tableSelectHandler(SearchClaimTableDTO t) {
		System.out.println("---the key---"+t.getIntimationNo());
//		fireViewEvent(SearchClaimPresenter.SHOW_CLAIM_STATUS, t);
		// fireViewEvent(SearchEnhancementPresenter.SHOW_ENHANCEMENT_FORM, t);

	}

	@Override
	public String textBundlePrefixString() {
		return "search-claim-";
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
