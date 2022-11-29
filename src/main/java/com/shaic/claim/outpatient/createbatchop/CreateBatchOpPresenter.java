package com.shaic.claim.outpatient.createbatchop;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
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
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OPUploadPaymentDetails;
import com.shaic.domain.outpatient.OutpatientService;
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

@ViewInterface(CreateBatchOpView.class)

public class CreateBatchOpPresenter extends AbstractMVPPresenter<CreateBatchOpView >{
	
	private static final long serialVersionUID = 1L;
	public static final String CREATE_SEARCH_OP = "Create_Search_Op";
	public static final String SHOW_EDIT_PAYMENT_DETAILS_VIEW_OP = "Show_edit_payment_details_view_op";
	public static final String GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING_OP = "generate_lot_no_for_payment_processing_op";
	public static final String SHOW_VIEW_DOCUMENTS_OP = "show_view_documents_op";
	
	public static final String BUILD_RESULT_TABLE_LAYOUT_BASED_ON_SEARCH_OP = "build_result_table_layout_based_on_search_op";
	public static final String RESET_OP_INTEREST_RATE = "reset_OP_interest_rate";
	
	public static final String CREATE_AND_LOT_POPULATE_PREVIOUS_ACCT_DETAILS_OP = "create_and_lot_populate_previous_acct_details_op";
	public static final String FA_SETUP_IFSC_DETAILS_OP = "create_setup_ifsc_details_op";
	public static final String GET_PAYMENT_CPU_NAME_OP = "create_batch_payment_cpu_name_op";
	protected static final String SAVE_PAYMENT_DETAIL_OP = "create_lot_save_hold_op";
	public static final String SET_UP_PAYMENT_CPU_CODE_OP = "set_up_payment_cpu_code_lot_op";
	public static final String SET_UP_PAYEE_NAME_LOT_OP = "set_up_payee_name_lot_op";
	
	public static final String GET_PAYEE_NAME_DETAILS_OP = "get_payee_name_details_lot_op";
	
	public static final String CHANGE_PAYABLE_AT_LOT_OP = "change payable at in lot screen_op";
	
	public static final String UPDATE_PAYEE_NAME_OP = "update_payee_name_lot_op";
	public static final String VIEW_LINKED_POLICY_OP = "view_linked_policy_op";
	
	public static final String UPDATE_CHEQUE_DETAILS_OP = "update_cheque_details_op";
	
	public static final String UPDATE_OP_PAYMENT_DETAILS_BATCH = "update_op_payment_detials_batch";
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@EJB
	private CreateBatchOpService searchService;
	
