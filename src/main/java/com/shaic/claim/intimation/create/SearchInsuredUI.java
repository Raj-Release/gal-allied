package com.shaic.claim.intimation.create;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.utils.StarIntimationUtils;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredSearchBean;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.pages.IntimationDetailsPage;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
public class SearchInsuredUI extends ViewComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = -5426174099053475085L;

	@Inject
	private InsuredService insuredService;

	@Inject
	private MasterService masterService;

	@Inject
//	@IntimationGroup
	private NewIntimationDto bean;
	
	private FieldGroup binder;
	
	private BeanFieldGroup<InsuredSearchBean> searchFieldGroup;

	public static BeanItemContainer<InsuredSearchBean> tmpInsuredSearchBeanContainer;

	private Button resetButton;

	private Button searchButton;

	private ComboBox cmbGender;

	private TextField agetextField;

	private PopupDateField dateofBirthpopupDateField;

	private TextField insuredNametextField;
	
	private TextField employeeIdTxt;
	
	private TextField healthCardNumberTxt;

	private Policy policy;

	private Table table;
	
	private VerticalLayout layout;
	
	private GridLayout gridLayout;

	private IndexedContainer iContainer;
	
	private IntimationDetailsPage intimationPage;

	public SearchInsuredUI() {
		
	}
	public void initView(Policy policy)
	{
		this.policy = policy;
	}
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	@PostConstruct	
	public void initView() {
		setCaption("Search Insured");

		setHeight("700px");
		setWidth("850px");

		searchFieldGroup = new BeanFieldGroup<InsuredSearchBean>(
				InsuredSearchBean.class);
		searchFieldGroup.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		searchFieldGroup.setItemDataSource(new InsuredSearchBean());
		
		

		setCompositionRoot(buildLayout());
	}

	private VerticalLayout buildLayout() {
		layout = new VerticalLayout();

		//Vaadin8-setImmediate() layout.setImmediate(false);
		layout.setWidth("100.0%");
		layout.setHeight("-1px");
		layout.setMargin(true);
		layout.setSpacing(true);

		layout.setCaption("Insured List");

		HorizontalLayout buildSearchPanel = buildSearchPanel();
		
		layout.addComponent(buildSearchPanel);
		layout.setComponentAlignment(buildSearchPanel, Alignment.TOP_LEFT);

		this.iContainer = createContainer();
		this.table = createTable(iContainer);
		
		layout.addComponent(table);
		layout.setComponentAlignment(table, Alignment.BOTTOM_LEFT);
		return layout;
	}

	private HorizontalLayout buildSearchPanel() {
		HorizontalLayout hLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() hLayout.setImmediate(false);
		hLayout.setWidth("90%");
		hLayout.setHeight("-1px");
		hLayout.setMargin(true);
		hLayout.setSpacing(true);
		gridLayout = buildSearchLayout();
		hLayout.addComponent(gridLayout);
		hLayout.setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);

		return hLayout;
	}

	private GridLayout buildSearchLayout() {

		gridLayout = new GridLayout(2, 4);
		
		gridLayout.setWidth("70%");
		gridLayout.setSpacing(true);
		
		insuredNametextField = searchFieldGroup.buildAndBind("Insured ??Name",
				"insuredName", TextField.class);
		cmbGender = searchFieldGroup.buildAndBind("Gender", "gender",
				ComboBox.class);
		dateofBirthpopupDateField = searchFieldGroup.buildAndBind(
				"Date Of Birth", "dateofbirth", PopupDateField.class);
		agetextField = searchFieldGroup.buildAndBind("Age", "age",
				TextField.class);
		cmbGender.setContainerDataSource(masterService
				.getSelectValueContainer(ReferenceTable.GENDER));

		agetextField.setNullRepresentation("");

		//TODO
		employeeIdTxt = searchFieldGroup.buildAndBind("Employee Id No", "employeeId",TextField.class);
//		employeeIdTxt = new TextField("Employee ID No");
		
		
		//TODO
		healthCardNumberTxt = searchFieldGroup.buildAndBind("Health Card No", "healthCardNumber",TextField.class);
//		healthCardNumberTxt = new TextField("Health Card No");
		
		gridLayout.addComponent(insuredNametextField);
		gridLayout.addComponent(dateofBirthpopupDateField);
		gridLayout.addComponent(agetextField);
		gridLayout.addComponent(cmbGender);
		gridLayout.addComponent(employeeIdTxt);
		gridLayout.addComponent(healthCardNumberTxt);
		
		gridLayout.setColumnExpandRatio(0, 0.5f);
		gridLayout.setColumnExpandRatio(2, 0.5f);
		
		searchButton = new Button("Search");
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		searchButton.setWidth("-1px");
		searchButton.setHeight("-1px");

		resetButton = new Button();
		resetButton.setCaption("Reset");
		//Vaadin8-setImmediate() resetButton.setImmediate(true);
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		
		HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		
		gridLayout.addComponent(buttonLayout, 0,3, 1,3);
		gridLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		addListener();
		return gridLayout;
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
					table.removeAllItems();
					searchFieldGroup.commit();
					tmpInsuredSearchBeanContainer = new BeanItemContainer<InsuredSearchBean>(InsuredSearchBean.class);
					tmpInsuredSearchBeanContainer.addBean(searchFieldGroup.getItemDataSource().getBean());
					if (cmbGender.getValue() != null
							|| agetextField.getValue() != null
							|| dateofBirthpopupDateField.getValue() != null
							|| insuredNametextField.getValue() != null
							|| healthCardNumberTxt.getValue() != null )

					{

						if (tmpInsuredSearchBeanContainer.getItemIds().size() != 0) {
							InsuredSearchBean intimationBean = tmpInsuredSearchBeanContainer
									.getItemIds().get(0);
							// fieldGroup.setItemDataSource(intimationBean);

							BeanItemContainer<Insured> searchResult = insuredService
									.search(intimationBean, policy);
							if (searchResult.size() != 0) {
								for (Insured insured : searchResult
										.getItemIds()) {
									System.out.println("tmpInsured :"
											+ insured);
									addItem(table, iContainer, insured);
								}
							} else {
//								Notification
//										.show("ERROR",
//												"record is not found for the given search criteria",
//												Notification.TYPE_HUMANIZED_MESSAGE);
								showErrorMessage("record is not found for the given search criteria");
							}
						}
					} else {
//						Notification
//								.show("ERROR",
//										"Please enter at least one input parameters for search.",
//										Notification.TYPE_HUMANIZED_MESSAGE);
						
						showErrorMessage("Please enter at least one input parameters for search.");
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


		table.setColumnHeader("insuredName", "Insured Name");
		table.setColumnHeader("employeeId","Employee ID No");
		table.setColumnHeader("healthCardNumber","Health Card No");
		table.setColumnHeader("insuredDateOfBirth", "Date of Birth");
		table.setColumnHeader("insuredAge", "Age");
		table.setColumnHeader("insuredGender", "Gender");
		

		table.addContainerProperty("key", Long.class, null);
		table.addContainerProperty("insuredName", String.class, null);
		table.addContainerProperty("employeeId",String.class, null);
		table.addContainerProperty("healthCardNumber",String.class, null);
		table.addContainerProperty("insuredGender", String.class, null);
		table.addContainerProperty("insuredDateOfBirth", String.class, null);
		table.addContainerProperty("insuredAge", String.class, null);
		table.setContainerDataSource(container);

		table.setVisibleColumns(new Object[] { "insuredName","employeeId","healthCardNumber", "insuredGender",
				"insuredDateOfBirth", "insuredAge", "Select" });

		table.setPageLength(table.size());
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("key", Long.class, null);
		container.addContainerProperty("insuredName", String.class, null);
		container.addContainerProperty("employeeId",String.class,null);
		container.addContainerProperty("healthCardNumber",String.class, null);
		container.addContainerProperty("insuredGender", String.class, null);
		container.addContainerProperty("insuredDateOfBirth", String.class, null);
		container.addContainerProperty("insuredAge", String.class, null);
		container.addContainerProperty("Select", Button.class, null);
		return container;
	}

	public Table createTable(IndexedContainer container) {
		final BeanItemContainer<MastersValue> masterValue = masterService
				.getMasterValue(ReferenceTable.RELATIONSHIP);
		final Table table = new Table("Insured List");
		columnMapper(table, container);
		table.setWidth("100%");
		table.setHeight("-1");
		table.setPageLength(5);
		table.setEditable(false);
		return table;
	}

	@SuppressWarnings("serial")
	public void addItem(final Table table, final IndexedContainer container,
			final Insured insured) {
		Object itemId = container.addItem();
		container.getItem(itemId).getItemProperty("key")
				.setValue(insured.getKey());
		container.getItem(itemId).getItemProperty("insuredName")
				.setValue(insured.getInsuredName());
		//TODO Need to set employee Id
		container.getItem(itemId).getItemProperty("employeeId")
		.setValue(" ");
		container.getItem(itemId).getItemProperty("healthCardNumber")
		.setValue(insured.getHealthCardNumber());
		
		container.getItem(itemId).getItemProperty("insuredGender")
				.setValue(insured.getInsuredGender().getValue());
		container.getItem(itemId).getItemProperty("insuredDateOfBirth")
				.setValue(new SimpleDateFormat("dd-MM-yyy").format(insured.getInsuredDateOfBirth()));
		container.getItem(itemId).getItemProperty("insuredAge")
				.setValue(String.valueOf(insured.getInsuredAge().intValue()));

		Button btnSelect = new Button("Select");
		
		HashMap<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("insured", insured);
		objectMap.put("intimation", this.intimationPage);
		btnSelect.setData(objectMap);
		
		btnSelect.addClickListener(new ClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				
				HashMap<String, Object> objectMap =  (HashMap<String, Object>) event.getButton().getData();
				Insured insured =  (Insured) objectMap.get("insured");
				fireViewEvent(IntimationDetailsPage.INSURED_SELECTED, insured);
				
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
		if(gridLayout != null) {
			StarIntimationUtils.resetAlltheValues(gridLayout);
			table.removeAllItems();
			searchFieldGroup.setItemDataSource(new InsuredSearchBean());
		}
	}
	
 	public void setParent(IntimationDetailsPage parent) {
        if (parent != null) {
            this.intimationPage = parent;
        } 
    }
}
