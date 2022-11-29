/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequest.search;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.poi.hssf.record.ScenarioProtectRecord;
import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestViewImpl extends AbstractMVPView implements SearchProcessClaimRequestView{

	
	@Inject
	private SearchProcessClaimRequestForm  searchForm;
	
	@Inject
	private SearchProcessClaimRequestTable searchResultTable;
	
	private SearchProcessClaimRequestFormDTO dto = new SearchProcessClaimRequestFormDTO();
	private VerticalSplitPanel mainPanel;
	
	private String screenName;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.initView(dto, false);
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		Panel panel = new Panel(searchResultTable);
		mainPanel.setSecondComponent(panel);
		mainPanel.setSplitPosition(45);
		setHeight("570px");
	//	mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	public void initViewForReferesh(SearchProcessClaimRequestFormDTO dto, Boolean shouldDoSearch){
		searchForm.initView(dto, shouldDoSearch);
		if(shouldDoSearch) {
			doSearch();
		}
	}
	
	@Override
	public void resetView() {
		if(searchForm != null) {
			searchForm.refresh();
		}
	}
	

	@Override
	public void doSearch() {
		SearchProcessClaimRequestFormDTO searchDTO = searchForm.getSearchDTO();

		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		ImsUser imsUser= (ImsUser) getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		searchDTO.setImsUser(imsUser);
		
		Double claimedAmountFrom = searchDTO.getClaimedAmountFrom();
		Double claimedAmountTo = searchDTO.getClaimedAmountTo();
		
		if(claimedAmountFrom != null && claimedAmountTo != null)  {
			if((claimedAmountFrom > claimedAmountTo || claimedAmountTo < claimedAmountFrom)) {
				 getErrorMessage("Claimed Amount From should not less than Claim Amount To");
			}else{
				 fireViewEvent(SearchProcessClaimRequestPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord,screenName);
			}
		     
		}else{
			 fireViewEvent(SearchProcessClaimRequestPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord,screenName);
		}
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchProcessClaimRequestTable)
			{
				((SearchProcessClaimRequestTable) comp).removeRow();
			}
		}
	
		
	}
	
	public void getErrorMessage(String eMsg){
		
	/*	Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	@Override
	public void list(Page<SearchProcessClaimRequestTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SearchProcessClaimRequestTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
				
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
		}
		else
		{
			SearchProcessClaimRequestFormDTO searchDTO = searchForm.getSearchDTO();
			/**
			 * If every screen has intimation no within, then the 
			 * below search id setter is not required. Post analysis, shall
			 * think of removing the same.
			 * */
			if(searchDTO.getIntimationNo() != null && !searchDTO.getIntimationNo().isEmpty() && searchDTO.getIntimationNo().length() > 0  && 
					(null == searchDTO.getPolicyNo() ||  searchDTO.getPolicyNo().isEmpty()) && (null == searchDTO.getHospitalType()) && (null == searchDTO.getTreatementType())
					&& (null == searchDTO.getCpuCode()) && (null == searchDTO.getPriority()) && (null == searchDTO.getType()) && (null == searchDTO.getIntimationSource())
					&& (null == searchDTO.getNetworkHospType()) && (null == searchDTO.getSpeciality()) && (null == searchDTO.getProductName()) && (null == searchDTO.getSource())
					&& (null == searchDTO.getRequestedBy()) && (null == searchDTO.getClaimedAmountFrom()) && (null == searchDTO.getClaimedAmountTo())
					) {
				searchDTO.setSearchId(searchDTO.getIntimationNo());
				fireViewEvent(MenuPresenter.SHOW_SEARCH_SCREEN_VALIDATION_MESSAGE, searchDTO,null);
			} else {
				Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);
				/*Button homeButton = new Button("Process Claim Request Home");*/
				String buttonName="Process Claim Request Home";
				if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
					/*homeButton.setCaption("Waiting For Input Home");*/
					buttonName="Waiting For Input Home";
				}
				
				/*homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), buttonName);
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox("No Records found.", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(null != screenName && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(screenName)))
						{
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION, null);
						}
						else if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))){
							
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, null);
						}
						else {
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, null);
						}
						
						
					}
				});
			}
			
			
		}
		searchForm.enableButtons();
	}

	

	
	@Override
	public void specialityList(
			BeanItemContainer<SelectValue> specialityValueByReference) {
		searchForm.setSpecialityCBX(specialityValueByReference);
		
	}
	
	
	@Override
	public void init(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> claimType,
			String screenName) {
		
		this.screenName = screenName;
		searchResultTable.setpresenterName(screenName);
		searchForm.setCBXValue(intimationSource, hospitalType, networkHospitalType,treatementType,typeContainer,productName,cpuCode,statusByStage,claimType,screenName);
		
	}
	
}
