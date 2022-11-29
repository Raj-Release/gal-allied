package com.shaic.claim.cpuautoallocation;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.vaadin.ui.Window;


@ViewInterface(SearchCpuAutoAllocationView.class)
public class SearchCpuAutoAllocationPresenter extends AbstractMVPPresenter<SearchCpuAutoAllocationView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchCpuAutoAllocationService searchService;
	
	public static final String SEARCH_BUTTON_CLICK = "doDoctorSearchTablecpuautoallocation";
	
	public static final String CPU_SEARCH_CRITERIA = "searchcpucriteriaautoallocation";
	
	public static final String BUILD_SUBMIT_LAYOUT = "Build_submit_layout_cpuautoallocation";
	
	public static final String SUBMIT_CPU_ALLOCATION = "Submit_cpuautoallocation";
	
	@EJB
	private SearchCpuAutoAllocationService searchDoctorDetailsService;

	private Window popup;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCpuAutoAllocationDTO searchFormDTO = (SearchCpuAutoAllocationDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchDoctorDetailsService.search(searchFormDTO));
	}
	
	
	public void generateLayoutBasedOnType(@Observes @CDIEvent(BUILD_SUBMIT_LAYOUT) final ParameterDTO parameters) {
		
		view.buildSubmitLayout();
	}
	
	public void submitReallocation(@Observes @CDIEvent(SUBMIT_CPU_ALLOCATION) final ParameterDTO parameters){
		List<SearchCpuAutoAllocationTableDTO> tableDTOList = (List<SearchCpuAutoAllocationTableDTO>) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		Integer count = searchService.updateCpuMaster(tableDTOList,userName);
		view.buildSuccessLayout(count);
	}
	
	public void setupCpuDetails(
			@Observes @CDIEvent(CPU_SEARCH_CRITERIA) final ParameterDTO parameters) {
		SearchCpuAutoAllocationTableDTO viewSearchCriteriaDTO = (SearchCpuAutoAllocationTableDTO) parameters.getPrimaryParameter();
		view.setCpuDetails(viewSearchCriteriaDTO);
	}
	
	@Override
	public void viewEntered() {
		
		
	}
}
