package com.shaic.claim.aadhar.pages;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.CommonFileUpload;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.validator.IntegerRangeValidator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateAadharDetailsPageUI extends ViewComponent {
	
	@Inject
	private CommonFileUpload fileUploadUI;
	
	@Inject
	private UpdateAadharDetailsTable aadharDetailsTable;
	
	@Inject
	private UpdateAadharDetailsLayout aadharDetailsLayout;
	
	@EJB
	private IntimationService intimationService;
	
	private TextField insuredName;
	private TextField policyNo;
	
	private TextField aadharNo;
	private TextArea aadharRemarks;
	
	private Button submitBtn;
    private Button cancelBtn;
    
    private SearchUpdateAadharTableDTO bean;
    private BeanFieldGroup<SearchUpdateAadharTableDTO> binder;
    private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
    
    private String screenName;
    private Window popup;
    
    private HorizontalLayout buttonHorLayout;
    
    
    public void init(SearchUpdateAadharTableDTO bean, VerticalLayout mainLayout,String screenName, Window popup){
		this.bean = bean;
		this.screenName = screenName;
		this.popup =popup;
		initBinder();
		insuredName = (TextField) binder.buildAndBind("Insured Name", "insuredName", TextField.class);
		policyNo = (TextField) binder.buildAndBind("Policy number", "policyNo", TextField.class);
		insuredName.setReadOnly(Boolean.TRUE);
		policyNo.setReadOnly(Boolean.TRUE);
		HorizontalLayout horizontalLayout =new HorizontalLayout(insuredName,policyNo);
		horizontalLayout.setSpacing(Boolean.TRUE);
		horizontalLayout.setSizeFull();
		horizontalLayout.setCaption("");
		aadharNo = (TextField) binder.buildAndBind("Aadhar Card No", "aadharCardNo", TextField.class);
		aadharRemarks = (TextArea) binder.buildAndBind("Aadhar Remarks", "aadharRemarks", TextArea.class);
//		mandatoryFields.add(aadharNo);
//		aadharNo.setRequired(Boolean.TRUE);
		aadharNo.setMaxLength(12);
//		aadharNo.setConverter(Integer.class);
		
		CSValidator aadharNoValidator = new CSValidator();
		aadharNoValidator.extend(aadharNo);
		aadharNoValidator.setRegExp("^[0-9]*$");
//		aadharNoValidator.setRegExp("^[0-9]*");
		aadharNoValidator.setPreventInvalidTyping(true);
		
		VerticalLayout verticalLayout = new VerticalLayout(aadharNo, aadharRemarks);
		verticalLayout.setSpacing(Boolean.TRUE);
		aadharDetailsLayout.init();
		Label lblUploadDocumentsTable = new Label("<H3>Uploaded Documents</H3>",ContentMode.HTML);
		
		aadharDetailsTable.init(" ",false,false);
		
	    submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
		
		buttonHorLayout.setSpacing(true);
		Long key = bean.getNewIntimationDto().getKey();
		fileUploadUI.init(key,ReferenceTable.UPDATE_AADHAR_SCREEN,"Aadhar Card Details");
		Table table = new Table();
		table.setPageLength(table.size()+2);
		table.setHeight("150px");
		table.addContainerProperty("File Upload", CommonFileUpload.class, null);
		table.addContainerProperty("File Type", String.class, null);
		Object addItem = table.addItem();
	    table.getContainerProperty(addItem, "File Upload").setValue(fileUploadUI);
		
		mainLayout = new VerticalLayout(horizontalLayout, verticalLayout, fileUploadUI,lblUploadDocumentsTable,aadharDetailsTable,buttonHorLayout);
		mainLayout.setSpacing(Boolean.TRUE);
		mainLayout.setSizeFull();
		mainLayout.setCaption("");
		mainLayout.setComponentAlignment(lblUploadDocumentsTable, Alignment.MIDDLE_LEFT);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		showOrHideValidation(false);
		addListener();
		setCompositionRoot(mainLayout);

	}
    
    public void initBinder() {
		this.binder = new BeanFieldGroup<SearchUpdateAadharTableDTO>(SearchUpdateAadharTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
}
    
    private void addListener(){

		
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
				            	if(screenName.equalsIgnoreCase(SHAConstants.PRE_AUTH_ENHANCEMENT)){
				            		 popup.close();
				            		 dialog.close();
				            	}
				            	else if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.UPDATE_AADHAR_DETAILS, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					/*Below condition removed as per Raja instructions not mandatory to upload files*/
					/*List<UpdateAadharDetailsDTO> values = aadharDetailsTable.getValues();
					if(values != null && ! values.isEmpty()){*/
						fireViewEvent(UpdateAadharDetailsPresenter.SUBMIT_EVENT,bean,screenName);
						if(screenName.equalsIgnoreCase(SHAConstants.PRE_AUTH_ENHANCEMENT)){
		            		 popup.close();
		            	}
					/*}else{
						showErrorPopUp("Please upload atleast one file");
					}*/
				}

			}
		});
		
	
    }
    
	public void setUploadTableValues(Long intimationKey){
		aadharDetailsTable.removeRow();
		aadharDetailsTable.setTableList(intimationService.getUploadAadharDocumentList(intimationKey));
		
	}
    
    protected void showOrHideValidation(Boolean isVisible) {
    	for (Component component : mandatoryFields) {
    		AbstractField<?>  field = (AbstractField<?>)component;
    		field.setRequired(!isVisible);
    		field.setValidationVisible(isVisible);
    	}
    }
    
    public Boolean validatePage(){

    	Boolean hasError = false;
    	showOrHideValidation(true);
    	StringBuffer eMsg = new StringBuffer();
    	
    	if (aadharNo.getValue() == null) {
    		hasError = true;
    		eMsg.append("Please Enter Aadhar No");
    	} else if(aadharNo.getValue().length()<12){
    		hasError = true;
    		eMsg.append("Please Enter 12 Digit Aadhar No");
    	}
    	   if(hasError) {
    		   showOrHideValidation(false);
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
    	   } else {
    			try {
    				this.binder.commit();
    				
    			} catch (CommitException e) {
    				e.printStackTrace();
    			}
    		   showOrHideValidation(false);
    		   return true;
    	   }

    }
	public void showErrorPopUp(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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
	}

}
