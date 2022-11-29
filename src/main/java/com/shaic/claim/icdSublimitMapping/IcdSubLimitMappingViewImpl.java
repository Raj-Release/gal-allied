package com.shaic.claim.icdSublimitMapping;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.SublimitFunObject;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.navigator.View;

@UIScoped
@CDIView(value = MenuItemBean.ICD_SUBLIMIT_MAPPING)
public class IcdSubLimitMappingViewImpl extends AbstractMVPView  implements IcdSubLimitMappingView, View{
	
	@Inject
    private IcdSubLimitMapping searchMapComponent;
	
	@PostConstruct
	public void initView(){
		
		setSizeFull();
		setVisible(true);
		searchMapComponent.initView();
		setSizeFull();
		setCompositionRoot(searchMapComponent);
	}	
	
	@Override
	public void resetView() {

		searchMapComponent.resetSearchForm();
	}
	
	@Override
	public void resetSearchView() {
		
		resetView();
		if(searchMapComponent.getSearchResultTable() != null){
			searchMapComponent.getSearchResultTable().removeRow();
		}
	}
	
	@Override
	public void setIcdBlockDroDownValues(BeanItemContainer<SelectValue> icdChapterContainer) {
		searchMapComponent.setIcdBlockDropDown(icdChapterContainer);
	}
	
	@Override
	public void showSubLimitName(SublimitFunObject sublimitObj){
		searchMapComponent.showSublimitName(sublimitObj);
	}
		
	@Override
	public void showMappingTable(List<IcdSublimitMappingDto> icdCodeSelectList) {
				
		searchMapComponent.showResultTable(icdCodeSelectList);
	}
	
	@Override
	public void showMappingTableWithMappedData(BeanItemContainer<SelectValue> icdChapterContainer, BeanItemContainer<SelectValue> icdBlockContainer, List<IcdSublimitMappingDto> icdCodeSelectList, boolean selectAll) {
		searchMapComponent.showResultTable(icdChapterContainer, icdBlockContainer, icdCodeSelectList, selectAll);
	}
	@Override
	public void showViewButtonClick(
			BeanItemContainer<SelectValue> icdChapterContainer) {
		searchMapComponent.setIcdChapter(icdChapterContainer);
		
	}
	
	@Override
	public void showSuccessLayout(String showMsg){
		searchMapComponent.showSuccessLayout(showMsg);
	}
	
	@Override
	public void showErroLayout(String errMsg){
		searchMapComponent.showErrorPopup(errMsg);
	}
		
	@Override
	public void showUnChecked(boolean unchecked){
		searchMapComponent.showUnchecked(unchecked);
	}
}
