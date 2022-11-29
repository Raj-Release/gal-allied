package com.shaic.newcode.wizard.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthDataExtractionPage;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class NomineeDetailsTable extends GBaseTable<NomineeDetailsDto>{
		
	private final static Object COLUM_HEADER[] = new Object[] {
		"sno","nomineeName", "nomineeDob", "nomineeAge", "nomineeRelationship", "nomineePercent",
			"appointeeName", "appointeeAge", "appointeeRelationship","paymentMode", "payableAt", 
			"preference", "accType", "ifscCode", "accNumber", "nameAsPerBankAc", "bankName", 
			"bankBranchName", "bankCity", "micrCode", "panNumber", "virtualPayAdd", "effFrmDate", "effToDate"};
	
	private final static Object VIEW_COLUM_HEADER[] = new Object[] {
		"sno","nomineeName", "nomineeDob", "nomineeAge", "nomineeRelationship", "nomineePercent",
			"appointeeName", "appointeeAge", "appointeeRelationship" };
	
	private TextField legalHeirFirstNameTxt;
	private TextField legalHeirMiddleNameTxt;
	private TextField legalHeirLastNameTxt;
	private TextArea legalHeirAddressTxt;
	
	private FormLayout legalFormLayout;
	
	private String presenterString;

	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private String policySource;
	
	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
	
	private ReceiptOfDocumentsDTO bean;
	
	private String screenName = "";
	
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {

		table.setCaption("Nominee Details ");
		BeanItemContainer<NomineeDetailsDto> nomineeDetailsContainer = new BeanItemContainer<NomineeDetailsDto>(NomineeDetailsDto.class);

		table.setContainerDataSource(nomineeDetailsContainer);
		table.setVisibleColumns(COLUM_HEADER);		
		
		bankDetailsTableObj =  bankDetailsTableInstance.get();
		
		generatePaymentRelatedFields();
		
		table.setColumnWidth("appointeeRelationship",150);
		table.setWidth("100%");
		tablesize();
		
		legalFormLayout = new FormLayout();
		
//		validateSelected();
	}
	
	public void setViewColumnDetails() {
		table.setVisibleColumns(VIEW_COLUM_HEADER);
	}

	public void generateSelectColumn() {
		table.removeGeneratedColumn("Select");
		table.addGeneratedColumn("Select",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							CheckBox selectedNominee = new CheckBox("");
							
							selectedNominee.setData(itemId);
							
							selectedNominee.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									
									CheckBox chkbox = event.getProperty() != null ? (CheckBox)event.getProperty() : null;
									
									NomineeDetailsDto itemId = (NomineeDetailsDto) chkbox.getData();
									
									if(chkbox != null && chkbox.getValue() != null && chkbox.getValue()) {
										itemId.setSelectedNominee(chkbox.getValue());
									}
									else{
										itemId.setSelectedNominee(false);
									}
//									validateSelected();
									
								}
							});
							
							if(((NomineeDetailsDto)itemId).isSelectedNominee() ){
								selectedNominee.setValue(true);
								//Vaadin8-setImmediate() selectedNominee.setImmediate(true);
							}
					        return selectedNominee;
						}						 
				}
		);
	}
	
	/*public void validateSelected() {
		
		if(table.getItemIds() != null && table.getItemIds().size() > 0){
			List<NomineeDetailsDto> nomineeDetailsList = (List<NomineeDetailsDto>)table.getItemIds();
			
			int selectCnt = 0;
			if(nomineeDetailsList != null && !nomineeDetailsList.isEmpty()){
				for (NomineeDetailsDto nomineeDetailsDto : nomineeDetailsList) {
					if(nomineeDetailsDto.isSelectedNominee())
						selectCnt++;
				}
			}
			if(selectCnt > 0) {
				enableLegalHeir(false);
			}
			else{
				enableLegalHeir(true);
			}
			enableLegalHeir(false);
		}
		else{
			enableLegalHeir(true);
		}
	}*/
	
	@Override
	public void tableSelectHandler(NomineeDetailsDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "nomineetable-";
	}
	
	protected void tablesize(){
		/*List<NomineeDetailsDto> itemIds = table.getItemIds() != null ? (List<NomineeDetailsDto>) table.getItemIds() : null;
        if(itemIds != null && itemIds.isEmpty()) {
        	for (NomineeDetailsDto nomineeDetailsDto : itemIds) {
				table.addItem();
			}
        }*/
				
		table.setPageLength(4);		
		
	}
	
	public void setLegalHeirDetails(String nomineeName, String nomineeAddr){
		
		if(legalHeirFirstNameTxt != null) {
			legalHeirFirstNameTxt.setValue(nomineeName != null ? nomineeName : "");
			legalHeirAddressTxt.setValue(nomineeAddr != null ? nomineeAddr : "");
		}
	}
	
	public void removeLegalHeirValidation() {
		legalHeirFirstNameTxt.setRequired(false);
		legalHeirAddressTxt.setRequired(false);
	}
	
	public synchronized FormLayout getLegalHeirLayout(boolean enable){
		
		legalFormLayout.removeAllComponents();
		
		legalHeirFirstNameTxt = new TextField("Claimant / Legal Heir Name");
		legalHeirFirstNameTxt.setWidth("45%");
		legalHeirFirstNameTxt.setEnabled(enable);
		legalHeirFirstNameTxt.setRequired(enable);
//		legalHeirMiddleNameTxt = new TextField();
//		legalHeirLastNameTxt = new TextField();
		legalHeirAddressTxt = new TextArea("Claimant / Legal Heir Address");
		legalHeirAddressTxt.setWidth("45%");
		legalHeirAddressTxt.setEnabled(enable);
		legalHeirAddressTxt.setRequired(enable);
		legalHeirAddressTxt.setMaxLength(4000);		
		
		HorizontalLayout legaHeirHLayout = new HorizontalLayout();

		if(presenterString != null && SHAConstants.CASHLESS.equalsIgnoreCase(presenterString)){
			legalHeirFirstNameTxt.setRequired(false);
			legalHeirAddressTxt.setRequired(false);
		}
		
		legaHeirHLayout.setCaption("Claimant / Legal Heir Name");
		legaHeirHLayout.addComponents(legalHeirFirstNameTxt/*,legalHeirMiddleNameTxt,legalHeirLastNameTxt*/);
		legaHeirHLayout.setSpacing(true);
		
		
		legalFormLayout.addComponents(legalHeirFirstNameTxt, legalHeirAddressTxt);
		
//		VerticalLayout legaHeirLayout = new VerticalLayout(legalFormLayout);
//		return legaHeirLayout;
		
		return legalFormLayout;
	}
	
	public void enableLegalHeir(boolean enable) {
		
		if(legalFormLayout.getComponentCount() == 2) {
			legalFormLayout.getComponent(0).setEnabled(enable);
			legalFormLayout.getComponent(1).setEnabled(enable);
		}		
		
		if(legalHeirFirstNameTxt != null && legalHeirMiddleNameTxt != null
				&& legalHeirLastNameTxt != null && legalHeirAddressTxt != null) {
		
			legalHeirFirstNameTxt.setEnabled(enable);
			//Vaadin8-setImmediate() legalHeirFirstNameTxt.setImmediate(true);
//			legalHeirMiddleNameTxt.setEnabled(false);
//			legalHeirLastNameTxt.setEnabled(false);
			legalHeirAddressTxt.setEnabled(enable);
			//Vaadin8-setImmediate() legalHeirAddressTxt.setImmediate(true);
		
			legalHeirFirstNameTxt.setRequired(enable);
//			legalHeirMiddleNameTxt.setRequired(enable);
//			legalHeirLastNameTxt.setRequired(enable);
			legalHeirAddressTxt.setRequired(enable);
		}	
		
	}
	
	public Map<String, String> getLegalHeirDetails(){
		WeakHashMap<String, String> legalHeirMap = new WeakHashMap<String, String>();
		
		legalHeirMap.put("FNAME", legalHeirFirstNameTxt.getValue() != null ? legalHeirFirstNameTxt.getValue() : "");
//		legalHeirMap.put("MNAME", legalHeirMiddleNameTxt.getValue() != null ? legalHeirMiddleNameTxt.getValue() : "");
//		legalHeirMap.put("LNAME", legalHeirLastNameTxt.getValue() != null ? legalHeirLastNameTxt.getValue() : "");
		legalHeirMap.put("ADDR", legalHeirAddressTxt.getValue() != null ? legalHeirAddressTxt.getValue() : "");
		
		return legalHeirMap;
	}
	
	public List<NomineeDetailsDto> getTableList()
	{
		List<NomineeDetailsDto> tableList = table.getItemIds() != null ? (List<NomineeDetailsDto>) table.getItemIds() : new ArrayList<NomineeDetailsDto>();
		return tableList;
	}
	
	public void setNomineeBankDetails(ViewSearchCriteriaTableDTO dto, NomineeDetailsDto nomineeDto) {
	
		List<NomineeDetailsDto> tableList = table.getItemIds() != null ? (List<NomineeDetailsDto>) table.getItemIds() : new ArrayList<NomineeDetailsDto>();

		if(!tableList.isEmpty()) {
			for (NomineeDetailsDto nomineeDetailsDto : tableList) {
				if(nomineeDetailsDto.getNomineeName().equalsIgnoreCase(nomineeDto.getNomineeName())) {
					nomineeDetailsDto.setPreference(dto.getAccPreference());
					nomineeDetailsDto.setAccType(dto.getAccType());
					nomineeDetailsDto.setIfscCode(dto.getIfscCode());
					nomineeDetailsDto.setBankId(dto.getBankId());
					nomineeDetailsDto.setBankName(dto.getBankName());
					nomineeDetailsDto.setBankBranchName(dto.getBranchName());
					nomineeDetailsDto.setBankCity(dto.getCity());
					nomineeDetailsDto.setAccNumber(dto.getAccNumber());
					nomineeDetailsDto.setMicrCode(dto.getMicrCode());
					nomineeDetailsDto.setVirtualPayAdd(dto.getAddress());
					nomineeDetailsDto.setEffFrmDate(dto.getEffectiveFrmDate() != null ? dto.getEffectiveFrmDate() : "");
					nomineeDetailsDto.setEffToDate(dto.getEffectiveToDate() != null ? dto.getEffectiveToDate() : "");
					nomineeDetailsDto.setPanNumber(dto.getPanNumber());
					
				}
			}
		}
		
		generatePaymentRelatedFields();
		
	}
	
	public void setCitySearchCriteriaWindow(CitySearchCriteriaViewImpl citySearchCriteriaWindow) {
		this.citySearchCriteriaWindow = citySearchCriteriaWindow;
	}

	public void setIfscView(ViewSearchCriteriaViewImpl viewSearchCriteriaWindow) {
		this.viewSearchCriteriaWindow = viewSearchCriteriaWindow;
	}
	public void generatePaymentRelatedFields() {
		
		table.removeGeneratedColumn("preference");
		table.addGeneratedColumn("preference",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							HorizontalLayout accPreferenceLayout = new HorizontalLayout();
							TextField txtAccPreference = new TextField();
							txtAccPreference.setCaption(null);
							txtAccPreference.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

							/**
							 No web service available for Nominee so disabling this btn 14/12/2019  said by Mr. Satish Sir
							 **/
							/*if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)
									&& !nomineeDto.isPaymentMode()) {
							txtAccPreference.setEnabled(true);
							}
							else {
								txtAccPreference.setEnabled(false);
							}*/

							txtAccPreference.setEnabled(false);
							
							txtAccPreference.setValue(nomineeDto.getPreference() != null ? nomineeDto.getPreference() : "");
							Button accPreferenceBtnSrch = new Button();
							accPreferenceBtnSrch.setData(nomineeDto);
							accPreferenceBtnSrch.setStyleName(ValoTheme.BUTTON_LINK);
							
							/**
							 No web service available for Nominee so disabling this btn 14/12/2019  said by Mr. Satish Sir
							 **/
							accPreferenceBtnSrch.setEnabled(false);  
							
							accPreferenceBtnSrch.setIcon(new ThemeResource("images/search.png"));
							
							accPreferenceBtnSrch.addClickListener(new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {				
									final Window popup = new com.vaadin.ui.Window();
//										bankDetailsTableObj =  bankDetailsTableInstance.get();
										bean = new ReceiptOfDocumentsDTO();
										SelectValue patientStatus = new SelectValue(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB,"");
										DocumentDetailsDTO documentDto = new DocumentDetailsDTO(); 
										documentDto.setPatientStatus(patientStatus);
										bean.setDocumentDetails(documentDto);
										bean.setSourceRiskID(nomineeDto.getNomineeCode());
										bankDetailsTableObj.init(bean);
										bankDetailsTableObj.setNomineeDto(nomineeDto);
										bankDetailsTableObj.initPresenter(screenName);
										bankDetailsTableObj.setCaption("Bank Details");
										
										popup.setWidth("75%");
										popup.setHeight("70%");
										popup.setClosable(true);
										popup.center();
										popup.setResizable(true);
										popup.addCloseListener(new Window.CloseListener() {
											/**
											 * 
											 */
											private static final long serialVersionUID = 1L;

											@Override
											public void windowClose(CloseEvent e) {
												System.out.println("Close listener called");
											}
										});
										Button okBtn = new Button("Cancel");
										okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
										okBtn.addClickListener(new Button.ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												List<BankDetailsTableDTO> bankDetailsTableDTO = bankDetailsTableObj.getValues();
												bankDetailsTableDTO = new ArrayList<BankDetailsTableDTO>();
												//bean.setVerificationAccountDeatilsTableDTO(bankDetailsTableDTO);
												popup.close();
											}
										});
								
										VerticalLayout vlayout = new VerticalLayout(bankDetailsTableObj);
										HorizontalLayout hLayout = new HorizontalLayout(okBtn);
										hLayout.setSpacing(true);
										vlayout.addComponent(hLayout);
										vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
										popup.setContent(vlayout);
										popup.setModal(true);
										UI.getCurrent().addWindow(popup);
						        		
								 }
							});
							
							accPreferenceLayout.addComponents(txtAccPreference, accPreferenceBtnSrch);
							accPreferenceLayout.setSpacing(true);
					return accPreferenceLayout;
					
				}
		});

		table.removeGeneratedColumn("paymentMode");
		table.addGeneratedColumn("paymentMode",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							OptionGroup optPaymentMode = new OptionGroup();
							optPaymentMode.setData(nomineeDto);
							Collection<Boolean> values = new ArrayList<Boolean>(2);
							values.add(true);
							values.add(false);
							optPaymentMode.addItems(values);
							optPaymentMode.setItemCaption(true, "Cheque/DD");
							optPaymentMode.setItemCaption(false, "Bank Transfer");
							optPaymentMode.setStyleName("vertical");
							optPaymentMode.setValue(nomineeDto.isPaymentMode());
							optPaymentMode.addValueChangeListener(paymentModeListener());
//							optPaymentMode.setValue(false);
//							optPaymentMode.setValue(nomineeDto.isPaymentMode());
							return optPaymentMode;
				}
		});
		
		table.removeGeneratedColumn("payableAt");
		table.addGeneratedColumn("payableAt",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							TextField txtPayableAt = new TextField();
							txtPayableAt.setData(nomineeDto);
							txtPayableAt.setCaption(null);
							
							txtPayableAt.setValue(nomineeDto.getPayableAt() != null ? nomineeDto.getPayableAt() : "");
//							txtPayableAt.setEnabled(false);
							if(nomineeDto.isPaymentMode()) {
								txtPayableAt.setEnabled(true);
							}
							else {
								txtPayableAt.setEnabled(false);
							}
							
							txtPayableAt.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									NomineeDetailsDto nomineeDto = (NomineeDetailsDto) txtPayableAt.getData();
									if(event.getProperty().getValue() != null) {
										nomineeDto.setPayableAt(String.valueOf(event.getProperty().getValue()));
									}
								}
							});
							
							return txtPayableAt;
				}
		});
		table.removeGeneratedColumn("accNumber");
		table.addGeneratedColumn("accNumber",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							TextField txtAccNo = new TextField();
							txtAccNo.setCaption(null);
							txtAccNo.setValue(nomineeDto.getAccNumber() != null ? nomineeDto.getAccNumber() : "");
							
							if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)) {
								txtAccNo.setEnabled(false);
								txtAccNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							}
							else {
								txtAccNo.setEnabled(true);
								txtAccNo.addTextChangeListener(new FieldEvents.TextChangeListener() {
									
									@Override
									public void textChange(TextChangeEvent event) {
										
										String newValue = event.getText();
										
										if(newValue != null && !newValue.isEmpty()) {
											nomineeDto.setAccNumber(newValue);
										}										
										
									}
								});
							}	
							return txtAccNo;
				}
		});
				
		table.removeGeneratedColumn("ifscCode");
		table.addGeneratedColumn("ifscCode",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							HorizontalLayout ifsLayout = new HorizontalLayout();
							TextField txtifsc = new TextField();
							txtifsc.setCaption(null);
							txtifsc.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtifsc.setEnabled(false);
							txtifsc.setValue(nomineeDto.getIfscCode() != null ? nomineeDto.getIfscCode() : "");
							Button ifscBtnSrch = new Button();
							ifscBtnSrch.setData(nomineeDto);
							ifscBtnSrch.setStyleName(ValoTheme.BUTTON_LINK);
							ifscBtnSrch.setIcon(new ThemeResource("images/search.png"));
							/**
							 No search for Nominee IFSC so disabling this btn 14/12/2019  said by Mr. Satish - For BaNCS policy
							 **/

							if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)) {
								ifscBtnSrch.setEnabled(false);
							}
							else {
								
								if(nomineeDto.isPaymentMode()) {
									ifscBtnSrch.setEnabled(false);
								}
								else {
									ifscBtnSrch.setEnabled(true);
								}
								ifscBtnSrch.addClickListener(new ClickListener() {
									
									@Override
									public void buttonClick(ClickEvent event) {
										Window popup = new com.vaadin.ui.Window();
										viewSearchCriteriaWindow.setWindowObject(popup);
										viewSearchCriteriaWindow.setPresenterString(screenName);
										PreauthDTO preauthBean = new PreauthDTO();
										PreauthDataExtaractionDTO datExtractDto = new PreauthDataExtaractionDTO();
										datExtractDto.setPatientStatus(new SelectValue(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB,""));
										NewIntimationDto intimationDto = new NewIntimationDto();
										Insured insured = new Insured();
										MastersValue relationship = new MastersValue();
										relationship.setKey(ReferenceTable.RELATION_SHIP_SELF_KEY);
										insured.setRelationshipwithInsuredId(relationship);
										intimationDto.setInsuredPatient(insured);
										preauthBean.setNewIntimationDTO(intimationDto);
										preauthBean.setPreauthDataExtractionDetails(datExtractDto);
										viewSearchCriteriaWindow.setPreauthDto(preauthBean);
										viewSearchCriteriaWindow.setNomineeDto((NomineeDetailsDto)ifscBtnSrch.getData());
										bankDetailsTableObj.setNomineeDto((NomineeDetailsDto)ifscBtnSrch.getData());
										viewSearchCriteriaWindow.initView();
										popup.setWidth("75%");
										popup.setHeight("90%");
										popup.setContent(viewSearchCriteriaWindow);
										popup.setClosable(true);
										popup.center();
										popup.setResizable(true);
										
										popup.addCloseListener(new Window.CloseListener() {
											
											private static final long serialVersionUID = 1L;
	
											@Override
											public void windowClose(CloseEvent e) {
												System.out.println("Close listener called");
											}
										});
	
										popup.setModal(true);
										
										UI.getCurrent().addWindow(popup);
										ifscBtnSrch.setEnabled(true);
									}
								});
							}
							
							ifsLayout.addComponents(txtifsc, ifscBtnSrch);
							ifsLayout.setSpacing(true);
					return ifsLayout;
					
				}
		});
		
		table.removeGeneratedColumn("bankName");
		table.addGeneratedColumn("bankName",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankName = new TextField();
							txtBankName.setCaption(null);
							txtBankName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankName.setEnabled(false);
							txtBankName.setValue(nomineeDto.getBankName() != null ? nomineeDto.getBankName() : "");
							
							return txtBankName;
				}
		});
		
		table.removeGeneratedColumn("bankBranchName");
		table.addGeneratedColumn("bankBranchName",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankBrnchName = new TextField();
							txtBankBrnchName.setCaption(null);
							txtBankBrnchName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankBrnchName.setEnabled(false);
							txtBankBrnchName.setValue(nomineeDto.getBankBranchName() != null ? nomineeDto.getBankBranchName() : "");
							return txtBankBrnchName;
				}
		});
		
		table.removeGeneratedColumn("bankCity");
		table.addGeneratedColumn("bankCity",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankCity = new TextField();
							txtBankCity.setCaption(null);
							txtBankCity.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankCity.setEnabled(false);
							txtBankCity.setValue(nomineeDto.getBankCity() != null ? nomineeDto.getBankCity() : "");
							
							return txtBankCity;
				}
		});
	}

	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}
	
	public ValueChangeListener paymentModeListener() {
		ValueChangeListener valChangeListener = new Property.ValueChangeListener() {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			//TODO
			Boolean value = event.getProperty().getValue() != null ? (Boolean) event.getProperty().getValue() : false;
			OptionGroup mode = (OptionGroup)event.getProperty();
			
			NomineeDetailsDto nomineeDto = (NomineeDetailsDto)mode.getData();
			
			nomineeDto.setPaymentMode(value);
			((NomineeDetailsDto)mode.getData()).setPaymentMode(value);

			if(value)
			{
				nomineeDto.setPaymentModeId(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
				enableChequePaymentFields();
			}
			else{
				nomineeDto.setPaymentModeId(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				enableBankTransferPaymentFields();
			}	
					
		}
	};
		return valChangeListener;
	}
	
	public void enableChequePaymentFields() {

		table.removeGeneratedColumn("payableAt");
		table.addGeneratedColumn("payableAt",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							HorizontalLayout payAtLayout = new HorizontalLayout();
							
							TextField txtPayableAt = new TextField();
							txtPayableAt.setCaption(null);
							txtPayableAt.setData(nomineeDto);
							if(nomineeDto.isPaymentMode()) {
								txtPayableAt.setValue(nomineeDto.getPayableAt() != null ? nomineeDto.getPayableAt() : "");
							}	
							/*txtPayableAt.setEnabled(false);
							
							Button payAtBtnSrch = new Button();
							payAtBtnSrch.setData(nomineeDto);
							payAtBtnSrch.setStyleName(ValoTheme.BUTTON_LINK);
							payAtBtnSrch.setIcon(new ThemeResource("images/search.png"));
							payAtBtnSrch.setEnabled(true);
							payAtBtnSrch.addClickListener(new ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
									Window popup = new com.vaadin.ui.Window();
									citySearchCriteriaWindow.setPresenterString(SHAConstants.CREATE_ROD);
									citySearchCriteriaWindow.initView(popup);
									popup.setWidth("75%");
									popup.setHeight("90%");
									popup.setContent(citySearchCriteriaWindow);
									popup.setClosable(true);
									popup.center();
									popup.setResizable(true);
									
									popup.addCloseListener(new Window.CloseListener() {
										*//**
										 * 
										 *//*
										private static final long serialVersionUID = 1L;

										@Override
										public void windowClose(CloseEvent e) {
											System.out.println("Close listener called");
										}
									});

									popup.setModal(true);
									
									UI.getCurrent().addWindow(popup);
								}
							});*/
							
							if(nomineeDto.isPaymentMode()) {
								txtPayableAt.setEnabled(true);
							}
							else {
								txtPayableAt.setEnabled(false);
							}
							txtPayableAt.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									NomineeDetailsDto nomineeDto = (NomineeDetailsDto) txtPayableAt.getData();
									if(event.getProperty().getValue() != null) {
										nomineeDto.setPayableAt(String.valueOf(event.getProperty().getValue()));
									}
								}
							});
							
							payAtLayout.addComponents(txtPayableAt/*, payAtBtnSrch*/);
							
							return payAtLayout;
				}
		});
		
		table.removeGeneratedColumn("accNumber");
		table.addGeneratedColumn("accNumber",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							TextField txtAccNo = new TextField();
							txtAccNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtAccNo.setCaption(null);
							if(nomineeDto.isPaymentMode()) {
								txtAccNo.setValue("");
								txtAccNo.setEnabled(false);
							}
							else {
								txtAccNo.setValue(nomineeDto.getAccNumber());
								txtAccNo.setEnabled(false);
							}
							return txtAccNo;
				}
		});
		
		table.removeGeneratedColumn("ifscCode");
		table.addGeneratedColumn("ifscCode",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							HorizontalLayout ifsLayout = new HorizontalLayout();
							TextField txtifsc = new TextField();
							txtifsc.setCaption(null);
							txtifsc.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtifsc.setEnabled(false);
							if(nomineeDto.isPaymentMode()) {
								txtifsc.setValue("");
							}
							else {
								txtifsc.setValue(nomineeDto.getIfscCode());
							}
							Button ifscBtnSrch = new Button();
							ifscBtnSrch.setData(nomineeDto);
							ifscBtnSrch.setStyleName(ValoTheme.BUTTON_LINK);
							ifscBtnSrch.setIcon(new ThemeResource("images/search.png"));
							
							if(nomineeDto.isPaymentMode()) {
								ifscBtnSrch.setEnabled(false);
							}
							else {
								ifscBtnSrch.setEnabled(true);
							}
							
							/**
							 No search for Nominee IFSC for bancs policy so disabling this btn 14/12/2019  said by Mr. Satish
							 **/
							if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)) {
								ifscBtnSrch.setEnabled(false);
							}
							
							ifsLayout.addComponents(txtifsc, ifscBtnSrch);
							ifsLayout.setSpacing(true);
					return ifsLayout;
					
				}
		});
		
		table.removeGeneratedColumn("bankName");
		table.addGeneratedColumn("bankName",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankName = new TextField();
							txtBankName.setCaption(null);
							txtBankName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankName.setEnabled(false);
							
							if(nomineeDto.isPaymentMode()) {
								txtBankName.setValue("");
							}
							else {
								txtBankName.setValue(nomineeDto.getBankName());
							}
							
							return txtBankName;
				}
		});
		
		table.removeGeneratedColumn("bankCity");
		table.addGeneratedColumn("bankCity",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankCity = new TextField();
							txtBankCity.setCaption(null);
							txtBankCity.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankCity.setEnabled(false);
							
							if(nomineeDto.isPaymentMode()) {
								txtBankCity.setValue("");
							}
							else {
								txtBankCity.setValue(nomineeDto.getBankCity());
							}
							
							return txtBankCity;
				}
		});
		
		table.removeGeneratedColumn("bankBranchName");
		table.addGeneratedColumn("bankBranchName",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankBrnchName = new TextField();
							txtBankBrnchName.setCaption(null);
							txtBankBrnchName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankBrnchName.setEnabled(false);
							
							if(nomineeDto.isPaymentMode()) {
								txtBankBrnchName.setValue("");
							}
							else {
								txtBankBrnchName.setValue(nomineeDto.getBankBranchName());
							}
							return txtBankBrnchName;
				}
		});
	}

	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void enableBankTransferPaymentFields() {
		
		table.removeGeneratedColumn("payableAt");
		table.addGeneratedColumn("payableAt",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							TextField txtPayableAt = new TextField();
							txtPayableAt.setCaption(null);
							
							if(nomineeDto.isPaymentMode()) {
								txtPayableAt.setValue(nomineeDto.getPayableAt() != null ? nomineeDto.getPayableAt() : "");
							}
							else {
								txtPayableAt.setValue("");
							}	
//							txtPayableAt.setEnabled(false);
							
							if(nomineeDto.isPaymentMode()) {
								txtPayableAt.setEnabled(true);
							}
							else {
								txtPayableAt.setEnabled(false);
							}
							
							return txtPayableAt;
				}
		});
		
		table.removeGeneratedColumn("accNumber");
		table.addGeneratedColumn("accNumber",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							TextField txtAccNo = new TextField();
							txtAccNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtAccNo.setCaption(null);
							
							if(!nomineeDto.isPaymentMode()) {
								txtAccNo.setValue(nomineeDto.getAccNumber() != null ? nomineeDto.getAccNumber() : "");
							}	
							if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)){
								txtAccNo.setEnabled(false);
							}else{
								txtAccNo.setEnabled(true);
							}
							return txtAccNo;
				}
		});
		
		table.removeGeneratedColumn("ifscCode");
		table.addGeneratedColumn("ifscCode",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							HorizontalLayout ifsLayout = new HorizontalLayout();
							TextField txtifsc = new TextField();
							txtifsc.setCaption(null);
							txtifsc.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtifsc.setEnabled(false);
							if(!nomineeDto.isPaymentMode()) {
								txtifsc.setValue(nomineeDto.getIfscCode() != null ? nomineeDto.getIfscCode() : "");
							}	
							Button ifscBtnSrch = new Button();
							ifscBtnSrch.setData(nomineeDto);
							ifscBtnSrch.setStyleName(ValoTheme.BUTTON_LINK);
							ifscBtnSrch.setIcon(new ThemeResource("images/search.png"));

							if(policySource != null && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)) {
								ifscBtnSrch.setEnabled(false);
							}
							else {
								if(!nomineeDto.isPaymentMode()) {
									ifscBtnSrch.setEnabled(true);
								}
								else {
									ifscBtnSrch.setEnabled(false);
								}	
								ifscBtnSrch.addClickListener(new ClickListener() {
									
									@Override
									public void buttonClick(ClickEvent event) {
										Window popup = new com.vaadin.ui.Window();
										viewSearchCriteriaWindow.setWindowObject(popup);
										viewSearchCriteriaWindow.setPresenterString(screenName);
										viewSearchCriteriaWindow.initView();
										//IMSSUPPOR-31243
										PreauthDTO preauthBean = new PreauthDTO();
										PreauthDataExtaractionDTO datExtractDto = new PreauthDataExtaractionDTO();
										datExtractDto.setPatientStatus(new SelectValue(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB,""));
										NewIntimationDto intimationDto = new NewIntimationDto();
										Insured insured = new Insured();
										MastersValue relationship = new MastersValue();
										relationship.setKey(ReferenceTable.RELATION_SHIP_SELF_KEY);
										insured.setRelationshipwithInsuredId(relationship);
										intimationDto.setInsuredPatient(insured);
										preauthBean.setNewIntimationDTO(intimationDto);
										preauthBean.setPreauthDataExtractionDetails(datExtractDto);
										viewSearchCriteriaWindow.setPreauthDto(preauthBean);
	//									viewSearchCriteriaWindow.setPreauthDto(preauthBean);
										viewSearchCriteriaWindow.setNomineeDto((NomineeDetailsDto)ifscBtnSrch.getData());
										popup.setWidth("75%");
										popup.setHeight("90%");
										popup.setContent(viewSearchCriteriaWindow);
										popup.setClosable(true);
										popup.center();
										popup.setResizable(true);
										
										popup.addCloseListener(new Window.CloseListener() {
											/**
											 * 
											 */
											private static final long serialVersionUID = 1L;
	
											@Override
											public void windowClose(CloseEvent e) {
												System.out.println("Close listener called");
											}
										});
	
										popup.setModal(true);
										
										UI.getCurrent().addWindow(popup);
										ifscBtnSrch.setEnabled(true);
									}
								});
							}	
							
							ifsLayout.addComponents(txtifsc, ifscBtnSrch);
							ifsLayout.setSpacing(true);
					return ifsLayout;
					
				}
		});
		
		table.removeGeneratedColumn("bankName");
		table.addGeneratedColumn("bankName",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
								
								NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
								
								TextField txtBankName = new TextField();
								txtBankName.setCaption(null);
								txtBankName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
								txtBankName.setEnabled(false);
								
								if(!nomineeDto.isPaymentMode()) {
									txtBankName.setValue(nomineeDto.getBankName() != null ? nomineeDto.getBankName() : "");
								}
								else {
									txtBankName.setValue("");
								}
								
								return txtBankName;
					}
			});
		
		table.removeGeneratedColumn("bankCity");
		table.addGeneratedColumn("bankCity",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankCity = new TextField();
							txtBankCity.setCaption(null);
							txtBankCity.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankCity.setEnabled(false);
							
							if(!nomineeDto.isPaymentMode()) {
								txtBankCity.setValue(nomineeDto.getBankCity() != null ? nomineeDto.getBankCity() : "");
							}
							else {
								txtBankCity.setValue("");
							}
							
							return txtBankCity;
				}
		});
		
		table.removeGeneratedColumn("bankBranchName");
		table.addGeneratedColumn("bankBranchName",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
							
							NomineeDetailsDto nomineeDto = (NomineeDetailsDto)itemId;
							
							TextField txtBankBrnchName = new TextField();
							txtBankBrnchName.setCaption(null);
							txtBankBrnchName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							txtBankBrnchName.setEnabled(false);
							
							if(!nomineeDto.isPaymentMode()) {
								txtBankBrnchName.setValue(nomineeDto.getBankBranchName() != null ? nomineeDto.getBankBranchName() : "");
							}
							else {
								txtBankBrnchName.setValue("");
							}
							
							return txtBankBrnchName;
				}
		});
	}

	public BankDetailsTable getBankDetailsTableObj() {
		return bankDetailsTableObj;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	public String validatePayableAtForSelectedNominee() {
		StringBuffer errMsg = new StringBuffer("");
		
		List<NomineeDetailsDto> tableList = table.getItemIds() != null ? (List<NomineeDetailsDto>) table.getItemIds() : new ArrayList<NomineeDetailsDto>();

		if(!tableList.isEmpty()) {
			
			int count = 1;
			for (NomineeDetailsDto nomineeDetailsDto : tableList) {
				if(nomineeDetailsDto.isSelectedNominee()  
						&& nomineeDetailsDto.isPaymentMode()) {
					
					if(nomineeDetailsDto.getPayableAt() == null || nomineeDetailsDto.getPayableAt().isEmpty()) {
						
						errMsg.append("Please Enter Payable At for Selected Nominee No." + nomineeDetailsDto.getSno() + "<br>");
						count++;
					}
				}
			}						
		}			
		
		return errMsg.toString();
	}
	
	public String validateIFSCForSelectedNominee() {
		StringBuffer errMsg = new StringBuffer("");
//		if(policySource != null && !SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(policySource)) {
		List<NomineeDetailsDto> tableList = table.getItemIds() != null ? (List<NomineeDetailsDto>) table.getItemIds() : new ArrayList<NomineeDetailsDto>();

		if(!tableList.isEmpty()) {
			
			int count = 1;
			for (NomineeDetailsDto nomineeDetailsDto : tableList) {
				if(nomineeDetailsDto.isSelectedNominee()  
						&& !nomineeDetailsDto.isPaymentMode()
						) {
					
					if(nomineeDetailsDto.getAccNumber() == null || nomineeDetailsDto.getAccNumber().isEmpty()) {
						
						errMsg.append("Please Enter Account Number for Selected Nominee No." + nomineeDetailsDto.getSno() + "<br>");
					}
					
					if(nomineeDetailsDto.getIfscCode() == null || nomineeDetailsDto.getIfscCode().isEmpty()) {
						
						errMsg.append("Please Select IFSC Code for Selected Nominee No." + nomineeDetailsDto.getSno() + "<br>");
					}
					
					count++;
				}
			}						
		}
//		}
		
		return errMsg.toString();
	}
}