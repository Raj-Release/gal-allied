package com.shaic.arch.table;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class NewSpecialityTable extends ViewComponent {

	private Map<FillUp, HashMap<String, ComboBox>> tableItem = new HashMap<FillUp, HashMap<String, ComboBox>>();

	BeanItemContainer<FillUp> data = new BeanItemContainer<FillUp>(FillUp.class);

	private Table table;

	private Button btnAdd;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {
			"diagnosis", "icdchapter" };

	/**
	 * Custom field factory that sets the fields as immediate.
	 */
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7067711376117004831L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			FillUp fillup = (FillUp) itemId;
			Map<String, ComboBox> tableRow = null;

			if (tableItem.get(fillup) == null) {
				tableRow = new HashMap<String, ComboBox>();
				tableItem.put(fillup, new HashMap<String, ComboBox>());
			} else {
				tableRow = tableItem.get(fillup);
			}

			if ("quantity".equals(propertyId)) {
				ComboBox box = new ComboBox();
				addQuantity(box);
				tableRow.put("quantity", box);
				// To fill the exising values
				final ComboBox priceCmb = tableRow.get("price");
				box.setData(fillup);
				addQuantityListener(box, priceCmb);
				return box;
			} else if ("price".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setData(fillup);
				ComboBox qtyCmb = tableRow.get("quantity");
				tableRow.put("price", box);
				addPriceListener(box, qtyCmb);
				addPrice(qtyCmb, box);

				return box;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public void printContainerValues() {
		List<FillUp> itemIds = data.getItemIds();
		for (FillUp fillUp : itemIds) {
			System.out.println("Container value : " + fillUp.getQuantity()
					+ " : " + fillUp.getPrice());
		}
	}

	public void addQuantity(ComboBox box) {
		for (int pl = 0; pl < 10; pl++)
			box.addItem(pl);

	}

	public void addPrice(ComboBox qty, ComboBox box) {
		for (int pl = 0; pl < 10; pl++)
			box.addItem(pl);

	}

	public void init() {
		VerticalLayout layout = new VerticalLayout();
		btnAdd = new Button("Add Row");
		layout.addComponent(btnAdd);
		extended(layout);

		btnAdd.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				BeanItem<FillUp> addItem = data.addItem(new FillUp(19, 2, 2005,
						1, 51.21));
				manageListeners();
			}
		});

		setCompositionRoot(layout);
	}

	private void addQuantityListener(final ComboBox qtyCombo,
			final ComboBox priceCombo) {
		if (qtyCombo != null) {
			qtyCombo.addListener(new Listener() {
				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					FillUp data2 = (FillUp) component.getData();
					HashMap<String, ComboBox> hashMap = tableItem.get(data2);
					System.out.println("Quantity" + data2.getQuantity());
					if (data2 != null) {
						addPrice(qtyCombo, priceCombo);
					}
				}
			});
		}

	}

	private void addPriceListener(final ComboBox priceCombo,
			final ComboBox qtyCombo) {
		if (priceCombo != null) {
			priceCombo.addListener(new Listener() {
				@Override
				public void componentEvent(Event event) {
					addPrice(qtyCombo, priceCombo);
					ComboBox component = (ComboBox) event.getComponent();
					FillUp data2 = (FillUp) component.getData();
					if (data2 != null)
						System.out.println("Price" + data2.getPrice());
				}
			});
		}

	}

	protected void manageListeners() {

		for (FillUp object : tableItem.keySet()) {
			HashMap<String, ComboBox> combos = tableItem.get(object);

			final ComboBox qtyCombo = combos.get("quantity");
			final ComboBox priceCombo = combos.get("price");
			System.out.println(qtyCombo + " : " + priceCombo);
			addQuantityListener(qtyCombo, priceCombo);
			addPrice(qtyCombo, priceCombo);

		}
	}

	public class FillUp implements Serializable {
		private static final long serialVersionUID = -5909762375694974599L;

		Date date;
		int quantity;
		int price;

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		double total;

		public FillUp() {
		}

		public FillUp(int day, int month, int year, int quantity, double total) {
			date = new GregorianCalendar(year, month - 1, day).getTime();
			this.quantity = quantity;
			this.price = 0;
			this.total = total;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}
	};

	void extended(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table(null, data);
		data.addNestedContainerProperty("quantity");
		data.addNestedContainerProperty("price");
		// table.addContainerProperty("quantity", ComboBox.class, null);
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.addStyleName("generatedcolumntable");
		table.setHeight("300px");

		table.setVisibleColumns(new Object[] { "quantity", "price", "total" });

		table.setColumnHeader("date", "Date");
		table.setColumnHeader("quantity", "Quantity (l)");
		table.setColumnHeader("price", "Price (â‚¬)");
		table.setColumnHeader("total", "Total (â‚¬)");
		table.setColumnHeader("consumption", "Consumption (l/day)");
		table.setColumnHeader("dailycost", "Daily Cost (â‚¬/day)");
		table.setEditable(true);
		// Add some data.
		data.addBean(new FillUp(19, 2, 2005, 1, 51.21));
		data.addBean(new FillUp(30, 3, 2005, 2, 53.67));
		manageListeners();
		// Have a check box that allows the user to make the
		// quantity and total columns editable.
		final CheckBox editable = new CheckBox(
				"Edit the input values - calculated columns are regenerated");
		editable.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				table.setEditable(editable.getValue().booleanValue());

				// The visible columns are affected by removal and addition of
				// generated columns so we have to redefine them.
				table.setVisibleColumns(new Object[] { "quantity", "total",
						"price" });
			}
		});

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

		// Build the layout
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.addComponents(
				new Label(
						"Table with column generators that format and calculate cell values."),
				table, editable, new Label(
						"Columns displayed in blue are calculated from Quantity and Total. "
								+ "Others are simply formatted."));
		layout.setExpandRatio(table, 1);
		layout.setSizeUndefined();
	}

}
