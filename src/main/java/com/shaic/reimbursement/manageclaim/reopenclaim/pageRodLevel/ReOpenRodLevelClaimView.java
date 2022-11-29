package com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel.SearchReOpenClaimRodLevelTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ReOpenRodLevelClaimView extends GMVPView {

	public void init(SearchReOpenClaimRodLevelTableDTO searchDTO);

	public void setUpReference(ReOpenRodLevelClaimDTO reopenClaimDTO, BeanItemContainer<SelectValue> reasonForReOpen);

	public void buildSuccessLayout();

	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails);

}
