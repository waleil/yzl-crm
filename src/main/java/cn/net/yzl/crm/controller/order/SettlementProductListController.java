package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedReqDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedResDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedTotalResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 以商品为维度的结算商品列表
 *
 * @author chengyu
 * @since 2021-02-05 20:40:53
 */
@RestController
@RequestMapping("settlement")
@Api(tags = "结算中心")
public class SettlementProductListController {


    @Resource
    private SettlementFein settlementFein;

    @PostMapping("v1/selectProductDetailBySettledOrder")
    @ApiOperation("以商品为维度的结算商品列表")
    public ComResponse<Page<ProductDetailSettlementedResDTO>> selectProductDetailBySettledOrder(@RequestBody ProductDetailSettlementedReqDTO dto){

        return settlementFein.selectProductDetailBySettledOrder(dto);

    }

    @PostMapping("v1/selectProductDetailBySettledOrderTotal")
    @ApiOperation("以商品为维度的结算商品列表汇总信息")
    public ComResponse<ProductDetailSettlementedTotalResDTO> selectProductDetailBySettledOrderTotal(@RequestBody ProductDetailSettlementedReqDTO dto){

        return settlementFein.selectProductDetailBySettledOrderTotal(dto);

    }


}