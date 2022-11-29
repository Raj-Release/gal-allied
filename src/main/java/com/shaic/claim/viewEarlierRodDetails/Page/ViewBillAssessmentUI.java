

package com.shaic.claim.viewEarlierRodDetails.Page;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Table.HospitalisationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewBillDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewHospitalizationListenerTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewPreHospitalizationTable;
import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.BillingHospitalisation;
import com.shaic.domain.BillingPostHospitalisation;
import com.shaic.domain.BillingPreHospitalisation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasBillDetailsType;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ViewBillRemarks;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewBillAssessmentUI extends ViewComponent {
	

	private static final long serialVersionUID = 1L;
	
	private Window popup;
	
	@Inject
	private Instance<ViewBillDetailsTable> viewBillDetailsTableInstance;
	
	private ViewBillDetailsTable viewBillDetailsTableObj;
	
	@Inject
	private Instance<ViewPreHospitalizationTable> preHospitalizationInstance;
	
	private ViewPreHospitalizationTable preHospitalizationTable;
	
	@Inject 
	private Instance<ViewPreHospitalizationTable> postHospitalizationInstance;
	
	private ViewPreHospitalizationTable postHospitalizationObj;
	
	@Inject
	private Instance<HospitalisationTable> hospitalizationInstance;
	
	private HospitalisationTable hospitalizationObj;
	
	@Inject
	private Instance<ViewHospitalizationListenerTable> viewHospitalizationInstance;
	
	private ViewHospitalizationListenerTable viewHospitalizationObj;
	
	
	//@Inject
	private PreauthDTO bean;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private CreateRODService billDetailsService;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Map<Integer, String> masterValues = new HashMap<Integer, String>();
	
	private Map<Integer,String> serialMap = new HashMap<Integer,String>();
	
	private Map<Integer,String> serialMap1 = new HashMap<Integer,String>();
	
	private Map<Long, Integer> sequenceMap = new HashMap<Long, Integer>();
	
	private Button billDetailsBtn;
	
	private Button exportPdfBtn;
	
	private Long insuredId;
	
	private Long policyKey;
	
	private Long productId;
	
	private Intimation intimation;
	
	private ComboBox proportionateDeductionBox;
	
	private TextField txtDeductionPercentage;
	
	private List<BillingHospitalisation> newHospitalizationList ;
	
	private VerticalLayout viewHospitalListenerTableLayout ;
	
	private VerticalLayout hospListenerTableLayout;
	
	private ComboBox cmbPackage;
	
	private String proportionalDeductionFlg;
	
	private String packageFlg = "N";
	
	private Boolean isPackageAvailable ;
	
	private Map seniorCitizenViewMap;
	
	
	private List<BillEntryDetailsDTO> billEntryListForReport;
	
	private List<PreHospitalizationDTO> prePostHospitalizationList;
	
	private List<PreHospitalizationDTO> postHospitalizationList = new ArrayList<PreHospitalizationDTO>();
	
	private Long rodKey;
	
	private Boolean isView = false;
	
	Panel mainPanel = new Panel();
	
	private Double proportionalValue;
	
	private Long productKey;
	
	private Double insuredSumInsured;
	
	
	private List<BillEntryDetailsDTO> roomRentNursingChargeList;

	private List<BillEntryDetailsDTO> policyLimitDList;
	
	private List<BillEntryDetailsDTO> policyLimitEList;
	
	private List<BillEntryDetailsDTO> ambulanceChargeList;
	private List<BillEntryDetailsDTO> policyLimitDandEList;
	
	private Reimbursement reimbursement;

	
	
	public void init(PreauthDTO preauthDTO, Long rodKey, Boolean isView)
	{
		this.isView = isView;
		this.bean = preauthDTO;
		this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		this.prePostHospitalizationList = new ArrayList<PreHospitalizationDTO>();
		this.postHospitalizationList = new ArrayList<PreHospitalizationDTO>();
//		init(rodKey);
		if(null != rodKey){
			if(!(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
					|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
				dbCalculationService.getBillDetailsSummary(rodKey);
			} 
			else if((preauthDTO.getProrataDeductionFlag() != null && preauthDTO.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
					(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
							|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
				dbCalculationService.getBillDetailsSummary(rodKey);
			}
			dbCalculationService.callBillAssessmentSheet(rodKey);
			
		}
		
		newHospitalizationList = new ArrayList<BillingHospitalisation>();
		viewHospitalListenerTableLayout = new VerticalLayout();
		hospListenerTableLayout = new VerticalLayout();
		this.rodKey = rodKey;
		this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		this.prePostHospitalizationList = new ArrayList<PreHospitalizationDTO>();


		Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
		this.reimbursement = reimbursement;
		
//		if (null != reimbursement && null != reimbursement.getProrataDeductionFlag() && !("").equalsIgnoreCase(reimbursement.getProrataDeductionFlag()))
//		{
//			this.bean.setProrataDeductionFlag(reimbursement.getProrataDeductionFlag());
//		}
		
		if(reimbursement != null){
			Intimation intimation = reimbursement.getClaim().getIntimation();
		    this.intimation = intimation;
			NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
			Long insuredId = intimationDto.getInsuredPatient().getInsuredId();
			Long policyKey = intimationDto.getPolicy().getKey();
			Long productId = intimationDto.getPolicy().getProduct().getKey();
			this.insuredId = insuredId;
			this.policyKey = policyKey;
			this.productId = productId;
		}
		
	
		
	
		//Vaadin8-setImmediate() mainPanel.setImmediate(false);
		mainPanel.setWidth("100.0%");
		mainPanel.setHeight("100.0%");
		Label label = new Label("View Bill Summary");
		label.setStyleName("labelDesign");
		billDetailsBtn = new Button("View Bill Details");
		exportPdfBtn = new Button("Export to Pdf");
		
		 proportionalValue = dbCalculationService.getProportionalDeductionPercentage(rodKey);

		
		HorizontalLayout btnformLayout = new HorizontalLayout(billDetailsBtn,exportPdfBtn);
		btnformLayout.setSpacing(true);
		
		
		addListener();
		HorizontalLayout dummyLayout = null;
		if(isView){
			dummyLayout = new HorizontalLayout(billDetailsBtn);
			dummyLayout.setComponentAlignment(billDetailsBtn, Alignment.TOP_RIGHT);
		}else{
			dummyLayout = new HorizontalLayout(btnformLayout);
			dummyLayout.setComponentAlignment(btnformLayout, Alignment.TOP_RIGHT);
		}
		dummyLayout.setHeight("50px");
		dummyLayout.setWidth("100%");
		VerticalLayout verticalMain = new VerticalLayout(label,dummyLayout,builBillSummaryTabs());
		mainPanel.setContent(verticalMain);
		setCompositionRoot(mainPanel);
		
		
	}
	
	public void init(Long rodKey){
		
		if(null != rodKey){
			dbCalculationService.getBillDetailsSummary(rodKey);
		}
		
		newHospitalizationList = new ArrayList<BillingHospitalisation>();
		viewHospitalListenerTableLayout = new VerticalLayout();
		hospListenerTableLayout = new VerticalLayout();
		this.rodKey = rodKey;
		this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		this.prePostHospitalizationList = new ArrayList<PreHospitalizationDTO>();


		Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
		
		PreauthDTO preauthDTO = new PreauthDTO();
		
	//	PreauthDTO prorataFlagFromProduct = getProrataFlagFromProduct(reimbursement,preauthDTO);
		
	//	this.bean = prorataFlagFromProduct;
		
		if(reimbursement != null){
			Intimation intimation = reimbursement.getClaim().getIntimation();
		    this.intimation = intimation;
			NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
			Long insuredId = intimationDto.getInsuredPatient().getInsuredId();
			Long policyKey = intimationDto.getPolicy().getKey();
			Long productId = intimationDto.getPolicy().getProduct().getKey();
			this.insuredId = insuredId;
			this.policyKey = policyKey;
			this.productId = productId;
		}
		
		Panel mainPanel = new Panel();
		//Vaadin8-setImmediate() mainPanel.setImmediate(false);
		mainPanel.setWidth("100.0%");
		mainPanel.setHeight("100.0%");
		Label label = new Label("View Bill Summary");
		label.setStyleName("labelDesign");
		billDetailsBtn = new Button("View Bill Details");
		
		addListener();
		
		HorizontalLayout dummyLayout = new HorizontalLayout(billDetailsBtn);
		dummyLayout.setComponentAlignment(billDetailsBtn, Alignment.TOP_RIGHT);
		dummyLayout.setHeight("50px");
		dummyLayout.setWidth("100%");
		VerticalLayout verticalMain = new VerticalLayout(label,dummyLayout,builBillSummaryTabs());
		mainPanel.setContent(verticalMain);
		setCompositionRoot(mainPanel);
		
	}
	
	
	
	public void addListener(){
		billDetailsBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {


				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("85%");
				popup.setHeight("85%");
				viewBillDetailsTableObj = viewBillDetailsTableInstance.get();
				viewBillDetailsTableObj.init();
				viewBillDetailsTableObj.setReferenceData(referenceData);
				setBillDetailsTable(rodKey);
				popup.setContent(viewBillDetailsTableObj);
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

		});
		
		if(exportPdfBtn != null){
			exportPdfBtn.addClickListener(new Button.ClickListener() {
	
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
				public void buttonClick(ClickEvent event) {
				 
					popup = new com.vaadin.ui.Window();
					popup.setCaption("");
					popup.setWidth("85%");
					popup.setHeight("85%");
					popup.setContent(exportPdfFile());
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
	
			});
		}
	}
	
	private TabSheet builBillSummaryTabs() {
		TabSheet billSummaryTab = new TabSheet();
		//Vaadin8-setImmediate() billSummaryTab.setImmediate(true);
		
		// previousClaimTab.setWidth("100.0%");
		// previousClaimTab.setHeight("100.0%");
		billSummaryTab.setSizeFull();
		
		billSummaryTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		//TabSheet hospitalizationTab = getHospitalizationTab();
		TabSheet hospitalizationTab =  getNewHospitalizationTab();
		billSummaryTab.setHeight("100.0%");
		billSummaryTab.addTab(hospitalizationTab, "Hospitalization", null);

		// tabSheet_2
		TabSheet preHospitalizationTab = getPreHospitalizationTab();
		billSummaryTab.addTab(preHospitalizationTab, "Pre-Hospitalization", null);

		TabSheet postHospitalizationTab = getPostHospitalizatonTab();
		billSummaryTab.addTab(postHospitalizationTab, "Post-Hospitalization", null);

		return billSummaryTab;
	}
	
	private TabSheet getHospitalizationTab(){
		TabSheet hospitalizationTab = new TabSheet();
		hospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		hospitalizationTab.setWidth("100%");
		hospitalizationTab.setHeight("100%");
		hospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		
		
		VerticalLayout previousClaimTableLayout = new VerticalLayout();
		
		
		
		hospitalizationObj = hospitalizationInstance.get();
		hospitalizationObj.init();
		hospitalizationObj.setReferenceData(this.referenceData);
		loadMasterValues();
//		setHospitalizationValues(this.rodKey);
		

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);

		//previousClaimTableLayout.addComponent(lblHospitalization);
		
		hospitalizationTab.addComponent(hospitalizationObj);
		
		return hospitalizationTab;
	}
	
	private TabSheet getNewHospitalizationTab(){
		
		proportionateDeductionBox = new ComboBox("Proportinal Deduction");
		
		BeanItemContainer<SelectValue> selValContainer = loadComboBox();
		proportionateDeductionBox.setContainerDataSource(selValContainer);
		proportionateDeductionBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		proportionateDeductionBox.setItemCaptionPropertyId("value");
		proportionateDeductionBox.setEnabled(this.isView ? false : true);
		
		txtDeductionPercentage = new TextField("Proportinal deduction % age");
		
		//if(null != this.bean.getProrataPercentage())
		
				
		TabSheet hospitalizationTab = new TabSheet();
		hospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		hospitalizationTab.setWidth("100%");
		//hospitalizationTab.setHeight("100%");
		hospitalizationTab.setHeight("100%");
		hospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		
		Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
		insuredSumInsured = dbCalculationService.getInsuredSumInsured(
			reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey(),reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
		
		productKey = reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey();
		
		VerticalLayout previousClaimTableLayout = new VerticalLayout();
		
		viewHospitalizationObj = viewHospitalizationInstance.get();
		viewHospitalizationObj.init(productKey);
		viewHospitalizationObj.setReferenceData(this.referenceData);
		
		viewHospitalListenerTableLayout.setHeight("100%");
		viewHospitalListenerTableLayout.setWidth("100%");
		
		hospListenerTableLayout.setHeight("100%");
		hospListenerTableLayout.setWidth("100%");
		hospListenerTableLayout.setSizeFull();
		
		
		
		
		//if(ReferenceTable.SENIOR_CITIZEN_RED_CARPET.equals(productKey))
		if(null != this.bean && null != this.bean.getProductBasedPackage()  && ("Y").equalsIgnoreCase(this.bean.getProductBasedPackage()))
		{
			isPackageAvailable = true;
			
			cmbPackage = new ComboBox("Package");
			cmbPackage.setContainerDataSource(selValContainer);
			cmbPackage.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPackage.setItemCaptionPropertyId("value");	
			
			viewHospitalListenerTableLayout.addComponent(cmbPackage);
		}
		else
		{
			isPackageAvailable = false;
			packageFlg = "N";
		}
		
		if(null != this.bean && null != this.bean.getPackageAvailableFlag() && ("Y").equalsIgnoreCase(this.bean.getPackageAvailableFlag()))
		{
			packageFlg = "Y";
		}
		else
		{
			packageFlg = "N";
		}
		
		//setHospitalizationTableValues(this.rodKey);
		
		/*hospitalizationObj = hospitalizationInstance.get();
		hospitalizationObj.init();
		hospitalizationObj.setReferenceData(this.referenceData);
		loadMasterValues();
		setHospitalizationValues(this.rodKey);*/
		

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);

		//previousClaimTableLayout.addComponent(lblHospitalization);
		
		
		
		viewHospitalListenerTableLayout.addComponent(proportionateDeductionBox);
		viewHospitalListenerTableLayout.addComponent(txtDeductionPercentage);
		viewHospitalListenerTableLayout.addComponent(hospListenerTableLayout);
		
		
		
		//hospitalizationTab.addComponent(proportionateDeductionBox , txtDeductionPercentage , viewHospitalListenerTableLayout);
		hospitalizationTab.addComponent(viewHospitalListenerTableLayout);

		
		
		
		addHospTabListener();
		
		loadMapForStarSeniorCitizen();
		
		newHospitalizationList = billDetailsService.getBillingHospitalisationListOrderByItemNumber(rodKey);
		
	/*	if(productKey != null && productKey.equals(ReferenceTable.SENIOR_CITIZEN_POLICY) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET)){*/
		if(productKey != null && ReferenceTable.getSeniorCitizenKeys().containsKey(productKey)){
			setRevisedHospitalizationTableValuesForStarSeniorCitizen(rodKey);
		}else{
			setRevisedHospitalizationTableValues(rodKey);
			
			if(!(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey)))
			{
				viewHospitalizationObj.calculateTotal();

			}
		}
		
		
		
		Map<String, Double> footerValues = viewHospitalizationObj.getFooterValues();
		 if(footerValues != null){
			 
			 this.bean.setNonPayableTotalAmt(footerValues.get(SHAConstants.NONPAYABLE) + footerValues.get(SHAConstants.NONPAYABLE_PRODUCT_BASED));
			 
			 this.bean.setReasonableDeductionTotalAmt(footerValues.get(SHAConstants.REASONABLE_DEDUCTION));
			 
			 if(this.bean.getReasonableDeductionTotalAmt() != null){
				 if(this.bean.getDeductions() != null && this.bean.getDeductions() < 0){
					 this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt() + this.bean.getDeductions());
				 }else if(this.bean.getDeductions() != null && this.bean.getDeductions() > 0){
					 this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt()- this.bean.getDeductions());
				 }
				 
				 if(this.bean.getHospitalDiscount() != null && this.bean.getHospitalDiscount() < 0){
					 
//					 this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt() + this.bean.getHospitalDiscount());
					 
				 }else if(this.bean.getHospitalDiscount() != null && this.bean.getHospitalDiscount() > 0){
//					 this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt() - this.bean.getHospitalDiscount());
				 }
			 }
			 
			 this.bean.setProportionateDeductionTotalAmt(footerValues.get(SHAConstants.PROPORTION_DEDUCTION_AMOUNT));
			 
			 this.bean.setTotalApprovedAmt(footerValues.get(SHAConstants.NET_AMOUNT));
			 
			 this.bean.setTotalClaimedAmt(footerValues.get(SHAConstants.TOTAL_CLAIMED_AMOUNT));
			 
			 this.bean.setToatlNonPayableAmt(footerValues.get(SHAConstants.TOTALDISALLOWANCE));
			 
			 this.bean.setAmountConsidered(footerValues.get(SHAConstants.NET_AMOUNT) != null ? String.valueOf(footerValues.get(SHAConstants.NET_AMOUNT).intValue()) : this.bean.getAmountConsidered());

			 if(this.bean.getToatlNonPayableAmt() != null){
				 if(this.bean.getDeductions() != null && this.bean.getDeductions() < 0){
					 this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt() + this.bean.getDeductions());
				 }else if(this.bean.getDeductions() != null && this.bean.getDeductions() > 0){
					 this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt()- this.bean.getDeductions());
				 }
				 
				 if(this.bean.getHospitalDiscount() != null && this.bean.getHospitalDiscount() < 0){
					 
					 this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt() + this.bean.getHospitalDiscount());
					 
				 }else if(this.bean.getHospitalDiscount() != null && this.bean.getHospitalDiscount() > 0){
					 this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt() - this.bean.getHospitalDiscount());
				 }
			 }
		 }
		
		
