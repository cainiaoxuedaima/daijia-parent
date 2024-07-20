package cn.van.daijia.map.service.impl;

import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.map.service.MapService;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class MapServiceImpl implements MapService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("tencent.cloud.map")
    private String key;

    /**
     * 计算驾驶路线
     * @param calculateDrivingLineForm
     * @return
     */
    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        //请求腾讯地图接口 spring封装调用RestTemplate
        //定义腾讯地址
        String url="https://apis.map.qq.com/ws/direction/v1/driving/?from={from}&to={to}&key={key}";

        //封装传递参数
        Map<String,String>map=new HashMap<>();
        //开始位置
        //经纬度
        map.put("from",calculateDrivingLineForm.getStartPointLatitude()+","+calculateDrivingLineForm.getStartPointLongitude());
        //结束位置
        map.put("to",calculateDrivingLineForm.getEndPointLatitude()+","+calculateDrivingLineForm.getEndPointLongitude());
        //结束的值
        map.put("key",key);

        //get 请求
        JSONObject jsonObject = restTemplate.getForObject(url, JSONObject.class, map);
        int status = jsonObject.getIntValue("status");
        if(status==0){
            throw new GuiguException(ResultCodeEnum.MAP_FAIL);
        }
        JSONObject route = jsonObject.getJSONObject("result").getJSONArray("routes").getJSONObject(0);

        DrivingLineVo drivingLineVo = new DrivingLineVo();
        //方案的预估时间
        drivingLineVo.setDuration(route.getBigDecimal("duration"));
        //距离 米转换成公里
        drivingLineVo.setDistance(route.getBigDecimal("distance")
                .divideToIntegralValue(new BigDecimal("1000"))
                .setScale(2, RoundingMode.HALF_UP));
        //路线
        drivingLineVo.setPolyline(route.getJSONArray("polyline"));
        return drivingLineVo;
    }
}
