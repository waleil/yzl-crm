package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
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

@RestController
@RequestMapping("/product/image")
public class ImageController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @ApiOperation("上传图片接口")
    @PostMapping("uploadImage")
    public ComResponse<String> uploadImage(@RequestParam(value = "file",required = false) @Valid @NotNull(message = "请选择图片或视频!")  MultipartFile file) throws IOException {
        StorePath upload = fastdfsUtils.upload(file);
        return ComResponse.success(upload.getPath());
    }

}
