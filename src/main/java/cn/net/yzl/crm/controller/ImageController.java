package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product/v1/image")
@Api(tags = "图片管理", description = "包含：图片上传")
public class ImageController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @ApiOperation("上传图片接口")
    @PostMapping("uploadImage")
    public ComResponse<String> uploadImage(@RequestParam(value = "file",required = true)MultipartFile[] files) throws IOException {
        if (files.length == 0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "文件数量为0！");
        }else {
            for (MultipartFile file : files) {
                if (file.isEmpty()){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"存在空文件");
                }else {
                    long size = file.getSize();
                    if (size > 2<<20) {
                        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片大小超过2M限制！");
                    }else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

}
