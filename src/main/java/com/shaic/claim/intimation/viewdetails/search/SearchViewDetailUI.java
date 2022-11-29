package com.shaic.claim.intimation.viewdetails.search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.intimation.search.SearchIntimationPresenter;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@CDIView
public class SearchViewDetailUI extends ViewComponent implements View {
	
	@EJB
	IntimationService intimationservice;

	@EJB
	MasterService masterService;

	@EJB
	PolicyService policyService;

	@EJB
	ClaimService claimService;

//	@EJB
//	HospitalService hospitalService;

	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;
	
	@Inject
	private NewIntimationService newIntimationService;

	@Inject
	private ClaimStatusDto claimStatusDto;
	
	private List<Component> mandatoryFields = new ArrayList<Component>();

	private Panel searchPanel;

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	private VerticalLayout verticalLayout_1;
	
	private AbsoluteLayout btnAbsLayout;

//	private GridLayout gridLayout_1;

	private GridLayout searchGridLayout;

	private Button resetButton;

	private Button searchButton;

	private PopupDateField endDateField;

	private ComboBox yearComboBox;

	private TextField hospitalName;

	private PopupDateField fromDateField;

	private TextField intimationSequenceNumber;

	private TextField insuredName;

	private TextField policyNumber;

	private TextField healthCardNumber;
	
	private TextField claimNumber;

	private TextField intimationNumber;

	private ComboBox cmbIntimationStatus;

	private Label intimationStatuslbl;

	private Label intimationNumberlbl;

	private Label claimNumberlbl;

	private AbstractComponent policyNumberlbl;

	private Label healthCardNumberlbl;

	private AbstractComponent insuredNamelbl;

	private Label intimationSequenceNumberlbl;

	private AbstractComponent fromDateFieldlbl;

	private AbstractComponent yearComboBoxlbl;

	private Label hospitalNamelbl;

	private Label endDatebl;

	private VerticalLayout searchTableLayout;
	
	private FormLayout leftForm;
	
	private FormLayout rightForm;
	
	private HorizontalLayout mainHorizantal;

//	private PagedTable searchIntimationTable;

	private static final long serialVersionUID = 1L;
	
	private SearchIntimationFormDto searchIntimationFormDto;
	
	@Inject
	protected SearchViewDetailIntimationTable searchViewDetailResultTable;

	@PostConstruct
	public void init() {

		setSizeFull();
//		mainPanel.setWidth("100.0%");
		// mainPanel.m
		setHeight("650px");
		// searchPanel = buildMainLayout();
		searchPanel = buildMainLayout();
		mainPanel.setFirstComponent(searchPanel);
		mainPanel.setSplitPosition(54);
		mainPanel.setSizeFull();
		
//		searchViewDetailResultTable.setSizeFull();
		searchViewDetailResultTable.init("", false, true);
//		searchViewDetailResultTable.getPageable().setPageNumber(1);
//		searchViewDetailResultTable.addSearchListener(this);
		
//		mainPanel.setSecondComponent(searchViewDetailResultTable);
		
		
		setCompositionRoot(mainPanel);

	}
	
	
	public VerticalSplitPanel getMainPanel(){
		return mainPanel;
	}

