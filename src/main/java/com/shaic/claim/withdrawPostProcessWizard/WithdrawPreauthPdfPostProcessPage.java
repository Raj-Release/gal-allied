package com.shaic.claim.withdrawPostProcessWizard;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.bea.objectweb.asm.Label;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class WithdrawPreauthPdfPostProcessPage extends ViewComponent implements WizardStep<WithdrawPreauthPostProcessPageDTO> {

	private WithdrawPreauthPostProcessPageDTO bean;

	private Label txtLabel;

	public static String dataDir = System.getProperty("jboss.server.data.dir");

	@Override
	public void init(WithdrawPreauthPostProcessPageDTO bean) {
		this.bean=bean;

	}

	@Override
	public String getCaption() {
		return "Decision Communication";
	}

	public Component getContent()
	{
		Panel panel = new Panel();
		panel.setCaption("Withdrawal of Authorization Letter");
		// panel.setSizeFull();
		panel.setHeight("450px");

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
			// IMSSUPPOR-28105 ---IMSSUPPOR-28458 commented for jira - IMSSUPPOR-28726
//			this.bean.getClaimDto().setPreauthApprovedDate(this.bean.getPreviousPreAuthTableDTO().get(0).getModifiedDate() != null ? this.bean.getPreviousPreAuthTableDTO().get(0).getModifiedDate() : this.bean.getPreviousPreAuthTableDTO().get(0).getCreatedDate());	
			}
		this.bean.getPreauthDto().setStrUserName(this.bean.getPreauthDto().getStrUserName() != null ? this.bean.getPreauthDto().getStrUserName().toUpperCase() : "" );
		List<PreauthDTO> withdrawPreauthPageDTOList = new ArrayList<PreauthDTO>();
		withdrawPreauthPageDTOList.add(this.bean.getPreauthDto());		
		reportDto.setClaimId(bean.getClaimDto().getClaimId());
		reportDto.setBeanList(withdrawPreauthPageDTOList);
		String filepath = "";
		if((ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS).equals(bean.getStatusKey())) {

			String templateName = "PreauthWithDrawLetter_NonGMC";

			if((ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDto().getPolicy().getProduct().getKey()))){
				templateName = "PreauthWithDrawLetter";
			}			

			filepath = docGen.generatePdfDocument(templateName, reportDto);
		}

//		} else if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
//
//			String templateName = "PreauthWithDrawAndRejectionLetter_NonGMC";
//
//			if((ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDto().getPolicy().getProduct().getKey()))){
//				templateName = "PreauthWithDrawAndRejectionLetter";
//			}
//
//			filepath = docGen.generatePdfDocument(templateName, reportDto);
//		}

		if(null != bean.getPreauthDto())
		{
			if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
				bean.getPreauthDto().setDocFilePath(filepath);
				bean.getPreauthDto().setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_LETTER);
				bean.getPreauthDto().setDocSource(SHAConstants.WITHDRAW_PRE_AUTH);
			} 
//			else if(bean.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
//				bean.getPreauthDto().setDocFilePath(filepath);
//				bean.getPreauthDto().setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_AND_REJECTION_LETTER);
//				bean.getPreauthDto().setDocSource(SHAConstants.WITHDRAW_PRE_AUTH);
//			}
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
		panel.setContent(e);
		return panel;
	}
	//		@Override
	//		public Component getContent() {
	//			panel=new Panel();
	//			panel.setCaption("Withdrawal of Authorization Letter");
	//	        panel.setSizeFull();
	//	        panel.setHeight("100%");
	//			
	////			DocumentGenerator docGenarator = new DocumentGenerator();
	////			
	////			String fileUrl = null;
	////			fileUrl = docGenarator.generateCoveringLetter("ClaimFormCoveringLetter", null);
	////			//ClaimFormCoveringLetter
	//			final String filepath=System.getProperty("jboss.server.data.dir");
	//	
	//			
	//			//Path p = Paths.get(filepath);
	//			final String fileURL ="file:///" + filepath + "//ClaimFormCoveringLetter.pdf";
	//
	//			StreamResource.StreamSource s = new StreamResource.StreamSource() {
	//
	//				public FileInputStream getStream() {
	//					try {
	//
	//						File f = new File(fileURL);
	//						FileInputStream fis = new FileInputStream(f);
	//						return fis;
	//
	//					} catch (Exception e) {
	//						e.printStackTrace();
	//						return null;
	//					}
	//				}
	//			};
	//
	//			StreamResource r = new StreamResource(s, fileURL);
	//			Embedded e = new Embedded();
	//			e.setSizeFull();
	//			e.setType(Embedded.TYPE_BROWSER);
	//			r.setMIMEType("application/pdf");
	//			e.setSource(r);
	//	        panel.setContent(e);
	//			return panel;
	//		}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onAdvance() {
		return true;
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

