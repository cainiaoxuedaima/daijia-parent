package cn.van.daijia.driver.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "service-driver")
public interface OcrFeignClient {


}