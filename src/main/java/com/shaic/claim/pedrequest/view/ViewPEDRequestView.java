package com.shaic.claim.pedrequest.view;

import java.util.List;
import java.util.WeakHashMap;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ViewPEDRequestView extends GMVPView {

	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);

	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);

	//void setPEDCode(BeanItemContainer<SelectValue> icdCodeContainer);
	
	void setPEDCode(String pedCode);
	public void showPEDAlreadyAvailable(String pedAvailableMsg);

	void setEditDetailsForPED(OldInitiatePedEndorsement initiate,
			List<ViewPEDTableDTO> pedInitiateDetailsDTOList, Boolean isWatchList);
	
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO);
	
}
