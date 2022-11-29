package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PendingLotBatchReportDto;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface UpdatePaymentDetailView extends Searchable  {
	
	public void buildCreatePendingBatchLayout(List<PendingLotBatchReportDto> pendingList);
	public void list(Page<UpdatePaymentDetailTableDTO> tableRows);
	
	void init(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> paymentMode,BeanItemContainer<SelectValue> nonKeralacpuCode,BeanItemContainer<SelectValue> batchType,
			BeanItemContainer<SelectValue> zoneType,BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct);
	void getPenalInterestRate(CreateAndSearchLotTableDTO tableDto);
	void buildSuccessLayout(Map<String, Object> createLotMapper);
	void buildHoldPendingLayout(String layoutType,String batchType);
	void buildSearchBatchLayout(String layout); 

	void showClaimsDMS(String url);
	
	//void buildResultantTableLayout(String layoutType);

	void showDetails(List<CreateAndSearchLotTableDTO> tableDTOList,
			String batchNo);

	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView);
	
	void intrestRateValidation(); 
	void noOfExceedingDaysValidation();
	void repaintTable(String layout);
	void resetSlNo();
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO, UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO);
	public void setPaymentCpu(
			UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO);
	void buildSuccessLayout(String strRecMessage);
}
