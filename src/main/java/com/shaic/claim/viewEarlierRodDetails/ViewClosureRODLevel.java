package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.CloseClaim;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewClosureRODLevel extends ViewComponent{

	private TextField intimationNumber;
	
	private TextField rodNumber;
	
	private HorizontalLayout horizontalLayout;
	
	private VerticalLayout mainLayout;
	
	private Panel mainPanel;
	
	private FormLayout formlayoutRight;
	
	private FormLayout formlayoutleft;
	
	@Inject
	private Instance<ViewClosureRODLevelTable> closureTableInstance;
	
	private ViewClosureRODLevelTable closureTable;
	
	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgmentService;
	
	private void buildLayout(){
		
		Panel buildMainLayout = buildMainLayout();
		setCompositionRoot(buildMainLayout);
		
	}
	
	public void bindFieldGroup(Intimation intimation, Long rodKey){
		
		buildLayout();
		
		intimationNumber.setValue(intimation.getIntimationId() != null ? intimation.getIntimationId() : "");
		intimationNumber.setNullRepresentation("");
		
		Reimbursement reimbursement = acknowledgmentService.getReimbursementByKey(rodKey);
		
		if(reimbursement != null){
			rodNumber.setValue(reimbursement.getRodNumber() != null ? reimbursement.getRodNumber() : "");
		}
		rodNumber.setNullRepresentation("");
		
		setReadOnly(formlayoutleft);
		setReadOnly(formlayoutRight);
		
		buildTableLayout(reimbursement);
	}
	
	
	private Panel buildMainLayout(){
		
		closureTable = closureTableInstance.get();
		closureTable.init("", false, false);
		
		intimationNumber = new TextField("Intimation No");
		rodNumber = new TextField("ROD No");
		
		formlayoutleft = new FormLayout(intimationNumber);
		formlayoutleft.addStyleName("layoutDesign");
		formlayoutleft.setWidth("100.0%");
		formlayoutleft.setMargin(false);
		formlayoutleft.setSpacing(true);
//		formlayoutleft.setReadOnly(false);
		
		formlayoutRight = new FormLayout(rodNumber);
		formlayoutRight.addStyleName("layoutDesign");
		formlayoutRight.setWidth("100.0%");
		formlayoutRight.setMargin(false);
		formlayoutRight.setSpacing(true);
//		formlayoutRight.setReadOnly(false);
		
		horizontalLayout = new HorizontalLayout(formlayoutleft, formlayoutRight);
		horizontalLayout.setWidth("100.0%");
		horizontalLayout.setHeight("100.0%");
		
		mainLayout = new VerticalLayout(horizontalLayout, closureTable);
		
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		mainPanel = new Panel();
		mainPanel.setSizeFull();
		mainPanel.setContent(mainLayout);
		
		return mainPanel;
	}
	
	private void buildTableLayout(Reimbursement reimbursement) {

		List<ViewClosureDto> dtoList = new ArrayList<ViewClosureDto>();

		if (reimbursement != null) {
			List<CloseClaim> list = acknowledgmentService
					.getCloseClaimByReimbursementKeyAndType(reimbursement
							.getKey());
			if (list != null) {
				int no = 1;
				for (CloseClaim closeClaim : list) {
					ViewClosureDto dto = new ViewClosureDto();
					dto.setsNo(no);
					dto.setClosedDate(SHAUtils.formatDate(closeClaim
							.getClosedDate()));
					dto.setClosedBy(closeClaim.getModifiedBy());
					dto.setReasonForClosure(closeClaim.getClosingReasonId()
							.getValue());
					dtoList.add(dto);
					no++;
				}
			}
		}
		if (dtoList != null && !dtoList.isEmpty()) {
			closureTable.setTableList(dtoList);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				TextField field = (TextField) c;
				field.setWidth("440px");
				field.setReadOnly(true);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
}
