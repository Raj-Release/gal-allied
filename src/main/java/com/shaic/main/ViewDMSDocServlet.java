package com.shaic.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;

import com.shaic.arch.SHAFileUtils;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class ViewDMSDocServlet extends HttpServlet {

	private static final long serialVersionUID = 2393067028390109294L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestParam = request.getQueryString();
		if(!StringUtils.isBlank(requestParam)){
			if(requestParam.contains("docId")){
				String tempToken = requestParam.substring(requestParam.lastIndexOf("=")+1, requestParam.length());
				Hashids hashids = new Hashids(BPMClientContext.SALT_KEY, 8);
				long[] tokenArray = hashids.decode(tempToken);
				
				String longUrl = SHAFileUtils.viewFileByToken(String.valueOf(tokenArray[0]));
	
				try {
					ServletOutputStream out = response.getOutputStream();
					response.setContentType("application/pdf");
					String fileName = tempToken+".pdf";
					response.setHeader("Content-Disposition","inline;filename="+fileName);
					
					URL url = new URL(longUrl);
					URLConnection connection = url.openConnection();
					connection.connect();
					int fileLenth = connection.getContentLength();
					InputStream inputStream = url.openStream();
					BufferedInputStream in = new BufferedInputStream(inputStream);
				    byte data[] = new byte[fileLenth];
				    int count;
				    while((count = in.read(data,0,fileLenth)) != -1) {
				        out.write(data, 0, count);
				    }
					in.close();
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
