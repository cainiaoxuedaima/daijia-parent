package cn.van.daijia.driver.controller;

import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.service.CosService;
import cn.van.daijia.model.vo.driver.CosUploadVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "上传管理接口")
@RestController
@RequestMapping("file")
public class FileController {



    @Autowired
    private CosService cosService;
    //文件上传接口
    @Operation(summary = "上传")
    @VanLogin
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file,
                 @RequestParam(name="path",defaultValue = "auth") String path){
        CosUploadVo cosUploadVo =cosService.uploadFile(file,path);
        return Result.ok(cosUploadVo);
    }
}
