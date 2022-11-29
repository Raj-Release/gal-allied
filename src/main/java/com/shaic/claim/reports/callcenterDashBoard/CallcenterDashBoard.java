package com.shaic.claim.reports.callcenterDashBoard;

import java.util.ArrayList;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class CallcenterDashBoard extends ViewComponent {
	
	@EJB
	MasterService masterService;
	
	private Panel searchPanel;

	private VerticalLayout mainWholelayout = new VerticalLayout();

	private VerticalLayout verticalLayout_1;

	private GridLayout gridLayout_1;

	private GridLayout searchGridLayout;

	private Button resetButton;

	private Button searchButton;
	
	private Button exportToExcelBtn;
	
	//private Label dummyLabel;

	private PopupDateField endDateField;

//	private ComboBox yearComboBox;

	private TextField hospitalName;

	private PopupDateField fromDateField;

//	private TextField intimationSequenceNumber;

	private TextField policyNumber;

	private TextField healthCardNumber;

	private TextField claimNumber;

	private TextField intimationNumber;

	private ComboBox cmbIntimationStatus;
	
	private ComboBox cmbType;

	private Label typelbl;
	
	private Label intimationStatuslbl;

	private Label intimationNumberlbl;

	private Label claimNumberlbl;

	private AbstractComponent policyNumberlbl;

	private Label healthCardNumberlbl;

//	private Label intimationSequenceNumberlbl;

	private AbstractComponent fromDateFieldlbl;

//	private AbstractComponent yearComboBoxlbl;

	private Label hospitalNamelbl;

	private Label endDatebl;

//	private VerticalLayout searchTableLayout;
	
	private ExcelExport excelExport;
	
	private VerticalLayout vLayout;
	
	private HorizontalLayout buttonLayout;
	
	private boolean auditColVisible;

	private static final long serialVersionUID = 1L;

	@Inject
	private CallcenterDashBoardReportTable searchResultTable;

	@PostConstruct
	public void init() {

		setSizeFull();
		mainWholelayout.setWidth("100.0%");
		setSizeFull();

		vLayout = buildMainLayout();
		mainWholelayout.addComponent(vLayout);
		searchResultTable.init("", false, false);
		this.auditColVisible = false;
		
		setCompositionRoot(mainWholelayout);

	}

	public void buildSearchIntimationTable(

	List<CallcenterDashBoardReportDto> callcenterDashBoardReportDtoList) {

		if (callcenterDashBoardReportDtoList != null && !callcenterDashBoardReportDtoList.isEmpty()) {
			
			searchResultTable.setTableList(callcenterDashBoardReportDtoList);
			searchResultTable.setAudiColVisible(this.auditColVisible);
//			searchTableLayout = new VerticalLayout(searchResultTable);				
//
//			searchTableLayout.setWidth("100%");
//			mainWholelayout.addComponent(searchTableLayout);
			vLayout.addComponent(searchResultTable);
			vLayout.setSpacing(true);
			vLayout.setMargin(true);
			
			buttonLayout.removeComponent(exportToExcelBtn);
			
			if(buttonLayout.getComponentIndex(exportToExcelBtn) > 0){
				buttonLayout.removeComponent(exportToExcelBtn);
			}
			
			exportToExcelBtn = new Button("Export To Excel");
			
			exportToExcelBtn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					excelExport = new ExcelExport(searchResultTable.getTable());
					String reportTitle = "";
					if(auditColVisible){
						reportTitle = "Administrative Dash Board";
						
					}else{
						reportTitle = "Callcenter Dash Board";
					}	
					excelExport.setReportTitle(reportTitle);
					excelExport.setDisplayTotals(false);
					excelExport.export();					
				}
			});
			
//			if(gridLayout_1.getComponent(2, 0) == null){
//				gridLayout_1.addComponent(exportToExcelBtn, 2, 0);
//				buttonLayout.addComponent(dummyLabel);
				
								
				buttonLayout.addComponent(exportToExcelBtn);
