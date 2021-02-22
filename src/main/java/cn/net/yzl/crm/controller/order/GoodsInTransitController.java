package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.GoodsInTransitFeign;
import cn.net.yzl.order.model.mongo.order.GoodsInTransit;
import cn.net.yzl.order.model.mongo.order.ProductDetailDTO;
import cn.net.yzl.order.model.vo.order.GoodsInTransitReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "结算中心")
@RestController
@RequestMapping("/goodsintransit")
@Slf4j
public class GoodsInTransitController {

    @Autowired
    private GoodsInTransitFeign goodsInTransitdetails;



    @ApiOperation(value = "查询在途商品明细")
    @PostMapping("v1/goodsInTransitdetails")
    public ComResponse<Page<ProductDetailDTO>>  goodsInTransitdetail (@RequestBody GoodsInTransitReqDTO dto ){
        return goodsInTransitdetails.goodsInTransitdetail(dto);
    }

    @ApiOperation(value = "查询在途商品")
    @PostMapping("v1/selectgoodsInTransitlist")
    public ComResponse<Page<GoodsInTransit>>  selectgoodsInTransitlist (@RequestBody GoodsInTransitReqDTO dto ){
        return goodsInTransitdetails.selectgoodsInTransitlist(dto);
    }

    @ApiOperation(value = "查询在途商品总数量")
    @PostMapping("v1/selectgoodsInTransitTotalCount")
    public ComResponse<Integer>  selectgoodsInTransitTotalCount (@RequestBody GoodsInTransitReqDTO dto ){
        return goodsInTransitdetails.selectgoodsInTransitTotalCount(dto);
    }

}
