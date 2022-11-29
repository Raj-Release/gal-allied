package com.shaic.claim.cvc.auditreport;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.claim.fss.filedetailsreport.FileDetailsReportPresenter;
import com.shaic.claim.omppaymentletterbulk.SearchPrintPaymentBulkFormDTO;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionTable;
import com.shaic.domain.MasReportConfig;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

public class ClaimsAuditReportForm  extends SearchComponent<ClaimsAuditReportFormDTO> {
	
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PopupDateField fromDate;
	private PopupDateField toDate;
	
	private Button xmlReport;
	
	private VerticalLayout vLayout = new VerticalLayout();
	
	protected VerticalLayout printLetterVerticalLayout;
	
	
	private VerticalLayout mainVerticalLayout;
	
	private Button clearButton;
	
	@Inject
	private ClaimsAuditTable claimAuditTableObj;
	
	private ExcelExport excelExport;

	@EJB
	private ClaimsAuditReportService repoConfigService;
	
	private FormLayout toDateFrmLayout = new FormLayout();


	@PostConstruct
	public void init() {
		initBinder();
	}
	
	@SuppressWarnings("serial")
	public void getContent(){
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Claims Audit Report");
		mainVerticalLayout = new VerticalLayout();
		
		
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class); 
		String dateValue = "dd/MM/yyyy";
		fromDate.setDateFormat(dateValue);
		fromDate.setRequired(true);
		
		toDate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDate.setDateFormat(dateValue);
		toDate.setRequired(true);

		mandatoryFields.add(fromDate);
		mandatoryFields.add(toDate);
		showOrHideValidation(false);


	
		printLetterVerticalLayout = new VerticalLayout();
		printLetterVerticalLayout.setMargin(new MarginInfo(false, true, false, true));
		printLetterVerticalLayout.setSpacing(true);
		
		printLetterVerticalLayout = buildGenerateLetterLayout();	
		
		mainVerticalLayout.addComponent(printLetterVerticalLayout);
				
		mainVerticalLayout.setSizeFull();
		
		mainPanel.setContent(mainVerticalLayout);
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout buildGenerateLetterLayout(){
		printLetterVerticalLayout.removeAllComponents();
		/*btnSearch.setCaption("Search");
		btnSearch.setDisableOnClick(true);*/
		
		xmlReport = new Button("Generate Excel");

		
		FormLayout formLayoutLeft = new FormLayout(fromDate);    
		formLayoutLeft.setSpacing(false);
		
		FormLayout formLayoutMiddle = new FormLayout(toDate);	
		formLayoutMiddle.setSpacing(false);

		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle);
		fieldLayout.setSpacing(true);
		
		clearButton = new Button("Reset");
		clearButton.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout buttonhLayout = new HorizontalLayout(xmlReport,clearButton);
		
		buttonhLayout.setSpacing(true);
		
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.setWidth("70%");
		btnLayout.addComponent(buttonhLayout);
		btnLayout.setComponentAlignment(buttonhLayout,  Alignment.MIDDLE_CENTER);
		
		printLetterVerticalLayout.addComponent(fieldLayout);
		printLetterVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		printLetterVerticalLayout.addComponent(btnLayout);
//		printLetterVerticalLayout.setMargin(true);
		printLetterVerticalLayout.setSpacing(true);
		addListener();		
		
		clearButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetFields();
			}
		});
		
		xmlReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
			public void buttonClick(ClickEvent event) {
				String reportDuration = repoConfigService.getReportDuration(SHAConstants.CLAIMS_AUDIT_REPORT);
				ClaimsAuditReportFormDTO searchDTO = getSearchFilters(reportDuration);
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

				if (searchDTO != null) {
					DBCalculationService dbcalculationService = new DBCalculationService();
					List<ClaimsAuditTableDTO> downloadForProvisonExcel = dbcalculationService.getDownloadForClaimAuditExcel(fromDate.getValue(),toDate.getValue());
					
					java.sql.Date from = new java.sql.Date(fromDate.getValue().getTime());
					java.sql.Date to = new java.sql.Date(toDate.getValue().getTime());
					String fromDate = format.format(from);
					String toDate = format.format(to);

					claimAuditTableObj.init("", false, false);

					mainVerticalLayout.addComponent(claimAuditTableObj);
					claimAuditTableObj.setVisible(false);

					for (ClaimsAuditTableDTO searchShadowProvisionDTO : downloadForProvisonExcel) {
						claimAuditTableObj
								.addBeanToList(searchShadowProvisionDTO);
					}
					excelExport = new ExcelExport(claimAuditTableObj.getTable());
					excelExport.excludeCollapsedColumns();
					excelExport.setDisplayTotals(false);
					excelExport.setExportFileName("ClaimsAuditReport"+ new Date() + ".xls");
					excelExport
							.setReportTitle("CLAIMS PROCESSING ERROR & EXCESS PAYMENT IDENTIFIED BY CLAIMS AUDIT TEAM From "+ fromDate + " to "+ toDate);
					CellStyle cs = excelExport.getTitleStyle();
					cs.setAlignment(CellStyle.ALIGN_LEFT);
					cs.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
					Workbook workbook = excelExport.getWorkbook();
					Sheet sheet = workbook.getSheet(excelExport.getSheetName());
					Font f = sheet.getWorkbook().createFont();
					f.setBoldweight(Font.BOLDWEIGHT_BOLD);
					f.setFontHeight((short) 200);
					cs.setFont(f);
					excelExport.setTitleStyle(cs);
					/**/
					excelExport.export();

				}
			}
		});
		return printLetterVerticalLayout;
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<ClaimsAuditReportFormDTO>(ClaimsAuditReportFormDTO.class);
		this.binder.setItemDataSource(new ClaimsAuditReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	
	public void resetFields(){		
		fromDate.setValue(null);
		toDate.setValue(null);
		vLayout.removeAllComponents();	
	}
	

	
	@SuppressWarnings("deprecation")
	public ClaimsAuditReportFormDTO getSearchFilters(String duartion){
		ClaimsAuditReportFormDTO bean = null;
		try {			
				boolean hasError = false;
				StringBuffer errorMsg = new StringBuffer();		
				Date value = fromDate.getValue();

				
			if (fromDate.getValue() == null && toDate.getValue() == null) {
				errorMsg.append("The From and To date Fields are Mandatory.<br>");
				hasError = true;
			} else if (fromDate.getValue() != null && toDate.getValue() == null) {
				errorMsg.append("Date Fields are Mandatory, Please provide To Date Value<br>");
				hasError = true;
			} else if (fromDate.getValue() == null && toDate.getValue() != null) {
				errorMsg.append("Date Fields are Mandatory, Please provide From Date Value<br>");
				hasError = true;
			}  else if(fromDate.getValue().compareTo(new Date()) > 0 && toDate.getValue().compareTo(new Date()) > 0){
				errorMsg.append("Please provide Valid Dates<br>");
				hasError = true;
			} else {
				if (fromDate.getValue() != null && toDate.getValue() != null) {
					long difference = fromDate.getValue().getTime() - toDate.getValue().getTime();
					long differenceDates = difference / (24 * 60 * 60 * 1000);
					if (differenceDates < 0 && Math.abs(differenceDates) >= Integer.valueOf(duartion)) {
						errorMsg.append("The From and To date range should not Exceed " + duartion + " Days.<br>");
						hasError = true;
					} else if (toDate.getValue().before(fromDate.getValue())) {
						errorMsg.append("Enter Valid To Date<br>");
						hasError = true;
					}
				}
			}
				
				
				if(!hasError && binder.isValid()){
					this.binder.commit();	
				}
				
				if(!hasError){
					bean = binder.getItemDataSource().getBean();
					//bean.setBatchId(batchIdTxt.getValue());
				}
				else{
					showErrorMsg(errorMsg.toString());
					bean = null;
				}
				
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void showErrorMsg(String errorMessage) {
		Label label = new Label(errorMessage, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Info");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		return;
	}	
	
	
	
	
	
	
	public VerticalLayout getgenerateLaout(){
		return printLetterVerticalLayout;
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	
}

