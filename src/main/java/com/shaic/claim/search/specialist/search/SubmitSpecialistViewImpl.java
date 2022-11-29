package com.shaic.claim.search.specialist.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class SubmitSpecialistViewImpl  extends AbstractMVPView implements SubmitSpecialistView, Searchable  {
	

	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SubmitSpecialistForm searchForm;
	
	@Inject
	private SubmitSpecialistTable searchResultTable;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();	
	
	boolean isReimburement;
	
	@PostConstruct
	protected void initView() {
		/*addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false);
		searchResultTable.setHeight("100.0%");
		searchResultTable.setWidth("100.0%");
		
		searchForm.setHeight(200, Unit.PIXELS);
		
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		
		mainPanel.setSplitPosition(40);
		mainPanel.setHeight("100.0%");
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);
		
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);*/
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		/*searchResultTable.setHeight("100.0%");
		searchResultTable.setWidth("100.0%");
		
		searchResultTable.setWidth("1378px");
		searchResultTable.setHeight("380px");*/
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(47);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
//		String username = (String)getSession().getAttribute(BPMClientContext.USERID);
		//String username = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
		resetView();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		
		searchForm.refresh(); 
		/*if(searchForm.get() != null) {
			searchForm.get().init();
		}*/
	}
	@Override
	public void list(Page<SubmitSpecialistTableDTO> tableRows) {
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
//			List<SubmitSpecialistTableDTO> result = tableRows.getPageItems();
//			
//			if(result.get(0).getIsReimburementFlag()){
//				searchResultTable.setVisibleColumns();
//			}
			
			searchResultTable.setTableList(tableRows);
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SubmitSpecialistTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Submit Specialist Home");
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
					
					if(isReimburement){
						fireViewEvent(MenuItemBean.SUBMIT_SPECIALIST_ADVISE, null);
					}else{
						fireViewEvent(MenuItemBean.SUBMIT_SPECIALLIST_ADVISE, null);
					}
					
					
				}
			});
			
		}
		//searchResultTable.setTableList(tableRows.getPageItems());
	}
	
	@Override
	public void init(BeanItemContainer<SelectValue> parameter, boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer) {
		// TODO Auto-generated method stub
		searchForm.setRefByDocList(parameter);
		this.isReimburement = isReimburement;
		searchForm.setReimbursement(isReimburement);
		if(isReimburement){
			searchResultTable.setVisibleColumns();
		}else{
			searchResultTable.setNaturalColumns();
		}
		searchForm.setDropDownValues(cpuCodeContainer);
		searchForm.init();
	}
	
	
	

	
	@Override
	public void doSearch() {
		SubmitSpecialistFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		
		String username = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
		String password = (String) getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		searchDTO.setUsername(username);
		searchDTO.setPassword(password);

		fireViewEvent(SubmitSpecialistPresenter.SEARCH_BUTTON_CLICK, searchDTO ,isReimburement);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SubmitSpecialistTable)
			{
				((SubmitSpecialistTable) comp).removeRow();
			}
		}
	}

	@Override
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCodeContainer) {
		searchForm.setDropDownValues(cpuCodeContainer);
	}

	public void initResetView(BeanItemContainer<SelectValue> parameter, boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer,SubmitSpecialistFormDTO dto) {
		// TODO Auto-generated method stub
		searchForm.setRefByDocList(parameter);
		this.isReimburement = isReimburement;
		searchForm.setReimbursement(isReimburement);
		if(isReimburement){
			searchResultTable.setVisibleColumns();
		}else{
			searchResultTable.setNaturalColumns();
		}
		searchForm.setDropDownValues(cpuCodeContainer);
		searchForm.initwithSearchForm(dto);
		doSearch();
	}
	

}