package com.shaic.claim.registration;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class ViewHospitalDetails extends Window {

	private static final long serialVersionUID = -6677087629747499658L;

	private Intimation intimation;
	private TextField city;
	private TextField name;
	private TextField state;
	private TextArea remark;
	private TextField pincode;
	private TextField hospitalScore;
	private TextField phoneNumber;
	private TextField hospitalCode;
	private AbsoluteLayout mainLayout;
	private FormLayout formLayoutLeft;
	private TextField hospitalCategory;
	private FormLayout formLayoutRight;
	private TextField representativeName;
	private TextField doorApartmentNumber;
	private TextField roomCategoryTextField;
	private TextField authorizedRepresentative;
	private HorizontalLayout hospitalDetailshorizontal;

	@EJB
	private IntimationService intimationService;
	@EJB
	private HospitalService hospitalService;
	@EJB
	private DBCalculationService dbService;
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;
	
	Button viewMBPackageReatesButton;
	
	private GridLayout grid;

	@PostConstruct
	public void initView() {

		buildMainLayout();
		setWidth("1000px");
		setHeight("400px");
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(mainLayout);
		setCaption("Hospital Details");

	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				if(c instanceof TextField){
					TextField field = (TextField) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}else if(c instanceof TextArea){
					
					TextArea field = (TextArea) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				
			}
		}
	}

	private void bindFieldGroupValue(String intimationNo) {
		setReadOnly(formLayoutLeft, false);
		setReadOnly(formLayoutRight, false);
		intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		roomCategoryTextField
				.setValue(intimation.getRoomCategory() != null ? intimation
						.getRoomCategory().getValue() : "");
		Hospitals hospitals = hospitalService.getHospitalById(intimation
				.getHospital());
		FieldGroup binder = new FieldGroup();
		BeanItem<Hospitals> item = new BeanItem<Hospitals>(hospitals);
		binder.setItemDataSource(item);
		binder.bindMemberFields(this);
		doorApartmentNumber.setValue(hospitals.getAddress());
		if(hospitals.getHospitalCode() != null) {
		Map<String,Object> hospitalScorePoints = dbService.getHospitalScorePoints(hospitals.getHospitalCode()); 
		hospitalScore.setValue(hospitalScorePoints.get(SHAConstants.HOSPITAL_SCORE_POINTS).toString());
		}
		if(hospitals.getMediBuddy() != null && hospitals.getMediBuddy().equals(1)){
			if(viewMBPackageReatesButton != null){
				grid.removeComponent(viewMBPackageReatesButton);
				grid.addComponent(viewMBPackageReatesButton, 1, 0);	
			}
			
		}else{
			if(viewMBPackageReatesButton != null){
				grid.removeComponent(viewMBPackageReatesButton);
			}
		}
		setReadOnly(formLayoutLeft, true);
		setReadOnly(formLayoutRight, true);
	}

	public ViewHospitalDetails() {

		// TODO add user code here
	}

	private AbsoluteLayout buildMainLayout() {

		mainLayout = new AbsoluteLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		setWidth("100.0%");
		setHeight("100.0%");

		hospitalDetailshorizontal = new HorizontalLayout();
		hospitalDetailshorizontal.setWidth("100.0%");
		hospitalDetailshorizontal.setHeight("100.0%");

		formLayoutLeft = new FormLayout();
		formLayoutLeft.setWidth("100.0%");
		formLayoutLeft.setMargin(true);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.addStyleName("layoutDesign");	

		formLayoutRight = new FormLayout();
		formLayoutRight.setWidth("100.0%");
		formLayoutRight.setMargin(true);
		formLayoutRight.setSpacing(true);
		formLayoutRight.addStyleName("layoutDesign");

		name = new TextField();
		name.setCaption("Hospital Name");
		formLayoutLeft.addComponent(name);

		phoneNumber = new TextField();
		phoneNumber.setCaption("Hospital Ph No");
		formLayoutLeft.addComponent(phoneNumber);

		authorizedRepresentative = new TextField();
		authorizedRepresentative.setCaption("Authorized Representative");
		formLayoutLeft.addComponent(authorizedRepresentative);

		representativeName = new TextField();
		representativeName.setCaption("Name of Representative");
		formLayoutLeft.addComponent(representativeName);

		hospitalCategory = new TextField();
		hospitalCategory.setCaption("Hospital Category");
		formLayoutLeft.addComponent(hospitalCategory);

		roomCategoryTextField = new TextField();
		roomCategoryTextField.setCaption("Room Category");
		formLayoutLeft.addComponent(roomCategoryTextField);

		remark = new TextArea();
		remark.setCaption("Remarks");
		formLayoutLeft.addComponent(remark);
		
			
		hospitalDetailshorizontal.addComponent(formLayoutLeft);
		hospitalDetailshorizontal.setExpandRatio(formLayoutLeft, 1.0f);

		hospitalCode = new TextField();
		hospitalCode.setCaption("Hospital Code");
		formLayoutRight.addComponent(hospitalCode);
		

		doorApartmentNumber = new TextField();
		doorApartmentNumber.setCaption("Address ");
		
		formLayoutRight.addComponent(doorApartmentNumber);

//		buildingName = new TextField();
//		buildingName.setCaption("Address 2");
//		formLayoutRight.addComponent(buildingName);
//
//		streetName = new TextField();
//		streetName.setCaption("Address 3");
//		formLayoutRight.addComponent(streetName);

		city = new TextField();
		city.setCaption("City");
		formLayoutRight.addComponent(city);

		state = new TextField();
		state.setCaption("State");
		formLayoutRight.addComponent(state);

		pincode = new TextField();
		pincode.setCaption("PinCode");
		formLayoutRight.addComponent(pincode);
		
		hospitalScore = new TextField();
		hospitalScore.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospitalScore.setCaption("Hospital Scoring");
		
		
		Button viewPackageReatesButton = new Button(
				"View Package Rates");

		viewPackageReatesButton
				.addClickListener(new Button.ClickListener() {
					public void buttonClick(
							ClickEvent event) {
		Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
		
		if(null != intimation.getPolicy().getPolicyNumber() && hospitals != null){
			
			Boolean isPaayasPlicy = intimationService.getPaayasPolicyDetails(intimation.getPolicy().getPolicyNumber());
		
			if(null != isPaayasPlicy && isPaayasPlicy){
				pdfPageUI.init(hospitals);
				if(pdfPageUI.isAttached()){
					pdfPageUI.detach();
				}
				UI.getCurrent().addWindow(pdfPageUI);
			
			}else if(hospitals.getHospitalCode() != null){
				BPMClientContext bpmClientContext = new BPMClientContext();
				String url = bpmClientContext.getHospitalPackageDetails() + hospitals.getHospitalCode();
				//getUI().getPage().open(url, "_blank");
				getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
			}
		}
		
		
		
//		if(packageRatesDto != null){
//		ReportDto reportDto = new ReportDto();
//		reportDto.setClaimId(intimation.getIntimationId());
//		List<HospitalPackageRatesDto> beanList = new ArrayList<HospitalPackageRatesDto>();
//		beanList.add(packageRatesDto);
//		reportDto.setBeanList(beanList);
//		DocumentGenerator docGen = new DocumentGenerator();
//		String fileUrl = docGen.generatePdfDocument("HospitalPackageRates", reportDto);
//		openPdfFileInWindow(fileUrl);
//		}
//		else{
//			showErrorPopup("Package Not Available for the selected Hospital");
//		}
	   }
					
	});
		
				viewPackageReatesButton.addStyleName("link");
				
				viewMBPackageReatesButton = new Button(
						"View MB Package Rates");
				
				viewMBPackageReatesButton
						.addClickListener(new Button.ClickListener() {
							public void buttonClick(
									ClickEvent event) {
				Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
				
				if(null != intimation.getPolicy().getPolicyNumber() && hospitals != null){
					
					Boolean isPaayasPlicy = intimationService.getPaayasPolicyDetails(intimation.getPolicy().getPolicyNumber());
				
					if(null != isPaayasPlicy && isPaayasPlicy){
						pdfPageUI.init(hospitals);
						if(pdfPageUI.isAttached()){
							pdfPageUI.detach();
						}
						UI.getCurrent().addWindow(pdfPageUI);
					
					}else if(hospitals.getHospitalCode() != null){
						BPMClientContext bpmClientContext = new BPMClientContext();
						String url = bpmClientContext.getMediBuddyPackageDetails() + hospitals.getHospitalCode();
						//getUI().getPage().open(url, "_blank");
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
					}
				}
				
			   }
							
			});
				
				viewMBPackageReatesButton.addStyleName("link");			
				
				
				HorizontalLayout hospitalScoreAndPackage = new HorizontalLayout();
				hospitalScoreAndPackage.addComponent(hospitalScore);
				hospitalScoreAndPackage.addComponent(viewPackageReatesButton);
				hospitalScoreAndPackage.setSpacing(false);
				formLayoutRight.addComponent(hospitalScore);
				grid = new GridLayout(2,1);
				grid.addComponent(viewPackageReatesButton);
				
				formLayoutRight.addComponent(grid);
		hospitalDetailshorizontal.addComponent(formLayoutRight);
		hospitalDetailshorizontal.setExpandRatio(formLayoutRight, 1.0f);
		
		
		mainLayout.addComponent(hospitalDetailshorizontal);
					
		return mainLayout;
	}

	public void showValues(String intimationNumber) {

		bindFieldGroupValue(intimationNumber);
	}
	private void openPdfFileInWindow(final String filepath) {
		
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Hospital Package Rate");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	}
	public void showErrorPopup(String eMsg)
	{
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
	}
}
