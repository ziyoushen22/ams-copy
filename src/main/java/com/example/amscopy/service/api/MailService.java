package com.example.amscopy.service.api;

import com.example.amscopy.dto.EmailLog;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MailService {

    EmailLog sendAndSaveMail(EmailLog emailLog, List<MultipartFile> attachments, Map<String, Resource> map);

}
