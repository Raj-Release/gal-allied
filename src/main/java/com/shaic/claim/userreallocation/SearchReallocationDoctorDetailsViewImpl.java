package com.shaic.claim.userreallocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.AutoAllocationDetails;
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

public class SearchReallocationDoctorDetailsViewImpl extends AbstractMVPView implements SearchReallocationDoctorDetailsView {
	
	/**
	 * 
	 */
	
	@Inject
	private SearchReallocationDoctorNameForm searchDoctorForm;
	
	@Inject 
	private SearchReallocationDoctorDetailsTable searchDoctorDetails;
	
	@Inject 
	private SearchReallocationIntimationDetailsTable searchintimationDetails;
	
	private VerticalSplitPanel mainPanel;
	
	private HorizontalLayout btnLayout;
	
	private VerticalLayout secondLayout = null;
	
	private Button submitBtn;
	
	private Button cancelBtn;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchDoctorForm.init();
		searchDoctorDetails.init("", false, true);
		searchintimationDetails.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchDoctorForm);
		
		secondLayout = new VerticalLayout();
		
		secondLayout.addComponent(searchDoctorDetails);
		
		btnLayout = buildSecondComponent();
		
		mainPanel.setSecondComponent(secondLayout);
		mainPanel.setSplitPosition(41);
		mainPanel.setSizeFull();
		setHeight("570px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchDoctorDetails.addSearchListener(this);
		searchDoctorForm.addSearchListener(this);
		addListener();
		resetView();
	}
	
	@Override
	public void resetView() {
		searchDoctorForm.refresh();
		searchDoctorDetails.resetTableDataList();
		
	}

	@Override
	public void doSearch() {
		SearchReallocationDoctorNameDTO searchDTO = searchDoctorForm.getSearchDTO();
		
		//if(searchDTO != null && (searchDTO.getDoctorName() != null && !("").equalsIgnoreCase(searchDTO.getDoctorName()))){
			Pageable pageable = searchDoctorDetails.getPageable();
			searchDTO.setPageable(pageable);
			
//			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			
			fireViewEvent(SearchReallocationDoctorDetailsPresenter.SEARCH_BUTTON_CLICK, searchDTO);
		//}
		/*else{
			showErrorMessage("Please Enter Doctor Name");
			resetView();
			resetSearchResultTableValues();
		}*/
	}

	@Override
	public void resetSearchResultTableValues() {
		searchDoctorDetails.getPageable().setPageNumber(1);
		searchDoctorDetails.resetTable();
		searchDoctorDetails.resetTableDataList();
		
		searchintimationDetails.resetTable();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				VerticalLayout vLayout = (VerticalLayout)comp;
				Iterator<Component> vCompIter = vLayout.getComponentIterator();
				while(vCompIter.hasNext())
				{
					Component vComp = (Component)vCompIter.next();
					if(vComp instanceof SearchReallocationDoctorDetailsTable)
					{
						((SearchReallocationDoctorDetailsTable) vComp).removeRow();
					}
				}
			
			
			}
		}
	
		
	}
	
	@Override
	public void setDoctorDetails(SearchReallocationDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		searchDoctorForm.setDoctorNameValue(viewSearchCriteriaDTO);
		
	}

	@Override
	public void getResultList(Page<SearchReallocationDoctorDetailsTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			tableRows.getPageItems();
			
			searchDoctorDetails.setTableList(tableRows, SHAConstants.SEARCH_USER_REALLOCATION);
			searchDoctorDetails.setFinalTableList(tableRows.getPageItems());
			searchDoctorDetails.tablesize();
			searchDoctorDetails.setHasNextPage(tableRows.isHasNext());
			
			fireViewEvent(SearchReallocationDoctorDetailsPresenter.BUILD_SUBMIT_LAYOUT, null);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("User Acces Home");
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
					fireViewEvent(MenuItemBean.USER_RE_ALLOCATION, null);
					
				}
			});
		}
		searchDoctorForm.enableButtons();
	}
	
	private HorizontalLayout buildSecondComponent()
	{
		submitBtn = new Button();
		submitBtn.setCaption("Submit");
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		
		
		cancelBtn = new Button();
		cancelBtn.setCaption("Cancel");
		//Vaadin8-setImmediate() cancelBtn.setImmediate(true);
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		//Vaadin8-setImmediate() cancelBtn.setImmediate(true);
		
		btnLayout = new HorizontalLayout();
		btnLayout.setSpacing(true);
		
		return btnLayout;
	}
	
	public void buildSubmitLayout(){
		
		if (null != btnLayout
				&& btnLayout.getComponentCount() > 0) {
			btnLayout.removeAllComponents();
		}
		
		if (null != secondLayout
				&& secondLayout.getComponentCount() > 0) {
			secondLayout.removeAllComponents();
		}
		
		
		if (null != mainPanel
				&& mainPanel.getComponentCount() > 0) {
			mainPanel.removeAllComponents();
		}
		mainPanel.setFirstComponent(searchDoctorForm);
		secondLayout.addComponent(searchDoctorDetails);
		mainPanel.setSecondComponent(secondLayout);
		
		btnLayout.addComponent(submitBtn);
		btnLayout.addComponent(cancelBtn);
		
		secondLayout.addComponent(btnLayout);
		secondLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		
		//resetSearchResultTableValues();
	}
	
	
	@Override
	public void init(SearchReallocationDoctorDetailsTableDTO doctorDetailsDTO) {
		// TODO Auto-generated method stub
		
	}
	
	private void addListener() {
		submitBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if(searchDoctorDetails != null && searchDoctorDetails.getTableItems() != null && !searchDoctorDetails.getTableItems().isEmpty()){
					
					Boolean chkBoxValue = false;

					List<SearchReallocationDoctorDetailsTableDTO> requestTableList = new ArrayList<SearchReallocationDoctorDetailsTableDTO>();

					requestTableList = searchDoctorDetails
							.getTableItems();

					List<SearchReallocationDoctorDetailsTableDTO> finalListForProcessing = null;
					if (null != requestTableList
							&& !requestTableList
									.isEmpty()) {
						finalListForProcessing = new ArrayList<SearchReallocationDoctorDetailsTableDTO>();
						for (SearchReallocationDoctorDetailsTableDTO createAndSearchLotTableDTO : requestTableList) {

							if (("true")
									.equalsIgnoreCase(createAndSearchLotTableDTO
											.getCheckBoxStatus())) {
								finalListForProcessing
										.add(createAndSearchLotTableDTO);
							}

						}
					}
					
					if (null != finalListForProcessing
							&& !finalListForProcessing
									.isEmpty()) {
						ConfirmDialog dialog = ConfirmDialog
								.show(getUI(),
										"Confirmation",
										"Are you sure, you want to update status for the selected record(s)?",
										"No", "Yes", new ConfirmDialog.Listener() {

											public void onClose(ConfirmDialog dialog) {
												if (!dialog.isConfirmed()) {
													// Confirmed to continue
													String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
													
													List<SearchReallocationDoctorDetailsTableDTO> requestTableList = new ArrayList<SearchReallocationDoctorDetailsTableDTO>();

													requestTableList = searchDoctorDetails
															.getTableItems();
													
													fireViewEvent(
															SearchReallocationDoctorDetailsPresenter.SUBMIT_REALLOCATION,
															requestTableList,userName);
												} else {
													// User did not confirm
													dialog.setClosable(false);
													// dialog.setStyleName(Reindeer.WINDOW_BLACK);

												}
											}
										});
						dialog.setClosable(false);
						
						
					} else {
						Label label = new Label(
								"Please select a record.",
								ContentMode.HTML);
						label.setStyleName("errMessage");
						VerticalLayout layout = new VerticalLayout();
						layout.setMargin(true);
						layout.addComponent(label);
						ConfirmDialog dialog1 = new ConfirmDialog();
						dialog1.setCaption("Errors");
						dialog1.setClosable(true);
						dialog1.setContent(layout);
						dialog1.setResizable(false);
						dialog1.setModal(true);
						dialog1.show(getUI()
								.getCurrent(), null,
								true);
					}
				
			}else{
				Label label = new Label(
						"No records found.",
						ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog1 = new ConfirmDialog();
				dialog1.setCaption("Errors");
				dialog1.setClosable(true);
				dialog1.setContent(layout);
				dialog1.setResizable(false);
				dialog1.setModal(true);
				dialog1.show(getUI()
						.getCurrent(), null,
						true);
			
				}
			}
		});

		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				resetView();
				resetSearchResultTableValues();
				fireViewEvent(MenuItemBean.USER_RE_ALLOCATION, null, null);

			}
		});
	}

	@Override
	public void buildSuccessLayout(Map<String, Object> mapper) {
		// TODO Auto-generated method stub

		final String error = (String)mapper.get(SHAConstants.REALLOCATION_ERROR);
		
		
		String totalNoOfRecords = (String)mapper.get(SHAConstants.TOTAL_NO_REALLOCATION_RECORDS);
		String noOfRecordsSentForReallocation = (String)mapper.get(SHAConstants.NO_RECORDS_SENT_TO_REALLOCATION);
		
		String strMessage = null;
		Label successLabel = null;
		if(null != error && !("").equalsIgnoreCase(error) && ("true").equalsIgnoreCase(error))
		{
			strMessage = "Status not Updated.";
					successLabel = new Label("<b style = 'color: red;'>" + strMessage + "</b>",
							ContentMode.HTML);
		}
		else
		{
			if(noOfRecordsSentForReallocation.equalsIgnoreCase("0")){
				strMessage = "No Status to Update.";}
			else{
				strMessage = "Status Updated.";}
			String strRecMessage = "Selected "+noOfRecordsSentForReallocation +"/"+ totalNoOfRecords+" records have been sent to the Reallocation successfully !!";
			successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>" + "</br>" + "<b style = 'color: green;'>" + strRecMessage + "</b>",
				ContentMode.HTML);
		}
		
		Button homeButton = new Button("Reallocation Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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

				if(!(null != error && !("").equalsIgnoreCase(error) && ("true").equalsIgnoreCase(error)))
				{
					fireViewEvent(MenuItemBean.USER_RE_ALLOCATION, null, null);
				}
				
				
				
			}
		});
	
		
	}

	@Override
	public void buildIntimationTableLayout(AutoAllocationDetails detail) {
		// TODO Auto-generated method stub
		if(detail != null){
		if (null != mainPanel
				&& mainPanel.getComponentCount() > 0) {
			mainPanel.removeAllComponents();
		}
		mainPanel.setFirstComponent(searchDoctorForm);
		mainPanel.setSecondComponent(buildIntimationLayout(detail));
		}
		else{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("User Acces Home");
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
					fireViewEvent(MenuItemBean.USER_RE_ALLOCATION, null);
					
				}
			});
		}
	}
	
	public VerticalLayout buildIntimationLayout(AutoAllocationDetails detail) {
		
		if (null != secondLayout
				&& secondLayout.getComponentCount() > 0) {
			secondLayout.removeAllComponents();
		}
		
		searchintimationDetails.removeTableItems();
		
		int sNo = 1;
		if(detail != null){
			AutoAllocationDetailsTableDTO dto = new AutoAllocationDetailsTableDTO();
			dto.setIntimationNo(detail.getIntimationNo());
			dto.setDoctorId(detail.getDoctorId());
			dto.setDoctorName(detail.getDoctorName());
			dto.setsNumber(sNo);
			searchintimationDetails.addBeanToList(dto);
		}
		
		secondLayout.addComponent(searchintimationDetails);
		return secondLayout;
	}

}
