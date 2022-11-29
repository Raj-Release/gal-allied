package com.shaic.claim.omp.createintimation;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(OMPCreateIntimationView.class)
public class OMPCreateIntimationPresenter extends AbstractMVPPresenter<OMPCreateIntimationView>{

	public static final String OMP_SUBMIT_SEARCH = "searchompintimationtable";
	public static final String OMP_SUBMIT_CREATE = "createompintimationtable";

	@EJB
	private OMPCreateIntimationService ompService;
	
	@Override
	public void viewEntered() {
		System.out.println("OMPCreateIntimationPresenter called");
	}

	public void handleSearch(@Observes @CDIEvent(OMP_SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPCreateIntimationFormDTO searchFormDTO = (OMPCreateIntimationFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);		
		view.list(ompService.generateTableData(searchFormDTO,userName,passWord));
	}
	
	public void handleCreateSearch(@Observes @CDIEvent(OMP_SUBMIT_CREATE) final ParameterDTO parameters) {
		OMPCreateIntimationFormDTO searchFormDTO = (OMPCreateIntimationFormDTO) parameters.getPrimaryParameter();
		view.list(ompService.getAddIntimationData(searchFormDTO.getPolicyNo()));
	}

}
