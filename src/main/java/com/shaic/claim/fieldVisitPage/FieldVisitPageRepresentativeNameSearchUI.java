package com.shaic.claim.fieldVisitPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reassignfieldVisitPage.ReAssignFieldVisitPresenter;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class FieldVisitPageRepresentativeNameSearchUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HospitalService hospitalService;

	private ComboBox cmbState;

	private ComboBox cmbCity;

	private ComboBox cmbAllocationTo;

	private ComboBox cmbBrachOffice;

	private Button btnSearch;

	private BeanFieldGroup<FieldVisitPageRepresentativeNameSearchDTO> binder;

	private VerticalLayout mainLayout;

	private HorizontalLayout horizontalLayout;

	private FormLayout allocationAndBrachOfficeLayout;
	
	private Intimation intimation;
	
	private Hospitals hospital;
	
	private String presenterString;
	
	private Boolean isSearch = false;
	
	@Inject
	private SearchRepresentativeNameSearchTable searchRepresentativeNameSearchTable;

	public void initBinder() {
		this.binder = new BeanFieldGroup<FieldVisitPageRepresentativeNameSearchDTO>(
				FieldVisitPageRepresentativeNameSearchDTO.class);
		this.binder
				.setItemDataSource(new FieldVisitPageRepresentativeNameSearchDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void initRepresentativeNameSearch(Intimation intimation, String presenterString) {
		initBinder();
		this.intimation = intimation;
		
		isSearch = false;
		
		this.presenterString = presenterString;
		
		Long hospitalKey = this.intimation.getHospital();
		
		Hospitals hospitals = hospitalService.searchbyHospitalKey(hospitalKey);
		
		this.hospital = hospitals;
		
		cmbState = binder.buildAndBind("State", "state", ComboBox.class);
//		cmbState.setRequired(true);
		cmbCity = binder.buildAndBind("City", "city", ComboBox.class);
		cmbCity.setVisible(true);
//		cmbCity.setRequired(true);
		cmbAllocationTo = binder.buildAndBind("Allocation to", "allocationTo",
				ComboBox.class);
		cmbAllocationTo.setVisible(false);
//		cmbAllocationTo.setRequired(true);
		cmbBrachOffice = binder.buildAndBind("Branch Office", "branch",
				ComboBox.class);
		
//		cmbBrachOffice.setRequired(true);
		cmbBrachOffice.setVisible(false);
		btnSearch = new Button("Search");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		allocationAndBrachOfficeLayout = new FormLayout(cmbAllocationTo,
				cmbBrachOffice);
		horizontalLayout = new HorizontalLayout(new FormLayout(cmbState),
				new FormLayout(cmbCity), allocationAndBrachOfficeLayout,
				btnSearch);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(btnSearch, Alignment.BOTTOM_RIGHT);
		searchRepresentativeNameSearchTable.init("", false, false);
		searchRepresentativeNameSearchTable.setPresenterString(presenterString);
		mainLayout = new VerticalLayout(horizontalLayout, searchRepresentativeNameSearchTable);
		mainLayout.setMargin(true);
		Panel mainPanel = new Panel();
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);
//		cmbCity.setEnabled(false);
//		cmbAllocationTo.setEnabled(false);
//		cmbBrachOffice.setEnabled(false);
		addListener();
		
		Map<String, Object>  searchRepresentative = new  HashMap<String, Object>();
		
		searchRepresentative.put("stateId", hospitals.getStateId());
		searchRepresentative.put("cityId", hospitals.getCityId());
		searchRepresentative.put("catgoryId", 0l);
		
		if(presenterString.equalsIgnoreCase(SHAConstants.ASSIGN_FVR)){
			fireViewEvent(FieldVisitPagePresenter.SEARCH_REPRESENTATIVE, searchRepresentative);
		}else if(presenterString.equalsIgnoreCase(SHAConstants.REASSIGN_FVR)){
			fireViewEvent(ReAssignFieldVisitPresenter.SEARCH_REPRESENTATIVE, searchRepresentative);
		}
		
		
	}

	private void addListener() {
		cmbState.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					SelectValue state = (SelectValue) cmbState.getValue();
					Long state_Id = state.getId();
					fireViewEvent(FieldVisitPagePresenter.SEARCH_CITY, state_Id);
				}catch(Exception e){
					cmbCity.setContainerDataSource(null);
//					cmbBrachOffice.setContainerDataSource(null);
//					cmbAllocationTo.setValue(null);
				}
			}
		});
		
		cmbCity.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
