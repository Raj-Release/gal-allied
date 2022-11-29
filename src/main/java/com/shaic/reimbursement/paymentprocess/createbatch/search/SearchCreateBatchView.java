package com.shaic.reimbursement.paymentprocess.createbatch.search;

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
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;


/**
 * @author ntv.narenj
 *
 */
public interface SearchCreateBatchView extends Searchable  {
	
	public void buildCreatePendingBatchLayout(List<PendingLotBatchReportDto> pendingList);
	public void list(Page<CreateAndSearchLotTableDTO> tableRows);
	
	void init(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> paymentMode,BeanItemContainer<SelectValue> nonKeralacpuCode,BeanItemContainer<SelectValue> batchType,
			BeanItemContainer<SelectValue> zoneType,BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,BeanItemContainer<SelectValue> paymentType,BeanItemContainer<SelectValue> penalDueDays,BeanItemContainer<SelectValue> selectValueContainerForVerificationType,String presenterString);
	void getPenalInterestRate(CreateAndSearchLotTableDTO tableDto);
	void buildSuccessLayout(Map<String, Object> createLotMapper, Window popUp,SearchCreateBatchFormDTO searchDto);
	
	void buildHoldPendingLayout(String layoutType,String batchType);
	void buildSearchBatchLayout(String layout); 

	void showClaimsDMS(String url);
	
	//void buildResultantTableLayout(String layoutType);

	void showDetails(List<CreateAndSearchLotTableDTO> tableDTOList,
			String batchNo);

	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			CreateAndSearchLotTableDTO editPaymentView);
	
	void intrestRateValidation(); 
	void noOfExceedingDaysValidation();
	void repaintTable(String layout);
	void resetSlNo();
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);
	public void setPaymentCpu(
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);
	
	void buildSuccessLayout(String strRecMessage);
	
	public void listForQuick(Page<CreateAndSearchLotTableDTO> quickSearch);
	void resetTableView();
	
	public void setUpPaymentCpuCodeDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);
	public void setUpPayeeNameDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);
	public void setUpPayableDetails(String payableName,
			CreateAndSearchLotTableDTO tableDto);
	public void populatePreviousAccntDetails(CreateAndSearchLotTableDTO tableDto);
	public void prevsAcntDtlsAlert();
	//GALAXYMAIN-13257
	public void buildSuccessLayoutForHoldBancs(String presenter,Window popUp);
	//GALAXYMAIN-13257
	public void buildSuccessLayoutBancs(String presenter,Window popUp, Integer size); //GALAXYMAIN-13257
	public void LinkPayeeBankDetails(
			BankDetailsTableDTO viewSearchCriteriaDTO);
}
