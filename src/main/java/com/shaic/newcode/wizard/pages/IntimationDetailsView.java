package com.shaic.newcode.wizard.pages;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface IntimationDetailsView extends GMVPView  {
	public void showIntimationDetails();
	public NewIntimationDto getModel();
	public void buildNewBornBabyTable(Boolean isNewBorn,Map<String,Object> referenceData);
	public void setModel(NewIntimationDto newIntimationDto);
	public void initView(NewIntimationDto newIntimationDto);
	public void setReferenceData(Map<String,Object> referenceData);
	public void cancelIntimation();
	public void showFailurePanel(GalaxyIntimation intimation);
	public void showSuccessPanel(GalaxyIntimation intimation);
	public void setRegisteredHospital(UnFreezHospitals hospital);
	public void handleSave(GalaxyIntimation intimation);
//	public void setEditHospitalHumanTask(HumanTask editHospitalHumanTask);
	public void addedReasonForAdmission(SelectValue selectValue);	
	public void intializeReasonForAdmission(BeanItemContainer<SelectValue> selectValue);
	public void setSelectedInsured(GmcMainMemberList insured, List<GmcMainMemberList> dependentRisk);
	public void showFailure(String string);
	
}
