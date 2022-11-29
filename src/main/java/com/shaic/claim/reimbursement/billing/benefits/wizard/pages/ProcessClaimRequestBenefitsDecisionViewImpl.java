/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.MedicalRequestBenefitsButtonsUI;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.ProcessClaimRequestBenefitsDecisionPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessClaimRequestBenefitsDecisionViewImpl extends AbstractMVPView 
implements ProcessClaimRequestBenefitsDecisionView
{

	@Inject
	private ProcessClaimRequestBenefitsDecisionPage documentDetailsPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private ReceiptOfDocumentsDTO bean;
	
	private VerticalLayout submainlayout;
	
    private ComboBox cmbFVRNotRequiredRemarks;
	
	private VerticalLayout fvrVertLayout;
	
	private OptionGroup select;
	
	private TextArea fvrOthersRemarks;
	
	private FormLayout dynamicFrmLayout;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private ComboBox allocationTo;
	private ComboBox fvrPriority;
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;

	private TextArea triggerPointsToFocus;
	private TextArea reasonForReferringIV;

	private TextArea remarksForCPU;

	private TextField txtQueryCount;

	private ArrayList<Component> mandatoryFields;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private Instance<MedicalRequestBenefitsButtonsUI> medicalRequestBenefitsButtonInstance;
	
	private MedicalRequestBenefitsButtonsUI medicalRequestBenefitsButtonObj;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		//documentDetailsPage.init(bean);

	}

	@Override
	public Component getContent() {
		Component comp =  documentDetailsPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.CLAIM_REQUEST_BENEFITS_DECISION_SETUP_DROPDOWN_VALUES, bean);
		return comp;

	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//documentDetailsPage.setValuesFromDTO();
	}

	@Override
	public boolean onAdvance() {		
		documentDetailsPage.setTableValuesToDTO();
		return documentDetailsPage.validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		documentDetailsPage.setTableValuesToDTO();
		return documentDetailsPage.validatePage();
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		localize(null);
		this.bean = bean;
		documentDetailsPage.init(bean,wizard);
		this.documentDetailsPage.totalApprovedAmt = 0d;
		//resetPage();
		//documentDetailsPage.resetPage();
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
	}
	
	
	
	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) 
	{
		documentDetailsPage.loadContainerDataSources(referenceDataMap);
	}

	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "decision");//String.valueOf(propertyId).toLowerCase());
        }
    
	

	private String textBundlePrefixString()
	{
		return "process-claim-benefits-";
	}


	
	@Override
	public void resetPage() {
		
		documentDetailsPage.resetPage();
		
	}

	
	@Override
	public void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer) {
		documentDetailsPage.generateButtonFields(SHAConstants.REFER_TO_COORDINATOR, selectValueContainer);
	}



	@Override
	public void generateSendToFinancialLayout() {
		documentDetailsPage.generateButtonFields(SHAConstants.FINANCIAL, null);
	}
	@Override
	public void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer){
		documentDetailsPage.generateButtonFields(SHAConstants.BILLING_CANCEL_ROD, selectValueContainer);
		
	}

	@Override
	public void generateReferCoOrdinatorLayoutForFA(
			BeanItemContainer<SelectValue> selectValueContainer) {
		documentDetailsPage.generateButton(4, selectValueContainer);
		
	}

	@Override
	public void generateApproveLayoutForFA() {
		documentDetailsPage.generateButton(9, null);
		
	}

	@Override
	public void generateCancelRODLayoutForFA(
			BeanItemContainer<SelectValue> selectValueContainer) {
		documentDetailsPage.generateButton(10, selectValueContainer);
		
	}

	@Override
	public void generateRejectLayoutForFA() {
		documentDetailsPage.generateButton(8, null);
		
	}

	@Override
	public void genertateReferToBillingLayoutForFA() {
		documentDetailsPage.generateButton(6, null);
		
	}

	@Override
	public void generateQueryLayoutForFA() {
		documentDetailsPage.generateButton(7, null);
		
	}
	
	@Override
	public void genrateFieldVisitLayoutForMA(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isFVRAssigned, String repName, String repContactNo) { 
		documentDetailsPage.genertateFieldsBasedOnFieldVisitForMA(selectValueContainer, fvrAssignTo,fvrPriority,
				isFVRAssigned,repName,repContactNo);
//		 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
//		this.bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
		 }
	
		@Override
		public void generateQueryLayoutForMA() {
			documentDetailsPage.generateQueryLayoutForMA();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			this.bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
	}
		
		@Override
		public void generateInvestigationLayoutForMA(boolean isDirectToAssignInv) {
			documentDetailsPage.generateInvestigationLayoutForMA(isDirectToAssignInv);
		}
		
		@Override
		public void generateCancelRODLayoutForMA() {
			documentDetailsPage.generateCancelRODLayoutForMA();
		}

		@Override
		public void generateApproveLayoutForMA() {
			documentDetailsPage.generateApproveRODLayoutForMA();
			
		}
		
		@Override
		public void generateRejectLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer) {
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			documentDetailsPage.generateRejectLayoutForMA(selectValueContainer);
		}
		
		@Override
		public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
			documentDetailsPage.setSubCategContainer(rejSubcategContainer);
		}
		
		@Override
		public void generateReferToCoordinatorLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer) {
			documentDetailsPage.generateReferToCoordinatorLayoutForMA(selectValueContainer);
			
		}
		
		@Override
		public void generateReferToMedicalLayoutForFA() {
			documentDetailsPage.generateButton(5, null);
			
		}
		
		@Override
		public void generateSendReplyToFinancial() {
			documentDetailsPage.generateSendReplyToFinancial();
			
		}
		
  }