	public void buildSearchIntimationTable(

	Page<NewIntimationDto> newIntimationDtoPagedContainer) {

		if (newIntimationDtoPagedContainer != null) {
			
			MastersValue selectedIntimStatus = null;
			if (cmbIntimationStatus.getValue() != null)
				selectedIntimStatus = (MastersValue) cmbIntimationStatus
						.getValue();

			if (selectedIntimStatus != null){
					if(StringUtils.contains(selectedIntimStatus.getValue(),
							"Submitted")) {
						searchViewDetailResultTable.setSubmitTableHeader();
						searchViewDetailResultTable.addClaimNumberColum();
//						searchViewDetailResultTable.addViewIntimationDetailsColumn();
					}
					else if(!StringUtils.contains(selectedIntimStatus.getValue(), "Submitted")){
						searchViewDetailResultTable.setDraftTableHeader();
						searchViewDetailResultTable.addEditColumn();				
					}
			}	
			
			searchTableLayout = new VerticalLayout(searchViewDetailResultTable);
//			searchTableLayout = new VerticalLayout(searchViewDetailResultTable);
				

			searchTableLayout.setWidth("100%");
			mainPanel.setSecondComponent(searchViewDetailResultTable);
			
//			searchIntimationTable = new PagedTable();
//			searchIntimationTable.setPageLength(10);
//			searchIntimationTable.setWidth(100, Unit.PERCENTAGE);
//			searchIntimationTable.setRowHeaderMode(Table.ROW_HEADER_MODE_INDEX);;
//			newIntimationDtoContainer
//					.addNestedContainerProperty("policy.policyNumber");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("insuredPatient.healthCardNumber");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("insuredPatientName");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("intimaterName");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("hospitalDto.name");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("modeOfIntimation.value");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("intimatedBy.value");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("callerContactNum");
//			newIntimationDtoContainer
//					.addNestedContainerProperty("status.processValue");
//
//			searchIntimationTable
//					.setContainerDataSource(newIntimationDtoContainer);
//			Object ColumHeaderForDraftIntimation[] = new Object[] {
//					"policy.policyNumber", "insuredPatient.healthCardNumber",
//					"insuredPatientName", "intimaterName", "hospitalDto.name",
//					"modeOfIntimation.value", "intimatedBy.value",
//					"status.processValue", "callerContactNum" };
//
//			MastersValue selectedIntimStatus = null;
//			if (cmbIntimationStatus.getValue() != null)
//				selectedIntimStatus = (MastersValue) cmbIntimationStatus
//						.getValue();
//
//			if (selectedIntimStatus != null
//					&& StringUtils.contains(selectedIntimStatus.getValue(),
//							"Submitted")) {
//				searchIntimationTable.addGeneratedColumn("claimNumber",
//						new Table.ColumnGenerator() {
//							@Override
//							public Object generateCell(Table source,
//									final Object itemId, Object columnId) {
//								String claimNum = "";
//
//								Claim a_claim = claimService
//										.getClaimforIntimation(((NewIntimationDto) itemId)
//												.getKey());
//
//								if (a_claim != null && a_claim.getClaimId() != null) {
//									claimNum = a_claim.getClaimId();
//								}
//								Label claimNumber = new Label(claimNum);
//								// source.getContainerDataSource().addItem(claimNumber);
//								return claimNumber;
//							}
//						});
//			}
//
//			Object ColumHeaderForSubmitIntimation[] = new Object[] {
//					"intimationId", "policy.policyNumber", "claimNumber",
//					"insuredPatient.healthCardNumber", "insuredPatientName",
//					"intimaterName", "hospitalDto.name",
//					"modeOfIntimation.value", "intimatedBy.value",
//					"status.processValue", "callerContactNum" };

//			if (selectedIntimStatus != null) {
//				if (StringUtils.contains(selectedIntimStatus.getValue(),
//						"Submitted")) {
//					searchIntimationTable
//							.setVisibleColumns(ColumHeaderForSubmitIntimation);
//					searchIntimationTable.setColumnHeader("intimationId",
//							"Intimation Number");
//				} else if (!StringUtils.contains(
//						selectedIntimStatus.getValue(), "Submitted")) {
//					searchIntimationTable
//							.setVisibleColumns(ColumHeaderForDraftIntimation);
//				}
//			}

//			searchIntimationTable.setColumnHeader("policy.policyNumber",
//					"Policy Number");
//			if (StringUtils.contains(selectedIntimStatus.getValue(),
//					"Submitted")) {
//				searchIntimationTable.setColumnHeader("claimNumber",
//						"Claim Number");
//			}
//			searchIntimationTable.setColumnHeader(
//					"insuredPatient.healthCardNumber", "Health Card Number");
//			searchIntimationTable.setColumnHeader("insuredPatientName",
//					"Insured Patient Name");
//			searchIntimationTable.setColumnHeader("intimaterName",
//					"Intimator Name");
//			searchIntimationTable.setColumnHeader("hospitalDto.name",
//					"Hospital Name");
//			searchIntimationTable.setColumnHeader("modeOfIntimation.value",
//					"Intimation Mode");
//			searchIntimationTable.setColumnHeader("intimatedBy.value",
//					"Intimated By");
//			searchIntimationTable.setColumnHeader("status.processValue",
//					"Status");
////			searchIntimationTable.setColumnHeader("createdDate",
////					"Intimation Date");
//			searchIntimationTable.setColumnHeader("callerContactNum",
//					"Contact No");
//			
//			searchIntimationTable.addGeneratedColumn("Intimation Date",
//					new Table.ColumnGenerator() {
//						@Override
//						public Object generateCell(Table source,
//								final Object itemId, Object columnId) {
//							if(((NewIntimationDto)itemId).getCreatedDate() != null){
//						        return  new SimpleDateFormat("dd/MM/yyyy").format(((NewIntimationDto)itemId).getCreatedDate()); 
//						}else{
//						                return "";
//						}						 
//						}
//						});
//			
//
//			if (selectedIntimStatus != null) {
//				if (!StringUtils.contains(selectedIntimStatus.getValue(),
//						"Submitted")) {
//
//					searchIntimationTable.addGeneratedColumn("Edit Intimation",
//							new Table.ColumnGenerator() {
//								@Override
//								public Object generateCell(Table source,
//										final Object itemId, Object columnId) {
//									final Button viewIntimationDetailsButton = new Button(
//											"View / Edit Intimation");
//
//									viewIntimationDetailsButton.setData(itemId);
//									viewIntimationDetailsButton
//											.addClickListener(new Button.ClickListener() {
//												public void buttonClick(
//														ClickEvent event) {
//													NewIntimationDto newIntimationDto = (NewIntimationDto) event
//															.getButton()
//															.getData();
//													// fireViewEvent(MenuItemBean.EDIT_INTIMATION,
//													// newIntimationDto);
//													fireViewEvent(
//															MenuItemBean.REVISED_EDIT_INTIMATION,
//															newIntimationDto);
//
//												}
//											});
//									viewIntimationDetailsButton
//											.addStyleName(BaseTheme.BUTTON_LINK);
//									return viewIntimationDetailsButton;
//								}
//							});
//				}
//
//				if (StringUtils.equalsIgnoreCase(
//						selectedIntimStatus.getValue(), "Submitted")) {
//					searchIntimationTable.addGeneratedColumn("Action",
//							new Table.ColumnGenerator() {
//								@Override
//								public Object generateCell(final Table source,
//										final Object itemId, Object columnId) {
//
//									final Button viewIntimationDetailsButton = new Button();
//									viewIntimationDetailsButton.setData(itemId);
//									Long intimationKey = ((NewIntimationDto) itemId)
//											.getKey();
//
//									Claim a_claim = claimService
//											.getClaimforIntimation(intimationKey);
//
//									if (a_claim != null
//											&& a_claim.getClaimId() != null)
//
//									{
//										viewIntimationDetailsButton
//												.setCaption("View Claim Status");
//									} else {
//										viewIntimationDetailsButton
//												.setCaption("View Intimation Details");
//
//									}
//									viewIntimationDetailsButton
//											.addClickListener(new Button.ClickListener() {
//												public void buttonClick(
//														ClickEvent event) {
//
//													NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;
//
//													Claim a_claim = claimService
//															.getClaimforIntimation(newIntimationDto
//																	.getKey());
//													TmpCPUCode CPUMaster = null;
//
//													if (newIntimationDto
//															.getHospitalDto() != null) {
//
//														CPUMaster = policyService
//																.getTmpCpuCode(newIntimationDto
//																		.getHospitalDto()
//																		.getCpuId());
//													}
//
//													if (!ValidatorUtils
//															.isNull(CPUMaster)) {
//														newIntimationDto
//																.setCpuCode(CPUMaster
//																		.getCpuCode());
//													}
//
//													Intimation a_intimation = intimationservice
//															.getIntimationByKey(newIntimationDto
//																	.getKey());
//
//													NewIntimationDto a_intimationsDto = intimationservice
//															.getIntimationDto(a_intimation);
//
//													if (newIntimationDto
//															.getStatus() != null
//															&& newIntimationDto
//																	.getStatus()
//																	.getProcessValue()
//																	.equalsIgnoreCase(
//																			"Submitted")
//															&& a_claim != null
//															&& a_claim
//																	.getClaimId() != null) {
//														ClaimDto claimdto = claimService
//																.claimToClaimDTO(a_claim);
//														if (claimdto != null) {
//															claimStatusDto
//																	.setClaimDto(claimdto);
//															claimStatusDto
//																	.setNewIntimationDto(claimdto
//																			.getNewIntimationDto());
//															/*ViewIntimationStatus viewIntimationStatus = new ViewIntimationStatus(
//																	claimStatusDto,
//																	false,
//																	cashLessTableDetails,
//																	cashlessTable,
//																	cashLessTableMapper,
//																	newIntimationService,
//																	a_intimation);
//															
//															UI.getCurrent()
//																	.addWindow(
//																			viewIntimationStatus);*/
//															viewDetails.viewClaimStatusUpdated(a_intimation.getIntimationId());
//														}
//													} else if (a_claim == null) {
//														/*ViewIntimation viewIntimation = new ViewIntimation(
//																a_intimationsDto,
//																hospitalService);*/
//														/*UI.getCurrent()
//																.addWindow(
//																		viewIntimation);*/
//														viewDetails.getViewIntimation(a_intimation.getIntimationId());
//													}
//												}
//											});
//									viewIntimationDetailsButton
//											.addStyleName(BaseTheme.BUTTON_LINK);
//									return viewIntimationDetailsButton;
//								}
//							});
//				}
//			} else {
//				// Status is NULL
//			}
//			searchIntimationTable.setWidth(searchIntimationTable.getWidth(),
//					Unit.PERCENTAGE);
//			searchIntimationTable.setHeight("200px");
//			searchIntimationTable.setPageLength(searchIntimationTable
//					.getItemIds().size() > 10 ? 10 : searchIntimationTable
//					.getItemIds().size());
			
//			if (searchTableLayout != null) {
//				// verticalLayout_1.removeComponent(searchTableLayout);
//				mainPanel.removeComponent(searchTableLayout);
//			} else {
//				searchTableLayout = null;
//			}
//
//			searchTableLayout = new VerticalLayout(
//					searchIntimationTable.createControls(),
//					searchIntimationTable);
//			// searchTableLayout.setHeight("238px");
//			searchTableLayout.setWidth("100%");
//			// verticalLayout_1.addComponent(searchTableLayout);
//			mainPanel.setSecondComponent(searchTableLayout);
		}

		else {
			// empty result

			clearsearchViewDetailResultTable();
		}
	}

