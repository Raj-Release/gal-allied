package com.shaic.claim.pcc.reassignHRM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCQuery;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestPresenter;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.ZonalMedicalDetailsTable;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinateRequestPresenter;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ProcessPccReassignHRMCoordinatorRequestPage extends ViewComponent{

	@Inject
	private Instance<ProcessingDoctorDetailsTable> processingDoctorDetailsTableInst;
	private ProcessingDoctorDetailsTable processingDoctorDetailsTable;
	
	@Inject
	private Instance<ZonalMedicalDetailsTable> zonalMedicalDetailsTableInst;
	private ZonalMedicalDetailsTable zonalMedicalDetailsTable;
	
	private PccDTO bean;

	private BeanFieldGroup<PccDTO> binder;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PccDetailsTableDTO pccDetailsTableDTO;
	
	private String presenterString;
	
	private ComboBox cmbAssignedHRM;

	private ComboBox cmbProposedHRM;
	
	private FormLayout dummyForm;
	
	private TextField dummytext;
	
	private VerticalLayout mainVLayout;
	
	private VerticalLayout VLayout;
	
	private VerticalLayout proposedVLayout;
	
	/*private Button btnReassign;
	
	private Boolean reassign = false;*/
	
	private String assignedHRMempID ="";
	
	private BeanItemContainer<SelectValue> userDetailsContainer;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;

	@PostConstruct
	protected void initView() {

	}

	public void init(PccDetailsTableDTO pccDetailsTableDTO, String presenterString) {
		
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
		
		zonalMedicalDetailsTable = zonalMedicalDetailsTableInst.get();
		zonalMedicalDetailsTable.init("", false, false);
		zonalMedicalDetailsTable.setCaption("Zonal Medical Details");
		if(pccDetailsTableDTO !=null){
			zonalMedicalDetailsTable.setTableList(pccDetailsTableDTO.getZonalDetails());
		}
		
		cmbAssignedHRM = binder.buildAndBind("Actual Assigned HRM Coordinator","assginedHRMCoordinator",ComboBox.class);	
		cmbProposedHRM = binder.buildAndBind("Proposed HRM Coordinator","proposedHRMCoordinator",ComboBox.class);
		
        //FormLayout reassignBtnLayout=new FormLayout(btnReassign);
        
		//btnReassign=new Button("Reassign");
			
		/*HorizontalLayout reassignBtnLayout=new HorizontalLayout(btnReassign);
		reassignBtnLayout.setSpacing(true);
		reassignBtnLayout.setMargin(false);*/
        
		FormLayout  VLayout=new FormLayout(cmbAssignedHRM,cmbProposedHRM);
		HorizontalLayout hLayout = new HorizontalLayout(VLayout);
       // proposedVLayout=new VerticalLayout(cmbSelectUserName);
        
        //dyanamicVLayout = new VerticalLayout();
		//dyanamicVLayout.setVisible(false);
		
		mainVLayout = new VerticalLayout(processingDoctorDetailsTable,zonalMedicalDetailsTable,dummytext,VLayout);
		//mainVLayout.setComponentAlignment(reassignBtnLayout, Alignment.BOTTOM_CENTER);
		mainVLayout.setComponentAlignment(VLayout, Alignment.BOTTOM_LEFT);

		addListener();
		setAssignedHRM();
		getProposedHRM();
		setCompositionRoot(mainVLayout);

	}


	@SuppressWarnings("deprecation")
	public void addListener() {	
		
		/*cmbAssignedHRM.addListener(new Listener(){
			@Override
			public void componentEvent(Event event) {
				if (cmbAssignedHRM.getValue() !=null) {
					SelectValue selectValue = (SelectValue)cmbAssignedHRM.getValue();
					if(selectValue.getCommonValue() !=null){
						//SelectValue hrmName =pccRequestService.getPCCUserNames(selectValue.getCommonValue());
						fireViewEvent(ProcessPCCReassignHrmCoordinatorRequestPresenter.REASSIGN_HRM_COORDINATOR_GENERATE_USER_DETAILS,selectValue.getCommonValue());
					}
				}
			}			
		});		*/
		
	/*	btnReassign.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessPccCoOrdinateRequestPresenter.PCCCOORDINATE_GENERATE_APPROVE_LAYOUT,null);
			}
		});*/
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

	public Boolean validatePage() {

		Boolean hasError=false;
		StringBuffer eMsg = new StringBuffer(); 
		
		if(cmbProposedHRM != null && cmbProposedHRM.getValue() == null){			
			hasError=true;
			eMsg.append("Please select the proposed HRMCoordinator to proceed further </br> ");
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

	@SuppressWarnings("deprecation")
	public PccDTO getvalues() {

		PccDTO pccDTO = binder.getItemDataSource().getBean();
		pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
		if(cmbProposedHRM.getValue() !=null){
			SelectValue selectedValue=(SelectValue)cmbProposedHRM.getValue();
			System.out.println(String.format("proposed HRM Value [%s]", selectedValue.getCommonValue()));
			System.out.println(String.format("proposed HRM Value [%s]",selectedValue.getId()));
			System.out.println(String.format("proposed HRM Value [%s]",selectedValue.getValue()));
			
			pccDTO.setAssginedHRMCoordinator(selectedValue);
		}
		return pccDTO;

	}

	
	
	private void addAssignedHRM(BeanItemContainer<SelectValue> listAssignedHRM,SelectValue selectedAssignedHRM) {

		unbindField(cmbAssignedHRM);
		cmbAssignedHRM.setContainerDataSource(listAssignedHRM);
		cmbAssignedHRM.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAssignedHRM.setItemCaptionPropertyId("value");
		cmbAssignedHRM.setEnabled(false);
		
		if(selectedAssignedHRM !=null){
			cmbAssignedHRM.setValue(selectedAssignedHRM);
		}
		
	}

	
	public void addProposedHRMList(BeanItemContainer<SelectValue> userDetailsContainer) {

		unbindField(cmbProposedHRM);
		this.userDetailsContainer = userDetailsContainer;
		cmbProposedHRM.setContainerDataSource(this.userDetailsContainer);
		cmbProposedHRM.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProposedHRM.setItemCaptionPropertyId("value");
	}

	private void setAssignedHRM(){
		
		Long pccKey= pccDetailsTableDTO.getPccKey();
		List<Long> statuskeyList = new ArrayList<Long>();
		statuskeyList.add(SHAConstants.PCC_HRMC_ASSIGNED_STATUS);
		System.out.println(String.format("pccKey [%s]", pccKey));
		
		List<PCCQuery> pccQueryObj = pccRequestService.getPCCQueryBykeyandAssignedRole(pccKey,statuskeyList,SHAConstants.HRM_COORDINATOR_ROLE);
		PCCQuery pccQueryOne= pccQueryObj.get(0);
		String empName = "";
		if(pccQueryOne!=null){
			String empID =  pccQueryOne.getUserAssigned();
			if(empID !=null && !empID.isEmpty()){
				assignedHRMempID = empID;
				TmpEmployee employee= pccRequestService.getEmployeeByID(empID);
				
				if(employee!=null){
					 empName = employee.getEmpId()+"-"+employee.getEmpFirstName();
				}
				
			}
		}
		SelectValue assignedHRMName =  new SelectValue(empName);
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  listAssignedHRMs= new ArrayList<SelectValue>();
		listAssignedHRMs.add(assignedHRMName);
		selectValueContainer.addAll(listAssignedHRMs);
		addAssignedHRM(selectValueContainer,assignedHRMName);
	}
	
	private void getProposedHRM(){
		   
		Long pccKey = pccDetailsTableDTO.getPccKey();
		PCCRequest pccRequest =pccRequestService.getPCCRequestByKey(pccKey);
		Long cpuCode = 0L;
		if(pccRequest!=null){
			TmpCPUCode cpuCodeObject=  pccRequest.getIntimation().getCpuCode();
			if(cpuCodeObject !=null){
				 cpuCode= cpuCodeObject.getCpuCode();
				System.out.println(String.format("Cpu Code in process Page [%s]", cpuCode));
			}
		}
		  BeanItemContainer<SelectValue> proposedHRMUserNames = pccRequestService.getPCCUserNamesBasedOnCPUCode(SHAConstants.HRM_COORDINATOR_ROLE,cpuCode);
		  //BeanItemContainer<SelectValue> proposedHRMUserNames = pccRequestService.getPCCUserNames(SHAConstants.HRM_COORDINATOR_ROLE);
		  if(proposedHRMUserNames !=null){
			  
			  List<SelectValue> proposedHRMList = proposedHRMUserNames.getItemIds();
			  List<SelectValue> finalProposedHRMList = new ArrayList<SelectValue>();
			  
			  for (SelectValue oneproposedHRM : proposedHRMList) {
				   
				  System.out.println(String.format("Proposed HRM LIST[%s] ",oneproposedHRM.getValue()));
				  if(assignedHRMempID.equalsIgnoreCase(oneproposedHRM.getCommonValue())){
					  System.out.println(String.format("Skipping entity [%s] ",oneproposedHRM.getValue()));

				  }else{
					  finalProposedHRMList.add(oneproposedHRM);
				  }
			  }
			  proposedHRMUserNames.removeAllItems();
			  proposedHRMUserNames.addAll(finalProposedHRMList);
			  addProposedHRMList(proposedHRMUserNames);
		  }
		 
		  
		  
	}
	
}