//		setProportionalDeductionValue(selValContainer,productKey);
//		
//		setPackageValue(selValContainer,reimbursement.getPackageAvailableFlag());
//		if(this.bean != null){
//			setPackageValue(selValContainer, this.bean.getPackageAvailableFlag());
//		}else{
////			isPackageAvailable = true;
////			setPackageValue(selValContainer,"N" );
//			
//		}
		
		
		 
		
		return hospitalizationTab;
	}
	
	private void setProportionalDeductionValue(BeanItemContainer<SelectValue> selValContainer,Long productKey)
	{
		
		String protataFlag =null;
		if(this.bean != null && this.bean.getProrataDeductionFlag() != null && "Y".equalsIgnoreCase(this.bean.getProrataDeductionFlag()))
		{
			protataFlag = "Yes";
		}
		else if(this.bean != null && this.bean.getProrataDeductionFlag() != null && ("N".equalsIgnoreCase(this.bean.getProrataDeductionFlag()) || ("P".equalsIgnoreCase(this.bean.getProrataDeductionFlag()))))
		{
			protataFlag = "No";
		}else if(isView){
			protataFlag = "No";
		}
		 for(int i = 0 ; i<selValContainer.size() ; i++)
		 	{
				 if(null != this.bean && null != this.bean.getProrataDeductionFlag() && !("").equalsIgnoreCase(this.bean.getProrataDeductionFlag()))
				 {
					 if (protataFlag != null && (protataFlag).equalsIgnoreCase(selValContainer.getIdByIndex(i).getValue()))
						{
							this.proportionateDeductionBox.setValue(selValContainer.getIdByIndex(i));
						}
				 }
				/* else
				 { 
					 if(ReferenceTable.SENIOR_CITIZEN_RED_CARPET.equals(productKey))
						{
						 if (("NO").equalsIgnoreCase(selValContainer.getIdByIndex(i).getValue()))
							{
								this.proportionateDeductionBox.setValue(selValContainer.getIdByIndex(i));
							}
						}
					 
					 else if (("YES").equalsIgnoreCase(selValContainer.getIdByIndex(i).getValue()))
					{
						this.proportionateDeductionBox.setValue(selValContainer.getIdByIndex(i));
					}
				 }*/
			}

	}
	
	private void setPackageValue(BeanItemContainer<SelectValue> selValContainer, String packageFlag)
	{
		if(isPackageAvailable)
		{
			if((null == packageFlag || ("").equalsIgnoreCase(packageFlag)) || (null != packageFlag && ("Y").equalsIgnoreCase(packageFlag)))
			{
				packageFlag = "Yes";
			}
			else if(null != packageFlag && ("N").equalsIgnoreCase(packageFlag))
			{
				packageFlag = "No";
			}
			for(int i = 0 ; i<selValContainer.size() ; i++)
		 	{
				if ((packageFlag).equalsIgnoreCase(selValContainer.getIdByIndex(i).getValue()))
				{
					
						this.cmbPackage.setValue(selValContainer.getIdByIndex(i));
				}
				/*if(("No").equalsIgnoreCase(selValContainer.getIdByIndex(i).getValue()))
				{
					this.cmbPackage.setValue(selValContainer.getIdByIndex(i));
					
				}*/
			}
			this.proportionateDeductionBox.setReadOnly(true);
		}
	}
	
	private void addHospTabListener()
	{
		proportionateDeductionBox
	.addValueChangeListener(new Property.ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			SelectValue value = (SelectValue) event.getProperty().getValue();
			if(null != value)
			{	
				
				if (hospListenerTableLayout != null
						&& hospListenerTableLayout.getComponentCount() > 0) {
					hospListenerTableLayout.removeAllComponents();
					if(null != viewHospitalizationObj)
					{
						viewHospitalizationObj.removeAllItems();
					}	
				}
				
				/*if(null == packageFlg || ("").equalsIgnoreCase(packageFlg))
				{
					packageFlg = "N";
				}*/
				
				if(("YES").equalsIgnoreCase(value.getValue()))
				{	
					proportionalDeductionFlg = "Y";
					loadTableValuesBasedOnLisetner();
				}
				else
				{
					proportionalDeductionFlg = "N";
					//proportionateDeductionBox.setReadOnly(true);
					loadTableValuesBasedOnLisetner();
				}
				
				if(null != bean && null != bean.getProductBasedProRata() && ("N").equalsIgnoreCase(bean.getProductBasedProRata()))
				 {
					 if(null != proportionateDeductionBox)
					 {
						 proportionateDeductionBox.setReadOnly(true);
					 }
				 }
				 
				 
				
			}
			
			
			
		}
	});
		
		if(isPackageAvailable)
		{
			cmbPackage
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
//					SelectValue value = (SelectValue) event.getProperty().getValue();
//					if(null != value)
//					{	
//						
//						if (hospListenerTableLayout != null
//								&& hospListenerTableLayout.getComponentCount() > 0) {
//							hospListenerTableLayout.removeAllComponents();
//							if(null != viewHospitalizationObj)
//							{
//								viewHospitalizationObj.removeAllItems();
//							}	
//						}
//						
//						/*if(null == proportionalDeductionFlg || ("").equalsIgnoreCase(proportionalDeductionFlg))
//						{
//							proportionalDeductionFlg = "Y";
//						}*/
//						
//						if(("YES").equalsIgnoreCase(value.getValue()))
//						{	
//							packageFlg = "Y";	
//							loadTableValuesBasedOnLisetner();
//						}
//						else
//						{
//							packageFlg = "N";
//							//cmbPackage.setReadOnly(true);
//							loadTableValuesBasedOnLisetner();
//						}
//						
//						if(null != bean &&  null != bean.getProductBasedPackage() && ("N").equalsIgnoreCase(bean.getProductBasedPackage()))
//						 {
//							 if(null != cmbPackage)
//							 {
//								 cmbPackage.setReadOnly(true);
//							 }
//						 }
//					}
					
				}
			});
		}
	}
	
	private void loadTableValuesBasedOnLisetner()
	{
			 proportionalValue = dbCalculationService.getProportionalDeductionPercentage(rodKey);
			
			if(("N").equalsIgnoreCase(proportionalDeductionFlg))
			{
				txtDeductionPercentage.setReadOnly(false);
				txtDeductionPercentage.setValue(null);
				txtDeductionPercentage.setNullRepresentation("");
				txtDeductionPercentage.setReadOnly(true);
			}
			else
			{
				txtDeductionPercentage.setReadOnly(false);
				txtDeductionPercentage.setValue(String.valueOf(proportionalValue));
				txtDeductionPercentage.setReadOnly(true);
			}
			dbCalculationService.populateHospitalisationData(rodKey, packageFlg, proportionalDeductionFlg);
			if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			{
				newHospitalizationList.clear();
			}
			newHospitalizationList = billDetailsService.getBillingHospitalisationListOrderByItemNumber(rodKey);
			if(isPackageAvailable)
			{
				loadMapForStarSeniorCitizen();
				setRevisedHospitalizationTableValuesForStarSeniorCitizen(rodKey);
			}
			else
			{
				loadMapForStarSeniorCitizen();
				setRevisedHospitalizationTableValues(rodKey);
				if(!(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey)))
				{
					viewHospitalizationObj.calculateTotal();
				}
			}
			hospListenerTableLayout.addComponent(viewHospitalizationObj);
			
			if(this.bean != null){
					this.bean.setProrataDeductionFlag(proportionalDeductionFlg);
					this.bean.setProrataPercentage(proportionalValue);
					this.bean.setPackageAvailableFlag(packageFlg);
			}
	}
	
	private BeanItemContainer<SelectValue>  loadComboBox()
	{
		SelectValue selValue1 = new SelectValue();
		selValue1.setId(1l);
		selValue1.setValue("Yes");
		
		SelectValue selValue2 = new SelectValue();
		selValue2.setId(2l);
		selValue2.setValue("No");
		List comboList = new ArrayList<SelectValue>();
		comboList.add(selValue1);
		comboList.add(selValue2);
		
		@SuppressWarnings("deprecation")
		BeanItemContainer<SelectValue> selectedValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectedValueContainer.addAll(comboList);
/*		cmb.setContainerDataSource(selectedValueContainer);
		cmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb.setItemCaptionPropertyId("value");*/
		return selectedValueContainer;
		
	}
	
	
	private TabSheet getPreHospitalizationTab(){
		TabSheet preHospitalizationTab = new TabSheet();
		preHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);
		preHospitalizationTab.setWidth("100%");
		preHospitalizationTab.setHeight("100%");
		preHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);
		
		VerticalLayout previousClaimTableLayout = new VerticalLayout();
		
		preHospitalizationTable = preHospitalizationInstance.get();
		preHospitalizationTable.init();
		preHospitalizationTable.setReferenceData(this.referenceData);
		
		setTableValue(this.rodKey);
		
	//	lblPreHospitalization = new Label("Pre-Hospitalization");

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);

		//previousClaimTableLayout.addComponent(lblPreHospitalization);
		
		preHospitalizationTab.addComponent(preHospitalizationTable);
		
		return preHospitalizationTab;
		
	}
	
	private TabSheet getPostHospitalizatonTab(){
		TabSheet postHospitalizationTab = new TabSheet();
		postHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);
		postHospitalizationTab.setWidth("100%");
		postHospitalizationTab.setHeight("100%");
		postHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);
		
		VerticalLayout previousClaimTableLayout = new VerticalLayout();
		
		postHospitalizationObj = postHospitalizationInstance.get();
		postHospitalizationObj.init();
		postHospitalizationObj.setReferenceData(this.referenceData);
		
		setPostHospitalizationValue(this.rodKey);

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);
		postHospitalizationTab.addComponent(postHospitalizationObj);
		
		return postHospitalizationTab;
	}
	
	private void setTableValue(Long rodKey){
		List<PreHospitalizationDTO> hospitalizationList = new ArrayList<PreHospitalizationDTO>();
		List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.PRE_HOSPITALIZATION);
		List<BillingPreHospitalisation> preHospitalisation = billDetailsService.getBillingPreHospitalisationList(rodKey);
		
		int sno =1;
		for(int i=0;i<billDetailsType.size();i++){                                          //load all master values initially
			
			PreHospitalizationDTO prehoDto = new PreHospitalizationDTO();
			if(sno == 1){
			prehoDto.setSno(sno);
			}
			
			if(i==2){
				PreHospitalizationDTO dummyDto = new PreHospitalizationDTO();
				dummyDto.setDetails("C)Medicines & Consumables");
//				dummyDto.setSno(sno);
				hospitalizationList.add(dummyDto);
				prehoDto.setSno(null);
			    prehoDto.setDetails("           i)"+billDetailsType.get(i).getValue());
			}
			else if(i==3){
				prehoDto.setSno(null);
				prehoDto.setDetails("           ii)"+billDetailsType.get(i).getValue());
				sno--;
			}
			else{
				if(i==0){
					prehoDto.setDetails("a)"+billDetailsType.get(i).getValue());
				}else if(i==1){
					prehoDto.setDetails("b)"+billDetailsType.get(i).getValue());
				}else if(i==4){
					prehoDto.setDetails("d)"+billDetailsType.get(i).getValue());
				}else if(i==5){
					prehoDto.setDetails("e)"+billDetailsType.get(i).getValue());
				}
			}
			hospitalizationList.add(prehoDto);
			sno++;
		}
		
       for (BillingPreHospitalisation preHospitalisation2 : preHospitalisation) {                 //Load all value to corresponding Master Values
    	   int sequenceNumber =0;
			for (MasBillDetailsType masBillDetailsType : billDetailsType) {
				if (preHospitalisation2.getBillTypeNumber() != null) {
					if (preHospitalisation2.getBillTypeNumber().equals(masBillDetailsType.getKey())) {
						sequenceNumber = masBillDetailsType.getSequenceNumber().intValue();
						if (!(sequenceNumber > 2)) {
							sequenceNumber--;
						}
						break;
					}
				}
			}

    	   PreHospitalizationDTO preHospitalDto = hospitalizationList.get(sequenceNumber);
    	   preHospitalDto.setClaimedAmt(preHospitalisation2.getClaimedAmountBills());
    	   //Need to check why this non payable is set to 0l by default;
    	   preHospitalDto.setBillingNonPayable(preHospitalisation2.getNonPayableAmount());
    	//   preHospitalDto.setBillingNonPayable(0l);
    	//   preHospitalDto.setNetAmount(preHospitalisation2.getClaimedAmountBills());
    	   preHospitalDto.setNetAmount(preHospitalisation2.getNetAmount());;
    	   /**
    	    * Changes done for new pre hospitalization veiw.
    	    */
    	   preHospitalDto.setReasonableDeduction(preHospitalisation2.getDeductibleAmount());
    	 //  preHospitalDto.setAmount(preHospitalisation2.getClaimedAmountBills());
    	   //preHospitalDto.setDeductingNonPayable(0l);
    	  // preHospitalDto.setPayableAmount(preHospitalisation2.getClaimedAmountBills());
    	   preHospitalDto.setReason(preHospitalisation2.getReason());
		}
       
       	int i = 0;
       
       	Double claimedAmount = 0d; 
		Double nonPayableAmount = 0d;
	    Double deductionAmt = 0d;
	    Double netAmount =0d;
	    String remarks = "";
       
	    if(hospitalizationList != null && hospitalizationList.size()>5){
	    	int index = 0;
	    	
	    	for (PreHospitalizationDTO preHospital : hospitalizationList) {
				if(index == 3 || index == 4){
					if(preHospital.getClaimedAmt() != null){
						claimedAmount += preHospital.getClaimedAmt();
					}
					if(preHospital.getBillingNonPayable() != null){
						nonPayableAmount += preHospital.getBillingNonPayable();
					}
					if(preHospital.getReasonableDeduction() != null){
						deductionAmt += preHospital.getReasonableDeduction();
					}
					if(preHospital.getNetAmount() != null){
						netAmount += preHospital.getNetAmount();
					}
//					remarks = remarks + "/";
					
				}
				
				index++;
	    	}
	    	
	    }
		
		for (PreHospitalizationDTO preHospital : hospitalizationList) {
//			preHospitalizationTable.addBeanToList(preHospital);
//			preHospital.setClassificationFlag(SHAConstants.PREHOSPITALIZATION);
//			prePostHospitalizationList.add(preHospital);
			
            if(i != 3 && i != 4){
            	if(i == 2){
            		preHospital.setClaimedAmt(claimedAmount);
            		preHospital.setReasonableDeduction(deductionAmt);
            		preHospital.setBillingNonPayable(nonPayableAmount);
            		preHospital.setNetAmount(netAmount);
            		preHospital.setReason(remarks);
            	}
            	
            	preHospitalizationTable.addBeanToList(preHospital);
            	if(prePostHospitalizationList != null){
            		prePostHospitalizationList.add(preHospital);
				}
            }
            i++;
		}

		Double deductions = 0d;
		
		if(prePostHospitalizationList != null && ! prePostHospitalizationList.isEmpty()){
			for (PreHospitalizationDTO billingPostHospitalisation : prePostHospitalizationList) {
				if(billingPostHospitalisation.getBillingNonPayable() != null){
				deductions += billingPostHospitalisation.getBillingNonPayable();
				}
				if(billingPostHospitalisation.getReasonableDeduction() != null){
					deductions += billingPostHospitalisation.getReasonableDeduction();
				}
			}
		}
		
		if(null != bean)
		{
			this.bean.setPreHospitalDeduction(deductions);
			this.bean.setPrehospitalizationDTO(prePostHospitalizationList);
		}
	}
	
	private void setPostHospitalizationValue(Long rodKey){
		
        List<PreHospitalizationDTO> hospitalizationList = new ArrayList<PreHospitalizationDTO>();
		List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.POST_HOSPITALIZATION);
		List<BillingPostHospitalisation> preHospitalisation = billDetailsService.getBillingPostHospitalisationList(rodKey);
		int sno =1;
		for(int i=0;i<billDetailsType.size();i++){                                          //load all master values initially
			
			PreHospitalizationDTO prehoDto = new PreHospitalizationDTO();
			if(sno == 1){
			prehoDto.setSno(sno);
			}
			if(i==2){
				PreHospitalizationDTO dummyDto = new PreHospitalizationDTO();
				dummyDto.setDetails("C)Medicines & Consumables");
//				dummyDto.setSno(sno);
				prehoDto.setSno(null);
				hospitalizationList.add(dummyDto);
			    prehoDto.setDetails("           i)"+billDetailsType.get(i).getValue());
			   
			}
			else if(i==3){
				prehoDto.setSno(null);
				prehoDto.setDetails("           ii)"+billDetailsType.get(i).getValue());
				sno--;
			}
			else{
				if(i==0){
					prehoDto.setDetails("a)"+billDetailsType.get(i).getValue());
				}else if(i==1){
					prehoDto.setDetails("b)"+billDetailsType.get(i).getValue());
				}else if(i==4){
					prehoDto.setDetails("d)"+billDetailsType.get(i).getValue());
				}else if(i==5){
					prehoDto.setDetails("e)"+billDetailsType.get(i).getValue());
				}
				
			}
			hospitalizationList.add(prehoDto);
			sno++;
		}
		
       for (BillingPostHospitalisation preHospitalisation2 : preHospitalisation) {                 //Load all value to corresponding Master Values
    	   int sequenceNumber =0;
    	   for (MasBillDetailsType masBillDetailsType : billDetailsType) {
    		   if(preHospitalisation2.getBillTypeNumber() != null){
   			             if(preHospitalisation2.getBillTypeNumber().equals(masBillDetailsType.getKey())){
   			            	 sequenceNumber = masBillDetailsType.getSequenceNumber().intValue();
   			            	 if(!(sequenceNumber>2)){
   			            	 sequenceNumber--;
   			            	 }
   			            	 break;
   			             }
    		   }
   			 }
    	   
    	   //Need to check why this pre hos
    	   
    	   PreHospitalizationDTO preHospitalDto = hospitalizationList.get(sequenceNumber);
    	   preHospitalDto.setClaimedAmt(preHospitalisation2.getClaimedAmountBills());
    	   //preHospitalDto.setBillingNonPayable(0l);
    	   preHospitalDto.setBillingNonPayable(preHospitalisation2.getNonPayableAmount());
    	   preHospitalDto.setReasonableDeduction(preHospitalisation2.getDeductibleAmount());
    	 //  preHospitalDto.setNetAmount(preHospitalisation2.getClaimedAmountBills());
    	   preHospitalDto.setNetAmount(preHospitalisation2.getNetAmount());
    	  // preHospitalDto.setAmount(preHospitalisation2.getClaimedAmountBills());         //Need to implements
    	   //preHospitalDto.setDeductingNonPayable(preHospitalisation2.getDeductibleAmount());  //Need to implements 
    	   //preHospitalDto.setDeductingNonPayable(0l);
    	   //preHospitalDto.setPayableAmount(preHospitalisation2.getClaimedAmountBills());
    	   preHospitalDto.setReason(preHospitalisation2.getReason());
 
    	   }
		
        int i = 0;
        
        Double claimedAmount = 0d; 
		Double nonPayableAmount = 0d;
	    Double deductionAmt = 0d;
	    Double netAmount =0d;
	    String remarks = "";
	    
	    
	    if(hospitalizationList != null && hospitalizationList.size()>5){
	    	int index = 0;
	    	
	    	for (PreHospitalizationDTO preHospital : hospitalizationList) {
				if(index == 3 || index == 4){
					if(preHospital.getClaimedAmt() != null){
						claimedAmount += preHospital.getClaimedAmt();
					}
					if(preHospital.getBillingNonPayable() != null){
						nonPayableAmount += preHospital.getBillingNonPayable();
					}
					if(preHospital.getReasonableDeduction() != null){
						deductionAmt += preHospital.getReasonableDeduction();
					}
					if(preHospital.getNetAmount() != null){
						netAmount += preHospital.getNetAmount();
					}
//					remarks = remarks + "/";
					
				}
				
				index++;
	    	}
	    	
	    }
	    
	  
		
		for (PreHospitalizationDTO preHospital : hospitalizationList) {

                if(i != 3 && i != 4){
                	if(i == 2){
                		preHospital.setClaimedAmt(claimedAmount);
                		preHospital.setReasonableDeduction(deductionAmt);
                		preHospital.setBillingNonPayable(nonPayableAmount);
                		preHospital.setNetAmount(netAmount);
                		preHospital.setReason(remarks);
                	}
                	
                	postHospitalizationObj.addBeanToList(preHospital);
                	if(postHospitalizationList != null){
    					postHospitalizationList.add(preHospital);
    				}
                }
                
                i++;

			preHospital.setClassificationFlag(SHAConstants.POSTHOSPITALIZATION);
		}
		
		
		Double deductions = 0d;
		
		if(postHospitalizationList != null && ! postHospitalizationList.isEmpty()){
			for (PreHospitalizationDTO billingPostHospitalisation : postHospitalizationList) {
				if(billingPostHospitalisation.getBillingNonPayable() != null){
					deductions += billingPostHospitalisation.getBillingNonPayable();
					}
					if(billingPostHospitalisation.getReasonableDeduction() != null){
						deductions += billingPostHospitalisation.getReasonableDeduction();
					}
			}
		}
		
		
		if(null != bean)
		{
			this.bean.setPostHospitalDeduction(deductions);
			this.bean.setPostHospitalizationDTO(postHospitalizationList);
		}
	}
	
	
	
	private void setHospitalizationTableValuesForMicroProduct(Long rodKey)
	{
		
		 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
		 
		 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
		 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
		 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
		 List<BillingHospitalisation> roomRentList = null;
		 List<BillingHospitalisation> nursingList = null;
		 
		 List<BillingHospitalisation> icuRoomRentList = null;
		 List<BillingHospitalisation> icuNursingList = null;
		 
		 Double fromOTToDeductionsTotalClaimedAmt = 0d;
		 Double fromOTToDeductionsTotalAllowableAmt = 0d;
		 Double fromOTToDeductionsTotalNonPayablePdtBasedAmt = 0d;
		 Double fromOTToDeductionsTotalNonPayableManualAmt = 0d;
		 Double fromOTToDeductionsTotalProprotionateAmt = 0d;
		 Double fromOTToDeductionsTotalReasonableAmt = 0d;
		 Double fromOTToDeductionsTotalTotalDisallowanceAmt = 0d;
		 Double fromOTToDeductionsTotalNetPayableAmt = 0d;
		 
		 Double OTNonPayable = 0d;
		 
		 Double OTReasonableDeductions = 0d;
		 String deductibleReason = "";
		 
		 
		 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<BillingHospitalisation>();
			 nursingList = new ArrayList<BillingHospitalisation>();
			 icuRoomRentList = new ArrayList<BillingHospitalisation>();
			 icuNursingList = new ArrayList<BillingHospitalisation>();
			 
			 for (BillingHospitalisation hospitalisation : newHospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuRoomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuNursingList.add(hospitalisation);
				 }
				
			}
			 
		 }
		 
		 if(null != billDetailsType && !billDetailsType.isEmpty())
		 {
			 
			 int iMapSize = 0;
			 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
			 {
				iMapSize = seniorCitizenViewMap.size(); 
			 }
			 //for (MasBillDetailsType masBillType : billDetailsType)
			 for(int i = 1; i<=iMapSize; i++)
			 {
				 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
				 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
				 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
				 {
					 //Adding a blank row for room rent and nursing charges.
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					// Double subTotalAllowableAmt = 0d;
					 //Double subTotalPdtBasedPerDayAmt = 0d;
					 //Double subTotalNonPayablePdtBased = 0d;
					// Double subTotalNonPayable = 0d;
					 Double subTotalProportionateDeduction = 0d;
					 //Double reasonableDeduction = 0d;
					 
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : roomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "1."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													//String value = "1."+ iNo;
													//billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<nursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = nursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleReason += billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
														
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));

													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Room Rent and Nursing Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						billEntryDetailsDTO4.setDeductibleOrNonPayableReason(deductibleReason);
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						billEntryListForReport.add(billEntryDetailsDTO4);
						deductibleReason = "";

						
				 }
				 
				 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
				 {
					
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);

					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 
					 Double subTotalProportionateDeduction = 0d;
					 
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : icuRoomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "2."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													}
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<icuNursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = icuNursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleReason += billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));

													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("ICU Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						billEntryDetailsDTO4.setDeductibleOrNonPayableReason(deductibleReason);
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						billEntryListForReport.add(billEntryDetailsDTO4);

				 }
				 
				 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				OTNonPayable = billEntryDetailsObj.getNonPayable();
			 				OTReasonableDeductions = billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble((String.valueOf(i))));
			 			billEntryDetailsObj.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 /*else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble((String.valueOf(i))));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble((String.valueOf(i))));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
						billEntryListForReport.add(billEntryDetailList);

					 
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailList);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailList);

			 			}
					}
			 	}
				else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				// }
				 /*else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						billEntryDetailList.setItemNoForView((6.0d));
						billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
						viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.MEDICINES).equals(billTypeKey))
				 {
						BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						billEntryDetailList.setItemNoForView((6.0d));
						billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
						viewHospitalizationObj.addBeanToList(billEntryDetailList);
		 				billEntryListForReport.add(billEntryDetailList);

					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)Medicines");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 			}
				 }
				 else if((ReferenceTable.CONSUMABLES).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Consumbles");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Ìmplant/Stent/Valve/Pacemaker/Etc");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
				 {

					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
		 			 billEntryListForReport.add(billEntryDetailList);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)ANH Package");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Other packages");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(9.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
				 {
						BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
						billEntryDetailsObj.setItemNoForView(10.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		}
				 }*/
				 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 billEntryDetailsObject.setItemNoForView(10.0d);
					 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
		 			 billEntryListForReport.add(billEntryDetailsObject);

			 			
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHERS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			billEntryDetailsObj.setItemNoForView(11.0d);
				 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 	}
				 }
				 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(12.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(13.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				 else if((ReferenceTable.MICRO_INS_IND_OT_TILL_DEDUCTIONS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
						 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
						 	billEntryDetailsObj.setItemName(SHAConstants.MICRO_INS_IND_OT_TILL_DEDUCTIONS);
						 	billEntryDetailsObj.setItemValue(fromOTToDeductionsTotalClaimedAmt);
						 	Double amount1 = getDiffOfTwoNumber(OTNonPayable, OTReasonableDeductions);
						 	Double amount2 = fromOTToDeductionsTotalClaimedAmt - amount1;
						 	Double proportionalDeductionVal = 100-proportionalValue;
						 	Double allowableAmt = Math.min((amount2*proportionalDeductionVal), 1500);
						 	billEntryDetailsObj.setAmountAllowableAmount(allowableAmt);

				 		}
				 }
				 else if((ReferenceTable.NETWORK_HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(14.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.NETWORK_HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
			 }
				
		 }
		 if(null != this.bean)
		 {
			 bean.setBillEntryDetailsDTO(billEntryListForReport);
		 }
	}
	
	private void setRevisedHospitalizationTableValues(Long rodKey)
	{
		
		 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
		 
		 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
		 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
		 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
		 List<BillingHospitalisation> roomRentList = null;
		 List<BillingHospitalisation> nursingList = null;
		 
		 List<BillingHospitalisation> icuRoomRentList = null;
		 List<BillingHospitalisation> icuNursingList = null;
		 
		 Double fromOTToDeductionsTotalClaimedAmt = 0d;
		 Double fromOTToDeductionsTotalAllowableAmt = 0d;
		 Double fromOTToDeductionsTotalNonPayablePdtBasedAmt = 0d;
		 Double fromOTToDeductionsTotalNonPayableManualAmt = 0d;
		 Double fromOTToDeductionsTotalProprotionateAmt = 0d;
		 Double fromOTToDeductionsTotalReasonableAmt = 0d;
		 Double fromOTToDeductionsTotalTotalDisallowanceAmt = 0d;
		 Double fromOTToDeductionsTotalNetPayableAmt = 0d;
		 
		 Double OTNonPayable = 0d;
		 Double OTReasonableDeductions = 0d;
		 
		 Double totalItemValueForMicro = 0d;
	//	 Double totalICUForMicro = 0d;
		 Double totalAllowableAmountForMicro = 0d;
		 Double totalNonPayableForMicro = 0d;
		 Double totalNonPayableManualForMicro = 0d;
		 Double totalProportionateDeductionForMicro = 0d;
		 Double totalReasonableDeductionForMicro = 0d;
		 Double totalDisallowanceForMicro = 0d;
		 Double totalNetPayableForMicro = 0d;
		 
		 Double itemValue = 0d;
		 Double amountAllowableAmount = 0d;
		 Double nonPayable = 0d;
		 Double proportionateDeduction = 0d;
		 Double reasonableDeduction = 0d;
		 Double totalDisallowancesAmount = 0d;
		 Double netPayableAmount = 0d;
		 String deductibleOrNonPayableReason = "";
		 
		 Double hospitalDiscount = 0d;
		 Double deductions = 0d;
		 
		 Double nonPaybleAmt = 0d;
		 
		 int sno = 0;
		 
		 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<BillingHospitalisation>();
			 nursingList = new ArrayList<BillingHospitalisation>();
			 icuRoomRentList = new ArrayList<BillingHospitalisation>();
			 icuNursingList = new ArrayList<BillingHospitalisation>();
			 
			 for (BillingHospitalisation hospitalisation : newHospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuRoomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuNursingList.add(hospitalisation);
				 }
				
			}
			 
		 }
		 
		/* if(null != hospitalizationList && !hospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<Hospitalisation>();
			 nursingList = new ArrayList<Hospitalisation>();
			 for (Hospitalisation hospitalisation : hospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				
			}
			 
		 }*/
		 
		 if(null != billDetailsType && !billDetailsType.isEmpty())
		 {
			 
			 int iMapSize = 0;
			 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
			 {
				iMapSize = seniorCitizenViewMap.size(); 
			 }
			 //for (MasBillDetailsType masBillType : billDetailsType)
			 for(int i = 1; i<=iMapSize; i++)
			 {
				 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
				 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
				 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
				 {
					 //Adding a blank row for room rent and nursing charges.
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					// Double subTotalAllowableAmt = 0d;
					 //Double subTotalPdtBasedPerDayAmt = 0d;
					 //Double subTotalNonPayablePdtBased = 0d;
					// Double subTotalNonPayable = 0d;
					 Double subTotalProportionateDeduction = 0d;
					 //Double reasonableDeduction = 0d;
					 
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : roomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "1."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													//String value = "1."+ iNo;
													//billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													if(billEntryDetailsDTO1.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleOrNonPayableReason();
													}
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<nursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = nursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleOrNonPayableReason += billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
															
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
														
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));

													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total Room Rent");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						
						totalItemValueForMicro += totalClaimedAmt;
						totalAllowableAmountForMicro += totalAllowableAmt;
						totalNonPayableForMicro += totalNonPayable;
						totalNonPayableManual += totalNonPayableManual;
						totalReasonableDeductionForMicro += totalReasonableDeduction;
						totalDisallowanceForMicro += totalDisallowances;
						totalNetPayableForMicro += netPayableAmt;
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("Room Rent & Nursing Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						reportDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
						
						
						
						billEntryListForReport.add(reportDto);
						
						deductibleOrNonPayableReason = "";
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
//						billEntryListForReport.add(billEntryDetailsDTO4);

						
				 }
				 
				 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);

					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 
					 Double subTotalProportionateDeduction = 0d;
					 
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : icuRoomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "2."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													}
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													
													if(billEntryDetailsDTO1.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleOrNonPayableReason();
													}
													
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<icuNursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = icuNursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleOrNonPayableReason += billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));

													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total ICU Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						
						totalItemValueForMicro += totalClaimedAmt;
						totalAllowableAmountForMicro += totalAllowableAmt;
						totalNonPayableForMicro += totalNonPayable;
						totalNonPayableManual += totalNonPayableManual;
						totalReasonableDeductionForMicro += totalReasonableDeduction;
						totalDisallowanceForMicro += totalDisallowances;
						totalNetPayableForMicro += netPayableAmt;
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("ICU Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						reportDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
						
						billEntryListForReport.add(reportDto);
						
						deductibleOrNonPayableReason = "";

				 }
				 
				 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				OTNonPayable = billEntryDetailsObj.getNonPayable();
			 				OTReasonableDeductions = billEntryDetailsObj.getReasonableDeduction();
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryDetailsObj.setItemName("Operation Theatre Charges");
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble((String.valueOf(i))));
			 			billEntryDetailsObj.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				totalItemValueForMicro += billEntryDetailsObj.getItemValue();
							totalAllowableAmountForMicro += billEntryDetailsObj.getAmountAllowableAmount();
							totalNonPayableForMicro += billEntryDetailsObj.getNonPayableProductBased();
							totalNonPayableManualForMicro += billEntryDetailsObj.getNonPayable();
							totalReasonableDeductionForMicro += billEntryDetailsObj.getReasonableDeduction();
							totalProportionateDeductionForMicro += billEntryDetailsObj.getProportionateDeduction();
							totalDisallowanceForMicro += billEntryDetailsObj.getTotalDisallowances();
							totalNetPayableForMicro += billEntryDetailsObj.getNetPayableAmount();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 /*else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble((String.valueOf(i))));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble((String.valueOf(i))));
					 sno = i;
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//					 billEntryListForReport.add(billEntryDetailList);

					 
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//							billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
			 			else
			 			{
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
					}
			 	}
				// }
				 /*else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						billEntryDetailList.setItemNoForView((6.0d));
						billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
						viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				else if((ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
						BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						billEntryDetailList.setItemNoForView((6.0d));
						billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
				//		viewHospitalizationObj.addBeanToList(billEntryDetailList);
//		 				billEntryListForReport.add(billEntryDetailList);

					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
					 	billEntryDetailsObj1.setItemName("	a)Medicines");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 			
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("		a.i) Medicines - within Hospital");
			 			
			 			//Below bill type key will be hardcoded once db confirms the same.
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_INSIDE_HOSPITAL);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}

			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 			}
			 			BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 			billEntryDetailsObject.setItemName("		a.ii) Medicines - outside Hospital");
			 			
			 			//Below bill type key will be hardcoded once db confirms the same.
			 			BillingHospitalisation hospObject = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject)
			 			{
			 				billEntryDetailsObject = populateHospitalizationDetails(billEntryDetailsObject, hospObject);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObject.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObject.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObject.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObject.getReasonableDeduction();
			 				
			 				billEntryDetailsObject.setNoOfDays(null);
			 				billEntryDetailsObject.setPerDayAmt(null);
			 				billEntryDetailsObject.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObject.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObject.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObject.getNonPayable();
			 				if(billEntryDetailsObject.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObject.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObject.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObject.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject.getDeductibleOrNonPayableReason();
			 				}

			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 				billEntryListForReport.add(billEntryDetailsObject);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 				billEntryListForReport.add(billEntryDetailsObject);
			 			}
				 }
				 else if((ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObj1.setItemName("	b)Consumbles");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 			
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("		 b.i) Consumables - within Hospital");
			 			// Need to hard code the bill type key after prakash confirms the same.
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.CONSUMABLES_INSIDE_HOSPITAL);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction(); 
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 			}
			 			BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 			billEntryDetailsObject.setItemName("		b.ii) Consumables - outside Hospital");
			 			
			 			//Below bill type key will be hardcoded once db confirms the same.
			 			BillingHospitalisation hospObject = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject)
			 			{
			 				billEntryDetailsObject = populateHospitalizationDetails(billEntryDetailsObject, hospObject);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObject.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObject.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObject.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObject.getReasonableDeduction();
			 				
			 				billEntryDetailsObject.setNoOfDays(null);
			 				billEntryDetailsObject.setPerDayAmt(null);
			 				billEntryDetailsObject.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObject.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObject.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObject.getNonPayable();
			 				if(billEntryDetailsObject.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObject.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObject.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObject.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject.getDeductibleOrNonPayableReason();
			 				}

			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 				billEntryListForReport.add(billEntryDetailsObject);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
		 				billEntryListForReport.add(billEntryDetailsObject);
			 			}
				 }
				 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Ìmplant/Stent/Valve/Pacemaker/Etc");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView((6.0d));
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
			 			else
			 			{
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView((6.0d));
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
				 }
				 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(13.0d);
			 			}
			 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				nonPaybleAmt = 0d;

			 				totalItemValueForMicro += billEntryDetailsObj.getItemValue();
							totalAllowableAmountForMicro += billEntryDetailsObj.getAmountAllowableAmount();
							totalNonPayableForMicro += billEntryDetailsObj.getNonPayableProductBased();
							totalNonPayableManualForMicro += billEntryDetailsObj.getNonPayable();
							totalReasonableDeductionForMicro += billEntryDetailsObj.getReasonableDeduction();
							totalProportionateDeductionForMicro += billEntryDetailsObj.getProportionateDeduction();
							totalDisallowanceForMicro += billEntryDetailsObj.getTotalDisallowances();
							totalNetPayableForMicro += billEntryDetailsObj.getNetPayableAmount();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryObj1 = new BillEntryDetailsDTO();
			 				billEntryObj1 = setDtoValuesToReportDTO(billEntryObj1, billEntryDetailsObj);
			 				billEntryObj1.setItemNoForView(7.0d);
				 			if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
				 				billEntryObj1.setItemNoForView(13.0d);
				 			}
				 			billEntryObj1.setItemName(SHAConstants.AMBULANCE_FEES);
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayableProductBased();
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayable();
//			 				billEntryObj1.setNonPayable(nonPaybleAmt);
			 				
			 				billEntryListForReport.add(billEntryObj1);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
				 {

					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
			 			{
						 billEntryDetailList.setItemNoForView(7.0d);
			 			}
					 else
					 {
						 billEntryDetailList.setItemNoForView(8.0d);
					 }
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//		 			 billEntryListForReport.add(billEntryDetailList);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)ANH Package");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Other packages");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				
			 				if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
			 					billEntryDetailsObj1.setItemNoForView(7.0d);
				 			}
			 				else
			 				{
							 billEntryDetailsObj1.setItemNoForView(8.0d);
			 				}
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);

			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
			 					billEntryDetailsObj1.setItemNoForView(7.0d);
				 			}
			 				else
			 				{
							 billEntryDetailsObj1.setItemNoForView(8.0d);
			 				}
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(8.0d);
			 			}
			 			else
			 			{
			 				billEntryDetailsObj.setItemNoForView(9.0d);
			 			}
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
				 {
						BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
						billEntryDetailsObj.setItemNoForView(10.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		}
				 }*/
				 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
					 {
						 billEntryDetailsObject.setItemNoForView(9.0d);
					 }
					 else
					 {
						 billEntryDetailsObject.setItemNoForView(10.0d);
					 }
					 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
