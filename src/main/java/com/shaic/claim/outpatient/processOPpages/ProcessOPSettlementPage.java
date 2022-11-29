package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessOPSettlementPage extends ViewComponent{

	private static final long serialVersionUID = 2069575197802626263L;

	private OutPatientDTO bean;
	private GWizard wizard;

	private VerticalLayout opRegisterPageLayout;
	private VerticalLayout commentsHolderlayout;
	
	@EJB
	private InsuredService insuredService;

	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService calcService;

	// Start
	@Inject
	private Instance<OPSettlementDetailsTable> opSettleDetailsTableInstance;

	private OPSettlementDetailsTable opSettleDetailsTableObj;
	
	private TextArea rejectRemarks; 
	
	private TextArea approvalRemarks;
	private TextField approvalAmt;
	private OptionGroup optPaymentMode;
	
	private HorizontalLayout paymentTypelayout;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	
	private TextField chqmodeChngReason;
	private TextField chqEmailId;
	private TextField chqPanno;
	private TextField chqPayableAt;
	private ComboBox chqPayeeName;
	private TextField chqNameChngReason;
	private TextField chqHeirName;
	
	private TextField bnkmodeChngReason;
	private TextField bnkEmailId;
	private TextField bnkPanno;
	private TextField bnkAccNo;
	private TextField bnkIfsc;
	private TextField bnkName;
	private TextField bnkCity;
	private ComboBox bnkPayeeName;
	private TextField bnkNameChngReason;
	private TextField bnkHeirName;
	private TextField bnkBranch;
	private Button btnIFCSSearch;
	
	 @Inject
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Window populatePreviousWindowPopup;
	 
	 private VerticalLayout previousPaymentVerticalLayout;
	 
	 private HorizontalLayout previousAccntDetailsButtonLayout;
	 
	 private Button btnOk;
	 private Button btnCancel;
	 
	 private TextField amountEligible;
	 private TextField availableOPSI;
	 private TextField amountPayable;
	 
	@Inject
	private OpUploadedDocumentDetails  opUploadedDetails;
			
	private List<UploadDocumentDTO> listOfdocDetailsObj;
	
	private StringBuilder errMsg = new StringBuilder();
	
	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}
	
	//End

	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		opRegisterPageLayout = new VerticalLayout();
		commentsHolderlayout = new VerticalLayout();
		commentsHolderlayout.setWidth("100%");
	}

	public Component getContent(){
		wizard.getNextButton().setEnabled(true);
		
		opRegisterPageLayout.removeAllComponents();
		
		opSettleDetailsTableObj = opSettleDetailsTableInstance.get();
		opSettleDetailsTableObj.init(new OPBillDetailsDTO());
		opSettleDetailsTableObj.setVisibleColumns();
		if(bean.getUploadedDocsTableList() != null){
			for(com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO rec : bean.getOpBillDetailsList()){
				opSettleDetailsTableObj.addBeanToList(rec);
			}
		}

		listOfdocDetailsObj =  bean.getUploadedDocsTableList();
		opRegisterPageLayout.addComponent(UploadButtonLayout());
		opRegisterPageLayout.addComponent(opSettleDetailsTableObj);
		
		Integer opAvailableAmount = 0;
		Map<String, Integer> claimAmt = calcService.getOPAvailableAmount(bean.getDocumentDetails().getInsuredPatientName().getKey(),bean.getClaimDTO().getKey(), bean.getClaimDTO().getClaimType().getId(),
				bean.getDocumentDetails().getConsultationType().getCommonValue() != null ? bean.getDocumentDetails().getConsultationType().getCommonValue():"0");
		if(claimAmt != null && !claimAmt.isEmpty()){
			opAvailableAmount = claimAmt.get(SHAConstants.CURRENT_BALANCE_SI);
		}
		bean.setReportOPAvailableSI(opAvailableAmount);
		
		Integer amt = new Integer(0);
		Integer consBillAmount = new Integer(0);
		Integer consDeductAmount = new Integer(0);
		for (OPBillDetailsDTO billDetailsDTO : bean.getOpBillDetailsList()) {
			consBillAmount += billDetailsDTO.getBillAmount().intValue();
			consDeductAmount += billDetailsDTO.getDeductibleAmount().intValue();
			amt +=  SHAUtils.getIntegerFromString(String.valueOf(billDetailsDTO.getBillAmount().intValue()))  -  SHAUtils.getIntegerFromString(String.valueOf(billDetailsDTO.getDeductibleAmount().intValue())) ;
		}
		bean.setAmountEligible(amt);
		bean.setReportOPAmountClaimed(amt);
		bean.setReportConsolidatedBillAmount(consBillAmount);
		bean.setReportConsolidatedDedAmount(consDeductAmount);

		opRegisterPageLayout.addComponent(buttonLayout());		
		opRegisterPageLayout.addComponent(commentsHolderlayout);


		return opRegisterPageLayout;
	}
	
	
	public HorizontalLayout UploadButtonLayout(){
		Button uploadDocButton = new Button("Uploaded Documents");
		
		HorizontalLayout uploadButlayout = new HorizontalLayout(uploadDocButton);
		uploadButlayout.setWidth("15%");
		
		HorizontalLayout holderUpllayout = new HorizontalLayout(uploadButlayout);
		holderUpllayout.setWidth("100%");
		holderUpllayout.setHeight("70px");
		holderUpllayout.setComponentAlignment(uploadButlayout, Alignment.MIDDLE_RIGHT);
		
		uploadDocButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showUploadedDocuments();
			}
		});
		return holderUpllayout;
	}
	
	
	public HorizontalLayout buttonLayout(){
		Button approveButton = new Button("Approve");
		Button rejectButton = new Button("Reject");
//		FormLayout holder = new FormLayout(approveButton, rejectButton);
		HorizontalLayout approvallayout = new HorizontalLayout(approveButton, rejectButton);
		approvallayout.setSpacing(false);
		approvallayout.setWidth("15%");
		
		HorizontalLayout holderlayout = new HorizontalLayout(approvallayout);
		holderlayout.setWidth("100%");
		holderlayout.setComponentAlignment(approvallayout, Alignment.BOTTOM_RIGHT);
		
		approveButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				updateApproveFlag();
				commentsHolderlayout.removeAllComponents();
				commentsHolderlayout.addComponent(buildApproveLayout());
			}
		});
		rejectButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				updateRejectFlag();
				commentsHolderlayout.removeAllComponents();
				commentsHolderlayout.addComponent(buildRejectLayout());
			}
		});
		
		return holderlayout;
	}
	
	private void updateApproveFlag(){
		bean.setApprove(true);
		bean.setReject(false);
	}
	
	private void updateRejectFlag(){
		bean.setApprove(false);
		bean.setReject(true);
	}
	
	private void updatePaymentFlag(boolean isChq, boolean isBank){
		if(isChq){
			bean.setChqPayment(true);
		}else{
			bean.setChqPayment(false);
		}
	}
	
	public HorizontalLayout buildApproveLayout(){
		paymentTypelayout = new HorizontalLayout();

		amountEligible = new TextField("Amount Eligible");
		if(bean.getAmountEligible() > 0){
			amountEligible.setValue(bean.getAmountEligible().toString());
		}/*else{
			amountEligible.setValue("0");
		}*/
		amountEligible.setReadOnly(true);
		
		availableOPSI = new TextField("Available OP SI");
		if(bean.getReportOPAvailableSI() != null){
			availableOPSI.setValue(bean.getReportOPAvailableSI().toString());
		}/*else{
			availableOPSI.setValue("0");
		}*/
		availableOPSI.setReadOnly(true);
		
		amountPayable = new TextField("Amount Payable");
		Integer min = Math.min(SHAUtils.getIntegerFromStringWithComma(amountEligible.getValue()), SHAUtils.getIntegerFromStringWithComma(availableOPSI.getValue()) );
		if(min > 0){
			amountPayable.setValue(min.toString());
		}/*else{
			amountPayable.setValue("0");
		}*/
		amountPayable.setReadOnly(true);
		

		FormLayout infoForm = new FormLayout(amountEligible);
		FormLayout infoForm2 = new FormLayout(availableOPSI);
		FormLayout infoForm3 = new FormLayout(amountPayable);


		//Label appAmtLabel = new Label("Approved Amount");
		approvalAmt = new TextField("Approved Amount");
		approvalAmt.setValue(bean.getAmountEligible().toString());
		approvalAmt.setReadOnly(true);

		//Label appRemLabel = new Label("Approval Remarks <b style= 'color: red'>*</b>");
		approvalRemarks = new TextArea("Approval Remarks <b style= 'color: red'>*</b>");
		approvalRemarks.setCaptionAsHtml(true);
		approvalRemarks.setRows(5);
		approvalRemarks.setColumns(16);
		handleTextAreaPopup(approvalRemarks,null);

	//	Label payOptLabel = new Label("Payment Mode <b style= 'color: red'>*</b>");

		optPaymentMode = new OptionGroup("Payment Mode <b style= 'color: red'>*</b>");
		optPaymentMode.setCaptionAsHtml(true);
		optPaymentMode.addItems(getRadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean value = (Boolean) event.getProperty().getValue();
				if(value){
					updatePaymentFlag(true, false);
					paymentTypelayout.removeAllComponents();
					paymentTypelayout.addComponent(buildChequelayout());
				}else{
					updatePaymentFlag(false, true);
					paymentTypelayout.removeAllComponents();
					paymentTypelayout.addComponent(buildBanklayout());
				}
			}
		});

		FormLayout infoForm4 = new FormLayout(approvalAmt);
		FormLayout infoForm5 = new FormLayout(approvalRemarks);
		
		
		HorizontalLayout amtlayout = new HorizontalLayout(infoForm, infoForm2,infoForm3);
		amtlayout.setSpacing(true);

		HorizontalLayout approvallayout = new HorizontalLayout( infoForm4,  infoForm5);
		approvallayout.setSpacing(true);

		HorizontalLayout paymentlayout = new HorizontalLayout( optPaymentMode);
		paymentlayout.setSpacing(true);

		VerticalLayout containerVLayout = new VerticalLayout(amtlayout, approvallayout, paymentlayout, paymentTypelayout);

		HorizontalLayout containerHLayout = new HorizontalLayout(containerVLayout);

		HorizontalLayout appHolderlayout = new HorizontalLayout(containerHLayout);
		appHolderlayout.setWidth("100%");
		appHolderlayout.setComponentAlignment(containerHLayout, Alignment.MIDDLE_LEFT);

		return appHolderlayout;
	}

	public HorizontalLayout buildRejectLayout(){
		FormLayout rejectionForm = new FormLayout();
		rejectRemarks = new TextArea("Rejection Remarks");
		rejectionForm.addComponent(rejectRemarks);
		HorizontalLayout rejectlayout = new HorizontalLayout(rejectionForm);
		
		HorizontalLayout rejHolderlayout = new HorizontalLayout(rejectlayout);
		rejHolderlayout.setWidth("100%");
		rejHolderlayout.setComponentAlignment(rejectlayout, Alignment.MIDDLE_CENTER);
		
		return rejHolderlayout;
	}

	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	public HorizontalLayout buildChequelayout(){
		chqmodeChngReason = new TextField("Reason for change (Pay Mode) <b style= 'color: red'>*</b>");
		chqmodeChngReason.setCaptionAsHtml(true);
		chqmodeChngReason.setValue((bean.getChqModeChngReason() == null)?"":bean.getChqModeChngReason());
		
		chqEmailId = new TextField("Email ID <b style= 'color: red'>*</b>");
		chqEmailId.setCaptionAsHtml(true);
		chqEmailId.setValue((bean.getChqEmailId() == null)?"":bean.getChqEmailId());
		
		chqPanno = new TextField("PAN No <b style= 'color: red'>*</b>");
		chqPanno.setCaptionAsHtml(true);
		chqPanno.setValue((bean.getChqPanno() == null)?"":bean.getChqPanno());
		
		chqPayableAt = new TextField("Payable at <b style= 'color: red'>*</b>");
		chqPayableAt.setCaptionAsHtml(true);
		chqPayableAt.setValue((bean.getChqPayableAt() == null)?"":bean.getChqPayableAt());
		
		FormLayout left = new FormLayout(chqmodeChngReason, chqEmailId, chqPanno, chqPayableAt);
		
		chqPayeeName = new ComboBox("Payee Name <b style= 'color: red'>*</b>");
		chqPayeeName.setCaptionAsHtml(true);
		BeanItemContainer<Insured> insuredList = insuredService.getCLSInsuredList(bean.getPolicyDto().getPolicyNumber());
		chqPayeeName.setContainerDataSource(insuredList);
		chqPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		chqPayeeName.setItemCaptionPropertyId("insuredName");
		chqPayeeName.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		bean.setChqPayeeName(bean.getNewIntimationDTO().getInsuredPatient());
		
		chqPayeeName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured Insval = (Insured) event.getProperty().getValue();
				if(Insval != null){
					bean.setChqPayeeName(Insval);
				}else{
					bean.setChqPayeeName(null);
				}
			}
		});
		
		chqNameChngReason = new TextField("Reason For Change (Payee Name) <b style= 'color: red'>*</b>");
		chqNameChngReason.setCaptionAsHtml(true);
		chqNameChngReason.setValue((bean.getChqNameChngReason() == null)?"":bean.getChqNameChngReason());
		
