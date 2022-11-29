package com.shaic.claim.pincodemapping;

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

public class SearchPinCodeViewImpl extends AbstractMVPView implements SearchPinCodeView {
	
	/**
	 * 
	 */
	
	@Inject
	private SearchPinCodeForm searchCpuForm;
	
	@Inject 
	private SearchPinCodeTable searchCpuDetails;
	
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
		SearchPinCodeDTO searchDTO = searchCpuForm.getSearchDTO();
		
		if(searchDTO != null && (searchDTO.getPinCode() != null && !("").equalsIgnoreCase(searchDTO.getPinCode()))){
			Pageable pageable = searchCpuDetails.getPageable();
			searchDTO.setPageable(pageable);
			
//			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			
			fireViewEvent(SearchPinCodePresenter.SEARCH_BUTTON_CLICK, searchDTO);
		}
		else{
			showErrorMessage("Please Enter Pincode");
			resetView();
			resetSearchResultTableValues();
		}	}

	@Override
	public void resetSearchResultTableValues() {
		searchCpuDetails.getPageable().setPageNumber(1);
		searchCpuDetails.resetTable();
		searchCpuDetails.resetTableDataList();
	}
	
	@Override
	public void getResultList(Page<SearchPinCodeTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			
			searchCpuDetails.setTableList(tableRows.getPageItems());
			searchCpuDetails.setFinalTableList(tableRows.getPageItems());
			searchCpuDetails.tablesize();
			searchCpuDetails.setHasNextPage(tableRows.isHasNext());
			
			fireViewEvent(SearchPinCodePresenter.BUILD_SUBMIT_LAYOUT, null);
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>The entered pincode is not available,</br> Pls contact the IT team for adding a new PINCODE.</b>", ContentMode.HTML);
						
			Button homeButton = new Button("Pincode Mapping Home");
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
					fireViewEvent(MenuItemBean.PINCODE_CLASS_MAPPING, null);
					
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
					
					List<SearchPinCodeTableDTO> requestTableList = new ArrayList<SearchPinCodeTableDTO>();

					List<SearchPinCodeTableDTO> finalListForProcessing = null;
					requestTableList = searchCpuDetails
							.getTableItems();
					
					if (null != requestTableList
							&& !requestTableList
									.isEmpty()) {
						
						finalListForProcessing = new ArrayList<SearchPinCodeTableDTO>();
						
						for (SearchPinCodeTableDTO tableDTO : requestTableList) {

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
													
													List<SearchPinCodeTableDTO> requestTableList = new ArrayList<SearchPinCodeTableDTO>();

													requestTableList = searchCpuDetails
															.getTableItems();
													
													fireViewEvent(
															SearchPinCodePresenter.SUBMIT_PIN_CODE,
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
											MenuItemBean.PINCODE_CLASS_MAPPING,
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
				strMessage = "Pincode Mapping Updated Successfully.";}
			successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("Pincode Mapping Home");
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

					fireViewEvent(MenuItemBean.PINCODE_CLASS_MAPPING, null, null);
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
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
}
