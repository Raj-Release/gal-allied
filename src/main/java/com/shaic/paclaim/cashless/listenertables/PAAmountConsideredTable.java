package com.shaic.paclaim.cashless.listenertables;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import ch.meemin.pmtable.PMTreeTable;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.AmountConsideredBean;
import com.shaic.claim.preauth.dto.AmountConsideredField;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PMTableRow;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAAmountConsideredTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected PMTreeTable pmTreeTable;
	
	private PreauthDTO preauthDto;
	
	private TextField coPayTextField;
	
	private TextField considerAmountField;
	
	private Boolean isSumInsuredTable;
	
	private TextField deductibleTxt;
	
	Integer[] considerAmount={2};
	
	Integer[] textFieldItemId = {3};
	
	Integer[] totalAmountItemId={4};
	
	public Integer consideredAmount = 0;
	
	public Integer balanceSumInsuredAmt = 0;
	
	public Integer consideredAmountCopay = 0;
	
	public Integer balanceSumInsuredAmtCopay = 0;
	
	Map<Integer, String> checkboxMap = new HashMap<Integer, String>();
	
	public TextField dummyField;
	
	public TextField otherinsurerDummyField;
	
	public TextField ambulanceChargeField;
	
	public TextField otherinsurerDummyFieldForCalc;
	
	private Table amountconsideredTable;

	private Table balanceSITable;
	
	public TextField hospApprovedAmountTxt;
	
	public TextField postHospApprovedAmountTxt;
	
	public TextField deductibleAmtTxt;
	
	public TextField settledAmtTxt;
	
	public TextField finalApprovedAmtTxt;
	
	public String otherInsurerAmt = "0";

	private HorizontalLayout layout;
	
	public String selectedCopay = "0";
	
	public String selectedCopayForDefault = null;
	
	public String amountAftCopay = "0";
	
	@PostConstruct
	public void init()
	{
		
	}
	
	@SuppressWarnings("unchecked")
	public void initView(PreauthDTO preauthDTO,Boolean isSumInsuredTable) {
		
		
		
		
		this.isSumInsuredTable=isSumInsuredTable;
	
		this.preauthDto = preauthDTO;
		
		checkboxMap.put(2, "Co_Pay");
		
		pmTreeTable = new PMTreeTable("Amount Considered");
		
		pmTreeTable.addContainerProperty("Particulars", AmountConsideredField.class, null );
		pmTreeTable.addContainerProperty("Amount", TextField.class, null);
		
		pmTreeTable.setStyleName(ValoTheme.TABLE_COMPACT);
		pmTreeTable.setWidth("30%");
		
		pmTreeTable.setColumnWidth("Particulars", 300);
		
		pmTreeTable.setHierarchyColumn("Particulars");
		
		if(!isSumInsuredTable){
		
        setupTable(AmountConsideredBean.getPMTableRows());
		}
		else
		{
			setupTable(AmountConsideredBean.getPMTTableRowsforSumInsured());
		}
		
		pmTreeTable.removeItem(1);
		
		
		setCompositionRoot(pmTreeTable);
		
		
	}
	
	private void showErrorPopup(VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
//		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}

	@SuppressWarnings("unchecked")
	public void initTable(final PreauthDTO preauthDTO, HorizontalLayout consideredLabel , HorizontalLayout balanceSILabel, Layout approvedFormLayout, final Boolean isCashless, final Boolean isNeededOtherIns) {
		this.preauthDto = preauthDTO;
		layout = new HorizontalLayout();
		dummyField = new TextField();
		otherinsurerDummyField = new TextField();
		ambulanceChargeField = new TextField();
		
		Map<Integer, String> firstableValues = new HashMap<Integer, String>();
		
		firstableValues.put(0, "Amount Considered");
		firstableValues.put(1, "Co-Pay");
		firstableValues.put(2, "Amount Considered (After Co-Pay)");
		if(!isCashless) {
//			firstableValues.put(0, "Amount Considered");
//			firstableValues.put(1, "Amount Received from other insurers");
//			firstableValues.put(2, "Amount Considered (After other insurers)");
//			firstableValues.put(3, "Co-Pay");
//			firstableValues.put(4, "Amount Considered (After Co-Pay)");
			
			firstableValues.put(0, ReferenceTable.getSeniorCitizenKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? "Amount Considered Excluding Ambulance Charges": "Amount Considered");
			firstableValues.put(1, "Co-Pay");
			firstableValues.put(2, "Ambulance Charges");
			firstableValues.put(3, "Amount Considered");
		}
	
		if(isNeededOtherIns) {
			firstableValues.put(0,  ReferenceTable.getSeniorCitizenKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? "Amount Considered Excluding Ambulance Charges": "Amount Considered");
			firstableValues.put(1, "Amount Received from other insurers");
			firstableValues.put(2, "Amount Considered (After other insurers)");
			firstableValues.put(3, "Co-Pay");
			firstableValues.put(4, "Ambulance Charges");
			firstableValues.put(5, "Amount Considered");
		}
		
		
		Map<Integer, String> secondtableValues = new HashMap<Integer, String>();
		secondtableValues.put(0, "Balance Sum Insured");
		secondtableValues.put(1, "Co-Pay");
		secondtableValues.put(2, "Balance Sum Insured (After Co-Pay)");
		
		for(int j = 0; j < 2; j++) {
			Table table = new Table();
			table.addContainerProperty("Particulars", AmountConsideredField.class, null );
			table.addContainerProperty("Amount", TextField.class, null);
			table.addStyleName("rowheight");
			
			for(int i = 0; i < ((!isCashless && j == 0) ? !isNeededOtherIns ?  4 : 6 : 3); i++) {
				Object addItem = table.addItem(i);
				if(isCashless || j == 1) {
					table.getContainerProperty(i, "Particulars").setValue(new AmountConsideredField( i== 1 ? true : false, j == 0 ? firstableValues.get(i) : secondtableValues.get(i)));
				} else {
					table.getContainerProperty(i, "Particulars").setValue(new AmountConsideredField( i == (!isNeededOtherIns ? 1 : 3) ? true : false, j == 0 ? firstableValues.get(i) : secondtableValues.get(i)));
				}
				
				TextField field = new TextField();
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("80px");
			
				if(i == 0) {
					field.setValue(j == 0 ? (ReferenceTable.getSeniorCitizenKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDTO.getAmbulanceAmountConsidered()).longValue()) : String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDTO.getAmountConsidered()).longValue()) ) : String.valueOf(preauthDTO.getBalanceSI().intValue()) );
//					if(isNeededOtherIns &&  j == 0) {
//						TextField aftOtherInsField = (TextField) table.getContainerProperty(2, "Amount").getValue();
//						field.setValue(aftOtherInsField.getValue());
//					}
				}
				
				field.setReadOnly(true);
				if(!isCashless && i == (!isNeededOtherIns ? 2 : 4)  && j != 1) {
					field.setData(table);
					field.setReadOnly(false);
					field.setNullRepresentation("");
					CSValidator validator = new CSValidator();
					validator.extend(field);
					validator.setRegExp("^[0-9]*$");
					validator.setPreventInvalidTyping(true);
					field.setValue("0");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);
					ambulanceChargeField.setData(table);
					ambulanceChargeField.addValueChangeListener(new ValueChangeListener() {
						
						private static final long serialVersionUID = 6000773489844521325L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							TextField property = (TextField) event.getProperty();
							if(property.getData() != null) {
								Table table = (Table) property.getData();
								AmountConsideredField addedField = (AmountConsideredField) table.getContainerProperty((!isNeededOtherIns ? 1 : 3), "Particulars").getValue();
								
								TextField ambulanceField = (TextField) table.getContainerProperty((!isNeededOtherIns ? 2 : 4), "Amount").getValue();
								ambulanceField.setReadOnly(false);
								ambulanceField.setValue(property.getValue());
								ambulanceField.setReadOnly(true);
								ComboBox percentageTextfield = addedField.getPercentageTextfield();
							    setDynamicValues(percentageTextfield, true, isCashless, true, isNeededOtherIns);
//							    otherinsurerDummyField.setValue(property.getValue());
							}
							
						}
					});
