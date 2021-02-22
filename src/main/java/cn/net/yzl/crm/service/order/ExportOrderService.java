package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedReqDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedResDTO;
import cn.net.yzl.order.model.vo.order.SettlementListReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface ExportOrderService {
    void exportSettlementList(SettlementListReqDTO dto, HttpServletResponse response) ;

    void exportSelectProductDetailBySettledOrder(ProductDetailSettlementedReqDTO dto, HttpServletResponse response);
}
