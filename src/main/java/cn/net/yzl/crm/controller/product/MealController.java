package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.model.MealRequestVO;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.product.model.vo.product.dto.MealDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.*;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("productMeal")
@Api(tags = "套餐服务")
public class MealController {

    @Autowired
    private MealClient mealClient;


    @Autowired
    private FastdfsUtils fastdfsUtils;
    
    @Autowired
    private FastDFSConfig fastDFSConfig;


    /**
     * @param
     * @Author: wanghuasheng
     * @Description:
     * @Date: 2021/1/9 09:20 上午
     * @Return: cn.net.yzl.common.entity.ComResponse<java.util.List < cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO>>
     */
    @GetMapping(value = "v1/queryCountByStatus")
    @ApiOperation("按照上下架状态查询商品套餐数量")
    public ComResponse<List<ProductStatusCountDTO>> queryCountByStatus() {
       return mealClient.queryCountByStatus();
    }


    @GetMapping(value = "v1/queryPageProductMeal")
    @ApiOperation("分页查询商品套餐列表")
    public ComResponse<Page<ProductMealListDTO>> queryListProductMeal(ProductMealSelectVO vo) {
        return mealClient.queryListProductMeal(vo);
    }
    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:12 上午
     * @param：
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateStatus")
    @ApiOperation("修改套餐上下架状态")

    public ComResponse updateStatusByMealCode(@RequestBody @Valid ProductMealUpdateStatusVO vo) {
        if (CollectionUtils.isEmpty(vo.getMealNoList())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), "套餐code不能为空");
        }
        return mealClient.updateStatusByMealCode(vo);
    }

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:30
     * @param :
     * @return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     **/
    @PostMapping(value = "v1/edit")
    @ApiOperation("编辑套餐")
    public ComResponse<Void> editProductMeal(@Valid MealRequestVO vo,MultipartFile file, HttpServletRequest request) throws IOException {

        MealVO mealVO = new MealVO();

        BeanUtils.copyProperties(vo, mealVO);

        String no = vo.getMealNo();

        mealVO.setMealProducts(new ArrayList<>(vo.getMealProducts().size()));

        mealVO.setUpdateTime(new Date());

        if(StringUtils.isEmpty(no)){
            mealVO.setMealNo(null);
            vo.getMealProducts().forEach(mealProductVO -> {
                MealProductVO mpvo = new MealProductVO();
                BeanUtils.copyProperties(mealProductVO, mpvo);
                mpvo.setCreateTime(new Date());
                mealVO.getMealProducts().add(mpvo);
            });
        }else{
            vo.getMealProducts().forEach(mealProductVO -> {
                MealProductVO mpvo = new MealProductVO();
                BeanUtils.copyProperties(mealProductVO, mpvo);
                mpvo.setUpdateTime(new Date());
                mealVO.getMealProducts().add(mpvo);
            });
        }

        if(StringUtils.isEmpty(vo.getUrl())&&file==null){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片为空!");
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
            StorePath storePath = fastdfsUtils.upload(file);
            String filePath = storePath.getFullPath();
            mealVO.setImageUrl(filePath);
        }

        String userId = request.getHeader("userId");

        if(StringUtils.isEmpty(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"校验操作员失败,请重新登录!");
        }

        mealVO.setUpdateNo(userId);

        System.out.println(JsonUtil.toJsonStr(mealVO));

        return mealClient.editProductMeal(mealVO);
    }



//    /**
//     * @Description:
//     * @Author: dongjunmei
//     * @Date: 2021-01-09 13:00
//     * @param:
//     * @return: cn.net.yzl.common.entity.ComResponse
//     **/
//    @PostMapping(value = "v1/queryMealDetail")
//    @ApiOperation("查询商品详情")
//    public ComResponse<ProductMealDetailVO> queryMealDetail(@RequestBody Meal meal) {
//        if(meal.getMealNo()!=null){
//            return mealClient.queryMealDetail(meal);
//        }
//        return null;
//    }

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 13:26
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateTime")
    @ApiOperation("修改套餐售卖时间")
    ComResponse updateTimeByMealCode(@RequestBody @Valid ProductMealUpdateTimeVO vo) {

        return mealClient.updateTimeByMealCode(vo);
    }

    @GetMapping(value = "v1/queryProductMealPortray")
    @ApiOperation("查询商品套餐画像")
    public ComResponse<MealDTO> queryProductMealPortray(@RequestParam("mealNo") String mealNo) {
        return mealClient.queryProductMealPortray(mealNo).setMessage(fastDFSConfig.getUrl());
    }
    

}
