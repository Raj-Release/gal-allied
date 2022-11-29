package com.shaic.claim.intimation.unlockompintimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.naming.NamingException;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBFormDTO;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class SearchUnlockOMPIntimationDBService extends AbstractDAO<Intimation> {
	

	
	public SearchUnlockOMPIntimationDBService(){
		
	}
	
	
	public  Page<SearchUnlockIntimationDBTableDTO> searchForUnlockIntimation(
			SearchUnlockIntimationDBFormDTO formDTO,
			String userName, String passWord) throws NamingException {
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		
		Page<SearchUnlockIntimationDBTableDTO> page = new Page<SearchUnlockIntimationDBTableDTO>();
		
		List<Map<String, Object>> taskProcedure = null ;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		taskProcedure = dbCalculationService.getTaskProcedureOmpUnlock(intimationNo);
		
		List<SearchUnlockIntimationDBTableDTO> commonList = new ArrayList<SearchUnlockIntimationDBTableDTO>();
		for (Map<String, Object> dbObj : taskProcedure) {
			SearchUnlockIntimationDBTableDTO searchUnlockIntimationTableDTO = new SearchUnlockIntimationDBTableDTO();
			searchUnlockIntimationTableDTO.setIntimationNo((String)dbObj.get(SHAConstants.INTIMATION_NO));
			searchUnlockIntimationTableDTO.setLockedBy((String)dbObj.get(SHAConstants.LOCK_USER));
			searchUnlockIntimationTableDTO.setStage((String)dbObj.get(SHAConstants.CURRENT_Q));
			searchUnlockIntimationTableDTO.setWorkflowKey((Long)dbObj.get(SHAConstants.WK_KEY));
			commonList.add(searchUnlockIntimationTableDTO);
		}
		
		
		page.setPageItems(commonList);
		return page;
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
