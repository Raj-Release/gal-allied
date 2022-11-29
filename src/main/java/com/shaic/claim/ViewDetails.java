package com.shaic.claim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import com.claim.view.document.ViewdocumentdetailsPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.VB64ComplianceDto;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPDocumentRelatedService.OMPAckDocService;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPEarlierAcknowledgementDetailsPage;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPViewAcknowledgementPage;
import com.shaic.claim.OMPViewDetails.view.OMPViewClaimStatusMapper;
import com.shaic.claim.OMPViewDetails.view.OMPViewCurrentPolicyDetailsUI;
import com.shaic.claim.OMPViewDetails.view.OMPViewPreviousClaimUI;
import com.shaic.claim.OMPViewDetails.view.OMPViewRiskDetailsUI;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPPreviousClaimWindowUI;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimWindowOpen;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimsTable;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPEarlierRodDetailsViewImpl;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.activitylog.ActivityLogWindow;
import com.shaic.claim.bpc.ViewBusinessProfileChart;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACService;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACTableDTO;
import com.shaic.claim.cashlessprocess.processicac.search.ViewICACResponseDetailsUI;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.claimhistory.view.ompView.ViewOMPClaimHistoryRequest;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyService;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDetailsViewImpl;
import com.shaic.claim.hospitalspocdetails.view.ViewHospitalSPOCDetailsUI;
import com.shaic.claim.intimatino.view.ViewGalaxyIntimation;
import com.shaic.claim.intimatino.view.ViewIntimationDetails;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashLessTableMapper;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewClaimStatus;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.claim.intimation.create.CallRecordingResponse;
import com.shaic.claim.intimation.create.CallRecordingsUI;
import com.shaic.claim.intimation.create.ClaimGrievanceAPIService;
import com.shaic.claim.intimation.create.ClaimGrievanceDTO;
import com.shaic.claim.intimation.create.ViewEscalaltionTrails;
import com.shaic.claim.intimation.create.ViewGrievanceTrails;
import com.shaic.claim.intimation.create.ViewMasterPolicyDetails;
import com.shaic.claim.intimation.create.ViewBasePolicyDetails;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.legal.history.view.ViewLegalHistoryRequest;
import com.shaic.claim.linkedPolicyDetails.ViewLinkedPolicyDetails;
import com.shaic.claim.lumen.create.ViewLumenTrialsPage;
import com.shaic.claim.medical.opinion.RecMarketingEscalationService;
import com.shaic.claim.medical.opinion.ViewMarkEscHistoryDTO;
import com.shaic.claim.medical.opinion.ViewMarkEscHistoryUI;
import com.shaic.claim.omp.ratechange.OMPViewEndorsementDetailTable;
import com.shaic.claim.omp.registration.OMPBalanceSiForm;
import com.shaic.claim.omp.registration.OMPPreviousClaimDetailTable;
import com.shaic.claim.omp.registration.OMPPreviousClaimTableDTO;
import com.shaic.claim.ompviewroddetails.OMPIntimationAndViewDetailsUI;
import com.shaic.claim.ompviewroddetails.OMPViewClaimStatusDto;
import com.shaic.claim.ompviewroddetails.OMPViewIntimationDetailsUI;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityDTO;
import com.shaic.claim.outpatient.processOPpages.OPViewRiskDetails;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.mapper.OutpatientMapper;
import com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails.ClaimAndDocumentDetailsPageViewImpl;
import com.shaic.claim.outpatient.registerclaim.table.OPBillDetailsListenerTable;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.ViewPCCRemarksDTO;
import com.shaic.claim.pcc.dto.ViewPCCTrailsDTO;
import com.shaic.claim.pcc.hrmp.HRMAndDivisionHeadViewDetailsPage;
import com.shaic.claim.pcc.views.ViewPCCRemarksDetailsPage;
import com.shaic.claim.pcc.views.ViewPCCTrailsUI;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksUI;
import com.shaic.claim.pedrequest.view.ViewPedRequestDetail;
import com.shaic.claim.pedrequest.view.ViewPostCashlessRemarkUI;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.opsearch.OPViewClaimStatus;
import com.shaic.claim.policy.search.ui.opsearch.ProcessOPRequestService;
import com.shaic.claim.preauth.HRMHospitalDetailsTableDTO;
import com.shaic.claim.preauth.HRMTable;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.ViewEarlierHRMListenerTable;
import com.shaic.claim.preauth.ViewEarlierHRMTable;
import com.shaic.claim.preauth.view.ViewPreAuthDetailsMainClass;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.view.ViewRenewedPolicyPreviousClaims;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTableDTO;
import com.shaic.claim.preauth.wizard.pages.ViewGmcCorpBufferDetailsPage;
import com.shaic.claim.preauth.wizard.pages.ViewGmcCorporateBufferUtilisationPage;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.preauth.wizard.view.ViewInvestigationDetails;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.previousclaim.PreviousClaimViewPage;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.SearchProcessDataCorrectionService;
import com.shaic.claim.processdatacorrection.viewtable.ViewDataCorrectionUI;
import com.shaic.claim.productbenefit.view.ViewContinuityBenefitDetailsUI;
import com.shaic.claim.productbenefit.view.ViewPortablityPolicyUI;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.claim.registration.IntimationSourceDetailsView;
import com.shaic.claim.registration.PreviousClaimWindowUI;
import com.shaic.claim.registration.ViewCopayDetails;
import com.shaic.claim.registration.ViewHospitalDetails;
import com.shaic.claim.registration.ViewPreviousClaimWindowOpen;
import com.shaic.claim.registration.ViewPreviousClaims;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsured;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsuredComp;
import com.shaic.claim.registration.balancesuminsured.view.OPBalanceSumInsured;
import com.shaic.claim.registration.balancesuminsured.view.OPBillAssesmentSheet;
import com.shaic.claim.registration.balancesuminsured.view.OPBillAssesmentSheetDTO;
import com.shaic.claim.registration.balancesuminsured.view.OPBillAssesmentSheetMapper;
import com.shaic.claim.registration.balancesuminsured.view.OPClaimStatus;
import com.shaic.claim.registration.balancesuminsured.view.PABalanceSumInsured;
import com.shaic.claim.registration.balancesuminsured.view.ViewBalenceSumInsured;
import com.shaic.claim.registration.balancesuminsured.view.ViewBillDetails;
import com.shaic.claim.registration.balancesuminsured.view.ViewPABAlanceSumInsured;
import com.shaic.claim.registration.balancesuminsured.view.ViewRiskDetailsUI;
import com.shaic.claim.registration.balancesuminsured.view.ViewUnnamedRiskDetailsUI;
import com.shaic.claim.registration.previousinsurance.view.PreviousInsuranceInsuredDetailsTableDTO;
import com.shaic.claim.registration.previousinsurance.view.ViewPreviousInsurance;
import com.shaic.claim.registration.previousinsurance.view.ViewPreviousInsuranceInsuredDetails;
import com.shaic.claim.reimbursement.billing.wizard.BillingInternalRemarksPage;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FAInternalRemarksPage;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingUI;
import com.shaic.claim.reimbursement.rawanalysis.ViewRawRequestPage;
import com.shaic.claim.reimbursement.rrc.services.ViewTALKTALKTable;
import com.shaic.claim.reimbursement.talktalktalk.DialerStatusLog;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkService;
import com.shaic.claim.reimbursement.talktalktalk.ViewTalkTalkTalkDetailsTable;
import com.shaic.claim.reimbursement.talktalktalk.ViewTalkTalkTalkDetailsUI;
import com.shaic.claim.reports.PolicywiseClaimReportDto;
import com.shaic.claim.reports.negotiationreport.ViewNegotiationAmountDetailsUI;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
import com.shaic.claim.translationmiscrequest.view.ViewTranslationMiscRequestDetails;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.CorporateBufferUtilisationTable;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPreviousPreauthQueryTable;
import com.shaic.claim.viewEarlierRodDetails.ViewProductBenefitsTable;
import com.shaic.claim.viewEarlierRodDetails.ViewProductBenefitsTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.IrdaBillDetailsPage;
import com.shaic.claim.viewEarlierRodDetails.Page.RevisedPreauthViewPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewAcknowledgementPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewIrdaNonPayablePdfPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewMedicalSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPABillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPAMedicalSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPAacknowledgementPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPreauthDetailsPage;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewBillDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.shaic.claim.viewsublimits.view.ViewSublimitsCompviewImpl;
import com.shaic.claim.viewsublimits.view.ViewSublimitsviewImpl;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.ActivityLogService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.CoPayService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.GpaBenefitDetails;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasCopay;
import com.shaic.domain.MasOpClaimSection;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OMPViewClaimantDetailsPageUI;
import com.shaic.domain.OMPViewDeductiblesPageUI;
import com.shaic.domain.OMPViewOtherCurrencyRateHistoryUI;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyRiskCover;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TalkTalkTalk;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpOPClaim;
import com.shaic.domain.ViewTmpOPIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPDocumentList;
import com.shaic.domain.outpatient.OPHCDetails;
import com.shaic.domain.outpatient.OPHCHospitalDetails;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OPSpeciality;
import com.shaic.domain.outpatient.OutpatientService;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.ViewRevisedCashlessTopPanel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.CashlessDetailsDto;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
//import com.shaic.view.pages.PageUI;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
public class ViewDetails extends ViewComponent {


	public enum ViewLevels {
		INTIMATION {
			@Override
			public int getIndex() {
				return 1;
			}
		},
		REGISTRATION {
			@Override
			public int getIndex() {
				return 1;
			}
		},
		PREAUTH_MEDICAL {
			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 1;
			}
		},
		PREAUTH {

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 1;
			}

		},

		OUPATIENT {

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 2;
			}

		},
		
		
		
		CLOSE_CLAIM {

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 3;
			}

		},
		
		PED_OUTSIDE_PROCESS {

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 4;
			}

		},
		LEGAL_CLAIMS {
			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 8;
			}
		},

		
		UPLOAD_INVESTIGATION_REPORT {
			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 5;
			}

		},
		
		OMP {
			@Override
			public int getIndex() {
				return 9;
			}
		

		},		
	
		
		CLAIM_STATUS {

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 6;
			}

		},
		
	PA_PROCESS{
			
			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 7;
			}
		},
		
	ACK_ROD_PROCESS{
			
			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
				return 11;
			}
		},
		
	LUMEN_TRAILS{
				@Override
				public int getIndex() {
					// TODO Auto-generated method stub
					return 10;
				}
			},
			
	FVR_GRADING_PROCESS{
				
				@Override
				public int getIndex() {
					// TODO Auto-generated method stub
					return 12;
				}
			},
   HRM_DIVISION_HEAD_REMARKS{
				
				@Override
				public int getIndex() {
					// TODO Auto-generated method stub
					return 13;
				}
			};	
			
		public abstract int getIndex();

	}

	private static final String VIEW_TOP_PANEL_DETAILS = "View Top Panel Details";
	private static final String VIEW_POLICY = "Current Policy Details";	
	private static final String VIEW_BASE_POLICY = "Base Policy Details";
	private static final String VIEW_DOCUMENTS = "Policy Documents";
	private static final String VIEW_PORTABLITY = "Policy Details";
	private static final String VIEW_SUBLIMITS = "Sublimits";
	private static final String VIEW_INTIMATION = "Intimation";
	private static final String VIEW_FVR_DETAILS = "FVR Details";
	private static final String VIEW_MER_DETAILS = "MER Details";
	private static final String VIEW_RISK_DETAILS = "Risk Details";
	public static final String VIEW_CLAIM_STATUS = "Claim Status";
	private static final String VIEW_CLAIM_HISTORY = "History";
	private static final String VIEW_CO_PAY_DETAILS = "Co-pay Details";
	private static final String VIEW_STOPLOSS_DETAILS = "StopLoss details";
	private static final String VIEW_QUERY_DETAILS = "Query Details";
	private static final String VIEW_INVESTIGATION_DETAILS = "Investigation details";
	private static final String VIEW_CORPORATE_BUFFER = "Corporate Buffer";
	private static final String VIEW_PRODUCT_BENEFITS = "Product Benefits";
	private static final String VIEW_HOSPITAL_DETAILS = "Hospital Details";
	private static final String VIEW_PRE_AUTH_DETAILS = "Pre Auth Details";
	private static final String VIEW_SPECIALITY_OPINION = "Specialist Trail";
	private static final String VIEW_BALANCE_SUM_INSURED = "Balance Sum Insured";
	private static final String VIEW_PED_REQUEST_DETAILS = "PED Request Details";
	private static final String VIEW_DOCTOR_REMARKS = "Doctor Note (Trails)";            //Doctor Note(Internal Remarks)";
	private static final String VIEW_PREVIOUS_INSURANCE = "Previous/Renewed Insurance Details";
	private static final String VIEW_PREVIOUS_CLAIM_DETAILS = "Previous Claim Details";
	private static final String VIEW_TRANSLATION_MISC_REQUEST = "Translation Misc Request";
	private static final String VIEW_IRDA_CATEGORY_DETAILS = "Category Wise Bill Details (IRDA )";
	private static final String VIEW_ACKNOWLEDGMENT_DETAILS = "Acknowledgment Details";
	
	private static final String VIEW_EARLIER_ACKNOWLEDGE_DETAILS = "Earlier Acknowledgement Details";
	private static final String VIEW_BILL_SUMMARY_DETAILS = "Bill Summary Details";
	private static final String VIEW_MEDICAL_SUMMARY_DETAILS = "Medical Summary";
	private static final String VIEW_IRDA_NON_PAYABLES = "IRDA Non-payables";
	private static final String VIEW_COORDINATOR_REPLY = "Co-ordinator Reply";
	private static final String VIEW_PORTABLITY_POLICY_DETAILS = "Portablity Policy";
	private static final String VIEW_HRM_DETAILS = "Hrm Details";
	
	private static final String VIEW_64VB_COMPLIANCE ="64Vb Compliance";
	private static final String VIEW_BILL_DETAILS ="Bill Details";
	private static final String VIEW_PREVIOUSCLAIM_DETAILS="Previous Claim New Detials";
	public static final String VIEW_DOC_DETAILS="View Document Details";

	private static final String VIEW_PAGE="View Page";
	// Add one constant for document details view.
	private static final String VIEW_DOCUMENT_DETAILS = "Claim Documents";
	
	private static final String VIEW_OP_BALANCE_SUM_INSURED ="Balance Sum_Insured";
	
	private static final String VIEW_PA_BALANCE_SUM_INSURED ="Balance Sum_Insured PA";
	
	private static final String VIEW_OP_BILL_SUMMARY="Bill Summary";

	private static final String VIEW_CLAIMS_STATUS="OP Claim Status";
	private static final String VIEW_OP_BILL_ASSESMENTSHEET="Bill AssesmentSheet";
	private static final String VIEW_CLAIMANT_DETAILS = "Claimant details";
	private static final String VIEW_OTHER_CURRENCY_RATE_HISTORY = "Other Currency Rate History";
//	private static final String VIEW_OMP_INTIMATION_DETAILS = "OMPIntimation";
	private static final String VIEW_ENDORSEMENT_DETAILS = "Endorsement Details";

	private static final String VIEW_DEDUCTIBLES="View deductibles";
	private static final String VIEW_PROPOSAL = "Proposal";
	private static final String VIEW_EARLIER_ROD_DETAILS = "Earlier ROD";
//	private static final String VIEW_BALANCE_SI_DETAILS = "Balance SI";
//	private static final String VIEW_OMP_RISK_DETAILS = "Omp Risk Details";
	
	private static final String VIEW_POLICY_SCHEDULE="View Policy Schedule";

	private static final String VIEW_CORPORATE_BUFFER_UTILISATION = "Corporate Buffer";
	
	private static final String VIEW_EXCLUSIONS_WAIVER ="Exclusions and Waiver";
	
	private static final String VIEW_UNNAMED_RISK_DETAILS = "Unnamed Risk Details";
	private static final String VIEW_BILLING_INTERNAL_REMARKS = "Billing Internal Remarks";
	private static final String VIEW_FA_INTERNAL_REMARKS = "FA Internal Remarks";
	private static final String VIEW_ACTIVITY_LOG = "Activity Log";
	
	private static final String VIEW_CALL_RECORDING = "Review Televerification Call(s)";
	private static final String VIEW_GRIEVANCE_TRAILS = "Grievance Trails";
	private static final String VIEW_ESCALATION_TRAILS = "View Escalation";
	
	private Boolean isTVCLogin = false;
	
	// User roles
	private static final String USER_ROLE_ACTIVITY_LOG = "CLM_ACTIVITY_LOG";
	
	private static final String USER_ROLE_VIEW_ASSESSMENT_SHEET_VERSIONS = "CLM_VIEW_BILL_ASSESSEMENT";  // R1293
	
	private static final String VIEW_ICR_DETAILS = "ICR Details";
	private static final String USER_ROLE_ESCLATE_RAW = "CLM_RAW_VIEW";
	
	private static final String VIEW_REPLIED_RAW = "View RAW Request";
	
	private static final String CUMULATIVE_BONUS= "View Bonus Logic";
	
	private static final String BILLASSESSMENT_SHEET_VERSIONS = "View Bill Assessment Sheet Versions";
	
	private static final String CVC_CLAIM_AUDIT_TRAILS = "Claim Audit Trails";
	private static final String CVC_CLAIM_AUDIT_HISTORY = "Claim Audit History";
	
	private static final String USER_ROLE_VIEW_CVC_AUDIT_TRAILS = "CLM_CVC_AUDIT_TRAILS";
	
	private static final String CLM_AUDIT_QRY_REPLY_CASHLESS = "CLM_CVC_AUDIT_REPLY_CASHLESS";
	private static final String CLM_AUDIT_QRY_REPLY_MEDICAL = "CLM_CVC_AUDIT_REPLY_MEDICAL";
	private static final String CLM_AUDIT_QRY_REPLY_BILLING_FA = "CLM_CVC_AUDIT_REPLY_BILLING_FA";
	

	private static final String CONTINUITY_BENEFITS = "Continuity Benefits";
	
	private static final String VIEW_POST_CASHLESS_REMARK = "PCC Detailed View"; 
	
	private static final String VIEW_HOSPITAL_SPOC_DETAILS = "Hospital SPOC Details";
	
	private static final String VIEW_NEGOTIATION_AMOUNT_DETAILS = "Negotiation Details"; 
	
	private static final String VIEW_DATA_VALIDATION_DETAILS = "Data Validation Details"; 
	
	private static final String VIEW_POLICY_SCHEDULE_WITHOUT_RISK="Policy Schedule without Risk";

	private static final String VIEW_ICAC_REPONSE_DETAILS = "ICAC Detailed View"; 

	private static final String VIEW_PCC_TRAILS = "PCC Trails";
	
	private static final String VIEW_HRM_DIVISION_HEAD_REMARKS = "HRM/Division Head Remarks";
	private static final String VIEW_REC_MARKETING_ESCALATION = "Marketing Escalation History";
	private static final String VIEW_BUSINESS_PROFILE_CHART = "Mini Business Profile";
	
	private static final String VIEW_TALK_TALK_TABLE = "Talk, Talk, Talk";
	
	private static final String SOURCE_CHANNEL_DETAILS = "Source Channel Details";

	private ViewLevels level;

	private Boolean btnDisabled;

	private Boolean isOutpatient = false;
	
	private Boolean isNotHealthCheckUP = true;
	
	private String legalType;

	private boolean clmAuditUser = false;
	
	private boolean clmAuditQryReplyUser = false;

	private static Window popup;
		
	private MedicalApprovalPremedicalProcessingUI parent;
	
	//private MedicalApprovalZonalReviewWizardViewImpl parent;

	@EJB
	private PolicyService policyService;

	@EJB
	private PEDValidationService pedValidationService;
	@EJB
	private InsuredService insuredService;

	@EJB
	private MasterService masterService;

	@EJB
	private CreateRODService billDetailsService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private OMPIntimationService ompIntimationService;

	@EJB
	private PreauthService preAuthService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private CreateRODService createRodService;

	@EJB
	private PreviousPolicyService previousPolicyService;
	
	@Inject
	private Instance<ViewPreviousInsuranceInsuredDetails> viewPreviousInsuranceInsuredDetails;
	
	@Inject
	private ViewSeriousDeficiencyUI viewSeriousDeficiencyUI;

	@EJB
	private CoPayService coPayService;
	
	@EJB
	private InitiateTalkTalkTalkService talkService;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	@Inject
	private ViewRiskDetailsUI riskDetailsUI;
	
	@Inject
	private OPViewRiskDetails opriskDetailsUI;
//	
	@Inject
	private ViewPreviousPreauthQueryTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private  ViewBusinessProfileChart businessPCPage;


	@EJB
	private InvestigationService investigationService;

	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;

	@EJB
	private ViewCoOrdinatorReplyService viewCoOrdinatorReplyService;

	@Inject
	private ProcessOPRequestService processOPRequestService;

	@EJB
	private OutpatientService outPatientService;

	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;

	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;

	@Inject
	private Instance<ViewBalenceSumInsured> viewBalenceSumInsured;
	
	@Inject
	private BalanceSumInsured balanceSumInsured;
	
	/*@Inject 
	private OutpatientService outpatientService;*/

	@Inject
	private Instance<ViewHospitalDetails> viewHospitalDetails;

	@Inject
	private IrdaBillDetailsPage viewIrdaBillDetailsPage;

	@Inject
	private PreviousClaimViewPage viewpreviousclaimPage;
	
//	@Inject 
//	private PreviousClaimTable viewPreviousClaimTable;
	
//	@Inject
//	private PreviousClaimsTableDTO viewPreviousClaimTableDTO;
//	
	@Inject
	private  ViewdocumentdetailsPage viewDocumentDetailsPage;
	
	@Inject
	private  ViewElearnContentPage eLearnPage;
	
	//@Inject
	//private Viewdocumentpage viewDocumentPageUI;
		
	@Inject
	private ViewIrdaNonPayablePdfPage pdfPageUI;

	@Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;

	@Inject
	private Instance<ViewPreviousClaims> viewPreviousClaims;
	
	@Inject
	private Instance<ViewPreviousClaimWindowOpen> viewPreviousClaimWindowOpen;
	
	@Inject
	private Instance<ViewOMPPreviousClaimWindowOpen> viewOMPPreviousClaimWindowOpen;
	
	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	
	@Inject
	private ViewRenewedPolicyPreviousClaims renewedPolicyPrevClaims;
	
	@Inject
	private ViewOMPPreviousClaimsTable ompPreviousClaimsTable;

	@Inject
	private Instance<ViewCopayDetails> viewCopayDetails;

	@Inject
	private ViewInvestigationDetails viewInvestigationDetails;

	@Inject
	private ViewPolicyDetails viewPolicyDetail;
	
	@Inject
	private ViewBasePolicyDetails viewBasePolicyDetail;
	
	@Inject 
	ViewRevisedCashlessTopPanel viewTopPanelDetails;
	
	@Inject 
	OPBillDetailsListenerTable oPBillDetailsListenerTable; 

	@Inject 
	ViewProductBenefitsTable viewProductBenefitsTable;
	
	@Inject
	private Instance<ViewPreviousInsurance> viewPreviousInsurance;

	@Inject
	private CashLessTableDetails cashLessTableDetails;
	
	@Inject
	private ViewCoOrdinatorReplyTable viewCoordinatorReplyTableObj;

	@Inject
	private RodIntimationDetailsUI rodIntimationDetailsObj;

	@Inject
	private ViewBillDetailsTable viewBillDetailsTableObj;
	
	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;

	@Inject
	private CashLessTableMapper cashLessTableMapper;
	
	@Inject 
	private NewIntimationMapper newIntimationMapper;

	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	

	@Inject
	private ViewOMPClaimHistoryRequest viewOMPClaimHistoryRequest;
	
	@Inject
	private ViewTranslationMiscRequestDetails viewTranslationMiscRequestDetails;

	@Inject
	private ViewClaimStatus viewClaimStatus;

	@Inject
	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;

	@Inject
	private ViewMedicalSummaryPage viewMedicalSummaryPage;
	
	@Inject
	private ViewStopLossDetalsForGMC viewStopLossGmcPage;
	
	@Inject
	private ViewGmcCorpBufferDetailsPage corporateBufferPage;
	
	@Inject
	private ViewGmcCorporateBufferUtilisationPage corporateBufferUtilisationPage;
	
	@Inject
	private ViewPAMedicalSummaryPage viewPAMedicalSummaryPage;

	@Inject
	private ViewPreAuthDetailsMainClass viewPreAuthDetailsMainClass;

	@Inject
	private ViewPreauthDetailsPage viewPreauthDetailsPage;

	@Inject
	private RevisedPreauthViewPage revisedPreauthViewPage;

	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private ViewPABillSummaryPage viewPABillSummaryPage;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;

	@Inject
	private ViewPedRequestDetail viewPedRequestDetails;
	
	@Inject
	private ViewPortablityPolicyUI viewPortablityPolicyUI;
	
	@Inject
	private ViewContinuityBenefitDetailsUI contBenefitDtls;
	
	@Inject
	private ViewDoctorRemarksUI viewDoctorRemarksUI;

	@Inject
	private ViewIntimationDetails viewIntimationDetails;
	
	@Inject
	private ViewGalaxyIntimation viewGalaxyIntimationDetails;

	@Inject
	private ViewSublimitsviewImpl subLimits;

	@Inject
	private OPBalanceSumInsured opBalanceSumInsured;
	
	@Inject
	private PABalanceSumInsured balanceSumInsuredPA;
	
	@Inject
	private ViewPABAlanceSumInsured viewBalanceSumInsuredPA;
	
	@Inject
	private OPClaimStatus oPClaimStatus;

	@Inject
	private ViewBillDetails viewBillDetails;
	
	@Inject
	private OPBillAssesmentSheet billAssesmentSheet;

	@Inject
	private ViewFVRDetailsViewImpl viewFVRDetailsViewImpl;

	@Inject
	private ClaimStatusDto claimStatusDto;

	@Inject
	private CashlessDetailsService cashlessDetailsService;

	@Inject
	DBCalculationService DBCalculationService;

	@Inject
	private ViewDocuments viewDocuments;

	@Inject
	private ViewAcknowledgementPage viewAcknowledgementPage;
	
	@Inject
	private ViewPAacknowledgementPage viewPAacknowledgementPage;

	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@Inject
	private ViewEarlierHRMTable viewEarlierHrmTable;
	
	@Inject
	private ViewEarlierHRMListenerTable viewEarilerHrmListenerTable;
		
	@Inject
	private HRMTable hrmtable;
	
	@Inject
	private BalanceSumInsuredComp balanceSumInsuredComp;
	
	@Inject
	private ViewSublimitsCompviewImpl compSublimits;
	
	@Inject
	private OMPViewDeductiblesPageUI ompViewDeductiblesPageUI;
	
	@Inject
	private OMPViewClaimantDetailsPageUI viewClaimantDetailsUI;
	@Inject
	private OMPViewOtherCurrencyRateHistoryUI ompViewOtherCurrencyRateUI;
	@Inject
	private OMPViewIntimationDetailsUI ompViewIntimationDetails;
	@Inject
	private OMPViewEndorsementDetailTable ompViewEndorsementDetails;
	@Inject
	private OMPBalanceSiForm ompBalanceSIForm;
	@Inject
	private OMPViewRiskDetailsUI ompRiskDetailsUI;
	@EJB
	private OMPProcessRODBillEntryService ompRodBillEntryService;
	@Inject
	private OMPIntimationAndViewDetailsUI ompclaimStatusUI;
	@Inject
	private OMPViewPreviousClaimUI ompViewPreviousClaimUI;
	@Inject
	private OMPViewCurrentPolicyDetailsUI ompViewCurrentPolicyUI;
	
	private OMPEarlierRodDetailsViewImpl ompearlierRodDetailsViewObj;
	
	@Inject
	private OMPPreviousClaimDetailTable previousClaimsRgistration;
	
	@Inject
	private Instance<OMPEarlierRodDetailsViewImpl> ompearlierRodDetailsViemImplInstance;
	
	@Inject
	private ViewLegalHistoryRequest viewLegalHistoryRequest;
	
	@Inject 
	CorporateBufferUtilisationTable corporateBufferUtilisationTable; 
	
	@Inject
	private ViewRTAsumInsuredUI viewRTAsumInsuredUI;

	@Inject
	private ViewUnnamedRiskDetailsUI unnamedRiskDetailsUI;
	
	private Policy policy;

	@Inject
	private BillingInternalRemarksPage billingInternalRemarksPage;
	
	@Inject
	private FAInternalRemarksPage faInternalRemarksPage;
	
	@Inject
	private ViewRawRequestPage viewRawRequestPage;

	@Inject
	private ViewLinkedPolicyDetails linkedPolicyDtlsPage;
	
	@Inject
	private OPViewClaimStatus opViewClaimStatus;
	
	@EJB
	private SearchProcessDataCorrectionService correctionService;
	
	@EJB
	private ProcessICACService procesIcacService;
	
	@Inject
	private ViewDataCorrectionUI dataCorrectionUI;

	@Inject
	private ViewICACResponseDetailsUI processICACUI;
	
	@Inject
	private ViewPCCTrailsUI pccTrailsUI;
	
	@Inject
	private ViewMarkEscHistoryUI viewMarkEscHistoryUI;
	
	private String intimationNo;
	
	private String rodNo;
	
	// Do not make the screenName as null
	private String screenName = "";
	
	private Long key;

	private Long preAuthKey;
	
	private Long stageKey;

	
	private Map<String, Object> referenceData = new HashMap<String, Object>();

	private Long rodKey;

	private Long policyKey;
	
	private String policyNumber;
	private String healthCardNo;
	private OutPatientDTO opBeanDto;
	
//	private Long intimationKey;  // to be removed
	
