package com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAReOpenRodLevelClaimView extends GMVPView {

	public void init(PASearchReOpenClaimRodLevelTableDTO searchDTO);

	public void setUpReference(PAReOpenRodLevelClaimDTO reopenClaimDTO, BeanItemContainer<SelectValue> reasonForReOpen);

	public void buildSuccessLayout();

	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails);

}
