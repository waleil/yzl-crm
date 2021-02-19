package cn.net.yzl.crm.service.order;

import cn.net.yzl.order.model.vo.order.SettlementListReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface ExportOrderService {
    void exportSettlementList(SettlementListReqDTO dto, HttpServletResponse response) ;
}
