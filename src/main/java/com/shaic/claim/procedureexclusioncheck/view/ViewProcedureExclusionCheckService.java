package com.shaic.claim.procedureexclusioncheck.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.ClaimLimitService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ViewTmpProcedure;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ViewTmpPreauth;

@Stateless
public class ViewProcedureExclusionCheckService extends AbstractDAO<Procedure> {

	@Inject
	private MasterService masterService;

	@Inject
	private ClaimLimitService claimLimitService;

	public ViewProcedureExclusionCheckService() {
		super();
	}

	public List<ViewProcedureExclusionCheckDTO> search(Long preAuthKey) {

         List<ViewTmpProcedure> resultList = new ArrayList<ViewTmpProcedure>();
		
		Query query = entityManager.createNamedQuery("ViewTmpProcedure.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);
		
		resultList = (List<ViewTmpProcedure>)query.getResultList();
		
		Query preauthQuery = entityManager.createNamedQuery("ViewTmpPreauth.findByKey");
		preauthQuery.setParameter("preauthKey", preAuthKey);
		
		ViewTmpPreauth preauth = (ViewTmpPreauth)preauthQuery.getSingleResult();
		
		List<ViewProcedureExclusionCheckDTO> procedureExclusionCheckServiceDTO = ViewProcedureExclusionCheckMapper.getInstance()
				.getViewProcedureExclusionCheckDTO(resultList);
		for (ViewProcedureExclusionCheckDTO viewProcedureExclusionCheckDTO : procedureExclusionCheckServiceDTO) {
			if(viewProcedureExclusionCheckDTO.getDayCareProcedure() != null){
				if(viewProcedureExclusionCheckDTO.getDayCareProcedure().equals("Y")){
					viewProcedureExclusionCheckDTO.setDayCareProcedure("Yes");
				}else
				{
					viewProcedureExclusionCheckDTO.setDayCareProcedure("No");
				}
			}
			if(viewProcedureExclusionCheckDTO.getConsiderForDayCare() != null){
				if(viewProcedureExclusionCheckDTO.getConsiderForDayCare().equals("Y")){
					viewProcedureExclusionCheckDTO.setConsiderForDayCare("Yes");
				}else
				{
					viewProcedureExclusionCheckDTO.setConsiderForDayCare("No");
				}
			}
			if(viewProcedureExclusionCheckDTO.getSubLimitApplicable() != null){
				if(viewProcedureExclusionCheckDTO.getSubLimitApplicable().equals("Y")){
					viewProcedureExclusionCheckDTO.setSubLimitApplicable("Yes");
				}else
				{
					viewProcedureExclusionCheckDTO.setSubLimitApplicable("No");
				}
			}
			if(viewProcedureExclusionCheckDTO.getConsiderForPayment() != null){
				if(viewProcedureExclusionCheckDTO.getConsiderForPayment().equals("Y")){
					viewProcedureExclusionCheckDTO.setConsiderForPayment("Yes");
				}else
				{
					viewProcedureExclusionCheckDTO.setConsiderForPayment("No");
				}
			}
		}
		for (ViewProcedureExclusionCheckDTO viewProcedureExclusionCheckDTO : procedureExclusionCheckServiceDTO) {
			if (viewProcedureExclusionCheckDTO.getSubLimits() != null) {
				List<ClaimLimit> claimLimit = claimLimitService.getClaimByKey(
						Long.parseLong(viewProcedureExclusionCheckDTO
								.getSubLimits()), entityManager);
				
				if (!claimLimit.isEmpty()) {
					viewProcedureExclusionCheckDTO.setSubLimits(claimLimit.get(
							0).getLimitName());
					viewProcedureExclusionCheckDTO.setSubLimitName(claimLimit.get(
							0).getLimitName());
					viewProcedureExclusionCheckDTO.setSubLimitDesc(claimLimit.get(0).getLimitDescription() );
					if(claimLimit.get(0).getMaxPerClaimAmount() != null){
					viewProcedureExclusionCheckDTO.setSubLimitAmt(claimLimit.get(0).getMaxPerClaimAmount().toString());
					}
				}
			}
		}
		return procedureExclusionCheckServiceDTO;
	}

	@Override
	public Class<Procedure> getDTOClass() {
		// TODO Auto-generated method stub
		return Procedure.class;
	}

}
