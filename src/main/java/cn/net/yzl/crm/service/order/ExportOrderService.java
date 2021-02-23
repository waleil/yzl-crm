package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedReqDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedResDTO;
import cn.net.yzl.order.model.vo.order.SettlementListReqDTO;

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
    void exportSelectgoodsInTransitlist(ProductDetailSettlementedReqDTO dto, HttpServletResponse response);
}
