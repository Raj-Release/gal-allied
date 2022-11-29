package com.shaic.paclaim.financial.claimapproval.hosiptalpagereview;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAClaimAprHosReviewPageWizard extends GMVPView, WizardStep<PreauthDTO>{
	void buildSuccessLayout();
	void init(PreauthDTO bean, GWizard wizard);
	void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue);
	void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue);
	void generateFieldsBasedOnTreatment();
	void intiateCoordinatorRequest();
	void genertateFieldsBasedOnPatientStaus();
	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);
	void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO);
	void setUpIrdaLevel2Values( BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb, SelectValue sel);
	void setUpIrdaLevel3Values(BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,SelectValue sel);
	void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne);
	void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO);
	void setCompareWithRODResult(String comparisonResult);
	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateCancelRODLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateReferToMedicalApproverLayout();
	void generateReferToBillEntryLayout();
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
	void updateProductBasedAmtDetails(Map<Integer, Object> detailsMap);
}
