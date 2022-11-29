package com.shaic.claim.pedquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PedQueryPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private OldPedEndorsementDTO bean;
	
	private BeanFieldGroup<OldPedEndorsementDTO> binder;
	
	private SearchPEDQueryTableDTO searchDTO;
	
	private OldPedEndorsementDTO submitBean;
	
	private ComboBox txtPEDSuggestion;
	
	private TextField txtNameofPED;
	
	private TextField txtRemarks;
	
	private DateField txtRepudiationLetterDate;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private OldPedEndorsementDTO tableRows;
	
    private BeanItemContainer<SelectValue> selectPedCodeContainer;
	
	private BeanItemContainer<SelectValue> icdChapterContainer;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private BeanItemContainer<SelectValue> pedValueContainer;
	
    private TextArea txtQueryRemarks;
    
    private HorizontalLayout buttonHorLayout;
	
	private TextArea txtQueryReplyRemarks;
	
    private Button submitBtn;
	
	private Button cancelBtn;
	
	@Inject
	private Instance<PEDQueryEditableTable> initiatePEDEndorsementTable;
	
	private PEDQueryEditableTable initiatePEDEndorsementObj;
	
	private HorizontalLayout intimatePEDFLayout;
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void initView(SearchPEDQueryTableDTO searchDTO,OldPedEndorsementDTO editDTO){

        if(editDTO==null){
        	
        	this.searchDTO=searchDTO;
			
			fireViewEvent(PEDQueryPresenter.SET_FIELD_DATA, searchDTO);
			
			fireViewEvent(PEDQueryPresenter.SET_SECOND_TABLE, searchDTO);
		}
		else
		{
			fireViewEvent(PEDQueryPresenter.SET_EDIT_DATA,editDTO);
			
			fireViewEvent(PEDQueryPresenter.SET_SECOND_EDIT_TABLE, editDTO);
		}
        
		this.binder = new BeanFieldGroup<OldPedEndorsementDTO>(OldPedEndorsementDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		txtPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","pedSuggestion", ComboBox.class);
		
		txtPEDSuggestion.setContainerDataSource(this.selectValueContainer);
		txtPEDSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtPEDSuggestion.setItemCaptionPropertyId("value");
		
		txtPEDSuggestion.setValue(this.bean.getPedSuggestion());
		
		HorizontalLayout IntimatePEDLayout=buildIntimatePEDFLayout();
		
		initiatePEDEndorsementObj=initiatePEDEndorsementTable.get();
		//initiatePEDEndorsementObj.init("", true);
		initiatePEDEndorsementObj.init();
		
	   initiatePEDEndorsementObj.setReferenceData(this.referenceData);
	   referenceData.put("pedCode", this.selectPedCodeContainer);
	   referenceData.put("ICDChapter", this.icdChapterContainer);
	
       initiatePEDEndorsementObj.setTableList(this.tableRows.getNewInitiatePedEndorsementDto());
	
	   VerticalLayout editVeritical=new VerticalLayout(initiatePEDEndorsementObj);
	   
	    submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		try{
			binder.commit();
		    bean = binder.getItemDataSource().getBean();
		    if(submitBean==null){
		    submitBean=bean;
		    }
		}
		
		catch(CommitException e){
			e.printStackTrace();
		}
		addListener();
		
		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
		buttonHorLayout.setSpacing(true);
	   
	   VerticalLayout verticalLayout = new VerticalLayout(
				IntimatePEDLayout, editVeritical,buildPEDRefference(),buttonHorLayout);
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		setCompositionRoot(verticalLayout);
	}
	
	private HorizontalLayout buildIntimatePEDFLayout() {

		txtNameofPED = (TextField) binder.buildAndBind("Name of PED",
				"pedName", TextField.class);
		txtNameofPED.setMaxLength(100);
		
		txtNameofPED.setValue(this.bean.getPedName());

		txtRemarks = (TextField) binder.buildAndBind("Remarks",
				"remarks", TextField.class);
		txtRemarks.setNullRepresentation("");
		txtRemarks.setMaxLength(100);

		txtRepudiationLetterDate = (DateField) binder.buildAndBind(
				"Repudiation Letter Date", "repudiationLetterDate",
				DateField.class);
		
		intimatePEDFLayout = new HorizontalLayout(txtPEDSuggestion,
				txtNameofPED, txtRemarks);
		
		if(txtPEDSuggestion.getValue().toString().toLowerCase()
				.contains("sug 002 - cancel policy"))
				{
					intimatePEDFLayout.addComponent(txtRepudiationLetterDate);
				}
				else
				{
					txtRepudiationLetterDate.setValue(null);
					unbindField(txtRepudiationLetterDate);
					intimatePEDFLayout.removeComponent(txtRepudiationLetterDate);
				}

		intimatePEDFLayout.setSpacing(true);
		return intimatePEDFLayout;
	}
	
	private HorizontalLayout buildPEDRefference() {

		txtQueryRemarks = (TextArea) binder.buildAndBind(
				"Query Remarks", "reasonforReferring", TextArea.class);
		txtQueryRemarks.setNullRepresentation("");
		txtQueryRemarks.setReadOnly(true);
		txtQueryRemarks.setWidth("400px");

		txtQueryReplyRemarks = (TextArea) binder.buildAndBind(
				"Query Reply Remarks", "replyRemarks", TextArea.class);
		txtQueryReplyRemarks.setRequired(true);
		//Vaadin8-setImmediate() txtQueryReplyRemarks.setImmediate(true);
		txtQueryReplyRemarks.setNullRepresentation("");
		txtQueryReplyRemarks.setMaxLength(100);
		txtQueryReplyRemarks.setWidth("400px");
//		CSValidator validator = new CSValidator();
//		validator.extend(txtQueryReplyRemarks);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);

		mandatoryFields.add(txtQueryReplyRemarks);
		
		showOrHideValidation(false);

		HorizontalLayout pedRefferenceFLayout = new HorizontalLayout(txtQueryRemarks,
				txtQueryReplyRemarks);
		pedRefferenceFLayout.setSpacing(true);
		
		return pedRefferenceFLayout;
	}
	
	public void addListener() {
		
		cancelBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
					        "No", "Yes", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                	
					                	VaadinSession session = getSession();
										//SHAUtils.releaseHumanTask(searchDTO.getUsername(), searchDTO.getPassword(), searchDTO.getTaskNumber(),session);
					                	releaseHumanTask();
					                	fireViewEvent(MenuItemBean.PROCESS_PED_QUERY, true);
					                	
					                } else {
					                    dialog.close();
					                }
					            }
					        });
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			});
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
					try{
						binder.commit();
					    bean = binder.getItemDataSource().getBean();
					}
					catch(CommitException e){
						e.printStackTrace();
					}
				if(submitBean.getKey()==bean.getKey()){
					submitBean=bean;
				}
				else
				{
					submitBean.setReplyRemarks(bean.getReplyRemarks());
				}

				bean.setNewInitiatePedEndorsementDto(initiatePEDEndorsementObj.getValues());
				
				fireViewEvent(PEDQueryPresenter.SUBMIT_BUTTON_CLICK, bean,searchDTO);
				}
			}
		});
		
		txtPEDSuggestion.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (txtPEDSuggestion.getValue() != null) {
					if (txtPEDSuggestion.getValue().toString().toLowerCase()
							.contains("sug 002 - cancel policy")) {
						intimatePEDFLayout
								.addComponent(txtRepudiationLetterDate);
						txtRepudiationLetterDate.setValue(bean.getRepudiationLetterDate());
					} else {
						txtRepudiationLetterDate.setValue(null);
						unbindField(txtRepudiationLetterDate);
						intimatePEDFLayout.removeComponent(txtRepudiationLetterDate);
					}
				}
				else
				{
					txtPEDSuggestion.setValue(bean.getPedSuggestion());
				}
			}
		});
	}
	
	public void setReferenceData(OldPedEndorsementDTO bean,BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> pedValueContainer){
		this.bean=bean;
		this.selectValueContainer=selectValueContainer;
		this.pedValueContainer=pedValueContainer;
	}
	
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer){
		initiatePEDEndorsementObj.setIcdBlock(icdBlockContainer);	
	}
	
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		initiatePEDEndorsementObj.setIcdCode(icdCodeContainer);
	}
	
	public void setPEDCode(String pedCode) {
		initiatePEDEndorsementObj.setPEDCode(pedCode);
	}
	
	public void setPEDEndorsementTable(OldPedEndorsementDTO tableRows,Map<String, Object> referenceData,BeanItemContainer<SelectValue> pedCodeContainer,BeanItemContainer<SelectValue> icdChapterContainer) {
		this.referenceData=referenceData;
		this.tableRows=tableRows;
		this.selectPedCodeContainer=pedCodeContainer;
		this.icdChapterContainer=icdChapterContainer;
	}

	
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if(this.initiatePEDEndorsementObj != null && this.initiatePEDEndorsementObj.getValues().isEmpty()) {
			hasError = true;
			eMsg.append("Please Add Atleast one diagnosis List Details to Proceed Further. </br>"); 
		}
		
		if (this.initiatePEDEndorsementObj != null){
			boolean isValid = this.initiatePEDEndorsementObj.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.initiatePEDEndorsementObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} 
			showOrHideValidation(false);
			return true;
		}
	
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
 		}
 		
 		if(wrkFlowKey != null){
 			DBCalculationService dbService = new DBCalculationService();
 			dbService.callUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
}
