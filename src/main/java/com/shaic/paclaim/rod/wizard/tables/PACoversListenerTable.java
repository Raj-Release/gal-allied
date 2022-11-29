/**
 * 
 */
package com.shaic.paclaim.rod.wizard.tables;




/**
 * @author ntv.vijayar
 *
 */


/**
 * 
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class PACoversListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<AddOnCoversTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AddOnCoversTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<AddOnCoversTableDTO> container = new BeanItemContainer<AddOnCoversTableDTO>(AddOnCoversTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Button btnDelete;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	private List<AddOnCoversTableDTO> addOnCoversList;
	
	private List<AddOnCoversTableDTO> optionalCoversList;
	
	private List<AddOnCoversTableDTO> deletedList;

	private PreauthDTO bean;
	
	//private Map<String, Object> referenceData ;
	
//	private ReceiptOfDocumentsDTO bean;
	

	public void init(String presenterString,Map<String, Object> referenceData) {
		//container.removeAllItems();
		this.presenterString  = presenterString;
		this.referenceData = referenceData;
		this.bean = bean;
		addOnCoversList = new ArrayList<AddOnCoversTableDTO>();
		optionalCoversList = new ArrayList<AddOnCoversTableDTO>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		deletedList = new ArrayList<AddOnCoversTableDTO>();
		
		this.errorMessages = new ArrayList<String>();
		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		/*btnDelete = new Button();
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.setStyleName(ValoTheme.BUTTON_BORDERLESS);*/
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
		//btnLayout.setComponentAlignment(btnDelete, Alignment.MIDDLE_RIGHT);

		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(btnLayout);
		initTable();
		table.removeAllItems();
	//	table.setWidth("100%");
		table.setHeight("100%");
		table.setPageLength(table.getItemIds().size());
		if((SHAConstants.ADDITIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
			table.setCaption("Part II Add On Covers");
		else 
			table.setCaption("Part III - Optional Covers");
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	/**
	 * Date of admission changed during bug fixing activity.
	 * Hence to not disturb existing flow, we have added 
	 * the parameterized constructor with extra attribute for
	 * date of admission.
	 * */
	
	public void init(Boolean isAddBtnRequired, Date admissionDate, Date dischargeDate) {
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
	

		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		if(isAddBtnRequired)
		{
			layout.addComponent(btnLayout);
		}
		
		initTable();
	//	table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<AddOnCoversTableDTO> list) {
		table.removeAllItems();
		for (final AddOnCoversTableDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		//container.removeAllItems();
		table.addStyleName("generateColumnTable");
	//	table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		
		table.setVisibleColumns(new Object[] { "slNo","addOnCovers","eligibleForPolicy","claimedAmount","remarks"});

		table.setColumnHeader("slNo","Sl No");
		if((SHAConstants.ADDITIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
			table.setColumnHeader("addOnCovers", "Add on Covers");
		else
			table.setColumnHeader("addOnCovers", "Optional Covers");
		table.setColumnHeader("eligibleForPolicy", "Eligible for Policy");
		table.setColumnHeader("claimedAmount", "Amount Claimed");
		table.setColumnHeader("remarks", "Remarks");
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						AddOnCoversTableDTO coversDTO = (AddOnCoversTableDTO) currentItemId;
						deletedList.add(coversDTO);
					/*	if(null != deletedList && !deletedList.isEmpty() && deletedList.size() <=1 )
						{
							showErrorMsg("One row is mandatory for benefits table.");
						}
						else
						{
							table.removeItem(currentItemId);
						}*/
						table.removeItem(currentItemId);
					}
				});

				return deleteButton;
			}
		});

		
		/*table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		table.setColumnHeader("status", "Status");*/
		table.setEditable(true);


