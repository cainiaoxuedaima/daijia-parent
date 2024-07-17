package cn.van.daijia.customer.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.customer.client.CustomerInfoFeignClient;
import cn.van.daijia.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerServiceImpl implements CustomerService {

    //注入远程调用接口
    @Autowired
    private CustomerInfoFeignClient client;

    //注入Redis
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String login(String code) {
        //1.获取code进行远程调用，返回用户id
        Result<Long> login = client.login(code);

        //2. 判断如果失败了，返回错误提示
        Integer codeResult=login.getCode();
        if (codeResult!=200) {
            throw new GuiguException(ResultCodeEnum.FAIL);
        }
        //3. 获取远程调用返回用户id
        Long customer = login.getData();
        //4 判断返回的用户id是否为空 如果为空，返回错误提示
        if(customer==null){
            throw new GuiguException(ResultCodeEnum.FAIL);
        }
        //5.生成token字符串
        String token = UUID.randomUUID().toString().replaceAll("-","");

        //6.吧用户id放到redis 设置过期时间
        //key是token value是customer id
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX+token,
                customer.toString(),
                RedisConstant.USER_LOGIN_KEY_TIMEOUT,
                TimeUnit.MINUTES);
        //7.返回token
        return token;
    }
}
