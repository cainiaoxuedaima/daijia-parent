package cn.van.daijia.common.login;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.common.util.AuthContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/18 17:46
 */
@Component
@Aspect
public class VanLoginAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    //环绕通知，登录判断
    //切入点表达式：制定对哪些规则的方法进行增强
    @Around("execution(* cn.van.daijia.*.controller.*.*(..))&& @annotation(vanLogin)")
    public Object login(ProceedingJoinPoint proceedingJoinPoint,VanLogin vanLogin) throws Throwable{
        //1. 获取request对象
        RequestAttributes attributes= RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra= (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();
        //2.从请求头获取token
        String token = request.getHeader("token");
        //3. 判断token是否为空，如果为空，返回登录提示
        if(!StringUtils.isEmpty(token)){
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        //4. token不为空，查询redis
        String customerId = (String) redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);
        //5. 查询redis对应的用户id，把用户放在ThreadLocal中
        if(StringUtils.hasText(customerId)){
            AuthContextHolder.setUserId(Long.valueOf(customerId));
        }
        //6. 执行业务方法
        return proceedingJoinPoint.proceed();
    }
}
