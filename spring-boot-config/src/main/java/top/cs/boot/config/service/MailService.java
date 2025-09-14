package top.cs.boot.config.service;

import org.springframework.web.multipart.MultipartFile;
import top.cs.boot.config.enums.ResultStatus;
import top.cs.boot.config.model.Mail;

public interface MailService {
    ResultStatus sendSimpleMail(Mail mail);

    ResultStatus sendHtmlMail(Mail mail);

    ResultStatus sendAttachmentsMail(Mail mail, MultipartFile[] files);
}
