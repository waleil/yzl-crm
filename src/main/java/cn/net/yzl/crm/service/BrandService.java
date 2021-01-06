package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.vo.brand.BrandDelVO;
import cn.net.yzl.product.model.vo.brand.BrandVO;


public interface BrandService {
    ComResponse getAllBrands(Integer pageNo, Integer pageSize, String keyword);

    ComResponse getBrandById(Integer id);

    ComResponse<Void> changeBrandStatus(Integer flag, Integer id);

    ComResponse insertBrand(BrandVO brand);

    ComResponse<Void> updateBrand(BrandVO brand);

    ComResponse deleteBrandById(BrandDelVO brandDelVO);

    ComResponse<Boolean> checkUnique(String name, int type);
}
