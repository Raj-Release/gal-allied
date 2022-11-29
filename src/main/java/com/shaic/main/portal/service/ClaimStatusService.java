package com.shaic.main.portal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import net.minidev.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.VB64ComplianceDto;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.claimhistory.view.PedHistoryDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryService;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsDTO;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationService;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTableDTO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDetailDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsuredDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveBariatricSurgeryTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveDeliveryNewBornTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHealthCheckTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHospitalCashTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHospitalisationTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveOutpatientTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.LumpSumTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.PABalanceSumInsuredTableDTO;
import com.shaic.claim.registration.previousinsurance.view.PreviousInsuranceInsuredDetailsTableDTO;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewBabyIntimation;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PEDEndormentTrails;
import com.shaic.domain.preauth.PedEndorementHistory;
import com.shaic.domain.preauth.PedEndorsementDetailsHistory;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.InsuredMapper;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Layout;

@Stateless
public class ClaimStatusService {
	
	private final Logger logger = LoggerFactory.getLogger(ClaimStatusService.class);
	
	@Inject
	private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private DBCalculationService dbCalculationService;	
	  
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private FieldVisitRequestService fieldVisitRequestService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PolicyService policyservice;
	
	@EJB
	private DiagnosisService diagnosisService;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private ViewPEDEndoresementDetailsService pedEndorsementService;
	
	@EJB
	private ViewClaimHistoryService viewClaimHistoryService;
	
	@Inject
	private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private ViewPedValidationService viewPedValidationService;
	
	private PreauthDTO bean;
	
	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	private String diagnosisName;
	
	private Map<String, Object> sublimitCalculatedValues;
	
    public ViewQueryDTO getQueryDetails(RODQueryDetailsDTO viewQueryDto){
    	logger.debug("Enter ClaimStatusService.getQueryDetails() Method");
		ReimbursementQuery reimbursementQuery = acknowledgementDocumentsReceivedService.getReimbursementQuery(viewQueryDto.getReimbursementQueryKey());
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewQueryDTO viewDetails = instance.getviewDetailsQuery(reimbursementQuery);
		
		if (reimbursementQuery != null) {
			String billClassification = getBillClassification(reimbursementQuery.getReimbursement().getDocAcknowLedgement());
			viewDetails.setBillClassification(billClassification);
			viewDetails.setQueryStatus(reimbursementQuery.getStatus().getProcessValue());
		}
		
		viewDetails.setDiagnosis(viewQueryDto.getDiagnosis());
		viewDetails.setQueryRaiseRole(viewQueryDto.getQueryRaisedRole());
		viewDetails.setQueryRaised(viewQueryDto.getQueryRaisedRole());
		
		if (viewDetails.getQueryRaisedDate()!= null) {
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryRaisedDate().toString());
			viewDetails.setQueryRaisedDateStr(SHAUtils.formatDate(tempDate));
		}
		
