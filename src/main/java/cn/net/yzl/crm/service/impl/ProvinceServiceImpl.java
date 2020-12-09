package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.bean.DataSourceSelector;
import cn.net.yzl.crm.bean.DynamicDataSourceEnum;
import cn.net.yzl.crm.dao.ProvinceMapper;
import cn.net.yzl.crm.model.Province;
import cn.net.yzl.crm.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 19:04
 * @description:
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {


    @Autowired
    ProvinceMapper provinceMapper;

    @Override
    public void saveProvince(Province province) {
            provinceMapper.saveProvince(province);
    }

    @DataSourceSelector(value = DynamicDataSourceEnum.slave)
    @Override
    public Province getProvince() {
        return provinceMapper.getProvince();
    }
}