//	private Long insuredKey;  // to be removed
	
	private List<String> listOfScreens = new ArrayList<String>();
	
	private Insured insuredSelected;
	

	public Insured getInsuredSelected() {
		return insuredSelected;
	}

	public void setInsuredSelected(Insured insuredSelected) {
		this.insuredSelected = insuredSelected;
	}

	private ClaimAndDocumentDetailsPageViewImpl documentDetailsPageInstance; // to be removed
	
	private NativeSelect viewDetailsSelect;
	private Button goButton;
	
	private Button dummyButton;
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME",  type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	private Long acknowledgementKey;
	
	private BrowserWindowOpener opener;
	
	private HorizontalLayout horizontalLayout1 ;	
	
	private FormLayout viewDetailsForm ;	
	
	private VerticalLayout mainPanel;
	
	
	private Button btnGo;
	
	private Button hiddenGoBtn;
	
	private static final String LEGAL_TRAILS="Legal Trails";
	private static final String VIEW_LUMEN_TRAILS="View Lumen Trails";
	
	private Button eLearnBtn;
		
	private Boolean isLumenViewEnabled;
	
	private Long lumenPolicyKey;

	/***
	 * Fix for issue 756. Removing VIEW FVR DETAILS from drop down if the
	 * enhancement is Final. This is used in process enhancement screen.
	 */

	private static boolean isFVRDisabled = false;
	
	@Inject
	private ViewGMCExclusionsWaiverTable viewGMCExclusionsWaiverTableObj;
	
	@Inject
	private ViewMasterPolicyDetails viewMasterPolicyDetail;
	
	@Inject
	private ActivityLogWindow activityLogWindow;

	@EJB
	private ActivityLogService activityLogService;

	
	@Inject
	private ViewICRDetails viewICRDetailsFrm;
		
	@EJB
	private ClaimsReportService clmreportService;
	
	@Inject
	private ViewNegotiationDetailsPage viewNegotiationDetailsPage;
	
	@Inject
	private CallRecordingsUI callRecordingsUI;
	
	@Inject
	private ViewGrievanceTrails viewGrievanceTrailsUI;
	
	@Inject
	private ViewEscalaltionTrails viewEscalaltionTrailsUI;
	
	
	private CallRecordingResponse callRecordingResponse;
	
	@Inject
	private ViewPCCRemarksDetailsPage viewPostCashlessRemarkUI;
	
	@Inject
	private ViewHospitalSPOCDetailsUI viewHospitalSPOCDetailsUI;
	
	@Inject
	private ViewNegotiationAmountDetailsUI viewNegotiationAmountDetailsUI;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;
	
	@Inject
	private ViewPostCashlessRemarkUI viewPostCashlessRemarkOLDUI;
	
	@EJB
	private RecMarketingEscalationService recMarkEscservice;
	
	private Boolean isOpRegLogin = false;
	
	@Inject
	private HRMAndDivisionHeadViewDetailsPage hrmAndDivisionHeadViewDetailsPage;
	
	@Inject
	private OMPEarlierAcknowledgementDetailsPage oMPEarlierAcknowledgementDetailsPage;
	
	@Inject
	private OMPViewAcknowledgementPage ompViewAcknowledgementPage;
	
	@EJB
	private OMPAckDocService oMPAckDocService;
	
	@Inject
	private Instance<ViewTALKTALKTable> viewtalktalkTableInst;
	
	private ViewTALKTALKTable viewtalktalkTable;
	
	@Inject
	private Instance<ViewTalkTalkTalkDetailsTable> viewTalkTalkTalkDetailsTableInst;
	
	private ViewTalkTalkTalkDetailsTable viewTalkTalkTalkDetailsTable;
	
	@Inject
	private ViewTalkTalkTalkDetailsUI viewTalkTalkTalkDetailsUI;
	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Long getPreAuthKey() {
		return preAuthKey;
	}

	public void setPreAuthKey(Long preAuthKey) {
		this.preAuthKey = preAuthKey;
	}
	
	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	@PostConstruct
	public void init() {
		mainPanel = new VerticalLayout();
		addStyleName("view");
		setSizeFull();
		dummyButton = new Button();
		 opener = new BrowserWindowOpener(PreviousClaimWindowUI.class);
		/*FormLayout viewDetailsForm = new FormLayout();*/
		 viewDetailsForm = new FormLayout();
		viewDetailsForm.setSpacing(true);
		viewDetailsForm.setMargin(false);
		this.viewDetailsSelect = new NativeSelect("View Details");
		//
		
		viewDetailsForm.addComponent(viewDetailsSelect);
		goButton = getGoButton(viewDetailsSelect);
		

		addViewDetailsSelectListener();
		FormLayout goButtonForm = new FormLayout(goButton);

		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);*/
		horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);
		horizontalLayout1.setSizeUndefined();
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		horizontalLayout1.setSpacing(true);

		mainPanel.addComponent(horizontalLayout1);
		mainPanel.setComponentAlignment(horizontalLayout1,
				Alignment.MIDDLE_RIGHT);

		setHeight("40px");
		setCompositionRoot(mainPanel);
	}
	
	public void addVaadinSessionAttribute(){
		
		btnGo = new Button();
		btnGo.setCaption("Go");
		//Vaadin8-setImmediate() btnGo.setImmediate(true);
//		addPreviousClaimDetailsListener(btnGo);
		
		if(btnGo != null){
			Intimation intimationObj = intimationService.getIntimationByNo(intimationNo);
			OPIntimation opintimation = intimationService.getOPIntimationByNo(intimationNo);
				
			if(intimationObj != null){
					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,intimationNo);					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewPreviousClaimWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_RENEWED_POLICY_CLAIMS_TABLE,renewedPolicyPrevClaims);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OUTPATIENT_FLAG,false);
				EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(PreviousClaimWindowUI.class);
				opener.popupBlockerWorkaround(true);
				opener.withShortcut(callPreviousClaimsDetailsViewSListener());
				opener.setFeatures("height=700,width=1300,resizable");
				opener.doExtend(btnGo);
				setSopener(opener);
			} else if(opintimation != null || policyKey != null){
				if(opintimation != null){
					VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,opintimation.getIntimationId());
				}
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OP_POLICY_DETAILS,policyKey);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OUTPATIENT_FLAG,true);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewPreviousClaimWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_RENEWED_POLICY_CLAIMS_TABLE,renewedPolicyPrevClaims);
		
				EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(PreviousClaimWindowUI.class);
				opener.popupBlockerWorkaround(true);
				opener.withShortcut(callPreviousClaimsDetailsViewSListener());
				opener.setFeatures("height=700,width=1300,resizable");
				opener.doExtend(btnGo);
				setSopener(opener);
			}
			else{
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,intimationNo);					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
	
				EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(OMPPreviousClaimWindowUI.class);
				opener.popupBlockerWorkaround(true);
				opener.withShortcut(callPreviousClaimsDetailsViewSListener());
				opener.setFeatures("height=700,width=1300,resizable");
				opener.doExtend(btnGo);
				setSopener(opener);
			}
			
			btnGo.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					getSopener().open();				
				}
			});
		}
	}
	
	private void addViewDetailsSelectListener()
	{
		viewDetailsSelect.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String dropDownValue = (String) event.getProperty().getValue();
				if(null != dropDownValue)
				{
					//String dropDownValue = value.getValue();
					if(!("Previous Claim Details").equalsIgnoreCase(dropDownValue))
					{

						if (horizontalLayout1 != null
								&& horizontalLayout1.getComponentCount() > 0) {
							horizontalLayout1.removeAllComponents();
						}
						
						horizontalLayout1 = new HorizontalLayout(
								viewDetailsForm, goButton);
						
						if (mainPanel != null
						&& mainPanel.getComponentCount() > 0) {
							mainPanel.removeAllComponents();
						}
						mainPanel.addComponent(horizontalLayout1);
						mainPanel.setComponentAlignment(horizontalLayout1,
								Alignment.MIDDLE_RIGHT);
					}
					else
					{
						
						if (horizontalLayout1 != null
								&& horizontalLayout1.getComponentCount() > 0) {
							horizontalLayout1.removeAllComponents();
						}
						
						horizontalLayout1 = new HorizontalLayout(
								viewDetailsForm, btnGo);
						
						if (mainPanel != null
						&& mainPanel.getComponentCount() > 0) {
							mainPanel.removeAllComponents();
						}
						mainPanel.addComponent(horizontalLayout1);
						mainPanel.setComponentAlignment(horizontalLayout1,
								Alignment.MIDDLE_RIGHT);
						
					}
				}
			}
		});

		
		
		

	}
	
	public void initView(String intimationNo, ViewLevels level) {
		this.intimationNo = intimationNo;
		this.level = level;
		this.isOutpatient = false;
		this.rodKey = null;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
	}
	
	public void initView(String intimationNo, ViewLevels level,Boolean isOutPatient,String screenName) {
		this.intimationNo = intimationNo;
		this.level = level;
		this.isOutpatient=false;
		this.rodKey = null;
		this.screenName = screenName;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
	}

	public void initView(String intimationNo, Long rodKey, ViewLevels level,String screenName) {
		this.intimationNo = intimationNo;
		this.screenName = screenName;
		this.level = level;
		this.rodKey = rodKey;
		
		if(this.rodKey != null && this.rodKey.intValue() != 0){
			setRodNumber(this.rodKey);
		}
		else {
			this.rodNo ="%"+this.intimationNo+"%";
		}
		
		this.isOutpatient = false;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		getViewDetailsNativeSelect();
		
		addVaadinSessionAttribute();
		/*if(screenName.equals("Process Claim Billing") || screenName.equals("Process Claim Financials")){
			callPreviousClaimsDetailsViewSListener();
		}*/
	}

	public void initView(String intimationNo, Long rodKey, ViewLevels level,
			boolean isDisabled,String screenName) {
		
		
		this.viewDetailsSelect.removeAllItems();
		
		this.intimationNo = intimationNo;
		this.level = level;
		this.rodKey = rodKey;
		if(this.rodKey != null && this.rodKey.intValue() != 0){
			setRodNumber(this.rodKey);
		}	
		this.isOutpatient = false;
		isFVRDisabled = isDisabled;
		this.screenName = screenName;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		isOpRegLogin = false;
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
	}

	private void setRodNumber(Long rodKey) {
		if(this.rodKey != null){
			Reimbursement rodObj = reimbursementService.getReimbursement(rodKey);
			if(rodObj != null){
				this.rodNo = rodObj.getRodNumber();
			}
		}
	}
	
	public void initViewForRegisterClaim(OutPatientDTO opBean,String policyNumber,Long policyKey,String healthCardNo, ViewLevels level, Boolean isDisabled,Insured argInsured,Boolean oPflag){
		
		this.policyNumber = policyNumber;
		this.intimationNo = null;
		this.healthCardNo = healthCardNo;
		this.opBeanDto = opBean;
		this.policyKey = policyKey;
		this.level = level;
		this.btnDisabled = false;
		this.rodKey = null;
		this.isOutpatient = true;
		this.insuredSelected = argInsured;
//		this.insuredKey = argInsured.getKey();
		this.screenName = "OutPatient";
		listOfScreens.add("OutPatient");
		this.documentDetailsPageInstance = documentDetailsPageInstance;
		this.isNotHealthCheckUP = oPflag;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		isOpRegLogin = true;
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
		
		
	}
	
	public void initViewForProcessClaimRegistraionOP(String policyNumber,String intimationNo, Long policyKey, ViewLevels level, boolean isDisabled,Insured argInsured,Boolean oPflag) {
		
		this.policyNumber = policyNumber;
		this.policyKey = policyKey;
		this.intimationNo = intimationNo;
		this.level = level;
		this.btnDisabled = false;
		this.rodKey = null;
		this.isOutpatient = true;
		this.insuredSelected = argInsured;
		this.screenName = "OutPatient";
		listOfScreens.add("OutPatient");
		this.isNotHealthCheckUP = oPflag;
		isFVRDisabled = isDisabled;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		isOpRegLogin = false;
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
		
	}


	// @Inject
	// private Instance<ViewPEDEndorsementDetails> viewPEDEndorsementDetails;
	/***
	 * Fix for issue 756. Removing VIEW FVR DETAILS from drop down if the
	 * enhancement is Final. This is used in process enhancement screen.
	 */
	public void initView(String intimationNo, ViewLevels level,
			boolean isDisabled,String screenName) {
		
		this.viewDetailsSelect.removeAllItems();
		
		this.intimationNo = intimationNo;
		this.level = level;
		this.rodKey = null;
		this.policyKey = null;
		this.isOutpatient = false;
		this.screenName = screenName;
		isFVRDisabled = isDisabled;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
		
	}
	
	//lumen view init
	public void initView(String intimationNo, Long argPolicyKey, ViewLevels level,	boolean isDisabled,String screenName, boolean argLumenViewEnabled) {
		
		this.viewDetailsSelect.removeAllItems();
		
		this.intimationNo = intimationNo;
		this.level = level;
		this.rodKey = 0L;
		if(argPolicyKey == 0L){
			this.policyKey = null;
		}else{
			this.policyKey = argPolicyKey;
		}
		this.lumenPolicyKey = argPolicyKey;
		this.isOutpatient = false;
		this.screenName = screenName;
		isFVRDisabled = isDisabled;
		this.isLumenViewEnabled = argLumenViewEnabled;
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
		
	}
	
	public void initView(String intimationNo, ViewLevels level, String legalType) {
		this.intimationNo = intimationNo;
		this.level = level;
		this.legalType = legalType;
		this.clmAuditUser = false;
		this.clmAuditQryReplyUser = false;
		
		if(this.viewDetailsSelect != null){
			this.viewDetailsSelect.removeAllItems();
		}
		//viewDetailsSelect = new NativeSelect();
		getViewDetailsNativeSelect();
		addVaadinSessionAttribute();
	}
	
	private NativeSelect getViewDetailsNativeSelect() {
		//Vaadin8-setImmediate() viewDetailsSelect.setImmediate(true);
		viewDetailsSelect.setWidth("180px");
		
		List<String> listOfCallRecordingScreens = new ArrayList<>();
		listOfCallRecordingScreens.add("First Level Processing (Pre-auth)");
		listOfCallRecordingScreens.add("Process Pre-authorization");
		listOfCallRecordingScreens.add("First Level Processing (Enhancement)");
		listOfCallRecordingScreens.add("Process Enhancement");
		
		listOfCallRecordingScreens.add("Process Claim Request (Zonal Medical Review)");
		listOfCallRecordingScreens.add("Process Claim Request");
		
		listOfCallRecordingScreens.add("Process Claim Billing");
		listOfCallRecordingScreens.add("Process Claim Financials");
		
		listOfCallRecordingScreens.add("Draft Rejection Letter");
		listOfCallRecordingScreens.add("Process Draft Rejection Letter");
		
		
		
		ImsUser imsUser = null;
		String[] userRoles = null;
		if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
			imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
			userRoles = imsUser.getUserRoleList();
		}
		
		Long productIdforPSWR =0l;
        Intimation intimationforPSWR = intimationService.getIntimationByNo(intimationNo);
        if(intimationforPSWR!=null){
                productIdforPSWR= intimationforPSWR.getPolicy().getProduct().getKey();
        }
		/**
		 * Need to add only the view displayed at the intimation level
		 */
		if (ViewLevels.CLOSE_CLAIM.getIndex() == this.level.getIndex()) {
			
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
			viewDetailsSelect.addItem(VIEW_SUBLIMITS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			//viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
			viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER_UTILISATION);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_EXCLUSIONS_WAIVER);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
					
		}
		
		if(ViewLevels.PED_OUTSIDE_PROCESS.getIndex() == this.level.getIndex()){
			
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.setValue(VIEW_DOCUMENTS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
		}
		
		if (ViewLevels.OUPATIENT.getIndex() == this.level.getIndex()) {
//			if(!isOpRegLogin){
				
			viewDetailsSelect.addItem(VIEW_OP_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIMS_STATUS);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_POLICY);
//			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
//			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
//			viewDetailsSelect.addItem(VIEW_OP_BILL_SUMMARY);
//			viewDetailsSelect.addItem(VIEW_OP_BILL_ASSESMENTSHEET);
			
			
//			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
//			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
//			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
//			viewDetailsSelect.addItem(VIEW_OP_BALANCE_SUM_INSURED);
//			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
//			viewDetailsSelect.addItem(VIEW_DOCUMENTS);
//			viewDetailsSelect.addItem(VIEW_OP_BILL_SUMMARY);
//			viewDetailsSelect.addItem(VIEW_OP_BILL_ASSESMENTSHEET);
//			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
//			viewDetailsSelect.addItem(VIEW_PORTABLITY);
//			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
		}		 
		
		
		if(ViewLevels.UPLOAD_INVESTIGATION_REPORT.getIndex() == this.level.getIndex()){
			
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.setValue(VIEW_DOCUMENTS);		
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
		}	

		
		if (ViewLevels.PA_PROCESS.getIndex() == this.level.getIndex()) {
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
			viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
			viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);	
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
			viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItems(VIEW_COORDINATOR_REPLY);
			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
			//viewDetailsSelect.addItem(VIEW_UNNAMED_RISK_DETAILS);
			//viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
		}
		
		if(Arrays.asList(userRoles).contains(USER_ROLE_VIEW_ASSESSMENT_SHEET_VERSIONS) ) {  //TODO CLM_VIEW_BILL_ASSESSEMENT New Role
			viewDetailsSelect.addItem(BILLASSESSMENT_SHEET_VERSIONS);
		}	
		
		if(Arrays.asList(userRoles).contains(USER_ROLE_VIEW_CVC_AUDIT_TRAILS) ) {  //TODO CLM_CVC_AUDIT_TRAILS New Role
			viewDetailsSelect.addItem(CVC_CLAIM_AUDIT_TRAILS);
			this.clmAuditUser = true;
		}	
		else if(Arrays.asList(userRoles).contains(CLM_AUDIT_QRY_REPLY_CASHLESS)
				|| Arrays.asList(userRoles).contains(CLM_AUDIT_QRY_REPLY_MEDICAL)
				|| Arrays.asList(userRoles).contains(CLM_AUDIT_QRY_REPLY_BILLING_FA) ){ //TODO CLAIM PROCESSING USERS  for AUDITY QUERY Reply
			viewDetailsSelect.addItem(CVC_CLAIM_AUDIT_HISTORY);
			this.clmAuditQryReplyUser = true;
			this.clmAuditUser = false;
		}
		
		if(ViewLevels.CLAIM_STATUS.getIndex() == this.level.getIndex())
		{
			if(Arrays.asList(userRoles).contains(USER_ROLE_ACTIVITY_LOG)) {
				viewDetailsSelect.addItem(VIEW_ACTIVITY_LOG);	
			}
			if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
				viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
			}
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_ICR_DETAILS);
			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
			viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
			viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCTOR_REMARKS);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);	
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
			viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItems(VIEW_COORDINATOR_REPLY);
			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
			viewDetailsSelect.addItem(VIEW_SUBLIMITS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
			viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
			viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);	
			viewDetailsSelect.addItem(VIEW_HRM_DETAILS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			viewDetailsSelect.addItem(VIEW_POST_CASHLESS_REMARK);
			viewDetailsSelect.addItem(VIEW_NEGOTIATION_AMOUNT_DETAILS);
			viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
			viewDetailsSelect.addItem(VIEW_ICAC_REPONSE_DETAILS);
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
			rodNo = "%"+this.intimationNo+"%";
		}
		
		if(ViewLevels.ACK_ROD_PROCESS.getIndex() == this.level.getIndex())
		{
			if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
				viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
			}
			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_ICR_DETAILS);
			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
			viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
			viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCTOR_REMARKS);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);	
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
			viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItems(VIEW_COORDINATOR_REPLY);
			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
			viewDetailsSelect.addItem(VIEW_MEDICAL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SUBLIMITS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
			viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER_UTILISATION);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
			viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);	
			viewDetailsSelect.addItem(VIEW_HRM_DETAILS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_EXCLUSIONS_WAIVER);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(VIEW_POST_CASHLESS_REMARK);
			viewDetailsSelect.addItem(VIEW_NEGOTIATION_AMOUNT_DETAILS);
			viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
			viewDetailsSelect.addItem(VIEW_ICAC_REPONSE_DETAILS);
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
					
		}
		
		 
		if (ViewLevels.INTIMATION.getIndex() <= this.level.getIndex() && ViewLevels.OUPATIENT.getIndex() != this.level.getIndex()
				&& ViewLevels.CLOSE_CLAIM.getIndex() != this.level.getIndex() && ViewLevels.PED_OUTSIDE_PROCESS.getIndex() != this.level.getIndex()
				&& ViewLevels.UPLOAD_INVESTIGATION_REPORT.getIndex() != this.level.getIndex()
				&& ViewLevels.OMP.getIndex() != this.level.getIndex() && ViewLevels.PA_PROCESS.getIndex() != this.level.getIndex() &&
				ViewLevels.CLAIM_STATUS.getIndex() != this.level.getIndex() &&
				ViewLevels.ACK_ROD_PROCESS.getIndex() != this.level.getIndex()) {

			viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
			viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
			viewDetailsSelect.addItem(VIEW_ICR_DETAILS);
			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
			viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
			viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
			viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
			viewDetailsSelect.addItem(VIEW_DOCTOR_REMARKS);
			viewDetailsSelect.addItem(VIEW_DOCUMENTS);	
			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
			viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItems(VIEW_COORDINATOR_REPLY);
			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
			viewDetailsSelect.addItem(VIEW_MEDICAL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_SUBLIMITS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
			viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER_UTILISATION);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
			viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);
			viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);	
			viewDetailsSelect.addItem(VIEW_HRM_DETAILS);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_EXCLUSIONS_WAIVER);
			viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
			if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
				viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
			}
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(VIEW_POST_CASHLESS_REMARK);
			viewDetailsSelect.addItem(VIEW_NEGOTIATION_AMOUNT_DETAILS);
			viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);	
			if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
			}
			viewDetailsSelect.addItem(VIEW_ICAC_REPONSE_DETAILS);
			viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
		}

		if(ViewLevels.OMP.getIndex() == this.level.getIndex()){
			
			viewDetailsSelect.removeAllItems();
			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
//			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
			viewDetailsSelect.addItem(VIEW_PROPOSAL);
			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
			viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
			viewDetailsSelect.addItem(VIEW_INTIMATION);
			viewDetailsSelect.addItem(VIEW_PORTABLITY);
			viewDetailsSelect.addItem(VIEW_POLICY);
			viewDetailsSelect.addItem(VIEW_BASE_POLICY);
			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
			viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
//			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
			viewDetailsSelect.addItem(VIEW_EARLIER_ROD_DETAILS);
			viewDetailsSelect.addItem(VIEW_DEDUCTIBLES);
			viewDetailsSelect.addItem(VIEW_CLAIMANT_DETAILS);
			viewDetailsSelect.addItem(VIEW_OTHER_CURRENCY_RATE_HISTORY);
//			viewDetailsSelect.addItem(VIEW_OMP_INTIMATION_DETAILS);
			viewDetailsSelect.addItem(VIEW_ENDORSEMENT_DETAILS);
//			viewDetailsSelect.addItem(VIEW_BALANCE_SI_DETAILS);
//			viewDetailsSelect.addItem(VIEW_OMP_RISK_DETAILS);
			viewDetailsSelect.addItem(CUMULATIVE_BONUS);
			viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
			viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
			viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
			viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
			
			
			
		}

		if (ViewLevels.REGISTRATION.getIndex() <= this.level.getIndex() && ViewLevels.OUPATIENT.getIndex() != this.level.getIndex()
				&& ViewLevels.CLOSE_CLAIM.getIndex() != this.level.getIndex() && ViewLevels.PED_OUTSIDE_PROCESS.getIndex() != this.level.getIndex() && ViewLevels.PA_PROCESS.getIndex() != this.level.getIndex() && ViewLevels.CLAIM_STATUS.getIndex() != this.level.getIndex()) {

//			viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
//			viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
//			viewDetailsSelect.addItem(VIEW_SUBLIMITS);
//			viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
			
			
		}

		if (ViewLevels.PREAUTH_MEDICAL.getIndex() <= this.level.getIndex() && ViewLevels.OUPATIENT.getIndex() != this.level.getIndex()
				&& ViewLevels.CLOSE_CLAIM.getIndex() != this.level.getIndex() && ViewLevels.PED_OUTSIDE_PROCESS.getIndex() != this.level.getIndex() && ViewLevels.PA_PROCESS.getIndex() != this.level.getIndex() && ViewLevels.CLAIM_STATUS.getIndex() != this.level.getIndex()) {
			
//			viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
//			viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
//			viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
//			viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
//			viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
//			viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
//			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
//			viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
//			viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
//			viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
//			viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
//			viewDetailsSelect.addItem(VIEW_DOCUMENTS);	
//			viewDetailsSelect.addItem(VIEW_MER_DETAILS);
//			viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
//			viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
//			viewDetailsSelect.addItems(VIEW_COORDINATOR_REPLY);
//			viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
//			viewDetailsSelect.addItem(VIEW_MEDICAL_SUMMARY_DETAILS);
//			viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
//			viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
//			viewDetailsSelect.addItem(VIEW_INTIMATION);
//			viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
//			viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);
//			viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);

		}
		

		viewDetailsSelect.setValue(VIEW_DOCUMENT_DETAILS);

		if(ViewLevels.PED_OUTSIDE_PROCESS.getIndex() != this.level.getIndex()){
			viewDetailsSelect.setValue(VIEW_DOCUMENT_DETAILS);
		}
		
		if (ViewLevels.LEGAL_CLAIMS.getIndex() == this.level.getIndex()) {
			viewDetailsSelect.removeAllItems();
			viewDetailsSelect.addItem(LEGAL_TRAILS);
		}
		
		if (ViewLevels.LUMEN_TRAILS.getIndex() == this.level.getIndex()) {
			viewDetailsSelect.removeAllItems();
			
			/*Views Added in CR-R1085 - VIEW_TOP_PANEL_DETAILS,VIEW_POLICY,VIEW_POLICY_SCHEDULE,VIEW_PORTABLITY_POLICY_DETAILS,VIEW_QUERY_DETAILS,VIEW_SPECIALITY_OPINION,
			VIEW_DOCTOR_REMARKS,VIEW_RISK_DETAILS,VIEW_EARLIER_ACKNOWLEDGE_DETAILS,VIEW_64VB_COMPLIANCE,VIEW_BILL_DETAILS,VIEW_IRDA_CATEGORY_DETAILS,VIEW_HRM_DETAILS,VIEW_PORTABLITY,
			VIEW_ACKNOWLEDGMENT_DETAILS,VIEW_IRDA_NON_PAYABLES,VIEW_COORDINATOR_REPLY,VIEW_TRANSLATION_MISC_REQUEST,VIEW_CORPORATE_BUFFER_UTILISATION,VIEW_STOPLOSS_DETAILS,VIEW_CLAIM_HISTORY,
			VIEW_INVESTIGATION_DETAILS,VIEW_BILL_SUMMARY_DETAILS*/
			if(intimationNo != null && policyKey == null){
				viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
				viewDetailsSelect.addItem(VIEW_CLAIM_STATUS); // doubt
				viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
				viewDetailsSelect.addItem(VIEW_POLICY);
				viewDetailsSelect.addItem(VIEW_BASE_POLICY);
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
				viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
				viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
				viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
				viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY); // doubt
				viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
				viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
				viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
				viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
				viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
				viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
				viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS); // doubt
				viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
				viewDetailsSelect.addItem(VIEW_DOCTOR_REMARKS);
				viewDetailsSelect.addItem(VIEW_DOCUMENTS);
				viewDetailsSelect.addItem(VIEW_MER_DETAILS);
				viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
				viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
				viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
				viewDetailsSelect.addItem(VIEW_COORDINATOR_REPLY);
				viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
				viewDetailsSelect.addItem(VIEW_SUBLIMITS);
				viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
				viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
				viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER_UTILISATION);
				viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
				viewDetailsSelect.addItem(VIEW_INTIMATION);
				viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
				viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);// doubt
				viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);
				viewDetailsSelect.addItem(VIEW_HRM_DETAILS);
				viewDetailsSelect.addItem(VIEW_PORTABLITY);
				viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
				if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
					viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
				}
				
				if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
					viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
				}
				viewDetailsSelect.addItem(CUMULATIVE_BONUS);

				if(isLumenViewEnabled){
					viewDetailsSelect.addItem(VIEW_LUMEN_TRAILS);
				}
				viewDetailsSelect.setValue(VIEW_DOCUMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
				viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
				viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
				viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
				viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
			}else if(intimationNo != null && (policyKey != null && policyKey > 0L)){
				viewDetailsSelect.addItem(VIEW_TOP_PANEL_DETAILS);
				viewDetailsSelect.addItem(VIEW_CLAIM_STATUS); // doubt
				viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
				viewDetailsSelect.addItem(VIEW_POLICY);
				viewDetailsSelect.addItem(VIEW_BASE_POLICY);
				viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE);
				viewDetailsSelect.addItem(VIEW_PORTABLITY_POLICY_DETAILS);
				viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
				viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
				viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY); // doubt
				viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
				viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
				viewDetailsSelect.addItem(VIEW_DOCUMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
				viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
				viewDetailsSelect.addItem(VIEW_QUERY_DETAILS);
				viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
				viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS); // doubt
				viewDetailsSelect.addItem(VIEW_PED_REQUEST_DETAILS);
				viewDetailsSelect.addItem(VIEW_DOCTOR_REMARKS);
				viewDetailsSelect.addItem(VIEW_DOCUMENTS);
				viewDetailsSelect.addItem(VIEW_MER_DETAILS);
				viewDetailsSelect.addItem(VIEW_RISK_DETAILS);
				viewDetailsSelect.addItem(VIEW_EARLIER_ACKNOWLEDGE_DETAILS);
				viewDetailsSelect.addItem(VIEW_ACKNOWLEDGMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_IRDA_NON_PAYABLES);
				viewDetailsSelect.addItem(VIEW_COORDINATOR_REPLY);
				viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
				viewDetailsSelect.addItem(VIEW_SUBLIMITS);
				viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
				viewDetailsSelect.addItem(VIEW_64VB_COMPLIANCE);
				viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER_UTILISATION);
				viewDetailsSelect.addItem(VIEW_STOPLOSS_DETAILS);
				viewDetailsSelect.addItem(VIEW_INTIMATION);
				viewDetailsSelect.addItem(VIEW_BILL_DETAILS);
				viewDetailsSelect.addItem(VIEW_BILL_SUMMARY_DETAILS);// doubt
				viewDetailsSelect.addItem(VIEW_IRDA_CATEGORY_DETAILS);
				viewDetailsSelect.addItem(VIEW_HRM_DETAILS);
				viewDetailsSelect.addItem(VIEW_PORTABLITY);
				viewDetailsSelect.addItem(CONTINUITY_BENEFITS);
				if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
					viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
				}
				viewDetailsSelect.addItem(CUMULATIVE_BONUS);

				if(isLumenViewEnabled){
					viewDetailsSelect.addItem(VIEW_LUMEN_TRAILS);
				}
				viewDetailsSelect.setValue(VIEW_DOCUMENT_DETAILS);
				viewDetailsSelect.addItem(VIEW_POST_CASHLESS_REMARK);
				viewDetailsSelect.addItem(VIEW_NEGOTIATION_AMOUNT_DETAILS);
				viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);
				if(productIdforPSWR !=null && ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(productIdforPSWR)){
					viewDetailsSelect.addItem(VIEW_POLICY_SCHEDULE_WITHOUT_RISK);
				}
				viewDetailsSelect.addItem(VIEW_ICAC_REPONSE_DETAILS);
				viewDetailsSelect.addItem(VIEW_DATA_VALIDATION_DETAILS);	
				viewDetailsSelect.addItem(VIEW_PCC_TRAILS);
				viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
				viewDetailsSelect.addItem(VIEW_REC_MARKETING_ESCALATION);
				viewDetailsSelect.addItem(VIEW_BUSINESS_PROFILE_CHART);
				viewDetailsSelect.addItem(VIEW_TALK_TALK_TABLE);
			}else{
				viewDetailsSelect.addItem(VIEW_POLICY);
				viewDetailsSelect.addItem(VIEW_BASE_POLICY);
				if(isLumenViewEnabled){
					viewDetailsSelect.addItem(VIEW_LUMEN_TRAILS);
				}
				if(Arrays.asList(userRoles).contains(USER_ROLE_ESCLATE_RAW)){
					viewDetailsSelect.addItem(VIEW_REPLIED_RAW);
				}
			}
			
		}
		
		if(!StringUtils.isBlank(screenName)) {
			if(screenName.equals(MenuItemBean.PROCESS_PREAUTH) || screenName.equals(MenuItemBean.PROCESS_ENHANCEMENT) ||
			   screenName.equals(MenuItemBean.PROCESS_CLAIM_REQUEST) || screenName.equals(MenuItemBean.PROCESS_CLAIM_FINANCIALS) ||
			   screenName.equals(MenuItemBean.PA_PROCESS_PREAUTH) || screenName.equals(MenuItemBean.PA_PROCESS_ENHANCEMENT) ||
			   screenName.equals(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST) || screenName.equals(MenuItemBean.PA_PROCESS_CLAIM_REQUEST) ||
			   screenName.equals(MenuItemBean.PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL) || screenName.equals(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS)){
				viewDetailsSelect.addItem(VIEW_HOSPITAL_SPOC_DETAILS);	
			}
		}
		
		if (ViewLevels.FVR_GRADING_PROCESS.getIndex() == this.level.getIndex()) {
			viewDetailsSelect.removeAllItems();
			viewDetailsSelect.addItem(VIEW_FVR_DETAILS);
		}
		
		if (ViewLevels.HRM_DIVISION_HEAD_REMARKS.getIndex() == this.level.getIndex()) {
			viewDetailsSelect.removeAllItems();
			viewDetailsSelect.addItem(VIEW_HRM_DIVISION_HEAD_REMARKS);
		}
		
		if(!StringUtils.isBlank(screenName)){
			if(screenName.indexOf("Trails Page loaded") <= 0){
				if(!listOfScreens.contains(screenName)){
					viewDetailsSelect.addItem(VIEW_BILLING_INTERNAL_REMARKS);
					viewDetailsSelect.addItem(VIEW_FA_INTERNAL_REMARKS);
				}
			}else if(screenName.indexOf("Trails Page loaded") > 0){
				if(screenName.indexOf("Policy") == -1){ //Lumen Policy Trails Page loaded
					if(intimationNo != null && policyKey != null && lumenPolicyKey > 0L){
						viewDetailsSelect.addItem(VIEW_BILLING_INTERNAL_REMARKS);
						viewDetailsSelect.addItem(VIEW_FA_INTERNAL_REMARKS);
					}
					if(intimationNo != null && policyKey == null && lumenPolicyKey == 0L){
						viewDetailsSelect.addItem(VIEW_BILLING_INTERNAL_REMARKS);
						viewDetailsSelect.addItem(VIEW_FA_INTERNAL_REMARKS);
					}
				}
			}
			System.out.println("screenName : "+screenName);
			if(listOfCallRecordingScreens.contains(screenName)){
				ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
				isTVCLogin = user.getFilteredRoles().contains("CLM_TVC");
				
				if(isTVCLogin){
					viewDetailsSelect.addItem(VIEW_CALL_RECORDING);
				}
			}
			
			viewDetailsSelect.addItem(VIEW_GRIEVANCE_TRAILS);
			viewDetailsSelect.addItem(VIEW_ESCALATION_TRAILS);
		}
		//GAL-1157
		viewDetailsSelect.addItem(SOURCE_CHANNEL_DETAILS);
				
		return viewDetailsSelect;
	}

	public ViewLevels getLevel() {
		return level;
	}

	public void setLevel(ViewLevels level) {
		this.level = level;
	}

	public Button getGoButton(final NativeSelect viewDetailsSelect) {
		Button goButton = new Button();
		goButton.setCaption("Go");
		//Vaadin8-setImmediate() goButton.setImmediate(true);
		
		goButton.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				 opener.extend(dummyButton);
				if (viewDetailsSelect.getValue() != null) {
					switch (viewDetailsSelect.getValue().toString()) {
					
					case VIEW_ACTIVITY_LOG:
						if (intimationNo != null) {
							getActivityLog(intimationNo);
						}
						break;
					case VIEW_TOP_PANEL_DETAILS:
						if(isOutpatient){
							if(intimationNo != null){
								getOPViewTopPanelDetails(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
							
						}
						else if (intimationNo != null) {
							getViewTopPanelDetails(intimationNo);
						}
						break;
						
					case VIEW_POLICY:
						if (isOutpatient) {	
								getViewPolicy(policyKey);
						}else{
							if (intimationNo != null) {
								getViewPolicy(intimationNo);
							} else if(policyKey !=  null){
								getViewPolicy(policyKey);
							}
						}

						break;	
					case VIEW_BASE_POLICY:
						/*if (isOutpatient) {	
								getViewBasePolicy(policyKey);
						}else{*/
							if (intimationNo != null) {
								getViewBasePolicy(intimationNo);
							/*} else if(policyKey !=  null){
								getViewBasePolicy(policyKey);
							}*/
						}

						break;	
					case VIEW_DOCUMENTS:
						if (!isOutpatient) {
							getViewDocument(intimationNo);
						
						}else{
							getViewDocumentByPolicyNo(policyNumber);
						}
						break;
						
					case VIEW_PORTABLITY:
						if (isOutpatient) {
							getPortabilityViewtByPolicyNo(policyNumber);
						}else{
							getPortabilityView(intimationNo);
						
						}
						break;
						
					case VIEW_PORTABLITY_POLICY_DETAILS:
						getPortablityDetails(intimationNo);
						break;
						
					//for continuity benefit
					case CONTINUITY_BENEFITS:	
					getContinuityBenefitDetails(intimationNo);
					break;
						
					case VIEW_CLAIM_HISTORY:
						if (!isOutpatient) {
								getViewClaimHistory(intimationNo);
							
						}else{
							if (intimationNo != null) {
								getViewClaimHistoryForOPHealthCheckUp(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
							
						}
					break;
					case VIEW_CLAIM_STATUS:
//						viewClaimStatusUpdated(intimationNo);
						viewSearchClaimStatus(intimationNo,rodKey);
						break;

					case VIEW_DOCUMENT_DETAILS:
						if (isOutpatient) {
							if (intimationNo != null) {
								viewUploadedDocumentDetails(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
						}else{
							viewUploadedDocumentDetails(intimationNo);
						}
						
						break;


					case VIEW_PRODUCT_BENEFITS:
						if(isOutpatient){
							if(policyKey != null){	
								getViewProductBenefits(policyKey);
							}
						}else{
							if (intimationNo != null) {
								getViewProductBenefits(intimationNo);
							} else {
								getViewProductBenefits(policyKey);
							}
						}

						break;
					case VIEW_CO_PAY_DETAILS:
						if(intimationNo != null){
							getViewCoPay(intimationNo);
						}else{
							getViewCoPayForOP();
						}
						break;
					case VIEW_MER_DETAILS:
						if(isOutpatient){
							if(intimationNo != null){	
								getMERDetails(intimationNo);
							} else {
								getMERDetailsByHealthCard(opBeanDto.getInsuredDto().getHealthCardNumber());
							}
						}else{
							getMERDetails(intimationNo);
						}
						break;
					case VIEW_RISK_DETAILS:
						if(isOutpatient){
							getOPViewRiskDetails(intimationNo,opBeanDto);
						} else {
							getViewRiskDetails(intimationNo);
						}
						
						break;
					case VIEW_INTIMATION:
						getViewIntimation(intimationNo);
						break;
					case VIEW_HOSPITAL_DETAILS:
						getViewHospitalDetails(intimationNo);
						break;
					case VIEW_PREVIOUS_CLAIM_DETAILS:
						if (!isOutpatient) {
							getViewPreviousClaimDetails(intimationNo);
						}else{
							getClaimByPolicyInsuredWiseForOP(policyKey,intimationNo,key);
						}
						break;
					case VIEW_BALANCE_SUM_INSURED:
//						 if(!isOutpatient) {
						getViewBalanceSumInsured(intimationNo);
//						}
						break;
						
					case VIEW_PED_REQUEST_DETAILS:
						getPedRequestDetails(intimationNo);
						break;
						
					case VIEW_DOCTOR_REMARKS:
						getDoctorRemarks(intimationNo);
						break;
						
					case VIEW_SUBLIMITS:
						//getSublimits(intimationNo);
						getComprehensiveSublimits(intimationNo);
						break;
						
					case VIEW_PREVIOUS_INSURANCE:
						if(isOutpatient && policyKey != null){
							getViewPreviousInsuranceDetailsForOp();
						}else{
							if(intimationNo != null){
								getViewPreviousInsuranceDetails();
							}
						}
						break;
					case VIEW_TRANSLATION_MISC_REQUEST:
						getTranslationMiscRequest(intimationNo);
						break;

					case VIEW_INVESTIGATION_DETAILS:
						getInvestigationDetails(intimationNo, isFVRDisabled);
						break;

					case VIEW_QUERY_DETAILS:
						//getQueryDetailsTable(intimationNo);
						getRevisedQueryDetailsTable(intimationNo);
						break;

					case VIEW_IRDA_NON_PAYABLES:
						getIrdaNonPayables(intimationNo);
						break;

					case VIEW_64VB_COMPLIANCE:
						getIrda64VbDetails(intimationNo);
						break;

					case VIEW_COORDINATOR_REPLY:
						getViewCoordinatorReply(intimationNo);
						break;

					case VIEW_BILL_DETAILS:
						getViewBillDetails(intimationNo);
						break;
					
					case VIEW_ACKNOWLEDGMENT_DETAILS:
						getAcknowledgementDetailsView(intimationNo);
						break;
						
					case VIEW_IRDA_CATEGORY_DETAILS:
						getIrdaCategoryBillDetails(intimationNo);
						break;
					case VIEW_EARLIER_ACKNOWLEDGE_DETAILS:
						getEarlierAcknowledgmentDetails(intimationNo);
						break;
					case VIEW_BILL_SUMMARY_DETAILS:
						if(!isOutpatient){
							getBillSummaryDetails(intimationNo);
						}
						break;
						
					case VIEW_OP_BILL_SUMMARY:
						if(isOutpatient){
							if(intimationNo != null){	
								getOPBillSummary(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
						}
						break;
						
					case VIEW_OP_BALANCE_SUM_INSURED:
						if(isOutpatient){
							if(intimationNo != null){	
								getOPBalanceSumInsured(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
						}
						break;
					case VIEW_CLAIMS_STATUS:
						if(isOutpatient){
							if(intimationNo != null){	
								viewOPClaimStatusUpdated(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
							
						}else {
							getClaimStatus(intimationNo);
						}
						break;
					case VIEW_OP_BILL_ASSESMENTSHEET:
						if(isOutpatient){
							if(intimationNo != null){	
								getBillAssesmentSheet(intimationNo);
							}
							else{
								getErrorMessage("Intimation Number Not Generated");
							}
						}
						break;
						
					case VIEW_MEDICAL_SUMMARY_DETAILS:
						getMedicalSummaryDetails(intimationNo);
						break;
						
					case VIEW_SPECIALITY_OPINION:
						getSpecialityOpinion(intimationNo);
						break;
					// case VIEW_CO_ORDINATOR_REPLAY:
					// getTranslationMiscRequest(intimationNo);
					// break;
					case VIEW_PRE_AUTH_DETAILS:
						// getPreAuthDetail(intimationNo);
						getPreAuthUpdateDetail(intimationNo); 
						break;
					case VIEW_FVR_DETAILS:
						getFVRDetails(intimationNo, isFVRDisabled);
						break;
					
					case VIEW_PREVIOUSCLAIM_DETAILS:
						
						break;
					case VIEW_DOC_DETAILS:
						getviewdocumentDetails(intimationNo);
						break;
					case VIEW_DEDUCTIBLES:
						getDeductibles(intimationNo);
						break;	
					case VIEW_HRM_DETAILS:
						
						Intimation intimation = intimationService.getIntimationByNo(intimationNo);
						Claim  claims = intimationService.getClaimforIntimation(intimation.getKey());
						if(claims != null){
						PreauthDTO preauthDTO = new PreauthDTO();
						NewIntimationDto newIntimationDto = new NewIntimationDto();
						List<Preauth> preauthList = preAuthService.getPreauthByIntimationKey(intimation.getKey());
						Claim claim = new Claim();
						 Preauth preauth = null;
						 PreMedicalMapper premedicalMapper = new PreMedicalMapper();
						if(null != preauthList && !preauthList.isEmpty())
						{
						 preauth = preauthList.get(0);
						
						 preauthDTO = premedicalMapper.getPreauthDTO(preauth);
						 claim = preauth.getClaim();
						}
						else
						{
							claim = claims;
						}
						if(null != intimation)
						{
						 newIntimationDto = intimationService.getIntimationDto(intimation);
						}
						preauthDTO.setNewIntimationDTO(newIntimationDto);
						
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("premedicalMapper", premedicalMapper);
						map.put("claim",claim);
						map.put("preauth", preauth);
						map.put("preauthDTO", preauthDTO);
						map.put("isEnabled",true);
						
						fireViewEvent(
								MenuPresenter.DIAGNOSIS_DETAILS,map );
				
						getEarlierHrmDetails(preauthDTO,intimationNo);}
						else{
							getErrorMessage("Hrm details not available");
						}
						break;	
						
					case VIEW_CLAIMANT_DETAILS:
						getviewClaimantDetails(intimationNo);
						break;
						
					case VIEW_OTHER_CURRENCY_RATE_HISTORY:
						getviewOtherCurrencyRateDetails(intimationNo,rodKey);
						break;
						
//					case VIEW_OMP_INTIMATION_DETAILS:
//						getViewOmpIntimation(intimationNo);
//						break;
						
					case VIEW_ENDORSEMENT_DETAILS:
						getViewEndorsementDetails(intimationNo);
						break;
						
					case VIEW_PROPOSAL:
						getProposalViewDocument(intimationNo);
						break;
					case VIEW_EARLIER_ROD_DETAILS:
						getOMPEarlierAcknowledgmentDetails(intimationNo);
						break;
									
						case LEGAL_TRAILS:
						getLegalTrails();
						break;		
						case VIEW_POLICY_SCHEDULE:
						if(!isOutpatient){
							if(intimationNo != null){	
									getViewPolicySchedule(intimationNo);
								}
							}
							else{
								PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
										.fetchPolicyScheduleFromPremia(policyNumber,0);
								if (fetchPolicyScheduleFromPremia != null
										&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
									String url = fetchPolicyScheduleFromPremia.getResultUrl();
									// getUI().getPage().open(url, "_blank");
									getUI().getPage().open(url, "_blank", 1550, 650,
											BorderStyle.NONE);
								} else {
									getErrorMessage("Policy Schedule Not Available");
								}

							}
							
						/*else {
							getClaimStatus(intimationNo);
						}*/
						
						break;
				/*	case VIEW_CORPORATE_BUFFER:
						//getSublimits(intimationNo);
						getViewCorpBufferDetails(intimationNo);
						break;*/
					case VIEW_STOPLOSS_DETAILS:
						//getSublimits(intimationNo);
						getViewStopLossDetails(intimationNo);
						break;
						
					case VIEW_EXCLUSIONS_WAIVER:
						getViewGmcExclusionDetails(intimationNo);
						break;
					case VIEW_CORPORATE_BUFFER_UTILISATION:
						
						getViewCorporateBufferUtilisationDetails(intimationNo);
						break;
					case VIEW_LUMEN_TRAILS:						
						getLumenTrails();
						break;
					case VIEW_BILLING_INTERNAL_REMARKS:					
						Intimation intimation1 = intimationService.getIntimationByNo(intimationNo);
						getBillingInternalRemarksTrials(intimation1.getKey(), ReferenceTable.BILLING_STAGE);
						break;
					case VIEW_FA_INTERNAL_REMARKS:					
						Intimation intimation2 = intimationService.getIntimationByNo(intimationNo);
						getFaInternalRemarksTrials(intimation2.getKey(), ReferenceTable.FINANCIAL_STAGE);
						break;
					case VIEW_ICR_DETAILS:
						Intimation intimationObj = intimationService.getIntimationByNo(intimationNo);
						getICRDetailsbyPolicyNumber(intimationObj.getPolicy().getPolicyNumber());
						break;
					case VIEW_REPLIED_RAW:
						getViewRepliedRAW(intimationNo);
						break;
						
					case CUMULATIVE_BONUS:
						getCumulativeBonusView(intimationNo);
						break;
						
					case BILLASSESSMENT_SHEET_VERSIONS:
						viewBillAssessmentVersionDetails(intimationNo, rodNo);
						break;	
					case VIEW_CALL_RECORDING:
						Intimation intiObj = intimationService.getIntimationByNo(intimationNo);
						viewCallRecordings(intiObj);
						break;
						
					case VIEW_GRIEVANCE_TRAILS:
						Intimation inti = intimationService.getIntimationByNo(intimationNo);
						viewGrievanceTrails(inti);
						break;
					case VIEW_ESCALATION_TRAILS:
						Intimation intima = intimationService.getIntimationByNo(intimationNo);
						viewEscalationTrails(intima);
						break;
						
					case CVC_CLAIM_AUDIT_TRAILS:
//						Intimation intimation = intimationService.getIntimationByKey(intimationKey);
						viewAuditClaimTrails(intimationNo, clmAuditUser);
						break;	
						
					case VIEW_POST_CASHLESS_REMARK:
						getPCCRemarks(intimationNo);
						break;

					case VIEW_HOSPITAL_SPOC_DETAILS:
						getHospSPOCDetails(intimationNo);
						break;	
						
					case CVC_CLAIM_AUDIT_HISTORY:
//						Intimation intimation = intimationService.getIntimationByKey(intimationKey);
						viewAuditClaimTrails(intimationNo, clmAuditUser);
						break;

					case VIEW_NEGOTIATION_AMOUNT_DETAILS:
						getNegotiationAmountDetails(intimationNo);
						break;
						
					case VIEW_DATA_VALIDATION_DETAILS:
						getDataCorrectionDetails(intimationNo);
						break;	
					case VIEW_POLICY_SCHEDULE_WITHOUT_RISK:	
						if(intimationNo != null){	
							getViewPolicyScheduleWithoutRisk(intimationNo);
						}
						break;
					case VIEW_ICAC_REPONSE_DETAILS:
						getICACResponseDetails(intimationNo);
						break;	
					case VIEW_PCC_TRAILS:
						getPccTrails(intimationNo);
						break;
					case VIEW_HRM_DIVISION_HEAD_REMARKS:
						Intimation intimation3 = intimationService.getIntimationByNo(intimationNo);
						getHRMDivisionHeadRemarks(intimation3);	
					break;
					case VIEW_REC_MARKETING_ESCALATION:
						getRecMarkEscHistory(intimationNo);
					break;
					case VIEW_BUSINESS_PROFILE_CHART:
						
						Intimation intimationObjByKey = intimationService.getIntimationByNo(intimationNo);
						PreauthDTO preauthDTO = new PreauthDTO();
						NewIntimationDto newIntimationDto = new NewIntimationDto();
						if(null != intimationObjByKey)
						{
						 newIntimationDto = intimationService.getIntimationDto(intimationObjByKey);
						}
						preauthDTO.setNewIntimationDTO(newIntimationDto);
						
						getBusinessProfileChart(intimationNo,preauthDTO);
						
						break;
					case VIEW_TALK_TALK_TABLE:
						Intimation intimationByKey = intimationService.getIntimationByNo(intimationNo);
						PreauthDTO preauthdto = new PreauthDTO();
						NewIntimationDto newIntimationDTO = new NewIntimationDto();
						if(null != intimationByKey)
						{
						 newIntimationDto = intimationService.getIntimationDto(intimationByKey);
						}
						preauthdto.setNewIntimationDTO(newIntimationDTO);
						
						getTalkTalkTalkDetails(intimationNo,preauthdto);
						//getTalkTalkTable(intimationNo);
						
						break;
						
					case SOURCE_CHANNEL_DETAILS:
						getIntimationSourceDetails(intimationNo);
						break;
					}
				} else {
				}
			}
		});
		return goButton;
	}
	
	public void getViewPolicyScheduleWithoutRisk(String intimationNo) {
		// TODO Auto-generated method stub

		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		String PrintRiskYesNo="N";

		if (intimation != null && intimation.getPolicy() != null
				&& intimation.getPolicy().getPolicyNumber() != null) {

			PremPolicySchedule fetchPolicyScheduleWithoutRsikFromPremia = policyService
					.fetchPolicyScheduleWithoutRiskFromPremia(intimation.getPolicy()
							.getPolicyNumber(), -1, PrintRiskYesNo);
			if (fetchPolicyScheduleWithoutRsikFromPremia != null
					&& fetchPolicyScheduleWithoutRsikFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleWithoutRsikFromPremia.getResultUrl();
				getUI().getPage().open(url, "_blank", 1550, 650,
						BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}

		}else{
			getErrorMessage("Policy Not Available");
		}

	
	}

	public DocumentCheckListDTO getviewdocumentDetails(String intimationNo) {
		
		if(intimationNo != null){
		// TODO Auto-generated method stub
		OPClaim claimkey=claimService.getOPClaimByIntimationNo(intimationNo);
		OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claimkey.getKey());
		if(opHealthCheckup.getKey() !=null){
		List<OPDocumentList> opDocumentList = processOPRequestService.getOPDocumentListByClaimKey(opHealthCheckup.getKey());
		}
		
		
		 
		//List<OPHealthCheckup> opHealthACheckupList=new ArrayList<OPHealthCheckup>();
		List<ViewDocumentDetailsDTO> viewDocumentDetailsDTOsList=new ArrayList<ViewDocumentDetailsDTO>();
		
		ViewDocumentDetailsDTO viewDocumentDetailsDTO=new ViewDocumentDetailsDTO();
		
		MastersValue documentReceivedFromId = opHealthCheckup.getDocumentReceivedFromId();
		
		MastersValue modeOfReceipt=opHealthCheckup.getModeOfReceipt();
		
		SelectValue selecDocumentReceivedFromId=new SelectValue();
		selecDocumentReceivedFromId.setValue(documentReceivedFromId.getValue());
		viewDocumentDetailsDTO.setReceivedFrom(selecDocumentReceivedFromId);
		SelectValue selectModeOfReciept2=new SelectValue();
		selectModeOfReciept2.setValue(modeOfReceipt.getValue());
		viewDocumentDetailsDTO.setModeOfReceipt(selectModeOfReciept2);
		viewDocumentDetailsDTO.setDocumentReceivedDate(opHealthCheckup.getDocumentReceivedDate());
		viewDocumentDetailsDTO.setPersonemailID(opHealthCheckup.getPersonEmailId());
		viewDocumentDetailsDTO.setPersonContactNumber(opHealthCheckup.getPersonContactNumber());
		viewDocumentDetailsDTO.setAdditionalRemarks(opHealthCheckup.getAdditionalRemarks());
		viewDocumentDetailsDTOsList.add(viewDocumentDetailsDTO);
		
			
			Query query = entityManager.createNamedQuery("OPDocumentList.findByHealthCheckupKey");
			query.setParameter("healthCheckupKey", opHealthCheckup.getKey());
			@SuppressWarnings("unchecked")
			List<OPDocumentList> resultList =  query.getResultList();
			List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
			int i=1;
			if(resultList != null && !resultList.isEmpty()){
			
			for(OPDocumentList opDocument : resultList){
			
				//entityManager.refresh(opDocument);
				DocumentCheckListDTO dto = new DocumentCheckListDTO();
				dto.setSlNo(i);
			
			SelectValue receiveStatus = new SelectValue(opDocument.getReceivedStatusId().getKey(), opDocument.getReceivedStatusId().getValue());
			dto.setReceivedStatus(receiveStatus);
			dto.setNoOfDocuments(opDocument.getNumberOfDocuments());
			dto.setRemarks(opDocument.getRemarks());
			Long documentTypeId = opDocument.getDocumentTypeId();
			
			DocumentCheckListMaster documentCheckListMaster = getDocumentCheckListMaster(documentTypeId);
			if(documentCheckListMaster != null){
				dto.setValue(documentCheckListMaster.getValue());
				if(documentCheckListMaster.getMandatoryDocFlag() != null && documentCheckListMaster.getMandatoryDocFlag().equalsIgnoreCase("Y")){
					dto.setMandatoryDocFlag("Yes");
				}else{
					dto.setMandatoryDocFlag("No");
				}
				
				dto.setRequiredDocType(documentCheckListMaster.getRequiredDocType());
				
			}
			documentCheckListDTOList.add(dto);
			i++;
			
			}
			
			}
			
		
		if(null!=viewDocumentDetailsDTOsList && null!=documentCheckListDTOList){
		viewDocumentDetailsPage.init(viewDocumentDetailsDTOsList,documentCheckListDTOList);
		}
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Document Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent( viewDocumentDetailsPage);
		popup.setClosable(true);
		
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}
		else {
			getErrorMessage("DocumentDetails is not available in Register Claim");
		}
		
		return null;
	}

		
	
	public DocumentCheckListMaster getDocumentCheckListMaster(Long key){
		
		Query query = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
		query.setParameter("primaryKey", key);
		
		List<DocumentCheckListMaster> resultList =  (List<DocumentCheckListMaster>)query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	protected void getClaimByPolicyInsuredWiseForOP(Long policyKey,String intimationNo,Long key){
		
		
		VerticalLayout vLayout = new VerticalLayout();
		if(policyKey != null ){
			
			viewpreviousclaimPage.showValuesForOP(policyKey,intimationNo);
			viewpreviousclaimPage.setDocumentDetailsPageInstance(this.documentDetailsPageInstance);
		}else{
			Intimation intimationByNo = intimationService.getIntimationByNo(this.intimationNo);
			Policy policy = intimationByNo.getPolicy();
			viewpreviousclaimPage.showValuesForOP(policy.getKey(),intimationNo);
		} 
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Previous Claim");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(viewpreviousclaimPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	
	

	public void getPAacknowledgementDetailsView(String intimationNo) {

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Boolean isAckAvaliable = false;
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		
		DocAcknowledgement docAcknowledgement = null;
		
		if (null != claimKey) {
			docAcknowledgement = reimbursementService
					.findAcknowledgment(this.rodKey);
			if (null != docAcknowledgement) {
				isAckAvaliable = true;
				
			} else if(this.acknowledgementKey != null){
				
				isAckAvaliable = true;
				docAcknowledgement = reimbursementService.getDocAcknowledgementBasedOnKey(this.acknowledgementKey);
				
			}else {
				isAckAvaliable = true;
				docAcknowledgement = reimbursementService
						.findAcknowledgmentByClaimKey(claimKey);
			}
			if(docAcknowledgement != null){
				viewPAacknowledgementPage.init(intimation,
					docAcknowledgement.getKey());
			}

		}
		if (isAckAvaliable && docAcknowledgement !=null) {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Acknowledgement Details");
			popup.setWidth("75%");
			popup.setHeight("90%");
			popup.setContent(viewPAacknowledgementPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewPAacknowledgementPage!=null){
						viewPAacknowledgementPage.clearObjects();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("No Records Found");
		}
	}
	
	
	public void getAcknowledgementDetailsView(String intimationNo) {
		
			Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		//If PA
		if(intimation.getProcessClaimType() !=null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			getPAacknowledgementDetailsView(intimationNo);
		}
		
		else
		{
		Boolean isAckAvaliable = false;
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		
		DocAcknowledgement docAcknowledgement = null;
		
		if (null != claimKey) {
			docAcknowledgement = reimbursementService
					.findAcknowledgment(this.rodKey);
			if (null != docAcknowledgement) {
				isAckAvaliable = true;
				
			} else if(this.acknowledgementKey != null){
				
				isAckAvaliable = true;
				docAcknowledgement = reimbursementService.getDocAcknowledgementBasedOnKey(this.acknowledgementKey);
				
			}else {
				isAckAvaliable = true;
				docAcknowledgement = reimbursementService
						.findAcknowledgmentByClaimKey(claimKey);
			}
			if(docAcknowledgement != null){
			viewAcknowledgementPage.init(intimation,
					docAcknowledgement.getKey());
			}

		}
		if (isAckAvaliable && docAcknowledgement !=null) {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Acknowledgement Details");
			popup.setWidth("75%");
			popup.setHeight("90%");
			popup.setContent(viewAcknowledgementPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewAcknowledgementPage!=null){
						viewAcknowledgementPage.clearObjects();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("No Records Found");
		}
		}
	}

	public void getIrdaCategoryBillDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		Long rodKey = 0l;
		DocAcknowledgement docAcknowledgment = reimbursementService
				.findAcknowledgmentByClaimKey(claimKey);
		if (docAcknowledgment != null) {
			rodKey = reimbursementService
					.getReimbursementByAckKey(docAcknowledgment.getKey());
		}

		if (rodKey != null) {
			viewIrdaBillDetailsPage.init(rodKey);
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(viewIrdaBillDetailsPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void getEarlierAcknowledgmentDetails(String intimationNo) {

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
		ViewDocumentDetailsDTO documentDetails = new ViewDocumentDetailsDTO();
		// documentDetails.setClaimDto(bean.getClaimDTO());
		earlierRodDetailsViewObj.init(claimKey, this.rodKey);
		popup.setContent(earlierRodDetailsViewObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				if(earlierRodDetailsViewObj!=null){
					earlierRodDetailsViewObj.setClearReferenceData();
				}
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void getOMPEarlierAcknowledgmentDetails(String intimationNo) {

		OMPIntimation intimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		Long claimKey = null;
		if (intimation != null && intimation.getKey() != null) {
			OMPClaim claim = ompclaimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim && claim.getKey() != null) {
				claimKey = claim.getKey();
			}
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		ompearlierRodDetailsViewObj = ompearlierRodDetailsViemImplInstance.get();
		ViewDocumentDetailsDTO documentDetails = new ViewDocumentDetailsDTO();
		// documentDetails.setClaimDto(bean.getClaimDTO());
		ompearlierRodDetailsViewObj.init(claimKey, this.rodKey);
		popup.setContent(ompearlierRodDetailsViewObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	public void getFVRDetails(String intimationNo, Boolean isFVRDisabled) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewFVRDetailsViewImpl.init(intimation, isFVRDisabled);
		viewFVRDetailsViewImpl.setPreauthKey(this.preAuthKey);
		viewFVRDetailsViewImpl.setStageKey(this.stageKey);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("FVR Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewFVRDetailsViewImpl);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called FVR");
				viewFVRDetailsViewImpl.clearFVRDetailsPopup();
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	public void getClaimStatus(String intimationNo){
		if(intimationNo!=null){
		Long intimationKey = intimationService.getIntimationByNo(
				intimationNo).getKey();
		Claim claim = intimationService
				.getClaimforIntimation(intimationKey);
		String claimId = claim.getClaimId();
		Long claimKey = claim.getKey();
		OPHealthCheckup opHealthByClaimKey = processOPRequestService
				.getOpHealthByClaimKey(claimKey);
		if(opHealthByClaimKey != null){
		Long oPhealthCheckupKey = opHealthByClaimKey.getKey();
		
		List<OPHCDetails> oPHCDetailsList= processOPRequestService.getOpHCDetails(oPhealthCheckupKey);
		oPClaimStatus.init(intimationNo,oPHCDetailsList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Status of Claim No"+"  "+claimId);
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(oPClaimStatus);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Claim Status is not available. Since Claim is not Registered");
		}
		}else{
			getErrorMessage("Claim Status is not available at Register Claim stage");
		}
		
		
		
	}

	public void getBillAssesmentSheet(String intimationNo) {
		OPBillAssesmentSheetDTO oPBillAssesmentDto = null;
		if (null != this.policyKey && !isOutpatient) {
			if (documentDetailsPageInstance.getInsuredKey() != null) {

				Long insuredKey = documentDetailsPageInstance.getInsuredKey();
				Policy policyObject = policyService
						.getPolicyByKey(this.policyKey);
				Insured insured = insuredService.getInsuredByInsuredKey(insuredKey);
				String claimantName=insured.getInsuredName();
				String claimType = "HC";
				if (this.isNotHealthCheckUP) {
					claimType = "OP";
				}
				
				Map<String, Object> opBalanceSumInsured = DBCalculationService
						.getOPBalanceSumInsured(0l, insuredKey, claimType,"0");
				Double sumInsured = (Double) opBalanceSumInsured
						.get("sumInsured");
				if (policyObject.getProductType().getKey()
						.equals(ReferenceTable.FLOATER_POLICY)) {

					oPBillAssesmentDto = OPBillAssesmentSheetMapper.getInstance().getpolicyMapper(policyObject);
					oPBillAssesmentDto.setInsuredName(policyObject
							.getProposerFirstName());
					String formatDateForOP = SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodFrom());
					oPBillAssesmentDto.setCoveragePeriodFromStr(formatDateForOP);
					oPBillAssesmentDto.setCoveragePeriodToStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodTo()));
					oPBillAssesmentDto.setSumInsuredOP(sumInsured.toString());
					oPBillAssesmentDto.setClaimantName(claimantName);

				} else {

					oPBillAssesmentDto = OPBillAssesmentSheetMapper.getInstance()
							.getInsuredMapper(insured);
					oPBillAssesmentDto.setInsuredName(policyObject
							.getProposerFirstName());
					oPBillAssesmentDto.setCoveragePeriodFromStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodFrom()));
					oPBillAssesmentDto.setCoveragePeriodToStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodTo()));
					oPBillAssesmentDto.setSumInsuredOP(sumInsured.toString());
					oPBillAssesmentDto.setClaimantName(claimantName);
				}
				
			} else {
				getErrorMessage("Please select the insured");
				return;
			}
		} else {
			OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
			
			Long intimationKey = intimation.getKey();
			Long insuredKey = intimation.getInsured().getKey();
			Policy policyObject = intimation.getPolicy();
			String policyNumber=policyObject.getPolicyNumber();
			OPClaim claim = claimService.getOPClaimByIntimationNo(intimationNo);
			Integer opAvailableAmount = 0;
			Map<String, Integer> claimAmt = dbCalculationService
					.getOPAvailableAmount(intimation.getInsured().getKey(),claim.getKey(),
							claim.getClaimType().getKey(),claim.getOpcoverSection() != null ? claim.getOpcoverSection():"0");
			if(claimAmt != null && !claimAmt.isEmpty()){
				opAvailableAmount = claimAmt.get(SHAConstants.CURRENT_BALANCE_SI);
			}
			Long claimKey = claim.getKey();
			String claimType = intimation.getClaimType().getValue();
			if (claimType.equalsIgnoreCase("Out Patient")) {
				claimType = "OP";
			} else if (claimType.equalsIgnoreCase("Health Check-up")) {
				claimType = "HC";
			}
			Map<String, Object> opBalanceSumInsured = DBCalculationService
					.getOPBalanceSumInsured(claimKey, insuredKey, claimType,claim.getOpcoverSection());
			Double sumInsured = (Double) opBalanceSumInsured.get("sumInsured");
			OPHealthCheckup oPhealthCheckup = processOPRequestService
					.getOpHealthByClaimKey(claimKey);
			Long oPhealthCheckupKey = oPhealthCheckup.getKey();
			List<OPDocumentBillEntry> oPDocumentBillEntryList = outPatientService
					.getOpBillEntryDetails(oPhealthCheckupKey);
			OPDocumentBillEntry oPDocumentBillEntry = oPDocumentBillEntryList
					.get(0);
			Long totalBillClaimedAmount = 0l;
			Double totalBillDeductions = 0.0;
			Double totalBillApprovedAmount = 0.0;
			Double netPayAmount = 0.0;
			String billDate="";

			for (OPDocumentBillEntry list : oPDocumentBillEntryList) {
				totalBillClaimedAmount += list.getClaimedAmount();
				totalBillDeductions += list.getNonPayableAmount();


				billDate+=SHAUtils.formatDate(list.getBillDate()).toString()+" ,";
				

			}
			billDate=billDate.substring(0, billDate.length()-1);
			totalBillApprovedAmount = totalBillClaimedAmount
					- totalBillDeductions;
			netPayAmount = Math.min(opAvailableAmount, totalBillApprovedAmount);
			
			
			Double balance=sumInsured-netPayAmount;
			Long claimStatus=0l;
			Double provisionAmount=0.0;
			List<Claim> previousClaims = claimService.getClaimsByPolicyNumber(policyNumber);
			for (Claim previousClaimsDTO : previousClaims) {
				if (!previousClaimsDTO.getClaimId().equals(claim.getClaimId())
						&& ((previousClaimsDTO.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)) || (previousClaimsDTO
								.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP))) ){
					claimStatus = previousClaimsDTO.getStatus().getKey();
					if (claimStatus.equals(ReferenceTable.OP_REGISTER_CLAIM_STATUS)) {
						provisionAmount += previousClaimsDTO
								.getProvisionAmount();
					} else if (claimStatus.equals(ReferenceTable.OP_APPROVE)) {
						provisionAmount += previousClaimsDTO
								.getCurrentProvisionAmount();

					}
				}
			}
			Double balanceSIAvailable=balance-provisionAmount;
			if(balanceSIAvailable<0){
				balanceSIAvailable=0d;
			}
			oPBillAssesmentDto = OPBillAssesmentSheetMapper
					.getOPDocumentBillEntryMapper(oPDocumentBillEntry);
			oPBillAssesmentDto.setCoveragePeriodFromStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodFrom()));
			oPBillAssesmentDto.setCoveragePeriodToStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getCoveragePeriodTo()));
			oPBillAssesmentDto.setAmountClaimed(claim.getClaimedAmount()
					.toString());
			oPBillAssesmentDto.setDeductions(totalBillDeductions.toString());
			oPBillAssesmentDto.setApprovedAmount(netPayAmount.toString());
			oPBillAssesmentDto.setNetPayAmount(netPayAmount.toString());
			oPBillAssesmentDto.setClaimantName(intimation
					.getInsuredPatientName());


			oPBillAssesmentDto.setMainBillDate(billDate);
			oPBillAssesmentDto.setBillReceivedDateStr(SHAUtils.formatDateForOP(oPBillAssesmentDto.getBillReceivedDate()));

			oPBillAssesmentDto.setTotalBillAmount(totalBillClaimedAmount);
			oPBillAssesmentDto.setPreparedBy(oPhealthCheckup.getCreatedBy());
			oPBillAssesmentDto.setSumInsuredOP(sumInsured.toString());
			oPBillAssesmentDto
					.setBalanceSIAvailable(balanceSIAvailable);
			
			

		}
		billAssesmentSheet.init(oPBillAssesmentDto);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Bill Assesment Sheet");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(billAssesmentSheet);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	public void getOPBalanceSumInsured(String intimationNo) {
		if (intimationNo != null) {
			/*Long intimationKey = intimationService.getIntimationByNo(
					intimationNo).getKey();
			Long claimKey = intimationService.getClaimforIntimation(
					intimationKey).getKey();
			Intimation intimation = intimationService
					.getIntimationByNo(intimationNo);*/
			OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
			OPClaim claimByKey = claimService.getOPClaimByIntimationNo(intimationNo);
			Long insuredKey = intimation.getInsured().getKey();
			String claimType = intimation.getClaimType().getValue();
			if (claimType.equalsIgnoreCase("Out Patient")) {
				claimType = "OP";
			} else if (claimType.equalsIgnoreCase("Health Check-up")) {
				claimType = "HC";
			}
			opBalanceSumInsured.init(claimByKey.getKey(), insuredKey, claimType);
		} else {
				Policy policyDtls = policyService.getByPolicyNumber(policyNumber);
				Insured insuredDtls = insuredService.getInsuredByPolicyNo(policyDtls.getKey().toString());
				Long insuredKey = insuredDtls.getKey();
				Long claimKey = 0l;
				String claimType = "OP";
				opBalanceSumInsured.init(claimKey, insuredKey, claimType);
			
		}
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Out Patient Balance Sum Insured");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(opBalanceSumInsured);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	public void getOPBillSummary(String intimationNo) {
		if (intimationNo != null) {
			BeanItemContainer<SelectValue> selectValueContainer = masterService
					.getSelectValueContainer(ReferenceTable.OP_BILL_DETAILS);
			OutpatientMapper mapper = new OutpatientMapper();
			Long intimationKey = intimationService.getIntimationByNo(
					intimationNo).getKey();
			Claim claim = intimationService
					.getClaimforIntimation(intimationKey);
			OPHealthCheckup opHealthByClaimKey = outPatientService
					.getOpHealthByClaimKey(claim.getKey());
			List<OPBillDetailsDTO> billDetailsDTOList = new ArrayList<OPBillDetailsDTO>();
			List<OPDocumentBillEntry> opBillEntryDetails = outPatientService
					.getOpBillEntryDetails(opHealthByClaimKey.getKey());
			for (OPDocumentBillEntry opDocumentBillEntry : opBillEntryDetails) {
				billDetailsDTOList.add(mapper
						.getOPBillEntryDTO(opDocumentBillEntry));
			}

			for (OPBillDetailsDTO billEntryDTO : billDetailsDTOList) {
				Integer amt = SHAUtils.getIntegerFromString(billEntryDTO
						.getClaimedAmount())
						- SHAUtils.getIntegerFromString(billEntryDTO
								.getNonPayableAmt());
				billEntryDTO.setPayableAmt(String.valueOf(amt));
				billEntryDTO.setBillDateStr(SHAUtils.formatDate(billEntryDTO.getBillDate()));
			}

			List<SelectValue> masterList = selectValueContainer.getItemIds();
			for (SelectValue selectValue : masterList) {
				for (OPBillDetailsDTO billDetailsDTO : billDetailsDTOList) {
					if (selectValue.getId()
							.equals(billDetailsDTO.getMasterId())) {
						billDetailsDTO.setDetails(selectValue.getValue());
					}
				}
			}
			viewBillDetails.init(billDetailsDTOList);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Bill Details");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewBillDetails);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("Bill summary is not available at Register Claim stage");
		}

	}
	
	public void getPABillSummary(Intimation intimation)
	{
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		Long rodKey = null;

		if (this.rodKey != null && ! this.rodKey.equals(0l)) {
			rodKey = this.rodKey;

		} else {
			DocAcknowledgement docAcknowledgment = reimbursementService
					.findAcknowledgmentByClaimKey(claimKey);
			if (docAcknowledgment != null) {
				rodKey = reimbursementService
						.getReimbursementByAckKey(docAcknowledgment.getKey());
			}
		}
		
		if(rodKey != null)
		{
			DocAcknowledgement docAcknowledgment =  reimbursementService.findAcknowledgment(rodKey);
			
			if(docAcknowledgment.getHospitalisationFlag() != null){
			if(docAcknowledgment.getHospitalisationFlag().equalsIgnoreCase(SHAConstants.HOSPITALIZATION_FLAG))
			{
				getBillSummary(intimation);
			}else{
				getPABillSummaryNonHosp(rodKey);
			}
			}else{
				getPABillSummaryNonHosp(rodKey);
			}
		}else{
			getErrorMessage("Bill Summary Details are not available");
		}
	}
	
	public void getPABillSummaryNonHosp(Long rodKey)
	{
		if(rodKey != null){
			viewPABillSummaryPage.init(rodKey);
			Panel mainPanel = new Panel(viewPABillSummaryPage);
	        mainPanel.setWidth("2000px");
			Window popup = new com.vaadin.ui.Window();
			// popup.setCaption("Pre-auth Details");
			popup.setWidth("75%");
			//popup.setHeight("90%");
			popup.setSizeFull();
			popup.setContent(mainPanel);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);


				popup.addCloseListener(new Window.CloseListener() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if(viewPABillSummaryPage!=null){
							viewPABillSummaryPage.clearObjects();
						}
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			} else {
				getErrorMessage("Bill Summary Details are not available");
			}
	}
	
	public Window getPASumInsured(String intimationNo)
	{
		viewBalanceSumInsuredPA.init(intimationNo);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Balance Sum Insured");
		popup.setWidth("90%");
		popup.setHeight("90%");
		popup.setContent(viewBalanceSumInsuredPA);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				if(viewBalanceSumInsuredPA!=null){
					viewBalanceSumInsuredPA.setClearReferenceData();
				}
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		//UI.getCurrent().addWindow(popup);
		return popup;
	}

//	public void getSublimits(String intimationNo) {
//		Intimation intimations = intimationService.searchbyIntimationNo(intimationNo);
//		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
//		
//		Policy policy = policyService.getPolicyByKey(intimations.getPolicy()
//				.getKey());
//		Long productKey = policy.getProduct().getKey();
//		Double sumInsured = intimations.getInsured().getInsuredSumInsured();
//		Double insuredAge = intimations.getInsured().getInsuredAge();
//		Long insuredNumber = intimations.getInsured().getInsuredId();
//		
//		Preauth latestPreauthByIntimationKey = preAuthService.getLatestPreauthByIntimationKey(intimations.getKey());
//		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
//		if(latestPreauthByIntimationKey != null){
//			/*subLimits.init(DBCalculationService.getClaimedAmountDetailsForSection(productKey,
//					sumInsured, insuredAge,latestPreauthByIntimationKey.getSectionCategory() != null ? latestPreauthByIntimationKey.getSectionCategory() : 0l ,policy.getPolicyPlan() != null ? policy.getPolicyPlan() : "0"));*/
//			Long section = latestPreauthByIntimationKey.getSectionCategory() != null ? latestPreauthByIntimationKey.getSectionCategory() : 0l;
//			String plan = policy.getPolicyPlan() != null ? policy.getPolicyPlan() : "0";
//			
//			 sublimitList = DBCalculationService.getSublimitUtilization(insuredNumber, productKey, insuredAge,section, plan, claim.getKey());
//			subLimits.init(sublimitList);
//			//subLimits.init(DBCalculationService.getSublimitUtilization(insuredNumber, productKey, insuredAge,section, plan, claim.getKey()));
//		}else{
//			/*subLimits.init(DBCalculationService.getClaimedAmountDetailsForSection(productKey,
//					sumInsured, insuredAge, 0l ,"0"));*/	
//			 sublimitList = DBCalculationService.getSublimitUtilization(insuredNumber,productKey,insuredAge, 0l ,"0",claim.getKey());
//			 subLimits.init(sublimitList);
//			//subLimits.init(DBCalculationService.getSublimitUtilization(insuredNumber,
//			//		productKey,insuredAge, 0l ,"0",claim.getKey()));
//		}
//		
//		// subLimits.init(DBCalculationService.getClaimedAmountDetails(productKey,
//		// 200000.0, insuredAge));
//		if(null != sublimitList && !sublimitList.isEmpty())
//		{
//		VerticalLayout vLayout = new VerticalLayout();
//		vLayout.addComponents(subLimits);
//		
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("Sub Limits Details");
//		popup.setWidth("75%");
//		popup.setHeight("75%");
//		popup.setContent(vLayout);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(true);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//		}
//		else
//		{
//			getErrorMessage("Sublimit is not available");
//		}
//	}

	public void getComprehensiveSublimits(String intimationNo) {
				
		Intimation intimations = intimationService.searchbyIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		
		Policy policy = policyService.getPolicyByKey(intimations.getPolicy()
				.getKey());
		Long productKey = policy.getProduct().getKey();
		Double insuredAge = intimations.getInsured().getInsuredAge() != null ? intimations.getInsured().getInsuredAge() : 0d;
		Long insuredNumber = intimations.getInsured().getInsuredId();
		
		Preauth latestPreauthByIntimationKey = preAuthService.getLatestPreauthByIntimationKey(intimations.getKey());

	if(claim != null && claim.getKey() != null){
		if(latestPreauthByIntimationKey == null && policy.getProduct() != null 
				&& (ReferenceTable.getSuperSurplusKeys().containsKey(policy.getProduct().getKey()))){
			String plan1 = policy.getPolicyPlan() != null ? policy.getPolicyPlan() : "0";
			compSublimits.init(DBCalculationService.getSublimitUtilizationBasedOnProduct(insuredNumber, productKey, insuredAge, 0l, plan1, claim.getKey()));	
		}
		else if(latestPreauthByIntimationKey != null){
			
			Long section = latestPreauthByIntimationKey.getSectionCategory() != null ? latestPreauthByIntimationKey.getSectionCategory() : 0l;
			String plan = policy.getPolicyPlan() != null ? policy.getPolicyPlan() : "0";
			
			/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(policy.getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(policy.getProduct().getKey())) {*/
			if(policy.getProduct() != null 
					&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(policy.getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(policy.getProduct().getCode()))
					|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(policy.getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(policy.getProduct().getCode()))
					&&	policy.getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)) || SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(policy.getProduct().getCode()) 
					|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(policy.getProduct().getCode()))) {
				plan = intimations.getInsured().getPolicyPlan() != null ? intimations.getInsured().getPolicyPlan() : "";
			}
			
			compSublimits.init(DBCalculationService.getSublimitUtilizationBasedOnProduct(insuredNumber, productKey, insuredAge, section, plan, claim.getKey()));
		}else{
			compSublimits.init(DBCalculationService.getSublimitUtilizationBasedOnProduct(insuredNumber,	productKey, insuredAge, 0l , "0", claim.getKey()));
		}
			
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Sub Limits Details");
		popup.setWidth("85%");
		popup.setHeight("80%");
		popup.setContent(compSublimits);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Sublimit is not available");
		}
	}

	public void getInvestigationDetails(String intimationNo, Boolean isDiabled) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		
		isDiabled = false;
		
		viewInvestigationDetails.init(isDiabled);
		viewInvestigationDetails.setViewDMSDocViewPage(dmsDocumentDetailsViewPage);
		viewInvestigationDetails.showValues(claimKey);
		UI.getCurrent().addWindow(viewInvestigationDetails);
	}

	//public ViewInvestigationDetails getRevisedInvestigationDetails(String intimationNo, Boolean isDiabled,Long stageKey,MedicalApprovalZonalReviewWizardViewImpl parent) {	
	public ViewInvestigationDetails getRevisedInvestigationDetails(String intimationNo, Boolean isDiabled,Long stageKey,MedicalApprovalPremedicalProcessingUI parent) {

	Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		if(null != stageKey && stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE))
		{
			isDiabled = true;
		}
		else
		{
			isDiabled = false;
		}
		
		viewInvestigationDetails.init(isDiabled);
		this.parent = parent;
		viewInvestigationDetails.setViewDMSDocViewPage(dmsDocumentDetailsViewPage);
		viewInvestigationDetails.showRevisedValues(claimKey,stageKey,parent);
		InitiateInvestigationDTO initateInvDto = viewInvestigationDetails.getInitateInvDto();
		return viewInvestigationDetails;
	}
	public void getPedRequestDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewPedRequestDetails.init(intimation.getKey());
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("PED Request Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewPedRequestDetails);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				viewPedRequestDetails.clearPEDReqDetailsPopup();
				System.out.println("Close listener called PED Request Details");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getPortablityDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewPortablityPolicyUI.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Portablity Policy Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewPortablityPolicyUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getDoctorRemarks(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewDoctorRemarksUI.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Doctor Note (Trails)");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewDoctorRemarksUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				viewDoctorRemarksUI.clearDoctorNotePopup();
				System.out.println("Close listener called Doctor Note");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	// private void getPreAuthDetail(String intimationNo){
	public void getPreAuthDetail(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewPreAuthDetailsMainClass.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		// popup.setCaption("Pre-auth Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(viewPreAuthDetailsMainClass);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void getPreAuthUpdateDetail(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		// viewPreauthDetailsPage.init(intimationNo);
		List<Preauth> preauthList = preAuthService
				.getPreauthByIntimationKey(intimation.getKey());
		if (preauthList != null && !preauthList.isEmpty()) {
			revisedPreauthViewPage.init(intimationNo);
			Window popup = new com.vaadin.ui.Window();
			// popup.setCaption("Pre-auth Details");
			popup.setWidth("95%");
			popup.setHeight("90%");
			// popup.setContent(viewPreauthDetailsPage);
			popup.setContent(revisedPreauthViewPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					revisedPreauthViewPage.setClearReferenceData();
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("Preauth is not available");
		}
	}

	/**
	 * @author yosuva.a
	 * @param intimationNo
	 */

	public void getBillSummaryDetails(String intimationNo) {

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		if(intimation != null)
		{
			if(intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE))
			{
				getPABillSummary(intimation);
			}else{
				getBillSummary(intimation);
			}
		}
	}
	
	public void getBillSummary(Intimation intimation)
	{
		Long claimKey = null;
		if (intimation != null) {
			Claim claim = claimService.getClaimforIntimation(intimation
					.getKey());
			if (null != claim) {
				claimKey = claim.getKey();
			}
		}
		Long rodKey = null;

		if (this.rodKey != null && ! this.rodKey.equals(0l)) {
			rodKey = this.rodKey;

		} else {
			DocAcknowledgement docAcknowledgment = reimbursementService
					.findAcknowledgmentByClaimKey(claimKey);
			if (docAcknowledgment != null) {
				rodKey = reimbursementService
						.getReimbursementByAckKey(docAcknowledgment.getKey());
			}
			if (rodKey != null) {
				//added by noufel fro GMC prop CR
				if(!(intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
				DBCalculationService.getBillDetailsSummary(rodKey);
				}
			}
		}
		
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
		PreauthDTO preauthDto = new PreauthDTO();
		preauthDto.setNewIntimationDTO(intimationDto);
		
		preauthDto = getProrataFlagFromProduct(preauthDto);
				
		if(rodKey != null){
		viewBillSummaryPage.init(preauthDto,rodKey,true);
		Panel mainPanel = new Panel(viewBillSummaryPage);
        mainPanel.setWidth("2000px");
		Window popup = new com.vaadin.ui.Window();
		// popup.setCaption("Pre-auth Details");
		popup.setWidth("75%");
		//popup.setHeight("90%");
		popup.setSizeFull();
		popup.setContent(mainPanel);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);


			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewBillSummaryPage!=null){
						viewBillSummaryPage.clearObjects();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("Bill Summary Details are not available");
		}

	
	}

	public void getQueryDetailsTable(String intimationNo) {

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Claim claim = null;
		Long rodKey = 0l;
		if (intimation != null) {
			claim = claimService.getClaimforIntimation(intimation.getKey());
			if(null != claim){
				DocAcknowledgement docAcknowledgment = reimbursementService
						.findAcknowledgmentByClaimKey(claim.getKey());
				if (docAcknowledgment != null) {
					rodKey = reimbursementService
							.getReimbursementByAckKey(docAcknowledgment.getKey());
					this.rodKey = rodKey;
				}
			}
		}

		if(rodKey != null && rodKey != 0 && claim != null){
			queryDetailsTableObj.init("View Query Details", false, false);
			queryDetailsTableObj.setViewQueryDetailsColumn();
			
			if(intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		    {
				queryDetailsTableObj.setViewPAQueryDetialsColumn();
		    }
			
			setQueryValues(this.rodKey,claim);
	
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(queryDetailsTableObj);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;
	
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});
	
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
		 }else if(this.intimationNo != null){
			
			preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
			
			setQueryDetailsForPreauth(this.intimationNo);
			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("90%");
			popup.setContent(preAuthPreviousQueryDetailsTable);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
		
//			popup.addCloseListener(new Window.CloseListener() {
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void windowClose(CloseEvent e) {
//					System.out.println("Close listener called");
//				}
//			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		 }
		 else{
				getErrorMessage("Reimbursement Query is not Available");
			}

	}


	public void getIrdaNonPayables(String intimationNo) {

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");
		pdfPageUI.init(null, null,popup);
		popup.setContent(pdfPageUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	public void getIrda64VbDetails(String intimationNo) {
		
		
		
		 if(intimationNo !=null){
			 Embedded pdfContent = null;
		popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");
		OMPIntimation  ompIntimation = ompIntimationService.searchbyIntimationNo(intimationNo);
		if(isOutpatient){
			pdfContent = getOPIrda64VbDetails(intimationNo);
		}else{
			if(ompIntimation!= null){

				pdfContent = getompIrda64VbDetails(intimationNo);

			}else{
				VB64ComplianceDto vb64dto = new VB64ComplianceDto();

				Double reportId = Math.random() * 10000;

				vb64dto.setReportId("PGIR"+reportId.intValue());

				Claim clm = claimService.getClaimsByIntimationNumber(intimationNo);

				Intimation intimation = intimationService
						.searchbyIntimationNo(intimationNo);
				ClaimDto clmDto = null;

				if(clm != null){

					clmDto = ClaimMapper.getInstance().getClaimDto(clm);
				}else{
					clmDto = new ClaimDto();
				}
				NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);

				clmDto.setNewIntimationDto(intimationDto);

				vb64dto.setClaimDto(clmDto);

				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;

				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					String get64vbStatus = PremiaService.getInstance().get64VBStatusForView(clmDto.getNewIntimationDto().getPolicy().getPolicyNumber(), clmDto.getNewIntimationDto().getIntimationId());

					if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("p") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_PENDING) || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_DUE))){
						vb64dto.setPaymentStatus(SHAConstants.PENDING);
					}
					else if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("r") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_COLLECTED))){
						vb64dto.setPaymentStatus(SHAConstants.REALISED);
					}
					else if(get64vbStatus != null &&  get64vbStatus.equalsIgnoreCase("d")){
						vb64dto.setPaymentStatus(SHAConstants.DISHONOURED);
					}

				}
				if(clm!= null && clm.getIntimation()!= null && clm.getIntimation().getPolicy()!= null && clm.getIntimation().getPolicy().getPolicyNumber()!= null){

					List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(clm.getIntimation().getPolicy().getPolicyNumber());

					vb64dto.setEndorsement(endorsementList);
				}

				DocumentGenerator docgen = new DocumentGenerator();

				ReportDto rptDto = new ReportDto();

				List<VB64ComplianceDto> vb64ListDto = new ArrayList<VB64ComplianceDto>();
				vb64ListDto.add(vb64dto);		
				rptDto.setBeanList(vb64ListDto);
				rptDto.setClaimId(clmDto.getClaimId());

				String fileUrl = docgen.generatePdfDocument("Compliance64VB", rptDto);
				pdfContent = getPDFContentWindow(fileUrl);

			}
		}
		
			popup.setContent(pdfContent);
			popup.setCaption("64 VB Compliance Report");
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
	
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		
//		  }else{
//			  getErrorMessage(" 64vb Detials are not Available");
//		  }
		
		}else {
			getErrorMessage(" 64vb Detials are not Available");
		}
	
	}
	
	public Embedded getompIrda64VbDetails(String intimationNo){
		
		 if(intimationNo !=null){
				
				VB64ComplianceDto vb64dto = new VB64ComplianceDto();
				
				Double reportId = Math.random() * 10000;
				
				vb64dto.setReportId("PGIR"+reportId.intValue());
				
				OMPClaim clm = ompclaimService.getClaimsByIntimationNumber(intimationNo);
				
				OMPIntimation intimation = ompIntimationService
						.searchbyIntimationNo(intimationNo);
				ClaimDto clmDto = null;
				
				 if(clm != null){
				
				    clmDto = OMPClaimMapper.getInstance().getClaimDto(clm);
				  }else{
				    	 clmDto = new ClaimDto();
				  }
					NewIntimationDto intimationDto = ompIntimationService.getIntimationDto(intimation);
					
					clmDto.setNewIntimationDto(intimationDto);
					
					vb64dto.setClaimDto(clmDto);
					
					String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
					
					if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
						String get64vbStatus = PremiaService.getInstance().get64VBStatusForView(clmDto.getNewIntimationDto().getPolicy().getPolicyNumber(), clmDto.getNewIntimationDto().getIntimationId());
					
						if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("p") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_PENDING) || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_DUE))){
							vb64dto.setPaymentStatus(SHAConstants.PENDING);
						}
						else if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("r") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_COLLECTED))){
							vb64dto.setPaymentStatus(SHAConstants.REALISED);
						}
						else if(get64vbStatus != null &&  get64vbStatus.equalsIgnoreCase("d")){
							vb64dto.setPaymentStatus(SHAConstants.DISHONOURED);
						}
						
					}
					if(clm!= null && clm.getIntimation()!= null && clm.getIntimation().getPolicy()!= null && clm.getIntimation().getPolicy().getPolicyNumber()!= null){
						
						List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(clm.getIntimation().getPolicy().getPolicyNumber());
						
						vb64dto.setEndorsement(endorsementList);
					}
					
					DocumentGenerator docgen = new DocumentGenerator();
					
					ReportDto rptDto = new ReportDto();
					
					List<VB64ComplianceDto> vb64ListDto = new ArrayList<VB64ComplianceDto>();
					vb64ListDto.add(vb64dto);		
					rptDto.setBeanList(vb64ListDto);
					rptDto.setClaimId(clmDto.getClaimId());
					
					String fileUrl = docgen.generatePdfDocument("Compliance64VB", rptDto);
					
					Embedded pdfContent = getPDFContentWindow(fileUrl);
					return pdfContent;
		 }
		return null;
					
	
	}
	
	
	private Embedded getPDFContentWindow(final String fileUrl){

		Path p = Paths.get(fileUrl);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(fileUrl);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded embd = new Embedded();
		embd.setSizeFull();
		embd.setType(Embedded.TYPE_BROWSER);
		embd.setMimeType("application/pdf");
		embd.setSource(r);
		
		return embd;
	
	}

	public void getViewCoordinatorReply(String intimationNo) {

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("85%");
		popup.setHeight("90%");
		popup.setCaption("View Coordinator Reply");
		viewCoordinatorReplyTableObj.init("", false, false);
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewCoordinatorReplyTableObj.setTableList(viewCoOrdinatorReplyService
				.search(intimation.getKey()));
		popup.setContent(viewCoordinatorReplyTableObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}
	
	public void getViewRiskDetails(String intimationNo) {

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("800px");
		popup.setHeight("280px");
		popup.setCaption("Risk Details");
		VerticalLayout vLayout = new VerticalLayout();
		
		
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		if(intimation != null){
		riskDetailsUI.init(intimation);
		vLayout.addComponent(riskDetailsUI);
		}else{
		

		OMPIntimation ompintimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		if(ompintimation != null){
//		ompRiskDetailsUI.init(ompintimation);
//		vLayout.addComponent(ompRiskDetailsUI);
		}
		}
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	public void getViewBillDetails(String intimationNo) {
		
		if(rodKey != null && ! rodKey.equals(0l)){

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");

		viewBillDetailsTableObj.init();
		viewBillDetailsTableObj.setReferenceData(referenceData);
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		setBillDetailsTable(rodKey, intimation);
		popup.setContent(viewBillDetailsTableObj);
		// popup.setContent();
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
    		 * 
    		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				if(viewBillDetailsTableObj!=null){
					viewBillDetailsTableObj.clearObjects();
				}
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Bill Details is not applicable");
		}

	}
	
	
	public void setQueryValues(Long key, Claim claim) {

		List<ViewQueryDTO> QuerytableValues = reimbursementService
				.getQueryDetails(key);
		Hospitals hospitalDetails = null;
		String diagnosisName = reimbursementService.getDiagnosisName(key);

		// need to implement
		if (claim != null) {
			Long hospitalKey = claim.getIntimation().getHospital();
			hospitalDetails = hospitalService.getHospitalById(hospitalKey);
		}
		int sno=1;
		for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
			viewQueryDTO.setDiagnosis(diagnosisName);
			viewQueryDTO.setSno(sno);
			if (hospitalDetails != null) {
				viewQueryDTO.setHospitalName(hospitalDetails.getName());
				viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
			}
			viewQueryDTO.setClaim(claim);
			sno++;
			queryDetailsTableObj.addBeanToList(viewQueryDTO);
		}
	}
    
    public void setQueryDetailsForPreauth(String intimationNo){
    	
    	Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
//		intimation.setKey(this.bean.getIntimationKey());
		List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.search(intimation);
		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
    	
    }
	
    public void getPAMedicalSummaryDetails()
    {
		if(this.rodKey != null && this.rodKey != 0l){
			
		viewPAMedicalSummaryPage.init(this.rodKey);
		
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(viewPAMedicalSummaryPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewPAMedicalSummaryPage!=null){
						viewPAMedicalSummaryPage.setClearReferenceData();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("Medical Summary Details are not available");
		}
	
    }
    
	public void getMedicalSummaryDetails(String intimationNo){
		
		
		Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		
		if(intimation.getProcessClaimType() !=null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			getPAMedicalSummaryDetails();
		}
		
		
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		
////		Long claimKey = intimation
//		Claim claim = claimService.getClaimforIntimation(intimation.getKey());
//		
//		Long rodKey=null;
//		if (claim != null) {
//			DocAcknowledgement docAcknowledgment = reimbursementService
//					.findAcknowledgmentByClaimKey(claim.getKey());
//			if (docAcknowledgment != null) {
//				rodKey = reimbursementService
//						.getReimbursementByAckKey(docAcknowledgment.getKey());
//			}
//			if (null != rodKey) {
//				viewMedicalSummaryPage.init(rodKey);
//			}//
//			else{
//				viewMedicalSummaryPage.init(null);
//			}
//		}
		else
		{
		if(this.rodKey != null && this.rodKey != 0l){
		viewMedicalSummaryPage.init(this.rodKey);
		Window popup = new com.vaadin.ui.Window();
		// popup.setCaption("Pre-auth Details");
		popup.setWidth("95%");
		popup.setHeight("90%");
		popup.setContent(viewMedicalSummaryPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewMedicalSummaryPage!=null){
						viewMedicalSummaryPage.setClearReferenceData();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		} else {
			getErrorMessage("Medical Summary Details are not available");
		}
		}
	}

	public void getSpecialityOpinion(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewSpecialistOpinionDetails.init(intimation.getKey());
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Specialist Opinion");
		popup.setWidth("70%");
		popup.setHeight("40%");
		popup.setResizable(true);
		popup.setContent(viewSpecialistOpinionDetails);
		popup.setClosable(true);
		popup.center();
//		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
	
		UI.getCurrent().addWindow(popup);
	}

	public void getTranslationMiscRequest(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewTranslationMiscRequestDetails.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Translation / Misc Request");
		popup.setWidth("88%");
		popup.setHeight("40%");
		popup.setContent(viewTranslationMiscRequestDetails);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void getViewPreviousInsuranceDetails() {
		Intimation intimations = intimationService
				.searchbyIntimationNo(intimationNo);

		Policy policy = policyService.getPolicy(intimations.getPolicy()
				.getPolicyNumber());
		ViewPreviousInsurance viewPreviousInsuranceInstance = viewPreviousInsurance
				.get();
		viewPreviousInsuranceInstance.showValue(policy,intimations);
		UI.getCurrent().addWindow(viewPreviousInsuranceInstance);
	}
	
	public void getViewPreviousInsuranceDetailsForOp() {
		Intimation intimations = intimationService
				.searchbyIntimationNo(intimationNo);

		Policy policy = policyService.getPolicyByKey(this.policyKey);
		ViewPreviousInsurance viewPreviousInsuranceInstance = viewPreviousInsurance.get();
		viewPreviousInsuranceInstance.showValue(policy,intimations);
		UI.getCurrent().addWindow(viewPreviousInsuranceInstance);
	}

	/**
	 * Added for pre auth screen change
	 * 
	 * */

	public void getViewInvestigationDetails(Long claimKey, boolean isDisabled) {
		viewInvestigationDetails.init(isDisabled);
		viewInvestigationDetails.setViewDMSDocViewPage(dmsDocumentDetailsViewPage);
		viewInvestigationDetails.showValues(claimKey);
		UI.getCurrent().addWindow(viewInvestigationDetails);
	}
	
	public void getViewInvestigationDetailsForPreauth(Long claimKey, boolean isDisabled, String userName, String password,PreauthDTO bean){
		
		viewInvestigationDetails.init(true,bean);
		//viewInvestigationDetails.init(false);
		viewInvestigationDetails.setViewDMSDocViewPage(dmsDocumentDetailsViewPage);
		viewInvestigationDetails.showValuesForPreauth(claimKey,preAuthKey, userName, password);
		UI.getCurrent().addWindow(viewInvestigationDetails);
	}

	/**
	 * Added for ticket 756. This method is used only by process enhancement
	 * screen.
	 * */

	public void getViewInvestigationDetailsForEnhancement(Long claimKey,
			boolean isDisabled) {
		// ViewInvestigationDetails a_viewInvestigationDetails =
		// viewInvestigationDetails
		// .get();
	//	viewInvestigationDetails.init(true);
		viewInvestigationDetails.init(false);
		viewInvestigationDetails.setViewDMSDocViewPage(dmsDocumentDetailsViewPage);
		viewInvestigationDetails.showValues(claimKey, isDisabled);
		UI.getCurrent().addWindow(viewInvestigationDetails);
	}

	public void getViewCoPay(String intimationNo) {
		Intimation intimations = null;
		OPIntimation opintimations = null;
		
		if(isOutpatient && intimationNo != null){
			opintimations = intimationService.searchbyOPIntimationNo(intimationNo);
			
			MasCopay copay; 
			 if(opintimations.getPolicy().getProduct().getKey() != null && (opintimations.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)||
					 opintimations.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))) {
				 copay = coPayService.getByProductAndSi(opintimations.getPolicy().getProduct().getKey(),policy.getTotalSumInsured()!= null ? policy.getTotalSumInsured() : 0);
				} else {
					copay = coPayService.getByProduct(opintimations.getPolicy()
						.getProduct().getKey());
				}
			
			ViewCopayDetails ciewCopayDetailsInstancet = viewCopayDetails.get();
			ciewCopayDetailsInstancet.showValues(copay, opintimations.getPolicy(), opintimations.getInsured().getInsuredDateOfBirth());
			UI.getCurrent().addWindow(ciewCopayDetailsInstancet);
		}else if(intimationNo != null){
				intimations = intimationService.searchbyIntimationNo(intimationNo);
				MasCopay copay; 
				if(intimations.getPolicy().getProduct().getKey() != null && (intimations.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)||
						 intimations.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))) {
					 copay = coPayService.getByProductAndSi(intimations.getPolicy().getProduct().getKey(),intimations.getPolicy().getTotalSumInsured()!= null ? intimations.getPolicy().getTotalSumInsured() : 0);
					} else {
						copay = coPayService.getByProduct(intimations.getPolicy()
							.getProduct().getKey());
				}
				
				ViewCopayDetails ciewCopayDetailsInstancet = viewCopayDetails.get();
				ciewCopayDetailsInstancet.showValues(copay, intimations.getPolicy(),
						intimations.getInsured().getInsuredDateOfBirth());
				
				UI.getCurrent().addWindow(ciewCopayDetailsInstancet);
		}

	}
	
	public void getViewCoPayForOP(){
		
		if (insuredSelected != null) {

//			Long insuredKey = documentDetailsPageInstance.getInsuredKey();
			Policy policy = policyService.getPolicyByKey(this.policyKey);
			
			MasCopay copay = coPayService.getByProduct(policy.getProduct().getKey());
			
			ViewCopayDetails ciewCopayDetailsInstancet = viewCopayDetails.get();
			ciewCopayDetailsInstancet.showValues(copay, policy,	insuredService.getInsuredByInsuredKey(insuredSelected.getKey()).getInsuredDateOfBirth());
			
			UI.getCurrent().addWindow(ciewCopayDetailsInstancet);
		}else {
			getErrorMessage("Please select the Insured Patient Name");
		}
		
	}
	
	public void getViewTopPanelDetails(String intimationNo){
		
		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		//NewIntimationDto intimationDto = (new NewIntimationMapper()).getInstance().getNewIntimationDto(intimation);
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
		
		Hospitals hospObj = hospitalService.getHospitalById(intimation.getHospital());
		if(hospObj != null){
			HospitalDto hospDto = new HospitalDto(hospObj);
		intimationDto.setHospitalDto(hospDto);
		}		
		
		Claim claimObj = claimService.getClaimforIntimation(intimation.getKey());
		PreMedicalMapper premedicalMapper = new PreMedicalMapper();
		List<PedValidation> findPedValidationList = new ArrayList<PedValidation>();
		
		findPedValidationList = pedValidationService.getDiagnosis(intimation.getKey());
		
		String diagnosisName = "";
		Preauth preauthObj = null;
		ClaimDto claimDto = null;
		if(claimObj != null){
				ClaimMapper clmMapper = new ClaimMapper();
				claimDto = clmMapper.getInstance().getClaimDto(claimObj);
				if(null != claimDto){
					claimDto.setNewIntimationDto(intimationDto);
				}
			
				preauthObj = preAuthService.getLatestPreauthByClaim(claimObj.getKey());
		}
				Long transacKey = null;
				
			if(preauthObj != null){
				Double totalappAmt = preauthObj.getTotalApprovalAmount();
				claimDto.setCashlessAppAmt(totalappAmt);
				transacKey = preauthObj.getKey();	
			}
			
			Reimbursement reimbObj = reimbursementService.getLatestReimbursementDetails(null != claimDto ?claimDto.getKey():0);
			if(reimbObj != null){
				transacKey = reimbObj.getKey(); 
			}
		
			if(transacKey != null){
				diagnosisName = preAuthService.getDiagnosisForPreauthByKey(transacKey);	
			}
			
			
			intimationDto.setDiagnosis(diagnosisName);
			if(null != claimObj){
			intimationDto.setLineofBusiness(ReferenceTable.HEALTH_LOB_KEY.equals(claimObj.getIntimation().getPolicy().getLobId()) ? SHAConstants.HEALTH_LOB :""); 
			}
			Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
			if (portablityStatus != null) {
				intimationDto.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
				intimationDto.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
			}
				
			viewTopPanelDetails.init(intimationDto, claimDto, "TOP Panel Elements");
			UI.getCurrent().addWindow(viewTopPanelDetails);
		
	}

	public void getViewPolicy(String intimationNo) {
		
		VerticalLayout vLayout = new VerticalLayout();
		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		if(intimation != null){
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());
		
		if(policy.getProduct() != null && (ReferenceTable.getGMCProductCodeList().containsKey(policy.getProduct().getCode()) || (policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
				|| policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE))
				|| policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96))){
			viewPolicyDetail.setPolicyServiceAndPolicyForGMC(policyService, policy,intimation.getInsured(),
					masterService, intimationService);	
		}else{
			viewPolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
					masterService, intimationService);	
		}
		viewPolicyDetail.initView();
