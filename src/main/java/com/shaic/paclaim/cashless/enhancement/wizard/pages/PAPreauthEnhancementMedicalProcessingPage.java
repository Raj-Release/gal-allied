package com.shaic.paclaim.cashless.enhancement.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.shaic.claim.preauth.wizard.pages.PEDValidationTable;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.paclaim.cashless.listenertables.PAPEDValidationListenerTableForPremedical;
import com.shaic.paclaim.cashless.listenertables.PAProcedureExclusionCheckTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAPreauthEnhancementMedicalProcessingPage extends ViewComponent implements WizardStep<PreauthDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6374897297399990973L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PAPEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<PAProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
//	@Inject
//	private Instance<PreauthButtons> preauthButtonInstance;

	private PAPEDValidationListenerTableForPremedical pedValidationTableObj;

	private PAProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
//	private PreauthButtons preauthButtonObj;
	
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private Map<String, Object> referenceData;
	
	private String diagnosisName;
	
	Button initiatePEDButton;
	
	private GWizard wizard;
	
	private OptionGroup preExistingDisablities;
	
	private VerticalLayout preExDiesForm;
	
	private TextArea pedDetailsBox;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	public void init(PreauthDTO bean,GWizard wizard ) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	@Override
	public String getCaption() {
		return "Medical Processing";
	}

	@Override
	public Component getContent() {
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		initiatePEDButton.setEnabled(false);
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);
		
		PAPEDValidationListenerTableForPremedical pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init("preauthEnhancement", bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		PAProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
		Collection<Boolean> preExstingdisValues = new ArrayList<Boolean>(2);
		preExstingdisValues.add(true);
		preExstingdisValues.add(false);
		
		preExistingDisablities = new OptionGroup("Pre-Existing Disabilities");
		preExistingDisablities.addItems(preExstingdisValues);
		
		preExistingDisablities.setItemCaption(true, "Yes");
		preExistingDisablities.setItemCaption(false, "No");
		preExistingDisablities.setStyleName("horizontal");
		
		preExDiesForm = new VerticalLayout(new FormLayout(preExistingDisablities));
		
		preExistingDisablities.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -283060651842304500L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					pedDetailsBox = new TextArea();
					pedDetailsBox.setNullRepresentation("");
					pedDetailsBox.setCaption("Details");
					pedDetailsBox.setValue(bean.getPreauthMedicalProcessingDetails().getPedDisabilityDetails());
					preExDiesForm.addComponent(pedDetailsBox);
				} else {
					if(pedDetailsBox != null) {
						preExDiesForm.removeComponent(pedDetailsBox);
					}
					
				}
				
			}
		});

		preExistingDisablities.setValue(bean.getPreauthMedicalProcessingDetails().getPedDisability());
		preExistingDisablities.select(bean.getPreauthMedicalProcessingDetails().getPedDisability());
		
		VerticalLayout wholeVLayout = new VerticalLayout(buttonHLayout, preExDiesForm, pedValidationTableInstance, procedureExclusionCheckTableInstance);
		wholeVLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);
		addListener();
		
		return wholeVLayout;
	}
	
	public void addListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}				
			}
		});
	}

	
	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);						
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	

	
	
public Boolean alertMessageForPEDInitiate(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}
	private void setPEDTable(PEDValidationTable pedValidationTableInstance) {
////		List<DiagnosisTableDTO> diagnosisTableList2 = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
//		List<DiagnosisDetailsTableDTO> list = new ArrayList<DiagnosisDetailsTableDTO>();
//		List<TmpPED> tmpPEDList = (List<TmpPED>) referenceData.get("pedList");
//		for(int i = 8; i<10; i++) {
//			if(!tmpPEDList.isEmpty()) {
//				int size = tmpPEDList.size();
//				Float center = new Float(size)/ 2;
//				int round = Math.round(center);
//				for (int j = 0; j<tmpPEDList.size(); j++) {
//					 TmpPED ped =  tmpPEDList.get(j);
//					 DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
//					 dto.setDiagnosisId(new Long(i));
//					 if(j == round) {
//						 
//						 dto.setDiagnosis("Diagnosis" + i);
//					 }
//					 dto.setPedName(ped.getPedName());
////					 dto.setPolicyAgeing(new Long(2));
//					 this.pedValidationTableObj.addBeanToList(dto);
//				}
//				
//			}
			
		}
		
		
