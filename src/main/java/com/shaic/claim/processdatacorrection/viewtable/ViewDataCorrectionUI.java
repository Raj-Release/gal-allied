package com.shaic.claim.processdatacorrection.viewtable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODService;
import com.shaic.claim.intimation.CashLessTableDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.HospitalScoringCorrectionView;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPDetailView;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPTabUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ViewDataCorrectionUI extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8551456904860347414L;

	@Inject
	private ProcessDataCorrectionDTO bean;

	private VerticalLayout verticalMain;

	private TextArea txtRemarks;

	@Inject
	private Instance<ViewDiganosisCorrectionTable> diganosisCorrectionInstance;

	private ViewDiganosisCorrectionTable diganosisCorrectionObj;

	@Inject
	private Instance<ViewProcedureCorrectionTable> procedureCorrectionInstance;

	private ViewProcedureCorrectionTable procedureCorrectionObj;	

	@Inject
	private Instance<ViewSpecialityCorrectionTable> specialityCorrectionInstance;

	private ViewSpecialityCorrectionTable specialityCorrectionObj;

	@Inject
	private Instance<ViewTreatingCorrectionTabel> treatingCorrectionInstance;

	private ViewTreatingCorrectionTabel treatingCorrectionObj;
	
	@Inject
	private Instance<ViewImplantCorrectionTabel> implantCorrectionInstance;

	private ViewImplantCorrectionTabel implantCorrectionObj;
	
	@Inject
	private Instance<ViewActualImplantCorrectionTabel> actualimplantCorrectionInstance;

	private ViewActualImplantCorrectionTabel actualimplantCorrectionObj;
	
	@Inject
	private Instance<ViewActualSpecialityTable> actualSpecialityInstance;

	private ViewActualSpecialityTable actualSpecialityObj;

	@Inject
	private HospitalScoringCorrectionView scoringCorrectionViewObj;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private ReimbursementService reimbService;

	private TextField roomCategory;

	private TextField proposedroomCategory;
	
	private OptionGroup ventilatorSupportRadio;
	
	private OptionGroup proposedVentilatorSupportRadio;
	
	private ComboBox cmbCovid19Vairant;
	    
	private ComboBox cmbCocktailDrug;
	    
	private HorizontalLayout covidVariantHLayout;

	private TextField treatmentType;

	private Button btnhospitalScroing;

	private Button btnproposedHospitalScroing;

	private Label labhospitalScoring;

	private TextArea correctionRemarks;
	
	private TextArea  reasonForIcdExclusion;

	private BeanFieldGroup<ProcessDataCorrectionDTO> binder;
	
	private TextField txtimplantApplicable;
	
	@Inject
	private ScoringAndPPDetailView scoringAndPPDetailView;
	private TextField admissionType;

	private TextField proposedadmissionType;
	
	@Inject
	private Instance<ViewActualProcedureCorrectionTable> actualProcedureCorrectionInstance;

	private ViewActualProcedureCorrectionTable actualProcedureCorrectionObj;	

	public void init(ProcessDataCorrectionDTO bean) {

		this.bean=bean;
		this.binder = new BeanFieldGroup<ProcessDataCorrectionDTO>(ProcessDataCorrectionDTO.class);
		this.binder.setItemDataSource(bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		specialityCorrectionObj = specialityCorrectionInstance.get();
		specialityCorrectionObj.init("", false, false);
		specialityCorrectionObj.setWidth("100%");
		specialityCorrectionObj.setCaption("Speciality Details");
		if(bean.getSpecialityCorrectionDTOs() !=null && !bean.getSpecialityCorrectionDTOs().isEmpty()){
			specialityCorrectionObj.setTableList(bean.getSpecialityCorrectionDTOs());
		}
		
		actualSpecialityObj = actualSpecialityInstance.get();
		actualSpecialityObj.init("", false, false);
		actualSpecialityObj.setWidth("100%");
		actualSpecialityObj.setCaption("Actual Speciality Details");
		if(bean.getSpecialityCorrectionDTOs() !=null && !bean.getSpecialityCorrectionDTOs().isEmpty()){
			actualSpecialityObj.setTableList(bean.getSpecialityCorrectionDTOs());
		}

		diganosisCorrectionObj = diganosisCorrectionInstance.get();
		diganosisCorrectionObj.init("", false, false);
		diganosisCorrectionObj.setCaption("Diganosis Details");
		if(bean.getDiganosisCorrectionDTOs() !=null && !bean.getDiganosisCorrectionDTOs().isEmpty()){
			diganosisCorrectionObj.setTableList(bean.getDiganosisCorrectionDTOs());
		}

		
		cmbCovid19Vairant = (ComboBox) binder.buildAndBind("COVID-19 Variant","covid19Variant", ComboBox.class);
		cmbCovid19Vairant.setReadOnly(true);
		
		cmbCocktailDrug = (ComboBox) binder.buildAndBind("Cocktail drug(Monoclonal Antibody) Approved","cocktailDrug", ComboBox.class);
		cmbCocktailDrug.setReadOnly(true);
		
		List<Reimbursement> reimbursementDetails = reimbService
				.getRembursementDetails(this.bean.getClaimKey());
		if(reimbursementDetails != null && !reimbursementDetails.isEmpty()){
			for (Reimbursement reimbursement : reimbursementDetails) {
				if(reimbursement.getCovid19Variant()!=null){
					BeanItemContainer<SelectValue> covid19VariantContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					MastersValue covid19Variant = reimbursement.getCovid19Variant();
					
					covid19VariantContainer.addBean(new SelectValue(covid19Variant.getKey(), covid19Variant.getValue()));
					cmbCovid19Vairant.setReadOnly(false);
					cmbCovid19Vairant.setContainerDataSource(covid19VariantContainer);
					cmbCovid19Vairant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbCovid19Vairant.setItemCaptionPropertyId("value");
					cmbCovid19Vairant.setValue(covid19VariantContainer.size() > 0 ? covid19VariantContainer.getIdByIndex(0): null);
					cmbCovid19Vairant.setReadOnly(true);
					
				}
				
				if(reimbursement.getCocktailDrug()!=null){
					BeanItemContainer<SelectValue> cocktailDrugContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					
					MastersValue cocktailDrug = reimbursement.getCocktailDrug();
					
				/*	SelectValue selectValue = new SelectValue();
					selectValue.setId((cocktailDrug != null && cocktailDrug.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : (cocktailDrug != null && cocktailDrug.equalsIgnoreCase("N")) ? ReferenceTable.COMMONMASTER_NO : 0l);
					selectValue.setValue((cocktailDrug != null && cocktailDrug.equalsIgnoreCase("Y")) ? SHAConstants.YES: (cocktailDrug != null && cocktailDrug.equalsIgnoreCase("N")) ? SHAConstants.No : "");
					*/
					cocktailDrugContainer.addBean(new SelectValue(cocktailDrug.getKey(), cocktailDrug.getValue()));
					cmbCocktailDrug.setReadOnly(false);
					cmbCocktailDrug.setContainerDataSource(cocktailDrugContainer);
					cmbCocktailDrug.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbCocktailDrug.setItemCaptionPropertyId("value");
					cmbCocktailDrug.setValue(cocktailDrugContainer.size() > 0 ? cocktailDrugContainer.getIdByIndex(0): null);
					cmbCocktailDrug.setReadOnly(true);
				}
			}
		}
		
		FormLayout covid19Form = new FormLayout(cmbCovid19Vairant);
		covid19Form.setSpacing(true);
		FormLayout cocktailDrugForm = new FormLayout(cmbCocktailDrug);
		cocktailDrugForm.setSpacing(true);
		covidVariantHLayout = new HorizontalLayout(covid19Form,cocktailDrugForm);
		covidVariantHLayout.setWidth("100%");
		covidVariantHLayout.setSpacing(true);
		
		treatingCorrectionObj = treatingCorrectionInstance.get();
		treatingCorrectionObj.init("", false, false);
		treatingCorrectionObj.setCaption("Treating Doctor Details");
		if(bean.getTreatingCorrectionDTOs() !=null && !bean.getTreatingCorrectionDTOs().isEmpty()){
			treatingCorrectionObj.setTableList(bean.getTreatingCorrectionDTOs());
		}	
		
		VerticalLayout tableLayout = null;
		if(bean.getTreatment() !=null && bean.getTreatment().equals("Surgical")){
			
			procedureCorrectionObj = procedureCorrectionInstance.get();
			procedureCorrectionObj.init("", false, false);
			procedureCorrectionObj.setCaption("Procedure Details");
			if(bean.getProcedureCorrectionDTOs() !=null && !bean.getProcedureCorrectionDTOs().isEmpty()){
				procedureCorrectionObj.setTableList(bean.getProcedureCorrectionDTOs());
			}
			
			implantCorrectionObj = implantCorrectionInstance.get();
			implantCorrectionObj.init("", false, false);
			implantCorrectionObj.setCaption("Implant Details");
			if(bean.getImplantCorrectionDTOs() !=null && !bean.getImplantCorrectionDTOs().isEmpty()){
				implantCorrectionObj.setTableList(bean.getImplantCorrectionDTOs());
			}
			
			actualimplantCorrectionObj = actualimplantCorrectionInstance.get();
			actualimplantCorrectionObj.init("", false, false);
			actualimplantCorrectionObj.setCaption("Actual Implant Details");
			if(bean.getImplantCorrectionDTOs() !=null && !bean.getImplantCorrectionDTOs().isEmpty()){
				actualimplantCorrectionObj.setTableList(bean.getImplantCorrectionDTOs());
			}
			
			txtimplantApplicable = new TextField("Implant Applicable");
	    	txtimplantApplicable.setWidth("300px");
	    	txtimplantApplicable.setNullRepresentation("-");
	    	if(bean.getImplantApplicable()){
	    		txtimplantApplicable.setValue("Yes");
	    	}else{
	    		txtimplantApplicable.setValue("No");
	    	}
	    	txtimplantApplicable.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	    	txtimplantApplicable.setReadOnly(true);
	    	
	    	FormLayout implantDetails = new FormLayout(txtimplantApplicable);
	    	actualProcedureCorrectionObj = actualProcedureCorrectionInstance.get();
	    	actualProcedureCorrectionObj.init("", false, false);
	    	actualProcedureCorrectionObj.setCaption("Actual Procedure Details");
			if(bean.getProcedureCorrectionDTOs() !=null && !bean.getProcedureCorrectionDTOs().isEmpty()){
				actualProcedureCorrectionObj.setTableList(bean.getProcedureCorrectionDTOs());
			}
	    	if(bean.getImplantApplicable()){
				tableLayout = new VerticalLayout(commonTopLayout(),diganosisCorrectionObj,covidVariantHLayout,treatingCorrectionObj,implantDetails,implantCorrectionObj,actualimplantCorrectionObj,procedureCorrectionObj,actualProcedureCorrectionObj/*,specialityCorrectionObj,actualSpecialityObj*/);
	    	}else{
				tableLayout = new VerticalLayout(commonTopLayout(),diganosisCorrectionObj,covidVariantHLayout,treatingCorrectionObj,implantDetails,procedureCorrectionObj,actualProcedureCorrectionObj/*,specialityCorrectionObj,actualSpecialityObj*/);
	    	}
		}else{
			tableLayout = new VerticalLayout(commonTopLayout(),diganosisCorrectionObj,covidVariantHLayout,treatingCorrectionObj,specialityCorrectionObj,actualSpecialityObj);
		}
		tableLayout.setSpacing(true);

		Panel mainPanel = new Panel(tableLayout);
		mainPanel.addStyleName("girdBorder");

		verticalMain = new VerticalLayout(mainPanel);
		verticalMain.setSizeFull();
		setCompositionRoot(verticalMain);
	}

	@SuppressWarnings("deprecation")
	public VerticalLayout commonTopLayout(){

		treatmentType = (TextField) binder.buildAndBind("Treatment Type","treatment",TextField.class);	
		roomCategory = (TextField) binder.buildAndBind("Room Category","roomCat",TextField.class);	
		proposedroomCategory = (TextField) binder.buildAndBind("Actual Room Category","proposedroomCat",TextField.class);
		
		ventilatorSupportRadio =new OptionGroup("");
		
		if(bean.getRoomCat() != null 
					&& bean.getRoomCat().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")){
			
			ventilatorSupportRadio = (OptionGroup) binder
					.buildAndBind("Ventilator Support",
							"ventilatorSupport", OptionGroup.class);
			
			
							ventilatorSupportRadio =new OptionGroup("Ventilator Support");
			 				ventilatorSupportRadio.addItems(getRadioButtonOptions());
			 				ventilatorSupportRadio.setItemCaption(true, "Yes");
			 				ventilatorSupportRadio.setItemCaption(false, "No");
			 				ventilatorSupportRadio.setStyleName("horizontal");
			 				if(bean.getVentilatorSupport() !=null){
			 				System.out.println(String.format("Ventilator Support value in View Details: [%s]", bean.getVentilatorSupport()));
			 				ventilatorSupportRadio.setValue(bean.getVentilatorSupport()? true : false);
			 				}
			 				ventilatorSupportRadio.setReadOnly(true);
		
		}
		
		proposedVentilatorSupportRadio =new OptionGroup("");
		if(bean.getRoomCat() != null 
					&& bean.getRoomCat().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")){
			
			proposedVentilatorSupportRadio = (OptionGroup) binder
					.buildAndBind("Actual Ventilator Support",
							"proposedVentilatorSupport", OptionGroup.class);
			
			//System.out.println(String.format("Actual Ventilator Support value in View Details: [%s]", bean.getVentilatorSupport()));
			proposedVentilatorSupportRadio =new OptionGroup("Actual Ventilator Support");
			proposedVentilatorSupportRadio.addItems(getRadioButtonOptions());
			proposedVentilatorSupportRadio.setItemCaption(true, "Yes");
			proposedVentilatorSupportRadio.setItemCaption(false, "No");
			proposedVentilatorSupportRadio.setStyleName("horizontal");
			//proposedVentilatorSupportRadio.setValue(bean.getProposedVentilatorSupport()? true : false);
			
			if(bean.getVentilatorSupport() !=null && bean.getVentilatorSupport().toString().equalsIgnoreCase("true")){
				proposedVentilatorSupportRadio.setValue(false);
			}
			
			if(bean.getVentilatorSupport() !=null && bean.getVentilatorSupport().toString().equalsIgnoreCase("false")){
				proposedVentilatorSupportRadio.setValue(true);
			}
			proposedVentilatorSupportRadio.setReadOnly(true);
		
		}else{
			//proposedVentilatorSupportRadio.setValue(bean.getProposedVentilatorSupport()? true : false);
			proposedVentilatorSupportRadio.setEnabled(false);
			//proposedVentilatorSupportRadio.setVisible(false);
		}
		correctionRemarks = (TextArea) binder.buildAndBind("Remarks","remarks",TextArea.class);	
		reasonForIcdExclusion = (TextArea) binder.buildAndBind("Time based excluded ICD Reason","icdExclusionReason",TextArea.class);	
		admissionType = (TextField) binder.buildAndBind("Type Of Admission","typeofAdmission",TextField.class);	
		proposedadmissionType = (TextField) binder.buildAndBind("Actual Type Of Admission","actualtypeofAdmission",TextField.class);	
		reasonForIcdExclusion.setReadOnly(true);
		TextField dummyField = new TextField();		
		btnhospitalScroing = new Button("Old Hospital Scoring/PP Coding");
		btnproposedHospitalScroing = new Button("Actual Hospital Scoring/PP Coding");

		btnhospitalScroing.addStyleName(ValoTheme.BUTTON_LINK);
		btnhospitalScroing.setWidth("-1px");
		btnhospitalScroing.setHeight("-10px");

		btnproposedHospitalScroing.addStyleName(ValoTheme.BUTTON_LINK);
		btnproposedHospitalScroing.setWidth("-1px");
		btnproposedHospitalScroing.setHeight("-10px");
		addListener();

		HorizontalLayout scoringHLayout = new HorizontalLayout(btnhospitalScroing,btnproposedHospitalScroing);
		scoringHLayout.setCaption("Hospital Scoring");
		scoringHLayout.setSpacing(true);
		
		FormLayout firstForm = new FormLayout(treatmentType,roomCategory,ventilatorSupportRadio,correctionRemarks,reasonForIcdExclusion);
		FormLayout secondForm = new FormLayout(dummyField,proposedroomCategory,proposedVentilatorSupportRadio,admissionType,proposedadmissionType);
		setReadOnly(firstForm,true);
		setReadOnly(secondForm,true);

		HorizontalLayout roomHLayout = new HorizontalLayout(firstForm,secondForm);
		VerticalLayout tableLayout = new VerticalLayout(roomHLayout,scoringHLayout);
		tableLayout.setSizeFull();
		return tableLayout;
	}
	
	 protected Collection<Boolean> getRadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	    


	public void addListener() {

		btnhospitalScroing.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showoldScoringView();				
			}
		});

		btnproposedHospitalScroing.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showProposedScoringView();				
			}
		});

	}

	public void showProposedScoringView(){

		/*scoringCorrectionViewObj.setDtoBean(bean);
		scoringCorrectionViewObj.setParentScoringButton(btnproposedHospitalScroing);
		scoringCorrectionViewObj.setScreenName("Data Validation View");	
		scoringCorrectionViewObj.setProposedScoring(false);
		scoringCorrectionViewObj.init(bean.getIntimationNo(), false,false);*/
		
		scoringAndPPDetailView.setIntimationNumber(bean.getIntimationNo());
		scoringAndPPDetailView.setScreenName("ViewPage");
		scoringAndPPDetailView.setIsoldppscoreview(false);
		scoringAndPPDetailView.init(false);
		VerticalLayout misLayout = new VerticalLayout(scoringAndPPDetailView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		popup.setHeight("58%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		scoringAndPPDetailView.setPopupWindow(popup);
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

	public void showoldScoringView(){

		/*scoringCorrectionViewObj.setDtoBean(bean);
		scoringCorrectionViewObj.setParentScoringButton(btnproposedHospitalScroing);
		scoringCorrectionViewObj.setScreenName("Data Validation View");	
		scoringCorrectionViewObj.setProposedScoring(false);
		scoringCorrectionViewObj.init(bean.getIntimationNo(), false,true);*/
		
		scoringAndPPDetailView.setIntimationNumber(bean.getIntimationNo());
		scoringAndPPDetailView.setScreenName("ViewPage");
		scoringAndPPDetailView.setIsoldppscoreview(true);
		scoringAndPPDetailView.init(false);
		VerticalLayout misLayout = new VerticalLayout(scoringAndPPDetailView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		popup.setHeight("58%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		scoringAndPPDetailView.setPopupWindow(popup);
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

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setHeight("29px");
				field.setNullRepresentation("-");
				correctionRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(correctionRemarks,null,getUI(),SHAConstants.DATA_CORECTION_VIEW);
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	public void setClearReferenceData(){
    	if(verticalMain!=null){
    		verticalMain.removeAllComponents();
    	}
    	
    	if(this.procedureCorrectionObj!=null){
    		this.procedureCorrectionObj.removeRow();
    		this.procedureCorrectionObj=null;
    	}
    	if(this.diganosisCorrectionObj!=null){
    		this.diganosisCorrectionObj.removeRow();
    		this.diganosisCorrectionObj=null;
    	}
    	if(this.procedureCorrectionObj!=null){
    		this.procedureCorrectionObj.removeRow();
    		this.procedureCorrectionObj=null;
    	}
    	if(this.specialityCorrectionObj!=null){
    		this.specialityCorrectionObj.removeRow();
    		this.specialityCorrectionObj=null;
    	}
    	if(this.treatingCorrectionObj!=null){
    		this.treatingCorrectionObj.removeRow();
    		this.treatingCorrectionObj=null;
    	}
    	if(this.implantCorrectionObj!=null){
    		this.implantCorrectionObj.removeRow();
    		this.implantCorrectionObj=null;
    	}
    	if(this.actualimplantCorrectionObj!=null){
    		this.actualimplantCorrectionObj.removeRow();
    		this.actualimplantCorrectionObj=null;
    	}
    	if(this.actualSpecialityObj!=null){
    		this.actualSpecialityObj.removeRow();
    		this.actualSpecialityObj=null;
    	}
    	this.binder=null;
    }
	
}