//		vLayout.addComponent(viewPolicyDetail);
		UI.getCurrent().addWindow(viewPolicyDetail);
		}else{
					
			OMPIntimation ompintimation = ompIntimationService
					.searchbyIntimationNo(intimationNo);
			if(ompintimation != null){
				Policy policy = policyService.getPolicy(ompintimation.getPolicy().getPolicyNumber());
				ompViewCurrentPolicyUI.setPolicyServiceAndPolicy(policyService, policy,
						masterService);
				ompViewCurrentPolicyUI.initView();
				UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
			}
				
			}
//		UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
	}

	
public void getViewBasePolicy(String intimationNo) {
		
		VerticalLayout vLayout = new VerticalLayout();
		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		if((intimation.getPolicy().getBasePolicyNo())!=null){
		Policy policy = policyService.getByPolicyNumber(intimation.getPolicy().getBasePolicyNo());
		if(policy!=null){
			viewBasePolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
					masterService, intimationService);	
		viewBasePolicyDetail.initView();
		UI.getCurrent().addWindow(viewBasePolicyDetail);
		}
		else{
			getErrorMessage("Intimation not available for this base policy");	
		}
		}
		else{
			getErrorMessage("Base Policy not available");	
		}
		
	}

public void getViewBasePolicy(Long policyKey) {
	Policy policyByKey = policyService.getPolicyByKey(policyKey);
	Policy Policy= policyService.getByPolicyNumber(policy.getBasePolicyNo());
		viewBasePolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
				masterService, intimationService);	
	viewBasePolicyDetail.initView();
	UI.getCurrent().addWindow(viewBasePolicyDetail);
	}



	public void getViewPolicy(Long policyKey) {
		Policy policy = policyService.getPolicyByKey(policyKey);
		viewPolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
				masterService, intimationService);	
		viewPolicyDetail.initView();
		UI.getCurrent().addWindow(viewPolicyDetail);
	}

	public void getViewClaimStatus1(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		if (intimation != null && intimation.getIntimationId() != null) {
			viewClaimStatus.init(intimation.getIntimationId());
			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewClaimStatus);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}

	}
	
	
	

	public void getViewClaimStatus(String intimationNo) {
		final Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		Hospitals hospital = hospitalService.searchbyHospitalKey(intimation
				.getKey());
		Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());

		CashlessDetailsDto cashlessDetailsDto = cashlessDetailsService
				.getCashlessDetails(intimation.getKey());

		if (a_claim != null) {
			ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);
			if (a_claimDto != null) {
				claimStatusDto.setClaimDto(a_claimDto);
				if (a_claimDto.getNewIntimationDto() != null
						&& a_claimDto.getNewIntimationDto()
								.getDateOfIntimation() != null) {
					Date tempDate = SHAUtils.formatTimestamp(a_claimDto
							.getNewIntimationDto().getDateOfIntimation());
					a_claimDto.getNewIntimationDto().setDateOfIntimation(
							SHAUtils.formatDate(tempDate));
				}

				if (a_claimDto.getNewIntimationDto() != null
						&& a_claimDto.getNewIntimationDto().getAdmissionDate() != null) {
					a_claimDto.getNewIntimationDto()
							.setAdmissionDate(
									a_claimDto.getNewIntimationDto()
											.getAdmissionDate());
				}
				claimStatusDto.setNewIntimationDto(a_claimDto
						.getNewIntimationDto());
				claimStatusDto.setCashlessDetailsDto(cashlessDetailsDto);
				cashLessTableMapper.getAllMapValues();
				ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
						claimStatusDto, intimation.getPolicy()
								.getActiveStatus() == null,
						cashLessTableDetails, cashlessTable,
						cashLessTableMapper, newIntimationService, intimation);
				UI.getCurrent().addWindow(intimationStatus);
			}
		} else {
			NewIntimationDto intimationToIntimationDetailsDTO = intimationService
					.getIntimationDto(intimation);
			if (intimationToIntimationDetailsDTO != null
					&& intimationToIntimationDetailsDTO.getDateOfIntimation() != null) {
				Date tempDate = SHAUtils
						.formatTimestamp(intimationToIntimationDetailsDTO
								.getDateOfIntimation());
				intimationToIntimationDetailsDTO.setDateOfIntimation(SHAUtils
						.formatDate(tempDate));
			}

			if (intimationToIntimationDetailsDTO != null
					&& intimationToIntimationDetailsDTO.getAdmissionDate() != null) {
				intimationToIntimationDetailsDTO
						.setAdmissionDate(intimationToIntimationDetailsDTO
								.getAdmissionDate());
			}
			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
					intimationToIntimationDetailsDTO, intimation.getPolicy()
							.getActiveStatus() == null, cashLessTableDetails,
					cashlessTable, cashLessTableMapper, newIntimationService,
					intimation);
			UI.getCurrent().addWindow(intimationStatus);
		}
	}

	public void getViewDocument(String intimationNo) {

		String strPolicyNo = "";
		OPIntimation opintimation = null;
		Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
		
		if(intimation !=null){
			strPolicyNo = intimation.getPolicy().getPolicyNumber();
		}else if(intimation == null){
			opintimation = intimationService.searchbyOPIntimationNo(intimationNo);
			strPolicyNo = opintimation.getPolicy().getPolicyNumber();
		}
//		else{
//			final OMPIntimation ompintimation = ompIntimationService
//					.searchbyIntimationNo(intimationNo);
//			if(ompintimation != null && ompintimation.getPolicy() != null){
//				strPolicyNo = ompintimation.getPolicy().getPolicyNumber();
//			}
//		}
		
		//String strPolicyNo = "P/181219/01/2014/001945";
		getViewDocumentByPolicyNo(strPolicyNo);

	//	UI.getCurrent().addWindow(viewDocuments);
		
	}
	
	public void getProposalViewDocument(String intimationNo) {
		String strPolicyNo = "";
		Insured insuredByKey =null;

		final OMPIntimation ompintimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		if(ompintimation != null && ompintimation.getPolicy() != null){
			strPolicyNo = ompintimation.getPolicy().getPolicyNumber();
			Long insuredKey = ompintimation.getInsured().getKey();
			insuredByKey = intimationService.getInsuredByKey(insuredKey);
		}
	
	getViewDocumentByPolicyNo(strPolicyNo); //should pass intimation as second parameter to get risk id
	}
	
	public void getPortabilityView(String intimationNo) {

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		OPIntimation opintimation = intimationService.getOPIntimationByNo(intimationNo);
		if(intimation != null){
		String strPolicyNo = intimation.getPolicy().getPolicyNumber();
		
		getPortabilityViewtByPolicyNo(strPolicyNo);
		} else if(opintimation != null){
			String strPolicyNo = opintimation.getPolicy().getPolicyNumber();
			
			getPortabilityViewtByPolicyNo(strPolicyNo);
		} else{
			OMPIntimation ompIntimation = ompIntimationService.searchbyIntimationNo(intimationNo);
			if(ompIntimation != null){
				String strPolicyNo = ompIntimation.getPolicy().getPolicyNumber();
				
				getPortabilityViewtByPolicyNo(strPolicyNo);
			}
		}
		
	}

	/*public void getViewDocumentByPolicyNo(String strPolicyNo,Insured insured) {
		
	String strDmsViewURL = null;
	Policy policyObj = null;
	BrowserFrame browserFrame = null;
	
	if (strPolicyNo != null) {
		policyObj = policyService.getByPolicyNumber(strPolicyNo);
		if (policyObj != null) {
			if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
				strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
				strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
				if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
					strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
				}else{
					strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
				}
				browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL));
			}else{
				strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
				browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL+strPolicyNo));
			}
		}
	}
	 
		VerticalLayout vLayout = new VerticalLayout();
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		String dmsToken = intimationService.createDMSToken(strPolicyNo);
		getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
		/*BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+dmsToken));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
	}
	
	public void getPortabilityViewtByPolicyNo(String strPolicyNo) {
		
		String strDmsViewURL = null;
		Policy policyObj = null;

		if (strPolicyNo != null) {
			DBCalculationService dbService = new DBCalculationService();
			policyObj = dbService.getPolicyObject(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DETAIL_VIEW_URL;
				}else{
					strDmsViewURL = BPMClientContext.PORTABLITY_VIEW_URL;
				}
			}
		}
		 
		strDmsViewURL = strDmsViewURL.replace("||", strPolicyNo);
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL));
		
		
		browserFrame.setHeight("600px");
		browserFrame.setWidth("200%");
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
//		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		btnSubmit.setWidth("-1px");
//		btnSubmit.setHeight("-10px");
//		btnSubmit.setDisableOnClick(true);
//		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		VerticalLayout vLayout = new VerticalLayout(browserFrame,btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getViewClaimStatusFromPremia(String intimationNo) {
		VerticalLayout vLayout = new VerticalLayout();

		String strDmsViewURL = BPMClientContext.PREMIA_CLAIM_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+intimationNo));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setWidth("100%");
		browserFrame.setHeight("150%");
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setWidth("100%");
		vLayout.setHeight("110%");
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getMERDetails(String intimationNo) {
		Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
		OMPIntimation ompIntimation = ompIntimationService.searchbyIntimationNo(intimationNo);
		OPIntimation opintimation = intimationService.getOPIntimationByNo(intimationNo);
		
		String healthCardNo = null;
		Long insuredKey = null;
		

		//Bancs Changes Start
				Policy policyObj = null;
				Builder builder = null;
				String strMERReportURL = null;
				if(intimation != null){
					insuredKey = intimation.getInsured().getKey();
				}
				Insured insuredByKey = intimationService.getInsuredByKey(insuredKey);
				if(intimation!= null){			
					healthCardNo = intimation.getInsured().getHealthCardNumber();
				}else if(ompIntimation!= null){			
					healthCardNo = ompIntimation.getInsured().getHealthCardNumber();
				} else if(opintimation != null){
					healthCardNo = opintimation.getInsured().getHealthCardNumber();
				}
				if(intimation != null){
					if(intimation.getPolicy() != null){
						policyObj = policyService.getByPolicyNumber(intimation.getPolicy().getPolicyNumber() );
						if (policyObj != null) {
							if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
								strMERReportURL = BPMClientContext.BANCS_MER_REPORT_URL;
								strMERReportURL = strMERReportURL.replace("POLICY", intimation.getPolicy().getPolicyNumber());		
								strMERReportURL = strMERReportURL.replace("MEMBER", insuredByKey!=null?String.valueOf(insuredByKey.getSourceRiskId()!=null?insuredByKey.getSourceRiskId():""):"");		
							}else{
								strMERReportURL = BPMClientContext.MER_REPORT_URL;
								strMERReportURL = strMERReportURL.replace("H_C_NO", healthCardNo);		
							}
						}
					}
					
				} else if(opintimation != null) {
					policyObj = policyService.getByPolicyNumber(opintimation.getPolicy().getPolicyNumber() );
					if (policyObj != null) {
						strMERReportURL = BPMClientContext.MER_REPORT_URL;
						strMERReportURL = strMERReportURL.replace("H_C_NO", healthCardNo);		
					}
				}
				
				//Bancs Changes End
		//String strMERReportURL = BPMClientContext.MER_REPORT_URL;
		
		
		UI.getCurrent().getPage().open(strMERReportURL, "_blank",1500,800,BorderStyle.DEFAULT);
	}

	public void getMERDetailsByHealthCard(String healthCardNo) {

		String strMERReportURL = BPMClientContext.MER_REPORT_URL;

		if(healthCardNo != null){			
			strMERReportURL = strMERReportURL.replace("H_C_NO", healthCardNo);		
			getUI().getPage().open(strMERReportURL, "_blank",1500,800,BorderStyle.DEFAULT);
		}else {
			getErrorMessage("Please select the Insured Patient Name");
		}

	}

	
	
	public void viewUploadedDocumentDetails(String intimationNo) {
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
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
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		
	/*	BrowserWindowOpener browserWindow = new BrowserWindowOpener(new ExternalResource(url));
		browserWindow.setFeatures("height=800,width=1400,resizable");
		browserWindow.setWindowName("_blank");
		browserWindow.extend(goButton);*/
	//	Page page = getUI().getPage();
		
		getUI().getPage().open(url, "_blank",1500,800,BorderStyle.NONE);
		

	/*	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/

	}
	

/*	public void viewUploadedDocumentDetails(String intimationNo) {
		
		
	}

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);

		}
		
		popup = new com.vaadin.ui.Window();

//		String url = BPMClientContext.GALAXY_DMS_URL + intimationNo;
//		getUI().getPage().open(url, "_blank");
		popup = new com.vaadin.ui.Window();
>>

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
			/*private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}*/
	
	
	
	
	public void viewUploadedQueryDocumentDetails(String intimationNo,String docType) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());	
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getQueryDocumentDetailsData(intimationNo,docType);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		Window popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	
	public void viewUploadedBillDocumentDetails(String intimationNo, String rodNo, String docType, String docTypeScrc, String docSource) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());	
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getQueryDocumentDetailsDataByRod(rodNo,docType,docTypeScrc,docSource);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		Window popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true); 
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	public void viewClaimStatusUpdated(String intimationNo) {

		ViewTmpClaim claim = null;

		try {
			final ViewTmpIntimation intimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);
			// Hospitals hospital =
			// hospitalService.searchbyHospitalKey(intimation
			// .getKey());
			claim = claimService.getTmpClaimforIntimation(intimation.getKey());
			if (claim != null) {
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ClaimStatusRegistrationDTO registrationDetails = instance
						.getRegistrationDetails(claim);
				 Intimation intimationObj =
				 intimationService.getIntimationByKey(claim.getIntimation().getKey());

				 ViewClaimStatusDTO intimationDetails = EarlierRodMapper
						 .getViewClaimStatusDto(intimationObj);
				 
				 if(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey())){
					 boolean isjiopolicy = false;	
						isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
						  
						intimationDetails.setJioPolicy(isjiopolicy);
					      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
					      Insured MainMemberInsured = null;
					      
					      if(insuredByKey.getDependentRiskId() == null){
					    	  MainMemberInsured = insuredByKey;
					      }else{
					    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
					    	  MainMemberInsured = insuredByPolicyAndInsuredId;
					      }
					      
					      if(MainMemberInsured != null){
					    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
					      }		
					}
				 
				 
				Hospitals hospitals = hospitalService
						.getHospitalById(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				EarlierRodMapper.invalidate(instance);
				intimationDetails.setClaimKey(claim.getKey());
				String name = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey()).getName()
						: "";
				intimationDetails.setPatientNotCoveredName(name);
				String relationship = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey())
						.getBabyRelationship().getValue() : "";
				intimationDetails.setRelationshipWithInsuredId(relationship);
				List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService
						.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, claim);

				DocAcknowledgement docAcknowledgement = reimbursementService
						.findAcknowledgment(this.rodKey);
				
				
				NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
				
				PreauthDTO preauthDTO = new PreauthDTO();
				preauthDTO.setNewIntimationDTO(intimationDto);
				Claim claimObj = intimationService.getClaimforIntimation(intimationObj.getKey());
				if(claimObj!=null){
					preauthDTO.setCrmFlagged(claimObj.getCrcFlag());
					preauthDTO.setVipCustomer(claimObj.getIsVipCustomer());
					preauthDTO.setClaimPriorityLabel(claimObj.getClaimPriorityLabel());
				}
				intimationDetails.setPreauthDTO(preauthDTO);
				
				
				if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					rodIntimationDetailsObj
							.init(intimationDetails, this.rodKey);
				} else {
					rodIntimationDetailsObj.init(intimationDetails, null);
				}
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(rodIntimationDetailsObj);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		} catch (Exception e) {
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	}



	public void viewOPClaimStatusUpdated(String intimationNo) {

		ViewTmpOPClaim claim = null;

		try {
			final ViewTmpOPIntimation intimation = intimationService.searchbyOPIntimationNoFromViewIntimation(intimationNo);
			// Hospitals hospital =
			// hospitalService.searchbyHospitalKey(intimation
			// .getKey());
			claim = claimService.getTmpOPClaimforOPIntimation(intimation.getKey());
			if (claim != null) {
				/*ClaimStatusRegistrationDTO registrationDetails = EarlierRodMapper.getInstance().getRegistrationDetails(claim);
				 OPIntimation intimationObj =
				 intimationService.getOPIntimationByKey(claim.getIntimation().getKey());

				 ViewClaimStatusDTO intimationDetails = EarlierRodMapper.getViewClaimStatusDto(intimationObj);
				 
				 if(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey())){
					 boolean isjiopolicy = false;	
						isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
						  
						intimationDetails.setJioPolicy(isjiopolicy);
					      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
					      Insured MainMemberInsured = null;
					      
					      if(insuredByKey.getDependentRiskId() == null){
					    	  MainMemberInsured = insuredByKey;
					      }else{
					    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
					    	  MainMemberInsured = insuredByPolicyAndInsuredId;
					      }
					      
					      if(MainMemberInsured != null){
					    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
					      }		
					}
				 
				 
				Hospitals hospitals = hospitalService
						.getHospitalById(intimation.getHospital());
//				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				intimationDetails.setClaimKey(claim.getKey());
				String name = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey()).getName()
						: "";
				intimationDetails.setPatientNotCoveredName(name);
				String relationship = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey())
						.getBabyRelationship().getValue() : "";
				intimationDetails.setRelationshipWithInsuredId(relationship);
				List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService
						.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, claim);

				DocAcknowledgement docAcknowledgement = reimbursementService
						.findAcknowledgment(this.rodKey);*/
				OPClaim claimByKey = claimService.getOPClaimByKey(claim.getKey());
				PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey.getIntimation().getPolicy());
				
				
				OPHealthCheckup opHealthByClaimKey = outPatientService.getOpHealthByClaimKey(claimByKey.getKey());
				List<OPHCHospitalDetails> opHospitalDtlsByKey = outPatientService.getHospitalDetailsByHCKey(opHealthByClaimKey.getKey());
				List<OPDocumentBillEntry> billDtls = outPatientService.getOpBillEntryByOPHealthKey(opHealthByClaimKey.getKey());
				List<OPSpeciality> specialityList = outPatientService.getSpecialityDtlsByHcKey(opHealthByClaimKey.getKey());
				OutpatientMapper mapper = new OutpatientMapper();
//				List<DiagnosisDetailsOPTableDTO> diagnosisListenerTableList = outpatientService.getDiagnosisList(opHealthByClaimKey.getKey());
				OutPatientDTO mainDTO = mapper.getOutpatientDTO(opHealthByClaimKey);
//				mainDTO.setDiagnosisListenerTableList(diagnosisListenerTableList);
				mainDTO.setStrUserName((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID));
				mainDTO.setStrPassword((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.PASSWORD));
				//mainDTO.setHumanTask(tableDTO.getOpHumanTask());
//				Map<String, Integer> healthCheckupFlag = calcService.getHealthCheckupFlag(claimByKey.getIntimation().getPolicy().getKey());
				/*if(healthCheckupFlag.get(SHAConstants.OUTPATIENT_FLAG) == 1) {
					mainDTO.setOutpatientFlag(true);
				}
				
				if(healthCheckupFlag.get(SHAConstants.HEALTH_CHECK_UP_FLAG) == 1) {
					mainDTO.setHealthCheckupFlag(true);
				}*/
				NewIntimationDto newIntimationDto = intimationService.getOPIntimationDto(claimByKey.getIntimation());
				// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
				ClaimDto claimDTO =  OPClaimMapper.getInstance().getClaimDto(claimByKey);
				mainDTO.setNewIntimationDTO(newIntimationDto);
				mainDTO.setClaimDTO(claimDTO);
				mainDTO.setPolicy(claimByKey.getIntimation().getPolicy());
				mainDTO.setClaimKey(claimByKey.getKey());
				mainDTO.setIntimationId(claimByKey.getIntimation().getIntimationId());
				mainDTO.setSelectedHospital(newIntimationDto.getHospitalDto());
				mainDTO.setApprovalRemarks(opHealthByClaimKey.getApprovalRemarks());
				mainDTO.setApprovedAmt(opHealthByClaimKey.getAmountPayable());
				String pioName = "";
				if(claimByKey.getIntimation() != null && claimByKey.getIntimation().getPolicy().getHomeOfficeCode() != null) {
					List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService.getInsuredOfficeNameByDivisionCode(claimByKey.getIntimation().getPolicy().getHomeOfficeCode());
					 if(insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
			    		pioName = insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName();
			    	  }					        
				}
				Long docReceivedId = ReferenceTable.RECEIVED_FROM_HOSPITAL;
//				mainDTO.setPreviousAccntDetailsList(calcService.getPreviousAccountDetails(claimByKey.getIntimation().getInsured().getInsuredId(),docReceivedId));
				mainDTO.setPioName(pioName);
				mainDTO.setPolicyDto(policyDto);
				
				mainDTO.getDocumentDetails().setDocEmailId(claimByKey.getIntimation().getCallerEmail());
				mainDTO.getDocumentDetails().setReasonforConsultation(claimByKey.getIntimation().getAdmissionReason());
				mainDTO.getDocumentDetails().setDocSubmittedContactNo(claimByKey.getDoctorNo());
				mainDTO.getDocumentDetails().setDocSubmittedName(claimByKey.getIntimation().getDoctorName());
				mainDTO.getDocumentDetails().setRemarksForEmergencyAccident(claimByKey.getRemarksForEmergencyAccident());
				mainDTO.getDocumentDetails().setEmergencyFlag(claimByKey.getEmergencyFlag());
				mainDTO.getDocumentDetails().setAccidentFlag(claimByKey.getAccidentFlag());
				Long opCoverSecKey = claimByKey.getConsulationTypeId();
				MasOpClaimSection opsectionDtls = outPatientService.getSectionDtlsByKey(opCoverSecKey);
				if(opsectionDtls != null){
					SelectValue secDtls = new SelectValue();
					secDtls.setId(opsectionDtls.getKey());
					secDtls.setValue(opsectionDtls.getOpDescription());
					mainDTO.getDocumentDetails().setConsultationType(secDtls);
				}
				if(claimByKey.getPhysicalDocReceivedFlag() != null){
					mainDTO.getDocumentDetails().setPhysicalDocsReceivedFlag(claimByKey.getPhysicalDocReceivedFlag());
				}
				if(claimByKey.getPhysicalDocReceivedDate() != null){
					mainDTO.getDocumentDetails().setPhysicalDocsReceivedDate(claimByKey.getPhysicalDocReceivedDate());
				}
				
				List<OPSpecialityDTO> specialityDto = new ArrayList<OPSpecialityDTO>();
				if(specialityList != null && !specialityList.isEmpty()){
					Long sno = 1l;
					for (OPSpeciality opSpeciality : specialityList) {
						OPSpecialityDTO specList = new OPSpecialityDTO();
						SelectValue spe = new SelectValue();
						Long specId = opSpeciality.getSpecialityTypeId();
						SpecialityType spType = outPatientService.getSpecialityTypeByKey(specId);	
						spe.setId(spType.getKey());
						spe.setValue(spType.getValue());
						specList.setSpecialityType(spe);
						specList.setPedfromPolicy(opSpeciality.getPedName());
						SelectValue ped = new SelectValue();
						if(opSpeciality != null && opSpeciality.getPedRelatedId().equals(1061l)){
							ped.setId(1061l);
							ped.setValue("Not Related");
						} else {
							ped.setId(1062l);
							ped.setValue("Related PED");
						}
						specList.setPed(ped);
						specList.setRemarks(opSpeciality.getRemarks());
						specList.setsNo(sno.longValue());
						specialityDto.add(specList);
						sno++;
					}
				}
				
				if(specialityDto != null && !specialityDto.isEmpty()){
					mainDTO.setSpecialityDTOList(specialityDto);
				}
				
				if(opHospitalDtlsByKey != null && !opHospitalDtlsByKey.isEmpty()){
					for (OPHCHospitalDetails ophcHospitalDetails : opHospitalDtlsByKey) {
						if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Consultation")){
							mainDTO.getBenefitsAvailedDto().setState(ophcHospitalDetails.getState());
							mainDTO.getBenefitsAvailedDto().setCity(ophcHospitalDetails.getCity());
							mainDTO.getBenefitsAvailedDto().setHospitalName(ophcHospitalDetails.getHospitalName());
							mainDTO.getBenefitsAvailedDto().setHospitalConsulationName(ophcHospitalDetails.getHospitalConsulationName());
							mainDTO.getBenefitsAvailedDto().setHospitalAddress(ophcHospitalDetails.getHospitalAddress());
							mainDTO.getBenefitsAvailedDto().setHospitalContactNumber(ophcHospitalDetails.getHospitalContactNo());
							mainDTO.getBenefitsAvailedDto().setHospitalFaxNo(ophcHospitalDetails.getHospitalFaxNo());
							mainDTO.getBenefitsAvailedDto().setHospitalDoctorName(ophcHospitalDetails.getHospitalDoctorsName());
							if(ophcHospitalDetails.getHospitalType() != null 
									&& ophcHospitalDetails.getHospitalType().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
								mainDTO.getBenefitsAvailedDto().setConsulationProvider(Boolean.TRUE);
							} else if(ophcHospitalDetails.getHospitalType() != null 
									&& ophcHospitalDetails.getHospitalType().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
								mainDTO.getBenefitsAvailedDto().setConsulationProvider(Boolean.FALSE);
							}
							if(ophcHospitalDetails.getClinicFlag() != null){
								mainDTO.getBenefitsAvailedDto().setClinic(ophcHospitalDetails.getClinicFlag().equalsIgnoreCase("Y") ? true : false);
							}
							mainDTO.setConsulation(false);
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Diagnostics")){
							mainDTO.setDiagnosis(true);
							
							mainDTO.getBenefitsAvailedDto().setProviderState(ophcHospitalDetails.getState());
							mainDTO.getBenefitsAvailedDto().setProviderCity(ophcHospitalDetails.getCity());
							mainDTO.getBenefitsAvailedDto().setProviderName(ophcHospitalDetails.getHospitalName());
							mainDTO.getBenefitsAvailedDto().setProviderAddress(ophcHospitalDetails.getHospitalAddress());
							mainDTO.getBenefitsAvailedDto().setProviderContactNo(ophcHospitalDetails.getHospitalContactNo());
							if(ophcHospitalDetails.getHospitalType() != null 
									&& ophcHospitalDetails.getHospitalType().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
								mainDTO.getBenefitsAvailedDto().setDiagnosisProvider(Boolean.TRUE);
							} else if(ophcHospitalDetails.getHospitalType() != null 
									&& ophcHospitalDetails.getHospitalType().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
								mainDTO.getBenefitsAvailedDto().setDiagnosisProvider(Boolean.FALSE);
							} 
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Physiotherapy")){
							mainDTO.setPhysiotherapy(true);
							mainDTO.getBenefitsAvailedDto().setPhysiotherapistName(ophcHospitalDetails.getHospitalName());
							mainDTO.getBenefitsAvailedDto().setClinicAddress(ophcHospitalDetails.getHospitalAddress());
							mainDTO.getBenefitsAvailedDto().setPhysiotherapistContactNo(ophcHospitalDetails.getHospitalContactNo());
							mainDTO.getBenefitsAvailedDto().setEmailId(ophcHospitalDetails.getEmailId());
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Medicine")){
							mainDTO.setMedicine(true);
							mainDTO.getBenefitsAvailedDto().setPharmacytName(ophcHospitalDetails.getHospitalName());
							mainDTO.getBenefitsAvailedDto().setPharmacyContactNo(ophcHospitalDetails.getHospitalContactNo());
							mainDTO.getBenefitsAvailedDto().setPharmacyAddress(ophcHospitalDetails.getHospitalAddress());
							mainDTO.getBenefitsAvailedDto().setPharmacyemailId(ophcHospitalDetails.getEmailId());

						} 
					}
				}
				
				List<UploadDocumentDTO> listOfDocs = new ArrayList<UploadDocumentDTO>();
				if(billDtls != null && !billDtls.isEmpty()){
					int i = 1;
					for (OPDocumentBillEntry opDocumentBillEntry : billDtls) {
						UploadDocumentDTO doc = new UploadDocumentDTO();
						Long billType = opDocumentBillEntry.getBillTypeId();
						MastersValue billValue = masterService.getMaster(billType);
						doc.setFileTypeValue(billValue.getValue());
						if(opHealthByClaimKey != null){
						if(opDocumentBillEntry.getBillDate() != null){
							doc.setBillDate(opDocumentBillEntry.getBillDate());
						}
						if(opDocumentBillEntry.getBillNumber() != null){
							doc.setBillNo(opDocumentBillEntry.getBillNumber());
						}
						if(opDocumentBillEntry.getClaimedAmount() != null){
							doc.setBillAmt(opDocumentBillEntry.getClaimedAmount());
						}
						if(opDocumentBillEntry.getDeductibleAmt() != null){
							doc.setDeductibleAmt(opDocumentBillEntry.getDeductibleAmt());
						}
						if(opDocumentBillEntry.getNonPayableAmount() != null){
							doc.setNonPaybleAmt(opDocumentBillEntry.getNonPayableAmount());
						}
						if(opDocumentBillEntry.getPayableAmt() != null){
							doc.setBillValue(opDocumentBillEntry.getPayableAmt().doubleValue());
						}
						}
						doc.setRemarks(opDocumentBillEntry.getBillEntryRemarks());
						String fileName = SHAFileUtils.viewFileByToken(opDocumentBillEntry.getDocumentToken());
						doc.setFileName(opDocumentBillEntry.getFileName() != null ? opDocumentBillEntry.getFileName() :"");
						doc.setDmsDocToken(opDocumentBillEntry.getDocumentToken());
						doc.setsNo(String.valueOf(i++));
						listOfDocs.add(doc);
					}
				}
				mainDTO.setUploadedDocsTableList(listOfDocs);
				
				Long paymentMode = opHealthByClaimKey.getPaymentNoteId();
				if(paymentMode != null){
					int i = 1;
					mainDTO.getViewPaymentDto().setSerialNo(i);
					MastersValue paymentModeValue = masterService.getMaster(paymentMode);
					mainDTO.getViewPaymentDto().setPaymentTypeValue(paymentModeValue.getValue());
					if(opHealthByClaimKey.getIfscCode() != null){
						BankMaster bankDtls = masterService.getBankDetails(opHealthByClaimKey.getIfscCode());
						mainDTO.getViewPaymentDto().setBankName(bankDtls.getBankName());
						mainDTO.getViewPaymentDto().setBranchName(bankDtls.getBranchName());
					}
					mainDTO.getViewPaymentDto().setAccontNo(opHealthByClaimKey.getAccountNumber());
					mainDTO.getViewPaymentDto().setIfscCode(opHealthByClaimKey.getIfscCode());
					mainDTO.getViewPaymentDto().setChequeNo(opHealthByClaimKey.getChequeNo());
					mainDTO.getViewPaymentDto().setChequeDate(opHealthByClaimKey.getChequeDate());
					String claimType = claimByKey.getClaimType().getValue().toString();
					mainDTO.getViewPaymentDto().setClaimType(claimType);
				}

			/*	if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					opViewClaimStatus
							.init(intimationDetails, this.rodKey);
				} else {
					opViewClaimStatus.init(intimationDetails, null);
				}*/
				opViewClaimStatus.init(mainDTO, null);
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(opViewClaimStatus);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		} catch (Exception e) {
			//Notification.show("Claim Number is not generated", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	}
	public void viewClaimStatusUpdated(String intimationNo,boolean auditvisible) {
		viewClaimStatusUpdated(intimationNo);
		if(auditvisible){
		rodIntimationDetailsObj.setAuditDetails();
		}
		
	}

	public void viewSearchClaimStatus(String intimationNo, Long rodKey) {
		
		final ViewTmpIntimation intimation = intimationService
				.searchbyIntimationNoFromViewIntimation(intimationNo);
		
		final OMPIntimation ompintimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		
		// Hospitals hospital =
		// hospitalService.searchbyHospitalKey(intimation
		
		// .getKey());
		if(intimation != null){
		ViewTmpClaim claim = claimService.getTmpClaimforIntimation(intimation
				.getKey());
		
		this.rodKey = rodKey;
		
		if(this.rodKey != null && this.rodKey.intValue() != 0){
			setRodNumber(this.rodKey);
		}
		
		if(null != claim){
		if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
			
			if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
				
				showClaimStatus(intimation, claim);                            // fetch claim status from galaxy
				
			}else{
				getViewClaimStatusFromPremia(intimation.getIntimationId());    // fetch claim status from premia end.
			}
			
		}else{
			showClaimStatus(intimation, claim);     //fetch claim status only from galaxy
		}}else{
			getErrorMessage("Claim details not available");	
		}
		}else{
			if(ompintimation != null && ompintimation.getKey() != null){
			OMPClaim claim = ompclaimService.getClaimforIntimation(ompintimation
					.getKey());
			
			this.rodKey = rodKey;
			if(claim != null ){				
				showompClaimStatus(ompintimation, claim);                            // fetch claim status from galaxy
			}
			}			
		}
		

	}
	public void showompClaimStatus(OMPIntimation ompintimation,OMPClaim claim)
	{
		if(claim != null){
			VerticalLayout vLayout = new VerticalLayout();
		try {
			
			ClaimStatusRegistrationDTO registrationDetails = OMPEarlierRodMapper.getInstance()
					.getRegistrationDetails(claim);
			// Intimation intimation =
			// intimationService.getIntimationByKey(claim.getIntimation().getKey());
			ViewClaimStatusDTO intimationDetails = OMPEarlierRodMapper
					.getViewClaimStatusDto(ompintimation);
			
			OMPViewClaimStatusDto claimStatusDetails = OMPViewClaimStatusMapper.getInstance().getOMPViewClaimStatusDto(intimationDetails);
			Hospitals hospitals = hospitalService.getHospitalById(ompintimation
					.getHospital());
//			getHospitalDetails(intimationDetails, hospitals);
			intimationDetails
					.setClaimStatusRegistrionDetails(registrationDetails);
			intimationDetails.setClaimKey(claim.getKey());
			
			
			List<ViewDocumentDetailsDTO> listDocumentDetails = ompRodBillEntryService
					.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
			intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);

			setOMPPaymentDetails(intimationDetails, claim);

			OMPDocAcknowledgement docAcknowledgement = ompRodBillEntryService
					.findAcknowledgment(this.rodKey);
			if (docAcknowledgement != null) {					
				ompclaimStatusUI.init(claimStatusDetails, this.rodKey);         
				vLayout.addComponent(ompclaimStatusUI);
			} else {
				ompViewIntimationDetails.init(ompintimation);
				vLayout.addComponent(ompViewIntimationDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("No preauth for this intimation");
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Claim Status");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Claim Number is not generated");
		}
	
	}
	private void showClaimStatus(final ViewTmpIntimation intimation,
			ViewTmpClaim claim) {
		
		if(claim != null){
		
		try {
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ClaimStatusRegistrationDTO registrationDetails = instance
					.getRegistrationDetails(claim);
			
			 Intimation intimationObj = intimationService.getIntimationByKey(claim.getIntimation().getKey());
			ViewClaimStatusDTO intimationDetails = EarlierRodMapper
					.getViewClaimStatusDto(intimationObj);
			Hospitals hospitals = hospitalService.getHospitalById(intimation
					.getHospital());
			getHospitalDetails(intimationDetails, hospitals);
			intimationDetails
					.setClaimStatusRegistrionDetails(registrationDetails);
			EarlierRodMapper.invalidate(instance);
			intimationDetails.setClaimKey(claim.getKey());
			String name = null != intimationService
					.getNewBabyByIntimationKey(intimationDetails
							.getIntimationKey()) ? intimationService
					.getNewBabyByIntimationKey(
							intimationDetails.getIntimationKey()).getName()
					: "";
			intimationDetails.setPatientNotCoveredName(name);
			String relationship = null != intimationService
					.getNewBabyByIntimationKey(intimationDetails
							.getIntimationKey()) ? intimationService
					.getNewBabyByIntimationKey(
							intimationDetails.getIntimationKey())
					.getBabyRelationship().getValue() : "";
			intimationDetails.setRelationshipWithInsuredId(relationship);
			
			 if(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey())){
				 boolean isjiopolicy = false;	
					isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
					  
					intimationDetails.setJioPolicy(isjiopolicy);
				      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
				      Insured MainMemberInsured = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				      }
				      
				      if(MainMemberInsured != null){
				    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
				      }		
				}
			
			
			List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService
					.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
			intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);
			Policy policyByPolicyNubember = policyService.getPolicyByPolicyNubember(intimation.getPolicyNumber());
			if(null != intimation && null != intimation.getPolicy() && 
					null != policyByPolicyNubember.getProduct() && 
					null != policyByPolicyNubember.getProduct().getKey() &&
					(ReferenceTable.getGPAProducts().containsKey(intimationObj.getPolicy().getProduct().getKey()))){
				
				//intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
				if(intimationObj.getPaPatientName() !=null && !intimationObj.getPaPatientName().isEmpty())
				{
					intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
				}else
				{
					intimationDetails.setInsuredPatientName((intimationObj.getInsured() !=null && intimationObj.getInsured().getInsuredName() !=null) ? intimationObj.getInsured().getInsuredName() : "");
				}
			}

			setPaymentDetails(intimationDetails, claim);

			DocAcknowledgement docAcknowledgement = reimbursementService
					.findAcknowledgment(this.rodKey);
			
			NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
			
			PreauthDTO preauthDTO = new PreauthDTO();
			preauthDTO.setNewIntimationDTO(intimationDto);
			Claim claimObj = intimationService.getClaimforIntimation(intimationObj.getKey());
			if(claimObj!=null){
				preauthDTO.setCrmFlagged(claimObj.getCrcFlag());
				preauthDTO.setVipCustomer(claimObj.getIsVipCustomer());
				preauthDTO.setClaimPriorityLabel(claimObj.getClaimPriorityLabel());
				preauthDTO.setZohoGrievanceFlag(claimObj.getZohoGrivanceFlag());
			}
			intimationDetails.setPreauthDTO(preauthDTO);
			
			
			if (docAcknowledgement != null
					&& docAcknowledgement.getHospitalisationFlag() != null
					&& !docAcknowledgement.getHospitalisationFlag()
							.equalsIgnoreCase("Y")) {
				rodIntimationDetailsObj.init(intimationDetails, this.rodKey);
			} else {
				rodIntimationDetailsObj.init(intimationDetails, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("No preauth for this intimation");
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Claim Status");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(rodIntimationDetailsObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				if(rodIntimationDetailsObj != null){
					rodIntimationDetailsObj.clearObjects();
				}
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Claim Number is not generated");
		}
	}

	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = reimbursementService
						.getRimbursementForPayment(claim.getClaimId());
			}

			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
			bean.setAccountName(reimbursementForPayment.getAccountNumber());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setChequeDate(formatDate);
				}
			}
			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	
	public OMPClaimPayment setOMPPaymentDetails(ViewClaimStatusDTO bean,
			OMPClaim claim) {

		try {
			
			OMPClaimPayment reimbursementForPayment = null;
			
			OMPReimbursement settledReimbursement = ompRodBillEntryService.getLatestReimbursementDetailsByclaimKey(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = ompRodBillEntryService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = ompRodBillEntryService
						.getRimbursementForPayment(claim.getClaimId());
			}

			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().getValue() != null ? reimbursementForPayment.getPaymentType().getValue() :"");
			bean.setAccountName(reimbursementForPayment.getAccountNo());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDdNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().getValue().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDdDate()!= null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDdDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDdDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDdDate());
					bean.setChequeDate(formatDate);
				}
			}
			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = instance
				.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		
		/*if(hospitals.getFspFlag() !=null && hospitals.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){

			intimationDetails.setHospitalName(hospitalDetails.getHospitalName()+"(VSP)");
		}
		else{

			intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		}*/

		if(hospitals.getFspFlag() !=null && hospitals.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){

			intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue()+"(VSP)");
		}
		else{

			intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
		}

		
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		intimationDetails.setHospitalFlag(hospitals.getSuspiciousType());
		intimationDetails.setSuspiciousReason(hospitals.getClmPrcsInstruction());
		EarlierRodMapper.invalidate(instance);
	}

	public void getViewClaimHistory(String intimationNo) {
		Intimation intimation = null;
		OMPIntimation ompIntimation = null;
		Boolean result = false;
		VerticalLayout vlayout = null;
		
		if(intimationNo != null){
		intimation = intimationService
				.getIntimationByNo(intimationNo);
		}
		
		if(intimation  == null){
			ompIntimation = ompIntimationService.getIntimationByNo(intimationNo);
		}
		
		if (ompIntimation != null) {
//			if (this.rodKey == null) {
//				 result = viewOMPClaimHistoryRequest.init(ompIntimation);
//			}
//			else if(this.rodKey.equals(0l)) {
//				result = viewOMPClaimHistoryRequest.showCashlessAndReimbursementHistory(ompIntimation);
//			}
//			else{
			vlayout = viewOMPClaimHistoryRequest.showOMPReimbursementClaimHistory(
						ompIntimation.getKey());
//			}
		}		
		
		if (intimation != null) {
			if (this.rodKey == null) {
				 result = viewClaimHistoryRequest.init(intimation);
			} else if(this.rodKey.equals(0l)) {
				result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			}else{
				//As said Raja.A 29/03/2018 ref:DR.KrishnaShankar
				/*result = viewClaimHistoryRequest.showReimbursementClaimHistory(
				intimation.getKey(), this.rodKey);*/
				result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			}
		}	
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							if(viewClaimHistoryRequest!=null){
								viewClaimHistoryRequest.setClearReferenceData();
							}
							System.out.println("Close listener called");
						}
					});
		
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					
			if(result){
				popup.setContent(viewClaimHistoryRequest);
			}		
			else if(vlayout != null){
				popup.setContent(vlayout);
			}
			else{
					getErrorMessage("Claim is not available");
			}
		 
		
		if(intimation == null && ompIntimation == null){
			getErrorMessage("History is not available");
		}
	}
	
