package com.shaic.claim.reports.preauthFormDocReport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchPreauthFormDocTable extends GBaseTable<NewIntimationDto>{
		
	private final static Object COLUM_PREAUTH_FORM_DOC[] = new Object[] {
		"serialNumber","intimationId","insuredPatient.insuredName","doctorName","hospitalDto.name","hospitalDto.hospitalCode","cpuAddress",
		"hospitalDto.HospitalTypeValue","hospitalDto.address","hospitalDto.phoneNumber","reasonForAdmission"};
	
	
	@Override
	public void removeRow() {
		
//		table.removeAllItems();
		
	}

	@Override
	public void initTable() {

		BeanItemContainer<NewIntimationDto> newIntimationDtoContainer = new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class);
		
//newIntimationDtoContainer
//		.addNestedContainerProperty("policy.policyNumber");
//newIntimationDtoContainer
//		.addNestedContainerProperty("insuredPatient.healthCardNumber");
newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatient.insuredName");
//newIntimationDtoContainer
//		.addNestedContainerProperty("intimaterName");
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

//table.setColumnHeader("insuredPatient.insuredName", "Insured PatientName");
//table.setColumnHeader("hospitalDto.name","Hospital Name");
//table.setColumnHeader("hospitalDto.hospitalCode", "Hospital Code");
//table.setColumnHeader("hospitalDto.HospitalTypeValue", "Hospital Type");
//table.setColumnHeader("hospitalDto.address", "Hospital Address");
//table.setColumnHeader("hospitalDto.phoneNumber", "Hospital Contact Details");


	
		table.setContainerDataSource(newIntimationDtoContainer);
		table.setVisibleColumns(COLUM_PREAUTH_FORM_DOC);
		table.setSizeFull();
		
	}

	
	@Override
	public void tableSelectHandler(NewIntimationDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchPreauthFormDoc-";
	}
		
//	public void getErrorMessage(String eMsg) {
//
//		Label label = new Label(eMsg, ContentMode.HTML);
//		label.setStyleName("errMessage");
//		VerticalLayout layout = new VerticalLayout();
//		layout.setMargin(true);
//		layout.addComponent(label);
//
//		ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Error");
//		dialog.setClosable(true);
//		dialog.setContent(layout);
//		dialog.setResizable(false);
//		dialog.setModal(true);
//		dialog.show(getUI().getCurrent(), null, true);
//	}

}
