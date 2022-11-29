package com.shaic.reimbursement.billing.processClaimRequestBenefits;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessClaimRequestBenefitsView extends Searchable  {
	public void list(Page<SearchProcessClaimRequestBenefitsTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType);
	public void specialityList(
			BeanItemContainer<SelectValue> specialityValueByReference);
}
