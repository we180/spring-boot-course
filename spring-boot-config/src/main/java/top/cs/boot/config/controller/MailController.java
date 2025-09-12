package top.cs.boot.config.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cs.boot.config.common.ApiResponse;
import top.cs.boot.config.enums.ResultStatus;
import top.cs.boot.config.model.Mail;
import top.cs.boot.config.service.MailService;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Resource
    private MailService mailService;

    @PostMapping("/simple")
    public ResponseEntity<ApiResponse<ResultStatus>> sendSimpleMail(@Valid @RequestBody Mail mail){
        ResultStatus rs = mailService.sendSimpleMail(mail);
        if(rs == ResultStatus.SUCCESS){
            return ResponseEntity.ok(ApiResponse.success("发送成功",rs));
        }
        //业务失败返回400
        return ResponseEntity.ok(ApiResponse.error("发送失败"));
    }

//    @@PostMapping("/html")
//    public ResponseEntity<ApiResponse<ResultStatus>> sendHtmlMail(@Valid @RequestBody Mail mail){
//        ResultStatus rs = mailService.sendHtmlMail(mail);
//        return rs == ResultStatus.SUCCESS ?
//                ResponseEntity.ok(ApiResponse.success("发送成功",rs)) :
//                ResponseEntity.badRequest().body(ApiResponse.error("发送失败"));
//    }
}
