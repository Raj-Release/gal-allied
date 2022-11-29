package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsTableDTO;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploaddocumentsFormDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
@Stateless
public class PASearchUploadDocumentsService extends AbstractDAO<Reimbursement>{

	
	@PersistenceContext
	protected EntityManager entityManager;

	public PASearchUploadDocumentsService() {
		super();
	}

	public Page<SearchUploadDocumentsTableDTO> search(SearchUploaddocumentsFormDTO searchFormDTO, String userName,String passWord) {
		List<DocAcknowledgement> reimbursementList = new ArrayList<DocAcknowledgement>();
		try {
			String intimationNo = null != searchFormDTO && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() : null;
			String policyNo = null != searchFormDTO && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
			String acknowledgementNo = null != searchFormDTO && !searchFormDTO.getAckNo().isEmpty() ? searchFormDTO.getAckNo() : null;
			SelectValue selType = null;
			String strType = null;
			
			selType = searchFormDTO.getType();
			if (null != selType && null != selType.getValue()) {
				strType = selType.getValue();
			}

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<DocAcknowledgement> criteriaQuery = criteriaBuilder.createQuery(DocAcknowledgement.class);

			Root<DocAcknowledgement> root = criteriaQuery.from(DocAcknowledgement.class);
			

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(root.<Claim> get("claim").<Intimation> get("intimation").<String> get("intimationId"), "%"+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder.like(root.<Claim> get("claim").<Intimation> get("intimation").<Policy> get("policy").<String> get("policyNumber"), "%" + policyNo+ "%");
				conditionList.add(condition2);
			}
			if(null != acknowledgementNo)
			{
				Predicate condition3 = criteriaBuilder.like(root.<String>get("acknowledgeNumber"), "%" + acknowledgementNo+ "%");
				conditionList.add(condition3);
			}
			
			/*Predicate condition4 = criteriaBuilder.isNull(root.<Long>get("rodKey"));
			conditionList.add(condition4);*/
			
			Predicate condition5 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"),ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
			conditionList.add(condition5);
			
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		

			final TypedQuery<DocAcknowledgement> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			@SuppressWarnings("unused")
			int firtResult;
			
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 10;
			} 
			else {
				firtResult = 1;
			}
			
			reimbursementList = typedQuery.getResultList();
			
			List<DocAcknowledgement> doList = new ArrayList<DocAcknowledgement>();
			
			if(null != reimbursementList && !reimbursementList.isEmpty())
			{
				for (DocAcknowledgement docAck : reimbursementList) {
					
					if(null == docAck.getRodKey())
					{
						doList.add(docAck);
					}
					
				}
			}
					
		    

			List<SearchUploadDocumentsTableDTO> tableDTO = PASearchUploadDocumentsMapper.getInstance().getSeachOrUploadDocumentsTableObj(doList);

			for (SearchUploadDocumentsTableDTO searchUploadDocumentsTableDTO : tableDTO) {
				
				searchUploadDocumentsTableDTO.setUsername(userName);
			}
			
			tableDTO = getHospitalDetails(tableDTO);
		
			List<SearchUploadDocumentsTableDTO> result = new ArrayList<SearchUploadDocumentsTableDTO>();
			result.addAll(tableDTO);
			Page<SearchUploadDocumentsTableDTO> page = new Page<SearchUploadDocumentsTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;
	}

	@Override
	public Class<Reimbursement> getDTOClass() {
		// TODO Auto-generated method stub
		return Reimbursement.class;
	}

	

