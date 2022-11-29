package com.shaic.arch.test;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroup;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroupItemComponent;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnGenerator;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class TestTable extends ViewComponent {

	private static final long serialVersionUID = 1L;

	private static final String ROD_NO = "RODNO";
	private static final String SNO = "SNO";
	private static final String BILL_CLASSSIFICATION = "BILL_CLASSSIFICATION";
	private static final String CLAIMEDAMOUNT = "CLAIMEDAMOUNT";
	private static final String APPROVEDAMOUNT = "APPROVEDAMOUNT";
	private static final String RODSTATUS = "RODSTATUS";
	private static final String ACTION = "ACTION";
	private static final String VIEWDETAIL = "VIEWDETAIL";

	
	
	
//	ROD No, S.NO, Bill Classsification, Claimed Amount, Approved Amount, ROD Status, Action, View Detail
	
	private static final String ICON_PROPERTY = "icon";
	private static final String SELECTION_PROPERTY = "selection";
	
	public static final Object[] VISIBLE_COL_ORDER = new Object[] {"serialNumber", "id",
			"name", "salary" };

	private static final String[] DOCUMENTS = new String[] { "Word",
		"document-doc.png", "Image", "document-image.png", "PDF",
		"document-pdf.png", "PowerPoint", "document-ppt.png", "Text",
		"document-txt.png", "Web", "document-web.png", "Excel",
		"document-xsl.png" };

	

	public void initTable() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);

		Label headerLabel = new Label("FlexibleOptionGroup");
		headerLabel.setStyleName(Reindeer.LABEL_H1);
		mainLayout.addComponent(headerLabel);

		mainLayout.addComponent(new TableExampleTab());
	}
	
	private static Container createTestContainer() {
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty(ROD_NO, String.class, null);
		cont.addContainerProperty(SNO, String.class, null);
		cont.addContainerProperty(BILL_CLASSSIFICATION, String.class, null);
		cont.addContainerProperty(CLAIMEDAMOUNT, String.class, null);
		cont.addContainerProperty(APPROVEDAMOUNT, String.class, null);
		cont.addContainerProperty(RODSTATUS, String.class, null);
		cont.addContainerProperty(ACTION, String.class, null);
		cont.addContainerProperty(VIEWDETAIL, String.class, null);
		cont.addContainerProperty(ICON_PROPERTY, Resource.class, null);
			      
		for (int i = 0; i < DOCUMENTS.length; i++) {
			String rodNo = DOCUMENTS[i++];
			String id = DOCUMENTS[i];
			String sNO = DOCUMENTS[i];;
			String billClasssification = DOCUMENTS[i];;
			String claimedAmount = DOCUMENTS[i];;
			String approvedAmount = DOCUMENTS[i];;
			String rodStatus = DOCUMENTS[i];;
			String action = DOCUMENTS[i];;
			String viewDetails = DOCUMENTS[i];;
			Item item = cont.addItem(id);

			valuateTestContainerItem(item, rodNo, id, sNO, billClasssification, claimedAmount, approvedAmount, rodStatus, action, viewDetails);

		}
		return cont;
	}

	private static void valuateTestContainerItem(Item item, String rodNo, String id, String sNo, String billClasificaiton, String claimedAmount, String approvedAmount, String rodStatus, String action, String viewDetails ) {
		item.getItemProperty(ROD_NO).setValue(rodNo);
		item.getItemProperty(SNO).setValue(sNo);
		item.getItemProperty(BILL_CLASSSIFICATION).setValue(billClasificaiton);
		item.getItemProperty(CLAIMEDAMOUNT).setValue(claimedAmount);
		item.getItemProperty(APPROVEDAMOUNT).setValue(approvedAmount);
		item.getItemProperty(RODSTATUS).setValue(rodStatus);
		item.getItemProperty(ACTION).setValue(action);
		item.getItemProperty(VIEWDETAIL).setValue("value");
		item.getItemProperty(ICON_PROPERTY).setValue(new ThemeResource("../runo/icons/16/" + id));
	}

	public static Label createCaptionLabel(FlexibleOptionGroupItemComponent fog) {
		Label captionLabel = new Label();
		captionLabel.setData(fog);
		captionLabel.setIcon(fog.getIcon());
		captionLabel.setCaption(fog.getCaption());
		captionLabel.setWidth(null);
		return captionLabel;
	}

	private static abstract class AbstractTab extends VerticalLayout {

		protected FlexibleOptionGroup flexibleOptionGroup;

		protected LayoutClickListener layoutClickListener = new LayoutClickListener() {

			public void layoutClick(LayoutClickEvent event) {
				FlexibleOptionGroupItemComponent c = null;
				boolean allowUnselection = flexibleOptionGroup.isMultiSelect();
				if (event.getChildComponent() instanceof FlexibleOptionGroupItemComponent) {
					c = (FlexibleOptionGroupItemComponent) event
							.getChildComponent();
				} else if (event.getChildComponent() instanceof AbstractComponent) {
					Object data = ((AbstractComponent) event
							.getChildComponent()).getData();
					if (data instanceof FlexibleOptionGroupItemComponent) {
						c = (FlexibleOptionGroupItemComponent) data;
					}
					if (event.getChildComponent() instanceof HorizontalLayout) {
						allowUnselection = false;
					}
				}
				if (c != null) {
					Object itemId = c.getItemId();
					if (flexibleOptionGroup.isSelected(itemId)
							&& allowUnselection) {
						flexibleOptionGroup.unselect(itemId);
					} else {
						flexibleOptionGroup.select(itemId);
					}
				}
			}
		};

		public AbstractTab(String caption) {
			setCaption(caption);
			setMargin(true);
			flexibleOptionGroup = new FlexibleOptionGroup(createTestContainer());
			flexibleOptionGroup.setItemCaptionPropertyId(ROD_NO);
			flexibleOptionGroup.setItemIconPropertyId(ICON_PROPERTY);
			flexibleOptionGroup.setItemCaptionPropertyId(SNO);
			flexibleOptionGroup.setItemCaptionPropertyId(BILL_CLASSSIFICATION);
			flexibleOptionGroup.setItemCaptionPropertyId(CLAIMEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(APPROVEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(RODSTATUS);
			flexibleOptionGroup.setItemCaptionPropertyId(ACTION);
			flexibleOptionGroup.setItemCaptionPropertyId(VIEWDETAIL);
		}
	}

	
	private static class TableExampleTab extends AbstractTab {

		public TableExampleTab() {
			super("Table");

			final Table table = new Table(null, flexibleOptionGroup.getContainerDataSource());

			flexibleOptionGroup = new FlexibleOptionGroup(createTestContainer()) {
				public void setImmediate(boolean immediate) {
					//Vaadin8-setImmediate() super.setImmediate(immediate);
					//Vaadin8-setImmediate() table.setImmediate(true);
				}

				public void setMultiSelect(boolean multiSelect) {
					super.setMultiSelect(multiSelect);
					table.setMultiSelect(multiSelect);
				}

				public void setEnabled(boolean enabled) {
					super.setEnabled(enabled);
					table.setEnabled(enabled);
				}

				public void setReadOnly(boolean readOnly) {
					super.setReadOnly(readOnly);
					table.setReadOnly(readOnly);
				}
			};
			flexibleOptionGroup.setItemCaptionPropertyId(ROD_NO);
			flexibleOptionGroup.setItemIconPropertyId(ICON_PROPERTY);
			flexibleOptionGroup.setItemCaptionPropertyId(SNO);
			flexibleOptionGroup.setItemCaptionPropertyId(BILL_CLASSSIFICATION);
			flexibleOptionGroup.setItemCaptionPropertyId(CLAIMEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(APPROVEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(RODSTATUS);
			flexibleOptionGroup.setItemCaptionPropertyId(ACTION);
			flexibleOptionGroup.setItemCaptionPropertyId(VIEWDETAIL);

			//Vaadin8-setImmediate() flexibleOptionGroup.setImmediate(true);
			flexibleOptionGroup
					.setPropertyDataSource(new ObjectProperty<Object>(null,
							Object.class));

			table.setSelectable(true);
			table.setPropertyDataSource(flexibleOptionGroup
					.getPropertyDataSource());
			table.addGeneratedColumn(SELECTION_PROPERTY, new ColumnGenerator() {
				public Component generateCell(Table source, Object itemId,
						Object columnId) {
					return flexibleOptionGroup.getItemComponent(itemId);
				}
			});
			table.addGeneratedColumn(ACTION, new ColumnGenerator() {
				public Component generateCell(Table source, Object itemId,
						Object columnId) {
					return flexibleOptionGroup.getItemComponent(itemId);
				}
			});
			table.setRowHeaderMode(Table.RowHeaderMode.HIDDEN);
			table.setItemIconPropertyId(ICON_PROPERTY);
			table.setVisibleColumns(new Object[] { SELECTION_PROPERTY,ROD_NO, SNO, BILL_CLASSSIFICATION, CLAIMEDAMOUNT, APPROVEDAMOUNT, RODSTATUS, ACTION, VIEWDETAIL });
			table.setColumnHeader(SELECTION_PROPERTY, "");
			addComponent(table);
		}
	}

	
	public void init(String string, boolean b, boolean c) {
		initTable();
		
	}
}