package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.model.order.LogisticsInfo;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.model.order.OrderLogistcInfo;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.micservice.MemberFien;

import cn.net.yzl.crm.service.order.IOrderSearchService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.logistics.enums.OrderStatus;
import cn.net.yzl.logistics.model.ExpressFindTraceDTO;
import cn.net.yzl.logistics.model.ExpressTraceResDTO;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import cn.net.yzl.order.model.vo.order.OrderProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class OrderSearchServiceImpl implements IOrderSearchService {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private MemberFien memberFien;

    @Autowired
    private LogisticsFien logisticsFien;

    private static final int[] CAN_SEARCH_LOGIC_STATS = new int[]{OrderStatus.ORDER_STATUS_1.getCode()
            ,OrderStatus.ORDER_STATUS_2.getCode()
            ,OrderStatus.ORDER_STATUS_3.getCode()
            ,OrderStatus.ORDER_STATUS_8.getCode()};
    @Override
    public ComResponse<OrderInfoVO> selectOrderInfo(String orderNo) {
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        ComResponse<List<OrderInfoResDTO>> respons = orderSearchClient.selectOrderInfo(orderNo);
        if (respons.getCode().compareTo(Integer.valueOf(200)) != 0) {
            throw new BizException(respons.getCode(), respons.getMessage());
        }
        List<OrderInfoResDTO> list = respons.getData();
        if (list == null || list.size() == 0) {
            log.error("订单不存在，订单号：" + orderNo);
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), respons.getMessage());
        }

        orderInfoVO.setOrderInfoResDTOList(list);
        GeneralResult<Member> member = memberFien.getMember(list.get(0).getMemberCardNo());
        if (member.getCode().compareTo(Integer.valueOf(200)) != 0) {
            log.error("查询顾客信息异常，顾客卡号：" + list.get(0).getMemberCardNo());
            throw new BizException(member.getCode(), member.getMessage());
        }
        if (member.getData() == null) {
            log.error("查询顾客信息异常，顾客信息不存在，顾客卡号：" + list.get(0).getMemberCardNo());
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "顾客信息不存在");
        }
        orderInfoVO.setMember(member.getData());
        return ComResponse.success(orderInfoVO);
    }


    @Override
    public ComResponse<List<OrderProductDTO>> selectOrderProductDetail(String orderNo) {
//        ComResponse<List<OrderProductDTO>> respons = orderSearchClient.selectOrderProductDetail(orderNo);
////        if(respons.getCode().compareTo(Integer.valueOf(200)) !=0){
////            throw new BizException(respons.getCode(),respons.getMessage());
////        }

        return null;
    }

    @Override
    public ComResponse<OrderLogistcInfo> selectLogisticInfo(@NotEmpty(message = "订单号不能为空") String orderNo, @NotEmpty(message = "快递公司") String companyCode, String mailid) {

        OrderLogistcInfo orderLogistcInfo = null;
        ComResponse<OrderInfoResDTO> respons = orderSearchClient.selectOrderInfoOnly(orderNo);
        if (respons.getCode().compareTo(Integer.valueOf(200)) != 0) {
            log.error("调用订单服务失败：" + respons.getCode(),respons.getMessage());
            throw new BizException(respons.getCode(), respons.getMessage());
        }
        if (respons.getData() == null) {
            log.error("该订单不存在,订单号：" + orderNo);
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "该订单不存在");
        }
        OrderInfoResDTO order = respons.getData();
        if (Arrays.asList(CAN_SEARCH_LOGIC_STATS).contains(order.getOrderStatus())) {
            return ComResponse.success(null);
        }
        if(StringUtils.isBlank(order.getExpressNumber())){
            return ComResponse.success(null);
        }
        if (!StringUtils.isBlank(mailid) && !StringUtils.isBlank(companyCode) &&
                !StringUtils.isBlank(order.getExpressNumber()) && !mailid.equals(order.getExpressNumber())) {
            log.error("您要查询的快递号，不属于该订单，订单号：" + orderNo);
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "您要查询的快递号，不属于该订单");
        }
        orderLogistcInfo = new OrderLogistcInfo();
        orderLogistcInfo.setCompanyName(order.getExpressCompanyName());
        orderLogistcInfo.setMailId(order.getExpressNumber());
        ExpressFindTraceDTO dto = new ExpressFindTraceDTO();
        dto.setMailId(order.getExpressNumber());
        dto.setCompanyCode(order.getExpressCompanyCode());
        GeneralResult<List<ExpressTraceResDTO>> logisticsTraces = logisticsFien.findLogisticsTraces(dto);
        if (logisticsTraces.getCode().compareTo(Integer.valueOf(200)) != 0) {
            log.error("调用物流服务查询物流轨迹失败，订单号：" + orderNo,respons.getMessage());
            throw new BizException(logisticsTraces.getCode(), logisticsTraces.getMessage());
        }
        List<ExpressTraceResDTO> data = logisticsTraces.getData();
        List<LogisticsInfo> result = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (Optional.ofNullable(data).map(List::isEmpty).isPresent()) {
            data.forEach(map -> {
                LogisticsInfo info = new LogisticsInfo();
                info.setMailId(order.getExpressNumber());
                info.setCity(map.getCity());
                info.setDescription(map.getDescription());
                info.setSite(map.getSite());
                info.setStatus(map.getStatus());

                info.setTime(df.format(map.getTime()));
                result.add(info);

            });
        }

        orderLogistcInfo.setList(result);
        return ComResponse.success(orderLogistcInfo);
    }


}
