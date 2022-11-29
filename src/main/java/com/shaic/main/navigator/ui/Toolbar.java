package com.shaic.main.navigator.ui;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.annotation.ButtonProperties;
import org.vaadin.addon.cdiproperties.annotation.HorizontalLayoutProperties;
import org.vaadin.dialogs.ConfirmDialog;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.MagazineDTO;
import com.shaic.arch.utils.Props;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.ClaimMagazine;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class Toolbar extends ViewComponent {
	@Inject
	@HorizontalLayoutProperties(styleName = "toolbar", spacing = true, widthValue = 100.0f, widthUnits = Unit.PERCENTAGE)
	private HorizontalLayout layout;
	@Inject
	@ButtonProperties(captionKey = "toolbar-addcontact")
	private Button addContactButton;
	@Inject
	@ButtonProperties(captionKey = "toolbar-search")
	private Button searchButton;
	@Inject
	@ButtonProperties(captionKey = "toolbar-share")
	private Button shareButton;
	@Inject
	@ButtonProperties(captionKey = "toolbar-help")
	private Button helpButton;
	@Inject
	private MagazineGallery magazineGallery; 
	@Inject
	private MagazineDTO magazineDTO;
	@Inject
	private MasterService masterService;
	
	private MenuBar menuBar;
	
	private List<ClaimMagazine> magazineList;
	private String lblProjectName = "Galaxy Claims";
	private String lblMenuItemManageAccount = "Manage Account";
	private String lblMenuItemLogout = "Logout";
	private String lblMenuItemSwitch = "Switch Module";
//	private String lblChatBot = "Knowledge Bot";
	
	protected Label tranCount;
	
	protected Label prodCount;
	
	protected Label mlabel;
		
	private HorizontalLayout countLayout = new HorizontalLayout();
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	private BPMClientContext bpmClientContext = new BPMClientContext();

	private Button lblGallery;
	private Button okButton;
	private HorizontalLayout hLayout = new HorizontalLayout();
	private Panel panelGallery = new Panel();

	@PostConstruct
	public void init() {
		VerticalLayout verticlaLayout = new VerticalLayout();
		verticlaLayout.setHeight(55, Unit.PIXELS);
		
		Embedded headerIcon = new Embedded("", new ThemeResource("images/logo4.png"));
		headerIcon.addStyleName("logo");
		headerIcon.setWidth(180, Unit.PIXELS);
		headerIcon.setHeight(55, Unit.PIXELS);

		Embedded botIcon = new Embedded("", new ThemeResource("images/bot_1.png"));
		botIcon.addStyleName("logo");
		botIcon.setWidth(67, Unit.PIXELS);
		botIcon.setHeight(56, Unit.PIXELS);		
		
		com.vaadin.event.MouseEvents.ClickListener botListener =new com.vaadin.event.MouseEvents.ClickListener() {			
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println(new Date() + " User: " +((String) getSession().getAttribute(BPMClientContext.USERID))+" launched Bot window");
				String bot_url =BPMClientContext.GALAXY_BOT_URL+generateBotToken((String) getSession().getAttribute(BPMClientContext.USERID));
				getUI().getPage().open(bot_url, "Chatbot",455,620,BorderStyle.NONE);
			}};
		botIcon.addClickListener(  botListener);

		Label label = new Label(lblProjectName);
		label.setWidth(95, Unit.PERCENTAGE);
		label.addStyleName("labelHeader");
		
//		BPMClientContext bpmClientContext = new BPMClientContext();
		if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
			mlabel = new Label("Reimbursement");	
			mlabel.setWidth(95, Unit.PERCENTAGE);	
			mlabel.addStyleName("labelHeader");
		} else if(!bpmClientContext.showActions() && bpmClientContext.showReports()){
			mlabel = new Label("Reports");	
			mlabel.setWidth(95, Unit.PERCENTAGE);	
			mlabel.addStyleName("labelHeader");
		}else if(bpmClientContext.showActions() && bpmClientContext.showReports()){
			mlabel = new Label("Allied");	
			mlabel.setWidth(95, Unit.PERCENTAGE);	
			mlabel.addStyleName("labelHeader");
		}

		lblGallery = new Button("Magazine Gallery");
		lblGallery.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		//lblGallery.addStyleName(ValoTheme.BUTTON_LINK);
		lblGallery.setWidth(80,Unit.PERCENTAGE);
		lblGallery.setHeight(20,Unit.PERCENTAGE);
		
		lblGallery.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				final Window popupWindow = new Window();
				VerticalLayout galleryPopup=new VerticalLayout();
				/*popupWindow.setCaptionAsHtml(true);
				popupWindow.setCaption("<center>Magazine Gallery</center>");*/
				popupWindow.setWidth("400px");
				popupWindow.setHeight("300px");
				Button okButton = new Button("Close");
				okButton.setStyleName(ValoTheme.BUTTON_DANGER);
				magazineGallery.init("", false, false);
				fireViewEvent(MenuPresenter.GET_MAGAZINE,null);
				HorizontalLayout mGAll = new HorizontalLayout();
				mGAll.addComponent(magazineGallery);
				HorizontalLayout mokPop = new HorizontalLayout();
				mokPop.addComponent(okButton);
				galleryPopup.addComponents(mGAll,mokPop);
				galleryPopup.setComponentAlignment(mokPop, Alignment.BOTTOM_CENTER);
				popupWindow.setContent(galleryPopup);
				
				
				popupWindow.center();
				popupWindow.setClosable(true);
				popupWindow.setResizable(false);
				
				okButton.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popupWindow.close();
					}
				});
				popupWindow.addCloseListener(new Window.CloseListener() {
				
					private static final long serialVersionUID = 1L;
					
	
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});
	
				popupWindow.setModal(true);
				UI.getCurrent().addWindow(popupWindow);
			
				
			}
			
		});
		
