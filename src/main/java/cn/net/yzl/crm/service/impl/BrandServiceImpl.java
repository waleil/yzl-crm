package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.BrandClient;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.service.BrandService;
import cn.net.yzl.product.model.vo.brand.BrandDelVO;
import cn.net.yzl.product.model.vo.brand.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandClient brandClient;


    @Override
    public ComResponse getAllBrands(Integer pageNo, Integer pageSize, String keyword) {
        return ComResponse.success(brandClient.getAllBrands(pageNo, pageSize,keyword));
    }

    @Override
    public ComResponse getBrandById(Integer id) {
        ComResponse<BrandBean> brands = brandClient.getBrandById(id);
        return brands;
    }


    @Override
    public ComResponse<Void> changeBrandStatus(Integer flag, Integer id) {
        ComResponse comResponse = brandClient.changeBrandStatus(flag, id);
        return comResponse;
    }

    @Override
    public ComResponse insertBrand(BrandVO brand) {
        ComResponse comResponse = brandClient.editBrand(brand);
        return comResponse;
    }

    @Override
    public ComResponse<Void> updateBrand(BrandVO brand) {
        ComResponse comResponse = brandClient.editBrand(brand);
        return comResponse;
    }

    @Override
    public ComResponse deleteBrandById(BrandDelVO brandDelVO) {
        return brandClient.deleteById(brandDelVO);
    }
}
