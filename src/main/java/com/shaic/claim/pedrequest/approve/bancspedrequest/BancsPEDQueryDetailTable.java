package com.shaic.claim.pedrequest.approve.bancspedrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.aadhar.pages.UpdateAadharDetailsDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndorsementDetails;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.userproduct.document.UserProductMappingService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.PreauthService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.CellStyleGenerator;

public class BancsPEDQueryDetailTable extends GBaseTable<BancsPEDQueryDetailTableDTO> {

	public BancsPEDQueryDetailTable() {
		// super(PEDRequestDetailsTable.class);
		// setUp();
	}

	private static final long serialVersionUID = 7031963170040209948L;

	@EJB
	private PEDQueryService pedService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	@Inject
	private BancsPEDQueryDetailTableDTO oldPedEndorsementDTO;

	@SuppressWarnings("unused")
	private Instance<ViewPEDEndorsementDetails> viewDetailsInstance;

	@EJB
	private UserProductMappingService userMappingService;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setHeight("150px");
		table.setContainerDataSource(new BeanItemContainer<BancsPEDQueryDetailTableDTO>(
				BancsPEDQueryDetailTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
		"queryId","queryType", "queryDesc", "queryCode", "queryRemarks",
				"raisedByUser","raisedByRole", "raisedDate","replyRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("replyRemarks");
		table.addGeneratedColumn("replyRemarks", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			   BancsPEDQueryDetailTableDTO oldPedEndorsementDTO = (BancsPEDQueryDetailTableDTO)itemId;
				TextArea replyText = new TextArea("");
				replyText.setNullRepresentation("");
				replyText.setId("flag");
				replyText.setHeight("20px");
				replyText.setReadOnly(Boolean.FALSE);
				replyText.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				replyText.setData(oldPedEndorsementDTO);
				handleTextAreaPopup(replyText,null);
				replyText.addValueChangeListener(replyRemarksListener());
		    	return replyText;
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
		ShortcutListener listener =  new ShortcutListener("Query Reply",KeyCodes.KEY_F8,null) {

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
				txtArea.setMaxLength(2000);
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

				String strCaption = "Query Reply";

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

	@Override
	public String textBundlePrefixString() {
		return "ped-query-details-";
	}


	public void setRowColor(Long key) {
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
		final Object selectedRowId = getSelectedRowId(itemIds, key);
		System.out.print(";;;;;;;;;;;;;;;;;; selected id = " + selectedRowId);

		table.setCellStyleGenerator(new CellStyleGenerator() {

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {
				// table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);

				oldPedEndorsementDTO = (BancsPEDQueryDetailTableDTO) selectedRowId;
				long key1 = oldPedEndorsementDTO.getKey();
				oldPedEndorsementDTO = (BancsPEDQueryDetailTableDTO) itemId;
				long key2 = oldPedEndorsementDTO.getKey();
				if (key1 == key2) {
					return "select";
				} else {
					return "none";
				}

			}

		});

	}

	private Object getSelectedRowId(ArrayList<Object> ids, Long key) {

		for (Object id : ids) {
			oldPedEndorsementDTO = (BancsPEDQueryDetailTableDTO) id;
			Long key1 = oldPedEndorsementDTO.getKey();
			if (key1.equals(key)) {
				return id;
			}
		}

		return null;

	}
	
	@Override
	public void tableSelectHandler(BancsPEDQueryDetailTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public ValueChangeListener replyRemarksListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextArea replyRemarks = (TextArea) event.getProperty();
				BancsPEDQueryDetailTableDTO bancsPEDQueryDetailTableDTO = (BancsPEDQueryDetailTableDTO)replyRemarks.getData();
				if(replyRemarks!=null && replyRemarks.getValue()!=null){
					bancsPEDQueryDetailTableDTO.setReplyRemarks(replyRemarks.getValue());
				}
			}
		};
		
		return listener;
	}
	
	public List<BancsPEDQueryDetailTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<BancsPEDQueryDetailTableDTO> itemIds = (List<BancsPEDQueryDetailTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }

}
