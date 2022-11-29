package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/



import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class EsclateClaimToRawPage extends ViewComponent{
	@Inject
	private Instance<EsclateToRawTable> esclateToRawTableObj;	
	private EsclateToRawTable esclateToRawTable;
	@Inject
	private Instance<EsclateToRawReplyReceivedTable> esclateToRawReplyReceivedTableObj;	
	private EsclateToRawReplyReceivedTable esclateToRawReplyReceivedTable;
	private Button btnSubmit;	
	private Button btnCancel;
	private String presenterString;
	private PreauthDTO bean;	
	private Window popup;	
	@EJB
	private MasterService masterService;
	@EJB
	private ClaimService claimService;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(PreauthDTO preauthDTO, Window popup)
	{
		bean = preauthDTO;
		this.popup = popup;
		esclateToRawTable = esclateToRawTableObj.get();
		esclateToRawTable.init();
		loadContainerDataSources();
		
		
		esclateToRawReplyReceivedTable = esclateToRawReplyReceivedTableObj.get();
		esclateToRawReplyReceivedTable.init("Reply Received from RAW", false, false);
		
		VerticalLayout esclateToRawLayout = new VerticalLayout();
		esclateToRawLayout.addComponents(esclateToRawTable,buildButtonLayout());
		esclateToRawLayout.addStyleName("layout-with-border");
		FormLayout esclateToRawReplyReceivedLayout = new FormLayout();
		esclateToRawReplyReceivedLayout.addComponent(esclateToRawReplyReceivedTable);
		esclateToRawReplyReceivedLayout.addStyleName("layout-with-border");
		
		VerticalLayout verticalLayout = new VerticalLayout( esclateToRawLayout,esclateToRawReplyReceivedLayout);
		
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		setTableValues();
		addListener();
		
		setCompositionRoot(verticalLayout);
	}
	private void setTableValues()
	{
		List<EsclateToRawTableDTO> esclatedClaimList = getEsclatedToRawList(bean.getNewIntimationDTO().getIntimationId());
		if(null != esclatedClaimList && !esclatedClaimList.isEmpty()){
			if(null != esclateToRawTable){
				esclateToRawTable.setTableList(esclatedClaimList);
				
			}
		} 
		List<EsclateToRawTableDTO> setRepliedRawList = getRepliedRawList(bean.getNewIntimationDTO().getIntimationId());
		if(null != setRepliedRawList && !setRepliedRawList.isEmpty()){
			if(null != esclateToRawReplyReceivedTable){
				esclateToRawReplyReceivedTable.setTableList(setRepliedRawList);
			}
		}
	}
	
	public void addListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!isValid()){
					
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					
					bean.setStrUserName(userName);
					
					List<EsclateToRawTableDTO> values = esclateToRawTable.getValues();
					bean.setDeletedEsclateRawList(esclateToRawTable.getDeltedEsclateClaimList());
					if(null != esclateToRawTable){
						bean.setEsclateToRawList(esclateToRawTable.getValues());
					}
					setEsclateStage();
					claimService.updateRawDetails(values, bean);
					buildSuccessLayout();
				}
			}
			
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					/*ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
					        "No", "yes", new ConfirmDialog.Listener() {
						
					            public void onClose(ConfirmDialog dialog) {
					            	if (dialog.isCanceled() && !dialog.isConfirmed()) {
					                	UI.getCurrent().removeWindow(popup);
					                } else {
					                    // User did not confirm
					                	dialog.close();
					                }
					            }
					        });
					
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
					buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
					Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
							.toString());
					Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
							.toString());
					yesButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							UI.getCurrent().removeWindow(popup);
						}
						});
					noButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							
						}
						});
			}
		});
	}
	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("Submit");
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
		hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}
	
	private void loadContainerDataSources(){
		Map<String, Object> referenceData = new HashMap<String, Object>();
		referenceData.put("category", masterService.getRawCategory(SHAConstants.RAW_CATEGORY_TYPE));
		esclateToRawTable.setReferenceData(referenceData);
	}
	
	public Boolean isValid(){
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		List<EsclateToRawTableDTO> esclateToRawTableList = this.esclateToRawTable.getValues();
		if(null != esclateToRawTableList && !esclateToRawTableList.isEmpty())
		{
			for (EsclateToRawTableDTO esclateToRawTableDTO : esclateToRawTableList) {
				
				if(!(null != esclateToRawTableDTO.getEsclateCategory() && !("").equals(esclateToRawTableDTO.getEsclateCategory().getValue())))
				{
					hasError = true;
					eMsg.append("Please choose Category for item no ").append(" ").append(esclateToRawTableDTO.getRollNo()).append("</br>");
					break;
				}
				else if(null != esclateToRawTableDTO.getEsclateCategory())
				{
					if(null == esclateToRawTableDTO.getEsclateSubCategory() && esclateToRawTableDTO.getIsSubCategoryAvailable()){
						hasError = true;
						eMsg.append("Please choose Subcategory for item no ").append(" ").append(esclateToRawTableDTO.getRollNo()).append("</br>");
						break;
					}
				}
			}
		}
		else
		{
			hasError = true;
			eMsg.append("Please enter escalate details").append("</br>");
		}
		if (hasError) {
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
		} 
		return hasError;
	}
	
	private List<EsclateToRawTableDTO> getEsclatedToRawList(String intimationNo)  {
		
		List<EsclateToRawTableDTO> esclateToRawList = new ArrayList<EsclateToRawTableDTO>();
		RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(intimationNo);
		SelectValue category = null;
		SelectValue subCategory = null;
		if(null != headerObj){
			List<RawInvsDetails> existingList = claimService.getRawDetailsByRawInvKey(headerObj.getKey());
			if(null != existingList && !existingList.isEmpty()){				
					for (RawInvsDetails rawInvObj : existingList) {
						EsclateToRawTableDTO esclateToRawTableDTO = new EsclateToRawTableDTO();
						category = new SelectValue();
						category.setId(null != rawInvObj.getRawCategory()?rawInvObj.getRawCategory().getKey():null);
						category.setValue(null != rawInvObj.getRawCategory()? rawInvObj.getRawCategory().getCategoryDescription():null);
						esclateToRawTableDTO.setEsclateCategory(category);
						subCategory = new SelectValue();
						subCategory.setId(null != rawInvObj.getRawSubCategory()?rawInvObj.getRawSubCategory().getKey():null);
						subCategory.setValue(null !=rawInvObj.getRawSubCategory()?rawInvObj.getRawSubCategory().getSubCategoryDescription():null);
						esclateToRawTableDTO.setEsclateSubCategory(subCategory);
						esclateToRawTableDTO.setEsclateToRawRemarks(rawInvObj.getRequestedRemarks());
						esclateToRawList.add(esclateToRawTableDTO);
						esclateToRawTableDTO.setKey(rawInvObj.getKey());
						esclateToRawTableDTO.setRecordType(rawInvObj.getRecordType());
					}
			}
		}
		return esclateToRawList;
	}
	
	public void buildSuccessLayout() {
		
		Label successLabel = new Label("<b style = 'color: black;'>Claim escalated successfully !!!</b>", ContentMode.HTML);
		
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("<b style = 'color: black;'>Claim escalated successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				popup.close();
			}
		});
	}
	public void setEsclateStage()
	{
		if(SHAConstants.PROCESS_PREAUTH.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.PREAUTH_STAGE);
		}
		else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		}
		else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		}
		else if(SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		}
		else if(SHAConstants.BILLING.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.BILLING_STAGE);
		}
		else if(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString)){
			bean.setEsclateStageKey(ReferenceTable.FINANCIAL_STAGE);
		}
	}
		
	private List<EsclateToRawTableDTO> getRepliedRawList(String intimationNo){
		List<EsclateToRawTableDTO> repliedRawList = new ArrayList<EsclateToRawTableDTO>();
		RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(intimationNo);
		SelectValue category = null;
		SelectValue subCategory = null;
		if(null != headerObj){
			List<RawInvsDetails> existingList = claimService.getRepliedRawData(headerObj.getKey());
			if(null != existingList && !existingList.isEmpty()){				
					for (RawInvsDetails rawInvObj : existingList) {
						EsclateToRawTableDTO esclateToRawTableDTO = new EsclateToRawTableDTO();
						category = new SelectValue();
						if(null != rawInvObj.getRawCategory()){
							category.setId(rawInvObj.getRawCategory().getKey());
							category.setValue(rawInvObj.getRawCategory().getCategoryDescription());
							esclateToRawTableDTO.setEsclateCategory(category);
							esclateToRawTableDTO.setCategory(rawInvObj.getRawCategory().getCategoryDescription());
						}
						subCategory = new SelectValue();
						if(null != rawInvObj.getRawSubCategory()){
							subCategory.setId(rawInvObj.getRawSubCategory().getKey());
							subCategory.setValue(rawInvObj.getRawSubCategory().getSubCategoryDescription());
							esclateToRawTableDTO.setEsclateSubCategory(subCategory);
							esclateToRawTableDTO.setSubCategory(rawInvObj.getRawSubCategory().getSubCategoryDescription());
						}
						esclateToRawTableDTO.setRemarksToRaw(rawInvObj.getRequestedRemarks());
						esclateToRawTableDTO.setResolutionFromRaw(rawInvObj.getRedolutionType());
						esclateToRawTableDTO.setRemarksFromRaw(rawInvObj.getRepliedRemarks());
						repliedRawList.add(esclateToRawTableDTO);
					}
			}
		}
		return repliedRawList;
	}
}
