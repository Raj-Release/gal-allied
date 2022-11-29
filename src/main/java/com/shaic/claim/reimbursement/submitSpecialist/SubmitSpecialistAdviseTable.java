package com.shaic.claim.reimbursement.submitSpecialist;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.table.GInlineTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.submitSpecialist.SubmitSpecialistDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SubmitSpecialistAdviseTable extends GInlineTable<SubmitSpecialistDTO> {
	
	private static final long serialVersionUID = 1L;

	public SubmitSpecialistAdviseTable() {
		super(SubmitSpecialistAdviseTable.class);
		setUp();
	}
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "requestorRemarks",
		"requestedDate", "viewFile", "specialistRemarks","fileUpload" };
	*/
	
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	{
		fieldMap.put("requestorRemarks", new TableFieldDTO("requestorRemarks", TextArea.class,
				String.class, false));
		fieldMap.put("requestedDate", new TableFieldDTO(
				"requestedDate", TextArea.class, String.class, false));
		fieldMap.put("viewFile", new TableFieldDTO("viewFile", TextField.class,
				String.class, false));
		fieldMap.put("specialistRemarks", new TableFieldDTO(
				"specialistRemarks", TextArea.class, String.class, true));
		fieldMap.put("fileUpload", new TableFieldDTO(
				"fileUpload", TextField.class, String.class, true));
	}*/

	@Override
	protected void newRowAdded() {
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		
			fieldMap.put("requestorRemarks", new TableFieldDTO("requestorRemarks", TextArea.class,
					String.class, false));
			fieldMap.put("requestedDate", new TableFieldDTO(
					"requestedDate", TextArea.class, String.class, false));
			fieldMap.put("viewFile", new TableFieldDTO("viewFile", TextField.class,
					String.class, false));
			fieldMap.put("specialistRemarks", new TableFieldDTO(
					"specialistRemarks", TextArea.class, String.class, true));
			fieldMap.put("fileUpload", new TableFieldDTO(
					"fileUpload", TextField.class, String.class, true));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {

		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<SubmitSpecialistDTO>(
				SubmitSpecialistDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "requestorRemarks",
			"requestedDate", "viewFile", "specialistRemarks","fileUpload" };
		
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		
		Map<String, TableFieldDTO> fieldMap = getFiledMapping();
		
		TextArea specialistArea = (TextArea)(fieldMap.get("specialistRemarks")).getField();
		
		addDetailPopupForSpecialistRemarks(specialistArea,null);
		
	}

	@Override
	public void tableSelectHandler(SubmitSpecialistDTO t) {
		
//		Map<String, TableFieldDTO> fieldMap = getFiledMapping();
//		
//		TextArea specialistArea = (TextArea)(fieldMap.get("specialistRemarks")).getField();
//		specialistArea.setData(t);
	}

	@Override
	public String textBundlePrefixString() {
		return "upload-translated-documents-";
		
	}
	private void addDetailPopupForSpecialistRemarks(TextArea txtFld, final  Listener listener) {
		  ShortcutListener enterShortCut = new ShortcutListener(
			        "ShortcutForSpecailistRemarks", ShortcutAction.KeyCode.F8, null) {
				
			      private static final long serialVersionUID = 1L;
			      @Override
			      public void handleAction(Object sender, Object target) {
			        ((ShortcutListener) listener).handleAction(sender, target);
			      }
			    };
			    handleShortcutForSpecialistRem(txtFld, getShortCutListenerForSpecialistRemarks(txtFld));
			    
			  }
			
			public  void handleShortcutForSpecialistRem(final TextArea textField, final ShortcutListener shortcutListener) {
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
			private ShortcutListener getShortCutListenerForSpecialistRemarks(final TextArea txtFld)
			{
				ShortcutListener listener =  new ShortcutListener("Specialist Remarks",KeyCodes.KEY_F8,null) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void handleAction(Object sender, Object target) {
						SubmitSpecialistDTO  searchPedDto = (SubmitSpecialistDTO) txtFld.getData();
						VerticalLayout vLayout =  new VerticalLayout();
						
						vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
						vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
						vLayout.setMargin(true);
						vLayout.setSpacing(true);
						TextArea txtArea = new TextArea();
						txtArea.setNullRepresentation("");
						txtArea.setValue(txtFld.getValue());
						txtArea.setSizeFull();
						txtArea.setWidth("100%");
						txtArea.setRows(txtFld.getValue() != null ? (txtFld.getValue().length()/80 >= 25 ? 25 : ((txtFld.getValue().length()/80)%25)+1) : 25);
						txtArea.setReadOnly(true);
						
						Button okBtn = new Button("OK");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						vLayout.addComponent(txtArea);
						vLayout.addComponent(okBtn);
						vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
						
						final Window dialog = new Window();
						dialog.setCaption("Specialist Remarks");
						dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
						dialog.setWidth("45%");
						dialog.setClosable(true);
						
						dialog.setContent(vLayout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.setDraggable(true);
						
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
