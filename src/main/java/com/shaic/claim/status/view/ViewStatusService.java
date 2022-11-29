package com.shaic.claim.status.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.domain.ClaimAmountDetailsService;
import com.shaic.domain.MasterService;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;

@Stateless
public class ViewStatusService extends AbstractDAO<PedValidation> {

	@EJB
	private DiagnosisService diagnosisService;

	@EJB
	private MasterService masterService;

	@EJB
	private ClaimAmountDetailsService claimAmountDetailsService;

	public ViewStatusService() {
		super();
	}

	private List<ViewStatusDTO> getDiagnosisName(Long preAuthKey) {
		return searchPedValidation(preAuthKey);
	}

	public List<ViewStatusDTO> search(Long preAuthKey) {
		StringBuffer strProcedureData = new StringBuffer();
		StringBuffer strSignleDiagnosisName = new StringBuffer();
		String strDiagnosisName = "";
		String strDiagnosisNames = "";
		ViewStatusDTO viewDTO = searchPreAuth(preAuthKey);
		List<ViewStatusDTO> viewProcedureDTO = searchProcedure(preAuthKey);
		List<ViewStatusDTO> viewDiagnosisStatusDTO = getDiagnosisName(preAuthKey);
		List<ViewStatusDTO> viewStatusFinalDTO = getDiagnosisName(viewDiagnosisStatusDTO);

		for (ViewStatusDTO viewStatusProcedureDTO : viewProcedureDTO) {
			strProcedureData
					.append(viewStatusProcedureDTO.getProcedure() + ",");
		}

		String strProcedureName = strProcedureData.toString();
		if (strProcedureName.length() > 0
				&& strProcedureName.charAt(strProcedureName.length() - 1) == ',') {
			strProcedureName = strProcedureName.substring(0,
					strProcedureName.length() - 1);
		}

		if (!viewStatusFinalDTO.isEmpty()) {
			for (ViewStatusDTO viewStatusDTO : viewStatusFinalDTO) {
				strDiagnosisName = viewStatusDTO.getDiagnosis();				
				strSignleDiagnosisName.append(strDiagnosisName + " ");
			}
			strDiagnosisNames = strSignleDiagnosisName.toString();
		}

		viewDTO.setProcedure(strProcedureName);
		if (strDiagnosisNames != null) {
			if (strDiagnosisNames.length() > 0
					&& strDiagnosisNames.charAt(strDiagnosisNames.length() - 1) == ',') {
				strDiagnosisNames = strDiagnosisNames.substring(0,
						strDiagnosisNames.length() - 1);
			}
		}
		viewDTO.setDiagnosis(strDiagnosisNames);

		List<ViewStatusDTO> viewFinalStatusDTO = new ArrayList<ViewStatusDTO>();
		viewFinalStatusDTO.add(viewDTO);

		return viewFinalStatusDTO;
	}

	public List<ViewStatusDTO> getDiagnosisName(
			List<ViewStatusDTO> viewStatusDTO) {
//		StringBuffer diagnosisName = new StringBuffer();
		if (viewStatusDTO != null) {
			for (ViewStatusDTO viewDTO : viewStatusDTO) {
				viewDTO.setDiagnosis(masterService.getDiagnosisList(
						viewDTO.getDiagnosis(), this.entityManager));
			}
			return viewStatusDTO;
		} else {
			return null;
		}
	}

	public List<ViewStatusDTO> searchPedValidation(Long preAuthKey) {
		List<PedValidation> pedValidation = diagnosisService.search(preAuthKey);
		List<ViewStatusDTO> pedValidationListDTO = ViewStatusMapper.getInstance()
				.getViewStatusOfPedValidationDTO(pedValidation);
		return pedValidationListDTO;
	}

	public ViewStatusDTO searchPreAuth(Long preAuthKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Preauth> criteriaQuery = builder
				.createQuery(Preauth.class);

		Root<Preauth> searchRoot = criteriaQuery.from(Preauth.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Preauth> resultList = new ArrayList<Preauth>();

		if (preAuthKey != null) {
			Predicate intimationPredicate = builder.equal(
					searchRoot.<Long> get("key"), preAuthKey);
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Preauth> preAuthQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = preAuthQuery.getResultList();

		List<ViewStatusDTO> viewStatusDTO = ViewStatusMapper.getInstance()
				.getViewStatuOfPreAuthsDTO(resultList);

		for (ViewStatusDTO viewStatusDTO2 : viewStatusDTO) {
			List<ClaimAmountDetails> claimAmountDetailsList = claimAmountDetailsService
					.getClaimedAmoutnDetailsByPreAuthKey(preAuthKey, entityManager);

			Float preAuthRequestedAmount = 0f;

			if(!claimAmountDetailsList.isEmpty()){
				for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsList) {
					preAuthRequestedAmount = preAuthRequestedAmount
							+ claimAmountDetails.getClaimedBillAmount();
				}
			}
			
			viewStatusDTO2.setReqAmt(preAuthRequestedAmount.toString()
					.equalsIgnoreCase("0f") ? "" : preAuthRequestedAmount
					.toString());
		}

		return viewStatusDTO.get(0);
	}

	public List<ViewStatusDTO> searchProcedure(Long preAuthKey) {

        List<Procedure> resultList = new ArrayList<Procedure>();
		
		Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);
		
		resultList = (List<Procedure>)query.getResultList();

		return ViewStatusMapper.getInstance().getViewStatuOfProcedureDTO(resultList);
	}

	@Override
	public Class<PedValidation> getDTOClass() {
		return PedValidation.class;
	}

}
