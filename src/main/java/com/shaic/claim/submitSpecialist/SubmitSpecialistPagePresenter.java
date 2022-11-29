package com.shaic.claim.submitSpecialist;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.search.specialist.search.SubmitSpecialistService;
import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(SubmitSpecialistPageView.class)
public class SubmitSpecialistPagePresenter extends AbstractMVPPresenter<SubmitSpecialistPageView>{
	
	public static final String SET_FIRST_TABLE = "Set data for Submit Specialist";
	
	public static final String SUBMIT_BUTTON_CLICK="Update data for preauth esclated table";	
	
	public static final String SUBMIT = "Preauth Specialist Submit";
	
	@EJB
	private SubmitSpecialistService submitSpecialistService;
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB 
	private PreauthService preauthService;
	
	//private NewIntimationMapper intimationMapper=new NewIntimationMapper();
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	public static final String VALIDATE_SUBMIT_SPECIALIST_USER_RRC_REQUEST = "validate_submit_specialist_user_rrc_request_cashless";
	
	public static final String SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "submit_specialist_load_rrc_request_drop_down_values_cashless";
	
	public static final String SUBMIT_SPECIALIST_SAVE_RRC_REQUEST_VALUES = "submit_specialist_save_rrc_request_values";
	
	public static final String VALIDATE_SUBMIT_SPECIALIST_USER_LUMEN_REQUEST = "validate_submit_specialist_user_lumen_request_cashless";

	public static final String SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "submit_specialist_load_rrc_request_sub_category_values";

	public static final String SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SOURCE_VALUES = "submit_specialist_load_rrc_request_source_values";
	
	public void setEditableTable( @Observes@CDIEvent(SET_FIRST_TABLE) final ParameterDTO parameters){
		
		SubmitSpecialistTableDTO bean=(SubmitSpecialistTableDTO)parameters.getPrimaryParameter();
		
	    Intimation intimation=intimationService.searchbyIntimationNo(bean.getIntimationNo());
	    
	    NewIntimationDto intimationDto=intimationService.getIntimationDto(intimation);
	    
		SubmitSpecialistDTO resultList=submitSpecialistService.findSpecialistValue(bean);
		
		BeanItemContainer<SelectValue> specialistContainer = masterService.getMasterValueByReference(ReferenceTable.SPECIALIST_TYPE);
		
		resultList.setSpecialistType(specialistContainer);
		
		Claim claim = null;
		
		if(bean.getClaimKey() != null){
		    claim = claimService.getClaimByClaimKey(bean.getClaimKey());
		}

	        ClaimDto claimDto=new ClaimDto();
	        claimDto = ClaimMapper.getInstance().getClaimDto(claim);
	        claimDto.setClaimId(claim.getClaimId());
	        MastersValue currency=claim.getCurrencyId();
	        SelectValue currencyId=new SelectValue();
	        currencyId.setId(currency.getKey());
	        currencyId.setValue(currency.getValue());
	        
	        claimDto.setCurrencyId(currencyId);
		
		view.ListOfValues(resultList,intimationDto,claimDto);
	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_SUBMIT_SPECIALIST_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(VALIDATE_SUBMIT_SPECIALIST_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(SUBMIT_SPECIALIST_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void submitSpecialist( @Observes@CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters){
		
		SubmitSpecialistDTO bean=(SubmitSpecialistDTO)parameters.getPrimaryParameter();
		
		SubmitSpecialistTableDTO searchDto=(SubmitSpecialistTableDTO)parameters.getSecondaryParameter(0, SubmitSpecialistTableDTO.class);
		
		Boolean result=submitSpecialistService.submitSpecialistforReimbursement(bean,searchDto);
		
		if(result){
//			view.result();
			view.reimbursementResult(searchDto);
			//view.reimbursementResult();
		}
		
		
	}
	
   public void submitSpecialistForPreauth( @Observes@CDIEvent(SUBMIT) final ParameterDTO parameters){
		
		SubmitSpecialistDTO bean=(SubmitSpecialistDTO)parameters.getPrimaryParameter();
		
		SubmitSpecialistTableDTO searchDto=(SubmitSpecialistTableDTO)parameters.getSecondaryParameter(0, SubmitSpecialistTableDTO.class);
		
		Boolean result=submitSpecialistService.submitSpecialistforPreauth(bean,searchDto);
		
		if(result){
			view.result();
		}
		
		
	}
	
	
 
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
