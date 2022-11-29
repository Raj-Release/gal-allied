package com.shaic.reimbursement.investigationgrading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeTableDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.TmpInvestigation;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InvestigationGradingPageView.class)
public class InvestigationGradingPagePresenter extends
AbstractMVPPresenter<InvestigationGradingPageView>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_REPRESENTATIVE = "Representative for Reimbursement Investigation Grading";
	
	public static final String SET_REPRESENTATIVE = "Set Representative for Reimbursement Investigation Grading";
	
	public static final String SEARCH_CITY = "Search City For Reimbursement Investigation Grading";
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private InvestigationGradingPage investigationGradingPage;
	
	public void searchCityForReimbursement(
			@Observes @CDIEvent(SEARCH_CITY) final ParameterDTO parameters) {
		Long state_id = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<CityTownVillage> cityContainer = new BeanItemContainer<CityTownVillage>(
				CityTownVillage.class);
		BeanItemContainer<SelectValue> citySelectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		cityContainer = masterService.getCityTownVillage(state_id);
		List<CityTownVillage> cityTownVillageList = cityContainer.getItemIds();
		for (CityTownVillage cityTownVillage : cityTownVillageList) {
			SelectValue stateSelectValue = new SelectValue();
			stateSelectValue.setId(cityTownVillage.getKey());
			stateSelectValue.setValue(cityTownVillage.getValue());
			citySelectValueContainer.addBean(stateSelectValue);
		}
		investigationGradingPage
				.setCityContainer(citySelectValueContainer);
	}
	
	
	@SuppressWarnings("unchecked")
	public void searchRepresentative(
			@Observes @CDIEvent(SEARCH_REPRESENTATIVE) final ParameterDTO parameters) {
		Map<String,Object> searchRepresentative = (Map<String,Object>) parameters.getPrimaryParameter();
		Long stateId = (Long) searchRepresentative.get("stateId");
		Long cityId = (Long) searchRepresentative.get("cityId");
		Long catgoryId = (Long) searchRepresentative.get("catgoryId");
		List<TmpInvestigation> tmpInvestigationList = investigationService.getRepresentativeList(stateId, cityId, catgoryId);
		List<SearchRepresentativeTableDTO> tmpInvestigationDTOList = new ArrayList<SearchRepresentativeTableDTO>();
		for (TmpInvestigation tmpInvestigation : tmpInvestigationList) {
			SearchRepresentativeTableDTO searchRepresentativeTableDTO = new SearchRepresentativeTableDTO();
			searchRepresentativeTableDTO.setKey(tmpInvestigation.getKey());
			searchRepresentativeTableDTO.setRepresentativeCode(tmpInvestigation.getInvestigatorCode());
			searchRepresentativeTableDTO.setRepresentativeName(tmpInvestigation.getInvestigatorName());
			searchRepresentativeTableDTO.setRepresentativeContactNo(tmpInvestigation.getPhoneNumber().toString());
			searchRepresentativeTableDTO.setRepsentativeMobileNo(tmpInvestigation.getMobileNumber().toString());
			tmpInvestigationDTOList.add(searchRepresentativeTableDTO);
		}
		investigationGradingPage.setInvestigatorData(tmpInvestigationDTOList);
	}
	
	@SuppressWarnings("unchecked")
	public void setRepresentative(
			@Observes @CDIEvent(SET_REPRESENTATIVE) final ParameterDTO parameters) {
		SelectValue investigationSelectValue = (SelectValue) parameters.getPrimaryParameter();
		TmpInvestigation tmpInvestigation = investigationService.getRepresentativeListByInvestigationKey(investigationSelectValue.getId());
		if(tmpInvestigation!=null){
			SearchRepresentativeTableDTO searchRepresentativeTableDTO = new SearchRepresentativeTableDTO();
			if(tmpInvestigation.getPhoneNumber() != null){
				searchRepresentativeTableDTO.setRepresentativeContactNo(tmpInvestigation.getPhoneNumber().toString());
			}
			if(tmpInvestigation.getMobileNumber() != null){
				searchRepresentativeTableDTO.setRepsentativeMobileNo(tmpInvestigation.getMobileNumber().toString());
			}
			investigationGradingPage.setInvestigatorDetails(searchRepresentativeTableDTO);
		}
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}



}
