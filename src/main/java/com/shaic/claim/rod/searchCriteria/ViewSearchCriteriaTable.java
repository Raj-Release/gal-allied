package com.shaic.claim.rod.searchCriteria;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.outpatient.processOP.pages.assessmentsheet.OPClaimAssessmentPagePresenter;
import com.shaic.claim.outpatient.processOP.pages.claimDecision.OPClaimDecisionPagePresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPagePresenter;
import com.shaic.claim.rod.wizard.forms.AddBankDetailsTable;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claims.reibursement.rod.UploadNEFTDetails.UploadNEFTDetailsPresenter;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAditionalDocumentsPaymentInfoPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview.PAClaimAprNonHosReviewPagePresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview.PANonHospFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp.PAHealthBillingHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization.PAHealthFinancialHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPagePresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODDocumentDetailsPresenter;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchPresenter;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailPresenter;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewSearchCriteriaTable extends GBaseTable<ViewSearchCriteriaTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"ifscCode","bankName", "branchName", "address"}; 
	
	private Window popup;
	
	private ViewSearchCriteriaTableDTO viewSearchCriteriaTableDTO;
	
	private String presenterString;
	
	private AddBankDetailsTable bankDetailsObj;
	
	private EditPaymentDetailsView view;
	
	private UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO;
	
	private CreateAndSearchLotTableDTO createAndSearchLotTableDTO;
	
	private PreauthDTO preauthDto;
	
	private AddBanksDetailsTableDTO addBanksDetailsTableDTO;
	
	private NomineeDetailsDto nomineeDto;
	
	/*@Inject
	private CreateRODDocumentDetailsPage createRODDocumentDetailsPage;*/
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	
	public void initPresenter(String presenterString, EditPaymentDetailsView view,UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO,CreateAndSearchLotTableDTO createAndSearchLotTableDTO,AddBanksDetailsTableDTO addBanksDetailsTableDTO)
	{
		this.presenterString = presenterString;
		this.view = view;
		this.updatePaymentDetailTableDTO= updatePaymentDetailTableDTO;
		this.createAndSearchLotTableDTO= createAndSearchLotTableDTO;
		this.addBanksDetailsTableDTO = addBanksDetailsTableDTO;
	}
	
	public void setBankDetailsObj(AddBankDetailsTable bankDetailsObj){
		this.bankDetailsObj = bankDetailsObj;
	}
	
	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<ViewSearchCriteriaTableDTO>(ViewSearchCriteriaTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		 generaterColumn();
	}

	@Override
	public void tableSelectHandler(ViewSearchCriteriaTableDTO t) {
		/*fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, t);
		
		popup.close();*/
		
	}
	private void generaterColumn(){
		table.removeGeneratedColumn("Select");
		
		table.addGeneratedColumn("Select", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnSelect = new Button("Select");
				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
				final ViewSearchCriteriaTableDTO viewSearchCriteriaTableDTO = (ViewSearchCriteriaTableDTO)itemId;
				btnSelect.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						if((SHAConstants.PA_FINANCIAL_APPROVER).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									/*&& preauthDto.getClaimDTO() != null*///IMSSUPPOR-30886 Commented as suggested by Laxmi
									&& ((preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId()))
									/*|| ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(preauthDto.getClaimDTO().getIncidenceFlagValue()))*/)) {
								
								fireViewEvent(PANonHospFinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_FA, viewSearchCriteriaTableDTO);
							}
							else{
								fireViewEvent(PANonHospFinancialReviewPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}
						}
						
						else if((SHAConstants.PA_CLAIM_APPROVAL).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getClaimDTO() != null
									&& ((preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId()))
									||((SHAConstants.DEATH_FLAG).equalsIgnoreCase(preauthDto.getClaimDTO().getIncidenceFlagValue())))) {
								
								fireViewEvent(PAClaimAprNonHosReviewPagePresenter.SETUP_NOMINEE_IFSC_PA_NON_HOSP_CLAIM_APPROVAL, viewSearchCriteriaTableDTO);
							}
							else {
								fireViewEvent(PAClaimAprNonHosReviewPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}
						}
						else if((SHAConstants.FINANCIAL_APPROVER).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId())) {
								
								fireViewEvent(FinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_FA, viewSearchCriteriaTableDTO);
							}
							else {	
								fireViewEvent(FinancialHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}	
						}
						else if((SHAConstants.BILLING_SCREEN).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
								&& preauthDto.getNewIntimationDTO() != null
								&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
								&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
								&& preauthDto.getPreauthDataExtractionDetails() != null
								&& preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
								&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId())) {
						
								fireViewEvent(BillingReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_BILLING, viewSearchCriteriaTableDTO);
							}
							else {
									fireViewEvent(BillingHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
								}
							}	
						else if((SHAConstants.EDIT_PAYMENT_DETAILS).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(EditPaymentDetailsPresenter.LOT_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO,view,popup);
						}
						else if((SHAConstants.CREATE_ROD).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId())) {
							
									fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_NOMINEE_IFSC_DETAILS, viewSearchCriteriaTableDTO);
								}
								else {
									fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
								}
						}
						else if((SHAConstants.PA_CREATE_ROD).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getClaimDTO() != null
									&& ((preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId()))
									||((SHAConstants.DEATH_FLAG).equalsIgnoreCase(preauthDto.getClaimDTO().getIncidenceFlagValue())))) {
								
								fireViewEvent(PACreateRODDocumentDetailsPresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_CROD, viewSearchCriteriaTableDTO);
							}
							else {
								fireViewEvent(PACreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}	
						}
						else if((SHAConstants.PA_HEALTH_BILLING_SCREEN).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getClaimDTO() != null
									&& ((preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId()))
									||((SHAConstants.DEATH_FLAG).equalsIgnoreCase(preauthDto.getClaimDTO().getIncidenceFlagValue())))) {
								
								fireViewEvent(PAHealthBillingReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_HOSP_BILLING, viewSearchCriteriaTableDTO);
							}
							else {	
							
								fireViewEvent(PAHealthBillingHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}	
						}
						else if((SHAConstants.PA_HEALTH_FINANCIAL_APPROVER).equalsIgnoreCase(presenterString))
						{
							if(preauthDto != null 
									&& preauthDto.getNewIntimationDTO() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
									&& preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
									&& preauthDto.getPreauthDataExtractionDetails() != null
									&& preauthDto.getClaimDTO() != null
									&& ((preauthDto.getPreauthDataExtractionDetails().getPatientStatus() != null
									&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(preauthDto.getPreauthDataExtractionDetails().getPatientStatus().getId()))
									|| ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(preauthDto.getClaimDTO().getIncidenceFlagValue())))) {
								
								fireViewEvent(PAHealthFinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_FA, viewSearchCriteriaTableDTO);
							}
							else {
								fireViewEvent(PAHealthFinancialHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
							}	
						}
						
						else if(SHAConstants.OP_IFSC.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(OPClaimAssessmentPagePresenter.OP_IFSC_DETAILS, viewSearchCriteriaTableDTO);
						}
						else if(SHAConstants.UPDATE_PAYMENT_DETAIL.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(UpdatePaymentDetailPresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO,updatePaymentDetailTableDTO);
						}
						else if(SHAConstants.CREATE_LOT.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(CreateAndSearchLotPresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						else if(SHAConstants.CREATE_BATCH.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(SearchCreateBatchPresenter.FA_SETUP_IFSC_DETAILS, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						else if((SHAConstants.UPLOAD_NEFT_DETAILS).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(UploadNEFTDetailsPresenter.SETUP_UPLOAD_NEFT_DETAILS_IFSC_DETAILS, viewSearchCriteriaTableDTO);
						}
						
						else if((SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(AddAditionalDocumentsPaymentInfoPagePresenter.SETUP_PAYMENT_INFO_DOCUMENTS_IFSC_DETAILS, viewSearchCriteriaTableDTO);
						}
						else if((SHAConstants.PA_ADD_ADDITIONAL_PAYMENT_INFO).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoPagePresenter.SETUP_PAYMENT_INFO_DOCUMENTS_IFSC_DETAILS, viewSearchCriteriaTableDTO);
						}
						else if((CreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_CROD).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(CreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_CROD, viewSearchCriteriaTableDTO);
						}
						else if((BillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_BILLING).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(BillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_BILLING, viewSearchCriteriaTableDTO);
						}
						else if((FinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_FA).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(FinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_FA, viewSearchCriteriaTableDTO);
						}
						else if((PACreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_PA_CROD).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PACreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_PA_CROD, viewSearchCriteriaTableDTO);
						}
						else if((PAHealthBillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_BILLING).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAHealthBillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_BILLING, viewSearchCriteriaTableDTO);
						}
						else if((PAHealthFinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_FA).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAHealthFinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_FA, viewSearchCriteriaTableDTO);
						}
						else if((PANonHospFinancialReviewPagePresenter.ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_FA).equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PANonHospFinancialReviewPagePresenter.ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_FA, viewSearchCriteriaTableDTO);
						}
						else if(SHAConstants.PAYMENT_VERIFICATION_LVL2.equalsIgnoreCase(presenterString) ||
								SHAConstants.PAYMENT_VERIFICATION_LVL1.equalsIgnoreCase(presenterString)) {
							bankDetailsObj.setUpAddBankIFSCDetails(viewSearchCriteriaTableDTO);
						}
						
						popup.close();
					}
				});
				return btnSelect;
			}
		});
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view_search_criteria_table-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	public void setWindowObject(Window popup){
		this.popup = popup;
	}


	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}


	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}


	public NomineeDetailsDto getNomineeDto() {
		return nomineeDto;
	}


	public void setNomineeDto(NomineeDetailsDto nomineeDto) {
		this.nomineeDto = nomineeDto;
	}	
	
}