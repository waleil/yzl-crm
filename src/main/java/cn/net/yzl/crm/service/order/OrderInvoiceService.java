package cn.net.yzl.crm.service.order;

import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface OrderInvoiceService {
    void exportInvoiceList(OrderInvoiceReqDTO dto, HttpServletResponse response);
}