//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//manageListeners();
	//	table.setEditable(false);
		//table.setFooterVisible(true);

	}
	
	public void removeAllItems()
	{
		table.removeAllItems();
		//table.removeItem(\);
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		addOnCoversList = (List<AddOnCoversTableDTO>) referenceData.get("addOnCoverProc");
		optionalCoversList = (List<AddOnCoversTableDTO>) referenceData.get("optionalCoverProc");
		
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
		    public void buttonClick(ClickEvent event) {
				AddOnCoversTableDTO paCoverDTO = new AddOnCoversTableDTO();	
				BeanItem<AddOnCoversTableDTO> addItem = container.addItem(paCoverDTO);
				table.setPageLength(2);
				
			}
		});
		
	/*	btnDelete.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<AddOnCoversTableDTO> paCoversDTO = getValues();
				if(null != paCoversDTO && !paCoversDTO.isEmpty())
				{
					int iSize = paCoversDTO.size();
					table.removeItem(paCoversDTO.get(iSize - 1));
				}
			}
		});*/
	}
	
	 @SuppressWarnings("unused")
		private void addCoversListener(
				final ComboBox categoryCombo) {
			if (categoryCombo != null) {
				categoryCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						Boolean isError = false;
						ComboBox component = (ComboBox) event.getComponent();
						AddOnCoversTableDTO coversDTO = (AddOnCoversTableDTO) component.getData();
						if(null != component)
						{
						if(null != coversDTO)
						{
							/*SelectValue value = (SelectValue) component.getValue();
							if(null != value)
							{
								List<AddOnCoversTableDTO> paCoversList = getValues();
								if(null != paCoversList && !paCoversList.isEmpty())
								{
									for (AddOnCoversTableDTO coverDTO : paCoversList) {
										if(coverDTO.getCovers().getId().equals(value.getId()))
										{
											showErrorMsg("Same cover Id cannot be selected again. Please choose different cover.");
											//categoryCombo.setValue(null);
											isError = true;
											break;
										}
									}
								}
							}*/
							if(!isError)
							{
							 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
							 if(null != hashMap)
							 {
								 SelectValue coversCombo = (SelectValue)component.getValue();
								 TextField eligibleForPolicyFld = (TextField) hashMap.get("eligibleForPolicy");
								 TextField amountClaimedFld = (TextField) hashMap.get("claimedAmount");
									if(null != coversCombo && null != coversCombo.getId())
									{
										if(SHAConstants.ADDITIONAL_COVER_TABLE.equalsIgnoreCase(presenterString))
										{
										if(null != addOnCoversList && !addOnCoversList.isEmpty())
										{
											fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_VALIDATE_COVERS, (PreauthDTO)referenceData.get("preauthDTO"), coversCombo.getId(),eligibleForPolicyFld , amountClaimedFld,presenterString,categoryCombo);
											//populateCoversTableData(addOnCoversList, eligibleForPolicyFld , amountClaimedFld,coversCombo.getId());
											/*for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversList) {
												if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCoverId() && coversCombo.getId().equals(addOnCoversTableDTO.getCoverId()))
												{
													if(null != eligibleForPolicyFld)
													{
														eligibleForPolicyFld.setValue(addOnCoversTableDTO.getEligibleForPolicy());
													}
													if(null != amountClaimedFld)
													{
														amountClaimedFld.setValue(null != addOnCoversTableDTO.getClaimedAmount() ? String.valueOf(addOnCoversTableDTO.getClaimedAmount()) : null);
													}
												}
											}*/
										}
										}
										else if(SHAConstants.OPTIONAL_COVER_TABLE.equalsIgnoreCase(presenterString)){
											if(null != optionalCoversList && !optionalCoversList.isEmpty())
											{
												if(coversCombo != null && coversCombo.getValue() != null &&
														!coversCombo.getValue().equalsIgnoreCase(SHAConstants.MEDICAL_EXTENSION_COVER_DESC) ){
												fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_VALIDATE_OPTIONAL_COVERS, (PreauthDTO)referenceData.get("preauthDTO"), coversCombo.getId(),eligibleForPolicyFld , amountClaimedFld,presenterString,categoryCombo);
												}
												//populateCoversTableData(optionalCoversList, eligibleForPolicyFld , amountClaimedFld,coversCombo.getId());
												/*for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversList) {
													if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCoverId() && coversCombo.getId().equals(addOnCoversTableDTO.getCoverId()))
													{
														if(null != eligibleForPolicyFld)
														{
															eligibleForPolicyFld.setValue(addOnCoversTableDTO.getEligibleForPolicy());
														}
														if(null != amountClaimedFld)
														{
															amountClaimedFld.setValue(null != addOnCoversTableDTO.getClaimedAmount() ? String.valueOf(addOnCoversTableDTO.getClaimedAmount()) : null);
														}
													}
												}*/
											}
										}
									}
							 }
							}
						}
						
						
					}
					}

				});
			}

		}
	
	 private void showErrorMsg(String message)
		{
			Label label = new Label(message, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		/* HorizontalLayout layout = new HorizontalLayout(
					new Label(""));
			layout.setMargin(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
		}
	 
	
		
	 public void populateCoversTableData(TextField eligibleForPolicyFld , TextField amountClaimedFld,Long coverId,List<AddOnCoversTableDTO> coversDTOList)
	 //public void populateCoversTableData(List<AddOnCoversTableDTO> coversTableList , TextField eligibleForPolicyFld , TextField amountClaimedFld,Long coverId)
	 {
		 if(null != coversDTOList && !coversDTOList.isEmpty())
			{
				for (AddOnCoversTableDTO addOnCoversTableDTO : coversDTOList) {
					if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCoverId() && coverId.equals(addOnCoversTableDTO.getCoverId()))
					{
						if(null != eligibleForPolicyFld)
						{
							//if(SHAConstants.YES_FLAG.equalsIgnoreCase(addOnCoversTableDTO.getEligibleForPolicy()))
							eligibleForPolicyFld.setValue(addOnCoversTableDTO.getEligibleForPolicy());
						}
						/**
						 * Added for issue no 5382 in PA.
						 * */
						if(null != amountClaimedFld)
						{
						//	amountClaimedFld.setValue(null != addOnCoversTableDTO.getClaimedAmount() ? String.valueOf(addOnCoversTableDTO.getClaimedAmount()) : null);
						}
					}
				}
			}
	 }
	 
	public class ImmediateFieldFactory extends DefaultFieldFactory {
/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*		private static final long serialVersionUID = -2192723245525925990L;
*/
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			AddOnCoversTableDTO entryDTO = (AddOnCoversTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			}
				tableRow = tableItem.get(entryDTO);

			if("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("slNo", field);	
				if(entryDTO.getIsReconsideration())
				{
					field.setEnabled(false);
				}
			
				return field;
			} 
			else if ("addOnCovers".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("500px");
				//box.setWidth("150px");
				tableRow.put("addOnCovers", box);
				box.setData(entryDTO);
				addCoversValues(box, entryDTO);
				addCoversListener(box);
				if(null != entryDTO.getCovers())
				{
					box.setValue(entryDTO.getCovers());
				}
				
				if(entryDTO.getIsReconsideration())
				{
					box.setEnabled(false);
				}
				return box;
			}
			else if("eligibleForPolicy".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("eligibleForPolicy", field);	
				generateSlNo(field);
				if(entryDTO.getIsReconsideration())
				{
					field.setEnabled(false);
				}
				return field;
			}
			else if("claimedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("claimedAmount", field);		
			
				return field;
			}
			else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				//CSValidator validator = new CSValidator();
				//validator.extend(field);
				//validator.setRegExp("^[0-9]*$");
				//validator.setPreventInvalidTyping(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("remarks", field);	
				if(entryDTO.getIsReconsideration())
				{
					field.setEnabled(false);
				}
				return field;
			}
			else
			{
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	
	
	

	


	
	
	
	public void addCoversValues(GComboBox comboBox,AddOnCoversTableDTO entryDTO) {
		BeanItemContainer<SelectValue> fileTypeContainer = null;
		if((SHAConstants.ADDITIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
		 fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("addOnCovers");
		else if((SHAConstants.OPTIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
			fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("optionalCovers");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
		

	}
	
	 public void addBeanToList(AddOnCoversTableDTO coversDTO) {
		 container.addItem(coversDTO);
	    }
	
	 
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			List<AddOnCoversTableDTO> itemIds = (List<AddOnCoversTableDTO>) table.getItemIds();
			Map<Long, String> duplicateItemMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();
 			if(null != itemIds && !itemIds.isEmpty())
			{
 				for (AddOnCoversTableDTO bean : itemIds) {
 					if(null != bean.getAddOnCovers())
 					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty())
						{
							if(duplicateItemMap.containsKey(bean.getAddOnCovers().getId()))
							{
								hasError = true;
								if((SHAConstants.ADDITIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
								{
									errorMessages.add("Same covers value cannot be selected twice in Add on Covers Table. Please change the covers in row " + bean.getSlNo() +" to proceed further");
								}
								else
								{
									errorMessages.add("Same covers value cannot be selected twice in Optional Covers Table. Please change the covers in row " + bean.getSlNo() +" to proceed further");
								}
								break;
							}
							
						}
							duplicateItemMap.put(bean.getAddOnCovers().getId(), bean.getAddOnCovers().getValue());
 					}
 					else
 					{
 						hasError = true;
 						if((SHAConstants.ADDITIONAL_COVER_TABLE).equalsIgnoreCase(presenterString))
						{
							errorMessages.add("Covers value cannot be empty in Add on Covers Table. Please select the covers value in row " + bean.getSlNo() +" to proceed further");
						}
						else
						{
							errorMessages.add("Covers value cannot be empty in Optional Covers Table. Please select the covers value in row " + bean.getSlNo() +" to proceed further");
						}
 						break;
 					}
			}
 		}
			return !hasError;

		}
	
	 public List<AddOnCoversTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
	    	List<AddOnCoversTableDTO> itemIds = new ArrayList<AddOnCoversTableDTO>();
	    	if(this.table != null) {
	    		itemIds = (List<AddOnCoversTableDTO>) this.table.getItemIds() ;
	    	} 
			
	    	return itemIds;
	    }
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
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
	  
	  
	  private void generateSlNo(TextField txtField)
		{
			
			Collection<AddOnCoversTableDTO> itemIds = (Collection<AddOnCoversTableDTO>) table.getItemIds();
			
			int i = 0;
			 for (AddOnCoversTableDTO coversDTO : itemIds) {
				 i++;
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
				 if(null != hashMap && !hashMap.isEmpty())
				 {
					 TextField itemNoFld = (TextField)hashMap.get("slNo");
					 if(null != itemNoFld)
					 {
						 itemNoFld.setValue(String.valueOf(i)); 
						 itemNoFld.setEnabled(false);
					 }
				 }
			 }
			
		}

	  
	  public void resetCoversDropDown(ComboBox categoryCmb,TextField amtFld, TextField eligbilityFld)
	  {
		  if(null != categoryCmb)
		  {
			  categoryCmb.setValue(null);
		  }
		  if(null != amtFld)
		  {
			  amtFld.setValue(null);
		  }
		  if(null != eligbilityFld)
		  {
			  eligbilityFld.setValue(null);
		  }
		  
	  }
	  
		public List<AddOnCoversTableDTO> getDeletedBillList()
		{
			return deletedList;
		}
		
}

