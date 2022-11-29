package com.shaic.claim.medical.opinion;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(OpinionValidationView.class)
public class OpinionValidationPresenter extends AbstractMVPPresenter<OpinionValidationView>{
	
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "doSearchOpinionTable";
	
	public static final String SUBMIT_BUTTON_CLICK = "submitDetails";
	
	public static final String SEARCH_COMPLETED_CASES = "searchCompletedCases";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OpinionValidationService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		OpinionValidationFormDTO searchFormDTO = (OpinionValidationFormDTO) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> doctorNames = (BeanItemContainer<SelectValue>) parameters.getSecondaryParameter(0, BeanItemContainer.class);
		view.list(searchService.search(searchFormDTO, doctorNames));
	}

	@SuppressWarnings({ "deprecation" })
	public void submitDetails(@Observes @CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters) {
		HashMap<Long, Boolean> opinionStatusMap = (HashMap<Long, Boolean>) parameters.getPrimaryParameter();
		HashMap<Long, String> opinionRemarksMap = (HashMap<Long, String>) parameters.getSecondaryParameter(0, HashMap.class);
		String userName = (String)parameters.getSecondaryParameter(1, String.class);
		searchService.submitStatus(opinionStatusMap, opinionRemarksMap, userName);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void fetchCompletedCases(@Observes @CDIEvent(SEARCH_COMPLETED_CASES) final ParameterDTO parameters) {
		OpinionValidationFormDTO searchFormDTO = (OpinionValidationFormDTO) parameters.getPrimaryParameter();
		view.completedCaseList(searchService.fetchCompletedCases(searchFormDTO));
	}
	
	@Override
	public void viewEntered() {
		
	}
	
}