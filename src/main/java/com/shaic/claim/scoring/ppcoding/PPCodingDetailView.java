package com.shaic.claim.scoring.ppcoding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;

public class PPCodingDetailView extends ViewComponent{
	


	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout buttonHorlayout;
	
	private HorizontalLayout ppHorlayout;
	
	private HorizontalLayout ppDataHlayout;
	
	private FormLayout dummyFormLayout;
				
	private Button resetButton;
	
	private String intimationNumber;
	
	private String screenName;
		
	private OptionGroup ppCoding;
	
	private Map<String,Boolean> selectedPPCoding;
	
	private String hospitalType = "";
	
	private Intimation selectedIntimation;
	
	private Boolean isoldppscoreview = false;
		
	@EJB
	private PPCodingService codingService;

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}	

	public void setSelectedPPCoding(Map<String, Boolean> selectedPPCoding) {
		this.selectedPPCoding = selectedPPCoding;
	}

	public Map<String, Boolean> getSelectedPPCoding() {
		return selectedPPCoding;
	}

	public Boolean getIsoldppscoreview() {
		return isoldppscoreview;
	}

	public void setIsoldppscoreview(Boolean isoldppscoreview) {
		this.isoldppscoreview = isoldppscoreview;
	}

	private boolean isButtonVisible;

	@SuppressWarnings("deprecation")
	public void init(String argIntimationNumber, boolean isButtonVisible){
		
		intimationNumber = argIntimationNumber;
		this.isButtonVisible =  isButtonVisible;
		mainLayout = new VerticalLayout();
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
		ppCoding.setEnabled(false);
		
		FormLayout ppForm = new FormLayout(ppCoding);
		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("100px");
		ppHorlayout = new HorizontalLayout(dummyForm,ppForm);
				
		mainLayout.addComponent(ppHorlayout);
		mainLayout.addComponent(ppDataHlayout);
		mainLayout.setComponentAlignment(ppDataHlayout, Alignment.BOTTOM_LEFT);
		if(getIsoldppscoreview()){
			setOldvalues();
		}else{
			setvalues();
		}
		setCompositionRoot(mainLayout);
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@SuppressWarnings("deprecation")
	private void generatePPCoadingField(Boolean isChecked){
		
		if(isChecked){
			if (ppDataHlayout != null
					&& ppDataHlayout.getComponentCount() > 0) {
				ppDataHlayout.removeAllComponents();
			}
			ppDataHlayout.setVisible(false);
		}else{
			List<PPCodingDTO> codingDTOs = codingService.populatePPCoding(hospitalType);
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
					checkBox.setReadOnly(true);
					dataFormLayout.addComponent(checkBox);
				}
				ppDataHlayout.addComponent(dummyFormLayout);
				ppDataHlayout.addComponent(dataFormLayout);
				ppDataHlayout.setVisible(true);
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void setvalues(){
		Claim claim = codingService.getClaimByIntimationkey(selectedIntimation.getKey());
		if(claim !=null && claim.getPpProtected() !=null){
			selectedPPCoding = codingService.getPPCodingValues(selectedIntimation.getKey(), hospitalType);
			if(claim.getPpProtected().equals("Y")){
				ppCoding.setValue(Boolean.valueOf("true"));
				ppCoding.select(Boolean.valueOf("true"));
				generatePPCoadingField(true);
			}else if(claim.getPpProtected().equals("N")){
				ppCoding.setValue(Boolean.valueOf("false"));
				ppCoding.select(Boolean.valueOf("false"));
				generatePPCoadingField(false);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setOldvalues(){
		Claim claim = codingService.getClaimByIntimationkey(selectedIntimation.getKey());
		if(claim !=null && claim.getDcppFlag() !=null 
				&& claim.getDcppFlag().equals("Y") ){
			if(claim.getOldppProtected() !=null){
				if(claim.getOldppProtected().equals("N") 
						&& claim.getPpProtected().equals("Y")){
					selectedPPCoding = codingService.getdeletedoldPPCodingValues(selectedIntimation.getKey(), hospitalType,"Data Validation");
				}else{
					selectedPPCoding = codingService.getoldPPCodingValues(selectedIntimation.getKey(), hospitalType);
				}
				if(claim.getOldppProtected().equals("Y")){
					ppCoding.setValue(Boolean.valueOf("true"));
					ppCoding.select(Boolean.valueOf("true"));
					generatePPCoadingField(true);
				}else if(claim.getOldppProtected().equals("N")){
					ppCoding.setValue(Boolean.valueOf("false"));
					ppCoding.select(Boolean.valueOf("false"));
					generatePPCoadingField(false);
				}		
			}
		}else{
			setvalues();
		}
	}


}
