package com.oneplat.oap.mgmt.common.download;

import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Api(description="공통 > 파일다운로드 ", produces = "application/json")
@Controller
@RequestMapping(value = "/common/download")
public class DownloadController {
    
    private static final Logger log = LoggerFactory.getLogger(DownloadController.class);

    @Autowired 
    private Environment env;
    
    @RequestMapping(value="/file", method = RequestMethod.GET)
    public ModelAndView fileDownload(String filePath){
    	File file = new File(filePath);
    	if(file.exists() && file.isFile()){
    		return new ModelAndView(new DownloadView(file));
    	}else{
    		return null;
    	}
    }
    
    @ApiOperation(value = "파일 다운로드", notes = "파일 다운로드")
    @RequestMapping(value="/{folderType}", method = RequestMethod.GET)
    public void fileDownload(
            @ApiParam(value="파일") @PathVariable String folderType,String fileName, HttpServletResponse response    ) throws IOException{
        // doc 공유 폴더 경로
        String docPath = env.getProperty("doc.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("doc.upload."+folderType+".path");
        log.debug("folderTypePath : "+folderTypePath);
        // folderType에 맞는 경로가 없는 경우 기본 경로로 잡아준다
        if(StringUtils.isNullOrEmpty(folderTypePath)){
            folderTypePath = env.getProperty("doc.upload.default.path")+File.separator+folderType;
            log.debug("folderTypePath not found(change default path): "+folderTypePath);
        }
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = docPath+File.separator+folderTypePath;
        
        String[] allowMimeTypes = {"image/jpeg","image/png","image/jpg"};
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        File file = new File(folderPath);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = mimeTypesMap.getContentType(file);
        Boolean isAllowed = false;
        for(String allowMimeType: allowMimeTypes){
            if(allowMimeType.equals(mimeType))
                isAllowed = true;
        }
        isAllowed = true; //임시
        if(isAllowed){
            mimeType = "application/octet-stream";
             
            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
             
            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);
             
            OutputStream outStream = response.getOutputStream();
             
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
             
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
             
            inputStream.close();
            outStream.close();   
        }else{
            /*
             * Do something.
             */
        }
    }
    
    @ApiOperation(value = "파일 다운로드", notes = "파일 다운로드")
    @RequestMapping(value="/{folderType}/{subFolderType}", method = RequestMethod.GET)
    public void fileDownload(
            @ApiParam(value="파일") @PathVariable String folderType,@ApiParam(value="파일") @PathVariable String subFolderType, String fileName, HttpServletResponse response    ) throws IOException{
        
        // doc 공유 폴더 경로
        String docPath = env.getProperty("doc.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("doc.upload."+folderType+".path");
        log.debug("folderTypePath : "+folderTypePath);
        // folderType에 맞는 경로가 없는 경우 기본 경로로 잡아준다
        if(StringUtils.isNullOrEmpty(folderTypePath)){
            folderTypePath = env.getProperty("doc.upload.default.path")+File.separator+folderType;
            log.debug("folderTypePath not found(change default path): "+folderTypePath);
        }
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = docPath+File.separator+folderType+File.separator+File.separator+subFolderType+File.separator+File.separator+fileName;
        
        String[] allowMimeTypes = {"image/jpeg","image/png","image/jpg"};
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        File file = new File(folderPath);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = mimeTypesMap.getContentType(file);
        Boolean isAllowed = false;
        for(String allowMimeType: allowMimeTypes){
            if(allowMimeType.equals(mimeType))
                isAllowed = true;
        }
        isAllowed = true; //임시
        if(isAllowed){
            mimeType = "application/octet-stream";
             
            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
             
            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);
             
            OutputStream outStream = response.getOutputStream();
             
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
             
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
             
            inputStream.close();
            outStream.close();   
        }else{
            /*
             * Do something.
             */
        }
    }
    @ApiOperation(value = "파일 다운로드", notes = "파일 다운로드")
    @RequestMapping(value="", method = RequestMethod.GET)
    public void fileDownloadByPath(
            @ApiParam(value="파일") @RequestParam String filePath, HttpServletResponse response    ) throws IOException{
        
     // doc 공유 폴더 경로
        String docPath = env.getProperty("doc.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderPath = docPath+File.separator+filePath;
        
        String[] allowMimeTypes = {"image/jpeg","image/png","image/jpg"};
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        File file = new File(folderPath);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = mimeTypesMap.getContentType(file);
        Boolean isAllowed = false;
        for(String allowMimeType: allowMimeTypes){
            if(allowMimeType.equals(mimeType))
                isAllowed = true;
        }
        isAllowed = true; //임시
        if(isAllowed){
            mimeType = "application/octet-stream";
             
            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
             
            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);
             
            OutputStream outStream = response.getOutputStream();
             
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
             
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
             
            inputStream.close();
            outStream.close();   
        }else{
            /*
             * Do something.
             */
        }
    }
}
