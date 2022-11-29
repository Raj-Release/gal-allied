package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;

public interface CreateAndSearchLotView extends Searchable {

	void list(Page<CreateAndSearchLotTableDTO> tableRows);

	void init(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> paymentStatus , BeanItemContainer<SpecialSelectValue> product
			,BeanItemContainer<SelectValue> docVerified,BeanItemContainer<SelectValue> paymentMode,BeanItemContainer<SelectValue> selectValueContainerForVerificationType);
	
	void buildSuccessLayout(Map<String, Object> createLotMapper,Window popUp);
	
	void buildResultantTableLayout(String layoutType);
	
	void showClaimsDMS(String url);
	


	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView);

	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);

	void setPaymentCpu(CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);

	void buildSuccessLayout(String strRecMessage);

	void listForQuick(Page<CreateAndSearchLotTableDTO> search);

	void setUpPaymentCpuCodeDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);

	void setUpPayeeNameDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO);

	void setUpPayableDetails(String payableName, CreateAndSearchLotTableDTO tableDto);

	//void buildPaymentDetailsSuccessLayout(Boolean isSuccess);
	
	


	
}
 

