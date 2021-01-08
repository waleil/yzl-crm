package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import cn.net.yzl.product.model.vo.ImageDTO;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PostLoad;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            @ApiImplicitParam(name = "file", value = "需要上传的图片或视频", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "type",value = "文件类型(0:图片，1:视频)",paramType = "query", required = true),
            @ApiImplicitParam(name = "storeId",value = "图片库id",paramType = "query", required = true)
    })
    @PostMapping("upload")
    public ComResponse<String> uploadImage(@RequestParam("file")MultipartFile[] files,
                                           @RequestParam("type") Integer type,
                                           HttpServletRequest request,
                                           @RequestParam("storeId") Integer storeId) throws IOException {
        String result = "";//定义url集
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
                                if(imageService.selectTypeById(storeId).getData()!=0){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"无法将图片上传至视频库！");
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
                                if(imageService.selectTypeById(storeId).getData()!=1){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"无法将视频上传至图片库！");
                                }
                                if (files.length>1){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"视频只能单独上传!");
                                }
                                result+=this.upload(file,request.getHeader("userId"),type,storeId)+",";
                            }
                        }else {
                            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"文件类型不合法！");
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

    @PostMapping("creaeteAlbum")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "相册名称",paramType = "query",required = true),
            @ApiImplicitParam(name = "descri",value = "相册描述",paramType = "query"),
            @ApiImplicitParam(name = "sort",value = "排序",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "相册类型（0：图片库，1：视频库）",paramType = "query",required = true)
    })
    @ApiOperation("创建图片库相册")
    public ComResponse createAlbum(String name,
                                   @RequestParam(required = false) String descri,
                                   @RequestParam(required = false) Integer sort,
                                   Byte type,
                                   HttpServletRequest request){
        if(StringUtils.isEmpty(name)){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"相册名称不能为空！");
        }
        if (type>1 || type <0){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"非法的相册类型！");
        }
        String userId;
        if(StringUtils.isEmpty(userId=request.getHeader("userId"))){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"无法获取操作员编号，请检查登录状态！");
        }
        ImageStore is = new ImageStore();
        is.setSort(sort);
        is.setType(type);
        is.setDescri(descri);
        is.setCreateNo(userId);
        return imageService.createAlbum(is);
    }


    @GetMapping("selectByStoreId")
    @ApiOperation("通过相册id查询图片库该相册下所有图片")
    @ApiImplicitParam(name = "storeId",value = "图片库id",paramType = "query",required = true)
    public ComResponse<List<ImageDTO>> selectByStoreId(@RequestParam("storeId") Integer storeId){
        return imageService.selectByStoreId(storeId);
    }

}
