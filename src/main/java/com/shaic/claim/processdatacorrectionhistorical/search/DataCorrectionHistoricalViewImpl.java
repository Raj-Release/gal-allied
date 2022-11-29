package com.shaic.claim.processdatacorrectionhistorical.search;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.google.gwt.i18n.client.Messages.Select;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.processdatacorrection.listenertable.ActualDiganosisCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.ActualProcedureCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.ActualSpecialityCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.ActualTreatingCorrectionTabel;
import com.shaic.claim.processdatacorrection.listenertable.DiganosisCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.ProcedureCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.SpecialityCorrectionTable;
import com.shaic.claim.processdatacorrection.listenertable.TreatingCorrectionTabel;
import com.shaic.claim.processdatacorrection.search.HospitalScoringCorrectionView;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPTabUI;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;


public class DataCorrectionHistoricalViewImpl extends AbstractMVPView implements DataCorrectionHistoricalView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8551456904860347414L;

	@Inject
	private ProcessDataCorrectionDTO bean;

	private VerticalLayout verticalMain;
	
	private HorizontalLayout buttonHorLayout;
	
	private Map<String, Object> referenceData;
	
	private TextArea txtRemarks;
	
	private NewIntimationDto newIntimationDTO;
	
	private ClaimDto claimDTO;
	
	private BeanFieldGroup<ProcessDataCorrectionDTO> binder;

	private Button submitBtn;

	private Button cancelBtn;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<DiganosisCorrectionTable> diganosisCorrectionInstance;

	private DiganosisCorrectionTable diganosisCorrectionObj;
	
	@Inject
	private Instance<ActualDiganosisCorrectionTable> actualdiganosisInstance;

	private ActualDiganosisCorrectionTable actualdiganosisObj;
	
	@Inject
	private Instance<ActualProcedureCorrectionTable> actualprocedureInstance;
	
	private ActualProcedureCorrectionTable actualprocedureObj;
	
	@Inject
	private Instance<ProcedureCorrectionTable> procedureCorrectionInstance;
	
	private ProcedureCorrectionTable procedureCorrectionObj;
	
	@Inject
	private Instance<SpecialityCorrectionTable> specialityCorrectionInstance;

	private SpecialityCorrectionTable specialityCorrectionObj;
	
	@Inject
	private Instance<ActualSpecialityCorrectionTable> actualSpecialityInstance;

	private ActualSpecialityCorrectionTable actualSpecialityObj;
	
	@Inject
	private Instance<TreatingCorrectionTabel> treatingCorrectionInstance;

	private TreatingCorrectionTabel treatingCorrectionObj;
	
	@Inject
	private Instance<ActualTreatingCorrectionTabel> actualtreatingInstance;

	private ActualTreatingCorrectionTabel actualtreatingObj;
	
	@Inject
	private HospitalScoringCorrectionView proposalscoringViewObj;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	private ComboBox roomCategory;
	
	private ComboBox proposedroomCategory;
	
	private ComboBox treatmentType;
	
	private FormLayout covid19VariantFLayout;
	
	private FormLayout cocktailDrugFLayout;
	
	private HorizontalLayout covidVariantHLayout;
	
	private FormLayout actualcovid19VariantFLayout;
	
	private FormLayout actualCocktailDrugFLayout;
	
	private HorizontalLayout actualCovidVariantHLayout;
	
	private ComboBox covid19Vairant;
	
	private ComboBox cocktailDrug;
	
	private ComboBox actualCovid19Vairant;
	
	private ComboBox actualCocktailDrug;
		
	private Button btnproposedHospitalScroing;
	
	private Label labhospitalScoring;
	
	private TextArea correctionRemarks;
	
	@Inject
	private ViewDetails viewDetails;
	
	private Boolean scoringchanges =false;
	
	private Boolean ppChangesmade =false;
	
	@Inject
	private ScoringAndPPTabUI scoringAndPPTabUI;
	
	public Boolean getScoringchanges() {
		return scoringchanges;
	}

	@Override
	public void setScoringchanges(Boolean scoringchanges,List<HospitalScoringDTO> scoringDTOs,Boolean isPPChangesmade,Boolean ppCodingSelected,Map<String, Boolean> selectedPPCoding) {
		this.scoringchanges = scoringchanges;
		this.ppChangesmade = isPPChangesmade;
		this.bean.setIsScoringChanged(scoringchanges);
		this.bean.setScoringDTOs(scoringDTOs);
		this.bean.setIsPPChangesmade(isPPChangesmade);
		this.bean.setPpCodingSelected(ppCodingSelected);
		this.bean.setSelectedPPCoding(selectedPPCoding);
	}

	@Override
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData=referenceData;	
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<ProcessDataCorrectionDTO>(ProcessDataCorrectionDTO.class);
		this.binder.setItemDataSource(bean);
		
	}

	public void initView(ProcessDataCorrectionDTO bean) {
		
		this.bean=bean;
		this.newIntimationDTO = bean.getClaimDto().getNewIntimationDto();
		this.claimDTO = bean.getClaimDto();
		this.scoringchanges = false;
		initBinder();
		addStyleName("view");
		setSizeFull();
		fireViewEvent(DataCorrectionHistoricalPresenter.SET_TABLE_DATA_HISTORICAL,null);
		
		VerticalSplitPanel mainPanel = new VerticalSplitPanel();
		
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(newIntimationDTO,claimDTO,"DATA Validation Historical");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		covid19Vairant = new ComboBox("COVID-19 Variant");
		covid19Vairant.setWidth("180px");		
		covid19Vairant.setHeight("-1px");
		covid19Vairant.setCaptionAsHtml(true);
		//covid19Vairant.setEnabled(false);
		
		cocktailDrug = new ComboBox("Cocktail drug(Monoclonal Antibody) Approved");
		cocktailDrug.setWidth("180px");		
		cocktailDrug.setHeight("-1px");
		cocktailDrug.setCaptionAsHtml(true);
		//cocktailDrug.setEnabled(false);
		
		covid19VariantFLayout = new FormLayout(covid19Vairant);
		covid19VariantFLayout.setMargin(false);
		cocktailDrugFLayout = new FormLayout(cocktailDrug);
		cocktailDrugFLayout.setMargin(false);
		covidVariantHLayout = new HorizontalLayout(covid19Vairant,cocktailDrug);
		covidVariantHLayout.setWidth("60%");
		covidVariantHLayout.setMargin(true);
		
		actualCovid19Vairant = new ComboBox(" Actual COVID-19 Variant");
		actualCovid19Vairant.setWidth("180px");		
		actualCovid19Vairant.setHeight("-1px");
		actualCovid19Vairant.setCaptionAsHtml(true);
		actualCovid19Vairant.setEnabled(false);
		
		actualCocktailDrug = new ComboBox("Actual Cocktail drug(Monoclonal Antibody) Approved");
		actualCocktailDrug.setWidth("180px");		
		actualCocktailDrug.setHeight("-1px");
		actualCocktailDrug.setCaptionAsHtml(true);
		actualCocktailDrug.setEnabled(false);
		
		actualcovid19VariantFLayout = new FormLayout(actualCovid19Vairant);
		actualcovid19VariantFLayout.setMargin(false);
		actualCocktailDrugFLayout = new FormLayout(actualCocktailDrug);
		cocktailDrugFLayout.setMargin(false);
		actualCovidVariantHLayout = new HorizontalLayout(actualCovid19Vairant,actualCocktailDrug);
		covidVariantHLayout.setWidth("60%");
		covidVariantHLayout.setMargin(true);

		specialityCorrectionObj = specialityCorrectionInstance.get();
		specialityCorrectionObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		specialityCorrectionObj.setWidth("100%");
		specialityCorrectionObj.setReferenceData(referenceData);
		if(bean.getSpecialityCorrectionDTOs() !=null && !bean.getSpecialityCorrectionDTOs().isEmpty()){
			specialityCorrectionObj.addBeansToList(bean.getSpecialityCorrectionDTOs());
		}
		
		actualSpecialityObj = actualSpecialityInstance.get();
		actualSpecialityObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		actualSpecialityObj.setWidth("100%");
		actualSpecialityObj.setReferenceData(referenceData);
		
		diganosisCorrectionObj = diganosisCorrectionInstance.get();
		diganosisCorrectionObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		diganosisCorrectionObj.setWidth("100.0%");
		diganosisCorrectionObj.setReferenceData(referenceData);
		if(bean.getDiganosisCorrectionDTOs() !=null && !bean.getDiganosisCorrectionDTOs().isEmpty()){
			diganosisCorrectionObj.addBeansToList(bean.getDiganosisCorrectionDTOs());
		}

		actualdiganosisObj = actualdiganosisInstance.get();
		actualdiganosisObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		actualdiganosisObj.setWidth("100.0%");
		actualdiganosisObj.setReferenceData(referenceData);
		
		treatingCorrectionObj = treatingCorrectionInstance.get();
		treatingCorrectionObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		treatingCorrectionObj.setWidth("100.0%");
		if(bean.getTreatingCorrectionDTOs() !=null && !bean.getTreatingCorrectionDTOs().isEmpty()){
			treatingCorrectionObj.addBeansToList(bean.getTreatingCorrectionDTOs());
		}
		
		actualtreatingObj = actualtreatingInstance.get();
		actualtreatingObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
		actualtreatingObj.setWidth("100.0%");
		
		btnproposedHospitalScroing = new Button("Actual Hospital Scoring");
		
		labhospitalScoring = new Label("<b style = 'font-size: 14px;'> Hospital Scoring : </b>",ContentMode.HTML);
		labhospitalScoring.setWidth("-1px");
		labhospitalScoring.setHeight("-10px");
		
		btnproposedHospitalScroing.addStyleName(ValoTheme.BUTTON_LINK);
		btnproposedHospitalScroing.setWidth("-1px");
		btnproposedHospitalScroing.setHeight("-10px");
		
		correctionRemarks = new TextArea("Remarks");
		//premedicalRemarks.setHeight("50px");
		correctionRemarks.setNullRepresentation("");
		correctionRemarks.setWidth("350px");
		correctionRemarks.setHeight("80px");
		correctionRemarks.setMaxLength(4000);
		correctionRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		correctionRemarks.setRequired(true);
		SHAUtils.handleTextAreaPopupDetails(correctionRemarks,null,getUI(),SHAConstants.DATA_CORECTION);
		
		FormLayout correctionRemarksform =new FormLayout(correctionRemarks);
		HorizontalLayout remarkHLayout = new HorizontalLayout(correctionRemarksform);
		remarkHLayout.setMargin(true);
		remarkHLayout.setSpacing(true);
		
		getbuttonLayout();
		addListener();	
		
		HorizontalLayout scoringHLayout = new HorizontalLayout(labhospitalScoring,btnproposedHospitalScroing);
		scoringHLayout.setSpacing(true);
		scoringHLayout.setMargin(true);
		
		VerticalLayout tableLayout = null;
		if(bean.getTreatmentType() !=null && 
				bean.getTreatmentType().getValue() !=null && bean.getTreatmentType().getValue().equals("Surgical")){
					
			procedureCorrectionObj = procedureCorrectionInstance.get();
			procedureCorrectionObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
			procedureCorrectionObj.setWidth("100.0%");
			procedureCorrectionObj.setReferenceData(referenceData);
			if(bean.getProcedureCorrectionDTOs() !=null && !bean.getProcedureCorrectionDTOs().isEmpty()){
				procedureCorrectionObj.addBeansToList(bean.getProcedureCorrectionDTOs());
			}
			
			actualprocedureObj = actualprocedureInstance.get();
			actualprocedureObj.init(SHAConstants.DATA_VALIDATION_HISTORICAL);
			actualprocedureObj.setWidth("100.0%");
			actualprocedureObj.setReferenceData(referenceData);
			
			tableLayout = new VerticalLayout(/*specialityCorrectionObj,actualSpecialityObj,*/diganosisCorrectionObj,covidVariantHLayout,actualdiganosisObj,actualCovidVariantHLayout,procedureCorrectionObj,actualprocedureObj,treatingCorrectionObj,actualtreatingObj,scoringHLayout,remarkHLayout);
		}else{
			tableLayout = new VerticalLayout(specialityCorrectionObj,actualSpecialityObj,diganosisCorrectionObj,covidVariantHLayout,actualdiganosisObj,actualCovidVariantHLayout,treatingCorrectionObj,actualtreatingObj,scoringHLayout,remarkHLayout);
		}
		tableLayout.setSpacing(true);
		
		viewDetails.initView(newIntimationDTO.getIntimationId(),bean.getTransactionKey(), ViewLevels.PREAUTH_MEDICAL,"Process Claim Financials");
		
		verticalMain = new VerticalLayout(viewDetailsLayout(),commonTopLayout(),tableLayout,buttonHorLayout);
		verticalMain.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalMain.setSpacing(false);

		mainPanel.setSecondComponent(verticalMain);
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainPanel.setSizeFull();
		mainPanel.setHeight("670px");
		setCompositionRoot(mainPanel);
	}
	
	@SuppressWarnings("deprecation")
	public HorizontalLayout commonTopLayout(){
		
		treatmentType = new ComboBox("Treatment Type");
		treatmentType.setWidth("180px");		
		treatmentType.setTabIndex(1);
		treatmentType.setHeight("-1px");
		treatmentType.setCaptionAsHtml(true);
		
		FormLayout formLayout3 = new FormLayout(treatmentType);
		formLayout3.setSpacing(true);
		
		roomCategory = new ComboBox("Room Category");
		roomCategory.setWidth("180px");		
		roomCategory.setHeight("-1px");
		roomCategory.setCaptionAsHtml(true);
			
		proposedroomCategory = new ComboBox("Actual Room Category");
		proposedroomCategory.setWidth("180px");		
		proposedroomCategory.setHeight("-1px");
		proposedroomCategory.setCaptionAsHtml(true);
		
		addContainerNames();
		
		if(bean.getRoomCategory() !=null){
			roomCategory.setValue(bean.getRoomCategory());	
		}
		
		if(bean.getTreatmentType() !=null){
			treatmentType.setValue(bean.getTreatmentType());	
		}
		roomCategory.setReadOnly(true);
		treatmentType.setReadOnly(true);
		formLayout3.addComponent(roomCategory);
		formLayout3.setMargin(true);
		
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout formLayout4 = new FormLayout();
		formLayout4.setSpacing(true);
		formLayout4.addComponent(dummyField);
		formLayout4.addComponent(proposedroomCategory);
		formLayout4.setMargin(true);

		/*FormLayout formLayout1 = new FormLayout(roomCategory);
		formLayout1.setSpacing(true);
		FormLayout formLayout2 = new FormLayout(proposedroomCategory);
		formLayout2.setSpacing(true);*/
		HorizontalLayout HLayout1 = new HorizontalLayout(formLayout3,formLayout4);
		HLayout1.setSpacing(true);
		//HLayout1.setSizeFull();
		return HLayout1;
	}
	
	private void getbuttonLayout(){
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
		buttonHorLayout.setSpacing(true);
	}
	
	@SuppressWarnings("deprecation")
	private void addContainerNames() {
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("treatmentType");
		treatmentType.setContainerDataSource(procedure);
		treatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		treatmentType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> roomContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		roomContainer.addBean(bean.getRoomCategory());	
		roomCategory.setContainerDataSource(roomContainer);
		roomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		roomCategory.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> proposedroomContainer = (BeanItemContainer<SelectValue>) referenceData.get("roomCategory");
		proposedroomCategory.setContainerDataSource(proposedroomContainer);
		proposedroomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		proposedroomCategory.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> covid19Variantbean = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(bean.getCovid19Variant() !=null){
		covid19Variantbean.addBean(bean.getCovid19Variant());
		}
		covid19Vairant.setContainerDataSource(covid19Variantbean);
		covid19Vairant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		covid19Vairant.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> actualCovid19Variantbean = (BeanItemContainer<SelectValue>) referenceData.get("covid19Variant");	
		actualCovid19Vairant.setContainerDataSource(actualCovid19Variantbean);
		actualCovid19Vairant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actualCovid19Vairant.setItemCaptionPropertyId("value");	
		if(bean.getCovid19Variant() !=null){
			covid19Vairant.setValue(bean.getCovid19Variant());
		}
		
		BeanItemContainer<SelectValue> cocktailDrugsbean = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(bean.getCocktailDrug() !=null){
			cocktailDrugsbean.addBean(bean.getCocktailDrug());	
		}
		cocktailDrug.setContainerDataSource(cocktailDrugsbean);
		cocktailDrug.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cocktailDrug.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> actualCocktailDrugsbean = (BeanItemContainer<SelectValue>) referenceData.get("cocktailDrugs");		
		actualCocktailDrug.setContainerDataSource(actualCocktailDrugsbean);
		actualCocktailDrug.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actualCocktailDrug.setItemCaptionPropertyId("value");	
		if(bean.getCocktailDrug() !=null){
			cocktailDrug.setValue(bean.getCocktailDrug());
		}
		covid19Vairant.setEnabled(false);
		actualCovid19Vairant.setEnabled(false);
		cocktailDrug.setEnabled(false);
		actualCocktailDrug.setEnabled(false);
		
		
	}
	
	
	public void addListener() {

		submitBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(isvalid()){
					setvalue();
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					fireViewEvent(DataCorrectionHistoricalPresenter.SUBMIT_BUTTON_DATA_CODING_HISTORICAL,bean,userName);	
				}
					
			}
		});

		cancelBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
						"NO", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							dbCalculationService.dataCoadingHistoricalRelease(userName,null,"CANCEL",bean.getCoadingKey());
							fireViewEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION_HISTORICAL, true);
						} else {
							dialog.close();
						}
					}
				});
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
	
	private void setvalue(){
		
		if(proposedroomCategory.getValue() != null){
			SelectValue value = (SelectValue)proposedroomCategory.getValue();
			bean.setProposedroomCategory(value);
			if(value !=null && bean.getRoomCategory() !=null 
					&& !value.getId().equals(bean.getRoomCategory().getId())){
				bean.setIsroomCatChanged(true);
			}else if(value !=null && bean.getRoomCategory() == null){
				bean.setIsroomCatChanged(true);
			}
		}if(actualSpecialityObj !=null 
				&& actualSpecialityObj.getValues() != null && !actualSpecialityObj.getValues().isEmpty()){
			bean.setIsspecialityChanged(true);
			bean.setSpecialityCorrectionDTOs(actualSpecialityObj.getValues());			
		}if(actualdiganosisObj !=null && actualdiganosisObj.getValues() !=null
				&& !actualdiganosisObj.getValues().isEmpty()){
			bean.setIsdiganosisChanged(true);
			bean.setDiganosisCorrectionDTOs(actualdiganosisObj.getValues());
			if(actualCovid19Vairant.getValue() != null){
				SelectValue value = (SelectValue)actualCovid19Vairant.getValue();
				bean.setActualCovid19Variant(value);
				if(value !=null && bean.getCovid19Variant() !=null 
						&& !value.getId().equals(bean.getCovid19Variant().getId())){
					bean.setIsCovid19VariantChanged(true);
				}else if(value !=null && bean.getCovid19Variant() == null){
					bean.setIsCovid19VariantChanged(true);
				}
			}
			if(actualCocktailDrug.getValue() != null){
				SelectValue value = (SelectValue)actualCocktailDrug.getValue();
				bean.setActualCocktailDrug(value);
				if(value !=null && bean.getCocktailDrug() !=null 
						&& !value.getId().equals(bean.getCocktailDrug().getId())){
					bean.setIsCocktailDrugChanged(true);
				}else if(value !=null && bean.getCocktailDrug() == null){
					bean.setIsCocktailDrugChanged(true);
				}
			}
		}if(correctionRemarks !=null){
			if(correctionRemarks.getValue()!=null && !correctionRemarks.getValue().isEmpty()){
				bean.setRemarks(correctionRemarks.getValue());
			}
		}if(actualtreatingObj !=null && actualtreatingObj.getValues() !=null 
				&& !actualtreatingObj.getValues().isEmpty()){
			bean.setIstreatingChanged(true);
			bean.setTreatingCorrectionDTOs(actualtreatingObj.getValues());
		}if(bean.getTreatmentType() !=null && 
				bean.getTreatmentType().getValue() !=null && bean.getTreatmentType().getValue().equals("Surgical")){
			if(actualprocedureObj !=null && actualprocedureObj!=null 
					&& !actualprocedureObj.getValues().isEmpty()){
				bean.setIsprocedureChanged(true);
				bean.setProcedureCorrectionDTOs(actualprocedureObj.getValues());
			}
		}	
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}
	
