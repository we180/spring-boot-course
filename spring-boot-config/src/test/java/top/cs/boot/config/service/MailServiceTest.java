package top.cs.boot.config.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import top.cs.boot.config.model.Mail;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class MailServiceTest {
    @Resource
    private MailService mailService;

    @Test
    void sendSimpleMail() {
        Mail mail = new Mail();
        mail.setTo("1805825699@qq.com");
        mail.setSubject("测试邮件0721");
        mail.setText("测试邮件内容114514");
        mailService.sendSimpleMail(mail);
    }

    @Test
    void sendAttachmentsMail() throws IOException {
        Mail mail = new Mail();
        mail.setTo("1805825699@qq.com");
        mail.setSubject("测试附件邮件");
        mail.setText("测试邮件内容123456");
        //本地文件
        File file1 = new File("D:\\软件工程\\前端框架\\image\\z4.jpg");
        File file2 = new File("D:\\软件工程\\前端框架\\image\\a5.jpg");
        MultipartFile[] files = new MultipartFile[2];
        files[0] = new MockMultipartFile(
                file1.getName(),
                file1.getName(),
                "image/jpeg",
                FileCopyUtils.copyToByteArray(file1)
        );
        files[1] = new MockMultipartFile(
                file2.getName(),
                file2.getName(),
                "image/jpeg",
                FileCopyUtils.copyToByteArray(file2)
        );
        mailService.sendAttachmentsMail(mail, files);
    }


    @Test
    void sendHtmlMail() {
        Mail mail = new Mail();
        mail.setTo("1805825699@qq.com");
        mail.setSubject("测试html邮件");
        String content = """
                <!DOCTYPE html>
                <html>
                  <head>
                    <meta charset="UTF-8" />
                    <style>
                      body {
                        font-family: "Microsoft YaHei", Arial, sans-serif;
                        background-color: #f6f8fa;
                        margin: 0;
                        padding: 0;
                      }
                      .container {
                        max-width: 600px;
                        margin: 30px auto;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                      }
                      .header {
                        background: #2d8cf0;
                        color: #fff;
                        text-align: center;
                        padding: 20px;
                        font-size: 24px;
                        font-weight: bold;
                      }
                      .content {
                        padding: 30px;
                        color: #333;
                        line-height: 1.6;
                      }
                      .content h2 {
                        color: #2d8cf0;
                      }
                      .button {
                        display: inline-block;
                        margin: 20px 0;
                        padding: 12px 28px;
                        background: #2d8cf0;
                        color: #fff;
                        text-decoration: none;
                        border-radius: 4px;
                        font-size: 16px;
                      }
                      .note {
                        font-size: 13px;
                        color: #888;
                        margin-top: 15px;
                      }
                      .footer {
                        background: #f1f1f1;
                        text-align: center;
                        font-size: 12px;
                        color: #666;
                        padding: 15px;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <div class="header">账号注册成功确认</div>
                      <div class="content">
                        <h2>亲爱的用户，欢迎加入！</h2>
                        <p>
                          您的账号已经注册成功，请点击下方按钮完成邮箱确认，开启完整功能体验：
                        </p>
                        <a href="https://example.com/verify?token=xxxxxx" class="button"
                          >确认我的邮箱</a
                        >
                        <p class="note">如果按钮无法点击，请复制以下链接到浏览器打开：</p>
                        <p class="note">https://example.com/verify?token=xxxxxx</p>
                        <hr style="margin: 30px 0" />
                        <p>
                          为了保障您的账号安全，本链接将在
                          <b>24小时内失效</b>。如果您没有注册该账号，请忽略此邮件。
                        </p>
                      </div>
                      <div class="footer">
                        © 2025 示例平台 · 本邮件由系统自动发送，请勿直接回复
                      </div>
                    </div>
                  </body>
                </html>
                
                """;
        mail.setText(content);
        mailService.sendHtmlMail(mail);
    }

}