package cn.net.yzl.crm.controller.file;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("file/v1")
@Api("上传接口")
public class FileController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private FastDFSConfig fastDFSConfig;

    @PostMapping("uploadExcel")
    @ApiOperation("excel上传接口")
    @ApiImplicitParam(name = "file", value = "需要上传的Excel", required = true, dataType = "MultipartFile")
    public ComResponse upload(MultipartFile file, HttpServletRequest request) throws IOException {

        String userId = request.getHeader("userId");

        if(StringUtils.isEmpty(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"获取登录状态失败，请尝试重新登陆！");
        }

        if(null == file||file.getSize()==0){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"文件为空，请检查！");
        }
        long size = file.getSize();
        if(size > 10<<20){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"文件过大，上传文件的最大限制为10M！");
        }
        String fileName = file.getOriginalFilename();
        if(StringUtils.isEmpty(fileName)||(!fileName.endsWith(".xlsx")&&!fileName.endsWith("xls"))){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"请上传正确的Excel文件！");
        }

        StorePath upload = fastdfsUtils.upload(file);

        String path = fastDFSConfig.getUrl()+"/"+upload.getFullPath();

        return ComResponse.success(path);

    }


}
