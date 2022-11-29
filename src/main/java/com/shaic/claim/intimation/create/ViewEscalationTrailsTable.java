package com.shaic.claim.intimation.create;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewEscalationTrailsTable extends GBaseTable<ClaimGrievanceDTO> {

	private static final long serialVersionUID = 3028729594709428726L;

	@Inject
	private ViewGrievanceDoctorSolnEmail viewGrievanceDoctorSolnEmail;
	
	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ClaimGrievanceDTO>(ClaimGrievanceDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"escTicketCreationDateTime","escTicketNo","escSource","escContent","escTicketClosedDateTime"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		
		table.removeGeneratedColumn("escContent");
		table.addGeneratedColumn("escContent",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Email");
						final ClaimGrievanceDTO claimGrievanceDTO = (ClaimGrievanceDTO) itemId;
						System.out.println("Grievance Doctor Trail Mail : "+claimGrievanceDTO.getGrievDoctorSolMailTrail());
	/*					String mailTrailData = "<meta /> <style style=\"display: none;\">div.zm_-8465585266602674939_parse_7243954129022288187 P { margin-top: 0; margin-bottom: 0 }</style>  <div dir=\"ltr\" class=\" zm_-8465585266602674939_parse_7243954129022288187\"> <div style=\"font-family: Calibri, Arial, Helvetica, sans-serif;font-size: 12.0pt;color: rgb(0,0,0);\" class=\"x_556502352elementToProof\"> </div> Dear Mam/Sir, <div><br /> </div> <div>Kindly do the needful.</div> <div><br /> </div> <div>Regards,</div> <div>Jayashree S</div> Chairman Secretariat <div id=\"x_556502352divRplyFwdMsg\" dir=\"ltr\"> <div class=\"x_556502352elementToProof\"></div> </div> <div> <div style=\"direction: ltr;font-family: Tahoma;color: rgb(0,0,0);font-size: 10.0pt;\"><br /> <div style=\"font-family: Times New Roman;color: rgb(0,0,0);font-size: 16.0px;\"> <hr /> <div id=\"x_divRpF19714\" style=\"direction: ltr;\"><font face=\"Tahoma\" size=\"2\" color=\"#000000\"><b>From:</b> Guru Maravanthe [gurudutt171187@gmail.com]<br /> <b>Sent:</b> 29 May 2022 19:52:08<br /> <b>To:</b> claims.payment@starhealth.biz; gro; grievances; GRIEVANCES BLR; complaints IRDA; bimalokpal bengaluru<br /> <b>Cc:</b> A.R. RAMESH; savithree76@gmail.com; cmd; DR.Prakash S (Managing Director); cmdsec<br /> <b>Subject:</b> Re: Intimation No: CIR/2023/141137/0202003 - Incomplete Info on Policy- URGENT<br /> </font><br /> </div> <div></div> <div> <p style=\"color: red;\"><b><i><font face=\"Times New Roman\" size=\"4\">Cyber Security First: </font><font face=\"Times New Roman\">External email – Use caution whilst clicking links or opening any attachment. </font></i></b></p> <div> <div dir=\"auto\">There was no information from your side, either be teleo or thru email, regarding the claim of Ayush sub limit. <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">This was not informed to me while opting for Mediclaim Policy and the same is not in other Company Policies, why can't you give full and correct information while opting for Policy&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">I want 87% of claim to be done as per mediclaim policy and this was the information given to me, hence based on that I have gone through the treatment&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Request to check on this</div> </div> <br /> <div class=\"x_gmail_quote\"> <div dir=\"ltr\" class=\"x_gmail_attr\">On Sat, 28 May 2022, 11:43 Guru Maravanthe, &lt;<a href=\"mailto:gurudutt171187@gmail.com\" target=\"_blank\" rel=\"noreferrer\">gurudutt171187@gmail.com</a>&gt; wrote:<br /> </div> <blockquote class=\"x_gmail_quote\" style=\"margin: 0 0 0 0.8ex;border-left: 1.0px rgb(204,204,204) solid;padding-left: 1.0ex;\"> <div dir=\"auto\">Helo, <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><span style=\"font-size: 12.8px;\">My Concern is&nbsp;</span> <div dir=\"auto\" style=\"font-size: 12.8px;\"><br /> <div dir=\"auto\">Why the Claim is not been settled for the ratio 87% , you need to check on this and as per that total amount of 37,050 to be approved,&nbsp; wherein approved 25k,&nbsp;&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">You have mentioned non payable amount of Rs.19050 - As per policy Ayush sublimit exhausted,&nbsp; mine is the Mediclaim policy of 10 lakh and why only 53% approvEd instead of 87%?&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">(IRDAI) directed all general insurance companies to include AYUSH treatments in their health insurance policies</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Kindly check and do the&nbsp;needful</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Regards&nbsp;</div> <div dir=\"auto\">Gurudutt&nbsp;</div> </div> </div> </div> <br /> <div class=\"x_gmail_quote\"> <div dir=\"ltr\" class=\"x_gmail_attr\">On Fri, 27 May 2022, 23:57 , &lt;<a href=\"mailto:claims.payment@starhealth.biz\" target=\"_blank\" rel=\"noreferrer\">claims.payment@starhealth.biz</a>&gt; wrote:<br /> </div> <blockquote class=\"x_gmail_quote\" style=\"margin: 0 0 0 0.8ex;border-left: 1.0px rgb(204,204,204) solid;padding-left: 1.0ex;\"> <u></u> <div> <p style=\"text-align: center;\"></p> <h2 style=\"text-align: center;\">STAR HEALTH AND ALLIED INSURANCE CO. LTD.</h2> <p></p> <p style=\"text-align: center;\"></p> <h4 style=\"text-align: center;\">No.15, SRI BALAJI COMPLEX,1st FLOOR, WHITES LANE,ROYAPETTAH,CHENNAI-600014. Customer Care Number - 044 6900 6900 / Corporate Customers - 044 43664666, <img alt=\"Whatsapp\" height=\"16\" width=\"16\" />  Chat - +91 9597652225, <a href=\"http://www.Starhealth.in\" target=\"_blank\" rel=\"noreferrer\"> www.Starhealth.in</a></h4> <p></p> <p style=\"text-align: right;\">Date: 27/05/2022</p> <p style=\"text-align: left;\">Claim Int No :CIR/2023/141137/0202003 </p> <p style=\"text-align: left;\">Policy No. :P/141137/01/2022/006590 </p> <p style=\"text-align: left;\">Main Member Name :Mr.GURUDUTT </p> <p style
						
						if(!StringUtils.isBlank(mailTrailData)){
							mailTrailData = mailTrailData.replaceAll("\r\n", "");
							claimGrievanceDTO.setGrievDoctorSolMailTrail(mailTrailData);
						}*/
						System.out.println("Internal Escalation Trail Mail : "+claimGrievanceDTO.getGrievDoctorSolMailTrail());
						
						String grievanceDocSolEmail=claimGrievanceDTO.getGrievDoctorSolMailTrail();
						button.addClickListener(new Button.ClickListener()
						{
							@Override
							public void buttonClick(ClickEvent event) {
	
								
								Window popup = new com.vaadin.ui.Window();
								popup.setWidth("75%");
								popup.setHeight("90%");
								viewGrievanceDoctorSolnEmail.init(grievanceDocSolEmail);
								popup.setCaption("Escalation Content");
	
								/*String grievanceDoctorSolEmail=null;
								grievanceDoctorSolEmail = claimGrievanceDTO.getGrievDoctorSolMailTrail();
								System.out.println("Grievance Doctor Solution Email Content" + grievanceDoctorSolEmail);
								BrowserFrame browserFrame = new BrowserFrame("",
									    new ExternalResource(grievanceDoctorSolEmail!=null?grievanceDoctorSolEmail:""));
								
								browserFrame.setHeight("600px");
								browserFrame.setWidth("100%");
								
								VerticalLayout vLayout = new VerticalLayout(browserFrame);
								vLayout.setSizeFull();*/
								popup.setContent(viewGrievanceDoctorSolnEmail);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
								popup.addCloseListener(new Window.CloseListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void windowClose(CloseEvent e) {
										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
								
								
					
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
		
	}
	@Override
	public void tableSelectHandler(ClaimGrievanceDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "escalation-trails-";
	}

}