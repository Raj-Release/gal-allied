package com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTable;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchUpdateRodDetailsViewImpl extends AbstractMVPView implements SearchUpdateRodDetailsView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchUpdateRodDetailsFormPage searchFormPage;
	
	@Inject
	private SearchUpdateRodDetailsTable searchTable;
	
	/*@Inject
	private SearchCreateRODTable searchTable;*/
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView(){
		addStyleName("view");
		setSizeFull();
		searchFormPage.init();
		searchTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchFormPage);
		mainPanel.setSecondComponent(searchTable);
		mainPanel.setSplitPosition(46);
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchTable.addSearchListener(this);
		searchFormPage.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		SearchUpdateRodDetailsFormDTO searchDTO = searchFormPage.getSearchDTO();
		Pageable pageable = searchTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchUpdateRodDetailsPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchTable.resetTable();
		searchTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchCreateRODTable)
			{
				((SearchCreateRODTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchFormPage.refresh();
		searchTable.resetTable();
	}

	@Override
	public void list(Page<SearchCreateRODTableDTO> tableRows) {
		
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchTable.setTableList(tableRows);
			searchTable.tablesize();
			searchTable.setHasNextPage(tableRows.isHasNext());
			
			/*for (SearchCreateRODTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchTable.setRowColor(tableDto);
				}
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchTable.setRowColor(tableDto);
				}
			}*/
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Update ROD Details Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);

			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
					
				}
			});
		}
		
		searchFormPage.enableButtons();
	}
	
	@Override
	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		searchFormPage.setCPUCode(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		
	}

}
