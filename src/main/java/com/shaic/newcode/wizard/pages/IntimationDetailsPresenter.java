package com.shaic.newcode.wizard.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalDTO;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalMapper;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.claim.selecthospital.ui.HospitalMapper;
import com.shaic.claim.selecthospital.ui.HospitalSearch;
import com.shaic.claim.selecthospital.ui.HospitalSearchTable;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.NewBabyIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewBabyIntimationMapper;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewBabyIntimationDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;

@ViewInterface(IntimationDetailsView.class)
public class IntimationDetailsPresenter extends
		AbstractMVPPresenter<IntimationDetailsView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String INTIMATION_SAVE_EVENT = "intimation_save_event";

	public static final String INTIMATION_CANCEL_EVENT = "intimation_cancel_event";

	public static final String INTIMATION_SUBMIT_EVENT = "intimation_submit_event";

	public static final String SET_REFERENCE_DATA = "intimation_set_edit_data";

	public static final String BUILD_NEW_BORN_BABY_TABLE = "build_new_baby_table";

	public static final String UPDATE_SUBMIT_EVENT = "update_submit_event";

	public static final String SEARCH_HOSPITAL_BY_ID = "search_hospital_by_id";

	public static final String SEARCH_HOSPITAL_BY_NAME = "search_hospital_by_name";

	public static final String SEARCH_HOSPITAL_KEY = "search_hospital_key";

	public static final String SET_EDIT_HOSPITAL_HUMANTASK = "set_edit_hospital_human_task";
		
	public static final String ADD_REASON_FOR_ADMISSION = "add_reason_for_admission";
	
	public static final String GET_REASON_FOR_ADMISSION = "get_reason_for_admission";
	
	public static final String INTIALIZE_REASON_FOR_ADMISSION = "intialize_reason_for_admission";
		
	public static final String INTIMATION_SAVE_SUBMIT_EVENT = "intimation_save_submit_event";
	
	public static final String GMC_INSURED_SELECTED = "gmc Insured details List";
	public static final String HOSPITAL_CONTACT_NO_SEARCH = "hospital_contact_no_search";
	
	//private NewIntimationDto newIntimationDto;

	@EJB
	private MasterService masterService;

	@EJB
	private DBCalculationService dBCalculationService;

	@EJB
	private UpdateHospitalService updateHospitalService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private InsuredService insuredService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private PolicyService policyService;
	
	@EJB
	private PremiaPullService premiaPullService;

	@Inject
	private Instance<HospitalMapper> hospitalMapper;

	@Inject
	private Instance<HospitalSearchTable> hospitalSearchTable;

	@Inject
	private IntimationDetailsPage intimationDetailsPage;

	@Inject
	private HospitalSearch hospitalSearch;
	
	//Added for Auto registration process
	//RM-STUB
	/*@EJB
	private StarFaxSimulatorService starFaxService;*/

	private Map<String, Object> referenceData = new HashMap<String, Object>();

	private List<HospitalDto> searchHospitalDTO;

	//private HumanTask editHospitalHumanTask;

	@Override
	public void viewEntered() {

		// setPolicyValues(newIntimationDto.getTmpPolicy());
		// BeanItemContainer<TmpInsured> insuredList =
		// insuredService.getInsuredList(newIntimationDto.getTmpPolicy().getPolNo());
		// setReferece(insuredList);
		// this.view.setReferenceData(referenceData);

	}
	
	public void loadReasonForAdmissionValues(
			@Observes @CDIEvent(INTIALIZE_REASON_FOR_ADMISSION) final ParameterDTO parameters) {
		view.intializeReasonForAdmission(masterService.getSelectValueContainerForReasonForAdmission());

	}

	

	public void setUpEditHospitalHumanTask(
			@Observes @CDIEvent(SET_EDIT_HOSPITAL_HUMANTASK) final ParameterDTO parameters) {
	//	editHospitalHumanTask = (HumanTask) parameters.getPrimaryParameter();

	}

	public void setUpReferenceObjectForEdit(
			@Observes @CDIEvent(SET_REFERENCE_DATA) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters
				.getPrimaryParameter();
//		TmpPolicy policy = newIntimationDto.getTmpPolicy();
//		this.view.getModel().setTmpPolicy(policy);
//		BeanItemContainer<TmpInsured> insuredList = insuredService
//				.getInsuredList(policy.getPolNo());

		BeanItemContainer<Insured> insuredList = insuredService
				.getCLSInsuredList(newIntimationDto.getPolicy().getPolicyNumber());
		
		String sumInsured = newIntimationDto.getPolicySummary()!=null?newIntimationDto.getPolicySummary().getSumInsured()!=null?newIntimationDto.getPolicySummary().getSumInsured():"0":"0";
	
		setReferece(insuredList,sumInsured);
		setPolicyValues(newIntimationDto.getPolicy());

		// CustomLazyContainer customLazyContainer = new CustomLazyContainer(3,
		// "name", hospitalService,
		// "hospital",newIntimationDto.getState(),newIntimationDto.getCity(),newIntimationDto.getArea());
		// customLazyContainer.addContainerProperty("name", HospitalDto.class,
		// null);
		// referenceData.put("hospitalDto", customLazyContainer);

		this.view.setReferenceData(referenceData);
		this.view.setModel(newIntimationDto);
	}

	public void setReferece(BeanItemContainer<Insured> insuredList, String sumInsured) {
		referenceData.put("modeOfIntimation", masterService
				.getSelectValueContainer(ReferenceTable.MODE_OF_INTIMATION));
		referenceData.put("intimatedBy", masterService
				.getSelectValueContainer(ReferenceTable.INTIMATED_BY));
		referenceData.put("hospitalType", masterService
				.getSelectValueContainer(ReferenceTable.HOSPITAL_TYPE));
		referenceData.put("admissionType", masterService
				.getSelectValueContainer(ReferenceTable.ADMISSION_TYPE)); //admissiontype
		referenceData.put("managementType", masterService
				.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService
				.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("insuredList", insuredList);
		referenceData.put("sumInsured", sumInsured);
//		referenceData.put("lateIntimationReason",masterService
//				.getSelectValueContainer(ReferenceTable.LATE_INTIMATION_REASON));
		referenceData.put("babyRelationship", masterService
				.getSelectValueContainer(ReferenceTable.PNC_RELATIONSHIP));
		referenceData.put("relapseofIllness", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("claimType", masterService
				.getSelectValueContainer(ReferenceTable.CLAIM_TYPE)); // claim type
		
	}

	public void getReasonforAdmissionForEdit(@Observes @CDIEvent(GET_REASON_FOR_ADMISSION) final ParameterDTO parameters)
	{
		NewIntimationDto intiationDto = (NewIntimationDto)parameters.getPrimaryParameter();
		referenceData.put("reasonForAdmission",masterService.getSelectValueContainerForReasonForAdmission(intiationDto));
		this.view.setReferenceData(referenceData);	
	}

	public void setPolicyValues(Policy policy) {
		LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
		Product product = policy.getProduct();
		OrganaizationUnit organaizationUnit = policyService.getOrgUnitName(policy.getHomeOfficeCode());
		String productType = policy.getProduct() != null ? policy.getProduct().getProductType() : null;
		
		policyValues.put("Policy Number", policy.getPolicyNumber());
		policyValues.put("Insured Name", policy.getProposerFirstName() != null ? policy.getProposerFirstName() : "");
		policyValues.put("Product Type", productType != null ? productType : "");
		policyValues.put("Product Name", (null != product ? product.getValue() : null));
		if(organaizationUnit != null) {
			policyValues.put("PIO Name", organaizationUnit.getOrganizationUnitName());
		} else {
			policyValues.put("PIO Name","");
		}
		referenceData.put("policyValues", policyValues);
	}

	/*public void setEditReferece(IntimationsDto intimationsDTO) {
		Long intimationId = intimationsDTO.getKey().longValue();
//		Long polsysId = intimationsDTO.getPolicyDto().getPolicySysId();
		NewIntimationDto newIntimationDto = intimationService
				.readNewIntimation(intimationId);
//		newIntimationDto.setTmpPolicy(policy);
	}*/

	public void saveNewBornBaby(NewIntimationDto newIntimationDto,
			GalaxyIntimation createdIntimation) {

		List<NewBabyIntimationDto> newBabyListDto = newIntimationDto
				.getNewBabyIntimationListDto();
		List<NewBabyIntimationDto> deletedBabyListDto = newIntimationDto
				.getDeletedBabyList();
		List<NewBabyIntimation> newBabies = new ArrayList<NewBabyIntimation>();
		List<NewBabyIntimation> deletedBabies = new ArrayList<NewBabyIntimation>();
		NewBabyIntimation newBaby;
		NewBabyIntimation deletedBaby;
		if (newBabyListDto != null && !newBabyListDto.isEmpty()) {
			for (NewBabyIntimationDto newBabyIntimationDto : newBabyListDto) {
				newBaby = new NewBabyIntimationMapper()
						.getNewBabyIntimation(newBabyIntimationDto);
				newBaby.setIntimation(createdIntimation);
				if (newBaby != null) {
					newBabies.add(newBaby);
				}
			}
		}

		if (deletedBabyListDto != null && !deletedBabyListDto.isEmpty()) {
			for (NewBabyIntimationDto deletedBabyIntimationDto : deletedBabyListDto) {
				deletedBaby = new NewBabyIntimationMapper()
						.getNewBabyIntimation(deletedBabyIntimationDto);
				deletedBaby.setIntimation(createdIntimation);
				if (deletedBaby != null) {
					deletedBabies.add(deletedBaby);
				}
			}
		}

		if (!newBabies.isEmpty()) {
			// intimationService.createNewBabyIntimation(newBabies);
			intimationService.createNewBabyIntimation(newBabies, deletedBabies);
		}
	}

	public void buildNewBornBabyTable(
			@Observes @CDIEvent(BUILD_NEW_BORN_BABY_TABLE) final ParameterDTO parameters) {
		Map<String, Object> referenceData = new HashMap<String, Object>();

		this.view.buildNewBornBabyTable(
				(Boolean) parameters.getPrimaryParameter(), referenceData);
	}

	public void saveIntimation(
			@Observes @CDIEvent(INTIMATION_SAVE_EVENT) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters
				.getPrimaryParameter();
		GalaxyIntimation intimation = new NewIntimationMapper().getInstance().getNewIntimation(newIntimationDto);

		TmpCPUCode CPUCode = null;
		if (newIntimationDto.getHospitalDto() != null
				&& newIntimationDto.getHospitalDto().getCpuId() != null) {

			CPUCode = policyService.getTmpCpuCode(newIntimationDto
					.getHospitalDto().getCpuId());
			if (CPUCode == null) {
				CPUCode = intimationService
						.getCpuObjectByPincode(newIntimationDto
								.getHospitalDto().getPincode());
			}
			if (CPUCode != null) {
				newIntimationDto.setCpuCode(CPUCode.getCpuCode());
				newIntimationDto.setCpuId(CPUCode.getKey());
			}

		}
		intimation.setCpuCode(CPUCode);
		
		//added for CPU code change in create intimation
		if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
			CPUCode = premiaPullService.getCpuDetails(ReferenceTable.GMC_CPU_CODE);
			if(newIntimationDto.getPolicy() != null && newIntimationDto.getHospitalDto().getNetworkHospitalTypeId() != null
					&& newIntimationDto.getHospitalDto().getHospitalType() != null && newIntimationDto.getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
				if(newIntimationDto.getPolicy().getHomeOfficeCode() != null) {
					OrganaizationUnit branchOffice = premiaPullService.getInsuredOfficeNameByDivisionCode(newIntimationDto.getPolicy().getHomeOfficeCode());
					if(branchOffice != null){
						String officeCpuCode = branchOffice.getCpuCode();
						if(officeCpuCode != null) {
							MasCpuLimit masCpuLimit = premiaPullService.getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
							if(masCpuLimit != null){
								CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
		   }
			intimation.setCpuCode(CPUCode);
		}
		
		if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
			String baayasCpuCodeByPolicy = premiaPullService.getPaayasCpuCodeByPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
			if(baayasCpuCodeByPolicy != null){
				CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
				if(CPUCode != null){
					intimation.setCpuCode(CPUCode);
				}
			}
			
			String jioCpuCodeByPolicy = premiaPullService.getJioPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
			if(jioCpuCodeByPolicy != null){
				CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
				if(CPUCode != null){
					intimation.setCpuCode(CPUCode);
				}
			}
			
			Long tataPolicy = premiaPullService.getTataPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
			if(tataPolicy != null){
				CPUCode = premiaPullService.getMasCpuCode(tataPolicy);
				if(CPUCode != null){
					intimation.setCpuCode(CPUCode);
				}
			}
			String kotakCpuCodeByPolicy = premiaPullService.getKotakPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
			if(kotakCpuCodeByPolicy != null){
				CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
				if(CPUCode != null){
					intimation.setCpuCode(CPUCode);
				}
			}
		}
		if(newIntimationDto.getPolicySummary().getPolicyNo() != null && newIntimationDto.getPolicySummary().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
			CPUCode =  premiaPullService.getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
			intimation.setCpuCode(CPUCode);
		}
		//added for CPU routing
		if(newIntimationDto.getPolicySummary().getPolicyNo() != null  && newIntimationDto.getPolicySummary().getProduct().getKey() != null){
			String CpuCode= premiaPullService.getMasProductCpu(newIntimationDto.getPolicySummary().getProduct().getKey());
			if(CpuCode != null){
				CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(CpuCode));
				intimation.setCpuCode(CPUCode);
			}
		}
		//added for CPU routing
		
		String gpaPolicyDetails = premiaPullService.getGpaPolicyDetails(newIntimationDto.getPolicySummary().getPolicyNo());
		if(gpaPolicyDetails != null){
			TmpCPUCode masCpuCode = premiaPullService.getMasCpuCode(Long.valueOf(gpaPolicyDetails));
			if(masCpuCode != null){
				intimation.setCpuCode(masCpuCode);
				CPUCode = masCpuCode;
			}
		}
		//added for CPU code change in create intimation

		// if(newIntimationDto.getHospitalDto() != null &&
		// newIntimationDto.getHospitalDto().getHospitalType().
		// newIntimationDto.getHospitalDto().getNotRegisteredHospitals() !=
		// null)
		// {
		// Long tempHospitalKey =
		// hospitalService.createTmpHospital(newIntimationDto.getHospitalDto().getNotRegisteredHospitals());
		// newIntimationDto.getHospitalDto().setKey(tempHospitalKey);
		// }

//		intimation = setMasterValuesToPolicy(newIntimationDto, intimation);

		if (newIntimationDto.getRelapseofIllness() != null
				&& newIntimationDto.getRelapseofIllness().getValue() != null) {
			intimation.setRelapseofIllness(newIntimationDto
					.getRelapseofIllness().getValue().toLowerCase()
					.contains("yes") ? "Y" : "N");
		}
		intimation.setPolicy(newIntimationDto.getPolicy());

		intimationService.saveIntimation(newIntimationDto, intimation);
		GalaxyIntimation createdIntimation = intimationService.galaxyIntimationsearchByKey(newIntimationDto.getKey());

		// After created Intimation save the New born babies if any ..
		if (newIntimationDto.getNewBornFlag()) {
			saveNewBornBaby(newIntimationDto, createdIntimation);
		}
//		view.handleSave(createdIntimation);
		view.showSuccessPanel(createdIntimation);
		Notification.show("Intimation Saved Successfully.", Notification.TYPE_WARNING_MESSAGE);

	}

	public void submitIntimation(@Observes @CDIEvent(INTIMATION_SUBMIT_EVENT) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		Map<String, String> userCredentials = (Map<String, String>) parameters.getSecondaryParameters()[0];

		GalaxyIntimation intimation = new NewIntimationMapper().getInstance().getNewIntimation(newIntimationDto);
		intimation.setPolicy(newIntimationDto.getPolicy());
		intimation.setInsured(newIntimationDto.getInsuredPatient());
		TmpCPUCode CPUCode = null;
		
		if (newIntimationDto.getHospitalDto() != null) {
			if (newIntimationDto.getHospitalDto().getCpuId() != null) {
				CPUCode = policyService.getTmpCpuCode(newIntimationDto.getHospitalDto().getCpuId());
			}

			if (CPUCode == null) {
				CPUCode = intimationService.getCpuObjectByPincode(newIntimationDto.getHospitalDto().getPincode());
			}
			intimation.setCpuCode(CPUCode);
			
			//added for CPU code change in create intimation
			if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
				CPUCode = premiaPullService.getCpuDetails(ReferenceTable.GMC_CPU_CODE);
				if(newIntimationDto.getPolicy() != null && newIntimationDto.getHospitalDto().getNetworkHospitalTypeId() != null
						&& newIntimationDto.getHospitalDto().getHospitalType() != null && newIntimationDto.getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
					if(newIntimationDto.getPolicy().getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = premiaPullService.getInsuredOfficeNameByDivisionCode(newIntimationDto.getPolicy().getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								MasCpuLimit masCpuLimit = premiaPullService.getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
								if(masCpuLimit != null){
									CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
			   }
				intimation.setCpuCode(CPUCode);
			}
			
			if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
				String baayasCpuCodeByPolicy = premiaPullService.getPaayasCpuCodeByPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
				if(baayasCpuCodeByPolicy != null){
					CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
					if(CPUCode != null){
						intimation.setCpuCode(CPUCode);
					}
				}
				
				String jioCpuCodeByPolicy = premiaPullService.getJioPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
				if(jioCpuCodeByPolicy != null){
					CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
					if(CPUCode != null){
						intimation.setCpuCode(CPUCode);
					}
				}
				
				Long tataPolicy = premiaPullService.getTataPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
				if(tataPolicy != null){
					CPUCode = premiaPullService.getMasCpuCode(tataPolicy);
					if(CPUCode != null){
						intimation.setCpuCode(CPUCode);
					}
				}
				String kotakCpuCodeByPolicy = premiaPullService.getKotakPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
				if(kotakCpuCodeByPolicy != null){
					CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
					if(CPUCode != null){
						intimation.setCpuCode(CPUCode);
					}
				}
			}
			if(newIntimationDto.getPolicySummary().getPolicyNo() != null && newIntimationDto.getPolicySummary().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
				CPUCode =  premiaPullService.getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
				intimation.setCpuCode(CPUCode);
			}
			
			//added for CPU routing
			if(newIntimationDto.getPolicySummary().getPolicyNo() != null  && newIntimationDto.getPolicySummary().getProduct().getKey() != null){
				String CpuCode= premiaPullService.getMasProductCpu(newIntimationDto.getPolicySummary().getProduct().getKey());
				if(CpuCode != null){
					CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(CpuCode));
					intimation.setCpuCode(CPUCode);
				}
			}
			//added for CPU routing
			
			String gpaPolicyDetails = premiaPullService.getGpaPolicyDetails(newIntimationDto.getPolicySummary().getPolicyNo());
			if(gpaPolicyDetails != null){
				TmpCPUCode masCpuCode = premiaPullService.getMasCpuCode(Long.valueOf(gpaPolicyDetails));
				if(masCpuCode != null){
					intimation.setCpuCode(masCpuCode);
					CPUCode = masCpuCode;
				}
			}
			//added for CPU code change in create intimation
			
			if(newIntimationDto.getHospitalDto().getHospitalCode() != null){
				intimation.setHospitalCode(newIntimationDto.getHospitalDto().getHospitalCode());
			}
		}
		if (newIntimationDto.getRelapseofIllness() != null && newIntimationDto.getRelapseofIllness().getValue() != null) {
			intimation.setRelapseofIllness(newIntimationDto.getRelapseofIllness().getValue().toLowerCase().contains("yes") ? "Y" : "N");
		}

		/*List<GalaxyIntimation> list = intimationService.findDuplicateInitmation(intimation);
		if (list != null && list.size() > 0 && StringUtils.containsIgnoreCase(list.get(0).getStatus().getProcessValue(),"Submitted")) {
			view.showFailurePanel(list.get(0));
		} else {*/
			intimationService.submitIntimation(newIntimationDto, intimation, null);
			GalaxyIntimation createdIntimation = intimationService.getGalaxyIntimationByKey(intimation.getKey());
			view.showSuccessPanel(createdIntimation);
	    //}
		// temp line
		//if(true) {
			// After created Intimation save the New born babies if any ..
			/** if (newIntimationDto.getNewBornFlag()) {
				saveNewBornBaby(newIntimationDto, createdIntimation);
			}
			/*System.out.println("invoked BPM service successfully");
			view.showSuccessPanel(createdIntimation);*/
		//}
	}

	public void saveSubmitIntimation(@Observes @CDIEvent(INTIMATION_SAVE_SUBMIT_EVENT) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		Map<String, String> userCredentials = (Map<String, String>) parameters.getSecondaryParameters()[0];
		Long statusKey = (Long) parameters.getSecondaryParameters()[1];
		
		Boolean isInsuredExist = false;

		GalaxyIntimation intimation = new NewIntimationMapper().getInstance().getNewIntimation(newIntimationDto);
		
		if((newIntimationDto.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				||newIntimationDto.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode()))){
			Insured insuredDetailsForGroup = getInsuredDetailsForGroup(newIntimationDto);
			intimation.setPolicy(newIntimationDto.getPolicy());
			intimation.setOfficeCode(newIntimationDto.getPolicy().getHomeOfficeCode());
			intimation.setInsured(insuredDetailsForGroup);
			if(insuredDetailsForGroup != null){
				intimation.setInsuredNumber(insuredDetailsForGroup.getInsuredId().longValue());
			}
		}else{
			intimation.setPolicy(newIntimationDto.getPolicy());
			intimation.setOfficeCode(newIntimationDto.getPolicy().getHomeOfficeCode());
			intimation.setInsured(newIntimationDto.getInsuredPatient());
			if(newIntimationDto.getInsuredPatient() != null){
				intimation.setInsuredNumber(newIntimationDto.getInsuredPatient().getInsuredId());
			}
			
		}
		
		//Added for BANCS
		intimation.setPolicySource(intimation.getPolicy().getPolicySource());
		
		if(intimation.getInsured() != null){ 
			TmpCPUCode CPUCode = null;
			if (newIntimationDto.getHospitalDto() != null) {
				if (newIntimationDto.getHospitalDto().getCpuId() != null) {
					CPUCode = policyService.getTmpCpuCode(newIntimationDto.getHospitalDto().getCpuId());
				}
				if (CPUCode == null) {
					CPUCode = intimationService.getCpuObjectByPincode(newIntimationDto.getHospitalDto().getPincode());
				}
				
				if(newIntimationDto.getPolicy() != null && newIntimationDto.getHospitalDto().getNetworkHospitalTypeId() != null
						&& newIntimationDto.getHospitalDto().getHospitalType() != null && newIntimationDto.getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
					if(newIntimationDto.getPolicy().getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = premiaPullService.getInsuredOfficeNameByDivisionCode(newIntimationDto.getPolicy().getHomeOfficeCode());
						if(branchOffice != null){
							String officeCpuCode = branchOffice.getCpuCode();
							if(officeCpuCode != null) {
								CPUCode = masterService.getMasCpuCode(Long.valueOf(officeCpuCode));
							}
						}
					}
				}
				
				intimation.setCpuCode(CPUCode);
				
				//added for CPU code change in create intimation
				if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
					CPUCode = premiaPullService.getCpuDetails(ReferenceTable.GMC_CPU_CODE);
					if(newIntimationDto.getPolicy() != null && newIntimationDto.getHospitalDto().getNetworkHospitalTypeId() != null
							&& newIntimationDto.getHospitalDto().getHospitalType() != null && newIntimationDto.getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
						if(newIntimationDto.getPolicy().getHomeOfficeCode() != null) {
							OrganaizationUnit branchOffice = premiaPullService.getInsuredOfficeNameByDivisionCode(newIntimationDto.getPolicy().getHomeOfficeCode());
							if(branchOffice != null){
								String officeCpuCode = branchOffice.getCpuCode();
								if(officeCpuCode != null) {
									MasCpuLimit masCpuLimit = premiaPullService.getMasCpuLimit(Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
									if(masCpuLimit != null){
										CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
									}
								}
							}
						}
				   }
					intimation.setCpuCode(CPUCode);
				}
				
				if(newIntimationDto.getPolicySummary().getPolicyNo() != null && ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(newIntimationDto.getPolicySummary().getProduct().getCode())){
					String baayasCpuCodeByPolicy = premiaPullService.getPaayasCpuCodeByPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
					if(baayasCpuCodeByPolicy != null){
						CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
						if(CPUCode != null){
							intimation.setCpuCode(CPUCode);
						}
					}
					
					String jioCpuCodeByPolicy = premiaPullService.getJioPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
					if(jioCpuCodeByPolicy != null){
						CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
						if(CPUCode != null){
							intimation.setCpuCode(CPUCode);
						}
					}
					
					Long tataPolicy = premiaPullService.getTataPolicy(newIntimationDto.getPolicySummary().getPolicyNo());
					if(tataPolicy != null){
						CPUCode = premiaPullService.getMasCpuCode(tataPolicy);
						if(CPUCode != null){
							intimation.setCpuCode(CPUCode);
						}
					}
					String kotakCpuCodeByPolicy = premiaPullService.getKotakPolicyCpuCode(newIntimationDto.getPolicySummary().getPolicyNo());
					if(kotakCpuCodeByPolicy != null){
						CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
						if(CPUCode != null){
							intimation.setCpuCode(CPUCode);
						}
					}
				}
				if(newIntimationDto.getPolicySummary().getPolicyNo() != null && newIntimationDto.getPolicySummary().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
					CPUCode =  premiaPullService.getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
					intimation.setCpuCode(CPUCode);
				}
				
				//added for CPU routing
				if(newIntimationDto.getPolicySummary().getPolicyNo() != null  && newIntimationDto.getPolicySummary().getProduct().getKey() != null){
					String CpuCode= premiaPullService.getMasProductCpu(newIntimationDto.getPolicySummary().getProduct().getKey());
					if(CpuCode != null){
						CPUCode = premiaPullService.getMasCpuCode(Long.valueOf(CpuCode));
						intimation.setCpuCode(CPUCode);
					}
				}
				//added for CPU routing
				
				String gpaPolicyDetails = premiaPullService.getGpaPolicyDetails(newIntimationDto.getPolicySummary().getPolicyNo());
				if(gpaPolicyDetails != null){
					TmpCPUCode masCpuCode = premiaPullService.getMasCpuCode(Long.valueOf(gpaPolicyDetails));
					if(masCpuCode != null){
						intimation.setCpuCode(masCpuCode);
						CPUCode = masCpuCode;
					}
				}
				//added for CPU code change in create intimation
				
				
				
				if(newIntimationDto.getHospitalDto().getHospitalCode() != null){
					intimation.setHospitalCode(newIntimationDto.getHospitalDto().getHospitalCode());
				}
				
			}
			
			intimation.setPolicyYear(intimation.getInsured().getPolicyYear());
			
			if (newIntimationDto.getRelapseofIllness() != null && newIntimationDto.getRelapseofIllness().getValue() != null) {
				intimation.setRelapseofIllness(newIntimationDto.getRelapseofIllness().getValue().toLowerCase().contains("yes") ? "Y" : "N");
			}
			
			/*List<GalaxyIntimation> list = intimationService.findDuplicateInitmation(intimation);
			if (list != null && list.size() > 0 && StringUtils.containsIgnoreCase(list.get(0).getStatus().getProcessValue(),"Submitted")) {
				view.showFailurePanel(list.get(0));
			}else{*/
				intimationService.submitIntimation(newIntimationDto, intimation, statusKey);
				GalaxyIntimation createdIntimation = intimationService.getGalaxyIntimationByKey(intimation.getKey());
			
				System.out.println("Intimation saved successfully");
				view.showSuccessPanel(createdIntimation);
			//}
			
		
		}else{
			view.showFailure("Insured Details is not available");
		}
	}
	
	/*private Claim doAutoRegistrationProcess(Intimation objIntimation)
	{
		return intimationService.doAutoRegistrationProcess(objIntimation, starFaxService);
	}*/

	public void cancelIntimation(
			@Observes @CDIEvent(INTIMATION_CANCEL_EVENT) final ParameterDTO parameters) {
		view.cancelIntimation();
	}

//	private Intimation setMasterValuesToPolicy(
//			NewIntimationDto newIntimationDto, Intimation intimation) {
//		MastersValue gender = masterService.getKeyByValue(newIntimationDto
//				.getInsuredPatient().getInsuredGender());
//		MastersValue relationship = masterService
//				.getRelationsShipKeyByValue(newIntimationDto
//						.getInsuredPatient().getRelationshipwithInsuredId());
//		MastersValue lineofBusiness = masterService
//				.getMaster(newIntimationDto.getPolicy()
//						.getLobId());
//		intimation.getPolicy().setLobId(lineofBusiness.getKey());
//		Product product = masterService
//				.getProductByProductCode(newIntimationDto.getTmpPolicy()
//						.getPolProductCode());
//		MastersValue policyType = masterService.getMaster(newIntimationDto
//				.getTmpPolicy().getPolicyType() != null ? Long
//				.valueOf(newIntimationDto.getTmpPolicy().getPolicyType())
//				: null);
//		MastersValue productType = masterService.getMaster(newIntimationDto
//				.getTmpPolicy().getProductTypeId() != null ? Long
//				.valueOf(newIntimationDto.getTmpPolicy().getProductTypeId())
//				: null);
//		List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService
//				.getInsuredOfficeNameByDivisionCode(newIntimationDto
//						.getTmpPolicy().getPolhDivnCode());
//		intimation.getPolicy().setPolicyNumber(
//				newIntimationDto.getTmpPolicy().getPolNo());
//		intimation.getPolicy().setActiveStatus(true);
//
//		if (intimation.getPolicy().getKey() == null
//				&& newIntimationDto.getPolicy().getKey() != null) {
//			intimation.getPolicy()
//					.setKey(newIntimationDto.getPolicy().getKey());
//		}
//		intimation.getPolicy().setGenderId(gender);
//
//		if(relationship != null){
//			intimation.getPolicy().setRelationshipWithInsuredId(relationship);
//		}
//		intimation.getPolicy().setHomeOfficeName(insuredOfficeNameByDivisionCode.get(0) != null ? insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName() : "");
//		intimation.getPolicy().setProduct(product);
//		intimation.getPolicy().setPolicyType(policyType);
//		intimation.getPolicy().setProductType(productType);
//		intimation.getPolicy().setInsuredFirstName(
//				newIntimationDto.getInsuredPatientId().getInsuredName());
//		intimation.getPolicy().setInsuredMiddleName(
//				newIntimationDto.getInsuredPatientId().getInsuredName());
//		intimation.getPolicy().setInsuredLastName(
//				newIntimationDto.getInsuredPatientId().getInsuredName());
//		intimation.getPolicy().setInsuredSumInsured(
//				newIntimationDto.getInsuredPatientId().getInsuredSumInsured());
//		intimation.getPolicy().setCummulativeBonus(
//				newIntimationDto.getInsuredPatientId().getCummulativeBonus());
//		intimation.getPolicy().setInsuredId(
//				newIntimationDto.getInsuredPatientId().getKey().toString());
//		intimation.getPolicy().setProposerFirstName(
//				newIntimationDto.getTmpPolicy().getPolAssrName());
//		String rmn = newIntimationDto.getTmpPolicy().getRegisterdMobileNumber() != null ? newIntimationDto
//				.getTmpPolicy().getRegisterdMobileNumber() : null;
//		intimation.getPolicy().setRegisteredMobileNumber(rmn);
//		// intimation.getPolicy().setCummulativeBonus(newIntimationDto.getTmpPolicy().getPolCumulativeBonus());
//		// intimation.getPolicy().setCopay(newIntimationDto.getTmpPolicy().getCopay());
//		// intimation.getPolicy().setCorporateBufferFlag(newIntimationDto.getTmpPolicy().getCorporateBufferFlag());
//		// intimation.getPolicy().setSumInsuredII(newIntimationDto.getTmpPolicy().getSumInsuredII());
//
//		return intimation;
//	}

	public void submitUpdateHospital(
			@Observes @CDIEvent(UPDATE_SUBMIT_EVENT) final ParameterDTO parameters) {

		UpdateHospitalDTO updateHospitalDto = (UpdateHospitalDTO) parameters
				.getPrimaryParameter();
		UpdateHospital updateHospital = new UpdateHospitalMapper()
				.getUpdateHospital(updateHospitalDto);
		updateHospitalService.submitUpdateHospital(updateHospitalDto,
				updateHospital);
		Notification.show("Updated Successfully");

	}

	@SuppressWarnings("deprecation")
	public void searchHospitalById(
			@Observes @CDIEvent(SEARCH_HOSPITAL_BY_ID) final ParameterDTO parameters) {
		HospitalSearchTable searchTable = hospitalSearchTable.get();
		HospitalDto searchHospitalDetails = (HospitalDto) parameters
				.getPrimaryParameter();
		if (searchHospitalDetails != null) {
			if (searchHospitalDetails.getHospitalCode() == null
					|| searchHospitalDetails.getHospitalCode() == "") {
				Notification.show("Information",
						"Please Enter Hospital Id to search",
						Notification.TYPE_HUMANIZED_MESSAGE);

			} else {
				List<UnFreezHospitals> searchedDBData = hospitalService
						.getANHHospitalByCode(searchHospitalDetails
								.getHospitalCode());
				if (!searchedDBData.isEmpty() && searchedDBData != null) {
					searchHospitalDTO = new ArrayList<HospitalDto>();
					for (UnFreezHospitals instance : searchedDBData) {
						searchHospitalDTO.add(hospitalMapper.get()
								.getUnFreezHospitalDTO(instance));
					}

					searchTable.setTableList(searchHospitalDTO);
				} else {
					Notification.show("Information", "No Result Found",
							Notification.Type.HUMANIZED_MESSAGE);
					hospitalSearch.resetHospitalName();
					searchTable.resetTable();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void searchHospitalByName(
			@Observes @CDIEvent(SEARCH_HOSPITAL_BY_NAME) final ParameterDTO parameters) {
		HospitalSearchTable searchTable = hospitalSearchTable.get();
		HospitalDto searchHospitalDetails = (HospitalDto) parameters
				.getPrimaryParameter();
		if (searchHospitalDetails != null) {
			if (searchHospitalDetails.getName() == null
					|| searchHospitalDetails.getName() == "") {
				Notification.show("Information",
						"Please Enter Hospital Name to search",
						Notification.TYPE_HUMANIZED_MESSAGE);
			} else {
				List<UnFreezHospitals> searchedDBData = hospitalService
						.getANHHospitalByName(searchHospitalDetails.getName());
				if (!searchedDBData.isEmpty()) {
					searchHospitalDTO = new ArrayList<HospitalDto>();
					for (UnFreezHospitals instance : searchedDBData) {
						HospitalDto hospitalDto = new HospitalDto(instance);
						searchHospitalDTO.add(hospitalDto);
						
					}
					searchTable.setTableList(searchHospitalDTO);
				} else {
					Notification.show("Information", "No Result Found",
							Notification.TYPE_HUMANIZED_MESSAGE);
					hospitalSearch.resetHospitalId();
					searchTable.resetTable();
				}
			}
		}
	}
	
	
	 
	
	
	public void addResonForAdmission(@Observes @CDIEvent(ADD_REASON_FOR_ADMISSION) final ParameterDTO parameters)
	{
		String reason = (String) parameters.getPrimaryParameter();
		SelectValue admissionReasonSelectValue = null;
		if(reason != null  && !StringUtils.equals(StringUtils.trim(reason), "")){
		admissionReasonSelectValue = masterService.addReasonForAdmission(reason);
		}
		view.addedReasonForAdmission(admissionReasonSelectValue);
	}
	
	@SuppressWarnings("deprecation")
	public void searchHospitalByKey(
			@Observes @CDIEvent(SEARCH_HOSPITAL_KEY) final ParameterDTO parameters) {
		HospitalDto searchHospitalDetails = (HospitalDto) parameters
				.getPrimaryParameter();
		if (searchHospitalDetails != null) {
			if (searchHospitalDetails.getKey() == null) {
				Notification
						.show("Information",
								"Unable to get the Hospital information, Please try again",
								Notification.TYPE_HUMANIZED_MESSAGE);
			} else {
				List<UnFreezHospitals> searchedDBData = hospitalService
						.searchUnFreezHospitalByKey(searchHospitalDetails.getKey());

				if (!searchedDBData.isEmpty()) {
					searchHospitalDTO = new ArrayList<HospitalDto>();
					for (UnFreezHospitals instance : searchedDBData) {
						searchHospitalDTO.add(hospitalMapper.get()
								.getUnFreezHospitalDTO(instance));
					}
					if (searchHospitalDTO != null) {
						System.out.println("key =========================="
								+ searchHospitalDTO.get(0).getKey());
						view.setRegisteredHospital(searchedDBData.get(0));
						intimationDetailsPage
								.setHospitalDetails(searchHospitalDTO.get(0));
					}
				} else {
					Notification.show("Information", "No Result Found",
							Notification.TYPE_HUMANIZED_MESSAGE);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void searchHospitalContactNo(
			@Observes @CDIEvent(HOSPITAL_CONTACT_NO_SEARCH) final ParameterDTO parameters) {
		HospitalDto searchHospitalDetails = (HospitalDto) parameters
				.getPrimaryParameter();
		intimationDetailsPage
		.setHospitalDetails(searchHospitalDetails);
	}
	
	public void selectedGmcInsured(
			@Observes @CDIEvent(GMC_INSURED_SELECTED) final ParameterDTO parameters) {
		GmcMainMemberList insured = (GmcMainMemberList) parameters.getPrimaryParameter();
		List<GmcMainMemberList> dependentRisk = intimationService.getGMCInsuredBasedOnMemberId(insured);
		if(insured != null &&  insured.getMemberId() == null && 
				insured.getMemberType() != null && insured.getMemberType().equalsIgnoreCase("PROPOSER")){
			dependentRisk.add(insured);
		}
		view.setSelectedInsured(insured,dependentRisk);
	}
	
	
	public Insured getInsuredDetailsForGroup(NewIntimationDto bean){
		
		PremPolicyDetails policyDetails = null;
		Boolean isIntegratedPolicy = null;
		
		if(bean.getPolicyServiceType().equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
			policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(bean.getPolicySummary()
					.getPolicyNo(), bean.getRiskId());
			isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, bean.getRiskId(),false);
			
		}else if(bean.getPolicyServiceType().equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
			policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(bean.getPolicySummary()
					.getPolicyNo(), bean.getRiskId());
			//policyDetails.setRiskSysId("");
			isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, bean.getRiskId(),false);
		}
		
		if(isIntegratedPolicy){
			
			Policy policy = policyService.getPolicy(policyDetails.getPolicyNo());
			bean.setPolicy(policy);
			
			Insured insured = premiaPullService.getInsuredByPolicyAndInsuredNameForDefault(policyDetails.getPolicyNo(), bean.getRiskId().toString());
			return insured;
		}
		
		return null;
		
	}
	
	
}