public void getViewClaimHistoryForOPHealthCheckUp(String intimationNo) {
		
		if(intimationNo != null){
			OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
		if (intimation != null) {
			viewClaimHistoryRequest.showOPHealthCheckUpOPClaimHistory(intimation);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View History");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewClaimHistoryRequest);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		
		}else{
			getErrorMessage("History is not available");
		}
	}

	public void getViewProductBenefits(String registrationBean) {
		ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
				.get();
		Intimation intimation = intimationService
				.searchbyIntimationNo(registrationBean);
		if(null != intimation && null != intimation.getPolicy() && null != intimation.getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()))){
		a_viewProductBenefits.showValues(intimation.getPolicy()
				.getPolicyNumber());
		UI.getCurrent().addWindow(a_viewProductBenefits);
		}/*else if(null != intimation && null != intimation.getPolicy() && null != intimation.getPolicy().getProduct().getKey() &&
				(intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) 
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) 
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY)
						|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GROUP_HEALTH_INSURANCE_PROD_KEY))){

			if(intimation != null && intimation.getPolicy() != null &&null != intimation.getPolicy().getKey()){			

				getGMCProductBenefitwithPolicyCkause(intimation.getPolicy().getKey());
			}
		}*/
		else
		{
			if(null != intimation.getPolicy().getKey()){			
				
				getGMCProductBenefitwithPolicyCkause(intimation.getPolicy().getKey());
			}
		}
		
	}

	public void getViewProductBenefits(Long policyKey) {
		ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
				.get();
		Policy policyByKey = policyService.getPolicyByKey(policyKey);
		
		if(null != policyByKey && null != policyByKey.getProduct() && null != policyByKey.getProduct().getKey() &&
				!(ReferenceTable.getGMCProductList().containsKey(policyByKey.getProduct().getKey()))){
			
			a_viewProductBenefits.showValues(policyByKey.getPolicyNumber());
			UI.getCurrent().addWindow(a_viewProductBenefits);
		}/*else if(null != policyByKey && null != policyByKey.getProduct() && null != policyByKey.getProduct().getKey() &&
				(policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) 
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) 
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY)
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.GROUP_TOPUP_PROD_KEY)
						|| policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GROUP_HEALTH_INSURANCE_PROD_KEY))){

			if(null != policyByKey){			
				
				getGMCProductBenefitwithPolicyCkause(policyByKey.getKey());
			}
		}*/
		else
		{
			if(null != policyByKey){			
			
				getGMCProductBenefitwithPolicyCkause(policyByKey.getKey());
			}
		}
		
	}


	public void getViewBalanceSumInsured(String intimationNo) {
//		ViewBalenceSumInsured a_viewBalenceSumInsured = viewBalenceSumInsured
//				.get();
		VerticalLayout vlayout = new VerticalLayout();		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Balance Sum Insured");
		popup.setWidth("90%");
		popup.setHeight("100%");
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				if(balanceSumInsured!=null){
					balanceSumInsured.setClearReferenceData();
				}
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		
		Intimation intimation = intimationService
					.getIntimationByNo(intimationNo);

		if(intimation == null){
			vlayout.addComponent(getViewOmpBalanceSI(intimationNo));
			popup.setContent(vlayout);
			UI.getCurrent().addWindow(popup);
		}
		else{
		//If PA
			if(intimation.getProcessClaimType() !=null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			{			
				popup = getPASumInsured(intimationNo);				
			}
			else
			{
			balanceSumInsured.bindFieldGroup(intimationNo, rodKey);
	//		UI.getCurrent().addWindow(a_viewBalenceSumInsured);
			popup.setContent(balanceSumInsured);
			}
			UI.getCurrent().addWindow(popup);
		}
		/**
		 * below code is commented by yosuva.
		 */
		//UI.getCurrent().addWindow(popup);
		
	}


	
	/*public void getComprehensiveBalanceSumInsured(String intimationNo) {
		
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		
		
		
		if(intimationNo != null)
		{
<<<<<<< HEAD
//			balanceSumInsuredComp.init(intimation);
=======
			balanceSumInsuredComp.init();
>>>>>>> 11a97d12dee6ae935357fc3f2760a7c27c319842
		}
		
		popup = new com.vaadin.ui.Window();
		popup.setCaption("Comprehensive Balance Sum Insured");
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(balanceSumInsuredComp);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		
		
	}*/

	public void getViewPreviousClaimDetails(String registrationBean) {
		
		/*ViewPreviousClaims a_viewPreviousClaims = viewPreviousClaims.get();
		a_viewPreviousClaims.showValues(registrationBean);
		UI.getCurrent().addWindow(a_viewPreviousClaims);*/
	
	/*	VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,registrationBean);					
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewPreviousClaimWindowOpen);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
		
		
		BrowserWindowOpener opener = new BrowserWindowOpener(PreviousClaimWindowUI.class);
		opener.setFeatures("height=500,width=800,resizable");
		opener.setWindowName("_blank");
//	    opener.extend(goButton);
	    opener.extend(btnGo);*/
	   
	    
	    
	}
	
	
	public void getViewPreviousClaimDetailsForOP(Long policyKey) {
		ViewPreviousClaims a_viewPreviousClaims = viewPreviousClaims.get();
		a_viewPreviousClaims.showValuesForOP(policyKey);
		UI.getCurrent().addWindow(a_viewPreviousClaims);
	}
	
	

	public void getViewHospitalDetails(String registrationBean) {
		ViewHospitalDetails a_viewHospitalDetails = viewHospitalDetails.get();
		a_viewHospitalDetails.showValues(registrationBean);
		UI.getCurrent().addWindow(a_viewHospitalDetails);
	}

	/*
	 * private void getViewIntimation(String intimationNo) { Intimation
	 * intimation = intimationService.getIntimationByNo(intimationNo);
	 * 
	 * List<Claim> claimByIntimation = claimService
	 * .getClaimByIntimation(intimation.getKey());
	 * 
	 * boolean claimExists = (claimByIntimation != null && claimByIntimation
	 * .size() > 0) ? true : false;
	 * 
	 * NewIntimationDto intimationToIntimationDetailsDTO = policyService
	 * .newIntimationToIntimationDTO(intimation);
	 * 
	 * if (intimation.getStatus() != null &&
	 * intimation.getStatus().equalsIgnoreCase("SUBMITTED") && claimExists ==
	 * true) { ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
	 * intimationToIntimationDetailsDTO, intimation .getPolicy().getStatus() ==
	 * null, cashLessTableDetails, cashlessTable, cashLessTableMapper,
	 * newIntimationService, intimation);
	 * 
	 * UI.getCurrent().addWindow(intimationStatus); } else if
	 * (intimation.getStatus() != null &&
	 * intimation.getStatus().equalsIgnoreCase("SUBMITTED") && claimExists ==
	 * false) { ViewIntimation intimationDetails = new ViewIntimation(
	 * intimationToIntimationDetailsDTO, hospitalService);
	 * UI.getCurrent().addWindow(intimationDetails); }
	 * 
	 * final Intimation intimation = intimationService
	 * .searchbyIntimationNo(registrationBean);
	 * 
	 * Hospitals hospital = policyService.getVWHospitalByKey(intimation
	 * .getHospital());
	 * 
	 * NewIntimationDto a_intimationDto = DtoConverter
	 * .intimationToIntimationDTO(intimation, hospital);
	 * 
	 * Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
	 * 
	 * 
	 * ViewIntimationRegisteredDetails intimationStatus = new
	 * ViewIntimationRegisteredDetails( a_intimationDto,
	 * intimation.getPolicy().getStatus() == null);
	 * UI.getCurrent().addWindow(intimationStatus);
	 * 
	 * 
	 * }
	 */

	public void getViewIntimation(String registrationBean) {

		final ViewTmpIntimation intimation = intimationService
				.searchbyIntimationNoFromViewIntimation(registrationBean);
		VerticalLayout vLayout = new VerticalLayout();
		
		if(intimation != null){
			viewIntimationDetails.init(intimation);
			vLayout.addComponent(viewIntimationDetails);
		} else {

			OMPIntimation ompintimation = ompIntimationService
					.searchbyIntimationNo(registrationBean);
			if (ompintimation != null) {
				ompViewIntimationDetails.init(ompintimation);
				vLayout.addComponent(ompViewIntimationDetails);
			}
		}
				
//		if (intimation != null) {
//			viewIntimationDetails.init(intimation);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Intimation Details");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(vLayout);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
//		}
	}
	
	public void getViewGalaxyIntimation(String registrationBean) {

		final GalaxyIntimation intimation = intimationService
				.searchbyGalaxyIntimationNo(registrationBean);
		VerticalLayout vLayout = new VerticalLayout();
		
		if(intimation != null){
			viewGalaxyIntimationDetails.init(intimation);
			vLayout.addComponent(viewGalaxyIntimationDetails);
		} else {

			OMPIntimation ompintimation = ompIntimationService
					.searchbyIntimationNo(registrationBean);
			if (ompintimation != null) {
				ompViewIntimationDetails.init(ompintimation);
				vLayout.addComponent(ompViewIntimationDetails);
			}
		}
				
//		if (intimation != null) {
//			viewIntimationDetails.init(intimation);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Intimation Details");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(vLayout);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
//		}
	}


	public void show(String intimationId) {
		this.intimationNo = intimationId;
	}

	public void getErrorMessage(String eMsg) {

		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	public void setBillDetailsTable(Long rodKey, Intimation intimation) {

		// rodKey = 5029l;

		Double sumInsured = null;
		NewIntimationDto intimationDto = intimationService
				.getIntimationDto(intimation);
		Long insuredId = intimationDto.getInsuredPatient().getInsuredId();
		Long policyKey = intimationDto.getPolicy().getKey();
		Long productId = intimationDto.getPolicy().getProduct().getKey();
		
		Reimbursement reimbursement = reimbursementService.getReimbursement(rodKey);

	    if(insuredId != null){
	    sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,"H");
	    }
	    Hospitals hospitalById = hospitalService.getHospitalById(intimation.getHospital());
	    Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
	    
	    if(reimbursement.getSectionCategory() != null){
	    	valuesMap = dbCalculationService.getHospitalizationDetails(productId, sumInsured,hospitalById.getCityClass().toString(), insuredId, intimation.getKey(),reimbursement.getSectionCategory(),"0");
	    }else{
	    	 valuesMap = dbCalculationService.getHospitalizationDetails(productId, sumInsured,hospitalById.getCityClass().toString(), insuredId, intimation.getKey(),0l,"0");
	    }
	   
	  	 

		
		List<RODDocumentSummary> rodDocumentSummaryList = billDetailsService.getBillDetailsByRodKey(rodKey);
		
		int sno=1;

		for (RODDocumentSummary rodDocumentSummary : rodDocumentSummaryList) {
			Long documentSummaryKey = rodDocumentSummary.getKey();

			List<RODBillDetails> rodBillDetails = billDetailsService
					.getBilldetailsByDocumentSummayKey(documentSummaryKey);

			if (rodBillDetails != null) {
				List<HospitalisationDTO> hospitalisationList = new ArrayList<HospitalisationDTO>();
				int i = 1;
				for (RODBillDetails rodBillDetails2 : rodBillDetails) {
					HospitalisationDTO dto = new HospitalisationDTO();
					if (i == 1) {
						dto.setSno(sno);
						dto.setRodNumber(rodDocumentSummary.getReimbursement()
								.getRodNumber());
						if (null != rodDocumentSummary.getFileType()) {
							dto.setFileType(rodDocumentSummary.getFileType()
									.getValue());
						}
						dto.setFileName(rodDocumentSummary.getFileName());
						dto.setBillNumber(rodDocumentSummary.getBillNumber());
						if (null != rodDocumentSummary.getBillDate()) {

							String formatDate = SHAUtils
									.formatDate(rodDocumentSummary
											.getBillDate());
							dto.setBillDate(formatDate);
						}
						dto.setNoOfItems(rodDocumentSummary.getNoOfItems());
						dto.setBillValue(rodDocumentSummary.getBillAmount());
						sno++;
					}
					dto.setItemNo(i);
					dto.setItemName(rodBillDetails2.getItemName());
					if (null != rodBillDetails2.getBillClassification()) {
						dto.setClassification(rodBillDetails2
								.getBillClassification().getValue());
					}
					if (null != rodBillDetails2.getBillCategory()) {
						dto.setCategory(rodBillDetails2.getBillCategory()
								.getValue());
					}
					
				
				if(null != rodBillDetails2.getNoOfDaysBills())
				{
					dto.setNoOfDays(rodBillDetails2.getNoOfDaysBills().longValue());
				}
					if (null != rodBillDetails2.getPerDayAmountBills()) {
						dto.setPerDayAmt(rodBillDetails2.getPerDayAmountBills()
								.longValue());
					}
					if (null != rodBillDetails2.getClaimedAmountBills()) {
						dto.setClaimedAmount(rodBillDetails2
								.getClaimedAmountBills().longValue());
					}
					if (null != rodBillDetails2.getNoOfDaysPolicy()) {
						dto.setEntitlementNoOfDays(rodBillDetails2
								.getNoOfDaysPolicy().longValue());
					}
					if (null != rodBillDetails2.getPerDayAmountPolicy()) {
						dto.setEntitlementPerDayAmt(rodBillDetails2
								.getPerDayAmountPolicy().longValue());
					}
					if (null != rodBillDetails2.getTotalAmount()) {
						dto.setAmount(rodBillDetails2.getTotalAmount()
								.longValue());
					}
					if (null != rodBillDetails2.getDeductibleAmount()) {
						dto.setDeductionNonPayableAmount(rodBillDetails2
								.getPayableAmount().longValue());
					}
					if (null != rodBillDetails2.getClaimedAmountBills()
							&& null != rodBillDetails2.getPayableAmount()) {
						Double claimedAmount = rodBillDetails2
								.getClaimedAmountBills();
						Double disAllowance = rodBillDetails2
								.getPayableAmount();
						Double netAmount = claimedAmount - disAllowance;
						dto.setPayableAmount(netAmount.longValue());
					}
					dto.setReason(rodBillDetails2.getNonPayableReason());
					dto.setMedicalRemarks(rodBillDetails2.getMedicalRemarks());
					if (null != rodBillDetails2.getIrdaLevel1Id()) {

						SelectValue irdaLevel1ValueByKey = masterService
								.getIRDALevel1ValueByKey(rodBillDetails2
										.getIrdaLevel1Id());
						if (irdaLevel1ValueByKey != null) {
							dto.setIrdaLevel1(irdaLevel1ValueByKey.getValue());
						}
					}
					if (null != rodBillDetails2.getIrdaLevel2Id()) {
						SelectValue irdaLevel2ValueByKey = masterService
								.getIRDALevel2ValueByKey(rodBillDetails2
										.getIrdaLevel2Id());
						if (irdaLevel2ValueByKey != null) {
							dto.setIrdaLevel2(irdaLevel2ValueByKey.getValue());
						}
					}
					if (null != rodBillDetails2.getIrdaLevel3Id()) {
						SelectValue irdaLevel3ValueByKey = masterService
								.getIRDALevel3ValueByKey(rodBillDetails2
										.getIrdaLevel3Id());
						if (irdaLevel3ValueByKey != null) {
							dto.setIrdaLevel3(irdaLevel3ValueByKey.getValue());
						}
					}

					hospitalisationList.add(dto);
					i++;
				}

				for (HospitalisationDTO hospitalisationDTO : hospitalisationList) {
					viewBillDetailsTableObj.addBeanToList(hospitalisationDTO);

				}
			}
		}
				
	}
	
	private PreauthDTO getProrataFlagFromProduct(PreauthDTO preauthDTO)
	{
		Product product = masterService.getProrataForProduct(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		if(null != product)
		{
			preauthDTO.setProrataDeductionFlag(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			preauthDTO.setProductBasedProRata(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			preauthDTO.setProductBasedPackage(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
			//ends.
			preauthDTO.setPackageAvailableFlag(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
			
			//added for CR GLX2020069 GMC prorata calculation
			if(product.getCode() != null && (product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_PRODUCT_CODE)|| product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE))){
				Double sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				MasRoomRentLimit gmcProrataFlag = intimationService.getMasRoomRentLimitbySuminsured(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),sumInsured);
			if(gmcProrataFlag != null && gmcProrataFlag.getProportionateFlag() != null){
					preauthDTO.setProrataDeductionFlag(null != gmcProrataFlag.getProportionateFlag() ? gmcProrataFlag.getProportionateFlag() : null);	
					preauthDTO.setProductBasedProRata(null != gmcProrataFlag.getProportionateFlag() ? gmcProrataFlag.getProportionateFlag() : null);
				}else {
					preauthDTO.setProrataDeductionFlag("N");	
					preauthDTO.setProductBasedProRata("N");
				}
			}
		}
		return preauthDTO;

	}
	
	public void getInsuredDetailsList(Policy policy) {
		try{
		
		
		List<Insured> insuredList = insuredService
				.getInsuredListByPolicyNo(String.valueOf(policy
						.getKey()));
		List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuranceInsuredDetailsList = new ArrayList<PreviousInsuranceInsuredDetailsTableDTO>();
		for (Insured insured : insuredList) {
			PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO = new PreviousInsuranceInsuredDetailsTableDTO();
			previousInsuranceInsuredDetailsTableDTO
					.setInsuredName(insured
							.getInsuredName() !=null ? insured
									.getInsuredName() :"");
			previousInsuranceInsuredDetailsTableDTO.setAge(insured.getInsuredAge());
			previousInsuranceInsuredDetailsTableDTO.setDOB(insured.getInsuredDateOfBirth());
			previousInsuranceInsuredDetailsTableDTO.setRelation(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
			previousInsuranceInsuredDetailsTableDTO.setSumInsured(insured.getInsuredSumInsured());
			previousInsuranceInsuredDetailsTableDTO.setSex(insured.getInsuredGender() != null ? insured.getInsuredGender().getValue() : " ");
			List<InsuredPedDetails> insuredPedDetails =insuredService.getInsuredKeyListByInsuredkey(insured.getInsuredId());
			String description="";
			for(InsuredPedDetails insuredPedDetail : insuredPedDetails){
				PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO1 =new PreviousInsuranceInsuredDetailsTableDTO();
				if(insuredPedDetail.getPedDescription() != null){
				 description += (String)insuredPedDetail.getPedDescription()+" ,";
				}
			}
			previousInsuranceInsuredDetailsTableDTO.setPedDescription(description);
			previousInsuranceInsuredDetailsList.add(previousInsuranceInsuredDetailsTableDTO);
		}
		ViewPreviousInsuranceInsuredDetails viewPreviousInsuranceInsuredDetailsInstance = viewPreviousInsuranceInsuredDetails
				.get();
		viewPreviousInsuranceInsuredDetailsInstance
				.showValues(previousInsuranceInsuredDetailsList);
		UI.getCurrent()
				.addWindow(
						viewPreviousInsuranceInsuredDetailsInstance);
		}catch(Exception e){
			/*Label successLabel = new Label("<b style = 'color: black;'>No record found!!! </b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("insured Details");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);*/
			
			/*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "insured Details");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("<b style = 'color: black;'>No record found!!! </b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
				
					
				}
			});
			
		}
}

	public void setAcknowledgementKey(Long docAcknowledgementKey) {
		
		this.acknowledgementKey = docAcknowledgementKey;
		
	}
	
	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}


	public void getEarlierHrmDetails(PreauthDTO bean,String intimationNo)
	{
		
		
		List<HRMHospitalDetailsTableDTO> hospitalDtoList = new ArrayList<HRMHospitalDetailsTableDTO>();	
		
		HRMHospitalDetailsTableDTO hospitalDto1 = new HRMHospitalDetailsTableDTO();
		hospitalDto1.setHardCodedString("Intimation Number");
		hospitalDto1.setHardCodedStringValue(bean.getNewIntimationDTO().getIntimationId());
		hospitalDto1.setHardCodedString1("Hoaspital Name");
		hospitalDto1.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getName());
		hospitalDtoList.add(hospitalDto1);
		HRMHospitalDetailsTableDTO hospitalDto2 = new HRMHospitalDetailsTableDTO();
		hospitalDto2.setHardCodedString("Hrm Id");
		hospitalDto2.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
		hospitalDto2.setHardCodedString1("Phone");
		hospitalDto2.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo());
		hospitalDtoList.add(hospitalDto2);
		HRMHospitalDetailsTableDTO hospitalDto3 = new HRMHospitalDetailsTableDTO();
		hospitalDto3.setHardCodedString("Name");
		hospitalDto3.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName());
		hospitalDto3.setHardCodedString1("Email Id");
		hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId());
		hospitalDtoList.add(hospitalDto3);	
		
		viewEarlierHrmTable.viewInt(hospitalDtoList);
		viewEarlierHrmTable.init("",false,false);
		viewEarlierHrmTable.initTable();
		
		List<HRMTableDTO> earilerHrmDetails = preAuthService.getEarlierHrmDetails(bean.getNewIntimationDTO().getIntimationId());
		List<DiagnosisDetailsTableDTO> diagnosisList =  bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		 
		for (HRMTableDTO hrmTableDTO : earilerHrmDetails) {
			
			String diagnosisName = "";
			
			 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				
				 diagnosisName += diagnosisDetailsTableDTO.getDiagnosis() + " ";
			}
			 
			 hrmTableDTO.setDiagnosis(diagnosisName);
		}
		 
		
		 
		viewEarilerHrmListenerTable.init();			
		viewEarilerHrmListenerTable.initTable();
	//	setTableValues(hrmtableDtoList);
		
		 
		setTableValuesForEarliarHrmDetails(earilerHrmDetails);
		VerticalLayout verticalLayout = new VerticalLayout();
    	verticalLayout.addComponents(viewEarlierHrmTable,viewEarilerHrmListenerTable);	
    	
    	Window popup = new com.vaadin.ui.Window();
    	popup.setWidth("75%");
    	popup.setHeight("90%");					
		popup.setContent(verticalLayout);
//    	popup.setContent();
    	popup.setClosable(true);
    	popup.center();
    	popup.setResizable(false);
    	popup.addCloseListener(new Window.CloseListener() {
    		
    		@Override
    		public void windowClose(CloseEvent e) {
    			System.out.println("Close listener called");
    		}
    	});

    	popup.setModal(true);
    	UI.getCurrent().addWindow(popup);


}
	
	
	private void setTableValuesForEarliarHrmDetails(List<HRMTableDTO> hrmtableDtoList)
	{
		
		if(null != viewEarilerHrmListenerTable)
	
			{
				if(null != hrmtableDtoList && !hrmtableDtoList.isEmpty())
				{
					for (HRMTableDTO hrmTableDto : hrmtableDtoList) {
						
						viewEarilerHrmListenerTable.addBeanToList(hrmTableDto);
					}
				}
			}
		}
	
	 public static List<String> getScreenName() {
		 List< String> scrNameList = new ArrayList<String>();
		 scrNameList.add("Process Pre-authorization");
		 scrNameList.add("Process Enhancement");
		 scrNameList.add("Process Claim Billing");
		 scrNameList.add("Process Claim Financials");
		 scrNameList.add("Process Claim Request (Zonal Medical Review)");
		 scrNameList.add("Process Claim Request");
		    
		 return scrNameList;
		 
	 }
	 
	 public void getDeductibles(String intimationNo) {


			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("800px");
			popup.setHeight("400px");
			popup.setCaption("View Deductibles");
			OMPIntimation intimation = ompIntimationService.searchbyIntimationNo(intimationNo);
			OMPClaim claim = ompclaimService.getClaimsByIntimationNumber(intimationNo);
			if(claim!= null){
				ompViewDeductiblesPageUI.init(claim);
			}
			popup.setContent(ompViewDeductiblesPageUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
<<<<<<< HEAD
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);

		
		 
	 }
	 public void getLegalTrails() {

			
			Window popup = new com.vaadin.ui.Window();
			// popup.setCaption("Pre-auth Details");
			popup.setWidth("75%");
			popup.setHeight("75%");
			Intimation intimationByKey = intimationService.getIntimationByNo(intimationNo);
			if(intimationByKey!=null){
				
			viewLegalHistoryRequest.init(intimationByKey);
			popup.setContent(viewLegalHistoryRequest);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);


				popup.addCloseListener(new Window.CloseListener() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
	
	}
	 
	public void getViewPolicySchedule(String intimationNo) {
		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		OPIntimation opintimation = intimationService.getOPIntimationByNo(intimationNo);

		if (intimation != null && intimation.getPolicy() != null
				&& intimation.getPolicy().getPolicyNumber() != null) {

			PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
					.fetchPolicyScheduleFromPremia(intimation.getPolicy()
							.getPolicyNumber(), 0);
			if (fetchPolicyScheduleFromPremia != null
					&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleFromPremia.getResultUrl();
				// getUI().getPage().open(url, "_blank");
				getUI().getPage().open(url, "_blank", 1550, 650,
						BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}

		} else 	if (opintimation != null && opintimation.getPolicy() != null
				&& opintimation.getPolicy().getPolicyNumber() != null) {

			PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
					.fetchPolicyScheduleFromPremia(opintimation.getPolicy()
							.getPolicyNumber(), 0);
			if (fetchPolicyScheduleFromPremia != null
					&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleFromPremia.getResultUrl();
				// getUI().getPage().open(url, "_blank");
				getUI().getPage().open(url, "_blank", 1550, 650,
						BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}

	}else{
			getErrorMessage("Policy Not Available");
		}

	}
	
	public void getViewPolicyScheduleByPolicyNumber(String policyNumber) {


			PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
					.fetchPolicyScheduleFromPremia(policyNumber, 0);
			if (fetchPolicyScheduleFromPremia != null
					&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleFromPremia.getResultUrl();
				// getUI().getPage().open(url, "_blank");
				getUI().getPage().open(url, "_blank", 1550, 650,
						BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}

		

	}
	
	 public Button getElearnButton(){
		eLearnBtn = new Button("E-Learning");
		eLearnBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				List<ELearnDto> eContentList = masterService.getELearnContentList();
				Window eLeanrWindow = new Window();
				eLeanrWindow.setWidth("35%");
				eLeanrWindow.setHeight("55%");
				eLeanrWindow.center();
				eLeanrWindow.setClosable(false);
				eLeanrWindow.setResizable(false);
				eLearnPage.initView(eContentList,eLeanrWindow);
				eLeanrWindow.setContent(eLearnPage);
				UI.getCurrent().addWindow(eLeanrWindow);				
			}
		});
		 return eLearnBtn; 
	 }
	 
	 public void getViewStopLossDetails(String intimationNo){
			

		   Intimation intimation = intimationService
					.searchbyIntimationNo(intimationNo);
		    viewStopLossGmcPage.init(intimation,this.rodKey);
			Window popup = new com.vaadin.ui.Window();
			 popup.setCaption("Stop Loss Details");
			popup.setWidth("50%");
			popup.setHeight("45%");
			popup.setContent(viewStopLossGmcPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
		}
	 
	 public void getviewClaimantDetails(String intimationNo) {

			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("800px");
			popup.setHeight("280px");
			popup.setCaption("View Claimant Details");
			OMPIntimation intimation = ompIntimationService
					.searchbyIntimationNo(intimationNo);
//			viewClaimantDetailsUI.init(intimation);
			popup.setContent(viewClaimantDetailsUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);

		}
	 
	 public void getViewCorpBufferDetails(String intimationNo){
			

		   Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);	   
		   Claim claimforIntimation = intimationService.getClaimforIntimation(intimation.getKey());
		   Long mainNo = 0L;
			if(intimation.getInsured().getDependentRiskId() !=null){
				mainNo = intimation.getInsured().getDependentRiskId();
			}else{
				mainNo = intimation.getInsured().getInsuredId();
			}
		   PreauthDTO preauthDTO = new PreauthDTO();
		   ClaimDto claimDto =new ClaimDto();
		   
		   Map<String, Double> values = null;
		   
		   if(claimforIntimation != null){
			   values = dbCalculationService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_CB,intimation.getPolicy().getPolicyNumber(),intimation.getInsured().getKey(),mainNo,claimforIntimation.getKey());
			   claimDto.setGmcCorpBufferLmt(claimforIntimation.getGmcCorpBufferLmt().intValue());
		   }

			if(values != null && !values.isEmpty()){
				if(values.get(SHAConstants.LN_POLICY_BUFFER_SI) != null){
					preauthDTO.getPreauthDataExtractionDetails().setCorpBufferSI(values.get(SHAConstants.LN_POLICY_BUFFER_SI));	
				}
				if(values.get(SHAConstants.LN_BUFFER_UTILISED_AMT) != null){
					preauthDTO.getPreauthDataExtractionDetails().setCorpBufferUtilisedAmt(values.get(SHAConstants.LN_BUFFER_UTILISED_AMT));	
				}
				if(values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) != null){
					preauthDTO.getPreauthDataExtractionDetails().setCorpBufferLimit(values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));	
				}
				if(values.get(SHAConstants.LN_INSURED_UTIL_AMT) != null &&
						values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) !=null){
					Double avlbBalance = values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) - values.get(SHAConstants.LN_INSURED_UTIL_AMT);
					preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAvalBal(avlbBalance);	
				}
				
			}
		   
			preauthDTO.setClaimDTO(claimDto);
		   
		   corporateBufferPage.bindFieldGroup(preauthDTO, true);
			Window popup = new com.vaadin.ui.Window();
			 popup.setCaption("corporate Buffer Details");
			popup.setWidth("45%");
			popup.setHeight("45%");
			popup.setContent(corporateBufferPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
		}
	 
	 
	 public void getViewCorporateBufferUtilisationDetails(String intimationNo){
		  	   
		 	Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);		  
			corporateBufferUtilisationPage.init(intimation);			
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Corporate Buffer");
			popup.setWidth("90%");
			popup.setHeight("70%");
			popup.setContent(corporateBufferUtilisationPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**

				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
		}
	 
	 
	  
	 private void getGMCProductBenefitTableValues(Long policyKey)
	 {		  

		 	viewProductBenefitsTable.init("", false, false);
		 	setBenefitTableValues(policyKey);		 	
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Product Benefits");
			popup.setWidth("90%");
			popup.setHeight("70%");
			popup.setContent(viewProductBenefitsTable);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
 }

	 public void getViewGmcExclusionDetails(String intimationNo) {

		 
		Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
		
		 if(intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
			
			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("75%");
			
			List<ViewGMCExclusionsWaiverTableDto> newList = new ArrayList<ViewGMCExclusionsWaiverTableDto>();
			
			List<PolicyRiskCover> list = policyService.getRiskCoverByPolicy(intimation.getPolicy().getKey());
			
			if(list != null){
				ViewGMCExclusionsWaiverTableDto exclusionWaiverDto = null;
				for (PolicyRiskCover policyRiskCover : list) {
					exclusionWaiverDto = new ViewGMCExclusionsWaiverTableDto();
					exclusionWaiverDto.setCoverCode(policyRiskCover.getCoverCode());
					exclusionWaiverDto.setCoverDesc(policyRiskCover.getCoverCodeDescription());
					exclusionWaiverDto.setSumInsured(policyRiskCover.getSumInsured().longValue());
					
					newList.add(exclusionWaiverDto);
				}
			}
			
			viewGMCExclusionsWaiverTableObj.init("", false, false);
			viewGMCExclusionsWaiverTableObj.setTableList(newList);
			popup.setContent(viewGMCExclusionsWaiverTableObj);
			// popup.setContent();
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */

				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		 }else{
				getErrorMessage("Exclusions and Waiver is not applicable for this Product");
			}

		}
	 
//		public void getViewOmpIntimation(String registrationBean) {
//
//			final OMPIntimation intimation = ompIntimationService
//					.searchbyIntimationNo(registrationBean);
//			if (intimation != null) {
//				ompViewIntimationDetails.init(intimation);
//				popup = new com.vaadin.ui.Window();
//				popup.setCaption("View Intimation Details");
//				popup.setWidth("75%");
//				popup.setHeight("75%");
//				popup.setContent(ompViewIntimationDetails);
//				popup.setClosable(true);
//				popup.center();
//				popup.setResizable(false);
//				popup.addCloseListener(new Window.CloseListener() {
//					/**
//				 * 
//				 */
//					private static final long serialVersionUID = 1L;
//

	 
	 
	 public void setBenefitTableValues(Long policyKey)
	 {
		 List<GpaBenefitDetails> gpaDetails = createRodService.getGpaBenefitDetails(policyKey);
		 List<ViewProductBenefitsTableDTO> finalTableList = new ArrayList<ViewProductBenefitsTableDTO>();
		 if(null != gpaDetails){
			 
			 ViewProductBenefitsTableDTO productBenefitDetails = null;
			 
			 for (GpaBenefitDetails gpaBenefitDetails : gpaDetails) {
				
				 productBenefitDetails = new ViewProductBenefitsTableDTO();
				 
				 productBenefitDetails.setConditionCode(gpaBenefitDetails.getBenefitCode());
				 productBenefitDetails.setDescription(gpaBenefitDetails.getBenefitDescription());
				 productBenefitDetails.setLongDescription(gpaBenefitDetails.getBenefitLongDescription());
				 
				 finalTableList.add(productBenefitDetails);
			}
			 
			 viewProductBenefitsTable.setTableList(finalTableList);
		 }
				 
	 }

	 public void getViewRTAsumInsured(Long policyKey, Long insuredKey, Long claimKey) {

			
			Window popup = new com.vaadin.ui.Window();
			// popup.setCaption("Pre-auth Details");
			popup.setWidth("30%");
			popup.setHeight("30%");
			Intimation intimationByKey = intimationService.getIntimationByNo(intimationNo);
			if(intimationByKey!=null){
				
			viewRTAsumInsuredUI.init(policyKey,insuredKey,claimKey);
			popup.setContent(viewRTAsumInsuredUI);
			popup.setCaption("View RTA Sum Insured");
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);


				popup.addCloseListener(new Window.CloseListener() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
	
	}
	 
	 public void getRevisedQueryDetailsTable(String intimationNo) {

			Intimation intimation = intimationService
					.searchbyIntimationNo(intimationNo);
			Claim claim = null;		
			if (intimation != null) {
				claim = claimService.getClaimforIntimation(intimation.getKey());
				
				if(claim != null){
					
					queryDetailsTableObj.init("View Query Details", false, false);
					queryDetailsTableObj.setViewQueryDetailsColumn();
					
					if(intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P"))
				    {
						queryDetailsTableObj.setViewPAQueryDetialsColumn();
				    }
					
				
					List<Reimbursement> reimbList = reimbursementService
							.findReimbursementByClaimKey(claim.getKey());
					if (reimbList != null && !reimbList.isEmpty()) {
						
						for (Reimbursement reimbursement : reimbList) {
							
							setQueryValues(reimbursement.getKey(),claim);
						}
					}
				
			}		
		
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					popup.setContent(queryDetailsTableObj);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
					 * 
					 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							queryDetailsTableObj.clearQueryDetailsPopup();
							System.out.println("Close listener called Query Details");
						}
					});
		
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			 }else if(this.intimationNo != null){
					
					preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
					
					setQueryDetailsForPreauth(this.intimationNo);
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					popup.setContent(preAuthPreviousQueryDetailsTable);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
				
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							preAuthPreviousQueryDetailsTable.clearPreviousQueryDetailsPopup();
							System.out.println("Close listener called Prev Query Details");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				 }
				 else{
						getErrorMessage("Reimbursement Query is not Available");
					}

			}


public void getViewEndorsementDetails(String intimationNo) {
			
//			if(rodKey != null && ! rodKey.equals(0l)){

			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("90%");

			ompViewEndorsementDetails.init("", false, false);
//			ompViewEndorsementDetails.setReferenceData(referenceData);
			OMPIntimation intimation = ompIntimationService
					.searchbyIntimationNo(intimationNo);
//			setBillDetailsTable(rodKey, intimation);
			popup.setContent(ompViewEndorsementDetails);
			// popup.setContent();
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
	    		 * 
	    		 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
//			}else{
//				getErrorMessage("Bill Details is not applicable");
//			}

		}

	public void getViewUnnamedRiskDetails(String intimationNo) {
	
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("800px");
		popup.setHeight("280px");
		popup.setCaption("Risk Details");
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		unnamedRiskDetailsUI.init(intimation.getKey());
		popup.setContent(unnamedRiskDetailsUI);
		popup.setClosable(true);
		popup.center();
		popup.setCaption("UNNAMED RISK DETAILS");
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;
	
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
	
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	
	 public void getviewOtherCurrencyRateDetails(String intimationNo,Long rodKey) {

			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("800px");
			popup.setHeight("280px");
			popup.setCaption("View Other Currency Rate History");
			OMPClaim claim = ompclaimService
					.getClaimsByIntimationNumber(intimationNo);
			ompViewOtherCurrencyRateUI.init(claim,rodKey);
			popup.setContent(ompViewOtherCurrencyRateUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);

		}
		
		
		public Component getViewOmpBalanceSI(String intimationNo) {
//			ViewBalenceSumInsured a_viewBalenceSumInsured = viewBalenceSumInsured
//					.get();
			OMPClaim ompclm = ompclaimService.getClaimsByIntimationNumber(intimationNo);
			Long clmKey = ompclm != null ? ompclm.getKey() : 0l;
			ompBalanceSIForm.bindFieldGroup(intimationNo, clmKey,rodKey);
//			UI.getCurrent().addWindow(a_viewBalenceSumInsured);
//			popup = new com.vaadin.ui.Window();
//			popup.setCaption("Balance Sum Insured");
//			popup.setWidth("90%");
//			popup.setHeight("90%");
//			popup.setContent(ompBalanceSIForm);
//			popup.setClosable(true);
//			popup.center();
//			popup.setResizable(true);
//			popup.addCloseListener(new Window.CloseListener() {
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void windowClose(CloseEvent e) {
//					System.out.println("Close listener called");
//				}
//			});
//
//			popup.setModal(true);
//			UI.getCurrent().addWindow(popup);
			
			return ompBalanceSIForm;
		}
		
		public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
				MasterService masterService) {
			//super("View Policy Details");
			this.policyService = a_policyService;
			this.policy = policy;
			this.masterService = masterService;
			


		}
		public void getViewOmpRiskDetails(Policy policyNo,String intimationNo) {
			Window popup =null;
			popup = new com.vaadin.ui.Window();
			popup.setWidth("800px");
			popup.setHeight("280px");
			popup.setCaption("Risk Details");
			OMPIntimation intimation = ompIntimationService
					.searchbyIntimationNo(intimationNo);
			Policy policy = policyNo;
			Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
			ompRiskDetailsUI.init(apolicy,intimation);
			popup.setContent(ompRiskDetailsUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);

		}
		
		public void getViewOmpPreviousClaimDetails(String intimationNo){
			if(intimationNo!= null){
				List<OMPPreviousClaimTableDTO> previousClaimList = null;
				OMPIntimation intimation = ompIntimationService
						.searchbyIntimationNo(intimationNo);
			NewIntimationDto	newIntimationDto = ompIntimationService.getIntimationDto(intimation);
			previousClaimList = ompIntimationService.getPreviousClaimByForRegistration(newIntimationDto);
			if (null != previousClaimList && !previousClaimList.isEmpty()) {
				previousClaimsRgistration.init("", false, false);
				previousClaimsRgistration.setTableList(previousClaimList);
			}
//				viewDetails.getViewPolicy(dtoBean.getIntimationno());
			
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Previous Claims");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(previousClaimsRgistration);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
			
	}
		
//		public void getViewOmpRiskDetails(String intimationNo) {
//
//			popup = new com.vaadin.ui.Window();
//			popup.setWidth("800px");
//			popup.setHeight("280px");
//			popup.setCaption("Risk Details");
//			OMPIntimation intimation = ompIntimationService
//					.searchbyIntimationNo(intimationNo);
//			ompRiskDetailsUI.init(intimation);
//			popup.setContent(ompRiskDetailsUI);
//			popup.setClosable(true);
//			popup.center();
//			popup.setResizable(false);
//			popup.addCloseListener(new Window.CloseListener() {
//				/**
//			 * 
//			 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void windowClose(CloseEvent e) {
//					System.out.println("Close listener called");
//				}
//			});
//
//			popup.setModal(true);
//			UI.getCurrent().addWindow(popup);
//
//		}
		
		public void getViewCashlessDocument(String intimationNo ) {
			
			/**
			 * Release Number : R3
			 * Requirement Number:R0725
			 * Modified By : Durga Rao
			 * Modified On : 15th May 2017
			 *
			 */
			final Intimation intimation = intimationService
					.searchbyIntimationNo(intimationNo);
			Long dummyno = 1l;
			String dummystrin = "";
			if(intimation != null && intimationNo != null){
				BPMClientContext bpmClientContext = new BPMClientContext();
				Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNo);
				 tokenInputs.put("cashlessDoc", "Yes");
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyviewCashless() + intimationNoToken; 
				/*Below Code commneted for Security Reason
				String url = bpmClientContext.getGalaxyviewCashless() + intimationNo + "&&dummystrin?" + dummyno;*/
			//	getUI().getPage().open(url, "_blank");
				getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
			
				} else {
					getErrorMessage("Cashless Document Not Available");
				}
			
			}
	 
		 @Inject
		 private ViewLumenTrialsPage viewlumenTrailsPage;
		 
		 private Long lumenKey;
		 
		 public Long getLumenKey() {
			return lumenKey;
		}

		public void setLumenKey(Long lumenKey) {
			this.lumenKey = lumenKey;
		}

		public void getLumenTrails() {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Lumen Details");
			popup.setWidth("75%");
			popup.setHeight("75%");
			//Since lumenKey alone need to be used to fetch data hence passing lumenKey
			viewlumenTrailsPage.loadData(getLumenKey());
			popup.setContent(viewlumenTrailsPage);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);

		}

		public void getViewjetPriviledgePolicyDetails(String intimationNo) {
			final Intimation intimation = intimationService
					.searchbyIntimationNo(intimationNo);

			if (intimation != null && intimation.getPolicy() != null
					&& intimation.getPolicy().getPolicyNumber() != null) {

				PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
						.fetchPolicyScheduleFromPremia(intimation.getPolicy()
								.getPolicyNumber(), 0);
				if (fetchPolicyScheduleFromPremia != null
						&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
					String url = fetchPolicyScheduleFromPremia.getResultUrl();
					// getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank", 1550, 650,
							BorderStyle.NONE);
				} else {
					getErrorMessage("Policy Schedule Not Available");
				}

			}else{
				getErrorMessage("Policy Not Available");
			}

		}

		public DMSDocumentViewDetailsPage getDmsDocumentDetailsViewPage() {
			return dmsDocumentDetailsViewPage;
		}
		
		public void getBillingInternalRemarksTrials(Long intimationKey, Long stageKey) {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Billing Internal Remarks");
			popup.setWidth("75%");
			popup.setHeight("75%");
			
			// Get the list of billing internal remarks
			billingInternalRemarksPage.loadData(intimationKey, stageKey);
			popup.setContent(billingInternalRemarksPage);
			
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		
		public void getFaInternalRemarksTrials(Long intimationKey, Long stageKey) {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("FA Internal Remarks");
			popup.setWidth("75%");
			popup.setHeight("75%");
			
			// Get the list of FA internal remarks
			faInternalRemarksPage.loadData(intimationKey, stageKey);
			popup.setContent(faInternalRemarksPage);
			
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		
	public String getOMPMERDetails(String intimationNo) {

		OMPIntimation ompIntimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);

		String strPolSysId = null;
		String finalUrl = null;
		String url = null;
		String strInsuredSysId = null;
		

		//Bancs Changes Start
				Intimation intimationByNo = intimationService.getIntimationByNo(intimationNo);

				Policy policyObj = null;
				Builder builder = null;
				String strMERReportURL = null;
				
				if(intimationByNo != null){
					if(intimationByNo.getPolicy() != null){
						policyObj = policyService.getByPolicyNumber(intimationByNo.getPolicy().getPolicyNumber() );
						if (policyObj != null) {
							if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
								strMERReportURL = BPMClientContext.BANCS_MER_REPORT_URL;
							}else{
								strMERReportURL = BPMClientContext.MER_REPORT_URL;
							}
						}
					}
					
				}
				
				//Bancs Changes End
		//String strMERReportURL = BPMClientContext.MER_REPORT_URL;

		if (ompIntimation != null) {
			strMERReportURL = BPMClientContext.MER_REPORT_URL;
			Long polSysId = ompIntimation.getPolicy().getPolicySystemId();
			if (null != polSysId)
				strPolSysId = String.valueOf(polSysId);

			Long insuredSysId = ompIntimation.getInsured().getInsuredId();
			if (null != insuredSysId)
				strInsuredSysId = String.valueOf(insuredSysId);

			if (null != strPolSysId)
				url = strMERReportURL.replace("|", strPolSysId);
			if (null != strInsuredSysId)
				finalUrl = url.replace(",", strInsuredSysId);

		}
		return finalUrl;

	}
	
	/**
	 * Get activity log details 
	 * @param intimationNo
	 */
	public void getActivityLog(String intimationNo) {		
		ImsUser imsUser = null;
		if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
			imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
			
			activityLogWindow.init(masterService, activityLogService, intimationNo, imsUser.getUserName().toUpperCase(), imsUser.getEmpName());
			activityLogWindow.initView();
			UI.getCurrent().addWindow(activityLogWindow);
		}
	}
	
	private EnhancedBrowserWindowOpener sopener;
	
	public EnhancedBrowserWindowOpener getSopener() {
		return sopener;
	}

	public void setSopener(EnhancedBrowserWindowOpener sopener) {
		this.sopener = sopener;
	}

	@SuppressWarnings("serial")
	public ShortcutListener callPreviousClaimsDetailsViewSListener(){
		ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM2, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
			@Override
			public void handleAction(Object sender, Object target) {
				viewDetailsSelect.setValue(VIEW_PREVIOUS_CLAIM_DETAILS);
				if(viewDetailsSelect.getValue() != null){
					viewDetailsSelect.select(viewDetailsSelect.getValue());
					btnGo.click();
				}
			}
		};
		getActionManager().addAction(shortcutListener);
		return shortcutListener;
	}
	
	public void getICRDetailsbyPolicyNumber(String policyNumber) {
		Window popup = new Window("ICR Details");
		
		popup.setHeight("250px");
		popup.setWidth("500px");

		Policy policyObject = policyService
				.getPolicyByPolicyNubember(policyNumber);

		if(policyObject != null && policyObject.getProduct() != null 
				&& policyObject.getProduct().getKey() != null 
				&& ReferenceTable.getGMCProductList().containsKey(policyObject.getProduct().getKey())){

			PolicywiseClaimReportDto  icrDetailsDto = clmreportService.getICRDetailsByPolicyNumber(policyNumber);
			// Get the list of ICR Details
			viewICRDetailsFrm.initView(icrDetailsDto);
			popup.setContent(viewICRDetailsFrm);
			
		}
		else{
			Label notApplicableLbl = new Label("<B>ICR Details Not Applicable for this Policy.</B>",ContentMode.HTML);
			HorizontalLayout hlayout = new HorizontalLayout(notApplicableLbl);
//			hlayout.setWidth("100%");
			hlayout.setComponentAlignment(notApplicableLbl, Alignment.MIDDLE_CENTER);
			hlayout.setMargin(true);
			VerticalLayout vlayout = new VerticalLayout(hlayout);
			vlayout.setHeight("100%");
			vlayout.setComponentAlignment(hlayout, Alignment.MIDDLE_CENTER);
			
			popup.setContent(vlayout);
		}
					
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	public void getViewRepliedRAW(String intimationNo){
		
				viewRawRequestPage.init(intimationNo);
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View RAW Request");
				popup.setWidth("90%");
				popup.setHeight("90%");
				Panel panel = new Panel();
				panel.setContent(viewRawRequestPage);
				popup.setContent(panel);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
	 
		}
	
	
	public void getCumulativeBonusView(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		if(intimation != null){
		String strPolicyNo = intimation.getPolicy().getPolicyNumber();
		
		getCumulativeBonusViewtByPolicyNo(strPolicyNo,intimation);
		}else{
			OMPIntimation ompIntimation = ompIntimationService.searchbyIntimationNo(intimationNo);
			if(ompIntimation != null){
				String strPolicyNo = ompIntimation.getPolicy().getPolicyNumber();
				
				getCumulativeBonusViewtByPolicyNo(strPolicyNo);
			}
		}
	}
	
	
public void getCumulativeBonusViewtByPolicyNo(String strPolicyNo) { 
	
	Policy intimationObject = intimationService.getPolicyByPolicyNubember(strPolicyNo);
				
	
	if(strPolicyNo!=null && intimationObject.getProduct().getCode()!=null && 
			(ReferenceTable.getCumulativeProductBonusList().contains(intimationObject.getProduct().getCode()))) {
		
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		String strDmsViewURL = null;
		
		if(strPolicyNo != null){
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
					/*strDmsViewURL = BPMClientContext.BANCS_CUMULATIVE_BONUS_URL;*/
					getErrorMessage("Bonus Logic Details Not Applicable For Release 1.0");
				}else{
					strDmsViewURL = BPMClientContext.CUMULATIVE_BONUS_URL;
					strDmsViewURL = strDmsViewURL.concat(strPolicyNo);
					BrowserFrame browserFrame = new BrowserFrame("",
						    new ExternalResource(strDmsViewURL!=null?strDmsViewURL:""));
					
					
					browserFrame.setHeight("600px");
					browserFrame.setWidth("200%");
					Button btnSubmit = new Button("OK");
					
					btnSubmit.setCaption("CLOSE");
//					//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
					btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//					btnSubmit.setWidth("-1px");
//					btnSubmit.setHeight("-10px");
//					btnSubmit.setDisableOnClick(true);
//					//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
					VerticalLayout vLayout = new VerticalLayout(browserFrame,btnSubmit);
					vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
					vLayout.setSizeFull();
					final Window popup = new com.vaadin.ui.Window();
					
					popup.setCaption("");
					popup.setWidth("100%");
					popup.setHeight("100%");
					//popup.setSizeFull();
					popup.setContent(vLayout);
					
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					
					btnSubmit.addClickListener(new Button.ClickListener() {
						
						private static final long serialVersionUID = 1L;
				
						@Override
						public void buttonClick(ClickEvent event) {

							popup.close();
								
						}
						
					});
					popup.addCloseListener(new Window.CloseListener() {
						
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
					popup.setModal(true);
					UI.getCurrent().addWindow(popup); 
				}
			}
		}
		
		
		//Bancs Changes End
		
		//String strDmsViewURL = BPMClientContext.CUMULATIVE_BONUS_URL;
		
		}else {
		getErrorMessage("Bonus Logic Details Not Applicable");
		}
	}
			
	public Button getLinkedPolicyDetails(final PreauthDTO preauthDto) {
		Button viewLinkedPolicy = new Button("View Linked PolicyDetails");
		
		viewLinkedPolicy.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Window linkedPolicyWindow = new Window();
				linkedPolicyWindow.setCaption("Linked PolicyDetails");
				linkedPolicyWindow.setWidth("50%");
				linkedPolicyWindow.setHeight("45%");
				linkedPolicyWindow.center();
				linkedPolicyWindow.setClosable(true);
				linkedPolicyWindow.setResizable(true);
				linkedPolicyDtlsPage.init(preauthDto,linkedPolicyWindow);
				linkedPolicyWindow.setContent(linkedPolicyDtlsPage);
				UI.getCurrent().addWindow(linkedPolicyWindow);				
			}
		});
		 return viewLinkedPolicy; 
		
	}
	
	public void showViewLinkedPolicy(final ViewClaimStatusDTO preauthDto) {
		
		//Intimation intimationDetails = createRodService.getIntimationByNo(preauthDto.getIntimationId());
		Intimation intimationDetails = preAuthService.getIntimationByNo(preauthDto.getIntimationId());
	
		Insured insuredDetails = insuredService.getInsuredByInsuredKey(intimationDetails.getInsured().getKey());
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, String> linkPolicyDetails = dbCalculationService.getLinkedPolicyDetails(preauthDto.getPolicyNumber(), insuredDetails.getLinkEmpNumber());
		
		TextField corporateName = new TextField();
		corporateName.setCaption("Name of the Corporate");
		corporateName.setValue(linkPolicyDetails.get(SHAConstants.PROPOSER_NAME));
		corporateName.setWidth("100%");
		corporateName.setReadOnly(true);
		corporateName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		corporateName.setNullRepresentation("-");
		
		TextField insuredName = new TextField();
		insuredName.setCaption("Name of the Insured");
		insuredName.setValue(insuredDetails.getInsuredName());
		insuredName.setWidth("100%");
		insuredName.setReadOnly(true);
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		insuredName.setNullRepresentation("-");
		
		TextField policyNumber = new TextField();
		policyNumber.setCaption("Policy Number");
		policyNumber.setValue(linkPolicyDetails.get(SHAConstants.POLICY_NUMBER));
		policyNumber.setWidth("100%");
		policyNumber.setReadOnly(true);
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		policyNumber.setNullRepresentation("-");
		
		TextField mainMemberName = new TextField();
		mainMemberName.setCaption("Name of Main Member");
		mainMemberName.setValue(linkPolicyDetails.get(SHAConstants.INSURED_NAME));
		mainMemberName.setWidth("100%");
		mainMemberName.setReadOnly(true);
		mainMemberName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		mainMemberName.setNullRepresentation("-");
		
		TextField mainMemberId = new TextField();
		mainMemberId.setCaption("Main Member ID");
		mainMemberId.setValue(linkPolicyDetails.get(SHAConstants.EMPLOYEE_ID));
		mainMemberId.setWidth("100%");
		mainMemberId.setReadOnly(true);
		mainMemberId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		mainMemberId.setNullRepresentation("-");
		
		FormLayout linkedPolicyDetails = new FormLayout(corporateName,insuredName,policyNumber,mainMemberName,mainMemberId);
		linkedPolicyDetails.setSpacing(true);
		linkedPolicyDetails.setMargin(true);
		linkedPolicyDetails.setStyleName("layoutDesign");
		
		Button btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
			}
		});
		
		HorizontalLayout closebutLayout = new HorizontalLayout(btnClose);
		closebutLayout.setSizeFull();
		closebutLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
		
		VerticalLayout vLayout = new VerticalLayout(linkedPolicyDetails,closebutLayout);
		vLayout.setSpacing(true);
		
		popup = new com.vaadin.ui.Window();
		popup.setCaption("View Linked Policy");
		popup.setWidth("50%");
		popup.setHeight("50%");
