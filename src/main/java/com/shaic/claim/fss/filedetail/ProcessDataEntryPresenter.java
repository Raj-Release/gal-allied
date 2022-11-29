/**
 * 
 */
package com.shaic.claim.fss.filedetail;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;

/**
 * 
 *
 */
@ViewInterface(ProcessDataEntryWizard.class)
public class ProcessDataEntryPresenter extends AbstractMVPPresenter<ProcessDataEntryWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_FILE_DETAILS = "submit_file_details";
	
	@EJB
	private ProcessDataEntryService dataEntryService;

	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_FILE_DETAILS) final ParameterDTO parameters) {
		SearchDataEntryTableDTO tableDTO = (SearchDataEntryTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String message = dataEntryService.submitProcessDataEntry(tableDTO, userName);
		
			view.buildSuccessLayout(message);
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	

}
