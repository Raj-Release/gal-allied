package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;

public class ConsumerForumPagePresenter {
	
	public static final String SEARCH_CONSUMER_FORUM = "Search_consumer_forum";
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private ConsumerFormSearchUI consumerFormSearchUI;
	
	@Inject
	private SearchProcessConsumerForumForm searchProcessConsumerForumForm;
	
	
	
	@SuppressWarnings("unchecked")	
	public void searchRepresentative(
			@Observes @CDIEvent(SEARCH_CONSUMER_FORUM) final ParameterDTO parameters) {
		String intimationNumber  = (String) parameters.getPrimaryParameter();
		String policyNumber  = (String) parameters.getSecondaryParameters()[0];
		List<SearchProcessConsumerForumTableDTO> tableList = null; 
		
		List<Claim> claimObject = claimService.getClaimObjectByLikeOperator(intimationNumber, policyNumber);
		if (claimObject != null) {
			tableList= SearchConsumerForumMapper.getInstance().getViewClaimHistoryDTO(claimObject);
		}
		consumerFormSearchUI.setTableData(tableList);
	
				
	}	
}
	
	
		
		
				
