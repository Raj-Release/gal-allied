/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(UpdatePaymentDetailView.class)
public class UpdatePaymentDetailPresenter extends AbstractMVPPresenter<UpdatePaymentDetailView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_PENDING_CASES_BUTTON_CLICK = "payment_SearchPendingCasesClick";
	
	public static final String SEARCH_BUTTON_CLICK = "payment_doSearchCreatebatchTable";
	
	public static final String CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW = "payment_Create_batch_show_edit_payment_details_view";
	public static final String CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING = "payment_generate_batch_no_for_payment_processing";
	public static final String CREATE_BATCH_SHOW_VIEW_DOCUMENTS = "payment_create_batch_show_view_documents";
	
	public static final String BUILD_LAYOUT_BASED_ON_TYPE = "payment_Build_layout_based_on_type";
	
	public static final String CREATE_BATCH_SHOW_DETAILS = "payment_create_batch_show_details";
	
	public static final String BUILD_LAYOUT_BASED_ON_BATCH_TYPE = "payment_Build_layout_base_on_batch_type";
	public static final String BUILD_LAYOUT_BASED_ON_SEARCH_BATCH_TYPE = "payment_Build_layout_based_on_search_batch_type";
	public static final String VALIDATION = "payment_Validate_penal_intrest";
	public static final String NO_OF_EXCEEDING_DAYS_VALIDATION = "payment_no_of_days_exceeeding_validation";
	public static final String SET_INTEREST_RATE = "payment_set_interrest_rate";
	public static final String REPAINT_TABLE = "payment_repaint_table";
	
	public static final String CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS = "payment_create_batch_populate_previous_acct_details";
	
	public static final String RESET_SERIAL_NO = "payment_RESET_SERIAL_NO";
	
	public static final String FA_SETUP_IFSC_DETAILS = "update_payment_ifsc code";

	public static final String GET_PAYMENT_CPU_NAME = "payment_cpu_name";

	public static final String HOLD_PAYMENT_PROCESS = "HOLD_PAYMENT_PROCESS";

	public static final String UPDATE_PAYMENT_PROCESS = "UPDATE_PAYMENT_PROCESS";
	
	public static final String SAVE_PAYMENT_PROCESS = "SAVE_PAYMENT_PROCESS";

	public static final String GET_PAYMENT_CPU = "GET_PAYMENT_CPU";
	
	@EJB
	private UpdatePaymentDetailService paymentDetailService;	
	
	
	@Inject
	private ViewDetails viewDetails;
	@EJB
	private PolicyService policyService;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private Window popup;

	@Inject
	private PreviousAccountDetailsTable previousAcctDetailsTable;
	
	@EJB
	private ClaimService claimService;
	
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB		
	private DBCalculationService dbCalculationService;
	

	@PersistenceContext
	protected EntityManager entityManager;
	
	public void searchPendingCases(@Observes @CDIEvent(SEARCH_PENDING_CASES_BUTTON_CLICK) final ParameterDTO parameters) {
		
		DBCalculationService dbcalService = new DBCalculationService();
		view.buildCreatePendingBatchLayout(dbcalService.getPendingBatchLotCases());
	}
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		UpdatePaymentDetailFormDTO searchFormDTO = (UpdatePaymentDetailFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(paymentDetailService.search(searchFormDTO,userName,passWord));
	}
	
	public void generateBatchNumber(@Observes @CDIEvent(CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING) final ParameterDTO parameters) {
		List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		Map<String, Object> lotCreationMap = paymentDetailService.updateBatchNumberForPaymentProcessing(tableDTOList,typeOfService);
		String holdPendingFlag = (String)lotCreationMap.get(SHAConstants.HOLD_PENDING_SERVICE);
		if((SHAConstants.N_FLAG).equalsIgnoreCase(holdPendingFlag))
		{
			String strBatchNo = (String)lotCreationMap.get(SHAConstants.BATCH_NUMBER);
			if(null != strBatchNo)
			{
				CreateAndSearchLotTableDTO tableDTO = paymentDetailService.getBatchDetails(strBatchNo);
				lotCreationMap.put(SHAConstants.SUCCESS_TABLE, tableDTO);
				view.buildSuccessLayout(lotCreationMap);
			}
			
		}
		else
		{
			view.buildSuccessLayout(lotCreationMap);
		}
	}
	
	
	public void holdPaymentDetail(@Observes @CDIEvent(HOLD_PAYMENT_PROCESS) final ParameterDTO parameters) {
		List<UpdatePaymentDetailTableDTO> tableDTOList = (List<UpdatePaymentDetailTableDTO>) parameters.getPrimaryParameter();
		 paymentDetailService.updatePaymentDetail(tableDTOList, SHAConstants.H_FLAG);
		 UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = tableDTOList.get(0);
		 String succesMsg = tableDTOList.size() + " / " + updatePaymentDetailTableDTO.getTotalCount() +" kept on hold successfully";
		 view.buildSuccessLayout(succesMsg);
	}
	
	public void updatePaymentDetail(@Observes @CDIEvent(UPDATE_PAYMENT_PROCESS) final ParameterDTO parameters) {
		List<UpdatePaymentDetailTableDTO> tableDTOList = (List<UpdatePaymentDetailTableDTO>) parameters.getPrimaryParameter();
	     String flag = (String)parameters.getSecondaryParameter(0, String.class);
		 paymentDetailService.updatePaymentDetail(tableDTOList,flag);
		 UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = tableDTOList.get(0);
		 String succesMsg = tableDTOList.size() + " / " + updatePaymentDetailTableDTO.getTotalCount() +" updated successfully";
		 view.buildSuccessLayout(succesMsg);
	}
	
	public void savePaymentDetail(@Observes @CDIEvent(SAVE_PAYMENT_PROCESS) final ParameterDTO parameters) {
		List<UpdatePaymentDetailTableDTO> tableDTOList = (List<UpdatePaymentDetailTableDTO>) parameters.getPrimaryParameter();
		 paymentDetailService.updatePaymentDetail(tableDTOList, null);
		 UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = tableDTOList.get(0);
		 String succesMsg = tableDTOList.size() + " / " + updatePaymentDetailTableDTO.getTotalCount() +" saved successfully";
		 view.buildSuccessLayout(succesMsg);
	}
	
	public void setPaymentCpu(
			@Observes @CDIEvent(GET_PAYMENT_CPU) final ParameterDTO parameters) {
		UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = (UpdatePaymentDetailTableDTO) parameters.getPrimaryParameter();
		String paymentCpuCode = updatePaymentDetailTableDTO.getPaymentCpuCodeString();
		TmpCPUCode masCpuCode = null;
		try {
			masCpuCode = claimService.getMasCpuCode(Long.parseLong(paymentCpuCode));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(masCpuCode!=null){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(masCpuCode.getKey());
			selectValue.setValue(masCpuCode.getCpuCode().toString() + "-" +masCpuCode.getDescription());
			updatePaymentDetailTableDTO.setPaymentCpuCode(selectValue);
			updatePaymentDetailTableDTO.setPaymentCpuName(masCpuCode.getDescription());
			view.setPaymentCpu(updatePaymentDetailTableDTO);
		}
	}
	
	public void generateValidateIntrestRate(@Observes @CDIEvent(VALIDATION) final ParameterDTO parameters) {		
		
		view.intrestRateValidation();
		
	}
	
	public void repaintTable(@Observes @CDIEvent(REPAINT_TABLE) final ParameterDTO parameters) {		
		String layoutType = (String) parameters.getPrimaryParameter();			
		view.repaintTable(layoutType);	
		view.buildSearchBatchLayout(layoutType);
		
		
	}
	public void generateValidateNoOfDaysExceeding(@Observes @CDIEvent(NO_OF_EXCEEDING_DAYS_VALIDATION) final ParameterDTO parameters) {		
		
		view.noOfExceedingDaysValidation();
		
	}
	
	public void generateLayoutBasedOnType(@Observes @CDIEvent(BUILD_LAYOUT_BASED_ON_TYPE) final ParameterDTO parameters) {
		String layoutType = (String) parameters.getPrimaryParameter();
		String batchType =  (String) parameters.getSecondaryParameters()[0];
		view.buildHoldPendingLayout(layoutType,batchType);
	}
	
	public void generateResultTable(@Observes @CDIEvent(BUILD_LAYOUT_BASED_ON_BATCH_TYPE) final ParameterDTO parameters) {
		String layoutType = (String) parameters.getPrimaryParameter();
		//Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
		//view.buildSuccessLayout(lotCreationMap);
	//	view.buildResultantTableLayout(layoutType,);
	}
	
	public void generateLayoutBaseOnSearchBatch(@Observes @CDIEvent(BUILD_LAYOUT_BASED_ON_SEARCH_BATCH_TYPE) final ParameterDTO parameters) {
		String layoutType = (String) parameters.getPrimaryParameter();
		view.buildSearchBatchLayout(layoutType);
	}
	@SuppressWarnings({ "deprecation" })
	public void showEditPaymentDetailsView(@Observes @CDIEvent(CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW) final ParameterDTO parameters) {
		
		
		//RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		
		/*List<List<PreviousAccountDetailsDTO>> previousAcctDetailsList = populatePreviousAccountDetails(tableDTO.getIntimationNo(), tableDTO.getDocReceivedFrom());
		if(null != previousAcctDetailsList && !previousAcctDetailsList.isEmpty())
		{
			tableDTO.setPreviousAccountDetailsList(previousAcctDetailsList);
		}*/
		
		//List<List<PreviousAccountDetailsDTO>> previousAcctDetailsList = populatePreviousAccountDetails(tableDTO.getIntimationNo(), tableDTO.getDocReceivedFrom());				
	
		Claim claim = claimService.getClaimsByIntimationNumber(tableDTO.getIntimationNo());
		Long documentReceivedId = tableDTO.getDocumentReceivedFromId() != null ? tableDTO.getDocumentReceivedFromId() : 1542l;
		List<PreviousAccountDetailsDTO> previousAcctDetailsList = dbCalculationService.getPreviousAccountDetails(claim.getIntimation().getInsured().getInsuredId(),documentReceivedId);
		if(null != previousAcctDetailsList && !previousAcctDetailsList.isEmpty())
		{
			tableDTO.setPreviousAccntDetailsList(previousAcctDetailsList);
		}
		popup = new com.vaadin.ui.Window();
		
		final EditPaymentDetailsView paymentDetailsView = new EditPaymentDetailsView();
		
		//paymentDetailsView.initPresenter(SHAConstants.CREATE_BATCH_EDIT_PAYMENT_DETAILS);
		paymentDetailsView.initPresenter(SHAConstants.CREATE_BATCH_PAYMENT);
		paymentDetailsView.initAccntTable(previousAcctDetailsTable);

		paymentDetailsView.init(tableDTO,popup,viewSearchCriteriaWindow);
		paymentDetailsView.loadContainerForPayeeName(getValuesForNameDropDown(tableDTO));
	//	paymentDetailsView.initPresenter(SHAConstants.SEARCH_RRC_REQUEST); 
		paymentDetailsView.getContent();
		
		popup.setCaption("Edit Payment Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(paymentDetailsView);
		popup.setClosable(false);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				
				CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
			//	if(paymentDetailsView.validatePage())
				if(null != paymentDetailsView.getIsSaveToDB() && paymentDetailsView.getIsSaveToDB())
				{
					//Boolean isSuccess = paymentDetailService.savePaymentDetails(tableDTO);
				}
				
				//System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showDetailsPopup(@Observes @CDIEvent(CREATE_BATCH_SHOW_DETAILS) final ParameterDTO parameters) {
		CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();	
		StringBuffer productCodeWithName;
		
		List<CreateAndSearchLotTableDTO> tableDTOList = paymentDetailService.getBatchDetails(tableDTO);
		
		List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
			
		
		Reimbursement reimbObj =paymentDetailService.getReimbursementObject(createAndSearchLotTableDTO.getRodNo());
		
		if(null != reimbObj){
		
		DocAcknowledgement docAck = paymentDetailService.getDocAcknowledgeBasedOnROD(reimbObj.getKey());
		
		if(null != docAck)
		{
		
			createAndSearchLotTableDTO.setReconsiderationFlag(reimbObj.getReconsiderationRequest());
		
		}
		 Calendar faDate = Calendar.getInstance();
	      faDate.setTime(createAndSearchLotTableDTO.getFaApprovedDate());
	      faDate.add(Calendar.DATE, 1);
	      Date faApproveDate = faDate.getTime();
	      
	      if(null != faApproveDate)
	      {
	    	  createAndSearchLotTableDTO.setNextDayOfFaApprovedDate(faApproveDate);
	      }
		}
		
		productCodeWithName = new StringBuffer();
		if(null != createAndSearchLotTableDTO.getProductName()){
			
			createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("-").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
		}
		else
		{
			createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
		}
		finalList.add(createAndSearchLotTableDTO);
		}
		view.showDetails(finalList,tableDTO.getAccountBatchNo());
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showViewDocuments(@Observes @CDIEvent(CREATE_BATCH_SHOW_VIEW_DOCUMENTS) final ParameterDTO parameters) {
		
		
		//RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		String intimationNo = (String) parameters.getPrimaryParameter();
		//popup = new com.vaadin.ui.Window();
		
		
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
		/*Below code commented for security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		view.showClaimsDMS(url);
		//viewDetails.viewUploadedDocumentDetails(intimationNo);
		
		/*popup.setCaption("View Documents");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(viewDetails);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
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
	
	
	

	
	private BeanItemContainer<SelectValue> getValuesForNameDropDown( CreateAndSearchLotTableDTO tableDTO)
	{
		BeanItemContainer<SelectValue> payeeNameValueContainer = null;
		Policy policy = policyService.getPolicy(tableDTO.getPolicyNo());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			SelectValue selectValue = new SelectValue();
			SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(tableDTO.getTypeOfClaim()))
		{
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(tableDTO.getHospitalName());
			payeeValueList.add(hospitalName);
		}
		
		 payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		
		
		
		}
		return payeeNameValueContainer;
		
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		EditPaymentDetailsView editPaymentView = (EditPaymentDetailsView) parameters.getSecondaryParameter(0, EditPaymentDetailsView.class);
		view.populatePreviousPaymentDetails(tableDTO,editPaymentView);
		
	}
	
	@Override
	public void viewEntered() {
		
		
	}
	
	 private List<List<PreviousAccountDetailsDTO>> populatePreviousAccountDetails(String intimationNo,String docReceivedFrom)
	    {
		 	List<List<PreviousAccountDetailsDTO>> listOfPreviousClaims = new ArrayList<List<PreviousAccountDetailsDTO>>();
	    	ViewTmpIntimation viewTmpIntimation = intimationService.searchbyIntimationNoFromViewIntimation(intimationNo);
	    	if(null != viewTmpIntimation)
	    	{
	    		List<String> claimNoList = getClaimByPolicyWiseForPaymentDetails(viewTmpIntimation);
	    		if(null != claimNoList)
	    		{
	    			for (String claimNo : claimNoList) {
	    				List<PreviousAccountDetailsDTO> previousClaimsDTOList = createRodService.getPaymentDetailsForPreviousClaim(claimNo,docReceivedFrom);
	    				if(null !=  previousClaimsDTOList && !previousClaimsDTOList.isEmpty())
	    					listOfPreviousClaims.add(previousClaimsDTOList);
					}
	    			
	    		}
	    	}
	    	return listOfPreviousClaims ;
	    }
	
	 
	 /**
		 * Added for R0333 enhancement- populate previous account details.
		 * 
		 **/
		private List<String> getClaimByPolicyWiseForPaymentDetails(final ViewTmpIntimation intimation) {

			
			List<String> claimNoList = new ArrayList<String>();
			List<ViewTmpClaim> currentClaim = claimService.getTmpClaimByIntimation(intimation.getKey());
			
			String policyNumber =intimation.getPolicyNumber();
			List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
			List<ViewTmpClaim> claimsByPolicyNumber = claimService
					.getViewTmpClaimsByPolicyNumber(policyNumber);
			
			Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
			previousclaimsList.addAll(claimsByPolicyNumber);
			
			previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);
			
			
			if(null != previousclaimsList && !previousclaimsList.isEmpty())
			{
				for (ViewTmpClaim viewTmpClaim : previousclaimsList) {
					claimNoList.add(viewTmpClaim.getClaimId());
				}
				
			}
			
			return claimNoList;

		}
		

		public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
			try {
				Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
				if(renewalPolNo != null) {
					List<ViewTmpIntimation> intimationKeys = intimationService.getIntimationByPolicyKey(renewalPolNo.getKey());
					List<ViewTmpClaim> claimsByPolicyNumber = claimService
							.getViewTmpClaimsByIntimationKeys(intimationKeys);
//					List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
					if(claimsByPolicyNumber != null && !claimsByPolicyNumber.isEmpty()) {
						for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
							if(!generatedList.contains(viewTmpClaim)) {
								generatedList.add(viewTmpClaim);
							}
						}
					}
					if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
						getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
					} else {
						return generatedList;
					}
				}
			} catch(Exception e) {
				
			}
			return generatedList;
		}
	 
		public void setupIFSCDetails(
				@Observes @CDIEvent(FA_SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = (UpdatePaymentDetailTableDTO) parameters.getSecondaryParameter(0, UpdatePaymentDetailTableDTO.class);
			view.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void setPaymentCpuName(
				@Observes @CDIEvent(GET_PAYMENT_CPU_NAME) final ParameterDTO parameters) {
			String paymentCpuCode = (String) parameters.getPrimaryParameter();
			paymentCpuCode = paymentCpuCode.trim().substring(0, paymentCpuCode.indexOf("-"));
			TmpCPUCode masCpuCode = null;
			try {
				masCpuCode = claimService.getMasCpuCode(Long.parseLong(paymentCpuCode.trim()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = (UpdatePaymentDetailTableDTO) parameters.getSecondaryParameter(0, UpdatePaymentDetailTableDTO.class);
			if(masCpuCode!=null){
				updatePaymentDetailTableDTO.setPaymentCpuName(masCpuCode.getDescription());
				view.setPaymentCpu(updatePaymentDetailTableDTO);
			}
		}
		
}
