package com.shaic.claim.lumen.components;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.table.Page;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MISSubTableViews extends ViewComponent{
	
	@Inject
	private MISSubDetails  misSubObj;
	
	@Inject
	private MISDocumentDetails misDocObj;
	
	private TextArea replyRemarks;
	
	private FormLayout tempLayout;

	@PostConstruct
	public void initView() {

	}

	@SuppressWarnings("unchecked")
	public void init(List<MISSubDTO> firstTableList, List<MISDocumentDTO> secondTableList, String argRemarks) {
		
		VerticalLayout tableContainer = new VerticalLayout();
		misSubObj.init("Query Details", false, false);
		
		Page<MISSubDTO> misSubPage = new Page<MISSubDTO>();
		misSubPage.setPageItems(firstTableList);
		misSubPage.setTotalRecords(firstTableList.size());
		misSubPage.setTotalList(firstTableList);
		misSubObj.setTableList(misSubPage.getTotalList());
		misSubObj.setSubmitTableHeader();
		misSubObj.setSizeFull();
		
		misDocObj.init("Uploaded Documents", false, false);
		Page<MISDocumentDTO> misDocPage = new Page<MISDocumentDTO>();
		misDocPage.setPageItems(secondTableList);
		misDocPage.setTotalRecords(secondTableList.size());
		misDocPage.setTotalList(secondTableList);
		misDocObj.setTableList(misDocPage.getTotalList());
		misDocObj.setSubmitTableHeader();
		misDocObj.setSizeFull();
		
		tempLayout = new FormLayout();
		
		replyRemarks = new TextArea("Reply Remarks");
		if(!StringUtils.isBlank(argRemarks)){
			replyRemarks.setValue(argRemarks);
		}else{
			replyRemarks.setValue("");
		}
		replyRemarks.setRows(6);
		replyRemarks.setColumns(22);
		replyRemarks.setMaxLength(4000);
		replyRemarks.setReadOnly(true);
		handleTextAreaPopup(replyRemarks,null);
		
		tempLayout.addComponent(replyRemarks);
		
		tableContainer.addComponent(misSubObj);
		tableContainer.addComponent(misDocObj);
		tableContainer.addComponent(tempLayout);		
		
		setCompositionRoot(tableContainer);
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
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

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

				String strCaption = "Comments";

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
