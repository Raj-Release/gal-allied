package com.shaic.claim.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@UIScoped
@CDIView(value = MenuItemBean.GENERATE_COVERINGLETTER)
public class GenerateCoveringLetterViewImpl extends AbstractMVPView implements GenerateLetterView, View  {
		 
	@Inject private Instance<GenerateCoveringLetterUI> generateCoveringLetterUIInstance;
	
	private GenerateCoveringLetterUI generateCoveringLetterUI;
	
	@Inject private Instance<GenerateCoveringLetterDetailUI> coveringLetterDetailsUIInstance;
	
	private GenerateCoveringLetterDetailUI coveringLetterDetailsUI;
    
	//@Inject private Instance<MVPView> views;
	
	@EJB IntimationService intimationService;
	@EJB ClaimService claimService;
	@EJB PolicyService policyService;
	
		@PostConstruct
		public void initView() {
			addStyleName("view");
			setSizeFull();
			generateCoveringLetterUI = generateCoveringLetterUIInstance.get();
			generateCoveringLetterUI.init();
			setCompositionRoot(generateCoveringLetterUI);
		}

		@Override
		public void showClaimSearchTable(Page<GenerateCoveringLetterSearchTableDto> resultTableList) {
			
			if(null != resultTableList && resultTableList.getPageItems() != null &&  !resultTableList.getPageItems().isEmpty()){
				generateCoveringLetterUI.showSearchResultTable(resultTableList);
				
			}
			else{
				
				generateCoveringLetterUI.showInformation("No Result Found for Search");

//				Notification.show("Claim Search Information","No Task for Covering Letter Generation" , Notification.Type.HUMANIZED_MESSAGE);
			 return;
		   }
			  
//			if(!filters.isEmpty())
//			{
//			  List<Claim> resultClaimList = claimService.getClaimforIntimationByParams(filters);
//			  BeanItemContainer<ClaimDtoOld> claimsDtoContainer = new BeanItemContainer<ClaimDtoOld>(ClaimDtoOld.class);
//			  
//		   if(!ValidatorUtils.isNull(resultClaimList) && !resultClaimList.isEmpty())
//		   {	DtoConverter dtoConverter = new DtoConverter();
//		      for (Claim claim : resultClaimList) {
//		    	  Hospitals hospital = policyService.getVWHospitalByKey(claim.getIntimation().getHospital());
//		    	  ClaimDtoOld claimDto = dtoConverter.claimToClaimDTO(claim,hospital);
//			
//				 claimsDtoContainer.addBean(claimDto);
//			  }
//	       }
//		   else
//		   {
//			   Notification.show("Claim Search Information","No Records found" , Notification.TYPE_HUMANIZED_MESSAGE);
//			   return;
//		   }	   
//	        generateCoveringLetterUI.get().showSearchResultTable(claimsDtoContainer);
//	        
//		}
//			else{
//				Notification.show("Claim Search Information","Please Enter Atleast One parameter for Search" , Notification.TYPE_HUMANIZED_MESSAGE);
//				   return;
//			}
	
		}
		
		
//		@Override
//		public void showClaimSearchTable(Map<String, Object> filters) {
//			
//			GetHumanTasks BPMserviceInstance = new GetHumanTasks();
//			//TODO User and Password for BPM Service call is hardcoded for R1
//			List<HumanTask> taskList = BPMserviceInstance.execute("zonaluser1", "welcome1");
//			
//			
//			if(!taskList.isEmpty())
//			{
//			  Map<String,Object> params = new HashMap<String,Object>();
//			  Claim resultClaim;
//			  List<ClaimDto> claimListDto = new ArrayList<ClaimDto>();
//			  if(filters.containsKey("policyNumber")) 
//			  {
//				 params.put("policyNumber", filters.get("policyNumber"));
//			  }	
//			  if(filters.containsKey("cpuCode"))
//			  {
//				 params.put("cpuCode", filters.get("cpuCode"));
//			  }
//			  if(filters.containsKey("claimNumber"))
//			  {
//				 params.put("claimNumber", filters.get("claimNumber"));
//			  }
//			  if(filters.containsKey("registeredDate"))
//			  {
//				 params.put("registeredDate",filters.get("registeredDate"));
//			  }	
//			  for (HumanTask humanTask : taskList) 
//			  { 
//				String intimationNumber = getIntimationNumberFromPayLoad(humanTask);
//			
//				//add intimation number to param list
//            	
//				if(filters.containsKey("intimationNumber") && intimationNumber.equalsIgnoreCase(filters.get("intimatonNumber").toString()))
//				{
//					params.put("intimationNumber", intimationNumber);
//				}	
//			  
//				resultClaim = claimService.getClaimforIntimationByParams(params);
//				if(!ValidatorUtils.isNull(resultClaim))
//				{
//					Hospitals hospital = (new PolicyService()).getVWHospitalByKey(resultClaim.getIntimation().getHospital());
//					ClaimDto claimDto = DtoConverter.claimToClaimDTO(resultClaim, hospital);
//					claimListDto.add(claimDto);
//				}
//			  }
//					
//			  
//			}
//			else
//		    {
//				  //empty task list
//			}
//			  
//			if(!filters.isEmpty())
//			{
//			  List<Claim> resultClaimList = claimService.getClaimforIntimationByParams(filters);
//			  BeanItemContainer<ClaimDtoOld> claimsDtoContainer = new BeanItemContainer<ClaimDtoOld>(ClaimDtoOld.class);
//			  
//		   if(!ValidatorUtils.isNull(resultClaimList) && !resultClaimList.isEmpty())
//		   {	DtoConverter dtoConverter = new DtoConverter();
//		      for (Claim claim : resultClaimList) {
//		    	  Hospitals hospital = policyService.getVWHospitalByKey(claim.getIntimation().getHospital());
//		    	  ClaimDtoOld claimDto = dtoConverter.claimToClaimDTO(claim,hospital);
//			
//				 claimsDtoContainer.addBean(claimDto);
//			  }
//	       }
//		   else
//		   {
//			   Notification.show("Claim Search Information","No Records found" , Notification.TYPE_HUMANIZED_MESSAGE);
//			   return;
//		   }	   
//	        generateCoveringLetterUI.showSearchResultTable(claimsDtoContainer);
//	        
//		}
//			else{
//				Notification.show("Claim Search Information","Please Enter Atleast One parameter for Search" , Notification.TYPE_HUMANIZED_MESSAGE);
//				   return;
//			}
//			
//		}
		