	private List<SearchUploadDocumentsTableDTO> getHospitalDetails(
			List<SearchUploadDocumentsTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {

			Query findByHospitalKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key",
					tableDTO.get(index).getHospitalNameID());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey
						.getSingleResult();
				if (hospitalDetail != null) {
					tableDTO.get(index).setHospitalName(
							hospitalDetail.getName());

					tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId() + " ,"+ hospitalDetail.getCity());
					tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					
					TmpCPUCode cpuCode = getTmpCPUCode(tableDTO.get(0).getCpuId());
					
					if(null != cpuCode){
						
						tableDTO.get(index).setCpuCode(cpuCode.getCpuCode().toString());
					}
					
					
				}
			} catch (Exception e) {
				continue;
			}

		}

		return tableDTO;
	}

	private TmpCPUCode getTmpCPUCode(Long cpuId) {
		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
		} catch (Exception e) {

		}
		return null;
	}

	
	private Reimbursement getType(Long key) {
		try {
			Query findType = entityManager.createNamedQuery(
					"Reimbursement.findByKey").setParameter("primaryKey", key);
			Reimbursement reimbursement = (Reimbursement) findType
					.getSingleResult();
			return reimbursement;
		} catch (Exception e) {

		}
		return null;
	}
	
	public String getDocAckByIntimationNo(String intimationNo)
	{
		String strMessage = null;
		Query query = entityManager.createNamedQuery("Claim.findByIntimationId");
		query = query.setParameter("intimationNumber", intimationNo);
		List<Claim> claimList = query.getResultList();
		if(null != claimList && !claimList.isEmpty())
		{
			entityManager.refresh(claimList.get(0));
			Claim claim = claimList.get(0);
			if(null != claim)
			{
				strMessage = getDocAcknowledgement(claim.getKey());
			}
			return strMessage;
		}
		else
		{
			strMessage = "<b><br>Invalid intimation number entered </br>. <br>Please provide a valid intimation number to proceed</br><b>";
		}
		return strMessage;
	}
	
	public String getDocAckByAckNo(String ackNo)
	{
		String strMessage = null;
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckNumber");
		query = query.setParameter("ackNo", ackNo);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			entityManager.refresh(docAckList.get(0));
			DocAcknowledgement docAck = docAckList.get(0);
			if(null != docAck.getRodKey())
			{
				strMessage =  "<b> <br>Acknowledgement is already mapped to ROD </br>.<br> Please use Add Additional Documents menu for uploading documents against the ROD created.</br></b>";

			}
		}
		else
		{
			strMessage = "<br> You have entered an invalid acknowledgement number.</br>. <br> Please provide a valid acknowledgement number to proceed further </br>";
		}
		return strMessage;
	}
	
	private String getDocAcknowledgement(Long claimKey)
	{
		String strMessage = null;
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckByClaim");
		query = query.setParameter("claimkey",claimKey);
		List<DocAcknowledgement> docAcknowledgementList = query.getResultList();
		if(null != docAcknowledgementList && !docAcknowledgementList.isEmpty())
		{
			List<String> errorList = new ArrayList<String>();
			for (DocAcknowledgement docAcknowledgement : docAcknowledgementList) {
				if(null != docAcknowledgement.getRodKey())
				{
					errorList.add(docAcknowledgement.getAcknowledgeNumber());
				}
			}
			
			if(null != errorList && !errorList.isEmpty())
			{
					
				strMessage =  "<b> <br>Acknowledgement is already mapped to ROD </br>.<br> Please use Add Additional Documents menu for uploading documents against the ROD created.</br></b>";
			}
		}
		else
		{
			strMessage = "No Acknowledgement is available for this Intimation. Please create an acknowledgement to proceed";
		}
		return strMessage;
	}
	
	public List<UploadDocumentDTO> getAckDocByDocAckKey(Long key)
    {
    	Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByDocAcknowledgementKey");
    	query = query.setParameter("docAckKey",key);
    	List<AcknowledgeDocument> ackDocList = query.getResultList();
    	List<UploadDocumentDTO> uploadDTOList = null;
    	if(null != ackDocList && !ackDocList.isEmpty())
    	{
    		uploadDTOList = new ArrayList<UploadDocumentDTO>();
    		for (AcknowledgeDocument acknowledgeDocument : ackDocList) {
    			UploadDocumentDTO uploadDocDTO = new UploadDocumentDTO();
    			if(null != acknowledgeDocument.getFileType())
    			{
    				SelectValue selValue = new SelectValue();
    				selValue.setId(acknowledgeDocument.getFileType().getKey());
    				selValue.setValue(acknowledgeDocument.getFileType().getValue());
    				uploadDocDTO.setFileType(selValue);
    			}
    			uploadDocDTO.setAckDocKey(acknowledgeDocument.getKey());
    			uploadDocDTO.setFileName(acknowledgeDocument.getFileName());
    			uploadDTOList.add(uploadDocDTO);
			}
    		return uploadDTOList;
    	}
    	return null;
    }




}