public boolean isvalid(){
		
		boolean hasError =false;
		boolean haschanges =false;
		StringBuffer eMsg = new StringBuffer();
		SelectValue value = (SelectValue)proposedroomCategory.getValue();
		
		/*if(value !=null && bean !=null && bean.getRoomCategory() !=null
				&& !value.getId().equals(bean.getRoomCategory().getId())){
			haschanges =true;
		}else if(value !=null && bean.getRoomCategory() == null){
			haschanges =true;
		}if(!haschanges){
			if(actualSpecialityObj !=null 
					&& actualSpecialityObj.getValues() != null && !actualSpecialityObj.getValues().isEmpty()){
				haschanges =true;
			}
		}if(!haschanges){
			if(actualdiganosisObj !=null && actualdiganosisObj.getValues() !=null
					&& !actualdiganosisObj.getValues().isEmpty()){
				haschanges =true;
			}
		}if(!haschanges){
			if(actualprocedureObj !=null && actualprocedureObj!=null 
					&& !actualprocedureObj.getValues().isEmpty()){
				bean.setIsprocedureChanged(true);
				bean.setProcedureCorrectionDTOs(actualprocedureObj.getValues());
			}
		}if(!haschanges){
			haschanges = scoringchanges;
		}if(!haschanges){ 
			haschanges = ppChangesmade;
		}if(!haschanges){
			if(actualtreatingObj !=null && actualtreatingObj.getValues() !=null 
					&& !actualtreatingObj.getValues().isEmpty()){
				haschanges =true;
			}
		}*/
		
		if(correctionRemarks !=null && (correctionRemarks.getValue() == null
				|| correctionRemarks.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Remarks field is mandatory to proceed further </br>");
		}	 

		if(actualtreatingObj !=null){
			boolean isValid = this.actualtreatingObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.actualtreatingObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(actualSpecialityObj !=null){		
			boolean isValid = this.actualSpecialityObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.actualSpecialityObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(actualdiganosisObj !=null && actualdiganosisObj.getValues() !=null){
			List<DiganosisCorrectionDTO> diagnosisValuesList =  actualdiganosisObj.getValues();
			if(diagnosisValuesList != null){
				for (DiganosisCorrectionDTO diagnosisDetailsTableDTO : diagnosisValuesList) {
					if(diagnosisDetailsTableDTO != null && diagnosisDetailsTableDTO.getProposedicdCode() != null && diagnosisDetailsTableDTO.getProposedicdCode().getValue() != null){
						String insuranceDiagnosisCode= diagnosisDetailsTableDTO.getProposedicdCode().getValue();
						if(ReferenceTable.getCovid19VariantInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
							if(actualCovid19Vairant !=null && actualCovid19Vairant.getValue() == null){
								hasError = true;
								eMsg.append("Please Select Covid 19 Variant </br>");
							}
							if(actualCocktailDrug !=null && actualCocktailDrug.getValue() == null){
								hasError = true;
								eMsg.append("Please Select Cocktail drug(Monoclonal Antibody) Approved</br>");
							}
							break;

						}
					}
				}
			}
		}
		
		if (hasError) {
			
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();
			hasError = true;
			return !hasError;
		} 
		return true;
	}

@Override
public void generateCovid19VariantFields(Boolean covid19Variant) {
	if (covid19Variant) {
		actualCovid19Vairant.setEnabled(true);
		actualCovid19Vairant.setRequired(true);
		actualCocktailDrug.setEnabled(true);
		actualCocktailDrug.setRequired(true);
		
		BeanItemContainer<SelectValue> covid19Variantbean = (BeanItemContainer<SelectValue>) referenceData.get("covid19Variant");
		
		BeanItemContainer<SelectValue> cocktailDrugs = (BeanItemContainer<SelectValue>) referenceData.get("cocktailDrugs");
		
		actualCovid19Vairant.setContainerDataSource(covid19Variantbean);
		actualCovid19Vairant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actualCovid19Vairant.setItemCaptionPropertyId("value");
		
		actualCocktailDrug.setContainerDataSource(cocktailDrugs);
		actualCocktailDrug.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actualCocktailDrug.setItemCaptionPropertyId("value");

	} 
	else{
		int duplicateCovidCount = 0;
		List<DiganosisCorrectionDTO> diagnosisValuesList =  actualdiganosisObj.getValues();
		if(diagnosisValuesList != null){
			for (DiganosisCorrectionDTO diagnosisDetailsTableDTO : diagnosisValuesList) {
				if(diagnosisDetailsTableDTO != null && diagnosisDetailsTableDTO.getProposedicdCode() != null && diagnosisDetailsTableDTO.getProposedicdCode().getValue() != null){
					String insuranceDiagnosisCode= diagnosisDetailsTableDTO.getProposedicdCode().getValue();
					if(ReferenceTable.getCovid19VariantInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
						duplicateCovidCount = duplicateCovidCount+1;
					}
				}
			}
		}
		if(duplicateCovidCount == 0){

			actualCovid19Vairant.setEnabled(false);
			actualCovid19Vairant.setRequired(false);
			actualCocktailDrug.setEnabled(false);
			actualCocktailDrug.setRequired(false);
			actualCovid19Vairant.clear();
			actualCocktailDrug.clear();
		}
	}

}
	
	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {

		StringBuffer successLabel = new StringBuffer("Data validation Historical Record Saved Successfully.");
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(successLabel.toString())
			    .withOkButton(ButtonOption.caption("Data Validation Historical"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {	
				msgBox.close();
				fireViewEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION_HISTORICAL, true);
			}
		});
		
	}
	
	private HorizontalLayout viewDetailsLayout(){
		
		HorizontalLayout commonButtons = new HorizontalLayout(viewDetails);
		commonButtons.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButtons.setWidth("100%");
		commonButtons.setSpacing(false);
		
		return commonButtons;
	}
	
	public void showProposedScoringView(){
		/*proposalscoringViewObj.setDtoBean(bean);
		proposalscoringViewObj.setParentScoringButton(btnproposedHospitalScroing);
		proposalscoringViewObj.setScreenName("Data Validation Historical");	
		proposalscoringViewObj.setProposedScoring(true);
		proposalscoringViewObj.init(newIntimationDTO.getIntimationId(), true,false);*/
		
		scoringAndPPTabUI.setIntimationNumber(newIntimationDTO.getIntimationId());
		scoringAndPPTabUI.setScreenName("Data Validation Historical");
		scoringAndPPTabUI.init(true);
		
		VerticalLayout misLayout = new VerticalLayout(scoringAndPPTabUI);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		popup.setHeight("58%");
		popup.setContent(misLayout);
		popup.setClosable(false);
		popup.center();
		popup.setResizable(true);
		scoringAndPPTabUI.setPopupWindow(popup);
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
	
	@Override
	public void getcorrectionProcedureValues(BeanItemContainer<SelectValue> procedures) {
		actualSpecialityObj.setProcedure(procedures);
	}
	
	@Override
	public void addSpecialityEdited(SpecialityCorrectionDTO specialityCorrectionDTO) {
		actualSpecialityObj.addBeanToList(specialityCorrectionDTO);
	}
	
	@Override
	public void removeSpecialityEdited(Long Key) {
		specialityCorrectionObj.removeSpecialityEdited(Key);
	}
	
	@Override
	public void addSpecialityProcedure(Long specId,ComboBox prodcomboBox,SelectValue procValue) {
		actualSpecialityObj.addProcedureValues(specId,prodcomboBox,procValue);
	}

	@Override
	public void addDiganosisEdited(DiganosisCorrectionDTO diganosisCorrectionDTO) {
		actualdiganosisObj.addBeanToList(diganosisCorrectionDTO);
	}

	@Override
	public void deleteactualDiganosis(Long key) {
		diganosisCorrectionObj.removeDiganosisEdited(key);
	}

	@Override
	public void addProcedureEdited(ProcedureCorrectionDTO procedureCorrectionDTO) {
		actualprocedureObj.addBeanToList(procedureCorrectionDTO);
	}

	@Override
	public void deleteactualProcedure(Long key) {
		procedureCorrectionObj.removeProcedureEdited(key);
	}

	@Override
	public void addTreatingEdited(TreatingCorrectionDTO treatingCorrectionDTO) {
		actualtreatingObj.addBeanToList(treatingCorrectionDTO);
	}

	@Override
	public void deleteactualTreating(Long key) {
		treatingCorrectionObj.removeTreatingEdited(key);	
	}

	@Override
	 public void generatePPCoadingField(Boolean ischecked,List<PPCodingDTO> codingDTOs,Map<String,Boolean> selectedPPCoding) {
		 scoringAndPPTabUI.generatePPCoadingField(ischecked,codingDTOs,selectedPPCoding);
	 }
}
