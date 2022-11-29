package com.shaic.claim.ped.outsideprocess;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface InitiatePedView extends GMVPView {

	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);

	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);

	void setPEDCode(String pedCode);

	void buildSuccessLayout();

	public void buildInitiateLumenRequest(String intimationId);
	
	public void showPEDAlreadyAvailable(String pedAvailableMsg);
	
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO);

}
