package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;



@ViewInterface(PaymentReprocessSearchView.class)
public class PaymentReprocessSearchPresenter extends AbstractMVPPresenter<PaymentReprocessSearchView>{
	
	
	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	

	private static final long serialVersionUID = 3362624633543797966L;
	
	public static final String PAYMENT_REPROCESS_SEARCH = "PaymentReprocessSearch";
	public static final String PAYMENT_CANCEL_REPROCESS_REMARKS="PaymentCancelReprocessRemarks";
	public static final String POPUP_CANCEL_REQUEST="popupCancelLayout";
	public static final String SHOW_VIEW_DOCUMENTS="showViewDocuments";
	
	@Inject
	public PaymentReprocessDBService dbService;
	
	private Window popup;
	private TextArea remarks;

	public PaymentReprocessSearchPresenter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void viewEntered() {
		System.out.println("Payment Re-Process View Entered");		
	}
	
	public void handleSearch(@Observes @CDIEvent(PAYMENT_REPROCESS_SEARCH) final ParameterDTO parameters) {
		PaymentReprocessSearchFormDTO searchFormDTO = (PaymentReprocessSearchFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
    	view.renderTable(dbService.performSearch(searchFormDTO,userName,"ProcessLevelOneSearch"));
	}
	
	
	@SuppressWarnings("deprecation")
	public void paymenetRemarks(@Observes @CDIEvent(PAYMENT_CANCEL_REPROCESS_REMARKS) final ParameterDTO parameters){
		PaymentReprocessSearchResultDTO tableDto = (PaymentReprocessSearchResultDTO) parameters.getPrimaryParameter();
		
		dbService.submitCancelReprocessRemarks(tableDto);
		view.buildSuccessLayout();
		
		
	}
	
	public void cancelLumenRequest(@Observes @CDIEvent(POPUP_CANCEL_REQUEST) final ParameterDTO parameters) {
		view.buildCancelLayout();
	}

	
	public void showViewDocuments(@Observes @CDIEvent(SHOW_VIEW_DOCUMENTS) final ParameterDTO parameters){
		String intimationNo = (String) parameters.getPrimaryParameter();

		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		view.showClaimsDMS(url);

	
		
	}

}
