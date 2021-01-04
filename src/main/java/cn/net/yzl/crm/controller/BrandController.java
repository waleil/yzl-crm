package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.model.BrandBeanTO;
import cn.net.yzl.crm.model.ProductBean;
import cn.net.yzl.crm.service.BrandService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product/brand")
public class BrandController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "查询全部品牌")
    @GetMapping("getAllBrands")
    public ComResponse<PageInfo<BrandBeanTO>> getAllBrands(@NotNull(message = "数据不能为空！") @RequestParam("pageNo") @Valid Integer pageNo, @NotNull(message = "数据不能为空！") @RequestParam("pageSize") @Valid Integer pageSize) {
        if (pageSize>50) {
            pageSize=50;
        }
        return brandService.getAllBrands(pageNo, pageSize);
    }

    @ApiOperation(value = "通过id精确查询品牌")
    @GetMapping("getBrandById")
    public ComResponse<BrandBean> getBrandById(@RequestParam("id") @NotNull(message = "id不能为空！") @Valid Integer id) {
        ComResponse comResponse = brandService.getBrandById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "根据id查询该品牌下的所有商品")
    @GetMapping("getProductByBid")
    public ComResponse<List<ProductBean>> getProductByBid(@NotNull(message = "数据不能为空！") @RequestParam("bid") @Valid @ApiParam("品牌id") Integer bid) {
        ComResponse comResponse = brandService.getProductByBid(bid);
        List list = new ArrayList<>();
        if ((list = (List) comResponse.getData()).size() == 0||comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "修改品牌状态")
    @GetMapping("changeBrandStatus")
    public ComResponse<Void> changeBrandStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否启用（0禁用，1启用）") Integer flag, @RequestParam("id") @NotNull(message = "id不能为空！") @Valid Integer id) {
        if (flag == 1||flag==0) {
            return brandService.changeBrandStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "新增品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer") })
    @PostMapping("insertBrand")
    public ComResponse insertBrand(MultipartFile file, HttpServletRequest request,
                                   String name, String descri, int sort) {
        try {
            BrandBean brand = new BrandBean();
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
                brand.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brand.setCreateNo(userId);
            brand.setName(name);
            brand.setDescri(descri);
            brand.setSort(sort);
            return brandService.insertBrand(brand);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }

    @ApiOperation(value = "修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事", required = false, dataType = "String"),
            @ApiImplicitParam(name = "brandId", value = "品牌编号", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer") })
    @PostMapping("updateBrand")
    public ComResponse<Void> updateBrand(MultipartFile file, HttpServletRequest request,
                                         String name, String descri, int sort, int brandId) {
        try {
            if(brandId<=0){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数错误!");
            }
            BrandBean brand = (BrandBean) brandService.getBrandById(brandId).getData();
            if(brand==null){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"品牌不存在!");
            }
            if(file!=null){
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
                brand.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brand.setCreateNo(userId);
            brand.setName(name);
            brand.setDescri(descri);
            brand.setSort(sort);
            return brandService.updateBrand(brand);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }

}
