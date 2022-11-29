package com.shaic.claim.cpuautoallocation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class SearchCpuAutoAllocationViewImpl extends AbstractMVPView implements SearchCpuAutoAllocationView {
	
	/**
	 * 
	 */
	
	@Inject
	private SearchCpuAutoAllocationForm searchCpuForm;
	
	@Inject 
	private SearchCpuAutoAllocationTable searchCpuDetails;
	
	private VerticalSplitPanel mainPanel;
	
	private HorizontalLayout btnLayout;
	
	private VerticalLayout secondLayout = null;
	
	private Button submitBtn;
	
	private Button cancelBtn;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchCpuForm.init();
		searchCpuDetails.init("",false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchCpuForm);
		
		secondLayout = new VerticalLayout();
		
		secondLayout.addComponent(searchCpuDetails);
		
		btnLayout = buildSecondComponent();
		
		mainPanel.setSecondComponent(secondLayout);
		mainPanel.setSplitPosition(41);
		mainPanel.setSizeFull();
		setHeight("570px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchCpuDetails.addSearchListener(this);
		searchCpuForm.addSearchListener(this);
		addListener();
		resetView();
	}
	
	@Override
	public void resetView() {
		searchCpuForm.refresh();
		searchCpuDetails.resetTableDataList();
		
	}

	@Override
	public void doSearch() {
		SearchCpuAutoAllocationDTO searchDTO = searchCpuForm.getSearchDTO();
		
		//if(searchDTO != null && (searchDTO.getDoctorName() != null && !("").equalsIgnoreCase(searchDTO.getDoctorName()))){
			Pageable pageable = searchCpuDetails.getPageable();
			searchDTO.setPageable(pageable);
			
//			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			
			fireViewEvent(SearchCpuAutoAllocationPresenter.SEARCH_BUTTON_CLICK, searchDTO);
		//}
		/*else{
			showErrorMessage("Please Enter Doctor Name");
			resetView();
			resetSearchResultTableValues();
		}*/
	}

	@Override
	public void resetSearchResultTableValues() {
		searchCpuDetails.getPageable().setPageNumber(1);
		searchCpuDetails.resetTable();
		searchCpuDetails.resetTableDataList();
	}
	
	@Override
	public void setCpuDetails(SearchCpuAutoAllocationTableDTO viewSearchCriteriaDTO) {
		searchCpuForm.setCpuNameValue(viewSearchCriteriaDTO);
		
	}

	@Override
	public void getResultList(Page<SearchCpuAutoAllocationTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			
			searchCpuDetails.setTableList(tableRows.getPageItems());
			searchCpuDetails.setFinalTableList(tableRows.getPageItems());
			searchCpuDetails.tablesize();
			searchCpuDetails.setHasNextPage(tableRows.isHasNext());
			
			fireViewEvent(SearchCpuAutoAllocationPresenter.BUILD_SUBMIT_LAYOUT, null);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Cpu Allocation Home");
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
				
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.CPU_AUTO_ALLOCATION, null);
					
				}
			});
		}
		searchCpuForm.enableButtons();
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
	
	private void addListener() {
		submitBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if(searchCpuDetails != null && searchCpuDetails.getTableItems() != null && !searchCpuDetails.getTableItems().isEmpty()){
					
					List<SearchCpuAutoAllocationTableDTO> requestTableList = new ArrayList<SearchCpuAutoAllocationTableDTO>();

					List<SearchCpuAutoAllocationTableDTO> finalListForProcessing = null;
					requestTableList = searchCpuDetails
							.getTableItems();
					
					if (null != requestTableList
							&& !requestTableList
									.isEmpty()) {
						
						finalListForProcessing = new ArrayList<SearchCpuAutoAllocationTableDTO>();
						
						for (SearchCpuAutoAllocationTableDTO tableDTO : requestTableList) {

							if (("true")
									.equalsIgnoreCase(tableDTO
											.getCheckBoxStatus())) {
								finalListForProcessing
										.add(tableDTO);
							}

						}}
						if (null != finalListForProcessing
								&& !finalListForProcessing
										.isEmpty()) {
						ConfirmDialog dialog = ConfirmDialog
								.show(getUI(),
										"Confirmation",
										"Are you sure, you want to update for the selected record?",
										"No", "Yes", new ConfirmDialog.Listener() {

											public void onClose(ConfirmDialog dialog) {
												if (!dialog.isConfirmed()) {
													// Confirmed to continue
													String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
													
													List<SearchCpuAutoAllocationTableDTO> requestTableList = new ArrayList<SearchCpuAutoAllocationTableDTO>();

													requestTableList = searchCpuDetails
															.getTableItems();
													
													fireViewEvent(
															SearchCpuAutoAllocationPresenter.SUBMIT_CPU_ALLOCATION,
															requestTableList,userName);
												} else {
													dialog.setClosable(false);
												}
											}
										});
						dialog.setClosable(false);
						
						
					}else {
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
			
			}
			}
		});

		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if(searchCpuDetails != null && searchCpuDetails.getTableItems() != null && !searchCpuDetails.getTableItems().isEmpty()){
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Do You want to Cancel?", "No", "Yes",
						new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									resetView();
									resetSearchResultTableValues();
									fireViewEvent(
											MenuItemBean.CPU_AUTO_ALLOCATION,
											null, null);
								}else{
									dialog.setClosable(false);
								}
							}
						});
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);

			}}
		});
	}

	@Override
	public void buildSuccessLayout(Integer count) {
		// TODO Auto-generated method stub

		String strMessage = null;
		Label successLabel = null;
			if(count == 0){
				strMessage = "Record Not Updated.";}
			else{
				strMessage = "Records Updated Successfully.";}
			successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("Cpu Aullocation Home");
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
			
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();

					fireViewEvent(MenuItemBean.CPU_AUTO_ALLOCATION, null, null);
			}
		});
	
		
	}

	@Override
	public void buildSubmitLayout() {
		
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
		mainPanel.setFirstComponent(searchCpuForm);
		secondLayout.addComponent(searchCpuDetails);
		mainPanel.setSecondComponent(secondLayout);
		
		btnLayout.addComponent(submitBtn);
		btnLayout.addComponent(cancelBtn);
		
		secondLayout.addComponent(btnLayout);
		secondLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		
		//resetSearchResultTableValues();
	}
}
