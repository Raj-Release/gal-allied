/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.createbatch.search;

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
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotService;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerficationAccountDetailsService;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchCreateBatchView.class)
public class SearchCreateBatchPresenter extends AbstractMVPPresenter<SearchCreateBatchView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_PENDING_CASES_BUTTON_CLICK = "SearchPendingCasesClick";
	
	public static final String SEARCH_BUTTON_CLICK = "doSearchCreatebatchTable";
	
	public static final String CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW = "Create_batch_show_edit_payment_details_view";
	public static final String CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING = "generate_batch_no_for_payment_processing";
	
	public static final String CREATE_BATCH_PAYMENT_LVL1_SUBMIT = "payment verification level 1";
	
	public static final String CREATE_BATCH_PAYMENT_LVL1_HOLD = "payment verification level 2";
			
	
	public static final String CREATE_BATCH_SHOW_VIEW_DOCUMENTS = "create_batch_show_view_documents";
	
	public static final String BUILD_LAYOUT_BASED_ON_TYPE = "Build_layout_based_on_type";
	
	public static final String CREATE_BATCH_SHOW_DETAILS = "create_batch_show_details";
	
	public static final String BUILD_LAYOUT_BASED_ON_BATCH_TYPE = "Build_layout_base_on_batch_type";
	public static final String BUILD_LAYOUT_BASED_ON_SEARCH_BATCH_TYPE = "Build_layout_based_on_search_batch_type";
	public static final String VALIDATION = "Validate_penal_intrest";
	public static final String NO_OF_EXCEEDING_DAYS_VALIDATION = "no_of_days_exceeeding_validation";
	public static final String SET_INTEREST_RATE = "set_interrest_rate";
	public static final String REPAINT_TABLE = "repaint_table";
	
	public static final String CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS = "create_batch_populate_previous_acct_details";
	
	public static final String RESET_SERIAL_NO = "RESET_SERIAL_NO";

	public static final String GET_PAYMENT_CPU_NAME = "batch_payment_cpu_name";

	public static final String FA_SETUP_IFSC_DETAILS = "batch_ifsc_details";
	
	protected static final String SAVE_PAYMENT_DETAIL = "create_batch_save_hold";

	public static final String PAYMENT_CPU_EMAIL_ID = "create_bathc_cpu_email_id";
	
	public static final String SET_UP_PAYMENT_CPU_CODE = "set_up_payment_cpu_code_batch";
	
	public static final String SET_UP_PAYEE_NAME = "set_up_payee_name";
	
	public static final String GET_PAYEE_NAME_DETAILS = "get_payee_name_details";
	
	protected static final String SAVE_PAYMENT_DETAILS_FOR_PENAL = "create_batch_save_button";
	
	public static final String CHANGE_PAYABLE_AT_BATCH = "change payable at in lot screen";
	
	public static final String UPDATE_PAYEE_NAME = "update_payee_name";
	
	public static final String VIEW_LINKED_POLICY_BATCH = "view_linked_policy_batch";
	
	public static final String PAYEE_BANK_DETAILS = "Payee Bank Details";

	public static final String BATCH_VERIFICATION_ACCOUNT_DETAILS = "Batch Verification Account Details"; 
	
	public static final String BATCH_VERIFICATION_ACCOUNT_DETAILS_SAVE = "Save Batch Verification Account Details"; 
	
	public static final String RELOAD_SEARCH_RESULT_FOR_SUBMIT = "reload search result for submit";
	
	public static final String BATCH_VERIFICATION_ACCOUNT_DETAILS_SETTLED = "Batch Verification Account Details Settled";
	
	@EJB
	private SearchCreateBatchService createBatchService;	
	
	
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
	
	@EJB
	private CreateAndSearchLotService searchService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private DBCalculationService dbService;
	
	
	@EJB
	private VerficationAccountDetailsService verficationAccountDetailsService;
	
	public void searchPendingCases(@Observes @CDIEvent(SEARCH_PENDING_CASES_BUTTON_CLICK) final ParameterDTO parameters) {
		
		DBCalculationService dbcalService = new DBCalculationService();
		view.buildCreatePendingBatchLayout(dbcalService.getPendingBatchLotCases());
	}
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCreateBatchFormDTO searchFormDTO = (SearchCreateBatchFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		if(null != searchFormDTO && null != searchFormDTO.getSearchTabType() && (SHAConstants.QUICK_SEARCH.equalsIgnoreCase(searchFormDTO.getSearchTabType()))){
			
			view.listForQuick(createBatchService.QuickSearch(searchFormDTO,userName,passWord));
		}
		else
		{
			view.list(createBatchService.search(searchFormDTO,userName,passWord));
		}
		
		
	}
	
	public void generateBatchNumber(@Observes @CDIEvent(CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING) final ParameterDTO parameters) {
		List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		Window popUp = (Window)parameters.getSecondaryParameter(1, Window.class);
		String menuString = (String)parameters.getSecondaryParameter(2, String.class);
		SearchCreateBatchFormDTO searchDTO = (SearchCreateBatchFormDTO)parameters.getSecondaryParameter(3, SearchCreateBatchFormDTO.class);
		Map<String, Object> lotCreationMap = createBatchService.updateBatchNumberForPaymentProcessing(tableDTOList,typeOfService, menuString);
		String holdPendingFlag = (String)lotCreationMap.get(SHAConstants.HOLD_PENDING_SERVICE);
		if((SHAConstants.N_FLAG).equalsIgnoreCase(holdPendingFlag))
		{
			String strBatchNo = (String)lotCreationMap.get(SHAConstants.BATCH_NUMBER);
			if(null != strBatchNo)
			{
				//CreateAndSearchLotTableDTO tableDTO = createBatchService.getBatchDetails(strBatchNo);
				List<CreateAndSearchLotTableDTO> tableDTO = createBatchService.getBatchDetailsList(strBatchNo);
				lotCreationMap.put(SHAConstants.SUCCESS_TABLE, tableDTO);
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
					if(null != createAndSearchLotTableDTO.getCheckBoxStatus() && ("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						createBatchService.savePaymentDetails(createAndSearchLotTableDTO);
					}
				}
				view.buildSuccessLayout(lotCreationMap,popUp,searchDTO);
			}
			
		}
		else
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
				if(null != createAndSearchLotTableDTO.getCheckBoxStatus() && ("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					createBatchService.savePaymentDetails(createAndSearchLotTableDTO);
				}
			}
			view.buildSuccessLayout(lotCreationMap,popUp,searchDTO);
		}
	}
	
	
	public void generateBatchNumberBancs(@Observes @CDIEvent(CREATE_BATCH_PAYMENT_LVL1_SUBMIT) final ParameterDTO parameters) {
		CreateAndSearchLotTableDTO tableDTOList = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
//		Window popUp = (Window)parameters.getSecondaryParameter(1, Window.class);
		String presenter = (String)parameters.getSecondaryParameter(1, String.class);
		String menu = (String)parameters.getSecondaryParameter(2, String.class);
		
		Integer totRec = (Integer)parameters.getSecondaryParameter(3, Integer.class);
		
		if(presenter.equalsIgnoreCase("submit")) {
				createBatchService.updatePaymentLevel1(tableDTOList,menu);
		}
				view.buildSuccessLayoutBancs(presenter,popup, totRec);
	}
	
	
	public void generateBatchNumberBancslvl2(@Observes @CDIEvent(CREATE_BATCH_PAYMENT_LVL1_HOLD) final ParameterDTO parameters) {
		CreateAndSearchLotTableDTO tableDTOList = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		String holdRemarks = (String)parameters.getSecondaryParameter(0, String.class);
		Window popUp = (Window)parameters.getSecondaryParameter(1, Window.class);
		String presenter = "Hold";
				createBatchService.updateHold(holdRemarks,tableDTOList);
				view.buildSuccessLayoutForHoldBancs(presenter,popUp);
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
		
		CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDTO.getIntimationNo());
		Long documentReceivedId = null != tableDTO.getDocumentReceivedFrom() && SHAConstants.DOC_RECEIVED_FROM_INSURED.equalsIgnoreCase(tableDTO.getDocumentReceivedFrom()) ? 1541l : 1542l;
		List<PreviousAccountDetailsDTO> previousAcctDetailsList = dbCalculationService.getPreviousAccountDetails(claim.getIntimation().getInsured().getInsuredId(),documentReceivedId);
		if(null != previousAcctDetailsList && !previousAcctDetailsList.isEmpty())
		{
			tableDTO.setPreviousAccntDetailsList(previousAcctDetailsList);
			view.populatePreviousAccntDetails(tableDTO);
		}
		else
		{
			view.prevsAcntDtlsAlert();
		}
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showDetailsPopup(@Observes @CDIEvent(CREATE_BATCH_SHOW_DETAILS) final ParameterDTO parameters) {
		CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();	
		StringBuffer productCodeWithName;
		
		List<CreateAndSearchLotTableDTO> tableDTOList = createBatchService.getBatchDetails(tableDTO);
		
		List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
			
			if(createAndSearchLotTableDTO.getLegalFirstName()!=null && !createAndSearchLotTableDTO.getLegalFirstName().isEmpty()){
				SelectValue payeeName = new SelectValue();
				payeeName.setId(0l);
				payeeName.setValue(createAndSearchLotTableDTO.getLegalFirstName());
				createAndSearchLotTableDTO.setPayeeName(payeeName);
				createAndSearchLotTableDTO.setPayeeNameStr(createAndSearchLotTableDTO.getLegalFirstName());
			}
			
			
		Reimbursement reimbObj =createRodService.getReimbursementObject(createAndSearchLotTableDTO.getRodNo());
		
		createAndSearchLotTableDTO.setPatientName(reimbObj.getClaim().getIntimation().getInsured().getInsuredName()); 
		
		if(null != reimbObj){
		
		DocAcknowledgement docAck = createRodService.getDocAckBasedOnROD(reimbObj.getKey());
		
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
		BPMClientContext bpmClientContext = new BPMClientContext();
		/*Below code commented due to security issue
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
	
	
	@SuppressWarnings({ "deprecation" })
	public void showViewLinkedPolicy(@Observes @CDIEvent(VIEW_LINKED_POLICY_BATCH) final ParameterDTO parameters) {
		
		CreateAndSearchLotTableDTO createLotDTO  = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		
		Intimation intimationDetails = createRodService.getIntimationByNo(createLotDTO.getIntimationNo());
	
		Insured insuredDetails = insuredService.getInsuredByInsuredKey(intimationDetails.getInsured().getKey()); 
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, String> linkPolicyDetails = dbCalculationService.getLinkedPolicyDetails(createLotDTO.getPolicyNo(), insuredDetails.getLinkEmpNumber());
		
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
		
		TextField paymentMadeAt = new TextField();
		paymentMadeAt.setCaption("PAYMENT TO BE MADE");
		paymentMadeAt.setValue(linkPolicyDetails.get(SHAConstants.PAYMENT_PARTY));
		paymentMadeAt.setWidth("100%");
		paymentMadeAt.setReadOnly(true);
		paymentMadeAt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		paymentMadeAt.setNullRepresentation("-");
		
		FormLayout linkedPolicyDetails = new FormLayout(corporateName,insuredName,policyNumber,mainMemberName,mainMemberId,paymentMadeAt);
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

	
	private BeanItemContainer<SelectValue> getValuesForNameDropDown( CreateAndSearchLotTableDTO tableDTO)
	{
		
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
		
		Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
			
			payeeNameValueContainer.addAll(payeeValueList);
		}
		else
		{
			payeeNameValueContainer = dbCalculationService.getPayeeName(intimation.getPolicy().getKey(),
					intimation.getKey());
		}
		
		return payeeNameValueContainer;
		}
		
		return null;
		
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		CreateAndSearchLotTableDTO createBatchTableDto = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
		view.populatePreviousPaymentDetails(tableDTO,createBatchTableDto);
		
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
	 
		public void setPaymentCpuName(
				@Observes @CDIEvent(GET_PAYMENT_CPU_NAME) final ParameterDTO parameters) {
			String paymentCpuCode = (String) parameters.getPrimaryParameter();
			TmpCPUCode masCpuCode = null;
			try {
				masCpuCode = claimService.getMasCpuCode(Long.parseLong(paymentCpuCode));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			if(masCpuCode!=null){
				updatePaymentDetailTableDTO.setPaymentCpuName(masCpuCode.getDescription());
				view.setPaymentCpu(updatePaymentDetailTableDTO);
			}
		}
		
		public void setupIFSCDetails(
				@Observes @CDIEvent(FA_SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void savePaymentDetails(@Observes @CDIEvent(SAVE_PAYMENT_DETAIL) final ParameterDTO parameters) {
			List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
			String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
				searchService.savePaymentDetails(createAndSearchLotTableDTO, Boolean.TRUE);
			}
			view.buildSuccessLayout("Records has been saved successfully!");
		}
		
		public void getEmailIdForpaymentCpu(@Observes @CDIEvent(PAYMENT_CPU_EMAIL_ID) final ParameterDTO parameters){
			
			String cpucode = (String) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			searchService.saveEmailForPaymentCpu(cpucode, updatePaymentDetailTableDTO);
			
		}
		
		public void setupPaymentcpuCodeDetails(
				@Observes @CDIEvent(SET_UP_PAYMENT_CPU_CODE) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpPaymentCpuCodeDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void setupPayeeNameDetails(
				@Observes @CDIEvent(SET_UP_PAYEE_NAME) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpPayeeNameDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void getPayeeNameDetails(
				@Observes @CDIEvent(GET_PAYEE_NAME_DETAILS) final ParameterDTO parameters) {
			CreateAndSearchLotTableDTO createAndSearchLotDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
			
			List<ViewSearchCriteriaTableDTO> payeeNameList = new ArrayList<ViewSearchCriteriaTableDTO>();
			if(createAndSearchLotDTO.getDocumentReceivedFrom()!=null && createAndSearchLotDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
				ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
				if(createAndSearchLotDTO.getHospitalPayableAt() != null){
					
					dto.setPayeeName(createAndSearchLotDTO.getHospitalPayableAt());
					dto.setRelationship("");
				}else if(createAndSearchLotDTO.getHospitalName() != null){
					dto = new ViewSearchCriteriaTableDTO();
					dto.setPayeeName(createAndSearchLotDTO.getHospitalName());
					dto.setRelationship("");
				}
				payeeNameList.add(dto);
			}else{
				payeeNameList = getPayeeNameList(createAndSearchLotDTO);
				
				Intimation intimation = intimationService.getIntimationByNo(createAndSearchLotDTO.getIntimationNo());
				if(null != intimation && null != intimation.getPolicy().getProduct().getKey() &&
						(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()))){
					if(createAndSearchLotDTO.getDocumentReceivedFrom()!=null && createAndSearchLotDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)){
						for(int value = 0 ; value< payeeNameList.size() ; value++)
						{
							payeeNameList.remove(payeeNameList.get(2));
							break;
						}
					}
				}
			}
			if(null != payeeNameList && !payeeNameList.isEmpty()){
				
				createAndSearchLotDTO.setPayeeNameList(payeeNameList);
			}
		}
		
		private List<ViewSearchCriteriaTableDTO> getPayeeNameList( CreateAndSearchLotTableDTO tableDTO)
		{
			List<ViewSearchCriteriaTableDTO> resultList = new ArrayList<ViewSearchCriteriaTableDTO>();
			Policy policy = policyService.getPolicy(tableDTO.getPolicyNo());
			if(null != policy)
			{
			
			
			/*if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(tableDTO.getTypeOfClaim()))
			{
				SelectValue hospitalName = new SelectValue();
				hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
				hospitalName.setValue(tableDTO.getHospitalName());
				payeeValueList.add(hospitalName);
			}*/
			
			Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
			List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
				String proposerName =  policy.getProposerFirstName();
				List<Insured> insuredList = policy.getInsured();
				
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				for (int i = 0; i < insuredList.size(); i++) {
					
					Insured insured = insuredList.get(i);
					SelectValue selectValue = new SelectValue();
					SelectValue payeeValue = new SelectValue();
					selectValue.setId(Long.valueOf(String.valueOf(i)));
					selectValue.setValue(insured.getInsuredName());
					selectValue.setCommonValue(insured.getRelationshipwithInsuredId() != null?insured.getRelationshipwithInsuredId().getValue():"");
					
					payeeValue.setId(Long.valueOf(String.valueOf(i)));
					payeeValue.setValue(insured.getInsuredName());
					payeeValue.setCommonValue(insured.getRelationshipwithInsuredId() != null?insured.getRelationshipwithInsuredId().getValue():"");
					
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
				payeeSelValue.setCommonValue("");
				
				payeeValueList.add(payeeSelValue);
				payeeNameValueContainer.addAll(payeeValueList);
			}
			else
			{
				payeeNameValueContainer = dbCalculationService.getPayeeNameWithRelationship(intimation.getPolicy().getKey(),
						intimation.getKey());
			}
			
			 List<SelectValue> itemIds = payeeNameValueContainer.getItemIds();
			 for (SelectValue selectValue : itemIds) {
				ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
				dto.setPayeeName(selectValue.getValue());
				dto.setRelationship(selectValue.getCommonValue() != null ? selectValue.getCommonValue() :"" );
				resultList.add(dto);
			}
			
			return resultList;
			
			}
			
			return null;
			
		}
		
		public void savePaymentDetailsPenal(@Observes @CDIEvent(SAVE_PAYMENT_DETAILS_FOR_PENAL) final ParameterDTO parameters) {
			List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
			String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
				searchService.savePaymentDetailsPenal(createAndSearchLotTableDTO, Boolean.TRUE);
			}
			view.buildSuccessLayout("Records has been saved successfully!");
		}
		
		public void setupPayableDetails(
				@Observes @CDIEvent(CHANGE_PAYABLE_AT_BATCH) final ParameterDTO parameters) {
			String payableName = (String) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO tableDto = (CreateAndSearchLotTableDTO)parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpPayableDetails(payableName,tableDto);
		}
		
		public void updatePayeeName(@Observes @CDIEvent(UPDATE_PAYEE_NAME) final ParameterDTO parameters) {
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO)parameters.getPrimaryParameter();
			ViewSearchCriteriaTableDTO tableDto = (ViewSearchCriteriaTableDTO)parameters.getSecondaryParameter(0, ViewSearchCriteriaTableDTO.class);
				searchService.updatePayeeName(createAndSearchLotTableDTO,tableDto);
				view.setUpPayeeNameDetails(tableDto,createAndSearchLotTableDTO);
			//view.buildSuccessLayout("Records has been saved successfully!");
		}
		
		public void setupPayeeBankDetails(
				@Observes @CDIEvent(PAYEE_BANK_DETAILS) final ParameterDTO parameters) {
			BankDetailsTableDTO viewSearchCriteriaDTO = (BankDetailsTableDTO) parameters.getPrimaryParameter();
			view.LinkPayeeBankDetails(viewSearchCriteriaDTO);
		}
		
		public void setVerifictaionAccountDetails(
				@Observes @CDIEvent(BATCH_VERIFICATION_ACCOUNT_DETAILS) final ParameterDTO parameters) {
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
//			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(createAndSearchLotTableDTO.getBeneficiaryAcntNo());
			
			List<VerificationAccountDeatilsTableDTO> resultList = dbService.getAccountDetailsForPaymentVerification(createAndSearchLotTableDTO.getBeneficiaryAcntNo(),createAndSearchLotTableDTO.getPayeeNameStr() != null ? createAndSearchLotTableDTO.getPayeeNameStr():createAndSearchLotTableDTO.getLegalFirstName(),createAndSearchLotTableDTO.getIfscCode());
			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = resultList;			
			createAndSearchLotTableDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);			
			/*List<VerificationAccountDeatilsTableDTO> accountDetails = resultList.get(1);
			createAndSearchLotTableDTO.setPaidAccountDeatilsTableDTO(accountDetails);	*/		
		}
		
		@SuppressWarnings("static-access")
		public void saveVerifiedAccount(
				@Observes @CDIEvent(BATCH_VERIFICATION_ACCOUNT_DETAILS_SAVE) final ParameterDTO parameters) {		
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
			verficationAccountDetailsService.submitAccountVerificationDetails(createAndSearchLotTableDTO.getVerificationAccountDeatilsTableDTO(),SHAConstants.BATCH_VERIFICATION);
			createAndSearchLotTableDTO.setVerificationClicked(true);
		}
		
		public void reloadSearchResult(
				@Observes @CDIEvent(RELOAD_SEARCH_RESULT_FOR_SUBMIT) final ParameterDTO parameters) {		
			view.doSearch();
		}
		
		public void setVerifictaionAccountDetailsSettled(
				@Observes @CDIEvent(BATCH_VERIFICATION_ACCOUNT_DETAILS_SETTLED) final ParameterDTO parameters) {
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
//			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(createAndSearchLotTableDTO.getBeneficiaryAcntNo());
			
			List<VerificationAccountDeatilsTableDTO> resultList = dbService.getAccountDetailsForPaymentVerificationSettled(createAndSearchLotTableDTO.getBeneficiaryAcntNo(),createAndSearchLotTableDTO.getPayeeNameStr() != null ? createAndSearchLotTableDTO.getPayeeNameStr():createAndSearchLotTableDTO.getLegalFirstName(),createAndSearchLotTableDTO.getIfscCode());
			/*List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = resultList.get(0);			
			createAndSearchLotTableDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);*/			
			List<VerificationAccountDeatilsTableDTO> accountDetails = resultList;
			createAndSearchLotTableDTO.setPaidAccountDeatilsTableDTO(accountDetails);		
		}
}
