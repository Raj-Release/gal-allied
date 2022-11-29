package com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthReOpenRodLevelClaimView extends GMVPView {

	public void init(PAHealthSearchReOpenClaimRodLevelTableDTO searchDTO);

	public void setUpReference(PAHealthReOpenRodLevelClaimDTO reopenClaimDTO, BeanItemContainer<SelectValue> reasonForReOpen);

	public void buildSuccessLayout();

	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails);

}
