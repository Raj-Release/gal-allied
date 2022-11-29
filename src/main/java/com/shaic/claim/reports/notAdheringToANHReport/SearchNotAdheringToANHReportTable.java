package com.shaic.claim.reports.notAdheringToANHReport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchNotAdheringToANHReportTable extends GBaseTable<NewIntimationNotAdheringToANHDto> {
	
	private final static Object NOT_ADHERING_TO_ANH[] = new Object[] {
		"serialNumber","intimationId","insuredPatient.insuredName","doctorName","hospitalDto.name","hospitalDto.hospitalCode","cpuAddress",
		"hospitalDto.HospitalTypeValue","hospitalDto.address","hospitalDto.phoneNumber","reasonForAdmission"};

	@Override
	public void removeRow() {
    
	}

	@Override
	public void initTable() {
      BeanItemContainer<NewIntimationNotAdheringToANHDto> newIntimationDtoContainer = new BeanItemContainer<NewIntimationNotAdheringToANHDto>(NewIntimationNotAdheringToANHDto.class);

newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatient.insuredName");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.name");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.hospitalCode");
newIntimationDtoContainer
.addNestedContainerProperty("hospitalDto.HospitalTypeValue");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.address");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.phoneNumber");

		table.setContainerDataSource(newIntimationDtoContainer);
		table.setVisibleColumns(NOT_ADHERING_TO_ANH);
		table.setSizeFull();
}

	@Override
	public void tableSelectHandler(NewIntimationNotAdheringToANHDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchPreauthFormDoc-";
	}

}
