package com.shaic.paclaim.manageclaim.closeclaim.pageRodLevel;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PACloseClaimRodLevelView extends GMVPView {

	void init(PASearchCloseClaimTableDTORODLevel searchDTO);

	void setUpReference(BeanItemContainer<SelectValue> reasonForClosing);

	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails);

	public void buildSuccessLayout();
	
}