	public void hideSearchFields(String valChanged) {
		if (StringUtils.equalsIgnoreCase(valChanged, "Draft")) {
			intimationNumber.setValue("");
			claimNumber.setValue("");
			intimationSequenceNumber.setValue("");
			yearComboBox.setValue("");

			intimationNumber.setEnabled(false);
			claimNumber.setEnabled(false);
			intimationSequenceNumber.setEnabled(false);
			//yearComboBox.setEnabled(false);

			// For Hiding
			// intimationNumberlbl.setVisible(false);
			// claimNumberlbl.setVisible(false);
			// intimationSequenceNumberlbl.setVisible(false);
			// yearComboBoxlbl.setVisible(false);
			//
			// intimationNumber.setVisible(false);
			// claimNumber.setVisible(false);
			// intimationSequenceNumber.setVisible(false);
			// yearComboBox.setVisible(false);

		} else {

			intimationNumber.setEnabled(true);
			claimNumber.setEnabled(true);
			intimationSequenceNumber.setEnabled(true);
//			yearComboBox.setEnabled(true);

			// For unHiding
			// intimationNumberlbl.setVisible(true);
			// claimNumberlbl.setVisible(true);
			// intimationSequenceNumberlbl.setVisible(true);
			// yearComboBoxlbl.setVisible(true);
			//
			// intimationNumber.setVisible(true);
			// claimNumber.setVisible(true);
			// intimationSequenceNumber.setVisible(true);
			// yearComboBox.setVisible(true);
		}

	}

	private Panel buildMainLayout() {
		searchPanel = buildSearchPanel();
		searchPanel.setWidth("100%");
		searchPanel.addStyleName("g-search-panel");
		return searchPanel;
	}

	private Panel buildSearchPanel() {

		// common part: create layout
		searchPanel = new Panel();
		searchPanel.setCaption("Intimation Search - View Details");

		//Vaadin8-setImmediate() searchPanel.setImmediate(false);
		// searchPanel.setWidth("100.0%");
		/*
		 * searchPanel.setWidth("700px"); searchPanel.setHeight("50%");
		 */
		searchPanel.setWidth("90.0%");
		searchPanel.setHeight("350px");
		searchPanel.addStyleName("panelHeader");
		// verticalLayout_1
		verticalLayout_1 = buildVerticalLayout_1();

		searchPanel.setContent(verticalLayout_1);

		return searchPanel;
	}

