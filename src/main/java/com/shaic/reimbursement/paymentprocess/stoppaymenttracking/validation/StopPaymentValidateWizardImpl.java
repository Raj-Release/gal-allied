package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.keycloak.representations.AddressClaimSet;
import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;


public abstract class StopPaymentValidateWizardImpl extends AbstractMVPView implements PopupStopPaymentValidateWizard{



	private VerticalLayout mainPanel;
	private StopPaymentRequestDto dtoBean;
	private StringBuilder errMsg = new StringBuilder();


	private ComboBox cmbActionTaken;
	private TextArea txtComments;
	private TextField forCaption;
	Button forTracking;
	private Window popup;
	
	@Inject
	private RevisedCarousel revisedCarousel;
	
	@Inject
	private StopPaymentTrackingTable trailForStopPayment;
	
	public void initView(StopPaymentRequestDto result, Window popup) {
		this.dtoBean=result;
		this.popup=popup;
		mainPanel = new VerticalLayout();
		mainPanel.removeAllComponents();
		
		revisedCarousel.init(result.getPreauthDto().getNewIntimationDTO(), "xyz");
		Panel p=new Panel();
		VerticalLayout forSpace= new VerticalLayout();
		VerticalLayout vertical=new VerticalLayout();
		Label caption = new Label("<b style='font-size:x-large;padding-left:9px'>Stop Payment Validation</b>",ContentMode.HTML);

		/*TextField trmp=new TextField("");
		trmp.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		trmp.setReadOnly(true);*/
		
		HorizontalLayout forCap = new HorizontalLayout();
		//forCap.addComponent(trmp);
		forCap.addComponent(caption);
		forCap.setComponentAlignment(caption, Alignment.TOP_LEFT);
		vertical.addComponent(forCap);
		
		forTracking=new Button("Stop Payment Tracking");
		/*HorizontalLayout forTrc = new HorizontalLayout(trmp,forTracking);
		forTrc.setComponentAlignment(forTracking, Alignment.MIDDLE_LEFT);*/
		AbsoluteLayout sPlayout =  new AbsoluteLayout();
		sPlayout.addComponent(forTracking, "left: 1%; top: 20%;");
		sPlayout.setWidth("100%");
		sPlayout.setHeight("50px");
		
		mainPanel.addComponent(revisedCarousel);
		mainPanel.addComponent(p);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(forSpace);
		mainPanel.addComponent(vertical);
		mainPanel.addComponent(sPlayout);
		mainPanel.addComponent(commonTextLayout());
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSizeFull();		

		setCompositionRoot(mainPanel);	
		addListener();
		
	}

	
	private void addListener() {
	    forTracking.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				        trailForStopPayment.init("", false, false);
						fireViewEvent(PopupStopPaymentValidationWizardPresenter.GET_STOP_PAYMENT_TRACKING_TRIALS, dtoBean);								
					
			}
		});
		
	}


	private Component addFooterButtons() {

		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PopupStopPaymentValidationWizardPresenter.POPUP_CANCEL_VALIDATION_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					if(cmbActionTaken.getValue()!= null && cmbActionTaken.getValue().toString().equals("Approve")){
						dtoBean.setForActionTaken("A");
					}
					if(cmbActionTaken.getValue()!= null && cmbActionTaken.getValue().toString().equals("Reject")){
						dtoBean.setForActionTaken("R");
					}
					dtoBean.setStopPaymentResReamrks(txtComments.getValue());
					
					fireViewEvent(PopupStopPaymentValidationWizardPresenter.POPUP_SUBMIT_VALIDATION_REQUEST,dtoBean);
				}else{
					showErrorMessage(errMsg.toString());
				}				
			}
		});

		buttonsLayout.addComponents(submitButton,cancelButton);
		buttonsLayout.setSpacing(true);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 40%; top: 20%;");
		submit_layout.setWidth("100%");
		submit_layout.setHeight("50px");

		return submit_layout;
	
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}



	private Component commonTextLayout() {


		cmbActionTaken = new ComboBox("Action to be Taken");

		BeanItemContainer<SelectValue> actionContainer = getReasonForStopPaymentType();
		cmbActionTaken.setContainerDataSource(actionContainer);
		cmbActionTaken.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbActionTaken.setItemCaptionPropertyId("value");
		cmbActionTaken.setRequired(true);

		txtComments = new TextArea("Stop Payment Response remarks");
		txtComments.setDescription("Click the Text Box and Press F8 for Detail Popup");
		txtComments.setWidth(20, Unit.EM);
		txtComments.setMaxLength(500);
		txtComments.setRequired(true);
		handleTextAreaPopup(txtComments,null);
		
		FormLayout firstForm1 = new FormLayout(cmbActionTaken,txtComments);
		

		

		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1);
		hLayout1.setSizeFull();

		AbsoluteLayout searchlumen_layout =  new AbsoluteLayout();
		searchlumen_layout.addComponent(hLayout1, "left: 5%; top: 0%;");		
		searchlumen_layout.setWidth("100%");
		searchlumen_layout.setHeight("310px");
		return searchlumen_layout;
	
	}
	
	@SuppressWarnings("deprecation")
	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		
		if((cmbActionTaken!=null && cmbActionTaken.getValue()==null || (cmbActionTaken.getValue() != null && cmbActionTaken.getValidators().toString().length() ==0)) ||
				(txtComments!=null && txtComments.getValue()==null || (txtComments.getValue() != null && txtComments.getValue().toString().trim().equals("")))){
			hasError = true;
			errMsg.append("Please enter all the fields </br>");
		}
		return hasError;
	}
	
	@SuppressWarnings("deprecation")
	private BeanItemContainer<SelectValue> getReasonForStopPaymentType() {
		
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(2l);
		selectValue2.setValue("Approve");
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(3l);
		selectValue3.setValue("Reject");
		container.addBean(selectValue2);
		container.addBean(selectValue3);
		
		container.sort(new Object[] {"value"}, new boolean[] {true});
		
		return container;
	}


	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	@SuppressWarnings("deprecation")
	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Redraft Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(500);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());		
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Stop Payment Response-Remarks";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(350);
					dialog.setPositionY(50);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void cancelStopPaymentValidate() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SearchStopPaymentValidationPresenter.RESET_SEARCH_VIEW,null);
				UI.getCurrent().removeWindow(popup);
			}
			});
		
	}


	@Override
	public void submitStopPaymentValidate() {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Stop Payment Validation Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Request Submitted Successfully", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SearchStopPaymentValidationPresenter.RESET_SEARCH_VIEW,null);
				UI.getCurrent().removeWindow(popup);

			}
		});
		
	
	}


	@Override
	public void showTrackingTrails(
			List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {

		
		trailForStopPayment.setTableList(viewStopPaymentTrackingTableList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Stop Payment Tracking-Detailed View");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(trailForStopPayment);				
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


	


	

}
