package top.cs.boot.config.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.cs.boot.config.config.OssConfig;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class OssServiceImpl implements OssService{
    @Resource
    private OssConfig ossConfig;
    @Override
    public String upload(MultipartFile file){
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            //1.1.1.jpg获取后缀名
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + suffix;
            log.info("新文件名:{}",newFileName);
            //读取配置文件
            String endpoint = ossConfig.getEndpoint();
            String bucket = ossConfig.getBucket();
            String accessKeyId = ossConfig.getAccessKeyId();
            String accessKeySecret = ossConfig.getAccessKeySecret();
            String dir = ossConfig.getDir();
            //创建OSS客户端
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //设置ContentType是image/jpeg,可以在浏览器预览图片
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/jpeg");
            meta.setContentDisposition("inline");
            String uploadPath = dir + newFileName;
            InputStream inputStream;
            try {
                inputStream = file.getInputStream();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //调用SDK,实现文件上传
            ossClient.putObject(bucket, uploadPath, inputStream, meta);
            //关闭客户端
            ossClient.shutdown();
            return "https://" + bucket + "." + endpoint + "/" + uploadPath;
        }
        return "上传失败";
    }
}
