package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;


@ViewInterface(CancelDocumentWizardView.class)
public class CancelDocumentWizardPresenter extends AbstractMVPPresenter<CancelDocumentWizardView> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SETUP_DROPDOWN_VALUES = "Reason for cancel acknowledgement";
	
	public static final String SETUP_TABLE_DATA = "Set table values for cancel acknowledgement";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentReceivedService;

	public void setUpDropDownValues(
			@Observes @CDIEvent(SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("reasonForCancel", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_CANCEL_ACKNOWLEDGEMENT));
		
		
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	public void setUpTableValues(
			@Observes @CDIEvent(SETUP_TABLE_DATA) final ParameterDTO parameters) {
		
		Long acknowledgementKey = (Long)parameters.getPrimaryParameter();
		
		List<ViewDocumentDetailsDTO> acknowledgmentForCancel = documentReceivedService.getAcknowledgmentForCancel(acknowledgementKey);
		
		view.setTableValues(acknowledgmentForCancel);
		
		
	}
	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
