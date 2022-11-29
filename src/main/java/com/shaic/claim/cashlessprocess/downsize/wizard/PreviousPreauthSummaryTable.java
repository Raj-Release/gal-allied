package com.shaic.claim.cashlessprocess.downsize.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPreauthMoreDetailsPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreviousPreauthSummaryTable extends GBaseTable<PreviousPreAuthTableDTO>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private ViewPreauthMoreDetailsPage viewPreviousPreAuthDetails;
	
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private DiagnosisService diagnosisService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private HospitalService hosptialService;
		
	private Window popup;
	
	private Double totalApprovedAmt;
	
	private String diagnosisName;
	
	private PreauthDTO bean;
	
	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	
	
	private Map<String, Object> sublimitCalculatedValues;
	
	public static final Object[] COLUMN_HEADER = new Object[] {
		"serialNumber",
		"referenceNo",
		"referenceType",
		"treatementType",
		"requestedAmt",
		"approvedAmt"
	};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<PreviousPreAuthTableDTO>(PreviousPreAuthTableDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
		table.removeGeneratedColumn("ViewDetails");
		table.setHeight("30%");
		table.addGeneratedColumn("ViewDetails", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("ViewDetails");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						PreviousPreAuthTableDTO preAuth = (PreviousPreAuthTableDTO)itemId;
						Preauth preauth = preauthService.getPreauthById(preAuth.getKey());
						PreauthDTO preauthDto = getPreauthDTO(preauth);
						getResidualAmount(preauth, preauthDto);
						List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues = setValueToMedicalDecisionValues(preauthDto);
						viewPreviousPreAuthDetails.init(preauthDto,preauth,setValueToMedicalDecisionValues);
						popup = new com.vaadin.ui.Window();
						popup.setCaption("View Pre Auth Details");
						popup.setWidth("75%");
						popup.setHeight("100%");
						popup.setContent(viewPreviousPreAuthDetails);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
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
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		table.setPageLength(table.size()+2);
		
	}

	@Override
	public void tableSelectHandler(PreviousPreAuthTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void setTableList(final Collection<PreviousPreAuthTableDTO> list) {
		table.removeAllItems();
		Double total=new Double(0);
		Double reqTotal =  new Double(0);
		int i =1;
		for (final PreviousPreAuthTableDTO bean : list) {
			bean.setSerialNumber(i++);
			table.addItem(bean);
			if(null != bean.getApprovedAmt()){
				total=total+new Double(bean.getApprovedAmt());
			}
			if(null != bean.getRequestedAmt()){
				reqTotal = reqTotal + new Double(bean.getRequestedAmt());
			}
		}
		
		table.setColumnFooter("approvedAmt", total.toString());
		table.setColumnFooter("requestedAmt", reqTotal.toString());
		table.setColumnFooter("treatementType", "Total Amount");
		totalApprovedAmt=total;
		table.setFooterVisible(true);
		table.sort();
	}
	public Double getTotalApprovedAmt(){
		return totalApprovedAmt;
	}

	@Override
	public String textBundlePrefixString() {
		return "previous-pre-auth-details-";
	}
	
	public PreauthDTO getPreauthDTO(Preauth preauth){
		
		PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//		PreMedicalMapper.getAllMapValues();
		
		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
	    setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(), preauth,
				preauthDTO, false);
	 

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO
						.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			copayValue = dbCalculationService.getProductCoPay(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);
		
		return preauthDTO;
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
		}
		/*String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

//		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
//				.getPreviousClaims(claimsByPolicyNumber,                                   used from shautils
//						claimByKey.getClaimId(), pedValidationService,
//						masterService);
		
		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());*/
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);
		
		//used from preauthservice

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if (previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = premedicalMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(previousPreauth
									.getClaim().getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		List<SpecialityDTO> specialityDTOList = premedicalMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(previousPreauth.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<ProcedureDTO> procedureMainDTOList = premedicalMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			if (procedureDTO.getSublimitName() != null) {
				ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
						.getSublimitName().getLimitId());
				procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
				procedureDTO.setSublimitDesc(limit.getLimitDescription());
				procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
						.toString());
			}
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());

		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			copayValue = dbCalculationService.getProductCoPay(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(),preauthDTO);
		// Fix for issue 732 ends

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
			}

			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());

			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);
		}
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
				.findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
		Float amountConsider = 0.0f;
		if (findClaimAmountDetailsByPreauthKey != null) {
			for (ClaimAmountDetails claimAmountDetails : findClaimAmountDetailsByPreauthKey) {
                      amountConsider += claimAmountDetails.getPaybleAmount();
			}
		}
		preauthDTO.setAmountConsidered(String.valueOf(amountConsider.intValue()));
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setClaimedDetailsList(
						premedicalMapper
								.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));
		Integer sumInsured = preauthService.getSumInsured(preauthDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
				.getPolicy().getProduct().getAutoRestorationFlag();

		if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
			if (sumInsured != null && sumInsured.intValue() > 0) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(
								SHAConstants.AUTO_RESTORATION_NOTDONE);
			} else if (null != sumInsured && 0 == sumInsured.intValue()) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
			}
		} else {
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(
					SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}

	}
	
	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hosptialService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}
	
	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}
	
	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge,PreauthDTO preauthDTO) {
		
		Preauth preauth = null;
		
		if(preauthDTO.getKey() != null){
			List<Preauth> preauthList = preauthService.getPreauthByKey(preauthDTO.getKey());
			if(preauthList != null && ! preauthList.isEmpty()){
				preauth = preauthList.get(0);
				}
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new WeakHashMap<Long, SublimitFunObject>();
		
		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
		
		if(preauth != null && preauth.getSectionCategory() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauth.getClaim().getIntimation().getPolicy().getProduct().getKey())){
				sublimitList = dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(preauth.getClaim().getIntimation().getPolicy().getKey(), insuredSumInsured,
								insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", preauthDTO.getClaimDTO().getClaimSubCoverCode());
			}else{
				sublimitList = dbCalculationService
						.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
								insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", preauthDTO.getClaimDTO().getClaimSubCoverCode(),preauth.getClaim().getIntimation().getInsured().getInsuredId());
			}
			
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauth.getClaim().getIntimation().getPolicy().getProduct().getKey())){
				
				sublimitList = dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(productKey, insuredSumInsured,
								insuredAge,0l, "0", preauthDTO.getClaimDTO().getClaimSubCoverCode());
			}else{
				sublimitList = dbCalculationService
						.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
								insuredAge,0l, "0", preauthDTO.getClaimDTO().getClaimSubCoverCode(),preauth.getClaim().getIntimation().getInsured().getInsuredId());
			}
			
		}
		
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;

	}
	
	
	public List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues(PreauthDTO preauthDto){
		Double sublimitPackageAmount = 0d;
		List<DiagnosisProcedureTableDTO> diagnosisProcedureValues = new ArrayList<DiagnosisProcedureTableDTO>();
		this.bean = preauthDto;
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				
				if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)){
					
					dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
					dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
					dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
				}
				
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								if(pedDetailsTableDTO
										.getExclusionDetails()!=null && pedDetailsTableDTO
										.getExclusionDetails().getId()!=null){
									exclusionValues(pedDetailsTableDTO
											.getExclusionDetails().getId());
								}
								if(this.exlusionContainer!=null&& this.exlusionContainer.getItemIds()!=null){
									pedDetailsTableDTO
									.setExclusionAllDetails(this.exlusionContainer
											.getItemIds());
								}
								if (null != pedDetailsTableDTO.getExclusionDetails()) {
								exclusionValues(pedDetailsTableDTO
										.getExclusionDetails().getId());

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								if(exclusionAllDetails!=null && !exclusionAllDetails.isEmpty()){
									for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
										if (null != pedDetailsTableDTO
												.getExclusionDetails()
												&& exclusionDetails
														.getKey()
														.equals(pedDetailsTableDTO
																.getExclusionDetails()
																.getId())) {
											paymentFlag = exclusionDetails
													.getPaymentFlag();
										}
									}
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
						 }		
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);

				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				dto.setRestrictionSI("NA");

				dto.setPackageAmt("NA");

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				medicalDecisionDTOList.add(dto);
			}

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				int diagCount = 0;
				int proCount = 0;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					medicalDecisionDto.setIsEnabled(false);
					// if(medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Diag "+(diag++));
					// caluculationInputValues.put("restrictedSI",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction().getValue()
					// : null);
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey() ==
					// null ? 0l :
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey());
					// caluculationInputValues.put("diagnosisId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId().toString());
					// caluculationInputValues.put("referenceFlag", "D" );
					// } else if(medicalDecisionDto.getProcedureDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Proc " +
					// (proc++));
					// caluculationInputValues.put("restrictedSI", null );
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getProcedureDTO().getSublimitName() !=
					// null ?
					// medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getProcedureDTO().getKey() == null ?
					// 0l : medicalDecisionDto.getProcedureDTO().getKey());
					// caluculationInputValues.put("referenceFlag", "P" );
					// }
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? "0"
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId()
												.toString());
						caluculationInputValues.put("referenceFlag", "D");
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}

					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										medicalDecisionDto.getProcedureDTO()
												.getProcedureName() == null ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName()
														.getId() == null ? 0l
														: medicalDecisionDto
																.getProcedureDTO()
																.getProcedureName()
																.getId());
						caluculationInputValues.put("referenceFlag", "P");
						if (null != this.bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()
								&& !this.bean
										.getPreauthMedicalProcessingDetails()
										.getProcedureExclusionCheckTableList()
										.isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthMedicalProcessingDetails()
									.getProcedureExclusionCheckTableList()
									.get(proCount).getAmountConsideredAmount();
							medicalDecisionDto
									.getProcedureDTO()
									.setAmountConsideredAmount(amountConsidered);
							medicalDecisionDto
									.setAmountConsidered(amountConsidered != null ? amountConsidered
											.intValue() : 0);
							
							Double approvedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
							}