//			}
		}

		else {
			// empty result

//			resetSearchFields();
//			if(gridLayout_1.getComponent(2, 0) == null){
//				gridLayout_1.removeComponent(exportToExcelBtn);
//				buttonLayout.removeComponent(dummyLabel);
				buttonLayout.removeComponent(exportToExcelBtn);
//			}
			
//			Notification.show("No Records Found", Notification.TYPE_HUMANIZED_MESSAGE);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			
			String captionStr = "";
			if(!auditColVisible){
				captionStr = "Callcenter DashBoard Home";
			}
			else{
				captionStr = "Administrative DashBoard Home";
			}
			
			Button homeButton = new Button(captionStr);
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();	
					resetSearchFields();
					
					if(!auditColVisible){
						fireViewEvent(MenuItemBean.CALL_CENTER_DASH_BOARD, null);
					}
					else{
						fireViewEvent(MenuItemBean.ADMINISTRATIVE_DASH_BOARD, null);
					}
					
				}
			});			
		}
	}
	
	public void setAuditColVisible(boolean visible){
		this.auditColVisible = visible;
		
	}

	public void hideSearchFields(String valChanged) {
		if (!StringUtils.equalsIgnoreCase(valChanged, SHAConstants.INTIMATION_SUBMITTED)) {
//			intimationNumber.setValue("");
			claimNumber.setValue("");
//			intimationSequenceNumber.setValue("");
//			yearComboBox.setValue("");

//			intimationNumber.setEnabled(false);
			claimNumber.setEnabled(false);
//			intimationSequenceNumber.setEnabled(false);
//			yearComboBox.setEnabled(false);

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

//			intimationNumber.setEnabled(true);
			claimNumber.setEnabled(true);
//			intimationSequenceNumber.setEnabled(true);
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

	private VerticalLayout buildMainLayout() {
		
		VerticalLayout searchVerticalLayout = new VerticalLayout();
		
		searchPanel = buildSearchPanel();
		searchPanel.addStyleName("g-search-panel");

		searchVerticalLayout.addComponent(searchPanel);
		searchVerticalLayout.setMargin(true);
		searchVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_CENTER);
		
		return searchVerticalLayout;
	}

	private Panel buildSearchPanel() {

		// common part: create layout
		searchPanel = new Panel();
		searchPanel.setCaption("Claims Search");

		//Vaadin8-setImmediate() searchPanel.setImmediate(false);
		searchPanel.setWidth("100%");
//		 searchPanel.setWidth("85%");
		/*
		 * searchPanel.setWidth("700px"); searchPanel.setHeight("50%");
		 */
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
		verticalLayout_1.setHeight("-1px");
		verticalLayout_1.setMargin(true);
		verticalLayout_1.setSpacing(true);
		

//		dummyLabel =new Label();
//		dummyLabel.setWidth("30px");

		 searchGridLayout = buildSearchGridLayout();

		 verticalLayout_1.addComponent(searchGridLayout);
		 verticalLayout_1.setComponentAlignment(searchGridLayout,
		 Alignment.TOP_LEFT);
		 
//		Label dummyLabel1 =new Label();
//			dummyLabel1.setWidth("30px");
		 // gridLayout_1
		 gridLayout_1 = buildGridLayout_1();
		 gridLayout_1.setMargin(true);
		 verticalLayout_1.addComponent(gridLayout_1);
		verticalLayout_1.setComponentAlignment(gridLayout_1,
				Alignment.MIDDLE_CENTER);
//		Label dummyLabe2 =new Label();
//		dummyLabe2.setWidth("200px");
		buttonLayout= new HorizontalLayout(searchButton,resetButton);
		buttonLayout.setSpacing(true);
//		VerticalLayout vblayout = new VerticalLayout();
//		vblayout.addComponent(buttonLayout);
//		vblayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		
		verticalLayout_1.addComponent(buttonLayout);
		verticalLayout_1.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		return verticalLayout_1;
	}

	public void clearSearchResultTable() {
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

		if (vLayout.getComponentCount() > 1) {
			searchResultTable.setTableList(new ArrayList<CallcenterDashBoardReportDto>());
		}
		
	}

	@SuppressWarnings("deprecation")
	 private GridLayout buildSearchGridLayout() {

		// common part: create layout
		searchGridLayout = new GridLayout();
		//Vaadin8-setImmediate() searchGridLayout.setImmediate(false);
		searchGridLayout.setWidth("75%");
		searchGridLayout.setHeight("-1px");
		searchGridLayout.setSpacing(true);
		searchGridLayout.setColumns(4);
		searchGridLayout.setRows(10);

//		searchGridLayout.setColumnExpandRatio(1, 0.25f);
//		searchGridLayout.setColumnExpandRatio(3, 0.25f);
	
		typelbl = new Label("Type");
		//Vaadin8-setImmediate() typelbl.setImmediate(false);
		typelbl.setWidth("-1px");
		typelbl.setHeight("-1px");
		
		searchGridLayout.addComponent(typelbl,0,0);
		
		cmbType = new ComboBox();
		cmbType.getContainerDataSource().addItem("Policy");
		cmbType.getContainerDataSource().addItem("Claim");
		cmbType.setWidth("200px");		
		
		searchGridLayout.addComponent(cmbType,1,0);
		
		
		// intiationStatuslbl
		intimationStatuslbl = new Label("Status");
		//Vaadin8-setImmediate() intimationStatuslbl.setImmediate(false);
		intimationStatuslbl.setWidth("-1px");
		intimationStatuslbl.setHeight("-1px");

		searchGridLayout.addComponent(intimationStatuslbl, 0, 1);
		
		// intiationStatus
		cmbIntimationStatus = new ComboBox();
		//Vaadin8-setImmediate() cmbIntimationStatus.setImmediate(true);
		cmbIntimationStatus.setWidth("200px");
		cmbIntimationStatus.setValue("");
		
		cmbIntimationStatus.setTabIndex(1);
		cmbIntimationStatus.setHeight("-1px");
		cmbIntimationStatus.setContainerDataSource(masterService
				.getMasterValue(ReferenceTable.RECORD_STATUS));
		cmbIntimationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimationStatus.setItemCaptionPropertyId("value");

		// Set Default Value to First Option.
		//Collection<?> itemIds = cmbIntimationStatus.getContainerDataSource().getItemIds();

		cmbIntimationStatus.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				MastersValue a_selectedOption = (MastersValue) event
						.getProperty().getValue();
				if (!ValidatorUtils.isNull(a_selectedOption)) {
					if (a_selectedOption.getValue() != null)
						fireViewEvent(
								CallcenterDashBoardPresenter.DISABLE_FIELDS,
								a_selectedOption.getValue());
				}
			}
		});
		 searchGridLayout.addComponent(cmbIntimationStatus, 1, 1);
		
		// policyNumberlbl
			policyNumberlbl = new Label("Policy No");
			//Vaadin8-setImmediate() policyNumberlbl.setImmediate(false);
			policyNumberlbl.setWidth("-1px");
			policyNumberlbl.setHeight("-1px");
			searchGridLayout.addComponent(policyNumberlbl, 0, 4);
			
