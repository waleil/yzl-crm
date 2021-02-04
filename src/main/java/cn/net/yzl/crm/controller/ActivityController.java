package cn.net.yzl.crm.controller;

import cn.net.yzl.activity.model.requestModel.ProductDiscountRequest;
import cn.net.yzl.activity.model.requestModel.ProductListDiscountRequest;
import cn.net.yzl.activity.model.responseModel.ProductDiscountResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/2/4 11:55
 * @desc: //TODO  请说明该类的用途
 **/
@Slf4j
@RestController
@RequestMapping("activity/")
@Api(tags = "商品活动相关")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @ApiOperation(value = "根据多个商品唯一编码 查询当前的优惠方式、可用优惠券")
    @PostMapping("/v1/getProductDiscountByProductCodes")
    public ComResponse<List<ProductDiscountResponse>> getProductListDiscount(@RequestBody ProductListDiscountRequest request) {
        return activityService.getProductListDiscount(request);
    }

    @ApiOperation(value = "根据商品唯一编码 查询当前的优惠方式、可用优惠券")
    @PostMapping("/v1/getProductDiscountByProductCode")
    public ComResponse<ProductDiscountResponse> getProductDiscount(@RequestBody ProductDiscountRequest request) {
        return activityService.getProductDiscount(request);
    }
}
