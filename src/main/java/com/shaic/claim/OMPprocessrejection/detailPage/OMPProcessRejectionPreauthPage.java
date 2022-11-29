//package com.shaic.claim.OMPprocessrejection.detailPage;
//
//import java.util.Iterator;
//
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.ViewComponent;
//
//import com.shaic.arch.EnhancedFieldGroupFieldFactory;
//import com.shaic.claim.ViewDetails;
//import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
//import com.shaic.newcode.wizard.dto.NewIntimationDto;
//import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
//import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickListener;
//import com.vaadin.v7.ui.ComboBox;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.ui.Panel;
//import com.vaadin.v7.ui.TextArea;
//import com.vaadin.v7.ui.TextField;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.themes.ValoTheme;
//
//public class OMPProcessRejectionPreauthPage extends ViewComponent {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	@Inject
//	private ProcessRejectionDTO bean;
//	
//	private BeanFieldGroup<ProcessRejectionDTO> binder;
//	
//	@Inject
//	private ViewDetails viewDetails;
//	
//	private TextField premedicalSuggestionTxt;
//	
//	private TextField premedicalCategory;
//	
//	private TextArea rejectionRemarks;
//	
//	private FormLayout mainForm;
//	
//	private NewIntimationDto intimationDto;
//	
//	private SearchProcessRejectionTableDTO searchDTO;
//	
//	@PostConstruct
//	public void initView(){
//		
//	}
//	
//	public void setReferenceData(ProcessRejectionDTO bean,NewIntimationDto intimationDto){
//		this.intimationDto=intimationDto;
//		this.bean=bean;
//		
//		this.searchDTO.getProcessRejectionDTO().setKey(bean.getKey());
//		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
//		this.binder.setItemDataSource(this.bean);
//		
//	}
//	
//	public void initView(SearchProcessRejectionTableDTO searchDTO){
//		
//		this.searchDTO = searchDTO;
//	
//		if(searchDTO.getStatusKey() == null){
//			
//			fireViewEvent(OMPProcessRejectionPresenter.OMP_SET_DATA,searchDTO);
//			
//		}else{
//			this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
//			this.binder.setItemDataSource(searchDTO.getProcessRejectionDTO());
//		}
//        
//		
//		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//		
//		premedicalSuggestionTxt=(TextField) binder.buildAndBind("Pre-Medical Suggestion","rejectionStatus", TextField.class);
//		
//		premedicalSuggestionTxt.setValue("Suggested Rejection");
//		
//		premedicalCategory=(TextField) binder.buildAndBind("Category","premedicalCategory", TextField.class);
//		premedicalCategory.setRequired(true);
//		
//		rejectionRemarks=(TextArea) binder.buildAndBind("Rejection Remarks (Pre-medical)","rejectionRemarks", TextArea.class);
//		rejectionRemarks.setRequired(true);
//		
//		mainForm=new FormLayout(premedicalSuggestionTxt,premedicalCategory,rejectionRemarks);
//		
//		setReadOnly(mainForm, true);
//		
//		Panel panel=new Panel();
//		panel.setCaption("Pre-medical Remarks");
//		panel.addStyleName("girdBorder");
//		panel.setContent(mainForm);
//		panel.setWidth("60%");
//		panel.setHeight("200px");
//		
//		setCompositionRoot(panel);
//		
//	}
//	
//	@SuppressWarnings({ "rawtypes", "deprecation" })
//	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
//		Iterator<Component> formLayoutLeftComponent = a_formLayout
//				.getComponentIterator();
//		while (formLayoutLeftComponent.hasNext()) {
//			Component c = formLayoutLeftComponent.next();
//			if (c instanceof TextField) {
//				TextField field = (TextField) c;
//				field.setNullRepresentation("");
//				field.setReadOnly(readOnly);
//				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//			}
//			else if (c instanceof ComboBox) {
//				ComboBox field = (ComboBox) c;
//				field.setReadOnly(readOnly);
//				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//			}
//			else if (c instanceof TextArea) {
//				TextArea field = (TextArea) c;
//				field.setReadOnly(readOnly);
//			}
//		}
//	}
//	
//
//}
