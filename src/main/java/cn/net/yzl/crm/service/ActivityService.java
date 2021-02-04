package cn.net.yzl.crm.service;

import cn.net.yzl.activity.model.requestModel.ProductDiscountRequest;
import cn.net.yzl.activity.model.requestModel.ProductListDiscountRequest;
import cn.net.yzl.activity.model.responseModel.ProductDiscountResponse;
import cn.net.yzl.common.entity.ComResponse;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/2/4 11:58
 * @desc: //TODO  请说明该类的用途
 **/
public interface ActivityService {
    ComResponse<List<ProductDiscountResponse>> getProductListDiscount(ProductListDiscountRequest request);

    ComResponse<ProductDiscountResponse> getProductDiscount(ProductDiscountRequest request);
}
