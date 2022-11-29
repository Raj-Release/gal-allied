package com.shaic.paclaim.cashless.downsize.wizard;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthPageDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PADownsizePreauthPage extends ViewComponent implements
WizardStep<PAWithdrawPreauthPageDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<RevisedMedicalDecisionTable> diagnosisProcedureListenerTableInstance;
	
	private RevisedMedicalDecisionTable diagnosisProcedureTableObj;
	
	@Inject 
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	private AmountConsideredTable amountConsideredTable;
	
	@Inject
	private Instance<AmountConsideredTable> balanceSumInsuredTableInstance;
	
	private AmountConsideredTable balanceSumInsuredTableObj;
	
	@Inject
	private PAWithdrawPreauthPageDTO bean;
	
	@Inject
	private PreauthDTO preauthDto;
	
	private BeanFieldGroup<PAWithdrawPreauthPageDTO> binder;
	
	private VerticalLayout wholeVLayout;
	
	@Override
	public String getCaption() {
		return "DownSize preauth";
	}
	
	@PostConstruct
	public void init() {

	}

	@Override
	public void init(PAWithdrawPreauthPageDTO bean) {
		this.bean=bean;
		this.preauthDto=this.bean.getPreauthDto();
		
		
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PAWithdrawPreauthPageDTO>(
				PAWithdrawPreauthPageDTO.class);
		this.binder.setItemDataSource(this.bean);
	}

	@Override
	public Component getContent() {
		initBinder();
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.diagnosisProcedureTableObj=this.diagnosisProcedureListenerTableInstance.get();
		
		this.diagnosisProcedureTableObj.init(this.preauthDto);
		
		DiagnosisProcedureTableDTO diagnosisProcedureTableDTO=new DiagnosisProcedureTableDTO();
		diagnosisProcedureTableDTO.setDiagOrProcedure("Diag 1");
		diagnosisProcedureTableDTO.setDescription("Viral Fever");
		diagnosisProcedureTableDTO.setPedOrExclusion("Not Related - NA");
//		diagnosisProcedureTableDTO.setPackageAmt(0);
//		diagnosisProcedureTableDTO.setRestrictionSI(10000);
		diagnosisProcedureTableDTO.setUtilizedAmt(95000);
		diagnosisProcedureTableDTO.setAvailableAmout(5000);
		SelectValue coPay = new SelectValue();
		coPay.setId(12l);
		coPay.setValue("30");
		diagnosisProcedureTableDTO.setCoPayPercentage(coPay);
//		diagnosisProcedureTableDTO.setSubLimitAmount(50000);
		diagnosisProcedureTableDTO.setSubLimitAvaliableAmt(2500);
		diagnosisProcedureTableDTO.setSubLimitUtilAmount(47500);
		
		this.diagnosisProcedureTableObj.addBeanToList(diagnosisProcedureTableDTO);
		
		
		this.amountConsideredTable=this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initView(this.preauthDto,false);
		
		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		
		VerticalLayout lLayout = new VerticalLayout();
		lLayout.addComponent(amtClaimedDetailsLbl);
		lLayout.addComponent(amountConsideredTable);
		lLayout.setMargin(true);
		
		this.balanceSumInsuredTableObj=this.amountConsideredTableInstance.get();
		this.balanceSumInsuredTableObj.initView(this.preauthDto,true);
		
		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");
		
		VerticalLayout sumInsuredLayout = new VerticalLayout();
		sumInsuredLayout.addComponent(balanceSumInsuredLbl);
		sumInsuredLayout.addComponent(balanceSumInsuredTableObj);
		sumInsuredLayout.setMargin(true);
		
		HorizontalLayout horizontalTable=new HorizontalLayout(lLayout,sumInsuredLayout);
		horizontalTable.setSpacing(true);
		
		wholeVLayout = new VerticalLayout(diagnosisProcedureTableObj,horizontalTable);
		wholeVLayout.setSpacing(true);
		
		return wholeVLayout;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
