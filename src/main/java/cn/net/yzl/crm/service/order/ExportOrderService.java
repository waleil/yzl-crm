package cn.net.yzl.crm.service.order;

import cn.net.yzl.order.model.vo.order.*;

import javax.servlet.http.HttpServletResponse;

public interface ExportOrderService {
    /**
     * 导出结算单
     * @param dto
     * @param response
     */
    void exportSettlementList(SettlementListReqDTO dto, HttpServletResponse response) ;
    /**
     * 导出已对账的商品明细
     * @param dto
     * @param response
     */
    void exportSelectProductDetailBySettledOrder(ProductDetailSettlementedReqDTO dto, HttpServletResponse response);

    /**
     * 导出在途商品列表
     * @param dto
     * @param response
     */
    void exportSelectgoodsInTransitlist(GoodsInTransitReqDTO dto, HttpServletResponse response);

    /**
     * 导出商品销售明细
     * @param dto
     * @param response
     */
    void sexportselectOrderSaleDetail(OrderSellDetailReqDTO dto, HttpServletResponse response);
}
