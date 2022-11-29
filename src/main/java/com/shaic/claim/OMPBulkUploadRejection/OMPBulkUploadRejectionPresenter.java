package com.shaic.claim.OMPBulkUploadRejection;

import java.text.ParseException;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
@ViewInterface(OMPBulkUploadRejectionView.class)
public class OMPBulkUploadRejectionPresenter extends
AbstractMVPPresenter<OMPBulkUploadRejectionView> {

//	public static final String SEARCH_CPU_WISE_PREAUTH = "Search CPU wise Preauth";
	public static final String SEARCH_BULK_UPLOAD_REJECTION = "Search Bulk Upload Rejection";
	
	@EJB
	
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private PreauthService preAuthService;
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;	
	
	@EJB
	private DBCalculationService dbclaService;
	
	@EJB
	private OMPBulkUploadRejectionService ompBulkUploadRejectionService;
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showBulkUploadRejectionSearch(@Observes @CDIEvent(SEARCH_BULK_UPLOAD_REJECTION) final ParameterDTO parameters) throws ParseException
	    {
		   WeakHashMap<String,Object> searchFilter = (WeakHashMap<String,Object>) parameters.getPrimaryParameter();
		   
//		   List<CPUwisePreauthReportDto> claimReportDto = (List<CPUwisePreauthReportDto>) preAuthService.getCpuWisePreauth(searchFilter);

		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   
		   List<OMPBulkUploadRejectionResultDto> resultDto = ompBulkUploadRejectionService.getOmpBulkUploadRejectionDetails(searchFilter,dbclaService);
		   
		   view.showBulkUploadRejectionDetails(resultDto);
	    }
	

}
