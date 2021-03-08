package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.db.order.ProductDetailDTO;
import cn.net.yzl.order.model.db.order.ProductDetailResDTO;
import cn.net.yzl.order.model.mongo.order.GoodsInTransit;
import cn.net.yzl.order.model.vo.order.GoodsInTransitReqDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "goodsintransit",url = "${api.gateway.url}/orderService/goodsintransit")
@FeignClient(name = "goodsintransit",url = "localhost:4455/goodsintransit")
public interface GoodsInTransitFeign {



    @ApiOperation(value = "查询在途商品明细")
    @PostMapping("v1/goodsInTransitdetails")
    public ComResponse<Page<ProductDetailResDTO>>  goodsInTransitdetail (@RequestBody GoodsInTransitReqDTO dto );

    @ApiOperation(value = "查询在途商品")
    @PostMapping("v1/selectgoodsInTransitlist")
    public ComResponse<Page<GoodsInTransit>>  selectgoodsInTransitlist (@RequestBody GoodsInTransitReqDTO dto );

    @ApiOperation(value = "查询在途商品总数量")
    @PostMapping("v1/selectgoodsInTransitTotalCount")
    public ComResponse<Integer>  selectgoodsInTransitTotalCount (@RequestBody GoodsInTransitReqDTO dto );

}