//		for (DiagnosisTableDTO diagnosisTableDTO : diagnosisTableList2) {
//			PEDValidationTableDTO dto = new PEDValidationTableDTO();
//			dto.setDiagnosis(diagnosisTableDTO.getDiagnosis() != null ? diagnosisTableDTO.getDiagnosis().getValue() : null);
//			dto.setPedName("Dummy PED");
//			dto.setPolicyAgeing(new Long(2));
//			pedValidationTableInstance.addBeanToList(dto);
//		}
//	}
	
	private void setExclusionTable(ProcedureExclusionCheckTable procedureExclusionCheck) {
//		List<ProcedureTableDTO> procedureDTOList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
//		List<NewProcedureTableDTO> newProcedureDTOList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
//		List<ProcedureTableDTO> list = new ArrayList<ProcedureTableDTO>();
//		for (ProcedureTableDTO procedureDto : procedureDTOList) {
//			addProcedureBeantoList(procedureExclusionCheck, procedureDto);
//		}
//		
//		for (NewProcedureTableDTO procedureDto : newProcedureDTOList) {
//			ProcedureDTO dto = new ProcedureDTO();
////			dto.setProcedureName(procedureDto.getProcedureName());
////			dto.setProcedureCode(procedureDto.getProcedureCode());
//			dto.setPackageAmount("2000");
//			procedureExclusionCheck.addBeanToList(dto);
//		}
	}

	private void addProcedureBeantoList(
			ProcedureExclusionCheckTable procedureExclusionCheck,
			ProcedureTableDTO procedureDto) {
		ProcedureDTO dto = new ProcedureDTO();
//		dto.setProcedureName(procedureDto.getProcedureName().getValue());
//		dto.setProcedureCode(procedureDto.getProcedureCode().getValue());
		dto.setPackageAmount("2000");
//			dto.setPackageAmount(procedureDto.get);
//			dto.setPolicyAging(procedureDto.getP);
		procedureExclusionCheck.addBeanToList(dto);
	}

	@Override
	public boolean onAdvance() {
		return validatePage();
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		return validatePage();
	}
	
	@SuppressWarnings("static-access")
	private boolean validatePage() {
		Boolean hasError = false;
		String eMsg = "";
		
		if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.pedValidationTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		if(this.procedureExclusionCheckTableObj != null) {
			boolean isValid = this.procedureExclusionCheckTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.procedureExclusionCheckTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
	   if(hasError) {
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
		    
		    hasError = true;
		    return !hasError;
	   } else {
			try {
//				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setPedValidationTableList(this.pedValidationTableObj.getValues());
				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
				this.bean.getPreauthMedicalProcessingDetails().setPedDisability((Boolean)preExistingDisablities.getValue());
				this.bean.getPreauthMedicalProcessingDetails().setPedDisabilityDetails(pedDetailsBox != null  ? pedDetailsBox.getValue() : null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	
	public void generateButtonFields(String buttonSelected){
		
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
//		this.pedValidationTableObj.setTableList(this.bean.getPreMedicalPreauthMedicalDecisionDetails().getPedValidationTableList());
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : pedValidationTableList) {
			List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
			int size = pedList.size();
			if(size != 0) {
				Float value = (float) (size/2);
				int round = Math.round(value);
				try {
					PedDetailsTableDTO pedDetailsTableDTO = pedList.get(round);
					pedDetailsTableDTO.setIsShowingCopay(true);
					if(diagnosisDetailsTableDTO.getCopayPercentage() != null) {
						SelectValue selectValue = new SelectValue();
						selectValue.setId(diagnosisDetailsTableDTO.getCopayPercentage().longValue());
						selectValue.setValue(String.valueOf(diagnosisDetailsTableDTO.getCopayPercentage().intValue()));
						pedDetailsTableDTO.setCopay(selectValue);
					}
				} catch (Exception e) {
					
				}
			}
			for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
				this.pedValidationTableObj.addBeanToList(pedDetailsTableDTO);
			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				procedureDTO.setCopayPercentage(0d);
				if(procedureDTO.getCopayPercentage() != null) {
					SelectValue selectValue = new SelectValue();
					selectValue.setId(procedureDTO.getCopayPercentage().longValue());
					selectValue.setValue(String.valueOf(procedureDTO.getCopayPercentage().intValue()));
					procedureDTO.setCopay(selectValue);
				}
				if(procedureDTO.getEnableOrDisable())
				{
					procedureDTO.setStatusFlag(true);
				}
				else
				{
					procedureDTO.setStatusFlag(false);
				}
				
				procedureDTO = StarCommonUtils.getDefaultProcedureDTO(procedureDTO);
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
		
	}

	public void setDiagnosisName(String diagnosis) {
		this.diagnosisName = diagnosis;
	}	
	
}
