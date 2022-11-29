package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
//import com.shaic.claim.clearcashless.ClearCashlessPresenter;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ReconsiderationFlagRequestUI extends ViewComponent {

	private static final long serialVersionUID = 1L;
	
	private TextField rodNumber;
	
	private TextField rodStatus ;
	
	private TextArea rejectFlagRemarks ;
	
	private Button submitButton;
	
	private Button cancelButton;
	
	private Panel mainPanel;
	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout deatilHLayout;
	
	private FormLayout formLayout;
	
	private HorizontalLayout submitButtonLayout;
	
	private FormLayout formOneLayout;

	private OptionGroup clearCashlessReq;
	
	private NewIntimationDto newIntimationDTO;
	
	private ClaimDto claimDTO;
	
	private SearchFlagReconsiderationReqTableDTO flagReconsiderationReqTableDTO;
	
	private Label detailLabel;
	
	private CheckBox alertEnable;
	
	private ArrayList<Component> mandatoryFields;
	
	@Inject
	private Instance<RevisedCarousel> carouselInstance;
	
	@Inject
	private ViewDetails viewDetails;


	@PostConstruct
	public void init() {

	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			if (field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}
	
	 private Panel buildRegistrationPanel() {
			// common part: create layout
			Panel registrationPanel = new Panel();
			HorizontalLayout panelCaption = new HorizontalLayout();

			panelCaption.addStyleName(ValoTheme.PANEL_WELL);
			panelCaption.setSpacing(true);
			panelCaption.setWidth("100%");
			panelCaption.setMargin(new MarginInfo(false, true, false, true));
			
			//Vaadin8-setImmediate() registrationPanel.setImmediate(false);
			registrationPanel.setWidth("100%");
			// registrationPanel.setHeight("130px");
			registrationPanel.addStyleName("panelHeader");
			RevisedCarousel intimationDetailsCarousel = carouselInstance.get();
			intimationDetailsCarousel.init (newIntimationDTO,claimDTO, "Flag Reconsideration Request");

			VerticalLayout vlayout = new VerticalLayout();
			vlayout.addComponent(panelCaption);
			vlayout.addComponent(intimationDetailsCarousel);
			vlayout.setStyleName("policygridinfo");
			registrationPanel.setContent(vlayout);

			return registrationPanel;
		}
	    

	public void initView(SearchFlagReconsiderationReqTableDTO flagReconsiderationReqdto, NewIntimationDto newIntimationDTO, ClaimDto claimDto) {
		this.newIntimationDTO = newIntimationDTO;
		this.claimDTO= claimDto;
		this.flagReconsiderationReqTableDTO = flagReconsiderationReqdto;
		
		viewDetails.initView(this.newIntimationDTO.getIntimationId(),flagReconsiderationReqdto.getRodKey(), ViewLevels.INTIMATION, false,"Flag Reconsideration Request");
		mandatoryFields = new ArrayList<Component>();
		mainLayout = new VerticalLayout();
		
		submitButtonLayout = new HorizontalLayout();
		mainPanel = new Panel();
		
		HorizontalLayout buttonLayout1 = buildSubmitAndCancelBtnLayout(flagReconsiderationReqdto);
		submitButtonLayout.addComponent(buttonLayout1);
		submitButtonLayout.setWidth("100%");
		submitButtonLayout.setSpacing(true);
		submitButtonLayout.setMargin(true);
		submitButtonLayout.setComponentAlignment(buttonLayout1,
				Alignment.MIDDLE_CENTER);

		
		mainLayout.addComponent(buildRegistrationPanel());
		mainLayout.addComponent(commonButtonsLayout());
		mainLayout.addComponent(detailFormLayout());
		setDataToForm(flagReconsiderationReqdto);
		mainLayout.addComponent(submitButtonLayout);
		mainLayout.setComponentAlignment(submitButtonLayout, Alignment.BOTTOM_CENTER);
		mainPanel.setWidth("100%");
		mainPanel.setHeight("620px");
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);

	}
	
	private HorizontalLayout buildSubmitAndCancelBtnLayout(final SearchFlagReconsiderationReqTableDTO dto) {

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
					if(validatePage()) {
					
					ConfirmDialog dialog = ConfirmDialog
							.show(getUI(),
									"Confirmation",
									"Are you sure you want to Submit ?",
									"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												
												if(rejectFlagRemarks.getValue() != null){
													dto.setRejectRemarks(rejectFlagRemarks.getValue());
													if(alertEnable.getValue()){
															dto.setCrmFlagged("Y");
													}else{
															dto.setCrmFlagged("N");
													}
												}
												fireViewEvent(ReconsiderationFlagRequestPresenter.SUBMIT_RECONSIDERATION_FLAG_REQUEST, dto);
													
												} else {
													// User did not confirm
												}
											}
									
									});
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
					
					}
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
												fireViewEvent(MenuItemBean.FLAG_RECONSIDERATION_REQUEST, null);
												deatilHLayout.removeAllComponents();
