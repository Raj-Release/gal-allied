package com.shaic.claim.intimation.create;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;


public class ViewGrievanceDoctorSolnEmail extends ViewComponent {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private VerticalLayout mainLayout;
		
		private TextField grievsolemail;
		
		private Label label;
		ClaimGrievanceDTO claimGrievanceDTO;
		
		public void init(String grievanceDoctorSolEmail){
			
			//grievanceDoctorSolEmail = claimGrievanceDTO.getGrievDoctorSolMailTrail();
			System.out.println("Grievance Doctor Solution Email Content" + grievanceDoctorSolEmail);
			/*grievsolemail =new TextField();
			grievsolemail.setValue(grievanceDoctorSolEmail);
			grievsolemail.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);*/
			Label label = new Label();
			label.setValue(grievanceDoctorSolEmail);
			label.setContentMode(ContentMode.HTML);
			Panel panel = new Panel();
	        panel.setHeight("450px");
	        panel.setSizeFull();
	        panel.setContent(label);
			
			VerticalLayout vLayout = new VerticalLayout(panel);
			vLayout.setSizeFull();
			mainLayout = new VerticalLayout(vLayout);
			mainLayout.setMargin(true);
			mainLayout.setHeight("100%");
			setHeight("100%");
			setCompositionRoot(mainLayout);
			
			
		}

}