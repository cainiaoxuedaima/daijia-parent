package cn.van.daijia.driver.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.vo.driver.IdCardOcrVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-driver")
public interface OcrFeignClient {

    @PostMapping(value="/ocr/idCardOcr",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<IdCardOcrVo> idCardOcr(@RequestPart("file")MultipartFile file);
}