//							Double minimumAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getMinimumAmount();
//							medicalDecisionDto.getProcedureDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//								sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							
							proCount++;
						}
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}
					}
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimDTO().getKey());
					
					sumInsuredCalculation(caluculationInputValues);
					Map<String, Object> values = this.sublimitCalculatedValues;

					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					if (values.get("restrictedAvailAmt") != null) {
						medicalDecisionDto.setAvailableAmout(((Double) values
								.get("restrictedAvailAmt")).intValue());
					}
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
						if (null != this.bean.getPreauthDataExtractionDetails()
								.getDiagnosisTableList()
								&& !this.bean.getPreauthDataExtractionDetails()
										.getDiagnosisTableList().isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthDataExtractionDetails()
									.getDiagnosisTableList().get(diagCount)
									.getAmountConsideredAmount();
							medicalDecisionDto
									.getDiagnosisDetailsDTO()
									.setAmountConsideredAmount(amountConsidered);
							if(amountConsidered != null){
							medicalDecisionDto
									.setAmountConsidered(amountConsidered
											.intValue());
							}
							
							Double approvedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
								
							}
							
//							Double minimumAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getMinimumAmount();
//							medicalDecisionDto.getDiagnosisDetailsDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//							sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							diagCount++;
						}
					}

					// need to implement in new medical listener table
					medicalDecisionDto.setIsEnabled(false);
