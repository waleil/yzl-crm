package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.model.order.OrderLogistcInfo;
import cn.net.yzl.order.model.vo.order.OrderProductDTO;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public interface IOrderSearchService {
    /**
     * 查询订单基本信息 包含订单基础信息和购买人信息
     * @param orderNo
     * @return
     */
    public ComResponse<OrderInfoVO> selectOrderInfo(String orderNo);

    /**
     * 根据订单号 查询订单购买商品明细列表
     * @param orderNo
     * @return
     */
    ComResponse<List<OrderProductDTO>> selectOrderProductDetail(String orderNo);

    /**
     *
     *
     * @param orderNo
     * @param companyCode
     * @param dto
     * @return
     */
    ComResponse<OrderLogistcInfo> selectLogisticInfo(@NotEmpty(message = "订单号不能为空") String orderNo, @NotEmpty(message = "快递公司") String companyCode, String dto);
}
