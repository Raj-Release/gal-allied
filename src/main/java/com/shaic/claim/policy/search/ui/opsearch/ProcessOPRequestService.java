/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import bsh.StringUtil;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPDocumentList;
import com.shaic.domain.outpatient.OPDocumentSummary;
import com.shaic.domain.outpatient.OPHCDetails;
import com.shaic.domain.outpatient.OPHealthCheckup;
/*import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.customer.CustomerType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType;*/
/*import com.shaic.ims.bpm.claim.corev2.PagedTaskList;
import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.ims.bpm.claim.servicev2.ophealth.search.ProcessClaimTask;*/
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;

/**
 * @author SARAVANA
 *
 * This service file is common for search and submit services , related
 * with PROCESS CLAIM OP CHECKUP menu.
 */
@Stateless
public class ProcessOPRequestService extends AbstractDAO<Intimation>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private ClaimService claimService;
	
	public ProcessOPRequestService() {
		super();
	}
	
	public Claim getClaimByKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> singleResult = query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		return null;
	}
	
	public List<OPDocumentList> getOPDocumentListByClaimKey(Long opHealthCheckupKey) {
		Query query = entityManager.createNamedQuery("OPDocumentList.findByopHealthCheckupKey");
		query.setParameter("opHealthCheckupKey", opHealthCheckupKey);
		List<OPDocumentList> resultList =  query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for(OPDocumentList opDocumentList : resultList){
			entityManager.refresh(opDocumentList);
			}
			return resultList;
		}
		return null;
	}
	
	public List<OPHCDetails> getOpHCDetails(Long healthCheckupKey) {
		Query query = entityManager.createNamedQuery("OPHCDetails.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPHCDetails> resultList =  query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPHCDetails ophcDetails : resultList) {
				entityManager.refresh(ophcDetails);
			}
			return resultList;
		}
		return resultList;
	}
	
	public OPHealthCheckup getOpHealthByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("OPHealthCheckup.findByClaim");
		query.setParameter("claimKey", claimKey);
		List<OPHealthCheckup> singleResult =  query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		return null;
	}

	public List<OPDocumentBillEntry> getOpBillEntryByOPHealthKey(Long healthCheckupKey){
		Query query=entityManager.createNamedQuery("OPDocumentBillEntry.findByHealthCheckupKey");
		query.setParameter("healthCheckupKey", healthCheckupKey);
		@SuppressWarnings("unchecked")
		List<OPDocumentBillEntry> resultList = query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (OPDocumentBillEntry opDocumentBillEntry : resultList) {
				entityManager.refresh(opDocumentBillEntry);
			}
			return resultList;
		}
		
		return resultList;
	}

	public  Page<SearchProcessOPClaimRequestTableDTO> search(SearchProcessOPClaimFormDTO searchFormDTO, String userName, String passWord) {
		try{			
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			CriteriaQuery<OPHealthCheckup> OPCriteriaQuery = builder.createQuery(OPHealthCheckup.class);
			Root<OPHealthCheckup> searchRoot = OPCriteriaQuery.from(OPHealthCheckup.class);
			// As per Raja Instruction commented below code
			/*List<String> officeCodeList = new ArrayList<String>();
			String processOPUserValidation = getProcessOPUserValidation(userName, null);
			if(StringUtils.isNotBlank(processOPUserValidation)){
				String[] split = processOPUserValidation.split(",");
				for(String officeCode : split){
					officeCodeList.add(officeCode);
				}
			}*/
			
			List<OPHealthCheckup> opSearchResultList = new ArrayList<OPHealthCheckup>();
			
			if(StringUtils.isNotBlank(searchFormDTO.getIntimationNo())){
				Predicate searchByIntimation = builder.like(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<String>get("intimationId"), "%"+searchFormDTO.getIntimationNo()+"%");
				predicates.add(searchByIntimation);
//				System.out.println("OP Intimation No : "+searchFormDTO.getIntimationNo());
			}
			
			if(StringUtils.isNotBlank(searchFormDTO.getClaimNo())){
				Predicate searchByClaimNo = builder.like(searchRoot.<OPClaim>get("claim").<String>get("claimId"), "%"+searchFormDTO.getClaimNo()+"%");
				predicates.add(searchByClaimNo);
//				System.out.println("OP Intimation No : "+searchFormDTO.getIntimationNo());
			}
			
			if(StringUtils.isNotBlank(searchFormDTO.getPolicyNumber())){
				Predicate searchByPolicy = builder.equal(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), searchFormDTO.getPolicyNumber());
				predicates.add(searchByPolicy);
//				System.out.println("OP Policy No : "+searchFormDTO.getPolicyNumber());
			}
			
			if(StringUtils.isNotBlank(searchFormDTO.getHealthCardNo())){
				Predicate searchByHealthCardNo = builder.equal(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Insured>get("insured").<String>get("healthCardNumber"), searchFormDTO.getHealthCardNo());
				predicates.add(searchByHealthCardNo);
//				System.out.println("OP Health Card Number : "+searchFormDTO.getHealthCardNo());
			}
			
			if(searchFormDTO.getClaimType() != null){
				Predicate searchByHealthCardNo = builder.equal(searchRoot.<OPClaim>get("claim").<MastersValue>get("claimType").<Long>get("key"), searchFormDTO.getClaimType().getId());
				predicates.add(searchByHealthCardNo);
//				System.out.println("OP Claim Type : "+searchFormDTO.getClaimType().getId());
			}
			
			/*System.out.println(String.format("PIO CODE [%s]", searchFormDTO.getPioCode().getId()));

			System.out.println(String.format("PIO CODE Value [%s]", searchFormDTO.getPioCode().getValue()));*/
			if(searchFormDTO.getPioCode() != null){
				Predicate searchByPIOCode = builder.like(searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+searchFormDTO.getPioCode().getValue()+"%");
				//Predicate searchByPIOCode = builder.equal(searchRoot.<OPClaim>get("claim").<OrganaizationUnit>get("OrganaizationUnit").<String>get("organizationUnitId"), searchFormDTO.getPioCode().getValue());
				predicates.add(searchByPIOCode);
			}
			
			// As per Raja Instruction commented below code
			/*if(officeCodeList.size() > 0){
				Expression<String> exp = searchRoot.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("homeOfficeCode"); 
				Predicate searchByOfficeCode = exp.in(officeCodeList);
				predicates.add(searchByOfficeCode);
//				System.out.println("OP Health Card Number : "+searchFormDTO.getHealthCardNo());
			}*/
			

			Predicate searchStage = builder.equal(searchRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.OP_STAGE);
			predicates.add(searchStage);
			
			Predicate searchStatus = builder.equal(searchRoot.<Status>get("status").<Long>get("key"), ReferenceTable.OP_REGISTER_CLAIM_STATUS);
			predicates.add(searchStatus);
			
			OPCriteriaQuery.select(searchRoot).where(builder.and(predicates.toArray(new Predicate[] {}))).orderBy(builder.desc(searchRoot.<Long>get("key")));
			TypedQuery<OPHealthCheckup> opquery = entityManager.createQuery(OPCriteriaQuery);
			opSearchResultList = opquery.getResultList();
			List<SearchProcessOPClaimRequestTableDTO> tableDTO = new ArrayList<SearchProcessOPClaimRequestTableDTO>();
			
			if(opSearchResultList != null && opSearchResultList.size() > 0){
				for(OPHealthCheckup rec : opSearchResultList){
					SearchProcessOPClaimRequestTableDTO dto = new SearchProcessOPClaimRequestTableDTO();
					dto.setClaimKey(rec.getClaim().getKey());
					dto.setClaimNo(rec.getClaim().getClaimId());
					dto.setPolicyNo(rec.getClaim().getIntimation().getPolicyNumber());
					dto.setClaimType(rec.getClaim().getClaimType() != null ? rec.getClaim().getClaimType().getValue() : "");
					//dto.setPioCode(rec.getOfficeCode() != null ? rec.getOfficeCode() : "");
					dto.setHealthCardIDNo(rec.getClaim().getIntimation().getInsured().getHealthCardNumber());
					dto.setHealthCheckupDate(rec.getOpHealthCheckupDate());
					dto.setHealthCheckupKey(rec.getKey());
					dto.setInsuredPatientName(rec.getClaim().getIntimation().getInsured().getInsuredName());
					dto.setIntimationNo(rec.getClaim().getIntimation().getIntimationId());
					dto.setReasonForHealthVisit((rec.getClaim().getIntimation().getAdmissionReason() == null)?"":rec.getClaim().getIntimation().getAdmissionReason());
					dto.setAmountClaimed(String.valueOf(rec.getClaim().getClaimedAmount() != null ? rec.getClaim().getClaimedAmount().longValue() : "") );
					dto.setPassword(passWord);
					dto.setUsername(userName);
					tableDTO.add(dto);
				}
				
			}
			
			
			Page<SearchProcessOPClaimRequestTableDTO> page = new Page<SearchProcessOPClaimRequestTableDTO>();
			page.setPageItems(tableDTO);
			page.setTotalRecords(tableDTO.size());
			page.setTotalList(tableDTO);
			return page;
			
			
			
			/*List<Long> keys = new ArrayList<Long>();
			List<SearchProcessOPClaimRequestTableDTO> tableDTO = new ArrayList<SearchProcessOPClaimRequestTableDTO>();
			keys.add(50016047L);
			Collections.sort(keys);
			for (Long claimKey : keys) {
				
				if(claimKey != null) {
					
					Claim claim = getClaimByKey(claimKey);
					
					OPHealthCheckup opHealthByClaimKey = getOpHealthByClaimKey(claimKey);
					
					if(opHealthByClaimKey != null) {
						List<OPHCDetails> opHCDetails = getOpHCDetails(opHealthByClaimKey.getKey());
						String billDate = "";
						String reason = "";
						
						if(opHCDetails != null) {
							for (int i = 0; i < opHCDetails.size() ; i++) {
								OPHCDetails ophcDetails2 = opHCDetails.get(i);
								if(ophcDetails2.getTreatmentDate() != null) {
									billDate += SHAUtils.formatDate(ophcDetails2.getTreatmentDate());
									if(!(i == (opHCDetails.size() - 1))) {
										billDate += ", ";
									}
								}
								
								if(ophcDetails2.getReasonForVisit() != null) {
									reason += ophcDetails2.getReasonForVisit();
									if(!(i == (opHCDetails.size() - 1))) {
										reason += ", ";
									}
								}
							}
						}
						SearchProcessOPClaimRequestTableDTO dto = new SearchProcessOPClaimRequestTableDTO();
						dto.setClaimKey(claim.getKey());
						dto.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber());
						dto.setClaimType(claim.getClaimType() != null ? claim.getClaimType().getValue() : "");
						dto.setHealthCardIDNo(claim.getIntimation().getInsured().getHealthCardNumber());
						dto.setHealthCheckupDate(billDate);
						dto.setHealthCheckupKey(opHealthByClaimKey.getKey());
						//dto.setOpHumanTask(mappping.get(claimKey));
						dto.setInsuredPatientName(claim.getIntimation().getInsuredPatientName());
						dto.setIntimationNo(claim.getIntimation().getIntimationId());
						dto.setReasonForHealthVisit(reason);
						dto.setAmountClaimed(String.valueOf(claim.getClaimedAmount() != null ? claim.getClaimedAmount().longValue() : "") );
						dto.setPassword(passWord);
						dto.setUsername(userName);
						tableDTO.add(dto);
					}
					
					
				}
			}
			Page<SearchProcessOPClaimRequestTableDTO> page = new Page<SearchProcessOPClaimRequestTableDTO>();
			page.setTotalRecords(tableDTO.size());
			page.setTotalList(tableDTO);
			page.setPageItems(tableDTO);
			return page;*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
			return null;	
		}
	
	public String getProcessOPUserValidation(String userCode, String officeCode) {
		
		Connection connection = null;
		CallableStatement cs= null;
		ResultSet rs = null;
	    String branchList = null;
		Map<String, String> values = new HashMap<String, String>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OP_USER_BRANCH_MAPPING (?, ?, ?, ?, ?)}");
			cs.setString(1, userCode.toUpperCase());
			cs.setString(2, officeCode);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();

			branchList = cs.getString(5);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return branchList;
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public List<DMSDocumentDetailsDTO> getOPDocumentDetailsData(String intimationNumber) {
		OPClaim opclaim = claimService.getOPClaimByIntimationNo(intimationNumber);		 
		Query query = entityManager.createNamedQuery("OPDocumentSummary.findByClaimKey");
		query = query.setParameter("claimKey", opclaim.getKey());

		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();

		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<OPDocumentSummary> documentDetailsList  = query.getResultList();

		if(documentDetailsList != null && !documentDetailsList.isEmpty()){
			for (OPDocumentSummary documentDetails : documentDetailsList) {
				entityManager.refresh(documentDetails);
			}
			documentDetailsDTOList = CreateRODMapper.getInstance().getOPDMSDocumentDetails(documentDetailsList);
		}

		List<DMSDocumentDetailsDTO> finalDMSDataList = new ArrayList<DMSDocumentDetailsDTO>();
		if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
		{
			for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
				documentDetails.setDmsRestApiURL(dmsAPIUrl);	
				/*if(null != documentDetails.getReimbursementNumber())				{
					documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());;
				} else {
					documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
				}*/

				if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName())){
					documentDetails.setFileName(documentDetails.getGalaxyFileName());
				}

				documentDetails.setDocumentType(SHAConstants.OTHERS);
				documentDetails.setDocumentSource("OP"); //SHAConstants.ROD_DOC_SOURCE
				documentDetails.setIntimationNo(intimationNumber);

				{
					finalDMSDataList.add(documentDetails);
				}
			}
		}
		return finalDMSDataList;
	}

	}