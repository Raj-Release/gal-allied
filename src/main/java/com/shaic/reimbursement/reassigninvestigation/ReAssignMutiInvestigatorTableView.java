package com.shaic.reimbursement.reassigninvestigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.TmpInvestigation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class ReAssignMutiInvestigatorTableView extends GBaseTable<AssignInvestigatorDto> {

	@EJB
	private InvestigationService invService;
	
	@Override
	public void removeRow() {
		
	}

	public void initView(AssignInvestigatorDto assignDto){
		
		if(assignDto != null){
			List<AssignInvestigatorDto> assignedListDto = new ArrayList<AssignInvestigatorDto>();
			assignedListDto.add(assignDto);
			
//			AssignedInvestigatiorDetails assignedObj =  this.invService.getAssignedInvestigByKey(invassignKey);
//			if(assignedObj != null){
//					AssignInvestigatorDto assignDto = new AssignInvestigatorDto();
//					TmpInvestigation tmpInv = assignedObj.getInvestigatorCode() != null ? this.invService.getTmpInvestigationByInvestigatorCode(assignedObj.getInvestigatorCode()) : null;
//					assignDto.setStateSelectValue(assignedObj.getState() != null ? new SelectValue(assignedObj.getState().getKey(), assignedObj.getState().getValue()): null);
//					assignDto.setCitySelectValue(assignedObj.getCity() != null ? new SelectValue(assignedObj.getCity().getKey(), assignedObj.getCity().getValue()): null);
//					assignDto.setAllocationToSelectValue(assignedObj.getAllocationTo() != null ? new SelectValue(assignedObj.getAllocationTo().getKey(),assignedObj.getAllocationTo().getValue()) : null);
//					assignDto.setAllocationToValue(assignedObj.getAllocationTo() != null ? assignedObj.getAllocationTo().getValue() : "");
//					assignDto.setAllocationToId(assignedObj.getAllocationTo() != null && assignedObj.getAllocationTo().getKey() != null ? assignedObj.getAllocationTo().getKey() : null);
//					assignDto.setInvestigatorCode(assignedObj.getInvestigatorCode() != null ? assignedObj.getInvestigatorCode() : "");
//					assignDto.setInvestigatorNameListSelectValue(new SelectValue(1l, assignedObj.getInvestigatorName()));
//					assignDto.setInvestigatorName(assignedObj.getInvestigatorName() != null ? assignedObj.getInvestigatorName() : "");
//					assignDto.setInvestigatorTelNo(tmpInv != null && tmpInv.getPhoneNumber() != null && tmpInv.getPhoneNumber().intValue() != 0 ? String.valueOf(tmpInv.getPhoneNumber()): "");
//					assignDto.setInvestigatorMobileNo(tmpInv != null && tmpInv.getMobileNumber() != null && tmpInv.getMobileNumber().intValue() != 0 ? String.valueOf(tmpInv.getMobileNumber()) : "");
//					assignedListDto.add(assignDto);
//				
//			}

			setTableList(assignedListDto);
		}
	}
	
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<AssignInvestigatorDto>(AssignInvestigatorDto.class));
		
		table.setVisibleColumns(new Object[] {
				"serialNumber", "stateSelectValue", "citySelectValue", "allocationToSelectValue", "zoneName", "coordinatorName", "investigatorNameListSelectValue", "investigatorTelNo", "investigatorMobileNo"});
		
		table.setColumnWidth("serialNumber",45);
		table.setColumnWidth("stateSelectValue", 180);
		table.setColumnWidth("citySelectValue", 180);
		table.setColumnWidth("allocationToSelectValue", 85);
		table.setColumnWidth("investigatorNameListSelectValue", 320);
		table.setColumnWidth("investigatorTelNo", 158);
		table.setColumnWidth("investigatorMobileNo", 148);
		table.setColumnWidth("zoneName", 120);
		table.setColumnWidth("coordinatorName", 320);
//		table.setHeight("180px");
		table.setPageLength(table.getContainerDataSource() != null && !table.getContainerDataSource().getItemIds().isEmpty() ? table.getContainerDataSource().getItemIds().size() : 1);
		table.removeGeneratedColumn("reassign");
		table.addGeneratedColumn("reassign",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button reassignBtn = new Button("Re-assign Investigation");
						reassignBtn.setData(itemId);
						
						reassignBtn
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										AssignInvestigatorDto assignedDto = (AssignInvestigatorDto) reassignBtn.getData();
										
										if(assignedDto.isReassignAllowed()){
											assignedDto.setReassignedFrom(assignedDto.getInvestigatorNameListSelectValue() != null ? assignedDto.getInvestigatorNameListSelectValue().getValue() : "");
											assignedDto.setReassignedDate(new Date());
											assignedDto.setReassignAllowed(false);
											assignedDto.setUserName(String.valueOf(VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID)));
											fireViewEvent(ReAssignInvestigatorPresenter.SET_REASSIGN_EDIT_TABLE, assignedDto, null);
										}
									}
					    });
						reassignBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						return reassignBtn;
					}
				});
		
		
	}

	@Override
	public void tableSelectHandler(AssignInvestigatorDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-Multi-Investigator-";
	}
	
	
}





