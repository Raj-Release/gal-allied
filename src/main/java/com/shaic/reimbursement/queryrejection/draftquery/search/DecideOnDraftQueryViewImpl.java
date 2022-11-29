//package com.shaic.reimbursement.queryrejection.draftquery.search;
//
//import java.util.Map;
//
//import javax.enterprise.event.Observes;
//import javax.enterprise.event.Reception;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.AbstractMVPView;
//import org.vaadin.addon.cdimvp.ParameterDTO;
//import org.vaadin.addon.cdiproperties.TextBundle;
//import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
//import org.vaadin.teemu.wizards.GWizard;
//
//import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
//import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
//import com.vaadin.ui.Component;
//
///**
// * 
// * @author Lakshminarayana
// *
// */
//public class DecideOnDraftQueryViewImpl extends AbstractMVPView implements DecideOnDraftQueryView{
//	
//	@Inject 
//	private DraftQueryLetterDetailPage decideOnDraftQueryDetailPage;
//	
//	private String strCaptionString;
//	
//	@Inject
//	private TextBundle tb;	
//	
//	private SearchDraftQueryLetterTableDTO bean;
//
//	@Override
//	public void resetView() {
//		
//	}
//
//	@Override
//	public void init(SearchDraftQueryLetterTableDTO bean) {
//
//		
//	}
//	
//	@Override
//	public void init(SearchDraftQueryLetterTableDTO bean, GWizard wizard) {
//		// TODO Auto-generated method stub
//		localize(null);
//		this.bean = bean;
//		decideOnDraftQueryDetailPage.init(bean,wizard);		
//		
//	}
//
//	@Override
//	public String getCaption() {
//		return strCaptionString;
//	}
//
//	@Override
//	public Component getContent() {
//		Component comp =  decideOnDraftQueryDetailPage.getContent();
//		
//		return comp;
//	}
//
//	@Override
//	public void setupReferences(Map<String, Object> referenceData) {
//		
//	}
//
//	@Override
//	public boolean onAdvance() {
//				
//		return true;
//		
//	}
//
//	@Override
//	public boolean onBack() {
//		
//		return true;
//	}
//
//	@Override
//	public boolean onSave() {
//		return false;
//	}
//
//	protected void localize(
//            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
//        strCaptionString = tb.getText(textBundlePrefixString() + "decideondraftquery");
//    }
//    
//	private String textBundlePrefixString()
//	{
//		return "queryrejection-";
//	}
//	
//	@Override
//	public void returnPreviousPage(SearchDraftQueryLetterTableDTO updatedBean) {
//		this.bean = updatedBean;
//		onBack();		
//	}
//	
//	@Override
//	public void getUpdatedBean() {
//		decideOnDraftQueryDetailPage.validatePage();
//	}
//
//	@Override
//	public void setUpdatedBean(SearchDraftQueryLetterTableDTO updatedBean) {
//		this.bean = updatedBean;
//		decideOnDraftQueryDetailPage.updateBean();
//		
//	}
//	
//	
//}
