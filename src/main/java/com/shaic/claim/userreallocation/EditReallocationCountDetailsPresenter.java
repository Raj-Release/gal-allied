package com.shaic.claim.userreallocation;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(EditReallocationCountDetailsView.class)
public class EditReallocationCountDetailsPresenter extends AbstractMVPPresenter<EditReallocationCountDetailsView> {
	
	//public static final String SET_INTIMATION_DETAILS_RE_ALLOCATION = "set intimation details re-allocate";

	public static final String RETRIVE_RE_ALLOCATION = "Retrive re-allocate User";
	
	public static final String SUBMIT_INTIMATION_DETAILS_RE_ALLOCATION = "submit intimation details re-allocate";
	
	@EJB
	DoctorReallocationSearchCriteriaService searchService;
	
	@EJB
	private ReallocationDoctorIntimationDetailsService doctorIntimationService;
	
	public void searchRetriveUser(
			@Observes @CDIEvent(RETRIVE_RE_ALLOCATION) final ParameterDTO parameters) {
		
	/*	String countType = (String)parameters.getPrimaryParameter(String.class);
		
		List<MasUser> tmpList = (List<MasUser>) searchService.getDoctorDetailsBasedOnLimit();*/
		//view.setUserDetails(tmpList);
	}
	
	public void submitIntimationDetailsReallocation(
			@Observes @CDIEvent(SUBMIT_INTIMATION_DETAILS_RE_ALLOCATION) final ParameterDTO parameters){
		
		SearchReallocationDoctorDetailsTableDTO bean = (SearchReallocationDoctorDetailsTableDTO) parameters
				.getPrimaryParameter();
		
		List<AutoAllocationDetailsTableDTO> detailsList = bean.getIntimationDetailsList();
		
		String userName = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		Boolean isupdated = doctorIntimationService.updateAutoAllocationDetails(detailsList,userName);
		
		view.submitValues(isupdated);
		
		}
	
	/*public void setIntimationDetailsOfUser(
			@Observes @CDIEvent(SET_INTIMATION_DETAILS_RE_ALLOCATION) final ParameterDTO parameters) {
		
		SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) parameters
				.getPrimaryParameter();

		String countType = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		List<AutoAllocationDetails> list = doctorIntimationService.getAssignedDetails(tableDTO.getEmpId());
		
		if(countType != null){
			if(countType.equalsIgnoreCase(SHAConstants.REALLOCATION_COMPLETED)){
				list = doctorIntimationService.getCompletedAssignedDetails(tableDTO.getEmpId(), SHAConstants.REALLOCATION_COMPLETED_STATUS);
			}else if(countType.equalsIgnoreCase(SHAConstants.REALLOCATION_PENDING)){
				list = doctorIntimationService.getCompletedAssignedDetails(tableDTO.getEmpId(), SHAConstants.PENDING);
			}
		
		}
		
		view.setUserIntimationDetails(list);
	}*/

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
