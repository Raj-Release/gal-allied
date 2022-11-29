package com.shaic.claim.pancard.page;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.CommonFileUpload;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class UpdatePanCardReportUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	private VerticalLayout mainLayout;


	@Inject
	private UpdatePanCardReportLayout updatePanCardReportLayout;

	private TextField proposerName;
	private TextField policyNo;
	
	private TextField panCardNo;
	private TextArea panRemarks;
	
	private Button submitBtn;
		
    private Button cancelBtn;
    
    private HorizontalLayout buttonHorLayout;
    private FormLayout form1;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject	
	private UpdatepanCardDocumentsUI uploadedDocumentsUI;
	
	@Inject
	private UploadedPanCardDocumentsTable uploadedPanCardDocumentsTable;
	
	@Inject
	private CommonFileUpload fileUploadUI;
	
	@Inject
	private ViewDetails viewDetails;

	@Inject
	private IntimationService intimationService;

	private SearchUploadPanCardTableDTO bean;


	private BeanFieldGroup<SearchUploadPanCardTableDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private String screenName;



	private Window popup;
	
	

	public void init(SearchUploadPanCardTableDTO bean, VerticalLayout mainLayout,String screenName, Window popup) {
		this.bean = bean;
		this.screenName = screenName;
		this.popup =popup;
		initBinder();
		proposerName = (TextField) binder.buildAndBind("Proposer Name", "proposerName", TextField.class);
		policyNo = (TextField) binder.buildAndBind("Policy number", "policyNo", TextField.class);
		proposerName.setReadOnly(Boolean.TRUE);
		policyNo.setReadOnly(Boolean.TRUE);
		HorizontalLayout horizontalLayout =new HorizontalLayout(proposerName,policyNo);
		horizontalLayout.setSpacing(Boolean.TRUE);
		horizontalLayout.setSizeFull();
		horizontalLayout.setCaption("");
		panCardNo = (TextField) binder.buildAndBind("Pan Card No", "panCardNo", TextField.class);
		panRemarks = (TextArea) binder.buildAndBind("Pan Remarks", "panRemarks", TextArea.class);
		mandatoryFields.add(panCardNo);
		panCardNo.setRequired(Boolean.TRUE);
		VerticalLayout verticalLayout = new VerticalLayout(panCardNo, panRemarks);
		verticalLayout.setSpacing(Boolean.TRUE);
		updatePanCardReportLayout.init();
		Label lblUploadDocumentsTable = new Label("<H3>Uploaded Documents</H3>",ContentMode.HTML);
		
		uploadedPanCardDocumentsTable.init(" ",false,false);
		
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
		fileUploadUI.init(key,ReferenceTable.UPDATE_PANCARD_SCREEN,"Pan Card Details");
		Table table = new Table();
		table.setPageLength(table.size()+2);
		table.setHeight("150px");
		table.addContainerProperty("File Upload", CommonFileUpload.class, null);
		table.addContainerProperty("File Type", String.class, null);
		Object addItem = table.addItem();
	    table.getContainerProperty(addItem, "File Upload").setValue(fileUploadUI);
		
		mainLayout = new VerticalLayout(horizontalLayout, verticalLayout, fileUploadUI,lblUploadDocumentsTable,uploadedPanCardDocumentsTable,buttonHorLayout);
		mainLayout.setSpacing(Boolean.TRUE);
		mainLayout.setSizeFull();
		mainLayout.setCaption("");
		mainLayout.setComponentAlignment(lblUploadDocumentsTable, Alignment.MIDDLE_LEFT);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
//		mainLayout.setComponentAlignment(updatePanCardReportLayout,
//				Alignment.MIDDLE_LEFT);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		showOrHideValidation(false);
		addListener();
		//setTableData();
		setCompositionRoot(mainLayout);
		/*fireViewEvent(
				UpdatePanCardReportPresenter.SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS,
				investigationKey,this.rodKey);*/
		//return mainLayout;
	}
	

	public void setCompleteLayout(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		
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
				                	fireViewEvent(MenuItemBean.UPDATE_PAN_CARD, true);
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
				
					List<UploadedPanCardDocumentsDTO> values = uploadedPanCardDocumentsTable.getValues();
					if(values != null && ! values.isEmpty()){
						fireViewEvent(UpdatePanCardReportPresenter.SUBMIT_EVENT,bean,screenName);
						if(screenName.equalsIgnoreCase(SHAConstants.PRE_AUTH_ENHANCEMENT)){
		            		 popup.close();
		            	}
					}else{
						showErrorPopUp("Please upload atleast one file");
					}
				}

			}
		});
		
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
	

	
	public void setUploadTableValues(Long intimationKey){
		uploadedPanCardDocumentsTable.removeRow();
		uploadedPanCardDocumentsTable.setTableList(intimationService.getUploadDocumentList(intimationKey));
		
	}

	
	
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<SearchUploadPanCardTableDTO>(SearchUploadPanCardTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
}
	
public boolean validatePage() {
	Boolean hasError = false;
	showOrHideValidation(true);
	StringBuffer eMsg = new StringBuffer();
	
	if (!this.binder.isValid()) {
	    for (Field<?> field : this.binder.getFields()) {
	    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
	    	if (errMsg != null) {
	    		eMsg.append(errMsg.getFormattedHtmlMessage());
	    	}
	    	hasError = true;
	    }
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

protected void showOrHideValidation(Boolean isVisible) {
	for (Component component : mandatoryFields) {
		AbstractField<?>  field = (AbstractField<?>)component;
		field.setRequired(!isVisible);
		field.setValidationVisible(isVisible);
	}
}


public void setUploadTableValues(MultipleUploadDocumentDTO dto) {
	uploadedPanCardDocumentsTable.removeRow();
	uploadedPanCardDocumentsTable.setTableList(intimationService.getUploadDocumentList(dto));
	
}

}
