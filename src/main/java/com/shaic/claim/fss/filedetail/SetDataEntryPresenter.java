package com.shaic.claim.fss.filedetail;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PDIntimationPremia;

@ViewInterface(ProcessDataEntryView.class)
public class SetDataEntryPresenter extends AbstractMVPPresenter<ProcessDataEntryView> {
	
public static final String CHEQUE_DETAILS_SETUP_REFERENCE = "cheque_details_setup_reference";

public static final String GET_SELECT_LIST = "get_select_list";

public static final String VALIDATE_CLAIM = "validate_claim";
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ProcessDataEntryService processService;
	
	
	public void setUpReference(
			@Observes @CDIEvent(CHEQUE_DETAILS_SETUP_REFERENCE) final ParameterDTO parameters) {
		
		SearchDataEntryTableDTO dto = (SearchDataEntryTableDTO) parameters.getPrimaryParameter();
		view.setupReferences(referenceData);
	}
	
	public void setUpSelectValueList(
			@Observes @CDIEvent(GET_SELECT_LIST) final ParameterDTO parameters) {
		
		Long key = (Long) parameters.getPrimaryParameter();
		String flag = parameters.getSecondaryParameter(0, String.class);
		Map<String, Object> containerMap = new HashMap<String, Object>();
		if(flag.equalsIgnoreCase(SHAConstants.DATA_ENTRY_RACK_CONTAINER)){
			containerMap.put(SHAConstants.DATA_ENTRY_RACK_CONTAINER,
					masterService.getRackListByLocation(key));
		}else if(flag.equalsIgnoreCase(SHAConstants.DATA_ENTRY_SHELF_CONTAINER)){
			containerMap.put(SHAConstants.DATA_ENTRY_SHELF_CONTAINER,
					masterService.getShelfListByRack(key));
		}
		view.setUpListenerContainer(containerMap);
	}
	
	public void validateClaimNumber(
			@Observes @CDIEvent(VALIDATE_CLAIM) final ParameterDTO parameters) {
		String claim = (String) parameters.getPrimaryParameter();
		String year = parameters.getSecondaryParameter(0, String.class);
		PDIntimationPremia pdiIntimation = processService.getClaimDetail(claim, year);
		view.setPremiaIntimation(pdiIntimation);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