//											
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

	public HorizontalLayout commonButtonsLayout()
	{
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(viewDetails);
		horizontalLayout.setComponentAlignment(viewDetails,Alignment.TOP_RIGHT);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		return horizontalLayout;
	}
	
	public HorizontalLayout detailFormLayout()
	{
		HorizontalLayout horizontalFormoneLayout = new HorizontalLayout();

		formOneLayout = new FormLayout();
		
		rodNumber = new TextField("ROD Number &nbsp; : &nbsp;");
		rodNumber.setCaptionAsHtml(true);
		rodNumber.setEnabled(false);
		rodNumber.setWidthUndefined();
		rodNumber.setNullRepresentation("");
		
		
		FormLayout rodnoFormLayout = new FormLayout();
		rodnoFormLayout.addComponent(rodNumber);
		
		rodStatus = new TextField("ROD Status  &nbsp; : &nbsp;");
		rodStatus.setCaptionAsHtml(true);
		rodStatus.setNullRepresentation("");
		rodStatus.setEnabled(false);
		

		FormLayout rodStatusFormLayout = new FormLayout();
		rodStatusFormLayout.addComponent(rodStatus);

		
		horizontalFormoneLayout.addComponent(rodnoFormLayout);
		
		TextField dummytext = new TextField("");
		dummytext.setEnabled(false);
		dummytext.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		FormLayout dumbLayout = new FormLayout(dummytext);
		horizontalFormoneLayout.addComponent(dumbLayout);
		
		horizontalFormoneLayout.addComponent(rodStatusFormLayout);
		horizontalFormoneLayout.setMargin(true);
		horizontalFormoneLayout.setSpacing(true);
		horizontalFormoneLayout.setComponentAlignment(rodnoFormLayout,Alignment.MIDDLE_LEFT);
		horizontalFormoneLayout.setComponentAlignment(rodStatusFormLayout,Alignment.MIDDLE_RIGHT);
		
		HorizontalLayout horizontalFormsecLayout = new HorizontalLayout();
		
		alertEnable = new CheckBox();
		detailLabel = new Label("<B>Alert Enable &nbsp; : &nbsp;&nbsp;&nbsp; </B>",ContentMode.HTML);
		HorizontalLayout alertchekLayout = new HorizontalLayout(detailLabel, alertEnable);
		alertchekLayout.setMargin(true);
		alertchekLayout.setSpacing(true);
		FormLayout alertEnableFormLayout = new FormLayout();
		alertEnableFormLayout.addComponent(alertchekLayout);
		alertEnableFormLayout.setComponentAlignment(alertchekLayout, Alignment.MIDDLE_LEFT);
		addListener(alertEnable);
	
		
		Label remarkLabel = new Label("Remarks <B style=color:red;> * </B>  &nbsp; : &nbsp;&nbsp;",ContentMode.HTML);
		rejectFlagRemarks = new TextArea();
		rejectFlagRemarks.setWidth("250px");
		rejectFlagRemarks.setMaxLength(500);
		rejectFlagRemarks.setNullRepresentation("");
		rejectFlagRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		handleTextAreaPopup(rejectFlagRemarks,null);
		HorizontalLayout rejectFlagLayout = new HorizontalLayout(remarkLabel, rejectFlagRemarks);
		rejectFlagLayout.setSpacing(true);
		rejectFlagLayout.setMargin(true);
		
		FormLayout rejectFlagRemarksFormLayout = new FormLayout();
		rejectFlagRemarksFormLayout.addComponent(rejectFlagLayout);
		
		horizontalFormsecLayout.addComponent(alertEnableFormLayout);
		horizontalFormsecLayout.setComponentAlignment(alertEnableFormLayout, Alignment.MIDDLE_LEFT);
		
		TextField dummytext1 = new TextField("");
		dummytext1.setEnabled(false);
		dummytext1.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		FormLayout dumbLayout1 = new FormLayout(dummytext1);
		dumbLayout1.setMargin(true);
		dumbLayout1.setSpacing(true);
	
		horizontalFormsecLayout.addComponent(dumbLayout1);
		
		horizontalFormsecLayout.addComponent(rejectFlagRemarksFormLayout);
		horizontalFormsecLayout.setMargin(true);
		horizontalFormsecLayout.setSpacing(true);
		horizontalFormsecLayout.setComponentAlignment(alertEnableFormLayout,Alignment.MIDDLE_LEFT);
		horizontalFormsecLayout.setComponentAlignment(rejectFlagRemarksFormLayout,Alignment.MIDDLE_RIGHT);
		
		
		formOneLayout.addComponent(horizontalFormoneLayout);
		formOneLayout.addComponent(horizontalFormsecLayout);
		formOneLayout.setMargin(true);
		formOneLayout.setSpacing(true);
		deatilHLayout = new HorizontalLayout(formOneLayout);
		
		
		return deatilHLayout;
	}
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();					
					if(value)
					{
						rejectFlagRemarks.setEnabled(true);
						if(flagReconsiderationReqTableDTO.getRejectRemarks() != null && !flagReconsiderationReqTableDTO.getRejectRemarks().isEmpty()){
							rejectFlagRemarks.setValue(flagReconsiderationReqTableDTO.getRejectRemarks());
						}
					}else
					{
						rejectFlagRemarks.setEnabled(false);
						rejectFlagRemarks.clear();
					}
					
				}
			}
		});
	}
	
	@SuppressWarnings("unused")
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
		ShortcutListener listener =  new ShortcutListener("CRM Flagged Remarks",KeyCodes.KEY_F8,null) {

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

				String strCaption = "Remarks";

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
					dialog.setPositionX(450);
					dialog.setPositionY(500);
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
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		if (rejectFlagRemarks != null
				&& rejectFlagRemarks.getValue() == null
				|| (rejectFlagRemarks.getValue() != null && rejectFlagRemarks
						.getValue().length() == 0)
						&& alertEnable.getValue() != null && alertEnable.getValue()) {
			hasError = true;
			eMsg.append("Please Enter Remarks </br>");
		}
		
		
		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		}
		
		return true;
				
	}

	private void setDataToForm(SearchFlagReconsiderationReqTableDTO flagReconsiderationReqdto){
		if(flagReconsiderationReqdto != null){
			rodNumber.setValue(flagReconsiderationReqTableDTO.getRodNumber());
			rodStatus.setValue(flagReconsiderationReqTableDTO.getRodStatus());
			rejectFlagRemarks.setValue(flagReconsiderationReqTableDTO.getRejectRemarks());
			alertEnable.setValue(true);
			
			if(flagReconsiderationReqdto.getFlagStatus() != null && flagReconsiderationReqdto.getFlagStatus().equalsIgnoreCase("N")){
				alertEnable.setValue(false);
				rejectFlagRemarks.setEnabled(false);
				rejectFlagRemarks.clear();
			}
			
			if(flagReconsiderationReqdto.getDisableReconsiderationReq() != null && flagReconsiderationReqdto.getDisableReconsiderationReq().equalsIgnoreCase("Y")){
				submitButton.setEnabled(false);
				if(flagReconsiderationReqdto.getFlagStatus() != null && flagReconsiderationReqdto.getFlagStatus().equalsIgnoreCase("Y")){
					alertEnable.setValue(true);
					rejectFlagRemarks.setValue(flagReconsiderationReqTableDTO.getRejectRemarks());
				}else{
					alertEnable.setValue(false);
				}
				alertEnable.setEnabled(false);
				rejectFlagRemarks.setEnabled(false);
			}
		}
	}
}