//		popup.setStyleName("layoutDesign");
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public Button viewNegotiationDetails(final Long intimationKey){
		Button viewNegotiation = new Button("Negotiation");
		
		viewNegotiation.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Window viewNegWindow = new Window();
				viewNegWindow.setCaption("View Negotiation Details");
				viewNegWindow.setWidth("70%");
				viewNegWindow.setHeight("55%");
				viewNegWindow.center();
				viewNegWindow.setClosable(true);
				viewNegWindow.setResizable(true);
				viewNegotiationDetailsPage.init(intimationKey);
				viewNegWindow.setContent(viewNegotiationDetailsPage);
				UI.getCurrent().addWindow(viewNegWindow);				
			}
		});
		 return viewNegotiation; 
	}
	public void viewBillAssessmentVersionDetails(String intimationNo, String rodNo) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		
		if((this.rodKey == null || rodNo == null || (rodNo != null && rodNo.isEmpty())) && intimationNo != null) {
			rodNo = "%"+intimationNo+"%";
		}
		if(isOutpatient && rodNo != null && !rodNo.isEmpty()){
			String docType = SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.toLowerCase(); 
			String docTypeScrc = SHAConstants.BILLASSESSMENTSHEETSCRC.toLowerCase(); 
			String docSource = SHAConstants.FINANCIAL_APPROVER.toLowerCase();

			DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
			dmsDTO.setIntimationNo(intimationNo);
			OPClaim claim = claimService.getOPClaimByIntimationNo(intimationNo);
			dmsDTO.setClaimNo(claim.getClaimId());

			/*if(ReferenceTable.PA_LOB_KEY.equals(claim.getLobId())) {
				docTypeScrc = SHAConstants.PA_BILLASSESSMENTSHEET.toLowerCase();
			}*/			

			List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
					.getDocumentDetailsByIntimationNo(intimationNo);

			if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
				dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
			}

			dmsDocumentDetailsViewPage.init(dmsDTO, popup);
			dmsDocumentDetailsViewPage.getContent();
			popup.setContent(dmsDocumentDetailsViewPage);
		}else if(!isOutpatient && rodNo != null && !rodNo.isEmpty()){
			
			String docType = SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.toLowerCase(); 
			String docTypeScrc = SHAConstants.BILLASSESSMENTSHEETSCRC.toLowerCase(); 
			String docSource = SHAConstants.FINANCIAL_APPROVER.toLowerCase();
			
			DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
			dmsDTO.setIntimationNo(intimationNo);
			Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
			dmsDTO.setClaimNo(claim.getClaimId());
			
			if(ReferenceTable.PA_LOB_KEY.equals(claim.getLobId())) {
				docTypeScrc = SHAConstants.PA_BILLASSESSMENTSHEET.toLowerCase();
			}			
			
			List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
					.getBillAssessmentDocumentsVersionsByRod(rodNo,docType,docTypeScrc,docSource);
			
			if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
				dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
			}
			
			dmsDocumentDetailsViewPage.init(dmsDTO, popup);
			dmsDocumentDetailsViewPage.getContent();
			popup.setContent(dmsDocumentDetailsViewPage);
		}
		else{
			Label notApplicableLbl = new Label("<B>View Not Applicable</B>",ContentMode.HTML);
			HorizontalLayout hlayout = new HorizontalLayout(notApplicableLbl);
			hlayout.setComponentAlignment(notApplicableLbl, Alignment.MIDDLE_CENTER);
			hlayout.setMargin(true);
			VerticalLayout vlayout = new VerticalLayout(hlayout);
			vlayout.setHeight("100%");
			vlayout.setComponentAlignment(hlayout, Alignment.MIDDLE_CENTER);
			
			popup.setContent(vlayout);
		}
		popup.setClosable(true); 
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean, ViewTmpOPClaim claim) {

		try {
			
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = reimbursementService
						.getRimbursementForPayment(claim.getClaimId());
			}

			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
			bean.setAccountName(reimbursementForPayment.getAccountNumber());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setChequeDate(formatDate);
				}
			}
			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	
