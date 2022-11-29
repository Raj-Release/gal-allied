package com.shaic.claim.pcc.views;

import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.pcc.dto.ViewPCCRemarksDTO;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPCCRemarksDetailsPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7874417450890458639L;

	private TextField txtIntimationNo;
	
	private TextField userNameAndId;
	
	private TextField dateAndTime;
	
	private TextField remarksType;
	
	private TextArea remarks;
	
	private TextField userRole;
	
	private TextField userNameAndIdforDoct;
	
	private TextField dateAndTimeforDoct;
	
	private TextField remarksTypeforDoct;
	
	private TextArea remarksforDoct;
	
	private TextField userRoleforDoct;
	
	private VerticalLayout mainVertical= null;
	
	private BeanFieldGroup<ViewPCCRemarksDTO> binder;
	
	@Inject
	private Instance<QueryDetailsTable> queryDetailsTableInst;
	
	private QueryDetailsTable queryDetailsTable;

	@Inject
	private Instance<ReplyDetailsTable> replyDetailsTableInst;
	
	private ReplyDetailsTable replyDetailsTable;
	
	@Inject
	private Instance<PCCDetailsTable> pccDetailsTableInst;
	
	private PCCDetailsTable pccDetailsTable;
	
	@Inject
	private Instance<PCCUploadedFileDocsTable> pccUploadedFileDocsTableInst;
	
	private PCCUploadedFileDocsTable pccUploadedFileDocsTable;
	
	private ViewPCCRemarksDTO bean;

    public void init(ViewPCCRemarksDTO viewPCCRemarksDTO){
    	
    	this.bean=viewPCCRemarksDTO;   	
    	this.binder = new BeanFieldGroup<ViewPCCRemarksDTO>(ViewPCCRemarksDTO.class);
		this.binder.setItemDataSource(viewPCCRemarksDTO);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
    	
        txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
    	userNameAndIdforDoct = (TextField) binder.buildAndBind("User Name / ID","userNameAndIdforDoct",TextField.class);
    	dateAndTimeforDoct = (TextField) binder.buildAndBind("Date / Time","dateAndTimeforDoct",TextField.class);
    	remarksTypeforDoct = (TextField) binder.buildAndBind("Remarks Type","remarksTypeforDoct",TextField.class);
    	remarksforDoct = (TextArea) binder.buildAndBind("Remarks","remarksforDoct",TextArea.class);
    	userRoleforDoct = (TextField) binder.buildAndBind("User Role","userRoleforDoct",TextField.class);
    	
    	userNameAndId = (TextField) binder.buildAndBind("User Name / ID","userNameAndId",TextField.class);
    	dateAndTime = (TextField) binder.buildAndBind("Date / Time","dateAndTime",TextField.class);
    	remarksType = (TextField) binder.buildAndBind("Remarks Type","remarksType",TextField.class);
    	remarks = (TextArea) binder.buildAndBind("Remarks","remarks",TextArea.class);
    	userRole = (TextField) binder.buildAndBind("User Role","userRole",TextField.class);
    	//uploadFileName = (TextField) binder.buildAndBind("File Name","uploadDocFileName",TextField.class);
    	
    	FormLayout forIntimation=new FormLayout(txtIntimationNo);
    	forIntimation.setHeight("3rem");
    	
    	FormLayout forUser=new FormLayout(userNameAndId, remarks);
    	FormLayout forDate=new FormLayout(dateAndTime);
    	FormLayout forRemarksType=new FormLayout(remarksType);
    	FormLayout forRemarks=new FormLayout(userRole);
    	
    	FormLayout forUserforDoc=new FormLayout(userNameAndIdforDoct, remarksforDoct);
    	FormLayout forDateforDoc=new FormLayout(dateAndTimeforDoct);
    	FormLayout forRemarksTypeforDoc=new FormLayout(remarksTypeforDoct);
    	FormLayout forRemarksforDoc=new FormLayout(userRoleforDoct);
    	
    	setReadOnly(forIntimation,true);
    	setReadOnly(forUser,true);
    	setReadOnly(forDate,true);
    	setReadOnly(forRemarksType,true);
    	setReadOnly(forRemarks,true);
    	setReadOnly(forUserforDoc,true);
    	setReadOnly(forDateforDoc,true);
    	setReadOnly(forRemarksTypeforDoc,true);
    	setReadOnly(forRemarksforDoc,true);
    	
    	
    	HorizontalLayout doctorHLayout =new HorizontalLayout(forUserforDoc,forDateforDoc,forRemarksTypeforDoc,forRemarksforDoc);  
    	HorizontalLayout requestHLayout=new HorizontalLayout(forUser,forDate,forRemarksType,forRemarks);
    	doctorHLayout.setSpacing(false);
    	requestHLayout.setSpacing(false);
    	
    	queryDetailsTable = queryDetailsTableInst.get();
    	queryDetailsTable.init("", false, false);
    	if(viewPCCRemarksDTO !=null && viewPCCRemarksDTO.getQueryDetails() !=null
    			&& !viewPCCRemarksDTO.getQueryDetails().isEmpty()){
    		queryDetailsTable.setTableList(viewPCCRemarksDTO.getQueryDetails());
		}
    	   	
    	replyDetailsTable = replyDetailsTableInst.get();
    	replyDetailsTable.init("", false, false);
    	if(viewPCCRemarksDTO !=null && viewPCCRemarksDTO.getReplyDetails() !=null
    			&& !viewPCCRemarksDTO.getReplyDetails().isEmpty()){
    		replyDetailsTable.setTableList(viewPCCRemarksDTO.getReplyDetails());
		}
    	
    	pccDetailsTable = pccDetailsTableInst.get();
    	pccDetailsTable.init("", false, false);
    	if(viewPCCRemarksDTO !=null && viewPCCRemarksDTO.getReplyDetails() !=null
    			&& !viewPCCRemarksDTO.getReplyDetails().isEmpty()){
    		pccDetailsTable.setTableList(viewPCCRemarksDTO.getPccDetails());
		}
    	
    	pccUploadedFileDocsTable = pccUploadedFileDocsTableInst.get();
    	pccUploadedFileDocsTable.init("", false, false);
    	if(viewPCCRemarksDTO !=null && viewPCCRemarksDTO.getPccUploadedFileDetails()!=null
    			&& !viewPCCRemarksDTO.getPccUploadedFileDetails().isEmpty()){
    		pccUploadedFileDocsTable.setTableList(viewPCCRemarksDTO.getPccUploadedFileDetails());
		}
    	  	
    	VerticalLayout tableLayout = new VerticalLayout(forIntimation,doctorHLayout,requestHLayout);
    	
    	
    	
    	Panel mainPanel = new Panel(tableLayout);
		mainPanel.addStyleName("girdBorder");
		mainPanel.setSizeFull();
		mainPanel.setHeightUndefined();
		
		Panel pccUploadedFilePanel = new Panel(pccUploadedFileDocsTable);
    	//pccUploadedFilePanel.addStyleName("girdBorder");
    	pccUploadedFilePanel.setCaption("Uploaded Documents");
    	pccUploadedFilePanel.setWidth("40%");
		
		Panel replyPanel = new Panel(replyDetailsTable);
    	replyPanel.addStyleName("girdBorder");
    	replyPanel.setCaption("Reply Details");
    	
    	Panel queryPanel = new Panel(queryDetailsTable);
    	queryPanel.addStyleName("girdBorder");
    	queryPanel.setCaption("Query Details");
    	
    	Panel pccPanel = new Panel(pccDetailsTable);
    	pccPanel.addStyleName("girdBorder");
    	pccPanel.setCaption("Approved Details");
    	
    	
    	
    	mainVertical=new VerticalLayout(mainPanel,pccUploadedFilePanel,pccPanel,queryPanel,replyPanel);
    	mainVertical.setSpacing(true);
		mainVertical.setMargin(true);
		setCompositionRoot(mainVertical);
		
   }	
    
    @SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("15em");
				field.setSizeUndefined();
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("15em");
				field.setNullRepresentation("-");
				field.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(field,null,getUI(),SHAConstants.STP_REMARKS);
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

    public void setClearReferenceData(){
    	if(mainVertical!=null){
    		mainVertical.removeAllComponents();
    	}
    	
    	if(this.queryDetailsTable!=null){
    		this.queryDetailsTable.removeRow();
    		this.queryDetailsTable=null;
    	}
    	if(this.replyDetailsTable!=null){
    		this.replyDetailsTable.removeRow();
    		this.replyDetailsTable=null;
    	}
    	if(this.pccDetailsTable!=null){
    		this.pccDetailsTable.removeRow();
    		this.pccDetailsTable=null;
    	}
    	if(this.pccUploadedFileDocsTable!=null){
    		this.pccUploadedFileDocsTable.removeRow();
    		this.pccUploadedFileDocsTable=null;
    	}
    	this.binder=null;
    }
}
