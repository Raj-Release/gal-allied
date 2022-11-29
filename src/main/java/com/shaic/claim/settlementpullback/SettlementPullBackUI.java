package com.shaic.claim.settlementpullback;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

public class SettlementPullBackUI extends ViewComponent {

	private static final long serialVersionUID = 1L;
	
	private Button submitButton;
	
	private Button cancelButton;
	
	private Panel mainPanel;
	
	private VerticalLayout mainLayout;
	
	private FormLayout formLayout;
	
	////private static Window popup;
	
	private HorizontalLayout submitButtonLayout;

	private TextField approvedAmtTxt;
	
	private TextField netPaymentAmtTxt;
	
	private TextField approverNameTxt;
	
	private TextField approvedDateTxt;
	
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
			String caption = "UNDO FA";
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
	    

	public void initView(SearchSettlementPullBackDTO dto, NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
		mainLayout = new VerticalLayout();
		submitButtonLayout = new HorizontalLayout();
		mainPanel = new Panel();
		
		approvedAmtTxt = new TextField("Approved Amount");
		approvedAmtTxt.setValue(dto.getApprovedAmount() != null ? dto.getApprovedAmount().toString() : "");
		approvedAmtTxt.setEnabled(false);
		
		netPaymentAmtTxt = new TextField("Net Payment Amount");
		netPaymentAmtTxt.setValue(dto.getApprovedAmount() != null ? dto.getApprovedAmount().toString() : "");
		netPaymentAmtTxt.setEnabled(false);
		
		approverNameTxt = new TextField("Approver Name");
		approverNameTxt.setValue(dto.getApproverName());
		approverNameTxt.setEnabled(false);
		
		approvedDateTxt = new TextField("Approved Date");
		approvedDateTxt.setValue(dto.getApprovedDate());
		approvedDateTxt.setEnabled(false);
		
		formLayout = new FormLayout(approvedAmtTxt, netPaymentAmtTxt, approverNameTxt, approvedDateTxt);
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
		
		
		viewDetails.initView(newIntimationDTO.getIntimationId(),dto.getRodKey(), ViewLevels.PREAUTH_MEDICAL,"Claim Final Approval Cancel");
		
		viewTrailsBtn = new Button("View History");

//		viewTrailsBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewTrailsBtn.setWidth("150px");
//		viewTrailsBtn.addStyleName(ValoTheme.BUTTON_LINK);
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
	private HorizontalLayout buildSubmitAndCancelBtnLayout(final SearchSettlementPullBackDTO dto) {

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
									"Are you sure you want to cancel the FA approval ?",
									"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												fireViewEvent(SettlementPullBackPresenter.SUBMIT_SETTLEMENT_PULL_BACK, dto);
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
												fireViewEvent(MenuItemBean.SETTLEMENT_PULL_BACK,
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
	
	/*private void buildSuccessfulLayout() {
		Button finalOKButton = new Button("OK");
		finalOKButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Label label = new Label("Cashless Record Cleared");
		
		VerticalLayout verti =new VerticalLayout(label,finalOKButton);
		verti.setMargin(true);
		verti.setSpacing(true);
		verti.setComponentAlignment(label, Alignment.TOP_CENTER);
		verti.setComponentAlignment(finalOKButton, Alignment.BOTTOM_CENTER);
		popup = new com.vaadin.ui.Window();
		popup.setWidth("20%");
		popup.setHeight("30%");
		popup.setContent(verti);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		
		finalOKButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				fireViewEvent(MenuItemBean.CLEAR_CASHLESS,
						null);
				
			}
		});
	}*/
}