//					field.addValueChangeListener(new ValueChangeListener() {
//						
//						private static final long serialVersionUID = 1191883540492013123L;
//
//						@Override
//						public void valueChange(ValueChangeEvent event) {
//							TextField property = (TextField) event.getProperty();
//							if(property.getData() != null) {
//								Table table = (Table) property.getData();
//								AmountConsideredField addedField = (AmountConsideredField) table.getContainerProperty(3, "Particulars").getValue();
//								ComboBox percentageTextfield = addedField.getPercentageTextfield();
//							    setDynamicValues(percentageTextfield, true, isCashless, true);
////							    otherinsurerDummyField.setValue(property.getValue());
//							}
//						}
//					});
				}
//				if(!isCashless && i == 3 && j != 1) {
//					field.setReadOnly(false);
//					Integer amount = SHAUtils.getIntegerFromString(preauthDTO.getAmountConsidered())  - SHAUtils.getIntegerFromString(field.getValue()) - ;
//					field.setValue(amount.toString());
//					field.setReadOnly(true);
//				}
				
				if(isNeededOtherIns && i == 1 && j != 1) {
					field.setData(table);
					field.setReadOnly(false);
					field.setNullRepresentation("");
					CSValidator validator = new CSValidator();
					validator.extend(field);
					validator.setRegExp("^[0-9]*$");
					validator.setPreventInvalidTyping(true);
					Double doubleValueFromString = SHAUtils.getDoubleValueFromString(preauthDTO.getOtherInsurerAmount());
					field.setValue(String.valueOf(doubleValueFromString.intValue()));
					field.setStyleName(ValoTheme.TEXTFIELD_SMALL);
					field.addValueChangeListener(new ValueChangeListener() {
						
						private static final long serialVersionUID = 1191883540492013123L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							TextField property = (TextField) event.getProperty();
							if(property.getData() != null) {
								Table table = (Table) property.getData();
								AmountConsideredField addedField = (AmountConsideredField) table.getContainerProperty(3, "Particulars").getValue();
								ComboBox percentageTextfield = addedField.getPercentageTextfield();
							    setDynamicValues(percentageTextfield, true, isCashless, true, isNeededOtherIns);
							    otherinsurerDummyField.setValue(property.getValue());
							}
						}
					});
				}
				
				if(isNeededOtherIns && i == 2 && j != 1) {
					field.setReadOnly(false);
					Integer amount = SHAUtils.getIntegerFromString(preauthDTO.getAmountConsidered())  - SHAUtils.getIntegerFromString(preauthDTO.getOtherInsurerAmount());
					field.setValue(amount.toString());
					field.setReadOnly(true);
					
				}
				
				table.getContainerProperty(i, "Amount").setValue(field);
				
				AmountConsideredField addedField = (AmountConsideredField) table.getContainerProperty(i, "Particulars").getValue();
				ComboBox percentageTextfield = addedField.getPercentageTextfield();
				percentageTextfield.setData(table);
				percentageTextfield.setEnabled(false);
				if(j == 0) {
					percentageTextfield.addValueChangeListener(new ValueChangeListener() {
						
						
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							ComboBox component = (ComboBox) event.getProperty();
							setDynamicValues(component, false, isCashless, false, isNeededOtherIns);
//							if(integerFromString < preauthDTO.getProductCopay().intValue()) {
//								VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>  Copay Percentage Should be Above Product Copay. </b>", ContentMode.HTML));
//								layout.setMargin(true);
//								layout.setSpacing(true);
//								showErrorPopup(layout);
//								component.setValue(String.valueOf(preauthDTO.getProductCopay().intValue()));
//							} else {
//								
//							}
			               
						}
					});
				} else {
					percentageTextfield.setEnabled(false);
				}
				
				
