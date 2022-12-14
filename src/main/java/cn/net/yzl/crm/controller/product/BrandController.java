package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.service.product.BrandService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.db.BrandBean;
import cn.net.yzl.product.model.vo.brand.BrandBeanTO;
import cn.net.yzl.product.model.vo.brand.BrandDelVO;
import cn.net.yzl.product.model.vo.brand.BrandVO;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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
@Slf4j
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
    public ComResponse<?> getAllBrands(@RequestParam(required = false,defaultValue = "1",value = "pageNo") Integer pageNo,
                                    @RequestParam(required = false,defaultValue = "15",value = "pageSize") Integer pageSize,
                                    String keyword) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize <= 0){
            pageSize = 10;
        }
        if(StringUtils.isBlank(keyword)){
            keyword = null;
        }
        return brandService.getAllBrands(pageNo, pageSize,keyword);
    }

    @ApiOperation(value = "通过id精确查询品牌")
    @GetMapping("selectById")
    @ApiImplicitParam(name = "id", value = "主键信息", required = true, dataType = "Integer",paramType = "query")
    public ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id) {
        ComResponse<?> comResponse = brandService.getBrandById(id);
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
        if(id == null||id < 1){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"不合法的id类型！");
        }
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
    public ComResponse<?> insertBrand(@RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request,
                                      String name, @RequestParam(value = "descri",required = false) String descri,@RequestParam(value = "sort",required = false,defaultValue = "0") Integer sort) {
        //新增失败的回滚url
        String path = "";
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
                path = filePath;
                brandVO.setBrandUrl(filePath);
            }
            String userNo= QueryIds.userNo.get();
            if (StringUtils.isBlank(userNo)) {
                return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户登录状态，请尝试重新登陆！");
            }
            brandVO.setUpdateNo(userNo);
            brandVO.setName(name);
            brandVO.setDescri(descri);
            brandVO.setSort(sort);
            return brandService.insertBrand(brandVO);
        } catch (IOException e) {
            e.printStackTrace();
            if(!StringUtils.isBlank(path)){
                fastdfsUtils.delete(path);
            }
            return ComResponse.fail(0,"添加失败");
        }
    }
    
    @ApiOperation("通过id删除品牌")
    @ApiImplicitParam(name = "id", value = "id",paramType = "query",required = true)
    @GetMapping("delete")
    public ComResponse<?> delete(@RequestParam("id") Integer id,HttpServletRequest request){
        if(id == null){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());
        }
        BrandDelVO brandDelVO = new BrandDelVO();
        brandDelVO.setId(id);
        String userNo= QueryIds.userNo.get();
        if (StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"获取用户登录信息失败，请尝试重新登陆！");
        }
        brandDelVO.setUpdateNo(userNo);
        return brandService.deleteBrandById(brandDelVO);
    }

    @ApiOperation(value = "修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = false, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称",paramType="query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事",paramType="query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "brandId", value = "品牌编号", paramType="query",required = false, dataType = "Integer",readOnly = true),
            @ApiImplicitParam(name = "sort", value = "排序",paramType="query", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "url",value="url(该字段为文件为空时传入的字段，如果想删除图片则将文件和此处全部输入空值即可)",paramType = "query", required = false, dataType = "String")
    })
    @PostMapping("update")
    public ComResponse<Void> updateBrand(MultipartFile file, HttpServletRequest request,
                                         String name, String descri, @RequestParam(defaultValue = "0")Integer sort, Integer brandId,String url) {
        String userNo= QueryIds.userNo.get();
        //修改失败的回滚url
        String path = "";
        if (StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户登录信息，请尝试重新登陆！");
        }
        if (StringUtils.isBlank(name)){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"品牌名称不能为空！");
        }

        try {
            if(brandId<=0){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数错误!");
            }
            BrandVO brandVO = new BrandVO();
            BrandBean brand = (BrandBean) brandService.getBrandById(brandId).getData();
            if(brand==null){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"品牌不存在!");
            }if(file==null){
                if(StringUtils.isBlank(url)){
                    brandVO.setBrandUrl("");
                }else {
                    if (url.contains(fastDFSConfig.getUrl()+"/")){
                        brandVO.setBrandUrl(url.split(fastDFSConfig.getUrl()+"/")[1]);
                    }else {
                        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"不合法的url！");
                    }
                }
            }else{
                long size = file.getSize();
                if (size > 50 << 10) { //判断图片大小 (50*1024)B
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片过大,保证在50Kb下");
                }
                String fileName = file.getOriginalFilename();
                if(!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png格式文件");
                }
                StorePath storePath = fastdfsUtils.upload(file);
                String filePath = storePath.getFullPath();
                path = filePath;
                brandVO.setBrandUrl(filePath);
            }
            brandVO.setUpdateNo(userNo);
            brandVO.setName(name);
            brandVO.setDescri(descri);
            brandVO.setSort(sort);
            brandVO.setId(brandId);
            return brandService.updateBrand(brandVO);
        } catch (IOException e) {
            e.printStackTrace();
            if(!StringUtils.isBlank(path)){
                try {
                    fastdfsUtils.delete(path);
                }catch (Exception ex) {
                    log.warn("错误码：2，错误信息：找不到节点或文件");
                }
            }
            return ComResponse.fail(0,"添加/修改失败失败");
        }
    }

    @ApiOperation("前端品牌名称blur事件查重接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "需要查重的名称",required = true,paramType = "query"),
            @ApiImplicitParam(name = "id",value = "需要输入id，如新增操作则输入0即可",required = true,paramType = "query")
    })
    @GetMapping("checkUnique")
    public ComResponse<Boolean> checkUnique(@RequestParam("name")String name,@RequestParam("id")Integer id){
        if (id == null || id < 1) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"不合法的id格式！");
        }
        if (StringUtils.isBlank(name)){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"需要校验的名称不能为空！");
        }
        return brandService.checkUnique(name, id);
    }


    @ApiOperation(value = "下拉列表查询", notes = "下拉列表查询")
    @GetMapping("v1/query4Select")
    public ComResponse<List<BrandBeanTO>> query4Select(){
        return brandService.query4Select();
    }

}
