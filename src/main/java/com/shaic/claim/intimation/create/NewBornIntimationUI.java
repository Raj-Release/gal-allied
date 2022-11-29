package com.shaic.claim.intimation.create;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewBabyIntimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
@UIScoped
public class NewBornIntimationUI extends ViewComponent {

	// protected IndexedContainer container;
	@Inject
	private MasterService masterService;

	private void columnMapper(Table table, IndexedContainer container) {
		table.addContainerProperty("key", Long.class, null);
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("relationship", SelectValue.class, null);
		table.addContainerProperty("action", Button.class, null);
		table.setContainerDataSource(container);
		table.setPageLength(table.size());
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("key", Long.class, null);
		container.addContainerProperty("name", String.class, null);
		container.addContainerProperty("relationship", SelectValue.class, null);
		container.addContainerProperty("action", Button.class, null);
		return container;
	}

	public Table createTable(IndexedContainer container) {
		final BeanItemContainer<MastersValue> masterValue = masterService
				.getMasterValue(ReferenceTable.PNC_RELATIONSHIP);

		final Table table = new Table("Patient Not Covered");
		columnMapper(table, container);

		table.setTableFieldFactory(new DefaultFieldFactory() {
			@SuppressWarnings({ "rawtypes", "deprecation" })
			@Override
			public Field createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {
				Class<?> cls = container.getType(propertyId);

				// Create a DateField with year resolution for dates
				if (cls.equals(Date.class)) {
					DateField df = new DateField();
					df.setResolution(DateField.RESOLUTION_YEAR);
					return df;
				}
				if (cls.equals(String.class))
				{
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setRequired(true);

//					CSValidator nameValidator = new CSValidator();
////					nameValidator.setRegExp("^[a-zA-Z]$");
//					nameValidator.setPreventInvalidTyping(true);
//					nameValidator.extend(field);
				
					return field;
				}
				// Create a CheckBox for Boolean fields
				if (cls.equals(Boolean.class))
					return new CheckBox();
				if (cls.equals(MastersValue.class)) {
					ComboBox relationship = new ComboBox();
					relationship.setContainerDataSource(masterValue);
					return relationship;
				}
				// Otherwise use the default field factory
				return super.createField(container, itemId, propertyId,
						uiContext);
			}
		});
		table.setWidth("100%");
		table.setHeight("200px");
		table.setVisibleColumns(new Object[] { "name", "relationship", "action" });
		table.setEditable(true);
		return table;
	}

	@SuppressWarnings("unchecked")
	public void addItem(final Table table, IndexedContainer container,
			NewBabyIntimation newBorn) {
		Object itemId = container.addItem();
		if (newBorn != null)
		{
			Long key = newBorn.getKey();
			if (key != null)
			{
				container.getItem(itemId).getItemProperty("key").setValue(key);
			}	
			container.getItem(itemId).getItemProperty("name")
					.setValue(newBorn.getName());
			container.getItem(itemId).getItemProperty("relationship")
					.setValue(newBorn.getBabyRelationship());
			
			Button deleteBtn = new Button("Delete");
			deleteBtn.setData(itemId);
			deleteBtn.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					table.removeItem(event.getButton().getData());
				}
			});
			container.getItem(itemId).getItemProperty("action").setValue(deleteBtn);
		}
	}

	public List<NewBabyIntimation> getBeanList(IndexedContainer container)
{
		List ids =  container.getItemIds();
		List<NewBabyIntimation> newBornList = new ArrayList<NewBabyIntimation>();
		for (Object id : ids) {
			 Long key = container.getItem(id).getItemProperty("key").getValue() != null ? (Long) container.getItem(id).getItemProperty("key").getValue() : null;
			 String tmpName = (String) container.getItem(id).getItemProperty("name").getValue();
			 MastersValue tmpRelationship = (MastersValue) container.getItem(id).getItemProperty("relationship").getValue();
			 NewBabyIntimation newBaby = new NewBabyIntimation();
			 newBaby.setName(tmpName);
			 newBaby.setBabyRelationship(tmpRelationship);
			 if(key != null) {
				 newBaby.setKey(key);
			 }
			 newBornList.add(newBaby);
		} 
		return newBornList;
	}
}
