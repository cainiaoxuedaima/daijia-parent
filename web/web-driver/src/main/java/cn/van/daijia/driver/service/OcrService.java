package cn.van.daijia.driver.service;

import cn.van.daijia.model.vo.driver.DriverLicenseOcrVo;
import cn.van.daijia.model.vo.driver.IdCardOcrVo;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    //身份验证
    IdCardOcrVo idCardOcr(MultipartFile file);

    //驾驶证验证
    DriverLicenseOcrVo driverLicenseOcr(MultipartFile file);
}
