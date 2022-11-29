package com.shaic.claim.OMPBulkUploadRejection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsMapper;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.HospitalCommunicationUpdateMapper;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class OMPBulkUploadRejectionService {

	@PersistenceContext
	protected EntityManager entityManager;

	public void persistOmpRejectionBulkUploadList(
			List<OmpRejectionBulkUpload> ompRejectionBulkUploadList) {

		 for (Iterator<OmpRejectionBulkUpload> it = ompRejectionBulkUploadList.iterator(); it.hasNext();) {
			 OmpRejectionBulkUpload bulkUploadEntity = it.next();

			 entityManager.persist(bulkUploadEntity);
			 entityManager.flush();
			 entityManager.clear();
	        }

	}

	public List<OMPBulkUploadRejectionResultDto> getOmpBulkUploadRejectionDetails(
			WeakHashMap<String, Object> searchFilter,
			DBCalculationService dbclaService) throws ParseException {
		
		String rodNumber = null;
		String podNumber = null;
		Date fromDate = null;
		Date toDate = null;
		String uploadType = null;
		String uploadTypeKey = null;

		Query query = null;
		if(searchFilter.containsKey("rodNumber") && searchFilter.get("rodNumber") != null){
			rodNumber = (String)searchFilter.get("rodNumber");
		}
		if(searchFilter.containsKey("podNumber") && searchFilter.get("podNumber") != null){
			podNumber = (String)searchFilter.get("podNumber");
		}
		if(searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null && searchFilter.containsKey("toDate") && searchFilter.get("toDate") != null){
			fromDate = (Date)searchFilter.get("fromDate");
			toDate = (Date)searchFilter.get("toDate");
		}
		if(searchFilter.containsKey("uploadType") && searchFilter.get("uploadType") != null){
			SelectValue selectValue = (SelectValue) searchFilter.get("uploadType");
			uploadTypeKey = selectValue.getValue();
			if(uploadTypeKey.equalsIgnoreCase("discharge voucher")) {
				uploadType = "D";
			} else if(uploadTypeKey.equalsIgnoreCase("rejection flow")) {
				uploadType = "R";
			}
		}
		
		if(fromDate != null && toDate != null) {
			if(rodNumber != null && podNumber != null && rodNumber != "" && podNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByPodAndRodNumberWithDate");
				query.setParameter("rodNumber", rodNumber);
				query.setParameter("podNumber", podNumber);
				query.setParameter("fromDate", fromDate);
				query.setParameter("toDate", toDate);
				query.setParameter("uploadType", uploadType);
			}else if(rodNumber != null && rodNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByRodNumberWithDate");
				query.setParameter("rodNumber", rodNumber);
				query.setParameter("fromDate", fromDate);
				query.setParameter("toDate", toDate);
				query.setParameter("uploadType", uploadType);
			}else if(podNumber != null && podNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByPodNumberWithDate");
				query.setParameter("podNumber", podNumber);
				query.setParameter("fromDate", fromDate);
				query.setParameter("toDate", toDate);
				query.setParameter("uploadType", uploadType);
			}else {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByDate");
				query.setParameter("fromDate", fromDate);
				query.setParameter("toDate", toDate);
				query.setParameter("uploadType", uploadType);
			}
			
		} else {
			if(rodNumber != null && podNumber != null && rodNumber != "" && podNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByPodAndRodNumber");
				query.setParameter("rodNumber", rodNumber);
				query.setParameter("podNumber", podNumber);
				query.setParameter("uploadType", uploadType);
			}else if(rodNumber != null && rodNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByRodNumber");
				query.setParameter("rodNumber", rodNumber);
				query.setParameter("uploadType", uploadType);
			}else if(podNumber != null && podNumber != "" && uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByPodNumber");
				query.setParameter("podNumber", podNumber);
				query.setParameter("uploadType", uploadType);
			} else if(uploadType != null) {
				query = entityManager.createNamedQuery("OmpRejectionBulkUpload.findByUploadType");
				query.setParameter("uploadType", uploadType);
			}
		}
		List<OmpRejectionBulkUpload> bulkUploadRejectionList = new ArrayList<OmpRejectionBulkUpload>();
		if(query != null) {
			bulkUploadRejectionList = (List<OmpRejectionBulkUpload>) query.getResultList();
		}
		
		List<OMPBulkUploadRejectionResultDto> resultDtoList = new ArrayList<OMPBulkUploadRejectionResultDto>();
		if(bulkUploadRejectionList != null && !bulkUploadRejectionList.isEmpty()) {
			for(OmpRejectionBulkUpload ompEntity : bulkUploadRejectionList) {
				OMPBulkUploadRejectionResultDto rejectionResultDto = new OMPBulkUploadRejectionResultDto();
				rejectionResultDto.setActiveStatus(ompEntity.getActiveStatus());
				rejectionResultDto.setCreatedBy(ompEntity.getCreatedBy());
				rejectionResultDto.setCreatedDate(ompEntity.getCreatedDate());
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				rejectionResultDto.setDateOfDispatch(df.format(ompEntity.getDateOfDispatch()));
				rejectionResultDto.setKey(ompEntity.getKey());
				rejectionResultDto.setModeOfDispatch(ompEntity.getModeOfDispatch());
				rejectionResultDto.setModifiedBy(ompEntity.getModifiedBy());
				rejectionResultDto.setModifiedDate(ompEntity.getModifiedDate());
				rejectionResultDto.setPodNumber(ompEntity.getPodNumber());
				rejectionResultDto.setRemarks(ompEntity.getRemarks());
				rejectionResultDto.setRodNumber(ompEntity.getRodNumber());
				resultDtoList.add(rejectionResultDto);
			}
		}
		
		return resultDtoList;
	}

	
}
