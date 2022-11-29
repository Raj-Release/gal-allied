package com.shaic.claim.bedphoto;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(UploadBedPhotoView.class)
public class BedPhotoDetailsPresenter extends AbstractMVPPresenter<UploadBedPhotoView>{
	
public static final String UPLOAD_EVENT = "Upload bed photo file";
	
	public static final String SUBMIT_EVENT = "Submit bed photo details";
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void setUploadDocument(
			@Observes @CDIEvent(UPLOAD_EVENT) final ParameterDTO parameters) {

		Long intimationKey = (Long) parameters.getPrimaryParameter();

		String fileName = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		String token = (String)parameters.getSecondaryParameter(1, String.class);
		
		String screenName = (String)parameters.getSecondaryParameter(2, String.class);
		String userName = (String)parameters.getSecondaryParameter(3, String.class);
		
		MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
		dto.setFileName(fileName);
		dto.setTransactionKey(intimationKey);
		dto.setFileToken(token);
		dto.setTransactionName(screenName);
		dto.setUploadedDate(new Date());
		dto.setUsername(userName);
		reimbursementService.updateDocumentDetails(dto);

		Intimation intimationByKey = intimationService.getIntimationByKey(intimationKey);
		intimationService.updateBedPhotoDetails(dto,intimationByKey.getIntimationId());
		view.updateTableValues(intimationByKey.getKey());
		
	}
	
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {
		view.buildSuccessLayout();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
