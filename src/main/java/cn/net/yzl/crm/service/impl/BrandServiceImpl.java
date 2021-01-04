package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.model.BrandBeanTO;
import cn.net.yzl.crm.service.BrandService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {
    @Override
    public ComResponse<PageInfo<BrandBeanTO>> getAllBrands(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public ComResponse getBrandById(Integer id) {
        return null;
    }

    @Override
    public ComResponse getProductByBid(Integer bid) {
        return null;
    }

    @Override
    public ComResponse<Void> changeBrandStatus(Integer flag, Integer id) {
        return null;
    }

    @Override
    public ComResponse insertBrand(BrandBean brand) {
        return null;
    }

    @Override
    public ComResponse<Void> updateBrand(BrandBean brand) {
        return null;
    }
}
