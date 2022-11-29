package com.shaic.reimbursement.assigninvesigation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasInvZone;
import com.shaic.domain.MasterService;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AssignMutiInvestigatorTableView extends GBaseTable<AssignInvestigatorDto> {

	@EJB
	private InvestigationService invService;
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private MasterService masterService;
	
	@Override
	public void removeRow() {
		
	}

	public void initView(Long invassignKey){
		
		if(invassignKey != null){
			AssignedInvestigatiorDetails assignedObj =  this.invService.getAssignedInvestigByKey(invassignKey);
			List<AssignInvestigatorDto> assignedListDto = new ArrayList<AssignInvestigatorDto>();
			
			if(assignedObj != null){
					AssignInvestigatorDto assignDto = new AssignInvestigatorDto();
					if(assignedObj.getZoneCode() != null &&  !(assignedObj.getInvestigatorCode().contains("INV"))){
						Long privateInvKey = Long.valueOf(assignedObj.getInvestigatorCode());
						MasPrivateInvestigator privateInvestigation = this.invService.getPrivateInvestigatorByKey(privateInvKey);
						assignDto.setInvestigatorTelNo(privateInvestigation != null && privateInvestigation.getMobileNumberTwo() != null && privateInvestigation.getMobileNumberTwo().intValue() != 0 ? String.valueOf(privateInvestigation.getMobileNumberTwo()): "");
						assignDto.setInvestigatorMobileNo(privateInvestigation != null && privateInvestigation.getMobileNumberOne() != null && privateInvestigation.getMobileNumberOne().intValue() != 0 ? String.valueOf(privateInvestigation.getMobileNumberOne()) : "");
						assignDto.setZoneName(privateInvestigation != null && privateInvestigation.getZoneName() != null ? privateInvestigation.getZoneName() : "");
						assignDto.setCoordinatorName(privateInvestigation != null && privateInvestigation.getCordinatorName() != null ? privateInvestigation.getCordinatorName() : "");
					} else {
						TmpInvestigation tmpInv = assignedObj.getInvestigatorCode() != null ? this.invService.getTmpInvestigationByInactiveInvestigatorCode(assignedObj.getInvestigatorCode()) : null;
						assignDto.setInvestigatorTelNo(tmpInv != null && tmpInv.getPhoneNumber() != null && tmpInv.getPhoneNumber().intValue() != 0 ? String.valueOf(tmpInv.getPhoneNumber()): "");
						assignDto.setInvestigatorMobileNo(tmpInv != null && tmpInv.getMobileNumber() != null && tmpInv.getMobileNumber().intValue() != 0 ? String.valueOf(tmpInv.getMobileNumber()) : "");
						assignDto.setInvestigatorCode(assignedObj.getInvestigatorCode() != null ? assignedObj.getInvestigatorCode() : "");
					}
					assignDto.setStateSelectValue(assignedObj.getState() != null ? new SelectValue(assignedObj.getState().getKey(), assignedObj.getState().getValue()): null);
					assignDto.setCitySelectValue(assignedObj.getCity() != null ? new SelectValue(assignedObj.getCity().getKey(), assignedObj.getCity().getValue()): null);
					assignDto.setAllocationToSelectValue(assignedObj.getAllocationTo() != null ? new SelectValue(assignedObj.getAllocationTo().getKey(),assignedObj.getAllocationTo().getValue()) : null);
					assignDto.setAllocationToValue(assignedObj.getAllocationTo() != null ? assignedObj.getAllocationTo().getValue() : "");
					assignDto.setAllocationToId(assignedObj.getAllocationTo() != null && assignedObj.getAllocationTo().getKey() != null ? assignedObj.getAllocationTo().getKey() : null);
					
					if(assignedObj.getZoneCode() != null){
						if(assignedObj.getAllocationTo() != null && assignedObj.getAllocationTo().getValue()  != null
								&& !assignedObj.getAllocationTo().getValue() .isEmpty()
								&& assignedObj.getAllocationTo().getValue().equalsIgnoreCase("Private")){
							Long cpuCode = Long.valueOf(assignedObj.getZoneCode());
							MasInvZone masCpuCode = masterService.getMasInvZone(cpuCode);
							assignDto.setZoneName(masCpuCode.getZoneName());
							assignDto.setZoneSelectValue(new SelectValue(cpuCode,masCpuCode.getZoneName()));
						} else {
							Long cpuCode = Long.valueOf(assignedObj.getZoneCode());
							TmpCPUCode masCpuCode = createRodService.getMasCpuCode(cpuCode);
							assignDto.setZoneName(masCpuCode.getDescription());
							assignDto.setZoneSelectValue(new SelectValue(cpuCode,masCpuCode.getDescription()));
						}
					}
					
					assignDto.setInvestigatorNameListSelectValue(new SelectValue(1l, assignedObj.getInvestigatorName()));
					assignDto.setInvestigatorName(assignedObj.getInvestigatorName() != null ? assignedObj.getInvestigatorName() : "");
					/*assignDto.setInvestigatorTelNo(tmpInv != null && tmpInv.getPhoneNumber() != null && tmpInv.getPhoneNumber().intValue() != 0 ? String.valueOf(tmpInv.getPhoneNumber()): "");
					assignDto.setInvestigatorMobileNo(tmpInv != null && tmpInv.getMobileNumber() != null && tmpInv.getMobileNumber().intValue() != 0 ? String.valueOf(tmpInv.getMobileNumber()) : "");*/
					assignedListDto.add(assignDto);
				
			}
		setTableList(assignedListDto);
		}
	}
	
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<AssignInvestigatorDto>(AssignInvestigatorDto.class));
		
		table.setVisibleColumns(new Object[] {
				"serialNumber", "stateSelectValue", "citySelectValue", "allocationToSelectValue","zoneName","coordinatorName", "investigatorNameListSelectValue", "investigatorTelNo", "investigatorMobileNo"});
		
		table.setColumnWidth("serialNumber",45);
		table.setColumnWidth("stateSelectValue", 180);
		table.setColumnWidth("citySelectValue", 180);
		table.setColumnWidth("allocationToSelectValue", 85);
		table.setColumnWidth("zoneName", 180);
		table.setColumnWidth("coordinatorName", 280);
		table.setColumnWidth("investigatorNameListSelectValue", 320);
		table.setColumnWidth("investigatorTelNo", 158);
		table.setColumnWidth("investigatorMobileNo", 148);
		table.setHeight("180px");
		
	}

	@Override
	public void tableSelectHandler(AssignInvestigatorDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-Multi-Investigator-";
	}
	
	
}