//				percentageTextfield.addBlurListener(new BlurListener() {
//					private static final long serialVersionUID = -7848153501331470215L;
//
//					@Override
//					public void blur(BlurEvent event) {
//						TextField component = (TextField) event.getComponent();
//						Integer integerFromString = SHAUtils.getIntegerFromString(component.getValue());
//						if(integerFromString < preauthDTO.getProductCopay().intValue()) {
//							VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>  Copay Percentage Should be Above Product Copay. </b>", ContentMode.HTML));
//							layout.setMargin(true);
//							layout.setSpacing(true);
//							showErrorPopup(layout);
//							component.setValue(String.valueOf(preauthDTO.getProductCopay().intValue()));
//						} else {
//							setDynamicValues(component);
//						}
//		               
//					}
//				});
			}
			AmountConsideredField addedField = (AmountConsideredField) table.getContainerProperty((!isCashless && j==0) ? (!isNeededOtherIns) ?  1  : 3: 1, "Particulars").getValue();
			ComboBox percentageTextfield = addedField.getPercentageTextfield();
			addCoPayPercentage(percentageTextfield, 0,true);
			setDynamicValues(percentageTextfield, false, isCashless, true, isNeededOtherIns);
			VerticalLayout verticalLayout = new VerticalLayout();
			table.setStyleName(ValoTheme.TABLE_COMPACT);
			table.setWidth("30%");
			table.setPageLength(table.getItemIds().size());
			if(j == 0) {
				amountconsideredTable = table;
			} else if(j == 1) {
				balanceSITable = table;
			}
			if(j == 0) {
				
				 verticalLayout = new VerticalLayout(consideredLabel, table);
			} else {
				 verticalLayout = new VerticalLayout(balanceSILabel, table);
			}
			
			layout.addComponent(verticalLayout);
		}
		if(approvedFormLayout != null){
		layout.addComponent(approvedFormLayout);
		}
		layout.setSpacing(true);
		layout.setMargin(true);
		VerticalLayout wholeLayout = new VerticalLayout(layout);
		if(preauthDTO.getNewIntimationDTO() != null) {
			Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
			if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
				hospApprovedAmountTxt = new TextField("Approval Amt(Hosp)");
				//hospApprovedAmountTxt.setValue("0");
				//hospApprovedAmountTxt.setValue(preauthDTO.getAmountConsidedAfterCoPay().toString());
				hospApprovedAmountTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				postHospApprovedAmountTxt = new TextField("Approval Amt (Post Hosp)");
				postHospApprovedAmountTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				postHospApprovedAmountTxt.setValue(preauthDTO.getPostHospitalisationValue());
				deductibleAmtTxt = new TextField("Compulsory Deductible");
				deductibleAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				deductibleAmtTxt.setValue(String.valueOf(preauthDTO.getDeductibleAmount().intValue() > 0 ? preauthDTO.getDeductibleAmount().intValue() : 300000) );
				settledAmtTxt = new TextField("Already Settled in other Policy");
				settledAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				settledAmtTxt.setValue(preauthDTO.getSettledAmount() != null ? String.valueOf(preauthDTO.getSettledAmount().intValue()) : "0");
				finalApprovedAmtTxt = new TextField("Final Approval Amt");
				finalApprovedAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				finalApprovedAmtTxt.setValue(String.valueOf(doSuperSurplusCalculation()));
				hospApprovedAmountTxt.setEnabled(false);
				postHospApprovedAmountTxt.setEnabled(false);
				deductibleAmtTxt.setEnabled(false);
				settledAmtTxt.setEnabled(false);
				finalApprovedAmtTxt.setEnabled(false);
				
				getSuperSurplusListener();
				VerticalLayout vLayout = new VerticalLayout(new FormLayout(hospApprovedAmountTxt, postHospApprovedAmountTxt, deductibleAmtTxt, settledAmtTxt, finalApprovedAmtTxt)  );
				
				wholeLayout.addComponent(vLayout);
			}
		}
		setCompositionRoot(wholeLayout);
	}
	
	protected void getSuperSurplusListener() {
		if(this.hospApprovedAmountTxt != null) {
			this.hospApprovedAmountTxt.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					finalApprovedAmtTxt.setValue(String.valueOf(doSuperSurplusCalculation()));
				}
			});
		}
	}
	
	public Integer doSuperSurplusCalculation() {
//		if(otherinsurerDummyField != null && otherinsurerDummyField.getValue() != null)   {
//			if(SHAUtils.getIntegerFromStringWithComma(otherinsurerDummyField.getValue()) >= SHAUtils.getIntegerFromStringWithComma(deductibleAmtTxt.getValue())) {
//				deductibleAmtTxt.setValue("0");
//			} else if(SHAUtils.getIntegerFromStringWithComma(otherinsurerDummyField.getValue()) < SHAUtils.getIntegerFromStringWithComma(deductibleAmtTxt.getValue())) {
//				Integer amout = SHAUtils.getIntegerFromStringWithComma(deductibleAmtTxt.getValue()) - SHAUtils.getIntegerFromStringWithComma(otherinsurerDummyField.getValue());
//				deductibleAmtTxt.setValue(amout.toString());
//			}
//		}
		Integer hospAndPostHospValue = SHAUtils.getDoubleFromString(hospApprovedAmountTxt.getValue()) +  SHAUtils.getDoubleFromString(postHospApprovedAmountTxt.getValue());
		Integer calculatedValue = hospAndPostHospValue - Math.max(SHAUtils.getIntegerFromStringWithComma(deductibleAmtTxt.getValue()), SHAUtils.getIntegerFromStringWithComma(settledAmtTxt.getValue()));
		Integer resultValue = Math.min(balanceSumInsuredAmt , calculatedValue);
		
		return resultValue < 0 ? 0 : resultValue;
	}
	
	@SuppressWarnings("unused")
	public void setDynamicValues(ComboBox component, Boolean isHappened, Boolean isCashless, Boolean isFirstTime, Boolean isNeededOtherIns) {
		Integer enteredAmt = 0;
		Table table = new Table();
		if(component != null) {
			table = (Table) component.getData();
			enteredAmt = SHAUtils.getFloatFromString(component.getValue() != null ? component.getValue().toString() : "0");
			selectedCopay = String.valueOf(enteredAmt.intValue()) ;
			selectedCopayForDefault = String.valueOf(enteredAmt.intValue()) ;
			preauthDto.setAmountConsCopayPercentage(Double.valueOf(selectedCopay));			
			preauthDto.setBalanceSICopayPercentage(Double.valueOf(selectedCopay));
		} else {
			if(amountconsideredTable != null) {
				table = amountconsideredTable;
			}
			
			enteredAmt = SHAUtils.getFloatFromString(ReferenceTable.getSeniorCitizenKeys().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDto.getAmbulanceAmountConsidered()).longValue()) : String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDto.getAmountConsidered()).longValue()));
		}
		Item firstItem = table.getItem(0);
		AmountConsideredField consideredAmtField =  (AmountConsideredField) firstItem.getItemProperty("Particulars").getValue();
		
		   Item item1 = table.getItem(0);
		   Item item2 = table.getItem(1);
		   Item item3 = table.getItem(2);
		   
		   if(!isCashless && consideredAmtField.getLabel() != null && consideredAmtField.getLabel().toString().contains("Amount Considered")) {
//			   TextField amountConsidredField =  (TextField) firstItem.getItemProperty("Amount").getValue();
//			   TextField insurerField = (TextField) table.getItem(1).getItemProperty("Amount").getValue();
//			   Integer minusValue = SHAUtils.getIntegerFromStringWithComma(amountConsidredField.getValue()) - SHAUtils.getIntegerFromStringWithComma(insurerField.getValue());
//			   if(minusValue < 0) {
//				   minusValue = 0;
//			   }
//			   item1 = table.getItem(2);
//			   TextField amountConsidredAftInsurerField = (TextField) item1.getItemProperty("Amount").getValue();
//			   amountConsidredAftInsurerField.setReadOnly(false);
//			   amountConsidredAftInsurerField.setValue(minusValue.toString());
//			   amountConsidredAftInsurerField.setReadOnly(true);
			   item2 = table.getItem(1);
			   item3 = table.getItem(3);
		   }
		   
		   if(!isCashless && isNeededOtherIns && consideredAmtField.getLabel() != null && consideredAmtField.getLabel().toString().contains("Amount Considered")) {
			   TextField amountConsidredField =  (TextField) firstItem.getItemProperty("Amount").getValue();
			   TextField insurerField = (TextField) table.getItem(1).getItemProperty("Amount").getValue();
			   Integer minusValue = SHAUtils.getIntegerFromStringWithComma(amountConsidredField.getValue()) - SHAUtils.getIntegerFromStringWithComma(insurerField.getValue());
			   if(minusValue < 0) {
				   minusValue = 0;
			   }
			   item1 = table.getItem(2);
			   TextField amountConsidredAftInsurerField = (TextField) item1.getItemProperty("Amount").getValue();
			   amountConsidredAftInsurerField.setReadOnly(false);
			   amountConsidredAftInsurerField.setValue(minusValue.toString());
			   amountConsidredAftInsurerField.setReadOnly(true);
			   item2 = table.getItem(3);
			   item3 = table.getItem(5);
		   }
		   
		   AmountConsideredField consideredField =  (AmountConsideredField) item2.getItemProperty("Particulars").getValue();
		   
		   TextField field =  (TextField) item1.getItemProperty("Amount").getValue();
		   
		   
		   TextField copayField =  (TextField) item2.getItemProperty("Amount").getValue();
		   if(component == null) {
			   field.setReadOnly(false);
//			   field.setValue(preauthDto.getAmountConsidered());
			   field.setValue(ReferenceTable.getSeniorCitizenKeys().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDto.getAmbulanceAmountConsidered()).longValue()) : String.valueOf(SHAUtils.getDoubleFromStringWithComma(preauthDto.getAmountConsidered()).longValue()));
			   field.setReadOnly(true);
			   enteredAmt = SHAUtils.getIntegerFromString(copayField.getValue());
			   if(consideredField != null) {
				   ComboBox percentageTextfield2 =   consideredField.getPercentageTextfield();
				   enteredAmt = SHAUtils.getIntegerFromString(percentageTextfield2.getValue() != null ? percentageTextfield2.getValue().toString() : "0" );
			   }
		   }
		   
		  // ComboBox percentageTextfield = consideredField.getPercentageTextfield();
		   
		  
		   TextField calculatedField =  (TextField) item3.getItemProperty("Amount").getValue();
		   Float copayValue =  SHAUtils.getIntegerFromString(field.getValue()).floatValue() *  enteredAmt.floatValue()/100;
		   int round = Math.round(copayValue);
		   copayField.setReadOnly(false);
		   copayField.setValue(String.valueOf(round));
		   copayField.setReadOnly(true);
			
		  Integer calculatedValue =  SHAUtils.getDoubleFromString(field.getValue()).intValue() - round;
		  
		  if(!isCashless && consideredAmtField.getLabel() != null && consideredAmtField.getLabel().toString().contains("Amount Considered")) {
			  Item ambulanceItem = table.getItem( !isNeededOtherIns ? 2 : 4);
			  TextField ambulanceField =  (TextField) ambulanceItem.getItemProperty("Amount").getValue();
			  
			  calculatedValue += SHAUtils.getIntegerFromStringWithComma(ambulanceField.getValue());
			  if(calculatedValue < 0) { 
				  calculatedValue = 0;
			  }
		  }
		  
		  calculatedField.setReadOnly(false);
		  calculatedField.setValue(String.valueOf(calculatedValue));
		  calculatedField.setReadOnly(true);
		  
		  if(consideredAmtField.getLabel() != null && consideredAmtField.getLabel().toString().contains("Amount Considered")) {
			   consideredAmount = calculatedValue;
			   consideredAmountCopay = enteredAmt;
		   } else if(consideredAmtField.getLabel() != null && consideredAmtField.getLabel().toString().contains("Balance Sum Insured")) {
			   balanceSumInsuredAmt = calculatedValue;
			   balanceSumInsuredAmtCopay = enteredAmt;
			   ComboBox percentageTextfield = consideredField.getPercentageTextfield();
		   }
		  dummyField.setValue(enteredAmt.toString());
		  
		  if(!isHappened && balanceSITable != null) {
			  AmountConsideredField addedField = (AmountConsideredField) balanceSITable.getContainerProperty(1, "Particulars").getValue();
			  ComboBox percentageTextfield = addedField.getPercentageTextfield();
//			  percentageTextfield.setData(balanceSITable);
//			  percentageTextfield.setContainerDataSource(component.getContainerDataSource());
//			  percentageTextfield.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			  percentageTextfield.setItemCaptionPropertyId("value");
			  percentageTextfield.setNullSelectionAllowed(false);
			  Collection<?> itemIds = percentageTextfield.getContainerDataSource().getItemIds();
			  percentageTextfield.setEnabled(false);
			  addCoPayPercentage(percentageTextfield, ((SelectValue)component.getValue()).getId().intValue(), isFirstTime);
			  setDynamicValues(percentageTextfield, true, isCashless, true, isNeededOtherIns);
			  //percentageTextfield.setValue(itemIds.toArray()[((SelectValue)component.getValue()).getId().intValue()]);
//			  percentageTextfield.select(itemIds.toArray()[((SelectValue)component.getValue()).getId().intValue()]);
////			  percentageTextfield.setValue(((SelectValue)component.getValue()));
////			  percentageTextfield.setValue(null);
//			  setDynamicValues(percentageTextfield, true);
		  }
	}
	
	public void setApprovedAmtForViews(String hosApprovedAmt,String finalApprovedAmt){
		
		if(hospApprovedAmountTxt != null){
			hospApprovedAmountTxt.setValue(hosApprovedAmt);
		}
		
		if(finalApprovedAmtTxt != null){
			finalApprovedAmtTxt.setValue(finalApprovedAmt);
		}
		
		
	}
	
	
	 public void addCoPayPercentage(ComboBox comboBox, Integer index, Boolean isFirstTime) {
		   BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		  
		    Long i = 0l;
		    for (Double copayValue : this.preauthDto.getProductCopay()) {
		    	SelectValue value = new SelectValue();
		    	value.setId(i);
		    	value.setValue(String.valueOf(copayValue.intValue()));
		    	coPayContainer.addBean(value);
		    	i++;
			}
		    
		    
		    for (SelectValue values : coPayContainer.getItemIds()) {
				if(values.getValue() != null && preauthDto.getAmountConsCopayPercentage() != null && Long.valueOf(values.getValue().toString()).equals(preauthDto.getAmountConsCopayPercentage())) {
					if(isFirstTime) {
						index = values.getId() != null ? values.getId().intValue() : 0;
					}
					
				}
			}
			comboBox.setContainerDataSource(coPayContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			comboBox.setNullSelectionAllowed(false);

			if (preauthDto.getIsDefaultCopay() && !preauthDto.getIsCopaySelected()){
				String string = preauthDto.getDefaultCopayStr();
				for (SelectValue values : coPayContainer.getItemIds()) {
					if(values.getValue() != null && string != null && values.getValue().toString().equalsIgnoreCase(string)) {
						index = values.getId() != null ? values.getId().intValue() : 0;
					}
				}
			}


			
			Collection<?> itemIds = comboBox.getContainerDataSource().getItemIds();
			
			
			
			if(!itemIds.isEmpty()) {
				comboBox.setValue(itemIds.toArray()[index]);
				selectedCopay =  comboBox.getValue() != null ? ((SelectValue)comboBox.getValue()).getValue() : "0";
				selectedCopayForDefault =  comboBox.getValue() != null ? ((SelectValue)comboBox.getValue()).getValue() : "0";
				preauthDto.setIsCopaySelected(true);
				preauthDto.setAmountConsCopayPercentage(Double.valueOf(selectedCopay));			
				preauthDto.setBalanceSICopayPercentage(Double.valueOf(selectedCopay));
			}
	 }
	
	public Integer getMinimumValue()
	{
		return Math.min(consideredAmount, balanceSumInsuredAmt);
		
	}
	
	public Integer getConsideredAmountValue()
	{
		return consideredAmount;
		
	}
	
	public Integer getConsideredAmountCopayValue()
	{
		return consideredAmountCopay;
		
	}
	
	public Integer getBalanceSICopayValue()
	{
		return balanceSumInsuredAmtCopay;
		
	}
	
	public String getCoPayValue(){
		return selectedCopay;
	}
	
	public Integer getBalanceSumInsuredAmt(){
		return balanceSumInsuredAmt;
	}
	
	private void setupTable(PMTableRow root)
	{
		addTreeMenuItem(root, null);
		
	}
	
	@SuppressWarnings("unchecked")
	private void addTreeMenuItem(PMTableRow menu, Object parent) {
		
		Object itemId = pmTreeTable.addItem();
		Item parentItem = pmTreeTable.getItem(itemId);
		
		AmountConsideredField amountConsideredField;
		
		System.out.println(itemId.toString());
		
		if(Arrays.asList(this.textFieldItemId).contains(itemId)) {
			amountConsideredField=new AmountConsideredField(true, menu.getDetailLabel(), checkboxMap.get(itemId));	
		}else
		{
			amountConsideredField=new AmountConsideredField(false,menu.getDetailLabel(), null);
		}
		
		amountConsideredField.setData(menu.getMasterId());
		parentItem.getItemProperty("Particulars").setValue(amountConsideredField);
		
		Property deductiblesProperty = parentItem.getItemProperty("Amount");
		deductibleTxt = new TextField();
		
         if(Arrays.asList(this.considerAmount).contains(itemId)&&! isSumInsuredTable) {
        	 
			deductibleTxt.setValue(preauthDto.getAmountConsidered());                                                 //need to implement
         }
//		coPayTextField = amountConsideredField.getPercentageTextfield();
		
		if(!isSumInsuredTable){
			coPayTextField.setValue("30");                                                   //need to implement with listener
			addListener();
		}
		
		if(Arrays.asList(this.textFieldItemId).contains(itemId) && !isSumInsuredTable) {
			deductibleTxt.setValue("12975");                                                //need to implement with listener
		}
		
		CSValidator deductibleValidator = new CSValidator();
		
		deductibleValidator.extend(deductibleTxt);
		deductibleValidator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
		deductibleValidator.setPreventInvalidTyping(true);
		
		deductibleTxt.setWidth("80px");
		deductibleTxt.setData(itemId);
		deductibleTxt.setNullRepresentation("0");
		deductiblesProperty.setValue(deductibleTxt);
		
		pmTreeTable.setCollapsed(itemId, false);
		
		if (parent != null) {
			pmTreeTable.setParent(itemId, parent);
		}
		
		if (menu.hasChild()) {
			pmTreeTable.setChildrenAllowed(itemId, true);
			for (PMTableRow childItem : menu.getChildRow()) {
				addTreeMenuItem(childItem, itemId);
			}
		}
		else {
			pmTreeTable.setChildrenAllowed(itemId, false);
		}
		
	}
	
	private void addListener(){
		
		coPayTextField.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
              
				Integer enteredAmt = SHAUtils.getFloatFromString(component.getValue());
				
			}
		});
	}
	
	public Boolean isValid() {
		if(this.amountconsideredTable != null) {
			Item item = amountconsideredTable.getItem(1);
			TextField otherInsurerField = (TextField) item.getItemProperty("Amount").getValue();
			if(otherInsurerField.getValue() == null || (otherInsurerField.getValue() != null && otherInsurerField.getValue().length() == 0)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	public void setBalanceSumInsuredAfterRecharge(Double balanceSI){
		preauthDto.setBalanceSI(balanceSI);
		if(balanceSITable !=  null) {
			Item item1 = balanceSITable.getItem(0);
			Item item2 = balanceSITable.getItem(1);
			Item item3 = balanceSITable.getItem(2);
			TextField amountField =  (TextField) item1.getItemProperty("Amount").getValue();
			TextField copayTxtField =  (TextField) item2.getItemProperty("Amount").getValue();
			TextField resultantField =  (TextField) item3.getItemProperty("Amount").getValue();
			amountField.setReadOnly(false);
			amountField.setValue(String.valueOf(balanceSI.intValue()));
			amountField.setReadOnly(true);
			AmountConsideredField consideredAmtField =  (AmountConsideredField) item2.getItemProperty("Particulars").getValue();
			ComboBox percentageTextfield = consideredAmtField.getPercentageTextfield();
			if(percentageTextfield != null && percentageTextfield.getValue() != null) {
				Integer enteredAmt = SHAUtils.getIntegerFromString(percentageTextfield.getValue() != null ? percentageTextfield.getValue().toString() : "0" );
				  Float copayValue =  SHAUtils.getIntegerFromString(amountField.getValue()).floatValue() *  enteredAmt.floatValue()/100;
				   int round = Math.round(copayValue);
				   copayTxtField.setReadOnly(false);
				   copayTxtField.setValue(String.valueOf(round));
				   copayTxtField.setReadOnly(true);
			}
			
			if(resultantField != null){
				Float resultValue = SHAUtils.getIntegerFromString(amountField.getValue()).floatValue() - SHAUtils.getIntegerFromString(copayTxtField.getValue()).floatValue();
				int round = Math.round(resultValue);
				resultantField.setReadOnly(false);
				resultantField.setValue(String.valueOf(round));
				balanceSumInsuredAmt = round;
				resultantField.setReadOnly(true);
			}
		}
		
	}
	
	public String getOtherInsurerAmount() {
		if(this.amountconsideredTable != null) {
			Item item = amountconsideredTable.getItem(1);
			TextField otherInsurerField = (TextField) item.getItemProperty("Amount").getValue();
			if(otherInsurerField.getValue() != null && otherInsurerField.getValue().length() > 0) {
				return otherInsurerField.getValue();
			}
		}
		return "";
	}
	
	public Integer getApprovedAmount() {
		if(this.finalApprovedAmtTxt != null) {
			return SHAUtils.getIntegerFromStringWithComma(finalApprovedAmtTxt.getValue());
		}
		return 0;
	}
	
}
