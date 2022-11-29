package com.shaic.claim.medical.opinion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.uprSearch.ClmPaymentCancelDto;
import com.shaic.claim.pcc.dto.ViewPCCTrailsDTO;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.Intimation;
import com.shaic.domain.RecMarkEscalation;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.OptionGroup;

@Stateless
public class RecMarketingEscalationService extends AbstractDAO<RecMarkEscalation> {

	@PersistenceContext
	protected EntityManager entityManager;


	public RecMarketingEscalationService() {

	}
	
	public enum Outcome {IPEDIPSA}

	@SuppressWarnings("unchecked")
	public Page<RecordMarkEscDTO> search(Long key) {
		System.out.println("Called one----------------------------------->");
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		try {
			final CriteriaQuery<RecMarkEscalation> criteriaQuery = builder
					.createQuery(RecMarkEscalation.class);

			Root<RecMarkEscalation> searchRoot = criteriaQuery
					.from(RecMarkEscalation.class);

			Predicate predicates = builder.equal(searchRoot.get("key"), key);

			criteriaQuery.select(searchRoot).where(builder.and(predicates));

			final TypedQuery<RecMarkEscalation> oldInitiatePedQuery = entityManager
					.createQuery(criteriaQuery);

			RecMarkEscalation result = oldInitiatePedQuery
					.getSingleResult();
			
			Predicate preauthQuery = null;

			criteriaQuery.select(searchRoot).where(builder.and(preauthQuery));

			final TypedQuery<RecMarkEscalation> recMarkEscalationList = entityManager
					.createQuery(criteriaQuery);

			List<RecMarkEscalation> resultList = recMarkEscalationList
					.getResultList();

			final List<RecordMarkEscDTO> resultDto = new ArrayList<RecordMarkEscDTO>();

			for (int i = 0; i < resultList.size(); i++) {
				RecordMarkEscDTO details = new RecordMarkEscDTO();

				details.setEscalatedRole(resultList.get(i).getEscalatedRole());
				details.setEscalatedBy(resultList.get(i).getEscalatedBy());
				details.setActionTaken(resultList.get(i).getActionTaken());
				details.setEscalationReason(resultList.get(i).getEscalationReason());
				details.setDoctorRemarks(resultList.get(i).getDoctorRemarks());
				details.setEscalatedDate(resultList.get(i).getEscalatedDate());
				details.setCreatedDate(resultList.get(i).getCreatedDate());
				details.setModifiedDate(resultList.get(i).getModifiedDate());
				details.setModifiedBy(resultList.get(i).getModifiedBy());
				details.setCreatedBy(resultList.get(i).getCreatedBy());
				details.setStageId(resultList.get(i).getStageId());
				
				details.setStatusId(resultList.get(i).getStatusId());
				details.setIntimationNumber(resultList.get(i).getIntimationNumber());
				details.setRefNumber(resultList.get(i).getRefNumber());
				details.setEscalationRemarks(resultList.get(i).getEscalationRemarks());
				
                
				resultDto.add(details);
			}

			for (int i = 0; i < resultDto.size(); i++) {
				OptionGroup radio = new OptionGroup();
				radio.addItems("");
				radio.addValueChangeListener(new ValueChangeListener() {

					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(
							com.vaadin.v7.data.Property.ValueChangeEvent event) {

					}
				});
				//resultDto.get(i).setSelect(radio);

			}

			Page<RecordMarkEscDTO> page = new Page<RecordMarkEscDTO>();
			page.setPageNumber(1);
			page.setPageItems(resultDto);
			// page.setPagesAvailable(pagedList.getPagesAvailable());
			if (page != null) {

				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	

	public Boolean sumbitRecMarkEsc(RecordMarkEscDTO bean) {
		
		String strUserName = bean.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		RecMarkEscalation resultData = RecMarkEscalationMapper.getInstance()
				.getRecMarkEscalation(bean);

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<RecMarkEscalation> criteriaQuery = builder
				.createQuery(RecMarkEscalation.class);

		Root<RecMarkEscalation> searchRoot = criteriaQuery
				.from(RecMarkEscalation.class);

		Predicate predicates = builder.equal(searchRoot.<Long> get("key"),
				resultData.getKey());

		criteriaQuery.select(searchRoot).where(builder.and(predicates));

		final TypedQuery<RecMarkEscalation> recMarkEscalationQuery = entityManager
				.createQuery(criteriaQuery);

		RecMarkEscalation existingList = new RecMarkEscalation();
			Long stageId=58l;
			Long statusId= 3233l;
			existingList.setEscalatedRole(resultData.getEscalatedRole());
			existingList.setEscalatedBy(resultData.getEscalatedBy());
			existingList.setActionTaken(resultData.getActionTaken());
			existingList.setEscalationReason(resultData.getEscalationReason());
			existingList.setDoctorRemarks(resultData.getDoctorRemarks());
			existingList.setEscalatedDate(resultData.getEscalatedDate());
			existingList.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			//existingList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			//existingList.setModifiedBy(strUserName);
			existingList.setCreatedBy(strUserName);
			existingList.setStageId(stageId);
			
			existingList.setStatusId(statusId);
			existingList.setIntimationNumber(resultData.getIntimationNumber());
			
			existingList.setEscalationRemarks(resultData.getEscalationRemarks());
			existingList.setMrkPersonnel(bean.getMrkPersonnelFlag());
			
			int count = getRecordMarkEscCount(resultData.getIntimationNumber());
			if(count < 9)
			{
				existingList.setRefNumber(resultData.getIntimationNumber()+"/00"+(count+1));
			}
			else if(count >= 9 && count < 99 )
			{
				existingList.setRefNumber(resultData.getIntimationNumber()+"/0"+(count+1));
			}
			else if(count >= 99 )
			{
				existingList.setRefNumber(resultData.getIntimationNumber()+"/"+(count+1));
			}

			entityManager.persist(existingList);
			entityManager.flush();
			
		//setDBOutcomeForRecMarkEsc(bean, SHAConstants.PED_PROCESS_PED_QUERY_REPLY_PROCESSOR);
			return true;
	}

	
	public void setDBOutcomeForRecMarkEsc(RecordMarkEscDTO resultTask, String outCome) {
		
		/*Map<String, Object> wrkFlowMap = (Map<String, Object>) resultTask.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);*/
				/*DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);*/
				System.out.println("Submit Executed Successfully");
		 }



	@Override
	public Class<RecMarkEscalation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Integer getRecordMarkEscCount(String intimationNumber){

		Query findEsccount = entityManager.createNamedQuery(
				"RecMarkEscalation.findRecMarkEscCountByIntimationNo");
		findEsccount.setParameter("intimationNumber", intimationNumber);
		List<RecMarkEscalation> result =  findEsccount.getResultList();
		if(result !=null && !result.isEmpty()){
			return result.size();
		}
		return 0;
	}
	
	public List<RecMarkEscalation> getRecordMarkEscHistory(String intimationNumber){
		List<RecMarkEscalation> recMarkEscList = new ArrayList<RecMarkEscalation>();
		Query findEsccount = entityManager.createNamedQuery(
				"RecMarkEscalation.findRecMarkEscCountByIntimationNo");
		findEsccount.setParameter("intimationNumber", intimationNumber);


		try {
			recMarkEscList =findEsccount.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recMarkEscList;
	}
	
	public Intimation searchbyIntimationNo(String intimationNo) {
		Intimation intimation = null;
		if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

			Query findByIntimNo = entityManager.createNamedQuery(
					"Intimation.findByIntimationNumber").setParameter(
					"intimationNo", intimationNo);
			try {

				List<Intimation> intimationQueryResultList = (List<Intimation>) findByIntimNo.getResultList();
				if (intimationQueryResultList != null
						&& !intimationQueryResultList.isEmpty()) {
					intimation = (Intimation) intimationQueryResultList.get(0);
					entityManager.refresh(intimation);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intimation;
	}
	
	public String getActionTaken(Long actionTakenKey) {
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT ACTION_TAKEN_DESC FROM MAS_MKT_ACTION_MAPPING WHERE MKT_ACTION_KEY=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, actionTakenKey);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getString(1);
						}
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public String getEscalationReason(Long escReasonKey) {
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MASTER_VALUE FROM MAS_REFERENCE_VALUE WHERE MASTER_KEY=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, escReasonKey);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getString(1);
						}
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public List<ViewMarkEscHistoryDTO> getMarkEscHistoryDTO(String intimationNo){
		ViewMarkEscHistoryDTO resultList = null;
		
		Intimation intimation = searchbyIntimationNo(intimationNo);
		List<RecMarkEscalation> recMarkEscalation = getRecordMarkEscHistory(intimationNo);
		
		List<ViewMarkEscHistoryDTO> result = new ArrayList<ViewMarkEscHistoryDTO>();
		ViewMarkEscHistoryDTO paymentCancelDto = null;
		int sNo = 0;
			for (RecMarkEscalation tempRecMarkEscalation : recMarkEscalation) {
				resultList = new ViewMarkEscHistoryDTO();
				sNo++;
				resultList.setsNo(sNo);
				resultList.setIntimationNumber(tempRecMarkEscalation.getIntimationNumber());
				resultList.setEscalatedRole(tempRecMarkEscalation.getEscalatedRole());
				resultList.setEscalatedBy(tempRecMarkEscalation.getEscalatedBy());
				
				SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
				resultList.setDateAndTime(dt1.format(intimation.getCreatedDate()));
				
				resultList.setActionTaken(getActionTaken(tempRecMarkEscalation.getActionTaken()));
				resultList.setEscalationReason(getEscalationReason(tempRecMarkEscalation.getEscalationReason()));
				resultList.setDoctorRemarks(tempRecMarkEscalation.getDoctorRemarks());
				
				SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy");
				resultList.setEscalatedDate(dt.format(tempRecMarkEscalation.getEscalatedDate()));
				
				if(tempRecMarkEscalation.getCreatedBy() != null){
					TmpEmployee employeeName = getEmployeeName(tempRecMarkEscalation.getCreatedBy());
					if(employeeName != null){
						resultList.setUserName(tempRecMarkEscalation.getCreatedBy() +" - "+employeeName.getEmpFirstName());
					}
				}
				
				resultList.setDoctorRemarks(tempRecMarkEscalation.getDoctorRemarks());
				resultList.setLackOfmrkPersonnel(tempRecMarkEscalation.getMrkPersonnel());
				result.add(resultList);
			}
				
		return result;
	}
	
	private TmpEmployee getEmployeeName(String initiatorId)
	{
	  TmpEmployee fvrInitiatorDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
		try{
			fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
			return fvrInitiatorDetail;
		}
		catch(Exception e)
		{
			return null;
		}
							
	}

}


