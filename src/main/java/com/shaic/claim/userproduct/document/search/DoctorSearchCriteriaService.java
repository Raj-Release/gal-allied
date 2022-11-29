package com.shaic.claim.userproduct.document.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.shaic.domain.MasUser;

@Stateless
public class DoctorSearchCriteriaService {

	
	public DoctorSearchCriteriaService() {
		super();
	}
	

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	
	public  List<SearchDoctorDetailsTableDTO> search() {
		
		List<MasUser> listOfClaim = new ArrayList<MasUser>(); 
		try{
			
			List<MasUser> fvrInitiatorDetail = getDoctorDetailsBasedOnLimit();
			listOfClaim.addAll(fvrInitiatorDetail);
			
		List<SearchDoctorDetailsTableDTO> tableDTO = SearchDoctorDetailsMapper.getClaimDTO(listOfClaim);
		
		return tableDTO;
	
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}
	
	/*public  List<SearchDoctorDetailsTableDTO> search(String argName) {
		List<MasUser> listOfClaim = new ArrayList<MasUser>(); 
		try{
			List<MasUser> fvrInitiatorDetail = getDoctorDetails(argName);
			listOfClaim.addAll(fvrInitiatorDetail);

			List<SearchDoctorDetailsTableDTO> tableDTO = SearchDoctorDetailsMapper.getClaimDTO(listOfClaim);
			return tableDTO;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<MasUser> getDoctorDetails(String argDoctorsName){
		String query = "";
		if(StringUtils.isBlank(argDoctorsName)){
			query = "SELECT Distinct m.* FROM MAS_SEC_USER m INNER JOIN MAS_SEC_USER_LIMIT_MAPPING n ON m.USER_ID = n.USER_ID WHERE m.ACTIVE_STATUS IS NOT NULL AND m.ACTIVE_STATUS <> 'N'";
		}else{
			query = "SELECT Distinct m.* FROM MAS_SEC_USER m INNER JOIN MAS_SEC_USER_LIMIT_MAPPING n ON m.USER_ID = n.USER_ID WHERE m.USER_NAME like '%"+argDoctorsName+"%' AND m.ACTIVE_STATUS IS NOT NULL AND m.ACTIVE_STATUS <> 'N'";
		}
		Query nativeQuery = entityManager.createNativeQuery(query);

		List<Object[]> objList = (List<Object[]>) nativeQuery.getResultList();
		List<MasUser> list = new ArrayList<MasUser>();
		for (Object[] obj : objList) {
			MasUser user = new MasUser();
			user.setUserId((String)obj[1]);
			user.setEmpId((String)obj[2]);
			user.setUserName((String)obj[3]);
			user.setUserType((String)obj[4]);
			BigDecimal min = (BigDecimal)obj[5];
			user.setMinAmt(min.longValue());
			BigDecimal max = (BigDecimal)obj[6];
			user.setMaxAmt(max.longValue());
			user.setActiveStatus((String)obj[11]);

			list.add(user);
		}
		return list;
	}*/
	
	@SuppressWarnings("unchecked")
	private List<MasUser> getDoctorDetailsBasedOnLimit(){
		String query = "select distinct m.* from MAS_SEC_USER m inner join MAS_SEC_USER_LIMIT_MAPPING n on m.USER_ID = n.USER_ID where m.ACTIVE_STATUS is not null and m.ACTIVE_STATUS <> 'N'";
		Query nativeQuery = entityManager.createNativeQuery(query);
		
		List<Object[]> objList = (List<Object[]>) nativeQuery
				.getResultList();
		List<MasUser> list = new ArrayList<MasUser>();
		BigDecimal temp = new BigDecimal(0L);
		for (Object[] obj : objList) {
			MasUser user = new MasUser();
			user.setUserId((String)obj[1]);
			user.setEmpId((String)obj[2]);
			user.setUserName((String)obj[3]);
			user.setUserType((String)obj[4]);
			BigDecimal min = (BigDecimal)obj[5];
			if(min != null){
				user.setMinAmt(min.longValue());
			}else{
				user.setMinAmt(temp.longValue());
			}
			BigDecimal max = (BigDecimal)obj[6];
			if(max != null){
				user.setMaxAmt(max.longValue());
			}else{
				user.setMaxAmt(temp.longValue());
			}
			user.setActiveStatus((String)obj[11]);
			list.add(user);
		}
		
		return list;
	}
 	
	 /*@SuppressWarnings("unchecked")
	private List<MasUser> getDoctorDetails()
		{
		 MasUser fvrInitiatorDetail;
		  Query findAll = entityManager.createNamedQuery(
					"MasUser.findAll");
			List<MasUser> doctorList = (List<MasUser>) findAll.getResultList();
					
			return doctorList;
		}*/
	
}
