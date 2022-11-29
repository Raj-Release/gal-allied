package com.shaic.reimbursement.assigninvesigation;

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
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

@ViewInterface(AssignInvestigatorView.class)
public class AssignInvestigatorPresenter extends
		AbstractMVPPresenter<AssignInvestigatorView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_REPRESENTATIVE = "Representative for Reimbursement";
	
	public static final String SET_REPRESENTATIVE = "Set Representative for Reimbursement";
	
	public static final String SEARCH_CITY = "Search City For Reimbursement";
	
	public static final String GET_STATE  = "Search State";
	
	public static final String GET_CITY  = "Search Investigator City";
	
	public static final String GET_ALLOCATION_TO  = "Search Allocation To";
	
	public static final String GET_INVESTIGATOR  = "Search Investigator";
	
	public static final String GET_COORDINATOR = "Search Coordinator";
	
	public static final String GET_PRIVATE_INVESTIGATOR = "Search Investigator";
	
	public static final String GET_ZONE = "Search Zone";
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private AssignInvestigatorUI assignInvestigatorUI;	
	
	@Inject 
	private AssignMutiInvestigatorTable multiInvestigTable;
	
	public void getState(
			@Observes @CDIEvent(GET_STATE) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> stateContainer = masterService
				.getStateListSelectValue();

//		multiInvestigTable.setstateContainer(stateContainer);
	}
	
	public void getCity(
			@Observes @CDIEvent(GET_CITY) final ParameterDTO parameters) {
		Long state_id = (Long) parameters.getPrimaryParameter();
		ComboBox cityCmbo = (ComboBox) parameters.getSecondaryParameters()[0];
		SelectValue selecteCity = (SelectValue) parameters.getSecondaryParameters()[1];
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
		multiInvestigTable.setCityContainer(citySelectValueContainer,cityCmbo,selecteCity);
	}	
	
	public void getAllocationTo(
			@Observes @CDIEvent(GET_ALLOCATION_TO) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> allocationContainer = masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION);
//		multiInvestigTable.setAllocationContainer(allocationContainer);
	}

//	public void getInvestigator(
//			@Observes @CDIEvent(GET_INVESTIGATOR) final ParameterDTO parameters) {
//		
//		Long stateId = (Long) parameters.getPrimaryParameter();
//		Long cityId = (Long) parameters.getSecondaryParameters()[0];
//		Long allocationToId = (Long) parameters.getSecondaryParameters()[1];
//		ComboBox investigCmbo = (ComboBox) parameters.getSecondaryParameters()[2];
//		
//		List<TmpInvestigation> tmpInvestigationList = investigationService.getRepresentativeList(stateId, cityId, allocationToId);
//		multiInvestigTable.setInvestigatorDetails(tmpInvestigationList,investigCmbo);
//	}
		
	
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
		assignInvestigatorUI
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
		assignInvestigatorUI.setInvestigatorData(tmpInvestigationDTOList);
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
			assignInvestigatorUI.setInvestigatorDetails(searchRepresentativeTableDTO);
		}
		
	}
	
	public void getCoordinator(
			@Observes @CDIEvent(GET_COORDINATOR) final ParameterDTO parameters) {
		String selectedZone = (String) parameters.getPrimaryParameter();
		ComboBox coordinatorComb = (ComboBox) parameters.getSecondaryParameters()[0];
		SelectValue coordinatorSelect = (SelectValue) parameters.getSecondaryParameters()[1];
		
		BeanItemContainer<MasPrivateInvestigator> coordinatorContainer = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		BeanItemContainer<SelectValue> coordinatorSelectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class); 
		coordinatorContainer = masterService.getPrivateInvestigationCoordinatorName(selectedZone);
		
		List<MasPrivateInvestigator> privateInvCordList = coordinatorContainer.getItemIds();
		for (MasPrivateInvestigator masPrivateInvestigator : privateInvCordList) {
			Long i = 0l;
			SelectValue coordSelectValue = new SelectValue();
			coordSelectValue.setId(i);
			coordSelectValue.setValue(masPrivateInvestigator.getCordinatorName() != null ? masPrivateInvestigator.getCordinatorName() : "");
			coordSelectValue.setCommonValue(masPrivateInvestigator.getCoridnatorCode() != null ? masPrivateInvestigator.getCoridnatorCode() : "");
			coordinatorSelectValueContainer.addBean(coordSelectValue);
			i++;
		}
		multiInvestigTable.setCoordinatorContainer(coordinatorSelectValueContainer,coordinatorComb,coordinatorSelect);
	}
	
	public void getPrivateInvestigatorName(
			@Observes @CDIEvent(GET_PRIVATE_INVESTIGATOR) final ParameterDTO parameters){
		String selectedCordinator = (String) parameters.getPrimaryParameter();
		ComboBox investigatorComb = (ComboBox) parameters.getSecondaryParameters()[0];
		SelectValue investigatorSelect = (SelectValue) parameters.getSecondaryParameters()[1];
		
		BeanItemContainer<MasPrivateInvestigator> privateInvestigator = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		BeanItemContainer<SelectValue> privateInvestigatorSelectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		privateInvestigator = masterService.getPrivateInvestigatorNames(selectedCordinator);
		List<MasPrivateInvestigator> privateInvestigationNameList = privateInvestigator.getItemIds();
		for (MasPrivateInvestigator masPrivateInvestigator : privateInvestigationNameList) {
			SelectValue investigatorName = new SelectValue();
			investigatorName.setId(masPrivateInvestigator.getPrivateInvestigationKey());
			investigatorName.setValue(masPrivateInvestigator.getInvestigatorName());
			privateInvestigatorSelectValueContainer.addBean(investigatorName);
			
		}
		multiInvestigTable.setPrivateInvestigatorNameContainer(privateInvestigatorSelectValueContainer,investigatorComb,investigatorSelect);
	}
	
	public void getZones(
			@Observes @CDIEvent(GET_ZONE) final ParameterDTO parameters){
		String selectedAllocationTo = (String) parameters.getPrimaryParameter();
		ComboBox zonecmb = (ComboBox) parameters.getSecondaryParameters()[0];
		SelectValue zoneList = (SelectValue) parameters.getSecondaryParameters()[1];
		
		BeanItemContainer<SelectValue> zoneSelectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(selectedAllocationTo != null && selectedAllocationTo.equalsIgnoreCase("Private")){
			List<SelectValue> privateInvestigator = new ArrayList<SelectValue>();
			//privateInvestigator = masterService.getPrivateInvestigatorsZones();
			privateInvestigator = masterService.getPrivateInvestigatorsZoneNames();
			
			for (SelectValue selectValue : privateInvestigator) {
				zoneSelectValueContainer.addBean(selectValue);
			}
		} else {
			BeanItemContainer<SelectValue> zoneCode = new BeanItemContainer<SelectValue>(SelectValue.class);
			zoneCode = masterService.getInvestigatorZoneNames();
			List<SelectValue> zonesList = zoneCode.getItemIds();
			for (SelectValue selectValue : zonesList) {
				zoneSelectValueContainer.addBean(selectValue);
			}
		}
		multiInvestigTable.setZonesContainer(zoneSelectValueContainer,zonecmb,zoneList);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
