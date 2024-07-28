package cn.van.daijia.driver.service.impl;

import cn.van.daijia.driver.client.CiFeignClient;
import cn.van.daijia.driver.service.FileService;
import cn.van.daijia.driver.service.MonitorService;
import cn.van.daijia.model.entity.order.OrderMonitorRecord;
import cn.van.daijia.model.form.order.OrderMonitorForm;
import cn.van.daijia.model.vo.order.TextAuditingVo;
import cn.van.daijia.order.client.OrderMonitorFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private FileService fileService;

    @Autowired
    private OrderMonitorFeignClient orderMonitorFeignClient;

    @Autowired
    private CiFeignClient ciFeignClient;

    /**
     * 上传录音
     * @param file
     * @param orderMonitorForm
     * @return
     */
    @Override
    public Boolean upload(MultipartFile file, OrderMonitorForm orderMonitorForm) {
        //文件上传
        String url = fileService.upload(file);
        OrderMonitorRecord orderMonitorRecord=new OrderMonitorRecord();
        orderMonitorRecord.setOrderId(orderMonitorForm.getOrderId());
        orderMonitorRecord.setFileUrl(url);
        orderMonitorRecord.setContent(orderMonitorForm.getContent());
        //增加文本审核
        TextAuditingVo textAuditingVo =
                ciFeignClient.textAuditing(orderMonitorForm.getContent()).getData();
        orderMonitorRecord.setResult(textAuditingVo.getResult());
        orderMonitorRecord.setKeywords(textAuditingVo.getKeywords());

        
        orderMonitorFeignClient.saveMonitorRecord(orderMonitorRecord);
        return true;
    }
}
