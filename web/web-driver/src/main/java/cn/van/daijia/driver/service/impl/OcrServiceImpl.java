package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.client.OcrFeignClient;
import cn.van.daijia.driver.service.OcrService;
import cn.van.daijia.model.vo.driver.DriverLicenseOcrVo;
import cn.van.daijia.model.vo.driver.IdCardOcrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OcrServiceImpl implements OcrService {


    @Autowired
    private OcrFeignClient ocrFeignClient;
    /**
     * 身份证识别
     * @param file
     * @return
     */
    @Override
    public IdCardOcrVo idCardOcr(MultipartFile file) {
        Result<IdCardOcrVo> ocrVoResult = ocrFeignClient.idCardOcr(file);
        IdCardOcrVo idCardOcrVo=ocrVoResult.getData();
        return idCardOcrVo;
    }


    /**
     * 驾驶证验证
     * @param file
     * @return
     */
    //驾驶证识别
    @Override
    public DriverLicenseOcrVo driverLicenseOcr(MultipartFile file) {
        Result<DriverLicenseOcrVo> driverLicenseOcrVoResult = ocrFeignClient.driverLicenseOcr(file);
        DriverLicenseOcrVo driverLicenseOcrVo = driverLicenseOcrVoResult.getData();
        return driverLicenseOcrVo;
    }
}
