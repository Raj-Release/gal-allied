package com.shaic.claim.preauth.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.domain.ClaimAmountDetailsService;
import com.shaic.domain.MasterService;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.PedValidation;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreAuthDetails extends ViewComponent {

	/**
	 * Pre Auth Requested Amount
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	private TextField txtConsultationDate;
	private TextField txtNatureOfTreatment;
	private TextField txtNumberOfDays;
	private TextField txtAdmissionReason;
	private TextField txtTreatmentType;
	private TextField txtRoomCategory;
	private TextField txtPreAuthRequestedAmount;
	private TextArea txtDiagnosis;
	private TextField txtDateOfAdmission;

	@Inject
	private PreAuthService preAuthService;

	@Inject
	private DiagnosisService diagnosisService;

	@Inject
	private MasterService masterService;

	@Inject
	private ClaimAmountDetailsService claimAmountDetailsService;

	private BeanFieldGroup<ViewPreAuthDetailsDTO> binder;

	public ViewPreAuthDetailsDTO viewPreAuthDetailsDTO;

	private FormLayout leftForm;

	private FormLayout rightForm;

	public VerticalLayout init(Long preAuthKey) {
		initBinder();
		viewPreAuthDetailsDTO = preAuthService.search(preAuthKey);
		// setting date of admission value
		txtDateOfAdmission = binder.buildAndBind("Date of Admission",
				"dateOfAdmission", TextField.class);

		SimpleDateFormat ft = new SimpleDateFormat("dd - MM - yyyy");
		SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		if (viewPreAuthDetailsDTO.getDateOfAdmission() != null) {
			try {
				Date tempDate = simpledateformat.parse(viewPreAuthDetailsDTO
						.getDateOfAdmission().toString());
				txtDateOfAdmission.setValue(viewPreAuthDetailsDTO
						.getDateOfAdmission() != null ? ft.format(tempDate)
						: "");
			} catch (ReadOnlyException | ParseException e) {
				e.printStackTrace();
			}
		}
		// setting diagnosis value
		List<PedValidation> pedValidation = diagnosisService.search(preAuthKey);
		List<DiagnosisDTO> diagnosisDTOList = diagnosisService
				.setMapper(pedValidation);
		txtDiagnosis = new TextArea("Diagnosis");
		String strDiagnosisName = masterService
				.getDiagnosisList(diagnosisDTOList);
		if (strDiagnosisName.length() > 0
				&& strDiagnosisName.charAt(strDiagnosisName.length() - 2) == ',') {
			strDiagnosisName = strDiagnosisName.substring(0,
					strDiagnosisName.length() - 2);
		}
		txtDiagnosis.setValue(strDiagnosisName != null ? strDiagnosisName : "");
		// setting pre auth reuqested amount
		txtPreAuthRequestedAmount = binder.buildAndBind(
				"Pre Auth Requested Amt", "preAuthRequestedAmt",
				TextField.class);
		List<ClaimAmountDetails> claimAmountDetailsList = claimAmountDetailsService
				.getClaimedAmoutnDetailsByPreAuthKey(preAuthKey);

		Float preAuthRequestedAmount = 0f;

		if(!claimAmountDetailsList.isEmpty()){
			for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsList) {
				preAuthRequestedAmount = preAuthRequestedAmount
						+ claimAmountDetails.getClaimedBillAmount();
			}
		}
		txtPreAuthRequestedAmount.setValue(preAuthRequestedAmount.toString()
				.equalsIgnoreCase("0f") ? "" : preAuthRequestedAmount.toString());
		// setting room category
		txtRoomCategory = binder.buildAndBind("Room Category", "roomCategory",
				TextField.class);
		txtRoomCategory
				.setValue(viewPreAuthDetailsDTO.getRoomCategory() != null ? viewPreAuthDetailsDTO
						.getRoomCategory() : "");
		// setting Treatement Type
		txtTreatmentType = binder.buildAndBind("Treatement Type",
				"treatmentType", TextField.class);
		txtTreatmentType
				.setValue(viewPreAuthDetailsDTO.getTreatmentType() != null ? viewPreAuthDetailsDTO
						.getTreatmentType() : "");
		// setting reason for admission
		txtAdmissionReason = binder.buildAndBind("Reason For Admission",
				"reasonForAdmission", TextField.class);
		txtAdmissionReason.setValue(viewPreAuthDetailsDTO
				.getReasonForAdmission() != null ? viewPreAuthDetailsDTO
				.getReasonForAdmission() : "");
		// setting number of days
		txtNumberOfDays = binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		txtNumberOfDays
				.setValue(viewPreAuthDetailsDTO.getNoOfDays() != null ? viewPreAuthDetailsDTO
						.getNoOfDays().toString() : "");
		// setting nature of treatement
		txtNatureOfTreatment = binder.buildAndBind("Nature of Treatement",
				"natureOfTreatement", TextField.class);
		txtNatureOfTreatment.setValue(viewPreAuthDetailsDTO
				.getNatureOfTreatement() != null ? viewPreAuthDetailsDTO
				.getNatureOfTreatement() : "");
		// setting consulation date
		txtConsultationDate = binder.buildAndBind("1st Consulation Date",
				"firstConsultationDate", TextField.class);
		txtConsultationDate.setValue(viewPreAuthDetailsDTO
				.getFirstConsultationDate() != null ? viewPreAuthDetailsDTO
				.getFirstConsultationDate() : "");
		// setting layout
		leftForm = new FormLayout(txtDateOfAdmission, txtDiagnosis,
				txtPreAuthRequestedAmount, txtRoomCategory, txtTreatmentType);
		rightForm = new FormLayout(txtAdmissionReason, txtNumberOfDays,
				txtNatureOfTreatment, txtConsultationDate);
		HorizontalLayout preAuthHor = new HorizontalLayout(leftForm, rightForm);
		mainLayout = new VerticalLayout(preAuthHor);
		setReadOnly(leftForm, true);
		setReadOnly(rightForm, true);
		return mainLayout;
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewPreAuthDetailsDTO>(
				ViewPreAuthDetailsDTO.class);
		this.binder.setItemDataSource(new ViewPreAuthDetailsDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				try{
					TextField field = (TextField) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}catch(ClassCastException e){
					TextArea field = (TextArea) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
	}

}