	private VerticalLayout buildVerticalLayout_1() {

		// common part: create layout
		verticalLayout_1 = new VerticalLayout();
		//Vaadin8-setImmediate() verticalLayout_1.setImmediate(false);
		verticalLayout_1.setWidth("100.0%");
//		verticalLayout_1.setHeight("-1px");
//		verticalLayout_1.setMargin(true);
		verticalLayout_1.setSpacing(true);

//		 searchGridLayout = buildSearchGridLayout();
//		searchAbsLayout = buildSearchAbsoluteLayout();
		 mainHorizantal = buildSearchGridLayout();
		 verticalLayout_1.addComponent(mainHorizantal);
//		verticalLayout_1.addComponent(searchAbsLayout);
//		 verticalLayout_1.setComponentAlignment(searchGridLayout,
//		 Alignment.TOP_LEFT);
//		verticalLayout_1.setComponentAlignment(searchAbsLayout,
//				Alignment.TOP_LEFT);
		 
		 
		// gridLayout_1
//		 gridLayout_1 = buildGridLayout_1();
		 
		btnAbsLayout = buildCmdButtonLayout();
		verticalLayout_1.addComponent(btnAbsLayout);
		verticalLayout_1.setComponentAlignment(btnAbsLayout,
				Alignment.MIDDLE_CENTER);
		 
//		 verticalLayout_1.addComponent(gridLayout_1);
//		verticalLayout_1.setComponentAlignment(gridLayout_1,
//				Alignment.MIDDLE_CENTER);
		return verticalLayout_1;
	}

	public void clearsearchViewDetailResultTable() {
//		 //Iterator<Component> i = verticalLayout_1.getComponentIterator();
//		//
//		 //Component c = i.next();
//		//
//		 //while (i.hasNext() && !(c instanceof VerticalLayout)) {
//		 //c = i.next();
//		 //}
//		 //if (c instanceof VerticalLayout){
//		// verticalLayout_1.removeComponent(c); }
//
//		if (mainPanel.getSecondComponent() != null) {
//			mainPanel.removeComponent(searchTableLayout);
//		}

		if (mainPanel.getSecondComponent() != null) {
			Page a_page = new Page();
			a_page.setPageNumber(1);
			if(searchViewDetailResultTable != null){
				searchViewDetailResultTable.getPageable().setPageNumber(0);
				searchViewDetailResultTable.removeRow();
			}
			
		}
		
	}

	@SuppressWarnings("deprecation")
	 private HorizontalLayout buildSearchGridLayout() {
//	private AbsoluteLayout buildSearchAbsoluteLayout() {

//		AbsoluteLayout abs = new AbsoluteLayout();
		
//		//Vaadin8-setImmediate() abs.setImmediate(false);
//		abs.setWidth("100.0%");
//		abs.setHeight("250px");
		// common part: create layout
		searchGridLayout = new GridLayout();
		//Vaadin8-setImmediate() searchGridLayout.setImmediate(false);
		searchGridLayout.setWidth("55%");
		searchGridLayout.setHeight("-1px");
		searchGridLayout.setMargin(true);
		searchGridLayout.setSpacing(true);
		searchGridLayout.setColumns(4);
		searchGridLayout.setRows(7);

		searchGridLayout.setColumnExpandRatio(1, 0.25f);
		searchGridLayout.setColumnExpandRatio(3, 0.25f);
		// intiationStatuslbl
//		intimationStatuslbl = new Label(
//				"<b>Intimation Status<b style='color:red;'> * </b></b>",
//				Label.CONTENT_TEXT.HTML);
//		//Vaadin8-setImmediate() intimationStatuslbl.setImmediate(false);
//		intimationStatuslbl.setWidth("-1px");
//		intimationStatuslbl.setHeight("-1px");

//		searchGridLayout.addComponent(intimationStatuslbl, 0, 0);
		
//		abs.addComponent(intimationStatuslbl, "top: 28px; left: 28px;");
		
		// intiationStatus
		cmbIntimationStatus = new ComboBox("Intimation Status");
//		//Vaadin8-setImmediate() cmbIntimationStatus.setImmediate(true);
		cmbIntimationStatus.setWidth("160px");
//		cmbIntimationStatus.setValue("");
		cmbIntimationStatus.setRequired(true);
		
		BeanItemContainer<MastersValue> masterValue = masterService
		.getMasterValue(ReferenceTable.RECORD_STATUS);
		
		
		
		cmbIntimationStatus.setTabIndex(1);
		cmbIntimationStatus.setHeight("-1px");
		cmbIntimationStatus.setContainerDataSource(masterValue);
		cmbIntimationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimationStatus.setItemCaptionPropertyId("value");
		
	    List<MastersValue> masterValues = masterValue.getItemIds();
	    for (MastersValue mastersValue : masterValues) {
			if(mastersValue.getKey().equals(ReferenceTable.INTIMATION_SUBMITTED_VALUE)){
				
				cmbIntimationStatus.setValue(mastersValue);
				cmbIntimationStatus.select(mastersValue);
				cmbIntimationStatus.setEnabled(false);
			}
		}
	    
//	    cmbIntimationStatus.setValue(masterValue.getIdByIndex(2));
		

		// Set Default Value to First Option.
		Collection<?> itemIds = cmbIntimationStatus.getContainerDataSource()
				.getItemIds();

		cmbIntimationStatus.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				MastersValue a_selectedOption = (MastersValue) event
						.getProperty().getValue();
				if (!ValidatorUtils.isNull(a_selectedOption)) {
					if (a_selectedOption.getValue() != null)
						fireViewEvent(
								SearchIntimationPresenter.DISABLE_SEARCH_FIELDS,
								a_selectedOption.getValue());
				}
			}
		});
//		 searchGridLayout.addComponent(cmbIntimationStatus, 1, 0);

		// intimationNumberlbl
