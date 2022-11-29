/**
 * 
 */
package com.shaic.claim.rod.wizard.forms;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.ws.security.util.StringUtil;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class AcknowledgeReceiptPage extends ViewComponent {
	
private static final long serialVersionUID = 7618109414654462771L;
	
	ReceiptOfDocumentsDTO bean;
	
	@EJB 
	private PreauthService preauthService;
	
	//HorizontalLayout horizontalLayout ;
	VerticalLayout vLayout;


	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;

	}


	
	public Component getContent() {
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();

		if(bean.getDocumentDetails().getDocumentsReceivedFrom() != null
				&& bean.getDocumentDetails().getDocumentsReceivedFrom().getId() != null
				&& bean.getDocumentDetails().getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			String hospitAddress = this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getAddress();
			String[] hospAddress = StringUtil.split(hospitAddress,',');
			int length;
			if(hospAddress.length != 0 ){
				length=hospAddress.length;			
				this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr1(hospAddress[0]);
				if(length >2){
					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr2(hospAddress[1]);
				}
				if(length >3){
					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr3(hospAddress[2]);
				}
				if(length >4){
					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr4(hospAddress[3]);
				}
			}
		}	
		
		
		
		List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
		rodDTOList.add(this.bean);		
		reportDto.setClaimId(bean.getClaimDTO().getClaimId());
				
//		reportDto.setBeanList(rodDTOList);		
//		final String filePath = docGen.generatePdfDocument("AckReceipt", reportDto);
		
		String filePath = "";

		String templateName = "AckReceipt";
		SelectValue docReceivedSelect = this.bean.getDocumentDetails().getDocumentsReceivedFrom();

		if(bean != null 
				&& ((SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
						&& (bean.getReconsiderRODdto() != null || bean.isQueryReplyStatus()))
						||((bean.getReconsiderRODdto() != null && bean.getReconsiderRODdto().getPatientStatus() != null && (ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReconsiderRODdto().getPatientStatus().getId())
								|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReconsiderRODdto().getPatientStatus().getId())))
						||(bean.isQueryReplyStatus() && bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus() != null && (ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().getId())
								|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().getId()))))
					)	
				&& docReceivedSelect != null
				&& docReceivedSelect.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)
				&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
			
			if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() == null
					|| bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) {
			
				List<LegalHeirDTO> legalHeirList = bean.getPreauthDTO().getLegalHeirDTOList();
				if(legalHeirList != null && !legalHeirList.isEmpty()) {
					Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
					ArrayList<File> filelistForMerge = new ArrayList<File>();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirList) {
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirDTO.getHeirName());
						bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
						rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
						rodDTOList.add(bean);
						reportDto.setBeanList(rodDTOList);
						filePath = docGen.generatePdfDocument(templateName, reportDto);
						try{
							File fl = new File(filePath);
							filelistForMerge.add(fl);
						}
						catch(Exception e) {
//							e.printStackTrace();
						}
					}						
					if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
						File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getClaimDTO().getClaimId().replaceAll("/", "_"));
						filePath =  mergedDoc.getAbsolutePath();
					}
					
				}
				
			}	
			else {
				rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
				rodDTOList.add(bean);
				reportDto.setBeanList(rodDTOList);
				filePath = docGen.generatePdfDocument(templateName, reportDto);
				
			}
			
		}	
		else {
			rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
			rodDTOList.add(bean);
			reportDto.setBeanList(rodDTOList);
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			
		}	
		
		
		this.bean.setDocFilePath(filePath);
		this.bean.setDocType(SHAConstants.ACK_RECEIPT);
		
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
		//e.setSizeFull();
		e.setWidth("100%");
		e.setHeight("100%");
		
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		com.vaadin.ui.Panel vPanel = new com.vaadin.ui.Panel();
		vPanel.setHeight("100%");
		vPanel.setContent(e);
		vLayout = new VerticalLayout();
		vLayout.setWidth("100%");
		vLayout.setHeight("400px");
		//vLayout.setHeight("100%");
		vLayout.addComponent(vPanel);
		/*horizontalLayout = new HorizontalLayout(e);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("100%");*/

		return vLayout;
	}

	public void resetPage()
	{		
		if(null != vLayout)
			vLayout.removeAllComponents();
	}



	public void setClearReferenceData() {
		 if(null != vLayout){
			 vLayout.removeAllComponents();
		 }
	}

}
