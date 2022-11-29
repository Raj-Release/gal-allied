package com.shaic.claim.processdatacorrectionhistorical.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.preauth.search.flpautoallocation.SearchFLPAutoAllocationTableDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationPresenter;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTable;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchProcessDataCorrectionHistoricalViewImpl extends AbstractMVPView implements SearchProcessDataCorrectionHistoricalView ,Searchable {

	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SearchProcessDataCorrectionHistoricalForm searchForm;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	private boolean manualCoadingFlag = false;	
	
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setWidth("100.0%");
		mainPanel.setSplitPosition(25);
		setHeight("500px");
		setCompositionRoot(mainPanel);

		searchForm.addSearchListener(this);
		resetView();
		
	}

	@Override
	public void resetView() {
		
		System.out.println("---tinside the reset view");
		searchForm.refresh(); 
	}

	@Override
	public void list(ProcessDataCorrectionDTO tableRows) {
		if(null != tableRows)
		{	
			fireViewEvent(MenuItemBean.PROCESS_DATA_CORRECTION_HISTORICAL_VIEW,tableRows);
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Data Validation Historical");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION_HISTORICAL, null);
					
				}
			});
			
		}
	}
	
	@Override
	public void doSearch() {
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);		
		fireViewEvent(SearchProcessDataCorrectionHistoricalPresenter.SEARCH_INTIMATION_DATA_CORRECTION_HISTORICAL, null,userName);
	}

	@Override
	public void resetSearchResultTableValues() {
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessTranslationTable)
			{
				((SearchProcessTranslationTable) comp).removeRow();
			}
		}
	
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(boolean manualCoadingFlag) {	
		this.manualCoadingFlag = manualCoadingFlag;	
	}

	public void tableSelectHandler(SearchFLPAutoAllocationTableDTO t) {

		
	}
}
