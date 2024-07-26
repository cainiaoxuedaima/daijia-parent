package cn.van.daijia.driver.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.service.CiService;
import cn.van.daijia.model.vo.order.TextAuditingVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "腾讯云CI审核接口管理")
@RestController
@RequestMapping(value="/cos")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CiController {

    @Autowired
    private CiService ciService;

    @Operation(summary = "文本审核")
    @PostMapping("/textAuditing")
    public Result<TextAuditingVo> textAuditing(@RequestBody String content) {
        return Result.ok(ciService.textAuditing(content));
    }

}

