package com.shaic.claim.preauth.search;


import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.MasUserAutoAllocation;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchPreauthView extends GMVPView {
	public void changeView(SearchPreauthFormDTO searchFormDTO, MasUserAutoAllocation user);
	  public void list(Page<SearchPreauthTableDTO> tableRows);
	  public void initSpeciality(BeanItemContainer<SelectValue> specialityTypeParameter);
	  public void init(BeanItemContainer<SelectValue> intimationSrcParameter,
			BeanItemContainer<SelectValue> networkHospTypeParameter,
			BeanItemContainer<SelectValue> treatmentTypeParameter,
			BeanItemContainer<SelectValue> preAuthTypeParameter,
			BeanItemContainer<SelectValue> specialityContainer, BeanItemContainer<SelectValue> cpuCodeContainer);
}
	