		if (viewDetails.getAdmissionDate() != null) {
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getAdmissionDate());
			viewDetails.setAdmissionDate(SHAUtils.formatDate(tempDate));
		}
		
		if (viewDetails.getQueryDraftedDate()!= null) {
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryDraftedDate());
			viewDetails.setQueryDraftedDate(SHAUtils.formatDate(tempDate));
		}
		
		if (viewDetails.getApprovedRejectedDate()!= null) {
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getApprovedRejectedDate());
			viewDetails.setApprovedRejectedDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getIntimationNo() != null){
			Long hospitalId = reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital();
			Hospitals hospitals = hospitalService.getHospitalById(hospitalId);
			if (hospitals != null) {
				viewDetails.setHospitalName(hospitals.getName());
				viewDetails.setHospitalCity(hospitals.getCity());
				viewDetails.setHospitalType(hospitals.getHospitalTypeName());
			}
		}
		
		return viewDetails;
	}
	
	public String getBillClassification(DocAcknowledgement docAcknowledgement) {
		logger.debug("Enter ClaimStatusService.getBillClassification() Method");
		String classification = StringUtils.EMPTY;
    	if (docAcknowledgement.getPreHospitalisationFlag() != null) {
    		if( docAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
    			if (classification.equals(StringUtils.EMPTY)) {
    				classification ="Pre-Hospitalisation";
    			} else {
    				classification = classification + "," + "Pre-Hospitalisation";
    			}
    		}
    	}
    	if (docAcknowledgement.getHospitalisationFlag() != null) {
    		if (docAcknowledgement.getHospitalisationFlag().equals("Y")) {
    			if (classification.equals(StringUtils.EMPTY)){
    				classification = "Hospitalisation";
    			} else {
    				classification = classification + "," + "Hospitalisation";
    			}
    		}
    	}
		if (docAcknowledgement.getPostHospitalisationFlag() != null) {
			if (docAcknowledgement.getPostHospitalisationFlag().equals("Y")) {
				if (classification.equals(StringUtils.EMPTY)) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + "," + " Post-Hospitalisation";
				}
			}
		}
		
		if (docAcknowledgement.getHospitalCashFlag() != null) {
			if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {
				if (classification.equals(StringUtils.EMPTY)) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + "," + "Add on Benefits (Hospital cash)";
				}
			}
		}
	         
	    if (docAcknowledgement.getLumpsumAmountFlag() != null) {
	    	if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {
	    		if (classification.equals(StringUtils.EMPTY)) {
	    			classification = "Lumpsum Amount";
	    		} else {
	    			classification = classification + "," + "Lumpsum Amount";
	    		}
	    	}
	    }
	         
	    if (docAcknowledgement.getPartialHospitalisationFlag() != null) {
	    	if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {
	    		if (classification.equals("")) {
	    			classification = "Partial Hospitalisation";
	    		} else {
	    			classification = classification + "," + "Partial Hospitalisation";
	    		}
	    	}
	    }
	         
	    if (docAcknowledgement.getOtherBenefitsFlag() != null) {
	    	if (docAcknowledgement.getOtherBenefitsFlag().equals("Y")) {
	    		if (classification.equals("")) {
	    			classification = "Other Benefit";
	    		} else {
	    			classification =classification+ "," + " Other Benefit";
	    		}
	    	}
	    }
	    
	    return classification;
	}
	
	// Method to get document file path
	public String getDocumentFileURL(String fileName, String token) {
		logger.debug("Enter ClaimStatusService.getDocumentFileURL() Method");
		String filePath = null;
		if (fileName != null) {
			filePath = SHAFileUtils.viewFileByToken(token);
			if(filePath == null) {
				if (fileName.endsWith(".JPG") || fileName.endsWith(".jpg") ||fileName.endsWith(".jpeg")) {
					filePath = System.getProperty("jboss.server.data.dir") + "/" + "SampleBill.JPG";
				} else if (fileName.endsWith(".xlsx")) {
					filePath = System.getProperty("jboss.server.data.dir") + "/" + "Amount considered and Decision Table.xlsx";
				} else {
					filePath = System.getProperty("jboss.server.data.dir") + "/" + "BILL.PDF";
				}
			}
		}
		logger.debug("Document file path Url:" + filePath);
		return filePath; 
	}
	
	public String get64VBComplainceFilePath(String intimationNo) {
		logger.debug("Enter ClaimStatusService.get64VBComplainceFilePath() Method");
		String filePath = null;
		if (intimationNo != null) {
			VB64ComplianceDto vb64dto = new VB64ComplianceDto();
			Double reportId = Math.random() * 10000;
			vb64dto.setReportId("PGIR" + reportId.intValue());
			
			ClaimDto claimDto = new ClaimDto();
					
			OMPIntimation ompIntimation = ompIntimationService.searchbyIntimationNo(intimationNo);
			if (ompIntimation != null) {
				OMPClaim ompClaim = ompClaimService.getClaimsByIntimationNumber(intimationNo);
				OMPIntimation intimation = ompIntimationService.searchbyIntimationNo(intimationNo);
				if (ompClaim != null) {
					claimDto = OMPClaimMapper.getInstance().getClaimDto(ompClaim);
					if (ompClaim.getIntimation()!= null && ompClaim.getIntimation().getPolicy()!= null && ompClaim.getIntimation().getPolicy().getPolicyNumber()!= null){
						List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(ompClaim.getIntimation().getPolicy().getPolicyNumber());
						vb64dto.setEndorsement(endorsementList);
					}
				} 
				NewIntimationDto intimationDto = ompIntimationService.getIntimationDto(intimation);
				claimDto.setNewIntimationDto(intimationDto);
			} else {
				Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
				Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
				if (claim != null) {
					claimDto = ClaimMapper.getInstance().getClaimDto(claim);
					if (claim != null && claim.getIntimation()!= null && claim.getIntimation().getPolicy()!= null && claim.getIntimation().getPolicy().getPolicyNumber()!= null){
						List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(claim.getIntimation().getPolicy().getPolicyNumber());
						vb64dto.setEndorsement(endorsementList);
					}
				} 
				NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
				claimDto.setNewIntimationDto(intimationDto);
			}
			
			vb64dto.setClaimDto(claimDto);
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			
			if (strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatusForView(claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId());
				if (get64vbStatus != null && get64vbStatus.equalsIgnoreCase("p")) {
					vb64dto.setPaymentStatus(SHAConstants.PENDING);
				} else if (get64vbStatus != null && get64vbStatus.equalsIgnoreCase("r")) {
					vb64dto.setPaymentStatus(SHAConstants.REALISED);
				} else if (get64vbStatus != null && get64vbStatus.equalsIgnoreCase("d")) {
					vb64dto.setPaymentStatus(SHAConstants.DISHONOURED);
				}
			}
			
			DocumentGenerator docgen = new DocumentGenerator();
			ReportDto rptDto = new ReportDto();
			
			List<VB64ComplianceDto> vb64ListDto = new ArrayList<VB64ComplianceDto>();
			vb64ListDto.add(vb64dto);		
			rptDto.setBeanList(vb64ListDto);
			rptDto.setClaimId(claimDto.getClaimId());
			filePath = docgen.generatePdfDocument("Compliance64VB", rptDto);
		}
		logger.debug("64VBComplaince file path Url:" + filePath);
		return filePath;
	}

	public List<PreviousInsuranceInsuredDetailsTableDTO> getPreviousInsuredDetails(PreviousPolicy previousPolicy) {
		logger.debug("Enter ClaimStatusService.getPreviousInsuredDetails() Method");
		List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuranceInsuredDetailsList = null;
		if (previousPolicy != null) {
			Policy policy = policyService.getPolicyByRenewalPolicyNumber(previousPolicy.getPolicyNumber());
			if(policy != null) {
				List<Insured> insuredList = insuredService.getInsuredListByPolicyNo(policy.getKey().toString());
				if (insuredList != null) {
					previousInsuranceInsuredDetailsList = new ArrayList<PreviousInsuranceInsuredDetailsTableDTO>();
					PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO = null;
					for (Insured insured : insuredList) {
						previousInsuranceInsuredDetailsTableDTO = new PreviousInsuranceInsuredDetailsTableDTO();
						previousInsuranceInsuredDetailsTableDTO.setInsuredName(insured.getInsuredName());
						previousInsuranceInsuredDetailsTableDTO.setAge(insured.getInsuredAge());
						previousInsuranceInsuredDetailsTableDTO.setDOB(insured.getInsuredDateOfBirth());
						if (insured.getRelationshipwithInsuredId() != null) {
							previousInsuranceInsuredDetailsTableDTO.setRelation(insured.getRelationshipwithInsuredId().getValue());
						}
						previousInsuranceInsuredDetailsTableDTO.setSumInsured(insured.getInsuredSumInsured());
						if (insured.getInsuredGender() != null) {
							previousInsuranceInsuredDetailsTableDTO.setSex(insured.getInsuredGender().getValue());
						}
						List<InsuredPedDetails> insuredPedDetails = insuredService.getInsuredKeyListByInsuredkey(insured.getInsuredId());
						if (insuredPedDetails != null) {
							StringBuffer description = new StringBuffer();
							for (InsuredPedDetails insuredPedDetail : insuredPedDetails) {
								description.append((String)insuredPedDetail.getPedDescription()).append(" ,");
							}
							previousInsuranceInsuredDetailsTableDTO.setPedDescription(description.toString());
						}
						previousInsuranceInsuredDetailsList.add(previousInsuranceInsuredDetailsTableDTO);
					}
				}
			}
		}
		return previousInsuranceInsuredDetailsList;
	}
	
	public JSONObject getPASumInsuredDetails(Intimation intimation) {
		logger.debug("Enter ClaimStatusService.getPASumInsuredDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null) {
			jsonObject = new JSONObject();	
			jsonObject.put("isPASumInsured", Boolean.TRUE);	
			DBCalculationService dbCalculationService = new DBCalculationService();
			Claim claim = claimService.getClaimforIntimation(intimation.getKey());
			
			InsuredMapper insuredMapper = new InsuredMapper();
			InsuredDto insuredDto = insuredMapper.getInsuredDto(intimation.getInsured());
			Map<String, List> list = new HashMap<String, List>();
			
			if (insuredDto != null) {
				jsonObject.put("insuredName" , insuredDto.getInsuredName() != null ? insuredDto.getInsuredName() : "" );
				jsonObject.put("insuredAge" , insuredDto.getInsuredAge() != null ? String.valueOf(insuredDto.getInsuredAge().intValue()) : "");		
			}
					
			if (intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && null != intimation.getPolicy().getProduct().getKey() && 
					(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
				if (claim != null) {							
					jsonObject.put("insuredName" , claim.getGpaRiskName() != null ? claim.getGpaRiskName(): "");
					jsonObject.put("insuredAge" , claim.getGpaRiskAge() != null ? String.valueOf(claim.getGpaRiskAge().intValue()) : "");
				} else {
					jsonObject.put("insuredName" , intimation.getPaPatientName() != null ? intimation.getPaPatientName(): "");
					jsonObject.put("insuredAge" , null != insuredDto && insuredDto.getInsuredAge() != null ? String.valueOf(insuredDto
							.getInsuredAge().intValue()) : "");
				}
			}

			if (claim != null) {
				if (intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && null != intimation.getPolicy().getProduct().getKey() && 
						!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
					list = dbCalculationService.getPABalanceSIView(claim.getKey(),insuredDto.getKey());
				} else {
					Long namedKey = null != intimation.getUnNamedKey() ? intimation.getUnNamedKey() : 0l;
					list = dbCalculationService.getGPABalanceSIView(insuredDto.getKey(), claim.getKey(), namedKey);
				}
			} else {
				if (intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && null != intimation.getPolicy().getProduct().getKey() &&  
						!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
					list = dbCalculationService.getPABalanceSIView(0L,insuredDto.getKey());
				} else {
					Long namedKey = null != intimation.getUnNamedKey() ? intimation.getUnNamedKey() : 0l;
					list = dbCalculationService.getGPABalanceSIView(insuredDto.getKey(),0L,namedKey);
				}
			}			
						
			List<PABalanceSumInsuredTableDTO> balanceSumInsured = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_DESC);
			List<PABalanceSumInsuredTableDTO> balanceSumInsuredBenefits = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_BENEFIT_COVER_DESC);
			List<PABalanceSumInsuredTableDTO> gpaSumInsuredCovers = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_OPTIONAL_DESC);
			List<PABalanceSumInsuredTableDTO> balanceSumInsuredAddOn = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_ADDITIONAL_DESC);
			
			jsonObject.put("balanceSumInsured", balanceSumInsured);
			jsonObject.put("balanceSumInsuredBenefits", balanceSumInsuredBenefits);
			jsonObject.put("gpaSumInsuredCovers", gpaSumInsuredCovers);
			jsonObject.put("balanceSumInsuredAddOn", balanceSumInsuredAddOn);				
		}
		return jsonObject;
	}
	
	public JSONObject getSumInsuredDetails(Intimation intimation, Claim claim) {
		logger.debug("Enter ClaimStatusService.getSumInsuredDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null) {
			jsonObject = new JSONObject();	
			String originalSiValue = null;
			String restoredSiValue = null;
			String rechargedSiValue = null;
			String cummulativeBonus = null;
			String limitCoverage = null;
			boolean isGmc = Boolean.FALSE;
			boolean isClassicGroup = Boolean.FALSE;
			boolean isSuperSurplus = Boolean.FALSE;
			Map<String, Object> list = null;
			List<ComprehensiveHospitalisationTableDTO> optinalCoversList= null;
			Double restoredSumInsured = 0d;
			String claimsPaid = null;
			String claimsOutStanding = null;
			String currentClaimProvision = null;
			String gmcInnerLimitInsured  = null;
			String gmcInnerLimitUtilised = null;
			String gmcInnerLimitAval  = null;
			boolean isGmcSections = Boolean.FALSE;
		
			jsonObject.put("isPASumInsured", Boolean.FALSE);	
			jsonObject.put("policyNo", (intimation.getPolicy() != null ? (intimation.getPolicy().getPolicyNumber() != null ? intimation.getPolicy().getPolicyNumber() : "") : "" ));
			jsonObject.put("intimatioNo", (intimation.getIntimationId() != null ? intimation.getIntimationId() : ""));
			jsonObject.put("insuredName", (intimation.getInsured() != null ? (intimation.getInsured().getInsuredName() != null ?intimation.getInsured().getInsuredName() : "") : "" ));
			jsonObject.put("productCode", (intimation.getPolicy() != null ? (intimation.getPolicy().getProduct() != null ? (intimation.getPolicy().getProduct().getCode() != null ? intimation.getPolicy().getProduct().getCode() :"") : "") : "" ));				
			jsonObject.put("productName", (intimation.getPolicy() != null ? (intimation.getPolicy().getProduct() != null ? (intimation.getPolicy().getProduct().getValue() != null ? intimation.getPolicy().getProduct().getValue() :"") : "") : "" ));
						
			if (intimation.getPolicy() != null) {
					
				PolicyMapper mapper = new PolicyMapper();
				PolicyDto policyDto = mapper.getPolicyDto(intimation.getPolicy());
				Double originalSi = (double) 0;
				if (intimation.getPolicy().getProduct() != null 
					&& ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()))	{
					originalSi = dbCalculationService.getInsuredSumInsuredForGMC(policyDto.getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode());
				} else {
					InsuredMapper insuredMapper = new InsuredMapper();
					InsuredDto insuredDto = insuredMapper.getInsuredDto(intimation.getInsured());
					originalSi = dbCalculationService.getInsuredSumInsured(insuredDto.getInsuredId().toString(), policyDto.getKey(),intimation.getInsured().getLopFlag());
				}
				if (originalSi != null ) {
					originalSiValue = SHAUtils.getIndianFormattedNumber(originalSi);
				} 		
					
				BalanceSumInsuredDTO balanceSumInsuredDTO = dbCalculationService.getCumulativeBonusRestrictedSIRestoredSi(policyDto.getKey(), intimation.getInsured().getKey(), intimation.getKey());
				if (balanceSumInsuredDTO != null) {
					restoredSiValue =  (balanceSumInsuredDTO.getRestoredSumInsured() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getRestoredSumInsured()) : SHAUtils.getIndianFormattedNumber(0));						
					rechargedSiValue = (balanceSumInsuredDTO.getRechargedSumInsured() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getRechargedSumInsured()) : SHAUtils.getIndianFormattedNumber(0));
					cummulativeBonus = (balanceSumInsuredDTO.getCumulativeBonus() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getCumulativeBonus()) : SHAUtils.getIndianFormattedNumber(0));						
					limitCoverage =	 (balanceSumInsuredDTO.getLimitOfCoverage() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getLimitOfCoverage()) : SHAUtils.getIndianFormattedNumber(0));
					restoredSumInsured = balanceSumInsuredDTO.getRestoredSumInsured() != null? balanceSumInsuredDTO.getRestoredSumInsured() : 0;
					claimsPaid = (balanceSumInsuredDTO.getPaidAmout() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getPaidAmout()) : SHAUtils.getIndianFormattedNumber(0));
					claimsOutStanding = (balanceSumInsuredDTO.getOutstandingAmout() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getOutstandingAmout()) : SHAUtils.getIndianFormattedNumber(0));
					currentClaimProvision = (balanceSumInsuredDTO.getProvisionAmout() != null ? SHAUtils.getIndianFormattedNumber(balanceSumInsuredDTO.getProvisionAmout()) : SHAUtils.getIndianFormattedNumber(0));
				}
				
				if (intimation.getPolicy().getProduct() != null &&  ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
					isGmc = Boolean.TRUE;					
				}
				
				if(intimation.getPolicy().getProduct() != null && intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
					isClassicGroup = Boolean.TRUE;
				}
				
				if(intimation.getPolicy().getProduct() != null && ReferenceTable.getSuperSurplusKeys().containsKey(intimation.getPolicy().getProduct().getKey())
						&& intimation.getPolicy().getPolicyPlan() != null && intimation.getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
					isSuperSurplus = true;
				}else{
					isSuperSurplus = false;
				}
				
				if(intimation.getPolicy().getProduct() != null && intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
					
					isClassicGroup = Boolean.TRUE;
				}
				
				String sectionCode = intimation.getPolicy().getSectionCode();
				
				if(isGmc  && sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.GMC_SECTION_D)){
					isGmcSections = Boolean.TRUE;
					jsonObject.put("gmcMainMember", (intimation.getInsured() != null ? (intimation.getInsured().getInsuredName() != null ?intimation.getInsured().getInsuredName() : "") : "" ));
					jsonObject.put("gmcInnerLimitAppl", (intimation.getInsured() != null ? (intimation.getInsured().getInnerLimit() != null ? (intimation.getInsured().getInnerLimit() >0  ? SHAConstants.YES : SHAConstants.No) : SHAConstants.No) :  SHAConstants.No));
					if (intimation.getInsured() != null) {
						Map<String, Integer> values = dbCalculationService.getGmcInnerLimit(intimation.getInsured().getKey(), (claim != null ? claim.getKey() :  0));
						if (values != null && !values.isEmpty()) {
							gmcInnerLimitInsured = values.get(SHAConstants.GMC_INNER_LIMIT_SI) != null ? SHAUtils.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_SI)) : SHAUtils.getIndianFormattedNumber(0);
							gmcInnerLimitAval = values.get(SHAConstants.GMC_INNER_LIMIT_AVAILABLE) != null ? SHAUtils.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_AVAILABLE)) : SHAUtils.getIndianFormattedNumber(0);
							gmcInnerLimitUtilised = values.get(SHAConstants.GMC_INNER_LIMIT_UTILISED_AMT) != null ? SHAUtils.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_UTILISED_AMT)) : SHAUtils.getIndianFormattedNumber(0);
						}				
					}
					jsonObject.put("gmcInnerLimitInsured", gmcInnerLimitInsured);
					jsonObject.put("gmcInnerLimitAval", gmcInnerLimitAval);
					jsonObject.put("gmcInnerLimitUtilised", gmcInnerLimitUtilised);
				}				
				
				String gmcSectionName = null;
				if(intimation.getPolicy().getSectionCode() != null && intimation.getPolicy().getSectionDescription() != null){
					gmcSectionName = intimation.getPolicy().getSectionCode() + " - " + intimation.getPolicy().getSectionDescription();	
				}
				jsonObject.put("gmcPolicySecCode", gmcSectionName);
			}
			
			
			jsonObject.put("originalSI", originalSiValue != null ? originalSiValue : "");
			jsonObject.put("restoredSI", restoredSiValue != null ? restoredSiValue : "");
			jsonObject.put("rechargedSI", rechargedSiValue != null ? rechargedSiValue : "");
			jsonObject.put("cummulativeBonus", cummulativeBonus != null ? cummulativeBonus : "");
			jsonObject.put("limitcoverage", limitCoverage != null ? limitCoverage : "");	
			jsonObject.put("isGmc", isGmc);	
			jsonObject.put("isGmcSections", isGmcSections);
			jsonObject.put("isSuperSurplus", isSuperSurplus);	
			jsonObject.put("restoredSumInsured", restoredSumInsured);
			jsonObject.put("claimsPaid", claimsPaid);
			jsonObject.put("claimsOutStanding", claimsOutStanding);
			jsonObject.put("currentClaimProvision", currentClaimProvision);
						
			if (isGmc) {
				list = dbCalculationService.getGmcSI(intimation.getInsured().getKey(), intimation.getIntimationId(), 
					dbCalculationService.getInsuredSumInsuredForGMC(intimation.getPolicy().getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode())
					,intimation.getPolicy().getProduct().getKey());
			} else {
				list = dbCalculationService.getComprehensiveSI(intimation.getInsured().getKey(), intimation.getIntimationId(), dbCalculationService.getInsuredSumInsured(String
					.valueOf(intimation.getInsured().getInsuredId()), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()),intimation.getPolicy().getProduct().getKey());
			}
			
			if (isClassicGroup) {							
				optinalCoversList = dbCalculationService.getComprehensiveSIForClassicGroup(intimation.getInsured().getKey(), intimation.getIntimationId(), dbCalculationService.getInsuredSumInsured(String
						.valueOf(intimation.getInsured().getInsuredId()), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()),intimation.getPolicy().getProduct().getKey());
			}
			
			if (isSuperSurplus) {
				Double definedLimitAmt = 300000d;
				if (intimation.getInsured() != null && intimation.getInsured().getDeductibleAmount() != null) {
					definedLimitAmt = intimation.getInsured().getDeductibleAmount();
				}
				jsonObject.put("definedLimit", definedLimitAmt);	
			}			
			
			jsonObject.put("hasHospTable", Boolean.FALSE);					
			jsonObject.put("hasNewBornTable", Boolean.FALSE);					
			jsonObject.put("hasOutPatientTable", Boolean.FALSE);					
			jsonObject.put("hasHospCashTable", Boolean.FALSE);		
			jsonObject.put("hasHealthCheckTable", Boolean.FALSE);					
			jsonObject.put("hasBariatricTable", Boolean.FALSE);
			jsonObject.put("hasLumpSumTable", Boolean.FALSE);		
			jsonObject.put("hasOptionalTable", Boolean.FALSE);
			
			if (list != null && !list.isEmpty()) {
	
				List<ComprehensiveHospitalisationTableDTO> tableList1 = (List<ComprehensiveHospitalisationTableDTO>)list.get(SHAConstants.SECTION_CODE_1);
				if (tableList1 != null && !tableList1.isEmpty()) {
					jsonObject.put("hasHospTable", Boolean.TRUE);
					jsonObject.put("hospTableDetails", tableList1);
				} 
				
				List<ComprehensiveDeliveryNewBornTableDTO> tableList2 = (List<ComprehensiveDeliveryNewBornTableDTO>)list.get(SHAConstants.SECTION_CODE_2);
				if (tableList2 != null && !tableList2.isEmpty()) {
					jsonObject.put("hasNewBornTable", Boolean.TRUE);
					jsonObject.put("newBornTableDetails", tableList2);
				} 
					
				List<ComprehensiveOutpatientTableDTO> tableList3 = (List<ComprehensiveOutpatientTableDTO>)list.get(SHAConstants.SECTION_CODE_3);
				if (tableList3 != null && !tableList3.isEmpty()) {
					jsonObject.put("HasOutPatientTable", Boolean.TRUE);
					jsonObject.put("outPatientTableDetails", tableList3);
				} 
				
				List<ComprehensiveHospitalCashTableDTO> tableList4 = (List<ComprehensiveHospitalCashTableDTO>)list.get(SHAConstants.SECTION_CODE_4);
				if (tableList4 != null && !tableList4.isEmpty()) {
					jsonObject.put("hasHospCashTable", Boolean.TRUE);
					jsonObject.put("hospCashTableDetails", tableList4);
				} 
					
				List<ComprehensiveHealthCheckTableDTO> tableList5 = (List<ComprehensiveHealthCheckTableDTO>)list.get(SHAConstants.SECTION_CODE_5);
				if (tableList5 != null && !tableList5.isEmpty()) {
					jsonObject.put("hasHealthCheckTable", Boolean.TRUE);
					jsonObject.put("healthCheckTableDetails", tableList5);
				} 
					
				List<ComprehensiveBariatricSurgeryTableDTO> tableList6 = (List<ComprehensiveBariatricSurgeryTableDTO>)list.get(SHAConstants.SECTION_CODE_6);
				if (tableList6 != null && !tableList6.isEmpty()) {
					jsonObject.put("hasBariatricTable", Boolean.TRUE);
					jsonObject.put("bariatricTableDetails", tableList6);
				} 				
					
				List<LumpSumTableDTO> tableList8 = (List<LumpSumTableDTO>)list.get(SHAConstants.SECTION_CODE_8);
				if (tableList6 != null && !tableList6.isEmpty()) {
					jsonObject.put("hasLumpSumTable", Boolean.TRUE);
					jsonObject.put("lumpSumTableDetails", tableList8);
				} 
			}
			
			if (optinalCoversList != null && !optinalCoversList.isEmpty()) {
				jsonObject.put("hasOptionalTable", Boolean.TRUE);
				jsonObject.put("optionalTableDetails", optinalCoversList);				
			}
		}
		return jsonObject;
	}
	
  	public PreauthDetailDTO getPreAuthDetails(Long preauthKey) {
  		logger.debug("Enter ClaimStatusService.getPreAuthDetails() Method");
  		PreauthDetailDTO preAuthDetail = null;
  		Integer totalDiagReverseAllocAmt = 0;
  		Integer totalDiagConsAmt = 0;
  		Integer totalDiagPayableAmt = 0;
  		Integer totalDiagCopayAmt = 0;
  		Integer totalDiagNetAmt = 0;
  		if(preauthKey != null) {
  			Preauth preauth = preauthService.getPreauthById(preauthKey);
  			if(preauth != null) {
  				PreauthDTO preauthDTO = getPreauthDTO(preauth);
  				getResidualAmount(preauth, preauthDTO);

  				Double insuredSumInsured = dbCalculationService
  						.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
  								.getInsuredPatient().getInsuredId().toString(),
  								preauthDTO.getNewIntimationDTO().getPolicy()
  										.getKey(),preauthDTO.getNewIntimationDTO()
  										.getInsuredPatient().getLopFlag());
  				
  				Double balanceSI = 0d;
  				List<Double> copayValue = new ArrayList<Double>();
  				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
  					balanceSI = dbCalculationService.getBalanceSIForGMC(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
  							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
  							preauthDTO.getClaimKey());
  					copayValue = dbCalculationService.getProductCoPayForGMC(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
  				}else{
  					balanceSI = dbCalculationService.getBalanceSI(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
  							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
  							preauthDTO.getClaimKey(), insuredSumInsured,
  							preauthDTO.getNewIntimationDTO().getKey()).get(
  							SHAConstants.TOTAL_BALANCE_SI);
  					 copayValue = dbCalculationService.getProductCoPay(
  							preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
  									.getKey(), preauthDTO.getNewIntimationDTO()
  									.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
  				}
  				preauthDTO.setBalanceSI(balanceSI);
  				preauthDTO.setProductCopay(copayValue);
  				preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
  						preauthDTO.getMedicalRemarks());
  				
  				List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues = setValueToMedicalDecisionValues(preauthDTO);
  				
  				NewClaimedAmountTable claimedAmountDetailsTableObj = claimedAmountDetailsTable.get();
  				claimedAmountDetailsTableObj.initView(preauthDTO,SHAConstants.VIEW_PREAUTH_CLAIM_STATUS);
  				claimedAmountDetailsTableObj.setValues(preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
  				
  				List<NoOfDaysCell> claimedAmtDetailsValues = claimedAmountDetailsTableObj.getValues();
  				preauthDTO.getPreauthDataExtractionDetails().setClaimedDetailsList(claimedAmtDetailsValues);
			 						
  				preAuthDetail = new PreauthDetailDTO(preauth, preauthDTO);
  				preAuthDetail.setDiagnosisTableList(setValueToMedicalDecisionValues);
  				
  				preAuthDetail.setTotalClaimedAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalClaimedAmt());
  				preAuthDetail.setTotalDeductAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalDeductibleAmount());
  				preAuthDetail.setTotalNonPayableAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalDeductableAmount());
  				preAuthDetail.setTotalPayableAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalPayableAmt());
  				preAuthDetail.setTotalNetAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalNetAmount());
  				preAuthDetail.setTotalProductAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalProductAmount());
  		
 				
  				Map<Integer, String> amountConsideredValues = new HashMap<Integer, String>();
  				
  				amountConsideredValues.put(0, "Amount Considered");
  				amountConsideredValues.put(1, "Co-Pay");
  				amountConsideredValues.put(2, "Amount Considered (After Co-Pay)");
  				
  				Map<Integer, String> balanceSumInsuredValues = new HashMap<Integer, String>();
  				balanceSumInsuredValues.put(0, "Balance Sum Insured");
  				balanceSumInsuredValues.put(1, "Co-Pay");
  				balanceSumInsuredValues.put(2, "Balance Sum Insured (After Co-Pay)");
  				
  				AmountConsideredTable amountConsideredTable = amountConsideredTableInstance.get();
  				amountConsideredTable.initView(preauthDTO, true);
  				HorizontalLayout HorizontalLayout = new HorizontalLayout();
  				Layout layout = null;
  				
  				amountConsideredTable.initTable(preauthDTO, HorizontalLayout, HorizontalLayout , layout, true, false);
  				
  				Integer balanceSumInsuredAmt = amountConsideredTable.getBalanceSumInsuredAmt();
  				Integer minimumValue = amountConsideredTable.getMinimumValue();

  				if(preauthDTO.getAmountConsidered() != null){		
  					minimumValue = Integer.valueOf(preauthDTO.getAmountConsidered());		
  				}
  				
  				preAuthDetail.setAmountConsideredValues(amountConsideredValues);
  				preAuthDetail.setMinimumValue(minimumValue);
  				preAuthDetail.setBalanceSumInsuredValues(balanceSumInsuredValues);
  				preAuthDetail.setBalanceSumInsured(balanceSumInsuredAmt);
  				
  				List<ViewPedValidationTableDTO> viewPedValidationTableDTO = viewPedValidationService.search(preauth.getKey());
  				if (viewPedValidationTableDTO != null) {
  					String policyAgeing = null;
  					if(preauthDTO.getNewIntimationDTO() != null){
  						policyAgeing = preauthDTO.getNewIntimationDTO().getPolicyYear();
  					}
  				  				
  					for (ViewPedValidationTableDTO viewPedValidationTableDTO2 : viewPedValidationTableDTO) {
  						viewPedValidationTableDTO2.setPolicyAgeing(policyAgeing);
  					}
  					preAuthDetail.setPedValidationList(viewPedValidationTableDTO);
  				}
  				
  				FieldVisitRequest fieldVisitByPreauthKey = fieldVisitRequestService.getFieldVisitByPreauthKey(preauth.getKey());
  				if(fieldVisitByPreauthKey != null) {
  					preAuthDetail.setAllocationTo((fieldVisitByPreauthKey.getAllocationTo() != null ? fieldVisitByPreauthKey.getAllocationTo().getValue() : "-"));
  					preAuthDetail.setFvrTriggeredPoints(fieldVisitByPreauthKey.getFvrTriggerPoints());
  				}
  				  				
  				if(preauth.getSpecialistConsulted() != null){
  					MastersValue specialistConsulted = masterService.getSpecialistConsulted(preauth.getSpecialistConsulted());
  					preAuthDetail.setSpecialistConsulted(specialistConsulted != null ? specialistConsulted.getValue() : "-");
  				}
  				
 				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : setValueToMedicalDecisionValues) {
 					Integer amountCons = diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() :0 ;
 					totalDiagConsAmt += amountCons != null ? amountCons : 0;
 					Integer minAmt = diagnosisProcedureTableDTO.getMinimumAmount() != null ? diagnosisProcedureTableDTO.getMinimumAmount() : 0;
 					totalDiagPayableAmt += minAmt != null ? minAmt : 0;
 					Integer diagCopay = diagnosisProcedureTableDTO.getCoPayAmount() != null ? diagnosisProcedureTableDTO.getCoPayAmount() : 0;
 					totalDiagCopayAmt += diagCopay != null ? diagCopay : 0;
 					Integer netAmt = diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount() : 0;
 					totalDiagNetAmt += netAmt != null ? netAmt : 0;
 					Integer reverseAllocAmt = diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt() : 0;
 					totalDiagReverseAllocAmt += reverseAllocAmt != null ? reverseAllocAmt :0;
  				}
 				preAuthDetail.setTotalDiagReverseAllocAmt(totalDiagReverseAllocAmt);
 				preAuthDetail.setTotalDiagAllocAmt(totalDiagConsAmt);
 				preAuthDetail.setTotalDiagPayableAmt(totalDiagPayableAmt);
 				preAuthDetail.setTotalDiagCopayAmt(totalDiagCopayAmt);
 				preAuthDetail.setTotalDiagNetAmt(totalDiagNetAmt);
  				
  			}
		 }
  		return preAuthDetail;
  	}
  	
    public PreauthDTO getPreauthDTO(Preauth preauth){
    	logger.debug("Enter ClaimStatusService.getPreauthDTO() Method");		
		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		
		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
	    setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(), preauth, preauthDTO, false);
	 
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO
						.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if (ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		} else {
			balanceSI = dbCalculationService.getBalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
			 copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
							.getKey(), preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);		
		return preauthDTO;
	}
    
 	private void getResidualAmount(Preauth previousPreauth, PreauthDTO preauthDTO) {
 		logger.debug("Enter ClaimStatusService.getResidualAmount() Method");
		ResidualAmount residualAmtByPreauthKey = preauthService
				.getLatestResidualAmtByPreauthKey(previousPreauth.getKey());
		if (null != residualAmtByPreauthKey) {
			ResidualAmountDTO residualDTO = new ResidualAmountDTO();
			residualDTO.setKey(residualAmtByPreauthKey.getKey());
			residualDTO.setPreauthKey(previousPreauth.getKey());
			residualDTO.setAmountConsideredAmount(residualAmtByPreauthKey
					.getAmountConsideredAmount());
			residualDTO.setMinimumAmount(residualAmtByPreauthKey.getMinimumAmount());
			residualDTO.setNetAmount(residualAmtByPreauthKey.getNetAmount());
			residualDTO.setApprovedAmount(residualAmtByPreauthKey
					.getApprovedAmount());
			residualDTO.setRemarks(residualAmtByPreauthKey.getRemarks());
			residualDTO.setNetApprovedAmount(residualAmtByPreauthKey.getNetApprovedAmount());
			if (null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()) {
				if (null != residualAmtByPreauthKey.getCoPayTypeId()) {		
					SelectValue copayTypeValue = new SelectValue();
					copayTypeValue.setValue(residualAmtByPreauthKey.getCoPayTypeId().getValue());
					copayTypeValue.setId(residualAmtByPreauthKey.getCoPayTypeId().getKey());
					residualDTO.setCoPayTypeId(copayTypeValue);
				}
			}
			preauthDTO.setResidualAmountDTO(residualDTO);
		}
	}
 	
 	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
 		logger.debug("Enter ClaimStatusService.setpreauthTOPreauthDTO() Method ");
 		//Changed from view preauthDetail Page setpreauthTOPreauthDTO()  
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

