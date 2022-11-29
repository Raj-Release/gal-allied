package com.shaic.claim.lumen.components;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RequestInitiationDetails extends ViewComponent{
	
	private TextField employeeName;
	private TextField employeeId;
	private TextArea comments;

	HorizontalLayout layout1;
	
	private LumenRequestDTO dtoObj;

	@PostConstruct
	public void initView() {

	}

	public void init(LumenRequestDTO resultDTO, String caption) {
		this.dtoObj = resultDTO;
		Panel panel = new Panel(caption);
		panel.setHeight("310px");
		panel.setWidth("100%");
		panel.setContent(buildCarousel());

		enableOrDisableFields(false);
		enableOrDisableFields(true);
		setCompositionRoot(panel);
	}

	private void enableOrDisableFields(boolean flag){
		employeeName.setReadOnly(flag);
		employeeId.setReadOnly(flag);
		comments.setReadOnly(flag);
	}

	private FormLayout buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		firstForm.setWidth("100%");		

		employeeName = new TextField("Employee Name");
		if(!StringUtils.isBlank(dtoObj.getEmpName())){
			employeeName.setValue(dtoObj.getEmpName());
		}else{
			employeeName.setValue("");
		}
		//Vaadin8-setImmediate() employeeName.setImmediate(true);
		employeeName.setWidth("-1px");
		employeeName.setReadOnly(true);

		employeeId = new TextField("Employee ID");
		if(!StringUtils.isBlank(dtoObj.getLoginId())){
			employeeId.setValue(dtoObj.getLoginId());
		}else{
			employeeId.setValue("");
		}
		//Vaadin8-setImmediate() employeeId.setImmediate(true);
		employeeId.setWidth("-1px");
		employeeId.setReadOnly(true);

		comments = new TextArea("Comments");
		if(!StringUtils.isBlank(dtoObj.getComments())){
			comments.setValue(dtoObj.getComments());
		}else{
			comments.setValue("");
		}
		//Vaadin8-setImmediate() comments.setImmediate(true);
		comments.setRows(6);
		comments.setColumns(22);
		comments.setReadOnly(true);
		comments.setMaxLength(4000);
		handleTextAreaPopup(comments,null);

		firstForm.addComponent(employeeName);
		firstForm.addComponent(employeeId);
		firstForm.addComponent(comments);
		
		return firstForm;
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
		ShortcutListener listener =  new ShortcutListener("Initiator Remarks",KeyCodes.KEY_F8,null) {

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
				txtArea.setReadOnly(true);
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

				String strCaption = "Initiator Comments";

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

}
