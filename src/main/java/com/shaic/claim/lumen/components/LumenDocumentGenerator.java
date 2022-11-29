package com.shaic.claim.lumen.components;

import java.io.File;
import java.util.Date;
import java.util.WeakHashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ReportDto;

public class LumenDocumentGenerator {
	
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
									if(template_Name.contains("LumenLetter")){
										a_pdfFileName = "GAL_" + template_Name+ "_" + claimId.replace("/", "_");
									}
								} else {
									a_pdfFileName = "GAL_" + template_Name;
								}
								final String PDF_URL = filePath+ File.separator + "documents"+ File.separator + a_pdfFileName + "_"+ new Date().getTime() + ".pdf";
								System.out.println(PDF_URL);
								JasperExportManager.exportReportToPdfFile(a_jasperPrint, PDF_URL);
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
