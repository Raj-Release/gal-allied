package com.shaic.claim.preauth.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.MasUserAutoAllocation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;


public class SearchPreauthViewImpl extends AbstractMVPView implements
		SearchPreauthView,Searchable, View {

	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private SearchPreauthForm searchForm;
	
	@Inject
	private SearchPreAuthList searchResultTable;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	@PostConstruct
	protected void initView() {
	
		
		/*addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false);
		searchResultTable.setHeight("100.0%");
		searchResultTable.setWidth("100.0%");
		
		searchResultTable.setWidth("1378px");
		searchResultTable.setHeight("");
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));

		//searchForm.setHeight(600, Unit.PIXELS);

		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);

		mainPanel.setSplitPosition(33);
		//mainPanel.setHeight("100.0%");
		mainPanel.setWidth("100.0%");
		

		//mainPanel.m
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);

		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this); */
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		//searchResultTable.setHeight("100.0%");
		//searchResultTable.setWidth("100.0%");
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));;

		//searchForm.setHeight(600, Unit.PIXELS);

		//searchResultTable.setWidth("2400px");
		//searchResultTable.setHeight("300px");
		//searchResultTable.tablesize();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		
		/**
		 * This is added for UAT.
		 * */
		mainPanel.setSplitPosition(36);

		//mainPanel.setSplitPosition(40);
		//mainPanel.setHeight("100.0%");
		mainPanel.setWidth("100.0%");
		//mainPanel.m
		setHeight("100.0%");
		setHeight("550px");
		setCompositionRoot(mainPanel);

		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
	
	
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
	public void list(Page<SearchPreauthTableDTO> tableRows) {
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			//searchResultTable.setPage(tableRows);
			
			for (SearchPreauthTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
					//searchResultTable.setCellColour(tableDto);
				}
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
					//searchResultTable.setCellColour(tableDto);
				}
			}
		}
		
		else
		{
			SearchPreauthFormDTO searchDTO = searchForm.getSearchDTO();
			/**
			 * If every screen has intimation no within, then the 
			 * below search id setter is not required. Post analysis, shall
			 * think of removing the same.
			 * */
			if(searchDTO.getIntimationNo() != null && !searchDTO.getIntimationNo().isEmpty() && searchDTO.getIntimationNo().length() > 0  && 
					(null == searchDTO.getPolicyNo() ||  searchDTO.getPolicyNo().isEmpty()) && (null == searchDTO.getSpeciality()) && (null == searchDTO.getTreatmentType())
					&& (null == searchDTO.getSource()) && (null == searchDTO.getCpuCode()) && (null == searchDTO.getPriority()) && (null == searchDTO.getType()) 
					&& (null == searchDTO.getNetworkHospType()) && (null == searchDTO.getIntimationSource()) && (null == searchDTO.getClaimedAmtFrom() || searchDTO.getClaimedAmtFrom().isEmpty()) 
					&& (null == searchDTO.getClaimedAmtTo() ||  searchDTO.getClaimedAmtTo().isEmpty())
					) {
				searchDTO.setSearchId(searchDTO.getIntimationNo());
				fireViewEvent(MenuPresenter.SHOW_SEARCH_SCREEN_VALIDATION_MESSAGE_CASHLESS, searchDTO,null);
			} else
			{/*
				
				
				
				Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
				Button homeButton = new Button("Pre Auth Home");
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
						fireViewEvent(MenuItemBean.PROCESS_PREAUTH, null);
						
					}
				});
				
			*/
				
				final MessageBox msgBox = MessageBox
					    .createInfo()
					    .withCaptionCust("Information")
					    .withMessage("No Records found.")
					    .withOkButton(ButtonOption.caption("Pre Auth Home"))
					    .open();
				Button homeButton=msgBox.getButton(ButtonType.OK);
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						msgBox.close();
						fireViewEvent(MenuItemBean.PROCESS_PREAUTH, null);
						
					}
				});
			}
			
			
		}
		
		
			
	
	}

	@Override
	public void doSearch() {
		SearchPreauthFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);

		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		ImsUser imsUser = (ImsUser)getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		searchDTO.setImsUser(imsUser);
		
		String claimedAmtFrom = searchDTO.getClaimedAmtFrom();
		String claimedAmtTo = searchDTO.getClaimedAmtTo();
		
		if(claimedAmtFrom != null && claimedAmtTo != null && !claimedAmtFrom.isEmpty() && !claimedAmtTo.isEmpty() )  {
			Double claimAmtFrom = SHAUtils.getDoubleValueFromString(claimedAmtFrom);
			Double claimAmtTo = SHAUtils.getDoubleValueFromString(claimedAmtTo);
			if((claimAmtFrom > claimAmtTo || claimAmtTo < claimAmtFrom) ) {
				 getErrorMessage("Claimed Amount From should not less than Claim Amount To");
			}else{
				fireViewEvent(SearchPreauthPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord,imsUser);
			}
		} else {
			fireViewEvent(SearchPreauthPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord,imsUser);
		}
		
		
		
		
	}

	@Override
	public void init(BeanItemContainer<SelectValue> intimationSrcParameter,
			BeanItemContainer<SelectValue> networkHospTypeParameter,
			BeanItemContainer<SelectValue> treatmentTypeParameter,
			BeanItemContainer<SelectValue> preAuthTypeParameter,BeanItemContainer<SelectValue> specialityContainer, BeanItemContainer<SelectValue> cpuCodeContainer) {
		
		searchForm.setIntimationSrc(intimationSrcParameter);
		searchForm.setNetworkHospType(networkHospTypeParameter);
		searchForm.setTreatmentType(treatmentTypeParameter);
		searchForm.setPreAuthType(preAuthTypeParameter);
		searchForm.setSpeciality(specialityContainer);
		searchForm.setCpuCode(cpuCodeContainer);
		// CR2019213
		searchForm.setManaualFlag();
		// TODO Auto-generated method stub
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		SearchPreauthFormDTO searchDTO = searchForm.getSearchDTO();
		if(userName != null){
			fireViewEvent(SearchPreauthPresenter.AUTO_ALLOCATION_VIEW, searchDTO, userName);
		}
		
	}

	@Override
	public void initSpeciality(
			BeanItemContainer<SelectValue> specialityTypeParameter) {
		// TODO Auto-generated method stub
		searchForm.setSpeciality(specialityTypeParameter);
		
	}
	
	@Override
	public void resetSearchResultTableValues() {
		
		searchResultTable.getPageable().setPageNumber(1);
	    searchResultTable.resetTable();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchPreAuthList)
			{
				((SearchPreAuthList) comp).removeRow();
			}
		}
	}
	
	public void getErrorMessage(String eMsg){/*
		
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
	*/
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();	
	}

	@Override
	public void changeView(SearchPreauthFormDTO searchFormDTO, MasUserAutoAllocation user) {
		if (user != null) {
			if (user.getUserType() != null) {
				if (user.getUserType().getValue().equalsIgnoreCase(
						SHAConstants.CPU_ALLOCATION_CORP_USER)) {
					searchFormDTO.setIsCPUUser(false);
					searchFormDTO.setIsCorpUser(true);
					if(searchResultTable != null){
						searchResultTable.setCorpView();
					}
					
				} else if (user.getUserType().getValue().equalsIgnoreCase(
						SHAConstants.CPU_ALLOCATION_CPU_USER)) {
					searchFormDTO.setIsCPUUser(true);
					searchFormDTO.setIsCorpUser(false);
					if(searchForm != null){
						searchForm.setCpuView();
					}
					if(searchResultTable != null){
						searchResultTable.setCpuView();
					}
				}else{
					searchFormDTO.setIsCPUUser(false);
					searchFormDTO.setIsCorpUser(false);
				}

			}
		}

	}

}
