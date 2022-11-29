package com.shaic.claim.premedical.listenerTables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateOtherClaimDetailsUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtInsuredName;
	
	private Button btnSubmit;
	
	private Button btnClose;
	
	@Inject
	public Instance<UpdateClaimDetailListenerTable> updateClaimDetailListenerTable;
	
	public UpdateClaimDetailListenerTable updateClaimDetailTableObj;
	
	private Window popup = null;
	
	private PreauthDTO bean;
	
	
	public void init(List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList,Map<String, Object> referenceData,PreauthDTO preauthDto, Window popup){
		
		
		txtInsuredName = new TextField("Insured Name");
		txtInsuredName.setValue(preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredName());
		txtInsuredName.setEnabled(false);
		this.popup = popup;
		this.bean = preauthDto;
		
		
		UpdateClaimDetailListenerTable updateClaimDetailTableInstance = updateClaimDetailListenerTable.get();
		//procedureTableInstance.init(bean.getHospitalKey(), "preauthEnhancement",diagList);
		updateClaimDetailTableInstance.init(preauthDto);
		this.updateClaimDetailTableObj = updateClaimDetailTableInstance;
		this.updateClaimDetailTableObj.setReferenceData(referenceData);
		
		for (UpdateOtherClaimDetailDTO updateOtherClaimDetailDTO : updateOtherClaimDetailList) {
			this.updateClaimDetailTableObj.addBeanToList(updateOtherClaimDetailDTO);
		}
		btnSubmit = new Button("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnClose = new Button("Cancel");
		btnClose.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnClose.setWidth("-1px");
		btnClose.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(btnSubmit,btnClose);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(new FormLayout(txtInsuredName),updateClaimDetailTableObj,buttonHor);
		mainVertical.setSpacing(true);
		mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		
		addListener();
		
		setCompositionRoot(mainVertical);
		
		
		
	}
	
	public void addListener(){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(updateClaimDetailTableObj.isValid()){
				
					List<UpdateOtherClaimDetailDTO> values = updateClaimDetailTableObj.getValues();
					Integer totalClaimedAmount = updateClaimDetailTableObj.getTotalClaimedAmount();
					Integer totalDeductibleAmount = updateClaimDetailTableObj.getTotalDeductibleAmount();
				    Integer totalApprovedAmount = updateClaimDetailTableObj.getTotalApprovedAmount();
				    
				    bean.setUpdateOtherClaimDetailDTO(values);
				    bean.setOtherInsurerClaimedAmount(totalClaimedAmount);
				    bean.setOtherInsurerDeductionAmt(totalDeductibleAmount);
				    bean.setOtherInsurerAdmissibleAmt(totalApprovedAmount);
				    popup.close();
				}else{
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = updateClaimDetailTableObj.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					   }
					
					/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
				}
				
			}
		});
		
		btnClose.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});
		
		
	}
	
	

}
