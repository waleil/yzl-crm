package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    @Resource
    private OrderInvoiceClient orderInvoiceClient;


    @Override
    public void exportInvoiceList(OrderInvoiceReqDTO dto, HttpServletResponse response) {
        ComResponse<Page<OrderInvoiceListDTO>> res = orderInvoiceClient.selectInvoiceList(dto);
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
            throw new BizException(res.getCode(),res.getMessage());
        }
        if(res.getData() == null || CollectionUtils.isEmpty(res.getData().getItems())){
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),
                   "没有符合条件的数据");
        }


    }


}