//			 			billEntryListForReport.add(billEntryDetailsObject);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayable += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayable += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeduction += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
							 if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
							 {
								 billEntryDetailsObj1.setItemNoForView(9.0d);
							 }
							 else
							 {
								 billEntryDetailsObj1.setItemNoForView(10.0d);
							 }
							 billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";


			 			}
			 			else
			 			{
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
							 if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
							 {
								 billEntryDetailsObj1.setItemNoForView(9.0d);
							 }
							 else
							 {
								 billEntryDetailsObj1.setItemNoForView(10.0d);
							 }
							 billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
//					 		viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayable);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				
			 				
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayable = 0d;
			 				proportionateDeduction = 0d;
			 				reasonableDeduction = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";


			 			}
			 		}
				 }
				 
				 /*
				  * Added for star gift insurance policy
				  * */
				 
				 else if((ReferenceTable.PRE_NATAL_EXPENSES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(11.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PRE_NATAL_EXPENSES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalAllowableAmt += billEntryDetailsObj.getAmountAllowableAmount();
			 				fromOTToDeductionsTotalNonPayablePdtBasedAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				 else if((ReferenceTable.POST_NATAL_EXPENSES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(12.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.POST_NATAL_EXPENSES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalAllowableAmt += billEntryDetailsObj.getAmountAllowableAmount();
			 				fromOTToDeductionsTotalNonPayablePdtBasedAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				 else if((ReferenceTable.OTHERS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))
				 			{
				 				billEntryDetailsObj.setItemNoForView(13.0d);
				 			}
				 			else if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
				 				billEntryDetailsObj.setItemNoForView(10.0d);
				 			}
				 			else
				 			{
				 				billEntryDetailsObj.setItemNoForView(11.0d);
				 			}
				 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				
				 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsObj.getItemValue();
				 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsObj.getNonPayable();
				 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsObj.getProportionateDeduction();
				 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsObj.getReasonableDeduction();
				 				
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 	}
				 }
				 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemName(SHAConstants.DEDUCTIONS_AND_DISCOUNTS);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//					 billEntryListForReport.add(billEntryDetailList);
						
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(14.0d);
			 			}
			 			else if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(11.0d);
			 			}
			 			else
			 			{
			 				billEntryDetailsObj.setItemNoForView(12.0d);
			 			}
			 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt -= billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt -= billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt -= billEntryDetailsObj.getReasonableDeduction();
			 				hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				
				 
				
				 
				 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(15.0d);
			 			}
			 			else if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
			 			{
			 				billEntryDetailsObj.setItemNoForView(12.0d);
			 			}
			 			else
			 			{
			 				billEntryDetailsObj.setItemNoForView(13.0d);
			 			}
			 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
			 				fromOTToDeductionsTotalNonPayableManualAmt -= billEntryDetailsObj.getNonPayable();
			 				fromOTToDeductionsTotalProprotionateAmt -= billEntryDetailsObj.getProportionateDeduction();
			 				fromOTToDeductionsTotalReasonableAmt -= billEntryDetailsObj.getReasonableDeduction();
			 				deductions = billEntryDetailsObj.getReasonableDeduction();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setDeductionRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setDeductionRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				 /**
				  * Below block is applicable only if its a micro ins product
				  * **/
				 
					 else if((ReferenceTable.MICRO_INS_IND_OT_TILL_DEDUCTIONS).equals(billTypeKey))
					 {
						 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{/*
							 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
							 	billEntryDetailsObj.setItemName(SHAConstants.MICRO_INS_IND_OT_TILL_DEDUCTIONS);
							 	billEntryDetailsObj.setItemValue(fromOTToDeductionsTotalClaimedAmt);
							 	Double amount1 = getDiffOfTwoNumber(OTNonPayable, OTReasonableDeductions);
							 	Double amount2 = fromOTToDeductionsTotalClaimedAmt - amount1;
							 	Double proportionalDeductionVal = 100-proportionalValue;
							 	Double productConditionAmt = 0d;
							 	if(null != insuredSumInsured && insuredSumInsured == 10000)
							 	{
							 		productConditionAmt = 1500d;
							 	}
							 	else if(null != insuredSumInsured && insuredSumInsured == 20000)
							 	{
							 		productConditionAmt = 3000d;
							 	}
							 	else if(null != insuredSumInsured && insuredSumInsured == 30000)
							 	{
							 		productConditionAmt = 4500d;
							 	}
							 	Double allowableAmt = Math.min((amount2*proportionalDeductionVal), productConditionAmt);
							 	billEntryDetailsObj.setAmountAllowableAmount(allowableAmt);
							 	billEntryDetailsObj.setNonPayable(fromOTToDeductionsTotalNonPayableManualAmt);
							 	billEntryDetailsObj.setProportionateDeduction(fromOTToDeductionsTotalProprotionateAmt);
							 	billEntryDetailsObj.setReasonableDeduction(fromOTToDeductionsTotalReasonableAmt);
							 	Double amount3 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, allowableAmt);
							 	Double amount4 = getDiffOfTwoNumber(fromOTToDeductionsTotalNonPayableManualAmt, fromOTToDeductionsTotalProprotionateAmt);
							 	Double amount3 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, fromOTToDeductionsTotalNonPayableManualAmt);
							 	Double amount4 = getDiffOfTwoNumber(amount3,  billEntryDetailsObj.getReasonableDeduction());
							 	Double amount = getDiffOfTwoNumber( amount4, allowableAmt);
							 	Double diffAmountForNonPayable = getDiffOfTwoNumber(amount, fromOTToDeductionsTotalProprotionateAmt);
							 	
							 //	Double amt = fromOTToDeductionsTotalClaimedAmt - fromOTToDeductionsTotalNonPayableManualAmt -  billEntryDetailsObj.getReasonableDeduction() - allowableAmt - fromOTToDeductionsTotalProprotionateAmt;
							 	
							 	//billEntryDetailsObj.setNonPayableProductBased(getDiffOfTwoNumber(diffAmountForNonPayable, billEntryDetailsObj.getReasonableDeduction()));
							 	billEntryDetailsObj.setNonPayableProductBased(diffAmountForNonPayable);//, billEntryDetailsObj.getReasonableDeduction()));
							 	billEntryDetailsObj.setTotalDisallowances(billEntryDetailsObj.getNonPayableProductBased()+ fromOTToDeductionsTotalNonPayableManualAmt+
							 			fromOTToDeductionsTotalProprotionateAmt+fromOTToDeductionsTotalReasonableAmt);
							 	Double amount5 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, billEntryDetailsObj.getNonPayableProductBased());
							 	Double amount6 = getDiffOfTwoNumber(fromOTToDeductionsTotalNonPayableManualAmt, fromOTToDeductionsTotalProprotionateAmt);
							 	Double diffAmountForNetPayableAmt = getDiffOfTwoNumber(amount5, amount6);
							 	billEntryDetailsObj.setNetPayableAmount(Math.min(allowableAmt, getDiffOfTwoNumber(diffAmountForNetPayableAmt, fromOTToDeductionsTotalReasonableAmt)));
							 	
							 	totalItemValueForMicro += billEntryDetailsObj.getItemValue();
								totalAllowableAmountForMicro += billEntryDetailsObj.getAmountAllowableAmount();
								totalNonPayableForMicro += billEntryDetailsObj.getNonPayableProductBased();
								totalNonPayableManualForMicro += billEntryDetailsObj.getNonPayable();
								totalReasonableDeductionForMicro += billEntryDetailsObj.getReasonableDeduction();
								totalProportionateDeductionForMicro += billEntryDetailsObj.getProportionateDeduction();
								totalDisallowanceForMicro += billEntryDetailsObj.getTotalDisallowances();
								totalNetPayableForMicro += billEntryDetailsObj.getNetPayableAmount();
							 	
							 	viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
							 	
					 		*/

							 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
							 	billEntryDetailsObj.setItemName(SHAConstants.MICRO_INS_IND_OT_TILL_DEDUCTIONS);
							 	billEntryDetailsObj.setItemValue(fromOTToDeductionsTotalClaimedAmt);
							 	Double amount1 = getDiffOfTwoNumber(OTNonPayable, OTReasonableDeductions);
							 	Double amount2 = fromOTToDeductionsTotalClaimedAmt - amount1;
							 	Double proportionalDeductionVal = 100-proportionalValue;
							 	Double productConditionAmt = 0d;
							 	if(null != insuredSumInsured && insuredSumInsured == 10000)
							 	{
							 		productConditionAmt = 1500d;
							 	}
							 	else if(null != insuredSumInsured && insuredSumInsured == 20000)
							 	{
							 		productConditionAmt = 3000d;
							 	}
							 	else if(null != insuredSumInsured && insuredSumInsured == 30000)
							 	{
							 		productConditionAmt = 4500d;
							 	}
							 	Double allowableAmt = Math.min((amount2*proportionalDeductionVal), productConditionAmt);
							 //	billEntryDetailsObj.setAmountAllowableAmount(allowableAmt);
								billEntryDetailsObj.setAmountAllowableAmount(null);
							 	billEntryDetailsObj.setNonPayable(fromOTToDeductionsTotalNonPayableManualAmt);
							 	billEntryDetailsObj.setProportionateDeduction(fromOTToDeductionsTotalProprotionateAmt);
							 	billEntryDetailsObj.setReasonableDeduction(fromOTToDeductionsTotalReasonableAmt);
							 	/*Double amount3 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, allowableAmt);
							 	Double amount4 = getDiffOfTwoNumber(fromOTToDeductionsTotalNonPayableManualAmt, fromOTToDeductionsTotalProprotionateAmt);*/
							 	Double amount3 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, fromOTToDeductionsTotalNonPayableManualAmt);
							 	Double amount4 = getDiffOfTwoNumber(amount3,  billEntryDetailsObj.getReasonableDeduction());
							 	Double amount = getDiffOfTwoNumber( amount4, allowableAmt);
							 	Double diffAmountForNonPayable = getDiffOfTwoNumber(amount, fromOTToDeductionsTotalProprotionateAmt);
							 	
							 //	Double amt = fromOTToDeductionsTotalClaimedAmt - fromOTToDeductionsTotalNonPayableManualAmt -  billEntryDetailsObj.getReasonableDeduction() - allowableAmt - fromOTToDeductionsTotalProprotionateAmt;
							 	
							 	//billEntryDetailsObj.setNonPayableProductBased(getDiffOfTwoNumber(diffAmountForNonPayable, billEntryDetailsObj.getReasonableDeduction()));
							 	billEntryDetailsObj.setNonPayableProductBased(diffAmountForNonPayable);//, billEntryDetailsObj.getReasonableDeduction()));
							 	/*billEntryDetailsObj.setTotalDisallowances(billEntryDetailsObj.getNonPayableProductBased()+ fromOTToDeductionsTotalNonPayableManualAmt+
							 			fromOTToDeductionsTotalProprotionateAmt+fromOTToDeductionsTotalReasonableAmt);*/
							 	billEntryDetailsObj.setTotalDisallowances(fromOTToDeductionsTotalNonPayableManualAmt+
							 			fromOTToDeductionsTotalProprotionateAmt+fromOTToDeductionsTotalReasonableAmt);
							 /*	Double amount5 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, billEntryDetailsObj.getNonPayableProductBased());
							 	Double amount6 = getDiffOfTwoNumber(fromOTToDeductionsTotalNonPayableManualAmt, fromOTToDeductionsTotalProprotionateAmt);*/
								Double amount5 = getDiffOfTwoNumber(fromOTToDeductionsTotalClaimedAmt, fromOTToDeductionsTotalNonPayableManualAmt);
							 	Double amount6 = getDiffOfTwoNumber(fromOTToDeductionsTotalReasonableAmt, fromOTToDeductionsTotalProprotionateAmt);
							 	Double diffAmountForNetPayableAmt = getDiffOfTwoNumber(amount5, amount6);
							// 	billEntryDetailsObj.setNetPayableAmount(Math.min(allowableAmt, getDiffOfTwoNumber(diffAmountForNetPayableAmt, fromOTToDeductionsTotalReasonableAmt)));
							 	billEntryDetailsObj.setNetPayableAmount(Math.min(productConditionAmt,diffAmountForNetPayableAmt));
							 	
							 	Double amount7 = getDiffOfTwoNumber(amount6, amount5);
							 	Double diffAmt = getDiffOfTwoNumber(amount7, billEntryDetailsObj.getNetPayableAmount());
							 	billEntryDetailsObj.setNonPayableProductBased(diffAmt);
							 	
							 	totalItemValueForMicro += billEntryDetailsObj.getItemValue();
								//totalAllowableAmountForMicro += billEntryDetailsObj.getAmountAllowableAmount();
								totalNonPayableForMicro += billEntryDetailsObj.getNonPayableProductBased();
								totalNonPayableManualForMicro += billEntryDetailsObj.getNonPayable();
								totalReasonableDeductionForMicro += billEntryDetailsObj.getReasonableDeduction();
								totalProportionateDeductionForMicro += billEntryDetailsObj.getProportionateDeduction();
								totalDisallowanceForMicro += billEntryDetailsObj.getTotalDisallowances();
								totalNetPayableForMicro += billEntryDetailsObj.getNetPayableAmount();
							 	
							 	viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
							 	
					 		}
					 }
					 else if((ReferenceTable.NETWORK_HOSPITAL_DISCOUNT).equals(billTypeKey))
					 {
						 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						 viewHospitalizationObj.addBeanToList(billEntryDetailList);
							
				 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))
				 			{
				 				billEntryDetailsObj.setItemNoForView(16.0d);
				 			}
				 			else if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
				 				billEntryDetailsObj.setItemNoForView(13.0d);
				 			}
				 			else
				 			{
				 				billEntryDetailsObj.setItemNoForView(14.0d);
				 			}
				 			billEntryDetailsObj.setItemName(SHAConstants.NETWORK_HOSPITAL_DISCOUNT);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				
				 				fromOTToDeductionsTotalClaimedAmt -= billEntryDetailsObj.getItemValue();
				 				fromOTToDeductionsTotalNonPayableManualAmt -= billEntryDetailsObj.getNonPayable();
				 				fromOTToDeductionsTotalProprotionateAmt -= billEntryDetailsObj.getProportionateDeduction();
				 				fromOTToDeductionsTotalReasonableAmt -= billEntryDetailsObj.getReasonableDeduction();
				 				hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				this.bean.setNetworkHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
				 				this.bean.setNetworkHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());

				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

				 			}
				 		}
					 }
			 	}
				
		 	}
		 
		 if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
		 {
			 viewHospitalizationObj.calculateTotalForSeniorCitizen(totalItemValueForMicro, null, totalAllowableAmountForMicro, totalNonPayableForMicro, totalNonPayableManualForMicro, totalProportionateDeductionForMicro, totalReasonableDeductionForMicro, totalDisallowanceForMicro, totalNetPayableForMicro);
		 }
		 
		 if(null != this.bean)
		 {
			 bean.setHospitalDiscount(hospitalDiscount);
			 
			 bean.setDeductions(deductions);
			 try{
			 if(deductions != null && deductions > 0){
				 bean.setDeductions(deductions * -1d);
			 }
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 bean.setBillEntryDetailsDTO(billEntryListForReport);
		 }
		 
		
	}
	
	
	
	/*private void setRevisedHospitalizationTableValuesForStarSeniorCitizen(Long rodKey)
	{

		 this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.roomRentNursingChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.ambulanceChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDandEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 Double totalDisallowanceForPolicyLimitE = 0d;
		 
		 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
		 
		 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
		 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
		 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
		 List<BillingHospitalisation> roomRentList = null;
		 List<BillingHospitalisation> nursingList = null;
		 List<BillingHospitalisation> icuRoomRentList = null;
		 List<BillingHospitalisation> icuNursingList = null;
		 
		 *//**
		  * Added for bill assessment sheet. scrc enhancement
		  * *//* 
			Double insuredSumInsuredObj = dbCalculationService.getInsuredSumInsured(
					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey());
		 this.bean.setSumInsuredFromView(insuredSumInsuredObj);
		 
		 
		 Double totalItemValueInFooter = 0d;
		 Double totalProductPerDayAmtInFooter = 0d;
		 Double totalAllowableAmtInFooter = 0d;
		 Double totalNonPayablePdtBasedInFooter = 0d;
		 Double totalNonPayableManualInFooter = 0d;
		 Double totalProportionateDedInFooter = 0d;
		 Double totalReasonableDeductionInFooter = 0d;
		 Double totalDisallowancesInFooter = 0d;
		 Double totalNetPayableInFooter = 0d;
		 
		 Double packTotalItemValueInFooter = 0d;
		 Double packTotalAllowableAmtInFooter = 0d;
		 Double packTotalNonPayablePdtBasedInFooter = 0d;
		 Double packTotalNonPayableManualInFooter = 0d;
		 Double packTotalProportionateDedInFooter = 0d;
		 Double packTotalReasonableDeductionInFooter = 0d;
		 Double packTotalDisallowancesInFooter = 0d;
		 Double packTotalNetPayableInFooter = 0d;
		 // Added for ticket 3092.
	//	 Double packTotalProductPerDayAmtInFooter = 0d;
		 
		 Double nonpackTotalItemValueInFooter = 0d;
		 Double nonpackTotalProductPerDayAmtInFooter = 0d;
		 Double nonpackTotalAllowableAmtInFooter = 0d;
		 Double nonpackTotalNonPayablePdtBasedInFooter = 0d;
		 
		//To start from herenonpackTotalNonPayablePdtBasedInFooter
		 Double nonpackTotalNonPayableManualInFooter = 0d;
		 Double nonpackTotalProportionateDedInFooter = 0d;
		 Double nonpackTotalReasonableDeductionInFooter = 0d;
		 Double nonpackTotalDisallowancesInFooter = 0d;
		 Double nonpackTotalNetPayableInFooter = 0d;
		 
		 Double itemValue = 0d;
		 Double amountAllowableAmount = 0d;
		 Double nonPayableAmt = 0d;
		 Double proportionateDeductionAmt = 0d;
		 Double reasonableDeductionAmt = 0d;
		 Double totalDisallowancesAmount = 0d;
		 Double netPayableAmount = 0d;
		 String deductibleOrNonPayableReason = "";
		 String deductibleOrNonPayableReasonBilling = "";
		 String deductibleOrNonPayableReasonFA = "";
		 
		 Double deductions = 0d;
		 Double hospitalDiscount = 0d;
		 
		 Double productLimit25percent = insuredSumInsured *(25f/100f);
		 Double productLimit50percent = insuredSumInsured *(50f/100f);
		 Double productLimit75percent = insuredSumInsured *(75f/100f);
		 
		 Double ambulanceTotalClaimedAmtForAssessmentSheet = 0d;
		 
		 int sno = 0;
		 
		 
		 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<BillingHospitalisation>();
			 nursingList = new ArrayList<BillingHospitalisation>();
			 icuRoomRentList = new ArrayList<BillingHospitalisation>();
			 icuNursingList = new ArrayList<BillingHospitalisation>();
			 
			 for (BillingHospitalisation hospitalisation : newHospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				 
				 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuRoomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuNursingList.add(hospitalisation);
				 }
				 
				 
				 
				 //Need to create another list for ICU rooms and ICU nursing charges. 
				
			}
			 
		 }
		
		 if(null != billDetailsType && !billDetailsType.isEmpty())
		 {
			 //Applicable only for senior citizen red carpet pdt alone.
			 Double totalAmountIfPackAvailable = 0d;
			 Double totalNonPayableAmt = 0d;
			 Double totalProportionateDeduction = 0d;
			 Double totalReasonableDeductionAmt = 0d;
			 Double nonPayableProductBasedIfPackageIsAvailable = 0d;
			 Double totalDisallowancesIfPackageIsAvailable = 0d;
			 //Added for bill assessment sheet.
			 Double totalAmountIfPackAvailableForEList = 0d;
			 Double totalProportionateDeductionForEList = 0d;
			 Double totalNonPayableAmtForEList = 0d;
			 Double totalReasonableDeductionAmtForEList = 0d;
			 Double nonPayableProductBasedIfPackageIsAvailableForEList = 0d;
			
			 
			 int iMapSize = 0;
			 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
			 {
				iMapSize = seniorCitizenViewMap.size(); 
			 }
			// for (MasBillDetailsType masBillType : billDetailsType)
			 for(int i = 1; i<=iMapSize; i++)
			 {
				 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
				 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
				 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
				 {
					 //Adding a blank row for room rent and nursing charges.
					 
					 
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
					 //Added for report generation.
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : roomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "1."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													//billEntryDetailsDTO1.setItemNo(Long.valueOf(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													//Added for scrc format list - starts
													//Added this line to show admissible amount blank in assessment sheet.
													billEntryDetailsDTO1.setApprovedAmountForAssessmentSheet(0d);
													this.roomRentNursingChargeList.add(billEntryDetailsDTO1);
													//Added for scrc format list - ends
													
													if(billEntryDetailsDTO1.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason +=billEntryDetailsDTO1.getDeductibleOrNonPayableReason();
													}
													if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling())
													{
														deductibleOrNonPayableReason +=  billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling();
													}
													if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonFA())
													{
														deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleNonPayableReasonFA();
													}
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<nursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = nursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															//Added for scrc format list - starts -- added only for asssesment sheet, the below dto
															BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3, hospitalisationObj);
															//Added this line to show admissible amount blank in assessment sheet.
															billEntryDetailsDTO3.setApprovedAmountForAssessmentSheet(0d);
															billEntryDetailsDTO3.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO3.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO3.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO3.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO3.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO3.getItemValue();
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO3.getProportionateDeduction();
															this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
															//Added for scrc format list - ends
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleOrNonPayableReason +=billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
															if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling())
															{
																deductibleOrNonPayableReason +=  billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling();
															}
															if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonFA())
															{
																deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleNonPayableReasonFA();
															}
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													BillEntryDetailsDTO subTotalRoomRentDTO = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													billEntryDetailsDTO3.setDeductibleOrNonPayableReason(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonBilling("");
													billEntryDetailsDTO3.setDeductibleNonPayableReasonFA("");
													subTotalRoomRentDTO.setRoomRentAndNursingSubTotal(billEntryDetailsDTO3.getItemValue());
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
													//Added for scrc format list - starts
												//	this.roomRentNursingChargeList.add(billEntryDetailsDTO3); - not required as per lakshmi for srsc assessment.
													this.roomRentNursingChargeList.add(subTotalRoomRentDTO);
													//Added for scrc format list - ends
													
													if(billEntryDetailsDTO3.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason +=billEntryDetailsDTO3.getDeductibleOrNonPayableReason();
													}
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total Room Rent");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						
						if(null != billEntryDetailsDTO4.getItemValue() &&  null != billEntryDetailsDTO4.getTotalDisallowances())
						{
							Double approvedAmt = billEntryDetailsDTO4.getItemValue() - billEntryDetailsDTO4.getTotalDisallowances();
							billEntryDetailsDTO4.setApprovedAmountForAssessmentSheet(approvedAmt);
							
						}
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();

						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("Room Rent & Nursing Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						
						reportDto.setNonPayable(totalNonPayableManual);
						
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						reportDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
						reportDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
						reportDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						//Added for scrc format list - starts
						this.roomRentNursingChargeList.add(billEntryDetailsDTO4);
						//Added for scrc format list - ends
//						billEntryListForReport.add(billEntryDetailsDTO4);
						
				 }
				 //Once ICU room rent mapping is finalized, will implement the below block. 
				 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;

					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : icuRoomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "2."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													String value = "2."+ iNo;
													billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													//Added this line to show admissible amount blank in assessment sheet.
													billEntryDetailsDTO1.setApprovedAmountForAssessmentSheet(0d);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													
													//Added for scrc format list - starts
													this.roomRentNursingChargeList.add(billEntryDetailsDTO1);
													//Added for scrc format list - ends
													
													
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<icuNursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = icuNursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															//Added this line to show admissible amount blank in assessment sheet.
															billEntryDetailsDTO2.setApprovedAmountForAssessmentSheet(0d);
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															//Added for scrc format list - starts
															
															
															BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3, hospitalisationObj);
															billEntryDetailsDTO3.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO3.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO3.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO3.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO3.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO3.getItemValue();
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO3.getProportionateDeduction();
															this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
															
															
															//this.roomRentNursingChargeList.add(billEntryDetailsDTO2);
															//Added for scrc format list - ends
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													//Added only for bill assessment sheet. To show sub total in separate dto. Not used in view bill summary page.
													BillEntryDetailsDTO subTotalRoomRentDTO = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setApprovedAmountForAssessmentSheet(0d);
													billEntryDetailsDTO3.setNoOfDays(null);
													billEntryDetailsDTO3.setDeductibleOrNonPayableReason(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonBilling(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonFA(null);
													
													subTotalRoomRentDTO.setRoomRentAndNursingSubTotal(billEntryDetailsDTO3.getItemValue());

						
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
													//Added for scrc format list - starts
													//this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
													this.roomRentNursingChargeList.add(subTotalRoomRentDTO);
													//Added for scrc format list - ends
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total ICU Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						
						if(null != billEntryDetailsDTO4.getItemValue() &&  null != billEntryDetailsDTO4.getTotalDisallowances())
						{
							Double approvedAmt = billEntryDetailsDTO4.getItemValue() - billEntryDetailsDTO4.getTotalDisallowances();
							billEntryDetailsDTO4.setApprovedAmountForAssessmentSheet(approvedAmt);
							
						}
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("ICU Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						//Added for scrc format list - starts
						this.roomRentNursingChargeList.add(billEntryDetailsDTO4);
						//Added for scrc format list - ends
//						billEntryListForReport.add(billEntryDetailsDTO4);

				 }
				 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					 totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					 totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList +=  billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);


			 			}
			 		}
				 }
				 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 		 billEntryDetailsObject.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 		 billEntryDetailsObject.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		 billEntryListForReport.add(billEntryDetailsObject);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			//Added for senior citizen red carpet pdt.
			 			int iSlNo = 97;
		 				Double totalAmount = 0d;
		 				Double productLimit = 0d;
		 				Double allowableAmount = 0d;
		 				Double nonPayable = 0d;
		 				Double reasonableDeduction = 0d;
		 				Double proportionateDeduction = 0d;
		 			//	Double productPerDayAmt = 0d;
			 			List<BillingHospitalisation> hospObj = getListOfHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj && !hospObj.isEmpty())
			 			{
			 				for (BillingHospitalisation hospitalisation : hospObj) {
			 					BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 					BillEntryDetailsDTO billEntryDetailsObjForAssessment = new BillEntryDetailsDTO();
			 					//billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 			billEntryDetailsObj.setItemName("	"+Character.toString((char)iSlNo)+")"+SHAConstants.PROFESSIONAL_CHARGES);
					 			billEntryDetailsObjForAssessment.setItemName("Policy Limit D");
					 			billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospitalisation);
					 			billEntryDetailsObjForAssessment = populateHospitalizationDetails(billEntryDetailsObjForAssessment, hospitalisation);
					 			billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 			//	billEntryDetailsObj.setPerDayAmtProductBased(null);
					 			if(null != billEntryDetailsObj.getItemValue())
					 			{
					 				totalAmount += Math.round(billEntryDetailsObj.getItemValue());
					 				nonpackTotalItemValueInFooter += totalAmount;
					 				//packTotalItemValueInFooter += totalAmount;
					 			}
					 			if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 			{
					 				productLimit += Math.round(billEntryDetailsObj.getPerDayAmtProductBased());
					 				nonpackTotalProductPerDayAmtInFooter += productLimit;
					 			//	packTotalProductPerDayAmtInFooter += productLimit;
					 			}
					 			if(null != billEntryDetailsObj.getAmountAllowableAmount())
					 			{
					 				allowableAmount += Math.round(billEntryDetailsObj.getAmountAllowableAmount());
					 				nonpackTotalAllowableAmtInFooter += allowableAmount;
					 			}
					 			if(null != billEntryDetailsObj.getNonPayableProductBased())
					 			{
					 				 nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
					 				nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
					 			}
					 			if(null != billEntryDetailsObj.getNonPayable())
					 			{
					 				nonPayable += Math.round(billEntryDetailsObj.getNonPayable());
					 				nonpackTotalNonPayableManualInFooter += nonPayable;
					 			}
								if(null != billEntryDetailsObj.getProportionateDeduction())
								{
									proportionateDeduction += Math.round(billEntryDetailsObj.getProportionateDeduction());
									nonpackTotalProportionateDedInFooter += proportionateDeduction;
								}
					 			if(null != billEntryDetailsObj.getReasonableDeduction())
					 			{
					 				reasonableDeduction += Math.round(billEntryDetailsObj.getReasonableDeduction());
					 				nonpackTotalReasonableDeductionInFooter += reasonableDeduction;
					 			}
					 			if(null != billEntryDetailsObj.getTotalDisallowances())
					 			{
					 				nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 			}
					 			if(null != billEntryDetailsObj.getNetPayableAmount())
					 			{
					 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 			}
					 			
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
				 				this.policyLimitDList.add(billEntryDetailsObj);
				 				this.policyLimitDandEList.add(billEntryDetailsObjForAssessment);
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
				 				{
				 					//totalAmountIfPackAvailable = totalAmount;
				 					totalAmountIfPackAvailable += totalAmount;
				 					totalNonPayableAmt += nonPayable;
				 					totalReasonableDeductionAmt += reasonableDeduction;
				 					totalProportionateDeduction += proportionateDeduction;
				 				}
				 				iSlNo++;
							}
			 				
			 			}
			 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
			 			{
				 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
				 			if( iSlNo == 97)
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total");
				 			}
				 			else
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total (Sl.No "+Character.toString((char)(iSlNo - (iSlNo-1)))+"to"+Character.toString((char)iSlNo));
				 			}
				 			billEntryDetailsObj1.setItemValue(totalAmount);
				 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
				 			billEntryDetailsObj1.setPerDayAmtProductBased(productLimit);
				 			billEntryDetailsObj1.setAmountAllowableAmount(allowableAmount);
				 			Double amount1 = 0d;
				 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
				 			Double amount2 = 0d;
				 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); 
				 			Double nonPayablePdtBased = 0d;
				 			nonPayablePdtBased = getDiffOfTwoNumber(totalAmount, allowableAmount);
				 			billEntryDetailsObj1.setNonPayableProductBased(nonPayablePdtBased);
				 			billEntryDetailsObj1.setNonPayable(nonPayable);
				 			billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
				 			Double totalDisAllowance = nonPayablePdtBased+reasonableDeduction+nonPayable;
				 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowance);
				 			Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
				 			Double amount2 = getDiffOfTwoNumber(amount1,reasonableDeduction);
				 			billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
				 			billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmount, amount2));
				 			
				 			//Fix for the issue, 3092.
				 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
				 			
				 			billEntryDetailsObj1.setNoOfDays(null);
				 			billEntryDetailsObj1.setPerDayAmt(null);
				 			
				 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				//this.policyLimitDList.add(billEntryDetailsObj1);
			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj2 = new BillEntryDetailsDTO();
				 			
				 				billEntryDetailsObj2.setItemName("Policy Limit D");
				 			
				 				billEntryDetailsObj2.setItemValue(totalAmount);
				 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
				 				billEntryDetailsObj2.setPerDayAmtProductBased(productLimit);
				 				billEntryDetailsObj2.setAmountAllowableAmount(allowableAmount);
				 			Double amount1 = 0d;
				 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
				 			Double amount2 = 0d;
				 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); 
				 			
				 				billEntryDetailsObj2.setNonPayableProductBased(nonPayablePdtBased);
				 				billEntryDetailsObj2.setNonPayable(nonPayable);
				 				billEntryDetailsObj2.setProportionateDeduction(proportionateDeduction);
				 			//Double totalDisAllowanceObj = nonPayablePdtBased+reasonableDeduction+nonPayable;
				 			billEntryDetailsObj2.setTotalDisallowances(totalDisAllowance);
				 			//Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,reasonableDeduction);
				 				billEntryDetailsObj2.setReasonableDeduction(reasonableDeduction);
				 				billEntryDetailsObj2.setNetPayableAmount(Math.min(allowableAmount, amount2));
				 			
				 			//Fix for the issue, 3092.
				 			//nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
			 				
			 				this.policyLimitDandEList.add(billEntryDetailsObj2);


			 			}	
			 		}
				 }
				 else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }
				 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 deductibleOrNonPayableReason = "";
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 sno = i;
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//	 				 billEntryListForReport.add(billEntryDetailList);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList = billEntryDetailsObj.getProportionateDeduction();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				 billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				// }
				 else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(6.0d);
					 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }
				else if((ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
						 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
						 billEntryDetailList.setItemNoForView(6.0d);
						 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
					//	 viewHospitalizationObj.addBeanToList(billEntryDetailList);
		 				// billEntryListForReport.add(billEntryDetailList);

					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObj1.setItemName("	a)Medicines");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 			
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	  	a.i) Medicines - within Hospital");
			 			// bill type key needs to be hardcoded once hospital confirm the same.
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_INSIDE_HOSPITAL);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();;
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

							this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 				billEntryListForReport.add(billEntryDetailsObj);
			 				


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
							this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			
			 			
			 			BillEntryDetailsDTO billEntryDetailsObject1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObject1.setItemName("	  	b.ii) Medicines - outside Hospital");
			 			// bill type key needs to be hardcoded once hospital confirm the same.
			 			BillingHospitalisation hospObject1 = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject1)
			 			{
			 				billEntryDetailsObject1 = populateHospitalizationDetails(billEntryDetailsObject1, hospObject1);
			 				
			 				billEntryDetailsObject1.setNoOfDays(null);
			 				billEntryDetailsObject1.setPerDayAmt(null);
			 				billEntryDetailsObject1.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject1.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObject1.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObject1.getItemValue();
			 				if(null != billEntryDetailsObject1.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(null != billEntryDetailsObject1.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObject1.getProportionateDeduction();
			 				if(null != billEntryDetailsObject1.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObject1.getReasonableDeduction();
			 				if(null != billEntryDetailsObject1.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObject1.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObject1.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObject1.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObject1.getItemValue();
			 				if(billEntryDetailsObject1.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObject1.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(billEntryDetailsObject1.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObject1.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObject1.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObject1.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject1.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject1.getNetPayableAmount();
			 				if(billEntryDetailsObject1.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject1.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObject1);
			 				this.policyLimitEList.add(billEntryDetailsObject1);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObject1);
			 				this.policyLimitEList.add(billEntryDetailsObject1);

			 			}
			 			
				 }
				 else if((ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObj1.setItemName("	b)Consumbles");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 		
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("		b.i) Consumables - within Hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.CONSUMABLES_INSIDE_HOSPITAL);
			 			
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}

			 			BillEntryDetailsDTO billEntryDetailsObject1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObject1.setItemName("		b.ii) Consumables - outside Hospital");
			 			BillingHospitalisation hospObject = getHospDetailsForBillType(newHospitalizationList,  ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject)
			 			{
			 				billEntryDetailsObject1 = populateHospitalizationDetails(billEntryDetailsObject1, hospObject);
			 				
			 				billEntryDetailsObject1.setNoOfDays(null);
			 				billEntryDetailsObject1.setPerDayAmt(null);
			 				billEntryDetailsObject1.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject1.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObject1.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObject1.getItemValue();
			 				if(null != billEntryDetailsObject1.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(null != billEntryDetailsObject1.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObject1.getProportionateDeduction();
			 				if(null != billEntryDetailsObject1.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObject1.getReasonableDeduction();
			 				if(null != billEntryDetailsObject1.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObject1.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObject1.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObject1.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObject1.getItemValue();
			 				if(billEntryDetailsObject1.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObject1.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(billEntryDetailsObject1.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObject1.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObject1.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObject1.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject1.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject1.getNetPayableAmount();
			 				if(billEntryDetailsObject1.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject1.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObject1);
			 				this.policyLimitEList.add(billEntryDetailsObject1);


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObject1);
			 				this.policyLimitEList.add(billEntryDetailsObject1);


			 			}
				 }
				 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Ìmplant/Stent/Valve/Pacemaker/Etc");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();;
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				{
			 					totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
//			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 
				 
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				billEntryDetailsObj.setNonPayableProductBased(null);
			 			//	billEntryDetailsObj.setNonPayable(null);
			 				billEntryDetailsObj.setProportionateDeduction(null);
			 				//billEntryDetailsObj.setReasonableDeduction(null);
			 				billEntryDetailsObj.setTotalDisallowances(null);
			 				billEntryDetailsObj.setNetPayableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();;
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
				 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
				 				totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				if(billEntryDetailsObj.getNonPayableProductBased() == null){
			 					billEntryDetailsObj.setNonPayableProductBased(0d);
			 				}
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				
				 else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
				 {

					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }
				 
				 
				 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
				 {
					 
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
					 
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)ANH Package");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();;
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				{
			 					totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Other packages");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();	
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				*//***//*
			 				//this.policyLimitDList.add(billEntryDetailsObj1);


			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitDList.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitDList.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
				 {
					 //The below block needs to be checked later once the mapping is available.
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(9.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, masBillType.getKey());
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		//}
				 }
				 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailsObject.setItemNoForView(9.0d);
					 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				
			 				
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);
			 				billEntryListForReport.add(billEntryDetailsObj1);

			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHERS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
				 			billEntryDetailsObj.setItemNoForView(10.0d);
				 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				
				 				if(null != billEntryDetailsObj.getItemValue() )
				 				{
				 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
				 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
				 				}
				 				if(null != billEntryDetailsObj.getNonPayable() )
				 				{
				 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
				 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
				 				}
				 				if(null != billEntryDetailsObj.getProportionateDeduction() )
				 				{
				 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
				 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
				 				}
				 				if(null != billEntryDetailsObj.getReasonableDeduction())
				 				{
				 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
				 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
				 				}
				 				
				 				if(null != billEntryDetailsObj.getNonPayableProductBased())
				 				{
				 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
				 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
				 				}
				 				
				 				if(null != billEntryDetailsObj.getTotalDisallowances())
				 				{
				 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
				 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
				 				}
				 				
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
								this.policyLimitEList.add(billEntryDetailsObj);


				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
								this.policyLimitEList.add(billEntryDetailsObj);


				 			}
				 			//if(isPackageAvailable)
				 			//{
					 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
					 			BillEntryDetailsDTO billEntryNoPkgDTO = new BillEntryDetailsDTO();
					 			Double calculatedAmtIfPackIsAvailable = 0d;
					 			Double calculatedAmtIfPackIsAvailableForEList = 0d;
					 			Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
					 			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
			 					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey());
					 			
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
					 			{
					 				
					 				billEntryDetailsObj1.setItemName("Sub Total (Sl no 4,5, 6 & 7,8, 9 & 10)");
					 				billEntryNoPkgDTO.setItemName("Policy Limit E");
					 				
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
					 				billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailable);
					 				//billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailableForEList);
						 			
					 				Double productlimit = insuredSumInsured *(50f/100f);
						 			nonpackTotalProductPerDayAmtInFooter +=productlimit;
						 			billEntryDetailsObj1.setPerDayAmtProductBased(productlimit);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			
						 			billEntryNoPkgDTO.setPerDayAmtProductBased(productlimit);
						 			billEntryNoPkgDTO.setNonPayable(totalNonPayableAmt);
						 			billEntryNoPkgDTO.setReasonableDeduction(totalReasonableDeductionAmt);
						 			
						 			*//**
						 			 * E46 - totalAmountIfPackAvailable
						 			 * J46 - totalNonPayableAmt
						 			 * L46 - totalReasonableDeductionAmt
						 			 * G46 - productlimit
						 			 * H46 - allowableAmt
						 			 * *//*
						 			
						 			Double amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, totalNonPayableAmt); 
						 			Double amount2 = getDiffOfTwoNumber(amount1,totalReasonableDeductionAmt);
						 			
						 			Double allowableAmt = Math.min(amount2, productlimit);
							 		billEntryDetailsObj1.setAmountAllowableAmount(allowableAmt);
							 		
							 		billEntryNoPkgDTO.setAmountAllowableAmount(allowableAmt);
							 		
							 		Double amountForNonPayablePdtBased1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, allowableAmt);
							 		Double amountForNonPayablePdtBased2 = getDiffOfTwoNumber(totalNonPayableAmt,totalReasonableDeductionAmt);
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(amountForNonPayablePdtBased1, amountForNonPayablePdtBased2));
							 		billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
							 		billEntryNoPkgDTO.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
							 		
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(billEntryDetailsObj1.getItemValue() , billEntryDetailsObj1.getAmountAllowableAmount()));
							 		billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
							 		billEntryNoPkgDTO.setProportionateDeduction(totalProportionateDeduction);

							 		Double proportionateDeduc = billEntryDetailsObj1.getProportionateDeduction();
					 				Double totalDisAllowances = billEntryDetailsObj1.getNonPayableProductBased() + billEntryDetailsObj1.getReasonableDeduction() + 
						 					billEntryDetailsObj1.getNonPayable() + proportionateDeduc;
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowances);
						 			billEntryNoPkgDTO.setTotalDisallowances(totalDisAllowances);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(totalAmountIfPackAvailable,totalNonPayableAmt );
						 			Double amount4 = 0d;
						 			amount4 = getDiffOfTwoNumber(amount3,totalReasonableDeductionAmt );
						 			billEntryDetailsObj1.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
						 			billEntryNoPkgDTO.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
						 			nonpackTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			nonpackTotalAllowableAmtInFooter += billEntryDetailsObj1.getAmountAllowableAmount();
						 			nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			nonpackTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable();
						 			nonpackTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 			nonpackTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
						 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			else
					 			{
					 				//billEntryDetailsObj1.setItemName("Sub Total (Sl no 3 to 10)");
					 				*//**
					 				 * The above line is commented as per srikanth sir suggestion and below line is
					 				 * added.
					 				 * *//*
					 				billEntryDetailsObj1.setItemName("Sub Total For Package");
					 			//	billEntryNoPkgDTO.setItemName("Policy Limit E");
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
					 				//billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailable);
					 				
						 			billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
						 			
						 			
						 			billEntryNoPkgDTO.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
						 			billEntryNoPkgDTO.setNonPayable(totalNonPayableAmt);
						 			billEntryNoPkgDTO.setReasonableDeduction(totalReasonableDeductionAmt);
						 			billEntryNoPkgDTO.setProportionateDeduction(totalProportionateDeduction);
						 			
						 			Double amount1 = 0d;
						 			amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, nonPayableProductBasedIfPackageIsAvailable);
						 			Double amount2 = 0d;
						 			amount2 = getDiffOfTwoNumber(amount1, totalNonPayableAmt);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(amount2 ,totalProportionateDeduction );
						 			calculatedAmtIfPackIsAvailable = getDiffOfTwoNumber(amount3, totalReasonableDeductionAmt);
						 			
						 			Double amount4 = 0d;
						 			amount4 = getDiffOfTwoNumber(totalAmountIfPackAvailableForEList, nonPayableProductBasedIfPackageIsAvailableForEList);
						 			Double amount5 = 0d;
						 			amount5 = getDiffOfTwoNumber(amount4, totalNonPayableAmtForEList);
						 			Double amount6 = 0d;
						 			amount6 = getDiffOfTwoNumber(amount5 ,totalProportionateDeductionForEList );
						 			calculatedAmtIfPackIsAvailableForEList = getDiffOfTwoNumber(amount6, totalReasonableDeductionAmtForEList);
						 			
						 			
						 			
						 			//billEntryDetailsObj1.setProportionateDeduction(null);
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
						 			//billEntryNoPkgDTO.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
						 			
						 			if(null != billEntryDetailsObj1.getItemValue())
						 			packTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			if(null != billEntryDetailsObj1.getNonPayableProductBased())
						 			packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			if(null != billEntryDetailsObj1.getNonPayable())
						 			packTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable(); 
						 			if(null != billEntryDetailsObj1.getProportionateDeduction())
						 			packTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			if(null != billEntryDetailsObj1.getReasonableDeduction())
						 			packTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 			if(null != billEntryDetailsObj1.getTotalDisallowances())
						 			packTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
						 			if(null != billEntryDetailsObj1.getNetPayableAmount())
						 			packTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			
					 			
					 		//	billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmt, totalDisAllowances));
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
				 				{
				 					viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
					 				billEntryListForReport.add(billEntryDetailsObj1);
					 				//Added for bill assessment.
					 				bean.setPolicyETotalDisallowanceAmt(billEntryNoPkgDTO.getTotalDisallowances());
					 				this.policyLimitDandEList.add(billEntryNoPkgDTO);
				 				}
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 			{
					 				BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 				BillEntryDetailsDTO billEntryDetailsObjectForAssessment = new BillEntryDetailsDTO();
					 				
					 				billEntryDetailsObject.setItemName(SHAConstants.PACKAGE_RESTRICTIONS);
					 				billEntryDetailsObjectForAssessment.setItemName("Policy Limit E");
					 				Double productLimit = insuredSumInsured*(75f/100f);
					 				billEntryDetailsObject.setPerDayAmtProductBased(productLimit);
					 				billEntryDetailsObjectForAssessment.setPerDayAmtProductBased(productLimit);
					 				billEntryDetailsObjectForAssessment.setItemValue(totalAmountIfPackAvailableForEList);
					 				
					 				if(null != billEntryDetailsObject.getAmountAllowableAmount())
					 				totalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
					 				if(null != billEntryDetailsObject.getNonPayableProductBased())
					 				totalNonPayablePdtBasedInFooter += billEntryDetailsObject.getNonPayableProductBased();
					 				if(null != billEntryDetailsObject.getNonPayable())
									totalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
					 				if(null != billEntryDetailsObject.getProportionateDeduction())
									totalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
					 				if(null != billEntryDetailsObject.getReasonableDeduction())
									totalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
					 				if(null != billEntryDetailsObject.getTotalDisallowances())
									totalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
					 				if(null != billEntryDetailsObject.getNetPayableAmount())
					 				totalNetPayableInFooter += billEntryDetailsObject.getNetPayableAmount();
					 				
					 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
						 			{
						 				Double amountPdtBasedNonPayable = getDiffOfTwoNumber( billEntryDetailsObj1.getNonPayableProductBased(),proportionateDeduc);
						 				Double amount6 = getDiffOfTwoNumber(amount1, amountPdtBasedNonPayable);
						 				Double amount7 = getDiffOfTwoNumber(amount6, billEntryDetailsObj1.getReasonableDeduction());
						 				billEntryDetailsObj1.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailable));
						 				billEntryDetailsObjectForAssessment.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailableForEList));
						 				billEntryListForReport.add(billEntryDetailsObj1);
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
						 				//this.policyLimitDandEList.add(billEntryNoPkgDTO);

						 				
						 				Double amountallowable = Math.min(productLimit, billEntryDetailsObj1.getAmountAllowableAmount());
						 				Double amountAllowableForEList = Math.min(productLimit, billEntryDetailsObjectForAssessment.getAmountAllowableAmount());
						 				billEntryDetailsObject.setAmountAllowableAmount(amountallowable);
						 				billEntryDetailsObjectForAssessment.setAmountAllowableAmount(amountAllowableForEList);
						 				if(null != billEntryDetailsObject.getAmountAllowableAmount())
						 				{
							 				packTotalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				billEntryDetailsObject.setNonPayableProductBased(getDiffOfTwoNumber(amountallowable,billEntryDetailsObj1.getAmountAllowableAmount()));
						 				billEntryDetailsObjectForAssessment.setNonPayableProductBased(getDiffOfTwoNumber(amountAllowableForEList,billEntryDetailsObjectForAssessment.getAmountAllowableAmount()));
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					packTotalNonPayablePdtBasedInFooter +=  billEntryDetailsObject.getNonPayableProductBased();
						 				}
						 				//billEntryDetailsObject.setNonPayable(totalNonPayableAmt);
						 				billEntryDetailsObjectForAssessment.setNonPayable(totalNonPayableAmt);
						 				*//**
						 				 * As per requirement sheet, this is made blank.
						 				 * *//*
						 				billEntryDetailsObject.setNonPayable(null);
						 			//	billEntryDetailsObjectForAssessment.setNonPayable(null);
						 				if(null != billEntryDetailsObject.getNonPayable())
						 				{
						 					//packTotalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
						 				}
						 				*//**
						 				 * As per requirement sheet, this is made blank.
						 				 * *//*
						 				//billEntryDetailsObject.setProportionateDeduction(totalProportionateDeduction);
						 				billEntryDetailsObject.setProportionateDeduction(null);
						 				billEntryDetailsObjectForAssessment.setProportionateDeduction(totalProportionateDeduction);
						 				*//**
						 				 * As per requirement sheet, this is made blank.
						 				 * *//*
						 				//billEntryDetailsObjectForAssessment.setProportionateDeduction(null);
						 				if(null != billEntryDetailsObject.getProportionateDeduction())
						 				{
						 					//packTotalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
						 				}
						 				//billEntryDetailsObject.setReasonableDeduction(totalReasonableDeductionAmt);
						 				billEntryDetailsObjectForAssessment.setReasonableDeduction(totalReasonableDeductionAmt);
						 				*//**
						 				 * As per requirement sheet, this is made blank.
						 				 * *//*
						 				billEntryDetailsObject.setReasonableDeduction(null);
						 				//billEntryDetailsObjectForAssessment.setReasonableDeduction(null);
						 				if(null != billEntryDetailsObject.getReasonableDeduction())
						 				{
						 					//packTotalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
						 				}
						 				
						 				//billEntryDetailsObject.setTotalDisallowances(totalNonPayableAmt + totalProportionateDeduction + totalReasonableDeductionAmt+billEntryDetailsObject.getNonPayableProductBased());
						 				billEntryDetailsObject.setTotalDisallowances(null);
						 			//	billEntryDetailsObjectForAssessment.setTotalDisallowances(totalNonPayableAmtForEList + totalProportionateDeductionForEList + totalReasonableDeductionAmtForEList+billEntryDetailsObjectForAssessment.getNonPayableProductBased());
						 				billEntryDetailsObjectForAssessment.setTotalDisallowances(totalDisallowanceForPolicyLimitE);
						 										 										 				
						 				if(null != billEntryDetailsObject.getTotalDisallowances())
						 				{
						 					
						 					 * Below code is commented for issue 168.
						 					 
						 					
						 					*//**
						 					 * The below line which was commented is now uncommented. This is done as per kalai and BA team srikanth 
						 					 * sir suggestion. This is because there was an issue in total disallowances calculation, after displaying
						 					 * non payable manual value for all categories (done as per kalai and BA suggestion). To resolve the same, 
						 					 * they had suggested to include total disallowance in package restriction column and to exclude
						 					 * the total disallowance calculation in sub total column. 
						 					 * *//*
						 					packTotalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
						 				}
						 				billEntryDetailsObject.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
						 				billEntryDetailsObjectForAssessment.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					
						 					 * Below code is commented for issue 168.
						 					 
						 					packTotalNetPayableInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				
						 				Double claimedAmt = billEntryDetailsObjectForAssessment.getItemValue();
//						 				Double totalDisallowanceAmt =  billEntryDetailsObjectForAssessment.getTotalDisallowances();
						 				Double totalDisallowanceAmt =  (billEntryDetailsObjectForAssessment.getNonPayable() != null ? billEntryDetailsObjectForAssessment.getNonPayable() : 0d) + 
						 						(billEntryDetailsObjectForAssessment.getNonPayableProductBased() != null ? billEntryDetailsObjectForAssessment.getNonPayableProductBased() : 0d)+
						 						(billEntryDetailsObjectForAssessment.getProportionateDeduction() != null ? billEntryDetailsObjectForAssessment.getProportionateDeduction() : 0d )+
						 						(billEntryDetailsObjectForAssessment.getReasonableDeduction() != null ? billEntryDetailsObjectForAssessment.getReasonableDeduction() : 0d);
						 				if(null != claimedAmt && null != totalDisallowanceAmt)
						 				{
						 					Double approvedAmt = claimedAmt - totalDisallowanceAmt;
						 					billEntryDetailsObjectForAssessment.setApprovedAmountForAssessmentSheet(approvedAmt);
						 				}
						 				
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
						 				billEntryListForReport.add(billEntryDetailsObject);
						 				this.policyLimitDandEList.add(billEntryDetailsObjectForAssessment);
						 				

						 			}

					 				
					 			}
	
				 	}
				 }
				 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(11.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter +=  billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter += billEntryDetailsObj.getItemValue();
			 					ambulanceTotalClaimedAmtForAssessmentSheet += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 				{
			 					nonpackTotalProductPerDayAmtInFooter += billEntryDetailsObj.getPerDayAmtProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getAmountAllowableAmount())
			 				{
			 					packTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 					nonpackTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 					nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				{
			 					packTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 					nonpackTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction())
			 				{
			 					packTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 					nonpackTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				

			 				if(this.bean != null && this.bean.getPolicyDto() != null && this.bean.getPolicyDto().getProduct() != null){
			 				
				 				if(this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET)||
				 						this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)){
				 					Double netPayableAmount2 = billEntryDetailsObj.getNetPayableAmount();
				 					if(netPayableAmount2 != null && netPayableAmount2 > 0d){
					 					this.bean.setAmbulanceLimitAmount(netPayableAmount2);
					 					this.bean.setIsAmbulanceApplicable(true);
				 					}else{
				 						this.bean.setAmbulanceLimitAmount(0d);
				 						this.bean.setIsAmbulanceApplicable(false);
				 					}
				 				}

			 				
//			 				BillEntryDetailsDTO billEntryObj1 = new BillEntryDetailsDTO();
//			 				billEntryObj1 = setDtoValuesToReportDTO(billEntryObj1, billEntryDetailsObj);
//				 			billEntryObj1.setItemName(SHAConstants.AMBULANCE_FEES);
//				 			billEntryObj1.setItemNoForView(11.0d);
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayableProductBased();
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayable();
//			 				billEntryObj1.setNonPayable(nonPaybleAmt);
//			 				
//			 				billEntryListForReport.add(billEntryObj1);
			 				
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.ambulanceChargeList.add(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.ambulanceChargeList.add(billEntryDetailsObj);
			 				
			 				this.bean.setAmbulanceLimitAmount(0d);
		 					this.bean.setIsAmbulanceApplicable(false);
			 				

			 			}
			 		}
				 }
				 }
				 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(12.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(13.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					deductions = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setDeductionRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
			 }
			 
			 if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
			 {
				 this.bean.setTotalClaimedAmt(packTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(packTotalDisallowancesInFooter);
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(packTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayablePdtBasedInFooter;
				 }
				 if(packTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 this.bean.setReasonableDeductionTotalAmt(packTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(packTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(packTotalItemValueInFooter ,
							0d , packTotalAllowableAmtInFooter ,
							packTotalNonPayablePdtBasedInFooter ,
							packTotalNonPayableManualInFooter ,
							packTotalProportionateDedInFooter ,
							packTotalReasonableDeductionInFooter ,
							packTotalDisallowancesInFooter ,
							packTotalNetPayableInFooter );	
			 }
			 else
			 {
				 this.bean.setTotalClaimedAmt(nonpackTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(nonpackTotalDisallowancesInFooter);
//				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(nonpackTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayablePdtBasedInFooter;
				 }
				 if(nonpackTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 this.bean.setReasonableDeductionTotalAmt(nonpackTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(nonpackTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(nonpackTotalItemValueInFooter ,
							nonpackTotalProductPerDayAmtInFooter , nonpackTotalAllowableAmtInFooter ,
							nonpackTotalNonPayablePdtBasedInFooter ,
							nonpackTotalNonPayableManualInFooter ,
							nonpackTotalProportionateDedInFooter ,
							nonpackTotalReasonableDeductionInFooter ,
							nonpackTotalDisallowancesInFooter ,
							nonpackTotalNetPayableInFooter );	
			 }
			
		 }
		 if(null != this.bean)
		 {
			 bean.setBillEntryDetailsDTO(billEntryListForReport);
			 bean.setRoomRentNursingChargeList(roomRentNursingChargeList);
			 bean.setPolicyLimitDList(policyLimitDList);
			 bean.setPolicyLimitEList(policyLimitEList);
			 bean.setAmbulanceChargeList(ambulanceChargeList);
			 bean.setPolicyLimitDandEList(policyLimitDandEList);
			 
			 Double roomRentNursing = 0d;
			 Double totalDisallowanceForRoomRent = 0d;
			 if(null != roomRentNursingChargeList && !roomRentNursingChargeList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : roomRentNursingChargeList) {
					 if(null != billDetailsDTO.getItemValue())
					 {
						 if(!(null != billDetailsDTO.getItemName() && (billDetailsDTO.getItemName().startsWith("Sub total") || billDetailsDTO.getItemName().equalsIgnoreCase("Total Room Rent")  
								 || billDetailsDTO.getItemName().equalsIgnoreCase("Total ICU Charges")))) 
								 {
							 		roomRentNursing += billDetailsDTO.getItemValue();
							 		
								 }
						 if(null != billDetailsDTO.getItemName() && 
								 (billDetailsDTO.getItemName().equalsIgnoreCase("Total Room Rent")  
								 || billDetailsDTO.getItemName().equalsIgnoreCase("Total ICU Charges"))
								 ) 
						 {
							 totalDisallowanceForRoomRent += billDetailsDTO.getTotalDisallowances();
						 }
						 
						 //roomRentNursing += billDetailsDTO.getItemValue();
					 }
				}
			 }
			 
			 Double policyDTotal = 0d;
			 Double totalApprovedAmountDList = 0d;
			 Double totalDisallowanceForDList = 0d;
			 if(null != policyLimitDList && !policyLimitDList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitDList) {
					 if(null != billDetailsDTO.getItemValue()){
						 policyDTotal += billDetailsDTO.getItemValue();
					 }
					 if(null != billDetailsDTO.getTotalDisallowances())
					 {
						 totalDisallowanceForDList += billDetailsDTO.getTotalDisallowances();
					 }
					 if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountDList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }
				}
				 
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountDList && null != productLimit25percent && totalApprovedAmountDList > productLimit25percent){
	 					this.bean.setExceeds25percentAmt(totalApprovedAmountDList - productLimit25percent);	
	 				} 
	 				else{
	 					this.bean.setExceeds25percentAmt(0d);
	 				}
			 }
			 
			 Double policyETotal = 0d;
			 Double totalApprovedAmountEList = 0d;
			 Double totalDisallowanceForEList = 0d;
			 if(null != policyLimitEList && !policyLimitEList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitEList) {
					 if(null != billDetailsDTO.getItemValue()){
						 policyETotal += billDetailsDTO.getItemValue();
					 }
					 //if(null != billDetailsDTO.getTotalDisallowances())
					 {
						 totalDisallowanceForEList +=  (billDetailsDTO.getNonPayable() != null ? billDetailsDTO.getNonPayable() : 0d) + 
	 						(billDetailsDTO.getNonPayableProductBased() != null ? billDetailsDTO.getNonPayableProductBased() : 0d)+
	 						(billDetailsDTO.getProportionateDeduction() != null ? billDetailsDTO.getProportionateDeduction() : 0d )+
	 						(billDetailsDTO.getReasonableDeduction() != null ? billDetailsDTO.getReasonableDeduction() : 0d);
							 
						 //totatDisallowanceForEList +=  
						// totalDisallowanceForEList += billDetailsDTO.getTotalDisallowances();
					 }
					 if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountEList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }
				}
				 
				 bean.setPolicyETotalDisallowanceAmt(totalDisallowanceForEList);
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountEList && null != productLimit50percent && totalApprovedAmountEList > productLimit50percent){
	 					this.bean.setExceeds50percentAmt(totalApprovedAmountEList - productLimit50percent);	
	 				}
	 				else{
	 					this.bean.setExceeds50percentAmt(0d);
	 				}
			 }
			 
			 Double totalApprovedAmountDandEList = 0d;
			 if(null != policyLimitDandEList && !policyLimitDandEList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitDandEList) {
					 
					 if(null != billDetailsDTO && null != billDetailsDTO.getItemName() && ("Policy Limit E").equalsIgnoreCase(billDetailsDTO.getItemName()))
					 {
						 billDetailsDTO.setTotalDisallowances(totalDisallowanceForEList);
						 billDetailsDTO.setApprovedAmountForAssessmentSheet(totalApprovedAmountEList);
					 }
					 if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountDandEList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }
				}
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountDandEList && null != productLimit75percent && totalApprovedAmountDandEList > productLimit75percent){
	 					this.bean.setExceeds75percentAmt(totalApprovedAmountDandEList - productLimit75percent);	
	 				}
	 				else{
	 					this.bean.setExceeds75percentAmt(0d);
	 				}
			 }
			 
			 Double totalAmt = totalDisallowanceForRoomRent + totalDisallowanceForDList + totalDisallowanceForEList;
			 
			 if(null != bean && null != bean.getPackageFlag() && bean.getPackageFlag())
			 {
				
				this.bean.setTotalHospitalizationDeductionsForAssesment(totalAmt + this.bean.getExceeds75percentAmt());
				 //this.bean.setToatlNonPayableAmt(this.bean.getExceeds75percentAmt());
			 }
			 else if(null != bean && null != bean.getPackageFlag() && !bean.getPackageFlag())
			 {
				 
					 totalAmt = totalDisallowanceForRoomRent + totalDisallowanceForDList + bean.getPolicyETotalDisallowanceAmt();
				this.bean.setTotalHospitalizationDeductionsForAssesment(totalAmt + this.bean.getExceeds25percentAmt() + this.bean.getExceeds50percentAmt());
				 //this.bean.setToatlNonPayableAmt(this.bean.getExceeds25percentAmt() + this.bean.getExceeds50percentAmt());
			 }
			 
			 
			 bean.setTotalItemValueForAssesment(roomRentNursing + policyDTotal +  policyETotal);
			 
		 }
		
	}*/
	
	private void setRevisedHospitalizationTableValuesForStarSeniorCitizen(Long rodKey)
	{
		 this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.roomRentNursingChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDList = new ArrayList<BillEntryDetailsDTO>();	
		 
		 this.policyLimitEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.ambulanceChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDandEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 BillEntryDetailsDTO billEntryDetailsForTaxes = new BillEntryDetailsDTO();
		 
		 //setHospitalizationTableValuesForStarSeniorCitizen = new BillEntryDetailsDTO();
		 
		 Boolean isOtherAvailable = false;
		 
		 Double totalDisallowanceForPolicyLimitE = 0d;
		 
		 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
		 
		 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
		 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
		 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
		 List<BillingHospitalisation> roomRentList = null;
		 List<BillingHospitalisation> nursingList = null;
		 List<BillingHospitalisation> icuRoomRentList = null;
		 List<BillingHospitalisation> icuNursingList = null;
		 
		 /**
		  * Added for bill assessment sheet. scrc enhancement
		  * */ 
			Double insuredSumInsuredObj = dbCalculationService.getInsuredSumInsured(
 					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey(),reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
		 this.bean.setSumInsuredFromView(insuredSumInsuredObj);
		 
		 
		/* Double totalItemValueInFooter = 0d;
		 Double totalProductPerDayAmtInFooter = 0d;
		 Double totalAllowableAmtInFooter = 0d;
		 Double totalNonPayablePdtBasedInFooter = 0d;
		 Double totalNonPayableManualInFooter = 0d;
		 Double totalProportionateDedInFooter = 0d;
		 Double totalReasonableDeductionInFooter = 0d;
		 Double totalDisallowancesInFooter = 0d;
		 Double totalNetPayableInFooter = 0d;*/
		 
		 Double packTotalItemValueInFooter = 0d;
		 Double packTotalAllowableAmtInFooter = 0d;
		 Double packTotalNonPayablePdtBasedInFooter = 0d;
		 Double packTotalNonPayableManualInFooter = 0d;
		 Double packTotalProportionateDedInFooter = 0d;
		 Double packTotalReasonableDeductionInFooter = 0d;
		 Double packTotalDisallowancesInFooter = 0d;
		 Double packTotalNetPayableInFooter = 0d;
		 // Added for ticket 3092.
	//	 Double packTotalProductPerDayAmtInFooter = 0d;
		 
		 Double nonpackTotalItemValueInFooter = 0d;
		 Double nonpackTotalProductPerDayAmtInFooter = 0d;
		 Double nonpackTotalAllowableAmtInFooter = 0d;
		 Double nonpackTotalNonPayablePdtBasedInFooter = 0d;
		 
		//To start from herenonpackTotalNonPayablePdtBasedInFooter
		 Double nonpackTotalNonPayableManualInFooter = 0d;
		 Double nonpackTotalProportionateDedInFooter = 0d;
		 Double nonpackTotalReasonableDeductionInFooter = 0d;
		 Double nonpackTotalDisallowancesInFooter = 0d;
		 Double nonpackTotalNetPayableInFooter = 0d;
		 
		 Double itemValue = 0d;
		 Double amountAllowableAmount = 0d;
		 Double nonPayableAmt = 0d;
		 Double proportionateDeductionAmt = 0d;
		 Double reasonableDeductionAmt = 0d;
		 Double totalDisallowancesAmount = 0d;
		 Double netPayableAmount = 0d;
		 String deductibleOrNonPayableReason = "";
		/* String deductibleOrNonPayableReasonBilling = "";
		 String deductibleOrNonPayableReasonFA = "";*/
		 
		 Double deductions = 0d;
		 Double hospitalDiscount = 0d;
		 
		 Double productLimit25percent = insuredSumInsured *(25f/100f);
		 Double productLimit50percent = insuredSumInsured *(50f/100f);
		 Double productLimit75percent = insuredSumInsured *(75f/100f);
		 
		 Double ambulanceTotalClaimedAmtForAssessmentSheet = 0d;
		 
		 int sno = 0;
		 
		 
		 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<BillingHospitalisation>();
			 nursingList = new ArrayList<BillingHospitalisation>();
			 icuRoomRentList = new ArrayList<BillingHospitalisation>();
			 icuNursingList = new ArrayList<BillingHospitalisation>();
			 
			 for (BillingHospitalisation hospitalisation : newHospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				 
				 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuRoomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuNursingList.add(hospitalisation);
				 }
				 
				 
				 
				 //Need to create another list for ICU rooms and ICU nursing charges. 
				
			}
			 
		 }
		
		 if(null != billDetailsType && !billDetailsType.isEmpty())
		 {
			 //Applicable only for senior citizen red carpet pdt alone.
			 Double totalAmountIfPackAvailable = 0d;
			 Double totalNonPayableAmt = 0d;
			 Double totalProportionateDeduction = 0d;
			 Double totalReasonableDeductionAmt = 0d;
			 Double nonPayableProductBasedIfPackageIsAvailable = 0d;
			 Double totalDisallowancesIfPackageIsAvailable = 0d;
			 //Added for bill assessment sheet.
			 Double totalAmountIfPackAvailableForEList = 0d;
			 Double totalProportionateDeductionForEList = 0d;
			 Double totalNonPayableAmtForEList = 0d;
			 Double totalReasonableDeductionAmtForEList = 0d;
			 Double nonPayableProductBasedIfPackageIsAvailableForEList = 0d;
			
			 
			 int iMapSize = 0;
			 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
			 {
				iMapSize = seniorCitizenViewMap.size(); 
			 }
			// for (MasBillDetailsType masBillType : billDetailsType)
			 for(int i = 1; i<=iMapSize; i++)
			 {
				 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
				 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
				 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
				 {
					 //Adding a blank row for room rent and nursing charges.
					 
					 
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
					 //Added for report generation.
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										BillEntryDetailsDTO billEntryDetailsDTO1 = null;
										for (BillingHospitalisation hospitalisation : roomRentList) {
											billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "1."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													//billEntryDetailsDTO1.setItemNo(Long.valueOf(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													//Added for scrc format list - starts
													//Added this line to show admissible amount blank in assessment sheet.
													billEntryDetailsDTO1.setApprovedAmountForAssessmentSheet(0d);
													this.roomRentNursingChargeList.add(billEntryDetailsDTO1);
													//Added for scrc format list - ends
													
													if(billEntryDetailsDTO1.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason +=billEntryDetailsDTO1.getDeductibleOrNonPayableReason();
													}
													if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling())
													{
														deductibleOrNonPayableReason +=  billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling();
													}
													if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonFA())
													{
														deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleNonPayableReasonFA();
													}
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<nursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = nursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															//Added for scrc format list - starts -- added only for asssesment sheet, the below dto
															BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3, hospitalisationObj);
															billEntryDetailsDTO3.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO3.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO3.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO3.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															/*totalNursingCharges += billEntryDetailsDTO3.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO3.getItemValue();*/
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO3.getProportionateDeduction();
															//Added this line to show admissible amount blank in assessment sheet.
															billEntryDetailsDTO3.setApprovedAmountForAssessmentSheet(0d);
															this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
															//Added for scrc format list - ends
															if(billEntryDetailsDTO2.getDeductibleOrNonPayableReason() != null){
																deductibleOrNonPayableReason +=billEntryDetailsDTO2.getDeductibleOrNonPayableReason();
															}
															if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling())
															{
																deductibleOrNonPayableReason +=  billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling();
															}
															if(null != billEntryDetailsDTO1.getDeductibleNonPayableReasonFA())
															{
																deductibleOrNonPayableReason += billEntryDetailsDTO1.getDeductibleNonPayableReasonFA();
															}
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													BillEntryDetailsDTO subTotalRoomRentDTO = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													billEntryDetailsDTO3.setDeductibleOrNonPayableReason(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonBilling("");
													billEntryDetailsDTO3.setDeductibleNonPayableReasonFA("");
													subTotalRoomRentDTO.setRoomRentAndNursingSubTotal(billEntryDetailsDTO3.getItemValue());
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
													//Added for scrc format list - starts
												//	this.roomRentNursingChargeList.add(billEntryDetailsDTO3); - not required as per lakshmi for srsc assessment.
													this.roomRentNursingChargeList.add(subTotalRoomRentDTO);
													//Added for scrc format list - ends
													
													if(billEntryDetailsDTO3.getDeductibleOrNonPayableReason() != null){
														deductibleOrNonPayableReason +=billEntryDetailsDTO3.getDeductibleOrNonPayableReason();
													}
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total Room Rent");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						
						if(null != billEntryDetailsDTO4.getItemValue() &&  null != billEntryDetailsDTO4.getTotalDisallowances())
						{
							Double approvedAmt = billEntryDetailsDTO4.getItemValue() - billEntryDetailsDTO4.getTotalDisallowances();
							billEntryDetailsDTO4.setApprovedAmountForAssessmentSheet(approvedAmt);
							
						}
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();

						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("Room Rent & Nursing Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						
						reportDto.setNonPayable(totalNonPayableManual);
						
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						reportDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
						/*reportDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
						reportDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);*/
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						//Added for scrc format list - starts
						this.roomRentNursingChargeList.add(billEntryDetailsDTO4);
						//Added for scrc format list - ends
//						billEntryListForReport.add(billEntryDetailsDTO4);
						
				 }
				 //Once ICU room rent mapping is finalized, will implement the below block. 
				 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;

					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										BillEntryDetailsDTO billEntryDetailsDTO1 = null;
										for (BillingHospitalisation hospitalisation : icuRoomRentList) {
											billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "2."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													/*String value = "2."+ iNo;
													billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													*/billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													
													//Added for scrc format list - starts
													//Added this line to show admissible amount blank in assessment sheet.
													billEntryDetailsDTO1.setApprovedAmountForAssessmentSheet(0d);
													this.roomRentNursingChargeList.add(billEntryDetailsDTO1);
													//Added for scrc format list - ends
													
													
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<icuNursingList.size() ; j++)
													{
														BillEntryDetailsDTO billEntryDetailsDTO2 = null;
														BillEntryDetailsDTO billEntryDetailsDTO3 = null;
														BillingHospitalisation hospitalisationObj = icuNursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
															//Added for scrc format list - starts
															
															
															billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3, hospitalisationObj);
															billEntryDetailsDTO3.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO3.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO3.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO3.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															/*totalNursingCharges += billEntryDetailsDTO3.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO3.getItemValue();*/
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO3.getProportionateDeduction();
															//Added this line to show admissible amount blank in assessment sheet.
															billEntryDetailsDTO3.setApprovedAmountForAssessmentSheet(0d);
															this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
															
															
															//this.roomRentNursingChargeList.add(billEntryDetailsDTO2);
															//Added for scrc format list - ends
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													//Added only for bill assessment sheet. To show sub total in separate dto. Not used in view bill summary page.
													BillEntryDetailsDTO subTotalRoomRentDTO = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													billEntryDetailsDTO3.setDeductibleOrNonPayableReason(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonBilling(null);
													billEntryDetailsDTO3.setDeductibleNonPayableReasonFA(null);
													
													subTotalRoomRentDTO.setRoomRentAndNursingSubTotal(billEntryDetailsDTO3.getItemValue());

						
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
													//Added for scrc format list - starts
													//this.roomRentNursingChargeList.add(billEntryDetailsDTO3);
													this.roomRentNursingChargeList.add(subTotalRoomRentDTO);
													//Added for scrc format list - ends
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total ICU Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						
						if(null != billEntryDetailsDTO4.getItemValue() &&  null != billEntryDetailsDTO4.getTotalDisallowances())
						{
							Double approvedAmt = billEntryDetailsDTO4.getItemValue() - billEntryDetailsDTO4.getTotalDisallowances();
							billEntryDetailsDTO4.setApprovedAmountForAssessmentSheet(approvedAmt);
							
						}
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("ICU Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
						//Added for scrc format list - starts
						this.roomRentNursingChargeList.add(billEntryDetailsDTO4);
						//Added for scrc format list - ends
//						billEntryListForReport.add(billEntryDetailsDTO4);

				 }
				 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					 totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					 totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList +=  billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);


			 			}
			 		}
				 }
				 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 		 billEntryDetailsObject.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 		 billEntryDetailsObject.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		 billEntryListForReport.add(billEntryDetailsObject);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			//Added for senior citizen red carpet pdt.
			 			int iSlNo = 97;
		 				Double totalAmount = 0d;
		 				Double productLimit = 0d;
		 				Double allowableAmount = 0d;
		 				Double nonPayable = 0d;
		 				Double reasonableDeduction = 0d;
		 				Double proportionateDeduction = 0d;
		 			//	Double productPerDayAmt = 0d;
			 			List<BillingHospitalisation> hospObj = getListOfHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj && !hospObj.isEmpty())
			 			{
			 				BillEntryDetailsDTO billEntryDetailsObj = null;
			 				for (BillingHospitalisation hospitalisation : hospObj) {
			 					billEntryDetailsObj = new BillEntryDetailsDTO();
			 					BillEntryDetailsDTO billEntryDetailsObjForAssessment = new BillEntryDetailsDTO();
			 					//billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 			billEntryDetailsObj.setItemName("	"+Character.toString((char)iSlNo)+")"+SHAConstants.PROFESSIONAL_CHARGES);
					 			billEntryDetailsObjForAssessment.setItemName("Policy Limit D");
					 			billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospitalisation);
					 			billEntryDetailsObjForAssessment = populateHospitalizationDetails(billEntryDetailsObjForAssessment, hospitalisation);
					 			billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 			//	billEntryDetailsObj.setPerDayAmtProductBased(null);
					 			if(null != billEntryDetailsObj.getItemValue())
					 			{
					 				totalAmount += Math.round(billEntryDetailsObj.getItemValue());
					 				nonpackTotalItemValueInFooter += totalAmount;
					 				//packTotalItemValueInFooter += totalAmount;
					 			}
					 			if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 			{
					 				productLimit += Math.round(billEntryDetailsObj.getPerDayAmtProductBased());
					 				nonpackTotalProductPerDayAmtInFooter += productLimit;
					 			//	packTotalProductPerDayAmtInFooter += productLimit;
					 			}
					 			if(null != billEntryDetailsObj.getAmountAllowableAmount())
					 			{
					 				allowableAmount += Math.round(billEntryDetailsObj.getAmountAllowableAmount());
					 				nonpackTotalAllowableAmtInFooter += allowableAmount;
					 			}
					 			if(null != billEntryDetailsObj.getNonPayableProductBased())
					 			{
					 				 nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
					 				nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
					 			}
					 			if(null != billEntryDetailsObj.getNonPayable())
					 			{
					 				nonPayable += Math.round(billEntryDetailsObj.getNonPayable());
					 				nonpackTotalNonPayableManualInFooter += nonPayable;
					 			}
								if(null != billEntryDetailsObj.getProportionateDeduction())
								{
									proportionateDeduction += Math.round(billEntryDetailsObj.getProportionateDeduction());
									nonpackTotalProportionateDedInFooter += proportionateDeduction;
								}
					 			if(null != billEntryDetailsObj.getReasonableDeduction())
					 			{
					 				reasonableDeduction += Math.round(billEntryDetailsObj.getReasonableDeduction());
					 				nonpackTotalReasonableDeductionInFooter += reasonableDeduction;
					 			}
					 			if(null != billEntryDetailsObj.getTotalDisallowances())
					 			{
					 				nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 			}
					 			if(null != billEntryDetailsObj.getNetPayableAmount())
					 			{
					 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 			}
					 			
				 				Double totalDisallowanceAmt =  (billEntryDetailsObj.getNonPayable() != null ? billEntryDetailsObj.getNonPayable() : 0d) + 
				 						(billEntryDetailsObj.getNonPayableProductBased() != null ? billEntryDetailsObj.getNonPayableProductBased() : 0d)+
				 						(billEntryDetailsObj.getProportionateDeduction() != null ? billEntryDetailsObj.getProportionateDeduction() : 0d )+
				 						(billEntryDetailsObj.getReasonableDeduction() != null ? billEntryDetailsObj.getReasonableDeduction() : 0d);
				 				
					 			Double approvedAmountForAssessmentSheet = billEntryDetailsObj.getItemValue() != null && billEntryDetailsObj.getItemValue() != 0d ? billEntryDetailsObj.getItemValue() - totalDisallowanceAmt : 0d ;
					 					
					 			
					 			billEntryDetailsObj.setApprovedAmountForAssessmentSheet(approvedAmountForAssessmentSheet);
					 			
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
				 				this.policyLimitDList.add(billEntryDetailsObj);
				 				this.policyLimitDandEList.add(billEntryDetailsObjForAssessment);
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
				 				{
				 					//totalAmountIfPackAvailable = totalAmount;
				 					totalAmountIfPackAvailable += totalAmount;
				 					totalNonPayableAmt += nonPayable;
				 					totalReasonableDeductionAmt += reasonableDeduction;
				 					totalProportionateDeduction += proportionateDeduction;
				 				}
				 				iSlNo++;
							}
			 				
			 			}
			 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
			 			{
				 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
				 			if( iSlNo == 97)
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total");
				 			}
				 			else
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total (Sl.No "+Character.toString((char)(iSlNo - (iSlNo-1)))+"to"+Character.toString((char)iSlNo));
				 			}
				 			billEntryDetailsObj1.setItemValue(totalAmount);
				 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
				 			billEntryDetailsObj1.setPerDayAmtProductBased(productLimit);
				 			billEntryDetailsObj1.setAmountAllowableAmount(allowableAmount);
				 			/*Double amount1 = 0d;
				 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
				 			Double amount2 = 0d;
				 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); */
				 			Double nonPayablePdtBased = 0d;
				 			nonPayablePdtBased = getDiffOfTwoNumber(totalAmount, allowableAmount);
				 			billEntryDetailsObj1.setNonPayableProductBased(nonPayablePdtBased);
				 			billEntryDetailsObj1.setNonPayable(nonPayable);
				 			billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
				 			Double totalDisAllowance = nonPayablePdtBased+reasonableDeduction+nonPayable;
				 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowance);
				 			Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
				 			Double amount2 = getDiffOfTwoNumber(amount1,reasonableDeduction);
				 			billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
				 			billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmount, amount2));
				 			
				 			//Fix for the issue, 3092.
				 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
				 			
				 		/*	billEntryDetailsObj1.setNoOfDays(null);
				 			billEntryDetailsObj1.setPerDayAmt(null);*/
				 			
				 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				//this.policyLimitDList.add(billEntryDetailsObj1);
			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj2 = new BillEntryDetailsDTO();
			 				billEntryDetailsObj2.setPresenterString("");
				 			
				 				billEntryDetailsObj2.setItemName("Policy Limit D");
				 			
				 				billEntryDetailsObj2.setItemValue(totalAmount);
				 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
				 				billEntryDetailsObj2.setPerDayAmtProductBased(productLimit);
				 				billEntryDetailsObj2.setAmountAllowableAmount(allowableAmount);
				 			/*Double amount1 = 0d;
				 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
				 			Double amount2 = 0d;
				 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); */
				 			
				 				billEntryDetailsObj2.setNonPayableProductBased(nonPayablePdtBased);
				 				billEntryDetailsObj2.setNonPayable(nonPayable);
				 				billEntryDetailsObj2.setProportionateDeduction(proportionateDeduction);
				 			//Double totalDisAllowanceObj = nonPayablePdtBased+reasonableDeduction+nonPayable;
				 			billEntryDetailsObj2.setTotalDisallowances(totalDisAllowance);
				 			//Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,reasonableDeduction);
				 				billEntryDetailsObj2.setReasonableDeduction(reasonableDeduction);
				 				billEntryDetailsObj2.setNetPayableAmount(Math.min(allowableAmount, amount2));
				 			
				 			//Fix for the issue, 3092.
				 			//nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
			 				
			 				this.policyLimitDandEList.add(billEntryDetailsObj2);


			 			}	
			 		}
				 }
				 /*else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 deductibleOrNonPayableReason = "";
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 sno = i;
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//	 				 billEntryListForReport.add(billEntryDetailList);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList = billEntryDetailsObj.getProportionateDeduction();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				 billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
     			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
 			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
 			 			if(null != hospObj)
			 			{
 			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				/*billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);*/
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				// }
				/* else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(6.0d);
					 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
						 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
						 billEntryDetailList.setItemNoForView(6.0d);
						 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
						 viewHospitalizationObj.addBeanToList(billEntryDetailList);
		 				 //billEntryListForReport.add(billEntryDetailList);

					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObj1.setItemName("	a)Medicines");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 			
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	  	a.i) Medicines - within Hospital");
			 			// bill type key needs to be hardcoded once hospital confirm the same.
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_INSIDE_HOSPITAL);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();;
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

							this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			

			 			BillEntryDetailsDTO billEntryDetailsObject1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObject1.setItemName("	  	b.ii) Medicines - outside Hospital");

			 			// bill type key needs to be hardcoded once hospital confirm the same.
			 			BillingHospitalisation hospObject1 = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.MEDICINES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject1)
			 			{
			 				billEntryDetailsObject1 = populateHospitalizationDetails(billEntryDetailsObject1, hospObject1);
			 				
			 				billEntryDetailsObject1.setNoOfDays(null);
			 				billEntryDetailsObject1.setPerDayAmt(null);
			 				billEntryDetailsObject1.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject1.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObject1.getItemValue() ){
			 					totalAmountIfPackAvailable += billEntryDetailsObject1.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObject1.getItemValue();
			 				}
			 				if(null != billEntryDetailsObject1.getNonPayable() ){
			 					totalNonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObject1.getNonPayable();
			 				}
			 					
			 				if(null != billEntryDetailsObject1.getProportionateDeduction() ){
			 					
			 					totalProportionateDeduction += billEntryDetailsObject1.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObject1.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObject1.getReasonableDeduction()){
			 					totalReasonableDeductionAmt +=billEntryDetailsObject1.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObject1.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObject1.getNonPayableProductBased()){
			 					
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObject1.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObject1.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObject1.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObject1.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObject1.getItemValue();
			 				if(billEntryDetailsObject1.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObject1.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(billEntryDetailsObject1.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObject1.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObject1.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObject1.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject1.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject1.getNetPayableAmount();
			 				if(billEntryDetailsObject1.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject1.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);

			 				this.policyLimitEList.add(billEntryDetailsObject1);

			 				billEntryListForReport.add(billEntryDetailsObject1);


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObject1);

			 				billEntryListForReport.add(billEntryDetailsObject1);


			 			}
			 			
				 }
				 else if((ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObj1.setItemName("	b)Consumbles");
			 			//viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 		
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("		b.i) Consumables - within Hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, ReferenceTable.CONSUMABLES_INSIDE_HOSPITAL);
			 			
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 				billEntryListForReport.add(billEntryDetailsObj);


			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);

			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}


			 			BillEntryDetailsDTO billEntryDetailsObject1 = new BillEntryDetailsDTO();
			 			billEntryDetailsObject1.setItemName("		b.ii) Consumables - outside Hospital");
			 			BillingHospitalisation hospObject = getHospDetailsForBillType(newHospitalizationList,  ReferenceTable.CONSUMABLES_OUTSIDE_HOSPITAL);
			 			if(null != hospObject)
			 			{
			 				billEntryDetailsObject1 = populateHospitalizationDetails(billEntryDetailsObject1, hospObject);
			 				
			 				billEntryDetailsObject1.setNoOfDays(null);
			 				billEntryDetailsObject1.setPerDayAmt(null);
			 				billEntryDetailsObject1.setNoOfDaysAllowed(null);
			 				billEntryDetailsObject1.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObject1.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObject1.getItemValue();
			 				if(null != billEntryDetailsObject1.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(null != billEntryDetailsObject1.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObject1.getProportionateDeduction();
			 				if(null != billEntryDetailsObject1.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObject1.getReasonableDeduction();
			 				if(null != billEntryDetailsObject1.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObject1.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObject1.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObject1.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObject1.getItemValue();
			 				if(billEntryDetailsObject1.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObject1.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObject1.getNonPayable();
			 				if(billEntryDetailsObject1.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObject1.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObject1.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObject1.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObject1.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObject1.getNetPayableAmount();
			 				if(billEntryDetailsObject1.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObject1.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObject1);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject1);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObject1);


			 			}
				 }
				 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Ìmplant/Stent/Valve/Pacemaker/Etc");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();;
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				{
			 					totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 
				 
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				billEntryDetailsObj.setNonPayableProductBased(null);
			 			//	billEntryDetailsObj.setNonPayable(null);
			 				billEntryDetailsObj.setProportionateDeduction(null);
			 				//billEntryDetailsObj.setReasonableDeduction(null);
			 				billEntryDetailsObj.setTotalDisallowances(null);
			 				billEntryDetailsObj.setNetPayableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				/*if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();*/
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();;
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
				 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
				 				totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				if(billEntryDetailsObj.getNonPayableProductBased() == null){
			 					billEntryDetailsObj.setNonPayableProductBased(0d);
			 				}
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);
							this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				
				 /*else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
				 {

					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 
				 
				 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
				 {
					 
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
					 
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)ANH Package");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();;
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				{
			 					totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 					
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Other packages");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();	
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				/***/
			 				//this.policyLimitDList.add(billEntryDetailsObj1);


			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
				 {
					 //The below block needs to be checked later once the mapping is available.
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(9.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, masBillType.getKey());
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		//}
				 }*/
				 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailsObject.setItemNoForView(9.0d);
					 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				
			 				
			 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.policyLimitEList.add(billEntryDetailsObj);

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 				{
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 				{
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 				{
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 					totalReasonableDeductionAmtForEList += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonBilling())
			 				{
			 					deductibleOrNonPayableReason +=  billEntryDetailsObj.getDeductibleNonPayableReasonBilling();
			 				}
			 				if(null != billEntryDetailsObj.getDeductibleNonPayableReasonFA())
			 				{
			 					deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleNonPayableReasonFA();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				this.policyLimitEList.add(billEntryDetailsObj);

			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 			/*	billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);*/
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				/*billEntryDetailsObj1.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReasonBilling);
			 				billEntryDetailsObj1.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReasonFA);*/
			 				billEntryListForReport.add(billEntryDetailsObj1);

			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHERS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
				 			billEntryDetailsObj.setItemNoForView(10.0d);
				 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				
				 				if(null != billEntryDetailsObj.getItemValue() )
				 				{
				 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
				 					totalAmountIfPackAvailableForEList += billEntryDetailsObj.getItemValue();
				 				}
				 				if(null != billEntryDetailsObj.getNonPayable() )
				 				{
				 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
				 					totalNonPayableAmtForEList += billEntryDetailsObj.getNonPayable();
				 				}
				 				if(null != billEntryDetailsObj.getProportionateDeduction() )
				 				{
				 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
				 					totalProportionateDeductionForEList += billEntryDetailsObj.getProportionateDeduction();
				 				}
				 				if(null != billEntryDetailsObj.getReasonableDeduction())
				 				{
				 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
				 					totalReasonableDeductionAmtForEList +=billEntryDetailsObj.getReasonableDeduction();
				 				}
				 				
				 				if(null != billEntryDetailsObj.getNonPayableProductBased())
				 				{
				 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
				 					nonPayableProductBasedIfPackageIsAvailableForEList +=  billEntryDetailsObj.getNonPayableProductBased();
				 				}
				 				
				 				if(null != billEntryDetailsObj.getTotalDisallowances())
				 				{
				 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
				 					totalDisallowanceForPolicyLimitE += billEntryDetailsObj.getTotalDisallowances();
				 				}
				 				
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				
				 				isOtherAvailable = true;
				 				
				 				billEntryListForReport.add(billEntryDetailsObj);
								this.policyLimitEList.add(billEntryDetailsObj);


				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
								this.policyLimitEList.add(billEntryDetailsObj);


				 			}
				 			//if(isPackageAvailable)
				 			//{
					 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
					 			BillEntryDetailsDTO billEntryNoPkgDTO = new BillEntryDetailsDTO();
					 			billEntryNoPkgDTO.setPresenterString(billEntryDetailsObj.getPresenterString());
					 			Double calculatedAmtIfPackIsAvailable = 0d;
					 			Double calculatedAmtIfPackIsAvailableForEList = 0d;
					 			Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
					 			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
			 					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey(),reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
					 			
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
					 			{
					 				
					 				billEntryDetailsObj1.setItemName("Sub Total (Sl no 4,5, 6 & 7,8, 9 & 10)");
					 				billEntryNoPkgDTO.setItemName("Policy Limit E");
					 				
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
					 				billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailable);
					 				//billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailableForEList);
						 			
					 				Double productlimit = insuredSumInsured *(50f/100f);
						 			nonpackTotalProductPerDayAmtInFooter +=productlimit;
						 			billEntryDetailsObj1.setPerDayAmtProductBased(productlimit);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			
						 			billEntryNoPkgDTO.setPerDayAmtProductBased(productlimit);
						 			billEntryNoPkgDTO.setNonPayable(totalNonPayableAmt);
						 			billEntryNoPkgDTO.setReasonableDeduction(totalReasonableDeductionAmt);
						 			
						 			/**
						 			 * E46 - totalAmountIfPackAvailable
						 			 * J46 - totalNonPayableAmt
						 			 * L46 - totalReasonableDeductionAmt
						 			 * G46 - productlimit
						 			 * H46 - allowableAmt
						 			 * */
						 			
						 			Double amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, totalNonPayableAmt); 
						 			Double amount2 = getDiffOfTwoNumber(amount1,totalReasonableDeductionAmt);
						 			
						 			Double allowableAmt = Math.min(amount2, productlimit);
							 		billEntryDetailsObj1.setAmountAllowableAmount(allowableAmt);
							 		
							 		billEntryNoPkgDTO.setAmountAllowableAmount(allowableAmt);
							 		
							 		//Double amountForNonPayablePdtBased1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, allowableAmt);
							 		//Double amountForNonPayablePdtBased2 = getDiffOfTwoNumber(totalNonPayableAmt,totalReasonableDeductionAmt);
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(amountForNonPayablePdtBased1, amountForNonPayablePdtBased2));
							 		billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
							 		billEntryNoPkgDTO.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
							 		
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(billEntryDetailsObj1.getItemValue() , billEntryDetailsObj1.getAmountAllowableAmount()));
							 		billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
							 		billEntryNoPkgDTO.setProportionateDeduction(totalProportionateDeduction);

							 		Double proportionateDeduc = billEntryDetailsObj1.getProportionateDeduction();
					 				Double totalDisAllowances = billEntryDetailsObj1.getNonPayableProductBased() + billEntryDetailsObj1.getReasonableDeduction() + 
						 					billEntryDetailsObj1.getNonPayable() + proportionateDeduc;
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowances);
						 			billEntryNoPkgDTO.setTotalDisallowances(totalDisAllowances);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(totalAmountIfPackAvailable,totalNonPayableAmt );
						 			Double amount4 = 0d;
						 			amount4 = getDiffOfTwoNumber(amount3,totalReasonableDeductionAmt );
						 			billEntryDetailsObj1.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
						 			billEntryNoPkgDTO.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
						 			nonpackTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			nonpackTotalAllowableAmtInFooter += billEntryDetailsObj1.getAmountAllowableAmount();
						 			nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			nonpackTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable();
						 			nonpackTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 			nonpackTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
						 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			else
					 			{
					 				//billEntryDetailsObj1.setItemName("Sub Total (Sl no 3 to 10)");
					 				/**
					 				 * The above line is commented as per srikanth sir suggestion and below line is
					 				 * added.
					 				 * */
					 				billEntryDetailsObj1.setItemName("Sub Total For Package");
					 			//	billEntryNoPkgDTO.setItemName("Policy Limit E");
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
					 				//billEntryNoPkgDTO.setItemValue(totalAmountIfPackAvailable);
					 				
						 			billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
						 			
						 			
						 			/*billEntryNoPkgDTO.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
						 			billEntryNoPkgDTO.setNonPayable(totalNonPayableAmt);
						 			billEntryNoPkgDTO.setReasonableDeduction(totalReasonableDeductionAmt);
						 			billEntryNoPkgDTO.setProportionateDeduction(totalProportionateDeduction);*/
						 			
						 			Double amount1 = 0d;
						 			amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, nonPayableProductBasedIfPackageIsAvailable);
						 			Double amount2 = 0d;
						 			amount2 = getDiffOfTwoNumber(amount1, totalNonPayableAmt);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(amount2 ,totalProportionateDeduction );
						 			calculatedAmtIfPackIsAvailable = getDiffOfTwoNumber(amount3, totalReasonableDeductionAmt);
						 			
						 			Double amount4 = 0d;
						 			amount4 = getDiffOfTwoNumber(totalAmountIfPackAvailableForEList, nonPayableProductBasedIfPackageIsAvailableForEList);
						 			Double amount5 = 0d;
						 			amount5 = getDiffOfTwoNumber(amount4, totalNonPayableAmtForEList);
						 			Double amount6 = 0d;
						 			amount6 = getDiffOfTwoNumber(amount5 ,totalProportionateDeductionForEList );
						 			calculatedAmtIfPackIsAvailableForEList = getDiffOfTwoNumber(amount6, totalReasonableDeductionAmtForEList);
						 			
						 			
						 			
						 			//billEntryDetailsObj1.setProportionateDeduction(null);
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
						 			//billEntryNoPkgDTO.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
						 			
						 			if(null != billEntryDetailsObj1.getItemValue())
						 			packTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			if(null != billEntryDetailsObj1.getNonPayableProductBased())
						 			packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			if(null != billEntryDetailsObj1.getNonPayable())
						 			packTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable(); 
						 			if(null != billEntryDetailsObj1.getProportionateDeduction())
						 			packTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			if(null != billEntryDetailsObj1.getReasonableDeduction())
						 			packTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 			if(null != billEntryDetailsObj1.getTotalDisallowances())
						 			packTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
						 			if(null != billEntryDetailsObj1.getNetPayableAmount())
						 			packTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			
					 			
					 		//	billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmt, totalDisAllowances));
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
				 				{
				 					viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
					 				billEntryListForReport.add(billEntryDetailsObj1);
					 				//Added for bill assessment.
					 				bean.setPolicyETotalDisallowanceAmt(billEntryNoPkgDTO.getTotalDisallowances());
					 				
					 				billEntryNoPkgDTO.setDeductibleNonPayableReasonBilling(billEntryDetailsObj.getDeductibleOrNonPayableReason());
					 				billEntryNoPkgDTO.setDeductibleNonPayableReasonFA(billEntryDetailsObj.getDeductibleOrNonPayableReason());
					 				this.policyLimitDandEList.add(billEntryNoPkgDTO);
				 				}
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 			{
					 				BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 				BillEntryDetailsDTO billEntryDetailsObjectForAssessment = new BillEntryDetailsDTO();
					 				
					 				billEntryDetailsObject.setItemName(SHAConstants.PACKAGE_RESTRICTIONS);
					 				billEntryDetailsObjectForAssessment.setItemName("Policy Limit E");
					 				Double productLimit = insuredSumInsured*(75f/100f);
					 				billEntryDetailsObject.setPerDayAmtProductBased(productLimit);
					 				billEntryDetailsObjectForAssessment.setPerDayAmtProductBased(productLimit);
					 				billEntryDetailsObjectForAssessment.setItemValue(totalAmountIfPackAvailableForEList);
					 				
					 				billEntryDetailsObjectForAssessment.setDeductibleNonPayableReasonBilling(billEntryDetailsObj.getDeductibleOrNonPayableReason());
					 				billEntryDetailsObjectForAssessment.setDeductibleNonPayableReasonFA(billEntryDetailsObj.getDeductibleOrNonPayableReason());
					 				
					 				/*if(null != billEntryDetailsObject.getAmountAllowableAmount())
					 				totalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
					 				if(null != billEntryDetailsObject.getNonPayableProductBased())
					 				totalNonPayablePdtBasedInFooter += billEntryDetailsObject.getNonPayableProductBased();
					 				if(null != billEntryDetailsObject.getNonPayable())
									totalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
					 				if(null != billEntryDetailsObject.getProportionateDeduction())
									totalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
					 				if(null != billEntryDetailsObject.getReasonableDeduction())
									totalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
					 				if(null != billEntryDetailsObject.getTotalDisallowances())
									totalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
					 				if(null != billEntryDetailsObject.getNetPayableAmount())
					 				totalNetPayableInFooter += billEntryDetailsObject.getNetPayableAmount();*/
					 				
					 					/*Double amountPdtBasedNonPayable = getDiffOfTwoNumber( billEntryDetailsObj1.getNonPayableProductBased(),proportionateDeduc);
						 				Double amount6 = getDiffOfTwoNumber(amount1, amountPdtBasedNonPayable);
						 				Double amount7 = getDiffOfTwoNumber(amount6, billEntryDetailsObj1.getReasonableDeduction());*/
						 				billEntryDetailsObj1.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailable));
						 				billEntryDetailsObjectForAssessment.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailableForEList));
						 				billEntryListForReport.add(billEntryDetailsObj1);
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
						 				//this.policyLimitDandEList.add(billEntryNoPkgDTO);

						 				
						 				Double amountallowable = Math.min(productLimit, billEntryDetailsObj1.getAmountAllowableAmount());
						 				Double amountAllowableForEList = Math.min(productLimit, billEntryDetailsObjectForAssessment.getAmountAllowableAmount());
						 				billEntryDetailsObject.setAmountAllowableAmount(amountallowable);
						 				billEntryDetailsObjectForAssessment.setAmountAllowableAmount(amountAllowableForEList);
						 				if(null != billEntryDetailsObject.getAmountAllowableAmount())
						 				{
							 				packTotalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				billEntryDetailsObject.setNonPayableProductBased(getDiffOfTwoNumber(amountallowable,billEntryDetailsObj1.getAmountAllowableAmount()));
						 				billEntryDetailsObjectForAssessment.setNonPayableProductBased(getDiffOfTwoNumber(amountAllowableForEList,billEntryDetailsObjectForAssessment.getAmountAllowableAmount()));
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					packTotalNonPayablePdtBasedInFooter +=  billEntryDetailsObject.getNonPayableProductBased();
						 				}
						 				//billEntryDetailsObject.setNonPayable(totalNonPayableAmt);
						 				billEntryDetailsObjectForAssessment.setNonPayable(totalNonPayableAmt);
						 				/**
						 				 * As per requirement sheet, this is made blank.
						 				 * */
						 				billEntryDetailsObject.setNonPayable(null);
						 			//	billEntryDetailsObjectForAssessment.setNonPayable(null);
						 				if(null != billEntryDetailsObject.getNonPayable())
						 				{
						 					//packTotalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
						 				}
						 				/**
						 				 * As per requirement sheet, this is made blank.
						 				 * */
						 				//billEntryDetailsObject.setProportionateDeduction(totalProportionateDeduction);
						 				billEntryDetailsObject.setProportionateDeduction(null);
						 				billEntryDetailsObjectForAssessment.setProportionateDeduction(totalProportionateDeduction);
						 				/**
						 				 * As per requirement sheet, this is made blank.
						 				 * */
						 				//billEntryDetailsObjectForAssessment.setProportionateDeduction(null);
						 				if(null != billEntryDetailsObject.getProportionateDeduction())
						 				{
						 					//packTotalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
						 				}
						 				//billEntryDetailsObject.setReasonableDeduction(totalReasonableDeductionAmt);
						 				billEntryDetailsObjectForAssessment.setReasonableDeduction(totalReasonableDeductionAmt);
						 				/**
						 				 * As per requirement sheet, this is made blank.
						 				 * */
						 				billEntryDetailsObject.setReasonableDeduction(null);
						 				//billEntryDetailsObjectForAssessment.setReasonableDeduction(null);
						 				if(null != billEntryDetailsObject.getReasonableDeduction())
						 				{
						 					//packTotalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
						 				}
						 				
						 				//billEntryDetailsObject.setTotalDisallowances(totalNonPayableAmt + totalProportionateDeduction + totalReasonableDeductionAmt+billEntryDetailsObject.getNonPayableProductBased());
						 				billEntryDetailsObject.setTotalDisallowances(null);
						 			//	billEntryDetailsObjectForAssessment.setTotalDisallowances(totalNonPayableAmtForEList + totalProportionateDeductionForEList + totalReasonableDeductionAmtForEList+billEntryDetailsObjectForAssessment.getNonPayableProductBased());
						 				billEntryDetailsObjectForAssessment.setTotalDisallowances(totalDisallowanceForPolicyLimitE);
						 										 										 				
						 				if(null != billEntryDetailsObject.getTotalDisallowances())
						 				{
						 					/*
						 					 * Below code is commented for issue 168.
						 					 */
						 					
						 					/**
						 					 * The below line which was commented is now uncommented. This is done as per kalai and BA team srikanth 
						 					 * sir suggestion. This is because there was an issue in total disallowances calculation, after displaying
						 					 * non payable manual value for all categories (done as per kalai and BA suggestion). To resolve the same, 
						 					 * they had suggested to include total disallowance in package restriction column and to exclude
						 					 * the total disallowance calculation in sub total column. 
						 					 * */
						 					packTotalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
						 				}
						 				billEntryDetailsObject.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
						 				billEntryDetailsObjectForAssessment.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					/*
						 					 * Below code is commented for issue 168.
						 					 */
						 					packTotalNetPayableInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				
						 				Double claimedAmt = billEntryDetailsObjectForAssessment.getItemValue();