public void viewAuditClaimTrails(String intimationNo, boolean clmAuditUser) {
		
		if(intimationNo != null){
		Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		
		Boolean result = true;
		
		if (intimation != null) {

		result = viewClaimHistoryRequest.showClaimAuditHistory(intimation, clmAuditUser);
			
			
			if(result){
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setContent(viewClaimHistoryRequest);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							viewClaimHistoryRequest.clearClaimAuditHistoryPopup();
							System.out.println("Close listener called Clm Audit Trail");
						}
					});
		
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					popup = null;
			}else{
					getErrorMessage("Claim is not available");
			}
		 } else{
				getErrorMessage("History is not available");
			}
		
		}else{
			getErrorMessage("History is not available");
		}
	}
	
	public void getContinuityBenefitDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		contBenefitDtls.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Continuity benefit");
		/*popup.setWidth("75%");
		popup.setHeight("75%");*/
		popup.setWidth("50%");
		popup.setHeight("50%");
		popup.setContent(contBenefitDtls);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getSeriousDeficiencyByHospitalCode(String hospitalCode) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewSeriousDeficiencyUI.init(hospitalCode);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Serious Deficiency Intimation List");
		popup.setWidth("50%");
		popup.setHeight("50%");
		popup.setContent(viewSeriousDeficiencyUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	
	public void viewCallRecordings(Intimation argIntimationObj){
		try {
			if(argIntimationObj != null){
				URL url = new URL(BPMClientContext.CALL_RECORDING_WS_URL);//"http://192.168.3.11:8080/crmivr/tvcaudio");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				String policyNumber = argIntimationObj.getPolicy().getPolicyNumber();
				// call the procedure......
				//			policyNumber = "P/231114/01/2018/0019443";
				policyNumber = argIntimationObj.getPolicy().getPolicyNumber();
				System.out.println("Actual Policy Number :"+policyNumber);
				policyNumber = dbCalculationService.getFirstPolicyNo(policyNumber);
//				policyNumber = "P/231114/01/2018/001944";
				String POST_PARAMS = "{" + "\"polnumber\":"+ "\""+policyNumber+"\"," +
						"\"phonenumber\":"+"\"\""+"}";
				JSONObject json = new JSONObject(POST_PARAMS);
				System.out.println("Request to call Recordings : "+POST_PARAMS);
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(json.toString().getBytes());
				os.flush();
				os.close();
				CallRecordingResponse response = null;
				boolean isErrorCodeReceived = false;
				if (conn.getResponseCode() != 200) {
					//				throw new RuntimeException("Failed : HTTP Error code : "+ conn.getResponseCode());
					isErrorCodeReceived = true;
				}else if (conn.getResponseCode() == 200) {
					InputStreamReader in = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(in);
					String output = "";
					while ((output = br.readLine()) != null) {
						JSONObject jsonObject = new JSONObject(output);
						ObjectMapper mapper = new ObjectMapper();

						String jsonInString = jsonObject.toString();
						System.out.println("Response :"+jsonInString);
						response = mapper.readValue(jsonInString, CallRecordingResponse.class);
					}
					//			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
					//			JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				}
				conn.disconnect();
				//			System.out.println(response.getErrorCode());//1002
				
				if(!StringUtils.isBlank(response.getErrorCode()) && StringUtils.isBlank(response.getResCode())){
					isErrorCodeReceived = true;
				}else if(StringUtils.isBlank(response.getErrorCode()) && !StringUtils.isBlank(response.getResCode())){
					isErrorCodeReceived = false;
				}
				callRecordingsUI.init(isErrorCodeReceived, response);
				Window popup = new com.vaadin.ui.Window();
				callRecordingsUI.setPopup(popup);
				popup.setCaption("Call Recording Details");
				popup.setWidth("50%");
				popup.setHeight("50%");
				popup.setContent(callRecordingsUI);
				popup.setClosable(false);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		} catch (Exception e) {
			System.out.println("Exception in viewCallRecordings" + e);
		}
	}
	
	public void getPCCRemarks(String intimationNo) {
		ViewPCCRemarksDTO viewPCCRemarksDTO = pccRequestService.getViewPCCRemarksDTO(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		List<ViewPccRemarksDTO> pccRemarksDTOHistoryDetails = null;
		if(claim !=null){
			pccRemarksDTOHistoryDetails = preAuthService.getPccRemarksDTOHistoryDetails(claim.getKey());
		}
		if(viewPCCRemarksDTO !=null 
				|| (pccRemarksDTOHistoryDetails !=null && !pccRemarksDTOHistoryDetails.isEmpty())){
			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			if(viewPCCRemarksDTO !=null ){
				viewPostCashlessRemarkUI.init(viewPCCRemarksDTO);
				popup.setCaption("PCC Detailed View");
				popup.setContent(viewPostCashlessRemarkUI);
			}else {
				viewPostCashlessRemarkOLDUI.init(pccRemarksDTOHistoryDetails);
				popup.setCaption("PCC Remarks Details");
				popup.setContent(viewPostCashlessRemarkOLDUI);	
			}
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(viewPostCashlessRemarkUI!=null){
						viewPostCashlessRemarkUI.setClearReferenceData();
					}
					if(viewPostCashlessRemarkOLDUI!=null){
						viewPostCashlessRemarkOLDUI.setClearReferenceData();
					}
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			SHAUtils.showMessageBoxWithCaption("PCC Remarks is not avalible for "+intimationNo,"Information - PCC Remarks");
		}
	}
	
	public void getHospSPOCDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
//		viewPostCashlessRemark.init("", false, false);
		viewHospitalSPOCDetailsUI.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Hospital SPOC Details");
		popup.setWidth("50%");
//		popup.setHeight("20%");
		popup.setResizable(true);
		popup.setContent(viewHospitalSPOCDetailsUI);
		popup.setClosable(true);
		popup.center();
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	public void getOmpViewClaimHistory(String intimationNo) {
//		Intimation intimation = null;
		OMPIntimation ompIntimation = null;
		Boolean result = false;
		VerticalLayout vlayout = null;
		
	/*	if(intimationNo != null){
		intimation = intimationService
				.getIntimationByNo(intimationNo);
		}*/
		
		if(intimationNo  != null){
			ompIntimation = ompIntimationService.getIntimationByNo(intimationNo);
		}
		
		if (ompIntimation != null) {
//			if (this.rodKey == null) {
//				 result = viewOMPClaimHistoryRequest.init(ompIntimation);
//			}
//			else if(this.rodKey.equals(0l)) {
//				result = viewOMPClaimHistoryRequest.showCashlessAndReimbursementHistory(ompIntimation);
//			}
//			else{
			vlayout = viewOMPClaimHistoryRequest.showOMPReimbursementClaimHistory(
						ompIntimation.getKey());
//			}
		}		
		
	/*	if (intimation != null) {
			if (this.rodKey == null) {
				 result = viewClaimHistoryRequest.init(intimation);
			} else if(this.rodKey.equals(0l)) {
				result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			}else{
				//As said Raja.A 29/03/2018 ref:DR.KrishnaShankar
				result = viewClaimHistoryRequest.showReimbursementClaimHistory(
				intimation.getKey(), this.rodKey);
				result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			}
		}	*/
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
		
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					if(vlayout!=null){
						popup.setContent(vlayout);
					}else{
						getErrorMessage("Claim is not available");
					}
					
	/*		if(result){
				popup.setContent(viewClaimHistoryRequest);
			}		
			else if(vlayout != null){
				popup.setContent(vlayout);
			}
			else{
					getErrorMessage("Claim is not available");
			}*/
		 
		
	/*	if(intimation == null && ompIntimation == null){
			getErrorMessage("History is not available");
		}*/
	}
	
	public void getNegotiationAmountDetails(String intimationNo) {
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		viewNegotiationAmountDetailsUI.init(intimation);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Negotiation Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewNegotiationAmountDetailsUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				}
			});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}


	public void getOPViewTopPanelDetails(String intimationNo){
		
		OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
		OPClaim claimByKey = claimService.getOPClaimByIntimationNo(intimationNo);
		Long insuredKey = intimation.getInsured().getKey();
		String claimType = intimation.getClaimType().getValue();
//		Claim claimObj = claimService.getClaimforIntimation(intimation.getKey());
//		NewIntimationDto intimationDto = new NewIntimationDto();
		NewIntimationDto intimationDto = intimationService.getOPIntimationDto(intimation);
		String diagnosisName = "";
		Preauth preauthObj = null;
		ClaimDto claimDto = null;
		if(claimByKey != null){
				OPClaimMapper clmMapper = new OPClaimMapper();
				claimDto = clmMapper.getInstance().getClaimDto(claimByKey);
				if(null != claimDto){
					claimDto.setNewIntimationDto(intimationDto);
				}
		}
		if(null != claimByKey){
			intimationDto.setLineofBusiness(ReferenceTable.HEALTH_LOB_KEY.equals(claimByKey.getIntimation().getPolicy().getLobId()) ? SHAConstants.HEALTH_LOB :""); 
		}
		if(intimation != null){
			intimationDto.getPolicy().setPolicyPlan(intimation.getInsured().getPlan());
		}
		Map<String, Object> portablityStatus = dbCalculationService.getPortablityStatus(intimation.getIntimationId());
		if (portablityStatus != null) {
			intimationDto.setIsPortablity(portablityStatus.get(SHAConstants.PORTABLITY_STATUS).equals("Y") ? "YES" : "NO");
			intimationDto.setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}
				
		viewTopPanelDetails.init(intimationDto, claimDto, "TOP Panel Elements");
		UI.getCurrent().addWindow(viewTopPanelDetails);
		
	}
	
	public void getOPViewRiskDetails(String intimationNo,OutPatientDTO opBeanDto) {

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("800px");
		popup.setHeight("280px");
		popup.setCaption("Risk Details");
		VerticalLayout vLayout = new VerticalLayout();
		
		
		OPIntimation opintimation = intimationService.getOPIntimationByNo(intimationNo);
		
		if(opintimation != null){
			opriskDetailsUI.init(opintimation);
			vLayout.addComponent(opriskDetailsUI);
		} else {
			opriskDetailsUI.init(opBeanDto);
			vLayout.addComponent(opriskDetailsUI);
		}
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	public Embedded getOPIrda64VbDetails(String intimationNo){
		if(intimationNo !=null){
			VB64ComplianceDto vb64dto = new VB64ComplianceDto();
			Double reportId = Math.random() * 10000;
			vb64dto.setReportId("PGIR"+reportId.intValue());

			OPClaim clm = claimService.getOPClaimByIntimationNo(intimationNo);
			OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
			ClaimDto clmDto = null;
			if(clm != null){
				clmDto = OPClaimMapper.getInstance().getClaimDto(clm);
			}else{
				clmDto = new ClaimDto();
			}
			NewIntimationDto intimationDto = intimationService.getOPIntimationDto(intimation);

			clmDto.setNewIntimationDto(intimationDto);

			vb64dto.setClaimDto(clmDto);

			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;

			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatusForView(clmDto.getNewIntimationDto().getPolicy().getPolicyNumber(), clmDto.getNewIntimationDto().getIntimationId());

				if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("p") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_PENDING) || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_DUE))){
					vb64dto.setPaymentStatus(SHAConstants.PENDING);
				}
				else if(get64vbStatus != null && (get64vbStatus.equalsIgnoreCase("r") || get64vbStatus.equalsIgnoreCase(SHAConstants.UNIQUE_64VB_COLLECTED))){
					vb64dto.setPaymentStatus(SHAConstants.REALISED);
				}
				else if(get64vbStatus != null &&  get64vbStatus.equalsIgnoreCase("d")){
					vb64dto.setPaymentStatus(SHAConstants.DISHONOURED);
				}

			}
			if(clm!= null && clm.getIntimation()!= null && clm.getIntimation().getPolicy()!= null && clm.getIntimation().getPolicy().getPolicyNumber()!= null){

				List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(clm.getIntimation().getPolicy().getPolicyNumber());

				vb64dto.setEndorsement(endorsementList);
			}

			DocumentGenerator docgen = new DocumentGenerator();

			ReportDto rptDto = new ReportDto();

			List<VB64ComplianceDto> vb64ListDto = new ArrayList<VB64ComplianceDto>();
			vb64ListDto.add(vb64dto);		
			rptDto.setBeanList(vb64ListDto);
			rptDto.setClaimId(clmDto.getClaimId());

			String fileUrl = docgen.generatePdfDocument("Compliance64VB", rptDto);

			Embedded pdfContent = getPDFContentWindow(fileUrl);
			return pdfContent;
		}
		return null;
	}
	
	public void setDefaultDropView(String viewName) {
		switch(viewName) {
		case VIEW_CLAIM_STATUS : 
								 viewDetailsSelect.setValue(VIEW_CLAIM_STATUS);
								 break;
		default                : viewDetailsSelect.setValue(VIEW_DOC_DETAILS);				
		}
	}
	
	public void getDataCorrectionDetails(String intimationNo) {
		ProcessDataCorrectionDTO correctionDTO = correctionService.getCorrectionViewDatas(intimationNo);
		if(correctionDTO !=null ){
			dataCorrectionUI.init(correctionDTO);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Data Validation");
			popup.setWidth("75%");
			popup.setHeight("600px");
			popup.setContent(dataCorrectionUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					if(dataCorrectionUI!=null){
						dataCorrectionUI.setClearReferenceData();
					}
					System.out.println("Close listener called");
					}
				});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			SHAUtils.showMessageBoxWithCaption("Data Correction is not avalible for "+intimationNo,"Information - Data Validation");
		}
		
	}
	
	public void getICACResponseDetails(String intimationNo) {
		SearchProcessICACTableDTO processIcac = procesIcacService.getProcessICACViewDetails(intimationNo);
		if(processIcac !=null ){
			processICACUI.init(processIcac);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("ICAC Response Details");
			popup.setWidth("75%");
			popup.setHeight("600px");
			popup.setContent(processICACUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
					}
				});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			SHAUtils.showMessageBoxWithCaption("ICAC Response is not avalible for "+intimationNo,"Information - ICAC Response Details");
		}
	}
	
	private void getGMCProductBenefitwithPolicyCkause(Long policyKey)
	{		  
		VerticalLayout mainLayout = new VerticalLayout();
		VerticalLayout mainPolicyBtn=buildMainLayout(policyKey);
		mainLayout.addComponent(mainPolicyBtn);
		mainLayout.setComponentAlignment(mainPolicyBtn, Alignment.TOP_RIGHT);
		viewProductBenefitsTable.init("", false, false);
		setBenefitTableValues(policyKey);
		mainLayout.addComponent(viewProductBenefitsTable);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Product Benefits");
		popup.setWidth("90%");
		popup.setHeight("70%");
		popup.setContent(mainLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	protected void getPccTrails(String intimationNo) {

		List<ViewPCCTrailsDTO> viewPCCTrailsDTOs = pccRequestService.getViewPCCTrailsDTO(intimationNo);
		pccTrailsUI.init(viewPCCTrailsDTOs);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("PCC Trails");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(pccTrailsUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}
	
	private VerticalLayout buildMainLayout(Long policyKey) {
		Policy policyByKey = policyService.getPolicyByKey(policyKey);
		FormLayout formLayoutLeft = new FormLayout();
		FormLayout formLayoutRight = new FormLayout();
		/***
		 * POLICY CLAUSE button added for GLX2020058
		 */
		Button policyClauseButton=new Button("Policy Clause");
		policyClauseButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		policyClauseButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getViewPolicyClauseGMC(policyByKey);
				
			}
		});

		FormLayout policyClausefl=new FormLayout();
		policyClausefl.addComponent(policyClauseButton);
		
		//if(policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GROUP_HEALTH_INSURANCE_PROD_KEY)){
			Product product=masterService.getProductByProductCode(policyByKey.getProduct().getCode());
			TextField productCodeTxt = new TextField();
			productCodeTxt.setCaption("Product Code");
			//productCodeTxt.setWidth("250px");
			productCodeTxt.setNullRepresentation("");
			productCodeTxt.setValue(product.getCode());
			productCodeTxt.setReadOnly(true);



			TextField productNameTxt = new TextField();
			productNameTxt.setCaption("Product Name");
			//productNameTxt.setWidth("250px");
			productNameTxt.setNullRepresentation("");
			productNameTxt.setValue(product.getValue());
			productNameTxt.setReadOnly(true);
			formLayoutLeft.addComponent(productCodeTxt);
			formLayoutRight.addComponent(productNameTxt);
		//}
		
		HorizontalLayout headerInfo = new HorizontalLayout(formLayoutRight,
				formLayoutLeft,policyClausefl);
		/*headerInfo.setWidth("100.0%");
		headerInfo.setHeight("100px");*/
		headerInfo.setMargin(true);
		headerInfo.setSpacing(true);

		VerticalLayout mainLayout = new VerticalLayout(headerInfo);
		/*if(!policyByKey.getProduct().getKey().equals(ReferenceTable.STAR_GROUP_HEALTH_INSURANCE_PROD_KEY)){
			mainLayout.setComponentAlignment(headerInfo, Alignment.MIDDLE_RIGHT);
		}*/
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setMargin(false);
		//mainLayout.setSpacing(true);
		return mainLayout;
	}
	

	protected void getViewPolicyClause(Policy policyByKey) {

		if (policyByKey != null){			
			if (policyByKey != null && policyByKey.getProduct().getDocumentToken() != null) {
				String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyByKey.getProduct().getDocumentToken()));
				if(docViewURL!=null){
					getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
				}
			}
			else{
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Policy Clause Not Available", buttonsNamewithType);
			}
		}
	}
	
	protected void getHRMDivisionHeadRemarks(Intimation intimation){

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("HRM/Division Head Remarks");
		popup.setWidth("75%");
		popup.setHeight("75%");
		
		// Get the list of HRM/Division Head remarks
		hrmAndDivisionHeadViewDetailsPage.loadData(intimation);
		popup.setContent(hrmAndDivisionHeadViewDetailsPage);
		
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	protected void getRecMarkEscHistory(String intimationNo) {

		List<ViewMarkEscHistoryDTO> viewMarkEscHistoryDTOs = recMarkEscservice.getMarkEscHistoryDTO(intimationNo);
		viewMarkEscHistoryUI.init(viewMarkEscHistoryDTOs);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Marketing Escalations History");
		popup.setWidth("90%");
		popup.setHeight("90%");
		popup.setContent(viewMarkEscHistoryUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}
public void getCumulativeBonusViewtByPolicyNo(String strPolicyNo,Intimation intimation) { 
		
		Policy intimationObject = intimationService.getPolicyByPolicyNubember(strPolicyNo);
		Long insuredKeyForCB =0l;
		if(intimation != null){
			 insuredKeyForCB = intimation.getInsured().getKey();
		}
		Insured insuredByKey = intimationService.getInsuredByKey(insuredKeyForCB);		
		
		if(strPolicyNo!=null && intimationObject.getProduct().getCode()!=null && 
				(ReferenceTable.getCumulativeProductBonusList().contains(intimationObject.getProduct().getCode()))) {
			
			//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			String strDmsViewURL = null;
			String bancsCBFlag = "N";
            BPMClientContext bpmClientContext = new BPMClientContext();
             bancsCBFlag = bpmClientContext.getBancsBonusFlag();
			if(strPolicyNo != null){
				policyObj = policyService.getByPolicyNumber(strPolicyNo);
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY) &&
							bancsCBFlag != null && bancsCBFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)){
						try{
						strDmsViewURL = BPMClientContext.BANCS_CUMULATIVE_BONUS_URL;
						strDmsViewURL = strDmsViewURL.replace("POLICY", intimation.getPolicy().getPolicyNumber());		
						if(policyObj != null && policyObj.getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)){
							strDmsViewURL = strDmsViewURL.replace("MEMBER", insuredByKey!=null?String.valueOf(insuredByKey.getSourceRiskId()!=null?insuredByKey.getSourceRiskId():""):"");
							}else{
								strDmsViewURL = strDmsViewURL.replace("MEMBER","");	
							}
						System.out.println("Bancs Bonus URL" + strDmsViewURL);
						BrowserFrame browserFrame = new BrowserFrame("",
							    new ExternalResource(strDmsViewURL!=null?strDmsViewURL:""));
						
						
						browserFrame.setHeight("600px");
						browserFrame.setWidth("100%");
						Button btnSubmit = new Button("OK");
						
						btnSubmit.setCaption("CLOSE");
//						//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
						btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						VerticalLayout vLayout = new VerticalLayout(browserFrame,btnSubmit);
						vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
						vLayout.setSizeFull();
						final Window popup = new com.vaadin.ui.Window();
						
						popup.setCaption("");
						popup.setWidth("100%");
						popup.setHeight("100%");
						//popup.setSizeFull();
						popup.setContent(vLayout);
						
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						
						btnSubmit.addClickListener(new Button.ClickListener() {
							
							private static final long serialVersionUID = 1L;
					
							@Override
							public void buttonClick(ClickEvent event) {

								popup.close();
									
							}
							
						});
						popup.addCloseListener(new Window.CloseListener() {
							
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});
						popup.setModal(true);
						UI.getCurrent().addWindow(popup); 
						}
						catch(Exception e){
							e.printStackTrace();
							getErrorMessage("Bonus Logic Details Not Applicable");
						}
					
					}else{
						strDmsViewURL = BPMClientContext.CUMULATIVE_BONUS_URL;
						strDmsViewURL = strDmsViewURL.concat(strPolicyNo);
						BrowserFrame browserFrame = new BrowserFrame("",
							    new ExternalResource(strDmsViewURL!=null?strDmsViewURL:""));
						
						
						browserFrame.setHeight("600px");
						browserFrame.setWidth("200%");
						Button btnSubmit = new Button("OK");
						
						btnSubmit.setCaption("CLOSE");
						btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						VerticalLayout vLayout = new VerticalLayout(browserFrame,btnSubmit);
						vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
						vLayout.setSizeFull();
						final Window popup = new com.vaadin.ui.Window();
						
						popup.setCaption("");
						popup.setWidth("100%");
						popup.setHeight("100%");
						//popup.setSizeFull();
						popup.setContent(vLayout);
						
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						
						btnSubmit.addClickListener(new Button.ClickListener() {
							
							private static final long serialVersionUID = 1L;
					
							@Override
							public void buttonClick(ClickEvent event) {

								popup.close();
									
							}
							
						});
						popup.addCloseListener(new Window.CloseListener() {
							
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});
						popup.setModal(true);
						UI.getCurrent().addWindow(popup); 
					}
				}
			}
			
			}else {
			getErrorMessage("Bonus Logic Details Not Applicable");
			}
		}
	
	public void getEarlierAcknowledgementDetailsButton(String intimationNo){

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Earlier Acknowledgement Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		
		// Get the list of HRM/Division Head remarks
		
		OMPIntimation ompIntimation = ompIntimationService.getIntimationByNo(intimationNo);
		OMPClaim claim = ompclaimService.getClaimforIntimation(ompIntimation.getKey());
		OMPDocAcknowledgement docAcknowledgement = oMPAckDocService.findAcknowledgmentByClaimKey(claim.getKey());
		//List<OMPReimbursement> ompReimbursementList = oMPAckDocService.getReimbursementByClaimKey(claim.getKey());
		if(docAcknowledgement != null)
		{
			oMPEarlierAcknowledgementDetailsPage.loadData(ompIntimation);
			popup.setContent(oMPEarlierAcknowledgementDetailsPage);
			
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("No Records Found");
		}
		
	
	}
	
	public void getAcknowledgementDetailsButton(String intimationNo){
		

		Boolean isAckAvaliable = false;
		
		OMPIntimation intimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		OMPClaim claim = ompclaimService.getClaimforIntimation(intimation
				.getKey());
		Long claimKey = null;
		if (intimation != null && intimation.getKey() != null) {
			
			if (null != claim && claim.getKey() != null) {
				claimKey = claim.getKey();
			}
		}
		
		OMPDocAcknowledgement docAcknowledgement = null;
		
		if (null != claimKey) {
			docAcknowledgement = oMPAckDocService.findAcknowledgmentByClaimKey(claimKey);
			if (null != docAcknowledgement) {
				isAckAvaliable = true;
				
			}
			//List<OMPReimbursement> ompReimbursementList = oMPAckDocService.getReimbursementByClaimKey(claim.getKey());
			
			if(docAcknowledgement != null ){
				ompViewAcknowledgementPage.init(intimation,
					docAcknowledgement.getKey() ,docAcknowledgement);
			
				if (isAckAvaliable && docAcknowledgement !=null) {
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View Acknowledgement Details");
					popup.setWidth("75%");
					popup.setHeight("90%");
					popup.setContent(ompViewAcknowledgementPage);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
					 * 
					 */
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				}
			}
			 else {
					getErrorMessage("No Records Found");
				}

		}
		
	}
	
	public void getBusinessProfileChart(String intimationNo,PreauthDTO preauthDTO) {


		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("VIEW MINI BUSINESS PROFILE");
		businessPCPage.init(preauthDTO);
		
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(businessPCPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);

		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		//}

	}
	
	private void getViewPolicyClauseGMC(Policy policyByKey) {

		if (policyByKey != null){			
			//Added for version
			Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(policyByKey.getKey(),policyByKey.getPolicyNumber(),0l,0l);
			Long versionNumber =1l;
			if(getproductUINvalues != null){
				if(getproductUINvalues.containsKey("productversionNumber")){
					versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
				}
			}

			System.out.println(String.format("Version Number [%s]", versionNumber));

			if(versionNumber !=null && versionNumber ==1L){
				if (policyByKey != null && policyByKey.getProduct().getDocumentToken() != null) {
					String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyByKey.getProduct().getDocumentToken()));
					if(docViewURL!=null){
						getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
					}
				}
			}
			else if(versionNumber!=null && versionNumber ==2L){

				if (policyByKey != null && policyByKey.getProduct().getDocumentToken2() != null) {
					String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyByKey.getProduct().getDocumentToken2()));
					if(docViewURL!=null){
						getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
					}
				}
			}
			else if(versionNumber!=null && versionNumber ==3L){

				if (policyByKey != null && policyByKey.getProduct().getDocumentToken3() != null) {
					String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyByKey.getProduct().getDocumentToken3()));
					if(docViewURL!=null){
						getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
					}
				}
			}
			else if(versionNumber!=null && versionNumber ==4L){

				if (policyByKey != null && policyByKey.getProduct().getDocumentToken4() != null) {
					String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyByKey.getProduct().getDocumentToken4()));
					if(docViewURL!=null){
						getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
					}
				}
			}
			else{
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Policy Clause Not Available", buttonsNamewithType);
			}
		}
	}
	
	protected void getTalkTalkTable(String intimationNo) {
		
		//Claim claim =talkService.getClaimByIntimationNo(intimationNo);
		//Intimation intimation = talkService.getIntimationByNo(intimationNo);
		
		List<TalkTalkTalk> talkTalkTalkViewDetailsList = talkService.getTalkTalkTalkDetailsByIntimationNumList(intimationNo);
		List<ExtraEmployeeEffortDTO> viewTALKCATDTOs = claimService.getCategoryDetailsFromTALKView(intimationNo);
		
		if((viewTALKCATDTOs !=null && !viewTALKCATDTOs.isEmpty()) || (talkTalkTalkViewDetailsList!=null && !talkTalkTalkViewDetailsList.isEmpty())){
			viewtalktalkTable = viewtalktalkTableInst.get();
			viewtalktalkTable.init("", false, false);
			viewtalktalkTable.setCaption("Talk, Talk, Talk");
			viewtalktalkTable.setTableList(viewTALKCATDTOs);
			
			setViewTalkTalkTalkTableValues(intimationNo);
			
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Talk, Talk, Talk");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewtalktalkTable);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Talk,Talk,Talk information is not available");
		}
	}
	
	public void getTalkTalkTalkDetails(String intimationNo,PreauthDTO preauthDTO) {
		
		//Intimation intimation = talkService.getIntimationByNo(intimationNo);
		
		List<TalkTalkTalk> talkTalkTalkViewDetailsList = talkService.getTalkTalkTalkDetailsByIntimationNumList(intimationNo);
		List<ExtraEmployeeEffortDTO> viewTALKCATDTOs = claimService.getCategoryDetailsFromTALKView(intimationNo);
		
		if((viewTALKCATDTOs !=null && !viewTALKCATDTOs.isEmpty()) || (talkTalkTalkViewDetailsList!=null && !talkTalkTalkViewDetailsList.isEmpty())){
			viewTalkTalkTalkDetailsUI.init(preauthDTO,intimationNo);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("TALK TALK TALK");
			popup.setWidth("75%");
			popup.setHeight("600px");
			popup.setContent(viewTalkTalkTalkDetailsUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
					}
				});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