		@Override
		public void resetClaimSearchCoveringLetterGeneration() {
			//ConfirmDialog modalDialog = new ConfirmDialog();
//			modalDialog.show(generateCoveringLetter.get(), "Confirm", "Are You Sure, Do You Want to Cancel Covering Letter Generation", "Ok", "Cancel");
			
		}

		@Override
		public void showGenrateCoveringLetterDetailView(ParameterDTO parameter) {
			//GenerateCoveringLetterSearchTableDto tableDTO = (GenerateCoveringLetterSearchTableDto) parameter.getPrimaryParameter();
			/*if(tableDTO.getIsLegal())
			{
				Label label = new Label("Intimation is locked by legal process. Cannot proceed further", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);

		/*	}
			else
			{*/
				coveringLetterDetailsUI = coveringLetterDetailsUIInstance.get();
				coveringLetterDetailsUI.init();
				GenerateCoveringLetterSearchTableDto dto = (GenerateCoveringLetterSearchTableDto) parameter.getPrimaryParameter(); 
				
				// R1045
				Intimation intimation = intimationService.searchbyIntimationNo(dto.getIntimationNo());
				NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);

				List<Claim> claimList = claimService.getClaimByIntimation(intimation.getKey());
				Claim claim = null;
				if(null != claimList && !claimList.isEmpty()) {
					claim = claimList.get(0);
				}

				dto.setPreauthDTO(new PreauthDTO());
				dto.getPreauthDTO().setNewIntimationDTO(newIntimationDto);
				dto.getPreauthDTO().setCrmFlagged(claim.getCrcFlag());
				dto.setCrcFlaggedReason(claim.getCrcFlaggedReason());
				dto.setCrcFlaggedRemark(claim.getCrcFlaggedRemark());
				
				coveringLetterDetailsUI.initView(dto);
				setCompositionRoot(coveringLetterDetailsUI);
			//}
			
//			parameter.getPrimaryParameter();
//			MVPView view = views.select(viewClass).get();
//			 setCompositionRoot((Component) view);
//		      ((AbstractMVPView) view).enter();
			
		}
		
		
