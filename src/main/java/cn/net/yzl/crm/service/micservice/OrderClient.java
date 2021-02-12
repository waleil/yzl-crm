package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.staff.dto.lasso.OrderCalculationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/26 0:16
 * @desc: //TODO  请说明该类的用途
 **/
@FeignClient(name = "orderService",url = "${api.gateway.url}/orderService")
public interface OrderClient {

    @PostMapping("/order/v1/querystaffcodes")
    ComResponse<List<String>> querystaffcodes(OrderCalculationDto orderCalculationDto);



    default List<String> getStaffOrderList(OrderCalculationDto orderCalculationDto) {
        ComResponse<List<String>> staffScheduleInfo = querystaffcodes(orderCalculationDto);
        if (null != staffScheduleInfo && staffScheduleInfo.getCode() == 200) {
            return staffScheduleInfo.getData();
        }
        return Collections.emptyList();
    }
}
