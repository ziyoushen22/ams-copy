package com.example.amscopy.task;

import com.example.amscopy.dto.EmailLog;
import com.example.amscopy.service.api.MailService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PublicOpinionMonitoringTask {
    @Autowired
    private MailService mailService;

    private static final String cid = "MailHeadCut";
    private static Map<String, Resource> map = new HashMap<>();

    static {
        ClassPathResource resource = new ClassPathResource("static/img/MailHeadCut.png");
        map.put(cid, resource);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "publicOpinionMonitoringForSendMail", lockAtLeastForString = "PT5S")
    public void publicOpinionMonitoringForSendMail() {
        EmailLog emailLog = new EmailLog();

        ArrayList<String> toMailList = new ArrayList<>();
        emailLog.setTo(String.join(",", toMailList));
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div style=''>")
                .append("<img style=''/>")
                //TODO
                .append("</div>");
        emailLog.setContent(buffer.toString());
        mailService.sendAndSaveMail(emailLog,null,map);
    }

}
