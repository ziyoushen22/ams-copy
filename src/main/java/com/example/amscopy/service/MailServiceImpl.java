package com.example.amscopy.service;

import com.example.amscopy.dto.EmailLog;
import com.example.amscopy.service.api.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async("asyncMailTaskExecutor")
    @Override
    public EmailLog sendAndSaveMail(EmailLog emailLog, List<MultipartFile> attachments, Map<String, Resource> map) {
        checkMail(emailLog);
        sendMimeMail(emailLog,attachments,map);
        log.info("邮件投递成功");
        return null;
    }

    private void sendMimeMail(EmailLog emailLog, List<MultipartFile> attachments, Map<String, Resource> map) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            emailLog.setFrom(mailSender.getJavaMailProperties().getProperty("from"));
            messageHelper.setFrom(emailLog.getFrom());
            messageHelper.setTo(emailLog.getTo());
            messageHelper.setSubject(emailLog.getSubject());
            messageHelper.setText(emailLog.getContent(),true);
            if (StringUtils.isNotEmpty(emailLog.getCc())){
                messageHelper.setCc(emailLog.getCc().split(","));
            }
            if (attachments!=null){
                for (MultipartFile multipartFile : attachments) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(),multipartFile);
                }
            }
            messageHelper.setSentDate(emailLog.getSendDate());
            if (!CollectionUtils.isEmpty(map)){
                for (Map.Entry<String, Resource> entry : map.entrySet()) {
                    if (entry.getValue()!=null){
                        messageHelper.addInline(entry.getKey(),entry.getValue());
                    }
                }
            }
            mailSender.send(messageHelper.getMimeMessage());
            emailLog.setStatus(0);
            // TODO 插入数据库日志

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void checkMail(EmailLog emailLog) {
        if (StringUtils.isEmpty(emailLog.getFrom()))throw new RuntimeException("邮件发信人不能为空");
    }
}
