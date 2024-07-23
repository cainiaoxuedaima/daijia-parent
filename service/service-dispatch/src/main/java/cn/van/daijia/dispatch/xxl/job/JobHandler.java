package cn.van.daijia.dispatch.xxl.job;

import cn.van.daijia.dispatch.mapper.XxlJobLogMapper;
import cn.van.daijia.dispatch.service.NewOrderService;
import cn.van.daijia.model.entity.dispatch.XxlJobLog;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/23 18:47
 */
@Component
public class JobHandler {

    @Autowired
    private XxlJobLogMapper xxlJobLogMapper;

    @Autowired
    private NewOrderService newOrderService;

    @XxlJob("newOrderTaskHandler")
    public void newOrderTaskHandler(){

        //记录任务调度日志
        XxlJobLog xxlJobLog = new XxlJobLog();
        xxlJobLog.setJobId(XxlJobHelper.getJobId());
        long startTime = System.currentTimeMillis();
        try{
            //执行任务：搜索附近代驾司机
            //todo
            newOrderService.executeTask(XxlJobHelper.getJobId());
            //成功状态
            xxlJobLog.setStatus(1);
        }catch(Exception e){
            xxlJobLog.setStatus(0);
            xxlJobLog.setError(e.getMessage());
            e.printStackTrace();
        }finally{
            xxlJobLog.setTimes((int) (System.currentTimeMillis()-startTime));
            xxlJobLogMapper.insert(xxlJobLog);
        }
    }
}
