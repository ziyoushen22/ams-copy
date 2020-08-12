package com.example.amscopy.utils.esg.mail;

import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.utils.esg.EsgUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 该类仅适用于通过平安esg.mail平台进行服务转发业务
 */
@Component
@Data
@Slf4j
public class EmailUtil {

    @Autowired
    private EsgUtil esgUtil;

    @Value("${esg.mail.system.name}")
    private String systemName;
    @Value("${esg.mail.department.id}")
    private String departmentId;
    @Value("${esg.mail.template.name}")
    private String templateName;
    @Value("${system.contactus.link}")
    private String contactUsLink;
    @Value("${system.activate.link}")
    private String activateLink;
    @Value("${system.company.name}")
    private String companyName;

    private ResponseEntity<JSONObject> sendEmail(JSONObject jsonParams){
        return esgUtil.postOpenApi("/appsvr/public/smg/sendEmail",""+System.currentTimeMillis(),jsonParams);
    }

    public ResponseEntity<JSONObject> sendActivateEmail(String email,String orgName,String loginId){
        HashMap<String, Object> mailInfo = new HashMap<>();
        mailInfo.put("SystemName",systemName);
        mailInfo.put("DepartmentID",departmentId);
        mailInfo.put("TemplateName",templateName);
        mailInfo.put("SendDate","1970-01-01");
        // TODO 一堆参数设置 。。。

        return this.sendEmail(new JSONObject(mailInfo));



    }






}
