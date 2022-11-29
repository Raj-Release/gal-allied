package com.shaic.claim.registration.balancesuminsured.view;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OutpatientService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class OPDocumentDetails extends ViewComponent {
	
	
	@Inject
	private OPClaimStatusDoumentListTable documentCheckListObj;
	@Inject
	private OutpatientService outPatientService;

	private List<OPClaimStatusDoumentListDTO> oPDocumentList;

	private VerticalLayout mainLayout;

	public void init(OPHealthCheckup oPhealthCheckup) {
		VerticalLayout buildMainLayout = buildMainLayout(oPhealthCheckup);
		
		oPDocumentList=outPatientService.getOPDocumentList(oPhealthCheckup.getKey());
		
		documentCheckListObj.setTableList(oPDocumentList);
		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}

	public VerticalLayout buildMainLayout(OPHealthCheckup oPhealthCheckup) {
		documentCheckListObj.init("", false, false);
		documentCheckListObj.setHeight("100.0%");
		documentCheckListObj.setWidth("100.0%");
		TextField documentsRecievedFrom = new TextField(
				"Documents Recieved From ");
		
		documentsRecievedFrom.setValue(oPhealthCheckup.getDocumentReceivedFromId().getValue());
		documentsRecievedFrom.setReadOnly(true);
		TextField documentsRecievedDate = new TextField(
				"Documents Recieved Date ");
		documentsRecievedDate.setValue(oPhealthCheckup.getDocumentReceivedDate().toString());
		
		documentsRecievedDate.setReadOnly(true);
		TextField modeofReceipt = new TextField("Mode of Receipt");
		modeofReceipt.setValue(oPhealthCheckup.getModeOfReceipt().toString());
		modeofReceipt.setReadOnly(true);
		FormLayout leftForm = new FormLayout(documentsRecievedFrom,
				documentsRecievedDate, modeofReceipt);
		TextField acknowledgementContactNumber=new TextField("Acknowledgement Contact Number ");
		acknowledgementContactNumber.setValue(oPhealthCheckup.getPersonContactNumber().toString());
		acknowledgementContactNumber.setReadOnly(true);
		TextField eMailId=new TextField("Email ID");
		eMailId.setValue(oPhealthCheckup.getPersonEmailId());
		eMailId.setReadOnly(true);
		eMailId.setNullRepresentation("");
		TextField additionalRemarks = new TextField("Additional Remarks");
		additionalRemarks.setHeight("40px");
		additionalRemarks.setValue(oPhealthCheckup.getAdditionalRemarks());
		additionalRemarks.setNullRepresentation("");
		additionalRemarks.setReadOnly(true);
		FormLayout rightForm = new FormLayout(acknowledgementContactNumber,eMailId);
		
		HorizontalLayout formLayout=new HorizontalLayout(leftForm,rightForm);
		FormLayout addRemarks =new FormLayout(additionalRemarks);
		VerticalLayout vertical = new VerticalLayout(formLayout,documentCheckListObj,addRemarks);
		vertical.setSpacing(true);
		return vertical;
	}
}