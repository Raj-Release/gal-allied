package com.shaic.main.navigator.ui;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.InitiateLumenPolicyRequestWizardImpl;
import com.shaic.claim.lumen.create.InitiateLumenRequestView;
import com.shaic.claim.lumen.create.InitiateLumenRequestWizard;
import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.initiatorquerycase.ProcessInitiatorWizard;
import com.shaic.claim.lumen.initiatorquerycase.SearchLumenInitiatorRequestView;
import com.shaic.claim.lumen.querytomis.ProcessMISWizard;
import com.shaic.claim.lumen.querytomis.SearchLumenQueryToMISView;
import com.shaic.claim.lumen.search.SearchLumenRequestView;
import com.shaic.claim.lumen.searchcoordinator.ProcessCoordinatorWizard;
import com.shaic.claim.lumen.searchcoordinator.SearchLumenCoordinatorRequestView;
import com.shaic.claim.lumen.searchlevelone.ProcessLevelOneWizard;
import com.shaic.claim.lumen.searchlevelone.SearchLumenLevelOneRequestView;
import com.shaic.claim.lumen.searchleveltwo.ProcessLevelTwoWizard;
import com.shaic.claim.lumen.searchleveltwo.SearchLumenLevelTwoRequestView;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummaryReportView;
import com.shaic.claim.reports.lumenstatus.LumenStatusWiseReportView;
import com.shaic.domain.Claim;
import com.shaic.domain.MasterService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
@ViewInterface(LumenMenuView.class)
public class LumenMenuPresenter extends AbstractMVPPresenter<LumenMenuView>{

	@EJB 
	private MasterService masterService;
	
	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	protected void showLumenInitiateReqPage(
			@Observes @CDIEvent(MenuItemBean.CREATE_LUMEN) final ParameterDTO parameters){
		view.setViewG(InitiateLumenRequestView.class, true);
	}
	
	protected void showLumenSearchReqPage(
			@Observes @CDIEvent(MenuItemBean.SEARCH_LUMEN) final ParameterDTO parameters){
		view.setViewG(SearchLumenRequestView.class, true);
	}
	
	protected void showLumenSearchLevelOneReqPage(
			@Observes @CDIEvent(MenuItemBean.LUMEN_REQUEST_LEVEL_I) final ParameterDTO parameters){
		view.setViewG(SearchLumenLevelOneRequestView.class, true);
	}
	
	protected void showLumenSearchCoordinatorReqPage(
			@Observes @CDIEvent(MenuItemBean.LUMEN_REQUEST_CO_ORDINATOR) final ParameterDTO parameters){
		view.setViewG(SearchLumenCoordinatorRequestView.class, true);
	}
	
	protected void showLumenSearchLevelTwoReqPage(
			@Observes @CDIEvent(MenuItemBean.LUMEN_REQUEST_LEVEL_II) final ParameterDTO parameters){
		view.setViewG(SearchLumenLevelTwoRequestView.class, true);
	}
	
	protected void showQueryToMISPage(
			@Observes @CDIEvent(MenuItemBean.LUMEN_REQUEST_MIS) final ParameterDTO parameters){
		view.setViewG(SearchLumenQueryToMISView.class, true);
	}
	
	protected void showLumenIntiatorQueryCasePage(
			@Observes @CDIEvent(MenuItemBean.LUMEN_REQUEST_INITIATOR) final ParameterDTO parameters){
		view.setViewG(SearchLumenInitiatorRequestView.class, true);
	}
	
	protected void showLumenInitiateRequestPage(@Observes @CDIEvent(MenuItemBean.INITIATE_REQUEST) final ParameterDTO parameters) {
		LumenSearchResultTableDTO createDTO = (LumenSearchResultTableDTO)parameters.getPrimaryParameter();
		Claim claim = masterService.getClaimByIntimationKey(createDTO.getIntimationKey());
		if(claim != null){
			createDTO.setClaim(claim);
		}
		view.renderLumenInitiateRequestPage(InitiateLumenRequestWizard.class, createDTO);
	}
	
	protected void showPolicyLumenInitiateRequestPage(@Observes @CDIEvent(MenuItemBean.INITIATE_POLICY_REQUEST) final ParameterDTO parameters) {
		LumenPolicySearchResultTableDTO createDTO = (LumenPolicySearchResultTableDTO)parameters.getPrimaryParameter();
		view.renderPolicyLumenInitiateRequestPage(InitiateLumenPolicyRequestWizardImpl.class, createDTO);
	}
	
	protected void showLumenLevelOnePage(@Observes @CDIEvent(MenuItemBean.LEVEL_I_WIZARD) final ParameterDTO parameters) {
		LumenRequestDTO lumenRequestDTO = (LumenRequestDTO)parameters.getPrimaryParameter();
		view.renderLevelOneWizardPage(ProcessLevelOneWizard.class, lumenRequestDTO);
	}
	
	protected void showLumenCoordinatorPage(@Observes @CDIEvent(MenuItemBean.COORDINATOR_WIZARD) final ParameterDTO parameters) {
		LumenRequestDTO lumenRequestDTO = (LumenRequestDTO)parameters.getPrimaryParameter();
		view.renderCoordinatorWizardPage(ProcessCoordinatorWizard.class, lumenRequestDTO);
	}
	
	protected void showLumenLevelTwoPage(@Observes @CDIEvent(MenuItemBean.LEVEL_II_WIZARD) final ParameterDTO parameters) {
		LumenRequestDTO lumenRequestDTO = (LumenRequestDTO)parameters.getPrimaryParameter();
		view.renderLevelTwoWizardPage(ProcessLevelTwoWizard.class, lumenRequestDTO);
	}
	
	
	protected void showLumenMISQueryPage(@Observes @CDIEvent(MenuItemBean.QUERY_MIS_WIZARD) final ParameterDTO parameters) {
		LumenRequestDTO lumenRequestDTO = (LumenRequestDTO)parameters.getPrimaryParameter();
		view.renderMISQueryWizardPage(ProcessMISWizard.class, lumenRequestDTO);
	}
	
	protected void showLumenInitiatorCasePage(@Observes @CDIEvent(MenuItemBean.INITIATOR_WIZARD) final ParameterDTO parameters) {
		LumenRequestDTO lumenRequestDTO = (LumenRequestDTO)parameters.getPrimaryParameter();
		view.renderInitiatorWizardPage(ProcessInitiatorWizard.class, lumenRequestDTO);
	}
	
	
	/**
	 * No Parameter,  part of CR R0798 
	 * @param parameters
	 */
	protected void showLumenStatusWiseReport(
			@Observes @CDIEvent(MenuItemBean.LUMEN_STATUS_WISE_REPORT) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> empTypeContainer = masterService.getClaimtypeContainer();
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
		BeanItemContainer<SelectValue> cpuCodeContainer = usertoCPUMapService.getRevisedUserCpuContainer(userId);
		BeanItemContainer<SelectValue> lumenStatusContainer = masterService.getLumenStatusContainer();
		BeanItemContainer<SelectValue> clmTypeContainer = masterService.getClaimtypeContainer();
		
	    view.setLumenStatusReportView(LumenStatusWiseReportView.class, cpuCodeContainer,lumenStatusContainer, clmTypeContainer);
	}
	
	@Override
	public void viewEntered() {
		System.out.println("LumenMenuPresenter called...");
	}

}