//		List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
//				.getPreviousClaims(claimsByPolicyNumber,
//						claimByKey.getClaimId(), pedValidationService,
//						masterService);
		
		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());  */
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

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
				.getProcedureMainDTOList(preauthService.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			
			if(procedureDTO.getApprovedAmount() != null && procedureDTO.getNetApprovedAmount() != null && !procedureDTO.getApprovedAmount().equals(procedureDTO.getNetApprovedAmount())) {
				preauthDTO.setIsReverseAllocation(true);
			}
			
			if (procedureDTO.getSublimitName() != null) {
				ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
						.getSublimitName().getLimitId());
				procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
				procedureDTO.setSublimitDesc(limit.getLimitDescription());
				procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
						.toString());
			}
			
			procedureDTO.setNetApprovedAmount(procedureDTO.getNetApprovedAmount());
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				insuredSumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
			}else{
				 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
							preauthDTO.getNewIntimationDTO().getInsuredPatient()
									.getInsuredId().toString(), preauthDTO.getPolicyDto()
									.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
		
		}
		else{
			
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}
		

		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
			 copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
							.getKey(), preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),
							preauthDTO.getNewIntimationDTO());
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
			
			pedValidationTableDTO.setNetApprovedAmount(pedValidationTableDTO.getNetApprovedAmount());
			
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
		preauthDTO
				.setAmountConsidered(String.valueOf(amountConsider.intValue()));
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
			
		
		/*TreatingDoctorDetails Populate CR2019211*/
		List<TreatingDoctorDetails> findDoctorDetailsByClaimKey = preauthService.findTreatingDoctorDetailsByTransactionKey(previousPreauth.getKey());
		if(findDoctorDetailsByClaimKey !=null && !findDoctorDetailsByClaimKey.isEmpty()){
			List<TreatingDoctorDTO> TreatingDoctorDTOList = premedicalMapper.gettreatingDoctorDTOList(findDoctorDetailsByClaimKey);
			preauthDTO.getPreauthDataExtractionDetails().setTreatingDoctorDTOs(TreatingDoctorDTOList);
		}
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
		
		/*Negotiation Details */
		if(previousPreauth.getKey() !=null){
			NegotiationAmountDetails negotiationAmountDetails = preauthService.getNegotiationAmountDetailsByTransactionKey(previousPreauth.getKey());
			if(negotiationAmountDetails !=null
					&& negotiationAmountDetails.getNegotiationWith() !=null){
				preauthDTO.getPreauthMedicalDecisionDetails().setNegotiationWith(negotiationAmountDetails.getNegotiationWith());
			}
		}
	}
	
	public List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues(PreauthDTO preauthDto){
		logger.debug("Enter ClaimStatusService.setValueToMedicalDecisionValues() Method");
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
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
								if (pedDetailsTableDTO.getExclusionDetails() != null) {
									exclusionValues(pedDetailsTableDTO
										.getExclusionDetails().getId());

									pedDetailsTableDTO.setExclusionAllDetails(this.exlusionContainer
											.getItemIds());

									List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO.getExclusionAllDetails();
									String paymentFlag = "y";
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
							
							Double netApprovedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							//copay amount
							Double copayAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							}
							
							Double copayPercentage = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}
							Double netAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetAmount();

							if(netAmount != null){
							medicalDecisionDto.setNetAmount(netAmount.intValue());
							}
							
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
							
							Double netApprovedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							
							Double copayAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							
							}
							
							Double copayPercentage = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}
							
							//netamount After Copay 
							Double netAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetAmount();
							if(netAmount != null){
							medicalDecisionDto.setNetAmount(netAmount.intValue());
							}
							diagCount++;
						}
					}

					// need to implement in new medical listener table
					medicalDecisionDto.setIsEnabled(false);				
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
						
						Double netApprovedAmount = this.bean.getResidualAmountDTO().getNetApprovedAmount();
						
						dto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
						if(this.bean.getResidualAmountDTO().getNetAmount() != null){
							dto.setNetAmount(this.bean.getResidualAmountDTO().getNetAmount().intValue());
												
					}
				    ///copay amount calculation///
					if(this.bean.getResidualAmountDTO().getCopayAmount() != null){
						dto.setCoPayAmount(this.bean.getResidualAmountDTO().getCopayAmount().intValue());
						}
						Double copayPercentage = this.bean.getResidualAmountDTO().getCopayPercentage();
					if(copayPercentage != null){
						dto.setCopayPercentageAmount(copayPercentage.doubleValue());
				}
									}
				
				}dto.setIsEnabled(false);
			
				diagnosisProcedureValues.add(dto);
			}
		} 
		else {
//			this.diagnosisListenerTableObj.addList(filledDTO);
		}
		
		this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(sublimitPackageAmount);
		
		 
		 return diagnosisProcedureValues;
		
	}

 	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
 		logger.debug("Enter ClaimStatusService.setClaimValuesToDTO() Method");
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hospitalService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation().getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation().getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}
 	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		logger.debug("Enter ClaimStatusService.getPreviousClaimForPreviousPolicy() Method");
		Policy renewalPolNo = policyservice.getByPolicyNumber(policyNumber);
		List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
		if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
			for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
				if	(!generatedList.contains(viewTmpClaim)) {
					generatedList.add(viewTmpClaim);
				}
			}
		}
		if (renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
			getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
		} else {
			return generatedList;
		}
		return generatedList;
	}
	
	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		logger.debug("Enter ClaimStatusService.getSublimitFunctionObj() Method");
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}
	
	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge,PreauthDTO preauthDTO) {
		logger.debug("Enter ClaimStatusService.getSublimitFunObjMap() Method");
		Preauth preauth = null;
		
		if (preauthDTO.getKey() != null) {
			List<Preauth> preauthList = preauthService.getPreauthByKey(preauthDTO.getKey());
			if(preauthList != null && ! preauthList.isEmpty()){
				preauth = preauthList.get(0);
				}
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
		
		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
		
		if (preauth != null && preauth.getSectionCategory() != null) {
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		} else {
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,0l, "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			
		}
		
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;
	}
	
	public void exclusionValues(Long impactDiagnosisKey){
		logger.debug("Enter ClaimStatusService.exclusionValues() Method");
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		this.exlusionContainer = icdCodeContainer;
	}
	
	public void sumInsuredCalculation(Map<String, Object> values){
		logger.debug("Enter ClaimStatusService.sumInsuredCalculation() Method");
		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
		this.diagnosisName = diagnosis;
		this.sublimitCalculatedValues = medicalDecisionTableValue;
		
   }
	
	// Method to fecth PED History Details
	public List<PedHistoryDTO> getPedHistoryDetails(Long pedEndorsementKey) {
		logger.debug("Enter CRCPortalServlet.getPedHistoryDetails() Method");
		List<PEDEndormentTrails> pedEndorsementTrails = pedQueryService.getPedEndorsementTrails(pedEndorsementKey);
		List<PedHistoryDTO> pedHistoryList = null;
		if(pedEndorsementTrails != null) {
			pedHistoryList = new ArrayList<PedHistoryDTO>();
			for (PEDEndormentTrails pedEndormentTrails : pedEndorsementTrails) {
				PedHistoryDTO tableDto = new PedHistoryDTO();
				tableDto.setStatus(pedEndormentTrails.getStatus());
				tableDto.setStrDateAndTime((pedEndormentTrails.getCreatedDate() != null?pedEndormentTrails.getCreatedDate().toString(): ""));
				tableDto.setRemarks(pedEndormentTrails.getStatusRemarks());
				tableDto.setUserName(pedEndormentTrails.getCreatedBy());
				pedHistoryList.add(tableDto);
			}
		}
		return pedHistoryList;
	}
	
	// Method to fetch PED History Details By pedEndorsementKey
	public OldPedEndorsementDTO setPedEndorsementByHistory(PedEndorementHistory pedEndorsementHistory) {
		logger.debug("Enter ClaimStatusService.setPedEndorsementByHistory() Method");
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		OldPedEndorsementDTO oldPedEndorsementDTO = new OldPedEndorsementDTO();
		oldPedEndorsementDTO.setPedSuggestionName(pedEndorsementHistory.getPedSuggestion().getValue());
		oldPedEndorsementDTO.setPedName(pedEndorsementHistory.getPedName());
		oldPedEndorsementDTO.setRemarks(pedEndorsementHistory.getRemarks());
		oldPedEndorsementDTO.setRepudiationLetterDate(pedEndorsementHistory.getRepudiationLetterDate());
		if (pedEndorsementHistory.getModifiedBy() != null) {
			oldPedEndorsementDTO.setRequestorId(pedEndorsementHistory.getModifiedBy());
		} else {
			oldPedEndorsementDTO.setRequestorId(pedEndorsementHistory.getCreatedBy());
		}
		if (pedEndorsementHistory.getModifiedBy() != null) {
			String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getCreatedDate());
			oldPedEndorsementDTO.setRequestedDate(formatDate);
		}
			
		List<PedEndorsementDetailsHistory> pedEndorsementDetailsHistory = pedEndorsementService.getPedEndorsementDetailsHistory(pedEndorsementHistory.getKey());
		if (pedEndorsementDetailsHistory != null) {
			List<ViewPEDEndoresementDetailsDTO> pedEndorsementDetailList = new ArrayList<ViewPEDEndoresementDetailsDTO>();
			for(PedEndorsementDetailsHistory endorsementHistory : pedEndorsementDetailsHistory) {
				ViewPEDEndoresementDetailsDTO pedEndorsementObj = new  ViewPEDEndoresementDetailsDTO();
				pedEndorsementObj.setKey(endorsementHistory.getKey());
				if (endorsementHistory.getPedCode() != null) {
					pedEndorsementObj.setPedCode(pedEndorsementService.getPreExistingDiseaseById(endorsementHistory.getPedCode()).getCode());
					pedEndorsementObj.setDescription(pedEndorsementService.getPreExistingDiseaseById(endorsementHistory.getPedCode()).getValue());
				} 
				if (endorsementHistory.getIcdCodeId() != null) {
					pedEndorsementObj.setIcdCode(pedEndorsementService.getIcdCodeById(endorsementHistory.getIcdCodeId()).getValue());
				} 
				if (endorsementHistory.getIcdChapterId() != null) {
					pedEndorsementObj.setIcdChapter(pedEndorsementService.getIcdChapterById(endorsementHistory.getIcdChapterId()).getValue());
				}
				if (endorsementHistory.getIcdBlockId() != null) {
					pedEndorsementObj.setIcdBlock(pedEndorsementService.getIcdBlockById(endorsementHistory.getIcdBlockId()).getValue());
				}
					
				pedEndorsementObj.setSource(endorsementHistory.getSource().getValue());
				pedEndorsementObj.setOthersSpecify(endorsementHistory.getOthesSpecify());
				pedEndorsementObj.setDoctorRemarks(endorsementHistory.getDoctorRemarks());
				pedEndorsementDetailList.add(pedEndorsementObj);
			}
			oldPedEndorsementDTO.setViewPEDEndoresementDetailsDTO(pedEndorsementDetailList);
		}
		return oldPedEndorsementDTO;
	}
	
	public List<ViewClaimHistoryDTO> getClaimHistoryDetails(Intimation intimation) {
		logger.debug("Enter ClaimStatusService.getClaimHistoryDetails() Method");
		List<ViewClaimHistoryDTO> trailsList = null;
		if (intimation != null) {
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.ViewTrailsForCashlessAndReimbursementHistory(intimation);
			if (claimHistoryForReimbursement != null) {
				trailsList = new ArrayList<ViewClaimHistoryDTO>();
				ViewClaimHistoryDTO viewHistoryDto = null;
				for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
					viewHistoryDto = new ViewClaimHistoryDTO();
					viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getTypeofClaim());
					viewHistoryDto.setReferenceNo(viewClaimHistoryDTO.getReferenceNo());
					viewHistoryDto.setCreatedDate(viewClaimHistoryDTO.getCreatedDate());
					viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
					viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
					viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
					viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
					viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
					viewHistoryDto.setDateAndTime(SHAUtils.formateDateForHistory(viewClaimHistoryDTO.getCreatedDate()));
					viewHistoryDto.setClassification(viewClaimHistoryDTO.getBillClassif());
					viewHistoryDto.setDocrecdfrom(viewClaimHistoryDTO.getDocrecdfrom());
					viewHistoryDto.setRodtype(viewClaimHistoryDTO.getRodtype());
					trailsList.add(viewHistoryDto);
				}
			}
		}
		return trailsList;
	}
	
	public ViewClaimStatusDTO getHospitalDetails(ViewClaimStatusDTO intimationDetails, Hospitals hospitals) {
		logger.debug("Enter ClaimStatusService.getHospitalDetails() Method");
		if (hospitals != null) {
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
			if(hospitalDetails != null){
				intimationDetails.setState(hospitalDetails.getState());
				intimationDetails.setCity(hospitalDetails.getCity());
				intimationDetails.setArea(hospitalDetails.getArea());
				intimationDetails.setHospitalAddress(hospitals.getAddress());
				intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
				intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
				intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
				intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
			}	
			EarlierRodMapper.invalidate(instance);
		}
		if (intimationDetails != null && intimationDetails.getIntimationKey() != null) {
			NewBabyIntimation newBabyIntimation = intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey());
			if (newBabyIntimation != null) {
				intimationDetails.setPatientNotCoveredName(newBabyIntimation.getName());
				intimationDetails.setRelationshipWithInsuredId(newBabyIntimation.getBabyRelationship() != null ? newBabyIntimation.getBabyRelationship().getValue() : "");
			}
		}
		return intimationDetails;
	}

}
