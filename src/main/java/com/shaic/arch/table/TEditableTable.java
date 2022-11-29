package com.shaic.arch.table;

import com.vaadin.cdi.CDIUI;

@SuppressWarnings("serial")

public abstract class TEditableTable<T>  {

//	private VerticalLayout vLayout = new VerticalLayout();
//
//	private Button btnAdd;
//
//	@Override
//	protected void preInit() {
//		super.preInit();
//		btnAdd = new Button("Add Row");
//		table.setEditable(true);
//		table.setTableFieldFactory(new EnhanceTableFactory());
//	}
//
//	private final T createContents() {
//		try {
//			return ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
//		} catch (InstantiationException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setEditable(boolean flag) {
//		table.setEditable(flag);
//	}
//
//	public void init() {
//		preInit();
//		setSizeFull();
//
//		table.addValueChangeListener(new Property.ValueChangeListener() {
//			public void valueChange(
//					final com.vaadin.v7.data.Property.ValueChangeEvent event) {
//				final T bean = (T) event.getProperty().getValue();
//				if (bean != null) {
//					tableSelectHandler(bean);
//				}
//			}
//		});
//
//		btnAdd.addClickListener(new Button.ClickListener() {
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				addItem(createContents());
//			}
//		});
//
//		vLayout.addComponent(btnAdd);
//		vLayout.addComponent(table);
//
//		setCompositionRoot(vLayout);
//		initTable();
//		localize(null);
//	}
//
//	public void addItem(T bean) {
//		table.addItem(bean);
//		table.setEditable(true);
//		// Object itemId = container.addItem();
//		// if (bean != null)
//		// {
//		// // Long key = ((SearchTableDTO) bean).getKey();
//		// // if (key != null)
//		// // {
//		// // container.getItem(itemId).getItemProperty("key").setValue(key);
//		// // }
//		// //
//		// container.getItem(itemId).getItemProperty("firstName").setValue(bean.getFirstName());
//		// //
//		// container.getItem(itemId).getItemProperty("lastName").setValue(bean.getLastName());
//		// //
//		// container.getItem(itemId).getItemProperty("relationship").setValue(bean.getBabyRelationship());
//		// //
//		// addActionButtons();
//		// }
//	}
//
//	// private void addActionButtons(Long key) {
//	// Button deleteBtn = new Button("Delete");
//	// deleteBtn.setData(itemId);
//	// deleteBtn.addClickListener(new ClickListener() {
//	// @Override
//	// public void buttonClick(ClickEvent event) {
//	// table.removeItem(event.getButton().getData());
//	// }
//	// });
//	// container.getItem(itemId).getItemProperty("action").setValue(deleteBtn);
//	// }
//	protected abstract void newRowAdded();

}
