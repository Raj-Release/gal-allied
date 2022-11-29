package com.shaic.claim.userreallocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.MasUser;

@Stateless
public class DoctorReallocationSearchCriteriaService {

	
	public DoctorReallocationSearchCriteriaService() {
		super();
	}
	

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	
	public  List<SearchReallocationDoctorDetailsTableDTO> search() {
		
		List<MasUser> listOfClaim = new ArrayList<MasUser>(); 
		try{
			
			List<MasUser> doctorDetail = getDoctorDetailsBasedOnLimit();
			listOfClaim.addAll(doctorDetail);
			
		List<SearchReallocationDoctorDetailsTableDTO> tableDTO = SearchReallocationDoctorDetailsMapper.getClaimDTO(listOfClaim);
		
		return tableDTO;
	
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<MasUser> getDoctorDetailsBasedOnLimit(){
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
	
	
	@SuppressWarnings("unchecked")
	private List<MasUser> getDoctorDetails()
		{
		 MasUser fvrInitiatorDetail;
		  Query findAll = entityManager.createNamedQuery(
					"MasUser.findAll");
			List<MasUser> doctorList = (List<MasUser>) findAll.getResultList();
					
			return doctorList;
		}
	
public  List<SearchReallocationDoctorDetailsTableDTO> searchNameWise(String docName) {
		
		List<MasUser> listOfClaim = new ArrayList<MasUser>(); 
		try{
			
			List<MasUser> doctorDetail = getDoctorDetailsBasedOnName(docName.toLowerCase());
			listOfClaim.addAll(doctorDetail);
			
		List<SearchReallocationDoctorDetailsTableDTO> tableDTO = SearchReallocationDoctorDetailsMapper.getClaimDTO(listOfClaim);
		
		return tableDTO;
	
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}


@SuppressWarnings("unchecked")
public List<MasUser> getDoctorDetailsBasedOnName(String name){
	String query = "select distinct m.* from MAS_SEC_USER m inner join MAS_SEC_USER_LIMIT_MAPPING n on m.USER_ID = n.USER_ID where m.ACTIVE_STATUS is not null and m.ACTIVE_STATUS <> 'N' and Lower(m.USER_NAME) like '%"+name+"%' and rownum <=10";
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
	
}
