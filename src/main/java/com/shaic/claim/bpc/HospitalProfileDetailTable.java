package com.shaic.claim.bpc;


import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class HospitalProfileDetailTable extends GBaseTable<ViewBusinessProfilChartDTO>{

	
	@Inject
	private ViewBusinessProfilChartDTO bean;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private Map<ViewBusinessProfilChartDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ViewBusinessProfilChartDTO, HashMap<String, AbstractField<?>>>();
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","labelName","colValue"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "deprecation", "serial" })
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewBusinessProfilChartDTO>(
				ViewBusinessProfilChartDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("labelName", "Hospital Details");
		table.setColumnHeader("colValue", "Values");
		table.setColumnWidth("serialNumber", 50);
		table.setColumnWidth("labelName", 500);
		table.setColumnWidth("colValue", 500);
		
		table.setEditable(false);
		table.setSelectable(true);
		
		table.setWidth("1000px");

	}

	@Override
	public void tableSelectHandler(ViewBusinessProfilChartDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "view-hospitalProfile-";
	}
	
	
	/*public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			ViewBusinessProfilChartDTO viewBusinessProfilChartDTO = (ViewBusinessProfilChartDTO) itemId;
			TextField queryfield = null;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(viewBusinessProfilChartDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(viewBusinessProfilChartDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(viewBusinessProfilChartDTO);
			}

			if("hospitalDetails".equals(propertyId)) {
				queryfield = new TextField();
				queryfield.setNullRepresentation("");
				queryfield.setStyleName("tfwb");
				queryfield.setData(viewBusinessProfilChartDTO);
				//queryfield.setStyleName(viewBusinessProfilChartDTO.getTextFieldStyleName());
				queryfield.setWidth("100%");
				queryfield.setReadOnly(true);
				tableRow.put("scoringName", queryfield);
				return queryfield;
			}
			return queryfield;
		}
	}*/


}
