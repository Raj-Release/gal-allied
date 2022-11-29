package com.shaic.feedback.managerfeedback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

public class ManagerFeedBackPolicyTable extends GEditableTable<ManagerFeedBackPolicyTableDto> {
	
	private List<ManagerFeedBackPolicyTableDto> policyRemarksList;
	
	@EJB
	private MasterService masterService;

	public ManagerFeedBackPolicyTable() {
		super(ManagerFeedBackPolicyTableDto.class);
		// TODO Auto-generated constructor stub
		setUp();
		policyRemarksList = new ArrayList<ManagerFeedBackPolicyTableDto>();
	}

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();		

		fieldMap.put("policyNumber", new TableFieldDTO("policyNumber", TextField.class, String.class,true,true,350,50));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextArea.class, String.class,true,true,4000,100));
		//fieldMap.put("feedBackType", new TableFieldDTO("feedBackType", ComboBox.class, String.class,true,true));
	
	return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		((ManagerFeedBackPolicyTableDto)itemId).setDeltedFlag("Y");
		((ManagerFeedBackPolicyTableDto)itemId).setProcessType("D");
		policyRemarksList.add((ManagerFeedBackPolicyTableDto)itemId);
		this.table.getContainerDataSource().removeItem(itemId);
//		fireViewEvent(DecideOnQueryPresenter.DELET_DRAFT_LETTER_REMARKS, (DraftQueryLetterDetailTableDto)itemId);	
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		this.vLayout.setMargin(false);
		table.setContainerDataSource(new BeanItemContainer<ManagerFeedBackPolicyTableDto>(ManagerFeedBackPolicyTableDto.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "policyNumber","fbCategory", "remarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnCollapsingAllowed(false);
		table.setColumnExpandRatio("policyNumber",80.0f);
		table.setColumnExpandRatio("remarks",80.0f);
		table.setWidth("95%");
		table.setColumnWidth("feedBackType", 160);
		this.btnLayout.setWidth("80%");
		
		if(!table.getVisibleItemIds().contains("serialNumber")){
			table.removeGeneratedColumn("serialNumber");
			table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	 
			    	  BeanItemContainer<ManagerFeedBackPolicyTableDto> container = (BeanItemContainer<ManagerFeedBackPolicyTableDto>)source.getContainerDataSource(); 
			    	  return container.getItemIds().indexOf((ManagerFeedBackPolicyTableDto)itemId)+1;
			    			  
			      };
			});
		}
		
		if(!table.getVisibleItemIds().contains("feedBackType")){
			table.removeGeneratedColumn("feedBackType");
			table.addGeneratedColumn("feedBackType", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	 ManagerFeedBackPolicyTableDto dto = (ManagerFeedBackPolicyTableDto)itemId;
			    	 GridLayout gridLayout = new GridLayout(2,1);
			    	 GComboBox comboBox = new GComboBox();
			    	 comboBox.setNullSelectionAllowed(false);
			    	 BeanItemContainer<SelectValue> selectValueContainer = masterService.getSelectValueContainer(ReferenceTable.FEEDBACKRATING);
			    	 comboBox.setContainerDataSource(selectValueContainer);
			    	 selectValueContainer.sort(new Object[] {"id"}, new boolean[] {true});
			    	 Collection<SelectValue> feedBackStatusIds = (Collection<SelectValue>) comboBox.getContainerDataSource().getItemIds();
				 	    if(feedBackStatusIds != null && !feedBackStatusIds.isEmpty()) {
				    	 selectValueContainer.removeItem(feedBackStatusIds.toArray()[5]);
				 	    }
			    	 comboBox.setContainerDataSource(selectValueContainer);
			    	 comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			    	 comboBox.setItemCaptionPropertyId("value");
			    	 comboBox.setData(gridLayout);
			    	
			    	 if(dto.getFeedBackRating() != null){
			    		 comboBox.setValue(dto.getFeedBackRating());
			    	 }
			    	 
			    	 addListenerForFeedBackRating(comboBox, dto);
			    	 gridLayout.addComponent(comboBox,0,0);
			    	 if(dto.getFeedBackRating() != null){
			    		 setImagesBasedOnRating(dto.getFeedBackRating(), gridLayout,dto);
			    	 }
			    	 //field.setHeight("40px");
			    	 //field.setComponentAlignment(comboBox, Alignment.MIDDLE_LEFT);
//			    	 gridLayout.setStyleName("gridAlignment");
//					 gridLayout.addStyleName("gridAlignment");
					 //gridLayout.setHeight("28px");
			    	 
					return gridLayout;
			      };
			});
			
			table.setColumnWidth("feedBackType", 200);
		}
		
		if(!table.getVisibleItemIds().contains("feedbackIcon")){
			table.removeGeneratedColumn("feedbackIcon");
			table.addGeneratedColumn("feedbackIcon", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	 ManagerFeedBackPolicyTableDto dto = (ManagerFeedBackPolicyTableDto)itemId;
			    	 Button iconButton = null;
			    	 if(dto.getBtnFeedbackIcon() == null){
			    		 iconButton = new Button();
			    	 }else{
			    		 iconButton = dto.getBtnFeedbackIcon();
			    	 }
			    	 
			    	 iconButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	 dto.setBtnFeedbackIcon(iconButton);
					return iconButton;
			      };
			});
			
			table.setColumnWidth("feedbackIcon", 50);
		}
		
		
		table.removeGeneratedColumn("Delete");
		if(!table.getVisibleItemIds().contains("Deleted")){
			table.removeGeneratedColumn("Deleted");
			table.addGeneratedColumn("Deleted", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	table.removeGeneratedColumn("Delete");
			    	Button deleteButton = new Button("Delete");
			    	final ManagerFeedBackPolicyTableDto rowDto = (ManagerFeedBackPolicyTableDto)itemId;
			    	rowDto.setDeleteButton(deleteButton);
			    	Collection<?> itemIds = table.getItemIds();
			    	if(itemIds.size() == 1){
			    		deleteButton.setVisible(false);
			    	}
			    	
			    	if(itemIds.size() > 9){
			    		enableOrDisableAddButton(false);
			    	}else{
			    		enableOrDisableAddButton(true);
			    	}
			    	
			    	deleteButton.addClickListener(new Button.ClickListener() {
				        public void buttonClick(ClickEvent event) {
				        	final List<ManagerFeedBackPolicyTableDto> itemIds = ( List<ManagerFeedBackPolicyTableDto>)table.getItemIds();
				        	 if(null != rowDto.getIntimationNO() || null != rowDto.getRemarks() || null != rowDto.getFbCategory() || null!= rowDto.getFeedBackRating()){
									ConfirmDialog dialog = ConfirmDialog
											.show(getUI(),
													"Confirmation",
													"Do you want to Delete ?",
													"No", "Yes", new ConfirmDialog.Listener() {

														public void onClose(ConfirmDialog dialog) {
															if (!dialog.isConfirmed()) {
																deleteRow(itemId);
															} else {
																// User did not confirm
															//	deleteRowDetails(itemId,itemIds);
																dialog.close();
															}
														}
													});
						    		dialog.setClosable(false);
								}
								else
								{
									deleteRowDetails(itemId,itemIds);
								}
				        	 
				        	
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}
		table.setColumnHeader("Deleted"," ");	
		table.setColumnWidth("Deleted", 80);
		table.setPageLength(10);
		
		if(!table.getVisibleItemIds().contains("remarks")){
			table.removeGeneratedColumn("remarks");
			table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	  ManagerFeedBackPolicyTableDto dto = (ManagerFeedBackPolicyTableDto)itemId;
			    	  TextArea field = new TextArea();
//			    	  field.setWidth("350px");
			    	  field.setHeight("23px");
			    	  field.setWidth("349px");
			    	  field.setMaxLength(4000);
			    	  field.setData(dto);
			    	  SHAUtils.shortCutForFeedbackRemarks(field,null,getUI());
			    	  field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			    	  if(null != dto.getRemarks()){
			    	  dto.setRemarks(dto.getRemarks());
			    	  field.setValue(dto.getRemarks());
			    	  }
			    	  addListener(field);
					return field;
			      };
			});
		}
	
		if(!table.getVisibleItemIds().contains("fbCategory")){
			table.removeGeneratedColumn("fbCategory");
			table.addGeneratedColumn("fbCategory", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				    	 ManagerFeedBackPolicyTableDto dto = (ManagerFeedBackPolicyTableDto)itemId;
				    	 GComboBox comboBox = new GComboBox();
				    	 comboBox.setNullSelectionAllowed(false);
				    	 BeanItemContainer<SelectValue> selectValueContainer = masterService.getSelectValueCategoryContainer(ReferenceTable.FEEDBACK_TYPE_CLAIM_RETAIL);
				    	 selectValueContainer.sort(new Object[] {"id"}, new boolean[] {false});
				    	 comboBox.setContainerDataSource(selectValueContainer);				    	 
				    	 comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				    	 comboBox.setItemCaptionPropertyId("value");
				    	
				    	 if(dto.getFbCategory() != null){
						    dto.setFbCategory(dto.getFbCategory());
						    comboBox.setValue(dto.getFbCategory());
				    	 }
				    	 
				    	 addListenerForFeedBackCategory(comboBox, dto);
				    	 
						return comboBox;
				      };
			});
		}
			
	}

	@Override
	public void tableSelectHandler(ManagerFeedBackPolicyTableDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		 return "manager-feedback-policy-";
	}
	
	private void addListener(final TextArea textField)
	{	
		textField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String remarks = (String) event.getProperty().getValue();
					ManagerFeedBackPolicyTableDto tableDTO = (ManagerFeedBackPolicyTableDto)textField.getData();
					tableDTO.setRemarks(remarks);
				}
			}
		});
	}
	
	private void addListenerForFeedBackRating(final ComboBox comboBox,final ManagerFeedBackPolicyTableDto tableDto)
	{	
		comboBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					SelectValue value = (SelectValue)event.getProperty().getValue();
					tableDto.setFeedBackRating(value);
					GridLayout gridLayout = (GridLayout)comboBox.getData();
					gridLayout.setSpacing(false);
					/*if(gridLayout.getData() != null){
						Image existingImage = (Image)gridLayout.getData();
						gridLayout.removeComponent(existingImage);
					}*/
					setImagesBasedOnRating(value, gridLayout,tableDto);
				}
			}


		});
	}
	
	private void setImagesBasedOnRating(SelectValue value,
			GridLayout gridLayout,ManagerFeedBackPolicyTableDto tableDto) {
		if(value.getId().equals(ReferenceTable.FEEDBACK_TYPE_VERY_GOOD)){
			Button btnFeedbackIcon = tableDto.getBtnFeedbackIcon();
			btnFeedbackIcon.setIcon(new ThemeResource("images/vgood_icon.png"));
			/*Image vGood = new Image("", new ThemeResource("images/vgood_icon.png"));
			
			gridLayout.addComponent(vGood,1,0);
			gridLayout.setData(vGood);
			gridLayout.setComponentAlignment(vGood, Alignment.TOP_RIGHT);
//			vGood.setStyleName("gridAlignment");
//			vGood.addStyleName("gridAlignment");*/

		}
		if(value.getId().equals(ReferenceTable.FEEDBACK_TYPE_GOOD)){
			Button btnFeedbackIcon = tableDto.getBtnFeedbackIcon();
			btnFeedbackIcon.setIcon(new ThemeResource("images/good_icon.png"));
			/*Image good = new Image ("", new ThemeResource("images/good_icon.png"));
			gridLayout.addComponent(good,1,0);
			gridLayout.setData(good);
			gridLayout.setComponentAlignment(good, Alignment.TOP_RIGHT);
			good.setStyleName("gridAlignment");
			good.addStyleName("gridAlignment");*/
		}
		if(value.getId().equals(ReferenceTable.FEEDBACK_TYPE_SATISFACTORY)){
			Button btnFeedbackIcon = tableDto.getBtnFeedbackIcon();
			btnFeedbackIcon.setIcon(new ThemeResource("images/satisactory_icon.png"));
			/*Image satisfactory = new Image ("",new ThemeResource("images/satisactory_icon.png"));
			gridLayout.addComponent(satisfactory,1,0);
			gridLayout.setData(satisfactory);
			gridLayout.setComponentAlignment(satisfactory, Alignment.TOP_RIGHT);
			satisfactory.setStyleName("gridAlignment");
			satisfactory.addStyleName("gridAlignment");*/
		}
		if(value.getId().equals(ReferenceTable.FEEDBACK_TYPE_AVERAGE)){
			Button btnFeedbackIcon = tableDto.getBtnFeedbackIcon();
			btnFeedbackIcon.setIcon(new ThemeResource("images/average_icon.png"));
			/*Image average = new Image ("", new ThemeResource("images/average_icon.png"));
			gridLayout.addComponent(average,1,0);
			gridLayout.setData(average);
			gridLayout.setComponentAlignment(average, Alignment.TOP_RIGHT);
			average.setStyleName("gridAlignment");
			average.addStyleName("gridAlignment");*/
		}
		if(value.getId().equals(ReferenceTable.FEEDBACK_TYPE_BELOW_AVERAGE)){
			Button btnFeedbackIcon = tableDto.getBtnFeedbackIcon();
			btnFeedbackIcon.setIcon(new ThemeResource("images/below_average_icon.png"));
			/*Image bAverage = new Image ("", new ThemeResource("images/below_average_icon.png"));
			gridLayout.addComponent(bAverage,1,0);
			gridLayout.setData(bAverage);
			gridLayout.setComponentAlignment(bAverage, Alignment.TOP_RIGHT);
			bAverage.setStyleName("gridAlignment");
			bAverage.addStyleName("gridAlignment");*/
		}
//		 gridLayout.setStyleName("gridAlignment");
//		 gridLayout.addStyleName("gridAlignment");
	}
	
	
	private void deleteRowDetails(Object itemId,List<ManagerFeedBackPolicyTableDto> itemIds){
		
		policyRemarksList.add((ManagerFeedBackPolicyTableDto)itemId);
		
    	deleteRow(itemId);				        	
    	 if(itemIds.size() == 1){
    		ManagerFeedBackPolicyTableDto firstRow = (ManagerFeedBackPolicyTableDto) itemIds.get(0);
    		Button deleteButton2 = firstRow.getDeleteButton();
    		deleteButton2.setVisible(false);
	     }
    	 if(itemIds.size() > 9){
	    		enableOrDisableAddButton(false);
	    	}else{
	    		enableOrDisableAddButton(true);
	    	}
	}
	
	private void addListenerForFeedBackCategory(final ComboBox comboBox,final ManagerFeedBackPolicyTableDto tableDto)
	{	
		comboBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					SelectValue value = (SelectValue)event.getProperty().getValue();
					tableDto.setFbCategory(value);
					/*GridLayout gridLayout = (GridLayout)comboBox.getData();
					gridLayout.setSpacing(false);*/
				}
			}


		});
	}
}