//		intimationNumberlbl = new Label();
//		//Vaadin8-setImmediate() intimationNumberlbl.setImmediate(true);
//		intimationNumberlbl.setWidth("-1px");
//		intimationNumberlbl.setHeight("-1px");
//		intimationNumberlbl.setValue("Intimation Number");

//		 searchGridLayout.addComponent(intimationNumberlbl, 2, 1);

		// intimationNumber
		intimationNumber = new TextField("Intimation Number");
		//Vaadin8-setImmediate() intimationNumber.setImmediate(false);
		intimationNumber.setWidth("160px");
		
		intimationNumber.setTabIndex(8);
		intimationNumber.setHeight("-1px");
		intimationNumber.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(intimationNumber);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");

//		 searchGridLayout.addComponent(intimationNumber, 3, 1);

		// claimNumberlbl
//		claimNumberlbl = new Label();
//		//Vaadin8-setImmediate() claimNumberlbl.setImmediate(false);
//		claimNumberlbl.setWidth("-1px");
//		claimNumberlbl.setHeight("-1px");
//		claimNumberlbl.setValue("Claim No");
//
//		 searchGridLayout.addComponent(claimNumberlbl, 2, 2);
//
		// claimNumber
		claimNumber = new TextField("Claim No");
		//Vaadin8-setImmediate() claimNumber.setImmediate(false);
		claimNumber.setWidth("160px");
		claimNumber.setTabIndex(9);
		claimNumber.setHeight("-1px");
		claimNumber.setMaxLength(25);

		CSValidator claimNumValidator = new CSValidator();
		claimNumValidator.extend(claimNumber);
		claimNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		claimNumValidator.setPreventInvalidTyping(true);

//		 searchGridLayout.addComponent(claimNumber, 3, 2);

		// policyNumberlbl
//		policyNumberlbl = new Label("Policy No");
//		//Vaadin8-setImmediate() policyNumberlbl.setImmediate(false);
//		policyNumberlbl.setWidth("-1px");
//		policyNumberlbl.setHeight("-1px");
//		searchGridLayout.addComponent(policyNumberlbl, 0, 1);
//
		// policyNumber
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(false);
		policyNumber.setWidth("160px");
		policyNumber.setTabIndex(2);
		policyNumber.setHeight("-1px");
		policyNumber.setMaxLength(50);

		CSValidator policyNumvalidator = new CSValidator();

		policyNumvalidator.extend(policyNumber);
		policyNumvalidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		policyNumvalidator.setPreventInvalidTyping(true);

//		 searchGridLayout.addComponent(policyNumber, 1, 1);

		// healthCardNumberlbl
//		healthCardNumberlbl = new Label();
//		//Vaadin8-setImmediate() healthCardNumberlbl.setImmediate(false);
//		healthCardNumberlbl.setWidth("-1px");
//		healthCardNumberlbl.setHeight("-1px");
//		healthCardNumberlbl.setValue("Health Card No");
//     	searchGridLayout.addComponent(healthCardNumberlbl, 0, 3);

		// healthCardNumber
		healthCardNumber = new TextField("Health Card No");
		//Vaadin8-setImmediate() healthCardNumber.setImmediate(false);
		healthCardNumber.setWidth("160px");
		healthCardNumber.setHeight("-1px");
		healthCardNumber.setTabIndex(4);
		healthCardNumber.setMaxLength(25);
		CSValidator healthCardValidator = new CSValidator();
		healthCardValidator.extend(healthCardNumber);
		healthCardValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		healthCardValidator.setPreventInvalidTyping(true);

		searchGridLayout.addComponent(healthCardNumber, 1, 3);

		// insuredNamelbl
//		insuredNamelbl = new Label("Insured Name");
//		//Vaadin8-setImmediate() insuredNamelbl.setImmediate(false);
//		insuredNamelbl.setWidth("-1px");
//		insuredNamelbl.setHeight("-1px");
//		 searchGridLayout.addComponent(insuredNamelbl, 0, 2);

		// insuredName
		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(false);
		insuredName.setWidth("160px");
		insuredName.setHeight("-1px");
		insuredName.setTabIndex(3);
		insuredName.setMaxLength(25);
		 searchGridLayout.addComponent(insuredName, 1, 2);

		// intimationSequenceNumberlbl
//		intimationSequenceNumberlbl = new Label();
//		//Vaadin8-setImmediate() intimationSequenceNumberlbl.setImmediate(false);
//		intimationSequenceNumberlbl.setWidth("-1px");
//		intimationSequenceNumberlbl.setHeight("-1px");
//		intimationSequenceNumberlbl.setValue("Intimation Seq No");
		// searchGridLayout.addComponent(intimationSequenceNumberlbl, 0, 4);
//		abs.addComponent(intimationSequenceNumberlbl, "top: 140px; left: 28px;");
		// intimationSequenceNumber
		intimationSequenceNumber = new TextField("Intimation Seq No");
		//Vaadin8-setImmediate() intimationSequenceNumber.setImmediate(false);
		intimationSequenceNumber.setWidth("160px");
		intimationSequenceNumber.setTabIndex(5);
		intimationSequenceNumber.setHeight("-1px");
		intimationSequenceNumber.setMaxLength(30);
//		abs.addComponent(intimationSequenceNumber, "top: 140px; left: 197px;");
		CSValidator intimationSeqNumberValidator = new CSValidator();
		intimationSeqNumberValidator.extend(intimationSequenceNumber);
		intimationSeqNumberValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		intimationSeqNumberValidator.setPreventInvalidTyping(true);
		intimationSequenceNumber.setVisible(false);
		
		intimationSequenceNumber.addBlurListener(getIntimationSequenceNoListener());

		// searchGridLayout.addComponent(intimationSequenceNumber, 1, 4);

