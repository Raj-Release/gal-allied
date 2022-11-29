/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.StarIntimationUtils;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.HospitalService;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narasimhaj
 *
 */
public class SearchOPHospitalContactDetails extends ViewComponent{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */


	@Inject
	private HospitalService hospitalService;
	
	private FieldGroup binder;
	
	private BeanFieldGroup<HospitalDto> searchFieldGroup;

	public static BeanItemContainer<HospitalDto> tmpInsuredSearchBeanContainer;

	private Button resetButton;

	private Button searchButton;

	//private ComboBox cmbGender;

	//private PopupDateField dateofBirthpopupDateField;
	
	private TextField hospitalPhoneNumberField;
	private TextField hospitalPinCodeField;
	private TextField hospitalCodeField;

	@Inject
	private SearchOPHospitalContactTable resultTable;
	
	private VerticalLayout layout;
	
	private FormLayout formLayout;
	
	private VerticalLayout mainVerticalLayout;

	
	private NewIntimationDto newIntimationDto;
	
	private ConsultationTabPage consultationPage;
	
	private Long hospitalTypeId = 0l;

	public SearchOPHospitalContactDetails() {
		
	}

	public void initView(NewIntimationDto bean,Long hospitalTypeId) {
		newIntimationDto = bean;
		this.hospitalTypeId = hospitalTypeId;
		setCaption("Search Hospitals");
		newIntimationDto = bean;
		searchFieldGroup = new BeanFieldGroup<HospitalDto>(
				HospitalDto.class);
		searchFieldGroup.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		searchFieldGroup.setItemDataSource(new HospitalDto());
		
		

		setCompositionRoot(buildLayout());
	}

	private VerticalLayout buildLayout() {
		layout = new VerticalLayout();
		layout.setWidth("100.0%");
		layout.setHeight("-1px");
		layout.setMargin(true);
		layout.setSpacing(true);

		layout.setCaption("Hospital List");

		HorizontalLayout buildSearchPanel = buildSearchPanel();
		
		layout.addComponent(buildSearchPanel);
		layout.setComponentAlignment(buildSearchPanel, Alignment.TOP_LEFT);
		return layout;
	}

	private HorizontalLayout buildSearchPanel() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("90%");
		hLayout.setHeight("-1px");
		hLayout.setMargin(true);
		hLayout.setSpacing(true);
		mainVerticalLayout = buildSearchLayout();
		hLayout.addComponent(mainVerticalLayout);
		hLayout.setComponentAlignment(mainVerticalLayout, Alignment.MIDDLE_CENTER);

