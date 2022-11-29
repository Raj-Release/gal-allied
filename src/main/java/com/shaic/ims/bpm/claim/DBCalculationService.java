package com.shaic.ims.bpm.claim;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang.StringUtils;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.MagazineDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTableDTO;
import com.shaic.claim.SumInsuredBonusAlertDTO;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.bpc.ViewBusinessProfilChartDTO;
import com.shaic.claim.coinsurance.view.CoInsuranceTableDTO;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditreport.ClaimsAuditTableDTO;
import com.shaic.claim.fss.filedetailsreport.FileDetailsReportTableDTO;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.omp.registration.OMPBalanceSiTableDTO;
import com.shaic.claim.omp.reports.outstandingreport.OmpStatusReportDto;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.pedrequest.view.ViewSeriousDeficiencyDTO;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.claim.policy.search.ui.Unique64VbstatusDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.productbenefit.view.PortablitiyPolicyDTO;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsuredDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveBariatricSurgeryTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveDeliveryNewBornTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHealthCheckTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHospitalCashTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveHospitalisationTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.ComprehensiveOutpatientTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.LumpSumTableDTO;
import com.shaic.claim.registration.balancesuminsured.view.PABalanceSumInsuredTableDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestTableDTO;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCStatusTableDTO;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.reimbursement.viewPaymentAudit.ViewPaymentAuditDTO;
import com.shaic.claim.reports.ackwithoutrodreport.AckWithoutRodTableDto;
import com.shaic.claim.reports.agentbrokerreport.AgentBrokerReportTableDTO;
import com.shaic.claim.reports.automationdashboard.AutomationDashboardTableDTO;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportDto;
import com.shaic.claim.reports.branchManagerFeedbackReportingPattern.BranchManagerFeedBackReportingPatternDto;
import com.shaic.claim.reports.callcenterDashBoard.CallcenterDashBoardReportDto;
import com.shaic.claim.reports.claimstatusreportnew.ClaimsStatusReportDto;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUWisePreauthResultDto;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportDto;
import com.shaic.claim.reports.dailyreport.DailyReportTableDTO;
import com.shaic.claim.reports.dispatchDetailsReport.DispatchDetailsReportTableDTO;
import com.shaic.claim.reports.finapprovednotsettledreport.FinApprovedPaymentPendingReportDto;
import com.shaic.claim.reports.fvrassignmentreport.FVRAssignmentReportTableDTO;
import com.shaic.claim.reports.investigationassignedreport.InvAssignStatusReportDto;
import com.shaic.claim.reports.marketingEscalationReport.MarketingEscalationReportTableDTO;
import com.shaic.claim.reports.notAdheringToANHReport.NewIntimationNotAdheringToANHDto;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportTableDTO;
import com.shaic.claim.reports.paymentpendingdashboard.PaymentPendingDashboardTableDTO;
import com.shaic.claim.reports.productivityreport.ProductivityReportTableDTO;
import com.shaic.claim.reports.provisionhistory.ProvisionHistoryDTO;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.reports.tatreport.TATReportTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ComprehensiveSublimitDTO;
import com.shaic.domain.MasHospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.Stage;
import com.shaic.feedback.managerfeedback.BranchManagerFeedbackhomePageDto;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.feedback.managerfeedback.ReviewStatsDto;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackTableDTO;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PendingLotBatchReportDto;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.BeanItemContainer;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

@Stateless
public class DBCalculationService {

	// public List<SublimitFunObject> getClaimedAmountDetails(long productKey,
	// Double sumInsured, Double age) {
	// final String typeName = "OBJ_SUB_LIM";
	// final String typeTableName = "TYP_SUB_LIM";
	//
	// Connection connection = null;
	// try {
	// connection = BPMClientContext.getConnection();
	// CallableStatement cs = connection
	// .prepareCall("{call PRC_SUB_LIMIT(?, ?, ?, ?, ? , ?)}");
	// cs.setLong(1, productKey);
	// cs.setDouble(2, sumInsured);
	// cs.setDouble(3, age != null ? age : 0d)
	// cs.setLong(4,0l);
	// cs.setString(5, "0");
	// // Result is an java.sql.Array...
	// cs.registerOutParameter(6, Types.ARRAY, typeTableName);
	// cs.execute();
	//
	// Object[] data = (Object[]) ((Array) cs.getObject(6)).getArray();
	// List<SublimitFunObject> subLimits = new ArrayList<SublimitFunObject>();
	// for (Object tmp : data) {
	// Struct row = (Struct) tmp;
	// Object[] attributes = row.getAttributes();
	// Double amount = Double.valueOf("" + attributes[0]);
	// String name = String.valueOf(attributes[1]);
	// String description = String.valueOf(attributes[2]);
	// Long limitId = Long.valueOf(String.valueOf(attributes[3]));
	//
	// System.out.println("---the sublimit calculation---amount--"
	// + amount + "--name---" + name + "----description---"
	// + description + "---limit id---" + limitId);
	//
	// subLimits.add(new SublimitFunObject(name, description, amount,
	// limitId));
	//
	// }
	// System.out.println("---sublimt list ----" + subLimits);
	//
	// return subLimits;
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	// Backup for before implementing the section, cover, subcover
	// functionality...
	// public List<SublimitFunObject> getClaimedAmountDetailsForSection(long
	// productKey,
	// Double sumInsured, Double age, Long section, String plan) {
	// final String typeName = "OBJ_SUB_LIM";
	// final String typeTableName = "TYP_SUB_LIM";
	// if(section == null) {
	// section = 0l;
	// }
	// Long sectionValue = 0l;
	//
	// if(section.equals(ReferenceTable.POL_SECTION_1)){
	// sectionValue = 1l;
	// }else if(section.equals(ReferenceTable.POL_SECTION_2)){
	// sectionValue = 2l;
	// }
	//
	// Connection connection = null;
	// CallableStatement cs =null;
	// List<SublimitFunObject> subLimits = new ArrayList<SublimitFunObject>();
	// try {
	// connection = BPMClientContext.getConnection();
	// cs = connection
	// .prepareCall("{call PRC_SUB_LIMIT(?, ?, ?, ?, ? , ?)}");
	// cs.setLong(1, productKey);
	// cs.setDouble(2, sumInsured);
	// cs.setDouble(3, age != null ? age : 0d);
	// cs.setLong(4,sectionValue);
	// cs.setString(5, sectionValue.equals(0l) ? "0" : ((plan != null) ? plan :
	// "A"));
	// // Result is an java.sql.Array...
	// cs.registerOutParameter(6, Types.ARRAY, typeTableName);
	// cs.execute();
	//
	// Object[] data = (Object[]) ((Array) cs.getObject(6)).getArray();
	//
	// for (Object tmp : data) {
	// Struct row = (Struct) tmp;
	// Object[] attributes = row.getAttributes();
	// Double amount = Double.valueOf("" + attributes[0]);
	// String name = String.valueOf(attributes[1]);
	// String description = String.valueOf(attributes[2]);
	// Long limitId = Long.valueOf(String.valueOf(attributes[3]));
	//
	// // System.out.println("---the sublimit calculation---amount--"
	// // + amount + "--name---" + name + "----description---"
	// // + description + "---limit id---" + limitId);
	//
	// subLimits.add(new SublimitFunObject(name, description, amount,
	// limitId));
	// // if(section.equals(468l)){
	// // break;
	// // }
	// }
	// // System.out.println("---sublimt list ----" + subLimits);
	//
	//
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return subLimits;
	// }

	public List<OtherBenefitsTableDto> getOtherBebefitsList(Long productKey,
			Long sumInsured, Long claimTypeId) {
		final String typeTableName = "TYP_FHO_BENEFIT_AMT";

		Connection connection = null;
		CallableStatement cs = null;
		List<OtherBenefitsTableDto> otherBenefitsListDto = new ArrayList<OtherBenefitsTableDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_FHO_BENEFIT_AMT(?, ?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setDouble(2, sumInsured);
			cs.setDouble(3, claimTypeId);
			cs.registerOutParameter(4, Types.ARRAY, typeTableName);
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(4)).getArray();
			Struct row = null;
			int i = 0;
			for (Object tmp : data) {
				row = (Struct) tmp;
				Object[] attributes = row.getAttributes();
				i++;
				String name = String.valueOf(attributes[0]);
				Double eligibleAmt = Double.valueOf("" + attributes[1]);
				Long benefitId = Long.valueOf("" + attributes[2]);

				otherBenefitsListDto.add(new OtherBenefitsTableDto(i,
						benefitId, name, eligibleAmt));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return otherBenefitsListDto;

	}

	public List<SublimitFunObject> getClaimedAmountDetailsForSection(
			long productKey, Double sumInsured, Long siRestric, Double age, Long section,
			String plan, String subCoverCode, Long insuredNumber) {
		// final String typeName = "OBJ_SUB_LIM";
		final String typeTableName = "TYP_SUB_LIM";
		if (section == null) {
			section = 0l;
		}
		Long sectionValue = 0l;

		if (section.equals(ReferenceTable.POL_SECTION_1)) {
			sectionValue = 1l;
		} else if (section.equals(ReferenceTable.POL_SECTION_2)) {
			sectionValue = 2l;
		}

		if (ReferenceTable.getSuperSurplusKeys().containsKey(productKey)
				&& subCoverCode != null
				&& subCoverCode
						.equalsIgnoreCase(ReferenceTable.MATERNITY_CEASEAREAN_SUB_COVER_CODE)) {
			subCoverCode = ReferenceTable.MATERNITY_NORMAL_SUB_COVER_CODE;
		}

		Connection connection = null;
		CallableStatement cs = null;
		List<SublimitFunObject> subLimits = new ArrayList<SublimitFunObject>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
//					.prepareCall("{call PRC_SUB_LIMIT(?, ?, ?, ?, ? , ?, ?, ?)}");
					
					.prepareCall("{call PRC_SUB_LIMIT(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setDouble(2, sumInsured);
			cs.setDouble(3, age != null ? age : 0d);
			cs.setLong(4, sectionValue);
			/*
			 * cs.setString(5, sectionValue.equals(0l) ? "0" : ((plan != null) ?
			 * plan : "A"));
			 */
			cs.setString(5, plan != null ? plan : "A");
			cs.setString(6, subCoverCode != null ? subCoverCode
					: ReferenceTable.HOSP_SUB_COVER_CODE);
			cs.setLong(7, insuredNumber);
			cs.setLong(8, siRestric != null ? siRestric : 0l);
			
			// Result is an java.sql.Array...
			cs.registerOutParameter(9, Types.ARRAY, typeTableName);
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(9)).getArray();
			Struct row = null;
			for (Object tmp : data) {
				row = (Struct) tmp;
				Object[] attributes = row.getAttributes();
				Double amount = Double.valueOf("" + attributes[0]);
				String name = String.valueOf(attributes[1]);
				String description = String.valueOf(attributes[2]);
				Long limitId = Long.valueOf(String.valueOf(attributes[3]));

				// System.out.println("---the sublimit calculation---amount--"
				// + amount + "--name---" + name + "----description---"
				// + description + "---limit id---" + limitId);

				subLimits.add(new SublimitFunObject(name, description, amount,
						limitId));
				// if(section.equals(468l)){
				// break;
				// }
			}
			// System.out.println("---sublimt list ----" + subLimits);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return subLimits;
	}

	// public List<SublimitFunObject> getSublimitUtilization(Long insuredNumber
	// , Long productKey ,Double age, Long section, String plan , Long claimKey
	// )
	// {
	// Connection connection = null;
	// CallableStatement cs =null;
	// List<SublimitFunObject> subLimits = new ArrayList<SublimitFunObject>();
	// try {
	// final String typeName = "OBJ_SUB_LIM";
	// final String typeTableName = "TYP_VW_SUB_LIM";
	//
	// if(section == null) {
	// section = 0l;
	// }
	// Long sectionValue = 0l;
	//
	// if(section.equals(ReferenceTable.POL_SECTION_1)){
	// sectionValue = 1l;
	// }else if(section.equals(ReferenceTable.POL_SECTION_2)){
	// sectionValue = 2l;
	// }
	//
	// connection = BPMClientContext.getConnection();
	// cs = connection
	// .prepareCall("{call PRC_VIEW_SUB_LMT_CALC(?, ?, ?,?,?,?,?)}");
	// cs.setLong(1, insuredNumber);
	// cs.setLong(2, productKey);
	// cs.setDouble(3, age != null ? age : 0d);
	// cs.setLong(4, section);
	// cs.setString(5, sectionValue.equals(0l) ? "0" : ((plan != null) ? plan :
	// "A"));
	// cs.setLong(6, claimKey);
	// cs.registerOutParameter(7, Types.ARRAY, typeTableName);
	// cs.execute();
	//
	//
	// Object object = cs.getObject(7);
	//
	// if(object != null) {
	// Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();
	//
	// for (Object tmp : data) {
	// Struct row = (Struct) tmp;
	// Object[] attributes = row.getAttributes();
	// Long limitIdNumber = null != attributes[0] ?
	// Long.valueOf(String.valueOf(attributes[0])) : null;
	// String name = null != attributes[1] ? String.valueOf(attributes[1]) :
	// null;
	// Double limitAmnt = null != attributes[2] ? Double.valueOf("" +
	// attributes[2]) : null;
	// Double limittotalUtilizationAmnt = null != attributes[3] ?
	// Double.valueOf("" + attributes[3]) : 0d;
	// Double limitUtilizationAmnt = null != attributes[4] ? Double.valueOf("" +
	// attributes[4]) : 0d;
	//
	// subLimits.add(new
	// SublimitFunObject(limitIdNumber,name,limitAmnt,limittotalUtilizationAmnt,limitUtilizationAmnt));
	// }
	// }
	// // System.out.println("---sublimt list ----" + subLimits);
	//
	//
	//
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return subLimits;
	//
	// }

	public List<ComprehensiveSublimitDTO> getSublimitUtilizationBasedOnProduct(
			Long insuredNumber, Long productKey, Double age, Long section,
			String plan, Long claimKey) {
		Connection connection = null;
		CallableStatement cs = null;
		List<ComprehensiveSublimitDTO> subLimits = new ArrayList<ComprehensiveSublimitDTO>();
		try {
			// final String typeName = "OBJ_VW_SUB_LIM1";
			final String typeTableName = "TYP_VW_SUB_LIM1";

			Long sectionValue = 0l;
			if (section == null) {
				sectionValue = 0l;
			}

			if (section.equals(ReferenceTable.POL_SECTION_1)) {
				sectionValue = 1l;
			} else if (section.equals(ReferenceTable.POL_SECTION_2)) {
				sectionValue = 2l;
			}
			
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_VIEW_SUB_LMT_CALC(?, ?, ?,?,?,?,?)}");
			cs.setLong(1, insuredNumber);
			cs.setLong(2, productKey);
			cs.setDouble(3, age != null ? age : 0d);
			cs.setLong(4, section);
			/*cs.setString(5, sectionValue.equals(0l) ? "0"
					: ((plan != null) ? plan : "A"));*/
			
			//JIRA - GALAXYMAIN-13129  fixed on 11-02-2020
			if(!ReferenceTable.getSectionKeys().containsKey(productKey)){
				cs.setString(5, plan != null ? plan : "A");
			} else {
				cs.setString(5, sectionValue.equals(0l) ? "0"
					: ((plan != null) ? plan : "A"));
			}
			
			cs.setLong(6, claimKey);
			cs.registerOutParameter(7, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(7);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();
				Struct row = null;
				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					if (null != row) {
						attributes = row.getAttributes();
						Long limitIdNumber = null != attributes[0] ? Long
								.valueOf(String.valueOf(attributes[0])) : null;
						String name = null != attributes[1] ? String
								.valueOf(attributes[1]) : null;
						Double limitAmnt = null != attributes[2] ? Double
								.valueOf("" + attributes[2]) : null;
						Double limittotalUtilizationAmnt = null != attributes[3] ? Double
								.valueOf("" + attributes[3]) : 0d;
						Double limitUtilizationAmnt = null != attributes[4] ? Double
								.valueOf("" + attributes[4]) : 0d;
						String secName = null != attributes[5] ? String
								.valueOf(attributes[5]) : null;

						subLimits.add(new ComprehensiveSublimitDTO(
								limitIdNumber, name, limitAmnt,
								limittotalUtilizationAmnt,
								limitUtilizationAmnt, secName));
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return subLimits;

	}

	/*
	 * public List<TATReportTableDTO> getCompletedClaimsTat(java.sql.Date
	 * fromDate,java.sql.Date toDate,String dateTypeValue, Long cpuCode,Long
	 * officeCode) { final String TAT_COMPLETED_CLAIMS =
	 * "{call  PKG_TAT_CLAIMS_RPT.PRC_RPT_FA_APPR_TAT(?,?,?,?,?,?)}";
	 * 
	 * Connection connection = null; CallableStatement cs =null; ResultSet rset
	 * = null; List<TATReportTableDTO> tatpendingClaims = new
	 * ArrayList<TATReportTableDTO>(); try { connection =
	 * BPMClientContext.getConnection(); cs =
	 * connection.prepareCall(TAT_COMPLETED_CLAIMS); cs.setDate(1, fromDate);
	 * cs.setDate(2, toDate); cs.setString(3,dateTypeValue); cs.setLong(4, null
	 * != cpuCode ? cpuCode : 0l); cs.setLong(5, null != officeCode ? officeCode
	 * : 0l);
	 * 
	 * cs.registerOutParameter(6, OracleTypes.CURSOR); cs.execute(); rset =
	 * (ResultSet) cs.getObject(6);
	 * 
	 * if(null != rset) { while (rset.next()) {
	 * 
	 * TATReportTableDTO tatDTO = new TATReportTableDTO();
	 * 
	 * String intimationNo = rset.getString("INTIMATION_NUMBER"); String
	 * claimType = rset.getString("CLM_TYPE"); String rodNo =
	 * rset.getString("ROD_NO"); String cpu = rset.getString("CPU"); String
	 * ackDate= rset.getString("ACK_DATE"); String latAckDate =
	 * rset.getString("LAT_ACK_DT"); String rodDate =
	 * rset.getString("ROD_DATE"); String zonalApproveDate =
	 * rset.getString("ZONAL_APR_DT"); String maApproveDate =
	 * rset.getString("MA_APR_DT"); String billApproveDate =
	 * rset.getString("BILL_APR_DT"); String faApproveDate =
	 * rset.getString("FA_APR_DT"); String settleDate =
	 * rset.getString("SETL_DT"); Integer rodDelay = rset.getInt("ROD_DELAY");
	 * Integer zonalDelay = rset.getInt("ZONAL_DELAY"); Integer maDelay =
	 * rset.getInt("MEDICAL_DELAY"); Integer billingDelay =
	 * rset.getInt("BILLING_DELAY"); Integer faDelay = rset.getInt("FA_DELAY");
	 * Integer totalDelay = rset.getInt("TOTAL_DELAY"); Integer
	 * delayExcludingSunday = rset.getInt("DELAY_EXCLUDING_SUNDAY"); String
	 * status = rset.getString("STATUS");
	 * 
	 * 
	 * tatDTO.setIntimationNo(intimationNo); tatDTO.setClaimType(claimType);
	 * tatDTO.setRodNo(rodNo); tatDTO.setCpu(cpu); tatDTO.setAckDate(ackDate);
	 * tatDTO.setLatAckDate(latAckDate); tatDTO.setRodDate(rodDate);
	 * tatDTO.setZonalApprovalDate(zonalApproveDate);
	 * tatDTO.setMaApproveDate(maApproveDate);
	 * tatDTO.setBillApproveDate(billApproveDate);
	 * tatDTO.setFaApprovedDate(faApproveDate);
	 * tatDTO.setSettledDate(settleDate); tatDTO.setRodDelay(rodDelay);
	 * tatDTO.setZonalDelay(zonalDelay); tatDTO.setMedicalDelay(maDelay);
	 * tatDTO.setBillingDelay(billingDelay); tatDTO.setFaDelay(faDelay);
	 * tatDTO.setTotalDelay(totalDelay);
	 * tatDTO.setDelayExcludingSunday(delayExcludingSunday);
	 * tatDTO.setStatus(status);
	 * 
	 * 
	 * 
	 * tatpendingClaims.add(tatDTO); } } } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { if (connection != null) {
	 * connection.close(); } if (cs != null) { cs.close(); } } catch
	 * (SQLException e) { e.printStackTrace(); } } return tatpendingClaims;
	 * 
	 * }
	 */

	/*
	 * public List<TATReportTableDTO> getPendingClaimsTat(java.sql.Date
	 * fromDate,java.sql.Date toDate) {
	 * 
	 * final String TAT_PENDING_CLAIMS =
	 * "{call  PKG_TAT_CLAIMS_RPT.PRC_FA_NOT_APPR_TAT(?,?,?)}"; Connection
	 * connection = null; CallableStatement cs =null; ResultSet rset = null;
	 * List<TATReportTableDTO> tatpendingClaims = new
	 * ArrayList<TATReportTableDTO>(); try { connection =
	 * BPMClientContext.getConnection(); cs =
	 * connection.prepareCall(TAT_PENDING_CLAIMS); cs.setDate(1, fromDate);
	 * cs.setDate(2, toDate); //cs.setLong(3, null != cpuCode ? cpuCode : 0l);
	 * //cs.setLong(4, null != officeCode ? officeCode : 0l);
	 * 
	 * 
	 * cs.registerOutParameter(3, OracleTypes.CURSOR); cs.execute(); rset =
	 * (ResultSet) cs.getObject(3);
	 * 
	 * if(null != rset) { while (rset.next()) {
	 * 
	 * TATReportTableDTO tatDTO = new TATReportTableDTO();
	 * 
	 * String intimationNo = rset.getString("INTIMATION_NUMBER"); String
	 * claimType = rset.getString("CLM_TYPE"); String rodNo =
	 * rset.getString("ROD_NO"); String cpu = rset.getString("CPU"); String
	 * ackDate= rset.getString("ACK_DATE"); String latAckDate =
	 * rset.getString("LAT_ACK_DT"); String rodDate =
	 * rset.getString("ROD_DATE"); String zonalApproveDate =
	 * rset.getString("ZONAL_DT"); String maApproveDate =
	 * rset.getString("MA_APR_DT"); String billApproveDate =
	 * rset.getString("BILL_APR_DT"); String faApproveDate =
	 * rset.getString("FA_APR_DT"); Integer totalDelay =
	 * rset.getInt("TOTAL_DELAY"); String status = rset.getString("STATUS");
	 * String userQueue = rset.getString("USER_QUEUE"); Integer currentDelay =
	 * rset.getInt("CURRENT_DELAY");
	 * 
	 * tatDTO.setIntimationNo(intimationNo); tatDTO.setClaimType(claimType);
	 * tatDTO.setRodNo(rodNo); tatDTO.setCpu(cpu); tatDTO.setAckDate(ackDate);
	 * tatDTO.setLatAckDate(latAckDate); tatDTO.setRodDate(rodDate);
	 * tatDTO.setZonalApprovalDate(zonalApproveDate);
	 * tatDTO.setMaApproveDate(maApproveDate);
	 * tatDTO.setBillApproveDate(billApproveDate);
	 * tatDTO.setFaApprovedDate(faApproveDate);
	 * tatDTO.setTotalDelay(totalDelay); tatDTO.setStatus(status);
	 * tatDTO.setUserQueue(userQueue); tatDTO.setCurrentDelay(currentDelay);
	 * 
	 * 
	 * tatpendingClaims.add(tatDTO); } } } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { if (connection != null) {
	 * connection.close(); } if (cs != null) { cs.close(); } } catch
	 * (SQLException e) { e.printStackTrace(); } } return tatpendingClaims;
	 * 
	 * }
	 */

	/*
	 * public List<TATReportTableDTO> getPendingClaimsTat(java.sql.Date
	 * fromDate,java.sql.Date toDate,Long cpuCode,Long officeCode) { final
	 * String TAT_PENDING_CLAIMS =
	 * "{call  PKG_TAT_CLAIMS_REPORT.PRC_FA_NOT_APPR_TAT(?,?,?,?,?)}";
	 * 
	 * Connection connection = null; CallableStatement cs =null; ResultSet rset
	 * = null; List<TATReportTableDTO> tatpendingClaims = new
	 * ArrayList<TATReportTableDTO>(); try { connection =
	 * BPMClientContext.getConnection(); cs =
	 * connection.prepareCall(TAT_PENDING_CLAIMS); cs.setDate(1, fromDate);
	 * cs.setDate(2, toDate); cs.setLong(3, null != cpuCode ? cpuCode :0l);
	 * cs.setLong(4, null != officeCode ? officeCode : 0l);
	 * 
	 * cs.registerOutParameter(5, OracleTypes.CURSOR); cs.execute(); rset =
	 * (ResultSet) cs.getObject(5);
	 * 
	 * if(null != rset) { while (rset.next()) {
	 * 
	 * TATReportTableDTO tatDTO = new TATReportTableDTO();
	 * 
	 * String intimationNo = rset.getString("INTIMATION_NUMBER"); String
	 * claimType = rset.getString("CLM_TYPE"); String rodNo =
	 * rset.getString("ROD_NO"); String cpu = rset.getString("CPU"); String
	 * ackDate= rset.getString("ACK_DATE"); String latAckDate =
	 * rset.getString("LAT_ACK_DT"); String rodDate =
	 * rset.getString("ROD_DATE"); String zonalApproveDate =
	 * rset.getString("ZONAL_DT"); String maApproveDate =
	 * rset.getString("MA_APR_DT"); String billApproveDate =
	 * rset.getString("BILL_APR_DT"); String faApproveDate =
	 * rset.getString("FA_APR_DT"); Integer totalDelay =
	 * rset.getInt("TOTAL_DELAY"); String status = rset.getString("STATUS");
	 * String userQueue = rset.getString("USER_QUEUE"); Integer currentDelay =
	 * rset.getInt("CURRENT_DELAY");
	 * 
	 * tatDTO.setIntimationNo(intimationNo); tatDTO.setClaimType(claimType);
	 * tatDTO.setRodNo(rodNo); if(null != cpu) { tatDTO.setCpu(cpu); }
	 * tatDTO.setAckDate(ackDate); tatDTO.setLatAckDate(latAckDate);
	 * tatDTO.setRodDate(rodDate);
	 * tatDTO.setZonalApprovalDate(zonalApproveDate);
	 * tatDTO.setMaApproveDate(maApproveDate);
	 * tatDTO.setBillApproveDate(billApproveDate);
	 * tatDTO.setFaApprovedDate(faApproveDate);
	 * tatDTO.setTotalDelay(totalDelay); tatDTO.setStatus(status);
	 * tatDTO.setUserQueue(userQueue); tatDTO.setCurrentDelay(currentDelay);
	 * 
	 * 
	 * tatpendingClaims.add(tatDTO); } } } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { if (connection != null) {
	 * connection.close(); } if (cs != null) { cs.close(); } } catch
	 * (SQLException e) { e.printStackTrace(); } } return tatpendingClaims;
	 * 
	 * }
	 */

	// public List<TATReportTableDTO> getCompletedClaimsTat(java.sql.Date
	// fromDate,java.sql.Date toDate,String dateTypeValue, Long cpuCode,Long
	// officeCode,String tatDateValue)
	public List<TATReportTableDTO> getCompletedClaimsTat(
			java.sql.Date fromDate, java.sql.Date toDate, String dateTypeValue,
			Long cpuCode, Long officeCode, String tatDateValue, String userId,
			Long clmType) {
		// final String TAT_COMPLETED_CLAIMS =
		// "{call  PKG_TAT_CLAIMS_RPT.PRC_RPT_FA_APPR_TAT(?,?,?,?,?,?,?)}";

		final String TAT_COMPLETED_CLAIMS = "{call  PRC_FA_APPROVED_TAT_LST(?,?,?,?,?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<TATReportTableDTO> tatpendingClaims = new ArrayList<TATReportTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(TAT_COMPLETED_CLAIMS);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			if (SHAConstants.TYPE_APPROVED.equalsIgnoreCase(dateTypeValue)) {
				cs.setString(3, SHAConstants.TAT_PARAM_FINANCIAL_APPROVED);
			} else if (SHAConstants.TYPE_PAID.equalsIgnoreCase(dateTypeValue)) {
				cs.setString(3, SHAConstants.TAT_PARAM_PAID_DATE);
			}
			// cs.setString(3, dateTypeValue);

			/*
			 * if(null != cpuCodeList && !cpuCodeList.isEmpty()){ String
			 * cpuCodeValue = ""; for(Long cpuCode : cpuCodeList){ cpuCodeValue
			 * = !cpuCodeValue.isEmpty() ? cpuCodeValue + "," +
			 * String.valueOf(cpuCode) : String.valueOf(cpuCode); }
			 * cs.setString(4,cpuCodeValue); }
			 */
			cs.setLong(4, null != cpuCode ? cpuCode : 0l);
			/*
			 * else{ cs.setString(4," "); }
			 */
			cs.setLong(5, null != officeCode ? officeCode : 0l);

			if (tatDateValue != null) {
				switch (tatDateValue) {
				case SHAConstants.DAYS_0_8:
					cs.setInt(6, 0);
					cs.setInt(7, 8);
					break;
				case SHAConstants.DAYS_9_15:
					cs.setInt(6, 9);
					cs.setInt(7, 15);
					break;

				case SHAConstants.DAYS_7:
					cs.setInt(6, 7);
					cs.setInt(7, 1000);
					break;
				case SHAConstants.DAYS_20:
					cs.setInt(6, 20);
					cs.setInt(7, 1000);
					break;
				case SHAConstants.DAYS_16_21:
					cs.setInt(6, 16);
					cs.setInt(7, 21);
					break;
				case SHAConstants.DAYS_21_30:
					cs.setInt(6, 21);
					cs.setInt(7, 30);
					break;
				case SHAConstants.DAYS_30:
					cs.setInt(6, 30);
					cs.setInt(7, 1000);
					break;
				}
			} else {
				/*****
				 * As per Raju's suggestion if the tat Days was not selected the
				 * paasing days values
				 * 
				 */
				cs.setInt(6, -10000);
				cs.setInt(7, 10000);
			}

			cs.setString(8, userId);
			cs.setLong(9, null != clmType ? clmType : 0l);
			cs.registerOutParameter(10, OracleTypes.CURSOR);
			cs.execute();
			rset = (ResultSet) cs.getObject(10);

			if (null != rset) {
				TATReportTableDTO tatDTO = null;

				while (rset.next()) {

					tatDTO = new TATReportTableDTO();

					/**********************************************************************************************
					 * String intimationNo =
					 * rset.getString("INTIMATION_NUMBER"); String claimType =
					 * rset.getString("CLM_TYPE"); String rodNo =
					 * rset.getString("ROD_NO"); String cpu =
					 * rset.getString("CPU"); String ackDate=
					 * rset.getString("ACK_DATE"); String latAckDate =
					 * rset.getString("LAT_ACK_DT"); String rodDate =
					 * rset.getString("ROD_DATE"); String zonalApproveDate =
					 * rset.getString("ZONAL_APR_DT"); String maApproveDate =
					 * rset.getString("MA_APR_DT"); String billApproveDate =
					 * rset.getString("BILL_APR_DT"); String faApproveDate =
					 * rset.getString("FA_APR_DT"); String settleDate =
					 * rset.getString("SETL_DT"); Integer rodDelay =
					 * rset.getInt("ROD_DELAY"); Integer zonalDelay =
					 * rset.getInt("ZONAL_DELAY"); Integer maDelay =
					 * rset.getInt("MEDICAL_DELAY"); Integer billingDelay =
					 * rset.getInt("BILLING_DELAY"); Integer faDelay =
					 * rset.getInt("FA_DELAY"); Integer totalDelay =
					 * rset.getInt("TOTAL_DELAY"); Integer delayExcludingSunday
					 * = rset.getInt("DELAY_EXCLUDING_SUNDAY"); String status =
					 * rset.getString("STATUS");
					 * 
					 * 
					 * tatDTO.setIntimationNo(intimationNo);
					 * tatDTO.setClaimType(claimType); tatDTO.setRodNo(rodNo);
					 * tatDTO.setCpu(cpu); tatDTO.setAckDate(ackDate);
					 * tatDTO.setLatAckDate(latAckDate);
					 * tatDTO.setRodDate(rodDate);
					 * tatDTO.setZonalApprovalDate(zonalApproveDate);
					 * tatDTO.setMaApproveDate(maApproveDate);
					 * tatDTO.setBillApproveDate(billApproveDate);
					 * tatDTO.setFaApprovedDate(faApproveDate);
					 * tatDTO.setSettledDate(settleDate);
					 * tatDTO.setRodDelay(rodDelay);
					 * tatDTO.setZonalDelay(zonalDelay);
					 * tatDTO.setMedicalDelay(maDelay);
					 * tatDTO.setBillingDelay(billingDelay);
					 * tatDTO.setFaDelay(faDelay);
					 * tatDTO.setTotalDelay(totalDelay);
					 * tatDTO.setDelayExcludingSunday(delayExcludingSunday);
					 * tatDTO.setStatus(status);
					 ***********************************************************************************************/

					tatDTO.setIntimationNo(rset.getString("INTIMATION_NUMBER"));
					tatDTO.setClaimType(rset.getString("CLM_TYPE"));
					tatDTO.setRodNo(rset.getString("ROD_NO"));
					Long cpuCodeValue = rset.getLong("CPU_CODE");
					if (cpuCodeValue != null) {
						tatDTO.setCpuCode(String.valueOf(cpuCodeValue));
					}
					tatDTO.setCpu(rset.getString("CPU"));
					tatDTO.setClaimedAmt(rset.getDouble("CLAIMED_AMOUNT"));
					tatDTO.setAckDate(rset.getString("ACK_DATE"));
					tatDTO.setLatAckDate(rset.getString("LAT_ACK_DT"));
					tatDTO.setRodDate(rset.getString("ROD_DATE"));
					tatDTO.setZonalApprovalDate(rset.getString("ZONAL_APR_DT"));
					tatDTO.setMaApproveDate(rset.getString("MA_APR_DT"));
					tatDTO.setBillApproveDate(rset.getString("BILL_APR_DT"));
					tatDTO.setFaApprovedDate(rset.getString("FA_APR_DT"));
					tatDTO.setSettledDate(rset.getString("SETL_DT"));
					tatDTO.setRodDelay(rset.getInt("ROD_DELAY"));
					tatDTO.setZonalDelay(rset.getInt("ZONAL_DELAY"));
					tatDTO.setMedicalDelay(rset.getInt("MEDICAL_DELAY"));
					tatDTO.setBillingDelay(rset.getInt("BILLING_DELAY"));
					tatDTO.setFaDelay(rset.getInt("FA_DELAY"));
					tatDTO.setTotalDelay(rset.getInt("TOTAL_DURATION"));
					tatDTO.setDelayExcludingSunday(rset
							.getInt("DELAY_EXCLUDING_SUNDAY"));
					tatDTO.setStatus(rset.getString("STATUS"));
					tatDTO.setDocRecvdFrom(rset
							.getString("DOCUMENT_RECEVIED_FROM"));
					tatDTO.setHospName(rset.getString("HOSPITAL_NAME"));
					tatDTO.setHospCity(rset.getString("HOSPITAL_CITY"));
					tatDTO.setClaimedBillCompletedAmt(rset
							.getDouble("Claimed_Bill_Completed_Amnt"));
					tatDTO.setFAApprovedAmt(rset
							.getDouble("FA_Approved_Amount"));

					tatpendingClaims.add(tatDTO);
					tatDTO = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return tatpendingClaims;

	}

	public List<TATReportTableDTO> getPendingClaimsTat(java.sql.Date fromDate,
			java.sql.Date toDate, Long cpuCode, Long officeCode,
			String tatDateValue, String userID, Long clmType) {
		final String TAT_PENDING_CLAIMS = "{call  PKG_TAT_CLAIMS_RPT.PRC_FA_NOT_APPR_TAT(?,?,?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<TATReportTableDTO> tatpendingClaims = new ArrayList<TATReportTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(TAT_PENDING_CLAIMS);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, null != cpuCode ? cpuCode : 0l);
			cs.setLong(4, null != officeCode ? officeCode : 0l);
			cs.setString(5, tatDateValue);
			cs.setString(6, userID);
			cs.setLong(7, null != clmType ? clmType : 0l);
			cs.registerOutParameter(8, OracleTypes.CURSOR);
			cs.execute();
			rset = (ResultSet) cs.getObject(8);

			if (null != rset) {
				TATReportTableDTO tatDTO = null;

				while (rset.next()) {

					tatDTO = new TATReportTableDTO();

					/**********************************************************************************************
					 * String intimationNo =
					 * rset.getString("INTIMATION_NUMBER"); String claimType =
					 * rset.getString("CLM_TYPE"); String rodNo =
					 * rset.getString("ROD_NO"); String cpu =
					 * rset.getString("CPU"); String ackDate=
					 * rset.getString("ACK_DATE"); String latAckDate =
					 * rset.getString("LAT_ACK_DT"); String rodDate =
					 * rset.getString("ROD_DATE"); String zonalApproveDate =
					 * rset.getString("ZONAL_DT"); String maApproveDate =
					 * rset.getString("MA_APR_DT"); String billApproveDate =
					 * rset.getString("BILL_APR_DT"); String faApproveDate =
					 * rset.getString("FA_APR_DT"); Integer totalDelay =
					 * rset.getInt("TOTAL_DELAY"); String status =
					 * rset.getString("STATUS"); String userQueue =
					 * rset.getString("USER_QUEUE"); Integer currentDelay =
					 * rset.getInt("CURRENT_DELAY");
					 * 
					 * tatDTO.setIntimationNo(intimationNo);
					 * tatDTO.setClaimType(claimType); tatDTO.setRodNo(rodNo);
					 * if(null != cpu) { tatDTO.setCpu(cpu); }
					 * tatDTO.setAckDate(ackDate);
					 * tatDTO.setLatAckDate(latAckDate);
					 * tatDTO.setRodDate(rodDate);
					 * tatDTO.setZonalApprovalDate(zonalApproveDate);
					 * tatDTO.setMaApproveDate(maApproveDate);
					 * tatDTO.setBillApproveDate(billApproveDate);
					 * tatDTO.setFaApprovedDate(faApproveDate);
					 * tatDTO.setTotalDelay(totalDelay);
					 * tatDTO.setStatus(status); tatDTO.setUserQueue(userQueue);
					 * tatDTO.setCurrentDelay(currentDelay);
					 ***************************************************************************************************/
					tatDTO.setIntimationNo(rset.getString("INTIMATION_NUMBER"));
					tatDTO.setClaimType(rset.getString("CLM_TYPE"));
					tatDTO.setPendingWith(rset.getString("PENGING_WITH"));
					tatDTO.setRodDelay(rset.getInt("ROD_DELAY"));
					tatDTO.setZonalDelay(rset.getInt("ZONAL_DELAY"));
					tatDTO.setMedicalDelay(rset.getInt("MEDICAL_DELAY"));
					tatDTO.setBillingDelay(rset.getInt("BILLING_DELAY"));
					tatDTO.setFaDelay(rset.getInt("FA_DELAY"));
					tatDTO.setPendingDays(rset.getInt("PENDING_DAYS"));
					tatDTO.setTotalDelay(rset.getInt("TOTAL_DURATION"));
					tatDTO.setDelayExcludingSunday(rset
							.getInt("DELAY_EXCLUDING_SUNDAY"));
					tatDTO.setRodNo(rset.getString("ROD_NO"));
					String cpu = rset.getString("CPU");
					if (null != cpu) {
						tatDTO.setCpu(cpu);
					}
					tatDTO.setClaimedAmt(rset.getDouble("CLAIMED_AMOUNT"));
					tatDTO.setZMRAmt(rset.getDouble("ZMR_AMOUNT"));
					tatDTO.setReconsider(rset.getString("RECONSIDER"));
					tatDTO.setAckDate(rset.getString("ACK_DATE"));
					tatDTO.setLatAckDate(rset.getString("LAT_ACK_DT"));
					tatDTO.setRodDate(rset.getString("ROD_DATE"));
					tatDTO.setZonalApprovalDate(rset.getString("ZONAL_PROC_DT"));
					tatDTO.setMaApproveDate(rset.getString("MA_PRO_DT"));
					tatDTO.setBillApproveDate(rset.getString("BILL_PRO_DT"));
					tatDTO.setFaApprovedDate(rset.getString("FA_PRO_DT"));
					tatDTO.setLastStatusDate(rset.getString("LAST_STATUS_DATE"));
					tatDTO.setInvestigationDate(rset
							.getString("INVESTIGATION_INIT_DT"));
					tatDTO.setRejectionDate(rset.getString("REJECTION_INIT_DT"));
					tatDTO.setStatus(rset.getString("STATUS"));
					tatDTO.setOfficeCodeValue(String.valueOf(rset
							.getLong("OFFICE_CODE")));
					tatDTO.setZone(rset.getString("ZONE"));
					tatDTO.setUserId(rset.getString("USER_ID"));
					tatDTO.setUserName(rset.getString("USER_NAME"));
					tatDTO.setDocRecvdFrom(rset
							.getString("DOCUMENT_RECEVIED_FROM"));
					tatDTO.setHospName(rset.getString("HOSPITAL_NAME"));
					tatDTO.setHospCity(rset.getString("HOSPITAL_CITY"));

					tatpendingClaims.add(tatDTO);
					tatDTO = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}

				if (cs != null) {
					cs.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tatpendingClaims;

	}

	public List<SearchShadowProvisionDTO> getDownloadForProvisonExcel(
			Date currentDate) {
		final String TAT_PENDING_CLAIMS = "{call  PRC_CURR_PROVISION_DOWNLOAD(?,?)}";

		java.sql.Date now = new java.sql.Date(currentDate.getTime());

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<SearchShadowProvisionDTO> tatpendingClaims = new ArrayList<SearchShadowProvisionDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(TAT_PENDING_CLAIMS);
			cs.setDate(1, now);

			cs.registerOutParameter(2, OracleTypes.CURSOR);
			cs.execute();
			rset = (ResultSet) cs.getObject(2);

			if (null != rset) {
				SearchShadowProvisionDTO tatDTO = null;
				while (rset.next()) {
					tatDTO = new SearchShadowProvisionDTO();

					/*
					 * String intimationNo =
					 * rset.getString("INTIMATION_NUMBER"); String cpuCode =
					 * rset.getString("CPU_CODE"); Integer existingProvision =
					 * rset.getInt("CURRENT_PROVISION_AMOUNT"); Integer
					 * revisedProvision =
					 * rset.getInt("REVISED_CURRENT_PROVISION_AMT");
					 */

					tatDTO.setIntimationNumber(rset
							.getString("INTIMATION_NUMBER"));
					tatDTO.setCpuCode(rset.getString("CPU_CODE"));
					tatDTO.setExistingProvision(rset
							.getInt("CURRENT_PROVISION_AMOUNT"));
					tatDTO.setNewProvision(rset
							.getInt("REVISED_CURRENT_PROVISION_AMT"));

					tatpendingClaims.add(tatDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tatpendingClaims;

	}

	public List<ClaimsAuditTableDTO> getDownloadForClaimAuditExcel(
			Date fromDate, Date toDate) {
		final String CLAIMS_AUDIT_REPORT = "{call  PRC_RPT_CLAIM_AUDIT(?,?,?)}";

		java.sql.Date from = new java.sql.Date(fromDate.getTime());
		java.sql.Date to = new java.sql.Date(toDate.getTime());

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<ClaimsAuditTableDTO> claimsAuditList = new ArrayList<ClaimsAuditTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CLAIMS_AUDIT_REPORT);
			cs.setDate(1, from);
			cs.setDate(2, to);

			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.execute();
			rset = (ResultSet) cs.getObject(3);

			if (null != rset) {
				ClaimsAuditTableDTO tatDTO = null;
				while (rset.next()) {
					tatDTO = new ClaimsAuditTableDTO();

					tatDTO.setAuditCompletedDate(rset
							.getString("AUDIT_COMPETE_DATE"));
					tatDTO.setTeam(rset.getString("ERROR_TEAM"));
					String empID = rset.getString("Claim processing ID - name") != null ? rset
							.getString("Claim processing ID - name").split("-")[0]
							: "";
					String empName = rset
							.getString("Claim processing ID - name") != null ? rset
							.getString("Claim processing ID - name").split("-")[1]
							: "";
					tatDTO.setEmployeeID(empID);
					tatDTO.setEmployeeName(empName);
					tatDTO.setIntimationNo(rset.getString("INTIMATION_NUMBER") != null ? rset
							.getString("INTIMATION_NUMBER") : "");
					tatDTO.setInitialApprovedAmt(rset
							.getString("Claim approved amount ") != null ? rset
							.getString("Claim approved amount ") : "");
					tatDTO.setAmountInvloved(rset.getString("AMT_INVOLVED") != null ? rset
							.getString("AMT_INVOLVED") : "");
					tatDTO.setMonetaryResult(rset.getString("MONETARY_RESULT") != null ? rset
							.getString("MONETARY_RESULT") : "");
					tatDTO.setErrorCategory(rset.getString("ERROR_CATEGORY") != null ? rset
							.getString("ERROR_CATEGORY") : "");
					tatDTO.setRemarks(rset.getString("AUDIT_REMARKS") != null ? rset
							.getString("AUDIT_REMARKS") : "");
					tatDTO.setResolution(rset.getString("REMEDIATION_REMARKS") != null ? rset
							.getString("REMEDIATION_REMARKS") : "");
					tatDTO.setZone(rset.getString("Zone") != null ? rset
							.getString("Zone") : "");
					tatDTO.setCpu(rset.getString("CPU") != null ? rset
							.getString("CPU") : "");
					tatDTO.setPolicyNo(rset.getString("POLICY_NUMBER") != null ? rset
							.getString("POLICY_NUMBER") : "");
					tatDTO.setProductName(rset.getString("PRODUCT_NAME") != null ? rset
							.getString("PRODUCT_NAME") : "");
					tatDTO.setPolicyAging(rset.getString("Policy Ageing") != null ? rset
							.getString("Policy Ageing") : "");
					tatDTO.setDaignosis(rset.getString("DIAGNOSIS/PROCEDURE")
							.trim() != null ? rset.getString(
							"DIAGNOSIS/PROCEDURE").trim() : "");
					tatDTO.setHospital(rset
							.getString("Hospital Name and Location") != null ? rset
							.getString("Hospital Name and Location") : "");
					tatDTO.setAuditingUser(rset.getString("AUDIT USER") != null ? rset
							.getString("AUDIT USER") : "");
					claimsAuditList.add(tatDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return claimsAuditList;

	}

	public List<Object> getAddOnBenefitsValues(Long reimbursementKey,
			Long insuredKey, Double sumInsured, Long productId,
			String benefitFlag) {
		Connection connection = null;
		CallableStatement cs = null;
		List<Object> benefitsValues = new ArrayList<Object>();
		try {
			// final String typeName = "OBJ_SUB_LIM";
			final String typeTableName = "TYP_HOSCS_PATCR";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BENEFITS_HOSCS_PATCR(?, ?, ?,?,?,?,?)}");
			cs.setLong(1, reimbursementKey);
			cs.setLong(2, insuredKey);
			cs.setDouble(3, sumInsured);
			cs.setLong(4, productId);
			cs.setString(5, benefitFlag);
			cs.registerOutParameter(6, Types.INTEGER, "LN_REDUCED_DAYS");
			cs.registerOutParameter(7, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(7);

			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;
				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();
					// Map<Integer, Object> benefitsMap = new HashMap<Integer,
					// Object>();

					for (Object object2 : attributes) {
						benefitsValues.add(object2);
					}
					/*
					 * for(int i = 0 ; i<attributes.length ; i++) {
					 * //benefitsMap.put(i,attributes[i]); benefitsValues.add(e)
					 * 
					 * } benefitsValues.add(benefitsMap);
					 */

					/*
					 * for (Object object2 : attributes) {
					 * benefitsValues.add(SHAUtils
					 * .getDoubleValueFromString(String.valueOf(object2))) ; }
					 */
				}
			}
			benefitsValues.add(cs.getObject(6));

			// return benefitsValues;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return benefitsValues;

	}

	public static void main(String args[]) {
		/*
		 * DBCalculationService dbCalculationService = new
		 * DBCalculationService(); dbCalculationService.initiateTaskProcedure();
		 */
		/*
		 * DBCalculationService dbCalculationService = new
		 * DBCalculationService();
		 * //dbCalculationService.getProductBenefitFlag(845l, 19l);
		 * dbCalculationService.getBalanceSI(3689l, 8129l, 0l, 1000000d, 6940l);
		 */

		// AuditImageService auditImageService =
		// BPMClientContext.getAuditImageService("claimshead", "Star@123");
		// byte[] image = auditImageService.getImage("claimshead", "224218");

		/*
		 * Map<String, Object> inputValues = new HashMap<String, Object>();
		 * inputValues.put("referenceFlag", "D");
		 * 
		 * 
		 * inputValues.put("insuredId", 296773L);
		 * inputValues.put("diagOrProcId", 39L); inputValues.put("preauthKey",
		 * 0L); inputValues.put("diagnosisId",11L); inputValues.put("policySI",
		 * "150000.0"); inputValues.put("policyNumber",
		 * "P/121215/01/2007/000034"); inputValues.put("restrictedSI",
		 * "200000"); inputValues.put("sublimitId", 8L);
		 * dbCalculationService.getMedicalDecisionTableValue(inputValues);
		 */

		// Date fromDate = new Date("4/24/2018 12:00:00 AM");
		String format = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

		System.out.println("Date ----------------> " + format);

		DBCalculationService dbcal = new DBCalculationService();
		dbcal.getBranchContainerForManagerFeedback("SH022077");

	}

	public Map<String, Double> getNonAllopathicAmount(Long policyKey,
			Long insuredKey, Long transactionKey, String transactionType,
			Long claimKey) {
		final String nonAllopathicOriginalAmt = "LN_LIMIT_AMT";
		final String nonAllopathicUtilizedAmt = "LN_UTILIZED_AMT";

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_NONALLOPATHIC_CALC(?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, transactionKey);
			cs.setString(5, transactionType);

			cs.registerOutParameter(6, Types.DOUBLE, nonAllopathicOriginalAmt);
			cs.registerOutParameter(7, Types.DOUBLE, nonAllopathicUtilizedAmt);

			cs.execute();

			// BalanceSumInsuredDTO balanceSumInsuredDTO = new
			// BalanceSumInsuredDTO();
			if (cs.getObject(6) != null) {
				values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT,
						Double.parseDouble(cs.getObject(6).toString()));
			}
			if (cs.getObject(7) != null) {
				values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT,
						Double.parseDouble(cs.getObject(7).toString()));
			}

			// return values;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
					if (cs != null) {
						cs.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public Map<String, Integer> getHealthCheckupFlag(Long policyKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Integer> values = new HashMap<String, Integer>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HOSP_CHK_OP_CHECK(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.registerOutParameter(2, Types.INTEGER, "LN_HOS_CHKUP_FLAG");
			cs.registerOutParameter(3, Types.INTEGER, "LN_OP_FLAG");

			cs.execute();

			// BalanceSumInsuredDTO balanceSumInsuredDTO = new
			// BalanceSumInsuredDTO();
			if (cs.getObject(2) != null) {
				values.put(SHAConstants.HEALTH_CHECK_UP_FLAG,
						Integer.parseInt(cs.getObject(2).toString()));
			}
			if (cs.getObject(3) != null) {
				values.put(SHAConstants.OUTPATIENT_FLAG,
						Integer.parseInt(cs.getObject(3).toString()));
			}

			// if(values.isEmpty()){
			// values.put(SHAConstants.HEALTH_CHECK_UP_FLAG, 0);
			// values.put(SHAConstants.OUTPATIENT_FLAG, 0);
			// }

			// return values;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public Integer getHealthCheckupSumInsured(Long insuredKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Integer output = 0;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_HOSP_CHKUP_SI_CHECK(?, ?)}");
			cs.setLong(1, insuredKey);
			cs.registerOutParameter(2, Types.INTEGER, "LN_HOS_CHKUP_FLAG");

			cs.execute();
			if (cs.getObject(2) != null) {
				output = Integer.parseInt(cs.getObject(2).toString());
			}

			// return 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Map<String, Integer> getOPAvailableAmount(Long insuredKey, Long claimKey, Long claimTypeKey,String secCode) {

		Connection connection = null;
		CallableStatement cs = null;
		Integer availableAmt = 0;
		Integer claimLimitAmt = 0;
		Integer policyLimitAmt = 0;
		Map<String, Integer> values = new HashMap<String, Integer>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OP_HC_AVAILABLE_AMT(?, ?, ?, ?, ?,?,?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);
			cs.setLong(3, claimTypeKey);
			cs.setString(4, secCode);
			cs.registerOutParameter(5, Types.INTEGER, "LN_AVAILABLE_AMT");
			cs.registerOutParameter(6, Types.INTEGER, "LN_CLM_LIMIT_AMT");
			cs.registerOutParameter(7, Types.INTEGER, "LN_POL_LIMIT_AMT");

			cs.execute();
			if (cs.getObject(5) != null) {
				availableAmt = Integer.parseInt(cs.getObject(5).toString());
			}
			if (cs.getObject(6) != null) {
				/*Double clmLimit = (Double) cs.getObject(4);
				claimLimitAmt = Integer.valueOf(clmLimit.toString());*/
				claimLimitAmt = Integer.parseInt(cs.getObject(6).toString());
			}
			if (cs.getObject(7) != null) {
				/*Double policyLimit = (Double) cs.getObject(5);
				policyLimitAmt = Integer.valueOf(policyLimit.toString());*/
				policyLimitAmt = Integer.parseInt(cs.getObject(7).toString());
			}

			// return 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				availableAmt != null ? availableAmt : 0);
		values.put(SHAConstants.OP_CLAIM_LIMIT,
				claimLimitAmt != null ? claimLimitAmt : 0);
		values.put(SHAConstants.OP_POLICY_LIMIT,
				policyLimitAmt != null ? policyLimitAmt : 0);
		return values;
	}

	public List<Double> getProductCoPay(Long productKey, Long insuredKey,
			Long insuredNumber, NewIntimationDto intimationDto) {
		Connection connection = null;
		CallableStatement cs = null;
		List<Double> copayValues = new ArrayList<Double>();
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_POLICY_COPAY(?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setLong(2, insuredKey);
			// cs.setLong(3, intimationDto.getKey());

			// Result is an java.sql.Array...
			cs.registerOutParameter(3, Types.ARRAY, "TYP_PRODUCT_COPAY");

			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;

				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();
					Double doubleValueFromString = null != attributes[0] ? SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[0])) : null;
					copayValues.add(doubleValueFromString);

					String copayFlag = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					intimationDto.setCopayFlag(copayFlag);
				}

			}

			/*
			 * if(null != productKey &&
			 * !productKey.equals(ReferenceTable.JET_PRIVILEGE_PRODUCT) &&
			 * !(ReferenceTable
			 * .STAR_SPECIAL_CARE_PRODUCT_KEY.equals(productKey))){ if
			 * (!getInsuredEntryAgeCheck(insuredNumber)) { copayValues = new
			 * ArrayList<Double>(); copayValues.add(0d); } }
			 */

			Boolean isHavingZero = false;
			for (Double double1 : copayValues) {
				if (double1.equals(0d)) {
					isHavingZero = true;
					break;
				}
			}

			if (!isHavingZero) {
				copayValues.add(0, 0d);
			}
			// copayValues.add(7.5d);
			// return copayValues;
			// Collections.sort(copayValues);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return copayValues;
	}

	public Map<String, Integer> getHealthCheckupLimitAmount(Long claimKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Integer limitAmt = 0;
		Integer availAmt = 0;
		Map<String, Integer> helathCheckupAmt = new HashMap<String, Integer>();
		// List<Double> copayValues = new ArrayList<Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HOSP_CHKUP_LIMIT_AMOUNT(?, ?, ?)}");
			cs.setLong(1, claimKey);
			// Result is an java.sql.Array...
			cs.registerOutParameter(2, Types.INTEGER, "LN_HOSP_CHKUP_LIMIT_AMT");
			cs.registerOutParameter(3, Types.INTEGER,
					" LN_HOSP_CHKUP_AVAIL_AMT");
			cs.execute();

			Object limit = cs.getObject(2);
			Object avail = cs.getObject(3);

			if (limit != null) {
				limitAmt = Integer.parseInt(limit.toString());
			}
			if (avail != null) {
				availAmt = Integer.parseInt(avail.toString());
			}
			helathCheckupAmt.put(SHAConstants.LIMIT_AMOUNT, limitAmt);
			helathCheckupAmt.put(SHAConstants.AVAILABLE_SI, availAmt);
			// return helathCheckupAmt;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return helathCheckupAmt;
	}

	public Map<String, Integer> getOPLimitAmount(Long claimKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Integer limitAmt = 0;
		Integer availAmt = 0;
		Map<String, Integer> helathCheckupAmt = new HashMap<String, Integer>();
		// List<Double> copayValues = new ArrayList<Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_OP_LIMIT_AMOUNT(?, ?, ?)}");
			cs.setLong(1, claimKey);
			// Result is an java.sql.Array...
			cs.registerOutParameter(2, Types.INTEGER, "LN_OP_LIMIT_AMT");
			cs.registerOutParameter(3, Types.INTEGER, " LN_OP_AVAIL_AMT");
			cs.execute();

			Object limit = cs.getObject(2);
			Object avail = cs.getObject(3);

			if (limit != null) {
				limitAmt = Integer.parseInt(limit.toString());
			}
			if (avail != null) {
				availAmt = Integer.parseInt(avail.toString());
			}
			helathCheckupAmt.put(SHAConstants.LIMIT_AMOUNT, limitAmt);
			helathCheckupAmt.put(SHAConstants.AVAILABLE_SI, availAmt);
			// return helathCheckupAmt;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return helathCheckupAmt;
	}

	public BalanceSumInsuredDTO getCumulativeBonusRestrictedSIRestoredSi(
			Long policyKey, Long insuredKey, Long intimaitonKey) {
		final String cumulativeBonus = "LN_NO_CLAIM_BONUS";
		final String restoredSI = "LN_RESTORED_SI";
		final String restrictedSI = "LN_RECHARGED_SI";
		final String limitOfCoverage = "LN_LIMIT_OF_COVERAGE";
		final String lvRechargeAmount= "LV_RECHARGED_AMT";

		Connection connection = null;
		CallableStatement cs = null;
		BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CUMBONUS_REST_RECHRG_SI(?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimaitonKey);
			cs.registerOutParameter(4, Types.DOUBLE, cumulativeBonus);
			cs.registerOutParameter(5, Types.DOUBLE, restoredSI);
			cs.registerOutParameter(6, Types.DOUBLE, restrictedSI);
			cs.registerOutParameter(7, Types.DOUBLE, limitOfCoverage);
			cs.registerOutParameter(8, Types.DOUBLE, lvRechargeAmount);
			cs.execute();

			if (cs.getObject(4) != null) {
				balanceSumInsuredDTO.setCumulativeBonus(Double.parseDouble(cs
						.getObject(4).toString()));
			} else {
				balanceSumInsuredDTO.setCumulativeBonus(0.0);
			}
			if (cs.getObject(5) != null) {
				balanceSumInsuredDTO.setRestoredSumInsured(Double
						.parseDouble(cs.getObject(5).toString()));
			} else {
				balanceSumInsuredDTO.setRestoredSumInsured(0.0);
			}
			if (cs.getObject(6) != null) {
				balanceSumInsuredDTO.setRechargedSumInsured(Double
						.parseDouble(cs.getObject(6).toString()));
			} else {
				balanceSumInsuredDTO.setRechargedSumInsured(0.0);
			}
			if (cs.getObject(7) != null) {
				balanceSumInsuredDTO.setLimitOfCoverage(Double.parseDouble(cs
						.getObject(7).toString()));
			} else {
				balanceSumInsuredDTO.setLimitOfCoverage(0.0);
			}
			if (cs.getObject(8) != null) {
				balanceSumInsuredDTO.setRechargeAmount(Double.parseDouble(cs
						.getObject(8).toString()));
			} else {
				balanceSumInsuredDTO.setRechargeAmount(0.0);
			}
			// return balanceSumInsuredDTO;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return balanceSumInsuredDTO;
	}

	/***
	 * Method to calculate Balance SI for pre auth.
	 *
	 * key shld be given
	 */
	// public Double getBalanceSumInsured(String policyNumber, int sumInsured)
	@Deprecated
	public Double getBalanceSumInsured(Long lPolicyKey, int sumInsured) {
		final String typeName = "LV_BAL_SI";

		Connection connection = null;
		CallableStatement cs = null;
		Double output = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BALANCE_SI(?, ?, ?)}");
			cs.setDouble(1, lPolicyKey);
			cs.setDouble(2, sumInsured);
			cs.registerOutParameter(3, Types.DOUBLE, typeName);
			cs.execute();
			output = (Double) cs.getObject(3);
			// return (Double) cs.getObject(3);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Map<String, Double> getBalanceSI(Long policyKey, Long insuredKey,
			Long claimKey, Double sumInsured, Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BALANCE_SI(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);

			if (sumInsured != null) {
				cs.setDouble(5, sumInsured);
			} else {
				cs.setDouble(5, 0);
			}

			cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(6);
			currentBalanceSI = (Double) cs.getObject(7);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public Map<String, Double> getBalanceSIRnd(Long policyKey, Long insuredKey,
			Long claimKey, Long intimationKey, Long sumInsured) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BALANCE_SI_INT(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);

			if (sumInsured != null) {
				cs.setDouble(5, sumInsured);
			} else {
				cs.setDouble(5, 0);
			}

			cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(6);
			currentBalanceSI = (Double) cs.getObject(7);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public Map<String, Double> getRTABalanceSI(Long policyKey, Long insuredKey,
			Long claimKey, Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_FHO_BALANCE_SI(?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(5);
			currentBalanceSI = (Double) cs.getObject(6);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	// public Map<String, Double> getBalanceSI(Long policyKey, Long insuredKey,
	// Long claimKey,
	// Double sumInsured,Long intimationKey, String subCoverCode) {
	//
	// Connection connection = null;
	// CallableStatement cs= null;
	// Double totalBalanceSI = 0d;
	// Double currentBalanceSI = 0d;
	// Map<String, Double> values = new HashMap<String, Double>();
	// try {
	// connection = BPMClientContext.getConnection();
	// cs = connection
	// .prepareCall("{call PRC_BALANCE_SI1(?, ?, ?, ?, ?, ?, ?, ?)}");
	// cs.setLong(1, policyKey);
	// cs.setLong(2, insuredKey);
	// cs.setLong(3, intimationKey);
	// cs.setLong(4, claimKey);
	//
	// if (sumInsured != null) {
	// cs.setDouble(5, sumInsured);
	// } else {
	// cs.setDouble(5, 0);
	// }
	// cs.setString(6, subCoverCode);
	// cs.registerOutParameter(7, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
	// cs.registerOutParameter(8, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
	// cs.execute();
	//
	// // return (Double) cs.getObject(4);
	// totalBalanceSI = (Double) cs.getObject(7);
	// currentBalanceSI = (Double) cs.getObject(8);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// // return new Double("0");
	// values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI != null ?
	// totalBalanceSI : 0d);
	// values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI != null ?
	// currentBalanceSI : 0d);
	// return values;
	// }

	public String getMaternityFlagForProduct(Long productKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_MATERNITY_CHECK(?, ?)}");
			cs.setLong(1, productKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(2) != null) {
				output = (String) cs.getObject(2).toString();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getComprehensiveSI(Long insuredKey,
			String intimationNo, Double sumInsured, Long prodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfTable = new HashMap<String, Object>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PREV_CLAIM_2(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7, Types.ARRAY, "TYP_SI_LIST");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();

			List<ComprehensiveHospitalisationTableDTO> sectionTable1 = new ArrayList<ComprehensiveHospitalisationTableDTO>();
			List<ComprehensiveDeliveryNewBornTableDTO> sectionTable2 = new ArrayList<ComprehensiveDeliveryNewBornTableDTO>();
			List<ComprehensiveOutpatientTableDTO> sectionTable3 = new ArrayList<ComprehensiveOutpatientTableDTO>();
			List<ComprehensiveHospitalCashTableDTO> sectionTable4 = new ArrayList<ComprehensiveHospitalCashTableDTO>();
			List<ComprehensiveHealthCheckTableDTO> sectionTable5 = new ArrayList<ComprehensiveHealthCheckTableDTO>();
			List<ComprehensiveBariatricSurgeryTableDTO> sectionTable6 = new ArrayList<ComprehensiveBariatricSurgeryTableDTO>();
			List<LumpSumTableDTO> sectionTable8 = new ArrayList<LumpSumTableDTO>();

			Struct row = null;
			Object[] attributes = null;
			for (Object object : data) {

				row = (Struct) object;
				attributes = row.getAttributes();
				if (attributes != null) {
					String sectionNameWithCode = (String) attributes[0];
					String sectionCode = (String) attributes[1];
					String coverName = (String) attributes[2];
					// String coverCode = (String)attributes[3];
					String subCoverName = (String) attributes[4];
					// String subCoverCode = (String)attributes[5];
					Double limitAmt = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[6]));
					Double claimsPaid = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[7]));
					Double outStanding = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[8]));
					Double balanceSumInsured = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[9]));
					Double currentProvisionAmt = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[10]));
					Double balSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[11]));
					ComprehensiveHospitalisationTableDTO section1 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_1)) {
						section1 = new ComprehensiveHospitalisationTableDTO();

						section1.setSectionI(sectionNameWithCode.split("-")[2]);
						section1.setCover(coverName);
						section1.setSubCover(subCoverName);
						section1.setSumInsured(limitAmt);
						section1.setClaimPaid(claimsPaid);
						section1.setClaimOutStanding(outStanding);
						section1.setBalance(balanceSumInsured);
						section1.setProvisionCurrentClaim(currentProvisionAmt);
						section1.setBalanceSI(balSI);

						sectionTable1.add(section1);
					}

					ComprehensiveDeliveryNewBornTableDTO section2 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_2)) {
						section2 = new ComprehensiveDeliveryNewBornTableDTO();

						section2.setSectionII(sectionNameWithCode.split("-")[2]);
						section2.setCover(coverName);
						section2.setSubCover(subCoverName);
						section2.setLimit(limitAmt);
						section2.setClaimPaid(claimsPaid);
						section2.setClaimOutstanding(outStanding);
						section2.setBalance(balanceSumInsured);
						section2.setProvisionCurrentClaim(currentProvisionAmt);
						section2.setBalanceSI(balSI);
						sectionTable2.add(section2);
					}

					ComprehensiveOutpatientTableDTO section3 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_3)) {
						section3 = new ComprehensiveOutpatientTableDTO();

						section3.setSectionIII(sectionNameWithCode.split("-")[2]);
						section3.setCover(coverName);
						section3.setSubCover(subCoverName);
						section3.setLimit(limitAmt);
						section3.setClaimPaid(claimsPaid);
						section3.setClaimOutstanding(outStanding);
						section3.setBalance(balanceSumInsured);
						section3.setProvisionCurrentClaim(currentProvisionAmt);
						section3.setBalanceSI(balSI);
						sectionTable3.add(section3);
					}

					ComprehensiveHospitalCashTableDTO section4 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_4)) {
						section4 = new ComprehensiveHospitalCashTableDTO();

						section4.setSectionIV(sectionNameWithCode.split("-")[2]);
						section4.setCover(coverName);
						section4.setSubCover(subCoverName);
						section4.setLimit(limitAmt);
						section4.setClaimPaid(claimsPaid);
						section4.setClaimOutstanding(outStanding);
						section4.setBalance(balanceSumInsured);
						section4.setProvisionCurrentClaim(currentProvisionAmt);
						section4.setBalanceSI(balSI);
						sectionTable4.add(section4);
					}

					ComprehensiveHealthCheckTableDTO section5 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_5)) {
						section5 = new ComprehensiveHealthCheckTableDTO();

						section5.setSectionV(sectionNameWithCode.split("-")[2]);
						section5.setCover(coverName);
						section5.setSubCover(subCoverName);
						section5.setLimit(limitAmt);
						section5.setClaimPaid(claimsPaid);
						section5.setClaimOutstanding(outStanding);
						section5.setBalance(balanceSumInsured);
						section5.setProvisionCurrentClaim(currentProvisionAmt);
						section5.setBalanceSI(balSI);
						sectionTable5.add(section5);
					}

					ComprehensiveBariatricSurgeryTableDTO section6 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_6)) {
						section6 = new ComprehensiveBariatricSurgeryTableDTO();

						section6.setSectionVI(sectionNameWithCode.split("-")[2]);
						section6.setCover(coverName);
						section6.setSubCover(subCoverName);
						section6.setLimit(limitAmt);
						section6.setClaimPaid(claimsPaid);
						section6.setClaimOutstanding(outStanding);
						section6.setBalance(balanceSumInsured);
						section6.setProvisionCurrentClaim(currentProvisionAmt);
						section6.setBalanceSI(balSI);
						sectionTable6.add(section6);
					}

					LumpSumTableDTO section8 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_8)) {
						section8 = new LumpSumTableDTO();

						section8.setSectionVIII(sectionNameWithCode.split("-")[2]);
						section8.setCover(coverName);
						section8.setSubCover(subCoverName);
						section8.setLimit(limitAmt);
						section8.setClaimPaid(claimsPaid);
						section8.setClaimOutstanding(outStanding);
						section8.setBalance(balanceSumInsured);
						section8.setProvisionCurrentClaim(currentProvisionAmt);
						section8.setBalanceSI(balSI);

						sectionTable8.add(section8);
					}

				}
			}

			listOfTable.put(SHAConstants.SECTION_CODE_1, sectionTable1);
			listOfTable.put(SHAConstants.SECTION_CODE_2, sectionTable2);
			listOfTable.put(SHAConstants.SECTION_CODE_3, sectionTable3);
			listOfTable.put(SHAConstants.SECTION_CODE_4, sectionTable4);
			listOfTable.put(SHAConstants.SECTION_CODE_5, sectionTable5);
			listOfTable.put(SHAConstants.SECTION_CODE_6, sectionTable6);
			listOfTable.put(SHAConstants.SECTION_CODE_8, sectionTable8);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;

	}

	public Map<String, Object> getOPBalanceSumInsured(Long claimKey,
			Long insuredKey, String claimType,String secCode) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OP_HC_BALSI_CALC(?, ?, ?, ?,?, ?, ?,?)}");
			cs.setLong(1, claimKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, claimType);
			cs.setString(4,secCode);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_LMT_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_OUTSTND_AMT");
			cs.registerOutParameter(7, Types.DOUBLE, "LN_PAID_AMT");
			cs.registerOutParameter(8, Types.DOUBLE, "LN_CRY_AMT");
			cs.execute();

			if(cs.getObject(5) != null){
				valuesMap.put("sumInsured",
						Double.parseDouble(cs.getObject(5).toString()));
			}else{
				valuesMap.put("sumInsured",
						0.0d);
			}
			if(cs.getObject(6) != null){
				valuesMap.put("claimsOutstanding",
						Double.parseDouble(cs.getObject(6).toString()));
			}else{
				valuesMap.put("claimsOutstanding",
						0.0d);
			}
			if(cs.getObject(7) != null){
				valuesMap.put("claimsSettled",
						Double.parseDouble(cs.getObject(7).toString()));
			}else{
				valuesMap.put("claimsSettled",
						0.0d);
			}
			if(cs.getObject(8) != null){
				valuesMap.put("balanceOPSumImsured",
						Double.parseDouble(cs.getObject(8).toString()));
			}else{
				valuesMap.put("balanceOPSumImsured",
						0.0d);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return valuesMap;
	}

	public Map<String, Double> getBalanceSIForReimbursement(Long policyKey,
			Long insuredKey, Long claimKey, Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		System.out.println("Start getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_BALANCE_SI(?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();
			System.out.println("Middle getBalanceSIForReimbursement Procedure "
					+ System.currentTimeMillis());

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(5);
			currentBalanceSI = (Double) cs.getObject(6);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI);
		values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI);
		System.out.println("End getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		return values;
	}

	public Map<Integer, Object> getHospitalizationDetails(Long productKey,
			Double sumInsured, String cityClass, Long insuredId,
			Long intimationKey, Long section, String plan) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();

		Long sectionValue = 0l;

		if (section.equals(ReferenceTable.POL_SECTION_1)) {
			sectionValue = 1l;
		} else if (section.equals(ReferenceTable.POL_SECTION_2)) {
			sectionValue = 2l;
		}

		if (plan == null) {
			plan = "0";
		}

		// //// Prorata Flag 1 = Yes, 0 = No, 2 = Percentage...

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HOSPITALIZATION(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)}");
			cs.setLong(1, productKey);
			cs.setDouble(2, sumInsured);
			cs.setString(3, cityClass);
			cs.setLong(4, sectionValue);
			cs.setString(5, plan);
			cs.setLong(6, insuredId);
			cs.setLong(7, intimationKey != null ? intimationKey : 0l);
			// Result is an java.sql.Array...
			cs.registerOutParameter(8, Types.DOUBLE, "RR_AMT");
			cs.registerOutParameter(9, Types.DOUBLE, "ICU_AMT");
			cs.registerOutParameter(10, Types.DOUBLE, "AMB_AMT");
			cs.registerOutParameter(11, Types.INTEGER, "LV_PRORAT_FLG");
			cs.execute();

			valuesMap.put(8, Double.parseDouble(cs.getObject(8).toString()));
			valuesMap.put(9, Double.parseDouble(cs.getObject(9).toString()));
			valuesMap.put(15, Double.parseDouble(cs.getObject(10).toString()));
			valuesMap.put(0, Integer.parseInt(cs.getObject(11).toString()));
			valuesMap.put(22, Double.parseDouble(cs.getObject(9).toString()));

			// valuesMap.put(8,1000d);
			// valuesMap.put(9 ,2000d);
			// valuesMap.put(15 , 1200d);
			// valuesMap.put(0, 500);

			if (valuesMap.get(0) != null
					&& ((Integer) valuesMap.get(0)).equals(2)) {
				Object[] prorataPercentage = getProrataPercentage(productKey);
				if (prorataPercentage != null) {
					Struct row = null;
					Object[] attributes = null;
					for (Object tmp : prorataPercentage) {
						row = (Struct) tmp;
						attributes = row.getAttributes();
						if (valuesMap.containsKey(Integer.valueOf(String
								.valueOf(attributes[0])))) {
							continue;
						}
						valuesMap.put(Integer.valueOf(String
								.valueOf(attributes[0])), Double
								.parseDouble(String.valueOf(attributes[1])));
					}
				}

			}
			// return valuesMap;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public Double getProductBasedAmbulanceAmt(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Double ambAmt = 0d;
		final String typeTableName = "LN_AVAIL_UTIL_AMT";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_REIMB_AMBULANCE_AMT(?, ?)}");
			cs.setLong(1, rodKey);
			cs.registerOutParameter(2, Types.DOUBLE, typeTableName);
			cs.execute();
			ambAmt = (Double) cs.getObject(2);
			// return ambAmt;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ambAmt;
	}

	public Object[] getProrataPercentage(Long productKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Object[] data = null;
		final String typeTableName = "TYP_PRORATA_PERCENT";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PRORATA_CHECK(?, ?)}");
			cs.setLong(1, productKey);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();
			data = (Object[]) ((Array) cs.getObject(2)).getArray();
			// return data;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public Map<String, Double> getDiagnosisSumInsuredLimit(String policyNumber,
			Long insuredId, Double policySumInsured, Double currentSumInsured,
			Long sublimitId) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Double> valuesMap = new HashMap<String, Double>();
		try {
			// Map<String, Double> valuesMap = new HashMap<String, Double>();
			if (sublimitId != null) {
				connection = BPMClientContext.getConnection();
				cs = connection
						.prepareCall("{call PRC_DIAGNOSIS_SL_AMT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				cs.setString(1, policyNumber);
				cs.setLong(2, insuredId);
				cs.setDouble(3, policySumInsured);
				cs.setDouble(4, currentSumInsured);
				cs.setLong(5, sublimitId);
				// Result is an java.sql.Array...
				cs.registerOutParameter(6, Types.DOUBLE, "LN_POL_SL");
				cs.registerOutParameter(7, Types.DOUBLE, "LN_CURR_SL");
				cs.registerOutParameter(8, Types.DOUBLE, "LN_UTIL_AMT");
				cs.registerOutParameter(9, Types.DOUBLE, "LN_AVAIL_AMT");
				cs.registerOutParameter(10, Types.DOUBLE,
						"LN_RESTRICTED_BAL_AMT");
				cs.execute();
				valuesMap.put("policySublimit",
						Double.parseDouble(cs.getObject(6).toString()));
				valuesMap.put("currentSublimit",
						Double.parseDouble(cs.getObject(7).toString()));
				valuesMap.put("utilizedAmount",
						Double.parseDouble(cs.getObject(8).toString()));
				valuesMap.put("availableAmount",
						Double.parseDouble(cs.getObject(9).toString()));
				valuesMap.put("siRestriction",
						Double.parseDouble(cs.getObject(10).toString()));
			} else {

				valuesMap.put("policySublimit", Double.parseDouble("0"));
				valuesMap.put("currentSublimit", Double.parseDouble("0"));
				valuesMap.put("utilizedAmount", Double.parseDouble("0"));
				valuesMap.put("availableAmount", Double.parseDouble("0"));
				valuesMap.put("siRestriction", Double.parseDouble("0"));
			}
			// return valuesMap;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public Map<String, Object> getMedicalDecisionTableValue(
			Map<String, Object> inputValues, NewIntimationDto intimationDto) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> valuesMap = new WeakHashMap<String, Object>();
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_SUBLIMIT_RES_SI_CALC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, (Long) inputValues.get("insuredId"));
			cs.setLong(2, (Long) inputValues.get(SHAConstants.CLAIM_KEY));
			cs.setLong(3, (Long) inputValues.get("preauthKey"));
			cs.setLong(
					4,
					inputValues.get("restrictedSI") != null ? (Long) inputValues
							.get("restrictedSI") : 0l);
			cs.setLong(
					5,
					inputValues.get("sublimitId") != null ? (Long) inputValues
							.get("sublimitId") : 0);
			cs.setLong(6, (Long) inputValues.get("diagOrProcId"));
			cs.setString(7, (String) inputValues.get("referenceFlag"));

			// Result is an java.sql.Array...
			cs.registerOutParameter(8, Types.DOUBLE, "LN_RESTRICT_UTIL_AMT");
			cs.registerOutParameter(9, Types.DOUBLE, "LN_RESTRICT_AVAIL_AMT");
			cs.registerOutParameter(10, Types.DOUBLE, "LN_CURRENT_SL");
			cs.registerOutParameter(11, Types.DOUBLE, "LN_UTILIZED_AMT");
			cs.registerOutParameter(12, Types.DOUBLE, "LN_AVAILABLE_AMT");
			cs.registerOutParameter(13, Types.ARRAY, "TYP_PRODUCT_COPAY");

			cs.execute();

			Object object = cs.getObject(13);
			List<String> copayValues = new ArrayList<String>();
			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;
				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();

					String doubleValueFromString = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					copayValues.add(doubleValueFromString);

					String copayFlag = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					intimationDto.setCopayFlag(copayFlag);
					/*
					 * for (Object object2 : attributes) {
					 * copayValues.add(String.valueOf(object2)); }
					 */
				}

			}

			if (inputValues.get("productKey") != null
					&& !((Long) inputValues.get("productKey"))
							.equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)) {
				if (inputValues.containsKey("insuredId")
						&& !getInsuredEntryAgeCheck((Long) inputValues
								.get("insuredId"))) {
					copayValues = new ArrayList<String>();
					copayValues.add("0");
				}
			}/*
			 * else if(inputValues.get("productKey") == null){ if
			 * (inputValues.containsKey("insuredId") &&
			 * !getInsuredEntryAgeCheck((Long) inputValues .get("insuredId"))) {
			 * copayValues = new ArrayList<String>(); copayValues.add("0"); } }
			 */

			valuesMap.put("restrictedUtilAmt", Double.parseDouble(cs
					.getObject(8) != null ? cs.getObject(8).toString() : "0"));
			valuesMap.put("restrictedAvailAmt", Double.parseDouble(cs
					.getObject(9) != null ? cs.getObject(9).toString() : "0"));
			// //need to change
			valuesMap.put(
					"currentSL",
					Double.parseDouble(cs.getObject(10) != null ? cs.getObject(
							10).toString() : "0"));
			valuesMap.put(
					"SLUtilAmt",
					Double.parseDouble(cs.getObject(11) != null ? cs.getObject(
							11).toString() : "0"));
			valuesMap.put(
					"SLAvailAmt",
					Double.parseDouble(cs.getObject(12) != null ? cs.getObject(
							12).toString() : "0"));
			valuesMap.put("copay", copayValues);

			// return valuesMap;

			Collections.sort(copayValues);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public Map<String, Object> getMedicalDecisionTableValueForGMC(
			Map<String, Object> inputValues, NewIntimationDto intimationDto) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> valuesMap = new WeakHashMap<String, Object>();
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_SUBLIMIT_SI_CALC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, (Long) inputValues.get("insuredId"));
			cs.setLong(2, (Long) inputValues.get(SHAConstants.CLAIM_KEY));
			cs.setLong(3, (Long) inputValues.get("preauthKey"));
			cs.setLong(
					4,
					inputValues.get("restrictedSI") != null ? (Long) inputValues
							.get("restrictedSI") : 0l);
			cs.setLong(
					5,
					inputValues.get("sublimitId") != null ? (Long) inputValues
							.get("sublimitId") : 0);
			cs.setLong(6, (Long) inputValues.get("diagOrProcId"));
			cs.setString(7, (String) inputValues.get("referenceFlag"));

			// Result is an java.sql.Array...
			cs.registerOutParameter(8, Types.DOUBLE, "LN_RESTRICT_UTIL_AMT");
			cs.registerOutParameter(9, Types.DOUBLE, "LN_RESTRICT_AVAIL_AMT");
			cs.registerOutParameter(10, Types.DOUBLE, "LN_CURRENT_SL");
			cs.registerOutParameter(11, Types.DOUBLE, "LN_UTILIZED_AMT");
			cs.registerOutParameter(12, Types.DOUBLE, "LN_AVAILABLE_AMT");
			cs.registerOutParameter(13, Types.ARRAY, "TYP_PRODUCT_COPAY");

			cs.execute();

			Object object = cs.getObject(13);
			List<String> copayValues = new ArrayList<String>();
			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;
				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();

					String doubleValueFromString = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					copayValues.add(doubleValueFromString);

					String copayFlag = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					intimationDto.setCopayFlag(copayFlag);

					/*
					 * for (Object object2 : attributes) {
					 * copayValues.add(String.valueOf(object2)); }
					 */
				}

			}

			/*
			 * if (inputValues.containsKey("insuredId") &&
			 * !getInsuredEntryAgeCheck((Long) inputValues .get("insuredId"))) {
			 * copayValues = new ArrayList<String>(); copayValues.add("0"); }
			 */
			valuesMap.put("restrictedUtilAmt", Double.parseDouble(cs
					.getObject(8) != null ? cs.getObject(8).toString() : "0"));
			valuesMap.put("restrictedAvailAmt", Double.parseDouble(cs
					.getObject(9) != null ? cs.getObject(9).toString() : "0"));
			// //need to change
			valuesMap.put(
					"currentSL",
					Double.parseDouble(cs.getObject(10) != null ? cs.getObject(
							10).toString() : "0"));
			valuesMap.put(
					"SLUtilAmt",
					Double.parseDouble(cs.getObject(11) != null ? cs.getObject(
							11).toString() : "0"));
			valuesMap.put(
					"SLAvailAmt",
					Double.parseDouble(cs.getObject(12) != null ? cs.getObject(
							12).toString() : "0"));
			valuesMap.put("copay", copayValues);

			// return valuesMap;
			Collections.sort(copayValues);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public String getPolicyAgeing(Date admissionDate, String policyNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_POL_AGE(?, ?, ?, ?)}");
			cs.setString(1, policyNumber);
			cs.setString(2, admissionDate != null ? new java.sql.Date(
					admissionDate.getTime()).toString() : null);

			System.out.println("----policy number----" + policyNumber);
			System.out.println("---the admission date00000"
					+ new java.sql.Date(admissionDate.getTime()).toString());

			// Result is an java.sql.Array...
			/*
			 * cs.registerOutParameter(3, Types.INTEGER, "LN_YR");
			 * cs.registerOutParameter(4, Types.INTEGER, "LN_MNT");
			 */
			cs.registerOutParameter(3, Types.INTEGER, "LN_YEAR");
			cs.registerOutParameter(4, Types.INTEGER, "LN_MONTH");
			cs.execute();
			// Adding null check
			Integer year = (null != cs.getObject(3) ? Integer.valueOf(cs
					.getObject(3).toString()) : 0);
			Integer month = (null != cs.getObject(4) ? Integer.valueOf(cs
					.getObject(4).toString()) : 0);
			String yearStr = " Year ";
			if (year > 1) {
				yearStr = " Years ";
			}

			String monthStr = " Month ";
			if (month > 1) {
				monthStr = " Months ";
			}
			output = year + yearStr + month + monthStr;
			// return
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Boolean getPolicyAgeingForPopup(Date admissionDate,
			String policyNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		Boolean output = false;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_POL_AGE(?, ?, ?, ?)}");
			cs.setString(1, policyNumber);
			cs.setString(2, admissionDate != null ? new java.sql.Date(
					admissionDate.getTime()).toString() : null);

			System.out.println("----policy number----" + policyNumber);
			System.out.println("---the admission date00000"
					+ new java.sql.Date(admissionDate.getTime()).toString());

			cs.registerOutParameter(3, Types.INTEGER, "LN_YEAR");
			cs.registerOutParameter(4, Types.INTEGER, "LN_MONTH");
			cs.execute();
			// Adding null check
			Integer year = (null != cs.getObject(3) ? Integer.valueOf(cs
					.getObject(3).toString()) : 0);
			if (year.equals(0)) {
				output = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public void invokeAccumulatorProcedure(Long preauthKey) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CLM_ACCUMULATOR(?)}");
			cs.setLong(1, preauthKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void invokeAccumulatorForOP(String policyNo,String healthCardNo,String sectionCode) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_OP_HC_ACCUMULATOR(?,?,?)}");
			cs.setString(1, policyNo);
			cs.setString(2, healthCardNo);
			cs.setString(3, sectionCode);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateProvisionAmount(Long rodKey, Long claimKey) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CLM_PROVISON_AMT_CALC(?,?)}");
			cs.setLong(1, rodKey);
			cs.setLong(2, claimKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void getBillDetailsSummary(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_ROD_PRE_POS_HOSPITAL_SPLIT(?)}");
			cs.setLong(1, rodKey);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void callBillAssessmentSheet(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ROD_BILL_SUMMARY(?)}");
			cs.setLong(1, rodKey);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void getICURoomRentSplit(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_ROD_ICU_ROOM_RENT_SPLIT(?)}");
			cs.setLong(1, rodKey);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void getICURoomRentCalc(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call  PRC_ROD_ICU_ROOM_RENT_CALC(?)}");
			cs.setLong(1, rodKey);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void getBillItemCalc(Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call  PRC_ROD_BILL_ITEM_CALC(?, ? , ?)}");
			cs.setLong(1, rodKey);
			cs.setString(2, "N");
			cs.setString(3, "Y");
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void invokeReimbursementAccumulatorProcedure(Long reimbursementKey) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CLM_REIMB_ACCUMULATOR(?)}");
			cs.setLong(1, reimbursementKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// The below method has been commented, because Double should be the return
	// type but not Integer
	/*
	 * public Integer getInsuredSumInsured(Long policyKey, String insuredId) {
	 * //public Double getInsuredSumInsured(Long policyKey, String insuredId) {
	 * Connection connection = null; Integer sumInsured = null; //Double
	 * sumInsured = null; try { connection = BPMClientContext.getConnection();
	 * CallableStatement cs = connection
	 * .prepareCall("{call PRC_INSURED_SUM_INSURED(?, ?, ?)}"); cs.setLong(1,
	 * policyKey); cs.setString(2, insuredId);
	 * 
	 * System.out.println("---- policy Key ---- " + policyKey);
	 * System.out.println("--- insured Id ****** " + insuredId);
	 * 
	 * // Result is an java.sql.Array... cs.registerOutParameter(3,
	 * Types.INTEGER, "LN_SUM_INSURED"); cs.execute();
	 * 
	 * sumInsured = (Integer) cs.getObject(3); //sumInsured = (Double)
	 * cs.getObject(3);
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } } catch (SQLException e) {
	 * e.printStackTrace(); } } return sumInsured; }
	 */

	/**
	 * The below is an overridden method added while fixing issues which occured
	 * when testing policy refractory changes. Double is expected instead of
	 * Integer value. Changing above method will have an impact on existing
	 * code. Hence below method was added.
	 * */

	public Double getInsuredSumInsured(String insuredId, Long policyKey,
			String lobFlag) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		if (lobFlag == null) {
			lobFlag = "H";
		}
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
			// .prepareCall("{call PRC_INSURED_SUM_INSURED(?, ?, ?)}");
					.prepareCall("{call PRC_PA_INSURED_SUM_INSURED(?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, Long.valueOf(insuredId));
			cs.setString(3, lobFlag);

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(4, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(4) ? Double.parseDouble(cs
					.getObject(4).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	/**
	 * The below method will invoke a procedure to check wheter whether product
	 * benefits are available for given product and given claim key
	 * */

	public Map<String, Integer> getProductBenefitFlag(Long claimKey,
			Long productId) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Integer> productBenefitMap = new HashMap<String, Integer>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PRODUCT_BENEFIT_CHECK1(?, ?, ?, ?, ?,?,?,?)}");
			cs.setLong(1, claimKey);
			cs.setLong(2, productId);
			cs.registerOutParameter(3, Types.INTEGER, "LN_LUMPSUM_FLAG");
			cs.registerOutParameter(4, Types.INTEGER, "LN_PATIENT_CARE_FLAG");
			cs.registerOutParameter(5, Types.INTEGER, "LN_HOSPITAL_CASH_FLAG");
			cs.registerOutParameter(6, Types.INTEGER, "LN_POST_HOSP_FLAG");
			cs.registerOutParameter(7, Types.INTEGER, "LN_PRE_HOSP_FLAG");
			cs.registerOutParameter(8, Types.INTEGER, "LN_OTHR_BEN_FLAG");

			cs.execute();
			Integer strLumpSumFlag = (Integer) cs.getInt(3);
			Integer patientCareFlag = (Integer) cs.getInt(4);
			Integer hospitalCashFlag = (Integer) cs.getInt(5);
			Integer postHospFlag = (Integer) cs.getInt(6);
			Integer preHospFlag = (Integer) cs.getInt(7);
			Integer otherBenefitsFlag = (Integer) cs.getInt(8);
			/*
			 * String strLumpSumFlag = (String) cs.getObject(3); String
			 * patientCareFlag = (String) cs.getObject(4); String
			 * hospitalCashFlag = (String) cs.getObject(5);
			 */
			productBenefitMap.put(SHAConstants.LUMP_SUM_FLAG, strLumpSumFlag);
			productBenefitMap.put(SHAConstants.PATIENTCARE_FLAG,
					patientCareFlag);
			productBenefitMap.put(SHAConstants.HOSPITALCASH_FLAG,
					hospitalCashFlag);
			productBenefitMap.put(SHAConstants.PRE_HOSP_FLAG, preHospFlag);
			productBenefitMap.put(SHAConstants.POST_HOSP_FLAG, postHospFlag);
			productBenefitMap.put(SHAConstants.OTHER_BENEFITS_FLAG,
					otherBenefitsFlag);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return productBenefitMap;

	}

	/**
	 * The below method will invoke a procedure to check whether lumpsum amount
	 * for the product.
	 * */

	public Double getLumpSumAmount(Long productId, Long claimKey,
			String sectionSubCoverCode) {
		Connection connection = null;
		CallableStatement cs = null;
		Double lumpSumAmt = 0d;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_LIMIT_SEC_CALC(?, ?, ?, ?)}");
			cs.setLong(1, productId);
			cs.setLong(2, claimKey);
			cs.setString(3, sectionSubCoverCode);
			cs.registerOutParameter(4, Types.DOUBLE, "LN_AVAIL_AMT");
			/*
			 * cs.registerOutParameter(4, Types.INTEGER,
			 * "LN_PATIENT_CARE_FLAG"); cs.registerOutParameter(5,
			 * Types.INTEGER, "LN_HOSPITAL_CASH_FLAG");
			 * cs.registerOutParameter(6, Types.INTEGER, "LN_POST_HOSP_FLAG");
			 * cs.registerOutParameter(7, Types.INTEGER, "LN_PRE_HOSP_FLAG");
			 */
			cs.execute();
			lumpSumAmt = (Double) cs.getDouble(4);

			/*
			 * String strLumpSumFlag = (String) cs.getObject(3); String
			 * patientCareFlag = (String) cs.getObject(4); String
			 * hospitalCashFlag = (String) cs.getObject(5);
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lumpSumAmt;

	}

	public Double getProportionalDeductionPercentage(Long rodKey) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		Double proportionalDeduction = 0d;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PROPORTINAL_DEDUCTION_CALC(?,?)}");
			cs.setLong(1, rodKey);

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(2, Types.DOUBLE, "LN_PRO_DED_PERCNT");
			cs.execute();
			proportionalDeduction = (null != cs.getObject(2) ? Double
					.parseDouble(cs.getObject(2).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return proportionalDeduction;
	}

	public void populateHospitalisationData(Long rodKey, String packageFlag,
			String deductionFlag) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		// Double proportionalDeduction = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ROD_BILL_ITEM_CALC(?,?,?)}");
			cs.setLong(1, rodKey);
			cs.setString(2, packageFlag);
			cs.setString(3, deductionFlag);
			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			// cs.registerOutParameter(2, Types.DOUBLE, "LN_PRO_DED_PERCNT");
			cs.execute();
			// proportionalDeduction = (null != cs.getObject(2) ?
			// Double.parseDouble(cs.getObject(2).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return proportionalDeduction;
	}

	public void rechargingSIbasedOnProduct(Long policyKey, Long insuredKey) {

		Connection connection = null;
		CallableStatement cs = null;
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_RECHARGED_SI(?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Double> getPostHospitalizationValues(Long rodKey) {
		final String limitPercentage = "LN_LIMIT_PERCENTAGE";
		final String limitAmount = "LN_LIMIT_AMOUNT";

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call   PRC_POST_HOSPITALIZATION_CALC(?, ?, ?)}");
			cs.setLong(1, rodKey);
			cs.registerOutParameter(2, Types.DOUBLE, limitPercentage);
			cs.registerOutParameter(3, Types.DOUBLE, limitAmount);

			cs.execute();
			if (cs.getObject(2) != null) {
				values.put(SHAConstants.POST_HOSP_LIMIT_PERCENTAGE,
						Double.parseDouble(cs.getObject(2).toString()));
			}
			if (cs.getObject(3) != null) {
				values.put(SHAConstants.POST_HOSP_LIMIT_AMOUNT,
						Double.parseDouble(cs.getObject(3).toString()));
			}

			// return values;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public Map<String, Object> getAutoRestroation(String intimationId) {
		Connection connection = null;
		CallableStatement cs = null;
		final String typeTableName = "LC_ACTION";
		final String typeCountName = "LV_RESTORED_SI_COUNT";
		String outString = "NA";
		Map<String, Object> restorationMap = new HashMap<String, Object>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_AUTORESTORATION_CHK(?, ?,?)}");
			cs.setString(1, intimationId);
			cs.registerOutParameter(2, Types.INTEGER, typeTableName);
			cs.registerOutParameter(3, Types.INTEGER, typeCountName);
			cs.execute();
			if (cs.getObject(2) != null) {
				String string = cs.getObject(2).toString();
				outString = "NOT DONE";
				if (SHAUtils.getIntegerFromStringWithComma(string).equals(1)) {
					outString = "DONE";
				} else if (SHAUtils.getIntegerFromStringWithComma(string)
						.equals(2)) {
					outString = "N/A";
				}
				// return outString;
				restorationMap
						.put(SHAConstants.AUTO_RESTORATION_CHK, outString);
			}

			if (cs.getObject(3) != null) {
				String count = cs.getObject(3).toString();
				restorationMap.put(SHAConstants.AUTO_RESTORATION_COUNT, count);
			}
			// return "NA";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return restorationMap;
	}

	public Double getAvailableNonNetworkSI(Long intimationKey,
			Integer approvedAmt, Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double output = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CLAIM_RESTRICTION(?, ?, ?, ?)}");
			cs.setLong(1, intimationKey);
			cs.setLong(2, rodKey);
			cs.setLong(3, approvedAmt.longValue());
			cs.registerOutParameter(4, Types.DOUBLE, "LN_RESTRICTED_AMT");

			cs.execute();
			if (cs.getObject(4) != null) {
				output = Double.valueOf(cs.getObject(4).toString());
			}

			// return 0d;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Double getDialysisAmount(Long productKey, Long sumInsured) {

		Connection connection = null;
		CallableStatement cs = null;
		Double output = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PRODUCT_DIALYSIS_LMT(?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setLong(2, sumInsured);
			cs.registerOutParameter(3, Types.DOUBLE, "LM_LMT_AMT");

			cs.execute();
			if (cs.getObject(3) != null) {
				output = Double.valueOf(cs.getObject(3).toString());
			}

			// return 0d;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public void reimbursementRollBackProc(Long transactionKey,
			String transactionFlag) {

		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_DIAG_PROC_AMT_RLBK(?, ?)}");
			cs.setLong(1, transactionKey != null ? transactionKey : 0l);
			cs.setString(2, transactionFlag);

			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String generateReminderBatchId(String sequenceName) {
		String nextVal = "";
		Connection connection = null;
		Statement sqlStatement = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			sqlStatement = connection.createStatement();
			rs = sqlStatement.executeQuery("Select " + sequenceName
					+ ".NEXTVAL from dual");
			if (rs.next()) {
				nextVal = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (sqlStatement != null) {
					sqlStatement.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nextVal;
	}

	public Long generateSequence(String sequenceName) {
		Long nextVal = 0l;
		Connection connection = null;
		Statement sqlStatement = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			sqlStatement = connection.createStatement();
			rs = sqlStatement.executeQuery("Select " + sequenceName
					+ ".NEXTVAL from dual");
			if (rs.next()) {
				int inextVal = rs.getInt(1);
				nextVal = Long.valueOf(inextVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (sqlStatement != null) {
					sqlStatement.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nextVal;
	}

	public Boolean getInsuredEntryAgeCheck(Long insuredNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		Boolean output = false;
		final String typeTableName = "LN_ENTRY_AGE_CHK";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_INSURED_ENTRY_AGE_CHK(?, ?)}");
			cs.setLong(1, insuredNumber);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.execute();
			if (cs.getObject(2) != null) {
				output = (SHAUtils.getIntegerFromString(
						cs.getObject(2).toString()).equals(1) ? true : false);
				// return
				// ((String)cs.getObject(2).toString()).equalsIgnoreCase("Y") ?
				// true : false;
			}
			// return false;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Map<String, String> getPOPUPMessages(Long policyKey,
			Long insuredKey, Long productKey) {
		final String insuredChange = "LV_INSURED_CNT_CNG_CHK";
		final String siChange = "LV_SI_CNG_CHK";
		final String portability = "LV_PORTABLITY_CHK";
		final String breakInsurance = "LV_BRK_INSURANCE_CHK";
		Map<String, String> popupMap = new HashMap<String, String>();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_POPUP_MSG_FLAGS(?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.registerOutParameter(3, Types.INTEGER, insuredChange);
			cs.registerOutParameter(4, Types.INTEGER, siChange);
			cs.registerOutParameter(5, Types.INTEGER, portability);
			cs.registerOutParameter(6, Types.INTEGER, breakInsurance);

			cs.execute();

			if (cs.getObject(3) != null) {
				if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(3).toString()).equals(1)) {
					popupMap.put(SHAConstants.INSURED_REMOVE_CHANGE_MESSAGE,
							"<b style = 'black'>MEMBER REMOVED IN THE CURRENT POLICY : </b> ");
				} else if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(3).toString()).equals(2)) {
					popupMap.put(SHAConstants.INSURED_CHANGE_MESSAGE,
							"<b style = 'black'>MEMBER ADDED IN THE CURRENT POLICY : </b> ");
				}
			}
			if (cs.getObject(4) != null) {
				if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(4).toString()).equals(1)) {

					if (ReferenceTable.getHealthGainProducts().containsKey(
							productKey)) {

						popupMap.put(
								SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN,
								"<b style = 'black'>CHANGE IN SUM INSURED : </b>");
					} else {

						popupMap.put(SHAConstants.SUM_INSURED_CHANGE_MESSAGE,
								"<b style = 'black'>CHANGE IN SUM INSURED : </b>");
					}

				}
			}

			if (cs.getObject(5) != null) {
				if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(5).toString()).equals(1)) {
					popupMap.put(SHAConstants.PORTABILITY_CHANGE_MESSAGE,
							"<b style = 'black'>PROTABILITY : </b>");
				}
			}

			if (cs.getObject(6) != null) {
				if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(6).toString()).equals(1)) {
					popupMap.put(SHAConstants.BREAK_INSURANCE_MESSAGE,
							"<b style = 'black'>BREAK IN INSURANCE : </b> ");
				}
			}
			// return popupMap;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return popupMap;
	}

	public void updateRemarksAndProvisionForClearCashless(Long intimationKey,
			String userId, String remarks) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CLEAR_CASHLESS_INS(?, ?, ?)}");
			cs.setLong(1, intimationKey);
			cs.setString(2, userId);
			cs.setString(3, remarks);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	// Below procedure is used to retrieve the available amount including pre
	// and post utilization if bariatric sublimit is selected.
	public Double getBariatricSublimitAmount(Long rodKey, Long productKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Double output = 0d;
		final String typeTableName = "LN_AVL_AMT";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BARTRICT_LMT_CHK_1(?, ?, ?)}");
			cs.setLong(1, rodKey);
			cs.setLong(2, productKey);
			cs.registerOutParameter(3, Types.DOUBLE, typeTableName);
			cs.execute();
			if (cs.getObject(3) != null) {
				output = (SHAUtils.getDoubleFromStringWithComma(cs.getObject(3)
						.toString()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public String initiateTaskProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		String successMsg = "";
		try {

			// connection = BPMClientContext.getConnectionFromURL();
			// connection = getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_SUBMIT_TASK_1", conn);
			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_SUBMIT_TASK (?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println("---THE STRING---" + cs.getString(2));

			if (cs.getString(2) != null) {
				successMsg = cs.getNString(2).toString();
				// return successMsg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return successMsg;
	}

	public List<Map<String, Object>> getTaskProcedureUnlock(String intimationNo) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_LOCKBY (?,?)}");
			cs.setString(1, intimationNo);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getLockObjFromCursor(rs);
					list.add(mappedValues);
				}
				// return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<Map<String, Object>> getTaskProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_GET_TASK", conn);
			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				// return list;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public List<Map<String, Object>> revisedGetTaskProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start TYP_SEC_CSH_ROD_GET_TASK "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public String callLockProcedure(Long wrkFlowKey, String currentqueue,
			String userID) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_LOCK_USER (?,?,?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.setString(2, currentqueue);
			cs.setString(3, userID);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getString(4) != null) {
				successMsg = cs.getString(4);
			}

			// return successMsg;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String closeClaimProcedure(String intimationNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_CLOSE_CLAIM (?,?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}
			// return successMsg;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	// public String reopenClaimProcedure(String intimationNumber){
	//
	// Connection connection = null;
	// CallableStatement cs =null;
	// String successMsg="";
	// try{
	//
	// connection = BPMClientContext.getConnection();
	//
	// cs = connection.prepareCall("{call PRC_SEC_REOPEN_CLAIM (?,?)}");
	// cs.setString(1, intimationNumber);
	// cs.registerOutParameter(2, Types.VARCHAR);
	// cs.execute();
	//
	// if(cs.getString(2) != null) {
	// successMsg = cs.getString(2);
	// }
	//
	// return successMsg;
	//
	//
	// }catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	//
	// }

	public String callUnlockProcedure(Long wrkFlowKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_UNLOCK_USER (?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}

			// return successMsg;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}

	public String getBatchNumerForShadowProvision() {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_PROVISION_UPLOAD_BATCH(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.execute();

			if (cs.getString(1) != null) {
				successMsg = cs.getString(1);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String callUnlockProcedure(VaadinSession ss) {

		// Integer existingTaskNumber= (Integer)
		// ss.getAttribute(SHAConstants.TOKEN_ID);
		// String userName=(String)ss.getAttribute(BPMClientContext.USERID);
		// String passWord=(String)ss.getAttribute(BPMClientContext.PASSWORD);
		Long wrkFlowKey = (Long) ss.getAttribute(SHAConstants.WK_KEY);
		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_UNLOCK_USER (?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String getLockUser(Long wrkFlowKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_LOCK_USER_DTS (?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String stopReminderProcessProcedure(String intimationNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_REMAINDER_END (?,?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String revisedInitiateTaskProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		String successMsg = "";
		try {

			// connection = BPMClientContext.getConnectionFromURL();
			// connection = getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_SUBMIT_TASK", conn);
			/* "TYP_SEC_CSH_ROD_SUBMIT_TASK1", connection); */

			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_CSH_ROD_SUBMIT_TASK (?,?)}");
			/* .prepareCall("{call PRC_SEC_CSH_ROD_SUBMIT_TASK_pd (?,?)}"); */
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println("---THE STRING---" + cs.getString(2));

			if (cs.getString(2) != null) {
				successMsg = cs.getNString(2).toString();
				// return successMsg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}

	public void pullBackSubmitProcedure(Long workFlowKey, String strOutcome,
			String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_SUBMIT_PULLBACK (?,?,?)}");
			cs.setLong(1, workFlowKey);
			cs.setString(2, strOutcome);
			cs.setString(3, userId);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* return null; */

	}

	public String stopReminderProcessProcedure(String intimationNumber,
			String reminderCategory) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_REMAINDER_END (?,?,?)}");
			cs.setString(1, intimationNumber);
			cs.setString(2, reminderCategory);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			if (cs.getString(3) != null) {
				successMsg = cs.getString(3);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public List<Map<String, Object>> getTaskProcedureForCloseClaim(
			String intimationNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_CSH_ROD_GET_CLOSE_CLM (?,?)}");
			cs.setString(1, intimationNumber);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			// Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					// mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				// return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public List<Map<String, Object>> getTaskProcedureForCloseClaimRodLevel(
			String intimationNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_CSH_ROD_GET_CLOSE_ROD (?,?)}");
			cs.setString(1, intimationNumber);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				// return list;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public void submitProcedureReopenClaim(Long workFlowKey,
			String intimationNumber, String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		// String successMsg="";
		try {

			connection = BPMClientContext.getConnection();
			// connection = getConnectionFromURL();

			/*
			 * PRC_SEC_SUBMIT_PULLBACK(LN_WK_KEY IN NUMBER,LV_OUTCOME
			 * VARCHAR2,LV_USER_ID VARCHAR2)
			 */

			/*
			 * ArrayDescriptor des =
			 * ArrayDescriptor.createDescriptor("TYP_SEC_CSH_ROD_SUBMIT_TASK",
			 * connection); //LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK ARRAY arrayToPass
			 * = new ARRAY(des,connection,input);
			 */
			cs = connection
					.prepareCall("{call PRC_SEC_REOPEN_CLAIM (?,?,?,?)}");
			// cs.setArray(1, arrayToPass);
			cs.setLong(1, workFlowKey);
			cs.setString(2, intimationNumber);
			cs.setString(3, userId);
			cs.registerOutParameter(4, Types.VARCHAR);

			cs.execute();

			// System.out.println("---THE STRING---"+cs.getString(2));

			/*
			 * if(cs.getString(2) != null) { successMsg =
			 * cs.getNString(2).toString(); return successMsg; }
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* return null; */

	}

	public SearchPreauthTableDTO getDetailsForAutoAlloctionForUser(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		SearchPreauthTableDTO resultDto = new SearchPreauthTableDTO();

		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_SEC_ASSIGN_CNT(?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			// Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					// String userName = rs.getString("USER_ID");
					Integer assigned = rs.getInt("TOTAL_ASSIGNED");
					Integer completed = rs.getInt("COMPLETED");
					Integer pending = rs.getInt("PENDING");
					resultDto.setAssignedValue(assigned);
					resultDto.setPendingValue(pending);
					resultDto.setCompletedValue(completed);

				}
				// return resultDto;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultDto;
	}

	public List<FVRAssignmentReportTableDTO> getFVRReportList(
			java.sql.Date fromDate, java.sql.Date toDate, String reportType,
			Long claimType, Long cpuCode, Long fvrCpuCode, String userId) {

		List<FVRAssignmentReportTableDTO> resultFVRList = new ArrayList<FVRAssignmentReportTableDTO>();

		final String FVR_REPORT_PROC = "{call PRC_RPT_FVR_ASSG_PENDING_SKIP(?, ?, ?, ?, ?, ?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(FVR_REPORT_PROC);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, cpuCode != null ? cpuCode : 0);
			cs.setLong(4, fvrCpuCode != null ? fvrCpuCode : 0);
			cs.setString(5, reportType);
			cs.setLong(6, claimType != null ? claimType : 0);
			cs.setString(7, userId);

			cs.registerOutParameter(8, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(8);

			if (rs != null) {
				FVRAssignmentReportTableDTO newFvrDto = null;
				while (rs.next()) {

					newFvrDto = new FVRAssignmentReportTableDTO();
					newFvrDto.setIntimationNo(rs.getString("INTIMATION NO"));

					// if (rs.getString("Date of Admission") != null) {
					//
					// String dateValue = SHAUtils.parseDate(
					// rs.getDate("Date of Admission"),
					// SHAUtils.DEFAULT_DATE_WITHOUT_TIME_FORMAT);
					// newFvrDto.setDateofAdmissionValue(dateValue);
					// }
					newFvrDto.setDateofAdmissionValue(rs
							.getString("Date of Admission"));
					newFvrDto.setAdmissionType(rs.getString("Admission Type"));
					newFvrDto.setPatientName(rs.getString("Patient Name"));
					newFvrDto.setHospitalName(rs.getString("Hospital Name"));
					newFvrDto.setHospitalType(rs.getString("Hospital Type"));
					newFvrDto.setLocation(rs.getString("Location"));
					newFvrDto.setProductName(rs.getString("Policy Type"));
					newFvrDto.setClaimType(rs.getString("Claim Type"));
					newFvrDto.setRepresentativeType(rs
							.getString("Representative Type"));
					newFvrDto.setPointToFocus(rs.getString("Point To Focus"));
					newFvrDto.setInitiatorId(rs.getString("Initiator ID"));
					newFvrDto.setInitiatorName(rs.getString("Initiator Name"));
					newFvrDto.setRepresentativeId(rs
							.getString("Representative Id"));
					newFvrDto.setRepresentativeName(rs
							.getString("Representative Name"));
					newFvrDto.setFvrAssigneeName(rs
							.getString("FVR Assignee Name"));

					newFvrDto.setFvrDateValue(rs
							.getString("FVR Initiated Date"));

					newFvrDto.setFvrTime(rs.getString("FVR Initiated Time"));

					newFvrDto.setFvrAssignedDateValue(rs
							.getString("FVR Assigned Date"));

					newFvrDto.setFvrAssignedTime(rs
							.getString("FVR Assigned Time"));

					newFvrDto.setFvrReceivedDateValue(rs
							.getString("FVR Received Date"));

					newFvrDto.setFvrReceivedTime(rs
							.getString("FVR Received Time"));
					newFvrDto.setTat(rs.getString("TAT"));
					newFvrDto.setCpuCode(rs.getString("CPU Code"));
					newFvrDto.setFvrCpuCode(rs.getString("FVR CPU Code"));
					newFvrDto.setFvrExecutiveComments(rs
							.getString("FVR Executive Comments"));
					newFvrDto.setFvrNotRequiredComments(rs
							.getString("FVR Not Required Comments"));
					newFvrDto.setHospitalCode(rs.getString("Hospital Code"));
					resultFVRList.add(newFvrDto);
					newFvrDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultFVRList;
	}

	public List<DailyReportTableDTO> getDailyReportList(java.sql.Date fromDate,
			java.sql.Date toDate) {

		List<DailyReportTableDTO> dailyReportList = new ArrayList<DailyReportTableDTO>();

		final String DAILY_REPORT_PROC = "{call prc_rpt_daily_cashless(?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(DAILY_REPORT_PROC);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);

			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				DailyReportTableDTO newDailyDto = null;
				while (rs.next()) {

					newDailyDto = new DailyReportTableDTO();

					newDailyDto.setIntimationNo(rs.getString("Intimation No"));
					newDailyDto.setIntimationDateValue(rs
							.getString("Intimation Date"));
					newDailyDto.setPolicyNumber(rs.getString("Policy Number"));
					newDailyDto.setProductType(rs.getString("Product Type"));
					newDailyDto.setInsuredName(rs.getString("Insured Name"));
					newDailyDto.setMainMemberName(rs
							.getString("MainMember Name"));
					newDailyDto.setCallerContactNo(rs
							.getString("Caller Contact No"));
					newDailyDto.setHospitalCode(rs.getString("Hospital Code"));
					newDailyDto.setPatientName(rs.getString("Patient Name"));
					newDailyDto.setPatientAge(rs.getDouble("Patient Age"));
					newDailyDto.setAdmissionReason(rs
							.getString("Admission Reason"));
					newDailyDto.setHospitalType(rs.getString("Hospital Type"));
					newDailyDto.setHospitalDateValue(rs
							.getString("Hospital Date"));
					newDailyDto.setHospitalName(rs.getString("Hospital Name"));
					newDailyDto.setCpuCode(rs.getString("Cpu Code"));
					newDailyDto
							.setHospitalState(rs.getString("Hospital State"));
					newDailyDto.setHospitalCity(rs.getString("Hospital City"));
					newDailyDto.setFieldDoctorNameAllocated(rs
							.getString("Field Doctor Name Allocated"));
					newDailyDto.setContactNoOftheDoctor(rs
							.getString("Contact No of the Doctor"));
					newDailyDto.setDateAndTimeOfAllocationValue(rs
							.getString("Date and Time of Allocation"));
					newDailyDto.setRegistrationStatus(rs
							.getString("Registration Status"));
					newDailyDto.setTotalAuthAmount(rs
							.getString("Total Auth Amount"));
					newDailyDto.setAuthDateValue(rs.getString("Auth Date"));
					newDailyDto.setCashlessStatus(rs
							.getString("Cashless Status"));
					newDailyDto.setCashlessReason(rs
							.getString("Cashless Reason"));

					dailyReportList.add(newDailyDto);
					newDailyDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dailyReportList;
	}

	public List<ClaimsStatusReportDto> getClaimsReportList(
			java.sql.Date fromDate, java.sql.Date toDate, String claimStatus,
			String cpuCode, String user, String period,
			java.sql.Date fyFromDate, java.sql.Date fyToDate) {

		List<ClaimsStatusReportDto> claimsReportList = new ArrayList<ClaimsStatusReportDto>();

		final String CLAIMS_REPORT_PROC = "{call PRC_RPT_CLAIMS_ALL_STATUS(?, ?, ?, ?, ?, ?, ?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CLAIMS_REPORT_PROC);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setString(3, claimStatus);
			cs.setLong(4, (cpuCode != null) ? Long.valueOf(cpuCode) : 0L);
			cs.setString(5, period);
			cs.setString(6, user);
			cs.setDate(7, fyFromDate);
			cs.setDate(8, fyToDate);

			cs.registerOutParameter(9, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(9);

			if (rs != null) {
				Integer sno = 1;
				ClaimsStatusReportDto newClaimsStatusDto = null;
				while (rs.next()) {

					newClaimsStatusDto = new ClaimsStatusReportDto();

					if (claimStatus != null) {
						switch (claimStatus) {
						case SHAConstants.CLAIMS_PAID_STATUS:
							newClaimsStatusDto = setClaimsReportPaid(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.CLAIMS_PRE_AUTH_STATUS:
							newClaimsStatusDto = setClaimsReportPreAuth(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.CLOSED_CLAIMS:
							newClaimsStatusDto = setClaimsReportClosed(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.CLAIM_QUERY:
							newClaimsStatusDto = setClaimsReportQuery(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.MEDICAL_APPROVAL:
							newClaimsStatusDto = setClaimsReportMedical(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.BILLING_COMPLETED:
							newClaimsStatusDto = setClaimsReportBilling(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.CLAIMS_BILL_RECEIVED_STATUS:
							newClaimsStatusDto = setClaimsReportBillReceived(
									newClaimsStatusDto, rs);
							break;
						case SHAConstants.REJECTED_CLAIMS:
							newClaimsStatusDto = setClaimsReportReject(
									newClaimsStatusDto, rs);
							break;
						}
						newClaimsStatusDto.setSno(sno);
					}

					// newClaimsStatusDto.setSno(sno);
					claimsReportList.add(newClaimsStatusDto);

					sno++;
					newClaimsStatusDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return claimsReportList;
	}

	private ClaimsStatusReportDto setClaimsReportPaid(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setClaimNo(rs.getString("Claim No"));
		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setCpuCode(rs.getString("CPU Code"));
		dto.setOffiCode(rs.getString("BRANCH CODE"));
		dto.setOffiName(rs.getString("BRANCH NAME"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setHospitalName(rs.getString("Hospital Name"));
		dto.setHospCity(rs.getString("Hospital City"));
		dto.setPatientName(rs.getString("Patient Name"));
		dto.setPaidDate(rs.getString("paidDate"));
		dto.setPaidAmout(rs.getString("paidAmout"));
		dto.setClaimedAmount(rs.getString("Claimed Amount"));
		dto.setDeductedAmount(rs.getString("deductedAmount"));
		dto.setCashlessOrReimbursement(rs.getString("C/R"));
		dto.setFvrUploaded(rs.getString("fVRUploaded"));
		dto.setMedicalApprovalPerson(rs.getString("medicalApprovalPerson"));
		dto.setBillingPerson(rs.getString("billingPerson"));
		dto.setFinancialApprovalPerson(rs.getString("financialApprovalPerson"));
		dto.setIcdCode(rs.getString("iCDCode"));
		dto.setClaimCoverage(rs.getString("claimCoverage"));
		dto.setSuminsured(rs.getString("suminsured"));
		dto.setPatientAge(rs.getString("Age"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setAdmissionDate(rs.getString("DOA"));
		dto.setIntimationDate(rs.getString("Intimated Date"));
		dto.setScheme(rs.getString("scheme"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportPreAuth(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setClaimNo(rs.getString("claimNo"));
		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setCpuCode(rs.getString("cpuCode"));
		dto.setCpuName(rs.getString("cpuName"));
		dto.setUserId(rs.getString("userId"));
		dto.setUserName(rs.getString("userName"));
		dto.setPatientName(rs.getString("Patient Name"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setHospitalName(rs.getString("Hospital Name"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setPreauthApprovalDate(rs.getString("preauthApprovalDate"));
		dto.setStatus(rs.getString("status"));
		dto.setQueryReplyRemarks(rs.getString("Remarks"));
		dto.setWithDrawRemarks(rs.getString("WITHDRAW_REMARKS"));
		dto.setRejectionRemarks(rs.getString("REJECT_REMARKS"));
		dto.setPreauthAmt(rs.getString("preauthAmt"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportClosed(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setClaimNo(rs.getString("Claim No"));
		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setCpuCode(rs.getString("CPU Code"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setHospitalName(rs.getString("Hospital Name"));
		dto.setPatientName(rs.getString("Patient Name"));
		dto.setClaimedAmount(rs.getString("Claimed Amount"));
		dto.setCashlessOrReimbursement(rs.getString("C/R"));
		dto.setPatientAge(rs.getString("Age"));
		dto.setAdmissionDate(rs.getString("DOA"));
		dto.setCloseDate(rs.getString("closeDate"));
		dto.setClosedRemarks(rs.getString("closedRemarks"));
		dto.setUserId(rs.getString("userId"));
		dto.setUserName(rs.getString("userName"));
		dto.setCloseStage(rs.getString("closeStage"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setInitialProvisionAmount(rs.getString("initialProvisionAmount"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setIntimationDate(rs.getString("Intimated Date"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportQuery(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setOffiCode(rs.getString("Branch Code"));
		dto.setOffiName(rs.getString("Branch Name"));
		dto.setSmCode(rs.getString("SM Code"));
		dto.setSmName(rs.getString("SM Name"));
		dto.setAgentCode(rs.getString("Agent Code"));
		dto.setAgentName(rs.getString("Agent Name"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setHospitalName(rs.getString("hospitalName"));
		dto.setHospCity(rs.getString("Hospital City"));
		dto.setPatientName(rs.getString("patientName"));
		dto.setClaimedAmount(rs.getString("claimedAmount"));
		dto.setCashlessOrReimbursement(rs.getString("cashlessOrReimbursement"));
		dto.setCloseDate(rs.getString("closeDate"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setNoofTimeBillRec(rs.getString("noofTimeBillRec"));
		dto.setCpuCode(rs.getString("cpuCode"));
		dto.setCpuName(rs.getString("cpuName"));
		dto.setUserId(rs.getString("userId"));
		dto.setUserName(rs.getString("userName"));
		dto.setQueryRaisedDate(rs.getString("queryRaisedDate"));
		dto.setQueryRaisedRemarks(rs.getString("queryRaisedRemarks"));
		dto.setQueryReplyDate(rs.getString("queryReplyDate"));
		dto.setQueryReplyRemarks(rs.getString("queryReplyRemarks"));
		dto.setCurrentProvisionAmount(rs.getString("currentProvisionAmount"));
		dto.setStatusValue(rs.getString("Current Status"));
		dto.setDocRecvdFrom(rs.getString("Document Received From"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportMedical(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setCashlessOrReimbursement(rs.getString("cashlessOrReimbursement"));
		dto.setIcdCode(rs.getString("iCDCode"));
		dto.setClaimCoverage(rs.getString("claimCoverage"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setMedicalApprovalPerson(rs.getString("medicalApprovalPerson"));
		dto.setIntimationDate(rs.getString("intimationDate"));
		dto.setMaApprovedDate(rs.getString("maApprovedDate"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setClaimedAmount(rs.getString("claimedAmount"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportBilling(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setClaimedAmount(rs.getString("claimedAmount"));
		dto.setDeductedAmount(rs.getString("deductedAmount"));
		dto.setBillingApprovedAmount(rs.getString("billingApprovedAmount"));
		dto.setBillingPerson(rs.getString("billingPerson"));
		dto.setTat(rs.getString("tat"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportBillReceived(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setPatientName(rs.getString("Patient Name"));
		dto.setCashlessOrReimbursement(rs.getString("C/R"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setHospitalName(rs.getString("Hospital Name"));
		dto.setClaimedAmount(rs.getString("Claimed Amount"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setNoofTimeBillRec(rs.getString("noofTimeBillRec"));
		dto.setCpuCode(rs.getString("CPU Code"));
		dto.setFinacialYear(rs.getString("Financial Year"));

		return dto;
	}

	private ClaimsStatusReportDto setClaimsReportReject(
			ClaimsStatusReportDto dto, ResultSet rs) throws SQLException {

		dto.setIntimationNo(rs.getString("Intimation No"));
		dto.setPolicyNumber(rs.getString("Policy Number"));
		dto.setProductName(rs.getString("Product Name"));
		dto.setPatientName(rs.getString("Patient Name"));
		dto.setIntimationDate(rs.getString("Intimated Date"));
		dto.setAdmissionDate(rs.getString("DOA"));
		dto.setClaimNo(rs.getString("Claim No"));
		dto.setDiagnosis(rs.getString("Diagnosis"));
		dto.setPatientAge(rs.getString("Age"));
		dto.setFinacialYear(rs.getString("Financial Year"));
		dto.setHospitalName(rs.getString("Hospital Name"));
		dto.setRejectDate(rs.getString("rejectDate"));
		dto.setCloseStage(rs.getString("closeStage"));
		dto.setRejectionRemarks(rs.getString("rejectionRemarks"));
		dto.setCashlessOrReimbursement(rs.getString("C/R"));
		dto.setCpuCode(rs.getString("CPU Code"));
		dto.setCpuName(rs.getString("CPU Name"));
		dto.setBillReceivedDate(rs.getString("billReceivedDate"));
		dto.setClaimedAmount(rs.getString("Claimed Amount"));
		dto.setInitialProvisionAmount(rs.getString("initialProvisionAmount"));
		dto.setUserId(rs.getString("userId"));
		dto.setUserName(rs.getString("userName"));

		return dto;
	}

	public List<ProductivityReportTableDTO> getProductivityReportList(
			java.sql.Date date) {

		List<ProductivityReportTableDTO> productivityReportList = new ArrayList<ProductivityReportTableDTO>();

		final String PRODUCTIVITY_REPORT_PROC = "{call PRC_RPT_DAILY_PRODUCTIVITY(?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PRODUCTIVITY_REPORT_PROC);
			cs.setDate(1, date);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				ProductivityReportTableDTO prodDto = null;
				while (rs.next()) {

					prodDto = new ProductivityReportTableDTO();

					prodDto.setIntimationNo(rs.getString("Intimation no"));
					prodDto.setIntimationDate(rs.getString("Intimation date"));
					prodDto.setClaimNumber(rs.getString("Claim Number"));
					prodDto.setPolicyNumber(rs.getString("Policy Number"));
					prodDto.setCashlessReferenceNo(rs
							.getString("Cashless Reference No"));
					prodDto.setClaimType(rs.getString("Claim Type"));
					prodDto.setDateOfInception(rs
							.getString("Date of inception"));
					prodDto.setBreakInPolicy(rs
							.getString("Break in Policy(YES/NO)"));
					prodDto.setNoOfPreviousClaims(rs
							.getInt("No of previous Claims (COUNT)"));
					prodDto.setPortableIfAny(rs
							.getString("Portable if any (YES / NO)"));
					// prodDto.setEndorsementsIfAny(rs
					// .getString("Endorsements If any"));
					prodDto.setCurrentSumInsured(rs
							.getDouble("Current Sum Insured"));
					prodDto.setChangeInSi(rs
							.getString("Change in SI (yes / no)"));
					prodDto.setProductCode(rs.getString("Product Code"));
					prodDto.setProductType(rs.getString("Product Type"));
					prodDto.setInsuredName(rs
							.getString("Insured Name (Proposer)"));
					prodDto.setPatientName(rs.getString("Patient Name"));
					prodDto.setPatientAge(rs.getLong("Patient Age"));
					prodDto.setDoa(rs.getString("DOA"));
					prodDto.setDod(rs.getString("DOD"));
					prodDto.setDiagnosis(rs.getString("Diagnosis (ICT / CPT)"));
					prodDto.setRoomRent(rs.getDouble("Room Rent"));
					prodDto.setIcu(rs.getDouble("ICU"));
					prodDto.setCpuCode(rs.getLong("CPU_CODE"));
					prodDto.setCpuName(rs.getString("CPU Name - Code"));
					prodDto.setRegistrationQueueTime(rs
							.getString("Registration Queue Time"));
					prodDto.setMatchedQueueDateTime(rs
							.getString("Matched Queue Date/Time"));
					prodDto.setResponseDateAndTime(rs
							.getString("Response Date and Time"));
					prodDto.setDocumentType(rs.getString("Document Type"));
					prodDto.setResponseDocType(rs
							.getString("Response Doc Type"));
					prodDto.setProcessingDoctorId(rs
							.getString("Processing Doctor ID"));
					prodDto.setProcessingDoctorName(rs
							.getString("Processing Doctor Name"));
					prodDto.setProcessingDoctorsRemarks(rs
							.getString("Processing Doctors Remarks"));
					prodDto.setUserId(rs.getString("User ID"));
					prodDto.setUserName(rs.getString("User Name"));
					prodDto.setRemarks(rs.getString("Remarks"));
					prodDto.setRequestAmount(rs.getDouble("Request Amount"));
					prodDto.setRequestStatus(rs.getString("Request Status"));
					prodDto.setRequestProcessedDate(rs
							.getString("Request Processed Date"));
					prodDto.setApprovedAmount(rs.getDouble("Approved Amount"));
					prodDto.setHospitalName(rs.getString("Hospital Name"));
					prodDto.setCity(rs.getString("City"));
					prodDto.setAnh(rs.getString("ANH(YES/NO)"));
					prodDto.setDeclaredPedIfAny(rs
							.getString("Declared PED if any"));
					prodDto.setCopay(rs.getDouble("Copay"));

					productivityReportList.add(prodDto);
					prodDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return productivityReportList;
	}
	
	public List<AutomationDashboardTableDTO> getAutomationDashboardReportList(java.sql.Date date) {

		List<AutomationDashboardTableDTO> automationDashboardReportList = new ArrayList<AutomationDashboardTableDTO>();

		final String AUTOMATION_DASHBOARD_REPORT_PROC = "{call PRC_AUTO_LOT_PAYMENT_REPORT(?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(AUTOMATION_DASHBOARD_REPORT_PROC);
			cs.setDate(1, date);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				AutomationDashboardTableDTO autoDashboardDto = null;
				while (rs.next()) {

					autoDashboardDto = new AutomationDashboardTableDTO();

					autoDashboardDto.setSerialNumber(rs.getInt("S_No"));
					//autoDashboardDto.setDate(rs.getString("Date"));
					autoDashboardDto.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
					//autoDashboardDto.setIntimationDate(rs.getString("Intimation date"));
					autoDashboardDto.setLotNumber(rs.getString("LOT_NUMBER"));
					autoDashboardDto.setHospitalCode(rs.getString("HOSPITAL_CODE"));
					autoDashboardDto.setHospitalName(rs.getString("HOSPITAL_NAME"));
					autoDashboardDto.setStatus(rs.getString("STATUS"));
					autoDashboardDto.setApprovedAmount(rs.getDouble("TOT_APPROVED_AMOUNT"));
					autoDashboardDto.setLotCreatedDate(rs.getString("LOT_CREATED_DATE"));
					autoDashboardDto.setBatchNumber(rs.getString("BATCH_NUMBER"));
					autoDashboardDto.setBatchCreatedDate(rs.getString("BATCH_CREATED_DATE"));
					autoDashboardDto.setPmsNeftVerifedStatus(rs.getString("NEFT_STATUS"));
					autoDashboardDto.setBatchAutomated(rs.getString("AUTO_BLOCK"));				
					
					//autoDashboardDto.setLotCreatedDate(rs.getString("Lot Created Date"));
					

					automationDashboardReportList.add(autoDashboardDto);
					autoDashboardDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return automationDashboardReportList;
	}
	
	public List<PaymentPendingDashboardTableDTO> getPaymentPendingDashboardReportList(java.sql.Date date) {

		List<PaymentPendingDashboardTableDTO> PaymentPendingDashboardReportList = new ArrayList<PaymentPendingDashboardTableDTO>();

		final String PAYMENT_PENDING_DASHBOARD_REPORT_PROC = "{call PRC_PAYMENT_PENDING_DASHBOARD(?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PAYMENT_PENDING_DASHBOARD_REPORT_PROC);
			cs.setDate(1,date);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				PaymentPendingDashboardTableDTO ppDashboardDto = null;
				while (rs.next()) {

					ppDashboardDto = new PaymentPendingDashboardTableDTO();

					ppDashboardDto.setSerialNumber(rs.getInt("S.NO"));
					ppDashboardDto.setClaimIntimationNo(rs.getString("Claim Intimation Number"));
					ppDashboardDto.setPayeeName(rs.getString("Payee Name"));
					ppDashboardDto.setHosCode(rs.getString("Hos Code"));
					ppDashboardDto.setClaimType(rs.getString("Claim Type"));
					ppDashboardDto.setApprovedAmount(rs.getDouble("FA Approved Amount"));
					ppDashboardDto.setPaymentFileSentOn(rs.getString("Modified Date"));
					ppDashboardDto.setBatchNumber(rs.getString("Batch Number"));
					ppDashboardDto.setIfscCode(rs.getString("IFSC Code"));
					ppDashboardDto.setAccountNumber(rs.getString("Account Number"));

					PaymentPendingDashboardReportList.add(ppDashboardDto);
					ppDashboardDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return PaymentPendingDashboardReportList;
	}

	public Map<String, Object> getPortablityStatus(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> valuesMap = null;
		try {
			connection = BPMClientContext.getConnection();
			/*
			 * cs = connection
			 * .prepareCall("{call IMS_MASTXN.PRC_RPT_DAILY_PRODU_INN(?, ?, ?, ?)}"
			 * );
			 */
			cs = connection.prepareCall("{call PRC_PRD_PORT(?, ?, ?, ?)}");
			cs.setString(1, intimationNumber);
			cs.setString(2, null);
			cs.registerOutParameter(3, Types.DATE);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(3) != null && cs.getObject(4) != null) {
				valuesMap = new WeakHashMap<String, Object>();
				valuesMap.put(SHAConstants.INCEPTION_DATE, cs.getObject(3));
				valuesMap.put(SHAConstants.PORTABLITY_STATUS, cs.getObject(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public List<FileDetailsReportTableDTO> getFileReportList(
			java.sql.Date fromDate, java.sql.Date toDate, String status) {

		List<FileDetailsReportTableDTO> resultFileList = null;

		final String FVR_REPORT_PROC = "{call PRC_RPT_PHY_STORAGE(?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(FVR_REPORT_PROC);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setString(3, status);
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(4);

			if (rs != null) {
				resultFileList = new ArrayList<FileDetailsReportTableDTO>();
				while (rs.next()) {

					FileDetailsReportTableDTO newFileDto = new FileDetailsReportTableDTO();
					newFileDto.setClientName(rs.getString("CLIENT"));
					newFileDto.setStoragelocation(rs
							.getString("STORAGE_LOCATION"));
					newFileDto.setRackDesc(rs.getString("RACK_DESC"));
					newFileDto.setShelfDesc(rs.getString("SHELF_DESC"));
					newFileDto.setClaimNumber(rs.getString("CLAIM_NUMBER"));
					newFileDto.setPatientName(rs.getString("PATIENT_NAME"));
					newFileDto.setYear(rs.getInt("YEAR"));
					newFileDto.setAlmirahNo(rs.getString("ALMIRAH_NO"));
					// newFileDto.setAddlShelfDesc(rs.getLong("SHELF_DESC1"));
					newFileDto.setBundleNo(rs.getString("BUNDLE_NUMBER"));
					newFileDto.setFileStatus(rs.getString("IN_OUT_FLAG"));

					resultFileList.add(newFileDto);
					newFileDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultFileList;
	}

	public Map<String, Double> getBalanceSIViewForRTA(Long policyKey,
			Long insuredKey, Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_FHO_RTA_VIEW_SI(?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);

			cs.registerOutParameter(4, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(5, Types.DOUBLE, "LN_RTA_RECHARGED_SI");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(4);
			currentBalanceSI = (Double) cs.getObject(5);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.RTA_SUM_INSURED,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public String getLimitAmountValidation(String userId, Long claimedAmount,
			String flag) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_GET_USER_LIMIT(?,?,?,?)}");
			cs.setString(1, userId);
			cs.setLong(2, claimedAmount);
			cs.setString(3, flag);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getString(4) != null) {
				successMsg = cs.getString(4);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}

	/*
	 * public static Connection getConnectionFromURL() throws SQLException {
	 * Connection connection = null; DriverManager.registerDriver(new
	 * oracle.jdbc.OracleDriver()); if (true) { connection = DriverManager
	 * .getConnection
	 * ("jdbc:oracle:thin:IMS_GALTXN/star123@192.168.1.176:1522:GALDEV"); } else
	 * //{ connection = DriverManager.getConnection(
	 * "jdbc:oracle:thin:IMS_GALTXN/Star123@192.168.1.128:1521:GLXQA2DB",
	 * "IMS_GALTXN", "Star123"); //}
	 * 
	 * return connection; }
	 */

	// public BalanceSumInsuredDTO getClaimsOutstandingAmt(Long insuredKey,
	// String intimationNo, Double sumInsured) {
	// final String previousClaimPaid = "LV_PREV_CLM_PAID";
	// final String claimOutstandAmt = "LV_CLM_OUTSTAND";
	// final String provisionAmt = "LV_CURR_PROV_AMT";
	//
	// BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
	// CallableStatement cs= null;

	// Connection connection = null;
	// try {
	// connection = BPMClientContext.getConnection();
	// cs = connection
	// .prepareCall("{call PRC_PREV_CLAIM(?, ?, ?, ?, ?,?)}");
	// cs.setLong(1, insuredKey);
	// cs.setString(2, intimationNo);
	// cs.setDouble(3, sumInsured);
	// cs.registerOutParameter(4, Types.DOUBLE, previousClaimPaid);
	// cs.registerOutParameter(5, Types.DOUBLE, claimOutstandAmt);
	// cs.registerOutParameter(6, Types.DOUBLE, provisionAmt);
	//
	// cs.execute();
	// if (cs.getObject(4) != null) {
	// balanceSumInsuredDTO.setPreviousClaimPaid(Double.parseDouble(cs
	// .getObject(4).toString()));
	// } else {
	// balanceSumInsuredDTO.setPreviousClaimPaid(0.0);
	// }
	// if (cs.getObject(5) != null) {
	// balanceSumInsuredDTO.setClaimOutStanding(Double.parseDouble(cs
	// .getObject(5).toString()));
	// } else {
	// balanceSumInsuredDTO.setClaimOutStanding(0.0);
	// }
	//
	// if(cs.getObject(6) != null){
	// balanceSumInsuredDTO.setProvisionAmout(Double.parseDouble(cs.getObject(6).toString()));
	// } else{
	// balanceSumInsuredDTO.setProvisionAmout(0.0);
	// }
	//
	// // return balanceSumInsuredDTO;
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return balanceSumInsuredDTO;
	// }

	public BalanceSumInsuredDTO getClaimsPaidNOutStandingAmt(Long insuredKey,
			String intimationNo, Double sumInsured) {
		final String previousClaimPaid = "LV_PREV_CLM_PAID";
		final String claimOutstandAmt = "LV_CLM_OUTSTAND";
		final String provisionAmt = "LV_CURR_PROV_AMT";
		final String outsatndingAmount = "LN_RES_OUTSTD_AMT";
		final String paidAmount = "LN_RES_PAID_AMT";

		BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
		CallableStatement cs = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			// cs = connection
			// .prepareCall("{call PRC_PREV_CLAIM(?, ?, ?, ?, ?,?,?,?)}");
			cs = connection
					.prepareCall("{call PRC_PREV_CLAIM_RPT(?, ?, ?, ?, ?,?,?,?)}");
			cs.setLong(1, insuredKey);
			cs.setString(2, intimationNo);
			cs.setDouble(3, sumInsured);
			cs.registerOutParameter(4, Types.DOUBLE, previousClaimPaid);
			cs.registerOutParameter(5, Types.DOUBLE, claimOutstandAmt);
			cs.registerOutParameter(6, Types.DOUBLE, provisionAmt);
			cs.registerOutParameter(7, Types.DOUBLE, outsatndingAmount);
			cs.registerOutParameter(8, Types.DOUBLE, paidAmount);

			cs.execute();
			if (cs.getObject(4) != null) {
				balanceSumInsuredDTO.setPreviousClaimPaid(Double.parseDouble(cs
						.getObject(4).toString()));
			} else {
				balanceSumInsuredDTO.setPreviousClaimPaid(0.0);
			}
			if (cs.getObject(5) != null) {
				balanceSumInsuredDTO.setClaimOutStanding(Double.parseDouble(cs
						.getObject(5).toString()));
			} else {
				balanceSumInsuredDTO.setClaimOutStanding(0.0);
			}

			if (cs.getObject(6) != null) {
				balanceSumInsuredDTO.setProvisionAmout(Double.parseDouble(cs
						.getObject(6).toString()));
			} else {
				balanceSumInsuredDTO.setProvisionAmout(0.0);
			}

			if (cs.getObject(7) != null) {
				balanceSumInsuredDTO.setOutstandingAmout(Double.parseDouble(cs
						.getObject(7).toString()));
			} else {
				balanceSumInsuredDTO.setOutstandingAmout(0.0);
			}

			if (cs.getObject(8) != null) {
				balanceSumInsuredDTO.setPaidAmout(Double.parseDouble(cs
						.getObject(8).toString()));
			} else {
				balanceSumInsuredDTO.setPaidAmout(0.0);
			}

			// return balanceSumInsuredDTO;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return balanceSumInsuredDTO;
	}

	public BalanceSumInsuredDTO getClaimsOutstandingAmt(Long insuredKey,
			String intimationNo, Double sumInsured) {
		final String previousClaimPaid = "LV_PREV_CLM_PAID";
		final String claimOutstandAmt = "LV_CLM_OUTSTAND";
		final String provisionAmt = "LV_CURR_PROV_AMT";
		final String outsatndingAmount = "LN_RES_OUTSTD_AMT";
		final String paidAmount = "LN_RES_PAID_AMT";

		BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
		CallableStatement cs = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			// cs = connection
			// .prepareCall("{call PRC_PREV_CLAIM(?, ?, ?, ?, ?,?,?,?)}");
			cs = connection
					.prepareCall("{call PRC_PREV_CLAIM_1(?, ?, ?, ?, ?,?,?,?)}");
			cs.setLong(1, insuredKey);
			cs.setString(2, intimationNo);
			cs.setDouble(3, sumInsured);
			cs.registerOutParameter(4, Types.DOUBLE, previousClaimPaid);
			cs.registerOutParameter(5, Types.DOUBLE, claimOutstandAmt);
			cs.registerOutParameter(6, Types.DOUBLE, provisionAmt);
			cs.registerOutParameter(7, Types.DOUBLE, outsatndingAmount);
			cs.registerOutParameter(8, Types.DOUBLE, paidAmount);

			cs.execute();
			if (cs.getObject(4) != null) {
				balanceSumInsuredDTO.setPreviousClaimPaid(Double.parseDouble(cs
						.getObject(4).toString()));
			} else {
				balanceSumInsuredDTO.setPreviousClaimPaid(0.0);
			}
			if (cs.getObject(5) != null) {
				balanceSumInsuredDTO.setClaimOutStanding(Double.parseDouble(cs
						.getObject(5).toString()));
			} else {
				balanceSumInsuredDTO.setClaimOutStanding(0.0);
			}

			if (cs.getObject(6) != null) {
				balanceSumInsuredDTO.setProvisionAmout(Double.parseDouble(cs
						.getObject(6).toString()));
			} else {
				balanceSumInsuredDTO.setProvisionAmout(0.0);
			}

			if (cs.getObject(7) != null) {
				balanceSumInsuredDTO.setOutstandingAmout(Double.parseDouble(cs
						.getObject(7).toString()));
			} else {
				balanceSumInsuredDTO.setOutstandingAmout(0.0);
			}

			if (cs.getObject(8) != null) {
				balanceSumInsuredDTO.setPaidAmout(Double.parseDouble(cs
						.getObject(8).toString()));
			} else {
				balanceSumInsuredDTO.setPaidAmout(0.0);
			}

			// return balanceSumInsuredDTO;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return balanceSumInsuredDTO;
	}

	/***
	 * Method to calculate Balance SI for pre auth.
	 *
	 * key shld be given
	 */
	// public Double getBalanceSumInsured(String policyNumber, int sumInsured)

	// public Map<String, Double> getBalanceSI(Long policyKey, Long insuredKey,
	// Long claimKey,
	// Double sumInsured,Long intimationKey, String subCoverCode) {
	//
	// Connection connection = null;
	// CallableStatement cs= null;
	// Double totalBalanceSI = 0d;
	// Double currentBalanceSI = 0d;
	// Map<String, Double> values = new HashMap<String, Double>();
	// try {
	// connection = BPMClientContext.getConnection();
	// cs = connection
	// .prepareCall("{call PRC_BALANCE_SI(?, ?, ?, ?, ?, ?,?)}");
	// cs.setLong(1, policyKey);
	// cs.setLong(2, insuredKey);
	// cs.setLong(3, intimationKey);
	// cs.setLong(4, claimKey);
	//
	// if (sumInsured != null) {
	// cs.setDouble(5, sumInsured);
	// } else {
	// cs.setDouble(5, 0);
	// }
	//
	// cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
	// cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
	// cs.execute();
	//
	// // return (Double) cs.getObject(4);
	// totalBalanceSI = (Double) cs.getObject(6);
	// currentBalanceSI = (Double) cs.getObject(7);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// // return new Double("0");
	// values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI != null ?
	// totalBalanceSI : 0d);
	// values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI != null ?
	// currentBalanceSI : 0d);
	// return values;
	// }

	public Map<String, Double> getBalanceSIForPAHealth(Long insuredKey,
			Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PA_HOSP_BALANCE_SI(?, ?, ?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);

			cs.registerOutParameter(3, Types.DOUBLE, "LN_AVAIL_SI");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(3);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		return values;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, List> getPABalanceSIView(Long claimKey, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;

		Map<String, List> listOfTable = new HashMap<String, List>();

		List<PABalanceSumInsuredTableDTO> listBalSIDTO = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOPA = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOAddOn = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOOptional = new ArrayList<PABalanceSumInsuredTableDTO>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_VIEW_SI(?, ?, ?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);
			cs.registerOutParameter(3, Types.ARRAY, "TYP_PA_VIEW_SI");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

			for (Object object : data) {

				Struct row = (Struct) object;
				Object[] attributes = row.getAttributes();
				if (attributes != null) {

					String coverCode = (String) attributes[0];
					String coverName = (String) attributes[1];
					Double orgSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[2]));
					Double cumBonus = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[3]));
					Double totalSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[4]));
					Double claimPaid = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[5]));
					Double claimOut = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[6]));
					Double balSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[7]));
					Double provAmt = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[8]));
					Double availSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[9]));

					PABalanceSumInsuredTableDTO balanceSIDTO = new PABalanceSumInsuredTableDTO();

					if (coverName != null
							&& coverName.equalsIgnoreCase(SHAConstants.PA_DESC)) {
						balanceSIDTO.setOrginalSI(orgSI);
						balanceSIDTO.setCumulativeBonus(cumBonus);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setBalanceSI(balSI);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);
						listBalSIDTO.add(balanceSIDTO);
					}
					if (coverName != null
							&& coverName
									.equalsIgnoreCase(SHAConstants.PA_BENEFIT_COVER_DESC)) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setOrginalSI(orgSI);
						if (coverCode != null
								&& (coverCode
										.equalsIgnoreCase(SHAConstants.PPD) || coverCode
										.equalsIgnoreCase(SHAConstants.TTD))) {
							balanceSIDTO.setCumulativeBonus(null);
						} else {
							balanceSIDTO.setCumulativeBonus(cumBonus);
						}
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOPA.add(balanceSIDTO);
					}

					if (coverName != null
							&& coverName
									.equalsIgnoreCase(SHAConstants.PA_OPTIONAL_DESC)) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOOptional.add(balanceSIDTO);
					}

					if (coverName != null
							&& coverName
									.equalsIgnoreCase(SHAConstants.PA_ADDITIONAL_DESC)) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOAddOn.add(balanceSIDTO);
					}
				}
			}

			listOfTable.put(SHAConstants.PA_DESC, listBalSIDTO);
			listOfTable.put(SHAConstants.PA_BENEFIT_COVER_DESC, listBalSIDTOPA);
			listOfTable.put(SHAConstants.PA_ADDITIONAL_DESC, listBalSIDTOAddOn);
			listOfTable
					.put(SHAConstants.PA_OPTIONAL_DESC, listBalSIDTOOptional);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;

	}

	public Map<String, String> getSectionDetailsForView(Long insuredKey,
			String intimationNo, Double sumInsured, Long prodKey) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, String> listOfValues = new HashMap<String, String>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PREV_CLAIM_2(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7, Types.ARRAY, "TYP_SI_LIST");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();

			for (Object object : data) {

				Struct row = (Struct) object;
				Object[] attributes = row.getAttributes();
				if (attributes != null) {

					String sectionNameWithCode = (String) attributes[0];
					String sectionCode = (String) attributes[1];
					String coverName = (String) attributes[2];
					String coverCode = (String) attributes[3];
					String subCoverName = (String) attributes[4];
					String subCoverCode = (String) attributes[5];
					if (!listOfValues.containsKey(sectionCode)) {
						listOfValues.put(sectionCode, sectionNameWithCode);
					}
					if (!listOfValues.containsKey(coverCode)) {
						listOfValues.put(coverCode, coverName);
					}
					if (!listOfValues.containsKey(subCoverCode)) {
						listOfValues.put(subCoverCode, subCoverName);
					}

				}
			}

			BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
			String outstandingAmout = "0.0";
			String paidAmout = "0.0";

			if (cs.getObject(5) != null) {
				outstandingAmout = cs.getObject(5).toString();
			}

			if (cs.getObject(6) != null) {
				paidAmout = cs.getObject(6).toString();
			}

			listOfValues.put("outstandingAmout", outstandingAmout);
			listOfValues.put("paidAmout", paidAmout);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfValues;
	}

	public void updateProvisionAmountForPANonHealth(Long rodKey, Long claimKey) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_PROV_AMT_CALC(?,?)}");
			cs.setLong(1, rodKey);
			cs.setLong(2, claimKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// The below method has been commented, because Double should be the return
	// type but not Integer
	/*
	 * public Integer getInsuredSumInsured(Long policyKey, String insuredId) {
	 * //public Double getInsuredSumInsured(Long policyKey, String insuredId) {
	 * Connection connection = null; Integer sumInsured = null; //Double
	 * sumInsured = null; try { connection = BPMClientContext.getConnection();
	 * CallableStatement cs = connection
	 * .prepareCall("{call PRC_INSURED_SUM_INSURED(?, ?, ?)}"); cs.setLong(1,
	 * policyKey); cs.setString(2, insuredId);
	 * 
	 * System.out.println("---- policy Key ---- " + policyKey);
	 * System.out.println("--- insured Id ****** " + insuredId);
	 * 
	 * // Result is an java.sql.Array... cs.registerOutParameter(3,
	 * Types.INTEGER, "LN_SUM_INSURED"); cs.execute();
	 * 
	 * sumInsured = (Integer) cs.getObject(3); //sumInsured = (Double)
	 * cs.getObject(3);
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } } catch (SQLException e) {
	 * e.printStackTrace(); } } return sumInsured; }
	 */

	// Below procedure is used to retrieve the available amount including pre
	// and post utilization if bariatric sublimit is selected.

	// public String reopenClaimProcedure(String intimationNumber){
	//
	// Connection connection = null;
	// CallableStatement cs =null;
	// String successMsg="";
	// try{
	//
	// connection = BPMClientContext.getConnection();
	//
	// cs = connection.prepareCall("{call PRC_SEC_REOPEN_CLAIM (?,?)}");
	// cs.setString(1, intimationNumber);
	// cs.registerOutParameter(2, Types.VARCHAR);
	// cs.execute();
	//
	// if(cs.getString(2) != null) {
	// successMsg = cs.getString(2);
	// }
	//
	// return successMsg;
	//
	//
	// }catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (connection != null) {
	// connection.close();
	// }
	// if (cs != null) {
	// cs.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	//
	// }

	public BeanItemContainer<SelectValue> getBebefitAdditionalCovers(
			String coverType, Long productId, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;

		BeanItemContainer<SelectValue> coverContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try {
			final String typeName = "OBJ_CLAIM_COVERS ";
			final String typeTableName = "TYP_CLAIM_COVERS";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PA_CLAIM_COVERS(?, ?, ?,?)}");

			cs.setLong(1, insuredKey);
			cs.setString(2, coverType);
			cs.setLong(3, productId);
			cs.registerOutParameter(4, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(4);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(4)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					Long coverKey = null != attributes[0] ? Long.valueOf(String
							.valueOf(attributes[0])) : null;
					String coverCode = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					String coverDescription = null != attributes[2] ? String
							.valueOf(attributes[2]) : null;
					Double maxPolicyAmnt = null != attributes[3] ? Double
							.valueOf("" + attributes[3]) : 0d;

					SelectValue covers = new SelectValue(coverKey,
							coverDescription);

					coverContainer.addBean(covers);
				}
			}
			// System.out.println("---sublimt list ----" + subLimits);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coverContainer;

	}

	public BeanItemContainer<SelectValue> getBenefitCoverValueContainer(
			Long insuredKey, Long benefitId) {
		Connection connection = null;
		CallableStatement cs = null;

		// List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
		BeanItemContainer<SelectValue> coverContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try {
			final String typeTableName = "TYP_CLAIM_BENEFIT_COVER";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PA_CLAIM_BENEFIT_COVER(?, ?, ?)}");

			cs.setLong(1, insuredKey);
			cs.setLong(2, benefitId);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
					String benefitCover = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					String percentage = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					Double sumInsuredValue = null != attributes[2] ? Double
							.valueOf(String.valueOf(attributes[2])) : 0d;
					Double eligibleAmt = null != attributes[3] ? Double
							.valueOf(String.valueOf(attributes[3])) : 0d;
					Long benefitsId = null != attributes[4] ? Long
							.valueOf(String.valueOf(attributes[4])) : 0;

					paBenefitsDTO.setBenefitCover(benefitCover);
					paBenefitsDTO.setPercentage(percentage);
					paBenefitsDTO.setSumInsured(sumInsuredValue);
					paBenefitsDTO.setEligibleAmount(eligibleAmt);
					// paBenefitsList.add(paBenefitsDTO);

					SelectValue covers = new SelectValue(benefitsId,
							benefitCover);
					coverContainer.addBean(covers);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coverContainer;

	}

	public List<PABenefitsDTO> getBenefitCoverValues(Long insuredKey,
			Long benefitId) {
		Connection connection = null;
		CallableStatement cs = null;

		List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_CLAIM_BENEFIT_COVER";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PA_CLAIM_BENEFIT_COVER(?, ?, ?)}");

			cs.setLong(1, insuredKey);
			cs.setLong(2, benefitId);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
					String benefitCover = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					String percentage = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					Double sumInsuredValue = null != attributes[2] ? Double
							.valueOf(String.valueOf(attributes[2])) : 0d;
					Double eligibleAmt = null != attributes[3] ? Double
							.valueOf(String.valueOf(attributes[3])) : 0d;
					Long benefitsId = null != attributes[4] ? Long
							.valueOf(String.valueOf(attributes[4])) : 0;
					Double eligibleAmtPerWeek = null != attributes[5] ? Double
							.valueOf(String.valueOf(attributes[5])) : 0d;

					// Double eligibleAmtPerWeek = 5000d;

					paBenefitsDTO.setBenefitCover(benefitCover);
					paBenefitsDTO.setPercentage(percentage);
					paBenefitsDTO.setSumInsured(sumInsuredValue);
					paBenefitsDTO.setEligibleAmount(eligibleAmt);
					paBenefitsDTO.setBenefitsId(benefitsId);
					if (null != sumInsuredValue) {
						Double calculatedSIValue = sumInsuredValue
								* (1f / 100f);
						paBenefitsDTO.setEligibleAmountPerWeek(Math.min(
								calculatedSIValue, eligibleAmtPerWeek));
					}
					// paBenefitsDTO.setEligibleAmountPerWeek(eligibleAmtPerWeek);
					paBenefitsList.add(paBenefitsDTO);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return paBenefitsList;

	}

	public List<AddOnCoversTableDTO> getClaimCoverValues(String coverType,
			Long productId, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;

		List<AddOnCoversTableDTO> coversList = new ArrayList<AddOnCoversTableDTO>();
		BeanItemContainer<SelectValue> coverContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if (productId != null
				&& ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(productId)) {
			if (coverType.equalsIgnoreCase(SHAConstants.ADDITIONAL_COVER)) {
				coverType = SHAConstants.JET_ADDITIONAL_COVER;
			} else if (coverType.equalsIgnoreCase(SHAConstants.OPTIONAL_COVER)) {
				coverType = SHAConstants.JET_OPTIONAL_COVER;
			}
		}

		try {
			final String typeName = "OBJ_CLAIM_COVERS ";
			final String typeTableName = "TYP_CLAIM_COVERS";

			connection = BPMClientContext.getConnection();
			// connection = getConnectionFromURL();
			cs = connection
					.prepareCall("{call PRC_PA_CLAIM_COVERS(?, ?, ?,?)}");

			cs.setLong(1, insuredKey);
			cs.setString(2, coverType);
			cs.setLong(3, productId);
			cs.registerOutParameter(4, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(4);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(4)).getArray();

				for (Object tmp : data) {
					AddOnCoversTableDTO coversTableDTO = new AddOnCoversTableDTO();

					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					Long coverKey = null != attributes[0] ? Long.valueOf(String
							.valueOf(attributes[0])) : null;
					String coverCode = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					String coverDescription = null != attributes[2] ? String
							.valueOf(attributes[2]) : null;
					Double maxPolicyAmnt = null != attributes[3] ? Double
							.valueOf("" + attributes[3]) : 0d;
					String eligibleFlag = null != attributes[4] ? String
							.valueOf(attributes[4]) : null;

					coversTableDTO.setCoverId(coverKey);
					coversTableDTO.setClaimedAmount(maxPolicyAmnt);
					if (SHAConstants.YES_FLAG.equalsIgnoreCase(eligibleFlag))
						coversTableDTO
								.setEligibleForPolicy(SHAConstants.CAPS_YES);
					else
						coversTableDTO
								.setEligibleForPolicy(SHAConstants.CAPS_NO);

					SelectValue covers = new SelectValue(coverKey,
							coverDescription);
					coversList.add(coversTableDTO);

					// coverContainer.addBean(covers);
				}
			}
			// System.out.println("---sublimt list ----" + subLimits);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coversList;

	}

	/*
	 * public List<PABenefitsDTO> getClaimCoverValues(Long insuredKey,Long
	 * coverTypeKey , Long productKey) { Connection connection = null;
	 * CallableStatement cs =null;
	 * 
	 * List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
	 * //BeanItemContainer<SelectValue> coverContainer = new
	 * BeanItemContainer<SelectValue>(SelectValue.class);
	 * 
	 * try { final String typeTableName = "TYP_CLAIM_COVERS";
	 * 
	 * 
	 * connection = BPMClientContext.getConnection(); cs = connection
	 * .prepareCall("{call PRC_PA_CLAIM_COVERS(?, ?, ?)}");
	 * 
	 * cs.setLong(1, insuredKey); cs.setLong(2,coverTypeKey); cs.setLong(3,
	 * productKey); cs.registerOutParameter(4, Types.ARRAY, typeTableName);
	 * cs.execute();
	 * 
	 * Object object = cs.getObject(3);
	 * 
	 * if(object != null) { Object[] data = (Object[]) ((Array)
	 * cs.getObject(3)).getArray();
	 * 
	 * for (Object tmp : data) { Struct row = (Struct) tmp; Object[] attributes
	 * = row.getAttributes();
	 * 
	 * PABenefitsDTO paBenefitsDTO = new PABenefitsDTO(); String benefitCover =
	 * null != attributes[0] ? String.valueOf(attributes[0]) : null; String
	 * percentage = null != attributes[1] ? String.valueOf(attributes[1]) :
	 * null; Double sumInsuredValue = null != attributes[2] ?
	 * Double.valueOf(String.valueOf(attributes[2])) : 0d ; Double eligibleAmt =
	 * null != attributes[3] ? Double.valueOf(String.valueOf(attributes[3])) :
	 * 0d ;
	 * 
	 * paBenefitsDTO.setBenefitCover(benefitCover);
	 * paBenefitsDTO.setPercentage(percentage);
	 * paBenefitsDTO.setSumInsured(sumInsuredValue);
	 * paBenefitsDTO.setEligibleAmout(eligibleAmt);
	 * 
	 * paBenefitsList.add(paBenefitsDTO);
	 * 
	 * } } } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } if (cs != null) {
	 * cs.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * return paBenefitsList;
	 * 
	 * }
	 */

	public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> getCoverValues(
			Long rodID) {
		Connection connection = null;
		CallableStatement cs = null;

		List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> coversTableList = new ArrayList<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_ADD_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_ADD_CVR_CALC(?, ?)}");

			cs.setLong(1, rodID);
			// cs.setLong(3, productId);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(2);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO coversTableDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal prodSI = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal eligibleAmt = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					coversTableDTO.setCoverId(coverId.longValue());
					coversTableDTO.setEligibleAmount(eligibleAmt.doubleValue());
					coversTableDTO.setSiLimit(prodSI.doubleValue());

					coversTableList.add(coversTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coversTableList;

	}

	/*
	 * public com.shaic.paclaim.billing.processclaimbilling.page.billreview.
	 * AddOnCoversTableDTO getCoverValues(Long coverID) { Connection connection
	 * = null; CallableStatement cs =null;
	 * 
	 * com.shaic.paclaim.billing.processclaimbilling.page.billreview.
	 * AddOnCoversTableDTO coversTableDTO = new
	 * com.shaic.paclaim.billing.processclaimbilling
	 * .page.billreview.AddOnCoversTableDTO();
	 * 
	 * try { connection = BPMClientContext.getConnection(); cs =
	 * connection.prepareCall("{call PRC_PA_ADD_CVR_CALC(?,?,?)}");
	 * cs.setLong(1, coverID); cs.registerOutParameter(2, Types.DOUBLE,
	 * "PROD_SI"); cs.registerOutParameter(3, Types.DOUBLE, "ELIGIBLE_AMT");
	 * cs.execute();
	 * 
	 * Double prodSI = 0d; Double eligibleAmt =0d;
	 * 
	 * if (cs.getObject(2) != null) { prodSI =
	 * Double.valueOf(cs.getObject(2).toString()); } if (cs.getObject(3) !=
	 * null) { eligibleAmt = Double.valueOf(cs.getObject(3).toString()); }
	 * coversTableDTO.setKey(coverID);
	 * coversTableDTO.setEligibleAmount(eligibleAmt);
	 * coversTableDTO.setSiLimit(prodSI);
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } if (cs != null) {
	 * cs.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * return coversTableDTO;
	 * 
	 * }
	 */

	/*
	 * >PRC_PA_BALANCE_SI > >LN_INSURED_KEY - input >LN_CLAIM_KEY input
	 * >LN_ROD_KEY input >LN_AVAIL_SI - output.
	 */

	public Double getPABalanceSI(Long LN_INSURED_KEY, Long LN_CLAIM_KEY,
			Long LN_ROD_KEY, Long benefitsId) {
		Connection connection = null;
		CallableStatement cs = null;

		Double availableSI = 0d;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_BALANCE_SI(?,?,?,?,?)}");
			cs.setLong(1, LN_INSURED_KEY);
			cs.setLong(2, LN_CLAIM_KEY);
			cs.setLong(3, LN_ROD_KEY);
			cs.setLong(4, benefitsId);
			cs.registerOutParameter(5, Types.DOUBLE, "LN_AVAIL_SI");
			cs.execute();

			if (cs.getObject(5) != null) {
				availableSI = Double.valueOf(cs.getObject(5).toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return availableSI;

	}

	public Double getGPAInsuredSumInsuredForHospitalization(String insuredId,
			Long policyKey, Long benefitId) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_HOS_SUM_INSURED (?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, Long.valueOf(insuredId));
			cs.setLong(3, benefitId);

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(4, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(4) ? Double.parseDouble(cs
					.getObject(4).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	public List<OptionalCoversDTO> getOptValues(Long coverID) {
		Connection connection = null;
		CallableStatement cs = null;

		List<OptionalCoversDTO> optTableList = new ArrayList<OptionalCoversDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_OPT_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_OPT_CVR_CALC(?, ?)}");

			cs.setLong(1, coverID);
			// cs.setLong(3, productId);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(2);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					OptionalCoversDTO optTableDTO = new OptionalCoversDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal minDays = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal maxDays = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					BigDecimal utilDays = null != attributes[3] ? (BigDecimal) (attributes[3])
							: null;
					BigDecimal availDays = null != attributes[4] ? (BigDecimal) (attributes[4])
							: null;
					BigDecimal perDayAmt = null != attributes[5] ? (BigDecimal) (attributes[5])
							: null;
					BigDecimal minAmt = null != attributes[6] ? (BigDecimal) (attributes[6])
							: null;
					BigDecimal maxAmt = null != attributes[7] ? (BigDecimal) (attributes[7])
							: null;
					BigDecimal balAmt = null != attributes[8] ? (BigDecimal) (attributes[8])
							: null;
					String coverEligible = null != attributes[9] ? (String) (attributes[9])
							: null;
					optTableDTO.setCoverId(coverId.longValue());
					optTableDTO.setMaxDaysAllowed(maxDays.intValue());
					if (utilDays != null) {
						optTableDTO.setNoOfDaysUtilised(utilDays.intValue());
					}
					if (availDays != null) {
						optTableDTO.setNoOfDaysAvailable(availDays.intValue());
					}
					optTableDTO.setAllowedAmountPerDay(perDayAmt.doubleValue());
					optTableDTO.setMaxNoOfDaysPerHospital(minDays.intValue());
					optTableDTO.setSiLimit(minAmt.doubleValue());
					optTableDTO.setLimit(maxAmt.doubleValue());
					optTableDTO.setBalanceSI(balAmt.doubleValue());
					optTableDTO.setEligibleForPolicy(coverEligible);
					optTableList.add(optTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return optTableList;

	}

	public List<Object> testingArrayAsInput() {
		Connection connection = null;
		OracleConnection conn = null;
		CallableStatement cs = null;
		Object[] objArray = new Object[6];
		Object[] objArray1 = new Object[1];
		Object object1 = new Object();
		// for(int i=0; i <2; i++) {
		// TestingProcedureWithArray obj = new TestingProcedureWithArray();
		// obj.setIntimationNumber("1234");
		// obj.setClaimType("Cashless");
		// obj.setCpuCode(Double.valueOf(2));
		// obj.setPolicyType(Double.valueOf(1));
		String intimationNumber = "1234";
		Long cpu_code = 950001l;
		Long claim_type = 401l;
		String policy_number = "p/2734387";
		Long policy_type = 12l;
		String product_code = "prod";
		objArray[0] = intimationNumber;
		objArray[1] = cpu_code;
		objArray[2] = claim_type;
		objArray[3] = policy_number;
		objArray[4] = policy_type;
		objArray[5] = product_code;
		objArray1[0] = objArray;
		// Object object1 = (Object)obj;
		// }
		try {
			// connection = BPMClientContext.getConnection();
			// String name = connection.getClass().getName();
			// OracleConnection conn =
			// connection.unwrap(OracleConnection.class);
			// OracleConnection conn = (oracle.jdbc.driver.OracleConnection)
			// connection.getMetaData().getConnection();

			// oracle.jdbc.driver.OracleConnection oracleConn =
			// (oracle.jdbc.driver.OracleConnection) connection;

			/*
			 * DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver
			 * ()); connection = DriverManager.getConnection(
			 * "jdbc:oracle:thin:IMS_GALTXN/Star123@192.168.1.128:1521:GLXQA2DB"
			 * , "IMS_GALTXN", "Star123");
			 */

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			// Connection underlyingConnection = null;
			// WrappedConnection wrappedConnection =
			// connection.unwrap(WrappedConnection.class);
			// try {
			// underlyingConnection =
			// wrappedConnection.getUnderlyingConnection();
			//
			// } catch (SQLException e) {
			//
			// }

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEARCH", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, objArray1);
			cs = conn.prepareCall("{call PRC_APP_SEARCH (?, ?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, Types.ARRAY, "TYP_SRH_OUT");
			cs.execute();

			Object object = cs.getObject(2);
			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				if (data.length > 0) {
					System.out.println("Got the object");
				} else {
					System.out.println("some error");
				}
			}

			// if(object != null) {
			// Object[] data = (Object[]) ((Array) object).getArray();
			//
			// for (Object tmp : data) {
			// Struct row = (Struct) tmp;
			// Object[] attributes = row.getAttributes();
			// //Map<Integer, Object> benefitsMap = new HashMap<Integer,
			// Object>();
			//
			// for (Object object2 : attributes) {
			// benefitsValues.add(object2);
			// }
			// /*for(int i = 0 ; i<attributes.length ; i++)
			// {
			// //benefitsMap.put(i,attributes[i]);
			// benefitsValues.add(e)
			//
			// }
			// benefitsValues.add(benefitsMap);*/
			//
			// /*for (Object object2 : attributes) {
			// benefitsValues.add(SHAUtils.getDoubleValueFromString(String.valueOf(object2)))
			// ;
			// }*/
			// }
			// }
			// benefitsValues.add(cs.getObject(6));

			// return benefitsValues;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public Map<Integer, Object> getHospitalizationDetailsForGMC(
			Long productKey, Double sumInsured, String cityClass,
			Long insuredId, Long intimationKey, Long section, String plan) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();

		Long sectionValue = 0l;

		if (sumInsured == null) {
			sumInsured = 0d;
		}

		if (section.equals(ReferenceTable.POL_SECTION_1)) {
			sectionValue = 1l;
		} else if (section.equals(ReferenceTable.POL_SECTION_2)) {
			sectionValue = 2l;
		}

		// //// Prorata Flag 1 = Yes, 0 = No, 2 = Percentage...

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_HOSPITALIZATION(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setDouble(2, sumInsured);
			cs.setString(3, cityClass);
			cs.setLong(4, sectionValue);
			cs.setString(5, plan);
			cs.setLong(6, insuredId);
			cs.setLong(7, intimationKey != null ? intimationKey : 0l);
			// Result is an java.sql.Array...
			cs.registerOutParameter(8, Types.DOUBLE, "LN_AMBULANCE_AMT");
			cs.registerOutParameter(9, Types.DOUBLE, "LN_ICU_AMT");
			cs.registerOutParameter(10, Types.DOUBLE, "LN_ROOM_RENT_AMT");
			cs.registerOutParameter(11, Types.INTEGER, "LV_PRORAT_FLG");
			cs.registerOutParameter(12, Types.DOUBLE, "LN_COPAYLIMIT");
			cs.registerOutParameter(13, Types.DOUBLE, "LN_DELIVERYEXPLIMIT");
			cs.registerOutParameter(14, Types.DOUBLE, "LN_PREPOSTHOSPLIMIT");
			cs.registerOutParameter(15, Types.DOUBLE, "LN_AILMENTLIMIT");
			cs.execute();

			valuesMap.put(8, Double.parseDouble(cs.getObject(10).toString()));
			valuesMap.put(9, Double.parseDouble(cs.getObject(9).toString()));
			valuesMap.put(15, Double.parseDouble(cs.getObject(8).toString()));
			if (cs.getObject(11) != null) {
				valuesMap.put(0, Integer.parseInt(cs.getObject(11).toString()));
			} else {
				valuesMap.put(0, 0);
			}

			// valuesMap.put(8,1000d);
			// valuesMap.put(9 ,2000d);
			// valuesMap.put(15 , 1200d);
			// valuesMap.put(0, 500);

			if (((Integer) valuesMap.get(0)).equals(2)) {
				Object[] prorataPercentage = getProrataPercentage(productKey);
				if (prorataPercentage != null) {
					Struct row = null;
					Object[] attributes = null;
					for (Object tmp : prorataPercentage) {
						row = (Struct) tmp;
						attributes = row.getAttributes();
						if (valuesMap.containsKey(Integer.valueOf(String
								.valueOf(attributes[0])))) {
							continue;
						}
						valuesMap.put(Integer.valueOf(String
								.valueOf(attributes[0])), Double
								.parseDouble(String.valueOf(attributes[1])));
					}
				}

			}
			// return valuesMap;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public Double getInsuredSumInsuredForGMC(Long policyKey, Long insuredKey,
			String sectionCode) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_INSURED_SUM_INSURED(?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, sectionCode);

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(4, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(4) ? Double.parseDouble(cs
					.getObject(4).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	public Map<String, Double> getGmcCorpBufferASIForRegister(String bufferType,String policyNumber,Long insuredKey,Long mainNo,Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Double> values = new WeakHashMap<String, Double>();
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GMC_CORP_BUFFER_ASI(?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, bufferType);
			cs.setString(2, policyNumber);
			cs.setLong(3, insuredKey);
			cs.setLong(4, mainNo);
			cs.setLong(5, claimKey);
			cs.registerOutParameter(6, Types.DOUBLE, SHAConstants.LN_POLICY_BUFFER_SI);
			cs.registerOutParameter(7, Types.DOUBLE, SHAConstants.LN_BUFFER_UTILISED_AMT);
			cs.registerOutParameter(8, Types.DOUBLE, SHAConstants.LN_INSURED_ALLOCATE_AMT);
			cs.registerOutParameter(9, Types.DOUBLE, SHAConstants.LN_INSURED_UTIL_AMT);
			cs.registerOutParameter(10, Types.DOUBLE, SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT);
			
			cs.execute();

			if (cs.getObject(6) != null) {
				values.put(SHAConstants.LN_POLICY_BUFFER_SI,
						Double.parseDouble(cs.getObject(6).toString()));
			}
			if (cs.getObject(7) != null) {
				values.put(SHAConstants.LN_BUFFER_UTILISED_AMT,
						Double.parseDouble(cs.getObject(7).toString()));
			}

			if (cs.getObject(8) != null) {
				values.put(SHAConstants.LN_INSURED_ALLOCATE_AMT,
						Double.parseDouble(cs.getObject(8).toString()));
			}

			if (cs.getObject(9) != null) {
				values.put(SHAConstants.LN_INSURED_UTIL_AMT,
						Double.parseDouble(cs.getObject(9).toString()));
			}
			if (cs.getObject(10) != null) {
				values.put(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT,
						Double.parseDouble(cs.getObject(10).toString()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return values;

	}

	public Map<String, Integer> getStopLossProcedure(Long policyKey,
			Long claimKey, Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;
		if (claimKey == null) {
			claimKey = 0l;
		}
		Map<String, Integer> productBenefitMap = new HashMap<String, Integer>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_STOP_LOSS(?, ?, ?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, claimKey);
			cs.setLong(3, rodKey);
			cs.registerOutParameter(4, Types.INTEGER, "LN_AVAIL_SI");
			cs.registerOutParameter(5, Types.INTEGER, "LV_STOP_LOSS_FLAG");
			cs.execute();
			Integer availableAmt = (Integer) cs.getInt(4);
			Integer stopLossFlag = (Integer) cs.getInt(5);
			/*
			 * String strLumpSumFlag = (String) cs.getObject(3); String
			 * patientCareFlag = (String) cs.getObject(4); String
			 * hospitalCashFlag = (String) cs.getObject(5);
			 */
			productBenefitMap.put(SHAConstants.STOP_LOSS_AVAILABLE,
					availableAmt);
			productBenefitMap.put(SHAConstants.STOP_LOSS_FLAG, stopLossFlag);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return productBenefitMap;

	}

	public Map<String, Integer> getStopLossProcedureForView(Long policyKey,
			Long claimKey, Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;
		if (rodKey == null) {
			rodKey = 0l;
		}
		Map<String, Integer> productBenefitMap = new HashMap<String, Integer>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_STOP_LOSS_ASI (?, ?, ?,?,?,?, ?, ?,?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, claimKey);
			cs.setLong(3, rodKey);
			cs.registerOutParameter(4, Types.INTEGER, "LN_SI");
			cs.registerOutParameter(5, Types.INTEGER, "LN_ST_LOSS_PER");
			cs.registerOutParameter(6, Types.INTEGER, "LN_ST_LOSS_AMT");
			cs.registerOutParameter(7, Types.INTEGER, "LN_PAID_AMT");
			cs.registerOutParameter(8, Types.INTEGER, "LN_OUT_AMT");
			cs.registerOutParameter(9, Types.INTEGER, "LN_TOT_INCUR_AMT");
			cs.registerOutParameter(10, Types.INTEGER, "LN_BSI");
			cs.registerOutParameter(11, Types.INTEGER, "LN_PROV_AMT");
			cs.registerOutParameter(12, Types.INTEGER, "LN_FIN_BSI");
			cs.execute();

			if (cs.getObject(4) != null) {
				Integer premium = (Integer) cs.getInt(4);
				productBenefitMap.put(SHAConstants.PREMIUM_FOR_STOP_LOSS,
						premium);
			}
			if (cs.getObject(5) != null) {
				Integer percentage = (Integer) cs.getInt(5);
				productBenefitMap.put(SHAConstants.STOP_LOSS_PER, percentage);
			}
			if (cs.getObject(6) != null) {
				Integer stopLossAmt = (Integer) cs.getInt(6);
				productBenefitMap.put(SHAConstants.STOP_LOSS_AMT, stopLossAmt);
			}
			if (cs.getObject(7) != null) {
				Integer paid = (Integer) cs.getInt(7);
				productBenefitMap.put(SHAConstants.STOP_LOSS_PAID, paid);
			}
			if (cs.getObject(8) != null) {
				Integer outStanding = (Integer) cs.getInt(8);
				productBenefitMap.put(SHAConstants.STOP_LOSS_OUTSTANDING,
						outStanding);
			}
			if (cs.getObject(9) != null) {
				Integer incurredClaims = (Integer) cs.getInt(9);
				productBenefitMap.put(SHAConstants.STOP_LOSS_INCURRED_CLAIMS,
						incurredClaims);
			}

			if (cs.getObject(10) != null) {
				Integer balance = (Integer) cs.getInt(10);
				productBenefitMap.put(SHAConstants.STOP_LOSS_BALANCE_AVAILABLE,
						balance);
			}
			if (cs.getObject(11) != null) {
				Integer currentClaim = (Integer) cs.getInt(11);
				productBenefitMap.put(SHAConstants.STOP_LOSS_CURR_CLAIM_AMT,
						currentClaim);
			}
			if (cs.getObject(12) != null) {
				Integer premium = (Integer) cs.getInt(12);
				productBenefitMap.put(SHAConstants.STOP_LOSS_BALANCE_AMT_CLAIM,
						premium);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return productBenefitMap;

	}

	public Double getBalanceSIForGMC(Long policyKey, Long insuredKey,
			Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GMC_CAL(?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);

			cs.registerOutParameter(4, Types.DOUBLE, "LN_BSI");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(4);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalBalanceSI;

	}

	public Map<String, Double> getGPABalanceSI(Long policyKey, Long insuredKey,
			Long claimKey, Double sumInsured, Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_BALANCE_SI(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);

			if (sumInsured != null) {
				cs.setDouble(5, sumInsured);
			} else {
				cs.setDouble(5, 0);
			}

			cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(6);
			currentBalanceSI = (Double) cs.getObject(7);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public Map<String, Double> getBalanceSIForPAHealth(Long insuredKey,
			Long claimKey, Long productKey, Long benefitId) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();

			if (null != productKey
					&& !(ReferenceTable.getGPAProducts()
							.containsKey(productKey))) {
				cs = connection
						.prepareCall("{call PRC_PA_HOSP_BALANCE_SI(?, ?, ?)}");

				cs.setLong(1, insuredKey);
				cs.setLong(2, claimKey);

				cs.registerOutParameter(3, Types.DOUBLE, "LN_AVAIL_SI");
				cs.execute();

				// return (Double) cs.getObject(4);
				totalBalanceSI = (Double) cs.getObject(3);
			} else {
				cs = connection
						.prepareCall("{call PRC_GPA_HOSP_BALANCE_SI(?, ?, ? ,?)}");

				cs.setLong(1, insuredKey);
				cs.setLong(2, claimKey);
				cs.setLong(3, benefitId);

				cs.registerOutParameter(4, Types.DOUBLE, "LN_AVAIL_SI");
				cs.execute();

				// return (Double) cs.getObject(4);
				totalBalanceSI = (Double) cs.getObject(4);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		return values;
	}

	public Map<String, Integer> getGmcInnerLimit(Long insuredKey, Long claimKey) {

		final String innerSI = "LN_INNER_LIMIT_SI";
		final String innerUtilisedAmt = "LN_UTILISED_AMT";
		final String innerAvailSI = "LN_AVAILABLE_SI";

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Integer> values = new WeakHashMap<String, Integer>();
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GMC_INNER_LIMIT_ASI(?,?,?,?,?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);
			cs.registerOutParameter(3, Types.INTEGER, innerSI);
			cs.registerOutParameter(4, Types.INTEGER, innerUtilisedAmt);
			cs.registerOutParameter(5, Types.INTEGER, innerAvailSI);
			cs.execute();

			if (cs.getObject(3) != null) {
				values.put(SHAConstants.GMC_INNER_LIMIT_SI,
						Integer.parseInt(cs.getObject(3).toString()));
			}
			if (cs.getObject(4) != null) {
				values.put(SHAConstants.GMC_INNER_LIMIT_UTILISED_AMT,
						Integer.parseInt(cs.getObject(4).toString()));
			}

			if (cs.getObject(5) != null) {
				values.put(SHAConstants.GMC_INNER_LIMIT_AVAILABLE,
						Integer.parseInt(cs.getObject(5).toString()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return values;

	}

	public Map<String, Double> getBalanceSIForGPAHealth(Long insuredKey,
			Long claimKey, Long benefitId) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_HOSP_BALANCE_SI(?, ?, ?, ?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);

			cs.registerOutParameter(3, Types.DOUBLE, "LN_AVAIL_SI");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(3);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		return values;
	}

	public List<Double> getProductCoPayForGMC(Long productKey, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Double> copayValues = new ArrayList<Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_COPAY_LIMIT_PER(?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setLong(2, insuredKey);
			// Result is an java.sql.Array...
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				while (rs.next()) {

					Double coPay = (Double) rs.getDouble("copay");
					copayValues.add(coPay);
				}
			}

			Boolean isHavingZero = false;
			for (Double double1 : copayValues) {
				if (double1.equals(0d)) {
					isHavingZero = true;
					break;
				}
			}

			if (!isHavingZero) {
				copayValues.add(0, 0d);
			}
			// return copayValues;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return copayValues;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getGmcPaidNOutStanding(Long insuredKey,
			String intimationNo, Double sumInsured, Long prodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfTable = new HashMap<String, Object>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_PRV_CLAIM_RPT(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7, Types.ARRAY, "TYP_SI_LIST");
			cs.execute();
			
			BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();

			if(cs.getObject(7) != null){

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();
			
			ComprehensiveHospitalisationTableDTO section1 = null;
			ComprehensiveDeliveryNewBornTableDTO section2 = null;
			ComprehensiveOutpatientTableDTO section3 = null;
			ComprehensiveHospitalCashTableDTO section4 = null;
			ComprehensiveHealthCheckTableDTO section5 = null;
			ComprehensiveBariatricSurgeryTableDTO section6 = null;
			LumpSumTableDTO section8 = null;
			
			
			List<ComprehensiveHospitalisationTableDTO> sectionTable1 = new ArrayList<ComprehensiveHospitalisationTableDTO>();
			List<ComprehensiveDeliveryNewBornTableDTO> sectionTable2 = new ArrayList<ComprehensiveDeliveryNewBornTableDTO>();
			List<ComprehensiveOutpatientTableDTO> sectionTable3 = new ArrayList<ComprehensiveOutpatientTableDTO>();
			List<ComprehensiveHospitalCashTableDTO> sectionTable4 = new ArrayList<ComprehensiveHospitalCashTableDTO>();
			List<ComprehensiveHealthCheckTableDTO> sectionTable5 = new ArrayList<ComprehensiveHealthCheckTableDTO>();
			List<ComprehensiveBariatricSurgeryTableDTO> sectionTable6 = new ArrayList<ComprehensiveBariatricSurgeryTableDTO>();
			List<LumpSumTableDTO> sectionTable8 = new ArrayList<LumpSumTableDTO>();

			Struct row = null;
			Object[] attributes = null;
			for (Object object : data) {

				row = (Struct) object;
				attributes = row.getAttributes();
				if (attributes != null) {
					String sectionNameWithCode = (String) attributes[0];
					String sectionCode = (String) attributes[1];
					String coverName = (String) attributes[2];
					// String coverCode = (String)attributes[3];
					String subCoverName = (String) attributes[4];
					// String subCoverCode = (String)attributes[5];
					Double limitAmt = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[6]));
					Double claimsPaid = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[7]));
					Double outStanding = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[8]));
					Double balanceSumInsured = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[9]));
					Double currentProvisionAmt = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[10]));
					Double balSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[11]));
					section1 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_1)) {
						section1 = new ComprehensiveHospitalisationTableDTO();

						section1.setSectionI(sectionNameWithCode.split("-")[2]);
						section1.setCover(coverName);
						section1.setSubCover(subCoverName);
						section1.setSumInsured(limitAmt);
						section1.setClaimPaid(claimsPaid);
						section1.setClaimOutStanding(outStanding);
						section1.setBalance(balanceSumInsured);
						section1.setProvisionCurrentClaim(currentProvisionAmt);
						section1.setBalanceSI(balSI);

						sectionTable1.add(section1);
					}

					section2 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_2)) {
						section2 = new ComprehensiveDeliveryNewBornTableDTO();

						section2.setSectionII(sectionNameWithCode.split("-")[2]);
						section2.setCover(coverName);
						section2.setSubCover(subCoverName);
						section2.setLimit(limitAmt);
						section2.setClaimPaid(claimsPaid);
						section2.setClaimOutstanding(outStanding);
						section2.setBalance(balanceSumInsured);
						section2.setProvisionCurrentClaim(currentProvisionAmt);
						section2.setBalanceSI(balSI);
						sectionTable2.add(section2);
					}

					section3 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_3)) {
						section3 = new ComprehensiveOutpatientTableDTO();

						section3.setSectionIII(sectionNameWithCode.split("-")[2]);
						section3.setCover(coverName);
						section3.setSubCover(subCoverName);
						section3.setLimit(limitAmt);
						section3.setClaimPaid(claimsPaid);
						section3.setClaimOutstanding(outStanding);
						section3.setBalance(balanceSumInsured);
						section3.setProvisionCurrentClaim(currentProvisionAmt);
						section3.setBalanceSI(balSI);
						sectionTable3.add(section3);
					}

					section4 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_4)) {
						section4 = new ComprehensiveHospitalCashTableDTO();

						section4.setSectionIV(sectionNameWithCode.split("-")[2]);
						section4.setCover(coverName);
						section4.setSubCover(subCoverName);
						section4.setLimit(limitAmt);
						section4.setClaimPaid(claimsPaid);
						section4.setClaimOutstanding(outStanding);
						section4.setBalance(balanceSumInsured);
						section4.setProvisionCurrentClaim(currentProvisionAmt);
						section4.setBalanceSI(balSI);
						sectionTable4.add(section4);
					}

					section5 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_5)) {
						section5 = new ComprehensiveHealthCheckTableDTO();

						section5.setSectionV(sectionNameWithCode.split("-")[2]);
						section5.setCover(coverName);
						section5.setSubCover(subCoverName);
						section5.setLimit(limitAmt);
						section5.setClaimPaid(claimsPaid);
						section5.setClaimOutstanding(outStanding);
						section5.setBalance(balanceSumInsured);
						section5.setProvisionCurrentClaim(currentProvisionAmt);
						section5.setBalanceSI(balSI);
						sectionTable5.add(section5);
					}

					section6 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_6)) {
						section6 = new ComprehensiveBariatricSurgeryTableDTO();

						section6.setSectionVI(sectionNameWithCode.split("-")[2]);
						section6.setCover(coverName);
						section6.setSubCover(subCoverName);
						section6.setLimit(limitAmt);
						section6.setClaimPaid(claimsPaid);
						section6.setClaimOutstanding(outStanding);
						section6.setBalance(balanceSumInsured);
						section6.setProvisionCurrentClaim(currentProvisionAmt);
						section6.setBalanceSI(balSI);
						sectionTable6.add(section6);
					}

					section8 = null;
					if (sectionCode != null
							&& sectionCode
									.equalsIgnoreCase(SHAConstants.SECTION_CODE_8)) {
						section8 = new LumpSumTableDTO();

						section8.setSectionVIII(sectionNameWithCode.split("-")[2]);
						section8.setCover(coverName);
						section8.setSubCover(subCoverName);
						section8.setLimit(limitAmt);
						section8.setClaimPaid(claimsPaid);
						section8.setClaimOutstanding(outStanding);
						section8.setBalance(balanceSumInsured);
						section8.setProvisionCurrentClaim(currentProvisionAmt);
						section8.setBalanceSI(balSI);

						sectionTable8.add(section8);
					}

				}
			}

			listOfTable.put(SHAConstants.SECTION_CODE_1, sectionTable1);
			listOfTable.put(SHAConstants.SECTION_CODE_2, sectionTable2);
			listOfTable.put(SHAConstants.SECTION_CODE_3, sectionTable3);
			listOfTable.put(SHAConstants.SECTION_CODE_4, sectionTable4);
			listOfTable.put(SHAConstants.SECTION_CODE_5, sectionTable5);
			listOfTable.put(SHAConstants.SECTION_CODE_6, sectionTable6);
			listOfTable.put(SHAConstants.SECTION_CODE_8, sectionTable8);
			}			
			balanceSumInsuredDTO = new BalanceSumInsuredDTO();
			String outstandingAmout = "0.0";
			String paidAmout = "0.0";
			
			if(cs.getObject(5) != null){
				outstandingAmout = cs.getObject(5).toString();
			}

			if(cs.getObject(6) != null){
				paidAmout = cs.getObject(6).toString();
			} 
			
			listOfTable.put("outstandingAmount",outstandingAmout);
			listOfTable.put("paidAmount",paidAmout);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;

	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getGmcSI(Long insuredKey, String intimationNo,
			Double sumInsured, Long prodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfTable = new HashMap<String, Object>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_PRV_CLAIM(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7, Types.ARRAY, "TYP_SI_LIST");
			cs.execute();

			if (cs.getObject(7) != null) {

				Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();

				List<ComprehensiveHospitalisationTableDTO> sectionTable1 = new ArrayList<ComprehensiveHospitalisationTableDTO>();
				List<ComprehensiveDeliveryNewBornTableDTO> sectionTable2 = new ArrayList<ComprehensiveDeliveryNewBornTableDTO>();
				List<ComprehensiveOutpatientTableDTO> sectionTable3 = new ArrayList<ComprehensiveOutpatientTableDTO>();
				List<ComprehensiveHospitalCashTableDTO> sectionTable4 = new ArrayList<ComprehensiveHospitalCashTableDTO>();
				List<ComprehensiveHealthCheckTableDTO> sectionTable5 = new ArrayList<ComprehensiveHealthCheckTableDTO>();
				List<ComprehensiveBariatricSurgeryTableDTO> sectionTable6 = new ArrayList<ComprehensiveBariatricSurgeryTableDTO>();
				List<LumpSumTableDTO> sectionTable8 = new ArrayList<LumpSumTableDTO>();

				Struct row = null;
				Object[] attributes = null;
				for (Object object : data) {

					row = (Struct) object;
					attributes = row.getAttributes();
					if (attributes != null) {
						String sectionNameWithCode = (String) attributes[0];
						String sectionCode = (String) attributes[1];
						String coverName = (String) attributes[2];
						// String coverCode = (String)attributes[3];
						String subCoverName = (String) attributes[4];
						// String subCoverCode = (String)attributes[5];
						Double limitAmt = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(attributes[6]));
						Double claimsPaid = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(attributes[7]));
						Double outStanding = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(attributes[8]));
						Double balanceSumInsured = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(attributes[9]));
						Double currentProvisionAmt = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(attributes[10]));
						Double balSI = SHAUtils.getDoubleValueFromString(String
								.valueOf(attributes[11]));
						ComprehensiveHospitalisationTableDTO section1 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_1)) {
							section1 = new ComprehensiveHospitalisationTableDTO();

							section1.setSectionI(sectionNameWithCode.split("-")[2]);
							section1.setCover(coverName);
							section1.setSubCover(subCoverName);
							section1.setSumInsured(limitAmt);
							section1.setClaimPaid(claimsPaid);
							section1.setClaimOutStanding(outStanding);
							section1.setBalance(balanceSumInsured);
							section1.setProvisionCurrentClaim(currentProvisionAmt);
							section1.setBalanceSI(balSI);

							sectionTable1.add(section1);
						}

						ComprehensiveDeliveryNewBornTableDTO section2 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_2)) {
							section2 = new ComprehensiveDeliveryNewBornTableDTO();

							section2.setSectionII(sectionNameWithCode
									.split("-")[2]);
							section2.setCover(coverName);
							section2.setSubCover(subCoverName);
							section2.setLimit(limitAmt);
							section2.setClaimPaid(claimsPaid);
							section2.setClaimOutstanding(outStanding);
							section2.setBalance(balanceSumInsured);
							section2.setProvisionCurrentClaim(currentProvisionAmt);
							section2.setBalanceSI(balSI);
							sectionTable2.add(section2);
						}

						ComprehensiveOutpatientTableDTO section3 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_3)) {
							section3 = new ComprehensiveOutpatientTableDTO();

							section3.setSectionIII(sectionNameWithCode
									.split("-")[2]);
							section3.setCover(coverName);
							section3.setSubCover(subCoverName);
							section3.setLimit(limitAmt);
							section3.setClaimPaid(claimsPaid);
							section3.setClaimOutstanding(outStanding);
							section3.setBalance(balanceSumInsured);
							section3.setProvisionCurrentClaim(currentProvisionAmt);
							section3.setBalanceSI(balSI);
							sectionTable3.add(section3);
						}

						ComprehensiveHospitalCashTableDTO section4 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_4)) {
							section4 = new ComprehensiveHospitalCashTableDTO();

							section4.setSectionIV(sectionNameWithCode
									.split("-")[2]);
							section4.setCover(coverName);
							section4.setSubCover(subCoverName);
							section4.setLimit(limitAmt);
							section4.setClaimPaid(claimsPaid);
							section4.setClaimOutstanding(outStanding);
							section4.setBalance(balanceSumInsured);
							section4.setProvisionCurrentClaim(currentProvisionAmt);
							section4.setBalanceSI(balSI);
							sectionTable4.add(section4);
						}

						ComprehensiveHealthCheckTableDTO section5 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_5)) {
							section5 = new ComprehensiveHealthCheckTableDTO();

							section5.setSectionV(sectionNameWithCode.split("-")[2]);
							section5.setCover(coverName);
							section5.setSubCover(subCoverName);
							section5.setLimit(limitAmt);
							section5.setClaimPaid(claimsPaid);
							section5.setClaimOutstanding(outStanding);
							section5.setBalance(balanceSumInsured);
							section5.setProvisionCurrentClaim(currentProvisionAmt);
							section5.setBalanceSI(balSI);
							sectionTable5.add(section5);
						}

						ComprehensiveBariatricSurgeryTableDTO section6 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_6)) {
							section6 = new ComprehensiveBariatricSurgeryTableDTO();

							section6.setSectionVI(sectionNameWithCode
									.split("-")[2]);
							section6.setCover(coverName);
							section6.setSubCover(subCoverName);
							section6.setLimit(limitAmt);
							section6.setClaimPaid(claimsPaid);
							section6.setClaimOutstanding(outStanding);
							section6.setBalance(balanceSumInsured);
							section6.setProvisionCurrentClaim(currentProvisionAmt);
							section6.setBalanceSI(balSI);
							sectionTable6.add(section6);
						}

						LumpSumTableDTO section8 = null;
						if (sectionCode != null
								&& sectionCode
										.equalsIgnoreCase(SHAConstants.SECTION_CODE_8)) {
							section8 = new LumpSumTableDTO();

							section8.setSectionVIII(sectionNameWithCode
									.split("-")[2]);
							section8.setCover(coverName);
							section8.setSubCover(subCoverName);
							section8.setLimit(limitAmt);
							section8.setClaimPaid(claimsPaid);
							section8.setClaimOutstanding(outStanding);
							section8.setBalance(balanceSumInsured);
							section8.setProvisionCurrentClaim(currentProvisionAmt);
							section8.setBalanceSI(balSI);

							sectionTable8.add(section8);
						}

					}
				}

				listOfTable.put(SHAConstants.SECTION_CODE_1, sectionTable1);
				listOfTable.put(SHAConstants.SECTION_CODE_2, sectionTable2);
				listOfTable.put(SHAConstants.SECTION_CODE_3, sectionTable3);
				listOfTable.put(SHAConstants.SECTION_CODE_4, sectionTable4);
				listOfTable.put(SHAConstants.SECTION_CODE_5, sectionTable5);
				listOfTable.put(SHAConstants.SECTION_CODE_6, sectionTable6);
				listOfTable.put(SHAConstants.SECTION_CODE_8, sectionTable8);
			}
			BalanceSumInsuredDTO balanceSumInsuredDTO = new BalanceSumInsuredDTO();
			String outstandingAmout = "0.0";
			String paidAmout = "0.0";

			if (cs.getObject(5) != null) {
				outstandingAmout = cs.getObject(5).toString();
			}

			if (cs.getObject(6) != null) {
				paidAmout = cs.getObject(6).toString();
			}

			listOfTable.put("outstandingAmout", outstandingAmout);
			listOfTable.put("paidAmout", paidAmout);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;

	}

	@SuppressWarnings("rawtypes")
	public Map<String, List> getGPABalanceSIView(Long insuredKey,
			Long claimKey, Long namedKey) {
		Connection connection = null;
		CallableStatement cs = null;

		Map<String, List> listOfTable = new HashMap<String, List>();

		List<PABalanceSumInsuredTableDTO> listBalSIDTO = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOPA = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOAddOn = new ArrayList<PABalanceSumInsuredTableDTO>();
		List<PABalanceSumInsuredTableDTO> listBalSIDTOOptional = new ArrayList<PABalanceSumInsuredTableDTO>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GPA_VIEW_SI(?, ? ,? ,?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);
			cs.setLong(3, namedKey);
			cs.registerOutParameter(4, Types.ARRAY, "TYP_PA_VIEW_SI");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(4)).getArray();

			for (Object object : data) {

				Struct row = (Struct) object;
				Object[] attributes = row.getAttributes();
				if (attributes != null) {

					String coverCode = (String) attributes[0];
					String coverName = (String) attributes[1];
					Double orgSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[2]));
					Double cumBonus = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[3]));
					Double totalSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[4]));
					Double claimPaid = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[5]));
					Double claimOut = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[6]));
					Double balSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[7]));
					Double provAmt = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[8]));
					Double availSI = SHAUtils.getDoubleValueFromString(String
							.valueOf(attributes[9]));

					PABalanceSumInsuredTableDTO balanceSIDTO = new PABalanceSumInsuredTableDTO();

					if (coverName != null
							&& (coverName
									.equalsIgnoreCase(SHAConstants.GPA_UNNAMED) || coverName
									.equalsIgnoreCase(SHAConstants.GPA_NAMED))) {
						balanceSIDTO.setOrginalSI(orgSI);
						balanceSIDTO.setCumulativeBonus(cumBonus);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setBalanceSI(balSI);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);
						listBalSIDTO.add(balanceSIDTO);
					}

					if (coverName != null
							&& (coverName
									.equalsIgnoreCase(SHAConstants.GPA_UNNAMED_BENEFIT) || coverName
									.equalsIgnoreCase(SHAConstants.GPA_NAMED_BENEFIT))) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setOrginalSI(orgSI);
						if (coverCode != null
								&& (coverCode
										.equalsIgnoreCase(SHAConstants.PPD) || coverCode
										.equalsIgnoreCase(SHAConstants.TTD))) {
							balanceSIDTO.setCumulativeBonus(null);
						} else {
							balanceSIDTO.setCumulativeBonus(cumBonus);
						}
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOPA.add(balanceSIDTO);
					}

					if (coverName != null
							&& (coverName
									.equalsIgnoreCase(SHAConstants.GPA_NAMED_OPTIONAL_COVERS) || coverName
									.equalsIgnoreCase(SHAConstants.GPA_UNNAMED_OPTIONAL_COVERS))) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOOptional.add(balanceSIDTO);
					}

					/*
					 * if(coverName != null &&
					 * (coverName.equalsIgnoreCase(SHAConstants
					 * .GPA_NAMED_COVERS) ||
					 * coverName.equalsIgnoreCase(SHAConstants
					 * .GPA_UNNAMED_COVERS))){
					 * balanceSIDTO.setCoverDesc(coverCode);
					 * balanceSIDTO.setTotalSI(totalSI);
					 * balanceSIDTO.setClaimPaid(claimPaid);
					 * balanceSIDTO.setClaimOutStanding(claimOut);
					 * balanceSIDTO.setCurrentClaim(provAmt);
					 * balanceSIDTO.setAvailableSI(availSI);
					 * 
					 * listBalSIDTOOptional.add(balanceSIDTO); }
					 */

					if (coverName != null
							&& (coverName
									.equalsIgnoreCase(SHAConstants.GPA_NAMED_ADDITIOANAL_COVERS) || coverName
									.equalsIgnoreCase(SHAConstants.GPA_UNNAMED_ADDITIONAL_COVERS))) {
						balanceSIDTO.setCoverDesc(coverCode);
						balanceSIDTO.setTotalSI(totalSI);
						balanceSIDTO.setClaimPaid(claimPaid);
						balanceSIDTO.setClaimOutStanding(claimOut);
						balanceSIDTO.setCurrentClaim(provAmt);
						balanceSIDTO.setAvailableSI(availSI);

						listBalSIDTOAddOn.add(balanceSIDTO);
					}
				}
			}

			listOfTable.put(SHAConstants.PA_DESC, listBalSIDTO);
			listOfTable.put(SHAConstants.PA_BENEFIT_COVER_DESC, listBalSIDTOPA);
			listOfTable
					.put(SHAConstants.PA_OPTIONAL_DESC, listBalSIDTOOptional);
			listOfTable.put(SHAConstants.PA_ADDITIONAL_DESC, listBalSIDTOAddOn);
			/*
			 * listOfTable.put(SHAConstants.PA_OPTIONAL_DESC,
			 * listBalSIDTOOptional);
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;

	}

	public List<SublimitFunObject> getClaimedAmountDetailsForSectionForGMC(
			long policyKey, Double sumInsured, Double age, Long section,
			String plan, String subCoverCode) {
		// final String typeName = "OBJ_SUB_LIM";
		final String typeTableName = "TYP_SUB_LIM";
		if (section == null) {
			section = 0l;
		}
		Long sectionValue = 0l;

		if (section.equals(ReferenceTable.POL_SECTION_1)) {
			sectionValue = 1l;
		} else if (section.equals(ReferenceTable.POL_SECTION_2)) {
			sectionValue = 2l;
		}

		Connection connection = null;
		CallableStatement cs = null;
		List<SublimitFunObject> subLimits = new ArrayList<SublimitFunObject>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GMC_SUB_LIMIT(?, ?, ?, ?, ? , ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setDouble(2, sumInsured);
			cs.setDouble(3, age != null ? age : 0d);
			cs.setLong(4, sectionValue);
			cs.setString(5, sectionValue.equals(0l) ? "0"
					: ((plan != null) ? plan : "A"));
			cs.setString(6, subCoverCode != null ? subCoverCode
					: ReferenceTable.HOSP_SUB_COVER_CODE);
			// Result is an java.sql.Array...
			cs.registerOutParameter(7, Types.ARRAY, typeTableName);
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();
			Struct row = null;
			for (Object tmp : data) {
				row = (Struct) tmp;
				Object[] attributes = row.getAttributes();
				Double amount = Double.valueOf("" + attributes[0]);
				String name = String.valueOf(attributes[1]);
				String description = String.valueOf(attributes[2]);
				Long limitId = Long.valueOf(String.valueOf(attributes[3]));

				// System.out.println("---the sublimit calculation---amount--"
				// + amount + "--name---" + name + "----description---"
				// + description + "---limit id---" + limitId);

				subLimits.add(new SublimitFunObject(name, description, amount,
						limitId));
				// if(section.equals(468l)){
				// break;
				// }
			}
			// System.out.println("---sublimt list ----" + subLimits);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return subLimits;
	}

	// The below method has been commented, because Double should be the return
	// type but not Integer
	/*
	 * public Integer getInsuredSumInsured(Long policyKey, String insuredId) {
	 * //public Double getInsuredSumInsured(Long policyKey, String insuredId) {
	 * Connection connection = null; Integer sumInsured = null; //Double
	 * sumInsured = null; try { connection = BPMClientContext.getConnection();
	 * CallableStatement cs = connection
	 * .prepareCall("{call PRC_INSURED_SUM_INSURED(?, ?, ?)}"); cs.setLong(1,
	 * policyKey); cs.setString(2, insuredId);
	 * 
	 * System.out.println("---- policy Key ---- " + policyKey);
	 * System.out.println("--- insured Id ****** " + insuredId);
	 * 
	 * // Result is an java.sql.Array... cs.registerOutParameter(3,
	 * Types.INTEGER, "LN_SUM_INSURED"); cs.execute();
	 * 
	 * sumInsured = (Integer) cs.getObject(3); //sumInsured = (Double)
	 * cs.getObject(3);
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } } catch (SQLException e) {
	 * e.printStackTrace(); } } return sumInsured; }
	 */

	public Double getGPAInsuredSumInsured(String insuredId, Long policyKey) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_INSURED_SUM_INSURED(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, Long.valueOf(insuredId));

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(3, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(3) ? Double.parseDouble(cs
					.getObject(3).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	public List<Map<String, Object>> getTaskProcedureForAutoAllocation(
			Object[] input) {

		assignedAutoAllocationProcedure();

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_GET_TASK", conn);
			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_GET_AUTOALLOCATION (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public void assignedAutoAllocationProcedure() {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_AUTO_ASSIGN_LOAD()}");
			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<Map<String, Object>> getUserForReallocation(String doctorId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = null;

		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_AUTO(?,?)}");
			cs.setString(1, doctorId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				list = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getReallocationUserFromCursor(rs);
					list.add(mappedValues);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public BeanItemContainer<SelectValue> getGPABenefitCoverValueContainer(
			Long insuredKey, Long benefitId) {
		Connection connection = null;
		CallableStatement cs = null;

		// List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
		BeanItemContainer<SelectValue> coverContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try {
			final String typeTableName = "TYP_CLAIM_BENEFIT_COVER";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_CLAIM_BENEFIT_COVER(?, ?, ?)}");

			cs.setLong(1, insuredKey);
			cs.setLong(2, benefitId);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
					String benefitCover = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					String percentage = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					Double sumInsuredValue = null != attributes[2] ? Double
							.valueOf(String.valueOf(attributes[2])) : 0d;
					Double eligibleAmt = null != attributes[3] ? Double
							.valueOf(String.valueOf(attributes[3])) : 0d;
					Long benefitsId = null != attributes[4] ? Long
							.valueOf(String.valueOf(attributes[4])) : 0;

					paBenefitsDTO.setBenefitCover(benefitCover);
					paBenefitsDTO.setPercentage(percentage);
					paBenefitsDTO.setSumInsured(sumInsuredValue);
					paBenefitsDTO.setEligibleAmount(eligibleAmt);
					// paBenefitsList.add(paBenefitsDTO);

					SelectValue covers = new SelectValue(benefitsId,
							benefitCover);
					coverContainer.addBean(covers);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coverContainer;

	}

	public List<PABenefitsDTO> getGPABenefitCoverValues(Long insuredKey,
			Long benefitId) {
		Connection connection = null;
		CallableStatement cs = null;

		List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_CLAIM_BENEFIT_COVER";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_CLAIM_BENEFIT_COVER(?, ?, ?)}");

			cs.setLong(1, insuredKey);
			cs.setLong(2, benefitId);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					PABenefitsDTO paBenefitsDTO = new PABenefitsDTO();
					String benefitCover = null != attributes[0] ? String
							.valueOf(attributes[0]) : null;
					String percentage = null != attributes[1] ? String
							.valueOf(attributes[1]) : null;
					Double sumInsuredValue = null != attributes[2] ? Double
							.valueOf(String.valueOf(attributes[2])) : 0d;
					Double eligibleAmt = null != attributes[3] ? Double
							.valueOf(String.valueOf(attributes[3])) : 0d;
					Long benefitsId = null != attributes[4] ? Long
							.valueOf(String.valueOf(attributes[4])) : 0;
					Double eligibleAmtPerWeek = null != attributes[5] ? Double
							.valueOf(String.valueOf(attributes[5])) : 0d;

					// Double eligibleAmtPerWeek = 5000d;

					paBenefitsDTO.setBenefitCover(benefitCover);
					paBenefitsDTO.setPercentage(percentage);
					paBenefitsDTO.setSumInsured(sumInsuredValue);
					paBenefitsDTO.setEligibleAmount(eligibleAmt);
					paBenefitsDTO.setBenefitsId(benefitsId);
					if (null != sumInsuredValue) {
						Double calculatedSIValue = sumInsuredValue
								* (1f / 100f);
						paBenefitsDTO.setEligibleAmountPerWeek(Math.min(
								calculatedSIValue, eligibleAmtPerWeek));
					}
					// paBenefitsDTO.setEligibleAmountPerWeek(eligibleAmtPerWeek);
					paBenefitsList.add(paBenefitsDTO);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return paBenefitsList;

	}

	/*
	 * public List<PABenefitsDTO> getClaimCoverValues(Long insuredKey,Long
	 * coverTypeKey , Long productKey) { Connection connection = null;
	 * CallableStatement cs =null;
	 * 
	 * List<PABenefitsDTO> paBenefitsList = new ArrayList<PABenefitsDTO>();
	 * //BeanItemContainer<SelectValue> coverContainer = new
	 * BeanItemContainer<SelectValue>(SelectValue.class);
	 * 
	 * try { final String typeTableName = "TYP_CLAIM_COVERS";
	 * 
	 * 
	 * connection = BPMClientContext.getConnection(); cs = connection
	 * .prepareCall("{call PRC_PA_CLAIM_COVERS(?, ?, ?)}");
	 * 
	 * cs.setLong(1, insuredKey); cs.setLong(2,coverTypeKey); cs.setLong(3,
	 * productKey); cs.registerOutParameter(4, Types.ARRAY, typeTableName);
	 * cs.execute();
	 * 
	 * Object object = cs.getObject(3);
	 * 
	 * if(object != null) { Object[] data = (Object[]) ((Array)
	 * cs.getObject(3)).getArray();
	 * 
	 * for (Object tmp : data) { Struct row = (Struct) tmp; Object[] attributes
	 * = row.getAttributes();
	 * 
	 * PABenefitsDTO paBenefitsDTO = new PABenefitsDTO(); String benefitCover =
	 * null != attributes[0] ? String.valueOf(attributes[0]) : null; String
	 * percentage = null != attributes[1] ? String.valueOf(attributes[1]) :
	 * null; Double sumInsuredValue = null != attributes[2] ?
	 * Double.valueOf(String.valueOf(attributes[2])) : 0d ; Double eligibleAmt =
	 * null != attributes[3] ? Double.valueOf(String.valueOf(attributes[3])) :
	 * 0d ;
	 * 
	 * paBenefitsDTO.setBenefitCover(benefitCover);
	 * paBenefitsDTO.setPercentage(percentage);
	 * paBenefitsDTO.setSumInsured(sumInsuredValue);
	 * paBenefitsDTO.setEligibleAmout(eligibleAmt);
	 * 
	 * paBenefitsList.add(paBenefitsDTO);
	 * 
	 * } } } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } if (cs != null) {
	 * cs.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * return paBenefitsList;
	 * 
	 * }
	 */

	public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> getGPACoverValues(
			Long rodID) {
		Connection connection = null;
		CallableStatement cs = null;

		List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> coversTableList = new ArrayList<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_ADD_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GPA_ADD_CVR_CALC(?, ?)}");

			cs.setLong(1, rodID);
			// cs.setLong(3, productId);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(2);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO coversTableDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal prodSI = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal eligibleAmt = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					coversTableDTO.setCoverId(coverId.longValue());
					coversTableDTO.setEligibleAmount(eligibleAmt.doubleValue());
					coversTableDTO.setSiLimit(prodSI.doubleValue());

					coversTableList.add(coversTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return coversTableList;

	}

	/*
	 * public com.shaic.paclaim.billing.processclaimbilling.page.billreview.
	 * AddOnCoversTableDTO getCoverValues(Long coverID) { Connection connection
	 * = null; CallableStatement cs =null;
	 * 
	 * com.shaic.paclaim.billing.processclaimbilling.page.billreview.
	 * AddOnCoversTableDTO coversTableDTO = new
	 * com.shaic.paclaim.billing.processclaimbilling
	 * .page.billreview.AddOnCoversTableDTO();
	 * 
	 * try { connection = BPMClientContext.getConnection(); cs =
	 * connection.prepareCall("{call PRC_PA_ADD_CVR_CALC(?,?,?)}");
	 * cs.setLong(1, coverID); cs.registerOutParameter(2, Types.DOUBLE,
	 * "PROD_SI"); cs.registerOutParameter(3, Types.DOUBLE, "ELIGIBLE_AMT");
	 * cs.execute();
	 * 
	 * Double prodSI = 0d; Double eligibleAmt =0d;
	 * 
	 * if (cs.getObject(2) != null) { prodSI =
	 * Double.valueOf(cs.getObject(2).toString()); } if (cs.getObject(3) !=
	 * null) { eligibleAmt = Double.valueOf(cs.getObject(3).toString()); }
	 * coversTableDTO.setKey(coverID);
	 * coversTableDTO.setEligibleAmount(eligibleAmt);
	 * coversTableDTO.setSiLimit(prodSI);
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } if (cs != null) {
	 * cs.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * return coversTableDTO;
	 * 
	 * }
	 */

	/*
	 * >PRC_PA_BALANCE_SI > >LN_INSURED_KEY - input >LN_CLAIM_KEY input
	 * >LN_ROD_KEY input >LN_AVAIL_SI - output.
	 */

	public Double getGPAAvailableSI(Long LN_INSURED_KEY, Long LN_CLAIM_KEY,
			Long LN_ROD_KEY, Long benefitsId) {
		Connection connection = null;
		CallableStatement cs = null;

		Double availableSI = 0d;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_BALANCE_ASI(?,?,?,?,?)}");
			cs.setLong(1, LN_INSURED_KEY);
			cs.setLong(2, LN_CLAIM_KEY);
			cs.setLong(3, LN_ROD_KEY);
			cs.setLong(4, benefitsId);
			cs.registerOutParameter(5, Types.DOUBLE, "LN_AVAIL_SI");
			cs.execute();

			if (cs.getObject(5) != null) {
				availableSI = Double.valueOf(cs.getObject(5).toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return availableSI;

	}

	public void invokeProcedureForUnnamedPolicy(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call  PRC_GPA_UNNAMED_KEY(?)}");
			cs.setString(1, intimationNumber);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getRTARechargeAvailable(Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String isRechargeAvailable = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_FHO_RTA_CHK(?,?)}");
			cs.setLong(1, claimKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				isRechargeAvailable = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isRechargeAvailable;

	}

	public void getRTARechargeSI(Long policyKey, Long insuredKey, Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_RECHARGED_RTA_SI(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<NewIntimationDto> getPreauthFormDocList(java.sql.Date fromDate,
			java.sql.Date toDate, Long cpuCode, String userId) {
		final String PREAUTH_FORM_DOCS = "{call  PRC_RPT_PRE_AUTH_WOUT_SDOC(?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<NewIntimationDto> preauthFormDocList = new ArrayList<NewIntimationDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PREAUTH_FORM_DOCS);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, cpuCode);
			cs.setString(4, userId);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();
			rset = (ResultSet) cs.getObject(5);

			if (null != rset) {
				NewIntimationDto intimationDto = null;

				while (rset.next()) {

					intimationDto = new NewIntimationDto();

					intimationDto.setIntimationId(rset
							.getString("Intimation No"));
					intimationDto.getInsuredPatient().setInsuredName(
							rset.getString("Insured Patient Name"));
					intimationDto.setDoctorName(rset.getString("Doctor"));
					intimationDto.getHospitalDto().setName(
							rset.getString("Hospital Name"));
					intimationDto.getHospitalDto().setHospitalCode(
							rset.getString("Hospital Code"));
					intimationDto.getHospitalDto().setHospitalTypeValue(
							rset.getString("Hospital Type"));
					intimationDto.getHospitalDto().setAddress(
							rset.getString("Hospital Address"));
					intimationDto.getHospitalDto().setPhoneNumber(
							rset.getString("Hospital Contact Details"));
					intimationDto.setReasonForAdmission(rset
							.getString("Diagnosis"));
					intimationDto.setCpuAddress(rset.getString("CPU_CODE"));
					preauthFormDocList.add(intimationDto);
					intimationDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return preauthFormDocList;

	}

	public Map<String, Object> getWorkPlaceStatus(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> valuesMap = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_PORT_WORKPLACE(?, ?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(2) != null) {
				valuesMap = new WeakHashMap<String, Object>();
				valuesMap.put(SHAConstants.WORKPLACE_STATUS, cs.getObject(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valuesMap;
	}

	public BeanItemContainer<SelectValue> getGPACategory(Long policyKey) {
		Connection connection = null;
		CallableStatement cs = null;

		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try {
			final String typeTableName = "TYP_GPA_INSURED_CATEGORY";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GPA_INSURED_CATEGORY(?, ?)}");

			cs.setLong(1, policyKey);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(2);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					UnnamedRiskDetailsPageDTO unnamedRiskPageDto = new UnnamedRiskDetailsPageDTO();

					String category = null != attributes[0] ? (String) (attributes[0])
							: null;
					String categoryDescription = null != attributes[1] ? (String) (attributes[1])
							: null;

					String categoryWithDescription = category + "-"
							+ categoryDescription;

					/*
					 * SelectValue selectFvrAssigned = new SelectValue();
					 * selectFvrAssigned.setId(1l);
					 * selectFvrAssigned.setValue("A");
					 */

					SelectValue categorySelectValue = new SelectValue(2l,
							categoryWithDescription);
					categoryContainer.addBean(categorySelectValue);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return categoryContainer;

	}

	public List<OptionalCoversDTO> getGPAOptValues(Long coverID) {
		Connection connection = null;
		CallableStatement cs = null;

		List<OptionalCoversDTO> optTableList = new ArrayList<OptionalCoversDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_OPT_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GPA_OPT_CVR_CALC(?, ?)}");

			cs.setLong(1, coverID);
			// cs.setLong(3, productId);
			cs.registerOutParameter(2, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(2);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					OptionalCoversDTO optTableDTO = new OptionalCoversDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal minDays = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal maxDays = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					BigDecimal utilDays = null != attributes[3] ? (BigDecimal) (attributes[3])
							: null;
					BigDecimal availDays = null != attributes[4] ? (BigDecimal) (attributes[4])
							: null;
					BigDecimal perDayAmt = null != attributes[5] ? (BigDecimal) (attributes[5])
							: null;
					BigDecimal minAmt = null != attributes[6] ? (BigDecimal) (attributes[6])
							: null;
					BigDecimal maxAmt = null != attributes[7] ? (BigDecimal) (attributes[7])
							: null;
					BigDecimal balAmt = null != attributes[8] ? (BigDecimal) (attributes[8])
							: null;
					String coverEligible = null != attributes[9] ? (String) (attributes[9])
							: null;
					optTableDTO.setCoverId(coverId.longValue());
					optTableDTO.setMaxDaysAllowed(maxDays.intValue());
					if (utilDays != null) {
						optTableDTO.setNoOfDaysUtilised(utilDays.intValue());
					}
					if (availDays != null) {
						optTableDTO.setNoOfDaysAvailable(availDays.intValue());
					}
					optTableDTO.setAllowedAmountPerDay(perDayAmt.doubleValue());
					optTableDTO.setMaxNoOfDaysPerHospital(minDays.intValue());
					optTableDTO.setSiLimit(minAmt.doubleValue());
					optTableDTO.setLimit(maxAmt.doubleValue());
					optTableDTO.setBalanceSI(balAmt.doubleValue());
					optTableDTO.setEligibleForPolicy(coverEligible);
					optTableList.add(optTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return optTableList;
	}

	public List<Map<String, Object>> getTaskProcedureOmpUnlock(
			String intimationNo) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = null;

		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_OMP_LOCKBY (?,?)}");
			cs.setString(1, intimationNo);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				list = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getLockObjFromCursor(rs);
					list.add(mappedValues);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<SearchRRCRequestTableDTO> getRRCCompleteList(
			String intimationNo, String rrcRequestNo, String cpuCode,
			String rrcReqTypeId, String eligibleTypeId, java.sql.Date fromDate,
			java.sql.Date todate) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SearchRRCRequestTableDTO> rrcCompleteList = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_APP_RRC_REPORT(?, ?, ?, ?, ? , ?, ? , ?)}");
			cs.setString(1, intimationNo);
			cs.setString(2, rrcRequestNo);
			cs.setString(3, cpuCode);
			cs.setString(4, rrcReqTypeId);
			cs.setString(5, eligibleTypeId);
			cs.setDate(6, fromDate);
			cs.setDate(7, todate);

			cs.registerOutParameter(8, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(8);

			if (rs != null) {
				rrcCompleteList = new ArrayList<SearchRRCRequestTableDTO>();
				while (rs.next()) {

					SearchRRCRequestTableDTO dto = new SearchRRCRequestTableDTO();

					String intimationNumber = (String) rs
							.getString("INTIMATION_NUMBER");
					String rrcReqstNo = (String) rs.getString("RRC_NO");
					String zoneCode = (String) rs.getString("Zone");
					String rrcInitiatedDate = (String) rs
							.getString("Request_Date");
					String requesterId = (String) rs.getString("Requestor_ID");
					String requesterName = (String) rs
							.getString("Requestor_name");
					Integer initialSumInsured = (Integer) rs
							.getInt("INITIAL_SUM_INSURED");
					Integer sumInsured = (Integer) rs.getInt("SUM_INSURED");
					String productCode = (String) rs
							.getString("TYPE_OF_PRODUCT_CODE");
					String productName = (String) rs
							.getString("TYPE_OF_PRODUCT_NAME");
					String pedDisclosed = (String) rs
							.getString("PED_DISCLOSED");
					String claimType = (String) rs.getString("TYPE_OF_CLAIM");
					String diagnosis = (String) rs.getString("DIAGNOSIS");
					String managementProcedure = (String) rs
							.getString("MANAGEMENT_PROCEDURE");
					String patientName = (String) rs.getString("PATIENT_NAME");
					String hospitalName = (String) rs
							.getString("HOSPITAL_NAME");
					String networkHospitalType = (String) rs
							.getString("ANH_NH_NNH");
					String pedProcessingDoc = (String) rs
							.getString("PED_PROCESSING_DOCTOR");
					String pedSuggesions = (String) rs
							.getString("PED_SUGGESTIONS");
					String pedName = (String) rs.getString("PED_NAME");
					String amountClaimed = (String) rs
							.getString("AMOUNT_CLAIMED");
					String settledAmount = (String) rs
							.getString("SETTLED_AMOUNT");
					String amountSaved = (String) rs.getString("AMOUNT_SAVED");
					String category = (String) rs.getString("CATEGORY");
					String status = (String) rs.getString("STATUS");
					String statusDate = (String) rs.getString("STATUS_DATE");
					String claimsProRemarks = (String) rs
							.getString("CLAIMS_PROC_REMARKS");
					String rrcRemarks = (String) rs.getString("RRC_REMARKS");
					String credit1 = (String) rs.getString("CREDIT1");
					String credit2 = (String) rs.getString("CREDIT2");
					String credit3 = (String) rs.getString("CREDIT3");
					String credit4 = (String) rs.getString("CREDIT4");
					String credit5 = (String) rs.getString("CREDIT5");
					String credit6 = (String) rs.getString("CREDIT6");
					String credit7 = (String) rs.getString("CREDIT7");
					String lapse1 = (String) rs.getString("LAPSE1");
					String lapse2 = (String) rs.getString("LAPSE2");
					String lapse3 = (String) rs.getString("LAPSE3");
					String lapse4 = (String) rs.getString("LAPSE4");
					String lapse5 = (String) rs.getString("LAPSE5");
					String lapse6 = (String) rs.getString("LAPSE6");
					String lapse7 = (String) rs.getString("LAPSE7");

					dto.setIntimationNo(intimationNumber);
					dto.setRrcRequestNo(rrcReqstNo);
					dto.setCpuCode(zoneCode);
					dto.setDateOfRequestForTable(rrcInitiatedDate);
					dto.setRequestorId(requesterId);
					dto.setRequestorName(requesterName);
					dto.setInitialSumInsured(Double.valueOf(initialSumInsured));
					dto.setPresentSumInsured(Long.valueOf(sumInsured));
					dto.setProductCode(productCode);
					dto.setProductName(productName);
					dto.setPedDisclosed(pedDisclosed);
					dto.setClaimType(claimType);
					dto.setDiag(diagnosis);
					dto.setManagement(managementProcedure);
					dto.setPatientName(patientName);
					dto.setHospitalName(hospitalName);
					dto.setHospitalType(networkHospitalType);
					dto.setInitialPEDRecommendation(pedProcessingDoc);
					dto.setPedSuggestions(pedSuggesions);
					dto.setPedName(pedName);
					dto.setAmountClaimed(amountClaimed);
					dto.setSettledAmount(settledAmount);
					dto.setAmountSaved(amountSaved);
					dto.setCategoryValue(category);
					dto.setStatus(status);
					dto.setStatusDate(statusDate);
					dto.setInitiateRRCRemarks(claimsProRemarks);
					dto.setProcessRRCRemarks(rrcRemarks);
					dto.setCreditsEmployeeName(credit1);
					dto.setCreditsEmployeeName2(credit2);
					dto.setCreditsEmployeeName3(credit3);
					dto.setCreditsEmployeeName4(credit4);
					dto.setCreditsEmployeeName5(credit5);
					dto.setCreditsEmployeeName6(credit6);
					dto.setCreditsEmployeeName7(credit7);
					dto.setLapseEmployeeName(lapse1);
					dto.setLapseEmployeeName2(lapse2);
					dto.setLapseEmployeeName3(lapse3);
					dto.setLapseEmployeeName4(lapse4);
					dto.setLapseEmployeeName5(lapse5);
					dto.setLapseEmployeeName6(lapse6);
					dto.setLapseEmployeeNmae7(lapse7);
					rrcCompleteList.add(dto);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rrcCompleteList;
	}

	
	//Dinesh
	public List<SearchRRCStatusTableDTO> getRRCStatusList(
			String intimationNo, String rrcRequestNo, String cpuCode,
			String rrcReqTypeId, String eligibleTypeId, java.sql.Date fromDate,
			java.sql.Date todate, String userName) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SearchRRCStatusTableDTO> rrcCompleteList = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_RPT_RRC_STATUS(?, ?, ?, ?, ? , ?, ? , ? , ?)}");
			cs.setString(1, intimationNo);
			cs.setString(2, rrcRequestNo);
			cs.setString(3, cpuCode);
			cs.setString(4, rrcReqTypeId);
			cs.setString(5, eligibleTypeId);
			cs.setDate(6, fromDate);
			cs.setDate(7, todate);
			cs.setString(8, userName);

			cs.registerOutParameter(9, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(9);

			if (rs != null) {
				rrcCompleteList = new ArrayList<SearchRRCStatusTableDTO>();
				while (rs.next()) {

					SearchRRCStatusTableDTO dto = new SearchRRCStatusTableDTO();

					String intimationNumber = (String) rs
							.getString("INTIMATION_NUMBER");
					String rrcReqstNo = (String) rs.getString("RRC_NO");
					String zoneCode = (String) rs.getString("Zone");
					String rrcInitiatedDate = (String) rs
							.getString("Request_Date");
					String requesterId = (String) rs.getString("Requestor_ID");
					String requesterName = (String) rs
							.getString("Requestor_name");
					Integer initialSumInsured = (Integer) rs
							.getInt("INITIAL_SUM_INSURED");
					Integer sumInsured = (Integer) rs.getInt("SUM_INSURED");
					String productCode = (String) rs
							.getString("TYPE_OF_PRODUCT_CODE");
					String productName = (String) rs
							.getString("TYPE_OF_PRODUCT_NAME");
					String pedDisclosed = (String) rs
							.getString("PED_DISCLOSED");
					String claimType = (String) rs.getString("TYPE_OF_CLAIM");
					String diagnosis = (String) rs.getString("DIAGNOSIS");
					String managementProcedure = (String) rs
							.getString("MANAGEMENT_PROCEDURE");
					String patientName = (String) rs.getString("PATIENT_NAME");
					String hospitalName = (String) rs
							.getString("HOSPITAL_NAME");
					String networkHospitalType = (String) rs
							.getString("ANH_NH_NNH");
					String pedProcessingDoc = (String) rs
							.getString("PED_PROCESSING_DOCTOR");
					String pedSuggesions = (String) rs
							.getString("PED_SUGGESTIONS");
					String pedName = (String) rs.getString("PED_NAME");
					String amountClaimed = (String) rs
							.getString("AMOUNT_CLAIMED");
					String settledAmount = (String) rs
							.getString("SETTLED_AMOUNT");
					String amountSaved = (String) rs.getString("AMOUNT_SAVED");
					String category = (String) rs.getString("CATEGORY");
					String status = (String) rs.getString("STATUS");
					String statusDate = (String) rs.getString("STATUS_DATE");
					String claimsProRemarks = (String) rs
							.getString("CLAIMS_PROC_REMARKS");
					String rrcRemarks = (String) rs.getString("RRC_REMARKS");
					String credit1 = (String) rs.getString("CREDIT1");
					String credit2 = (String) rs.getString("CREDIT2");
					String credit3 = (String) rs.getString("CREDIT3");
					String credit4 = (String) rs.getString("CREDIT4");
					String credit5 = (String) rs.getString("CREDIT5");
					String credit6 = (String) rs.getString("CREDIT6");
					String credit7 = (String) rs.getString("CREDIT7");
					String lapse1 = (String) rs.getString("LAPSE1");
					String lapse2 = (String) rs.getString("LAPSE2");
					String lapse3 = (String) rs.getString("LAPSE3");
					String lapse4 = (String) rs.getString("LAPSE4");
					String lapse5 = (String) rs.getString("LAPSE5");
					String lapse6 = (String) rs.getString("LAPSE6");
					String lapse7 = (String) rs.getString("LAPSE7");

					dto.setIntimationNo(intimationNumber);
					dto.setRrcRequestNo(rrcReqstNo);
					dto.setCpuCode(zoneCode);
					dto.setDateOfRequestForTable(rrcInitiatedDate);
					dto.setRequestorId(requesterId);
					dto.setRequestorName(requesterName);
					dto.setInitialSumInsured(Double.valueOf(initialSumInsured));
					dto.setPresentSumInsured(Long.valueOf(sumInsured));
					dto.setProductCode(productCode);
					dto.setProductName(productName);
					dto.setPedDisclosed(pedDisclosed);
					dto.setClaimType(claimType);
					dto.setDiag(diagnosis);
					dto.setManagement(managementProcedure);
					dto.setPatientName(patientName);
					dto.setHospitalName(hospitalName);
					dto.setHospitalType(networkHospitalType);
					dto.setInitialPEDRecommendation(pedProcessingDoc);
					dto.setPedSuggestions(pedSuggesions);
					dto.setPedName(pedName);
					dto.setAmountClaimed(amountClaimed);
					dto.setSettledAmount(settledAmount);
					dto.setAmountSaved(amountSaved);
					dto.setCategoryValue(category);
					dto.setStatus(status);
					dto.setStatusDate(statusDate);
					dto.setInitiateRRCRemarks(claimsProRemarks);
					dto.setProcessRRCRemarks(rrcRemarks);
					dto.setCreditsEmployeeName(credit1);
					dto.setCreditsEmployeeName2(credit2);
					dto.setCreditsEmployeeName3(credit3);
					dto.setCreditsEmployeeName4(credit4);
					dto.setCreditsEmployeeName5(credit5);
					dto.setCreditsEmployeeName6(credit6);
					dto.setCreditsEmployeeName7(credit7);
					dto.setLapseEmployeeName(lapse1);
					dto.setLapseEmployeeName2(lapse2);
					dto.setLapseEmployeeName3(lapse3);
					dto.setLapseEmployeeName4(lapse4);
					dto.setLapseEmployeeName5(lapse5);
					dto.setLapseEmployeeName6(lapse6);
					dto.setLapseEmployeeNmae7(lapse7);
					rrcCompleteList.add(dto);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rrcCompleteList;
	}
	
	
	
	
	public String callOMPLockProcedure(Long wrkFlowKey, String currentqueue,
			String userID) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_OMP_LOCK_USER (?,?,?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.setString(2, currentqueue);
			cs.setString(3, userID);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getString(4) != null) {
				successMsg = new String();
				successMsg = cs.getString(4);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String callOMPUnlockProcedure(Long wrkFlowKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_OMP_UNLOCK_USER (?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = new String();
				successMsg = cs.getString(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public String initiateOMPTaskProcedure(Object[] input){
		Connection connection = null;
		CallableStatement cs =null;
		OracleConnection conn= null;
		String successMsg= null;
		try{
			
//			connection = BPMClientContext.getConnectionFromURL();
			
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);
			
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_SEC_OMP_SUBMIT_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des,conn,input);
			cs =  conn.prepareCall("{call PRC_SEC_OMP_SUBMIT_TASK (?,?)}");
			cs.setArray(1, arrayToPass);
		
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println("---THE STRING---"+cs.getString(2));
			
			if(cs.getString(2) != null) {
				successMsg =null;
				successMsg = cs.getNString(2).toString();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Map<String, Object>> getOMPTaskProcedure(Object[] input){
		
		Connection connection = null;
		CallableStatement cs =null;
		OracleConnection conn= null;
		ResultSet rs = null;
		List<Map<String,Object>> list = null;
		
		
		try{
			
//			connection = BPMClientContext.getConnectionFromURL();
			
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);
			
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_SEC_OMP_GET_TASK", conn);
			//LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des,conn,input);
			 cs =  conn.prepareCall("{call PRC_SEC_OMP_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);
		
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			
		
			
			rs = (ResultSet)cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String,Object> mappedValues = SHAUtils.getOMPObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);
					
					list.add(mappedValues);
				}
				
			}
			
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
				if(rs!=null){
					rs.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
		
		
	}

	/*
	 * public Map<String, Double> getOMPBalanceSI(Long policyKey, Long
	 * insuredKey, Long claimKey, Double sumInsured,Long intimationKey) {
	 * 
	 * Connection connection = null; CallableStatement cs= null; Double
	 * totalBalanceSI = 0d; Double currentBalanceSI = 0d; Map<String, Double>
	 * values = new HashMap<String, Double>(); try { connection =
	 * BPMClientContext.getConnection(); cs = connection
	 * .prepareCall("{call PRC_OMP_BALANCE_SI(?, ?, ?, ?, ?, ?,?)}");
	 * cs.setLong(1, policyKey); cs.setLong(2, insuredKey); cs.setLong(3,
	 * intimationKey); cs.setLong(4, claimKey);
	 * 
	 * if (sumInsured != null) { cs.setDouble(5, sumInsured); } else {
	 * cs.setDouble(5, 0); }
	 * 
	 * cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
	 * cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
	 * cs.execute();
	 * 
	 * // return (Double) cs.getObject(4); totalBalanceSI = (Double)
	 * cs.getObject(6); currentBalanceSI = (Double) cs.getObject(7); } catch
	 * (SQLException e) { e.printStackTrace(); } finally { try { if (connection
	 * != null) { connection.close(); } if (cs != null) { cs.close(); } } catch
	 * (SQLException e) { e.printStackTrace(); } } // return new Double("0");
	 * values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI != null ?
	 * totalBalanceSI : 0d); values.put(SHAConstants.CURRENT_BALANCE_SI,
	 * currentBalanceSI != null ? currentBalanceSI : 0d); return values; }
	 */

	public Double getOMPInsuredSumInsured(String insuredId, Long policyKey) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OMP_INSURED_SUM_INSURED(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, Long.valueOf(insuredId));

			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(3, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(3) ? Double.parseDouble(cs
					.getObject(3).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, List> getOMPComprehensiveSI(Long insuredKey, String intimationNo, Double sumInsured, Long prodKey){
				
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, List> listOfTable = new HashMap<String, List>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OMP_PREV_CLAIM_2(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);
			
			cs.registerOutParameter(5,Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6,Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7,Types.ARRAY, "TYP_SI_LIST");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();
			
			
			List<ComprehensiveHospitalisationTableDTO> sectionTable1 = new ArrayList<ComprehensiveHospitalisationTableDTO>();
			List<ComprehensiveDeliveryNewBornTableDTO> sectionTable2 = new ArrayList<ComprehensiveDeliveryNewBornTableDTO>();
			List<ComprehensiveOutpatientTableDTO> sectionTable3 = new ArrayList<ComprehensiveOutpatientTableDTO>();
			List<ComprehensiveHospitalCashTableDTO> sectionTable4 = new ArrayList<ComprehensiveHospitalCashTableDTO>();
			List<ComprehensiveHealthCheckTableDTO> sectionTable5 = new ArrayList<ComprehensiveHealthCheckTableDTO>();
			List<ComprehensiveBariatricSurgeryTableDTO> sectionTable6 = new ArrayList<ComprehensiveBariatricSurgeryTableDTO>();
			List<LumpSumTableDTO> sectionTable8 = new ArrayList<LumpSumTableDTO>();
			List<OMPBalanceSiTableDTO> sectionTable9 = new ArrayList<OMPBalanceSiTableDTO>();
			for (Object object : data) {
				
				Struct row = (Struct) object;
				Object[] attributes = row.getAttributes();
				if(attributes != null){
					String sectionNameWithCode = (String)attributes[0];
					String sectionCode = (String)attributes[1];
					String coverName = (String)attributes[2];
					String coverCode = (String)attributes[3];
					String subCoverName = (String)attributes[4];
					String subCoverCode = (String)attributes[5];
					Double limitAmt = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[6]));
					Double claimsPaid = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[7]));
					Double outStanding = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[8]));
					Double balanceSumInsured = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[9]));
					Double currentProvisionAmt = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[10]));
					Double balSI = SHAUtils.getDoubleValueFromString(String.valueOf(attributes[11]));
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_1)){
						ComprehensiveHospitalisationTableDTO section1 = new ComprehensiveHospitalisationTableDTO();
						
						section1.setSectionI(sectionNameWithCode.split("-")[2]);
						section1.setCover(coverName);
						section1.setSubCover(subCoverName);
						section1.setSumInsured(limitAmt);
						section1.setClaimPaid(claimsPaid);
						section1.setClaimOutStanding(outStanding);
						section1.setBalance(balanceSumInsured);
						section1.setProvisionCurrentClaim(currentProvisionAmt);
						section1.setBalanceSI(balSI);
						
						sectionTable1.add(section1);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_2)){
						ComprehensiveDeliveryNewBornTableDTO section2 = new ComprehensiveDeliveryNewBornTableDTO();
						
						section2.setSectionII(sectionNameWithCode.split("-")[2]);
						section2.setCover(coverName);
						section2.setSubCover(subCoverName);
						section2.setLimit(limitAmt);
						section2.setClaimPaid(claimsPaid);
						section2.setClaimOutstanding(outStanding);
						section2.setBalance(balanceSumInsured);
						section2.setProvisionCurrentClaim(currentProvisionAmt);
						section2.setBalanceSI(balSI);
						sectionTable2.add(section2);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_3)){
						ComprehensiveOutpatientTableDTO section3 = new ComprehensiveOutpatientTableDTO();
						
						section3.setSectionIII(sectionNameWithCode.split("-")[2]);
						section3.setCover(coverName);
						section3.setSubCover(subCoverName);
						section3.setLimit(limitAmt);
						section3.setClaimPaid(claimsPaid);
						section3.setClaimOutstanding(outStanding);
						section3.setBalance(balanceSumInsured);
						section3.setProvisionCurrentClaim(currentProvisionAmt);
						section3.setBalanceSI(balSI);
						sectionTable3.add(section3);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_4)){
						ComprehensiveHospitalCashTableDTO section4 = new ComprehensiveHospitalCashTableDTO();
						
						section4.setSectionIV(sectionNameWithCode.split("-")[2]);
						section4.setCover(coverName);
						section4.setSubCover(subCoverName);
						section4.setLimit(limitAmt);
						section4.setClaimPaid(claimsPaid);
						section4.setClaimOutstanding(outStanding);
						section4.setBalance(balanceSumInsured);
						section4.setProvisionCurrentClaim(currentProvisionAmt);
						section4.setBalanceSI(balSI);
						sectionTable4.add(section4);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_5)){
						ComprehensiveHealthCheckTableDTO section5 = new ComprehensiveHealthCheckTableDTO();
						
						section5.setSectionV(sectionNameWithCode.split("-")[2]);
						section5.setCover(coverName);
						section5.setSubCover(subCoverName);
						section5.setLimit(limitAmt);
						section5.setClaimPaid(claimsPaid);
						section5.setClaimOutstanding(outStanding);
						section5.setBalance(balanceSumInsured);
						section5.setProvisionCurrentClaim(currentProvisionAmt);
						section5.setBalanceSI(balSI);
						sectionTable5.add(section5);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_6)){
						ComprehensiveBariatricSurgeryTableDTO section6 = new ComprehensiveBariatricSurgeryTableDTO();
						
						section6.setSectionVI(sectionNameWithCode.split("-")[2]);
						section6.setCover(coverName);
						section6.setSubCover(subCoverName);
						section6.setLimit(limitAmt);
						section6.setClaimPaid(claimsPaid);
						section6.setClaimOutstanding(outStanding);
						section6.setBalance(balanceSumInsured);
						section6.setProvisionCurrentClaim(currentProvisionAmt);
						section6.setBalanceSI(balSI);
						sectionTable6.add(section6);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_8)){
						LumpSumTableDTO section8 = new LumpSumTableDTO();
						
						section8.setSectionVIII(sectionNameWithCode.split("-")[2]);
						section8.setCover(coverName);
						section8.setSubCover(subCoverName);
						section8.setLimit(limitAmt);
						section8.setClaimPaid(claimsPaid);
						section8.setClaimOutstanding(outStanding);
						section8.setBalance(balanceSumInsured);
						section8.setProvisionCurrentClaim(currentProvisionAmt);
						section8.setBalanceSI(balSI);
						
						sectionTable8.add(section8);
					}
					
					if(sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.SECTION_CODE_8)){
						OMPBalanceSiTableDTO section9 = new OMPBalanceSiTableDTO();
						
//						section9.setSectionVIII(sectionNameWithCode.split("-")[2]);
						section9.setCoverCodeDescription(coverName);
						section9.setCoverCode(coverCode);
//						section9.setLimit(limitAmt);
						section9.setClaimPaid(claimsPaid);
						section9.setClaimOustanding(outStanding);
						section9.setBalance(balanceSumInsured);
						section9.setProvisionforcurrentclaim(currentProvisionAmt);
						section9.setbSIafterProvision(balSI);
						
						sectionTable9.add(section9);
					}
					
				}
			}
			
			
			listOfTable.put(SHAConstants.SECTION_CODE_1, sectionTable1);
			listOfTable.put(SHAConstants.SECTION_CODE_2, sectionTable2);
			listOfTable.put(SHAConstants.SECTION_CODE_3, sectionTable3);
			listOfTable.put(SHAConstants.SECTION_CODE_4, sectionTable4);
			listOfTable.put(SHAConstants.SECTION_CODE_5, sectionTable5);
			listOfTable.put(SHAConstants.SECTION_CODE_6, sectionTable6);
			listOfTable.put(SHAConstants.SECTION_CODE_8, sectionTable8);
			listOfTable.put(SHAConstants.SECTION_CODE_9, sectionTable9);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTable;
		
		
	}

	@SuppressWarnings("rawtypes")
	public List<OMPBalanceSiTableDTO> getOMPBalanceSIView(Long insuredKey,
			Long claimKey, Long rodKey) {
		Connection connection = null;
		CallableStatement cs = null;

		Map<String, List> listOfTable = new HashMap<String, List>();
		List<OMPBalanceSiTableDTO> listBalSIDTO = new ArrayList<OMPBalanceSiTableDTO>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_OMP_VIEW_SI(?, ?, ?,?)}");
			cs.setLong(1, insuredKey);
			cs.setLong(2, claimKey);
			if (rodKey != null) {
				cs.setLong(3, rodKey);
			} else {
				cs.setLong(3, 0L);
			}
			cs.registerOutParameter(4, Types.ARRAY, "TYP_OMP_VIEW_SI");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(4)).getArray();

			OMPBalanceSiTableDTO ompBsiDto = null;
			for (Object object : data) {

				Struct row = (Struct) object;
				Object[] attributes = row.getAttributes();
				if (attributes != null) {
					ompBsiDto = new OMPBalanceSiTableDTO();
					ompBsiDto
							.setCoverCode(attributes[0] != null ? (String) attributes[0]
									: "");
					ompBsiDto
							.setCoverCodeDescription(attributes[1] != null ? (String) attributes[1]
									: "");

					ompBsiDto.setSumInsured(attributes[4] != null ? SHAUtils
							.getLongFromString(String.valueOf(attributes[4]))
							.longValue() : 0l);
					ompBsiDto.setClaimPaid(attributes[5] != null ? SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[5])) : 0d);
					ompBsiDto
							.setClaimOustanding(attributes[6] != null ? SHAUtils
									.getDoubleValueFromString(String
											.valueOf(attributes[6])) : 0d);

					ompBsiDto.setBalance(attributes[7] != null ? SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[7])) : 0d);

					ompBsiDto
							.setProvisionforcurrentclaim(attributes[8] != null ? SHAUtils
									.getDoubleValueFromString(String
											.valueOf(attributes[8])) : 0d);

					ompBsiDto
							.setbSIafterProvision(attributes[9] != null ? SHAUtils
									.getDoubleValueFromString(String
											.valueOf(attributes[9])) : 0d);

					listBalSIDTO.add(ompBsiDto);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listBalSIDTO;
	}

	public Map<String, Double> getOmpBalanceSI(Long policyKey, Long insuredKey,
			Long claimKey, Long rodKey, Double sumInsured, Long intimationKey,
			String eventCode) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OMP_BALANCE_SI (?, ?, ?, ?, ?, ?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setString(2, eventCode);
			cs.setLong(3, insuredKey);
			cs.setLong(4, intimationKey);
			if (claimKey != null) {
				cs.setLong(5, claimKey);
			} else {
				cs.setLong(5, 0L);
			}
			if (rodKey != null) {
				cs.setLong(6, 0L);
			} else {
				cs.setLong(6, 0L);
			}

			if (sumInsured != null) {
				cs.setDouble(7, sumInsured);
			} else {
				cs.setDouble(7, 0);
			}

			cs.registerOutParameter(8, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(9, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(8);
			currentBalanceSI = (Double) cs.getObject(9);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public Double getOmpInsuredSumInsured(Long policyKey, String eventCode) {
		// public Double getInsuredSumInsured(Long policyKey, String insuredId)
		// {
		Connection connection = null;
		CallableStatement cs = null;
		Double sumInsured = 0d;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OMP_INSURED_SUM_INSURED(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setString(2, eventCode);
			// System.out.println("---- policy Key ---- " + policyKey);
			// System.out.println("--- insured Id ****** " + insuredId);

			// Result is an java.sql.Array...
			cs.registerOutParameter(3, Types.DOUBLE, "LN_SUM_INSURED");

			cs.execute();
			sumInsured = (null != cs.getObject(3) ? Double.parseDouble(cs
					.getObject(3).toString()) : 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumInsured;
	}

	public Map<String, Double> getOmpDeductible(Long productKey, String plan,
			Double sumInsured, String eventCode, Double insuredSumInsured) {

		Connection connection = null;
		CallableStatement cs = null;
		Double deductibles = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_GET_OMP_DEDUCTIBLE (?, ?, ?, ?, ?,?)}");
			cs.setLong(1, productKey);
			cs.setString(2, plan);

			if (insuredSumInsured != null) {
				cs.setDouble(3, insuredSumInsured);
			} else {
				cs.setDouble(3, 0);
			}
			if (sumInsured != null) {
				cs.setDouble(4, sumInsured);
			} else {
				cs.setDouble(4, 0);
			}
			cs.setString(5, eventCode);

			cs.registerOutParameter(6, Types.DOUBLE, "LN_DEDUCTIBLE");
			cs.execute();

			// return (Double) cs.getObject(4);
			deductibles = (Double) cs.getObject(6);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.DEDUCTIBLES, deductibles != null ? deductibles
				: 0d);
		return values;
	}

	public int updateCurrencyRate(Double currencyrate, Long rodKey) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int iRowCount = 0;
		try {
			connection = BPMClientContext.getConnection();
			// connection.setAutoCommit(false);
			if (null != connection) {
				String updateQuery = "UPDATE IMS_CLS_OMP_REIMBURSEMENT SET INR_CONVERSION_RATE = ?"
						+ " WHERE REIMBURSEMENT_KEY = ?";

				System.out
						.println("----Update Query for Update conversion rate  ---->"
								+ updateQuery);
				preparedStatement = connection.prepareStatement(updateQuery);
				if (null != preparedStatement) {
					preparedStatement.setDouble(1, currencyrate);
					preparedStatement.setLong(2, rodKey);

					iRowCount = preparedStatement.executeUpdate();
					// connection.commit();
				}
			}

		} catch (Exception e) {
			// if(null != connection)
			try {
				if (connection != null) {
					connection.rollback();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return iRowCount;
	}

	public void getOmpProvisionCalc(Long rodKey, Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_OMP_PROV_AMT_CALC (?, ?)}");
			cs.setLong(1, rodKey);
			cs.setLong(2, claimKey);
			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public CPUWisePreauthResultDto getPreauthCpuwiseReport(
			java.sql.Date fromDate, java.sql.Date toDate, Long cpuCode,
			String userId) {

		CPUWisePreauthResultDto resultDto = new CPUWisePreauthResultDto();
		List<CPUwisePreauthReportDto> resultListDto = new ArrayList<CPUwisePreauthReportDto>();

		final String TAT_COMPLETED_CLAIMS = "{call PRC_RPT_PRE_AUTH_CPU_SUMMARY(?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(TAT_COMPLETED_CLAIMS);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, null != cpuCode ? cpuCode : 0l);
			cs.setString(4, userId);
			cs.registerOutParameter(5, OracleTypes.CURSOR);

			cs.execute();
			rset = (ResultSet) cs.getObject(5);

			if (null != rset) {
				CPUwisePreauthReportDto preauthCpuDto = null;
				int slno = 0;
				while (rset.next()) {

					preauthCpuDto = new CPUwisePreauthReportDto();
					preauthCpuDto.setSno(++slno);
					preauthCpuDto.setIntimationNo(rset
							.getString("INTIMATION_NUMBER"));
					preauthCpuDto.setPolicyNumber(rset
							.getString("POLICY_NUMBER"));
					preauthCpuDto
							.setProductName(rset.getString("PRODUCT_NAME"));
					preauthCpuDto.setPreauthStatus(rset
							.getString("PRE_AUTH_STATUS"));
					preauthCpuDto.setPreauthReqAmount(rset
							.getString("PRE_AUTH_REQ_AMOUNT"));
					preauthCpuDto.setPreauthAmount(String.valueOf(rset
							.getDouble("PRE_AUTH_AMOUNT")));
					String preDate = rset.getString("PRE_AUTH_DATE");
					preauthCpuDto
							.setPreauthDate(preDate != null ? preDate : "");
					preauthCpuDto.setDiagnosis(rset.getString("DIAGNOSIS"));
					preauthCpuDto
							.setPatientName(rset.getString("PATIENT_NAME"));
					preauthCpuDto.setPatientAge(rset.getString("PATIENT_AGE"));
					preauthCpuDto.setHospitalName(rset
							.getString("HOSPITAL_NAME"));
					preauthCpuDto
							.setAnhHospital(rset.getString("ANH_HOSPITAL"));
					preauthCpuDto.setTreatmentType(rset
							.getString("TREATMENT_TYPE"));
					preauthCpuDto.setDoctor(rset.getString("DOCTOR_NAME"));
					preauthCpuDto.setRemarks(rset.getString("REMARKS"));

					resultListDto.add(preauthCpuDto);
					preauthCpuDto = null;
				}
			}
			resultDto.setCpuwisePreauthProcessedList(resultListDto);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultDto;

	}

	public List<PreviousClaimsTableDTO> getPreviousClaims(Long claimKey,
			Long policyKey, Long insuredKey, String searchType) {

		List<PreviousClaimsTableDTO> previous = new ArrayList<PreviousClaimsTableDTO>();

		final String PREVIOUS_CLAIM_PROC = "{call PRC_GET_PREVIOUS_CLAIM(?, ?, ?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PREVIOUS_CLAIM_PROC);
			cs.setLong(1, claimKey);
			cs.setLong(2, policyKey);
			cs.setLong(3, insuredKey);
			cs.setString(4, searchType);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(5);

			if (rs != null) {
				PreviousClaimsTableDTO previousclaimDto = null;
				while (rs.next()) {

					previousclaimDto = new PreviousClaimsTableDTO();
					previousclaimDto.setPolicyNumber(rs
							.getString("POLICY_NUMBER"));
					previousclaimDto.setPolicyYear(rs.getString("POLICY_YEAR"));
					previousclaimDto.setClaimNumber(rs
							.getString("CLAIM_NUMBER"));
					previousclaimDto.setClaimType(rs.getString("CLAIM_TYPE"));
					previousclaimDto.setIntimationNumber(rs
							.getString("INTIMATION_NUMBER"));
					previousclaimDto.setBenefits(rs.getString("BENIFITS"));
					previousclaimDto.setInsuredPatientName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setInsuredName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setParentName(rs.getString("PARENT_NAME"));
					// previousclaimDto.setPatientName(rs.getString("PATIENT_NAME"));
					previousclaimDto.setParentDOB(rs.getDate("PARENT_DOB"));
					previousclaimDto.setDiagnosis(rs.getString("DIAGNOSIS"));
					previousclaimDto.setDiagnosisForPreviousClaim(rs
							.getString("ADMISSION_DATE"));
					previousclaimDto.setPatientDOB(rs.getDate("PATIENT_DOB"));
					if (rs.getDate("ADMISSION_DATE") != null) {
						Date admissionDate = rs.getDate("ADMISSION_DATE");
						String dateWithoutTime = SHAUtils
								.getDateWithoutTime(admissionDate);
						previousclaimDto.setAdmissionDate(dateWithoutTime);
					}
					previousclaimDto.setClaimStatus(rs
							.getString("CLAIM_STATUS"));
					previousclaimDto.setClaimAmount(String.valueOf(rs
							.getDouble("CLAIM_AMOUNT")));
					previousclaimDto.setApprovedAmount(String.valueOf(rs
							.getDouble("APPROVED_AMOUNT")));
					previousclaimDto.setCopayPercentage(rs
							.getDouble("COPAY_PERCENTAGE"));
					previousclaimDto.setHospitalName(rs
							.getString("HOSPITAL_NAME"));
					previousclaimDto.setPedName(rs.getString("PED_NAME"));
					previousclaimDto.setIcdCodes(rs.getString("ICD_CODE"));
					previousclaimDto.setKey(rs.getLong("CLAIM_KEY"));
					// CR20181291
					previousclaimDto.setIsSubLimitApplicable(rs
							.getString("SUBLIMIT_APPLICABLE"));
					if (rs.getString("SUBLIMIT_APPLICABLE") == null
							|| rs.getString("SUBLIMIT_APPLICABLE").equals("N,")) {
						previousclaimDto.setSubLimitApplicable("No");
					} else {
						previousclaimDto.setSubLimitApplicable("Yes");
					}
					previousclaimDto.setSubLimitName(rs
							.getString("SUBLIMIT_NAME"));
					previousclaimDto.setSubLimitAmount(rs
							.getString("SUBLIMIT_AMOUNT"));
					// CR2019052
					previousclaimDto.setForSIRest(rs
							.getString("SI_RESTRICATION_FLAG"));

					if (rs.getString("SI_RESTRICATION_FLAG").equals("Y")) {
						previousclaimDto.setSiRestrication("Yes");
					} else {
						previousclaimDto.setSiRestrication("No");
					}

					previous.add(previousclaimDto);
					previousclaimDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return previous;
	}
	public List<PreviousClaimsTableDTO> getBasePolicyPreviousClaims(Long policyKey, String searchType) {

		List<PreviousClaimsTableDTO> previous = new ArrayList<PreviousClaimsTableDTO>();

		final String BASE_POL_PREVIOUS_CLAIM_PROC = "{call PRC_GET_BASE_POL_PRVS_CLAIM(?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(BASE_POL_PREVIOUS_CLAIM_PROC);
			cs.setLong(1, policyKey);
			cs.setString(2, searchType);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				PreviousClaimsTableDTO previousclaimDto = null;
				while (rs.next()) {

					previousclaimDto = new PreviousClaimsTableDTO();
					previousclaimDto.setPolicyNumber(rs
							.getString("POLICY_NUMBER"));
					previousclaimDto.setPolicyYear(rs.getString("POLICY_YEAR"));
					previousclaimDto.setClaimNumber(rs
							.getString("CLAIM_NUMBER"));
					previousclaimDto.setClaimType(rs.getString("CLAIM_TYPE"));
					previousclaimDto.setIntimationNumber(rs
							.getString("INTIMATION_NUMBER"));
					previousclaimDto.setBenefits(rs.getString("BENIFITS"));
					previousclaimDto.setInsuredPatientName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setInsuredName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setParentName(rs.getString("PARENT_NAME"));
					// previousclaimDto.setPatientName(rs.getString("PATIENT_NAME"));
					previousclaimDto.setParentDOB(rs.getDate("PARENT_DOB"));
					previousclaimDto.setDiagnosis(rs.getString("DIAGNOSIS"));
					previousclaimDto.setDiagnosisForPreviousClaim(rs
							.getString("ADMISSION_DATE"));
					previousclaimDto.setPatientDOB(rs.getDate("PATIENT_DOB"));
					if (rs.getDate("ADMISSION_DATE") != null) {
						Date admissionDate = rs.getDate("ADMISSION_DATE");
						String dateWithoutTime = SHAUtils
								.getDateWithoutTime(admissionDate);
						previousclaimDto.setAdmissionDate(dateWithoutTime);
					}
					previousclaimDto.setClaimStatus(rs
							.getString("CLAIM_STATUS"));
					previousclaimDto.setClaimAmount(String.valueOf(rs
							.getDouble("CLAIM_AMOUNT")));
					previousclaimDto.setApprovedAmount(String.valueOf(rs
							.getDouble("APPROVED_AMOUNT")));
					previousclaimDto.setCopayPercentage(rs
							.getDouble("COPAY_PERCENTAGE"));
					previousclaimDto.setHospitalName(rs
							.getString("HOSPITAL_NAME"));
					previousclaimDto.setPedName(rs.getString("PED_NAME"));
					previousclaimDto.setIcdCodes(rs.getString("ICD_CODE"));
					previousclaimDto.setKey(rs.getLong("CLAIM_KEY"));
					// CR20181291
					previousclaimDto.setIsSubLimitApplicable(rs
							.getString("SUBLIMIT_APPLICABLE"));
					if (rs.getString("SUBLIMIT_APPLICABLE") == null
							|| rs.getString("SUBLIMIT_APPLICABLE").equals("N,")) {
						previousclaimDto.setSubLimitApplicable("No");
					} else {
						previousclaimDto.setSubLimitApplicable("Yes");
					}
					previousclaimDto.setSubLimitName(rs
							.getString("SUBLIMIT_NAME"));
					previousclaimDto.setSubLimitAmount(rs
							.getString("SUBLIMIT_AMOUNT"));
					// CR2019052
					previousclaimDto.setForSIRest(rs
							.getString("SI_RESTRICATION_FLAG"));

					if (rs.getString("SI_RESTRICATION_FLAG").equals("Y")) {
						previousclaimDto.setSiRestrication("Yes");
					} else {
						previousclaimDto.setSiRestrication("No");
					}

					previous.add(previousclaimDto);
					previousclaimDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return previous;
	}

	public List<PreviousClaimsTableDTO> getRenewedPolicyPreviousClaims(
			Long claimKey, Long policyKey, Long insuredKey, String searchType) {

		List<PreviousClaimsTableDTO> previous = new ArrayList<PreviousClaimsTableDTO>();

		final String PREVIOUS_CLAIM_PROC = "{call PRC_RPT_RENEWAL_POLICY (?, ?, ?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PREVIOUS_CLAIM_PROC);
			cs.setLong(1, claimKey);
			cs.setLong(2, policyKey);
			cs.setLong(3, insuredKey);
			cs.setString(4, searchType);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(5);

			if (rs != null) {
				PreviousClaimsTableDTO previousclaimDto = null;
				while (rs.next()) {

					previousclaimDto = new PreviousClaimsTableDTO();
					previousclaimDto.setPolicyNumber(rs
							.getString("POLICY_NUMBER"));
					previousclaimDto.setPolicyYear(rs.getString("POLICY_YEAR"));
					previousclaimDto.setClaimNumber(rs
							.getString("CLAIM_NUMBER"));
					previousclaimDto.setClaimType(rs.getString("CLAIM_TYPE"));
					previousclaimDto.setIntimationNumber(rs
							.getString("INTIMATION_NUMBER"));
					previousclaimDto.setBenefits(rs.getString("BENIFITS"));
					previousclaimDto.setInsuredPatientName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setInsuredName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setParentName(rs.getString("PARENT_NAME"));
					// previousclaimDto.setPatientName(rs.getString("PATIENT_NAME"));
					previousclaimDto.setParentDOB(rs.getDate("PARENT_DOB"));
					previousclaimDto.setDiagnosis(rs.getString("DIAGNOSIS"));
					previousclaimDto.setPatientDOB(rs.getDate("PATIENT_DOB"));
					previousclaimDto.setDiagnosisForPreviousClaim(rs
							.getString("ADMISSION_DATE"));
					if (rs.getDate("ADMISSION_DATE") != null) {
						Date admissionDate = rs.getDate("ADMISSION_DATE");
						String dateWithoutTime = SHAUtils
								.getDateWithoutTime(admissionDate);
						previousclaimDto.setAdmissionDate(dateWithoutTime);
					}
					previousclaimDto.setClaimStatus(rs
							.getString("CLAIM_STATUS"));
					previousclaimDto.setClaimAmount(String.valueOf(rs
							.getDouble("CLAIM_AMOUNT")));
					previousclaimDto.setApprovedAmount(String.valueOf(rs
							.getDouble("APPROVED_AMOUNT")));
					previousclaimDto.setCopayPercentage(rs
							.getDouble("COPAY_PERCENTAGE"));
					previousclaimDto.setHospitalName(rs
							.getString("HOSPITAL_NAME"));
					previousclaimDto.setPedName(rs.getString("PED_NAME"));
					previousclaimDto.setIcdCodes(rs.getString("ICD_CODE"));
					previousclaimDto.setKey(rs.getLong("CLAIM_KEY"));
					previous.add(previousclaimDto);
					previousclaimDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return previous;

	}

	public String getLimitAmountValidationForFA(String userId,
			Long claimedAmount, String flag) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_GET_USER_LIMIT_FA(?,?,?)}");
			cs.setString(1, userId);
			cs.setLong(2, claimedAmount);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			if (cs.getString(3) != null) {
				successMsg = cs.getString(3);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public List<PendingLotBatchReportDto> getPendingBatchLotCases() {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<PendingLotBatchReportDto> pendingListDto = new ArrayList<PendingLotBatchReportDto>();
		try {
			final String LOT_BATCH_PENDING_CLAIMS = "{call PRC_APP_RPT_PAYMENT_PENDING(?)}";
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(LOT_BATCH_PENDING_CLAIMS);
			cs.registerOutParameter(1, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(1);

			if (null != rset) {
				PendingLotBatchReportDto pendingDto = null;
				int slno = 0;
				while (rset.next()) {

					pendingDto = new PendingLotBatchReportDto();
					pendingDto.setSno(++slno);
					pendingDto.setIntimationNo(rset.getString("Intimation NO"));
					pendingDto.setClaimNo(rset.getString("Claim No"));
					pendingDto.setPolicyNo(rset.getString("Policy No"));
					pendingDto.setProduct(rset.getString("Product"));
					pendingDto.setClaimType(rset.getString("Type Of Claim"));
					pendingDto.setCPUCode(String.valueOf(rset
							.getLong("CPU Code")));
					pendingDto.setCPUName(rset.getString("CPU Name"));
					pendingDto.setPaymentMode(rset.getString("Payment Mode"));
					pendingDto.setLotNo(rset.getString("Lot No"));
					pendingDto.setPayeeName(rset.getString("Payee Name"));
					pendingDto.setBankName(rset.getString("Bank Name"));
					pendingDto.setPaymentType(rset.getString("Payment Type"));
					pendingDto.setIFSCCode(rset.getString("IFSC Code"));
					pendingDto.setBenefiAccNo(rset
							.getString("Beneficiary Acnt No"));
					pendingDto.setBranchName(rset.getString("Branch Name"));
					pendingDto.setPayableAt(rset.getString("Payable At"));
					pendingDto.setPanNo(rset.getString("Pan No"));
					pendingDto.setApprovedAmt(rset.getDouble("Approved Amt"));
					pendingDto.setProviderCode(rset.getString("Provider Code"));
					pendingDto.setPaymentReqDate(rset
							.getString("Payment Req Date"));
					pendingDto.setPaymentReqUID(rset
							.getString("Payment Req UID"));
					pendingDto.setEmailID(rset.getString("Email ID"));
					pendingDto.setStatus(rset.getString("Status"));
					pendingListDto.add(pendingDto);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pendingListDto;
	}

	public List<String> getGMCEmployeeName(Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		List<String> employeeName = new ArrayList<String>();
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GMC_PRO_EMPLOYEE_NAMES(?,?)}");
			cs.setLong(1, intimationKey);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);

			if (null != rset) {
				while (rset.next()) {
					String proposerName = rset.getString("PROPOSER_NAME");
					String employee = rset.getString("EMPLOYEE_NAME");
					employeeName.add(proposerName);
					employeeName.add(employee);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return employeeName;
	}

	public List<PreviousAccountDetailsDTO> getPreviousAccountDetails(
			Long insuredNumber, Long documentReceivedId) {

		List<PreviousAccountDetailsDTO> previous = new ArrayList<PreviousAccountDetailsDTO>();

		final String PREVIOUS_ACC_DETAIL_PROC = "{call PRC_GMC_HOS_DTLS_NEW(?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PREVIOUS_ACC_DETAIL_PROC);
			cs.setLong(1, insuredNumber);
			cs.setLong(2, documentReceivedId);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				PreviousAccountDetailsDTO previousAcntDetailsDto = null;
				while (rs.next()) {

					previousAcntDetailsDto = new PreviousAccountDetailsDTO();
					previousAcntDetailsDto.setSource("Claim");
					previousAcntDetailsDto.setPolicyClaimNo(rs
							.getString("POLICY_NUMBER"));
					previousAcntDetailsDto.setReceiptDate(rs
							.getDate("RECEIPT_DATE"));
					/*
					 * if(rs.getDate("RECEIPT_DATE") != null){ Date
					 * admissionDate = rs.getDate("RECEIPT_DATE"); String
					 * dateWithoutTime =
					 * SHAUtils.getDateWithoutTime(admissionDate);
					 * previousAcntDetailsDto
					 * .setReceiptDateValue(dateWithoutTime); }
					 */
					previousAcntDetailsDto.setBankAccountNo(rs
							.getString("ACCOUNT_NUMBER"));
					previousAcntDetailsDto.setBankName(rs
							.getString("BANK_NAME"));
					previousAcntDetailsDto.setBankCity(rs
							.getString("CITY_NAME"));
					previousAcntDetailsDto.setBankBranch(rs
							.getString("BRANCH_NAME"));
					previousAcntDetailsDto.setAccountType(rs
							.getString("ACCOUNT_TYPE"));
					previousAcntDetailsDto.setIfsccode(rs
							.getString("IFSC_CODE"));
					previousAcntDetailsDto.setIntimationNumber(rs
							.getString("INTIMATION_NUMBER"));
					previousAcntDetailsDto.setRodNumber(rs
							.getString("ROD_NUMBER"));
					previous.add(previousAcntDetailsDto);
					previousAcntDetailsDto = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return previous;
	}
	
	public String getNEFTDetailsAvailableFlag(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_NEFT_DETAILS(?, ?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(2) != null) {
				output = (String) cs.getObject(2).toString();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}


	public List<InvesAndQueryAndFvrParallelFlowTableDTO> getParallelFlowFvrInvsQuery(
			Long rodKey, String currentQ, Long intimationKey, Long cashlessKey,
			Long docRecFromId) {

		List<InvesAndQueryAndFvrParallelFlowTableDTO> parallelFlowList = new ArrayList<InvesAndQueryAndFvrParallelFlowTableDTO>();

		final String PARALLEL_FLOW_CPCR_PROC = "{call PRC_PARALLEL_FLOW_CPCR(?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PARALLEL_FLOW_CPCR_PROC);
			cs.setLong(1, rodKey);
			cs.setString(2, currentQ);
			cs.setLong(3, intimationKey);
			cs.setLong(4, cashlessKey);
			cs.setLong(5, docRecFromId);
			cs.registerOutParameter(6, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(6);
			rs1 = (ResultSet) cs.getObject(7);
			Map<Long, Object> wkflwList = new HashMap<Long, Object>();
			if (rs1 != null) {
				Map<String, Object> mappedValues = null;
				while (rs1.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs1);
					Long wkFlwKey = (Long) mappedValues
							.get(SHAConstants.WK_KEY);
					wkflwList.put(wkFlwKey, mappedValues);

				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

			if (rs != null) {
				InvesAndQueryAndFvrParallelFlowTableDTO parallelFlowDto = null;
				while (rs.next()) {

					parallelFlowDto = new InvesAndQueryAndFvrParallelFlowTableDTO();
					parallelFlowDto.setType(rs.getString("TYPE"));
					if (rs.getDate("INITIATED_DATE") != null) {
						Date initiatedDate = rs.getDate("INITIATED_DATE");
						String dateWithoutTime = SHAUtils
								.getDateWithoutTime(initiatedDate);
						parallelFlowDto.setInitiatedDate(dateWithoutTime);
					}
					parallelFlowDto.setRemarks(rs.getString("REMARKS"));
					parallelFlowDto.setStatus(rs.getString("STATUS"));
					parallelFlowDto.setWorkFlowKey(rs.getLong("WK_KEY"));
					Object object = wkflwList.get(rs.getLong("WK_KEY"));
					parallelFlowDto.setDbOutArray(object);
					parallelFlowList.add(parallelFlowDto);
					parallelFlowDto = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return parallelFlowList;
	}

	public String getCPULimitForUser(String code, String amount) {
		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GET_CPU_ALLOCATION_LIMIT(?,?,?)}");
			cs.setString(1, code);
			cs.setString(2, amount);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			if (cs.getString(3) != null) {
				successMsg = cs.getString(3);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	// lumen
	public String lumenSubmitTaskProcedure(Object[] input) {
		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		String successMsg = null;
		try {
			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_LUMEN_SUBMIT_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_LUMEN_SUBMIT_TASK (?,?)}");
			cs.setArray(1, arrayToPass);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			successMsg = (cs.getString(2) != null) ? cs.getString(2) : "";
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}

	public List<LumenRequestDTO> getTaskProcedureLumen(Object[] input) {
		List<LumenRequestDTO> listOfDTOs = new ArrayList<LumenRequestDTO>();
		LumenRequestDTO tableDTO = null;
		Connection connection = null;
		OracleConnection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		try {
			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_LUMEN_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_LUMEN_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			if (rs != null) {
				while (rs.next()) {
					tableDTO = new LumenRequestDTO();
					tableDTO.setWorkFlowKey(rs.getLong(SHAConstants.WK_KEY));
					tableDTO.setIntimationNumber(rs
							.getString(SHAConstants.INTIMATION_NO));
					tableDTO.setCpuCode(rs.getString(SHAConstants.CPU_CODE));
					tableDTO.setPolicyNumber(rs
							.getString(SHAConstants.POLICY_NUMBER));
					tableDTO.setProductName(rs
							.getString(SHAConstants.PRODUCT_NAME));
					tableDTO.setInsuredPatientName(rs
							.getString(SHAConstants.DOCUMENT_RECEVIED_FROM));
					tableDTO.setClaimType(rs.getString(SHAConstants.CLAIM_TYPE));
					tableDTO.setInitiatedScreen(rs
							.getString(SHAConstants.INT_SOURCE));
					tableDTO.setInitiatedBy(rs
							.getString(SHAConstants.REIMB_REQ_BY)
							+ " - "
							+ rs.getString(SHAConstants.ROD_REFER_BY)); // Emp
																		// ID -
																		// EMp
																		// Name
					tableDTO.setInitiatedDate(rs
							.getTimestamp(SHAConstants.ROD_CREATED_DATE));
					tableDTO.setLumenStatus(rs
							.getString(SHAConstants.PREAUTH_STATUS));
					tableDTO.setLumenRequestKey(rs
							.getLong(SHAConstants.FVR_KEY));
					tableDTO.setOutcome(rs.getString(SHAConstants.OUTCOME));
					tableDTO.setMisQueryRaisedFrom(rs
							.getString(SHAConstants.STAGE_SOURCE));
					tableDTO.setTotalRecords(cs.getLong(3));
					listOfDTOs.add(tableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfDTOs;
	}

	public List<OMPClaimProcessorDTO> getdeductiblevalues(Long productkey,
			String eventCode, Double sumInsured) {
		List<OMPClaimProcessorDTO> queryList = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			System.out.println("----inside getAlertData Method---");
			if (null != connection) {

				// String fetchQuery =
				// "SELECT EVENT_CODE , DEDUCTIBLE,DEDCUTION_TYPE FROM MAS_OMP_DEDUCTIBLES WHERE "
				// + " PRODUCT_ID = ? " + " AND PLAN =?";
				String fetchQuery = ""
						+ "SELECT DISTINCT EVENT_CODE , DEDUCTIBLE,DEDCUTION_TYPE "
						+ "FROM MAS_OMP_DEDUCTIBLES WHERE "
						+ "PRODUCT_ID =? and  LOWER(PLAN) = LOWER(?) AND EVENT_CODE is not null and ACTIVE_STATUS='Y' and "
						+ "SI =?";

				System.out
						.println("----Select Query for Query reply received ---->"
								+ fetchQuery);
				// eventCode = "''" + eventCode + "''";
				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, productkey);
				preparedStatement.setString(2, eventCode);
				preparedStatement.setDouble(3, sumInsured);
				if (null != preparedStatement) {
					queryList = new ArrayList<OMPClaimProcessorDTO>();
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							OMPClaimProcessorDTO queryReplyDTO = new OMPClaimProcessorDTO();
							queryReplyDTO.setDedutEventCode(rs.getString(1));
							// queryReplyDTO.setEventdescription(rs.getString(2));
							queryReplyDTO.setDeductibles(rs.getDouble(2));
							queryReplyDTO.setDescription(rs.getString(3));
							queryList.add(queryReplyDTO);

						}

					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryList;
	}

	public BeanItemContainer<SelectValue> getParticipantsFromProcedure(
			String argIntimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> empIdContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_INTM_PROC_USR (?,?)}");
			cs.setString(1, argIntimationNumber);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			if (rs != null) {
				SelectValue select = null;
				while (rs.next()) {
					select = new SelectValue();
					select.setValue(rs.getString("EMP_ID") + " - "
							+ rs.getString("EMP_NAME"));
					selectValuesList.add(select);
				}
			}
			empIdContainer.addAll(selectValuesList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return empIdContainer;
	}

	public Map<String, Double> getBalanceSIForReimbursementStarCancerGold(
			Long policyKey, Long insuredKey, Long claimKey, Long rodKey,
			String sobCoverCode) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		System.out.println("Start getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_BALANCE_SI_CC_GOLD (?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);
			cs.setString(5, sobCoverCode);

			cs.registerOutParameter(6, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(7, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();
			System.out.println("Middle getBalanceSIForReimbursement Procedure "
					+ System.currentTimeMillis());

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(6);
			currentBalanceSI = (Double) cs.getObject(7);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI);
		values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI);
		System.out.println("End getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		return values;
	}

	public List<OMPPaymentDetailsTableDTO> getpayeeNameDetails(Long paymentId) {
		List<OMPPaymentDetailsTableDTO> queryList = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			System.out.println("----inside getAlertData Method---");
			if (null != connection) {

				String fetchQuery = "SELECT NAME , EMAIL_ID FROM MAS_OMP_PAYMENT_TO WHERE "
						+ " PAYMENT_TO_ID = ? ";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, paymentId);
				// preparedStatement.setString(2,eventCode);
				System.out
						.println("----Select Query for Payee Name Email ---->"
								+ fetchQuery);

				if (null != preparedStatement) {
					queryList = new ArrayList<OMPPaymentDetailsTableDTO>();
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							OMPPaymentDetailsTableDTO queryReplyDTO = new OMPPaymentDetailsTableDTO();
							queryReplyDTO.setPayeeNameStr(rs.getString(1));
							// queryReplyDTO.setEventdescription(rs.getString(2));
							// queryReplyDTO.setDescription(rs.getString(3));
							queryReplyDTO.setEmailId(rs.getString(2));
							queryList.add(queryReplyDTO);

						}

					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println("===stageList===" + queryList);
		return queryList;
		// return new Double("0");

	}

	public Map<String, Double> getBalanceSIForStarCancerGold(Long policyKey,
			Long insuredKey, Long claimKey, Double sumInsured,
			Long intimationKey, String sobCoverCode) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BALANCE_SI_CC_GOLD(?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);

			if (sumInsured != null) {
				cs.setDouble(5, sumInsured);
			} else {
				cs.setDouble(5, 0);
			}

			cs.setString(6, sobCoverCode);

			cs.registerOutParameter(7, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(8, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(7);
			currentBalanceSI = (Double) cs.getObject(8);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;

	}

	public Map<String, Double> getOmpBalanceSIByPolicyKey(Long policyKey,
			String eventCode) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OMP_POLICY_BALANCE_SI (?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setString(2, eventCode);

			cs.registerOutParameter(3, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(4, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(3);
			currentBalanceSI = (Double) cs.getObject(4);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI,
				totalBalanceSI != null ? totalBalanceSI : 0d);
		values.put(SHAConstants.CURRENT_BALANCE_SI,
				currentBalanceSI != null ? currentBalanceSI : 0d);
		return values;
	}

	public void invokeProcedureAutoSkipFVR(String fvrId) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_UPD_FVR_PORTAL_REQUEST(?)}");
			cs.setString(1, fvrId);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, Object>> revisedGetTaskProcedureForBatch(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start TYP_SEC_CSH_ROD_GET_TASK For Batch "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_STARFAX_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public List<UpdatePaymentDetailTableDTO> getPaymentDetailProcedure(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		List<UpdatePaymentDetailTableDTO> list = null;

		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_PAYMENT_INFO", conn);
			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_GET_PAYMENT_INFO (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				list = new ArrayList<UpdatePaymentDetailTableDTO>();
				while (rs.next()) {

					UpdatePaymentDetailTableDTO detailTableDTO = new UpdatePaymentDetailTableDTO();
					detailTableDTO.setIntimationNo((String) rs
							.getString(SHAConstants.INTIMATION_NO));
					detailTableDTO.setApprovedAmt((String) rs
							.getString(SHAConstants.APPROVED_AMT));
					detailTableDTO.setCpucode((String) rs
							.getString(SHAConstants.CPU_CODE));
					detailTableDTO.setCpuName((String) rs
							.getString(SHAConstants.CPU_NAME));
					detailTableDTO.setEmailID((String) rs
							.getString(SHAConstants.EMAIL_ID));
					detailTableDTO.setEmployeeName((String) rs
							.getString(SHAConstants.EMPLOYEE_NAME));
					detailTableDTO.setPaymentTypeValue((String) rs
							.getString(SHAConstants.PAYMENT_TYPE));
					if (detailTableDTO.getPaymentTypeValue() != null) {
						if (detailTableDTO.getPaymentTypeValue()
								.equalsIgnoreCase("BANK TRANSFER")) {
							detailTableDTO.setIfscCode((String) rs
									.getString(SHAConstants.IFSC_CODE));
							detailTableDTO.setBeneficiaryAcno((String) rs
									.getString(SHAConstants.ACCOUNT_NUMBER));
							detailTableDTO.setBankName((String) rs
									.getString(SHAConstants.BANK_NAME));
							detailTableDTO.setBranchCity((String) rs
									.getString(SHAConstants.BANK_CITY));
							detailTableDTO.setBranchName((String) rs
									.getString(SHAConstants.BRANCH_NAME));
						}
					}
					detailTableDTO.setMaApprovedDate((String) rs
							.getString(SHAConstants.MA_APPROVED_DATE));
					detailTableDTO.setPayableCity((String) rs
							.getString(SHAConstants.PAYABLE_CITY));
					detailTableDTO.setPayeeNameString((String) rs
							.getString(SHAConstants.PAYEE_NAME));
					detailTableDTO.setPaymentCpuCodeString((String) rs
							.getString(SHAConstants.PAYMENT_CPU_CODE));
					detailTableDTO.setProductName((String) rs
							.getString(SHAConstants.PRODUCT_NAME));
					detailTableDTO.setProposerName((String) rs
							.getString(SHAConstants.PROPOSER_NAME));
					detailTableDTO.setReasonForChange((String) rs
							.getString(SHAConstants.REASON_FOR_CHANGE));
					detailTableDTO.setTypeOfClaim((String) rs
							.getString(SHAConstants.CLAIM_TYPE));
					detailTableDTO.setRodKey((Long) rs
							.getLong(SHAConstants.REIMBURSEMENT_KEY));

					detailTableDTO.setTotalCount(totalCount);
					detailTableDTO.setSerialNumber(list.size() + 1);
					detailTableDTO.setIntimation_key((Long) rs
							.getLong(SHAConstants.INTIMATION_KEY));
					detailTableDTO.setPolicy_key((Long) rs
							.getLong(SHAConstants.DB_POLICY_KEY));

					BeanItemContainer<SelectValue> payeeName = getPayeeName(
							detailTableDTO.getPolicy_key(),
							detailTableDTO.getIntimation_key());
					detailTableDTO.setPayeeNameList(payeeName);
					// detailTableDTO.setPaymentKey((Long)rs.getLong(SHAConstants.PAYMENT_KEY));
					list.add(detailTableDTO);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public List<SearchFvrReportGradingTableDto> getFVRGradingList(
			String intimationNo, String policyNo, Long cpu, Long claimId,
			java.sql.Date fromDate, java.sql.Date toDate) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SearchFvrReportGradingTableDto> list = new ArrayList<SearchFvrReportGradingTableDto>();

		try {
			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GET_FVR_GRADING_PENDING(?,?,?,?,?,?,?)}");
			cs.setString(1, intimationNo != null ? intimationNo : "");
			cs.setString(2, policyNo != null ? policyNo : "");
			cs.setLong(3, cpu != null ? cpu : 0L);
			cs.setLong(4, claimId != null ? claimId : 0L);
			cs.setDate(5, fromDate);
			cs.setDate(6, toDate);
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(7);

			if (rs != null) {
				SearchFvrReportGradingTableDto dto = null;
				while (rs.next()) {
					dto = new SearchFvrReportGradingTableDto();
					dto.setClaimKey(rs.getLong("CLAIM_KEY"));
					dto.setFvrKey(rs.getLong("fvr_key"));
					dto.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
					dto.setPolicyNo(rs.getString("POLICY_NUMBER"));
					dto.setInsuredPatientName(rs.getString("INSURED_NAME"));
					dto.setHospitalName(rs.getString("HOSPITAL_NAME"));
					dto.setHospCity(rs.getString("HOSPITAL_CITY"));
					dto.setLob(rs.getString("LOB"));
					dto.setCpuCode(String.valueOf(rs.getLong("FVR_CPU_ID")));
					dto.setProduct(rs.getString("PRODUCT_NAME"));
					dto.setAdmissionReason(rs.getString("ADMISSION_REASON"));
					dto.setRodKey(rs.getLong("ROD_KEY"));
					list.add(dto);
					dto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public BeanItemContainer<SelectValue> getPayeeName(Long policykey,
			Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		List<String> employeeName = new ArrayList<String>();
		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_PAYYEE_NAMES(?,?,?)}");
			cs.setLong(1, policykey);
			cs.setLong(2, intimationKey);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rset = (ResultSet) cs.getObject(3);

			if (null != rset) {
				while (rset.next()) {
					String proposerName = rset.getString("PROPOSER_NAME");
					String employee = rset.getString("EMPLOYEE_NAME");
					String hospitalName = rset.getString("HOSPITAL_NAME");
					employeeName.add(proposerName);
					employeeName.add(employee);

					SelectValue proposerSelValue = new SelectValue();
					proposerSelValue.setId(1l);
					proposerSelValue.setValue(proposerName);
					SelectValue employeeSelValue = new SelectValue();
					employeeSelValue.setId(2l);
					employeeSelValue.setValue(employee);
					SelectValue hospitalSelValue = new SelectValue();
					hospitalSelValue.setId(3l);
					hospitalSelValue.setValue(hospitalName);
					selectValuesList.add(proposerSelValue);
					selectValuesList.add(employeeSelValue);
					selectValuesList.add(hospitalSelValue);

				}
				categoryContainer.addAll(selectValuesList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return categoryContainer;
	}

	public void callInsertPolicyForGMC() {

		Connection connection = null;
		CallableStatement cs = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GMC_CLAIM_COLOR_RATIO_INS()}");
			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void calculateGmcClaimRatio() {

		Connection connection = null;
		CallableStatement cs = null;
		try {
			System.out
					.println("@@@@@@@@@ ICR BATCH PRC_GMC_CLAIM_COLOR_RATIO STARTED @@@@@@@@@@----->");
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_GMC_CLAIM_COLOR_RATIO()}");
			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out
				.println("@@@@@@@@@ ICR BATCH PRC_GMC_CLAIM_COLOR_RATIO ENDED @@@@@@@@@@----->");
	}

	@SuppressWarnings("rawtypes")
	public List<ComprehensiveHospitalisationTableDTO> getComprehensiveSIForClassicGroup(
			Long insuredKey, String intimationNo, Double sumInsured,
			Long prodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		List<ComprehensiveHospitalisationTableDTO> optionalCoverstableList = new ArrayList<ComprehensiveHospitalisationTableDTO>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PREV_CLAIM_58(?, ?, ?, ?, ?, ?,?)}");
			cs.setLong(1, prodKey);
			cs.setLong(2, insuredKey);
			cs.setString(3, intimationNo);
			cs.setDouble(4, sumInsured);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_RES_OUTSTD_AMT");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_RES_PAID_AMT");
			cs.registerOutParameter(7, Types.ARRAY, "TYP_SI_LIST_58");
			cs.execute();

			Object[] data = (Object[]) ((Array) cs.getObject(7)).getArray();

			Struct row = null;
			Object[] attributes = null;
			for (Object object : data) {

				row = (Struct) object;
				attributes = row.getAttributes();
				if (attributes != null) {
					String optionalCoverName = (String) attributes[0];
					Double policyLimitAmt = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[1]));
					Double claimsPaid = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[2]));
					Double outStanding = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[3]));
					Double balanceSumInsured = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[4]));
					Double currentProvisionAmt = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[5]));
					Double currentBalSI = SHAUtils
							.getDoubleValueFromString(String
									.valueOf(attributes[6]));

					ComprehensiveHospitalisationTableDTO optionalCoversTableDto = new ComprehensiveHospitalisationTableDTO();

					optionalCoversTableDto
							.setOptionalcoverName(optionalCoverName);
					optionalCoversTableDto.setPolicyLevelLimit(policyLimitAmt);
					optionalCoversTableDto.setClaimPaid(claimsPaid);
					optionalCoversTableDto.setClaimOutStanding(outStanding);
					optionalCoversTableDto.setBalance(balanceSumInsured);
					optionalCoversTableDto
							.setProvisionCurrentClaim(currentProvisionAmt);
					optionalCoversTableDto.setBalanceSI(currentBalSI);

					optionalCoverstableList.add(optionalCoversTableDto);

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return optionalCoverstableList;

	}

	public Boolean getPOPUPMessagesForProduct(Long policyKey, Long insuredKey,
			Long productKey) {
		final String insuredChange = "LV_INSURED_CNT_CNG_CHK";
		final String siChange = "LV_SI_CNG_CHK";
		final String portability = "LV_PORTABLITY_CHK";
		final String breakInsurance = "LV_BRK_INSURANCE_CHK";
		Boolean isChangeInSumInsured = false;
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_POPUP_MSG_FLAGS(?, ?, ?, ?, ?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.registerOutParameter(3, Types.INTEGER, insuredChange);
			cs.registerOutParameter(4, Types.INTEGER, siChange);
			cs.registerOutParameter(5, Types.INTEGER, portability);
			cs.registerOutParameter(6, Types.INTEGER, breakInsurance);

			cs.execute();

			if (cs.getObject(4) != null) {
				if (SHAUtils.getIntegerFromStringWithComma(
						cs.getObject(4).toString()).equals(1)) {

					if (ReferenceTable.getHealthGainProducts().containsKey(
							productKey)) {

						isChangeInSumInsured = true;
					}
				}
			}
			// return popupMap;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isChangeInSumInsured;
	}

	public List<CreateAndSearchLotTableDTO> getCpuWiseCountForBatch(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<CreateAndSearchLotTableDTO> cpucodeList = new ArrayList<CreateAndSearchLotTableDTO>();

		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_GET_PAYMENT_CPU", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_GET_PAYMENT_CPU (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "INT");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			Long cashlessTotalCount = 0l;
			Long reimbursementTotalCount = 0l;
			if (rs != null) {
				while (rs.next()) {
					CreateAndSearchLotTableDTO dto = new CreateAndSearchLotTableDTO();
					String cpu = rs.getString("CPU");
					Long count = rs.getString("COUNT") != null ? Long
							.valueOf(rs.getString("COUNT")) : 0l;
					Long cashlessCount = rs.getString("CASHLESS") != null ? Long
							.valueOf(rs.getString("CASHLESS")) : 0l;
					cashlessTotalCount += cashlessCount;
					Long reimbursementCount = rs.getString("REIMBURSEMENT") != null ? Long
							.valueOf(rs.getString("REIMBURSEMENT")) : 0l;
					reimbursementTotalCount += reimbursementCount;
					dto.setCpuCode(cpu);
					dto.setCpuCodeCount(count);
					dto.setTotalCpuCodeCount(totalCount);
					dto.setIsRecordExceed(true);
					dto.setCashlessCount(cashlessCount);
					dto.setReimbursementCount(reimbursementCount);
					cpucodeList.add(dto);
				}

				if (cpucodeList != null && !cpucodeList.isEmpty()) {
					CreateAndSearchLotTableDTO createAndSearchLotTableDTO = cpucodeList
							.get(0);
					createAndSearchLotTableDTO
							.setCashlessTotalCount(cashlessTotalCount);
					createAndSearchLotTableDTO
							.setReimbursementTotalCount(reimbursementTotalCount);
				}

				// return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cpucodeList;

	}

	public List<CreateAndSearchLotTableDTO> getCpuWiseCountForLot(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<CreateAndSearchLotTableDTO> cpucodeList = new ArrayList<CreateAndSearchLotTableDTO>();

		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_PAYMENT_STATUS", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_GET_PAYMENT_STATUS (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "INT");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			Long cashlessTotalCount = 0l;
			Long reimbursementTotalCount = 0l;
			if (rs != null) {
				while (rs.next()) {
					CreateAndSearchLotTableDTO dto = new CreateAndSearchLotTableDTO();
					String cpu = rs.getString("CPU");
					Long count = rs.getString("COUNT") != null ? Long
							.valueOf(rs.getString("COUNT")) : 0l;
					Long cashlessCount = rs.getString("CASHLESS") != null ? Long
							.valueOf(rs.getString("CASHLESS")) : 0l;
					cashlessTotalCount += cashlessCount;
					Long reimbursementCount = rs.getString("REIMBURSEMENT") != null ? Long
							.valueOf(rs.getString("REIMBURSEMENT")) : 0l;
					reimbursementTotalCount += reimbursementCount;
					dto.setCpuCode(cpu);
					dto.setCpuCodeCount(count);
					dto.setTotalCpuCodeCount(totalCount);
					dto.setIsRecordExceed(true);
					dto.setCashlessCount(cashlessCount);
					dto.setReimbursementCount(reimbursementCount);
					cpucodeList.add(dto);
				}
				if (cpucodeList != null && !cpucodeList.isEmpty()) {
					CreateAndSearchLotTableDTO createAndSearchLotTableDTO = cpucodeList
							.get(0);
					createAndSearchLotTableDTO
							.setCashlessTotalCount(cashlessTotalCount);
					createAndSearchLotTableDTO
							.setReimbursementTotalCount(reimbursementTotalCount);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cpucodeList;

	}

	public List<Double> getJetPrivilegeProductCoPayBasedOnPedExclusion(
			Long productKey, Long insuredKey, Long insuredNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		List<Double> copayValues = new ArrayList<Double>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_POLICY_COPAY(?, ?, ?, ?)}");
			cs.setLong(1, productKey);
			cs.setLong(2, insuredKey);
			// Result is an java.sql.Array...
			cs.registerOutParameter(3, Types.ARRAY, "TYP_PRODUCT_COPAY");
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;
				Object[] attributes = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();
					for (Object object2 : attributes) {
						Double doubleValueFromString = SHAUtils
								.getDoubleValueFromString(String
										.valueOf(object2));
						copayValues.add(doubleValueFromString);
					}
				}
			}

			Boolean isHavingZero = false;
			for (Double double1 : copayValues) {
				if (double1.equals(0d)) {
					isHavingZero = true;
					break;
				}
			}

			if (!isHavingZero) {
				copayValues.add(0, 0d);
			}
			// return copayValues;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return copayValues;
	}

	public BeanItemContainer<SelectValue> getSiRestricationValueForHealthGain(
			Long policykey, Long insuredkey, MasterService masterService) {

		Connection connection = null;
		CallableStatement cs = null;
		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_PREV_SUMINSURED_CHANGE1(?,?)}");
			// cs.setLong(1, policykey);
			cs.setLong(1, insuredkey);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);
			if (null != rset) {
				while (rset.next()) {
					Long sumInsured = rset.getLong("SI");
					// String inceptionDate =
					// rset.getString("POLICY_START_DATE");
					MastersValue master = masterService.getMaster(sumInsured);
					if (master != null /* && inceptionDate != null */) {
						// String value = inceptionDate + " - "+sumInsured;
						String value = sumInsured.toString();
						selectValue = new SelectValue(master.getKey(),
								sumInsured.toString());
						selectValue.setCommonValue(value);
						selectValuesList.add(selectValue);
					}
				}
				categoryContainer.addAll(selectValuesList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return categoryContainer;
	}

	public void callTouchLessProcedure(Long cashlessKey) {
		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_DECISION_MAKING_AUTOMATION(?)}");
			cs.setLong(1, cashlessKey);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getUserZoneDetails(String userId) {
		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_KERALA_ZONE_USER (?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				successMsg = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}

	public void workFlowEndCallProcedure(Long wk_key, String intimationNo) {

		Connection connection = null;
		CallableStatement cs = null;
		try {

			connection = BPMClientContext.getConnection();
			// connection = getConnectionFromURL();

			cs = connection.prepareCall("{call PRC_PARALLEL_WFINCPCR (?,?)}");
			cs.setLong(1, wk_key);
			cs.setString(2, intimationNo);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<SearchLumenStatusWiseDto> getLumenStatusWiseReport(
			java.sql.Date fromDate, java.sql.Date toDate, String statusList,
			String cpuList, String userId, Long clmType) {

		final String LUMEN_STATUS_REPORT = "{call  PRC_RPT_LUMEN_STATUS_WISE(?,?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<SearchLumenStatusWiseDto> lumenStatusResultList = new ArrayList<SearchLumenStatusWiseDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(LUMEN_STATUS_REPORT);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setString(3,
					statusList != null && !statusList.isEmpty() ? statusList
							: "0");
			cs.setString(4, cpuList != null && !cpuList.isEmpty() ? cpuList
					: "0");
			cs.setInt(5, null != clmType ? clmType.intValue() : 0);
			cs.setString(6, userId);

			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(7);

			if (null != rset) {
				SearchLumenStatusWiseDto lumenStatDto = null;

				while (rset.next()) {

					lumenStatDto = new SearchLumenStatusWiseDto();

					lumenStatDto.setDate(rset.getString("DATE"));
					lumenStatDto.setClmNo(rset.getString("CLAIM_NUMBER"));
					lumenStatDto.setDoi(rset.getString("DOI"));
					lumenStatDto.setPolicyYr(rset.getString("POLICY_YEAR"));
					lumenStatDto.setProductType(rset.getString("PRODUCT_TYPE"));
					lumenStatDto.setPolicyNo(rset.getString("POLICY_NUMBER"));
					lumenStatDto.setPolicyIssueOffice(rset
							.getString("POLICY_ISSUE_OFFICE"));
					lumenStatDto.setAgentCode(rset.getString("AGENT_CODE"));
					lumenStatDto.setSuminsured(rset.getLong("SUM_INSURED"));
					lumenStatDto.setDoa(rset.getString("DOA"));
					lumenStatDto.setDod(rset.getString("DOD"));
					lumenStatDto.setHospitalName(rset.getString("HOSPITAL"));
					lumenStatDto.setAilment(rset.getString("AILMENT"));
					lumenStatDto.setClaimedAmount(rset
							.getLong("CLAIMED_AMOUNT"));
					lumenStatDto.setDescreption(rset.getString("DESCREPTION"));
					lumenStatDto.setTypeofError(rset.getString("ERROR_TYPE"));
					lumenStatDto.setClaimHistory(rset
							.getString("CLAIM_HISTORY"));
					lumenStatDto.setLapse1(rset.getString("LAPSE"));
					// setLapse2(rset.getString("lapse2"));
					// setLapse3(rset.getString("lapse3"));
					// setLapse4(rset.getString("lapse4"));
					// setLapse5(rset.getString("lapse5"));
					lumenStatDto.setCpu(rset.getString("CPU"));
					lumenStatDto.setStatus(rset.getString("STATUS"));

					lumenStatusResultList.add(lumenStatDto);
					lumenStatDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lumenStatusResultList;

	}

	public String isClaimManuallyProcessed(String healthCardNo) {

		Connection connection = null;
		CallableStatement cs = null;
		String isClaimManuallyProcess = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_PAAYAS_HEALTH_CARD_ALERT(?,?)}");
			cs.setString(1, healthCardNo);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				isClaimManuallyProcess = cs.getString(2);
			}

			// return successMsg;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isClaimManuallyProcess;
	}

	// CR2017275
	public Unique64VbstatusDto getUnique64Details(String intimationNo) {
		Unique64VbstatusDto dto = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "select a.ADMISSION_DATE,b.POLICY_START_DATE,b.POLICY_END_DATE from IMS_CLS_INTIMATION a inner join IMS_CLS_POLICY b on a.POLICY_KEY = b.POLICY_KEY and a.INTIMATION_NUMBER=? and b.PRODUCT_ID in (?)";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, intimationNo);
				preparedStatement.setLong(2,
						ReferenceTable.STAR_UNIQUE_PRODUCT_KEY);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							dto = new Unique64VbstatusDto();
							dto.setAdmissionDate(rs.getTimestamp(1));
							dto.setPolicyFromDate(rs.getTimestamp(2));
							dto.setPolicyToDate(rs.getTimestamp(3));
						}

					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dto;

	}
	
	

	public List<AckWithoutRodTableDto> getAckWithoutRodReport(
			java.sql.Date fromDate, java.sql.Date toDate, Long cpuKey,
			Long docFromKey, String userId) {

		final String ACK_WITHOUT_ROD_REPORT = "{call  PRC_RPT_ACK_ROD_CREATION(?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<AckWithoutRodTableDto> ackWithoutRodResultList = new ArrayList<AckWithoutRodTableDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(ACK_WITHOUT_ROD_REPORT);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setString(
					3,
					cpuKey != null && cpuKey.intValue() != 0 ? cpuKey
							.toString() : "0");
			cs.setInt(4, docFromKey != null ? docFromKey.intValue() : 0);
			cs.setString(5, userId);

			cs.registerOutParameter(6, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(6);

			if (null != rset) {
				AckWithoutRodTableDto ackwithoutRodDto = null;

				while (rset.next()) {

					ackwithoutRodDto = new AckWithoutRodTableDto();
					ackwithoutRodDto.setIntimationNumber(rset
							.getString("INTIMATION_NUMBER"));
					ackwithoutRodDto.setBillRcvdDate(rset
							.getString("BILL_RECEIVED_DATE"));
					ackwithoutRodDto.setAckDate(rset
							.getString("ACKNOWLEDGEMENT_DATE"));
					ackwithoutRodDto.setDocRecdFrm(rset
							.getString("DOCUMENT_RECEIVED_FROM"));
					ackwithoutRodDto.setCpuCode(rset.getString("CPU_CODE"));
					ackwithoutRodDto.setCpu(rset.getString("CPU"));
					ackwithoutRodDto.setClaimType(rset.getString("CLAIM_TYPE"));
					ackwithoutRodDto.setProductCode(rset
							.getString("PRODUCT_CODE"));
					ackwithoutRodDto.setProductName(rset
							.getString("PRODUCT_NAME"));
					ackwithoutRodDto.setUserName(rset.getString("USER_NAME"));
					ackwithoutRodDto.setUserId(rset.getString("USER_ID"));

					ackWithoutRodResultList.add(ackwithoutRodDto);
					ackwithoutRodDto = null;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ackWithoutRodResultList;

			}

			public Long getCopayPercentageforJio(Long policyKey, String coPayType) {

				Connection connection = null;
				CallableStatement cs = null;
				Long coPayPercentage = 0L;
				try {

					connection = BPMClientContext.getConnection();

					cs = connection.prepareCall("{call PRC_JIO_COPAY_LIMIT(?,?,?)}");
					cs.setLong(1, policyKey);
					cs.setString(2, coPayType);
					cs.registerOutParameter(3, Types.NUMERIC);
					cs.execute();

					if (cs.getString(3) != null) {
						coPayPercentage = cs.getLong(3);
					}

					// return successMsg;

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cs != null) {
							cs.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return coPayPercentage;

			}

	/**
	 * As Part of CR R0768
	 * 
	 * @param fromDate
	 *            SQL Date
	 * @param toDate
	 *            SQL Date
	 * @param status
	 *            String
	 * @param cpuList
	 *            String
	 * @param userId
	 *            String
	 * @param clmType
	 *            Long
	 * @return
	 */
			public List<InvAssignStatusReportDto> getInvAssignStatusReport(
					java.sql.Date fromDate, java.sql.Date toDate, String status,
					String cpuList, String userId, Long clmType) {

				final String LUMEN_STATUS_REPORT = "{call  PRC_RPT_INVESTIGATION_STS(?,?,?,?,?,?,?)}";

				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rset = null;
				List<InvAssignStatusReportDto> invAssignStatusResultList = new ArrayList<InvAssignStatusReportDto>();
				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall(LUMEN_STATUS_REPORT);
					cs.setDate(1, fromDate);
					cs.setDate(2, toDate);
					cs.setString(3, status);
					cs.setInt(4, null != clmType ? clmType.intValue() : 0);
					cs.setString(5, cpuList != null && !cpuList.isEmpty() ? cpuList
							: "0");
					cs.setString(6, userId);

					cs.registerOutParameter(7, OracleTypes.CURSOR);
					cs.execute();

					rset = (ResultSet) cs.getObject(7);

					if (null != rset) {
						InvAssignStatusReportDto invStatDto = null;
						int index = 1;
						while (rset.next()) {

							invStatDto = new InvAssignStatusReportDto();

							invStatDto.setSno(index);
							invStatDto.setIntimationNumber(rset
									.getString("INTIMATION_NUMBER"));
							invStatDto.setRefRodNo(rset.getString("ROD_NUMBER"));
							invStatDto.setPatientName(rset.getString("PATIENT_NAME"));
							invStatDto.setPolicyNumber(rset.getString("POLICY_NUMBER"));
							invStatDto.setClmType(rset.getString("CLAIM_TYPE"));
							invStatDto.setCpu(rset.getString("CPU"));
							invStatDto.setHospitalName(rset.getString("HOSPITAL_NAME"));
							invStatDto.setHospitalType(rset.getString("HOSPITAL_TYPE"));
							invStatDto.setHosLocation(rset
									.getString("HOSPITAL_ADDRESS"));
							invStatDto.setAliment(rset.getString("ALIMENT_NAME"));
							invStatDto.setClaimedAmount(rset
									.getDouble("CLAIMED_AMOUNT"));
							invStatDto.setClaimStatus(rset.getString("CLAIM_STATUS"));
							invStatDto.setSaettledAmount(rset
									.getString("SETTLED_AMOUNT"));
							invStatDto.setRvo(rset.getString("RVO"));
							invStatDto.setInvApprovedDate(rset
									.getString("APPROVED_DATE"));
							invStatDto.setDraftInvLetterDate(rset
									.getString("DRAFT_DATE"));
							invStatDto.setClmReqDate(rset.getString("ASSIGNED_DATE"));

							if (status
									.equalsIgnoreCase(SHAConstants.UPLOAD_INVESTIGATION_COMPLETED_REPORT_TYPE)) {
								invStatDto.setInvRplyDate(rset
										.getString("REPORT_UPLOAD_DATE"));
								invStatDto.setInvTat(rset.getInt("VERIFICATION_DAYS"));
								invStatDto.setInvrptUploadBy(rset
										.getString("REPORT_UPLOAD_BY"));
							}

							index++;

							invAssignStatusResultList.add(invStatDto);
							invStatDto = null;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rset != null) {
							rset.close();
							rset = null;
						}
						if (cs != null) {
							cs.close();
							cs = null;
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				return invAssignStatusResultList;

			}

	public Long getProductKey(String policyNumber) {
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT PRODUCT_ID FROM IMS_CLS_POLICY WHERE POLICY_NUMBER =?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, policyNumber);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getLong(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}

	public Map<Long, Object> getCashlessInvestigations(Long investigationKey,
			Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		Map<Long, Object> wkflwList = new HashMap<Long, Object>();
		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_UPD_CASH_INV(?,?)}");
			cs.setLong(1, investigationKey);
			cs.setLong(2, rodKey);
			// cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			// rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				/*
				 * Map<String, Object> mappedValues = null; while (rs.next()) {
				 * mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
				 * mappedValues.put(SHAConstants.PAYLOAD_ROD_KEY, rodKey);
				 * Object[] inputWorkFlowArray = SHAUtils
				 * .getRevisedObjArrayForSubmit(mappedValues);
				 * revisedInitiateTaskProcedure(inputWorkFlowArray); }
				 */
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wkflwList;

	}

	public List<OmpStatusReportDto> getOmpReport(java.sql.Date fromDate,
			java.sql.Date toDate, Long classification, Long subClassification,
			Long sts, String periodofLoss, Long yearofLoss, String rptType,
			String userId) {

		final String LUMEN_STATUS_REPORT = "{call  PRC_RPT_OMP_STATUS(?,?,?,?,?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<OmpStatusReportDto> ompOutStandingResultList = new ArrayList<OmpStatusReportDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(LUMEN_STATUS_REPORT);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, classification != null ? classification : 0L);
			cs.setLong(4, subClassification != null ? subClassification : 0L);
			cs.setLong(5, sts != null ? sts : 0L);
			cs.setString(6, periodofLoss != null ? periodofLoss : null);
			cs.setLong(7, toDate.getYear()); // yearofLoss );
			cs.setString(8, rptType);

			cs.setString(9, userId);

			cs.registerOutParameter(10, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(10);

			if (null != rset) {
				ompOutStandingResultList = getOmpCommonColumnsForRpt(rset,
						rptType, sts);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ompOutStandingResultList;
	}

	private List<OmpStatusReportDto> getOmpCommonColumnsForRpt(ResultSet rset,
			String rptType, Long sts) {

		List<OmpStatusReportDto> ompOutStandingResultList = new ArrayList<OmpStatusReportDto>();
		OmpStatusReportDto OmpStatDto = null;

		try {
			while (rset.next()) {

				OmpStatDto = new OmpStatusReportDto();

				OmpStatDto.setIntimationNo(rset.getString("Intimation No"));
				OmpStatDto.setTpaIntimationNumber(rset
						.getString("TPA Intimation Number"));
				OmpStatDto.setPolicyNo(rset.getString("Policy No"));
				OmpStatDto.setPolicyFromDate(rset.getDate("Policy From Date"));
				OmpStatDto.setPolicyToDate(rset.getDate("Policy To Date"));
				OmpStatDto.setBranchOfficeCode(rset
						.getString("Branch Office Code"));
				OmpStatDto.setPolicyIssuingState(rset
						.getString("Policy Issuing State"));
				OmpStatDto.setChannelType(rset.getString("Channel Type"));
				OmpStatDto.setProductType(rset.getString("Product Type"));
				OmpStatDto.setPolicySumInsured(rset
						.getDouble("Policy Sum Insured"));
				OmpStatDto.setEventCodeSumInsured(rset
						.getDouble("Event Code Sum Insured"));
				OmpStatDto.setInsuredName(rset.getString("Insured Name"));
				OmpStatDto.setAge(rset.getInt("Age"));
				OmpStatDto.setEventCode(rset.getString("Event code"));
				OmpStatDto.setTypeofClaim(rset.getString("Type of Claim"));
				OmpStatDto.setNatureOfClaim(rset.getString("Nature Of Claim"));
				OmpStatDto.setAilmentLoss(rset.getString("Ailment/Loss"));
				OmpStatDto.setIntimationDate(rset.getDate("Intimation date"));
				OmpStatDto.setAdmissionLossDate(rset
						.getDate("Admission/Loss Date"));
				OmpStatDto.setCountry(rset.getString("Country"));
				OmpStatDto.setInitialProvisionAmount(rset
						.getDouble("Initial Provision Amount"));
				OmpStatDto.setInrConversionRate(rset
						.getDouble("INR Conversion Rate"));
				OmpStatDto.setInrValue(rset.getDouble("INR Value"));
				OmpStatDto.setPlan(rset.getString("Plan"));

				if (("o").equalsIgnoreCase(rptType)) {
					OmpStatDto.setClaimStatus(rset.getString("Claim Status"));
					OmpStatDto.setConverstionRate(rset
							.getDouble("Converstion Rate"));
					OmpStatDto.setPendingStage(rset.getString("Pending Stage"));
				}

				if (("s").equalsIgnoreCase(rptType)) {

					if (sts != null
							&& !sts.equals(ReferenceTable.PAYMENT_SETTLED)) {
						if (sts.equals(ReferenceTable.CLAIM_REGISTERED_STATUS)) {
							OmpStatDto.setRegDate(rset
									.getString("Date of Registration"));
						}
						if (sts.equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)) {
							OmpStatDto.setRejectionDate(rset
									.getString("Date of Rejection"));
							OmpStatDto.setClassification(rset
									.getString("Classification"));
							OmpStatDto.setCategory(rset.getString("Category"));
							OmpStatDto.setRodClaimType(rset
									.getString("ROD Claim Type"));
							OmpStatDto.setFinalApprovedAmountINR(rset
									.getDouble("Final Approved Amount INR"));
							OmpStatDto.setFinalApprovedAmountinDoll(rset
									.getDouble("Final Approved Amount $"));

						}
						if (sts.equals(ReferenceTable.CLAIM_CLOSED_STATUS)) {
							OmpStatDto.setClosureDateValue(rset
									.getString("Date of Closure"));

						}
					}
					if (sts == null
							|| (sts != null && sts
									.equals(ReferenceTable.PAYMENT_SETTLED))) {
						OmpStatDto.setRodNumber(rset.getString("ROD Number"));
						OmpStatDto.setClassification(rset
								.getString("Classification"));
						OmpStatDto.setCategory(rset.getString("Category"));
						OmpStatDto.setRodClaimType(rset
								.getString("ROD Claim Type"));
						OmpStatDto.setDocumentReceivedFrom(rset
								.getString("Document Received From"));
						OmpStatDto.setCurrencyType(rset
								.getString("Currency Type"));
						OmpStatDto.setCurrencyRate(rset
								.getDouble("Currency Rate"));
						OmpStatDto.setConversionValue(rset
								.getString("Conversion Value"));
						OmpStatDto.setBillAmountinFC(rset
								.getDouble("Bill Amount in FC"));
						OmpStatDto.setAmountInDoll(rset
								.getDouble("Amount in $"));
						OmpStatDto.setDeductionInDoll(rset
								.getDouble("Deduction $"));
						OmpStatDto.setTotalAmountInDoll(rset
								.getDouble("Total amount in $"));
						OmpStatDto.setNegotiationDone(rset
								.getString("Negotiation Done"));
						OmpStatDto.setAgreedAmountInDoll(rset
								.getDouble("Agreed Amount $"));
						OmpStatDto.setTotalExpenseInDoll(rset
								.getDouble("Total Expense $"));
						OmpStatDto.setFinalApprovedAmountINR(rset
								.getDouble("Final Approved Amount INR"));
						OmpStatDto.setFinalApprovedAmountinDoll(rset
								.getDouble("Final Approved Amount $"));
						OmpStatDto.setNameOfNegotiator(rset
								.getString("Name of the Negotiator"));
						OmpStatDto.setNegotiationFee(rset
								.getDouble("Negotiation Fee"));
						OmpStatDto.setPayeeName(rset.getString("Payee Name"));
						OmpStatDto.setDateOfPaymentValue(rset
								.getString("Date of Payment"));
						OmpStatDto.setBankCharges(rset
								.getDouble("Bank charges"));
						OmpStatDto.setPayeeCurrency(rset
								.getString("Payee Currency"));
						OmpStatDto.setPaymentType(rset
								.getString("Payment type"));
						OmpStatDto.setChequeNo(rset.getString("Cheque No"));
						OmpStatDto
								.setChequeDtValue(rset.getString("Cheque Dt"));
						OmpStatDto.setBankCode(rset.getString("Bank code"));
						OmpStatDto.setBankName(rset.getString("Bank Name"));
						OmpStatDto.setAccountsApprovalDtValue(rset
								.getString("Accounts Approval Dt"));

					}

					OmpStatDto.setRemarks(rset.getString("Remarks"));
					OmpStatDto.setClaimStatus(rset.getString("Claim Status"));
				}
				ompOutStandingResultList.add(OmpStatDto);
				OmpStatDto = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return ompOutStandingResultList;
	}

	// IMSSUPPOR-24284
	public BeanItemContainer<SelectValue> getTmpCpuCodeListByUser(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_KERALA_LOT_CPU(?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);
			if (null != rset) {
				while (rset.next()) {

					Long cpuKey = rset.getLong("CPU_KEY");
					String cpuName = rset.getString("CPU");
					select = new SelectValue();
					select.setId(cpuKey);
					select.setValue(cpuName);
					selectValuesList.add(select);

				}
				cpuCodeContainer.addAll(selectValuesList);
				cpuCodeContainer.sort(new Object[] { "value" },
						new boolean[] { true });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cpuCodeContainer;
	}

	public List<Map<String, Object>> getTaskProcedureForPreauthAutoAllocation(
			String userId) {

		assignedAutoAllocationProcedure();

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_PRAH_ENHN_AUTO_GET_TASK (?,?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<SearchHoldMonitorScreenTableDTO> getHoldMonitorClaims(
			String intimationNo, String userID, String type, String menuType,
			Long cpuCode) {

		List<SearchHoldMonitorScreenTableDTO> holdmonitorClaims = new ArrayList<SearchHoldMonitorScreenTableDTO>();

		final String HOLD_MONITOR_CLAIMS = "{call PRC_AUTO_HOLD_MONITOR(?, ?, ?, ?,?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(HOLD_MONITOR_CLAIMS);
			cs.setString(1, intimationNo);
			cs.setString(2, userID);
			cs.setString(3, type);
			cs.setString(4, menuType);
			cs.setLong(5, cpuCode != null ? cpuCode : 0l);
			cs.registerOutParameter(6, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();

			rs = (ResultSet) cs.getObject(6);

			rs1 = (ResultSet) cs.getObject(7);
			Map<Long, Object> wkflwList = new HashMap<Long, Object>();
			SearchHoldMonitorScreenTableDTO holdClaims = null;

			if (rs != null) {

				while (rs.next()) {

					holdClaims = new SearchHoldMonitorScreenTableDTO();
					holdClaims.setIntimationNumber(rs
							.getString("INTIMATION_NO"));
					if (rs.getString("DOC_RECEIVED_DATE") != null) {

						// String reqDate =
						// SHAUtils.formatDateForHold(rs.getString("DOC_RECEIVED_DATE"));
						holdClaims
								.setReqDate(rs.getString("DOC_RECEIVED_DATE"));
					}
					holdClaims.setType(rs.getString("TYPE"));
					holdClaims.setLeg(rs.getString("LEG"));
					holdClaims.setHoldBy(rs.getString("HOLD_USER"));
					if (rs.getString("HOLD_DATE") != null) {
						// String holdDate =
						// SHAUtils.formatDateForHold(rs.getString("HOLD_DATE"));
						holdClaims.setHoldDate(rs.getString("HOLD_DATE"));
					}
					holdClaims.setHoldRemarks(rs.getString("HOLD_REMARKS"));
					holdClaims.setWkKey(rs.getLong("WK_KEY"));
					holdClaims.setPreauthKey(rs.getLong("CASHLESS_KEY"));
					holdmonitorClaims.add(holdClaims);
					holdClaims = null;
				}
			}
			if (!menuType.equalsIgnoreCase("M")) {
				if (rs1 != null) {

					Map<Long, Map<String, Object>> workFlowObj = new HashMap<Long, Map<String, Object>>();

					holdClaims = new SearchHoldMonitorScreenTableDTO();
					Map<String, Object> mappedValues = null;
					while (rs1.next()) {
						mappedValues = SHAUtils
								.getRevisedObjectFromCursorObj(rs1);
						Long wkFlwKey = (Long) mappedValues
								.get(SHAConstants.WK_KEY);
						workFlowObj.put(wkFlwKey, mappedValues);
						/*
						 * wkflwList.put(wkFlwKey, mappedValues); Object object
						 * = wkflwList.get(rs1.getLong("WK_KEY"));
						 * holdClaims.setDbOutArray(object);
						 * holdmonitorClaims.add(holdClaims);
						 */
						holdClaims.setPreauthKey(rs1.getLong("CASHLESS_KEY"));
					}
					for (SearchHoldMonitorScreenTableDTO searchHoldMonitorScreenTableDTO : holdmonitorClaims) {
						if (searchHoldMonitorScreenTableDTO.getWkKey() != null) {
							Map<String, Object> map = workFlowObj
									.get(searchHoldMonitorScreenTableDTO
											.getWkKey());
							searchHoldMonitorScreenTableDTO.setDbOutArray(map);
						}
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return holdmonitorClaims;
	}

	public String releaseHoldClaim(Long wkKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_AUTO_HOLD_RELASE (?, ?)}");
			cs.setLong(1, wkKey);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(2) != null) {
				successMsg = cs.getNString(2).toString();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return successMsg;

	}

	/*
	 * public Map<String,Object> getOpinionValidationDetails(Long
	 * claimedAmnt,Long claimStage,Long desicion,Long cpuCode,String
	 * reconsiderationFlag,Long policyKey,Long claimKey, String enhType, String
	 * hospitilizationType, Integer queryTyp, Integer queryCount) {
	 * 
	 * final String PARALLEL_FLOW_CPCR_PROC =
	 * "{call PRC_OPINION_DETAILS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	 * String loginUserId =
	 * ((String)VaadinSession.getCurrent().getAttribute(BPMClientContext
	 * .USERID)).toUpperCase();
	 * System.out.println("call PRC_OPINION_DETAILS("+claimedAmnt
	 * +","+claimStage+
	 * ","+desicion+","+cpuCode+","+reconsiderationFlag+","+policyKey
	 * +","+claimKey
	 * +","+loginUserId+","+enhType+","+hospitilizationType+","+queryTyp
	 * +","+queryCount+")"); Connection connection = null; CallableStatement cs
	 * = null; ResultSet rs = null; ResultSet rs1 = null; Map<String,Object>
	 * resultMap = new HashMap<String, Object>();
	 * 
	 * try { connection = BPMClientContext.getConnection(); cs =
	 * connection.prepareCall(PARALLEL_FLOW_CPCR_PROC); cs.setLong(1,
	 * claimedAmnt); cs.setLong(2, claimStage); cs.setLong(3, desicion);
	 * cs.setLong(4, cpuCode); cs.setString(5, reconsiderationFlag);
	 * cs.setLong(6, policyKey); cs.setLong(7, claimKey); cs.setString(8,
	 * loginUserId); //LV_USER_CODE cs.setString(9, enhType);
	 * //LV_ENHANCEMENT_TYPE cs.setString(10,
	 * hospitilizationType);//LV_HOSPITALIZATION cs.setLong(11, queryTyp);
	 * cs.setLong(12, queryCount); cs.registerOutParameter(13,
	 * OracleTypes.CURSOR); cs.registerOutParameter(14, OracleTypes.CURSOR);
	 * cs.execute();
	 * 
	 * rs = (ResultSet) cs.getObject(14); rs1 = (ResultSet) cs.getObject(13);
	 * Long i = 1l; if (rs != null) { List<SpecialSelectValue> listOfEmp = new
	 * ArrayList<SpecialSelectValue>();
	 * 
	 * while (rs.next()) {
	 * System.out.println("Role List "+rs.getString("EMP_ID")
	 * +"---"+rs.getString("EMP_NAME")+"---"+rs.getString("EMP_ROLE"));
	 * SpecialSelectValue covers = new
	 * SpecialSelectValue(i,rs.getString("EMP_ID"
	 * ),rs.getString("EMP_NAME"),rs.getString("EMP_ROLE"));
	 * listOfEmp.add(covers); i++; } resultMap.put("emp", listOfEmp);
	 * 
	 * }
	 * 
	 * if (rs1 != null) { List<SpecialSelectValue> listOfRole = new
	 * ArrayList<SpecialSelectValue>(); String mandatoryFlag = "N"; String
	 * portedFlag = ""; while (rs1.next()) {
	 * System.out.println("Empolyee List "+
	 * rs1.getString("ROLE_DESCRIPTION")+"---"
	 * +rs1.getString("MANDATORY_FLAG")+"---"+rs1.getString("ROLE_CODE"));
	 * SpecialSelectValue select = new
	 * SpecialSelectValue(i,rs1.getString("ROLE_DESCRIPTION"
	 * ),rs1.getString("MANDATORY_FLAG"),rs1.getString("ROLE_CODE"));
	 * listOfRole.add(select); mandatoryFlag = rs1.getString("MANDATORY_FLAG");
	 * portedFlag = rs1.getString("LV_PORTED_POLICY");
	 * 
	 * i++; } resultMap.put("role", listOfRole); resultMap.put("mandatoryFlag",
	 * mandatoryFlag); resultMap.put("portedFlag", portedFlag); }
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if (rs
	 * != null) { rs.close(); rs = null; } if (rs1 != null) { rs1.close(); rs1 =
	 * null; } if (cs != null) { cs.close(); cs = null; } if (connection !=
	 * null) { connection.close(); } } catch (SQLException e) {
	 * e.printStackTrace(); } } return resultMap;
	 * 
	 * }
	 */

	public Map<String,Object> getOpinionValidationDetails(Long claimedAmnt,Long claimStage,Long desicion,Long cpuCode,String reconsiderationFlag,Long policyKey,Long claimKey, String enhType, String hospitilizationType, Integer queryTyp, Integer queryCount,Long calimType, Long docRecFrom,String presenterString,Long cashlessOrRodKey, Long statusKey,String negotationFlag) {
		final String PARALLEL_FLOW_CPCR_PROC = "{call PRC_OPINION_DETAILS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		String loginUserId = ((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID)).toUpperCase();	
		System.out.println("call PRC_OPINION_DETAILS("+claimedAmnt+","+claimStage+","+desicion+","+cpuCode+","+reconsiderationFlag+","+policyKey+","+claimKey+","+loginUserId+","+enhType+","+hospitilizationType+","+queryTyp+","+queryCount+","+calimType+","+docRecFrom+","+presenterString+","+cashlessOrRodKey+","+statusKey+","+negotationFlag+")");
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PARALLEL_FLOW_CPCR_PROC);
			cs.setLong(1, claimedAmnt);
			cs.setLong(2, claimStage);
			cs.setLong(3, desicion);		
			cs.setLong(4, cpuCode);
			cs.setString(5, reconsiderationFlag);
			cs.setLong(6, policyKey);
			cs.setLong(7, claimKey);
			cs.setString(8, loginUserId); //LV_USER_CODE
			cs.setString(9, enhType); //LV_ENHANCEMENT_TYPE
			cs.setString(10, hospitilizationType);//LV_HOSPITALIZATION
			cs.setLong(11, queryTyp); 
			cs.setLong(12, queryCount);
			cs.setLong(13, calimType);
			cs.setLong(14, docRecFrom);
			cs.setString(15, presenterString);
			cs.setLong(16, cashlessOrRodKey != null ? cashlessOrRodKey : 0L);
			cs.setLong(17, statusKey);
			cs.setString(18, negotationFlag);
			cs.registerOutParameter(19, Types.VARCHAR);
			cs.registerOutParameter(21, OracleTypes.CURSOR);
			cs.registerOutParameter(20, OracleTypes.CURSOR);
			cs.execute();

			rs1 = (ResultSet) cs.getObject(20);
			rs = (ResultSet) cs.getObject(21);
			Long i = 1l;
			if (rs != null) {
	            List<SpecialSelectValue> listOfEmp = new ArrayList<SpecialSelectValue>();
	           
				while (rs.next()) {
					if(rs.getString("EMP_ID") != null /*&& rs.getString("EMP_NAME") != null && rs.getString("EMP_ROLE") != null*/) {
					System.out.println("Role List "+rs.getString("EMP_ID")+"---"+rs.getString("EMP_NAME")+"---"+rs.getString("EMP_ROLE"));
					SpecialSelectValue covers = new SpecialSelectValue(i,rs.getString("EMP_ID"),rs.getString("EMP_NAME"),rs.getString("EMP_ROLE"));
					listOfEmp.add(covers);
					i++;
				}
				}
				resultMap.put("emp", listOfEmp);
				
			}
			
			if (rs1 != null) {
	            List<SpecialSelectValue> listOfRole = new LinkedList<SpecialSelectValue>();
	            String mandatoryFlag = "N";
				String portedFlag = "";
				Long heirarchyKey = 0L;
				while (rs1.next()) {
					System.out.println("Empolyee List "+rs1.getString("ROLE_DESCRIPTION")+"---"+rs1.getString("MANDATORY_FLAG")+"---"+rs1.getString("ROLE_CODE"));
					SpecialSelectValue select = new SpecialSelectValue(i,rs1.getString("ROLE_DESCRIPTION"),rs1.getString("MANDATORY_FLAG"),rs1.getString("ROLE_CODE"));
					listOfRole.add(select);
					mandatoryFlag = rs1.getString("MANDATORY_FLAG");
					portedFlag = rs1.getString("LV_PORTED_POLICY");
					heirarchyKey = rs1.getLong("HIERARCHY_KEY");
					i++;
				}
				resultMap.put("role", listOfRole);
				resultMap.put("mandatoryFlag", mandatoryFlag);
				resultMap.put("portedFlag", portedFlag);
				resultMap.put("heirarchyKey", heirarchyKey);
			}
			if (cs.getObject(19) != null) {
				resultMap.put("seriousDeficiencyFlag", String.valueOf(cs.getString(19)));
			}else{
				resultMap.put("seriousDeficiencyFlag", null);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultMap;

	}

	public List<OpinionValidationReportTableDTO> getOpinionValidationReportList(
			String role, String userId, java.sql.Date fromDate,
			java.sql.Date toDate, Long opinionStatus, Long validatedStatus) {

		List<OpinionValidationReportTableDTO> opinionValidationReportList = new ArrayList<OpinionValidationReportTableDTO>();

		final String OPINION_VALIDATION_REPORT_PROC = "{call prc_rpt_opinion(?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(OPINION_VALIDATION_REPORT_PROC);
			cs.setString(1, role);
			cs.setString(2, userId);
			cs.setDate(3, fromDate);
			cs.setDate(4, toDate);
			cs.setInt(5, opinionStatus.intValue());
			cs.setInt(6, validatedStatus.intValue());

			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(7);

			if (rs != null) {
				OpinionValidationReportTableDTO newOpinionValidationDto = null;
				while (rs.next()) {
					newOpinionValidationDto = new OpinionValidationReportTableDTO();

					newOpinionValidationDto.setIntimationNo(rs
							.getString("INTIMATION_NUMBER"));
					newOpinionValidationDto.setUpdatedBy(rs
							.getString("UPDATED_BY_USER_ID"));
					newOpinionValidationDto.setUpdatedDateTime(rs
							.getTimestamp("UPDATED_BY_DATE"));
					newOpinionValidationDto.setConsultedRole(rs
							.getString("CONSULTED_ROLE"));
					newOpinionValidationDto.setConsultedName(rs
							.getString("CONSULTED_NAME"));
					newOpinionValidationDto.setConsultedRemarks(rs
							.getString("CONSULTED_REMARKS"));
					newOpinionValidationDto.setValidatedBy(rs
							.getString("VALIDATED_BY"));
					newOpinionValidationDto.setValidatedDateTime(rs
							.getTimestamp("VALIDATED_DATE"));
					newOpinionValidationDto.setValidatedStatus(rs
							.getString("VALIDATED_STATUS"));
					newOpinionValidationDto.setValidatedRemarks(rs
							.getString("VALIDATED_REMARKS"));

					opinionValidationReportList.add(newOpinionValidationDto);
					newOpinionValidationDto = null;
				}
				// System.out.println("--- Start prc_rpt_daily_cashless after "+
				// System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return opinionValidationReportList;
	}

	public Map<String, String> callStopProcess(String policyNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PROCESS_STOP (?, ?)}");
			cs.setString(1, policyNumber);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				while (rs.next()) {
					String processFlag = rs.getString("PROCESS_STOP");
					String remarks = rs.getString("REMARKS");
					resultMap.put(SHAConstants.PROCESS_FLAG, processFlag);
					resultMap.put(SHAConstants.PROCESS_REMARKS, remarks);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;

	}

	public Map<String, Object> getAadharGenaratorNo(Long aadharNo,
			String insuredName, String insureddob, String gender) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfAadharDtls = new HashMap<String, Object>();
		Long generatorNo = 0L;
		String refFlag = "";
		String refMsg = "";
		Long refGenNo = 0L;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_AADHAR_DETAILS (?,?,?,?,?,?,?,?)}");
			cs.setLong(1, aadharNo);
			cs.setString(2, insuredName);
			cs.setString(3, insureddob);
			cs.setString(4, gender);
			cs.setString(5, "G");

			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.NUMERIC);
			cs.execute();
			if (cs.getString(6) != null) {
				refFlag = cs.getString(6);
			}
			if (cs.getObject(7) != null) {
				refMsg = cs.getString(7);
			}
			if (cs.getObject(8) != null) {
				refGenNo = cs.getLong(8);
			}
			listOfAadharDtls.put("refFlag", refFlag);
			listOfAadharDtls.put("refMessage", refMsg);
			listOfAadharDtls.put("refGenerateNo", refGenNo);
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return listOfAadharDtls;

	}

	public List<SelectValue> getDuplicateInsuredDetails(Long policyKey,
			Long insuredKey) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SelectValue> resultList = new ArrayList<SelectValue>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_DUPLICATE_INS_DTLS (?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				while (rs.next()) {
					String insuredName = rs.getString("INSURED_NAME");
					String healthCardNo = rs.getString("HEALTH_CARD_NUMBER");
					SelectValue dto = new SelectValue();
					dto.setValue(insuredName);
					dto.setCommonValue(healthCardNo);
					resultList.add(dto);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;

	}

	/**
	 * As Part of CR R1201
	 * 
	 * @param fromDate
	 *            SQL Date
	 * @param toDate
	 *            SQL Date
	 * @param cpuKey
	 *            Long
	 * @param userId
	 *            String
	 * @return
	 */
	public List<FinApprovedPaymentPendingReportDto> getFAapprovedSettlementPendingReport(
			java.sql.Date fromDate, java.sql.Date toDate, Long cpuSelect,
			String userId) {

		final String FA_APPROVED_SETTLEMTN_PENDING_REPORT = "{call  PRC_RPT_FA_APRVED_NON_SETTLED(?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<FinApprovedPaymentPendingReportDto> faApprovedSettlementPendingResultList = new ArrayList<FinApprovedPaymentPendingReportDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(FA_APPROVED_SETTLEMTN_PENDING_REPORT);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setLong(3, cpuSelect);
			cs.setString(4, userId);

			cs.registerOutParameter(5, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(5);

			if (null != rset) {
				FinApprovedPaymentPendingReportDto settlementPendingDto = null;
				int index = 1;
				while (rset.next()) {

					settlementPendingDto = new FinApprovedPaymentPendingReportDto();

					settlementPendingDto.setSno(String.valueOf(index));
					settlementPendingDto.setIntimationNumber(rset
							.getString("intimation_no"));
					settlementPendingDto.setClaimNumber(rset
							.getString("claim_no"));
					settlementPendingDto.setPolicyNumber(rset
							.getString("policy_number"));
					settlementPendingDto.setProdName(rset
							.getString("product_name"));
					settlementPendingDto.setCpuCode(rset.getString("cpu_code"));
					settlementPendingDto.setDiagnosis(rset
							.getString("diagnosis"));
					settlementPendingDto.setHospitalName(rset
							.getString("hospital_name"));
					settlementPendingDto.setHospCode(rset
							.getString("hospital_code"));
					settlementPendingDto.setPatientName(rset
							.getString("patient_name"));
					settlementPendingDto.setPaidDate(rset
							.getString("fa_approval_date"));
					settlementPendingDto.setApprovedAmount(rset
							.getDouble("amount"));
					settlementPendingDto.setClaimedAmount(rset
							.getDouble("claimed_amount"));
					settlementPendingDto.setDeductAmount(rset
							.getDouble("deducted_amount"));
					settlementPendingDto.setClmType(rset
							.getString("claim_type"));
					settlementPendingDto.setMedicalApprovedBy(rset
							.getString("medical_approval_person"));
					settlementPendingDto.setBillingApprovedBy(rset
							.getString("billing_person"));
					settlementPendingDto.setFinApprovedBy(rset
							.getString("financial_approved"));
					settlementPendingDto.setClmCoverage(rset
							.getString("claim_coverage"));
					settlementPendingDto.setSumInsured(rset
							.getString("sum_insured"));
					settlementPendingDto.setAge(rset.getInt("age"));
					settlementPendingDto.setFinancialYear(rset
							.getString("financial_year"));
					settlementPendingDto
							.setAdmissionDate(rset.getString("doa"));
					settlementPendingDto.setIntimationDate(rset
							.getString("intimated_date"));

					index++;

					faApprovedSettlementPendingResultList
							.add(settlementPendingDto);
					settlementPendingDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return faApprovedSettlementPendingResultList;

	}

	public List<AgentBrokerReportTableDTO> getAgentBrokerList(
			java.sql.Date fromDate, java.sql.Date toDate, String userName) {

		List<AgentBrokerReportTableDTO> agentBrokerReportList = new ArrayList<AgentBrokerReportTableDTO>();

		final String AGENT_REPORT_PROC = "{call PRC_RPT_FOR_AGENT_BROKER(?, ?, ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		// System.out.println("--- Start prc_rpt_daily_cashless before "+
		// System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(AGENT_REPORT_PROC);
			cs.setDate(1, fromDate);
			cs.setDate(2, toDate);
			cs.setString(3, userName);

			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(4);

			if (rs != null) {
				AgentBrokerReportTableDTO newAgentBrokerDto = null;
				while (rs.next()) {

					newAgentBrokerDto = new AgentBrokerReportTableDTO();

					newAgentBrokerDto.setIntimationNo(rs
							.getString("intimationNo"));
					newAgentBrokerDto.setPolicyNo(rs.getString("policyNo"));
					newAgentBrokerDto.setPolicyIssueOffice(rs
							.getString("policyIssueOffice"));
					newAgentBrokerDto.setSmCode(rs.getString("smCode"));
					newAgentBrokerDto.setSmName(rs.getString("smName"));
					newAgentBrokerDto.setAgentBrokerCode(rs
							.getString("agentBrokerCode"));
					newAgentBrokerDto.setAgentBrokerName(rs
							.getString("agentBrokerName"));
					newAgentBrokerDto.setHospitalName(rs
							.getString("hospitalName"));
					newAgentBrokerDto.setCashlessSettledAmount(rs
							.getString("cashlessSettledAmount"));
					newAgentBrokerDto.setReimbursementSettledAmount(rs
							.getString("reimbursementSettledAmount"));
					newAgentBrokerDto.setOutstandingAmount(rs
							.getString("outstandingAmount"));
					newAgentBrokerDto.setTotalAmount(rs
							.getString("totalAmount"));
					/*
					 * newAgentBrokerDto.setHospitalId(rs
					 * .getLong("hospitalId"));
					 */

					agentBrokerReportList.add(newAgentBrokerDto);
					newAgentBrokerDto = null;
				}
				System.out.println("--- Start PRC_RPT_FOR_AGENT_BROKER after "
						+ System.currentTimeMillis());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return agentBrokerReportList;
	}

	public List<SelectValue> getSuperSurplusAlertList(String policyNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<SelectValue> resultList = new ArrayList<SelectValue>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_MATERNITY_STL_AMT (?, ?)}");
			cs.setString(1, policyNumber);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				while (rs.next()) {
					String intimationNumber = rs.getString("INTIMATION_NUMBER");
					String approvedAmount = rs.getString("TOT_APPROVED_AMOUNT");
					SelectValue dto = new SelectValue();
					dto.setValue(intimationNumber);
					dto.setCommonValue(approvedAmount);
					resultList.add(dto);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;

	}

	public List<BranchManagerFeedBackReportDto> getBranchManagerFeedbackReport(
			Long feedbackTypeId, Long period, Long zoneCode, Long branchCode,
			String searchType) {
		List<BranchManagerFeedBackReportDto> resultList = new ArrayList<BranchManagerFeedBackReportDto>();

		// final String BRANCH_MANAGER_FB_REPORT =
		// "{call  PRC_RPT_BM_FB_ZONE_RATING(?,?,?,?,?,?)}";

		final String BRANCH_MANAGER_FB_REPORT = "{call  PRC_RPT_BM_FB_ZONE_RAT(?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(BRANCH_MANAGER_FB_REPORT);
			cs.setLong(1, feedbackTypeId);
			cs.setLong(2, period);
			cs.setLong(3, zoneCode);
			cs.setLong(4, branchCode);
			cs.setString(5, searchType);

			cs.registerOutParameter(6, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(6);

			if (null != rset) {
				BranchManagerFeedBackReportDto bmFBDto = null;
				int index = 1;
				String prevBranchCode = "";
				while (rset.next()) {

					if (!prevBranchCode.equalsIgnoreCase(rset
							.getString("FEEDBACK_BRANCH_CODE"))) {
						bmFBDto = new BranchManagerFeedBackReportDto();
					}
					if (bmFBDto != null) {
						bmFBDto.setSno(String.valueOf(index));

						bmFBDto.setFeedbackSelect(new SelectValue(
								feedbackTypeId, ""));
						bmFBDto.setPeriodSelect(new SelectValue(period, ""));
						bmFBDto.setZonalOfficeSelect(new SelectValue(zoneCode,
								""));
						bmFBDto.setBranchOfficeSelect(new SelectValue(
								branchCode, ""));

						/*
						 * if(SHAConstants.BM_FB_BRANCH_ACTUAL_TYPE.equalsIgnoreCase
						 * (searchType)){
						 * bmFBDto.setZoneCode(rset.getString("FEEDBACK_ZONE_CODE"
						 * )); } if(SHAConstants.BM_FB_BRANCH_DRILLED_TYPE.
						 * equalsIgnoreCase(searchType)){
						 * bmFBDto.setBranchCode(rset
						 * .getString("FEEDBACK_BRANCH_CODE")); }
						 */

						feedbackTypeId = rset.getLong("FEEBACK_TYPE_ID");
						bmFBDto.setBranchCode(rset
								.getString("FEEDBACK_BRANCH_CODE"));
						prevBranchCode = bmFBDto.getBranchCode();
						bmFBDto.setBranchName(rset.getString("BRANCH_NAME"));
						bmFBDto.setRating(rset.getDouble("RATING_AVG"));

						if (feedbackTypeId
								.equals(ReferenceTable.FEEDBACK_TYPE_CLAIM_RETAIL)) {
							bmFBDto.setClaimRetailReported(rset
									.getInt("REPORTED") != 0 ? String
									.valueOf(rset.getInt("REPORTED")) : "0");
							bmFBDto.setClaimRetailResponded(rset
									.getInt("REPLIED") != 0 ? String
									.valueOf(rset.getInt("REPLIED")) : "0");
							bmFBDto.setClaimRetailPending(rset
									.getInt("PENDING") != 0 ? String
									.valueOf(rset.getInt("PENDING")) : "0");
						} else if (feedbackTypeId
								.equals(ReferenceTable.FEEDBACK_TYPE_MER)) {
							bmFBDto.setMerReported(rset.getInt("REPORTED") != 0 ? String
									.valueOf(rset.getInt("REPORTED")) : "0");
							bmFBDto.setMerResponded(rset.getInt("REPLIED") != 0 ? String
									.valueOf(rset.getInt("REPLIED")) : "0");
							bmFBDto.setMerPending(rset.getInt("PENDING") != 0 ? String
									.valueOf(rset.getInt("PENDING")) : "0");
						} else if (feedbackTypeId
								.equals(ReferenceTable.FEEDBACK_TYPE_CLAIM_GMC)) {
							bmFBDto.setClaimGmcReported(rset.getInt("REPORTED") != 0 ? String
									.valueOf(rset.getInt("REPORTED")) : "0");
							bmFBDto.setClaimGmcResponded(rset.getInt("REPLIED") != 0 ? String
									.valueOf(rset.getInt("REPLIED")) : "0");
							bmFBDto.setClaimGmcPending(rset.getInt("PENDING") != 0 ? String
									.valueOf(rset.getInt("PENDING")) : "0");
						}

						index++;

						resultList.add(bmFBDto);
						// bmFBDto = null;
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;
	}

	public List<BranchManagerFeedBackReportingPatternDto> getBranchManagerFeedbackReportingPattern(
			Long feedbackTypeId, String period, Long zoneCode, Long branchCode) {

		List<BranchManagerFeedBackReportingPatternDto> resultList = new ArrayList<BranchManagerFeedBackReportingPatternDto>();

		final String BRANCH_MANAGER_FB_RPT_PATTERN = "{call  PRC_RPT_BM_FB_PATTERN(?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(BRANCH_MANAGER_FB_RPT_PATTERN);
			cs.setLong(1, feedbackTypeId);
			cs.setString(2, period);
			cs.setLong(3, zoneCode);
			cs.setLong(4, branchCode);

			cs.registerOutParameter(5, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(5);

			if (null != rset) {
				BranchManagerFeedBackReportingPatternDto bmFBPatternDto = null;
				int index = 1;
				while (rset.next()) {

					bmFBPatternDto = new BranchManagerFeedBackReportingPatternDto();

					bmFBPatternDto.setSno(String.valueOf(index));

					bmFBPatternDto.setFeedbackSelect(new SelectValue(
							feedbackTypeId, ""));
					bmFBPatternDto.setPeriodSelect(new SelectValue(1l, period));
					bmFBPatternDto.setZonalOfficeSelect(new SelectValue(
							zoneCode, ""));
					bmFBPatternDto.setBranchOfficeSelect(new SelectValue(
							branchCode, ""));
					bmFBPatternDto.setMonthName(period);

					bmFBPatternDto.setBranchCode(rset
							.getString("FEEDBACK_BRANCH_CODE"));
					bmFBPatternDto.setBranchName((String) rset.getObject(2));
					bmFBPatternDto.setD1(rset.getInt("D1"));
					bmFBPatternDto.setD2(rset.getInt("D2"));
					bmFBPatternDto.setD3(rset.getInt("D3"));
					bmFBPatternDto.setD4(rset.getInt("D4"));
					bmFBPatternDto.setD5(rset.getInt("D5"));
					bmFBPatternDto.setD6(rset.getInt("D6"));
					bmFBPatternDto.setD7(rset.getInt("D7"));
					bmFBPatternDto.setD8(rset.getInt("D8"));
					bmFBPatternDto.setD9(rset.getInt("D9"));
					bmFBPatternDto.setD10(rset.getInt("D10"));
					bmFBPatternDto.setD11(rset.getInt("D11"));
					bmFBPatternDto.setD12(rset.getInt("D12"));
					bmFBPatternDto.setD13(rset.getInt("D13"));
					bmFBPatternDto.setD14(rset.getInt("D14"));
					bmFBPatternDto.setD15(rset.getInt("D15"));
					bmFBPatternDto.setD16(rset.getInt("D16"));
					bmFBPatternDto.setD17(rset.getInt("D17"));
					bmFBPatternDto.setD18(rset.getInt("D18"));
					bmFBPatternDto.setD19(rset.getInt("D19"));
					bmFBPatternDto.setD20(rset.getInt("D20"));
					bmFBPatternDto.setD21(rset.getInt("D21"));
					bmFBPatternDto.setD22(rset.getInt("D22"));
					bmFBPatternDto.setD23(rset.getInt("D23"));
					bmFBPatternDto.setD24(rset.getInt("D24"));
					bmFBPatternDto.setD25(rset.getInt("D25"));
					bmFBPatternDto.setD26(rset.getInt("D26"));
					bmFBPatternDto.setD27(rset.getInt("D27"));
					bmFBPatternDto.setD28(rset.getInt("D28"));
					bmFBPatternDto.setD29(rset.getInt("D29"));
					bmFBPatternDto.setD30(rset.getInt("D30"));
					bmFBPatternDto.setD31(rset.getInt("D31"));

					index++;

					resultList.add(bmFBPatternDto);
					bmFBPatternDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;

	}

	public Map<String, Object> getFeedBackCountDetails(String userId,
			String branchcode) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfFeedBackCount = new HashMap<String, Object>();
		Long dailyCount = 0L;
		Long overAllcount = 0L;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BM_FB_COUNT (?,?,?,?)}");
			cs.setString(1, userId);
			cs.setString(2, branchcode);
			cs.registerOutParameter(3, Types.NUMERIC);
			cs.registerOutParameter(4, Types.NUMERIC);
			cs.execute();
			if (cs.getObject(3) != null) {
				dailyCount = cs.getLong(3);
			}
			if (cs.getObject(4) != null) {
				overAllcount = cs.getLong(4);
			}
			listOfFeedBackCount.put("dailyCount", dailyCount);
			listOfFeedBackCount.put("overAllcount", overAllcount);
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return listOfFeedBackCount;

	}

	public String bypassInvestigationAllowed(String policyNumber) {

		Connection connection = null;
		CallableStatement cs = null;
		String bypassInvAllowed = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_INV_BYPASS(?,?)}");
			cs.setString(1, policyNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				bypassInvAllowed = cs.getString(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bypassInvAllowed;
	}

	public List<BranchManagerFeedbackTableDTO> getManagerFeedbackReport( SelectValue feedbackValue, SelectValue zoneValue, SelectValue branchValue,SelectValue feedbackStatusValue,
			SelectValue feedbackTypeValue,java.sql.Date fromDate,java.sql.Date toDate,EntityManager entityManager) {


		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		 List<BranchManagerFeedbackTableDTO> list = new ArrayList<BranchManagerFeedbackTableDTO>();

		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_BM_FB_GET_TASK (?,?,?,?,?,?,?,?,?)}");
			cs.setLong(1, feedbackValue!=null?feedbackValue.getId():0L);
			cs.setLong(2, zoneValue!=null && zoneValue.getId()!=null?zoneValue.getId():0L);
			cs.setLong(3, branchValue!=null && branchValue.getId()!=null?branchValue.getId():0L);
			cs.setLong(4, feedbackStatusValue!=null && feedbackStatusValue.getId()!=null?feedbackStatusValue.getId():0L);
			cs.setLong(5, feedbackTypeValue!=null && feedbackTypeValue.getId()!=null?feedbackTypeValue.getId():0L);
			cs.setDate(6, fromDate!=null?fromDate:null);
			cs.setDate(7, toDate!=null?toDate:null);
			cs.registerOutParameter(8, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(9,Types.INTEGER);
			cs.execute();

			rs = (ResultSet) cs.getObject(8);

			if (rs != null) {
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO = null;
				while (rs.next()) {
					branchManagerFeedbackTableDTO = new BranchManagerFeedbackTableDTO();
					branchManagerFeedbackTableDTO.setBranchDetails(rs.getString("BRANCH_DETAILS"));
					branchManagerFeedbackTableDTO.setFeedbackKey(rs.getLong("FEEDBACK_KEY"));
					branchManagerFeedbackTableDTO.setFeedBackDtlsKey(rs.getLong("FEEDBACK_DTLS_KEY"));
					branchManagerFeedbackTableDTO.setFeedbackStatus(rs.getString("PROCESS_VALUE"));
					branchManagerFeedbackTableDTO.setFeedbackStatusId(getStatusByName(branchManagerFeedbackTableDTO.getFeedbackStatus(),entityManager));
					branchManagerFeedbackTableDTO.setFeedbackType(rs.getString("FEEDBACK_RATING"));
					branchManagerFeedbackTableDTO.setReportedDate(rs.getString("FEEDBACK_RAISED_DATE"));
					branchManagerFeedbackTableDTO.setRepliedDate(rs.getString("FEEDBACK_REPLY_DATE"));
					branchManagerFeedbackTableDTO.setFeedbackRemarksOverall(rs.getString("DOCUMENT_RAISED_REMARKS"));
					branchManagerFeedbackTableDTO.setFeedBack(rs.getString("FEEDBACK_TYPE"));
					branchManagerFeedbackTableDTO.setFeedBackReviewFlag(rs.getString("FB_REVIEWER_FLAG"));
					branchManagerFeedbackTableDTO.setFeedBackReviewRatingId(rs.getLong("FB_REVIEWER_RATING_ID"));
					branchManagerFeedbackTableDTO.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
				//	branchManagerFeedbackTableDTO.setFeedBackTypeId(rs.getLong("FEEBACK_TYPE_ID"));
					branchManagerFeedbackTableDTO.setFbStatusId(rs.getLong("STATUS_ID"));
					branchManagerFeedbackTableDTO.setFbCategory(rs.getString("BM_CATEGORY"));
					branchManagerFeedbackTableDTO.setIntimationNo(rs.getString("DOCUMENT_NUMBER"));
					branchManagerFeedbackTableDTO.setClaimNumber(rs.getString("DOCUMENT_NUMBER"));
					branchManagerFeedbackTableDTO.setProposalNumber(rs.getString("DOCUMENT_NUMBER"));
					
					//branchManagerFeedbackTableDTO.setFeedbackRepliedDate(rs.getDate("FEEDBACK_REPLY_DATE"));
					branchManagerFeedbackTableDTO.setMobile(rs.getString("MOBILE_NO"));
					branchManagerFeedbackTableDTO.setFeedbackKeyForFeedbackValue(feedbackValue.getId());
					if(branchManagerFeedbackTableDTO.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)) {
						branchManagerFeedbackTableDTO.setTechnicalTeamReply(rs.getString("DOCUMENT_REPLY_RAMARKS"));
						}else if(branchManagerFeedbackTableDTO.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
						branchManagerFeedbackTableDTO.setCorporateMedicalUnderwritingReply(rs.getString("DOCUMENT_REPLY_RAMARKS")); }
						else if(branchManagerFeedbackTableDTO.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
						branchManagerFeedbackTableDTO.setClaimsDepartmentReply(rs.getString("DOCUMENT_REPLY_RAMARKS"));
						
						}
					branchManagerFeedbackTableDTO.setTotalCount(cs.getInt(9));
					branchManagerFeedbackTableDTO.setFeedbackOption(true);
					list.add(branchManagerFeedbackTableDTO);
					branchManagerFeedbackTableDTO= null;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

		
	  }

	public Map<String, Object> getTopUpPolicyDetails(String policyNo) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> listOfTopUpPolicy = new HashMap<String, Object>();
		String alertFlag = "";
		String alertMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_GET_TOPUP_POLCY_ALERT(?,?,?)}");
			cs.setString(1, policyNo);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				alertFlag = cs.getString(2);
			}
			if (cs.getString(3) != null) {
				alertMsg = cs.getString(3);
			}
			listOfTopUpPolicy.put("alertFlag", alertFlag);
			listOfTopUpPolicy.put("alertMsg", alertMsg);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfTopUpPolicy;
	}

	public List<BranchManagerPreviousFeedbackTableDTO> getManagerPreviousFeedback(
			SelectValue feedbackValue, SelectValue zoneValue,
			SelectValue branchValue, SelectValue feedbackStatusValue,
			SelectValue feedbackTypeValue, java.sql.Date fromDate,
			java.sql.Date toDate, EntityManager entityManager, String user) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		List<BranchManagerPreviousFeedbackTableDTO> list = new ArrayList<BranchManagerPreviousFeedbackTableDTO>();

		try {
			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_BM_PRVS_FB_TASK (?,?,?,?,?,?,?,?)}");
			cs.setString(1, user.toUpperCase());
			cs.setLong(2, null != feedbackValue ? feedbackValue.getId() : 0L);
			/*
			 * cs.setLong(2, zoneValue!=null &&
			 * zoneValue.getId()!=null?zoneValue.getId():0L); cs.setLong(3,
			 * branchValue!=null &&
			 * branchValue.getId()!=null?branchValue.getId():0L);
			 */
			cs.setLong(
					3,
					feedbackStatusValue != null
							&& feedbackStatusValue.getId() != null ? feedbackStatusValue
							.getId() : 0L);
			cs.setLong(
					4,
					feedbackTypeValue != null
							&& feedbackTypeValue.getId() != null ? feedbackTypeValue
							.getId() : 0L);
			cs.setDate(5, fromDate != null ? fromDate : null);
			cs.setDate(6, toDate != null ? toDate : null);
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(8, Types.INTEGER);
			cs.execute();

			rs = (ResultSet) cs.getObject(7);

			if (rs != null) {
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO = null;
				while (rs.next()) {
					branchManagerFeedbackTableDTO = new BranchManagerPreviousFeedbackTableDTO();
					branchManagerFeedbackTableDTO.setBranchDetails(rs
							.getString("BRANCH_DETAILS"));
					branchManagerFeedbackTableDTO.setFeedbackKey(rs
							.getLong("FEEDBACK_KEY"));
					branchManagerFeedbackTableDTO.setFeedbackStatus(rs
							.getString("PROCESS_VALUE"));
					// branchManagerFeedbackTableDTO.setFeedbackStatusId(getStatusByName(branchManagerFeedbackTableDTO.getFeedbackStatus(),entityManager));
					branchManagerFeedbackTableDTO.setFeedbackType(rs
							.getString("FEEDBACK_RATING"));
					branchManagerFeedbackTableDTO.setReportedDate(rs
							.getString("FEEDBACK_RAISED_DATE"));
					branchManagerFeedbackTableDTO.setFeedbackRemarksOverall(rs
							.getString("DOCUMENT_RAISED_REMARKS"));
					branchManagerFeedbackTableDTO.setTechnicalTeamReply(rs
							.getString("DOCUMENT_REPLY_RAMARKS"));
					branchManagerFeedbackTableDTO
							.setCorporateMedicalUnderwritingReply(rs
									.getString("DOCUMENT_REPLY_RAMARKS"));
					branchManagerFeedbackTableDTO.setClaimsDepartmentReply(rs
							.getString("DOCUMENT_REPLY_RAMARKS"));
					branchManagerFeedbackTableDTO.setClaimsDeptReply(rs
							.getString("DOCUMENT_REPLY_RAMARKS"));
					branchManagerFeedbackTableDTO.setRepliedDate(rs
							.getString("FEEDBACK_REPLY_DATE"));
					branchManagerFeedbackTableDTO.setMobile(rs
							.getInt("MOBILE_NO"));
					branchManagerFeedbackTableDTO.setFeedBackDate(rs
							.getString("FEEDBACK_RAISED_DATE"));
					branchManagerFeedbackTableDTO.setFeedBackTypeId(rs
							.getLong("FEEBACK_TYPE_ID"));
					branchManagerFeedbackTableDTO.setFeedBack(rs
							.getString("FEEDBACK_TYPE"));
					branchManagerFeedbackTableDTO.setFeedBackDtlsKey(rs
							.getLong("FEEDBACK_DTLS_KEY"));
					branchManagerFeedbackTableDTO.setDocumentNumber(rs
							.getString("DOCUMENT_NUMBER"));
					branchManagerFeedbackTableDTO.setFeedBackReviewFlag(rs
							.getString("FB_REVIEWER_FLAG"));
					branchManagerFeedbackTableDTO.setFeedBackReviewRatingId(rs
							.getLong("FB_REVIEWER_RATING_ID"));
					branchManagerFeedbackTableDTO.setFbStatusId(rs
							.getLong("STATUS_ID"));
					branchManagerFeedbackTableDTO.setFbCategory(rs
							.getString("BM_CATEGORY"));
					list.add(branchManagerFeedbackTableDTO);
					branchManagerFeedbackTableDTO = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public BeanItemContainer<SelectValue> getZoneCode() {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		BeanItemContainer<SelectValue> List = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BM_FB_LOAD_ZONE (?)}");
			// cs.setString(1, zoneValue.getValue());
			cs.registerOutParameter(1, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(1);
			if (rs != null) {
				SelectValue select = null;
				while (rs.next()) {
					select = new SelectValue();
					select.setValue(rs.getString("BRANCH_CODE") + " - "
							+ rs.getString("BRANCH_NAME"));
					select.setId(Long.valueOf(rs.getString("BRANCH_CODE")));
					selectValuesList.add(select);
				}
			}
			List.addAll(selectValuesList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return List;
	}

	public BeanItemContainer<SelectValue> getBranchDetailsContainerForBranchManagerFeedback(
			Long zoneCode) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		BeanItemContainer<SelectValue> branchList = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BM_FB_BRANCH_LOAD (?,?)}");
			cs.setLong(1, zoneCode);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			if (rs != null) {
				SelectValue select = null;
				while (rs.next()) {
					// PARENT_BRANCH_KEY
					// LEVEL
					select = new SelectValue();
					select.setValue(rs.getString("BRANCH_CODE") + " - "
							+ rs.getString("BRANCH_NAME"));
					select.setId(Long.valueOf(rs.getString("BRANCH_CODE")));
					selectValuesList.add(select);
				}
			}
			branchList.addAll(selectValuesList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return branchList;

	}

	public BranchManagerFeedbackhomePageDto getBranchManagerHomePageStats(
			String branchCode, String userId) {

		BranchManagerFeedbackhomePageDto bmfbhomePageDto = new BranchManagerFeedbackhomePageDto();
		List<FeedbackStatsDto> resultList = new ArrayList<FeedbackStatsDto>();
		List<ReviewStatsDto> reviewList = new ArrayList<ReviewStatsDto>();

		final String BRANCH_MANAGER_FB_HOMEPAGE = "{call PRC_BM_FEEDBACK(?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		ResultSet rset1 = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(BRANCH_MANAGER_FB_HOMEPAGE);
			cs.setString(1, userId);
			cs.setString(2, branchCode);

			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, OracleTypes.CURSOR);
			cs.execute();

			rset = (ResultSet) cs.getObject(3);
			rset1 = (ResultSet) cs.getObject(4);

			if (null != rset) {
				FeedbackStatsDto bmFBStatsDto = null;
				int index = 1;
				while (rset.next()) {

					bmFBStatsDto = new FeedbackStatsDto();

					bmFBStatsDto
							.setFeedbackArea(rset.getString("MASTER_VALUE"));
					bmFBStatsDto.setReported(rset.getLong("REPORTED"));
					bmFBStatsDto.setResponded(rset.getLong("REPLIED"));
					bmFBStatsDto.setPending(rset.getLong("PENDING"));
					bmFBStatsDto.setFeedbackAreaKey(rset.getLong("MASTER_KEY"));

					resultList.add(bmFBStatsDto);
					bmFBStatsDto = null;
				}
			}

			bmfbhomePageDto.setFeedbackStatsList(resultList);

			if (rset1 != null) {

				ReviewStatsDto bmFBreviewDto = null;
				int index = 1;
				while (rset1.next()) {

					bmFBreviewDto = new ReviewStatsDto();

					bmFBreviewDto.setReviewStatus("Review Pending");
					bmFBreviewDto.setMerReview(rset1.getLong("MER"));
					bmFBreviewDto.setClaimsRetailReview(rset1
							.getLong("CLAIM_RETAIL"));
					bmFBreviewDto
							.setClaimsGmcReview(rset1.getLong("CLAIM_GMC"));

					reviewList.add(bmFBreviewDto);
					bmFBreviewDto = null;
				}

			}

			bmfbhomePageDto.setReviewStatsList(reviewList);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (rset1 != null) {
					rset1.close();
					rset1 = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return bmfbhomePageDto;
	}

	private Long getStatusByName(String statusname, EntityManager entityManager) {
		Query findByBranchCode = entityManager
				.createNamedQuery("Status.findByName");
		findByBranchCode.setParameter("status", statusname.toLowerCase());
		List<Status> resultList = (List<Status>) findByBranchCode
				.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0).getKey();
		}
		return null;

	}

	public BeanItemContainer<SelectValue> getBranchContainerForManagerFeedback(
			String userName) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		BeanItemContainer<SelectValue> List = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_USER_BRANCH_CNT (?,?)}");
			cs.setString(1, userName.toUpperCase());
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			if (rs != null) {
				SelectValue select = null;
				while (rs.next()) {
					select = new SelectValue();
					select.setValue(rs.getString("BRANCH_CODE") + " - "
							+ rs.getString("BRANCH_NAME"));
					select.setId(Long.valueOf(rs.getString("BRANCH_CODE")));
					select.setCommonValue(rs.getString("ZONE_CODE"));
					selectValuesList.add(select);
				}
			}
			List.addAll(selectValuesList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return List;

	}

	public void callProcedureForSTP(PreauthDTO preauthDto) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_STP_COMPARE (?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, null != preauthDto.getKey() ? preauthDto.getKey()
					: 0l);
			cs.setLong(
					2,
					preauthDto.getAmountConsidered() != null ? Long
							.valueOf(preauthDto.getAmountConsidered()) : 0l);
			cs.setLong(3, preauthDto.getStatusKey());
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.registerOutParameter(6, Types.NUMERIC);
			cs.registerOutParameter(7, Types.NUMERIC);
			cs.registerOutParameter(8, Types.NUMERIC);
			cs.execute();
			if (cs.getObject(4) != null) {
				preauthDto.getPreauthMedicalDecisionDetails()
						.setIsDecisionMismatched((String) cs.getObject(4));
			}
			if (cs.getObject(5) != null) {
				preauthDto.getPreauthMedicalDecisionDetails().setStpStatus(
						(String) cs.getObject(5));
			}
			if (cs.getObject(6) != null) {
				preauthDto.getPreauthMedicalDecisionDetails().setStpClaimedAmt(
						((BigDecimal) cs.getObject(6)).toString());
			}
			if (cs.getObject(7) != null) {
				preauthDto.getPreauthMedicalDecisionDetails()
						.setStpDeductibleAmt(
								((BigDecimal) cs.getObject(7)).toString());
			}
			if (cs.getObject(8) != null) {
				preauthDto.getPreauthMedicalDecisionDetails()
						.setStpApprovedAmt(
								((BigDecimal) cs.getObject(8)).toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<NewIntimationNotAdheringToANHDto> getNotAdheringToANHRList(
			java.sql.Date sqlFrmDate, java.sql.Date sqlToDate, Long cpuCode,
			String userId) {

		final String NOT_ADHERING_TO_ANH_REPORT = "{call  PRC_NON_ADHERING_ANH(?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<NewIntimationNotAdheringToANHDto> notAdheringToANHRList = new ArrayList<NewIntimationNotAdheringToANHDto>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(NOT_ADHERING_TO_ANH_REPORT);
			cs.setDate(1, sqlFrmDate);
			cs.setDate(2, sqlToDate);
			cs.setLong(3, cpuCode);
			cs.setString(4, userId);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();
			rset = (ResultSet) cs.getObject(5);

			if (null != rset) {
				NewIntimationNotAdheringToANHDto intimationNotAdheringToANHDto = null;

				while (rset.next()) {

					intimationNotAdheringToANHDto = new NewIntimationNotAdheringToANHDto();

					intimationNotAdheringToANHDto.setIntimationId(rset
							.getString("Intimation No"));
					intimationNotAdheringToANHDto.getInsuredPatient()
							.setInsuredName(
									rset.getString("Insured Patient Name"));
					intimationNotAdheringToANHDto.setDoctorName(rset
							.getString("Doctor"));
					intimationNotAdheringToANHDto.getHospitalDto().setName(
							rset.getString("Hospital Name"));
					intimationNotAdheringToANHDto.getHospitalDto()
							.setHospitalCode(rset.getString("Hospital Code"));
					intimationNotAdheringToANHDto.getHospitalDto()
							.setHospitalTypeValue(
									rset.getString("Hospital Type"));
					intimationNotAdheringToANHDto.getHospitalDto().setAddress(
							rset.getString("Hospital Address"));
					intimationNotAdheringToANHDto.getHospitalDto()
							.setPhoneNumber(
									rset.getString("Hospital Contact Details"));
					intimationNotAdheringToANHDto.setReasonForAdmission(rset
							.getString("Diagnosis"));
					intimationNotAdheringToANHDto.setCpuAddress(rset
							.getString("CPU_CODE"));
					notAdheringToANHRList.add(intimationNotAdheringToANHDto);
					intimationNotAdheringToANHDto = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return notAdheringToANHRList;

	}	
	
	public List<ClaimPayment> getCreateBatchPaymentDetails(Long SearchType,Long batchType,Long zone,String paymentType,String cpuCode,String intimationNo,
			String paymentCpu,String claimType,String docRecFrom,String lotNo,Long productKey,Long tatOver,String batchNo,java.sql.Date sqlFrmDate, java.sql.Date sqlToDate,EntityManager entityManager,String verificationType) {

		List<ClaimPayment> createBatchPaymentList = new ArrayList<ClaimPayment>();

		final String CREATE_BATCH_PROC = "{call PRC_GET_PAYMENT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setLong(1, SearchType);
			cs.setLong(2, batchType);
			cs.setLong(3, zone);
			cs.setString(4, paymentType);
			cs.setString(5, cpuCode);
			cs.setString(6, intimationNo);
			cs.setString(7, paymentCpu);
			cs.setString(8, claimType);
			cs.setString(9, docRecFrom);
			cs.setString(10, lotNo);
			cs.setLong(11, productKey);
			cs.setLong(12, tatOver);
			cs.setString(13, batchNo);
			cs.setDate(14, sqlFrmDate);
			cs.setDate(15, sqlToDate);
			cs.setString(16, verificationType);
			
			

			cs.registerOutParameter(17, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(17);

			if (rs != null) {
				ClaimPayment claimPayment = null;
				while (rs.next()) {

					claimPayment = new ClaimPayment();

					claimPayment.setKey(rs.getLong("PAYMENT_KEY"));
					claimPayment.setBatchNumber(rs.getString("BATCH_NUMBER"));
					claimPayment.setLotNumber(rs.getString("LOT_NUMBER"));
					claimPayment.setRodNumber(rs.getString("ROD_NUMBER"));
					claimPayment.setPioCode(rs.getString("PIO_CODE"));
					claimPayment.setPaymentCpuCode(rs.getLong("PAYMENT_CPU_CODE"));
					claimPayment.setProductCode(rs.getString("PRODUCT_CODE"));
					claimPayment.setClaimType(rs.getString("CLAIM_TYPE"));					
					claimPayment.setIntimationNumber(rs.getString("INTIMATION_NUMBER"));
					claimPayment.setClaimNumber(rs.getString("CLAIM_NUMBER"));					
					claimPayment.setPolicyNumber(rs.getString("POLICY_NUMBER"));
					claimPayment.setPolicySysId(rs.getLong("POLICY_SYS_ID"));
					claimPayment.setRiskId(rs.getLong("RISK_ID"));					
					claimPayment.setPaymentType(rs.getString("PAYMENT_TYPE"));
					claimPayment.setApprovedAmount(rs.getDouble("APPROVED_AMOUNT"));
					claimPayment.setCpuCode(rs.getLong("CPU_CODE"));
					claimPayment.setIfscCode(rs.getString("IFSC_CODE"));
					claimPayment.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
					claimPayment.setBranchName(rs.getString("BRANCH_NAME"));
					claimPayment.setPayeeName(rs.getString("PAYEE_NAME"));
					claimPayment.setPayableAt(rs.getString("PAYABLE_AT"));
					claimPayment.setPanNumber(rs.getString("PAN_NUMBER"));
					claimPayment.setHospitalCode(rs.getString("HOSPITAL_CODE"));
					claimPayment.setEmailId(rs.getString("EMAIL_ID"));
					claimPayment.setProposerCode(rs.getString("PROPOSER_CODE"));
					claimPayment.setProposerName(rs.getString("PROPOSER_NAME"));
					claimPayment.setCreatedDate(rs.getDate("CREATED_DATE"));
					claimPayment.setCreatedBy(rs.getString("CREATED_BY"));
					claimPayment.setNotificationFlag(rs.getString("NOTIFICATION_FLAG"));
					MastersValue paymentStaus = new MastersValue();
					paymentStaus.setKey(rs.getLong("PAYMENT_STATUS_ID"));
					paymentStaus.setValue(rs.getString("PAYMENT_STATUS"));
					claimPayment.setPaymentStatus(paymentStaus);
					Status lotStatus = new Status();
					lotStatus.setKey(rs.getLong("LOT_STATUS"));
					Status batchStatus = new Status();
					batchStatus.setKey(rs.getLong("BATCH_STATUS"));
					claimPayment.setLotStatus(lotStatus);
					claimPayment.setBatchStatus(batchStatus);
					claimPayment.setBankCode(rs.getString("BANK_CODE"));
					claimPayment.setChequeDDDate(rs.getDate("CHEQUE_DD_DATE"));
					claimPayment.setChequeDDNumber(rs.getString("CHEQUE_DD_NUMBER"));			
					claimPayment.setBankName(rs.getString("BANK_NAME"));
					claimPayment.setTdsAmount(rs.getDouble("TDS_AMOUNT"));
					claimPayment.setTdsPercentage(rs.getLong("TDS_PERCENTAGE"));
					claimPayment.setNetAmount(rs.getDouble("NET_AMOUNT"));
					claimPayment.setFaApprovalDate(rs.getDate("FA_APPROVAL_DATE"));
					claimPayment.setReissueRejectionFlag(rs.getString("REISSUE_REJECTION_FLAG"));
					claimPayment.setBatchHoldFlag(rs.getString("BATCH_HOLD_FLAG"));	
					Stage stage = new Stage();
					stage.setKey(rs.getLong("STAGE_ID"));
					claimPayment.setStageId(stage);
					Status status = new Status();
					status.setKey(rs.getLong("STATUS_ID"));
					claimPayment.setStatusId(status);
					claimPayment.setLetterPrintingMode(rs.getString("LETTER_FLAG"));
					claimPayment.setDocumentReceivedFrom(rs.getString("DOCUMENT_RECEIVED_FROM"));
					claimPayment.setModifiedBy(rs.getString("MODIFIED_BY"));
					Timestamp modifyDate = new Timestamp(rs.getDate("MODIFIED_DATE").getTime());
					claimPayment.setModifiedDate(modifyDate);
					claimPayment.setReasonForChange(rs.getString("REASON_FOR_CHANGE"));
					claimPayment.setLegalHeirName(rs.getString("LEGAL_HEIR_NAME"));	
					claimPayment.setPaymentDate(rs.getDate("PAYMENT_DATE"));
					claimPayment.setBatchCreatedDate(rs.getDate("BATCH_CREATED_DATE"));	
					claimPayment.setRemarks(rs.getString("REMARKS"));
					claimPayment.setDeletedFlag(rs.getString("DELETED_FLAG"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setDelayDays(rs.getLong("DELAY_DAYS"));
					claimPayment.setInterestRate(rs.getDouble("INT_RATE"));
					claimPayment.setInterestAmount(rs.getDouble("INT_AMT"));
					claimPayment.setIntrestRemarks(rs.getString("INT_REMARKS"));
					claimPayment.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
					claimPayment.setLastAckDate(rs.getDate("LAST_ACKNOW_DT"));	
					claimPayment.setAllowedDelayDays(rs.getLong("ALLOWED_DELAY_DAYS"));
					claimPayment.setReconisderFlag(rs.getString("RECONSIDER_FLAG"));
					claimPayment.setTotalApprovedAmount(rs.getDouble("TOT_APPROVED_AMOUNT"));
					claimPayment.setPremiumAmt(rs.getDouble("PREMIUM_AMOUNT"));
					claimPayment.setPremiaWSFlag(rs.getString("INT_FLAG_PREMIUM"));
					claimPayment.setInstallmentDeductionFlag(rs.getString("INSTAL_DED_FLAG"));
					claimPayment.setBatchProcessFlag(rs.getString("BATCH_PROCESS_FLAG"));
					claimPayment.setLumpsumFlag(rs.getString("LUMPSUM_FLAG"));
					claimPayment.setRecordStatusFlag(rs.getString("REC_STATUS"));
					claimPayment.setBenefitsId(rs.getLong("BENEFITS_ID"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setOptionalApprovedAmount(rs.getDouble("OPT_APPROVED_AMOUNT"));
					claimPayment.setAddOnCoversApprovedAmount(rs.getDouble("ADD_APPROVED_AMOUNT"));
					claimPayment.setProcessClaimType(rs.getString("PROCESS_CLM_TYPE"));
					claimPayment.setZonalMailId(rs.getString("ZONE_MAIL_ID"));
					Long hospitalKey = rs.getLong("HOSPITAL_KEY");
					claimPayment.setHospitalKey(hospitalKey);
					
					claimPayment.setInsuredName(rs.getString("INSURED_NAME"));
					claimPayment.setPayModeChangeReason(rs.getString("PAYMODE_CHANGE_REASON"));
					claimPayment.setGmcEmployeeName(rs.getString("EMPLOYEE_NAME"));	
					claimPayment.setProductName(rs.getString("PRODUCT_NAME"));
					claimPayment.setRodkey(rs.getLong("ROD_KEY"));
					claimPayment.setCorporateBufferLimit(rs.getDouble("CORPORATE_BUFFER_LIMIT"));
					claimPayment.setSaveFlag(rs.getString("SAVE_FLAG"));	
					claimPayment.setVerifiedStatusFlag(rs.getString("VERIFIED_STATUS_FLAG"));						
					claimPayment.setSelectCount(rs.getString("RECORD_COUNT"));

					createBatchPaymentList.add(claimPayment);
					claimPayment = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return createBatchPaymentList;
	}
	
	public List<ClaimPayment> getCreateBatchPaymentDetailsForBancs(Long SearchType,Long batchType,Long zone,String paymentType,String cpuCode,String intimationNo,
			Long paymentCpu,String claimType,String docRecFrom,String lotNo,Long productKey,Long tatOver,String batchNo,java.sql.Date sqlFrmDate,  
			java.sql.Date sqlToDate,String menuString, EntityManager entityManager,String strVerificationType) {

		List<ClaimPayment> createBatchPaymentList = new ArrayList<ClaimPayment>();

		final String CREATE_BATCH_PROC = "{call PRC_GB_GET_PAYMENT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setLong(1, SearchType);
			cs.setLong(2, batchType);
			cs.setLong(3, zone);
			cs.setString(4, paymentType);
			cs.setString(5, cpuCode);
			cs.setString(6, intimationNo);
			cs.setLong(7, paymentCpu);
			cs.setString(8, claimType);
			cs.setString(9, docRecFrom);
			cs.setString(10, lotNo);
			cs.setLong(11, productKey);
			cs.setLong(12, tatOver);
			cs.setString(13, batchNo);
			cs.setDate(14, sqlFrmDate);
			cs.setDate(15, sqlToDate);
			cs.setString(16, menuString);
			cs.setString(17, strVerificationType);

			System.out.println("PRC_GB_GET_PAYMENT("+SearchType.toString()+", "+batchType.toString()+", "+zone.toString()+", "+paymentType+", ("+
					cpuCode+"), "+intimationNo+", "+paymentCpu.toString()+", "+claimType+", "+docRecFrom+", "+lotNo+", "+productKey.toString()+", "+
					tatOver.toString()+", "+batchNo+", "+sqlFrmDate+", "+sqlToDate+", "+menuString+", "+strVerificationType+")");
			

			cs.registerOutParameter(18, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(18);

			if (rs != null) {
				ClaimPayment claimPayment = null;
				while (rs.next()) {

					claimPayment = new ClaimPayment();

					claimPayment.setKey(rs.getLong("PAYMENT_KEY"));
					claimPayment.setBatchNumber(rs.getString("BATCH_NUMBER"));
					claimPayment.setLotNumber(rs.getString("LOT_NUMBER"));
					claimPayment.setRodNumber(rs.getString("ROD_NUMBER"));
					claimPayment.setPioCode(rs.getString("PIO_CODE"));
					claimPayment.setPaymentCpuCode(rs.getLong("PAYMENT_CPU_CODE"));
					claimPayment.setProductCode(rs.getString("PRODUCT_CODE"));
					claimPayment.setClaimType(rs.getString("CLAIM_TYPE"));					
					claimPayment.setIntimationNumber(rs.getString("INTIMATION_NUMBER"));
					claimPayment.setClaimNumber(rs.getString("CLAIM_NUMBER"));					
					claimPayment.setPolicyNumber(rs.getString("POLICY_NUMBER"));
					claimPayment.setPolicySysId(rs.getLong("POLICY_SYS_ID"));
					claimPayment.setRiskId(rs.getLong("RISK_ID"));					
					claimPayment.setPaymentType(rs.getString("PAYMENT_TYPE"));
					claimPayment.setApprovedAmount(rs.getDouble("APPROVED_AMOUNT"));
					claimPayment.setCpuCode(rs.getLong("CPU_CODE"));
					claimPayment.setIfscCode(rs.getString("IFSC_CODE"));
					claimPayment.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
					claimPayment.setBranchName(rs.getString("BRANCH_NAME"));
					claimPayment.setPayeeName(rs.getString("PAYEE_NAME"));
					claimPayment.setPayableAt(rs.getString("PAYABLE_AT"));
					claimPayment.setPanNumber(rs.getString("PAN_NUMBER"));
					claimPayment.setHospitalCode(rs.getString("HOSPITAL_CODE"));
					claimPayment.setEmailId(rs.getString("EMAIL_ID"));
					claimPayment.setProposerCode(rs.getString("PROPOSER_CODE"));
					claimPayment.setProposerName(rs.getString("PROPOSER_NAME"));
					claimPayment.setCreatedDate(rs.getDate("CREATED_DATE"));
					claimPayment.setCreatedBy(rs.getString("CREATED_BY"));
					claimPayment.setNotificationFlag(rs.getString("NOTIFICATION_FLAG"));
					MastersValue paymentStaus = new MastersValue();
					paymentStaus.setKey(rs.getLong("PAYMENT_STATUS_ID"));
					paymentStaus.setValue(rs.getString("PAYMENT_STATUS"));
					claimPayment.setPaymentStatus(paymentStaus);
					Status lotStatus = new Status();
					lotStatus.setKey(rs.getLong("LOT_STATUS"));
					Status batchStatus = new Status();
					batchStatus.setKey(rs.getLong("BATCH_STATUS"));
					claimPayment.setLotStatus(lotStatus);
					claimPayment.setBatchStatus(batchStatus);
					claimPayment.setBankCode(rs.getString("BANK_CODE"));
					claimPayment.setChequeDDDate(rs.getDate("CHEQUE_DD_DATE"));
					claimPayment.setChequeDDNumber(rs.getString("CHEQUE_DD_NUMBER"));			
					claimPayment.setBankName(rs.getString("BANK_NAME"));
					claimPayment.setTdsAmount(rs.getDouble("TDS_AMOUNT"));
					claimPayment.setTdsPercentage(rs.getLong("TDS_PERCENTAGE"));
					claimPayment.setNetAmount(rs.getDouble("NET_AMOUNT"));
					claimPayment.setFaApprovalDate(rs.getDate("FA_APPROVAL_DATE"));
					claimPayment.setReissueRejectionFlag(rs.getString("REISSUE_REJECTION_FLAG"));
					claimPayment.setBatchHoldFlag(rs.getString("BATCH_HOLD_FLAG"));	
					Stage stage = new Stage();
					stage.setKey(rs.getLong("STAGE_ID"));
					claimPayment.setStageId(stage);
					Status status = new Status();
					status.setKey(rs.getLong("STATUS_ID"));
					claimPayment.setStatusId(status);
					claimPayment.setLetterPrintingMode(rs.getString("LETTER_FLAG"));
					claimPayment.setDocumentReceivedFrom(rs.getString("DOCUMENT_RECEIVED_FROM"));
					claimPayment.setModifiedBy(rs.getString("MODIFIED_BY"));
					if(rs.getDate("MODIFIED_DATE") != null) {
					Timestamp modifyDate = new Timestamp(rs.getDate("MODIFIED_DATE").getTime());
					claimPayment.setModifiedDate(modifyDate); }
					claimPayment.setReasonForChange(rs.getString("REASON_FOR_CHANGE"));
					claimPayment.setLegalHeirName(rs.getString("LEGAL_HEIR_NAME"));	
					claimPayment.setPaymentDate(rs.getDate("PAYMENT_DATE"));
					claimPayment.setBatchCreatedDate(rs.getDate("BATCH_CREATED_DATE"));	
					claimPayment.setRemarks(rs.getString("REMARKS"));
					claimPayment.setDeletedFlag(rs.getString("DELETED_FLAG"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setDelayDays(rs.getLong("DELAY_DAYS"));
					claimPayment.setInterestRate(rs.getDouble("INT_RATE"));
					claimPayment.setInterestAmount(rs.getDouble("INT_AMT"));
					claimPayment.setIntrestRemarks(rs.getString("INT_REMARKS"));
					claimPayment.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
					claimPayment.setLastAckDate(rs.getDate("LAST_ACKNOW_DT"));	
					claimPayment.setAllowedDelayDays(rs.getLong("ALLOWED_DELAY_DAYS"));
					claimPayment.setReconisderFlag(rs.getString("RECONSIDER_FLAG"));
					claimPayment.setTotalApprovedAmount(rs.getDouble("TOT_APPROVED_AMOUNT"));
					claimPayment.setPremiumAmt(rs.getDouble("PREMIUM_AMOUNT"));
					claimPayment.setPremiaWSFlag(rs.getString("INT_FLAG_PREMIUM"));
					claimPayment.setInstallmentDeductionFlag(rs.getString("INSTAL_DED_FLAG"));
					claimPayment.setBatchProcessFlag(rs.getString("BATCH_PROCESS_FLAG"));
					claimPayment.setLumpsumFlag(rs.getString("LUMPSUM_FLAG"));
					claimPayment.setRecordStatusFlag(rs.getString("REC_STATUS"));
					claimPayment.setBenefitsId(rs.getLong("BENEFITS_ID"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setOptionalApprovedAmount(rs.getDouble("OPT_APPROVED_AMOUNT"));
					claimPayment.setAddOnCoversApprovedAmount(rs.getDouble("ADD_APPROVED_AMOUNT"));
					claimPayment.setProcessClaimType(rs.getString("PROCESS_CLM_TYPE"));
					claimPayment.setZonalMailId(rs.getString("ZONE_MAIL_ID"));
					Long hospitalKey = rs.getLong("HOSPITAL_KEY");
					claimPayment.setHospitalKey(hospitalKey);
					
					claimPayment.setInsuredName(rs.getString("INSURED_NAME"));
					claimPayment.setPayModeChangeReason(rs.getString("PAYMODE_CHANGE_REASON"));
					claimPayment.setGmcEmployeeName(rs.getString("EMPLOYEE_NAME"));	
					claimPayment.setProductName(rs.getString("PRODUCT_NAME"));
					claimPayment.setRodkey(rs.getLong("ROD_KEY"));
					claimPayment.setCorporateBufferLimit(rs.getDouble("CORPORATE_BUFFER_LIMIT"));
					claimPayment.setSaveFlag(rs.getString("SAVE_FLAG"));						
 					claimPayment.setNomineeName(rs.getString("NOMINEE_NAME"));
					claimPayment.setNomineeRelationship(rs.getString("NOMINEE_RELATIONSHIP"));
					claimPayment.setPayeeRelationship(rs.getString("PAYEE_RELATIONSHIP"));
					claimPayment.setLegalHeirName(rs.getString("LEGAL_HEIR_NAME"));
					claimPayment.setLegalHeirRelationship(rs.getString("LEGAL_HEIR_RELATIONSHIP"));
//					claimPayment.setAccType(rs.getString("ACCOUNT_TYPE"));
					claimPayment.setMicrCode(rs.getString("MICR_CODE"));
					claimPayment.setAccountPreference(rs.getString("ACCOUNT_PREFERENCE"));
					claimPayment.setVirtualPaymentAddr(rs.getString("VIRTUAL_PAYMENT_ADDRESS"));
					claimPayment.setSourceRiskId(rs.getString("SOURCE_RISK_ID"));
					claimPayment.setBancsPriorityFlag(rs.getString("BANCS_PRIORITY_FLAG"));
//					claimPayment.setAccountPreference(rs.getString("ACCOUNT_PREFERENCE"));
					claimPayment.setSelectCount(rs.getString("RECORD_COUNT"));
					createBatchPaymentList.add(claimPayment);
					claimPayment = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return createBatchPaymentList;
	}
	
	public List<ClaimPayment> getCreateLotPaymentDetails(String strType,
			Date startDate, Date toDate, String intimationNo, String cpuCode,
			String strClaimType, String strClaimant, Long paymentStatusID,
			String strVerificationType, String lotNo, String claimNo,
			String rodNo, String strProduct, String batchNo) {

		List<ClaimPayment> createBatchPaymentList = new ArrayList<ClaimPayment>();
		
		java.sql.Date sqlToDate = null;
		java.sql.Date sqlFromDate = null;
		if(toDate != null){
			sqlToDate = new java.sql.Date(toDate.getTime());
		}
		if(startDate != null){
			sqlFromDate = new java.sql.Date(startDate.getTime());
		}

		final String CREATE_BATCH_PROC = "{call PRC_LOT_GET_PAYMENT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, strType);
			cs.setDate(2, sqlFromDate);
			cs.setDate(3, sqlToDate);
			cs.setString(4, intimationNo);
			cs.setString(5, cpuCode);
			cs.setString(6, strClaimType);
			cs.setString(7, strClaimant);
			cs.setLong(8, paymentStatusID);
			cs.setString(9, strVerificationType);
			cs.setString(10, lotNo);
			cs.setString(11, claimNo);
			cs.setString(12, rodNo);
			cs.setString(13, strProduct);
			cs.setString(14, batchNo);
	
			cs.registerOutParameter(15, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(15);

			if (rs != null) {
				ClaimPayment claimPayment = null;
				while (rs.next()) {

					claimPayment = new ClaimPayment();

					claimPayment.setKey(rs.getLong("PAYMENT_KEY"));
					claimPayment.setBatchNumber(rs.getString("BATCH_NUMBER"));
					claimPayment.setLotNumber(rs.getString("LOT_NUMBER"));
					claimPayment.setRodNumber(rs.getString("ROD_NUMBER"));
					claimPayment.setPioCode(rs.getString("PIO_CODE"));
					claimPayment.setPaymentCpuCode(rs.getLong("PAYMENT_CPU_CODE"));
					claimPayment.setProductCode(rs.getString("PRODUCT_CODE"));
					claimPayment.setClaimType(rs.getString("CLAIM_TYPE"));					
					claimPayment.setIntimationNumber(rs.getString("INTIMATION_NUMBER"));
					claimPayment.setClaimNumber(rs.getString("CLAIM_NUMBER"));					
					claimPayment.setPolicyNumber(rs.getString("POLICY_NUMBER"));
					claimPayment.setPolicySysId(rs.getLong("POLICY_SYS_ID"));
					claimPayment.setRiskId(rs.getLong("RISK_ID"));					
					claimPayment.setPaymentType(rs.getString("PAYMENT_TYPE"));
					claimPayment.setApprovedAmount(rs.getDouble("APPROVED_AMOUNT"));
					claimPayment.setCpuCode(rs.getLong("CPU_CODE"));
					claimPayment.setIfscCode(rs.getString("IFSC_CODE"));
					claimPayment.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
					claimPayment.setBranchName(rs.getString("BRANCH_NAME"));
					claimPayment.setPayeeName(rs.getString("PAYEE_NAME"));
					claimPayment.setPayableAt(rs.getString("PAYABLE_AT"));
					claimPayment.setPanNumber(rs.getString("PAN_NUMBER"));
					claimPayment.setHospitalCode(rs.getString("HOSPITAL_CODE"));
					claimPayment.setEmailId(rs.getString("EMAIL_ID"));
					claimPayment.setProposerCode(rs.getString("PROPOSER_CODE"));
					claimPayment.setProposerName(rs.getString("PROPOSER_NAME"));
					claimPayment.setCreatedDate(rs.getDate("CREATED_DATE"));
					claimPayment.setCreatedBy(rs.getString("CREATED_BY"));
					claimPayment.setNotificationFlag(rs.getString("NOTIFICATION_FLAG"));
					MastersValue paymentStaus = new MastersValue();
					paymentStaus.setKey(rs.getLong("PAYMENT_STATUS_ID"));
					paymentStaus.setValue(rs.getString("PAYMENT_STATUS"));
					claimPayment.setPaymentStatus(paymentStaus);
					Status lotStatus = new Status();
					lotStatus.setKey(rs.getLong("LOT_STATUS"));
					Status batchStatus = new Status();
					batchStatus.setKey(rs.getLong("BATCH_STATUS"));
					claimPayment.setLotStatus(lotStatus);
					claimPayment.setBatchStatus(batchStatus);
					claimPayment.setBankCode(rs.getString("BANK_CODE"));
					claimPayment.setChequeDDDate(rs.getDate("CHEQUE_DD_DATE"));
					claimPayment.setChequeDDNumber(rs.getString("CHEQUE_DD_NUMBER"));			
					claimPayment.setBankName(rs.getString("BANK_NAME"));
					claimPayment.setTdsAmount(rs.getDouble("TDS_AMOUNT"));
					claimPayment.setTdsPercentage(rs.getLong("TDS_PERCENTAGE"));
					claimPayment.setNetAmount(rs.getDouble("NET_AMOUNT"));
					claimPayment.setFaApprovalDate(rs.getDate("FA_APPROVAL_DATE"));
					claimPayment.setReissueRejectionFlag(rs.getString("REISSUE_REJECTION_FLAG"));
					claimPayment.setBatchHoldFlag(rs.getString("BATCH_HOLD_FLAG"));	
					Stage stage = new Stage();
					stage.setKey(rs.getLong("STAGE_ID"));
					claimPayment.setStageId(stage);
					Status status = new Status();
					status.setKey(rs.getLong("STATUS_ID"));
					claimPayment.setStatusId(status);
					claimPayment.setLetterPrintingMode(rs.getString("LETTER_FLAG"));
					claimPayment.setDocumentReceivedFrom(rs.getString("DOCUMENT_RECEIVED_FROM"));
					claimPayment.setModifiedBy(rs.getString("MODIFIED_BY") != null ? rs.getString("MODIFIED_BY") :null);
					claimPayment.setModifiedDate(rs.getDate("MODIFIED_DATE") != null ? new Timestamp(rs.getDate("MODIFIED_DATE").getTime()) : null);
					claimPayment.setReasonForChange(rs.getString("REASON_FOR_CHANGE"));
					claimPayment.setLegalHeirName(rs.getString("LEGAL_HEIR_NAME"));	
					claimPayment.setPaymentDate(rs.getDate("PAYMENT_DATE"));
					claimPayment.setBatchCreatedDate(rs.getDate("BATCH_CREATED_DATE"));	
					claimPayment.setRemarks(rs.getString("REMARKS"));
					claimPayment.setDeletedFlag(rs.getString("DELETED_FLAG"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setDelayDays(rs.getLong("DELAY_DAYS"));
					claimPayment.setInterestRate(rs.getDouble("INT_RATE"));
					claimPayment.setInterestAmount(rs.getDouble("INT_AMT"));
					claimPayment.setIntrestRemarks(rs.getString("INT_REMARKS"));
					claimPayment.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
					claimPayment.setLastAckDate(rs.getDate("LAST_ACKNOW_DT"));	
					claimPayment.setAllowedDelayDays(rs.getLong("ALLOWED_DELAY_DAYS"));
					claimPayment.setReconisderFlag(rs.getString("RECONSIDER_FLAG"));
					claimPayment.setTotalApprovedAmount(rs.getDouble("TOT_APPROVED_AMOUNT"));
					claimPayment.setPremiumAmt(rs.getDouble("PREMIUM_AMOUNT"));
					claimPayment.setPremiaWSFlag(rs.getString("INT_FLAG_PREMIUM"));
					claimPayment.setInstallmentDeductionFlag(rs.getString("INSTAL_DED_FLAG"));
					claimPayment.setBatchProcessFlag(rs.getString("BATCH_PROCESS_FLAG"));
					claimPayment.setLumpsumFlag(rs.getString("LUMPSUM_FLAG"));
					claimPayment.setRecordStatusFlag(rs.getString("REC_STATUS"));
					claimPayment.setBenefitsId(rs.getLong("BENEFITS_ID"));
					claimPayment.setBenefitsApprovedAmt(rs.getDouble("BEN_APPR_AMT"));
					claimPayment.setOptionalApprovedAmount(rs.getDouble("OPT_APPROVED_AMOUNT"));
					claimPayment.setAddOnCoversApprovedAmount(rs.getDouble("ADD_APPROVED_AMOUNT"));
					claimPayment.setProcessClaimType(rs.getString("PROCESS_CLM_TYPE"));
					claimPayment.setZonalMailId(rs.getString("ZONE_MAIL_ID"));
					Long hospitalKey = rs.getLong("HOSPITAL_KEY");
					claimPayment.setHospitalKey(hospitalKey);
					
					claimPayment.setInsuredName(rs.getString("INSURED_NAME"));
					claimPayment.setPayModeChangeReason(rs.getString("PAYMODE_CHANGE_REASON"));
					claimPayment.setGmcEmployeeName(rs.getString("EMPLOYEE_NAME"));	
					claimPayment.setProductName(rs.getString("PRODUCT_NAME"));
					claimPayment.setRodkey(rs.getLong("ROD_KEY"));
					claimPayment.setCorporateBufferLimit(rs.getDouble("CORPORATE_BUFFER_LIMIT"));
					claimPayment.setSaveFlag(rs.getString("SAVE_FLAG"));						

					createBatchPaymentList.add(claimPayment);
					claimPayment = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return createBatchPaymentList;
	}
	
			public Map<String, String> getLinkedPolicyDetails(String policyNumber, String empId) {
				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rs = null;
				Map<String,String> resultMap = new HashMap<String, String>();
			
				try {
					connection = BPMClientContext.getConnection();
					cs = connection
							.prepareCall("{call PRC_VW_LINK_POLICY (?, ?, ?)}");
					cs.setString(1, policyNumber);
					cs.setString(2, empId);
					cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
					cs.execute();
					rs = (ResultSet) cs.getObject(3);
					
					if (rs != null) {
						while (rs.next()) {
							String linkPolicyNumber = rs.getString("POLICY_NUMBER");
							String proposerName = rs.getString("PROPOSER_NAME");
							String insuredName = rs.getString("INSURED_NAME");
							String employeeId = rs.getString("EMPLOYEE_ID");
							String paymentParty = rs.getString("PAYMENT_PARTY");
							String accountNo = rs.getString("ACCOUNT_NO");
							String bankName = rs.getString("BANK_NAME");
							String branchName = rs.getString("BRANCH_NAME");
							String ifscCode = rs.getString("IFSC_CODE");
							String insuredEmailId = rs.getString("INSURED_EMAIL_ID");
//							String accountHolder = rs.getString("NAME_OF_ACCOUNT_HOLDER");
							
							resultMap.put(SHAConstants.POLICY_NUMBER, linkPolicyNumber);
							resultMap.put(SHAConstants.PROPOSER_NAME, proposerName);
							resultMap.put(SHAConstants.INSURED_NAME, insuredName);
							resultMap.put(SHAConstants.EMPLOYEE_ID, employeeId);
							resultMap.put(SHAConstants.PAYMENT_PARTY, paymentParty);
							resultMap.put(SHAConstants.ACCOUNT_NO, accountNo);
							resultMap.put(SHAConstants.BANK_NAME, bankName);
							resultMap.put(SHAConstants.BRANCH_NAME, branchName);
							resultMap.put(SHAConstants.IFSC_CODE, ifscCode);
							resultMap.put(SHAConstants.INSURED_EMAIL_ID, insuredEmailId);
//							resultMap.put(SHAConstants.NAME_OF_ACCOUNT_HOLDER, accountHolder);
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
							}
					
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			
				return resultMap;
			
			}
	
	
			public Map<String, String> getProvisionUpdateValidation(Long stage, Long status, Long provisionAmt,Long policyKey,
					Long insuredKey, Long transactionKey, String transactionFlag) {

				Connection connection = null;
				CallableStatement cs = null;
				Map<String, String> result = new HashMap<String, String>();
				String alertFlag = "";
				String alertMsg = "";
				try {

					connection = BPMClientContext.getConnection();

					cs = connection.prepareCall("{call PRC_PROVISION_VALIDATION(?,?,?,?,?,?,?,?,?)}");
					cs.setLong(1, status);
					cs.setLong(2, stage);
					cs.setLong(3, provisionAmt);
					cs.setLong(4, policyKey);
					cs.setLong(5, insuredKey);
					cs.setLong(6, transactionKey);
					cs.setString(7, transactionFlag);
					cs.registerOutParameter(8, Types.VARCHAR);
					cs.registerOutParameter(9, Types.VARCHAR);
					cs.execute();

					if (cs.getString(8) != null) {
						alertFlag = cs.getString(8);
					}
					if (cs.getString(9) != null) {
						alertMsg = cs.getString(9);
					}
					result.put(SHAConstants.PROCESS_FLAG, alertFlag);
					result.put(SHAConstants.PROCESS_REMARKS, alertMsg);
				} 
				catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cs != null) {
							cs.close();
						}
						if (connection != null) {
							connection.close();
						}
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
							}
					
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return result;
			}
			
			private MasHospitals getMasHospitalById(Long key,EntityManager entityManager) {

				Query query = entityManager.createNamedQuery("MasHospitals.findByKey");
				query.setParameter("key", key);

				List<MasHospitals> resultList = (List<MasHospitals>) query.getResultList();

				if (resultList != null && !resultList.isEmpty()) {
					return resultList.get(0);
				}
				return null;

			}

	public MagazineDTO getDataFromMasMagazine() {
		MagazineDTO magazineInfo = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MAG_GAL_KEY, DOCUMENT_KEY, MAGAZINE_CODE, MAGAZINE_CATEGORY, MAGAZINE_SUB_CATEGORY, "
						+ "MAGAZINE_DESC, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, ACTIVE_FLAG "
						+ "FROM MAS_MAGAZINE_GALLERY WHERE ACTIVE_FLAG = 'Y'";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							magazineInfo = new MagazineDTO();
							magazineInfo.setMasMagKey(rs.getLong(1));
							magazineInfo.setMasDocKey(rs.getLong(2));
							magazineInfo.setMasMagCode(rs.getString(3));
							magazineInfo.setMasMagCategory(rs.getString(4));
							magazineInfo.setMasMagSubCategory(rs.getString(5));
							magazineInfo.setMasMagDesc(rs.getString(6));
							magazineInfo.setMasCreatedDate(rs.getString(7));
							magazineInfo.setMasCreatedBy(rs.getString(8));
							magazineInfo.setMasModifiedDate(rs.getString(9));
							magazineInfo.setMasModifiedBy(rs.getString(10));
							magazineInfo.setMasActiveFlag(rs.getString(11));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return magazineInfo;
	}

	public String getCMDMemberType(Long policykey) {

		String memberType = "";
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MEMBER_TYPE FROM VW_INTERMEDIARY WHERE CUSTOMER_CODE = (SELECT AGENT_CODE FROM IMS_CLS_POLICY WHERE POLICY_KEY = ?)";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, policykey);
				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							memberType = rs.getString(1);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberType;
	}

	public String getInermediaryDetails(String intermediaryCode) {

		String intermediaryName = "";
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT NAME FROM VW_INTERMEDIARY WHERE CUSTOMER_CODE = ?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, intermediaryCode);
				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							intermediaryName = rs.getString(1);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return intermediaryName;
	}
	@SuppressWarnings("null")
	public List<ViewNegotiationDetailsDTO> getNegotiationReport(
			java.sql.Date sqlFrmDate, java.sql.Date sqlToDate, Long stageId,
			String intimationNo, Long cpuId, String userId) {

		final String NEGOTIATION_REPORT = "{call  PRC_RPT_NEGOTIATION_AMT_DTLS(?,?,?,?,?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
		String frmDate = null;
		String toDate = null;
		if (sqlFrmDate != null) {
			frmDate = formatDate.format(sqlFrmDate);
		}
		if (sqlToDate != null) {
			toDate = formatDate.format(sqlToDate);
		}

		List<ViewNegotiationDetailsDTO> negReportList = new ArrayList<ViewNegotiationDetailsDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(NEGOTIATION_REPORT);
			cs.setString(1, frmDate);
			cs.setString(2, toDate);
			cs.setLong(3, null != stageId ? stageId.intValue() : 0l);
			cs.setString(4, intimationNo);
			cs.setLong(5, null != cpuId ? cpuId.intValue() : 0l);
			cs.setString(6, userId);
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();
			rset = (ResultSet) cs.getObject(7);

			if (rset != null) {
				ViewNegotiationDetailsDTO reportDtls = null;

				while (rset.next()) {
					reportDtls = new ViewNegotiationDetailsDTO();
					reportDtls.setIntimationNo(rset.getString("INTIMATION_NO"));
					reportDtls.setCashlessorReimNo(rset
							.getString("REFERENCE_NO"));
					reportDtls.setNegotiatedUpdateBy(rset
							.getString("USER_NAME"));
					reportDtls.setCpucodeName(rset.getString("CPU_CODE"));
					String negAmt = rset.getString("NEGOTIATED_AMOUNT");
					reportDtls.setNegotiatedAmt(Double.valueOf(negAmt));
					String savedAmt = rset.getString("SAVED_AMOUNT");
					reportDtls.setSavedAmt(Double.valueOf(savedAmt));
					String claimAppAmt = rset
							.getString("CLAIM_APPROVED_AMOUNT");
					reportDtls.setClaimApprovedAmt(Double.valueOf(claimAppAmt));
					reportDtls
							.setIntimationStage(rset.getString("CLAIM_STAGE"));
					reportDtls.setStatusValue(rset.getString("CLAIM_STATUS"));
					negReportList.add(reportDtls);
					reportDtls = null;

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return negReportList;
	}

	public BeanItemContainer<SelectValue> getSiRestricationValue(Long insuredkey) {

		Connection connection = null;
		CallableStatement cs = null;
		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_GET_PRVS_POLICY_SI(?,?)}");
			cs.setLong(1, insuredkey);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);

			/*
			 * if (null != rset) { // PreauthDTO dto = null; while (rset.next())
			 * { // dto = new PreauthDTO();
			 * //dto.setSumInsuredKey(rset.getLong("MASTER_KEY")); Long
			 * sumInsured = rset.getLong("SI"); String value =
			 * sumInsured.toString(); selectValue = new SelectValue(value);
			 * selectValue.setId(rset.getLong("MASTER_KEY"));
			 * selectValue.setValue(value); selectValuesList.add(selectValue);
			 * 
			 * }
			 */
			// handling empty and null for master key to avoid invalid column
			// name
			if (null != rset) {
				// PreauthDTO dto = null;
				while (rset.next()) {
					// dto = new PreauthDTO();
					// dto.setSumInsuredKey(rset.getLong("MASTER_KEY"));

					String masterKey = rset.getString("MASTER_KEY");
					if (masterKey != null && !masterKey.isEmpty()) {
						Long sumInsured = rset.getLong("SI");
						String value = sumInsured.toString();
						selectValue = new SelectValue(value);
						selectValue.setId(Long.valueOf(masterKey));

						selectValue.setValue(value);
						selectValuesList.add(selectValue);
					}
				}

				categoryContainer.addAll(selectValuesList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return categoryContainer;
	}

	public Map<Integer, Object> getUserProductivityCount(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<Integer, Object> countValues = new HashMap<Integer, Object>();
		Integer prodCount = 0;
		Integer claimCount = 0;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CLAIM_TRANS_CNT (?,?,?)}");

			cs.setString(1, userId);

			cs.registerOutParameter(2, Types.NUMERIC);
			cs.registerOutParameter(3, Types.NUMERIC);
			cs.execute();
			if (cs.getString(2) != null) {
				prodCount = cs.getInt(2);
			}
			if (cs.getObject(3) != null) {
				claimCount = cs.getInt(3);
			}

			countValues.put(1, prodCount);
			countValues.put(2, claimCount);

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return countValues;
	}

	public Map<String, Object> checkSTPProcess(Long cashlessKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> stpValues = new HashMap<String, Object>();
		Integer approvedAmt = 0;
		String stpFlag = null;
		String outCome = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_STP_BYPASS_PRAH_WSTP (?,?,?,?)}");

			cs.setLong(1, cashlessKey);

			cs.registerOutParameter(2, Types.NUMERIC);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(2) != null) {
				approvedAmt = cs.getInt(2);
			}
			if (cs.getObject(3) != null) {
				stpFlag = cs.getString(3);
			}
			if (cs.getObject(4) != null) {
				outCome = cs.getString(4);
			}

			stpValues.put("approveAmnt", approvedAmt);
			stpValues.put("stpFlag", stpFlag);
			stpValues.put("outCome", outCome);

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return stpValues;
	}

	/*
	 * p_ref_id IN NUMBER, p_claimno IN VARCHAR2 , p_provider_code IN VARCHAR2 ,
	 * p_claimed_amt IN NUMBER , p_hp_dt IN VARCHAR2, p_hosp_name IN VARCHAR2,
	 * p_file_type_id IN NUMBER , p_status OUT VARCHAR2
	 */

	// 0,intim.getIntimationId(),hosDetails.getProviderCode(),dateText,hosDetails.getHospitalName(),hosDetails.getFileTypeId()
	public String callHPPushGlx(Long argRefId, String argIntNo,
			String argProvCode, Long argClmAmt, String argHpDate,
			String argHosName, Long argFileTypId) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HP_PUSH_GLX(?,?,?,?,?,?,?,?)}");
			cs.setLong(1, argRefId);
			cs.setString(2, argIntNo);
			cs.setString(3, argProvCode);
			cs.setLong(4, argClmAmt);
			cs.setString(5, argHpDate);
			cs.setString(6, argHosName);
			cs.setLong(7, argFileTypId);
			cs.registerOutParameter(8, Types.VARCHAR);

			cs.execute();
			if (cs.getObject(8) != null) {
				output = cs.getString(8);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public Boolean getGMCInsuredDeleted(String policyNumber, Long memberId) {
		Connection connection = null;
		CallableStatement cs = null;
		Boolean output = false;
		String flag = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_RESIGNED_MEMEBER(?, ?, ?,?)}");
			cs.setString(1, policyNumber);
			cs.setLong(2, memberId);
			cs.setString(3, "D");
			System.out.println("----policy number----" + policyNumber);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(4) != null) {
				flag = cs.getString(4);
			}
			if (flag.equalsIgnoreCase("y")) {
				output = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	//
	public List<Map<String, Object>> GetTaskProcedureForClearCashless(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start PRC_SEC_CSH_CLEAR_GET_TASK "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_CSH_CLEAR_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle PRC_SEC_CSH_CLEAR_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End PRC_SEC_CSH_CLEAR_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public List<PortablitiyPolicyDTO> getPreviousPortabilityPolicy(
			String policyNumber, String insuredName) {

		List<PortablitiyPolicyDTO> portPolicy = new ArrayList<PortablitiyPolicyDTO>();

		final String SELECT_PORT_TABLE = "{call PRC_PREVS_PORT_POLICY(?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(SELECT_PORT_TABLE);
			cs.setString(1, policyNumber);
			cs.setString(2, insuredName);

			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				PortablitiyPolicyDTO portabilityPreviousPolicy = null;
				while (rs.next()) {

					portabilityPreviousPolicy = new PortablitiyPolicyDTO();

					portabilityPreviousPolicy.setInsurerName(rs
							.getString("INSURER_NAME"));
					portabilityPreviousPolicy.setSumInsured(rs
							.getLong("SUM_INSURED"));
					portabilityPreviousPolicy.setCumulativeBonus(rs
							.getLong("CUMULATIVE_BONUS"));
					Long uwYEAR = rs.getLong("UW_YEAR");
					String underWriteYear = Long.toString(uwYEAR);
					portabilityPreviousPolicy
							.setUnderwritingYear(underWriteYear);
					portabilityPreviousPolicy.setWaiver30Days(rs
							.getString("WAIVER_30DAYS".toString()));
					portabilityPreviousPolicy.setExclusionYear1(rs
							.getInt("EXCLUSION_1STYEAR"));
					portabilityPreviousPolicy.setExclusionYear2(rs
							.getInt("EXCLUSION_2NDYEAR"));
					portabilityPreviousPolicy.setPEDwaiver(rs
							.getString("PED_WAIVER"));
					portabilityPreviousPolicy.setDetailPEDAny(rs
							.getString("PED_DETAILS"));
					portabilityPreviousPolicy.setPolicyFrmDate(rs
							.getDate("POLICY_FM_DT"));
					portabilityPreviousPolicy.setPolicyToDate(rs
							.getDate("POLICY_TO_DT"));
					portabilityPreviousPolicy.setProductName(rs
							.getString("PRODUCT_NAME"));
					portabilityPreviousPolicy.setPolicyType(rs
							.getString("POLICY_TYPE"));
					portabilityPreviousPolicy.setPolicyNo(rs
							.getString("POLICY_NUMBER"));
					portabilityPreviousPolicy.setCustId(rs
							.getString("CUSTOMER_ID"));
					portabilityPreviousPolicy.setNoofClaims(rs
							.getInt("NO_OF_CLAIMS"));
					portabilityPreviousPolicy.setAmount(rs.getLong("AMOUNT"));
					portabilityPreviousPolicy.setNatureOfIllnessClaimPaid(rs
							.getString("NATURE_OF_ILLNESS"));
					portabilityPreviousPolicy.setCurrentPolicyNo(rs
							.getString("CUR_POLICY_NUMBER"));
					portabilityPreviousPolicy.setInsuredName(rs
							.getString("INSURED_NAME"));

					portPolicy.add(portabilityPreviousPolicy);
					portabilityPreviousPolicy = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return portPolicy;
	}

	public void callReverseFeed(String intimationNo, String cashlessNo,
			String createdBy) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CLEAR_CASHLESS_REVERSE_FD(?,?,?)}");
			cs.setString(1, intimationNo);
			cs.setString(2, cashlessNo);
			cs.setString(3, createdBy);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public SearchCVCTableDTO getCVCAuditData(String userId, String claimType,String productList ,Long productFlag) {

		final String CREATE_BATCH_PROC = "{call PRC_CVC_AUTO_GET_TASK(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		SearchCVCTableDTO cvcTableDto = null;
		try {
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			/*ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_CVC_AUTO_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, productList);
			*/
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, userId);
			cs.setString(2, claimType);
			cs.setString(3,productList);
			cs.setLong(4,productFlag);
			
			// cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(5, Types.NUMERIC);
			cs.registerOutParameter(6, Types.NUMERIC);
			cs.registerOutParameter(7, Types.NUMERIC);
			cs.registerOutParameter(8, Types.NUMERIC);
			cs.registerOutParameter(9, Types.NUMERIC);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.registerOutParameter(11, Types.NUMERIC);

			cs.execute();

			/*
			 * rs = (ResultSet) cs.getObject(3);
			 * 
			 * if (rs != null) { while (rs.next()) {
			 * 
			 * cvcTableDto = new SearchCVCTableDTO();
			 * 
			 * cvcTableDto.setIntimationKey(rs.getLong("INTIMATION_KEY"));
			 * cvcTableDto.setClaimKey(rs.getLong("CLAIM_KEY"));
			 * cvcTableDto.setTransactionKey(rs.getLong("TRANSACTION_KEY"));
			 * cvcTableDto.setClaimType(rs.getString("CALIM_TYPE")); } }
			 */
			cvcTableDto = new SearchCVCTableDTO();
					
				if (cs.getObject(5) != null) {
				cvcTableDto.setTransactionKey(Long.valueOf(cs.getInt(5)));
				}
				if (cs.getObject(6) != null) {
				cvcTableDto.setClaimKey(Long.valueOf(cs.getInt(6)));
				}
				if (cs.getObject(7) != null) {
				cvcTableDto.setIntimationKey(Long.valueOf(cs.getInt(7)));
				}
				if (cs.getObject(10) != null) {
				cvcTableDto.setClaimType(cs.getString(10));
				}
				if (cs.getObject(11) != null) {
				cvcTableDto.setStageKey(Long.valueOf(cs.getInt(11)));
				}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cvcTableDto;

	}

	public String getHospIcdMappingAvailable(String hospCode, Long icdCodeKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ICD_MAPPING_ALERT(?,?,?)}");
			cs.setString(1, hospCode);
			cs.setLong(2, icdCodeKey);
			cs.registerOutParameter(3, Types.VARCHAR);

			cs.execute();
			if (cs.getObject(3) != null) {
				output = cs.getString(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	public Map<String, String> getPermanentExclusionsIcdMapping(String icdCode,Long prodKey,String prodCode,String policyNo,String insuredNo,String commonIP) {
		Connection connection = null;
		CallableStatement cs = null;
		String flag="";
		String remarks="";
		Map<String, String> excludedicdcode = new HashMap<String, String>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ICD_EXCLUSION_ALERT(?,?,?,?,?,?,?,?)}");
			cs.setString(1, icdCode != null ? icdCode.trim() : "");
			cs.setLong(2,prodKey);
			cs.setString(3,prodCode);
			cs.setString(4,policyNo);
			cs.setString(5,insuredNo);
			cs.setString(6,commonIP);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(7) != null) {
				flag = cs.getString(7);
			}
			if (cs.getObject(8) != null) {
				remarks = cs.getString(8);
			}
			
			excludedicdcode.put("flag", flag);
			excludedicdcode.put("remarks", remarks);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return excludedicdcode;
	}

	public String getInvPending(Long policyKey, Long insuredKey,
			Long intimationKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PENDING_INVESTIGATION(?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.registerOutParameter(4, Types.VARCHAR);

			cs.execute();
			if (cs.getObject(4) != null) {
				output = cs.getString(4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/*
	 * public String getAnyClaimsAvailable(Long policyKey, Long insuredKey, Long
	 * intimationKey) { Connection connection = null; CallableStatement cs =
	 * null; String output = ""; try { connection =
	 * BPMClientContext.getConnection(); cs = connection
	 * .prepareCall("{call PRC_MULTIPLE_CLAIMS(?,?,?,?)}"); cs.setLong(1,
	 * policyKey); cs.setLong(2, insuredKey); cs.setLong(3, intimationKey);
	 * cs.registerOutParameter(4, Types.VARCHAR);
	 * 
	 * cs.execute(); if (cs.getObject(4) != null) { output = cs.getString(4); }
	 * } catch (SQLException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.close(); } if (cs != null) {
	 * cs.close(); } } catch (SQLException e) { e.printStackTrace(); } } return
	 * output; }
	 */

	public String getClaimRestrictionAvailable(Long policyKey, Long insuredKey,
			Long intimationKey, String icdCode, String diag) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_MULTIPLE_CLAIMS_RESTRICT(?,?,?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setString(4, icdCode != null ? icdCode.trim() : "");
			cs.setString(5, diag != null ? diag.trim() : "");
			cs.registerOutParameter(6, Types.VARCHAR);

			cs.execute();
			if (cs.getObject(6) != null) {
				output = cs.getString(6);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public List<ViewSeriousDeficiencyDTO> getSDIntimationListByHospitalCode(
			String hospitalCode) {
		Connection connection = null;
		CallableStatement cs = null;
		List<ViewSeriousDeficiencyDTO> sdIntimationList = new ArrayList<ViewSeriousDeficiencyDTO>();
		ResultSet rs = null;

		try {
			// check v8 update
			// connection = BPMClientContext.getConnectionFromURL();
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SERIOUS_DEF_INTM(?,?)}");
			cs.setString(1, hospitalCode);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				while (rs.next()) {
					ViewSeriousDeficiencyDTO newSD = new ViewSeriousDeficiencyDTO();
					if (rs.getString("INTIMATION_NUMBER") != null) {
						newSD.setIntimationNumber(rs
								.getString("INTIMATION_NUMBER"));
						newSD.setHospitalCode(hospitalCode);
						sdIntimationList.add(newSD);
						newSD = null;
					}
					// (rs.getString("CLIENT"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sdIntimationList;
	}

	public String getSeriousDeficiency(String hospitalCode) {

		Connection connection = null;
		CallableStatement cs = null;
		String seriousDeficiency = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SERIOUS_DEFICIENCY(?,?)}");
			cs.setString(1, hospitalCode);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				seriousDeficiency = cs.getString(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seriousDeficiency;
	}

	public RawInvsDetails getRawDetailsByInitmationNo(String intimationNumber,
			EntityManager entityManager) {
		Query query = entityManager
				.createNamedQuery("RawInvsDetails.findByIntimationNo");
		query.setParameter("intimationNo", intimationNumber);
		List<RawInvsDetails> resultList = (List<RawInvsDetails>) query
				.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (RawInvsDetails r : resultList) {
				if (r.getRequestedStatus() == 251) {
					return r;
				}
			}
			return resultList.get(0);
		}
		return null;

	}

	// CR2019058 New get task method for assign investigation
	public List<Map<String, Object>> revisedAssignInvestigationGetTaskProcedure(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start PRC_SEC_CSH_INV_GET_TASK "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_INV_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_CSH_INV_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle PRC_SEC_CSH_INV_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils
							.getRevisedAssignInvestigationObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End PRC_SEC_CSH_INV_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	// CR2019058 New Submit task for Assign Investigation feature
	public String revisedAssignInvestigationTaskProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		String successMsg = "";
		try {

			// connection = BPMClientContext.getConnectionFromURL();
			// connection = getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_INV_SUBMIT_TASK", conn);
			/* "TYP_SEC_CSH_ROD_SUBMIT_TASK1", connection); */

			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_CSH_INV_SUBMIT_TASK (?,?)}");
			/* .prepareCall("{call PRC_SEC_CSH_ROD_SUBMIT_TASK_pd (?,?)}"); */
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println("---THE STRING---" + cs.getString(2));

			if (cs.getString(2) != null) {
				successMsg = cs.getNString(2).toString();
				// return successMsg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}

	public List<CallcenterDashBoardReportDto> getCallCenterDashBoard(
			Long statusId, Date toDate, Date fromDate, String polNo,
			String intimationNo, String claimNo, String hosName, String type) {

		List<CallcenterDashBoardReportDto> callCenterDashBoardReportData = new ArrayList<CallcenterDashBoardReportDto>();
		java.sql.Date sqlToDate = null;
		java.sql.Date sqlFromDate = null;
		if (toDate != null) {
			sqlToDate = new java.sql.Date(toDate.getTime());
		}
		if (fromDate != null) {
			sqlFromDate = new java.sql.Date(fromDate.getTime());
		}

		final String SELECT_CALLCENTER_TABLE = "{call PRC_RPT_CALL_CENTER_DASHBOARD(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(SELECT_CALLCENTER_TABLE);
			cs.setLong(1, statusId);
			cs.setDate(2, sqlToDate);
			cs.setDate(3, sqlFromDate);
			cs.setString(4, polNo);
			cs.setString(5, intimationNo);
			cs.setString(6, claimNo);
			cs.setString(7, hosName);
			cs.setString(8, type);
			cs.registerOutParameter(9, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(9);

			if (rs != null) {
				CallcenterDashBoardReportDto callCenterDashBoardReport = null;
				while (rs.next()) {

					callCenterDashBoardReport = new CallcenterDashBoardReportDto();

					callCenterDashBoardReport.setIntimationNo(rs
							.getString("INTIMATION_NUMBER"));
					callCenterDashBoardReport.setPolicyNumber(rs
							.getString("POLICY_NUMBER"));
					callCenterDashBoardReport.setClaimNo(rs
							.getString("CLAIM_NUMBER"));
					callCenterDashBoardReport.setPatientName(rs
							.getString("INSURED_NAME"));
					callCenterDashBoardReport.setIntimatorName(rs
							.getString("INTIMATED_NAME"));
					callCenterDashBoardReport.setHospitalName(rs
							.getString("HOSPITAL_NAME"));
					callCenterDashBoardReport.setIntimationMode(rs
							.getString("MODE"));
					callCenterDashBoardReport.setIntimatedBy(rs
							.getString("MODE_BY"));
					callCenterDashBoardReport.setStatus(rs.getString("STATUS"));
					callCenterDashBoardReport.setIntimationDate(rs
							.getString("CREATED_DATE"));

					callCenterDashBoardReportData
							.add(callCenterDashBoardReport);
					callCenterDashBoardReport = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return callCenterDashBoardReportData;
	}

	public String getTopupPolicyIntimation(String polNo, String intimationNo,
			Long intimationKey, Long topPolicyKey, Long topInsuredKey,
			Long topInsuredId) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_TOPUP_POL_AUTO_CREATE_INTM(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, polNo);
			cs.setString(2, intimationNo);
			cs.setLong(3, intimationKey);
			cs.setLong(4, topPolicyKey);
			cs.setLong(5, topInsuredKey);
			cs.setLong(6, topInsuredId);

			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(7) != null) {
				output = (String) cs.getObject(7).toString();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public String getInsuredPatientStatus(Long policyKey, Long insuredKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_INS_PATIENT_ALERT(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();
			if (cs.getObject(3) != null) {
				output = (String) cs.getObject(3).toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public List<PreauthDTO> getTopUpPolicyDetailsForRiskName(String policyNo,
			Long insuredNo, Long insuredKey, Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;

		List<PreauthDTO> list = new ArrayList<PreauthDTO>();

		// List<Map<String, Object>> list = new ArrayList<Map<String,
		// Object>>();

		try {
			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_TOPUP_POL_INSURED_DTLS (?,?,?,?,?,?,?)}");
			cs.setString(1, policyNo);
			cs.setLong(2, insuredNo);
			cs.setLong(3, insuredKey);
			cs.setLong(4, intimationKey);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();

			rs = (ResultSet) cs.getObject(5);

			if (rs != null) {
				PreauthDTO mappedValues = null;
				while (rs.next()) {
					mappedValues = new PreauthDTO();
					mappedValues.setTopUpInsuredName(rs
							.getString("INSURED_NAME"));
					mappedValues.setTopUpInsuredNo(rs.getLong("RISK_ID"));
					if (cs.getObject(6) != null && cs.getObject(7) != null) {
						String topUpProposerName = (String) cs.getObject(6)
								.toString();
						String topUpPolNO = (String) cs.getObject(7).toString();
						mappedValues.setTopUpProposerName(topUpProposerName);
						mappedValues.setTopUPPolicyNumber(topUpPolNO);
					}
					list.add(mappedValues);
				}

				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<String> getTopUpAlertFlag(String policyNo) {

		Connection connection = null;
		CallableStatement cs = null;
		ArrayList<String> topupPolicyList = new ArrayList<String>();
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_TOPUP_POLICY_STATUS(?,?,?,?)}");
			cs.setString(1, policyNo);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getString(2) != null) {
				topupPolicyList.add(cs.getString(2)) ;
			}
			if (cs.getString(3) != null) {
				topupPolicyList.add(cs.getString(3)) ;
			}
			if (cs.getString(4) != null) {
				topupPolicyList.add(cs.getString(4)) ;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return topupPolicyList;
	}
	
	public List<String> getSIRestrication(String intimationNo,
			Long intimationKey, Long policyKey) {
		List<String> si_restrict = new ArrayList<String>();
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_SI_RESTRICT_ALERT_FOR_OC(?, ?, ?, ?, ?)}");
			cs.setString(1, intimationNo);
			cs.setLong(2, intimationKey);
			cs.setLong(3, policyKey);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();
			rs = (ResultSet) cs.getObject(5);

			if (cs.getString(4) != null) {
				si_restrict.add(cs.getString(4).toString());
			}
			if (cs.getObject(5) != null) {
				if (rs != null) {
					while (rs.next()) {
						if (rs.getString("ALERT") != null) {
							si_restrict.add(rs.getString("ALERT"));
						}
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return si_restrict;
	}

	public Map<String, Object> getAgentAndBranchName(String intimationNo) {
		final String AGENT_BRANCH_NAME = "{call PRC_OFF_AGENT_CLR_CODE(?, ?, ?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet agentResultSet = null;
		ResultSet branchResultSet = null;
		ResultSet smResultSet = null;
		Map<String, Object> outputValues = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(AGENT_BRANCH_NAME);
			cs.setString(1, intimationNo);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			agentResultSet = (ResultSet) cs.getObject(2);
			branchResultSet = (ResultSet) cs.getObject(3);
			smResultSet = (ResultSet) cs.getObject(4);
			if (agentResultSet != null && branchResultSet != null && smResultSet != null)
				while (agentResultSet.next() && branchResultSet.next() && smResultSet.next()) {
					String agentColor = agentResultSet
							.getString("HEXA_CLR_VALUE");
					String agentPoints = agentResultSet.getString("ICR_VALUE");
					String agentPrvPoint = agentResultSet.getString("CHNL_PRV_ICR_VALUE");
					String branchColor = branchResultSet
							.getString("HEXA_CLR_VALUE");
					String branchPoints = branchResultSet
							.getString("ICR_VALUE");
					String smPoints = smResultSet
							.getString("SM_VALUE");
					String smColor = smResultSet
							.getString("HEXA_CLR_VALUE");
					String smPrvPoints = smResultSet
							.getString("SM_PRV_VALUE");

					outputValues = new HashMap<String, Object>();
					outputValues.put(SHAConstants.ICR_AGENT_COLOR, agentColor);
					outputValues.put(SHAConstants.ICR_AGENT_POINT, agentPoints);
					outputValues.put(SHAConstants.AGENT_PRV_POINT, agentPrvPoint);
					outputValues
							.put(SHAConstants.ICR_BRANCH_COLOR, branchColor);
					outputValues.put(SHAConstants.ICR_BRANCH_POINT,
							branchPoints);
					outputValues.put(SHAConstants.SM_AGENT_POINT, smPoints);
					outputValues.put(SHAConstants.SM_COLOUR_CODE, smColor);
					outputValues.put(SHAConstants.SM_AGENT_PRV_POINT, smPrvPoints);
				}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (agentResultSet != null) {
					agentResultSet.close();
				}
				if (branchResultSet != null) {
					branchResultSet.close();
				}
				if (smResultSet != null) {
					smResultSet.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return outputValues;
	}

	public List<String> getPreviousClaimInvestigatedAlert(Long policyKey,
			Long intimationKey) {
		List<String> prev_claimInvestigation = new ArrayList<String>();
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PREVIUOS_CLAIM_INV_ALERT(?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, intimationKey);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();
			rs = (ResultSet) cs.getObject(4);

			if (cs.getString(3) != null) {
				prev_claimInvestigation.add(cs.getString(3).toString());
			}
			if (cs.getObject(4) != null) {
				if (rs != null) {
					while (rs.next()) {
						if (rs.getString("ALERT") != null) {
							prev_claimInvestigation.add(rs.getString("ALERT"));
						}
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return prev_claimInvestigation;
	}

	public Map<String, String> getOPUserValidation(String userCode, String officeCode) {
		
		Connection connection = null;
		CallableStatement cs= null;
		ResultSet rs = null;
	    String status = null;
	    String remarks = null;
		Map<String, String> values = new HashMap<String, String>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_OP_USER_BRANCH_MAPPING (?, ?, ?, ?, ?)}");
			cs.setString(1, userCode.toUpperCase());
			cs.setString(2, officeCode);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();

			status = cs.getString(3);
			remarks = cs.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.STATUS, status != null ? status : null);
		values.put(SHAConstants.REMARKS, remarks != null ? remarks : null);
		return values;
	}
	
	public Map<String, Object> getHospitalScorePoints(String hospitalCode) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> preAuthDTO = null;
		ResultSet rs = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_HOSPITAL_SCORE(?,?,?,?)}");
			cs.setString(1, hospitalCode);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			String hospScore = (String) cs.getString(2);
			String scoreColor = (String) cs.getString(3);
			String colorId = (String) cs.getString(4);
			if (hospScore != null && cs.getString(3) != null
					&& cs.getString(4) != null) {
				preAuthDTO = new HashMap<String, Object>();
				preAuthDTO.put(SHAConstants.HOSPITAL_SCORE_COLOR, colorId);
				preAuthDTO.put(SHAConstants.HOSPITAL_SCORE_POINTS, hospScore);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return preAuthDTO;
	}

	public SearchCVCAuditActionTableDTO getCVCAuditActionData(String userId,
			String claimType) {

		final String CREATE_BATCH_PROC = "{call PRC_CVC_AUTO_GET_TASK(?, ?, ?, ?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		SearchCVCAuditActionTableDTO cvcTableDto = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, userId);
			cs.setString(2, claimType);

			// cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.NUMERIC);
			cs.registerOutParameter(4, Types.NUMERIC);
			cs.registerOutParameter(5, Types.NUMERIC);
			cs.registerOutParameter(6, Types.NUMERIC);
			cs.registerOutParameter(7, Types.NUMERIC);
			cs.registerOutParameter(8, Types.VARCHAR);
			// cs.registerOutParameter(9,Types.NUMERIC);

			cs.execute();

			/*
			 * rs = (ResultSet) cs.getObject(3);
			 * 
			 * if (rs != null) { while (rs.next()) {
			 * 
			 * cvcTableDto = new SearchCVCTableDTO();
			 * 
			 * cvcTableDto.setIntimationKey(rs.getLong("INTIMATION_KEY"));
			 * cvcTableDto.setClaimKey(rs.getLong("CLAIM_KEY"));
			 * cvcTableDto.setTransactionKey(rs.getLong("TRANSACTION_KEY"));
			 * cvcTableDto.setClaimType(rs.getString("CALIM_TYPE")); } }
			 */
			cvcTableDto = new SearchCVCAuditActionTableDTO();
			if (cs.getObject(3) != null) {
				cvcTableDto.setTransactionKey(Long.valueOf(cs.getInt(3)));
			}
			if (cs.getObject(4) != null) {
				cvcTableDto.setClaimKey(Long.valueOf(cs.getInt(4)));
			}
			if (cs.getObject(5) != null) {
				cvcTableDto.setIntimationKey(Long.valueOf(cs.getInt(5)));
			}
			if (cs.getObject(8) != null) {
				cvcTableDto.setClaimType(cs.getString(8));
			}
			/*
			 * if (cs.getObject(9) != null) {
			 * cvcTableDto.setAuditCompleted(cs.getInt(9)); }
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cvcTableDto;

	}

	public SearchCVCTableDTO getCVCAuditDataForIntimationNumber(String userId,
			String intimationNo,String year) {

		final String CREATE_BATCH_PROC = "{call PRC_CVC_GET_INTIMATION(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)}";
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		SearchCVCTableDTO cvcTableDto = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, userId);
			cs.setString(2, intimationNo);
			cs.setString(3, year);

			// cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
//			cs.registerOutParameter(3, Types.NUMERIC);
			cs.registerOutParameter(4, Types.NUMERIC);
			cs.registerOutParameter(5, Types.NUMERIC);
			cs.registerOutParameter(6, Types.NUMERIC);
			cs.registerOutParameter(7, Types.NUMERIC);
			cs.registerOutParameter(8, Types.NUMERIC);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.registerOutParameter(11, Types.NUMERIC);

			cs.execute();

			/*
			 * rs = (ResultSet) cs.getObject(3);
			 * 
			 * if (rs != null) { while (rs.next()) {
			 * 
			 * cvcTableDto = new SearchCVCTableDTO();
			 * 
			 * cvcTableDto.setIntimationKey(rs.getLong("INTIMATION_KEY"));
			 * cvcTableDto.setClaimKey(rs.getLong("CLAIM_KEY"));
			 * cvcTableDto.setTransactionKey(rs.getLong("TRANSACTION_KEY"));
			 * cvcTableDto.setClaimType(rs.getString("CALIM_TYPE")); } }
			 */
			cvcTableDto = new SearchCVCTableDTO();
			if (cs.getObject(4) != null) {
				cvcTableDto.setTransactionKey(Long.valueOf(cs.getInt(4)));
			}
			if (cs.getObject(5) != null) {
				cvcTableDto.setClaimKey(Long.valueOf(cs.getInt(5)));
			}
			if (cs.getObject(6) != null) {
				cvcTableDto.setIntimationKey(Long.valueOf(cs.getInt(6)));
			}
			if (cs.getObject(9) != null) {
				cvcTableDto.setClaimType(cs.getString(9));
			}
			if (cs.getObject(10) != null) {
				cvcTableDto.setMessage(cs.getString(10));
			}
			if (cs.getObject(11) != null) {
				cvcTableDto.setStageKey(Long.valueOf(cs.getInt(11)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cvcTableDto;

	}

	public List<VerificationAccountDeatilsTableDTO> getAccountDetailsForPaymentVerification(String accountNumber, String payeeName, String ifscCode) {

		final String CREATE_BATCH_PROC = "{call PRC_GET_PAYMENT_INFO_ACNO(?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		//List<List<VerificationAccountDeatilsTableDTO>> resultList = new ArrayList<List<VerificationAccountDeatilsTableDTO>>();
		VerificationAccountDeatilsTableDTO verificationTableDto = null;
		List<VerificationAccountDeatilsTableDTO> verificationTableDtoList = new ArrayList<VerificationAccountDeatilsTableDTO>();
		//List<VerificationAccountDeatilsTableDTO> paidAccTableDtoList = new ArrayList<VerificationAccountDeatilsTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, accountNumber);
			cs.setString(2, payeeName);
			cs.setString(3, ifscCode);
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			
			rs = (ResultSet) cs.getObject(5);
			if (rs != null) {

				while (rs.next()) {
					verificationTableDto = new VerificationAccountDeatilsTableDTO();
					verificationTableDto.setClaimNumber(rs.getString(1));
					verificationTableDto.setPolicyNumber(rs.getString(3));
					verificationTableDto.setRodNumber(rs.getString(4));
					verificationTableDto.setRodKey(rs.getLong(5));
					verificationTableDto.setInsuredName(rs.getString(7));
					verificationTableDto.setAccountNumber(rs.getString(8));
					verificationTableDto.setIfscCode(rs.getString(9));
					verificationTableDto.setPaidAmount(rs.getString(10));
					verificationTableDto.setPayeeName(rs.getString(11));
					verificationTableDtoList.add(verificationTableDto);
				}
			}
			//resultList.add(verificationTableDtoList);
			
			/*rs = null;
			rs = (ResultSet) cs.getObject(4);
			if (rs != null) {

				while (rs.next()) {
					verificationTableDto = new VerificationAccountDeatilsTableDTO();
					verificationTableDto.setClaimNumber(rs.getString(1));
					verificationTableDto.setPolicyNumber(rs.getString(3));
					verificationTableDto.setRodNumber(rs.getString(4));
					verificationTableDto.setRodKey(rs.getLong(5));
					verificationTableDto.setInsuredName(rs.getString(7));
					verificationTableDto.setAccountNumber(rs.getString(8));
					verificationTableDto.setIfscCode(rs.getString(9));
					verificationTableDto.setPaidAmount(rs.getString(10));
					verificationTableDto.setPayeeName(rs.getString(11));
					paidAccTableDtoList.add(verificationTableDto);
				}
			}
			resultList.add(paidAccTableDtoList);*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return verificationTableDtoList;

	}
	
	public String getFirstPolicyNo(String argCurrentPolicyNo) {
		String firstPolicyNo = "";
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_FETCH_POLICY_TVC(?,?)}");
			cs.setString(1, argCurrentPolicyNo);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			firstPolicyNo = (String) cs.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return firstPolicyNo;
	}

	public List<AddOnBenefitsDTO> getAddOnBenefitsValuesPhc(
			Long reimbursementKey, Long policyKey, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;
		List<AddOnBenefitsDTO> benefitsValues = new ArrayList<AddOnBenefitsDTO>();
		try {
			// final String typeName = "OBJ_SUB_LIM";
			final String typeTableName = "TYP_HOSCASH";

			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PHC_BENEFITS_CALC(?, ?, ?,?)}");
			cs.setLong(1, reimbursementKey);
			cs.setLong(2, policyKey);
			cs.setLong(3, insuredKey);
			cs.registerOutParameter(4, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(4);

			if (object != null) {
				Object[] data = (Object[]) ((Array) object).getArray();
				Struct row = null;
				Object[] attributes = null;
				AddOnBenefitsDTO benefitDTo = null;
				for (Object tmp : data) {
					row = (Struct) tmp;
					attributes = row.getAttributes();
					// Map<Integer, Object> benefitsMap = new HashMap<Integer,
					// Object>();

					benefitDTo = new AddOnBenefitsDTO();
					benefitDTo
							.setPhcBenefitId(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[0])));

					benefitDTo.setParticulars(String.valueOf(attributes[1]));

					benefitDTo
							.setEntitledNoOfDays(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[2])));

					benefitDTo
							.setNoOfDaysPerHospitalization(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[2])));

					benefitDTo
							.setEntitlementPerDayAmt(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[3])));

					benefitDTo
							.setUtilizedNoOfDays(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[4])));

					benefitDTo
							.setBalanceAvailable(SHAUtils
									.getIntegerFromString(String
											.valueOf(attributes[5])));

					/*
					 * for(int i = 0 ; i<attributes.length ; i++) {
					 * //benefitsMap.put(i,attributes[i]); benefitsValues.add(e)
					 * 
					 * } benefitsValues.add(benefitsMap);
					 */

					/*
					 * for (Object object2 : attributes) {
					 * benefitsValues.add(SHAUtils
					 * .getDoubleValueFromString(String.valueOf(object2))) ; }
					 */
					benefitsValues.add(benefitDTo);
				}
			}

			// return benefitsValues;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return benefitsValues;

	}
	
	public BeanItemContainer<SelectValue> getPayeeNameWithRelationship(Long policykey,Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		List <String> employeeName = new ArrayList<String>();
		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_PAYYEE_NAMES(?,?,?)}");		
			cs.setLong(1, policykey);
			cs.setLong(2, intimationKey);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			rset = (ResultSet) cs.getObject(3);

			if (null != rset) {					
				while (rset.next()) {
					String proposerName = rset.getString("PROPOSER_NAME");
					String employee = rset.getString("EMPLOYEE_NAME");
					String hospitalName = rset.getString("HOSPITAL_NAME");
					String employeeRelation = rset.getString("RELATIONSHIP_DESC");
					employeeName.add(proposerName);
					employeeName.add(employee);		
					
					SelectValue proposerSelValue = new SelectValue();
					proposerSelValue.setId(1l);
					proposerSelValue.setValue(proposerName);
					SelectValue employeeSelValue = new SelectValue();
					employeeSelValue.setId(2l);
					employeeSelValue.setValue(employee);
					employeeSelValue.setCommonValue(employeeRelation);
					SelectValue hospitalSelValue = new SelectValue();
					hospitalSelValue.setId(3l);
					hospitalSelValue.setValue(hospitalName);
					selectValuesList.add(proposerSelValue);
					selectValuesList.add(employeeSelValue);
					selectValuesList.add(hospitalSelValue);
				
				}
				categoryContainer.addAll(selectValuesList);	
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return categoryContainer;
}

	public Map<String, String> getFraudAlerts(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		String salesForceFlag = null;
		String salesForceMsg = null;
		Map<String,String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_FRAUD_CHECK (?, ?, ?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();
			salesForceFlag = (String) cs.getString(2);
			salesForceMsg = (String) cs.getString(3);
			resultMap.put(SHAConstants.FLAG, salesForceFlag);
			resultMap.put(SHAConstants.ALERT_MESSAGE, salesForceMsg);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}
	
	public Map<String, String> getCashlessFraudAlert(String intimationNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		String salesForceFlag = null;
		String salesForceMsg = null;
		Map<String,String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_FRAUD_CHECK_CASHLESS (?, ?, ?)}");
			cs.setString(1, intimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();
			salesForceFlag = (String) cs.getString(2);
			salesForceMsg = (String) cs.getString(3);
			resultMap.put(SHAConstants.FLAG, salesForceFlag);
			resultMap.put(SHAConstants.ALERT_MESSAGE, salesForceMsg);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	//CR2019214
	public Map<String, String> getApproveFlagDisable(String intimationNumber, String hospitalCode) {
		Connection connection = null;
		CallableStatement cs = null;
		String approveFlag = null;
		String approveMsg = null;
		Map<String,String> resultValue = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HOSP_STOP_PAYMENT_FA (?,?, ?, ?)}");
			cs.setString(1, intimationNumber);
			cs.setString(2, hospitalCode);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			approveFlag = (String) cs.getString(3);
			approveMsg = (String) cs.getString(4);
			resultValue.put(SHAConstants.FLAG, approveFlag);
			resultValue.put(SHAConstants.ALERT_MESSAGE, approveMsg);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
					}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultValue;
	}
	
	// CR2019199
		public String getCoInsuranceCheckValue(String poilcyNumber) {
			Connection connection = null;
			CallableStatement cs = null;
			String active = "";
			final String CALL_PROCEDURE = "{call PRC_POLICY_COINSURANCE_CHK(?,?)}";
			try {

				connection = BPMClientContext.getConnection();
				cs = connection.prepareCall(CALL_PROCEDURE);
				cs.setString(1, poilcyNumber);
				cs.registerOutParameter(2, Types.VARCHAR);
				cs.execute();

				if (cs.getObject(2) != null) {
					active = (String)cs.getString(2);
//					active = "Y";
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
					if (cs != null) {
						cs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return active;
		}

		// CR2019199
		public List<CoInsuranceTableDTO> getCoInsuranceDetails(String poilcyNumber) {
			Connection connection = null;
			CallableStatement cs = null;

			final String CALL_PROCEDURE = "{call PRC_POLICY_COINSURANCE(?,?)}";
			ResultSet rs = null;
			List<CoInsuranceTableDTO> coInsuranceData = new ArrayList<CoInsuranceTableDTO>();
			try {
				connection = BPMClientContext.getConnection();
				cs = connection.prepareCall(CALL_PROCEDURE);
				cs.setString(1, poilcyNumber);
//				cs.setString(1, "P/151118/01/2018/031");
				cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
				cs.execute();
				rs = (ResultSet) cs.getObject(2);
				if (rs != null) {
					while (rs.next()) {

						CoInsuranceTableDTO setCoInsuranceListValue = new CoInsuranceTableDTO();
						setCoInsuranceListValue.setSequenceNumber(rs
								.getInt("SEQ_NO"));
						setCoInsuranceListValue.setShareType(rs
								.getString("SHARE_TYPE"));
						setCoInsuranceListValue.setInsuranceName((rs
								.getString("INSURANCE_NAME")));
						setCoInsuranceListValue.setSharePercentage(rs
								.getDouble("SHARE_PERCENTAGE"));
						coInsuranceData.add(setCoInsuranceListValue);
					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cs != null) {
						cs.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return coInsuranceData;
		}
		
		public List<Map<String, Object>> revisedGetTaskProcedureForCommonBillingAndFA(Object[] input) {
			
			Connection connection = null;
			CallableStatement cs = null;
			OracleConnection conn= null;
			ResultSet rs = null;
		
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
			System.out.println("--- Start TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());
			try {
		
				connection = BPMClientContext.getConnection();
				conn = connection.unwrap(OracleConnection.class);
		
				ArrayDescriptor des = ArrayDescriptor.createDescriptor(
						"TYP_SEC_CSH_ROD_GET_TASK", conn);					
				ARRAY arrayToPass = new ARRAY(des, conn, input);

				cs = conn
						.prepareCall("{call PRC_SEC_CSH_ROD_BLMFA_GET_TASK (?,?,?)}");
				cs.setArray(1, arrayToPass);
		
				cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
				cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
				cs.execute();
				System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
						+ System.currentTimeMillis());
		
				rs = (ResultSet) cs.getObject(2);
				Integer totalCount = (Integer) cs.getObject(3);
				if (rs != null) {
					Map<String, Object> mappedValues = null;
					while (rs.next()) {
						mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
						mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);
		
						list.add(mappedValues);
					}
					System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
							+ System.currentTimeMillis());
					// return list;
				}
		
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
		
					if (rs != null) {
						rs.close();
					}
					if (cs != null) {
						cs.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return list;
		
		}
		
	public List<Map<String, Object>> revisedGetTaskProcedureForUndoFAandLot(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn= null;
		ResultSet rs = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start TYP_SEC_CSH_ROD_GET_TASK "
				+ System.currentTimeMillis());
		try {
			
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);					
			ARRAY arrayToPass = new ARRAY(des, conn, input);

			cs = conn
					.prepareCall("{call PRC_SEC_CSH_ROD_UNFA_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public Policy getPolicyObject(String policyNo)
	{
		Policy dto = null;
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String fetchQuery = "select a.POLICY_SOURCE from IMS_CLS_POLICY a where a.POLICY_NUMBER = ?";

				PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
				preparedStatement.setString(1,policyNo);

				if(null != preparedStatement)
				{
					ResultSet rs = preparedStatement.executeQuery();
					if(null != rs)
					{
						while (rs.next()) {
							dto = new Policy();
							dto.setPolicySource(rs.getString(1));
						}
					}
				}

			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dto;

	}
	
	public IntimationDto getIntimationObject(String intimationNo)
	{
		IntimationDto dto = null;
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String fetchQuery = "select a.INTIMATION_NUMBER,a.POLICY_NUMBER,b.POLICY_SOURCE,c.CLAIM_NUMBER from IMS_CLS_INTIMATION a inner join IMS_CLS_POLICY b on a.POLICY_KEY = b.POLICY_KEY and a.INTIMATION_NUMBER=? inner join IMS_CLS_CLAIM c on a.INTIMATION_KEY = c.INTIMATION_KEY";
				PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
				preparedStatement.setString(1,intimationNo);

				if(null != preparedStatement)
				{
					ResultSet rs = preparedStatement.executeQuery();
					if(null != rs)
					{
						while (rs.next()) {
							dto = new IntimationDto();
							dto.setIntimationNumber(rs.getString(1));
							dto.setPolicyNumber(rs.getString(2));
							dto.setPolicySource(rs.getString(3));
							dto.setClaimNumber(rs.getString(4));
						}
					}
				}

			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dto;

	}

	public void insertLock(String policyNumber, String intimationId,
			String claimId, String ltype) {
		IntimationDto dto = null;
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String insertQuery = "INSERT INTO IMS_CLS_WS_LOCK_POLICY (POLICY_NUMBER,INTIMATION_NUMBER,CLAIM_NUMBER,L_TYPE) VALUES (?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
				preparedStatement.setString(1,policyNumber);
				preparedStatement.setString(2,intimationId);
				preparedStatement.setString(3,claimId);
				preparedStatement.setString(4,ltype);
				
				preparedStatement.execute();
				
			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void insertProvision(String policyNumber, String intimationId,
			String currentProvisionAmount, Timestamp timestamp, Long intimationKey,Long claimKey, String claimNumber) {
		IntimationDto dto = null;
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String insertQuery = "INSERT INTO IMS_CLS_SFX_PROVISION_AMT_HIS (GLX_SFX_PROV_KEY,POLICY_NUMBER,INTIMATION_NUMBER,CURRENT_PROVISION_AMT,MODIFIED_DATE,INTIMATION_KEY,CLAIM_KEY,CLAIM_NUMBER) VALUES (SEQ_GLX_SFX_PROV_KEY.nextval, ?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
				preparedStatement.setString(1,policyNumber);
				preparedStatement.setString(2,intimationId);
				preparedStatement.setString(3,currentProvisionAmount);
				preparedStatement.setTimestamp(4,timestamp);
				preparedStatement.setLong(5, intimationKey);
				preparedStatement.setLong(6, claimKey);
				preparedStatement.setString(7,claimNumber);
				preparedStatement.execute();
		
			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public IntimationDto getClaimObject(String intimationNo)
	{
		IntimationDto dto = null;
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String fetchQuery = "select CLAIM_KEY,CLAIM_NUMBER,INTIMATION_KEY from IMS_CLS_CLAIM where INTIMATION_NUMBER = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
				preparedStatement.setString(1,intimationNo);

				if(null != preparedStatement)
				{
					ResultSet rs = preparedStatement.executeQuery();
					if(null != rs)
					{
						while (rs.next()) {
							dto = new IntimationDto();
							dto.setClaimKey(rs.getLong(1));
							dto.setClaimNumber(rs.getString(2));
							dto.setIntimationKey(rs.getLong(3));
						}
					}
				}

			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dto;

	}
	
	public List<PreviousClaimsTableDTO> getBancsIntimationDetails(String policyNo,String healthCardNo,String insurdKey) {

		final String CREATE_CLAIMS_VIEW_PROC = "{call PRC_BANCS_GET_PREVIOUS_CLAIM(?, ?,?,?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		PreviousClaimsTableDTO previousclaimDto  = new PreviousClaimsTableDTO();
		List<PreviousClaimsTableDTO> claimsDtoList = new ArrayList<PreviousClaimsTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_CLAIMS_VIEW_PROC);
			cs.setString(1, policyNo);
			/*cs.setString(2, healthCardNo != null && !healthCardNo.isEmpty() ? healthCardNo : null);
			cs.setLong(3, insurdKey != null && !insurdKey.isEmpty() ? Long.valueOf(insurdKey) : 0l);*/
			cs.setString(2, healthCardNo != null && !healthCardNo.isEmpty() ? healthCardNo : "");
			cs.setString(3, insurdKey != null && !insurdKey.isEmpty() ? insurdKey : "");
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			
			rs = (ResultSet) cs.getObject(4);
			if (rs != null) {
				while (rs.next()) {
					previousclaimDto = new PreviousClaimsTableDTO();
					previousclaimDto.setPolicyNumber(rs
							.getString("POLICY_NUMBER"));
					previousclaimDto.setPolicyYear(rs.getString("POLICY_YEAR"));
					previousclaimDto.setClaimNumber(rs
							.getString("CLAIM_NUMBER"));
					previousclaimDto.setClaimType(rs.getString("CLAIM_TYPE"));
					previousclaimDto.setIntimationNumber(rs
							.getString("INTIMATION_NUMBER"));
					previousclaimDto.setBenefits(rs.getString("BENIFITS"));
					previousclaimDto.setInsuredPatientName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setInsuredName(rs
							.getString("INSURED_PATIENT_NAME"));
					previousclaimDto.setParentName(rs.getString("PARENT_NAME"));
					previousclaimDto.setParentDOB(rs.getDate("PARENT_DOB"));
					previousclaimDto.setDiagnosis(rs.getString("DIAGNOSIS"));
					previousclaimDto.setDiagnosisForPreviousClaim(rs
							.getString("ADMISSION_DATE"));
					previousclaimDto.setPatientDOB(rs.getDate("PATIENT_DOB"));
					if (rs.getDate("ADMISSION_DATE") != null) {
						Date admissionDate = rs.getDate("ADMISSION_DATE");
						String dateWithoutTime = SHAUtils
								.getDateWithoutTime(admissionDate);
						previousclaimDto.setAdmissionDate(dateWithoutTime);
					}
					previousclaimDto.setClaimStatus(rs
							.getString("CLAIM_STATUS"));
					previousclaimDto.setClaimAmount(String.valueOf(rs
							.getDouble("CLAIM_AMOUNT")));
					previousclaimDto.setApprovedAmount(String.valueOf(rs
							.getDouble("APPROVED_AMOUNT")));
					previousclaimDto.setCopayPercentage(rs
							.getDouble("COPAY_PERCENTAGE"));
					previousclaimDto.setHospitalName(rs
							.getString("HOSPITAL_NAME"));
					previousclaimDto.setPedName(rs.getString("PED_NAME"));
					previousclaimDto.setIcdCodes(rs.getString("ICD_CODE"));
					previousclaimDto.setKey(rs.getLong("CLAIM_KEY"));
					previousclaimDto.setIsSubLimitApplicable(rs
							.getString("SUBLIMIT_APPLICABLE"));
					if (rs.getString("SUBLIMIT_APPLICABLE") == null
							|| rs.getString("SUBLIMIT_APPLICABLE").equals("N,")) {
						previousclaimDto.setSubLimitApplicable("No");
					} else {
						previousclaimDto.setSubLimitApplicable("Yes");
					}
					previousclaimDto.setSubLimitName(rs
							.getString("SUBLIMIT_NAME"));
					previousclaimDto.setSubLimitAmount(rs
							.getString("SUBLIMIT_AMOUNT"));
					previousclaimDto.setForSIRest(rs
							.getString("SI_RESTRICATION_FLAG"));

					if (rs.getString("SI_RESTRICATION_FLAG").equals("Y")) {
						previousclaimDto.setSiRestrication("Yes");
					} else {
						previousclaimDto.setSiRestrication("No");
					}
					BPMClientContext bpmClientContext = new BPMClientContext();
					Map<String,String> tokenInputs = new HashMap<String, String>();
					 tokenInputs.put("intimationNo", previousclaimDto.getIntimationNumber());
					 String intimationNoToken = null;
					  try {
						  intimationNoToken = createJWTTokenForClaimStatusPages(tokenInputs);
					  } catch (NoSuchAlgorithmException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (ParseException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					  tokenInputs = null;
					String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
					/*Below code commented due to security reason
					String url = bpmClientContext.getGalaxyDMSUrl() + previousclaimDto.getIntimationNumber();*/
					if(url != null){
						previousclaimDto.setDmsUrlForBancs(url);
					}
					claimsDtoList.add(previousclaimDto);
					previousclaimDto = null;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return claimsDtoList;

	}
	
	public List<PreviousAccountDetailsDTO> getOPPreviousAccountDetails(
			Long insuredNumber, Long documentReceivedId) {

		List<PreviousAccountDetailsDTO> previous = new ArrayList<PreviousAccountDetailsDTO>();

		final String PREVIOUS_ACC_DETAIL_PROC = "{call PRC_OP_GMC_HOS_DTLS_NEW(?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PREVIOUS_ACC_DETAIL_PROC);
			cs.setLong(1, insuredNumber);
//			cs.setLong(2, documentReceivedId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				PreviousAccountDetailsDTO previousAcntDetailsDto = null;
				while (rs.next()) {

					previousAcntDetailsDto = new PreviousAccountDetailsDTO();
					previousAcntDetailsDto.setSource("Claim");
					previousAcntDetailsDto.setPolicyClaimNo(rs
							.getString("POLICY_NUMBER"));
					previousAcntDetailsDto.setReceiptDate(rs
							.getDate("RECEIPT_DATE"));
					/*
					 * if(rs.getDate("RECEIPT_DATE") != null){ Date
					 * admissionDate = rs.getDate("RECEIPT_DATE"); String
					 * dateWithoutTime =
					 * SHAUtils.getDateWithoutTime(admissionDate);
					 * previousAcntDetailsDto
					 * .setReceiptDateValue(dateWithoutTime); }
					 */
					previousAcntDetailsDto.setBankAccountNo(rs
							.getString("ACCOUNT_NUMBER"));
					previousAcntDetailsDto.setBankName(rs
							.getString("BANK_NAME"));
					previousAcntDetailsDto.setBankCity(rs
							.getString("CITY_NAME"));
					previousAcntDetailsDto.setBankBranch(rs
							.getString("BRANCH_NAME"));
					previousAcntDetailsDto.setAccountType(rs
							.getString("ACCOUNT_TYPE"));
					previousAcntDetailsDto.setIfsccode(rs
							.getString("IFSC_CODE"));
					previous.add(previousAcntDetailsDto);
					previousAcntDetailsDto = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return previous;
	}
	
	public int getGPAClaimCount(Long poilcyKey, Long insuredNumber) {
		Connection connection = null;
		CallableStatement cs = null;
		int count = 0;
		final String CALL_PROCEDURE = "{call PRC_CLAIM_CNT(?,?,?)}";
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CALL_PROCEDURE);
			cs.setLong(1, poilcyKey);
			cs.setLong(2, insuredNumber);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.execute();

			if (cs.getObject(3) != null) {
				count = cs.getInt(3);
				// active = "Y";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public PreauthDTO getBalanceSIAlert(Long policyKey, Long insuredKey,
			Long claimKey, Double sumInsured, Long intimationKey, Double clmApprovedAmount, PreauthDTO preauthDTO) {

		Connection connection = null;
		CallableStatement cs = null;
		SumInsuredBonusAlertDTO bonusAlertDTO = new SumInsuredBonusAlertDTO();
		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_BALANCE_SI_ALERT (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);
			cs.setDouble(5, sumInsured);
			cs.setDouble(6, clmApprovedAmount);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.registerOutParameter(9, Types.LONGNVARCHAR);
			cs.registerOutParameter(10, Types.DATE);
			cs.registerOutParameter(11, Types.DATE);
			cs.registerOutParameter(12, Types.LONGNVARCHAR);
			cs.registerOutParameter(13, Types.LONGNVARCHAR);
			cs.registerOutParameter(14, Types.LONGNVARCHAR);
			cs.registerOutParameter(15, Types.VARCHAR);
			cs.registerOutParameter(16, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(7) != null) {
				bonusAlertDTO.setProductName(cs.getString(7)); 
			}if (cs.getObject(8) != null) {
				bonusAlertDTO.setPreviousPolicyNumber(cs.getString(8)); 
			}if (cs.getObject(9) != null) {
				bonusAlertDTO.setPolicyYear(cs.getLong(9)); 
			}if (cs.getObject(10) != null) {
				bonusAlertDTO.setPolicyFromdate(cs.getDate(10)); 
			}if (cs.getObject(11) != null) {
				bonusAlertDTO.setPolicyTodate(cs.getDate(11)); 
			}if (cs.getObject(12) != null) {
				bonusAlertDTO.setSumInsured(cs.getLong(12)); 
			}if (cs.getObject(13) != null) {
				bonusAlertDTO.setBonus(cs.getLong(13)); 
			}if (cs.getObject(14) != null) {
				bonusAlertDTO.setUtilizedAmount(cs.getLong(14)); 
			}if (cs.getObject(15) != null) {
				preauthDTO.setSiAlertFlag(cs.getString(15)); 
			}if (cs.getObject(16) != null) {
				preauthDTO.setSiAlertDesc(cs.getString(16)); 
			}
			bonusAlertDTO.setsNo(1);
			preauthDTO.setBonusAlertDTO(bonusAlertDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return preauthDTO;
	}
	
	public PreauthDTO getBalanceSIForReimbursementAlert(Long policyKey,
			Long insuredKey, Long claimKey, Long rodKey, Double clmApprovedAmount, PreauthDTO preauthDTO) {

		Connection connection = null;
		CallableStatement cs = null;
		SumInsuredBonusAlertDTO bonusAlertDTO = new SumInsuredBonusAlertDTO();
		
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_BALANCE_SI_ALERT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);
			cs.setDouble(5, clmApprovedAmount);

			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.LONGNVARCHAR);
			cs.registerOutParameter(9, Types.DATE);
			cs.registerOutParameter(10, Types.DATE);
			cs.registerOutParameter(11, Types.LONGNVARCHAR);
			cs.registerOutParameter(12, Types.LONGNVARCHAR);
			cs.registerOutParameter(13, Types.LONGNVARCHAR);
			cs.registerOutParameter(14, Types.VARCHAR);
			cs.registerOutParameter(15, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(6) != null) {
				bonusAlertDTO.setProductName(cs.getString(6)); 
			}if (cs.getObject(7) != null) {
				bonusAlertDTO.setPreviousPolicyNumber(cs.getString(7)); 
			}if (cs.getObject(8) != null) {
				bonusAlertDTO.setPolicyYear(cs.getLong(8)); 
			}if (cs.getObject(9) != null) {
				bonusAlertDTO.setPolicyFromdate(cs.getDate(9)); 
			}if (cs.getObject(10) != null) {
				bonusAlertDTO.setPolicyTodate(cs.getDate(10)); 
			}if (cs.getObject(11) != null) {
				bonusAlertDTO.setSumInsured(cs.getLong(11)); 
			}if (cs.getObject(12) != null) {
				bonusAlertDTO.setBonus(cs.getLong(12)); 
			}if (cs.getObject(13) != null) {
				bonusAlertDTO.setUtilizedAmount(cs.getLong(13)); 
			}if (cs.getObject(14) != null) {
				preauthDTO.setSiAlertFlag(cs.getString(14)); 
			}if (cs.getObject(15) != null) {
				preauthDTO.setSiAlertDesc(cs.getString(15)); 
			}
			bonusAlertDTO.setsNo(1);
			preauthDTO.setBonusAlertDTO(bonusAlertDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return preauthDTO;
	}
	
	public PreauthDTO getBalanceSIForGMCAlert(Long policyKey, Long insuredKey,
			Long claimKey, Double clmApprovedAmount, PreauthDTO preauthDTO) {

		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GMC_CAL_ALERT(?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setDouble(4, clmApprovedAmount);

			cs.registerOutParameter(5, Types.VARCHAR);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(5) != null) {
				preauthDTO.setSiAlertFlag(cs.getString(5)); 
			}
			if (cs.getObject(6) != null) {
				preauthDTO.setSiAlertDesc(cs.getString(6)); 
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return preauthDTO;

	}
	
	public PreauthDTO getBalanceSIForReimbursementStarCancerGoldAlert(
			Long policyKey, Long insuredKey, Long claimKey, Long rodKey,
			String sobCoverCode, Double clmApprovedAmount, PreauthDTO preauthDTO) {

		Connection connection = null;
		CallableStatement cs = null;
		SumInsuredBonusAlertDTO bonusAlertDTO = new SumInsuredBonusAlertDTO();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_BAL_SICCG_ALERT (?, ?, ?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);
			cs.setString(5, sobCoverCode);
			cs.setDouble(6, clmApprovedAmount);

			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.registerOutParameter(9, Types.LONGNVARCHAR);
			cs.registerOutParameter(10, Types.DATE);
			cs.registerOutParameter(11, Types.DATE);
			cs.registerOutParameter(12, Types.LONGNVARCHAR);
			cs.registerOutParameter(13, Types.LONGNVARCHAR);
			cs.registerOutParameter(14, Types.LONGNVARCHAR);
			cs.registerOutParameter(15, Types.VARCHAR);
			cs.registerOutParameter(16, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(7) != null) {
				bonusAlertDTO.setProductName(cs.getString(7)); 
			}if (cs.getObject(8) != null) {
				bonusAlertDTO.setPreviousPolicyNumber(cs.getString(8)); 
			}if (cs.getObject(9) != null) {
				bonusAlertDTO.setPolicyYear(cs.getLong(9)); 
			}if (cs.getObject(10) != null) {
				bonusAlertDTO.setPolicyFromdate(cs.getDate(10)); 
			}if (cs.getObject(11) != null) {
				bonusAlertDTO.setPolicyTodate(cs.getDate(11)); 
			}if (cs.getObject(12) != null) {
				bonusAlertDTO.setSumInsured(cs.getLong(12)); 
			}if (cs.getObject(13) != null) {
				bonusAlertDTO.setBonus(cs.getLong(13)); 
			}if (cs.getObject(14) != null) {
				bonusAlertDTO.setUtilizedAmount(cs.getLong(14)); 
			}if (cs.getObject(15) != null) {
				preauthDTO.setSiAlertFlag(cs.getString(15)); 
			}if (cs.getObject(16) != null) {
				preauthDTO.setSiAlertDesc(cs.getString(16)); 
			}
			bonusAlertDTO.setsNo(1);
			preauthDTO.setBonusAlertDTO(bonusAlertDTO);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return preauthDTO;
	}
	
	public PreauthDTO getBalanceSIForStarCancerGoldAlert(Long policyKey,
			Long insuredKey, Long claimKey, Double sumInsured,
			Long intimationKey, String subCoverCode, Double clmApprovedAmount, PreauthDTO preauthDTO) {

		Connection connection = null;
		CallableStatement cs = null;
		SumInsuredBonusAlertDTO bonusAlertDTO = new SumInsuredBonusAlertDTO();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BALANCE_SICCG_ALERT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, intimationKey);
			cs.setLong(4, claimKey);
			if (sumInsured != null) {
				cs.setDouble(5, sumInsured);
			} else {
				cs.setDouble(5, 0);
			}
			cs.setString(6, subCoverCode);
			cs.setDouble(7, clmApprovedAmount);

			cs.registerOutParameter(8, Types.VARCHAR);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.registerOutParameter(10, Types.LONGNVARCHAR);
			cs.registerOutParameter(11, Types.DATE);
			cs.registerOutParameter(12, Types.DATE);
			cs.registerOutParameter(13, Types.LONGNVARCHAR);
			cs.registerOutParameter(14, Types.LONGNVARCHAR);
			cs.registerOutParameter(15, Types.LONGNVARCHAR);
			cs.registerOutParameter(16, Types.VARCHAR);
			cs.registerOutParameter(17, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(8) != null) {
				bonusAlertDTO.setProductName(cs.getString(8)); 
			}if (cs.getObject(9) != null) {
				bonusAlertDTO.setPreviousPolicyNumber(cs.getString(9)); 
			}if (cs.getObject(10) != null) {
				bonusAlertDTO.setPolicyYear(cs.getLong(10)); 
			}if (cs.getObject(11) != null) {
				bonusAlertDTO.setPolicyFromdate(cs.getDate(11)); 
			}if (cs.getObject(12) != null) {
				bonusAlertDTO.setPolicyTodate(cs.getDate(12)); 
			}if (cs.getObject(13) != null) {
				bonusAlertDTO.setSumInsured(cs.getLong(13)); 
			}if (cs.getObject(14) != null) {
				bonusAlertDTO.setBonus(cs.getLong(14)); 
			}if (cs.getObject(15) != null) {
				bonusAlertDTO.setUtilizedAmount(cs.getLong(15)); 
			}if (cs.getObject(16) != null) {
				preauthDTO.setSiAlertFlag(cs.getString(16)); 
			}if (cs.getObject(17) != null) {
				preauthDTO.setSiAlertDesc(cs.getString(17)); 
			}
			bonusAlertDTO.setsNo(1);
			preauthDTO.setBonusAlertDTO(bonusAlertDTO);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return preauthDTO;

	}
	
	
	// CR2019250
			public int getGMCClaimCount(Long poilcyKey, Long insuredNumber) {
				Connection connection = null;
				CallableStatement cs = null;
				int count = 0;
				final String CALL_PROCEDURE = "{call PRC_CLAIM_CNT(?,?,?)}";
				try {

					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall(CALL_PROCEDURE);
					cs.setLong(1, poilcyKey);
					cs.setLong(2, insuredNumber);
					cs.registerOutParameter(3, Types.INTEGER);
					cs.execute();

					if (cs.getObject(3) != null) {
						count = cs.getInt(3);
						// active = "Y";
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return count;
			}
			
			
			public Map<String, Object> getOPBalSumInsuredValue(Long policyKey,
					Long insuredKey, String claimType) {
				Connection connection = null;
				CallableStatement cs = null;
				Map<String, Object> valuesMap = new HashMap<String, Object>();
				try {
					connection = BPMClientContext.getConnection();
					cs = connection
							.prepareCall("{call PRC_OP_HC_BALANCE(?, ?, ?, ?, ?)}");
					cs.setLong(1, policyKey);
					cs.setLong(2, insuredKey);
					cs.setString(3, claimType);

					cs.registerOutParameter(4, Types.DOUBLE, "LN_CRY_AMT");
					cs.registerOutParameter(5, Types.DOUBLE, "LN_BALANCE_SI");
					cs.execute();

					valuesMap.put("careAmount",
							Double.parseDouble(cs.getObject(4).toString()));
					valuesMap.put("balanceSumInsured",
							Double.parseDouble(cs.getObject(5).toString()));

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				return valuesMap;
			}
			
			public Map<Integer, Object> getOPProductivityCount(String userId) {

				Connection connection = null;
				CallableStatement cs = null;
				Map<Integer, Object> countValues = new HashMap<Integer, Object>();
				Integer prodCount = 0;
				Integer claimCount = 0;

				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall("{call PRC_CLAIM_OP_TRANS_CNT (?,?,?)}");

					cs.setString(1, userId);

					cs.registerOutParameter(2, Types.NUMERIC);
					cs.registerOutParameter(3, Types.NUMERIC);
					cs.execute();
					if (cs.getString(2) != null) {
						prodCount = cs.getInt(2);
					}
					if (cs.getObject(3) != null) {
						claimCount = cs.getInt(3);
					}

					countValues.put(1, prodCount);
					countValues.put(2, claimCount);

				} catch (SQLException e) {

					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return countValues;
			}
			
			public List<Map<String, Object>> getTaskProcedureForFLPAutoAllocation(String userId) {

				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rs = null;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

				try {

					connection = BPMClientContext.getConnection();

					cs = connection
							.prepareCall("{call PRC_FLPA_FLEN_AUTO_GET_TASK (?,?,?)}");
					cs.setString(1, userId);
					cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
					cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
					cs.execute();

					rs = (ResultSet) cs.getObject(2);
					Integer totalCount = (Integer) cs.getObject(3);
					if (rs != null) {

						while (rs.next()) {
							Map<String, Object> mappedValues = SHAUtils
									.getObjectFromCursorObj(rs);
							mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

							list.add(mappedValues);
						}
					}

				} catch (SQLException e) {

					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return list;
			}

			public List<SearchHoldMonitorScreenTableDTO> getFLPHoldMonitorClaims(String intimationNo, String userID,
					String type,String menuType,Long cpuCode) {

				List<SearchHoldMonitorScreenTableDTO> holdmonitorClaims = new ArrayList<SearchHoldMonitorScreenTableDTO>();

				final String HOLD_MONITOR_CLAIMS = "{call PRC_AUTO_FLPA_HOLD_MONITOR(?, ?, ?, ?,?, ?, ?)}";

				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rs = null;
				ResultSet rs1 = null;
				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall(HOLD_MONITOR_CLAIMS);
					cs.setString(1, intimationNo);
					cs.setString(2, userID);
					cs.setString(3, type);
					cs.setString(4, menuType);
					cs.setLong(5, cpuCode != null ? cpuCode : 0l);
					cs.registerOutParameter(6, OracleTypes.CURSOR, "SYS_REFCURSOR");
					cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");

					cs.execute();
					rs = (ResultSet) cs.getObject(6);
					rs1 = (ResultSet) cs.getObject(7);
					Map<Long, Object> wkflwList = new HashMap<Long, Object>();
					SearchHoldMonitorScreenTableDTO holdClaims = null;

					if (rs != null) {

						while (rs.next()) {

							holdClaims = new SearchHoldMonitorScreenTableDTO();
							holdClaims.setIntimationNumber(rs
									.getString("INTIMATION_NO"));
							if (rs.getString("DOC_RECEIVED_DATE") != null) {
								holdClaims.setReqDate(rs.getString("DOC_RECEIVED_DATE"));
							}
							holdClaims.setType(rs.getString("TYPE"));
							holdClaims.setLeg(rs.getString("LEG"));
							holdClaims.setHoldBy(rs.getString("HOLD_USER"));
							if (rs.getString("HOLD_DATE") != null) {
								holdClaims.setHoldDate(rs.getString("HOLD_DATE"));
							}
							holdClaims.setHoldRemarks(rs.getString("HOLD_REMARKS"));
							holdClaims.setWkKey(rs.getLong("WK_KEY"));
							holdClaims.setPreauthKey(rs.getLong("CASHLESS_KEY")); 
							holdmonitorClaims.add(holdClaims);
							holdClaims = null;
						}
					}

					if (!menuType.equalsIgnoreCase("M")) {
						if (rs1 != null) {
							Map<Long, Map<String, Object>> workFlowObj = new HashMap<Long, Map<String, Object>>();
							Map<String, Object> mappedValues = null;
							while (rs1.next()) {
								mappedValues = SHAUtils
										.getRevisedObjectFromCursorObj(rs1);
								Long wkFlwKey = (Long) mappedValues
										.get(SHAConstants.WK_KEY);
								workFlowObj.put(wkFlwKey, mappedValues);		
							}
							for (SearchHoldMonitorScreenTableDTO searchHoldMonitorScreenTableDTO : holdmonitorClaims) {
								if (searchHoldMonitorScreenTableDTO.getWkKey() != null) {
									Map<String, Object> map = workFlowObj.get(
											searchHoldMonitorScreenTableDTO.getWkKey());
									searchHoldMonitorScreenTableDTO.setDbOutArray(map);
								}
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rs != null) {
							rs.close();
							rs = null;
						}
						if (rs1 != null) {
							rs1.close();
							rs1 = null;
						}
						if (cs != null) {
							cs.close();
							cs = null;
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				return holdmonitorClaims;
			}
			
			public String createJWTTokenForClaimStatusPages(Map<String, String> userMap) throws NoSuchAlgorithmException, ParseException{	
				String token = "";	
				try {	
					KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");	
					keyGenerator.initialize(1024);	
				
					KeyPair kp = keyGenerator.genKeyPair();	
					RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();	
					RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();	
				
				
					JWSSigner signer = new RSASSASigner(privateKey);	
				
					JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();	
					claimsSet.issuer("galaxy");	
					if(userMap.get("intimationNo") != null){
						claimsSet.claim("intimationNo", userMap.get("intimationNo"));	
					}
					if(userMap.get("reimbursementkey") != null){
						claimsSet.claim("reimbursementkey", userMap.get("reimbursementkey"));
					}
					if(userMap.get("acknowledgementNo") !=null){
						claimsSet.claim("acknowledgementNo", userMap.get("acknowledgementNo"));
					}
					if(userMap.get("ompdoc") != null){
						claimsSet.claim("ompdoc", userMap.get("ompdoc"));
					}
					if(userMap.get("cashlessDoc") != null){
						claimsSet.claim("cashlessDoc", userMap.get("cashlessDoc"));
					}
					if(userMap.get("lumenKey") != null){
						claimsSet.claim("lumenKey", userMap.get("lumenKey"));
					}
					claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));	
				
					SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());	
				
					signedJWT.sign(signer);	
					token = signedJWT.serialize();	
					signedJWT = SignedJWT.parse(token);	
				
					JWSVerifier verifier = new RSASSAVerifier(publicKey);	
						
					return token;	
				} catch (JOSEException ex) {	
						
				}	
				return null;	
				
			 }

			public ProcessDataCorrectionDTO getDataCoadingGetTask(String userId,String intimationNo,String claimType) {

				Connection connection = null;
				CallableStatement cs = null;
				ProcessDataCorrectionDTO dataCorrectionDTO = null;

				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall("{call PRC_CODING_GET_TASK (?,?,?,?,?,?,?)}");

					cs.setString(1, userId);
					cs.setString(2, claimType);
					cs.setString(3, intimationNo);

					cs.registerOutParameter(4, Types.LONGNVARCHAR);
					cs.registerOutParameter(5, Types.LONGNVARCHAR);
					cs.registerOutParameter(6, Types.LONGNVARCHAR);
					cs.registerOutParameter(7, Types.LONGNVARCHAR);
					cs.execute();
					if (cs.getString(4) != null) {
						dataCorrectionDTO = new ProcessDataCorrectionDTO();
						dataCorrectionDTO.setTransactionKey(cs.getLong(4)); 
						if (cs.getObject(5) != null) {
							dataCorrectionDTO.setClaimKey(cs.getLong(5));
						}
						if (cs.getObject(6) != null) {
							dataCorrectionDTO.setIntimationKey(cs.getLong(6));
						}
						if (cs.getObject(7) != null) {
							dataCorrectionDTO.setCoadingKey(cs.getLong(7));
						}
					}
					
				} catch (SQLException e) {

					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return dataCorrectionDTO;
			}
			
			public void dataCoadingRelease(String userId,String claimType,String action,Long paymentKey) {

				Connection connection = null;
				CallableStatement cs = null;

				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall("{call PRC_CODING_RELEASE (?,?,?,?)}");

					cs.setString(1, userId);
					cs.setString(2, claimType);
					cs.setString(3, action);
					cs.setLong(4, paymentKey);
					cs.execute();
					
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			public LegalBillingDTO getInterestOtherClaimForTaxDeductions(LegalBillingDTO legalBillingDTO,Long rodKey) {

				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rs = null;
				try {

					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall("{call PRC_LEGAL_TAX_DEDUCTION (?,?,?)}");
					cs.setString(1, legalBillingDTO.getPanNo());
					cs.setLong(2, rodKey);
					cs.registerOutParameter(3, OracleTypes.CURSOR,"LV_REF_GET");

					cs.execute();

					rs = (ResultSet) cs.getObject(3);
					if (rs != null) {
						while (rs.next()) {
							legalBillingDTO.setInterestOtherClaim(rs.getLong("P_TOTAL_INTEREST"));
							legalBillingDTO.setTdsOtherClaim(rs.getLong("P_TOTAL_TDS"));
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
						if (cs != null) {
							cs.close();
						}
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return legalBillingDTO;
			}
			
			public List<VerificationAccountDeatilsTableDTO> getVerifiedAccountDetails (String accountNumber) {

				final String CREATE_BATCH_PROC = "{call PRC_GET_PAYMENT_INFO_ACNO(?, ?)}";

				Connection connection = null;
				CallableStatement cs = null;
				ResultSet rs = null;
				VerificationAccountDeatilsTableDTO verificationTableDto = null;
				List<VerificationAccountDeatilsTableDTO> verificationTableDtoList = new ArrayList<VerificationAccountDeatilsTableDTO>();
				try {
					connection = BPMClientContext.getConnection();
					cs = connection.prepareCall(CREATE_BATCH_PROC);
					//cs.setString(1, docReceivedFrom);
					//cs.setString(1, policyNo);
					cs.setString(1, accountNumber);
					//cs.setString(4, verificationStage);
					
					cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
					cs.execute();
					
					rs = (ResultSet) cs.getObject(2);
					if (rs != null) {

						while (rs.next()) {
							verificationTableDto = new VerificationAccountDeatilsTableDTO();
							verificationTableDto.setClaimNumber(rs.getString(2));
							verificationTableDto.setPolicyNumber(rs.getString(3));
							verificationTableDto.setRodNumber(rs.getString(4));
							verificationTableDto.setRodKey(rs.getLong(5));
							verificationTableDto.setInsuredName(rs.getString(7));
							verificationTableDto.setAccountNumber(rs.getString(8));
							verificationTableDto.setIfscCode(rs.getString(9));
							verificationTableDto.setPaidAmount(rs.getString(10));
							verificationTableDto.setPayeeName(rs.getString(11));
							verificationTableDtoList.add(verificationTableDto);
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (cs != null) {
							cs.close();
						}
						if (connection != null) {
							connection.close();
						} 
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return verificationTableDtoList;

			}
			
	// added for installement payment status check at policy level
	public Map<String, String> getInstallmentPaymentFlag(String policyNumber,
			Long productKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String policyInstalmentFlag = null;
		String policyInstalmentMsg = null;
		Map<String, String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_POLICY_INSTALMENT_DTLS (?, ?, ?, ?)}");
			cs.setString(1, policyNumber);
			cs.setLong(2, productKey);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			policyInstalmentFlag = (String) cs.getString(3);
			policyInstalmentMsg = (String) cs.getString(4);
			resultMap.put(SHAConstants.FLAG, policyInstalmentFlag);
			resultMap.put(SHAConstants.POLICY_INSTALMENT_MESSAGE,
					policyInstalmentMsg);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}
	public ProcessDataCorrectionDTO getDataHistoricalGetTask(String userId,String intimationNo,String claimType) {

		Connection connection = null;
		CallableStatement cs = null;
		ProcessDataCorrectionDTO dataCorrectionDTO = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CODHIS_CODING_GET_TASK (?,?,?,?,?,?,?)}");

			cs.setString(1, userId);
			cs.setString(2, claimType);
			cs.setString(3, intimationNo);

			cs.registerOutParameter(4, Types.LONGNVARCHAR);
			cs.registerOutParameter(5, Types.LONGNVARCHAR);
			cs.registerOutParameter(6, Types.LONGNVARCHAR);
			cs.registerOutParameter(7, Types.LONGNVARCHAR);
			cs.execute();
			if (cs.getString(4) != null) {
				dataCorrectionDTO = new ProcessDataCorrectionDTO();
				dataCorrectionDTO.setTransactionKey(cs.getLong(4)); 
				if (cs.getObject(5) != null) {
					dataCorrectionDTO.setClaimKey(cs.getLong(5));
				}
				if (cs.getObject(6) != null) {
					dataCorrectionDTO.setIntimationKey(cs.getLong(6));
				}
				if (cs.getObject(7) != null) {
					dataCorrectionDTO.setCoadingKey(cs.getLong(7));
				}
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataCorrectionDTO;
	}
	
	public void dataCoadingHistoricalRelease(String userId,String claimType,String action,Long paymentKey) {

		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CODHIS_CODING_RELEASE (?,?,?,?)}");

			cs.setString(1, userId);
			cs.setString(2, claimType);
			cs.setString(3, action);
			cs.setLong(4, paymentKey);
			cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
			
			
	//added for installement payment status check at policy level

	public Map<String, String> getPolicyInstallmentdetails(String policyNumber,Double instalmentAmount, java.sql.Date admissionDate
			,Double approvedAmount, java.sql.Date instalmentDate,Long input1,String input2) {
		Connection connection = null;
		CallableStatement cs = null;
		String policyInstalmentFlag = null;
		String policyInstalmentMsg = null;
		Map<String,String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_POLICY_INSTL_CLM_DECISION (?, ?, ?, ?, ?, ?, ? , ?, ?)}");
			cs.setString(1, policyNumber);
			cs.setDouble(2, instalmentAmount);
			cs.setDate(3,  admissionDate);
			cs.setDouble(4, approvedAmount);
			cs.setDate(5,  instalmentDate);
			cs.setLong(6, input1);
			cs.setString(7, input2);
			
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.execute();
			policyInstalmentFlag = (String) cs.getString(8);
			policyInstalmentMsg = (String) cs.getString(9);
			resultMap.put(SHAConstants.FLAG, policyInstalmentFlag);
			resultMap.put(SHAConstants.POLICY_INSTALMENT_MESSAGE, policyInstalmentMsg);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}
	
	public String getTopupPolicyIntimationByReimbursement(String polNo, String intimationNo,
			Long intimationKey, Long topPolicyKey, Long topInsuredKey,
			Long topInsuredId) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_TP_POL_REIM_CREATE_INTM(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, polNo);
			cs.setString(2, intimationNo);
			cs.setLong(3, intimationKey);
			cs.setLong(4, topPolicyKey);
			cs.setLong(5, topInsuredKey);
			cs.setLong(6, topInsuredId);

			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();

			if (cs.getObject(7) != null) {
				output = (String) cs.getObject(7).toString();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public String getSIBasedICDAlertMsg(Long claimidtemp,
			Long policyidtemp, Long insuredKey, Object[] icdCodeList) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		OracleConnection conn = null;
		List<PreauthDTO> list = new ArrayList<PreauthDTO>();

		try {
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_ICD_CODE", conn);
			ARRAY arrayToPass = new ARRAY(des,conn,icdCodeList);
			
			cs = conn
					.prepareCall("{call PRC_LOCK_SI_BASED_ICD_ALERT (?,?,?,?,?,?)}");
			cs.setLong(1, claimidtemp);
			cs.setLong(2, policyidtemp);
			cs.setLong(3, insuredKey);
			cs.setArray(4, arrayToPass);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();

			String flag = cs.getString(5);
			String msg = cs.getString(6);

			if(flag.equals(SHAConstants.YES_FLAG))
			{
				return msg;
			}else
			{
				return "null";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "null";
	}
	
	//added for new CR R1022
	public List<OptionalCoversDTO> getOptValuesForMedicalExtension(Long reimbursementKey,Long approvedAmt) {
		Connection connection = null;
		CallableStatement cs = null;

		List<OptionalCoversDTO> optTableList = new ArrayList<OptionalCoversDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_OPT_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PA_OPT_CVR_CALC(?, ?, ?)}");

			cs.setLong(1, reimbursementKey);
			cs.setLong(2, approvedAmt);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					OptionalCoversDTO optTableDTO = new OptionalCoversDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal minDays = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal maxDays = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					BigDecimal utilDays = null != attributes[3] ? (BigDecimal) (attributes[3])
							: null;
					BigDecimal availDays = null != attributes[4] ? (BigDecimal) (attributes[4])
							: null;
					BigDecimal perDayAmt = null != attributes[5] ? (BigDecimal) (attributes[5])
							: null;
					BigDecimal minAmt = null != attributes[6] ? (BigDecimal) (attributes[6])
							: null;
					BigDecimal maxAmt = null != attributes[7] ? (BigDecimal) (attributes[7])
							: null;
					BigDecimal balAmt = null != attributes[8] ? (BigDecimal) (attributes[8])
							: null;
					String coverEligible = null != attributes[9] ? (String) (attributes[9])
							: null;
					optTableDTO.setCoverId(coverId.longValue());
					optTableDTO.setMaxDaysAllowed(maxDays.intValue());
					if (utilDays != null) {
						optTableDTO.setNoOfDaysUtilised(utilDays.intValue());
					}
					if (availDays != null) {
						optTableDTO.setNoOfDaysAvailable(availDays.intValue());
					}
					optTableDTO.setAllowedAmountPerDay(perDayAmt.doubleValue());
					optTableDTO.setMaxNoOfDaysPerHospital(minDays.intValue());
					optTableDTO.setSiLimit(minAmt.doubleValue());
					optTableDTO.setLimit(maxAmt.doubleValue());
					optTableDTO.setBalanceSI(balAmt.doubleValue());
					optTableDTO.setEligibleForPolicy(coverEligible);
					optTableList.add(optTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return optTableList;

	}
	//added for new CR R1022
	public List<OptionalCoversDTO> getGPAOptValuesForMedicalExtension(Long reimbursementKey,Long approvedAmt) {
		Connection connection = null;
		CallableStatement cs = null;

		List<OptionalCoversDTO> optTableList = new ArrayList<OptionalCoversDTO>();
		// BeanItemContainer<SelectValue> coverContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);

		try {
			final String typeTableName = "TYP_PA_OPT_CALC";

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GPA_OPT_CVR_CALC(?, ?, ?)}");

			cs.setLong(1, reimbursementKey);
			cs.setLong(2, approvedAmt);
			// cs.setLong(3, productId);
			cs.registerOutParameter(3, Types.ARRAY, typeTableName);
			cs.execute();

			Object object = cs.getObject(3);

			if (object != null) {
				Object[] data = (Object[]) ((Array) cs.getObject(3)).getArray();

				for (Object tmp : data) {
					Struct row = (Struct) tmp;
					Object[] attributes = row.getAttributes();

					OptionalCoversDTO optTableDTO = new OptionalCoversDTO();

					BigDecimal coverId = null != attributes[0] ? (BigDecimal) (attributes[0])
							: null;
					BigDecimal minDays = null != attributes[1] ? (BigDecimal) (attributes[1])
							: null;
					BigDecimal maxDays = null != attributes[2] ? (BigDecimal) (attributes[2])
							: null;
					BigDecimal utilDays = null != attributes[3] ? (BigDecimal) (attributes[3])
							: null;
					BigDecimal availDays = null != attributes[4] ? (BigDecimal) (attributes[4])
							: null;
					BigDecimal perDayAmt = null != attributes[5] ? (BigDecimal) (attributes[5])
							: null;
					BigDecimal minAmt = null != attributes[6] ? (BigDecimal) (attributes[6])
							: null;
					BigDecimal maxAmt = null != attributes[7] ? (BigDecimal) (attributes[7])
							: null;
					BigDecimal balAmt = null != attributes[8] ? (BigDecimal) (attributes[8])
							: null;
					String coverEligible = null != attributes[9] ? (String) (attributes[9])
							: null;
					optTableDTO.setCoverId(coverId.longValue());
					optTableDTO.setMaxDaysAllowed(maxDays.intValue());
					if (utilDays != null) {
						optTableDTO.setNoOfDaysUtilised(utilDays.intValue());
					}
					if (availDays != null) {
						optTableDTO.setNoOfDaysAvailable(availDays.intValue());
					}
					optTableDTO.setAllowedAmountPerDay(perDayAmt.doubleValue());
					optTableDTO.setMaxNoOfDaysPerHospital(minDays.intValue());
					optTableDTO.setSiLimit(minAmt.doubleValue());
					optTableDTO.setLimit(maxAmt.doubleValue());
					optTableDTO.setBalanceSI(balAmt.doubleValue());
					optTableDTO.setEligibleForPolicy(coverEligible);
					optTableList.add(optTableDTO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return optTableList;

	}
	
	//added for getting admission reason priority flag by noufel
	public Map<String, Object> getPriorityFlag(Long claimKey,String admissionReason) {
		Connection connection = null;
		CallableStatement cs = null;
		Long priorityWeightage = 0L;
		String priorityEvent = null;
		Map<String, Object> priorityValues = new HashMap<String, Object>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_CLAIM_PRIORITY_EVENT (?, ?, ?, ?)}");
			cs.setLong(1, claimKey);
			cs.setString(2, admissionReason);

			cs.registerOutParameter(3, Types.NUMERIC);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			if(cs.getObject(3) != null){
				priorityWeightage =  cs.getLong(3);
			}
			if(cs.getObject(4) != null){
				priorityEvent = (String) cs.getString(4);
			}
			priorityValues.put("priorityWeightage", priorityWeightage);
			priorityValues.put("priorityEvent", priorityEvent);
			System.out.println("PriorityWeightage--->" + priorityWeightage + "PriorityEvent-->" + priorityEvent);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return priorityValues;
	}
	
	public List<VerificationAccountDeatilsTableDTO> getAccountDetailsForPaymentVerificationSettled(String accountNumber, String payeeName, String ifscCode) {

		final String CREATE_BATCH_PROC = "{call PRC_GET_PAYMENT_INFO_ACNO(?, ?, ?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		//List<List<VerificationAccountDeatilsTableDTO>> resultList = new ArrayList<List<VerificationAccountDeatilsTableDTO>>();
		VerificationAccountDeatilsTableDTO verificationTableDto = null;
		//List<VerificationAccountDeatilsTableDTO> verificationTableDtoList = new ArrayList<VerificationAccountDeatilsTableDTO>();
		List<VerificationAccountDeatilsTableDTO> paidAccTableDtoList = new ArrayList<VerificationAccountDeatilsTableDTO>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(CREATE_BATCH_PROC);
			cs.setString(1, accountNumber);
			cs.setString(2, payeeName);
			cs.setString(3, ifscCode);
			cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			
			/*rs = (ResultSet) cs.getObject(5);
			if (rs != null) {

				while (rs.next()) {
					verificationTableDto = new VerificationAccountDeatilsTableDTO();
					verificationTableDto.setClaimNumber(rs.getString(1));
					verificationTableDto.setPolicyNumber(rs.getString(3));
					verificationTableDto.setRodNumber(rs.getString(4));
					verificationTableDto.setRodKey(rs.getLong(5));
					verificationTableDto.setInsuredName(rs.getString(7));
					verificationTableDto.setAccountNumber(rs.getString(8));
					verificationTableDto.setIfscCode(rs.getString(9));
					verificationTableDto.setPaidAmount(rs.getString(10));
					verificationTableDto.setPayeeName(rs.getString(11));
					verificationTableDtoList.add(verificationTableDto);
				}
			}
			resultList.add(verificationTableDtoList);*/
			
			//rs = null;
			rs = (ResultSet) cs.getObject(4);
			if (rs != null) {

				while (rs.next()) {
					verificationTableDto = new VerificationAccountDeatilsTableDTO();
					verificationTableDto.setClaimNumber(rs.getString(1));
					verificationTableDto.setPolicyNumber(rs.getString(3));
					verificationTableDto.setRodNumber(rs.getString(4));
					verificationTableDto.setRodKey(rs.getLong(5));
					verificationTableDto.setInsuredName(rs.getString(7));
					verificationTableDto.setAccountNumber(rs.getString(8));
					verificationTableDto.setIfscCode(rs.getString(9));
					verificationTableDto.setPaidAmount(rs.getString(10));
					verificationTableDto.setPayeeName(rs.getString(11));
					paidAccTableDtoList.add(verificationTableDto);
				}
			}
			//resultList.add(paidAccTableDtoList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return paidAccTableDtoList;

	}
			
	public List<Map<String, Object>> revisedGetTaskProcedureForReOpen(
			Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start PRC_CLOS_GET_TASK "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_CLOS_GET_TASK (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle PRC_CLOS_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End PRC_CLOS_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	
	public String getPolicyInsuredAgeingFlag(Long policyKey, Long insuredKey) {
		Connection connection = null;
		CallableStatement cs = null;
		String policyInsuredAgeingFlag = null;
		String policyInsuredAgeingRemark = null;
		String policyInsuredAge = null;
		//Map<String,String> resultMap = new WeakHashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_POL_INS_AGEING_ALERT (?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);

			cs.registerOutParameter(3, Types.NUMERIC);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();
			policyInsuredAge = Integer.toString(cs.getInt(3));
			policyInsuredAgeingFlag = (String) cs.getString(4);
			policyInsuredAgeingRemark = (String) cs.getString(5);
			//resultMap.put(SHAConstants.FLAG, policyInstalmentFlag);
			//resultMap.put(SHAConstants.POLICY_INSTALMENT_MESSAGE, policyInstalmentMsg);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return policyInsuredAgeingFlag;
	}
	
	//added for cr GLX2020127
	public Map<String, String> getICDBlockAlertFlag(String blockValue,String ICDValue) {
		Connection connection = null;
		CallableStatement cs = null;
		String blockAlertFlag = null;
		String blockAlertRemark = null;
		Map<String, String> priorityValues = new HashMap<String, String>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_PEREX_ICD_BLOCK_ALERT (?, ?, ?, ?)}");
			cs.setString(1, blockValue );
			cs.setString(2, ICDValue);

			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			if(cs.getObject(3) != null){
				blockAlertFlag =  (String) cs.getString(3);
			}
			if(cs.getObject(4) != null){
				blockAlertRemark = (String) cs.getString(4);
			}
			priorityValues.put("blockAlertFlag", blockAlertFlag);
			priorityValues.put("blockAlertRemark", blockAlertRemark);
			System.out.println("blockAlertFlag--->" + blockAlertFlag + "blockAlertRemark-->" + blockAlertRemark);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return priorityValues;
	}

	public Map<String, Object> getUINVersionNumberForrejectionCategory(Long policyKey, String policyNumber, Long insuredKey, Long insuredID ) {
		Connection connection = null;
		CallableStatement cs = null;
		Long productKey =0l;
		String productUINnumber = null;
		Long productVersionNumber = null;
		Map<String, Object> premiaUINversionValue = new HashMap<String, Object>();

		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_SUB_LIMIT_IRDA (?, ?, ?, ?, ?, ?, ? )}");
			cs.setLong(1, policyKey);
			cs.setString(2, policyNumber);
			cs.setLong(3,  insuredKey);
			cs.setLong(4, insuredID);
			
			cs.registerOutParameter(5, Types.NUMERIC);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.NUMERIC);
			cs.execute();
			if(cs.getObject(5) != null){
				productKey =  cs.getLong(5);
			}
			if(cs.getObject(6) != null){
				productUINnumber = (String) cs.getString(6);
			}
			if(cs.getObject(7) != null){
				productVersionNumber = cs.getLong(7);
			}
			premiaUINversionValue.put("productKey", productKey);
			premiaUINversionValue.put("productUINnumber", productUINnumber);
			premiaUINversionValue.put("productversionNumber", productVersionNumber);
			System.out.println("productUINnumber--->" + productUINnumber + "productversionNumber-->" + productVersionNumber);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return premiaUINversionValue;
	}
	
	public Date getDocRecievedDate(Long claimKey) {
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MAX(DOCUMENT_RECEIVED_DATE) FROM IMS_CLS_DOC_ACKNOWLEDGEMENT WHERE CLAIM_KEY=? ";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setLong(1, claimKey);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getDate(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public String revisedInitiateTaskProcedureForOP(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		String successMsg = "";
		try {

			// connection = BPMClientContext.getConnectionFromURL();
			// connection = getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_OP_CSH_ROD_SUBMIT_TASK", conn);
			/* "TYP_SEC_CSH_ROD_SUBMIT_TASK1", connection); */

			// LV_WORK_FLOW,TYP_SEC_SUBMIT_TASK
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			cs = conn.prepareCall("{call PRC_SEC_CSH_OP_SUBMIT_TASK (?,?)}");
			/* .prepareCall("{call PRC_SEC_CSH_ROD_SUBMIT_TASK_pd (?,?)}"); */
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println("---THE STRING---" + cs.getString(2));

			if (cs.getString(2) != null) {
				successMsg = cs.getNString(2).toString();
				// return successMsg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;
	}
	
	public List<MarketingEscalationReportTableDTO> getMarketingEscalationReportList(
		java.sql.Date fromDate, java.sql.Date toDate, String productCodeName) {
	

	List<MarketingEscalationReportTableDTO> marketingEscalationReportList = new ArrayList<MarketingEscalationReportTableDTO>();

	final String MARKETING_ESCALATION_REPORT_PROC = "{call PRC_RPT_MKT_ESCALATION(?,?,?,?)}";

	Connection connection = null;
	CallableStatement cs = null;
	ResultSet rs = null;
	try {
		connection = BPMClientContext.getConnection();
		cs = connection.prepareCall(MARKETING_ESCALATION_REPORT_PROC);
		cs.setDate(1, fromDate);
		cs.setDate(2, toDate);	
		cs.setString(3, productCodeName);

		cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
		cs.execute();

		rs = (ResultSet) cs.getObject(4);

		if (rs != null) {
			MarketingEscalationReportTableDTO marketingEscalationReportTableDTO = null;
			while (rs.next()) {
				marketingEscalationReportTableDTO = new MarketingEscalationReportTableDTO();
				marketingEscalationReportTableDTO.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
				marketingEscalationReportTableDTO.setCpuName(rs.getString("CPU_NAME"));
				marketingEscalationReportTableDTO.setZone(rs.getString("ZONE_CODE"));
				marketingEscalationReportTableDTO.setHospitalName(rs.getString("HOSPITAL_NAME"));
				
				SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
				
				marketingEscalationReportTableDTO.setEscalatedDate(dt.format(rs.getTimestamp("ESCALATED_DATE")));
				marketingEscalationReportTableDTO.setEscalatedRole(rs.getString("ESCALATED_ROLE"));
				marketingEscalationReportTableDTO.setEscalatedBy(rs.getString("ESCALATED_BY"));
				marketingEscalationReportTableDTO.setReasonForEscalation(rs.getString("ESCALATION_REASON"));
				marketingEscalationReportTableDTO.setActionTaken(rs.getString("ACTION_TAKEN"));
				marketingEscalationReportTableDTO.setDoctorRemarks(rs.getString("DOCTOR_REMARKS"));
				marketingEscalationReportTableDTO.setRecordedDate(dt.format(rs.getTimestamp("RECORDED_DATE")));
				marketingEscalationReportTableDTO.setRecordedBy(rs.getString("RECORDED_BY"));
				marketingEscalationReportTableDTO.setProductNameCode(rs.getString("Product_Name/Code"));
				marketingEscalationReportTableDTO.setLackOfMrkPersonnel(rs.getString("LACK_UNDERSTAND_MKT_PERSONEL"));
				marketingEscalationReportTableDTO.setClaimType(rs.getString("CLAIM_TYPE"));
				

				marketingEscalationReportList.add(marketingEscalationReportTableDTO);
				marketingEscalationReportTableDTO = null;
			}

		}
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
			if (cs != null) {
				cs.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	return marketingEscalationReportList;

	}


	public BeanItemContainer<SelectValue> getActionTakenValue(Long escReasonKey)
	{
		BeanItemContainer<SelectValue> actionTakenContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Connection connection = null;
		try 
		{
			connection = BPMClientContext.getConnection();
			if(null != connection)
			{

				String fetchQuery = "SELECT MKT_ACTION_KEY,ACTION_TAKEN_DESC FROM MAS_MKT_ACTION_MAPPING WHERE ESCALATION_REASON_KEY=?";
				PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
				preparedStatement.setLong(1,escReasonKey);

				if(null != preparedStatement)
				{
					ResultSet rs = preparedStatement.executeQuery();
					
					SelectValue select = null;
					if(null != rs)
					{
						while (rs.next()) {
							select = new SelectValue();
							select.setId(rs.getLong(1));
							select.setValue(rs.getString(2));	
							actionTakenContainer.addBean(select);
						}
					}
				}

			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return actionTakenContainer;

	}
	
	public Integer getGmcCgpActiveFlag(String policyNumber) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT ACTIVE_STATUS FROM MAS_GMC_CGP WHERE POLICY_NUMBER=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, policyNumber);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getInt(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;

	}

	public Map<String, Double> getGmcCorpBufferASIForVwDeatils(String bufferType,String policyNumber,Long insuredKey,Long mainNo,Long claimKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Double> values = new WeakHashMap<String, Double>();
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GMC_VIEW_CORP_BUFFER_ASI(?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, bufferType);
			cs.setString(2, policyNumber);
			cs.setLong(3, insuredKey);
			cs.setLong(4, mainNo);
			cs.setLong(5, claimKey);
			cs.registerOutParameter(6, Types.DOUBLE, SHAConstants.LN_POLICY_BUFFER_SI);
			cs.registerOutParameter(7, Types.DOUBLE, SHAConstants.LN_BUFFER_UTILISED_AMT);
			cs.registerOutParameter(8, Types.DOUBLE, SHAConstants.LN_INSURED_ALLOCATE_AMT);
			cs.registerOutParameter(9, Types.DOUBLE, SHAConstants.LN_INSURED_UTIL_AMT);
			cs.registerOutParameter(10, Types.DOUBLE, SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT);
			
			cs.execute();

			if (cs.getObject(6) != null) {
				values.put(SHAConstants.LN_POLICY_BUFFER_SI,
						Double.parseDouble(cs.getObject(6).toString()));
			}
			if (cs.getObject(7) != null) {
				values.put(SHAConstants.LN_BUFFER_UTILISED_AMT,
						Double.parseDouble(cs.getObject(7).toString()));
			}

			if (cs.getObject(8) != null) {
				values.put(SHAConstants.LN_INSURED_ALLOCATE_AMT,
						Double.parseDouble(cs.getObject(8).toString()));
			}

			if (cs.getObject(9) != null) {
				values.put(SHAConstants.LN_INSURED_UTIL_AMT,
						Double.parseDouble(cs.getObject(9).toString()));
			}
			if (cs.getObject(10) != null) {
				values.put(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT,
						Double.parseDouble(cs.getObject(10).toString()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return values;

	}
	
	public Double getRechargeAmount(
			Long policyKey, Long insuredKey) {
		final String rechargeAmount = "LN_RECHARGED_AMT";
        Double reachargeValue = 0.0;
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_RECHARGED_AMOUNT(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.registerOutParameter(3, Types.DOUBLE, rechargeAmount);
			
			cs.execute();

			if (cs.getObject(3) != null) {
				return reachargeValue = Double.parseDouble(cs.getObject(3).toString());
			} else {
				return reachargeValue;
			}
			// return balanceSumInsuredDTO;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reachargeValue;
	}
	
	public String getModernSublimitFlag(Long policyKey,
			Long productKey, Object[] sublimitSelectedList) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		OracleConnection conn = null;
		String flag = "N";
		try {
			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_LIMIT_ID", conn);
			ARRAY arrayToPass = new ARRAY(des,conn,sublimitSelectedList);
			
			cs = conn
					.prepareCall("{call PRC_MODERN_SUBLIMIT_DTLS (?,?,?,?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, productKey);
			cs.setArray(3, arrayToPass);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			 flag = cs.getString(4);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag ;
	}
	
		public void populatePropHospitalisationDataForGMC(Long rodKey,String packFlag,String propFlag,String chargesApplicable) {
			Connection connection = null;
			CallableStatement cs = null;
			OracleConnection conn = null;
			try {
				connection = BPMClientContext.getConnection();
				conn = connection.unwrap(OracleConnection.class);
				cs = connection.prepareCall("{call PRC_PW_GMC_PRORATE_CALC(?,?,?,?)}");
				cs.setLong(1, rodKey);
				cs.setString(2, packFlag);
				cs.setString(3, propFlag);
				cs.setString(4, chargesApplicable);
				cs.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
					if (cs != null) {
						cs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
	public List<Map<String, Object>> getTaskProcedureForBillingFAAutoAllocation(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_BLPR_FAPR_AUTO_GET_TASK (?,?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public String getLimitAmountValidationForCommonBillingFA(String userId,
			Long claimedAmount,Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_SEC_GET_USER_LIMIT_BL(?,?,?,?)}");
			cs.setString(1, userId);
			cs.setLong(2, claimedAmount);
			cs.setLong(3, rodKey);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();

			if (cs.getString(4) != null) {
				successMsg = cs.getString(4);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}
	
		public Map<String, Double> getBalanceSIForModernSublimit(Long policyKey,
				Long insuredKey, Long claimKey, Long rodKey) {

			Connection connection = null;
			CallableStatement cs = null;
			Double totalBalanceSI = 0d;
			Double currentBalanceSI = 0d;
			Map<String, Double> values = new HashMap<String, Double>();
			System.out.println("Start getBalanceSIForModernSublimit Procedure "
					+ System.currentTimeMillis());
			try {
				connection = BPMClientContext.getConnection();
				cs = connection
						.prepareCall("{call PRC_SUBLIMIT_REIM_BALANCE_SI(?, ?, ?, ?, ?, ?)}");
				cs.setLong(1, policyKey);
				cs.setLong(2, insuredKey);
				cs.setLong(3, claimKey);
				cs.setLong(4, rodKey);

				cs.registerOutParameter(5, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
				cs.registerOutParameter(6, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
				cs.execute();
				System.out.println("Middle getBalanceSIForModernSublimit Procedure "
						+ System.currentTimeMillis());

				// return (Double) cs.getObject(4);
				totalBalanceSI = (Double) cs.getObject(5);
				currentBalanceSI = (Double) cs.getObject(6);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
					if (cs != null) {
						cs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// return new Double("0");
			values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI);
			values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI);
			System.out.println("End getBalanceSIForModernSublimit Procedure "
					+ System.currentTimeMillis());
			return values;
		}
	
	public Integer getGmcATOSActiveFlag(String policyNumber) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT ACTIVE_STATUS FROM MAS_GMC_ATOS WHERE POLICY_NUMBER=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, policyNumber);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getInt(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;

	}
	
	public List<SearchHoldMonitorScreenTableDTO> getHoldMonitorClaimsForBillingFA(
			String intimationNo, String userID, String type, String menuType,
			Long cpuCode,String screenName) {

		List<SearchHoldMonitorScreenTableDTO> holdmonitorClaims = new ArrayList<SearchHoldMonitorScreenTableDTO>();

		final String BILLING_HOLD_MONITOR_CLAIMS = "{call PRC_AUTO_BLPR_HOLD_MONITOR(?, ?, ?, ?,?, ?, ?)}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(BILLING_HOLD_MONITOR_CLAIMS);
			cs.setString(1, intimationNo);
			cs.setString(2, userID);
			cs.setString(3, type);
			cs.setString(4, menuType);
			cs.setLong(5, cpuCode != null ? cpuCode : 0l);
			cs.registerOutParameter(6, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");

			cs.execute();

			rs = (ResultSet) cs.getObject(6);

			rs1 = (ResultSet) cs.getObject(7);
			Map<Long, Object> wkflwList = new HashMap<Long, Object>();
			SearchHoldMonitorScreenTableDTO holdClaims = null;

			if (rs != null) {

				while (rs.next()) {

					holdClaims = new SearchHoldMonitorScreenTableDTO();
					holdClaims.setIntimationNumber(rs
							.getString("INTIMATION_NO"));
					if (rs.getString("DOC_RECEIVED_DATE") != null) {

						// String reqDate =
						// SHAUtils.formatDateForHold(rs.getString("DOC_RECEIVED_DATE"));
						holdClaims
								.setReqDate(rs.getString("DOC_RECEIVED_DATE"));
					}
					
					if(screenName !=null && 
							(screenName.equals(SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)
									||screenName.equals(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION))){
						holdClaims.setType("Process Claim Billing");
					}else{
						holdClaims.setType(rs.getString("TYPE"));
					}
					holdClaims.setLeg(rs.getString("LEG"));
					holdClaims.setHoldBy(rs.getString("HOLD_USER"));
					if (rs.getString("HOLD_DATE") != null) {
						holdClaims.setHoldDate(rs.getString("HOLD_DATE"));
					}
					holdClaims.setHoldRemarks(rs.getString("HOLD_REMARKS"));
					holdClaims.setWkKey(rs.getLong("WK_KEY"));
					holdClaims.setPreauthKey(rs.getLong("ROD_KEY"));
					holdmonitorClaims.add(holdClaims);
					holdClaims = null;
				}
			}
			if (!menuType.equalsIgnoreCase("M")) {
				if (rs1 != null) {

					Map<Long, Map<String, Object>> workFlowObj = new HashMap<Long, Map<String, Object>>();

					holdClaims = new SearchHoldMonitorScreenTableDTO();
					Map<String, Object> mappedValues = null;
					while (rs1.next()) {
						mappedValues = SHAUtils
								.getRevisedObjectFromCursorObj(rs1);
						Long wkFlwKey = (Long) mappedValues
								.get(SHAConstants.WK_KEY);
						workFlowObj.put(wkFlwKey, mappedValues);
						holdClaims.setPreauthKey(rs1.getLong("ROD_KEY"));
					}
					for (SearchHoldMonitorScreenTableDTO searchHoldMonitorScreenTableDTO : holdmonitorClaims) {
						if (searchHoldMonitorScreenTableDTO.getWkKey() != null) {
							Map<String, Object> map = workFlowObj
									.get(searchHoldMonitorScreenTableDTO
											.getWkKey());
							searchHoldMonitorScreenTableDTO.setDbOutArray(map);
						}
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return holdmonitorClaims;
	}
	
	public List<Map<String, Object>> getTaskProcedureForProcessClaimAutoAllocation(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_CPCR_AUTO_GET_TASK (?,?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public String getHospitalCashTopupPolicyIntimationByReimbursement(String polNo, String intimationNo,
		Long intimationKey, Long topPolicyKey, Long topInsuredKey,
			Long topInsuredId) {
		Connection connection = null;
		CallableStatement cs = null;
		String output = "";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_HOSPCASH_REIM_CREATE_INTM(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, polNo);
			cs.setString(2, intimationNo);
			cs.setLong(3, intimationKey);
			cs.setLong(4, topPolicyKey);
			cs.setLong(5, topInsuredKey);
			cs.setLong(6, topInsuredId);
	
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
	
			if (cs.getObject(7) != null) {
				output = (String) cs.getObject(7).toString();
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
		}

	public String getProposerAddress(String policyNumber) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT PROPOSER_ADDRESS_LINE1||','||chr(10)||PROPOSER_ADDRESS_LINE2||','||chr(10)||PROPOSER_ADDRESS_LINE3||','||chr(10)||CITY||','||chr(10)||STATE||' - '||POLICY_PINCODE FROM IMS_CLS_POLICY WHERE POLICY_NUMBER=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, policyNumber);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return (rs.getString(1)).toString();
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public Map<String, Integer> getClaimAndActivityAge(Long wkKey,Long claimKey,String transactionKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Integer> values = new WeakHashMap<String, Integer>();
		try {

			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GET_CLM_ACTVITY_AGEING(?,?,?,?,?)}");
			cs.setLong(1, wkKey);
			cs.setLong(2, claimKey);
			cs.setString(3, transactionKey);
			cs.registerOutParameter(4, Types.INTEGER, SHAConstants.LN_CLAIM_AGEING);
			cs.registerOutParameter(5, Types.INTEGER, SHAConstants.LN_ACTIVITY_AGEING);

			
			cs.execute();

			if (cs.getObject(4) != null) {
				values.put(SHAConstants.LN_CLAIM_AGEING,
						Integer.parseInt(cs.getObject(4).toString()));
			}
			if (cs.getObject(5) != null) {
				values.put(SHAConstants.LN_ACTIVITY_AGEING,
						Integer.parseInt(cs.getObject(5).toString()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return values;

	}
	
	public String getHospitalClaimAlertFlag(String IntimationNumber) {
		//final String rechargeAmount = "LN_RECHARGED_AMT";
	    //Double reachargeValue = 0.0;
		Connection connection = null;
		CallableStatement cs = null;
		String result = "N";
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_NNW_HOSPITAL_CLAIM_ALERT(?, ?)}");
			cs.setString(1, IntimationNumber);
			cs.registerOutParameter(2, Types.VARCHAR);
			
			cs.execute();
	
			if (cs.getObject(2) != null) {
				result = cs.getObject(2).toString();
				 return result;
			} else {
				return result;
			}
			// return balanceSumInsuredDTO;
	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				return result;
	}			
	public List<ViewBusinessProfilChartDTO> getBusinessProfileChart(int chartNo,String codeParameter) {
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		
		List<ViewBusinessProfilChartDTO> businessProfileDTOList= new ArrayList<ViewBusinessProfilChartDTO>();
		
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_GET_business_profile_CHART(?, ?, ?)}");
			cs.setInt(1, chartNo);
			cs.setString(2, codeParameter);
			cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs != null) {
				ViewBusinessProfilChartDTO bpcDTO = null;
				
				//Map<String, Object> mappedValues= null;
				while (rs.next()) {
					
					bpcDTO = new ViewBusinessProfilChartDTO();
					bpcDTO.setSlNo(rs.getLong(SHAConstants.SL_NO));
					bpcDTO.setLabelName(rs.getString(SHAConstants.LABEL_NAME));
					bpcDTO.setColValue(rs.getString (SHAConstants.COLVALUE));
					
					businessProfileDTOList.add(bpcDTO);
					bpcDTO = null;
					
				/* mappedValues = SHAUtils.getBusinessProfileList(rs);
					list.add(mappedValues);*/
					}
				}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return businessProfileDTOList;
	}
	
	public String callLockProcedureForAutoAllocation(Long wrkFlowKey, String currentqueue,
			String userID) {

		Connection connection = null;
		CallableStatement cs = null;
		String successMsg = "";
		String autoAllocation = "Y";
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_SEC_AUTO_ALOC_LOCK_USER (?,?,?,?,?)}");
			cs.setLong(1, wrkFlowKey);
			cs.setString(2, currentqueue);
			cs.setString(3, userID);
			cs.setString(4, autoAllocation);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();

			if (cs.getString(5) != null) {
				successMsg = cs.getString(5);
			}

			// return successMsg;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return successMsg;

	}
	
	public String getHospitalAddress(String hospitalCode) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT ADDRESS||','||chr(10)||CITY||','||chr(10)||STATE||' - '||PINCODE FROM MAS_HOSPITALS WHERE HOSPITAL_CODE=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, hospitalCode);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return (rs.getString(1)).toString();
						}

					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public Long getWorkflowKey(String intimationNumber,String currentQ) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MAX(WK_KEY) FROM IMS_CLS_SEC_WORK_FLOW WHERE INTIMATION_NO=? AND CURRENT_Q=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, intimationNumber);
				preparedStatement.setString(2, currentQ);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getLong(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0l;

	}
	
	public List<Map<String, Object>> getHopScrColrIcnByHoscode(String hospitalCode) {
		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> outputValues = null;

		List<Map<String, Object>> resultOut = new ArrayList<Map<String,Object>>();
		ResultSet rs = null;

		try {
			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_OVERCHARGE_HOSPITAL_SCORE(?,?)}");
			cs.setString(1, hospitalCode);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				while (rs.next()) {

					String hpScDesc = rs.getString(SHAConstants.HS_DESC) != null? rs.getString(SHAConstants.HS_DESC): "";
					String hpScPerc = rs.getString(SHAConstants.HS_PERC) != null? rs.getString(SHAConstants.HS_PERC): "";
					String hpSc1Points = rs.getString(SHAConstants.HS_COLR) != null ? rs.getString(SHAConstants.HS_COLR) :"";
					outputValues = new HashMap<String, Object>();
					outputValues.put(SHAConstants.HS_DESC, hpScDesc);
					outputValues.put(SHAConstants.HS_PERC, hpScPerc);
					outputValues.put(SHAConstants.HS_COLR, hpSc1Points);
					resultOut.add(outputValues);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultOut;
	}
	
	public List<Map<String, Object>> revisedGetTaskRODOnlineProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start PRC_SEC_CSH_ROD_GET_TASK_ONLN "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK_ONLN (?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	
	public List<Map<String, Object>> revisedGetTaskRODOfflineProcedure(Object[] input) {

		Connection connection = null;
		CallableStatement cs = null;
		OracleConnection conn = null;
		ResultSet rs = null;
		// String successMsg="";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println("--- Start PRC_SEC_CSH_ROD_GET_TASK_OFFLN "
				+ System.currentTimeMillis());
		try {

			// connection = BPMClientContext.getConnectionFromURL();

			connection = BPMClientContext.getConnection();
			conn = connection.unwrap(OracleConnection.class);

			ArrayDescriptor des = ArrayDescriptor.createDescriptor(
					"TYP_SEC_CSH_ROD_GET_TASK", conn);
			ARRAY arrayToPass = new ARRAY(des, conn, input);
			/*
			 * cs = connection
			 * .prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK (?,?,?)}");
			 */
			cs = conn.prepareCall("{call PRC_SEC_CSH_ROD_GET_TASK_OFFLN(?,?,?)}");
			cs.setArray(1, arrayToPass);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();
			System.out.println("--- Middle TYP_SEC_CSH_ROD_GET_TASK "
					+ System.currentTimeMillis());

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {
				Map<String, Object> mappedValues = null;
				while (rs.next()) {
					mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
				System.out.println("--- End TYP_SEC_CSH_ROD_GET_TASK before "
						+ System.currentTimeMillis());
				// return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	
	public String ProvisionBehaviourValueFlag(String intimationNo , String policyNumber,Long policyKey,Long insuredNumber ) {

		Connection connection = null;
		CallableStatement cs = null;
		String provisionBehaviourFlag = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_BLK_CSH_PROCESSING (?,?,?,?,?)}");
			cs.setString(1, intimationNo);
			cs.setString(2, policyNumber);
			cs.setLong(3, policyKey);
			cs.setLong(4, insuredNumber);
			cs.registerOutParameter(5, OracleTypes.VARCHAR);
			cs.execute();
			System.out.println("call PRC_BLK_CSH_PROCESSING intimationNo "+intimationNo+" -  "+" policyNumber "+policyNumber+" cs.getString(3) -- "+cs.getString(5));
			String flag = cs.getString(5);

			return provisionBehaviourFlag = flag;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return provisionBehaviourFlag;
	}
	
	public String getUnlockProvisionFlag(String intimationNo , String policyNumber,Long policyKey,Long insuredNumber,String userID  ) {

		Connection connection = null;
		CallableStatement cs = null;
		String provisionUnlockFlag = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_UNBLK_CSH_PROCESSING (?,?,?,?,?,?)}");
			cs.setString(1, intimationNo);
			cs.setString(2, policyNumber);
			cs.setLong(3, policyKey);
			cs.setLong(4, insuredNumber);
			cs.setString(5, userID);
			cs.registerOutParameter(6, OracleTypes.VARCHAR);
			cs.execute();
			System.out.println("call PRC_UNBLK_CSH_PROCESSING intimationNo "+intimationNo+" -  "+" policyNumber "+policyNumber+" cs.getString(3) -- "+cs.getString(6));
			String flag = cs.getString(6);

			return provisionUnlockFlag = flag;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return provisionUnlockFlag;
	}
	
	public ProcessDataCorrectionDTO getDataCoadingPriorityGetTask(String userId,String intimationNo,String claimType) {

		Connection connection = null;
		CallableStatement cs = null;
		ProcessDataCorrectionDTO dataCorrectionDTO = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PRTY_CODING_GET_TASK (?,?,?,?,?,?,?,?)}");

			cs.setString(1, userId);
			cs.setString(2, claimType);
			cs.setString(3, intimationNo);

			cs.registerOutParameter(4, Types.LONGNVARCHAR);
			cs.registerOutParameter(5, Types.LONGNVARCHAR);
			cs.registerOutParameter(6, Types.LONGNVARCHAR);
			cs.registerOutParameter(7, Types.LONGNVARCHAR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			if (cs.getString(4) != null) {
				dataCorrectionDTO = new ProcessDataCorrectionDTO();
				dataCorrectionDTO.setTransactionKey(cs.getLong(4)); 
				if (cs.getObject(5) != null) {
					dataCorrectionDTO.setClaimKey(cs.getLong(5));
				}
				if (cs.getObject(6) != null) {
					dataCorrectionDTO.setIntimationKey(cs.getLong(6));
				}
				if (cs.getObject(7) != null) {
					dataCorrectionDTO.setCoadingKey(cs.getLong(7));
				}
				if (cs.getObject(8) != null) {
					dataCorrectionDTO.setClaimType(cs.getString(8));
				}
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataCorrectionDTO;
	}
	
	public List<Map<String, Object>> getTaskForFinancialApprovalAutoAllocation(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			connection = BPMClientContext.getConnection();

			cs = connection
					.prepareCall("{call PRC_FAPR_AUTO_GET_TASK (?,?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils
							.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);

					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<Map<String, Object>> getTaskProcedureForBillingAutoAllocation(String userId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BLPR_AUTO_GET_TASK (?,?,?)}");
			cs.setString(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.registerOutParameter(3, Types.INTEGER, "NUMBER");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			Integer totalCount = (Integer) cs.getObject(3);
			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> mappedValues = SHAUtils.getRevisedObjectFromCursorObj(rs);
					mappedValues.put(SHAConstants.TOTAL_RECORDS, totalCount);
					list.add(mappedValues);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}	
	
	public void dataCoadingPriorityRelease(String userId,String claimType,String action,Long paymentKey) {

		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_PRTY_CODING_RELEASE (?,?,?,?)}");

			cs.setString(1, userId);
			cs.setString(2, claimType);
			cs.setString(3, action);
			cs.setLong(4, paymentKey);
			cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, Double> getBalanceSIForCardicCarePlatRemb(Long policyKey,
			Long insuredKey, Long claimKey, Long rodKey) {

		Connection connection = null;
		CallableStatement cs = null;
		Double totalBalanceSI = 0d;
		Double currentBalanceSI = 0d;
		Map<String, Double> values = new HashMap<String, Double>();
		System.out.println("Start getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		try {
			connection = BPMClientContext.getConnection();
			cs = connection
					.prepareCall("{call PRC_BILLING_BALANCE_SI_CC(?, ?, ?, ?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, insuredKey);
			cs.setLong(3, claimKey);
			cs.setLong(4, rodKey);

			cs.registerOutParameter(5, Types.DOUBLE, "LN_TOT_BAL_SUM_INSURED");
			cs.registerOutParameter(6, Types.DOUBLE, "LN_CUR_BAL_SUM_INSURED");
			cs.execute();
			System.out.println("Middle getBalanceSIForReimbursement Procedure "
					+ System.currentTimeMillis());

			// return (Double) cs.getObject(4);
			totalBalanceSI = (Double) cs.getObject(5);
			currentBalanceSI = (Double) cs.getObject(6);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return new Double("0");
		values.put(SHAConstants.TOTAL_BALANCE_SI, totalBalanceSI);
		values.put(SHAConstants.CURRENT_BALANCE_SI, currentBalanceSI);
		System.out.println("End getBalanceSIForReimbursement Procedure "
				+ System.currentTimeMillis());
		return values;
	}
	
	public String getIntimationQueueForFraud(String intimationNumber) {

		Connection connection = null;
		String queue = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				if(queue == null)
				{
					String fetchQuery = "SELECT COUNT(*) FROM IMS_CLS_CLAIM_PAYMENT WHERE BATCH_STATUS IS NOT NULL AND BATCH_STATUS=155 AND INTIMATION_NUMBER=?";

					PreparedStatement preparedStatement = connection
							.prepareStatement(fetchQuery);
					preparedStatement.setString(1, intimationNumber);

					if (null != preparedStatement) {
						ResultSet rs = preparedStatement.executeQuery();
						if (null != rs) {
							while (rs.next()) {
								if(rs.getInt(1) == 1)
								{
									queue = "BATCH";
								}
							}
						}
					}
				}

				if(queue == null)
				{
					String fetchQuery = "SELECT COUNT(*) FROM IMS_CLS_CLAIM_PAYMENT WHERE LOT_STATUS IS NOT NULL AND LOT_STATUS=154 AND INTIMATION_NUMBER=?";

					PreparedStatement preparedStatement = connection
							.prepareStatement(fetchQuery);
					preparedStatement.setString(1, intimationNumber);

					if (null != preparedStatement) {
						ResultSet rs = preparedStatement.executeQuery();
						if (null != rs) {
							while (rs.next()) {
								if(rs.getInt(1) == 1)
								{
									queue = "LOT";
								}
							}
						}
					}
				}

				if(queue == null)
				{
					String fetchQuery = "SELECT COUNT(*) FROM IMS_CLS_SEC_WORK_FLOW WHERE PREVIOUS_Q = 'FAPR' AND INTIMATION_NO=?";

					PreparedStatement preparedStatement = connection
							.prepareStatement(fetchQuery);
					preparedStatement.setString(1, intimationNumber);

					if (null != preparedStatement) {
						ResultSet rs = preparedStatement.executeQuery();
						if (null != rs) {
							while (rs.next()) {
								if(rs.getInt(1) >= 1)
								{
									queue = "FA";
								}
							}
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return queue;

	}
	
	public Boolean getFraudFlag(String intimationNumber,String policyNumber,String hospitalCode,String irdaCode,String intermediaryCode) {
		Connection connection = null;
		
		String query = "SELECT COUNT(*) FROM MAS_FRAUD_IDENTIFICATION WHERE VALUE IN (";
		
		if(intimationNumber != null)
		{
			query = query+"'"+intimationNumber+"',";
		}
		
		if(policyNumber != null)
		{
			query = query+"'"+policyNumber+"',";
		}
		
		if(hospitalCode != null)
		{
			query = query+"'"+hospitalCode+"',";
		}
		
		if(irdaCode != null)
		{
			query = query+"'"+irdaCode+"',";
		}
		
		if(intermediaryCode != null)
		{
			query = query+"'"+intermediaryCode+"'";
		}
		try {
			connection = BPMClientContext.getConnection();
			
			Boolean flag = getFraudFlagFoEff(query+") AND EFFECTIVE_TO_DATE IS NULL");
			
			if (null != connection) {
				if(!flag){

					String fetchQuery = query+") AND CURRENT_DATE BETWEEN (EFFECTIVE_FROM_DATE-1) AND (EFFECTIVE_TO_DATE+1)";

					PreparedStatement preparedStatement = connection
							.prepareStatement(fetchQuery);
					//preparedStatement.setString(1, value);

					if (null != preparedStatement) {
						ResultSet rs = preparedStatement.executeQuery();
						if (null != rs) {
							while (rs.next()) {
								if(rs.getLong(1) == 0)
								{
									return false;
								}
								else {
									return true;
								}
							}

						}
					}
				}
				else
				{
					return true;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;

	}

public Boolean getFraudAlertValue(String value) {
	Connection connection = null;
	try {
		connection = BPMClientContext.getConnection();
		
		Boolean effectiveFlag = getFraudAlertEffectiveFlag(value);
		
		if (null != connection) {

			if(!effectiveFlag)
			{
				String fetchQuery = "SELECT COUNT(*) FROM MAS_FRAUD_IDENTIFICATION WHERE VALUE =? AND CURRENT_DATE BETWEEN (EFFECTIVE_FROM_DATE-1) AND (EFFECTIVE_TO_DATE+1)";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, value);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							if(rs.getLong(1) == 0)
							{
								return false;
							}
							else {
								return true;
							}
						}

					}
				}
			}
			else
			{
				return true;
			}

		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {

		try {

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	return false;

}

public Boolean getFraudAlertEffectiveFlag(String value) {
	Connection connection = null;
	try {
		connection = BPMClientContext.getConnection();
		
		if (null != connection) {

			String fetchQuery = "SELECT COUNT(*) FROM MAS_FRAUD_IDENTIFICATION WHERE VALUE =? AND EFFECTIVE_TO_DATE IS NULL";

			PreparedStatement preparedStatement = connection
					.prepareStatement(fetchQuery);
			preparedStatement.setString(1, value);

			if (null != preparedStatement) {
				ResultSet rs = preparedStatement.executeQuery();
				if (null != rs) {
					while (rs.next()) {
						if(rs.getLong(1) == 0)
						{
							return false;
						}
						else {
							return true;
						}
					}

				}
			}

		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {

		try {

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	return false;

}

public Boolean getFraudFlagFoEff(String query) {
	Connection connection = null;
	try {
		connection = BPMClientContext.getConnection();
		
		if (null != connection) {

			String fetchQuery = query;

			PreparedStatement preparedStatement = connection
					.prepareStatement(fetchQuery);
			/*preparedStatement.setString(1, value);*/

			if (null != preparedStatement) {
				ResultSet rs = preparedStatement.executeQuery();
				if (null != rs) {
					while (rs.next()) {
						if(rs.getLong(1) == 0)
						{
							return false;
						}
						else {
							return true;
						}
					}

				}
			}

		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {

		try {

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	return false;

}

public List<DispatchDetailsReportTableDTO> getDispatchDetailsReportList(Long updateType,String intimationNo,String rodNo,String batchNo,String awbBatchNo,String awsNo,Date fromDate,Date toDate) {
		
		List<DispatchDetailsReportTableDTO> dispatchDetailsReportTableDTOs = new ArrayList<DispatchDetailsReportTableDTO>();
		final String DISPATCH_DETAILS_REPORT_PROC = "{call PRC_RPT_AWB_DETAILS(?,?,?,?,?,?,?,?,?,?)}";
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			java.sql.Date from = null;
			java.sql.Date to = null;
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(DISPATCH_DETAILS_REPORT_PROC);
			cs.setLong(1,updateType);
			cs.setString(2, intimationNo);
			cs.setString(3, rodNo);
			cs.setString(4, batchNo);
			cs.setString(5, awbBatchNo);
			cs.setString(6, awsNo);
			if(fromDate !=null ){
				from = new java.sql.Date(fromDate.getTime());
			}
			if(toDate !=null ){
				to = new java.sql.Date(toDate.getTime());
			}
			cs.setDate(7, from);
			cs.setDate(8, to);
			cs.registerOutParameter(9, OracleTypes.NUMBER, "NUMBER");
			cs.registerOutParameter(10, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(10);

			if (rs != null) {
				DispatchDetailsReportTableDTO detailsReportTableDTO = null;
				while (rs.next()) {
					detailsReportTableDTO = new DispatchDetailsReportTableDTO();
					detailsReportTableDTO.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
					detailsReportTableDTO.setPolicyNo(rs.getString("POLICY_NUMBER"));
					detailsReportTableDTO.setRodNo(rs.getString("ROD_NUMBER"));
					detailsReportTableDTO.setBatchNumber(rs.getString("BATCH_NUMBER"));
					detailsReportTableDTO.setClaimType(rs.getString("CLAIM_TYPE"));
					detailsReportTableDTO.setDocReceivedFrom(rs.getString("DOCUMENT_RECEIVED_FROM"));
					detailsReportTableDTO.setRodType(rs.getString("ROD_TYPE"));					
					detailsReportTableDTO.setCourierPartner(rs.getString("COURIER_PARTNER_NAME"));
					detailsReportTableDTO.setAwsNumber(rs.getString("AWB_NUMBER"));
					detailsReportTableDTO.setChequeDDNumber(rs.getString("CHEQUE_DD_NUMBER"));
					detailsReportTableDTO.setSettledAmount(rs.getDouble("CHEQUE_DD_AMOUNT"));
					detailsReportTableDTO.setChequeDDStatus(rs.getString("CHEQUE_DD_DELIVERY_STATUS"));
					detailsReportTableDTO.setChequeDDdeliveryTo(rs.getString("CHEQUE_DD_DELEIVERED_TO"));
					Date checkDDDate = rs.getDate("CHEQUE_DD_DELIVERED_DATE_TIME");
					detailsReportTableDTO.setChequeDDdate(SHAUtils.formateDateForDispatch(checkDDDate));
					detailsReportTableDTO.setReturnRemark(rs.getString("CHQ_DD_DLVRY_REMARKS"));
					Date returnDate = rs.getDate("CHEQUE_DD_RETURNED_DATE_TIME");
					detailsReportTableDTO.setChequeDDReturndate(SHAUtils.formateDateForDispatch(returnDate));
					dispatchDetailsReportTableDTOs.add(detailsReportTableDTO);
					detailsReportTableDTO = null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dispatchDetailsReportTableDTOs;
	}

	public static void insertIntegrationLogTable(IntegrationLogTable integrationLog) {       
	    PreparedStatement preparedStatement = null;
	    Connection connection = null;
	    String insertQuery="INSERT INTO IMS_CLS_INTEGRATION_LOG (OWNER,SERVICE_NAME,URL,REQUEST,RESPONSE,STATUS,REMARKS,CREATED_BY,CREATED_DATE,ACTIVE_STATUS,INTEGRATION_LOG_KEY)"
	                       +" Values (?,?,?,?,?,?,?,?,?,?,SEQ_INTEG_LOG_KEY.nextval)";
	    try {
	        connection = BPMClientContext.getConnection();
	        preparedStatement= connection.prepareStatement(insertQuery);           
	        preparedStatement.setString(1, integrationLog.getOwner());
	        preparedStatement.setString(2, integrationLog.getServiceName());
	        preparedStatement.setString(3, integrationLog.getUrl());
	        preparedStatement.setString(4, integrationLog.getRequest());
	        if(integrationLog.getResponse() !=null && integrationLog.getResponse().length() >= 3000)
	        {
	            preparedStatement.setString(5, integrationLog.getResponse().substring(0, 2995));
	        }
	        else
	        {
	            preparedStatement.setString(5, integrationLog.getResponse());   
	        }
	        preparedStatement.setString(6, integrationLog.getStatus());
	        preparedStatement.setString(7, integrationLog.getRemarks());
	        preparedStatement.setString(8,integrationLog.getCreatedBy());   
	        preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
	        preparedStatement.setInt(10,1);           
	        preparedStatement.execute();
	       
	        }
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
	            if (!preparedStatement.isClosed())
	            {
	                preparedStatement.close();
	            }
	            if (!connection.isClosed())
	            {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	public List<ProvisionHistoryDTO> getProvisionHistoryList(
			String intimationNo) {

		List<ProvisionHistoryDTO> resultProvisionHistory = new ArrayList<ProvisionHistoryDTO>();

		final String PROVISION_HISTORY_PROC = "{call IMS_GALTXN.PRC_GET_PROVISION_FLOW( ?, ? )}";

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PROVISION_HISTORY_PROC);
			cs.setString(1, intimationNo);

			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			if (rs != null) {
				ProvisionHistoryDTO provisionHistorydto= null;
				while (rs.next()) {

					provisionHistorydto = new ProvisionHistoryDTO();

                                          //change from this
					provisionHistorydto.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
                                        provisionHistorydto.setCashlessRefNo(rs.getString("ITERATION_NUMBER"));
                                        provisionHistorydto.setReimbursementRefNo(rs.getString("ROD_NUMBER"));
                                        provisionHistorydto.setPreviousProvisionAmount(rs.getDouble("Previous Provision Amount"));
                                        provisionHistorydto.setCurrentProvisionAmount(rs.getDouble("Current Provision Amount"));
                                        provisionHistorydto.setPreviousClaimProvisionAmount(rs.getDouble("Previous Claim Prov Amount"));
                                        provisionHistorydto.setCurrentClaimProvisionAmount(rs.getDouble("Current Claim Prov Amount"));
                                        provisionHistorydto.setStatus(rs.getString("PROCESS_VALUE"));
                                        provisionHistorydto.setDate(rs.getDate("CREATED_DATE"));
					
															resultProvisionHistory.add(provisionHistorydto);
					provisionHistorydto  = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultProvisionHistory;
	}
	
	public List<ViewPaymentAuditDTO> getPaymentAuditDetails(String bancsUprId) {

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rset = null;
		List<ViewPaymentAuditDTO> paymentAuditDTOs = new ArrayList<ViewPaymentAuditDTO>();
		try {
			
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_BANCS_CLAIM_RECON_INFO(?,?)}");
			cs.setString(1, bancsUprId);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);
			if (null != rset) {
				while (rset.next()) {	
					ViewPaymentAuditDTO paymentAuditDTO = new ViewPaymentAuditDTO();
					paymentAuditDTO.setStage(rset.getString("STAGE"));
					paymentAuditDTO.setAuditDate(rset.getString("STAGE_DATE"));
					paymentAuditDTOs.add(paymentAuditDTO);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return paymentAuditDTOs;
	}
	
	public String getMaxInvestigatorCode() {
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "SELECT MAX(INVESTIGATOR_CODE) FROM IMS_TMP_INVESTIGATION";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							return rs.getString(1);
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
	
	public static String getZohoAuthToken() {
		ResultSet rs = null;
		String authToken = "";
		String temp = "";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			String fetchQuery=	"SELECT RESPONSE FROM IMS_CLS_INTEGRATION_LOG WHERE OWNER = 'ZOHO' AND SERVICE_NAME='ZohoLogon' AND STATUS='Success' "+
					"AND INTEGRATION_LOG_KEY=(SELECT MAX(INTEGRATION_LOG_KEY) FROM IMS_CLS_INTEGRATION_LOG WHERE OWNER = 'ZOHO' AND SERVICE_NAME='ZohoLogon' AND STATUS='Success')";

			preparedStatement = connection.prepareStatement(fetchQuery);	            
			System.out.println("----------Select Query For ZOHO Access Token Service----->\n" + fetchQuery + "\n");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				authToken = rs.getString(1);
			}
			if(!StringUtils.isBlank(authToken)){
				Any response = JsonIterator.deserialize(authToken);
				temp = response.get("access_token").toString();
				System.out.println("Access Token : "+temp);
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null && !rs.isClosed()){
					rs.close();
				}
				if (preparedStatement != null && !preparedStatement.isClosed()){
					preparedStatement.close();
				}
				if (connection != null && !connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	
	public InitiateTalkTalkTalkDTO getDailerEndCallDateTime(String refno,InitiateTalkTalkTalkDTO dto) {

		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if (null != connection) {

				String fetchQuery = "select TO_TIMESTAMP( call_date || ' ' || call_hour || ':' || call_min, 'DD-MM-YYYY HH24:MI:SS' ) as START_DATE,"
									+"TO_TIMESTAMP(call_date|| ' ' || call_hour || ':' || call_min, 'DD-MM-YYYY HH24:MI:SS' )"
									+"+ NUMTODSINTERVAL(SUBSTR(call_duration, 1,2) , 'hour')"
									+"+ NUMTODSINTERVAL(SUBSTR(call_duration, 4,2) , 'minute')"
									+"+ NUMTODSINTERVAL(SUBSTR(call_duration, 7,2) , 'second') as END_DATE from ims_cls_dialer_status_log d where d.CALL_REFERENCE_ID=?";

				PreparedStatement preparedStatement = connection
						.prepareStatement(fetchQuery);
				preparedStatement.setString(1, refno);

				if (null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if (null != rs) {
						while (rs.next()) {
							dto.setDialerCallStartTime(rs.getTimestamp(1));
							dto.setDialerCallEndTime(rs.getTimestamp(2));

						}

					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dto;

	}
}
