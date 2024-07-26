package cn.van.daijia.driver.service;

import cn.van.daijia.model.vo.order.TextAuditingVo;

public interface CiService {

    //图片审核
    Boolean imagesAuditing(String path);

    //文本审核
    TextAuditingVo textAuditing(String content);
}
