package com.shaic.claim.processRejectionPage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ProcessRejectionLetterUI extends AbstractMVPView implements ProcessRejectionLetterView {
	
	
	@Inject
	private SearchProcessRejectionTableDTO searchDTO;
	
	ProcessRejectionDTO bean;
	
	//private GWizard wizard;
	
	private Claim claim;
	
	private PreauthDTO preauthDTO;
	
	private CheckBox validateLetterChk;
	
	@Override
	public String getCaption() {
		return "Process Rejection Letter";
	}
	
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		

	}
	
	public void init(SearchProcessRejectionTableDTO searchDTO,ProcessRejectionDTO bean,GWizard wizard){
		
		this.searchDTO = searchDTO;
		this.bean = bean;
		//this.wizard = wizard;
		
	}

	public Component getContent() {
		
		fireViewEvent(ProcessRejectionLetterPresenter.GENERATED_REJECTION_LETTER, this.bean, this.searchDTO,this.searchDTO.getIntimationDTO());
		
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		
		preauthDTOList.add(preauthDTO);
		if(claim != null){
			reportDto.setClaimId(claim.getClaimId());
		}

		reportDto.setBeanList(preauthDTOList);	
		
		
		String filePath = docGen.generatePdfDocument("RegistrationSuggestRejectionLetter", reportDto);
		
		final String finalFilePath = filePath;
		
		// ((VerticalLayout) window.getContent()).setSizeFull();
	

		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		
		
		this.searchDTO.setDocFilePath(finalFilePath);
		this.searchDTO.setDocSource("Process Rejection");
		this.searchDTO.setDocType(SHAConstants.PROCESS_REJECTION_LETTER);
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		validateLetterChk = new CheckBox();

		validateLetterChk.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	
                if(event.getProperty().getValue() != null) {
                	preauthDTO.setLetterContentValidated((boolean)(event.getProperty().getValue()));
	                if(searchDTO.getPreAuthDto() != null) {
	                	searchDTO.getPreAuthDto().setLetterContentValidated((boolean)(event.getProperty().getValue()));
	                }
	                if(bean.getPreauthDTO() != null) {
	                	bean.getPreauthDTO().setLetterContentValidated((boolean)(event.getProperty().getValue()));
	                }
                }    
			}
		});
		
        Label chkLbl = new Label("<B>I confirm that the Contents of the Letter have been reviewed and are found to be in Order</B>",
                        ContentMode.HTML);
        HorizontalLayout validatechkLayout = new HorizontalLayout(validateLetterChk, chkLbl);
        
		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setHeight("100%");
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);

		VerticalSplitPanel panel = new VerticalSplitPanel();
		panel.setSplitPosition(90.0f, Unit.PERCENTAGE);
		panel.setLocked(true);
		panel.setFirstComponent(e);
		panel.setSecondComponent(validatechkLayout);
		panel.setHeight("450px");
		
//		VerticalLayout mainVertical = new VerticalLayout(e);
//	    return mainVertical;
		
		return panel;
		
	}
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ProcessRejectionDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		
		if(preauthDTO.isLetterContentValidated())
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
		return true;
	}


	@Override
	public void openPdfFileInWindow(Claim claim, PreauthDTO preauthDTO) {
		this.claim = claim;
		this.preauthDTO = preauthDTO;
		
		if(bean.getPreauthDTO() == null) {
			bean.setPreauthDTO(preauthDTO);
		}
		if(searchDTO.getPreAuthDto() == null) {
			searchDTO.setPreAuthDto(preauthDTO);
		}
	}
}
