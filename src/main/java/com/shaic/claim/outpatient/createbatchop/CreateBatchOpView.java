package com.shaic.claim.outpatient.createbatchop;

import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface CreateBatchOpView extends Searchable{

	void list(Page<CreateBatchOpTableDTO> tableRows);

	void init(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> paymentStatus , BeanItemContainer<SpecialSelectValue> product
			,BeanItemContainer<SelectValue> docVerified,BeanItemContainer<SelectValue> paymentMode,BeanItemContainer<SelectValue> pioCode);
	
	void buildSuccessLayout(Map<String, Object> createLotMapper,Window popUp);
	
	void buildResultantTableLayout(SelectValue layoutType);
	
	void showClaimsDMS(String url);
	


	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView);

	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO);

	void setPaymentCpu(CreateBatchOpTableDTO updatePaymentDetailTableDTO);

	void buildSuccessLayout(String strRecMessage);

	void listForQuick(Page<CreateBatchOpTableDTO> search);

	void setUpPaymentCpuCodeDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO);

	void setUpPayeeNameDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO);

	void setUpPayableDetails(String payableName, CreateBatchOpTableDTO tableDto);

	//void buildPaymentDetailsSuccessLayout(Boolean isSuccess);
	
	


	
}