//			getErrorMessage("Talk,Talk,Talk information is not available");
			getErrorMessage("No Details Found");
		}
	}
	
	public void setViewTalkTalkTalkTableValues(String initmationNumber)
	 {
		//Intimation intimation = talkService.getIntimationByNo(intimationNo);
		if(initmationNumber !=null){
		List<TalkTalkTalk> talkTalkTalkViewDetailsList = talkService.getTalkTalkTalkDetailsByIntimationNumList(initmationNumber);
		 //List<TalkTalkTalk> talkTalkTalkViewDetailsList = talkService.getTalkTalkTalkDetailsByClaimKeyList(claim.getKey());
		 List<InitiateTalkTalkTalkDTO> finalTableDTOList = new ArrayList<InitiateTalkTalkTalkDTO>();
		 if(null != talkTalkTalkViewDetailsList){
			 
			 InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO = null;
			 
			 for (TalkTalkTalk talkTalkTalk : talkTalkTalkViewDetailsList) {
				
				 initiateTalkTalkTalkDTO = new InitiateTalkTalkTalkDTO();
				 if(talkTalkTalk.getTypeOfCommunication() !=null){
						MastersValue masterValue = talkTalkTalk.getTypeOfCommunication();
						 SelectValue typeOfComm=new SelectValue();
						 typeOfComm.setValue(masterValue.getValue());
						 typeOfComm.setId(masterValue.getKey());
						 initiateTalkTalkTalkDTO.setTypeOfCommunication(typeOfComm);
						
					}
					if(talkTalkTalk.getDateAndTimeofCall() !=null){
						initiateTalkTalkTalkDTO.setTalkSpokenDate(talkTalkTalk.getDateAndTimeofCall());
					}
					
					if(talkTalkTalk.getContactNumber() !=null){
						initiateTalkTalkTalkDTO.setTalkMobto(talkTalkTalk.getContactNumber().toString());
					}
					if(talkTalkTalk.getSpokenTo() !=null){
						initiateTalkTalkTalkDTO.setTalkSpokento(talkTalkTalk.getSpokenTo());
					}
					if(talkTalkTalk.getRemarks() !=null){
						initiateTalkTalkTalkDTO.setRemarks(talkTalkTalk.getRemarks());
					}
					if(talkTalkTalk.getProcessingUserName() !=null){
						initiateTalkTalkTalkDTO.setProcessingUserName(talkTalkTalk.getProcessingUserName());
					}
					if(talkTalkTalk.getReferenceId() != null){
						DialerStatusLog dialerStatus = talkService.getDialerStatusLog(talkTalkTalk.getReferenceId(),talkTalkTalk.getConvoxId().toString());
						if(dialerStatus != null){
							initiateTalkTalkTalkDTO = dbCalculationService.getDailerEndCallDateTime(dialerStatus.getCallReferenceId(), initiateTalkTalkTalkDTO);
							String starTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallStartTime());
							String endTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallEndTime());
							initiateTalkTalkTalkDTO.setCallStartTime(starTime);
							initiateTalkTalkTalkDTO.setCallEndTime(endTime);
							initiateTalkTalkTalkDTO.setCallDuration(dialerStatus.getCallDuration());
							initiateTalkTalkTalkDTO.setFileName(dialerStatus.getFileName());
							
							
						}
					}
				 
					finalTableDTOList.add(initiateTalkTalkTalkDTO);
			}
			 
			 for (InitiateTalkTalkTalkDTO initiateTalkTalkDTO : finalTableDTOList) {
				 viewTalkTalkTalkDetailsTable.addBeanToList(initiateTalkTalkDTO);

				}
			 viewTalkTalkTalkDetailsTable.setTableList(finalTableDTOList);
		 }
				 
		}
	 }
	
	@Inject
	private Instance<IntimationSourceDetailsView> intimationSrcDetailsViewInstance;
	private IntimationSourceDetailsView intimationSrcDetailsView;
	
	private void getIntimationSourceDetails(String argIntimationNo){
		Intimation intimationObj = intimationService.getIntimationByNo(argIntimationNo);
		List<Preauth> listofDocuments = new ArrayList<>();
		if(intimationObj.getClaimType().getKey().intValue() == ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.intValue()){
			listofDocuments = preAuthService.getPreauthByIntimationKey(intimationObj.getKey());
		}
		intimationSrcDetailsView = intimationSrcDetailsViewInstance.get();
		Window popup = new com.vaadin.ui.Window();
		intimationSrcDetailsView.init(intimationObj, listofDocuments);
		
		popup.setCaption("Source Channel Details");
		popup.setContent(intimationSrcDetailsView);
		popup.center();
		popup.setWidth("45%");
		if(intimationObj.getClaimType().getKey().intValue() == ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.intValue()&& listofDocuments.size() > 0){
			popup.setHeight("40%");
		}else{
			popup.setHeight("20%");
		}
		popup.setClosable(true);
		popup.setResizable(false);
		popup.setModal(true);
		popup.addCloseListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Source Details Close Listener Called....");
			}
		});
		UI.getCurrent().addWindow(popup);
	}
	
	public void viewGrievanceTrails(Intimation argIntimationObj){
		List<ClaimGrievanceDTO> data = null;
		try {
		ClaimGrievanceAPIService grievSer = new ClaimGrievanceAPIService();
//		data = grievSer.getGrievanceTrialDataForClaim("CIR/2023/141137/0202003");
		//List<ClaimGrievanceDTO> data = grievSer.getGrievanceTrialDataForClaim(argIntimationObj.getIntimationId());
		
			if(argIntimationObj !=null){
				Claim claimObj = intimationService.getClaimforIntimation(argIntimationObj.getKey());
				if(claimObj != null && claimObj.getGrivanceDeptID() != null){
					data = grievSer.getGrievanceTrialDataForClaim(argIntimationObj.getIntimationId(), claimObj.getGrivanceDeptID(), "Grievance");
					//data = grievSer.getGrievanceTrialDataForClaim("CIR/2023/141137/0202003", claimObj.getGrivanceDeptID(), "Grievance");
				/*}else{
					getErrorMessage("Grievance View is not available");
				}*/
				System.out.println("Zoho Grievance Data "+data);
				//ViewGrievanceTrailsTableDTO viewGrievanceTrailsTableDTO = new ViewGrievanceTrailsTableDTO();
				ClaimGrievanceDTO claimGrievanceDTO= new ClaimGrievanceDTO();
				if(claimObj !=null && claimObj.getZohoGrivanceFlag()!=null){
					claimGrievanceDTO.setGrievanceFlag(claimObj.getZohoGrivanceFlag());
				}
				claimGrievanceDTO.setIntimationNumber(argIntimationObj.getIntimationId());
				if(data !=null){
				claimGrievanceDTO.setGrievanceDetails(data);
				}
				if (claimObj !=null && claimObj.getZohoGrivanceFlag() !=null && claimObj.getZohoGrivanceFlag().equalsIgnoreCase("Y")){
					viewGrievanceTrailsUI.init(claimGrievanceDTO);
					Window popup = new com.vaadin.ui.Window();
					//viewGrievanceTrailsUI.setPopup(popup);
					popup.setCaption("View Grievance Trails");
					popup.setWidth("70%");
					popup.setHeight("70%");
					popup.setContent(viewGrievanceTrailsUI);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						private static final long serialVersionUID = 1L;
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);

				}
				else{
					getErrorMessage("Grievance View is not available");
				}
				}else{
					getErrorMessage("Grievance View is not available");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in viewGrievanceTrails" + e);
		}
	}
	
	public void viewEscalationTrails(Intimation argIntimationObj){
		List<ClaimGrievanceDTO> data = null;
		try {
		ClaimGrievanceAPIService grievSer = new ClaimGrievanceAPIService();
//		data = grievSer.getGrievanceTrialDataForClaim("CIR/2023/141137/0202003", "", "");
		//List<ClaimGrievanceDTO> data = grievSer.getGrievanceTrialDataForClaim(argIntimationObj.getIntimationId());
		//System.out.println("Internal Escalation Data "+data);
			if(argIntimationObj !=null){
				Claim claimObj = intimationService.getClaimforIntimation(argIntimationObj.getKey());
				if(claimObj != null && claimObj.getEscalationDeptID() != null){
					data = grievSer.getGrievanceTrialDataForClaim(argIntimationObj.getIntimationId(), claimObj.getEscalationDeptID(), "Escalation");
					//data = grievSer.getGrievanceTrialDataForClaim("CIR/2023/141137/0202003", claimObj.getEscalationDeptID(), "Escalation");
				System.out.println("Internal Escalation Data "+data);
				ClaimGrievanceDTO claimGrievanceDTO= new ClaimGrievanceDTO();
				if(claimObj !=null && claimObj.getInternalEscalationFlag()!=null){
					claimGrievanceDTO.setEscalationFlag(claimObj.getInternalEscalationFlag());
				}
				claimGrievanceDTO.setIntimationNumber(argIntimationObj.getIntimationId());
				if(data !=null){
				claimGrievanceDTO.setGrievanceDetails(data);
				}
				if (claimObj !=null && claimObj.getInternalEscalationFlag() !=null && claimObj.getInternalEscalationFlag().equalsIgnoreCase("Y")){
					viewEscalaltionTrailsUI.init(claimGrievanceDTO);
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View Escalation");
					popup.setWidth("70%");
					popup.setHeight("70%");
					popup.setContent(viewEscalaltionTrailsUI);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						private static final long serialVersionUID = 1L;
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				}else{
					getErrorMessage("Escalation View is not available");
				}
				}else{
					getErrorMessage("Escalation View is not available");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in view Escalation Trails" + e);
		}
	}
}
