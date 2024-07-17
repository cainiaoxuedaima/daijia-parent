package cn.van.daijia.customer.service.impl;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.van.daijia.customer.mapper.CustomerInfoMapper;
import cn.van.daijia.customer.mapper.CustomerLoginLogMapper;
import cn.van.daijia.customer.service.CustomerInfoService;
import cn.van.daijia.model.entity.customer.CustomerInfo;
import cn.van.daijia.model.entity.customer.CustomerLoginLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {


    //调用微信工具包
    @Autowired
    private WxMaService wxMaService;

    //调用配置类
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    //登录日志
    @Autowired
    private CustomerLoginLogMapper customerLoginLogMapper;
    //登录接口
    @Override
    public Long login(String code) {
        //1.获取到code值，使用微信工具包对象，获取微信唯一标识
        String openId=null;
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            openId = sessionInfo.getOpenid();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        //2.根据openid查询数据库表，判断是否第一次登录
        //如果openid不存在返回null，如果存在返回一条记录
        //select * from customer_info ci where ci.wx_open_id='' 用mybatis-plus实现
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerInfo::getWxOpenId,openId);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(wrapper);

        //3. 如果第一次登录，添加信息到用户表
        if(customerInfo==null){
            customerInfo=new CustomerInfo();
            customerInfo.setNickname(String.valueOf(System.currentTimeMillis()));
            customerInfo.setAvatarUrl("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");//oss阿里云默认的头像
            customerInfo.setWxOpenId(openId);
            customerInfoMapper.insert(customerInfo);
        }
        //4.记录登录的日志信息
        CustomerLoginLog customerLoginLog = new CustomerLoginLog();
        customerLoginLog.setCustomerId(customerInfo.getId());
        customerLoginLog.setMsg("微信小程序登录");
        customerLoginLogMapper.insert(customerLoginLog);
        //5.返回用户id
        return customerInfo.getId();
    }
}
