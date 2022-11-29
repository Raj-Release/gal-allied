package com.shaic.claim.reimbursement.createandsearchlot;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerficationAccountDetailsService;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
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

@ViewInterface(CreateAndSearchLotView.class)

public class CreateAndSearchLotPresenter extends AbstractMVPPresenter<CreateAndSearchLotView >{
	
	private static final long serialVersionUID = 1L;
	public static final String CREATE_SEARCH_LOT = "Create_And_Search_Lot";
	public static final String SHOW_EDIT_PAYMENT_DETAILS_VIEW = "Show_edit_payment_details_view";
	public static final String GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING = "generate_lot_no_for_payment_processing";
	public static final String SHOW_VIEW_DOCUMENTS = "show_view_documents";
	
	public static final String BUILD_RESULT_TABLE_LAYOUT_BASED_ON_TYPE = "build_result_table_layout_based_on_type";
	public static final String RESET_INTEREST_RATE = "reset_interest_rate";
	
	public static final String CREATE_AND_LOT_POPULATE_PREVIOUS_ACCT_DETAILS = "create_and_lot_populate_previous_acct_details";
	public static final String FA_SETUP_IFSC_DETAILS = "create_setup_ifsc_details";
	public static final String GET_PAYMENT_CPU_NAME = "create_batch_payment_cpu_name";
	protected static final String SAVE_PAYMENT_DETAIL = "create_lot_save_hold";
	public static final String SET_UP_PAYMENT_CPU_CODE = "set_up_payment_cpu_code_lot";
	public static final String SET_UP_PAYEE_NAME_LOT = "set_up_payee_name_lot";
	
	public static final String GET_PAYEE_NAME_DETAILS = "get_payee_name_details_lot";
	
	public static final String CHANGE_PAYABLE_AT_LOT = "change payable at in lot screen";
	
	public static final String UPDATE_PAYEE_NAME = "update_payee_name_lot";
	public static final String VIEW_LINKED_POLICY = "view_linked_policy"; 
	
	//public static final String LOT_VERIFICATION_ACCOUNT_DETAILS = "Lot Verification Account Details"; 
	
	public static final String LOT_VERIFICATION_ACCOUNT_DETAILS_SAVE = "Save Lot Verification Account Details"; 


	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@EJB
	private CreateAndSearchLotService searchService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private CreateLotQuickSearchService quickSearchService;
	
	@Inject
	private ViewDetails viewDetails;
	@EJB
	private PolicyService policyService;
	
	@Inject
	private PreviousAccountDetailsTable previousAcctDetailsTable;
	
	private Window popup;
	
	@EJB
	private ClaimService claimService;
	
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private VerficationAccountDetailsService verficationAccountDetailsService;
	