//		chqHeirName = new TextField("Legal Heir Name <b style= 'color: red'>*</b>");
//		chqHeirName.setCaptionAsHtml(true);
//		chqHeirName.setValue((bean.getChqHeirName() == null)?"":bean.getChqHeirName());
		
		FormLayout right = new FormLayout(chqPayeeName, chqNameChngReason);
		
		HorizontalLayout returnlayout = new HorizontalLayout(left, right);
		returnlayout.setSpacing(true);
		
		return returnlayout;
		
	}
	
	public HorizontalLayout buildBanklayout(){
		VerticalLayout holderLay = new VerticalLayout();

		Button btnPopulatePreviousAccntDetails = new Button("Use account details from policy/previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);

		btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {	
				
				btnOk = new Button("OK");
				btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				btnOk.setWidth("-1px");
				btnOk.setHeight("-10px");
				//Vaadin8-setImmediate() btnOk.setImmediate(true);
				
				btnCancel = new Button("CANCEL");
				btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
				btnCancel.setWidth("-1px");
				btnCancel.setHeight("-10px");
				//Vaadin8-setImmediate() btnCancel.setImmediate(true);
				
				btnOk.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						String err = previousAccountDetailsTable.isValidate();
						if("" == err){								
							buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK);
						}
					}
				});
			
			btnCancel.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					populatePreviousWindowPopup.close();
