package com.shaic.claim.intimation.viewdetails.search;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class SearchClaimDocumentsTable extends GBaseTable<NewIntimationDto>{
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	private final static Object COLUM_HEADER_SUBMIT_INTIMATION[] = new Object[] {
		"serialNumber","intimationId","Action"};
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ClaimService claimService;
	
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

	table.removeGeneratedColumn("Action");
	table.addGeneratedColumn("Action",
			new Table.ColumnGenerator() {
				@SuppressWarnings("serial")
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View");
					viewIntimationDetailsButton.setData(itemId);
					final Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {
									Intimation intimation = intimationService.getIntimationByKey(intimationKey);
									viewUploadedDocumentDetails(intimation.getIntimationId());
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});

	
	
	   table.setColumnHeader("Action", "Action");
	   table.setContainerDataSource(new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class));
		table.setSizeFull();
		
	}
	
	@SuppressWarnings("deprecation")
	public void setSubmitTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_SUBMIT_INTIMATION);
		table.setColumnHeader("serialNumber", "S.no"); 	
		table.setColumnHeader("intimationId", "Intimation Number");
		table.setColumnHeader("Action", "Action");
	}
	
	@Override
	public void tableSelectHandler(NewIntimationDto t) {
		// TODO Auto-generated method stub
		//Long Key =t.getKey();
	}

	@Override
	public String textBundlePrefixString() {
		return "searchIntimation-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void addClaimNumberColum(){
		table.removeGeneratedColumn("claimNumber");
		table.addGeneratedColumn("claimNumber",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						String claimNum = "";

						Claim a_claim = claimService
								.getClaimforIntimation(((NewIntimationDto) itemId)
										.getKey());

						if (a_claim != null && a_claim.getClaimId() != null) {
							claimNum = a_claim.getClaimId();
						}
						Label claimNumber = new Label(claimNum);
						// source.getContainerDataSource().addItem(claimNumber);
						return claimNumber;
					}
				});
	}

	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
public void viewUploadedDocumentDetails(String intimationNo) {
		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);

	

	}

	
}



