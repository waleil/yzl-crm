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

    public ComResponse updateStatusByMealCode(@RequestBody @Valid ProductMealUpdateStatusRequestVO vo,HttpServletRequest request) {

        String userId;

        if(StringUtils.isEmpty(userId = request.getHeader("userId"))){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"校验用户身份失败，请您重新登陆！");
        }

        ProductMealUpdateStatusVO condition = new ProductMealUpdateStatusVO();

        condition.setUpdateNo(userId);

        if (CollectionUtils.isEmpty(vo.getMealNoList())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), "套餐code不能为空");
        }

        condition.setMealNoList(vo.getMealNoList());

        condition.setStatus(vo.getStatus());

        return mealClient.updateStatusByMealCode(condition);
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
    public ComResponse<Void> editProductMeal(@Valid @RequestBody MealRequestVO vo, HttpServletRequest request) throws IOException {

        MealVO mealVO = new MealVO();

        BeanUtils.copyProperties(vo, mealVO);

        mealVO.setMealProducts(new ArrayList<>(vo.getMealProducts().size()));

        mealVO.setUpdateTime(new Date());


        if(StringUtils.isEmpty(vo.getMealNo())){
            mealVO.setMealNo(null);
        }
        vo.getMealProducts().forEach(mealProductVO -> {
                MealProductVO mpvo = new MealProductVO();
                BeanUtils.copyProperties(mealProductVO, mpvo);
                mpvo.setCreateTime(mealVO.getMealNo()==null?null:new Date());
                mpvo.setUpdateTime(mealVO.getMealNo()==null?null:new Date());
                mealVO.getMealProducts().add(mpvo);
            });
        String userId = request.getHeader("userId");

        if(StringUtils.isEmpty(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"校验操作员失败,请重新登录!");
        }

        String url = vo.getUrl();

        if (StringUtils.isEmpty(url)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"尚未选择上传的图片，请选择！");
        }

        mealVO.setImageUrl(url);

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