//		// fromDateFieldlbl
//		fromDateFieldlbl = new Label("From Date");
//		//Vaadin8-setImmediate() fromDateFieldlbl.setImmediate(false);
//		fromDateFieldlbl.setWidth("160px");
//		fromDateFieldlbl.setHeight("-1px");
//		searchGridLayout.addComponent(fromDateFieldlbl, 0, 5);

		// fromDateField
		fromDateField = new PopupDateField("From Date");
		//Vaadin8-setImmediate() fromDateField.setImmediate(false);
		fromDateField.setTabIndex(6);
		fromDateField.setWidth("160px");
		fromDateField.setHeight("-1px");
		fromDateField.setTextFieldEnabled(false);
		fromDateField.setDateFormat(("dd/MM/yyyy"));
//        searchGridLayout.addComponent(fromDateField, 1, 5);

		// yearComboBoxlbl
//		yearComboBoxlbl = new Label("Year");
//		//Vaadin8-setImmediate() yearComboBoxlbl.setImmediate(false);
//		yearComboBoxlbl.setWidth("-1px");
//		yearComboBoxlbl.setHeight("-1px");

//		abs.addComponent(yearComboBoxlbl, "top: 140px; left: 390px;");
		// searchGridLayout.addComponent(yearComboBoxlbl, 2, 4);

		// yearComboBox
		yearComboBox = new ComboBox("Year");
		//Vaadin8-setImmediate() yearComboBox.setImmediate(false);
		yearComboBox.setHeight("-1px");
		yearComboBox.setTabIndex(10);
		yearComboBox.setWidth("160px");
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		yearComboBox.setContainerDataSource(policyYearValues);
		yearComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		yearComboBox.setItemCaptionPropertyId("value");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			yearComboBox.setValue(selectValue);
		}
        yearComboBox.setId("year");
		
		// searchGridLayout.addComponent(yearComboBox, 3, 4);
//		abs.addComponent(yearComboBox, "top: 140px; left: 525px;");

		// hospitalNamelbl
//		hospitalNamelbl = new Label("Hospital Name");
//		//Vaadin8-setImmediate() hospitalNamelbl.setImmediate(false);
//		hospitalNamelbl.setWidth("-1px");
//		hospitalNamelbl.setHeight("-1px");
//		searchGridLayout.addComponent(hospitalNamelbl, 0, 6);
		
		// hospitalName
		hospitalName = new TextField("Hospital Name");
		//Vaadin8-setImmediate() hospitalName.setImmediate(false);
		hospitalName.setTabIndex(7);
		hospitalName.setWidth("160px");
		hospitalName.setHeight("-1px");
		CSValidator hospitalNameValidator = new CSValidator();
		hospitalNameValidator.extend(hospitalName);
		hospitalNameValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		hospitalNameValidator.setPreventInvalidTyping(true);

//		 searchGridLayout.addComponent(hospitalName, 1, 6);
//
//		// toDateFieldlbl
//		endDatebl = new Label();
//		//Vaadin8-setImmediate() endDatebl.setImmediate(false);
//		endDatebl.setWidth("-1px");
//		endDatebl.setHeight("-1px");
//		endDatebl.setValue("End Date");
//		 searchGridLayout.addComponent(endDatebl, 2, 5);

		 // toDateField
		endDateField = new PopupDateField("End Date");
		//Vaadin8-setImmediate() endDateField.setImmediate(false);
		endDateField.setTabIndex(11);
		endDateField.setWidth("160px");
		endDateField.setHeight("-1px");
		endDateField.setDateFormat("dd/MM/yyyy");
		endDateField.setTextFieldEnabled(false);
//		searchGridLayout.addComponent(endDateField, 3, 5);
		TextField dummyText = new TextField();
		dummyText.setEnabled(false);
		dummyText.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText.setHeight("100%");
		dummyText.setReadOnly(true);
		
		TextField dummyText2 = new TextField();
		dummyText2.setEnabled(false);
		dummyText2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText2.setHeight("100%");
		dummyText2.setReadOnly(true);
			
		leftForm = new FormLayout(cmbIntimationStatus,policyNumber,insuredName,healthCardNumber,intimationSequenceNumber,fromDateField,hospitalName);
		leftForm.setMargin(true);
		rightForm =new FormLayout(dummyText,intimationNumber,claimNumber,/*dummyText2,*/yearComboBox,endDateField);
		rightForm.setMargin(true);
		mainHorizantal = new HorizontalLayout(leftForm,rightForm);
		mainHorizantal.setSpacing(true);
//		mainHorizantal.setMargin(true);
		 return mainHorizantal;
	}
	
	@SuppressWarnings("deprecation")
