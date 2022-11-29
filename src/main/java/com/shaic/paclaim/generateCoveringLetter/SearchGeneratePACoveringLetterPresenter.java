package com.shaic.paclaim.generateCoveringLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.registration.GenerateCoveringLetterSearchDto;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.GenerateCoveringLetterService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@SuppressWarnings("serial")
@ViewInterface(SearchGenerateCoveringLetterPAView.class)
public class SearchGeneratePACoveringLetterPresenter extends AbstractMVPPresenter<SearchGenerateCoveringLetterPAView>{

	public static final String INITIATE_SEARCH_PA = "Initiate Search PA";
	
	public static final String SUBMIT_PA_COVERING_LETTER_SEARCH = "Submit PA Claim Covering letter Search";
	
	@EJB
	private GenerateCoveringLetterService generateCoverinLetterSrevice;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackService;
	
	@EJB
	private CreateRODService createRodService;
	
	protected void initiateSearch(@Observes @CDIEvent(INITIATE_SEARCH_PA) final ParameterDTO parameters) {
		view.doSearch();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void showClaimSearchTable(@Observes @CDIEvent(SUBMIT_PA_COVERING_LETTER_SEARCH) final ParameterDTO parameters) {
		Map<String,Object> queryFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		
//		queryFilter.put("lobType", SHAConstants.PA_LOB_TYPE);
		queryFilter.put("lob",SHAConstants.PA_LOB);
		
		GenerateCoveringLetterSearchDto searchDto = new GenerateCoveringLetterSearchDto();
		searchDto.setQueryFilter(queryFilter);
		
		searchDto.setPageable(queryFilter != null && queryFilter.containsKey("pageable") ? (Pageable)queryFilter.get("pageable") : null );
		Page<GenerateCoveringLetterSearchTableDto> coveringLetterTableContainer = generateCoverinLetterSrevice.getClaimsToGenerateCoveringLetter(searchDto);
		
		List<GenerateCoveringLetterSearchTableDto> searchResultList = coveringLetterTableContainer.getPageItems();
		
		if(coveringLetterTableContainer != null && !searchResultList.isEmpty()){
			for (GenerateCoveringLetterSearchTableDto generateCoveringLetterSearchTableDto : searchResultList) {
				Intimation intimation = intimationService.getIntimationByKey(generateCoveringLetterSearchTableDto.getClaimDto().getNewIntimationDto().getKey());
				NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);
				generateCoveringLetterSearchTableDto.getClaimDto().setNewIntimationDto(newIntimationDto);
				generateCoveringLetterSearchTableDto.setPaDocChecklist(ackService.getPADocCheckListValues(masterService,generateCoveringLetterSearchTableDto.getClaimDto().getIncidenceFlagValue() != null && SHAConstants.DEATH_FLAG.equalsIgnoreCase(generateCoveringLetterSearchTableDto.getClaimDto().getIncidenceFlagValue()) ? SHAConstants.DEATH : SHAConstants.HOSPITALIZATION));
//				BeanItemContainer<SelectValue> selectDocContainer = masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA);
				if(generateCoveringLetterSearchTableDto.getPaDocChecklist() != null && !generateCoveringLetterSearchTableDto.getPaDocChecklist().isEmpty()){
					List<DocumentCheckListDTO> paDocChecklist = generateCoveringLetterSearchTableDto.getPaDocChecklist();
					List<SelectValue> docContainerList = new ArrayList<SelectValue>();
					SelectValue docSelect = null;
					for (DocumentCheckListDTO docChkListDto : paDocChecklist) {
						docSelect = new SelectValue(docChkListDto.getParticulars().getId(),docChkListDto.getValue());
						docContainerList.add(docSelect);
					}
					if(docContainerList != null && !docContainerList.isEmpty()){
						generateCoveringLetterSearchTableDto.setPaDocContainerList(docContainerList);	
					}				
				}
				
			}			
		}
		
		view.showClaimSearchTable(coveringLetterTableContainer);
	}
}
