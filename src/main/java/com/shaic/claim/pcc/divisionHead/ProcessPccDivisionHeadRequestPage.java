package com.shaic.claim.pcc.divisionHead;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.QueryDetailsTable;
import com.shaic.claim.pcc.views.ReplyDetailsTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ProcessPccDivisionHeadRequestPage extends ViewComponent{



	@Inject
	private Instance<ProcessingDoctorDetailsTable> processingDoctorDetailsTableInst;
	private ProcessingDoctorDetailsTable processingDoctorDetailsTable;
	
	@Inject
	private Instance<QueryDetailsTable> queryDetailsTableInst;
	private QueryDetailsTable queryDetailsTable;

	@Inject
	private Instance<ReplyDetailsTable> replyDetailsTableInst;
	private ReplyDetailsTable replyDetailsTable;
	
	private PccDTO bean;

	private BeanFieldGroup<PccDTO> binder;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private SearchProcessPCCRequestTableDTO dto;
	
	private String presenterString;
	
	
	private PccDetailsTableDTO pccDetailsTableDTO;
	private TextArea txtRemarksForDivisionHead;
	private FormLayout dummyForm;
	private TextField dummytext;
	
	private VerticalLayout mainVLayout;
	private FormLayout forDivhead;

	@PostConstruct
	protected void initView() {

	}

	public void init(PccDetailsTableDTO  pccDetailsTableDTO, String presenterString) {
		
		this.presenterString=presenterString;
		this.pccDetailsTableDTO = pccDetailsTableDTO;
		
		this.binder = new BeanFieldGroup<PccDTO>(PccDTO.class);
		this.binder.setItemDataSource(new PccDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());


		dummyForm = new FormLayout();
		dummyForm.setWidth("500px");
		dummyForm.setHeight("45px");
		
		dummytext = new TextField();
		dummytext.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummytext.setWidth("-1px");
		dummytext.setHeight("-10px");
		
		processingDoctorDetailsTable = processingDoctorDetailsTableInst.get();
		processingDoctorDetailsTable.init("", false, false);
		processingDoctorDetailsTable.setCaption("Processing Doctor Details");
		if(pccDetailsTableDTO !=null){
			List<PccDetailsTableDTO> pccDetailsTableDTOs = new ArrayList<PccDetailsTableDTO>();
			pccDetailsTableDTOs.add(pccDetailsTableDTO);
			processingDoctorDetailsTable.setTableList(pccDetailsTableDTOs);
		}
		
		queryDetailsTable = queryDetailsTableInst.get();
		queryDetailsTable.init("", false, false);
		queryDetailsTable.setCaption("Query Details");
		if(pccDetailsTableDTO.getQueryDetails() !=null 
				&& !pccDetailsTableDTO.getQueryDetails().isEmpty()){
			queryDetailsTable.setTableList(pccDetailsTableDTO.getQueryDetails());
		}
		
		replyDetailsTable = replyDetailsTableInst.get();
		replyDetailsTable.init("", false, false);
		replyDetailsTable.setCaption("Reply Details");
		if(pccDetailsTableDTO.getReplyDetails() !=null 
				&& !pccDetailsTableDTO.getReplyDetails().isEmpty()){
			replyDetailsTable.setTableList(pccDetailsTableDTO.getReplyDetails());
		}

		
		txtRemarksForDivisionHead = binder.buildAndBind("Reply Remarks", "remarkForResponse", TextArea.class);
		txtRemarksForDivisionHead.setMaxLength(1000);
		txtRemarksForDivisionHead.setWidth("278px");
		txtRemarksForDivisionHead.setHeight("130px");
		txtRemarksForDivisionHead.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtRemarksForDivisionHead,null,getUI(),SHAConstants.STP_REMARKS);
		
		forDivhead = new FormLayout(txtRemarksForDivisionHead);
		

		mainVLayout = new VerticalLayout(processingDoctorDetailsTable,queryDetailsTable,replyDetailsTable,dummytext,forDivhead);
		setCompositionRoot(mainVLayout);

	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
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
	
	//mandatoryFields.add(txtRemarksForDivisionHead);
	public Boolean validatePage() {
		
		Boolean hasError=false;
		StringBuffer eMsg = new StringBuffer(); 
		
			if(txtRemarksForDivisionHead != null && txtRemarksForDivisionHead.getValue() == null){
					
				hasError=true;
				eMsg.append("Enter Remarks to proceed </br> ");
			}	
		
		if(hasError){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			return !hasError;
		}
	
		return !hasError;
	}

	public PccDTO getvalues() {
		
		
			PccDTO pccDTO = binder.getItemDataSource().getBean();
			pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
			if(txtRemarksForDivisionHead != null && txtRemarksForDivisionHead.getValue() != null){			
				pccDTO.setRemarkForResponse(txtRemarksForDivisionHead.getValue());
			}
			return pccDTO;
		
	}

}
