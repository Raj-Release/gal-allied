package com.shaic.arch.fields.dto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;

public abstract class AbstractDAO<T extends AbstractEntity> {

	@PersistenceContext
	protected EntityManager entityManager;

	public T find(final Object o) {
		return entityManager.find(getDTOClass(), o);
	}

	public abstract Class<T> getDTOClass();
	


	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<T> list() {
		return entityManager.createQuery("from " + getDTOClass().getName(), getDTOClass()).getResultList();
	}

	public T merge(final T aT) {
		return entityManager.merge(aT);
	}
	
	public void persist(final T aT) {
		entityManager.persist(aT);
	}

	public T persistOrMerge(final T aT) {
		if (aT.getKey() != null && entityManager.find(getDTOClass(), aT.getKey()) != null) {
			return entityManager.merge(aT);
		}
		entityManager.persist(aT);
		return aT;
	}

	public void remove(final T aT) {
		entityManager.remove(aT);
	}
	
	public Page<T> pagedList(Pageable pabeable){
		try{
			Page<T> page = new Page<T>();
			int rowCount = numRecords();
			int pageSize = pabeable.getPageSize();
			
			int pageCount = rowCount / pageSize;
	        if (rowCount > pageSize * pageCount) {
	            pageCount++;
	        }
		        
			if (rowCount > 0 && getDTOClass() != null)
			{
				TypedQuery<T> query = entityManager.createQuery("From " + getDTOClass().getName(), getDTOClass());
				int pageNumber = pabeable.getPageNumber();
				query.setFirstResult(Math.abs((pageNumber) * pageSize));
				query.setMaxResults(pageSize);
				List <T> employeeList = query.getResultList();
				
				page.setPageNumber(pageNumber);
				page.setPagesAvailable(pageSize);
				page.setPageItems(employeeList);
			}
			return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}
	
	public int numRecords(){
		Query queryTotal = entityManager.createQuery("Select count(id) from " + getDTOClass().getName());
		return Integer.valueOf("" + queryTotal.getSingleResult());
	}
}
