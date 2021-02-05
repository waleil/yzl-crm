package cn.net.yzl.crm.service.order;

import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OrderInvoiceService {
    void exportInvoiceList(OrderInvoiceReqDTO dto, HttpServletResponse response) throws IOException;
}