//						 				Double totalDisallowanceAmt =  billEntryDetailsObjectForAssessment.getTotalDisallowances();
						 				Double totalDisallowanceAmt =  (billEntryDetailsObjectForAssessment.getNonPayable() != null ? billEntryDetailsObjectForAssessment.getNonPayable() : 0d) + 
						 						(billEntryDetailsObjectForAssessment.getNonPayableProductBased() != null ? billEntryDetailsObjectForAssessment.getNonPayableProductBased() : 0d)+
						 						(billEntryDetailsObjectForAssessment.getProportionateDeduction() != null ? billEntryDetailsObjectForAssessment.getProportionateDeduction() : 0d )+
						 						(billEntryDetailsObjectForAssessment.getReasonableDeduction() != null ? billEntryDetailsObjectForAssessment.getReasonableDeduction() : 0d);
						 				if(null != claimedAmt && null != totalDisallowanceAmt)
						 				{
						 					Double approvedAmt = claimedAmt - totalDisallowanceAmt;
						 					billEntryDetailsObjectForAssessment.setApprovedAmountForAssessmentSheet(approvedAmt);
						 				}
						 				
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
						 				billEntryListForReport.add(billEntryDetailsObject);
						 				this.policyLimitDandEList.add(billEntryDetailsObjectForAssessment);
						 							 				
					 			}
	
				 	}
				 }
				 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(12.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter +=  billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter += billEntryDetailsObj.getItemValue();
			 					ambulanceTotalClaimedAmtForAssessmentSheet += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 				{
			 					nonpackTotalProductPerDayAmtInFooter += billEntryDetailsObj.getPerDayAmtProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getAmountAllowableAmount())
			 				{
			 					packTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 					nonpackTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 					nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				{
			 					packTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 					nonpackTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction())
			 				{
			 					packTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 					nonpackTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				

			 				if(this.bean != null && this.bean.getPolicyDto() != null && this.bean.getPolicyDto().getProduct() != null){
			 				
				 				/*if(this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET)||
				 						this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)){*/
			 					if(null != this.bean.getPolicyDto().getProduct().getKey() &&
			 							ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
				 					Double netPayableAmount2 = billEntryDetailsObj.getNetPayableAmount();
				 					if(netPayableAmount2 != null && netPayableAmount2 > 0d){
					 					this.bean.setAmbulanceLimitAmount(netPayableAmount2);
					 					this.bean.setIsAmbulanceApplicable(true);
				 					}else{
				 						this.bean.setAmbulanceLimitAmount(0d);
				 						this.bean.setIsAmbulanceApplicable(false);
				 					}
				 				}
			 				}

			 				
//			 				BillEntryDetailsDTO billEntryObj1 = new BillEntryDetailsDTO();
//			 				billEntryObj1 = setDtoValuesToReportDTO(billEntryObj1, billEntryDetailsObj);
//				 			billEntryObj1.setItemName(SHAConstants.AMBULANCE_FEES);
//				 			billEntryObj1.setItemNoForView(11.0d);
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayableProductBased();
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayable();
//			 				billEntryObj1.setNonPayable(nonPaybleAmt);
//			 				
//			 				billEntryListForReport.add(billEntryObj1);
			 				
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.ambulanceChargeList.add(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				this.ambulanceChargeList.add(billEntryDetailsObj);
			 				
			 				this.bean.setAmbulanceLimitAmount(0d);
		 					this.bean.setIsAmbulanceApplicable(false);
			 				

			 			}
			 		}
				 }
				 
				 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(13.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(14.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					deductions = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setDeductionRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setDeductionRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				 else if((ReferenceTable.TAXES_AND_OTHER_CESS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			
				 			if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))
				 			{
				 				billEntryDetailsForTaxes.setItemNoForView(11.0d);
				 			}
				 			else if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				 			{
				 				billEntryDetailsForTaxes.setItemNoForView(11.0d);
				 			}
				 			else
				 			{
				 				billEntryDetailsForTaxes.setItemNoForView(11.0d);
				 			}
				 			billEntryDetailsForTaxes.setItemName(SHAConstants.TAXES_AND_OTHER_CESS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsForTaxes = populateHospitalizationDetails(billEntryDetailsForTaxes, hospObj);
				 				packTotalItemValueInFooter += billEntryDetailsForTaxes.getItemValue();
				 				nonpackTotalItemValueInFooter += billEntryDetailsForTaxes.getItemValue();
				 				totalAmountIfPackAvailableForEList += billEntryDetailsForTaxes.getItemValue();
				 				totalNonPayableAmtForEList += billEntryDetailsForTaxes.getNonPayable();
				 				
				 				
				 				if(null != billEntryDetailsForTaxes.getReasonableDeduction())
				 				{
				 					packTotalReasonableDeductionInFooter += billEntryDetailsForTaxes.getReasonableDeduction();
				 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsForTaxes.getReasonableDeduction();
				 					totalReasonableDeductionAmtForEList +=billEntryDetailsForTaxes.getReasonableDeduction();
				 					
				 				}
				 				
				 				if(null != billEntryDetailsForTaxes.getNonPayable()){
				 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsForTaxes.getNonPayable();
				 					nonpackTotalNonPayableManualInFooter += billEntryDetailsForTaxes.getNonPayable();
				 				}
				 			
				 				
				 				if(null != billEntryDetailsForTaxes.getTotalDisallowances())
				 				{
				 					packTotalDisallowancesInFooter += billEntryDetailsForTaxes.getTotalDisallowances();
				 					nonpackTotalDisallowancesInFooter += billEntryDetailsForTaxes.getTotalDisallowances();
				 					totalDisallowanceForPolicyLimitE += billEntryDetailsForTaxes.getTotalDisallowances();
				 				}
				 				if(null != billEntryDetailsForTaxes.getNetPayableAmount())
				 				{
				 					packTotalNetPayableInFooter += billEntryDetailsForTaxes.getNetPayableAmount();
				 					nonpackTotalNetPayableInFooter += billEntryDetailsForTaxes.getNetPayableAmount();
//				 					deductions = billEntryDetailsForTaxes.getNetPayableAmount();
				 				}
				 				
				 				billEntryDetailsForTaxes.setNoOfDays(null);
				 				billEntryDetailsForTaxes.setPerDayAmt(null);
				 				billEntryDetailsForTaxes.setNoOfDaysAllowed(null);
				 				billEntryDetailsForTaxes.setPerDayAmtProductBased(null);
				 				
//				 				fromOTToDeductionsTotalClaimedAmt += billEntryDetailsForTaxes.getItemValue();
//				 				fromOTToDeductionsTotalNonPayableManualAmt += billEntryDetailsForTaxes.getNonPayable();
//				 				fromOTToDeductionsTotalProprotionateAmt += billEntryDetailsForTaxes.getProportionateDeduction();
//				 				fromOTToDeductionsTotalReasonableAmt += billEntryDetailsForTaxes.getReasonableDeduction();
				 				
				 				
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsForTaxes);
				 				if(! isOtherAvailable){
//				 					billEntryDetailsForTaxes.setItemNam(SHAConstants.OTHERS);
				 					billEntryDetailsForTaxes.setDtoName(SHAConstants.SALES_TAX_WITH_OTHERS);
				 					billEntryDetailsForTaxes.setIsOthersAvaiable(isOtherAvailable);
				 					billEntryListForReport.add(billEntryDetailsForTaxes);
				 					this.policyLimitEList.add(billEntryDetailsForTaxes);
				 				}
				 				
				 				if(null != policyLimitDandEList && !policyLimitDandEList.isEmpty())
				 				{
				 					for (BillEntryDetailsDTO billEntryDetailsDTOObj : policyLimitDandEList) {
				 						
				 						billEntryDetailsDTOObj.setItemValue(billEntryDetailsDTOObj.getItemValue() + billEntryDetailsForTaxes.getItemValue());
										
									}
				 				}
//				 				if(! isMiscelliousAvailable){
//				 					billEntryDetailsForTaxes.setItemName(SHAConstants.MISC_CHARGES);
//				 					billEntryListForReport.add(billEntryDetailsForTaxes);
//				 				}
				 				
				 				
//				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsForTaxes);
//				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 	}
				 }
				 else if((ReferenceTable.NETWORK_HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(15.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.NETWORK_HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setNetworkHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setNetworkHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		}
				 }
				 
			 }
			 
			 if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
			 {
				 this.bean.setTotalClaimedAmt(packTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(packTotalDisallowancesInFooter);
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(packTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayablePdtBasedInFooter;
				 }
				 if(packTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 this.bean.setReasonableDeductionTotalAmt(packTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(packTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(packTotalItemValueInFooter ,
							0d , packTotalAllowableAmtInFooter ,
							packTotalNonPayablePdtBasedInFooter ,
							packTotalNonPayableManualInFooter ,
							packTotalProportionateDedInFooter ,
							packTotalReasonableDeductionInFooter ,
							packTotalDisallowancesInFooter ,
							packTotalNetPayableInFooter );	
			 }
			 else
			 {
				 this.bean.setTotalClaimedAmt(nonpackTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(nonpackTotalDisallowancesInFooter);
//				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(nonpackTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayablePdtBasedInFooter;
				 }
				 if(nonpackTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 this.bean.setReasonableDeductionTotalAmt(nonpackTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(nonpackTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(nonpackTotalItemValueInFooter ,
							nonpackTotalProductPerDayAmtInFooter , nonpackTotalAllowableAmtInFooter ,
							nonpackTotalNonPayablePdtBasedInFooter ,
							nonpackTotalNonPayableManualInFooter ,
							nonpackTotalProportionateDedInFooter ,
							nonpackTotalReasonableDeductionInFooter ,
							nonpackTotalDisallowancesInFooter ,
							nonpackTotalNetPayableInFooter );	
			 }
			
		 }
		 if(null != this.bean)
		 {
			 bean.setBillEntryDetailsDTO(billEntryListForReport);
			 bean.setRoomRentNursingChargeList(roomRentNursingChargeList);
			 bean.setPolicyLimitDList(policyLimitDList);
			 
//			 if(isOtherAvailable){
//			 
//				 for (BillEntryDetailsDTO billEntryDto : policyLimitEList) {
//					 
//					 if(billEntryDto.getItemName() != null && billEntryDto.getItemName().equalsIgnoreCase(SHAConstants.OTHERS)){
//						 
//						 if(billEntryDetailsForTaxes != null){
//							 if(billEntryDetailsForTaxes.getItemValue() != null){
//								 Double itemValue2 = billEntryDto.getItemValue();
//								 billEntryDto.setItemValue(itemValue2 + billEntryDetailsForTaxes.getItemValue());
//							 }
//							 
//							 if(billEntryDetailsForTaxes.getNonPayable() != null){
//								 Double itemValue2 = billEntryDto.getNonPayable();
//								 billEntryDto.setNonPayable(itemValue2 + billEntryDetailsForTaxes.getNonPayable());
//							 }
//							 
//							 if(billEntryDetailsForTaxes.getProportionateDeduction() != null){
//				 					Double itemValue2 = billEntryDto.getProportionateDeduction();
//				 					billEntryDto.setProportionateDeduction(itemValue2 + billEntryDetailsForTaxes.getProportionateDeduction());
//				 				}
//							 
//							 if(billEntryDetailsForTaxes.getReasonableDeduction() != null){
//				 					Double itemValue2 = billEntryDto.getReasonableDeduction();
//				 					billEntryDto.setProportionateDeduction(itemValue2 + billEntryDetailsForTaxes.getReasonableDeduction());
//				 				}
//							 if(billEntryDetailsForTaxes.getNetPayableAmount() != null){
//				 					Double itemValue2 = billEntryDto.getNetPayableAmount();
//				 					billEntryDto.setProportionateDeduction(itemValue2 + billEntryDetailsForTaxes.getNetPayableAmount());
//				 				}
//						 }
//	
//					 }
//					 
//					 if(! isOtherAvailable){
//						 if(billEntryDto.getDtoName() != null && billEntryDto.getDtoName().equalsIgnoreCase(SHAConstants.SALES_TAX_WITH_OTHERS)){
//							 billEntryDto.setItemName(SHAConstants.OTHERS);
//						 }
//					 }
//					
//				}
//			 }
			 
			 bean.setPolicyLimitEList(policyLimitEList);
			 bean.setAmbulanceChargeList(ambulanceChargeList);
			 bean.setPolicyLimitDandEList(policyLimitDandEList);
			 
			 Double roomRentNursing = 0d;
			 Double totalDisallowanceForRoomRent = 0d;
			 if(null != roomRentNursingChargeList && !roomRentNursingChargeList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : roomRentNursingChargeList) {
					 if(null != billDetailsDTO.getItemValue())
					 {
						 if(!(null != billDetailsDTO.getItemName() && (billDetailsDTO.getItemName().startsWith("Sub total") || billDetailsDTO.getItemName().equalsIgnoreCase("Total Room Rent")  
								 || billDetailsDTO.getItemName().equalsIgnoreCase("Total ICU Charges")))) 
								 {
							 		roomRentNursing += billDetailsDTO.getItemValue();
							 		
								 }
						 if(null != billDetailsDTO.getItemName() && 
								 (billDetailsDTO.getItemName().equalsIgnoreCase("Total Room Rent")  
								 || billDetailsDTO.getItemName().equalsIgnoreCase("Total ICU Charges"))
								 ) 
						 {
							 totalDisallowanceForRoomRent += billDetailsDTO.getTotalDisallowances();
						 }
						 
						 //roomRentNursing += billDetailsDTO.getItemValue();
					 }
				}
			 }
			 
			 Double policyDTotal = 0d;
			 Double totalApprovedAmountDList = 0d;
			 Double totalDisallowanceForDList = 0d;
			 if(null != policyLimitDList && !policyLimitDList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitDList) {
					 if(null != billDetailsDTO.getItemValue()){
						 policyDTotal += billDetailsDTO.getItemValue();
					 }
					 if(null != billDetailsDTO.getTotalDisallowances())
					 {
						 totalDisallowanceForDList += billDetailsDTO.getTotalDisallowances();
					 }
					 if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountDList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }
				}
				 
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountDList && null != productLimit25percent && totalApprovedAmountDList > productLimit25percent){
	 					this.bean.setExceeds25percentAmt(totalApprovedAmountDList - productLimit25percent);	
	 				} 
	 				else{
	 					this.bean.setExceeds25percentAmt(0d);
	 				}
			 }
			 
			 Double policyETotal = 0d;
			 Double totalApprovedAmountEList = 0d;
			 Double totalDisallowanceForEList = 0d;
			 if(null != policyLimitEList && !policyLimitEList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitEList) {
					 if(null != billDetailsDTO.getItemValue()){
						 policyETotal += billDetailsDTO.getItemValue();
					 }
					 //if(null != billDetailsDTO.getTotalDisallowances())
					 {
						 totalDisallowanceForEList +=  (billDetailsDTO.getNonPayable() != null ? billDetailsDTO.getNonPayable() : 0d) + 
	 						(billDetailsDTO.getNonPayableProductBased() != null ? billDetailsDTO.getNonPayableProductBased() : 0d)+
	 						(billDetailsDTO.getProportionateDeduction() != null ? billDetailsDTO.getProportionateDeduction() : 0d )+
	 						(billDetailsDTO.getReasonableDeduction() != null ? billDetailsDTO.getReasonableDeduction() : 0d);
							 
						 //totatDisallowanceForEList +=  
						// totalDisallowanceForEList += billDetailsDTO.getTotalDisallowances();
					 }
					/* if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountEList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }*/
				}
				 
				 totalApprovedAmountEList = (policyETotal - totalDisallowanceForEList);
				 bean.setPolicyETotalDisallowanceAmt(totalDisallowanceForEList);
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountEList && null != productLimit50percent && totalApprovedAmountEList > productLimit50percent){
	 					this.bean.setExceeds50percentAmt(totalApprovedAmountEList - productLimit50percent);	
	 				}
	 				else{
	 					this.bean.setExceeds50percentAmt(0d);
	 				}
			 }
			 
			 Double totalApprovedAmountDandEList = 0d;
			 if(null != policyLimitDandEList && !policyLimitDandEList.isEmpty())
			 {
				 for (BillEntryDetailsDTO billDetailsDTO : policyLimitDandEList) {
					 if(null != billDetailsDTO && null != billDetailsDTO.getItemName() && ("Policy Limit E").equalsIgnoreCase(billDetailsDTO.getItemName()))
					 {
						 billDetailsDTO.setTotalDisallowances(totalDisallowanceForEList);
						 billDetailsDTO.setApprovedAmountForAssessmentSheet(totalApprovedAmountEList);
					 }
					 if(null != billDetailsDTO.getApprovedAmountForAssessmentSheet()){
						 totalApprovedAmountDandEList += billDetailsDTO.getApprovedAmountForAssessmentSheet();
					 }
				}
				// Added the exceeds product limit amount To total Nonpayable amount for  Bill assessment Sheet
	 				if(null != totalApprovedAmountDandEList && null != productLimit75percent && totalApprovedAmountDandEList > productLimit75percent){
	 					this.bean.setExceeds75percentAmt(totalApprovedAmountDandEList - productLimit75percent);	
	 				}
	 				else{
	 					this.bean.setExceeds75percentAmt(0d);
	 				}
			 }
			 
			 Double totalAmt = totalDisallowanceForRoomRent + totalDisallowanceForDList + totalDisallowanceForEList;
			 
			 if(null != bean && null != bean.getPackageFlag() && bean.getPackageFlag())
			 {
				
				this.bean.setTotalHospitalizationDeductionsForAssesment(totalAmt + this.bean.getExceeds75percentAmt());
				 //this.bean.setToatlNonPayableAmt(this.bean.getExceeds75percentAmt());
			 }
			 else if(null != bean && null != bean.getPackageFlag() && !bean.getPackageFlag())
			 {
				 
					 totalAmt = totalDisallowanceForRoomRent + totalDisallowanceForDList + bean.getPolicyETotalDisallowanceAmt();
				this.bean.setTotalHospitalizationDeductionsForAssesment(totalAmt + this.bean.getExceeds25percentAmt() + this.bean.getExceeds50percentAmt());
				 //this.bean.setToatlNonPayableAmt(this.bean.getExceeds25percentAmt() + this.bean.getExceeds50percentAmt());
			 }
			 
			 
			 bean.setTotalItemValueForAssesment(roomRentNursing + policyDTotal +  policyETotal);
			 
		 }
		 
		// return billEntryDetailsForTaxes;
		}
	
	/**
	 * 
	 * This method is not in use. Instead new method with name setRevisedHospitalizationTableValuesForStarSeniorCitizen
	 * is added above. This is done due to scrc change.
	 * 
	 * */
	private void setUnUsedRevisedHospitalizationTableValuesForStarSeniorCitizen(Long rodKey)
	{
		 this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.billEntryListForReport = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.roomRentNursingChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.ambulanceChargeList = new ArrayList<BillEntryDetailsDTO>();
		 
		 this.policyLimitDandEList = new ArrayList<BillEntryDetailsDTO>();
		 
		 Double totalDisallowanceForPolicyLimitE = 0d;
		 
		 
		 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
		 
		 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
		 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
		 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
		 List<BillingHospitalisation> roomRentList = null;
		 List<BillingHospitalisation> nursingList = null;
		 List<BillingHospitalisation> icuRoomRentList = null;
		 List<BillingHospitalisation> icuNursingList = null;
		 
		/* Double totalItemValueInFooter = 0d;
		 Double totalProductPerDayAmtInFooter = 0d;
		 Double totalAllowableAmtInFooter = 0d;
		 Double totalNonPayablePdtBasedInFooter = 0d;
		 Double totalNonPayableManualInFooter = 0d;
		 Double totalProportionateDedInFooter = 0d;
		 Double totalReasonableDeductionInFooter = 0d;
		 Double totalDisallowancesInFooter = 0d;
		 Double totalNetPayableInFooter = 0d;*/
		 
		 Double packTotalItemValueInFooter = 0d;
		 Double packTotalAllowableAmtInFooter = 0d;
		 Double packTotalNonPayablePdtBasedInFooter = 0d;
		 Double packTotalNonPayableManualInFooter = 0d;
		 Double packTotalProportionateDedInFooter = 0d;
		 Double packTotalReasonableDeductionInFooter = 0d;
		 Double packTotalDisallowancesInFooter = 0d;
		 Double packTotalNetPayableInFooter = 0d;
		 // Added for ticket 3092.
	//	 Double packTotalProductPerDayAmtInFooter = 0d;
		 
		 Double nonpackTotalItemValueInFooter = 0d;
		 Double nonpackTotalProductPerDayAmtInFooter = 0d;
		 Double nonpackTotalAllowableAmtInFooter = 0d;
		 Double nonpackTotalNonPayablePdtBasedInFooter = 0d;
		 
		//To start from herenonpackTotalNonPayablePdtBasedInFooter
		 Double nonpackTotalNonPayableManualInFooter = 0d;
		 Double nonpackTotalProportionateDedInFooter = 0d;
		 Double nonpackTotalReasonableDeductionInFooter = 0d;
		 Double nonpackTotalDisallowancesInFooter = 0d;
		 Double nonpackTotalNetPayableInFooter = 0d;
		 
		 Double itemValue = 0d;
		 Double amountAllowableAmount = 0d;
		 Double nonPayableAmt = 0d;
		 Double proportionateDeductionAmt = 0d;
		 Double reasonableDeductionAmt = 0d;
		 Double totalDisallowancesAmount = 0d;
		 Double netPayableAmount = 0d;
		 String deductibleOrNonPayableReason = "";
		 
		 Double deductions = 0d;
		 Double hospitalDiscount = 0d;
		 
		 int sno = 0;
		 
		 
		 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
		 {
			 roomRentList = new ArrayList<BillingHospitalisation>();
			 nursingList = new ArrayList<BillingHospitalisation>();
			 icuRoomRentList = new ArrayList<BillingHospitalisation>();
			 icuNursingList = new ArrayList<BillingHospitalisation>();
			 
			 for (BillingHospitalisation hospitalisation : newHospitalizationList) {
				 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 roomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 nursingList.add(hospitalisation);
				 }
				 
				 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuRoomRentList.add(hospitalisation);
				 }
				 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
					 icuNursingList.add(hospitalisation);
				 }
				 
				 
				 
				 //Need to create another list for ICU rooms and ICU nursing charges. 
				
			}
			 
		 }
		
		 if(null != billDetailsType && !billDetailsType.isEmpty())
		 {
			 //Applicable only for senior citizen red carpet pdt alone.
			 Double totalAmountIfPackAvailable = 0d;
			 Double totalNonPayableAmt = 0d;
			 Double totalProportionateDeduction = 0d;
			 Double totalReasonableDeductionAmt = 0d;
			 Double nonPayableProductBasedIfPackageIsAvailable = 0d;
			 Double totalDisallowancesIfPackageIsAvailable = 0d;
			
			 
			 int iMapSize = 0;
			 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
			 {
				iMapSize = seniorCitizenViewMap.size(); 
			 }
			// for (MasBillDetailsType masBillType : billDetailsType)
			 for(int i = 1; i<=iMapSize; i++)
			 {
				 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
				 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
				 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
				 {
					 //Adding a blank row for room rent and nursing charges.
					 
					 
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
					 //Added for report generation.
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;
					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : roomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "1."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													//billEntryDetailsDTO1.setItemNo(Long.valueOf(value));
													billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
													this.roomRentNursingChargeList.add(billEntryDetailsDTO1);
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<nursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = nursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total Room Rent");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();

						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("Room Rent & Nursing Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
//						billEntryListForReport.add(billEntryDetailsDTO4);
						
				 }
				 //Once ICU room rent mapping is finalized, will implement the below block. 
				 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
					 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailsDTO);
//					 billEntryListForReport.add(billEntryDetailsDTO);
					 
					 //Adding fields for calculating total room rent.
					 Double totalClaimedAmt = 0d;
					 Double totalAllowableAmt = 0d;
					 Double totalPdtPerDayAmt = 0d;
					 Double totalNonPayable = 0d;
					 Double totalNonPayableManual = 0d;
					 Double totalReasonableDeduction = 0d;
					 Double totalDisallowances = 0d;
					 Double netPayableAmt = 0d;
					 Double proportionalDed = 0d;
					 Double subTotalProportionateDeduction = 0d;

					 int iNo = 0;
					 if(null != rodDocSummary && !rodDocSummary.isEmpty())
					 {
						 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
							List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
							if(null != rodBillDetails && !rodBillDetails.isEmpty())
							{
								for (RODBillDetails rodBillDetails2 : rodBillDetails) {
									
									if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
									{
										
										for (BillingHospitalisation hospitalisation : icuRoomRentList) {
											BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
											if(null != hospitalisation.getItemNumber())
											{
												if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
												{
													if(iNo != 0)
													{
														String value = "2."+ iNo;
														billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
													}
													/*String value = "2."+ iNo;
													billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
													*/billEntryDetailsDTO1.setItemName("	a)Room Rent");
													billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
													billEntryDetailsDTO1.setNetPayableAmount(null);
													if(null != billEntryDetailsDTO1.getProportionateDeduction())
														subTotalProportionateDeduction += billEntryDetailsDTO1.getProportionateDeduction();
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO1);
//													billEntryListForReport.add(billEntryDetailsDTO1);
													int iSlNo = 98;
													Double totalNursingCharges = 0d;
													Double totalNursingClaimedAmt = 0d;
													for(int j = 0 ; j<icuNursingList.size() ; j++)
													{
														BillingHospitalisation hospitalisationObj = icuNursingList.get(j);
														if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
														{	
															BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
															billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
															billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
															billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
															billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
															//To calculate the total nursing charges.
															totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
															totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
															if(null != billEntryDetailsDTO2.getProportionateDeduction())
																subTotalProportionateDeduction += billEntryDetailsDTO2.getProportionateDeduction();
															viewHospitalizationObj.addBeanToList(billEntryDetailsDTO2);
//															billEntryListForReport.add(billEntryDetailsDTO2);
															iSlNo++;
														}
													}
													
													//Add code for sub total charges for each room rent and nursing charges.
													BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
													billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
													billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
													billEntryDetailsDTO3.setNoOfDays(null);
													Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
													billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
													Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
													billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
													
													Double totalDisallowanceAmt = 0d;
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
														totalDisallowanceAmt += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getProportionateDeduction();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
														totalDisallowanceAmt += billEntryDetailsDTO3.getReasonableDeduction();
													billEntryDetailsDTO3.setTotalDisallowances(totalDisallowanceAmt);
													billEntryDetailsDTO3.setNetPayableAmount(getDiffOfTwoNumber(billEntryDetailsDTO3.getItemValue(), billEntryDetailsDTO3.getTotalDisallowances()));
													viewHospitalizationObj.addBeanToList(billEntryDetailsDTO3);
//													billEntryListForReport.add(billEntryDetailsDTO3);
													totalClaimedAmt += totalClaimedAmount;
													if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
													totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
													if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
													totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
													if(null != billEntryDetailsDTO3.getNonPayableProductBased())
													totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
													if(null != billEntryDetailsDTO3.getNonPayable())
													totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
													if(null != billEntryDetailsDTO3.getReasonableDeduction())
													totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
													if(null != billEntryDetailsDTO3.getTotalDisallowances())
													totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
													if(null != billEntryDetailsDTO3.getNetPayableAmount())
													netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
													if(null != billEntryDetailsDTO3.getProportionateDeduction())
													{
														proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
													}
													iNo++;
													
												}
											}
											//Add the room rent and nursing charges, if the mapping is not available.
										}
									}
								}
								
							}
						}
					 }
					//Add code for calculating the total room rent.
						BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
						billEntryDetailsDTO4.setItemName("Total ICU Charges");
						billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
						billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
						billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
						billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
						billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
						billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
						billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
						billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
						billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
						
						packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						//packTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
						nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
						
						nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
						nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
						nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
						
						nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
						nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
						nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
						nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
						
						
						BillEntryDetailsDTO reportDto = new BillEntryDetailsDTO();
						reportDto.setItemNoForView(Double.parseDouble(String.valueOf(i)));
						reportDto.setItemName("ICU Charges");
						reportDto.setItemValue(totalClaimedAmt);
						reportDto.setAmountAllowableAmount(totalAllowableAmt);