//					buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);
				}
			});
			
				
				previousAccntDetailsButtonLayout = new HorizontalLayout(btnOk,btnCancel);
				
				previousAccountDetailsTable.resetTableDataList();
				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");

				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.OP_IFSC);
				previousPaymentVerticalLayout = new VerticalLayout();
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
				populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);	
				previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
				previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);				


				setPreviousAccountDetailsValues();
				populatePreviousWindowPopup.setClosable(true);
				populatePreviousWindowPopup.center();
				populatePreviousWindowPopup.setResizable(true);

				populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				populatePreviousWindowPopup.setModal(true);
				populatePreviousWindowPopup.setClosable(false);

				UI.getCurrent().addWindow(populatePreviousWindowPopup);
			}
		});

		Button btnClearAll = new Button("Clear All");
		btnClearAll.addStyleName(ValoTheme.BUTTON_BORDERLESS);

		HorizontalLayout clearlayout = new HorizontalLayout(btnPopulatePreviousAccntDetails, btnClearAll);
		clearlayout.setSpacing(true);

		bnkmodeChngReason = new TextField("Reason for change (Pay Mode) <b style= 'color: red'>*</b>");
		bnkmodeChngReason.setCaptionAsHtml(true);
		bnkmodeChngReason.setValue((bean.getBnkNameChngReason() == null)?"":bean.getBnkNameChngReason());
		
		bnkEmailId = new TextField("Email ID <b style= 'color: red'>*</b>");
		bnkEmailId.setCaptionAsHtml(true);
		bnkEmailId.setValue((bean.getBnkEmailId() == null)?"":bean.getBnkEmailId());
		
		bnkPanno = new TextField("PAN No <b style= 'color: red'>*</b>");
		bnkPanno.setCaptionAsHtml(true);
		bnkPanno.setValue((bean.getBnkPanno() == null)?"":bean.getBnkPanno());
		
		bnkAccNo = new TextField("Account No <b style= 'color: red'>*</b>");
		bnkAccNo.setCaptionAsHtml(true);
		bnkAccNo.setValue((bean.getBnkAccNo() == null)?"":bean.getBnkAccNo());
		
		bnkIfsc = new TextField("IFSC Code <b style= 'color: red'>*</b>");
		bnkIfsc.setCaptionAsHtml(true);
		bnkIfsc.setValue((bean.getBnkIfsc() == null)?"":bean.getBnkIfsc());

		bnkName = new TextField("Bank Name <b style= 'color: red'>*</b>"); // No Need to save
		bnkName.setCaptionAsHtml(true);
		bnkCity = new TextField("City <b style= 'color: red'>*</b>"); // No Need to save
		bnkCity.setCaptionAsHtml(true);
		FormLayout left = new FormLayout(bnkmodeChngReason, bnkEmailId, bnkPanno, bnkAccNo, bnkIfsc, bnkName, bnkCity);

		bnkPayeeName = new ComboBox("Payee Name <b style= 'color: red'>*</b>");
		bnkPayeeName.setCaptionAsHtml(true);
		BeanItemContainer<Insured> insuredList = insuredService.getCLSInsuredList(bean.getPolicyDto().getPolicyNumber());
		bnkPayeeName.setContainerDataSource(insuredList);
		bnkPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bnkPayeeName.setItemCaptionPropertyId("insuredName");
		bnkPayeeName.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		bean.setBnkPayeeName(bean.getNewIntimationDTO().getInsuredPatient());
		
		bnkPayeeName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured Insval = (Insured) event.getProperty().getValue();
				if(Insval != null){
					bean.setBnkPayeeName(Insval);
				}else{
					bean.setBnkPayeeName(null);
				}
			}
		});

		bnkNameChngReason = new TextField("Reason For Change (Payee Name) <b style= 'color: red'>*</b>");
		bnkNameChngReason.setCaptionAsHtml(true);
		bnkNameChngReason.setValue((bean.getBnkNameChngReason() == null)?"":bean.getBnkNameChngReason());
		