//	 private GridLayout buildGridLayout_1() {
	 private AbsoluteLayout buildCmdButtonLayout(){
	
		btnAbsLayout = new AbsoluteLayout();
		btnAbsLayout.setHeight("30px");
		
		
	
		// common part: create layout
		
//		  gridLayout_1 = new GridLayout(); //Vaadin8-setImmediate() gridLayout_1.setImmediate(false);
//		  gridLayout_1.setWidth("150px"); gridLayout_1.setHeight("-1px");
//		  gridLayout_1.setMargin(false); gridLayout_1.setSpacing(true);
//		  gridLayout_1.setColumns(2);

		// searchButton
		searchButton = new Button();
		searchButton.setCaption("Search");
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		searchButton.setWidth("-1px");
		searchButton.setTabIndex(12);
		searchButton.setHeight("-1px");
		searchButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchButton.setDisableOnClick(true);
		searchButton.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				// searchIntimationTable = buildEmptyTable();
				
				clearsearchViewDetailResultTable();

				searchButton.setEnabled(true);
				searchIntimationFormDto = new SearchIntimationFormDto();
				Map<String, Object> filters = new HashMap<String, Object>();
				
				if(isValid()){

				if (cmbIntimationStatus.getValue() != null) {

					MastersValue intimStatus = (MastersValue) cmbIntimationStatus
							.getValue();

					if (cmbIntimationStatus.getValue() != null
							&& cmbIntimationStatus.getValue() != "") {
						
						filters.put("intimationStatus", intimStatus.getValue());
						

						System.out.println("Status : "
								+ filters.get("intimationStatus"));

						if (StringUtils.equalsIgnoreCase(
								intimStatus.getValue(), "Submitted")) {
							if (intimationNumber.getValue() != null
									&& intimationNumber.getValue() != "") {
								filters.put("intimationNumber",
										intimationNumber.getValue());
							}
							/*if (intimationSequenceNumber.getValue() != null
									&& intimationSequenceNumber.getValue() != "") {
								filters.put("intimationSequenceNumber",
										intimationSequenceNumber.getValue());
							}*/
							if (yearComboBox.getValue() != null
									&& yearComboBox.getValue() != "") {
								SelectValue year = (SelectValue) yearComboBox
										.getValue();
								filters.put("intimationYear", year.getValue());
							}
							if (claimNumber.getValue() != null
									&& claimNumber.getValue() != "") {
								filters.put("claimNumber",
										claimNumber.getValue());
							}
						}
						if (fromDateField.getValue() != null) {
							filters.put("fromDate", fromDateField.getValue());
						}
						if (endDateField.getValue() != null) {
							filters.put("toDate", endDateField.getValue());
						}
						if (policyNumber.getValue() != null
								&& policyNumber.getValue() != "") {
							filters.put("policyNumber", policyNumber.getValue());
						}
						if (insuredName.getValue() != null
								&& insuredName.getValue() != "") {
							filters.put("insuredName", insuredName.getValue());
						}
						if (hospitalName.getValue() != null
								&& hospitalName.getValue() != "") {
							filters.put("hospitalName", hospitalName.getValue());
						}

						if (healthCardNumber.getValue() != null
								&& healthCardNumber.getValue() != "") {
							filters.put("healthCardNumber",
									healthCardNumber.getValue());

						}
						
						filters.put("pageable", searchViewDetailResultTable.getPageable());

						searchIntimationFormDto.setFilters(filters);
						clearsearchViewDetailResultTable();

						fireViewEvent(SearchViewDetailPresenter.SUBMIT_SEARCH,
								searchIntimationFormDto);
						
//						fireViewEvent(SearchIntimationPresenter.SUBMIT_SEARCH,
//								filters);

					}
				} else {

					showErrorPopup("<b>Intimation Status Field is Mandatory<br></b>");

					// Notification
					// .show("Information",
					// "Intimation Status Field is Mandatory, Please Select one Value for Status",
					// Notification.TYPE_HUMANIZED_MESSAGE);
				}
				}else{
					showErrorPopup("<b>Please Enter any one of the field<br></b>");
				}
			}
		});
		
//		 gridLayout_1.addComponent(searchButton, 0, 0);

		btnAbsLayout.addComponent(searchButton, "left: 280px; top: -1px;");

		// resetButton
		resetButton = new Button();
		resetButton.setCaption("Reset");
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		//Vaadin8-setImmediate() resetButton.setImmediate(true);
		resetButton.setTabIndex(13);
		resetButton.setStyleName(ValoTheme.BUTTON_DANGER);
		resetButton.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				fireViewEvent(SearchViewDetailPresenter.RESET_SEARCH_VIEW, null);
			}
		});
		
		btnAbsLayout.addComponent(resetButton, "left: 390px; top: -1px;");
		
		return btnAbsLayout;
//		gridLayout_1.addComponent(resetButton, 1, 0);

