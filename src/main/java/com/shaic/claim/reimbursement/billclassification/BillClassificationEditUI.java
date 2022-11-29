package com.shaic.claim.reimbursement.billclassification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.WithDrawPostProcessBillDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.withdrawPostProcessWizard.UpdateBillClassificationListenerTable;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;

public class BillClassificationEditUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308951356475854324L;

	private CheckBox chkhospitalization;

	private CheckBox chkPreHospitalization;

	private CheckBox chkPostHospitalization;

	private CheckBox chkPartialHospitalization;

	private CheckBox chkHospitalizationRepeat;

	private CheckBox chkLumpSumAmount;

	private CheckBox chkAddOnBenefitsHospitalCash;

	private CheckBox chkAddOnBenefitsPatientCare;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;

	private BeanFieldGroup<PreauthDTO> binder;

	private List<UploadDocumentDTO> uploadDocDTO;
	
	private UploadedDocumentsListenerTable uploadedDocListenerTableObj;
	
	@Inject
	private Instance<UpdateBillClassificationListenerTable> uploadWithDrawTableObjInst;
	private UpdateBillClassificationListenerTable uploadWithDrawTableObj;
	
	private Map<String, Object> reference;
	
	private PreauthDTO bean;
	
	private VerticalLayout otherBenefitsLayout;
	
	private Button okBtn;
	
	OptionGroup domicillaryHospitalisation;
	
	private String presenter;

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDTO>(PreauthDTO.class);
		this.binder.setItemDataSource(bean);
	}
	
	public void init(OptionGroup domicillaryHospitalisation) {
		this.domicillaryHospitalisation = domicillaryHospitalisation;
	}

	public void init(PreauthDTO bean) {
		this.bean = bean;	
		initBinder();
		otherBenefitsLayout = new VerticalLayout();	
		VerticalLayout verticalBillClassificationLayout = new VerticalLayout();
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayout();
		verticalBillClassificationLayout.addComponents(buildBillClassificationLayout,otherBenefitsLayout);
		
		addBillClassificationLister();
		
		if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag()))
		{
			chkOtherBenefits.setValue(true);
		}	
		chkEmergencyMedicalEvaluation.setEnabled(false);
		chkCompassionateTravel.setEnabled(false);
		chkRepatriationOfMortalRemains.setEnabled(false);
		chkPreferredNetworkHospital.setEnabled(false);
		chkSharedAccomodation.setEnabled(false);
		
		
		setCompositionRoot(verticalBillClassificationLayout);
	}

	public void initForEdit(PreauthDTO bean, List<UploadDocumentDTO> uploadDocDTO, UploadedDocumentsListenerTable uploadedDocListenerTable,Button okBtn) {
		this.bean = bean;
		this.uploadDocDTO = uploadDocDTO;
		this.uploadedDocListenerTableObj = uploadedDocListenerTable;
		this.okBtn = okBtn;
		initBinder();
		
		otherBenefitsLayout = new VerticalLayout();	
		VerticalLayout verticalBillClassificationLayout = new VerticalLayout();
		HorizontalLayout buildBillClassificationLayout = new HorizontalLayout();
		if(null != presenter && SHAConstants.WITHDRAW_CLASSIFICATION.equalsIgnoreCase(presenter)){
			 buildBillClassificationLayout = buildWithdrawBillClassificationLayoutForEdit();
		}
		else
		{
			buildBillClassificationLayout = buildBillClassificationLayoutForEdit();
		}
		verticalBillClassificationLayout.addComponents(buildBillClassificationLayout,otherBenefitsLayout);
		
		addBillClassificationLister();
		/**
		 * Added for ticket 5156.
		 * If domicillary is true, then pre and post shouldn't be editable.
		 * 
		 * */
		if(null != chkPreHospitalization && null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() && bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
		{
			chkPreHospitalization.setEnabled(false);
		}
		if(null != chkPostHospitalization && null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() && bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
		{
			chkPostHospitalization.setEnabled(false);
		}
		
		if(null != this.bean.getOtherBenefitsFlag() && (this.bean.getOtherBenefitsFlag() == true))
		{
			chkOtherBenefits.setValue(true);
		}
		
		//GLX2020017
		if(null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			|| null != bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			|| null != bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
		{
			if(chkOtherBenefits != null){
				chkOtherBenefits.setEnabled(false);
			}
			if(chkAddOnBenefitsHospitalCash !=null){
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
		}
		
		SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
		if((sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null && sectionDetailsDTO.getCover().getCommonValue() != null && sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE))){
			chkPreHospitalization.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
		}
		
		
		
		setCompositionRoot(verticalBillClassificationLayout);
	}

	private HorizontalLayout buildBillClassificationLayout() {
		chkhospitalization = binder.buildAndBind("Hospitalisation",
				"hospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		chkhospitalization.setEnabled(false);

		chkPartialHospitalization = binder.buildAndBind(
				"Partial-Hospitalisation", "partialHospitalizaionFlag",
				CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		chkPartialHospitalization.setEnabled(false);

		chkHospitalizationRepeat = binder.buildAndBind(
				"Hospitalisation (Repeat)", "isHospitalizationRepeat",
				CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
		chkHospitalizationRepeat.setEnabled(false);

		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation",
				"preHospitalizaionFlag", CheckBox.class);

		if (!this.bean.getIsPreHospApplicable()) {
//			chkPreHospitalization.setEnabled(false);
		}
		chkPreHospitalization.setEnabled(false);

		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation",
				"postHospitalizaionFlag", CheckBox.class);

		if (!this.bean.getIsPostHospApplicable()) {
//			chkPostHospitalization.setEnabled(false);
		}
		chkPostHospitalization.setEnabled(false);

		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount",
				"lumpSumAmountFlag", CheckBox.class);
		if (!bean.getIsLumpsumApplicable()) {
			chkLumpSumAmount.setEnabled(false);
		}
		chkLumpSumAmount.setEnabled(false);

		chkAddOnBenefitsHospitalCash = binder.buildAndBind(
				"Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash",
				CheckBox.class);

		if (!this.bean.getIsHospitalCashApplicable()) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		chkAddOnBenefitsHospitalCash.setEnabled(false);

		chkAddOnBenefitsPatientCare = binder.buildAndBind(
				"Add on Benefits (Patient Care)", "addOnBenefitsPatientCare",
				CheckBox.class);
		
		chkOtherBenefits = binder.buildAndBind(
				"Other Benefits", "otherBenefitsFlag",
				CheckBox.class);
		
		chkOtherBenefits.setValue(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
		
		if (!this.bean.getIsPatientCareApplicable()) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		chkAddOnBenefitsPatientCare.setEnabled(false);

		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,
				chkHospitalizationRepeat);
		// classificationLayout1.setMargin(true);
		// classificationLayout1.setWidth("20%");
		FormLayout classificationLayout2 = new FormLayout(
				chkPreHospitalization, chkLumpSumAmount);
		// classificationLayout2.setMargin(true);
		// classificationLayout2.setWidth("20%");
		FormLayout classificationLayout3 = new FormLayout(
				chkPostHospitalization, chkAddOnBenefitsHospitalCash);
		// classificationLayout3.setMargin(true);
		// classificationLayout3.setWidth("40%");
		FormLayout classificationLayout4 = new FormLayout(
				chkPartialHospitalization, chkAddOnBenefitsPatientCare);
		// classificationLayout4.setMargin(true);
		// classificationLayout4.setWidth("40%");
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits);

		/*
		 * FormLayout classificationLayout1 = new
		 * FormLayout(chkhospitalization,chkLumpSumAmount); FormLayout
		 * classificationLayout2 = new
		 * FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		 * FormLayout classificationLayout3 = new
		 * FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		 * FormLayout classificationLayout4 = new
		 * FormLayout(chkPartialHospitalization);
		 */

		HorizontalLayout billClassificationLayout = new HorizontalLayout(
				classificationLayout1, classificationLayout2,
				classificationLayout3, classificationLayout4,classificationLayout5);
		// billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		// billClassificationLayout.setMargin(true);
		// billClassificationLayout.setWidth("110%");
		// addBillClassificationLister();

		return billClassificationLayout;
	}

	private HorizontalLayout buildBillClassificationLayoutForEdit() {
		chkhospitalization = binder.buildAndBind("Hospitalisation",
				"hospitalizaionFlag", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		chkhospitalization.setEnabled(false);

		chkPartialHospitalization = binder.buildAndBind(
				"Partial-Hospitalisation", "partialHospitalizaionFlag",
				CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		chkPartialHospitalization.setEnabled(false);

		chkHospitalizationRepeat = binder.buildAndBind(
				"Hospitalisation (Repeat)", "isHospitalizationRepeat",
				CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
		chkHospitalizationRepeat.setEnabled(false);

		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation",
				"preHospitalizaionFlag", CheckBox.class);

		if (!this.bean.getIsPreHospApplicable()) {
//			chkPreHospitalization.setEnabled(false);
		}

		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation",
				"postHospitalizaionFlag", CheckBox.class);

		if (!this.bean.getIsPostHospApplicable()) {
//			chkPostHospitalization.setEnabled(false);
		}

		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount",
				"lumpSumAmountFlag", CheckBox.class);
		if (!bean.getIsLumpsumApplicable()) {
			chkLumpSumAmount.setEnabled(false);
		} else if(!bean.getLumpSumAmountFlag()) {
			chkLumpSumAmount.setEnabled(false);
		}

		chkAddOnBenefitsHospitalCash = binder.buildAndBind(
				"Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash",
				CheckBox.class);

		if (!this.bean.getIsHospitalCashApplicable()) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}

		chkAddOnBenefitsPatientCare = binder.buildAndBind(
				"Add on Benefits (Patient Care)", "addOnBenefitsPatientCare",
				CheckBox.class);

		
		chkOtherBenefits = binder.buildAndBind(
				"Other Benefits", "otherBenefitsFlag",
				CheckBox.class);
		chkOtherBenefits.setValue(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
		
		if (!this.bean.getIsPatientCareApplicable()) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		
		//IMSSUPPOR-28847
		if(!this.bean.getIsOthrBenefitApplicable())
		{
			chkOtherBenefits.setEnabled(false);
		}


		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,
				chkHospitalizationRepeat);
		// classificationLayout1.setMargin(true);
		// classificationLayout1.setWidth("20%");
		FormLayout classificationLayout2 = new FormLayout(
				chkPreHospitalization, chkLumpSumAmount);
		// classificationLayout2.setMargin(true);
		// classificationLayout2.setWidth("20%");
		FormLayout classificationLayout3 = new FormLayout(
				chkPostHospitalization, chkAddOnBenefitsHospitalCash);
		// classificationLayout3.setMargin(true);
		// classificationLayout3.setWidth("40%");
		FormLayout classificationLayout4 = new FormLayout(
				chkPartialHospitalization, chkAddOnBenefitsPatientCare);
		
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits);
		// classificationLayout4.setMargin(true);
		// classificationLayout4.setWidth("40%");

		/*
		 * FormLayout classificationLayout1 = new
		 * FormLayout(chkhospitalization,chkLumpSumAmount); FormLayout
		 * classificationLayout2 = new
		 * FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		 * FormLayout classificationLayout3 = new
		 * FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		 * FormLayout classificationLayout4 = new
		 * FormLayout(chkPartialHospitalization);
		 */

		HorizontalLayout billClassificationLayout = new HorizontalLayout(
				classificationLayout1, classificationLayout2,
				classificationLayout3, classificationLayout4,classificationLayout5);
		// billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		// billClassificationLayout.setMargin(true);
		// billClassificationLayout.setWidth("110%");
		// addBillClassificationLister();

		return billClassificationLayout;
	}
	

	public Boolean validateBillClassification() {
		Boolean isError = false;
/*		if (false) {
			if (null != bean.getDocumentDetailsDTOList()
					&& !bean.getDocumentDetailsDTOList().isEmpty()) {
				for (DocumentDetailsDTO documentDetailsDTO : bean
						.getDocumentDetailsDTOList()) {
					if (null != this.chkhospitalization
							&& null != this.chkhospitalization.getValue()
							&& this.chkhospitalization.getValue()) {
						if (("Y").equalsIgnoreCase(documentDetailsDTO
								.getHospitalizationFlag())
								&& !(ReferenceTable.CANCEL_ROD_KEYS)
										.containsKey(documentDetailsDTO
												.getStatusId())) {
							isError = true;
						}
					}
					if (null != this.chkPartialHospitalization
							&& null != this.chkPartialHospitalization
									.getValue()
							&& this.chkPartialHospitalization.getValue()) {
						if (("Y").equalsIgnoreCase(documentDetailsDTO
								.getPartialHospitalizationFlag())
								&& !(ReferenceTable.CANCEL_ROD_KEYS)
										.containsKey(documentDetailsDTO
												.getStatusId())) {
							isError = true;
						}
					}

					*//**
					 * Below validation is added for cancel rod scenario. If an
					 * hospitalization rod is cancelled and user tries to
					 * deselect hospitalization and select hospitalization
					 * repeat, then below validation will not allow user to
					 * create an hospitalization repeat rod, since
					 * hospitalization rod is not yet created. -- Added for
					 * #3768
					 *//*
					if (null != this.chkHospitalizationRepeat
							&& null != this.chkHospitalizationRepeat.getValue()
							&& null != this.chkHospitalizationRepeat.getValue()) {
						if (("Y").equalsIgnoreCase(documentDetailsDTO
								.getHospitalizationFlag())
								&& (ReferenceTable.CANCEL_ROD_KEYS)
										.containsKey(documentDetailsDTO
												.getStatusId())) {
							isError = true;
						}
					}

				}
			} else {
				if (null != this.chkhospitalization
						&& null != this.chkhospitalization.getValue()
						&& this.chkhospitalization.getValue()) {
					isError = false;
				} else if (null != this.chkPartialHospitalization
						&& null != this.chkPartialHospitalization.getValue()
						&& this.chkPartialHospitalization.getValue()) {
					isError = false;
				} else if (null != this.chkhospitalization
						&& null != this.chkhospitalization.getValue()
						&& !this.chkhospitalization.getValue()) {
					if ((SHAConstants.CLAIMREQUEST_CASHLESS)
							.equalsIgnoreCase(this.bean.getClaimDTO()
									.getClaimTypeValue())
							&& bean.getIsFinalEnhancement()) {
						isError = false;
					} else {
						if (null != this.chkPreHospitalization
								&& null != this.chkPreHospitalization
										.getValue()
								&& this.chkPreHospitalization.getValue()) {
							isError = true;
						}
						if (null != this.chkPostHospitalization
								&& null != this.chkPostHospitalization
										.getValue()
								&& this.chkPostHospitalization.getValue()) {
							isError = true;
						}
						if (null != this.chkLumpSumAmount
								&& null != this.chkLumpSumAmount.getValue()
								&& this.chkLumpSumAmount.getValue()) {
							isError = true;
						}
						if (null != this.chkAddOnBenefitsHospitalCash
								&& null != this.chkAddOnBenefitsHospitalCash
										.getValue()
								&& this.chkAddOnBenefitsHospitalCash.getValue()) {
							isError = true;
						}
						if (null != this.chkAddOnBenefitsPatientCare
								&& null != this.chkAddOnBenefitsPatientCare
										.getValue()
								&& this.chkAddOnBenefitsPatientCare.getValue()) {
							isError = true;
						}
						if (null != this.chkHospitalizationRepeat
								&& null != this.chkHospitalizationRepeat
										.getValue()
								&& this.chkHospitalizationRepeat.getValue()) {
							isError = true;
						}
					}
				} else if (null != this.chkPartialHospitalization
						&& null != this.chkPartialHospitalization.getValue()
						&& !this.chkPartialHospitalization.getValue()) {
					if ((SHAConstants.CLAIMREQUEST_CASHLESS)
							.equalsIgnoreCase(this.bean.getClaimDTO()
									.getClaimTypeValue())
							&& bean.getIsFinalEnhancement()) {
						isError = false;
					} else {
						if (null != this.chkPreHospitalization
								&& null != this.chkPreHospitalization
										.getValue()
								&& this.chkPreHospitalization.getValue()) {
							isError = true;
						}
						if (null != this.chkPostHospitalization
								&& null != this.chkPostHospitalization
										.getValue()
								&& this.chkPostHospitalization.getValue()) {
							isError = true;
						}
						if (null != this.chkLumpSumAmount
								&& null != this.chkLumpSumAmount.getValue()
								&& this.chkLumpSumAmount.getValue()) {
							isError = true;
						}
						if (null != this.chkAddOnBenefitsHospitalCash
								&& null != this.chkAddOnBenefitsHospitalCash
										.getValue()
								&& this.chkAddOnBenefitsHospitalCash.getValue()) {
							isError = true;
						}
						if (null != this.chkAddOnBenefitsPatientCare
								&& null != this.chkAddOnBenefitsPatientCare
										.getValue()
								&& this.chkAddOnBenefitsPatientCare.getValue()) {
							isError = true;
						}
					}
				} else if ((SHAConstants.CLAIMREQUEST_CASHLESS)
						.equalsIgnoreCase(this.bean.getClaimDTO()
								.getClaimTypeValue())
						&& bean.getIsFinalEnhancement()) {
					isError = false;
				} else {
					isError = true;
				}
			}
		}*/
		return isError;

	}

	private void addBillClassificationLister() {
		chkhospitalization
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {
								if (validateBillClassification()) {
									/*Label label = new Label(
											"Already hospitalization is existing for this claim.",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("35%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
									okBtn.setEnabled(false);
									chkhospitalization.setValue(false);
								} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
									showConfirmationPopupHospitalisationWithDrawCashless(chkhospitalization);
								}
							} else {
								if (validateBillClassification()) {
									// Label label = new
									// Label("Pre or Post hospitalization cannot exist without hospitalization",
									// ContentMode.HTML);
									/*Label label = new Label(
											"None of the bill classification can exist without hospitalization",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("35%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("None of the bill classification can exist without hospitalization", buttonsNamewithType);
									okBtn.setEnabled(false);
									if (null != chkPreHospitalization) {
										chkPreHospitalization.setValue(false);
									}
									if (null != chkPostHospitalization) {
										chkPostHospitalization.setValue(false);
									}
									if (null != chkLumpSumAmount) {
										chkLumpSumAmount.setValue(false);
									}
									if (null != chkAddOnBenefitsHospitalCash) {
										chkAddOnBenefitsHospitalCash
												.setValue(false);
									}
									if (null != chkAddOnBenefitsPatientCare) {
										chkAddOnBenefitsPatientCare
												.setValue(false);
									}
									if (null != chkHospitalizationRepeat) {
										chkHospitalizationRepeat
												.setValue(false);
									}
								}
							}
						}
					}
				});

		chkPreHospitalization
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
									if(!bean.getPreHospitalizaionFlag() == value) {
										if (value) {

											if (validateBillClassification()) {
												/*Label label = new Label(
														"Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization",
														ContentMode.HTML);
												label.setStyleName("errMessage");
												HorizontalLayout layout = new HorizontalLayout(
														label);

												layout.setMargin(true);
												final ConfirmDialog dialog = new ConfirmDialog();
												dialog.setCaption("Errors");
												// dialog.setWidth("35%");
												dialog.setClosable(true);
												dialog.setContent(layout);
												dialog.setResizable(false);
												dialog.setModal(true);
												dialog.show(getUI().getCurrent(), null,
														true);*/
												HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
												buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
												GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
												okBtn.setEnabled(false);
												chkPreHospitalization.setValue(false);
											} else {
												if(bean.getIsHospitalizationRepeat()) {
													/*Label label = new Label(
															"Pre hosptilization cannot be selected with Hospitalization Repeat.",
															ContentMode.HTML);
													label.setStyleName("errMessage");
													HorizontalLayout layout = new HorizontalLayout(
															label);

													layout.setMargin(true);
													final ConfirmDialog dialog = new ConfirmDialog();
													dialog.setCaption("Errors");
													// dialog.setWidth("35%");
													dialog.setClosable(true);
													dialog.setContent(layout);
													dialog.setResizable(false);
													dialog.setModal(true);
													dialog.show(getUI().getCurrent(), null,
															true);*/
													HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
													buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
													GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected with Hospitalization Repeat.", buttonsNamewithType);
													okBtn.setEnabled(false);
													chkPreHospitalization.setValue(false);
												} else {
													showConfirmationPopup(chkPreHospitalization, true, false);
												}
												
											}
										} else {
											if(!(bean.getPreHospitalizaionFlag() == value)) {
												showConfirmationPopup(chkPreHospitalization, true, false);
											}
											
										}
									}
							
						}
					}
				});

		chkPostHospitalization
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if(!bean.getPostHospitalizaionFlag() == value) {
								if (value) {
									if (validateBillClassification()) {
										/*Label label = new Label(
												"Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization",
												ContentMode.HTML);
										label.setStyleName("errMessage");
										HorizontalLayout layout = new HorizontalLayout(
												label);
										layout.setMargin(true);
										final ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										// dialog.setWidth("35%");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(false);
										dialog.setModal(true);
										dialog.show(getUI().getCurrent(), null,
												true);*/
										HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
										buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
										GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
										okBtn.setEnabled(false);
										chkPostHospitalization.setValue(false);
									} else {
										if(bean.getIsHospitalizationRepeat()) {
											/*Label label = new Label(
													"Post hosptilization cannot be selected with Hospitalization Repeat.",
													ContentMode.HTML);
											label.setStyleName("errMessage");
											HorizontalLayout layout = new HorizontalLayout(
													label);

											layout.setMargin(true);
											final ConfirmDialog dialog = new ConfirmDialog();
											dialog.setCaption("Errors");
											// dialog.setWidth("35%");
											dialog.setClosable(true);
											dialog.setContent(layout);
											dialog.setResizable(false);
											dialog.setModal(true);
											dialog.show(getUI().getCurrent(), null,
													true);*/
											HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
											buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
											GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected with Hospitalization Repeat.", buttonsNamewithType);
											okBtn.setEnabled(false);
											chkPreHospitalization.setValue(false);
										} else {
											showConfirmationPopup(chkPostHospitalization, false, true);
										}
										
									}
								} else {
									if(!(bean.getPostHospitalizaionFlag() == value)) {
										showConfirmationPopup(chkPostHospitalization, false, true);
									}
									
								}
							}
						
						}
					}
				});

		chkLumpSumAmount
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {

								if (validateBillClassification()) {
									/*Label label = new Label(
											"Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("50%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									okBtn.setEnabled(false);
									chkLumpSumAmount.setValue(false);
								}
							}
						}
					}
				});

		chkAddOnBenefitsHospitalCash
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {

								if (validateBillClassification()) {
									/*Label label = new Label(
											"Hospital cash cannot be selected without selecting hospitalization or partial hosptilization",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("50%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									okBtn.setEnabled(false);
									chkAddOnBenefitsHospitalCash
											.setValue(false);
								} else {
									okBtn.setEnabled(true);
								}
							} else {
								okBtn.setEnabled(true);
							}
						}
					}
				});

		chkAddOnBenefitsPatientCare
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {

								if (validateBillClassification()) {
									/*Label label = new Label(
											"Patient care cannot be selected without selecting hospitalization or partial hosptilization",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("50%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Patient care cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									okBtn.setEnabled(false);
									chkAddOnBenefitsPatientCare.setValue(false);
								} else {
									okBtn.setEnabled(true);
								}
							} else {
								okBtn.setEnabled(true);
							}
						}
					}
				});

		chkPartialHospitalization
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {

								if (validateBillClassification()) {
									/*Label label = new Label(
											"Already partial hospitalization is existing for this claim.",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);

									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("35%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Already partial hospitalization is existing for this claim.", buttonsNamewithType);
									okBtn.setEnabled(false);
									// chkPartialHospitalization.setValue(false);
									chkPartialHospitalization.setValue(null);
								}

							} else {

								if (validateBillClassification()) {
									/*Label label = new Label(
											"Pre or Post hospitalization cannot exist without Partial hospitalization",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("35%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Pre or Post hospitalization cannot exist without Partial hospitalization", buttonsNamewithType);
									okBtn.setEnabled(false);
									if (null != chkPreHospitalization) {
										chkPreHospitalization.setValue(false);
									}
									if (null != chkPostHospitalization) {
										chkPostHospitalization.setValue(false);
									}
									// chkhospitalization.setValue(false);
									okBtn.setEnabled(false);
								}
							}
						}
					}
				});

		chkHospitalizationRepeat
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();
							if (value) {
								// chkPostHospitalization.setEnabled(true);
								// chkPreHospitalization.setEnabled(true);

								/*
								 * if(!((null != chkhospitalization && null !=
								 * chkhospitalization.getValue() &&
								 * chkhospitalization.getValue()) || (null !=
								 * chkPreHospitalization && null !=
								 * chkPreHospitalization.getValue() &&
								 * chkPreHospitalization.getValue()) || (null !=
								 * chkPostHospitalization && null !=
								 * chkPostHospitalization.getValue() &&
								 * chkPostHospitalization.getValue()) || (null
								 * != chkLumpSumAmount && null !=
								 * chkLumpSumAmount.getValue() &&
								 * chkLumpSumAmount.getValue()) || (null !=
								 * chkAddOnBenefitsHospitalCash && null !=
								 * chkAddOnBenefitsHospitalCash.getValue() &&
								 * chkAddOnBenefitsHospitalCash.getValue()) ||
								 * (null != chkAddOnBenefitsPatientCare && null
								 * != chkAddOnBenefitsPatientCare.getValue() &&
								 * chkAddOnBenefitsPatientCare.getValue())) )
								 */
								if (!((null != chkhospitalization
										&& null != chkhospitalization
												.getValue() && chkhospitalization
											.getValue())
										|| (null != chkPreHospitalization
												&& null != chkPreHospitalization
														.getValue() && chkPreHospitalization
													.getValue())
										|| (null != chkPostHospitalization
												&& null != chkPostHospitalization
														.getValue() && chkPostHospitalization
													.getValue())
										|| (null != chkLumpSumAmount
												&& null != chkLumpSumAmount
														.getValue() && chkLumpSumAmount
													.getValue())
										|| (null != chkAddOnBenefitsHospitalCash
												&& null != chkAddOnBenefitsHospitalCash
														.getValue() && chkAddOnBenefitsHospitalCash
													.getValue())
										|| (null != chkAddOnBenefitsPatientCare
												&& null != chkAddOnBenefitsPatientCare
														.getValue() && chkAddOnBenefitsPatientCare
													.getValue()) || (null != chkPartialHospitalization
										&& null != chkPartialHospitalization
												.getValue() && chkPartialHospitalization
										.getValue()))) {
									if (validateBillClassification()) {
										/*Label label = new Label(
												"Hospitalization Repeat cannot exist without hospitalization",
												ContentMode.HTML);
										label.setStyleName("errMessage");
										HorizontalLayout layout = new HorizontalLayout(
												label);
										layout.setMargin(true);
										final ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										// dialog.setWidth("40%");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(false);
										dialog.setModal(true);
										dialog.show(getUI().getCurrent(), null,
												true);*/
										HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
										buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
										GalaxyAlertBox.createErrorBox("Hospitalization Repeat cannot exist without hospitalization", buttonsNamewithType);
										okBtn.setEnabled(false);
										// chkHospitalizationRepeat.setValue(false);
										chkHospitalizationRepeat.setValue(null);
									} else {
										chkhospitalization.setEnabled(false);
										// if(null != chkPreHospitalization &&
										// chkPreHospitalization.isEnabled())
										chkPreHospitalization.setEnabled(false);
										// if(null != chkPostHospitalization &&
										// chkPostHospitalization.isEnabled())
										chkPostHospitalization
												.setEnabled(false);
										// if(null != chkPartialHospitalization
										// &&
										// chkPartialHospitalization.isEnabled())
										chkPartialHospitalization
												.setEnabled(false);
										// if(null != chkLumpSumAmount &&
										// chkLumpSumAmount.isEnabled())
										chkLumpSumAmount.setEnabled(false);
										// if(null !=
										// chkAddOnBenefitsHospitalCash &&
										// chkAddOnBenefitsHospitalCash.isEnabled())
										chkAddOnBenefitsHospitalCash
												.setEnabled(false);
										// if(null !=
										// chkAddOnBenefitsPatientCare &&
										// chkAddOnBenefitsPatientCare.isEnabled())
										chkAddOnBenefitsPatientCare
												.setEnabled(false);
									}
								} else {
									/*Label label = new Label(
											"None of the classification details can be selected along with hospitalization repeat",
											ContentMode.HTML);
									label.setStyleName("errMessage");
									HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									// dialog.setWidth("55%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null,
											true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("None of the classification details can be selected along with hospitalization repeat", buttonsNamewithType);
									okBtn.setEnabled(false);
									chkHospitalizationRepeat.setValue(false);
									if (bean.getPreauthDataExtractionDetails()
											.getDocAckknowledgement() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId()
													.getKey()
													.equals(ReferenceTable.RECEIVED_FROM_INSURED)
											&& null != chkPreHospitalization
											&& null != chkPreHospitalization
													.getValue()
											&& chkPreHospitalization.getValue()) {
										chkPreHospitalization.setEnabled(true);
									}
									if (bean.getPreauthDataExtractionDetails()
											.getDocAckknowledgement() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId()
													.getKey()
													.equals(ReferenceTable.RECEIVED_FROM_INSURED)
											&& null != chkPostHospitalization
											&& null != chkPostHospitalization
													.getValue()
											&& chkPostHospitalization
													.getValue()) {
										chkPostHospitalization.setEnabled(true);
									}
								}
							} else {
								if (null != chkhospitalization
										&& ((("Cashless").equalsIgnoreCase(bean
												.getClaimDTO()
												.getClaimTypeValue())
												&& bean.getPreauthDataExtractionDetails()
														.getDocAckknowledgement() != null
												&& bean.getPreauthDataExtractionDetails()
														.getDocAckknowledgement()
														.getDocumentReceivedFromId() != null && bean
												.getPreauthDataExtractionDetails()
												.getDocAckknowledgement()
												.getDocumentReceivedFromId()
												.getKey()
												.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) || ("Reimbursement")
													.equalsIgnoreCase(bean
															.getClaimDTO()
															.getClaimTypeValue())))
									if (null != chkhospitalization
											&& !chkhospitalization.isEnabled())
										chkhospitalization.setEnabled(true);
								// if(null != chkPreHospitalization &&
								// !chkPreHospitalization.isEnabled())
								if (null != chkPreHospitalization
										&& bean.getIsPreHospApplicable()) {
									if (bean.getPreauthDataExtractionDetails()
											.getDocAckknowledgement() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId()
													.getKey()
													.equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
										chkPreHospitalization.setEnabled(false);
										chkPreHospitalization.setValue(true);
									} else {
										chkPreHospitalization.setEnabled(true);
									}

								}
								// if(null != chkPostHospitalization &&
								// !chkPostHospitalization.isEnabled())
								if (null != chkPostHospitalization
										&& bean.getIsPostHospApplicable()) {
									if (bean.getPreauthDataExtractionDetails()
											.getDocAckknowledgement() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId() != null
											&& bean.getPreauthDataExtractionDetails()
													.getDocAckknowledgement()
													.getDocumentReceivedFromId()
													.getKey()
													.equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
										chkPostHospitalization
												.setEnabled(false);
										chkPostHospitalization.setValue(true);
									} else {
										chkPostHospitalization.setEnabled(true);
									}
								}
								if (null != chkPartialHospitalization
										&& ((("Cashless").equalsIgnoreCase(bean
												.getClaimDTO()
												.getClaimTypeValue())
												&& bean.getPreauthDataExtractionDetails()
														.getDocAckknowledgement() != null
												&& bean.getPreauthDataExtractionDetails()
														.getDocAckknowledgement()
														.getDocumentReceivedFromId() != null && bean
												.getPreauthDataExtractionDetails()
												.getDocAckknowledgement()
												.getDocumentReceivedFromId()
												.getKey()
												.equals(ReferenceTable.RECEIVED_FROM_INSURED)) // ||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
										))
									// if(null != chkPartialHospitalization &&
									// chkPartialHospitalization.isEnabled())
									chkPartialHospitalization.setEnabled(true);
								if (bean.getIsLumpsumApplicable()) {
									chkLumpSumAmount.setEnabled(true);
								}
								if (bean.getIsHospitalCashApplicable()) {
									chkAddOnBenefitsHospitalCash
											.setEnabled(true);
								}
								if (bean.getIsPatientCareApplicable()) {
									chkAddOnBenefitsPatientCare
											.setEnabled(true);
								}
							}
						}
					}
				});
		
		
		chkOtherBenefits .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				
										 
				 buildOtherBenefitsLayout(value);	
				 if(value)
				 {
					 okBtn.setEnabled(true);
				 }				
					 //okBtn.setEnabled(false);
					 if(!(bean.getOtherBenefitsFlag() == value)) {
							showConfirmationPopupForOtherBenefits(chkOtherBenefits, true);
						}
				 
				 
				 }							
			}
			
		});
	}
	
	
	protected void showPopup(String message) {
		/*Label label = new Label(
				message,
				ContentMode.HTML);
		label.setStyleName("errMessage");
		HorizontalLayout layout = new HorizontalLayout(
				label);
		layout.setMargin(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		// dialog.setWidth("40%");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null,
				true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(message, buttonsNamewithType);
	}
	
	protected void showConfirmationPopup(final CheckBox selectedCheckBox, final Boolean isPrehosp, final Boolean isPostHosp) {
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure Do you want to change Bill Classification ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								okBtn.setEnabled(true);
								if (!dialog.isConfirmed()) {
									dialog.close();
									Boolean value = selectedCheckBox.getValue();
									if(isPrehosp) {
										if((!bean.getPostHospitalizaionFlag() && (chkPostHospitalization != null && chkPostHospitalization.getValue() != null && !chkPostHospitalization.getValue())) &&
												(presenter == null && !presenter.equalsIgnoreCase("withdraw_classification") ? checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION) : checkSelectedClassifationAvailableOrNotForWithDraw(ReferenceTable.PRE_HOSPITALIZATION))) {
											showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please choose Post Hospitalization classifcation before making change.");
											okBtn.setEnabled(false);
											selectedCheckBox.setValue(!selectedCheckBox.getValue());
										} else {
											Boolean checkSelectedClassifationAvailableOrNot = false;
											if(presenter == null ){
												checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION);
											} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
												checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNotForWithDraw(ReferenceTable.PRE_HOSPITALIZATION);
											}
											
											if(value) {
												if(!checkSelectedClassifationAvailableOrNot) {
//													selectedCheckBox.setValue(!value);
													if(presenter == null ) {
														uploadedDocListenerTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
														uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
														uploadWithDrawTableObj.init();
														uploadWithDrawTableObj.setReferenceData(reference);
														uploadWithDrawTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													}
//													showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
													okBtn.setEnabled(true);
												}
											} else {
												if(checkSelectedClassifationAvailableOrNot) {
													selectedCheckBox.setValue(!value);
													showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Pre Hospitalization before making change.");
													okBtn.setEnabled(false);
												} else {
													if(presenter == null ) {
														uploadedDocListenerTableObj.setBillClassificationsDynamically(false, ( ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
														uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
														uploadWithDrawTableObj.init();
														uploadWithDrawTableObj.setReferenceData(reference);
														uploadWithDrawTableObj.setBillClassificationsDynamically(false, ( ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													} 
													
												}
											}
										}
									} else if(isPostHosp) {
										
										
										if((!bean.getPreHospitalizaionFlag() && (chkPreHospitalization != null && chkPreHospitalization.getValue() != null && !chkPreHospitalization.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION)) {
											showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please choose Pre Hospitalization classifcation before making change.");
											okBtn.setEnabled(false);
											selectedCheckBox.setValue(!selectedCheckBox.getValue());
										} else {
											Boolean checkSelectedClassifationAvailableOrNot = false;
											if(presenter == null) {
												checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION);
											} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
												checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNotForWithDraw(ReferenceTable.POST_HOSPITALIZATION);
											}
											if(value) {
												if(!checkSelectedClassifationAvailableOrNot) {
//													selectedCheckBox.setValue(!value);
													if(presenter == null) {
														uploadedDocListenerTableObj.setBillClassificationsDynamically((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), true, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
														uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
														uploadWithDrawTableObj.init();
														uploadWithDrawTableObj.setReferenceData(reference);
														uploadWithDrawTableObj.setBillClassificationsDynamically((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), true, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													}
//													showPopup("Post Hospitalization is not selected as classification in Bill Entry popup. Please choose Post Hospitalization before making change.");
													okBtn.setEnabled(true);
												}
											} else {
												if(checkSelectedClassifationAvailableOrNot) {
													selectedCheckBox.setValue(!value);
													showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Post Hospitalization before making change.");
													okBtn.setEnabled(false);
												} else {
													if(presenter == null){
														uploadedDocListenerTableObj.setBillClassificationsDynamically((((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), false, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
														uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
														uploadWithDrawTableObj.init();
														uploadWithDrawTableObj.setReferenceData(reference);
														uploadWithDrawTableObj.setBillClassificationsDynamically((((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), false, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
													}
												}
											}
										}
										
										
									}
									
									if(null != chkhospitalization && null != chkhospitalization.getValue() && null !=chkPreHospitalization && !chkPreHospitalization.getValue() &&
											null != chkPostHospitalization && !chkPostHospitalization.getValue()){
										
										if(null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() && !bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation()){
											domicillaryHospitalisation.setEnabled(true);
										}
										else
										{
											domicillaryHospitalisation.setEnabled(true);
										}
									}
									else
									{
										if(presenter == null){
											domicillaryHospitalisation.setEnabled(false);
										}
									}
								} else {
									dialog.close();
									selectedCheckBox.setValue(!selectedCheckBox.getValue());
								}
							}

							
						});
		dialog.setClosable(false);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure Do you want to change Bill Classification ?", buttonsNamewithType);
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				okBtn.setEnabled(true);
				//dialog.close();
				Boolean value = selectedCheckBox.getValue();
				if(isPrehosp) {
					if((!bean.getPostHospitalizaionFlag() && (chkPostHospitalization != null && chkPostHospitalization.getValue() != null && !chkPostHospitalization.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION)) {
						showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please choose Post Hospitalization classifcation before making change.");
						okBtn.setEnabled(false);
						selectedCheckBox.setValue(!selectedCheckBox.getValue());
					} else {
						Boolean checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION);
						if(value) {
							if(!checkSelectedClassifationAvailableOrNot) {
//								selectedCheckBox.setValue(!value);
								uploadedDocListenerTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
//								showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
								okBtn.setEnabled(true);
							}
						} else {
							if(checkSelectedClassifationAvailableOrNot) {
								selectedCheckBox.setValue(!value);
								showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Pre Hospitalization before making change.");
								okBtn.setEnabled(false);
							} else {
								uploadedDocListenerTableObj.setBillClassificationsDynamically(false, ( ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
								
							}
						}
					}
				} else if(isPostHosp) {
					
					
					if((!bean.getPreHospitalizaionFlag() && (chkPreHospitalization != null && chkPreHospitalization.getValue() != null && !chkPreHospitalization.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION)) {
						showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please choose Pre Hospitalization classifcation before making change.");
						okBtn.setEnabled(false);
						selectedCheckBox.setValue(!selectedCheckBox.getValue());
					} else {
						Boolean checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION);
						if(value) {
							if(!checkSelectedClassifationAvailableOrNot) {
//								selectedCheckBox.setValue(!value);
								uploadedDocListenerTableObj.setBillClassificationsDynamically((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), true, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
//								showPopup("Post Hospitalization is not selected as classification in Bill Entry popup. Please choose Post Hospitalization before making change.");
								okBtn.setEnabled(true);
							}
						} else {
							if(checkSelectedClassifationAvailableOrNot) {
								selectedCheckBox.setValue(!value);
								showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Post Hospitalization before making change.");
								okBtn.setEnabled(false);
							} else {
								uploadedDocListenerTableObj.setBillClassificationsDynamically((((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), false, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
							}
						}
					}
					
					
				}
				
				if(domicillaryHospitalisation != null){
					if(null != chkhospitalization && null != chkhospitalization.getValue() && null !=chkPreHospitalization && !chkPreHospitalization.getValue() &&
							null != chkPostHospitalization && !chkPostHospitalization.getValue()){
						
						if(null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() && !bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation()){
							domicillaryHospitalisation.setEnabled(true);
						}
						else
						{
							domicillaryHospitalisation.setEnabled(true);
						}
					}
					else
					{
						domicillaryHospitalisation.setEnabled(false);
					}
					
				}
			
			}
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				okBtn.setEnabled(true);
				//dialog.close();
				selectedCheckBox.setValue(!selectedCheckBox.getValue());
			
			}
			});
	}
	
	protected void showConfirmationPopupForOtherBenefits(final CheckBox selectedCheckBox, final Boolean isOtherBenefits) {
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure Do you want to change Bill Classification ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								okBtn.setEnabled(true);
								if (!dialog.isConfirmed()) {
									dialog.close();
									Boolean value = selectedCheckBox.getValue();
									if(isOtherBenefits) {
										if((!bean.getOtherBenefitsFlag() && (chkOtherBenefits != null && chkOtherBenefits.getValue() != null && !chkOtherBenefits.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.OTHER_BENEFIT)) {
											showPopup("Other Benefits is selected as classification in Bill Entry popup. Please choose Other Benefits classifcation before making change.");
											okBtn.setEnabled(false);
											selectedCheckBox.setValue(!selectedCheckBox.getValue());
										} else {
											Boolean checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.OTHER_BENEFIT);
											if(value) {
												if(!checkSelectedClassifationAvailableOrNot) {
//													selectedCheckBox.setValue(!value);
													uploadedDocListenerTableObj.setBillClassificationsDynamicallyForOtherBenefits((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)),(bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)),(chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue()) ? true : false);
//													showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
													okBtn.setEnabled(true);
												}
											} else {
												if(checkSelectedClassifationAvailableOrNot) {
													selectedCheckBox.setValue(!value);
													showPopup("Other Benefits is selected as classification in Bill Entry popup. Please choose Other Benefits classifcation before making change.");
													okBtn.setEnabled(false);
												} else {
													uploadedDocListenerTableObj.setBillClassificationsDynamicallyForOtherBenefits((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)),(bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)),(chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue()) ? true : false);
													
												}
											}
										}
									}  else {
									dialog.close();
									selectedCheckBox.setValue(!selectedCheckBox.getValue());
								}
							}

							}
						});*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure Do you want to change Bill Classification ?", buttonsNamewithType);
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				
				Boolean value = selectedCheckBox.getValue();
				if(isOtherBenefits) {
					if((!bean.getOtherBenefitsFlag() && (chkOtherBenefits != null && chkOtherBenefits.getValue() != null && !chkOtherBenefits.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.OTHER_BENEFIT)) {
						showPopup("Other Benefits is selected as classification in Bill Entry popup. Please choose Other Benefits classifcation before making change.");
						okBtn.setEnabled(false);
						selectedCheckBox.setValue(!selectedCheckBox.getValue());
					} else {
						Boolean checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.OTHER_BENEFIT);
						if(value) {
							if(!checkSelectedClassifationAvailableOrNot) {
//								selectedCheckBox.setValue(!value);
								uploadedDocListenerTableObj.setBillClassificationsDynamicallyForOtherBenefits((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)),(bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)),(chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue()) ? true : false);
//								showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
								okBtn.setEnabled(true);
							}
						} else {
							if(checkSelectedClassifationAvailableOrNot) {
								selectedCheckBox.setValue(!value);
								showPopup("Other Benefits is selected as classification in Bill Entry popup. Please choose Other Benefits classifcation before making change.");
								okBtn.setEnabled(false);
							} else {
								uploadedDocListenerTableObj.setBillClassificationsDynamicallyForOtherBenefits((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)),(bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)),(chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue()) ? true : false);
								
							}
						}
					}
				}  else {
				
				selectedCheckBox.setValue(!selectedCheckBox.getValue());
			}
		
			}
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			}
			});
		//dialog.setClosable(false);
	}
	
	public Boolean checkSelectedClassifationAvailableOrNot(Long classificationKey) {
		Boolean isAvailable = false;
		if(uploadedDocListenerTableObj != null){
			List<UploadDocumentDTO> values = uploadedDocListenerTableObj.getValues();
			if(values != null && !values.isEmpty()) {
				for (UploadDocumentDTO uploadDocDTO : values) {
					List<BillEntryDetailsDTO> billEntryDetailList = uploadDocDTO.getBillEntryDetailList();
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
						if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(classificationKey)) {
							isAvailable = true;
							break;
						}
					}
				}
			}
		}
		return isAvailable;
	}
	
	public Boolean checkSelectedClassifationAvailableOrNotForWithDraw(Long classificationKey) {
		Boolean isAvailable = false;
		
			/*uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
			uploadWithDrawTableObj.init();
			uploadWithDrawTableObj.setReferenceData(reference);
			uploadWithDrawTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
			if(uploadWithDrawTableObj != null){*/
			/*List<WithDrawPostProcessBillDetailsDTO> values = this.uploadWithDrawTableObj.getValues();
			if(values != null && !values.isEmpty()) {
				for (WithDrawPostProcessBillDetailsDTO uploadDocDTO : values) {*/
//					List<UploadDocumentDTO> uploadList = this.uploadDocDTO;
//					List<BillEntryDetailsDTO> billEntryDetailList = uploadList.get
					/*for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
						if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(classificationKey)) {
							isAvailable = true;
							break;
						}
					}*/
					List<UploadDocumentDTO> uploadList = this.uploadDocDTO;
					for (UploadDocumentDTO uploadDocumentDTO : uploadList) {
						List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
							if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(classificationKey)) {
								isAvailable = true;
								break;
							}
						}
					}
//		}
		return isAvailable;
	}
	
	public boolean validatePage() {
		try {
			
			if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
			{
				if(!((null != this.chkEmergencyMedicalEvaluation && null != this.chkEmergencyMedicalEvaluation.getValue() && this.chkEmergencyMedicalEvaluation.getValue()) ||
					(null != this.chkCompassionateTravel && null != this.chkCompassionateTravel.getValue() && this.chkCompassionateTravel.getValue()) ||
					(null != this.chkRepatriationOfMortalRemains && null != this.chkRepatriationOfMortalRemains.getValue() && this.chkRepatriationOfMortalRemains.getValue()) ||
					(null != this.chkPreferredNetworkHospital && null != this.chkPreferredNetworkHospital.getValue() && this.chkPreferredNetworkHospital.getValue()) ||
					(null != this.chkSharedAccomodation && null != this.chkSharedAccomodation.getValue() && this.chkSharedAccomodation.getValue())))
				{
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox("Please select any one of the benefits", buttonsNamewithType);
					return false;
									
				}else{
					this.binder.commit();
					return true;
				}
			}else{
				this.binder.commit();
				return true;
			}
			
			
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	private void buildOtherBenefitsLayout(Boolean value)
	{
		if(value)
		{
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			
			chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evaluation", "emergencyMedicalEvaluation", CheckBox.class);
			
			chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
			
			chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
			
			chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
			
			if(null != this.bean.getClaimDTO() && (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
					|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))){
				chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
			}
			
			chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
			
			FormLayout otherBenefitsLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
			FormLayout otherBenefitsLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
			FormLayout otherBenefitsLayout3 = new FormLayout(chkRepatriationOfMortalRemains);			
			
			HorizontalLayout otherBenefitsLayput = new HorizontalLayout();
			otherBenefitsLayput.addComponents(otherBenefitsLayout1,otherBenefitsLayout2,otherBenefitsLayout3);
			
			if(null != this.bean.getClaimDTO().getClaimType() && (SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				
				if( bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && 
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null &&
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
				{
					
				otherBenefitsLayput.removeAllComponents();
				otherBenefitsLayput.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
				
				}
				
			}
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkSharedAccomodation.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
				
				if( bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && 
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null &&
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
				{
					if(chkOtherBenefits != null){
						chkOtherBenefits.setValue(null);
						chkOtherBenefits.setEnabled(false);
					}
					otherBenefitsLayput.removeAllComponents();
				}
				
			}
			
//			if(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
					&& ((((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_72) ||
							bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87))
							|| bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_81)
							|| bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_012))
					&& ("G").equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getPolicyPlan()))
					|| (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_84) ||
							bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91)))){
				
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkCompassionateTravel.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
				chkPreferredNetworkHospital.setVisible(false);
			}
			else if(null != this.bean.getClaimDTO() && ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkCompassionateTravel.setVisible(false);
			}
			
			otherBenefitsLayput.setSpacing(false);
			otherBenefitsLayput.setMargin(false);
			
			otherBenefitsLayout.addComponent(otherBenefitsLayput);
		}else {
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			otherBenefitsLayout.removeAllComponents();
		}
		
		//return otherBenefitsLayput;
		
	}
	
	private List<Field<?>> getListOfOtherBenefitsChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkEmergencyMedicalEvaluation);
		fieldList.add(chkCompassionateTravel);
		fieldList.add(chkRepatriationOfMortalRemains);
		fieldList.add(chkPreferredNetworkHospital);
		fieldList.add(chkSharedAccomodation);
		return fieldList;
	}
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	 public void setPresenter(String presenter){
		 this.presenter = presenter;
	 }
	 
	 public void setReferenceData(Map<String, Object> referenceData) {
		 reference = referenceData;
	}

	 private HorizontalLayout buildWithdrawBillClassificationLayoutForEdit() {
			chkhospitalization = binder.buildAndBind("Hospitalisation",
					"hospitalizaionFlag", CheckBox.class);
			//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
//			chkhospitalization.setEnabled(false);			

			chkPartialHospitalization = binder.buildAndBind(
					"Partial-Hospitalisation", "partialHospitalizaionFlag",
					CheckBox.class);
			//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
			chkPartialHospitalization.setEnabled(false);
			
			if(null != bean.getPartialHospitalizaionFlag() && bean.getPartialHospitalizaionFlag()){
				chkPartialHospitalization.setValue(false);
				chkhospitalization.setEnabled(true);
			}

			chkHospitalizationRepeat = binder.buildAndBind(
					"Hospitalisation (Repeat)", "isHospitalizationRepeat",
					CheckBox.class);
			//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
			chkHospitalizationRepeat.setEnabled(false);

			chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation",
					"preHospitalizaionFlag", CheckBox.class);

			if (!this.bean.getIsPreHospApplicable()) {
//				chkPreHospitalization.setEnabled(false);
			}

			chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation",
					"postHospitalizaionFlag", CheckBox.class);

			if (!this.bean.getIsPostHospApplicable()) {
//				chkPostHospitalization.setEnabled(false);
			}

			chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount",
					"lumpSumAmountFlag", CheckBox.class);
			if (!bean.getIsLumpsumApplicable()) {
				chkLumpSumAmount.setEnabled(false);
			} else if(!bean.getLumpSumAmountFlag()) {
				chkLumpSumAmount.setEnabled(false);
			}

			chkAddOnBenefitsHospitalCash = binder.buildAndBind(
					"Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash",
					CheckBox.class);

			if (!this.bean.getIsHospitalCashApplicable()) {
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}

			chkAddOnBenefitsPatientCare = binder.buildAndBind(
					"Add on Benefits (Patient Care)", "addOnBenefitsPatientCare",
					CheckBox.class);

			
			chkOtherBenefits = binder.buildAndBind(
					"Other Benefits", "otherBenefitsFlag",
					CheckBox.class);
			chkOtherBenefits.setValue(false);
			//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
			
			if (!this.bean.getIsPatientCareApplicable()) {
				chkAddOnBenefitsPatientCare.setEnabled(false);
			}

			FormLayout classificationLayout1 = new FormLayout(chkhospitalization,
					chkHospitalizationRepeat);
			// classificationLayout1.setMargin(true);
			// classificationLayout1.setWidth("20%");
			FormLayout classificationLayout2 = new FormLayout(
					chkPreHospitalization, chkLumpSumAmount);
			// classificationLayout2.setMargin(true);
			// classificationLayout2.setWidth("20%");
			FormLayout classificationLayout3 = new FormLayout(
					chkPostHospitalization, chkAddOnBenefitsHospitalCash);
			// classificationLayout3.setMargin(true);
			// classificationLayout3.setWidth("40%");
			FormLayout classificationLayout4 = new FormLayout(
					chkPartialHospitalization, chkAddOnBenefitsPatientCare);
			
			FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits);
			// classificationLayout4.setMargin(true);
			// classificationLayout4.setWidth("40%");

			/*
			 * FormLayout classificationLayout1 = new
			 * FormLayout(chkhospitalization,chkLumpSumAmount); FormLayout
			 * classificationLayout2 = new
			 * FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
			 * FormLayout classificationLayout3 = new
			 * FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
			 * FormLayout classificationLayout4 = new
			 * FormLayout(chkPartialHospitalization);
			 */

			HorizontalLayout billClassificationLayout = new HorizontalLayout(
					classificationLayout1, classificationLayout2,
					classificationLayout3, classificationLayout4,classificationLayout5);
			// billClassificationLayout.setCaption("Document Details");
			billClassificationLayout.setMargin(false);
			billClassificationLayout.setCaption("Bill Classification");
			billClassificationLayout.setSpacing(false);
			// billClassificationLayout.setMargin(true);
			// billClassificationLayout.setWidth("110%");
			// addBillClassificationLister();

			return billClassificationLayout;
		}
	 
	 protected void showConfirmationPopupHospitalisationWithDrawCashless(final CheckBox selectedCheckBox) {
			ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Are you sure Do you want to change Bill Classification ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									okBtn.setEnabled(true);
									if (!dialog.isConfirmed()) {
										dialog.close();
										Boolean value = selectedCheckBox.getValue();
											/*if((!bean.getPostHospitalizaionFlag() && (chkPostHospitalization != null && chkPostHospitalization.getValue() != null && !chkPostHospitalization.getValue())) &&
													(presenter == null && !presenter.equalsIgnoreCase("withdraw_classification") ? checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION) : checkSelectedClassifationAvailableOrNotForWithDraw(ReferenceTable.PRE_HOSPITALIZATION))) {
												showPopup("Hospitalization is selected as classification in Bill Entry popup. Please choose Post Hospitalization classifcation before making change.");
												okBtn.setEnabled(false);
												selectedCheckBox.setValue(!selectedCheckBox.getValue());
											} else {*/
												Boolean checkSelectedClassifationAvailableOrNot = false;

													checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNotForWithDraw(ReferenceTable.HOSPITALIZATION);

												
												if(value) {
													if(!checkSelectedClassifationAvailableOrNot) {
//														selectedCheckBox.setValue(!value);
														if(presenter == null ) {
															uploadedDocListenerTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
														} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
															uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
															uploadWithDrawTableObj.setReferenceData(reference);
															uploadWithDrawTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
														}
//														showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
														okBtn.setEnabled(true);
													}
												} else {
													if(checkSelectedClassifationAvailableOrNot) {
														selectedCheckBox.setValue(!value);
														showPopup("Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Pre Hospitalization before making change.");
														okBtn.setEnabled(false);
													} else {
														if(presenter == null ) {
															uploadedDocListenerTableObj.setBillClassificationsDynamically(false, ( ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
														} else if(presenter != null && presenter.equalsIgnoreCase("withdraw_classification")) {
															uploadWithDrawTableObj = uploadWithDrawTableObjInst.get();
															uploadWithDrawTableObj.setReferenceData(reference);
															uploadWithDrawTableObj.setBillClassificationsDynamically(false, ( ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
														} 
														
													}
										}
										
										if(null != chkhospitalization && null != chkhospitalization.getValue() && null !=chkPreHospitalization && !chkPreHospitalization.getValue() &&
												null != chkPostHospitalization && !chkPostHospitalization.getValue()){
											
											if(null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() && !bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation()){
												domicillaryHospitalisation.setEnabled(true);
											}
											else
											{
												domicillaryHospitalisation.setEnabled(true);
											}
										}
										else
										{
											if(presenter == null){
												domicillaryHospitalisation.setEnabled(false);
											}
										}
									} else {
										dialog.close();
										selectedCheckBox.setValue(!selectedCheckBox.getValue());
									}
								}

								
							});
			dialog.setClosable(false);
		}
		
}
