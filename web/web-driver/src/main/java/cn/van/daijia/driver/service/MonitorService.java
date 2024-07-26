package cn.van.daijia.driver.service;

import cn.van.daijia.model.form.order.OrderMonitorForm;
import org.springframework.web.multipart.MultipartFile;

public interface MonitorService {

    //上传录音
    Boolean upload(MultipartFile file, OrderMonitorForm orderMonitorForm);
}
