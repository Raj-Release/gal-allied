package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewPABillSummaryAdditionalBenefitsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewPABillSummaryBenefitsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewPABillSummaryOptionalCoverTable;
import com.shaic.domain.Product;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.Reimbursement;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoverOnLoadDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingService;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPABillSummaryPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<ViewPABillSummaryBenefitsTable> tableBenefitsListenerTable;
	
	@Inject
	private Instance<ViewPABillSummaryOptionalCoverTable> optionalCoversListenerTable;
	
	@Inject
	private Instance<ViewPABillSummaryAdditionalBenefitsTable> addOnCoversListenerTable;
	@Inject
	private ViewPABillSummaryBenefitsTable tableBenefitsListenerTableObj;
	@Inject
	private ViewPABillSummaryOptionalCoverTable optionalCoversListenerTableObj;
	@Inject
	private ViewPABillSummaryAdditionalBenefitsTable addOnCoversListenerTableObj;
	
	Panel mainPanel = new Panel();

	private VerticalLayout mainLayout;
	
	private Long rodKey;
	
	@EJB
	private PASearchProcessClaimBillingService paSearchProcessClaimBillingService;
	
	@EJB
	private CreateRODService rodService;
	
	public void init(Long rodKey)
	{
		this.rodKey = rodKey;
		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainPanel.setImmediate(false);
		mainPanel.setWidth("100.0%");
		mainPanel.setHeight("100.0%");
		Label label = new Label("View Bill Summary for Non Hospitalisation");
		label.setStyleName("labelDesign");
	
		VerticalLayout verticalMain = new VerticalLayout(label, buildLayout());
		mainPanel.setContent(verticalMain);
		setCompositionRoot(mainPanel);
		
		bindFieldGroup(this.rodKey);
	
	}
	
	public VerticalLayout buildLayout()
	{
		//tableBenefitsListenerTableObj = tableBenefitsListenerTable.get();
		tableBenefitsListenerTableObj.init("", false, false);
		tableBenefitsListenerTableObj.setCaption("A) Benefits");
		
		//optionalCoversListenerTableObj = optionalCoversListenerTable.get();
		optionalCoversListenerTableObj.init("", false, false);
		optionalCoversListenerTableObj.setCaption("B) Optional Covers");
		
		//addOnCoversListenerTableObj = addOnCoversListenerTable.get();
		addOnCoversListenerTableObj.init("", false, false);
		addOnCoversListenerTableObj.setCaption("C) Additional Benefits");
		
		mainLayout.addComponent(tableBenefitsListenerTableObj);
		mainLayout.addComponent(optionalCoversListenerTableObj);
		mainLayout.addComponent(addOnCoversListenerTableObj);
		
		mainLayout.setHeight("100%");
		mainLayout.setWidth("100%");
		mainLayout.setSizeFull();
		return mainLayout;
	}
	
	public void bindFieldGroup(Long rodKey)
	{
		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(rodKey);
		List<RODDocumentSummary> rodDocSummary = rodService.getRODSummaryDetailsByReimbursementKey(reimbursementObjectByKey.getKey());
		
		Product product = reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct();
		
		List<TableBenefitsDTO> paBenefitsListByRodKey = paSearchProcessClaimBillingService.getPABenefitsListByRodKey(reimbursementObjectByKey.getKey());
		Long sno = 1l;
		for(TableBenefitsDTO tableBenefit: paBenefitsListByRodKey)
		{
			String fileName = "";
			String fileType = "";
			Double consideredAmt = new Double(0);
			tableBenefit.setSlNo(sno);
			tableBenefit.setRodNo(reimbursementObjectByKey.getRodNumber());
			
			if(tableBenefit.getNetAmount() != null && tableBenefit.getEligibleAmount() != null){
				consideredAmt = Math.min(tableBenefit.getNetAmount(), tableBenefit.getEligibleAmount());
			}
			else if(tableBenefit.getEligibleAmount() != null){
				consideredAmt = tableBenefit.getEligibleAmount();
			}
			
			tableBenefit.setAmtConsidered(consideredAmt);
			
			if(rodDocSummary != null)
			{
				for(RODDocumentSummary docSummary: rodDocSummary)
				{
					if(docSummary.getFileName() != null)
					{
						fileName += docSummary.getFileName()+", ";
					}
					if(docSummary.getFileType() != null)
					{
						fileType +=  docSummary.getFileType().getValue()+", ";
					}
				}
				tableBenefit.setFileName(fileName);
				tableBenefit.setFileType(fileType);
			}
			
			sno++;
		}
		
		List<OptionalCoversDTO> paOptionalCoverListByRodKey = paSearchProcessClaimBillingService.getPAOptionalCoverListByRodKey(reimbursementObjectByKey);
		int index = 1;
		for(OptionalCoversDTO optionalCover : paOptionalCoverListByRodKey)
		{
			Double amtPerDayPayable = new Double(0);
			optionalCover.setsNo(index);
			
			if(optionalCover.getAmtOfClaimPaid() != null  && optionalCover.getAllowedAmountPerDay() != null){
				amtPerDayPayable = Math.min(optionalCover.getAmtOfClaimPaid(), optionalCover.getAllowedAmountPerDay());	
			}else if(optionalCover.getAllowedAmountPerDay() != null){
				amtPerDayPayable = optionalCover.getAllowedAmountPerDay();
			}
			optionalCover.setAmtPerDayPayable(amtPerDayPayable);
			index++;
		}

		List<AddOnCoverOnLoadDTO> coverNameList= new ArrayList<AddOnCoverOnLoadDTO>();
		List<AddOnCoversTableDTO> paAddOnCoverListByRodKey = paSearchProcessClaimBillingService.getAddOnCoverListByRodKey(reimbursementObjectByKey.getKey(), product.getKey(),coverNameList);

		index = 1;
		for(AddOnCoversTableDTO addOnCover: paAddOnCoverListByRodKey)
		{
			addOnCover.setsNo(index);
			Double netAmt = new Double(0);
			if(addOnCover.getBillAmount() != null && addOnCover.getDeduction() != null){
				netAmt = addOnCover.getBillAmount() - addOnCover.getDeduction();
			}else if(addOnCover.getBillAmount() != null){
				netAmt = addOnCover.getBillAmount();
			}
			
			addOnCover.setNetamount(netAmt);
			index++;
		}
		
		tableBenefitsListenerTableObj.setTableList(paBenefitsListByRodKey);
		optionalCoversListenerTableObj.setTableList(paOptionalCoverListByRodKey);
		addOnCoversListenerTableObj.setTableList(paAddOnCoverListByRodKey);
		
	}
	
	public void clearObjects(){
	 	
		if(this.mainLayout!=null){
			this.mainLayout.removeAllComponents();
			this.mainPanel = null;
		}
		
 }
	
}
