package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.corpbuffer.allocation.search.AllocateCorpBufferService;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.CorporateBufferUtilisationTable;
import com.shaic.claim.viewEarlierRodDetails.CorporateBufferUtilisationTableDTO;
import com.shaic.claim.viewEarlierRodDetails.NacBufferUtilisationTable;
import com.shaic.claim.viewEarlierRodDetails.WintageBufferUtilisationTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewGmcCorporateBufferUtilisationPage extends ViewComponent{


	@Inject
	public CorporateBufferUtilisationTable corpbufferUtilisationTable;
	
	@Inject
	public WintageBufferUtilisationTable wintagebufferUtilisationTable;
	
	@Inject
	public NacBufferUtilisationTable nacbufferUtilisationTable;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private AllocateCorpBufferService bufferService;

	@EJB
	private CreateRODService createRodService;	
	
	private VerticalLayout mainVLayout;
	
	private VerticalLayout claimAllocatedLayout;
	

	public void init(Intimation intimation){

		Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey()); 		  

		HorizontalLayout gmcLayout = new HorizontalLayout();
		VerticalLayout gmcVLayout = new VerticalLayout();
		mainVLayout = new VerticalLayout();
		claimAllocatedLayout = new VerticalLayout();

		AllocateCorpBufferDetailDTO allocateCorpBufferDetailDTO = bufferService.getbufferViewDetails(intimation);
		
		
		Table discTable  = new Table();
		discTable.addContainerProperty("Particulars", Label.class, null);
		discTable.addContainerProperty("Amount",  Double.class, null);
		discTable.setNullSelectionAllowed(true);
		Label policyLabel = new Label("<div style='padding-left: 80px;'>" +"Policy Wise" + "</div>",ContentMode.HTML);
		Label disBuffSI = new Label("<b style='font-weight: normal !important;'>Discretionary Buffer SI </b>",ContentMode.HTML);
		Label disBuffUtil = new Label("<b style='font-weight: normal !important;'>Discretionary Buffer Utilised Amount",ContentMode.HTML);
		Label disAvlBal = new Label("<b style='font-weight: normal !important;'>Available Balance",ContentMode.HTML);
		Label insuredLabel = new Label("<b style='padding-left: 80px;'>" +"Insured Wise" + "</b>",ContentMode.HTML);
		Label disInsuAlloct = new Label("<b style='font-weight: normal !important;'>Discretionary Buffer Allocated for Employee",ContentMode.HTML);
		Label disInsUtil = new Label("<b style='font-weight: normal !important;'>Discretionary Buffer Utilised Amount",ContentMode.HTML);
		Label disInsAvl = new Label("<b style='font-weight: normal !important;'>Available Balance ",ContentMode.HTML);
		discTable.addItem(new Object[]{policyLabel, allocateCorpBufferDetailDTO.getPolicyWise() },1);
		discTable.addItem(new Object[]{disBuffSI, allocateCorpBufferDetailDTO.getDisBufferSI() },2);
		discTable.addItem(new Object[]{disBuffUtil, allocateCorpBufferDetailDTO.getDisBufferUtilizedAmt() },3);
		discTable.addItem(new Object[]{disAvlBal, allocateCorpBufferDetailDTO.getPolicywisedisBufferAvlBlnc() },4);
		discTable.addItem(new Object[]{insuredLabel, allocateCorpBufferDetailDTO.getPolicyWise() },5);
		discTable.addItem(new Object[]{disInsuAlloct, allocateCorpBufferDetailDTO.getDisAllocatedLimit() },6);
		discTable.addItem(new Object[]{disInsUtil, allocateCorpBufferDetailDTO.getDiscretionaryUtilizedInsured() },7);
		discTable.addItem(new Object[]{disInsAvl, allocateCorpBufferDetailDTO.getDisBufferAvailBalnc() },8);
		discTable.setPageLength(8);
		discTable.setCaption("Discretionary Buffer Details");
		discTable.setSizeFull();
		
		Table wintageTable  = new Table();
		wintageTable.addContainerProperty("Particulars", Label.class, null);
		wintageTable.addContainerProperty("Amount",  Double.class, null);
		wintageTable.setNullSelectionAllowed(true);
		Label winpolicyLabel = new Label("<div style='padding-left: 80px;'>" +"Policy Wise" + "</div>",ContentMode.HTML);
		Label winBuffSI = new Label("<b style='font-weight: normal !important;'>Vintage Buffer SI </b>",ContentMode.HTML);
		Label winBuffUtil = new Label("<b style='font-weight: normal !important;'>Vintage Buffer Utilised Amount",ContentMode.HTML);
		Label winAvlBal = new Label("<b style='font-weight: normal !important;'>Available Balance",ContentMode.HTML);
		Label wininsuredLabel = new Label("<b style='padding-left: 80px;'>" +"Insured Wise" + "</b>",ContentMode.HTML);
		Label winInsuAlloct = new Label("<b style='font-weight: normal !important;'>Vintage Buffer Allocated for Employee",ContentMode.HTML);
		Label winInsUtil = new Label("<b style='font-weight: normal !important;'>Vintage Buffer Utilised Amount",ContentMode.HTML);
		Label winInsAvl = new Label("<b style='font-weight: normal !important;'>Available Balance ",ContentMode.HTML);
		wintageTable.addItem(new Object[]{winpolicyLabel, allocateCorpBufferDetailDTO.getPolicyWise() },1);
		wintageTable.addItem(new Object[]{winBuffSI, allocateCorpBufferDetailDTO.getMaxwintageBufferLimit() },2);
		wintageTable.addItem(new Object[]{winBuffUtil, allocateCorpBufferDetailDTO.getWintageAllocatedLimit() },3);
		wintageTable.addItem(new Object[]{winAvlBal, allocateCorpBufferDetailDTO.getWintageBufferAvlBalnc() },4);
		wintageTable.addItem(new Object[]{wininsuredLabel, allocateCorpBufferDetailDTO.getPolicyWise() },5);
		wintageTable.addItem(new Object[]{winInsuAlloct, allocateCorpBufferDetailDTO.getWintageBufferLimit() },6);
		wintageTable.addItem(new Object[]{winInsUtil, allocateCorpBufferDetailDTO.getWintageUtilizedInsured() },7);
		wintageTable.addItem(new Object[]{winInsAvl, allocateCorpBufferDetailDTO.getWintageAvlBalnc() },8);
		wintageTable.setPageLength(8);
		wintageTable.setCaption("Vintage Buffer Details");
		wintageTable.setSizeFull();
		
		Table nacTable  = new Table();
		/*nacTable.setWidth("400px");
		nacTable.setHeight("88px");*/
		nacTable.addContainerProperty("Particulars", Label.class, null);
		nacTable.addContainerProperty("Amount",  Double.class, null);
		nacTable.setNullSelectionAllowed(true);
		Label nacbpolicyLabel = new Label("<div style='padding-left: 80px;'>" +"Policy Wise" + "</div>",ContentMode.HTML);
		Label nacBuffSI = new Label("<b style='font-weight: normal !important;'>NACB Buffer SI </b>",ContentMode.HTML);
		Label nacBuffUtil = new Label("<b style='font-weight: normal !important;'>NACB Buffer Utilised Amount",ContentMode.HTML);
		Label nacAvlBal = new Label("<b style='font-weight: normal !important;'>Available Balance",ContentMode.HTML);
		Label nacinsuredLabel = new Label("<b style='padding-left: 80px;'>" +"Insured Wise" + "</b>",ContentMode.HTML);
		Label nacInsuAlloct = new Label("<b style='font-weight: normal !important;'>NACB Buffer Allocated for Employee",ContentMode.HTML);
		Label nacInsUtil = new Label("<b style='font-weight: normal !important;'>NACB Buffer Utilised Amount",ContentMode.HTML);
		Label nacInsAvl = new Label("<b style='font-weight: normal !important;'>Available Balance ",ContentMode.HTML);
		nacTable.addItem(new Object[]{nacbpolicyLabel, allocateCorpBufferDetailDTO.getPolicyWise() },1);
		nacTable.addItem(new Object[]{nacBuffSI, allocateCorpBufferDetailDTO.getMaxnacBufferLimit() },2);
		nacTable.addItem(new Object[]{nacBuffUtil, allocateCorpBufferDetailDTO.getNacAllocatedLimit() },3);
		nacTable.addItem(new Object[]{nacAvlBal, allocateCorpBufferDetailDTO.getNacBufferAvlBalnc() },4);
		nacTable.addItem(new Object[]{nacinsuredLabel, allocateCorpBufferDetailDTO.getPolicyWise() },5);
		nacTable.addItem(new Object[]{nacInsuAlloct, allocateCorpBufferDetailDTO.getNacBufferLimit() },6);
		nacTable.addItem(new Object[]{nacInsUtil, allocateCorpBufferDetailDTO.getNacbUtilizedInsured() },7);
		nacTable.addItem(new Object[]{nacInsAvl, allocateCorpBufferDetailDTO.getNacAvlBalnc() },8);
		nacTable.setPageLength(8);
		nacTable.setCaption("NACB Buffer Details");
		nacTable.setSizeFull();
		
		HorizontalLayout tabelLayout = new HorizontalLayout(discTable,wintageTable,nacTable);
		tabelLayout.setSpacing(true);
		tabelLayout.setMargin(true);
		
		/*HorizontalLayout tabelLayout2 = new HorizontalLayout(nacTable);
		tabelLayout2.setSpacing(true);
		tabelLayout2.setMargin(true);*/
		
		corpbufferUtilisationTable.init("", false, false);	
		if(allocateCorpBufferDetailDTO.getIsDisBufferApplicable() != null && allocateCorpBufferDetailDTO.getIsDisBufferApplicable().equals(true)){
		List<CorporateBufferUtilisationTableDTO> corpbufferTableList = setCorporateUtilisedTableValues(claimforIntimation,allocateCorpBufferDetailDTO);
		if(corpbufferTableList !=null && !corpbufferTableList.isEmpty()){
			corpbufferUtilisationTable.setTableList(corpbufferTableList);
		}
	    }
		corpbufferUtilisationTable.setCaption("Discretionary Buffer Details");
		
		wintagebufferUtilisationTable.init("", false, false);	
		if(allocateCorpBufferDetailDTO.getIsWintageBufferApplicable() != null && allocateCorpBufferDetailDTO.getIsWintageBufferApplicable().equals(true)){
		List<CorporateBufferUtilisationTableDTO> wintageBufferTableList = setWintageBufferUtilisedTableValues(claimforIntimation,allocateCorpBufferDetailDTO);
		if(wintageBufferTableList !=null && !wintageBufferTableList.isEmpty()){
			wintagebufferUtilisationTable.setTableList(wintageBufferTableList);
		}
		}
			wintagebufferUtilisationTable.setCaption("Vintage Buffer Details");
		
		nacbufferUtilisationTable.init("", false, false);	
		if(allocateCorpBufferDetailDTO.getIsNacBufferApplicable() != null && allocateCorpBufferDetailDTO.getIsNacBufferApplicable().equals(true)){
		List<CorporateBufferUtilisationTableDTO> nacBufferTableList = setNacBufferUtilisedTableValues(claimforIntimation,allocateCorpBufferDetailDTO);
		if(nacBufferTableList !=null && !nacBufferTableList.isEmpty()){
			nacbufferUtilisationTable.setTableList(nacBufferTableList);
		}
		}
			nacbufferUtilisationTable.setCaption("NACB Buffer Details");
			
		
			claimAllocatedLayout.addComponents(tabelLayout,corpbufferUtilisationTable,wintagebufferUtilisationTable,nacbufferUtilisationTable);
		
//		claimAllocatedLayout.addComponents(tabelLayout,corpbufferUtilisationTable,wintagebufferUtilisationTable,nacbufferUtilisationTable);
		claimAllocatedLayout.setSpacing(true);
		claimAllocatedLayout.setMargin(true);


		Panel mainPanel = new Panel(claimAllocatedLayout);
		mainPanel.addStyleName("girdBorder");
		
		mainVLayout = new VerticalLayout(mainPanel);
		setCompositionRoot(mainVLayout);

	}	

	private List<CorporateBufferUtilisationTableDTO> setCorporateUtilisedTableValues(Claim claim,AllocateCorpBufferDetailDTO  allocateCorpBufferDetailDTO)
	{

		Double reimbUtilisedAmnt = 0d;
		Double preauthUtilisedAmnt = 0d;  	
		 Double availableBalance =0d;

		List<CorporateBufferUtilisationTableDTO> corpbufferTableList = new ArrayList<CorporateBufferUtilisationTableDTO>();

		List<Preauth> preauthList = null;
		if(claim != null){
			preauthList = createRodService.getPreauthListByClaimKey(claim.getKey());
		}else{
			preauthList = new ArrayList<Preauth>();
		}

		Preauth preauthObj = new Preauth();


		if(null != preauthList && !preauthList.isEmpty()){

			for (Preauth preauth : preauthList) {
				if(preauth.getCorporateUtilizedAmt() != null){
					preauthObj = preauth;
					break;
				}
			}
			//preauthObj = preauthList.get(0);

			for (Preauth preauth : preauthList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto = new CorporateBufferUtilisationTableDTO();

				corpBufferTableDto.setTypeOfClaim(preauth.getClaim().getClaimType().getValue());
				corpBufferTableDto.setReferenceOrRodNo(preauth.getPreauthId());
				corpBufferTableDto.setStatus(preauth.getStatus().getProcessValue());
				if(null != preauth.getCorporateUtilizedAmt()){
					corpBufferTableDto.setCorpBufferUtilisation(Double.valueOf(preauth.getCorporateUtilizedAmt()));
				}

				if(preauth.getCorporateUtilizedAmt() != null && preauth.getCorporateUtilizedAmt() > 0){
	            	 availableBalance = allocateCorpBufferDetailDTO.getDisAvlBalnc() - preauth.getCorporateUtilizedAmt();
	             }else {
	            	 availableBalance = allocateCorpBufferDetailDTO.getDisAvlBalnc();
	             }
					corpBufferTableDto.setCorpBufferRemainingforClaim(availableBalance);

				corpbufferTableList.add(corpBufferTableDto);				 
			}	
		}

		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(claim != null){
			reimList = createRodService.getReimbursementByClaimKey(claim.getKey());
		}


		if(null != reimList && !reimList.isEmpty()){				 

			for (Reimbursement reimbursement : reimList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto1 = new CorporateBufferUtilisationTableDTO();
				corpBufferTableDto1.setTypeOfClaim(reimbursement.getClaim().getClaimType().getValue());
				corpBufferTableDto1.setReferenceOrRodNo(reimbursement.getRodNumber());
				corpBufferTableDto1.setStatus(reimbursement.getStatus().getProcessValue());
				if(null != reimbursement.getCorporateUtilizedAmt()){
					corpBufferTableDto1.setCorpBufferUtilisation(Double.valueOf(reimbursement.getCorporateUtilizedAmt()));
				}

				if(reimbursement.getCorporateUtilizedAmt() != null && reimbursement.getCorporateUtilizedAmt() > 0){
	            	 availableBalance = allocateCorpBufferDetailDTO.getDisAvlBalnc() - reimbursement.getCorporateUtilizedAmt();
	             }else {
	            	 availableBalance = allocateCorpBufferDetailDTO.getDisAvlBalnc();
	             }
				corpBufferTableDto1.setCorpBufferRemainingforClaim(availableBalance);


				corpbufferTableList.add(corpBufferTableDto1);

				if(null != reimbursement.getCorporateUtilizedAmt()){
					reimbUtilisedAmnt += reimbursement.getCorporateUtilizedAmt();
				}
			}

		}


		if(null != preauthObj && null != preauthObj.getCorporateUtilizedAmt()){
			preauthUtilisedAmnt += preauthObj.getCorporateUtilizedAmt();
		}			 


//		if(null != txtCorpBufferUtilisedforCurrentClaim){
//
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(false);
//			if(!preauthUtilisedAmnt.equals(0d) && !reimbUtilisedAmnt.equals(0d)){
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(reimbUtilisedAmnt));
//			}else{
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(preauthUtilisedAmnt + reimbUtilisedAmnt));
//			}
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(true);
//		}


		return corpbufferTableList;
	}
	
	private List<CorporateBufferUtilisationTableDTO> setWintageBufferUtilisedTableValues(Claim claim,AllocateCorpBufferDetailDTO  allocateCorpBufferDetailDTO)
	{

		Double reimbUtilisedAmnt = 0d;
		Double preauthUtilisedAmnt = 0d;  	
		 Double availableBalance =0d;


		List<CorporateBufferUtilisationTableDTO> corpbufferTableList = new ArrayList<CorporateBufferUtilisationTableDTO>();

		List<Preauth> preauthList = null;
		if(claim != null){
			preauthList = createRodService.getPreauthListByClaimKey(claim.getKey());
		}else{
			preauthList = new ArrayList<Preauth>();
		}

		Preauth preauthObj = new Preauth();


		if(null != preauthList && !preauthList.isEmpty()){

			for (Preauth preauth : preauthList) {
				if(preauth.getWintageBufferUtilAmt() != null){
					preauthObj = preauth;
					break;
				}
			}
			//preauthObj = preauthList.get(0);

			for (Preauth preauth : preauthList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto = new CorporateBufferUtilisationTableDTO();
				corpBufferTableDto.setTypeOfClaim(preauth.getClaim().getClaimType().getValue());
				corpBufferTableDto.setReferenceOrRodNo(preauth.getPreauthId());
				corpBufferTableDto.setStatus(preauth.getStatus().getProcessValue());
				if(null != preauth.getWintageBufferUtilAmt()){
					corpBufferTableDto.setCorpBufferUtilisation(Double.valueOf(preauth.getWintageBufferUtilAmt()));
				}
             if(preauth.getWintageBufferUtilAmt() != null && preauth.getWintageBufferUtilAmt() > 0){
            	 availableBalance = allocateCorpBufferDetailDTO.getWintageAvlBalnc() - preauth.getWintageBufferUtilAmt();
             }else {
            	 availableBalance = allocateCorpBufferDetailDTO.getWintageAvlBalnc();
             }
				corpBufferTableDto.setCorpBufferRemainingforClaim(availableBalance);

				corpbufferTableList.add(corpBufferTableDto);				 
			}	
		}

		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(claim != null){
			reimList = createRodService.getReimbursementByClaimKey(claim.getKey());
		}


		if(null != reimList && !reimList.isEmpty()){				 

			for (Reimbursement reimbursement : reimList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto1 = new CorporateBufferUtilisationTableDTO();
				corpBufferTableDto1.setTypeOfClaim(reimbursement.getClaim().getClaimType().getValue());
				corpBufferTableDto1.setReferenceOrRodNo(reimbursement.getRodNumber());
				corpBufferTableDto1.setStatus(reimbursement.getStatus().getProcessValue());
				if(null != reimbursement.getWintageBufferUtilAmt()){
					corpBufferTableDto1.setCorpBufferUtilisation(Double.valueOf(reimbursement.getWintageBufferUtilAmt()));
				}

				 if(reimbursement.getWintageBufferUtilAmt() != null && reimbursement.getWintageBufferUtilAmt() > 0){
	            	 availableBalance = allocateCorpBufferDetailDTO.getWintageAvlBalnc() - reimbursement.getWintageBufferUtilAmt();
	             }else {
	            	 availableBalance = allocateCorpBufferDetailDTO.getWintageAvlBalnc();
	             }
				 corpBufferTableDto1.setCorpBufferRemainingforClaim(availableBalance);

				corpbufferTableList.add(corpBufferTableDto1);

				if(null != reimbursement.getWintageBufferUtilAmt()){
					reimbUtilisedAmnt += reimbursement.getWintageBufferUtilAmt();
				}
			}

		}


		if(null != preauthObj && null != preauthObj.getWintageBufferUtilAmt()){
			preauthUtilisedAmnt += preauthObj.getWintageBufferUtilAmt();
		}			 


//		if(null != txtCorpBufferUtilisedforCurrentClaim){
//
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(false);
//			if(!preauthUtilisedAmnt.equals(0d) && !reimbUtilisedAmnt.equals(0d)){
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(reimbUtilisedAmnt));
//			}else{
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(preauthUtilisedAmnt + reimbUtilisedAmnt));
//			}
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(true);
//		}


		return corpbufferTableList;
	}
	
	private List<CorporateBufferUtilisationTableDTO> setNacBufferUtilisedTableValues(Claim claim,AllocateCorpBufferDetailDTO  allocateCorpBufferDetailDTO)
	{

		Double reimbUtilisedAmnt = 0d;
		Double preauthUtilisedAmnt = 0d;  	
		Double availableBalance = 0d;


		List<CorporateBufferUtilisationTableDTO> corpbufferTableList = new ArrayList<CorporateBufferUtilisationTableDTO>();

		List<Preauth> preauthList = null;
		if(claim != null){
			preauthList = createRodService.getPreauthListByClaimKey(claim.getKey());
		}else{
			preauthList = new ArrayList<Preauth>();
		}

		Preauth preauthObj = new Preauth();


		if(null != preauthList && !preauthList.isEmpty()){

			for (Preauth preauth : preauthList) {
				if(preauth.getNacBufferUtilAmt() != null){
					preauthObj = preauth;
					break;
				}
			}
			//preauthObj = preauthList.get(0);

			for (Preauth preauth : preauthList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto = new CorporateBufferUtilisationTableDTO();

				corpBufferTableDto.setTypeOfClaim(preauth.getClaim().getClaimType().getValue());
				corpBufferTableDto.setReferenceOrRodNo(preauth.getPreauthId());
				corpBufferTableDto.setStatus(preauth.getStatus().getProcessValue());
				if(null != preauth.getNacBufferUtilAmt()){
					corpBufferTableDto.setCorpBufferUtilisation(Double.valueOf(preauth.getNacBufferUtilAmt()));
				}

				 if(preauth.getNacBufferUtilAmt() != null &&preauth.getNacBufferUtilAmt() > 0){
	            	 availableBalance = allocateCorpBufferDetailDTO.getNacAvlBalnc() - preauth.getNacBufferUtilAmt();
	             }else {
	            	 availableBalance = allocateCorpBufferDetailDTO.getNacAvlBalnc();
	             }
				 corpBufferTableDto.setCorpBufferRemainingforClaim(availableBalance);

				corpbufferTableList.add(corpBufferTableDto);				 
			}	
		}

		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(claim != null){
			reimList = createRodService.getReimbursementByClaimKey(claim.getKey());
		}


		if(null != reimList && !reimList.isEmpty()){				 

			for (Reimbursement reimbursement : reimList) {
				CorporateBufferUtilisationTableDTO corpBufferTableDto1 = new CorporateBufferUtilisationTableDTO();
				corpBufferTableDto1.setTypeOfClaim(reimbursement.getClaim().getClaimType().getValue());
				corpBufferTableDto1.setReferenceOrRodNo(reimbursement.getRodNumber());
				corpBufferTableDto1.setStatus(reimbursement.getStatus().getProcessValue());
				if(null != reimbursement.getNacBufferUtilAmt()){
					corpBufferTableDto1.setCorpBufferUtilisation(Double.valueOf(reimbursement.getNacBufferUtilAmt()));
				}

				 if(reimbursement.getNacBufferUtilAmt() != null && reimbursement.getNacBufferUtilAmt() > 0){
	            	 availableBalance = allocateCorpBufferDetailDTO.getNacAvlBalnc() - reimbursement.getNacBufferUtilAmt();
	             }else {
	            	 availableBalance = allocateCorpBufferDetailDTO.getNacAvlBalnc();
	             }
				 corpBufferTableDto1.setCorpBufferRemainingforClaim(availableBalance);

				corpbufferTableList.add(corpBufferTableDto1);

				if(null != reimbursement.getNacBufferUtilAmt()){
					reimbUtilisedAmnt += reimbursement.getNacBufferUtilAmt();
				}
			}

		}


		if(null != preauthObj && null != preauthObj.getNacBufferUtilAmt()){
			preauthUtilisedAmnt += preauthObj.getNacBufferUtilAmt();
		}			 


//		if(null != txtCorpBufferUtilisedforCurrentClaim){
//
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(false);
//			if(!preauthUtilisedAmnt.equals(0d) && !reimbUtilisedAmnt.equals(0d)){
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(reimbUtilisedAmnt));
//			}else{
//				txtCorpBufferUtilisedforCurrentClaim.setValue(String.valueOf(preauthUtilisedAmnt + reimbUtilisedAmnt));
//			}
//			txtCorpBufferUtilisedforCurrentClaim.setReadOnly(true);
//		}


		return corpbufferTableList;
	}

}
