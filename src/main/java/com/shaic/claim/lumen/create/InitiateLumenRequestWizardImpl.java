package com.shaic.claim.lumen.create;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.upload.LumenUploadDocumentsViewImpl;
import com.shaic.domain.MasterService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class InitiateLumenRequestWizardImpl extends AbstractMVPView implements InitiateLumenRequestWizard{

	@Inject
	private Instance<InitiateLumenCarousel> carouselInstance;

	@Inject
	private ViewDetails viewDetails;

	private VerticalLayout mainPanel;
	private LumenSearchResultTableDTO dtoBean;
	private StringBuilder errMsg = new StringBuilder();

	private TextField txtEmployeeName;
	private TextField txtEmployeeId;
	private TextField txtIntimationNo;
	private ComboBox cmbLumenType;
	//private ComboBox cmbErrorType;
	private TextArea txtComments;

	@EJB
	private MasterService masterService;

	@Inject
	private LumenDbService lumenService;

	private Button uploadButton;

	@Inject
	private LumenUploadDocumentsViewImpl uploadDocumentView;

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();			
	}

	public void initView(LumenSearchResultTableDTO resultDTO) {
		this.dtoBean =null;
		this.dtoBean = resultDTO;
		mainPanel = new VerticalLayout();

		InitiateLumenCarousel lumenCarousel = carouselInstance.get();
		lumenCarousel.init(dtoBean, "Initiate Lumen Request");
		mainPanel.addComponent(lumenCarousel);

		viewDetails.initView(resultDTO.getIntimationNumber(), 0L, ViewLevels.LUMEN_TRAILS, false,"Lumen Trails Page loaded.........", false);

		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(viewDetails);
		horLayout.setSizeFull();
		horLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);

		uploadButton = new Button();
		uploadButton.setCaption("Upload Document (Lumen)");
		uploadButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showDocumentUploadView();
			}
		});

		HorizontalLayout uploadLayout = new HorizontalLayout();
		uploadLayout.addComponent(uploadButton);
		uploadLayout.setSizeFull();
		uploadLayout.setComponentAlignment(uploadButton, Alignment.TOP_RIGHT);
		mainPanel.addComponent(horLayout);
		mainPanel.addComponent(uploadLayout);
		mainPanel.addComponent(commonButtonsLayout());
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSizeFull();		

		setCompositionRoot(mainPanel);			
	}

	private AbsoluteLayout commonButtonsLayout(){

		txtEmployeeName = new TextField("Employee Name");
		txtEmployeeName.setValue(this.dtoBean.getEmpName());
		txtEmployeeName.setReadOnly(true);

		txtEmployeeId = new TextField("Employee ID");
		txtEmployeeId.setValue(this.dtoBean.getLoginId());
		txtEmployeeId.setReadOnly(true);

		txtIntimationNo = new TextField("Intimation Number");
		txtIntimationNo.setValue(this.dtoBean.getIntimationNumber());
		txtIntimationNo.setReadOnly(true);

		cmbLumenType = new ComboBox("Lumen Type");

		BeanItemContainer<SelectValue> lumenContainer = masterService.getLumenType();
		cmbLumenType.setContainerDataSource(lumenContainer);
		cmbLumenType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbLumenType.setItemCaptionPropertyId("value");
//		cmbLumenType.addValueChangeListener(new ValueChangeListener() {
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				SelectValue type = (SelectValue) event.getProperty().getValue();
//				if(type != null && type.getId() != null){
//					if(type.getValue().equals("Hospital Errors")){
//						// show hospital errors field
//						cmbErrorType.setVisible(true);
//					}else{
//						// hide hospital errors field
//						cmbErrorType.setVisible(false);
//					}
//				}else{
//					//On selecting empty value hospitals error drop-down should not be displayed.
//					cmbErrorType.setVisible(false);
//				}
//			}
//		});
//
//		cmbErrorType = new ComboBox("Hospital Errors");
//		BeanItemContainer<SelectValue> errorTypeContainer = masterService.getLumenHospitalErrorTypes();
//		cmbErrorType.setContainerDataSource(errorTypeContainer);
//		cmbErrorType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbErrorType.setItemCaptionPropertyId("value");
//		cmbErrorType.setVisible(false);

		txtComments = new TextArea("Comments");
		txtComments.setDescription("Click the Text Box and Press F8 for Detail Popup");
		txtComments.setWidth(30, Unit.EM);
		txtComments.setMaxLength(4000);
		handleTextAreaPopup(txtComments,null);

		FormLayout firstForm1 = new FormLayout(txtEmployeeName,txtEmployeeId,txtIntimationNo,cmbLumenType,txtComments);

		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1);
		hLayout1.setSizeFull();

		AbsoluteLayout searchlumen_layout =  new AbsoluteLayout();
		searchlumen_layout.addComponent(hLayout1, "left: 25%; top: 0%;");		
		searchlumen_layout.setWidth("100%");
		searchlumen_layout.setHeight("310px");
		return searchlumen_layout;
	}


	public AbsoluteLayout addFooterButtons(){
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(InitiateLumenRequestWizardPresenter.CANCEL_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					fireViewEvent(InitiateLumenRequestWizardPresenter.SUBMIT_REQUEST,dtoBean);
				}else{
					showErrorMessage(errMsg.toString());
				}				
			}
		});

		buttonsLayout.addComponents(cancelButton,submitButton);
		buttonsLayout.setSpacing(true);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 40%; top: 20%;");
		submit_layout.setWidth("100%");
		submit_layout.setHeight("50px");

		return submit_layout;
	}



	@Override
	public void resetView() {

	}

	public void cancelLumenRequest(){
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {

			private static final long serialVersionUID = 1L;
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isCanceled() && !dialog.isConfirmed()) {
					// YES
					fireViewEvent(MenuItemBean.CREATE_LUMEN, null);
				}
			}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	public void submitLumenRequest(){
		this.dtoBean.setLumenType((SelectValue)cmbLumenType.getValue());
//		if(cmbLumenType.getValue() != null && cmbLumenType.getValue().toString().equals("Hospital Errors")){
//			this.dtoBean.setLumenErrorType((SelectValue)cmbErrorType.getValue());
//		}
		this.dtoBean.setLumenRemarks(txtComments.getValue());
		try {
			String refNumber = lumenService.saveLumenRequest(this.dtoBean);
			String msg = "Lumen Request Submitted Successfully </br> Case Reference Number : <strong>"+refNumber+"</strong>";
			showSubmitMessagePanel(msg);
		} catch (Exception e) {
			String msg = "Exception Occurred While Creating Lumen Request";
			showSubmitMessagePanel(msg);
			e.printStackTrace();
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		if(cmbLumenType.getValue() == null){
			hasError = true;
			errMsg.append("Please select any Lumen Type </br>");
		}

//		if(String.valueOf(cmbLumenType.getValue()).equals("Hospital Errors") && cmbErrorType.getValue() == null){
//			hasError = true;
//			errMsg.append("Please select any Hospital Errors </br>");
//		}
		
		if(String.valueOf(cmbLumenType.getValue()).equals("Others") && StringUtils.isBlank(txtComments.getValue())){
			hasError = true;
			errMsg.append("Please provide the comments. </br>");
		}
		return hasError;
	}


	@SuppressWarnings("static-access")
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


	@SuppressWarnings("static-access")
	public void showSubmitMessagePanel(String messageInfo){
		Label label = new Label(messageInfo, ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
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
				fireViewEvent(MenuItemBean.CREATE_LUMEN, null);

			}
		});
	}

	private Button btnClose;

	public void showDocumentUploadView(){
		final Window popup = new com.vaadin.ui.Window();
		// include the content here like viewlumenTrialsTable
		uploadDocumentView.init(this.dtoBean,"Lumen Documents Upload");
		// close button in window
		btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
			}
		});

		HorizontalLayout closebutLayout = new HorizontalLayout();
		closebutLayout.addComponent(btnClose);
		closebutLayout.setSizeFull();
		closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);

		VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.addComponent(uploadDocumentView);
		uploadLayout.addComponent(closebutLayout);
		uploadLayout.setSpacing(true);
		uploadLayout.setMargin(true);


		popup.setCaption("Upload Documents");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(uploadLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

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
				txtArea.setMaxLength(4000);
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

				String strCaption = "Lumen Initiate Comments";

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
					//GALAXYMAIN-10545
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

}