//		bnkHeirName = new TextField("Legal Heir Name <b style= 'color: red'>*</b>");
//		bnkHeirName.setCaptionAsHtml(true);
//		bnkHeirName.setValue((bean.getBnkHeirName() == null)?"":bean.getBnkHeirName());
		
		bnkBranch = new TextField("Branch <b style= 'color: red'>*</b>"); // No Need to save
		bnkBranch.setCaptionAsHtml(true);

		btnIFCSSearch = new Button();
		btnIFCSSearch.setDescription("Pick IFSC Code");
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));

		btnIFCSSearch.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5797933806585865425L;
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.OP_IFSC);
				viewSearchCriteriaWindow.initView();
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
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
		});
		FormLayout right = new FormLayout(bnkPayeeName, bnkNameChngReason, bnkBranch, btnIFCSSearch);

		HorizontalLayout supportlayout = new HorizontalLayout(left, right);
		supportlayout.setSpacing(true);

		holderLay.addComponent(clearlayout);
		holderLay.addComponent(supportlayout);
		holderLay.setComponentAlignment(clearlayout, Alignment.MIDDLE_RIGHT);


		HorizontalLayout returnlayout = new HorizontalLayout(holderLay);

		return returnlayout;
	}

//	public void setDropDownValues(){}

//	public void addListeners(){}

	public boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		
		if(!bean.isApprove() && !bean.isReject()){
			hasError = true;
			errMsg.append("Please approve or reject the claim </br>");
		}
		if(bean.isApprove()){
			//Prevent op approval where SI amount is Zero
			
			if(bean.getReportOPAvailableSI() == 0 ){
				hasError = true;
				errMsg.append(" Available OP SI Limit is 0.0 </br>");
			}
			
			if(bean.isChqPayment() != null){
				if(bean.isChqPayment()){
					if(bean.isChqPayment() && chqPayeeName != null && (chqPayeeName.getValue() == null || StringUtils.isBlank(chqPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please select the payee name </br>");
					}

					if(bean.isChqPayment() && chqmodeChngReason != null && (chqmodeChngReason.getValue() == null|| StringUtils.isBlank(chqPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Reason for change (Pay Mode) </br>");
					}
					
					if(bean.isChqPayment() && chqEmailId != null && (chqEmailId.getValue() == null|| StringUtils.isBlank(chqEmailId.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Email ID </br>");
					}
					
					if(bean.isChqPayment() && chqPanno != null && (chqPanno.getValue() == null|| StringUtils.isBlank(chqPanno.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter PAN No </br>");
					}
					
					if(bean.isChqPayment() && chqPayableAt != null && (chqPayableAt.getValue() == null|| StringUtils.isBlank(chqPayableAt.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Payable at </br>");
					}
					
					if(bean.isChqPayment() && chqNameChngReason != null && (chqNameChngReason.getValue() == null|| StringUtils.isBlank(chqNameChngReason.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Reason For Change (Payee Name) </br>");
					}
					
//					if(bean.isChqPayment() && chqHeirName != null && (chqHeirName.getValue() == null|| StringUtils.isBlank(chqHeirName.getValue().toString()))){
//						hasError = true;
//						errMsg.append("Please enter Legal Heir Name </br>");
//					}
					
				}else if(!bean.isChqPayment()){
					if(!bean.isChqPayment() && bnkPayeeName != null && (bnkPayeeName.getValue() == null|| StringUtils.isBlank(bnkPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please select the payee name </br>");
					}

					if(!bean.isChqPayment() && bnkmodeChngReason != null && StringUtils.isBlank(bnkmodeChngReason.getValue())){
						hasError = true;
						errMsg.append("Please enter Reason for change (Pay Mode) </br>");
					}
					
					if(!bean.isChqPayment() && bnkEmailId != null && StringUtils.isBlank(bnkEmailId.getValue())){
						hasError = true;
						errMsg.append("Please enter Email ID </br>");
					}
					
					if(!bean.isChqPayment() && bnkPanno != null && StringUtils.isBlank(bnkPanno.getValue())){
						hasError = true;
						errMsg.append("Please enter PAN No </br>");
					}

					if(!bean.isChqPayment() && bnkAccNo != null && StringUtils.isBlank(bnkAccNo.getValue())){
						hasError = true;
						errMsg.append("Please fill the account number </br>");
					}

					if(!bean.isChqPayment() && bnkIfsc != null && StringUtils.isBlank(bnkIfsc.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code </br>");
					}
					
					if(!bean.isChqPayment() && bnkName != null && StringUtils.isBlank(bnkName.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter Bank Name manually. </br>");
					}
					
					if(!bean.isChqPayment() && bnkCity != null && StringUtils.isBlank(bnkCity.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter City manually. </br>");
					}
					
					if(!bean.isChqPayment() && bnkNameChngReason != null && StringUtils.isBlank(bnkNameChngReason.getValue())){
						hasError = true;
						errMsg.append("Please enter Reason For Change (Payee Name) </br>");
					}
					
//					if(!bean.isChqPayment() && bnkHeirName != null && StringUtils.isBlank(bnkHeirName.getValue())){
//						hasError = true;
//						errMsg.append("Please enter Legal Heir Name </br>");
//					}
					
					if(!bean.isChqPayment() && bnkBranch != null && StringUtils.isBlank(bnkBranch.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter Branch manually.</br>");
					}
				}
			}else{
				hasError = true;
				errMsg.append("Please select Payment Mode </br>");
			}
		}
		
		if(bean.isReject()){
			if(rejectRemarks != null && StringUtils.isBlank(rejectRemarks.getValue())){
				hasError = true;
				errMsg.append("Please fill the rejection remarks </br>");
			}
		}

		if(!hasError){
			if(bean.isApprove()){
				if(bean.isChqPayment()){
					bean.setChqModeChngReason(chqmodeChngReason.getValue());
					bean.setChqEmailId(chqEmailId.getValue());
					bean.setChqPanno(chqPanno.getValue());
					bean.setChqPayableAt(chqPayableAt.getValue());
					bean.setChqNameChngReason(chqNameChngReason.getValue());
//					bean.setChqHeirName(chqHeirName.getValue());
				}else{
					bean.setBnkmodeChngReason(bnkmodeChngReason.getValue());
					bean.setBnkEmailId(bnkEmailId.getValue());
					bean.setBnkPanno(bnkPanno.getValue());
					bean.setBnkAccNo(bnkAccNo.getValue());
					bean.setBnkIfsc(bnkIfsc.getValue());
					bean.setBnkNameChngReason(bnkNameChngReason.getValue());
//					bean.setBnkHeirName(bnkHeirName.getValue());
					bean.getDocumentDetails().getPayeeName().setValue(bnkPayeeName.getValue().toString());
					Double balancsSIaftPayable = (bean.getAvailableSI() != null ? Double.valueOf(bean.getAvailableSI()) : 0d) - (bean.getPayble() != null ? Double.valueOf(bean.getPayble()) :0d);
					bean.getDocumentDetails().setBalanceSI(balancsSIaftPayable > 0d ? balancsSIaftPayable : 0d);
					if(bean.getPolicyDto().getProduct().getKey() != null &&
							!bean.getPolicyDto().getProduct().getKey().equals(601l)){
						bean.getDocumentDetails().setBalanceSI(bean.getPerPolicyLimit());
					}
					bean.getDocumentDetails().setAmountPayable(Double.valueOf(bean.getPayble()));
				}
				bean.setApprovalRemarks(approvalRemarks.getValue());
			}
			if(bean.isReject()){
				bean.setRejectRemarks(rejectRemarks.getValue());
			}
			
		}
		
		return hasError;
	}

	@SuppressWarnings("static-access")
	public void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		bnkIfsc.setReadOnly(false);
		bnkIfsc.setValue(dto.getIfscCode());
		bnkIfsc.setReadOnly(true);
		
		bnkName.setReadOnly(false);
		bnkName.setValue(dto.getBankName());
		bnkName.setReadOnly(true);
		
		bnkBranch.setReadOnly(false);
		bnkBranch.setValue(dto.getBranchName());
		bnkBranch.setReadOnly(true);
		
		bnkCity.setReadOnly(false);
		bnkCity.setValue(dto.getCity());
		bnkCity.setReadOnly(true);
	}
	
	public void setPreviousAccountDetailsValues() {
		if(null != previousAccountDetailsTable)	{
			int rowCount = 1;
			List<PreviousAccountDetailsDTO> previousListTable = this.bean.getPreviousAccntDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty()){				
				for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousListTable) {
					previousAccountDetailsDTO.setChkSelect(false);
					previousAccountDetailsDTO.setChkSelect(null);						
					previousAccountDetailsDTO.setSerialNo(rowCount);
					previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
					rowCount ++ ;
				}
			}
		}
	}
	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
//		
//		if(null != txtEmailId)
//		{
//			txtEmailId.setReadOnly(false);
//			txtEmailId.setEnabled(true);
//			txtEmailId.setValue(tableDTO.getEmailId());
//			//txtEmailId.setReadOnly(true);
//		}
//		if(null != txtPanNo)
//		{
//			txtPanNo.setReadOnly(false);
//			txtPanNo.setEnabled(true);
//			txtPanNo.setValue(tableDTO.getPanNo());
//			//txtPanNo.setReadOnly(true);
//		}
//		if(null != txtAccntNo)
//		{
//			txtAccntNo.setReadOnly(false);
//			txtAccntNo.setEnabled(true);
//
//			txtAccntNo.setValue(tableDTO.getBankAccountNo());
//			//txtAccntNo.setReadOnly(true);
//		}
//		if(null != txtIfscCode)
//		{
//			txtIfscCode.setReadOnly(false);
//			txtIfscCode.setEnabled(true);
//
//			txtIfscCode.setValue(tableDTO.getIfsccode());
//			//txtIfscCode.setReadOnly(true);
//		}
//		if(null != txtBankName)
//		{
//			txtBankName.setReadOnly(false);
//			txtBankName.setEnabled(true);
//
//			txtBankName.setValue(tableDTO.getBankName());
//			//txtBankName.setReadOnly(true);
//		}
//		if(null != txtCity)
//		{
//			txtCity.setReadOnly(false);
//			txtCity.setEnabled(true);
//			txtCity.setValue(tableDTO.getBankCity());
//			//txtCity.setReadOnly(true);
//		}
//		if(null != txtBranch)
//		{
//			txtBranch.setReadOnly(false);
//			txtBranch.setEnabled(true);
//			txtBranch.setValue(tableDTO.getBankBranch());
//			//txtBranch.setReadOnly(true);
//		}
//
	}
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);

		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);

		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
			}
		});
		if(null != cancelBtn){
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	public void showUploadedDocuments(){
		opUploadedDetails.init("OP Uploaded Documents", false, false);
		opUploadedDetails.setScreenName("OP");

		Page<UploadDocumentDTO> opPage = new Page<UploadDocumentDTO>();
		opPage.setPageItems(listOfdocDetailsObj);
		opPage.setTotalRecords(listOfdocDetailsObj.size());
		opPage.setTotalList(listOfdocDetailsObj);
		opUploadedDetails.setTableList(opPage.getTotalList());
		opUploadedDetails.setSubmitTableHeader();
		opUploadedDetails.setSizeFull();
		//------------------End of loading OP Uploaded Doc table------------------------
		
		VerticalLayout misLayout = new VerticalLayout(opUploadedDetails);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(misLayout);
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
	
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));
	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Comments";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}

}
