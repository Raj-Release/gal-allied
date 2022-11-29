/**
 * 
 */
package com.shaic.claim.rod.wizard.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.listenerTables.UpdateOtherClaimDetailsUI;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.wizard.PatientCareListenerTable;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.claim.rod.wizard.pages.HospitalCashProductDetailsTable;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsForBillEntry;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryDetailsPage extends ViewComponent { 
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private UploadedDocumentsListenerTable uploadListenerTable;
	//private UploadedDocumentsTableForBillEntry uploadListenerTable;
	
	//Below table is used to retreive values from uploaded table present in upload documents page.
	@Inject
	private UploadedDocumentsForBillEntry uploadedDocsTable;
	
	@Inject
	private UpdateOtherClaimDetailsUI updateOtherClaimDetailsUI;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	private Panel uploadDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	private VerticalLayout mainLayout ;
	
	private VerticalLayout benefitsLayout;
	
	private Panel benefitsPanel;
	
	private OptionGroup hospitalAddOnBenefits;
	
	private OptionGroup patientCareAddOnBenefits;
	
	private VerticalLayout hospitalCashBenefitsLayout;
	
	private VerticalLayout patientCareBenefitsLayout;

	
	private TextField hospitalCashNoOfDays;
	
	private TextField hospitalCashPerDayAmt;
	
	private TextField hospitalCashTotalClaimedAmt;
	
	private TextField patientCareNoOfDays;
	
	private TextField patientCarePerDayAmt;
	
	private TextField patientCareTotalClaimedAmt;
	
	private OptionGroup treatmentPhysiotherapy;	
	
	private TextArea remarksBillEntry;
	
	/*@Inject
	private Instance<PatientCareTable> patientCareTableInstance;*/
	
	//private PatientCareTable patientCareTableObj;
	@Inject
	//private Instance<PatientCareListenerTable> patientCareTable;
	private PatientCareListenerTable patientCareTableObj;
	
	protected Map<String, Object> referenceDataForPatientCare = new HashMap<String, Object>();


	private List<String> errorList;
	
	private List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	//private int iBillEntryTableSize;
	
	private Button billingWorksheetBtn;
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	//added for new product
	private TextField particulars;
	@Inject
	private HospitalCashProductDetailsTable hospitalCashProductDetailsTable;
	
	private VerticalLayout hospitalCashProductLayout;
	
	////private static Window popup;

	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		
/*		this.bean.getUploadDocumentsDTO().setHospitalizationFlag(bean.getDocumentDetails().getHospitalizationFlag());
		this.bean.getUploadDocumentsDTO().setPreHospitalizationFlag(bean.getDocumentDetails().getPreHospitalizationFlag());
		this.bean.getUploadDocumentsDTO().setPostHospitalizationFlag(bean.getDocumentDetails().getPostHospitalizationFlag());
		this.bean.getUploadDocumentsDTO().setPartialHospitalizationFlag(bean.getUploadDocumentsDTO().getPartialHospitalizationFlag());
		this.bean.getUploadDocumentsDTO().setHospitalizationRepeatFlag(bean.getDocumentDetails().getHospitalizationRepeatFlag());*/
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean
				.getUploadDocumentsDTO());
		
		
		uploadDocMainLayout = new VerticalLayout();
		uploadDocsPanel = new Panel();
		benefitsLayout = new VerticalLayout();
		benefitsPanel = new Panel();
		
		mainLayout = new VerticalLayout();
		
	}
	
	public Component getContent() 
	{	
		initBinder();
		errorList = new ArrayList<String>();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		uploadListenerTable.initPresenter(SHAConstants.BILL_ENTRY);
	//	uploadListenerTable.init(iBillEntryTableSize);
		uploadListenerTable.init();
		
		uploadListenerTable.setReferenceData(this.referenceData);
		enableUploadedDocsTableForBillEntry();
		
		hospitalCashProductDetailsTable.init(bean); 
		
		//patientCareList = new ArrayList<PatientCareDTO>();

		hospitalCashBenefitsLayout = new VerticalLayout();
		patientCareBenefitsLayout = new VerticalLayout();
		hospitalCashProductLayout = new VerticalLayout();
		
		
		
		Button updatePreviousButton = new Button("Update Previous/Other Claim Details(Defined Limit)");
		
		updatePreviousButton.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(BillEntryDetailsPresenter.UPDATE_PREVIOUS_CLAIM_DETAILS, bean);
			}
		});
		
		HorizontalLayout updatePreviousClaimAmount = new HorizontalLayout(updatePreviousButton);
		
		VerticalLayout uploadedDocsLayout = new VerticalLayout(updatePreviousClaimAmount,uploadListenerTable);
		//verticalLayout.addComponent(uploadListenerTable);
		uploadedDocsLayout.setCaption("Uploaded Documents");
		uploadedDocsLayout.setHeight("100%");
		uploadedDocsLayout.setSpacing(true);
		uploadedDocsLayout.setMargin(true);
		updatePreviousClaimAmount.setVisible(false);
		
		
		Product product =this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			updatePreviousClaimAmount.setVisible(true);
			uploadedDocsLayout.setComponentAlignment(updatePreviousClaimAmount, Alignment.TOP_RIGHT);
		}
		

		/*hospitalAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Hospital Cash) Applicable ", "hospitalCashAddonBenefits", OptionGroup.class);*/
		hospitalAddOnBenefits = new OptionGroup("Add on Benefits (Hospital Cash) Applicable");
		
		hospitalAddOnBenefits.addItems(getReadioButtonOptions());
		hospitalAddOnBenefits.setItemCaption(true, "Yes");
		hospitalAddOnBenefits.setItemCaption(false, "No");
		hospitalAddOnBenefits.setStyleName("horizontal");
		hospitalAddOnBenefits.setEnabled(false);
		
		
		/*if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")) && ( 
				!(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash()) && (this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash())))*/
		/*if (!(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash() && (this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash())))
		{
			hospitalAddOnBenefits.setEnabled(false);
		}
		else
		{
			fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_ADD_ON_BENEFITS_HOSPITAL_CASH, this.bean);
		}*/
		
		
		
		
		/*patientCareAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Patient Care) Applicable ", "patientCareAddOnBenefits", OptionGroup.class);*/
		patientCareAddOnBenefits = new OptionGroup("Add on Benefits (Patient Care) Applicable");
		
		patientCareAddOnBenefits.addItems(getReadioButtonOptions());
		patientCareAddOnBenefits.setItemCaption(true, "Yes");
		patientCareAddOnBenefits.setItemCaption(false, "No");
		patientCareAddOnBenefits.setStyleName("horizontal");
		patientCareAddOnBenefits.setEnabled(false);
		
		remarksBillEntry = binder.buildAndBind("Remarks(Bill Entry)","remarksBillEntry",TextArea.class);
		remarksBillEntry.setCaptionAsHtml(true);
		remarksBillEntry.setMaxLength(4000);
		remarksBillEntry.setWidth("250px");
		remarksBillEntry.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		remarksBillEntry.setRequired(true);
		SHAUtils.handleTextAreaPopupDetails(remarksBillEntry,null,getUI(),SHAConstants.REMARKS_BILL_ENTRY);
		remarksBillEntry.setData(this.bean.getUploadDocumentsDTO().getRemarksBillEntry() != null ? this.bean.getUploadDocumentsDTO().getRemarksBillEntry() : "");

		
		billingWorksheetBtn = new Button("Billing Worksheet");
		
		/*if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag"))&& ( 
				(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCare()) && (this.bean.getDocumentDetails().getAddOnBenefitsPatientCare())))*/
		
		/*if (!(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCare() && (this.bean.getDocumentDetails().getAddOnBenefitsPatientCare())))
		{
			patientCareAddOnBenefits.setEnabled(false);
		}
		else
		{
			fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_ADD_ON_BENEFITS_PATIENT_CARE, this.bean);
		}*/
		
		
		
		if(null != this.bean.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag() && ("Y").equalsIgnoreCase(this.bean.getUploadDocumentsDTO().getHospitalCashAddonBenefitsFlag()))
		{
			fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_HOSPITAL_BENEFITS, true);
		}
		
		if(null != this.bean.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag() && ("Y").equalsIgnoreCase(this.bean.getUploadDocumentsDTO().getPatientCareAddOnBenefitsFlag()))
		{
			fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_PATIENT_CARE_BENEFITS, true);
			
		}
		
		
		if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
		{
			fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_HOSPITAL_CASH_PRODUCT_BENEFITS, true);
		}
		
		uploadDocMainLayout.addComponent(uploadedDocsLayout);
		uploadDocsPanel.setContent(uploadDocMainLayout);
		
