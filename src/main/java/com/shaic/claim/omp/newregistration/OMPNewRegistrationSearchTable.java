package com.shaic.claim.omp.newregistration;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.Policy;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

@SuppressWarnings("serial")
public class OMPNewRegistrationSearchTable extends GBaseTable<OMPNewRegistrationSearchDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"snoTbl","intimationNoTbl","policyNoTbl","intimationDateTblStr","insuredNameTbl","productCodeTbl",
		"sumInsuredTbl","planTbl","eventCodeTbl","claimStatusTbl"}; 
	
	@EJB
	private PremiaPullService premiaPullService;

	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public Table getTable(){
		return table;
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OMPNewRegistrationSearchDTO>(OMPNewRegistrationSearchDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setHeight("240px");
	}

	 protected void tablesize(){
		 table.setPageLength(table.size()+1);
		 int length =table.getPageLength();
		 if(length>=10){
			 table.setPageLength(11);
		 }
	 }
	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}
	
	@Override
	public void tableSelectHandler(OMPNewRegistrationSearchDTO t) {
		OMPNewRegistrationSearchDTO intimationDto = new OMPNewRegistrationSearchDTO();
		intimationDto.setIntimationKey(t.getIntimationKey());
		intimationDto.setIntimationno(t.getIntimationNoTbl());
		intimationDto.setWfKey(t.getWfKey());
		String policyNumber = t.getPolicyNoTbl();
		Policy policyRecord = checkPolicyNumber(policyNumber);
		if(policyRecord == null){
			PremPolicyDetails policyDetails = premiaPullService.fetchOMPPolicyDetailsFromPremia(policyNumber);
			policyRecord = premiaPullService.populatePolicyToAddOMPIntimation(policyDetails);
		}
		intimationDto.setPolicy(policyRecord);
		policyRecord.setInsured(premiaPullService.getInsuredListByPolicyNo(policyRecord.getPolicyNumber()));
		intimationDto.setPolicy(policyRecord);
		
		fireViewEvent(OMPMenuPresenter.OMP_REG_DETAIL_PAGE, intimationDto);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "ompnewregsearch-";
	}

	public Policy checkPolicyNumber(String policyNumber){
		return premiaPullService.getPolicyByPolicyNubember(policyNumber);
	}
}
