/**
 * 
 */
package com.shaic.claim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.v7.ui.Tree;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class DMSDocumentTreeViewImpl extends ViewComponent{
	
	
	private List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist;
	
	private HorizontalSplitPanel horizontalSplitPanel;
	
	private VerticalLayout verticalLayout ;
	
	private String intimationNo;
	
	private List<String> preauthFileList = new ArrayList<String>();
	
	private List<String> enhancementFileList = new ArrayList<String>();
	
	private List<String> fvrFileList = new ArrayList<String>();
	
	private List<String> othersFileList = new ArrayList<String>();
	
	private List<String> queryReportFileList = new ArrayList<String>();
	
	private List<String> rodFileList = new ArrayList<String>();
	
	private List<String> pccDMSDocFileList = new ArrayList<String>();
	
	private Object[] preauthObjArr = null;
	private Object[] enhancementObjArr = null;
	private Object[] fvrObjArr = null;
	private Object[] othersObjArr = null;
	private Object[] queryReportObjArr = null;
	private Object[] rodObjArr = null;
	private Object[] pccDMSDocFileArr= null;
	
	private Object[][] treeMenuArr= null;
	
	 
	
	public DMSDocumentTreeViewImpl(List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist ,String intimationNo)
	{
		this.dmsDocumentDetailsDTOlist = dmsDocumentDetailsDTOlist;
		this.intimationNo = intimationNo;
		
	}
	
	public void getContent()
	{
		intializeFileListBasedOnDocType();
		intializeObjectArray();
		intializeTreeArray();
		verticalLayout = buildDMSTreeViewLayout();
		
	}
	
	
	private VerticalLayout buildDMSTreeViewLayout()
	{
		Tree dmsTree = new Tree("Manage Digital File");
		//
		dmsTree.addItem("Root("+intimationNo+")");
		if(null != treeMenuArr)
		{
			for (int i=0; i<treeMenuArr.length; i++) {
				    String docFileType = (String) (treeMenuArr[i][0]);
				    dmsTree.addItem(docFileType);
				
			    if (treeMenuArr[i].length == 1) {
			        // The planet has no moons so make it a leaf.
			    	dmsTree.setChildrenAllowed(docFileType, false);
			    } else {
			        // Add children (moons) under the planets.
			        for (int j=1; j<treeMenuArr[i].length; j++) {
			            String childNode = (String) treeMenuArr[i][j];
	
			            // Add the item as a regular item.
			            dmsTree.addItem(childNode);
	
			            // Set it to be a child.
			            dmsTree.setParent(childNode, docFileType);
	
			            // Make the moons look like leaves.
			            dmsTree.setChildrenAllowed(childNode, false);
			        }
	
			        // Expand the subtree.
			        dmsTree.expandItemsRecursively(docFileType);
			    }
			}
		}

		
		return null;
	}
	
	
	private void intializeTreeArray()
	{
		treeMenuArr = new Object[][]{preauthObjArr,enhancementObjArr,fvrObjArr,othersObjArr,queryReportObjArr,rodObjArr};
		
	}
	
	private void intializeFileListBasedOnDocType()
	{
		if(null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty())
		{
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				
				if(SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH))
				{
					this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());	
				}
				else if(SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT))
				{
					this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(SHAConstants.PREMIA_DOC_TYPE_REIMBURSEMENT_QUERY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.queryReportFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(SHAConstants.PREMIA_DOC_TYPE_FVR.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.fvrFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else if(SHAConstants.PREMIA_DOC_TYPE_PCC.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				{
					this.pccDMSDocFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
				else
				{
					this.rodFileList.add(dmsDocumentDetailsDTO.getFileName());
				}
			}
		}
	}
	
	
	private void intializeObjectArray()
	{
		if(null != preauthFileList && !preauthFileList.isEmpty())
		{
			preauthObjArr = new Object[preauthFileList.size()][];
			preauthObjArr = appendValue(preauthObjArr,preauthFileList,SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION);
		}
		if(null != enhancementFileList && !enhancementFileList.isEmpty())
		{
			enhancementObjArr = new Object[enhancementFileList.size()][];
			enhancementObjArr = appendValue(enhancementObjArr,enhancementFileList,SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT);
		}
		if(null != queryReportFileList && !queryReportFileList.isEmpty())
		{
			queryReportObjArr = new Object[queryReportFileList.size()][];
			queryReportObjArr = appendValue(queryReportObjArr,queryReportFileList,SHAConstants.PREMIA_DOC_TYPE_CASHLESS_QUERY_REPORT);
		}
		if(null != fvrFileList && !fvrFileList.isEmpty())
		{
			fvrObjArr = new Object[fvrFileList.size()][];
			fvrObjArr = appendValue(fvrObjArr,fvrFileList,SHAConstants.PREMIA_DOC_TYPE_FVR);
		}
		if(null != othersFileList && !othersFileList.isEmpty())
		{
			othersObjArr = new Object[othersFileList.size()][];
			othersObjArr = appendValue(othersObjArr,othersFileList,SHAConstants.PREMIA_DOC_TYPE_OTHERS);
		}
		if(null != pccDMSDocFileList && !pccDMSDocFileList.isEmpty())
		{
			pccDMSDocFileArr = new Object[pccDMSDocFileList.size()][];
			pccDMSDocFileArr = appendValue(pccDMSDocFileArr,pccDMSDocFileList,SHAConstants.PREMIA_DOC_TYPE_PCC);
		}
		if(null != rodFileList && !rodFileList.isEmpty())
		{
			rodObjArr = new Object[rodFileList.size()][];
			rodObjArr = appendValue(rodObjArr,rodFileList,"ROD");
		}
	}

	
	private Object[] appendValue(Object[] obj, List<String> fileNameList,String docFileType) {

		ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
		if(null != fileNameList && !fileNameList.isEmpty())
		{
			temp.add(docFileType);
			for (String string1 : fileNameList) {
				temp.add(string1);
			}
		}
		return temp.toArray();

	  }
}
