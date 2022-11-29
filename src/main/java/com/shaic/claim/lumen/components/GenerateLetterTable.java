package com.shaic.claim.lumen.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.claim.ReportDto;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class GenerateLetterTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<LetterDetailsDTO, HashMap<String, Field<?>>> tableItem = new HashMap<LetterDetailsDTO, HashMap<String, Field<?>>>();

	BeanItemContainer<LetterDetailsDTO> data = new BeanItemContainer<LetterDetailsDTO>(LetterDetailsDTO.class);

	private Table table;

	LetterDetailsDTO bean;
	
	private Button btnAdd;

	public Object[] VISIBLE_COLUMNS = new Object[] {"Preview","letterName","toPerson", "address", "subject", "letterContent", "Delete"};

	public void init(LetterDetailsDTO bean) {
		this.bean = bean;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		addListener();
		// add buttons in table 
		
		table.removeGeneratedColumn("Preview");
		table.addGeneratedColumn("Preview", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@SuppressWarnings("serial")
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button previewButton = new Button("Preview");
				previewButton.setData(itemId);
				previewButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						LetterDetailsDTO tempObj = ((LetterDetailsDTO)itemId);
						if(!validateLetterDetails(tempObj)){
							generateLetterPreview(tempObj);
						}else{
							showErrorMessage("To view preview of letter, please fill To, Address, Subject, Content of the Letter fields.");
						}
					} 
				});
				return previewButton;			
			}
		});
		
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@SuppressWarnings("serial")
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						table.removeItem(itemId);
					} 
				});
				return deleteButton;			
			}
		});
		
		table.setWidth("100%");
		table.setPageLength(4);
		table.setVisibleColumns(VISIBLE_COLUMNS);

		//table.setColumnHeader("isPreviewFlag", "Letter Preview");
		table.setColumnHeader("letterName", " Name of the Letter");
		table.setColumnHeader("toPerson", " To");
		table.setColumnHeader("address", " Address");
		table.setColumnHeader("subject", " Subject");
		table.setColumnHeader("letterContent", "Content of the letter");
		
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		layout.addComponent(btnLayout);
		layout.addComponent(table);
		layout.setSpacing(true);
	}
	
	private void generateLetterPreview(LetterDetailsDTO argObj){
		LumenDocumentGenerator letterObj = new LumenDocumentGenerator();
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(null);
		List<LetterDetailsDTO> letterDTOList = new ArrayList<LetterDetailsDTO>();
		letterDTOList.add(argObj);		
		reportDto.setBeanList(letterDTOList);
		String generatedFilePath = letterObj.generatePdfDocument("LumenLetter", reportDto);
		System.out.println("In PReview : "+generatedFilePath);
		Window window = new Window();
		window.setWidth("90%");
		window.setHeight("90%");
		BrowserFrame e = new BrowserFrame(argObj.getLetterName()+" Preview", new FileResource(new File(generatedFilePath)));
		e.setWidth("100%");
		e.setHeight("100%");
		window.setContent(e);
		window.center();
		window.setModal(true);
		UI.getCurrent().addWindow(window);
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				LetterDetailsDTO letterTableDTO = new LetterDetailsDTO();
				letterTableDTO.setLetterName("Letter "+(table.getItemIds().size()+1));
				letterTableDTO.setToPerson("");
				LetterAddressDetails addressObj = new LetterAddressDetails();
				addressObj.setAddressLine1("");
				addressObj.setAddressLine2("");
				addressObj.setAddressLine3("");
				addressObj.setCity("");
				addressObj.setState("");
				addressObj.setPincode("");
				letterTableDTO.setAddressDetails(addressObj);
				// address field is created due to error faced while using Class Object mapping in table field. Table not able to map object in the DTO class.
				letterTableDTO.setAddress(letterTableDTO.getAddressDetails().toString());
				
				letterTableDTO.setSubject("");
				letterTableDTO.setLetterContent("");
				data.addItem(letterTableDTO);
			}
		});
	}
	
	public void setVisibleColumns() {
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			LetterDetailsDTO queryDetailsTableDto = (LetterDetailsDTO) itemId;
			//OptionGroup previewOption =  null;
			TextField nameField = null;
			TextField toField = null;
			TextField addressField = null;
			TextField subjectField = null;
			TextArea contentField = null;

			Map<String, Field<?>> tableRow = null;
			if (tableItem.get(queryDetailsTableDto) == null) {
				tableRow = new HashMap<String, Field<?>>();
				tableItem.put(queryDetailsTableDto, new HashMap<String, Field<?>>());
			} else {
				tableRow = tableItem.get(queryDetailsTableDto);
			}

			/*if("isPreviewFlag".equals(propertyId)) {
				previewOption= new OptionGroup();
				previewOption.setData(queryDetailsTableDto);
				previewOption.addItems(getRadioButtonOptions());
				previewOption.setItemCaption(true, "");
				previewOption.setStyleName("horizontal");
				previewOption.select(false);
				previewOption.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@SuppressWarnings("unused")
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
							Boolean isChecked = (Boolean) event.getProperty().getValue();
						}
					}
				});
				
				tableRow.put("", previewOption);
				return previewOption;
			}else*/ 
			if("letterName".equals(propertyId)){
				nameField = new TextField();
				nameField.setWidth("-1px");
				nameField.setData(queryDetailsTableDto);
				nameField.setReadOnly(true);
				tableRow.put("letterName", nameField);
				return nameField;
			}else if("toPerson".equals(propertyId)) {
				toField = new TextField();
				toField.setNullRepresentation("");
				toField.setData(queryDetailsTableDto);
				toField.setWidth("-1px");
				toField.setId("toPerson");
				toField.setMaxLength(50);
				handleTextAreaPopup(toField, null);
				tableRow.put("toPerson", toField);
				return toField;
			}else if("address".equals(propertyId)){
				addressField = new TextField();
				addressField.setWidth("-1px");
				addressField.setData(queryDetailsTableDto);
				addressField.setReadOnly(false);
				addressField.setInputPrompt("Press F8 To Enter Address");
				tableRow.put("address", addressField);
				handleAddressPopup(addressField, null, queryDetailsTableDto);
				return addressField;
			}else if("subject".equals(propertyId)){
				subjectField = new TextField();
				subjectField.setWidth("-1px");
				subjectField.setData(queryDetailsTableDto);
				subjectField.setId("subject");
				subjectField.setMaxLength(1000);
				tableRow.put("subject", subjectField);
				handleTextAreaPopup(subjectField, null);
				return subjectField;
			}else{
				contentField = new TextArea();
				contentField.setWidth("-1px");
				contentField.setData(queryDetailsTableDto);
				contentField.setId("content");
				contentField.setRows(1);
				contentField.setColumns(25);
				contentField.setMaxLength(4000);
				tableRow.put("letterContent", contentField);
				handlePopupForLetterContent(contentField, null);
				return contentField;
			}
		}
	}
	
	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);
		return coordinatorValues;
	}


	public void addBeanToList(LetterDetailsDTO diagnosisProcedureTableDTO) {
		data.addBean(diagnosisProcedureTableDTO);
	}
	public void addList(List<LetterDetailsDTO> diagnosisProcedureTableDTO) {
		for (LetterDetailsDTO diagnosisProcedureTableDTO2 : diagnosisProcedureTableDTO) {
			data.addBean(diagnosisProcedureTableDTO2);
		}
	}

	@SuppressWarnings("unchecked")
	public List<LetterDetailsDTO> getValues() {
		List<LetterDetailsDTO> itemIds = (List<LetterDetailsDTO>) this.table.getItemIds() ;
		return itemIds;
	}
	
	// This is addressField popup on F8
	@SuppressWarnings("unused")
	public  void handleAddressPopup(TextField searchField, final  Listener listener, LetterDetailsDTO queryDetailsTableDto) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		ShortcutListener txtlistener = getShortCutListenerForAddress(searchField, queryDetailsTableDto);
		handleShortcutForAddress(searchField, txtlistener);
	}

	@SuppressWarnings("serial")
	public  void handleShortcutForAddress(final TextField textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForAddress(final TextField txtFld, final LetterDetailsDTO queryDetailsTableDto)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation", "serial" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				final Panel panel = new Panel("Address Details");
				panel.setHeight("310px");
				panel.setWidth("100%");
				panel.setContent(buildFormLayout(queryDetailsTableDto));
								
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(panel);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				dialog.setCaption("");
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
					//GALAXYMAIN-10593
					dialog.setPositionX(350);
					dialog.setPositionY(50);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						txtFld.setValue(queryDetailsTableDto.getAddressDetails().toString());
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}
	
	public FormLayout buildFormLayout(final LetterDetailsDTO queryDetailsTableDto){
		
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		firstForm.setWidth("100%");		

		TextField al1 = new TextField("Address Line 1");
		al1.setValue(queryDetailsTableDto.getAddressDetails().getAddressLine1());
		//Vaadin8-setImmediate() al1.setImmediate(true);
		al1.setWidth("-1px");
		al1.setMaxLength(100);
		al1.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3004636672234692021L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setAddressLine1(((TextField)event.getProperty()).getValue());
			}
		});

		TextField al2 = new TextField("Address Line 2");
		al2.setValue(queryDetailsTableDto.getAddressDetails().getAddressLine2());
		//Vaadin8-setImmediate() al2.setImmediate(true);
		al2.setWidth("-1px");
		al2.setMaxLength(100);
		al2.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6784957614821237895L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setAddressLine2(((TextField)event.getProperty()).getValue());
			}
		});
		
		TextField al3 = new TextField("Address Line 3");
		al3.setValue(queryDetailsTableDto.getAddressDetails().getAddressLine3());
		//Vaadin8-setImmediate() al3.setImmediate(true);
		al3.setWidth("-1px");
		al3.setMaxLength(100);
		al3.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1798115728471728507L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setAddressLine3(((TextField)event.getProperty()).getValue());
			}
		});
		
		TextField cty = new TextField("City");
		cty.setValue(queryDetailsTableDto.getAddressDetails().getCity());
		//Vaadin8-setImmediate() cty.setImmediate(true);
		cty.setWidth("-1px");
		cty.setMaxLength(50);
		cty.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3742573423890442272L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setCity(((TextField)event.getProperty()).getValue());
			}
		});
		
		TextField stat = new TextField("State");
		stat.setValue(queryDetailsTableDto.getAddressDetails().getState());
		//Vaadin8-setImmediate() stat.setImmediate(true);
		stat.setWidth("-1px");
		stat.setMaxLength(50);
		stat.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7020870689112615056L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setState(((TextField)event.getProperty()).getValue());
			}
		});
		
		TextField pin = new TextField("Pincode");
		pin.setValue(queryDetailsTableDto.getAddressDetails().getPincode());
		//Vaadin8-setImmediate() pin.setImmediate(true);
		pin.setWidth("-1px");
		pin.setMaxLength(50);
		pin.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7706385170788171611L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				queryDetailsTableDto.getAddressDetails().setPincode(((TextField)event.getProperty()).getValue());
			}
		});

		firstForm.addComponent(al1);
		firstForm.addComponent(al2);
		firstForm.addComponent(al3);
		firstForm.addComponent(cty);
		firstForm.addComponent(stat);
		firstForm.addComponent(pin);
		
		return firstForm;
	}
	
	
	// This is textField Popup on F8
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextField searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		ShortcutListener txtlistener = getShortCutListenerForRemarks(searchField);
		handleShortcutForRedraft(searchField, txtlistener);
	}

	@SuppressWarnings("serial")
	public  void handleShortcutForRedraft(final TextField textField, final ShortcutListener shortcutListener) {
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

	private ShortcutListener getShortCutListenerForRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation", "serial" })
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
				if(txtFld.getId().equals("subject")){
					txtArea.setMaxLength(1000);
				}else if(txtFld.getId().equals("toPerson")){
					txtArea.setMaxLength(50);
				}else{
					txtArea.setMaxLength(4000);
				}
				if(txtFld.isReadOnly()){
					txtArea.setReadOnly(true);
				}else{
					txtArea.setReadOnly(false);
				}
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
				String strCaption = "";
				if(txtFld.getId().equals("subject")){
					strCaption = "Subject";
				}else if(txtFld.getId().equals("toPerson")){
					strCaption = "To";
				}else{
					strCaption = "Content of the letter";
				}

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
					//GALAXYMAIN-10593
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
	
	//Letter Fields Validation
	public boolean validateLetterDetails(LetterDetailsDTO atgDto){
		boolean errorFlag = false;
		if(StringUtils.isBlank(atgDto.getToPerson())){
			errorFlag = true;
		}
		/*if(StringUtils.isBlank(atgDto.getAddressDetails().getAddressLine1()) || StringUtils.isBlank(atgDto.getAddressDetails().getAddressLine2()) || StringUtils.isBlank(atgDto.getAddressDetails().getAddressLine3())){
			errorFlag = true;
		}*/
		if(StringUtils.isBlank(atgDto.getAddress())){
			errorFlag = true;
		}
		if(StringUtils.isBlank(atgDto.getAddressDetails().getCity())){
			errorFlag = true;
		}
		if(StringUtils.isBlank(atgDto.getAddressDetails().getState())){
			errorFlag = true;
		}
		if(StringUtils.isBlank(atgDto.getAddressDetails().getPincode())){
			errorFlag = true;
		}
		if(StringUtils.isBlank(atgDto.getSubject())){
			errorFlag = true;
		}
		if(StringUtils.isBlank(atgDto.getLetterContent())){
			errorFlag = true;
		}
		return errorFlag;
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
	
	
	// This is textField Popup on F8
		@SuppressWarnings("unused")
		public  void handlePopupForLetterContent(TextArea searchField, final  Listener listener) {

			ShortcutListener enterShortCut = new ShortcutListener(
					"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					((ShortcutListener) listener).handleAction(sender, target);
				}
			};
			ShortcutListener txtlistener = getShortCutListenerForLetterContent(searchField);
			handleShortcutForLetterContent(searchField, txtlistener);
		}

		@SuppressWarnings("serial")
		public  void handleShortcutForLetterContent(final TextArea textField, final ShortcutListener shortcutListener) {
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

		private ShortcutListener getShortCutListenerForLetterContent(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

				private static final long serialVersionUID = 1L;

				@SuppressWarnings({ "static-access", "deprecation", "serial" })
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

					if(txtFld.isReadOnly()){
						txtArea.setReadOnly(true);
					}else{
						txtArea.setReadOnly(false);
					}
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
					dialog.setCaption("Content of the letter");
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
						//GALAXYMAIN-10593
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
