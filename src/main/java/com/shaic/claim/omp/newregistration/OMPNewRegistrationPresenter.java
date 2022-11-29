package com.shaic.claim.omp.newregistration;

import java.text.ParseException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.omp.createintimation.OMPCreateIntimationService;


@SuppressWarnings("serial")
@ViewInterface(OMPNewRegistrationView.class)
public class OMPNewRegistrationPresenter extends AbstractMVPPresenter<OMPNewRegistrationView>{

	public static final String OMP_REG_SEARCH = "searchompregistrationtable";

	@Inject
	private OMPCreateIntimationService ompService;
	
	@Override
	public void viewEntered() {
		System.out.println("OMPNewRegistrationPresenter called");
	}

	public void handleSearch(@Observes @CDIEvent(OMP_REG_SEARCH) final ParameterDTO parameters) throws ParseException {
		OMPNewRegistrationSearchDTO searchFormDTO = (OMPNewRegistrationSearchDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		OMPNewRegistrationSearchTable tableObj = (OMPNewRegistrationSearchTable)parameters.getSecondaryParameter(2, OMPNewRegistrationSearchTable.class);
		view.list(ompService.generateRegistrationTableData(searchFormDTO,userName,passWord,tableObj));
		
	}
	
}
