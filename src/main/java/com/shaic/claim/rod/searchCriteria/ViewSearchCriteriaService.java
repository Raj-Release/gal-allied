package com.shaic.claim.rod.searchCriteria;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.BankMaster;
@Stateless
public class ViewSearchCriteriaService extends AbstractDAO<BankMaster>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	public  Page<ViewSearchCriteriaTableDTO> search(
			ViewSearchCriteriaFormDTO searchFormDTO) {
		List<BankMaster> listIntimations = new ArrayList<BankMaster>(); 
		try{
		String ifscCode = searchFormDTO.getIfcsCode() != null ? searchFormDTO.getIfcsCode() :null;
		String bankName = searchFormDTO.getBankName() !=null ? searchFormDTO.getBankName() : null;
		String branchName = searchFormDTO.getBranchName() != null ? searchFormDTO.getBranchName() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<BankMaster> criteriaQuery = criteriaBuilder.createQuery(BankMaster.class);
		
		Root<BankMaster> root = criteriaQuery.from(BankMaster.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(ifscCode != null){
		Predicate condition1 = criteriaBuilder.like(root.<String>get("ifscCode"), "%"+ifscCode+"%");
		conditionList.add(condition1);
		}
		if(bankName != null){
		Predicate condition2 = criteriaBuilder.like(root.<String>get("bankName"), "%"+bankName+"%");
		conditionList.add(condition2);
		}
		if(branchName != null){
		Predicate condition3 = criteriaBuilder.like(root.<String>get("branchName"), "%"+branchName+"%");
		conditionList.add(condition3);
		}
		if(ifscCode == null && bankName == null && branchName == null){
			criteriaQuery.select(root);
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<BankMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		listIntimations = typedQuery.getResultList();
		for(BankMaster inti:listIntimations){
			System.out.println(inti.getBankName()+"oooooooooooooooooooooooooo"+inti.getBranchName()+"tttttttttttttt"+inti.getIfscCode());
		}
		List<BankMaster> doList = listIntimations;
		List<ViewSearchCriteriaTableDTO> tableDTO = ViewSearchCeritierMapper.getInstance().getBankMasterDTO(doList);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
		
		List<ViewSearchCriteriaTableDTO> result = new ArrayList<ViewSearchCriteriaTableDTO>();
		result.addAll(tableDTO);
		Page<ViewSearchCriteriaTableDTO> page = new Page<ViewSearchCriteriaTableDTO>();
		page.setPageItems(result);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
			
		return null;
	}

	@Override
	public Class<BankMaster> getDTOClass() {
		// TODO Auto-generated method stub
		return BankMaster.class;
	}

}
