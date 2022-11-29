package com.shaic.claim.withdrawWizard;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class WithdrawPreauthPdfPage extends ViewComponent implements WizardStep<WithdrawPreauthPageDTO> {
	
	private WithdrawPreauthPageDTO bean;
	
//	private Label txtLabel;
	
	public static String dataDir = System.getProperty("jboss.server.data.dir");
	
	private CheckBox validateLetterChk;

	@Override
	public void init(WithdrawPreauthPageDTO bean) {
		this.bean=bean;
		
	}
	
	@Override
	public String getCaption() {
		return "Decision Communication";
	}

	public Component getContent()
	{
		/*Panel panel = new Panel();
		panel.setCaption("Withdrawal of Authorization Letter");
       // panel.setSizeFull();
        panel.setHeight("450px");*/
        
        DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		if(this.bean.getPreauthDto().getPreauthDataExtractionDetails() != null && this.bean.getPreauthDto().getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDto().getPreauthDataExtractionDetails().getDiagnosisTableList();
			String diagnosis = "";
			if(!diagnosisList.isEmpty()){
			for(DiagnosisDetailsTableDTO diagnosisDto : diagnosisList){
				
				if(diagnosis.equals("")){
					diagnosis = diagnosisDto.getDiagnosisName().getValue();
				}
				else{
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? diagnosisDto.getDiagnosisName().getValue() : " - " ) ;
				}
			}
			}
			if(diagnosis != null && !diagnosis.equals("")){
			this.bean.getPreauthDto().getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
		if(this.bean.getPreviousPreAuthTableDTO() != null && !this.bean.getPreviousPreAuthTableDTO().isEmpty()){
			// IMSSUPPOR-28105  --- IMSSUPPOR-28458 commented for jira - IMSSUPPOR-28726
//			this.bean.getClaimDto().setPreauthApprovedDate(this.bean.getPreviousPreAuthTableDTO().get(0).getModifiedDate() != null ? this.bean.getPreviousPreAuthTableDTO().get(0).getModifiedDate() : this.bean.getPreviousPreAuthTableDTO().get(0).getCreatedDate());
		}
		this.bean.getPreauthDto().setStrUserName(this.bean.getPreauthDto().getStrUserName() != null ? this.bean.getPreauthDto().getStrUserName().toUpperCase() : "" );
		if(bean.getPreauthDto().getNewIntimationDTO() != null 
				&& bean.getPreauthDto().getNewIntimationDTO().getRoomCategory() == null ){

			if(bean.getPreauthDto().getPreauthDataExtractionDetails()!= null
					&& bean.getPreauthDto().getPreauthDataExtractionDetails().getRoomCategory() !=null){
				bean.getPreauthDto().getNewIntimationDTO().setRoomCategory(bean.getPreauthDto().getPreauthDataExtractionDetails().getRoomCategory());
			}

		}
		List<PreauthDTO> withdrawPreauthPageDTOList = new ArrayList<PreauthDTO>();
		withdrawPreauthPageDTOList.add(this.bean.getPreauthDto());		
		reportDto.setClaimId(bean.getClaimDto().getClaimId());
		reportDto.setBeanList(withdrawPreauthPageDTOList);
		 String filepath = "";
		if((ReferenceTable.STANDALONE_WITHDRAW_STATUS).equals(bean.getStatusKey())) {
			
			String templateName = "PreauthWithDrawLetter_NonGMC"; 
			
			if((ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDto().getPolicy().getProduct().getKey()))){
				templateName = "PreauthWithDrawLetter";
			}			
			
			filepath = docGen.generatePdfDocument(templateName, reportDto);
			
		} else if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
			
//			String templateName = "PreauthWithDrawAndRejectionLetter_NonGMC";
			
			String templateName = "CashlessRejectAndWithDrawLetter_NonGMC";   // CR R20181313
			
			if((ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDto().getPolicy().getProduct().getKey()))){
				templateName = "PreauthWithDrawAndRejectionLetter";
			}
			
			filepath = docGen.generatePdfDocument(templateName, reportDto);
		}
		   
        if(null != bean.getPreauthDto())
        {
        	if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)) {
        		bean.getPreauthDto().setDocFilePath(filepath);
            	bean.getPreauthDto().setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_LETTER);
            	bean.getPreauthDto().setDocSource(SHAConstants.WITHDRAW_PRE_AUTH);
        	} else if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
        		bean.getPreauthDto().setDocFilePath(filepath);
            	bean.getPreauthDto().setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_AND_REJECTION_LETTER);
            	bean.getPreauthDto().setDocSource(SHAConstants.WITHDRAW_PRE_AUTH);
        	}
        }
		
		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();
		final String filePathForListener = filepath;
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filePathForListener);
		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 9138325634649289303L;

			public InputStream getStream() {
				try {

					File f = new File(filePathForListener);
					System.out.println(f.getCanonicalPath());
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
//		panel.setContent(e);
//		return panel;
		
		validateLetterChk = new CheckBox();

		validateLetterChk.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	
                if(event.getProperty().getValue() != null)
                   	bean.getPreauthDto().setLetterContentValidated((boolean)(event.getProperty().getValue()));
			}
		});
		
        Label chkLbl = new Label("<B>I confirm that the Contents of the Letter have been reviewed and are found to be in Order</B>",
                        ContentMode.HTML);
        HorizontalLayout validatechkLayout = new HorizontalLayout(validateLetterChk, chkLbl);
        
        VerticalSplitPanel panel = new VerticalSplitPanel();
		panel.setSplitPosition(90.0f, Unit.PERCENTAGE);
		panel.setLocked(true);
		panel.setFirstComponent(e);
		panel.setSecondComponent(validatechkLayout);
		panel.setHeight("450px");

		return panel;
	}
//	@Override
//	public Component getContent() {
//		panel=new Panel();
//		panel.setCaption("Withdrawal of Authorization Letter");
//        panel.setSizeFull();
//        panel.setHeight("100%");
//		
////		DocumentGenerator docGenarator = new DocumentGenerator();
////		
////		String fileUrl = null;
////		fileUrl = docGenarator.generateCoveringLetter("ClaimFormCoveringLetter", null);
////		//ClaimFormCoveringLetter
//		final String filepath=System.getProperty("jboss.server.data.dir");
//	
//		
//		//Path p = Paths.get(filepath);
//		final String fileURL ="file:///" + filepath + "//ClaimFormCoveringLetter.pdf";
//
//		StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//			public FileInputStream getStream() {
//				try {
//
//					File f = new File(fileURL);
//					FileInputStream fis = new FileInputStream(f);
//					return fis;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//		};
//
//		StreamResource r = new StreamResource(s, fileURL);
//		Embedded e = new Embedded();
//		e.setSizeFull();
//		e.setType(Embedded.TYPE_BROWSER);
//		r.setMIMEType("application/pdf");
//		e.setSource(r);
//        panel.setContent(e);
//		return panel;
//	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {

		if(bean.getPreauthDto().isLetterContentValidated())
			return true;
		else {
			
			MessageBox.createInfo()
	    	.withCaptionCust(SHAConstants.INFO_TITLE).withHtmlMessage(SHAConstants.VALIDATE_LETTER_CONTENT_MSG)
	        .withOkButton(ButtonOption.caption("OK")).open();
		}
		return false;
		
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	

}
