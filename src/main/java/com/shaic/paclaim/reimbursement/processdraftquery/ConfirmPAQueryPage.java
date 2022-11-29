package com.shaic.paclaim.reimbursement.processdraftquery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class ConfirmPAQueryPage extends ViewComponent{

	
	ReimbursementQueryDto bean;


	public void init(ClaimQueryDto bean) {
		this.bean = bean.getReimbursementQueryDto();
	}
	
	public Component getContent() {
		
		VerticalLayout queryLetterLayout = new VerticalLayout();
		queryLetterLayout.setWidth("100%");
		queryLetterLayout.setHeight("400px");
		
		VerticalLayout dischargeLetterLayout = new VerticalLayout();
		dischargeLetterLayout.setWidth("100%");
		dischargeLetterLayout.setHeight("400px");
		
		HorizontalLayout lettersLayout = new HorizontalLayout();
		lettersLayout.setSizeFull();
				
		DocumentGenerator docGenarator = new DocumentGenerator();
		
		List<ReimbursementQueryDto> a_beanList = new ArrayList<ReimbursementQueryDto>();
		
		Panel vPanel = new Panel();
		vPanel.setHeight("100%");
				
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(bean.getReimbursementDto().getClaimDto().getClaimId());
		String filePath = "";
		
		String templateName = "PAMedicalQueryLetter";
		if(("Y").equalsIgnoreCase(this.bean.getQueryType())){ // For Payment Query  =>  query Type - is "Y" Flag
			templateName = "PAPaymentQueryLetter";
			vPanel.setHeight("100%");
			String amtWords = "";
			if(null != bean.getReimbursementDto().getClaimApprovalAmount() )
			{
				amtWords = SHAUtils.getParsedAmount(bean.getReimbursementDto().getClaimApprovalAmount());
			}
			bean.setPaApprovedAmtInwords(amtWords);
			
			
			List<ReimbursementQueryDto> beanList = new ArrayList<ReimbursementQueryDto>();

			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
							|| (this.bean.getReimbursementDto().getBenefitSelected() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(this.bean.getReimbursementDto().getBenefitSelected().getId()))
							|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
									||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId())))
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
							beanList = new ArrayList<ReimbursementQueryDto>();
							beanList.add(bean);
							reportDto.setBeanList(beanList);
							filePath = docGenarator.generatePdfDocument(templateName, reportDto);
							try{
								File fl = new File(filePath);
								filelistForMerge.add(fl);
							}
							catch(Exception e) {
//								e.printStackTrace();
							}
						}						
						if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
							File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
							filePath =  mergedDoc.getAbsolutePath();
						}
						
					}
					else {
						a_beanList = new ArrayList<ReimbursementQueryDto>();
						a_beanList.add(bean);
						reportDto.setBeanList(a_beanList);
						filePath = docGenarator.generatePdfDocument(templateName, reportDto);
						
					}
				}	
				else {
					a_beanList = new ArrayList<ReimbursementQueryDto>();
					a_beanList.add(bean);
					reportDto.setBeanList(a_beanList);
					filePath = docGenarator.generatePdfDocument(templateName, reportDto);
					
				}
				
			}	
			else {
				a_beanList = new ArrayList<ReimbursementQueryDto>();
				a_beanList.add(bean);
				reportDto.setBeanList(a_beanList);
				filePath = docGenarator.generatePdfDocument(templateName, reportDto);
				
			}
			
		}
		
		else {			
			
			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
							|| (this.bean.getReimbursementDto().getBenefitSelected() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(this.bean.getReimbursementDto().getBenefitSelected().getId()))
							|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementDto().getPatientStatusId()) 
									||ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementDto().getPatientStatusId())))
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
							a_beanList = new ArrayList<ReimbursementQueryDto>();
							a_beanList.add(bean);
							reportDto.setBeanList(a_beanList);
							filePath = docGenarator.generatePdfDocument(templateName, reportDto);
							try{
								File fl = new File(filePath);
								filelistForMerge.add(fl);
							}
							catch(Exception e) {
//								e.printStackTrace();
							}
						}						
						if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
							File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
							filePath =  mergedDoc.getAbsolutePath();
						}
						
					}
					else {
						a_beanList = new ArrayList<ReimbursementQueryDto>();
						a_beanList.add(bean);
						reportDto.setBeanList(a_beanList);
						filePath = docGenarator.generatePdfDocument(templateName, reportDto);
						
					}
				}	
				else {
					a_beanList = new ArrayList<ReimbursementQueryDto>();
					a_beanList.add(bean);
					reportDto.setBeanList(a_beanList);
					filePath = docGenarator.generatePdfDocument(templateName, reportDto);
					
				}
				
			}	
		else{
			a_beanList = new ArrayList<ReimbursementQueryDto>();
			a_beanList.add(bean);
			reportDto.setBeanList(a_beanList);
			filePath = docGenarator.generatePdfDocument(templateName, reportDto);
		}	
			
		}		
		
		bean.setDocFilePath(filePath);
		bean.setDocType(SHAConstants.REIMBURSEMENT_QUERY_LETTER);
		Path queryFilePath = Paths.get(filePath);
		
		Embedded queryEmbedObj = getEmbededLetterObj(queryFilePath,filePath);
		
		vPanel.setContent(queryEmbedObj);
		
		queryLetterLayout.addComponent(vPanel);
		

		//IMSSUPPOR-27713 - As discussed with Laxmi/Nofel commented below code - 24-sep-2019
		if((("Y").equalsIgnoreCase(this.bean.getQueryType()) || this.bean.getGenerateDisVoucher()) /*&& 
				(((null != this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName()) && !this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName().isEmpty() /*&& 
						(null != this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeAddr()) && !this.bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeAddr().isEmpty())|| 
						((null != this.bean.getReimbursementDto().getNomineeName()) && !this.bean.getReimbursementDto().getNomineeName().isEmpty() && 
								(null != this.bean.getReimbursementDto().getNomineeAddr()) && !this.bean.getReimbursementDto().getNomineeAddr().isEmpty()))*/){
		
			String dischargeTemplateName = "PADischargeVoucher";
			
			String dischargefilePath = "";
			
			List<ReimbursementQueryDto> beanList = new ArrayList<ReimbursementQueryDto>();

			if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
							|| this.bean.getReimbursementDto().getBenefitSelected() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(this.bean.getReimbursementDto().getBenefitSelected().getId()))
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
				
				if(bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null ||
						bean.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
					
					List<LegalHeirDTO> legalHeirList = this.bean.getReimbursementDto().getLegalHeirDTOList();
					if(legalHeirList != null && !legalHeirList.isEmpty()) {
						Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
						ArrayList<File> filelistForMerge = new ArrayList<File>();
												
						beanList = new ArrayList<ReimbursementQueryDto>();
						for (LegalHeirDTO legalHeirDTO : legalHeirList) {
							bean.getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
							bean.getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
							beanList = new ArrayList<ReimbursementQueryDto>();
							beanList.add(bean);
							reportDto.setBeanList(beanList);
							dischargefilePath = docGenarator.generatePdfDocument(dischargeTemplateName, reportDto);
							try{
								File fl = new File(dischargefilePath);
								filelistForMerge.add(fl);
							}
							catch(Exception e) {
//								e.printStackTrace();
							}
						}
						
						if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
							File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
							dischargefilePath =  mergedDoc.getAbsolutePath();
						}				
						
					}
					else {
						a_beanList = new ArrayList<ReimbursementQueryDto>();
						a_beanList.add(bean);
						reportDto.setBeanList(a_beanList);
						dischargefilePath = docGenarator.generatePdfDocument(dischargeTemplateName, reportDto);
					}
					
				}
				else {
					a_beanList = new ArrayList<ReimbursementQueryDto>();
					a_beanList.add(bean);
					reportDto.setBeanList(a_beanList);
					dischargefilePath = docGenarator.generatePdfDocument(dischargeTemplateName, reportDto);
				}
				
			}
			else {
				a_beanList = new ArrayList<ReimbursementQueryDto>();
				a_beanList.add(bean);
				reportDto.setBeanList(a_beanList);
				dischargefilePath = docGenarator.generatePdfDocument(dischargeTemplateName, reportDto);
			}	
			
			
			bean.setDischargeVoucherFilePath(dischargefilePath);
			bean.setDischargeVoucherDocType(SHAConstants.DISCHARGE_VOUCHER_LETTER);
			Path dischargeVoucherPath = Paths.get(dischargefilePath);
			
			Embedded dischargeVoucherObj = getEmbededLetterObj(dischargeVoucherPath,dischargefilePath);
			
			Panel dishcargePanel = new Panel();
			dishcargePanel.setHeight("100%");
			dishcargePanel.setWidth("100%");
			dishcargePanel.setContent(dischargeVoucherObj);
			
			dischargeLetterLayout.addComponent(dishcargePanel);
			
			lettersLayout.addComponents(queryLetterLayout,dischargeLetterLayout);
			lettersLayout.setSpacing(true);
			return lettersLayout;
		}
		
		// Commented Below code As Part of CR2019118 Payment Qry Letter Changes 
		
		/*else if( SHAConstants.DEATH_FLAG.equalsIgnoreCase(this.bean.getReimbursementDto().getClaimDto().getIncidenceFlagValue()))
		{

			final String imageUrl = SHAFileUtils.viewFileByToken(bean.getReimbursementDto().getClaimDto().getDocToken().toString());
			if(null != imageUrl)
			{
				
				 Embedded e = new Embedded();
			        e.setSizeFull();
			        e.setType(Embedded.TYPE_BROWSER);
		         StreamResource.StreamSource source = new StreamResource.StreamSource() {
                        public InputStream getStream() {
                           
                        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        	InputStream is = null;
                        	URL u = null;
                        	URLConnection urlConnection = null;
                        	try {
//                        		u =  new URL(imageUrl);
//                        	  is = u.openStream();
                        		
                        		u =  new URL(imageUrl);
            					urlConnection =  u.openConnection();
            					is = urlConnection.getInputStream();
                        	  
//                        	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
            					byte[]  byteChunk = null;
        						if(urlConnection.getContentLength() > 25000){
        							byteChunk = new byte[25000];
        						} else {
        							byteChunk = new byte[urlConnection.getContentLength()];
        						}
                        	  int n;

                        	  while ( (n = is.read(byteChunk)) > 0 ) {
                        	    baos.write(byteChunk, 0, n);
                        	  }
                        	  byteChunk = null;
                        	}
                        	catch (IOException e) {
                        	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                        	  e.printStackTrace ();
                        	  // Perform any other exception handling that's appropriate.
                        	}
                        	finally {
                        	  if (is != null) {
                        		  try
                        		  {
                        			  is.close();
                        		  }
                        		  catch(Exception e)
                        		  {
                        			  e.printStackTrace();
                        		  }
                        		  }
                        	  if (null != urlConnection) {
    		               		  try
    		               		  {
    		               			urlConnection.getInputStream().close();
    		               		  }
    		               		  catch(Exception e)
    		               		  {
    		               			  e.printStackTrace();
    		               		  }
    		                   	}
                        	}
                        	return new ByteArrayInputStream(baos.toByteArray());
                        }
                };
                StreamResource r = new StreamResource(source, SHAConstants.DEATH_COVERING_LETTER);
               
                	r.setMIMEType("application/pdf");
               
                r.setFilename(SHAConstants.DEATH_COVERING_LETTER);
                r.setStreamSource(source);
                e.setSource(r);
                
                Panel coveringPanel = new Panel();
                coveringPanel.setHeight("100%");
                coveringPanel.setWidth("100%");
                coveringPanel.setContent(e);
    			
    			dischargeLetterLayout.addComponent(coveringPanel);
    			
    			lettersLayout.addComponents(queryLetterLayout,dischargeLetterLayout);
    			lettersLayout.setSpacing(true);
    			return lettersLayout;
           
               // getUI().getPage().open(r, "_blank", false);
//			    e.setSource(r);
//			    showPopup(e);
				
				//Button saveExcel = new Button();
				//AdvancedFileDownloader adv = new AdvancedFileDownloader();
				//Resource res = new FileResource(new File(imageUrl));
				final ExternalResource res = new ExternalResource(imageUrl);
			
				//res.setMIMEType("application/pdf");
				FileDownloader fd = new FileDownloader(res);
				//fd.setOverrideContentType(false);
				//fd.setFileDownloadResource(res);
				fd.extend(editButton);
				
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+ bean.getFileName();
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(res);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s, bean.getFileName());
		        r.setMIMEType("application/pdf");
		       // Resource downloadedResources = fd.getFileDownloadResource();
		        
		     //   e.setSource(fd.getFileDownloadResource());
		        e.setSource(r);
		        showPopup(e);           	
    	  }
					
		}*/

		return queryLetterLayout;
	}
	
	private Embedded getEmbededLetterObj(Path filePath,final String fileLocation){
		try{
		
		final String fileName = filePath.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(fileLocation);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource queryLetterResouce = new StreamResource(s, fileName);
		Embedded queryEmbedObj = new Embedded();
		queryEmbedObj.setSizeFull();
		queryEmbedObj.setType(Embedded.TYPE_BROWSER);
		queryLetterResouce.setMIMEType("application/pdf");
		queryEmbedObj.setSource(queryLetterResouce);
		
		return queryEmbedObj;
		}
		catch(Exception E){
			E.printStackTrace();
		}
		
		return null;
	}
	
}
