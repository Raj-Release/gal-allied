package com.shaic.arch.test;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;

@SuppressWarnings("serial")
@ViewInterface(TestComponentView.class)
public class TestComponentPresenter extends AbstractMVPPresenter<TestComponentView> {

	@EJB
	private MasterService masterService;
	
	public static final String TEST_DIAGNOSIS_ADD_ITEM_COMBO = "test_add_new_item_to_combo";
	
	public static final String TEST_DIAGNOSIS_SEARCH_ITEM = "test_search_diagnosis";
	
	public void searchDiagnosis(@Observes @CDIEvent(TEST_DIAGNOSIS_SEARCH_ITEM) final ParameterDTO parameters)
	{
		String vlaue = (String) parameters.getPrimaryParameter(String.class);
		List<SelectValue> diagnosiSearch = masterService.getDiagnosisSelctValuesList(vlaue);
		view.showSearchResult(diagnosiSearch);
	}
	
	public void addNewDiagnosis(
			@Observes @CDIEvent(TEST_DIAGNOSIS_ADD_ITEM_COMBO) final ParameterDTO parameters) {
		String value = (String) parameters.getPrimaryParameter(String.class);
		SelectValue addDiagnosis = masterService.addDiagnosis(value);
		view.newItemAdded(addDiagnosis);
	}
	
	
	@Override
	public void viewEntered() {
		
	}
	
}