	@EJB
	private DBCalculationService dbService;

			
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CREATE_SEARCH_LOT) final ParameterDTO parameters) {
		
		CreateAndSearchLotFormDTO searchFormDTO = (CreateAndSearchLotFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		if(null != searchFormDTO && null != searchFormDTO.getSearchTabType() && (SHAConstants.QUICK_SEARCH.equalsIgnoreCase(searchFormDTO.getSearchTabType()))){
			
			view.listForQuick(quickSearchService.search(searchFormDTO,userName,passWord));
		}
		else
		{
			view.list(searchService.search(searchFormDTO,userName,passWord));
		}
	}

	public void generateLotForPayment(@Observes @CDIEvent(GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING) final ParameterDTO parameters) {
		List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		Window popUp = (Window)parameters.getSecondaryParameter(1, Window.class);
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
			searchService.savePaymentDetails(createAndSearchLotTableDTO, Boolean.FALSE);
		}
		
		Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
		view.buildSuccessLayout(lotCreationMap,popUp);
	}
	
	public void savePaymentDetails(@Observes @CDIEvent(SAVE_PAYMENT_DETAIL) final ParameterDTO parameters) {
		List<CreateAndSearchLotTableDTO> tableDTOList = (List<CreateAndSearchLotTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {
			searchService.savePaymentDetails(createAndSearchLotTableDTO, Boolean.TRUE);
		}
		view.buildSuccessLayout("Records has been saved successfully!");
	}
	
	public void resetInterestRate(@Observes @CDIEvent(RESET_INTEREST_RATE) final ParameterDTO parameters) {
			
		CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
		MastersValue penalIntrestRate = masterService.getPenalIntrestRate(ReferenceTable.PENAL_INTREST);		
		if(null != penalIntrestRate)
		{
			Double doublePenalIntrest = null != penalIntrestRate.getValue() ? Double.valueOf(penalIntrestRate.getValue()) : 0d;
		
		createAndSearchLotTableDTO.setInterestRateForCalculation(doublePenalIntrest);
		}
	}
	
	public void generateResultTable(@Observes @CDIEvent(BUILD_RESULT_TABLE_LAYOUT_BASED_ON_TYPE) final ParameterDTO parameters) {
		String layoutType = (String) parameters.getPrimaryParameter();
		//Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
		//view.buildSuccessLayout(lotCreationMap);
		view.buildResultantTableLayout(layoutType);
	}
	
	
	
	
	@SuppressWarnings({ "deprecation" })
	public void showEditPaymentDetailsView(@Observes @CDIEvent(SHOW_EDIT_PAYMENT_DETAILS_VIEW) final ParameterDTO parameters) {
		
		
		//RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
	//	List<List<PreviousAccountDetailsDTO>> previousAcctDetailsList = populatePreviousAccountDetails(tableDTO.getIntimationNo(), tableDTO.getDocReceivedFrom());
		Claim claim = claimService.getClaimsByIntimationNumber(tableDTO.getIntimationNo());
		Long documentReceivedId = tableDTO.getDocumentReceivedFromId() != null ? tableDTO.getDocumentReceivedFromId() : 1542l;
		List<PreviousAccountDetailsDTO> previousAcctDetailsList = dbCalculationService.getPreviousAccountDetails(claim.getIntimation().getInsured().getInsuredId()
				,documentReceivedId);
		if(null != previousAcctDetailsList && !previousAcctDetailsList.isEmpty())
		{
			tableDTO.setPreviousAccntDetailsList(previousAcctDetailsList);
		}
		popup = new com.vaadin.ui.Window();
		
		final EditPaymentDetailsView paymentDetailsView = new EditPaymentDetailsView();
		//paymentDetailsView.initPresenter(SHAConstants.CREATE_LOT_EDIT_PAYMENT_DETAILS);
		
		paymentDetailsView.initPresenter(SHAConstants.CREATE_LOT_PAYMENT);
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
				if(null != paymentDetailsView.getIsSaveToDB() && paymentDetailsView.getIsSaveToDB())
				{
					Boolean isSuccess = searchService.savePaymentDetails(tableDTO, Boolean.FALSE);
				}
				//Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
				//view.buildSuccessLayout(lotCreationMap);
				//view.buildPaymentDetailsSuccessLayout(isSuccess);
				
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	
	@SuppressWarnings({ "deprecation" })
	public void showViewDocuments(@Observes @CDIEvent(SHOW_VIEW_DOCUMENTS) final ParameterDTO parameters) {
		
		
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
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		view.showClaimsDMS(url);
		
	//	viewDetails.viewUploadedDocumentDetails(intimationNo);
		
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
	public void showViewLinkedPolicy(@Observes @CDIEvent(VIEW_LINKED_POLICY) final ParameterDTO parameters) {
		
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
		
		FormLayout linkedPolicyDetails = new FormLayout(corporateName,policyNumber,mainMemberName,mainMemberId,paymentMadeAt);
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
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	private BeanItemContainer<SelectValue> getValuesForNameDropDown( CreateAndSearchLotTableDTO tableDTO)
	{

		Policy policy = policyService.getPolicy(tableDTO.getPolicyNo());
		if(null != policy)
		{
			Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
			
			BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
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
	
		/*Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){*/
			
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
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_AND_LOT_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		EditPaymentDetailsView editPaymentView = (EditPaymentDetailsView) parameters.getSecondaryParameter(0, EditPaymentDetailsView.class);
		view.populatePreviousPaymentDetails(tableDTO,editPaymentView);
		
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
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
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
		
		public void setupPaymentcpuCodeDetails(
				@Observes @CDIEvent(SET_UP_PAYMENT_CPU_CODE) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) parameters.getSecondaryParameter(0, CreateAndSearchLotTableDTO.class);
			view.setUpPaymentCpuCodeDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void setupPayeeNameDetails(
				@Observes @CDIEvent(SET_UP_PAYEE_NAME_LOT) final ParameterDTO parameters) {
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
				//Added for slowness issue reported for GMC policies - 31-12-2018
			Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
			BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){	
			
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
			
			/*if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(tableDTO.getTypeOfClaim()))
			{
				SelectValue hospitalName = new SelectValue();
				hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
				hospitalName.setValue(tableDTO.getHospitalName());
				payeeValueList.add(hospitalName);
			}*/
			//Commented for slowness issue reported for GMC policies - 31-12-2018
			/*Intimation intimation = intimationService.getIntimationByNo(tableDTO.getIntimationNo());
			
			BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			if(!ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){*/
				
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
				dto.setRelationship(selectValue.getCommonValue() != null?selectValue.getCommonValue():"");
				resultList.add(dto);
			}
			
			return resultList;
			
			}
			
			return null;
			
		}
		
		public void setupPayableDetails(
				@Observes @CDIEvent(CHANGE_PAYABLE_AT_LOT) final ParameterDTO parameters) {
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
		
		/*public void setVerifictaionAccountDetails(
				@Observes @CDIEvent(LOT_VERIFICATION_ACCOUNT_DETAILS) final ParameterDTO parameters) {
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
//			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(createAndSearchLotTableDTO.getBeneficiaryAcntNo());
			List<List<VerificationAccountDeatilsTableDTO>> resultList = dbService.getAccountDetailsForPaymentVerification(createAndSearchLotTableDTO.getBeneficiaryAcntNo(),createAndSearchLotTableDTO.getPayeeNameStr(),createAndSearchLotTableDTO.getIfscCode());
			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = resultList.get(0);			
			createAndSearchLotTableDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);			
			List<VerificationAccountDeatilsTableDTO> accountDetails = resultList.get(1);
			createAndSearchLotTableDTO.setPaidAccountDeatilsTableDTO(accountDetails);
			
		}*/
		
		@SuppressWarnings("static-access")
		public void saveVerifiedAccount(
				@Observes @CDIEvent(LOT_VERIFICATION_ACCOUNT_DETAILS_SAVE) final ParameterDTO parameters) {		
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = (CreateAndSearchLotTableDTO) parameters.getPrimaryParameter();
			verficationAccountDetailsService.submitAccountVerificationDetails(createAndSearchLotTableDTO.getVerificationAccountDeatilsTableDTO(),SHAConstants.LOT_VERIFICATION);
			createAndSearchLotTableDTO.setVerificationClicked(true);
		}
}
