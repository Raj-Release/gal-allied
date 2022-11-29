package com.shaic.claim.scoring.ppcoding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jfree.layout.FormatLayout;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;

public class PPCodingView extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout buttonHorlayout;
	
	private HorizontalLayout ppHorlayout;
	
	private HorizontalLayout ppDataHlayout;
	
	private FormLayout dummyFormLayout;
				
	private Button resetButton;
	
	private String intimationNumber;
	
	private String screenName;
	
	private PreauthDTO dtoBean;
	
	private OptionGroup ppCoding;
	
	private Map<String,Boolean> selectedPPCoding;
	
	private String hospitalType = "";
	
	private Intimation selectedIntimation;
	
	@EJB
	private PPCodingService codingService;

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}	

	public void setDtoBean(PreauthDTO dtoBean) {
		this.dtoBean = dtoBean;
	}

	public void setSelectedPPCoding(Map<String, Boolean> selectedPPCoding) {
		this.selectedPPCoding = selectedPPCoding;
	}

	public Map<String, Boolean> getSelectedPPCoding() {
		return selectedPPCoding;
	}


	private boolean isButtonVisible;

	@SuppressWarnings("deprecation")
	public void init(String argIntimationNumber, boolean isButtonVisible){
		
		intimationNumber = argIntimationNumber;
		this.isButtonVisible =  isButtonVisible;
		mainLayout = new VerticalLayout();
		buttonHorlayout = new HorizontalLayout();
		ppDataHlayout = new HorizontalLayout();

		selectedIntimation  = codingService.getIntimationByNo(intimationNumber);
		if(selectedIntimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			hospitalType = "Network";
		}else{
			hospitalType = "Non-Network";
		}
		
		ppCoding = new OptionGroup("Is the Policy Holders Health Protected?");
		ppCoding.addItems(getReadioButtonOptions());
		ppCoding.setItemCaption(true, "Yes");
		ppCoding.setItemCaption(false, "No");
		ppCoding.setStyleName("horizontal");
		ppCoding.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				if(event.getProperty() != null && event.getProperty().getValue() != null) {
					Boolean isChecked = (Boolean)event.getProperty().getValue();
					if(screenName.equals("Claim Request")){						
						fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_PP_GENERATE_EVENT,isChecked,selectedIntimation.getKey(),hospitalType);							
					}else if(screenName.equals("Data Validation")){						
						fireViewEvent(DataCorrectionPresenter.DC_PP_GENERATE_EVENT,isChecked,selectedIntimation.getKey(),hospitalType);							
					}else if(screenName.equals("Data Validation Historical")){						
						fireViewEvent(DataCorrectionHistoricalPresenter.DC_PP_GENERATE_EVENT_HIST,isChecked,selectedIntimation.getKey(),hospitalType);							
					}else if(screenName.equals(SHAConstants.DATA_VALIDATION_PRIORITY)){						
						fireViewEvent(DataCorrectionPriorityPresenter.DC_PP_GENERATE_EVENT_PRIORITY,isChecked,selectedIntimation.getKey(),hospitalType);							
					}
				}
			}
		});
		
		FormLayout ppForm = new FormLayout(ppCoding);
		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("100px");
		ppHorlayout = new HorizontalLayout(dummyForm,ppForm);
		ppHorlayout.setSpacing(false);
		resetButton = new Button("Reset");
		addListeners();

		buttonHorlayout.addComponent(resetButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setVisible(isButtonVisible);
		buttonHorlayout.setSpacing(true);
		buttonHorlayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);
				
		mainLayout.addComponent(ppHorlayout);
		mainLayout.addComponent(ppDataHlayout);
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(ppDataHlayout, Alignment.BOTTOM_LEFT);
		//mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
		setvalues();
	}
	
	
	public void addListeners(){		

		resetButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				resetPPDataComponents();
				if(ppCoding !=null){
					ppCoding.setValue(null);
					ppCoding.select(null);
				}
			}
		});
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@SuppressWarnings("deprecation")
	public void generatePPCoadingField(List<PPCodingDTO> codingDTOs,Boolean isChecked){
		
		if(isChecked){
			resetPPDataComponents();
		}else{
			if(codingDTOs !=null && !codingDTOs.isEmpty()){
				dummyFormLayout = new FormLayout();
				dummyFormLayout.setWidth("150px");
				FormLayout dataFormLayout = new FormLayout();
				dataFormLayout.setSpacing(true);
				for(PPCodingDTO codingDTO:codingDTOs){
					CheckBox checkBox = new CheckBox(codingDTO.getPpCodingDesc());
					checkBox.setData(codingDTO);
					if(selectedPPCoding != null && selectedPPCoding.get(codingDTO.getPpCode()) !=null
							&& selectedPPCoding.get(codingDTO.getPpCode()).equals(true)){
						checkBox.setValue(true);
					}
					checkBox.addListener(new Listener(){
						@Override
						public void componentEvent(Event event) {
							CheckBox component = (CheckBox) event.getComponent();
							PPCodingDTO codingDTO = (PPCodingDTO) component.getData();
							if(codingDTO.getPpCode() !=null && selectedPPCoding !=null){
								if(checkBox.getValue() == true){
									selectedPPCoding.put(codingDTO.getPpCode(), true);
								}else{
									selectedPPCoding.put(codingDTO.getPpCode(), false);
								}
							}
						}
					});
					dataFormLayout.addComponent(checkBox);
				}
				ppDataHlayout.addComponent(dummyFormLayout);
				ppDataHlayout.addComponent(dataFormLayout);
				ppDataHlayout.setVisible(true);
			}
		}
		
	}

	public Map<String,Boolean> getselectedPPCoding(){
		
		if(selectedPPCoding !=null){
			return selectedPPCoding;
		}
		return null;
	}
	
	public Boolean isppCodingSelected(){
		if(ppCoding !=null && ppCoding.getValue() !=null){
			Boolean isChecked = (Boolean)ppCoding.getValue();
			return isChecked;
		}
		return null;
	}
	
	public void savePPCoding(){
		codingService.savePPCoding(intimationNumber, screenName, selectedPPCoding, isppCodingSelected(), hospitalType);
	}
	
	private void setvalues(){
		Claim claim = codingService.getClaimByIntimationkey(selectedIntimation.getKey());
		if(claim !=null && claim.getPpProtected() !=null){
			if(claim.getPpProtected().equals("Y")){
				ppCoding.setValue(Boolean.valueOf("true"));
				ppCoding.select(Boolean.valueOf("true"));
			}else if(claim.getPpProtected().equals("N")){
				ppCoding.setValue(Boolean.valueOf("false"));
				ppCoding.select(Boolean.valueOf("false"));
			}
		}
	}
	
	private void resetPPDataComponents(){
		if (ppDataHlayout != null
				&& ppDataHlayout.getComponentCount() > 0) {
			ppDataHlayout.removeAllComponents();
		}
		ppDataHlayout.setVisible(false);
	}
}
