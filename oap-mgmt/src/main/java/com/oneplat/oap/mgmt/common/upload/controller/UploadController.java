package com.oneplat.oap.mgmt.common.upload.controller;

import com.mysql.jdbc.StringUtils;
import com.oneplat.oap.core.util.FileHepler;
import com.oneplat.oap.core.util.StringHelper;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.common.upload.model.CkeditorFileInfo;
import com.oneplat.oap.mgmt.common.upload.model.FileInfo;
import com.oneplat.oap.mgmt.util.StringCommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Api(tags="/common/upload",  description="공통 > 파일업로드 ", produces = "application/json")
@RestController
@RequestMapping(value = "/common/upload")
public class UploadController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired 
    private Environment env;
    
    @Autowired
    private AuthenticationInjector authenticationInjector;

    @ApiOperation(value = "공통 > 파일업로드", notes = "공통 > 파일업로드")
    @RequestMapping(value="/ajaxFileUpload/{folderType}", method = RequestMethod.POST)
    public  @ResponseBody
    FileInfo fileUpload(@RequestParam("file") MultipartFile file,
                        @ApiParam(value="folderType", defaultValue="test") @PathVariable String folderType
	    ) {
        
    	String path = env.getProperty("upload.root.path");
        if(folderType != null && !"".equals(folderType.trim())){
        	path += folderType;
        }else{
        	path += "common";
        }
        String originalFileName = file.getOriginalFilename();
        StringBuilder fileName = new StringBuilder();
        fileName.append(authenticationInjector.getAuthentication().getName());
        fileName.append("_");
        fileName.append(System.currentTimeMillis());
        fileName.append(".");
        fileName.append(originalFileName.substring(originalFileName.lastIndexOf(".") + 1));
        
        String uploadName = "";
        try {
			uploadName = FileHepler.upload(path, fileName.toString(), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.debug("Error : " + e);
		}
    	
        String fileUrl = env.getProperty("file.url") + folderType + "?fileName=" + uploadName;
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setServerFileName(uploadName);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileExtensionName(StringHelper.getExtension(originalFileName));
        fileInfo.setFilePath(path + File.separator + uploadName);
        fileInfo.setFileUrl(fileUrl);

        return fileInfo;
    	
    	
    	
    	
        /*String docPath = env.getProperty("doc.upload.path");

        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("doc.upload."+folderType+".path");
        LOGGER.debug("folderTypePath : "+folderTypePath);
        // folderType에 맞는 경로가 없는 경우 기본 경로로 잡아준다
        if(StringUtils.isNullOrEmpty(folderTypePath)){
            folderTypePath = env.getProperty("doc.upload.default.path")+File.separator+folderType;
            LOGGER.debug("folderTypePath not found(change default path): "+folderTypePath);
        }

        // 업로드 폴더 년/월 구분 추가
        folderTypePath = StringCommonUtil.getDateFolderPath(folderTypePath);
        // 파일 업로드 경로는 doc공유폴더 + 저장폴더
        String folderPath = docPath+File.separator+folderTypePath;
        
        String originalFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis()+"_"+originalFileName;
        String fileNewName = "";
        try {
            fileNewName = FileHepler.upload(folderPath, fileName, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.debug("Error : "+e);
        }
        // db에 저장되는 경로는 doc 기본 경로 제외하고 저장되어야함(front에서 요청한 내용)
        String fileFullPath = folderTypePath+File.separator+fileNewName;
        
        String fileUrl = env.getProperty("file.url")+folderType+"?fileName="+fileNewName;//임시로 특정 이미지 url 전달 
        
        long fileSize = file.getSize();
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setServerFileName(fileNewName);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileExtensionName(StringHelper.getExtension(originalFileName));
        fileInfo.setFilePath(fileFullPath);
        fileInfo.setFileUrl(fileUrl);

        return fileInfo;*/
    }
    
    @ApiOperation(value = "공통 > 파일업로드>ckeditor", notes = "공통 > 파일업로드>ckeditor")
    @RequestMapping(value="/ckeditor", method = RequestMethod.POST)
    public Object fileUploadFromCkeditor(
            @ApiParam(value="파일") @RequestParam MultipartFile upload, String type, String CKEditor, String CKEditorFuncNum, String langCode, HttpServletResponse response    ){
        
        // cdn 공유 폴더 경로
        String cdnPath = env.getProperty("cdn.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("cdn.upload.ckeditor.path")+File.separator+type;
        LOGGER.debug("folderTypePath : "+folderTypePath);

        // 업로드 폴더 년/월 구분 추가
        folderTypePath = StringCommonUtil.getDateFolderPath(folderTypePath);
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = cdnPath+File.separator+folderTypePath;
        
        String originalFileName = upload.getOriginalFilename();
        String fileName = System.currentTimeMillis()+"_"+originalFileName;
        String fileNewName = "";
        try {
            fileNewName = FileHepler.upload(folderPath, fileName, upload);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.debug("Error : "+e);
        }
        // db에 저장되는 경로는 cdn 기본 경로 제외하고 저장되어야함(front에서 요청한 내용)
        String fileFullPath = folderTypePath+File.separator+fileNewName;
        
        OutputStream out = null;
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
 
        try{
 
            //String fileName = file.getOriginalFilename();
 
 
            printWriter = new PrintWriter(response.getOutputStream());
            //String fileUrl = "저장한 URL경로/" + fileName;//url경로
 
            printWriter.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("
                    + CKEditorFuncNum
                    + ",'"
                    + fileFullPath
                    + "','A Image is uploaded.'"
                    + ")</script>");
            printWriter.flush();
            
 
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        return "";
    }
    @ApiOperation(value = "공통 > ckeditor 이미지 파일 업로드")
    @RequestMapping(value="/ckeditorDrag", method = RequestMethod.POST)
    public  @ResponseBody Object ckeditorimageFileUpload(@RequestParam MultipartFile upload,
            @ApiParam(value="folderType", defaultValue="image") @RequestParam String type
            ) {

        LOGGER.debug("imageFileUpload folderType : "+type);

        // cdn 공유 폴더 경로
        String cdnPath = env.getProperty("cdn.upload.path");

        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("cdn.upload.ckeditor.path")+File.separator+type;
        LOGGER.debug("folderTypePath : "+folderTypePath);
        // 업로드 폴더 년/월 구분 추가
        folderTypePath = StringCommonUtil.getDateFolderPath(folderTypePath);
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = cdnPath+File.separator+folderTypePath;
        
        String originalFileName = upload.getOriginalFilename();
        String fileName = System.currentTimeMillis()+"_"+originalFileName;
        String fileNewName = "";
        try {
            fileNewName = FileHepler.upload(folderPath, fileName, upload);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.debug("Error : "+e);
        }
        // db에 저장되는 경로는 cdn 기본 경로 제외하고 저장되어야함(front에서 요청한 내용)
        String fileFullPath = folderTypePath+File.separator+fileNewName;
        
        CkeditorFileInfo ckeditorFileInfo = new CkeditorFileInfo();
        ckeditorFileInfo.setFileName(fileNewName);
        ckeditorFileInfo.setUrl(fileFullPath); //상대 경로로 전달
        ckeditorFileInfo.setUploaded(1);

        return ckeditorFileInfo;
    }  
    
    
    @ApiOperation(value = "공통 > 이미지 파일 업로드(cdn업로드)", notes = "공통 > 이미지 파일 업로드(cdn업로드)")
    @RequestMapping(value="/imageFileUpload/{folderType}", method = RequestMethod.POST)
    public  @ResponseBody FileInfo imageFileUpload(@RequestParam("file") MultipartFile file,
            @ApiParam(value="folderType", defaultValue="image") @PathVariable String folderType
            ) {

        LOGGER.debug("imageFileUpload folderType : "+folderType);
        // cdn 공유 폴더 경로
        String cdnPath = env.getProperty("cdn.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("cdn.upload."+folderType+".path");
        LOGGER.debug("folderTypePath : "+folderTypePath);
                
        // folderType에 맞는 이미지 경로가 없는 경우 기본 경로로 잡아준다
        if(StringUtils.isNullOrEmpty(folderTypePath)){
            // 2020.03.03 김동균  File.separator+ 삭제
            folderTypePath =  env.getProperty("cdn.upload.default.path")+folderType;
            LOGGER.debug("folderTypePath not found(change default path): "+folderTypePath);
        }
        // 업로드 폴더 년/월 구분 추가
        //2020.03.03 김동균 파일업로드 경로 변경으로 인해 아래 주석 처리 
        //folderTypePath = StringCommonUtil.getDateFolderPath(folderTypePath);
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = cdnPath+folderTypePath;
        
        String originalFileName = file.getOriginalFilename();
        String fileExt = "." + originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        
        // 2020.03.03 김돋균 저장 파일명 로그인 사용자_System.currentTimeMillis() 로 변경
        //String fileName = System.currentTimeMillis()+"_"+originalFileName;
        String fileName = authenticationInjector.getAuthentication().getName() + "_" + System.currentTimeMillis()+ fileExt;
        String fileNewName = "";
        try {
            fileNewName = FileHepler.upload(folderPath, fileName, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.debug("Error : "+e);
        }
        // db에 저장되는 경로는 cdn 기본 경로 제외하고 저장되어야함(front에서 요청한 내용)
        //2020.03.03 김동균 DB에 경로를 풀경로로 저장함 
        String fileFullPath = folderTypePath+File.separator+fileNewName;
        
        long fileSize = file.getSize();
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setServerFileName(fileNewName);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileExtensionName(StringHelper.getExtension(originalFileName));
        fileInfo.setFilePath(fileFullPath);
        fileInfo.setFileUrl(fileFullPath);

        return fileInfo;
    }    
    @ApiOperation(value = "공통 > 이미지 파일 업로드(cdn업로드)", notes = "공통 > 이미지 파일 업로드(cdn업로드)")
    @RequestMapping(value="/imageFileUpload/{folderType}/{subType}", method = RequestMethod.POST)
    public  @ResponseBody FileInfo imageFileUploadSub(@RequestParam("file") MultipartFile file,
            @ApiParam(value="folderType", defaultValue="image") @PathVariable String folderType,
            @ApiParam(value="subType", defaultValue="image") @PathVariable String subType
            ) {

        LOGGER.debug("imageFileUpload folderType : "+folderType);
        
        // cdn 공유 폴더 경로
        String cdnPath = env.getProperty("cdn.upload.path");
        
        // folderType을 기준으로 업로드 path를 config파일에서 조회한다
        String folderTypePath = env.getProperty("cdn.upload."+folderType+".path");
        LOGGER.debug("folderTypePath : "+folderTypePath);
                
        // folderType에 맞는 이미지 경로가 없는 경우 기본 경로로 잡아준다
        if(StringUtils.isNullOrEmpty(folderTypePath)){
            folderTypePath = env.getProperty("cdn.upload.default.path")+File.separator+folderType;
            LOGGER.debug("folderTypePath not found(change default path): "+folderTypePath);
        }
        folderTypePath = folderTypePath + File.separator+subType;//폴더 경로에서 하위 타입의 경로를 붙혀준다. 
        
        // 업로드 폴더 년/월 구분 추가
        folderTypePath = StringCommonUtil.getDateFolderPath(folderTypePath);
        // 파일 업로드 경로는 cdn공유폴더 + 저장폴더
        String folderPath = cdnPath+folderTypePath;
        
        String originalFileName = file.getOriginalFilename();
        String encodeFileName = originalFileName.replaceAll("[\\u3131-\\u318E\\uAC00-\\uD7A3]+", "");
        String fileName = System.currentTimeMillis()+"_"+encodeFileName;
        String fileNewName = "";
        try {
            fileNewName = FileHepler.upload(folderPath, fileName, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.debug("Error : "+e);
        }
        // db에 저장되는 경로는 cdn 기본 경로 제외하고 저장되어야함(front에서 요청한 내용)
        String fileFullPath = folderTypePath+File.separator+fileNewName;
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setServerFileName(fileNewName);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileExtensionName(StringHelper.getExtension(originalFileName));
        fileInfo.setFilePath(fileFullPath);
        fileInfo.setFileUrl(fileFullPath);

        return fileInfo;
    }
}
