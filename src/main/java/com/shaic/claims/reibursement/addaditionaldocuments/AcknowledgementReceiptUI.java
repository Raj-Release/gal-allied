package com.shaic.claims.reibursement.addaditionaldocuments;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.VerticalLayout;

public class AcknowledgementReceiptUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ReceiptOfDocumentsDTO receiptOfDocumentsDTO;

	VerticalLayout vLayout;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	public void init(ReceiptOfDocumentsDTO receiptOfDocumentsDTO) {
		this.receiptOfDocumentsDTO = receiptOfDocumentsDTO;
	}
	

	@SuppressWarnings("deprecation")
	public Component getContent() {

		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		generateAcknowledgeNo(this.receiptOfDocumentsDTO);

		List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
		rodDTOList.add(this.receiptOfDocumentsDTO);
		reportDto.setClaimId(receiptOfDocumentsDTO.getClaimDTO().getClaimId());
		reportDto.setBeanList(rodDTOList);
		final String filePath = docGen.generatePdfDocument("AckReceipt",
				reportDto);
		
		
		this.receiptOfDocumentsDTO.setDocFilePath(filePath);
		
		this.receiptOfDocumentsDTO.setDocType(SHAConstants.ACK_RECEIPT);
		
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
		// e.setSizeFull();
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
		// vLayout.setHeight("100%");
		vLayout.addComponent(vPanel);		

		return vLayout;
	}

	public void resetPage() {
		if (null != vLayout)
			vLayout.removeAllComponents();
	}

	
	private void generateAcknowledgeNo(ReceiptOfDocumentsDTO rodDTO) {
		Long claimKey = rodDTO.getClaimDTO().getKey();
		Long count = ackDocReceivedService.getCountOfAckByClaimKey(claimKey);
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ACK/")
				.append(rodDTO.getClaimDTO().getNewIntimationDto()
						.getIntimationId()).append("/").append(lackCount);
		rodDTO.getDocumentDetails().setAcknowledgementNumber(
				ackNoBuf.toString());
	}

}