//					this.diagnosisProcedureValues
//							.addBeanToList(medicalDecisionDto);
					diagnosisProcedureValues.add(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				if (null != this.bean.getResidualAmountDTO()) {
					if (null != this.bean.getResidualAmountDTO()
							.getAmountConsideredAmount()) {
						dto.setAmountConsidered(this.bean
								.getResidualAmountDTO()
								.getAmountConsideredAmount().intValue());
						dto.setMinimumAmount(this.bean.getResidualAmountDTO().getMinimumAmount().intValue());
						if(this.bean.getResidualAmountDTO().getMinimumAmount() != null){
						sublimitPackageAmount += this.bean.getResidualAmountDTO().getMinimumAmount();
						}
					}
				}
				dto.setIsEnabled(false);
//				this.diagnosisListenerTableObj.addBeanToList(dto);
				diagnosisProcedureValues.add(dto);
			}
		} else {
//			this.diagnosisListenerTableObj.addList(filledDTO);
		}
		
		this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(sublimitPackageAmount);
		
		 
		 return diagnosisProcedureValues;
		
	}
	
	private void getResidualAmount(Preauth previousPreauth,
			PreauthDTO preauthDTO) {
		ResidualAmount residualAmtByPreauthKey = preauthService
				.getLatestResidualAmtByPreauthKey(previousPreauth.getKey());
		if (null != residualAmtByPreauthKey) {
			ResidualAmountDTO residualDTO = new ResidualAmountDTO();
			residualDTO.setKey(residualAmtByPreauthKey.getKey());
			residualDTO.setPreauthKey(previousPreauth.getKey());
			residualDTO.setAmountConsideredAmount(residualAmtByPreauthKey
					.getAmountConsideredAmount());
			residualDTO.setMinimumAmount(residualAmtByPreauthKey.getMinimumAmount());
			residualDTO.setApprovedAmount(residualAmtByPreauthKey
					.getApprovedAmount());
			residualDTO.setRemarks(residualAmtByPreauthKey.getRemarks());
			preauthDTO.setResidualAmountDTO(residualDTO);
		}

	}
	
	public void exclusionValues(Long impactDiagnosisKey){
		
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		this.exlusionContainer=icdCodeContainer;
		
		
	}
	
	public void sumInsuredCalculation(Map<String, Object> values){
		
			String diagnosis = null;
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
			this.diagnosisName = diagnosis;
			this.sublimitCalculatedValues = medicalDecisionTableValue;
			
	}

}
