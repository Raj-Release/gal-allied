/**
 * 
 */
package com.shaic.main.navigator.ui;

import java.util.Map;

import org.vaadin.addon.cdimvp.MVPView;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPBulkUploadRejection.OMPBulkUploadRejectionView;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPProcessNegotiationWizardView;
import com.shaic.claim.OMPProcessNegotiation.search.OMPProcessNegotiationView;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPAcknowledgementDocumentsPageWizard;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPClaimProcessorAckDocumentsDTO;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPProcessOmpAcknowledgementDocumentsView;
import com.shaic.claim.OMPProcessOmpClaimApprover.pages.OMPProcessOmpClaimApproverWizardView;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPProcessOmpClaimProcessorPageWizard;
//import com.shaic.claim.OMPclaimregistration.wizard.pages.OMPClaimRegistrationWizardView;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryPageWizard;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationSearchDTO;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeAndOsUpdationView;
import com.shaic.claim.omp.registration.OMPClaimRegistrationWizardView;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.reports.shadowProvision.SearchShowdowView;
import com.vaadin.v7.data.util.BeanItemContainer;
//import com.shaic.claim.OMPclaimregistration.wizard.pages.OMPClaimRegistrationWizardView;

public interface OMPMenuView extends MVPView {
	
	  
    void setViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);

    void setViewG(Class<? extends GMVPView> viewClass,
            BeanItemContainer<SelectValue> classification, boolean selectInNavigationTree);
    
    void ompProcessApproverSearch(Class<? extends GMVPView> viewClass,
            BeanItemContainer<SelectValue> classification, boolean selectInNavigationTree);
    
    //CR20181332 
    void ompBulkUploadRejectionSearch(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> statusContainer, boolean b);
    
    public void showOMPAddIntimationView(Class<? extends GMVPView> viewClass, OMPCreateIntimationTableDTO ompcreateintimationtabledto);

    void setViewG(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree, Map<String,Object> parameter);


    void setOmpClaimDetailRegistrationPage(
			Class<OMPClaimRegistrationWizardView> class1,
			OMPSearchClaimRegistrationTableDTO ompClaimDetailRegistrationDTO,
			Boolean secondaryParameter);

	public void showErrorPopUp(String eMsg);
	
	public void setOMPRejectionView(Class<? extends GMVPView> viewClass, SearchProcessRejectionTableDTO searchProcessRejectionTableDTO);
	
	
	  void setOMPSearchRegistration(Class<? extends GMVPView> viewClass,
				boolean selectInNavigationTree);

	void setOmpRODBillEntryview(
			Class<OMPProcessRODBillEntryPageWizard> class1,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country);
	
	void setOmpProcessApproverView(
			Class<OMPProcessOmpClaimApproverWizardView> class1,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country);

	void setOmpClaimProcessorview(
			Class<OMPProcessOmpClaimProcessorPageWizard> class1,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country);

	void setOmpNegotiationViewG(Class<OMPProcessNegotiationView> class1,
			boolean b);
	
	void setOmpProcessNegotiationview(
			Class<OMPProcessNegotiationWizardView> class1,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country);

	void setSearchClaimRateChangeViewG(
			Class<OMPClaimRateChangeAndOsUpdationView> class1, boolean b);

	void renderOMPRegistationPage(Class<? extends GMVPView> viewClass, OMPNewRegistrationSearchDTO resultDTO);

	void setViewOMPDocumentUploadProcess(Class<OMPProcessOmpAcknowledgementDocumentsView> class1,BeanItemContainer<SelectValue> classification, boolean b);
	
	void setOmpClaimProcessorAknview(
			Class<OMPAcknowledgementDocumentsPageWizard> class1,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country);
//
//void setOmpNegotiationViewG(Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree);
	
}


