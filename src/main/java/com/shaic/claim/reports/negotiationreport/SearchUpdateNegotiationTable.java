package com.shaic.claim.reports.negotiationreport;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;

public class SearchUpdateNegotiationTable extends GBaseTable<ViewNegotiationDetailsDTO> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"intimationNo","cashlessorReimNo","cpucodeName","intimationStage"};
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ViewNegotiationDetailsDTO>(ViewNegotiationDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("120px");
		table.addStyleName("generateColumnTable");
		table.removeGeneratedColumn("unCheck");
		table.addGeneratedColumn("unCheck", new Table.ColumnGenerator() {			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CheckBox chkBox = new CheckBox();
				final ViewNegotiationDetailsDTO negDetDTO = (ViewNegotiationDetailsDTO) itemId;
				chkBox.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						List<ViewNegotiationDetailsDTO> items = (List<ViewNegotiationDetailsDTO>) table.getItemIds();
						
						boolean value = (Boolean) event.getProperty().getValue();
						if(value){
							negDetDTO.setUpdateNegotiation(Boolean.TRUE);
							fireViewEvent(SearchUpdateNegotiationPresenter.UPDATE_NEGOTIAION,negDetDTO);
						} else {
							negDetDTO.setUpdateNegotiation(Boolean.FALSE);
							fireViewEvent(SearchUpdateNegotiationPresenter.UPDATE_NEGOTIAION,negDetDTO);
						}
					}
				});
				return chkBox;
		}
		});	
		table.setColumnHeader("unCheck", "Uncheck");
		
	}

	@Override
	public void tableSelectHandler(ViewNegotiationDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "negotiation-report-";
	}
	
	public List<ViewNegotiationDetailsDTO> getValues(){
		List<ViewNegotiationDetailsDTO> negDetails = (List<ViewNegotiationDetailsDTO>) this.table.getItemIds();
		return negDetails;
	}

}
