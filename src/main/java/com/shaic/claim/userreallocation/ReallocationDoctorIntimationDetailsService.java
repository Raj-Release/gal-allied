package com.shaic.claim.userreallocation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.AutoAllocationDetails;
import com.shaic.domain.MasUser;
import com.shaic.domain.preauth.CashlessWorkFlow;

@Stateless
public class ReallocationDoctorIntimationDetailsService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(ReallocationDoctorIntimationDetailsService.class);

	public ReallocationDoctorIntimationDetailsService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<AutoAllocationDetailsTableDTO> getAssignedDetails(String doctor) {

		String doctorId = doctor.toLowerCase();

		/*List<String> statusList = new ArrayList<String>();
		statusList.add(SHAConstants.REALLOCATION_COMPLETED_STATUS);
		statusList.add(SHAConstants.PENDING);*/
		List<AutoAllocationDetailsTableDTO> list = null;
		try{
			String query = "SELECT * FROM IMS_CLS_AUTOALLOCATION_DTLS m where Lower(m.DOCTOR_ID) = '"+doctorId+"' and m.STATUS_FLAG IN ('C','P') and m.ACTIVE_STATUS = 1 and to_char(m.ASSIGNED_DATE_TIME,'yyyymmdd') = to_char(sysdate,'yyyymmdd') and m.AUTO_DTLS_KEY in (SELECT MAX(n.AUTO_DTLS_KEY) from IMS_CLS_AUTOALLOCATION_DTLS n where Lower(n.DOCTOR_ID) = '"+doctorId+"' GROUP BY n.INTIMATION_NUMBER)  order by m.ASSIGNED_DATE_TIME asc";
			Query nativeQuery = entityManager.createNativeQuery(query);
			
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			list = new ArrayList<AutoAllocationDetailsTableDTO>();
			AutoAllocationDetailsTableDTO tableDto = null;
			for (Object[] obj : objList) {
				tableDto = new AutoAllocationDetailsTableDTO();
				
				tableDto.setIntimationNo((String)obj[1]);
				tableDto.setDoctorId((String)obj[3]);
				tableDto.setDoctorName((String)obj[4]);
				
				BigDecimal claimedAmt = (BigDecimal)obj[5];
				tableDto.setClaimedAmt(claimedAmt.doubleValue());
				
				BigDecimal cpuCode = (BigDecimal)obj[6];
				tableDto.setCpu(cpuCode.longValue());
				
				Timestamp assigned = (Timestamp) obj[7];
				tableDto.setAssignedDate(assigned.toString());
				
				if(obj[8] != null){
					Timestamp completed = (Timestamp) obj[8];
					if(completed != null){
						tableDto.setCompletedDate(completed.toString());	
					}
				}
				
				tableDto.setsNumber(objList.indexOf(obj)+1);
				
				list.add(tableDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		/*Query findAll = entityManager.createNamedQuery(
				"AutoAllocationDetails.findAssignedByDoctor").setParameter("doctorId",
				doctorId).setParameter("statusList", statusList);
		List<AutoAllocationDetails> assignedDetails = (List<AutoAllocationDetails>) findAll
				.getResultList();*/

		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<AutoAllocationDetailsTableDTO> getCompletedAssignedDetails(String doctor, String statusFlag) {

		String doctorId = doctor.toLowerCase();

		/*Query findAll = entityManager.createNamedQuery(
				"AutoAllocationDetails.findStatusByDoctor").setParameter("doctorId",
						doctorId).setParameter("status",statusFlag);
		List<AutoAllocationDetails> details = (List<AutoAllocationDetails>) findAll
				.getResultList();*/
		
		List<AutoAllocationDetailsTableDTO> list = null;
		try{
			String query = "SELECT * FROM IMS_CLS_AUTOALLOCATION_DTLS m where Lower(m.DOCTOR_ID) = '"+doctorId+"' and m.STATUS_FLAG ='"+statusFlag+"' and m.ACTIVE_STATUS = 1 and to_char(m.ASSIGNED_DATE_TIME,'yyyymmdd') = to_char(sysdate,'yyyymmdd') and m.AUTO_DTLS_KEY in (SELECT MAX(n.AUTO_DTLS_KEY) from IMS_CLS_AUTOALLOCATION_DTLS n where Lower(n.DOCTOR_ID) = '"+doctorId+"' GROUP BY n.INTIMATION_NUMBER)  order by m.ASSIGNED_DATE_TIME asc";
			Query nativeQuery = entityManager.createNativeQuery(query);
			
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			list = new ArrayList<AutoAllocationDetailsTableDTO>();
			AutoAllocationDetailsTableDTO tableDto = null;
			for (Object[] obj : objList) {
				tableDto = new AutoAllocationDetailsTableDTO();
				
				tableDto.setIntimationNo((String)obj[1]);
				tableDto.setDoctorId((String)obj[3]);
				tableDto.setDoctorName((String)obj[4]);
				
				BigDecimal claimedAmt = (BigDecimal)obj[5];
				tableDto.setClaimedAmt(claimedAmt.doubleValue());
				
				BigDecimal cpuCode = (BigDecimal)obj[6];
				tableDto.setCpu(cpuCode.longValue());
				
				Timestamp assigned = (Timestamp) obj[7];
				tableDto.setAssignedDate(assigned.toString());
				
				if(obj[8] != null){
					Timestamp completed = (Timestamp) obj[8];
					if(completed != null){
						tableDto.setCompletedDate(completed.toString());
					}
				}
				
				tableDto.setsNumber(objList.indexOf(obj)+1);
				
				list.add(tableDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}
	
	private MasUser getUser(Long reAllocateToId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getByKey").setParameter("userId", reAllocateToId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}
	
	public AutoAllocationDetails getIntimation(String intimationNo) {
		AutoAllocationDetails userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"AutoAllocationDetails.findByIntimation").setParameter("intimationNo", intimationNo).setParameter("status",SHAConstants.PENDING);
		try {
			userDetail = (AutoAllocationDetails) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}
	}
	
	public AutoAllocationDetails getDoctorByIntimation(String intimationNo, String doctor) {
		
		String doctorId = doctor.toLowerCase();
		
		AutoAllocationDetails userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"AutoAllocationDetails.findByIntimationDoctor").setParameter("intimationNo", intimationNo).setParameter("doctorId",
						doctorId).setParameter("status",SHAConstants.PENDING);
		try {
			userDetail = (AutoAllocationDetails) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}
	}
	
	public CashlessWorkFlow getWorkFlowByIntimation(String intimationNo){
	
		CashlessWorkFlow workFlow;
		
		List<String> currentList = new ArrayList<String>();
		currentList.add(SHAConstants.PE_CURRENT_QUEUE);
		currentList.add(SHAConstants.PP_CURRENT_QUEUE);
		
		Query findByIntimation = entityManager.createNamedQuery(
				"CashlessWorkFlow.findIntimationReallocate").setParameter("intimationNo",
						intimationNo).setParameter("current", currentList);
		try {
			workFlow = (CashlessWorkFlow) findByIntimation.getSingleResult();
			return workFlow;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	public Boolean updateAutoAllocationDetails(
			List<AutoAllocationDetailsTableDTO> list, String user) {

		Boolean value = false;
		if (list != null && !list.isEmpty()) {
			for (AutoAllocationDetailsTableDTO dto : list) {

				if (dto.getChkSelect()) {

					AutoAllocationDetails details = getDoctorByIntimation(
							dto.getIntimationNo(), dto.getDoctorId());
					CashlessWorkFlow workFlow = getWorkFlowByIntimation(dto
							.getIntimationNo());

					if (details != null && workFlow != null) {
						details.setDoctorId(null);
						details.setDoctorName(null);
						details.setStatusFlag(null);
						details.setModifiedBy(user);
						details.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(details);
						entityManager.flush();
						entityManager.clear();

						workFlow.setAllocateUser(null);
						workFlow.setAllocateDate(null);

						entityManager.merge(workFlow);
						entityManager.flush();
						entityManager.clear();

						value = true;
					}

				} else if (dto.getReAllocateTo() != null) {

					MasUser masUser = getUser(dto.getReAllocateTo());

					if (masUser != null) {

						AutoAllocationDetails details = getDoctorByIntimation(
								dto.getIntimationNo(), dto.getDoctorId());
						CashlessWorkFlow workFlow = getWorkFlowByIntimation(dto
								.getIntimationNo());

						if (details != null && workFlow != null) {
							details.setDoctorId(masUser.getUserId());
							details.setDoctorName(masUser.getUserName());
							details.setStatusFlag(SHAConstants.PENDING);
							details.setModifiedBy(user);
							details.setModifiedDate(new Timestamp(System
									.currentTimeMillis()));
							entityManager.merge(details);
							entityManager.flush();
							entityManager.clear();

							workFlow.setAllocateUser(masUser.getUserId());
							workFlow.setAllocateDate(new Timestamp(System
									.currentTimeMillis()));
							entityManager.merge(workFlow);
							entityManager.flush();
							entityManager.clear();

							value = true;
						}
					}
				}

			}
		}
		return value;
	}
	
}
