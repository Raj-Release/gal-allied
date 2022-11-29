package com.shaic.claim.preauth.diagnosisexclusioncheck.view;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.domain.ClaimLimitService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PedValidationDummy;
import com.shaic.domain.preauth.Preauth;

@Stateless
public class ViewDiagnosisExclusionCheckService extends
		AbstractDAO<PedValidationDummy> {

	@Inject
	private DiagnosisService diagnosisService;

	@Inject
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ClaimLimitService calimLimitService;

	public ViewDiagnosisExclusionCheckService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<ViewDiagnosisExclusionCheckDTO> search(Long preAuthKey) {
		Preauth preAuth = preauthService.getPreauthById(preAuthKey);
		Long productKey = preAuth.getIntimation().getPolicy().getProduct().getKey();
		List<PedValidation> pedValidationList = diagnosisService.search(preAuthKey);
		
		List<ViewDiagnosisExclusionCheckDTO> pedValidationListDTO = ViewDiagnosisExclusionCheckMapper.getInstance()
				.getViewDiagnosisExclusionCheckDTO(pedValidationList);
		for (ViewDiagnosisExclusionCheckDTO viewDiagnosisExclusionCheckDTO : pedValidationListDTO) {
			List<DiagnosisPED> pedDiagnosisByPEDValidation = preauthService
					.getPEDDiagnosisByPEDValidationKey(
							viewDiagnosisExclusionCheckDTO.getKey(), entityManager);			
			if (!pedDiagnosisByPEDValidation.isEmpty()) {
				String impactOnDiagnosisName = "";
				for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidation) {
					impactOnDiagnosisName = impactOnDiagnosisName
							+ (diagnosisPED.getDiagonsisImpact() != null ? diagnosisPED
									.getDiagonsisImpact().getValue() : "");
				}
				viewDiagnosisExclusionCheckDTO.setImpactOnDiagnosis(impactOnDiagnosisName);
			}
			
			if(productKey!=null){
				Long subLimitId=0l;
				if(null != viewDiagnosisExclusionCheckDTO.getSubLimit()){
				subLimitId = Long.parseLong(viewDiagnosisExclusionCheckDTO.getSubLimit());
				}
				Query query = entityManager.createNamedQuery("ClaimLimit.findBySubLimitIdAndProductKey");
				query.setParameter("productKey", productKey);
				query.setParameter("limitKey", subLimitId);
				List<ClaimLimit> singleResult = (List<ClaimLimit>) query.getResultList();
				if(!singleResult.isEmpty()){
					viewDiagnosisExclusionCheckDTO.setSubLimit(singleResult.get(0).getLimitName());
				}else{
					viewDiagnosisExclusionCheckDTO.setSubLimit("");
				}
				
			}
		}
		return pedValidationListDTO;
	}
	
	public List<ViewDiagnosisExclusionCheckDTO> getDiagnosisName(List<ViewDiagnosisExclusionCheckDTO> viewDiagnosisExclusionCheckDTO){
		if(viewDiagnosisExclusionCheckDTO!=null) {
			for (ViewDiagnosisExclusionCheckDTO viewDTO : viewDiagnosisExclusionCheckDTO) {
				String strDiagnosisName = masterService.getDiagnosisList(
						viewDTO.getDiagnosis(), this.entityManager);
				if (strDiagnosisName != null) {
					if (strDiagnosisName.length() > 0
							&& strDiagnosisName.charAt(strDiagnosisName
									.length() - 1) == ',') {
						strDiagnosisName = strDiagnosisName.substring(0,
								strDiagnosisName.length() - 1);
					}
					viewDTO.setDiagnosis(strDiagnosisName);
				}
			}
			return viewDiagnosisExclusionCheckDTO;
		}else{
			return null;
		}		
		
	}

	@Override
	public Class<PedValidationDummy> getDTOClass() {
		return PedValidationDummy.class;
	}

}
