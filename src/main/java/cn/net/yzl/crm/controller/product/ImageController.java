package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.product.ImageService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.image.ImageVO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreVO;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public ComResponse<List<ImageDTO>> uploadImage(@RequestParam("file")MultipartFile[] files,
                                                  @RequestParam("type") Integer type,
                                                  HttpServletRequest request,
                                                  @RequestParam("storeId") Integer storeId) throws IOException {
        List<ImageDTO> list = new ArrayList<>();
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"非法的用户名！请检查您的登录状态！");
        }
        if (files.length == 0||files.length>15) {//开始判断
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "文件数量为"+files.length+",需要为1-15张！");
        }else {
            for (MultipartFile file : files) {//循环
                if (file.isEmpty()){//非空
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"存在空文件！");
                }else {
                    long size = file.getSize();//以Byte为单位
                        String fileName = file.getOriginalFilename();
                        if (type == 0){//图片
                            if (size > 2<<20) {//大小，最大2M
                                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片大小超过2M限制！");
                            } else {
                                //判断后缀
                                if (!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")&&!fileName.endsWith("jpeg")&&!fileName.endsWith(".gif")){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png/jpeg格式文件");
                                }
                                Integer i = imageService.selectTypeById(storeId).getData();
                                if(i == null){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"目标库不存在！");
                                }
                                if(i != 0){//图片库和类型是否匹配
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"无法将图片上传至视频库！");
                                }
                                //全部通过后上传
                                list.add(this.upload(file,request.getHeader("userId"),type,storeId));
                            }
                        }else if(type == 1) {//视频
                            if (size > 10<<20) {//最大10M
                                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"视频大小超过10M限制！");
                            }else {
                                //判断格式
                                if (!fileName.endsWith(".mp4")&&!fileName.endsWith(".mpeg")&&!fileName.endsWith(".wmv")&&!fileName.endsWith(".avi")&&!fileName.endsWith(".mov")){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传mp4/mpeg/wmv/avi/mov格式文件");
                                }
                                Integer i = imageService.selectTypeById(storeId).getData();
                                if(i == null){
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"目标库不存在！");
                                }
                                if(i != 1){//类型与库是否匹配
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"无法将视频上传至图片库！");
                                }
                                if (files.length>1){//视频只能单独上传
                                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"视频只能单独上传!");
                                }
                                //全部判断通过后开始上传
                                list.add(this.upload(file,request.getHeader("userId"),type,storeId));
                            }
                        }else {
                            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"文件类型不合法！");
                        }
                    }
                }
            }
        return ComResponse.success(list).setMessage(fastDFSConfig.getUrl()+"/");
    }

    private ImageDTO upload(MultipartFile file,String userId,Integer type,Integer storeId) throws IOException {
        ImageVO image = new ImageVO();
        StorePath storePath = fastdfsUtils.upload(file);
        String filePath = storePath.getFullPath();
        image.setUrl(filePath);
        image.setCreateTime(new Date());
        image.setCreator(userId);
        image.setType(type);
        image.setImageStoreId(storeId);
        Integer id = (Integer) imageService.insert(image).getData();
        ImageDTO dto = new ImageDTO();
        dto.setId(id);
        dto.setUrl(filePath);
        return dto;
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
        ImageStoreVO is = new ImageStoreVO();
        is.setName(name);
        is.setType(type);
        is.setDescri(descri);
        is.setCreateNo(userId);
        return imageService.createAlbum(is);
    }


    @GetMapping("selectByStoreId")
    @ApiOperation("通过相册id查询图片库该相册下所有图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId",value = "图片库id",paramType = "query",required = true),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query", required = true)
    })
    public ComResponse<Page<ImageDTO>> selectByStoreId(@RequestParam("storeId") Integer storeId
            , @RequestParam("pageNo") Integer pageNo
            , @RequestParam("pageSize") Integer pageSize){
        return imageService.selectByStoreId(storeId,pageNo,pageSize);
    }


    @GetMapping("selectStores")
    @ApiOperation("根据分类获取所有图片库列表")
    @ApiImplicitParam(name = "type",value = "库类型（0：图片库，1：视频库）",paramType = "query", required = true)
    public ComResponse<List<ImageStoreDTO>> selectStores(@RequestParam("type") Integer type){
        return imageService.selectStores(type);
    }

    @ApiOperation("删除图片")
    @GetMapping("v1/deleteById")
    public ComResponse deleteById(@RequestParam("id") Integer id,HttpServletRequest request){
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(userId)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"非法的用户名，请检查您的登录状态！");
        }
        return imageService.deleteById(id,userId);
    }

    @ApiOperation("删除图片库")
    @GetMapping("v1/deleteStoreById")
    public ComResponse deleteStoreById(@RequestParam("id") Integer id,HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(userId)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"非法的用户名，请检查您的登录状态！");
        }
        return imageService.deleteStoreById(id,userId);
    }




}
