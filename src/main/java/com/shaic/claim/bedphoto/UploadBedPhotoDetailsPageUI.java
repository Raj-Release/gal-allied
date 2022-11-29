package com.shaic.claim.bedphoto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.CommonFileUpload;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
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

public class UploadBedPhotoDetailsPageUI extends ViewComponent{
	
	@Inject
	private CommonFileUpload commonFileUpload;
	
	@Inject
	private UploadBedPhotoTable uploadBedPhotoTable;
	
	@Inject
	private UploadBedPhotoDetailsLayout bedPhotoLayout;
	
	@EJB
	private IntimationService intimationService;
	
	private Button submitBtn;
    private Button cancelBtn;
    
    private SearchBedPhotoTableDTO bean;
    private BeanFieldGroup<SearchBedPhotoTableDTO> binder;
    private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
    
    private String screenName;
    private Window popup;
    
    private HorizontalLayout buttonHorLayout;
    
    private TextField uploadBedPhotoDate;
    
    private TextArea uploadBedPhotoRemarks;
    
    public void init(SearchBedPhotoTableDTO bean, VerticalLayout mainLayout,String screenName, Window popup){
		this.bean = bean;
		this.screenName = screenName;
		this.popup =popup;
		initBinder();
		HorizontalLayout horizontalLayout =new HorizontalLayout();
		horizontalLayout.setSpacing(Boolean.TRUE);
		horizontalLayout.setSizeFull();
		horizontalLayout.setCaption("");
		
		uploadBedPhotoDate = new TextField("Date Of Upload");
		String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		uploadBedPhotoDate.setValue(dateformat);
		uploadBedPhotoDate.setReadOnly(true);
		
		uploadBedPhotoRemarks =  new TextArea("Bed Photo Upload Remarks");
		uploadBedPhotoRemarks.setRequired(true);
		uploadBedPhotoRemarks.setReadOnly(false);
		uploadBedPhotoRemarks.setWidth("500px");
//		uploadBedPhotoRemarks.setId("InvComplnRemarks");
		uploadBedPhotoRemarks.setData(bean);
		
		FormLayout dateandremarkslayout = new FormLayout();
		dateandremarkslayout.addComponent(uploadBedPhotoDate);
		dateandremarkslayout.addComponent(uploadBedPhotoRemarks);
		
		VerticalLayout verticalLayout = new VerticalLayout(dateandremarkslayout);
		verticalLayout.setSpacing(Boolean.TRUE);
		bedPhotoLayout.init();
		Label lblUploadDocumentsTable = new Label("<H3>Uploaded Documents</H3>",ContentMode.HTML);
		
		uploadBedPhotoTable.init(" ",false,false);
		
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
		commonFileUpload.init(key,ReferenceTable.UPLOAD_BED_PHOTO,"Bed Photo Details");
		Table table = new Table();
		table.setPageLength(table.size()+2);
		table.setHeight("150px");
		table.addContainerProperty("File Upload", CommonFileUpload.class, null);
		table.addContainerProperty("File Type", String.class, null);
		Object addItem = table.addItem();
	    table.getContainerProperty(addItem, "File Upload").setValue(commonFileUpload);
		
		mainLayout = new VerticalLayout(horizontalLayout, commonFileUpload,lblUploadDocumentsTable,uploadBedPhotoTable,verticalLayout,buttonHorLayout);
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
		this.binder = new BeanFieldGroup<SearchBedPhotoTableDTO>(SearchBedPhotoTableDTO.class);
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
				            	/*if(screenName.equalsIgnoreCase(SHAConstants.PRE_AUTH_ENHANCEMENT)){
				            		 popup.close();
				            		 dialog.close();
				            	}
				            	else */if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.UPLOAD_BED_PHOTO, true);
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
				
						fireViewEvent(BedPhotoDetailsPresenter.SUBMIT_EVENT,bean,screenName);
			}
		});
		
	
    }
    
	public void setUploadTableValues(Long intimationKey){
		uploadBedPhotoTable.removeRow();
		uploadBedPhotoTable.setTableList(intimationService.getBedPhotoDetails(intimationKey));
		
	}
    
    protected void showOrHideValidation(Boolean isVisible) {
    	for (Component component : mandatoryFields) {
    		AbstractField<?>  field = (AbstractField<?>)component;
    		field.setRequired(!isVisible);
    		field.setValidationVisible(isVisible);
    	}
    }
	
	
	
	

}