//		benefitsLayout.setWidth("100%");
		
		benefitsLayout.addComponent(hospitalAddOnBenefits);
		benefitsLayout.addComponent(hospitalCashBenefitsLayout);
		benefitsLayout.addComponent(patientCareAddOnBenefits);
		benefitsLayout.addComponent(patientCareBenefitsLayout);
		benefitsLayout.addComponent(hospitalCashProductLayout);		
		benefitsLayout.addComponent(remarksBillEntry);
		benefitsPanel.setContent(benefitsLayout);
		
		mainLayout.addComponent(uploadDocsPanel); 
		mainLayout.addComponent(benefitsPanel);
//		mainLayout.addComponent(uploadDocsPanel); 
		
		
	

		
		addListener();
		
		selectHospitalCashFlag();
		selectPatientCareFlag();
		
		addTotalClaimedListener();
		setTableValues();
		//return uploadDocsPanel;
		return mainLayout;
	}
	
	private void selectHospitalCashFlag()
	{
		if ((null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash() && (this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash())))
		{
			//fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_ADD_ON_BENEFITS_HOSPITAL_CASH, this.bean);
		//	hospitalAddOnBenefits.setValue(true);	
			Boolean val = this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash();
				/**
				 * The below if block is added to enabling the value change listener
				 * for option group. When the screen is painted for first time, the 
				 * payment mode will be null. When we proceed to next step a value
				 * is assigned in bean. When again traversing back, the same value
				 * is set to the option group, there by ,value change listener is not
				 * invoked. Hence to invoke value change listner, twice the value
				 * is set to optiongroup. This is like, selecting and unselecting the
				 * group. If the value is true, we first set false and again we set to true
				 * and vice versa.
				 * */
				if(val)
				{
					hospitalAddOnBenefits.setValue(false);
				}
				//unbindField(optPaymentMode);
				hospitalAddOnBenefits.setValue(val);			
		}
		else
		{
			hospitalAddOnBenefits.setValue(null);
		}
	}
	
	private void selectPatientCareFlag()
	{
		if ((null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCare() && (this.bean.getDocumentDetails().getAddOnBenefitsPatientCare())))
		{
			//fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_ADD_ON_BENEFITS_PATIENT_CARE, this.bean);
			Boolean val = this.bean.getDocumentDetails().getAddOnBenefitsPatientCare();
			/**
			 * The below if block is added to enabling the value change listener
			 * for option group. When the screen is painted for first time, the 
			 * payment mode will be null. When we proceed to next step a value
			 * is assigned in bean. When again traversing back, the same value
			 * is set to the option group, there by ,value change listener is not
			 * invoked. Hence to invoke value change listner, twice the value
			 * is set to optiongroup. This is like, selecting and unselecting the
			 * group. If the value is true, we first set false and again we set to true
			 * and vice versa.
			 * */
			if(val)
			{
				patientCareAddOnBenefits.setValue(false);
			}
			patientCareAddOnBenefits.setValue(val);
			
		}
		else
		{
			patientCareAddOnBenefits.setValue(null);
		}
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	

	
	private void addListener()
	{
		hospitalAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_HOSPITAL_BENEFITS, isChecked);
			}
		});
		
		patientCareAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_PATIENT_CARE_BENEFITS, isChecked);
			}
		});
		
		billingWorksheetBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7439398898591124006L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				
				uploadDocumentViewImpl.initPresenter(SHAConstants.BILLING_WORKSHEET);
				//uploadDocumentViewImpl.init(bean,popup);
				popup.setCaption("Billing Worksheet");
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setClosable(true);
				popup.setContent(uploadDocumentViewImpl);
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
		});
	}
	
	private void setTableValues()
	{
		if(null != this.uploadListenerTable)
		{
			
			Integer i = 1;
//			List<UploadDocumentDTO> uploadList = uploadedDocsTable.getValues();
			List<UploadDocumentDTO> uploadList = bean.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadList) {
				/**
				 * If add on benefits alone is choosen as bill classification , then
				 * the bill entry link needs to be disabled. Hence the below if condition
				 * was added.
				 * */
				if(null != bean.getDocumentDetails() && null != bean.getDocumentDetails() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())
						&& ("N").equalsIgnoreCase(bean.getDocumentDetails().getPreHospitalizationFlag()) && ("N").equalsIgnoreCase(bean.getDocumentDetails().getPostHospitalizationFlag())
						&& ("N").equalsIgnoreCase(bean.getDocumentDetails().getPartialHospitalizationFlag()) && ("N").equalsIgnoreCase(bean.getDocumentDetails().getLumpSumAmountFlag())
						&& (("Y").equalsIgnoreCase(bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()) || ("Y").equalsIgnoreCase(bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
				{
					uploadDocumentDTO.setEnableOrDisableBtn(false);
				}	
				else
				{
					uploadDocumentDTO.setEnableOrDisableBtn(true);
				}
			}
			uploadListenerTable.setTableInfo(uploadList);
			//List<BillEntryDetailsDTO> billEntryList = this.bean.getUploadDocumentsDTO().getBillEntryDetailList();
			if(null !=  uploadList && !uploadList.isEmpty())
			for (UploadDocumentDTO uploadDocLayout : uploadList) {
				uploadDocLayout.setSeqNo(i);
				if(null != bean.getClaimDTO() && null != bean.getClaimDTO().getNewIntimationDto() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy() 
						&& null != bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct())
				uploadDocLayout.setProductKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
				uploadDocLayout.setIntimationNo(bean.getIntimationNo());
				uploadDocLayout.setDateOfAdmission(bean.getDateOfAdmission());
				uploadDocLayout.setDateOfDischarge(bean.getDateOfDischarge());
				uploadDocLayout.setInsuredPatientName(bean.getInsuredPatientName());
				uploadDocLayout.setClaimType(bean.getClaimDTO().getClaimType());
				uploadDocLayout.setRodKey(bean.getDocumentDetails().getRodKey());
				uploadDocLayout.setUsername(bean.getStrUserName());
				
				if(null != this.bean.getSectionDetailsDTO() && null != bean.getSectionDetailsDTO().getSubCover() && null != bean.getSectionDetailsDTO().getSubCover().getCommonValue())
					uploadDocLayout.setSubCoverCode(bean.getSectionDetailsDTO().getSubCover().getCommonValue());
				
				List<BillEntryDetailsDTO> billEntryList = uploadDocLayout.getBillEntryDetailList();
				int iSize = 0;
				if(null != billEntryList && !billEntryList.isEmpty())
				{
					 iSize = billEntryList.size();
					List<BillEntryDetailsDTO> billEntryDetailsList = new ArrayList<BillEntryDetailsDTO>();
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryList) {
						if(null != billEntryDetailsDTO.getDocumentSummaryKey())
						{
							if(billEntryDetailsDTO.getDocumentSummaryKey().equals(uploadDocLayout.getDocSummaryKey()))
							{
								/**
								 * The below value is set to true, for ticket 2707. As per this ticket,
								 * in case of query reply, for those bills added earlier when query was raised,
								 * should not be mandatory. Rest of documents , user needs to add bill.
								 * To acheive this, if an bills are already available for an document, we set the
								 * status as true, which will enable tick mark which inturn means, the bills
								 * are entered for the document. Hence rest of the program flow remains untouched.
								 * */
								if(null != uploadDocLayout.getIsBillSaved() && uploadDocLayout.getIsBillSaved())
								{
									uploadDocLayout.setStatus(false);
								}
								else
								{
									uploadDocLayout.setStatus(true);
								}
								//billEntryDetailsList.add(billEntryDetailsDTO);
							}
							
						}
						billEntryDetailsList.add(billEntryDetailsDTO);
					}
					/*if(null != uploadDocLayout.getFileType() && (uploadDocLayout.getFileTypeValue().contains("Bills") || uploadDocLayout.getFileTypeValue().contains("Bill") ))
					{
						uploadDocLayout.setBillEntryDetailList(billEntryDetailsList);
					}*/
				}
				//if(null != uploadDocLayout.getFileType() && (uploadDocLayout.getFileTypeValue().contains("Bills") || uploadDocLayout.getFileTypeValue().contains("Bill") ))
				//{
					uploadDocLayout.setiNoOfEmptyRows(iSize);
					uploadDocLayout.setEmptyRowStatus(false);
					this.uploadListenerTable.addBeanToList(uploadDocLayout);
				//}
				
				i++;
			}
		}
		
		
		if(null != patientCareAddOnBenefits && null != patientCareAddOnBenefits.getValue())
		{
			//List<PatientCareDTO> patientCareList = this.bean.getUploadDocumentsDTO().getPatientCareDTO();
			if(null != this.patientCareList && !this.patientCareList.isEmpty())
			{
				if(null != patientCareTableObj)
				{
					for (PatientCareDTO patientCareDTO : this.patientCareList) {
						this.patientCareTableObj.addBeanToList(patientCareDTO);
					}
				}
			}
			
			List<PatientCareDTO> reconsiderPatientCareList =  this.bean.getUploadDocumentsDTO().getReconsiderationPatientCareDTOList();
			
			if(null != reconsiderPatientCareList &&  !reconsiderPatientCareList.isEmpty())
			{
				patientCareTableObj.removeAllItems();
				for (PatientCareDTO patientCareDTO : reconsiderPatientCareList) {
					this.patientCareTableObj.addBeanToList(patientCareDTO);
				}
			}
		}

		
		if(null != patientCareTableObj)
		{
			List<PatientCareDTO> patientCareList = this.bean.getUploadDocumentsDTO().getPatientCareDTO();
			if(null != patientCareList && !patientCareList.isEmpty())
			{
				patientCareTableObj.removeAllItems();
				for (PatientCareDTO patientCareDTO2 : patientCareList) {
					patientCareTableObj.addBeanToList(patientCareDTO2);
				}
				
			}
		}
		
		//added for new product
		if(null != hospitalCashProductDetailsTable)
		{
			List<HopsitalCashBenefitDTO> hospiitalCashList = this.bean.getUploadDocumentsDTO().getHopsitalCashBenefitDTO();
			if(null != hospiitalCashList && !hospiitalCashList.isEmpty())
			{
				 hospitalCashProductDetailsTable.removeAllItems();
				for (HopsitalCashBenefitDTO hospiitalCashList2 : hospiitalCashList) {
					hospitalCashProductDetailsTable.addBeanToList(hospiitalCashList2);
				}
				
			}
		}
		
		/*if(null != patientCareAddOnBenefits && null != patientCareAddOnBenefits.getValue())
		{
			if(null != patientCareTableObj)
			{
				List<PatientCareDTO> patientCareList = patientCareTableObj.getValues();
				for (PatientCareDTO patientCareDTO : patientCareList) {
					this.patientCareTableObj.addBeanToList(patientCareDTO);
				}
				
			}
		}*/
	}
			/*if(null != uploadListenerTable && null != uploadListenerTable.getValues() && !uploadListenerTable.getValues().isEmpty())
			{
				List<UploadDocumentDTO> uploadDocDTO = uploadListenerTable.getValues();
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocDTO) {
					
				}
			}
		}
	}*/
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		uploadListenerTable.setReferenceData(referenceData);
	}

	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		
		uploadListenerTable.setupCategoryValues(categoryValues);
	}

	public void setBillEntryFinalStatus(UploadDocumentDTO status) {
		
		List<UploadDocumentDTO> uploadDoc = uploadListenerTable.getValues();
		List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
		for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
			if(null != uploadDocumentDTO.getFileType() && null != uploadDocumentDTO.getFileType().getValue())
			{
				if(uploadDocumentDTO.getFileType().getValue().contains("Bill"))
				{
					//if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(status.getBillNo()))
					//Updating the upload listener table based on seq no and not bill no.
					/**
					 * Sequence number is an internal parameter maintained for updating the 
					 * uploadlistener table. This is because the row for which the bill is entered
					 * should only get updated. Rest of rows should  be the same. Earlier this
					 * was done with bill no. But there are chance that even bill no can be duplicate.
					 * Hence removed this and added validation based on seq no.
					 * */
					if(uploadDocumentDTO.getSeqNo().equals(status.getSeqNo()))
					{
						//uploadList.add(status);
					}
					else
					{
						uploadList.add(uploadDocumentDTO);
					}
				}
				else
				{
					uploadList.add(uploadDocumentDTO);
				}
			}
			
		}
		uploadList.add(status);
	uploadListenerTable.updateTable(uploadList);
		
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean value) {
		if(null != hospitalCashBenefitsLayout && hospitalCashBenefitsLayout.getComponentCount() > 0) {
			unbindField(hospitalCashNoOfDays);
			unbindField(hospitalCashPerDayAmt);
			unbindField(hospitalCashTotalClaimedAmt);
			unbindField(treatmentPhysiotherapy);
			hospitalCashBenefitsLayout.removeAllComponents();
		}
		if(value) {
			Table table = new Table();
			table.setHeight("140px");
			table.setWidth("100%");
			table.addContainerProperty("No of Days", TextField.class, null);
			table.addContainerProperty("Per Day Amount", TextField.class, null);
			table.addContainerProperty("Total Claimed Amount", TextField.class, null);
			
			Object addItem = table.addItem();
			
			hospitalCashNoOfDays = (TextField) binder.buildAndBind(
					"", "hospitalCashNoofDays", TextField.class);
			
			CSValidator hospitalCashValidator = new CSValidator();
			hospitalCashValidator.extend(hospitalCashNoOfDays);
			hospitalCashValidator.setRegExp("^[0-9]*$");
			hospitalCashValidator.setPreventInvalidTyping(true);
			hospitalCashNoOfDays.setMaxLength(5);
			
			
			
			hospitalCashNoOfDays.addBlurListener(new BlurListener() {
				
				private static final long serialVersionUID = -7944733816900171622L;

				@Override
				public void blur(BlurEvent event) {
					// Changes Made FOR GALAXYMAIN-13460 
					setCalculatedValue(SHAUtils.getIntegerFromStringRevised(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromStringRevised(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});
			
			hospitalCashPerDayAmt = (TextField) binder.buildAndBind(
					"", "hospitalCashPerDayAmt", TextField.class);
			hospitalCashPerDayAmt.setMaxLength(10);
			
			CSValidator perDayAmtValidator = new CSValidator();
			perDayAmtValidator.extend(hospitalCashPerDayAmt);
			perDayAmtValidator.setRegExp("^[0-9 .]*$");
			perDayAmtValidator.setPreventInvalidTyping(true);
	
			mandatoryFields.add(hospitalCashPerDayAmt);
			setRequiredAndValidation(hospitalCashPerDayAmt);
			showOrHideValidation(false);

		/*	if(null != patientCarePerDayAmt)
			{
				mandatoryFields.remove(patientCarePerDayAmt);
			}
			*/
			hospitalCashPerDayAmt.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					// Changes Made FOR GALAXYMAIN-13460 
					setCalculatedValue(SHAUtils.getIntegerFromStringRevised(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromStringRevised(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});
			
			hospitalCashTotalClaimedAmt = (TextField) binder.buildAndBind(
					"", "hospitalCashTotalClaimedAmt", TextField.class);
			hospitalCashTotalClaimedAmt.setEnabled(false);
			
			table.getContainerProperty(addItem, "No of Days").setValue(hospitalCashNoOfDays);
			table.getContainerProperty(addItem, "Per Day Amount").setValue(hospitalCashPerDayAmt);
			table.getContainerProperty(addItem, "Total Claimed Amount").setValue(hospitalCashTotalClaimedAmt);
			
			treatmentPhysiotherapy = (OptionGroup) binder.buildAndBind("Treatment for Physiotherapy or any Epidemic", "treatmentPhysiotherapy", OptionGroup.class);
			
			//hospitalCashBenefitsLayout = new VerticalLayout(table, treatmentPhysiotherapy);
			hospitalCashBenefitsLayout.addComponent(table);
			hospitalCashBenefitsLayout.addComponent(treatmentPhysiotherapy);
		} 
	}
	
	
	/*private void addHospitalCashListener()
	{
		hospitalCashNoOfDays
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if(null != value)
				{	
					if(null != hospitalCashPerDayAmt)
					{
						
						String total = String.valueOf((Integer.valueOf(value) * Integer.valueOf(hospitalCashPerDayAmt.getValue())));
						hospitalCashTotalClaimedAmt.setValue(total);
					}
					
				}
				
			}
		});
	}*/
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnPatientCareBenefits(Boolean value) {
		
		if(null != patientCareBenefitsLayout &&  patientCareBenefitsLayout.getComponentCount() > 0) {
			unbindField(patientCareNoOfDays);
			unbindField(patientCarePerDayAmt);
			unbindField(patientCareTotalClaimedAmt);
			//unbindField(treatmentPhysiotherapy);
			patientCareBenefitsLayout.removeAllComponents();
		}
		if(value) {
			Table table = new Table();
			table.setHeight("140px");
			table.setWidth("100%");
			table.addContainerProperty("No of Days", TextField.class, null);
			table.addContainerProperty("Per Day Amount", TextField.class, null);
			table.addContainerProperty("Total Claimed Amount", TextField.class, null);
			
			Object addItem = table.addItem();
			
			patientCareNoOfDays = (TextField) binder.buildAndBind(
					"", "patientCareNoofDays", TextField.class);
			patientCareNoOfDays.setMaxLength(3);
			patientCareNoOfDays.setEnabled(false);
			patientCareNoOfDays.setNullRepresentation("");
			CSValidator patientCareNoOfDaysValidator = new CSValidator();
			patientCareNoOfDaysValidator.extend(patientCareNoOfDays);
			patientCareNoOfDaysValidator.setRegExp("^[0-9 .]*$");
			patientCareNoOfDaysValidator.setPreventInvalidTyping(true);
			/*patientCareNoOfDays.addBlurListener(new BlurListener() {
				
				private static final long serialVersionUID = -7944733816900171622L;

				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getIntegerFromString(patientCarePerDayAmt.getValue()), patientCareTotalClaimedAmt);
				}
			});*/
			
			patientCareNoOfDays.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					/*Boolean isChecked = false;
					if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
						isChecked = true;
					} 
					fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_HOSPITAL_BENEFITS, isChecked);*/
					
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getIntegerFromString(patientCarePerDayAmt.getValue()), patientCareTotalClaimedAmt);
				}
			});
			
			
			
			
			patientCarePerDayAmt = (TextField) binder.buildAndBind(
					"", "patientCarePerDayAmt", TextField.class);
			patientCarePerDayAmt.setMaxLength(10);
			//patientCarePerDayAmt.setEnabled(false);
			patientCarePerDayAmt.setEnabled(true);
			
			mandatoryFields.add(patientCarePerDayAmt);
			setRequiredAndValidation(patientCarePerDayAmt);
			showOrHideValidation(false);
			
			/*if(null != hospitalCashPerDayAmt)
			{
				mandatoryFields.remove(hospitalCashPerDayAmt);
			}*/
			
			patientCarePerDayAmt.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getIntegerFromString(patientCarePerDayAmt.getValue()), patientCareTotalClaimedAmt);
				}
			});
			
			patientCareTotalClaimedAmt = (TextField) binder.buildAndBind(
					"", "patientCareTotalClaimedAmt", TextField.class);
			patientCareTotalClaimedAmt.setEnabled(false);
			
			table.getContainerProperty(addItem, "No of Days").setValue(patientCareNoOfDays);
			table.getContainerProperty(addItem, "Per Day Amount").setValue(patientCarePerDayAmt);
			table.getContainerProperty(addItem, "Total Claimed Amount").setValue(patientCareTotalClaimedAmt);
			
			//patientCareTableObj = patientCareTableInstance.get();
			//patientCareTableObj = patientCareTable.get();
		//	patientCareTableObj.init(true,this.bean.getDocumentDetails().getDateOfAdmission());

			patientCareTableObj.init(true,this.bean.getClaimDTO().getNewIntimationDto().getAdmissionDate(),this.bean.getDocumentDetails().getDateOfDischarge());
			patientCareTableObj.setDischargeDate(this.bean.getClaimDTO().getDischargeDate());

			/*patientCareTableObj.init("", true);
			patientCareTableObj.setReference(referenceDataForPatientCare);*/
			
			
			addNoofDaysListener();
			
			
			//patientCareBenefitsLayout = new VerticalLayout(table, patientCareTableObj);
			patientCareBenefitsLayout.addComponent(patientCareTableObj);
			patientCareBenefitsLayout.addComponent(table);
			
		} 
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	
	private void addNoofDaysListener()
	{
		this.patientCareTableObj.dateDiffFld.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String value = (String)event.getProperty().getValue();
				if(null != value)
				{
				patientCareNoOfDays.setValue(value);
				//Vaadin8-setImmediate() patientCareNoOfDays.setImmediate(true);
				}
				else
				{
					patientCareNoOfDays.setValue(null);
					patientCarePerDayAmt.setValue(null);
				}
			}
			
			
		});
		
		/*this.patientCareTableObj.engagedDateFld.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Date value = (Date)event.getProperty().getValue();
				if(value.before(bean.getDocumentDetails().getDateOfAdmission()))
				{
					Label label = new Label("Engaged from date is less than admission date", ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				
	
			}
			
			
		});*/
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	private void setCalculatedValue(Integer value1, Integer value2, TextField calculatedValueField) {
		Integer calculatedValue = value1 * value2;
		
		calculatedValueField.setValue(String.valueOf(calculatedValue));
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.patientCareAddOnBenefits && null != (this.patientCareAddOnBenefits.getValue()) && ("true").equalsIgnoreCase(this.patientCareAddOnBenefits.getValue().toString()) && null != patientCareTableObj )
		{
			List<PatientCareDTO> patientList = patientCareTableObj.getValues();
			this.bean.getUploadDocumentsDTO().setPatientCareDTO(patientList);
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
		{
			List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
			this.bean.getUploadDocumentsDTO().setHopsitalCashBenefitDTO(hospitalCashBenefit);
		}
	}
	
	public boolean validatePage() { 
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		showOrHideValidation(true);
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append( errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
//		List<UploadDocumentDTO> uploadList = uploadedDocsTable.getValues();
		List<UploadDocumentDTO> uploadList = bean.getUploadDocsList();
		if(enableUploadedDocsTableForBillEntry())
		{
			this.bean.setTotalClaimedAmount(uploadListenerTable.getTotalClaimedAmount());
			if((null != uploadList && !uploadList.isEmpty()))
			{
				for (UploadDocumentDTO uploadDoc : uploadList) 
				{
					List<BillEntryDetailsDTO> billEntryList = uploadDoc.getBillEntryDetailList();
					if(null != billEntryList && !billEntryList.isEmpty())
					{
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryList)
						{
							/*if(!(uploadDoc.getBillNo().equals(billEntryDetailsDTO.getBillNo())))
							{
								hasError = true;
								eMsg += "Please enter Bill details for uploaded documents </br>";
								break;
							}*/
							if(null != uploadDoc.getStatus() && !uploadDoc.getStatus())
							{
								hasError = true;
								eMsg.append("Please enter Bill details for uploaded documents </br>");
								break;
							}
						}
					}
					else
					{
						if(!(null != bean && null != bean.getDocumentDetails() && (null != bean.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())
								&& (null != bean.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getPartialHospitalizationFlag()))
								&& (null != bean.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getPreHospitalizationFlag()))
								&& (null != bean.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getPostHospitalizationFlag()))
								&& (null != bean.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(bean.getDocumentDetails().getLumpSumAmountFlag()))
								&& ( (null != bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()) 
								      || null != bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))))
						{
							if(uploadDoc.getFileTypeValue().contains("Bill"))
							{
								hasError = true;
								eMsg.append("Please enter Bill details for uploaded documents </br>");
							}
						}
					}
					
					if(null == uploadDoc.getFileType() || ("").equals(uploadDoc.getFileType()))
					{
						hasError = true;
						eMsg.append("Please select file type for uploaded documents </br>");
					}
				}
			}
			else
			{
				hasError = true;
				eMsg.append("Please enter Bill details for uploaded documents </br>");
			}
		}
		
		
		if(null != patientCareAddOnBenefits && null != patientCareAddOnBenefits.getValue())
		{
			 Boolean  patientCareBenefits = (Boolean) patientCareAddOnBenefits.getValue();
			 if(null != patientCareBenefits && null != patientCareBenefits && patientCareBenefits)
			 {
				 if(null != this.patientCareTableObj)
					{
						Boolean isValid = patientCareTableObj.isValid();
						if (!isValid) {
							hasError = true;
							List<String> errors = this.patientCareTableObj.getErrors();
							for (String error : errors) {
								eMsg.append(error).append("</br>");
							}
						}
					}
			 }
		}
		if(null != bean.getDocumentDetails().getHospitalCash() && bean.getDocumentDetails().getHospitalCash() &&
					(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  bean.getClaimDTO().getNewIntimationDto().getPolicy()
					.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					/*|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)*/)){
			if(hospitalCashProductDetailsTable != null &&  null != hospitalCashProductDetailsTable.getValues()){
				List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
				Long totalNoOfDays =0l;
				Long perDayAmount =0l;
				Long noOfDaysAllowed =0l;
				for (HopsitalCashBenefitDTO hospitalCashBenefit1 : hospitalCashBenefit){
					if(hospitalCashBenefit1.getHospitalCashDays() != null && !hospitalCashBenefit1.getHospitalCashDays().equals("")){
						totalNoOfDays = totalNoOfDays + Long.valueOf(hospitalCashBenefit1.getHospitalCashDays());
					}
					if(hospitalCashBenefit1.getHospitalCashPerDayAmt() != null && !hospitalCashBenefit1.getHospitalCashPerDayAmt().equals("")){
						perDayAmount = perDayAmount + Long.valueOf(hospitalCashBenefit1.getHospitalCashPerDayAmt());
					}
					if(hospitalCashBenefit1.getNoOfDaysAllowed() != null && !hospitalCashBenefit1.getNoOfDaysAllowed().equals("")){
						noOfDaysAllowed = noOfDaysAllowed + Long.valueOf(hospitalCashBenefit1.getNoOfDaysAllowed());
					}
				}
				if(totalNoOfDays == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Claimed table Values.</br> ");
				}
				if(perDayAmount==0){
					hasError = true;
					eMsg.append("Please enter any one Per Day Claimed Amount table Values.</br> ");
				}
				if(noOfDaysAllowed == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Allowed table Values .</br>");
				}
			}

		}
		
		if(remarksBillEntry!=null && remarksBillEntry.getValue()==null){
			hasError = true;
			eMsg.append("Please enter Remarks(Bill Entry) .</br>");
		}
		if(!this.bean.getPreauthDTO().getIsBusinessProfileClicked()){
			hasError = true;
			eMsg.append("Please Verify View Mini Business Profile.</br>");
		}
		
		/*if(null != this.patientCareTableObj)
		{
			if(!(null != patientCarePerDayAmt && null != patientCarePerDayAmt.getValue() && !("").equalsIgnoreCase(patientCarePerDayAmt.getValue())))
			{
				hasError = true;
				eMsg += "Please enter patient care per day amount </br>";
			}
		}*/
		if(hasError)
		{
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			
			return !hasError;
			
		}
		else
		{
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
	public Boolean commitBinderOnNavigation()
	{
		try {
			this.binder.commit();
			return true;
		} catch (CommitException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * The below method will set the per day amount
	 * for hospital cash and patient care. This amount
	 * is returned by procedure.
	 * */

	public void setHospitalCashValues(List<Object> benefitList) {
		if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
		this.bean.getUploadDocumentsDTO().setHospitalCashPerDayAmt(String.valueOf(benefitList.get(3)));
		
	}

	public void setPatientCareValues(List<Object> benefitList) {
		if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
			 this.bean.getUploadDocumentsDTO().setPatientCarePerDayAmt(String.valueOf(benefitList.get(3)));
		
	}
	
	public List<String> getErrors() {
		return errorList;
	}
	
	public Boolean isValid() {
		Boolean isValid = true;
		StringBuffer eMsg = new StringBuffer();
		errorList.removeAll(errorList);
		try {
			if(this.binder.isValid()) {
				this.binder.commit();
				if(this.patientCareTableObj != null) {
					if(null != patientCareList && !patientCareList.isEmpty())
					{
						patientCareList.clear();
					}
					
					this.bean.getUploadDocumentsDTO().setPatientCareDTO(this.patientCareTableObj.getValues());
					patientCareList.addAll(this.patientCareTableObj.getValues());
				}
			} else {
				isValid = false;
				
				for (Field<?> field : this.binder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg.append(errMsg.getFormattedHtmlMessage());
					}
					errorList.add(eMsg.toString());
				}
			}
			
			
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return isValid; 
	}
	
	
	protected void addTotalClaimedListener()
	{
		this.uploadListenerTable.claimedAmtField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != uploadListenerTable)
				{
					String provisionAmt = (String)event.getProperty().getValue();
					if(null != provisionAmt && !("").equalsIgnoreCase(provisionAmt))
					{
						bean.setCurrentProvisionAmount(Double.valueOf(provisionAmt));
					}
				}
			}
			
			
		});
		
	}

	private Boolean enableUploadedDocsTableForBillEntry()
	{
		Boolean isEnable = true;
		/****
		 * 
		 * Fix for ticket ---> 1154.
		 * 
		 * Uploaded documents Listener table will be enabled only if the bill classification type is hospitalization, pre Hospitalization,
		 * post hospitalization , partial hospitalization and hospitalization repeat. If any of the above classification is enabled, then
		 * user is allowed to enter the bill details. If none of them are true and only benefits alone selected, then table will be disabled.
		 * As per discussion with BA team for add on benefits, though user is allowed to enter the document, the bill entry is not applicable. 
		 * Instead, for add on benefits only hospital cash and patient care alone is mandatory based on the benefits selected.
		 * 
		 * 
		 * 
		 * **/
		/**
		 *If reconsideration request is Yes, the below validation will be skipped. 
		 */
		if(null != this.bean.getDocumentDetails().getReconsiderationRequestValue() &&  ("No").equalsIgnoreCase(this.bean.getDocumentDetails().getReconsiderationRequestValue()))
		{
			if(!((null != bean.getDocumentDetails().getHospitalization() && bean.getDocumentDetails().getHospitalization()) ||
					(null != bean.getDocumentDetails().getPreHospitalization() && bean.getDocumentDetails().getPreHospitalization()) ||
					(null != bean.getDocumentDetails().getPostHospitalization() && bean.getDocumentDetails().getPostHospitalization()) ||
					(null != bean.getDocumentDetails().getPartialHospitalization() && bean.getDocumentDetails().getPartialHospitalization()) ||
					(null != bean.getDocumentDetails().getHospitalizationRepeat() && bean.getDocumentDetails().getHospitalizationRepeat()) ||
					(null != bean.getDocumentDetails().getLumpSumAmount() && bean.getDocumentDetails().getLumpSumAmount()) ||
					(null != bean.getDocumentDetails().getOtherBenefits() && bean.getDocumentDetails().getOtherBenefits()) ||
					(null != bean.getDocumentDetails().getHospitalCash() && bean.getDocumentDetails().getHospitalCash() && (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null 
					&&  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)))))
					
			{
				uploadListenerTable.setEnabled(false);
				isEnable  = false;
			}
			else
			{
				uploadListenerTable.setEnabled(true);
				isEnable = true;
				
			}
		}
		return isEnable;
		/*else if((null != bean.getDocumentDetails().getLumpSumAmount() && bean.getDocumentDetails().getLumpSumAmount()) ||
				(null != bean.getDocumentDetails().getAddOnBenefitsHospitalCash() && bean.getDocumentDetails().getAddOnBenefitsHospitalCash()) ||
				(null != bean.getDocumentDetails().getAddOnBenefitsPatientCare() && bean.getDocumentDetails().getAddOnBenefitsPatientCare()))
		{
			uploadListenerTable.setEnabled(false);
		}*/
	}

	public void enableOrDisableDtn(UploadDocumentDTO uploadDTO) {
		uploadListenerTable.generateColumn();
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		if(((null != bean.getDocumentDetails().getHospitalization() && bean.getDocumentDetails().getHospitalization())
				|| (null != bean.getDocumentDetails().getHospitalizationRepeat() && bean.getDocumentDetails().getHospitalizationRepeat())  || (null != bean.getDocumentDetails().getPartialHospitalization() && bean.getDocumentDetails().getPartialHospitalization())
				))
		{
			uploadDTO.setHospitalizationId(ReferenceTable.HOSPITALIZATION);
		}
		else
		{
			uploadDTO.setHospitalizationId(null);
			
		}
		 if( (null != bean.getDocumentDetails().getPreHospitalization() && bean.getDocumentDetails().getPreHospitalization()))
		{
			uploadDTO.setPreHospitalizationId(ReferenceTable.PRE_HOSPITALIZATION);
		}
		 else
		 {
			 uploadDTO.setPreHospitalizationId(null);
			
		 }
		 if((null != bean.getDocumentDetails().getPostHospitalization() && bean.getDocumentDetails().getPostHospitalization()))
		{
			uploadDTO.setPostHospitalizationId(ReferenceTable.POST_HOSPITALIZATION);
		}
		 else
		 {
				 uploadDTO.setPostHospitalizationId(null);
		 }
		 if((null != bean.getDocumentDetails().getLumpSumAmount() && bean.getDocumentDetails().getLumpSumAmount()))
		{
			uploadDTO.setLumpsumId(ReferenceTable.LUMPSUM);
		}
		 else
		 {
				 uploadDTO.setLumpsumId(null);
		 }
		 
		 if((null != bean.getDocumentDetails().getOtherBenefits() && bean.getDocumentDetails().getOtherBenefits()))
			{
				uploadDTO.setOtherBenefitsId(ReferenceTable.OTHER_BENEFIT);
			}
			 else
			 {
					 uploadDTO.setOtherBenefitsId(null);
					
				
			 }
		 if((null != bean.getDocumentDetails().getHospitalCash() && bean.getDocumentDetails().getHospitalCash()))
			{
				uploadDTO.setHospitalCashId(ReferenceTable.HOSPITAL_CASH_BILL_ID);
			}
			 else
			 {
					 uploadDTO.setHospitalCashId(null);
			 }
		
		uploadListenerTable.loadBillEntryValues(uploadDTO);
	}	
	
	public void updatePreviousClaimDetails(List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList){

			Window popup = new com.vaadin.ui.Window();
			updateOtherClaimDetailsUI.init(updateOtherClaimDetailList, referenceData, this.bean.getPreauthDTO(),popup);
			popup.setCaption("Update Previous/Other Claim Details(Defined Limit)");
			popup.setWidth("100%");
			popup.setHeight("70%");
			popup.setContent(updateOtherClaimDetailsUI);
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
	
	public void setClearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);
		SHAUtils.setClearReferenceData(referenceDataForPatientCare);
		if(mainLayout != null){
			mainLayout.removeAllComponents();
		}
	}
	
	//added for new produtc
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnHospitalCashProduct(Boolean value) {
		if(null != hospitalCashProductLayout && hospitalCashProductLayout.getComponentCount() > 0) {
//			unbindField(hospitalCashNoOfDays);
			unbindField(hospitalCashPerDayAmt);
			unbindField(hospitalCashTotalClaimedAmt);
			unbindField(treatmentPhysiotherapy);
			hospitalCashProductLayout.removeAllComponents();
		}
		if(value) {
//			Object addItem = table.addItem();
//			hospitalCashNoOfDays = (TextField) binder.buildAndBind(
//					"", "hospitalCashNoofDays", TextField.class);
			hospitalCashNoOfDays = new TextField();
			hospitalCashPerDayAmt =new TextField();
			hospitalCashTotalClaimedAmt = new TextField();
			CSValidator hospitalCashValidator = new CSValidator();
			hospitalCashValidator.extend(hospitalCashNoOfDays);
			hospitalCashValidator.setRegExp("^[0-9]*$");
			hospitalCashValidator.setPreventInvalidTyping(true);
			hospitalCashNoOfDays.setMaxLength(5);
			
			
			
			hospitalCashNoOfDays.addBlurListener(new BlurListener() {
				
				private static final long serialVersionUID = -7944733816900171622L;

				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromString(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});
			
//			hospitalCashPerDayAmt = (TextField) binder.buildAndBind(
//					"", "hospitalCashPerDayAmt", TextField.class);
			hospitalCashPerDayAmt.setMaxLength(10);
			
			CSValidator perDayAmtValidator = new CSValidator();
			perDayAmtValidator.extend(hospitalCashPerDayAmt);
			perDayAmtValidator.setRegExp("^[0-9 .]*$");
			perDayAmtValidator.setPreventInvalidTyping(true);
	
			mandatoryFields.add(hospitalCashPerDayAmt);
			setRequiredAndValidation(hospitalCashPerDayAmt);
			showOrHideValidation(false);

		/*	if(null != patientCarePerDayAmt)
			{
				mandatoryFields.remove(patientCarePerDayAmt);
			}
			*/
			hospitalCashPerDayAmt.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromString(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});
			
//			hospitalCashTotalClaimedAmt = (TextField) binder.buildAndBind(
//					"", "hospitalCashTotalClaimedAmt", TextField.class);
			hospitalCashTotalClaimedAmt.setEnabled(false);
			
			//hospitalCashBenefitsLayout = new VerticalLayout(table, treatmentPhysiotherapy);
			hospitalCashProductLayout.addComponent(hospitalCashProductDetailsTable);
		} 
	}
}
