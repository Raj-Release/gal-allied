package com.shaic.claim.pcc.hrmp;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionPresenter;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.pcc.wizard.SearchProcessPCCRequestTable;
import com.shaic.domain.MasClmAuditUserMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
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

/**
 * @author GokulPrasath.A
 *
 */
public class SearchHRMPViewImpl extends AbstractMVPView implements SearchHRMPView , Searchable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchHRMPForm searchForm;
	
	@Inject
	private SearchHRMPTable searchTable;
	
	private VerticalSplitPanel mainPanel;


	
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		//searchForm.setViewImpl(this);
		searchTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchTable);
		mainPanel.setSplitPosition(41);
		setHeight("670px");
		setCompositionRoot(mainPanel);
		searchTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		SearchHRMPFormDTO searchDto = searchForm.getSearchDTO();
		Pageable pageable = searchTable.getPageable();
		searchDto.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
	
		fireViewEvent(SearchHRMPPresenter.SUBMIT_SEARCH, searchDto,userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchTable.getPageable().setPageNumber(1);
		searchTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessPCCRequestTable)
			{
				((SearchProcessPCCRequestTable) comp).removeRow();
			}
		}
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchForm.refresh();
	}

	@Override
	public void list(Page<SearchHRMPTableDTO> list) {
		if(null != list && null != list.getPageItems() && 0!= list.getPageItems().size())
		{	
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			//searchTable.setUserName(userName);
			searchTable.setTableList(list);
			searchTable.setHasNextPage(list.isHasNext());
		}
		else
		{
		
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		searchForm.enableButtons();
	}
	
	@Override
	public void setAuditUser(MasClmAuditUserMapping auditUserMap){
		searchForm.setAuditUser(auditUserMap);
	}

	@Override
	public void init(){

		String userName = (String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		searchForm.setDropDownValues(userName);
		fireViewEvent(SearchHRMPPresenter.GET_AUDIT_USER_CLM_TYPE, userName);

	}

}
	
	
