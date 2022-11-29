package com.shaic.claim.userreallocation;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.AutoAllocationDetails;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;


@ViewInterface(SearchReallocationDoctorDetailsView.class)
public class SearchReallocationDoctorDetailsPresenter extends AbstractMVPPresenter<SearchReallocationDoctorDetailsView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchReallocationDoctorDetailsService searchService;
	
	@EJB
	private ReallocationDoctorIntimationDetailsService doctorIntimationService;
	
	@Inject
	private Instance<AutoAllocationAssignedDetailsTable> autoAllocationAssignedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationCompletedDetailsTable> autoAllocationCompletedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationPendingDetailsTable> autoAllocationPendingDetailsTableInstance;
	
	@Inject
	private Instance<EditReallocationCountDetailsForm> reallocationForm;
	
	public static final String SEARCH_BUTTON_CLICK = "doDoctorSearchTableReallocation";
	
	public static final String DOCTOR_SEARCH_CRITERIA = "searchdoctorcriteriaReallocation";
	
	public static final String BUILD_SUBMIT_LAYOUT = "Build_submit_layout_reallocation";
	
	public static final String SUBMIT_REALLOCATION = "Submit_reallocation";
	
	public static final String SHOW_INTIMATION_STATUS = "show_intimation_status_reallocation";
	
	public static final String SHOW_INTIMATION_SEARCH = "show_intimation_search_reallocation";
	
	@EJB
	private SearchReallocationDoctorDetailsService searchDoctorDetailsService;

	private Window popup;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchReallocationDoctorNameDTO searchFormDTO = (SearchReallocationDoctorNameDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchDoctorDetailsService.search(searchFormDTO));
	}
	
	
	public void generateLayoutBasedOnType(@Observes @CDIEvent(BUILD_SUBMIT_LAYOUT) final ParameterDTO parameters) {
		
		view.buildSubmitLayout();
	}
	
	public void submitReallocation(@Observes @CDIEvent(SUBMIT_REALLOCATION) final ParameterDTO parameters){
		List<SearchReallocationDoctorDetailsTableDTO> tableDTOList = (List<SearchReallocationDoctorDetailsTableDTO>) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		Map<String, Object> map = searchService.updateStatusProcessing(tableDTOList,userName);
		view.buildSuccessLayout(map);
	}
	
	public void setupDoctorDetails(
			@Observes @CDIEvent(DOCTOR_SEARCH_CRITERIA) final ParameterDTO parameters) {
		SearchReallocationDoctorDetailsTableDTO viewSearchCriteriaDTO = (SearchReallocationDoctorDetailsTableDTO) parameters.getPrimaryParameter();
		view.setDoctorDetails(viewSearchCriteriaDTO);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showCountDetailsView(@Observes @CDIEvent(SHOW_INTIMATION_STATUS) final ParameterDTO parameters) {
		
		SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) parameters.getPrimaryParameter();
		
		String countType = (String)parameters.getSecondaryParameter(0, String.class);
		
		popup = new com.vaadin.ui.Window();
		
		EditReallocationCountDetailsViewImpl detailsView = new EditReallocationCountDetailsViewImpl();
		
		//detailsView.initPresenter();
		detailsView.initIntimationTable(autoAllocationAssignedDetailsTableInstance,autoAllocationCompletedDetailsTableInstance,autoAllocationPendingDetailsTableInstance,reallocationForm);
		
		List<AutoAllocationDetailsTableDTO> assignedList = doctorIntimationService
				.getAssignedDetails(tableDTO.getEmpId());

		List<AutoAllocationDetailsTableDTO> completedList = doctorIntimationService.getCompletedAssignedDetails(
						tableDTO.getEmpId(),
						SHAConstants.REALLOCATION_COMPLETED_STATUS);
			
		List<AutoAllocationDetailsTableDTO> pendingList = doctorIntimationService.getCompletedAssignedDetails(
						tableDTO.getEmpId(), SHAConstants.PENDING);
			
		detailsView.init(tableDTO, popup, countType, assignedList, completedList, pendingList);
		
		//detailsView.getContent();
		
		//detailsView.setIntimationDetails(list);
		
		popup.setCaption("Re-Allocation Screen");
		popup.setWidth("85%");
		popup.setHeight("85%");
		popup.setContent(detailsView);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void generateIntimationTable(@Observes @CDIEvent(SHOW_INTIMATION_SEARCH) final ParameterDTO parameters) {
		
		String intimation = (String)parameters.getPrimaryParameter(String.class);
		
		AutoAllocationDetails detail = doctorIntimationService.getIntimation(intimation);
		
		view.buildIntimationTableLayout(detail);
	}
	
	@Override
	public void viewEntered() {
		
		
	}
}
