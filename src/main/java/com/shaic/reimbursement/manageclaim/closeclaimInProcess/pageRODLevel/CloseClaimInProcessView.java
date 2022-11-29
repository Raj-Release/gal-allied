package com.shaic.reimbursement.manageclaim.closeclaimInProcess.pageRODLevel;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface CloseClaimInProcessView extends GMVPView {

	public void setUpReference(BeanItemContainer<SelectValue> reasonForClosing);

	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails);

	public void init(SearchCloseClaimTableDTORODLevel searchDTO);

	public void buildSuccessLayout();

}
