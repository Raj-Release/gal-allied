package com.shaic.claim.registration.ackhoscomm.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.hospitalCommunication.HospitalCommunicationService;
import com.shaic.domain.CPUCodeService;

@SuppressWarnings("serial")
@ViewInterface(SearchAcknowledgeHospitalCommunicationView.class)
public class SearchAcknowledgeHospitalCommunicationPresenter extends
		AbstractMVPPresenter<SearchAcknowledgeHospitalCommunicationView> {
	
	@EJB
	private HospitalCommunicationService hospitalCommunicationService;

	public static final String SEARCH_BUTTON_CLICK = "ahcSearchClick";
	
	public static final String GET_CPU_CODE = "Get cpu code for acknowledgement Hospital Communication Search";
	
	@EJB
	private CPUCodeService cpuCodeService;
	

	public void handleSearch(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchAcknowledgeHospitalCommunicationFormDTO searchFormDTO = (SearchAcknowledgeHospitalCommunicationFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(hospitalCommunicationService.search(searchFormDTO,userName,passWord));
	}
	
	public void getCpuCode(
			@Observes @CDIEvent(GET_CPU_CODE) final ParameterDTO parameters) {
		
//		BeanItemContainer<TmpCPUCode> tmpCpuCodes = cpuCodeService.getTmpCpuCodes();
//		
//		view.setCpuCode(tmpCpuCodes);
	
	}

	@Override
	public void viewEntered() {

	}

}
