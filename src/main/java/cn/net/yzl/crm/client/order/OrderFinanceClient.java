package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.db.order.OrderFinance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "OrderFinanceClient", url = "${api.gateway.url}/staffDB")
//@FeignClient(name = "OrderFinanceClient", url = "localhost:38080/")
public interface OrderFinanceClient {

    /**
     * 财务归属
     *
     * @param
     * @return
     */
    @GetMapping("/depart/getAllFinanceDepart")

    ComResponse<List<OrderFinance>> getOrderFinanceList();
}
