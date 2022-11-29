package com.shaic.paclaim.health.reimbursement.medicalapproval.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface PAHealthSearchProcessClaimRequestView extends Searchable  {
	public void list(Page<PAHealthSearchProcessClaimRequestTableDTO> tableRows);
	
	public void specialityList(
			BeanItemContainer<SelectValue> specialityValueByReference);
	

	public void init(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType,
			BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage,
			String screenName);
}
