package com.shaic.claim.enhacement.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.diagnosisexclusioncheck.view.ViewDiagnosisExclusionCheckDTO;
import com.shaic.claim.preauth.diagnosisexclusioncheck.view.ViewDiagnosisExclusionCheckService;
import com.shaic.claim.preauth.diagnosisexclusioncheck.view.ViewDiagnosisExclusionCheckTable;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationService;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTable;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTableDTO;
import com.shaic.claim.preauth.procedurelist.view.ProcedureListService;
import com.shaic.claim.preauth.procedurelist.view.ProcedureListTable;
import com.shaic.claim.preauth.view.ViewPreAuthCmbDTO;
import com.shaic.claim.preauth.view.ViewPreAuthDetails;
import com.shaic.claim.preauth.view.ViewPreAuthDetailsDTO;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckService;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckTable;
import com.shaic.claim.status.view.ViewStatusService;
import com.shaic.claim.status.view.ViewStatusTable;
import com.shaic.domain.PreauthService;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPreviousPreAuthDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ViewPreAuthDetails viewPreAuthDetails;

	@Inject
	private ProcedureListTable procedureListTable;

	@Inject
	private ProcedureListService procedureListService;

	@Inject
	private ViewPedValidationTable viewPedValidationTable;

	@Inject
	private ViewPedValidationService viewPedValidationService;

	@Inject
	private ViewDiagnosisExclusionCheckTable viewDiagnosisExclusionCheckTable;

	@Inject
	private ViewDiagnosisExclusionCheckService viewDiagnosisExclusionCheckService;

	@Inject
	private ViewProcedureExclusionCheckTable viewProcedureExclusionCheckTable;

	@Inject
	private ViewProcedureExclusionCheckService viewProcedureExclusionService;

	@Inject
	private ViewStatusTable viewStatusTable;

	@Inject
	private ViewStatusService viewStatusService;

	@Inject
	private PreauthService viewpreAuthService;

	private BeanFieldGroup<ViewPreAuthDetailsDTO> binder;

	private BeanFieldGroup<ViewPreAuthCmbDTO> cmbBinder;

	private VerticalLayout mainLayout;

	private ComboBox cmbPreAuthReference;

	private TextField txtRemarks;

	private TextField txtReplaceForIllness;

	private void unbindField(Field<?> field) {
		if (field != null && field.getValue() == null) {
			this.binder.unbind(field);
		}
	}

	public void init(Long preAuthKey) {
		mainLayout = new VerticalLayout();
		// setting combo box
		initBinder();
		List<Preauth> preAuthList = viewpreAuthService.getPreauthByKey(preAuthKey);
		cmbPreAuthReference = cmbBinder.buildAndBind("Pre-au th Reference No",
				"preReferenceId", ComboBox.class);

		txtRemarks = binder.buildAndBind("Remarks", "remarks", TextField.class);

		txtReplaceForIllness = binder.buildAndBind("Relapse of Illness",
				"replaceOfIlleness", TextField.class);
		
		BeanItemContainer<SelectValue> preAuthReference = getPreAuthIds(preAuthList);
		if (preAuthReference.getItemIds().size() > 0) {
			cmbPreAuthReference
					.setContainerDataSource(getPreAuthIds(preAuthList));
		} else {
			cmbPreAuthReference.addItem("No PreAuth References Found");
			cmbPreAuthReference.setNullSelectionAllowed(false);
			cmbPreAuthReference.setWidth("220px");
		}

		cmbPreAuthReference.setItemCaption(ItemCaptionMode.PROPERTY, "value");
		
		//setting the current claim preauth id to the combo box
		if(!preAuthList.isEmpty()){
			Collection<?> itemIds = cmbPreAuthReference.getContainerDataSource().getItemIds();
			cmbPreAuthReference.setValue(itemIds.toArray()[itemIds.size()-1]);
			cmbPreAuthReference.setNullSelectionAllowed(false);
			cmbPreAuthReference.setEnabled(false);
			mainLayout.addComponent(cmbPreAuthReference);			
			mainLayout.addComponent(buildwithFirstPreAuthId(preAuthList));
		}
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout buildwithFirstPreAuthId(List<Preauth> preAuthList) {		
		VerticalLayout defaultLayout = new VerticalLayout();
		defaultLayout.removeAllComponents();
		defaultLayout.addComponent(cmbPreAuthReference);
		// setting Pre-Auth details
		defaultLayout.addComponent(new Label("Pre-auth Details"));
		defaultLayout.addComponent(viewPreAuthDetails.init(preAuthList.get(0)
				.getKey()));
		// setting procedure list table
		procedureListTable.init("Procedure List", false, false);
		// procedureListTable.setTableList(procedureListService.search("027"));
		procedureListTable.setTableList(procedureListService.search(preAuthList
				.get(0).getKey()));
		procedureListTable.setHeight("20%");
		// setting ped validation table
		viewPedValidationTable.init("Ped Validation", false, false);
		List<ViewPedValidationTableDTO> ViewPedValidationTableDTO = viewPedValidationService
				.search(preAuthList.get(0).getKey());
		viewPedValidationTable.setTableList(ViewPedValidationTableDTO);
		// setting diagnosis check table
		viewDiagnosisExclusionCheckTable.init("Diagnosis Exclusion Check",
				false, false);
		List<ViewDiagnosisExclusionCheckDTO> viewDiagnosisExclusionCheckDTO = viewDiagnosisExclusionCheckService
				.search(preAuthList.get(0).getKey());
		viewDiagnosisExclusionCheckTable
				.setTableList(viewDiagnosisExclusionCheckService
						.getDiagnosisName(viewDiagnosisExclusionCheckDTO));

		// setting procedure exclusion table
		viewProcedureExclusionCheckTable.init("Procedure Exclusion Check",
				false, false);
		viewProcedureExclusionCheckTable
				.setTableList(viewProcedureExclusionService.search(preAuthList
						.get(0).getKey()));
		// setting status table
		viewStatusTable.init("Status", false, false);
		viewStatusTable.setTableList(viewStatusService.search(preAuthList
				.get(0).getKey()));
		defaultLayout.addComponent(procedureListTable);
		defaultLayout.addComponent(buildLayout());
		defaultLayout.addComponent(viewPedValidationTable);
		defaultLayout.addComponent(viewDiagnosisExclusionCheckTable);
		defaultLayout.addComponent(viewProcedureExclusionCheckTable);
		defaultLayout.addComponent(viewStatusTable);
		return defaultLayout;
	}

	public FormLayout buildLayout() {

		String strRelapseFlag = viewPreAuthDetails.viewPreAuthDetailsDTO
				.getReplaceOfIlleness();
		txtReplaceForIllness.setValue(strRelapseFlag);
		txtReplaceForIllness.setEnabled(false);

		String strReplaceForIllness = viewPreAuthDetails.viewPreAuthDetailsDTO
				.getRemarks();
		txtRemarks.setValue(strReplaceForIllness);
		txtRemarks.setEnabled(false);
		if (strRelapseFlag.equalsIgnoreCase("No")
				|| strRelapseFlag.equalsIgnoreCase("N")) {
			txtRemarks.setVisible(false);
		} else {
			txtRemarks.setVisible(true);
		}
		return new FormLayout(txtReplaceForIllness, txtRemarks);
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewPreAuthDetailsDTO>(
				ViewPreAuthDetailsDTO.class);
		this.binder.setItemDataSource(new ViewPreAuthDetailsDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		// for combo box

		this.cmbBinder = new BeanFieldGroup<ViewPreAuthCmbDTO>(
				ViewPreAuthCmbDTO.class);
		this.cmbBinder.setItemDataSource(new ViewPreAuthCmbDTO());
		this.cmbBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private BeanItemContainer<SelectValue> getPreAuthIds(
			List<Preauth> preauthList) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!preauthList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (Preauth preAuth : preauthList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(preAuth.getKey().longValue());
				selectValue.setValue(preAuth.getPreauthId());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

}
