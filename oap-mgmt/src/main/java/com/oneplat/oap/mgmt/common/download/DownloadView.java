package com.oneplat.oap.mgmt.common.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {

	private File file;

	public DownloadView(File file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> arg0, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Set-Cookie", "fileDownload=true;");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setStatus(HttpStatus.OK.value());
		FileInputStream fileInputStream = null;
		OutputStream outStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			outStream = response.getOutputStream();

			FileCopyUtils.copy(fileInputStream, outStream);
			
			outStream.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileInputStream != null)
				fileInputStream.close();
			if (outStream != null)
				outStream.close();
		}
	}
}
