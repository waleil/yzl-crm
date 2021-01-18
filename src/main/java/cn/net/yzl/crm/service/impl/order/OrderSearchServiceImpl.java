package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.IOrderSearchService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import cn.net.yzl.order.model.vo.order.OrderProductDTO;
import cn.net.yzl.product.model.db.Meal;
import cn.net.yzl.product.model.vo.product.vo.ProductMealDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSearchServiceImpl implements IOrderSearchService {

    @Autowired
    private OrderSearchClient orderSearchClient;
    
    @Autowired
    private MemberFien memberFien;

    @Autowired
    private MealClient mealClient;
    
    @Override
    public ComResponse<OrderInfoVO> selectOrderInfo(String orderNo) {
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        ComResponse<OrderInfoResDTO> respons = orderSearchClient.selectOrderInfo(orderNo);
        if(respons.getCode().compareTo(Integer.valueOf(200)) !=0){
            throw new BizException(respons.getCode(),respons.getMessage());
        }
        OrderInfoResDTO orderInfoResDTO = respons.getData();
        if(orderInfoResDTO == null ){
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),respons.getMessage());
        }

        orderInfoVO.setOrderInfoResDTO(orderInfoResDTO);
        orderInfoResDTO.setMemberCardNo("100000001");
        GeneralResult<Member> member = memberFien.getMember(orderInfoResDTO.getMemberCardNo());
        if(member.getCode().compareTo(Integer.valueOf(200)) !=0){
            throw new BizException(respons.getCode(),respons.getMessage());
        }
        if(member.getData() == null){
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),"顾客信息不存在");
        }
        orderInfoVO.setMember(member.getData());
        return ComResponse.success(orderInfoVO);
    }

    @Override
    public ComResponse<List<OrderProductDTO>> selectOrderProductDetail(String orderNo) {
        ComResponse<List<OrderProductDTO>> respons = orderSearchClient.selectOrderProductDetail(orderNo);
        if(respons.getCode().compareTo(Integer.valueOf(200)) !=0){
            throw new BizException(respons.getCode(),respons.getMessage());
        }
        List<OrderProductDTO> list = respons.getData();
        list.forEach(map->{
            if(map.getMealFlag() == 1){
                Meal meal = new Meal();
                meal.setMealNo(map.getMealNo());
                ComResponse<ProductMealDetailVO> mealRes = mealClient.queryMealDetail(meal);
                if(mealRes.getCode().compareTo(Integer.valueOf(200)) !=0){
                    throw new BizException(mealRes.getCode(),mealRes.getMessage());
                }
                Integer mealPrice = mealRes.getData().getPrice();
                map.setProductCount(map.getTotal()/mealPrice);
                map.setProductUnitPrice(mealPrice);

            }
        });

        return null;
    }
}
