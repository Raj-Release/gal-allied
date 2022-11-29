package com.shaic.claim.intimation.create.dto;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ReportDto;

public class DocumentGenerator {
	
//	  public String generateCoveringLetter(String template_Name, List a_beanList)
//	  {
//	    try
//	    {
//	      if (ValidatorUtils.isNull(template_Name))
//	      {
//	        // Do Nothing
//	      }
//	      else if(template_Name != "")
//	      {
//	    	  
//	    	
////	        File file =  new File("D:\\CoveringLetterTemplates\\Revised\\ClaimFormCoveringLetter.jrxml");
//	        
////	        File file =  new File("D:\\CoveringLetterTemplates\\Revised\\"+event_template_Name+".jrxml");
//
//	    	String filePath = System.getProperty("jboss.server.data.dir");
//	    	System.out.println("file path : ====================="+filePath);
//	    	File file =  new File(filePath+"\\templates\\"+template_Name+".jrxml");
//	    	  
//	        if(file.exists() == false)
//	        {
//	          System.out.println("::::::::::::::::::::::file not available::::::::::::::::::::::");
//	        }  
//	       else
//	        {   
////               System.out.println("file :  >>>>>>    "+ file);
//              
//              JasperDesign a_jasperDesign = JRXmlLoader.load(file);
//	        
//	        JasperReport a_jasperReport = JasperCompileManager.compileReport(a_jasperDesign);
//	        if (a_jasperReport == null)
//	        {
//	          // do nothing 
////	          System.out.println(":::::::::::::::::: Template Compilation Error ::::::::::::::::");
//	        }
//	        else
//	        {
//	        	String claimId = null;
//	      
//	          if (!a_beanList.isEmpty())
//	          {
////	           System.out.println("Jasper Design not null " );
////	        	 claimId = ((ClaimDto)a_beanList.get(0)).getClaimId();
//	        	  //claimId = ((ReimbursementRejectionDto)a_beanList.get(0)).getReimbursementDto().getClaimDto().getClaimId();
//	            JRBeanCollectionDataSource a_jrDataSource = new JRBeanCollectionDataSource(a_beanList);
//	            Map<String,Object> subReportPathMap = new HashMap<String, Object>();
//	            String resourcePath = filePath+"\\templates\\";
//	            subReportPathMap.put("resourcePath", resourcePath);
//	            JasperPrint a_jasperPrint = JasperFillManager.fillReport(a_jasperReport, subReportPathMap, a_jrDataSource);
////	            System.out.println("########################After fill report"+ a_jasperPrint);
//	            if (a_jasperPrint == null)
//	            {
//	              // do nothing
//	            }
//	            else
//	            {
////	              System.out.println("::::::::::::::::::::::::::Before PDF Export ");
//	            	String a_pdfFileName = null; 
//	              	if(claimId != null){
//	              		a_pdfFileName = template_Name+"_"+claimId.substring(7, claimId.length())+".pdf";
//	              	}
//	              	else{
//	              	     a_pdfFileName = template_Name+"_"+".pdf";
//	              	}
//	            	final String PDF_URL = filePath+"\\documents\\"+a_pdfFileName;
//	              
//	              JasperExportManager.exportReportToPdfFile(a_jasperPrint,PDF_URL);
//	            
//	              return PDF_URL;
//	            
//	            }
//	          }
//	          else
//	          {
//	            //do nothing
//	        	  return "";
//	          }
//	        }
//	      }
//	    }
//           else
//          {
//             System.out.println("Invalid template Name");
//             return "";
//          }
//	    }
//	    catch (JRException jre)
//	    {
//	      // TODO: Add error handling
//	      jre.printStackTrace();
//	    }
//	    catch (Exception e)
//	    {
//	      // TODO: Need to add error handling
//	      e.printStackTrace();
//	    }
//	    finally
//	    {
//
//	    }
//	  return "";
//	  }

	  
	  public String generatePdfDocument(String template_Name,ReportDto reportDto){
		try {
			if (ValidatorUtils.isNull(template_Name)
					|| ValidatorUtils.isNull(reportDto)) {
				return null;
			} else if (template_Name != "" && reportDto != null&& reportDto.getBeanList() != null&& !reportDto.getBeanList().isEmpty()) {

				String filePath = System.getProperty("jboss.server.data.dir");
				System.out.println("file path : =====================" + filePath);
				File file = new File(filePath + File.separator + "templates" + File.separator + template_Name + ".jrxml");
				System.out.println("----File name and path ------" + file.getPath());
				
				if (file.exists() == false) {
					System.out.println("::::::::::::::::::::::file not available::::::::::::::::::::::");
				} else {
					// System.out.println("file :  >>>>>>    "+ file);
					JasperDesign a_jasperDesign = JRXmlLoader.load(file);
					JasperReport a_jasperReport = JasperCompileManager.compileReport(a_jasperDesign);
					if (a_jasperReport == null) {
						// do nothing
						// System.out.println(":::::::::::::::::: Template Compilation Error ::::::::::::::::");
					} else {
						String claimId = null;

						if (!reportDto.getBeanList().isEmpty()) {
							System.out.println("Jasper Design not null ");
							claimId = reportDto.getClaimId();
							JRBeanCollectionDataSource a_jrDataSource = new JRBeanCollectionDataSource(reportDto.getBeanList());
							WeakHashMap<String, Object> subReportPathMap = new WeakHashMap<String, Object>();
							String resourcePath = filePath + File.separator+ "templates" + File.separator;
							System.out.println("--resourcepath---"+ resourcePath);
							subReportPathMap.put("resourcePath", resourcePath);
							JasperPrint a_jasperPrint = JasperFillManager.fillReport(a_jasperReport,subReportPathMap, a_jrDataSource);
							
							
							if (a_jasperPrint == null) {
								// do nothing
							} else {
								// System.out.println("::::::::::::::::::::::::::Before PDF Export ");
								String a_pdfFileName = null;
								if (claimId != null) {
									// a_pdfFileName =
									// template_Name+"_"+claimId.substring(16)+".pdf";
									a_pdfFileName = "GAL_" + template_Name+ "_" + claimId.replace("/", "_");
								} else {
									a_pdfFileName = "GAL_" + template_Name+ "_";
								}
								
								
								/**
								 * This is for Removing Empty Pages in PDF for Bill Assessment Sheet Only.
								 * 
								 */
								if(SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.equalsIgnoreCase(template_Name)){
									List<JRPrintPage> jrPages = a_jasperPrint.getPages();

									if(jrPages != null && !jrPages.isEmpty()) {
									      for (Iterator<JRPrintPage> i=jrPages.iterator(); i.hasNext();) {
									          JRPrintPage page = i.next();
									          if (page.getElements().size() == 1)
									              i.remove();
									      }
									}								
								}

								/***
								 * This will be used only for reimder letter
								 * batch. Not used any where else.
								 * 
								 * */
								/*
								 * if(null != reportDto.getPresenterString() &&
								 * !("").equalsIgnoreCase(reportDto.
								 * getPresenterString())) { a_pdfFileName =
								 * "GAL_"
								 * +template_Name+"_"+claimId.replace("/",
								 * "_")+("_")+reportDto.getPresenterString(); }
								 */

								// final String PDF_URL = filePath+
								// File.separator + "documents"+a_pdfFileName;
								String PDF_URL = filePath+ File.separator + "documents"+ File.separator + a_pdfFileName + "_"+ new Date().getTime() + ".pdf";
								
								/*if(template_Name.equalsIgnoreCase(SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS) 
										|| template_Name.equalsIgnoreCase(SHAConstants.BILLASSESSMENTSHEETSCRC)
										|| template_Name.equalsIgnoreCase(SHAConstants.PA_BILLASSESSMENTSHEET))
								{
									PDF_URL = filePath+ File.separator + "documents"+ File.separator + a_pdfFileName + "_"+ reportDto.getBillAssessmentVersion() + ".pdf";
								}*/
								System.out.println(PDF_URL);
								JasperExportManager.exportReportToPdfFile(a_jasperPrint, PDF_URL);

								// For Word Export
								// final String DOC_URL = filePath+
								// File.separator + "documents"+ File.separator
								// + a_pdfFileName + "_" + new Date().getTime()
								// +".docx" ;
								// JRDocxExporter exporter = new
								// JRDocxExporter();
								// exporter.setParameter(JRExporterParameter.JASPER_PRINT,
								// a_jasperPrint);
								// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
								// DOC_URL);
								// exporter.exportReport();
								// return DOC_URL;

								return PDF_URL;

							}
						} else {
							// do nothing
							return StringUtils.EMPTY;
						}
					}
				}
			} else {
				System.out.println("Invalid template Name or List Empty");
				return StringUtils.EMPTY;
			}
		} catch (JRException jre) {
			jre.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return StringUtils.EMPTY;
	  }
}