		return hLayout;
	}

	private VerticalLayout buildSearchLayout() {

		formLayout = new FormLayout();
		
		formLayout.setWidth("70%");
		formLayout.setSpacing(true);
		
		hospitalPhoneNumberField = searchFieldGroup.buildAndBind("Hospital Phone No",
				"phoneNumber", TextField.class);
		hospitalPinCodeField = searchFieldGroup.buildAndBind("Hospital PinCode","pincode",TextField.class);
		hospitalCodeField = searchFieldGroup.buildAndBind("Hospital Code","hospitalCode",TextField.class);
		
		CSValidator validator = new CSValidator();
		validator.extend(hospitalPinCodeField);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		//Vaadin8-setImmediate() hospitalPinCodeField.setImmediate(true);

		formLayout.addComponent(hospitalPhoneNumberField);
		formLayout.addComponent(hospitalPinCodeField);
		formLayout.addComponent(hospitalCodeField);

		searchButton = new Button("Search");
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");
		searchButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		resetButton = new Button();
		resetButton.setCaption("Reset");
		//Vaadin8-setImmediate() resetButton.setImmediate(true);
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		resetButton.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		
		mainVerticalLayout = new VerticalLayout(formLayout,buttonLayout);
		mainVerticalLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		addListener();
		return mainVerticalLayout;
	}

	private void addListener() {
		resetButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				resetSearchIntimationFields();
			}
		});
		// searchButton


		searchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5914033995530670042L;

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					resultTable.removeAllItems();
					searchFieldGroup.commit();
					tmpInsuredSearchBeanContainer = new BeanItemContainer<HospitalDto>(HospitalDto.class);
					tmpInsuredSearchBeanContainer.addBean(searchFieldGroup.getItemDataSource().getBean());
					//if (true)
					//{

						if (tmpInsuredSearchBeanContainer.getItemIds().size() != 0) {
							HospitalDto intimationBean = tmpInsuredSearchBeanContainer
									.getItemIds().get(0);
							List<UnFreezHospitals> hospitalList=hospitalService.searchOPHospitalPhoneNo(intimationBean.getPhoneNumber(),intimationBean.getPincode(),intimationBean.getHospitalCode(),hospitalTypeId);

							if (hospitalList.size() != 0) {
								layout.removeComponent(resultTable);
								resultTable.init("", false, false);
								resultTable.setColumnHeader();
								resultTable.removeAllItems();
								layout.addComponent(resultTable);
								layout.setComponentAlignment(resultTable, Alignment.BOTTOM_LEFT);
								Long i = 1l;
								for (UnFreezHospitals hospitals : hospitalList) {
									HospitalDto hospitalDto = new HospitalDto(hospitals);
									resultTable.addBeanToList(hospitalDto);
								}
//								resultTable.setTablesize();
							} else {
//								Notification
//										.show("ERROR",
//												"record is not found for the given search criteria",
//												Notification.TYPE_HUMANIZED_MESSAGE);
								showErrorMessage("record is not found for the given search criteria");
							}
						}


				} catch (CommitException e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	private void showErrorMessage(String eMsg) {
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

	private void columnMapper(Table table, IndexedContainer container) {


		table.setColumnHeader("name", "Hospital Name");
		table.setColumnHeader("address","Hospital Address");
		table.setColumnHeader("phoneNumber","Hospital Ph No");
		table.setColumnHeader("fax", "Hospital Fax");

		table.addContainerProperty("key", Long.class, null);
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("address",String.class, null);
		table.addContainerProperty("phoneNumber",String.class, null);
		table.addContainerProperty("fax", String.class, null);
		
		
		table.setContainerDataSource(container);

		table.setVisibleColumns(new Object[] { "name","address","phoneNumber", "fax","Select" });

		table.setPageLength(table.size());
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("name", String.class, null);
		container.addContainerProperty("address",String.class,null);
		container.addContainerProperty("phoneNumber",String.class, null);
		container.addContainerProperty("fax", String.class, null);
		return container;
	}

	@SuppressWarnings("serial")
	public void addItem(final Table table, final IndexedContainer container,
			final UnFreezHospitals hospital) {
		Object itemId = container.addItem();
		container.getItem(itemId).getItemProperty("key")
				.setValue(hospital.getKey());
		container.getItem(itemId).getItemProperty("name")
				.setValue(hospital.getName());
		//TODO Need to set employee Id
		container.getItem(itemId).getItemProperty("address")
		.setValue(hospital.getAddress());
		container.getItem(itemId).getItemProperty("phoneNumber")
		.setValue(hospital.getPhoneNumber());
		
		container.getItem(itemId).getItemProperty("fax")
				.setValue(hospital.getFax());
		

		Button btnSelect = new Button("Select");
		
		HashMap<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("hospitals", hospital);
		objectMap.put("intimation", this.consultationPage);
		btnSelect.setData(objectMap);
		
		btnSelect.addClickListener(new ClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				
				HashMap<String, Object> objectMap =  (HashMap<String, Object>) event.getButton().getData();
				UnFreezHospitals hospital =  (UnFreezHospitals) objectMap.get("hospitals");
				fireViewEvent(ConsultationTabPage.HOSPITAL_SELECTED, hospital);
				
//				fireViewEvent(IntimationDetailPage.INSURED_SELECTED, insured);
//				IntimationDetailPage intimationDetailsPage =  (IntimationDetailPage) objectMap.get("intimation");
//				if(intimationDetailsPage != null) {
//					Collection<?> itemIds = intimationDetailsPage.cmbInsuredPatiend.getContainerDataSource().getItemIds();
//					intimationDetailsPage.cmbInsuredPatiend.setValue(insured);
//				}
			}
		});
		container.getItem(itemId).getItemProperty("Select").setValue(btnSelect);
		btnSelect.addStyleName("link");
	}

	public void resetSearchIntimationFields() {
		if(formLayout != null) {
			StarIntimationUtils.resetAlltheValues(formLayout);
			resultTable.removeAllItems();
			searchFieldGroup.setItemDataSource(new HospitalDto());
		}
	}
	
 	public void setParent(ConsultationTabPage parent) {
        if (parent != null) {
            this.consultationPage = parent;
        } 
    }

}
