package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.model.BrandBeanTO;
import com.github.pagehelper.PageInfo;

public interface BrandService {
    ComResponse<PageInfo<BrandBeanTO>> getAllBrands(Integer pageNo, Integer pageSize);

    ComResponse getBrandById(Integer id);

    ComResponse getProductByBid(Integer bid);

    ComResponse<Void> changeBrandStatus(Integer flag, Integer id);

    ComResponse insertBrand(BrandBean brand);

    ComResponse<Void> updateBrand(BrandBean brand);
}
