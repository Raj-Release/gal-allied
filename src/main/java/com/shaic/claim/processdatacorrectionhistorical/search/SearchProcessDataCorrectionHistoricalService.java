package com.shaic.claim.processdatacorrectionhistorical.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.RevisedClaimMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.processdatacorrectionhistorical.bean.ClaimHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.DiagnosisHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.HospitalScoringHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.PPCodingHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.ProcedureHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.ReimbursementHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.SpecialityHist;
import com.shaic.claim.processdatacorrectionhistorical.bean.TreatingDoctorDetailsHist;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.HospitalScoringService;
import com.shaic.claim.scoring.ppcoding.MasPPCoding;
import com.shaic.claim.scoring.ppcoding.PPCoding;
import com.shaic.claim.scoring.ppcoding.PPCodingService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DataValidation;
import com.shaic.domain.HospitalCategory;
import com.shaic.domain.HospitalScoring;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.preauth.ProcedureSpecialityMaster;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class SearchProcessDataCorrectionHistoricalService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private HospitalScoringService hospitalScoringService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;

	private final Integer SCORING_VERSION = 5;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PPCodingService codingService;

	private final Logger log = LoggerFactory.getLogger(SearchProcessDataCorrectionHistoricalService.class);

	public SearchProcessDataCorrectionHistoricalService() {
		super();
	}

	public HospitalScoring getHospitalScoringBySkey(Long key) {
		Query findByKey = entityManager.createNamedQuery("HospitalScoring.findScoringByKey").setParameter("primaryKey", key);
		List<HospitalScoring> hospitalScorings = (List<HospitalScoring>) findByKey.getResultList();
		if (hospitalScorings !=null && !hospitalScorings.isEmpty()) {
			entityManager.refresh(hospitalScorings.get(0));
			return hospitalScorings.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(Long intimationkey) {
		Query findByKey = entityManager.createNamedQuery("Intimation.findByKey").setParameter("intiationKey", intimationkey);
		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimByKey(Long claimkey) {
		Query findByKey = entityManager.createNamedQuery("Claim.findByKey").setParameter("primaryKey", claimkey);
		List<Claim> claims = (List<Claim>) findByKey.getResultList();
		if (!claims.isEmpty()) {
			entityManager.refresh(claims.get(0));
			return claims.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedByintiKey(Long transactionKey) {

		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", transactionKey);
		List<PedValidation> resultList = (List<PedValidation>) query.getResultList();
		if(resultList!=null && !resultList.isEmpty()){
			return resultList;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public PedValidation findPedByKey(Long key) {

		Query query = entityManager.createNamedQuery("PedValidation.findByKey");
		query.setParameter("primaryKey", key);
		List<PedValidation> resultList = (List<PedValidation>) query.getResultList();
		if(resultList!=null && !resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Speciality findSpecialityByKey(Long primaryKey) {
		Query query = entityManager.createNamedQuery("Speciality.findByKey");
		query.setParameter("primaryKey", primaryKey);
		List<Speciality> resultList = (List<Speciality>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);		
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Procedure findProcedureByKey(Long key) {
		Query query = entityManager.createNamedQuery("Procedure.findByKey");
		query.setParameter("primaryKey", key);

		List<Procedure> resultList = (List<Procedure>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Reimbursement findRODByKey(Long key) {
		Query rodByKey = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", key);
		List<Reimbursement> reimbursements = (List<Reimbursement>) rodByKey.getResultList();
		if (reimbursements !=null && !reimbursements.isEmpty()) {
			return reimbursements.get(0);
		}
		return null;
	}

	private SelectValue getIcdcodeValue(SelectValue icdCode){
		Query rodByKey = entityManager.createNamedQuery("IcdCode.findByKey").setParameter("primaryKey", icdCode.getId());
		List<IcdCode> icdCodes = (List<IcdCode>) rodByKey.getResultList();
		if(icdCodes !=null && !icdCodes.isEmpty()){
			icdCode.setValue(icdCodes.get(0).getDescription()+ " - "+icdCodes.get(0).getValue());
			icdCode.setCommonValue(icdCodes.get(0).getDescription());
		}
		return icdCode;
	}
	private SelectValue getDiganosisValue(SelectValue diganosis){
		Query rodByKey = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey").setParameter("diagnosisKey", diganosis.getId());
		List<Diagnosis> diagnosisdata = (List<Diagnosis>) rodByKey.getResultList();
		if(diagnosisdata !=null && !diagnosisdata.isEmpty()){
			diganosis.setValue(diagnosisdata.get(0).getValue());
		}
		return diganosis;
	}

	@SuppressWarnings("unchecked")
	public DataValidation findDataByKey(String key) {
		Query rodByKey = entityManager.createNamedQuery("DataValidation.findbyIntimationNO").setParameter("intimationNO", key);
		List<DataValidation> reimbursements = (List<DataValidation>) rodByKey.getResultList();
		if (reimbursements !=null && !reimbursements.isEmpty()) {
			return reimbursements.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ProcedureMaster findMasProcByKey(Long key) {
		Query rodByKey = entityManager.createNamedQuery("ProcedureMaster.findByKey").setParameter("primarykey", key);
		List<ProcedureMaster> procedureMasters = (List<ProcedureMaster>) rodByKey.getResultList();
		if (procedureMasters !=null && !procedureMasters.isEmpty()) {
			return procedureMasters.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<TreatingDoctorDetails> findtreatingByClaimKey(Long claimkey) {
		Query rodByKey = entityManager.createNamedQuery("TreatingDoctorDetails.findByClaimKey").setParameter("claimKey", claimkey);
		List<TreatingDoctorDetails> treatingDoctorDetails = (List<TreatingDoctorDetails>) rodByKey.getResultList();
		if (treatingDoctorDetails !=null && !treatingDoctorDetails.isEmpty()) {
			return treatingDoctorDetails;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public TreatingDoctorDetails findtreatingByKey(Long key) {
		Query rodByKey = entityManager.createNamedQuery("TreatingDoctorDetails.findByKey").setParameter("primarykey", key);
		List<TreatingDoctorDetails> treatingDoctorDetails = (List<TreatingDoctorDetails>) rodByKey.getResultList();
		if (treatingDoctorDetails !=null && !treatingDoctorDetails.isEmpty()) {
			return treatingDoctorDetails.get(0);
		}
		return null;
	}



	private List<SpecialityCorrectionDTO> getSpecialityCorrection(Long claimkey,PreMedicalMapper preMedicalMapper,boolean isview) {
		List<SpecialityDTO> specialityDTOList = preMedicalMapper.getSpecialityDTOList(preauthService.findSpecialityByClaimKey(claimkey));
		if (specialityDTOList !=null && !specialityDTOList.isEmpty()) {
			List<SpecialityCorrectionDTO> specialityCorrectionDTOs = new ArrayList<SpecialityCorrectionDTO>();	
			for(SpecialityDTO speciality : specialityDTOList){
				if(speciality.getSpecialityType() !=null){
					SpecialityCorrectionDTO correctionDTO = new SpecialityCorrectionDTO();
					if(isview){
						if(speciality.getSplFlag() !=null &&
								speciality.getSplFlag().equals("Y")){
							correctionDTO.setSpecialityType(speciality.getOldspecialityType());
							correctionDTO.setActualspecialityType(speciality.getSpecialityType());
						}else{	
							correctionDTO.setSpecialityType(speciality.getSpecialityType());
						}
					}else{
						correctionDTO.setKey(speciality.getKey());
						correctionDTO.setSpecialityType(speciality.getSpecialityType());
					}

					specialityCorrectionDTOs.add(correctionDTO);
				}	
			}
			return specialityCorrectionDTOs;
		}
		return null;
	}

	private void setRODroomCat(Long rodKey,ProcessDataCorrectionDTO dataCorrectionDTO,boolean isview) {

		Reimbursement reimbursement = findRODByKey(rodKey);
		if(reimbursement !=null){
			if(isview){
				if(reimbursement.getRcFlag() !=null &&
						reimbursement.getRcFlag().equals("Y")){
					if(reimbursement.getRoomCategory() !=null){
						dataCorrectionDTO.setProposedroomCat(reimbursement.getRoomCategory().getValue());
					}if(reimbursement.getOldRoomCategory() !=null){
						dataCorrectionDTO.setRoomCat(reimbursement.getOldRoomCategory().getValue());
					}if(reimbursement.getTreatmentType() !=null){
						dataCorrectionDTO.setTreatment(reimbursement.getTreatmentType().getValue());
					}
				}else{
					if(reimbursement.getRoomCategory() !=null){
						dataCorrectionDTO.setRoomCat(reimbursement.getRoomCategory().getValue());
					}
					if(reimbursement.getTreatmentType() !=null){
						dataCorrectionDTO.setTreatment(reimbursement.getTreatmentType().getValue());
					}
				}

			}else{
				if(reimbursement.getRoomCategory() !=null){
					SelectValue value = new SelectValue();
					value.setId(reimbursement.getRoomCategory().getKey());
					value.setValue(reimbursement.getRoomCategory().getValue());
					dataCorrectionDTO.setRoomCategory(value);
				}
				if(reimbursement.getTreatmentType() !=null){
					SelectValue value = new SelectValue();
					value.setId(reimbursement.getTreatmentType().getKey());
					value.setValue(reimbursement.getTreatmentType().getValue());
					dataCorrectionDTO.setTreatmentType(value);
				}
			}
		}
	}

	private List<DiganosisCorrectionDTO> getDiganosisCorrection(Long transactionKey,PreMedicalMapper preMedicalMapper,boolean isview){

		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = preMedicalMapper.getNewPedValidationTableListDto(findPedByintiKey(transactionKey));
		if(newPedValidationTableListDto !=null && !newPedValidationTableListDto.isEmpty()){
			int sno =1;
			List<DiganosisCorrectionDTO> diganosisCorrectionDTOs = new ArrayList<DiganosisCorrectionDTO>();		
			for(DiagnosisDetailsTableDTO detailsTableDTO : newPedValidationTableListDto){
				DiganosisCorrectionDTO correctionDTO = new DiganosisCorrectionDTO();		
				if(isview){
					correctionDTO.setSerialNo(sno);
					sno++;
					if(detailsTableDTO.getDiagnosisFlag() !=null &&
							detailsTableDTO.getDiagnosisFlag().equals("Y")){
						correctionDTO.setDiagnosisName(getDiganosisValue(detailsTableDTO.getOldDiagnosisId()));
						correctionDTO.setProposeddiagnosisName(getDiganosisValue(detailsTableDTO.getDiagnosisName()));
					}else{
						correctionDTO.setDiagnosisName(getDiganosisValue(detailsTableDTO.getDiagnosisName()));
					}
					if(detailsTableDTO.getIcdFlag() !=null &&	
							detailsTableDTO.getIcdFlag().equals("Y")){
						correctionDTO.setIcdCode(getIcdcodeValue(detailsTableDTO.getOldIcdCode()));
						correctionDTO.setProposedicdCode(getIcdcodeValue(detailsTableDTO.getIcdCode()));
					}else{
						correctionDTO.setIcdCode(getIcdcodeValue(detailsTableDTO.getIcdCode()));
					}
				}else{
					correctionDTO.setKey(detailsTableDTO.getKey());
					correctionDTO.setDiagnosisName(getDiganosisValue(detailsTableDTO.getDiagnosisName()));
					correctionDTO.setIcdCode(getIcdcodeValue(detailsTableDTO.getIcdCode()));
				}
				if(detailsTableDTO.getPrimaryDiagnosisFlag()!=null 
						&& detailsTableDTO.getPrimaryDiagnosisFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					correctionDTO.setPrimaryDiagnosis(true);
				}
				diganosisCorrectionDTOs.add(correctionDTO);
			}
			return diganosisCorrectionDTOs;
		}
		return null;
	}

	private List<ProcedureCorrectionDTO> getProcedureCorrection(Long transactionKey,PreMedicalMapper preMedicalMapper,boolean isview){
		List<ProcedureDTO> procedureMainDTOList = preMedicalMapper.getProcedureMainDTOList(preauthService
				.findProcedureByPreauthKey(transactionKey));
		if(procedureMainDTOList !=null && !procedureMainDTOList.isEmpty()){
			List<ProcedureCorrectionDTO> procedureCorrectionDTOs = new ArrayList<ProcedureCorrectionDTO>();
			int sno =1;
			for(ProcedureDTO procedureDTO : procedureMainDTOList){
				ProcedureCorrectionDTO procedureCorrectionDTO = new ProcedureCorrectionDTO();
				if(isview){
					procedureCorrectionDTO.setSerialNo(sno);
					sno++;
					if(procedureDTO.getProcedureFlag() !=null &&
							procedureDTO.getProcedureFlag().equals("Y")){
						ProcedureMaster procedureMaster = findMasProcByKey(procedureDTO.getOldprocedureID());
						SelectValue value = new SelectValue();
						if(procedureMaster !=null){
							value.setId(procedureMaster.getKey());
							value.setValue(procedureMaster.getProcedureName());
						}					
						procedureCorrectionDTO.setProcedureCode(value);
						procedureCorrectionDTO.setProcedureName(value);
						procedureCorrectionDTO.setProposedProcedureCode(procedureDTO.getProcedureCode());
						procedureCorrectionDTO.setProposedProcedureName(procedureDTO.getProcedureName());
					}else{
						procedureCorrectionDTO.setProcedureCode(procedureDTO.getProcedureCode());
						procedureCorrectionDTO.setProcedureName(procedureDTO.getProcedureName());
					}	
				}else{
					procedureCorrectionDTO.setKey(procedureDTO.getKey());
					procedureCorrectionDTO.setProcedureCode(procedureDTO.getProcedureCode());
					procedureCorrectionDTO.setProcedureName(procedureDTO.getProcedureName());
				}
				procedureCorrectionDTOs.add(procedureCorrectionDTO);
			}
			return procedureCorrectionDTOs;
		}
		return null;
	}

	public ProcessDataCorrectionDTO getCorrectionDatas(ProcessDataCorrectionDTO dataCorrectionDTO){

		Long claimKey = dataCorrectionDTO.getClaimKey();
		Long intiKey = dataCorrectionDTO.getIntimationKey();
		Long transactionKey = dataCorrectionDTO.getTransactionKey();
		PreMedicalMapper preMedicalMapper = PreMedicalMapper.getInstance();
		if(claimKey !=null){
			RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
			Claim claim = getClaimByKey(claimKey);
			ClaimDto claimDto = claimMapper.getClaimDto(claim);
			if(claim !=null && claim.getIntimation() !=null
					&& claimDto !=null){
				claimDto.setNewIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
			}
			dataCorrectionDTO.setClaimDto(claimDto);
		}
		dataCorrectionDTO.setSpecialityCorrectionDTOs(getSpecialityCorrection(claimKey,preMedicalMapper,false));
		dataCorrectionDTO.setDiganosisCorrectionDTOs(getDiganosisCorrection(transactionKey,preMedicalMapper,false));
		setRODroomCat(transactionKey,dataCorrectionDTO,false);
		if(dataCorrectionDTO.getTreatmentType()!=null && dataCorrectionDTO.getTreatmentType().getValue().equals("Surgical")){
			dataCorrectionDTO.setProcedureCorrectionDTOs(getProcedureCorrection(transactionKey,preMedicalMapper,false));
		}
		dataCorrectionDTO.setTreatingCorrectionDTOs(getTreatingCorrection(claimKey,false));
		return dataCorrectionDTO;

	}

	private void saveDiganosisCorrectionDTO(List<DiganosisCorrectionDTO> diganosisCorrectionDTOs,String userID,DataValidationHistMapper dataValidationHistMapper){
		if(diganosisCorrectionDTOs !=null && !diganosisCorrectionDTOs.isEmpty()){
			for(DiganosisCorrectionDTO correctionDTO : diganosisCorrectionDTOs){
				if(correctionDTO.getHasChanges()){
					PedValidation pedValidation = findPedByKey(correctionDTO.getKey());
					DiagnosisHist diagnosisHist = dataValidationHistMapper.getDiagnosisHist(pedValidation);
					if(diagnosisHist !=null ){
						if(correctionDTO.getProposeddiagnosisName() !=null 
								&& !correctionDTO.getDiagnosisName().getId().equals(correctionDTO.getProposeddiagnosisName().getId())){
							diagnosisHist.setOldDiagnosisId(correctionDTO.getDiagnosisName().getId());
							diagnosisHist.setDiagnosisId(correctionDTO.getProposeddiagnosisName().getId());
							diagnosisHist.setDiagnosisFlag("Y");
						}
						if(correctionDTO.getProposedicdCode() !=null 
								&& !correctionDTO.getIcdCode().getId().equals(correctionDTO.getProposedicdCode().getId())){
							diagnosisHist.setOldIcdCode(correctionDTO.getIcdCode().getId());
							diagnosisHist.setIcdCodeId(correctionDTO.getProposedicdCode().getId());
							diagnosisHist.setIcdFlag("Y");
						}
						diagnosisHist.setModifiedBy(userID);
						diagnosisHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));

						entityManager.persist(diagnosisHist);
						entityManager.flush();
						entityManager.clear();
						log.info("------DiagnosisHist------>"+diagnosisHist+"<------------");
					}	
				}
			}
		}
	}

	private void saveSpecialityCorrectionDTO(List<SpecialityCorrectionDTO> specialityCorrectionDTOs,String userID,DataValidationHistMapper dataValidationHistMapper,Claim claim){

		if(specialityCorrectionDTOs !=null && !specialityCorrectionDTOs.isEmpty()){
			for(SpecialityCorrectionDTO specialityCorrectionDTO:specialityCorrectionDTOs){
				if(specialityCorrectionDTO.getHasChanges()){
					if(specialityCorrectionDTO.getKey() !=null){
						Speciality speciality = findSpecialityByKey(specialityCorrectionDTO.getKey());
						SpecialityHist specialityHist = dataValidationHistMapper.getSpecialityHist(speciality);
						if(specialityHist !=null){
							SpecialityType specialityType = new SpecialityType();
							ProcedureSpecialityMaster proc = new ProcedureSpecialityMaster();
							proc.setKey(specialityCorrectionDTO.getActualProcedure().getId());
							specialityType.setKey(specialityCorrectionDTO.getActualspecialityType().getId());
							specialityHist.setOldspecialityType(speciality.getSpecialityType());
							specialityHist.setSpecialityType(specialityType);
							specialityHist.setOldprocedure(speciality.getProcedure());
							specialityHist.setOldRemark(speciality.getRemarks());
							specialityHist.setProcedure(proc);
							specialityHist.setRemarks(specialityCorrectionDTO.getActualRemarks());
							specialityHist.setSplFlag("Y");
							entityManager.persist(specialityHist);
							entityManager.flush();
							entityManager.clear();
							log.info("------SpecialityHist------>"+specialityHist+"<------------");
						}
					}else{
						SpecialityHist speciality = new SpecialityHist();
						SpecialityType specialityType = new SpecialityType();
						ProcedureSpecialityMaster proc = new ProcedureSpecialityMaster();
						specialityType.setKey(specialityCorrectionDTO.getActualspecialityType().getId());
						proc.setKey(specialityCorrectionDTO.getActualProcedure().getId());
						speciality.setClaim(claim);
						speciality.setSpecialityType(specialityType);
						speciality.setProcedure(proc);
						speciality.setSplFlag("Y");
						speciality.setCreatedBy(userID);
						speciality.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						speciality.setActiveStatus(1L);
						entityManager.persist(speciality);
						entityManager.flush();
						entityManager.clear();
						log.info("------SpecialityHist------>"+speciality+"<------------");
					}
				}
			}
		}

	}

	private void saveProcedureCorrectionDTO(List<ProcedureCorrectionDTO> procedureCorrectionDTOs,String userID,DataValidationHistMapper dataValidationHistMapper){

		if(procedureCorrectionDTOs !=null && !procedureCorrectionDTOs.isEmpty()){
			for(ProcedureCorrectionDTO correctionDTO:procedureCorrectionDTOs){
				if(correctionDTO.getHasChanges()){
					Procedure procedure = findProcedureByKey(correctionDTO.getKey());
					ProcedureHist procedureHist = dataValidationHistMapper.getProcedureHist(procedure);
					if(procedureHist !=null){
						procedureHist.setOldprocedureID(procedure.getProcedureID());
						if(correctionDTO.getProposedProcedureName() !=null 
								&& !correctionDTO.getProposedProcedureName().getId().equals(correctionDTO.getProcedureName().getId())){
							procedureHist.setProcedureID(correctionDTO.getProposedProcedureName().getId());
							procedureHist.setProcedureName(correctionDTO.getProposedProcedureName().getValue());
							if(correctionDTO.getProposedProcedureCode() !=null){
								procedureHist.setProcedureCode(correctionDTO.getProposedProcedureCode().getValue());

							}
						}		
						procedureHist.setProcedureFlag("Y");
						procedureHist.setModifiedBy(userID);
						procedureHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.persist(procedureHist);
						entityManager.flush();
						entityManager.clear();
						log.info("------ProcedureHist------>"+procedureHist+"<------------");
					}
				}
			}
		}

	}

	public void saveDataCorrection(ProcessDataCorrectionDTO dataCorrectionDTO,String userID){
		boolean haschanges =false;
		DataValidationHistMapper dataValidationHistMapper = DataValidationHistMapper.getInstance();
		Claim claim = getClaimByKey(dataCorrectionDTO.getClaimKey());
		ClaimHist claimHist = dataValidationHistMapper.getClaim(claim);
		Reimbursement reimbursement = findRODByKey(dataCorrectionDTO.getTransactionKey());
		ReimbursementHist reimbursementHist =  dataValidationHistMapper.getReimbursementHist(reimbursement);
		if(dataCorrectionDTO.getProposedroomCategory() !=null && dataCorrectionDTO.getRoomCategory() !=null &&
				!dataCorrectionDTO.getProposedroomCategory().getId().equals(dataCorrectionDTO.getRoomCategory().getId())){
			haschanges= true;
			
			if(reimbursementHist !=null){
				MastersValue mastersValue = new MastersValue();
				mastersValue.setKey(dataCorrectionDTO.getProposedroomCategory().getId());
				reimbursementHist.setOldRoomCategory(reimbursement.getRoomCategory());
				reimbursementHist.setRoomCategory(mastersValue);
				reimbursementHist.setRcFlag("Y");
				reimbursementHist.setModifiedBy(userID);
				reimbursementHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursementHist.setDcFlag("Y");
				entityManager.persist(reimbursementHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------ReimbursementHist------>"+reimbursementHist+"<------------");
			}
		}else if(dataCorrectionDTO.getProposedroomCategory() !=null && dataCorrectionDTO.getRoomCategory() ==null){
			haschanges= true;

			if(reimbursementHist !=null){
				MastersValue mastersValue = new MastersValue();
				mastersValue.setKey(dataCorrectionDTO.getProposedroomCategory().getId());
				reimbursementHist.setOldRoomCategory(reimbursement.getRoomCategory());
				reimbursementHist.setRoomCategory(mastersValue);
				reimbursementHist.setRcFlag("Y");
				reimbursementHist.setModifiedBy(userID);
				reimbursementHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursementHist.setDcFlag("Y");
				entityManager.persist(reimbursementHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------ReimbursementHist------>"+reimbursementHist+"<------------");
			}
		}
		if(dataCorrectionDTO.getIsspecialityChanged()){
			haschanges= true;
			saveSpecialityCorrectionDTO(dataCorrectionDTO.getSpecialityCorrectionDTOs(),userID,dataValidationHistMapper,claim);
		}if(dataCorrectionDTO.getIsdiganosisChanged()){
			haschanges= true;
			saveDiganosisCorrectionDTO(dataCorrectionDTO.getDiganosisCorrectionDTOs(),userID,dataValidationHistMapper);
		}if(dataCorrectionDTO.getIsprocedureChanged()){
			haschanges= true;
			saveProcedureCorrectionDTO(dataCorrectionDTO.getProcedureCorrectionDTOs(),userID,dataValidationHistMapper);
		}if(dataCorrectionDTO.getIsScoringChanged()){
			haschanges= true;
			saveScoringChanges(dataCorrectionDTO.getScoringDTOs(),userID,dataValidationHistMapper);
		}if(dataCorrectionDTO.getIstreatingChanged()){
			haschanges= true;
			saveDoctorCorrectionDTO(dataCorrectionDTO.getTreatingCorrectionDTOs(),userID,dataCorrectionDTO.getTransactionKey(),dataCorrectionDTO.getClaimKey(),dataValidationHistMapper);
		}if(dataCorrectionDTO.getIsPPChangesmade()){
			haschanges= true;
			saveppCorrectionDTO(dataCorrectionDTO.getSelectedPPCoding(), dataCorrectionDTO.getPpCodingSelected(),userID,dataCorrectionDTO.getIntimationKey(),claimHist,dataValidationHistMapper);
		}
		if(haschanges){		
			if(claimHist !=null){
				claimHist.setCoadingUser(userID);
				claimHist.setCoadingDate(new Timestamp(System.currentTimeMillis()));
				claimHist.setCoadingFlag("Y");
				claimHist.setDcCoadingFlag("Y");
				claimHist.setCoadingRemark(dataCorrectionDTO.getRemarks());
				claimHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				claimHist.setModifiedBy(userID);
				entityManager.persist(claimHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------claimHist------>"+claimHist+"<------------");
			}
			if(reimbursementHist !=null
					&& !dataCorrectionDTO.getIsroomCatChanged()){
				reimbursementHist.setModifiedBy(userID);
				reimbursementHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursementHist.setDcFlag("Y");
				entityManager.persist(reimbursementHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------ReimbursementHist------>"+reimbursementHist+"<------------");
			}
			dbCalculationService.dataCoadingHistoricalRelease(userID,null,"SUBMIT",dataCorrectionDTO.getCoadingKey());
		}else{
			if(claimHist !=null){
				claimHist.setCoadingUser(userID);
				claimHist.setCoadingDate(new Date());
				claimHist.setCoadingFlag("N");
				claimHist.setDcCoadingFlag("Y");
				claimHist.setCoadingRemark(dataCorrectionDTO.getRemarks());
				claimHist.setModifiedDate(new Date());
				entityManager.persist(claimHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------claimHist------>"+claim+"<------------");
			}
			if(reimbursementHist !=null){
				reimbursementHist.setModifiedBy(userID);
				reimbursementHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				reimbursementHist.setDcFlag("Y");
				entityManager.persist(reimbursementHist);
				entityManager.flush();
				entityManager.clear();
				log.info("------ReimbursementHist------>"+reimbursementHist+"<------------");
			}
			dbCalculationService.dataCoadingHistoricalRelease(userID,null,"NOCHANGE",dataCorrectionDTO.getCoadingKey());
		}

	}

	private void saveScoringChanges(List<HospitalScoringDTO> scoringDTOs,String loginUserId,DataValidationHistMapper dataValidationHistMapper){
		if(scoringDTOs !=null && !scoringDTOs.isEmpty()){
			for(HospitalScoringDTO rec : scoringDTOs){
				HospitalScoring updIntimationScoring = getHospitalScoringBySkey(rec.getKey());
				if(updIntimationScoring != null){
					HospitalScoringHist scoringHist = dataValidationHistMapper.getHospitalScoringHist(updIntimationScoring);
					if((updIntimationScoring.getGradeScore() !=null 
							&& !updIntimationScoring.getGradeScore().equals(rec.getScoringValue()))
							||(updIntimationScoring.getGradeScore() == null && rec.getScoringValue()!=null)){
						scoringHist.setOldGradeScore(updIntimationScoring.getGradeScore());
						scoringHist.setGradeScore(rec.getScoringValue());
						scoringHist.setModifiedBy(loginUserId.toLowerCase());
						scoringHist.setModifiedDate((new Timestamp(System.currentTimeMillis())));
						scoringHist.setScoringVersion(SCORING_VERSION);
						scoringHist.setDeleteFlag(0);	
						entityManager.persist(scoringHist);
						entityManager.flush();
						entityManager.clear();
						log.info("------HospitalScoringHist------>"+scoringHist+"<------------");
					}else{
						scoringHist.setModifiedBy(loginUserId.toLowerCase());
						scoringHist.setModifiedDate((new Timestamp(System.currentTimeMillis())));
						entityManager.persist(scoringHist);
						entityManager.flush();
						entityManager.clear();
						log.info("------HospitalScoringHist------>"+scoringHist+"<------------");
					}
				}
			}
		}
	}

	public boolean iscScoringChangesmade(List<HospitalScoringDTO> scoringDTOs){
		if(scoringDTOs !=null && !scoringDTOs.isEmpty()){
			for(HospitalScoringDTO rec : scoringDTOs){
				HospitalScoring scoring = getHospitalScoringBySkey(rec.getKey());
				if(scoring != null){
					if((scoring.getGradeScore() !=null 
							&& !scoring.getGradeScore().equals(rec.getScoringValue()))
							||(scoring.getGradeScore() == null && rec.getScoringValue()!=null)){
						return true;
					}
				}
			}
		}
		return false;			
	}

	private List<TreatingCorrectionDTO> getTreatingCorrection(Long claimKey,boolean isview){
		List<TreatingDoctorDetails> treatingDoctorDetails = findtreatingByClaimKey(claimKey);
		if (treatingDoctorDetails !=null && !treatingDoctorDetails.isEmpty()) {
			List<TreatingCorrectionDTO> treatingCorrectionDTOs = new ArrayList<TreatingCorrectionDTO>();	
			int sno=1;
			for(TreatingDoctorDetails doctorDetails : treatingDoctorDetails){
				TreatingCorrectionDTO correctionDTO = new TreatingCorrectionDTO();
				if(isview){
					correctionDTO.setSerialNo(sno);
					sno++;
					if(doctorDetails.getDcDoctorFlag() !=null 
							&& doctorDetails.getDcDoctorFlag().equals("Y")
							&& doctorDetails.getOldDoctorName() !=null){
						correctionDTO.setTreatingDoctorName(doctorDetails.getOldDoctorName());
						correctionDTO.setQualification(doctorDetails.getOldQualification());
						correctionDTO.setActualtreatingDoctorName(doctorDetails.getDoctorName());
						correctionDTO.setActualqualification(doctorDetails.getDoctorQualification());
					}else if(doctorDetails.getDcDoctorFlag() !=null 
							&& doctorDetails.getDcDoctorFlag().equals("Y")
							&& doctorDetails.getOldDoctorName() == null){
						correctionDTO.setActualtreatingDoctorName(doctorDetails.getDoctorName());
						correctionDTO.setActualqualification(doctorDetails.getDoctorQualification());
					}else{
						correctionDTO.setTreatingDoctorName(doctorDetails.getDoctorName());
						correctionDTO.setQualification(doctorDetails.getDoctorQualification());
					}
					if(doctorDetails.getTreatingDoctorSignature() != null){
						SelectValue selVal = new SelectValue();
						if(doctorDetails.getTreatingDoctorSignature().equalsIgnoreCase("Yes")){
							selVal.setId(1l);
							selVal.setValue("Yes");
							correctionDTO.setTreatingDoctorSignature(selVal);
						} else {
							selVal.setId(2l);
							selVal.setValue("No");
							correctionDTO.setTreatingDoctorSignature(selVal);
						}
					}else {
						SelectValue selValNO = new SelectValue();
						selValNO.setId(2l);
						selValNO.setValue("No");
						correctionDTO.setTreatingDoctorSignature(selValNO);
					}
					
				}else{
					correctionDTO.setKey(doctorDetails.getKey());
					correctionDTO.setTreatingDoctorName(doctorDetails.getDoctorName());
					correctionDTO.setQualification(doctorDetails.getDoctorQualification());
					if(doctorDetails.getTreatingDoctorSignature() != null){
						SelectValue selVal = new SelectValue();
						if(doctorDetails.getTreatingDoctorSignature().equalsIgnoreCase("Yes")){
							selVal.setId(1l);
							selVal.setValue("Yes");
							correctionDTO.setTreatingDoctorSignature(selVal);
						} else {
							selVal.setId(2l);
							selVal.setValue("No");
							correctionDTO.setTreatingDoctorSignature(selVal);
						}
					}else {
						SelectValue selValNO = new SelectValue();
						selValNO.setId(2l);
						selValNO.setValue("No");
						correctionDTO.setTreatingDoctorSignature(selValNO);
					}
				}
				treatingCorrectionDTOs.add(correctionDTO);		
			}
			return treatingCorrectionDTOs;
		}
		return null;
	}

	private void saveDoctorCorrectionDTO(List<TreatingCorrectionDTO> doctorDetails,String userID,Long rodKey,Long claimKey,DataValidationHistMapper dataValidationHistMapper){

		if(doctorDetails !=null && !doctorDetails.isEmpty()){
			for(TreatingCorrectionDTO treatingDoctorDetails:doctorDetails){
				if(treatingDoctorDetails.getHasChanges()){
					if(treatingDoctorDetails.getKey() !=null){
						TreatingDoctorDetails details = findtreatingByKey(treatingDoctorDetails.getKey());
						TreatingDoctorDetailsHist doctorDetailsHist = dataValidationHistMapper.getTreatingDoctorDetailsHist(details);
						if(doctorDetailsHist !=null){
							doctorDetailsHist.setOldDoctorName(details.getDoctorName());
							doctorDetailsHist.setOldQualification(details.getDoctorQualification());
							doctorDetailsHist.setDoctorName(treatingDoctorDetails.getActualtreatingDoctorName());
							doctorDetailsHist.setDoctorQualification(treatingDoctorDetails.getActualqualification());
							doctorDetailsHist.setDcDoctorFlag("Y");
							entityManager.persist(doctorDetailsHist);
							entityManager.flush();
							entityManager.clear();
							log.info("------TreatingDoctorDetailsHist------>"+doctorDetailsHist+"<------------");
						}
					}else{
						TreatingDoctorDetailsHist details = new TreatingDoctorDetailsHist();
						details.setClaimKey(claimKey);
						details.setTransactionKey(rodKey);
						details.setCreatedBy(userID);
						details.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						details.setActiveStatus(1L);	
						details.setDoctorName(treatingDoctorDetails.getActualtreatingDoctorName());
						details.setDoctorQualification(treatingDoctorDetails.getActualqualification());
						details.setDcDoctorFlag("Y");
						entityManager.persist(details);
						entityManager.flush();
						entityManager.clear();
						log.info("------TreatingDoctorDetailsHist------>"+details+"<------------");
					}

				}
			}
		}

	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery("Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByIntimationNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("Claim.findByIntimationNo").setParameter("intimationNumber", intimationNo);
		List<Claim> claims = (List<Claim>) findByKey.getResultList();
		if (!claims.isEmpty()) {
			entityManager.refresh(claims.get(0));
			return claims.get(0);
		}
		return null;
	}
	
	public Boolean isPPChangesmade(Boolean ppCodingSelected ,Map<String, Boolean> selectedPPCoding,String intimationNumber){
		Claim claim = getClaimByIntimationNo(intimationNumber);
		String ppsource = ppCodingSelected ? "Y" : "N";
		if(claim !=null && claim.getPpProtected() !=null){	
			if(!claim.getPpProtected().equals(ppsource)){
				return true;
			}else if(ppsource.equals("N")){
				List<PPCoding> ppCodings= codingService.getPPCoding(claim.getIntimation().getKey());
				if(ppCodings !=null && !ppCodings.isEmpty()){
					for(PPCoding ppCoding:ppCodings){
						String source = selectedPPCoding.get(ppCoding.getPpCode()) ? "Y" : "N";
						if(!source.equals(ppCoding.getPpScore())){
							return true;
						}
					}
				}else if(ppCodings == null && selectedPPCoding!=null){
					return true;
				}
			}
		}else if(ppsource !=null){
			return true;
		}
		
		return false;
	}
	
	private void saveppCorrectionDTO(Map<String, Boolean> selectedPPCoding,Boolean ppCodingSelected,String userID,Long intimationkey,ClaimHist claimHist,DataValidationHistMapper dataValidationHistMapper){
		Intimation intimation = getIntimationByNo(intimationkey);
		Hospitals hospital = getHospitalDetailsByKey(intimation.getHospital());
		String ppsource = ppCodingSelected ? "Y" : "N";
		String oldppsource = null;

		if(claimHist !=null && claimHist.getPpProtected() !=null
				&& !claimHist.getPpProtected().equals(ppsource)){
			oldppsource = claimHist.getPpProtected();
			claimHist.setDcppFlag("Y");
			claimHist.setOldppProtected(claimHist.getPpProtected());
			claimHist.setPpProtected(ppsource);
		}else if(claimHist.getPpFlag() == null){
			claimHist.setPpFlag("Y");
			claimHist.setDcppFlag("Y");
			claimHist.setPpProtected(ppsource);
		}else if(claimHist !=null && claimHist.getPpProtected() !=null
				&& claimHist.getPpProtected().equals(ppsource)){
			claimHist.setDcppFlag("Y");
			claimHist.setOldppProtected(ppsource);
		}

		if(ppsource.equals("N")){
			if(selectedPPCoding != null){
				List<PPCoding> codings =codingService.getPPCoding(intimationkey);
				if(codings !=null && !codings.isEmpty()){
					for(PPCoding coding : codings){
						if(selectedPPCoding.get(coding.getPpCode()) != null){
							PPCodingHist codingHist = dataValidationHistMapper.getPpCodingHist(coding);
							String source = selectedPPCoding.get(coding.getPpCode()) ? "Y" : "N";
							if(!coding.getPpScore().equals(ppsource)){
								codingHist.setOldppScore(coding.getPpScore());
								codingHist.setPpScore(source);
								codingHist.setModifiedBy(userID);
								codingHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								codingHist.setPpStage("DataValidation Historical");
								entityManager.persist(codingHist);
							}
						}
					}
				}else{
					String hospitalType ="";
					if(intimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
						hospitalType = "Network";
					}else{
						hospitalType = "Non-Network";
					}
					List<MasPPCoding>  masPPCodings= codingService.getMasPPCoding(hospitalType);
					if(masPPCodings !=null && !masPPCodings.isEmpty()){
						for(MasPPCoding masPPCoding:masPPCodings){
							PPCodingHist ppCoding = new PPCodingHist();
							ppCoding.setIntimationKey(intimation.getKey());
							ppCoding.setClaimKey(claimHist.getKey());
							ppCoding.setPpCode(masPPCoding.getPpCode());
							ppCoding.setHospitalKey(intimation.getHospital());
							ppCoding.setHospitalType(hospitalType);
							if(hospital != null){
								ppCoding.setHospitalCode(hospital.getHospitalCode());
							}
							if(selectedPPCoding.get(masPPCoding.getPpCode()) != null){
								String source = selectedPPCoding.get(masPPCoding.getPpCode()) ? "Y" : "N";
								ppCoding.setPpScore(source);
							}
							ppCoding.setPpStage("DataValidation Historical");
							ppCoding.setCreatedBy(userID);
							ppCoding.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							ppCoding.setDeleteFlag(0L);
							ppCoding.setPpVersion(ReferenceTable.PP_VERSION);
							entityManager.persist(ppCoding);
						}

					}
				}
			}
		}else if(ppsource.equals("Y")){
			List<PPCoding> codings = codingService.getPPCoding(intimationkey);
			if(codings !=null && !codings.isEmpty()){
				for(PPCoding coding : codings){
					PPCodingHist codingHist = dataValidationHistMapper.getPpCodingHist(coding);
					codingHist.setDeleteFlag(1L);
					codingHist.setModifiedBy(userID);
					codingHist.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					codingHist.setPpStage("DataValidation Historical");
					entityManager.persist(codingHist);
				}
			}
		}
		entityManager.flush();
		entityManager.clear();	
	}
}