//						reportDto.setNonPayableProductBased(totalNonPayable);
						if(totalNonPayable != null){
							totalNonPayableManual += totalNonPayable;
						}
						reportDto.setNonPayable(totalNonPayableManual);
						reportDto.setReasonableDeduction(totalReasonableDeduction);
						reportDto.setTotalDisallowances(totalDisallowances);
						reportDto.setNetPayableAmount(netPayableAmt);
						
						billEntryListForReport.add(reportDto);
						
						viewHospitalizationObj.addBeanToList(billEntryDetailsDTO4);
//						billEntryListForReport.add(billEntryDetailsDTO4);

				 }
				 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 		 billEntryDetailsObject.setItemNoForView(Double.parseDouble(String.valueOf(i)));
			 		 billEntryDetailsObject.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		 billEntryListForReport.add(billEntryDetailsObject);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			//Added for senior citizen red carpet pdt.
			 			int iSlNo = 97;
		 				Double totalAmount = 0d;
		 				Double productLimit = 0d;
		 				Double allowableAmount = 0d;
		 				Double nonPayable = 0d;
		 				Double reasonableDeduction = 0d;
		 				Double proportionateDeduction = 0d;
		 			//	Double productPerDayAmt = 0d;
			 			List<BillingHospitalisation> hospObj = getListOfHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj && !hospObj.isEmpty())
			 			{
			 				for (BillingHospitalisation hospitalisation : hospObj) {
			 					BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 					//billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 			billEntryDetailsObj.setItemName("	"+Character.toString((char)iSlNo)+")"+SHAConstants.PROFESSIONAL_CHARGES);
					 			billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospitalisation);
					 			billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 			//	billEntryDetailsObj.setPerDayAmtProductBased(null);
					 			if(null != billEntryDetailsObj.getItemValue())
					 			{
					 				totalAmount += Math.round(billEntryDetailsObj.getItemValue());
					 				nonpackTotalItemValueInFooter += totalAmount;
					 				//packTotalItemValueInFooter += totalAmount;
					 			}
					 			if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 			{
					 				productLimit += Math.round(billEntryDetailsObj.getPerDayAmtProductBased());
					 				nonpackTotalProductPerDayAmtInFooter += productLimit;
					 			//	packTotalProductPerDayAmtInFooter += productLimit;
					 			}
					 			if(null != billEntryDetailsObj.getAmountAllowableAmount())
					 			{
					 				allowableAmount += Math.round(billEntryDetailsObj.getAmountAllowableAmount());
					 				nonpackTotalAllowableAmtInFooter += allowableAmount;
					 			}
					 			if(null != billEntryDetailsObj.getNonPayableProductBased())
					 			{
					 				 nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
					 				nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
					 			}
					 			if(null != billEntryDetailsObj.getNonPayable())
					 			{
					 				nonPayable += Math.round(billEntryDetailsObj.getNonPayable());
					 				nonpackTotalNonPayableManualInFooter += nonPayable;
					 			}
								if(null != billEntryDetailsObj.getProportionateDeduction())
								{
									proportionateDeduction += Math.round(billEntryDetailsObj.getProportionateDeduction());
									nonpackTotalProportionateDedInFooter += proportionateDeduction;
								}
					 			if(null != billEntryDetailsObj.getReasonableDeduction())
					 			{
					 				reasonableDeduction += Math.round(billEntryDetailsObj.getReasonableDeduction());
					 				nonpackTotalReasonableDeductionInFooter += reasonableDeduction;
					 			}
					 			if(null != billEntryDetailsObj.getTotalDisallowances())
					 			{
					 				nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 			}
					 			if(null != billEntryDetailsObj.getNetPayableAmount())
					 			{
					 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 			}
					 			
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
				 				{
				 					//totalAmountIfPackAvailable = totalAmount;
				 					totalAmountIfPackAvailable += totalAmount;
				 					totalNonPayableAmt += nonPayable;
				 					totalReasonableDeductionAmt += reasonableDeduction;
				 					totalProportionateDeduction += proportionateDeduction;
				 				}
				 				iSlNo++;
							}
			 				
			 			}
			 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
			 			{
				 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
				 			if( iSlNo == 97)
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total");
				 			}
				 			else
				 			{
				 				billEntryDetailsObj1.setItemName("Sub Total (Sl.No "+Character.toString((char)(iSlNo - (iSlNo-1)))+"to"+Character.toString((char)iSlNo));
				 			}
				 			billEntryDetailsObj1.setItemValue(totalAmount);
				 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
				 			billEntryDetailsObj1.setPerDayAmtProductBased(productLimit);
				 			billEntryDetailsObj1.setAmountAllowableAmount(allowableAmount);
				 			/*Double amount1 = 0d;
				 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
				 			Double amount2 = 0d;
				 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); */
				 			Double nonPayablePdtBased = 0d;
				 			nonPayablePdtBased = getDiffOfTwoNumber(totalAmount, allowableAmount);
				 			billEntryDetailsObj1.setNonPayableProductBased(nonPayablePdtBased);
				 			billEntryDetailsObj1.setNonPayable(nonPayable);
				 			billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
				 			Double totalDisAllowance = nonPayablePdtBased+reasonableDeduction+nonPayable;
				 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowance);
				 			Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
				 			//Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
				 			Double amount2 = getDiffOfTwoNumber(amount1,reasonableDeduction);
				 			billEntryDetailsObj1.setReasonableDeduction(reasonableDeduction);
				 			billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmount, amount2));
				 			
				 			//Fix for the issue, 3092.
				 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
				 			
				 		/*	billEntryDetailsObj1.setNoOfDays(null);
				 			billEntryDetailsObj1.setPerDayAmt(null);*/
				 			
				 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
			 				billEntryListForReport.add(billEntryDetailsObj1);

			 			}	
			 		}
				 }
				 /*else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 sno = i;
					 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//	 				 billEntryListForReport.add(billEntryDetailList);

			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				 billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemName(SHAConstants.INVESTIGATION_DIAG);
			 				billEntryDetailsObj1.setItemNoForView(Double.valueOf(sno));
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
					}
			 	}
				// }
				/* else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(6.0d);
					 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 else if((ReferenceTable.MEDICINES).equals(billTypeKey))
				 {
						 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
						 billEntryDetailList.setItemNoForView(6.0d);
						 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
						 viewHospitalizationObj.addBeanToList(billEntryDetailList);
//		 				 billEntryListForReport.add(billEntryDetailList);

					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)Medicines");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 else if((ReferenceTable.CONSUMABLES).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Consumbles");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
				 {
					 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Ìmplant/Stent/Valve/Pacemaker/Etc");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(6.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";

//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
				 }
				 
				 
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES_CHARGES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				billEntryDetailsObj.setNonPayableProductBased(null);
			 			//	billEntryDetailsObj.setNonPayable(null);
			 				billEntryDetailsObj.setProportionateDeduction(null);
			 				//billEntryDetailsObj.setReasonableDeduction(null);
			 				billEntryDetailsObj.setTotalDisallowances(null);
			 				billEntryDetailsObj.setNetPayableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				/*if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();*/
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
				 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				if(billEntryDetailsObj.getNonPayableProductBased() == null){
			 					billEntryDetailsObj.setNonPayableProductBased(0d);
			 				}
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
							billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 
				
				 /*else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
				 {

					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
					// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
				 }*/
				 
				 
				 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
				 {
					 
					 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
						// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailList.setItemNoForView(8.0d);
					 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
					 viewHospitalizationObj.addBeanToList(billEntryDetailList);
					 
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a)ANH Package");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	c)Other packages");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setAmountAllowableAmount(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(8.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.PACKAGES_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);

			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
			 				
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(7.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				/* else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
				 {
					 //The below block needs to be checked later once the mapping is available.
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(9.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
			 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, masBillType.getKey());
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		//}
				 }*/
				 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
				 {
					 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 billEntryDetailsObject.setItemNoForView(9.0d);
					 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
			 		 viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				
			 				
			 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue() )
			 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				if(null != billEntryDetailsObj.getNonPayable() )
			 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(null != billEntryDetailsObj.getProportionateDeduction() )
			 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
			 				
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
			 				
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
			 				
			 				itemValue += billEntryDetailsObj.getItemValue();
			 				if(billEntryDetailsObj.getAmountAllowableAmount() != null){
			 					amountAllowableAmount += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				nonPayableAmt += billEntryDetailsObj.getNonPayable();
			 				if(billEntryDetailsObj.getNonPayableProductBased() != null){
			 					nonPayableAmt += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				proportionateDeductionAmt += billEntryDetailsObj.getProportionateDeduction();
			 				reasonableDeductionAmt += billEntryDetailsObj.getReasonableDeduction();
			 				totalDisallowancesAmount += billEntryDetailsObj.getTotalDisallowances();
			 				netPayableAmount += billEntryDetailsObj.getNetPayableAmount();
			 				if(billEntryDetailsObj.getDeductibleOrNonPayableReason() != null){
			 				deductibleOrNonPayableReason += billEntryDetailsObj.getDeductibleOrNonPayableReason();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);
			 				
			 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
			 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
			 				
			 				
			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				
			 				BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
			 				
			 				billEntryDetailsObj1 = setDtoValuesToReportDTO(billEntryDetailsObj1,billEntryDetailsObj);
			 				billEntryDetailsObj1.setItemNoForView(9.0d);
			 				billEntryDetailsObj1.setItemName(SHAConstants.MISC_CHARGES);
			 				
			 				billEntryDetailsObj1.setItemValue(itemValue);
			 				billEntryDetailsObj1.setAmountAllowableAmount(amountAllowableAmount);
			 				billEntryDetailsObj1.setNonPayable(nonPayableAmt);
			 				billEntryDetailsObj1.setProportionateDeduction(proportionateDeductionAmt);
			 				billEntryDetailsObj1.setReasonableDeduction(reasonableDeductionAmt);
			 				billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesAmount);
			 				billEntryDetailsObj1.setNetPayableAmount(netPayableAmount);
			 				billEntryDetailsObj1.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
			 				billEntryListForReport.add(billEntryDetailsObj1);

			 				itemValue = 0d;
			 				amountAllowableAmount = 0d;
			 				nonPayableAmt = 0d;
			 				proportionateDeductionAmt = 0d;
			 				reasonableDeductionAmt = 0d;
			 				totalDisallowancesAmount = 0d;
			 				netPayableAmount = 0d;
			 				deductibleOrNonPayableReason = "";
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.OTHERS).equals(billTypeKey))
				 {
					 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 		{
				 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
				 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
				 			billEntryDetailsObj.setItemNoForView(10.0d);
				 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
				 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
				 			if(null != hospObj)
				 			{
				 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
				 				
				 				billEntryDetailsObj.setNoOfDays(null);
				 				billEntryDetailsObj.setPerDayAmt(null);
				 				billEntryDetailsObj.setNoOfDaysAllowed(null);
				 				billEntryDetailsObj.setPerDayAmtProductBased(null);
				 				
				 				if(null != billEntryDetailsObj.getItemValue() )
				 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
				 				if(null != billEntryDetailsObj.getNonPayable() )
				 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
				 				if(null != billEntryDetailsObj.getProportionateDeduction() )
				 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
				 				if(null != billEntryDetailsObj.getReasonableDeduction())
				 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
				 				
				 				if(null != billEntryDetailsObj.getNonPayableProductBased())
				 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
				 				
				 				if(null != billEntryDetailsObj.getTotalDisallowances())
				 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
				 				
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 			else
				 			{
				 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
				 				billEntryListForReport.add(billEntryDetailsObj);

				 			}
				 			//if(isPackageAvailable)
				 			//{
					 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
					 			Double calculatedAmtIfPackIsAvailable = 0d;
					 			Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
					 			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
			 					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey(),reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
					 			
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
					 			{
					 				billEntryDetailsObj1.setItemName("Sub Total (Sl no 4,5, 6 & 7,8, 9 & 10)");
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
						 			
						 			Double productlimit = insuredSumInsured *(50f/100f);
						 			nonpackTotalProductPerDayAmtInFooter +=productlimit;
						 			billEntryDetailsObj1.setPerDayAmtProductBased(productlimit);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			
						 			/**
						 			 * E46 - totalAmountIfPackAvailable
						 			 * J46 - totalNonPayableAmt
						 			 * L46 - totalReasonableDeductionAmt
						 			 * G46 - productlimit
						 			 * H46 - allowableAmt
						 			 * */
						 			
						 			Double amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, totalNonPayableAmt); 
						 			Double amount2 = getDiffOfTwoNumber(amount1,totalReasonableDeductionAmt);
						 			
						 			Double allowableAmt = Math.min(amount2, productlimit);
							 		billEntryDetailsObj1.setAmountAllowableAmount(allowableAmt);
							 		
							 		Double amountForNonPayablePdtBased1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, allowableAmt);
							 		Double amountForNonPayablePdtBased2 = getDiffOfTwoNumber(totalNonPayableAmt,totalReasonableDeductionAmt);
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(amountForNonPayablePdtBased1, amountForNonPayablePdtBased2));
							 		billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
							 		
							 		//billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(billEntryDetailsObj1.getItemValue() , billEntryDetailsObj1.getAmountAllowableAmount()));
							 		billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
							 		Double proportionateDeduc = billEntryDetailsObj1.getProportionateDeduction();
					 				Double totalDisAllowances = billEntryDetailsObj1.getNonPayableProductBased() + billEntryDetailsObj1.getReasonableDeduction() + 
						 					billEntryDetailsObj1.getNonPayable() + proportionateDeduc;
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowances);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(totalAmountIfPackAvailable,totalNonPayableAmt );
						 			Double amount4 = 0d;
						 			amount4 = getDiffOfTwoNumber(amount3,totalReasonableDeductionAmt );
						 			billEntryDetailsObj1.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
						 			nonpackTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			nonpackTotalAllowableAmtInFooter += billEntryDetailsObj1.getAmountAllowableAmount();
						 			nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			nonpackTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable();
						 			nonpackTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 			nonpackTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
						 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			else
					 			{
					 				//billEntryDetailsObj1.setItemName("Sub Total (Sl no 3 to 10)");
					 				/**
					 				 * The above line is commented as per srikanth sir suggestion and below line is
					 				 * added.
					 				 * */
					 				billEntryDetailsObj1.setItemName("Sub Total For Package");
					 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
					 				
						 			billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
						 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
						 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
						 			billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
						 			Double amount1 = 0d;
						 			amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, nonPayableProductBasedIfPackageIsAvailable);
						 			Double amount2 = 0d;
						 			amount2 = getDiffOfTwoNumber(amount1, totalNonPayableAmt);
						 			Double amount3 = 0d;
						 			amount3 = getDiffOfTwoNumber(amount2 ,totalProportionateDeduction );
						 			calculatedAmtIfPackIsAvailable = getDiffOfTwoNumber(amount3, totalReasonableDeductionAmt);
						 			//billEntryDetailsObj1.setProportionateDeduction(null);
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
						 			
						 			if(null != billEntryDetailsObj1.getItemValue())
						 			packTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			if(null != billEntryDetailsObj1.getNonPayableProductBased())
						 			packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
						 			if(null != billEntryDetailsObj1.getNonPayable())
						 			packTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable(); 
						 			if(null != billEntryDetailsObj1.getProportionateDeduction())
						 			packTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
						 			if(null != billEntryDetailsObj1.getReasonableDeduction())
						 			packTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
						 		/*	if(null != billEntryDetailsObj1.getTotalDisallowances())
						 			packTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();*/
						 			if(null != billEntryDetailsObj1.getNetPayableAmount())
						 			packTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
					 			}
					 			
					 			
					 		//	billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmt, totalDisAllowances));
				 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
				 				{
				 					viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
					 				billEntryListForReport.add(billEntryDetailsObj1);
				 				}
					 			
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
					 			{
					 				BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 				billEntryDetailsObject.setItemName(SHAConstants.PACKAGE_RESTRICTIONS);
					 				Double productLimit = insuredSumInsured*(75f/100f);
					 				billEntryDetailsObject.setPerDayAmtProductBased(productLimit);
					 				/*if(null != billEntryDetailsObject.getAmountAllowableAmount())
					 				totalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
					 				if(null != billEntryDetailsObject.getNonPayableProductBased())
					 				totalNonPayablePdtBasedInFooter += billEntryDetailsObject.getNonPayableProductBased();
					 				if(null != billEntryDetailsObject.getNonPayable())
									totalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
					 				if(null != billEntryDetailsObject.getProportionateDeduction())
									totalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
					 				if(null != billEntryDetailsObject.getReasonableDeduction())
									totalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
					 				if(null != billEntryDetailsObject.getTotalDisallowances())
									totalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
					 				if(null != billEntryDetailsObject.getNetPayableAmount())
					 				totalNetPayableInFooter += billEntryDetailsObject.getNetPayableAmount();*/
					 				
					 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
						 			{
						 				/*Double amountPdtBasedNonPayable = getDiffOfTwoNumber( billEntryDetailsObj1.getNonPayableProductBased(),proportionateDeduc);
						 				Double amount6 = getDiffOfTwoNumber(amount1, amountPdtBasedNonPayable);
						 				Double amount7 = getDiffOfTwoNumber(amount6, billEntryDetailsObj1.getReasonableDeduction());*/
						 				billEntryDetailsObj1.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailable));
						 				billEntryListForReport.add(billEntryDetailsObj1);
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj1);
						 				Double amountallowable = Math.min(productLimit, billEntryDetailsObj1.getAmountAllowableAmount());
						 				billEntryDetailsObject.setAmountAllowableAmount(amountallowable);
						 				if(null != billEntryDetailsObject.getAmountAllowableAmount())
						 				{
							 				packTotalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				billEntryDetailsObject.setNonPayableProductBased(getDiffOfTwoNumber(amountallowable,billEntryDetailsObj1.getAmountAllowableAmount()));
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					packTotalNonPayablePdtBasedInFooter +=  billEntryDetailsObject.getNonPayableProductBased();
						 				}
						 				billEntryDetailsObject.setNonPayable(totalNonPayableAmt);
						 				if(null != billEntryDetailsObject.getNonPayable())
						 				{
						 					//packTotalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
						 				}
						 				billEntryDetailsObject.setProportionateDeduction(totalProportionateDeduction);
						 				if(null != billEntryDetailsObject.getProportionateDeduction())
						 				{
						 					//packTotalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
						 				}
						 				billEntryDetailsObject.setReasonableDeduction(totalReasonableDeductionAmt);
						 				if(null != billEntryDetailsObject.getReasonableDeduction())
						 				{
						 					//packTotalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
						 				}
						 				
						 				billEntryDetailsObject.setTotalDisallowances(totalNonPayableAmt + totalProportionateDeduction + totalReasonableDeductionAmt+billEntryDetailsObject.getNonPayableProductBased());
						 				
						 				if(null != billEntryDetailsObject.getTotalDisallowances())
						 				{
						 					/*
						 					 * Below code is commented for issue 168.
						 					 */
						 					packTotalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
						 				}
						 				billEntryDetailsObject.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
						 				if(null != billEntryDetailsObject.getNonPayableProductBased())
						 				{
						 					/*
						 					 * Below code is commented for issue 168.
						 					 */
						 					packTotalNetPayableInFooter += billEntryDetailsObject.getAmountAllowableAmount();
						 				}
						 				viewHospitalizationObj.addBeanToList(billEntryDetailsObject);
						 				billEntryListForReport.add(billEntryDetailsObject);

						 			}

					 				
					 			}
	
				 	}
				 }
				 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(11.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				//billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter +=  billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter += billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 				{
			 					nonpackTotalProductPerDayAmtInFooter += billEntryDetailsObj.getPerDayAmtProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getAmountAllowableAmount())
			 				{
			 					packTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 					nonpackTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayableProductBased())
			 				{
			 					packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 					nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
			 				}
			 				if(null != billEntryDetailsObj.getNonPayable())
			 				{
			 					packTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 					nonpackTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
			 				}
			 				if(null != billEntryDetailsObj.getProportionateDeduction())
			 				{
			 					packTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 					nonpackTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
//			 				BillEntryDetailsDTO billEntryObj1 = new BillEntryDetailsDTO();
//			 				billEntryObj1 = setDtoValuesToReportDTO(billEntryObj1, billEntryDetailsObj);
//				 			billEntryObj1.setItemName(SHAConstants.AMBULANCE_FEES);
//				 			billEntryObj1.setItemNoForView(11.0d);
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayableProductBased();
//			 				nonPaybleAmt += billEntryDetailsObj.getNonPayable();
//			 				billEntryObj1.setNonPayable(nonPaybleAmt);
//			 				
//			 				billEntryListForReport.add(billEntryObj1);
			 				
			 				billEntryListForReport.add(billEntryDetailsObj);
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(12.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					//packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
			 			billEntryDetailsObj.setItemNoForView(13.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getReasonableDeduction())
			 				{
			 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
			 				}
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					deductions = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setDeductionRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setDeductionRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
//			 				billEntryListForReport.add(billEntryDetailsObj);

			 			}
			 		}
				 }
				 else if((ReferenceTable.NETWORK_HOSPITAL_DISCOUNT).equals(billTypeKey))
				 {
			 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
			 		{
			 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
			 			billEntryDetailsObj.setItemNoForView(14.0d);
			 			billEntryDetailsObj.setItemName(SHAConstants.NETWORK_HOSPITAL_DISCOUNT);
			 			BillingHospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
			 			if(null != hospObj)
			 			{
			 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
			 				
			 				billEntryDetailsObj.setNoOfDays(null);
			 				billEntryDetailsObj.setPerDayAmt(null);
			 				billEntryDetailsObj.setNoOfDaysAllowed(null);
			 				billEntryDetailsObj.setPerDayAmtProductBased(null);
			 				
			 				if(null != billEntryDetailsObj.getItemValue())
			 				{
			 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
			 				}
			 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
			 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
			 				if(null != billEntryDetailsObj.getTotalDisallowances())
			 				{
			 					packTotalNetPayableInFooter += billEntryDetailsObj.getTotalDisallowances();
			 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
			 				}
			 				
			 				if(null != billEntryDetailsObj.getNetPayableAmount())
			 				{
			 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
			 					hospitalDiscount = billEntryDetailsObj.getNetPayableAmount();
			 				}
			 				
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 				this.bean.setNetworkHospitalDiscountRemarks(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 				this.bean.setNetworkHospitalDiscountRemarksForAssessmentSheet(billEntryDetailsObj.getDeductibleOrNonPayableReason());
			 			}
			 			else
			 			{
			 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
			 			}
			 		}
				 }
			 }
			 
			 if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
			 {
				 this.bean.setTotalClaimedAmt(packTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(packTotalDisallowancesInFooter);
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(packTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayablePdtBasedInFooter;
				 }
				 if(packTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += packTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 this.bean.setReasonableDeductionTotalAmt(packTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(packTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(packTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(packTotalItemValueInFooter ,
							0d , packTotalAllowableAmtInFooter ,
							packTotalNonPayablePdtBasedInFooter ,
							packTotalNonPayableManualInFooter ,
							packTotalProportionateDedInFooter ,
							packTotalReasonableDeductionInFooter ,
							packTotalDisallowancesInFooter ,
							packTotalNetPayableInFooter );	
			 }
			 else
			 {
				 this.bean.setTotalClaimedAmt(nonpackTotalItemValueInFooter);
				 this.bean.setToatlNonPayableAmt(nonpackTotalDisallowancesInFooter);
//				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 Double nonPayableTotalAmt = 0d;
				 if(nonpackTotalNonPayablePdtBasedInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayablePdtBasedInFooter;
				 }
				 if(nonpackTotalNonPayableManualInFooter != null){
					 nonPayableTotalAmt += nonpackTotalNonPayableManualInFooter;
				 }
				 
				 this.bean.setNonPayableTotalAmt(nonPayableTotalAmt);
				 
				 
				 
				 this.bean.setReasonableDeductionTotalAmt(nonpackTotalReasonableDeductionInFooter);
				 
				 this.bean.setProportionateDeductionTotalAmt(nonpackTotalProportionateDedInFooter);
				 
				 this.bean.setTotalApprovedAmt(nonpackTotalNetPayableInFooter);
				 
				 this.bean.setHospitalDiscount(hospitalDiscount);
				 this.bean.setDeductions(deductions);
				 
				 try{
					 if(deductions != null && deductions > 0){
						 bean.setDeductions(deductions * -1d);
					 }
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 
				 viewHospitalizationObj.calculateTotalForSeniorCitizen(nonpackTotalItemValueInFooter ,
							nonpackTotalProductPerDayAmtInFooter , nonpackTotalAllowableAmtInFooter ,
							nonpackTotalNonPayablePdtBasedInFooter ,
							nonpackTotalNonPayableManualInFooter ,
							nonpackTotalProportionateDedInFooter ,
							nonpackTotalReasonableDeductionInFooter ,
							nonpackTotalDisallowancesInFooter ,
							nonpackTotalNetPayableInFooter );	
			 }
			
		 }
		 if(null != this.bean)
		 {
			 bean.setBillEntryDetailsDTO(billEntryListForReport);
		 }
	}
	
	private Double getDiffOfTwoNumber(Double number1 , Double number2)
	{
		Double diff = 0d;
		if(number1 > number2)
		{
			diff = number1 - number2;
		}
		else
		{
			diff = number2 - number1;
		}
		return diff;
	}
	
	private BillEntryDetailsDTO populateHospitalizationDetails(BillEntryDetailsDTO billEntryDetailsDTO1 , BillingHospitalisation hospitalisation)
	{
		
		String strItemName = billEntryDetailsDTO1.getItemName();
		//if(SHAConstants.ROOM_RENT_NURSING_CHARGES)
 		billEntryDetailsDTO1.setNoOfDays(getValidDoubleFromString(String.valueOf(hospitalisation.getNoOfDays())));
 		billEntryDetailsDTO1.setPerDayAmt(getValidDoubleFromString(String.valueOf(hospitalisation.getPerDayAmount())));
		billEntryDetailsDTO1.setItemValue(getValidDoubleFromString(String.valueOf(hospitalisation.getClaimedAmount())));
		billEntryDetailsDTO1.setNoOfDaysAllowed(getValidDoubleFromString(String.valueOf(hospitalisation.getPolicyNoOfDays())));
		billEntryDetailsDTO1.setPerDayAmtProductBased(getValidDoubleFromString(String.valueOf(hospitalisation.getPolicyPerDayAmount())));
		billEntryDetailsDTO1.setAmountAllowableAmount(getValidDoubleFromString(String.valueOf(hospitalisation.getTotalAmount())));
		billEntryDetailsDTO1.setNonPayableProductBased(getValidDoubleFromString(String.valueOf(hospitalisation.getNonPayableAmountProduct())));
		billEntryDetailsDTO1.setNonPayable(getValidDoubleFromString(String.valueOf(hospitalisation.getNonPayableAmt())));
		billEntryDetailsDTO1.setProportionateDeduction(getValidDoubleFromString(String.valueOf(hospitalisation.getProrateDeductionAmount())));
		billEntryDetailsDTO1.setReasonableDeduction(getValidDoubleFromString(String.valueOf(hospitalisation.getDeductibleAmount())));
		billEntryDetailsDTO1.setTotalDisallowances(getValidDoubleFromString(String.valueOf(hospitalisation.getPayableAmount())));
		billEntryDetailsDTO1.setNetPayableAmount(getValidDoubleFromString(String.valueOf(hospitalisation.getNetAmount())));
		billEntryDetailsDTO1.setDeductibleOrNonPayableReason(hospitalisation.getReason());
		
		/**
		 * Added for scrc format change.
		 * 
		 * Approved amount for each entry to be displayed in bill assessment sheet. This is not
		 * applicable for view bill summary page. Hence net payable amount is duplicated in 
		 * approved amount column.
		 * */ 
		
		Double claimedAmt  = 0d;
		Double disAllowanceAmt = 0d;
		
		claimedAmt = getValidDoubleFromString(String.valueOf(hospitalisation.getClaimedAmount()));
		disAllowanceAmt = getValidDoubleFromString(String.valueOf(hospitalisation.getPayableAmount()));
		if(null != claimedAmt && null != disAllowanceAmt)
		{
			Double approvedAmt =  claimedAmt - disAllowanceAmt;
		
		billEntryDetailsDTO1.setApprovedAmountForAssessmentSheet(approvedAmt);
		}
		
		if(null != hospitalisation.getReimbursement())
		{
			billEntryDetailsDTO1.setReimbursementKey(hospitalisation.getReimbursement().getKey());
			ViewBillRemarks viewBillRemarksObj = billDetailsService.getViewBillRemarksForROD(hospitalisation.getReimbursement().getKey(), ReferenceTable.HOSPITALIZATION, hospitalisation.getBillTypeNumber());
			if(null != viewBillRemarksObj)
			{
				billEntryDetailsDTO1.setBillRemarksKey(viewBillRemarksObj.getBillKey());
				billEntryDetailsDTO1.setDeductibleNonPayableReasonBilling(viewBillRemarksObj.getBillingRemarks());
				billEntryDetailsDTO1.setDeductibleNonPayableReasonFA(viewBillRemarksObj.getFaRemarks());
				
			
					billEntryDetailsDTO1.setDeductibleNonPayableReasonFA(viewBillRemarksObj.getBillingRemarks());
				
				
				//else if(null != this.presenterString && SHAConstants.BILLING.equalsIgnoreCase(presenterString))
				{
					//com.shaic.ims.bpm.claim.modelv2.HumanTask rodHumanTask = bean.getRodHumanTask();
					Map<String, Object> wrkFlowMap = (Map<String, Object>)bean.getDbOutArray();

					if(null != wrkFlowMap)
					{
						String reimbReplyBy = (String)wrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
						if(null != reimbReplyBy)
						{	
							if(null != reimbReplyBy && SHAConstants.BILLING_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
							{
								billEntryDetailsDTO1.setDeductibleNonPayableReasonBilling(viewBillRemarksObj.getFaRemarks());
								billEntryDetailsDTO1.setPresenterString(SHAConstants.BILLING);
							}else if(null != reimbReplyBy && SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
							{
								billEntryDetailsDTO1.setPresenterString(SHAConstants.FINANCIAL);
							}
						}
						
					}
				}
			}
	
			//else if(null != this.presenterString && SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
			{
				billEntryDetailsDTO1.setDeductibleNonPayableReasonBilling(hospitalisation.getReason());
				billEntryDetailsDTO1.setDeductibleNonPayableReasonFA(billEntryDetailsDTO1.getDeductibleNonPayableReasonBilling());
			}
					
		}
		//billEntryDetailsDTO1.setMedicalRemarks(hospitalisation.get);
		
		return billEntryDetailsDTO1;
	}
	
	private Double getValidDoubleFromString(String value)
	{
		Double dValue = null;
		if(null != value && !("").equalsIgnoreCase(value))
		{
		 dValue = SHAUtils.getDoubleValueFromString(value);
		}
		return dValue;
	
	}
	
	private BillingHospitalisation getHospDetailsForBillType(List<BillingHospitalisation> hospList , Long billKey)
	{
		BillingHospitalisation hospitalizationObj = null;
		if(null != hospList && !hospList.isEmpty())
		{
			for (BillingHospitalisation hospitalisation : hospList) {
				if(billKey.equals(hospitalisation.getBillTypeNumber()))
				{
					hospitalizationObj = hospitalisation;
					break;
				}
				
			}
		}
		return hospitalizationObj;
		
	}
	
	private List<BillingHospitalisation> getListOfHospDetailsForBillType(List<BillingHospitalisation> hospList , Long billKey)
	{
		List<BillingHospitalisation> hospitalisationList = null;
		if(null != hospList && !hospList.isEmpty())
		{
			hospitalisationList = new ArrayList<BillingHospitalisation>();
			for (BillingHospitalisation hospitalisation : hospList) {
				if(billKey.equals(hospitalisation.getBillTypeNumber()))
				{
					hospitalisationList.add(hospitalisation);
				}
				
			}
		}
		return hospitalisationList;
	}
	
	
	private void loadMapForStarSeniorCitizen()
	{
//		if(null == seniorCitizenViewMap || seniorCitizenViewMap.isEmpty())
//		{
			seniorCitizenViewMap = new HashMap<Integer , Long>();
			seniorCitizenViewMap.put(1, ReferenceTable.ROOM_RENT_NURSING_CHARGES);
			seniorCitizenViewMap.put(2, ReferenceTable.ICU_CHARGES);
			if(isPackageAvailable)
			{
				seniorCitizenViewMap.put(3, ReferenceTable.PROFESSIONAL_CHARGES);
				seniorCitizenViewMap.put(4, ReferenceTable.OT_CHARGES);
				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
				{
					seniorCitizenViewMap.put(10,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
					seniorCitizenViewMap.put(11,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(12,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(13,ReferenceTable.OTHER_PACKAGES);
					//seniorCitizenViewMap.put(17,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(14,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(15,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(16,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(17,ReferenceTable.AMBULANCE_FEES);
					seniorCitizenViewMap.put(18,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(19,ReferenceTable.DEDUCTIONS);
					seniorCitizenViewMap.put(20,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
				}
				else
				{
					//seniorCitizenViewMap.put(10,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
					seniorCitizenViewMap.put(10,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(11,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(12,ReferenceTable.OTHER_PACKAGES);
					//seniorCitizenViewMap.put(17,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(13,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(14,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(15,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(16,ReferenceTable.AMBULANCE_FEES);
					seniorCitizenViewMap.put(17,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(18,ReferenceTable.DEDUCTIONS);
					seniorCitizenViewMap.put(19,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
				}
				

			}
			else
			{
				if(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey))
				{
					seniorCitizenViewMap.put(4, ReferenceTable.OT_CHARGES);
					seniorCitizenViewMap.put(3, ReferenceTable.PROFESSIONAL_CHARGES);
					seniorCitizenViewMap.put(10,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(11,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(12,ReferenceTable.OTHER_PACKAGES);
					seniorCitizenViewMap.put(13,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(18,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(14,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(15,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(16,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(17,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(18,ReferenceTable.DEDUCTIONS);
					seniorCitizenViewMap.put(19,ReferenceTable.MICRO_INS_IND_OT_TILL_DEDUCTIONS);
					seniorCitizenViewMap.put(20,ReferenceTable.AMBULANCE_FEES);
					seniorCitizenViewMap.put(21,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
					
				}
				else
				{
					seniorCitizenViewMap.put(3, ReferenceTable.OT_CHARGES);
					seniorCitizenViewMap.put(4, ReferenceTable.PROFESSIONAL_CHARGES);
					seniorCitizenViewMap.put(10,ReferenceTable.AMBULANCE_FEES);
					//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
					seniorCitizenViewMap.put(11,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(12,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(13,ReferenceTable.OTHER_PACKAGES);
					seniorCitizenViewMap.put(14,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(18,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(15,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(16,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(17,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(18,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(19,ReferenceTable.DEDUCTIONS);	
					seniorCitizenViewMap.put(20,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
				}
			}
			//seniorCitizenViewMap.put(5,ReferenceTable.INVESTIGATION_DIAG);
			seniorCitizenViewMap.put(5,ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			seniorCitizenViewMap.put(6,ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			//seniorCitizenViewMap.put(8,ReferenceTable.MEDICINES_CONSUMABLES);
			seniorCitizenViewMap.put(7,ReferenceTable.MEDICINES);
			seniorCitizenViewMap.put(8,ReferenceTable.CONSUMABLES);
			seniorCitizenViewMap.put(9,ReferenceTable.IMPLANT_STUNT_VALVE);
			
//		}
	}
	
//	private void setHospitalizationValues(Long rodKey){
//	      List<HospitalisationDTO> hospitalDetails = new ArrayList<HospitalisationDTO>();
//	       
////	      rodKey = 5068l;
//	      
//	      Integer benefitId = 0;
//	      
//	      List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
//	      
//	      List<BillingHospitalisation> hospitalizationList = billDetailsService.getBillingHospitalisationList(rodKey);
//	      Double sumInsured = null;
//	      if(this.insuredId != null){
//	      sumInsured = dbCalculationService.getInsuredSumInsured(this.insuredId.toString(), this.policyKey);
//	      }
//	      Hospitals hospitalById = hospitalService.getHospitalById(intimation.getHospital());
//	      Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
//	      if(sumInsured != null){
//	    	  valuesMap = dbCalculationService.getHospitalizationDetails(this.productId, sumInsured,hospitalById.getCityClass().toString(), this.insuredId, this.bean.getNewIntimationDTO().getKey(),0l,"0");
//	      }
//	      
//	      for(int i=0; i<this.masterValues.size();i++){
//	    	  HospitalisationDTO hospDto = new HospitalisationDTO();
//	    	  hospDto.setDetails(this.masterValues.get(i));
//	    	  hospDto.setSerialNum(this.serialMap.get(i));
//	    	  hospitalDetails.add(hospDto);
//	      }
//	      
//	      for (BillingHospitalisation hospitalisation : hospitalizationList) {                 //Load all value to corresponding Master Values
//	    	   Long sequenceNumber =0l;
//	    	   for (MasBillDetailsType masBillDetailsType : billDetailsType) {
//	   			             if(hospitalisation.getBillTypeNumber() != null && hospitalisation.getBillTypeNumber().equals(masBillDetailsType.getKey())){
//	   			            	 sequenceNumber = masBillDetailsType.getSequenceNumber();
//	   			            	 benefitId = this.sequenceMap.get(sequenceNumber);
//	   			            	 break;
//	   			             }
//	   		       }
//	    	   
//	    	      HospitalisationDTO hospitalisationDTO = hospitalDetails.get(benefitId);
//	    	   
//	    	       if(benefitId.equals(0)){
//	    	    	   hospitalDetails.get(benefitId).setEntitlementPerDayAmt(((Double)valuesMap.get(8)).longValue());
//	    	    	   if(hospitalisation.getNoOfDays() != null){
//	    	    	   Long payableAmount = (((Double)valuesMap.get(8)).longValue()) * hospitalisation.getNoOfDays();
//	    	    	   hospitalisationDTO.setAmount(payableAmount);
//	    	    	   }
//	    	    	   
//	    	       }
//	    	       else if(benefitId.equals(1)){
//	    	    	   hospitalDetails.get(benefitId).setEntitlementPerDayAmt(((Double)valuesMap.get(9)).longValue());
//	    	    	   if(hospitalisation.getNoOfDays() != null){
//	    	    		   if((((Double)valuesMap.get(9)).longValue())!= 0){
//		    	    	   Long payableAmount = (((Double)valuesMap.get(9)).longValue()) * hospitalisation.getNoOfDays();
//		    	    	   hospitalisationDTO.setAmount(payableAmount);
//	    	    		   }
//	    	    		   else{
//	    	    			   Long payableAmount = hospitalisation.getNoOfDays() * hospitalisation.getClaimedAmount();
//	    	    			   hospitalisationDTO.setEntitlementPerDayAmt(hospitalisation.getClaimedAmount());
//	    	    			   hospitalisationDTO.setAmount(payableAmount);
//	    	    		   }
//		    	    	  }
//	    	       }
//	    	       else if(benefitId.equals(9)){
//	    	    	   hospitalDetails.get(benefitId).setAmount(((Double)valuesMap.get(15)).longValue());
//	    	       }
//	    	       else{
//	    	    	   hospitalisationDTO.setPayableAmount(hospitalisation.getClaimedAmount()); 
//	    	       }
//	    	       if(hospitalisation.getNoOfDays() != null && hospitalisation.getNoOfDays() != 0){
//	    	       hospitalisationDTO.setNoOfDays(hospitalisation.getNoOfDays());
//	    	       }
//	    	       hospitalisationDTO.setPerDayAmt(hospitalisation.getPerDayAmount());
//	    	       hospitalisationDTO.setClaimedAmount(hospitalisation.getClaimedAmount());
//	    	       hospitalisationDTO.setBillingNonPayableAmt(0l);
//	    	       hospitalisationDTO.setNetAmount(hospitalisation.getClaimedAmount());
//	    	       hospitalisationDTO.setEntitlementNoOfDays(hospitalisation.getNoOfDays());
//	    	       if(benefitId.equals(0) || benefitId.equals(1)){
//	    	       if(hospitalisationDTO.getAmount() != null && hospitalisationDTO.getClaimedAmount() != null){
//	    	    	     hospitalisationDTO.setPayableAmount(Math.min(hospitalisationDTO.getAmount(), hospitalisationDTO.getClaimedAmount()));
//	    	       }
//	    	       
//	    	       }
//	    	       else if(benefitId.equals(9)){
//	    	    	   hospitalisationDTO.setPayableAmount(Math.min(hospitalisationDTO.getAmount(), hospitalisationDTO.getClaimedAmount()));
//	    	       }
//	    	       else{
//	    	    	   hospitalisationDTO.setPayableAmount(hospitalisation.getClaimedAmount());
//	    	       }
//	    	       hospitalisationDTO.setDeductionNonPayableAmount(hospitalisation.getDeductibleAmount());
//	    	                     //need to implements
//	    	      
//			}
//	      
//	     for (HospitalisationDTO hospitalisationDTO : hospitalDetails) {
//	    	
//			hospitalizationObj.addBeanToList(hospitalisationDTO);
//			
//		}
//		
//	}
	
public void setBillDetailsTable(Long rodKey){
	
//    rodKey = 5029l;
	
   /* Double sumInsured = null;
    if(this.insuredId != null){
    sumInsured = dbCalculationService.getInsuredSumInsured(this.insuredId.toString(), this.policyKey);
    }*/
    //Hospitals hospitalById = hospitalService.getHospitalById(intimation.getHospital());
   /* Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
    if(sumInsured != null){
  	  valuesMap = dbCalculationService.getHospitalizationDetails(this.productId, sumInsured,hospitalById.getCityClass().toString(), this.insuredId, this.bean.getNewIntimationDTO().getKey(),0l,"0");
    }*/
	
	List<RODDocumentSummary> rodDocumentSummaryList = billDetailsService.getBillDetailsByRodKey(rodKey);
	int sno=1;
	for (RODDocumentSummary rodDocumentSummary : rodDocumentSummaryList) {
		Long documentSummaryKey = rodDocumentSummary.getKey();
		
		List<RODBillDetails> rodBillDetails = billDetailsService.getBilldetailsByDocumentSummayKey(documentSummaryKey);
	
		if(rodBillDetails != null){
			List<HospitalisationDTO> hospitalisationList = new ArrayList<HospitalisationDTO>();
			int i=1;
			for (RODBillDetails rodBillDetails2 : rodBillDetails) {
				HospitalisationDTO dto = new HospitalisationDTO();
				if(i==1){
					dto.setSno(sno);
					dto.setRodNumber(rodDocumentSummary.getReimbursement().getRodNumber());
					if(null != rodDocumentSummary.getFileType()){
					dto.setFileType(rodDocumentSummary.getFileType().getValue());
					}
					dto.setFileName(rodDocumentSummary.getFileName());
					dto.setBillNumber(rodDocumentSummary.getBillNumber());
					if(null != rodDocumentSummary.getBillDate()){
						
					String formatDate = SHAUtils.formatDate(rodDocumentSummary.getBillDate());
					dto.setBillDate(formatDate);
					}
					dto.setNoOfItems(rodDocumentSummary.getNoOfItems());
					dto.setBillValue(rodDocumentSummary.getBillAmount());
					sno++;
				}
				dto.setItemNo(i);
				dto.setItemName(rodBillDetails2.getItemName());
				if(null != rodBillDetails2.getBillClassification()){
				dto.setClassification(rodBillDetails2.getBillClassification().getValue());
				}
				if(null != rodBillDetails2.getBillCategory()){
				dto.setCategory(rodBillDetails2.getBillCategory().getValue());
				}
				dto.setNoOfDays(rodBillDetails2.getNoOfDaysBills().longValue());
				if(null != rodBillDetails2.getPerDayAmountBills()){
				dto.setPerDayAmt(rodBillDetails2.getPerDayAmountBills().longValue());
				}
				if(null != rodBillDetails2.getClaimedAmountBills()){
				dto.setClaimedAmount(rodBillDetails2.getClaimedAmountBills().longValue());
				}
				if(null != rodBillDetails2.getNoOfDaysPolicy()){
				dto.setEntitlementNoOfDays(rodBillDetails2.getNoOfDaysPolicy().longValue());
				}
				if(null != rodBillDetails2.getPerDayAmountPolicy()){
					dto.setEntitlementPerDayAmt(rodBillDetails2.getPerDayAmountPolicy().longValue());
				}
				if(null != rodBillDetails2.getTotalAmount()){
					dto.setAmount(rodBillDetails2.getTotalAmount().longValue());
				}
				if(null != rodBillDetails2.getDeductibleAmount()){
					dto.setDeductionNonPayableAmount(rodBillDetails2.getPayableAmount().longValue());
				}
				if(null != rodBillDetails2.getClaimedAmountBills() && null != rodBillDetails2.getPayableAmount()){
					Double claimedAmount = rodBillDetails2.getClaimedAmountBills();
					Double disAllowance = rodBillDetails2.getPayableAmount();
					Double netAmount = claimedAmount - disAllowance;
					dto.setPayableAmount(netAmount.longValue());
				}else{
					if(rodBillDetails2.getNetAmount() != null){
						dto.setPayableAmount(rodBillDetails2.getNetAmount().longValue());
					}
				}
				dto.setReason(rodBillDetails2.getNonPayableReason());
				dto.setMedicalRemarks(rodBillDetails2.getMedicalRemarks());
				if(null != rodBillDetails2.getIrdaLevel1Id()){
					
					SelectValue irdaLevel1ValueByKey = masterService
							.getIRDALevel1ValueByKey(rodBillDetails2.getIrdaLevel1Id());
					if(irdaLevel1ValueByKey != null){
						dto.setIrdaLevel1(irdaLevel1ValueByKey.getValue());
					}
				}
				if(null != rodBillDetails2.getIrdaLevel2Id()){
					SelectValue irdaLevel2ValueByKey = masterService
							.getIRDALevel2ValueByKey(rodBillDetails2.getIrdaLevel2Id());
					if(irdaLevel2ValueByKey != null){
						dto.setIrdaLevel2(irdaLevel2ValueByKey.getValue());
					}
				}
				if(null != rodBillDetails2.getIrdaLevel3Id()){
					SelectValue irdaLevel3ValueByKey = masterService
							.getIRDALevel3ValueByKey(rodBillDetails2.getIrdaLevel3Id());
					if(irdaLevel3ValueByKey != null){
						dto.setIrdaLevel3(irdaLevel3ValueByKey.getValue());
					}
				}
				
				if(rodBillDetails2.getBillClassification() != null && rodBillDetails2.getBillClassification().getKey()==8l){
					
//				dto.setEntitlementPerDayAmt(((Double)valuesMap.get(8)).longValue());
//	    	    	   if(rodBillDetails2.getNoOfDaysBills() != null){
//	    	    	   Long payableAmount = (((Double)valuesMap.get(8)).longValue()) * rodBillDetails2.getNoOfDaysBills();
//	    	    	   dto.setAmount(payableAmount);
//	    	    	  }
//	    	    	   if(rodBillDetails2.getClaimedAmountBills() != null){
//	    	    	   dto.setNetAmount(rodBillDetails2.getClaimedAmountBills().longValue());
//	    	    	   }
//		    	       dto.setEntitlementNoOfDays(rodBillDetails2.getNoOfDaysBills());
//		    	       
//		    	       if(dto.getAmount() != null && dto.getClaimedAmount() != null){
//		    	    	     dto.setPayableAmount(Math.min(dto.getAmount(), dto.getClaimedAmount()));
//		    	       }
					
				}
//				else {
//					if(rodBillDetails2.getClaimedAmountBills() != null){
//					dto.setPayableAmount(rodBillDetails2.getClaimedAmountBills().longValue());
//					}
//					
//				}
				hospitalisationList.add(dto);
				i++;
			}
			
            for (HospitalisationDTO hospitalisationDTO : hospitalisationList) {
            	
				viewBillDetailsTableObj.addBeanToList(hospitalisationDTO);
				
			}
		}
	}
			
	}
	
	private void loadMasterValues(){
		//To display the records in the view.
		this.masterValues.put(0, "Room Rent, Boarding & Nursing Charges");
		this.masterValues.put(1, "	a) Room Rent");
		this.masterValues.put(2, "  b) Nursing Charges");
		this.masterValues.put(1, "ICU Charges");
		this.masterValues.put(2, "Nursing charges");
		this.masterValues.put(3, "OT Charges");
		this.masterValues.put(4, "Professional Fees (Surgeon, Anastheist, Consultation charges etc)");
		this.masterValues.put(5, "Investigation & Diagnostics");
		this.masterValues.put(6, "Medicines & Consumables");
		this.masterValues.put(7, "           a) Medicines & Consumables");
		this.masterValues.put(8, "           b) Implant/Stunt/Valve/Pacemaker/Etc");
		this.masterValues.put(9, "Ambulance Fees");
		this.masterValues.put(10, "Package Charges");
		this.masterValues.put(11, "           a) ANH Package");
		this.masterValues.put(12, "           b) Composite Package");
		this.masterValues.put(13, "           c) Other Package");
		this.masterValues.put(14, "Others");
		
		//To display the serial number in the table
		this.serialMap.put(0, "1");
		this.serialMap.put(1, "2");
		this.serialMap.put(2, "3");
		this.serialMap.put(3, "4");
		this.serialMap.put(4, "5");
		this.serialMap.put(5, "6");
		this.serialMap.put(6, "7");
		this.serialMap.put(7, "");
		this.serialMap.put(8, "");
		this.serialMap.put(9, "8");
		this.serialMap.put(10,"9");
		this.serialMap.put(11,"");
		this.serialMap.put(12,"");
		this.serialMap.put(13,"");
		this.serialMap.put(14,"10");

		//key = sequence number from database,   value = to identify the particular row from that table
		this.sequenceMap.put(1l, 0);
		this.sequenceMap.put(2l, 1);
		this.sequenceMap.put(3l, 2);
		this.sequenceMap.put(4l, 3);
		this.sequenceMap.put(5l, 4);
		this.sequenceMap.put(6l, 5);
		this.sequenceMap.put(7l, 7);       //7th row is return only name..it has not any value
		this.sequenceMap.put(8l, 8);
		this.sequenceMap.put(9l, 9);
		this.sequenceMap.put(10l, 11);
		this.sequenceMap.put(11l, 12);
		this.sequenceMap.put(12l, 13);
		this.sequenceMap.put(13l, 14);
		
	}
	
	public Map<String, String> getPayableAmount() {
		Map<String, String> valueMap = new HashMap<String, String>();
		if(viewHospitalizationObj != null) {
			valueMap.put(SHAConstants.HOSPITALIZATION, viewHospitalizationObj.getPayableAmount() != null ? viewHospitalizationObj.getPayableAmount() : "0");
		} else if(hospitalizationObj != null) {
			valueMap.put(SHAConstants.HOSPITALIZATION, hospitalizationObj.getPayableAmount() != null ? hospitalizationObj.getPayableAmount() : "0");
		}
		
		valueMap.put(SHAConstants.PREHOSPITALIZATION, preHospitalizationTable.getPayableAmount());
		valueMap.put(SHAConstants.POSTHOSPITALIZATION, postHospitalizationObj.getPayableAmount());
		valueMap.put(SHAConstants.PRORATA_DEDUCTION_FLAG, proportionalDeductionFlg);
		valueMap.put(SHAConstants.PRORATA_PERCENTAGE, txtDeductionPercentage.getValue() != null ? txtDeductionPercentage.getValue() : "0");
		valueMap.put(SHAConstants.PACKAGE_AVAILABLE_FLAG, packageFlg);
		return valueMap;
	}
	
	private PreauthDTO getProrataFlagFromProduct(Reimbursement reimbursement,PreauthDTO preauthDTO)
	{
		Product product = masterService.getProrataForProduct(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey());
		if(null != product)
		{
			preauthDTO.setProrataDeductionFlag(null != product.getProrataFlag() ? product.getProrataFlag() : "N");
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			preauthDTO.setProductBasedProRata(null != product.getProrataFlag() ? product.getProrataFlag() : "N");
			preauthDTO.setProductBasedPackage(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : "N");
			//ends.
			preauthDTO.setPackageAvailableFlag(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : "N");
			
			//added for CR GLX2020069 GMC prorata calculation
			if(product.getCode() != null && (product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_PRODUCT_CODE)|| product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE))){
				MasRoomRentLimit gmcProrataFlag = intimationService.getMasRoomRentLimit(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
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
	
	private Panel exportPdfFile(){
		
		if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null) {
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(SHAUtils.getParsedAmount(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()));
		}

		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);
		if(!ReferenceTable.SENIOR_CITIZEN_RED_CARPET.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			calculateTotalForReport();	
		}
		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
			
		VerticalLayout firstVertical = getContentofTemplate("BillSummaryOtherProducts",reportDto,docGen);
		firstVertical.setWidth("100%");
		firstVertical.setHeight("100%");
		
		Panel panel = new Panel(firstVertical);
		panel.setSizeFull();

		return panel;
	}
	
private VerticalLayout getContentofTemplate(String templateName,ReportDto reportDto,DocumentGenerator docGen) {
		
		
		String filePath = docGen.generatePdfDocument(templateName, reportDto);	
		final String finalFilePath = filePath;

		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		VerticalLayout templateLayout = new VerticalLayout(e);
		templateLayout.setHeight("450px");
		return templateLayout;
	}
	
public void calculateTotalForReport(){
		
//		List<BillEntryDetailsDTO> itemIconPropertyId = this.bean.getBillEntryDetailsDTO();
//		//Long netAmount =0l;
//		Double claimedAmount = 0d;
//		Double allowableAmount = 0d;
//		Double nonPayablePdtBased = 0d;
//		Double nonPayableAmount = 0d;
//		Double proportionateDeduction = 0d;
//		Double totalDisallowances = 0d;
//		Double reasonableDeduction = 0d;
//		Double netAmount = 0d;
//		/*Long amount =0l;
//		Long nonPayableAmount =0l;
//		Long payableAmount =0l;*/
//		
//		if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
//			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
//			
//			if(null != billEntryDetailsDTO.getItemValue())
//			{
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
//						{
//							claimedAmount += billEntryDetailsDTO.getItemValue();
//							if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
//							{
//								claimedAmount -= billEntryDetailsDTO.getItemValue();
//							}
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getAmountAllowableAmount())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
//						{
//							allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getNonPayableProductBased())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						)))
//						{
//							nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getNonPayable())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						)))
//						{
//							nonPayableAmount += billEntryDetailsDTO.getNonPayable();
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getProportionateDeduction())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						)))
//						{
//							proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getReasonableDeduction())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						)))
//						{
//							reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
//						}
//			}
//			
//			if(null != billEntryDetailsDTO.getTotalDisallowances())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						)))*/
//				if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//						))
//						{
//							totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
//						}
//			}
//			if(null != billEntryDetailsDTO.getNetPayableAmount())
//			{
//				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
//						{
//							netAmount += billEntryDetailsDTO.getNetPayableAmount();
//						}
//			}
//			
//		  }
//		}
//		this.bean.setTotalClaimedAmt(claimedAmount);
//		this.bean.setToatlNonPayableAmt(reasonableDeduction + nonPayableAmount + proportionateDeduction);
//		this.bean.setTotalApprovedAmt(netAmount);
	
	
	List<BillEntryDetailsDTO> itemIconPropertyId = this.bean.getBillEntryDetailsDTO();
	//Long netAmount =0l;
	Double claimedAmount = 0d;
	Double allowableAmount = 0d;
	Double nonPayablePdtBased = 0d;
	Double nonPayableAmount = 0d;
	Double proportionateDeduction = 0d;
	Double totalDisallowances = 0d;
	Double reasonableDeduction = 0d;
	Double netAmount = 0d;
	/*Long amount =0l;
	Long nonPayableAmount =0l;
	Long payableAmount =0l;*/
	
	if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
		for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
		
		if(null != billEntryDetailsDTO.getItemValue())
		{
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
					{
						claimedAmount += billEntryDetailsDTO.getItemValue();
						if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
						{
							claimedAmount -= billEntryDetailsDTO.getItemValue();
						}
					}
		}
		
		if(null != billEntryDetailsDTO.getAmountAllowableAmount())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
					{
						allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
					}
		}
		
		if(null != billEntryDetailsDTO.getNonPayableProductBased())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					)))
					{
						nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
					}
		}
		
		if(null != billEntryDetailsDTO.getNonPayable())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					)))
					{
						nonPayableAmount += billEntryDetailsDTO.getNonPayable();
					}
		}
		
		if(null != billEntryDetailsDTO.getProportionateDeduction())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					)))
					{
						proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
					}
		}
		
		if(null != billEntryDetailsDTO.getReasonableDeduction())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					)))
					{
						reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
					}
		}
		
		if(null != billEntryDetailsDTO.getTotalDisallowances())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					)))*/
			if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
					))
					{
						totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
					}
		}
		if(null != billEntryDetailsDTO.getNetPayableAmount())
		{
			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
					{
						netAmount += billEntryDetailsDTO.getNetPayableAmount();
					}
		}
		
	  }
	}
	 this.bean.setTotalClaimedAmt(claimedAmount);
	 this.bean.setToatlNonPayableAmt(reasonableDeduction + nonPayableAmount + proportionateDeduction);
	 this.bean.setTotalApprovedAmt(netAmount);
	 this.bean.setAmountTotal(netAmount);
	 this.bean.setNonpayableProdTotal(nonPayablePdtBased);
	 this.bean.setNonpayableTotal(nonPayableAmount);
	 this.bean.setPropDecutTotal(proportionateDeduction);
	 this.bean.setReasonableDeducTotal(reasonableDeduction);
	 this.bean.setDisallowanceTotal(totalDisallowances);
	 this.bean.setNetPayableAmtTotal(netAmount);
	
		/*table.setColumnFooter("itemValue", String.valueOf(claimedAmount));
		table.setColumnFooter("amountAllowableAmount" , String.valueOf(allowableAmount));
		table.setColumnFooter("nonPayableProductBased" , String.valueOf(nonPayablePdtBased));
		table.setColumnFooter("nonPayable" , String.valueOf(nonPayableAmount));
		table.setColumnFooter("proportionateDeduction"  , String.valueOf(proportionateDeduction));
		table.setColumnFooter("reasonableDeduction" , String.valueOf(reasonableDeduction));
		table.setColumnFooter("totalDisallowances"  , String.valueOf(totalDisallowances));
		table.setColumnFooter("netPayableAmount"  , String.valueOf(netAmount));*/

		/*table.setColumnFooter("amount", String.valueOf(amount));
		table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
		table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
		//table.setColumnFooter("itemName", "Total");
		
}

public BillEntryDetailsDTO setDtoValuesToReportDTO(BillEntryDetailsDTO reportDTO, BillEntryDetailsDTO mainDto){
	
	reportDTO.setNoOfDays(mainDto.getNoOfDays());
	reportDTO.setPerDayAmt(mainDto.getPerDayAmt());
	reportDTO.setItemValue(mainDto.getItemValue());
	reportDTO.setNoOfDaysAllowed(mainDto.getNoOfDaysAllowed());
	reportDTO.setPerDayAmtProductBased(mainDto.getPerDayAmtProductBased());
	reportDTO.setAmountAllowableAmount(mainDto.getAmountAllowableAmount());
	reportDTO.setNonPayableProductBased(mainDto.getNonPayableProductBased());
	reportDTO.setNonPayable(mainDto.getNonPayable());
	reportDTO.setProportionateDeduction(mainDto.getProportionateDeduction());
	reportDTO.setReasonableDeduction(mainDto.getReasonableDeduction());
	reportDTO.setTotalDisallowances(mainDto.getTotalDisallowances());
	reportDTO.setNetPayableAmount(mainDto.getNetPayableAmount());
	reportDTO.setDeductibleNonPayableReasonBilling(mainDto.getDeductibleNonPayableReasonBilling());
	reportDTO.setDeductibleNonPayableReasonFA(mainDto.getDeductibleNonPayableReasonBilling());
	reportDTO.setDeductibleOrNonPayableReason(mainDto.getDeductibleNonPayableReasonBilling());
	
	return reportDTO;
	
}

}

