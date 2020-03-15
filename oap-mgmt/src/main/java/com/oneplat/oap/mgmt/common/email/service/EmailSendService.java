package com.oneplat.oap.mgmt.common.email.service;

import com.oneplat.oap.core.util.StringHelper;
import com.oneplat.oap.mgmt.common.email.model.MailRequest;
import com.oneplat.oap.mgmt.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author lee
 * @date 2016-12-29
 */
@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    @Async("threadPoolTaskExecutor")
    public void send(MailRequest mailRequest){
        try {
                File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + mailRequest.getEmailTemplateCode().getPath());
                String htmContent =  FileUtil.readFile(file,"utf-8");
                htmContent = StringHelper.convertInfoMail(htmContent, mailRequest.getConvertInfoMap());
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
                messageHelper.setTo(mailRequest.getReceiverEmails());
                messageHelper.setText(htmContent, true);
                messageHelper.setSubject(mailRequest.getEmailTemplateCode().getTitle());	// 메일제목은 생략이 가능하다
                mailSender.send(message);
            } catch(Exception e){
                e.printStackTrace();
            }
    }
}
