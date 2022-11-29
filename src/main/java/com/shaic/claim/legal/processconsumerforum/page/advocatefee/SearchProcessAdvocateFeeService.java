package com.shaic.claim.legal.processconsumerforum.page.advocatefee;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.CaseDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.domain.LegalAdvocateFee;
import com.shaic.domain.LegalConsumer;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpCPUCode;

@Stateless
public class SearchProcessAdvocateFeeService {


	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService masterService;

	public void saveLegalAdvocateFee(AdvocateFeeDTO advocateFeeDTO) {
		
		LegalAdvocateFee legalAdvocateFee = new LegalAdvocateFee(); 
		/*if(advocateFeeDTO.getIntimationDetailsDTO()!=null && advocateFeeDTO.getIntimationDetailsDTO().getIntimationNo()!=null){
			legalAdvocateFee = getLegalByIntimationNumberAndType(advocateFeeDTO.getIntimationDetailsDTO().getIntimationNo(),SHAConstants.LEGAL_ADVOCATE_FEE);
		}*/
		/*if(legalAdvocateFee == null){
			legalAdvocateFee = new LegalAdvocateFee();
		}*/
		IntimationDetailsDTO intimationDetailsDTO = advocateFeeDTO.getIntimationDetailsDTO();
		if(intimationDetailsDTO!=null){
			saveIntimationValues(intimationDetailsDTO, legalAdvocateFee);
			if(intimationDetailsDTO.getClaimKey()!=null){
				legalAdvocateFee.setClaimKey(intimationDetailsDTO.getClaimKey());
			}
		}

		//legalAdvocate.setL
		
		CaseDetailsDTO caseDetailsDTO = advocateFeeDTO.getCaseDetailsDTO();
		if(caseDetailsDTO!=null){
			legalAdvocateFee.setAdvocateName(caseDetailsDTO.getAdVocateName());
			legalAdvocateFee.setDcdrfRemarks(caseDetailsDTO.getDcdrf());
			legalAdvocateFee.setCcNo(caseDetailsDTO.getCcNo());
			if(caseDetailsDTO.getZone()!=null){
				Long id = caseDetailsDTO.getZone().getId();
				TmpCPUCode cpuCode = new TmpCPUCode();
				cpuCode.setKey(id);
				//MastersValue zone = masterService.getMaster(caseDetailsDTO.getZone().getId());
				legalAdvocateFee.setZoneId(cpuCode);
			}
		}
		legalAdvocateFee.setAdvocateFee(advocateFeeDTO.getAdvocateFee());
		legalAdvocateFee.setPaidAmount(advocateFeeDTO.getAmtPaid());
		legalAdvocateFee.setDdName(advocateFeeDTO.getDdName());
		legalAdvocateFee.setPartPaymentFlag(advocateFeeDTO.getIsPartPayment());
		legalAdvocateFee.setFullPaymentFlag(advocateFeeDTO.getIsFullPayment());
		legalAdvocateFee.setCaseAdvocateName(advocateFeeDTO.getAdvocateName());
		
		legalAdvocateFee.setLegalType(SHAConstants.LEGAL_ADVOCATE_FEE);
		
		legalAdvocateFee.setCreatedBy(advocateFeeDTO.getUserName());
		legalAdvocateFee.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(legalAdvocateFee);
		
		/*if(legalAdvocateFee.getKey()!=null){
			legalAdvocateFee.setModifiedBy(advocateFeeDTO.getUserName());
			legalAdvocateFee.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(legalAdvocateFee);
		}else{
			legalAdvocateFee.setCreatedBy(advocateFeeDTO.getUserName());
			legalAdvocateFee.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(legalAdvocateFee);
		}*/
		entityManager.flush();
		//entityManager.merge(legal);
		//entityManager.flush();
	}

	private LegalAdvocateFee saveIntimationValues(IntimationDetailsDTO intimationDetailsDTO,
			LegalAdvocateFee legalAdvocate) {
		legalAdvocate.setIntimationNumber(intimationDetailsDTO.getIntimationNo());
		legalAdvocate.setFinancialYear(intimationDetailsDTO.getFinancialYear());
		legalAdvocate.setPolicyNumber(intimationDetailsDTO.getPolicyNo());
		legalAdvocate.setProvisionAmount(intimationDetailsDTO.getProvisionAmt());
		legalAdvocate.setProductName(intimationDetailsDTO.getProductNo());
		legalAdvocate.setInsuredName(intimationDetailsDTO.getInsuredName());
		if(intimationDetailsDTO.getRepudiation()!=null){
			MastersValue repudiation = masterService.getMaster(intimationDetailsDTO.getRepudiation().getId());
			legalAdvocate.setRepudiationId(repudiation);
		}
		return legalAdvocate;
	}

	public LegalAdvocateFee getLegalByIntimationNumberAndType(String intimationNumber,String legalType) {
		List<LegalAdvocateFee> resultLegal = null;
		Query findByIntimationNum = entityManager.createNamedQuery("LegalAdvocateFee.findByIntimationNumberAndType");
		findByIntimationNum.setParameter("intimationNumber", intimationNumber);		
		findByIntimationNum.setParameter("legalType", legalType);

		try {
			resultLegal = (List<LegalAdvocateFee>) findByIntimationNum.getResultList();
			
			if(resultLegal != null && !resultLegal.isEmpty()){
				return resultLegal.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public LegalConsumer getConsumerByIntimationNumber(String intimationNumber,String legalType) {
		List<LegalConsumer> resultLegal = null;
		Query findByIntimationNum = entityManager.createNamedQuery("LegalConsumer.findByIntimationNumberAndType");
		findByIntimationNum.setParameter("intimationNumber", intimationNumber);		
		findByIntimationNum.setParameter("legalType", legalType);

		try {
			resultLegal = (List<LegalConsumer>) findByIntimationNum.getResultList();
			
			if(resultLegal != null && !resultLegal.isEmpty()){
				return resultLegal.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
