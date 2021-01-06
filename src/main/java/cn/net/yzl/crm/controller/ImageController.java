package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.db.Image;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/product/v1/image")
@Api(tags = "图片管理", description = "包含：图片/视频上传")
public class ImageController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FastDFSConfig fastDFSConfig;

    @ApiOperation("上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "文件",paramType = "query",required = true),
            @ApiImplicitParam(name = "type",value = "文件类型(0:图片，1:视频)",paramType = "query", required = true),
            @ApiImplicitParam(name = "storeId",value = "图片库id",paramType = "query", required = true)
    })
    @PostMapping("upload")
    public ComResponse<String> uploadImage(@RequestParam("file")MultipartFile[] files,
                                           @RequestParam("type") Integer type,
                                           HttpServletRequest request,
                                           @RequestParam("storeId") Integer storeId) throws IOException {
        String result = "";
        if (files.length == 0||files.length>15) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "文件数量为"+files.length+",需要为1-15张！");
        }else {
            for (MultipartFile file : files) {
                if (file.isEmpty()){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"存在空文件！");
                }else {
                    long size = file.getSize();

                        String fileName = file.getOriginalFilename();
                        if (type == 0){
                            if (size > 2<<20) {
                                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片大小超过2M限制！");
                            } else {
                                if (!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")&&!fileName.endsWith("jpeg")&&!fileName.endsWith(".gif")){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png/jpeg格式文件");
                                }
                                result+=this.upload(file,request.getHeader("userId"),type,storeId)+",";
                            }
                        }else if(type == 1) {
                            if (size > 10<<20) {
                                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"视频大小超过10M限制！");
                            }else {
                                if (!fileName.endsWith(".mp4")&&!fileName.endsWith(".mpeg")&&!fileName.endsWith(".wmv")&&!fileName.endsWith(".avi")&&!fileName.endsWith(".mov")){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传mp4/mpeg/wmv/avi/mov格式文件");
                                }
                                result+=this.upload(file,request.getHeader("userId"),type,storeId)+",";
                            }
                        }
                    }
                }
            }
        return ComResponse.success(result.substring(0,result.lastIndexOf(",")));
    }

    private String upload(MultipartFile file,String userId,Integer type,Integer storeId) throws IOException {
        Image image = new Image();
        StorePath storePath = fastdfsUtils.upload(file);
        String filePath = storePath.getFullPath();
        image.setUrl(filePath);
        image.setCreateTime(new Date());
        image.setUpdateTime(new Date());
        image.setCreator(userId);
        image.setUpdator(userId);
        image.setType(type);
        image.setImageStoreId(storeId);
        imageService.insert(image);
        return fastDFSConfig.getUrl()+"/"+filePath;
    }

}
