package com.shaic.claim.userreallocation;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class EditReallocationCountDetailsForm extends ViewComponent {
	
	private Button submitBtn;
	private Window countPopup;
	private Button cancelBtn;
	private SearchReallocationDoctorDetailsTableDTO bean;
	
	@Inject
	private Instance<AutoAllocationAssignedDetailsTable> autoAllocationAssignedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationCompletedDetailsTable> autoAllocationCompletedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationPendingDetailsTable> autoAllocationPendingDetailsTableInstance;
	
	public void init(SearchReallocationDoctorDetailsTableDTO bean, Window popUp, String countType, List<AutoAllocationDetailsTableDTO> assignedList, List<AutoAllocationDetailsTableDTO> completedList, List<AutoAllocationDetailsTableDTO> pendingList){
		
		this.bean = bean;
		this.countPopup = popUp;
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		submitBtn.setEnabled(false);
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		TabSheet sheet = countTab(countType, assignedList, completedList, pendingList);
		
		VerticalLayout verticalMain = new VerticalLayout(sheet, buttonHor);
		
		verticalMain.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		addListener();
		addTabListener(sheet);
		setCompositionRoot(verticalMain);
		
		//addListener();
	}
	
	public void addTabListener(TabSheet sheet){
		sheet
		.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				
				 TabSheet tabsheet = event.getTabSheet();
				 
				 String caption = tabsheet.getSelectedTab().getCaption();
				// TODO Auto-generated method stub
				if (caption != null && caption.equalsIgnoreCase("Pending"))
					submitBtn.setEnabled(true);
				else
					submitBtn.setEnabled(false);
			}
		});
	}
	
	private TabSheet countTab(String type, List<AutoAllocationDetailsTableDTO> assignedList, List<AutoAllocationDetailsTableDTO> completedList, List<AutoAllocationDetailsTableDTO> pendingList){
		TabSheet countTab = new TabSheet();
		//Vaadin8-setImmediate() countTab.setImmediate(true);
		
		
		countTab.setSizeFull();
		
		countTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		
		TabSheet assignedCountTab =  getAssignedCountTab(assignedList);
		
		countTab.addTab(assignedCountTab, "Assigned", null);
		

		// tabSheet_2
		TabSheet completedCountTab = getCompletedCountTab(completedList);
		
		countTab.addTab(completedCountTab, "Completed", null);

		// tabSheet_3
		TabSheet pendingCountTab = getPendingCountTab(pendingList);
		
		countTab.addTab(pendingCountTab, "Pending", null);
		
		if(type != null){
			if(type.equalsIgnoreCase(SHAConstants.REALLOCATION_TOTAL_ASSIGNED)){
				countTab.setSelectedTab(assignedCountTab);	
			}else if(type.equalsIgnoreCase(SHAConstants.REALLOCATION_COMPLETED)){
				countTab.setSelectedTab(completedCountTab);
			}else if(type.equalsIgnoreCase(SHAConstants.REALLOCATION_PENDING)){
				countTab.setSelectedTab(pendingCountTab);
				submitBtn.setEnabled(true);
			}else{
				countTab.setSelectedTab(assignedCountTab);
			}
		}else{
			countTab.setSelectedTab(assignedCountTab);
		}
		
		return countTab;
	}
	
	private TabSheet getAssignedCountTab(List<AutoAllocationDetailsTableDTO> assignedList) {

		TabSheet assignedTab = new TabSheet();
		assignedTab.hideTabs(true);
		//Vaadin8-setImmediate() assignedTab.setImmediate(true);
		assignedTab.setWidth("100%");
		assignedTab.setHeight("100%");
		assignedTab.setSizeFull();
		//Vaadin8-setImmediate() assignedTab.setImmediate(true);
		int sNo = 1;
		// assignedTab.addComponent();

		AutoAllocationAssignedDetailsTable autoAllocationDetailsTableObj1 = autoAllocationAssignedDetailsTableInstance.get();

		autoAllocationDetailsTableObj1.init("", false, false);
		
		if(assignedList != null && !assignedList.isEmpty()){
			
			for(AutoAllocationDetailsTableDTO detail : assignedList){/*
				AutoAllocationDetailsTableDTO tableDto = new AutoAllocationDetailsTableDTO();
				tableDto.setIntimationNo(detail.getIntimationNo());
				tableDto.setDoctorId(detail.getDoctorId());
				tableDto.setDoctorName(detail.getDoctorName());
				tableDto.setClaimedAmt(detail.getClaimedAmt());
				tableDto.setCpu(detail.getCpuCode());
				tableDto.setAssignedDate(detail.getAssignedDate() != null ? String.valueOf(detail.getAssignedDate()) : "");
				tableDto.setCompletedDate(detail.getCompletedDate() != null ? String.valueOf(detail.getCompletedDate()) : "");
				tableDto.setsNumber(sNo);*/
				autoAllocationDetailsTableObj1.addBeanToList(detail);
				//sNo++;
			}
		}
		
		assignedTab.addComponent(autoAllocationDetailsTableObj1);
		
		return assignedTab;
	}
	
	private TabSheet getCompletedCountTab(List<AutoAllocationDetailsTableDTO> completedList) {

		TabSheet completedTab = new TabSheet();
		completedTab.hideTabs(true);
		//Vaadin8-setImmediate() completedTab.setImmediate(true);
		completedTab.setWidth("100%");
		completedTab.setHeight("100%");
		completedTab.setSizeFull();
		//Vaadin8-setImmediate() completedTab.setImmediate(true);
		int sNo = 1;
		AutoAllocationCompletedDetailsTable autoAllocationDetailsTableObj2 = autoAllocationCompletedDetailsTableInstance
				.get();

		autoAllocationDetailsTableObj2.init("", false, false);
		
		if(completedList != null && !completedList.isEmpty()){
			
			for(AutoAllocationDetailsTableDTO detail : completedList){/*
				AutoAllocationDetailsTableDTO tableDto = new AutoAllocationDetailsTableDTO();
				tableDto.setIntimationNo(detail.getIntimationNo());
				tableDto.setDoctorId(detail.getDoctorId());
				tableDto.setDoctorName(detail.getDoctorName());
				tableDto.setClaimedAmt(detail.getClaimedAmt());
				tableDto.setCpu(detail.getCpuCode());
				tableDto.setAssignedDate(detail.getAssignedDate() != null ? String.valueOf(detail.getAssignedDate()) : "");
				tableDto.setCompletedDate(detail.getCompletedDate() != null ? String.valueOf(detail.getCompletedDate()) : "");
				tableDto.setsNumber(sNo);*/
				autoAllocationDetailsTableObj2.addBeanToList(detail);
				//sNo++;
			}
		}
		
		completedTab.addComponent(autoAllocationDetailsTableObj2);
		
		//completedTab.addComponent();

		return completedTab;
	}
	
	private TabSheet getPendingCountTab(List<AutoAllocationDetailsTableDTO> pendingList) {

		TabSheet pendingTab = new TabSheet();
		pendingTab.hideTabs(true);
		//Vaadin8-setImmediate() pendingTab.setImmediate(true);
		pendingTab.setWidth("100%");
		pendingTab.setHeight("100%");
		pendingTab.setSizeFull();
		//Vaadin8-setImmediate() pendingTab.setImmediate(true);
		pendingTab.setCaption("Pending");
		int sNo = 1;
		AutoAllocationPendingDetailsTable autoAllocationDetailsTableObj3 = autoAllocationPendingDetailsTableInstance
				.get();

		autoAllocationDetailsTableObj3.init("", false, false);
		
		
		if(pendingList != null && !pendingList.isEmpty()){
			
			for(AutoAllocationDetailsTableDTO detail : pendingList){/*
				AutoAllocationDetailsTableDTO tableDto = new AutoAllocationDetailsTableDTO();
				tableDto.setIntimationNo(detail.getIntimationNo());
				tableDto.setDoctorId(detail.getDoctorId());
				tableDto.setDoctorName(detail.getDoctorName());
				tableDto.setClaimedAmt(detail.getClaimedAmt());
				tableDto.setCpu(detail.getCpuCode());
				tableDto.setAssignedDate(detail.getAssignedDate() != null ? String.valueOf(detail.getAssignedDate()) : "");
				tableDto.setCompletedDate(detail.getCompletedDate() != null ? String.valueOf(detail.getCompletedDate()) : "");
				tableDto.setsNumber(sNo);*/
				autoAllocationDetailsTableObj3.addBeanToList(detail);
				//sNo++;
			}
		}
		
		pendingTab.addComponent(autoAllocationDetailsTableObj3);
		//pendingTab.addComponent();

		return pendingTab;
	}
	
	public void initIntimationTable(
			Instance<AutoAllocationAssignedDetailsTable> assignedInstance,
			Instance<AutoAllocationCompletedDetailsTable> completedInstance,
			Instance<AutoAllocationPendingDetailsTable> pendingInstance) {
		autoAllocationAssignedDetailsTableInstance = assignedInstance;
		autoAllocationCompletedDetailsTableInstance = completedInstance;
		autoAllocationPendingDetailsTableInstance = pendingInstance;
	}
	
	public void addListener() {
		submitBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (true) {
					// ();
					// view.popup.close();
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					setTableValuetoDTO();
					fireViewEvent(EditReallocationCountDetailsPresenter.SUBMIT_INTIMATION_DETAILS_RE_ALLOCATION, bean, userName);
					countPopup.close();
				}

			}
		});

		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// view.popup.close();
				countPopup.close();
			}
		});
	}
	
	public void setTableValuetoDTO() {

		AutoAllocationPendingDetailsTable autoAllocationDetailsTableObj = autoAllocationPendingDetailsTableInstance
				.get();

		if (autoAllocationDetailsTableObj != null) {
			List<AutoAllocationDetailsTableDTO> tableList = autoAllocationDetailsTableObj
					.getValues();
			this.bean.setIntimationDetailsList(tableList);
		}
	}
}