//			abs.addComponent(policyNumberlbl, "top: 56px; left: 28px;");

			// policyNumber
			policyNumber = new TextField();
			//Vaadin8-setImmediate() policyNumber.setImmediate(false);
			policyNumber.setWidth("200px");
			policyNumber.setTabIndex(2);
			policyNumber.setHeight("-1px");
			policyNumber.setMaxLength(25);
			
//			abs.addComponent(policyNumber, "top: 56px; left: 197px;");

			CSValidator policyNumvalidator = new CSValidator();

			policyNumvalidator.extend(policyNumber);
			policyNumvalidator.setRegExp("^[a-zA-Z 0-9/-]*$");
			policyNumvalidator.setPreventInvalidTyping(true);

			searchGridLayout.addComponent(policyNumber, 1, 4);
		 
		 
		 
		
		// intimationNumberlbl
		intimationNumberlbl = new Label();
		//Vaadin8-setImmediate() intimationNumberlbl.setImmediate(true);
		intimationNumberlbl.setWidth("-1px");
		intimationNumberlbl.setHeight("-1px");
		intimationNumberlbl.setValue("Intimation No");
		searchGridLayout.addComponent(intimationNumberlbl, 0, 3);
		
//		abs.addComponent(intimationNumberlbl, "top: 56px; left: 390px;");

		// intimationNumber
		intimationNumber = new TextField();
		//Vaadin8-setImmediate() intimationNumber.setImmediate(false);
		intimationNumber.setWidth("200px");
		
		intimationNumber.setTabIndex(8);
		intimationNumber.setHeight("-1px");
		intimationNumber.setMaxLength(25);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(intimationNumber);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");

		searchGridLayout.addComponent(intimationNumber, 1, 3);

		// claimNumberlbl
		claimNumberlbl = new Label();
		//Vaadin8-setImmediate() claimNumberlbl.setImmediate(false);
		claimNumberlbl.setWidth("-1px");
		claimNumberlbl.setHeight("-1px");
		claimNumberlbl.setValue("Claim No");

		searchGridLayout.addComponent(claimNumberlbl, 0, 2);

		// claimNumber
		claimNumber = new TextField();
		//Vaadin8-setImmediate() claimNumber.setImmediate(false);
		claimNumber.setWidth("200px");
		claimNumber.setTabIndex(9);
		claimNumber.setHeight("-1px");
		claimNumber.setMaxLength(25);

		CSValidator claimNumValidator = new CSValidator();
		claimNumValidator.extend(claimNumber);
		claimNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		claimNumValidator.setPreventInvalidTyping(true);

		searchGridLayout.addComponent(claimNumber, 1, 2);

		

		// healthCardNumberlbl
		healthCardNumberlbl = new Label();
		//Vaadin8-setImmediate() healthCardNumberlbl.setImmediate(false);
		healthCardNumberlbl.setWidth("-1px");
		healthCardNumberlbl.setHeight("-1px");
		healthCardNumberlbl.setValue("ID Card No");
		searchGridLayout.addComponent(healthCardNumberlbl, 0, 6);
		
