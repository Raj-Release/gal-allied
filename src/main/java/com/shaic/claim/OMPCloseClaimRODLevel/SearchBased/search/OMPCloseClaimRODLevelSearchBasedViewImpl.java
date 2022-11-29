package com.shaic.claim.OMPCloseClaimRODLevel.SearchBased.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;

public class OMPCloseClaimRODLevelSearchBasedViewImpl extends AbstractMVPView implements OMPCloseClaimRODLevelSearchBasedView{
	
	@Inject
	private OMPCloseClaimRODLevelSearchBasedUI searchForm;
		
	private OMPCloseClaimRODLevelSearchBasedFormDto searchDto;
	@Inject
	private OMPCloseClaimRODLevelSearchBasedDetailTable searchResultTable;
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, true);
		
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(46);
		setHeight("550px");
	//	mainPanel.setHeight("630px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		searchResultTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof OMPCloseClaimRODLevelSearchBasedDetailTable)
			{
				((OMPCloseClaimRODLevelSearchBasedDetailTable) comp).removeRow();
			}
		}
	
		
	}
	
	@Override
	public void list(Page<OMPCloseClaimRODLevelSearchBasedFormDto> tableRows) {
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}
	@Override
	public void doSearch() {
		OMPCloseClaimRODLevelSearchBasedFormDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(OMPCloseClaimRODLevelSearchBasedPresenter.SUBMIT_SEARCH, searchDTO,userName,passWord);
		
	}
	@Override
	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
//	searchForm.setType(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		

	}
}
