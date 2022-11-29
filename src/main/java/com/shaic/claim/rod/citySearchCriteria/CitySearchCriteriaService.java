package com.shaic.claim.rod.citySearchCriteria;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CitySearchCriteriaService {

	
	
	public CitySearchCriteriaService() {
		super();
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	protected EntityManager entityManager;
	
	public  List<SearchPayableAtTableDTO> search(String name) {

		List<SearchPayableAtTableDTO> listOfPayableAt = new ArrayList<SearchPayableAtTableDTO>(); 
		try{
			String query = "select * from (select distinct (m.DESCRIPTION) from MAS_DD_PAYABLE_LOCATION m where upper(m.DESCRIPTION) like '%"+name.toUpperCase()+"%' order by m.DESCRIPTION asc) where ROWNUM <= 20";
			Query nativeQuery = entityManager.createNativeQuery(query);

			List<Object> objList = (List<Object>) nativeQuery
					.getResultList();
			SearchPayableAtTableDTO tableDto = null;
			for (Object obj : objList) {
				tableDto = new SearchPayableAtTableDTO();
				tableDto.setPayable((String)obj);

				listOfPayableAt.add(tableDto);
			}

			return listOfPayableAt;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}
	
}
