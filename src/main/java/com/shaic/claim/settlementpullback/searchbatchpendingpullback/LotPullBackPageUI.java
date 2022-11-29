package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class LotPullBackPageUI extends ViewComponent {


	private static final long serialVersionUID = 1L;
	
	private Button submitButton;
	
	private Button cancelButton;
	
	private Panel mainPanel;
	
	private VerticalLayout mainLayout;
	
	private FormLayout formLayout;
	
	////private static Window popup;
	
	private HorizontalLayout submitButtonLayout;
	
	private TextField txtLotNo;
	
	private TextField txtLotCreatedby;
	
	private TextField txtLotCreatedDate;

	private TextField txtApprovedAmnt;
	
	private NewIntimationDto newIntimationDTO;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;

	private RevisedCarousel intimationDetailCarousel;

	private Button viewTrailsBtn;

	@PostConstruct
	public void init() {

	}
	
	protected void showOrHideValidation(Boolean isVisible, Component clearingRemarks) {
		AbstractField<?> field = (AbstractField<?>) clearingRemarks;
		field.setRequired(!isVisible);
		field.setValidationVisible(isVisible);
	}
	
	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			return coordinatorValues;
	}
	 
	 private Panel buildRegistrationPanel() {
			// common part: create layout
			Panel registrationPanel = new Panel();
			String caption = "UNDO LOT";
			HorizontalLayout panelCaption = new HorizontalLayout();

			panelCaption.addStyleName(ValoTheme.PANEL_WELL);
			panelCaption.setSpacing(true);
			panelCaption.setWidth("100%");
			panelCaption.setMargin(new MarginInfo(false, true, false, true));
			Label captionLbl = new Label(caption);
			panelCaption.addComponent(captionLbl);
			
			//Vaadin8-setImmediate() registrationPanel.setImmediate(false);
			registrationPanel.setWidth("100%");
			// registrationPanel.setHeight("130px");
			registrationPanel.addStyleName("panelHeader");

			intimationDetailCarousel = commonCarouselInstance.get();
			intimationDetailCarousel.init(newIntimationDTO);
			VerticalLayout vlayout = new VerticalLayout();
			vlayout.addComponent(panelCaption);
			vlayout.addComponent(intimationDetailCarousel);
			vlayout.setStyleName("policygridinfo");
			registrationPanel.setContent(vlayout);

			return registrationPanel;
		}
	    

	public void initView(SearchLotPullBackTableDTO dto, NewIntimationDto newIntimationDTO) {
		
		ClaimPayment claimPayment = dto.getClaimPayment();
		
		String msg = "";
		if(null != claimPayment.getBatchHoldFlag() && (SHAConstants.YES_FLAG.equalsIgnoreCase(claimPayment.getBatchHoldFlag()))){
			
			msg = "You are pulling the intimation which is kept on Hold";
			showLotStatusAlertMessage(msg);
		}
		else if(null != claimPayment.getPaymentStatus().getKey() && (ReferenceTable.CORRECTION_PAYMENT_STATUS_ID).equals(claimPayment.getPaymentStatus().getKey())){
			
			msg = "You are pulling intimation which is kept for correction";
			showLotStatusAlertMessage(msg);
		}
			
		
		this.newIntimationDTO = newIntimationDTO;
		mainLayout = new VerticalLayout();
		submitButtonLayout = new HorizontalLayout();
		mainPanel = new Panel();
					
		txtLotNo = new TextField("Lot No");
		txtLotNo.setValue(dto.getLotNumber());
		txtLotNo.setEnabled(false);
		
		txtLotCreatedby = new TextField("Lot Created By");
		txtLotCreatedby.setValue(dto.getLotCreatedBy());
		txtLotCreatedby.setEnabled(false);
		
		txtLotCreatedDate = new TextField("Lot Created Date");
		txtLotCreatedDate.setValue(dto.getLotCreatedDate());
		txtLotCreatedDate.setEnabled(false);
		
		txtApprovedAmnt = new TextField("Approved Amount");
		txtApprovedAmnt.setValue(dto.getApprovedAmount() != null ? dto.getApprovedAmount().toString() : "");
		txtApprovedAmnt.setEnabled(false);
		
		formLayout = new FormLayout(txtLotNo, txtLotCreatedby, txtLotCreatedDate, txtApprovedAmnt);
		formLayout.setCaption("Settlement Details");
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		
		HorizontalLayout buttonLayout1 = buildSubmitAndCancelBtnLayout(dto);
		submitButtonLayout.addComponent(buttonLayout1);
		submitButtonLayout.setWidth("100%");
		submitButtonLayout.setSpacing(true);
		submitButtonLayout.setMargin(true);
		submitButtonLayout.setComponentAlignment(buttonLayout1,
				Alignment.MIDDLE_CENTER);
		
		
		viewDetails.initView(newIntimationDTO.getIntimationId(),dto.getRodKey(), ViewLevels.PREAUTH_MEDICAL,SHAConstants.UNDO_LOT);
		
		viewTrailsBtn = new Button("View History");

		viewTrailsBtn.setWidth("150px");
		addListener(newIntimationDTO.getKey(), dto.getRodKey());
		
		mainLayout.addComponent(buildRegistrationPanel());
		HorizontalLayout layout = new HorizontalLayout(viewTrailsBtn,viewDetails );
		layout.setComponentAlignment(viewDetails, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(layout);
		mainLayout.addComponent(formLayout);
		mainLayout.addComponent(submitButtonLayout);
		mainLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(submitButtonLayout, Alignment.BOTTOM_CENTER);
		mainPanel.setWidth("100%");
		mainPanel.setHeight("620px");
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);
	}
	
	
	private void addListener(final Long intimationKey , final Long rodKey) {
		viewTrailsBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					viewClaimHistoryRequest.showReimbursementClaimHistory(intimationKey, rodKey);
				   
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setContent(viewClaimHistoryRequest);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					
	        }

		});
	}
	private HorizontalLayout buildSubmitAndCancelBtnLayout(final SearchLotPullBackTableDTO dto) {

		submitButton = new Button();
		String submitCaption = "Submit";
		submitButton.setCaption(submitCaption);
		//Vaadin8-setImmediate() submitButton.setImmediate(true);
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submitButton.setWidth("-1px");
		submitButton.setHeight("-1px");
		submitButton.setData(dto);
		mainLayout.addComponent(submitButton);

		submitButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					
					ConfirmDialog dialog = ConfirmDialog
							.show(getUI(),
									"Confirmation",
									"Are you sure you want to cancel the LOT approval ?",
									"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												fireViewEvent(LotPullBackPagePresenter.SUBMIT_lOT_PULL_BACK, dto);
											} else {
												
											}
										}

									
									});

					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
					
					
				}
			});
	
		//Vaadin8-setImmediate() submitButton.setImmediate(true);

		cancelButton = new Button();
		cancelButton.setCaption("Cancel");
		//Vaadin8-setImmediate() cancelButton.setImmediate(true);
		cancelButton.setWidth("-1px");
		cancelButton.setHeight("-1px");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);

		cancelButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog
							.show(getUI(),
									"Confirmation",
									"Are you sure you want to cancel ?",
									"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												fireViewEvent(MenuItemBean.LOT_PULL_BACK,
														null);
											} else {
												// User did not confirm
											}
										}
									});

					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			});
		

		HorizontalLayout newBtnLayout = new HorizontalLayout(submitButton,
				cancelButton);
		newBtnLayout.setSpacing(true);
		return newBtnLayout;
	}
	
	private void showLotStatusAlertMessage(String msg){
		
		Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		firstForm.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(firstForm);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setWidth("20%");
		dialog.show(getUI().getCurrent(), null, true);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		
	}
	
}
