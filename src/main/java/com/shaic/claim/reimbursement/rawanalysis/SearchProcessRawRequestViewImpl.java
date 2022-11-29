package com.shaic.claim.reimbursement.rawanalysis;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.rrc.services.SearchProcessRRCRequestTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchProcessRawRequestViewImpl extends AbstractMVPView implements SearchProcessRawRequestView {
	
	@Inject
	private SearchProcessRawRequestForm searchForm;
	
	@Inject
	private SearchProcessRawRequestTable searchTable;
	
	private VerticalSplitPanel mainPanel;
	
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchTable);
		mainPanel.setSplitPosition(41);
		setHeight("620px");
		setCompositionRoot(mainPanel);
		searchTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		SearchProcessRawRequestFormDto searchDto = searchForm.getSearchDTO();
		Pageable pageable = searchTable.getPageable();
		searchDto.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		if(null != searchForm && null != searchForm.getCmbHospitals() &&
				(null == searchForm.getCmbHospitals().getValue()||("").equalsIgnoreCase(searchForm.getCmbHospitals().getValue()))){
			searchDto.setHospitalCode(null);	
		}
		fireViewEvent(SearchProcessRawRequestPresenter.SEARCH_RAW_REQUEST, searchDto, userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchTable.getPageable().setPageNumber(1);
		searchTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessRRCRequestTable)
			{
				((SearchProcessRRCRequestTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void resetView() {
		searchForm.refresh();
	}

	@Override
	public void list(Page<SearchProcessRawRequestTableDto> list) {
		if(null != list && null != list.getPageItems() && 0!= list.getPageItems().size())
		{	
			searchTable.setTableList(list);
			searchTable.setHasNextPage(list.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Process RAW Request Home");
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
					fireViewEvent(MenuItemBean.PROCESS_RAW_REQUEST, null);
					
				}
			});
		}
		searchForm.enableButtons();
	}
	
	public void init(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> requestBy,BeanItemContainer<SelectValue> statusByStage){
		searchForm.setDropDownValues(cpu,requestBy,statusByStage);
	}

}
