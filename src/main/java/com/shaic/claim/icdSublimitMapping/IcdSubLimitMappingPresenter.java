package com.shaic.claim.icdSublimitMapping;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IcdSublimitMap;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
@ViewInterface(IcdSubLimitMappingView.class)
public class IcdSubLimitMappingPresenter extends
AbstractMVPPresenter<IcdSubLimitMappingView> {

	public static final String SEARCH_ICD_SUBLIMIT_MAPPING = "Search ICD Sublimit Mapping";
	public static final String GET_ICD_BLOCK_FOR_ICD_CHAPTER = "Get ICD Block For ICD Chapter";
	public static final String GET_ICD_CODE_FOR_ICD_BLOCK = "Get ICD Code For ICD Block"; 
	public static final String VIEW_ICD_SUBLIMIT_MAPPING = "View ICD Sublimit Mapping";
	public static final String ADD_ICD_SUBLIMIT_MAPPING = "Add ICD Sublimit Mapping";
	public static final String SUBMIT_BTN_CLICK = "Submit Button Click";
	
	public static final String UN_CHECK_MAPPING = "Uncheck the Selected ICD Code Mapping";

	
	@EJB
	private IcdSubLimitMappingService icdSublimitMappingService;

	@EJB
	private MasterService masterService;
		
	@EJB
	private PreauthService preauthService;
	
	@Override
	public void viewEntered() {
				
	}
	
	protected void showIcdSublimitMapSearch(@Observes @CDIEvent(SEARCH_ICD_SUBLIMIT_MAPPING) final ParameterDTO parameters)
	{	
		String sublimitName = (String)parameters.getPrimaryParameter();
		
		SublimitFunObject sublimitObj = icdSublimitMappingService.getSublimitDetailsBasedOnName(sublimitName);
		
		view.showSubLimitName(sublimitObj);
	}

	
	protected void unselectIcdSublimitMapping(@Observes @CDIEvent(UN_CHECK_MAPPING) final ParameterDTO parameters)
	{	
		boolean unChecked = (boolean)parameters.getPrimaryParameter();
		
		view.showUnChecked(unChecked);
	}
	
	protected void getIcdBlockForChapter(@Observes @CDIEvent(GET_ICD_BLOCK_FOR_ICD_CHAPTER) final ParameterDTO parameters)
	{		
		Long icdChapterKey = (Long)parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(icdChapterKey != null){
			
			 List<IcdSublimitMap> sublimitIcdMapList = icdSublimitMappingService.getSublimitMapByICDChapterKey(icdChapterKey);
			 BeanItemContainer<SelectValue> icdChapterContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			 
			 if(sublimitIcdMapList != null && !sublimitIcdMapList.isEmpty()){

				 IcdSublimitMap resultObj = sublimitIcdMapList.get(0);
				 
				 SelectValue icdBlkSelect = null;
				 List<SelectValue> icdBlkSelectList = new ArrayList<SelectValue>();

				 if(sublimitIcdMapList.size()>=2){
				 		icdBlkSelectList.add(masterService.getIcdBlock(sublimitIcdMapList.get(0).getIcdBlock().getKey()));
					 for(int i = 0,j = i+1; j < sublimitIcdMapList.size(); i++,j++){
							 
						 if(sublimitIcdMapList.get(i).getIcdBlock().getKey().intValue() != sublimitIcdMapList.get(j).getIcdBlock().getKey().intValue()){
						 		icdBlkSelect = masterService.getIcdBlock(sublimitIcdMapList.get(i).getIcdBlock().getKey());
						 		icdBlkSelectList.add(icdBlkSelect);
						 }	 
					 }
					 icdBlkSelectList.add(masterService.getIcdBlock(sublimitIcdMapList.get(sublimitIcdMapList.size()-1).getIcdBlock().getKey()));
					 icdBlockContainer.addAll(icdBlkSelectList);
										 
				 }
				 else{
					 if(resultObj.getIcdBlock() != null){
						 icdBlockContainer = masterService.getIcdBlockbyId(resultObj.getIcdBlock().getKey());
					 }				 
				 }			 
				 
			 }
			 else{
				 icdBlockContainer = masterService.getIcdBlockbyKey(icdChapterKey);
			 }
			
		view.setIcdBlockDroDownValues(icdBlockContainer);
		}
		
	}
	
	protected void getIcdCodeForBlock(@Observes @CDIEvent(GET_ICD_CODE_FOR_ICD_BLOCK) final ParameterDTO parameters)
	{	
		Long icdBlockKey = (Long)parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		List<IcdSublimitMap> sublimitIcdMapList = icdSublimitMappingService.getSublimitMapByIcdBlockKey(icdBlockKey);
		
		List<IcdSublimitMappingDto> icdCodeSelectList = new ArrayList<IcdSublimitMappingDto>();

		if(sublimitIcdMapList != null || !sublimitIcdMapList.isEmpty()){
			
			IcdSublimitMappingDto icdSublimitMappingDto = null;
				
				for(int i = 0; i < sublimitIcdMapList.size(); i++){
					icdSublimitMappingDto = new IcdSublimitMappingDto();
					icdSublimitMappingDto.setIcdDescripitonKey(sublimitIcdMapList.get(i).getIcdCode().getKey());
					icdSublimitMappingDto.setIcdSublimitMapKey(sublimitIcdMapList.get(i).getKey());
					icdSublimitMappingDto.setIcdDescripiton(sublimitIcdMapList.get(i).getIcdCode().getValue() +" - " + sublimitIcdMapList.get(i).getIcdCodeDescription());
					icdSublimitMappingDto.setIcdCodeSelect(new SelectValue(sublimitIcdMapList.get(i).getIcdCode().getKey(), (sublimitIcdMapList.get(i).getIcdCode().getValue() + " - " + sublimitIcdMapList.get(i).getIcdCodeDescription())));
					icdSublimitMappingDto.setSelected(sublimitIcdMapList.get(i).getActiveStatus().intValue() == 1 ? true : false);
					icdSublimitMappingDto.setSno(String.valueOf(sublimitIcdMapList.indexOf(sublimitIcdMapList.get(i))+1));
					icdCodeSelectList.add(icdSublimitMappingDto);
				}			
			
		}
		else{
			if(icdBlockKey != null){
				icdCodeContainer = masterService.searchIcdCodeByBlockKey(icdBlockKey);
			}
		
			if(icdCodeContainer != null && icdCodeContainer.getItemIds() != null && !icdCodeContainer.getItemIds().isEmpty()){
				IcdSublimitMappingDto icdSublimitMappingDto = null;
				
				for(int i = 0; i < icdCodeContainer.getItemIds().size(); i++){
					icdSublimitMappingDto = new IcdSublimitMappingDto();
					icdSublimitMappingDto.setIcdDescripitonKey(icdCodeContainer.getItemIds().get(i).getId());
					icdSublimitMappingDto.setIcdDescripiton(icdCodeContainer.getItemIds().get(i).getValue());
					icdSublimitMappingDto.setIcdCodeSelect(new SelectValue(icdCodeContainer.getItemIds().get(i).getId(), icdCodeContainer.getItemIds().get(i).getValue()));
					icdSublimitMappingDto.setSelected(false);
					icdSublimitMappingDto.setSno(String.valueOf(i+1));
					icdCodeSelectList.add(icdSublimitMappingDto);
				}
			}
		}
			view.showMappingTable(icdCodeSelectList);
	}
	
	protected void 	submitIcdSublimitMapping(@Observes @CDIEvent( SUBMIT_BTN_CLICK) final ParameterDTO parameters){
		SearchICDSubLimitMappingDto finalDto = (SearchICDSubLimitMappingDto)parameters.getPrimaryParameter();
		boolean result = icdSublimitMappingService.submit(finalDto);
		StringBuffer resultMsg = new StringBuffer("");
		if(result){
			resultMsg.append("Data Updated Successfully");
			view.showSuccessLayout(resultMsg.toString());
		}
		else{
			resultMsg.append("Error has Occured while Updating the Data");
			view.showErroLayout(resultMsg.toString());
		}
		
	}
	
	protected void viewIcdSubLimitMapping(@Observes @CDIEvent(VIEW_ICD_SUBLIMIT_MAPPING) final ParameterDTO parameters)
	 {	
		 String sublimitName = (String)parameters.getPrimaryParameter();
		 List<IcdSublimitMap> sublimitIcdMapList = icdSublimitMappingService.getSublimitMapByName(sublimitName);
		 BeanItemContainer<SelectValue> icdChapterContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 
		 if(sublimitIcdMapList != null && !sublimitIcdMapList.isEmpty()){

			 IcdSublimitMap resultObj = sublimitIcdMapList.get(0);
			 BeanItemContainer<SelectValue> icdBlockContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			 BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			 
			 icdChapterContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			 SelectValue icdBlkSelect = null;
			 List<SelectValue> icdBlkSelectList = new ArrayList<SelectValue>();

			 if(sublimitIcdMapList.size()>=2){
			 		icdBlkSelectList.add(masterService.getIcdBlock(sublimitIcdMapList.get(0).getIcdBlock().getKey()));
				 for(int i = 0,j = i+1; j < sublimitIcdMapList.size(); i++,j++){
						 
					 if(sublimitIcdMapList.get(i).getIcdBlock().getKey().intValue() != sublimitIcdMapList.get(j).getIcdBlock().getKey().intValue()){
					 		icdBlkSelect = masterService.getIcdBlock(sublimitIcdMapList.get(i).getIcdBlock().getKey());
					 		icdBlkSelectList.add(icdBlkSelect);
					 }	 
				 }
				 icdBlkSelectList.add(masterService.getIcdBlock(sublimitIcdMapList.get(sublimitIcdMapList.size()-1).getIcdBlock().getKey()));
				 icdBlockContainer.addAll(icdBlkSelectList);
									 
			 }
			 else{
				 if(resultObj.getIcdBlock() != null){
					 icdBlockContainer = masterService.getIcdBlockbyId(resultObj.getIcdBlock().getKey());
				 }				 
			 }			 
			 
			 if(resultObj != null && resultObj.getIcdChapter() != null && resultObj.getIcdChapter().getKey() != null){
				 icdChapterContainer = masterService.getIcdChapterbyKey(resultObj.getIcdChapter().getKey());
			 }
			 List<IcdSublimitMappingDto> icdCodeSelectList = new ArrayList<IcdSublimitMappingDto>();
			 IcdSublimitMappingDto icdSublimitMappingDto = null;
			 IcdCode icdCode = null;
			 boolean selectAll = true;
	
			 for (IcdSublimitMap icdSublimitMap : sublimitIcdMapList) {
				 if(icdBlkSelectList != null && !icdBlkSelectList.isEmpty() 
						 && icdBlkSelectList.get(0).getId().intValue() == icdSublimitMap.getIcdBlock().getKey().intValue()
						 && icdSublimitMap.getIcdCode().getKey() != null){
					 icdCode  = preauthService.getIcdCode(icdSublimitMap.getIcdCode().getKey());
					 icdSublimitMappingDto = new IcdSublimitMappingDto();
					 icdSublimitMappingDto.setIcdDescripitonKey(icdCode.getKey());
					 icdSublimitMappingDto.setIcdDescripiton(icdCode.getValue() + " - " + icdCode.getDescription());
					 icdSublimitMappingDto.setIcdCodeSelect(new SelectValue(icdCode.getKey(), icdCode.getValue() + " - " + icdCode.getDescription()));
					 icdSublimitMappingDto.setSelected(icdSublimitMap.getActiveStatus() != null && icdSublimitMap.getActiveStatus().intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);
					 selectAll = selectAll && icdSublimitMappingDto.isSelected();
					 icdSublimitMappingDto.setIcdSublimitMapKey(icdSublimitMap.getKey());
					 icdSublimitMappingDto.setSno(String.valueOf(sublimitIcdMapList.indexOf(icdSublimitMap)+1));
					 icdCodeSelectList.add(icdSublimitMappingDto);
				 }				
			}	
		 
		   view.showMappingTableWithMappedData(icdChapterContainer, icdBlockContainer, icdCodeSelectList,selectAll);
		 }	
		 
		 else{
		 
			 icdChapterContainer = masterService.getICDchapter();
		 
			 view.showViewButtonClick(icdChapterContainer);
		 }
	 }

	 protected void addIcdSubLimitMapping(@Observes @CDIEvent(ADD_ICD_SUBLIMIT_MAPPING) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }
}