//		abs.addComponent(healthCardNumberlbl, "top: 112px; left: 28px;");

		// healthCardNumber
		healthCardNumber = new TextField();
		//Vaadin8-setImmediate() healthCardNumber.setImmediate(false);
		healthCardNumber.setWidth("200px");
		healthCardNumber.setHeight("-1px");
		healthCardNumber.setTabIndex(4);
		healthCardNumber.setMaxLength(25);
//		abs.addComponent(healthCardNumber, "top: 112px; left: 197px;");
		CSValidator healthCardValidator = new CSValidator();
		healthCardValidator.extend(healthCardNumber);
		healthCardValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		healthCardValidator.setPreventInvalidTyping(true);

		searchGridLayout.addComponent(healthCardNumber, 1, 6);

		// intimationSequenceNumberlbl
//		intimationSequenceNumberlbl = new Label();
//		//Vaadin8-setImmediate() intimationSequenceNumberlbl.setImmediate(false);
//		intimationSequenceNumberlbl.setWidth("-1px");
//		intimationSequenceNumberlbl.setHeight("-1px");
//		intimationSequenceNumberlbl.setValue("Intimation Seq No");
//		searchGridLayout.addComponent(intimationSequenceNumberlbl, 0, 4);
		
//		abs.addComponent(intimationSequenceNumberlbl, "top: 140px; left: 28px;");
		
		// intimationSequenceNumber
//		intimationSequenceNumber = new TextField();
//		//Vaadin8-setImmediate() intimationSequenceNumber.setImmediate(false);
//		intimationSequenceNumber.setWidth("65px");
//		intimationSequenceNumber.setTabIndex(5);
//		intimationSequenceNumber.setHeight("-1px");
//		intimationSequenceNumber.setMaxLength(30);
////		abs.addComponent(intimationSequenceNumber, "top: 140px; left: 197px;");
//		CSValidator intimationSeqNumberValidator = new CSValidator();
//		intimationSeqNumberValidator.extend(intimationSequenceNumber);
//		intimationSeqNumberValidator.setRegExp("^[a-zA-Z 0-9/]*$");
//		intimationSeqNumberValidator.setPreventInvalidTyping(true);
		
		// yearComboBoxlbl
//		yearComboBoxlbl = new Label("Year");
//		//Vaadin8-setImmediate() yearComboBoxlbl.setImmediate(false);
//		yearComboBoxlbl.setWidth("-1px");
//		yearComboBoxlbl.setHeight("-1px");

//		searchGridLayout.addComponent(yearComboBoxlbl, 2, 4);

		// yearComboBox
//		yearComboBox = new ComboBox();
//		//Vaadin8-setImmediate() yearComboBox.setImmediate(false);
//		yearComboBox.setHeight("-1px");
//		yearComboBox.setTabIndex(10);
//		yearComboBox.setWidth("90px");
//		yearComboBox.setContainerDataSource(masterService
//				.getMasterValue(ReferenceTable.INTIMATION_YEAR));
//		yearComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		yearComboBox.setItemCaptionPropertyId("value");
		
//		searchGridLayout.addComponent(yearComboBox, 3, 4);
		
//		HorizontalLayout seqNumLayout = new HorizontalLayout();
//		seqNumLayout.setSpacing(true);
//		seqNumLayout.addComponents(intimationSequenceNumber,yearComboBoxlbl,yearComboBox);
		
//		searchGridLayout.addComponent(intimationSequenceNumber, 1, 4);

