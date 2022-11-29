package com.shaic.claim.preauth.pedvalidation.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PedValidationDummy;

@Stateless
public class ViewPedValidationService extends AbstractDAO<PedValidationDummy> {

	@EJB
	private DiagnosisService diagnosisService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private MasterService masterService;

	public ViewPedValidationService() {
		super();
	}

	public List<ViewPedValidationTableDTO> search(Long preAuthKey) {

		List<PedValidation> pedValidationDO = preauthService
				.findPedValidationByPreauthKey(preAuthKey, entityManager);

		List<ViewPedValidationTableDTO> list = new ArrayList<ViewPedValidationTableDTO>();

		if (!pedValidationDO.isEmpty()) {
			for (PedValidation pedValidation : pedValidationDO) {
				String diagnosis = masterService.getDiagnosis(
						pedValidation.getDiagnosisId(), entityManager);
				ViewPedValidationTableDTO dto = new ViewPedValidationTableDTO();

				dto.setDiagnosis(diagnosis);
				if(pedValidation.getIcdChpterId() != null) {
					SelectValue icdChapterbyId = masterService.getIcdChapterbyId(pedValidation.getIcdChpterId());
					if(icdChapterbyId != null){
						dto.setIcdChapterValue(icdChapterbyId.getValue());
					}
				}
				if(pedValidation.getIcdBlockId() != null) {
					SelectValue icdBlock = masterService.getIcdBlock(pedValidation.getIcdBlockId());
					if(icdBlock != null){
						dto.setIcdBlockValue(icdBlock.getValue());
					}
				}
				if(pedValidation.getIcdCodeId() != null) {
					SelectValue icdCode = masterService.getIcdCodeByKey(pedValidation.getIcdCodeId());
					if(icdCode != null){
						dto.setIcdCodeValue(icdCode.getValue());
					}
				}
				dto.setPolicyAgeing(pedValidation.getPolicyAging());
				if (pedValidation.getConsiderForPayment() != null) {
					dto.setConsideredForExclusion(pedValidation
							.getConsiderForPayment().equalsIgnoreCase("y") ? "Yes"
							: "No");
				}
				String pedNameConcat = "";
				String impactOnDiagnosisName = "";
				List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
						.getPEDDiagnosisByPEDValidationKey(
								pedValidation.getKey(), entityManager);
				if (!pedDiagnosisByPEDValidationKey.isEmpty()) {
					for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
						pedNameConcat = pedNameConcat
								+ (diagnosisPED.getPedName() != null ? diagnosisPED
										.getPedName() + ","
										: "");
						impactOnDiagnosisName = impactOnDiagnosisName
								+ (diagnosisPED.getDiagonsisImpact() != null ? diagnosisPED
										.getDiagonsisImpact().getValue() : "");
						dto.setConsideredForExclusion(diagnosisPED.getExclusionDetails()!=null ? diagnosisPED.getExclusionDetails().getExclusion() : "");
					}
					dto.setPedName(pedNameConcat);
					dto.setImpactOnDiagnosis(impactOnDiagnosisName);
					dto.setRemarks(pedValidation.getApprovedRemarks());
				}
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public Class<PedValidationDummy> getDTOClass() {
		return PedValidationDummy.class;
	}

}
