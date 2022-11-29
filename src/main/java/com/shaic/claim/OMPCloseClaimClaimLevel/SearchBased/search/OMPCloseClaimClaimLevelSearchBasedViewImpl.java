package com.shaic.claim.OMPCloseClaimClaimLevel.SearchBased.search;

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

public class OMPCloseClaimClaimLevelSearchBasedViewImpl extends AbstractMVPView implements OMPCloseClaimClaimLevelSearchBasedView{
	
	@Inject
	private OMPCloseClaimClaimLevelSearchBasedUI searchForm;
	
	private OMPCloseClaimClaimLevelSearchBasedFormDto searchDto;
	@Inject
	private OMPCloseClaimClaimLevelSearchBasedDetailTable searchResultTable;
	
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
			if(comp instanceof OMPCloseClaimClaimLevelSearchBasedDetailTable)
			{
				((OMPCloseClaimClaimLevelSearchBasedDetailTable) comp).removeRow();
			}
		}
	
		
	}
	
	@Override
	public void list(Page<OMPCloseClaimClaimLevelSearchBasedFormDto> tableRows) {
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}
	@Override
	public void doSearch() {
		OMPCloseClaimClaimLevelSearchBasedFormDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(OMPCloseClaimClaimLevelSearchBasedPresenter.SUBMIT_SEARCH, searchDTO,userName,passWord);
		
	}
	@Override
	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
//	searchForm.setType(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		

	}

}
