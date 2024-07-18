package cn.van.daijia.common.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/18 17:43
 */
//登录判断
    //原始注解
@Target(ElementType.METHOD)//表示执行的类的方法
@Retention(RetentionPolicy.RUNTIME)//表示注解在运行时生效
public @interface VanLogin {

}
