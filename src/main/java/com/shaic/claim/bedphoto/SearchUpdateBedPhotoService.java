package com.shaic.claim.bedphoto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.hssf.util.HSSFColor.TAN;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasHospitalUserMapping;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.ims.bpm.claim.BPMClientContext;

@Stateless
public class SearchUpdateBedPhotoService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private HospitalService hospitalService;
	
	public SearchUpdateBedPhotoService(){
		
	}
	
	public Page<SearchBedPhotoTableDTO> search(BedPhotoDTO searchDto,String userName,String passWord,List<Long> hospitalIds){
		
			try{
				String intimationNo =  null != searchDto  && null != searchDto.getIntimationNo() ? searchDto.getIntimationNo() : null;
				
				List<Claim> listIntimations = new ArrayList<Claim>(); 
				final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
				Root<Claim> root = criteriaQuery.from(Claim.class);
				
				List<Predicate> conditionList = new ArrayList<Predicate>();
				
				List<SearchBedPhotoTableDTO> tableList = new ArrayList<SearchBedPhotoTableDTO>();
				
				if(intimationNo != null){
					Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
					conditionList.add(condition1);
					
					Expression<Long> exp = root.<Intimation>get("intimation").<Long> get("hospital");
					Predicate condition2 = exp.in(hospitalIds);
					conditionList.add(condition2);
					
					Predicate condition3 =  criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_APPROVE_STATUS);
					conditionList.add(condition3);
					
					} else {
						Expression<Long> exp = root.<Intimation>get("intimation").<Long> get("hospital");
						Predicate condition2 = exp.in(hospitalIds);
						conditionList.add(condition2);
						
						Predicate condition3 =  criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_APPROVE_STATUS);
						conditionList.add(condition3);
					}
		
	
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
				
				int pageNumber = searchDto.getPageable().getPageNumber();
				int firtResult;
				if(pageNumber > 1){
					firtResult = (pageNumber-1) *10;
				}else{
					firtResult = 0;
				}

				if(intimationNo == null){
				listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
				}else{
					listIntimations = typedQuery.getResultList();
				}
				
				listIntimations = typedQuery.getResultList();	
				
				List<SearchBedPhotoTableDTO> tableDTO = SearchUploadBedPhotoMapper.getInstance().getIntimationDTO(listIntimations);
				
				for (SearchBedPhotoTableDTO searchBedPhotoTableDTO : tableDTO) {
					if(searchBedPhotoTableDTO.getHospitalNameKey() != null){
						Hospitals hospDtls = hospitalService.getHospitalById(searchBedPhotoTableDTO.getHospitalNameKey());
						if(hospDtls != null){
							searchBedPhotoTableDTO.setHospitalName(/*hospDtls.getHospitalCode()+"-"+*/hospDtls.getName());
						}
						if(searchBedPhotoTableDTO.getDateOfAdmission() != null){
							String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(searchBedPhotoTableDTO.getDateOfAdmission());
							searchBedPhotoTableDTO.setDateOfAdmsissionStr(dateformate);
						} else {
							searchBedPhotoTableDTO.setDateOfAdmsissionStr("");
						}
						if(searchBedPhotoTableDTO.getDateOfDischarge() != null){
							String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(searchBedPhotoTableDTO.getDateOfDischarge());
							searchBedPhotoTableDTO.setDateOfDischargeStr(dateformate);
						} else {
							searchBedPhotoTableDTO.setDateOfDischargeStr("");
						}
						tableList.add(searchBedPhotoTableDTO);
					}
				}
	
			
				Page<SearchBedPhotoTableDTO> page = new Page<SearchBedPhotoTableDTO>();
				searchDto.getPageable().setPageNumber(pageNumber+1);
				page.setHasNext(true);
				if(tableList.isEmpty()){
					searchDto.getPageable().setPageNumber(1);
				}
				page.setPageNumber(pageNumber);
				page.setPageItems(tableList);
				page.setIsDbSearch(true);
				page.setTotalRecords(tableList.size());
				return page;
				}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
		
		return null;

		
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
