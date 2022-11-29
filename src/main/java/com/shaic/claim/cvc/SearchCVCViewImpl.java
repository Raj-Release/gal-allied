package com.shaic.claim.cvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.preauth.search.autoallocation.SearchPreAuthAutoAllocationTable;
//import com.shaic.claim.preauth.search.autoallocation.SearchPreauthAutoAllocationPresenter;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportUI;
import com.vaadin.server.VaadinSession;
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

public class SearchCVCViewImpl extends AbstractMVPView implements
SearchCVCView,Searchable{


	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SearchCVCForm searchForm;
	
	@Inject
	private CVCPageUI cvcPageUI;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();		
		mainPanel.setFirstComponent(searchForm);	
	
		mainPanel.setSplitPosition(50);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchForm.addSearchListener(this);
		resetView();
	
		}


	public void init() {
		searchForm.init();
	}
	
		@Override
		public void resetView() {
			System.out.println("---tinside the reset view");
			searchForm.resetAlltheValues();
		}

	@Override
	public void list(SearchCVCTableDTO bean) {
		
		/*if(null != bean)
		{	
			fireViewEvent(MenuPresenter.CVC_AUDIT_WIZARD,bean);
			
		}*/
		if(null != bean && bean.getMessage() != null)
		{	
			if (null != bean && bean.getMessage() != null 
					&& bean.getMessage().equalsIgnoreCase("SUCCESS")){
				fireViewEvent(MenuPresenter.CVC_AUDIT_WIZARD,bean);
			} else {
				getErrorMessage(bean.getMessage());
			}
			
		} else if(null != bean && bean.getIntimationKey() != null) {
			fireViewEvent(MenuPresenter.CVC_AUDIT_WIZARD,bean);
		}
		
		else
		{
				Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
				Button homeButton = new Button("Claims Audit Home");
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
						fireViewEvent(MenuItemBean.CVC_AUDIT,null);
					}
				});
		}
	}

	@Override
	public void doSearch() {
		
		SearchCVCFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = new Pageable();
		searchDTO.setPageable(pageable);

		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		ImsUser imsUser = (ImsUser)getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		searchDTO.setImsUser(imsUser);
		fireViewEvent(SearchCVCPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord,imsUser);
		searchForm.resetAlltheValues();
	}
	
	@Override
	public void resetSearchResultTableValues() {
		
		/*searchResultTable.getPageable().setPageNumber(1);
	    searchResultTable.resetTable();*/
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchPreAuthAutoAllocationTable)
			{
				((SearchPreAuthAutoAllocationTable) comp).removeRow();
			}
		}
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	
	public void tableSelectHandler(SearchCVCTableDTO t) {
		Date date = new Date();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		VaadinSession session = getSession();
	    
	    Boolean isActiveHumanTask = false;
	    
	//	Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
	    Map<String, Object> wrkFlowMap = new HashMap<String, Object>();
		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
		
		String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		DBCalculationService dbCalculationService = new DBCalculationService();
		String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
		String[] lockSplit = callLockProcedure.split(":");
		String sucessMsg = lockSplit[0];
		String userName = lockSplit[1];
		
		if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
			isActiveHumanTask= true;
		}
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			
		} else {
			
		}
		
	}
	
	public void alertMessage(final SearchCVCTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setData(t);
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
	}
}
