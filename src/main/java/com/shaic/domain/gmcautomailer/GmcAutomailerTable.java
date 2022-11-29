package com.shaic.domain.gmcautomailer;

import java.util.List;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.TextBox;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.userproduct.document.search.UserMgmtApplicableCpuDTO;
//import com.shaic.claim.preauth.negotiation.SearchUncheckNegotiationTableDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class GmcAutomailerTable extends GBaseTable<GmcAutomailerTableDTO>{

	private static final long serialVersionUID = 1L;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {
		/*"serialNumber",*/ "policyNo_branchCode", "emailId", "disable" };

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<GmcAutomailerTableDTO>(
				GmcAutomailerTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		generatecolumns();
		table.setHeight("120px");
		table.getValue();
//		table.setColumnWidth("serialNumber", 60);
//		table.setColumnWidth("policyNo_branchCode", 250);
		table.setColumnWidth("emailId", 800);
//		table.setColumnWidth("update", 150);
		table.removeGeneratedColumn("emailId");
		table.addGeneratedColumn("emailId", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final GmcAutomailerTableDTO autoMailer = (GmcAutomailerTableDTO) itemId;
				final TextArea text = new TextArea();
				text.setValue(autoMailer.getEmailId());
				text.setEnabled(true);
				text.setWidth("100%");
				text.setHeight("50%");
				handleTextAreaPopup(text,null);
				text.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						autoMailer.setEmailId(text.getValue());
						
					}
				});
				return text;
			}
			
		});
//		table.setColumnWidth("disable", 150);
	}

	
	
	@Override
	public void tableSelectHandler(GmcAutomailerTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "gmcautomailer-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=6){
			table.setPageLength(6);
		}
		
	}
	
	 public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	 
	 private void generatecolumns(){
			
			table.removeGeneratedColumn("disable");
			table.addGeneratedColumn("disable", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					GmcAutomailerTableDTO tableDTO = (GmcAutomailerTableDTO) itemId;
						CheckBox chkBox = new CheckBox();													
						chkBox.setValue(tableDTO.getDisable());
						//checkBoxListener(chkBox);
						chkBox.addValueChangeListener(new ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								GmcAutomailerTableDTO tableDTO = (GmcAutomailerTableDTO) itemId;
								if(null != event && null != event.getProperty() && null != event.getProperty().getValue()) {
									boolean value = (Boolean) event.getProperty().getValue();
									if(value)
									{
										tableDTO.setDisable(Boolean.TRUE);
									}
									else
									{
										tableDTO.setDisable(Boolean.FALSE);
									}
								}
								
							}
						});
					return chkBox;
				}
			});
	 }
	 
	/* public void checkBoxListener(final CheckBox chkBox){
		 chkBox
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GmcAutomailerTableDTO tableDTO = (GmcAutomailerTableDTO) itemId;
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
						boolean value = (Boolean) event.getProperty().getValue();
						if(value)
						{
							tableDTO.setDisable(Boolean.TRUE);
						}
						else
						{
							tableDTO.setDisable(Boolean.FALSE);
						}
							
					}
				}
			});
	 }*/
	 
	 public Boolean isValid()
	 {
		List<GmcAutomailerTableDTO> tableList = (List<GmcAutomailerTableDTO>) table.getItemIds();
		
		if(null!= tableList && !tableList.isEmpty()){
			for (GmcAutomailerTableDTO tableDto : tableList) {
				if(null == tableDto.getDisable() || !tableDto.getDisable()){
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		 return true; 
	 }
	 
	 public List<GmcAutomailerTableDTO> getTableList()
	 {
		 List<GmcAutomailerTableDTO> tableList = (List<GmcAutomailerTableDTO>) table.getItemIds();
		return tableList;
	 }
	 
	 public void setColumnHeader(){
		 table.setColumnHeader("serialNumber", "S.No");
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
			ShortcutListener listener =  new ShortcutListener("FVR Not Required Remarks For Others",KeyCodes.KEY_F8,null) {

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
					txtArea.setMaxLength(230);
					txtArea.setReadOnly(false);
					txtArea.setRows(25);

					txtArea.addValueChangeListener(new ValueChangeListener() {

						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							if(null != mainDto){
								mainDto.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(txtFld.getValue());
							}
						
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

					final Window dialog = new Window();

					String strCaption = "Email Id";

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
