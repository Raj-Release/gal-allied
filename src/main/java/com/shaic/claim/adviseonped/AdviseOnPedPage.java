package com.shaic.claim.adviseonped;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.preauth.InitiatePEDEndorsementTable;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class AdviseOnPedPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7031159256210713639L;

	@Inject
	private Instance<InitiatePEDEndorsementTable> initiatePEDEndorsementTable;

	private InitiatePEDEndorsementTable initiatePEDEndorsementTableInstance;

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private OldPedEndorsementDTO tableRows;

	private HorizontalLayout intimatePEDFLayout;

	private DateField txtRepudiationLetterDate;

	private TextArea txtSpecialistRemarks;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	@Inject
	private UploadedFileViewUI fileViewUI;

	private ClaimDto claimDto;

	@Inject
	private ViewDetails viewDetails;

	private File file;

	// @Inject
	// private Instance<PEDQueryEditableTable> initiatePEDEndorsementTable;
	//
	// private PEDQueryEditableTable initiatePEDEndorsementObj;

	@Inject
	private OldPedEndorsementDTO bean;

	private OldPedEndorsementDTO submitBean;

	private BeanFieldGroup<OldPedEndorsementDTO> binder;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	private Button submitBtn;

	private Button cancelBtn;

	private HorizontalLayout buttonHorLayout;

	private Button viewFileBtn;

	private BeanItemContainer<SelectValue> selectValueContainer;

	private Upload fileUpload;

	private NewIntimationDto intimationDto;

	private ComboBox txtPEDSuggestion;

	private SearchAdviseOnPEDTableDTO searchDTO;

	private Button tmpViewBtn;

	@PostConstruct
	public void initView() {

	}

	public void initView(SearchAdviseOnPEDTableDTO searchDTO,
			OldPedEndorsementDTO editDTO) {

		this.searchDTO = searchDTO;
		// this.bean=bean;
		if (editDTO == null) {
			fireViewEvent(AdviseOnPEDPresenter.SET_FIELD_DATA, searchDTO);
		} else {
			fireViewEvent(AdviseOnPEDPresenter.SET_EDIT_DATA, editDTO);
		}
		this.binder = new BeanFieldGroup<OldPedEndorsementDTO>(
				OldPedEndorsementDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		txtPEDSuggestion = (ComboBox) binder.buildAndBind("PED Suggestion",
				"pedSuggestion", ComboBox.class);

		txtPEDSuggestion.setContainerDataSource(selectValueContainer);
		txtPEDSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtPEDSuggestion.setItemCaptionPropertyId("value");
		txtPEDSuggestion.setEnabled(false);

		txtPEDSuggestion.setValue(this.bean.getPedSuggestion());

		try {
			this.binder.commit();
			this.bean = this.binder.getItemDataSource().getBean();
			if (this.submitBean == null) {
				this.submitBean = this.bean;
			}

		} catch (CommitException e) {
			e.printStackTrace();
		}
		System.out.println("Intimation key====" + this.bean.getIntimationKey());

		HorizontalLayout IntimatePEDLayout = buildIntimatePEDFLayout();

		if (initiatePEDEndorsementTableInstance == null) {
			initiatePEDEndorsementTableInstance = initiatePEDEndorsementTable
					.get();
			initiatePEDEndorsementTableInstance.init("", false);
		}

		initiatePEDEndorsementTableInstance.setReference(this.referenceData);

		initiatePEDEndorsementTableInstance.setTableList(this.tableRows
				.getNewInitiatePedEndorsementDto());

		VerticalLayout editVeritical = new VerticalLayout(
				initiatePEDEndorsementTableInstance);

		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");

		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");

		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");

		tmpViewBtn = new Button("View File");
		tmpViewBtn.setEnabled(false);
		tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
		// tmpViewBtn.setWidth("50%");

		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
		buttonHorLayout.setSpacing(true);

		if (txtPEDSuggestion.getValue().toString().toLowerCase()
				.contains("sug 002 - cancel policy")) {
			intimatePEDFLayout.addComponent(txtRepudiationLetterDate);
		} else {
			unbindField(txtRepudiationLetterDate);
			intimatePEDFLayout.removeComponent(txtRepudiationLetterDate);
		}

		VerticalLayout verticalLayout = new VerticalLayout(IntimatePEDLayout,
				editVeritical, buildPEDRefference(), buttonHorLayout);
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.setComponentAlignment(buttonHorLayout,
				Alignment.BOTTOM_CENTER);

		addListener();

		setCompositionRoot(verticalLayout);

	}

	private HorizontalLayout buildIntimatePEDFLayout() {

		TextField txtNameofPED = (TextField) binder.buildAndBind("Name of PED",
				"pedName", TextField.class);
		txtNameofPED.setRequired(true);
		txtNameofPED.setReadOnly(true);
		txtNameofPED.setNullRepresentation("");

		TextField txtRemarks = (TextField) binder.buildAndBind("Remarks",
				"remarks", TextField.class);
		txtRemarks.setRequired(true);
		txtRemarks.setReadOnly(true);
		txtRemarks.setNullRepresentation("");

		txtRepudiationLetterDate = (DateField) binder.buildAndBind(
				"Repudiation Letter Date", "repudiationLetterDate",
				DateField.class);
		txtRepudiationLetterDate.setEnabled(false);

		intimatePEDFLayout = new HorizontalLayout(txtPEDSuggestion,
				txtNameofPED, txtRemarks);
		intimatePEDFLayout.setSpacing(true);

		return intimatePEDFLayout;
	}

	private HorizontalLayout buildPEDRefference() {

		TextArea txtReasonforReferring = (TextArea) binder.buildAndBind(
				"Reason for Referring", "reasonforReferring", TextArea.class);
		txtReasonforReferring.setRequired(true);
		txtReasonforReferring.setNullRepresentation("");
		txtReasonforReferring.setReadOnly(true);
		txtReasonforReferring.setWidth("400px");
		txtReasonforReferring.setId("RefReason");
		txtReasonforReferring.setData(bean);
		txtReasonforReferring.setDescription("Click the Text Box and Press F8 For Detail Popup");
		handlePopupForReasonForRefRemarks(txtReasonforReferring, null);

		FormLayout form1 = new FormLayout(txtReasonforReferring);
		form1.setMargin(false);

		TextField txtViewFile = (TextField) binder.buildAndBind("View File",
				"viewFile", TextField.class);
		// txtViewFile.setRequired(true);
		txtViewFile.setNullRepresentation("");

		viewFileBtn = new Button("");
		viewFileBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewFileBtn.addStyleName(ValoTheme.BUTTON_LINK);
		viewFileBtn.setIcon(FontAwesome.FILE);
		viewFileBtn.setEnabled(false);

		if (this.bean.getFileName() != null) {
			viewFileBtn.setCaption(this.bean.getFileName());
			viewFileBtn.setEnabled(true);
		}

		Label viewLabel = new Label("View File");

		HorizontalLayout horLayout = new HorizontalLayout(viewLabel,
				viewFileBtn);
		horLayout.setSpacing(true);
		horLayout.setWidth("280px");

		unbindField(txtSpecialistRemarks);

		txtSpecialistRemarks = (TextArea) binder.buildAndBind(
				"Specialist Remarks", "specialistRemarks", TextArea.class);
		txtSpecialistRemarks.setRequired(true);
		txtSpecialistRemarks.setMaxLength(2500);
		txtSpecialistRemarks.setWidth("400px");
		txtSpecialistRemarks.setId("specRem");
//		bean.setSpecialistRemarks(txtSpecialistRemarks.getValue());
		txtSpecialistRemarks.setData(bean);
		handlePopupForReasonForRefRemarks(txtSpecialistRemarks, null);
		txtSpecialistRemarks.setDescription("Click the Text Box and Press F8 For Detail Popup");
		
//		txtSpecialistRemarks.addValueChangeListener(new ValueChangeListener() {
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//
//				OldPedEndorsementDTO mainDto = (OldPedEndorsementDTO) txtSpecialistRemarks
//						.getData();
//				mainDto.setSpecialistRemarks(txtSpecialistRemarks.getValue());
//			}
//		});
		
		
		
		// TextField txtFileUpload = (TextField) binder.buildAndBind(
		// "File Upload", "uploadFile", TextField.class);
		// txtFileUpload.setRequired(true);
		// txtFileUpload.setNullRepresentation("");

		fileUpload = new Upload("", new Receiver() {

			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
				FileOutputStream fos = null; // Stream to write to
				try {
					// Open the file for writing.
					if (filename != null && !filename.equalsIgnoreCase("")) {

						file = new File(System
								.getProperty("jboss.server.data.dir")
								+ "/"
								+ filename);
						fos = new FileOutputStream(file);
					} else {
						// fileUpload.setComponentError(null);
						// fireViewEvent(AdviseOnPEDPresenter.SUBMIT_BUTTON,searchDTO,bean);
					}
					// if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
					// fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT,
					// this.key,filename);
					// }
				} catch (final java.io.FileNotFoundException e) {
					new Notification("Could not open file<br/>",
							e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(com.vaadin.server.Page.getCurrent());
					return null;
				}
				return fos; // Return the output stream to write to
			}
		});
		fileUpload.addSucceededListener(new SucceededListener() {

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());

				try {

					byte[] fileAsbyteArray = FileUtils
							.readFileToByteArray(file);

					if (null != fileAsbyteArray) {

						Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						boolean hasSpecialChar = p.matcher(event.getFilename())
								.find();
						// if(hasSpecialChar)
						// {
						WeakHashMap uploadStatus = SHAFileUtils
								.sendFileToDMSServer(event.getFilename(),
										fileAsbyteArray);
						Boolean flagUploadSuccess = new Boolean(""
								+ uploadStatus.get("status"));
						// TO read file after load
						if (flagUploadSuccess.booleanValue()) {
							String token = "" + uploadStatus.get("fileKey");
							String fileName = event.getFilename();
							bean.setTokenName(token);
							bean.setFileName(fileName);
							tmpViewBtn.setEnabled(true);
							buildSuccessLayout();
							// fireViewEvent(AdviseOnPEDPresenter.SUBMIT_BUTTON,searchDTO,bean);
							// thisObj.close();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		fileUpload.setCaption("File Upload");
		fileUpload.setButtonCaption(null);
		FormLayout uploadformlayout = new FormLayout(fileUpload);
		uploadformlayout.setMargin(false);

		// VerticalLayout pedRefferenceFLayout = new
		// VerticalLayout(txtReasonforReferring,
		// viewFileBtn, txtSpecialistRemarks, fileUpload);
		// pedRefferenceFLayout.setSpacing(true);

		Button uploadBtn = new Button("Upload");
		HorizontalLayout fileUploadhori = new HorizontalLayout(
				uploadformlayout, uploadBtn, tmpViewBtn);

		uploadBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fileUpload.submitUpload();
			}
		});

		TextField dummyText = new TextField();
		dummyText.setEnabled(false);
		dummyText.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText.setHeight("100%");

		TextField dummyText2 = new TextField();
		dummyText2.setEnabled(false);
		dummyText2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText2.setHeight("100%");

		TextField dummyText3 = new TextField();
		dummyText3.setEnabled(false);
		dummyText3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText3.setHeight("100%");

		TextField dummyText4 = new TextField();
		dummyText4.setEnabled(false);
		dummyText4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText4.setHeight("100%");

		TextField dummyText5 = new TextField();
		dummyText5.setEnabled(false);
		dummyText5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText5.setHeight("100%");

		TextField dummyText6 = new TextField();
		dummyText6.setEnabled(false);
		dummyText6.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText6.setHeight("100%");

		TextField dummyText7 = new TextField();
		dummyText7.setEnabled(false);
		dummyText7.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText7.setHeight("100%");

		TextField dummyText8 = new TextField();
		dummyText8.setEnabled(false);
		dummyText8.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText8.setHeight("100%");

		tmpViewBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (bean.getFileName() != null && bean.getTokenName() != null) {
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.setCurrentPage(getUI().getPage());
					fileViewUI.init(popup, bean.getFileName(),
							bean.getTokenName());
					popup.setContent(fileViewUI);
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
		});

		// FormLayout secondForm = new
		// FormLayout(dummyText,dummyText2,dummyText3,dummyText4,dummyText5,dummyText6,dummyText7,uploadBtn);
		// secondForm.setComponentAlignment(uploadBtn, Alignment.BOTTOM_LEFT);
		//
		// HorizontalLayout secondHor = new
		// HorizontalLayout(secondForm,tmpViewBtn);
		// secondHor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
		// secondHor.setWidth("50%");
		FormLayout txtSpecialisform = new FormLayout(txtSpecialistRemarks);
		VerticalLayout form2 = new VerticalLayout(txtSpecialisform,
				fileUploadhori);
		form2.setSpacing(true);
		form2.setMargin(false);
		VerticalLayout pedRefferenceFLayout = new VerticalLayout(form1,
				horLayout, form2);
		pedRefferenceFLayout.setSpacing(false);
		pedRefferenceFLayout.setMargin(false);

		HorizontalLayout mainHor = new HorizontalLayout(pedRefferenceFLayout);
		mainHor.setWidth("90%");

		mandatoryFields.add(txtSpecialistRemarks);

		showOrHideValidation(false);

		return mainHor;
	}

	public void handlePopupForSpecialistRemarks(TextArea searchField,
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForSpecialistRemarks", ShortcutAction.KeyCode.F7, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForSpecialist(searchField,
				getShortCutListenerForSpecialistRemarks(searchField));

	}

	public void handleShortcutForSpecialist(final TextArea textField,
			final ShortcutListener shortcutListener) {
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

	private ShortcutListener getShortCutListenerForSpecialistRemarks(
			final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("Specialist Remarks",
				KeyCodes.KEY_F7, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				OldPedEndorsementDTO pedDTO = (OldPedEndorsementDTO) txtFld
						.getData();
				VerticalLayout vLayout = new VerticalLayout();

				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setData(bean);
				
				txtArea.setValue(((TextArea)target).getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setRows(25);
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						OldPedEndorsementDTO mainDto = (OldPedEndorsementDTO) txtArea
								.getData();
						mainDto.setSpecialistRemarks(txtArea.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				dialog.setCaption("Specialist Remarks");
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
						TextArea txtArea = (TextArea) dialog.getData();
						txtArea.setValue(bean.getSpecialistRemarks());
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						TextArea txtArea = (TextArea) dialog.getData();
						txtArea.setValue(bean.getSpecialistRemarks());
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	public void handlePopupForReasonForRefRemarks(TextArea searchField,   
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForReasonForRefRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRequester(searchField,
				getShortCutListenerForReasonforRefRemarks(searchField));

	}

	private ShortcutListener getShortCutListenerForReasonforRefRemarks(
			final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("ReasonForReferringRemarks",
				KeyCodes.KEY_F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				
				OldPedEndorsementDTO specialistDTO = (OldPedEndorsementDTO) txtFld
						.getData();
				VerticalLayout vLayout = new VerticalLayout();

				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setValue(txtFld.getValue());
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				
				if(("RefReason").equalsIgnoreCase(txtFld.getId())){
					if (specialistDTO.getReasonforReferring() != null) {
						txtArea.setRows(specialistDTO.getReasonforReferring()
								.length() / 80 >= 25 ? 25 : ((specialistDTO.getReasonforReferring().length() / 80) % 25) + 1);
					} else {
						txtArea.setRows(25);
					}
					txtArea.setReadOnly(true);
				}
				else{
					txtArea.setRows(25);
					txtArea.setReadOnly(false);
				}
				
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						if(txtFld.getId().equalsIgnoreCase("specRem")){
							txtFld.setValue(txtArea.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				
				String caption = "";
				if(txtFld.getId().equalsIgnoreCase("specRem")){
					caption = "Specialist Remarks";
				}
				else{
					caption = "Reason For Referring";
				}				
				dialog.setCaption(caption);
				
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.setDraggable(true);
				
				dialog.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);

				okBtn.addClickListener(new ClickListener() {
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

	public void handleShortcutForRequester(final TextArea textField,
			final ShortcutListener shortcutListener) {
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

	public void addListener() {

		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure You want to Cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {

									VaadinSession session = getSession();
									SHAUtils.releaseHumanTask(
											searchDTO.getUsername(),
											searchDTO.getPassword(),
											searchDTO.getTaskNumber(), session);

									fireViewEvent(MenuItemBean.ADVISE_ON_PED,
											true);
								} else {
									dialog.close();
								}
							}
						});
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});

		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				Boolean hasError = false;
				try {
					binder.commit();
					bean = binder.getItemDataSource().getBean();
				} catch (CommitException e) {
					e.printStackTrace();
				}

				System.out.println("Specialist remarks---->"
						+ bean.getSpecialistRemarks());

				if (txtSpecialistRemarks.getValue() == null) {

					hasError = true;

				} else if (txtSpecialistRemarks.getValue().equals("")) {
					hasError = true;
				}

				if (validatePage(hasError)) {
					// bean.setSpecialistRemarks(txtSpecialistRemarks.getValue());
					fireViewEvent(AdviseOnPEDPresenter.SUBMIT_BUTTON,
							searchDTO, bean);
				}
			}
		});

		viewFileBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				fileViewUI.init(popup, bean.getFileName(), bean.getTokenName());
				popup.setContent(fileViewUI);
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
		txtPEDSuggestion.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (txtPEDSuggestion.getValue().toString().toLowerCase()
						.contains("sug 002 - cancel policy")) {
					intimatePEDFLayout.addComponent(txtRepudiationLetterDate);
				} else {
					unbindField(txtRepudiationLetterDate);
					intimatePEDFLayout
							.removeComponent(txtRepudiationLetterDate);
				}
			}
		});
	}

	public void setReferenceData(OldPedEndorsementDTO bean,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto, Map<String, Object> referenceData,
			OldPedEndorsementDTO tableRows, ClaimDto claimDto) {

		this.bean = bean;
		this.tableRows = tableRows;
		this.selectValueContainer = selectValueContainer;
		this.intimationDto = intimationDto;
		this.referenceData = referenceData;
		this.claimDto = claimDto;

	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	private boolean validatePage(Boolean hasError) {
		// Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";

		if (hasError) {
			setRequired(true);
			Label label = new Label("Please Enter specialist Remarks",
					ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		}
		showOrHideValidation(false);
		return true;
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			if (binder.getPropertyId(field) != null) {
				this.binder.unbind(field);
			}

		}
	}

	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
			}
		});
	}

}
