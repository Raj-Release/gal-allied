package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview.PAClaimAprNonHosReviewPagePresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview.PANonHospFinancialReviewPagePresenter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class TableBenefitsListenerTable extends ViewComponent {

	private Table table;

	BeanItemContainer<TableBenefitsDTO> data = new BeanItemContainer<TableBenefitsDTO>(TableBenefitsDTO.class);
	private Map<TableBenefitsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<TableBenefitsDTO, HashMap<String, AbstractField<?>>>();

	PreauthDTO bean;

	private BeanItemContainer<SelectValue> coverContainer;

	public Object[] VISIBLE_COLUMNS = new Object[] { "slNo", "classification",
			"duration", "billNo", "billDate", "billAmount", "deduction",
			"netAmount", "eligibleAmount", "approvedAmount",
			"reasonForDeduction" };

	public TextField netAmtText = new TextField();

	public TextField approvalAmtText = new TextField();

	public TextField payableToInsuredAmtText = new TextField();

	public OptionalCoversListenerTable optionalCoversListenerTableObj = new OptionalCoversListenerTable();

	@EJB
	private DBCalculationService dbCalculationService;

	private Double availableSI = 0d;

	private String fileName;

	@SuppressWarnings("deprecation")
	private TextArea pcfnonHospitalFlaggedRemarks;

	public void init(PreauthDTO bean) {
		this.bean = bean;
		this.availableSI = (null != bean.getPreauthDataExtractionDetails()
				.getAvailableSI() && !bean.getPreauthDataExtractionDetails()
				.getAvailableSI().equals("")) ? Double.valueOf(bean
				.getPreauthDataExtractionDetails().getAvailableSI()) : 0d;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		String benefitName = "";
		if (this.bean.getPreauthDataExtractionDetails() != null) {
			if (this.bean.getPreauthDataExtractionDetails().getPaBenefits() != null) {
				benefitName = this.bean.getPreauthDataExtractionDetails()
						.getPaBenefits().getValue();
			}
		}
		table = new Table(benefitName, data);
		// table.setCaption(table);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnHeader("slNo.no", "Sl.No");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("duration", "Duration");
		table.setColumnHeader("billNo", "Bill no");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("billAmount", "Bill Amount");
		table.setColumnHeader("deduction", "Deduction");
		table.setColumnHeader("netAmount", "Net Amount");
		table.setColumnHeader("eligibleAmount", "Eligible Amount(Product)");
		table.setColumnHeader("approvedAmount", "Approved Amount");
		table.setColumnHeader("reasonForDeduction", "Reason For Deductions");

		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

		table.setFooterVisible(true);

		table.setColumnFooter("billDate", "Total");

		layout.addComponent(table);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/* private static final long serialVersionUID = -2192723245525925990L; */

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			TableBenefitsDTO tableBenefitsDTO = (TableBenefitsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(tableBenefitsDTO) == null) {
				tableItem.put(tableBenefitsDTO,
						new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(tableBenefitsDTO);

			if (tableItem.get(tableBenefitsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(tableBenefitsDTO,
						new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(tableBenefitsDTO);

			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(tableBenefitsDTO);
				tableRow.put("slNo", field);
				field.setReadOnly(true);
				return field;
			} else if ("classification".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setData(tableBenefitsDTO);
				tableRow.put("classification", field);
				field.setNullSelectionAllowed(false);
				addCoversValues(field);
				if (null != bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement()
						&& bean.getPreauthDataExtractionDetails()
								.getReconsiderationFlag() != null
						&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
								.getPreauthDataExtractionDetails()
								.getReconsiderationFlag())) {
					field.setEnabled(false);
				}
				field.setEnabled(false);
				return field;
			}

			else if ("duration".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				// field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(tableBenefitsDTO);
				tableRow.put("duration", field);
				generateSlNo(field);
				// field.setReadOnly(true);
				return field;
			} else if ("billNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");

				// field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(tableBenefitsDTO);
				tableRow.put("billNo", field);
				// field.setEnabled(false);
				if (null != bean.getPreauthDataExtractionDetails()
						.getReconsiderationFlag()
						&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
								.getPreauthDataExtractionDetails()
								.getReconsiderationFlag())) {
					field.setEnabled(true);
				}

				// field.setReadOnly(true);
				return field;
			} else if ("billDate".equals(propertyId)) {
				// DateField field = new DateField();
				PopupDateField field = new PopupDateField();
				// field.setNullRepresentation("");
				// field.setReadOnly(true);
				// field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(tableBenefitsDTO);
				field.setDateFormat("dd/MM/yyyy");
				tableRow.put("billDate", field);
				if (null != bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement()
						&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
								.getPreauthDataExtractionDetails()
								.getDocAckknowledgement()
								.getReconsiderationRequest())) {
					field.setEnabled(true);
				}
				field.setValidationVisible(false);
				field.setTextFieldEnabled(false);
				return field;
			} else if ("netAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(tableBenefitsDTO);
				tableRow.put("netAmount", field);
				// addNetAmountListener(field);
				return field;
			} else if ("eligibleAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(tableBenefitsDTO);
				tableRow.put("eligibleAmount", field);
				return field;
			}

			else if ("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				// field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(tableBenefitsDTO);
				field.setReadOnly(Boolean.TRUE);
				// addNetAmountListener(field);
				tableRow.put("approvedAmount", field);
				return field;
			} else if ("reasonForDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(1000);
				field.setWidth("200px");
				field.setData(tableBenefitsDTO);
				tableRow.put("reasonForDeduction", field);
				handleEnter(field, null);
				// ADD only For product ACC_PRD-020
				if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
						&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY))
				{
					tableBenefitsDTO.setApprovedAmount(0d);
					if(tableBenefitsDTO.getApprovedAmount() != null && tableBenefitsDTO.getApprovedAmount() >= 0) {
					
						TextField component = (TextField) tableRow.get("approvedAmount");
						if(component != null) {
							addNetAmountListener(field);
						}
						
					}
				}
				return field;
			} else if ("billAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				// field.setReadOnly(true);
				addNetAmountListener(field);
			//	field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
						&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY))
				{
					tableBenefitsDTO.setBillAmount(availableSI);
//					field.setReadOnly(true);
				}
				field.setData(tableBenefitsDTO);
				tableRow.put("billAmount", field);
				if (null != bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement()
						&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
								.getPreauthDataExtractionDetails()
								.getDocAckknowledgement()
								.getReconsiderationRequest())) {
					field.setEnabled(true);
				}
				return field;
			} else if ("deduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				// field.setReadOnly(true);
				// field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(tableBenefitsDTO);
				addNetAmountListener(field);
				tableRow.put("deduction", field);
				if (null != bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement()
						&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
								.getPreauthDataExtractionDetails()
								.getDocAckknowledgement()
								.getReconsiderationRequest())) {
					field.setEnabled(true);
				}
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				return field;
			}

		}
	}

	public void addNetAmountListener(final TextField total) {

		if (null != total) {

			total.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateNetAmount(total);
					calculateTotal();
					// calculateTotalAmount(total);

				}
			});
		}
	}

	private void calculateNetAmount(TextField field) {
		TableBenefitsDTO dto = (TableBenefitsDTO) field.getData();
		if (null != dto) {
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
			TextField billAmtFld = (TextField) hashMap.get("billAmount");
			TextField deductionsFld = (TextField) hashMap.get("deduction");
			TextField netAmtFld = (TextField) hashMap.get("netAmount");
			TextField eligibleAmtFld = (TextField) hashMap
					.get("eligibleAmount");
			TextField approvalAmtFld = (TextField) hashMap
					.get("approvedAmount");
			Double netAmt = 0d;
			Double billAmt = 0d;
			Double dedAmt = 0d;
			Double approvalAmt = 0d;
			NumberFormat format = NumberFormat.getInstance(Locale.US);

			try {

				if (null != billAmtFld && null != billAmtFld.getValue()) {
					Number number = format.parse(billAmtFld.getValue());
					billAmt = number.doubleValue();
				}
				if (null != deductionsFld && null != deductionsFld.getValue()) {
					Number number = format.parse(deductionsFld.getValue());
					dedAmt = number.doubleValue();
				}

				if (dedAmt > billAmt) {
					showDeleteRowsPopup("Deduction Amount cannot be greater than bill amount");
					deductionsFld.setValue(null);
				} else {
					netAmt = billAmt - dedAmt;
					if (null != eligibleAmtFld
							&& null != eligibleAmtFld.getValue()) {
						Number number = format.parse(eligibleAmtFld.getValue());
						approvalAmt = Math.min(netAmt, number.doubleValue());
						approvalAmtFld.setReadOnly(Boolean.FALSE);
						approvalAmtFld.setValue(String.valueOf(approvalAmt));
						approvalAmtFld.setReadOnly(Boolean.TRUE);
					}
					if (null != netAmtFld && null != netAmt) {
						netAmtFld.setReadOnly(Boolean.FALSE);
						netAmtFld.setValue(String.valueOf(netAmt));
						netAmtFld.setReadOnly(Boolean.TRUE);
					}
				}

				// netAmtText.setValue(String.valueOf(netAmtFld));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void calculateTotal()
	{
		List<TableBenefitsDTO> tableList = (List<TableBenefitsDTO>) table.getItemIds();
		Double netAmt = 0d;
		Double appAmt = 0d;
		Double eligibleAmount =0d;
		Double billAmount = 0d;
		Double deductionAmount = 0d;
		if(null != tableList && !tableList.isEmpty())
		{
			for (TableBenefitsDTO tableBenefitsDTO : tableList) {
				netAmt += tableBenefitsDTO.getNetAmount();
				if(tableBenefitsDTO.getApprovedAmount()!=null){
					appAmt += tableBenefitsDTO.getApprovedAmount();
				}
				if(null != tableBenefitsDTO.getEligibleAmount())
				{
					eligibleAmount += tableBenefitsDTO.getEligibleAmount();
				}
				if(null != tableBenefitsDTO.getBillAmount())
				{
					billAmount += tableBenefitsDTO.getBillAmount();
				}
				if(null != tableBenefitsDTO.getDeduction())
				{
					deductionAmount += tableBenefitsDTO.getDeduction();
				}

			}
			double payableToInsured = Math.min(eligibleAmount, Math.min(appAmt, netAmt));
			payableToInsuredAmtText.setCaption("payableToInsuredAmtText");
//			payableToInsuredAmtText.setValue(String.valueOf(0d));
			payableToInsuredAmtText.setValue(String.valueOf(new BigDecimal(Math.min(availableSI, payableToInsured))));
			table.setColumnFooter("billAmount", String.valueOf(new BigDecimal(billAmount)));
			table.setColumnFooter("eligibleAmount", String.valueOf(new BigDecimal(eligibleAmount)));
			table.setColumnFooter("deduction", String.valueOf(new BigDecimal(deductionAmount)));
			table.setColumnFooter("netAmount", String.valueOf(new BigDecimal(netAmt)));
			table.setColumnFooter("approvedAmount", String.valueOf(new BigDecimal(appAmt)));
			netAmtText.setValue(String.valueOf(new BigDecimal(netAmt)));
			approvalAmtText.setValue(String.valueOf(new BigDecimal(appAmt)));
			///

			optionalCoversListenerTableObj.setCaption("Part III - Optional Covers");
			if(appAmt != null ){
				Boolean optionalCover = false;
				List<OptionalCoversDTO> optCoverDTOListPrc = null;
				List<OptionalCoversDTO> optionalCoverDtls = bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling();
				for (OptionalCoversDTO optionalCoversDTO : optionalCoverDtls) {
					String addOnValue = optionalCoversDTO.getOptionalCover().getValue();
					if(addOnValue != null ){
						if(addOnValue.equalsIgnoreCase("MEDICAL EXTENSION")){
							optionalCover = true;
							break;
						}	
						optionalCover = false;
					}
				}
				String approvedAmt = String.valueOf(new BigDecimal(appAmt));
				if(optionalCover && appAmt != null && appAmt > 0){
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
							(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
						 optCoverDTOListPrc = dbCalculationService.getGPAOptValuesForMedicalExtension(bean.getKey(),SHAUtils.getLongFromString(approvedAmt));
	
					}else{
					     optCoverDTOListPrc = dbCalculationService.getOptValuesForMedicalExtension(bean.getKey(),SHAUtils.getLongFromString(approvedAmt));
					}
					//					 bean.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(paOptionalCoverListByRodKey);
					//					 List<OptionalCoversDTO> optionalCoverDtls = bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling();
					for (OptionalCoversDTO optionalCoversDTO : optionalCoverDtls) {
						if(null != optCoverDTOListPrc && !optCoverDTOListPrc.isEmpty())
						{
							for (OptionalCoversDTO optionalCoversDTOList : optCoverDTOListPrc) {
								/*if(coverId.equals(optionalCoversDTO.getCoverId()))
									{*/
								if(optionalCoversDTO.getOptionalCover() !=null && optionalCoversDTO.getOptionalCover().getId() !=null && 
										optionalCoversDTOList.getCoverId()!=null &&
										optionalCoversDTO.getOptionalCover().getId().equals(optionalCoversDTOList.getCoverId())){
									optionalCoversDTO.setNoOfDaysUtilised(optionalCoversDTOList.getNoOfDaysUtilised());
									optionalCoversDTO.setNoOfDaysAvailable(optionalCoversDTOList.getNoOfDaysAvailable());
									optionalCoversDTO.setAllowedAmountPerDay(optionalCoversDTOList.getAllowedAmountPerDay());
									optionalCoversDTO.setMaxNoOfDaysPerHospital(optionalCoversDTOList.getMaxNoOfDaysPerHospital());
									optionalCoversDTO.setMaxDaysAllowed(optionalCoversDTOList.getMaxDaysAllowed());
									optionalCoversDTO.setSiLimit(optionalCoversDTOList.getSiLimit());
									optionalCoversDTO.setLimit(optionalCoversDTOList.getLimit());
									optionalCoversDTO.setBalanceSI(optionalCoversDTOList.getBalanceSI());
									break;
								}
							}
						}
					}
					if(optionalCoverDtls !=null && !optionalCoverDtls.isEmpty() && (bean.getScreenName() != null &&
							bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_BILLING_NON_HOSP))){
						fireViewEvent(PABillingReviewPagePresenter.PA_POPULATE_OPTIONAL_COVERS, optionalCoverDtls);
					}else if(optionalCoverDtls !=null && !optionalCoverDtls.isEmpty() && (bean.getScreenName() != null &&
							bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_CLAIM_APPROVAL_NON_HOSP))){
						fireViewEvent(PAClaimAprNonHosReviewPagePresenter.PA_POPULATE_OPTIONAL_COVERS_CLAIM_APPROVAL, optionalCoverDtls);
					}else if(optionalCoverDtls !=null && !optionalCoverDtls.isEmpty() && (bean.getScreenName() != null &&
							bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_FINANCIAL_NON_HOSP))){
						fireViewEvent(PANonHospFinancialReviewPagePresenter.PA_POPULATE_OPTIONAL_COVERS_FINANCIAL_APPROVAL_NON_HOSP, optionalCoverDtls);
					}
				}
			}

		}
	}

	private void generateSlNo(TextField txtField) {

		Collection<TableBenefitsDTO> itemIds = (Collection<TableBenefitsDTO>) table
				.getItemIds();

		int i = 0;
		for (TableBenefitsDTO tableBenefitsDTO : itemIds) {
			i++;
			HashMap<String, AbstractField<?>> hashMap = tableItem
					.get(tableBenefitsDTO);
			if (null != hashMap && !hashMap.isEmpty()) {
				TextField itemNoFld = (TextField) hashMap.get("itemNo");
				if (null != itemNoFld) {
					itemNoFld.setValue(String.valueOf(i));
					itemNoFld.setEnabled(false);
				}
			}
		}

	}

	public void setDropDownValues(BeanItemContainer<SelectValue> coverContainer) {
		this.coverContainer = coverContainer;
	}

	public void addCoversValues(GComboBox comboBox) {
		// BeanItemContainer<SelectValue> fileTypeContainer = null;
		comboBox.setContainerDataSource(coverContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}

	public void addBeanToList(TableBenefitsDTO tableBenefitsDTO) {
		data.addItem(tableBenefitsDTO);
	}

	public List<TableBenefitsDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<TableBenefitsDTO> itemIds = (List<TableBenefitsDTO>) this.table
				.getItemIds();
		return itemIds;
	}

	private void showDeleteRowsPopup(String message) {

		Label successLabel = new Label(
				"<b style = 'color: green;'> " + message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton,
				Alignment.MIDDLE_RIGHT);

		// horizontalLayout.setComponentAlignment(homeButton,
		// Alignment.BOTTOM_RIGHT);
		// horizontalLayout.setComponentAlignment(cancelButton,
		// Alignment.BOTTOM_RIGHT);

		VerticalLayout layout = new VerticalLayout(successLabel,
				horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

		final Window dialog = new Window();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		if (getUI().getCurrent().getPage().getWebBrowser().isIE()
				&& ((bean.getFileName() != null && bean.getFileName().endsWith(
						".PDF")) || (bean.getFileName() != null && bean
						.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			// dialog.setDraggable(true);

		}
		getUI().getCurrent().addWindow(dialog);
		// dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	}

	public void handleEnter(TextField searchField, final Listener listener) {
		ShortcutListener enterShortCut = new ShortcutListener("EnterShortcut",
				ShortcutAction.KeyCode.F8, null) {
			private static final long serialVersionUID = -2267576464623389044L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcut(searchField, getShortCutListener(searchField));
	}

	private ShortcutListener getShortCutListener(final TextField txtFld) {
		ShortcutListener listener = new ShortcutListener("EnterShortcut",
				KeyCodes.KEY_F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout = new VerticalLayout();
				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);

				TableBenefitsDTO tableBenefitsDTO = (TableBenefitsDTO) txtFld
						.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem
						.get(tableBenefitsDTO);
				TextField txtItemValue = (TextField) hashMap
						.get("reasonForDeduction");

				if (null != vLayout && vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}

				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(1000);
				// txtArea.setData(bean);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(21);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);

				txtArea.setValue(tableBenefitsDTO.getReasonForDeduction());
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea) event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
					}
				});

				tableBenefitsDTO.setReasonForDeduction(txtArea.getValue());
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.TOP_CENTER);

				final Window dialog = new Window();
				String strCaption = "Reason For Deductions";
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				dialog.setCaption(strCaption);
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	public void handleShortcut(final TextField textField,
			final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				textField.removeShortcutListener(shortcutListener);
			}
		});
	}
}
