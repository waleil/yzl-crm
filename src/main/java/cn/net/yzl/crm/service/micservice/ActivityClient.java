package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.activity.model.requestModel.CalculateRequest;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.ProductDiscountRequest;
import cn.net.yzl.activity.model.requestModel.ProductListDiscountRequest;
import cn.net.yzl.activity.model.responseModel.ProductDiscountResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.dmc.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(name = "activityDB", url = "${api.gateway.url}/activityDB")
public interface ActivityClient {

    Logger logger = LoggerFactory.getLogger(ActivityClient.class);


    @ApiOperation(value = "根据多个商品唯一编码 查询当前的优惠方式、可用优惠券")
    @PostMapping("db/v1/getProductDiscountByProductCodes")
    ComResponse<List<ProductDiscountResponse>> getProductListDiscount(@RequestBody ProductListDiscountRequest request);

    @ApiOperation(value = "根据商品唯一编码 查询当前的优惠方式、可用优惠券")
    @PostMapping("db/v1/getProductDiscountByProductCode")
    ComResponse<ProductDiscountResponse> getProductDiscount(@RequestBody ProductDiscountRequest request);

    @ApiOperation(value = "计算金额")
    @PostMapping("db/v1/calculate")
    ComResponse<BigDecimal> calculate(@RequestBody CalculateRequest request);

    @ApiOperation(value = "校验订单金额")
    @PostMapping("db/v1/checkOrderAmount")
    ComResponse<Boolean> checkOrderAmount(@RequestBody CheckOrderAmountRequest request);

    @GetMapping("db/v1/launchManage/getAllLaunchManage")
    ComResponse<List<LaunchManageDto>> getAllLaunchManage();

    @GetMapping("db/v1/launchManage/getLaunchManageByRelationBusNo")
    ComResponse<List<LaunchManageDto>> getLaunchManageByRelationBusNo(@RequestParam("relationBusNo") Long relationBusNo);

    @ApiOperation(value = "商品促销活动-获取所有的活动")
    @GetMapping("db/v1/productSales/getActivityList")
    ComResponse<List<ActivityDetailResponse>> getActivityList();

    @ApiOperation(value = "会员管理-会员级别管理-会员级别设置列表")
    @PostMapping("db/v1/memberLevelManager/getMemberLevelPages")
    ComResponse<Page<MemberLevelResponse>> getMemberLevelPages(@RequestBody PageModel request);

    @ApiOperation(value = "根据员工群Id 查询任务")
    @GetMapping("db/v1/getMarketTarget")
    ComResponse<TaskDto> getMarketTarget(@RequestParam("groupId") Long staffGroupBusNo);

    default TaskDto getMarketTargetDefault(Long groupId) {
        try {
            ComResponse<TaskDto> comResponse = getMarketTarget(groupId);
            if (null == comResponse || 200 != comResponse.getCode()) {
                return null;
            }
            return comResponse.getData();
        } catch (Exception e) {
            logger.error("根据员工群Id 查询任务：", e);
        }
        return null;
    }

}
