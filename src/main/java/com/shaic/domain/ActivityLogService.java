package com.shaic.domain;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.claim.activitylog.ActivityLogFormDto;
import com.shaic.claim.activitylog.ActivityTableDto;
import com.shaic.claim.activitylog.DoctorActivity;

@Stateless
public class ActivityLogService {

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * Constructor
	 */
	public ActivityLogService() {
		super();
	}

	/**
	 * Get activity log details by activity key 
	 * @param activityKey
	 * @return activityLog
	 */
	public DoctorActivity getActivityLogByKey(Long activityKey) {
		if (activityKey != null) {
			return entityManager.find(DoctorActivity.class, activityKey);
		}
		return null;
	}
	
	/**
	 * Get activity log details by intimation number 
	 * @param intimationNo
	 * @return
	 */
	public List<DoctorActivity> getActivityLogByIntimationNo(String intimationNo) {
		Query query = entityManager.createNamedQuery("DoctorActivity.findByIntimationNo");
		query.setParameter("intimationNo", intimationNo);

		@SuppressWarnings("unchecked")
		List<DoctorActivity> activityLogList = (List<DoctorActivity>) query.getResultList();
		return activityLogList;
	}
	
	/**
	 * Save doctor activity log details
	 * @param activityLogFormDto
	 * @param tableDtoList
	 * @return
	 */
	public Boolean saveDoctorActivity(ActivityLogFormDto activityLogFormDto, List<ActivityTableDto> tableDtoList) {
		Boolean isSuccess = Boolean.FALSE;
		DoctorActivity doctorActivity;
		try {
			for(ActivityTableDto activityTableDto : tableDtoList) {
				doctorActivity = new DoctorActivity();	
				doctorActivity.setActivityUserId(activityLogFormDto.getEmpId());
				doctorActivity.setActivityUserName(activityLogFormDto.getEmpName());
				doctorActivity.setIntimationNo(activityLogFormDto.getIntimationNo());
				doctorActivity.setActivityDate(activityLogFormDto.getActivityDate());
				doctorActivity.setActivityName(activityTableDto.getActivityName().getValue());
				doctorActivity.setActivityDesc(activityTableDto.getActivityDesc());
				doctorActivity.setCreatedBy(activityLogFormDto.getEmpId());
				doctorActivity.setCreatedDate(new Date());
				doctorActivity.setActiveStatus(1);
				
				entityManager.persist(doctorActivity);
				entityManager.flush();
				isSuccess = Boolean.TRUE;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isSuccess;		
	}
	
	public Boolean updateDoctorActivity(DoctorActivity doctorActivity) {
		Boolean isSuccess = Boolean.FALSE;
		try {
			entityManager.merge(doctorActivity);
			entityManager.flush();
			isSuccess = Boolean.TRUE;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isSuccess;		
	}
}