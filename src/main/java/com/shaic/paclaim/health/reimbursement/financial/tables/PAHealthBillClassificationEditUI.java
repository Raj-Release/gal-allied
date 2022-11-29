package com.shaic.paclaim.health.reimbursement.financial.tables;

import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class PAHealthBillClassificationEditUI extends ViewComponent {

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

	private BeanFieldGroup<PreauthDTO> binder;

	private List<UploadDocumentDTO> uploadDocDTO;
	
	private com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable uploadedDocListenerTableObj;
	
	private PreauthDTO bean;
	
	private com.vaadin.ui.Button okBtn;

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDTO>(PreauthDTO.class);
		this.binder.setItemDataSource(bean);
	}

	public void init(PreauthDTO bean) {
		this.bean = bean;	
		initBinder();
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayout();
		setCompositionRoot(buildBillClassificationLayout);
	}

	public void initForEdit(PreauthDTO bean, List<UploadDocumentDTO> uploadDocDTO, com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable uploadedDocListenerTable,com.vaadin.ui.Button okBtn) {
		this.bean = bean;
		this.uploadDocDTO = uploadDocDTO;
		this.uploadedDocListenerTableObj = uploadedDocListenerTable;
		this.okBtn = okBtn;
		initBinder();
		HorizontalLayout buildBillClassificationLayout = buildBillClassificationLayoutForEdit();
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
		setCompositionRoot(buildBillClassificationLayout);
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
				classificationLayout3, classificationLayout4);
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
				classificationLayout3, classificationLayout4);
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
		if (false) {
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

					/**
					 * Below validation is added for cancel rod scenario. If an
					 * hospitalization rod is cancelled and user tries to
					 * deselect hospitalization and select hospitalization
					 * repeat, then below validation will not allow user to
					 * create an hospitalization repeat rod, since
					 * hospitalization rod is not yet created. -- Added for
					 * #3768
					 */
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
		}
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
									Label label = new Label(
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
											true);
									okBtn.setEnabled(false);
									chkhospitalization.setValue(false);
								}
							} else {
								if (validateBillClassification()) {
									// Label label = new
									// Label("Pre or Post hospitalization cannot exist without hospitalization",
									// ContentMode.HTML);
									Label label = new Label(
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
											true);
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
												Label label = new Label(
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
														true);
												okBtn.setEnabled(false);
												chkPreHospitalization.setValue(false);
											} else {
												if(bean.getIsHospitalizationRepeat()) {
													Label label = new Label(
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
															true);
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
										Label label = new Label(
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
												true);
										okBtn.setEnabled(false);
										chkPostHospitalization.setValue(false);
									} else {
										if(bean.getIsHospitalizationRepeat()) {
											Label label = new Label(
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
													true);
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
									Label label = new Label(
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
											true);
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
									Label label = new Label(
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
											true);
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
									Label label = new Label(
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
											true);
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
									Label label = new Label(
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
											true);
									okBtn.setEnabled(false);
									// chkPartialHospitalization.setValue(false);
									chkPartialHospitalization.setValue(null);
								}

							} else {

								if (validateBillClassification()) {
									Label label = new Label(
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
											true);
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
										Label label = new Label(
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
												true);
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
									Label label = new Label(
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
											true);
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
	}
	
	
	protected void showPopup(String message) {
		Label label = new Label(
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
				true);
	}
	
	protected void showConfirmationPopup(final CheckBox selectedCheckBox, final Boolean isPrehosp, final Boolean isPostHosp) {
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
									if(isPrehosp) {
										if((!bean.getPostHospitalizaionFlag() && (chkPostHospitalization != null && chkPostHospitalization.getValue() != null && !chkPostHospitalization.getValue())) && checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION)) {
											showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please choose Post Hospitalization classifcation before making change.");
											okBtn.setEnabled(false);
											selectedCheckBox.setValue(!selectedCheckBox.getValue());
										} else {
											Boolean checkSelectedClassifationAvailableOrNot = checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION);
											if(value) {
												if(!checkSelectedClassifationAvailableOrNot) {
//													selectedCheckBox.setValue(!value);
													uploadedDocListenerTableObj.setBillClassificationsDynamically(true, (bean.getPostHospitalizaionFlag() ? bean.getPostHospitalizaionFlag() : ((chkPostHospitalization.getValue() != null && chkPostHospitalization.getValue()) ? true : false)), bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
//													showPopup("Pre Hospitalization is not selected as classification in Bill Entry popup. Please choose Pre Hospitalization before making change.");
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
//													selectedCheckBox.setValue(!value);
													uploadedDocListenerTableObj.setBillClassificationsDynamically((bean.getPreHospitalizaionFlag() ? bean.getPreHospitalizaionFlag() : ((chkPreHospitalization.getValue() != null && chkPreHospitalization.getValue()) ? true : false)), true, bean.getHospitalizaionFlag() ? true : (bean.getIsHospitalizationRepeat() ? true : (bean.getPartialHospitalizaionFlag() ? true : false)));
//													showPopup("Post Hospitalization is not selected as classification in Bill Entry popup. Please choose Post Hospitalization before making change.");
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
								} else {
									dialog.close();
									selectedCheckBox.setValue(!selectedCheckBox.getValue());
								}
							}

							
						});
		dialog.setClosable(false);
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
	
	public boolean validatePage() {
		try {
			this.binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
		return true;
	}
}
