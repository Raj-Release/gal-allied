package com.shaic.claim.OMPReopenClaimRODLevel.SearchBased.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.OMPIntimation;

@Stateless
public class OMPReopenClaimRODLevelSearchBasedService extends AbstractDAO<OMPIntimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public OMPReopenClaimRODLevelSearchBasedService(){
		super();
	}

	@Override
	public Class<OMPIntimation> getDTOClass() {
		// TODO Auto-generated method stub
		return OMPIntimation.class;
	}
	
	
	public Page<OMPReopenClaimRODLevelSearchBasedTableDTO>  search(
			OMPReopenClaimRODLevelSearchBasedFormDto searchFormDTO,String userName,String passWord){
		
		List<OMPReopenClaimRODLevelSearchBasedTableDTO> ompReOpenClaimRODLevelTableDTO = new ArrayList<OMPReopenClaimRODLevelSearchBasedTableDTO>();
		
		OMPReopenClaimRODLevelSearchBasedTableDTO Table1 = new OMPReopenClaimRODLevelSearchBasedTableDTO();
		Table1.setSerialNo("1");
		Table1.setIntimationNo("CLI/2016/181316/0291297");
		Table1.setClaimno("CLMG/2016/171100/0004253");
		Table1.setPolicyno("P/171100/01/2016/004117");
	//	Table1.setProductcode("OMP-PRD-001 - Star Travel Protect ");
	//	Table1.setType("OMP-CVR-004_Emergency");
	//	Table1.setLoss("Fever");
	//	Table1.setLossdate(new Date());
	//	Table1.setIntimationDate(new Date());
//		Table1.setIntimatername("Vijay Mallya");
//		Table1.setIntimatedby("Vijay Mallya");
//		Table1.setIntimaticmode("IntimationMode");
//		Table1.setHospitalname("ABC Hospital");
	//	Table1.setStatus("Normal");
		OMPReopenClaimRODLevelSearchBasedTableDTO Table2 = new OMPReopenClaimRODLevelSearchBasedTableDTO();
		Table2.setSerialNo("2");
		Table2.setIntimationNo("CLI/2017/111214/02/0002301");
		Table2.setPolicyno("P/171100/01/2016/004117");
		Table2.setClaimno("CLMG/2016/171100/0004253");
//		
//		Table2.setProductcode("OMP-PRD-001 - Star Travel Protect ");
//		Table2.setType("OMP-CVR-004_Emergency");
//		Table2.setLoss("Fever");
//		Table2.setLossdate(new Date());
//		Table2.setIntimationDate(new Date());
//			
		ompReOpenClaimRODLevelTableDTO.add(Table1);
		ompReOpenClaimRODLevelTableDTO.add(Table2);
		
		Page<OMPReopenClaimRODLevelSearchBasedTableDTO> page = new Page<OMPReopenClaimRODLevelSearchBasedTableDTO>();
//		Page<OMPClaim> pagedList = super.pagedList(searchFormDTO.getPageable());
		page.setPageItems(ompReOpenClaimRODLevelTableDTO);
		page.setTotalRecords(ompReOpenClaimRODLevelTableDTO.size());
		page.setTotalList(ompReOpenClaimRODLevelTableDTO);
		return page;
	}

}
