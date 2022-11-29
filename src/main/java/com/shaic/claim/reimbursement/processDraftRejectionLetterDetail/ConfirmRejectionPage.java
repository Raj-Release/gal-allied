package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class ConfirmRejectionPage extends ViewComponent{

	
	ReimbursementRejectionDto bean;


	public void init(ClaimRejectionDto bean) {
		this.bean = bean.getReimbursementRejectionDto();
		

	}

	
	public Component getContent() {
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setWidth("100%");
		vLayout.setHeight("400px");
		
		DocumentGenerator docGenarator = new DocumentGenerator();
		
		List<ReimbursementRejectionDto> a_beanList = new ArrayList<ReimbursementRejectionDto>();
		this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().setInsuredAge(this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredFullAge());
//		a_beanList.add(bean);
		
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(bean.getReimbursementDto().getClaimDto().getClaimId());
//		reportDto.setBeanList(a_beanList);
//		final String filePath = docGenarator.generatePdfDocument("ReimbursementRejectionLetter", reportDto);
		
		String filePath = "";
		
		String templateName = "ReimbursementRejectionLetter";
		
		if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
								||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null ||
					bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
				
				List<LegalHeirDTO> legalHeirList = this.bean.getReimbursementDto().getLegalHeirDTOList();
				if(legalHeirList != null && !legalHeirList.isEmpty()) {
					Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
					ArrayList<File> filelistForMerge = new ArrayList<File>();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirList) {
						bean.getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
						bean.getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
						a_beanList = new ArrayList<ReimbursementRejectionDto>();
						a_beanList.add(bean);
						reportDto.setBeanList(a_beanList);
						filePath = docGenarator.generatePdfDocument(templateName, reportDto);
						try{
							File fl = new File(filePath);
							filelistForMerge.add(fl);
						}
						catch(Exception e) {
//							e.printStackTrace();
						}
					}						
					if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
						File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
						filePath =  mergedDoc.getAbsolutePath();
					}
					
				}
				
			}	
			else {
				a_beanList = new ArrayList<ReimbursementRejectionDto>();
				a_beanList.add(bean);
				reportDto.setBeanList(a_beanList);
				filePath = docGenarator.generatePdfDocument(templateName, reportDto);
				
			}
			
		}	
		else {
			a_beanList = new ArrayList<ReimbursementRejectionDto>();
			a_beanList.add(bean);
			reportDto.setBeanList(a_beanList);
			filePath = docGenarator.generatePdfDocument(templateName, reportDto);
			
		}
		//Adding this for uploading rejection letter to DMS.
		this.bean.setDocFilePath(filePath);
		this.bean.setDocType(SHAConstants.REIMBURSEMENT_REJECTION_LETTER);
		Path p = Paths.get(filePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filePath);
		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filePath);
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
		Panel vPanel = new Panel();
		vPanel.setHeight("100%");
		vPanel.setContent(e);
		
		vLayout.addComponent(vPanel);		

		return vLayout;
	}
	
	public void clearObject(){
		bean = null;
	}
}
