package com.shaic.reimbursement.investigationmaster;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

@ViewInterface(InvestigationMasterWizardView.class)
public class InvestigationMasterWizardPresenter extends AbstractMVPPresenter<InvestigationMasterWizardView> {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public static final String INVESTIGATION_MASTER_WIZARD_SUBMIT = "Investigation Master Submit For Reimbursement";
	
	public static final String CREATE_INVESTIGATION = "Investigation Master Create Investigation";
	
	public static final String GET_INVESTIGATION_MASTER_STATE  = "Investigator Master State";
	
	public static final String GET_INVESTIGATION_MASTER_CITY  = "Investigator Master  City";
	
	public static final String GET_INVESTIGATION_MASTER_ALLOCATION_TO  = "Investigator Master  Allocation To";
	
	public static final String GET_INVESTIGATION_MASTER_INVESTIGATOR  = "Investigator Master  Investigator";
	
	public static final String GET_INVESTIGATION_MASTER_COORDINATOR = "Investigator Master  Coordinator";
	
	public static final String GET_INVESTIGATION_MASTER_PRIVATE_INVESTIGATOR = "Investigator Master Private Investigator";
	
	public static final String GET_INVESTIGATION_MASTER_ZONE = "Investigator Master Zone";
	
	@Inject
	private InvestigationMasterService investigationService;
	
	@Inject
	private InvestigationMasterUI investigationMasterUIObj;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	
	public void createInvestigation(@Observes @CDIEvent(CREATE_INVESTIGATION) final ParameterDTO parameters){
		InvestigationMasterDTO investigationMasterDTO = (InvestigationMasterDTO) parameters.getPrimaryParameter();
		view.initView(investigationMasterDTO);
		
	}
	
	public void submitWizard(
			@Observes @CDIEvent(INVESTIGATION_MASTER_WIZARD_SUBMIT) final ParameterDTO parameters) {
		InvestigationMasterDTO investigationMasterDTO = (InvestigationMasterDTO) parameters.getPrimaryParameter();
		investigationService.saveInvestigatorMasterDetails(investigationMasterDTO);
		view.buildSuccessLayout();
}

	public void getState(
			@Observes @CDIEvent(GET_INVESTIGATION_MASTER_STATE) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> stateContainer = masterService.getStateListSelectValue();

	}
	
	public void getCity(
			@Observes @CDIEvent(GET_INVESTIGATION_MASTER_CITY) final ParameterDTO parameters) {
		Long state_id = (Long) parameters.getPrimaryParameter();
		//ComboBox cityCmbo = (ComboBox) parameters.getSecondaryParameters()[0];
		//SelectValue selecteCity = (SelectValue) parameters.getSecondaryParameters()[0];
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
		investigationMasterUIObj.setCityContainer(citySelectValueContainer);
	}	
	
	public void getAllocationTo(
			@Observes @CDIEvent(GET_INVESTIGATION_MASTER_ALLOCATION_TO) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> allocationContainer = masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION);
		//investigationMasterUIObj.setAllocationContainer(allocationContainer);
	}

		

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
