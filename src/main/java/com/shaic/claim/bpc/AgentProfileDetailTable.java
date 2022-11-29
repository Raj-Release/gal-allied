package com.shaic.claim.bpc;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AgentProfileDetailTable extends GBaseTable<ViewBusinessProfilChartDTO>{

	@Inject
	private ViewBusinessProfilChartDTO bean;

	private static final long serialVersionUID = 1L;


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
		table.setColumnHeader("labelName", "Agent Details");
		table.setColumnHeader("colValue", "Values");
		table.setColumnWidth("serialNumber", 50);
		table.setColumnWidth("labelName", 500);
		table.setColumnWidth("colValue", 500);

		table.setEditable(false);
		//table.setSelectable(true);

		table.setWidth("1000px");
		//table.setWidth("70%");
		/*table.removeGeneratedColumn("Values");
		table.addGeneratedColumn("Values", new Table.ColumnGenerator() {
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
//				VerticalLayout buttonLayout =new VerticalLayout();	
//				ViewSeriousDeficiencyDTO seriousDeficiencyDTO = (ViewSeriousDeficiencyDTO) itemId;
		    	final Button viewHospitalScoringDetailsButton = new Button("View");
		    	viewHospitalScoringDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
		    	viewHospitalScoringDetailsButton.addClickListener(new Button.ClickListener() {
	    			public void buttonClick(ClickEvent event) {
	    				//showScoringView((ViewBusinessProfilChartDTO) itemId);
	    			} 
		    	});
//		    	buttonLayout.setMargin(Boolean.FALSE);
//		    	buttonLayout.setSpacing(Boolean.FALSE);
		    	return viewHospitalScoringDetailsButton;
			}
		});*/
		//table.setColumnWidth("Values", 250);
		//table.setColumnAlignment("View", Table.ALIGN_CENTER);


	}

	@Override
	public void tableSelectHandler(ViewBusinessProfilChartDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "view-agentProfile-";
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
