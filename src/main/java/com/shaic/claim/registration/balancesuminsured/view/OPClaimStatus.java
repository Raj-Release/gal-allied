package com.shaic.claim.registration.balancesuminsured.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.mapper.OutpatientMapper;
import com.shaic.claim.policy.search.ui.opsearch.ProcessOPRequestService;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPHCDetails;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OutpatientService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OPClaimStatus extends ViewComponent {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private PolicyService policyService;

	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ProcessOPRequestService processOPRequestService;
	
	@EJB
	private MasterService masterService;

	@Inject 
	private OutpatientService outpatientService;
	
	@Inject
	private Instance<OPInsuredDetailsTable> oPInsuredDetailsTable;

	private OPInsuredDetailsTable oPInsuredDetailsTableObj;
	
	@Inject
	private OPProcessingDetailsTable oPProcessingDetailsTable;
	
	@Inject
	private BillDetailsTable billDetailsTable;
	
	@Inject
	private ViewBillDetails viewBillDetails;

	@Inject
	private OPDocumentDetails oPDocumentDetails; 

	
	private OPHealthCheckup oPhealthCheckup;
	
	private Panel documentDetails;
	
	private VerticalLayout mainLayout;
	
	////private static Window popup;

	public void init(String intimationNo,List<OPHCDetails> oPHCDetailsList) {
		VerticalLayout buildMainLayout = buildMainLayout(intimationNo);
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);
		List<OPInsuredDetailsTableDTO> oPInsuredDetailsTableDTOList=new ArrayList<OPInsuredDetailsTableDTO>();
		for (OPHCDetails oPHCDetails : oPHCDetailsList) {
			OPInsuredDetailsTableDTO oPInsuredDetailsTable=new OPInsuredDetailsTableDTO();
			if(null!=oPHCDetails.getInsured()){
				oPInsuredDetailsTable.setInsuredPatientName(oPHCDetails.getInsured().getInsuredName());
			}else{
				oPInsuredDetailsTable.setInsuredPatientName(intimation.getInsuredPatientName());
			}
			oPInsuredDetailsTable.setCheckupDate((SHAUtils.formatDate(oPHCDetails.getTreatmentDate())));
			oPInsuredDetailsTable.setReasonForCheckup(oPHCDetails.getReasonForVisit());
			oPInsuredDetailsTableDTOList.add(oPInsuredDetailsTable);
		
		}
		oPInsuredDetailsTableObj.setTableList(oPInsuredDetailsTableDTOList);
		
		
	
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout buildMainLayout(final String intimationNo) {
		oPInsuredDetailsTableObj = oPInsuredDetailsTable.get();
		oPInsuredDetailsTableObj.init(" ",false,false);
		oPProcessingDetailsTable.init("", false, false);
		Intimation intimation1=intimationService.getIntimationByNo(intimationNo);
		if(intimation1.getInsured().getPolicy().getProductType().getKey()
				.equals(ReferenceTable.INDIVIDUAL_POLICY)) {
		oPInsuredDetailsTableObj.individualVisibleColumn();
		}
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		String claimType=intimation.getClaimType().getValue().toString();
		String insuredPatientName=intimation.getInsuredPatientName();
		Double claimedAmount=intimationService.getClaimforIntimation(intimation.getKey()).getClaimedAmount();
		Double provisionAmount=intimationService.getClaimforIntimation(intimation.getKey()).getProvisionAmount();
		Long claimKey=intimationService.getClaimforIntimation(intimation.getKey()).getKey();
		 oPhealthCheckup = processOPRequestService
				.getOpHealthByClaimKey(claimKey);
		
		//Setting value for the text fields
		 
		Panel registrationDetails = new Panel();
		registrationDetails.addStyleName(ValoTheme.PANEL_BORDERLESS);
		Label registerdetails=new Label("Registration Details");
		registerdetails.addStyleName("labelHighlight");
		TextField registeredBy = new TextField("Registered By");
		registeredBy.setValue(oPhealthCheckup.getCreatedBy());
		registeredBy.setNullRepresentation("");
		registeredBy.setReadOnly(true);
		TextField registeredOn = new TextField("Registered On");
		if(oPhealthCheckup.getCreatedDate() != null){
			registeredOn.setValue(oPhealthCheckup.getCreatedDate().toLocaleString());
		}
		registeredOn.setReadOnly(true);
		TextField claimTypeTxt = new TextField("Claim Type");
		claimTypeTxt.setValue(claimType);
		claimTypeTxt.setReadOnly(true);
		TextField insuredPatientNametxt= new TextField("Insured Patient Name");
		insuredPatientNametxt.setValue(insuredPatientName);
		insuredPatientNametxt.setReadOnly(true);
		TextField amountClaimed = new TextField("Amount Claimed");
		amountClaimed.setValue(claimedAmount.toString());
		amountClaimed.setReadOnly(true);
		TextField amountProvision = new TextField("Provision Amount");
		amountProvision.setValue(provisionAmount.toString());
		amountProvision.setReadOnly(true);
		FormLayout upperForm = new FormLayout(registerdetails,registeredBy, registeredOn,
				claimTypeTxt, insuredPatientNametxt);
		registrationDetails.setContent(upperForm);
		FormLayout leftUpperForm = new FormLayout(amountClaimed);
		FormLayout rightUpperForm = new FormLayout(amountProvision);
		HorizontalLayout upperHorizontal = new HorizontalLayout(leftUpperForm,
				rightUpperForm);
		documentDetails=new Panel();
		documentDetails.addStyleName(ValoTheme.PANEL_BORDERLESS);
		Label docuDetails=new Label("Document Details");
		docuDetails.addStyleName("labelHighlight");
		TextField documentsRecievedFrom = new TextField(
				"Documents Recieved From ");
		documentsRecievedFrom.setValue(oPhealthCheckup.getDocumentReceivedFromId().getValue());
		documentsRecievedFrom.setReadOnly(true);
		TextField documentsRecievedDate = new TextField(
				"Documents Recieved Date ");
		documentsRecievedDate.setValue(oPhealthCheckup.getDocumentReceivedDate().toString());
		documentsRecievedDate.setReadOnly(true);
		TextField modeofReceipt = new TextField("Mode of Receipt");
		modeofReceipt.setValue(oPhealthCheckup.getModeOfReceipt().toString());
		modeofReceipt.setReadOnly(true);
		Button moreDetails = new Button("more Details...");
		moreDetails.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
			getDocumentDetails(oPhealthCheckup);
			}});
		
		moreDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	//	moreDetails.setEnabled(false);
		FormLayout middleForm = new FormLayout(docuDetails,documentsRecievedFrom,
				documentsRecievedDate, modeofReceipt, moreDetails);
		documentDetails.setContent(middleForm);
		Panel processingDetails = new Panel();
		Label processDetail=new Label("Processing Details");
		processDetail.addStyleName("labelHighlight");
		FormLayout processdetails=new FormLayout(processDetail);
		Button processingMoreDetails=new Button("more details");
		processingMoreDetails.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
			getBillDetails(intimationNo);
			}});
		processingMoreDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		VerticalLayout processingDetailsLayout=new VerticalLayout(processdetails,oPProcessingDetailsTable,processingMoreDetails);
		processingDetailsLayout.setComponentAlignment(processingMoreDetails, Alignment.BOTTOM_RIGHT);
		processingDetailsLayout.setComponentAlignment(processdetails, Alignment.TOP_CENTER);
		processingDetails.addStyleName(ValoTheme.PANEL_BORDERLESS);
		processingDetails.setContent(processingDetailsLayout);
		VerticalLayout vertical = new VerticalLayout(registrationDetails,oPInsuredDetailsTableObj,upperHorizontal, documentDetails,
				processingDetails);
		vertical.setSpacing(true);
		Integer claimedAmt=0;
		Integer nonPayableAMount=0;
		Integer payableAmount=0;
		OPProcessingDetailsTableDTO oPProcessingDetailsTableDTO =new OPProcessingDetailsTableDTO();
		List<OPProcessingDetailsTableDTO> oPProcessingDetailsTableDTOList=new ArrayList<OPProcessingDetailsTableDTO>();
		List<OPBillDetailsDTO> list=getBillDetailsDto(intimationNo);
		for (OPBillDetailsDTO opBillDetailsDTO : list) {
			claimedAmt += Integer.parseInt(opBillDetailsDTO.getClaimedAmount());
			nonPayableAMount += Integer.parseInt(opBillDetailsDTO.getNonPayableAmt());
			payableAmount += Integer.parseInt(opBillDetailsDTO.getPayableAmt());
		}
			
		
		
		oPProcessingDetailsTableDTO.setType(claimType);
		oPProcessingDetailsTableDTO.setClaimedAmount(claimedAmt.toString());
		oPProcessingDetailsTableDTO.setNonPayable(nonPayableAMount.toString());
		oPProcessingDetailsTableDTO.setPayableAmt(payableAmount.toString());
		oPProcessingDetailsTableDTO.setStatus(oPhealthCheckup.getStatus().getProcessValue());
		oPProcessingDetailsTableDTO.setStatus("OP Registered");
		if(oPhealthCheckup.getClaim().getClaimType() != null && oPhealthCheckup.getClaim().getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)) {
			oPProcessingDetailsTableDTO.setStatus("HC Registered");
		}
		if(oPhealthCheckup.getClaim().getStatus().getKey().equals(ReferenceTable.OP_APPROVE)) {
			oPProcessingDetailsTableDTO.setStatus("Approved");
		} else if(oPhealthCheckup.getClaim().getStatus().getKey().equals(ReferenceTable.OP_REJECT)) {
			oPProcessingDetailsTableDTO.setStatus("Rejected");
		}
		
		oPProcessingDetailsTableDTO.setRemarks(oPhealthCheckup.getApprovalRemarks());
		oPProcessingDetailsTableDTOList.add(oPProcessingDetailsTableDTO);
		oPProcessingDetailsTable.setTableList(oPProcessingDetailsTableDTOList);
		
		return vertical;

	}
	
	public void getDocumentDetails(OPHealthCheckup oPhealthCheckup){
		oPDocumentDetails.init(oPhealthCheckup);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Documents Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(oPDocumentDetails);
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

	
public void getBillDetails(String intimationNo){
	if(intimationNo!=null){
	viewBillDetails.init(getBillDetailsDto(intimationNo));
	}
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
	

}
public List<OPBillDetailsDTO> getBillDetailsDto(String intimationNo){
	
	BeanItemContainer<SelectValue> selectValueContainer = masterService.getSelectValueContainer(ReferenceTable.OP_BILL_DETAILS);
	OutpatientMapper mapper = new OutpatientMapper();
	
	Long intimationKey = intimationService.getIntimationByNo(
			intimationNo).getKey();
	
	Claim claim = intimationService
			.getClaimforIntimation(intimationKey);
	OPHealthCheckup opHealthByClaimKey = outpatientService.getOpHealthByClaimKey(claim.getKey());
	List<OPBillDetailsDTO> billDetailsDTOList = new ArrayList<OPBillDetailsDTO>();
	List<OPDocumentBillEntry> opBillEntryDetails = outpatientService.getOpBillEntryDetails(opHealthByClaimKey.getKey());
	
	for (OPDocumentBillEntry opDocumentBillEntry : opBillEntryDetails) {
		billDetailsDTOList.add( mapper.getOPBillEntryDTO(opDocumentBillEntry));
	}
	
	for (OPBillDetailsDTO billEntryDTO : billDetailsDTOList) {
		Integer amt =  SHAUtils.getIntegerFromString(billEntryDTO.getClaimedAmount())  -  SHAUtils.getIntegerFromString(billEntryDTO.getNonPayableAmt()) ;
		billEntryDTO.setPayableAmt(String.valueOf(amt));
		billEntryDTO.setBillDateStr(SHAUtils.formatDate(billEntryDTO.getBillDate()));
	}
	
	List<SelectValue> masterList = selectValueContainer.getItemIds();
	for (SelectValue selectValue : masterList) {
		for (OPBillDetailsDTO billDetailsDTO : billDetailsDTOList) {
			if(selectValue.getId().equals(billDetailsDTO.getMasterId())) {
				billDetailsDTO.setDetails(selectValue.getValue());
			}
		}
	}
	
	
	return billDetailsDTOList;
	
}
	

}
