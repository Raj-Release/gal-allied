package com.shaic.claim.outpatient.processOP.pages.claimDecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(OPClaimDecisionPageInterface.class)
public class OPClaimDecisionPagePresenter extends AbstractMVPPresenter<OPClaimDecisionPageInterface>{

	private static final long serialVersionUID = 5811691782410656962L;
	public static final String SET_UP_REFERENCE = "process_claim_and_document_details_page_set_up_reference";
	public static final String OP_IFSC_DETAILS  = "op_ifsc_details";
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpReference(
			@Observes @CDIEvent(SET_UP_REFERENCE) final ParameterDTO parameters) {
		
		OutPatientDTO dto = (OutPatientDTO) parameters.getPrimaryParameter();
		BeanItemContainer<Insured> clsInsuredList = insuredService.getCLSInsuredList(dto.getPolicyDto().getPolicyNumber());
		dto.getNewIntimationDTO().getPolicy().setInsured(clsInsuredList.getItemIds());
		referenceData.put("insuredPatientName", insuredService.getCLSInsuredList(dto.getPolicyDto().getPolicyNumber()));
		referenceData.put("insuredList", clsInsuredList);
		referenceData.put("claimType", masterService.getSelectValueContainer(ReferenceTable.CLAIM_TYPE));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("modeOfReceipt", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
		referenceData.put("docReceivedFrom", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));
		referenceData.put("receivedStatus", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_STATUS));
		getValuesForNameDropDown(dto);
		view.setupReferences(referenceData);
	}
	
	
	private void getValuesForNameDropDown(OutPatientDTO dto)
	{
		Policy policy = dto.getNewIntimationDTO().getPolicy();
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			SelectValue selectValue = new SelectValue();
			SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		referenceData.put("payeeNameList", payeeNameValueContainer);
		referenceData.put("proposerName", payeeSelValue);
		}
		
	}
	public void setupIFSCDetails(
			@Observes @CDIEvent(OP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
//		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}


}
