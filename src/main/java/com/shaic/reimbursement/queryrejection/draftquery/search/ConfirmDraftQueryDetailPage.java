package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
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

public class ConfirmDraftQueryDetailPage extends ViewComponent{

	
	ReimbursementQueryDto bean;


	public void init(SearchDraftQueryLetterTableDTO bean) {
		this.bean = bean.getReimbursementQueryDto();
	}
	
	public Component getContent() {
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setWidth("100%");
		vLayout.setHeight("400px");
				
		DocumentGenerator docGenarator = new DocumentGenerator();
		
		List<ReimbursementQueryDto> a_beanList = new ArrayList<ReimbursementQueryDto>();
//		a_beanList.add(bean);
		
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(bean.getReimbursementDto().getClaimDto().getClaimId());
//		reportDto.setBeanList(a_beanList);
//		String filePath = docGenarator.generatePdfDocument("ReimbursementQueryLetter", reportDto);
		
		String templateName = "ReimbursementQueryLetter"; 
		String filePath = "";
		//IMSSUPPOR-30472
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
						a_beanList = new ArrayList<ReimbursementQueryDto>();
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
		
		Path p = Paths.get(filePath);
		final String fileStoredPath = watermarkPDFDocument(p);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(fileStoredPath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(fileStoredPath);
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
	public String watermarkPDFDocument(Path filePath) {
		
		String src = filePath.getFileName().toString();
        String fileStoredPath = System.getProperty("jboss.server.data.dir")+File.pathSeparator + src.substring(4, src.length());
        PdfStamper stamper = null;
        PdfReader reader = null;
        try{
			reader = new PdfReader(filePath.toString());
	        int n = reader.getNumberOfPages();
	        FileOutputStream fs = new FileOutputStream(fileStoredPath);
	        stamper = new PdfStamper(reader, fs);
	        System.out.println("final file Path : "+ src.substring(4, src.length()));
	        stamper.setRotateContents(false);
	
	        // text watermark
	        Font f = FontFactory.getFont(FontFactory.TIMES_BOLD, 90);
	        
	        Phrase p = new Phrase(" D R A F T ", f);
	        // image watermark
	//        Image img = Image.getInstance(IMG);
	//        float w = img.getScaledWidth();
	//        float h = img.getScaledHeight();
	        // transparency
	        PdfGState gs1 = new PdfGState();
	        gs1.setFillOpacity(0.25f);
	        // properties
	        PdfContentByte over;
	        Rectangle pagesize;
	        float x, y;
	        // loop over every page
	        for (int i = 1; i <= n; i++) {
	            pagesize = reader.getPageSize(i);
	            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
	            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
	            over = stamper.getOverContent(i);
	            over.saveState();
	            over.setGState(gs1);
	            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 0);
	            over.restoreState();
	        }
	        stamper.close();
	        reader.close();
	        if(fs != null){
	        	fs.close();
	        }
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(stamper != null){
					stamper.close();
				}
				if(reader != null){
					reader.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
        
    return fileStoredPath;
	}
	
	public void clearObject(){
		bean = null;
	}
}
