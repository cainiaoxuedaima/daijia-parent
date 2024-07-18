package cn.van.daijia.driver.service;

import cn.van.daijia.driver.client.CosFeignClient;
import cn.van.daijia.model.vo.driver.CosUploadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public interface CosService {

    //文件上传接口
    CosUploadVo uploadFile(MultipartFile file, String path);
}
