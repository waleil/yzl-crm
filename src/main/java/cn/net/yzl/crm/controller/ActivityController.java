package cn.net.yzl.crm.controller;

import cn.net.yzl.activity.model.requestModel.*;
import cn.net.yzl.activity.model.responseModel.*;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ComResponse<ProductListDiscountResponse> getProductListDiscount(@RequestBody ProductListDiscountRequest request) {
        return activityService.getProductListDiscount(request);
    }

    @ApiOperation(value = "根据商品唯一编码 查询当前的优惠方式、可用优惠券")
    @PostMapping("/v1/getProductDiscountByProductCode")
    public ComResponse<ProductDiscountResponse> getProductDiscount(@RequestBody ProductDiscountRequest request) {
        return activityService.getProductDiscount(request);
    }

    @ApiOperation(value = "计算金额")
    @PostMapping("v1/calculate")
    public ComResponse<BigDecimal> calculate(@RequestBody CalculateRequest request) {
        return activityService.calculate(request);
    }

    @ApiOperation(value = "根据单个会员卡号获取 每个顾客的优惠券 积分 红包")
    @GetMapping("v1/getAccountByMemberCard")
    public ComResponse<MemberAccountResponse> getAccountByMemberCard(@RequestParam("memberCard") String memberCard) {
        return activityService.getAccountByMemberCard(memberCard);
    }

    @ApiOperation(value = "根据单个会员卡号获取 每个顾客的优惠券 积分 红包的历史记录")
    @PostMapping("v1/getAccountHistoryByMemberCard")
    public ComResponse<Page<MemberAccountHistoryResponse>> getAccountHistoryByMemberCard(@RequestBody AccountHistoryRequest request) {
        return activityService.getAccountHistoryByMemberCard(request);
    }

    @ApiOperation(value = "顾客积分明细表")
    @PostMapping("v1/getMemberIntegralRecords")
    public ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecords(@RequestBody AccountRequest request) {
        return activityService.getMemberIntegralRecords(request);
    }

    @ApiOperation(value = "顾客红包明细表")
    @PostMapping("v1/getMemberRedBagRecords")
    public ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecords(@RequestBody AccountRequest request) {
        return activityService.getMemberRedBagRecords(request);
    }

    @ApiOperation(value = "顾客优惠券明细表")
    @PostMapping("v1/getMemberCoupon")
    public ComResponse<Page<MemberCouponResponse>> getMemberCoupon(@RequestBody AccountRequest request) {
        return activityService.getMemberCoupon(request);
    }

}
