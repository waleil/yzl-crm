package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.service.BrandService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.vo.brand.BrandDelVO;
import cn.net.yzl.product.model.vo.brand.BrandVO;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author majinbao
 * @version 1.0
 * @title: BrandController
 * @description 品牌控制层
 * @date: 2021/1/4 20:57
 */
@RestController
@RequestMapping("/product/v1/brand")
@Api(tags = "商品品牌管理", description = "包含：增删改查")
public class BrandController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private FastDFSConfig fastDFSConfig;

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "分页查询品牌")
    @GetMapping("selectPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", paramType="query",value = "关键词", dataType = "String"),
            @ApiImplicitParam(name = "pageNo", paramType="query",value = "页码", dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",paramType="query", value = "每页显示记录数", dataType = "int",defaultValue = "15")
    })
    public ComResponse getAllBrands(@RequestParam(required = false,defaultValue = "1",value = "pageNo") Integer pageNo,
                                    @RequestParam(required = false,defaultValue = "15",value = "pageSize") Integer pageSize,
                                    String keyword) {
        if (pageSize>50) {
            pageSize=50;
        }
        return brandService.getAllBrands(pageNo, pageSize,keyword);
    }

    @ApiOperation(value = "通过id精确查询品牌")
    @GetMapping("selectById")
    @ApiImplicitParam(name = "id", value = "主键信息", required = true, dataType = "Integer",paramType = "query")
    public ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id) {
        ComResponse comResponse = brandService.getBrandById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        BrandBean brandBean = (BrandBean)comResponse.getData();
        brandBean.setBrandUrl(StringUtils.isNotEmpty(brandBean.getBrandUrl())?fastDFSConfig.getUrl()+"/"+brandBean.getBrandUrl():null);
        return ComResponse.success(brandBean);
    }

    @ApiOperation(value = "修改品牌状态")
    @GetMapping("changeStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag",paramType="query",dataType = "Integer",required = true,value = "是否启用（1：启用，0：禁用）"),
            @ApiImplicitParam(name = "id",paramType="query",dataType = "Integer",required = true,value = "id")
    })
    public ComResponse<Void> changeBrandStatus(@RequestParam("flag") Integer flag,
                                               @RequestParam("id") Integer id) {
        if (flag == 1||flag==0) {
            return brandService.changeBrandStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "新增品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = false, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", paramType="query",value = "品牌名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", paramType="query",value = "品牌故事", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sort", paramType="query",value = "排序", required = false, dataType = "Integer") })
    @PostMapping("insert")
    public ComResponse insertBrand(MultipartFile file, HttpServletRequest request,
                                   String name, String descri, int sort) {
        try {
            BrandVO brandVO = new BrandVO();
            if(file!=null){
                long size = file.getSize() / 1024; //kb
                if (size > 50) { //判断图片大小 单位Kb
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片过大,保证在50Kb下");
                }
                String fileName = file.getOriginalFilename();
                if(!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png格式文件");
                }
                StorePath storePath = fastdfsUtils.upload(file);
                String filePath = storePath.getFullPath();
                brandVO.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brandVO.setUpdateNo(userId);
            brandVO.setName(name);
            brandVO.setDescri(descri);
            brandVO.setSort(sort);
            return brandService.insertBrand(brandVO);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }
    
    @ApiOperation("通过id删除品牌")
    @ApiImplicitParam(name = "id", value = "id",paramType = "query",required = true)
    @GetMapping("delete")
    public ComResponse delete(@RequestParam("id") Integer id,HttpServletRequest request){
        if(id == null){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());
        }
        BrandDelVO brandDelVO = new BrandDelVO();
        brandDelVO.setId(id);
        brandDelVO.setUpdateNo(request.getHeader("userId"));
        return brandService.deleteBrandById(brandDelVO);
    }

    @ApiOperation(value = "修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = false, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称",paramType="query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事",paramType="query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "brandId", value = "品牌编号", paramType="query",required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序",paramType="query", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "url",value="url(该字段为文件为空时传入的字段，如果想删除图片则将文件和此处全部输入空值即可)",paramType = "query", required = false, dataType = "String")
    })
    @PostMapping("update")
    public ComResponse<Void> updateBrand(MultipartFile file, HttpServletRequest request,
                                         String name, String descri, int sort, int brandId,String url) {
        try {
            if(brandId<=0){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数错误!");
            }
            BrandVO brandVO = new BrandVO();
            BrandBean brand = (BrandBean) brandService.getBrandById(brandId).getData();
            if(brand==null){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"品牌不存在!");
            }if(file==null){
                if(StringUtils.isEmpty(url)){
                    brandVO.setBrandUrl(null);
                }else {
                    if (url.contains(fastDFSConfig.getUrl()+"/")){
                        brandVO.setBrandUrl(url.split(fastDFSConfig.getUrl()+"/")[1]);
                    }else {
                        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"不合法的url！");
                    }
                }
            }else{
                long size = file.getSize() / 1024; //kb
                if (size > 50) { //判断图片大小 单位Kb
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片过大,保证在50Kb下");
                }
                String fileName = file.getOriginalFilename();
                if(!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png格式文件");
                }
                if(StringUtils.isNotEmpty(brand.getBrandUrl())){
                    fastdfsUtils.delete(brand.getBrandUrl());
                }
                StorePath storePath = fastdfsUtils.upload(file);
                String filePath = storePath.getFullPath();
                brandVO.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brandVO.setUpdateNo(userId);
            brandVO.setName(name);
            brandVO.setDescri(descri);
            brandVO.setSort(sort);
            brandVO.setId(brandId);
            return brandService.updateBrand(brandVO);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }

}
