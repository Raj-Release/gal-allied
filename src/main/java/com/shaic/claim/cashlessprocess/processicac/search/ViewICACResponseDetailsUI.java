package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODService;
import com.shaic.claim.intimation.CashLessTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ViewICACResponseDetailsUI extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8551456904860347414L;

	@Inject
	private SearchProcessICACTableDTO bean;

	private VerticalLayout verticalMain;

	private TextArea txtRemarks;
	
	private OptionGroup fiancalDecisionOption;
	
	@Inject
	private Instance<ViewProcessingDoctorDetailsTabel> processingDoctorDetailsInstance;

	private ViewProcessingDoctorDetailsTabel processingDoctorDeatilsTableObj;

	@Inject
	private Instance<ViewICACTeamResponseTabel>  icacProResponeTeamsInstance;

	private ViewICACTeamResponseTabel  icacProResponeTeamTableObj;
	
	@Inject
	private Instance<ViewICACTeamFinalResponseTabel> icacProFinalResTeamsInstance;

	private ViewICACTeamFinalResponseTabel icacProFinalResTeamTableObj;

	@EJB
	private DBCalculationService dbCalculationService;

	private TextField intimationNumber;

	private TextField clmType;

	private Label icacHLabel;

	private BeanFieldGroup<SearchProcessICACTableDTO> binder;
	

	@SuppressWarnings("deprecation")
	public void init(SearchProcessICACTableDTO bean) {

		this.bean=bean;
		this.binder = new BeanFieldGroup<SearchProcessICACTableDTO>(SearchProcessICACTableDTO.class);
		this.binder.setItemDataSource(bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		
		processingDoctorDeatilsTableObj = processingDoctorDetailsInstance.get();
		processingDoctorDeatilsTableObj.init("", false, false);
		processingDoctorDeatilsTableObj.setCaption("Processing Doctor Details");
		if(bean.getProcessingDoctorDetailsDTOs() !=null && !bean.getProcessingDoctorDetailsDTOs().isEmpty()){
			processingDoctorDeatilsTableObj.setTableList(bean.getProcessingDoctorDetailsDTOs());
		}	
		icacHLabel = new Label();
		icacHLabel.setCaption("ICAC Details");
		
		icacProResponeTeamTableObj = icacProResponeTeamsInstance.get();
		icacProResponeTeamTableObj.init("", false, false);
		icacProResponeTeamTableObj.setCaption("ICAC Response");
		if(bean.getProcessingICACTeamResponseDTO() !=null && !bean.getProcessingICACTeamResponseDTO().isEmpty()){
			icacProResponeTeamTableObj.setTableList(bean.getProcessingICACTeamResponseDTO());
		}
		
		fiancalDecisionOption = new OptionGroup();
		fiancalDecisionOption.setCaption("ICAC Final Decision");
		fiancalDecisionOption.addItems(getReadioButtonOptions());
		fiancalDecisionOption.setItemCaption(true, "Yes");
		fiancalDecisionOption.setItemCaption(false, "No");
		fiancalDecisionOption.setStyleName("horizontal");
		fiancalDecisionOption.setValue(false);
		if(bean.getFinailDecValue() != null && bean.getFinailDecValue()){
			fiancalDecisionOption.setValue(true);
		}
		fiancalDecisionOption.setReadOnly(true);
		


		fiancalDecisionOption.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				boolean selected = (boolean)event.getProperty().getValue();

			}
		});
		
		
		icacProFinalResTeamTableObj = icacProFinalResTeamsInstance.get();
		icacProFinalResTeamTableObj.init("", false, false);
		if(bean.getProcessingICACFinalTeamResDTO() !=null && !bean.getProcessingICACFinalTeamResDTO().isEmpty() && bean.getFinailDecValue()!= null && bean.getFinailDecValue()){
			icacProFinalResTeamTableObj.setTableList(bean.getProcessingICACFinalTeamResDTO());
		}
		
		VerticalLayout tableLayout = null;
		
		tableLayout = new VerticalLayout(commonTopLayout(),processingDoctorDeatilsTableObj,icacHLabel,icacProResponeTeamTableObj,fiancalDecisionOption,icacProFinalResTeamTableObj);
		tableLayout.setMargin(true);
		tableLayout.setSpacing(true);
		verticalMain = new VerticalLayout(tableLayout);
		verticalMain.setSizeFull();
		setCompositionRoot(verticalMain);
	}

	@SuppressWarnings("deprecation")
	public VerticalLayout commonTopLayout(){

		intimationNumber = (TextField) binder.buildAndBind("Intimation Number","intimationNo",TextField.class);	
		clmType = (TextField) binder.buildAndBind("Transaction Type","viewTransactoinType",TextField.class);
		
		FormLayout firstForm = new FormLayout(intimationNumber);
		FormLayout secondForm = new FormLayout(clmType);
		setReadOnly(firstForm,true);
		setReadOnly(secondForm,true);

		HorizontalLayout roomHLayout = new HorizontalLayout(firstForm,secondForm);
		VerticalLayout tableLayout = new VerticalLayout(roomHLayout);

		return tableLayout;
	}

	

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setHeight("29px");
				field.setNullRepresentation("-");
				/*correctionRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(correctionRemarks,null,getUI(),SHAConstants.DATA_CORECTION_VIEW);*/
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
}