//		searchGridLayout.addComponent(seqNumLayout, 1, 4);
		
		
		// fromDateFieldlbl
		fromDateFieldlbl = new Label("From Date");
		//Vaadin8-setImmediate() fromDateFieldlbl.setImmediate(false);
		fromDateFieldlbl.setWidth("-1px");
		fromDateFieldlbl.setHeight("-1px");
		searchGridLayout.addComponent(fromDateFieldlbl, 0, 5);

		// fromDateField
		fromDateField = new PopupDateField();
		//Vaadin8-setImmediate() fromDateField.setImmediate(false);
		fromDateField.setTabIndex(6);
		fromDateField.setWidth("200px");
		fromDateField.setHeight("-1px");
		fromDateField.setDateFormat(("dd/MM/yyyy"));
		searchGridLayout.addComponent(fromDateField, 1, 5);

		// hospitalNamelbl
		hospitalNamelbl = new Label("Hospital Name");
		//Vaadin8-setImmediate() hospitalNamelbl.setImmediate(false);
		hospitalNamelbl.setWidth("-1px");
		hospitalNamelbl.setHeight("-1px");
		searchGridLayout.addComponent(hospitalNamelbl, 0, 7);
		
		// hospitalName
		hospitalName = new TextField();
		//Vaadin8-setImmediate() hospitalName.setImmediate(false);
		hospitalName.setTabIndex(7);
		hospitalName.setWidth("200px");
		hospitalName.setHeight("-1px");
		CSValidator hospitalNameValidator = new CSValidator();
		hospitalNameValidator.extend(hospitalName);
		hospitalNameValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		hospitalNameValidator.setPreventInvalidTyping(true);

		 searchGridLayout.addComponent(hospitalName, 1, 7);

		// toDateFieldlbl
		endDatebl = new Label();
		//Vaadin8-setImmediate() endDatebl.setImmediate(false);
		endDatebl.setWidth("-1px");
		endDatebl.setHeight("-1px");
		endDatebl.setValue("End Date");
		searchGridLayout.addComponent(endDatebl, 2, 5);

		// toDateField
		endDateField = new PopupDateField();
		//Vaadin8-setImmediate() endDateField.setImmediate(false);
		endDateField.setTabIndex(11);
		endDateField.setWidth("160px");
		endDateField.setHeight("-1px");
		endDateField.setDateFormat("dd/MM/yyyy");
		searchGridLayout.addComponent(endDateField, 3, 5);
		
		 return searchGridLayout;
	}

	
	@SuppressWarnings("deprecation")
	private GridLayout buildGridLayout_1() {
	
//	private AbsoluteLayout buildCmdButtonLayout() {

//		btnAbsLayout = new AbsoluteLayout();
//		btnAbsLayout.setHeight("30px");

		// common part: create layout
		
		gridLayout_1 = new GridLayout(); //Vaadin8-setImmediate() gridLayout_1.setImmediate(false);
		gridLayout_1.setWidth("150px"); gridLayout_1.setHeight("-1px");
		gridLayout_1.setMargin(false); gridLayout_1.setSpacing(true);
		gridLayout_1.setColumns(3);
		
		exportToExcelBtn = new Button("Export To Excel");

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
				
				clearSearchResultTable();
//				if(gridLayout_1.getComponent(2, 0) != null){
//					gridLayout_1.removeComponent(exportToExcelBtn);
//					buttonLayout.removeComponent(dummyLabel);
					buttonLayout.removeComponent(exportToExcelBtn);
//				}

				searchButton.setEnabled(true);
				Map<String, Object> filters = new HashMap<String, Object>();

				if (cmbIntimationStatus.getValue() != null) {

					MastersValue intimStatus = (MastersValue) cmbIntimationStatus
							.getValue();

					if (cmbIntimationStatus.getValue() != null
							&& cmbIntimationStatus.getValue() != "") {
						filters.put("intimationStatus", intimStatus.getValue());
						filters.put("refstatusKey", intimStatus.getKey());
						System.out.println("Status : "
								+ filters.get("intimationStatus"));

						if (StringUtils.equalsIgnoreCase(
								intimStatus.getValue(), "Submitted")) {
							if (intimationNumber.getValue() != null
									&& intimationNumber.getValue() != "") {
								filters.put("intimationNumber",
										intimationNumber.getValue());
							}
//							if (intimationSequenceNumber.getValue() != null
//									&& intimationSequenceNumber.getValue() != "") {
//								filters.put("intimationSequenceNumber",
//										intimationSequenceNumber.getValue());
//							}
//							if (yearComboBox.getValue() != null
//									&& yearComboBox.getValue() != "") {
//								MastersValue year = (MastersValue) yearComboBox
//										.getValue();
//								filters.put("intimationYear", year.getValue());
//							}
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
						if (hospitalName.getValue() != null
								&& hospitalName.getValue() != "") {
							filters.put("hospitalName", hospitalName.getValue());
						}

						if (healthCardNumber.getValue() != null
								&& healthCardNumber.getValue() != "") {
							filters.put("healthCardNumber",
									healthCardNumber.getValue());

						}

						clearSearchResultTable();

						fireViewEvent(CallcenterDashBoardPresenter.SEARCH_CLM_CALLCENTER_DASH_BOARD,
								filters);
						
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
			}
		});
//		 gridLayout_1.addComponent(searchButton, 0, 0);

//		btnAbsLayout.addComponent(searchButton, "left: 300px; top: 0px;");

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

				fireViewEvent(CallcenterDashBoardPresenter.RESET_SEARCH_CALLCENTER_DASHBOARD_VIEW, null);
			}
		});