//		Panel panelGallery = new Panel();
		panelGallery.setContent(lblGallery);
		panelGallery.addStyleName(ValoTheme.PANEL_BORDERLESS);
		

		menuBar = new MenuBar();
		menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		menuBar.addStyleName("labelHeader");

		HorizontalLayout dumLayout = new HorizontalLayout();;
		Panel panel = new Panel();
		panel.setContent(countLayout);
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.addStyleName("productivitycount");
		//HorizontalLayout hLayout = new HorizontalLayout(headerIcon, label, panelGallery, menuBar);
//		HorizontalLayout hLayout = new HorizontalLayout(headerIcon,dumLayout,label,panelGallery, panel,menuBar);
		if(BPMClientContext.BOT_ENABLE.equalsIgnoreCase("YES")){
			hLayout.addComponents(headerIcon,dumLayout,label,panel,mlabel,botIcon,menuBar);
		}else{
			hLayout.addComponents(headerIcon,dumLayout,label,/*panelGallery,*/ panel,mlabel,menuBar);
		}
		hLayout.setComponentAlignment(headerIcon, Alignment.TOP_LEFT);
		hLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		hLayout.setComponentAlignment(mlabel, Alignment.MIDDLE_RIGHT);
		//hLayout.setComponentAlignment(panelGallery, Alignment.MIDDLE_RIGHT);
		if(BPMClientContext.BOT_ENABLE.equalsIgnoreCase("YES")){
			hLayout.setComponentAlignment(botIcon, Alignment.MIDDLE_RIGHT);
		}
		hLayout.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);

		hLayout.setWidth(100, Unit.PERCENTAGE);
		hLayout.setHeight(55, Unit.PIXELS);
		hLayout.setSpacing(false);
		verticlaLayout.addComponent(hLayout);

		layout.addComponent(verticlaLayout);
		layout.setExpandRatio(verticlaLayout, 1);

		setCompositionRoot(layout);
	}
	
	

	public void updateUserStatus() {
		if(getUI() != null && getUI().getSession() != null) {
			final String userName = (String) getSession().getAttribute(BPMClientContext.USERID);
			final String manageAccountUrl = BPMClientContext.KEYCLOAK_SERVER_URL + "/auth/realms/" + BPMClientContext.KEYCLOAK_REALM + "/account/password";
			String redirectUrl = BPMClientContext.KEYCLOAK_SERVER_URL + "/auth/realms/" 
		        	+ BPMClientContext.KEYCLOAK_REALM + "/protocol/openid-connect/logout?client_id="
		        	+ BPMClientContext.KEYCLOAK_CLIENT_ID + "&redirect_uri=" + BPMClientContext.KEYCLOAK_REDIRECT_URL;
			Boolean cashlessMenu = BPMClientContext.showCashlessMenu();
			MenuBar.Command mycommand = new MenuBar.Command() {
				public void menuSelected(MenuItem selectedItem) {
					if(lblMenuItemManageAccount.equals(selectedItem.getText())) {
						getUI().getPage().open(manageAccountUrl, "_blank");
					} else if(lblMenuItemLogout.equals(selectedItem.getText())) {
						Integer existingTaskNumber = (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
						if(existingTaskNumber != null) {
							getSession().setAttribute(SHAConstants.TOKEN_ID, null);
						}

						fireViewEvent(MenuPresenter.LOGOUT_UPDATE_DETAILS, userName);
						getUI().getSession().close();
						VaadinService.getCurrentRequest().getWrappedSession().invalidate();
						getUI().getPage().setLocation(redirectUrl);
						//						getUI().getPage().setLocation("./logout");

					} /*else if(lblChatBot.equals(selectedItem.getText())) {
						String bot_url ="http://claimssit.starhealth.in:8180/ims/knowledgeBot?token="+generateBotToken((String) getSession().getAttribute(BPMClientContext.USERID));
						getUI().getPage().open(bot_url, "_blank",455,620,BorderStyle.NONE);
					}*/
					
//					BPMClientContext bpmClientContext = new BPMClientContext();
					/*As per Saran instruction switch module disabled
					if(!cashlessMenu && bpmClientContext.showActions() && !bpmClientContext.showReports()){
						if(lblMenuItemSwitch.equals(selectedItem.getText())){
							loginpopup();
						}	
					}*/	
				}
			};
			ImsUser imsUser = null;
			String[] userRoles = null;
			if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
				imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
				userRoles = imsUser.getUserRoleList();
			}
			if(!Arrays.asList(userRoles).contains(SHAConstants.CLM_OMP_TPA_INTIMATION)) {
			MenuItem menuItem = menuBar.addItem(userName.toUpperCase(), FontAwesome.USER, null);
			menuItem.addItem(lblMenuItemManageAccount, FontAwesome.GEAR, mycommand);
			menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
			menuItem.addSeparator();
			/*if(BPMClientContext.BOT_ENABLE.equalsIgnoreCase("YES")){
				menuItem.addItem(lblChatBot, FontAwesome.GEAR, mycommand);
				menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
				menuItem.addSeparator();
			}*/
			/*As per Saran instruction switch module disabled
			if(!cashlessMenu){
				menuItem.addItem(lblMenuItemSwitch, FontAwesome.SIGN_IN, mycommand);
				menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
			}*/
//			menuItem.addSeparator();
			menuItem.addItem(lblMenuItemLogout, FontAwesome.SIGN_OUT, mycommand);
			menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
			
//			BPMClientContext bpmClientContext = new BPMClientContext();
			//if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
				Map<Integer,Object> values = dbCalculationService.getUserProductivityCount(userName.toUpperCase());
				
				prodCount = new Label();
				prodCount.setContentMode(ContentMode.HTML);
				prodCount.setCaption("Productivity Count:");
				prodCount.setValue(values.get(1).toString());
				prodCount.addStyleName("toolheader");
	
				
				tranCount = new Label();
				tranCount.setContentMode(ContentMode.HTML);
				tranCount.setCaption("Claim Count:");
				tranCount.setValue(values.get(2).toString());
				tranCount.addStyleName("toolheader");
				
				countLayout.addComponent(prodCount);
				countLayout.addComponent(tranCount);
				countLayout.setSpacing(true);
				countLayout.addStyleName("toolheader");
				//GLX2020101
				hLayout.addComponent(panelGallery, 3);
			//}
			}
			else	
			{
				MenuItem menuItem = menuBar.addItem(userName.toUpperCase(), FontAwesome.USER, null);
				menuItem.addItem(lblMenuItemManageAccount, FontAwesome.GEAR, mycommand);
				menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
				menuItem.addSeparator();
				/*As per Saran instruction switch module disabled
				if(!cashlessMenu){				
					menuItem.addItem(lblMenuItemSwitch, FontAwesome.SIGN_IN, mycommand);
					menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
					menuItem.addSeparator();
				}*/
				menuItem.addItem(lblMenuItemLogout, FontAwesome.SIGN_OUT, mycommand);
				menuItem.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
				
		}
		}
	}

	
	public void countTool(){
		
		final String userName = (String) getSession().getAttribute(BPMClientContext.USERID);
		//Added for OMP-TPA USER LOGIN - 16-Apr-19 
		if(tranCount != null && prodCount != null){
			Map<Integer,Object> values = dbCalculationService.getUserProductivityCount(userName.toUpperCase());
			tranCount.setValue(values.get(2).toString());
			prodCount.setValue(values.get(1).toString());
		}
	}
	
	public void opcountTool(){
		
		final String userName = (String) getSession().getAttribute(BPMClientContext.USERID);
		if(tranCount != null && prodCount != null){
			Map<Integer,Object> values = dbCalculationService.getOPProductivityCount(userName.toUpperCase());
			tranCount.setValue(values.get(2).toString());
			prodCount.setValue(values.get(1).toString());
		}
	}

	public void setMagazineTableValues( List<MasMagazineDocument> magazineList) {
		magazineGallery.setTableList(magazineList);
	}
	
	private void loginpopup(){

		Window popupWindow = new com.vaadin.ui.Window();
		popupWindow.setCaption("Switch Module");

		Label infoLabel = new Label("<b style = 'color: black; font-size:18px'>Please select the module you would like to access. </b>", ContentMode.HTML);			
		infoLabel.setHeight("70px");
		Button cashless = new Button("Cashless");
		cashless.setStyleName("moduleSwitch");
		cashless.setWidth("145px");
		cashless.setHeight("110px");
		cashless.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {
				final String cashlessurl = BPMClientContext.CASHLESS_URL;
				getUI().getPage().open(cashlessurl, "_blank",1550,650,BorderStyle.NONE);
				//getUI().getPage().open(cashlessurl, "_blank");			
			}
		});

		Button reimbursement = new Button("Reimbursement");
		reimbursement.setStyleName("moduleSwitch");
		reimbursement.setWidth("145px");
		reimbursement.setHeight("110px");
		reimbursement.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(!bpmClientContext.showActions() && bpmClientContext.showReports()){
					final String reimbursementurl = BPMClientContext.REPORTS_URL;
					getUI().getPage().open(reimbursementurl, "_blank",1550,650,BorderStyle.NONE);
				} else {
					popupWindow.close();
				}
			}
		});
		
		Button reports = new Button("Reports");
		reports.setStyleName("moduleSwitch");
		reports.setWidth("145px");
		reports.setHeight("110px");
		reports.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {
//				popupWindow.close();
				if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
					final String url = BPMClientContext.REPORTS_URL; 
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				} else{
					popupWindow.close();
				}
			}
		});
		
		Button others = new Button("Others");
		others.setStyleName("moduleSwitch");
		others.setWidth("145px");
		others.setHeight("110px");
		others.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {
				popupWindow.close();
			}
		});
		
		Label noteLabel = null;
