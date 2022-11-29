package com.shaic.claim.reports.helpdeskstatusreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;
import com.vaadin.server.VaadinSession;
@Stateless

@TransactionManagement(TransactionManagementType.BEAN)

public class HelpDeskStatusReportService extends AbstractDAO<Intimation> {

	@PersistenceContext
	protected EntityManager entityManager;
	protected EntityManager entityManager1;
	@Resource
	private UserTransaction utx;

	private Long hospitalType = null;
	private Date fromDate;
	private Date toDate;

	public HelpDeskStatusReportService() {
		super();
	}

	public Page<HelpDeskStatusReportTableDTO> search(
			HelpDeskStatusReportFormDTO helpDeskFormDTO, String userName,
			String passWord, UsertoCPUMappingService userCPUService) {

		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		List<Claim> claimsList = new ArrayList<Claim>();

		try {

			VaadinSession.getCurrent().getSession()
					.setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();

			hospitalType = null != helpDeskFormDTO.getHospitalType() ? helpDeskFormDTO
					.getHospitalType().getId() : null;
			fromDate = null != helpDeskFormDTO.getFromDate() ? helpDeskFormDTO
					.getFromDate() : null;
			toDate = null != helpDeskFormDTO.getToDate() ? helpDeskFormDTO
					.getToDate() : null;
			Long cpuCode = null != helpDeskFormDTO.getCpuCode() ? helpDeskFormDTO
					.getCpuCode().getId() : null;
			Date billRecFromDate = null != helpDeskFormDTO.getBillRecFrom() ? helpDeskFormDTO
					.getBillRecFrom() : null;
			Date billRecToDate = null != helpDeskFormDTO.getBillRecTo() ? helpDeskFormDTO
					.getBillRecTo() : null;
			boolean seniorCitizenClaim = null != helpDeskFormDTO
					.getSeniorCitizenClaim() ? helpDeskFormDTO
					.getSeniorCitizenClaim() : false;
//			boolean health = null != helpDeskFormDTO.getHealth() ? helpDeskFormDTO
//					.getHealth() : false;
			// Long dummyDate = null;
			String strClaimType = null;
			SelectValue selClaimType = null;

			selClaimType = helpDeskFormDTO.getClaimType();
			if (null != selClaimType && null != selClaimType.getValue()) {
				strClaimType = selClaimType.getValue();
			}

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaBuilder criteriaBuilder1 = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Reimbursement> criteriaQuery = criteriaBuilder
					.createQuery(Reimbursement.class);
			final CriteriaQuery<Claim> criteriaQuery1 = criteriaBuilder1
					.createQuery(Claim.class);

			Root<Reimbursement> root = criteriaQuery.from(Reimbursement.class);
			Root<Claim> root1 = criteriaQuery1.from(Claim.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			List<Predicate> conditionList1 = new ArrayList<Predicate>();

			if (null != hospitalType) {

				if (hospitalType == 1 || hospitalType == 3) {

					if (null != fromDate && null != toDate && hospitalType == 2) {
						Predicate condition5 = criteriaBuilder
								.greaterThanOrEqualTo(root.<Claim> get("claim")
										.<Intimation> get("intimation")
										.<Date> get("createdDate"), fromDate);
						conditionList.add(condition5);
						Calendar c = Calendar.getInstance();
						c.setTime(toDate);
						c.add(Calendar.DATE, 1);
						toDate = c.getTime();
						Predicate condition6 = criteriaBuilder
								.lessThanOrEqualTo(root.<Claim> get("claim")
										.<Intimation> get("intimation")
										.<Date> get("createdDate"), toDate);
						conditionList.add(condition6);
					}

					if (null != billRecFromDate && null != billRecToDate && (hospitalType == 1 || hospitalType == 3)) {

						Predicate condition7 = criteriaBuilder
								.greaterThanOrEqualTo(
										root.<Date> get("createdDate"),
										billRecFromDate);
						conditionList.add(condition7);
						Calendar c = Calendar.getInstance();
						c.setTime(billRecToDate);
						c.add(Calendar.DATE, 1);
						billRecToDate = c.getTime();
						Predicate condition8 = criteriaBuilder.lessThan(
								root.<Date> get("createdDate"), billRecToDate);
						conditionList.add(condition8);
					}

					if (seniorCitizenClaim == true) {
						// Predicate condition8 =
						// criteriaBuilder.equal(root.<Claim>get("claim").<Intimation>get("intimation").<Policy>get("policy").<Product>get("product").<Long>get("key"),ReferenceTable.SENIOR_CITIZEN_RED_CARPET);
						// conditionList.add(condition8);

						List<Long> myStatusList = new ArrayList<Long>();

						myStatusList
								.add(ReferenceTable.SENIOR_CITIZEN_RED_CARPET);
						myStatusList.add(ReferenceTable.SENIOR_CITIZEN_POLICY);

						Expression<Long> exp = root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<Policy> get("policy")
								.<Product> get("product").<Long> get("key");
						Predicate condition3 = exp.in(myStatusList);

						conditionList.add(condition3);

					}

					if (helpDeskFormDTO.getHealth() != null && helpDeskFormDTO.getHealth() == true && (helpDeskFormDTO.getPa() == null || (helpDeskFormDTO.getPa() != null && !helpDeskFormDTO.getPa())) ) {
						Predicate condition8 = criteriaBuilder.equal(
								root.<Claim> get("claim")
										.<Long> get("lobId"),
								ReferenceTable.HEALTH_LOB_KEY);
						Predicate condition9 = criteriaBuilder.equal(root
								.<Claim> get("claim").<Long> get("lobId"),
								ReferenceTable.PACKAGE_MASTER_VALUE);

						Predicate condition10 = criteriaBuilder.equal(
								root.<Claim> get("claim").<String> get(
										"processClaimType"),
								SHAConstants.HEALTH_LOB_FLAG);

						Predicate condition11 = criteriaBuilder.isNull(
								root.<Claim> get("claim").<String> get(
										"processClaimType"));
						
						Predicate clmTypePredicate = criteriaBuilder.or(
								condition10, condition11);

						Predicate lobPredicate = criteriaBuilder.and(
								condition9, clmTypePredicate);

						Predicate finalLobPredicate = criteriaBuilder.or(
								condition8, lobPredicate, condition11);
						conditionList.add(finalLobPredicate);

						// conditionList.add(condition8);
					}
					else if (helpDeskFormDTO.getPa() != null && helpDeskFormDTO.getPa() == true && (helpDeskFormDTO.getHealth() == null || (helpDeskFormDTO.getHealth() != null && !helpDeskFormDTO.getHealth()))){
						Predicate condition8 = criteriaBuilder.equal(
								root.<Claim> get("claim")
										.<Long> get("lobId"),
								ReferenceTable.PA_LOB_KEY);
						Predicate condition9 = criteriaBuilder.equal(root
								.<Claim> get("claim").<Long> get("lobId"),
								ReferenceTable.PACKAGE_MASTER_VALUE);

						Predicate condition10 = criteriaBuilder.equal(
								root.<Claim> get("claim").<String> get(
										"processClaimType"),
								SHAConstants.PA_LOB_TYPE);

						Predicate lobPredicate = criteriaBuilder.and(
								condition9, condition10);

						Predicate finalLobPredicate = criteriaBuilder.or(
								condition8, lobPredicate);
						conditionList.add(finalLobPredicate);

						// conditionList.add(condition8);
					}
					else{
						
					}
					

					if (null != cpuCode) {

						Predicate condition1 = criteriaBuilder.equal(
								root.<Claim> get("claim")
										.<Intimation> get("intimation")
										.<TmpCPUCode> get("cpuCode")
										.<Long> get("key"), cpuCode);
						conditionList.add(condition1);
					} else {
						List<Long> cpuKeyList = new ArrayList<Long>();
						cpuKeyList = userCPUService.getCPUCodeList(userName,
								entityManager);

						Predicate userCpuMappingCondition = root
								.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<TmpCPUCode> get("cpuCode").<Long> get("key")
								.in(cpuKeyList);

						conditionList.add(userCpuMappingCondition);
					}

					if (null != strClaimType) {
						Predicate condition1 = criteriaBuilder.equal(
								root.<Claim> get("claim")
										.<MastersValue> get("claimType")
										.<String> get("value"), strClaimType);
						conditionList.add(condition1);
					}

					if (hospitalType == 1) {

						Predicate condition2 = criteriaBuilder.notEqual(root
								.<Status> get("status").<Long> get("key"),
								ReferenceTable.FINANCIAL_SETTLED);
						conditionList.add(condition2);
					}

					else {
						List<Long> myStatusList = new ArrayList<Long>();
						myStatusList.add(ReferenceTable.FINANCIAL_SETTLED);
						Expression<Long> exp = root.<Status> get("status")
								.<Long> get("key");
						Predicate condition3 = exp.in(myStatusList);
						conditionList.add(condition3);

					}

				} else if (hospitalType == 2) {

					if (null != fromDate && null != toDate) {
						Predicate condition5 = criteriaBuilder1
								.greaterThanOrEqualTo(
										root1.<Intimation> get("intimation")
												.<Date> get("createdDate"),
										fromDate);
						conditionList1.add(condition5);
						Calendar c = Calendar.getInstance();
						c.setTime(toDate);
						c.add(Calendar.DATE, 1);
						toDate = c.getTime();
						Predicate condition6 = criteriaBuilder1
								.lessThanOrEqualTo(
										root1.<Intimation> get("intimation")
												.<Date> get("createdDate"),
										toDate);
						conditionList1.add(condition6);
					}
					
					Predicate statusCondition =  criteriaBuilder1.equal(root1
							.<Status> get("status").<Long> get("key"), ReferenceTable.INTIMATION_REGISTERED_STATUS);

					conditionList1.add(statusCondition);					
					
					if (seniorCitizenClaim == true) {

						List<Long> myStatusList = new ArrayList<Long>();

						myStatusList
								.add(ReferenceTable.SENIOR_CITIZEN_RED_CARPET);
						myStatusList.add(ReferenceTable.SENIOR_CITIZEN_POLICY);

						Expression<Long> exp = root1
								.<Intimation> get("intimation")
								.<Policy> get("policy")
								.<Product> get("product").<Long> get("key");
						Predicate condition3 = exp.in(myStatusList);

						conditionList1.add(condition3);
					}

					if (helpDeskFormDTO.getHealth() != null && helpDeskFormDTO.getHealth() == true && (helpDeskFormDTO.getPa() == null || (helpDeskFormDTO.getPa() != null && !helpDeskFormDTO.getPa())) ) {
						Predicate condition8 = criteriaBuilder1.equal(
								root1.<Long> get("lobId"),
								ReferenceTable.HEALTH_LOB_KEY);
						//conditionList1.add(condition8);
						Predicate condition9 = criteriaBuilder1.equal(
								root1.<Long> get("lobId"),
								ReferenceTable.PACKAGE_MASTER_VALUE);

						Predicate condition10 = criteriaBuilder1.equal(
								root1.<String> get("processClaimType"),
								SHAConstants.HEALTH_LOB_FLAG);
						
						Predicate condition11 = criteriaBuilder1.isNull(
								root1.<String> get("processClaimType"));
						
						Predicate clmTypePredicate = criteriaBuilder1.or(
								condition10, condition11);

						Predicate lobPredicate = criteriaBuilder1.and(
								condition9, clmTypePredicate);

						Predicate finalLobPredicate = criteriaBuilder1.or(
								condition8, lobPredicate);
						conditionList1.add(finalLobPredicate);
					}
					else if (helpDeskFormDTO.getPa() != null && helpDeskFormDTO.getPa() == true && (helpDeskFormDTO.getHealth() == null || (helpDeskFormDTO.getHealth() != null && !helpDeskFormDTO.getHealth()))){
						Predicate condition8 = criteriaBuilder1.equal(
								root1.<Long> get("lobId"),
								ReferenceTable.PA_LOB_KEY);
						//conditionList1.add(condition8);
						
						Predicate condition9 = criteriaBuilder1.equal(
								root1.<Long> get("lobId"),
								ReferenceTable.PACKAGE_MASTER_VALUE);

						Predicate condition10 = criteriaBuilder1.equal(
								root1.<String> get("processClaimType"),
								SHAConstants.PA_LOB_TYPE);

						Predicate lobPredicate = criteriaBuilder1.and(
								condition9, condition10);

						Predicate finalLobPredicate = criteriaBuilder1.or(
								condition8, lobPredicate);
						conditionList1.add(finalLobPredicate);
					}
						

					if (null != cpuCode) {

						Predicate condition1 = criteriaBuilder.equal(root1
								.<Intimation> get("intimation")
								.<TmpCPUCode> get("cpuCode").<Long> get("key"),
								cpuCode);
						conditionList1.add(condition1);
					} else {
						List<Long> cpuKeyList = new ArrayList<Long>();
						cpuKeyList = userCPUService.getCPUCodeList(userName,
								entityManager);

						Predicate userCpuMappingCondition = root1
								.<Intimation> get("intimation")
								.<TmpCPUCode> get("cpuCode").<Long> get("key")
								.in(cpuKeyList);

						conditionList1.add(userCpuMappingCondition);
					}

					if (null != strClaimType) {
						Predicate condition1 = criteriaBuilder.equal(
								root1.<MastersValue> get("claimType")
										.<String> get("value"), strClaimType);
						conditionList1.add(condition1);
					}

					/*
					 * Predicate condition7 =
					 * criteriaBuilder.greaterThanOrEqualTo
					 * (root.<DocAcknowledgement
					 * >get("docAcknowLedgement").<Date>
					 * get("documentReceivedDate"), billRecFromDate);
					 * conditionList.add(condition7); Predicate condition8 =
					 * criteriaBuilder
					 * .lessThanOrEqualTo(root.<DocAcknowledgement
					 * >get("docAcknowLedgement"
					 * ).<Date>get("documentReceivedDate"), billRecToDate);
					 * conditionList.add(condition8); Predicate condition9 =
					 * criteriaBuilder
					 * .equal(root.<DocAcknowledgement>get("docAcknowLedgement"
					 * ).<Long>get("key"), dummyDate);//for document not
					 * received conditionList.add(condition9);
					 */

				}

			}

			if (hospitalType == null && fromDate == null && toDate == null
					&& billRecFromDate == null && billRecToDate == null
					&& seniorCitizenClaim == false) {

				criteriaQuery.select(root);
			} else {

				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList
								.toArray(new Predicate[] {})));
				criteriaQuery1.select(root1).where(
						criteriaBuilder1.and(conditionList1
								.toArray(new Predicate[] {})));
			}

			int pageNumber = helpDeskFormDTO.getPageable().getPageNumber();

			/*
			 * if (hospitalType == null && listIntimations.size() > 10) {
			 * listIntimations = typedQuery.setFirstResult(firtResult)
			 * .setMaxResults(10).getResultList(); }
			 */

			if (null != hospitalType) {
				if (hospitalType == 1 || hospitalType == 3) {
					final TypedQuery<Reimbursement> typedQuery = entityManager
							.createQuery(criteriaQuery);
					ClaimsReportService.popinReportLog(entityManager, userName,
							"HelpDeskStatusReport", new Date(), new Date(),
							SHAConstants.RPT_BEGIN);
					reimbursementList = typedQuery.getResultList();
					for (Reimbursement rimbursement : reimbursementList) {
						entityManager.refresh(rimbursement);

					}
					List<Reimbursement> doList = reimbursementList;
					List<HelpDeskStatusReportTableDTO> tableDTO = HelpDeskStatusReportMapper
							.getInstance().getHelpDeskTableObjects(doList);
					tableDTO = getTableDetailsByReimbursement(tableDTO);
					List<HelpDeskStatusReportTableDTO> result = new ArrayList<HelpDeskStatusReportTableDTO>();
					result.addAll(tableDTO);
					Page<HelpDeskStatusReportTableDTO> page = new Page<HelpDeskStatusReportTableDTO>();
					helpDeskFormDTO.getPageable().setPageNumber(pageNumber + 1);
					// page.setHasNext(true);
					if (result.isEmpty()) {
						helpDeskFormDTO.getPageable().setPageNumber(1);
					}
					page.setPageNumber(pageNumber);
					page.setPageItems(result);
					page.setIsDbSearch(true);
					ClaimsReportService.popinReportLog(entityManager, userName,
							"HelpDeskStatusReport", new Date(), new Date(),
							SHAConstants.RPT_SUCCESS);
					utx.commit();
					return page;
				}
				if (hospitalType == 2) {
					final TypedQuery<Claim> typedQuery1 = entityManager
							.createQuery(criteriaQuery1);
					ClaimsReportService.popinReportLog(entityManager, userName,
							"HelpDeskStatusReport", new Date(), new Date(),
							SHAConstants.RPT_BEGIN);
					claimsList = typedQuery1.getResultList();
					List<Claim> doList1 = new ArrayList<Claim>();

					for (Claim claim : claimsList) {

						entityManager.refresh(claim);
						List<Reimbursement> billStatus = getBillReceivedDetails(claim
								.getKey());
						if (billStatus.isEmpty()) {
							doList1.add(claim);
						}
					}
					List<HelpDeskStatusReportTableDTO> tableDTO1 = HelpDeskStatusReportMapper
							.getHelpDeskTableObjects1(doList1);
					tableDTO1 = getTableDetailByClaim(tableDTO1);
					List<HelpDeskStatusReportTableDTO> result = new ArrayList<HelpDeskStatusReportTableDTO>();
					result.addAll(tableDTO1);
					Page<HelpDeskStatusReportTableDTO> page = new Page<HelpDeskStatusReportTableDTO>();
					helpDeskFormDTO.getPageable().setPageNumber(pageNumber + 1);
					// page.setHasNext(true);
					if (result.isEmpty()) {
						helpDeskFormDTO.getPageable().setPageNumber(1);
					}
					page.setPageNumber(pageNumber);
					page.setPageItems(result);
					page.setIsDbSearch(true);
					ClaimsReportService.popinReportLog(entityManager, userName,
							"HelpDeskStatusReport", new Date(), new Date(),
							SHAConstants.RPT_SUCCESS);
					utx.commit();
					return page;
				}
			}

		} catch (Exception e) {
			ClaimsReportService.popinReportLog(entityManager, userName,
					"HelpDeskStatusReport", new Date(), new Date(),
					SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(e);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}

	private List<HelpDeskStatusReportTableDTO> getTableDetailsByReimbursement(
			List<HelpDeskStatusReportTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		ReimbursementQuery queryReplyDateDetail;
		DocAcknowledgement ack;
		StageInformation zonalDate;
		Status status;
		Preauth prauthObj;
		for (HelpDeskStatusReportTableDTO helpDeskStatusReportTableDTO : tableDTO) {

			hospitalDetail = getHospitalDetails(helpDeskStatusReportTableDTO
					.getHospitalId());
			if (hospitalDetail != null) {
				helpDeskStatusReportTableDTO.setHospitalName(hospitalDetail
						.getName());
				helpDeskStatusReportTableDTO.setHospitalCode(hospitalDetail
						.getHospitalCode());
				// tableDTO.get(index).setCpuCode(hospitalDetail.getCpuId());
				helpDeskStatusReportTableDTO.setHospitalType(hospitalDetail
						.getHospitalTypeName());

			}

			queryReplyDateDetail = getQueryReplyDateDetails(helpDeskStatusReportTableDTO
					.getReimbursementKey());

			if (queryReplyDateDetail != null) {
				helpDeskStatusReportTableDTO
						.setLastQueryReplyDate(queryReplyDateDetail
								.getQueryReplyDate());
				helpDeskStatusReportTableDTO.setRemainder(queryReplyDateDetail
						.getReminderCount());
				if (null != queryReplyDateDetail.getReminderCount()) {
					if (queryReplyDateDetail.getReminderCount().equals(1)) {
						helpDeskStatusReportTableDTO
								.setRemainderDate(queryReplyDateDetail
										.getReminderDate1());
					}
					if (queryReplyDateDetail.getReminderCount().equals(2)) {
						helpDeskStatusReportTableDTO
								.setRemainderDate(queryReplyDateDetail
										.getReminderDate2());
					}
					if (queryReplyDateDetail.getReminderCount().equals(3)) {
						helpDeskStatusReportTableDTO
								.setRemainderDate(queryReplyDateDetail
										.getReminderDate3());
					}
				}

			}

			ack = getOfcLocationDetails(helpDeskStatusReportTableDTO.getKey());

			if (ack != null) {
				if (null != ack.getDocumentReceivedFromId()) {
					if ((ack.getDocumentReceivedFromId().getValue())
							.equalsIgnoreCase("Hospital")) {
						helpDeskStatusReportTableDTO
								.setReceivedFrom(hospitalDetail.getName());
					}
				}
			}

			zonalDate = getZonalApproveDate(helpDeskStatusReportTableDTO
					.getClaimKey());

			status = getStatusDetails(helpDeskStatusReportTableDTO
					.getStatusKey());

			if (status != null) {

				if ((ReferenceTable.CASHLESS_CLAIM)
						.equalsIgnoreCase(helpDeskStatusReportTableDTO
								.getClaimType())) {

					prauthObj = getPreauthDetails(helpDeskStatusReportTableDTO
							.getClaimKey());

					if (null != prauthObj) {
						List<ClaimAmountDetails> claimamountDetailsList = getClaimAmountDetailsByPreauth(prauthObj
								.getKey());

						if (null != claimamountDetailsList) {

							if (((ReferenceTable.CREATE_ROD_STAGE_KEY)
									.equals(helpDeskStatusReportTableDTO
											.getStageKey()) && (ReferenceTable.CREATE_ROD_STATUS_KEY
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())))
									|| ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
											.equals(helpDeskStatusReportTableDTO
													.getStageKey()) && (ReferenceTable.BILL_ENTRY_STATUS_KEY
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey())))) {
								if (!(("Y")
										.equalsIgnoreCase(helpDeskStatusReportTableDTO
												.getPartialHospitalizationFlag()))) {

									if (SHAUtils.isDirectToFinancial(prauthObj,
											claimamountDetailsList)) {
										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setBillingStage(null);
										helpDeskStatusReportTableDTO
												.setMedicalStatus(null);
										helpDeskStatusReportTableDTO
												.setZonalMedicalStatus(null);
										helpDeskStatusReportTableDTO
												.setZonalMedicalapprovedDateValue(null);
										helpDeskStatusReportTableDTO
												.setBillinCompletedDateValue(null);
										helpDeskStatusReportTableDTO
												.setMedicalApprovedDateValue(null);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
										if ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
												.equals(helpDeskStatusReportTableDTO
														.getStageKey())) {
											helpDeskStatusReportTableDTO
													.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
										}

									} else if (SHAUtils.isDirectToBilling(
											prauthObj, claimamountDetailsList)) {
										helpDeskStatusReportTableDTO
												.setMedicalStatus(null);
										helpDeskStatusReportTableDTO
												.setMedicalApprovedDateValue(null);
										helpDeskStatusReportTableDTO
												.setZonalMedicalStatus(null);
										helpDeskStatusReportTableDTO
												.setZonalMedicalapprovedDateValue(null);
										helpDeskStatusReportTableDTO
												.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setBillinCompletedDateValue(null);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
										if ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
												.equals(helpDeskStatusReportTableDTO
														.getStageKey())) {
											helpDeskStatusReportTableDTO
													.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
										}
									}

									else {

										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setZonalMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setZonalMedicalapprovedDateValue(null);
										helpDeskStatusReportTableDTO
												.setBillinCompletedDateValue(null);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
										helpDeskStatusReportTableDTO
												.setMedicalApprovedDateValue(null);
										if ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
												.equals(helpDeskStatusReportTableDTO
														.getStageKey())) {
											helpDeskStatusReportTableDTO
													.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
										}

									}

								} else {

									helpDeskStatusReportTableDTO
											.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setZonalMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setZonalMedicalapprovedDateValue(null);
									helpDeskStatusReportTableDTO
											.setBillinCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setMedicalApprovedDateValue(null);
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);
									if (!(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS)
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey())
											&& !(ReferenceTable.BILL_ENTRY_STATUS_KEY)
													.equals(helpDeskStatusReportTableDTO
															.getStatusKey())) {
										helpDeskStatusReportTableDTO
												.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
									} else {
										helpDeskStatusReportTableDTO
												.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
									}
								}

							}

							if (ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY
									.equals(helpDeskStatusReportTableDTO
											.getStageKey())) {
								if (!(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())
										&& !(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
												.equals(helpDeskStatusReportTableDTO
														.getStatusKey())) {

									helpDeskStatusReportTableDTO
											.setMedicalStatus(status
													.getProcessValue());
									helpDeskStatusReportTableDTO
											.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
									if (null != zonalDate) {
										helpDeskStatusReportTableDTO
												.setZonalMedicalApprovedDate(zonalDate
														.getCreatedDate());
									}
									helpDeskStatusReportTableDTO
											.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setBillinCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);

									if ((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey())) {
										helpDeskStatusReportTableDTO
												.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setBillinCompletedDate(helpDeskStatusReportTableDTO
														.getBillinCompletedDate());
										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
									}
								} else {
									helpDeskStatusReportTableDTO
											.setMedicalStatus(status
													.getProcessValue());
								}

								if ((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									if (SHAUtils.isDirectToFinancial(prauthObj,
											claimamountDetailsList)) {

										helpDeskStatusReportTableDTO
												.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
									} else {
										helpDeskStatusReportTableDTO
												.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setBillinCompletedDateValue(null);
										helpDeskStatusReportTableDTO
												.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
										helpDeskStatusReportTableDTO
												.setFinancialCompletedDateValue(null);
									}
								}

								if (!(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())
										&& !((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
												.equals(helpDeskStatusReportTableDTO
														.getStatusKey()))) {
									helpDeskStatusReportTableDTO
											.setMedicalApprovedDateValue(null);
									helpDeskStatusReportTableDTO
											.setBillingAmount(null);
								} else {
									helpDeskStatusReportTableDTO
											.setMedicalApprovedDateValue(null);
								}

								if ((ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setMedicalApprovedDate(helpDeskStatusReportTableDTO
													.getMedicalApprovedDate());
								}

							}

							if (ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY
									.equals(helpDeskStatusReportTableDTO
											.getStageKey()))

							{
								if (!(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalStatus(status
													.getProcessValue());
									if (null != zonalDate) {
										helpDeskStatusReportTableDTO
												.setZonalMedicalApprovedDate(zonalDate
														.getCreatedDate());
									}
									helpDeskStatusReportTableDTO
											.setMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setBillinCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setMedicalApprovedDateValue(null);
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);

								} else {
									helpDeskStatusReportTableDTO
											.setMedicalStatus(status
													.getProcessValue());
								}

								if (!(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalapprovedDateValue(null);
									helpDeskStatusReportTableDTO
											.setBillingAmount(null);
								}
							}

							if ((ReferenceTable.BILLING_STAGE)
									.equals(helpDeskStatusReportTableDTO
											.getStageKey())) {

								if (null != prauthObj) {
									if (null != claimamountDetailsList) {
										if (!(("Y")
												.equalsIgnoreCase(helpDeskStatusReportTableDTO
														.getPartialHospitalizationFlag()))) {
											if (SHAUtils.isDirectToBilling(
													prauthObj,
													claimamountDetailsList)) {

												helpDeskStatusReportTableDTO
														.setBillingStage(status
																.getProcessValue());
												helpDeskStatusReportTableDTO
														.setMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setMedicalApprovedDateValue(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalapprovedDateValue(null);
												helpDeskStatusReportTableDTO
														.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
												helpDeskStatusReportTableDTO
														.setFinancialCompletedDateValue(null);

												if (!(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)
														.equals(helpDeskStatusReportTableDTO
																.getStatusKey())) {
													helpDeskStatusReportTableDTO
															.setBillinCompletedDateValue(null);
													helpDeskStatusReportTableDTO
															.setBillingAmount(null);
												}

											} else {

												helpDeskStatusReportTableDTO
														.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
												helpDeskStatusReportTableDTO
														.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
												if (null != zonalDate) {
													helpDeskStatusReportTableDTO
															.setZonalMedicalApprovedDate(zonalDate
																	.getCreatedDate());
												}
												helpDeskStatusReportTableDTO
														.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
												helpDeskStatusReportTableDTO
														.setFinancialCompletedDateValue(null);
												helpDeskStatusReportTableDTO
														.setBillingStage(status
																.getProcessValue());

												if (!(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)
														.equals(helpDeskStatusReportTableDTO
																.getStatusKey())) {
													helpDeskStatusReportTableDTO
															.setBillinCompletedDateValue(null);
													helpDeskStatusReportTableDTO
															.setBillingAmount(null);
												}
											}
										} else {
											helpDeskStatusReportTableDTO
													.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
											helpDeskStatusReportTableDTO
													.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
											if (null != zonalDate) {
												helpDeskStatusReportTableDTO
														.setZonalMedicalApprovedDate(zonalDate
																.getCreatedDate());
											}
											helpDeskStatusReportTableDTO
													.setBillingStage(status
															.getProcessValue());
											helpDeskStatusReportTableDTO
													.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
											helpDeskStatusReportTableDTO
													.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
										}

									}
								}
							}

							if ((ReferenceTable.FINANCIAL_STAGE)
									.equals(helpDeskStatusReportTableDTO
											.getStageKey())) {

								if (null != prauthObj) {
									if (null != claimamountDetailsList) {
										if (!(("Y")
												.equalsIgnoreCase(helpDeskStatusReportTableDTO
														.getPartialHospitalizationFlag()))) {

											if (SHAUtils.isDirectToFinancial(
													prauthObj,
													claimamountDetailsList)) {
												helpDeskStatusReportTableDTO
														.setFinancialStage(status
																.getProcessValue());
												helpDeskStatusReportTableDTO
														.setBillingStage(null);
												helpDeskStatusReportTableDTO
														.setMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalapprovedDateValue(null);
												helpDeskStatusReportTableDTO
														.setBillinCompletedDateValue(null);
												helpDeskStatusReportTableDTO
														.setMedicalApprovedDateValue(null);

												if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
														.equals(helpDeskStatusReportTableDTO
																.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
														.equals(helpDeskStatusReportTableDTO
																.getStatusKey()))) {
													helpDeskStatusReportTableDTO
															.setFinancialCompletedDateValue(null);
												}

											}
											if (SHAUtils.isDirectToBilling(
													prauthObj,
													claimamountDetailsList)) {
												helpDeskStatusReportTableDTO
														.setMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalStatus(null);
												helpDeskStatusReportTableDTO
														.setZonalMedicalapprovedDateValue(null);
												helpDeskStatusReportTableDTO
														.setMedicalApprovedDateValue(null);
												helpDeskStatusReportTableDTO
														.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);

												if (ReferenceTable.BILLING_PROCESS_STAGE_KEY
														.equals(helpDeskStatusReportTableDTO
																.getStageKey())) {
													helpDeskStatusReportTableDTO
															.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);

													if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()))) {
														helpDeskStatusReportTableDTO
																.setFinancialCompletedDateValue(null);
													}
												} else {
													helpDeskStatusReportTableDTO
															.setFinancialStage(status
																	.getProcessValue());

													if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()))) {
														helpDeskStatusReportTableDTO
																.setFinancialCompletedDateValue(null);
													}
												}
											}

											else {
												if (!(SHAUtils
														.isDirectToFinancial(
																prauthObj,
																claimamountDetailsList))) {

													helpDeskStatusReportTableDTO
															.setFinancialStage(status
																	.getProcessValue());
													helpDeskStatusReportTableDTO
															.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);

													helpDeskStatusReportTableDTO
															.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
													helpDeskStatusReportTableDTO
															.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
													if (null != zonalDate) {
														helpDeskStatusReportTableDTO
																.setZonalMedicalApprovedDate(zonalDate
																		.getCreatedDate());
													}

													if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
															.equals(helpDeskStatusReportTableDTO
																	.getStatusKey()))) {
														helpDeskStatusReportTableDTO
																.setFinancialCompletedDateValue(null);
													}
												}
											}

										} else {
											helpDeskStatusReportTableDTO
													.setFinancialStage(status
															.getProcessValue());
											helpDeskStatusReportTableDTO
													.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
											helpDeskStatusReportTableDTO
													.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
											helpDeskStatusReportTableDTO
													.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
											if (null != zonalDate) {
												helpDeskStatusReportTableDTO
														.setZonalMedicalApprovedDate(zonalDate
																.getCreatedDate());
											}
											if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
													.equals(helpDeskStatusReportTableDTO
															.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
													.equals(helpDeskStatusReportTableDTO
															.getStatusKey()))) {
												helpDeskStatusReportTableDTO
														.setFinancialCompletedDateValue(null);
											}
											helpDeskStatusReportTableDTO
													.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
										}
									}
								}

							}
						}
						if (ReferenceTable.CREATE_ROD_STAGE_KEY
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {
							helpDeskStatusReportTableDTO
									.setRodScanStatus(SHAConstants.ROD_PENDING);
						} else {
							helpDeskStatusReportTableDTO
									.setRodScanStatus(SHAConstants.ROD_CREATED);
						}
					}

				}

				if (((ReferenceTable.REIMBURSEMENT_CLAIM)
						.equalsIgnoreCase(helpDeskStatusReportTableDTO
								.getClaimType()))) {

					if ((("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
							.getHospitalizationFlag()) || (("Y")
							.equalsIgnoreCase(helpDeskStatusReportTableDTO
									.getHospitalizationRepeatFlag())))
							|| ((("Y")
									.equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getHospitalizationFlag()))
									&& (("Y")
											.equalsIgnoreCase(helpDeskStatusReportTableDTO
													.getPreHospitalizationFlag())) && (("Y")
										.equalsIgnoreCase(helpDeskStatusReportTableDTO
												.getPostHospitalizationFlag())))) {

						if ((ReferenceTable.CREATE_ROD_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {

							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalapprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);

						}
						if ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())
								&& (ReferenceTable.BILL_ENTRY_STATUS_KEY
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {
							helpDeskStatusReportTableDTO
									.setMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setZonalMedicalapprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);

							if (!(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())
									&& !(ReferenceTable.BILL_ENTRY_STATUS_KEY)
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
							} else {
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
							}

						}

						if ((ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {
							if (!(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setBillinCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(status
												.getProcessValue());
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setMedicalApprovedDateValue(null);
								helpDeskStatusReportTableDTO
										.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
							} else {
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(status
												.getProcessValue());
							}
							if (!(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setMedicalApprovedDateValue(null);
							}
						}

						if ((ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {
							if (!(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setBillinCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setMedicalStatus(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}

								if ((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setBillinCompletedDate(helpDeskStatusReportTableDTO
													.getBillinCompletedDate());
									helpDeskStatusReportTableDTO
											.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
									if (null != zonalDate) {
										helpDeskStatusReportTableDTO
												.setZonalMedicalApprovedDate(zonalDate
														.getCreatedDate());
									}
									helpDeskStatusReportTableDTO
											.setMedicalStatus(status
													.getProcessValue());
									helpDeskStatusReportTableDTO
											.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);

								}

							} else {
								helpDeskStatusReportTableDTO
										.setMedicalStatus(status
												.getProcessValue());
							}
							if (!(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())
									&& (!(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey()))) {
								helpDeskStatusReportTableDTO
										.setMedicalApprovedDateValue(null);
							}

						}
						if ((ReferenceTable.BILLING_STAGE)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {

							if (!(ReferenceTable.BILLING_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setBillingStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}
								helpDeskStatusReportTableDTO
										.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
							}

							else {
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);

							}
							if (!(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setBillinCompletedDateValue(null);
								helpDeskStatusReportTableDTO
										.setBillingStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setBillingAmount(null);
							}

						}

						if ((ReferenceTable.FINANCIAL_STAGE)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {

							if (!(ReferenceTable.FINANCIAL_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())) {

								helpDeskStatusReportTableDTO
										.setFinancialStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}
							} else {
								helpDeskStatusReportTableDTO
										.setFinancialStage(status
												.getProcessValue());
							}

							if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey()))) {
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);
							}

						}

						if ((ReferenceTable.CREATE_ROD_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())
								&& (ReferenceTable.CREATE_ROD_STATUS_KEY
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {

							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalapprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
						}
					}

					if ((("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
							.getPreHospitalizationFlag())
							|| ("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
									.getPostHospitalizationFlag()) || ("Y")
								.equalsIgnoreCase(helpDeskStatusReportTableDTO
										.getAddOnBenifits()))
							&& !((("Y")
									.equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getHospitalizationFlag()))
									&& (("Y")
											.equalsIgnoreCase(helpDeskStatusReportTableDTO
													.getPreHospitalizationFlag())) && (("Y")
										.equalsIgnoreCase(helpDeskStatusReportTableDTO
												.getPostHospitalizationFlag())))) {

						if ((ReferenceTable.CREATE_ROD_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())
								&& (ReferenceTable.CREATE_ROD_STATUS_KEY
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {

							helpDeskStatusReportTableDTO
									.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
						}

						if ((ReferenceTable.BILL_ENTRY_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())
								&& (ReferenceTable.BILL_ENTRY_STATUS_KEY
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {

							helpDeskStatusReportTableDTO
									.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalapprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);

							if (!(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS)
									.equals(helpDeskStatusReportTableDTO
											.getStatusKey())
									&& !(ReferenceTable.BILL_ENTRY_STATUS_KEY)
											.equals(helpDeskStatusReportTableDTO
													.getStatusKey())) {
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_PENDING_STATUS);
							} else {
								helpDeskStatusReportTableDTO
										.setRodBillStatus(SHAConstants.BILL_ENTRY_COMPLETED_STATUS);
							}

						}

						if ((ReferenceTable.BILLING_STAGE)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {
							if (("Y")
									.equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getPreHospitalizationFlag())
									|| ("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getPostHospitalizationFlag())
									|| ("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getAddOnBenifits())) {

								helpDeskStatusReportTableDTO
										.setBillingStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setMedicalStatus(null);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(null);
								helpDeskStatusReportTableDTO
										.setZonalMedicalapprovedDateValue(null);
								helpDeskStatusReportTableDTO
										.setMedicalApprovedDateValue(null);
								helpDeskStatusReportTableDTO
										.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);

								if (!(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setBillinCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setBillingAmount(null);
								}

							} else {
								helpDeskStatusReportTableDTO
										.setBillingStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}
								helpDeskStatusReportTableDTO
										.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setFinancialCompletedDateValue(null);

								if (!(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey())) {
									helpDeskStatusReportTableDTO
											.setBillinCompletedDateValue(null);
									helpDeskStatusReportTableDTO
											.setBillingAmount(null);
								}
							}

						}

						if ((ReferenceTable.FINANCIAL_STAGE)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())) {
							if (("Y")
									.equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getPreHospitalizationFlag())
									|| ("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getPostHospitalizationFlag())
									|| ("Y").equalsIgnoreCase(helpDeskStatusReportTableDTO
											.getAddOnBenifits())) {
								helpDeskStatusReportTableDTO
										.setMedicalStatus(null);
								helpDeskStatusReportTableDTO
										.setMedicalApprovedDateValue(null);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(null);
								helpDeskStatusReportTableDTO
										.setZonalMedicalapprovedDateValue(null);
								helpDeskStatusReportTableDTO
										.setFinancialStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);

								if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);

								}

							} else {
								helpDeskStatusReportTableDTO
										.setFinancialStage(status
												.getProcessValue());
								helpDeskStatusReportTableDTO
										.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								helpDeskStatusReportTableDTO
										.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
								if (null != zonalDate) {
									helpDeskStatusReportTableDTO
											.setZonalMedicalApprovedDate(zonalDate
													.getCreatedDate());
								}

								if ((!(ReferenceTable.FINANCIAL_APPROVE_STATUS)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED)
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {
									helpDeskStatusReportTableDTO
											.setFinancialCompletedDateValue(null);
								}
							}
						}

						if ((ReferenceTable.CREATE_ROD_STAGE_KEY)
								.equals(helpDeskStatusReportTableDTO
										.getStageKey())
								&& (ReferenceTable.CREATE_ROD_STATUS_KEY
										.equals(helpDeskStatusReportTableDTO
												.getStatusKey()))) {

							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalapprovedDateValue(null);
						}

						if ((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
								.equals(helpDeskStatusReportTableDTO
										.getStatusKey())) {

							helpDeskStatusReportTableDTO
									.setMedicalStatus(status.getProcessValue());
							helpDeskStatusReportTableDTO
									.setBillingStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
							if (null != zonalDate) {
								helpDeskStatusReportTableDTO
										.setZonalMedicalApprovedDate(zonalDate
												.getCreatedDate());
							}
							helpDeskStatusReportTableDTO
									.setBillinCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);
							helpDeskStatusReportTableDTO.setBillingAmount(null);
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
						}

						if ((ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
								.equals(helpDeskStatusReportTableDTO
										.getStatusKey())) {
							helpDeskStatusReportTableDTO
									.setMedicalStatus(status.getProcessValue());
							helpDeskStatusReportTableDTO
									.setMedicalApprovedDateValue(null);
							helpDeskStatusReportTableDTO
									.setBillingStage(SHAConstants.APPROVED_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setFinancialStage(SHAConstants.PENDING_STAGE_STATUS);
							helpDeskStatusReportTableDTO
									.setFinancialCompletedDateValue(null);
							helpDeskStatusReportTableDTO
									.setZonalMedicalStatus(SHAConstants.APPROVED_STAGE_STATUS);
							if (null != zonalDate) {
								helpDeskStatusReportTableDTO
										.setZonalMedicalApprovedDate(zonalDate
												.getCreatedDate());
							}
						}

					}

				}

				if (ReferenceTable.CREATE_ROD_STAGE_KEY
						.equals(helpDeskStatusReportTableDTO.getStageKey())) {
					helpDeskStatusReportTableDTO
							.setRodScanStatus(SHAConstants.ROD_PENDING);
				} else {
					helpDeskStatusReportTableDTO
							.setRodScanStatus(SHAConstants.ROD_CREATED);
				}
			}
			
			hospitalDetail = null;
			queryReplyDateDetail = null;
			ack = null;
			zonalDate = null;
			status = null;
			prauthObj = null;
		}

		return tableDTO;
	}

	private List<HelpDeskStatusReportTableDTO> getTableDetailByClaim(
			List<HelpDeskStatusReportTableDTO> tableDTO1) {

		Hospitals hospitalDetail;
		RODDocumentSummary billDetail;
		DocAcknowledgement ack1;
		// List<Reimbursement> billStatus;
		for (HelpDeskStatusReportTableDTO helpDeskStatusReportTableDTO : tableDTO1) {

			hospitalDetail = getHospitalDetails(helpDeskStatusReportTableDTO
					.getHospitalId());
			if (hospitalDetail != null) {
				helpDeskStatusReportTableDTO.setHospitalName(hospitalDetail
						.getName());
				helpDeskStatusReportTableDTO.setHospitalCode(hospitalDetail
						.getHospitalCode());
				// tableDTO.get(index).setCpuCode(hospitalDetail.getCpuId());
				helpDeskStatusReportTableDTO.setHospitalType(hospitalDetail
						.getHospitalTypeName());

			}

			billDetail = getBillDetail(helpDeskStatusReportTableDTO.getKey());

			if (billDetail != null) {
				helpDeskStatusReportTableDTO.setBillingAmount(billDetail
						.getBillAmount());

			}

			// billStatus =
			// getMedicalDetails(helpDeskStatusReportTableDTO.getKey());
			//
			// if(billStatus != null && !billStatus.isEmpty())
			// {
			// Reimbursement bilstatus= billStatus.get(billStatus.size()-1);
			//
			// }

			if (null != hospitalType && hospitalType == 2) {
				helpDeskStatusReportTableDTO.setBillReceivedDateValue(null);
			}

			if ((null == hospitalType) && (null != fromDate || null != toDate)) {
				helpDeskStatusReportTableDTO.setBillReceivedDateValue(null);
			}

			ack1 = getOfcLocationDetail(helpDeskStatusReportTableDTO.getKey());

			if (ack1 != null) {
				if (null != ack1.getDocumentReceivedFromId()) {
					if ((ack1.getDocumentReceivedFromId().getValue())
							.equalsIgnoreCase("Hospital")) {
						helpDeskStatusReportTableDTO
								.setReceivedFrom(hospitalDetail.getName());
					}
				}
			}

		}

		return tableDTO1;
	}

	private List<Reimbursement> getMedicalDetails(Long claimKey) {
		List<Reimbursement> billStatus;

		Query findByClaimKey = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);
		try {
			billStatus = (List<Reimbursement>) findByClaimKey.getResultList();
			return billStatus;
		} catch (Exception e) {
			return null;
		}

	}

	private Status getStatusDetails(Long statusKey) {
		Status status;

		Query findByStatusKey = entityManager.createNamedQuery(
				"Status.findByKey").setParameter("statusKey", statusKey);
		try {
			status = (Status) findByStatusKey.getSingleResult();
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Preauth getPreauthDetails(Long claimKey) {
		Preauth preauthDetail = null;

		Query findByClaimKey = entityManager.createNamedQuery(
				"Preauth.findLatestPreauthByClaim").setParameter("claimkey",
				claimKey);
		try {
			List<Preauth> preauthList = (List<Preauth>) findByClaimKey
					.getResultList();
			if (null != preauthList && !preauthList.isEmpty()) {
				preauthDetail = preauthList.get(0);
				entityManager.refresh(preauthDetail);
			}
			return preauthDetail;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private List<Reimbursement> getBillReceivedDetails(Long claimKey) {
		List<Reimbursement> billStatus;
		Query findByClaimKey = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);
		try {
			billStatus = findByClaimKey.getResultList();
			return billStatus;
		} catch (Exception e) {
			return null;
		}

	}

	private List<Reimbursement> getFinancialCompletedListDetails() {
		List<Reimbursement> financialCompleted;
		Query findByStatus = entityManager
				.createNamedQuery("Reimbursement.findByStatusKey");
		try {
			financialCompleted = findByStatus.getResultList();
			return financialCompleted;
		} catch (Exception e) {
			return null;
		}

	}

	private Hospitals getHospitalDetails(Long hospitalId) {
		Hospitals hospitalDetail;
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		try {
			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			return hospitalDetail;
		} catch (Exception e) {
			return null;
		}

	}

	public List<ClaimAmountDetails> getClaimAmountDetailsByPreauth(
			Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<ClaimAmountDetails> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			for (ClaimAmountDetails claimAmountDetails : resultList) {
				entityManager.refresh(claimAmountDetails);
			}
		}

		return resultList;
	}

	private RODDocumentSummary getBillDetails(Long key) {
		RODDocumentSummary billDetail;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"RODDocumentSummary.findByReimbursementKey").setParameter(
				"reimbursementKey", key);
		try {
			billDetail = (RODDocumentSummary) findByReimbursementKey
					.getSingleResult();
			return billDetail;
		}

		catch (Exception e)

		{
			return null;
		}

	}

	private RODDocumentSummary getBillDetail(Long key) {
		RODDocumentSummary billDetail;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"RODDocumentSummary.findByKey").setParameter("primaryKey", key);
		try {
			billDetail = (RODDocumentSummary) findByReimbursementKey
					.getSingleResult();
			return billDetail;
		}

		catch (Exception e)

		{
			return null;
		}

	}

	private ReimbursementQuery getQueryReplyDateDetails(Long reimbursementKey) {
		List<ReimbursementQuery> location;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"ReimbursementQuery.findByReimbursement").setParameter(
				"reimbursementKey", reimbursementKey);
		try {
			location = (List<ReimbursementQuery>) findByReimbursementKey
					.getResultList();
			if (location != null && !location.isEmpty()) {
				entityManager.refresh(location.get(0));
				return location.get(0);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * private RodBillSummary getScanDetails(Long primaryKey) { RodBillSummary
	 * scan; String n = "IMS_SEC_ORGANIZATION_UNIT";
	 * 
	 * Query findByReimbursementKey = entityManager.createNamedQuery(
	 * "RodBillSummary.findByKey").setParameter("primaryKey", primaryKey); try{
	 * scan = (RodBillSummary) findByReimbursementKey.getResultList().get(0);
	 * return scan;
	 * 
	 * }catch(Exception e) { e.printStackTrace(); return null; } }
	 */

	private DocAcknowledgement getOfcLocationDetails(Long rodKey) {
		DocAcknowledgement ack = null;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"DocAcknowledgement.findByRODKey").setParameter("rodKey",
				rodKey);
		try {
			List<DocAcknowledgement> resultList = (List<DocAcknowledgement>) findByReimbursementKey
					.getResultList();
			List<DocAcknowledgement> ackList = resultList;
			if (null != ackList && !ackList.isEmpty()) {
				ack = ackList.get(0);
			}
			return ack;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private DocAcknowledgement getOfcLocationDetail(Long claimKey) {
		List<DocAcknowledgement> ack1;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"DocAcknowledgement.findByLatestClaimKey").setParameter(
				"claimkey", claimKey);
		try {
			ack1 = findByReimbursementKey.getResultList();
			if (!ack1.isEmpty())
				return ack1.get(0);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	private StageInformation getZonalApproveDate(Long claimKey) {
		List<StageInformation> zonalDate;

		Query findByClaimKey = entityManager
				.createNamedQuery("StageInformation.findStageUserByClaimKey");
		findByClaimKey.setParameter("claimKey", claimKey);
		findByClaimKey.setParameter("stageKey",
				ReferenceTable.ZONAL_REVIEW_STAGE);
		findByClaimKey.setParameter("statusKey",
				ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS);
		try {
			zonalDate = findByClaimKey.getResultList();
			if (null != zonalDate && !zonalDate.isEmpty())
				return zonalDate.get(0);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

}
