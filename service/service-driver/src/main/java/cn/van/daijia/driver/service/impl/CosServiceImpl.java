package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.driver.config.TencentCloudProperties;
import cn.van.daijia.driver.service.CiService;
import cn.van.daijia.driver.service.CosService;
import cn.van.daijia.model.vo.driver.CosUploadVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CosServiceImpl implements CosService {

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    private CiService ciService;
    @Override
    public CosUploadVo upload(MultipartFile file, String path) {
        //获取COSClient对象
        COSClient cosClient = this.getCosClient();
        //文件上传

        //元数据信息
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        meta.setContentEncoding("UTF-8");
        meta.setContentType(file.getContentType());

        //向存储桶中保存文件
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //文件后缀名
        String uploadPath = "/driver/" + path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;
        // 01.jpg
        // /driver/auth/0o98754.jpg
        PutObjectRequest putObjectRequest = null;
        try {
            //1 bucket名称
            //2
            putObjectRequest = new PutObjectRequest(tencentCloudProperties.getBucketPrivate(),
                    uploadPath,
                    file.getInputStream(),
                    meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest); //上传文件
        cosClient.shutdown();

        //图片审核
        Boolean imageAuditing = ciService.imagesAuditing(uploadPath);
        if(!imageAuditing) {
            //删除违规图片
            cosClient.deleteObject(tencentCloudProperties.getBucketPrivate(),uploadPath);
            throw new GuiguException(ResultCodeEnum.IMAGE_AUDITION_FAIL);
        }
        //返回vo对象
        CosUploadVo cosUploadVo = new CosUploadVo();
        cosUploadVo.setUrl(uploadPath);
        // 图片临时访问url，回显使用
        String imageUrl = this.getImageUrl(uploadPath);
        cosUploadVo.setShowUrl(imageUrl);
        cosUploadVo.setShowUrl("");



        return cosUploadVo;
    }

    //对方法做封装
    public COSClient getCosClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = tencentCloudProperties.getSecretId();
        String secretKey = tencentCloudProperties.getSecretKey();
        COSCredentials cred= new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(tencentCloudProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
    //获取临时签名URL
    @Override
    public String getImageUrl(String path) {
        if(!StringUtils.hasText(path))return "";
        //获取cosclient对象
        COSClient cosClient = this.getCosClient();
        //GeneratePresignedUrlRequest
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(
                        tencentCloudProperties.getBucketPrivate(), path, HttpMethodName.GET);
        //设置临时URL有效期为15分钟
        Date date = new DateTime().plusMinutes(15).toDate();
        request.setExpiration(date);
        //调用方法
        URL url = cosClient.generatePresignedUrl(request);
        return url.toString();
    }
}