	@EJB
	private MasterService masterService;
	
	
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
	private OutpatientService outPatientService;

			
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CREATE_SEARCH_OP) final ParameterDTO parameters) {
		
		CreateBatchOpDTO searchFormDTO = (CreateBatchOpDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		
			view.list(searchService.search(searchFormDTO,userName,passWord));
		
	}

	public void generateLotForPayment(@Observes @CDIEvent(GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING_OP) final ParameterDTO parameters) {
		List<CreateBatchOpTableDTO> tableDTOList = (List<CreateBatchOpTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		Window popUp = (Window)parameters.getSecondaryParameter(1, Window.class);
		for (CreateBatchOpTableDTO createAndSearchLotTableDTO : tableDTOList) {
			searchService.savePaymentDetails(createAndSearchLotTableDTO, Boolean.FALSE);
		}
		
		Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
		view.buildSuccessLayout(lotCreationMap,popUp);
	}
	
	public void savePaymentDetails(@Observes @CDIEvent(SAVE_PAYMENT_DETAIL_OP) final ParameterDTO parameters) {
		List<CreateBatchOpTableDTO> tableDTOList = (List<CreateBatchOpTableDTO>) parameters.getPrimaryParameter();
		String typeOfService = (String)parameters.getSecondaryParameter(0, String.class);
		for (CreateBatchOpTableDTO createAndSearchLotTableDTO : tableDTOList) {
			searchService.savePaymentDetails(createAndSearchLotTableDTO, Boolean.TRUE);
		}
		view.buildSuccessLayout("Records has been saved successfully!");
	}
	
	public void resetInterestRate(@Observes @CDIEvent(RESET_OP_INTEREST_RATE) final ParameterDTO parameters) {
			
		CreateBatchOpTableDTO createAndSearchLotTableDTO = (CreateBatchOpTableDTO) parameters.getPrimaryParameter();
		MastersValue penalIntrestRate = masterService.getPenalIntrestRate(ReferenceTable.PENAL_INTREST);		
		if(null != penalIntrestRate)
		{
			Double doublePenalIntrest = null != penalIntrestRate.getValue() ? Double.valueOf(penalIntrestRate.getValue()) : 0d;
		
		createAndSearchLotTableDTO.setInterestRateForCalculation(doublePenalIntrest);
		}
	}
	
	public void generateResultTable(@Observes @CDIEvent(BUILD_RESULT_TABLE_LAYOUT_BASED_ON_SEARCH_OP) final ParameterDTO parameters) {
		SelectValue layoutType = (SelectValue) parameters.getPrimaryParameter();
		//Map<String, Object> lotCreationMap = searchService.updateLotNumberForPaymentProcessing(tableDTOList);
		//view.buildSuccessLayout(lotCreationMap);
		view.buildResultantTableLayout(layoutType);
	}
	
	
	
	
	@SuppressWarnings({ "deprecation" })
	public void showEditPaymentDetailsView(@Observes @CDIEvent(SHOW_EDIT_PAYMENT_DETAILS_VIEW_OP) final ParameterDTO parameters) {/*
		
		
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
			*//**
			 * 
			 *//*
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
	*/}
	
	
	
	@SuppressWarnings({ "deprecation" })
	public void showViewDocuments(@Observes @CDIEvent(SHOW_VIEW_DOCUMENTS_OP) final ParameterDTO parameters) {
		
		
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
	public void showViewLinkedPolicy(@Observes @CDIEvent(VIEW_LINKED_POLICY_OP) final ParameterDTO parameters) {
		
		CreateBatchOpTableDTO createLotDTO  = (CreateBatchOpTableDTO) parameters.getPrimaryParameter();
		
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
	
	private BeanItemContainer<SelectValue> getValuesForNameDropDown( CreateBatchOpTableDTO tableDTO)
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
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_AND_LOT_POPULATE_PREVIOUS_ACCT_DETAILS_OP) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		//EditPaymentDetailsView editPaymentView = (EditPaymentDetailsView) parameters.getSecondaryParameter(0, EditPaymentDetailsView.class);
		//view.populatePreviousPaymentDetails(tableDTO,editPaymentView);
		
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
				@Observes @CDIEvent(FA_SETUP_IFSC_DETAILS_OP) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			view.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}

		public void setPaymentCpuName(
				@Observes @CDIEvent(GET_PAYMENT_CPU_NAME_OP) final ParameterDTO parameters) {
			String paymentCpuCode = (String) parameters.getPrimaryParameter();
			TmpCPUCode masCpuCode = null;
			try {
				masCpuCode = claimService.getMasCpuCode(Long.parseLong(paymentCpuCode));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			if(masCpuCode!=null){
				updatePaymentDetailTableDTO.setPaymentCpuName(masCpuCode.getDescription());
				view.setPaymentCpu(updatePaymentDetailTableDTO);
			}
		}
		
		public void setupPaymentcpuCodeDetails(
				@Observes @CDIEvent(SET_UP_PAYMENT_CPU_CODE_OP) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			view.setUpPaymentCpuCodeDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void setupPayeeNameDetails(
				@Observes @CDIEvent(SET_UP_PAYEE_NAME_LOT_OP) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			view.setUpPayeeNameDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		}
		
		public void getPayeeNameDetails(
				@Observes @CDIEvent(GET_PAYEE_NAME_DETAILS_OP) final ParameterDTO parameters) {
			CreateBatchOpTableDTO createAndSearchLotDTO = (CreateBatchOpTableDTO) parameters.getPrimaryParameter();
			
			List<ViewSearchCriteriaTableDTO> payeeNameList = new ArrayList<ViewSearchCriteriaTableDTO>();
			if(createAndSearchLotDTO.getDocumentReceivedFrom()!=null && createAndSearchLotDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
				ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
				if(createAndSearchLotDTO.getHospitalPayableAt() != null){
					
					dto.setPayeeName(createAndSearchLotDTO.getHospitalPayableAt());
				}else if(createAndSearchLotDTO.getHospitalName() != null){
					dto = new ViewSearchCriteriaTableDTO();
					dto.setPayeeName(createAndSearchLotDTO.getHospitalName());
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
		
		private List<ViewSearchCriteriaTableDTO> getPayeeNameList( CreateBatchOpTableDTO tableDTO)
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
				payeeNameValueContainer = dbCalculationService.getPayeeName(intimation.getPolicy().getKey(),
						intimation.getKey());
			}
			
			 List<SelectValue> itemIds = payeeNameValueContainer.getItemIds();
			 for (SelectValue selectValue : itemIds) {
				ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
				dto.setPayeeName(selectValue.getValue());
				resultList.add(dto);
			}
			
			return resultList;
			
			}
			
			return null;
			
		}
		
		public void setupPayableDetails(
				@Observes @CDIEvent(CHANGE_PAYABLE_AT_LOT_OP) final ParameterDTO parameters) {
			String payableName = (String) parameters.getPrimaryParameter();
			CreateBatchOpTableDTO tableDto = (CreateBatchOpTableDTO)parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			view.setUpPayableDetails(payableName,tableDto);
		}
		
		public void updatePayeeName(@Observes @CDIEvent(UPDATE_PAYEE_NAME_OP) final ParameterDTO parameters) {
			CreateBatchOpTableDTO createAndSearchLotTableDTO = (CreateBatchOpTableDTO)parameters.getPrimaryParameter();
			ViewSearchCriteriaTableDTO tableDto = (ViewSearchCriteriaTableDTO)parameters.getSecondaryParameter(0, ViewSearchCriteriaTableDTO.class);
				searchService.updatePayeeName(createAndSearchLotTableDTO,tableDto);
				view.setUpPayeeNameDetails(tableDto,createAndSearchLotTableDTO);
			//view.buildSuccessLayout("Records has been saved successfully!");
		}
		
		public void updateChequeDetails(@Observes @CDIEvent(UPDATE_CHEQUE_DETAILS_OP) final ParameterDTO parameters){
			String intimationNo = (String)parameters.getPrimaryParameter();
			OPIntimation opIntimation = intimationService.getOPIntimationByNo(intimationNo);
			OPClaim claim = claimService.getOPClaimforIntimation(opIntimation.getKey());
			CreateBatchOpTableDTO opdetails = (CreateBatchOpTableDTO)parameters.getSecondaryParameter(0, CreateBatchOpTableDTO.class);
			String userName=(String)parameters.getSecondaryParameter(1, String.class);
			OPHealthCheckup opHealthCheckup = claimService.getOpHealthByClaimKey(claim.getKey());
			if(opHealthCheckup != null){
				if(opHealthCheckup.getStatus().getKey() != null 
						&& opHealthCheckup.getStatus().getKey().equals(ReferenceTable.OP_APPROVE)){
				opHealthCheckup.setChequeNo(opdetails.getChequeNo() != null ? opdetails.getChequeNo() : "");
				if(opdetails.getIfscCode() != null){
					BankMaster cityName = masterService.getBankDetails(opdetails.getIfscCode());
					if (null != cityName)
					{
						opHealthCheckup.setBankId(cityName.getKey());
						opHealthCheckup.setIfscCode(opdetails.getIfscCode());
					}	
				}
				opHealthCheckup.setChequeDate(opdetails.getChequeDate());
				opHealthCheckup.setModifiedBy(userName);
				opHealthCheckup.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				Status status=new Status();
				status.setKey(ReferenceTable.OP_PAYMENT_SETTLED);
				opHealthCheckup.setStatus(status);
				claim.setModifiedBy(userName);
				//opHealthCheckup.setSettlementLetterFlag(SHAConstants.YES_FLAG);commented as per Narshima - 29-04-2020
				opHealthCheckup.setSettlementLetterFlag(SHAConstants.N_FLAG);
				opHealthCheckup.setBatchNumber(opdetails.getBatchNumber());
				claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				searchService.updateChequeDetails(opHealthCheckup,claim);
				outPatientService.updateOPClaimDetails(claim);
			}
//				view.buildSuccessLayout("File Upload Successfully");
			}
			
		}
		
		public void updatePaymentdetailsBatch(@Observes @CDIEvent(UPDATE_OP_PAYMENT_DETAILS_BATCH) final ParameterDTO parameters){
			
			OPUploadPaymentDetails opUploadPaymentDtls = (OPUploadPaymentDetails) parameters.getPrimaryParameter();
			outPatientService.updatePaymentDtlsBatch(opUploadPaymentDtls);
			
		}
}
