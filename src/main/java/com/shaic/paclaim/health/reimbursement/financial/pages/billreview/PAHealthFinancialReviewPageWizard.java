package com.shaic.paclaim.health.reimbursement.financial.pages.billreview;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthFinancialReviewPageWizard extends GMVPView, WizardStep<PreauthDTO>{
	void buildSuccessLayout();
	void init(PreauthDTO bean, GWizard wizard);
	void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue);
	void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue);
	void generateFieldsBasedOnTreatment();
	void intiateCoordinatorRequest();
	void genertateFieldsBasedOnPatientStaus();
	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);
	void setUpIrdaLevel2Values(BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue sel);
	void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO);
	void viewBillingWorkSheet(PreauthDTO bean);
	void setUpIrdaLevel3Values(BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,SelectValue sel);
	void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO);

//	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);

	void setCompareWithRODResult(String comparisonResult);
	void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne);
	void generateCancelRODLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateReferToBillingLayout();
	void generateReferToMedicalApproverLayout();
	void generateRejectLayout();
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
	void generateReferToBillEntryLayout();
	void updateProductBasedAmtDetails(Map<Integer, Object> detailsMap);
	void setCoverList(BeanItemContainer<SelectValue> coverContainer);
	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void genertateFieldsBasedOnDomicillaryFields(Boolean selectedValue);
}
