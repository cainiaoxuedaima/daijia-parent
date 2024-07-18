package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.client.CosFeignClient;
import cn.van.daijia.driver.service.CosService;
import cn.van.daijia.model.vo.driver.CosUploadVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CosServiceImpl implements CosService {


    @Autowired
    private CosFeignClient cosFeignClient;

    @Override
    public CosUploadVo uploadFile(MultipartFile file, String path) {
        Result<CosUploadVo> cosUploadVoVoResult=cosFeignClient.upload(file,path);
        CosUploadVo cosUploadVo = cosUploadVoVoResult.getData();
        return cosUploadVo;
    }













}
