package cn.van.daijia.order.testLock;

import io.micrometer.common.util.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/25 15:29
 */
@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

//    //本地锁
//    @Override
//    public synchronized void testLock(){
//        //从redis里面获取数据
//        String value = redisTemplate.opsForValue().get("num");
//        if(StringUtils.isBlank(value)){
//            return;
//        }
//
//
//        //把redis从获取数据+1
//        int num=Integer.parseInt(value);
//        //加1之后返回到redis里面
//        redisTemplate.opsForValue().set("num",String.valueOf(++num));
//    }

    //分布式锁
//    @Override
//    public  void testLock(){
//        String uuid= UUID.randomUUID().toString();
//        //从redis里面获取数据
//        Boolean lock =
//                redisTemplate.opsForValue()
//                            .setIfAbsent("lock:", uuid,3, TimeUnit.SECONDS);
//        if(lock){
//            String value = redisTemplate.opsForValue().get("num");
//            if(StringUtils.isBlank(value)){
//                return;
//            }
//            //把redis从获取数据+1
//            int num=Integer.parseInt(value);
//            //加1之后返回到redis里面
//            redisTemplate.opsForValue().set("num",String.valueOf(++num));
//
//            //3 释放锁
//            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//            //Lua脚本
//            String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
//                    "then\n" +
//                    "    return redis.call(\"del\",KEYS[1])\n" +
//                    "else\n" +
//                    "    return 0\n" +
//                    "end";
//            redisScript.setScriptText(script);
//            redisScript.setResultType(Long.class);
//            redisTemplate.execute(redisScript, Arrays.asList("lock"),uuid);
////            if(uuid.equals(redisTemplate.opsForValue().get("lock:"))){
////                redisTemplate.delete("lock");
////            }
//            //释放锁
//            redisTemplate.delete("lock");
//        }else{
//            try{
//                Thread.sleep(100);
//                this.testLock();
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }



    @Override
    public void testLock()  {
        //1 通过redisson创建锁对象
        RLock lock = redissonClient.getLock("lock1");

        //2 尝试获取锁
        //(1) 阻塞一直等待直到获取到 默认过期时间30s

        lock.lock();

//        //（2）
//        lock.lock(10, TimeUnit.SECONDS);
//
//        //(3)
//        lock.tryLock(30,10,TimeUnit.SECONDS);
        //3 编写业务代码
        String value = redisTemplate.opsForValue().get("num");
        if(StringUtils.isBlank(value)){
            return;
        }
        int num=Integer.parseInt(value);
        redisTemplate.opsForValue().set("num",String.valueOf(++num));
        //4 释放锁
        lock.unlock();
    }
}