//		@Override
//		public void showGenrateCoveringLetterDetailViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter) {
//			parameter.getPrimaryParameter();
//			GMVPView view = views.select(viewClass).get();
//			view.resetView();
//			 setCompositionRoot((Component) view);
//		      ((AbstractMVPView) view).enter();
//			
//		}
	
		@Override
		public void showSearchCoveringLetterView(String letterType) {
				
			if(letterType == null)
			{			
				generateCoveringLetterUI.init();
			}
			else
			{
				generateCoveringLetterUI.init(letterType);
			}
			setCompositionRoot(generateCoveringLetterUI);
//			Page.getCurrent().reload();
			
		}
		
		@Override
		public void generateCoveringLetter(ClaimDto claimDto)
		{
			
			
			String hosptialName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();
			
			if(hosptialName != null && !("").equalsIgnoreCase(hosptialName)){
				
//				hosptialName.replace("&", "and");
				
				hosptialName = StringUtil.replaceSubString(hosptialName, "&", "and");
				claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().setName(hosptialName);
			}
			
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			DocumentGenerator docGenarator = new DocumentGenerator();
			String fileUrl = null;
			ReportDto reportDto = new ReportDto();
			reportDto.setClaimId(claimDto.getClaimId());
			reportDto.setBeanList(claimDtoList);
			
			fileUrl = docGenarator.generatePdfDocument("ClaimFormCoveringLetter", reportDto);
			
			if(!ValidatorUtils.isNull(fileUrl))
			{
				
				/**
				 * 
				 * The below method was commented. Don't why this was done.
				 * As an impact, covering letter flag wasn't updated in 
				 * claim table. When discussed with lakshmi, the below 
				 * code does the updation. Hence to update this flag alone,
				 * instead of create method, a new method specifically 
				 * for updating the claim was added in claim service. This
				 * is done as per lakshmi suggestion.
				 * 
				 * */
		
			 claimService.updateClaim(claimDto.getKey());
			 claimDto.setDocFilePath(fileUrl);
			 claimDto.setDocType(SHAConstants.CLAIM_COVERING_LETTER);
//			 claimService.uploadCoveringLetterToDMs(claimDto);
//			 claimService.create(claim);			 

			 
			 coveringLetterDetailsUI = coveringLetterDetailsUIInstance.get();
					 coveringLetterDetailsUI.openPdfFileInWindow(fileUrl);
			}
			else
			{
				//Exception while PDF Letter Generation
			}	
			
		}
		@Override
		public void cancelCoveringLetter(Claim a_claim) {
			
			
			coveringLetterDetailsUIInstance.get().cancelCoveringLetter();
			
			
//			viewCoveringLetterDetails.get().cancelCoveringLetter();
//			
//			System.out.println("Status of claim ********************** : "+a_claim.getStatus());
//			if(a_claim.getStatus().getProcessValue().equalsIgnoreCase("Registered"))
//			{	
//				coveringLetterDetailsUIInstance.get().cancelCoveringLetter();
//			}
//			else if(a_claim.getStatus().getProcessValue().equalsIgnoreCase("Rejected"))
//			{
//				coveringLetterDetailsUI.get().cancelRejectionLetter();
//			}	
//			Long rejectionFlag = a_claim.getStatus().getProcessValue().equalsIgnoreCase("Rejected") ? 0l : 1l;
//			 Long coveringLetterFlag = a_claim.getStatus().getProcessValue().equalsIgnoreCase("Registered") ? 0l : 1l ;
//			 
//			 if(a_claim.getStatus().getProcessValue().equalsIgnoreCase("Rejected"))
//			 {
//				 a_claim.setRejectionLetterflag(rejectionFlag);	 
//			 }	 
//			 if(a_claim.getStatus().getProcessValue().equalsIgnoreCase("Registered"))
//			 {
//				 a_claim.setConversionLetter(coveringLetterFlag);	 
//			 }
//			 claimService.create(a_claim);			
		}

		@Override
		public void resetView() {
			if(generateCoveringLetterUI != null)
			{
				generateCoveringLetterUI.init();
			}	
			
		}

		@Override
		public void doSearch() {
			
			Map<String,Object> filters = generateCoveringLetterUI.getSearchFilter();
			
			filters.put(BPMClientContext.USERID,UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			filters.put(BPMClientContext.PASSWORD,UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());
			filters.put(BPMClientContext.USER_OBJECT, UI.getCurrent().getSession().getAttribute(BPMClientContext.USER_OBJECT));
			filters.put("pageable",generateCoveringLetterUI.getGenCovLetterSearchTable().getPageable());
	        
			fireViewEvent(GenerateCoveringLetterPresenter.SUBMIT_COVERING_LETTER_SEARCH,filters);
				
		}

		@Override
		public void resetSearchResultTableValues() {

			generateCoveringLetterUI.resetComponents();
			generateCoveringLetterUI.getGenCovLetterSearchTable().getPageable().setPageNumber(1);
			
			generateCoveringLetterUI.getGenCovLetterSearchTable().removeRow();
			
		}
		
}