//		if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
			 noteLabel = new Label(	
				    "<b style = 'color: black; font-size:14px'>&nbsp; &nbsp;<u>Note</u></b> \n" +	
				    "<ul> "+	
				    "  <li><b style = 'color: black; font-size:14px'>Cashless – All pure Cashless menus</b></li> "+	
				    "  <li><b style = 'color: black; font-size:14px'>Reimbursement - All pure Reimbursement menus – Acknowledgement to FA</b></li> "+
				    "  <li><b style = 'color: black; font-size:14px'>Reports - All pure Report menus</b></li> "+
				    "  <li><b style = 'color: black; font-size:14px'>Others (All other menus – both common ones for Cashless & Reimbursement  and special ones like audit, payment, etc.)</b></li> "+	
				    "</ul> "	
				    ,ContentMode.HTML);
//		} else {
//				noteLabel = new Label(	
//			    "<b style = 'color: black; font-size:14px'>&nbsp; &nbsp;<u>Note</u></b> \n" +	
//			    "<ul> "+	
//			    "  <li><b style = 'color: black; font-size:14px'>Cashless – All pure Cashless menus</b></li> "+	
//			    "  <li><b style = 'color: black; font-size:14px'>Reimbursement - All pure Reimbursement menus – Acknowledgement to FA</b></li> "+
//			    "  <li><b style = 'color: black; font-size:14px'>Others (All other menus – both common ones for Cashless & Reimbursement  and special ones like audit, payment, etc.)</b></li> "+	
//			    "</ul> "	
//			    ,ContentMode.HTML);
//		}

		HorizontalLayout HLayout = new HorizontalLayout(infoLabel);
		HorizontalLayout butHolder = new HorizontalLayout();
		butHolder.addComponent(cashless);
		butHolder.addComponent(reimbursement);