//					SelectValue state = (SelectValue) cmbState.getValue();
//					Long state_Id = state.getId();
//					SelectValue city = (SelectValue) cmbCity.getValue();
//					Long city_Id = city.getId();
//					fireViewEvent(PAFieldVisitPagePresenter.SEARCH_BRANCH_OFFICE, state_Id, city_Id);
				}catch(Exception e){
					
				}
			}
		});
		
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				    Long state_Id = 0l;
				    Long city_id = 0l;
				    Long category_Id = 0l;
				    
				    Map<String, Object>  searchRepresentative = new  HashMap<String, Object>();
				    
				    if(cmbState != null && cmbState.getValue() != null){
				    	SelectValue state = (SelectValue) cmbState.getValue();
				    	state_Id = state.getId();
				    	
				    }
				    
				    if(cmbCity != null && cmbCity.getValue() != null){
				    	SelectValue city = (SelectValue) cmbCity.getValue();
						city_id = city.getId();
						
				    }
//                    
//				    if(cmbAllocationTo != null && cmbAllocationTo.getValue() != null){
//				    	
//						SelectValue category = (SelectValue) cmbAllocationTo.getValue();
//
//						if(category != null){
//							category_Id = category.getId();
//							
//						}
//				    }
            
					searchRepresentative.put("stateId", state_Id);
					searchRepresentative.put("cityId", city_id);
					searchRepresentative.put("catgoryId", category_Id);
//					searchRepresentative.put("branchCode", branch_Id);
					isSearch = true;
					if(presenterString.equalsIgnoreCase(SHAConstants.ASSIGN_FVR)){
					fireViewEvent(FieldVisitPagePresenter.SEARCH_REPRESENTATIVE, searchRepresentative);
					}else if(presenterString.equalsIgnoreCase(SHAConstants.REASSIGN_FVR)){
						fireViewEvent(ReAssignFieldVisitPresenter.SEARCH_REPRESENTATIVE, searchRepresentative);
					}

				
			}
		});
	}
	
	public void setTableData(List<SearchRepresentativeTableDTO> tableData){
		if(tableData != null && !tableData.isEmpty()){
			
			searchRepresentativeNameSearchTable.setTableList(tableData);	
			
		}else{
			
			searchRepresentativeNameSearchTable.setTableList(tableData);
			
			if(isSearch){
			getErrorMessage("No Records Found");
			}
		}
		
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void setCityContainer(
			BeanItemContainer<SelectValue> citySelectValueContainer) {
		
		if (!citySelectValueContainer.getItemIds().isEmpty()) {
			cmbCity.setContainerDataSource(citySelectValueContainer);
			
			List<SelectValue> itemIds = citySelectValueContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(hospital != null && hospital.getCityId() != null && selectValue.getId().equals(hospital.getCityId())){
					cmbCity.setValue(selectValue);
					break;
				}
			}
			
			
			cmbBrachOffice.setContainerDataSource(null);
			cmbAllocationTo.setValue(null);
			cmbCity.setEnabled(true);
		}else{
			cmbBrachOffice.setContainerDataSource(null);
			cmbAllocationTo.setValue(null);
		}
	}
	
	public void setBranchContainer(BeanItemContainer<SelectValue> branchContainer){
		if(!branchContainer.getItemIds().isEmpty()){
			cmbBrachOffice.setContainerDataSource(branchContainer);
			cmbAllocationTo.setEnabled(true);
			cmbBrachOffice.setEnabled(true);
		}else{
			cmbBrachOffice.setContainerDataSource(null);
			cmbAllocationTo.setValue(null);			
		}
	}

	public void setBranchContainer() {

	}

	public void setReferenceDataForStateAndAllocationTo(
			BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer) {
		cmbState.setContainerDataSource(stateContainer);
		
		List<SelectValue> itemIds = stateContainer.getItemIds();
		
		for (SelectValue selectValue : itemIds) {
			if(hospital != null && hospital.getStateId() != null){
				if(selectValue.getId().equals(hospital.getStateId())){
					cmbState.setValue(selectValue);
					break;
				}
			}
		}
		
	
		cmbAllocationTo.setContainerDataSource(allocationToContainer);
	}
}
