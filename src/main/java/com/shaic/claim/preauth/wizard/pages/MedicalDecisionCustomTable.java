package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalDecisionCustomTable extends ViewComponent {

	private static final long serialVersionUID = -8489074235987873623L;
	
	public BeanFieldGroup<PreauthMedicalProcessingDTO> binder;
	
	//private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	//private PreauthDTO bean;
	
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView() {
		Table table = new Table();
		BeanItemContainer<ProcedureDTO> procedureDetails = new BeanItemContainer<ProcedureDTO>(ProcedureDTO.class);
		java.util.List<ProcedureDTO> dtoList = new ArrayList<ProcedureDTO>();
		for(int i=0; i<2; i++) {
			ProcedureDTO dto = new ProcedureDTO();
			dto.setProcedureNameValue("dummy" + i);
			dto.setProcedureCodeValue("dummy" + i);
			dtoList.add(dto);
			
		}
		
		procedureDetails.addAll(dtoList);
		table.setContainerDataSource(procedureDetails);
		
		Object[] columns = new Object[]{"procedureNameValue", "procedureCodeValue"};
		table.setVisibleColumns(columns);
		
		Table table1 = new Table();
		BeanItemContainer<ProcedureDTO> procedureDetails1= new BeanItemContainer<ProcedureDTO>(ProcedureDTO.class);
		java.util.List<ProcedureDTO> dtoList1 = new ArrayList<ProcedureDTO>();
		for(int i=0; i<2; i++) {
			ProcedureDTO dto = new ProcedureDTO();
			dto.setProcedureNameValue("dummy" + i);
			dto.setProcedureCodeValue("dummy" + i);
			dtoList1.add(dto);
			
		}
		
		procedureDetails.addAll(dtoList1);
		table1.setContainerDataSource(procedureDetails1);
		
		Object[] columns1 = new Object[]{"procedureNameValue", "procedureCodeValue"};
		table1.setVisibleColumns(columns1);
		
		table1.setStyleName(ValoTheme.TABLE_NO_HEADER);
		
		setCompositionRoot(new VerticalLayout(table, table1));
		
		
	}
	

}