//		BPMClientContext bpmClientContext = new BPMClientContext();
//		if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
			butHolder.addComponent(reports);
//		}	
		butHolder.addComponent(others);
		butHolder.setComponentAlignment(cashless, Alignment.BOTTOM_CENTER);
		butHolder.setComponentAlignment(reimbursement, Alignment.BOTTOM_CENTER);
//		if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
			butHolder.setComponentAlignment(reports, Alignment.BOTTOM_CENTER);
//		}
		butHolder.setComponentAlignment(others, Alignment.BOTTOM_CENTER);
		butHolder.setSpacing(true);
		butHolder.setMargin(true);
		HorizontalLayout NLayout = new HorizontalLayout(noteLabel);

		VerticalLayout holderLayout = new VerticalLayout();
		holderLayout.addComponent(HLayout);
		holderLayout.addComponent(butHolder);
		holderLayout.addComponent(NLayout);
		holderLayout.setSpacing(true);
		holderLayout.setComponentAlignment(HLayout, Alignment.BOTTOM_CENTER);
		holderLayout.setComponentAlignment(butHolder, Alignment.BOTTOM_CENTER);
		popupWindow.setContent(holderLayout);
//		if(bpmClientContext.showActions() && !bpmClientContext.showReports()){
//			popupWindow.setWidth("60%");
//		} else {
			popupWindow.setWidth("60%");
//		}
		popupWindow.setHeight("50%");
		popupWindow.center();
		popupWindow.setClosable(false);
		popupWindow.setResizable(false);
		UI.getCurrent().addWindow(popupWindow);
	}
	public String generateBotToken(String username) {
		String token = "";
		try {
			Path path = Paths.get(BPMClientContext.dataDir+BPMClientContext.BOT_KEY_LOCATION);
			byte[] privKeyByteArray = Files.readAllBytes(path);
	
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyByteArray);
	
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	
			JWSSigner signer = new RSASSASigner((RSAPrivateKey) privateKey);
	
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
			claimsSet.issuer("GALAXY_CLAIMS");
			
			claimsSet.issueTime(new Date());
			claimsSet.claim("sub", username);
			claimsSet.claim("jwtVersion", "v1");
			claimsSet.expirationTime(new Date(new Date().getTime() + 1000 * 60 * 5));
	
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());			
			signedJWT.sign(signer);
			token = signedJWT.serialize();			
			signedJWT = SignedJWT.parse(token);
		
	
	//	JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) generatePublic);	
			//System.out.println(new Date(new Date().getTime() + 1000 * 60 * 10));
			//System.out.println( username +" - "+token);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return token;
	}

}
