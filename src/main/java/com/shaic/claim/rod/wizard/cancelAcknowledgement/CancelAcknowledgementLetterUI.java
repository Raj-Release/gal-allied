package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.VerticalLayout;

public class CancelAcknowledgementLetterUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReceiptOfDocumentsDTO bean;
	
	private GWizard wizard;
	
	private HashMap fileMap;
	
	VerticalLayout vLayout;
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		this.fileMap = new HashMap();
	}
	
	
	public Component getContent() {
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();

		
//		if(this.bean.getDocumentDetails().getDocumentsReceivedFrom().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
//			String hospitAddress = this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getAddress();
//			String[] hospAddress = StringUtil.split(hospitAddress,',');
//			int length;
//			if(hospAddress.length != 0 ){
//				length=hospAddress.length;			
//				this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr1(hospAddress[0]);
//				if(length >2){
//					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr2(hospAddress[1]);
//				}
//				if(length >3){
//					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr3(hospAddress[2]);
//				}
//				if(length >4){
//					this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().setHospAddr4(hospAddress[3]);
//				}
//			}
//		}	
		
		
		
		List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
		rodDTOList.add(this.bean);		
		reportDto.setClaimId(bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(rodDTOList);
		final String filePath = docGen.generatePdfDocument("CancelAcknowledgement", reportDto);	
		
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
	
	
	
	

}
