package com.shaic.claim.icdSublimitMapping;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.SublimitFunObject;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface IcdSubLimitMappingView extends GMVPView {
	
	public void showSubLimitName(SublimitFunObject sublimitDto);
	public void showViewButtonClick(BeanItemContainer<SelectValue> icdChapterContainer);
	public void setIcdBlockDroDownValues(BeanItemContainer<SelectValue> icdBlockContainer);
	public void showMappingTable(List<IcdSublimitMappingDto> IcdCodeSelectList);
	public void showMappingTableWithMappedData(BeanItemContainer<SelectValue> icdChapterContainer, BeanItemContainer<SelectValue> icdBlockContainer, List<IcdSublimitMappingDto> icdCodeSelectList, boolean selectAll);
	public void resetSearchView();
	public void showSuccessLayout(String showMsg);
	public void showErroLayout(String errMsg);
	public void showUnChecked(boolean unchecked);
}