//		 return gridLayout_1;
		
		
	}
	
	public Boolean isValid(){
		Boolean isFieldEntered = Boolean.FALSE;
		if(policyNumber != null && policyNumber.getValue() != null && ! policyNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(intimationNumber != null && intimationNumber.getValue() != null && ! intimationNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(insuredName != null && insuredName.getValue() != null && ! insuredName.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(claimNumber != null && claimNumber.getValue() != null && ! claimNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(healthCardNumber != null && healthCardNumber.getValue() != null && ! healthCardNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(fromDateField != null && fromDateField.getValue() != null){
			isFieldEntered = Boolean.TRUE;
		}
		if(endDateField != null && endDateField.getValue() != null){
			isFieldEntered = Boolean.TRUE;
		}
		if(hospitalName != null && hospitalName.getValue() != null && ! hospitalName.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		
		/*if(intimationSequenceNumber != null && intimationSequenceNumber.getValue() != null && ! intimationSequenceNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}*/
		
		return isFieldEntered;
	}

	public void showErrorPopup(String errorMessage) {
		Label label = new Label(errorMessage, ContentMode.HTML);
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

		return;
	}
	public void resetSearchIntimationFields() {
		
		VerticalLayout wholeLayout = verticalLayout_1;
		Iterator<Component> i = wholeLayout.getComponentIterator();
		Component c = i.next();
		
		for (int count = 0; count < wholeLayout.getComponentCount(); count++) {

			 if(c instanceof HorizontalLayout){
				 
				 Iterator<Component> horizontalIter = ((HorizontalLayout) c).getComponentIterator();
					Component comp1 = (Component) horizontalIter.next();
					
				 for(int index = 0; index<((HorizontalLayout) c).getComponentCount();index++){
					 
					 if (comp1 instanceof FormLayout) {
						 
						 Iterator<Component> formIter = ((FormLayout) comp1).getComponentIterator();
						 Component field = (Component) formIter.next();
						 
						for(int pos = 0; pos<((FormLayout)comp1).getComponentCount();pos++){
							
							field.setEnabled(true);
							
							if (field instanceof TextField) {
								((TextField) field).setValue("");
								field.setEnabled(true);
							}
							if (field instanceof ComboBox) {
//								((ComboBox) field).setValue(null);
		
							}
		
							if (field instanceof ComboBox) {
//								((ComboBox) field).setValue(null);
								if(field.getId() != null && field.getId().equalsIgnoreCase("year")){field.setEnabled(true);
								ComboBox box = (ComboBox) field;
								BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
								List<SelectValue> itemIds2 = policyYearValues.getItemIds();
								if(itemIds2 != null && ! itemIds2.isEmpty()){
									SelectValue selectValue = itemIds2.get(0);
									box.setValue(selectValue);
								}
								}else{
									field.setEnabled(false);
								}		
		
							}
							if (field instanceof PopupDateField) {
								((PopupDateField) field).setValue(null);
							}
		
							if (field instanceof Label) {
								continue;
							}
					
							if (field instanceof PopupDateField) {
								((PopupDateField) field).setValue(null);
								field.setEnabled(true);
							}
//							 comp = (Component)gridIter.next();
//		
//							if (comp instanceof VerticalLayout) {
//								for(int pos = 0; pos<((VerticalLayout)comp).getComponentCount();pos++){
//									if(((VerticalLayout)comp).getComponent(pos) instanceof TextField){
//										((TextField)((VerticalLayout)comp).getComponent(pos)).setValue("");
//									}
//									if(((VerticalLayout)comp).getComponent(pos) instanceof Label){
//										continue;
//									}
//									if(((VerticalLayout)comp).getComponent(pos) instanceof ComboBox){
//										((TextField)((VerticalLayout)comp).getComponent(pos)).setValue(null);
//									}
//								}
//								((TextField) comp).setValue("");
//								comp.setEnabled(true);
//							}
//						 }
						 if (field instanceof FormLayout) {
								continue;
						}
						 if(formIter.hasNext())
						 {
							 field = (Component) formIter.next();
						 }
			}
						
		 }
					 if(horizontalIter.hasNext())
					 {
						 comp1 = (Component) horizontalIter.next();
					 }
					 
		}
	 }
		}
	}

//	public void resetSearchIntimationFields() {
//		
//		VerticalLayout wholeLayout = verticalLayout_1;
//		Iterator<Component> i = wholeLayout.getComponentIterator();
//		Component c = i.next();
//
//		for (int count = 0; count < wholeLayout.getComponentCount()
//				&& i.hasNext(); count++) {
//
//			 if(c instanceof GridLayout && ((GridLayout)
//			 c).getComponentCount()>2){
//			
//				 Iterator<Component> gridIter = ((GridLayout) c).getComponentIterator();
//				Component comp = (Component) gridIter.next();
//
//				 for(int index =0; index<((GridLayout) c).getComponentCount()
//				 && gridIter.hasNext();index++,comp=(Component)gridIter.next()){
//
//					comp.setEnabled(true);
//					if (comp instanceof TextField) {
//						((TextField) comp).setValue("");
//						comp.setEnabled(true);
//					}
//					if (comp instanceof ComboBox) {
//						((ComboBox) comp).setValue(null);
//
//					}
//
//					if (comp instanceof ComboBox) {
//						((ComboBox) comp).setValue(null);
//
//
//					}
//					if (comp instanceof PopupDateField) {
//						((PopupDateField) comp).setValue(null);
//					}
//
//					if (comp instanceof Label) {
//						continue;
//					}
//			
//					if (comp instanceof PopupDateField) {
//						((PopupDateField) comp).setValue(null);
//						comp.setEnabled(true);
//					}
//					 comp = (Component)gridIter.next();
//
//					if (comp instanceof VerticalLayout) {
//						for(int pos = 0; pos<((VerticalLayout)comp).getComponentCount();pos++){
//							if(((VerticalLayout)comp).getComponent(pos) instanceof TextField){
//								((TextField)((VerticalLayout)comp).getComponent(pos)).setValue("");
//							}
//							if(((VerticalLayout)comp).getComponent(pos) instanceof Label){
//								continue;
//							}
//							if(((VerticalLayout)comp).getComponent(pos) instanceof ComboBox){
//								((TextField)((VerticalLayout)comp).getComponent(pos)).setValue(null);
//							}
//						}
//						((TextField) comp).setValue("");
//						comp.setEnabled(true);
//					}
//				 }
//				 if (comp instanceof TextField) {
//						((TextField) comp).setValue("");
//						comp.setEnabled(true);
//					}
//			 }
//			 if(c instanceof GridLayout && ((GridLayout)
//			 c).getComponentCount()==2){
//				Button searchB = (Button) ((GridLayout) c).getComponent(0, 0);
//				((GridLayout) c).removeComponent(0, 0);
//				searchB.setData("");
//				searchB.setIcon(null);
//				searchB.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				((GridLayout) c).addComponent(searchB, 0, 0);
//			}
//			c = i.next();
//		}
//
//		if (c instanceof VerticalLayout)
//			verticalLayout_1.removeComponent(c);
//		if (mainPanel.getSecondComponent() != null) {
//			clearSearchResultTable();
////			mainWholelayout.removeComponent(searchTableLayout);
//		}
//		
//	}

	public void refresh() {
		System.out.println("---inside the refresh----");
		resetSearchIntimationFields();
	}

	public void searchButtonDisable() {
		searchButton.setEnabled(false);
	}
	
	public BlurListener getIntimationSequenceNoListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				if(yearComboBox != null){
					if(component.getValue() != null && ! component.getValue().equalsIgnoreCase("")){
						yearComboBox.setEnabled(true);
					}else{
						yearComboBox.setEnabled(false);
						yearComboBox.setValue(null);
					}
				}
				
			}
		};
		return listener;
		
	}
	
	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		
		//ADDED FOR FY APR - MAR
		int month = instance.get(Calendar.MONTH);
		if(month >= 3){
			instance.add(Calendar.YEAR, 1);
		}
		
		/*instance.add(Calendar.YEAR, 1);*/
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=year-13;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}


}