//		 gridLayout_1.addComponent(resetButton, 1, 0);
//		btnAbsLayout.addComponent(resetButton, "left: 390px; top: 0px;");

//		return btnAbsLayout;
		 return gridLayout_1;
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
		searchButton.setEnabled(true);

		return;
	}

	@SuppressWarnings("deprecation")
	public void resetSearchFields() {
//		 Iterator<Component> gridIter =
//		 searchGridLayout.getComponentIterator();
		VerticalLayout wholeLayout = verticalLayout_1;
		Iterator<Component> i = wholeLayout.getComponentIterator();
		Component c = i.next();

		for (int count = 0; count < wholeLayout.getComponentCount()
				&& i.hasNext(); count++) {

			 if(c instanceof GridLayout && ((GridLayout)
			 c).getComponentCount()>2){
			
				 Iterator<Component> gridIter = ((GridLayout) c).getComponentIterator();
				Component comp = (Component) gridIter.next();

				 for(int index =0; index<((GridLayout) c).getComponentCount()
				 && gridIter.hasNext();index++,comp=(Component)gridIter.next()){

					comp.setEnabled(true);
					if (comp instanceof TextField) {
						((TextField) comp).setValue("");
						comp.setEnabled(true);
					}
					if (comp instanceof ComboBox) {
						((ComboBox) comp).setValue(null);

					}

					if (comp instanceof ComboBox) {
						((ComboBox) comp).setValue(null);


					}
					if (comp instanceof PopupDateField) {
						((PopupDateField) comp).setValue(null);
					}

					if (comp instanceof Label) {
						continue;
					}
			
					if (comp instanceof PopupDateField) {
						((PopupDateField) comp).setValue(null);
						comp.setEnabled(true);
					}
					 comp = (Component)gridIter.next();

					if (comp instanceof VerticalLayout) {
						for(int pos = 0; pos<((VerticalLayout)comp).getComponentCount();pos++){
							if(((VerticalLayout)comp).getComponent(pos) instanceof TextField){
								((TextField)((VerticalLayout)comp).getComponent(pos)).setValue("");
							}
							if(((VerticalLayout)comp).getComponent(pos) instanceof Label){
								continue;
							}
							if(((VerticalLayout)comp).getComponent(pos) instanceof ComboBox){
								((TextField)((VerticalLayout)comp).getComponent(pos)).setValue(null);
							}
						}
						((TextField) comp).setValue("");
						comp.setEnabled(true);
					}
				 }
				 if (comp instanceof TextField) {
						((TextField) comp).setValue("");
						comp.setEnabled(true);
					}
			 }
			 if(c instanceof GridLayout && ((GridLayout)
			 c).getComponentCount()==2){
				Button searchB = (Button) ((GridLayout) c).getComponent(0, 0);
				((GridLayout) c).removeComponent(0, 0);
				searchB.setData("");
				searchB.setIcon(null);
				searchB.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				((GridLayout) c).addComponent(searchB, 0, 0);
			}
			c = i.next();
		}

		if (c instanceof VerticalLayout)
			verticalLayout_1.removeComponent(c);
		if (vLayout.getComponentCount() > 1) {
			clearSearchResultTable();
//			mainWholelayout.removeComponent(searchTableLayout);
		}
//		buttonLayout.removeComponent(dummyLabel);
		buttonLayout.removeComponent(exportToExcelBtn);

	}

	public void refresh() {
		System.out.println("---inside the refresh----");
		resetSearchFields();
	}

	public void searchButtonDisable() {
		searchButton.setEnabled(false);
	}	

}
